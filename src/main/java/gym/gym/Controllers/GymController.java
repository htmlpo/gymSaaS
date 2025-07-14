package gym.gym.Controllers;

import gym.gym.Models.Gym;

import gym.gym.Services.GymService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/gym")
public class GymController {


   @Autowired
    private GymService gymService;

    @GetMapping
    public List<Gym> getAllGyms() {
        return gymService.getAllGyms();
    }

    @PostMapping
    public Gym createGym(@RequestBody Gym gym) {
        return gymService.saveGym(gym);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Gym> getGymsById(@PathVariable Long id) {
        Optional<Gym> payment = gymService.getGymById(id);
        return payment.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePayment(@PathVariable Long id) {
        if (gymService.getGymById(id).isPresent()) {
            gymService.deleteGym(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }



}
