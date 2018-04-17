package org.telegram.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Vibrator;
import android.telephony.TelephonyManager;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewParent;
import android.view.animation.DecelerateInterpolator;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.BooleanResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wallet.Cart;
import com.google.android.gms.wallet.FullWallet;
import com.google.android.gms.wallet.FullWalletRequest;
import com.google.android.gms.wallet.LineItem;
import com.google.android.gms.wallet.MaskedWallet;
import com.google.android.gms.wallet.MaskedWalletRequest;
import com.google.android.gms.wallet.PaymentMethodTokenizationParameters;
import com.google.android.gms.wallet.Wallet;
import com.google.android.gms.wallet.Wallet.WalletOptions;
import com.google.android.gms.wallet.fragment.WalletFragment;
import com.google.android.gms.wallet.fragment.WalletFragmentInitParams;
import com.google.android.gms.wallet.fragment.WalletFragmentOptions;
import com.google.android.gms.wallet.fragment.WalletFragmentStyle;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.exception.APIConnectionException;
import com.stripe.android.exception.APIException;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import com.stripe.android.net.TokenParser;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import org.json.JSONException;
import org.json.JSONObject;
import org.telegram.PhoneFormat.PhoneFormat;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.NotificationCenter.NotificationCenterDelegate;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.beta.R;
import org.telegram.messenger.exoplayer2.C;
import org.telegram.messenger.exoplayer2.DefaultRenderersFactory;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_account_getPassword;
import org.telegram.tgnet.TLRPC.TL_account_getTmpPassword;
import org.telegram.tgnet.TLRPC.TL_account_noPassword;
import org.telegram.tgnet.TLRPC.TL_account_password;
import org.telegram.tgnet.TLRPC.TL_account_passwordInputSettings;
import org.telegram.tgnet.TLRPC.TL_account_tmpPassword;
import org.telegram.tgnet.TLRPC.TL_account_updatePasswordSettings;
import org.telegram.tgnet.TLRPC.TL_boolTrue;
import org.telegram.tgnet.TLRPC.TL_dataJSON;
import org.telegram.tgnet.TLRPC.TL_error;
import org.telegram.tgnet.TLRPC.TL_inputPaymentCredentials;
import org.telegram.tgnet.TLRPC.TL_inputPaymentCredentialsAndroidPay;
import org.telegram.tgnet.TLRPC.TL_inputPaymentCredentialsSaved;
import org.telegram.tgnet.TLRPC.TL_labeledPrice;
import org.telegram.tgnet.TLRPC.TL_paymentRequestedInfo;
import org.telegram.tgnet.TLRPC.TL_payments_clearSavedInfo;
import org.telegram.tgnet.TLRPC.TL_payments_paymentForm;
import org.telegram.tgnet.TLRPC.TL_payments_paymentReceipt;
import org.telegram.tgnet.TLRPC.TL_payments_paymentResult;
import org.telegram.tgnet.TLRPC.TL_payments_paymentVerficationNeeded;
import org.telegram.tgnet.TLRPC.TL_payments_sendPaymentForm;
import org.telegram.tgnet.TLRPC.TL_payments_validateRequestedInfo;
import org.telegram.tgnet.TLRPC.TL_payments_validatedRequestedInfo;
import org.telegram.tgnet.TLRPC.TL_postAddress;
import org.telegram.tgnet.TLRPC.TL_shippingOption;
import org.telegram.tgnet.TLRPC.User;
import org.telegram.tgnet.TLRPC.account_Password;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.ActionBar.AlertDialog.Builder;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.PaymentInfoCell;
import org.telegram.ui.Cells.RadioCell;
import org.telegram.ui.Cells.ShadowSectionCell;
import org.telegram.ui.Cells.TextCheckCell;
import org.telegram.ui.Cells.TextDetailSettingsCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Cells.TextPriceCell;
import org.telegram.ui.Cells.TextSettingsCell;
import org.telegram.ui.Components.AlertsCreator;
import org.telegram.ui.Components.ContextProgressView;
import org.telegram.ui.Components.EditTextBoldCursor;

public class PaymentFormActivity extends BaseFragment implements NotificationCenterDelegate {
    private static final int FIELDS_COUNT_ADDRESS = 10;
    private static final int FIELDS_COUNT_CARD = 6;
    private static final int FIELDS_COUNT_PASSWORD = 3;
    private static final int FIELDS_COUNT_SAVEDCARD = 2;
    private static final int FIELD_CARD = 0;
    private static final int FIELD_CARDNAME = 2;
    private static final int FIELD_CARD_COUNTRY = 4;
    private static final int FIELD_CARD_POSTCODE = 5;
    private static final int FIELD_CITY = 2;
    private static final int FIELD_COUNTRY = 4;
    private static final int FIELD_CVV = 3;
    private static final int FIELD_EMAIL = 7;
    private static final int FIELD_ENTERPASSWORD = 0;
    private static final int FIELD_ENTERPASSWORDEMAIL = 2;
    private static final int FIELD_EXPIRE_DATE = 1;
    private static final int FIELD_NAME = 6;
    private static final int FIELD_PHONE = 9;
    private static final int FIELD_PHONECODE = 8;
    private static final int FIELD_POSTCODE = 5;
    private static final int FIELD_REENTERPASSWORD = 1;
    private static final int FIELD_SAVEDCARD = 0;
    private static final int FIELD_SAVEDPASSWORD = 1;
    private static final int FIELD_STATE = 3;
    private static final int FIELD_STREET1 = 0;
    private static final int FIELD_STREET2 = 1;
    private static final int LOAD_FULL_WALLET_REQUEST_CODE = 1001;
    private static final int LOAD_MASKED_WALLET_REQUEST_CODE = 1000;
    private static final int done_button = 1;
    private static final int fragment_container_id = 4000;
    private int androidPayBackgroundColor;
    private boolean androidPayBlackTheme;
    private FrameLayout androidPayContainer;
    private TL_inputPaymentCredentialsAndroidPay androidPayCredentials;
    private String androidPayPublicKey;
    private User botUser;
    private TextInfoPrivacyCell[] bottomCell;
    private FrameLayout bottomLayout;
    private boolean canceled;
    private String cardName;
    private TextCheckCell checkCell1;
    private HashMap<String, String> codesMap;
    private ArrayList<String> countriesArray;
    private HashMap<String, String> countriesMap;
    private String countryName;
    private String currentBotName;
    private String currentItemName;
    private account_Password currentPassword;
    private int currentStep;
    private PaymentFormActivityDelegate delegate;
    private TextDetailSettingsCell[] detailSettingsCell;
    private ArrayList<View> dividers;
    private ActionBarMenuItem doneItem;
    private AnimatorSet doneItemAnimation;
    private boolean donePressed;
    private GoogleApiClient googleApiClient;
    private HeaderCell[] headerCell;
    private boolean ignoreOnCardChange;
    private boolean ignoreOnPhoneChange;
    private boolean ignoreOnTextChange;
    private EditTextBoldCursor[] inputFields;
    private boolean isWebView;
    private LinearLayout linearLayout2;
    private boolean loadingPasswordInfo;
    private MessageObject messageObject;
    private boolean need_card_country;
    private boolean need_card_name;
    private boolean need_card_postcode;
    private PaymentFormActivity passwordFragment;
    private boolean passwordOk;
    private TextView payTextView;
    private TL_payments_paymentForm paymentForm;
    private PaymentInfoCell paymentInfoCell;
    private String paymentJson;
    private HashMap<String, String> phoneFormatMap;
    private ContextProgressView progressView;
    private ContextProgressView progressViewButton;
    private RadioCell[] radioCells;
    private TL_payments_validatedRequestedInfo requestedInfo;
    private boolean saveCardInfo;
    private boolean saveShippingInfo;
    private ScrollView scrollView;
    private ShadowSectionCell[] sectionCell;
    private TextSettingsCell settingsCell1;
    private TL_shippingOption shippingOption;
    private Runnable shortPollRunnable;
    private String stripeApiKey;
    private TextView textView;
    private String totalPriceDecimal;
    private TL_payments_validateRequestedInfo validateRequest;
    private boolean waitingForEmail;
    private WebView webView;
    private boolean webviewLoading;

    class AnonymousClass21 implements OnClickListener {
        final /* synthetic */ String val$providerName;
        final /* synthetic */ String val$totalPrice;

        AnonymousClass21(String str, String str2) {
            this.val$providerName = str;
            this.val$totalPrice = str2;
        }

