package eu.isygoit.controller;


import com.networknt.schema.ValidationMessage;
import eu.isygoit.annotation.CtrlDef;
import eu.isygoit.exception.handler.IntegrationExceptionHandler;
import eu.isygoit.helper.JsonHelper;
import eu.isygoit.mapper.IntegrationFlowFileMapper;
import eu.isygoit.service.impl.IntegrationFlowService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Set;

@Slf4j
@Validated
@RestController
@RequestMapping(path = "/api/v1/private/integration")
@CtrlDef(handler = IntegrationExceptionHandler.class, mapper = IntegrationFlowFileMapper.class, minMapper = IntegrationFlowFileMapper.class,
        service = IntegrationFlowService.class)
public class JSONIntegrationFlowValidationController {

    @PostMapping("/validate/json-schema")
    @Operation(summary = "Validate JSON file against a schema")
    public ResponseEntity<?> validateJsonSchema(
            @RequestParam("jsonFile") MultipartFile jsonFile,
            @RequestParam("schemaFile") MultipartFile schemaFile,
            @RequestParam("schemaLanguage") String schemaLanguage) {

        try {
            File tempJsonFile = File.createTempFile("jsonFile", ".json");
            File tempSchemaFile = File.createTempFile("schemaFile", ".json");
            jsonFile.transferTo(tempJsonFile);
            schemaFile.transferTo(tempSchemaFile);
            Set<ValidationMessage> validationMessages = JsonHelper.validateJson(tempJsonFile, tempSchemaFile, schemaLanguage);
            tempJsonFile.delete();
            tempSchemaFile.delete();

            if (validationMessages.isEmpty()) {
                return ResponseEntity.ok("JSON is valid according to the schema.");
            } else {
                return ResponseEntity.badRequest().body(validationMessages);
            }
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error occurred: " + e.getMessage());
        }
    }

}