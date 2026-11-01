package com.example.ecommerce.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ReviewRequest {

    @NotNull(message = "Rating is required")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must be at most 5")
    private Integer rating;

    @Size(max = 1000, message = "Comment must be at most 1000 characters")
    private String comment;

    public ReviewRequest() {}

    public ReviewRequest(Integer rating, String comment) {
        this.rating = rating;
        this.comment = comment;
    }

    public Integer getRating() { return rating; }
    public void setRating(Integer rating) { this.rating = rating; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public static ReviewRequestBuilder builder() {
        return new ReviewRequestBuilder();
    }

    public static class ReviewRequestBuilder {
        private Integer rating;
        private String comment;

        public ReviewRequestBuilder rating(Integer rating) { this.rating = rating; return this; }
        public ReviewRequestBuilder comment(String comment) { this.comment = comment; return this; }

        public ReviewRequest build() {
            return new ReviewRequest(rating, comment);
        }
    }
}
