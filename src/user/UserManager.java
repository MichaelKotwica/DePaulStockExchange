package user;

import exceptions.DataValidationException;
import exceptions.InvalidParameterException;
import tradable.TradableDTO;

import java.util.TreeMap;

public class UserManager {

    private static final UserManager instance = new UserManager();

    // UserId -> User
    private TreeMap<String, User> users = new TreeMap<>();

    private UserManager() {
        // private constructor
    }

    public static UserManager getInstance() {
        return instance;
    }

    public void init(String[] usersIn) throws InvalidParameterException {
        if(usersIn == null) {
            throw new DataValidationException("usersIn array is null");
        }
        for(int i = 0; i < usersIn.length; i++) {
            if(usersIn[i] == null) {
                throw new DataValidationException("Element " + i + " in usersIn array is null");
            }
            users.put(usersIn[i], new User(usersIn[i]));
        }
    }

    public void updateTradable(String userId, TradableDTO o) {
        if(userId == null) {
            throw new DataValidationException("userId is null");
        }
        if (o == null) {
            throw new DataValidationException("TradableDTO is null");
        }
        if (!users.containsKey(userId)) {
            throw new DataValidationException("User " + userId + " does not exist in the treeMap");
        }

        users.get(userId).updateTradable(o);
    }

    public User getUser(String userId) throws InvalidParameterException {
        if(!users.containsKey(userId)) {
            throw new InvalidParameterException("UserManager does not contain the user: " + userId);
        }
        return users.get(userId);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(User user : users.values()) {
            sb.append(user.toString()).append("\n\n");
        }
        return sb.toString();
    }
}
