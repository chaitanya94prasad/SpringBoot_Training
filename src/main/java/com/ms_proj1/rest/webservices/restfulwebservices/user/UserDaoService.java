package com.ms_proj1.rest.webservices.restfulwebservices.user;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

//here we are trying to store data into DB and JPA and hibernate can be used to communicate with DB. DAO : Data Access Object used to access data
//right now we are using list for storage

@Component
public class UserDaoService {

    private static List<User> userList = new ArrayList<>();
    private static Integer userCount = 0;

    //    initializing with a set of users, using static because list is static

    static {
        userList.add(new User(++userCount,"Adam", LocalDate.now().minusYears(30)));
        userList.add(new User(++userCount,"Eve", LocalDate.now().minusYears(25)));
        userList.add(new User(++userCount,"Jim", LocalDate.now().minusYears(20)));
    }

    public User save(User user) {
        user.setId(++userCount);
        userList.add(user);
        return user;
    }
//    return all the users
    public List<User> findAll() {
        return userList;
    }

//    study Predicate and lambda in Java
    public User findOne(int id) {
        Predicate<? super User> predicate = User -> User.getId().equals(id);
        return this.userList.stream().filter(predicate).findFirst().orElse(null);
//        here if we pass an ID that does not exist it will give back white label error as id is not present
//        what we can do is instead of         return this.userList.stream().filter(predicate).findFirst().get();
//        we can use         return this.userList.stream().filter(predicate).findFirst().orElse(null)
//        now this will return 200 code but an empty page which is not right as well
    }

    public void deleteUserById(int id) {
        Predicate<? super User> predicate = User -> User.getId().equals(id);
        userList.removeIf(predicate);
    }
    
}
