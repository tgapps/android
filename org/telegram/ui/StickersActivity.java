package org.telegram.ui;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Locale;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.DataQuery;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.NotificationCenter.NotificationCenterDelegate;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.beta.R;
import org.telegram.messenger.support.widget.LinearLayoutManager;
import org.telegram.messenger.support.widget.RecyclerView;
import org.telegram.messenger.support.widget.RecyclerView.LayoutParams;
import org.telegram.messenger.support.widget.RecyclerView.ViewHolder;
import org.telegram.messenger.support.widget.helper.ItemTouchHelper;
import org.telegram.messenger.support.widget.helper.ItemTouchHelper.Callback;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.Document;
import org.telegram.tgnet.TLRPC.TL_error;
import org.telegram.tgnet.TLRPC.TL_messages_reorderStickerSets;
import org.telegram.tgnet.TLRPC.TL_messages_stickerSet;
import org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick;
import org.telegram.ui.ActionBar.AlertDialog.Builder;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Cells.ShadowSectionCell;
import org.telegram.ui.Cells.StickerSetCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Cells.TextSettingsCell;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.RecyclerListView.Holder;
import org.telegram.ui.Components.RecyclerListView.OnItemClickListener;
import org.telegram.ui.Components.RecyclerListView.SelectionAdapter;
import org.telegram.ui.Components.StickersAlert;
import org.telegram.ui.Components.URLSpanNoUnderline;

public class StickersActivity extends BaseFragment implements NotificationCenterDelegate {
    private int archivedInfoRow;
    private int archivedRow;
    private int currentType;
    private int featuredInfoRow;
    private int featuredRow;
    private ListAdapter listAdapter;
    private RecyclerListView listView;
    private int masksInfoRow;
    private int masksRow;
    private boolean needReorder;
    private int rowCount;
    private int stickersEndRow;
    private int stickersShadowRow;
    private int stickersStartRow;
    private int suggestInfoRow;
    private int suggestRow;

    public class TouchHelperCallback extends Callback {
        public boolean isLongPressDragEnabled() {
            return true;
        }

        public int getMovementFlags(RecyclerView recyclerView, ViewHolder viewHolder) {
            if (viewHolder.getItemViewType() != 0) {
                return Callback.makeMovementFlags(0, 0);
            }
            return Callback.makeMovementFlags(3, 0);
        }

        public boolean onMove(RecyclerView recyclerView, ViewHolder source, ViewHolder target) {
            if (source.getItemViewType() != target.getItemViewType()) {
                return false;
            }
            StickersActivity.this.listAdapter.swapElements(source.getAdapterPosition(), target.getAdapterPosition());
            return true;
        }

        public void onChildDraw(Canvas c, RecyclerView recyclerView, ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }

        public void onSelectedChanged(ViewHolder viewHolder, int actionState) {
            if (actionState != 0) {
                StickersActivity.this.listView.cancelClickRunnables(false);
                viewHolder.itemView.setPressed(true);
            }
            super.onSelectedChanged(viewHolder, actionState);
        }

        public void onSwiped(ViewHolder viewHolder, int direction) {
        }

        public void clearView(RecyclerView recyclerView, ViewHolder viewHolder) {
            super.clearView(recyclerView, viewHolder);
            viewHolder.itemView.setPressed(false);
        }
    }

    private class ListAdapter extends SelectionAdapter {
        private Context mContext;

        class AnonymousClass1 extends URLSpanNoUnderline {
            AnonymousClass1(String url) {
                super(url);
            }

            public void onClick(View widget) {
                MessagesController.getInstance(StickersActivity.this.currentAccount).openByUserName("stickers", StickersActivity.this, 1);
            }
        }

