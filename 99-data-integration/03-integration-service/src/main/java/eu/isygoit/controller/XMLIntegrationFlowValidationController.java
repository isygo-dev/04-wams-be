package eu.isygoit.controller;


import eu.isygoit.annotation.CtrlDef;
import eu.isygoit.exception.handler.IntegrationExceptionHandler;
import eu.isygoit.mapper.IntegrationFlowFileMapper;
import eu.isygoit.service.impl.IntegrationFlowService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import eu.isygoit.helper.XmlHelper;
import org.xml.sax.SAXException;

import java.io.IOException;

@Slf4j
@Validated
@RestController
@RequestMapping(path = "/api/v1/private/integration/validate")
@CtrlDef(handler = IntegrationExceptionHandler.class, mapper = IntegrationFlowFileMapper.class, minMapper = IntegrationFlowFileMapper.class,
        service = IntegrationFlowService.class)
public class XMLIntegrationFlowValidationController {
    @PostMapping("/validate-xml")
    public ResponseEntity<?> validateXml(@RequestParam("xmlFile") MultipartFile xmlFile,
                                         @RequestParam("xsdFile") MultipartFile xsdFile) {

        if (xmlFile.isEmpty() || xsdFile.isEmpty()) {
            return ResponseEntity.badRequest().body("Both XML and XSD files must be uploaded.");
        }

        try {
            String xmlContent = new String(xmlFile.getBytes());
            String xsdContent = new String(xsdFile.getBytes());
            boolean isValid = XmlHelper.validateXml(xmlContent, xsdContent, "http://www.w3.org/2001/XMLSchema");

            if (isValid) {
                return ResponseEntity.ok("XML is valid.");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("XML is invalid.");
            }

        } catch (IOException | SAXException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("XML validation failed: " + e.getMessage());
        }
    }
}