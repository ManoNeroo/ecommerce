package com.manonero.ecommerce.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import com.manonero.ecommerce.entities.Brand;
import com.manonero.ecommerce.entities.Category;
import com.manonero.ecommerce.models.BrandRequest;
import com.manonero.ecommerce.repositories.IBrandRepository;
import com.manonero.ecommerce.repositories.ICategoryRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BrandService implements IBrandService {

    @Autowired
	private IBrandRepository brandRepository;
    @Autowired
	private ICategoryRepository categoryRepository;

    @Override
    @Transactional
    public List<Brand> getBrandByOffsetLimit(int offset, int limit, String name, Boolean status) {
		List<Brand> brands = brandRepository.selectBrandByOffsetLimit(offset, limit, name, status);
		return brands;
    }

    @Override
    @Transactional
    public int getBrandCount(String name, Boolean status) {
        return brandRepository.selectBrandCount(name, status);
    }

    @Override
    @Transactional
    public Brand save(BrandRequest request) {
        Date now = new Date();
        Brand brand = new Brand();
        brand.setId(request.getId());
        brand.setName(request.getName());
        brand.setLogo(request.getLogo());
        brand.setStatus(request.isStatus());
		if(brand.getId() > 0) {
			brand.setUpdatedAt(now);
            brand.setCreatedAt(request.getCreatedAt());
            brand.setId(request.getId());
		} else {
			brand.setCreatedAt(now);
			brand.setUpdatedAt(now);
		}
        ArrayList<Category> categories = new ArrayList<>();
        if(request.getCategoryIds() != null) {
            for(int categoryId : request.getCategoryIds()) {
                categories.add(categoryRepository.selectCategoryById(categoryId));
            }
        }
        brand.setCategories(categories);
        return brandRepository.save(brand);
    }

    @Override
    @Transactional
    public boolean toggleBrandStatus(int id, boolean status) {
        return brandRepository.updateBrandStatus(id, status);
    }

    @Override
    @Transactional
    public Brand getBrandById(int id) {
        return brandRepository.selectBrandById(id);
    }

    @Override
    public List<Brand> getAllBrand(Boolean isEnable) {
        // TODO Auto-generated method stub
        return brandRepository.selectAllBrand(isEnable);
    }
    
}
