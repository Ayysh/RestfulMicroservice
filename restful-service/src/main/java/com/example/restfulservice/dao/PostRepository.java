package com.example.restfulservice.dao;

import com.example.restfulservice.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

    @Repository
    public interface PostRepository extends JpaRepository<Post, Integer> //<entity,primarykey>
    {

    }

