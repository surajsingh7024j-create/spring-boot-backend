package com.chat.ChatKarlore.Controller;

import com.chat.ChatKarlore.Entity.Message;
import com.chat.ChatKarlore.Repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatHistoryController {
    private final MessageRepository messageRepository;
    @GetMapping("/{user1}/{user2}")
    public List<Message> history(@PathVariable String user1, @PathVariable String user2) {
        List<Message> messages1 = messageRepository.findBySenderAndReceiver(user1, user2);
        List<Message> messages2 = messageRepository.findBySenderAndReceiver(user2, user1);
        messages1.addAll(messages2);
        messages1.sort(Comparator.comparing(Message::getSentAt));
        return messages1;
    }
}

