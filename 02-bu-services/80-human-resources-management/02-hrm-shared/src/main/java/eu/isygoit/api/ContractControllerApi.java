package eu.isygoit.api;

import eu.isygoit.com.rest.api.IMappedCrudApi;
import eu.isygoit.constants.JwtConstants;
import eu.isygoit.constants.RestApiConstants;
import eu.isygoit.dto.common.RequestContextDto;
import eu.isygoit.dto.data.ContractDto;
import eu.isygoit.dto.data.MinContractDto;
import eu.isygoit.dto.extendable.IdentifiableDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * The interface Contract controller api.
 */
public interface ContractControllerApi extends IMappedCrudApi<Long, MinContractDto, ContractDto> {
    /**
     * Update contract status response entity.
     *
     * @param requestContext the request context
     * @param id             the id
     * @param isLocked       the is locked
     * @return the response entity
     */
    @Operation(summary = "updateContractStatus Api",
            description = "updateContractStatus")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Api executed successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = IdentifiableDto.class))})
    })
    @PutMapping(path = "/updateContractStatus")
    ResponseEntity<ContractDto> updateContractStatus(@RequestAttribute(value = JwtConstants.JWT_USER_CONTEXT, required = false) RequestContextDto requestContext,
                                                     @RequestParam(name = RestApiConstants.ID) Long id,
                                                     @RequestParam(name = RestApiConstants.IS_LOCKED) Boolean isLocked);

}
