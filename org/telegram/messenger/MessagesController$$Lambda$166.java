package org.telegram.messenger;

import org.telegram.tgnet.TLRPC.updates_Difference;

final /* synthetic */ class MessagesController$$Lambda$166 implements Runnable {
    private final MessagesController arg$1;
    private final updates_Difference arg$2;

    MessagesController$$Lambda$166(MessagesController messagesController, updates_Difference org_telegram_tgnet_TLRPC_updates_Difference) {
        this.arg$1 = messagesController;
        this.arg$2 = org_telegram_tgnet_TLRPC_updates_Difference;
    }

    public void run() {
        this.arg$1.lambda$null$189$MessagesController(this.arg$2);
    }
}
