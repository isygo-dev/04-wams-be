package eu.isygoit.service.impl;

import eu.isygoit.annotation.*;
import eu.isygoit.com.rest.service.impl.FileService;
import eu.isygoit.config.AppProperties;
import eu.isygoit.constants.DomainConstants;
import eu.isygoit.enums.IEnumDocTempStatus;
import eu.isygoit.model.*;
import eu.isygoit.model.Document;
import eu.isygoit.model.schema.SchemaColumnConstantName;
import eu.isygoit.remote.dms.DmsLinkedFileService;
import eu.isygoit.remote.kms.KmsIncrementalKeyService;
import eu.isygoit.repository.DocumentRepository;
import eu.isygoit.service.IDocumentService;
import eu.isygoit.util.CustomMultipartFile;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HashSet;

@Slf4j
@Service
@Transactional
@DmsLinkFileService(DmsLinkedFileService.class)
@CodeGenLocal(value = NextCodeService.class)
@CodeGenKms(value = KmsIncrementalKeyService.class)
@SrvRepo(value = DocumentRepository.class)
public class DocumentService extends FileService<Long, Document, DocumentRepository> implements IDocumentService {

    private final AppProperties appProperties;
    private final TemplateService templateService;
    private  final  DmsLinkedFileService dmsLinkedFileService ;
    public DocumentService(AppProperties appProperties, TemplateService templateService, DmsLinkedFileService dmsLinkedFileService) {
        this.appProperties = appProperties;
        this.templateService = templateService;
        this.dmsLinkedFileService = dmsLinkedFileService;
    }

    @Override
    public AppNextCode initCodeGenerator() {
        return AppNextCode.builder()
                .domain(DomainConstants.DEFAULT_DOMAIN_NAME)
                .entity(Document.class.getSimpleName())
                .attribute(SchemaColumnConstantName.C_CODE)
                .prefix("DOC")
                .valueLength(6L)
                .value(1L)
                .build();
    }



    @Override
    public Document createFromTemplate(Long templateId, String content, String name) throws IOException {
        Template template = templateService.findById(templateId);
        if (template == null) {
            throw new EntityNotFoundException("Template not found with id: " + templateId);
        }

        Document doc = Document.builder()
                .name((name != null && !name.isBlank()) ? name : template.getName() + " - Copy")
                .description(template.getDescription())
                .isTemplateCopy(true)
                .originalDocumentId(templateId)
                .editionDate(LocalDateTime.now())
                .content(content != null ? content : "")
                .shared(false)
                .domain(DomainConstants.DEFAULT_DOMAIN_NAME)
                .tempType(IEnumDocTempStatus.Types.EDITING)
                .sharedWithUsers(new HashSet<>())
                .comments(new HashSet<>())
                .build();

        File tmp = File.createTempFile("html2doc_", ".docx");
        try {
            convertHtmlToDocx(content, tmp.getAbsolutePath());
            MultipartFile mp = convertFileToMultipart(tmp.getAbsolutePath());
            doc = createWithFile(doc.getDomain(), doc, mp);
        } finally {
            if (tmp.exists()) tmp.delete();
        }
        return doc;
    }


    public void convertHtmlToDocx(String html, String outputPath) throws IOException {
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(html);
        XWPFDocument docx = new XWPFDocument();

        setDefaultFont(docx);

        Element body = jsoupDoc.body();
        for (Node node : body.childNodes()) {
            processNode(docx, node);
        }

        try (FileOutputStream out = new FileOutputStream(outputPath)) {
            docx.write(out);
        }
    }

    private void setDefaultFont(XWPFDocument docx) {
        log.debug("Default font will be applied to individual runs");
    }

    private void processNode(XWPFDocument docx, Node node) {
        if (node instanceof TextNode tn) {
            String text = tn.text().trim();
            if (!text.isEmpty()) {
                XWPFParagraph p = docx.createParagraph();
                applyDefaultParagraphSpacing(p);
                XWPFRun r = p.createRun();
                r.setText(text);
                applyDefaultRunStyle(r);
            }
        } else if (node instanceof Element el) {
            String tag = el.tagName().toLowerCase();
            switch (tag) {
                case "h1", "h2", "h3", "h4", "h5", "h6" -> createHeading(docx, el);
                case "p" -> createParagraph(docx, el);
                case "div" -> createDiv(docx, el);
                case "ul", "ol" -> createList(docx, el, tag.equals("ol"));
                case "table" -> createTable(docx, el);
                case "img" -> insertImage(docx.createParagraph(), el);
                case "br" -> docx.createParagraph();
                default -> {
                    for (Node child : el.childNodes()) processNode(docx, child);
                }
            }
        }
    }

