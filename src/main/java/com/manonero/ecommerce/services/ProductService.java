package com.manonero.ecommerce.services;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.transaction.Transactional;

import com.manonero.ecommerce.configs.AppSettings;
import com.manonero.ecommerce.entities.Brand;
import com.manonero.ecommerce.entities.Category;
import com.manonero.ecommerce.entities.Product;
import com.manonero.ecommerce.models.ProductRequest;
import com.manonero.ecommerce.models.UpdateProductStatusRequest;
import com.manonero.ecommerce.repositories.IBrandRepository;
import com.manonero.ecommerce.repositories.ICategoryRepository;
import com.manonero.ecommerce.repositories.IProductRepository;
import com.manonero.ecommerce.utils.AppUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductService implements IProductService {
    @Autowired
    private IProductRepository productRepository;
    @Autowired
    private AppSettings appSettings;
    private int productCount;
    @Autowired
	private IBrandRepository brandRepository;
    @Autowired
	private ICategoryRepository categoryRepository;

    @Override
    @Transactional
    public List<Product> filterProduct(Integer offset, Integer limit, int[] categoryIds, int[] brandIds,
            int[] priceRangeIxs, String name, Boolean status, Boolean isSort, Boolean onlyEnable) {
        int lastIx = appSettings.priceRangeArr.length - 1;
        if (priceRangeIxs != null) {
            priceRangeIxs = Arrays.stream(priceRangeIxs).filter(r -> r >= 0 && r <= lastIx).toArray();
        }
        List<Product> products = productRepository.selectFilterProduct(categoryIds, brandIds, priceRangeIxs, name,
                status, isSort, onlyEnable);
        this.productCount = products.size();
        if (offset != null && limit != null) {
            int ix1 = offset - 1;
            int ix2 = offset + limit - 1;
            ix1 = ix1 >= 0 ? ix1 : 0;
            ix2 = ix2 >= 1 ? ix2 : 1;
            ix1 = this.productCount > ix1 ? ix1 : 0;
            ix2 = this.productCount > ix2 ? ix2 : this.productCount;
            return products.subList(ix1, ix2);
        }
        return products;
    }

    public int getProductCount() {
        return productCount;
    }

    @Override
    @Transactional
    public Product save(ProductRequest request) {
        Date now = new Date();
        Product product = new Product();
        if(request.getId() == null) {
            product.setId(AppUtils.generateRandomString(15));
            product.setCreatedAt(now);
            product.setUpdatedAt(now);
        } else {
            Product prod = productRepository.selectProductById(request.getId());
            product.setId(prod.getId());
            product.setUpdatedAt(now);
            product.setCreatedAt(prod.getCreatedAt());
            product.setAvgStar(prod.getAvgStar());
            product.setNumberVote(prod.getNumberVote());
        }
        product.setName(request.getName());
        product.setAvatar(request.getAvatar());
        product.setPrice(request.getPrice());
        product.setPriceOff(request.getPriceOff());
        product.setStatus(request.isStatus());
        product.setQuanlity(request.getQuanlity());
        product.setPromoPrice(request.getPrice() - request.getPriceOff());
        Brand brand = brandRepository.selectBrandById(request.getBrandId());
        Category category = categoryRepository.selectCategoryById(request.getCategoryId());
        product.setBrand(brand);
        product.setCategory(category);

        return productRepository.save(product);
    }

    @Override
    @Transactional
    public Product getProductById(String id) {
        return productRepository.selectProductById(id);
    }

    @Override
    @Transactional
    public void updateProductStatus(UpdateProductStatusRequest request) {
        productRepository.updateProductStatus(request.getStatus(), request.getProductId());
    }

}
