package org.telegram.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.os.Vibrator;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputFilter.LengthFilter;
import android.text.TextUtils.TruncateAt;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.text.method.PasswordTransformationMethod;
import android.view.ActionMode;
import android.view.ActionMode.Callback;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.NotificationCenter.NotificationCenterDelegate;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.beta.R;
import org.telegram.messenger.exoplayer2.C;
import org.telegram.messenger.exoplayer2.extractor.ts.TsExtractor;
import org.telegram.messenger.support.fingerprint.FingerprintManagerCompat;
import org.telegram.messenger.support.widget.LinearLayoutManager;
import org.telegram.messenger.support.widget.RecyclerView.Adapter;
import org.telegram.messenger.support.widget.RecyclerView.ViewHolder;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick;
import org.telegram.ui.ActionBar.ActionBarMenu;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.ActionBar.AlertDialog.Builder;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Cells.TextCheckCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Cells.TextSettingsCell;
import org.telegram.ui.Components.EditTextBoldCursor;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.NumberPicker;
import org.telegram.ui.Components.NumberPicker.Formatter;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.RecyclerListView.OnItemClickListener;
import org.telegram.ui.Components.RecyclerListView.SelectionAdapter;

public class PasscodeActivity extends BaseFragment implements NotificationCenterDelegate {
    private static final int done_button = 1;
    private static final int password_item = 3;
    private static final int pin_item = 2;
    private int autoLockDetailRow;
    private int autoLockRow;
    private int captureDetailRow;
    private int captureRow;
    private int changePasscodeRow;
    private int currentPasswordType = 0;
    private TextView dropDown;
    private ActionBarMenuItem dropDownContainer;
    private Drawable dropDownDrawable;
    private int fingerprintRow;
    private String firstPassword;
    private ListAdapter listAdapter;
    private RecyclerListView listView;
    private int passcodeDetailRow;
    private int passcodeRow;
    private int passcodeSetStep = 0;
    private EditTextBoldCursor passwordEditText;
    private int rowCount;
    private TextView titleTextView;
    private int type;

    private class ListAdapter extends SelectionAdapter {
        private Context mContext;

        public org.telegram.messenger.support.widget.RecyclerView.ViewHolder onCreateViewHolder(android.view.ViewGroup r1, int r2) {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.ui.PasscodeActivity.ListAdapter.onCreateViewHolder(android.view.ViewGroup, int):org.telegram.messenger.support.widget.RecyclerView$ViewHolder
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
            switch(r4) {
                case 0: goto L_0x001c;
                case 1: goto L_0x000b;
                default: goto L_0x0003;
            };
        L_0x0003:
            r0 = new org.telegram.ui.Cells.TextInfoPrivacyCell;
            r1 = r2.mContext;
            r0.<init>(r1);
            goto L_0x002d;
        L_0x000b:
            r0 = new org.telegram.ui.Cells.TextSettingsCell;
            r1 = r2.mContext;
            r0.<init>(r1);
            r1 = "windowBackgroundWhite";
            r1 = org.telegram.ui.ActionBar.Theme.getColor(r1);
            r0.setBackgroundColor(r1);
            goto L_0x002d;
        L_0x001c:
            r0 = new org.telegram.ui.Cells.TextCheckCell;
            r1 = r2.mContext;
            r0.<init>(r1);
            r1 = "windowBackgroundWhite";
            r1 = org.telegram.ui.ActionBar.Theme.getColor(r1);
            r0.setBackgroundColor(r1);
            r1 = new org.telegram.ui.Components.RecyclerListView$Holder;
            r1.<init>(r0);
            return r1;
            */
            throw new UnsupportedOperationException("Method not decompiled: org.telegram.ui.PasscodeActivity.ListAdapter.onCreateViewHolder(android.view.ViewGroup, int):org.telegram.messenger.support.widget.RecyclerView$ViewHolder");
        }

        public ListAdapter(Context context) {
            this.mContext = context;
        }

        public boolean isEnabled(ViewHolder holder) {
            int position = holder.getAdapterPosition();
            if (!(position == PasscodeActivity.this.passcodeRow || position == PasscodeActivity.this.fingerprintRow || position == PasscodeActivity.this.autoLockRow || position == PasscodeActivity.this.captureRow)) {
                if (SharedConfig.passcodeHash.length() == 0 || position != PasscodeActivity.this.changePasscodeRow) {
                    return false;
                }
            }
            return true;
        }

