package org.telegram.ui;

import android.content.Context;
import android.util.LongSparseArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import java.util.ArrayList;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaController;
import org.telegram.messenger.MediaController.AudioEntry;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.NotificationCenter.NotificationCenterDelegate;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.beta.R;
import org.telegram.messenger.support.widget.LinearLayoutManager;
import org.telegram.messenger.support.widget.RecyclerView.Adapter;
import org.telegram.messenger.support.widget.RecyclerView.ViewHolder;
import org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Cells.AudioCell;
import org.telegram.ui.Cells.AudioCell.AudioCellDelegate;
import org.telegram.ui.Components.EmptyTextProgressView;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.PickerBottomLayout;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.RecyclerListView.Holder;
import org.telegram.ui.Components.RecyclerListView.OnItemClickListener;
import org.telegram.ui.Components.RecyclerListView.SelectionAdapter;

public class AudioSelectActivity extends BaseFragment implements NotificationCenterDelegate {
    private ArrayList<AudioEntry> audioEntries = new ArrayList();
    private PickerBottomLayout bottomLayout;
    private AudioSelectActivityDelegate delegate;
    private RecyclerListView listView;
    private ListAdapter listViewAdapter;
    private boolean loadingAudio;
    private MessageObject playingAudio;
    private EmptyTextProgressView progressView;
    private LongSparseArray<AudioEntry> selectedAudios = new LongSparseArray();
    private View shadow;

    public interface AudioSelectActivityDelegate {
        void didSelectAudio(ArrayList<MessageObject> arrayList);
    }

    private class ListAdapter extends SelectionAdapter {
        private Context mContext;

        public ListAdapter(Context context) {
            this.mContext = context;
        }

        public int getItemCount() {
            return AudioSelectActivity.this.audioEntries.size();
        }

        public Object getItem(int i) {
            return AudioSelectActivity.this.audioEntries.get(i);
        }

        public long getItemId(int i) {
            return (long) i;
        }

        public boolean isEnabled(ViewHolder holder) {
            return true;
        }

        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            AudioCell view = new AudioCell(this.mContext);
            view.setDelegate(new AudioCellDelegate() {
                public void startedPlayingAudio(MessageObject messageObject) {
                    AudioSelectActivity.this.playingAudio = messageObject;
                }
            });
            return new Holder(view);
        }

        public void onBindViewHolder(ViewHolder holder, int position) {
            AudioEntry audioEntry = (AudioEntry) AudioSelectActivity.this.audioEntries.get(position);
            AudioCell audioCell = (AudioCell) holder.itemView;
            AudioEntry audioEntry2 = (AudioEntry) AudioSelectActivity.this.audioEntries.get(position);
            boolean z = true;
            boolean z2 = position != AudioSelectActivity.this.audioEntries.size() - 1;
            if (AudioSelectActivity.this.selectedAudios.indexOfKey(audioEntry.id) < 0) {
                z = false;
            }
            audioCell.setAudio(audioEntry2, z2, z);
        }

