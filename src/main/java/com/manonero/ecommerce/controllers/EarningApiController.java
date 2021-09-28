package com.manonero.ecommerce.controllers;

import java.util.Calendar;
import java.util.List;

import com.manonero.ecommerce.entities.EarningView;
import com.manonero.ecommerce.models.Response;
import com.manonero.ecommerce.services.IEarningService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/earning")
public class EarningApiController {
    
    @Autowired
    private IEarningService earningService;

    @GetMapping()
    public Response getCurrentYear() {
        int year = Calendar.getInstance().get(Calendar.YEAR);
        List<EarningView> earningViews = earningService.getByYear(year);
        return new Response(earningViews, true);
    }
}
