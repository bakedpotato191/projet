package com.example.demo.rest.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.rest.exceptions.EntityNotFoundException;
import com.example.demo.rest.models.Follower;
import com.example.demo.rest.models.User;
import com.example.demo.rest.repository.FollowerRepository;
import com.example.demo.rest.repository.PostRepository;
import com.example.demo.rest.repository.UserRepository;
import com.example.demo.rest.services.UserService;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private static final String USERNAME = "username";
	
	@Autowired
    private UserRepository userRepository;
	
	@Autowired
	private PostRepository postRepository;

    @Autowired
    private FollowerRepository followerRepository;

	@Override
	public User getUserData(String username){
		
		var user = userRepository.findByUsername(username);
		
		if(user.isPresent())
		{	
			var result = user.get();
			result.setPostCount(postRepository.countByUtilisateur(user.get()));
			
			if (SecurityContextHolder.getContext().getAuthentication() 
			          instanceof AnonymousAuthenticationToken) {
				result.setFollowed(false);
			}
			else {
				result.setFollowed(followerRepository.isFollowed(getUserFromSession(), result));
			}
			
			result.setFollowerCount(followerRepository.countFollowers(username));
			result.setFollowingCount(followerRepository.countFollowing(username));
			return result;
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
	public String setProfilePicture(String path) {
		path = "http://localhost:8081/api/user/profile_picture/" + path;
		userRepository.setProfilePicture(getUserFromSession(), path);
		return path;
	}
	
	@Override
	public String getProfilePicture() {
		return userRepository.getProfilePicture(getUserFromSession());	 
	}
	

	@Override
	public void resetProfilePicture() {
		userRepository.resetProfilePicture(getUserFromSession());
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
