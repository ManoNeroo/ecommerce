package com.manonero.ecommerce.repositories;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;

import com.manonero.ecommerce.entities.Brand;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class BrandRepository implements IBrandRepository {
  @Autowired
  private EntityManager entityManager;

  @SuppressWarnings("unchecked")
  @Override
  public List<Brand> selectBrandByOffsetLimit(int offset, int limit, String name, Boolean status) {
    StoredProcedureQuery query = entityManager.createNamedStoredProcedureQuery("Brand.selectBrandByOffsetLimit");
    query.setParameter("offset", offset);
    query.setParameter("limit", limit);
    query.setParameter("name", name);
    query.setParameter("status", status);
    return query.getResultList();
  }

  @Override
  public int selectBrandCount(String name, Boolean status) {
    StoredProcedureQuery query = entityManager.createNamedStoredProcedureQuery("Brand.selectCountBrand");
    query.setParameter("name", name);
    query.setParameter("status", status);
    query.execute();
    int count = (Integer) query.getOutputParameterValue("count");
    return count;
  }

  @Override
  public Brand save(Brand brand) {
    Brand b = entityManager.merge(brand);
    return b;
  }

  @Override
  public boolean updateBrandStatus(int id, boolean status) {
    StoredProcedureQuery query = entityManager.createNamedStoredProcedureQuery("Brand.toggleBrandStatus");
    query.setParameter("id", id);
    query.setParameter("status", status);
    int rs = query.executeUpdate();
    if (rs > 0) {
      return true;
    }
    return false;
  }

  @Override
  public Brand selectBrandById(int id) {
    Brand brand = entityManager.find(Brand.class, id);
    return brand;
  }

  @Override
  public List<Brand> selectAllBrand(Boolean isEnable) {
    String queryStr = "SELECT * FROM brand";
    if (isEnable != null) {
      if (isEnable) {
        queryStr += " WHERE brand_status=1";
      } else {
        queryStr += " WHERE brand_status=0";
      }
    }
    queryStr += " ORDER BY brand_name ASC";
    Query query = entityManager.createNativeQuery(queryStr, Brand.class);

    @SuppressWarnings("unchecked")
    List<Brand> brands = query.getResultList();
    return brands;
  }

}
