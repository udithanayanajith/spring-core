package com.orelit.springcore.testRepositoryTest;

import com.orelit.springcore.persistence.entity.Department;
import com.orelit.springcore.persistence.entity.OrelUser;
import com.orelit.springcore.persistence.repository.OrelUserDepartmentRepository;
import com.orelit.springcore.persistence.repository.OrelUserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * OrelUserRepositoryTest
 * Author: udithan
 * Date: 02-Oct-23
 */

@DataJpaTest
public class OrelUserRepositoryTest {
    @Autowired
    private OrelUserRepository orelUserRepository;

    @Autowired
    private OrelUserDepartmentRepository orelUserDepartmentRepository;

    @Autowired
    private TestEntityManager entityManager;

    /**
     * Find Orel User By phone number test case.
     */
    @Test
    public void testFindByPhoneNo() {

        OrelUser orelUser = new OrelUser();
        orelUser.setPhoneNo("0123456789");
        orelUser.setLanguage("English");
        orelUser.setMiddleName("Nayanajith");
        orelUser.setEmail("qwe@gmail.com");

        entityManager.persist(orelUser);
        entityManager.flush();

        Optional<OrelUser> foundUser = orelUserRepository.findByPhoneNo("0123456789");

        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getPhoneNo()).isEqualTo("0123456789");
    }

    /**
     * Find by Departments By given user ID.
     */

    @Test
    public void testFindByOrelUserId() {

        Department department = new Department();
        department.setDep_name("Sample Department");
        department.setDep_contact_no("1234567890");
        department.setDep_email("sample@example.com");

        OrelUser orelUser = new OrelUser();
        orelUser.setPhoneNo("0123456789");
        orelUser.setLanguage("English");
        orelUser.setMiddleName("Nayanajith");
        orelUser.setEmail("qwe@gmail.com");
        orelUser.setDepartment(department);

        department.setOrelUser(orelUser);
        orelUserDepartmentRepository.save(department);
        Optional<Department> foundDepartment = orelUserDepartmentRepository.findByOrelUserId(orelUser.getId());
        assertTrue(foundDepartment.isPresent());
        assertEquals(department.getId(), foundDepartment.get().getId());
    }


}
