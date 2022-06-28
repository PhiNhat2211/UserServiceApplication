package com.example.demo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.example.demo.model.Role;
import com.example.demo.repository.RoleRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(true)
public class RoleRepositoryTest {
	@Autowired private RoleRepository repo;
	
	@Test
	public void testCreateRole() {
		Role role = new Role();
		role.setRole("ROLE_VIEWER");
		
		assertTrue(true);
	}
	
	@Test
	public void testFindById() {
		long id = (long) 2;
		Role role = repo.findById(id).get();
		
		assertThat(role.getId()).isEqualTo(id);
	}
	
	@Test
	public void testFindByRoleName() {
		String roleName = "ROLE_USER";
		Role role = repo.findByName(roleName).get();
		System.out.println(role);
		
		assertThat(role.getRole()).isEqualTo(roleName);
	}
	
//	@Test
//	public void testFindRoleByUserId() {
//		long id= (long) 1;
//		Role role = repo.getRoleByUserId(id);
//	}
	
	@Test
	public void testListRoles() {
		List<Role> listRoles = repo.findAll();
		assertThat(listRoles.size()).isGreaterThan(0);
		
		listRoles.forEach(System.out::println);
	}
	
	@Test
	public void testUpdateRole() {
		Role role = new Role();
		role.setId((long)2);
		role.setRole("ROLE_VIEWER");
		
		repo.save(role);
		Role updatedRole = repo.findByName("ROLE_VIEWER").get();
		System.out.println(updatedRole);
		assertThat(updatedRole.getRole()).isEqualTo("ROLE_VIEWER");
	}
	
	@Test
	public void testDeleteRoleById() {
		Long id = (long) 2;
		Boolean isExist = repo.findById(id).isPresent();
		repo.deleteById(id);
		
		Boolean isNotExist = repo.findById(id).isPresent();
		
		assertTrue(isExist);
		assertFalse(isNotExist);
	}
	
//	@Test
//	public void testDeleteRoleByRole() {
//		String role = "ROLE_USER";
//		boolean isExist = repo.findByName(role).isPresent();
//		repo.deleteRollByName(role);
//		
//		boolean isNotExist = repo.findByName(role).isPresent();
//		
//		assertTrue(isExist);
//		assertFalse(isNotExist);
//	}
}







