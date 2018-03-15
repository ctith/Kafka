package com.formation.kafka.consumer.service;

import model.CarMessage;

public interface CarService {
    void saveCar(CarMessage model);
}
