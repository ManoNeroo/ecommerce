package com.manonero.ecommerce.entities;

import javax.persistence.GenerationType;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name = "product_technical_data")
public class ProductTechnicalData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "technical_data_id")
    private int id;

    @Column(name = "technical_data_content")
    private String content;

    @JoinColumn(name = "product_id", referencedColumnName = "product_id", foreignKey = @ForeignKey(name = "product_technical_data_product_id_FK"))
    @ManyToOne(optional = false)
    private Product product;

    public ProductTechnicalData() {
    }

    public ProductTechnicalData(int id, String content, Product product) {
        this.id = id;
        this.content = content;
        this.product = product;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @JsonIgnore
    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

}
