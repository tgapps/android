package org.telegram.messenger;

import org.telegram.tgnet.TLRPC.updates_ChannelDifference;

final /* synthetic */ class MessagesController$$Lambda$173 implements Runnable {
    private final MessagesController arg$1;
    private final updates_ChannelDifference arg$2;

    MessagesController$$Lambda$173(MessagesController messagesController, updates_ChannelDifference org_telegram_tgnet_TLRPC_updates_ChannelDifference) {
        this.arg$1 = messagesController;
        this.arg$2 = org_telegram_tgnet_TLRPC_updates_ChannelDifference;
    }

    public void run() {
        this.arg$1.lambda$null$179$MessagesController(this.arg$2);
    }
}
