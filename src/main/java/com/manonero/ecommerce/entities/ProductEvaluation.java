package com.manonero.ecommerce.entities;

import java.util.Collection;
import java.util.Date;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "product_evaluation")
@NamedStoredProcedureQueries({
        @NamedStoredProcedureQuery(name = "ProductEvaluation.insertProductEvaluation", procedureName = "usp_insertProductEvaluation", parameters = {
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "productId", type = String.class),
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "userId", type = Integer.class),
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "content", type = String.class),
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "star", type = Integer.class),
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "parentId", type = Integer.class),
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "createdAt", type = Date.class),
                @StoredProcedureParameter(mode = ParameterMode.OUT, name = "id", type = Integer.class) }),
        @NamedStoredProcedureQuery(name = "ProductEvaluation.selectProductEvaluation", procedureName = "usp_selectProductEvaluation", resultClasses = ProductEvaluation.class, parameters = {
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "productId", type = String.class),
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "userId", type = Integer.class),
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "isOfUser", type = Boolean.class) }),
        @NamedStoredProcedureQuery(name = "ProductEvaluation.selectProductEvaluationByParentId", procedureName = "usp_selectProductEvaluationByParentId", resultClasses = ProductEvaluation.class, parameters = {
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "parentId", type = Integer.class) }),
        @NamedStoredProcedureQuery(name = "ProductEvaluation.updateProductEvaluation", procedureName = "usp_updateProductEvaluation", parameters = {
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "id", type = Integer.class),
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "content", type = String.class),
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "star", type = Integer.class) }),
        @NamedStoredProcedureQuery(name = "ProductEvaluation.deleteProductEvaluation", procedureName = "usp_deleteProductEvaluation", parameters = {
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "id", type = Integer.class) }) })
public class ProductEvaluation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "evaluation_id")
    private int id;

    @Column(name = "product_id")
    private String productId;

    @JoinColumn(name = "user_id", referencedColumnName = "user_id", foreignKey = @ForeignKey(name = "product_evaluation_user_id_FK"))
    @ManyToOne(optional = false)
    private UserAccount userAccount;

    @Column(name = "evaluation_content")
    private String content;

    @Column(name = "evaluation_parent_id")
    private int parentId;

    @Column(name = "evaluation_star")
    private int star;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Transient
    private Collection<ProductEvaluation> children;

    public ProductEvaluation() {
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    @JsonIgnoreProperties(value = { "password", "phoneNumber", "gender", "status", "roles", "firstName", "lastName",
            "notificationTopics" })
    public UserAccount getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(UserAccount userAccount) {
        this.userAccount = userAccount;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
    }

    public Collection<ProductEvaluation> getChildren() {
        return children;
    }

    public void setChildren(Collection<ProductEvaluation> children) {
        this.children = children;
    }

}
