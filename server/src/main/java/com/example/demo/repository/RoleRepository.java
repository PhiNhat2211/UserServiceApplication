package com.example.demo.repository;

import java.util.Optional;

import javax.transaction.Transactional;

import com.example.demo.model.Role;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface RoleRepository extends JpaRepository<Role, Long> {
	
  // @Query(value = "SELECT r.* FROM roles r INNER JOIN users_roles ur ON r.id = ur.role_id WHERE ur.user_id = ?1", nativeQuery = true)
  // public List<Role> findRolesByUserID(Long id);

  @Query(value = "SELECT * FROM roles r WHERE r.role = ?1", nativeQuery = true)
  Optional<Role> findByName(String name);

  @Query(value = "DELETE FROM roles WHERE role = ?1", nativeQuery = true)
  Role deleteRollByName(String name);

  @Query(value = "SELECT role_id FROM users_roles where user_id = ?1", nativeQuery = true)
  Long getRoleById(Long id);

  @Query(value = "SELECT r.role FROM roles r INNER JOIN users_roles ur ON r.id = ur.role_id WHERE ur.user_id = ?1", nativeQuery = true)
  String getRoleByUserId(Long id);

  @Modifying
  @Transactional
  @Query(value = "UPDATE users_roles SET role_id= ?2 WHERE user_id= ?1 ", nativeQuery = true)
  void updateRoleById(Long user_id, Long role_id);

}