package com.example.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.rest.dao.FollowerRepository;
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
    private FollowerRepository followerRepository;
    
    @Autowired
    private MapstructMapper mapper;
    
	@Override
	@Async
	public CompletableFuture<UserDto> getUserData(String username){
		
		var optional = userRepository.findByUsername(username);
		
		if(optional.isPresent())
		{	
			var user = optional.get();
			var result = mapper.userToUserDto(user);
			result.setPostCount(user.getPosts().size());
			result.setFollowed(isAnonymous() ? false : followerRepository.isFollowed(getAuthenticatedUser(), user));
			result.setFollowerCount(user.getFollowers().size());
			result.setFollowingCount(user.getFollowing().size());
			return CompletableFuture.completedFuture(result);
        }
		else {
			throw new EntityNotFoundException(User.class, USERNAME, username);
		}
	}
	

	@Override
	@Async
	public void follow(String username) {
		
		var user = userRepository.findByUsername(username);
		
		if (user.isPresent()) {
			followerRepository.save(new Follower(getAuthenticatedUser(), user.get()));
		}
	}
	
	@Override
	@Async
	public void unfollow(String username) {
		
		int row = followerRepository.unfollow(getAuthenticatedUser(), username);
		
		if (row == 0) {
			throw new EntityNotFoundException(User.class, USERNAME, username);
		}
	}
	

	@Override
	@Async
	public CompletableFuture<AvatarResponse> setProfilePicture(String path) {
		path = "http://localhost:8081/api/user/profile_picture/" + path;
		userRepository.setProfilePicture(getAuthenticatedUser(), path);
		return CompletableFuture.completedFuture(new AvatarResponse(true, path, HttpStatus.OK));
	}
	

	@Override
	public AvatarResponse getProfilePicture() {
		var records = userRepository.getProfilePicture(getAuthenticatedUser());
	    Object[] userDetails = records.get(0);
	    var avatar = String.valueOf(userDetails[0]);
	    var has_avatar = Boolean.valueOf(String.valueOf(userDetails[1]));
		return new AvatarResponse(has_avatar, avatar, HttpStatus.OK);
	}
	

	@Override
	public AvatarResponse resetProfilePicture() {
		userRepository.resetProfilePicture(getAuthenticatedUser());
		return getProfilePicture();
	}


	@Override
	@Async
	public CompletableFuture<List<UserDto>> getSubscriptions(String username, int pageNo, int pageSize) throws InterruptedException {
		Pageable paging = PageRequest.of(pageNo, pageSize);
		var slicedResult = followerRepository.findAllToByFromUsername(username, paging);
		List<UserDto> list = new ArrayList<>();
		
		if (slicedResult.hasContent()) {
			Iterator<Follower> it = slicedResult.iterator();
			
			while(it.hasNext()) {
				Follower f = it.next();
				var result = mapper.userToUserDto(f.getTo());
				result.setFollowed(isAnonymous() ? false : followerRepository.isFollowed(getAuthenticatedUser(), f.getTo()));

				list.add(result);
			}
		}
		return CompletableFuture.completedFuture(list);
	}
	
	@Override
	@Async
	public CompletableFuture<List<UserDto>> getSubscribers(String username, int pageNo, int pageSize) {
		Pageable paging = PageRequest.of(pageNo, pageSize);
		Slice<Follower> slicedResult = followerRepository.findAllFromByToUsername(username, paging);
		List<UserDto> list = new ArrayList<>();
		if (slicedResult.hasContent()) {
			
			Iterator<Follower> it = slicedResult.iterator();
			
			while(it.hasNext()) {
				Follower f = it.next();
				var result = mapper.userToUserDto(f.getFrom());
				result.setFollowed(isAnonymous() ? false : followerRepository.isFollowed(getAuthenticatedUser(), f.getFrom()));

				list.add(result);
			}
		}
		return CompletableFuture.completedFuture(list);
	}
	
	@Override
	public User getAuthenticatedUser() {
		return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}
	
	@Override
	public boolean isAnonymous() {
		return SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken;
	}	
}
