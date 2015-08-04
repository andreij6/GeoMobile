package com.geospatialcorporation.android.geomobile.models.Layers;


import com.geospatialcorporation.android.geomobile.models.Query.map.response.mapquery.Feature;

import java.util.HashMap;
import java.util.List;

public class EditLayerAttributesRequest {

    List<RequestFeatures> Features;

    public EditLayerAttributesRequest(List<RequestFeatures> features) {
        Features = features;
    }

    public List<RequestFeatures> getFeatures() {
        return Features;
    }

    public void setFeatures(List<RequestFeatures> features) {
        Features = features;
    }

    public static class RequestFeatures{
        private String Id;
        private HashMap<Integer, String> Values;

        public RequestFeatures(String id, HashMap<Integer, String> values){
            Id = id;
            Values = values;
        }

        @Override
        public String toString() {
            String result = " Id: " + Id + " Values {";

            for(Integer key : Values.keySet()){
                result += " " + key + " : " + Values.get(key) + " ";
            }

            result += " } ";

            return result;
        }
    }

}


