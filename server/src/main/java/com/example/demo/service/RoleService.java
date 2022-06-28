package com.example.demo.service;

import java.util.Optional;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RoleService {

  @Autowired
  private RoleRepository roleRepository;

  @Autowired
  private UserRepository userRepository;

  @Transactional(readOnly = true)
  public Role getRoleById(Long id){
    Optional<Role> roleOptional = roleRepository.findById(id);
    if (!roleOptional.isPresent()){
      throw new IllegalStateException("Id does not exist.");
    }
    return roleOptional.get();
  }

  @Transactional(readOnly = true)
  public Role getRoleByName(String name){
    Optional<Role> roleOptional = roleRepository.findByName(name);
    if (!roleOptional.isPresent()){
      throw new IllegalStateException("Role does not exist.");
    }
    return roleOptional.get();
  }

  @Transactional(rollbackFor = Exception.class)
  public Role addRole(String name) {
    Role role = new Role(name);
    roleRepository.save(role);
    return role;
  }

  @Transactional(rollbackFor = Exception.class)
  public void updateRoleById(Long user_id, Long role_id){
    Optional<User> userOptional = userRepository.findById(user_id);
		if (!userOptional.isPresent()) {
			throw new ResourceNotFoundException("user not found with id = " + user_id);
		}
    roleRepository.updateRoleById(user_id, role_id);
  }
  
}
