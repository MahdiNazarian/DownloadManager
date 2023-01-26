package com.ghazalpaknia_mahdinazarian.static_values;

public class StaticValues {
    public static enum DownloadStates{
        DOWNLOADING,
        PAUSED,
        CANCELED,
        ERROR,
    }
    public static enum DownloadType{
        ALL_TOGETHER,
        SERIALIZED_SYNC
    }
}
