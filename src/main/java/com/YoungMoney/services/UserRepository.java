package com.YoungMoney.services;

import com.YoungMoney.entities.User;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by stevenburris on 10/28/16.
 */
public interface UserRepository extends CrudRepository<User, Integer> {

    User findFirstByName(String username);
}
