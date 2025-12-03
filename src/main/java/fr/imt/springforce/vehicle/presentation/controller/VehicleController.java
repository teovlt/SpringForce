package fr.imt.springforce.vehicle.presentation.controller;

import fr.imt.springforce.vehicle.api.VehicleClient;
import fr.imt.springforce.vehicle.api.VehicleDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/vehicles")
public class VehicleController {

    private final VehicleClient vehicleClient;

    @GetMapping("")
    public List<VehicleDetails> findAllVehicles() {
        return vehicleClient.findAll();
    }

    @GetMapping("/{id}")
    public VehicleDetails findVehicleById(@PathVariable String id) {
        return vehicleClient.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Vehicle not found"));
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public VehicleDetails createVehicle(@RequestBody VehicleDetails vehicle) {
        return vehicleClient.create(vehicle)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Vehicle creation failed"));
    }

    @PutMapping("/{id}")
    public VehicleDetails updateVehicle(@RequestBody VehicleDetails vehicle, @PathVariable String id) {
        return vehicleClient.update(vehicle, id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Vehicle not found"));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteVehicle(@PathVariable String id) {
        vehicleClient.delete(id);
    }

}