        public int getItemViewType(int i) {
            return 0;
        }
    }

    public boolean onFragmentCreate() {
        super.onFragmentCreate();
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.closeChats);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.messagePlayingDidReset);
        loadAudio();
        return true;
    }

    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.closeChats);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.messagePlayingDidReset);
        if (this.playingAudio != null && MediaController.getInstance().isPlayingMessage(this.playingAudio)) {
            MediaController.getInstance().cleanupPlayer(true, true);
        }
    }

    public View createView(Context context) {
        this.actionBar.setBackButtonImage(R.drawable.ic_ab_back);
        int i = 1;
        this.actionBar.setAllowOverlayTitle(true);
        this.actionBar.setTitle(LocaleController.getString("AttachMusic", R.string.AttachMusic));
        this.actionBar.setActionBarMenuOnItemClick(new ActionBarMenuOnItemClick() {
            public void onItemClick(int id) {
                if (id == -1) {
                    AudioSelectActivity.this.finishFragment();
                }
            }
        });
        this.fragmentView = new FrameLayout(context);
        FrameLayout frameLayout = this.fragmentView;
        this.progressView = new EmptyTextProgressView(context);
        this.progressView.setText(LocaleController.getString("NoAudio", R.string.NoAudio));
        frameLayout.addView(this.progressView, LayoutHelper.createFrame(-1, -1.0f));
        this.listView = new RecyclerListView(context);
        this.listView.setEmptyView(this.progressView);
        this.listView.setVerticalScrollBarEnabled(false);
        this.listView.setLayoutManager(new LinearLayoutManager(context, 1, false));
        RecyclerListView recyclerListView = this.listView;
        Adapter listAdapter = new ListAdapter(context);
        this.listViewAdapter = listAdapter;
        recyclerListView.setAdapter(listAdapter);
        recyclerListView = this.listView;
        if (!LocaleController.isRTL) {
            i = 2;
        }
        recyclerListView.setVerticalScrollbarPosition(i);
        frameLayout.addView(this.listView, LayoutHelper.createFrame(-1, -1.0f, 51, 0.0f, 0.0f, 0.0f, 48.0f));
        this.listView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(View view, int position) {
                AudioCell audioCell = (AudioCell) view;
                AudioEntry audioEntry = audioCell.getAudioEntry();
                if (AudioSelectActivity.this.selectedAudios.indexOfKey(audioEntry.id) >= 0) {
                    AudioSelectActivity.this.selectedAudios.remove(audioEntry.id);
                    audioCell.setChecked(false);
                } else {
                    AudioSelectActivity.this.selectedAudios.put(audioEntry.id, audioEntry);
                    audioCell.setChecked(true);
                }
                AudioSelectActivity.this.updateBottomLayoutCount();
            }
        });
        this.bottomLayout = new PickerBottomLayout(context, false);
        frameLayout.addView(this.bottomLayout, LayoutHelper.createFrame(-1, 48, 80));
        this.bottomLayout.cancelButton.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                AudioSelectActivity.this.finishFragment();
            }
        });
        this.bottomLayout.doneButton.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (AudioSelectActivity.this.delegate != null) {
                    ArrayList<MessageObject> audios = new ArrayList();
                    for (int a = 0; a < AudioSelectActivity.this.selectedAudios.size(); a++) {
                        audios.add(((AudioEntry) AudioSelectActivity.this.selectedAudios.valueAt(a)).messageObject);
                    }
                    AudioSelectActivity.this.delegate.didSelectAudio(audios);
                }
                AudioSelectActivity.this.finishFragment();
            }
        });
        View shadow = new View(context);
        shadow.setBackgroundResource(R.drawable.header_shadow_reverse);
        frameLayout.addView(shadow, LayoutHelper.createFrame(-1, 3.0f, 83, 0.0f, 0.0f, 0.0f, 48.0f));
        if (this.loadingAudio) {
            this.progressView.showProgress();
        } else {
            this.progressView.showTextView();
        }
        updateBottomLayoutCount();
        return this.fragmentView;
    }

    public void didReceivedNotification(int id, int account, Object... args) {
        if (id == NotificationCenter.closeChats) {
            removeSelfFromStack();
        } else if (id == NotificationCenter.messagePlayingDidReset && this.listViewAdapter != null) {
            this.listViewAdapter.notifyDataSetChanged();
        }
    }

    private void updateBottomLayoutCount() {
        this.bottomLayout.updateSelectedCount(this.selectedAudios.size(), true);
    }

    public void setDelegate(AudioSelectActivityDelegate audioSelectActivityDelegate) {
        this.delegate = audioSelectActivityDelegate;
    }

    private void loadAudio() {
        this.loadingAudio = true;
        if (this.progressView != null) {
            this.progressView.showProgress();
        }
        Utilities.globalQueue.postRunnable(new Runnable() {
            /* JADX WARNING: inconsistent code. */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public void run() {
                /*
                r21 = this;
                r1 = r21;
                r2 = 6;
                r5 = new java.lang.String[r2];
                r2 = "_id";
                r9 = 0;
                r5[r9] = r2;
                r2 = "artist";
                r10 = 1;
                r5[r10] = r2;
                r2 = "title";
                r11 = 2;
                r5[r11] = r2;
                r2 = "_data";
                r12 = 3;
                r5[r12] = r2;
                r2 = "duration";
                r13 = 4;
                r5[r13] = r2;
                r2 = "album";
                r14 = 5;
                r5[r14] = r2;
                r2 = new java.util.ArrayList;
                r2.<init>();
                r3 = 0;
                r15 = r3;
                r3 = org.telegram.messenger.ApplicationLoader.applicationContext;	 Catch:{ Exception -> 0x0183 }
                r3 = r3.getContentResolver();	 Catch:{ Exception -> 0x0183 }
                r4 = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;	 Catch:{ Exception -> 0x0183 }
                r6 = "is_music != 0";
                r7 = 0;
                r8 = "title";
                r3 = r3.query(r4, r5, r6, r7, r8);	 Catch:{ Exception -> 0x0183 }
                r15 = r3;
                r3 = -2000000000; // 0xffffffff88ca6c00 float:-1.2182823E-33 double:NaN;
            L_0x003f:
                r4 = r15.moveToNext();	 Catch:{ Exception -> 0x0183 }
                if (r4 == 0) goto L_0x017a;
            L_0x0045:
                r4 = new org.telegram.messenger.MediaController$AudioEntry;	 Catch:{ Exception -> 0x0183 }
                r4.<init>();	 Catch:{ Exception -> 0x0183 }
                r6 = r15.getInt(r9);	 Catch:{ Exception -> 0x0183 }
                r6 = (long) r6;	 Catch:{ Exception -> 0x0183 }
                r4.id = r6;	 Catch:{ Exception -> 0x0183 }
                r6 = r15.getString(r10);	 Catch:{ Exception -> 0x0183 }
                r4.author = r6;	 Catch:{ Exception -> 0x0183 }
                r6 = r15.getString(r11);	 Catch:{ Exception -> 0x0183 }
                r4.title = r6;	 Catch:{ Exception -> 0x0183 }
                r6 = r15.getString(r12);	 Catch:{ Exception -> 0x0183 }
                r4.path = r6;	 Catch:{ Exception -> 0x0183 }
                r6 = r15.getLong(r13);	 Catch:{ Exception -> 0x0183 }
                r16 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
                r6 = r6 / r16;
                r6 = (int) r6;	 Catch:{ Exception -> 0x0183 }
                r4.duration = r6;	 Catch:{ Exception -> 0x0183 }
                r6 = r15.getString(r14);	 Catch:{ Exception -> 0x0183 }
                r4.genre = r6;	 Catch:{ Exception -> 0x0183 }
                r6 = new java.io.File;	 Catch:{ Exception -> 0x0183 }
                r7 = r4.path;	 Catch:{ Exception -> 0x0183 }
                r6.<init>(r7);	 Catch:{ Exception -> 0x0183 }
                r7 = new org.telegram.tgnet.TLRPC$TL_message;	 Catch:{ Exception -> 0x0183 }
                r7.<init>();	 Catch:{ Exception -> 0x0183 }
                r7.out = r10;	 Catch:{ Exception -> 0x0183 }
                r7.id = r3;	 Catch:{ Exception -> 0x0183 }
                r8 = new org.telegram.tgnet.TLRPC$TL_peerUser;	 Catch:{ Exception -> 0x0183 }
                r8.<init>();	 Catch:{ Exception -> 0x0183 }
                r7.to_id = r8;	 Catch:{ Exception -> 0x0183 }
                r8 = r7.to_id;	 Catch:{ Exception -> 0x0183 }
                r10 = org.telegram.ui.AudioSelectActivity.this;	 Catch:{ Exception -> 0x0183 }
                r10 = r10.currentAccount;	 Catch:{ Exception -> 0x0183 }
                r10 = org.telegram.messenger.UserConfig.getInstance(r10);	 Catch:{ Exception -> 0x0183 }
                r10 = r10.getClientUserId();	 Catch:{ Exception -> 0x0183 }
                r7.from_id = r10;	 Catch:{ Exception -> 0x0183 }
                r8.user_id = r10;	 Catch:{ Exception -> 0x0183 }
                r18 = java.lang.System.currentTimeMillis();	 Catch:{ Exception -> 0x0183 }
                r13 = r18 / r16;
                r8 = (int) r13;	 Catch:{ Exception -> 0x0183 }
                r7.date = r8;	 Catch:{ Exception -> 0x0183 }
                r8 = "";
                r7.message = r8;	 Catch:{ Exception -> 0x0183 }
                r8 = r4.path;	 Catch:{ Exception -> 0x0183 }
                r7.attachPath = r8;	 Catch:{ Exception -> 0x0183 }
                r8 = new org.telegram.tgnet.TLRPC$TL_messageMediaDocument;	 Catch:{ Exception -> 0x0183 }
                r8.<init>();	 Catch:{ Exception -> 0x0183 }
                r7.media = r8;	 Catch:{ Exception -> 0x0183 }
                r8 = r7.media;	 Catch:{ Exception -> 0x0183 }
                r10 = r8.flags;	 Catch:{ Exception -> 0x0183 }
                r10 = r10 | r12;
                r8.flags = r10;	 Catch:{ Exception -> 0x0183 }
                r8 = r7.media;	 Catch:{ Exception -> 0x0183 }
                r10 = new org.telegram.tgnet.TLRPC$TL_document;	 Catch:{ Exception -> 0x0183 }
                r10.<init>();	 Catch:{ Exception -> 0x0183 }
                r8.document = r10;	 Catch:{ Exception -> 0x0183 }
                r8 = r7.flags;	 Catch:{ Exception -> 0x0183 }
                r8 = r8 | 768;
                r7.flags = r8;	 Catch:{ Exception -> 0x0183 }
                r8 = org.telegram.messenger.FileLoader.getFileExtension(r6);	 Catch:{ Exception -> 0x0183 }
                r10 = r7.media;	 Catch:{ Exception -> 0x0183 }
                r10 = r10.document;	 Catch:{ Exception -> 0x0183 }
                r13 = 0;
                r10.id = r13;	 Catch:{ Exception -> 0x0183 }
                r10 = r7.media;	 Catch:{ Exception -> 0x0183 }
                r10 = r10.document;	 Catch:{ Exception -> 0x0183 }
                r10.access_hash = r13;	 Catch:{ Exception -> 0x0183 }
                r10 = r7.media;	 Catch:{ Exception -> 0x0183 }
                r10 = r10.document;	 Catch:{ Exception -> 0x0183 }
                r13 = r7.date;	 Catch:{ Exception -> 0x0183 }
                r10.date = r13;	 Catch:{ Exception -> 0x0183 }
                r10 = r7.media;	 Catch:{ Exception -> 0x0183 }
                r10 = r10.document;	 Catch:{ Exception -> 0x0183 }
                r13 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0183 }
                r13.<init>();	 Catch:{ Exception -> 0x0183 }
                r14 = "audio/";
                r13.append(r14);	 Catch:{ Exception -> 0x0183 }
                r14 = r8.length();	 Catch:{ Exception -> 0x0183 }
                if (r14 <= 0) goto L_0x00fd;
            L_0x00fb:
                r14 = r8;
                goto L_0x00ff;
            L_0x00fd:
                r14 = "mp3";
            L_0x00ff:
                r13.append(r14);	 Catch:{ Exception -> 0x0183 }
                r13 = r13.toString();	 Catch:{ Exception -> 0x0183 }
                r10.mime_type = r13;	 Catch:{ Exception -> 0x0183 }
                r10 = r7.media;	 Catch:{ Exception -> 0x0183 }
                r10 = r10.document;	 Catch:{ Exception -> 0x0183 }
                r13 = r6.length();	 Catch:{ Exception -> 0x0183 }
                r13 = (int) r13;	 Catch:{ Exception -> 0x0183 }
                r10.size = r13;	 Catch:{ Exception -> 0x0183 }
                r10 = r7.media;	 Catch:{ Exception -> 0x0183 }
                r10 = r10.document;	 Catch:{ Exception -> 0x0183 }
                r13 = new org.telegram.tgnet.TLRPC$TL_photoSizeEmpty;	 Catch:{ Exception -> 0x0183 }
                r13.<init>();	 Catch:{ Exception -> 0x0183 }
                r10.thumb = r13;	 Catch:{ Exception -> 0x0183 }
                r10 = r7.media;	 Catch:{ Exception -> 0x0183 }
                r10 = r10.document;	 Catch:{ Exception -> 0x0183 }
                r10 = r10.thumb;	 Catch:{ Exception -> 0x0183 }
                r13 = "s";
                r10.type = r13;	 Catch:{ Exception -> 0x0183 }
                r10 = r7.media;	 Catch:{ Exception -> 0x0183 }
                r10 = r10.document;	 Catch:{ Exception -> 0x0183 }
                r10.dc_id = r9;	 Catch:{ Exception -> 0x0183 }
                r10 = new org.telegram.tgnet.TLRPC$TL_documentAttributeAudio;	 Catch:{ Exception -> 0x0183 }
                r10.<init>();	 Catch:{ Exception -> 0x0183 }
                r13 = r4.duration;	 Catch:{ Exception -> 0x0183 }
                r10.duration = r13;	 Catch:{ Exception -> 0x0183 }
                r13 = r4.title;	 Catch:{ Exception -> 0x0183 }
                r10.title = r13;	 Catch:{ Exception -> 0x0183 }
                r13 = r4.author;	 Catch:{ Exception -> 0x0183 }
                r10.performer = r13;	 Catch:{ Exception -> 0x0183 }
                r13 = r10.flags;	 Catch:{ Exception -> 0x0183 }
                r13 = r13 | r12;
                r10.flags = r13;	 Catch:{ Exception -> 0x0183 }
                r13 = r7.media;	 Catch:{ Exception -> 0x0183 }
                r13 = r13.document;	 Catch:{ Exception -> 0x0183 }
                r13 = r13.attributes;	 Catch:{ Exception -> 0x0183 }
                r13.add(r10);	 Catch:{ Exception -> 0x0183 }
                r13 = new org.telegram.tgnet.TLRPC$TL_documentAttributeFilename;	 Catch:{ Exception -> 0x0183 }
                r13.<init>();	 Catch:{ Exception -> 0x0183 }
                r14 = r6.getName();	 Catch:{ Exception -> 0x0183 }
                r13.file_name = r14;	 Catch:{ Exception -> 0x0183 }
                r14 = r7.media;	 Catch:{ Exception -> 0x0183 }
                r14 = r14.document;	 Catch:{ Exception -> 0x0183 }
                r14 = r14.attributes;	 Catch:{ Exception -> 0x0183 }
                r14.add(r13);	 Catch:{ Exception -> 0x0183 }
                r14 = new org.telegram.messenger.MessageObject;	 Catch:{ Exception -> 0x0183 }
                r11 = org.telegram.ui.AudioSelectActivity.this;	 Catch:{ Exception -> 0x0183 }
                r11 = r11.currentAccount;	 Catch:{ Exception -> 0x0183 }
                r14.<init>(r11, r7, r9);	 Catch:{ Exception -> 0x0183 }
                r4.messageObject = r14;	 Catch:{ Exception -> 0x0183 }
                r2.add(r4);	 Catch:{ Exception -> 0x0183 }
                r3 = r3 + -1;
                r10 = 1;
                r11 = 2;
                r13 = 4;
                r14 = 5;
                goto L_0x003f;
            L_0x017a:
                if (r15 == 0) goto L_0x018b;
            L_0x017c:
                r15.close();
                goto L_0x018b;
            L_0x0180:
                r0 = move-exception;
                r3 = r0;
                goto L_0x0194;
            L_0x0183:
                r0 = move-exception;
                r3 = r0;
                org.telegram.messenger.FileLog.e(r3);	 Catch:{ all -> 0x0180 }
                if (r15 == 0) goto L_0x018b;
            L_0x018a:
                goto L_0x017c;
            L_0x018b:
                r3 = new org.telegram.ui.AudioSelectActivity$5$1;
                r3.<init>(r2);
                org.telegram.messenger.AndroidUtilities.runOnUIThread(r3);
                return;
            L_0x0194:
                if (r15 == 0) goto L_0x0199;
            L_0x0196:
                r15.close();
            L_0x0199:
                throw r3;
                */
                throw new UnsupportedOperationException("Method not decompiled: org.telegram.ui.AudioSelectActivity.5.run():void");
            }
        });
    }

    public ThemeDescription[] getThemeDescriptions() {
        r1 = new ThemeDescription[24];
        r1[7] = new ThemeDescription(this.listView, 0, new Class[]{View.class}, Theme.dividerPaint, null, null, Theme.key_divider);
        r1[8] = new ThemeDescription(this.progressView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, Theme.key_emptyListPlaceholder);
        r1[9] = new ThemeDescription(this.progressView, ThemeDescription.FLAG_PROGRESSBAR, null, null, null, null, Theme.key_progressCircle);
        r1[10] = new ThemeDescription(this.listView, 0, new Class[]{AudioCell.class}, new String[]{"titleTextView"}, null, null, null, Theme.key_windowBackgroundWhiteBlackText);
        r1[11] = new ThemeDescription(this.listView, 0, new Class[]{AudioCell.class}, new String[]{"genreTextView"}, null, null, null, Theme.key_windowBackgroundWhiteGrayText2);
        r1[12] = new ThemeDescription(this.listView, 0, new Class[]{AudioCell.class}, new String[]{"authorTextView"}, null, null, null, Theme.key_windowBackgroundWhiteGrayText2);
        r1[13] = new ThemeDescription(this.listView, 0, new Class[]{AudioCell.class}, new String[]{"timeTextView"}, null, null, null, Theme.key_windowBackgroundWhiteGrayText3);
        r1[14] = new ThemeDescription(this.listView, ThemeDescription.FLAG_CHECKBOX, new Class[]{AudioCell.class}, new String[]{"checkBox"}, null, null, null, Theme.key_musicPicker_checkbox);
        r1[15] = new ThemeDescription(this.listView, ThemeDescription.FLAG_CHECKBOXCHECK, new Class[]{AudioCell.class}, new String[]{"checkBox"}, null, null, null, Theme.key_musicPicker_checkboxCheck);
        r1[16] = new ThemeDescription(this.listView, ThemeDescription.FLAG_USEBACKGROUNDDRAWABLE, new Class[]{AudioCell.class}, new String[]{"playButton"}, null, null, null, Theme.key_musicPicker_buttonIcon);
        r1[17] = new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_USEBACKGROUNDDRAWABLE, new Class[]{AudioCell.class}, new String[]{"playButton"}, null, null, null, Theme.key_musicPicker_buttonBackground);
        r1[18] = new ThemeDescription(this.bottomLayout, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_windowBackgroundWhite);
        r1[19] = new ThemeDescription(this.bottomLayout, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{PickerBottomLayout.class}, new String[]{"cancelButton"}, null, null, null, Theme.key_picker_enabledButton);
        r1[20] = new ThemeDescription(this.bottomLayout, ThemeDescription.FLAG_TEXTCOLOR | ThemeDescription.FLAG_CHECKTAG, new Class[]{PickerBottomLayout.class}, new String[]{"doneButtonTextView"}, null, null, null, Theme.key_picker_enabledButton);
        r1[21] = new ThemeDescription(this.bottomLayout, ThemeDescription.FLAG_TEXTCOLOR | ThemeDescription.FLAG_CHECKTAG, new Class[]{PickerBottomLayout.class}, new String[]{"doneButtonTextView"}, null, null, null, Theme.key_picker_disabledButton);
        r1[22] = new ThemeDescription(this.bottomLayout, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{PickerBottomLayout.class}, new String[]{"doneButtonBadgeTextView"}, null, null, null, Theme.key_picker_badgeText);
        r1[23] = new ThemeDescription(this.bottomLayout, ThemeDescription.FLAG_USEBACKGROUNDDRAWABLE, new Class[]{PickerBottomLayout.class}, new String[]{"doneButtonBadgeTextView"}, null, null, null, Theme.key_picker_badge);
        return r1;
    }
}
