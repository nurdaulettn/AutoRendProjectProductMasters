package kz.nurdaulet.AutoRent.controller;

import jakarta.validation.Valid;
import kz.nurdaulet.AutoRent.dto.Request.ReviewRequestDto;
import kz.nurdaulet.AutoRent.dto.Response.ReviewResponseDto;
import kz.nurdaulet.AutoRent.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cars")
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping("/{carId}/reviews")
    public ReviewResponseDto reveiws(@PathVariable Long carId, @Valid @RequestBody ReviewRequestDto request) {
        return reviewService.createReview(carId, request);
    }

    @GetMapping("/{carId}/reviews")
    public List<ReviewResponseDto> getReviewsByCarId(@PathVariable Long carId){
        return reviewService.getReviewsByCarId(carId);
    }

    @GetMapping("/reviews/{id}")
    public ReviewResponseDto getReviewById(@PathVariable Long id){
        return reviewService.getReviewById(id);
    }

    @PutMapping("/reviews/{id}")
    public ReviewResponseDto updateReview(@PathVariable Long id, @Valid @RequestBody ReviewRequestDto request){
        return reviewService.updateReview(id, request);
    }

    @DeleteMapping("/reviews/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteReview(@PathVariable Long id){
        reviewService.deleteReview(id);
    }

    @GetMapping("/reviews/my")
    public List<ReviewResponseDto> getMyReviews(){
        return reviewService.getMyReviews();
    }
}
