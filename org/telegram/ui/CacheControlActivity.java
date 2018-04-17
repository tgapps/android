package org.telegram.ui;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import java.io.File;
import java.util.ArrayList;
import org.telegram.SQLite.SQLiteCursor;
import org.telegram.SQLite.SQLiteDatabase;
import org.telegram.SQLite.SQLitePreparedStatement;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.ClearCacheService;
import org.telegram.messenger.DataQuery;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.ImageLoader;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.beta.R;
import org.telegram.messenger.support.widget.LinearLayoutManager;
import org.telegram.messenger.support.widget.RecyclerView.ViewHolder;
import org.telegram.tgnet.TLRPC.Message;
import org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.BottomSheet.BottomSheetCell;
import org.telegram.ui.ActionBar.BottomSheet.Builder;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Cells.CheckBoxCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Cells.TextSettingsCell;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.RecyclerListView.OnItemClickListener;
import org.telegram.ui.Components.RecyclerListView.SelectionAdapter;

public class CacheControlActivity extends BaseFragment {
    private long audioSize = -1;
    private int cacheInfoRow;
    private int cacheRow;
    private long cacheSize = -1;
    private boolean calculating = true;
    private volatile boolean canceled = false;
    private boolean[] clear = new boolean[6];
    private int databaseInfoRow;
    private int databaseRow;
    private long databaseSize = -1;
    private long documentsSize = -1;
    private int keepMediaInfoRow;
    private int keepMediaRow;
    private ListAdapter listAdapter;
    private RecyclerListView listView;
    private long musicSize = -1;
    private long photoSize = -1;
    private int rowCount;
    private long totalSize = -1;
    private long videoSize = -1;

    private class ListAdapter extends SelectionAdapter {
        private Context mContext;

