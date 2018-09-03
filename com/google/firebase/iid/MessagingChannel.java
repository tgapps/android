package com.google.firebase.iid;

import com.google.android.gms.tasks.Task;
import javax.annotation.Nullable;

public interface MessagingChannel {
    Task<Void> buildChannel(String str, @Nullable String str2);

    Task<String> getToken(String str, @Nullable String str2, String str3, String str4);

    boolean isAvailable();

    boolean isChannelBuilt();

    Task<Void> subscribeToTopic(String str, String str2, String str3);

    Task<Void> unsubscribeFromTopic(String str, String str2, String str3);
}