package com.orelit.springcore.persistence.converter;


import com.orelit.springcore.common.dto.OrelUserDto;
import com.orelit.springcore.persistence.entity.OrelUser;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper class for converting between OrelUser and OrelUserDto objects using ModelMapper.
 */
@Component
public class OrelUserMapper {

    private final ModelMapper modelMapper;

    public OrelUserMapper(ModelMapper modelMapper) {

        this.modelMapper = modelMapper;
    }

    /**
     * Convert a OrelUserDto to a OrelUser entity.
     *
     * @param orelUserDto - OrelUser details from front.
     * @return - Converted OrelUser Entity.
     */
    public OrelUser convertToEntity(OrelUserDto orelUserDto) {

        return modelMapper.map(orelUserDto, OrelUser.class);
    }

    /**
     * Convert a OrelUser entity to a OrelUserDto.
     *
     * @param orelUser - Converted OrelUser DTO.
     * @return
     */
    public OrelUserDto convertToDto(OrelUser orelUser) {
        OrelUserDto orelUserDto = modelMapper.map(orelUser, OrelUserDto.class);
        if (orelUser.getDepartment() != null) {
            orelUserDto.setDep_name(orelUser.getDepartment().getDep_name());
            orelUserDto.setDep_contact_no(orelUser.getDepartment().getDep_contact_no());
            orelUserDto.setDep_email(orelUser.getDepartment().getDep_email());
        }

        return orelUserDto;
    }

    /**
     * Update an existing OrelUser entity with data from a OrelUserDto.
     *
     * @param oldOrelUser - OrelUser Entity to update.
     * @param orelUserDto - OrelUser DTO from the front.
     * @return
     */
    public OrelUser convertToUpdateEntity(OrelUserDto orelUserDto, OrelUser oldOrelUser) {

        oldOrelUser.setFirstName(orelUserDto.getFirstName());
        oldOrelUser.setMiddleName(orelUserDto.getMiddleName());
        oldOrelUser.setPhoneNo(orelUserDto.getPhoneNo());
        oldOrelUser.setLanguage(orelUserDto.getLanguage());
        oldOrelUser.setEmail(orelUserDto.getEmail());
        return oldOrelUser;
    }

    /**
     * Convert a list of OrelUser entities to a list of OrelUserDto objects.
     *
     * @param orelUserList - List of OrelUser entities.
     * @return - List of OrelUserDto objects.
     */
    public List<OrelUserDto> convertToOrelUserDtoList(List<OrelUser> orelUserList) {
        return orelUserList.stream().map(OrelUser -> {
            OrelUserDto orelUserDto = convertToDto(OrelUser);
            return orelUserDto;
        }).collect(Collectors.toList());
    }

}

