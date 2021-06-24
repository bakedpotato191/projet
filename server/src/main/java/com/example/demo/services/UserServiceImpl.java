package com.example.demo.services;

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
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.exceptions.EntityNotFoundException;
import com.example.demo.exceptions.HttpUnauthorizedException;
import com.example.demo.mapstruct.dto.LoginDto;
import com.example.demo.mapstruct.dto.SignupDto;
import com.example.demo.mapstruct.mappers.MapStructMapper;
import com.example.demo.payload.response.JwtResponse;
import com.example.demo.persistence.models.PasswordResetToken;
import com.example.demo.persistence.models.User;
import com.example.demo.persistence.models.VerificationToken;
import com.example.demo.persistence.repository.PasswordResetTokenRepository;
import com.example.demo.persistence.repository.PostRepository;
import com.example.demo.persistence.repository.RoleRepository;
import com.example.demo.persistence.repository.UserRepository;
import com.example.demo.persistence.repository.VerificationTokenRepository;
import com.example.demo.security.jwt.JwtUtils;

@Service
@Transactional
public class UserServiceImpl implements UserService {

	@Autowired
	private AuthenticationManager authenticationManager;
	
    @Autowired
	private JwtUtils jwtUtils;
	
	@Autowired
    private UserRepository userRepository;
	
	@Autowired
	private PostRepository postRepository;
	
	@Autowired
    private RoleRepository roleRepository;

    @Autowired
    private SessionRegistry sessionRegistry;
	
	@Autowired
    private VerificationTokenRepository tokenRepository;

    @Autowired
    private PasswordResetTokenRepository passwordTokenRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private MapStructMapper mapstructMapper;

    public static final String TOKEN_INVALID = "invalidToken";
    public static final String TOKEN_EXPIRED = "expired";
    public static final String TOKEN_VALID = "valid";
	
	@Override
    public User registerNewUserAccount(final SignupDto signupDto) {
		
		var user = findUserByEmail(signupDto.getEmail());
		
        if (user.isPresent()) {
        	return null;
        }
        
		var newuser = new User();
		newuser.setNom(signupDto.getNom());
		newuser.setPrenom(signupDto.getPrenom());
		newuser.setPassword(passwordEncoder.encode(signupDto.getPassword()));
		newuser.setUsername(signupDto.getUsername());
		newuser.setEmail(signupDto.getEmail());
		newuser.setRoles(Arrays.asList(roleRepository.findByName("ROLE_USER")));
        return userRepository.save(newuser);
    }
	
	@Override
	public JwtResponse authenticateUser(LoginDto request) {

		var user = findUserByEmail(request.getEmail());
		
		if (user.isPresent()  && user.get().isEnabled()) {
			var authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
			SecurityContextHolder.getContext().setAuthentication(authentication);
			String jwt = jwtUtils.generateJwtToken(authentication);
			var userDetails = (UserDetailsImpl) authentication.getPrincipal();
			return new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(), userDetails.getAuthorities());	
		}
		else {
			throw new HttpUnauthorizedException("Invalid email or password");
		}	
	}

	@Override
	public User getUser(final String verificationToken) {
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

        final PasswordResetToken passwordToken = passwordTokenRepository.findByUser(user);

        if (passwordToken != null) {
            passwordTokenRepository.delete(passwordToken);
        }

        userRepository.delete(user);	
	}

	@Override
	public void createVerificationTokenForUser(final User user, final String token) {
		var myToken = new VerificationToken(token, user);
        tokenRepository.save(myToken);
		
	}

	@Override
	public VerificationToken getVerificationToken(final String verificationToken) {
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
		var myToken = new PasswordResetToken(token, user);
        passwordTokenRepository.save(myToken);	
	}

	@Override
	public Optional<User> findUserByEmail(final String email) {
		return userRepository.findByEmail(email);
	}

	@Override
	public PasswordResetToken getPasswordResetToken(final String token) {
		return passwordTokenRepository.findByToken(token);
	}

	@Override
	public Optional<User> getUserByPasswordResetToken(final String token) {
		return Optional.ofNullable(passwordTokenRepository.findByToken(token).getUser());
	}

	@Override
	public Optional<User> getUserByID(final long id) {
		return userRepository.findById(id);
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
	
	@Override
	public User getUserFromSession() {
		var user = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return (mapstructMapper.userImplToUser(user));
	}
	
	@Override
	public User getUserData(String username){
		
		var user = userRepository.findByUsername(username);
		
		if(user.isPresent())
		{	
			var my = user.get();
			my.setPostCount(postRepository.countByUtilisateur(user.get()));
			my.setPosts(user.get().getPosts());
			return my;	
        }
		else {
			throw new EntityNotFoundException(User.class, "username", username);
		}
	}
}
