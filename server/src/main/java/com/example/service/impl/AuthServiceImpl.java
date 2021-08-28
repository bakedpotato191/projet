package com.example.service.impl;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.jwt.JwtUtils;
import com.example.jwt.response.JwtResponse;
import com.example.rest.dao.PasswordResetTokenRepository;
import com.example.rest.dao.RoleRepository;
import com.example.rest.dao.UserRepository;
import com.example.rest.dao.VerificationTokenRepository;
import com.example.rest.model.PasswordResetToken;
import com.example.rest.model.User;
import com.example.rest.model.VerificationToken;
import com.example.service.AuthService;
import com.example.web.dto.request.LoginDto;
import com.example.web.dto.request.NewPasswordDto;
import com.example.web.dto.request.SignupDto;
import com.example.web.exception.EmailAlreadyExistsException;
import com.example.web.exception.HttpUnauthorizedException;
import com.example.web.exception.UsernameAlreadyExistsException;

@Service
public class AuthServiceImpl implements AuthService {
	
	private static final String TOKEN_INVALID = "invalidToken";
    private static final String TOKEN_EXPIRED = "expired";
    private static final String TOKEN_VALID = "valid";
    
	@Autowired
    private UserRepository userRepository;
	
	@Autowired
    private RoleRepository roleRepository;
	
	@Autowired
    private VerificationTokenRepository tokenRepository;
	
    @Autowired
    private PasswordResetTokenRepository passwordResetRepository;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
	private JwtUtils jwtUtils;
    
    @Autowired
    private SessionRegistry sessionRegistry;
	
	@Override
    public User registerUser(final SignupDto signupDto) {
		
        if (userRepository.findByUsername(signupDto.getUsername()).isPresent()) {
        	throw new UsernameAlreadyExistsException("Username is already in use.");
        }
        else if(userRepository.findByEmail(signupDto.getEmail()).isPresent()) {
        	throw new EmailAlreadyExistsException("Email is already in use.");
        }
        
		var user = new User();
		user.setNom(signupDto.getNom());
		user.setPrenom(signupDto.getPrenom());
		user.setPassword(passwordEncoder.encode(signupDto.getPassword()));
		user.setUsername(signupDto.getUsername());
		user.setEmail(signupDto.getEmail());
		user.setRoles(Arrays.asList(roleRepository.findByName("ROLE_USER")));
        return userRepository.save(user);
    }
	
	@Override
	public JwtResponse authenticateUser(LoginDto request) {

		var user = findUserByEmail(request.getEmail());
		
		if (user.isPresent()  && user.get().isEnabled()) {
			var authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
			SecurityContextHolder.getContext().setAuthentication(authentication);
			String jwt = jwtUtils.generateJwtToken(authentication);
			var userDetails = (User) authentication.getPrincipal();
			return new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(), userDetails.getAuthorities());	
		}
		else {
			throw new HttpUnauthorizedException("Invalid email or password");
		}	
	}
	
	@Override
	public User getUserFromVerificationToken(final String verificationToken) {
		final VerificationToken token = tokenRepository.findByToken(verificationToken);
        if (token != null) {
            return token.getUser();
        }
        return null;
	}
	
	@Override
	public void saveRegisteredUser(final User user) {
		userRepository.save(user);
	}

	@Override
	public void deleteUser(User user) {
		var verificationToken = tokenRepository.findByUser(user);

        if (verificationToken != null) {
            tokenRepository.delete(verificationToken);
        }

        final PasswordResetToken passwordToken = passwordResetRepository.findByUser(user);

        if (passwordToken != null) {
        	passwordResetRepository.delete(passwordToken);
        }

        userRepository.delete(user);
	}

	@Override
	public void createVerificationTokenForUser(final User user, final String token) {
		var myToken = new VerificationToken(token, user);
        tokenRepository.save(myToken);
		
	}

	@Override
	public VerificationToken findByVerificationToken(final String verificationToken) {
		return tokenRepository.findByToken(verificationToken);
	}

	@Override
	public VerificationToken generateNewVerificationToken(final String existingVerificationToken) {
		VerificationToken vToken = tokenRepository.findByToken(existingVerificationToken);
        vToken.updateToken(UUID.randomUUID()
            .toString());
        vToken = tokenRepository.save(vToken);
        return vToken;
	}

	@Override
	public void createPasswordResetTokenForUser(final User user, final String token) {
		
		invalidatePasswordResetTokensForUser(user);
	
		var myToken = new PasswordResetToken(token, user);
		passwordResetRepository.save(myToken);
	}
	
	@Override
	public void invalidatePasswordResetTokensForUser(final User user) {
		passwordResetRepository.deleteAllByUser(user);
	}

	@Override
	public Optional<User> findUserByEmail(final String email) {
		return userRepository.findByEmail(email);
	}

	@Override
	public void changeUserPassword(User user, String password) {
		user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
	}

	@Override
	public boolean checkIfValidOldPassword(final User user, String oldPassword) {
		return passwordEncoder.matches(oldPassword, user.getPassword());
	}

	@Override
	public String validateVerificationToken(String token) {
		var verificationToken = tokenRepository.findByToken(token);
        if (verificationToken == null) {
            return TOKEN_INVALID;
        }

        var user = verificationToken.getUser();
        var cal = Calendar.getInstance();
        if ((verificationToken.getExpiryDate()
            .getTime() - cal.getTime()
            .getTime()) <= 0) {
            tokenRepository.delete(verificationToken);
            return TOKEN_EXPIRED;
        }

        user.setEnabled(true);
        userRepository.save(user);
        return TOKEN_VALID;
	}
	
	@Override
	public Optional<PasswordResetToken> validatePasswordResetToken(String token) {
		var verificationToken = passwordResetRepository.findByToken(token);
        
		if (verificationToken.isEmpty()) {
            throw new HttpUnauthorizedException("token is invalid");
        }

        var tk = verificationToken.get();
        var cal = Calendar.getInstance();
        if ((tk.getExpiryDate()
            .getTime() - cal.getTime()
            .getTime()) <= 0) {
            passwordResetRepository.delete(tk);
            throw new HttpUnauthorizedException("token is expired");
        }
        return verificationToken;
	}
	
	@Override
	public void updatePassword(NewPasswordDto dto) {
		var token = validatePasswordResetToken(dto.getToken()).get();
	
		var user = token.getUser();
		user.setPassword(passwordEncoder.encode(dto.getPasswordRepeat()));
		userRepository.save(user);
		passwordResetRepository.delete(token);	
	}

	@Override
	public List<String> getUsersFromSessionRegistry() {
		return sessionRegistry.getAllPrincipals()
	            .stream()
	            .filter(u -> !sessionRegistry.getAllSessions(u, false)
	                .isEmpty())
	            .map(o -> {
	                if (o instanceof User) {
	                    return ((User) o).getEmail();
	                } else {
	                    return o.toString()
	            ;
	                }
	            }).collect(Collectors.toList());
	}

}
