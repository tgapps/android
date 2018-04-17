package android.support.v4.view.accessibility;

import android.graphics.Rect;
import android.os.Build.VERSION;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityNodeInfo.CollectionInfo;
import android.view.accessibility.AccessibilityNodeInfo.CollectionItemInfo;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.exoplayer2.C;

public class AccessibilityNodeInfoCompat {
    private final AccessibilityNodeInfo mInfo;
    public int mParentVirtualDescendantId = -1;

    public static class CollectionInfoCompat {
        final Object mInfo;

        public static CollectionInfoCompat obtain(int rowCount, int columnCount, boolean hierarchical, int selectionMode) {
            if (VERSION.SDK_INT >= 21) {
                return new CollectionInfoCompat(CollectionInfo.obtain(rowCount, columnCount, hierarchical, selectionMode));
            }
            if (VERSION.SDK_INT >= 19) {
                return new CollectionInfoCompat(CollectionInfo.obtain(rowCount, columnCount, hierarchical));
            }
            return new CollectionInfoCompat(null);
        }

        CollectionInfoCompat(Object info) {
            this.mInfo = info;
        }
    }

    public static class CollectionItemInfoCompat {
        final Object mInfo;

        public static CollectionItemInfoCompat obtain(int rowIndex, int rowSpan, int columnIndex, int columnSpan, boolean heading, boolean selected) {
            if (VERSION.SDK_INT >= 21) {
                return new CollectionItemInfoCompat(CollectionItemInfo.obtain(rowIndex, rowSpan, columnIndex, columnSpan, heading, selected));
            }
            if (VERSION.SDK_INT >= 19) {
                return new CollectionItemInfoCompat(CollectionItemInfo.obtain(rowIndex, rowSpan, columnIndex, columnSpan, heading));
            }
            return new CollectionItemInfoCompat(null);
        }

        CollectionItemInfoCompat(Object info) {
            this.mInfo = info;
        }
    }

    private AccessibilityNodeInfoCompat(AccessibilityNodeInfo info) {
        this.mInfo = info;
    }

    public static AccessibilityNodeInfoCompat wrap(AccessibilityNodeInfo info) {
        return new AccessibilityNodeInfoCompat(info);
    }

    public AccessibilityNodeInfo unwrap() {
        return this.mInfo;
    }

    public int getActions() {
        return this.mInfo.getActions();
    }

    public void addAction(int action) {
        this.mInfo.addAction(action);
    }

    public void getBoundsInParent(Rect outBounds) {
        this.mInfo.getBoundsInParent(outBounds);
    }

    public void getBoundsInScreen(Rect outBounds) {
        this.mInfo.getBoundsInScreen(outBounds);
    }

    public boolean isCheckable() {
        return this.mInfo.isCheckable();
    }

    public boolean isChecked() {
        return this.mInfo.isChecked();
    }

    public boolean isFocusable() {
        return this.mInfo.isFocusable();
    }

    public boolean isFocused() {
        return this.mInfo.isFocused();
    }

    public boolean isSelected() {
        return this.mInfo.isSelected();
    }

    public boolean isClickable() {
        return this.mInfo.isClickable();
    }

    public boolean isLongClickable() {
        return this.mInfo.isLongClickable();
    }

    public boolean isEnabled() {
        return this.mInfo.isEnabled();
    }

    public boolean isPassword() {
        return this.mInfo.isPassword();
    }

    public boolean isScrollable() {
        return this.mInfo.isScrollable();
    }

    public void setScrollable(boolean scrollable) {
        this.mInfo.setScrollable(scrollable);
    }

    public CharSequence getPackageName() {
        return this.mInfo.getPackageName();
    }

    public CharSequence getClassName() {
        return this.mInfo.getClassName();
    }

    public void setClassName(CharSequence className) {
        this.mInfo.setClassName(className);
    }

    public CharSequence getText() {
        return this.mInfo.getText();
    }

    public CharSequence getContentDescription() {
        return this.mInfo.getContentDescription();
    }

    public String getViewIdResourceName() {
        if (VERSION.SDK_INT >= 18) {
            return this.mInfo.getViewIdResourceName();
        }
        return null;
    }

    public void setCollectionInfo(Object collectionInfo) {
        if (VERSION.SDK_INT >= 19) {
            this.mInfo.setCollectionInfo((CollectionInfo) ((CollectionInfoCompat) collectionInfo).mInfo);
        }
    }

