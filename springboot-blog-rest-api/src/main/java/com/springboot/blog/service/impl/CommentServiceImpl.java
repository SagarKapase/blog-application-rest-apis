package com.springboot.blog.service.impl;

import com.springboot.blog.entities.Comment;
import com.springboot.blog.entities.Post;
import com.springboot.blog.exception.BlogApiException;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.payload.CommentDTO;
import com.springboot.blog.repository.CommentRepository;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.service.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {
    CommentRepository commentRepository;
    PostRepository postRepository;
    ModelMapper modelMapper;

    public CommentServiceImpl(CommentRepository commentRepository,PostRepository postRepository,ModelMapper modelMapper) {
        this.commentRepository = commentRepository;
        this.postRepository=postRepository;
        this.modelMapper=modelMapper;
    }

    @Override
    public CommentDTO createComment(long postId, CommentDTO commentDTO) {
        Comment comment=mapToEntity(commentDTO);

        //retrive post by id
        Post post=postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post","id",postId));

        //set post to comment entity
        comment.setPost(post);

        //comment entity to database
        Comment newComment=commentRepository.save(comment);

        return mapToDto(newComment);
    }

    @Override
    public List<CommentDTO> getCommentsByPostId(long postId) {

        //retrive comments from post id
        List<Comment> comments=commentRepository.findByPostId(postId);

        //convert list of comments entities into list of comment dto's
        return comments.stream().map(comment -> mapToDto(comment)).collect(Collectors.toList());
    }

    @Override
    public CommentDTO getCommentById(Long postId, Long commentId) {
        //retrive post by id
        Post post=postRepository.findById(postId).orElseThrow(() ->
                new ResourceNotFoundException("Post","id",postId));

        //retrive comment by id

        Comment comment=commentRepository.findById(commentId).orElseThrow(()
        -> new ResourceNotFoundException("Comment","id",commentId));

        if (!comment.getPost().getId().equals(post.getId())){
            throw new BlogApiException(HttpStatus.BAD_REQUEST,"Comment Does not belong to post");
        }
        return mapToDto(comment);
    }

    @Override
    public CommentDTO updateComment(Long postId, long commentId, CommentDTO commentRequest) {

        //retrive post by id
        Post post=postRepository.findById(postId).orElseThrow(() ->
                new ResourceNotFoundException("Post","id",postId));

        //retrive comment by id

        Comment comment=commentRepository.findById(commentId).orElseThrow(()
                -> new ResourceNotFoundException("Comment","id",commentId));

        if (!comment.getPost().getId().equals(post.getId())){
            throw new BlogApiException(HttpStatus.BAD_REQUEST,"Comment Does not belong to post");
        }

        comment.setName(commentRequest.getName());
        comment.setEmail(commentRequest.getEmail());
        comment.setBody(commentRequest.getBody());

        Comment updatedComment=commentRepository.save(comment);
        return mapToDto(updatedComment);

    }

    @Override
    public void deleteComment(Long postId, Long commentId) {

        //retrive post by id
        Post post=postRepository.findById(postId).orElseThrow(() ->
                new ResourceNotFoundException("Post","id",postId));

        //retrive comment by id

        Comment comment=commentRepository.findById(commentId).orElseThrow(()
                -> new ResourceNotFoundException("Comment","id",commentId));

        if (!comment.getPost().getId().equals(post.getId())){
            throw new BlogApiException(HttpStatus.BAD_REQUEST,"Comment Does not belong to post");
        }

        commentRepository.delete(comment);
    }

    //Creating private method to map Entity to DTO and DTO to Entity

    //Entity To DTO
    private CommentDTO mapToDto(Comment comment){

        CommentDTO commentDTO=modelMapper.map(comment,CommentDTO.class);
//        CommentDTO commentDTO=new CommentDTO();
//        commentDTO.setId(comment.getId());
//        commentDTO.setName(comment.getName());
//        commentDTO.setEmail(comment.getEmail());
//        commentDTO.setBody(comment.getBody());

        return commentDTO;
    }

    //DTO to Entity
    private Comment mapToEntity(CommentDTO commentDTO){
        Comment comment=modelMapper.map(commentDTO,Comment.class);
//        Comment comment=new Comment();
//        comment.setId(commentDTO.getId());
//        comment.setName(commentDTO.getName());
//        comment.setEmail(commentDTO.getEmail());
//        comment.setBody(commentDTO.getBody());

        return comment;
    }
}
