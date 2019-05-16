package com.example.restfulservice.controller;

import com.example.restfulservice.exception.UserNotFoundException;
import com.example.restfulservice.model.Post;
import com.example.restfulservice.model.User;
import com.example.restfulservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
public class UserJpaController {

    @Autowired
    private UserService service;


    @GetMapping("/jpa/users")
    public List<User> retrieveAllUsers() {
        return service.findAll();
    }

    @GetMapping("/jpa/users/{id}")
    public Resource<User> retrieveUser(@PathVariable int id ) {
        Optional<User> user = service.findById(id);
        // if (model == null) {
        if (!user.isPresent()) {
            throw new UserNotFoundException("id-" + id);
        }
        //HATEoas
        Resource<User> resource = new Resource<User>(user.get());
        ControllerLinkBuilder linkTo = linkTo(methodOn(this.getClass()).retrieveAllUsers());
        resource.add(linkTo.withRel("all-users"));

        return resource;
    }

    // Actual call
    @PostMapping("/jpa/users")
    public ResponseEntity<Object> createuser(@Valid @RequestBody User user)
    {
        //-- check if model already exists
        User saveduser = service.save(user);

        // Return or to give the location path,which has details/value of response
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest() // "/users"
                .path("/{id}") // "/users.4"
                .buildAndExpand(saveduser.getId()).toUri();

        return ResponseEntity.created(location).build();
        // return new ResponseEntity(saveduser,HttpStatus.CREATED);
    }

    @PostMapping("/jpa/users/{id}/posts")
    public ResponseEntity<Object> createPost(@PathVariable int id, @RequestBody Post post) {
        //-- check if user already exists
        Optional<User> userOptional = service.findById(id);
        // if (model == null) {
        if (!userOptional.isPresent()) {
            throw new UserNotFoundException("id-" + id);
        }
        User user = userOptional.get();
        post.setUser(user);
        post = service.savePost(post);
        // Return or to give the location path,which has details/value of response
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest() // "/users"
                .path("/{id}") // "/users.4"
                .buildAndExpand(post.getId()).toUri();

        return ResponseEntity.created(location).build();
        // return new ResponseEntity(saveduser,HttpStatus.CREATED);
    }

    @DeleteMapping("/jpa/users/{id}")
    public String deleteUser(@PathVariable int id) {

        return service.deleteById(id);
    }

    /**
     * This method updates a model
     *
     * @param user
     * @return ResponseEntity<Object>
     */

    @PutMapping("/jpa/users")
    public ResponseEntity<Object> updateUser(@Valid @RequestBody User user) {

        User saveduser = service.updateUser(user);

        return ResponseEntity.ok(saveduser);
        // return new ResponseEntity(saveduser,HttpStatus.CREATED);
    }
}
