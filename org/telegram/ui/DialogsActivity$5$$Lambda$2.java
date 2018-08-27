package org.telegram.ui;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import org.telegram.tgnet.TLRPC.TL_dialog;
import org.telegram.ui.DialogsActivity.AnonymousClass5;

final /* synthetic */ class DialogsActivity$5$$Lambda$2 implements OnClickListener {
    private final AnonymousClass5 arg$1;
    private final boolean arg$2;
    private final boolean arg$3;
    private final TL_dialog arg$4;
    private final boolean arg$5;
    private final boolean arg$6;

    DialogsActivity$5$$Lambda$2(AnonymousClass5 anonymousClass5, boolean z, boolean z2, TL_dialog tL_dialog, boolean z3, boolean z4) {
        this.arg$1 = anonymousClass5;
        this.arg$2 = z;
        this.arg$3 = z2;
        this.arg$4 = tL_dialog;
        this.arg$5 = z3;
        this.arg$6 = z4;
    }

    public void onClick(DialogInterface dialogInterface, int i) {
        this.arg$1.lambda$onItemClick$5$DialogsActivity$5(this.arg$2, this.arg$3, this.arg$4, this.arg$5, this.arg$6, dialogInterface, i);
    }
}
