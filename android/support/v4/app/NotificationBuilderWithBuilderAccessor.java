package android.support.v4.app;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.Notification.Builder;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import android.support.annotation.RestrictTo.Scope;

@RestrictTo({Scope.LIBRARY_GROUP})
@RequiresApi(11)
@TargetApi(11)
public interface NotificationBuilderWithBuilderAccessor {
    Notification build();

    Builder getBuilder();
}
