package com.ms_proj1.rest.webservices.restfulwebservices.user;

import com.ms_proj1.rest.webservices.restfulwebservices.customException.UserNotFoundException;
import jakarta.validation.Valid;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
public class UserResource {

    private UserDaoService userDaoService;

    UserResource(UserDaoService userDaoService) {
        this.userDaoService = userDaoService;
    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable int id) {
        userDaoService.deleteUserById(id);
    }

//    will be avialbe at url http://localhost:8080/users
    @GetMapping("/users")
    public List<User> retrieveAllUsers() {
        return this.userDaoService.findAll();
    }

    @GetMapping("/users/{id}")
    public User retrieveUser(@PathVariable int id) {
        User user = userDaoService.findOne(id);
        if(null == user) {
            throw new UserNotFoundException("id:"+id);
        }
        return user;
    }

//    here we are using HAL and give a link in the result
    @GetMapping("/users/entityModel/{id}")
    public EntityModel<User> retrieveUserEntityModel(@PathVariable int id) {
        User user = userDaoService.findOne(id);
        if(null == user) {
            throw new UserNotFoundException("id:"+id);
        }

//        here we are creating an entity model object inorder to add the HAL values
//        we have added the link to a specific function. basically the url that can be sent as new response
//                in our case it is url for retrieving all the users
        EntityModel<User> entityModel = EntityModel.of(user);
        WebMvcLinkBuilder link = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).retrieveAllUsers());
        entityModel.add(link.withRel("all-users"));
        return entityModel;
    }

    /*here @Valid helps to validate the user properties based on the constraits that has been set*/
    @PostMapping("/users")
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        User savedUser = userDaoService.save(user);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(savedUser.getId()).toUri();
//        ServletUriComponentsBuilder is used to fetch the details from the mapping used in the function.
//        here we want to add id to the path so that caller can use that url to verify the inserted data
//          it is returned as URI which we pass in created() below
//        ServletUriComponentsBuilder.fromCurrentRequest() this fetches the current uri and path is used to add id to it
        return ResponseEntity.created(location).build();
//        this class ResponseEntity is part of spring framework and is used to return the response back to the caller. here we are using null. created() return status as 201 which is for created
//        When you send a POST request with a JSON body (like user details) using Talend API Tester (or Postman, etc.), Spring Boot automatically converts that JSON into a Java object (User in your case) using something called message converters — specifically, the Jackson library by default.
//
//        Here’s the flow happening under the hood:
//
//        1. @RequestBody User user tells Spring:
//
//        "Take the request body (which is JSON), and try to map it into a User object."
//
//        2. Spring uses a HttpMessageConverter.
//
//        For JSON, Spring Boot uses MappingJackson2HttpMessageConverter by default.
//
//        This converter internally uses Jackson, a popular Java JSON library.
//
//        3. Jackson automatically maps JSON fields to the Java fields in the User class.
//
//        Jackson looks at the field names in JSON and matches them to the field names in the Java User class.
//
//        Example:
//
//        Suppose you send this JSON:
//
//        json
//                Copy
//        Edit
//        {
//            "name": "John",
//                "email": "john@example.com"
//        }
//        and your User class is:
//
//        java
//                Copy
//        Edit
//        public class User {
//            private String name;
//            private String email;
//
//            // getters and setters
//        }
//        Jackson will:
//
//        Find name and email in JSON.
//
//        Find matching fields (name, email) in User.
//
//        Use setters (setName, setEmail) to populate the User object.
//
//✅ Important Requirements for this to work:
//
//        Your User class must have:
//
//        A default constructor (no-argument constructor).
//
//                Getters and setters (or you can use Lombok’s @Data to auto-generate them).
//
//        The field names in JSON must match the Java field names (unless you use annotations like @JsonProperty to map them).
//
//        In short:
//        Spring Boot + Jackson automatically handles the JSON to Java object mapping behind the scenes when you use @RequestBody.
//        Would you also like me to show you what happens if the JSON field names don't exactly match the Java field names — and how to fix it using @JsonProperty? 🚀 (It's very useful in real-world APIs.)
/*You're building a simple Spring Boot REST API for user management with in-memory storage. Let me explain how JSON from the client becomes a User object, and how the full flow works step-by-step when you send a POST /users request.

🔁 Request Flow Breakdown
Let’s say your Talend API Tester sends this POST request to http://localhost:8080/users:

Request Body:
json
Copy
Edit
{
  "name": "Alice",
  "birthDate": "1995-05-10"
}
You do not send the id because the server assigns it.

🧠 Step-by-Step Execution:
✅ 1. Spring sees @PostMapping("/users") and calls:
java
Copy
Edit
public ResponseEntity<User> createUser(@RequestBody User user)
The @RequestBody tells Spring:

“Take the request body (JSON) and convert it into a User object using Jackson.”

✅ 2. JSON → Java Object Conversion (by Jackson)
Spring Boot uses Jackson (MappingJackson2HttpMessageConverter) to:

Create a new User object.

Set name to "Alice" and birthDate to LocalDate.parse("1995-05-10").

But wait! Your User class has only a parameterized constructor.
Jackson needs a default constructor (no-arg constructor) to instantiate the object before calling setters.

❗Problem in your code:
Your User class does not have a no-argument constructor, which Jackson needs.

❌ This would throw an error like:

pgsql
Copy
Edit
com.fasterxml.jackson.databind.exc.InvalidDefinitionException: Cannot construct instance of `User` (no Creators, like default constructor, exist)
✅ Fix: Add a no-arg constructor
Update your User class like this:

java
Copy
Edit
public class User {
    private Integer id;
    private String name;
    private LocalDate birthDate;

    // No-arg constructor required by Jackson
    public User() {
    }

    public User(Integer id, String name, LocalDate birthDate) {
        this.id = id;
        this.name = name;
        this.birthDate = birthDate;
    }

    // getters and setters...
}
📌 What Happens After Jackson Deserialization?
A new User object is created with:

name = "Alice"

birthDate = LocalDate.of(1995, 5, 10)

id = null

This object is passed into your service method:

java
Copy
Edit
User savedUser = userDaoService.save(user);
In UserDaoService.save():

id is auto-generated.

User is added to the in-memory list.

The API constructs the URI:

java
Copy
Edit
/users/{newlyCreatedId}
using:

java
Copy
Edit
ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
Returns HTTP 201 Created with a Location header pointing to the new user resource.

✅ Final Output
Response:
bash
Copy
Edit
201 Created
Location: http://localhost:8080/users/4
🔄 Full Cycle Summary

Step	Description
Client	Sends JSON (name, birthDate) via POST
Spring	@RequestBody uses Jackson to convert JSON to User
Jackson	Needs a no-arg constructor and setters
Service	Adds user to list and assigns ID
Response	201 Created + URI to newly created resource
Would you like to add validation next (e.g., name must be at least 2 characters)? That would use @Valid and @Size annotations.*/
    }
}
