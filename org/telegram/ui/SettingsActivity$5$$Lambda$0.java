package org.telegram.ui;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import org.telegram.ui.Components.NumberPicker;
import org.telegram.ui.SettingsActivity.AnonymousClass5;

final /* synthetic */ class SettingsActivity$5$$Lambda$0 implements OnClickListener {
    private final AnonymousClass5 arg$1;
    private final NumberPicker arg$2;
    private final int arg$3;

    SettingsActivity$5$$Lambda$0(AnonymousClass5 anonymousClass5, NumberPicker numberPicker, int i) {
        this.arg$1 = anonymousClass5;
        this.arg$2 = numberPicker;
        this.arg$3 = i;
    }

    public void onClick(DialogInterface dialogInterface, int i) {
        this.arg$1.lambda$onItemClick$0$SettingsActivity$5(this.arg$2, this.arg$3, dialogInterface, i);
    }
}
