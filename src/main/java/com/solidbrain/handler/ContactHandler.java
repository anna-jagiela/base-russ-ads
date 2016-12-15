package com.solidbrain.handler;

import com.getbase.Client;
import com.getbase.models.Contact;
import com.getbase.models.Deal;
import com.getbase.sync.Meta;
import com.solidbrain.data.ContactToDealRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import static com.solidbrain.common.ContactUtils.isAssignedToSalesRep;
import static com.solidbrain.common.ContactUtils.isNoActiveDealsAssignedToContact;
import static com.solidbrain.common.ContactUtils.isOrganization;
import static com.solidbrain.common.DealUtils.createDealForContact;
import static com.solidbrain.common.MetaUtils.isCreated;
import static com.solidbrain.common.MetaUtils.isUpdated;
import static com.solidbrain.data.ContactToDealUtils.dealWasNotCreatedBefore;
import static com.solidbrain.data.ContactToDealUtils.saveContactToDeal;

/**
 * Created by annajagiela on 09/12/2016.
 */
@Component
public class ContactHandler implements Handler<Contact> {
    private static final Logger log = LoggerFactory.getLogger(ContactHandler.class);

    public boolean handleNotification(Meta meta,
                                      Contact contact,
                                      Client client,
                                      ContactToDealRepository contactToDealRepository) {
        log.info(meta.toString());
        log.info(contact.toString());

        if(isCreated(meta) || isUpdated(meta)){
            handleContactChange(contact, client, contactToDealRepository);
        }
        return true;
    }

    private void handleContactChange(Contact contact,
                                     Client client,
                                     ContactToDealRepository contactToDealRepository) {

        if (shouldCreateNewDeal(contact, client, contactToDealRepository)) {
            log.info("Creating new deal");
            Deal deal = createDealForContact(contact, client);
            saveContactToDeal(deal, contactToDealRepository);
        }

    }

    private boolean shouldCreateNewDeal(Contact contact,
                                        Client client,
                                        ContactToDealRepository contactToDealRepository) {
        return isOrganization(contact)
                && isAssignedToSalesRep(contact, client)
                && isNoActiveDealsAssignedToContact(client, contact)
                && dealWasNotCreatedBefore(contact, contactToDealRepository);
    }

}
