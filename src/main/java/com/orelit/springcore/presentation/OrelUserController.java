package com.orelit.springcore.presentation;
import com.orelit.springcore.business.OrelUserService;
import com.orelit.springcore.common.constant.ApiConstant;
import com.orelit.springcore.common.dto.OrelUserDto;
import com.orelit.springcore.common.exception.ErrorExample;
import com.orelit.springcore.common.exception.ErrorMessageDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * Controller class responsible for managing OrelUser-related operations.
 */
@RestController
@RequestMapping(value = ApiConstant.BASE_PATH + "/OrelUser",
        produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "OrelUser", description = "OrelUser APIs.")
public class OrelUserController {

    private final OrelUserService orelUserService;

    public OrelUserController(OrelUserService orelUserService) {
        this.orelUserService = orelUserService;
    }

    /**
     * Create a new OrelUser.
     *
     * @param orelUserDto - Contains OrelUser details to be created.
     * @return - The created OrelUserDto.
     */
    @Operation(summary = "Create OrelUser.")
    @PostMapping
    @ResponseStatus(code = HttpStatus.OK)
    @ApiResponse(responseCode = "422", description = "Validation failed",
            content = @Content(schema = @Schema(implementation = ErrorMessageDto.class),
                    examples = @ExampleObject(ErrorExample.VALIDATION_FAILED)))
    public OrelUserDto createOrelUser(@Valid @RequestBody OrelUserDto orelUserDto) {
        return orelUserService.createOrelUser(orelUserDto);
    }

    /**
     * Update an existing OrelUser.
     *
     * @param orelUserDto - Contains updated OrelUser details.
     * @return - The updated OrelUserDto.
     */
    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update a OrelUser.")
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "422", description = "Validation failed",
                    content = {@Content(schema = @Schema(implementation = ErrorMessageDto.class),
                            examples = @ExampleObject(ErrorExample.VALIDATION_FAILED))}),
            @ApiResponse(responseCode = "404", description = "OrelUser not found",
                    content = {@Content(schema = @Schema(implementation = ErrorMessageDto.class),
                            examples = @ExampleObject(ErrorExample.NOT_FOUND))}),
            @ApiResponse(responseCode = "409", description = "Invalid version",
                    content = {@Content(schema = @Schema(implementation = OptimisticLockingFailureException.class),
                            examples = @ExampleObject(ErrorExample.OPTIMISTIC_LOCK))})})
    public OrelUserDto updateOrelUser(@Valid @RequestBody OrelUserDto orelUserDto) {
        return orelUserService.updateOrelUser(orelUserDto);
    }

    /**
     * Get OrelUser by phone number.
     *
     * @param phoneNo - The phone number to retrieve a OrelUser.
     * @return - The OrelUserDto.
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get OrelUser by phone no.")
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "OrelUser not found",
                    content = {@Content(schema = @Schema(implementation = ErrorMessageDto.class),
                            examples = @ExampleObject(ErrorExample.NOT_FOUND))})})
    public OrelUserDto getOrelUser(@Valid @RequestParam String phoneNo) {
        return orelUserService.getOrelUserByPhoneNo(phoneNo);
    }

    /**
     * Delete a OrelUser by phone number.
     *
     * @param phoneNo - The phone number to delete a OrelUser.
     */
    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Delete OrelUser.")
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "OrelUser not found",
                    content = {@Content(schema = @Schema(implementation = ErrorMessageDto.class),
                            examples = @ExampleObject(ErrorExample.NOT_FOUND))}),
            @ApiResponse(responseCode = "409", description = "Invalid version",
                    content = {@Content(schema = @Schema(implementation = OptimisticLockingFailureException.class),
                            examples = @ExampleObject(ErrorExample.OPTIMISTIC_LOCK))})})
    public void deleteOrelUser(String phoneNo) {
        orelUserService.deleteOrelUser(phoneNo);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get OrelUser by id.")
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "OrelUser not found",
                    content = {@Content(schema = @Schema(implementation = ErrorMessageDto.class),
                            examples = @ExampleObject(ErrorExample.NOT_FOUND))})})
    public OrelUserDto getOrelUserById(@Valid @PathVariable Long id) {
        return orelUserService.getOrelUserById(id);
    }

    @GetMapping("/list")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get OrelUser list")
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "OrelUser not found",
                    content = {@Content(schema = @Schema(implementation = ErrorMessageDto.class),
                            examples = @ExampleObject(ErrorExample.NOT_FOUND))})})
    public List<OrelUserDto> getOrelUserList() {
        return orelUserService.getOrelUserList();
    }
}
