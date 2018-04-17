package com.google.android.gms.wearable;

import android.os.Parcelable;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Releasable;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
import java.io.InputStream;
import java.io.OutputStream;
import javax.annotation.Nullable;

@Deprecated
public interface Channel extends Parcelable {

    @Deprecated
    public interface GetInputStreamResult extends Releasable, Result {
        @Nullable
        InputStream getInputStream();
    }

    @Deprecated
    public interface GetOutputStreamResult extends Releasable, Result {
        @Nullable
        OutputStream getOutputStream();
    }

    PendingResult<Status> close(GoogleApiClient googleApiClient);

    PendingResult<GetInputStreamResult> getInputStream(GoogleApiClient googleApiClient);

    PendingResult<GetOutputStreamResult> getOutputStream(GoogleApiClient googleApiClient);

    String getPath();
}
