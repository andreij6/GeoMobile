package com.geospatialcorporation.android.geomobile.library.helpers;

import java.util.HashMap;

public class QueryMachine {

    HashMap<Integer, Boolean> StateMap;

    public QueryMachine(){
        StateMap = new HashMap<>();
        StateMap.put(State.PICKQUERY, true);
        StateMap.put(State.BOXQUERY_DRAWN, false);
        StateMap.put(State.BOXQUERY_INPROGRESS, false);
        StateMap.put(State.POINTQUERY_DRAWN, false);
        StateMap.put(State.POINTQUERY_INPROGRESS, false);
    }

    public void setState(int stateInt){
        StateMap.put(stateInt, true);

        for(Integer key : StateMap.keySet()){
            if(key != stateInt) {
                StateMap.put(key, false);
            }
        }
    }

    public Boolean isState(int stateInt){
        return StateMap.get(stateInt);
    }

    public int getState(){
        int result = 0;

        for(int key : StateMap.keySet()){
            if(StateMap.get(key)){
                result = key;
            }
        }

        return result;
    }


    public static class State {
        public static final int POINTQUERY_INPROGRESS = 6;
        public static final int POINTQUERY_DRAWN = 5;
        public static final int BOXQUERY_STARTED = 4;
        public static final int BOXQUERY_INPROGRESS = 3;
        public static final int BOXQUERY_DRAWN = 2;
        public static final int PICKQUERY = 1;
    }
}
