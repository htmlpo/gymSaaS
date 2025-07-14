package gym.gym.Controllers;

import gym.gym.Models.Admin;
import gym.gym.Models.Member;
import gym.gym.Services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping
    public List<Admin> getAllAdmins() {
        return adminService.getAllAdmins();
    }

    @PostMapping
    public Admin saveAdmin(@RequestBody Admin admin) {

        return adminService.saveAdmin(admin);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMember(@PathVariable Long id) {
        adminService.deleteAdmin(id);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateAdmine (@PathVariable Long id, @RequestBody Admin adminData) {
        boolean updated = adminService.updateAdminById(id, adminData);

        if (updated) {
            return ResponseEntity.ok("Admin updated successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Admin not found.");
        }


    }

}
