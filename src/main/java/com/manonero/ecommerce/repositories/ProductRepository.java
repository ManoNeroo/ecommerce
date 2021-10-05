package com.manonero.ecommerce.repositories;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;

import com.manonero.ecommerce.configs.AppSettings;
import com.manonero.ecommerce.entities.Product;
import com.manonero.ecommerce.utils.AppUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ProductRepository implements IProductRepository {

    @Autowired
    private AppSettings appSettings;

    @Autowired
    private EntityManager entityManager;

    public List<Product> selectFilterProduct(int[] categoryIds, int[] brandIds, int[] priceRangeIxs, String name,
            Boolean status, Boolean isSort, Boolean onlyEnable) {
        String mainQueryStr = "SELECT * FROM product p";
        String whereQueryString = "";
        if (onlyEnable != null) {
            if (onlyEnable) {
                mainQueryStr += ", brand b, category c";
                whereQueryString += " WHERE p.product_status=1 AND b.brand_status=1 AND c.category_status=1 AND p.category_id=c.category_id AND p.brand_id=b.brand_id";
            } else {
                whereQueryString += " WHERE p.product_status=0";
            }
        }
        if (categoryIds != null) {
            if (whereQueryString.equals("")) {
                for (int i = 0; i < categoryIds.length; i++) {
                    if (i == 0) {
                        whereQueryString += " WHERE (p.category_id=" + categoryIds[i];
                    } else {
                        whereQueryString += " OR p.category_id=" + categoryIds[i];
                    }
                }
            } else {
                for (int i = 0; i < categoryIds.length; i++) {
                    if (i == 0) {
                        whereQueryString += " AND (p.category_id=" + categoryIds[i];
                    } else {
                        whereQueryString += " OR p.category_id=" + categoryIds[i];
                    }
                }
            }
            whereQueryString += ")";
        }
        if (brandIds != null) {
            if (whereQueryString.equals("")) {
                for (int i = 0; i < brandIds.length; i++) {
                    if (i == 0) {
                        whereQueryString += " WHERE (p.brand_id=" + brandIds[i];
                    } else {
                        whereQueryString += " OR p.brand_id=" + brandIds[i];
                    }
                }
            } else {
                for (int i = 0; i < brandIds.length; i++) {
                    if (i == 0) {
                        whereQueryString += " AND (p.brand_id=" + brandIds[i];
                    } else {
                        whereQueryString += " OR p.brand_id=" + brandIds[i];
                    }
                }
            }
            whereQueryString += ")";
        }
        if (priceRangeIxs != null) {
            if (whereQueryString.equals("")) {
                for (int i = 0; i < priceRangeIxs.length; i++) {
                    if (i == 0) {
                        whereQueryString += " WHERE ((p.product_promo_price >= "
                                + appSettings.priceRangeArr[priceRangeIxs[i]][0] + " AND p.product_promo_price < "
                                + appSettings.priceRangeArr[priceRangeIxs[i]][1] + ")";
                    } else {
                        whereQueryString += " OR (p.product_promo_price >= "
                                + appSettings.priceRangeArr[priceRangeIxs[i]][0] + " AND p.product_promo_price < "
                                + appSettings.priceRangeArr[priceRangeIxs[i]][1] + ")";
                    }
                }
            } else {
                for (int i = 0; i < priceRangeIxs.length; i++) {
                    if (i == 0) {
                        whereQueryString += " AND ((p.product_promo_price >= "
                                + appSettings.priceRangeArr[priceRangeIxs[i]][0] + " AND p.product_promo_price < "
                                + appSettings.priceRangeArr[priceRangeIxs[i]][1] + ")";
                    } else {
                        whereQueryString += " OR (p.product_promo_price >= "
                                + appSettings.priceRangeArr[priceRangeIxs[i]][0] + " AND p.product_promo_price < "
                                + appSettings.priceRangeArr[priceRangeIxs[i]][1] + ")";
                    }
                }
            }
            whereQueryString += ")";
        }
        if (name != null) {
            if (whereQueryString.equals("")) {
                whereQueryString += " WHERE [dbo].[uf_nonUnicode](p.product_name) LIKE '%" + AppUtils.removeAccent(name)
                        + "%'";
            } else {
                whereQueryString += " AND [dbo].[uf_nonUnicode](p.product_name) LIKE '%" + AppUtils.removeAccent(name)
                        + "%'";
            }
        }
        if (status != null && onlyEnable == null) {
            if (whereQueryString.equals("")) {
                whereQueryString += " WHERE p.product_status = " + (status ? 1 : 0);
            } else {
                whereQueryString += " AND p.product_status = " + (status ? 1 : 0);
            }
        }
        mainQueryStr += whereQueryString;
        if (isSort != null) {
            if (isSort) {
                mainQueryStr += " ORDER BY p.product_promo_price ASC";
            } else {
                mainQueryStr += " ORDER BY p.product_promo_price DESC";
            }
        } else {
            mainQueryStr += " ORDER BY p.product_name ASC";
        }

        Query query = entityManager.createNativeQuery(mainQueryStr, Product.class);
        @SuppressWarnings("unchecked")
        List<Product> products = query.getResultList();
        return products;
    }

    @Override
    public Product save(Product product) {
        Product p = entityManager.merge(product);
        return p;
    }

    @Override
    public Product selectProductById(String id) {
        Product p = entityManager.find(Product.class, id);
        return p;
    }

    @Override
    public void updateProductStatus(Boolean status, String productId) {
        int sInt = 0;
        if (status != null) {
            if (status) {
                sInt = 1;
            }
        }
        String sqlStr = "UPDATE product SET product_status=? WHERE product_id=?";
        Query query = entityManager.createNativeQuery(sqlStr);
        query.setParameter(1, sInt);
        query.setParameter(2, productId);
        query.executeUpdate();
    }

    @Override
    public void updateProductAvatar(String avatar, String productId) {
        if (avatar != null) {
            String sqlStr = "UPDATE product SET product_avatar=? WHERE product_id=?";
            Query query = entityManager.createNativeQuery(sqlStr);
            query.setParameter(1, avatar);
            query.setParameter(2, productId);
            query.executeUpdate();
        }
    }

    @Override
    public List<Product> selectTopProduct(int top, int categoryId) {
        String sqlString = "SELECT TOP " + top + " * FROM product WHERE category_id=?";
        Query query = entityManager.createNativeQuery(sqlString, Product.class);
        query.setParameter(1, categoryId);
        @SuppressWarnings("unchecked")
        List<Product> products = query.getResultList();
        return products;
    }

    @Override
    public List<Product> selectTopSale(int top) {
        StoredProcedureQuery query = entityManager.createNamedStoredProcedureQuery("Product.selectTopProductSale");
        query.setParameter("top", top);
        @SuppressWarnings("unchecked")
        List<Product> products = query.getResultList();
        return products;
    }

    @Override
    public List<Product> selectTopByName(int top, String name, Boolean status) {
        StoredProcedureQuery query = entityManager.createNamedStoredProcedureQuery("Product.selectTopProductByName");
        query.setParameter("top", top);
        query.setParameter("name", name);
        query.setParameter("status", status);
        @SuppressWarnings("unchecked")
        List<Product> products = query.getResultList();
        return products;
    }
}
