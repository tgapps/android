package org.telegram.messenger;

import org.telegram.tgnet.TLRPC.photos_Photos;

final /* synthetic */ class MessagesStorage$$Lambda$112 implements Runnable {
    private final MessagesStorage arg$1;
    private final photos_Photos arg$2;
    private final int arg$3;
    private final int arg$4;
    private final long arg$5;
    private final int arg$6;

    MessagesStorage$$Lambda$112(MessagesStorage messagesStorage, photos_Photos org_telegram_tgnet_TLRPC_photos_Photos, int i, int i2, long j, int i3) {
        this.arg$1 = messagesStorage;
        this.arg$2 = org_telegram_tgnet_TLRPC_photos_Photos;
        this.arg$3 = i;
        this.arg$4 = i2;
        this.arg$5 = j;
        this.arg$6 = i3;
    }

    public void run() {
        this.arg$1.lambda$null$37$MessagesStorage(this.arg$2, this.arg$3, this.arg$4, this.arg$5, this.arg$6);
    }
}
