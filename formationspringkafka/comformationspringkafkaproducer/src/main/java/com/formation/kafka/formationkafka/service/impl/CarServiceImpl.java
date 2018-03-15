package com.formation.kafka.formationkafka.service.impl;

import com.formation.kafka.formationkafka.dto.CarDto;
import com.formation.kafka.formationkafka.mapper.CarDtoToCarMessageMapper;
import com.formation.kafka.formationkafka.producer.Sender;
import com.formation.kafka.formationkafka.service.CarService;

public class CarServiceImpl implements CarService {

    private Sender sender;

    private CarDtoToCarMessageMapper mapper;

    public CarServiceImpl(Sender sender, CarDtoToCarMessageMapper mapper) {
        this.sender = sender;
        this.mapper = mapper;
    }

    @Override
    public void sendCar(CarDto carDto) {
        this.sender.send(mapper.apply(carDto));
    }
}
