package eu.isygoit.controller;

import eu.isygoit.annotation.CtrlDef;
import eu.isygoit.com.rest.controller.ResponseFactory;
import eu.isygoit.com.rest.controller.constants.CtrlConstants;
import eu.isygoit.com.rest.controller.impl.ControllerExceptionHandler;
import eu.isygoit.dto.data.DashboardStatsDTO;
import eu.isygoit.exception.handler.SmeKitExceptionHandler;
import eu.isygoit.mapper.DocumentCommentMapper;
import eu.isygoit.service.impl.DashboardService;
import eu.isygoit.service.impl.DocumentCommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Validated
@RestController
@CtrlDef(handler = SmeKitExceptionHandler.class, mapper = DocumentCommentMapper.class, minMapper = DocumentCommentMapper.class, service = DocumentCommentService.class)
@RequestMapping("/api/v1/private/dashboard")
@RequiredArgsConstructor
public class DashboardController extends ControllerExceptionHandler {

    private final DashboardService dashboardService;

    @GetMapping("/stats")
    public ResponseEntity<DashboardStatsDTO> getDashboardStats(@RequestParam(required = false) String userIdentifier) {

        log.info("executing getDashboardStats");
        try {
            return ResponseFactory.responseOk(dashboardService.getDashboardStatistics(userIdentifier));
        } catch (Throwable e) {
            log.error(CtrlConstants.ERROR_API_EXCEPTION, e);
            return getBackExceptionResponse(e);
        }
    }
}