package com.manonero.ecommerce.entities;

import java.util.Collection;
import java.util.Date;

import javax.persistence.*;

@Entity
@Table(name = "brand")
@NamedStoredProcedureQueries({
		@NamedStoredProcedureQuery(name = "Brand.selectBrandByOffsetLimit", procedureName = "usp_selectBrandByOffsetLimit", resultClasses = Brand.class, parameters = {
				@StoredProcedureParameter(name = "offset", type = Integer.class),
				@StoredProcedureParameter(name = "limit", type = Integer.class),
				@StoredProcedureParameter(name = "name", type = String.class),
				@StoredProcedureParameter(name = "status", type = Boolean.class) }),
		@NamedStoredProcedureQuery(name = "Brand.selectCountBrand", procedureName = "usp_selectCountBrand", parameters = {
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "name", type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "status", type = Boolean.class),
				@StoredProcedureParameter(mode = ParameterMode.OUT, name = "count", type = Integer.class) }),
		@NamedStoredProcedureQuery(name = "Brand.updateBrand", procedureName = "usp_updateBrand", parameters = {
				@StoredProcedureParameter(name = "id", type = Integer.class),
				@StoredProcedureParameter(name = "name", type = String.class),
				@StoredProcedureParameter(name = "status", type = Boolean.class),
				@StoredProcedureParameter(name = "logo", type = String.class) }),
		@NamedStoredProcedureQuery(name = "Brand.toggleBrandStatus", procedureName = "usp_toggleBrandStatus", parameters = {
				@StoredProcedureParameter(name = "id", type = Integer.class),
				@StoredProcedureParameter(name = "status", type = Boolean.class) }) })
public class Brand {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "brand_id")
	private int id;

	@Column(name = "brand_name")
	private String name;

	@Column(name = "brand_logo")
	private String logo;

	@Column(name = "brand_status")
	private boolean status;

	@Column(name = "created_at")
	private Date createdAt;

	@Column(name = "updated_at")
	private Date updatedAt;

	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "brand_category", joinColumns = @JoinColumn(name = "brand_id"), inverseJoinColumns = @JoinColumn(name = "category_id"))
	private Collection<Category> categories;

	public Brand() {
	}

	public Brand(int id, String name, String logo, boolean status, Date createdAt, Date updatedAt,
			Collection<Category> categories) {
		this.id = id;
		this.name = name;
		this.logo = logo;
		this.status = status;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.categories = categories;
	}

	public int getId() {
		return id;
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

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public Collection<Category> getCategories() {
		return categories;
	}

	public void setCategories(Collection<Category> categories) {
		this.categories = categories;
	}

}
