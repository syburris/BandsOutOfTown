package com.YoungMoney.services;

import com.YoungMoney.entities.Concert;
import com.YoungMoney.entities.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by stevenburris on 10/28/16.
 */
public interface ConcertRepository extends CrudRepository<Concert, Integer> {
    List<Concert> findFirstByCity(String city);
    
    Iterable<Concert> findByCity(String string);
    
    Iterable<Concert> findByVenue(String venueSearch);

    Iterable<Concert> findByName(String bandSearch);

    Iterable<Concert> findByNameAndCity(String bandSearch, String citySearch);

    Iterable<Concert> findByVenueAndName(String venueSearch, String bandSearch);

    Iterable<Concert> findByCityAndVenueAndName(String citySearch, String venueSearch, String bandSearch);

    Object findFirstByUserAndConcert(User user, Concert concert);
}
