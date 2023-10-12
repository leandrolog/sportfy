package com.sportfy.controller;

import com.sportfy.controller.ConflictException;
import com.sportfy.dto.UserDto;
import com.sportfy.model.User;
import com.sportfy.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class UserController {

    @Autowired
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/user")
    public User saveUser(@RequestBody UserDto userDto) {
        User user = new User();
        BeanUtils.copyProperties(userDto ,user);
        return userService.save(user);
    }
    @GetMapping("/users")
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<User> getAllUsers(@SortDefault(sort = "id", direction = Sort.Direction.ASC) Sort sort) {
        return userService.findAll(sort);
    }

    @GetMapping("user/{id}")
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    public User getUser(@PathVariable("id") Long id) {
        Optional<User> userModelOptional = userService.findById(id);
        if (userModelOptional.isEmpty()) {
            throw new ConflictException("No user with this ID was found. Please provide a valid user ID.");
        } else {
            return userModelOptional.get();
        }
    }

    @DeleteMapping("user/{id}")
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Object> deleteUser(@PathVariable("id") Long id) {
        Optional<User> userModelOptional = userService.findById(id);
        if (!userModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No match was found with the provided ID.");
        }
        userService.delete(userModelOptional.get());
        return ResponseEntity.status(HttpStatus.OK).body("Usuário deletado");
    }

    @PutMapping("user/{id}")
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    public User updateUser(@PathVariable("id") Long id, @RequestBody User user) {
        Optional<User> userModelOptional = userService.findById(id);
        if (!userModelOptional.isPresent()) {
            throw new ConflictException("Match not found.");
        }
        User userToUpDate = userModelOptional.get();
        BeanUtils.copyProperties(user, userToUpDate);
        return userService.save(userToUpDate);
    }
}
