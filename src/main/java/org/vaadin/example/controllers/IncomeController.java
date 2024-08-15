package org.vaadin.example.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.vaadin.example.model.Income;
import org.vaadin.example.service.IncomeService;

import java.util.List;

@RestController
@RequestMapping("/income")
public class IncomeController {

    @Autowired
    private IncomeService incomeService;

    @GetMapping("/user/{userId}")
    public List<Income> getIncomesByUserId(@PathVariable Long userId) {
        return incomeService.getIncomesByUserId(userId);
    }

    @PostMapping("/add")
    public Income addIncome(@RequestBody Income income) {
        return incomeService.addIncome(income);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteIncome(@PathVariable Long id) {
        incomeService.deleteIncome(id);
    }
}