        public org.telegram.messenger.support.widget.RecyclerView.ViewHolder onCreateViewHolder(android.view.ViewGroup r1, int r2) {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.ui.CacheControlActivity.ListAdapter.onCreateViewHolder(android.view.ViewGroup, int):org.telegram.messenger.support.widget.RecyclerView$ViewHolder
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:116)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:249)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:256)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
Caused by: java.lang.NullPointerException
*/
            /*
            r0 = this;
            if (r4 == 0) goto L_0x000a;
        L_0x0002:
            r0 = new org.telegram.ui.Cells.TextInfoPrivacyCell;
            r1 = r2.mContext;
            r0.<init>(r1);
            goto L_0x001b;
        L_0x000a:
            r0 = new org.telegram.ui.Cells.TextSettingsCell;
            r1 = r2.mContext;
            r0.<init>(r1);
            r1 = "windowBackgroundWhite";
            r1 = org.telegram.ui.ActionBar.Theme.getColor(r1);
            r0.setBackgroundColor(r1);
            r1 = new org.telegram.ui.Components.RecyclerListView$Holder;
            r1.<init>(r0);
            return r1;
            */
            throw new UnsupportedOperationException("Method not decompiled: org.telegram.ui.CacheControlActivity.ListAdapter.onCreateViewHolder(android.view.ViewGroup, int):org.telegram.messenger.support.widget.RecyclerView$ViewHolder");
        }

        public ListAdapter(Context context) {
            this.mContext = context;
        }

        public boolean isEnabled(ViewHolder holder) {
            int position = holder.getAdapterPosition();
            if (position != CacheControlActivity.this.databaseRow && (position != CacheControlActivity.this.cacheRow || CacheControlActivity.this.totalSize <= 0)) {
                if (position != CacheControlActivity.this.keepMediaRow) {
                    return false;
                }
            }
            return true;
        }

        public int getItemCount() {
            return CacheControlActivity.this.rowCount;
        }

        public void onBindViewHolder(ViewHolder holder, int position) {
            switch (holder.getItemViewType()) {
                case 0:
                    TextSettingsCell textCell = holder.itemView;
                    if (position == CacheControlActivity.this.databaseRow) {
                        textCell.setTextAndValue(LocaleController.getString("LocalDatabase", R.string.LocalDatabase), AndroidUtilities.formatFileSize(CacheControlActivity.this.databaseSize), false);
                        return;
                    } else if (position == CacheControlActivity.this.cacheRow) {
                        if (CacheControlActivity.this.calculating) {
                            textCell.setTextAndValue(LocaleController.getString("ClearMediaCache", R.string.ClearMediaCache), LocaleController.getString("CalculatingSize", R.string.CalculatingSize), false);
                            return;
                        } else {
                            textCell.setTextAndValue(LocaleController.getString("ClearMediaCache", R.string.ClearMediaCache), CacheControlActivity.this.totalSize == 0 ? LocaleController.getString("CacheEmpty", R.string.CacheEmpty) : AndroidUtilities.formatFileSize(CacheControlActivity.this.totalSize), false);
                            return;
                        }
                    } else if (position == CacheControlActivity.this.keepMediaRow) {
                        String value;
                        int keepMedia = MessagesController.getGlobalMainSettings().getInt("keep_media", 2);
                        if (keepMedia == 0) {
                            value = LocaleController.formatPluralString("Weeks", 1);
                        } else if (keepMedia == 1) {
                            value = LocaleController.formatPluralString("Months", 1);
                        } else if (keepMedia == 3) {
                            value = LocaleController.formatPluralString("Days", 3);
                        } else {
                            value = LocaleController.getString("KeepMediaForever", R.string.KeepMediaForever);
                            textCell.setTextAndValue(LocaleController.getString("KeepMedia", R.string.KeepMedia), value, false);
                            return;
                        }
                        textCell.setTextAndValue(LocaleController.getString("KeepMedia", R.string.KeepMedia), value, false);
                        return;
                    } else {
                        return;
                    }
                case 1:
                    TextInfoPrivacyCell privacyCell = holder.itemView;
                    if (position == CacheControlActivity.this.databaseInfoRow) {
                        privacyCell.setText(LocaleController.getString("LocalDatabaseInfo", R.string.LocalDatabaseInfo));
                        privacyCell.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, R.drawable.greydivider_bottom, Theme.key_windowBackgroundGrayShadow));
                        return;
                    } else if (position == CacheControlActivity.this.cacheInfoRow) {
                        privacyCell.setText(TtmlNode.ANONYMOUS_REGION_ID);
                        privacyCell.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, R.drawable.greydivider, Theme.key_windowBackgroundGrayShadow));
                        return;
                    } else if (position == CacheControlActivity.this.keepMediaInfoRow) {
                        privacyCell.setText(AndroidUtilities.replaceTags(LocaleController.getString("KeepMediaInfo", R.string.KeepMediaInfo)));
                        privacyCell.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, R.drawable.greydivider, Theme.key_windowBackgroundGrayShadow));
                        return;
                    } else {
                        return;
                    }
                default:
                    return;
            }
        }

        public int getItemViewType(int i) {
            if (!(i == CacheControlActivity.this.databaseInfoRow || i == CacheControlActivity.this.cacheInfoRow)) {
                if (i != CacheControlActivity.this.keepMediaInfoRow) {
                    return 0;
                }
            }
            return 1;
        }
    }

    public boolean onFragmentCreate() {
        super.onFragmentCreate();
        this.rowCount = 0;
        int i = this.rowCount;
        this.rowCount = i + 1;
        this.keepMediaRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.keepMediaInfoRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.cacheRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.cacheInfoRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.databaseRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.databaseInfoRow = i;
        this.databaseSize = MessagesStorage.getInstance(this.currentAccount).getDatabaseSize();
        Utilities.globalQueue.postRunnable(new Runnable() {
            public void run() {
                CacheControlActivity.this.cacheSize = CacheControlActivity.this.getDirectorySize(FileLoader.checkDirectory(4), 0);
                if (!CacheControlActivity.this.canceled) {
                    CacheControlActivity.this.photoSize = CacheControlActivity.this.getDirectorySize(FileLoader.checkDirectory(0), 0);
                    if (!CacheControlActivity.this.canceled) {
                        CacheControlActivity.this.videoSize = CacheControlActivity.this.getDirectorySize(FileLoader.checkDirectory(2), 0);
                        if (!CacheControlActivity.this.canceled) {
                            CacheControlActivity.this.documentsSize = CacheControlActivity.this.getDirectorySize(FileLoader.checkDirectory(3), 1);
                            if (!CacheControlActivity.this.canceled) {
                                CacheControlActivity.this.musicSize = CacheControlActivity.this.getDirectorySize(FileLoader.checkDirectory(3), 2);
                                if (!CacheControlActivity.this.canceled) {
                                    CacheControlActivity.this.audioSize = CacheControlActivity.this.getDirectorySize(FileLoader.checkDirectory(1), 0);
                                    CacheControlActivity.this.totalSize = ((((CacheControlActivity.this.cacheSize + CacheControlActivity.this.videoSize) + CacheControlActivity.this.audioSize) + CacheControlActivity.this.photoSize) + CacheControlActivity.this.documentsSize) + CacheControlActivity.this.musicSize;
                                    AndroidUtilities.runOnUIThread(new Runnable() {
                                        public void run() {
                                            CacheControlActivity.this.calculating = false;
                                            if (CacheControlActivity.this.listAdapter != null) {
                                                CacheControlActivity.this.listAdapter.notifyDataSetChanged();
                                            }
                                        }
                                    });
                                }
                            }
                        }
                    }
                }
            }
        });
        return true;
    }

    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        this.canceled = true;
    }

    private long getDirectorySize(File dir, int documentsMusicType) {
        if (dir != null) {
            if (!this.canceled) {
                long size = 0;
                if (dir.isDirectory()) {
                    size = Utilities.getDirSize(dir.getAbsolutePath(), documentsMusicType);
                } else if (dir.isFile()) {
                    size = 0 + dir.length();
                }
                return size;
            }
        }
        return 0;
    }

    private void cleanupFolders() {
        final AlertDialog progressDialog = new AlertDialog(getParentActivity(), 1);
        progressDialog.setMessage(LocaleController.getString("Loading", R.string.Loading));
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.show();
        Utilities.globalQueue.postRunnable(new Runnable() {
            public void run() {
                boolean imagesCleared = false;
                for (int a = 0; a < 6; a++) {
                    if (CacheControlActivity.this.clear[a]) {
                        int type = -1;
                        int documentsMusicType = 0;
                        if (a == 0) {
                            type = 0;
                        } else if (a == 1) {
                            type = 2;
                        } else if (a == 2) {
                            type = 3;
                            documentsMusicType = 1;
                        } else if (a == 3) {
                            type = 3;
                            documentsMusicType = 2;
                        } else if (a == 4) {
                            type = 1;
                        } else if (a == 5) {
                            type = 4;
                        }
                        if (type != -1) {
                            File file = FileLoader.checkDirectory(type);
                            if (file != null) {
                                Utilities.clearDir(file.getAbsolutePath(), documentsMusicType, Long.MAX_VALUE);
                            }
                            if (type == 4) {
                                CacheControlActivity.this.cacheSize = CacheControlActivity.this.getDirectorySize(FileLoader.checkDirectory(4), documentsMusicType);
                                imagesCleared = true;
                            } else if (type == 1) {
                                CacheControlActivity.this.audioSize = CacheControlActivity.this.getDirectorySize(FileLoader.checkDirectory(1), documentsMusicType);
                            } else if (type == 3) {
                                if (documentsMusicType == 1) {
                                    CacheControlActivity.this.documentsSize = CacheControlActivity.this.getDirectorySize(FileLoader.checkDirectory(3), documentsMusicType);
                                } else {
                                    CacheControlActivity.this.musicSize = CacheControlActivity.this.getDirectorySize(FileLoader.checkDirectory(3), documentsMusicType);
                                }
                            } else if (type == 0) {
                                imagesCleared = true;
                                CacheControlActivity.this.photoSize = CacheControlActivity.this.getDirectorySize(FileLoader.checkDirectory(0), documentsMusicType);
                            } else if (type == 2) {
                                CacheControlActivity.this.videoSize = CacheControlActivity.this.getDirectorySize(FileLoader.checkDirectory(2), documentsMusicType);
                            }
                        }
                    }
                }
                final boolean imagesClearedFinal = imagesCleared;
                CacheControlActivity.this.totalSize = ((((CacheControlActivity.this.cacheSize + CacheControlActivity.this.videoSize) + CacheControlActivity.this.audioSize) + CacheControlActivity.this.photoSize) + CacheControlActivity.this.documentsSize) + CacheControlActivity.this.musicSize;
                AndroidUtilities.runOnUIThread(new Runnable() {
                    public void run() {
                        if (imagesClearedFinal) {
                            ImageLoader.getInstance().clearMemory();
                        }
                        if (CacheControlActivity.this.listAdapter != null) {
                            CacheControlActivity.this.listAdapter.notifyDataSetChanged();
                        }
                        try {
                            progressDialog.dismiss();
                        } catch (Throwable e) {
                            FileLog.e(e);
                        }
                    }
                });
            }
        });
    }

    public View createView(Context context) {
        this.actionBar.setBackButtonImage(R.drawable.ic_ab_back);
        this.actionBar.setAllowOverlayTitle(true);
        this.actionBar.setTitle(LocaleController.getString("StorageUsage", R.string.StorageUsage));
        this.actionBar.setActionBarMenuOnItemClick(new ActionBarMenuOnItemClick() {
            public void onItemClick(int id) {
                if (id == -1) {
                    CacheControlActivity.this.finishFragment();
                }
            }
        });
        this.listAdapter = new ListAdapter(context);
        this.fragmentView = new FrameLayout(context);
        FrameLayout frameLayout = this.fragmentView;
        frameLayout.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundGray));
        this.listView = new RecyclerListView(context);
        this.listView.setVerticalScrollBarEnabled(false);
        this.listView.setLayoutManager(new LinearLayoutManager(context, 1, false));
        frameLayout.addView(this.listView, LayoutHelper.createFrame(-1, -1.0f));
        this.listView.setAdapter(this.listAdapter);
        this.listView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(View view, int position) {
                int i = position;
                if (CacheControlActivity.this.getParentActivity() != null) {
                    int i2 = 2;
                    int i3 = 4;
                    Builder builder;
                    if (i == CacheControlActivity.this.keepMediaRow) {
                        builder = new Builder(CacheControlActivity.this.getParentActivity());
                        builder.setItems(new CharSequence[]{LocaleController.formatPluralString("Days", 3), LocaleController.formatPluralString("Weeks", 1), LocaleController.formatPluralString("Months", 1), LocaleController.getString("KeepMediaForever", R.string.KeepMediaForever)}, new OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                AnonymousClass1 anonymousClass1 = this;
                                int i = which;
                                Editor editor = MessagesController.getGlobalMainSettings().edit();
                                if (i == 0) {
                                    editor.putInt("keep_media", 3).commit();
                                } else if (i == 1) {
                                    editor.putInt("keep_media", 0).commit();
                                } else if (i == 2) {
                                    editor.putInt("keep_media", 1).commit();
                                } else if (i == 3) {
                                    editor.putInt("keep_media", 2).commit();
                                }
                                if (CacheControlActivity.this.listAdapter != null) {
                                    CacheControlActivity.this.listAdapter.notifyDataSetChanged();
                                }
                                PendingIntent pintent = PendingIntent.getService(ApplicationLoader.applicationContext, 1, new Intent(ApplicationLoader.applicationContext, ClearCacheService.class), 0);
                                AlarmManager alarmManager = (AlarmManager) ApplicationLoader.applicationContext.getSystemService("alarm");
                                if (i == 2) {
                                    alarmManager.cancel(pintent);
                                } else {
                                    alarmManager.setInexactRepeating(2, 86400000, 86400000, pintent);
                                }
                            }
                        });
                        CacheControlActivity.this.showDialog(builder.create());
                    } else if (i == CacheControlActivity.this.databaseRow) {
                        AlertDialog.Builder builder2 = new AlertDialog.Builder(CacheControlActivity.this.getParentActivity());
                        builder2.setTitle(LocaleController.getString("AppName", R.string.AppName));
                        builder2.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null);
                        builder2.setMessage(LocaleController.getString("LocalDatabaseClear", R.string.LocalDatabaseClear));
                        builder2.setPositiveButton(LocaleController.getString("CacheClear", R.string.CacheClear), new OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                final AlertDialog progressDialog = new AlertDialog(CacheControlActivity.this.getParentActivity(), 1);
                                progressDialog.setMessage(LocaleController.getString("Loading", R.string.Loading));
                                progressDialog.setCanceledOnTouchOutside(false);
                                progressDialog.setCancelable(false);
                                progressDialog.show();
                                MessagesStorage.getInstance(CacheControlActivity.this.currentAccount).getStorageQueue().postRunnable(new Runnable() {
                                    public void run() {
                                        SQLiteDatabase database;
                                        ArrayList<Long> dialogsToCleanup;
                                        int i;
                                        StringBuilder ids;
                                        int i2;
                                        SQLitePreparedStatement state5;
                                        SQLitePreparedStatement state6;
                                        int a;
                                        Long did;
                                        SQLiteCursor cursor;
                                        int a2;
                                        StringBuilder ids2;
                                        int messageId;
                                        Throwable e;
                                        Runnable anonymousClass1;
                                        try {
                                            database = MessagesStorage.getInstance(CacheControlActivity.this.currentAccount).getDatabase();
                                            dialogsToCleanup = new ArrayList();
                                            i = 0;
                                            SQLiteCursor cursor2 = database.queryFinalized("SELECT did FROM dialogs WHERE 1", new Object[0]);
                                            ids = new StringBuilder();
                                            while (true) {
                                                i2 = 1;
                                                if (!cursor2.next()) {
                                                    break;
                                                }
                                                long did2 = cursor2.longValue(0);
                                                int high_id = (int) (did2 >> 32);
                                                if (!(((int) did2) == 0 || high_id == 1)) {
                                                    dialogsToCleanup.add(Long.valueOf(did2));
                                                }
                                            }
                                            cursor2.dispose();
                                            state5 = database.executeFast("REPLACE INTO messages_holes VALUES(?, ?, ?)");
                                            state6 = database.executeFast("REPLACE INTO media_holes_v2 VALUES(?, ?, ?, ?)");
                                            database.beginTransaction();
                                            a = 0;
                                            while (a < dialogsToCleanup.size()) {
                                                ArrayList<Long> dialogsToCleanup2;
                                                did = (Long) dialogsToCleanup.get(a);
                                                int messagesCount = 0;
                                                StringBuilder stringBuilder = new StringBuilder();
                                                stringBuilder.append("SELECT COUNT(mid) FROM messages WHERE uid = ");
                                                stringBuilder.append(did);
                                                cursor = database.queryFinalized(stringBuilder.toString(), new Object[i]);
                                                if (cursor.next()) {
                                                    messagesCount = cursor.intValue(i);
                                                }
                                                cursor.dispose();
                                                if (messagesCount <= 2) {
                                                    dialogsToCleanup2 = dialogsToCleanup;
                                                    a2 = a;
                                                    ids2 = ids;
                                                } else {
                                                    stringBuilder = new StringBuilder();
                                                    stringBuilder.append("SELECT last_mid_i, last_mid FROM dialogs WHERE did = ");
                                                    stringBuilder.append(did);
                                                    cursor = database.queryFinalized(stringBuilder.toString(), new Object[i]);
                                                    messageId = -1;
                                                    if (cursor.next()) {
                                                        long last_mid_i = cursor.longValue(i);
                                                        long last_mid = cursor.longValue(i2);
                                                        StringBuilder stringBuilder2 = new StringBuilder();
                                                        stringBuilder2.append("SELECT data FROM messages WHERE uid = ");
                                                        stringBuilder2.append(did);
                                                        stringBuilder2.append(" AND mid IN (");
                                                        stringBuilder2.append(last_mid_i);
                                                        stringBuilder2.append(",");
                                                        ids2 = ids;
                                                        long last_mid2 = last_mid;
                                                        stringBuilder2.append(last_mid2);
                                                        dialogsToCleanup2 = dialogsToCleanup;
                                                        stringBuilder2.append(")");
                                                        dialogsToCleanup = database.queryFinalized(stringBuilder2.toString(), new Object[0]);
                                                        while (dialogsToCleanup.next()) {
                                                            try {
                                                                messagesCount = dialogsToCleanup.byteBufferValue(0);
                                                                if (messagesCount != 0) {
                                                                    a2 = a;
                                                                    try {
                                                                        a = Message.TLdeserialize(messagesCount, messagesCount.readInt32(false), false);
                                                                        a.readAttachPath(messagesCount, UserConfig.getInstance(CacheControlActivity.this.currentAccount).clientUserId);
                                                                        messagesCount.reuse();
                                                                        if (a != 0) {
                                                                            messageId = a.id;
                                                                        }
                                                                    } catch (Throwable e2) {
                                                                        e = e2;
                                                                    }
                                                                } else {
                                                                    a2 = a;
                                                                }
                                                                a = a2;
                                                            } catch (Throwable e22) {
                                                                a2 = a;
                                                                e = e22;
                                                            }
                                                        }
                                                        a2 = a;
                                                        dialogsToCleanup.dispose();
                                                        StringBuilder stringBuilder3 = new StringBuilder();
                                                        stringBuilder3.append("DELETE FROM messages WHERE uid = ");
                                                        stringBuilder3.append(did);
                                                        stringBuilder3.append(" AND mid != ");
                                                        stringBuilder3.append(last_mid_i);
                                                        stringBuilder3.append(" AND mid != ");
                                                        stringBuilder3.append(last_mid2);
                                                        database.executeFast(stringBuilder3.toString()).stepThis().dispose();
                                                        stringBuilder3 = new StringBuilder();
                                                        stringBuilder3.append("DELETE FROM messages_holes WHERE uid = ");
                                                        stringBuilder3.append(did);
                                                        database.executeFast(stringBuilder3.toString()).stepThis().dispose();
                                                        stringBuilder3 = new StringBuilder();
                                                        stringBuilder3.append("DELETE FROM bot_keyboard WHERE uid = ");
                                                        stringBuilder3.append(did);
                                                        database.executeFast(stringBuilder3.toString()).stepThis().dispose();
                                                        stringBuilder3 = new StringBuilder();
                                                        stringBuilder3.append("DELETE FROM media_counts_v2 WHERE uid = ");
                                                        stringBuilder3.append(did);
                                                        database.executeFast(stringBuilder3.toString()).stepThis().dispose();
                                                        stringBuilder3 = new StringBuilder();
                                                        stringBuilder3.append("DELETE FROM media_v2 WHERE uid = ");
                                                        stringBuilder3.append(did);
                                                        database.executeFast(stringBuilder3.toString()).stepThis().dispose();
                                                        stringBuilder3 = new StringBuilder();
                                                        stringBuilder3.append("DELETE FROM media_holes_v2 WHERE uid = ");
                                                        stringBuilder3.append(did);
                                                        database.executeFast(stringBuilder3.toString()).stepThis().dispose();
                                                        DataQuery.getInstance(CacheControlActivity.this.currentAccount).clearBotKeyboard(did.longValue(), null);
                                                        if (messageId != -1) {
                                                            MessagesStorage.createFirstHoles(did.longValue(), state5, state6, messageId);
                                                        }
                                                    } else {
                                                        dialogsToCleanup2 = dialogsToCleanup;
                                                        a2 = a;
                                                        ids2 = ids;
                                                        int i3 = messagesCount;
                                                    }
                                                    cursor.dispose();
                                                }
                                                a = a2 + 1;
                                                ids = ids2;
                                                dialogsToCleanup = dialogsToCleanup2;
                                                i = 0;
                                                i2 = 1;
                                            }
                                            ids2 = ids;
                                            StringBuilder stringBuilder4 = new StringBuilder();
                                            stringBuilder4.append("DELETE FROM messages WHERE uid = ");
                                            stringBuilder4.append(-1117744619);
                                            database.executeFast(stringBuilder4.toString()).stepThis().dispose();
                                            stringBuilder4 = new StringBuilder();
                                            stringBuilder4.append("DELETE FROM messages_holes WHERE uid = ");
                                            stringBuilder4.append(-1117744619);
                                            database.executeFast(stringBuilder4.toString()).stepThis().dispose();
                                            stringBuilder4 = new StringBuilder();
                                            stringBuilder4.append("DELETE FROM bot_keyboard WHERE uid = ");
                                            stringBuilder4.append(-1117744619);
                                            database.executeFast(stringBuilder4.toString()).stepThis().dispose();
                                            stringBuilder4 = new StringBuilder();
                                            stringBuilder4.append("DELETE FROM media_counts_v2 WHERE uid = ");
                                            stringBuilder4.append(-1117744619);
                                            database.executeFast(stringBuilder4.toString()).stepThis().dispose();
                                            stringBuilder4 = new StringBuilder();
                                            stringBuilder4.append("DELETE FROM media_v2 WHERE uid = ");
                                            stringBuilder4.append(-1117744619);
                                            database.executeFast(stringBuilder4.toString()).stepThis().dispose();
                                            stringBuilder4 = new StringBuilder();
                                            stringBuilder4.append("DELETE FROM media_holes_v2 WHERE uid = ");
                                            stringBuilder4.append(-1117744619);
                                            database.executeFast(stringBuilder4.toString()).stepThis().dispose();
                                            state5.dispose();
                                            state6.dispose();
                                            database.commitTransaction();
                                            database.executeFast("PRAGMA journal_size_limit = 0").stepThis().dispose();
                                            database.executeFast("VACUUM").stepThis().dispose();
                                            database.executeFast("PRAGMA journal_size_limit = -1").stepThis().dispose();
                                            anonymousClass1 = new Runnable() {
                                                public void run() {
                                                    try {
                                                        progressDialog.dismiss();
                                                    } catch (Throwable e) {
                                                        FileLog.e(e);
                                                    }
                                                    if (CacheControlActivity.this.listAdapter != null) {
                                                        CacheControlActivity.this.databaseSize = MessagesStorage.getInstance(CacheControlActivity.this.currentAccount).getDatabaseSize();
                                                        CacheControlActivity.this.listAdapter.notifyDataSetChanged();
                                                    }
                                                }
                                            };
                                        } catch (Throwable e222) {
                                            FileLog.e(e222);
                                            anonymousClass1 = /* anonymous class already generated */;
                                        } catch (Throwable e2222) {
                                            Throwable th = e2222;
                                            AndroidUtilities.runOnUIThread(/* anonymous class already generated */);
                                        }
                                        AndroidUtilities.runOnUIThread(anonymousClass1);
                                        return;
                                        FileLog.e(e);
                                        dialogsToCleanup.dispose();
                                        StringBuilder stringBuilder32 = new StringBuilder();
                                        stringBuilder32.append("DELETE FROM messages WHERE uid = ");
                                        stringBuilder32.append(did);
                                        stringBuilder32.append(" AND mid != ");
                                        stringBuilder32.append(last_mid_i);
                                        stringBuilder32.append(" AND mid != ");
                                        stringBuilder32.append(last_mid2);
                                        database.executeFast(stringBuilder32.toString()).stepThis().dispose();
                                        stringBuilder32 = new StringBuilder();
                                        stringBuilder32.append("DELETE FROM messages_holes WHERE uid = ");
                                        stringBuilder32.append(did);
                                        database.executeFast(stringBuilder32.toString()).stepThis().dispose();
                                        stringBuilder32 = new StringBuilder();
                                        stringBuilder32.append("DELETE FROM bot_keyboard WHERE uid = ");
                                        stringBuilder32.append(did);
                                        database.executeFast(stringBuilder32.toString()).stepThis().dispose();
                                        stringBuilder32 = new StringBuilder();
                                        stringBuilder32.append("DELETE FROM media_counts_v2 WHERE uid = ");
                                        stringBuilder32.append(did);
                                        database.executeFast(stringBuilder32.toString()).stepThis().dispose();
                                        stringBuilder32 = new StringBuilder();
                                        stringBuilder32.append("DELETE FROM media_v2 WHERE uid = ");
                                        stringBuilder32.append(did);
                                        database.executeFast(stringBuilder32.toString()).stepThis().dispose();
                                        stringBuilder32 = new StringBuilder();
                                        stringBuilder32.append("DELETE FROM media_holes_v2 WHERE uid = ");
                                        stringBuilder32.append(did);
                                        database.executeFast(stringBuilder32.toString()).stepThis().dispose();
                                        DataQuery.getInstance(CacheControlActivity.this.currentAccount).clearBotKeyboard(did.longValue(), null);
                                        if (messageId != -1) {
                                            MessagesStorage.createFirstHoles(did.longValue(), state5, state6, messageId);
                                        }
                                        cursor.dispose();
                                        a = a2 + 1;
                                        ids = ids2;
                                        dialogsToCleanup = dialogsToCleanup2;
                                        i = 0;
                                        i2 = 1;
                                    }
                                });
                            }
                        });
                        CacheControlActivity.this.showDialog(builder2.create());
                    } else {
                        if (i == CacheControlActivity.this.cacheRow && CacheControlActivity.this.totalSize > 0) {
                            if (CacheControlActivity.this.getParentActivity() != null) {
                                builder = new Builder(CacheControlActivity.this.getParentActivity());
                                builder.setApplyTopPadding(false);
                                builder.setApplyBottomPadding(false);
                                LinearLayout linearLayout = new LinearLayout(CacheControlActivity.this.getParentActivity());
                                linearLayout.setOrientation(1);
                                int a = 0;
                                while (a < 6) {
                                    long size = 0;
                                    long size2 = null;
                                    if (a == 0) {
                                        size = CacheControlActivity.this.photoSize;
                                        size2 = LocaleController.getString("LocalPhotoCache", R.string.LocalPhotoCache);
                                    } else if (a == 1) {
                                        size = CacheControlActivity.this.videoSize;
                                        size2 = LocaleController.getString("LocalVideoCache", R.string.LocalVideoCache);
                                    } else if (a == i2) {
                                        size = CacheControlActivity.this.documentsSize;
                                        size2 = LocaleController.getString("LocalDocumentCache", R.string.LocalDocumentCache);
                                    } else if (a == 3) {
                                        size = CacheControlActivity.this.musicSize;
                                        size2 = LocaleController.getString("LocalMusicCache", R.string.LocalMusicCache);
                                    } else if (a == i3) {
                                        size = CacheControlActivity.this.audioSize;
                                        size2 = LocaleController.getString("LocalAudioCache", R.string.LocalAudioCache);
                                    } else if (a == 5) {
                                        size = CacheControlActivity.this.cacheSize;
                                        size2 = LocaleController.getString("LocalCache", R.string.LocalCache);
                                    }
                                    String name = size2;
                                    size2 = size;
                                    if (size2 > 0) {
                                        CacheControlActivity.this.clear[a] = true;
                                        CheckBoxCell checkBoxCell = new CheckBoxCell(CacheControlActivity.this.getParentActivity(), 1);
                                        checkBoxCell.setTag(Integer.valueOf(a));
                                        checkBoxCell.setBackgroundDrawable(Theme.getSelectorDrawable(false));
                                        linearLayout.addView(checkBoxCell, LayoutHelper.createLinear(-1, 48));
                                        checkBoxCell.setText(name, AndroidUtilities.formatFileSize(size2), true, true);
                                        checkBoxCell.setTextColor(Theme.getColor(Theme.key_dialogTextBlack));
                                        checkBoxCell.setOnClickListener(new View.OnClickListener() {
                                            public void onClick(View v) {
                                                CheckBoxCell cell = (CheckBoxCell) v;
                                                int num = ((Integer) cell.getTag()).intValue();
                                                CacheControlActivity.this.clear[num] = CacheControlActivity.this.clear[num] ^ true;
                                                cell.setChecked(CacheControlActivity.this.clear[num], true);
                                            }
                                        });
                                    } else {
                                        CacheControlActivity.this.clear[a] = false;
                                    }
                                    a++;
                                    i2 = 2;
                                    i3 = 4;
                                }
                                BottomSheetCell cell = new BottomSheetCell(CacheControlActivity.this.getParentActivity(), 1);
                                cell.setBackgroundDrawable(Theme.getSelectorDrawable(false));
                                cell.setTextAndIcon(LocaleController.getString("ClearMediaCache", R.string.ClearMediaCache).toUpperCase(), 0);
                                cell.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteRedText));
                                cell.setOnClickListener(new View.OnClickListener() {
                                    public void onClick(View v) {
                                        try {
                                            if (CacheControlActivity.this.visibleDialog != null) {
                                                CacheControlActivity.this.visibleDialog.dismiss();
                                            }
                                        } catch (Throwable e) {
                                            FileLog.e(e);
                                        }
                                        CacheControlActivity.this.cleanupFolders();
                                    }
                                });
                                linearLayout.addView(cell, LayoutHelper.createLinear(-1, 48));
                                builder.setCustomView(linearLayout);
                                CacheControlActivity.this.showDialog(builder.create());
                            }
                        }
                    }
                }
            }
        });
        return this.fragmentView;
    }

    public void onResume() {
        super.onResume();
        if (this.listAdapter != null) {
            this.listAdapter.notifyDataSetChanged();
        }
    }

    public ThemeDescription[] getThemeDescriptions() {
        r1 = new ThemeDescription[12];
        r1[0] = new ThemeDescription(this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{TextSettingsCell.class}, null, null, null, Theme.key_windowBackgroundWhite);
        r1[1] = new ThemeDescription(this.fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_windowBackgroundGray);
        r1[2] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_actionBarDefault);
        r1[3] = new ThemeDescription(this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, Theme.key_actionBarDefault);
        r1[4] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, Theme.key_actionBarDefaultIcon);
        r1[5] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, Theme.key_actionBarDefaultTitle);
        r1[6] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, Theme.key_actionBarDefaultSelector);
        r1[7] = new ThemeDescription(this.listView, ThemeDescription.FLAG_SELECTOR, null, null, null, null, Theme.key_listSelector);
        r1[8] = new ThemeDescription(this.listView, 0, new Class[]{TextSettingsCell.class}, new String[]{"textView"}, null, null, null, Theme.key_windowBackgroundWhiteBlackText);
        r1[9] = new ThemeDescription(this.listView, 0, new Class[]{TextSettingsCell.class}, new String[]{"valueTextView"}, null, null, null, Theme.key_windowBackgroundWhiteValueText);
        r1[10] = new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{TextInfoPrivacyCell.class}, null, null, null, Theme.key_windowBackgroundGrayShadow);
        r1[11] = new ThemeDescription(this.listView, 0, new Class[]{TextInfoPrivacyCell.class}, new String[]{"textView"}, null, null, null, Theme.key_windowBackgroundWhiteGrayText4);
        return r1;
    }
}
