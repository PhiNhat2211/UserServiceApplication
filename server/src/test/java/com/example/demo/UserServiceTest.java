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

import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(true)

public class UserServiceTest {
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private TestEntityManager testEntityManager;

	@Test
	public void getAllUsersTest() {
		userRepository.findAllUsers();
	}

	@Test
	public void addNewUserTest() {
		User user = new User(
			"test123",
			"1234",
			"test123@gmail.com"
		);
		
		User saveUser = userRepository.save(user);
		
		User existUser = testEntityManager.find(User.class, saveUser.getId());
		
		assertThat(user.getUsername()).isEqualTo(existUser.getUsername());
	}

	@Test
	public void updateUserTest(){
		Optional<User> userOptional = userRepository.findById((long) 1);
		User user = userOptional.get();
		user.setUsername("test12");
		user.setPassword("123456");
		user.setEmail("test12@gmail.com");

		userRepository.save(user);
		assertThat(user.getUsername()).isEqualTo("test12");
	}

	@Test
	public void getUserByIdTest(){
		long id = (long) 1;
		User user = userRepository.getById(id);

		assertThat(user.getId()).isEqualTo(1);
	}

	@Test
	public void getUsersWithUsernameLikeTest(){
		String name ="nhat";
		User user = userRepository.findByUsername(name);

		assertThat(user.getUsername()).isEqualTo("nhat");
	}

	@Test
	public void deleteUserTest(){
		userRepository.deleteById((long) 1);
		assertThat(userRepository.findById((long) 1)).isEmpty();
	}

}
