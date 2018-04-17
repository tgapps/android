package org.telegram.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Vibrator;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputFilter.LengthFilter;
import android.text.TextUtils.TruncateAt;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.beta.R;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_contacts_importContacts;
import org.telegram.tgnet.TLRPC.TL_contacts_importedContacts;
import org.telegram.tgnet.TLRPC.TL_error;
import org.telegram.tgnet.TLRPC.TL_inputPhoneContact;
import org.telegram.tgnet.TLRPC.User;
import org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.ActionBar.AlertDialog.Builder;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.ActionBar.ThemeDescription.ThemeDescriptionDelegate;
import org.telegram.ui.Components.AlertsCreator;
import org.telegram.ui.Components.AvatarDrawable;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.ui.Components.ContextProgressView;
import org.telegram.ui.Components.EditTextBoldCursor;
import org.telegram.ui.Components.HintEditText;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.CountrySelectActivity.CountrySelectActivityDelegate;

public class NewContactActivity extends BaseFragment implements OnItemSelectedListener {
    private static final int done_button = 1;
    private AvatarDrawable avatarDrawable;
    private BackupImageView avatarImage;
    private EditTextBoldCursor codeField;
    private HashMap<String, String> codesMap = new HashMap();
    private ArrayList<String> countriesArray = new ArrayList();
    private HashMap<String, String> countriesMap = new HashMap();
    private TextView countryButton;
    private int countryState;
    private boolean donePressed;
    private ActionBarMenuItem editDoneItem;
    private AnimatorSet editDoneItemAnimation;
    private ContextProgressView editDoneItemProgress;
    private EditTextBoldCursor firstNameField;
    private boolean ignoreOnPhoneChange;
    private boolean ignoreOnTextChange;
    private boolean ignoreSelection;
    private EditTextBoldCursor lastNameField;
    private View lineView;
    private HintEditText phoneField;
    private HashMap<String, String> phoneFormatMap = new HashMap();
    private TextView textView;

