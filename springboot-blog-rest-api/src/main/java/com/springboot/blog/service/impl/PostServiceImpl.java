package com.springboot.blog.service.impl;

import com.springboot.blog.entities.Post;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.payload.PostDTO;
import com.springboot.blog.payload.PostResponse;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.service.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {
    private PostRepository postRepository;
    private ModelMapper modelMapper;

    public PostServiceImpl(PostRepository postRepository,ModelMapper modelMapper) {
        this.postRepository = postRepository;
        this.modelMapper=modelMapper;
    }

    //This method is for creating post

    @Override
    public PostDTO createPost(PostDTO postDTO) {

        //convert dto to entity
        Post post=mapToEntity(postDTO);

        //to save the post into database
        Post newPost=postRepository.save(post);

        //Entity to DTO
        PostDTO postResponse=mapToDTO(newPost);


        return postResponse;
    }

    @Override
    public PostResponse getAllPosts(int pageNo, int pageSize,String sortBy,String sortDir) {

        //condition for sorting
        Sort sort=sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())?Sort.by(sortBy).ascending()
                :Sort.by(sortBy).descending();

        //create pageable instance
        Pageable pageable= PageRequest.of(pageNo,pageSize, Sort.by(sortBy));

        Page<Post> posts=postRepository.findAll(pageable);

        //get content for page object
        List<Post> listOfPosts=posts.getContent();
        List<PostDTO> content=listOfPosts.stream().map(post -> mapToDTO(post)).collect(Collectors.toList());

        PostResponse postResponse=new PostResponse();
        postResponse.setContent(content);
        postResponse.setPageNo(posts.getNumber());
        postResponse.setPageSize(posts.getSize());
        postResponse.setTotalElements(posts.getTotalElements());
        postResponse.setTotalPages(posts.getTotalPages());
        postResponse.setLast(posts.isLast());

        return postResponse;
    }

    @Override
    public PostDTO getPostById(long id) {
        Post post=postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post","id",id));

        return mapToDTO(post);
    }

    @Override
    public PostDTO updatePost(PostDTO postDTO, long id) {
        //To get post by the id from database
        Post post=postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post","id",id));
        post.setTitle(postDTO.getTitle());
        post.setDescription(postDTO.getDescription());
        post.setContent(postDTO.getContent());

        Post updatedPost=postRepository.save(post);

        return  mapToDTO(updatedPost);
    }
//to delete the post
    @Override
    public void deletePostById(long id) {
        //To get post by the id from database
        Post post=postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post","id",id));

        postRepository.delete(post);
    }

    //to return the saved post to the client then we have to convert entity into dto object
    private PostDTO mapToDTO(Post post){

        PostDTO postDTO=modelMapper.map(post,PostDTO.class);
//        PostDTO postDTO=new PostDTO();
//        postDTO.setId(post.getId());
//        postDTO.setTitle(post.getTitle());
//        postDTO.setDescription(post.getDescription());
//        postDTO.setContent(post.getContent());

        return postDTO;
    }
 //DTO object to entity
    private Post mapToEntity(PostDTO postDTO){

        Post post=modelMapper.map(postDTO,Post.class);
//        Post post=new Post();
//        post.setTitle(postDTO.getTitle());
//        post.setDescription(postDTO.getDescription());
//        post.setContent(postDTO.getContent());

        return post;
    }
}
