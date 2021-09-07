package com.example.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.rest.dao.FollowerRepository;
import com.example.rest.dao.PublicationRepository;
import com.example.rest.dao.UserRepository;
import com.example.rest.model.Follower;
import com.example.rest.model.User;
import com.example.service.UserService;
import com.example.web.dto.response.AvatarResponse;
import com.example.web.dto.response.UserDto;
import com.example.web.exception.EntityNotFoundException;
import com.example.web.mappers.MapstructMapper;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private static final String USERNAME = "username";
	
	@Autowired
    private UserRepository userRepository;
	
	@Autowired
	private PublicationRepository publicationRepository;

    @Autowired
    private FollowerRepository followerRepository;
    
    @Autowired
    private MapstructMapper mapper;

	@Override
	public UserDto getUserData(String username){
		
		var user = userRepository.findByUsername(username);
		
		if(user.isPresent())
		{	
			var result = user.get();
			result.setPostCount(publicationRepository.countByUtilisateur(user.get()));
			
			if (SecurityContextHolder.getContext().getAuthentication() 
			          instanceof AnonymousAuthenticationToken) {
				result.setFollowed(false);
			}
			else {
				result.setFollowed(followerRepository.isFollowed(getUserFromSession(), result));
			}
			
			result.setFollowerCount(followerRepository.countFollowers(username));
			result.setFollowingCount(followerRepository.countFollowing(username));
			return mapper.userToUserDto(result);
        }
		else {
			throw new EntityNotFoundException(User.class, USERNAME, username);
		}
	}
	
	@Override
	public void follow(String username) {
		
		var user = userRepository.findByUsername(username);
		
		if (user.isPresent()) {
			var currentUser = getUserFromSession();
			var following = user.get();
			followerRepository.save(new Follower(currentUser, following));
		}
		else {
			throw new EntityNotFoundException(User.class, USERNAME, username);
		}
	}
	
	@Override
	public void unfollow(String username) {
		
		int row = followerRepository.unfollow(getUserFromSession(), username);
		
		if (row == 0) {
			throw new EntityNotFoundException(User.class, USERNAME, username);
		}
	}
	
	@Override
	public AvatarResponse setProfilePicture(String path) {
		path = "http://localhost:8081/api/user/profile_picture/" + path;
		userRepository.setProfilePicture(getUserFromSession(), path);
		return new AvatarResponse(true, path, HttpStatus.OK);
	}
	
	@Override
	public AvatarResponse getProfilePicture() {
		var records = userRepository.getProfilePicture(getUserFromSession());
	    Object[] userDetails = records.get(0);
	    var avatar = String.valueOf(userDetails[0]);
	    var has_avatar = Boolean.valueOf(String.valueOf(userDetails[1]));
		return new AvatarResponse(has_avatar, avatar, HttpStatus.OK);
	}
	

	@Override
	public AvatarResponse resetProfilePicture() {
		userRepository.resetProfilePicture(getUserFromSession());
		return getProfilePicture();
	}
	
	@Override
	public List<Follower> getSubscriptions(String username, int pageNo, int pageSize) {
		Pageable paging = PageRequest.of(pageNo, pageSize);
		Slice<Follower> slicedResult = followerRepository.findAllUtilisateur2ByUtilisateur1Username(username, paging);
		
		if (slicedResult.hasContent()) {
			slicedResult.forEach(entity -> 
			entity.getUtilisateur2().setFollowed(followerRepository.isFollowed(getUserFromSession(), entity.getUtilisateur2())));
				
			return slicedResult.getContent();
		}
		else {
			return new ArrayList<>();
		}
	}
	
	@Override
	public User getUserFromSession() {
		return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}
}
