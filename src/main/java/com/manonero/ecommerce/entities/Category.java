package com.manonero.ecommerce.entities;

import java.util.Date;

import javax.persistence.*;

@Entity
@Table(name = "category")
@NamedStoredProcedureQueries({
		@NamedStoredProcedureQuery(name = "Category.selectCategoryByOffsetLimit", procedureName = "usp_selectCategoryByOffsetLimit", resultClasses = Category.class, parameters = {
				@StoredProcedureParameter(name = "offset", type = Integer.class),
				@StoredProcedureParameter(name = "limit", type = Integer.class),
				@StoredProcedureParameter(name = "name", type = String.class),
				@StoredProcedureParameter(name = "status", type = Boolean.class) }),
		@NamedStoredProcedureQuery(name = "Category.selectCountCategory", procedureName = "usp_selectCountCategory", parameters = {
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "name", type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "status", type = Boolean.class),
				@StoredProcedureParameter(mode = ParameterMode.OUT, name = "count", type = Integer.class) }),
		@NamedStoredProcedureQuery(name = "Category.insertCategory", procedureName = "usp_insertCategory", parameters = {
				@StoredProcedureParameter(name = "name", type = String.class),
				@StoredProcedureParameter(name = "status", type = Boolean.class),
				@StoredProcedureParameter(name = "logo", type = String.class) }),
		@NamedStoredProcedureQuery(name = "Category.updateCategory", procedureName = "usp_updateCategory", parameters = {
				@StoredProcedureParameter(name = "id", type = Integer.class),
				@StoredProcedureParameter(name = "name", type = String.class),
				@StoredProcedureParameter(name = "status", type = Boolean.class),
				@StoredProcedureParameter(name = "logo", type = String.class) }),
		@NamedStoredProcedureQuery(name = "Category.toggleCategoryStatus", procedureName = "usp_toggleCategoryStatus", parameters = {
				@StoredProcedureParameter(name = "id", type = Integer.class),
				@StoredProcedureParameter(name = "status", type = Boolean.class) }) })
public class Category {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "category_id")
	private int id;

	@Column(name = "category_name")
	private String name;

	@Column(name = "category_logo")
	private String logo;

	@Column(name = "category_status")
	private boolean status;

	@Column(name = "created_at")
	private Date createdAt;

	@Column(name = "updated_at")
	private Date updatedAt;

	// @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	// @JoinTable(name = "brand_category", joinColumns = @JoinColumn(name =
	// "category_id"), inverseJoinColumns = @JoinColumn(name = "brand_id"))
	// private Collection<Brand> brands;

	public Category() {

	}

	public Category(int id, String name, String logo, boolean status, Date createdAt, Date updatedAt) {
		this.id = id;
		this.name = name;
		this.logo = logo;
		this.status = status;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	public int getId() {
		return id;
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

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	// @JsonIgnore
	// public Collection<Brand> getBrands() {
	// return brands;
	// }

	// public void setBrands(Collection<Brand> brands) {
	// this.brands = brands;
	// }

}
