package com.example.emtechelppathbackend.like;

import com.example.emtechelppathbackend.comment.CommentEntity;
import com.example.emtechelppathbackend.feed.feedv2.FeedsRepositoryv2;
import com.example.emtechelppathbackend.feed.feedv2.FeedsV2;
import com.example.emtechelppathbackend.security.user.Users;
import com.example.emtechelppathbackend.security.user.UsersRepository;
import com.example.emtechelppathbackend.utils.CustomResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements  LikeService{

    @Autowired
    private final LikeRepository likeRepository;
    @Autowired
    private  final UsersRepository usersRepository;
    private final FeedsRepositoryv2 feedsRepository;

    @Override
    public CustomResponse<?> saveLike(Long userId, Long feedId, LikeEntity like) {
        CustomResponse<LikeEntity> response = new CustomResponse<>();
        try {
            // Retrieve user by userId
            Users user = usersRepository.findById(userId).orElse(null);

            // Check if user exists
            if (user == null) {
                response.setMessage("User with id " + userId + " not found");
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setSuccess(false);
                response.setPayload(null);
                return response;
            }

            // Retrieve feed by feedId
            FeedsV2 feed = feedsRepository.findById(feedId).orElse(null);

            // Check if feed exists
            if (feed == null) {
                response.setMessage("Feed with id " + feedId + " not found");
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setSuccess(false);
                response.setPayload(null);
                return response;
            }

            // Set user and feed for the like entity
            like.setUser(user);
            like.setFeed(feed);
            like.setId(like.getId());

            // Save the like entity
            LikeEntity savedLike = likeRepository.save(like);

            response.setMessage("Like added successfully");
            response.setStatusCode(HttpStatus.CREATED.value());
            response.setSuccess(true);
            response.setPayload(savedLike);
        } catch (Exception e) {
            response.setMessage("Failed to add like: " + e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setSuccess(false);
            response.setPayload(null);
        }
        return response;
    }



    @Override
    public CustomResponse<?> deleteLike(Long id) {
        CustomResponse<LikeEntity> response = new CustomResponse<>();
        try {
            // Check if the like exists
            LikeEntity existingLike = likeRepository.findById(id).orElse(null);

            if (existingLike == null) {
                response.setMessage("Like with id " + id + " not found");
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setSuccess(false);
                return response;
            }

            // Perform any necessary validation or business logic before deleting the like
            // For example, check if the current user is allowed to unlike this post

            likeRepository.delete(existingLike);

            response.setMessage("Like deleted successfully");
            response.setStatusCode(HttpStatus.OK.value());
            response.setSuccess(true);
        } catch (Exception e) {
            response.setMessage("Failed to delete like: " + e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setSuccess(false);
        }
        return response;
    }


    @Override
    public CustomResponse<?> getTotalLikes(Long feedId) {
        CustomResponse<LikeRepository.LikeCount> response = new CustomResponse<>();
        try {
            if (feedId == null) {
                throw new IllegalArgumentException("Feed ID must be provided");
            }

            // Retrieve the total number of likes for the specified feedId
            LikeRepository.LikeCount totalLikes = likeRepository.countByFeedId(feedId);

            response.setMessage("Total number of likes retrieved successfully");
            response.setStatusCode(HttpStatus.OK.value());
            response.setSuccess(true);
            response.setPayload(totalLikes);
        } catch (Exception e) {
            response.setMessage("Failed to retrieve total number of likes: " + e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setSuccess(false);
            response.setPayload(null);
        }
        return response;
    }

    @Override

    public CustomResponse<?> getAllLikes(Long feedId) {
        CustomResponse<List<LikeRepository.LikeInterface>> response = new CustomResponse<>();
        try {
            if (feedId == null) {
                throw new IllegalArgumentException("Feed ID must be provided");
            }

            List<LikeRepository.LikeInterface> likes = likeRepository.findLikesByFeedId(feedId);

            if (likes.isEmpty()) {
                response.setMessage("No likes found for the specified feed ID");
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setSuccess(false);
                response.setPayload(null);
            } else {
                response.setMessage("Likes retrieved successfully");
                response.setStatusCode(HttpStatus.OK.value());
                response.setSuccess(true);
                response.setPayload(likes);
            }
        } catch (Exception e) {
            response.setMessage("Failed to retrieve likes: " + e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setSuccess(false);
            response.setPayload(null);
        }
        return response;
    }


}
