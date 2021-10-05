package com.example.rest.dao;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.rest.model.Follower;
import com.example.rest.model.User;

@Repository
@Transactional(readOnly = true)
public interface FollowerRepository extends JpaRepository<Follower, Long> {

	@Modifying
	@Transactional
	@Query(value = "DELETE f.* FROM  follower AS f CROSS JOIN utilisateur AS u ON f.to_id = u.id WHERE u.username=:username AND f.from_id=:user", nativeQuery = true)
	int unfollow(@Param("user") User user, @Param("username") String username);
	
	@Query("SELECT COUNT(f)>0 from Follower f WHERE f.from = :user1 AND f.to = :user2")
	boolean isFollowed(@Param ("user1") User currentUser, @Param("user2") User browsedUser);
	
	@Query("SELECT COUNT(f)>0 from Follower f WHERE f.from = :user1 AND f.to = :user2")
	boolean isFollowing(@Param ("user1") User browsedUser, @Param("user2") User currentUser);
	
	@Query(value = "SELECT COUNT(to_id) FROM Follower f CROSS JOIN utilisateur u on f.to_id = u.id WHERE u.username= :username", nativeQuery=true)
	Long countFollowers(@Param ("username") String username);

	@Query(value = "SELECT COUNT(from_id) FROM Follower f CROSS JOIN utilisateur u on f.from_id = u.id WHERE u.username= :username", nativeQuery=true)
	Long countFollowing(@Param ("username") String username);
	
	Slice<Follower> findAllToByFromUsername(String username, Pageable paging);

	Slice<Follower> findAllFromByToUsername(String username, Pageable paging);
	
	@Async
	@Override
	@Transactional
    public <S extends Follower> S save(S entity);
}
