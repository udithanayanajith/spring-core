package com.orelit.springcore.testService;

import com.orelit.springcore.business.OrelUserService;
import com.orelit.springcore.common.dto.OrelUserDto;
import com.orelit.springcore.common.exception.NotFoundException;
import com.orelit.springcore.common.exception.PropertyValidationException;
import com.orelit.springcore.persistence.converter.DepartmentMapper;
import com.orelit.springcore.persistence.converter.OrelUserMapper;
import com.orelit.springcore.persistence.entity.Department;
import com.orelit.springcore.persistence.entity.OrelUser;
import com.orelit.springcore.persistence.repository.OrelUserTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class OrelUserServiceTest {

    @InjectMocks
    private OrelUserService orelUserService;

    @Mock
    private OrelUserTemplate orelUserTemplate;

    @Mock
    private OrelUserMapper orelUserMapper;
    @Mock
    private DepartmentMapper departmentMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateOrelUser() {
        OrelUserDto orelUserDto = new OrelUserDto();
        orelUserDto.setPhoneNo("+941234567890");
        orelUserDto.setLanguage("English");
        orelUserDto.setFirstName("Uditha");
        orelUserDto.setMiddleName("Nayanajith");
        orelUserDto.setEmail("qwe@gmail.com");
        orelUserDto.setDep_name("Engineering");
        orelUserDto.setDep_contact_no("+941234567897");
        orelUserDto.setDep_email("engineering@example.com");

        OrelUser orelUser = new OrelUser();
        when(orelUserMapper.convertToEntity(orelUserDto)).thenReturn(orelUser);
        when(orelUserTemplate.save(any(OrelUser.class))).thenReturn(new OrelUser());

        OrelUserDto createdUser = orelUserService.createOrelUser(orelUserDto);

        verify(orelUserMapper).convertToEntity(orelUserDto);
        verify(orelUserTemplate).save(any(OrelUser.class));

//        assertThat(createdUser).isNotNull();
    }

    @Test
    void testUpdateOrelUser() {
        OrelUserDto orelUserDto = new OrelUserDto();
        orelUserDto.setPhoneNo("+941234567890");
        orelUserDto.setLanguage("English");
        orelUserDto.setFirstName("Uditha");
        orelUserDto.setMiddleName("Nayanajith");
        orelUserDto.setEmail("qwe@gmail.com");
        orelUserDto.setDep_name("Engineering");
        orelUserDto.setDep_contact_no("+941234567897");
        orelUserDto.setDep_email("engineering@example.com");

        when(orelUserTemplate.findByPhoneNo(orelUserDto.getPhoneNo())).thenReturn(new OrelUser());
        when(orelUserMapper.convertToUpdateEntity(eq(orelUserDto), any())).thenReturn(new OrelUser());
        when(departmentMapper.convertUpdateDepartmentDetailDtoToEntity(eq(orelUserDto), any(), any())).thenReturn(new Department());
        when(orelUserTemplate.save(any(OrelUser.class))).thenReturn(new OrelUser());


        OrelUserDto updatedUser = orelUserService.updateOrelUser(orelUserDto);


        verify(orelUserTemplate).findByPhoneNo(orelUserDto.getPhoneNo());
        verify(orelUserMapper).convertToUpdateEntity(eq(orelUserDto), any());
        verify(departmentMapper).convertUpdateDepartmentDetailDtoToEntity(eq(orelUserDto), any(), any());
        verify(orelUserTemplate).save(any(OrelUser.class));

//        assertThat(updatedUser).isNotNull();
    }

    @Test
    void testGetOrelUserByPhoneNo() {
        String phoneNo = "+941234567890";
        OrelUser orelUser = new OrelUser();
        when(orelUserTemplate.findByPhoneNo(phoneNo)).thenReturn(orelUser);
        when(orelUserMapper.convertToDto(orelUser)).thenReturn(new OrelUserDto());

        OrelUserDto userDto = orelUserService.getOrelUserByPhoneNo(phoneNo);

        verify(orelUserTemplate).findByPhoneNo(phoneNo);
        verify(orelUserMapper).convertToDto(orelUser);

        assertThat(userDto).isNotNull();
    }

    @Test
    void testDeleteOrelUser() {
        String phoneNo = "+941234567890";
        OrelUser orelUser = new OrelUser();
        when(orelUserTemplate.findByPhoneNo(phoneNo)).thenReturn(orelUser);

        orelUserService.deleteOrelUser(phoneNo);

        verify(orelUserTemplate).findByPhoneNo(phoneNo);
        verify(orelUserTemplate).save(orelUser);
    }

    @Test
    void testGetOrelUserById() {
        Long userId = 1L;
        OrelUser orelUser = new OrelUser();
        when(orelUserTemplate.findById(userId)).thenReturn(orelUser);
        when(orelUserMapper.convertToDto(orelUser)).thenReturn(new OrelUserDto());

        OrelUserDto userDto = orelUserService.getOrelUserById(userId);

        verify(orelUserTemplate).findById(userId);
        verify(orelUserMapper).convertToDto(orelUser);

        assertThat(userDto).isNotNull();
    }

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

    @Test
    void testInvalidIdValidation() {
        Long invalidId = -1L;
        PropertyValidationException exception = assertThrows(PropertyValidationException.class, () -> orelUserService.getOrelUserById(invalidId));
        assertThat(exception.getMessage()).isEqualTo("OrelUser id Id not valid!");
    }

    @Test
    void testNonExistingPhoneNo() {
        String phoneNo = "+941234567890";
        when(orelUserTemplate.findByPhoneNo(phoneNo)).thenReturn(null);

        NotFoundException exception = assertThrows(NotFoundException.class, () -> orelUserService.getOrelUserByPhoneNo(phoneNo));
        assertThat(exception.getMessage()).isEqualTo("OrelUser not found!");
    }

    @Test
    void testExistingPhoneNoOnCreate() {
        String phoneNo = "+941234567890";
        when(orelUserTemplate.findByPhoneNo(phoneNo)).thenReturn(new OrelUser());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> orelUserService.createOrelUser(new OrelUserDto()));
        assertThat(exception.getMessage()).isEqualTo("Phone no already exists!");
    }
}
