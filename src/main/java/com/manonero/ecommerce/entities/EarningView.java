package com.manonero.ecommerce.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "uv_month_earning")
public class EarningView implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "month")
    private int month;

    @Id
    @Column(name = "year")
    private int year;

    @Column(name = "product_sold")
    private int productSold;

    @Column(name = "earning")
    private int monthEarning;

    @Column(name = "shipped")
    private int orderShipped;

    @Column(name = "un_shipped")
    private int orderUnShipped;

    @Column(name = "canceled")
    private int orderCanceled;

    public EarningView() {
    }

    public EarningView(int month, int year, int productSold, int monthEarning, int orderShipped,
            int orderUnShipped, int orderCanceled) {
        this.month = month;
        this.year = year;
        this.productSold = productSold;
        this.monthEarning = monthEarning;
        this.orderShipped = orderShipped;
        this.orderUnShipped = orderUnShipped;
        this.orderCanceled = orderCanceled;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getProductSold() {
        return productSold;
    }

    public void setProductSold(int productSold) {
        this.productSold = productSold;
    }

    public int getMonthEarning() {
        return monthEarning;
    }

    public void setMonthEarning(int monthEarning) {
        this.monthEarning = monthEarning;
    }

    public int getOrderShipped() {
        return orderShipped;
    }

    public void setOrderShipped(int orderShipped) {
        this.orderShipped = orderShipped;
    }

    public int getOrderUnShipped() {
        return orderUnShipped;
    }

    public void setOrderUnShipped(int orderUnShipped) {
        this.orderUnShipped = orderUnShipped;
    }

    public int getOrderCanceled() {
        return orderCanceled;
    }

    public void setOrderCanceled(int orderCanceled) {
        this.orderCanceled = orderCanceled;
    }

}
