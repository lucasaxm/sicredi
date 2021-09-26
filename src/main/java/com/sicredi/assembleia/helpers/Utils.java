package com.sicredi.assembleia.helpers;

public final class Utils {
    private static volatile Utils instance;

    public static Utils getInstance() {
        Utils result = instance;
        if (result != null){
            return result;
        }
        synchronized(Utils.class) {
            if (instance == null) {
                instance = new Utils();
            }
            return instance;
        }
    }

    public boolean isNullOrEmpty(String input) {
        return (input == null || input.isBlank());
    }
}
