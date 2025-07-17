package com.example.demo.admincontrollers;

import com.example.demo.adminservices.AdminBusinessService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:5174", allowCredentials = "true")
@RestController
@RequestMapping("/admin/business")
public class AdminBusinessController {

    private final AdminBusinessService svc;

    public AdminBusinessController(AdminBusinessService svc) {
        this.svc = svc;
    }

    @GetMapping("/monthly")
    public ResponseEntity<?> monthly(@RequestParam int month, @RequestParam int year) {
        try {
            // Validation is also done inside service, but double-checking here
            if (month < 1 || month > 12) {
                return ResponseEntity.badRequest().body(Map.of("error", "Month must be 1–12"));
            }
            if (year < 2000 || year > 2100) {
                return ResponseEntity.badRequest().body(Map.of("error", "Year must be between 2000 and 2100"));
            }

            return ResponseEntity.ok(svc.calculateMonthlyBusiness(month, year));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Internal server error"));
        }
    }

    @GetMapping("/daily")
    public ResponseEntity<?> daily(@RequestParam String date) {
        LocalDate d;
        try {
            d = LocalDate.parse(date);
        } catch (Exception isoEx) {
            try {
                d = LocalDate.parse(date, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            } catch (Exception e) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Date must be yyyy-MM-dd or dd-MM-yyyy"));
            }
        }
        try {
            return ResponseEntity.ok(svc.calculateDailyBusiness(d));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Internal server error"));
        }
    }

    @GetMapping("/yearly")
    public ResponseEntity<?> yearly(@RequestParam int year) {
        try {
            if (year < 2000 || year > 2100) {
                return ResponseEntity.badRequest().body(Map.of("error", "Year must be between 2000 and 2100"));
            }
            return ResponseEntity.ok(svc.calculateYearlyBusiness(year));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Internal server error"));
        }
    }

    @GetMapping("/overall")
    public ResponseEntity<?> overall() {
        try {
            return ResponseEntity.ok(svc.calculateOverallBusiness());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Internal server error"));
        }
    }
}
