package org.telegram.messenger;

import android.util.LongSparseArray;

final /* synthetic */ class MessagesController$$Lambda$58 implements Runnable {
    private final MessagesController arg$1;
    private final LongSparseArray arg$2;
    private final LongSparseArray arg$3;

    MessagesController$$Lambda$58(MessagesController messagesController, LongSparseArray longSparseArray, LongSparseArray longSparseArray2) {
        this.arg$1 = messagesController;
        this.arg$2 = longSparseArray;
        this.arg$3 = longSparseArray2;
    }

    public void run() {
        this.arg$1.lambda$updatePrintingStrings$81$MessagesController(this.arg$2, this.arg$3);
    }
}
