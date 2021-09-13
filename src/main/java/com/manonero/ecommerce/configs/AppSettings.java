package com.manonero.ecommerce.configs;

import org.springframework.stereotype.Component;

@Component
public class AppSettings {
    public int[][] priceRangeArr = {
    	{0, 10000000},
    	{10000000,15000000},
    	{15000000, 20000000},
    	{20000000, 25000000},
    	{25000000, 1000000000}
    };
}
