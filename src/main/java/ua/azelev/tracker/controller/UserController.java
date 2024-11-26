package ua.azelev.tracker.controller;

import ua.azelev.tracker.model.User;
import ua.azelev.tracker.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(summary = "Зареєструвати нового користувача", description = "Створює нового користувача та зберігає його в базі даних.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Користувач успішно зареєстрований"),
            @ApiResponse(responseCode = "400", description = "Невірні дані для реєстрації")
    })
    @PostMapping("/register")
    public User registerUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @Operation(summary = "Отримати користувача за ID", description = "Отримує інформацію про користувача за його унікальним ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Користувач успішно знайдений"),
            @ApiResponse(responseCode = "404", description = "Користувача не знайдено")
    })
    @GetMapping("/{userId}")
    public User getUserById(@PathVariable Long userId) {
        return userService.getUserById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
