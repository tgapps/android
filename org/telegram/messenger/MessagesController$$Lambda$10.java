package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;

final /* synthetic */ class MessagesController$$Lambda$10 implements RequestDelegate {
    private final MessagesController arg$1;

    MessagesController$$Lambda$10(MessagesController messagesController) {
        this.arg$1 = messagesController;
    }

    public void run(TLObject tLObject, TL_error tL_error) {
        this.arg$1.lambda$reloadDialogsReadValue$10$MessagesController(tLObject, tL_error);
    }
}
