package com.manonero.ecommerce.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "uv_product_star")
public class ProductStarView {
    @Id
    @Column(name = "product_id")
    private String id;

    @Column(name = "product_avg_star")
    private double avgStar;

    @Column(name = "product_number_vote")
    private int numberEvaluation;

    @Column(name = "one_star")
    private int oneStar;

    @Column(name = "two_stars")
    private int twoStars;

    @Column(name = "three_stars")
    private int threeStars;

    @Column(name = "four_stars")
    private int fourStars;

    @Column(name = "five_stars")
    private int fiveStars;

    public ProductStarView() {
    }

    public double getAvgStar() {
        return avgStar;
    }

    public void setAvgStar(double avgStar) {
        this.avgStar = avgStar;
    }

    public int getNumberEvaluation() {
        return numberEvaluation;
    }

    public void setNumberEvaluation(int numberEvaluation) {
        this.numberEvaluation = numberEvaluation;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getOneStar() {
        return oneStar;
    }

    public void setOneStar(int oneStar) {
        this.oneStar = oneStar;
    }

    public int getTwoStars() {
        return twoStars;
    }

    public void setTwoStars(int twoStars) {
        this.twoStars = twoStars;
    }

    public int getThreeStars() {
        return threeStars;
    }

    public void setThreeStars(int threeStars) {
        this.threeStars = threeStars;
    }

    public int getFourStars() {
        return fourStars;
    }

    public void setFourStars(int fourStars) {
        this.fourStars = fourStars;
    }

    public int getFiveStars() {
        return fiveStars;
    }

    public void setFiveStars(int fiveStars) {
        this.fiveStars = fiveStars;
    }

}
