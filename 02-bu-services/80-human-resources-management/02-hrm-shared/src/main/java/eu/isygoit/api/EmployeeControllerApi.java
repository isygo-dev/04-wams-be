package eu.isygoit.api;

import eu.isygoit.com.rest.api.IMappedCrudApi;
import eu.isygoit.constants.JwtConstants;
import eu.isygoit.constants.RestApiConstants;
import eu.isygoit.dto.common.ContextRequestDto;
import eu.isygoit.dto.data.EmployeeDto;
import eu.isygoit.dto.data.MinEmployeeDto;
import eu.isygoit.enums.IEnumEnabledBinaryStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * The interface Employee controller api.
 */
public interface EmployeeControllerApi extends IMappedCrudApi<Long, MinEmployeeDto, EmployeeDto> {
    /**
     * Update employee status response entity.
     
     * @param id             the id
     * @param newStatus      the new status
     * @return the response entity
     */
    @Operation(summary = "Update employee status",
            description = "This endpoint updates the status (ENABLED/DISABLED) of an employee identified by their ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Employee status updated successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = EmployeeDto.class))}),
            @ApiResponse(responseCode = "404", description = "Employee not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping(path = "/updateStatusEmployee")
    ResponseEntity<EmployeeDto> updateEmployeeStatus(
                                                     @RequestParam(name = RestApiConstants.ID) Long id,
                                                     @RequestParam(name = RestApiConstants.NEW_STATUS) IEnumEnabledBinaryStatus.Types newStatus);

    /**
     * Gets employee by code.
     
     * @param code           the code
     * @return the employee by code
     */
    @Operation(summary = "Get Employee by Code",
            description = "Get an employee by their code")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employee found successfully"),
            @ApiResponse(responseCode = "404", description = "Employee not found")
    })
    @GetMapping("/code/{code}")
    ResponseEntity<EmployeeDto> getEmployeeByCode(
                                                  @PathVariable(name = RestApiConstants.CODE) String code);

    /**
     * Gets employee by tenant.
     
     * @param tenant         the tenant
     * @return the employee by tenant
     */
    @Operation(summary = "Get Employee by Tenant",
            description = "Get an employee by their tenant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employee found successfully"),
            @ApiResponse(responseCode = "404", description = "Employee not found")
    })
    @GetMapping("/tenant/{tenant}")
    ResponseEntity<List<EmployeeDto>> getEmployeeByTenant(
                                                          @PathVariable(name = RestApiConstants.TENANT_NAME) String tenant);
}
