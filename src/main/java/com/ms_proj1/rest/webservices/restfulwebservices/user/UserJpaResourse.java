package com.ms_proj1.rest.webservices.restfulwebservices.user;

import com.ms_proj1.rest.webservices.restfulwebservices.customException.UserNotFoundException;
import com.ms_proj1.rest.webservices.restfulwebservices.jpa.PostRepository;
import com.ms_proj1.rest.webservices.restfulwebservices.jpa.UserRepository;
import jakarta.validation.Valid;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
public class UserJpaResourse {
    private UserRepository userRepository;
    private PostRepository postRepository;

    UserJpaResourse(UserRepository repository,PostRepository postRepository)
    {
        this.userRepository = repository;
        this.postRepository = postRepository;
    }

    @DeleteMapping("/jpa/users/{id}")
    public void deleteUser(@PathVariable int id) {

        userRepository.deleteById(id);
    }

    @GetMapping("/jpa/users")
    public List<User> retrieveAllUsers() {

        return this.userRepository.findAll();
    }


    @GetMapping("/jpa/users/entityModel/{id}")
    public EntityModel<User> retrieveUserEntityModel(@PathVariable int id) {
        Optional<User> user = userRepository.findById(id);
        if(user.isEmpty()) {
            throw new UserNotFoundException("id:"+id);
        }

        EntityModel<User> entityModel = EntityModel.of(user.get());
        WebMvcLinkBuilder link = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).retrieveAllUsers());
        entityModel.add(link.withRel("all-users"));
        return entityModel;
    }

    @PostMapping("/jpa/users")
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        User savedUser = userRepository.save(user);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(savedUser.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    @GetMapping("/jpa/users/{id}/post")
    public List<Post> retrievePostForUser(@PathVariable int id) {

        Optional<User> user = userRepository.findById(id);
        if(user.isEmpty()) {
            throw new UserNotFoundException("id:"+id);
        }

        return user.get().getPostList();
    }

    @PostMapping("/jpa/users/{id}/post")
    public ResponseEntity<Object> createPostForUser(@PathVariable int id, @Valid @RequestBody Post post) {

        Optional<User> user = userRepository.findById(id);
        if(user.isEmpty()) {
            throw new UserNotFoundException("id:"+id);
        }

        post.setUser(user.get());
        Post savedPost = postRepository.save(post);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(savedPost.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

}