    private void createHeading(XWPFDocument docx, Element el) {
        int level = Integer.parseInt(el.tagName().substring(1));
        XWPFParagraph p = docx.createParagraph();
        applyHeadingSpacing(p);

        XWPFRun r = p.createRun();
        r.setBold(true);
        int fontSize = switch (level) {
            case 1 -> 16;
            case 2 -> 14;
            case 3 -> 12;
            case 4 -> 11;
            default -> 10;
        };
        r.setFontSize(fontSize);
        r.setFontFamily("Calibri");
        r.setText(el.text());

        applyStylesFromElement(r, el);
    }

    private void createParagraph(XWPFDocument docx, Element el) {
        XWPFParagraph p = docx.createParagraph();
        applyDefaultParagraphSpacing(p);

        String style = el.attr("style");
        if (style.contains("text-align:center")) {
            p.setAlignment(ParagraphAlignment.CENTER);
        } else if (style.contains("text-align:right")) {
            p.setAlignment(ParagraphAlignment.RIGHT);
        } else {
            p.setAlignment(ParagraphAlignment.LEFT);
        }

        for (Node child : el.childNodes()) {
            applyInline(p, child);
        }

        if (p.getRuns().isEmpty()) {
            XWPFRun r = p.createRun();
            r.setText(" ");
            applyDefaultRunStyle(r);
        }
    }


    private void createDiv(XWPFDocument docx, Element el) {
        if (!el.ownText().isBlank()) {
            XWPFParagraph p = docx.createParagraph();
            applyDefaultParagraphSpacing(p);
            XWPFRun r = p.createRun();
            r.setText(el.ownText());
            applyDefaultRunStyle(r);
        }
        for (Node child : el.childNodes()) {
            processNode(docx, child);
        }
    }


    private void createList(XWPFDocument docx, Element listEl, boolean ordered) {
        Elements listItems = listEl.select("li");

        for (int i = 0; i < listItems.size(); i++) {
            Element li = listItems.get(i);
            XWPFParagraph p = docx.createParagraph();

            XWPFRun bulletRun = p.createRun();
            if (ordered) {
                bulletRun.setText((i + 1) + ". ");
            } else {
                bulletRun.setText("• ");
            }
            applyDefaultRunStyle(bulletRun);

            applyListSpacing(p);

            for (Node child : li.childNodes()) {
                applyInline(p, child);
            }
        }
    }

    private void createTable(XWPFDocument docx, Element tableEl) {
        XWPFTable tbl = docx.createTable();
        tbl.removeRow(0);

        tbl.setWidth("100%");

        tbl.setTableAlignment(TableRowAlign.CENTER);

        Elements rows = tableEl.select("tr");
        for (int rowIndex = 0; rowIndex < rows.size(); rowIndex++) {
            Element rowEl = rows.get(rowIndex);
            XWPFTableRow row = tbl.createRow();

            Elements cells = rowEl.select("th, td");

            while (row.getTableCells().size() < cells.size()) {
                row.addNewTableCell();
            }

            for (int cellIndex = 0; cellIndex < cells.size(); cellIndex++) {
                Element cellEl = cells.get(cellIndex);
                XWPFTableCell cell = row.getCell(cellIndex);

                applyCellStyling(cell, cellEl);

                cell.removeParagraph(0);

                if (cellEl.children().isEmpty() && !cellEl.text().trim().isEmpty()) {
                    XWPFParagraph p = cell.addParagraph();
                    applyTableCellParagraphSpacing(p);
                    XWPFRun r = p.createRun();
                    r.setText(cellEl.text().trim());
                    applyDefaultRunStyle(r);

                    if ("th".equals(cellEl.tagName())) {
                        r.setBold(true);
                    }

                    applyStylesFromElement(r, cellEl);
                } else {
                    for (Node child : cellEl.childNodes()) {
                        processTableCellNode(cell, child);
                    }
                }
            }
        }

        applyTableBorders(tbl);
    }

