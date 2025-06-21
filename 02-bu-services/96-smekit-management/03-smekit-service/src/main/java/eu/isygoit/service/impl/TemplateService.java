package eu.isygoit.service.impl;

import eu.isygoit.annotation.CodeGenKms;
import eu.isygoit.annotation.CodeGenLocal;
import eu.isygoit.annotation.DmsLinkFileService;
import eu.isygoit.annotation.SrvRepo;
import eu.isygoit.com.rest.service.impl.FileService;
import eu.isygoit.config.AppProperties;
import eu.isygoit.constants.DomainConstants;
import eu.isygoit.model.*;
import eu.isygoit.model.schema.ComSchemaColumnConstantName;
import eu.isygoit.remote.dms.DmsLinkedFileService;
import eu.isygoit.remote.kms.KmsIncrementalKeyService;
import eu.isygoit.repository.TemplateRepository;
import eu.isygoit.service.ITemplateService;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.poi.xwpf.usermodel.*;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNumPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPr;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.HtmlUtils;

import javax.imageio.ImageIO;
import org.jsoup.nodes.Element;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@Transactional
@DmsLinkFileService(DmsLinkedFileService.class)
@CodeGenLocal(value = NextCodeService.class)
@CodeGenKms(value = KmsIncrementalKeyService.class)
@SrvRepo(value = TemplateRepository.class)
public class TemplateService extends FileService<Long, Template, TemplateRepository> implements ITemplateService {

    private final AppProperties appProperties;
    private final TemplateRepository templateRepository;


    public TemplateService(AppProperties appProperties,
                           TemplateRepository templateRepository ) {
        this.appProperties = appProperties;
        this.templateRepository = templateRepository;

    }


    @Override
    protected String getUploadDirectory() {
        return this.appProperties.getUploadDirectory();
    }

    @Override
    public AppNextCode initCodeGenerator() {
        return AppNextCode.builder()
                .domain(DomainConstants.DEFAULT_DOMAIN_NAME)
                .entity(Template.class.getSimpleName())
                .attribute(ComSchemaColumnConstantName.C_CODE)
                .prefix("TMP")
                .valueLength(6L)
                .value(1L)
                .build();
    }

    public List<Template> getTemplatesByAuthor(Long authorId) {
        return templateRepository.getTemplatesByAuthor(authorId);
    }

    public List<Template> getTemplatesByCategory(Long categoryId) {
        return templateRepository.findByCategoryId(categoryId);

    }
    public String convertToHtml(Resource resource) throws IOException {
        if (resource == null || !resource.exists()) {
            throw new IOException("Resource not found or inaccessible");
        }

        String originalName = resource.getFilename();
        if (originalName == null) {
            throw new IOException("Filename cannot be determined");
        }

        String fileKey = originalName + "_" + resource.contentLength();
        if (conversionCache.containsKey(fileKey)) {
            return conversionCache.get(fileKey);
        }

        try {
            String html;
            if (isDocx(originalName)) {
                html = convertDocxToTiptapHtml(resource);
            } else if (isPdf(originalName)) {
                html = convertPdfToTiptapHtml(resource);
            } else {
                throw new IOException("Unsupported format. Only DOCX/PDF are accepted.");
            }

            String cleanedHtml = cleanHtmlForTiptap(html);
            conversionCache.put(fileKey, cleanedHtml);
            return cleanedHtml;
        } catch (Exception e) {
            log.error("Error converting file to HTML: {}", originalName, e);
            throw new IOException("Failed to convert file to HTML", e);
        }
    }

    private final Map<String, String> conversionCache = new ConcurrentHashMap<>();

    private boolean isDocx(String filename) {
        return filename.toLowerCase().endsWith(".docx");
    }

    private boolean isPdf(String filename) {
        return filename.toLowerCase().endsWith(".pdf");
    }

