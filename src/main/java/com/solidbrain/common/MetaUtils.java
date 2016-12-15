package com.solidbrain.common;

import com.getbase.sync.Meta;

/**
 * Created by annajagiela on 09/12/2016.
 */
public class MetaUtils {

    public static boolean isUpdated(Meta meta) {
        return getEventType(meta).equals("updated");
    }

    public static boolean isCreated(Meta meta) {
        return getEventType(meta).equals("created");
    }

    private static String getEventType(Meta meta){
        return meta.getSync().getEventType();
    }
}