    public void setCollectionItemInfo(Object collectionItemInfo) {
        if (VERSION.SDK_INT >= 19) {
            this.mInfo.setCollectionItemInfo((CollectionItemInfo) ((CollectionItemInfoCompat) collectionItemInfo).mInfo);
        }
    }

    public int hashCode() {
        return this.mInfo == null ? 0 : this.mInfo.hashCode();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        AccessibilityNodeInfoCompat other = (AccessibilityNodeInfoCompat) obj;
        if (this.mInfo == null) {
            if (other.mInfo != null) {
                return false;
            }
        } else if (!this.mInfo.equals(other.mInfo)) {
            return false;
        }
        return true;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(super.toString());
        Rect bounds = new Rect();
        getBoundsInParent(bounds);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("; boundsInParent: ");
        stringBuilder.append(bounds);
        builder.append(stringBuilder.toString());
        getBoundsInScreen(bounds);
        stringBuilder = new StringBuilder();
        stringBuilder.append("; boundsInScreen: ");
        stringBuilder.append(bounds);
        builder.append(stringBuilder.toString());
        builder.append("; packageName: ");
        builder.append(getPackageName());
        builder.append("; className: ");
        builder.append(getClassName());
        builder.append("; text: ");
        builder.append(getText());
        builder.append("; contentDescription: ");
        builder.append(getContentDescription());
        builder.append("; viewId: ");
        builder.append(getViewIdResourceName());
        builder.append("; checkable: ");
        builder.append(isCheckable());
        builder.append("; checked: ");
        builder.append(isChecked());
        builder.append("; focusable: ");
        builder.append(isFocusable());
        builder.append("; focused: ");
        builder.append(isFocused());
        builder.append("; selected: ");
        builder.append(isSelected());
        builder.append("; clickable: ");
        builder.append(isClickable());
        builder.append("; longClickable: ");
        builder.append(isLongClickable());
        builder.append("; enabled: ");
        builder.append(isEnabled());
        builder.append("; password: ");
        builder.append(isPassword());
        stringBuilder = new StringBuilder();
        stringBuilder.append("; scrollable: ");
        stringBuilder.append(isScrollable());
        builder.append(stringBuilder.toString());
        builder.append("; [");
        int actionBits = getActions();
        while (actionBits != 0) {
            int action = 1 << Integer.numberOfTrailingZeros(actionBits);
            actionBits &= action ^ -1;
            builder.append(getActionSymbolicName(action));
            if (actionBits != 0) {
                builder.append(", ");
            }
        }
        builder.append("]");
        return builder.toString();
    }

    private static String getActionSymbolicName(int action) {
        switch (action) {
            case 1:
                return "ACTION_FOCUS";
            case 2:
                return "ACTION_CLEAR_FOCUS";
            case 4:
                return "ACTION_SELECT";
            case 8:
                return "ACTION_CLEAR_SELECTION";
            case 16:
                return "ACTION_CLICK";
            case 32:
                return "ACTION_LONG_CLICK";
            case 64:
                return "ACTION_ACCESSIBILITY_FOCUS";
            case 128:
                return "ACTION_CLEAR_ACCESSIBILITY_FOCUS";
            case 256:
                return "ACTION_NEXT_AT_MOVEMENT_GRANULARITY";
            case 512:
                return "ACTION_PREVIOUS_AT_MOVEMENT_GRANULARITY";
            case 1024:
                return "ACTION_NEXT_HTML_ELEMENT";
            case 2048:
                return "ACTION_PREVIOUS_HTML_ELEMENT";
            case 4096:
                return "ACTION_SCROLL_FORWARD";
            case MessagesController.UPDATE_MASK_CHANNEL /*8192*/:
                return "ACTION_SCROLL_BACKWARD";
            case MessagesController.UPDATE_MASK_CHAT_ADMINS /*16384*/:
                return "ACTION_COPY";
            case 32768:
                return "ACTION_PASTE";
            case C.DEFAULT_BUFFER_SEGMENT_SIZE /*65536*/:
                return "ACTION_CUT";
            case 131072:
                return "ACTION_SET_SELECTION";
            default:
                return "ACTION_UNKNOWN";
        }
    }
}
