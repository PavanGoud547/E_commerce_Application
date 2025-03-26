package Pavan.com.E_commerce_Application.Service;

import Pavan.com.E_commerce_Application.model.Review;
import Pavan.com.E_commerce_Application.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private OrderService orderService;

    @Transactional(readOnly = true)
    public Page<Review> getReviewsByProduct(Long productId, Pageable pageable) {
        return reviewRepository.findByProductId(productId, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Review> getReviewsByUser(Long userId, Pageable pageable) {
        return reviewRepository.findByUserId(userId, pageable);
    }

    @Transactional
    public Review createReview(Long userId, Long productId, Review review) {
        // Check if user has already reviewed this product
        if (reviewRepository.existsByUserIdAndProductId(userId, productId)) {
            throw new RuntimeException("User has already reviewed this product");
        }

        // Optional: Check if user has purchased the product
        // This is commented out as it might be too restrictive
        /*
        if (!orderService.hasUserPurchasedProduct(userId, productId)) {
            throw new RuntimeException("User must purchase the product before reviewing");
        }
        */

//        review.setUserId(userId);
//        review.setProductId(productId);
        return reviewRepository.save(review);
    }

    @Transactional
    public Review updateReview(Long reviewId, Review reviewDetails) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        review.setRating(reviewDetails.getRating());
        review.setComment(reviewDetails.getComment());
        review.setImages(reviewDetails.getImages());

        return reviewRepository.save(review);
    }

    @Transactional
    public void deleteReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));
        reviewRepository.delete(review);
    }

    @Transactional(readOnly = true)
    public Double getAverageRating(Long productId) {
        return reviewRepository.calculateAverageRating(productId);
    }
} 