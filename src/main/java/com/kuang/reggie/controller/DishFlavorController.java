package com.kuang.reggie.controller;

import com.kuang.reggie.service.DishFlavorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/dishflavor")
public class DishFlavorController {

    @Autowired
    DishFlavorService dishFlavorService;


}
