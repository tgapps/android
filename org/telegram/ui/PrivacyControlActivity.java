package org.telegram.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.NotificationCenter.NotificationCenterDelegate;
import org.telegram.messenger.beta.R;
import org.telegram.messenger.support.widget.LinearLayoutManager;
import org.telegram.messenger.support.widget.RecyclerView.ViewHolder;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.InputUser;
import org.telegram.tgnet.TLRPC.PrivacyRule;
import org.telegram.tgnet.TLRPC.TL_account_privacyRules;
import org.telegram.tgnet.TLRPC.TL_account_setPrivacy;
import org.telegram.tgnet.TLRPC.TL_error;
import org.telegram.tgnet.TLRPC.TL_inputPrivacyKeyChatInvite;
import org.telegram.tgnet.TLRPC.TL_inputPrivacyKeyPhoneCall;
import org.telegram.tgnet.TLRPC.TL_inputPrivacyKeyStatusTimestamp;
import org.telegram.tgnet.TLRPC.TL_inputPrivacyValueAllowAll;
import org.telegram.tgnet.TLRPC.TL_inputPrivacyValueAllowContacts;
import org.telegram.tgnet.TLRPC.TL_inputPrivacyValueAllowUsers;
import org.telegram.tgnet.TLRPC.TL_inputPrivacyValueDisallowAll;
import org.telegram.tgnet.TLRPC.TL_inputPrivacyValueDisallowUsers;
import org.telegram.tgnet.TLRPC.TL_privacyValueAllowAll;
import org.telegram.tgnet.TLRPC.TL_privacyValueAllowUsers;
import org.telegram.tgnet.TLRPC.TL_privacyValueDisallowAll;
import org.telegram.tgnet.TLRPC.TL_privacyValueDisallowUsers;
import org.telegram.tgnet.TLRPC.User;
import org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.AlertDialog.Builder;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.RadioCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Cells.TextSettingsCell;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.RecyclerListView.Holder;
import org.telegram.ui.Components.RecyclerListView.OnItemClickListener;
import org.telegram.ui.Components.RecyclerListView.SelectionAdapter;
import org.telegram.ui.GroupCreateActivity.GroupCreateActivityDelegate;
import org.telegram.ui.PrivacyUsersActivity.PrivacyActivityDelegate;

public class PrivacyControlActivity extends BaseFragment implements NotificationCenterDelegate {
    private static final int done_button = 1;
    private int alwaysShareRow;
    private ArrayList<Integer> currentMinus;
    private ArrayList<Integer> currentPlus;
    private int currentType;
    private int detailRow;
    private View doneButton;
    private boolean enableAnimation;
    private int everybodyRow;
    private int lastCheckedType = -1;
    private ListAdapter listAdapter;
    private RecyclerListView listView;
    private int myContactsRow;
    private int neverShareRow;
    private int nobodyRow;
    private int rowCount;
    private int rulesType;
    private int sectionRow;
    private int shareDetailRow;
    private int shareSectionRow;

    private static class LinkMovementMethodMy extends LinkMovementMethod {
        private LinkMovementMethodMy() {
        }

        public boolean onTouchEvent(TextView widget, Spannable buffer, MotionEvent event) {
            try {
                return super.onTouchEvent(widget, buffer, event);
            } catch (Throwable e) {
                FileLog.e(e);
                return false;
            }
        }
    }

    private class ListAdapter extends SelectionAdapter {
        private Context mContext;

