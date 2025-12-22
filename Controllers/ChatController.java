package Controllers;

import java.util.List;
import Services.ChatService;
import Models.Message;
import Models.Product;
import Models.RegularUser;

public class ChatController {

    public List<RegularUser> getFriends(int currentUserId){
        return ChatService.getFriends(currentUserId);
    }

    public List<Message> getMessages(int currentUserId, String friendName) {
        return ChatService.getMessages(currentUserId, friendName);
    }

    public void sendMessage(int senderId, String receiverName, String content) {
        ChatService.sendMessage(senderId, receiverName, content);
    }

    public void sendProductRecommendation(int senderId, String receiverName, Product product) {
        ChatService.sendProductRecommend(senderId, receiverName, product);
    }

    public boolean isUserOnline(String username) {
        return ChatService.isUserOnline(username);
    }

}

