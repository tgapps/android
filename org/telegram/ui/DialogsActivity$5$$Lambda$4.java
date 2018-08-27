package org.telegram.ui;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import org.telegram.tgnet.TLRPC.Chat;
import org.telegram.ui.DialogsActivity.AnonymousClass5;

final /* synthetic */ class DialogsActivity$5$$Lambda$4 implements OnClickListener {
    private final AnonymousClass5 arg$1;
    private final Chat arg$2;

    DialogsActivity$5$$Lambda$4(AnonymousClass5 anonymousClass5, Chat chat) {
        this.arg$1 = anonymousClass5;
        this.arg$2 = chat;
    }

    public void onClick(DialogInterface dialogInterface, int i) {
        this.arg$1.lambda$null$1$DialogsActivity$5(this.arg$2, dialogInterface, i);
    }
}
