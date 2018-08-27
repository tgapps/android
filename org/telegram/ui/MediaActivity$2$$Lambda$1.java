package org.telegram.ui;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import org.telegram.ui.MediaActivity.AnonymousClass2;

final /* synthetic */ class MediaActivity$2$$Lambda$1 implements OnClickListener {
    private final AnonymousClass2 arg$1;
    private final boolean[] arg$2;

    MediaActivity$2$$Lambda$1(AnonymousClass2 anonymousClass2, boolean[] zArr) {
        this.arg$1 = anonymousClass2;
        this.arg$2 = zArr;
    }

    public void onClick(DialogInterface dialogInterface, int i) {
        this.arg$1.lambda$onItemClick$1$MediaActivity$2(this.arg$2, dialogInterface, i);
    }
}
