package projekt.wypozyczalnia.controllers;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projekt.wypozyczalnia.data.dtos.AddressDTO;
import lombok.RequiredArgsConstructor;
import projekt.wypozyczalnia.services.AddressService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/address")
public class AddressController {

    private final AddressService addressService;

    @GetMapping
    public ResponseEntity<List<AddressDTO>> getClientAddresses() {
        try {
            return new ResponseEntity<>(addressService.getClientAddresses(), HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/add")
    public ResponseEntity<String> addAddress(
            @RequestBody AddressDTO addressDTO
    ) {
        try {
            addressService.addAddress(addressDTO);
            return new ResponseEntity<>("Address added successfully", HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>("Error adding address", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/setDefault")
    public ResponseEntity<String> setDefaultAddress(
            @RequestParam int addressId
    ) {
        try {
            addressService.setDefaultAddress(addressId);
            return new ResponseEntity<>("Default address set successfully", HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>("Error setting default address", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