        public void onBindViewHolder(org.telegram.messenger.support.widget.RecyclerView.ViewHolder r1, int r2) {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.ui.StickersActivity.ListAdapter.onBindViewHolder(org.telegram.messenger.support.widget.RecyclerView$ViewHolder, int):void
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
            r0 = r8.getItemViewType();
            r1 = 1;
            r2 = 0;
            switch(r0) {
                case 0: goto L_0x01a2;
                case 1: goto L_0x010a;
                case 2: goto L_0x003f;
                case 3: goto L_0x000b;
                default: goto L_0x0009;
            };
        L_0x0009:
            goto L_0x01d5;
        L_0x000b:
            r0 = org.telegram.ui.StickersActivity.this;
            r0 = r0.stickersShadowRow;
            if (r9 != r0) goto L_0x0025;
        L_0x0013:
            r0 = r8.itemView;
            r1 = r7.mContext;
            r2 = 2131165332; // 0x7f070094 float:1.7944878E38 double:1.052935576E-314;
            r3 = "windowBackgroundGrayShadow";
            r1 = org.telegram.ui.ActionBar.Theme.getThemedDrawable(r1, r2, r3);
            r0.setBackgroundDrawable(r1);
            goto L_0x01d5;
        L_0x0025:
            r0 = org.telegram.ui.StickersActivity.this;
            r0 = r0.suggestInfoRow;
            if (r9 != r0) goto L_0x01d5;
        L_0x002d:
            r0 = r8.itemView;
            r1 = r7.mContext;
            r2 = 2131165331; // 0x7f070093 float:1.7944876E38 double:1.0529355757E-314;
            r3 = "windowBackgroundGrayShadow";
            r1 = org.telegram.ui.ActionBar.Theme.getThemedDrawable(r1, r2, r3);
            r0.setBackgroundDrawable(r1);
            goto L_0x01d5;
        L_0x003f:
            r0 = org.telegram.ui.StickersActivity.this;
            r0 = r0.featuredRow;
            if (r9 != r0) goto L_0x007e;
        L_0x0047:
            r0 = org.telegram.ui.StickersActivity.this;
            r0 = r0.currentAccount;
            r0 = org.telegram.messenger.DataQuery.getInstance(r0);
            r0 = r0.getUnreadStickerSets();
            r0 = r0.size();
            r3 = r8.itemView;
            r3 = (org.telegram.ui.Cells.TextSettingsCell) r3;
            r4 = "FeaturedStickers";
            r5 = 2131493535; // 0x7f0c029f float:1.8610553E38 double:1.05309773E-314;
            r4 = org.telegram.messenger.LocaleController.getString(r4, r5);
            if (r0 == 0) goto L_0x0077;
        L_0x0068:
            r5 = "%d";
            r1 = new java.lang.Object[r1];
            r6 = java.lang.Integer.valueOf(r0);
            r1[r2] = r6;
            r1 = java.lang.String.format(r5, r1);
            goto L_0x0079;
        L_0x0077:
            r1 = "";
        L_0x0079:
            r3.setTextAndValue(r4, r1, r2);
            goto L_0x01d5;
        L_0x007e:
            r0 = org.telegram.ui.StickersActivity.this;
            r0 = r0.archivedRow;
            if (r9 != r0) goto L_0x00b2;
        L_0x0086:
            r0 = org.telegram.ui.StickersActivity.this;
            r0 = r0.currentType;
            if (r0 != 0) goto L_0x00a0;
        L_0x008e:
            r0 = r8.itemView;
            r0 = (org.telegram.ui.Cells.TextSettingsCell) r0;
            r1 = "ArchivedStickers";
            r3 = 2131492991; // 0x7f0c007f float:1.860945E38 double:1.053097461E-314;
            r1 = org.telegram.messenger.LocaleController.getString(r1, r3);
            r0.setText(r1, r2);
            goto L_0x01d5;
        L_0x00a0:
            r0 = r8.itemView;
            r0 = (org.telegram.ui.Cells.TextSettingsCell) r0;
            r1 = "ArchivedMasks";
            r3 = 2131492986; // 0x7f0c007a float:1.860944E38 double:1.0530974587E-314;
            r1 = org.telegram.messenger.LocaleController.getString(r1, r3);
            r0.setText(r1, r2);
            goto L_0x01d5;
        L_0x00b2:
            r0 = org.telegram.ui.StickersActivity.this;
            r0 = r0.masksRow;
            if (r9 != r0) goto L_0x00cc;
        L_0x00ba:
            r0 = r8.itemView;
            r0 = (org.telegram.ui.Cells.TextSettingsCell) r0;
            r1 = "Masks";
            r3 = 2131493789; // 0x7f0c039d float:1.8611068E38 double:1.0530978555E-314;
            r1 = org.telegram.messenger.LocaleController.getString(r1, r3);
            r0.setText(r1, r2);
            goto L_0x01d5;
        L_0x00cc:
            r0 = org.telegram.ui.StickersActivity.this;
            r0 = r0.suggestRow;
            if (r9 != r0) goto L_0x01d5;
        L_0x00d4:
            r0 = org.telegram.messenger.SharedConfig.suggestStickers;
            switch(r0) {
                case 0: goto L_0x00ed;
                case 1: goto L_0x00e3;
                default: goto L_0x00d9;
            };
        L_0x00d9:
            r0 = "SuggestStickersNone";
            r2 = 2131494453; // 0x7f0c0635 float:1.8612415E38 double:1.0530981835E-314;
            r0 = org.telegram.messenger.LocaleController.getString(r0, r2);
            goto L_0x00f7;
        L_0x00e3:
            r0 = "SuggestStickersInstalled";
            r2 = 2131494452; // 0x7f0c0634 float:1.8612413E38 double:1.053098183E-314;
            r0 = org.telegram.messenger.LocaleController.getString(r0, r2);
            goto L_0x00f7;
        L_0x00ed:
            r0 = "SuggestStickersAll";
            r2 = 2131494451; // 0x7f0c0633 float:1.861241E38 double:1.0530981825E-314;
            r0 = org.telegram.messenger.LocaleController.getString(r0, r2);
            r2 = r8.itemView;
            r2 = (org.telegram.ui.Cells.TextSettingsCell) r2;
            r3 = "SuggestStickers";
            r4 = 2131494450; // 0x7f0c0632 float:1.8612409E38 double:1.053098182E-314;
            r3 = org.telegram.messenger.LocaleController.getString(r3, r4);
            r2.setTextAndValue(r3, r0, r1);
            goto L_0x01d5;
        L_0x010a:
            r0 = org.telegram.ui.StickersActivity.this;
            r0 = r0.featuredInfoRow;
            if (r9 != r0) goto L_0x0157;
            r0 = "FeaturedStickersInfo";
            r1 = 2131493536; // 0x7f0c02a0 float:1.8610555E38 double:1.0530977305E-314;
            r0 = org.telegram.messenger.LocaleController.getString(r0, r1);
            r1 = "@stickers";
            r2 = r0.indexOf(r1);
            r3 = -1;
            if (r2 == r3) goto L_0x014e;
            r3 = new android.text.SpannableStringBuilder;	 Catch:{ Exception -> 0x0142 }
            r3.<init>(r0);	 Catch:{ Exception -> 0x0142 }
            r4 = new org.telegram.ui.StickersActivity$ListAdapter$1;	 Catch:{ Exception -> 0x0142 }
            r5 = "@stickers";	 Catch:{ Exception -> 0x0142 }
            r4.<init>(r5);	 Catch:{ Exception -> 0x0142 }
            r5 = r1.length();	 Catch:{ Exception -> 0x0142 }
            r5 = r5 + r2;	 Catch:{ Exception -> 0x0142 }
            r6 = 18;	 Catch:{ Exception -> 0x0142 }
            r3.setSpan(r4, r2, r5, r6);	 Catch:{ Exception -> 0x0142 }
            r5 = r8.itemView;	 Catch:{ Exception -> 0x0142 }
            r5 = (org.telegram.ui.Cells.TextInfoPrivacyCell) r5;	 Catch:{ Exception -> 0x0142 }
            r5.setText(r3);	 Catch:{ Exception -> 0x0142 }
            goto L_0x014d;
        L_0x0142:
            r3 = move-exception;
            org.telegram.messenger.FileLog.e(r3);
            r4 = r8.itemView;
            r4 = (org.telegram.ui.Cells.TextInfoPrivacyCell) r4;
            r4.setText(r0);
            goto L_0x0155;
            r3 = r8.itemView;
            r3 = (org.telegram.ui.Cells.TextInfoPrivacyCell) r3;
            r3.setText(r0);
            goto L_0x01d5;
            r0 = org.telegram.ui.StickersActivity.this;
            r0 = r0.archivedInfoRow;
            if (r9 != r0) goto L_0x0189;
            r0 = org.telegram.ui.StickersActivity.this;
            r0 = r0.currentType;
            if (r0 != 0) goto L_0x0178;
            r0 = r8.itemView;
            r0 = (org.telegram.ui.Cells.TextInfoPrivacyCell) r0;
            r1 = "ArchivedStickersInfo";
            r2 = 2131492995; // 0x7f0c0083 float:1.8609458E38 double:1.053097463E-314;
            r1 = org.telegram.messenger.LocaleController.getString(r1, r2);
            r0.setText(r1);
            goto L_0x01d5;
            r0 = r8.itemView;
            r0 = (org.telegram.ui.Cells.TextInfoPrivacyCell) r0;
            r1 = "ArchivedMasksInfo";
            r2 = 2131492990; // 0x7f0c007e float:1.8609447E38 double:1.0530974607E-314;
            r1 = org.telegram.messenger.LocaleController.getString(r1, r2);
            r0.setText(r1);
            goto L_0x01d5;
            r0 = org.telegram.ui.StickersActivity.this;
            r0 = r0.masksInfoRow;
            if (r9 != r0) goto L_0x01d5;
            r0 = r8.itemView;
            r0 = (org.telegram.ui.Cells.TextInfoPrivacyCell) r0;
            r1 = "MasksInfo";
            r2 = 2131493790; // 0x7f0c039e float:1.861107E38 double:1.053097856E-314;
            r1 = org.telegram.messenger.LocaleController.getString(r1, r2);
            r0.setText(r1);
            goto L_0x01d5;
        L_0x01a2:
            r0 = org.telegram.ui.StickersActivity.this;
            r0 = r0.currentAccount;
            r0 = org.telegram.messenger.DataQuery.getInstance(r0);
            r3 = org.telegram.ui.StickersActivity.this;
            r3 = r3.currentType;
            r0 = r0.getStickerSets(r3);
            r3 = org.telegram.ui.StickersActivity.this;
            r3 = r3.stickersStartRow;
            r3 = r9 - r3;
            r4 = r8.itemView;
            r4 = (org.telegram.ui.Cells.StickerSetCell) r4;
            r5 = r0.get(r3);
            r5 = (org.telegram.tgnet.TLRPC.TL_messages_stickerSet) r5;
            r6 = r0.size();
            r6 = r6 - r1;
            if (r3 == r6) goto L_0x01d0;
            goto L_0x01d1;
            r1 = r2;
            r4.setStickersSet(r5, r1);
        L_0x01d5:
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: org.telegram.ui.StickersActivity.ListAdapter.onBindViewHolder(org.telegram.messenger.support.widget.RecyclerView$ViewHolder, int):void");
        }

