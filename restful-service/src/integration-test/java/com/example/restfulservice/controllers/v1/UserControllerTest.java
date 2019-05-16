package com.example.restfulservice.controllers.v1;

import com.example.restfulservice.configuration.IntegrationTestBase;
import com.example.restfulservice.configuration.TestRunnerConfiguration;
import com.example.restfulservice.dao.UserDao;
import com.example.restfulservice.model.User;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Date;

import static io.restassured.RestAssured.given;

@ActiveProfiles("h2integration")
@ContextConfiguration(classes = TestRunnerConfiguration.class)
public class UserControllerTest extends IntegrationTestBase {

    @Autowired
    private UserDao userDao;

    @BeforeMethod
    public void setUpMethod(){
        //-- Saving into the DB
        userDao.save(new User(30,"Ayys",new Date()));
        userDao.save(new User(40,"Adi",new Date()));
    }

    @Test
    public void testGetAllUsers(){
   //-- Calling the endpoint that fetches you all users from h2 db.
        Response response = given()
                .port(RestAssured.port)
                .contentType(ContentType.JSON)
                .when()
                .get("/jpa/users");

        //This is how to log response if error
        response.then().log().ifError();

        //-- This is done to see the actual response in json format
        JsonPath jsonPath = response.jsonPath();

        // Assert the response
        response.then()
                .content("[0].id",Matchers.is(1))
                .content("[0].name",Matchers.is("Ayys"))
                .content("[1].id",Matchers.is(2))
                .content("[1].name",Matchers.is("Adi"))
                .statusCode(200);
    }

    @Test
    public void testGetUser(){
        //-- Calling the endpoint that fetches you all users from h2 db.
        Response response = given()
                .port(RestAssured.port)
                .contentType(ContentType.JSON)
                .when()
                .get("/jpa/users/{id}",1);

        //This is how to log response if error
        response.then().log().ifError();

        //-- This is done to see the actual response in json format
        JsonPath jsonPath = response.jsonPath();

        // Assert the response
        response.then()
                .content("id",Matchers.is(1))
                .content("name",Matchers.is("Ayys"))
                .statusCode(200);
    }
    // Post Endpoint

    @Test
    public void testPostUser(){
        //-- Calling the endpoint that posts you all users to h2 db.
      //  Map<String,String> user = new HashMap<>();
       // user.put("name", "adi");
        //user.put("birthdate","2000-03-25T20:21:45.839Z");
        Response response = given()
                .port(RestAssured.port)
                .contentType(ContentType.JSON)
               // .body(user)
                .body("{ \"birthdate\": \"2000-03-25T20:21:45.839Z\", \"name\": \"shank\"}")
                .when()
                .post("/jpa/users");
        //This is how to log response if error
        response.then().log().ifError();
        //-- This is done to see the actual response in json format
        JsonPath jsonPath = response.jsonPath();
        // Assert the response
        response.then()
                .statusCode(201);
    }

    // Put Endpoint
    @Test
    public void testPutUser(){
        Response response = given()
                .port(RestAssured.port)
                .contentType(ContentType.JSON)
                .body("{ \"birthdate\": \"2000-03-25T20:21:45.839Z\", \"name\": \"Ayys\",\"id\": \"1\"}")
                .when()
                .put("/jpa/users");
        //This is how to log response if error
        response.then().log().ifError();
        //-- This is done to see the actual response in json format
        JsonPath jsonPath = response.jsonPath();
        // Assert the response
        response.then()
                .content("id",Matchers.is(1))
                .content("name",Matchers.is("Ayys"))
                .statusCode(200);
    }

    //Delete Endpoint
    @Test
    public void testDeleteUser(){
        Response response = given()
                .port(RestAssured.port)
                .contentType(ContentType.JSON)
                .when()
                .delete("/jpa/users/{id}",1);
        //This is how to log response if error
        response.then().log().ifError();
        //-- This is done to see the actual response in json format
        JsonPath jsonPath = response.jsonPath();
        // Assert the response
        response.then()
                .content(Matchers.is("1 deleted Successfully"))
                .statusCode(200);
    }
}
