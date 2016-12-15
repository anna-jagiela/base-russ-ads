package com.solidbrain.data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by annajagiela on 09/12/2016.
 */
@Entity
public class ContactToDeal {

    @Id
    @GeneratedValue
    private Long id;
    private Long contact;
    private String deal;

    private ContactToDeal(){}

    public ContactToDeal(Long contact, String deal){
        this.contact = contact;
        this.deal = deal;
    }

    public void setDeal(String deal) {
        this.deal = deal;
    }

    @Override
    public String toString(){
        return this.contact+" = "+this.deal;
    }

}
