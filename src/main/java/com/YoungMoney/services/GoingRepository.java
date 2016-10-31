package com.YoungMoney.services;

import com.YoungMoney.entities.Concert;
import com.YoungMoney.entities.Go;
import com.YoungMoney.entities.User;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by stevenburris on 10/30/16.
 */
public interface GoingRepository extends CrudRepository <Go, Integer> {


    Go findFirstByConcertAndUser(Concert concert, User user);

    Go findFirstByUserAndConcert(User user, Concert concert);
}
