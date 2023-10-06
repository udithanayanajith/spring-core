package com.orelit.springcore.testPresentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.orelit.springcore.business.OrelUserService;
import com.orelit.springcore.common.constant.ApiConstant;
import com.orelit.springcore.common.dto.OrelUserDto;
import com.orelit.springcore.persistence.entity.OrelUser;
import com.orelit.springcore.presentation.OrelUserController;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * OrelUserControllerTest
 * Author: udithan
 * Date: 11-Sep-23
 */
public class OrelUserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private OrelUserService orelUserService;
    @Autowired
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(new OrelUserController(orelUserService)).build();
    }

    /**
     * Test for  Orel User save.
     *
     * @throws Exception
     */
    @Test
    public void testCreateorelUser() throws Exception {

        OrelUserDto orelUserDto = new OrelUserDto();
        orelUserDto.setPhoneNo("+941234567890");
        orelUserDto.setLanguage("English");
        orelUserDto.setFirstName("Uditha");
        orelUserDto.setMiddleName("Nayanajith");
        orelUserDto.setEmail("qwe@gmail.com");
        orelUserDto.setDep_name("Engineering");
        orelUserDto.setDep_contact_no("+941234567897");
        orelUserDto.setDep_email("engineering@example.com");
        when(orelUserService.createOrelUser(any())).thenReturn(orelUserDto);
        mockMvc.perform(post(ApiConstant.BASE_PATH + "/OrelUser").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(orelUserDto)).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

    /**
     * Test for Update Orel User.
     *
     * @throws Exception
     */
    @Test
    public void testUpdateOrelUser() throws Exception {

        OrelUserDto orelUserDto = new OrelUserDto();
        orelUserDto.setPhoneNo("+941234567800");
        orelUserDto.setLanguage("English");
        orelUserDto.setFirstName("Uditha");
        orelUserDto.setMiddleName("Nayanajith");
        orelUserDto.setEmail("qwe@gmail.com");
        orelUserDto.setDep_name("Engineering");
        orelUserDto.setDep_contact_no("+941234567898");
        orelUserDto.setDep_email("engineering@example.com");

        when(orelUserService.updateOrelUser(any(OrelUserDto.class))).thenReturn(orelUserDto);
        ResultActions resultActions = mockMvc.perform(put(ApiConstant.BASE_PATH + "/OrelUser").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(orelUserDto)).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }


    /**
     * Test for Delete  Orel User by  Orel User phone number.
     *
     * @throws Exception
     */

    @Test
    public void testDeleteorelUser() throws Exception {

        String phoneNoToDelete = "0123456789";
        OrelUser user = new OrelUser();
        user.setId(1L);
        mockMvc.perform(delete(ApiConstant.BASE_PATH + "/OrelUser").contentType(MediaType.APPLICATION_JSON).param("phoneNo", phoneNoToDelete).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

    }

    /**
     * Test for get  Orel User by  Orel User Id.
     *
     * @throws Exception
     */

    @Test
    public void testGetOrelUserById() throws Exception {

        Long userId = 1681209342040948736L;
        mockMvc.perform(get(ApiConstant.BASE_PATH + "/OrelUser/{id}", userId).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }


    /**
     * @throws Exception
     */
    @Test
    public void testGetOrelUserList() throws Exception {

        mockMvc.perform(get(ApiConstant.BASE_PATH + "/OrelUser/list").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

    }


}