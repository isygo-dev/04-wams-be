package eu.isygoit.controller;

import eu.isygoit.annotation.CtrlDef;
import eu.isygoit.dto.data.DashboardStatsDTO;
import eu.isygoit.exception.handler.SmeKitExceptionHandler;
import eu.isygoit.mapper.DocCommentsMapper;
import eu.isygoit.service.impl.DashboardService;
import eu.isygoit.service.impl.DocCommentsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
@Slf4j
@Validated
@RestController
@CtrlDef(handler = SmeKitExceptionHandler.class, mapper = DocCommentsMapper.class, minMapper = DocCommentsMapper.class, service = DocCommentsService.class)
@RequestMapping("/api/v1/private/dashboard")
@RequiredArgsConstructor
public class DashboardController {
    private final DashboardService dashboardService;


    @GetMapping("/stats")
    public DashboardStatsDTO getDashboardStats(@RequestParam(required = false) String userIdentifier) {
        return dashboardService.getDashboardStatistics(userIdentifier);
    }
}