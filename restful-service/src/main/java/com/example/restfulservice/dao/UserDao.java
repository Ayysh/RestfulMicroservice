package com.example.restfulservice.dao;

import com.example.restfulservice.model.Post;
import com.example.restfulservice.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class UserDao {

    @Autowired
    UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findByName(String name) {
        return userRepository.findByName(name);
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public void deleteById(int id) {

        userRepository.deleteById(id);
    }

    public Optional<User> findById(int id) {

        return userRepository.findById(id);
    }


    public Post savePost(Post post) {
        return postRepository.save(post);
    }
}