    private void applyCellStyling(XWPFTableCell cell, Element cellEl) {
        String style = cellEl.attr("style");

        if (style.contains("background-color")) {
        }

        cell.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);
    }

    private void processTableCellNode(XWPFTableCell cell, Node node) {
        if (node instanceof TextNode tn) {
            String text = tn.text().trim();
            if (!text.isEmpty()) {
                XWPFParagraph p = cell.addParagraph();
                applyTableCellParagraphSpacing(p);
                XWPFRun r = p.createRun();
                r.setText(text);
                applyDefaultRunStyle(r);
            }
        } else if (node instanceof Element el) {
            if ("img".equals(el.tagName())) {
                XWPFParagraph p = cell.addParagraph();
                insertImage(p, el);
            } else {
                XWPFParagraph p = cell.addParagraph();
                applyTableCellParagraphSpacing(p);
                for (Node child : el.childNodes()) {
                    applyInline(p, child);
                }
            }
        }
    }

    private void applyTableBorders(XWPFTable table) {
        try {
            table.setInsideHBorder(XWPFTable.XWPFBorderType.SINGLE, 4, 0, "cccccc");
            table.setInsideVBorder(XWPFTable.XWPFBorderType.SINGLE, 4, 0, "cccccc");
            table.setTopBorder(XWPFTable.XWPFBorderType.SINGLE, 4, 0, "cccccc");
            table.setBottomBorder(XWPFTable.XWPFBorderType.SINGLE, 4, 0, "cccccc");
            table.setLeftBorder(XWPFTable.XWPFBorderType.SINGLE, 4, 0, "cccccc");
            table.setRightBorder(XWPFTable.XWPFBorderType.SINGLE, 4, 0, "cccccc");
        } catch (Exception e) {
            log.warn("Could not apply table borders", e);
        }
    }

    private void applyInline(XWPFParagraph p, Node node) {
        if (node instanceof TextNode tn) {
            String text = tn.text();
            if (!text.trim().isEmpty()) {
                XWPFRun r = p.createRun();
                r.setText(text);
                applyDefaultRunStyle(r);
            }
        } else if (node instanceof Element el) {
            String tagName = el.tagName().toLowerCase();

            if ("img".equals(tagName)) {
                insertImage(p, el);
            } else if ("br".equals(tagName)) {
                p.createRun().addBreak();
            } else {

                for (Node child : el.childNodes()) {
                    applyInline(p, child);
                }
            }
        }
    }


    private void applyStylesFromElement(XWPFRun run, Element el) {
        String style = el.attr("style");
        if (style.isEmpty()) return;

        String[] styles = style.split(";");
        for (String s : styles) {
            String[] parts = s.split(":", 2);
            if (parts.length == 2) {
                String property = parts[0].trim();
                String value = parts[1].trim();

                switch (property) {
                    case "font-weight" -> {
                        if ("bold".equals(value) || value.matches("\\d+") && Integer.parseInt(value) >= 600) {
                            run.setBold(true);
                        }
                    }
                    case "font-style" -> {
                        if ("italic".equals(value)) {
                            run.setItalic(true);
                        }
                    }
                    case "font-size" -> {
                        try {
                            String sizeStr = value.replaceAll("[^\\d.]", "");
                            if (!sizeStr.isEmpty()) {
                                int size = (int) Math.round(Double.parseDouble(sizeStr));
                                run.setFontSize(size);
                            }
                        } catch (NumberFormatException e) {
                            log.debug("Could not parse font size: {}", value);
                        }
                    }
                    case "font-family" -> {
                        String family = value.replaceAll("['\"]", "").split(",")[0].trim();
                        run.setFontFamily(family);
                    }
                    case "color" -> {
                        String color = value.replace("#", "");
                        if (color.length() == 6) {
                            run.setColor(color);
                        }
                    }
                }
            }
        }
    }

    private void insertImage(XWPFParagraph p, Element img) {
        String src = img.attr("src");
        if (src.startsWith("data:image")) {
            try {
                String[] parts = src.split(",");
                if (parts.length >= 2) {
                    byte[] data = Base64.getDecoder().decode(parts[1]);

                    int pictureType = XWPFDocument.PICTURE_TYPE_PNG;
                    if (parts[0].contains("jpeg") || parts[0].contains("jpg")) {
                        pictureType = XWPFDocument.PICTURE_TYPE_JPEG;
                    } else if (parts[0].contains("gif")) {
                        pictureType = XWPFDocument.PICTURE_TYPE_GIF;
                    }

                    try (ByteArrayInputStream bis = new ByteArrayInputStream(data)) {
                        XWPFRun imgRun = p.createRun();
                        imgRun.addPicture(bis, pictureType, "image",
                                Units.toEMU(400), Units.toEMU(300));
                    }
                }
            } catch (Exception e) {
                log.warn("Failed to insert image", e);
                XWPFRun r = p.createRun();
                r.setText("[Image could not be loaded]");
                r.setItalic(true);
            }
        }
    }

    private void applyDefaultParagraphSpacing(XWPFParagraph p) {
        p.setSpacingBefore(0);
        p.setSpacingAfter(200);
        p.setSpacingLineRule(LineSpacingRule.AUTO);
    }

    private void applyHeadingSpacing(XWPFParagraph p) {
        p.setSpacingBefore(240);
        p.setSpacingAfter(120);
        p.setSpacingLineRule(LineSpacingRule.AUTO);
    }

    private void applyListSpacing(XWPFParagraph p) {
        p.setSpacingBefore(0);
        p.setSpacingAfter(120);
        p.setSpacingLineRule(LineSpacingRule.AUTO);
    }

    private void applyTableCellParagraphSpacing(XWPFParagraph p) {
        p.setSpacingBefore(0);
        p.setSpacingAfter(0);
        p.setSpacingLineRule(LineSpacingRule.AUTO);
    }

    private void applyDefaultRunStyle(XWPFRun run) {
        try {
            run.setFontFamily("Calibri");
            run.setFontSize(11);
        } catch (Exception e) {
            log.debug("Could not apply default run style", e);
        }
    }

    public MultipartFile convertFileToMultipart(String path) throws IOException {
        File f = new File(path);
        byte[] bytes;
        try (FileInputStream fis = new FileInputStream(f)) {
            bytes = fis.readAllBytes();
        }
        return new CustomMultipartFile(
                f.getName(), f.getName(),
                "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
                bytes);
    }

    @Override
    protected String getUploadDirectory() {
        return appProperties.getUploadDirectory();
    }

    @Override
    public Document beforeCreate(Document document) {
        if (document.getDomain() == null) {
            document.setDomain(DomainConstants.DEFAULT_DOMAIN_NAME);
        }
        if (document.getTempType() == null) {
            document.setTempType(IEnumDocTempStatus.Types.EDITING);
        }
        return super.beforeCreate(document);
    }

    public void uploadDocumentFile(Document document, MultipartFile file) {
        if (file == null || file.isEmpty()) {
            log.warn("Tentative d'upload avec un fichier vide pour le document {}", document.getId());
            return;
        }

        try {
            this.uploadFile(document.getDomain(), document.getId(), file);
            log.info(" Fichier remplacé avec succès pour le document {}", document.getId());

        } catch (Exception e) {
            log.warn(" Échec de l'upload standard pour le document {} : {}", document.getId(), e.getMessage());

            String errorMessage = e.getMessage().toLowerCase();
            if (errorMessage.contains("uc.linked.file.code.constraint.violated")) {
                try {
                    String originalName = file.getOriginalFilename();
                    String extension = originalName != null && originalName.contains(".")
                            ? originalName.substring(originalName.lastIndexOf("."))
                            : ".docx";
                    String uniqueName = "doc_" + document.getId() + "_v" + System.currentTimeMillis() + extension;

                    MultipartFile renamed = new CustomMultipartFile(
                            uniqueName,
                            uniqueName,
                            file.getContentType(),
                            file.getBytes()
                    );

                    this.uploadFile(document.getDomain(), document.getId(), renamed);

                    log.info(" Fichier renommé et uploadé avec succès pour le document {}", document.getId());

                } catch (Exception retryEx) {
                    log.error(" Échec même après renommage pour le document {} : {}", document.getId(), retryEx.getMessage(), retryEx);
                }
            } else {
                log.error("Erreur inattendue pendant l'upload pour le document {}", document.getId(), e);
            }
        }
    }






}