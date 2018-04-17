package android.support.v4.app;

import android.app.Notification;
import android.app.Notification.Builder;
import android.app.RemoteInput;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat.Action;
import android.support.v4.app.NotificationCompat.Style;
import android.util.SparseArray;
import android.widget.RemoteViews;
import java.util.List;

class NotificationCompatBuilder implements NotificationBuilderWithBuilderAccessor {
    private final List<Bundle> mActionExtrasList;
    private RemoteViews mBigContentView;
    private final Builder mBuilder;
    private final NotificationCompat.Builder mBuilderCompat;
    private RemoteViews mContentView;
    private final Bundle mExtras;
    private int mGroupAlertBehavior;
    private RemoteViews mHeadsUpContentView;

    NotificationCompatBuilder(android.support.v4.app.NotificationCompat.Builder r1) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: android.support.v4.app.NotificationCompatBuilder.<init>(android.support.v4.app.NotificationCompat$Builder):void
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:116)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:249)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
Caused by: java.lang.NullPointerException
*/
        /*
        r0 = this;
        r9.<init>();
        r0 = new java.util.ArrayList;
        r0.<init>();
        r9.mActionExtrasList = r0;
        r0 = new android.os.Bundle;
        r0.<init>();
        r9.mExtras = r0;
        r9.mBuilderCompat = r10;
        r0 = android.os.Build.VERSION.SDK_INT;
        r1 = 26;
        if (r0 < r1) goto L_0x0025;
    L_0x0019:
        r0 = new android.app.Notification$Builder;
        r2 = r10.mContext;
        r3 = r10.mChannelId;
        r0.<init>(r2, r3);
        r9.mBuilder = r0;
        goto L_0x002e;
    L_0x0025:
        r0 = new android.app.Notification$Builder;
        r2 = r10.mContext;
        r0.<init>(r2);
        r9.mBuilder = r0;
    L_0x002e:
        r0 = r10.mNotification;
        r2 = r9.mBuilder;
        r3 = r0.when;
        r2 = r2.setWhen(r3);
        r3 = r0.icon;
        r4 = r0.iconLevel;
        r2 = r2.setSmallIcon(r3, r4);
        r3 = r0.contentView;
        r2 = r2.setContent(r3);
        r3 = r0.tickerText;
        r4 = r10.mTickerView;
        r2 = r2.setTicker(r3, r4);
        r3 = r0.vibrate;
        r2 = r2.setVibrate(r3);
        r3 = r0.ledARGB;
        r4 = r0.ledOnMS;
        r5 = r0.ledOffMS;
        r2 = r2.setLights(r3, r4, r5);
        r3 = r0.flags;
        r3 = r3 & 2;
        r4 = 1;
        r5 = 0;
        if (r3 == 0) goto L_0x0068;
    L_0x0066:
        r3 = r4;
        goto L_0x006a;
        r3 = r5;
        r2 = r2.setOngoing(r3);
        r3 = r0.flags;
        r3 = r3 & 8;
        if (r3 == 0) goto L_0x0076;
        r3 = r4;
        goto L_0x0078;
        r3 = r5;
        r2 = r2.setOnlyAlertOnce(r3);
        r3 = r0.flags;
        r6 = 16;
        r3 = r3 & r6;
        if (r3 == 0) goto L_0x0085;
        r3 = r4;
        goto L_0x0087;
        r3 = r5;
        r2 = r2.setAutoCancel(r3);
        r3 = r0.defaults;
        r2 = r2.setDefaults(r3);
        r3 = r10.mContentTitle;
        r2 = r2.setContentTitle(r3);
        r3 = r10.mContentText;
        r2 = r2.setContentText(r3);
        r3 = r10.mContentInfo;
        r2 = r2.setContentInfo(r3);
        r3 = r10.mContentIntent;
        r2 = r2.setContentIntent(r3);
        r3 = r0.deleteIntent;
        r2 = r2.setDeleteIntent(r3);
        r3 = r10.mFullScreenIntent;
        r7 = r0.flags;
        r7 = r7 & 128;
        if (r7 == 0) goto L_0x00b9;
        r7 = r4;
        goto L_0x00bb;
        r7 = r5;
        r2 = r2.setFullScreenIntent(r3, r7);
        r3 = r10.mLargeIcon;
        r2 = r2.setLargeIcon(r3);
        r3 = r10.mNumber;
        r2 = r2.setNumber(r3);
        r3 = r10.mProgressMax;
        r7 = r10.mProgress;
        r8 = r10.mProgressIndeterminate;
        r2.setProgress(r3, r7, r8);
        r2 = android.os.Build.VERSION.SDK_INT;
        r3 = 21;
        if (r2 >= r3) goto L_0x00e3;
        r2 = r9.mBuilder;
        r7 = r0.sound;
        r8 = r0.audioStreamType;
        r2.setSound(r7, r8);
        r2 = android.os.Build.VERSION.SDK_INT;
        r7 = 20;
        if (r2 < r6) goto L_0x0161;
        r2 = r9.mBuilder;
        r6 = r10.mSubText;
        r2 = r2.setSubText(r6);
        r6 = r10.mUseChronometer;
        r2 = r2.setUsesChronometer(r6);
        r6 = r10.mPriority;
        r2.setPriority(r6);
        r2 = r10.mActions;
        r2 = r2.iterator();
        r6 = r2.hasNext();
        if (r6 == 0) goto L_0x0112;
        r6 = r2.next();
        r6 = (android.support.v4.app.NotificationCompat.Action) r6;
        r9.addAction(r6);
        goto L_0x0102;
        r2 = r10.mExtras;
        if (r2 == 0) goto L_0x011d;
        r2 = r9.mExtras;
        r6 = r10.mExtras;
        r2.putAll(r6);
        r2 = android.os.Build.VERSION.SDK_INT;
        if (r2 >= r7) goto L_0x0159;
        r2 = r10.mLocalOnly;
        if (r2 == 0) goto L_0x012c;
        r2 = r9.mExtras;
        r6 = "android.support.localOnly";
        r2.putBoolean(r6, r4);
        r2 = r10.mGroupKey;
        if (r2 == 0) goto L_0x014c;
        r2 = r9.mExtras;
        r6 = "android.support.groupKey";
        r8 = r10.mGroupKey;
        r2.putString(r6, r8);
        r2 = r10.mGroupSummary;
        if (r2 == 0) goto L_0x0145;
        r2 = r9.mExtras;
        r6 = "android.support.isGroupSummary";
        r2.putBoolean(r6, r4);
        goto L_0x014c;
        r2 = r9.mExtras;
        r6 = "android.support.useSideChannel";
        r2.putBoolean(r6, r4);
        r2 = r10.mSortKey;
        if (r2 == 0) goto L_0x0159;
        r2 = r9.mExtras;
        r4 = "android.support.sortKey";
        r6 = r10.mSortKey;
        r2.putString(r4, r6);
        r2 = r10.mContentView;
        r9.mContentView = r2;
        r2 = r10.mBigContentView;
        r9.mBigContentView = r2;
        r2 = android.os.Build.VERSION.SDK_INT;
        r4 = 19;
        if (r2 < r4) goto L_0x0195;
        r2 = r9.mBuilder;
        r4 = r10.mShowWhen;
        r2.setShowWhen(r4);
        r2 = android.os.Build.VERSION.SDK_INT;
        if (r2 >= r3) goto L_0x0195;
        r2 = r10.mPeople;
        if (r2 == 0) goto L_0x0195;
        r2 = r10.mPeople;
        r2 = r2.isEmpty();
        if (r2 != 0) goto L_0x0195;
        r2 = r9.mExtras;
        r4 = "android.people";
        r6 = r10.mPeople;
        r8 = r10.mPeople;
        r8 = r8.size();
        r8 = new java.lang.String[r8];
        r6 = r6.toArray(r8);
        r6 = (java.lang.String[]) r6;
        r2.putStringArray(r4, r6);
        r2 = android.os.Build.VERSION.SDK_INT;
        if (r2 < r7) goto L_0x01b6;
        r2 = r9.mBuilder;
        r4 = r10.mLocalOnly;
        r2 = r2.setLocalOnly(r4);
        r4 = r10.mGroupKey;
        r2 = r2.setGroup(r4);
        r4 = r10.mGroupSummary;
        r2 = r2.setGroupSummary(r4);
        r4 = r10.mSortKey;
        r2.setSortKey(r4);
        r2 = r10.mGroupAlertBehavior;
        r9.mGroupAlertBehavior = r2;
        r2 = android.os.Build.VERSION.SDK_INT;
        if (r2 < r3) goto L_0x01f7;
        r2 = r9.mBuilder;
        r3 = r10.mCategory;
        r2 = r2.setCategory(r3);
        r3 = r10.mColor;
        r2 = r2.setColor(r3);
        r3 = r10.mVisibility;
        r2 = r2.setVisibility(r3);
        r3 = r10.mPublicVersion;
        r2 = r2.setPublicVersion(r3);
        r3 = r0.sound;
        r4 = r0.audioAttributes;
        r2.setSound(r3, r4);
        r2 = r10.mPeople;
        r2 = r2.iterator();
        r3 = r2.hasNext();
        if (r3 == 0) goto L_0x01f3;
        r3 = r2.next();
        r3 = (java.lang.String) r3;
        r4 = r9.mBuilder;
        r4.addPerson(r3);
        goto L_0x01e1;
        r2 = r10.mHeadsUpContentView;
        r9.mHeadsUpContentView = r2;
        r2 = android.os.Build.VERSION.SDK_INT;
        r3 = 24;
        if (r2 < r3) goto L_0x022b;
        r2 = r9.mBuilder;
        r3 = r10.mExtras;
        r2 = r2.setExtras(r3);
        r3 = r10.mRemoteInputHistory;
        r2.setRemoteInputHistory(r3);
        r2 = r10.mContentView;
        if (r2 == 0) goto L_0x0215;
        r2 = r9.mBuilder;
        r3 = r10.mContentView;
        r2.setCustomContentView(r3);
        r2 = r10.mBigContentView;
        if (r2 == 0) goto L_0x0220;
        r2 = r9.mBuilder;
        r3 = r10.mBigContentView;
        r2.setCustomBigContentView(r3);
        r2 = r10.mHeadsUpContentView;
        if (r2 == 0) goto L_0x022b;
        r2 = r9.mBuilder;
        r3 = r10.mHeadsUpContentView;
        r2.setCustomHeadsUpContentView(r3);
        r2 = android.os.Build.VERSION.SDK_INT;
        if (r2 < r1) goto L_0x026d;
        r1 = r9.mBuilder;
        r2 = r10.mBadgeIcon;
        r1 = r1.setBadgeIconType(r2);
        r2 = r10.mShortcutId;
        r1 = r1.setShortcutId(r2);
        r2 = r10.mTimeout;
        r1 = r1.setTimeoutAfter(r2);
        r2 = r10.mGroupAlertBehavior;
        r1.setGroupAlertBehavior(r2);
        r1 = r10.mColorizedSet;
        if (r1 == 0) goto L_0x0253;
        r1 = r9.mBuilder;
        r2 = r10.mColorized;
        r1.setColorized(r2);
        r1 = r10.mChannelId;
        r1 = android.text.TextUtils.isEmpty(r1);
        if (r1 != 0) goto L_0x026d;
        r1 = r9.mBuilder;
        r2 = 0;
        r1 = r1.setSound(r2);
        r1 = r1.setDefaults(r5);
        r1 = r1.setLights(r5, r5, r5);
        r1.setVibrate(r2);
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v4.app.NotificationCompatBuilder.<init>(android.support.v4.app.NotificationCompat$Builder):void");
    }

    public Builder getBuilder() {
        return this.mBuilder;
    }

    public Notification build() {
        RemoteViews styleBigContentView;
        Style style = this.mBuilderCompat.mStyle;
        if (style != null) {
            style.apply(this);
        }
        RemoteViews styleContentView = style != null ? style.makeContentView(this) : null;
        Notification n = buildInternal();
        if (styleContentView != null) {
            n.contentView = styleContentView;
        } else if (this.mBuilderCompat.mContentView != null) {
            n.contentView = this.mBuilderCompat.mContentView;
        }
        if (VERSION.SDK_INT >= 16 && style != null) {
            styleBigContentView = style.makeBigContentView(this);
            if (styleBigContentView != null) {
                n.bigContentView = styleBigContentView;
            }
        }
        if (VERSION.SDK_INT >= 21 && style != null) {
            styleBigContentView = this.mBuilderCompat.mStyle.makeHeadsUpContentView(this);
            if (styleBigContentView != null) {
                n.headsUpContentView = styleBigContentView;
            }
        }
        if (VERSION.SDK_INT >= 16 && style != null) {
            Bundle extras = NotificationCompat.getExtras(n);
            if (extras != null) {
                style.addCompatExtras(extras);
            }
        }
        return n;
    }

    private void addAction(Action action) {
        if (VERSION.SDK_INT >= 20) {
            Bundle actionExtras;
            Notification.Action.Builder actionBuilder = new Notification.Action.Builder(action.getIcon(), action.getTitle(), action.getActionIntent());
            if (action.getRemoteInputs() != null) {
                for (RemoteInput remoteInput : RemoteInput.fromCompat(action.getRemoteInputs())) {
                    actionBuilder.addRemoteInput(remoteInput);
                }
            }
            if (action.getExtras() != null) {
                actionExtras = new Bundle(action.getExtras());
            } else {
                actionExtras = new Bundle();
            }
            actionExtras.putBoolean("android.support.allowGeneratedReplies", action.getAllowGeneratedReplies());
            if (VERSION.SDK_INT >= 24) {
                actionBuilder.setAllowGeneratedReplies(action.getAllowGeneratedReplies());
            }
            actionBuilder.addExtras(actionExtras);
            this.mBuilder.addAction(actionBuilder.build());
        } else if (VERSION.SDK_INT >= 16) {
            this.mActionExtrasList.add(NotificationCompatJellybean.writeActionAndGetExtras(this.mBuilder, action));
        }
    }

    protected Notification buildInternal() {
        if (VERSION.SDK_INT >= 26) {
            return this.mBuilder.build();
        }
        Notification notification;
        if (VERSION.SDK_INT >= 24) {
            notification = this.mBuilder.build();
            if (this.mGroupAlertBehavior != 0) {
                if (!(notification.getGroup() == null || (notification.flags & 512) == 0 || this.mGroupAlertBehavior != 2)) {
                    removeSoundAndVibration(notification);
                }
                if (notification.getGroup() != null && (notification.flags & 512) == 0 && this.mGroupAlertBehavior == 1) {
                    removeSoundAndVibration(notification);
                }
            }
            return notification;
        } else if (VERSION.SDK_INT >= 21) {
            this.mBuilder.setExtras(this.mExtras);
            notification = this.mBuilder.build();
            if (this.mContentView != null) {
                notification.contentView = this.mContentView;
            }
            if (this.mBigContentView != null) {
                notification.bigContentView = this.mBigContentView;
            }
            if (this.mHeadsUpContentView != null) {
                notification.headsUpContentView = this.mHeadsUpContentView;
            }
            if (this.mGroupAlertBehavior != 0) {
                if (!(notification.getGroup() == null || (notification.flags & 512) == 0 || this.mGroupAlertBehavior != 2)) {
                    removeSoundAndVibration(notification);
                }
                if (notification.getGroup() != null && (notification.flags & 512) == 0 && this.mGroupAlertBehavior == 1) {
                    removeSoundAndVibration(notification);
                }
            }
            return notification;
        } else if (VERSION.SDK_INT >= 20) {
            this.mBuilder.setExtras(this.mExtras);
            notification = this.mBuilder.build();
            if (this.mContentView != null) {
                notification.contentView = this.mContentView;
            }
            if (this.mBigContentView != null) {
                notification.bigContentView = this.mBigContentView;
            }
            if (this.mGroupAlertBehavior != 0) {
                if (!(notification.getGroup() == null || (notification.flags & 512) == 0 || this.mGroupAlertBehavior != 2)) {
                    removeSoundAndVibration(notification);
                }
                if (notification.getGroup() != null && (notification.flags & 512) == 0 && this.mGroupAlertBehavior == 1) {
                    removeSoundAndVibration(notification);
                }
            }
            return notification;
        } else if (VERSION.SDK_INT >= 19) {
            SparseArray<Bundle> actionExtrasMap = NotificationCompatJellybean.buildActionExtrasMap(this.mActionExtrasList);
            if (actionExtrasMap != null) {
                this.mExtras.putSparseParcelableArray("android.support.actionExtras", actionExtrasMap);
            }
            this.mBuilder.setExtras(this.mExtras);
            Notification notification2 = this.mBuilder.build();
            if (this.mContentView != null) {
                notification2.contentView = this.mContentView;
            }
            if (this.mBigContentView != null) {
                notification2.bigContentView = this.mBigContentView;
            }
            return notification2;
        } else if (VERSION.SDK_INT < 16) {
            return this.mBuilder.getNotification();
        } else {
            notification = this.mBuilder.build();
            Bundle extras = NotificationCompat.getExtras(notification);
            Bundle mergeBundle = new Bundle(this.mExtras);
            for (String key : this.mExtras.keySet()) {
                if (extras.containsKey(key)) {
                    mergeBundle.remove(key);
                }
            }
            extras.putAll(mergeBundle);
            SparseArray<Bundle> actionExtrasMap2 = NotificationCompatJellybean.buildActionExtrasMap(this.mActionExtrasList);
            if (actionExtrasMap2 != null) {
                NotificationCompat.getExtras(notification).putSparseParcelableArray("android.support.actionExtras", actionExtrasMap2);
            }
            if (this.mContentView != null) {
                notification.contentView = this.mContentView;
            }
            if (this.mBigContentView != null) {
                notification.bigContentView = this.mBigContentView;
            }
            return notification;
        }
    }

    private void removeSoundAndVibration(Notification notification) {
        notification.sound = null;
        notification.vibrate = null;
        notification.defaults &= -2;
        notification.defaults &= -3;
    }
}
