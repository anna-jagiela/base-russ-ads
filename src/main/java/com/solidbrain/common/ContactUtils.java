package com.solidbrain.common;

import com.getbase.Client;
import com.getbase.models.Contact;
import com.getbase.models.Deal;

import java.util.List;
import java.util.stream.Collectors;

import static com.solidbrain.common.DealUtils.getDealsByContactIdCriteria;
import static com.solidbrain.common.DealUtils.isActive;

/**
 * Created by annajagiela on 15/12/2016.
 */
public class ContactUtils {

    public static Boolean isOrganization(Contact contact) {
        return contact.getIsOrganization();
    }

    public static boolean isAssignedToSalesRep(Contact contact, Client client) {
        return client
                .users()
                .get(contact.getOwnerId())
                .getRole()
                .equals("user");
    }

    public static boolean isNoActiveDealsAssignedToContact(Client client, Contact contact) {
        List<Deal> dealList = client
                .deals()
                .list(getDealsByContactIdCriteria(contact));

        return dealList
                .stream()
                .filter(deal -> isActive(deal, client))
                .collect(Collectors.toList())
                .isEmpty();
    }
}
