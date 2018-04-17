package com.google.firebase.remoteconfig;

public class FirebaseRemoteConfigFetchThrottledException extends FirebaseRemoteConfigFetchException {
    private final long zzr;

    public FirebaseRemoteConfigFetchThrottledException(long j) {
        this.zzr = j;
    }
}
