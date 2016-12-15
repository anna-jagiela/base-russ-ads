package com.solidbrain.common;

import com.getbase.Client;
import com.getbase.models.Contact;
import com.getbase.models.Deal;
import com.getbase.services.DealsService;
import com.getbase.services.StagesService;
import com.getbase.services.UsersService;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by annajagiela on 09/12/2016.
 */
public class DealUtils {

    public static long getDealStageIdByName(String name, Client client){
       return client
               .stages()
               .list(new StagesService.SearchCriteria().name(name))
               .get(0)
               .getId();
    }

    public static Deal createDealForContact(Contact contact, Client client) {
        Deal deal = prepareDeal(contact);
        client.deals().create(deal);
        return deal;
    }

    static private Deal prepareDeal(Contact contact){
        Deal deal = new Deal();
        deal.setName(generateDealName(contact));
        deal.setOwnerId(contact.getOwnerId());
        deal.setContactId(contact.getId());
        return deal;
    }

    static private String generateDealName(Contact contact) {
        return contact.getName() + "_" + getCurrentDateSimpleFormat();
    }

    static private String getCurrentDateSimpleFormat(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        return dateFormat.format(new Date());
    }

    public static  boolean isWon(Deal deal,
                          Client client) {
        return deal.getStageId() == getDealStageIdByName("Won", client);
    }

    public static Deal updateDeal(Deal deal,
                            Client client) {
        return client.deals().update(deal);
    }

    public static void assignDealToAccountManager(Deal deal, Client client) {
        deal.setOwnerId(client
                .users()
                .list(new UsersService.SearchCriteria()
                        .role("admin"))
                .get(0)
                .getId());
    }

    public static boolean isAssignedToAccountManager(Deal deal,
                                               Client client) {
        return client
                .users()
                .get(deal.getOwnerId())
                .getRole()
                .equals("admin");
    }

    public static DealsService.SearchCriteria getDealsByContactIdCriteria(Contact contact) {
        return new DealsService.SearchCriteria().contactId(contact.getId());
    }

    public static boolean isActive(Deal deal, Client client) {
        long lostStage = getDealStageIdByName("Lost", client);
        long wonStage = getDealStageIdByName("Won", client);

        return (deal.getStageId()!= lostStage
                && deal.getStageId() != wonStage);
    }

}
