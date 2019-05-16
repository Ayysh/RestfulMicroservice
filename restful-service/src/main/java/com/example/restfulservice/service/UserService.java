package com.example.restfulservice.service;

import com.example.restfulservice.dao.UserDao;
import com.example.restfulservice.exception.NoUsersFoundException;
import com.example.restfulservice.exception.UserAlreadyExistException;
import com.example.restfulservice.exception.UserNotFoundException;
import com.example.restfulservice.exception.UserUnderAgeException;
import com.example.restfulservice.model.Post;
import com.example.restfulservice.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Component
//@Repository // to interact with db
//@Transactional //declarative transcation mngt //each method will be involved in transcation
public class UserService {

    private UserDao userDAO;

    @Autowired
    public UserService(UserDao userDAO) {
        this.userDAO = userDAO;
    }

    public List<User> findAll() {
        List<User> userlist = userDAO.findAll();
        // check if any model exists in the list
        if (userlist.isEmpty()) {
            throw new NoUsersFoundException("No users found");
        }
        return userlist;
    }

    public User save(User user) {
        Date currentDate = new Date();
        long diffInMillies = Math.abs(user.getBirthdate().getTime() - currentDate.getTime());
        long difference = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

        if (difference < (18 * 365)) {
            throw new UserUnderAgeException(user.getName() + " User is underage");
        }

        User user1 = userDAO.findByName(user.getName());

        if (user1 != null) {
            throw new UserAlreadyExistException(user1.getName() + " User already exists");
        }
        //-- else below code is executed
        user = userDAO.save(user);
        return user;
    }

    /**
     * Updates an existing user with new values
     * @param userToUpdate - User with new values
     * @return User post updation
     */
    public User updateUser(User userToUpdate) { // userToUpdate - id = 10, name = ayys moni, dob = 18.02.1992
        //-- check if model already exists
        Optional<User> userFromDB = findById(userToUpdate.getId()); //  find(10) ; userFromDB = id =10, name = ayys, dob = 10.05.1992
        // User exists so set the new values
        userFromDB.get().setName(userToUpdate.getName());
        userFromDB.get().setBirthdate(userToUpdate.getBirthdate());
        userFromDB.get().setPosts(userToUpdate.getPosts());
        //Save the user in db
        return userDAO.save(userFromDB.get());
    }

    public String deleteById(int id) {
        userDAO.deleteById(id);
        return id + " deleted Successfully";
    }

    public Optional<User> findById(int id) {
        Optional<User> userOptional = userDAO.findById(id);
        // to check if user is present using optional, we do <obj>.isPresent
        if (!userOptional.isPresent()) {
            throw new UserNotFoundException(id + " not found");
        }
        return userOptional;
    }

    public Post savePost(Post post) {
        post = userDAO.savePost(post);
        return post;
    }
}
