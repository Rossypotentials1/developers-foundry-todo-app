package com.rossypotential.todo_assignment.controller;


import com.rossypotential.todo_assignment.dtos.response.ApiResponse;
import com.rossypotential.todo_assignment.dtos.response.UserResponse;
import com.rossypotential.todo_assignment.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserService userService;

    @PutMapping("/{id}/edit-user")
    public ResponseEntity<ApiResponse<UserResponse<?>>> editUser(@PathVariable Long id,
                                                                 @RequestParam(required = false) String firstName,
                                                                 @RequestParam(required = false) String lastName,
                                                                 @RequestParam(required = false) String phoneNumber

    )
    {
    return userService.editUser(id, firstName, lastName, phoneNumber);
                                        }


    @GetMapping("/{id}")
    public ResponseEntity<?> viewUser(@PathVariable Long id) {
        try {
            UserResponse appUser = userService.viewUser(id);
            return new ResponseEntity<>(appUser, HttpStatus.OK);
        } catch (UsernameNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, ex.getMessage()));
        }

    }

        @GetMapping
        public ResponseEntity<ApiResponse<Page<UserResponse<?>>>> getAllUsers(
                @RequestParam(defaultValue = "0") int page,
                @RequestParam(defaultValue = "10") int size) {

            Page<UserResponse<?>> usersPage = userService.getAllUsers(page, size);
            ApiResponse<Page<UserResponse<?>>> response = ApiResponse.ok("",usersPage);

            return ResponseEntity.ok(response);
        }
    }



