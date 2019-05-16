package com.example.restfulservice.controller;

import com.example.restfulservice.exception.NoUsersFoundException;
import com.example.restfulservice.exception.UserNotFoundException;
import com.example.restfulservice.model.User;
import com.example.restfulservice.service.UserService1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
public class UserController {

    @Autowired
    private UserService1 service;

    @GetMapping("/users")
    public List<User> retrieveAllUsers() {
        List<User> userlist = service.findAll();
        // check if any model exists in the list
        if (userlist.isEmpty()) {
            throw new NoUsersFoundException("No users found");
        }
        return userlist;
    }

    @GetMapping("/users/{id}")
    public Resource<User> retrieveUser(@PathVariable int id) {
        User user = service.findone(id);
        if (user == null) {
            throw new UserNotFoundException("id-" + id);
        }
        //HATEoas
        Resource<User> resource = new Resource<User>(user);
            ControllerLinkBuilder linkTo= linkTo(methodOn(this.getClass()).retrieveAllUsers());
            resource.add(linkTo.withRel("all-users"));

        return resource;
    }

    @PostMapping("/users")
    public ResponseEntity<Object> createuser(@Valid @RequestBody User user) {
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

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable int id) {
        User user = service.deleteById(id);

        if (user == null) {
            throw new UserNotFoundException("model not found to delete");
        }
    }

    /**
     * This method updates a model
     * @param user
     * @return ResponseEntity<Object>
     */
    @PutMapping("/users")
    public ResponseEntity<Object> updateUser(@Valid @RequestBody User user) {
        //-- check if model already exists
        User user1 = service.findone(user.getId());

        if (user1 == null) {
            throw new UserNotFoundException(user1.getName() + " User not found to update");
        }
        User saveduser = service.updateUser(user);

        return ResponseEntity.ok(saveduser);
        // return new ResponseEntity(saveduser,HttpStatus.CREATED);
    }
}
