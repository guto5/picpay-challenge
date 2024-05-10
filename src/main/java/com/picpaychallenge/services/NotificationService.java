package com.picpaychallenge.services;

import com.picpaychallenge.domain.user.User;
import com.picpaychallenge.dtos.NotificationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class NotificationService {
  @Autowired
  RestTemplate restTemplate;

  public void sendNotification(User user, String message) throws Exception {
    String email = user.getEmail();
    NotificationDTO notificationRequest = new NotificationDTO(email, message);

    ResponseEntity<String> notificationResponse = restTemplate.postForEntity("https://run.mocky.io/v3/54dc2cf1-3add-45b5-b5a9-6bf7e7f1f4a6", notificationRequest, String.class);

    if (!(notificationResponse.getStatusCode() == HttpStatus.OK)) {
      System.out.println("Error while sending notification");
      throw new Exception("Notification service is currently offline!");
    }
  }
}
