package android.support.v13.view.inputmethod;

import android.content.ClipDescription;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.text.TextUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputConnectionWrapper;
import android.view.inputmethod.InputContentInfo;

public final class InputConnectionCompat {
    public static int INPUT_CONTENT_GRANT_READ_URI_PERMISSION = 1;

    public interface OnCommitContentListener {
        boolean onCommitContent(InputContentInfoCompat inputContentInfoCompat, int i, Bundle bundle);
    }

    static boolean handlePerformPrivateCommand(String action, Bundle data, OnCommitContentListener onCommitContentListener) {
        int i = 1;
        if (!TextUtils.equals("android.support.v13.view.inputmethod.InputConnectionCompat.COMMIT_CONTENT", action) || data == null) {
            return false;
        }
        ResultReceiver resultReceiver = null;
        boolean result = false;
        try {
            resultReceiver = (ResultReceiver) data.getParcelable("android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_RESULT_RECEIVER");
            Uri contentUri = (Uri) data.getParcelable("android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_URI");
            ClipDescription description = (ClipDescription) data.getParcelable("android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_DESCRIPTION");
            Uri linkUri = (Uri) data.getParcelable("android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_LINK_URI");
            result = onCommitContentListener.onCommitContent(new InputContentInfoCompat(contentUri, description, linkUri), data.getInt("android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_FLAGS"), (Bundle) data.getParcelable("android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_OPTS"));
            if (resultReceiver != null) {
                int i2;
                if (result) {
                    i2 = 1;
                } else {
                    i2 = 0;
                }
                resultReceiver.send(i2, null);
            }
            return result;
        } catch (Throwable th) {
            if (resultReceiver != null) {
                if (!result) {
                    i = 0;
                }
                resultReceiver.send(i, null);
            }
        }
    }

    public static InputConnection createWrapper(InputConnection inputConnection, EditorInfo editorInfo, OnCommitContentListener onCommitContentListener) {
        if (inputConnection == null) {
            throw new IllegalArgumentException("inputConnection must be non-null");
        } else if (editorInfo == null) {
            throw new IllegalArgumentException("editorInfo must be non-null");
        } else if (onCommitContentListener == null) {
            throw new IllegalArgumentException("onCommitContentListener must be non-null");
        } else if (VERSION.SDK_INT >= 25) {
            listener = onCommitContentListener;
            return new InputConnectionWrapper(inputConnection, false) {
                public boolean commitContent(InputContentInfo inputContentInfo, int flags, Bundle opts) {
                    if (listener.onCommitContent(InputContentInfoCompat.wrap(inputContentInfo), flags, opts)) {
                        return true;
                    }
                    return super.commitContent(inputContentInfo, flags, opts);
                }
            };
        } else if (EditorInfoCompat.getContentMimeTypes(editorInfo).length == 0) {
            return inputConnection;
        } else {
            listener = onCommitContentListener;
            return new InputConnectionWrapper(inputConnection, false) {
                public boolean performPrivateCommand(String action, Bundle data) {
                    if (InputConnectionCompat.handlePerformPrivateCommand(action, data, listener)) {
                        return true;
                    }
                    return super.performPrivateCommand(action, data);
                }
            };
        }
    }
}
