package org.telegram.ui;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import org.telegram.ui.SettingsActivity.AnonymousClass5;

final /* synthetic */ class SettingsActivity$5$$Lambda$3 implements OnClickListener {
    private final AnonymousClass5 arg$1;
    private final int arg$2;

    SettingsActivity$5$$Lambda$3(AnonymousClass5 anonymousClass5, int i) {
        this.arg$1 = anonymousClass5;
        this.arg$2 = i;
    }

    public void onClick(DialogInterface dialogInterface, int i) {
        this.arg$1.lambda$onItemClick$3$SettingsActivity$5(this.arg$2, dialogInterface, i);
    }
}
