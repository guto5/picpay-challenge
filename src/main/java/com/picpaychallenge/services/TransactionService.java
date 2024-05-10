package com.picpaychallenge.services;

import com.picpaychallenge.domain.transaction.Transaction;
import com.picpaychallenge.domain.user.User;
import com.picpaychallenge.dtos.TransactionDTO;
import com.picpaychallenge.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TransactionService {
  @Autowired
  private TransactionRepository transactionRepository;

  @Autowired
  private UserService userService;

  @Autowired
  private AuthorizationService authorizationService;

  @Autowired
  private NotificationService notificationService;

  public Transaction createTransaction(TransactionDTO transactionDTO) throws Exception {
    User sender = this.userService.findUserById(transactionDTO.senderId());
    User receiver = this.userService.findUserById(transactionDTO.receiverId());

    userService.validateTransaction(sender, transactionDTO.value());

    boolean isAuthorized = this.authorizationService.authorizeTransaction(sender, transactionDTO.value());
    if (!isAuthorized) {
      throw new Exception("Transaction not allowed");
    }

    Transaction transaction = new Transaction();
    transaction.setAmount(transactionDTO.value());
    transaction.setSender(sender);
    transaction.setReceiver(receiver);
    transaction.setTimeStamp(LocalDateTime.now());

    sender.setBalance(sender.getBalance().subtract(transactionDTO.value()));
    receiver.setBalance(receiver.getBalance().add(transactionDTO.value()));

    this.transactionRepository.save(transaction);
    this.userService.saveUser(sender);
    this.userService.saveUser(receiver);

    this.notificationService.sendNotification(sender, "Your transaction was successful!");
    this.notificationService.sendNotification(receiver, "Transaction received successfully!");


    return transaction;
  }


}
