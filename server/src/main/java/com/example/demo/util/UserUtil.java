package com.example.demo.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.exception.WrongInputException;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.util.UserUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserUtil {

    @Autowired
    private UserRepository userRepository;

    public boolean checkRegex(String regex, String string) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(string);
        boolean matchFound = matcher.find();
        return matchFound;
    }

    public void inputChecking(User user) {
        if (user.getUsername().length() < 1) {
            throw new WrongInputException("Username is needed");
        }

        if (user.getPassword().length() < 1) {
            throw new WrongInputException("Password is needed");
        }

        if (user.getEmail().length() < 1) {
            throw new WrongInputException("Email is needed");
        }
        boolean nameMatchFound = checkRegex("^[A-Za-z0-9]*$", user.getUsername());
        if (!nameMatchFound) {
            throw new WrongInputException("Username can only be alphabets and numbers");
        }
        boolean emailMatchFound = checkRegex("[a-zA-Z0-9]{1,}[@]{1}[a-z]{5,}[.]{1}+[a-z]{3}", user.getEmail());
        if (!emailMatchFound) {
            throw new WrongInputException("Email pattern wrong");
        }
        boolean passwordMatchFound = checkRegex("^[A-Za-z0-9!@#$%^&*-_]*$", user.getPassword());
        if (!passwordMatchFound) {
            throw new WrongInputException("Password can only be alphabets and numbers and !@#$%^&*-_");
        }
    }

    public void existUserChecking(User user) {
        int numberUsername = userRepository.countUsername(user.getUsername());
        if (numberUsername > 0) {
            throw new ResourceNotFoundException("Username has been taken");
        }

        int numberEmail = userRepository.countEmail(user.getEmail());
        if (numberEmail > 0) {
            throw new ResourceNotFoundException("Email has been taken");
        }
    }
}
