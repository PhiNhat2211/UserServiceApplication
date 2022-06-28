package com.example.demo.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.security.RolesAllowed;

import com.example.demo.model.AuthRequest;
import com.example.demo.model.AuthResponse;
import com.example.demo.model.User;
import com.example.demo.model.Views;
import com.example.demo.repository.RoleRepository;
import com.example.demo.service.RoleService;
import com.example.demo.service.UserService;
import com.example.demo.util.JwtUtil;
import com.example.demo.util.ResponseUtil;
import com.fasterxml.jackson.annotation.JsonView;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class UserController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserService userService;

    @Autowired
    private ResponseUtil responseUtil;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    // API GET ALL USERS
    @GetMapping("/api/users")
    @RolesAllowed({"ROLE_ADMIN", "ROLE_USER"})
    @JsonView(Views.External.class)
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    // API GET USER BY ID
    @GetMapping("/api/user/{id}")
    @JsonView(Views.External.class)
    public User getUserById(@PathVariable("id") Long id) {
        return userService.getUserById(id);
    } 

    // API DELETE USER BY ID
    @DeleteMapping("/api/admin/{id}")
    @RolesAllowed("ROLE_ADMIN")
    public ResponseEntity<Object> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return responseUtil.successResponseEntity("Account deleted successfully");
    }

    // API UPDATE ROLE BY ID
    @PutMapping("/api/admin/{user_id}/{role_id}")
    @RolesAllowed("ROLE_ADMIN")
    public ResponseEntity<Object> updateRoleById(@PathVariable Long user_id, @PathVariable Long role_id) {
        roleService.updateRoleById(user_id, role_id);
        return responseUtil.successResponseEntity("Role updated successfully");
    }
    
    // API LOGIN 
    @Autowired AuthenticationManager authManager;
    @Autowired RoleRepository roleRepository;
    @PostMapping("/auth/login")
    public ResponseEntity<?> login2(@RequestBody AuthRequest request) {
    	if(request.getUsername() == "" || request.getPassword() == ""){
            return responseUtil.failResponseEntity("Please fill everyfield");
        }

        try {
    		Authentication authentication = authManager.authenticate(
    				new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
    				);
    		User user = (User) authentication.getPrincipal();
    		
    		String accessToken = jwtUtil.generateAccessToken(user);
    		String role = roleRepository.getRoleByUserId(user.getId());

    		AuthResponse response = new AuthResponse(user.getId(), user.getUsername(), accessToken, role, user.getEmail());
    		
    		return ResponseEntity.ok(response);
    		
    	}catch (Exception ex) {
    		return responseUtil.failResponseEntity("Wrong username or password");
    	}
    }

    // API SIGN UP NEW USER
    @PostMapping("/auth/signup")
    public ResponseEntity<?> register(@RequestBody Map<String, Object> payload) throws Exception {
        String email = null;
        String username = null;
        String password = null;
        try {
            email = payload.get("email").toString();
            password = payload.get("password").toString();

            // avoid bCryptPasswordEncoder encode an empty password
            if (password.length() > 0) {
                password = bCryptPasswordEncoder.encode(password);
            }
            username = payload.get("username").toString();
        } catch (Exception e) {
            return responseUtil.failResponseEntity("Missing value field !");
        }
        User user = new User(username, password, email);

        userService.addNewUser(user);

        return responseUtil.successResponseEntity("Account created successfully");
    }

    // API UPDATE USER
    @PutMapping("/api/user/{user_id}")
    public ResponseEntity<?> updateUser(@RequestBody Map<String, Object> payload, @PathVariable("user_id") Long user_id) throws Exception {
        String email = null;
        String username = null;
        String password = null;
        try {
            email = payload.get("email").toString();
            password = payload.get("password").toString();
            username = payload.get("username").toString();
            if(email == "" && password == "" && username == ""){
                return responseUtil.failResponseEntity("Update atleast a field");
            }
        } catch (Exception e) {
            return responseUtil.failResponseEntity("Missing value field !");
        }
        User user = new User(username, password, email);
        userService.updateUser(user, user_id);

        return responseUtil.successResponseEntity("Account updated successfully");
    }
}
