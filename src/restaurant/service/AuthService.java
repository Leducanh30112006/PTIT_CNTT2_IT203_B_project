package restaurant.service;

import restaurant.model.User;

public interface AuthService {

    User login(String username, String rawPassword);

    boolean register(String username, String rawPassword, String fullName);
}