        public int getItemCount() {
            return PasscodeActivity.this.rowCount;
        }

        public void onBindViewHolder(ViewHolder holder, int position) {
            boolean z = false;
            switch (holder.getItemViewType()) {
                case 0:
                    TextCheckCell textCell = holder.itemView;
                    if (position == PasscodeActivity.this.passcodeRow) {
                        String string = LocaleController.getString("Passcode", R.string.Passcode);
                        if (SharedConfig.passcodeHash.length() > 0) {
                            z = true;
                        }
                        textCell.setTextAndCheck(string, z, true);
                        return;
                    } else if (position == PasscodeActivity.this.fingerprintRow) {
                        textCell.setTextAndCheck(LocaleController.getString("UnlockFingerprint", R.string.UnlockFingerprint), SharedConfig.useFingerprint, true);
                        return;
                    } else if (position == PasscodeActivity.this.captureRow) {
                        textCell.setTextAndCheck(LocaleController.getString("ScreenCapture", R.string.ScreenCapture), SharedConfig.allowScreenCapture, false);
                        return;
                    } else {
                        return;
                    }
                case 1:
                    TextSettingsCell textCell2 = holder.itemView;
                    if (position == PasscodeActivity.this.changePasscodeRow) {
                        textCell2.setText(LocaleController.getString("ChangePasscode", R.string.ChangePasscode), false);
                        if (SharedConfig.passcodeHash.length() == 0) {
                            textCell2.setTag(Theme.key_windowBackgroundWhiteGrayText7);
                            textCell2.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteGrayText7));
                            return;
                        }
                        textCell2.setTag(Theme.key_windowBackgroundWhiteBlackText);
                        textCell2.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText));
                        return;
                    } else if (position == PasscodeActivity.this.autoLockRow) {
                        String val;
                        if (SharedConfig.autoLockIn == 0) {
                            val = LocaleController.formatString("AutoLockDisabled", R.string.AutoLockDisabled, new Object[0]);
                        } else if (SharedConfig.autoLockIn < 3600) {
                            val = LocaleController.formatString("AutoLockInTime", R.string.AutoLockInTime, LocaleController.formatPluralString("Minutes", SharedConfig.autoLockIn / 60));
                        } else if (SharedConfig.autoLockIn < 86400) {
                            val = LocaleController.formatString("AutoLockInTime", R.string.AutoLockInTime, LocaleController.formatPluralString("Hours", (int) Math.ceil((double) ((((float) SharedConfig.autoLockIn) / 60.0f) / 60.0f))));
                        } else {
                            val = LocaleController.formatString("AutoLockInTime", R.string.AutoLockInTime, LocaleController.formatPluralString("Days", (int) Math.ceil((double) (((((float) SharedConfig.autoLockIn) / 60.0f) / 60.0f) / 24.0f))));
                            textCell2.setTextAndValue(LocaleController.getString("AutoLock", R.string.AutoLock), val, true);
                            textCell2.setTag(Theme.key_windowBackgroundWhiteBlackText);
                            textCell2.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText));
                            return;
                        }
                        textCell2.setTextAndValue(LocaleController.getString("AutoLock", R.string.AutoLock), val, true);
                        textCell2.setTag(Theme.key_windowBackgroundWhiteBlackText);
                        textCell2.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText));
                        return;
                    } else {
                        return;
                    }
                case 2:
                    TextInfoPrivacyCell cell = holder.itemView;
                    if (position == PasscodeActivity.this.passcodeDetailRow) {
                        cell.setText(LocaleController.getString("ChangePasscodeInfo", R.string.ChangePasscodeInfo));
                        if (PasscodeActivity.this.autoLockDetailRow != -1) {
                            cell.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, R.drawable.greydivider, Theme.key_windowBackgroundGrayShadow));
                            return;
                        } else {
                            cell.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, R.drawable.greydivider_bottom, Theme.key_windowBackgroundGrayShadow));
                            return;
                        }
                    } else if (position == PasscodeActivity.this.autoLockDetailRow) {
                        cell.setText(LocaleController.getString("AutoLockInfo", R.string.AutoLockInfo));
                        cell.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, R.drawable.greydivider, Theme.key_windowBackgroundGrayShadow));
                        return;
                    } else if (position == PasscodeActivity.this.captureDetailRow) {
                        cell.setText(LocaleController.getString("ScreenCaptureInfo", R.string.ScreenCaptureInfo));
                        cell.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, R.drawable.greydivider_bottom, Theme.key_windowBackgroundGrayShadow));
                        return;
                    } else {
                        return;
                    }
                default:
                    return;
            }
        }

        public int getItemViewType(int position) {
            if (!(position == PasscodeActivity.this.passcodeRow || position == PasscodeActivity.this.fingerprintRow)) {
                if (position != PasscodeActivity.this.captureRow) {
                    if (position != PasscodeActivity.this.changePasscodeRow) {
                        if (position != PasscodeActivity.this.autoLockRow) {
                            if (!(position == PasscodeActivity.this.passcodeDetailRow || position == PasscodeActivity.this.autoLockDetailRow)) {
                                if (position != PasscodeActivity.this.captureDetailRow) {
                                    return 0;
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

    public PasscodeActivity(int type) {
        this.type = type;
    }

    public boolean onFragmentCreate() {
        super.onFragmentCreate();
        updateRows();
        if (this.type == 0) {
            NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.didSetPasscode);
        }
        return true;
    }

    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        if (this.type == 0) {
            NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.didSetPasscode);
        }
    }

    public View createView(Context context) {
        Context context2 = context;
        if (this.type != 3) {
            r0.actionBar.setBackButtonImage(R.drawable.ic_ab_back);
        }
        r0.actionBar.setAllowOverlayTitle(false);
        r0.actionBar.setActionBarMenuOnItemClick(new ActionBarMenuOnItemClick() {
            public void onItemClick(int id) {
                if (id == -1) {
                    PasscodeActivity.this.finishFragment();
                } else if (id == 1) {
                    if (PasscodeActivity.this.passcodeSetStep == 0) {
                        PasscodeActivity.this.processNext();
                    } else if (PasscodeActivity.this.passcodeSetStep == 1) {
                        PasscodeActivity.this.processDone();
                    }
                } else if (id == 2) {
                    PasscodeActivity.this.currentPasswordType = 0;
                    PasscodeActivity.this.updateDropDownTextView();
                } else if (id == 3) {
                    PasscodeActivity.this.currentPasswordType = 1;
                    PasscodeActivity.this.updateDropDownTextView();
                }
            }
        });
        r0.fragmentView = new FrameLayout(context2);
        FrameLayout frameLayout = r0.fragmentView;
        if (r0.type != 0) {
            ActionBarMenu menu = r0.actionBar.createMenu();
            float f = 56.0f;
            menu.addItemWithWidth(1, R.drawable.ic_done, AndroidUtilities.dp(56.0f));
            r0.titleTextView = new TextView(context2);
            r0.titleTextView.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteGrayText6));
            if (r0.type != 1) {
                r0.titleTextView.setText(LocaleController.getString("EnterCurrentPasscode", R.string.EnterCurrentPasscode));
            } else if (SharedConfig.passcodeHash.length() != 0) {
                r0.titleTextView.setText(LocaleController.getString("EnterNewPasscode", R.string.EnterNewPasscode));
            } else {
                r0.titleTextView.setText(LocaleController.getString("EnterNewFirstPasscode", R.string.EnterNewFirstPasscode));
            }
            r0.titleTextView.setTextSize(1, 18.0f);
            r0.titleTextView.setGravity(1);
            frameLayout.addView(r0.titleTextView, LayoutHelper.createFrame(-2, -2.0f, 1, 0.0f, 38.0f, 0.0f, 0.0f));
            r0.passwordEditText = new EditTextBoldCursor(context2);
            r0.passwordEditText.setTextSize(1, 20.0f);
            r0.passwordEditText.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText));
            r0.passwordEditText.setBackgroundDrawable(Theme.createEditTextDrawable(context2, false));
            r0.passwordEditText.setMaxLines(1);
            r0.passwordEditText.setLines(1);
            r0.passwordEditText.setGravity(1);
            r0.passwordEditText.setSingleLine(true);
            if (r0.type == 1) {
                r0.passcodeSetStep = 0;
                r0.passwordEditText.setImeOptions(5);
            } else {
                r0.passcodeSetStep = 1;
                r0.passwordEditText.setImeOptions(6);
            }
            r0.passwordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
            r0.passwordEditText.setTypeface(Typeface.DEFAULT);
            r0.passwordEditText.setCursorColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText));
            r0.passwordEditText.setCursorSize(AndroidUtilities.dp(20.0f));
            r0.passwordEditText.setCursorWidth(1.5f);
            frameLayout.addView(r0.passwordEditText, LayoutHelper.createFrame(-1, 36.0f, 51, 40.0f, 90.0f, 40.0f, 0.0f));
            r0.passwordEditText.setOnEditorActionListener(new OnEditorActionListener() {
                public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                    if (PasscodeActivity.this.passcodeSetStep == 0) {
                        PasscodeActivity.this.processNext();
                        return true;
                    } else if (PasscodeActivity.this.passcodeSetStep != 1) {
                        return false;
                    } else {
                        PasscodeActivity.this.processDone();
                        return true;
                    }
                }
            });
            r0.passwordEditText.addTextChangedListener(new TextWatcher() {
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                public void afterTextChanged(Editable s) {
                    if (PasscodeActivity.this.passwordEditText.length() != 4) {
                        return;
                    }
                    if (PasscodeActivity.this.type == 2 && SharedConfig.passcodeType == 0) {
                        PasscodeActivity.this.processDone();
                    } else if (PasscodeActivity.this.type != 1 || PasscodeActivity.this.currentPasswordType != 0) {
                    } else {
                        if (PasscodeActivity.this.passcodeSetStep == 0) {
                            PasscodeActivity.this.processNext();
                        } else if (PasscodeActivity.this.passcodeSetStep == 1) {
                            PasscodeActivity.this.processDone();
                        }
                    }
                }
            });
            r0.passwordEditText.setCustomSelectionActionModeCallback(new Callback() {
                public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                    return false;
                }

                public void onDestroyActionMode(ActionMode mode) {
                }

                public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                    return false;
                }

                public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                    return false;
                }
            });
            if (r0.type == 1) {
                frameLayout.setTag(Theme.key_windowBackgroundWhite);
                r0.dropDownContainer = new ActionBarMenuItem(context2, menu, 0, 0);
                r0.dropDownContainer.setSubMenuOpenSide(1);
                r0.dropDownContainer.addSubItem(2, LocaleController.getString("PasscodePIN", R.string.PasscodePIN));
                r0.dropDownContainer.addSubItem(3, LocaleController.getString("PasscodePassword", R.string.PasscodePassword));
                ActionBar actionBar = r0.actionBar;
                View view = r0.dropDownContainer;
                if (AndroidUtilities.isTablet()) {
                    f = 64.0f;
                }
                actionBar.addView(view, LayoutHelper.createFrame(-2, -1.0f, 51, f, 0.0f, 40.0f, 0.0f));
                r0.dropDownContainer.setOnClickListener(new OnClickListener() {
                    public void onClick(View view) {
                        PasscodeActivity.this.dropDownContainer.toggleSubMenu();
                    }
                });
                r0.dropDown = new TextView(context2);
                r0.dropDown.setGravity(3);
                r0.dropDown.setSingleLine(true);
                r0.dropDown.setLines(1);
                r0.dropDown.setMaxLines(1);
                r0.dropDown.setEllipsize(TruncateAt.END);
                r0.dropDown.setTextColor(Theme.getColor(Theme.key_actionBarDefaultTitle));
                r0.dropDown.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
                r0.dropDownDrawable = context.getResources().getDrawable(R.drawable.ic_arrow_drop_down).mutate();
                r0.dropDownDrawable.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_actionBarDefaultTitle), Mode.MULTIPLY));
                r0.dropDown.setCompoundDrawablesWithIntrinsicBounds(null, null, r0.dropDownDrawable, null);
                r0.dropDown.setCompoundDrawablePadding(AndroidUtilities.dp(4.0f));
                r0.dropDown.setPadding(0, 0, AndroidUtilities.dp(10.0f), 0);
                r0.dropDownContainer.addView(r0.dropDown, LayoutHelper.createFrame(-2, -2.0f, 16, 16.0f, 0.0f, 0.0f, 1.0f));
            } else {
                r0.actionBar.setTitle(LocaleController.getString("Passcode", R.string.Passcode));
            }
            updateDropDownTextView();
        } else {
            r0.actionBar.setTitle(LocaleController.getString("Passcode", R.string.Passcode));
            frameLayout.setTag(Theme.key_windowBackgroundGray);
            frameLayout.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundGray));
            r0.listView = new RecyclerListView(context2);
            r0.listView.setLayoutManager(new LinearLayoutManager(context2, 1, false) {
                public boolean supportsPredictiveItemAnimations() {
                    return false;
                }
            });
            r0.listView.setVerticalScrollBarEnabled(false);
            r0.listView.setItemAnimator(null);
            r0.listView.setLayoutAnimation(null);
            frameLayout.addView(r0.listView, LayoutHelper.createFrame(-1, -1.0f));
            RecyclerListView recyclerListView = r0.listView;
            Adapter listAdapter = new ListAdapter(context2);
            r0.listAdapter = listAdapter;
            recyclerListView.setAdapter(listAdapter);
            r0.listView.setOnItemClickListener(new OnItemClickListener() {
                public void onItemClick(View view, final int position) {
                    if (view.isEnabled()) {
                        boolean z = true;
                        if (position == PasscodeActivity.this.changePasscodeRow) {
                            PasscodeActivity.this.presentFragment(new PasscodeActivity(1));
                        } else if (position == PasscodeActivity.this.passcodeRow) {
                            TextCheckCell cell = (TextCheckCell) view;
                            if (SharedConfig.passcodeHash.length() != 0) {
                                SharedConfig.passcodeHash = TtmlNode.ANONYMOUS_REGION_ID;
                                SharedConfig.appLocked = false;
                                SharedConfig.saveConfig();
                                int count = PasscodeActivity.this.listView.getChildCount();
                                for (int a = 0; a < count; a++) {
                                    View child = PasscodeActivity.this.listView.getChildAt(a);
                                    if (child instanceof TextSettingsCell) {
                                        ((TextSettingsCell) child).setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteGrayText7));
                                        break;
                                    }
                                }
                                if (SharedConfig.passcodeHash.length() == 0) {
                                    z = false;
                                }
                                cell.setChecked(z);
                                NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.didSetPasscode, new Object[0]);
                            } else {
                                PasscodeActivity.this.presentFragment(new PasscodeActivity(1));
                            }
                        } else if (position == PasscodeActivity.this.autoLockRow) {
                            if (PasscodeActivity.this.getParentActivity() != null) {
                                Builder builder = new Builder(PasscodeActivity.this.getParentActivity());
                                builder.setTitle(LocaleController.getString("AutoLock", R.string.AutoLock));
                                final NumberPicker numberPicker = new NumberPicker(PasscodeActivity.this.getParentActivity());
                                numberPicker.setMinValue(0);
                                numberPicker.setMaxValue(4);
                                if (SharedConfig.autoLockIn == 0) {
                                    numberPicker.setValue(0);
                                } else if (SharedConfig.autoLockIn == 60) {
                                    numberPicker.setValue(1);
                                } else if (SharedConfig.autoLockIn == 300) {
                                    numberPicker.setValue(2);
                                } else if (SharedConfig.autoLockIn == 3600) {
                                    numberPicker.setValue(3);
                                } else if (SharedConfig.autoLockIn == 18000) {
                                    numberPicker.setValue(4);
                                }
                                numberPicker.setFormatter(new Formatter() {
                                    public String format(int value) {
                                        if (value == 0) {
                                            return LocaleController.getString("AutoLockDisabled", R.string.AutoLockDisabled);
                                        }
                                        if (value == 1) {
                                            return LocaleController.formatString("AutoLockInTime", R.string.AutoLockInTime, LocaleController.formatPluralString("Minutes", 1));
                                        } else if (value == 2) {
                                            return LocaleController.formatString("AutoLockInTime", R.string.AutoLockInTime, LocaleController.formatPluralString("Minutes", 5));
                                        } else if (value == 3) {
                                            return LocaleController.formatString("AutoLockInTime", R.string.AutoLockInTime, LocaleController.formatPluralString("Hours", 1));
                                        } else if (value != 4) {
                                            return TtmlNode.ANONYMOUS_REGION_ID;
                                        } else {
                                            return LocaleController.formatString("AutoLockInTime", R.string.AutoLockInTime, LocaleController.formatPluralString("Hours", 5));
                                        }
                                    }
                                });
                                builder.setView(numberPicker);
                                builder.setNegativeButton(LocaleController.getString("Done", R.string.Done), new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        which = numberPicker.getValue();
                                        if (which == 0) {
                                            SharedConfig.autoLockIn = 0;
                                        } else if (which == 1) {
                                            SharedConfig.autoLockIn = 60;
                                        } else if (which == 2) {
                                            SharedConfig.autoLockIn = 300;
                                        } else if (which == 3) {
                                            SharedConfig.autoLockIn = 3600;
                                        } else if (which == 4) {
                                            SharedConfig.autoLockIn = 18000;
                                        }
                                        PasscodeActivity.this.listAdapter.notifyItemChanged(position);
                                        UserConfig.getInstance(PasscodeActivity.this.currentAccount).saveConfig(false);
                                    }
                                });
                                PasscodeActivity.this.showDialog(builder.create());
                            }
                        } else if (position == PasscodeActivity.this.fingerprintRow) {
                            SharedConfig.useFingerprint ^= true;
                            UserConfig.getInstance(PasscodeActivity.this.currentAccount).saveConfig(false);
                            ((TextCheckCell) view).setChecked(SharedConfig.useFingerprint);
                        } else if (position == PasscodeActivity.this.captureRow) {
                            SharedConfig.allowScreenCapture ^= true;
                            UserConfig.getInstance(PasscodeActivity.this.currentAccount).saveConfig(false);
                            ((TextCheckCell) view).setChecked(SharedConfig.allowScreenCapture);
                            NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.didSetPasscode, new Object[0]);
                        }
                    }
                }
            });
        }
        return r0.fragmentView;
    }

    public void onResume() {
        super.onResume();
        if (this.listAdapter != null) {
            this.listAdapter.notifyDataSetChanged();
        }
        if (this.type != 0) {
            AndroidUtilities.runOnUIThread(new Runnable() {
                public void run() {
                    if (PasscodeActivity.this.passwordEditText != null) {
                        PasscodeActivity.this.passwordEditText.requestFocus();
                        AndroidUtilities.showKeyboard(PasscodeActivity.this.passwordEditText);
                    }
                }
            }, 200);
        }
        fixLayoutInternal();
    }

    public void didReceivedNotification(int id, int account, Object... args) {
        if (id == NotificationCenter.didSetPasscode && this.type == 0) {
            updateRows();
            if (this.listAdapter != null) {
                this.listAdapter.notifyDataSetChanged();
            }
        }
    }

    private void updateRows() {
        this.rowCount = 0;
        int i = this.rowCount;
        this.rowCount = i + 1;
        this.passcodeRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.changePasscodeRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.passcodeDetailRow = i;
        if (SharedConfig.passcodeHash.length() > 0) {
            try {
                if (VERSION.SDK_INT >= 23 && FingerprintManagerCompat.from(ApplicationLoader.applicationContext).isHardwareDetected()) {
                    int i2 = this.rowCount;
                    this.rowCount = i2 + 1;
                    this.fingerprintRow = i2;
                }
            } catch (Throwable e) {
                FileLog.e(e);
            }
            i = this.rowCount;
            this.rowCount = i + 1;
            this.autoLockRow = i;
            i = this.rowCount;
            this.rowCount = i + 1;
            this.autoLockDetailRow = i;
            i = this.rowCount;
            this.rowCount = i + 1;
            this.captureRow = i;
            i = this.rowCount;
            this.rowCount = i + 1;
            this.captureDetailRow = i;
            return;
        }
        this.captureRow = -1;
        this.captureDetailRow = -1;
        this.fingerprintRow = -1;
        this.autoLockRow = -1;
        this.autoLockDetailRow = -1;
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (this.listView != null) {
            this.listView.getViewTreeObserver().addOnPreDrawListener(new OnPreDrawListener() {
                public boolean onPreDraw() {
                    PasscodeActivity.this.listView.getViewTreeObserver().removeOnPreDrawListener(this);
                    PasscodeActivity.this.fixLayoutInternal();
                    return true;
                }
            });
        }
    }

    public void onTransitionAnimationEnd(boolean isOpen, boolean backward) {
        if (isOpen && this.type != 0) {
            AndroidUtilities.showKeyboard(this.passwordEditText);
        }
    }

    private void updateDropDownTextView() {
        if (this.dropDown != null) {
            if (this.currentPasswordType == 0) {
                this.dropDown.setText(LocaleController.getString("PasscodePIN", R.string.PasscodePIN));
            } else if (this.currentPasswordType == 1) {
                this.dropDown.setText(LocaleController.getString("PasscodePassword", R.string.PasscodePassword));
            }
        }
        if ((this.type == 1 && this.currentPasswordType == 0) || (this.type == 2 && SharedConfig.passcodeType == 0)) {
            this.passwordEditText.setFilters(new InputFilter[]{new LengthFilter(4)});
            this.passwordEditText.setInputType(3);
            this.passwordEditText.setKeyListener(DigitsKeyListener.getInstance("1234567890"));
        } else if ((this.type == 1 && this.currentPasswordType == 1) || (this.type == 2 && SharedConfig.passcodeType == 1)) {
            this.passwordEditText.setFilters(new InputFilter[0]);
            this.passwordEditText.setKeyListener(null);
            this.passwordEditText.setInputType(TsExtractor.TS_STREAM_TYPE_AC3);
        }
        this.passwordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
    }

    private void processNext() {
        if (this.passwordEditText.getText().length() != 0) {
            if (this.currentPasswordType != 0 || this.passwordEditText.getText().length() == 4) {
                if (this.currentPasswordType == 0) {
                    this.actionBar.setTitle(LocaleController.getString("PasscodePIN", R.string.PasscodePIN));
                } else {
                    this.actionBar.setTitle(LocaleController.getString("PasscodePassword", R.string.PasscodePassword));
                }
                this.dropDownContainer.setVisibility(8);
                this.titleTextView.setText(LocaleController.getString("ReEnterYourPasscode", R.string.ReEnterYourPasscode));
                this.firstPassword = this.passwordEditText.getText().toString();
                this.passwordEditText.setText(TtmlNode.ANONYMOUS_REGION_ID);
                this.passcodeSetStep = 1;
                return;
            }
        }
        onPasscodeError();
    }

    private void processDone() {
        if (this.passwordEditText.getText().length() == 0) {
            onPasscodeError();
            return;
        }
        if (this.type == 1) {
            if (this.firstPassword.equals(this.passwordEditText.getText().toString())) {
                try {
                    SharedConfig.passcodeSalt = new byte[16];
                    Utilities.random.nextBytes(SharedConfig.passcodeSalt);
                    byte[] passcodeBytes = this.firstPassword.getBytes(C.UTF8_NAME);
                    byte[] bytes = new byte[(32 + passcodeBytes.length)];
                    System.arraycopy(SharedConfig.passcodeSalt, 0, bytes, 0, 16);
                    System.arraycopy(passcodeBytes, 0, bytes, 16, passcodeBytes.length);
                    System.arraycopy(SharedConfig.passcodeSalt, 0, bytes, passcodeBytes.length + 16, 16);
                    SharedConfig.passcodeHash = Utilities.bytesToHex(Utilities.computeSHA256(bytes, 0, bytes.length));
                } catch (Throwable e) {
                    FileLog.e(e);
                }
                SharedConfig.passcodeType = this.currentPasswordType;
                SharedConfig.saveConfig();
                finishFragment();
                NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.didSetPasscode, new Object[0]);
                this.passwordEditText.clearFocus();
                AndroidUtilities.hideKeyboard(this.passwordEditText);
            } else {
                try {
                    Toast.makeText(getParentActivity(), LocaleController.getString("PasscodeDoNotMatch", R.string.PasscodeDoNotMatch), 0).show();
                } catch (Throwable e2) {
                    FileLog.e(e2);
                }
                AndroidUtilities.shakeView(this.titleTextView, 2.0f, 0);
                this.passwordEditText.setText(TtmlNode.ANONYMOUS_REGION_ID);
            }
        } else if (this.type == 2) {
            if (SharedConfig.checkPasscode(this.passwordEditText.getText().toString())) {
                this.passwordEditText.clearFocus();
                AndroidUtilities.hideKeyboard(this.passwordEditText);
                presentFragment(new PasscodeActivity(0), true);
            } else {
                this.passwordEditText.setText(TtmlNode.ANONYMOUS_REGION_ID);
                onPasscodeError();
            }
        }
    }

    private void onPasscodeError() {
        if (getParentActivity() != null) {
            Vibrator v = (Vibrator) getParentActivity().getSystemService("vibrator");
            if (v != null) {
                v.vibrate(200);
            }
            AndroidUtilities.shakeView(this.titleTextView, 2.0f, 0);
        }
    }

    private void fixLayoutInternal() {
        if (this.dropDownContainer != null) {
            if (!AndroidUtilities.isTablet()) {
                LayoutParams layoutParams = (LayoutParams) this.dropDownContainer.getLayoutParams();
                layoutParams.topMargin = VERSION.SDK_INT >= 21 ? AndroidUtilities.statusBarHeight : 0;
                this.dropDownContainer.setLayoutParams(layoutParams);
            }
            if (AndroidUtilities.isTablet() || ApplicationLoader.applicationContext.getResources().getConfiguration().orientation != 2) {
                this.dropDown.setTextSize(20.0f);
            } else {
                this.dropDown.setTextSize(18.0f);
            }
        }
    }

    public ThemeDescription[] getThemeDescriptions() {
        ThemeDescription[] themeDescriptionArr = new ThemeDescription[28];
        themeDescriptionArr[0] = new ThemeDescription(this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{TextCheckCell.class, TextSettingsCell.class}, null, null, null, Theme.key_windowBackgroundWhite);
        themeDescriptionArr[1] = new ThemeDescription(this.fragmentView, ThemeDescription.FLAG_BACKGROUND | ThemeDescription.FLAG_CHECKTAG, null, null, null, null, Theme.key_windowBackgroundWhite);
        themeDescriptionArr[2] = new ThemeDescription(this.fragmentView, ThemeDescription.FLAG_CHECKTAG | ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_windowBackgroundGray);
        themeDescriptionArr[3] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_actionBarDefault);
        themeDescriptionArr[4] = new ThemeDescription(this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, Theme.key_actionBarDefault);
        themeDescriptionArr[5] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, Theme.key_actionBarDefaultIcon);
        themeDescriptionArr[6] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, Theme.key_actionBarDefaultTitle);
        themeDescriptionArr[7] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, Theme.key_actionBarDefaultSelector);
        themeDescriptionArr[8] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SUBMENUBACKGROUND, null, null, null, null, Theme.key_actionBarDefaultSubmenuBackground);
        themeDescriptionArr[9] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SUBMENUITEM, null, null, null, null, Theme.key_actionBarDefaultSubmenuItem);
        themeDescriptionArr[10] = new ThemeDescription(this.listView, ThemeDescription.FLAG_SELECTOR, null, null, null, null, Theme.key_listSelector);
        themeDescriptionArr[11] = new ThemeDescription(this.listView, 0, new Class[]{View.class}, Theme.dividerPaint, null, null, Theme.key_divider);
        themeDescriptionArr[12] = new ThemeDescription(this.titleTextView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, Theme.key_windowBackgroundWhiteGrayText6);
        themeDescriptionArr[13] = new ThemeDescription(this.passwordEditText, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, Theme.key_windowBackgroundWhiteBlackText);
        themeDescriptionArr[14] = new ThemeDescription(this.passwordEditText, ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, Theme.key_windowBackgroundWhiteInputField);
        themeDescriptionArr[15] = new ThemeDescription(this.passwordEditText, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, null, null, null, null, Theme.key_windowBackgroundWhiteInputFieldActivated);
        themeDescriptionArr[16] = new ThemeDescription(this.dropDown, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, Theme.key_actionBarDefaultTitle);
        themeDescriptionArr[17] = new ThemeDescription(this.dropDown, 0, null, null, new Drawable[]{this.dropDownDrawable}, null, Theme.key_actionBarDefaultTitle);
        themeDescriptionArr[18] = new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"textView"}, null, null, null, Theme.key_windowBackgroundWhiteBlackText);
        themeDescriptionArr[19] = new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"checkBox"}, null, null, null, Theme.key_switchThumb);
        themeDescriptionArr[20] = new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"checkBox"}, null, null, null, Theme.key_switchTrack);
        themeDescriptionArr[21] = new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"checkBox"}, null, null, null, Theme.key_switchThumbChecked);
        themeDescriptionArr[22] = new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"checkBox"}, null, null, null, Theme.key_switchTrackChecked);
        themeDescriptionArr[23] = new ThemeDescription(this.listView, ThemeDescription.FLAG_CHECKTAG, new Class[]{TextSettingsCell.class}, new String[]{"textView"}, null, null, null, Theme.key_windowBackgroundWhiteBlackText);
        themeDescriptionArr[24] = new ThemeDescription(this.listView, ThemeDescription.FLAG_CHECKTAG, new Class[]{TextSettingsCell.class}, new String[]{"textView"}, null, null, null, Theme.key_windowBackgroundWhiteGrayText7);
        themeDescriptionArr[25] = new ThemeDescription(this.listView, 0, new Class[]{TextSettingsCell.class}, new String[]{"valueTextView"}, null, null, null, Theme.key_windowBackgroundWhiteValueText);
        themeDescriptionArr[26] = new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{TextInfoPrivacyCell.class}, null, null, null, Theme.key_windowBackgroundGrayShadow);
        themeDescriptionArr[27] = new ThemeDescription(this.listView, 0, new Class[]{TextInfoPrivacyCell.class}, new String[]{"textView"}, null, null, null, Theme.key_windowBackgroundWhiteGrayText4);
        return themeDescriptionArr;
    }
}
