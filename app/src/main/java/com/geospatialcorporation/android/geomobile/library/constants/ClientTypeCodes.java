package com.geospatialcorporation.android.geomobile.library.constants;

import java.util.AbstractMap;
import java.util.HashMap;

/**
 * Created by andre on 5/31/2015.
 */
public class ClientTypeCodes {

    public ClientTypeCodes(){

    }

    public static final AbstractMap.SimpleEntry<Integer, String> STANDARD = new AbstractMap.SimpleEntry<>(1, "Standard");
    public static final AbstractMap.SimpleEntry<Integer, String > DEMOCLONE = new AbstractMap.SimpleEntry<>(2, "Demo Clone");
    public static final AbstractMap.SimpleEntry<Integer, String> TUTORIAL = new AbstractMap.SimpleEntry<>(3, "Tutorial");
    public static final AbstractMap.SimpleEntry<Integer, String > TUTORIALCLONE = new AbstractMap.SimpleEntry<>(4, "Tutorial Clone");
    public static final AbstractMap.SimpleEntry<Integer, String> DEFAULT = new AbstractMap.SimpleEntry<>(5, "Default");
    public static final AbstractMap.SimpleEntry<Integer, String> SSP = new AbstractMap.SimpleEntry<>(6, "SSP");
    public static final AbstractMap.SimpleEntry<Integer, String> PLUGINOWNERS = new AbstractMap.SimpleEntry<>(7, "Plugin Owners");

    private HashMap<Integer, String> clientTypeCodes = new HashMap<>();

    public String getClientTypeName(int typeCode){
        clientTypeCodes.put(STANDARD.getKey(), STANDARD.getValue());
        clientTypeCodes.put(DEMOCLONE.getKey(), DEMOCLONE.getValue());
        clientTypeCodes.put(TUTORIAL.getKey(), TUTORIAL.getValue());
        clientTypeCodes.put(TUTORIALCLONE.getKey(), TUTORIALCLONE.getValue());
        clientTypeCodes.put(DEFAULT.getKey(), DEFAULT.getValue());
        clientTypeCodes.put(SSP.getKey(), SSP.getValue());
        clientTypeCodes.put(PLUGINOWNERS.getKey(), PLUGINOWNERS.getValue());

        return clientTypeCodes.get(typeCode);
    }


}
