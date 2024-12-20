package com.example.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.HttpStatus;
import com.example.service.AccountService;
import com.example.service.MessageService;
import com.example.entity.Account;
import com.example.entity.Message;

import java.util.List;

/*
 * Class to create an object for the incoming meesage text to be able to deserialize the json into.
 */
class MessageUpdateRequest {
    private String messageText;

    // Getters and setters
    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }
}

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
@RestController
public class SocialMediaController {    
    private final AccountService accountService;
    private final MessageService messageService;

    // Constructor injection
    public SocialMediaController(AccountService accountService, MessageService messageService) {
        this.accountService = accountService;
        this.messageService = messageService;
    }

    @GetMapping("/accounts/{accountId}/messages")
    public ResponseEntity getAllMessagesByAccountId(@PathVariable Integer accountId) {
        List<Message> list = messageService.getAllMessagesByAccountId(accountId);
        return ResponseEntity.status(200).body(list);
    }

    @PatchMapping("/messages/{id}")
    public ResponseEntity updateMessageById(@PathVariable Integer id, @RequestBody MessageUpdateRequest request) {
        String messageText = request.getMessageText();
        Boolean wasUpdated = messageService.updateMessageById(id, messageText);
        if (wasUpdated == false) {
            return ResponseEntity.status(400).body("Bad request.");    
        } 
        else {
            return ResponseEntity.status(200).body(1);
        }
    }

    @DeleteMapping("/messages/{id}")
    public ResponseEntity deleteMessageById(@PathVariable Integer id) {
        Boolean wasDeleted = messageService.deleteMessageById(id);
        return ResponseEntity.status(200).body(wasDeleted ? 1 : null);
    }

    @GetMapping("/messages/{id}")
    public ResponseEntity getMessageById(@PathVariable Integer id) {
        Message message = messageService.getMessageById(id);
        return ResponseEntity.status(200).body(message);
    }

    @GetMapping("/messages")
    public ResponseEntity getAllMessages() {
        // use the message service to get the messages
        List<Message> list = messageService.getAllMessages();
        // 200 if successful
        return ResponseEntity.status(200).body(list);
    }

    @PostMapping("/messages")
    public ResponseEntity createMessage(@RequestBody Message message) {
        // use the message service to create the message
        Message createdMessage = messageService.createMessage(message);
        // any other failure is 400
        if (createdMessage == null) {
            return ResponseEntity.status(400).body("Bad request.");
        }
        // 200 if successful
        return ResponseEntity.status(200).body(createdMessage);
    }

    @PostMapping("/login")
    public ResponseEntity loginAccount(@RequestBody Account account) {
        // use account service class to check if this is a valid account
        Account loginAccount = accountService.login(account);
        if (loginAccount == null) {
            return ResponseEntity.status(401).body("Unauthorized.");
        }
        return ResponseEntity.status(200).body(loginAccount);
    }

    @PostMapping("/register")
    public ResponseEntity registerAccount(@RequestBody Account account) {
        // use the account service to register the account
        Account addedAccount = accountService.createAccount(account);
        // any other failure is 400
        if (addedAccount == null) {
            return ResponseEntity.status(400).body("Bad request.");
        }
        // duplicate username returns 409
        if (addedAccount.getAccountId() == -1) {
            return ResponseEntity.status(409).body("Duplicate username.");
        }
        // 200 if successful
        return ResponseEntity.status(200).body(addedAccount);
    }
}
