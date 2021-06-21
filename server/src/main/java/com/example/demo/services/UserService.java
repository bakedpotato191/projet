package com.example.demo.services;

import java.util.List;
import java.util.Optional;

import com.example.demo.mapstruct.dto.LoginDto;
import com.example.demo.mapstruct.dto.SignupDto;
import com.example.demo.payload.response.JwtResponse;
import com.example.demo.persistence.models.PasswordResetToken;
import com.example.demo.persistence.models.User;
import com.example.demo.persistence.models.VerificationToken;

public interface UserService {

    User registerNewUserAccount(SignupDto user);

    User getUser(String verificationToken);

    void saveRegisteredUser(User user);

    void deleteUser(User user);

    void createVerificationTokenForUser(User user, String token);

    VerificationToken getVerificationToken(String verificationToken);

    VerificationToken generateNewVerificationToken(String token);

    void createPasswordResetTokenForUser(User user, String token);

    User findUserByEmail(String email);

    PasswordResetToken getPasswordResetToken(String token);

    Optional<User> getUserByPasswordResetToken(String token);

    Optional<User> getUserByID(long id);

    void changeUserPassword(User user, String password);

    boolean checkIfValidOldPassword(User user, String password);

    String validateVerificationToken(String token);

    List<String> getUsersFromSessionRegistry();

	User getUserData(String username);

	JwtResponse authenticateUser(LoginDto request);
	
	boolean isUserEnabled(String email);

}
