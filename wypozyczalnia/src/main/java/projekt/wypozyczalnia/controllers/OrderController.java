package projekt.wypozyczalnia.controllers;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import projekt.wypozyczalnia.data.dtos.OrderDTO;
import projekt.wypozyczalnia.exceptions.ConflictReservationException;
import projekt.wypozyczalnia.requests.OrderDetailRequest;
import projekt.wypozyczalnia.requests.OrderStatusChangeRequest;
import projekt.wypozyczalnia.services.OrderService;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/orders")
public class OrderController {

    private final OrderService orderService;

    @PreAuthorize("hasAnyAuthority('Client')")
    @GetMapping()
    public List<OrderDTO>getClientOrders(){
        return orderService.getClientOrders();
    }

    @PreAuthorize("hasAnyAuthority('Manager','Online Sales Representative','Customer Support')")
    @GetMapping("/all")
    public List<OrderDTO>getAllClientOrders(){
        return orderService.getAllClientOrders();
    }

    @PreAuthorize("hasAnyAuthority('Client')")
    @PostMapping
    public ResponseEntity<String> makeOrder(
            @RequestParam(name = "rentalDateStart")  @DateTimeFormat(pattern = "YYYY-MM-dd") LocalDate rentalDateStart,
            @RequestParam(name = "rentalDateEnd") @DateTimeFormat(pattern = "YYYY-MM-dd") LocalDate rentalDateEnd
    ) {

            if (rentalDateStart == null || rentalDateEnd == null) {
                return new ResponseEntity<>("Invalid date parameters", HttpStatus.BAD_REQUEST);
            }

        try {
            orderService.makeOrder(rentalDateStart, rentalDateEnd);
            return new ResponseEntity<>("Order placed successfully", HttpStatus.OK);
        } catch (EntityNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasAnyAuthority('Manager','Online Sales Representative','Customer Support', 'Client')")
    @GetMapping("/calculateProductsCost")
    public Float calculateProductsCost(){
        return orderService.calculateProductsCost();
    }

    @PreAuthorize("hasAnyAuthority('Manager','Online Sales Representative','Customer Support', 'Client')")
    @PostMapping("/changeOrderStatus")
    public ResponseEntity<String> changeOrderStatus(
            @RequestBody OrderStatusChangeRequest request) {
        try {
            orderService.changeOrderStatus(request);
            return new ResponseEntity<>("Order status changed successfully", HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>("Order not found", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/details")
    public OrderDetailRequest getOrderDetails(
            @RequestParam(name = "orderId") int orderId) {
        return orderService.getOrderDetails(orderId);
    }

    @PreAuthorize("hasAnyAuthority('Client')")
    @GetMapping("/invoice")
    public ResponseEntity<String> generateInvoice(
            @RequestParam(name = "orderId") int orderId) {
        try {
            orderService.generateInvoice(orderId);
            return new ResponseEntity<>("Invoice generated successfully", HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>("Order not found", HttpStatus.NOT_FOUND);
        }
    }

}
