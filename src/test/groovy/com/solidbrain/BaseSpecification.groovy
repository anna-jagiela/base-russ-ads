package com.solidbrain

import com.getbase.Client
import com.getbase.Configuration
import com.getbase.models.Contact
import com.getbase.models.Deal
import com.getbase.services.ContactsService
import com.getbase.services.DealsService
import spock.lang.Shared
import spock.lang.Specification

/**
 * Created by annajagiela on 06/12/2016.
 */
@groovy.util.logging.Log
abstract class BaseSpecification extends Specification{

    public static final ACCESS_TOKEN = "01afd61e554fd26af02f328373b2ef591f40bc0c2f8cd907fad06bbd8b5b2080"
    public static final SALES_REP_ID = 1042630
    public static final ACCOUNT_MANAGER_ID =  1042632
    public static final CONTACT_NAME = "XYZ"
    public static final DEAL_STAGE_INCOMING = 6092254
    public static final STAGE_ID_WON = 6092258
    public static final STAGE_ID_LOST = 6092260
    public static final LOSS_REASON_ID = 2326720
    public static final SLEEP_TIME = 4000 //4 seconds

    private List<Contact> contactList = new ArrayList<>();
    private List<Deal> dealList = new ArrayList<>();


    @Shared baseClient = new Client(new Configuration.Builder()
            .accessToken(ACCESS_TOKEN)
            .verbose()
            .build())

    Contact createTestContact(Map params = [:]) {
        def contact = new Contact(params)
        contact.setName(CONTACT_NAME)

        contact = baseClient.contacts().create(contact)
        contactList << contact

        contact
    }

    Deal createTestDeal(Map params = [:]) {
        def deal = new Deal(params)
        deal.setName(CONTACT_NAME+" DEAL")

        deal = baseClient
                .deals()
                .create(deal)

        dealList << deal

        deal
    }

    String getDealName() {
        CONTACT_NAME +"_"+ new Date().format('dd.MM.yyyy')
    }

    Deal getDealById(long dealId){
        baseClient.deals().get(dealId)
    }

    Boolean checkDealExist(long dealId){
        return baseClient.deals().get(dealId).id == dealId
    }

    Deal modifyStageOfDeal(Deal existingDeal, long stageId){
        existingDeal.setStageId(stageId)
        if (stageId == STAGE_ID_LOST){
            existingDeal.setLossReasonId(LOSS_REASON_ID)
        }
        baseClient.deals().update(existingDeal)
    }

    List<Deal> getDealsByContactId(long contactId) {
        baseClient.deals().list(new DealsService.SearchCriteria().contactId(contactId))
    }

    List<Contact> getContactsByName(String contactName) {
        baseClient
                .contacts()
                .list(new ContactsService.SearchCriteria().name(contactName))
    }

    def saveDealForCleanup(Deal deal){
        dealList << deal
    }

    def removeDealsAndContacts() {
        dealList.each {deal -> removeDeal(deal)}
        sleep(200)
        contactList.each{contact -> removeContact(contact)}
    }

    def removeContact(Contact contact){
        baseClient
                .contacts()
                .delete(contact.id)
    }

    def removeDeal(Deal deal){
        baseClient
                .deals()
                .delete(deal.id)
    }
}
