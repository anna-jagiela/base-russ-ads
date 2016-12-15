package com.solidbrain.data;

import com.getbase.models.Contact;
import com.getbase.models.Deal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/**
 * Created by annajagiela on 12/12/2016.
 */
public class ContactToDealUtils {
    private static final Logger log = LoggerFactory.getLogger(ContactToDealUtils.class);

    public static void saveContactToDeal(Deal deal,
                                         ContactToDealRepository contactToDealRepository) {

        Optional<ContactToDeal> maybeContactToDeal =
                Optional.ofNullable(deal.getContactId())
                        .map(d -> contactToDealRepository.findByContact(d));

        if(maybeContactToDeal.isPresent())
        {
            overwriteExistingEntry(deal, contactToDealRepository, maybeContactToDeal.get());
        } else {
            createNewEntry(deal, contactToDealRepository);
        }
    }

    private static void overwriteExistingEntry(Deal deal,
                                               ContactToDealRepository contactToDealRepository,
                                               ContactToDeal existingEntry) {
        log.info("Overwriting existing ContactToDeal entry");
        existingEntry.setDeal(deal.getName());
        contactToDealRepository.save(existingEntry);
    }

    private static void createNewEntry(Deal deal,
                                       ContactToDealRepository contactToDealRepository) {
        log.info("New ContactToDeal entry");
        contactToDealRepository.save(new ContactToDeal(deal.getContactId(), deal.getName()));
    }

    public static boolean dealWasNotCreatedBefore(Contact contact,
                                                  ContactToDealRepository contactToDealRepository) {
        boolean result = contactToDealRepository.findByContact(contact.getId()) == null;
        log.info("Deal was not created before  = "+result+"Contact ID = "+contact.getId());
        return result;
    }
}
