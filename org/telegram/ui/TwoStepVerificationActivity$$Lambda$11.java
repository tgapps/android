package org.telegram.ui;

import org.telegram.tgnet.TLRPC.TL_error;

final /* synthetic */ class TwoStepVerificationActivity$$Lambda$11 implements Runnable {
    private final TwoStepVerificationActivity arg$1;
    private final TL_error arg$2;

    TwoStepVerificationActivity$$Lambda$11(TwoStepVerificationActivity twoStepVerificationActivity, TL_error tL_error) {
        this.arg$1 = twoStepVerificationActivity;
        this.arg$2 = tL_error;
    }

    public void run() {
        this.arg$1.lambda$null$30$TwoStepVerificationActivity(this.arg$2);
    }
}
