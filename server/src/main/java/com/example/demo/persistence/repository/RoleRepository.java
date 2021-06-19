package com.example.demo.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.persistence.models.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
	
	  Role findByName(String name);

	  @Override
	  void delete(Role role);
}
