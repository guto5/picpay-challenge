package com.picpaychallenge.repositories;

import com.picpaychallenge.domain.user.User;
import com.picpaychallenge.domain.user.UserType;
import com.picpaychallenge.dtos.UserDTO;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {

  @Autowired
  UserRepository userRepository;

  @Autowired
  EntityManager entityManager;

  @Test
  @DisplayName("Should get user successfully from DB")
  void findUserByDocumentCase1() {
    String document = "01232133480";
    UserDTO data = new UserDTO("August", "Test", document, new BigDecimal(100), "test@gmail.com", "123", UserType.COMMON);
    this.createUser(data);

    Optional<User> result = this.userRepository.findUserByDocument(document);
    assertThat(result.isPresent()).isTrue();
  }


  @Test
  @DisplayName("Should not get User from DB when user not exist ")
  void findUserByDocumentCase2() {
    String document = "01232133480";
    Optional<User> result = this.userRepository.findUserByDocument(document);
    assertThat(result.isEmpty()).isTrue();
  }

  private User createUser(UserDTO data) {
    User newUser = new User(data);
    this.entityManager.persist(newUser);
    return newUser;
  }
}