        public ListAdapter(Context context) {
            this.mContext = context;
        }

        public int getItemCount() {
            return StickersActivity.this.rowCount;
        }

        public long getItemId(int i) {
            if (i >= StickersActivity.this.stickersStartRow && i < StickersActivity.this.stickersEndRow) {
                return ((TL_messages_stickerSet) DataQuery.getInstance(StickersActivity.this.currentAccount).getStickerSets(StickersActivity.this.currentType).get(i - StickersActivity.this.stickersStartRow)).set.id;
            }
            if (!(i == StickersActivity.this.suggestRow || i == StickersActivity.this.suggestInfoRow || i == StickersActivity.this.archivedRow || i == StickersActivity.this.archivedInfoRow || i == StickersActivity.this.featuredRow || i == StickersActivity.this.featuredInfoRow || i == StickersActivity.this.masksRow)) {
                if (i != StickersActivity.this.masksInfoRow) {
                    return (long) i;
                }
            }
            return -2147483648L;
        }

        private void processSelectionOption(int which, TL_messages_stickerSet stickerSet) {
            if (which == 0) {
                DataQuery.getInstance(StickersActivity.this.currentAccount).removeStickersSet(StickersActivity.this.getParentActivity(), stickerSet.set, !stickerSet.set.archived ? 1 : 2, StickersActivity.this, true);
            } else if (which == 1) {
                DataQuery.getInstance(StickersActivity.this.currentAccount).removeStickersSet(StickersActivity.this.getParentActivity(), stickerSet.set, 0, StickersActivity.this, true);
            } else if (which == 2) {
                try {
                    Intent intent = new Intent("android.intent.action.SEND");
                    intent.setType("text/plain");
                    r4 = Locale.US;
                    r5 = new StringBuilder();
                    r5.append("https://");
                    r5.append(MessagesController.getInstance(StickersActivity.this.currentAccount).linkPrefix);
                    r5.append("/addstickers/%s");
                    intent.putExtra("android.intent.extra.TEXT", String.format(r4, r5.toString(), new Object[]{stickerSet.set.short_name}));
                    StickersActivity.this.getParentActivity().startActivityForResult(Intent.createChooser(intent, LocaleController.getString("StickersShare", R.string.StickersShare)), 500);
                } catch (Throwable e) {
                    FileLog.e(e);
                }
            } else if (which == 3) {
                try {
                    ClipboardManager clipboard = (ClipboardManager) ApplicationLoader.applicationContext.getSystemService("clipboard");
                    r4 = Locale.US;
                    r5 = new StringBuilder();
                    r5.append("https://");
                    r5.append(MessagesController.getInstance(StickersActivity.this.currentAccount).linkPrefix);
                    r5.append("/addstickers/%s");
                    clipboard.setPrimaryClip(ClipData.newPlainText("label", String.format(r4, r5.toString(), new Object[]{stickerSet.set.short_name})));
                    Toast.makeText(StickersActivity.this.getParentActivity(), LocaleController.getString("LinkCopied", R.string.LinkCopied), 0).show();
                } catch (Throwable e2) {
                    FileLog.e(e2);
                }
            }
        }

