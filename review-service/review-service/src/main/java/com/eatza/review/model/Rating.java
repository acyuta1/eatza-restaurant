package com.eatza.review.model;

public enum Rating {

    PERFECT(5),
    GREAT(4),
    AVERAGE(3),
    OKAYISH(2),
    BAD(1);

    private int ratingValue;

    private Rating(int ratingValue) {
        this.ratingValue = ratingValue;
    }

    public int getRatingValue() {
        return this.ratingValue;
    }
}
