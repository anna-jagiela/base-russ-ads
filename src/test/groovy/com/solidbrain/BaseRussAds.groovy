package com.solidbrain

import groovy.util.logging.Slf4j

/**
 * Created by annajagiela on 05/12/2016.
 */
import org.awaitility.groovy.AwaitilitySupport
import spock.lang.Stepwise
import spock.lang.Unroll

import java.util.concurrent.TimeUnit


@Unroll
@Mixin(AwaitilitySupport)
class BaseRussAds extends BaseSpecification {

    def cleanup() {
        removeDealsAndContacts()
    }

    def "should reassign deal to Account Manager: #isReassigned, owner: #owner, deal stage: #dealStage"(long owner, long dealStage, boolean isReassigned)  {

        given:
        def contact = createTestContact(ownerId: owner, isOrganization: true)
        def deal = createTestDeal(contactId: contact.id, ownerId: owner)

        when:
        await().atMost(20, TimeUnit.SECONDS).until {
            checkDealExist(deal.id)
        }
        modifyStageOfDeal(deal, dealStage)

        then:
        def newOwner = isReassigned? SALES_REP_ID: deal.getOwnerId()
        await().atMost(20, TimeUnit.SECONDS).until {
            getDealById(deal.id).ownerId == newOwner
        }

        where:
        owner              | dealStage       | isReassigned
        SALES_REP_ID       | STAGE_ID_LOST   | false
        ACCOUNT_MANAGER_ID | STAGE_ID_WON    | false
        SALES_REP_ID       | STAGE_ID_WON    | true

    }

    def "Should not create Deal when Contact owner is not a Sales Rep or Contact is not organization"(long ownerId, boolean isOrganization) {

        when:
        def contact = createTestContact(ownerId: ownerId, isOrganization: isOrganization)
        sleep(SLEEP_TIME)

        then:
        getDealsByContactId(contact.id).isEmpty()

        where:
        ownerId            | isOrganization
        SALES_REP_ID       | false
        ACCOUNT_MANAGER_ID | true
    }

    def "Should not create Deal when there is an active deal"() {

        when:
        def contact = createTestContact(ownerId: SALES_REP_ID, isOrganization: true)
        createTestDeal(contactId: contact.id, ownerId:  SALES_REP_ID)
        sleep(SLEEP_TIME)

        then: "new deal should not be created"
        getDealsByContactId(contact.id).size() == 1
    }

    def "Should create Deal when Contact is a Company, Owner is a Sales Rep and there is no active Deal assigned to it"() {

        when:
        def contact = createTestContact(ownerId: SALES_REP_ID, isOrganization: true)

        await().atMost(40, TimeUnit.SECONDS).until{
            !getDealsByContactId(contact.id).isEmpty()
        }
        def deal = getDealsByContactId(contact.id).get(0)
        saveDealForCleanup(deal)

        then:
        deal.ownerId == SALES_REP_ID
        deal.name == getDealName()
        deal.stageId == DEAL_STAGE_INCOMING
    }

}