        public boolean isEnabled(ViewHolder holder) {
            int type = holder.getItemViewType();
            if (type != 0) {
                if (type != 2) {
                    return false;
                }
            }
            return true;
        }

        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = null;
            switch (viewType) {
                case 0:
                    view = new StickerSetCell(this.mContext, 1);
                    view.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                    ((StickerSetCell) view).setOnOptionsClick(new OnClickListener() {
                        public void onClick(View v) {
                            int[] options;
                            CharSequence[] items;
                            StickersActivity.this.sendReorder();
                            final TL_messages_stickerSet stickerSet = ((StickerSetCell) v.getParent()).getStickersSet();
                            Builder builder = new Builder(StickersActivity.this.getParentActivity());
                            builder.setTitle(stickerSet.set.title);
                            if (StickersActivity.this.currentType == 0) {
                                if (stickerSet.set.official) {
                                    options = new int[]{0};
                                    items = new CharSequence[]{LocaleController.getString("StickersHide", R.string.StickersHide)};
                                } else {
                                    options = new int[]{0, 1, 2, 3};
                                    items = new CharSequence[]{LocaleController.getString("StickersHide", R.string.StickersHide), LocaleController.getString("StickersRemove", R.string.StickersRemove), LocaleController.getString("StickersShare", R.string.StickersShare), LocaleController.getString("StickersCopy", R.string.StickersCopy)};
                                }
                            } else if (stickerSet.set.official) {
                                options = new int[]{0};
                                items = new CharSequence[]{LocaleController.getString("StickersRemove", R.string.StickersHide)};
                            } else {
                                options = new int[]{0, 1, 2, 3};
                                items = new CharSequence[]{LocaleController.getString("StickersHide", R.string.StickersHide), LocaleController.getString("StickersRemove", R.string.StickersRemove), LocaleController.getString("StickersShare", R.string.StickersShare), LocaleController.getString("StickersCopy", R.string.StickersCopy)};
                                builder.setItems(items, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        ListAdapter.this.processSelectionOption(options[which], stickerSet);
                                    }
                                });
                                StickersActivity.this.showDialog(builder.create());
                            }
                            builder.setItems(items, /* anonymous class already generated */);
                            StickersActivity.this.showDialog(builder.create());
                        }
                    });
                    break;
                case 1:
                    view = new TextInfoPrivacyCell(this.mContext);
                    view.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, R.drawable.greydivider_bottom, Theme.key_windowBackgroundGrayShadow));
                    break;
                case 2:
                    view = new TextSettingsCell(this.mContext);
                    view.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                    break;
                case 3:
                    view = new ShadowSectionCell(this.mContext);
                    break;
                default:
                    break;
            }
            view.setLayoutParams(new LayoutParams(-1, -2));
            return new Holder(view);
        }

        public int getItemViewType(int i) {
            if (i >= StickersActivity.this.stickersStartRow && i < StickersActivity.this.stickersEndRow) {
                return 0;
            }
            if (!(i == StickersActivity.this.featuredInfoRow || i == StickersActivity.this.archivedInfoRow)) {
                if (i != StickersActivity.this.masksInfoRow) {
                    if (!(i == StickersActivity.this.featuredRow || i == StickersActivity.this.archivedRow || i == StickersActivity.this.masksRow)) {
                        if (i != StickersActivity.this.suggestRow) {
                            if (i != StickersActivity.this.stickersShadowRow) {
                                if (i != StickersActivity.this.suggestInfoRow) {
                                    return 0;
                                }
                            }
                            return 3;
                        }
                    }
                    return 2;
                }
            }
            return 1;
        }

        public void swapElements(int fromIndex, int toIndex) {
            if (fromIndex != toIndex) {
                StickersActivity.this.needReorder = true;
            }
            ArrayList<TL_messages_stickerSet> arrayList = DataQuery.getInstance(StickersActivity.this.currentAccount).getStickerSets(StickersActivity.this.currentType);
            TL_messages_stickerSet from = (TL_messages_stickerSet) arrayList.get(fromIndex - StickersActivity.this.stickersStartRow);
            arrayList.set(fromIndex - StickersActivity.this.stickersStartRow, arrayList.get(toIndex - StickersActivity.this.stickersStartRow));
            arrayList.set(toIndex - StickersActivity.this.stickersStartRow, from);
            notifyItemMoved(fromIndex, toIndex);
        }
    }

    public StickersActivity(int type) {
        this.currentType = type;
    }

    public boolean onFragmentCreate() {
        super.onFragmentCreate();
        DataQuery.getInstance(this.currentAccount).checkStickers(this.currentType);
        if (this.currentType == 0) {
            DataQuery.getInstance(this.currentAccount).checkFeaturedStickers();
        }
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.stickersDidLoaded);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.archivedStickersCountDidLoaded);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.featuredStickersDidLoaded);
        updateRows();
        return true;
    }

    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.stickersDidLoaded);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.archivedStickersCountDidLoaded);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.featuredStickersDidLoaded);
        sendReorder();
    }

    public View createView(Context context) {
        this.actionBar.setBackButtonImage(R.drawable.ic_ab_back);
        this.actionBar.setAllowOverlayTitle(true);
        if (this.currentType == 0) {
            this.actionBar.setTitle(LocaleController.getString("StickersName", R.string.StickersName));
        } else {
            this.actionBar.setTitle(LocaleController.getString("Masks", R.string.Masks));
        }
        this.actionBar.setActionBarMenuOnItemClick(new ActionBarMenuOnItemClick() {
            public void onItemClick(int id) {
                if (id == -1) {
                    StickersActivity.this.finishFragment();
                }
            }
        });
        this.listAdapter = new ListAdapter(context);
        this.fragmentView = new FrameLayout(context);
        FrameLayout frameLayout = this.fragmentView;
        frameLayout.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundGray));
        this.listView = new RecyclerListView(context);
        this.listView.setFocusable(true);
        this.listView.setTag(Integer.valueOf(7));
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(1);
        this.listView.setLayoutManager(layoutManager);
        new ItemTouchHelper(new TouchHelperCallback()).attachToRecyclerView(this.listView);
        frameLayout.addView(this.listView, LayoutHelper.createFrame(-1, -1.0f));
        this.listView.setAdapter(this.listAdapter);
        this.listView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(View view, int position) {
                if (position >= StickersActivity.this.stickersStartRow && position < StickersActivity.this.stickersEndRow && StickersActivity.this.getParentActivity() != null) {
                    StickersActivity.this.sendReorder();
                    TL_messages_stickerSet stickerSet = (TL_messages_stickerSet) DataQuery.getInstance(StickersActivity.this.currentAccount).getStickerSets(StickersActivity.this.currentType).get(position - StickersActivity.this.stickersStartRow);
                    ArrayList<Document> stickers = stickerSet.documents;
                    if (stickers != null) {
                        if (!stickers.isEmpty()) {
                            StickersActivity.this.showDialog(new StickersAlert(StickersActivity.this.getParentActivity(), StickersActivity.this, null, stickerSet, null));
                        }
                    }
                } else if (position == StickersActivity.this.featuredRow) {
                    StickersActivity.this.sendReorder();
                    StickersActivity.this.presentFragment(new FeaturedStickersActivity());
                } else if (position == StickersActivity.this.archivedRow) {
                    StickersActivity.this.sendReorder();
                    StickersActivity.this.presentFragment(new ArchivedStickersActivity(StickersActivity.this.currentType));
                } else if (position == StickersActivity.this.masksRow) {
                    StickersActivity.this.presentFragment(new StickersActivity(1));
                } else if (position == StickersActivity.this.suggestRow) {
                    Builder builder = new Builder(StickersActivity.this.getParentActivity());
                    builder.setTitle(LocaleController.getString("SuggestStickers", R.string.SuggestStickers));
                    builder.setItems(new CharSequence[]{LocaleController.getString("SuggestStickersAll", R.string.SuggestStickersAll), LocaleController.getString("SuggestStickersInstalled", R.string.SuggestStickersInstalled), LocaleController.getString("SuggestStickersNone", R.string.SuggestStickersNone)}, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            SharedConfig.setSuggestStickers(which);
                            StickersActivity.this.listAdapter.notifyItemChanged(StickersActivity.this.suggestRow);
                        }
                    });
                    StickersActivity.this.showDialog(builder.create());
                }
            }
        });
        return this.fragmentView;
    }

    public void didReceivedNotification(int id, int account, Object... args) {
        if (id == NotificationCenter.stickersDidLoaded) {
            if (((Integer) args[0]).intValue() == this.currentType) {
                updateRows();
            }
        } else if (id == NotificationCenter.featuredStickersDidLoaded) {
            if (this.listAdapter != null) {
                this.listAdapter.notifyItemChanged(0);
            }
        } else if (id == NotificationCenter.archivedStickersCountDidLoaded && ((Integer) args[0]).intValue() == this.currentType) {
            updateRows();
        }
    }

    private void sendReorder() {
        if (this.needReorder) {
            DataQuery.getInstance(this.currentAccount).calcNewHash(this.currentType);
            this.needReorder = false;
            TL_messages_reorderStickerSets req = new TL_messages_reorderStickerSets();
            req.masks = this.currentType == 1;
            ArrayList<TL_messages_stickerSet> arrayList = DataQuery.getInstance(this.currentAccount).getStickerSets(this.currentType);
            for (int a = 0; a < arrayList.size(); a++) {
                req.order.add(Long.valueOf(((TL_messages_stickerSet) arrayList.get(a)).set.id));
            }
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(req, new RequestDelegate() {
                public void run(TLObject response, TL_error error) {
                }
            });
            NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.stickersDidLoaded, Integer.valueOf(this.currentType));
        }
    }

    private void updateRows() {
        this.rowCount = 0;
        if (this.currentType == 0) {
            int i = this.rowCount;
            this.rowCount = i + 1;
            this.suggestRow = i;
            i = this.rowCount;
            this.rowCount = i + 1;
            this.featuredRow = i;
            i = this.rowCount;
            this.rowCount = i + 1;
            this.featuredInfoRow = i;
            i = this.rowCount;
            this.rowCount = i + 1;
            this.masksRow = i;
            i = this.rowCount;
            this.rowCount = i + 1;
            this.masksInfoRow = i;
        } else {
            this.featuredRow = -1;
            this.featuredInfoRow = -1;
            this.masksRow = -1;
            this.masksInfoRow = -1;
        }
        if (DataQuery.getInstance(this.currentAccount).getArchivedStickersCount(this.currentType) != 0) {
            i = this.rowCount;
            this.rowCount = i + 1;
            this.archivedRow = i;
            i = this.rowCount;
            this.rowCount = i + 1;
            this.archivedInfoRow = i;
        } else {
            this.archivedRow = -1;
            this.archivedInfoRow = -1;
        }
        ArrayList<TL_messages_stickerSet> stickerSets = DataQuery.getInstance(this.currentAccount).getStickerSets(this.currentType);
        if (stickerSets.isEmpty()) {
            this.stickersStartRow = -1;
            this.stickersEndRow = -1;
            this.stickersShadowRow = -1;
        } else {
            this.stickersStartRow = this.rowCount;
            this.stickersEndRow = this.rowCount + stickerSets.size();
            this.rowCount += stickerSets.size();
            int i2 = this.rowCount;
            this.rowCount = i2 + 1;
            this.stickersShadowRow = i2;
        }
        if (this.listAdapter != null) {
            this.listAdapter.notifyDataSetChanged();
        }
    }

    public void onResume() {
        super.onResume();
        if (this.listAdapter != null) {
            this.listAdapter.notifyDataSetChanged();
        }
    }

    public ThemeDescription[] getThemeDescriptions() {
        ThemeDescription[] themeDescriptionArr = new ThemeDescription[19];
        themeDescriptionArr[0] = new ThemeDescription(this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{StickerSetCell.class, TextSettingsCell.class}, null, null, null, Theme.key_windowBackgroundWhite);
        themeDescriptionArr[1] = new ThemeDescription(this.fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_windowBackgroundGray);
        themeDescriptionArr[2] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_actionBarDefault);
        themeDescriptionArr[3] = new ThemeDescription(this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, Theme.key_actionBarDefault);
        themeDescriptionArr[4] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, Theme.key_actionBarDefaultIcon);
        themeDescriptionArr[5] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, Theme.key_actionBarDefaultTitle);
        themeDescriptionArr[6] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, Theme.key_actionBarDefaultSelector);
        themeDescriptionArr[7] = new ThemeDescription(this.listView, ThemeDescription.FLAG_SELECTOR, null, null, null, null, Theme.key_listSelector);
        themeDescriptionArr[8] = new ThemeDescription(this.listView, 0, new Class[]{View.class}, Theme.dividerPaint, null, null, Theme.key_divider);
        themeDescriptionArr[9] = new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{TextInfoPrivacyCell.class}, null, null, null, Theme.key_windowBackgroundGrayShadow);
        themeDescriptionArr[10] = new ThemeDescription(this.listView, 0, new Class[]{TextInfoPrivacyCell.class}, new String[]{"textView"}, null, null, null, Theme.key_windowBackgroundWhiteGrayText4);
        themeDescriptionArr[11] = new ThemeDescription(this.listView, ThemeDescription.FLAG_LINKCOLOR, new Class[]{TextInfoPrivacyCell.class}, new String[]{"textView"}, null, null, null, Theme.key_windowBackgroundWhiteLinkText);
        themeDescriptionArr[12] = new ThemeDescription(this.listView, 0, new Class[]{TextSettingsCell.class}, new String[]{"textView"}, null, null, null, Theme.key_windowBackgroundWhiteBlackText);
        themeDescriptionArr[13] = new ThemeDescription(this.listView, 0, new Class[]{TextSettingsCell.class}, new String[]{"valueTextView"}, null, null, null, Theme.key_windowBackgroundWhiteValueText);
        themeDescriptionArr[14] = new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{ShadowSectionCell.class}, null, null, null, Theme.key_windowBackgroundGrayShadow);
        themeDescriptionArr[15] = new ThemeDescription(this.listView, 0, new Class[]{StickerSetCell.class}, new String[]{"textView"}, null, null, null, Theme.key_windowBackgroundWhiteBlackText);
        themeDescriptionArr[16] = new ThemeDescription(this.listView, 0, new Class[]{StickerSetCell.class}, new String[]{"valueTextView"}, null, null, null, Theme.key_windowBackgroundWhiteGrayText2);
        themeDescriptionArr[17] = new ThemeDescription(this.listView, ThemeDescription.FLAG_USEBACKGROUNDDRAWABLE | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, new Class[]{StickerSetCell.class}, new String[]{"optionsButton"}, null, null, null, Theme.key_stickers_menuSelector);
        themeDescriptionArr[18] = new ThemeDescription(this.listView, 0, new Class[]{StickerSetCell.class}, new String[]{"optionsButton"}, null, null, null, Theme.key_stickers_menu);
        return themeDescriptionArr;
    }
}
