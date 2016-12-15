package com.solidbrain.handler;

import com.getbase.Client;
import com.getbase.sync.Meta;
import com.solidbrain.data.ContactToDealRepository;

/**
 * Created by annajagiela on 09/12/2016.
 */
@FunctionalInterface
public interface Handler<T> {

    boolean handleNotification(Meta meta,
                               T resource,
                               Client client,
                               ContactToDealRepository contactToDealRepository);
}
