package com.ms_proj1.rest.webservices.restfulwebservices.user;

/*these imports help in setting constraints on properties of class*/

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;

@Entity(name = "user_details")
@Table(name = "user_details")
public class User {

    @Id
    @GeneratedValue
    private Integer id;

    /*these messages are used to send as the error message if it does not validate*/
    @Size(min = 2, message = "Name should have at least 2 characters")
//    @JsonProperty("user_name") is used here so that in output instead of giving name: it will give user_name:
    @JsonProperty("user_name")
    @Column(name = "user_name")
    private String name;

    @Past(message = "Birth date should be in the past")
    private LocalDate birthDate;

//    here the mappedBy is the variable name that is being mapped with from the Post class
//    JsonIgnore is added so that it is not shown in the output
    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Post> postList;

    public List<Post> getPostList() {
        return postList;
    }

    public void setPostList(List<Post> postList) {
        this.postList = postList;
    }

    public User() {
    }

    public User(Integer id, String name, LocalDate birthDate) {
        this.id = id;
        this.name = name;
        this.birthDate = birthDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", birthDate=" + birthDate +
                '}';
    }
}
