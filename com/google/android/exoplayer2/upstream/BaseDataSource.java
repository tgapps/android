package com.google.android.exoplayer2.upstream;

import com.google.android.exoplayer2.util.Assertions;
import java.util.ArrayList;
import java.util.Map;

public abstract class BaseDataSource implements DataSource {
    private DataSpec dataSpec;
    private final boolean isNetwork;
    private final ArrayList<TransferListener> listeners = new ArrayList(1);

    public Map getResponseHeaders() {
        return DataSource$$CC.getResponseHeaders(this);
    }

    protected BaseDataSource(boolean isNetwork) {
        this.isNetwork = isNetwork;
    }

    public final void addTransferListener(TransferListener transferListener) {
        this.listeners.add(transferListener);
    }

    protected final void transferInitializing(DataSpec dataSpec) {
        for (int i = 0; i < this.listeners.size(); i++) {
            ((TransferListener) this.listeners.get(i)).onTransferInitializing(this, dataSpec, this.isNetwork);
        }
    }

    protected final void transferStarted(DataSpec dataSpec) {
        this.dataSpec = dataSpec;
        for (int i = 0; i < this.listeners.size(); i++) {
            ((TransferListener) this.listeners.get(i)).onTransferStart(this, dataSpec, this.isNetwork);
        }
    }

    protected final void bytesTransferred(int bytesTransferred) {
        DataSpec dataSpec = (DataSpec) Assertions.checkNotNull(this.dataSpec);
        for (int i = 0; i < this.listeners.size(); i++) {
            ((TransferListener) this.listeners.get(i)).onBytesTransferred(this, dataSpec, this.isNetwork, bytesTransferred);
        }
    }

    protected final void transferEnded() {
        DataSpec dataSpec = (DataSpec) Assertions.checkNotNull(this.dataSpec);
        for (int i = 0; i < this.listeners.size(); i++) {
            ((TransferListener) this.listeners.get(i)).onTransferEnd(this, dataSpec, this.isNetwork);
        }
        this.dataSpec = null;
    }
}
