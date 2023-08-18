package com.springboot.blog.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

//@Data //To create getter and setter also toString() method on runtime
/*
here we use @Getter and @Setter annotations for to get all comments of a particular post becase
@Data annotation internally uses toString() method and ModelMapper does not support the toString() method
 */
@Getter
@Setter
@AllArgsConstructor //To create all arg constructor
@NoArgsConstructor //To create empty constructor bz our hibernate internally uses proxies to create object and that object won't be without parameter so that's why we need to define NO arg constructor
@Entity //To map this entity with database
@Table(
        name = "posts",uniqueConstraints = {@UniqueConstraint(columnNames = {"title"})}
)
public class Post {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long id;
    @Column(name = "title",nullable = false)
    private String title;
    @Column(name = "description",nullable = false)
    private String description;
    @Column(name = "content",nullable = false)
    private String content;

    @OneToMany(mappedBy = "post",cascade = CascadeType.ALL,orphanRemoval = true)
    private Set<Comment> comments=new HashSet<>();
}