    private String convertDocxToTiptapHtml(Resource resource) throws IOException {
        StringBuilder html = new StringBuilder("<div style='font-family:Calibri, sans-serif; font-size:11pt;'>");

        try (XWPFDocument doc = new XWPFDocument(resource.getInputStream())) {
            boolean inList = false;
            String listType = "";

            for (IBodyElement element : doc.getBodyElements()) {
                if (element instanceof XWPFParagraph paragraph) {
                    String tag = "p";

                    if (isHeading(paragraph)) {
                        int level = getHeadingLevel(paragraph);
                        tag = "h" + Math.min(level, 6);
                    } else if (isNumbered(paragraph)) {
                        if (!inList || !"ol".equals(listType)) {
                            if (inList) html.append("</").append(listType).append(">");
                            html.append("<ol>");
                            listType = "ol";
                            inList = true;
                        }
                        tag = "li";
                    } else if (isBulleted(paragraph)) {
                        if (!inList || !"ul".equals(listType)) {
                            if (inList) html.append("</").append(listType).append(">");
                            html.append("<ul>");
                            listType = "ul";
                            inList = true;
                        }
                        tag = "li";
                    } else if (inList) {
                        html.append("</").append(listType).append(">");
                        inList = false;
                    }

                    html.append("<").append(tag).append(">");
                    for (XWPFRun run : paragraph.getRuns()) {
                        String runText = HtmlUtils.htmlEscape(run.text());
                        if (runText == null || runText.isBlank()) continue;

                        StringBuilder style = new StringBuilder();
                        if (run.isBold()) style.append("font-weight:bold;");
                        if (run.isItalic()) style.append("font-style:italic;");
                        if (run.getFontSize() > 0) style.append("font-size:").append(run.getFontSize()).append("pt;");
                        if (run.getFontFamily() != null) style.append("font-family:").append(run.getFontFamily()).append(";");
                        if (run.getColor() != null) style.append("color:#").append(run.getColor()).append(";");

                        if (style.length() > 0) {
                            html.append("<span style='").append(style).append("'>").append(runText).append("</span>");
                        } else {
                            html.append(runText);
                        }

                        for (XWPFPicture picture : run.getEmbeddedPictures()) {
                            addImageToHtml(html, picture);
                        }
                    }
                    html.append("</").append(tag).append(">");
                }

                if (element instanceof XWPFTable table) {
                    if (inList) {
                        html.append("</").append(listType).append(">");
                        inList = false;
                    }
                    processStyledTable(html, table);
                }
            }

            if (inList) {
                html.append("</").append(listType).append(">");
            }

            for (XWPFHeader header : doc.getHeaderList()) {
                for (XWPFParagraph headerParagraph : header.getParagraphs()) {
                    for (XWPFRun run : headerParagraph.getRuns()) {
                        for (XWPFPicture picture : run.getEmbeddedPictures()) {
                            addImageToHtml(html, picture);
                        }
                    }
                }
            }

            List<XWPFPictureData> allPictures = doc.getAllPictures();
            if (allPictures != null) {
                for (XWPFPictureData pictureData : allPictures) {
                    byte[] imageData = pictureData.getData();
                    String mimeType = getMimeType(pictureData.getPictureType());
                    String base64 = Base64.getEncoder().encodeToString(imageData);

                    html.append("<br><img style='max-width:100%; height:auto; margin:10px 0;' src='data:")
                            .append(mimeType)
                            .append(";base64,")
                            .append(base64)
                            .append("' /><br>");
                }
            }
        }

        html.append("</div>");
        return cleanHtmlForTiptap(html.toString());
    }

    private void processStyledTable(StringBuilder html, XWPFTable table) {
        html.append("<table style='border-collapse:collapse;width:100%;border:1px solid #ccc;'>");

        for (XWPFTableRow row : table.getRows()) {
            html.append("<tr>");
            for (XWPFTableCell cell : row.getTableCells()) {
                html.append("<td style='border:1px solid #ccc;padding:8px;'>");

                for (XWPFParagraph p : cell.getParagraphs()) {
                    for (XWPFRun run : p.getRuns()) {
                        String text = HtmlUtils.htmlEscape(run.text());
                        if (text == null || text.isBlank()) continue;

                        StringBuilder style = new StringBuilder();
                        if (run.isBold()) style.append("font-weight:bold;");
                        if (run.isItalic()) style.append("font-style:italic;");
                        if (run.getFontSize() > 0) style.append("font-size:").append(run.getFontSize()).append("pt;");
                        if (run.getFontFamily() != null) style.append("font-family:").append(run.getFontFamily()).append(";");
                        if (run.getColor() != null) style.append("color:#").append(run.getColor()).append(";");

                        if (style.length() > 0) {
                            html.append("<span style='").append(style.toString()).append("'>").append(text).append("</span>");
                        } else {
                            html.append(text);
                        }
                    }
                }

                html.append("</td>");
            }
            html.append("</tr>");
        }

        html.append("</table>");
    }
    private boolean isHeading(XWPFParagraph paragraph) {
        String style = paragraph.getStyle();
        return style != null && style.toLowerCase().startsWith("heading");
    }

    private int getHeadingLevel(XWPFParagraph paragraph) {
        String style = paragraph.getStyle();
        if (style == null) return 1;
        String[] parts = style.split(" ");
        if (parts.length > 1 && Character.isDigit(parts[1].charAt(0))) {
            return Integer.parseInt(parts[1]);
        }
        return 1;
    }

    private boolean isBulleted(XWPFParagraph paragraph) {
        CTPPr ppr = paragraph.getCTP().getPPr();
        return ppr != null && ppr.getNumPr() != null && ppr.getNumPr().getNumId() != null;
    }


