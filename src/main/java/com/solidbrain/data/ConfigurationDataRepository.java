package com.solidbrain.data;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by annajagiela on 09/12/2016.
 */
@Repository
public interface ConfigurationDataRepository extends CrudRepository<ConfigurationData, Long> {
    ConfigurationData findByName(String name);
}
