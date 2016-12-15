package com.solidbrain.data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by annajagiela on 09/12/2016.
 */
@Entity
public class ConfigurationData {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String value;

    private ConfigurationData(){}

    @Override
    public String toString(){
        return this.name+" = "+this.value;
    }

    public String getValue(){
        return value;
    }
}
