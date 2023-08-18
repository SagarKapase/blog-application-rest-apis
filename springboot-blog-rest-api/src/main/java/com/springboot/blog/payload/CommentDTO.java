package com.springboot.blog.payload;

import lombok.Data;

//This class is created for to send the response to the client
@Data
public class CommentDTO {
    private long id;
    private String name;
    private String email;
    private String body;
}
