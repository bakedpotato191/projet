package com.example.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.rest.dao.UserRepository;

@Service
@Transactional
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
	public UserDetails loadUserByUsername(final String email) throws UsernameNotFoundException {
		var user = userRepository.findByEmail(email);
	
		if (user.isPresent()) {
			return user.get();
        }
		else {
			throw new UsernameNotFoundException("No user found with email: " + email);
		}       
	}
}
