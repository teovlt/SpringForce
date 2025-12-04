package fr.imt.springforce.vehicle.presentation.controller;

import fr.imt.springforce.vehicle.api.VehicleClient;
import fr.imt.springforce.vehicle.api.VehicleDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/vehicles")
@Tag(name = "Vehicle", description = "Vehicle API")
public class VehicleController {

    private final VehicleClient vehicleClient;

    @Operation(summary = "Get all vehicles")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found all vehicles")
    })
    @GetMapping("")
    public List<VehicleDetails> findAllVehicles() {
        return vehicleClient.findAll();
    }

    @Operation(summary = "Get a vehicle by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the vehicle"),
            @ApiResponse(responseCode = "404", description = "Vehicle not found")
    })
    @GetMapping("/{id}")
    public VehicleDetails findVehicleById(@PathVariable String id) {
        return vehicleClient.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Vehicle not found"));
    }

    @Operation(summary = "Create a new vehicle")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Vehicle created"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Vehicle creation failed")
    })
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public VehicleDetails createVehicle(@RequestBody VehicleDetails vehicle) {
        return vehicleClient.create(vehicle)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Vehicle creation failed"));
    }

    @Operation(summary = "Update a vehicle")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vehicle updated"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Vehicle not found")
    })
    @PutMapping("/{id}")
    public VehicleDetails updateVehicle(@RequestBody VehicleDetails vehicle, @PathVariable String id) {
        return vehicleClient.update(vehicle, id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Vehicle not found"));
    }

    @Operation(summary = "Delete a vehicle")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Vehicle deleted"),
            @ApiResponse(responseCode = "404", description = "Vehicle not found")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteVehicle(@PathVariable String id) {
        vehicleClient.delete(id);
    }

}
