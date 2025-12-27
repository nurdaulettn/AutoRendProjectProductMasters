package kz.nurdaulet.AutoRent.controller;

import jakarta.validation.Valid;
import kz.nurdaulet.AutoRent.dto.Request.BookingRequestDto;
import kz.nurdaulet.AutoRent.dto.Request.UpdateBookingStatusDto;
import kz.nurdaulet.AutoRent.dto.Response.BookingResponseDto;
import kz.nurdaulet.AutoRent.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public BookingResponseDto createBooking(@RequestBody BookingRequestDto request){
        return bookingService.createBooking(request);
    }

    @GetMapping("{id}")
    public BookingResponseDto getBooking(@PathVariable("id") Long id){
        return bookingService.getBookingById(id);
    }

    @PutMapping("{id}")
    @PreAuthorize("hasRole('OWNER')")
    public BookingResponseDto updateBookingStatus(@PathVariable Long id, @Valid @RequestBody UpdateBookingStatusDto request){
        return bookingService.updateBookingStatus(id, request);
    }

    @GetMapping("/my")
    public List<BookingResponseDto> getMyBookings(){
        return bookingService.getMyBookings();
    }

    @GetMapping("/my-cars")
    @PreAuthorize("hasRole('OWNER')")
    public List<BookingResponseDto> getBookingsForMyCars(){
        return bookingService.getBookingsForMyCars();
    }

    @PutMapping("/{id}/cancel")
    public BookingResponseDto cancelBooking(@PathVariable Long id){
        return bookingService.cancelBooking(id);
    }
}
