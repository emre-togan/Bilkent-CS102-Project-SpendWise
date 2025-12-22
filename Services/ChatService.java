package Services;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import Database.DBconnection;
import Models.Message;
import Models.Product;
import  Models.User;

public class ChatService {
    

    public static List<User> getFriends(int currentUserId){

        List<User> friends = new ArrayList<>();

        String sql = "SELECT * FROM users WHERE user_id != ?";

        try{
            ResultSet resultSet = DBconnection.executeQuery(sql, currentUserId);

            while(resultSet != null && resultSet.next()){
                User friend = new User(resultSet.getString("username"),
                    "***",
                    resultSet.getString("email"),
                    resultSet.getInt("user_id"),
                    resultSet.getTimestamp("registration_date")
                );

                    friends.add(friend);
            }
        }

        catch(SQLException e){
            e.printStackTrace();
        }
        return friends;
    }

    public static boolean isUserOnline(String username){

        String sql = "SELECT last_active_time FROM users WHERE username = ?";
        ResultSet resultSet = Database.DBconnection.executeQuery(sql, username);

        try{

            if(resultSet != null && resultSet.next()){

                boolean isOnline = false;

                Timestamp lastActive = resultSet.getTimestamp("last_active_time");

                if(lastActive == null){
                    return false;
                }

                long currentTime = System.currentTimeMillis();
                long lastSeen = lastActive.getTime();
                long timeDifference = currentTime - lastSeen;

                if(timeDifference < 600000){
                    isOnline = true;
                }
                
                return isOnline;
            }
        }

        catch( SQLException e){
            e.printStackTrace();
        }

        return false;
    } 

    public static List<Message> getMessages(int currentUserId, String friendName){

        List<Message> messages = new ArrayList<>();
        int friendId = getUserIdByName(friendName);

        if(friendId == -1){
            return messages;
        }
        String sql = "SELECT * FROM messages WHERE (sender_id = ? AND receiver_id = ?) OR (sender_id = ? AND receiver_id = ?) ORDER BY timestamp ASC";

        try{
        ResultSet resultSet = DBconnection.executeQuery(sql, currentUserId, friendId, friendId, currentUserId);

            while( resultSet!= null && resultSet.next()){

                int msgId = resultSet.getInt("message_id");
                int sId = resultSet.getInt("sender_id");
                int rId = resultSet.getInt("receiver_id");
                String content = resultSet.getString("content");
                Timestamp time = resultSet.getTimestamp("timestamp");
                boolean isRead = resultSet.getBoolean("is_read");

                int productId = resultSet.getInt("shared_product_id");
                Integer sharedProductId = resultSet.wasNull() ? null : productId;

                Message message = new Message(msgId, sId, rId, content, time, isRead, sharedProductId);
                messages.add(message);
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }

        return messages;
    }

    public static void sendMessage(int senderId, String receiverName, String content){

        int receiverId = getUserIdByName(receiverName);

        if (receiverId == -1){
            return;
        } 

        String sql = "INSERT INTO messages (sender_id, receiver_id, content, timestamp, is_read, shared_product_id) VALUES (?, ?, ?, ?, ?, ?)";
        DBconnection.executeUpdate(sql, senderId, receiverId, content, new Timestamp(System.currentTimeMillis()), false, null);
    }

    public static void sendProductRecommend(int senderId, String receiverName, Product product){

        int receiverId = getUserIdByName(receiverName);

        if (receiverId == -1){
            return;
        } 

        String content = "Check out: " + product.getName() + " - $" + product.getPriceAfterDiscount();

        String sql = "INSERT INTO messages (sender_id, receiver_id, content, timestamp, is_read, shared_product_id) VALUES (?, ?, ?, ?, ?, ?)";
        DBconnection.executeUpdate(sql, senderId, receiverId, content, new Timestamp(System.currentTimeMillis()), false, null);
    }

    private static int getUserIdByName(String userName){

        String sql = "SELECT user_id FROM users WHERE username = ?";

        try{
            ResultSet resultSet = DBconnection.executeQuery(sql, userName);
                if(resultSet != null && resultSet.next()){
                    return resultSet.getInt("user_id");
                }
            }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
}
