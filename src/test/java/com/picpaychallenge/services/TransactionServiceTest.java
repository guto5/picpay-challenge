package com.picpaychallenge.services;

import com.picpaychallenge.domain.user.User;
import com.picpaychallenge.domain.user.UserType;
import com.picpaychallenge.dtos.TransactionDTO;
import com.picpaychallenge.repositories.TransactionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TransactionServiceTest {

  @Mock
  private TransactionRepository transactionRepository;

  @Mock
  private UserService userService;

  @Mock
  private AuthorizationService authorizationService;

  @Mock
  private NotificationService notificationService;

  @InjectMocks
  private TransactionService transactionService;

  @BeforeEach
  void setup() {
    MockitoAnnotations.initMocks(this);
  }

  @DisplayName("Should create transcation successfully")
  @Test
  void createTransactionCase1() throws Exception {
    User sender = new User(1L, "Augustinho", "Testes", "91222233347", "augustinho@gmail.com", "123", new BigDecimal(100), UserType.COMMON);
    User receiver = new User(2L, "Bernardo", "Celular", "9192223347", "bernardo@gmail.com", "1234", new BigDecimal(100), UserType.COMMON);

    when(userService.findUserById(1L)).thenReturn(sender);
    when(userService.findUserById(2L)).thenReturn(receiver);

    when(authorizationService.authorizeTransaction(any(), any())).thenReturn(true);

    TransactionDTO requestTransaction = new TransactionDTO(new BigDecimal(50), 1L, 2L);
    transactionService.createTransaction(requestTransaction);

    verify(transactionRepository, times(1)).save(any());

    sender.setBalance(new BigDecimal(50));
    verify(userService, times(1)).saveUser(sender);

    receiver.setBalance(new BigDecimal(150));
    verify(userService, times(1)).saveUser(receiver);

    verify(notificationService, times(1)).sendNotification(sender, "Your transaction was successful!");
    verify(notificationService, times(1)).sendNotification(receiver, "Transaction received successfully!");

  }

  @DisplayName("Should throw Exception when transaction is not allowed")
  @Test
  void createTransactionCase2() throws Exception{
    User sender = new User(1L, "Augustinho", "Testes", "91222233347", "augustinho@gmail.com", "123", new BigDecimal(100), UserType.COMMON);
    User receiver = new User(2L, "Bernardo", "Celular", "9192223347", "bernardo@gmail.com", "1234", new BigDecimal(100), UserType.COMMON);

    when(userService.findUserById(1L)).thenReturn(sender);
    when(userService.findUserById(2L)).thenReturn(receiver);

    when(authorizationService.authorizeTransaction(any(), any())).thenReturn(false);

    Exception thrown = Assertions.assertThrows(Exception.class, () -> {
      TransactionDTO requestTransaction = new TransactionDTO(new BigDecimal(50), 1L, 2L);
      transactionService.createTransaction(requestTransaction);
    });

    Assertions.assertEquals("Transaction not allowed", thrown.getMessage());
  }
}