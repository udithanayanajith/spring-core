package com.orelit.springcore.business;


import com.orelit.springcore.common.constant.SystemConstants;
import com.orelit.springcore.common.dto.OrelUserDto;
import com.orelit.springcore.common.exception.NotFoundException;
import com.orelit.springcore.common.exception.PropertyValidationException;
import com.orelit.springcore.common.exception.ValidationErrorUtil;
import com.orelit.springcore.persistence.converter.DepartmentMapper;
import com.orelit.springcore.persistence.converter.OrelUserMapper;
import com.orelit.springcore.persistence.entity.Department;
import com.orelit.springcore.persistence.entity.OrelUser;
import com.orelit.springcore.persistence.repository.OrelUserDepartmentTemplate;
import com.orelit.springcore.persistence.repository.OrelUserTemplate;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * Manage OrelUser-related operations.
 */
@Service
public class OrelUserService {

    private final OrelUserTemplate OrelUserTemplate;

    private final OrelUserDepartmentTemplate orelUserDepartmentTemplate;

    private final OrelUserMapper orelUserMapper;

    private final DepartmentMapper departmentMapper;

    public OrelUserService(OrelUserTemplate OrelUserTemplate, OrelUserDepartmentTemplate orelUserDepartmentTemplate, OrelUserMapper orelUserMapper, DepartmentMapper departmentMapper) {
        this.OrelUserTemplate = OrelUserTemplate;
        this.orelUserDepartmentTemplate = orelUserDepartmentTemplate;
        this.orelUserMapper = orelUserMapper;
        this.departmentMapper = departmentMapper;
    }

    /**
     * Save OrelUser.
     *
     * @param orelUserDto - Contains OrelUser data.
     * @return - The created OrelUser DTO.
     */

    public OrelUserDto createOrelUser(OrelUserDto orelUserDto) {

        existsPhoneNoValidation(orelUserDto.getPhoneNo());
        OrelUser orelUser = orelUserMapper.convertToEntity(orelUserDto);
        //OrelUser savedUser = OrelUserTemplate.save(orelUser);
        Department department = departmentMapper.convertDepartmentDetailDtoToEntity(orelUserDto);
        orelUserDepartmentTemplate.saveOrelUserDepartmentDetails(department);
        return orelUserDto;

    }

    /**
     * Validate if a phone number already exists.
     *
     * @param phoneNo - The phone number to be validated.
     */
    private void existsPhoneNoValidation(String phoneNo) {
        if (OrelUserTemplate.findByPhoneNo(phoneNo) != null) {
            throw new RuntimeException("Phone no already exists!");
        }
    }

    /**
     * update OrelUser
     *
     * @param orelUserDto - Contains OrelUser data.
     * @return - The updated OrelUser DTO.
     */
    public OrelUserDto updateOrelUser(OrelUserDto orelUserDto) {
        existsOrelUserValidation(orelUserDto.getPhoneNo());
        OrelUser savedOrelUser = OrelUserTemplate.findByPhoneNo(orelUserDto.getPhoneNo());
        OrelUser updatedOrelUser = orelUserMapper.convertToUpdateEntity(orelUserDto, savedOrelUser);
        Department updateDepartmentDetails = departmentMapper.convertUpdateDepartmentDetailDtoToEntity(orelUserDto, savedOrelUser.getDepartment(), savedOrelUser);
        updatedOrelUser.setDepartment(updateDepartmentDetails);
        return orelUserMapper.convertToDto(OrelUserTemplate.save(updatedOrelUser));
    }

    /**
     * Validates the existence of a OrelUser record by phone number.
     *
     * @param phoneNo - The phone number to validate the OrelUser.
     */
    private void existsOrelUserValidation(String phoneNo) {
        if (OrelUserTemplate.findByPhoneNo(phoneNo) == null) {
            throw new NotFoundException("OrelUser not found!");
        }
    }

    /**
     * Retrieve a OrelUserDto by phone number.
     *
     * @param phoneNo - The phone number to identify the OrelUser.
     * @return OrelUserDto - The DTO representing the retrieved OrelUser.
     */
    public OrelUserDto getOrelUserByPhoneNo(String phoneNo) {

        OrelUser orelUser = OrelUserTemplate.findByPhoneNo(phoneNo);
        if (orelUser == null) {
            existsOrelUserValidation(phoneNo);
            return null;
        }
        return orelUserMapper.convertToDto(orelUser);
    }

    /**
     * Delete a OrelUser record by phone number.
     *
     * @param phoneNo - The phone number to identify the OrelUser to delete.
     */
    public void deleteOrelUser(String phoneNo) {

        OrelUser user = OrelUserTemplate.findByPhoneNo(phoneNo);
        Department department = orelUserDepartmentTemplate.findByUserId(user.getId());
        if (department != null) {
            orelUserDepartmentTemplate.delete(department);
        }
        OrelUserTemplate.delete(user);

    }

    /**
     * Retrieve a OrelUserDto by its unique ID.
     *
     * @param id - The ID of the OrelUser to retrieve.
     * @return OrelUserDto - The DTO representing the retrieved OrelUser.
     */
    public OrelUserDto getOrelUserById(Long id) {
        validateId(id);
        OrelUser orelUser = OrelUserTemplate.findById(id);
        return orelUserMapper.convertToDto(orelUser);
    }

    /**
     * Validates if the provided ID is a valid numeric value.
     *
     * @param id - The ID to be validated.
     * @throws ValidationErrorUtil - If the ID is not a valid numeric value.
     */
    private void validateId(Long id) {
        if (SystemConstants.NUMBER_REGEX.matches(id.toString())) {
            throw new PropertyValidationException("OrelUser id", "Id not valid!");
        }
    }

    /**
     * Retrieve all users.
     *
     * @return - List of OrelUserDto objects.
     */
    public List<OrelUserDto> getOrelUserList() {
        List<OrelUser> orelUserList = OrelUserTemplate.findAll();
        return orelUserMapper.convertToOrelUserDtoList(orelUserList);
    }
}
