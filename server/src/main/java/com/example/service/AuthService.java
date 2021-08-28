package com.example.service;

import java.util.List;
import java.util.Optional;

import com.example.jwt.response.JwtResponse;
import com.example.rest.model.PasswordResetToken;
import com.example.rest.model.User;
import com.example.rest.model.VerificationToken;
import com.example.web.dto.request.LoginDto;
import com.example.web.dto.request.NewPasswordDto;
import com.example.web.dto.request.SignupDto;

public interface AuthService {

	User registerUser(SignupDto signupDto);

	JwtResponse authenticateUser(LoginDto request);

	void saveRegisteredUser(User user);

	void deleteUser(User user);

	void createVerificationTokenForUser(User user, String token);

	VerificationToken findByVerificationToken(String verificationToken);

	VerificationToken generateNewVerificationToken(String existingVerificationToken);

	List<String> getUsersFromSessionRegistry();

	String validateVerificationToken(String token);

	boolean checkIfValidOldPassword(User user, String oldPassword);

	void changeUserPassword(User user, String password);

	Optional<User> findUserByEmail(String email);

	void invalidatePasswordResetTokensForUser(User user);

	Optional<PasswordResetToken> validatePasswordResetToken(String token);

	User getUserFromVerificationToken(String verificationToken);

	void updatePassword(NewPasswordDto dto);

	void createPasswordResetTokenForUser(User user, String token);

}
