package android.support.v13.view.inputmethod;

import android.os.Build.VERSION;
import android.os.Bundle;
import android.view.inputmethod.EditorInfo;

public final class EditorInfoCompat {
    private static final String[] EMPTY_STRING_ARRAY = new String[0];

    public static void setContentMimeTypes(EditorInfo editorInfo, String[] contentMimeTypes) {
        if (VERSION.SDK_INT >= 25) {
            editorInfo.contentMimeTypes = contentMimeTypes;
            return;
        }
        if (editorInfo.extras == null) {
            editorInfo.extras = new Bundle();
        }
        editorInfo.extras.putStringArray("android.support.v13.view.inputmethod.EditorInfoCompat.CONTENT_MIME_TYPES", contentMimeTypes);
    }

    public static String[] getContentMimeTypes(EditorInfo editorInfo) {
        String[] result;
        if (VERSION.SDK_INT >= 25) {
            result = editorInfo.contentMimeTypes;
            return result != null ? result : EMPTY_STRING_ARRAY;
        } else if (editorInfo.extras == null) {
            return EMPTY_STRING_ARRAY;
        } else {
            result = editorInfo.extras.getStringArray("android.support.v13.view.inputmethod.EditorInfoCompat.CONTENT_MIME_TYPES");
            return result != null ? result : EMPTY_STRING_ARRAY;
        }
    }
}
