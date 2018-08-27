package org.telegram.ui;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import org.telegram.ui.MediaActivity.AnonymousClass9;

final /* synthetic */ class MediaActivity$9$$Lambda$0 implements OnClickListener {
    private final AnonymousClass9 arg$1;
    private final String arg$2;

    MediaActivity$9$$Lambda$0(AnonymousClass9 anonymousClass9, String str) {
        this.arg$1 = anonymousClass9;
        this.arg$2 = str;
    }

    public void onClick(DialogInterface dialogInterface, int i) {
        this.arg$1.lambda$onLinkLongPress$0$MediaActivity$9(this.arg$2, dialogInterface, i);
    }
}
