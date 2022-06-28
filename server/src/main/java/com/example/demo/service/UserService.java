package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.exception.WrongInputException;
import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.util.UserUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private RoleService roleService;

	@Autowired
	private UserUtil userUtil;

	@Transactional(readOnly = true)
	public List<User> getAllUsers() {
		return userRepository.findAllUsers();
	}
	
	@Transactional(rollbackFor = Exception.class)
	public void addNewUser(User user) {
		// Checking if field missing or wrong input data
		userUtil.inputChecking(user);
		// Checking if username or email exists
		userUtil.existUserChecking(user);
		// Give role of user as default
		Role defaultRole = roleService.getRoleByName("ROLE_USER");
		user.getRoles().add(defaultRole);
		//Save
		userRepository.save(user);

	}

	@Transactional(rollbackFor = Exception.class)
	public void updateUser(User user, Long id) {
		Optional<User> userOptional = userRepository.findById(id);
		//Check if user exists
		if (!userOptional.isPresent()) {
			throw new ResourceNotFoundException("user not found with id = " + id);
		}
		User updatedUser = userOptional.get();
		String password = null;

		// Checking if username or email exists before saving
		userUtil.existUserChecking(user);

		// Check if value match regex 
		if (!user.getEmail().isEmpty()) {
			boolean emailMatchFound = userUtil.checkRegex("[a-zA-Z0-9]{1,}[@]{1}[a-z]{5,}[.]{1}+[a-z]{3}",
					user.getEmail());
			if (!emailMatchFound) {
				throw new WrongInputException("Email pattern wrong");
			}
			updatedUser.setEmail(user.getEmail());
		}

		if (!user.getUsername().isEmpty()) {
			boolean nameMatchFound = userUtil.checkRegex("^[A-Za-z0-9]*$", user.getUsername());
			if (!nameMatchFound) {
				throw new WrongInputException("Username can only be alphabets and numbers");
			}
			updatedUser.setUsername(user.getUsername());
		}

		if (!user.getPassword().isEmpty()) {
			boolean passwordMatchFound = userUtil.checkRegex("^[A-Za-z0-9!@#$%^&*-_]*$", user.getPassword());
			if (!passwordMatchFound) {
				throw new WrongInputException("Password can only be alphabets and numbers and !@#$%^&*-_");
			}
			// Encrypt the updated password
			password = bCryptPasswordEncoder.encode(user.getPassword());
			updatedUser.setPassword(password);
		}

		try {
			userRepository.save(updatedUser);
		} catch (Exception e) {
			throw new WrongInputException("missing field value");
		}

	}

	@Transactional(readOnly = true)
	public User getUserById(Long id) {
		Optional<User> userOptional = userRepository.findById(id);
		if (!userOptional.isPresent()) {
			throw new ResourceNotFoundException("user not found with id = " + id);
		}
		return userOptional.get();
	}
	

	
	@Transactional(readOnly = true)
	public List<User> getUsersWithUsernameLike(String username) {
		if (userRepository.findByUsernameContaining(username).isEmpty()) {
			throw new ResourceNotFoundException("user not found with username = " + username);
		}
		return userRepository.findByUsernameContaining(username);
	}

	// @Transactional(rollbackFor = Exception.class)
	public void deleteUser(Long id) {
		Optional<User> userOptional = userRepository.findById(id);
		if (!userOptional.isPresent()) {
			throw new ResourceNotFoundException("user not found with id = " + id);
		}
		userRepository.deleteById(id);
	}

	@Transactional(readOnly = true)
	public User getUserWithNameAndPass(String username, String password) {
		User user = userRepository.findByUsername(username);
		user = userRepository.findByUsernameAndPassword(username, user.getPassword());
		return user;
	}

	@Transactional(readOnly = true)
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username);
		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
				new ArrayList<>());

	}
}
