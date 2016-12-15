package com.solidbrain;

import com.getbase.Client;
import com.getbase.Configuration;
import com.getbase.models.*;
import com.getbase.sync.Sync;
import com.solidbrain.data.ConfigurationData;
import com.solidbrain.data.ConfigurationDataRepository;
import com.solidbrain.data.ContactToDealRepository;
import com.solidbrain.handler.Handler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by annajagiela on 08/12/2016.
 */
@Component
public class ScheduledSyncWithBase {

    private Sync baseSync;

    public ScheduledSyncWithBase(ConfigurationDataRepository configurationDataRepository,
                                 ContactToDealRepository contactToDealRepository,
                                 Handler<Contact> contactHandler,
                                 Handler<Deal> dealHandler){

       String accessToken = configurationDataRepository.findByName("AccessToken").getValue();
       String deviceUUID = configurationDataRepository.findByName("UUID").getValue();

       Client baseClient = new Client(new Configuration.Builder()
               .accessToken(accessToken)
               .build());

       baseSync = new Sync(baseClient, deviceUUID)
                .subscribe(Account.class, (meta, account) -> true)
                .subscribe(Address.class, (meta, address) -> true)
                .subscribe(AssociatedContact.class, (meta, associatedContact) -> true)
                .subscribe(Contact.class, (meta, contact) ->
                        contactHandler.handleNotification(meta, contact, baseClient, contactToDealRepository))
                .subscribe(Deal.class, (meta, deal) ->
                        dealHandler.handleNotification(meta, deal, baseClient, contactToDealRepository))
                .subscribe(LossReason.class, (meta, lossReason) -> true)
                .subscribe(Note.class, (meta, note) -> true)
                .subscribe(Pipeline.class, (meta, pipeline) -> true)
                .subscribe(Source.class, (meta, source) -> true)
                .subscribe(Stage.class, (meta, stage) -> true)
                .subscribe(Tag.class, (meta, tag) -> true)
                .subscribe(Task.class, (meta, task) -> true)
                .subscribe(ConfigurationData.class, (meta, configurationData) -> true)
                .subscribe(Lead.class, (meta, lead) -> true);

    }

    @Scheduled(fixedDelay = 3000)
    public void fetchInformation() {
        baseSync.fetch();
    }
}
