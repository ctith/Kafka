package com.formation.kafka.formationkafka.service;

import com.formation.kafka.formationkafka.dto.CarDto;

public interface CarService {
    void sendCar(CarDto carDto);
}
