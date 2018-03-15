package com.formation.kafka.consumer.service.impl;

import com.formation.kafka.consumer.repository.CarRepository;
import com.formation.kafka.consumer.service.CarService;
import model.CarMessage;

public class CarServiceImpl implements CarService {


    private CarRepository repository;

    public CarServiceImpl(CarRepository repository) {
        this.repository = repository;
    }

    @Override
    public void saveCar(CarMessage model) {
        try{
            model.setHp(22);
            repository.save(model);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