        public void onBindViewHolder(org.telegram.messenger.support.widget.RecyclerView.ViewHolder r1, int r2) {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.ui.PrivacyControlActivity.ListAdapter.onBindViewHolder(org.telegram.messenger.support.widget.RecyclerView$ViewHolder, int):void
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
            r0 = r9.getItemViewType();
            r1 = -1;
            r2 = 2;
            r3 = 0;
            r4 = 1;
            switch(r0) {
                case 0: goto L_0x01a1;
                case 1: goto L_0x0101;
                case 2: goto L_0x00a5;
                case 3: goto L_0x000d;
                default: goto L_0x000b;
            };
        L_0x000b:
            goto L_0x0256;
        L_0x000d:
            r0 = r9.itemView;
            r0 = (org.telegram.ui.Cells.RadioCell) r0;
            r5 = 0;
            r6 = org.telegram.ui.PrivacyControlActivity.this;
            r6 = r6.everybodyRow;
            if (r10 != r6) goto L_0x0033;
        L_0x001a:
            r1 = "LastSeenEverybody";
            r2 = 2131493734; // 0x7f0c0366 float:1.8610957E38 double:1.0530978283E-314;
            r1 = org.telegram.messenger.LocaleController.getString(r1, r2);
            r2 = org.telegram.ui.PrivacyControlActivity.this;
            r2 = r2.lastCheckedType;
            if (r2 != 0) goto L_0x002d;
        L_0x002b:
            r2 = r4;
            goto L_0x002e;
        L_0x002d:
            r2 = r3;
        L_0x002e:
            r0.setText(r1, r2, r4);
            r5 = 0;
            goto L_0x007f;
        L_0x0033:
            r6 = org.telegram.ui.PrivacyControlActivity.this;
            r6 = r6.myContactsRow;
            if (r10 != r6) goto L_0x005f;
        L_0x003b:
            r6 = "LastSeenContacts";
            r7 = 2131493728; // 0x7f0c0360 float:1.8610944E38 double:1.0530978253E-314;
            r6 = org.telegram.messenger.LocaleController.getString(r6, r7);
            r7 = org.telegram.ui.PrivacyControlActivity.this;
            r7 = r7.lastCheckedType;
            if (r7 != r2) goto L_0x004e;
        L_0x004c:
            r2 = r4;
            goto L_0x004f;
        L_0x004e:
            r2 = r3;
        L_0x004f:
            r7 = org.telegram.ui.PrivacyControlActivity.this;
            r7 = r7.nobodyRow;
            if (r7 == r1) goto L_0x0059;
        L_0x0057:
            r1 = r4;
            goto L_0x005a;
        L_0x0059:
            r1 = r3;
        L_0x005a:
            r0.setText(r6, r2, r1);
            r5 = 2;
            goto L_0x007f;
        L_0x005f:
            r1 = org.telegram.ui.PrivacyControlActivity.this;
            r1 = r1.nobodyRow;
            if (r10 != r1) goto L_0x007f;
        L_0x0067:
            r1 = "LastSeenNobody";
            r2 = 2131493737; // 0x7f0c0369 float:1.8610963E38 double:1.05309783E-314;
            r1 = org.telegram.messenger.LocaleController.getString(r1, r2);
            r2 = org.telegram.ui.PrivacyControlActivity.this;
            r2 = r2.lastCheckedType;
            if (r2 != r4) goto L_0x007a;
        L_0x0078:
            r2 = r4;
            goto L_0x007b;
        L_0x007a:
            r2 = r3;
        L_0x007b:
            r0.setText(r1, r2, r3);
            r5 = 1;
        L_0x007f:
            r1 = org.telegram.ui.PrivacyControlActivity.this;
            r1 = r1.lastCheckedType;
            if (r1 != r5) goto L_0x0092;
        L_0x0087:
            r1 = org.telegram.ui.PrivacyControlActivity.this;
            r1 = r1.enableAnimation;
            r0.setChecked(r3, r1);
            goto L_0x0256;
        L_0x0092:
            r1 = org.telegram.ui.PrivacyControlActivity.this;
            r1 = r1.currentType;
            if (r1 != r5) goto L_0x0256;
        L_0x009a:
            r1 = org.telegram.ui.PrivacyControlActivity.this;
            r1 = r1.enableAnimation;
            r0.setChecked(r4, r1);
            goto L_0x0256;
        L_0x00a5:
            r0 = r9.itemView;
            r0 = (org.telegram.ui.Cells.HeaderCell) r0;
            r1 = org.telegram.ui.PrivacyControlActivity.this;
            r1 = r1.sectionRow;
            if (r10 != r1) goto L_0x00eb;
        L_0x00b1:
            r1 = org.telegram.ui.PrivacyControlActivity.this;
            r1 = r1.rulesType;
            if (r1 != r2) goto L_0x00c7;
        L_0x00b9:
            r1 = "WhoCanCallMe";
            r2 = 2131494638; // 0x7f0c06ee float:1.861279E38 double:1.053098275E-314;
            r1 = org.telegram.messenger.LocaleController.getString(r1, r2);
            r0.setText(r1);
            goto L_0x0256;
        L_0x00c7:
            r1 = org.telegram.ui.PrivacyControlActivity.this;
            r1 = r1.rulesType;
            if (r1 != r4) goto L_0x00dd;
        L_0x00cf:
            r1 = "WhoCanAddMe";
            r2 = 2131494633; // 0x7f0c06e9 float:1.861278E38 double:1.0530982725E-314;
            r1 = org.telegram.messenger.LocaleController.getString(r1, r2);
            r0.setText(r1);
            goto L_0x0256;
        L_0x00dd:
            r1 = "LastSeenTitle";
            r2 = 2131493739; // 0x7f0c036b float:1.8610967E38 double:1.053097831E-314;
            r1 = org.telegram.messenger.LocaleController.getString(r1, r2);
            r0.setText(r1);
            goto L_0x0256;
        L_0x00eb:
            r1 = org.telegram.ui.PrivacyControlActivity.this;
            r1 = r1.shareSectionRow;
            if (r10 != r1) goto L_0x0256;
        L_0x00f3:
            r1 = "AddExceptions";
            r2 = 2131492929; // 0x7f0c0041 float:1.8609324E38 double:1.0530974306E-314;
            r1 = org.telegram.messenger.LocaleController.getString(r1, r2);
            r0.setText(r1);
            goto L_0x0256;
        L_0x0101:
            r0 = r9.itemView;
            r0 = (org.telegram.ui.Cells.TextInfoPrivacyCell) r0;
            r1 = org.telegram.ui.PrivacyControlActivity.this;
            r1 = r1.detailRow;
            if (r10 != r1) goto L_0x0153;
        L_0x010d:
            r1 = org.telegram.ui.PrivacyControlActivity.this;
            r1 = r1.rulesType;
            if (r1 != r2) goto L_0x0122;
        L_0x0115:
            r1 = "WhoCanCallMeInfo";
            r2 = 2131494639; // 0x7f0c06ef float:1.8612792E38 double:1.0530982754E-314;
            r1 = org.telegram.messenger.LocaleController.getString(r1, r2);
            r0.setText(r1);
            goto L_0x0143;
        L_0x0122:
            r1 = org.telegram.ui.PrivacyControlActivity.this;
            r1 = r1.rulesType;
            if (r1 != r4) goto L_0x0137;
        L_0x012a:
            r1 = "WhoCanAddMeInfo";
            r2 = 2131494634; // 0x7f0c06ea float:1.8612782E38 double:1.053098273E-314;
            r1 = org.telegram.messenger.LocaleController.getString(r1, r2);
            r0.setText(r1);
            goto L_0x0143;
        L_0x0137:
            r1 = "CustomHelp";
            r2 = 2131493324; // 0x7f0c01cc float:1.8610125E38 double:1.0530976257E-314;
            r1 = org.telegram.messenger.LocaleController.getString(r1, r2);
            r0.setText(r1);
        L_0x0143:
            r1 = r8.mContext;
            r2 = 2131165331; // 0x7f070093 float:1.7944876E38 double:1.0529355757E-314;
            r3 = "windowBackgroundGrayShadow";
            r1 = org.telegram.ui.ActionBar.Theme.getThemedDrawable(r1, r2, r3);
            r0.setBackgroundDrawable(r1);
            goto L_0x0256;
        L_0x0153:
            r1 = org.telegram.ui.PrivacyControlActivity.this;
            r1 = r1.shareDetailRow;
            if (r10 != r1) goto L_0x0256;
        L_0x015b:
            r1 = org.telegram.ui.PrivacyControlActivity.this;
            r1 = r1.rulesType;
            if (r1 != r2) goto L_0x0170;
        L_0x0163:
            r1 = "CustomCallInfo";
            r2 = 2131493323; // 0x7f0c01cb float:1.8610123E38 double:1.053097625E-314;
            r1 = org.telegram.messenger.LocaleController.getString(r1, r2);
            r0.setText(r1);
            goto L_0x0191;
        L_0x0170:
            r1 = org.telegram.ui.PrivacyControlActivity.this;
            r1 = r1.rulesType;
            if (r1 != r4) goto L_0x0185;
        L_0x0178:
            r1 = "CustomShareInfo";
            r2 = 2131493326; // 0x7f0c01ce float:1.861013E38 double:1.0530976267E-314;
            r1 = org.telegram.messenger.LocaleController.getString(r1, r2);
            r0.setText(r1);
            goto L_0x0191;
        L_0x0185:
            r1 = "CustomShareSettingsHelp";
            r2 = 2131493327; // 0x7f0c01cf float:1.8610131E38 double:1.053097627E-314;
            r1 = org.telegram.messenger.LocaleController.getString(r1, r2);
            r0.setText(r1);
        L_0x0191:
            r1 = r8.mContext;
            r2 = 2131165332; // 0x7f070094 float:1.7944878E38 double:1.052935576E-314;
            r3 = "windowBackgroundGrayShadow";
            r1 = org.telegram.ui.ActionBar.Theme.getThemedDrawable(r1, r2, r3);
            r0.setBackgroundDrawable(r1);
            goto L_0x0256;
        L_0x01a1:
            r0 = r9.itemView;
            r0 = (org.telegram.ui.Cells.TextSettingsCell) r0;
            r2 = org.telegram.ui.PrivacyControlActivity.this;
            r2 = r2.alwaysShareRow;
            r5 = 2131493422; // 0x7f0c022e float:1.8610324E38 double:1.053097674E-314;
            if (r10 != r2) goto L_0x0209;
        L_0x01b0:
            r2 = org.telegram.ui.PrivacyControlActivity.this;
            r2 = r2.currentPlus;
            r2 = r2.size();
            if (r2 == 0) goto L_0x01cd;
        L_0x01bc:
            r2 = "Users";
            r5 = org.telegram.ui.PrivacyControlActivity.this;
            r5 = r5.currentPlus;
            r5 = r5.size();
            r2 = org.telegram.messenger.LocaleController.formatPluralString(r2, r5);
            goto L_0x01d3;
        L_0x01cd:
            r2 = "EmpryUsersPlaceholder";
            r2 = org.telegram.messenger.LocaleController.getString(r2, r5);
        L_0x01d3:
            r5 = org.telegram.ui.PrivacyControlActivity.this;
            r5 = r5.rulesType;
            if (r5 == 0) goto L_0x01f2;
        L_0x01db:
            r5 = "AlwaysAllow";
            r6 = 2131492956; // 0x7f0c005c float:1.8609379E38 double:1.053097444E-314;
            r5 = org.telegram.messenger.LocaleController.getString(r5, r6);
            r6 = org.telegram.ui.PrivacyControlActivity.this;
            r6 = r6.neverShareRow;
            if (r6 == r1) goto L_0x01ee;
        L_0x01ec:
            r3 = r4;
        L_0x01ee:
            r0.setTextAndValue(r5, r2, r3);
            goto L_0x0208;
        L_0x01f2:
            r5 = "AlwaysShareWith";
            r6 = 2131492958; // 0x7f0c005e float:1.8609383E38 double:1.053097445E-314;
            r5 = org.telegram.messenger.LocaleController.getString(r5, r6);
            r6 = org.telegram.ui.PrivacyControlActivity.this;
            r6 = r6.neverShareRow;
            if (r6 == r1) goto L_0x0205;
        L_0x0203:
            r3 = r4;
        L_0x0205:
            r0.setTextAndValue(r5, r2, r3);
        L_0x0208:
            goto L_0x0256;
        L_0x0209:
            r1 = org.telegram.ui.PrivacyControlActivity.this;
            r1 = r1.neverShareRow;
            if (r10 != r1) goto L_0x0256;
        L_0x0211:
            r1 = org.telegram.ui.PrivacyControlActivity.this;
            r1 = r1.currentMinus;
            r1 = r1.size();
            if (r1 == 0) goto L_0x022e;
        L_0x021d:
            r1 = "Users";
            r2 = org.telegram.ui.PrivacyControlActivity.this;
            r2 = r2.currentMinus;
            r2 = r2.size();
            r1 = org.telegram.messenger.LocaleController.formatPluralString(r1, r2);
            goto L_0x0234;
        L_0x022e:
            r1 = "EmpryUsersPlaceholder";
            r1 = org.telegram.messenger.LocaleController.getString(r1, r5);
        L_0x0234:
            r2 = org.telegram.ui.PrivacyControlActivity.this;
            r2 = r2.rulesType;
            if (r2 == 0) goto L_0x0249;
        L_0x023c:
            r2 = "NeverAllow";
            r4 = 2131493861; // 0x7f0c03e5 float:1.8611214E38 double:1.053097891E-314;
            r2 = org.telegram.messenger.LocaleController.getString(r2, r4);
            r0.setTextAndValue(r2, r1, r3);
            goto L_0x0255;
        L_0x0249:
            r2 = "NeverShareWith";
            r4 = 2131493863; // 0x7f0c03e7 float:1.8611218E38 double:1.053097892E-314;
            r2 = org.telegram.messenger.LocaleController.getString(r2, r4);
            r0.setTextAndValue(r2, r1, r3);
        L_0x0256:
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: org.telegram.ui.PrivacyControlActivity.ListAdapter.onBindViewHolder(org.telegram.messenger.support.widget.RecyclerView$ViewHolder, int):void");
        }

