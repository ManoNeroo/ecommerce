package com.manonero.ecommerce.entities;

import java.util.Date;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "product")
@NamedStoredProcedureQueries({
        @NamedStoredProcedureQuery(name = "Product.selectTopProductSale", procedureName = "usp_selectTopProductSale", resultClasses = Product.class, parameters = {
                @StoredProcedureParameter(name = "top", type = Integer.class) }),
        @NamedStoredProcedureQuery(name = "Product.selectTopProductByName", procedureName = "usp_selectTopProductByName", resultClasses = Product.class, parameters = {
                @StoredProcedureParameter(name = "top", type = Integer.class),
                @StoredProcedureParameter(name = "name", type = String.class),
                @StoredProcedureParameter(name = "status", type = Boolean.class) }) })
public class Product {
    @Id
    @Column(name = "product_id")
    private String id;

    @Column(name = "product_name")
    private String name;

    @Column(name = "product_quanlity")
    private int quanlity;

    @Column(name = "product_avg_star")
    private double avgStar;

    @Column(name = "product_price")
    private int price;

    @Column(name = "product_price_off")
    private int priceOff;

    @Column(name = "product_promo_price")
    private int promoPrice;

    @Column(name = "product_avatar")
    private String avatar;

    @Column(name = "product_status")
    private boolean status;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

    @Column(name = "product_number_vote")
    private int numberVote;

    @JoinColumn(name = "brand_id", referencedColumnName = "brand_id", foreignKey = @ForeignKey(name = "product_brand_id_FK"))
    @ManyToOne(optional = false)
    private Brand brand;

    @JoinColumn(name = "category_id", referencedColumnName = "category_id", foreignKey = @ForeignKey(name = "product_category_id_FK"))
    @ManyToOne(optional = false)
    private Category category;

    public Product() {
    }

    public Product(String id, String name, int quanlity, double avgStar, int price, int priceOff, int promoPrice,
            String avatar, boolean status, Date createdAt, Date updatedAt, int numberVote, Brand brand,
            Category category) {
        this.id = id;
        this.name = name;
        this.quanlity = quanlity;
        this.avgStar = avgStar;
        this.price = price;
        this.priceOff = priceOff;
        this.promoPrice = promoPrice;
        this.avatar = avatar;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.numberVote = numberVote;
        this.brand = brand;
        this.category = category;
    }

    public int getNumberVote() {
        return numberVote;
    }

    public void setNumberVote(int numberVote) {
        this.numberVote = numberVote;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuanlity() {
        return quanlity;
    }

    public void setQuanlity(int quanlity) {
        this.quanlity = quanlity;
    }

    public double getAvgStar() {
        return avgStar;
    }

    public void setAvgStar(double avgStar) {
        this.avgStar = avgStar;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getPriceOff() {
        return priceOff;
    }

    public void setPriceOff(int priceOff) {
        this.priceOff = priceOff;
    }

    public int getPromoPrice() {
        return promoPrice;
    }

    public void setPromoPrice(int promoPrice) {
        this.promoPrice = promoPrice;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    @JsonIgnoreProperties(value = { "name", "logo", "status", "createdAt", "updatedAt", "categories" })
    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    @JsonIgnoreProperties(value = { "name", "logo", "status", "createdAt", "updatedAt" })
    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

}
