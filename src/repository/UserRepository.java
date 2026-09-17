package repository;

import java.util.ArrayList;
import model.User;

public class UserRepository {

    private ArrayList<User> users = new ArrayList<>();

    public void addUser(User user) {
        users.add(user);
    }

    public ArrayList<User> getAllUsers() {
        return users;
    }

    public User findUserById(String id) {
        for (User user : users) {
            if (user.getId().equals(id)) {
                return user;
            }
        }
        return null;
    }
}