package com.picpaychallenge.controllers;

import com.picpaychallenge.domain.user.User;
import com.picpaychallenge.dtos.UserDTO;
import com.picpaychallenge.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

  @Autowired
  private UserService userService;

  @PostMapping
  public ResponseEntity<User> createUser(@RequestBody UserDTO userDto) {
    User newUser = userService.createUser(userDto);
    return new ResponseEntity<>(newUser, HttpStatus.CREATED);
  }

  @GetMapping
  public ResponseEntity<List<User>> getAllUsers() {
    List<User> userList = this.userService.getAllUsers();
    return new ResponseEntity<List<User>>(userList, HttpStatus.OK);
  }
}
