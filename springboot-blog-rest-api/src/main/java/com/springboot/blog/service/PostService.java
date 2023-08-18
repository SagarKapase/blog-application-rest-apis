package com.springboot.blog.service;


import com.springboot.blog.payload.PostDTO;
import com.springboot.blog.payload.PostResponse;

import java.util.List;

public interface PostService {
    PostDTO createPost(PostDTO postDTO);

    PostResponse getAllPosts(int pageNo, int pageSize,String sortBy,String sortDir);

    //To get post by that id
    PostDTO getPostById(long id);

    //To update the post
    //PostDTO is a return type

    PostDTO updatePost(PostDTO postDTO, long id);

    //To delete the post by post id
    void deletePostById(long id);
}


