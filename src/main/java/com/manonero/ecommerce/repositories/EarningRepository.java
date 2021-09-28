package com.manonero.ecommerce.repositories;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import com.manonero.ecommerce.entities.EarningView;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class EarningRepository implements IEarningRepository {

    @Autowired
    private EntityManager entityManager;

    @Override
    public List<EarningView> selectByYear(int year) {
        TypedQuery<EarningView> query = entityManager.createQuery("from EarningView where year=:year", EarningView.class);
        query.setParameter("year", year);
        return query.getResultList();
    }
    
}
