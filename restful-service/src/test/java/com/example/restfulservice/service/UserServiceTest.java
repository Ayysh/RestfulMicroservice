package com.example.restfulservice.service;

import com.example.restfulservice.dao.UserDao;
import com.example.restfulservice.exception.NoUsersFoundException;
import com.example.restfulservice.exception.UserAlreadyExistException;
import com.example.restfulservice.exception.UserNotFoundException;
import com.example.restfulservice.exception.UserUnderAgeException;
import com.example.restfulservice.model.Post;
import com.example.restfulservice.model.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {

    // ClassUnderTest -- Class whose methods need to be tested
    UserService userService;

    // Dependency that needs to be mocked
    @Mock
    UserDao userDao;

    @Before
    public void setUp() throws Exception {
        // Mock the dependent class. It can be removed if @Mock is written
        //   userDao = mock(UserDao.class);
        //inject the mocked class
        userService = new UserService(userDao);
    }

    @Test(expected = NoUsersFoundException.class)
    public void findAll_WhenNoUserFound() {
        // -- Prepare Dummy Data
        List<User> list = new ArrayList<>();
        //-- Set expectations
        when(userDao.findAll()).thenReturn(list);
        // Actual method call
        List<User> userList = userService.findAll();
        fail();
    }

    @Test
    public void findAll_WhenUserFound() {
        // -- Prepare Dummy Data
        List<User> expectedUserList = new ArrayList<>();
        expectedUserList.add(new User(2, "ice", new Date()));
        //-- Set expectations
        when(userDao.findAll()).thenReturn(expectedUserList);
        // Actual method call
        List<User> actualUserList = userService.findAll();
        // Assert or validate expected vs actual result
        assertEquals(expectedUserList, actualUserList);
        // Verify if mock method is used
        verify(userDao).findAll();
    }

    @Test(expected = UserUnderAgeException.class)
    public void save_WhenUserUnderAgeException() throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = format.parse("2015-12-31");
        //-- Actuall Call
        userService.save(new User(10, "ice", date));
        // To fail the test if exception doesnot occur
        fail();
    }

    @Test(expected = UserAlreadyExistException.class)
    public void save_WhenUserPresent() throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = format.parse("1990-12-31");
        User user = new User(10, "ice", date);
        //Expect the below call and return
        when(userDao.findByName(user.getName())).thenReturn(user);
        // Actual call
        userService.save(user);
        // To fail the test if exception doesnot occur
        fail("Error in the logic, Expected: User already exists, Found: User doesnt exist");
    }

    @Test
    public void save_WhenUserNotPresent() throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = format.parse("1990-12-31");
        User user = new User(10, "ice", date);
        //Expect the below call and return
        when(userDao.findByName(user.getName())).thenReturn(null);
        when(userDao.save(user)).thenReturn(user);
        // Actual call
        User savedUser = userService.save(user);
        assertEquals(user, savedUser);
    }

    @Test(expected = UserNotFoundException.class)
    public void findById_UserIdNotFound() throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = format.parse("1990-12-31");
        User user = new User(10, "ice", date);
        when(userDao.findById(user.getId())).thenReturn(null);
        userService.findById(5);
        fail();
    }

    @Test
    public void findById_UserIdFound() throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = format.parse("1990-12-31");
        Optional<User> user = Optional.of(new User(10, "ice", date));
        when(userDao.findById(user.get().getId())).thenReturn(user);
        Optional<User> userid = userService.findById(10);
        assertEquals(user, userid);
    }

    @Test
    public void updateUser() throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = format.parse("1990-12-31");
        Optional<User> existingUser = Optional.of(new User(10, "ice", date));
        User userWithNewValues = new User(10, "ice moni", date);
        when(userDao.findById(10)).thenReturn(existingUser);
        when(userDao.save(any(User.class))).thenReturn(userWithNewValues);
        User updatedUser = userService.updateUser(userWithNewValues);
        assertEquals(userWithNewValues, updatedUser);
    }

    @Test
    public void deleteById() throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = format.parse("1990-12-31");
        User existingUser = new User(10, "ice", date);
        Mockito.doNothing().when(userDao).deleteById(10);
        String userDeleted = userService.deleteById(10);
        assertEquals("deleted sucessfully", "10 deleted Successfully", userDeleted);
    }
   /* public Post savePost(Post post) {
        post = userDAO.savePost(post);
        return post;
    } */

    @Test
    public void savePost() throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = format.parse("1990-12-31");
        Post post = new Post();
        post.setId(10);
        post.setDescription("first string");
        post.setUser(new User(10, "ice", date));
        when(userDao.savePost(post)).thenReturn(post);
        Post post1 = userService.savePost(post);
        assertEquals(post, post1);
    }
}