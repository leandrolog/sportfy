package com.sportfy.controller;

import com.sportfy.dto.UserDto;
import com.sportfy.model.User;
import com.sportfy.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserController {

    @Autowired
    private final UserService userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserController(UserService userService, PasswordEncoder passwordEncoder, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userService = userService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Operation(description = "Create a new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User created successfully"),
            @ApiResponse(responseCode = "400", description = "Error creating a new user")
    })
    @PostMapping("/user")
    public User saveUser(@RequestBody UserDto userDto) {
        User user = new User();
        userDto.setPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
        BeanUtils.copyProperties(userDto ,user, "matches");
        return userService.save(user);
    }

    @Operation(description = "Get all users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return all users"),
            @ApiResponse(responseCode = "400", description = "Error finding all users")
    })
    @GetMapping("/users")
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<User> getAllUsers(@SortDefault(sort = "id", direction = Sort.Direction.ASC) Sort sort) {
        return userService.findAll(sort);
    }

    @Operation(description = "Find a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return one user"),
            @ApiResponse(responseCode = "400", description = "Error finding the user")
    })
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

    @Operation(description = "Delete a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Error deleting a user")
    })
    @DeleteMapping("user/{id}")
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Object> deleteUser(@PathVariable("id") Long id) {
        Optional<User> userModelOptional = userService.findById(id);
        if (!userModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No match was found with the provided ID.");
        }
        userService.delete(userModelOptional.get());
        return ResponseEntity.status(HttpStatus.OK).body("User deleted successfully");
    }

    @Operation(description = "Update a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully"),
            @ApiResponse(responseCode = "400", description = "Error updating a user")
    })
    @PutMapping("user/{id}")
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    public User updateUser(@PathVariable("id") Long id, @RequestBody User user) {
        Optional<User> userModelOptional = userService.findById(id);
        if (!userModelOptional.isPresent()) {
            throw new ConflictException("Match not found.");
        }
        User userToUpdate = userModelOptional.get();
        BeanUtils.copyProperties(user, userToUpdate);
        return userService.save(userToUpdate);
    }
}
