package org.telegram.ui;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;
import org.telegram.ui.PassportActivity.AnonymousClass8;

final /* synthetic */ class PassportActivity$8$$Lambda$0 implements RequestDelegate {
    private final AnonymousClass8 arg$1;

    PassportActivity$8$$Lambda$0(AnonymousClass8 anonymousClass8) {
        this.arg$1 = anonymousClass8;
    }

    public void run(TLObject tLObject, TL_error tL_error) {
        this.arg$1.lambda$resetSecret$3$PassportActivity$8(tLObject, tL_error);
    }
}