    public View createView(Context context) {
        Context context2 = context;
        this.actionBar.setBackButtonImage(R.drawable.ic_ab_back);
        this.actionBar.setAllowOverlayTitle(true);
        this.actionBar.setTitle(LocaleController.getString("AddContactTitle", R.string.AddContactTitle));
        this.actionBar.setActionBarMenuOnItemClick(new ActionBarMenuOnItemClick() {
            public void onItemClick(int id) {
                if (id == -1) {
                    NewContactActivity.this.finishFragment();
                } else if (id == 1 && !NewContactActivity.this.donePressed) {
                    Vibrator v;
                    if (NewContactActivity.this.firstNameField.length() == 0) {
                        v = (Vibrator) NewContactActivity.this.getParentActivity().getSystemService("vibrator");
                        if (v != null) {
                            v.vibrate(200);
                        }
                        AndroidUtilities.shakeView(NewContactActivity.this.firstNameField, 2.0f, 0);
                    } else if (NewContactActivity.this.codeField.length() == 0) {
                        v = (Vibrator) NewContactActivity.this.getParentActivity().getSystemService("vibrator");
                        if (v != null) {
                            v.vibrate(200);
                        }
                        AndroidUtilities.shakeView(NewContactActivity.this.codeField, 2.0f, 0);
                    } else if (NewContactActivity.this.phoneField.length() == 0) {
                        v = (Vibrator) NewContactActivity.this.getParentActivity().getSystemService("vibrator");
                        if (v != null) {
                            v.vibrate(200);
                        }
                        AndroidUtilities.shakeView(NewContactActivity.this.phoneField, 2.0f, 0);
                    } else {
                        NewContactActivity.this.donePressed = true;
                        NewContactActivity.this.showEditDoneProgress(true, true);
                        final TL_contacts_importContacts req = new TL_contacts_importContacts();
                        final TL_inputPhoneContact inputPhoneContact = new TL_inputPhoneContact();
                        inputPhoneContact.first_name = NewContactActivity.this.firstNameField.getText().toString();
                        inputPhoneContact.last_name = NewContactActivity.this.lastNameField.getText().toString();
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("+");
                        stringBuilder.append(NewContactActivity.this.codeField.getText().toString());
                        stringBuilder.append(NewContactActivity.this.phoneField.getText().toString());
                        inputPhoneContact.phone = stringBuilder.toString();
                        req.contacts.add(inputPhoneContact);
                        ConnectionsManager.getInstance(NewContactActivity.this.currentAccount).bindRequestToGuid(ConnectionsManager.getInstance(NewContactActivity.this.currentAccount).sendRequest(req, new RequestDelegate() {
                            public void run(TLObject response, final TL_error error) {
                                final TL_contacts_importedContacts res = (TL_contacts_importedContacts) response;
                                AndroidUtilities.runOnUIThread(new Runnable() {
                                    public void run() {
                                        NewContactActivity.this.donePressed = false;
                                        if (res == null) {
                                            NewContactActivity.this.showEditDoneProgress(false, true);
                                            AlertsCreator.processError(NewContactActivity.this.currentAccount, error, NewContactActivity.this, req, new Object[0]);
                                        } else if (!res.users.isEmpty()) {
                                            MessagesController.getInstance(NewContactActivity.this.currentAccount).putUsers(res.users, false);
                                            MessagesController.openChatOrProfileWith((User) res.users.get(0), null, NewContactActivity.this, 1, true);
                                        } else if (NewContactActivity.this.getParentActivity() != null) {
                                            NewContactActivity.this.showEditDoneProgress(false, true);
                                            Builder builder = new Builder(NewContactActivity.this.getParentActivity());
                                            builder.setTitle(LocaleController.getString("AppName", R.string.AppName));
                                            builder.setMessage(LocaleController.formatString("ContactNotRegistered", R.string.ContactNotRegistered, ContactsController.formatName(inputPhoneContact.first_name, inputPhoneContact.last_name)));
                                            builder.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null);
                                            builder.setPositiveButton(LocaleController.getString("Invite", R.string.Invite), new OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    try {
                                                        Intent intent = new Intent("android.intent.action.VIEW", Uri.fromParts("sms", inputPhoneContact.phone, null));
                                                        intent.putExtra("sms_body", ContactsController.getInstance(NewContactActivity.this.currentAccount).getInviteText(1));
                                                        NewContactActivity.this.getParentActivity().startActivityForResult(intent, 500);
                                                    } catch (Throwable e) {
                                                        FileLog.e(e);
                                                    }
                                                }
                                            });
                                            NewContactActivity.this.showDialog(builder.create());
                                        }
                                    }
                                });
                            }
                        }, 2), NewContactActivity.this.classGuid);
                    }
                }
            }
        });
        this.avatarDrawable = new AvatarDrawable();
        int i = 0;
        this.avatarDrawable.setInfo(5, TtmlNode.ANONYMOUS_REGION_ID, TtmlNode.ANONYMOUS_REGION_ID, false);
        this.editDoneItem = this.actionBar.createMenu().addItemWithWidth(1, R.drawable.ic_done, AndroidUtilities.dp(56.0f));
        this.editDoneItemProgress = new ContextProgressView(context2, 1);
        this.editDoneItem.addView(this.editDoneItemProgress, LayoutHelper.createFrame(-1, -1.0f));
        this.editDoneItemProgress.setVisibility(4);
        this.fragmentView = new ScrollView(context2);
        LinearLayout linearLayout = new LinearLayout(context2);
        linearLayout.setPadding(AndroidUtilities.dp(24.0f), 0, AndroidUtilities.dp(24.0f), 0);
        linearLayout.setOrientation(1);
        ((ScrollView) this.fragmentView).addView(linearLayout, LayoutHelper.createScroll(-1, -2, 51));
        linearLayout.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        FrameLayout frameLayout = new FrameLayout(context2);
        linearLayout.addView(frameLayout, LayoutHelper.createLinear(-1, -2, 0.0f, 24.0f, 0.0f, 0.0f));
        this.avatarImage = new BackupImageView(context2);
        this.avatarImage.setImageDrawable(this.avatarDrawable);
        frameLayout.addView(this.avatarImage, LayoutHelper.createFrame(60, 60.0f, 51, 0.0f, 9.0f, 0.0f, 0.0f));
        this.firstNameField = new EditTextBoldCursor(context2);
        this.firstNameField.setTextSize(1, 18.0f);
        this.firstNameField.setHintTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteHintText));
        this.firstNameField.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText));
        this.firstNameField.setMaxLines(1);
        this.firstNameField.setLines(1);
        this.firstNameField.setSingleLine(true);
        this.firstNameField.setBackgroundDrawable(Theme.createEditTextDrawable(context2, false));
        this.firstNameField.setGravity(3);
        this.firstNameField.setInputType(49152);
        this.firstNameField.setImeOptions(5);
        this.firstNameField.setHint(LocaleController.getString("FirstName", R.string.FirstName));
        this.firstNameField.setCursorColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText));
        this.firstNameField.setCursorSize(AndroidUtilities.dp(20.0f));
        this.firstNameField.setCursorWidth(1.5f);
        frameLayout.addView(this.firstNameField, LayoutHelper.createFrame(-1, 34.0f, 51, 84.0f, 0.0f, 0.0f, 0.0f));
        this.firstNameField.setOnEditorActionListener(new OnEditorActionListener() {
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i != 5) {
                    return false;
                }
                NewContactActivity.this.lastNameField.requestFocus();
                NewContactActivity.this.lastNameField.setSelection(NewContactActivity.this.lastNameField.length());
                return true;
            }
        });
        this.firstNameField.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void afterTextChanged(Editable editable) {
                NewContactActivity.this.avatarDrawable.setInfo(5, NewContactActivity.this.firstNameField.getText().toString(), NewContactActivity.this.lastNameField.getText().toString(), false);
                NewContactActivity.this.avatarImage.invalidate();
            }
        });
        this.lastNameField = new EditTextBoldCursor(context2);
        this.lastNameField.setTextSize(1, 18.0f);
        this.lastNameField.setHintTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteHintText));
        this.lastNameField.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText));
        this.lastNameField.setBackgroundDrawable(Theme.createEditTextDrawable(context2, false));
        this.lastNameField.setMaxLines(1);
        this.lastNameField.setLines(1);
        this.lastNameField.setSingleLine(true);
        this.lastNameField.setGravity(3);
        this.lastNameField.setInputType(49152);
        this.lastNameField.setImeOptions(5);
        this.lastNameField.setHint(LocaleController.getString("LastName", R.string.LastName));
        this.lastNameField.setCursorColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText));
        this.lastNameField.setCursorSize(AndroidUtilities.dp(20.0f));
        this.lastNameField.setCursorWidth(1.5f);
        frameLayout.addView(this.lastNameField, LayoutHelper.createFrame(-1, 34.0f, 51, 84.0f, 44.0f, 0.0f, 0.0f));
        this.lastNameField.setOnEditorActionListener(new OnEditorActionListener() {
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i != 5) {
                    return false;
                }
                NewContactActivity.this.phoneField.requestFocus();
                NewContactActivity.this.phoneField.setSelection(NewContactActivity.this.phoneField.length());
                return true;
            }
        });
        this.lastNameField.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void afterTextChanged(Editable editable) {
                NewContactActivity.this.avatarDrawable.setInfo(5, NewContactActivity.this.firstNameField.getText().toString(), NewContactActivity.this.lastNameField.getText().toString(), false);
                NewContactActivity.this.avatarImage.invalidate();
            }
        });
        this.countryButton = new TextView(context2);
        this.countryButton.setTextSize(1, 18.0f);
        this.countryButton.setPadding(AndroidUtilities.dp(6.0f), AndroidUtilities.dp(10.0f), AndroidUtilities.dp(6.0f), 0);
        this.countryButton.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText));
        this.countryButton.setMaxLines(1);
        this.countryButton.setSingleLine(true);
        this.countryButton.setEllipsize(TruncateAt.END);
        this.countryButton.setGravity(3);
        this.countryButton.setBackgroundResource(R.drawable.spinner_states);
        linearLayout.addView(this.countryButton, LayoutHelper.createLinear(-1, 36, 0.0f, 24.0f, 0.0f, 14.0f));
        this.countryButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                CountrySelectActivity fragment = new CountrySelectActivity(true);
                fragment.setCountrySelectActivityDelegate(new CountrySelectActivityDelegate() {
                    public void didSelectCountry(String name, String shortName) {
                        NewContactActivity.this.selectCountry(name);
                        AndroidUtilities.runOnUIThread(new Runnable() {
                            public void run() {
                                AndroidUtilities.showKeyboard(NewContactActivity.this.phoneField);
                            }
                        }, 300);
                        NewContactActivity.this.phoneField.requestFocus();
                        NewContactActivity.this.phoneField.setSelection(NewContactActivity.this.phoneField.length());
                    }
                });
                NewContactActivity.this.presentFragment(fragment);
            }
        });
        this.lineView = new View(context2);
        this.lineView.setPadding(AndroidUtilities.dp(8.0f), 0, AndroidUtilities.dp(8.0f), 0);
        this.lineView.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhiteGrayLine));
        linearLayout.addView(this.lineView, LayoutHelper.createLinear(-1, 1, 0.0f, -17.5f, 0.0f, 0.0f));
        LinearLayout linearLayout2 = new LinearLayout(context2);
        linearLayout2.setOrientation(0);
        linearLayout.addView(linearLayout2, LayoutHelper.createLinear(-1, -2, 0.0f, 20.0f, 0.0f, 0.0f));
        this.textView = new TextView(context2);
        this.textView.setText("+");
        this.textView.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText));
        this.textView.setTextSize(1, 18.0f);
        linearLayout2.addView(this.textView, LayoutHelper.createLinear(-2, -2));
        this.codeField = new EditTextBoldCursor(context2);
        this.codeField.setInputType(3);
        this.codeField.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText));
        this.codeField.setBackgroundDrawable(Theme.createEditTextDrawable(context2, false));
        this.codeField.setCursorColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText));
        this.codeField.setCursorSize(AndroidUtilities.dp(20.0f));
        this.codeField.setCursorWidth(1.5f);
        this.codeField.setPadding(AndroidUtilities.dp(10.0f), 0, 0, 0);
        this.codeField.setTextSize(1, 18.0f);
        this.codeField.setMaxLines(1);
        this.codeField.setGravity(19);
        this.codeField.setImeOptions(268435461);
        this.codeField.setFilters(new InputFilter[]{new LengthFilter(5)});
        linearLayout2.addView(this.codeField, LayoutHelper.createLinear(55, 36, -9.0f, 0.0f, 16.0f, 0.0f));
        this.codeField.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            /* JADX WARNING: inconsistent code. */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public void afterTextChanged(android.text.Editable r12) {
                /*
                r11 = this;
                r0 = org.telegram.ui.NewContactActivity.this;
                r0 = r0.ignoreOnTextChange;
                if (r0 == 0) goto L_0x0009;
            L_0x0008:
                return;
            L_0x0009:
                r0 = org.telegram.ui.NewContactActivity.this;
                r1 = 1;
                r0.ignoreOnTextChange = r1;
                r0 = org.telegram.ui.NewContactActivity.this;
                r0 = r0.codeField;
                r0 = r0.getText();
                r0 = r0.toString();
                r0 = org.telegram.PhoneFormat.PhoneFormat.stripExceptNumbers(r0);
                r2 = org.telegram.ui.NewContactActivity.this;
                r2 = r2.codeField;
                r2.setText(r0);
                r2 = r0.length();
                r3 = 0;
                r4 = 0;
                if (r2 != 0) goto L_0x0054;
            L_0x0032:
                r2 = org.telegram.ui.NewContactActivity.this;
                r2 = r2.countryButton;
                r5 = "ChooseCountry";
                r6 = 2131493246; // 0x7f0c017e float:1.8609967E38 double:1.053097587E-314;
                r5 = org.telegram.messenger.LocaleController.getString(r5, r6);
                r2.setText(r5);
                r2 = org.telegram.ui.NewContactActivity.this;
                r2 = r2.phoneField;
                r2.setHintText(r4);
                r2 = org.telegram.ui.NewContactActivity.this;
                r2.countryState = r1;
                goto L_0x01c0;
            L_0x0054:
                r2 = 0;
                r5 = 0;
                r6 = r0.length();
                r7 = 4;
                if (r6 <= r7) goto L_0x00e6;
            L_0x005d:
                r6 = org.telegram.ui.NewContactActivity.this;
                r6.ignoreOnTextChange = r1;
            L_0x0063:
                r6 = r7;
                if (r6 < r1) goto L_0x00ac;
            L_0x0066:
                r7 = r0.substring(r3, r6);
                r8 = org.telegram.ui.NewContactActivity.this;
                r8 = r8.codesMap;
                r8 = r8.get(r7);
                r8 = (java.lang.String) r8;
                if (r8 == 0) goto L_0x00a9;
            L_0x0078:
                r2 = 1;
                r9 = new java.lang.StringBuilder;
                r9.<init>();
                r10 = r0.length();
                r10 = r0.substring(r6, r10);
                r9.append(r10);
                r10 = org.telegram.ui.NewContactActivity.this;
                r10 = r10.phoneField;
                r10 = r10.getText();
                r10 = r10.toString();
                r9.append(r10);
                r5 = r9.toString();
                r9 = org.telegram.ui.NewContactActivity.this;
                r9 = r9.codeField;
                r0 = r7;
                r9.setText(r7);
                goto L_0x00ac;
            L_0x00a9:
                r7 = r6 + -1;
                goto L_0x0063;
            L_0x00ac:
                if (r2 != 0) goto L_0x00e6;
            L_0x00ae:
                r6 = org.telegram.ui.NewContactActivity.this;
                r6.ignoreOnTextChange = r1;
                r6 = new java.lang.StringBuilder;
                r6.<init>();
                r7 = r0.length();
                r7 = r0.substring(r1, r7);
                r6.append(r7);
                r7 = org.telegram.ui.NewContactActivity.this;
                r7 = r7.phoneField;
                r7 = r7.getText();
                r7 = r7.toString();
                r6.append(r7);
                r5 = r6.toString();
                r6 = org.telegram.ui.NewContactActivity.this;
                r6 = r6.codeField;
                r7 = r0.substring(r3, r1);
                r0 = r7;
                r6.setText(r7);
            L_0x00e6:
                r6 = org.telegram.ui.NewContactActivity.this;
                r6 = r6.codesMap;
                r6 = r6.get(r0);
                r6 = (java.lang.String) r6;
                r7 = 2;
                r8 = 2131494644; // 0x7f0c06f4 float:1.8612802E38 double:1.053098278E-314;
                if (r6 == 0) goto L_0x0163;
            L_0x00f8:
                r9 = org.telegram.ui.NewContactActivity.this;
                r9 = r9.countriesArray;
                r9 = r9.indexOf(r6);
                r10 = -1;
                if (r9 == r10) goto L_0x0145;
            L_0x0105:
                r7 = org.telegram.ui.NewContactActivity.this;
                r7.ignoreSelection = r1;
                r1 = org.telegram.ui.NewContactActivity.this;
                r1 = r1.countryButton;
                r7 = org.telegram.ui.NewContactActivity.this;
                r7 = r7.countriesArray;
                r7 = r7.get(r9);
                r7 = (java.lang.CharSequence) r7;
                r1.setText(r7);
                r1 = org.telegram.ui.NewContactActivity.this;
                r1 = r1.phoneFormatMap;
                r1 = r1.get(r0);
                r1 = (java.lang.String) r1;
                r7 = org.telegram.ui.NewContactActivity.this;
                r7 = r7.phoneField;
                if (r1 == 0) goto L_0x013c;
            L_0x0133:
                r4 = 88;
                r8 = 8211; // 0x2013 float:1.1506E-41 double:4.057E-320;
                r4 = r1.replace(r4, r8);
            L_0x013c:
                r7.setHintText(r4);
                r4 = org.telegram.ui.NewContactActivity.this;
                r4.countryState = r3;
                goto L_0x0162;
            L_0x0145:
                r1 = org.telegram.ui.NewContactActivity.this;
                r1 = r1.countryButton;
                r10 = "WrongCountry";
                r8 = org.telegram.messenger.LocaleController.getString(r10, r8);
                r1.setText(r8);
                r1 = org.telegram.ui.NewContactActivity.this;
                r1 = r1.phoneField;
                r1.setHintText(r4);
                r1 = org.telegram.ui.NewContactActivity.this;
                r1.countryState = r7;
            L_0x0162:
                goto L_0x0180;
            L_0x0163:
                r1 = org.telegram.ui.NewContactActivity.this;
                r1 = r1.countryButton;
                r9 = "WrongCountry";
                r8 = org.telegram.messenger.LocaleController.getString(r9, r8);
                r1.setText(r8);
                r1 = org.telegram.ui.NewContactActivity.this;
                r1 = r1.phoneField;
                r1.setHintText(r4);
                r1 = org.telegram.ui.NewContactActivity.this;
                r1.countryState = r7;
            L_0x0180:
                if (r2 != 0) goto L_0x0199;
            L_0x0182:
                r1 = org.telegram.ui.NewContactActivity.this;
                r1 = r1.codeField;
                r4 = org.telegram.ui.NewContactActivity.this;
                r4 = r4.codeField;
                r4 = r4.getText();
                r4 = r4.length();
                r1.setSelection(r4);
            L_0x0199:
                if (r5 == 0) goto L_0x01c0;
            L_0x019b:
                r1 = org.telegram.ui.NewContactActivity.this;
                r1 = r1.phoneField;
                r1.requestFocus();
                r1 = org.telegram.ui.NewContactActivity.this;
                r1 = r1.phoneField;
                r1.setText(r5);
                r1 = org.telegram.ui.NewContactActivity.this;
                r1 = r1.phoneField;
                r4 = org.telegram.ui.NewContactActivity.this;
                r4 = r4.phoneField;
                r4 = r4.length();
                r1.setSelection(r4);
            L_0x01c0:
                r1 = org.telegram.ui.NewContactActivity.this;
                r1.ignoreOnTextChange = r3;
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: org.telegram.ui.NewContactActivity.8.afterTextChanged(android.text.Editable):void");
            }
        });
        this.codeField.setOnEditorActionListener(new OnEditorActionListener() {
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i != 5) {
                    return false;
                }
                NewContactActivity.this.phoneField.requestFocus();
                NewContactActivity.this.phoneField.setSelection(NewContactActivity.this.phoneField.length());
                return true;
            }
        });
        this.phoneField = new HintEditText(context2);
        this.phoneField.setInputType(3);
        this.phoneField.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText));
        this.phoneField.setHintTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteHintText));
        this.phoneField.setBackgroundDrawable(Theme.createEditTextDrawable(context2, false));
        this.phoneField.setPadding(0, 0, 0, 0);
        this.phoneField.setCursorColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText));
        this.phoneField.setCursorSize(AndroidUtilities.dp(20.0f));
        this.phoneField.setCursorWidth(1.5f);
        this.phoneField.setTextSize(1, 18.0f);
        this.phoneField.setMaxLines(1);
        this.phoneField.setGravity(19);
        this.phoneField.setImeOptions(268435462);
        linearLayout2.addView(this.phoneField, LayoutHelper.createFrame(-1, 36.0f));
        this.phoneField.addTextChangedListener(new TextWatcher() {
            private int actionPosition;
            private int characterAction = -1;

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (count == 0 && after == 1) {
                    this.characterAction = 1;
                } else if (count != 1 || after != 0) {
                    this.characterAction = -1;
                } else if (s.charAt(start) != ' ' || start <= 0) {
                    this.characterAction = 2;
                } else {
                    this.characterAction = 3;
                    this.actionPosition = start - 1;
                }
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void afterTextChanged(Editable s) {
                if (!NewContactActivity.this.ignoreOnPhoneChange) {
                    StringBuilder stringBuilder;
                    int start = NewContactActivity.this.phoneField.getSelectionStart();
                    String phoneChars = "0123456789";
                    String str = NewContactActivity.this.phoneField.getText().toString();
                    if (this.characterAction == 3) {
                        stringBuilder = new StringBuilder();
                        stringBuilder.append(str.substring(0, this.actionPosition));
                        stringBuilder.append(str.substring(this.actionPosition + 1, str.length()));
                        str = stringBuilder.toString();
                        start--;
                    }
                    stringBuilder = new StringBuilder(str.length());
                    for (int a = 0; a < str.length(); a++) {
                        String ch = str.substring(a, a + 1);
                        if (phoneChars.contains(ch)) {
                            stringBuilder.append(ch);
                        }
                    }
                    NewContactActivity.this.ignoreOnPhoneChange = true;
                    String hint = NewContactActivity.this.phoneField.getHintText();
                    if (hint != null) {
                        int start2 = start;
                        start = 0;
                        while (start < stringBuilder.length()) {
                            if (start < hint.length()) {
                                if (hint.charAt(start) == ' ') {
                                    stringBuilder.insert(start, ' ');
                                    start++;
                                    if (!(start2 != start || this.characterAction == 2 || this.characterAction == 3)) {
                                        start2++;
                                    }
                                }
                                start++;
                            } else {
                                stringBuilder.insert(start, ' ');
                                if (!(start2 != start + 1 || this.characterAction == 2 || this.characterAction == 3)) {
                                    start = start2 + 1;
                                }
                                start = start2;
                            }
                        }
                        start = start2;
                    }
                    NewContactActivity.this.phoneField.setText(stringBuilder);
                    if (start >= 0) {
                        NewContactActivity.this.phoneField.setSelection(start <= NewContactActivity.this.phoneField.length() ? start : NewContactActivity.this.phoneField.length());
                    }
                    NewContactActivity.this.phoneField.onTextChange();
                    NewContactActivity.this.ignoreOnPhoneChange = false;
                }
            }
        });
        this.phoneField.setOnEditorActionListener(new OnEditorActionListener() {
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i != 6) {
                    return false;
                }
                NewContactActivity.this.editDoneItem.performClick();
                return true;
            }
        });
        HashMap<String, String> languageMap = new HashMap();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(context.getResources().getAssets().open("countries.txt")));
            while (true) {
                String readLine = reader.readLine();
                String line = readLine;
                if (readLine == null) {
                    break;
                }
                String[] args = line.split(";");
                r1.countriesArray.add(i, args[2]);
                r1.countriesMap.put(args[2], args[i]);
                r1.codesMap.put(args[i], args[2]);
                if (args.length > 3) {
                    r1.phoneFormatMap.put(args[i], args[3]);
                }
                languageMap.put(args[1], args[2]);
                i = 0;
            }
            reader.close();
        } catch (Throwable e) {
            FileLog.e(e);
        }
        Collections.sort(r1.countriesArray, new Comparator<String>() {
            public int compare(String lhs, String rhs) {
                return lhs.compareTo(rhs);
            }
        });
        String country = null;
        try {
            TelephonyManager telephonyManager = (TelephonyManager) ApplicationLoader.applicationContext.getSystemService("phone");
            if (telephonyManager != null) {
                country = telephonyManager.getSimCountryIso().toUpperCase();
            }
        } catch (Throwable e2) {
            FileLog.e(e2);
        }
        if (country != null) {
            String countryName = (String) languageMap.get(country);
            if (!(countryName == null || r1.countriesArray.indexOf(countryName) == -1)) {
                r1.codeField.setText((CharSequence) r1.countriesMap.get(countryName));
                r1.countryState = 0;
            }
        }
        if (r1.codeField.length() == 0) {
            r1.countryButton.setText(LocaleController.getString("ChooseCountry", R.string.ChooseCountry));
            r1.phoneField.setHintText(null);
            r1.countryState = 1;
        }
        return r1.fragmentView;
    }

    public void onResume() {
        super.onResume();
        if (!MessagesController.getGlobalMainSettings().getBoolean("view_animations", true)) {
            this.firstNameField.requestFocus();
            AndroidUtilities.showKeyboard(this.firstNameField);
        }
    }

    public void onTransitionAnimationEnd(boolean isOpen, boolean backward) {
        if (isOpen) {
            this.firstNameField.requestFocus();
            AndroidUtilities.showKeyboard(this.firstNameField);
        }
    }

    public void selectCountry(String name) {
        if (this.countriesArray.indexOf(name) != -1) {
            this.ignoreOnTextChange = true;
            String code = (String) this.countriesMap.get(name);
            this.codeField.setText(code);
            this.countryButton.setText(name);
            String hint = (String) this.phoneFormatMap.get(code);
            this.phoneField.setHintText(hint != null ? hint.replace('X', '–') : null);
            this.countryState = 0;
            this.ignoreOnTextChange = false;
        }
    }

    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (this.ignoreSelection) {
            this.ignoreSelection = false;
            return;
        }
        this.ignoreOnTextChange = true;
        this.codeField.setText((CharSequence) this.countriesMap.get((String) this.countriesArray.get(i)));
        this.ignoreOnTextChange = false;
    }

    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    private void showEditDoneProgress(boolean show, boolean animated) {
        final boolean z = show;
        if (this.editDoneItemAnimation != null) {
            r0.editDoneItemAnimation.cancel();
        }
        if (animated) {
            r0.editDoneItemAnimation = new AnimatorSet();
            Animator[] animatorArr;
            if (z) {
                r0.editDoneItemProgress.setVisibility(0);
                r0.editDoneItem.setEnabled(false);
                AnimatorSet animatorSet = r0.editDoneItemAnimation;
                animatorArr = new Animator[6];
                animatorArr[0] = ObjectAnimator.ofFloat(r0.editDoneItem.getImageView(), "scaleX", new float[]{0.1f});
                animatorArr[1] = ObjectAnimator.ofFloat(r0.editDoneItem.getImageView(), "scaleY", new float[]{0.1f});
                animatorArr[2] = ObjectAnimator.ofFloat(r0.editDoneItem.getImageView(), "alpha", new float[]{0.0f});
                animatorArr[3] = ObjectAnimator.ofFloat(r0.editDoneItemProgress, "scaleX", new float[]{1.0f});
                animatorArr[4] = ObjectAnimator.ofFloat(r0.editDoneItemProgress, "scaleY", new float[]{1.0f});
                animatorArr[5] = ObjectAnimator.ofFloat(r0.editDoneItemProgress, "alpha", new float[]{1.0f});
                animatorSet.playTogether(animatorArr);
            } else {
                r0.editDoneItem.getImageView().setVisibility(0);
                r0.editDoneItem.setEnabled(true);
                AnimatorSet animatorSet2 = r0.editDoneItemAnimation;
                animatorArr = new Animator[6];
                animatorArr[0] = ObjectAnimator.ofFloat(r0.editDoneItemProgress, "scaleX", new float[]{0.1f});
                animatorArr[1] = ObjectAnimator.ofFloat(r0.editDoneItemProgress, "scaleY", new float[]{0.1f});
                animatorArr[2] = ObjectAnimator.ofFloat(r0.editDoneItemProgress, "alpha", new float[]{0.0f});
                animatorArr[3] = ObjectAnimator.ofFloat(r0.editDoneItem.getImageView(), "scaleX", new float[]{1.0f});
                animatorArr[4] = ObjectAnimator.ofFloat(r0.editDoneItem.getImageView(), "scaleY", new float[]{1.0f});
                animatorArr[5] = ObjectAnimator.ofFloat(r0.editDoneItem.getImageView(), "alpha", new float[]{1.0f});
                animatorSet2.playTogether(animatorArr);
            }
            r0.editDoneItemAnimation.addListener(new AnimatorListenerAdapter() {
                public void onAnimationEnd(Animator animation) {
                    if (NewContactActivity.this.editDoneItemAnimation != null && NewContactActivity.this.editDoneItemAnimation.equals(animation)) {
                        if (z) {
                            NewContactActivity.this.editDoneItem.getImageView().setVisibility(4);
                        } else {
                            NewContactActivity.this.editDoneItemProgress.setVisibility(4);
                        }
                    }
                }

                public void onAnimationCancel(Animator animation) {
                    if (NewContactActivity.this.editDoneItemAnimation != null && NewContactActivity.this.editDoneItemAnimation.equals(animation)) {
                        NewContactActivity.this.editDoneItemAnimation = null;
                    }
                }
            });
            r0.editDoneItemAnimation.setDuration(150);
            r0.editDoneItemAnimation.start();
        } else if (z) {
            r0.editDoneItem.getImageView().setScaleX(0.1f);
            r0.editDoneItem.getImageView().setScaleY(0.1f);
            r0.editDoneItem.getImageView().setAlpha(0.0f);
            r0.editDoneItemProgress.setScaleX(1.0f);
            r0.editDoneItemProgress.setScaleY(1.0f);
            r0.editDoneItemProgress.setAlpha(1.0f);
            r0.editDoneItem.getImageView().setVisibility(4);
            r0.editDoneItemProgress.setVisibility(0);
            r0.editDoneItem.setEnabled(false);
        } else {
            r0.editDoneItemProgress.setScaleX(0.1f);
            r0.editDoneItemProgress.setScaleY(0.1f);
            r0.editDoneItemProgress.setAlpha(0.0f);
            r0.editDoneItem.getImageView().setScaleX(1.0f);
            r0.editDoneItem.getImageView().setScaleY(1.0f);
            r0.editDoneItem.getImageView().setAlpha(1.0f);
            r0.editDoneItem.getImageView().setVisibility(0);
            r0.editDoneItemProgress.setVisibility(4);
            r0.editDoneItem.setEnabled(true);
        }
    }

    public ThemeDescription[] getThemeDescriptions() {
        ThemeDescriptionDelegate сellDelegate = new ThemeDescriptionDelegate() {
            public void didSetColor() {
                if (NewContactActivity.this.avatarImage != null) {
                    NewContactActivity.this.avatarDrawable.setInfo(5, NewContactActivity.this.firstNameField.getText().toString(), NewContactActivity.this.lastNameField.getText().toString(), false);
                    NewContactActivity.this.avatarImage.invalidate();
                }
            }
        };
        ThemeDescription[] themeDescriptionArr = new ThemeDescription[34];
        themeDescriptionArr[0] = new ThemeDescription(this.fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_windowBackgroundWhite);
        themeDescriptionArr[1] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_actionBarDefault);
        themeDescriptionArr[2] = new ThemeDescription(this.fragmentView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, Theme.key_actionBarDefault);
        themeDescriptionArr[3] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, Theme.key_actionBarDefaultIcon);
        themeDescriptionArr[4] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, Theme.key_actionBarDefaultTitle);
        themeDescriptionArr[5] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, Theme.key_actionBarDefaultSelector);
        themeDescriptionArr[6] = new ThemeDescription(this.firstNameField, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, Theme.key_windowBackgroundWhiteBlackText);
        themeDescriptionArr[7] = new ThemeDescription(this.firstNameField, ThemeDescription.FLAG_HINTTEXTCOLOR, null, null, null, null, Theme.key_windowBackgroundWhiteHintText);
        themeDescriptionArr[8] = new ThemeDescription(this.firstNameField, ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, Theme.key_windowBackgroundWhiteInputField);
        themeDescriptionArr[9] = new ThemeDescription(this.firstNameField, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, null, null, null, null, Theme.key_windowBackgroundWhiteInputFieldActivated);
        themeDescriptionArr[10] = new ThemeDescription(this.lastNameField, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, Theme.key_windowBackgroundWhiteBlackText);
        themeDescriptionArr[11] = new ThemeDescription(this.lastNameField, ThemeDescription.FLAG_HINTTEXTCOLOR, null, null, null, null, Theme.key_windowBackgroundWhiteHintText);
        themeDescriptionArr[12] = new ThemeDescription(this.lastNameField, ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, Theme.key_windowBackgroundWhiteInputField);
        themeDescriptionArr[13] = new ThemeDescription(this.lastNameField, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, null, null, null, null, Theme.key_windowBackgroundWhiteInputFieldActivated);
        themeDescriptionArr[14] = new ThemeDescription(this.codeField, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, Theme.key_windowBackgroundWhiteBlackText);
        themeDescriptionArr[15] = new ThemeDescription(this.codeField, ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, Theme.key_windowBackgroundWhiteInputField);
        themeDescriptionArr[16] = new ThemeDescription(this.codeField, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, null, null, null, null, Theme.key_windowBackgroundWhiteInputFieldActivated);
        themeDescriptionArr[17] = new ThemeDescription(this.phoneField, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, Theme.key_windowBackgroundWhiteBlackText);
        themeDescriptionArr[18] = new ThemeDescription(this.phoneField, ThemeDescription.FLAG_HINTTEXTCOLOR, null, null, null, null, Theme.key_windowBackgroundWhiteHintText);
        themeDescriptionArr[19] = new ThemeDescription(this.phoneField, ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, Theme.key_windowBackgroundWhiteInputField);
        themeDescriptionArr[20] = new ThemeDescription(this.phoneField, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, null, null, null, null, Theme.key_windowBackgroundWhiteInputFieldActivated);
        themeDescriptionArr[21] = new ThemeDescription(this.textView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, Theme.key_windowBackgroundWhiteBlackText);
        themeDescriptionArr[22] = new ThemeDescription(this.lineView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_windowBackgroundWhiteGrayLine);
        themeDescriptionArr[23] = new ThemeDescription(this.countryButton, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, Theme.key_windowBackgroundWhiteBlackText);
        themeDescriptionArr[24] = new ThemeDescription(this.editDoneItemProgress, 0, null, null, null, null, Theme.key_contextProgressInner2);
        themeDescriptionArr[25] = new ThemeDescription(this.editDoneItemProgress, 0, null, null, null, null, Theme.key_contextProgressOuter2);
        themeDescriptionArr[26] = new ThemeDescription(null, 0, null, null, new Drawable[]{Theme.avatar_photoDrawable, Theme.avatar_broadcastDrawable, Theme.avatar_savedDrawable}, сellDelegate, Theme.key_avatar_text);
        themeDescriptionArr[27] = new ThemeDescription(null, 0, null, null, null, сellDelegate, Theme.key_avatar_backgroundRed);
        ThemeDescriptionDelegate themeDescriptionDelegate = сellDelegate;
        themeDescriptionArr[28] = new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_avatar_backgroundOrange);
        themeDescriptionArr[29] = new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_avatar_backgroundViolet);
        themeDescriptionArr[30] = new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_avatar_backgroundGreen);
        themeDescriptionArr[31] = new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_avatar_backgroundCyan);
        themeDescriptionArr[32] = new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_avatar_backgroundBlue);
        themeDescriptionArr[33] = new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_avatar_backgroundPink);
        return themeDescriptionArr;
    }
}
