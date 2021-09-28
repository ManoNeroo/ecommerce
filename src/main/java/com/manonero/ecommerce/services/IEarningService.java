package com.manonero.ecommerce.services;

import java.util.List;

import com.manonero.ecommerce.entities.EarningView;

public interface IEarningService {
    List<EarningView> getByYear(int year);
}