        public ListAdapter(Context context) {
            this.mContext = context;
        }

        public boolean isEnabled(ViewHolder holder) {
            int position = holder.getAdapterPosition();
            if (!(position == PrivacyControlActivity.this.nobodyRow || position == PrivacyControlActivity.this.everybodyRow || position == PrivacyControlActivity.this.myContactsRow || position == PrivacyControlActivity.this.neverShareRow)) {
                if (position != PrivacyControlActivity.this.alwaysShareRow) {
                    return false;
                }
            }
            return true;
        }

        public int getItemCount() {
            return PrivacyControlActivity.this.rowCount;
        }

        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view;
            switch (viewType) {
                case 0:
                    view = new TextSettingsCell(this.mContext);
                    view.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                    break;
                case 1:
                    view = new TextInfoPrivacyCell(this.mContext);
                    break;
                case 2:
                    view = new HeaderCell(this.mContext);
                    view.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                    break;
                default:
                    view = new RadioCell(this.mContext);
                    view.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                    break;
            }
            return new Holder(view);
        }

        public int getItemViewType(int position) {
            if (position != PrivacyControlActivity.this.alwaysShareRow) {
                if (position != PrivacyControlActivity.this.neverShareRow) {
                    if (position != PrivacyControlActivity.this.shareDetailRow) {
                        if (position != PrivacyControlActivity.this.detailRow) {
                            if (position != PrivacyControlActivity.this.sectionRow) {
                                if (position != PrivacyControlActivity.this.shareSectionRow) {
                                    if (!(position == PrivacyControlActivity.this.everybodyRow || position == PrivacyControlActivity.this.myContactsRow)) {
                                        if (position != PrivacyControlActivity.this.nobodyRow) {
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
            }
            return 0;
        }
    }

    public PrivacyControlActivity(int type) {
        this.rulesType = type;
    }

    public boolean onFragmentCreate() {
        super.onFragmentCreate();
        checkPrivacy();
        updateRows();
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.privacyRulesUpdated);
        return true;
    }

    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.privacyRulesUpdated);
    }

    public View createView(Context context) {
        this.actionBar.setBackButtonImage(R.drawable.ic_ab_back);
        this.actionBar.setAllowOverlayTitle(true);
        if (this.rulesType == 2) {
            this.actionBar.setTitle(LocaleController.getString("Calls", R.string.Calls));
        } else if (this.rulesType == 1) {
            this.actionBar.setTitle(LocaleController.getString("GroupsAndChannels", R.string.GroupsAndChannels));
        } else {
            this.actionBar.setTitle(LocaleController.getString("PrivacyLastSeen", R.string.PrivacyLastSeen));
        }
        this.actionBar.setActionBarMenuOnItemClick(new ActionBarMenuOnItemClick() {
            public void onItemClick(int id) {
                if (id == -1) {
                    PrivacyControlActivity.this.finishFragment();
                } else if (id == 1 && PrivacyControlActivity.this.getParentActivity() != null) {
                    if (PrivacyControlActivity.this.currentType != 0 && PrivacyControlActivity.this.rulesType == 0) {
                        final SharedPreferences preferences = MessagesController.getGlobalMainSettings();
                        if (!preferences.getBoolean("privacyAlertShowed", false)) {
                            Builder builder = new Builder(PrivacyControlActivity.this.getParentActivity());
                            if (PrivacyControlActivity.this.rulesType == 1) {
                                builder.setMessage(LocaleController.getString("WhoCanAddMeInfo", R.string.WhoCanAddMeInfo));
                            } else {
                                builder.setMessage(LocaleController.getString("CustomHelp", R.string.CustomHelp));
                            }
                            builder.setTitle(LocaleController.getString("AppName", R.string.AppName));
                            builder.setPositiveButton(LocaleController.getString("OK", R.string.OK), new OnClickListener() {
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    PrivacyControlActivity.this.applyCurrentPrivacySettings();
                                    preferences.edit().putBoolean("privacyAlertShowed", true).commit();
                                }
                            });
                            builder.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null);
                            PrivacyControlActivity.this.showDialog(builder.create());
                            return;
                        }
                    }
                    PrivacyControlActivity.this.applyCurrentPrivacySettings();
                }
            }
        });
        int visibility = this.doneButton != null ? this.doneButton.getVisibility() : 8;
        this.doneButton = this.actionBar.createMenu().addItemWithWidth(1, R.drawable.ic_done, AndroidUtilities.dp(56.0f));
        this.doneButton.setVisibility(visibility);
        this.listAdapter = new ListAdapter(context);
        this.fragmentView = new FrameLayout(context);
        FrameLayout frameLayout = this.fragmentView;
        frameLayout.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundGray));
        this.listView = new RecyclerListView(context);
        this.listView.setLayoutManager(new LinearLayoutManager(context, 1, false));
        this.listView.setVerticalScrollBarEnabled(false);
        frameLayout.addView(this.listView, LayoutHelper.createFrame(-1, -1.0f));
        this.listView.setAdapter(this.listAdapter);
        this.listView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(View view, final int position) {
                boolean z = false;
                if (!(position == PrivacyControlActivity.this.nobodyRow || position == PrivacyControlActivity.this.everybodyRow)) {
                    if (position != PrivacyControlActivity.this.myContactsRow) {
                        if (position == PrivacyControlActivity.this.neverShareRow || position == PrivacyControlActivity.this.alwaysShareRow) {
                            ArrayList<Integer> createFromArray;
                            if (position == PrivacyControlActivity.this.neverShareRow) {
                                createFromArray = PrivacyControlActivity.this.currentMinus;
                            } else {
                                createFromArray = PrivacyControlActivity.this.currentPlus;
                            }
                            if (createFromArray.isEmpty()) {
                                Bundle args = new Bundle();
                                args.putBoolean(position == PrivacyControlActivity.this.neverShareRow ? "isNeverShare" : "isAlwaysShare", true);
                                String str = "isGroup";
                                if (PrivacyControlActivity.this.rulesType != 0) {
                                    z = true;
                                }
                                args.putBoolean(str, z);
                                GroupCreateActivity fragment = new GroupCreateActivity(args);
                                fragment.setDelegate(new GroupCreateActivityDelegate() {
                                    public void didSelectUsers(ArrayList<Integer> ids) {
                                        int a;
                                        if (position == PrivacyControlActivity.this.neverShareRow) {
                                            PrivacyControlActivity.this.currentMinus = ids;
                                            for (a = 0; a < PrivacyControlActivity.this.currentMinus.size(); a++) {
                                                PrivacyControlActivity.this.currentPlus.remove(PrivacyControlActivity.this.currentMinus.get(a));
                                            }
                                        } else {
                                            PrivacyControlActivity.this.currentPlus = ids;
                                            for (a = 0; a < PrivacyControlActivity.this.currentPlus.size(); a++) {
                                                PrivacyControlActivity.this.currentMinus.remove(PrivacyControlActivity.this.currentPlus.get(a));
                                            }
                                        }
                                        PrivacyControlActivity.this.doneButton.setVisibility(0);
                                        PrivacyControlActivity.this.lastCheckedType = -1;
                                        PrivacyControlActivity.this.listAdapter.notifyDataSetChanged();
                                    }
                                });
                                PrivacyControlActivity.this.presentFragment(fragment);
                            } else {
                                boolean z2 = PrivacyControlActivity.this.rulesType != 0;
                                if (position == PrivacyControlActivity.this.alwaysShareRow) {
                                    z = true;
                                }
                                PrivacyUsersActivity fragment2 = new PrivacyUsersActivity(createFromArray, z2, z);
                                fragment2.setDelegate(new PrivacyActivityDelegate() {
                                    public void didUpdatedUserList(ArrayList<Integer> ids, boolean added) {
                                        int a;
                                        if (position == PrivacyControlActivity.this.neverShareRow) {
                                            PrivacyControlActivity.this.currentMinus = ids;
                                            if (added) {
                                                for (a = 0; a < PrivacyControlActivity.this.currentMinus.size(); a++) {
                                                    PrivacyControlActivity.this.currentPlus.remove(PrivacyControlActivity.this.currentMinus.get(a));
                                                }
                                            }
                                        } else {
                                            PrivacyControlActivity.this.currentPlus = ids;
                                            if (added) {
                                                for (a = 0; a < PrivacyControlActivity.this.currentPlus.size(); a++) {
                                                    PrivacyControlActivity.this.currentMinus.remove(PrivacyControlActivity.this.currentPlus.get(a));
                                                }
                                            }
                                        }
                                        PrivacyControlActivity.this.doneButton.setVisibility(0);
                                        PrivacyControlActivity.this.listAdapter.notifyDataSetChanged();
                                    }
                                });
                                PrivacyControlActivity.this.presentFragment(fragment2);
                            }
                        }
                    }
                }
                int newType = PrivacyControlActivity.this.currentType;
                if (position == PrivacyControlActivity.this.nobodyRow) {
                    newType = 1;
                } else if (position == PrivacyControlActivity.this.everybodyRow) {
                    newType = 0;
                } else if (position == PrivacyControlActivity.this.myContactsRow) {
                    newType = 2;
                }
                if (newType != PrivacyControlActivity.this.currentType) {
                    PrivacyControlActivity.this.enableAnimation = true;
                    PrivacyControlActivity.this.doneButton.setVisibility(0);
                    PrivacyControlActivity.this.lastCheckedType = PrivacyControlActivity.this.currentType;
                    PrivacyControlActivity.this.currentType = newType;
                    PrivacyControlActivity.this.updateRows();
                }
            }
        });
        return this.fragmentView;
    }

    public void didReceivedNotification(int id, int account, Object... args) {
        if (id == NotificationCenter.privacyRulesUpdated) {
            checkPrivacy();
        }
    }

    private void applyCurrentPrivacySettings() {
        int a;
        User user;
        InputUser inputUser;
        TL_account_setPrivacy req = new TL_account_setPrivacy();
        if (this.rulesType == 2) {
            req.key = new TL_inputPrivacyKeyPhoneCall();
        } else if (this.rulesType == 1) {
            req.key = new TL_inputPrivacyKeyChatInvite();
        } else {
            req.key = new TL_inputPrivacyKeyStatusTimestamp();
        }
        if (this.currentType != 0 && this.currentPlus.size() > 0) {
            TL_inputPrivacyValueAllowUsers rule = new TL_inputPrivacyValueAllowUsers();
            for (a = 0; a < this.currentPlus.size(); a++) {
                user = MessagesController.getInstance(this.currentAccount).getUser((Integer) this.currentPlus.get(a));
                if (user != null) {
                    inputUser = MessagesController.getInstance(this.currentAccount).getInputUser(user);
                    if (inputUser != null) {
                        rule.users.add(inputUser);
                    }
                }
            }
            req.rules.add(rule);
        }
        if (this.currentType != 1 && this.currentMinus.size() > 0) {
            TL_inputPrivacyValueDisallowUsers rule2 = new TL_inputPrivacyValueDisallowUsers();
            for (a = 0; a < this.currentMinus.size(); a++) {
                user = MessagesController.getInstance(this.currentAccount).getUser((Integer) this.currentMinus.get(a));
                if (user != null) {
                    inputUser = MessagesController.getInstance(this.currentAccount).getInputUser(user);
                    if (inputUser != null) {
                        rule2.users.add(inputUser);
                    }
                }
            }
            req.rules.add(rule2);
        }
        if (this.currentType == 0) {
            req.rules.add(new TL_inputPrivacyValueAllowAll());
        } else if (this.currentType == 1) {
            req.rules.add(new TL_inputPrivacyValueDisallowAll());
        } else if (this.currentType == 2) {
            req.rules.add(new TL_inputPrivacyValueAllowContacts());
        }
        AlertDialog progressDialog = null;
        if (getParentActivity() != null) {
            progressDialog = new AlertDialog(getParentActivity(), 1);
            progressDialog.setMessage(LocaleController.getString("Loading", R.string.Loading));
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
        final AlertDialog progressDialogFinal = progressDialog;
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(req, new RequestDelegate() {
            public void run(final TLObject response, final TL_error error) {
                AndroidUtilities.runOnUIThread(new Runnable() {
                    public void run() {
                        try {
                            if (progressDialogFinal != null) {
                                progressDialogFinal.dismiss();
                            }
                        } catch (Throwable e) {
                            FileLog.e(e);
                        }
                        if (error == null) {
                            PrivacyControlActivity.this.finishFragment();
                            TL_account_privacyRules rules = response;
                            MessagesController.getInstance(PrivacyControlActivity.this.currentAccount).putUsers(rules.users, false);
                            ContactsController.getInstance(PrivacyControlActivity.this.currentAccount).setPrivacyRules(rules.rules, PrivacyControlActivity.this.rulesType);
                            return;
                        }
                        PrivacyControlActivity.this.showErrorAlert();
                    }
                });
            }
        }, 2);
    }

    private void showErrorAlert() {
        if (getParentActivity() != null) {
            Builder builder = new Builder(getParentActivity());
            builder.setTitle(LocaleController.getString("AppName", R.string.AppName));
            builder.setMessage(LocaleController.getString("PrivacyFloodControlError", R.string.PrivacyFloodControlError));
            builder.setPositiveButton(LocaleController.getString("OK", R.string.OK), null);
            showDialog(builder.create());
        }
    }

    private void checkPrivacy() {
        this.currentPlus = new ArrayList();
        this.currentMinus = new ArrayList();
        ArrayList<PrivacyRule> privacyRules = ContactsController.getInstance(this.currentAccount).getPrivacyRules(this.rulesType);
        if (privacyRules != null) {
            if (privacyRules.size() != 0) {
                int type = -1;
                for (int a = 0; a < privacyRules.size(); a++) {
                    PrivacyRule rule = (PrivacyRule) privacyRules.get(a);
                    if (rule instanceof TL_privacyValueAllowUsers) {
                        this.currentPlus.addAll(rule.users);
                    } else if (rule instanceof TL_privacyValueDisallowUsers) {
                        this.currentMinus.addAll(rule.users);
                    } else if (rule instanceof TL_privacyValueAllowAll) {
                        type = 0;
                    } else if (rule instanceof TL_privacyValueDisallowAll) {
                        type = 1;
                    } else {
                        type = 2;
                    }
                }
                if (type != 0) {
                    if (type != -1 || this.currentMinus.size() <= 0) {
                        if (type != 2) {
                            if (type != -1 || this.currentMinus.size() <= 0 || this.currentPlus.size() <= 0) {
                                if (type == 1 || (type == -1 && this.currentPlus.size() > 0)) {
                                    this.currentType = 1;
                                }
                                if (this.doneButton != null) {
                                    this.doneButton.setVisibility(8);
                                }
                                updateRows();
                                return;
                            }
                        }
                        this.currentType = 2;
                        if (this.doneButton != null) {
                            this.doneButton.setVisibility(8);
                        }
                        updateRows();
                        return;
                    }
                }
                this.currentType = 0;
                if (this.doneButton != null) {
                    this.doneButton.setVisibility(8);
                }
                updateRows();
                return;
            }
        }
        this.currentType = 1;
    }

    private void updateRows() {
        this.rowCount = 0;
        int i = this.rowCount;
        this.rowCount = i + 1;
        this.sectionRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.everybodyRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.myContactsRow = i;
        if (this.rulesType == 0 || this.rulesType == 2) {
            i = this.rowCount;
            this.rowCount = i + 1;
            this.nobodyRow = i;
        } else {
            this.nobodyRow = -1;
        }
        i = this.rowCount;
        this.rowCount = i + 1;
        this.detailRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.shareSectionRow = i;
        if (this.currentType != 1) {
            if (this.currentType != 2) {
                this.alwaysShareRow = -1;
                if (this.currentType != 0) {
                    if (this.currentType == 2) {
                        this.neverShareRow = -1;
                        i = this.rowCount;
                        this.rowCount = i + 1;
                        this.shareDetailRow = i;
                        if (this.listAdapter == null) {
                            this.listAdapter.notifyDataSetChanged();
                        }
                    }
                }
                i = this.rowCount;
                this.rowCount = i + 1;
                this.neverShareRow = i;
                i = this.rowCount;
                this.rowCount = i + 1;
                this.shareDetailRow = i;
                if (this.listAdapter == null) {
                    this.listAdapter.notifyDataSetChanged();
                }
            }
        }
        i = this.rowCount;
        this.rowCount = i + 1;
        this.alwaysShareRow = i;
        if (this.currentType != 0) {
            if (this.currentType == 2) {
                this.neverShareRow = -1;
                i = this.rowCount;
                this.rowCount = i + 1;
                this.shareDetailRow = i;
                if (this.listAdapter == null) {
                    this.listAdapter.notifyDataSetChanged();
                }
            }
        }
        i = this.rowCount;
        this.rowCount = i + 1;
        this.neverShareRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.shareDetailRow = i;
        if (this.listAdapter == null) {
            this.listAdapter.notifyDataSetChanged();
        }
    }

    public void onResume() {
        super.onResume();
        this.lastCheckedType = -1;
        this.enableAnimation = false;
    }

    public ThemeDescription[] getThemeDescriptions() {
        ThemeDescription[] themeDescriptionArr = new ThemeDescription[17];
        themeDescriptionArr[0] = new ThemeDescription(this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{TextSettingsCell.class, HeaderCell.class, RadioCell.class}, null, null, null, Theme.key_windowBackgroundWhite);
        themeDescriptionArr[1] = new ThemeDescription(this.fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_windowBackgroundGray);
        themeDescriptionArr[2] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_actionBarDefault);
        themeDescriptionArr[3] = new ThemeDescription(this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, Theme.key_actionBarDefault);
        themeDescriptionArr[4] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, Theme.key_actionBarDefaultIcon);
        themeDescriptionArr[5] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, Theme.key_actionBarDefaultTitle);
        themeDescriptionArr[6] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, Theme.key_actionBarDefaultSelector);
        themeDescriptionArr[7] = new ThemeDescription(this.listView, ThemeDescription.FLAG_SELECTOR, null, null, null, null, Theme.key_listSelector);
        themeDescriptionArr[8] = new ThemeDescription(this.listView, 0, new Class[]{View.class}, Theme.dividerPaint, null, null, Theme.key_divider);
        themeDescriptionArr[9] = new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{TextInfoPrivacyCell.class}, null, null, null, Theme.key_windowBackgroundGrayShadow);
        themeDescriptionArr[10] = new ThemeDescription(this.listView, 0, new Class[]{TextSettingsCell.class}, new String[]{"textView"}, null, null, null, Theme.key_windowBackgroundWhiteBlackText);
        themeDescriptionArr[11] = new ThemeDescription(this.listView, 0, new Class[]{TextSettingsCell.class}, new String[]{"valueTextView"}, null, null, null, Theme.key_windowBackgroundWhiteValueText);
        themeDescriptionArr[12] = new ThemeDescription(this.listView, 0, new Class[]{TextInfoPrivacyCell.class}, new String[]{"textView"}, null, null, null, Theme.key_windowBackgroundWhiteGrayText4);
        themeDescriptionArr[13] = new ThemeDescription(this.listView, 0, new Class[]{HeaderCell.class}, new String[]{"textView"}, null, null, null, Theme.key_windowBackgroundWhiteBlueHeader);
        themeDescriptionArr[14] = new ThemeDescription(this.listView, 0, new Class[]{RadioCell.class}, new String[]{"textView"}, null, null, null, Theme.key_windowBackgroundWhiteBlackText);
        themeDescriptionArr[15] = new ThemeDescription(this.listView, ThemeDescription.FLAG_CHECKBOX, new Class[]{RadioCell.class}, new String[]{"radioButton"}, null, null, null, Theme.key_radioBackground);
        themeDescriptionArr[16] = new ThemeDescription(this.listView, ThemeDescription.FLAG_CHECKBOXCHECK, new Class[]{RadioCell.class}, new String[]{"radioButton"}, null, null, null, Theme.key_radioBackgroundChecked);
        return themeDescriptionArr;
    }
}
