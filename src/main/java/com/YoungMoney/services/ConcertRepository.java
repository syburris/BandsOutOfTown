package com.YoungMoney.services;

import com.YoungMoney.entities.Concert;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by stevenburris on 10/28/16.
 */
public interface ConcertRepository extends CrudRepository<Concert, Integer> {
    List<Concert> findFirstByCity(String city);

}
