package com.orelit.springcore.testService;

import com.orelit.springcore.business.OrelUserService;
import com.orelit.springcore.common.dto.OrelUserDto;;
import com.orelit.springcore.persistence.converter.DepartmentMapper;
import com.orelit.springcore.persistence.converter.OrelUserMapper;
import com.orelit.springcore.persistence.entity.Department;
import com.orelit.springcore.persistence.entity.OrelUser;
import com.orelit.springcore.persistence.repository.OrelUserDepartmentTemplate;
import com.orelit.springcore.persistence.repository.OrelUserTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class OrelUserServiceTest {

    @InjectMocks
    private OrelUserService orelUserService;

    @Mock
    private OrelUserTemplate orelUserTemplate;

    @Mock
    private OrelUserDepartmentTemplate orelUserDepartmentTemplate;

    @Mock
    private OrelUserMapper orelUserMapper;

    @Mock
    private DepartmentMapper departmentMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Create user test case.
     */
    @Test
    public void testCreateOrelUser() {

        OrelUserDto orelUserDto = new OrelUserDto();
        orelUserDto.setPhoneNo("1234567890");
        orelUserDto.setLanguage("English");
        orelUserDto.setFirstName("Uditha");
        orelUserDto.setMiddleName("Nayanajith");
        orelUserDto.setEmail("qwe@gmail.com");
        orelUserDto.setDep_name("Engineering");
        orelUserDto.setDep_contact_no("01234567897");
        orelUserDto.setDep_email("engineering@example.com");

        when(orelUserTemplate.findByPhoneNo(orelUserDto.getPhoneNo())).thenReturn(null);
        OrelUser orelUser = null;
        when(orelUserMapper.convertToEntity(orelUserDto)).thenReturn(orelUser);
        Department department = null;
      
        when(departmentMapper.convertDepartmentDetailDtoToEntity(orelUserDto,orelUser)).thenReturn(department);
        OrelUserDto result = orelUserService.createOrelUser(orelUserDto);
        verify(orelUserDepartmentTemplate, times(1)).saveOrelUserDepartmentDetails(department);
        assertEquals(orelUserDto, result);
    }

    /**
     * Update user by phone number test case.
     */
    @Test
    public void testUpdateOrelUser() {

        OrelUserDto orelUserDto = new OrelUserDto();
        orelUserDto.setPhoneNo("1234567890");
        orelUserDto.setLanguage("English");
        orelUserDto.setFirstName("Uditha");
        orelUserDto.setMiddleName("Nayanajith");
        orelUserDto.setEmail("qwe@gmail.com");
        orelUserDto.setDep_name("Engineering");
        orelUserDto.setDep_contact_no("01234567897");
        orelUserDto.setDep_email("engineering@example.com");

        OrelUser savedOrelUser = new OrelUser();
        when(orelUserTemplate.findByPhoneNo(orelUserDto.getPhoneNo())).thenReturn(savedOrelUser);
        OrelUser updatedOrelUser = new OrelUser();
        when(orelUserMapper.convertToUpdateEntity(orelUserDto, savedOrelUser)).thenReturn(updatedOrelUser);
        Department updateDepartmentDetails = new Department();
        when(departmentMapper.convertUpdateDepartmentDetailDtoToEntity(orelUserDto, savedOrelUser.getDepartment(), savedOrelUser))
                .thenReturn(updateDepartmentDetails);
        when(orelUserTemplate.save(updatedOrelUser)).thenReturn(updatedOrelUser);
        OrelUserDto updatedDto = orelUserService.updateOrelUser(orelUserDto);
        verify(orelUserTemplate, times(1)).save(updatedOrelUser);
        assertEquals(updatedDto, orelUserMapper.convertToDto(updatedOrelUser));
    }

    /**
     * Get orel user by phone number test case.
     */
    @Test
    public void testGetOrelUserByPhoneNo() {

        String phoneNo = "1234567890";
        OrelUser orelUser = new OrelUser();
        when(orelUserTemplate.findByPhoneNo(phoneNo)).thenReturn(orelUser);
        when(orelUserMapper.convertToDto(orelUser)).thenReturn(new OrelUserDto());
        OrelUserDto userDto = orelUserService.getOrelUserByPhoneNo(phoneNo);
        verify(orelUserTemplate).findByPhoneNo(phoneNo);
        verify(orelUserMapper).convertToDto(orelUser);
        assertThat(userDto).isNotNull();
    }

    /**
     * Delete user by phone user number test case.
     */
    @Test
    public void testDeleteOrelUser() {

        String phoneNo = "1234567890";
        OrelUser user = new OrelUser();
        user.setId(1688280425227304960L);
        when(orelUserTemplate.findByPhoneNo(phoneNo)).thenReturn(user);
        Department department = new Department();
        when(orelUserDepartmentTemplate.findByUserId(user.getId())).thenReturn(department);
        orelUserService.deleteOrelUser(phoneNo);
        verify(orelUserDepartmentTemplate, times(1)).delete(department);
        verify(orelUserTemplate, times(1)).delete(user);
    }

    /**
     * Get orel user by ID test case.
     */
    @Test
    void testGetOrelUserById() {

        Long userId = 1L;
        OrelUser orelUser = null;
        when(orelUserTemplate.findById(userId)).thenReturn(orelUser);
        when(orelUserMapper.convertToDto(orelUser)).thenReturn(new OrelUserDto());
        OrelUserDto userDto = orelUserService.getOrelUserById(userId);
        verify(orelUserTemplate).findById(userId);
        verify(orelUserMapper).convertToDto(orelUser);
        assertThat(userDto).isNotNull();
    }

    /**
     * Get all users as a list test case.
     */
    @Test
    void testGetOrelUserList() {

        List<OrelUser> userList = Collections.singletonList(new OrelUser());
        when(orelUserTemplate.findAll()).thenReturn(userList);
        when(orelUserMapper.convertToOrelUserDtoList(userList)).thenReturn(Collections.singletonList(new OrelUserDto()));
        List<OrelUserDto> userDtoList = orelUserService.getOrelUserList();
        verify(orelUserTemplate).findAll();
        verify(orelUserMapper).convertToOrelUserDtoList(userList);
        assertThat(userDtoList).isNotEmpty();
    }
}
