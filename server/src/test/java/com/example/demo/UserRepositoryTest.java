package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(true)

public class UserRepositoryTest {
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private TestEntityManager testEntityManager;

	@Test
	public void testCreateUser() {
		User user = new User();
		user.setUsername("admin");
		user.setPassword("123456789");
		user.setEmail("admin@mail.com");

		User saveUser = userRepository.save(user);
		
		User existUser = testEntityManager.find(User.class, saveUser.getId());
		
		assertThat(user.getUsername()).isEqualTo(existUser.getUsername());
	}
	
	@Test
	public void testFindByUsername() {
		String userName = "nhat";
		User user = userRepository.findByUsername(userName);
		
		assertThat(user.getUsername()).isEqualTo(userName);
	}
	
	@Test
	public void testUpdateUser() {
		String email = "update@gmail.com";
		User user = new User();
		user.setId( (long)11);
		user.setUsername("nhat5update");
		user.setPassword("nhat");
		user.setEmail(email);
		
		userRepository.save(user);
		
		User updatedUser = userRepository.findByUsername("nhat5update");
		assertThat(updatedUser.getEmail()).isEqualTo(email);
	}
	
	@Test
	public void testListUser() {
		List<User> users = userRepository.findAll();
		
		assertThat(users).hasSizeGreaterThan(0);
	}
	
	@Test
	public void testDeleteUser() {
		Long id = (long)2;
		
		Boolean isExist = userRepository.findById(id).isPresent();
		userRepository.deleteById(id);
		
		Boolean isNotExist = userRepository.findById(id).isPresent();
		
		assertTrue(isExist);
		assertFalse(isNotExist);
	}
	
	@Test
	public void testCountUsername() {
		List<User> users = userRepository.findAll();
		int sumUsers = users.size();
		
		System.out.println("sum users: " + sumUsers);
		assertThat(sumUsers).isGreaterThan(0);
	}
}


















