package com.example.emtechelppathbackend.comment;

import com.example.emtechelppathbackend.feed.feedv2.FeedsRepositoryv2;
import com.example.emtechelppathbackend.feed.feedv2.FeedsV2;
import com.example.emtechelppathbackend.feedback.Feedback;
import com.example.emtechelppathbackend.utils.HostNameCapture;
import com.example.emtechelppathbackend.utils.ServerPortService;
import jakarta.persistence.Tuple;
import org.springframework.stereotype.Service;
import com.example.emtechelppathbackend.security.user.Users;
import com.example.emtechelppathbackend.security.user.UsersRepository;
import com.example.emtechelppathbackend.utils.CustomResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;


@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService{
    @Autowired
    private final CommentRepository commentRepository;
    @Autowired
    private  final UsersRepository userRepository;
    private final FeedsRepositoryv2 feedsRepository;

    HostNameCapture hostNameCapture=new HostNameCapture();
    private final Path path = Paths.get(System.getProperty("user.dir")+"/images/");
    String imagesPath;
    @Autowired
    ServerPortService serverPortService;

    @Override
    public CustomResponse<?> addComment(Long userId, Long feedId, CommentDto commentDto) {
        CustomResponse<CommentEntity> response = new CustomResponse<>();

        try {
            Users user = userRepository.findById(userId).orElse(null);
            FeedsV2 feed = feedsRepository.findById(feedId).orElse(null);

            if (user == null) {
                response.setMessage("User with id " + userId + " not found");
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setSuccess(false);
                response.setPayload(null);
                return response;
            }

            if (feed == null) {
                response.setMessage("Feed with id " + feedId + " not found");
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setSuccess(false);
                response.setPayload(null);
                return response;
            }


            CommentEntity commentEntity = mapCommentDtoToEntity( commentDto, user, feed);

            commentRepository.save(commentEntity);

            response.setMessage("Comment added successfully");
            response.setStatusCode(HttpStatus.CREATED.value());
            response.setSuccess(true);
            response.setPayload(commentEntity);

        } catch (Exception e) {
            response.setMessage("Failed to add comment: " + e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setSuccess(false);
            response.setPayload(null);
        }

        return response;
    }




    private CommentEntity mapCommentDtoToEntity( CommentDto commentDto, Users user, FeedsV2 feed) {
        CommentEntity commentEntity = new CommentEntity();

        commentEntity.setUser(user);
        commentEntity.setFeed(feed);
        commentEntity.setMessage(commentDto.getMessage());


        return commentEntity;
    }




    @Override
    public CustomResponse<?> getTotalComments(Long feedId) {
        CustomResponse<CommentRepository.CommentCount> response = new CustomResponse<>();
        try {
            if (feedId == null) {
                throw new IllegalArgumentException("Feed ID must be provided");
            }


             var count = commentRepository.countCommentsByFeedId(feedId);


            response.setMessage("Total number of comments retrieved successfully");
            response.setStatusCode(HttpStatus.OK.value());
            response.setSuccess(true);
            response.setPayload(count);
        } catch (Exception e) {
            response.setMessage("Failed to retrieve total number of comments: " + e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setSuccess(false);
            response.setPayload(null);
        }
        return response;
    }

    @Override
    public CustomResponse<?> getAllComments(Long feedId) {
        CustomResponse<List<CommentEntity>> response = new CustomResponse<>();
        try {
            if (feedId == null) {
                throw new IllegalArgumentException("Feed ID must be provided");
            }

            List<CommentEntity> comments = commentRepository.findCommentsByFeedId(feedId);

            response.setMessage("Comments retrieved successfully");
            response.setStatusCode(HttpStatus.OK.value());
            response.setSuccess(true);
            response.setPayload(comments);
        } catch (Exception e) {
            response.setMessage("Failed to retrieve comments: " + e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setSuccess(false);
            response.setPayload(null);
        }
        return response;
    }


}
