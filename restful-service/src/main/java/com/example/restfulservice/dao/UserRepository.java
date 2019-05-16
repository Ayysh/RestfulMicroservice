package com.example.restfulservice.dao;


import com.example.restfulservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> //<entity,primarykey>
{

   User findByName(String name);

}
