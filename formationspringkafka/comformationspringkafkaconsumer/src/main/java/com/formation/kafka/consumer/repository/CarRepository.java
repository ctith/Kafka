package com.formation.kafka.consumer.repository;

import model.CarMessage;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;


public interface CarRepository extends MongoRepository<CarMessage, String> {

    CarMessage findById(String id);

    List<CarMessage> findByBrand(String brand);

}