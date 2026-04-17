package com.example.shopping.controllers;

import com.example.shopping.dto.PlanDTO;
import com.example.shopping.services.IPlanService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/list/{listId}/plan")
public class PlanController {

    IPlanService planService;

    public PlanController(IPlanService planService) {
        this.planService = planService;
    }

    @GetMapping
    public ResponseEntity<PlanDTO> getShoppingPlans(@PathVariable("listId") Long listId, @RequestParam(required = false) String zip) {
        return ResponseEntity
                .status(200)
                .body(planService.createPlan(listId, zip));
    }
}
