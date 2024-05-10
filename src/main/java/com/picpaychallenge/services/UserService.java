package com.picpaychallenge.services;

import com.picpaychallenge.domain.user.User;
import com.picpaychallenge.domain.user.UserType;
import com.picpaychallenge.dtos.UserDTO;
import com.picpaychallenge.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class UserService {
  @Autowired
  private UserRepository userRepository;

  public void validateTransaction(User sender, BigDecimal amount) throws Exception {
    if (sender.getUserType() == UserType.MERCHANT) {
      throw new Exception("Merchant user not authorized");
    }

    if (sender.getBalance().compareTo(amount) < 0) {
      throw new Exception("Insufficient balance");
    }
  }

  public User findUserById(Long id) throws Exception {
    return this.userRepository.findUserById(id).orElseThrow(() -> new Exception("User not found!"));
  }

  public User createUser(UserDTO userDTO) {
    User newUser = new User(userDTO);
    this.saveUser(newUser);
    return newUser;
  }

  public List<User> getAllUsers() {
    return this.userRepository.findAll();
  }

  public void saveUser(User user) {
    this.userRepository.save(user);
  }
}
