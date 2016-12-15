package com.solidbrain.handler;

import com.getbase.Client;
import com.getbase.models.Deal;
import com.getbase.sync.Meta;
import com.solidbrain.data.ContactToDealRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import static com.solidbrain.common.DealUtils.*;
import static com.solidbrain.common.MetaUtils.isCreated;
import static com.solidbrain.common.MetaUtils.isUpdated;
import static com.solidbrain.data.ContactToDealUtils.saveContactToDeal;

/**
 * Created by annajagiela on 09/12/2016.
 */
@Component
public class DealHandler implements Handler<Deal> {
    private static final Logger log = LoggerFactory.getLogger(DealHandler.class);

    public boolean handleNotification(Meta meta,
                                      Deal deal,
                                      Client client,
                                      ContactToDealRepository contactToDealRepository) {
        log.info(meta.toString());
        log.info(deal.toString());

        if(isUpdated(meta)){
            handleDealUpdate(deal, client);
        } else if (isCreated(meta)){
            handleDealCreate(deal, contactToDealRepository);
        }
        return true;
    }

    private void handleDealUpdate(Deal deal,
                                  Client client) {
        if(isWon(deal, client)
                && !isAssignedToAccountManager(deal, client)) {

            assignDealToAccountManager(deal, client);
            log.info("Reassigning the deal");
            updateDeal(deal, client);
        }
    }

    private void handleDealCreate(Deal deal,
                                  ContactToDealRepository contactToDealRepository) {
        saveContactToDeal(deal, contactToDealRepository);
    }


}
