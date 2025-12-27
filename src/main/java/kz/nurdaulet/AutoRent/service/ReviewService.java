package kz.nurdaulet.AutoRent.service;

import kz.nurdaulet.AutoRent.dto.Request.ReviewRequestDto;
import kz.nurdaulet.AutoRent.dto.Response.ReviewResponseDto;
import kz.nurdaulet.AutoRent.model.*;
import kz.nurdaulet.AutoRent.repository.BookingRepository;
import kz.nurdaulet.AutoRent.repository.CarRepository;
import kz.nurdaulet.AutoRent.repository.ReviewRepository;
import kz.nurdaulet.AutoRent.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {
    public final ReviewRepository reviewRepository;
    public final CarRepository carRepository;
    public final UserRepository userRepository;
    public final BookingRepository bookingRepository;

    public List<ReviewResponseDto> getReviewsByCarId(Long carId){
        Car car = carRepository.findById(carId).orElseThrow(() -> new RuntimeException("Car not found"));
        List<Review> reviews = reviewRepository.findByCarId(carId);
        return reviews.stream().map(ReviewResponseDto::new).collect(Collectors.toList());
    }

    public ReviewResponseDto createReview(ReviewRequestDto request){
        User user = getCurrentUser();
        Car car = carRepository.findById(request.getCarId()).orElseThrow(() -> new RuntimeException("Car not found"));

        List<Booking> bookings = bookingRepository.findByClientIdAndStatus(user.getId(), BookingStatus.COMPLETED);
        boolean hasRented = false;

        for(Booking booking : bookings){
            if(booking.getCar().getId().equals(car.getId())){
                hasRented = true;
            }
        }
        if(!hasRented){
            throw new RuntimeException("You can't create a review for a car that doesn't have a rented");
        }

        boolean hasReview = reviewRepository.existsByClientIdAndCarId(user.getId(), car.getId());
        if(hasReview){
            throw new RuntimeException("You have review for this car");
        }

        Review review = new Review();
        review.setRating(request.getRating());
        review.setComment(request.getComment());
        review.setCar(car);
        review.setClient(user);
        review.setCreatedAt(LocalDateTime.now());

        Review savedReview = reviewRepository.save(review);
        return new ReviewResponseDto(savedReview);
    }

    public ReviewResponseDto getReviewById(Long id){
        Review review = reviewRepository.findById(id).orElseThrow(() -> new RuntimeException("Review not found"));
        return new ReviewResponseDto(review);
    }

    public void deleteReview(Long id){
        User user = getCurrentUser();
        Review review = reviewRepository.findById(id).orElseThrow(() -> new RuntimeException("Review not found"));
        if(!review.getClient().getId().equals(user.getId())){
            throw new RuntimeException("You can't delete a review that written by another user");
        }
        reviewRepository.delete(review);
    }

    public ReviewResponseDto updateReview(Long id, ReviewRequestDto request){
        User client = getCurrentUser();
        Review review = reviewRepository.findById(id).orElseThrow(() -> new RuntimeException("Review not found"));
        if(!review.getClient().getId().equals(client.getId())){
            throw new RuntimeException("You can't update a review that written by another user");
        }
        review.setRating(request.getRating());
        review.setComment(request.getComment());

        Review savedReview = reviewRepository.save(review);
        return new ReviewResponseDto(savedReview);
    }

    public List<ReviewResponseDto> getMyReviews(){
        User user = getCurrentUser();
        List<Review> reviews = reviewRepository.findByClientId(user.getId());
        return reviews.stream().map(ReviewResponseDto::new).collect(Collectors.toList());
    }

    public User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        return userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User Not Found"));
    }
}
