package com.springboot.blog.controller;

import com.springboot.blog.entities.Comment;
import com.springboot.blog.payload.CommentDTO;
import com.springboot.blog.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/")
public class CommentController {

    private CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<CommentDTO> createComment(
            @PathVariable(value = "postId")long postId,
            @RequestBody CommentDTO commentDTO){
        return new ResponseEntity<>(commentService.createComment(postId,commentDTO), HttpStatus.CREATED);
    }

    @GetMapping("/posts/{postId}/comments")
    public List<CommentDTO> getCommentsByPostId(@PathVariable(value = "postId") Long postId){
        return commentService.getCommentsByPostId(postId);
    }

    @GetMapping("/posts/{postId}/comments/{id}")
    public ResponseEntity<CommentDTO> getCommentById(@PathVariable(value = "postId")Long postId,
                                                     @PathVariable(value = "id") Long commentId){
        CommentDTO commentDTO=commentService.getCommentById(postId,commentId);
        return new ResponseEntity<>(commentDTO,HttpStatus.OK);
    }

    //Rest API for update the comment of particular post
    @PutMapping("/posts/{postId}/comments/{id}")
    public ResponseEntity<CommentDTO> updateComment(@PathVariable(value = "postId")Long postId,
                                                     @PathVariable(value = "id") Long commentId,
                                                     @RequestBody CommentDTO commentDTO){
        CommentDTO updatedComment=commentService.updateComment(postId,commentId,commentDTO);
        return new ResponseEntity<>(updatedComment,HttpStatus.OK);
    }

    @DeleteMapping("/posts/{postId}/comments/{id}")
    public ResponseEntity<String> deletecomment(@PathVariable(value = "postId")Long postId,
                                                @PathVariable(value = "id") Long commentId){
        commentService.deleteComment(postId,commentId);
        return new ResponseEntity<>("Comment Deleted Successfully...",HttpStatus.OK);
    }
}
