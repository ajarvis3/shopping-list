package com.example.shopping.services;

import com.example.shopping.dto.PlanDTO;
import org.springframework.stereotype.Service;

public interface IPlanService {
    public PlanDTO createPlan(Long listId, String zipCode);
}
