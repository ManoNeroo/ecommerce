package com.manonero.ecommerce.services;

import java.util.List;

import javax.transaction.Transactional;

import com.manonero.ecommerce.entities.EarningView;
import com.manonero.ecommerce.repositories.IEarningRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EarningService implements IEarningService {

    @Autowired
    private IEarningRepository earningRepository;

    @Override
    @Transactional
    public List<EarningView> getByYear(int year) {
        return earningRepository.selectByYear(year);
    }
    
}
