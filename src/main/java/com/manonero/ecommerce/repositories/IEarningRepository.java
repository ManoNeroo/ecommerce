package com.manonero.ecommerce.repositories;

import java.util.List;

import com.manonero.ecommerce.entities.EarningView;

public interface IEarningRepository {
    List<EarningView> selectByYear(int year);
}
