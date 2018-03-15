package com.formation.kafka.formationkafka.mapper;

import com.formation.kafka.formationkafka.dto.CarDto;
import model.CarMessage;

import java.util.function.Function;

public class CarDtoToCarMessageMapper implements Function<CarDto, CarMessage> {

    @Override
    public CarMessage apply(CarDto dto) {
        CarMessage msg = new CarMessage();
        msg.setBrand(dto.getBrand());
        msg.setHp(dto.getHp());
        return msg;
    }
}
