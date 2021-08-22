package com.example.demo.rest.services;

import java.util.List;
import java.util.Optional;

import com.example.demo.jwt.response.JwtResponse;
import com.example.demo.rest.dto.LoginDto;
import com.example.demo.rest.dto.NewPasswordDto;
import com.example.demo.rest.dto.SignupDto;
import com.example.demo.rest.models.PasswordResetToken;
import com.example.demo.rest.models.User;
import com.example.demo.rest.models.VerificationToken;

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
