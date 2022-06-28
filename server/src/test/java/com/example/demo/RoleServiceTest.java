package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.example.demo.model.Role;
import com.example.demo.repository.RoleRepository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(true)

public class RoleServiceTest {
    @Autowired
	private RoleRepository roleRepository;

    @Test
    public void addRoleTest() {
        Role role = new Role(
            "Employee"
        );

        roleRepository.save(role);
		assertThat(role.getRole()).isEqualTo("Employee");
    }

    @Test
    public void getRoleByNameTest() {
        String name ="ROLE_ADMIN";
		Optional<Role> roleOptional = roleRepository.findByName(name);
        Role role = roleOptional.get();

		assertThat(role.getRole()).isEqualTo("ROLE_ADMIN");
    }

    @Test
    public void getRoleByIdTest() {
        long id = (long) 1;
		Role role = roleRepository.getById(id);

		assertThat(role.getId()).isEqualTo(1);
    }
}
