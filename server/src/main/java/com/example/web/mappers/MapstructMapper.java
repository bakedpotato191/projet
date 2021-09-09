package com.example.web.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.example.rest.model.Comment;
import com.example.rest.model.Favori;
import com.example.rest.model.Follower;
import com.example.rest.model.Publication;
import com.example.rest.model.User;
import com.example.web.dto.response.CommentResDto;
import com.example.web.dto.response.FavoriDto;
import com.example.web.dto.response.FollowerDto;
import com.example.web.dto.response.FollowingDto;
import com.example.web.dto.response.PublicationDto;
import com.example.web.dto.response.UserDto;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MapstructMapper {
	
	/* Comments */
	CommentResDto commToCommDto(Comment comment);
	List<CommentResDto> commListMap(List<Comment> comments);
	
	/* Publication */
	PublicationDto pubToPubDto(Publication publication);
	List<PublicationDto> listPubToListPubDto(List<Publication> publications);
	
	/* Utilisateur */
	UserDto userToUserDto(User user);
	
	/* Favorites */
	List<FavoriDto> listFavToListFavDto(List<Favori> favorites);
	
	/* Followers */
	List<FollowingDto> followingToFollowingDto(List<Follower> followings);
	List<FollowerDto> followerToFollowerDto(List<Follower> followers);
	
	FollowingDto fTofDto(Follower following);
}
