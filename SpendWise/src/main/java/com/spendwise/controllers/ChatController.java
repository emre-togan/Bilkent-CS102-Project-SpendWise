package com.spendwise.controllers;

import java.util.List;
import com.spendwise.services.ChatService;
import com.spendwise.models.Message;
import com.spendwise.models.Product;
import com.spendwise.models.User;

public class ChatController {

    public List<User> getFriends(int currentUserId) {
        return ChatService.getFriends(currentUserId);
    }

    public List<Message> getMessages(int currentUserId, String friendName) {
        return ChatService.getMessages(currentUserId, friendName);
    }

    public void sendMessage(int senderId, String receiverName, String content) {
        ChatService.sendMessage(senderId, receiverName, content);
    }

    public void sendProductRecommendation(int senderId, String receiverName, Product product) {
        ChatService.sendProductRecommendation(senderId, receiverName, product);
    }

    public boolean isUserOnline(String username) {
        return ChatService.isUserOnline(username);
    }

}
