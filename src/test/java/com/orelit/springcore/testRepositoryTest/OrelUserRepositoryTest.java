package com.orelit.springcore.testRepositoryTest;
import com.orelit.springcore.persistence.entity.OrelUser;
import com.orelit.springcore.persistence.repository.OrelUserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
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
    private TestEntityManager entityManager;

    @Test
    public void testFindByPhoneNo() {

        OrelUser orelUser = new OrelUser();
        orelUser.setPhoneNo("+941234567800");
        orelUser.setLanguage("English");
        orelUser.setMiddleName("Nayanajith");
        orelUser.setEmail("qwe@gmail.com");

        entityManager.persist(orelUser);
        entityManager.flush();

        Optional<OrelUser> foundUser = orelUserRepository.findByPhoneNo("+941234567890");

//        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getPhoneNo()).isEqualTo("+941234567890");
    }

    @Test
    public void testFindByPhoneNo_NotFound() {

        Optional<OrelUser> notFoundUser = orelUserRepository.findByPhoneNo("+1234567890");
        assertThat(notFoundUser).isEmpty();
    }
}
