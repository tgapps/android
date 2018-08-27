package org.telegram.ui;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import org.telegram.ui.ContactsActivity.AnonymousClass5;

final /* synthetic */ class ContactsActivity$5$$Lambda$0 implements OnClickListener {
    private final AnonymousClass5 arg$1;
    private final String arg$2;

    ContactsActivity$5$$Lambda$0(AnonymousClass5 anonymousClass5, String str) {
        this.arg$1 = anonymousClass5;
        this.arg$2 = str;
    }

    public void onClick(DialogInterface dialogInterface, int i) {
        this.arg$1.lambda$onItemClick$0$ContactsActivity$5(this.arg$2, dialogInterface, i);
    }
}
