package com.YoungMoney.services;

import com.YoungMoney.entities.Concert;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by stevenburris on 10/28/16.
 */
public interface ConcertRepository extends CrudRepository<Concert, Integer> {
}