        public void onClick(View v) {
            if (PaymentFormActivity.this.botUser == null || PaymentFormActivity.this.botUser.verified) {
                PaymentFormActivity.this.showPayAlert(this.val$totalPrice);
                return;
            }
            String botKey = new StringBuilder();
            botKey.append("payment_warning_");
            botKey.append(PaymentFormActivity.this.botUser.id);
            botKey = botKey.toString();
            SharedPreferences preferences = MessagesController.getNotificationsSettings(PaymentFormActivity.this.currentAccount);
            if (preferences.getBoolean(botKey, false)) {
                PaymentFormActivity.this.showPayAlert(this.val$totalPrice);
            } else {
                preferences.edit().putBoolean(botKey, true).commit();
                Builder builder = new Builder(PaymentFormActivity.this.getParentActivity());
                builder.setTitle(LocaleController.getString("PaymentWarning", R.string.PaymentWarning));
                builder.setMessage(LocaleController.formatString("PaymentWarningText", R.string.PaymentWarningText, PaymentFormActivity.this.currentBotName, this.val$providerName));
                builder.setPositiveButton(LocaleController.getString("OK", R.string.OK), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        PaymentFormActivity.this.showPayAlert(AnonymousClass21.this.val$totalPrice);
                    }
                });
                PaymentFormActivity.this.showDialog(builder.create());
            }
        }
    }

    class AnonymousClass22 extends WebView {
        AnonymousClass22(Context x0) {
            super(x0);
        }

        public boolean onTouchEvent(MotionEvent event) {
            getParent().requestDisallowInterceptTouchEvent(true);
            return super.onTouchEvent(event);
        }
    }

    class AnonymousClass8 extends WebView {
        AnonymousClass8(Context x0) {
            super(x0);
        }

        public boolean onTouchEvent(MotionEvent event) {
            getParent().requestDisallowInterceptTouchEvent(true);
            return super.onTouchEvent(event);
        }
    }

    private static class LinkMovementMethodMy extends LinkMovementMethod {
        private LinkMovementMethodMy() {
        }

        public boolean onTouchEvent(TextView widget, Spannable buffer, MotionEvent event) {
            try {
                boolean result = super.onTouchEvent(widget, buffer, event);
                if (event.getAction() == 1 || event.getAction() == 3) {
                    Selection.removeSelection(buffer);
                }
                return result;
            } catch (Throwable e) {
                FileLog.e(e);
                return false;
            }
        }
    }

    public class LinkSpan extends ClickableSpan {
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setUnderlineText(false);
        }

        public void onClick(View widget) {
            PaymentFormActivity.this.presentFragment(new TwoStepVerificationActivity(0));
        }
    }

    private interface PaymentFormActivityDelegate {
        void currentPasswordUpdated(account_Password org_telegram_tgnet_TLRPC_account_Password);

        boolean didSelectNewCard(String str, String str2, boolean z, TL_inputPaymentCredentialsAndroidPay tL_inputPaymentCredentialsAndroidPay);

        void onFragmentDestroyed();
    }

    private class TelegramWebviewProxy {
        private TelegramWebviewProxy() {
        }

        @JavascriptInterface
        public void postEvent(final String eventName, final String eventData) {
            AndroidUtilities.runOnUIThread(new Runnable() {
                public void run() {
                    if (PaymentFormActivity.this.getParentActivity() != null && eventName.equals("payment_form_submit")) {
                        try {
                            JSONObject jsonObject = new JSONObject(eventData);
                            PaymentFormActivity.this.paymentJson = jsonObject.getJSONObject("credentials").toString();
                            PaymentFormActivity.this.cardName = jsonObject.getString("title");
                        } catch (Throwable e) {
                            PaymentFormActivity.this.paymentJson = eventData;
                            FileLog.e(e);
                        }
                        PaymentFormActivity.this.goToNextStep();
                    }
                }
            });
        }
    }

    private void goToNextStep() {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.ui.PaymentFormActivity.goToNextStep():void
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:116)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:249)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
Caused by: java.lang.NullPointerException
*/
        /*
        r0 = this;
        r0 = r17;
        r1 = r0.currentStep;
        r2 = 0;
        r3 = 0;
        r4 = 2;
        if (r1 != 0) goto L_0x0079;
    L_0x0009:
        r1 = r0.paymentForm;
        r1 = r1.invoice;
        r1 = r1.flexible;
        if (r1 == 0) goto L_0x0014;
    L_0x0011:
        r4 = 1;
    L_0x0012:
        r8 = r4;
        goto L_0x005c;
    L_0x0014:
        r1 = r0.paymentForm;
        r1 = r1.saved_credentials;
        if (r1 == 0) goto L_0x005b;
    L_0x001a:
        r1 = r0.currentAccount;
        r1 = org.telegram.messenger.UserConfig.getInstance(r1);
        r1 = r1.tmpPassword;
        if (r1 == 0) goto L_0x004d;
    L_0x0024:
        r1 = r0.currentAccount;
        r1 = org.telegram.messenger.UserConfig.getInstance(r1);
        r1 = r1.tmpPassword;
        r1 = r1.valid_until;
        r4 = r0.currentAccount;
        r4 = org.telegram.tgnet.ConnectionsManager.getInstance(r4);
        r4 = r4.getCurrentTime();
        r4 = r4 + 60;
        if (r1 >= r4) goto L_0x004d;
    L_0x003c:
        r1 = r0.currentAccount;
        r1 = org.telegram.messenger.UserConfig.getInstance(r1);
        r1.tmpPassword = r2;
        r1 = r0.currentAccount;
        r1 = org.telegram.messenger.UserConfig.getInstance(r1);
        r1.saveConfig(r3);
    L_0x004d:
        r1 = r0.currentAccount;
        r1 = org.telegram.messenger.UserConfig.getInstance(r1);
        r1 = r1.tmpPassword;
        if (r1 == 0) goto L_0x0059;
    L_0x0057:
        r4 = 4;
        goto L_0x0012;
    L_0x0059:
        r4 = 3;
        goto L_0x0012;
    L_0x005b:
        goto L_0x0012;
    L_0x005c:
        r1 = new org.telegram.ui.PaymentFormActivity;
        r6 = r0.paymentForm;
        r7 = r0.messageObject;
        r9 = r0.requestedInfo;
        r10 = 0;
        r11 = 0;
        r12 = r0.cardName;
        r13 = r0.validateRequest;
        r14 = r0.saveCardInfo;
        r15 = r0.androidPayCredentials;
        r5 = r1;
        r5.<init>(r6, r7, r8, r9, r10, r11, r12, r13, r14, r15);
        r2 = r0.isWebView;
        r0.presentFragment(r1, r2);
        goto L_0x01db;
    L_0x0079:
        r1 = r0.currentStep;
        r5 = 1;
        if (r1 != r5) goto L_0x00e5;
    L_0x007e:
        r1 = r0.paymentForm;
        r1 = r1.saved_credentials;
        if (r1 == 0) goto L_0x00c5;
    L_0x0084:
        r1 = r0.currentAccount;
        r1 = org.telegram.messenger.UserConfig.getInstance(r1);
        r1 = r1.tmpPassword;
        if (r1 == 0) goto L_0x00b7;
        r1 = r0.currentAccount;
        r1 = org.telegram.messenger.UserConfig.getInstance(r1);
        r1 = r1.tmpPassword;
        r1 = r1.valid_until;
        r4 = r0.currentAccount;
        r4 = org.telegram.tgnet.ConnectionsManager.getInstance(r4);
        r4 = r4.getCurrentTime();
        r4 = r4 + 60;
        if (r1 >= r4) goto L_0x00b7;
        r1 = r0.currentAccount;
        r1 = org.telegram.messenger.UserConfig.getInstance(r1);
        r1.tmpPassword = r2;
        r1 = r0.currentAccount;
        r1 = org.telegram.messenger.UserConfig.getInstance(r1);
        r1.saveConfig(r3);
        r1 = r0.currentAccount;
        r1 = org.telegram.messenger.UserConfig.getInstance(r1);
        r1 = r1.tmpPassword;
        if (r1 == 0) goto L_0x00c3;
        r4 = 4;
        goto L_0x00c6;
        r4 = 3;
        goto L_0x00c2;
        r8 = r4;
        r1 = new org.telegram.ui.PaymentFormActivity;
        r6 = r0.paymentForm;
        r7 = r0.messageObject;
        r9 = r0.requestedInfo;
        r10 = r0.shippingOption;
        r11 = 0;
        r12 = r0.cardName;
        r13 = r0.validateRequest;
        r14 = r0.saveCardInfo;
        r15 = r0.androidPayCredentials;
        r5 = r1;
        r5.<init>(r6, r7, r8, r9, r10, r11, r12, r13, r14, r15);
        r2 = r0.isWebView;
        r0.presentFragment(r1, r2);
        goto L_0x01db;
    L_0x00e5:
        r1 = r0.currentStep;
        if (r1 != r4) goto L_0x0160;
        r1 = r0.paymentForm;
        r1 = r1.password_missing;
        if (r1 == 0) goto L_0x0128;
        r1 = r0.saveCardInfo;
        if (r1 == 0) goto L_0x0128;
        r1 = new org.telegram.ui.PaymentFormActivity;
        r3 = r0.paymentForm;
        r4 = r0.messageObject;
        r5 = 6;
        r6 = r0.requestedInfo;
        r7 = r0.shippingOption;
        r8 = r0.paymentJson;
        r9 = r0.cardName;
        r10 = r0.validateRequest;
        r11 = r0.saveCardInfo;
        r12 = r0.androidPayCredentials;
        r2 = r1;
        r2.<init>(r3, r4, r5, r6, r7, r8, r9, r10, r11, r12);
        r0.passwordFragment = r1;
        r1 = r0.passwordFragment;
        r2 = r0.currentPassword;
        r1.setCurrentPassword(r2);
        r1 = r0.passwordFragment;
        r2 = new org.telegram.ui.PaymentFormActivity$31;
        r2.<init>();
        r1.setDelegate(r2);
        r1 = r0.passwordFragment;
        r2 = r0.isWebView;
        r0.presentFragment(r1, r2);
        goto L_0x01db;
        r1 = r0.delegate;
        if (r1 == 0) goto L_0x013e;
        r1 = r0.delegate;
        r2 = r0.paymentJson;
        r3 = r0.cardName;
        r4 = r0.saveCardInfo;
        r5 = r0.androidPayCredentials;
        r1.didSelectNewCard(r2, r3, r4, r5);
        r17.finishFragment();
        goto L_0x01db;
        r1 = new org.telegram.ui.PaymentFormActivity;
        r7 = r0.paymentForm;
        r8 = r0.messageObject;
        r9 = 4;
        r10 = r0.requestedInfo;
        r11 = r0.shippingOption;
        r12 = r0.paymentJson;
        r13 = r0.cardName;
        r14 = r0.validateRequest;
        r15 = r0.saveCardInfo;
        r2 = r0.androidPayCredentials;
        r6 = r1;
        r16 = r2;
        r6.<init>(r7, r8, r9, r10, r11, r12, r13, r14, r15, r16);
        r2 = r0.isWebView;
        r0.presentFragment(r1, r2);
        goto L_0x01db;
        r1 = r0.currentStep;
        r2 = 3;
        if (r1 != r2) goto L_0x018e;
        r1 = r0.passwordOk;
        if (r1 == 0) goto L_0x016c;
        r4 = 4;
        r9 = r4;
        goto L_0x016d;
        goto L_0x016a;
        r1 = new org.telegram.ui.PaymentFormActivity;
        r7 = r0.paymentForm;
        r8 = r0.messageObject;
        r10 = r0.requestedInfo;
        r11 = r0.shippingOption;
        r12 = r0.paymentJson;
        r13 = r0.cardName;
        r14 = r0.validateRequest;
        r15 = r0.saveCardInfo;
        r2 = r0.androidPayCredentials;
        r6 = r1;
        r16 = r2;
        r6.<init>(r7, r8, r9, r10, r11, r12, r13, r14, r15, r16);
        r2 = r0.passwordOk;
        r2 = r2 ^ r5;
        r0.presentFragment(r1, r2);
        goto L_0x01db;
        r1 = r0.currentStep;
        r2 = 4;
        if (r1 != r2) goto L_0x01a4;
        r1 = r0.currentAccount;
        r1 = org.telegram.messenger.NotificationCenter.getInstance(r1);
        r2 = org.telegram.messenger.NotificationCenter.paymentFinished;
        r3 = new java.lang.Object[r3];
        r1.postNotificationName(r2, r3);
        r17.finishFragment();
        goto L_0x01db;
        r1 = r0.currentStep;
        r2 = 6;
        if (r1 != r2) goto L_0x01db;
        r1 = r0.delegate;
        r2 = r0.paymentJson;
        r3 = r0.cardName;
        r4 = r0.saveCardInfo;
        r6 = r0.androidPayCredentials;
        r1 = r1.didSelectNewCard(r2, r3, r4, r6);
        if (r1 != 0) goto L_0x01d8;
        r1 = new org.telegram.ui.PaymentFormActivity;
        r7 = r0.paymentForm;
        r8 = r0.messageObject;
        r9 = 4;
        r10 = r0.requestedInfo;
        r11 = r0.shippingOption;
        r12 = r0.paymentJson;
        r13 = r0.cardName;
        r14 = r0.validateRequest;
        r15 = r0.saveCardInfo;
        r2 = r0.androidPayCredentials;
        r6 = r1;
        r16 = r2;
        r6.<init>(r7, r8, r9, r10, r11, r12, r13, r14, r15, r16);
        r0.presentFragment(r1, r5);
        goto L_0x01db;
        r17.finishFragment();
    L_0x01db:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.ui.PaymentFormActivity.goToNextStep():void");
    }

    private void updatePasswordFields() {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.ui.PaymentFormActivity.updatePasswordFields():void
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:116)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:249)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
Caused by: java.lang.NullPointerException
*/
        /*
        r0 = this;
        r0 = r10.currentStep;
        r1 = 6;
        if (r0 != r1) goto L_0x0156;
    L_0x0005:
        r0 = r10.bottomCell;
        r1 = 2;
        r0 = r0[r1];
        if (r0 != 0) goto L_0x000e;
    L_0x000c:
        goto L_0x0156;
    L_0x000e:
        r0 = r10.currentPassword;
        r2 = 3;
        r3 = 8;
        r4 = 1;
        r5 = 0;
        if (r0 != 0) goto L_0x006b;
    L_0x0017:
        r0 = r10.doneItem;
        r0.setVisibility(r5);
        r10.showEditDoneProgress(r4, r4);
        r0 = r10.bottomCell;
        r0 = r0[r1];
        r0.setVisibility(r3);
        r0 = r10.settingsCell1;
        r0.setVisibility(r3);
        r0 = r10.headerCell;
        r0 = r0[r5];
        r0.setVisibility(r3);
        r0 = r10.headerCell;
        r0 = r0[r4];
        r0.setVisibility(r3);
        r0 = r10.bottomCell;
        r0 = r0[r5];
        r0.setVisibility(r3);
        r0 = r5;
        if (r0 >= r2) goto L_0x0053;
    L_0x0043:
        r1 = r10.inputFields;
        r1 = r1[r0];
        r1 = r1.getParent();
        r1 = (android.view.View) r1;
        r1.setVisibility(r3);
        r0 = r0 + 1;
        goto L_0x0041;
        r0 = r5;
        r1 = r10.dividers;
        r1 = r1.size();
        if (r0 >= r1) goto L_0x0155;
        r1 = r10.dividers;
        r1 = r1.get(r0);
        r1 = (android.view.View) r1;
        r1.setVisibility(r3);
        r5 = r0 + 1;
        goto L_0x0054;
    L_0x006b:
        r10.showEditDoneProgress(r4, r5);
        r0 = r10.waitingForEmail;
        if (r0 == 0) goto L_0x00f5;
        r0 = r10.getParentActivity();
        if (r0 == 0) goto L_0x0083;
        r0 = r10.getParentActivity();
        r0 = r0.getCurrentFocus();
        org.telegram.messenger.AndroidUtilities.hideKeyboard(r0);
        r0 = r10.doneItem;
        r0.setVisibility(r3);
        r0 = r10.bottomCell;
        r0 = r0[r1];
        r6 = "EmailPasswordConfirmText";
        r7 = 2131493418; // 0x7f0c022a float:1.8610316E38 double:1.053097672E-314;
        r8 = new java.lang.Object[r4];
        r9 = r10.currentPassword;
        r9 = r9.email_unconfirmed_pattern;
        r8[r5] = r9;
        r6 = org.telegram.messenger.LocaleController.formatString(r6, r7, r8);
        r0.setText(r6);
        r0 = r10.bottomCell;
        r0 = r0[r1];
        r0.setVisibility(r5);
        r0 = r10.settingsCell1;
        r0.setVisibility(r5);
        r0 = r10.bottomCell;
        r0 = r0[r4];
        r1 = "";
        r0.setText(r1);
        r0 = r10.headerCell;
        r0 = r0[r5];
        r0.setVisibility(r3);
        r0 = r10.headerCell;
        r0 = r0[r4];
        r0.setVisibility(r3);
        r0 = r10.bottomCell;
        r0 = r0[r5];
        r0.setVisibility(r3);
        r0 = r5;
        if (r0 >= r2) goto L_0x00dd;
        r1 = r10.inputFields;
        r1 = r1[r0];
        r1 = r1.getParent();
        r1 = (android.view.View) r1;
        r1.setVisibility(r3);
        r0 = r0 + 1;
        goto L_0x00cb;
        r0 = r5;
        r1 = r10.dividers;
        r1 = r1.size();
        if (r0 >= r1) goto L_0x0155;
        r1 = r10.dividers;
        r1 = r1.get(r0);
        r1 = (android.view.View) r1;
        r1.setVisibility(r3);
        r5 = r0 + 1;
        goto L_0x00de;
        r0 = r10.doneItem;
        r0.setVisibility(r5);
        r0 = r10.bottomCell;
        r0 = r0[r1];
        r0.setVisibility(r3);
        r0 = r10.settingsCell1;
        r0.setVisibility(r3);
        r0 = r10.bottomCell;
        r0 = r0[r4];
        r1 = "PaymentPasswordEmailInfo";
        r3 = 2131494106; // 0x7f0c04da float:1.8611711E38 double:1.053098012E-314;
        r1 = org.telegram.messenger.LocaleController.getString(r1, r3);
        r0.setText(r1);
        r0 = r10.headerCell;
        r0 = r0[r5];
        r0.setVisibility(r5);
        r0 = r10.headerCell;
        r0 = r0[r4];
        r0.setVisibility(r5);
        r0 = r10.bottomCell;
        r0 = r0[r5];
        r0.setVisibility(r5);
        r0 = r5;
        if (r0 >= r2) goto L_0x013e;
        r1 = r10.inputFields;
        r1 = r1[r0];
        r1 = r1.getParent();
        r1 = (android.view.View) r1;
        r1.setVisibility(r5);
        r0 = r0 + 1;
        goto L_0x012c;
        r0 = r5;
        r1 = r10.dividers;
        r1 = r1.size();
        if (r0 >= r1) goto L_0x0155;
        r1 = r10.dividers;
        r1 = r1.get(r0);
        r1 = (android.view.View) r1;
        r1.setVisibility(r5);
        r0 = r0 + 1;
        goto L_0x013f;
        return;
    L_0x0156:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.ui.PaymentFormActivity.updatePasswordFields():void");
    }

    @android.annotation.SuppressLint({"SetJavaScriptEnabled", "AddJavascriptInterface"})
    public android.view.View createView(android.content.Context r1) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.ui.PaymentFormActivity.createView(android.content.Context):android.view.View
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:116)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:249)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
Caused by: java.lang.NullPointerException
*/
        /*
        r0 = this;
        r1 = r28;
        r2 = r29;
        r3 = r1.currentStep;
        r4 = 6;
        r5 = 5;
        r6 = 4;
        r7 = 3;
        r8 = 2;
        r9 = 1;
        if (r3 != 0) goto L_0x001e;
    L_0x000e:
        r3 = r1.actionBar;
        r10 = "PaymentShippingInfo";
        r11 = 2131494122; // 0x7f0c04ea float:1.8611743E38 double:1.05309802E-314;
        r10 = org.telegram.messenger.LocaleController.getString(r10, r11);
        r3.setTitle(r10);
        goto L_0x00e2;
    L_0x001e:
        r3 = r1.currentStep;
        if (r3 != r9) goto L_0x0032;
    L_0x0022:
        r3 = r1.actionBar;
        r10 = "PaymentShippingMethod";
        r11 = 2131494123; // 0x7f0c04eb float:1.8611745E38 double:1.0530980205E-314;
        r10 = org.telegram.messenger.LocaleController.getString(r10, r11);
        r3.setTitle(r10);
        goto L_0x00e2;
    L_0x0032:
        r3 = r1.currentStep;
        if (r3 != r8) goto L_0x0046;
    L_0x0036:
        r3 = r1.actionBar;
        r10 = "PaymentCardInfo";
        r11 = 2131494082; // 0x7f0c04c2 float:1.8611662E38 double:1.053098E-314;
        r10 = org.telegram.messenger.LocaleController.getString(r10, r11);
        r3.setTitle(r10);
        goto L_0x00e2;
    L_0x0046:
        r3 = r1.currentStep;
        if (r3 != r7) goto L_0x005a;
    L_0x004a:
        r3 = r1.actionBar;
        r10 = "PaymentCardInfo";
        r11 = 2131494082; // 0x7f0c04c2 float:1.8611662E38 double:1.053098E-314;
        r10 = org.telegram.messenger.LocaleController.getString(r10, r11);
        r3.setTitle(r10);
        goto L_0x00e2;
    L_0x005a:
        r3 = r1.currentStep;
        if (r3 != r6) goto L_0x0095;
    L_0x005e:
        r3 = r1.paymentForm;
        r3 = r3.invoice;
        r3 = r3.test;
        if (r3 == 0) goto L_0x0086;
    L_0x0066:
        r3 = r1.actionBar;
        r10 = new java.lang.StringBuilder;
        r10.<init>();
        r11 = "Test ";
        r10.append(r11);
        r11 = "PaymentCheckout";
        r12 = 2131494089; // 0x7f0c04c9 float:1.8611677E38 double:1.0530980037E-314;
        r11 = org.telegram.messenger.LocaleController.getString(r11, r12);
        r10.append(r11);
        r10 = r10.toString();
        r3.setTitle(r10);
        goto L_0x00e2;
    L_0x0086:
        r3 = r1.actionBar;
        r10 = "PaymentCheckout";
        r11 = 2131494089; // 0x7f0c04c9 float:1.8611677E38 double:1.0530980037E-314;
        r10 = org.telegram.messenger.LocaleController.getString(r10, r11);
        r3.setTitle(r10);
        goto L_0x00e2;
    L_0x0095:
        r3 = r1.currentStep;
        if (r3 != r5) goto L_0x00d0;
    L_0x0099:
        r3 = r1.paymentForm;
        r3 = r3.invoice;
        r3 = r3.test;
        if (r3 == 0) goto L_0x00c1;
    L_0x00a1:
        r3 = r1.actionBar;
        r10 = new java.lang.StringBuilder;
        r10.<init>();
        r11 = "Test ";
        r10.append(r11);
        r11 = "PaymentReceipt";
        r12 = 2131494115; // 0x7f0c04e3 float:1.861173E38 double:1.0530980165E-314;
        r11 = org.telegram.messenger.LocaleController.getString(r11, r12);
        r10.append(r11);
        r10 = r10.toString();
        r3.setTitle(r10);
        goto L_0x00e2;
    L_0x00c1:
        r3 = r1.actionBar;
        r10 = "PaymentReceipt";
        r11 = 2131494115; // 0x7f0c04e3 float:1.861173E38 double:1.0530980165E-314;
        r10 = org.telegram.messenger.LocaleController.getString(r10, r11);
        r3.setTitle(r10);
        goto L_0x00e2;
    L_0x00d0:
        r3 = r1.currentStep;
        if (r3 != r4) goto L_0x00e2;
    L_0x00d4:
        r3 = r1.actionBar;
        r10 = "PaymentPassword";
        r11 = 2131494104; // 0x7f0c04d8 float:1.8611707E38 double:1.053098011E-314;
        r10 = org.telegram.messenger.LocaleController.getString(r10, r11);
        r3.setTitle(r10);
    L_0x00e2:
        r3 = r1.actionBar;
        r10 = 2131165346; // 0x7f0700a2 float:1.7944907E38 double:1.052935583E-314;
        r3.setBackButtonImage(r10);
        r3 = r1.actionBar;
        r3.setAllowOverlayTitle(r9);
        r3 = r1.actionBar;
        r10 = new org.telegram.ui.PaymentFormActivity$1;
        r10.<init>();
        r3.setActionBarMenuOnItemClick(r10);
        r3 = r1.actionBar;
        r3 = r3.createMenu();
        r10 = r1.currentStep;
        r11 = -1;
        if (r10 == 0) goto L_0x0118;
    L_0x0104:
        r10 = r1.currentStep;
        if (r10 == r9) goto L_0x0118;
    L_0x0108:
        r10 = r1.currentStep;
        if (r10 == r8) goto L_0x0118;
    L_0x010c:
        r10 = r1.currentStep;
        if (r10 == r7) goto L_0x0118;
    L_0x0110:
        r10 = r1.currentStep;
        if (r10 == r6) goto L_0x0118;
    L_0x0114:
        r10 = r1.currentStep;
        if (r10 != r4) goto L_0x0140;
    L_0x0118:
        r10 = 2131165376; // 0x7f0700c0 float:1.7944967E38 double:1.052935598E-314;
        r12 = 1113587712; // 0x42600000 float:56.0 double:5.50185432E-315;
        r12 = org.telegram.messenger.AndroidUtilities.dp(r12);
        r10 = r3.addItemWithWidth(r9, r10, r12);
        r1.doneItem = r10;
        r10 = new org.telegram.ui.Components.ContextProgressView;
        r10.<init>(r2, r9);
        r1.progressView = r10;
        r10 = r1.doneItem;
        r12 = r1.progressView;
        r13 = -1082130432; // 0xffffffffbf800000 float:-1.0 double:NaN;
        r13 = org.telegram.ui.Components.LayoutHelper.createFrame(r11, r13);
        r10.addView(r12, r13);
        r10 = r1.progressView;
        r10.setVisibility(r6);
    L_0x0140:
        r10 = new android.widget.FrameLayout;
        r10.<init>(r2);
        r1.fragmentView = r10;
        r10 = r1.fragmentView;
        r10 = (android.widget.FrameLayout) r10;
        r12 = r1.fragmentView;
        r13 = "windowBackgroundGray";
        r13 = org.telegram.ui.ActionBar.Theme.getColor(r13);
        r12.setBackgroundColor(r13);
        r12 = new android.widget.ScrollView;
        r12.<init>(r2);
        r1.scrollView = r12;
        r12 = r1.scrollView;
        r12.setFillViewport(r9);
        r12 = r1.scrollView;
        r13 = "actionBarDefault";
        r13 = org.telegram.ui.ActionBar.Theme.getColor(r13);
        org.telegram.messenger.AndroidUtilities.setScrollViewEdgeEffectColor(r12, r13);
        r12 = r1.scrollView;
        r13 = -1;
        r14 = -1082130432; // 0xffffffffbf800000 float:-1.0 double:NaN;
        r15 = 51;
        r16 = 0;
        r17 = 0;
        r18 = 0;
        r5 = r1.currentStep;
        if (r5 != r6) goto L_0x0183;
    L_0x017e:
        r5 = 1111490560; // 0x42400000 float:48.0 double:5.491493014E-315;
    L_0x0180:
        r19 = r5;
        goto L_0x0185;
    L_0x0183:
        r5 = 0;
        goto L_0x0180;
    L_0x0185:
        r5 = org.telegram.ui.Components.LayoutHelper.createFrame(r13, r14, r15, r16, r17, r18, r19);
        r10.addView(r12, r5);
        r5 = new android.widget.LinearLayout;
        r5.<init>(r2);
        r1.linearLayout2 = r5;
        r5 = r1.linearLayout2;
        r5.setOrientation(r9);
        r5 = r1.scrollView;
        r12 = r1.linearLayout2;
        r13 = new android.widget.FrameLayout$LayoutParams;
        r14 = -2;
        r13.<init>(r11, r14);
        r5.addView(r12, r13);
        r5 = r1.currentStep;
        r6 = 0;
        if (r5 != 0) goto L_0x09eb;
    L_0x01aa:
        r5 = new java.util.HashMap;
        r5.<init>();
        r13 = new java.util.HashMap;
        r13.<init>();
        r12 = new java.io.BufferedReader;	 Catch:{ Exception -> 0x0216 }
        r15 = new java.io.InputStreamReader;	 Catch:{ Exception -> 0x0216 }
        r4 = r29.getResources();	 Catch:{ Exception -> 0x0216 }
        r4 = r4.getAssets();	 Catch:{ Exception -> 0x0216 }
        r11 = "countries.txt";	 Catch:{ Exception -> 0x0216 }
        r4 = r4.open(r11);	 Catch:{ Exception -> 0x0216 }
        r15.<init>(r4);	 Catch:{ Exception -> 0x0216 }
        r12.<init>(r15);	 Catch:{ Exception -> 0x0216 }
    L_0x01cc:
        r4 = r12.readLine();	 Catch:{ Exception -> 0x0216 }
        r11 = r4;	 Catch:{ Exception -> 0x0216 }
        if (r4 == 0) goto L_0x0212;	 Catch:{ Exception -> 0x0216 }
    L_0x01d3:
        r4 = ";";	 Catch:{ Exception -> 0x0216 }
        r4 = r11.split(r4);	 Catch:{ Exception -> 0x0216 }
        r15 = r1.countriesArray;	 Catch:{ Exception -> 0x0216 }
        r14 = r4[r8];	 Catch:{ Exception -> 0x0216 }
        r15.add(r6, r14);	 Catch:{ Exception -> 0x0216 }
        r14 = r1.countriesMap;	 Catch:{ Exception -> 0x0216 }
        r15 = r4[r8];	 Catch:{ Exception -> 0x0216 }
        r7 = r4[r6];	 Catch:{ Exception -> 0x0216 }
        r14.put(r15, r7);	 Catch:{ Exception -> 0x0216 }
        r7 = r1.codesMap;	 Catch:{ Exception -> 0x0216 }
        r14 = r4[r6];	 Catch:{ Exception -> 0x0216 }
        r15 = r4[r8];	 Catch:{ Exception -> 0x0216 }
        r7.put(r14, r15);	 Catch:{ Exception -> 0x0216 }
        r7 = r4[r9];	 Catch:{ Exception -> 0x0216 }
        r14 = r4[r8];	 Catch:{ Exception -> 0x0216 }
        r13.put(r7, r14);	 Catch:{ Exception -> 0x0216 }
        r7 = r4.length;	 Catch:{ Exception -> 0x0216 }
        r14 = 3;	 Catch:{ Exception -> 0x0216 }
        if (r7 <= r14) goto L_0x0206;	 Catch:{ Exception -> 0x0216 }
    L_0x01fd:
        r7 = r1.phoneFormatMap;	 Catch:{ Exception -> 0x0216 }
        r15 = r4[r6];	 Catch:{ Exception -> 0x0216 }
        r6 = r4[r14];	 Catch:{ Exception -> 0x0216 }
        r7.put(r15, r6);	 Catch:{ Exception -> 0x0216 }
    L_0x0206:
        r6 = r4[r9];	 Catch:{ Exception -> 0x0216 }
        r7 = r4[r8];	 Catch:{ Exception -> 0x0216 }
        r5.put(r6, r7);	 Catch:{ Exception -> 0x0216 }
        r6 = 0;	 Catch:{ Exception -> 0x0216 }
        r7 = 3;	 Catch:{ Exception -> 0x0216 }
        r14 = -2;	 Catch:{ Exception -> 0x0216 }
        goto L_0x01cc;	 Catch:{ Exception -> 0x0216 }
    L_0x0212:
        r12.close();	 Catch:{ Exception -> 0x0216 }
        goto L_0x021b;
    L_0x0216:
        r0 = move-exception;
        r4 = r0;
        org.telegram.messenger.FileLog.e(r4);
    L_0x021b:
        r4 = r1.countriesArray;
        r6 = new org.telegram.ui.PaymentFormActivity$2;
        r6.<init>();
        java.util.Collections.sort(r4, r6);
        r4 = 10;
        r4 = new org.telegram.ui.Components.EditTextBoldCursor[r4];
        r1.inputFields = r4;
        r4 = 0;
        r6 = 10;
        r7 = 9;
        if (r4 >= r6) goto L_0x07f3;
    L_0x0232:
        if (r4 != 0) goto L_0x026b;
    L_0x0234:
        r6 = r1.headerCell;
        r11 = new org.telegram.ui.Cells.HeaderCell;
        r11.<init>(r2);
        r12 = 0;
        r6[r12] = r11;
        r6 = r1.headerCell;
        r6 = r6[r12];
        r11 = "windowBackgroundWhite";
        r11 = org.telegram.ui.ActionBar.Theme.getColor(r11);
        r6.setBackgroundColor(r11);
        r6 = r1.headerCell;
        r6 = r6[r12];
        r11 = "PaymentShippingAddress";
        r14 = 2131494116; // 0x7f0c04e4 float:1.8611731E38 double:1.053098017E-314;
        r11 = org.telegram.messenger.LocaleController.getString(r11, r14);
        r6.setText(r11);
        r6 = r1.linearLayout2;
        r11 = r1.headerCell;
        r11 = r11[r12];
        r14 = -1;
        r15 = -2;
        r8 = org.telegram.ui.Components.LayoutHelper.createLinear(r14, r15);
        r6.addView(r11, r8);
        goto L_0x02bc;
    L_0x026b:
        r12 = 0;
        r6 = 6;
        if (r4 != r6) goto L_0x02bc;
    L_0x026f:
        r6 = r1.sectionCell;
        r8 = new org.telegram.ui.Cells.ShadowSectionCell;
        r8.<init>(r2);
        r6[r12] = r8;
        r6 = r1.linearLayout2;
        r8 = r1.sectionCell;
        r8 = r8[r12];
        r11 = -1;
        r12 = -2;
        r14 = org.telegram.ui.Components.LayoutHelper.createLinear(r11, r12);
        r6.addView(r8, r14);
        r6 = r1.headerCell;
        r8 = new org.telegram.ui.Cells.HeaderCell;
        r8.<init>(r2);
        r6[r9] = r8;
        r6 = r1.headerCell;
        r6 = r6[r9];
        r8 = "windowBackgroundWhite";
        r8 = org.telegram.ui.ActionBar.Theme.getColor(r8);
        r6.setBackgroundColor(r8);
        r6 = r1.headerCell;
        r6 = r6[r9];
        r8 = "PaymentShippingReceiver";
        r11 = 2131494126; // 0x7f0c04ee float:1.8611752E38 double:1.053098022E-314;
        r8 = org.telegram.messenger.LocaleController.getString(r8, r11);
        r6.setText(r8);
        r6 = r1.linearLayout2;
        r8 = r1.headerCell;
        r8 = r8[r9];
        r11 = -1;
        r12 = -2;
        r14 = org.telegram.ui.Components.LayoutHelper.createLinear(r11, r12);
        r6.addView(r8, r14);
    L_0x02bc:
        r6 = 8;
        if (r4 != r6) goto L_0x02e3;
    L_0x02c0:
        r6 = new android.widget.LinearLayout;
        r6.<init>(r2);
        r8 = r6;
        r8 = (android.widget.LinearLayout) r8;
        r11 = 0;
        r8.setOrientation(r11);
        r8 = r1.linearLayout2;
        r11 = -1;
        r12 = 48;
        r14 = org.telegram.ui.Components.LayoutHelper.createLinear(r11, r12);
        r8.addView(r6, r14);
        r8 = "windowBackgroundWhite";
        r8 = org.telegram.ui.ActionBar.Theme.getColor(r8);
        r6.setBackgroundColor(r8);
        goto L_0x0357;
    L_0x02e3:
        if (r4 != r7) goto L_0x02f2;
    L_0x02e5:
        r6 = r1.inputFields;
        r8 = 8;
        r6 = r6[r8];
        r6 = r6.getParent();
        r6 = (android.view.ViewGroup) r6;
        goto L_0x0357;
    L_0x02f2:
        r6 = new android.widget.FrameLayout;
        r6.<init>(r2);
        r8 = r1.linearLayout2;
        r11 = -1;
        r12 = 48;
        r14 = org.telegram.ui.Components.LayoutHelper.createLinear(r11, r12);
        r8.addView(r6, r14);
        r8 = "windowBackgroundWhite";
        r8 = org.telegram.ui.ActionBar.Theme.getColor(r8);
        r6.setBackgroundColor(r8);
        r8 = 5;
        if (r4 == r8) goto L_0x0313;
    L_0x030f:
        if (r4 == r7) goto L_0x0313;
    L_0x0311:
        r8 = r9;
        goto L_0x0314;
    L_0x0313:
        r8 = 0;
    L_0x0314:
        if (r8 == 0) goto L_0x0337;
    L_0x0316:
        r11 = 7;
        if (r4 != r11) goto L_0x0323;
    L_0x0319:
        r11 = r1.paymentForm;
        r11 = r11.invoice;
        r11 = r11.phone_requested;
        if (r11 != 0) goto L_0x0323;
    L_0x0321:
        r8 = 0;
        goto L_0x0337;
    L_0x0323:
        r11 = 6;
        if (r4 != r11) goto L_0x0337;
    L_0x0326:
        r11 = r1.paymentForm;
        r11 = r11.invoice;
        r11 = r11.phone_requested;
        if (r11 != 0) goto L_0x0337;
    L_0x032e:
        r11 = r1.paymentForm;
        r11 = r11.invoice;
        r11 = r11.email_requested;
        if (r11 != 0) goto L_0x0337;
    L_0x0336:
        r8 = 0;
    L_0x0337:
        if (r8 == 0) goto L_0x0357;
    L_0x0339:
        r11 = new android.view.View;
        r11.<init>(r2);
        r12 = r1.dividers;
        r12.add(r11);
        r12 = "divider";
        r12 = org.telegram.ui.ActionBar.Theme.getColor(r12);
        r11.setBackgroundColor(r12);
        r12 = new android.widget.FrameLayout$LayoutParams;
        r14 = 83;
        r15 = -1;
        r12.<init>(r15, r9, r14);
        r6.addView(r11, r12);
    L_0x0357:
        if (r4 != r7) goto L_0x0363;
    L_0x0359:
        r8 = r1.inputFields;
        r11 = new org.telegram.ui.Components.HintEditText;
        r11.<init>(r2);
        r8[r4] = r11;
        goto L_0x036c;
    L_0x0363:
        r8 = r1.inputFields;
        r11 = new org.telegram.ui.Components.EditTextBoldCursor;
        r11.<init>(r2);
        r8[r4] = r11;
    L_0x036c:
        r8 = r1.inputFields;
        r8 = r8[r4];
        r11 = java.lang.Integer.valueOf(r4);
        r8.setTag(r11);
        r8 = r1.inputFields;
        r8 = r8[r4];
        r11 = 1098907648; // 0x41800000 float:16.0 double:5.42932517E-315;
        r8.setTextSize(r9, r11);
        r8 = r1.inputFields;
        r8 = r8[r4];
        r11 = "windowBackgroundWhiteHintText";
        r11 = org.telegram.ui.ActionBar.Theme.getColor(r11);
        r8.setHintTextColor(r11);
        r8 = r1.inputFields;
        r8 = r8[r4];
        r11 = "windowBackgroundWhiteBlackText";
        r11 = org.telegram.ui.ActionBar.Theme.getColor(r11);
        r8.setTextColor(r11);
        r8 = r1.inputFields;
        r8 = r8[r4];
        r11 = 0;
        r8.setBackgroundDrawable(r11);
        r8 = r1.inputFields;
        r8 = r8[r4];
        r11 = "windowBackgroundWhiteBlackText";
        r11 = org.telegram.ui.ActionBar.Theme.getColor(r11);
        r8.setCursorColor(r11);
        r8 = r1.inputFields;
        r8 = r8[r4];
        r11 = 1101004800; // 0x41a00000 float:20.0 double:5.439686476E-315;
        r11 = org.telegram.messenger.AndroidUtilities.dp(r11);
        r8.setCursorSize(r11);
        r8 = r1.inputFields;
        r8 = r8[r4];
        r11 = 1069547520; // 0x3fc00000 float:1.5 double:5.28426686E-315;
        r8.setCursorWidth(r11);
        r8 = 4;
        if (r4 != r8) goto L_0x03dc;
    L_0x03c8:
        r8 = r1.inputFields;
        r8 = r8[r4];
        r11 = new org.telegram.ui.PaymentFormActivity$3;
        r11.<init>();
        r8.setOnTouchListener(r11);
        r8 = r1.inputFields;
        r8 = r8[r4];
        r11 = 0;
        r8.setInputType(r11);
    L_0x03dc:
        if (r4 == r7) goto L_0x03f8;
    L_0x03de:
        r8 = 8;
        if (r4 != r8) goto L_0x03e3;
    L_0x03e2:
        goto L_0x03f8;
    L_0x03e3:
        r8 = 7;
        if (r4 != r8) goto L_0x03ee;
    L_0x03e6:
        r8 = r1.inputFields;
        r8 = r8[r4];
        r8.setInputType(r9);
        goto L_0x0400;
    L_0x03ee:
        r8 = r1.inputFields;
        r8 = r8[r4];
        r11 = 16385; // 0x4001 float:2.296E-41 double:8.0953E-320;
        r8.setInputType(r11);
        goto L_0x0400;
    L_0x03f8:
        r8 = r1.inputFields;
        r8 = r8[r4];
        r11 = 3;
        r8.setInputType(r11);
    L_0x0400:
        r8 = r1.inputFields;
        r8 = r8[r4];
        r11 = 268435461; // 0x10000005 float:2.5243564E-29 double:1.326247394E-315;
        r8.setImeOptions(r11);
        switch(r4) {
            case 0: goto L_0x0568;
            case 1: goto L_0x053a;
            case 2: goto L_0x050c;
            case 3: goto L_0x04dd;
            case 4: goto L_0x0498;
            case 5: goto L_0x0469;
            case 6: goto L_0x043c;
            case 7: goto L_0x040f;
            default: goto L_0x040d;
        };
    L_0x040d:
        goto L_0x0595;
    L_0x040f:
        r8 = r1.inputFields;
        r8 = r8[r4];
        r11 = "PaymentShippingEmailPlaceholder";
        r12 = 2131494121; // 0x7f0c04e9 float:1.8611741E38 double:1.0530980195E-314;
        r11 = org.telegram.messenger.LocaleController.getString(r11, r12);
        r8.setHint(r11);
        r8 = r1.paymentForm;
        r8 = r8.saved_info;
        if (r8 == 0) goto L_0x0595;
    L_0x0425:
        r8 = r1.paymentForm;
        r8 = r8.saved_info;
        r8 = r8.email;
        if (r8 == 0) goto L_0x0595;
    L_0x042d:
        r8 = r1.inputFields;
        r8 = r8[r4];
        r11 = r1.paymentForm;
        r11 = r11.saved_info;
        r11 = r11.email;
        r8.setText(r11);
        goto L_0x0595;
    L_0x043c:
        r8 = r1.inputFields;
        r8 = r8[r4];
        r11 = "PaymentShippingName";
        r12 = 2131494124; // 0x7f0c04ec float:1.8611748E38 double:1.053098021E-314;
        r11 = org.telegram.messenger.LocaleController.getString(r11, r12);
        r8.setHint(r11);
        r8 = r1.paymentForm;
        r8 = r8.saved_info;
        if (r8 == 0) goto L_0x0595;
    L_0x0452:
        r8 = r1.paymentForm;
        r8 = r8.saved_info;
        r8 = r8.name;
        if (r8 == 0) goto L_0x0595;
    L_0x045a:
        r8 = r1.inputFields;
        r8 = r8[r4];
        r11 = r1.paymentForm;
        r11 = r11.saved_info;
        r11 = r11.name;
        r8.setText(r11);
        goto L_0x0595;
    L_0x0469:
        r8 = r1.inputFields;
        r8 = r8[r4];
        r11 = "PaymentShippingZipPlaceholder";
        r12 = 2131494130; // 0x7f0c04f2 float:1.861176E38 double:1.053098024E-314;
        r11 = org.telegram.messenger.LocaleController.getString(r11, r12);
        r8.setHint(r11);
        r8 = r1.paymentForm;
        r8 = r8.saved_info;
        if (r8 == 0) goto L_0x0595;
    L_0x047f:
        r8 = r1.paymentForm;
        r8 = r8.saved_info;
        r8 = r8.shipping_address;
        if (r8 == 0) goto L_0x0595;
    L_0x0487:
        r8 = r1.inputFields;
        r8 = r8[r4];
        r11 = r1.paymentForm;
        r11 = r11.saved_info;
        r11 = r11.shipping_address;
        r11 = r11.post_code;
        r8.setText(r11);
        goto L_0x0595;
    L_0x0498:
        r8 = r1.inputFields;
        r8 = r8[r4];
        r11 = "PaymentShippingCountry";
        r12 = 2131494120; // 0x7f0c04e8 float:1.861174E38 double:1.053098019E-314;
        r11 = org.telegram.messenger.LocaleController.getString(r11, r12);
        r8.setHint(r11);
        r8 = r1.paymentForm;
        r8 = r8.saved_info;
        if (r8 == 0) goto L_0x0595;
    L_0x04ae:
        r8 = r1.paymentForm;
        r8 = r8.saved_info;
        r8 = r8.shipping_address;
        if (r8 == 0) goto L_0x0595;
    L_0x04b6:
        r8 = r1.paymentForm;
        r8 = r8.saved_info;
        r8 = r8.shipping_address;
        r8 = r8.country_iso2;
        r8 = r13.get(r8);
        r8 = (java.lang.String) r8;
        r11 = r1.paymentForm;
        r11 = r11.saved_info;
        r11 = r11.shipping_address;
        r11 = r11.country_iso2;
        r1.countryName = r11;
        r11 = r1.inputFields;
        r11 = r11[r4];
        if (r8 == 0) goto L_0x04d6;
    L_0x04d4:
        r12 = r8;
        goto L_0x04d8;
    L_0x04d6:
        r12 = r1.countryName;
    L_0x04d8:
        r11.setText(r12);
        goto L_0x0595;
    L_0x04dd:
        r8 = r1.inputFields;
        r8 = r8[r4];
        r11 = "PaymentShippingStatePlaceholder";
        r12 = 2131494129; // 0x7f0c04f1 float:1.8611758E38 double:1.0530980235E-314;
        r11 = org.telegram.messenger.LocaleController.getString(r11, r12);
        r8.setHint(r11);
        r8 = r1.paymentForm;
        r8 = r8.saved_info;
        if (r8 == 0) goto L_0x0595;
    L_0x04f3:
        r8 = r1.paymentForm;
        r8 = r8.saved_info;
        r8 = r8.shipping_address;
        if (r8 == 0) goto L_0x0595;
    L_0x04fb:
        r8 = r1.inputFields;
        r8 = r8[r4];
        r11 = r1.paymentForm;
        r11 = r11.saved_info;
        r11 = r11.shipping_address;
        r11 = r11.state;
        r8.setText(r11);
        goto L_0x0595;
    L_0x050c:
        r8 = r1.inputFields;
        r8 = r8[r4];
        r11 = "PaymentShippingCityPlaceholder";
        r12 = 2131494119; // 0x7f0c04e7 float:1.8611737E38 double:1.0530980185E-314;
        r11 = org.telegram.messenger.LocaleController.getString(r11, r12);
        r8.setHint(r11);
        r8 = r1.paymentForm;
        r8 = r8.saved_info;
        if (r8 == 0) goto L_0x0595;
    L_0x0522:
        r8 = r1.paymentForm;
        r8 = r8.saved_info;
        r8 = r8.shipping_address;
        if (r8 == 0) goto L_0x0595;
    L_0x052a:
        r8 = r1.inputFields;
        r8 = r8[r4];
        r11 = r1.paymentForm;
        r11 = r11.saved_info;
        r11 = r11.shipping_address;
        r11 = r11.city;
        r8.setText(r11);
        goto L_0x0595;
    L_0x053a:
        r8 = r1.inputFields;
        r8 = r8[r4];
        r11 = "PaymentShippingAddress2Placeholder";
        r12 = 2131494118; // 0x7f0c04e6 float:1.8611735E38 double:1.053098018E-314;
        r11 = org.telegram.messenger.LocaleController.getString(r11, r12);
        r8.setHint(r11);
        r8 = r1.paymentForm;
        r8 = r8.saved_info;
        if (r8 == 0) goto L_0x0595;
    L_0x0550:
        r8 = r1.paymentForm;
        r8 = r8.saved_info;
        r8 = r8.shipping_address;
        if (r8 == 0) goto L_0x0595;
    L_0x0558:
        r8 = r1.inputFields;
        r8 = r8[r4];
        r11 = r1.paymentForm;
        r11 = r11.saved_info;
        r11 = r11.shipping_address;
        r11 = r11.street_line2;
        r8.setText(r11);
        goto L_0x0595;
    L_0x0568:
        r8 = r1.inputFields;
        r8 = r8[r4];
        r11 = "PaymentShippingAddress1Placeholder";
        r12 = 2131494117; // 0x7f0c04e5 float:1.8611733E38 double:1.0530980175E-314;
        r11 = org.telegram.messenger.LocaleController.getString(r11, r12);
        r8.setHint(r11);
        r8 = r1.paymentForm;
        r8 = r8.saved_info;
        if (r8 == 0) goto L_0x0595;
    L_0x057e:
        r8 = r1.paymentForm;
        r8 = r8.saved_info;
        r8 = r8.shipping_address;
        if (r8 == 0) goto L_0x0595;
    L_0x0586:
        r8 = r1.inputFields;
        r8 = r8[r4];
        r11 = r1.paymentForm;
        r11 = r11.saved_info;
        r11 = r11.shipping_address;
        r11 = r11.street_line1;
        r8.setText(r11);
    L_0x0595:
        r8 = r1.inputFields;
        r8 = r8[r4];
        r11 = r1.inputFields;
        r11 = r11[r4];
        r11 = r11.length();
        r8.setSelection(r11);
        r8 = 8;
        if (r4 != r8) goto L_0x0621;
    L_0x05a8:
        r8 = new android.widget.TextView;
        r8.<init>(r2);
        r1.textView = r8;
        r8 = r1.textView;
        r11 = "+";
        r8.setText(r11);
        r8 = r1.textView;
        r11 = "windowBackgroundWhiteBlackText";
        r11 = org.telegram.ui.ActionBar.Theme.getColor(r11);
        r8.setTextColor(r11);
        r8 = r1.textView;
        r11 = 1098907648; // 0x41800000 float:16.0 double:5.42932517E-315;
        r8.setTextSize(r9, r11);
        r8 = r1.textView;
        r14 = -2;
        r15 = -2;
        r16 = 1099431936; // 0x41880000 float:17.0 double:5.431915495E-315;
        r17 = 1094713344; // 0x41400000 float:12.0 double:5.408602553E-315;
        r18 = 0;
        r19 = 1086324736; // 0x40c00000 float:6.0 double:5.367157323E-315;
        r11 = org.telegram.ui.Components.LayoutHelper.createLinear(r14, r15, r16, r17, r18, r19);
        r6.addView(r8, r11);
        r8 = r1.inputFields;
        r8 = r8[r4];
        r11 = 1092616192; // 0x41200000 float:10.0 double:5.398241246E-315;
        r11 = org.telegram.messenger.AndroidUtilities.dp(r11);
        r12 = 0;
        r8.setPadding(r11, r12, r12, r12);
        r8 = r1.inputFields;
        r8 = r8[r4];
        r11 = 19;
        r8.setGravity(r11);
        r8 = new android.text.InputFilter[r9];
        r11 = new android.text.InputFilter$LengthFilter;
        r14 = 5;
        r11.<init>(r14);
        r8[r12] = r11;
        r11 = r1.inputFields;
        r11 = r11[r4];
        r11.setFilters(r8);
        r11 = r1.inputFields;
        r11 = r11[r4];
        r14 = 55;
        r16 = 0;
        r18 = 1098907648; // 0x41800000 float:16.0 double:5.42932517E-315;
        r12 = org.telegram.ui.Components.LayoutHelper.createLinear(r14, r15, r16, r17, r18, r19);
        r6.addView(r11, r12);
        r11 = r1.inputFields;
        r11 = r11[r4];
        r12 = new org.telegram.ui.PaymentFormActivity$4;
        r12.<init>();
        r11.addTextChangedListener(r12);
        goto L_0x068b;
    L_0x0621:
        if (r4 != r7) goto L_0x0656;
    L_0x0623:
        r8 = r1.inputFields;
        r8 = r8[r4];
        r11 = 0;
        r8.setPadding(r11, r11, r11, r11);
        r8 = r1.inputFields;
        r8 = r8[r4];
        r11 = 19;
        r8.setGravity(r11);
        r8 = r1.inputFields;
        r8 = r8[r4];
        r14 = -1;
        r15 = -2;
        r16 = 0;
        r17 = 1094713344; // 0x41400000 float:12.0 double:5.408602553E-315;
        r18 = 1099431936; // 0x41880000 float:17.0 double:5.431915495E-315;
        r19 = 1086324736; // 0x40c00000 float:6.0 double:5.367157323E-315;
        r11 = org.telegram.ui.Components.LayoutHelper.createLinear(r14, r15, r16, r17, r18, r19);
        r6.addView(r8, r11);
        r8 = r1.inputFields;
        r8 = r8[r4];
        r11 = new org.telegram.ui.PaymentFormActivity$5;
        r11.<init>();
        r8.addTextChangedListener(r11);
        goto L_0x068b;
    L_0x0656:
        r8 = r1.inputFields;
        r8 = r8[r4];
        r11 = 1086324736; // 0x40c00000 float:6.0 double:5.367157323E-315;
        r11 = org.telegram.messenger.AndroidUtilities.dp(r11);
        r12 = 0;
        r8.setPadding(r12, r12, r12, r11);
        r8 = r1.inputFields;
        r8 = r8[r4];
        r11 = org.telegram.messenger.LocaleController.isRTL;
        if (r11 == 0) goto L_0x066e;
    L_0x066c:
        r11 = 5;
        goto L_0x066f;
    L_0x066e:
        r11 = 3;
    L_0x066f:
        r8.setGravity(r11);
        r8 = r1.inputFields;
        r8 = r8[r4];
        r20 = -1;
        r21 = -1073741824; // 0xffffffffc0000000 float:-2.0 double:NaN;
        r22 = 51;
        r23 = 1099431936; // 0x41880000 float:17.0 double:5.431915495E-315;
        r24 = 1094713344; // 0x41400000 float:12.0 double:5.408602553E-315;
        r25 = 1099431936; // 0x41880000 float:17.0 double:5.431915495E-315;
        r26 = 1086324736; // 0x40c00000 float:6.0 double:5.367157323E-315;
        r11 = org.telegram.ui.Components.LayoutHelper.createFrame(r20, r21, r22, r23, r24, r25, r26);
        r6.addView(r8, r11);
    L_0x068b:
        r8 = r1.inputFields;
        r8 = r8[r4];
        r11 = new org.telegram.ui.PaymentFormActivity$6;
        r11.<init>();
        r8.setOnEditorActionListener(r11);
        if (r4 != r7) goto L_0x07ed;
    L_0x0699:
        r7 = r1.paymentForm;
        r7 = r7.invoice;
        r7 = r7.email_to_provider;
        if (r7 != 0) goto L_0x06c4;
    L_0x06a1:
        r7 = r1.paymentForm;
        r7 = r7.invoice;
        r7 = r7.phone_to_provider;
        if (r7 == 0) goto L_0x06aa;
    L_0x06a9:
        goto L_0x06c4;
    L_0x06aa:
        r7 = r1.sectionCell;
        r8 = new org.telegram.ui.Cells.ShadowSectionCell;
        r8.<init>(r2);
        r7[r9] = r8;
        r7 = r1.linearLayout2;
        r8 = r1.sectionCell;
        r8 = r8[r9];
        r11 = -1;
        r12 = -2;
        r14 = org.telegram.ui.Components.LayoutHelper.createLinear(r11, r12);
        r7.addView(r8, r14);
        goto L_0x077b;
    L_0x06c4:
        r7 = 0;
        r8 = r7;
        r7 = 0;
    L_0x06c7:
        r11 = r1.paymentForm;
        r11 = r11.users;
        r11 = r11.size();
        if (r7 >= r11) goto L_0x06e7;
    L_0x06d1:
        r11 = r1.paymentForm;
        r11 = r11.users;
        r11 = r11.get(r7);
        r11 = (org.telegram.tgnet.TLRPC.User) r11;
        r12 = r11.id;
        r14 = r1.paymentForm;
        r14 = r14.provider_id;
        if (r12 != r14) goto L_0x06e4;
    L_0x06e3:
        r8 = r11;
    L_0x06e4:
        r7 = r7 + 1;
        goto L_0x06c7;
    L_0x06e7:
        if (r8 == 0) goto L_0x06f2;
    L_0x06e9:
        r7 = r8.first_name;
        r11 = r8.last_name;
        r7 = org.telegram.messenger.ContactsController.formatName(r7, r11);
        goto L_0x06f4;
    L_0x06f2:
        r7 = "";
    L_0x06f4:
        r11 = r1.bottomCell;
        r12 = new org.telegram.ui.Cells.TextInfoPrivacyCell;
        r12.<init>(r2);
        r11[r9] = r12;
        r11 = r1.bottomCell;
        r11 = r11[r9];
        r12 = "windowBackgroundGrayShadow";
        r14 = 2131165332; // 0x7f070094 float:1.7944878E38 double:1.052935576E-314;
        r12 = org.telegram.ui.ActionBar.Theme.getThemedDrawable(r2, r14, r12);
        r11.setBackgroundDrawable(r12);
        r11 = r1.linearLayout2;
        r12 = r1.bottomCell;
        r12 = r12[r9];
        r14 = -1;
        r15 = -2;
        r9 = org.telegram.ui.Components.LayoutHelper.createLinear(r14, r15);
        r11.addView(r12, r9);
        r9 = r1.paymentForm;
        r9 = r9.invoice;
        r9 = r9.email_to_provider;
        if (r9 == 0) goto L_0x0743;
    L_0x0724:
        r9 = r1.paymentForm;
        r9 = r9.invoice;
        r9 = r9.phone_to_provider;
        if (r9 == 0) goto L_0x0743;
    L_0x072c:
        r9 = r1.bottomCell;
        r11 = 1;
        r9 = r9[r11];
        r12 = "PaymentPhoneEmailToProvider";
        r14 = 2131494112; // 0x7f0c04e0 float:1.8611723E38 double:1.053098015E-314;
        r15 = new java.lang.Object[r11];
        r11 = 0;
        r15[r11] = r7;
        r11 = org.telegram.messenger.LocaleController.formatString(r12, r14, r15);
        r9.setText(r11);
        goto L_0x077a;
    L_0x0743:
        r9 = r1.paymentForm;
        r9 = r9.invoice;
        r9 = r9.email_to_provider;
        if (r9 == 0) goto L_0x0763;
        r9 = r1.bottomCell;
        r11 = 1;
        r9 = r9[r11];
        r12 = "PaymentEmailToProvider";
        r14 = 2131494100; // 0x7f0c04d4 float:1.8611699E38 double:1.053098009E-314;
        r15 = new java.lang.Object[r11];
        r16 = 0;
        r15[r16] = r7;
        r12 = org.telegram.messenger.LocaleController.formatString(r12, r14, r15);
        r9.setText(r12);
        goto L_0x077a;
        r11 = 1;
        r16 = 0;
        r9 = r1.bottomCell;
        r9 = r9[r11];
        r12 = "PaymentPhoneToProvider";
        r14 = 2131494113; // 0x7f0c04e1 float:1.8611725E38 double:1.0530980155E-314;
        r15 = new java.lang.Object[r11];
        r15[r16] = r7;
        r11 = org.telegram.messenger.LocaleController.formatString(r12, r14, r15);
        r9.setText(r11);
    L_0x077b:
        r7 = new org.telegram.ui.Cells.TextCheckCell;
        r7.<init>(r2);
        r1.checkCell1 = r7;
        r7 = r1.checkCell1;
        r8 = 1;
        r9 = org.telegram.ui.ActionBar.Theme.getSelectorDrawable(r8);
        r7.setBackgroundDrawable(r9);
        r7 = r1.checkCell1;
        r8 = "PaymentShippingSave";
        r9 = 2131494127; // 0x7f0c04ef float:1.8611754E38 double:1.0530980225E-314;
        r8 = org.telegram.messenger.LocaleController.getString(r8, r9);
        r9 = r1.saveShippingInfo;
        r11 = 0;
        r7.setTextAndCheck(r8, r9, r11);
        r7 = r1.linearLayout2;
        r8 = r1.checkCell1;
        r9 = -1;
        r11 = -2;
        r12 = org.telegram.ui.Components.LayoutHelper.createLinear(r9, r11);
        r7.addView(r8, r12);
        r7 = r1.checkCell1;
        r8 = new org.telegram.ui.PaymentFormActivity$7;
        r8.<init>();
        r7.setOnClickListener(r8);
        r7 = r1.bottomCell;
        r8 = new org.telegram.ui.Cells.TextInfoPrivacyCell;
        r8.<init>(r2);
        r9 = 0;
        r7[r9] = r8;
        r7 = r1.bottomCell;
        r7 = r7[r9];
        r8 = "windowBackgroundGrayShadow";
        r11 = 2131165332; // 0x7f070094 float:1.7944878E38 double:1.052935576E-314;
        r8 = org.telegram.ui.ActionBar.Theme.getThemedDrawable(r2, r11, r8);
        r7.setBackgroundDrawable(r8);
        r7 = r1.bottomCell;
        r7 = r7[r9];
        r8 = "PaymentShippingSaveInfo";
        r11 = 2131494128; // 0x7f0c04f0 float:1.8611756E38 double:1.053098023E-314;
        r8 = org.telegram.messenger.LocaleController.getString(r8, r11);
        r7.setText(r8);
        r7 = r1.linearLayout2;
        r8 = r1.bottomCell;
        r8 = r8[r9];
        r9 = -1;
        r11 = -2;
        r12 = org.telegram.ui.Components.LayoutHelper.createLinear(r9, r11);
        r7.addView(r8, r12);
    L_0x07ed:
        r4 = r4 + 1;
        r8 = 2;
        r9 = 1;
        goto L_0x022c;
    L_0x07f3:
        r4 = r1.paymentForm;
        r4 = r4.invoice;
        r4 = r4.name_requested;
        if (r4 != 0) goto L_0x080c;
        r4 = r1.inputFields;
        r6 = 6;
        r4 = r4[r6];
        r4 = r4.getParent();
        r4 = (android.view.ViewGroup) r4;
        r6 = 8;
        r4.setVisibility(r6);
        goto L_0x080e;
        r6 = 8;
        r4 = r1.paymentForm;
        r4 = r4.invoice;
        r4 = r4.phone_requested;
        if (r4 != 0) goto L_0x0823;
        r4 = r1.inputFields;
        r4 = r4[r6];
        r4 = r4.getParent();
        r4 = (android.view.ViewGroup) r4;
        r4.setVisibility(r6);
        r4 = r1.paymentForm;
        r4 = r4.invoice;
        r4 = r4.email_requested;
        if (r4 != 0) goto L_0x083b;
        r4 = r1.inputFields;
        r6 = 7;
        r4 = r4[r6];
        r4 = r4.getParent();
        r4 = (android.view.ViewGroup) r4;
        r6 = 8;
        r4.setVisibility(r6);
        r4 = r1.paymentForm;
        r4 = r4.invoice;
        r4 = r4.phone_requested;
        if (r4 == 0) goto L_0x084e;
        r4 = r1.inputFields;
        r4 = r4[r7];
        r6 = 268435462; // 0x10000006 float:2.5243567E-29 double:1.3262474E-315;
        r4.setImeOptions(r6);
        goto L_0x087b;
        r6 = 268435462; // 0x10000006 float:2.5243567E-29 double:1.3262474E-315;
        r4 = r1.paymentForm;
        r4 = r4.invoice;
        r4 = r4.email_requested;
        if (r4 == 0) goto L_0x0862;
        r4 = r1.inputFields;
        r7 = 7;
        r4 = r4[r7];
        r4.setImeOptions(r6);
        goto L_0x087b;
        r4 = r1.paymentForm;
        r4 = r4.invoice;
        r4 = r4.name_requested;
        if (r4 == 0) goto L_0x0873;
        r4 = r1.inputFields;
        r7 = 6;
        r4 = r4[r7];
        r4.setImeOptions(r6);
        goto L_0x087b;
        r4 = r1.inputFields;
        r7 = 5;
        r4 = r4[r7];
        r4.setImeOptions(r6);
        r4 = r1.sectionCell;
        r6 = 1;
        r4 = r4[r6];
        if (r4 == 0) goto L_0x08a7;
        r4 = r1.sectionCell;
        r4 = r4[r6];
        r6 = r1.paymentForm;
        r6 = r6.invoice;
        r6 = r6.name_requested;
        if (r6 != 0) goto L_0x08a2;
        r6 = r1.paymentForm;
        r6 = r6.invoice;
        r6 = r6.phone_requested;
        if (r6 != 0) goto L_0x08a2;
        r6 = r1.paymentForm;
        r6 = r6.invoice;
        r6 = r6.email_requested;
        if (r6 == 0) goto L_0x089f;
        goto L_0x08a2;
        r6 = 8;
        goto L_0x08a3;
        r6 = 0;
        r4.setVisibility(r6);
        goto L_0x08d2;
        r4 = r1.bottomCell;
        r6 = 1;
        r4 = r4[r6];
        if (r4 == 0) goto L_0x08d2;
        r4 = r1.bottomCell;
        r4 = r4[r6];
        r6 = r1.paymentForm;
        r6 = r6.invoice;
        r6 = r6.name_requested;
        if (r6 != 0) goto L_0x08ce;
        r6 = r1.paymentForm;
        r6 = r6.invoice;
        r6 = r6.phone_requested;
        if (r6 != 0) goto L_0x08ce;
        r6 = r1.paymentForm;
        r6 = r6.invoice;
        r6 = r6.email_requested;
        if (r6 == 0) goto L_0x08cb;
        goto L_0x08ce;
        r6 = 8;
        goto L_0x08cf;
        r6 = 0;
        r4.setVisibility(r6);
        r4 = r1.headerCell;
        r6 = 1;
        r4 = r4[r6];
        r6 = r1.paymentForm;
        r6 = r6.invoice;
        r6 = r6.name_requested;
        if (r6 != 0) goto L_0x08f3;
        r6 = r1.paymentForm;
        r6 = r6.invoice;
        r6 = r6.phone_requested;
        if (r6 != 0) goto L_0x08f3;
        r6 = r1.paymentForm;
        r6 = r6.invoice;
        r6 = r6.email_requested;
        if (r6 == 0) goto L_0x08f0;
        goto L_0x08f3;
        r6 = 8;
        goto L_0x08f4;
        r6 = 0;
        r4.setVisibility(r6);
        r4 = r1.paymentForm;
        r4 = r4.invoice;
        r4 = r4.shipping_address_requested;
        if (r4 != 0) goto L_0x0963;
        r4 = r1.headerCell;
        r6 = 0;
        r4 = r4[r6];
        r7 = 8;
        r4.setVisibility(r7);
        r4 = r1.sectionCell;
        r4 = r4[r6];
        r4.setVisibility(r7);
        r4 = r1.inputFields;
        r4 = r4[r6];
        r4 = r4.getParent();
        r4 = (android.view.ViewGroup) r4;
        r4.setVisibility(r7);
        r4 = r1.inputFields;
        r6 = 1;
        r4 = r4[r6];
        r4 = r4.getParent();
        r4 = (android.view.ViewGroup) r4;
        r4.setVisibility(r7);
        r4 = r1.inputFields;
        r6 = 2;
        r4 = r4[r6];
        r4 = r4.getParent();
        r4 = (android.view.ViewGroup) r4;
        r4.setVisibility(r7);
        r4 = r1.inputFields;
        r6 = 3;
        r4 = r4[r6];
        r4 = r4.getParent();
        r4 = (android.view.ViewGroup) r4;
        r4.setVisibility(r7);
        r4 = r1.inputFields;
        r6 = 4;
        r4 = r4[r6];
        r4 = r4.getParent();
        r4 = (android.view.ViewGroup) r4;
        r4.setVisibility(r7);
        r4 = r1.inputFields;
        r6 = 5;
        r4 = r4[r6];
        r4 = r4.getParent();
        r4 = (android.view.ViewGroup) r4;
        r4.setVisibility(r7);
        r4 = r1.paymentForm;
        r4 = r4.saved_info;
        if (r4 == 0) goto L_0x097f;
        r4 = r1.paymentForm;
        r4 = r4.saved_info;
        r4 = r4.phone;
        r4 = android.text.TextUtils.isEmpty(r4);
        if (r4 != 0) goto L_0x097f;
        r4 = r1.paymentForm;
        r4 = r4.saved_info;
        r4 = r4.phone;
        r1.fillNumber(r4);
        goto L_0x0983;
        r4 = 0;
        r1.fillNumber(r4);
        r4 = r1.inputFields;
        r6 = 8;
        r4 = r4[r6];
        r4 = r4.length();
        if (r4 != 0) goto L_0x09e9;
        r4 = r1.paymentForm;
        r4 = r4.invoice;
        r4 = r4.phone_requested;
        if (r4 == 0) goto L_0x09e9;
        r4 = r1.paymentForm;
        r4 = r4.saved_info;
        if (r4 == 0) goto L_0x09a9;
        r4 = r1.paymentForm;
        r4 = r4.saved_info;
        r4 = r4.phone;
        r4 = android.text.TextUtils.isEmpty(r4);
        if (r4 == 0) goto L_0x09e9;
        r4 = 0;
        r6 = org.telegram.messenger.ApplicationLoader.applicationContext;	 Catch:{ Exception -> 0x09c0 }
        r7 = "phone";	 Catch:{ Exception -> 0x09c0 }
        r6 = r6.getSystemService(r7);	 Catch:{ Exception -> 0x09c0 }
        r6 = (android.telephony.TelephonyManager) r6;	 Catch:{ Exception -> 0x09c0 }
        if (r6 == 0) goto L_0x09bf;	 Catch:{ Exception -> 0x09c0 }
        r7 = r6.getSimCountryIso();	 Catch:{ Exception -> 0x09c0 }
        r7 = r7.toUpperCase();	 Catch:{ Exception -> 0x09c0 }
        r4 = r7;
        goto L_0x09c5;
    L_0x09c0:
        r0 = move-exception;
        r6 = r0;
        org.telegram.messenger.FileLog.e(r6);
        if (r4 == 0) goto L_0x09e9;
        r6 = r5.get(r4);
        r6 = (java.lang.String) r6;
        if (r6 == 0) goto L_0x09e9;
        r7 = r1.countriesArray;
        r7 = r7.indexOf(r6);
        r8 = -1;
        if (r7 == r8) goto L_0x09e9;
        r8 = r1.inputFields;
        r9 = 8;
        r8 = r8[r9];
        r9 = r1.countriesMap;
        r9 = r9.get(r6);
        r9 = (java.lang.CharSequence) r9;
        r8.setText(r9);
        goto L_0x196e;
    L_0x09eb:
        r4 = r1.currentStep;
        r5 = 2;
        if (r4 != r5) goto L_0x0f57;
        r4 = r1.paymentForm;
        r4 = r4.native_params;
        if (r4 == 0) goto L_0x0a3a;
        r4 = new org.json.JSONObject;	 Catch:{ Exception -> 0x0a35 }
        r5 = r1.paymentForm;	 Catch:{ Exception -> 0x0a35 }
        r5 = r5.native_params;	 Catch:{ Exception -> 0x0a35 }
        r5 = r5.data;	 Catch:{ Exception -> 0x0a35 }
        r4.<init>(r5);	 Catch:{ Exception -> 0x0a35 }
        r5 = "android_pay_public_key";	 Catch:{ Exception -> 0x0a10 }
        r5 = r4.getString(r5);	 Catch:{ Exception -> 0x0a10 }
        r6 = android.text.TextUtils.isEmpty(r5);	 Catch:{ Exception -> 0x0a10 }
        if (r6 != 0) goto L_0x0a0f;	 Catch:{ Exception -> 0x0a10 }
        r1.androidPayPublicKey = r5;	 Catch:{ Exception -> 0x0a10 }
        goto L_0x0a15;
    L_0x0a10:
        r0 = move-exception;
        r5 = r0;
        r6 = 0;
        r1.androidPayPublicKey = r6;	 Catch:{ Exception -> 0x0a35 }
        r5 = "android_pay_bgcolor";	 Catch:{ Exception -> 0x0a21 }
        r5 = r4.getInt(r5);	 Catch:{ Exception -> 0x0a21 }
        r6 = -16777216; // 0xffffffffff000000 float:-1.7014118E38 double:NaN;	 Catch:{ Exception -> 0x0a21 }
        r5 = r5 | r6;	 Catch:{ Exception -> 0x0a21 }
        r1.androidPayBackgroundColor = r5;	 Catch:{ Exception -> 0x0a21 }
        goto L_0x0a26;
    L_0x0a21:
        r0 = move-exception;
        r5 = r0;
        r6 = -1;
        r1.androidPayBackgroundColor = r6;	 Catch:{ Exception -> 0x0a35 }
        r5 = "android_pay_inverse";	 Catch:{ Exception -> 0x0a2f }
        r5 = r4.getBoolean(r5);	 Catch:{ Exception -> 0x0a2f }
        r1.androidPayBlackTheme = r5;	 Catch:{ Exception -> 0x0a2f }
        goto L_0x0a34;
    L_0x0a2f:
        r0 = move-exception;
        r5 = r0;
        r6 = 0;
        r1.androidPayBlackTheme = r6;	 Catch:{ Exception -> 0x0a35 }
        goto L_0x0a3a;
    L_0x0a35:
        r0 = move-exception;
        r4 = r0;
        org.telegram.messenger.FileLog.e(r4);
        r4 = r1.isWebView;
        if (r4 == 0) goto L_0x0b64;
        r4 = r1.androidPayPublicKey;
        if (r4 == 0) goto L_0x0a45;
        r28.initAndroidPay(r29);
        r4 = new android.widget.FrameLayout;
        r4.<init>(r2);
        r1.androidPayContainer = r4;
        r4 = r1.androidPayContainer;
        r5 = 4000; // 0xfa0 float:5.605E-42 double:1.9763E-320;
        r4.setId(r5);
        r4 = r1.androidPayContainer;
        r5 = 1;
        r6 = org.telegram.ui.ActionBar.Theme.getSelectorDrawable(r5);
        r4.setBackgroundDrawable(r6);
        r4 = r1.androidPayContainer;
        r6 = 8;
        r4.setVisibility(r6);
        r4 = r1.linearLayout2;
        r6 = r1.androidPayContainer;
        r7 = -1;
        r8 = 48;
        r8 = org.telegram.ui.Components.LayoutHelper.createLinear(r7, r8);
        r4.addView(r6, r8);
        r1.webviewLoading = r5;
        r1.showEditDoneProgress(r5, r5);
        r4 = r1.progressView;
        r5 = 0;
        r4.setVisibility(r5);
        r4 = r1.doneItem;
        r4.setEnabled(r5);
        r4 = r1.doneItem;
        r4 = r4.getImageView();
        r5 = 4;
        r4.setVisibility(r5);
        r4 = new org.telegram.ui.PaymentFormActivity$8;
        r4.<init>(r2);
        r1.webView = r4;
        r4 = r1.webView;
        r4 = r4.getSettings();
        r5 = 1;
        r4.setJavaScriptEnabled(r5);
        r4 = r1.webView;
        r4 = r4.getSettings();
        r4.setDomStorageEnabled(r5);
        r4 = android.os.Build.VERSION.SDK_INT;
        r5 = 21;
        if (r4 < r5) goto L_0x0ac0;
        r4 = r1.webView;
        r4 = r4.getSettings();
        r5 = 0;
        r4.setMixedContentMode(r5);
        r4 = android.webkit.CookieManager.getInstance();
        r5 = r1.webView;
        r6 = 1;
        r4.setAcceptThirdPartyCookies(r5, r6);
        r4 = r1.webView;
        r5 = new org.telegram.ui.PaymentFormActivity$TelegramWebviewProxy;
        r6 = 0;
        r5.<init>();
        r6 = "TelegramWebviewProxy";
        r4.addJavascriptInterface(r5, r6);
        r4 = r1.webView;
        r5 = new org.telegram.ui.PaymentFormActivity$9;
        r5.<init>();
        r4.setWebViewClient(r5);
        r4 = r1.linearLayout2;
        r5 = r1.webView;
        r6 = -1073741824; // 0xffffffffc0000000 float:-2.0 double:NaN;
        r7 = -1;
        r6 = org.telegram.ui.Components.LayoutHelper.createFrame(r7, r6);
        r4.addView(r5, r6);
        r4 = r1.sectionCell;
        r5 = new org.telegram.ui.Cells.ShadowSectionCell;
        r5.<init>(r2);
        r6 = 2;
        r4[r6] = r5;
        r4 = r1.linearLayout2;
        r5 = r1.sectionCell;
        r5 = r5[r6];
        r6 = -2;
        r8 = org.telegram.ui.Components.LayoutHelper.createLinear(r7, r6);
        r4.addView(r5, r8);
        r4 = new org.telegram.ui.Cells.TextCheckCell;
        r4.<init>(r2);
        r1.checkCell1 = r4;
        r4 = r1.checkCell1;
        r5 = 1;
        r5 = org.telegram.ui.ActionBar.Theme.getSelectorDrawable(r5);
        r4.setBackgroundDrawable(r5);
        r4 = r1.checkCell1;
        r5 = "PaymentCardSavePaymentInformation";
        r6 = 2131494085; // 0x7f0c04c5 float:1.8611668E38 double:1.0530980017E-314;
        r5 = org.telegram.messenger.LocaleController.getString(r5, r6);
        r6 = r1.saveCardInfo;
        r7 = 0;
        r4.setTextAndCheck(r5, r6, r7);
        r4 = r1.linearLayout2;
        r5 = r1.checkCell1;
        r6 = -1;
        r7 = -2;
        r8 = org.telegram.ui.Components.LayoutHelper.createLinear(r6, r7);
        r4.addView(r5, r8);
        r4 = r1.checkCell1;
        r5 = new org.telegram.ui.PaymentFormActivity$10;
        r5.<init>();
        r4.setOnClickListener(r5);
        r4 = r1.bottomCell;
        r5 = new org.telegram.ui.Cells.TextInfoPrivacyCell;
        r5.<init>(r2);
        r6 = 0;
        r4[r6] = r5;
        r4 = r1.bottomCell;
        r4 = r4[r6];
        r5 = "windowBackgroundGrayShadow";
        r7 = 2131165332; // 0x7f070094 float:1.7944878E38 double:1.052935576E-314;
        r5 = org.telegram.ui.ActionBar.Theme.getThemedDrawable(r2, r7, r5);
        r4.setBackgroundDrawable(r5);
        r28.updateSavePaymentField();
        r4 = r1.linearLayout2;
        r5 = r1.bottomCell;
        r5 = r5[r6];
        r6 = -1;
        r7 = -2;
        r6 = org.telegram.ui.Components.LayoutHelper.createLinear(r6, r7);
        r4.addView(r5, r6);
        goto L_0x196e;
        r4 = r1.paymentForm;
        r4 = r4.native_params;
        if (r4 == 0) goto L_0x0bb4;
        r4 = new org.json.JSONObject;	 Catch:{ Exception -> 0x0baf }
        r5 = r1.paymentForm;	 Catch:{ Exception -> 0x0baf }
        r5 = r5.native_params;	 Catch:{ Exception -> 0x0baf }
        r5 = r5.data;	 Catch:{ Exception -> 0x0baf }
        r4.<init>(r5);	 Catch:{ Exception -> 0x0baf }
        r5 = "need_country";	 Catch:{ Exception -> 0x0b7e }
        r5 = r4.getBoolean(r5);	 Catch:{ Exception -> 0x0b7e }
        r1.need_card_country = r5;	 Catch:{ Exception -> 0x0b7e }
        goto L_0x0b83;
    L_0x0b7e:
        r0 = move-exception;
        r5 = r0;
        r6 = 0;
        r1.need_card_country = r6;	 Catch:{ Exception -> 0x0baf }
        r5 = "need_zip";	 Catch:{ Exception -> 0x0b8c }
        r5 = r4.getBoolean(r5);	 Catch:{ Exception -> 0x0b8c }
        r1.need_card_postcode = r5;	 Catch:{ Exception -> 0x0b8c }
        goto L_0x0b91;
    L_0x0b8c:
        r0 = move-exception;
        r5 = r0;
        r6 = 0;
        r1.need_card_postcode = r6;	 Catch:{ Exception -> 0x0baf }
        r5 = "need_cardholder_name";	 Catch:{ Exception -> 0x0b9a }
        r5 = r4.getBoolean(r5);	 Catch:{ Exception -> 0x0b9a }
        r1.need_card_name = r5;	 Catch:{ Exception -> 0x0b9a }
        goto L_0x0b9f;
    L_0x0b9a:
        r0 = move-exception;
        r5 = r0;
        r6 = 0;
        r1.need_card_name = r6;	 Catch:{ Exception -> 0x0baf }
        r5 = "publishable_key";	 Catch:{ Exception -> 0x0ba8 }
        r5 = r4.getString(r5);	 Catch:{ Exception -> 0x0ba8 }
        r1.stripeApiKey = r5;	 Catch:{ Exception -> 0x0ba8 }
        goto L_0x0bae;
    L_0x0ba8:
        r0 = move-exception;
        r5 = r0;
        r6 = "";	 Catch:{ Exception -> 0x0baf }
        r1.stripeApiKey = r6;	 Catch:{ Exception -> 0x0baf }
        goto L_0x0bb4;
    L_0x0baf:
        r0 = move-exception;
        r4 = r0;
        org.telegram.messenger.FileLog.e(r4);
        r28.initAndroidPay(r29);
        r4 = 6;
        r5 = new org.telegram.ui.Components.EditTextBoldCursor[r4];
        r1.inputFields = r5;
        r5 = 0;
        if (r5 >= r4) goto L_0x0f1f;
        if (r5 != 0) goto L_0x0bf8;
        r4 = r1.headerCell;
        r6 = new org.telegram.ui.Cells.HeaderCell;
        r6.<init>(r2);
        r7 = 0;
        r4[r7] = r6;
        r4 = r1.headerCell;
        r4 = r4[r7];
        r6 = "windowBackgroundWhite";
        r6 = org.telegram.ui.ActionBar.Theme.getColor(r6);
        r4.setBackgroundColor(r6);
        r4 = r1.headerCell;
        r4 = r4[r7];
        r6 = "PaymentCardTitle";
        r8 = 2131494088; // 0x7f0c04c8 float:1.8611675E38 double:1.053098003E-314;
        r6 = org.telegram.messenger.LocaleController.getString(r6, r8);
        r4.setText(r6);
        r4 = r1.linearLayout2;
        r6 = r1.headerCell;
        r6 = r6[r7];
        r7 = -1;
        r8 = -2;
        r9 = org.telegram.ui.Components.LayoutHelper.createLinear(r7, r8);
        r4.addView(r6, r9);
        goto L_0x0c31;
        r4 = 4;
        if (r5 != r4) goto L_0x0c31;
        r4 = r1.headerCell;
        r6 = new org.telegram.ui.Cells.HeaderCell;
        r6.<init>(r2);
        r7 = 1;
        r4[r7] = r6;
        r4 = r1.headerCell;
        r4 = r4[r7];
        r6 = "windowBackgroundWhite";
        r6 = org.telegram.ui.ActionBar.Theme.getColor(r6);
        r4.setBackgroundColor(r6);
        r4 = r1.headerCell;
        r4 = r4[r7];
        r6 = "PaymentBillingAddress";
        r8 = 2131494079; // 0x7f0c04bf float:1.8611656E38 double:1.0530979987E-314;
        r6 = org.telegram.messenger.LocaleController.getString(r6, r8);
        r4.setText(r6);
        r4 = r1.linearLayout2;
        r6 = r1.headerCell;
        r6 = r6[r7];
        r7 = -1;
        r8 = -2;
        r9 = org.telegram.ui.Components.LayoutHelper.createLinear(r7, r8);
        r4.addView(r6, r9);
        r4 = 3;
        if (r5 == r4) goto L_0x0c40;
        r4 = 5;
        if (r5 == r4) goto L_0x0c40;
        r4 = 4;
        if (r5 != r4) goto L_0x0c3e;
        r4 = r1.need_card_postcode;
        if (r4 == 0) goto L_0x0c40;
        r4 = 1;
        goto L_0x0c41;
        r4 = 0;
        r6 = new android.widget.FrameLayout;
        r6.<init>(r2);
        r7 = r1.linearLayout2;
        r8 = -1;
        r9 = 48;
        r11 = org.telegram.ui.Components.LayoutHelper.createLinear(r8, r9);
        r7.addView(r6, r11);
        r7 = "windowBackgroundWhite";
        r7 = org.telegram.ui.ActionBar.Theme.getColor(r7);
        r6.setBackgroundColor(r7);
        r7 = 0;
        r8 = r1.inputFields;
        r9 = new org.telegram.ui.Components.EditTextBoldCursor;
        r9.<init>(r2);
        r8[r5] = r9;
        r8 = r1.inputFields;
        r8 = r8[r5];
        r9 = java.lang.Integer.valueOf(r5);
        r8.setTag(r9);
        r8 = r1.inputFields;
        r8 = r8[r5];
        r9 = 1098907648; // 0x41800000 float:16.0 double:5.42932517E-315;
        r11 = 1;
        r8.setTextSize(r11, r9);
        r8 = r1.inputFields;
        r8 = r8[r5];
        r9 = "windowBackgroundWhiteHintText";
        r9 = org.telegram.ui.ActionBar.Theme.getColor(r9);
        r8.setHintTextColor(r9);
        r8 = r1.inputFields;
        r8 = r8[r5];
        r9 = "windowBackgroundWhiteBlackText";
        r9 = org.telegram.ui.ActionBar.Theme.getColor(r9);
        r8.setTextColor(r9);
        r8 = r1.inputFields;
        r8 = r8[r5];
        r9 = 0;
        r8.setBackgroundDrawable(r9);
        r8 = r1.inputFields;
        r8 = r8[r5];
        r9 = "windowBackgroundWhiteBlackText";
        r9 = org.telegram.ui.ActionBar.Theme.getColor(r9);
        r8.setCursorColor(r9);
        r8 = r1.inputFields;
        r8 = r8[r5];
        r9 = 1101004800; // 0x41a00000 float:20.0 double:5.439686476E-315;
        r9 = org.telegram.messenger.AndroidUtilities.dp(r9);
        r8.setCursorSize(r9);
        r8 = r1.inputFields;
        r8 = r8[r5];
        r9 = 1069547520; // 0x3fc00000 float:1.5 double:5.28426686E-315;
        r8.setCursorWidth(r9);
        r8 = 3;
        if (r5 != r8) goto L_0x0cf2;
        r9 = 1;
        r11 = new android.text.InputFilter[r9];
        r9 = new android.text.InputFilter$LengthFilter;
        r9.<init>(r8);
        r8 = 0;
        r11[r8] = r9;
        r8 = r1.inputFields;
        r8 = r8[r5];
        r8.setFilters(r11);
        r8 = r1.inputFields;
        r8 = r8[r5];
        r9 = 130; // 0x82 float:1.82E-43 double:6.4E-322;
        r8.setInputType(r9);
        r8 = r1.inputFields;
        r8 = r8[r5];
        r9 = android.graphics.Typeface.DEFAULT;
        r8.setTypeface(r9);
        r8 = r1.inputFields;
        r8 = r8[r5];
        r9 = android.text.method.PasswordTransformationMethod.getInstance();
        r8.setTransformationMethod(r9);
        goto L_0x0d38;
        if (r5 != 0) goto L_0x0cfd;
        r8 = r1.inputFields;
        r8 = r8[r5];
        r9 = 2;
        r8.setInputType(r9);
        goto L_0x0d38;
        r8 = 4;
        if (r5 != r8) goto L_0x0d15;
        r8 = r1.inputFields;
        r8 = r8[r5];
        r9 = new org.telegram.ui.PaymentFormActivity$11;
        r9.<init>();
        r8.setOnTouchListener(r9);
        r8 = r1.inputFields;
        r8 = r8[r5];
        r9 = 0;
        r8.setInputType(r9);
        goto L_0x0d38;
        r8 = 1;
        if (r5 != r8) goto L_0x0d22;
        r8 = r1.inputFields;
        r8 = r8[r5];
        r9 = 16386; // 0x4002 float:2.2962E-41 double:8.096E-320;
        r8.setInputType(r9);
        goto L_0x0d38;
        r8 = 2;
        if (r5 != r8) goto L_0x0d2f;
        r8 = r1.inputFields;
        r8 = r8[r5];
        r9 = 4097; // 0x1001 float:5.741E-42 double:2.024E-320;
        r8.setInputType(r9);
        goto L_0x0d38;
        r8 = r1.inputFields;
        r8 = r8[r5];
        r9 = 16385; // 0x4001 float:2.296E-41 double:8.0953E-320;
        r8.setInputType(r9);
        r8 = r1.inputFields;
        r8 = r8[r5];
        r9 = 268435461; // 0x10000005 float:2.5243564E-29 double:1.326247394E-315;
        r8.setImeOptions(r9);
        switch(r5) {
            case 0: goto L_0x0d9b;
            case 1: goto L_0x0d8a;
            case 2: goto L_0x0d79;
            case 3: goto L_0x0d68;
            case 4: goto L_0x0d57;
            case 5: goto L_0x0d46;
            default: goto L_0x0d45;
        };
        goto L_0x0dac;
        r8 = r1.inputFields;
        r8 = r8[r5];
        r9 = "PaymentShippingZipPlaceholder";
        r11 = 2131494130; // 0x7f0c04f2 float:1.861176E38 double:1.053098024E-314;
        r9 = org.telegram.messenger.LocaleController.getString(r9, r11);
        r8.setHint(r9);
        goto L_0x0dac;
        r8 = r1.inputFields;
        r8 = r8[r5];
        r9 = "PaymentShippingCountry";
        r11 = 2131494120; // 0x7f0c04e8 float:1.861174E38 double:1.053098019E-314;
        r9 = org.telegram.messenger.LocaleController.getString(r9, r11);
        r8.setHint(r9);
        goto L_0x0dac;
        r8 = r1.inputFields;
        r8 = r8[r5];
        r9 = "PaymentCardCvv";
        r11 = 2131494080; // 0x7f0c04c0 float:1.8611658E38 double:1.053097999E-314;
        r9 = org.telegram.messenger.LocaleController.getString(r9, r11);
        r8.setHint(r9);
        goto L_0x0dac;
        r8 = r1.inputFields;
        r8 = r8[r5];
        r9 = "PaymentCardName";
        r11 = 2131494083; // 0x7f0c04c3 float:1.8611664E38 double:1.0530980007E-314;
        r9 = org.telegram.messenger.LocaleController.getString(r9, r11);
        r8.setHint(r9);
        goto L_0x0dac;
        r8 = r1.inputFields;
        r8 = r8[r5];
        r9 = "PaymentCardExpireDate";
        r11 = 2131494081; // 0x7f0c04c1 float:1.861166E38 double:1.0530979997E-314;
        r9 = org.telegram.messenger.LocaleController.getString(r9, r11);
        r8.setHint(r9);
        goto L_0x0dac;
        r8 = r1.inputFields;
        r8 = r8[r5];
        r9 = "PaymentCardNumber";
        r11 = 2131494084; // 0x7f0c04c4 float:1.8611666E38 double:1.053098001E-314;
        r9 = org.telegram.messenger.LocaleController.getString(r9, r11);
        r8.setHint(r9);
        if (r5 != 0) goto L_0x0dbb;
        r8 = r1.inputFields;
        r8 = r8[r5];
        r9 = new org.telegram.ui.PaymentFormActivity$12;
        r9.<init>();
        r8.addTextChangedListener(r9);
        goto L_0x0dca;
        r8 = 1;
        if (r5 != r8) goto L_0x0dca;
        r8 = r1.inputFields;
        r8 = r8[r5];
        r9 = new org.telegram.ui.PaymentFormActivity$13;
        r9.<init>();
        r8.addTextChangedListener(r9);
        r8 = r1.inputFields;
        r8 = r8[r5];
        r9 = 1086324736; // 0x40c00000 float:6.0 double:5.367157323E-315;
        r9 = org.telegram.messenger.AndroidUtilities.dp(r9);
        r11 = 0;
        r8.setPadding(r11, r11, r11, r9);
        r8 = r1.inputFields;
        r8 = r8[r5];
        r9 = org.telegram.messenger.LocaleController.isRTL;
        if (r9 == 0) goto L_0x0de2;
        r9 = 5;
        goto L_0x0de3;
        r9 = 3;
        r8.setGravity(r9);
        r8 = r1.inputFields;
        r8 = r8[r5];
        r11 = -1;
        r12 = -1073741824; // 0xffffffffc0000000 float:-2.0 double:NaN;
        r13 = 51;
        r14 = 1099431936; // 0x41880000 float:17.0 double:5.431915495E-315;
        r15 = 1094713344; // 0x41400000 float:12.0 double:5.408602553E-315;
        r16 = 1099431936; // 0x41880000 float:17.0 double:5.431915495E-315;
        r17 = 1086324736; // 0x40c00000 float:6.0 double:5.367157323E-315;
        r9 = org.telegram.ui.Components.LayoutHelper.createFrame(r11, r12, r13, r14, r15, r16, r17);
        r6.addView(r8, r9);
        r8 = r1.inputFields;
        r8 = r8[r5];
        r9 = new org.telegram.ui.PaymentFormActivity$14;
        r9.<init>();
        r8.setOnEditorActionListener(r9);
        r8 = 3;
        if (r5 != r8) goto L_0x0e28;
        r8 = r1.sectionCell;
        r9 = new org.telegram.ui.Cells.ShadowSectionCell;
        r9.<init>(r2);
        r11 = 0;
        r8[r11] = r9;
        r8 = r1.linearLayout2;
        r9 = r1.sectionCell;
        r9 = r9[r11];
        r11 = -1;
        r12 = -2;
        r13 = org.telegram.ui.Components.LayoutHelper.createLinear(r11, r12);
        r8.addView(r9, r13);
        goto L_0x0edf;
        r8 = 5;
        if (r5 != r8) goto L_0x0eaa;
        r8 = r1.sectionCell;
        r9 = new org.telegram.ui.Cells.ShadowSectionCell;
        r9.<init>(r2);
        r11 = 2;
        r8[r11] = r9;
        r8 = r1.linearLayout2;
        r9 = r1.sectionCell;
        r9 = r9[r11];
        r11 = -1;
        r12 = -2;
        r13 = org.telegram.ui.Components.LayoutHelper.createLinear(r11, r12);
        r8.addView(r9, r13);
        r8 = new org.telegram.ui.Cells.TextCheckCell;
        r8.<init>(r2);
        r1.checkCell1 = r8;
        r8 = r1.checkCell1;
        r9 = 1;
        r11 = org.telegram.ui.ActionBar.Theme.getSelectorDrawable(r9);
        r8.setBackgroundDrawable(r11);
        r8 = r1.checkCell1;
        r9 = "PaymentCardSavePaymentInformation";
        r11 = 2131494085; // 0x7f0c04c5 float:1.8611668E38 double:1.0530980017E-314;
        r9 = org.telegram.messenger.LocaleController.getString(r9, r11);
        r11 = r1.saveCardInfo;
        r12 = 0;
        r8.setTextAndCheck(r9, r11, r12);
        r8 = r1.linearLayout2;
        r9 = r1.checkCell1;
        r11 = -1;
        r12 = -2;
        r13 = org.telegram.ui.Components.LayoutHelper.createLinear(r11, r12);
        r8.addView(r9, r13);
        r8 = r1.checkCell1;
        r9 = new org.telegram.ui.PaymentFormActivity$15;
        r9.<init>();
        r8.setOnClickListener(r9);
        r8 = r1.bottomCell;
        r9 = new org.telegram.ui.Cells.TextInfoPrivacyCell;
        r9.<init>(r2);
        r11 = 0;
        r8[r11] = r9;
        r8 = r1.bottomCell;
        r8 = r8[r11];
        r9 = "windowBackgroundGrayShadow";
        r12 = 2131165332; // 0x7f070094 float:1.7944878E38 double:1.052935576E-314;
        r9 = org.telegram.ui.ActionBar.Theme.getThemedDrawable(r2, r12, r9);
        r8.setBackgroundDrawable(r9);
        r28.updateSavePaymentField();
        r8 = r1.linearLayout2;
        r9 = r1.bottomCell;
        r9 = r9[r11];
        r11 = -1;
        r12 = -2;
        r13 = org.telegram.ui.Components.LayoutHelper.createLinear(r11, r12);
        r8.addView(r9, r13);
        goto L_0x0edf;
        if (r5 != 0) goto L_0x0edf;
        r8 = new android.widget.FrameLayout;
        r8.<init>(r2);
        r1.androidPayContainer = r8;
        r8 = r1.androidPayContainer;
        r9 = 4000; // 0xfa0 float:5.605E-42 double:1.9763E-320;
        r8.setId(r9);
        r8 = r1.androidPayContainer;
        r9 = 1;
        r11 = org.telegram.ui.ActionBar.Theme.getSelectorDrawable(r9);
        r8.setBackgroundDrawable(r11);
        r8 = r1.androidPayContainer;
        r9 = 8;
        r8.setVisibility(r9);
        r8 = r1.androidPayContainer;
        r11 = -2;
        r12 = -1073741824; // 0xffffffffc0000000 float:-2.0 double:NaN;
        r13 = 21;
        r14 = 0;
        r15 = 0;
        r16 = 1082130432; // 0x40800000 float:4.0 double:5.34643471E-315;
        r17 = 0;
        r9 = org.telegram.ui.Components.LayoutHelper.createFrame(r11, r12, r13, r14, r15, r16, r17);
        r6.addView(r8, r9);
        if (r4 == 0) goto L_0x0f00;
        r8 = new android.view.View;
        r8.<init>(r2);
        r9 = r1.dividers;
        r9.add(r8);
        r9 = "divider";
        r9 = org.telegram.ui.ActionBar.Theme.getColor(r9);
        r8.setBackgroundColor(r9);
        r9 = new android.widget.FrameLayout$LayoutParams;
        r11 = 83;
        r12 = 1;
        r13 = -1;
        r9.<init>(r13, r12, r11);
        r6.addView(r8, r9);
        r8 = 4;
        if (r5 != r8) goto L_0x0f07;
        r8 = r1.need_card_country;
        if (r8 == 0) goto L_0x0f15;
        r8 = 5;
        if (r5 != r8) goto L_0x0f0e;
        r8 = r1.need_card_postcode;
        if (r8 == 0) goto L_0x0f15;
        r8 = 2;
        if (r5 != r8) goto L_0x0f1a;
        r8 = r1.need_card_name;
        if (r8 != 0) goto L_0x0f1a;
        r8 = 8;
        r6.setVisibility(r8);
        r5 = r5 + 1;
        r4 = 6;
        goto L_0x0bbd;
        r4 = r1.need_card_country;
        if (r4 != 0) goto L_0x0f39;
        r4 = r1.need_card_postcode;
        if (r4 != 0) goto L_0x0f39;
        r4 = r1.headerCell;
        r5 = 1;
        r4 = r4[r5];
        r5 = 8;
        r4.setVisibility(r5);
        r4 = r1.sectionCell;
        r6 = 0;
        r4 = r4[r6];
        r4.setVisibility(r5);
        r4 = r1.need_card_postcode;
        if (r4 == 0) goto L_0x0f4a;
        r4 = r1.inputFields;
        r5 = 5;
        r4 = r4[r5];
        r5 = 268435462; // 0x10000006 float:2.5243567E-29 double:1.3262474E-315;
        r4.setImeOptions(r5);
        goto L_0x196e;
        r5 = 268435462; // 0x10000006 float:2.5243567E-29 double:1.3262474E-315;
        r4 = r1.inputFields;
        r6 = 3;
        r4 = r4[r6];
        r4.setImeOptions(r5);
        goto L_0x196e;
        r4 = r1.currentStep;
        r5 = 1;
        if (r4 != r5) goto L_0x1001;
        r4 = r1.requestedInfo;
        r4 = r4.shipping_options;
        r4 = r4.size();
        r5 = new org.telegram.ui.Cells.RadioCell[r4];
        r1.radioCells = r5;
        r5 = 0;
        if (r5 >= r4) goto L_0x0fd6;
        r6 = r1.requestedInfo;
        r6 = r6.shipping_options;
        r6 = r6.get(r5);
        r6 = (org.telegram.tgnet.TLRPC.TL_shippingOption) r6;
        r7 = r1.radioCells;
        r8 = new org.telegram.ui.Cells.RadioCell;
        r8.<init>(r2);
        r7[r5] = r8;
        r7 = r1.radioCells;
        r7 = r7[r5];
        r8 = java.lang.Integer.valueOf(r5);
        r7.setTag(r8);
        r7 = r1.radioCells;
        r7 = r7[r5];
        r8 = 1;
        r9 = org.telegram.ui.ActionBar.Theme.getSelectorDrawable(r8);
        r7.setBackgroundDrawable(r9);
        r7 = r1.radioCells;
        r7 = r7[r5];
        r9 = "%s - %s";
        r11 = 2;
        r12 = new java.lang.Object[r11];
        r11 = r6.prices;
        r11 = r1.getTotalPriceString(r11);
        r13 = 0;
        r12[r13] = r11;
        r11 = r6.title;
        r12[r8] = r11;
        r8 = java.lang.String.format(r9, r12);
        if (r5 != 0) goto L_0x0fb3;
        r9 = 1;
        goto L_0x0fb4;
        r9 = 0;
        r11 = r4 + -1;
        if (r5 == r11) goto L_0x0fba;
        r11 = 1;
        goto L_0x0fbb;
        r11 = 0;
        r7.setText(r8, r9, r11);
        r7 = r1.radioCells;
        r7 = r7[r5];
        r8 = new org.telegram.ui.PaymentFormActivity$16;
        r8.<init>();
        r7.setOnClickListener(r8);
        r7 = r1.linearLayout2;
        r8 = r1.radioCells;
        r8 = r8[r5];
        r7.addView(r8);
        r5 = r5 + 1;
        goto L_0x0f69;
        r5 = r1.bottomCell;
        r6 = new org.telegram.ui.Cells.TextInfoPrivacyCell;
        r6.<init>(r2);
        r7 = 0;
        r5[r7] = r6;
        r5 = r1.bottomCell;
        r5 = r5[r7];
        r6 = "windowBackgroundGrayShadow";
        r8 = 2131165332; // 0x7f070094 float:1.7944878E38 double:1.052935576E-314;
        r6 = org.telegram.ui.ActionBar.Theme.getThemedDrawable(r2, r8, r6);
        r5.setBackgroundDrawable(r6);
        r5 = r1.linearLayout2;
        r6 = r1.bottomCell;
        r6 = r6[r7];
        r7 = -1;
        r8 = -2;
        r7 = org.telegram.ui.Components.LayoutHelper.createLinear(r7, r8);
        r5.addView(r6, r7);
        goto L_0x196e;
        r4 = r1.currentStep;
        r5 = 3;
        if (r4 != r5) goto L_0x1256;
        r4 = 2;
        r5 = new org.telegram.ui.Components.EditTextBoldCursor[r4];
        r1.inputFields = r5;
        r5 = 0;
        if (r5 >= r4) goto L_0x196e;
        if (r5 != 0) goto L_0x1047;
        r4 = r1.headerCell;
        r6 = new org.telegram.ui.Cells.HeaderCell;
        r6.<init>(r2);
        r7 = 0;
        r4[r7] = r6;
        r4 = r1.headerCell;
        r4 = r4[r7];
        r6 = "windowBackgroundWhite";
        r6 = org.telegram.ui.ActionBar.Theme.getColor(r6);
        r4.setBackgroundColor(r6);
        r4 = r1.headerCell;
        r4 = r4[r7];
        r6 = "PaymentCardTitle";
        r8 = 2131494088; // 0x7f0c04c8 float:1.8611675E38 double:1.053098003E-314;
        r6 = org.telegram.messenger.LocaleController.getString(r6, r8);
        r4.setText(r6);
        r4 = r1.linearLayout2;
        r6 = r1.headerCell;
        r6 = r6[r7];
        r7 = -1;
        r8 = -2;
        r9 = org.telegram.ui.Components.LayoutHelper.createLinear(r7, r8);
        r4.addView(r6, r9);
        goto L_0x1048;
        r7 = -1;
        r4 = new android.widget.FrameLayout;
        r4.<init>(r2);
        r6 = r1.linearLayout2;
        r8 = 48;
        r9 = org.telegram.ui.Components.LayoutHelper.createLinear(r7, r8);
        r6.addView(r4, r9);
        r6 = "windowBackgroundWhite";
        r6 = org.telegram.ui.ActionBar.Theme.getColor(r6);
        r4.setBackgroundColor(r6);
        r6 = 1;
        if (r5 == r6) goto L_0x1066;
        r6 = 1;
        goto L_0x1067;
        r6 = 0;
        if (r6 == 0) goto L_0x108a;
        r7 = 7;
        if (r5 != r7) goto L_0x1076;
        r7 = r1.paymentForm;
        r7 = r7.invoice;
        r7 = r7.phone_requested;
        if (r7 != 0) goto L_0x1076;
        r6 = 0;
        goto L_0x108a;
        r7 = 6;
        if (r5 != r7) goto L_0x108a;
        r7 = r1.paymentForm;
        r7 = r7.invoice;
        r7 = r7.phone_requested;
        if (r7 != 0) goto L_0x108a;
        r7 = r1.paymentForm;
        r7 = r7.invoice;
        r7 = r7.email_requested;
        if (r7 != 0) goto L_0x108a;
        r6 = 0;
        if (r6 == 0) goto L_0x10ab;
        r7 = new android.view.View;
        r7.<init>(r2);
        r8 = r1.dividers;
        r8.add(r7);
        r8 = "divider";
        r8 = org.telegram.ui.ActionBar.Theme.getColor(r8);
        r7.setBackgroundColor(r8);
        r8 = new android.widget.FrameLayout$LayoutParams;
        r9 = 83;
        r11 = 1;
        r12 = -1;
        r8.<init>(r12, r11, r9);
        r4.addView(r7, r8);
        r7 = r1.inputFields;
        r8 = new org.telegram.ui.Components.EditTextBoldCursor;
        r8.<init>(r2);
        r7[r5] = r8;
        r7 = r1.inputFields;
        r7 = r7[r5];
        r8 = java.lang.Integer.valueOf(r5);
        r7.setTag(r8);
        r7 = r1.inputFields;
        r7 = r7[r5];
        r8 = 1098907648; // 0x41800000 float:16.0 double:5.42932517E-315;
        r9 = 1;
        r7.setTextSize(r9, r8);
        r7 = r1.inputFields;
        r7 = r7[r5];
        r8 = "windowBackgroundWhiteHintText";
        r8 = org.telegram.ui.ActionBar.Theme.getColor(r8);
        r7.setHintTextColor(r8);
        r7 = r1.inputFields;
        r7 = r7[r5];
        r8 = "windowBackgroundWhiteBlackText";
        r8 = org.telegram.ui.ActionBar.Theme.getColor(r8);
        r7.setTextColor(r8);
        r7 = r1.inputFields;
        r7 = r7[r5];
        r8 = 0;
        r7.setBackgroundDrawable(r8);
        r7 = r1.inputFields;
        r7 = r7[r5];
        r8 = "windowBackgroundWhiteBlackText";
        r8 = org.telegram.ui.ActionBar.Theme.getColor(r8);
        r7.setCursorColor(r8);
        r7 = r1.inputFields;
        r7 = r7[r5];
        r8 = 1101004800; // 0x41a00000 float:20.0 double:5.439686476E-315;
        r8 = org.telegram.messenger.AndroidUtilities.dp(r8);
        r7.setCursorSize(r8);
        r7 = r1.inputFields;
        r7 = r7[r5];
        r8 = 1069547520; // 0x3fc00000 float:1.5 double:5.28426686E-315;
        r7.setCursorWidth(r8);
        if (r5 != 0) goto L_0x1125;
        r7 = r1.inputFields;
        r7 = r7[r5];
        r8 = new org.telegram.ui.PaymentFormActivity$17;
        r8.<init>();
        r7.setOnTouchListener(r8);
        r7 = r1.inputFields;
        r7 = r7[r5];
        r8 = 0;
        r7.setInputType(r8);
        goto L_0x1137;
        r7 = r1.inputFields;
        r7 = r7[r5];
        r8 = 129; // 0x81 float:1.81E-43 double:6.37E-322;
        r7.setInputType(r8);
        r7 = r1.inputFields;
        r7 = r7[r5];
        r8 = android.graphics.Typeface.DEFAULT;
        r7.setTypeface(r8);
        r7 = r1.inputFields;
        r7 = r7[r5];
        r8 = 268435462; // 0x10000006 float:2.5243567E-29 double:1.3262474E-315;
        r7.setImeOptions(r8);
        switch(r5) {
            case 0: goto L_0x115d;
            case 1: goto L_0x1145;
            default: goto L_0x1144;
        };
        goto L_0x116b;
        r7 = r1.inputFields;
        r7 = r7[r5];
        r8 = "LoginPassword";
        r9 = 2131493777; // 0x7f0c0391 float:1.8611044E38 double:1.0530978495E-314;
        r8 = org.telegram.messenger.LocaleController.getString(r8, r9);
        r7.setHint(r8);
        r7 = r1.inputFields;
        r7 = r7[r5];
        r7.requestFocus();
        goto L_0x116b;
        r7 = r1.inputFields;
        r7 = r7[r5];
        r8 = r1.paymentForm;
        r8 = r8.saved_credentials;
        r8 = r8.title;
        r7.setText(r8);
        r7 = r1.inputFields;
        r7 = r7[r5];
        r8 = 1086324736; // 0x40c00000 float:6.0 double:5.367157323E-315;
        r8 = org.telegram.messenger.AndroidUtilities.dp(r8);
        r9 = 0;
        r7.setPadding(r9, r9, r9, r8);
        r7 = r1.inputFields;
        r7 = r7[r5];
        r8 = org.telegram.messenger.LocaleController.isRTL;
        if (r8 == 0) goto L_0x1183;
        r8 = 5;
        goto L_0x1184;
        r8 = 3;
        r7.setGravity(r8);
        r7 = r1.inputFields;
        r7 = r7[r5];
        r11 = -1;
        r12 = -1073741824; // 0xffffffffc0000000 float:-2.0 double:NaN;
        r13 = 51;
        r14 = 1099431936; // 0x41880000 float:17.0 double:5.431915495E-315;
        r15 = 1094713344; // 0x41400000 float:12.0 double:5.408602553E-315;
        r16 = 1099431936; // 0x41880000 float:17.0 double:5.431915495E-315;
        r17 = 1086324736; // 0x40c00000 float:6.0 double:5.367157323E-315;
        r8 = org.telegram.ui.Components.LayoutHelper.createFrame(r11, r12, r13, r14, r15, r16, r17);
        r4.addView(r7, r8);
        r7 = r1.inputFields;
        r7 = r7[r5];
        r8 = new org.telegram.ui.PaymentFormActivity$18;
        r8.<init>();
        r7.setOnEditorActionListener(r8);
        r7 = 1;
        if (r5 != r7) goto L_0x1251;
        r8 = r1.bottomCell;
        r9 = new org.telegram.ui.Cells.TextInfoPrivacyCell;
        r9.<init>(r2);
        r11 = 0;
        r8[r11] = r9;
        r8 = r1.bottomCell;
        r8 = r8[r11];
        r9 = "PaymentConfirmationMessage";
        r12 = 2131494097; // 0x7f0c04d1 float:1.8611693E38 double:1.0530980076E-314;
        r13 = new java.lang.Object[r7];
        r7 = r1.paymentForm;
        r7 = r7.saved_credentials;
        r7 = r7.title;
        r13[r11] = r7;
        r7 = org.telegram.messenger.LocaleController.formatString(r9, r12, r13);
        r8.setText(r7);
        r7 = r1.bottomCell;
        r7 = r7[r11];
        r8 = 2131165331; // 0x7f070093 float:1.7944876E38 double:1.0529355757E-314;
        r9 = "windowBackgroundGrayShadow";
        r8 = org.telegram.ui.ActionBar.Theme.getThemedDrawable(r2, r8, r9);
        r7.setBackgroundDrawable(r8);
        r7 = r1.linearLayout2;
        r8 = r1.bottomCell;
        r8 = r8[r11];
        r9 = -1;
        r11 = -2;
        r12 = org.telegram.ui.Components.LayoutHelper.createLinear(r9, r11);
        r7.addView(r8, r12);
        r7 = new org.telegram.ui.Cells.TextSettingsCell;
        r7.<init>(r2);
        r1.settingsCell1 = r7;
        r7 = r1.settingsCell1;
        r8 = 1;
        r9 = org.telegram.ui.ActionBar.Theme.getSelectorDrawable(r8);
        r7.setBackgroundDrawable(r9);
        r7 = r1.settingsCell1;
        r8 = "PaymentConfirmationNewCard";
        r9 = 2131494098; // 0x7f0c04d2 float:1.8611695E38 double:1.053098008E-314;
        r8 = org.telegram.messenger.LocaleController.getString(r8, r9);
        r9 = 0;
        r7.setText(r8, r9);
        r7 = r1.linearLayout2;
        r8 = r1.settingsCell1;
        r9 = -1;
        r11 = -2;
        r12 = org.telegram.ui.Components.LayoutHelper.createLinear(r9, r11);
        r7.addView(r8, r12);
        r7 = r1.settingsCell1;
        r8 = new org.telegram.ui.PaymentFormActivity$19;
        r8.<init>();
        r7.setOnClickListener(r8);
        r7 = r1.bottomCell;
        r8 = new org.telegram.ui.Cells.TextInfoPrivacyCell;
        r8.<init>(r2);
        r9 = 1;
        r7[r9] = r8;
        r7 = r1.bottomCell;
        r7 = r7[r9];
        r8 = "windowBackgroundGrayShadow";
        r11 = 2131165332; // 0x7f070094 float:1.7944878E38 double:1.052935576E-314;
        r8 = org.telegram.ui.ActionBar.Theme.getThemedDrawable(r2, r11, r8);
        r7.setBackgroundDrawable(r8);
        r7 = r1.linearLayout2;
        r8 = r1.bottomCell;
        r8 = r8[r9];
        r9 = -1;
        r11 = -2;
        r12 = org.telegram.ui.Components.LayoutHelper.createLinear(r9, r11);
        r7.addView(r8, r12);
        r5 = r5 + 1;
        r4 = 2;
        goto L_0x100c;
        r4 = r1.currentStep;
        r5 = 4;
        if (r4 == r5) goto L_0x1522;
        r4 = r1.currentStep;
        r5 = 5;
        if (r4 != r5) goto L_0x1262;
        goto L_0x1522;
        r4 = r1.currentStep;
        r5 = 6;
        if (r4 != r5) goto L_0x196e;
        r4 = r1.bottomCell;
        r5 = new org.telegram.ui.Cells.TextInfoPrivacyCell;
        r5.<init>(r2);
        r6 = 2;
        r4[r6] = r5;
        r4 = r1.bottomCell;
        r4 = r4[r6];
        r5 = 2131165331; // 0x7f070093 float:1.7944876E38 double:1.0529355757E-314;
        r7 = "windowBackgroundGrayShadow";
        r5 = org.telegram.ui.ActionBar.Theme.getThemedDrawable(r2, r5, r7);
        r4.setBackgroundDrawable(r5);
        r4 = r1.linearLayout2;
        r5 = r1.bottomCell;
        r5 = r5[r6];
        r6 = -1;
        r7 = -2;
        r8 = org.telegram.ui.Components.LayoutHelper.createLinear(r6, r7);
        r4.addView(r5, r8);
        r4 = new org.telegram.ui.Cells.TextSettingsCell;
        r4.<init>(r2);
        r1.settingsCell1 = r4;
        r4 = r1.settingsCell1;
        r5 = 1;
        r6 = org.telegram.ui.ActionBar.Theme.getSelectorDrawable(r5);
        r4.setBackgroundDrawable(r6);
        r4 = r1.settingsCell1;
        r5 = "windowBackgroundWhiteRedText3";
        r4.setTag(r5);
        r4 = r1.settingsCell1;
        r5 = "windowBackgroundWhiteRedText3";
        r5 = org.telegram.ui.ActionBar.Theme.getColor(r5);
        r4.setTextColor(r5);
        r4 = r1.settingsCell1;
        r5 = "AbortPassword";
        r6 = 2131492865; // 0x7f0c0001 float:1.8609194E38 double:1.053097399E-314;
        r5 = org.telegram.messenger.LocaleController.getString(r5, r6);
        r6 = 0;
        r4.setText(r5, r6);
        r4 = r1.linearLayout2;
        r5 = r1.settingsCell1;
        r6 = -1;
        r7 = -2;
        r8 = org.telegram.ui.Components.LayoutHelper.createLinear(r6, r7);
        r4.addView(r5, r8);
        r4 = r1.settingsCell1;
        r5 = new org.telegram.ui.PaymentFormActivity$24;
        r5.<init>();
        r4.setOnClickListener(r5);
        r4 = 3;
        r5 = new org.telegram.ui.Components.EditTextBoldCursor[r4];
        r1.inputFields = r5;
        r5 = 0;
        if (r5 >= r4) goto L_0x151d;
        if (r5 != 0) goto L_0x131b;
        r4 = r1.headerCell;
        r6 = new org.telegram.ui.Cells.HeaderCell;
        r6.<init>(r2);
        r7 = 0;
        r4[r7] = r6;
        r4 = r1.headerCell;
        r4 = r4[r7];
        r6 = "windowBackgroundWhite";
        r6 = org.telegram.ui.ActionBar.Theme.getColor(r6);
        r4.setBackgroundColor(r6);
        r4 = r1.headerCell;
        r4 = r4[r7];
        r6 = "PaymentPasswordTitle";
        r8 = 2131494111; // 0x7f0c04df float:1.8611721E38 double:1.0530980146E-314;
        r6 = org.telegram.messenger.LocaleController.getString(r6, r8);
        r4.setText(r6);
        r4 = r1.linearLayout2;
        r6 = r1.headerCell;
        r6 = r6[r7];
        r7 = -1;
        r8 = -2;
        r9 = org.telegram.ui.Components.LayoutHelper.createLinear(r7, r8);
        r4.addView(r6, r9);
        r7 = -1;
        goto L_0x1354;
        r4 = 2;
        if (r5 != r4) goto L_0x1319;
        r4 = r1.headerCell;
        r6 = new org.telegram.ui.Cells.HeaderCell;
        r6.<init>(r2);
        r7 = 1;
        r4[r7] = r6;
        r4 = r1.headerCell;
        r4 = r4[r7];
        r6 = "windowBackgroundWhite";
        r6 = org.telegram.ui.ActionBar.Theme.getColor(r6);
        r4.setBackgroundColor(r6);
        r4 = r1.headerCell;
        r4 = r4[r7];
        r6 = "PaymentPasswordEmailTitle";
        r8 = 2131494107; // 0x7f0c04db float:1.8611713E38 double:1.0530980126E-314;
        r6 = org.telegram.messenger.LocaleController.getString(r6, r8);
        r4.setText(r6);
        r4 = r1.linearLayout2;
        r6 = r1.headerCell;
        r6 = r6[r7];
        r7 = -1;
        r8 = -2;
        r9 = org.telegram.ui.Components.LayoutHelper.createLinear(r7, r8);
        r4.addView(r6, r9);
        r4 = new android.widget.FrameLayout;
        r4.<init>(r2);
        r6 = r1.linearLayout2;
        r8 = 48;
        r9 = org.telegram.ui.Components.LayoutHelper.createLinear(r7, r8);
        r6.addView(r4, r9);
        r6 = "windowBackgroundWhite";
        r6 = org.telegram.ui.ActionBar.Theme.getColor(r6);
        r4.setBackgroundColor(r6);
        if (r5 != 0) goto L_0x138e;
        r6 = new android.view.View;
        r6.<init>(r2);
        r7 = r1.dividers;
        r7.add(r6);
        r7 = "divider";
        r7 = org.telegram.ui.ActionBar.Theme.getColor(r7);
        r6.setBackgroundColor(r7);
        r7 = new android.widget.FrameLayout$LayoutParams;
        r8 = 83;
        r9 = 1;
        r11 = -1;
        r7.<init>(r11, r9, r8);
        r4.addView(r6, r7);
        r6 = r1.inputFields;
        r7 = new org.telegram.ui.Components.EditTextBoldCursor;
        r7.<init>(r2);
        r6[r5] = r7;
        r6 = r1.inputFields;
        r6 = r6[r5];
        r7 = java.lang.Integer.valueOf(r5);
        r6.setTag(r7);
        r6 = r1.inputFields;
        r6 = r6[r5];
        r7 = 1098907648; // 0x41800000 float:16.0 double:5.42932517E-315;
        r8 = 1;
        r6.setTextSize(r8, r7);
        r6 = r1.inputFields;
        r6 = r6[r5];
        r7 = "windowBackgroundWhiteHintText";
        r7 = org.telegram.ui.ActionBar.Theme.getColor(r7);
        r6.setHintTextColor(r7);
        r6 = r1.inputFields;
        r6 = r6[r5];
        r7 = "windowBackgroundWhiteBlackText";
        r7 = org.telegram.ui.ActionBar.Theme.getColor(r7);
        r6.setTextColor(r7);
        r6 = r1.inputFields;
        r6 = r6[r5];
        r7 = 0;
        r6.setBackgroundDrawable(r7);
        r6 = r1.inputFields;
        r6 = r6[r5];
        r8 = "windowBackgroundWhiteBlackText";
        r8 = org.telegram.ui.ActionBar.Theme.getColor(r8);
        r6.setCursorColor(r8);
        r6 = r1.inputFields;
        r6 = r6[r5];
        r8 = 1101004800; // 0x41a00000 float:20.0 double:5.439686476E-315;
        r8 = org.telegram.messenger.AndroidUtilities.dp(r8);
        r6.setCursorSize(r8);
        r6 = r1.inputFields;
        r6 = r6[r5];
        r8 = 1069547520; // 0x3fc00000 float:1.5 double:5.28426686E-315;
        r6.setCursorWidth(r8);
        if (r5 == 0) goto L_0x1402;
        r6 = 1;
        if (r5 != r6) goto L_0x13f7;
        goto L_0x1402;
        r6 = r1.inputFields;
        r6 = r6[r5];
        r8 = 268435462; // 0x10000006 float:2.5243567E-29 double:1.3262474E-315;
        r6.setImeOptions(r8);
        goto L_0x1421;
        r8 = 268435462; // 0x10000006 float:2.5243567E-29 double:1.3262474E-315;
        r6 = r1.inputFields;
        r6 = r6[r5];
        r9 = 129; // 0x81 float:1.81E-43 double:6.37E-322;
        r6.setInputType(r9);
        r6 = r1.inputFields;
        r6 = r6[r5];
        r9 = android.graphics.Typeface.DEFAULT;
        r6.setTypeface(r9);
        r6 = r1.inputFields;
        r6 = r6[r5];
        r9 = 268435461; // 0x10000005 float:2.5243564E-29 double:1.326247394E-315;
        r6.setImeOptions(r9);
        switch(r5) {
            case 0: goto L_0x1447;
            case 1: goto L_0x1436;
            case 2: goto L_0x1425;
            default: goto L_0x1424;
        };
        goto L_0x145f;
        r6 = r1.inputFields;
        r6 = r6[r5];
        r9 = "PaymentPasswordEmail";
        r11 = 2131494105; // 0x7f0c04d9 float:1.861171E38 double:1.0530980116E-314;
        r9 = org.telegram.messenger.LocaleController.getString(r9, r11);
        r6.setHint(r9);
        goto L_0x145f;
        r6 = r1.inputFields;
        r6 = r6[r5];
        r9 = "PaymentPasswordReEnter";
        r11 = 2131494110; // 0x7f0c04de float:1.861172E38 double:1.053098014E-314;
        r9 = org.telegram.messenger.LocaleController.getString(r9, r11);
        r6.setHint(r9);
        goto L_0x145f;
        r6 = r1.inputFields;
        r6 = r6[r5];
        r9 = "PaymentPasswordEnter";
        r11 = 2131494108; // 0x7f0c04dc float:1.8611715E38 double:1.053098013E-314;
        r9 = org.telegram.messenger.LocaleController.getString(r9, r11);
        r6.setHint(r9);
        r6 = r1.inputFields;
        r6 = r6[r5];
        r6.requestFocus();
        r6 = r1.inputFields;
        r6 = r6[r5];
        r9 = 1086324736; // 0x40c00000 float:6.0 double:5.367157323E-315;
        r9 = org.telegram.messenger.AndroidUtilities.dp(r9);
        r11 = 0;
        r6.setPadding(r11, r11, r11, r9);
        r6 = r1.inputFields;
        r6 = r6[r5];
        r9 = org.telegram.messenger.LocaleController.isRTL;
        if (r9 == 0) goto L_0x1477;
        r9 = 5;
        goto L_0x1478;
        r9 = 3;
        r6.setGravity(r9);
        r6 = r1.inputFields;
        r6 = r6[r5];
        r11 = -1;
        r12 = -1073741824; // 0xffffffffc0000000 float:-2.0 double:NaN;
        r13 = 51;
        r14 = 1099431936; // 0x41880000 float:17.0 double:5.431915495E-315;
        r15 = 1094713344; // 0x41400000 float:12.0 double:5.408602553E-315;
        r16 = 1099431936; // 0x41880000 float:17.0 double:5.431915495E-315;
        r17 = 1086324736; // 0x40c00000 float:6.0 double:5.367157323E-315;
        r9 = org.telegram.ui.Components.LayoutHelper.createFrame(r11, r12, r13, r14, r15, r16, r17);
        r4.addView(r6, r9);
        r6 = r1.inputFields;
        r6 = r6[r5];
        r9 = new org.telegram.ui.PaymentFormActivity$25;
        r9.<init>();
        r6.setOnEditorActionListener(r9);
        r6 = 1;
        if (r5 != r6) goto L_0x14dc;
        r6 = r1.bottomCell;
        r9 = new org.telegram.ui.Cells.TextInfoPrivacyCell;
        r9.<init>(r2);
        r11 = 0;
        r6[r11] = r9;
        r6 = r1.bottomCell;
        r6 = r6[r11];
        r9 = "PaymentPasswordInfo";
        r12 = 2131494109; // 0x7f0c04dd float:1.8611717E38 double:1.0530980136E-314;
        r9 = org.telegram.messenger.LocaleController.getString(r9, r12);
        r6.setText(r9);
        r6 = r1.bottomCell;
        r6 = r6[r11];
        r9 = 2131165331; // 0x7f070093 float:1.7944876E38 double:1.0529355757E-314;
        r12 = "windowBackgroundGrayShadow";
        r9 = org.telegram.ui.ActionBar.Theme.getThemedDrawable(r2, r9, r12);
        r6.setBackgroundDrawable(r9);
        r6 = r1.linearLayout2;
        r9 = r1.bottomCell;
        r9 = r9[r11];
        r11 = -1;
        r12 = -2;
        r13 = org.telegram.ui.Components.LayoutHelper.createLinear(r11, r12);
        r6.addView(r9, r13);
        goto L_0x1518;
        r6 = 2;
        if (r5 != r6) goto L_0x1518;
        r6 = r1.bottomCell;
        r9 = new org.telegram.ui.Cells.TextInfoPrivacyCell;
        r9.<init>(r2);
        r11 = 1;
        r6[r11] = r9;
        r6 = r1.bottomCell;
        r6 = r6[r11];
        r9 = "PaymentPasswordEmailInfo";
        r12 = 2131494106; // 0x7f0c04da float:1.8611711E38 double:1.053098012E-314;
        r9 = org.telegram.messenger.LocaleController.getString(r9, r12);
        r6.setText(r9);
        r6 = r1.bottomCell;
        r6 = r6[r11];
        r9 = "windowBackgroundGrayShadow";
        r12 = 2131165332; // 0x7f070094 float:1.7944878E38 double:1.052935576E-314;
        r9 = org.telegram.ui.ActionBar.Theme.getThemedDrawable(r2, r12, r9);
        r6.setBackgroundDrawable(r9);
        r6 = r1.linearLayout2;
        r9 = r1.bottomCell;
        r9 = r9[r11];
        r11 = -1;
        r12 = -2;
        r13 = org.telegram.ui.Components.LayoutHelper.createLinear(r11, r12);
        r6.addView(r9, r13);
        r5 = r5 + 1;
        r4 = 3;
        goto L_0x12df;
        r28.updatePasswordFields();
        goto L_0x196e;
        r4 = new org.telegram.ui.Cells.PaymentInfoCell;
        r4.<init>(r2);
        r1.paymentInfoCell = r4;
        r4 = r1.paymentInfoCell;
        r5 = "windowBackgroundWhite";
        r5 = org.telegram.ui.ActionBar.Theme.getColor(r5);
        r4.setBackgroundColor(r5);
        r4 = r1.paymentInfoCell;
        r5 = r1.messageObject;
        r5 = r5.messageOwner;
        r5 = r5.media;
        r5 = (org.telegram.tgnet.TLRPC.TL_messageMediaInvoice) r5;
        r6 = r1.currentBotName;
        r4.setInvoice(r5, r6);
        r4 = r1.linearLayout2;
        r5 = r1.paymentInfoCell;
        r6 = -1;
        r7 = -2;
        r8 = org.telegram.ui.Components.LayoutHelper.createLinear(r6, r7);
        r4.addView(r5, r8);
        r4 = r1.sectionCell;
        r5 = new org.telegram.ui.Cells.ShadowSectionCell;
        r5.<init>(r2);
        r8 = 0;
        r4[r8] = r5;
        r4 = r1.linearLayout2;
        r5 = r1.sectionCell;
        r5 = r5[r8];
        r8 = org.telegram.ui.Components.LayoutHelper.createLinear(r6, r7);
        r4.addView(r5, r8);
        r4 = new java.util.ArrayList;
        r4.<init>();
        r5 = r1.paymentForm;
        r5 = r5.invoice;
        r5 = r5.prices;
        r4.addAll(r5);
        r5 = r1.shippingOption;
        if (r5 == 0) goto L_0x1580;
        r5 = r1.shippingOption;
        r5 = r5.prices;
        r4.addAll(r5);
        r5 = r1.getTotalPriceString(r4);
        r6 = 0;
        r7 = r4.size();
        if (r6 >= r7) goto L_0x15bd;
        r7 = r4.get(r6);
        r7 = (org.telegram.tgnet.TLRPC.TL_labeledPrice) r7;
        r8 = new org.telegram.ui.Cells.TextPriceCell;
        r8.<init>(r2);
        r9 = "windowBackgroundWhite";
        r9 = org.telegram.ui.ActionBar.Theme.getColor(r9);
        r8.setBackgroundColor(r9);
        r9 = r7.label;
        r11 = org.telegram.messenger.LocaleController.getInstance();
        r12 = r7.amount;
        r14 = r1.paymentForm;
        r14 = r14.invoice;
        r14 = r14.currency;
        r11 = r11.formatCurrencyString(r12, r14);
        r12 = 0;
        r8.setTextAndValue(r9, r11, r12);
        r9 = r1.linearLayout2;
        r9.addView(r8);
        r6 = r6 + 1;
        goto L_0x1585;
        r6 = new org.telegram.ui.Cells.TextPriceCell;
        r6.<init>(r2);
        r7 = "windowBackgroundWhite";
        r7 = org.telegram.ui.ActionBar.Theme.getColor(r7);
        r6.setBackgroundColor(r7);
        r7 = "PaymentTransactionTotal";
        r8 = 2131494136; // 0x7f0c04f8 float:1.8611772E38 double:1.053098027E-314;
        r7 = org.telegram.messenger.LocaleController.getString(r7, r8);
        r8 = 1;
        r6.setTextAndValue(r7, r5, r8);
        r7 = r1.linearLayout2;
        r7.addView(r6);
        r7 = new android.view.View;
        r7.<init>(r2);
        r8 = r1.dividers;
        r8.add(r7);
        r8 = "divider";
        r8 = org.telegram.ui.ActionBar.Theme.getColor(r8);
        r7.setBackgroundColor(r8);
        r8 = r1.linearLayout2;
        r9 = new android.widget.FrameLayout$LayoutParams;
        r11 = 83;
        r12 = 1;
        r13 = -1;
        r9.<init>(r13, r12, r11);
        r8.addView(r7, r9);
        r8 = r1.detailSettingsCell;
        r9 = new org.telegram.ui.Cells.TextDetailSettingsCell;
        r9.<init>(r2);
        r11 = 0;
        r8[r11] = r9;
        r8 = r1.detailSettingsCell;
        r8 = r8[r11];
        r9 = org.telegram.ui.ActionBar.Theme.getSelectorDrawable(r12);
        r8.setBackgroundDrawable(r9);
        r8 = r1.detailSettingsCell;
        r8 = r8[r11];
        r9 = r1.cardName;
        r13 = "PaymentCheckoutMethod";
        r14 = 2131494091; // 0x7f0c04cb float:1.861168E38 double:1.0530980047E-314;
        r13 = org.telegram.messenger.LocaleController.getString(r13, r14);
        r8.setTextAndValue(r9, r13, r12);
        r8 = r1.linearLayout2;
        r9 = r1.detailSettingsCell;
        r9 = r9[r11];
        r8.addView(r9);
        r8 = r1.currentStep;
        r9 = 4;
        if (r8 != r9) goto L_0x163f;
        r8 = r1.detailSettingsCell;
        r8 = r8[r11];
        r9 = new org.telegram.ui.PaymentFormActivity$20;
        r9.<init>();
        r8.setOnClickListener(r9);
        r8 = 0;
        r9 = r8;
        r8 = 0;
        r11 = r1.paymentForm;
        r11 = r11.users;
        r11 = r11.size();
        if (r8 >= r11) goto L_0x1662;
        r11 = r1.paymentForm;
        r11 = r11.users;
        r11 = r11.get(r8);
        r11 = (org.telegram.tgnet.TLRPC.User) r11;
        r12 = r11.id;
        r13 = r1.paymentForm;
        r13 = r13.provider_id;
        if (r12 != r13) goto L_0x165f;
        r9 = r11;
        r8 = r8 + 1;
        goto L_0x1642;
        if (r9 == 0) goto L_0x169c;
        r8 = r1.detailSettingsCell;
        r11 = new org.telegram.ui.Cells.TextDetailSettingsCell;
        r11.<init>(r2);
        r12 = 1;
        r8[r12] = r11;
        r8 = r1.detailSettingsCell;
        r8 = r8[r12];
        r11 = org.telegram.ui.ActionBar.Theme.getSelectorDrawable(r12);
        r8.setBackgroundDrawable(r11);
        r8 = r1.detailSettingsCell;
        r8 = r8[r12];
        r11 = r9.first_name;
        r13 = r9.last_name;
        r11 = org.telegram.messenger.ContactsController.formatName(r11, r13);
        r13 = r11;
        r14 = "PaymentCheckoutProvider";
        r15 = 2131494095; // 0x7f0c04cf float:1.8611689E38 double:1.0530980067E-314;
        r14 = org.telegram.messenger.LocaleController.getString(r14, r15);
        r8.setTextAndValue(r11, r14, r12);
        r8 = r1.linearLayout2;
        r11 = r1.detailSettingsCell;
        r11 = r11[r12];
        r8.addView(r11);
        goto L_0x169e;
        r13 = "";
        r8 = r13;
        r11 = r1.validateRequest;
        if (r11 == 0) goto L_0x1826;
        r11 = r1.validateRequest;
        r11 = r11.info;
        r11 = r11.shipping_address;
        if (r11 == 0) goto L_0x1727;
        r11 = "%s %s, %s, %s, %s, %s";
        r12 = 6;
        r13 = new java.lang.Object[r12];
        r12 = r1.validateRequest;
        r12 = r12.info;
        r12 = r12.shipping_address;
        r12 = r12.street_line1;
        r14 = 0;
        r13[r14] = r12;
        r12 = r1.validateRequest;
        r12 = r12.info;
        r12 = r12.shipping_address;
        r12 = r12.street_line2;
        r14 = 1;
        r13[r14] = r12;
        r12 = r1.validateRequest;
        r12 = r12.info;
        r12 = r12.shipping_address;
        r12 = r12.city;
        r14 = 2;
        r13[r14] = r12;
        r12 = r1.validateRequest;
        r12 = r12.info;
        r12 = r12.shipping_address;
        r12 = r12.state;
        r14 = 3;
        r13[r14] = r12;
        r12 = r1.validateRequest;
        r12 = r12.info;
        r12 = r12.shipping_address;
        r12 = r12.country_iso2;
        r14 = 4;
        r13[r14] = r12;
        r12 = r1.validateRequest;
        r12 = r12.info;
        r12 = r12.shipping_address;
        r12 = r12.post_code;
        r14 = 5;
        r13[r14] = r12;
        r11 = java.lang.String.format(r11, r13);
        r12 = r1.detailSettingsCell;
        r13 = new org.telegram.ui.Cells.TextDetailSettingsCell;
        r13.<init>(r2);
        r14 = 2;
        r12[r14] = r13;
        r12 = r1.detailSettingsCell;
        r12 = r12[r14];
        r13 = "windowBackgroundWhite";
        r13 = org.telegram.ui.ActionBar.Theme.getColor(r13);
        r12.setBackgroundColor(r13);
        r12 = r1.detailSettingsCell;
        r12 = r12[r14];
        r13 = "PaymentShippingAddress";
        r15 = 2131494116; // 0x7f0c04e4 float:1.8611731E38 double:1.053098017E-314;
        r13 = org.telegram.messenger.LocaleController.getString(r13, r15);
        r15 = 1;
        r12.setTextAndValue(r11, r13, r15);
        r12 = r1.linearLayout2;
        r13 = r1.detailSettingsCell;
        r13 = r13[r14];
        r12.addView(r13);
        r11 = r1.validateRequest;
        r11 = r11.info;
        r11 = r11.name;
        if (r11 == 0) goto L_0x1766;
        r11 = r1.detailSettingsCell;
        r12 = new org.telegram.ui.Cells.TextDetailSettingsCell;
        r12.<init>(r2);
        r13 = 3;
        r11[r13] = r12;
        r11 = r1.detailSettingsCell;
        r11 = r11[r13];
        r12 = "windowBackgroundWhite";
        r12 = org.telegram.ui.ActionBar.Theme.getColor(r12);
        r11.setBackgroundColor(r12);
        r11 = r1.detailSettingsCell;
        r11 = r11[r13];
        r12 = r1.validateRequest;
        r12 = r12.info;
        r12 = r12.name;
        r14 = "PaymentCheckoutName";
        r15 = 2131494092; // 0x7f0c04cc float:1.8611683E38 double:1.053098005E-314;
        r14 = org.telegram.messenger.LocaleController.getString(r14, r15);
        r15 = 1;
        r11.setTextAndValue(r12, r14, r15);
        r11 = r1.linearLayout2;
        r12 = r1.detailSettingsCell;
        r12 = r12[r13];
        r11.addView(r12);
        r11 = r1.validateRequest;
        r11 = r11.info;
        r11 = r11.phone;
        if (r11 == 0) goto L_0x17ae;
        r11 = r1.detailSettingsCell;
        r12 = new org.telegram.ui.Cells.TextDetailSettingsCell;
        r12.<init>(r2);
        r13 = 4;
        r11[r13] = r12;
        r11 = r1.detailSettingsCell;
        r11 = r11[r13];
        r12 = "windowBackgroundWhite";
        r12 = org.telegram.ui.ActionBar.Theme.getColor(r12);
        r11.setBackgroundColor(r12);
        r11 = r1.detailSettingsCell;
        r11 = r11[r13];
        r12 = org.telegram.PhoneFormat.PhoneFormat.getInstance();
        r13 = r1.validateRequest;
        r13 = r13.info;
        r13 = r13.phone;
        r12 = r12.format(r13);
        r13 = "PaymentCheckoutPhoneNumber";
        r14 = 2131494094; // 0x7f0c04ce float:1.8611687E38 double:1.053098006E-314;
        r13 = org.telegram.messenger.LocaleController.getString(r13, r14);
        r14 = 1;
        r11.setTextAndValue(r12, r13, r14);
        r11 = r1.linearLayout2;
        r12 = r1.detailSettingsCell;
        r13 = 4;
        r12 = r12[r13];
        r11.addView(r12);
        r11 = r1.validateRequest;
        r11 = r11.info;
        r11 = r11.email;
        if (r11 == 0) goto L_0x17ed;
        r11 = r1.detailSettingsCell;
        r12 = new org.telegram.ui.Cells.TextDetailSettingsCell;
        r12.<init>(r2);
        r13 = 5;
        r11[r13] = r12;
        r11 = r1.detailSettingsCell;
        r11 = r11[r13];
        r12 = "windowBackgroundWhite";
        r12 = org.telegram.ui.ActionBar.Theme.getColor(r12);
        r11.setBackgroundColor(r12);
        r11 = r1.detailSettingsCell;
        r11 = r11[r13];
        r12 = r1.validateRequest;
        r12 = r12.info;
        r12 = r12.email;
        r14 = "PaymentCheckoutEmail";
        r15 = 2131494090; // 0x7f0c04ca float:1.8611679E38 double:1.053098004E-314;
        r14 = org.telegram.messenger.LocaleController.getString(r14, r15);
        r15 = 1;
        r11.setTextAndValue(r12, r14, r15);
        r11 = r1.linearLayout2;
        r12 = r1.detailSettingsCell;
        r12 = r12[r13];
        r11.addView(r12);
        r11 = r1.shippingOption;
        if (r11 == 0) goto L_0x1826;
        r11 = r1.detailSettingsCell;
        r12 = new org.telegram.ui.Cells.TextDetailSettingsCell;
        r12.<init>(r2);
        r13 = 6;
        r11[r13] = r12;
        r11 = r1.detailSettingsCell;
        r11 = r11[r13];
        r12 = "windowBackgroundWhite";
        r12 = org.telegram.ui.ActionBar.Theme.getColor(r12);
        r11.setBackgroundColor(r12);
        r11 = r1.detailSettingsCell;
        r11 = r11[r13];
        r12 = r1.shippingOption;
        r12 = r12.title;
        r14 = "PaymentCheckoutShippingMethod";
        r15 = 2131494096; // 0x7f0c04d0 float:1.861169E38 double:1.053098007E-314;
        r14 = org.telegram.messenger.LocaleController.getString(r14, r15);
        r15 = 0;
        r11.setTextAndValue(r12, r14, r15);
        r11 = r1.linearLayout2;
        r12 = r1.detailSettingsCell;
        r12 = r12[r13];
        r11.addView(r12);
        r11 = r1.currentStep;
        r12 = 4;
        if (r11 != r12) goto L_0x1944;
        r11 = new android.widget.FrameLayout;
        r11.<init>(r2);
        r1.bottomLayout = r11;
        r11 = r1.bottomLayout;
        r12 = 1;
        r13 = org.telegram.ui.ActionBar.Theme.getSelectorDrawable(r12);
        r11.setBackgroundDrawable(r13);
        r11 = r1.bottomLayout;
        r12 = 80;
        r13 = -1;
        r14 = 48;
        r12 = org.telegram.ui.Components.LayoutHelper.createFrame(r13, r14, r12);
        r10.addView(r11, r12);
        r11 = r1.bottomLayout;
        r12 = new org.telegram.ui.PaymentFormActivity$21;
        r12.<init>(r8, r5);
        r11.setOnClickListener(r12);
        r11 = new android.widget.TextView;
        r11.<init>(r2);
        r1.payTextView = r11;
        r11 = r1.payTextView;
        r12 = "windowBackgroundWhiteBlueText6";
        r12 = org.telegram.ui.ActionBar.Theme.getColor(r12);
        r11.setTextColor(r12);
        r11 = r1.payTextView;
        r12 = "PaymentCheckoutPay";
        r13 = 2131494093; // 0x7f0c04cd float:1.8611685E38 double:1.0530980057E-314;
        r14 = 1;
        r15 = new java.lang.Object[r14];
        r16 = 0;
        r15[r16] = r5;
        r12 = org.telegram.messenger.LocaleController.formatString(r12, r13, r15);
        r11.setText(r12);
        r11 = r1.payTextView;
        r12 = 1096810496; // 0x41600000 float:14.0 double:5.41896386E-315;
        r11.setTextSize(r14, r12);
        r11 = r1.payTextView;
        r12 = 17;
        r11.setGravity(r12);
        r11 = r1.payTextView;
        r12 = "fonts/rmedium.ttf";
        r12 = org.telegram.messenger.AndroidUtilities.getTypeface(r12);
        r11.setTypeface(r12);
        r11 = r1.bottomLayout;
        r12 = r1.payTextView;
        r13 = -1082130432; // 0xffffffffbf800000 float:-1.0 double:NaN;
        r14 = -1;
        r13 = org.telegram.ui.Components.LayoutHelper.createFrame(r14, r13);
        r11.addView(r12, r13);
        r11 = new org.telegram.ui.Components.ContextProgressView;
        r12 = 0;
        r11.<init>(r2, r12);
        r1.progressViewButton = r11;
        r11 = r1.progressViewButton;
        r12 = 4;
        r11.setVisibility(r12);
        r11 = r1.bottomLayout;
        r12 = r1.progressViewButton;
        r13 = -1082130432; // 0xffffffffbf800000 float:-1.0 double:NaN;
        r14 = -1;
        r13 = org.telegram.ui.Components.LayoutHelper.createFrame(r14, r13);
        r11.addView(r12, r13);
        r11 = new android.view.View;
        r11.<init>(r2);
        r12 = 2131165343; // 0x7f07009f float:1.79449E38 double:1.0529355816E-314;
        r11.setBackgroundResource(r12);
        r13 = -1;
        r14 = 1077936128; // 0x40400000 float:3.0 double:5.325712093E-315;
        r15 = 83;
        r16 = 0;
        r17 = 0;
        r18 = 0;
        r19 = 1111490560; // 0x42400000 float:48.0 double:5.491493014E-315;
        r12 = org.telegram.ui.Components.LayoutHelper.createFrame(r13, r14, r15, r16, r17, r18, r19);
        r10.addView(r11, r12);
        r12 = r1.doneItem;
        r13 = 0;
        r12.setEnabled(r13);
        r12 = r1.doneItem;
        r12 = r12.getImageView();
        r13 = 4;
        r12.setVisibility(r13);
        r12 = new org.telegram.ui.PaymentFormActivity$22;
        r12.<init>(r2);
        r1.webView = r12;
        r12 = r1.webView;
        r13 = -1;
        r12.setBackgroundColor(r13);
        r12 = r1.webView;
        r12 = r12.getSettings();
        r13 = 1;
        r12.setJavaScriptEnabled(r13);
        r12 = r1.webView;
        r12 = r12.getSettings();
        r12.setDomStorageEnabled(r13);
        r12 = android.os.Build.VERSION.SDK_INT;
        r13 = 21;
        if (r12 < r13) goto L_0x1927;
        r12 = r1.webView;
        r12 = r12.getSettings();
        r13 = 0;
        r12.setMixedContentMode(r13);
        r12 = android.webkit.CookieManager.getInstance();
        r13 = r1.webView;
        r14 = 1;
        r12.setAcceptThirdPartyCookies(r13, r14);
        r12 = r1.webView;
        r13 = new org.telegram.ui.PaymentFormActivity$23;
        r13.<init>();
        r12.setWebViewClient(r13);
        r12 = r1.webView;
        r13 = -1082130432; // 0xffffffffbf800000 float:-1.0 double:NaN;
        r14 = -1;
        r13 = org.telegram.ui.Components.LayoutHelper.createFrame(r14, r13);
        r10.addView(r12, r13);
        r12 = r1.webView;
        r13 = 8;
        r12.setVisibility(r13);
        r11 = r1.sectionCell;
        r12 = new org.telegram.ui.Cells.ShadowSectionCell;
        r12.<init>(r2);
        r13 = 1;
        r11[r13] = r12;
        r11 = r1.sectionCell;
        r11 = r11[r13];
        r12 = "windowBackgroundGrayShadow";
        r14 = 2131165332; // 0x7f070094 float:1.7944878E38 double:1.052935576E-314;
        r12 = org.telegram.ui.ActionBar.Theme.getThemedDrawable(r2, r14, r12);
        r11.setBackgroundDrawable(r12);
        r11 = r1.linearLayout2;
        r12 = r1.sectionCell;
        r12 = r12[r13];
        r13 = -1;
        r14 = -2;
        r13 = org.telegram.ui.Components.LayoutHelper.createLinear(r13, r14);
        r11.addView(r12, r13);
        r4 = r1.fragmentView;
        return r4;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.ui.PaymentFormActivity.createView(android.content.Context):android.view.View");
    }

    public PaymentFormActivity(MessageObject message, TL_payments_paymentReceipt receipt) {
        this.countriesArray = new ArrayList();
        this.countriesMap = new HashMap();
        this.codesMap = new HashMap();
        this.phoneFormatMap = new HashMap();
        this.headerCell = new HeaderCell[3];
        this.dividers = new ArrayList();
        this.sectionCell = new ShadowSectionCell[3];
        this.bottomCell = new TextInfoPrivacyCell[3];
        this.detailSettingsCell = new TextDetailSettingsCell[7];
        this.currentStep = 5;
        this.paymentForm = new TL_payments_paymentForm();
        this.paymentForm.bot_id = receipt.bot_id;
        this.paymentForm.invoice = receipt.invoice;
        this.paymentForm.provider_id = receipt.provider_id;
        this.paymentForm.users = receipt.users;
        this.shippingOption = receipt.shipping;
        this.messageObject = message;
        this.botUser = MessagesController.getInstance(this.currentAccount).getUser(Integer.valueOf(receipt.bot_id));
        if (this.botUser != null) {
            this.currentBotName = this.botUser.first_name;
        } else {
            this.currentBotName = TtmlNode.ANONYMOUS_REGION_ID;
        }
        this.currentItemName = message.messageOwner.media.title;
        if (receipt.info != null) {
            this.validateRequest = new TL_payments_validateRequestedInfo();
            this.validateRequest.info = receipt.info;
        }
        this.cardName = receipt.credentials_title;
    }

    public PaymentFormActivity(TL_payments_paymentForm form, MessageObject message) {
        int i;
        this.countriesArray = new ArrayList();
        this.countriesMap = new HashMap();
        this.codesMap = new HashMap();
        this.phoneFormatMap = new HashMap();
        this.headerCell = new HeaderCell[3];
        this.dividers = new ArrayList();
        this.sectionCell = new ShadowSectionCell[3];
        this.bottomCell = new TextInfoPrivacyCell[3];
        this.detailSettingsCell = new TextDetailSettingsCell[7];
        if (!(form.invoice.shipping_address_requested || form.invoice.email_requested || form.invoice.name_requested)) {
            if (!form.invoice.phone_requested) {
                if (form.saved_credentials != null) {
                    if (UserConfig.getInstance(this.currentAccount).tmpPassword != null && UserConfig.getInstance(this.currentAccount).tmpPassword.valid_until < ConnectionsManager.getInstance(this.currentAccount).getCurrentTime() + 60) {
                        UserConfig.getInstance(this.currentAccount).tmpPassword = null;
                        UserConfig.getInstance(this.currentAccount).saveConfig(false);
                    }
                    if (UserConfig.getInstance(this.currentAccount).tmpPassword != null) {
                        i = 4;
                    } else {
                        i = 3;
                    }
                } else {
                    i = 2;
                }
                init(form, message, i, null, null, null, null, null, false, null);
            }
        }
        i = 0;
        init(form, message, i, null, null, null, null, null, false, null);
    }

    private PaymentFormActivity(TL_payments_paymentForm form, MessageObject message, int step, TL_payments_validatedRequestedInfo validatedRequestedInfo, TL_shippingOption shipping, String tokenJson, String card, TL_payments_validateRequestedInfo request, boolean saveCard, TL_inputPaymentCredentialsAndroidPay androidPay) {
        this.countriesArray = new ArrayList();
        this.countriesMap = new HashMap();
        this.codesMap = new HashMap();
        this.phoneFormatMap = new HashMap();
        this.headerCell = new HeaderCell[3];
        this.dividers = new ArrayList();
        this.sectionCell = new ShadowSectionCell[3];
        this.bottomCell = new TextInfoPrivacyCell[3];
        this.detailSettingsCell = new TextDetailSettingsCell[7];
        init(form, message, step, validatedRequestedInfo, shipping, tokenJson, card, request, saveCard, androidPay);
    }

    private void setCurrentPassword(account_Password password) {
        if (!(password instanceof TL_account_password)) {
            this.currentPassword = password;
            if (this.currentPassword != null) {
                this.waitingForEmail = this.currentPassword.email_unconfirmed_pattern.length() > 0;
            }
            updatePasswordFields();
        } else if (getParentActivity() != null) {
            goToNextStep();
        }
    }

    private void setDelegate(PaymentFormActivityDelegate paymentFormActivityDelegate) {
        this.delegate = paymentFormActivityDelegate;
    }

    private void init(TL_payments_paymentForm form, MessageObject message, int step, TL_payments_validatedRequestedInfo validatedRequestedInfo, TL_shippingOption shipping, String tokenJson, String card, TL_payments_validateRequestedInfo request, boolean saveCard, TL_inputPaymentCredentialsAndroidPay androidPay) {
        this.currentStep = step;
        this.paymentJson = tokenJson;
        this.androidPayCredentials = androidPay;
        this.requestedInfo = validatedRequestedInfo;
        this.paymentForm = form;
        this.shippingOption = shipping;
        this.messageObject = message;
        this.saveCardInfo = saveCard;
        boolean z = true;
        this.isWebView = "stripe".equals(this.paymentForm.native_provider) ^ true;
        this.botUser = MessagesController.getInstance(this.currentAccount).getUser(Integer.valueOf(form.bot_id));
        if (this.botUser != null) {
            this.currentBotName = this.botUser.first_name;
        } else {
            this.currentBotName = TtmlNode.ANONYMOUS_REGION_ID;
        }
        this.currentItemName = message.messageOwner.media.title;
        this.validateRequest = request;
        this.saveShippingInfo = true;
        if (saveCard) {
            this.saveCardInfo = saveCard;
        } else {
            if (this.paymentForm.saved_credentials == null) {
                z = false;
            }
            this.saveCardInfo = z;
        }
        if (card != null) {
            this.cardName = card;
        } else if (form.saved_credentials != null) {
            this.cardName = form.saved_credentials.title;
        }
    }

    public void onResume() {
        super.onResume();
        AndroidUtilities.requestAdjustResize(getParentActivity(), this.classGuid);
        if (VERSION.SDK_INT >= 23) {
            try {
                if ((this.currentStep == 2 || this.currentStep == 6) && !this.paymentForm.invoice.test) {
                    getParentActivity().getWindow().setFlags(MessagesController.UPDATE_MASK_CHANNEL, MessagesController.UPDATE_MASK_CHANNEL);
                } else if (SharedConfig.passcodeHash.length() == 0 || SharedConfig.allowScreenCapture) {
                    getParentActivity().getWindow().clearFlags(MessagesController.UPDATE_MASK_CHANNEL);
                }
            } catch (Throwable e) {
                FileLog.e(e);
            }
        }
        if (this.googleApiClient != null) {
            this.googleApiClient.connect();
        }
    }

    public void onPause() {
        if (this.googleApiClient != null) {
            this.googleApiClient.disconnect();
        }
    }

    private void loadPasswordInfo() {
        if (!this.loadingPasswordInfo) {
            this.loadingPasswordInfo = true;
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(new TL_account_getPassword(), new RequestDelegate() {
                public void run(final TLObject response, final TL_error error) {
                    AndroidUtilities.runOnUIThread(new Runnable() {
                        public void run() {
                            PaymentFormActivity.this.loadingPasswordInfo = false;
                            if (error == null) {
                                PaymentFormActivity.this.currentPassword = (account_Password) response;
                                if (PaymentFormActivity.this.paymentForm != null && (PaymentFormActivity.this.currentPassword instanceof TL_account_password)) {
                                    PaymentFormActivity.this.paymentForm.password_missing = false;
                                    PaymentFormActivity.this.paymentForm.can_save_credentials = true;
                                    PaymentFormActivity.this.updateSavePaymentField();
                                }
                                byte[] salt = new byte[(PaymentFormActivity.this.currentPassword.new_salt.length + 8)];
                                Utilities.random.nextBytes(salt);
                                System.arraycopy(PaymentFormActivity.this.currentPassword.new_salt, 0, salt, 0, PaymentFormActivity.this.currentPassword.new_salt.length);
                                PaymentFormActivity.this.currentPassword.new_salt = salt;
                                if (PaymentFormActivity.this.passwordFragment != null) {
                                    PaymentFormActivity.this.passwordFragment.setCurrentPassword(PaymentFormActivity.this.currentPassword);
                                }
                            }
                            if ((response instanceof TL_account_noPassword) && PaymentFormActivity.this.shortPollRunnable == null) {
                                PaymentFormActivity.this.shortPollRunnable = new Runnable() {
                                    public void run() {
                                        if (PaymentFormActivity.this.shortPollRunnable != null) {
                                            PaymentFormActivity.this.loadPasswordInfo();
                                            PaymentFormActivity.this.shortPollRunnable = null;
                                        }
                                    }
                                };
                                AndroidUtilities.runOnUIThread(PaymentFormActivity.this.shortPollRunnable, DefaultRenderersFactory.DEFAULT_ALLOWED_VIDEO_JOINING_TIME_MS);
                            }
                        }
                    });
                }
            }, 10);
        }
    }

    private void showAlertWithText(String title, String text) {
        Builder builder = new Builder(getParentActivity());
        builder.setPositiveButton(LocaleController.getString("OK", R.string.OK), null);
        builder.setTitle(title);
        builder.setMessage(text);
        showDialog(builder.create());
    }

    private void showPayAlert(String totalPrice) {
        Builder builder = new Builder(getParentActivity());
        builder.setTitle(LocaleController.getString("PaymentTransactionReview", R.string.PaymentTransactionReview));
        builder.setMessage(LocaleController.formatString("PaymentTransactionMessage", R.string.PaymentTransactionMessage, totalPrice, this.currentBotName, this.currentItemName));
        builder.setPositiveButton(LocaleController.getString("OK", R.string.OK), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                PaymentFormActivity.this.setDonePressed(true);
                PaymentFormActivity.this.sendData();
            }
        });
        builder.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null);
        showDialog(builder.create());
    }

    private void initAndroidPay(Context context) {
        if (VERSION.SDK_INT < 0) {
            int i;
            GoogleApiClient.Builder addOnConnectionFailedListener = new GoogleApiClient.Builder(context).addConnectionCallbacks(new ConnectionCallbacks() {
                public void onConnected(Bundle bundle) {
                }

                public void onConnectionSuspended(int i) {
                }
            }).addOnConnectionFailedListener(new OnConnectionFailedListener() {
                public void onConnectionFailed(ConnectionResult connectionResult) {
                }
            });
            Api api = Wallet.API;
            WalletOptions.Builder builder = new WalletOptions.Builder();
            if (this.paymentForm.invoice.test) {
                i = 3;
            } else {
                i = 1;
            }
            this.googleApiClient = addOnConnectionFailedListener.addApi(api, builder.setEnvironment(i).setTheme(1).build()).build();
            Wallet.Payments.isReadyToPay(this.googleApiClient).setResultCallback(new ResultCallback<BooleanResult>() {
                public void onResult(BooleanResult booleanResult) {
                    if (booleanResult.getStatus().isSuccess() && booleanResult.getValue()) {
                        PaymentFormActivity.this.showAndroidPay();
                    }
                }
            });
            this.googleApiClient.connect();
        }
    }

    private String getTotalPriceString(ArrayList<TL_labeledPrice> prices) {
        long amount = 0;
        int a = 0;
        while (a < prices.size()) {
            a++;
            amount += ((TL_labeledPrice) prices.get(a)).amount;
        }
        return LocaleController.getInstance().formatCurrencyString(amount, this.paymentForm.invoice.currency);
    }

    private String getTotalPriceDecimalString(ArrayList<TL_labeledPrice> prices) {
        long amount = 0;
        int a = 0;
        while (a < prices.size()) {
            a++;
            amount += ((TL_labeledPrice) prices.get(a)).amount;
        }
        return LocaleController.getInstance().formatCurrencyDecimalString(amount, this.paymentForm.invoice.currency, false);
    }

    public boolean onFragmentCreate() {
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.didSetTwoStepPassword);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.didRemovedTwoStepPassword);
        if (this.currentStep != 4) {
            NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.paymentFinished);
        }
        return super.onFragmentCreate();
    }

    public void onFragmentDestroy() {
        if (this.delegate != null) {
            this.delegate.onFragmentDestroyed();
        }
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.didSetTwoStepPassword);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.didRemovedTwoStepPassword);
        if (this.currentStep != 4) {
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.paymentFinished);
        }
        if (this.webView != null) {
            try {
                ViewParent parent = this.webView.getParent();
                if (parent != null) {
                    ((FrameLayout) parent).removeView(this.webView);
                }
                this.webView.stopLoading();
                this.webView.loadUrl("about:blank");
                this.webView.destroy();
                this.webView = null;
            } catch (Throwable e) {
                FileLog.e(e);
            }
        }
        try {
            if ((this.currentStep == 2 || this.currentStep == 6) && VERSION.SDK_INT >= 23 && (SharedConfig.passcodeHash.length() == 0 || SharedConfig.allowScreenCapture)) {
                getParentActivity().getWindow().clearFlags(MessagesController.UPDATE_MASK_CHANNEL);
            }
        } catch (Throwable e2) {
            FileLog.e(e2);
        }
        super.onFragmentDestroy();
        this.canceled = true;
    }

    protected void onTransitionAnimationEnd(boolean isOpen, boolean backward) {
        if (isOpen && !backward) {
            if (this.webView != null) {
                if (this.currentStep != 4) {
                    this.webView.loadUrl(this.paymentForm.url);
                }
            } else if (this.currentStep == 2) {
                this.inputFields[0].requestFocus();
                AndroidUtilities.showKeyboard(this.inputFields[0]);
            } else if (this.currentStep == 3) {
                this.inputFields[1].requestFocus();
                AndroidUtilities.showKeyboard(this.inputFields[1]);
            } else if (this.currentStep == 6 && !this.waitingForEmail) {
                this.inputFields[0].requestFocus();
                AndroidUtilities.showKeyboard(this.inputFields[0]);
            }
        }
    }

    public void didReceivedNotification(int id, int account, Object... args) {
        if (id == NotificationCenter.didSetTwoStepPassword) {
            this.paymentForm.password_missing = false;
            this.paymentForm.can_save_credentials = true;
            updateSavePaymentField();
        } else if (id == NotificationCenter.didRemovedTwoStepPassword) {
            this.paymentForm.password_missing = true;
            this.paymentForm.can_save_credentials = false;
            updateSavePaymentField();
        } else if (id == NotificationCenter.paymentFinished) {
            removeSelfFromStack();
        }
    }

    private void showAndroidPay() {
        if (getParentActivity() != null) {
            if (this.androidPayContainer != null) {
                WalletFragmentStyle buyButtonText;
                PaymentMethodTokenizationParameters parameters;
                WalletFragmentOptions.Builder optionsBuilder = WalletFragmentOptions.newBuilder();
                optionsBuilder.setEnvironment(this.paymentForm.invoice.test ? 3 : 1);
                optionsBuilder.setMode(1);
                int i = 6;
                if (this.androidPayPublicKey != null) {
                    this.androidPayContainer.setBackgroundColor(this.androidPayBackgroundColor);
                    buyButtonText = new WalletFragmentStyle().setBuyButtonText(5);
                    if (!this.androidPayBlackTheme) {
                        i = 4;
                    }
                    buyButtonText = buyButtonText.setBuyButtonAppearance(i).setBuyButtonWidth(-1);
                } else {
                    buyButtonText = new WalletFragmentStyle().setBuyButtonText(6).setBuyButtonAppearance(6).setBuyButtonWidth(-2);
                }
                optionsBuilder.setFragmentStyle(buyButtonText);
                WalletFragment walletFragment = WalletFragment.newInstance(optionsBuilder.build());
                FragmentTransaction fragmentTransaction = getParentActivity().getFragmentManager().beginTransaction();
                fragmentTransaction.replace(fragment_container_id, walletFragment);
                fragmentTransaction.commit();
                ArrayList<TL_labeledPrice> arrayList = new ArrayList();
                arrayList.addAll(this.paymentForm.invoice.prices);
                if (this.shippingOption != null) {
                    arrayList.addAll(this.shippingOption.prices);
                }
                this.totalPriceDecimal = getTotalPriceDecimalString(arrayList);
                if (this.androidPayPublicKey != null) {
                    parameters = PaymentMethodTokenizationParameters.newBuilder().setPaymentMethodTokenizationType(2).addParameter("publicKey", this.androidPayPublicKey).build();
                } else {
                    parameters = PaymentMethodTokenizationParameters.newBuilder().setPaymentMethodTokenizationType(1).addParameter("gateway", "stripe").addParameter("stripe:publishableKey", this.stripeApiKey).addParameter("stripe:version", "3.5.0").build();
                }
                walletFragment.initialize(WalletFragmentInitParams.newBuilder().setMaskedWalletRequest(MaskedWalletRequest.newBuilder().setPaymentMethodTokenizationParameters(parameters).setEstimatedTotalPrice(this.totalPriceDecimal).setCurrencyCode(this.paymentForm.invoice.currency).build()).setMaskedWalletRequestCode(LOAD_MASKED_WALLET_REQUEST_CODE).build());
                this.androidPayContainer.setVisibility(0);
                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.playTogether(new Animator[]{ObjectAnimator.ofFloat(this.androidPayContainer, "alpha", new float[]{0.0f, 1.0f})});
                animatorSet.setInterpolator(new DecelerateInterpolator());
                animatorSet.setDuration(180);
                animatorSet.start();
            }
        }
    }

    public void onActivityResultFragment(int requestCode, int resultCode, Intent data) {
        if (requestCode == LOAD_MASKED_WALLET_REQUEST_CODE) {
            if (resultCode == -1) {
                showEditDoneProgress(true, true);
                setDonePressed(true);
                MaskedWallet maskedWallet = (MaskedWallet) data.getParcelableExtra("com.google.android.gms.wallet.EXTRA_MASKED_WALLET");
                Cart.Builder cardBuilder = Cart.newBuilder().setCurrencyCode(this.paymentForm.invoice.currency).setTotalPrice(this.totalPriceDecimal);
                ArrayList<TL_labeledPrice> arrayList = new ArrayList();
                arrayList.addAll(this.paymentForm.invoice.prices);
                if (this.shippingOption != null) {
                    arrayList.addAll(this.shippingOption.prices);
                }
                for (int a = 0; a < arrayList.size(); a++) {
                    TL_labeledPrice price = (TL_labeledPrice) arrayList.get(a);
                    String amount = LocaleController.getInstance().formatCurrencyDecimalString(price.amount, this.paymentForm.invoice.currency, false);
                    cardBuilder.addLineItem(LineItem.newBuilder().setCurrencyCode(this.paymentForm.invoice.currency).setQuantity("1").setDescription(price.label).setTotalPrice(amount).setUnitPrice(amount).build());
                }
                Wallet.Payments.loadFullWallet(this.googleApiClient, FullWalletRequest.newBuilder().setCart(cardBuilder.build()).setGoogleTransactionId(maskedWallet.getGoogleTransactionId()).build(), LOAD_FULL_WALLET_REQUEST_CODE);
                return;
            }
            showEditDoneProgress(true, false);
            setDonePressed(false);
        } else if (requestCode != LOAD_FULL_WALLET_REQUEST_CODE) {
        } else {
            if (resultCode == -1) {
                FullWallet fullWallet = (FullWallet) data.getParcelableExtra("com.google.android.gms.wallet.EXTRA_FULL_WALLET");
                String tokenJSON = fullWallet.getPaymentMethodToken().getToken();
                try {
                    if (this.androidPayPublicKey != null) {
                        this.androidPayCredentials = new TL_inputPaymentCredentialsAndroidPay();
                        this.androidPayCredentials.payment_token = new TL_dataJSON();
                        this.androidPayCredentials.payment_token.data = tokenJSON;
                        this.androidPayCredentials.google_transaction_id = fullWallet.getGoogleTransactionId();
                        String[] descriptions = fullWallet.getPaymentDescriptions();
                        if (descriptions.length > 0) {
                            this.cardName = descriptions[0];
                        } else {
                            this.cardName = "Android Pay";
                        }
                    } else {
                        this.paymentJson = String.format(Locale.US, "{\"type\":\"%1$s\", \"id\":\"%2$s\"}", new Object[]{token.getType(), TokenParser.parseToken(tokenJSON).getId()});
                        Card card = token.getCard();
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append(card.getType());
                        stringBuilder.append(" *");
                        stringBuilder.append(card.getLast4());
                        this.cardName = stringBuilder.toString();
                    }
                    goToNextStep();
                    showEditDoneProgress(true, false);
                    setDonePressed(false);
                } catch (JSONException e) {
                    showEditDoneProgress(true, false);
                    setDonePressed(false);
                }
                return;
            }
            showEditDoneProgress(true, false);
            setDonePressed(false);
        }
    }

    private void updateSavePaymentField() {
        if (this.bottomCell[0] != null) {
            if (this.sectionCell[2] != null) {
                if (this.paymentForm.password_missing || this.paymentForm.can_save_credentials) {
                    if (this.webView != null) {
                        if (!(this.webView == null || this.webviewLoading)) {
                        }
                    }
                    SpannableStringBuilder text = new SpannableStringBuilder(LocaleController.getString("PaymentCardSavePaymentInformationInfoLine1", R.string.PaymentCardSavePaymentInformationInfoLine1));
                    if (this.paymentForm.password_missing) {
                        loadPasswordInfo();
                        text.append("\n");
                        int len = text.length();
                        String str2 = LocaleController.getString("PaymentCardSavePaymentInformationInfoLine2", R.string.PaymentCardSavePaymentInformationInfoLine2);
                        int index1 = str2.indexOf(42);
                        int index2 = str2.lastIndexOf(42);
                        text.append(str2);
                        if (!(index1 == -1 || index2 == -1)) {
                            index1 += len;
                            index2 += len;
                            this.bottomCell[0].getTextView().setMovementMethod(new LinkMovementMethodMy());
                            text.replace(index2, index2 + 1, TtmlNode.ANONYMOUS_REGION_ID);
                            text.replace(index1, index1 + 1, TtmlNode.ANONYMOUS_REGION_ID);
                            text.setSpan(new LinkSpan(), index1, index2 - 1, 33);
                        }
                    }
                    this.checkCell1.setEnabled(true);
                    this.bottomCell[0].setText(text);
                    this.checkCell1.setVisibility(0);
                    this.bottomCell[0].setVisibility(0);
                    this.sectionCell[2].setBackgroundDrawable(Theme.getThemedDrawable(this.sectionCell[2].getContext(), R.drawable.greydivider, Theme.key_windowBackgroundGrayShadow));
                }
                this.checkCell1.setVisibility(8);
                this.bottomCell[0].setVisibility(8);
                this.sectionCell[2].setBackgroundDrawable(Theme.getThemedDrawable(this.sectionCell[2].getContext(), R.drawable.greydivider_bottom, Theme.key_windowBackgroundGrayShadow));
            }
        }
    }

    @SuppressLint({"HardwareIds"})
    public void fillNumber(String number) {
        try {
            TelephonyManager tm = (TelephonyManager) ApplicationLoader.applicationContext.getSystemService("phone");
            boolean allowCall = true;
            boolean allowSms = true;
            if (!(number == null && (tm.getSimState() == 1 || tm.getPhoneType() == 0))) {
                if (VERSION.SDK_INT >= 23) {
                    allowCall = getParentActivity().checkSelfPermission("android.permission.READ_PHONE_STATE") == 0;
                    allowSms = getParentActivity().checkSelfPermission("android.permission.RECEIVE_SMS") == 0;
                }
                if (number != null || allowCall || allowSms) {
                    if (number == null) {
                        number = PhoneFormat.stripExceptNumbers(tm.getLine1Number());
                    }
                    String textToSet = null;
                    boolean ok = false;
                    if (!TextUtils.isEmpty(number)) {
                        int a = 4;
                        if (number.length() > 4) {
                            int a2;
                            String sub;
                            while (true) {
                                a2 = a;
                                if (a2 < 1) {
                                    break;
                                }
                                sub = number.substring(0, a2);
                                if (((String) this.codesMap.get(sub)) != null) {
                                    break;
                                }
                                a = a2 - 1;
                            }
                            ok = true;
                            textToSet = number.substring(a2, number.length());
                            this.inputFields[8].setText(sub);
                            if (!ok) {
                                textToSet = number.substring(1, number.length());
                                this.inputFields[8].setText(number.substring(0, 1));
                            }
                        }
                        if (textToSet != null) {
                            this.inputFields[9].setText(textToSet);
                            this.inputFields[9].setSelection(this.inputFields[9].length());
                        }
                    }
                }
            }
        } catch (Throwable e) {
            FileLog.e(e);
        }
    }

    private void sendSavePassword(final boolean clear) {
        String firstPassword;
        TL_account_updatePasswordSettings req = new TL_account_updatePasswordSettings();
        if (clear) {
            this.doneItem.setVisibility(0);
            firstPassword = null;
            req.new_settings = new TL_account_passwordInputSettings();
            req.new_settings.flags = 2;
            req.new_settings.email = TtmlNode.ANONYMOUS_REGION_ID;
            req.current_password_hash = new byte[0];
        } else {
            firstPassword = this.inputFields[0].getText().toString();
            if (TextUtils.isEmpty(firstPassword)) {
                shakeField(0);
                return;
            } else if (firstPassword.equals(this.inputFields[1].getText().toString())) {
                String email = this.inputFields[2].getText().toString();
                if (email.length() < 3) {
                    shakeField(2);
                    return;
                }
                int dot = email.lastIndexOf(46);
                int dog = email.lastIndexOf(64);
                if (dot >= 0 && dog >= 0) {
                    if (dot >= dog) {
                        req.current_password_hash = new byte[0];
                        req.new_settings = new TL_account_passwordInputSettings();
                        byte[] newPasswordBytes = null;
                        try {
                            newPasswordBytes = firstPassword.getBytes(C.UTF8_NAME);
                        } catch (Throwable e) {
                            FileLog.e(e);
                        }
                        byte[] new_salt = this.currentPassword.new_salt;
                        byte[] hash = new byte[((new_salt.length * 2) + newPasswordBytes.length)];
                        System.arraycopy(new_salt, 0, hash, 0, new_salt.length);
                        System.arraycopy(newPasswordBytes, 0, hash, new_salt.length, newPasswordBytes.length);
                        System.arraycopy(new_salt, 0, hash, hash.length - new_salt.length, new_salt.length);
                        TL_account_passwordInputSettings tL_account_passwordInputSettings = req.new_settings;
                        tL_account_passwordInputSettings.flags |= 1;
                        req.new_settings.hint = TtmlNode.ANONYMOUS_REGION_ID;
                        req.new_settings.new_password_hash = Utilities.computeSHA256(hash, 0, hash.length);
                        req.new_settings.new_salt = new_salt;
                        if (email.length() > 0) {
                            TL_account_passwordInputSettings tL_account_passwordInputSettings2 = req.new_settings;
                            tL_account_passwordInputSettings2.flags = 2 | tL_account_passwordInputSettings2.flags;
                            req.new_settings.email = email.trim();
                        }
                        firstPassword = email;
                    }
                }
                shakeField(2);
                return;
            } else {
                try {
                    Toast.makeText(getParentActivity(), LocaleController.getString("PasswordDoNotMatch", R.string.PasswordDoNotMatch), 0).show();
                } catch (Throwable e2) {
                    FileLog.e(e2);
                }
                shakeField(1);
                return;
            }
        }
        showEditDoneProgress(true, true);
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(req, new RequestDelegate() {
            public void run(final TLObject response, final TL_error error) {
                AndroidUtilities.runOnUIThread(new Runnable() {
                    public void run() {
                        PaymentFormActivity.this.showEditDoneProgress(true, false);
                        if (clear) {
                            PaymentFormActivity.this.currentPassword = new TL_account_noPassword();
                            PaymentFormActivity.this.delegate.currentPasswordUpdated(PaymentFormActivity.this.currentPassword);
                            PaymentFormActivity.this.finishFragment();
                        } else if (error == null && (response instanceof TL_boolTrue)) {
                            if (PaymentFormActivity.this.getParentActivity() != null) {
                                PaymentFormActivity.this.goToNextStep();
                            }
                        } else if (error != null) {
                            if (error.text.equals("EMAIL_UNCONFIRMED")) {
                                Builder builder = new Builder(PaymentFormActivity.this.getParentActivity());
                                builder.setPositiveButton(LocaleController.getString("OK", R.string.OK), new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        PaymentFormActivity.this.waitingForEmail = true;
                                        PaymentFormActivity.this.currentPassword.email_unconfirmed_pattern = firstPassword;
                                        PaymentFormActivity.this.updatePasswordFields();
                                    }
                                });
                                builder.setMessage(LocaleController.getString("YourEmailAlmostThereText", R.string.YourEmailAlmostThereText));
                                builder.setTitle(LocaleController.getString("YourEmailAlmostThere", R.string.YourEmailAlmostThere));
                                Dialog dialog = PaymentFormActivity.this.showDialog(builder.create());
                                if (dialog != null) {
                                    dialog.setCanceledOnTouchOutside(false);
                                    dialog.setCancelable(false);
                                }
                            } else if (error.text.equals("EMAIL_INVALID")) {
                                PaymentFormActivity.this.showAlertWithText(LocaleController.getString("AppName", R.string.AppName), LocaleController.getString("PasswordEmailInvalid", R.string.PasswordEmailInvalid));
                            } else if (error.text.startsWith("FLOOD_WAIT")) {
                                String timeString;
                                int time = Utilities.parseInt(error.text).intValue();
                                if (time < 60) {
                                    timeString = LocaleController.formatPluralString("Seconds", time);
                                } else {
                                    timeString = LocaleController.formatPluralString("Minutes", time / 60);
                                }
                                PaymentFormActivity.this.showAlertWithText(LocaleController.getString("AppName", R.string.AppName), LocaleController.formatString("FloodWaitTime", R.string.FloodWaitTime, timeString));
                            } else {
                                PaymentFormActivity.this.showAlertWithText(LocaleController.getString("AppName", R.string.AppName), error.text);
                            }
                        }
                    }
                });
            }
        }, 10);
    }

    private boolean sendCardData() {
        Integer month;
        Integer parseInt;
        String[] args = this.inputFields[1].getText().toString().split("/");
        if (args.length == 2) {
            month = Utilities.parseInt(args[0]);
            parseInt = Utilities.parseInt(args[1]);
        } else {
            month = null;
            parseInt = null;
        }
        Integer year = parseInt;
        int i = 3;
        Card card = new Card(r1.inputFields[0].getText().toString(), month, year, r1.inputFields[3].getText().toString(), r1.inputFields[2].getText().toString(), null, null, null, null, r1.inputFields[5].getText().toString(), r1.inputFields[4].getText().toString(), null);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(card.getType());
        stringBuilder.append(" *");
        stringBuilder.append(card.getLast4());
        r1.cardName = stringBuilder.toString();
        if (card.validateNumber()) {
            if (card.validateExpMonth() && card.validateExpYear()) {
                if (card.validateExpiryDate()) {
                    if (r1.need_card_name && r1.inputFields[2].length() == 0) {
                        shakeField(2);
                        return false;
                    } else if (!card.validateCVC()) {
                        shakeField(i);
                        return false;
                    } else if (r1.need_card_country && r1.inputFields[4].length() == 0) {
                        shakeField(4);
                        return false;
                    } else if (r1.need_card_postcode && r1.inputFields[5].length() == 0) {
                        shakeField(5);
                        return false;
                    } else {
                        showEditDoneProgress(true, true);
                        try {
                            new Stripe(r1.stripeApiKey).createToken(card, new TokenCallback() {
                                public void onSuccess(Token token) {
                                    if (!PaymentFormActivity.this.canceled) {
                                        PaymentFormActivity.this.paymentJson = String.format(Locale.US, "{\"type\":\"%1$s\", \"id\":\"%2$s\"}", new Object[]{token.getType(), token.getId()});
                                        AndroidUtilities.runOnUIThread(new Runnable() {
                                            public void run() {
                                                PaymentFormActivity.this.goToNextStep();
                                                PaymentFormActivity.this.showEditDoneProgress(true, false);
                                                PaymentFormActivity.this.setDonePressed(false);
                                            }
                                        });
                                    }
                                }

                                public void onError(Exception error) {
                                    if (!PaymentFormActivity.this.canceled) {
                                        PaymentFormActivity.this.showEditDoneProgress(true, false);
                                        PaymentFormActivity.this.setDonePressed(false);
                                        if (!(error instanceof APIConnectionException)) {
                                            if (!(error instanceof APIException)) {
                                                AlertsCreator.showSimpleToast(PaymentFormActivity.this, error.getMessage());
                                            }
                                        }
                                        AlertsCreator.showSimpleToast(PaymentFormActivity.this, LocaleController.getString("PaymentConnectionFailed", R.string.PaymentConnectionFailed));
                                    }
                                }
                            });
                        } catch (Throwable e) {
                            FileLog.e(e);
                        }
                        return true;
                    }
                }
            }
            shakeField(1);
            return false;
        }
        shakeField(0);
        return false;
    }

    private void sendForm() {
        if (!this.canceled) {
            TL_paymentRequestedInfo tL_paymentRequestedInfo;
            showEditDoneProgress(true, true);
            this.validateRequest = new TL_payments_validateRequestedInfo();
            this.validateRequest.save = this.saveShippingInfo;
            this.validateRequest.msg_id = this.messageObject.getId();
            this.validateRequest.info = new TL_paymentRequestedInfo();
            if (this.paymentForm.invoice.name_requested) {
                this.validateRequest.info.name = this.inputFields[6].getText().toString();
                tL_paymentRequestedInfo = this.validateRequest.info;
                tL_paymentRequestedInfo.flags |= 1;
            }
            if (this.paymentForm.invoice.phone_requested) {
                tL_paymentRequestedInfo = this.validateRequest.info;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("+");
                stringBuilder.append(this.inputFields[8].getText().toString());
                stringBuilder.append(this.inputFields[9].getText().toString());
                tL_paymentRequestedInfo.phone = stringBuilder.toString();
                tL_paymentRequestedInfo = this.validateRequest.info;
                tL_paymentRequestedInfo.flags |= 2;
            }
            if (this.paymentForm.invoice.email_requested) {
                this.validateRequest.info.email = this.inputFields[7].getText().toString().trim();
                tL_paymentRequestedInfo = this.validateRequest.info;
                tL_paymentRequestedInfo.flags |= 4;
            }
            if (this.paymentForm.invoice.shipping_address_requested) {
                this.validateRequest.info.shipping_address = new TL_postAddress();
                this.validateRequest.info.shipping_address.street_line1 = this.inputFields[0].getText().toString();
                this.validateRequest.info.shipping_address.street_line2 = this.inputFields[1].getText().toString();
                this.validateRequest.info.shipping_address.city = this.inputFields[2].getText().toString();
                this.validateRequest.info.shipping_address.state = this.inputFields[3].getText().toString();
                this.validateRequest.info.shipping_address.country_iso2 = this.countryName != null ? this.countryName : TtmlNode.ANONYMOUS_REGION_ID;
                this.validateRequest.info.shipping_address.post_code = this.inputFields[5].getText().toString();
                TL_paymentRequestedInfo tL_paymentRequestedInfo2 = this.validateRequest.info;
                tL_paymentRequestedInfo2.flags |= 8;
            }
            final TLObject req = this.validateRequest;
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(this.validateRequest, new RequestDelegate() {
                public void run(final TLObject response, final TL_error error) {
                    if (response instanceof TL_payments_validatedRequestedInfo) {
                        AndroidUtilities.runOnUIThread(new Runnable() {
                            public void run() {
                                PaymentFormActivity.this.requestedInfo = (TL_payments_validatedRequestedInfo) response;
                                if (!(PaymentFormActivity.this.paymentForm.saved_info == null || PaymentFormActivity.this.saveShippingInfo)) {
                                    TL_payments_clearSavedInfo req = new TL_payments_clearSavedInfo();
                                    req.info = true;
                                    ConnectionsManager.getInstance(PaymentFormActivity.this.currentAccount).sendRequest(req, new RequestDelegate() {
                                        public void run(TLObject response, TL_error error) {
                                        }
                                    });
                                }
                                PaymentFormActivity.this.goToNextStep();
                                PaymentFormActivity.this.setDonePressed(false);
                                PaymentFormActivity.this.showEditDoneProgress(true, false);
                            }
                        });
                    } else {
                        AndroidUtilities.runOnUIThread(new Runnable() {
                            /* JADX WARNING: inconsistent code. */
                            /* Code decompiled incorrectly, please refer to instructions dump. */
                            public void run() {
                                /*
                                r11 = this;
                                r0 = org.telegram.ui.PaymentFormActivity.AnonymousClass34.this;
                                r0 = org.telegram.ui.PaymentFormActivity.this;
                                r1 = 0;
                                r0.setDonePressed(r1);
                                r0 = org.telegram.ui.PaymentFormActivity.AnonymousClass34.this;
                                r0 = org.telegram.ui.PaymentFormActivity.this;
                                r2 = 1;
                                r0.showEditDoneProgress(r2, r1);
                                r0 = r3;
                                if (r0 == 0) goto L_0x00e9;
                            L_0x0014:
                                r0 = r3;
                                r0 = r0.text;
                                r3 = -1;
                                r4 = r0.hashCode();
                                r5 = 4;
                                r6 = 7;
                                r7 = 3;
                                r8 = 5;
                                r9 = 6;
                                r10 = 2;
                                switch(r4) {
                                    case -2092780146: goto L_0x0079;
                                    case -1623547228: goto L_0x006f;
                                    case -1224177757: goto L_0x0065;
                                    case -1031752045: goto L_0x005b;
                                    case -274035920: goto L_0x0051;
                                    case 417441502: goto L_0x0047;
                                    case 708423542: goto L_0x003d;
                                    case 863965605: goto L_0x0032;
                                    case 889106340: goto L_0x0028;
                                    default: goto L_0x0026;
                                };
                            L_0x0026:
                                goto L_0x0083;
                            L_0x0028:
                                r4 = "REQ_INFO_EMAIL_INVALID";
                                r0 = r0.equals(r4);
                                if (r0 == 0) goto L_0x0083;
                            L_0x0030:
                                r0 = r10;
                                goto L_0x0084;
                            L_0x0032:
                                r4 = "ADDRESS_STREET_LINE2_INVALID";
                                r0 = r0.equals(r4);
                                if (r0 == 0) goto L_0x0083;
                            L_0x003a:
                                r0 = 8;
                                goto L_0x0084;
                            L_0x003d:
                                r4 = "REQ_INFO_PHONE_INVALID";
                                r0 = r0.equals(r4);
                                if (r0 == 0) goto L_0x0083;
                            L_0x0045:
                                r0 = r2;
                                goto L_0x0084;
                            L_0x0047:
                                r4 = "ADDRESS_STATE_INVALID";
                                r0 = r0.equals(r4);
                                if (r0 == 0) goto L_0x0083;
                            L_0x004f:
                                r0 = r9;
                                goto L_0x0084;
                            L_0x0051:
                                r4 = "ADDRESS_POSTCODE_INVALID";
                                r0 = r0.equals(r4);
                                if (r0 == 0) goto L_0x0083;
                            L_0x0059:
                                r0 = r8;
                                goto L_0x0084;
                            L_0x005b:
                                r4 = "REQ_INFO_NAME_INVALID";
                                r0 = r0.equals(r4);
                                if (r0 == 0) goto L_0x0083;
                            L_0x0063:
                                r0 = r1;
                                goto L_0x0084;
                            L_0x0065:
                                r4 = "ADDRESS_COUNTRY_INVALID";
                                r0 = r0.equals(r4);
                                if (r0 == 0) goto L_0x0083;
                            L_0x006d:
                                r0 = r7;
                                goto L_0x0084;
                            L_0x006f:
                                r4 = "ADDRESS_STREET_LINE1_INVALID";
                                r0 = r0.equals(r4);
                                if (r0 == 0) goto L_0x0083;
                            L_0x0077:
                                r0 = r6;
                                goto L_0x0084;
                            L_0x0079:
                                r4 = "ADDRESS_CITY_INVALID";
                                r0 = r0.equals(r4);
                                if (r0 == 0) goto L_0x0083;
                            L_0x0081:
                                r0 = r5;
                                goto L_0x0084;
                            L_0x0083:
                                r0 = r3;
                            L_0x0084:
                                switch(r0) {
                                    case 0: goto L_0x00e1;
                                    case 1: goto L_0x00d7;
                                    case 2: goto L_0x00cf;
                                    case 3: goto L_0x00c7;
                                    case 4: goto L_0x00bf;
                                    case 5: goto L_0x00b7;
                                    case 6: goto L_0x00af;
                                    case 7: goto L_0x00a7;
                                    case 8: goto L_0x009f;
                                    default: goto L_0x0087;
                                };
                            L_0x0087:
                                r0 = org.telegram.ui.PaymentFormActivity.AnonymousClass34.this;
                                r0 = org.telegram.ui.PaymentFormActivity.this;
                                r0 = r0.currentAccount;
                                r2 = r3;
                                r3 = org.telegram.ui.PaymentFormActivity.AnonymousClass34.this;
                                r3 = org.telegram.ui.PaymentFormActivity.this;
                                r4 = org.telegram.ui.PaymentFormActivity.AnonymousClass34.this;
                                r4 = r0;
                                r1 = new java.lang.Object[r1];
                                org.telegram.ui.Components.AlertsCreator.processError(r0, r2, r3, r4, r1);
                                goto L_0x00e9;
                            L_0x009f:
                                r0 = org.telegram.ui.PaymentFormActivity.AnonymousClass34.this;
                                r0 = org.telegram.ui.PaymentFormActivity.this;
                                r0.shakeField(r2);
                                goto L_0x00e9;
                            L_0x00a7:
                                r0 = org.telegram.ui.PaymentFormActivity.AnonymousClass34.this;
                                r0 = org.telegram.ui.PaymentFormActivity.this;
                                r0.shakeField(r1);
                                goto L_0x00e9;
                            L_0x00af:
                                r0 = org.telegram.ui.PaymentFormActivity.AnonymousClass34.this;
                                r0 = org.telegram.ui.PaymentFormActivity.this;
                                r0.shakeField(r7);
                                goto L_0x00e9;
                            L_0x00b7:
                                r0 = org.telegram.ui.PaymentFormActivity.AnonymousClass34.this;
                                r0 = org.telegram.ui.PaymentFormActivity.this;
                                r0.shakeField(r8);
                                goto L_0x00e9;
                            L_0x00bf:
                                r0 = org.telegram.ui.PaymentFormActivity.AnonymousClass34.this;
                                r0 = org.telegram.ui.PaymentFormActivity.this;
                                r0.shakeField(r10);
                                goto L_0x00e9;
                            L_0x00c7:
                                r0 = org.telegram.ui.PaymentFormActivity.AnonymousClass34.this;
                                r0 = org.telegram.ui.PaymentFormActivity.this;
                                r0.shakeField(r5);
                                goto L_0x00e9;
                            L_0x00cf:
                                r0 = org.telegram.ui.PaymentFormActivity.AnonymousClass34.this;
                                r0 = org.telegram.ui.PaymentFormActivity.this;
                                r0.shakeField(r6);
                                goto L_0x00e9;
                            L_0x00d7:
                                r0 = org.telegram.ui.PaymentFormActivity.AnonymousClass34.this;
                                r0 = org.telegram.ui.PaymentFormActivity.this;
                                r1 = 9;
                                r0.shakeField(r1);
                                goto L_0x00e9;
                            L_0x00e1:
                                r0 = org.telegram.ui.PaymentFormActivity.AnonymousClass34.this;
                                r0 = org.telegram.ui.PaymentFormActivity.this;
                                r0.shakeField(r9);
                            L_0x00e9:
                                return;
                                */
                                throw new UnsupportedOperationException("Method not decompiled: org.telegram.ui.PaymentFormActivity.34.2.run():void");
                            }
                        });
                    }
                }
            }, 2);
        }
    }

    private TL_paymentRequestedInfo getRequestInfo() {
        TL_paymentRequestedInfo info = new TL_paymentRequestedInfo();
        if (this.paymentForm.invoice.name_requested) {
            info.name = this.inputFields[6].getText().toString();
            info.flags |= 1;
        }
        if (this.paymentForm.invoice.phone_requested) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("+");
            stringBuilder.append(this.inputFields[8].getText().toString());
            stringBuilder.append(this.inputFields[9].getText().toString());
            info.phone = stringBuilder.toString();
            info.flags |= 2;
        }
        if (this.paymentForm.invoice.email_requested) {
            info.email = this.inputFields[7].getText().toString().trim();
            info.flags |= 4;
        }
        if (this.paymentForm.invoice.shipping_address_requested) {
            info.shipping_address = new TL_postAddress();
            info.shipping_address.street_line1 = this.inputFields[0].getText().toString();
            info.shipping_address.street_line2 = this.inputFields[1].getText().toString();
            info.shipping_address.city = this.inputFields[2].getText().toString();
            info.shipping_address.state = this.inputFields[3].getText().toString();
            info.shipping_address.country_iso2 = this.countryName != null ? this.countryName : TtmlNode.ANONYMOUS_REGION_ID;
            info.shipping_address.post_code = this.inputFields[5].getText().toString();
            info.flags |= 8;
        }
        return info;
    }

    private void sendData() {
        if (!this.canceled) {
            showEditDoneProgress(false, true);
            final TL_payments_sendPaymentForm req = new TL_payments_sendPaymentForm();
            req.msg_id = this.messageObject.getId();
            if (UserConfig.getInstance(this.currentAccount).tmpPassword != null && this.paymentForm.saved_credentials != null) {
                req.credentials = new TL_inputPaymentCredentialsSaved();
                req.credentials.id = this.paymentForm.saved_credentials.id;
                req.credentials.tmp_password = UserConfig.getInstance(this.currentAccount).tmpPassword.tmp_password;
            } else if (this.androidPayCredentials != null) {
                req.credentials = this.androidPayCredentials;
            } else {
                req.credentials = new TL_inputPaymentCredentials();
                req.credentials.save = this.saveCardInfo;
                req.credentials.data = new TL_dataJSON();
                req.credentials.data.data = this.paymentJson;
            }
            if (!(this.requestedInfo == null || this.requestedInfo.id == null)) {
                req.requested_info_id = this.requestedInfo.id;
                req.flags = 1 | req.flags;
            }
            if (this.shippingOption != null) {
                req.shipping_option_id = this.shippingOption.id;
                req.flags |= 2;
            }
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(req, new RequestDelegate() {
                public void run(final TLObject response, final TL_error error) {
                    if (response == null) {
                        AndroidUtilities.runOnUIThread(new Runnable() {
                            public void run() {
                                AlertsCreator.processError(PaymentFormActivity.this.currentAccount, error, PaymentFormActivity.this, req, new Object[0]);
                                PaymentFormActivity.this.setDonePressed(false);
                                PaymentFormActivity.this.showEditDoneProgress(false, false);
                            }
                        });
                    } else if (response instanceof TL_payments_paymentResult) {
                        MessagesController.getInstance(PaymentFormActivity.this.currentAccount).processUpdates(((TL_payments_paymentResult) response).updates, false);
                        AndroidUtilities.runOnUIThread(new Runnable() {
                            public void run() {
                                PaymentFormActivity.this.goToNextStep();
                            }
                        });
                    } else if (response instanceof TL_payments_paymentVerficationNeeded) {
                        AndroidUtilities.runOnUIThread(new Runnable() {
                            public void run() {
                                NotificationCenter.getInstance(PaymentFormActivity.this.currentAccount).postNotificationName(NotificationCenter.paymentFinished, new Object[0]);
                                PaymentFormActivity.this.setDonePressed(false);
                                PaymentFormActivity.this.webView.setVisibility(0);
                                PaymentFormActivity.this.webviewLoading = true;
                                PaymentFormActivity.this.showEditDoneProgress(true, true);
                                PaymentFormActivity.this.progressView.setVisibility(0);
                                PaymentFormActivity.this.doneItem.setEnabled(false);
                                PaymentFormActivity.this.doneItem.getImageView().setVisibility(4);
                                PaymentFormActivity.this.webView.loadUrl(((TL_payments_paymentVerficationNeeded) response).url);
                            }
                        });
                    }
                }
            }, 2);
        }
    }

    private void shakeField(int field) {
        Vibrator v = (Vibrator) getParentActivity().getSystemService("vibrator");
        if (v != null) {
            v.vibrate(200);
        }
        AndroidUtilities.shakeView(this.inputFields[field], 2.0f, 0);
    }

    private void setDonePressed(boolean value) {
        this.donePressed = value;
        this.swipeBackEnabled = value ^ 1;
        this.actionBar.getBackButton().setEnabled(this.donePressed ^ 1);
        if (this.detailSettingsCell[0] != null) {
            this.detailSettingsCell[0].setEnabled(this.donePressed ^ 1);
        }
    }

    private void checkPassword() {
        if (UserConfig.getInstance(this.currentAccount).tmpPassword != null && UserConfig.getInstance(this.currentAccount).tmpPassword.valid_until < ConnectionsManager.getInstance(this.currentAccount).getCurrentTime() + 60) {
            UserConfig.getInstance(this.currentAccount).tmpPassword = null;
            UserConfig.getInstance(this.currentAccount).saveConfig(false);
        }
        if (UserConfig.getInstance(this.currentAccount).tmpPassword != null) {
            sendData();
        } else if (this.inputFields[1].length() == 0) {
            Vibrator v = (Vibrator) ApplicationLoader.applicationContext.getSystemService("vibrator");
            if (v != null) {
                v.vibrate(200);
            }
            AndroidUtilities.shakeView(this.inputFields[1], 2.0f, 0);
        } else {
            final String password = this.inputFields[1].getText().toString();
            showEditDoneProgress(true, true);
            setDonePressed(true);
            final TL_account_getPassword req = new TL_account_getPassword();
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(req, new RequestDelegate() {
                public void run(final TLObject response, final TL_error error) {
                    AndroidUtilities.runOnUIThread(new Runnable() {
                        public void run() {
                            if (error != null) {
                                AlertsCreator.processError(PaymentFormActivity.this.currentAccount, error, PaymentFormActivity.this, req, new Object[0]);
                                PaymentFormActivity.this.showEditDoneProgress(true, false);
                                PaymentFormActivity.this.setDonePressed(false);
                            } else if (response instanceof TL_account_noPassword) {
                                PaymentFormActivity.this.passwordOk = false;
                                PaymentFormActivity.this.goToNextStep();
                            } else {
                                TL_account_password currentPassword = response;
                                byte[] passwordBytes = null;
                                try {
                                    passwordBytes = password.getBytes(C.UTF8_NAME);
                                } catch (Throwable e) {
                                    FileLog.e(e);
                                }
                                byte[] hash = new byte[((currentPassword.current_salt.length * 2) + passwordBytes.length)];
                                System.arraycopy(currentPassword.current_salt, 0, hash, 0, currentPassword.current_salt.length);
                                System.arraycopy(passwordBytes, 0, hash, currentPassword.current_salt.length, passwordBytes.length);
                                System.arraycopy(currentPassword.current_salt, 0, hash, hash.length - currentPassword.current_salt.length, currentPassword.current_salt.length);
                                final TL_account_getTmpPassword req = new TL_account_getTmpPassword();
                                req.password_hash = Utilities.computeSHA256(hash, 0, hash.length);
                                req.period = 1800;
                                ConnectionsManager.getInstance(PaymentFormActivity.this.currentAccount).sendRequest(req, new RequestDelegate() {
                                    public void run(final TLObject response, final TL_error error) {
                                        AndroidUtilities.runOnUIThread(new Runnable() {
                                            public void run() {
                                                PaymentFormActivity.this.showEditDoneProgress(true, false);
                                                PaymentFormActivity.this.setDonePressed(false);
                                                if (response != null) {
                                                    PaymentFormActivity.this.passwordOk = true;
                                                    UserConfig.getInstance(PaymentFormActivity.this.currentAccount).tmpPassword = (TL_account_tmpPassword) response;
                                                    UserConfig.getInstance(PaymentFormActivity.this.currentAccount).saveConfig(false);
                                                    PaymentFormActivity.this.goToNextStep();
                                                } else if (error.text.equals("PASSWORD_HASH_INVALID")) {
                                                    Vibrator v = (Vibrator) ApplicationLoader.applicationContext.getSystemService("vibrator");
                                                    if (v != null) {
                                                        v.vibrate(200);
                                                    }
                                                    AndroidUtilities.shakeView(PaymentFormActivity.this.inputFields[1], 2.0f, 0);
                                                    PaymentFormActivity.this.inputFields[1].setText(TtmlNode.ANONYMOUS_REGION_ID);
                                                } else {
                                                    AlertsCreator.processError(PaymentFormActivity.this.currentAccount, error, PaymentFormActivity.this, req, new Object[0]);
                                                }
                                            }
                                        });
                                    }
                                }, 2);
                            }
                        }
                    });
                }
            }, 2);
        }
    }

    private void showEditDoneProgress(boolean animateDoneItem, boolean show) {
        final boolean z = show;
        if (this.doneItemAnimation != null) {
            r0.doneItemAnimation.cancel();
        }
        AnimatorSet animatorSet;
        Animator[] animatorArr;
        if (animateDoneItem && r0.doneItem != null) {
            r0.doneItemAnimation = new AnimatorSet();
            if (z) {
                r0.progressView.setVisibility(0);
                r0.doneItem.setEnabled(false);
                AnimatorSet animatorSet2 = r0.doneItemAnimation;
                r6 = new Animator[6];
                r6[0] = ObjectAnimator.ofFloat(r0.doneItem.getImageView(), "scaleX", new float[]{0.1f});
                r6[1] = ObjectAnimator.ofFloat(r0.doneItem.getImageView(), "scaleY", new float[]{0.1f});
                r6[2] = ObjectAnimator.ofFloat(r0.doneItem.getImageView(), "alpha", new float[]{0.0f});
                r6[3] = ObjectAnimator.ofFloat(r0.progressView, "scaleX", new float[]{1.0f});
                r6[4] = ObjectAnimator.ofFloat(r0.progressView, "scaleY", new float[]{1.0f});
                r6[5] = ObjectAnimator.ofFloat(r0.progressView, "alpha", new float[]{1.0f});
                animatorSet2.playTogether(r6);
            } else if (r0.webView != null) {
                animatorSet = r0.doneItemAnimation;
                animatorArr = new Animator[3];
                animatorArr[0] = ObjectAnimator.ofFloat(r0.progressView, "scaleX", new float[]{0.1f});
                animatorArr[1] = ObjectAnimator.ofFloat(r0.progressView, "scaleY", new float[]{0.1f});
                animatorArr[2] = ObjectAnimator.ofFloat(r0.progressView, "alpha", new float[]{0.0f});
                animatorSet.playTogether(animatorArr);
            } else {
                r0.doneItem.getImageView().setVisibility(0);
                r0.doneItem.setEnabled(true);
                animatorSet = r0.doneItemAnimation;
                animatorArr = new Animator[6];
                animatorArr[0] = ObjectAnimator.ofFloat(r0.progressView, "scaleX", new float[]{0.1f});
                animatorArr[1] = ObjectAnimator.ofFloat(r0.progressView, "scaleY", new float[]{0.1f});
                animatorArr[2] = ObjectAnimator.ofFloat(r0.progressView, "alpha", new float[]{0.0f});
                animatorArr[3] = ObjectAnimator.ofFloat(r0.doneItem.getImageView(), "scaleX", new float[]{1.0f});
                animatorArr[4] = ObjectAnimator.ofFloat(r0.doneItem.getImageView(), "scaleY", new float[]{1.0f});
                animatorArr[5] = ObjectAnimator.ofFloat(r0.doneItem.getImageView(), "alpha", new float[]{1.0f});
                animatorSet.playTogether(animatorArr);
            }
            r0.doneItemAnimation.addListener(new AnimatorListenerAdapter() {
                public void onAnimationEnd(Animator animation) {
                    if (PaymentFormActivity.this.doneItemAnimation != null && PaymentFormActivity.this.doneItemAnimation.equals(animation)) {
                        if (z) {
                            PaymentFormActivity.this.doneItem.getImageView().setVisibility(4);
                        } else {
                            PaymentFormActivity.this.progressView.setVisibility(4);
                        }
                    }
                }

                public void onAnimationCancel(Animator animation) {
                    if (PaymentFormActivity.this.doneItemAnimation != null && PaymentFormActivity.this.doneItemAnimation.equals(animation)) {
                        PaymentFormActivity.this.doneItemAnimation = null;
                    }
                }
            });
            r0.doneItemAnimation.setDuration(150);
            r0.doneItemAnimation.start();
        } else if (r0.payTextView != null) {
            r0.doneItemAnimation = new AnimatorSet();
            if (z) {
                r0.progressViewButton.setVisibility(0);
                r0.bottomLayout.setEnabled(false);
                animatorSet = r0.doneItemAnimation;
                animatorArr = new Animator[6];
                animatorArr[0] = ObjectAnimator.ofFloat(r0.payTextView, "scaleX", new float[]{0.1f});
                animatorArr[1] = ObjectAnimator.ofFloat(r0.payTextView, "scaleY", new float[]{0.1f});
                animatorArr[2] = ObjectAnimator.ofFloat(r0.payTextView, "alpha", new float[]{0.0f});
                animatorArr[3] = ObjectAnimator.ofFloat(r0.progressViewButton, "scaleX", new float[]{1.0f});
                animatorArr[4] = ObjectAnimator.ofFloat(r0.progressViewButton, "scaleY", new float[]{1.0f});
                animatorArr[5] = ObjectAnimator.ofFloat(r0.progressViewButton, "alpha", new float[]{1.0f});
                animatorSet.playTogether(animatorArr);
            } else {
                r0.payTextView.setVisibility(0);
                r0.bottomLayout.setEnabled(true);
                animatorSet = r0.doneItemAnimation;
                animatorArr = new Animator[6];
                animatorArr[0] = ObjectAnimator.ofFloat(r0.progressViewButton, "scaleX", new float[]{0.1f});
                animatorArr[1] = ObjectAnimator.ofFloat(r0.progressViewButton, "scaleY", new float[]{0.1f});
                animatorArr[2] = ObjectAnimator.ofFloat(r0.progressViewButton, "alpha", new float[]{0.0f});
                animatorArr[3] = ObjectAnimator.ofFloat(r0.payTextView, "scaleX", new float[]{1.0f});
                animatorArr[4] = ObjectAnimator.ofFloat(r0.payTextView, "scaleY", new float[]{1.0f});
                animatorArr[5] = ObjectAnimator.ofFloat(r0.payTextView, "alpha", new float[]{1.0f});
                animatorSet.playTogether(animatorArr);
            }
            r0.doneItemAnimation.addListener(new AnimatorListenerAdapter() {
                public void onAnimationEnd(Animator animation) {
                    if (PaymentFormActivity.this.doneItemAnimation != null && PaymentFormActivity.this.doneItemAnimation.equals(animation)) {
                        if (z) {
                            PaymentFormActivity.this.payTextView.setVisibility(4);
                        } else {
                            PaymentFormActivity.this.progressViewButton.setVisibility(4);
                        }
                    }
                }

                public void onAnimationCancel(Animator animation) {
                    if (PaymentFormActivity.this.doneItemAnimation != null && PaymentFormActivity.this.doneItemAnimation.equals(animation)) {
                        PaymentFormActivity.this.doneItemAnimation = null;
                    }
                }
            });
            r0.doneItemAnimation.setDuration(150);
            r0.doneItemAnimation.start();
        }
    }

    public boolean onBackPressed() {
        return this.donePressed ^ 1;
    }

    public ThemeDescription[] getThemeDescriptions() {
        int a;
        ArrayList<ThemeDescription> arrayList = new ArrayList();
        arrayList.add(new ThemeDescription(this.fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_windowBackgroundGray));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_actionBarDefault));
        arrayList.add(new ThemeDescription(this.scrollView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, Theme.key_actionBarDefault));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, Theme.key_actionBarDefaultIcon));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, Theme.key_actionBarDefaultTitle));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, Theme.key_actionBarDefaultSelector));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SEARCH, null, null, null, null, Theme.key_actionBarDefaultSearch));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SEARCHPLACEHOLDER, null, null, null, null, Theme.key_actionBarDefaultSearchPlaceholder));
        arrayList.add(new ThemeDescription(this.linearLayout2, 0, new Class[]{View.class}, Theme.dividerPaint, null, null, Theme.key_divider));
        arrayList.add(new ThemeDescription(this.progressView, 0, null, null, null, null, Theme.key_contextProgressInner2));
        arrayList.add(new ThemeDescription(this.progressView, 0, null, null, null, null, Theme.key_contextProgressOuter2));
        arrayList.add(new ThemeDescription(this.progressViewButton, 0, null, null, null, null, Theme.key_contextProgressInner2));
        arrayList.add(new ThemeDescription(this.progressViewButton, 0, null, null, null, null, Theme.key_contextProgressOuter2));
        if (this.inputFields != null) {
            for (a = 0; a < r0.inputFields.length; a++) {
                arrayList.add(new ThemeDescription((View) r0.inputFields[a].getParent(), ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_windowBackgroundWhite));
                arrayList.add(new ThemeDescription(r0.inputFields[a], ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, Theme.key_windowBackgroundWhiteBlackText));
                arrayList.add(new ThemeDescription(r0.inputFields[a], ThemeDescription.FLAG_HINTTEXTCOLOR, null, null, null, null, Theme.key_windowBackgroundWhiteHintText));
            }
        } else {
            arrayList.add(new ThemeDescription(null, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, Theme.key_windowBackgroundWhiteBlackText));
            arrayList.add(new ThemeDescription(null, ThemeDescription.FLAG_HINTTEXTCOLOR, null, null, null, null, Theme.key_windowBackgroundWhiteHintText));
        }
        if (r0.radioCells != null) {
            for (a = 0; a < r0.radioCells.length; a++) {
                arrayList.add(new ThemeDescription(r0.radioCells[a], ThemeDescription.FLAG_SELECTORWHITE, null, null, null, null, Theme.key_windowBackgroundWhite));
                arrayList.add(new ThemeDescription(r0.radioCells[a], ThemeDescription.FLAG_SELECTORWHITE, null, null, null, null, Theme.key_listSelector));
                arrayList.add(new ThemeDescription(r0.radioCells[a], 0, new Class[]{RadioCell.class}, new String[]{"textView"}, null, null, null, Theme.key_windowBackgroundWhiteBlackText));
                arrayList.add(new ThemeDescription(r0.radioCells[a], ThemeDescription.FLAG_CHECKBOX, new Class[]{RadioCell.class}, new String[]{"radioButton"}, null, null, null, Theme.key_radioBackground));
                arrayList.add(new ThemeDescription(r0.radioCells[a], ThemeDescription.FLAG_CHECKBOXCHECK, new Class[]{RadioCell.class}, new String[]{"radioButton"}, null, null, null, Theme.key_radioBackgroundChecked));
            }
        } else {
            arrayList.add(new ThemeDescription(null, 0, new Class[]{RadioCell.class}, new String[]{"textView"}, null, null, null, Theme.key_windowBackgroundWhiteBlackText));
            arrayList.add(new ThemeDescription(null, ThemeDescription.FLAG_CHECKBOX, new Class[]{RadioCell.class}, new String[]{"radioButton"}, null, null, null, Theme.key_radioBackground));
            arrayList.add(new ThemeDescription(null, ThemeDescription.FLAG_CHECKBOXCHECK, new Class[]{RadioCell.class}, new String[]{"radioButton"}, null, null, null, Theme.key_radioBackgroundChecked));
        }
        for (a = 0; a < r0.headerCell.length; a++) {
            arrayList.add(new ThemeDescription(r0.headerCell[a], ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_windowBackgroundWhite));
            arrayList.add(new ThemeDescription(r0.headerCell[a], 0, new Class[]{HeaderCell.class}, new String[]{"textView"}, null, null, null, Theme.key_windowBackgroundWhiteBlueHeader));
        }
        for (View themeDescription : r0.sectionCell) {
            arrayList.add(new ThemeDescription(themeDescription, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{ShadowSectionCell.class}, null, null, null, Theme.key_windowBackgroundGrayShadow));
        }
        for (a = 0; a < r0.bottomCell.length; a++) {
            arrayList.add(new ThemeDescription(r0.bottomCell[a], ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{TextInfoPrivacyCell.class}, null, null, null, Theme.key_windowBackgroundGrayShadow));
            arrayList.add(new ThemeDescription(r0.bottomCell[a], 0, new Class[]{TextInfoPrivacyCell.class}, new String[]{"textView"}, null, null, null, Theme.key_windowBackgroundWhiteGrayText4));
            arrayList.add(new ThemeDescription(r0.bottomCell[a], ThemeDescription.FLAG_LINKCOLOR, new Class[]{TextInfoPrivacyCell.class}, new String[]{"textView"}, null, null, null, Theme.key_windowBackgroundWhiteLinkText));
        }
        for (a = 0; a < r0.dividers.size(); a++) {
            arrayList.add(new ThemeDescription((View) r0.dividers.get(a), ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_divider));
        }
        arrayList.add(new ThemeDescription(r0.textView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, Theme.key_windowBackgroundWhiteBlackText));
        arrayList.add(new ThemeDescription(r0.checkCell1, 0, new Class[]{TextCheckCell.class}, new String[]{"textView"}, null, null, null, Theme.key_windowBackgroundWhiteBlackText));
        arrayList.add(new ThemeDescription(r0.checkCell1, 0, new Class[]{TextCheckCell.class}, new String[]{"checkBox"}, null, null, null, Theme.key_switchThumb));
        arrayList.add(new ThemeDescription(r0.checkCell1, 0, new Class[]{TextCheckCell.class}, new String[]{"checkBox"}, null, null, null, Theme.key_switchTrack));
        arrayList.add(new ThemeDescription(r0.checkCell1, 0, new Class[]{TextCheckCell.class}, new String[]{"checkBox"}, null, null, null, Theme.key_switchThumbChecked));
        arrayList.add(new ThemeDescription(r0.checkCell1, 0, new Class[]{TextCheckCell.class}, new String[]{"checkBox"}, null, null, null, Theme.key_switchTrackChecked));
        arrayList.add(new ThemeDescription(r0.checkCell1, ThemeDescription.FLAG_SELECTORWHITE, null, null, null, null, Theme.key_windowBackgroundWhite));
        arrayList.add(new ThemeDescription(r0.checkCell1, ThemeDescription.FLAG_SELECTORWHITE, null, null, null, null, Theme.key_listSelector));
        arrayList.add(new ThemeDescription(r0.settingsCell1, ThemeDescription.FLAG_SELECTORWHITE, null, null, null, null, Theme.key_windowBackgroundWhite));
        arrayList.add(new ThemeDescription(r0.settingsCell1, ThemeDescription.FLAG_SELECTORWHITE, null, null, null, null, Theme.key_listSelector));
        arrayList.add(new ThemeDescription(r0.settingsCell1, 0, new Class[]{TextSettingsCell.class}, new String[]{"textView"}, null, null, null, Theme.key_windowBackgroundWhiteBlackText));
        arrayList.add(new ThemeDescription(r0.payTextView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, Theme.key_windowBackgroundWhiteBlueText6));
        arrayList.add(new ThemeDescription(r0.linearLayout2, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{TextPriceCell.class}, null, null, null, Theme.key_windowBackgroundWhite));
        arrayList.add(new ThemeDescription(r0.linearLayout2, ThemeDescription.FLAG_CHECKTAG, new Class[]{TextPriceCell.class}, new String[]{"textView"}, null, null, null, Theme.key_windowBackgroundWhiteBlackText));
        arrayList.add(new ThemeDescription(r0.linearLayout2, ThemeDescription.FLAG_CHECKTAG, new Class[]{TextPriceCell.class}, new String[]{"valueTextView"}, null, null, null, Theme.key_windowBackgroundWhiteBlackText));
        arrayList.add(new ThemeDescription(r0.linearLayout2, ThemeDescription.FLAG_CHECKTAG, new Class[]{TextPriceCell.class}, new String[]{"textView"}, null, null, null, Theme.key_windowBackgroundWhiteGrayText2));
        arrayList.add(new ThemeDescription(r0.linearLayout2, ThemeDescription.FLAG_CHECKTAG, new Class[]{TextPriceCell.class}, new String[]{"valueTextView"}, null, null, null, Theme.key_windowBackgroundWhiteGrayText2));
        arrayList.add(new ThemeDescription(r0.detailSettingsCell[0], ThemeDescription.FLAG_SELECTORWHITE, null, null, null, null, Theme.key_windowBackgroundWhite));
        arrayList.add(new ThemeDescription(r0.detailSettingsCell[0], ThemeDescription.FLAG_SELECTORWHITE, null, null, null, null, Theme.key_listSelector));
        for (a = 1; a < r0.detailSettingsCell.length; a++) {
            arrayList.add(new ThemeDescription(r0.detailSettingsCell[a], ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_windowBackgroundWhite));
            arrayList.add(new ThemeDescription(r0.detailSettingsCell[a], 0, new Class[]{TextDetailSettingsCell.class}, new String[]{"textView"}, null, null, null, Theme.key_windowBackgroundWhiteBlackText));
            arrayList.add(new ThemeDescription(r0.detailSettingsCell[a], 0, new Class[]{TextDetailSettingsCell.class}, new String[]{"valueTextView"}, null, null, null, Theme.key_windowBackgroundWhiteGrayText2));
        }
        arrayList.add(new ThemeDescription(r0.paymentInfoCell, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_windowBackgroundWhite));
        arrayList.add(new ThemeDescription(r0.paymentInfoCell, 0, new Class[]{PaymentInfoCell.class}, new String[]{"nameTextView"}, null, null, null, Theme.key_windowBackgroundWhiteBlackText));
        arrayList.add(new ThemeDescription(r0.paymentInfoCell, 0, new Class[]{PaymentInfoCell.class}, new String[]{"detailTextView"}, null, null, null, Theme.key_windowBackgroundWhiteBlackText));
        arrayList.add(new ThemeDescription(r0.paymentInfoCell, 0, new Class[]{PaymentInfoCell.class}, new String[]{"detailExTextView"}, null, null, null, Theme.key_windowBackgroundWhiteGrayText2));
        arrayList.add(new ThemeDescription(r0.bottomLayout, ThemeDescription.FLAG_SELECTORWHITE, null, null, null, null, Theme.key_windowBackgroundWhite));
        arrayList.add(new ThemeDescription(r0.bottomLayout, ThemeDescription.FLAG_SELECTORWHITE, null, null, null, null, Theme.key_listSelector));
        return (ThemeDescription[]) arrayList.toArray(new ThemeDescription[arrayList.size()]);
    }
}
