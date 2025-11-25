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
    public List<Vehicle> findAll() {
        return vehicleService.findAll();
    }

    @PostMapping("/")
    public Vehicle createVehicle(@RequestBody Vehicle vehicle) {
        return vehicleService.create(vehicle);
    }

}
