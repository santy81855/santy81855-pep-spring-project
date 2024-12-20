package com.example.service;
import org.springframework.stereotype.Service;

import com.example.entity.Message;
import com.example.entity.Account;
import com.example.repository.MessageRepository;
import com.example.repository.AccountRepository;
import java.util.List;

@Service
public class MessageService {
    private final MessageRepository messageRepository;
    private final AccountRepository accountRepository;

    // constructor injection to inject the AccountRepository class into the AccountService class
    public MessageService(MessageRepository messageRepository, AccountRepository accountRepository) {
        this.messageRepository = messageRepository;
        this.accountRepository = accountRepository;
    }

    public List<Message> getAllMessagesByAccountId(Integer accountId) {
        return messageRepository.getMessagesByPostedBy(accountId);
    }

    public Boolean updateMessageById(Integer id, String newText) {
        if (newText == null || newText.isEmpty() || newText.length() > 255) {
            return false;
        }
        // retrieve the message by id
        Message message = getMessageById(id);
        if (message == null) {
            return false;
        }
        // update the message
        message.setMessageText(newText);
        // save the updated message
        Message updatedMessage = messageRepository.save(message);
        if (updatedMessage == null) {
            return false;
        }
        return true;
    }

    public Boolean deleteMessageById(Integer id) {
        // check if the message exists
        Message message = getMessageById(id);
        if (message == null) {
            return false;
        }
        messageRepository.deleteById(id);
        return true;
    }

    public Message getMessageById(Integer id) {
        return messageRepository.findById(id).orElse(null);
    }

    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    public Message createMessage(Message message) {
        // determine if the message meets the requirements
        if (message.getMessageText().length() == 0 || message.getMessageText().length() > 255) {
            return null;
        }
        // check that the PostedBy is a valid Account
        Account postedByAccount = accountRepository.findById(message.getPostedBy()).orElse(null);
        if (postedByAccount == null) {
            return null;
        }
        return messageRepository.save(message);
    }
}
