package com.formation.kafka.formationkafka.controller;

import com.formation.kafka.formationkafka.dto.CarDto;
import com.formation.kafka.formationkafka.service.CarService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/v1")
public class CarController {
    private static final Logger LOGGER = LoggerFactory.getLogger(CarController.class);


    private CarService carService;

    public CarController(CarService userService) {
        this.carService = userService;
    }

    @ApiOperation(value = "car creation", httpMethod = "POST", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, message = "Invalid Request", response = ResponseEntity.class)
    })
    @PostMapping(value = "/car")
    public ResponseEntity  addCar(@ApiParam(name = "carDto", value = "car body") @RequestBody CarDto carDto) {

        LOGGER.info("############################################");


        carService.sendCar(carDto);

        return new ResponseEntity(1, HttpStatus.OK);

    }
}
