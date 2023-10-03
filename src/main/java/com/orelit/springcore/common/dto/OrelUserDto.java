package com.orelit.springcore.common.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) for OrelUser entity.
 * Contains fields and validation constraints for creating and updating OrelUser information.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrelUserDto {

    @NotEmpty(message = "Phone number should not be empty.")
    @Pattern(regexp = "^0\\d{9}$",
            message = "Invalid phone number format. Should be in the format 0XXXXXXXXXX.")
    private String phoneNo;

    @NotEmpty(message = "Language is required.")
    @Pattern(regexp = "^[A-Z][a-z]*$",
            message = "Invalid first name format. Should start with an uppercase " +
                    "letter, followed by lowercase letters.")
    private String language;

    @NotEmpty(message = "First name is required.")
    @Pattern(regexp = "^[A-Z][a-z]*$",
            message = "Invalid first name format. Should start with an uppercase " +
                    "letter, followed by lowercase letters.")
    private String firstName;

    @NotEmpty(message = "Middle name should not be empty.")
    @Pattern(regexp = "^[A-Z][a-zA-Z]*$",
            message = "Invalid middle name format. Should start with an uppercase " +
                    "letter, followed by letters.")
    private String middleName;

    @NotEmpty(message = "Email should not be empty.")
    @Email(message = "Invalid email format. Should be a valid email address.")
    private String email;

    @NotEmpty(message = "Department name should not be empty.")
    @Pattern(regexp = "^[A-Z][a-z]*$",
            message = "Invalid department name format. Should start with an uppercase " +
                    "letter, followed by lowercase letters.")
    private String dep_name;

    @NotEmpty(message = "Department contact should not be empty.")
    @Pattern(regexp = "^0\\d{9}$",
            message = "Invalid department phone number format. Should be in the format 0XXXXXXXXXX.")
    private String dep_contact_no;

    @NotEmpty(message = "Department email should not be empty.")
    @Email(message = "Invalid department email format. Should be a valid email address.")
    private String dep_email;


}