    private void addImageToHtml(StringBuilder html, XWPFPicture picture) {
        byte[] imageData = picture.getPictureData().getData();
        int pictureType = picture.getPictureData().getPictureType();
        String mimeType = getMimeType(pictureType);
        String base64 = Base64.getEncoder().encodeToString(imageData);

        html.append("<br><img style='max-width:100%; height:auto; margin:10px 0;' src='data:")
                .append(mimeType)
                .append(";base64,")
                .append(base64)
                .append("' /><br>");
    }
    public static boolean isNumbered(XWPFParagraph paragraph) {
        CTPPr ppr = paragraph.getCTP().getPPr();
        if (ppr == null) return false;

        CTNumPr numPr = ppr.getNumPr();
        return numPr != null && numPr.getNumId() != null;
    }

    private String convertPdfToTiptapHtml(Resource resource) throws IOException {
        try (PDDocument document = PDDocument.load(resource.getInputStream())) {
            PDFRenderer pdfRenderer = new PDFRenderer(document);
            StringBuilder html = new StringBuilder("<div>");

            for (int page = 0; page < document.getNumberOfPages(); ++page) {
                BufferedImage bim = pdfRenderer.renderImageWithDPI(page, 150, ImageType.RGB);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(bim, "png", baos);
                String base64 = Base64.getEncoder().encodeToString(baos.toByteArray());

                html.append("<img style='max-width:100%;height:auto;margin:10px 0;'")
                        .append(" src='data:image/png;base64,")
                        .append(base64).append("' />");
            }

            html.append("</div>");
            return cleanHtmlForTiptap(html.toString());
        }
    }


    private String cleanHtmlForTiptap(String html) {
        org.jsoup.nodes.Document doc = Jsoup.parse(html);
        doc.select("script, style, meta, link, noscript").remove();

        Safelist safelist = Safelist.relaxed()
                .addTags("table", "tr", "td", "th", "tbody", "thead", "ul", "ol", "li",
                        "h1", "h2", "h3", "h4", "h5", "h6", "strong", "em", "u", "a", "img")
                .addAttributes("a", "href", "target", "rel")
                .addAttributes("img", "src", "alt", "style", "width", "height")
                .addAttributes("span", "style")
                .addAttributes("td", "style")
                .addProtocols("a", "href", "http", "https")
                .addProtocols("img", "src", "data");

        return Jsoup.clean(doc.body().html(), safelist);
    }

    private static String getMimeType(int pictureType) {
        switch (
                pictureType) {
            case 5:
                return "image/jpeg";
            case 6:
                return "image/png";
            case 8:
                return "image/gif";
            case 11:
            case 7:
                return "image/x-ms-bmp";
            case 3:
                return "image/x-emf";
            case 4:
                return "image/x-wmf";
            default:
                return "image/unknown";
        }
    }
    public void convertHtmlToDocx(String html, String outputPath) throws IOException {
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(html);
        XWPFDocument docx = new XWPFDocument();

        Element body = jsoupDoc.body();

        for (Element element : body.children()) {
            if (element.tagName().equals("p")) {
                XWPFParagraph paragraph = docx.createParagraph();
                XWPFRun run = paragraph.createRun();
                applyStyleFromElement(run, element);
                run.setText(element.text());
            } else if (element.tagName().equals("ol") || element.tagName().equals("ul")) {
                for (Element li : element.select("li")) {
                    XWPFParagraph p = docx.createParagraph();
                    p.setStyle("ListParagraph"); 
                    XWPFRun run = p.createRun();
                    applyStyleFromElement(run, li);
                    run.setText("• " + li.text());
                }
            } else {
                XWPFParagraph p = docx.createParagraph();
                XWPFRun r = p.createRun();
                r.setText(element.text());
            }
        }

        try (FileOutputStream out = new FileOutputStream(outputPath)) {
            docx.write(out);
        }
    }

    private void applyStyleFromElement(XWPFRun run, Element element) {
        String style = element.attr("style");
        if (style.contains("font-weight:bold")) {
            run.setBold(true);
        }
        if (style.contains("font-style:italic")) {
            run.setItalic(true);
        }
        if (style.contains("text-decoration:underline")) {
            run.setUnderline(UnderlinePatterns.SINGLE);
        }
        if (style.contains("font-size:14pt")) {
            run.setFontSize(14);
        } else if (style.contains("font-size:16pt")) {
            run.setFontSize(16);
        }
    }

    public MultipartFile convertFileToMultipart(String path) throws IOException {
        File f = new File(path);
        byte[] bytes;
        try (FileInputStream fis = new FileInputStream(f)) {
            bytes = fis.readAllBytes();
        }

        return new eu.isygoit.util.CustomMultipartFile(
                f.getName(), f.getName(),
                "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
                bytes);
    }


}