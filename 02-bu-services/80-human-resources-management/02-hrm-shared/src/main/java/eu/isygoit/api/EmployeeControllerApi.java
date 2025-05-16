package eu.isygoit.api;

import eu.isygoit.com.rest.api.IMappedCrudApi;
import eu.isygoit.constants.JwtConstants;
import eu.isygoit.constants.RestApiConstants;
import eu.isygoit.dto.common.RequestContextDto;
import eu.isygoit.dto.data.EmployeeDto;
import eu.isygoit.dto.data.MinEmployeeDto;
import eu.isygoit.dto.extendable.IdentifiableDto;
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
     *
     * @param requestContext the request context
     * @param id             the id
     * @param newStatus      the new status
     * @return the response entity
     */
    @Operation(summary = "xxx Api",
            description = "xxx")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Api executed successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = IdentifiableDto.class))}),
            @ApiResponse(responseCode = "404", description = "Api not found")
    })
    @PutMapping(path = "/updateStatusEmployee")
    ResponseEntity<EmployeeDto> updateEmployeeStatus(@RequestAttribute(value = JwtConstants.JWT_USER_CONTEXT, required = false) RequestContextDto requestContext,
                                                     @RequestParam(name = RestApiConstants.ID) Long id,
                                                     @RequestParam(name = RestApiConstants.NEW_STATUS) IEnumEnabledBinaryStatus.Types newStatus);

    /**
     * Gets employee by code.
     *
     * @param requestContext the request context
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
    ResponseEntity<EmployeeDto> getEmployeeByCode(@RequestAttribute(value = JwtConstants.JWT_USER_CONTEXT, required = false) RequestContextDto requestContext,
                                                  @PathVariable(name = RestApiConstants.CODE) String code);

    /**
     * Gets employee by domain.
     *
     * @param requestContext the request context
     * @param domain         the domain
     * @return the employee by domain
     */
    @Operation(summary = "Get Employee by Domain",
            description = "Get an employee by their domain")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employee found successfully"),
            @ApiResponse(responseCode = "404", description = "Employee not found")
    })
    @GetMapping("/domain/{domain}")
    ResponseEntity<List<EmployeeDto>> getEmployeeByDomain(@RequestAttribute(value = JwtConstants.JWT_USER_CONTEXT, required = false) RequestContextDto requestContext,
                                                          @PathVariable(name = RestApiConstants.DOMAIN_NAME) String domain);
}
