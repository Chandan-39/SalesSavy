package com.example.demo.admincontrollers;

import com.example.demo.entity.User;
import com.example.demo.adminservices.AdminUserService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin(origins = "http://localhost:5174", allowCredentials = "true")
@RestController
@RequestMapping("/admin/user")
public class AdminUserController {

    private final AdminUserService svc;

    public AdminUserController(AdminUserService svc) {
        this.svc = svc;
    }

    @PutMapping("/modify")
    public ResponseEntity<?> modify(@RequestBody Map<String, Object> req) {
        try {
            System.out.println("Request payload: " + req);

            Object idObj = req.get("userId");
            if (!(idObj instanceof Number)) {
                return ResponseEntity.badRequest().body(Map.of("error", "userId must be a number"));
            }

            Integer userId = ((Number) idObj).intValue();
            String username = (String) req.get("username");
            String email = (String) req.get("email");
            String role = (String) req.get("role");

            System.out.println("userId: " + userId);
            System.out.println("username: " + username);
            System.out.println("email: " + email);
            System.out.println("role: " + role);

            User u = svc.modifyUser(userId, username, email, role);

            return ResponseEntity.ok(Map.of(
                "userId", u.getUserId(),
                "username", u.getUsername(),
                "email", u.getEmail(),
                "role", u.getRole().name(),
                "createdAt", u.getCreatedAt(),
                "updatedAt", u.getUpdatedAt()
            ));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .body(Map.of("error", "Data integrity violation: " + e.getMostSpecificCause().getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Internal server error"));
        }
    }

    @GetMapping("/getbyid")
    public ResponseEntity<?> getById(@RequestParam Integer userId) {
        try {
            User u = svc.getUserById(userId);
            return ResponseEntity.ok(Map.of(
                "userId", u.getUserId(),
                "username", u.getUsername(),
                "email", u.getEmail(),
                "role", u.getRole().name(),
                "createdAt", u.getCreatedAt(),
                "updatedAt", u.getUpdatedAt()
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Internal server error"));
        }
    }
}
