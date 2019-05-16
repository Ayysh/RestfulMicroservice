package com.example.restfulservice.service;

import com.example.restfulservice.exception.UserAlreadyExistException;
import com.example.restfulservice.model.User;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Component
@Repository // to interact with db
@Transactional //declarative transcation mngt //each method will be involved in transcation

// This UserService1 class is used for creating List of users and using it
public class UserService1 {

    private static List<User> users = new ArrayList<>();
    private static int usercount =3;
    static {
       users.add(new User(1,"Ayys",new Date()));
       users.add(new User(2,"Bala",new Date()));
       users.add(new User(3,"David",new Date()));
    }

    public List<User> findAll(){
    return users;
    }

    public User save(User user){

        // User user1 = userDAO.findByName(model.getName());

        User user1 = findByName(user.getName());

        if (user1 != null) {
            throw new UserAlreadyExistException(user1.getName() + " User already exists");
        }
        if(user.getId()== null){
            user.setId(++usercount);
        }

        //model = userDAO.savePost(model)
        users.add(user);
        return user;
    }

    public User findone(int id){
        for (User user:users) {
            if (user.getId() == id) {
                return user;
            }
        }
            return null;
    }

    public User findByName(String name){
        for (User user:users) {
            if (user.getName().equalsIgnoreCase(name)) {
                return user;
            }
        }
        return null;
    }


    public User deleteById(int id){
        Iterator<User> iterator = users.iterator();
        while (iterator.hasNext())
        {
            User user = iterator.next();
            if(user.getId() == id){
                iterator.remove();
                return user;
            }
        }
        return null;
    }

    public User updateUser(User user){
        for (User user2:users) {
            if (user2.getId() == user.getId()) {
                user2.setName(user.getName());
                user2.setBirthdate(user.getBirthdate());
                return user2;
            }
        }
        return null;
    }
}
