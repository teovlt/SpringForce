package fr.imt.springForce.vehicle.presentation.controller;

import fr.imt.springForce.vehicle.business.model.Vehicle;
import fr.imt.springForce.vehicle.business.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/vehicles")
public class VehicleController {

    private final VehicleService vehicleService;

    @GetMapping("/")
    public List<Vehicle> findAllVehicles() {
        return vehicleService.findAll();
    }

    @GetMapping("/:id")
    public Vehicle findVehicleById(@PathVariable String vehicleId) {
        return vehicleService.findById(vehicleId);
    }

    @PostMapping("/")
    public Vehicle createVehicle(@RequestBody Vehicle vehicle) {
        return vehicleService.create(vehicle);
    }

    @PutMapping("/:id")
    public Vehicle updateVehicle(@RequestBody Vehicle vehicle, @PathVariable String vehicleId) {
        return vehicleService.update(vehicle, vehicleId);
    }

    @DeleteMapping("/:id")
    public void deleteVehicle(@PathVariable String vehicleId) {
         vehicleService.delete(vehicleId);
    }

}
