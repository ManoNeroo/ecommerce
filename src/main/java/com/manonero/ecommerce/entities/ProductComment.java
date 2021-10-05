package com.manonero.ecommerce.entities;

import java.util.Collection;
import java.util.Date;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "product_comment")
@NamedStoredProcedureQueries({
        @NamedStoredProcedureQuery(name = "ProductComment.insertProductComment", procedureName = "usp_insertProductComment", parameters = {
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "productId", type = String.class),
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "userId", type = Integer.class),
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "content", type = String.class),
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "parentId", type = Integer.class),
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "createdAt", type = Date.class),
                @StoredProcedureParameter(mode = ParameterMode.OUT, name = "id", type = Integer.class) }),
        @NamedStoredProcedureQuery(name = "ProductComment.selectProductComment", procedureName = "usp_selectProductComment", resultClasses = ProductComment.class, parameters = {
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "productId", type = String.class),
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "userId", type = Integer.class),
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "isOfUser", type = Boolean.class) }),
        @NamedStoredProcedureQuery(name = "ProductComment.selectProductCommentByParentId", procedureName = "usp_selectProductCommentByParentId", resultClasses = ProductComment.class, parameters = {
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "parentId", type = Integer.class) }),
        @NamedStoredProcedureQuery(name = "ProductComment.updateProductComment", procedureName = "usp_updateProductComment", parameters = {
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "id", type = Integer.class),
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "content", type = String.class) }),
        @NamedStoredProcedureQuery(name = "ProductComment.deleteProductComment", procedureName = "usp_deleteProductComment", parameters = {
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "id", type = Integer.class) }) })
public class ProductComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private int id;

    @Column(name = "product_id")
    private String productId;

    @JoinColumn(name = "user_id", referencedColumnName = "user_id", foreignKey = @ForeignKey(name = "product_comment_user_id_FK"))
    @ManyToOne(optional = false)
    private UserAccount userAccount;

    @Column(name = "comment_content")
    private String content;

    @Column(name = "comment_parent_id")
    private int parentId;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Transient
    private Collection<ProductComment> children;

    public ProductComment() {
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

    public Collection<ProductComment> getChildren() {
        return children;
    }

    public void setChildren(Collection<ProductComment> children) {
        this.children = children;
    }

}
