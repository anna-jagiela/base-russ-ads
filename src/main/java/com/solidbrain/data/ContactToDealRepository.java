package com.solidbrain.data;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by annajagiela on 09/12/2016.
 */
@Repository
public interface ContactToDealRepository extends CrudRepository<ContactToDeal, Long> {
    ContactToDeal findByContact(Long contact);
}
