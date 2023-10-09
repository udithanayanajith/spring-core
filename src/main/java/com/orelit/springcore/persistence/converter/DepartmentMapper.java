package com.orelit.springcore.persistence.converter;

import com.orelit.springcore.common.dto.OrelUserDto;
import com.orelit.springcore.persistence.entity.Department;
import com.orelit.springcore.persistence.entity.OrelUser;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

/**
 * DepartmentMapper
 * Author: udithan
 * Date: 03-Oct-23
 */
@Component
public class DepartmentMapper {
    private ModelMapper modelMapper;

    public DepartmentMapper(ModelMapper modelMapper) {

        this.modelMapper = modelMapper;
    }


    /**
     * Convert Orel User DTO to an entity save user data and department data.
     *
     * @param OrelUserDto - Contains the user details.
     * @return - Department Object.
     */
    public Department convertDepartmentDetailDtoToEntity(OrelUserDto OrelUserDto,OrelUser savedUser) {

        Department department = modelMapper.map(OrelUserDto, Department.class);
        department.setOrelUser(savedUser);
        department.setDep_name(OrelUserDto.getDep_name());
        department.setDep_email(OrelUserDto.getDep_email());
        department.setDep_contact_no(OrelUserDto.getDep_contact_no());
        return department;

    }

    /**
     * Convert user data and department data to an entity.
     *
     * @param OrelUserDto -Contains only User details.
     * @param department  -Contains only Department details.
     * @param orelUser    -Contains User details.
     * @return - Department Object.
     */
    public Department convertUpdateDepartmentDetailDtoToEntity(OrelUserDto OrelUserDto, Department department, OrelUser orelUser) {

        department.setDep_name(OrelUserDto.getDep_name());
        department.setDep_email(OrelUserDto.getDep_email());
        department.setDep_contact_no(OrelUserDto.getDep_contact_no());
        return department;

    }
}
