package org.telegram.ui.Components;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Calendar;
import java.util.Locale;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.MessagesStorage.IntCallback;
import org.telegram.messenger.NotificationsController;
import org.telegram.messenger.SecretChatHelper;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.UserObject;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.beta.R;
import org.telegram.messenger.browser.Browser;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.EncryptedChat;
import org.telegram.tgnet.TLRPC.InputPeer;
import org.telegram.tgnet.TLRPC.TL_account_reportPeer;
import org.telegram.tgnet.TLRPC.TL_dialog;
import org.telegram.tgnet.TLRPC.TL_error;
import org.telegram.tgnet.TLRPC.TL_inputReportReasonPornography;
import org.telegram.tgnet.TLRPC.TL_inputReportReasonSpam;
import org.telegram.tgnet.TLRPC.TL_inputReportReasonViolence;
import org.telegram.tgnet.TLRPC.TL_messages_report;
import org.telegram.tgnet.TLRPC.TL_peerNotifySettings;
import org.telegram.tgnet.TLRPC.User;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.AlertDialog.Builder;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.BottomSheet;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.CacheControlActivity;
import org.telegram.ui.Cells.AccountSelectCell;
import org.telegram.ui.Cells.RadioColorCell;
import org.telegram.ui.Cells.TextColorCell;
import org.telegram.ui.Components.NumberPicker.Formatter;
import org.telegram.ui.Components.NumberPicker.OnScrollListener;
import org.telegram.ui.Components.NumberPicker.OnValueChangeListener;
import org.telegram.ui.LaunchActivity;
import org.telegram.ui.ReportOtherActivity;

public class AlertsCreator {

    public interface AccountSelectDelegate {
        void didSelectAccount(int i);
    }

    public interface DatePickerDelegate {
        void didSelectDate(int i, int i2, int i3);
    }

    public interface PaymentAlertDelegate {
        void didPressedNewCard();
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static android.app.Dialog processError(int r7, org.telegram.tgnet.TLRPC.TL_error r8, org.telegram.ui.ActionBar.BaseFragment r9, org.telegram.tgnet.TLObject r10, java.lang.Object... r11) {
        /*
        r6 = 2131493292; // 0x7f0c01ac float:1.861006E38 double:1.05309761E-314;
        r2 = 1;
        r5 = 2131493488; // 0x7f0c0270 float:1.8610458E38 double:1.053097707E-314;
        r4 = 2131493586; // 0x7f0c02d2 float:1.8610656E38 double:1.053097755E-314;
        r1 = 0;
        r0 = r8.code;
        r3 = 406; // 0x196 float:5.69E-43 double:2.006E-321;
        if (r0 == r3) goto L_0x0015;
    L_0x0011:
        r0 = r8.text;
        if (r0 != 0) goto L_0x0017;
    L_0x0015:
        r0 = 0;
    L_0x0016:
        return r0;
    L_0x0017:
        r0 = r10 instanceof org.telegram.tgnet.TLRPC.TL_account_saveSecureValue;
        if (r0 != 0) goto L_0x001f;
    L_0x001b:
        r0 = r10 instanceof org.telegram.tgnet.TLRPC.TL_account_getAuthorizationForm;
        if (r0 == 0) goto L_0x0074;
    L_0x001f:
        r0 = r8.text;
        r1 = "PHONE_NUMBER_INVALID";
        r0 = r0.contains(r1);
        if (r0 == 0) goto L_0x0039;
    L_0x002a:
        r0 = "InvalidPhoneNumber";
        r1 = 2131493725; // 0x7f0c035d float:1.8610938E38 double:1.053097824E-314;
        r0 = org.telegram.messenger.LocaleController.getString(r0, r1);
        showSimpleAlert(r9, r0);
    L_0x0037:
        r0 = 0;
        goto L_0x0016;
    L_0x0039:
        r0 = r8.text;
        r1 = "FLOOD_WAIT";
        r0 = r0.startsWith(r1);
        if (r0 == 0) goto L_0x004f;
    L_0x0044:
        r0 = "FloodWait";
        r0 = org.telegram.messenger.LocaleController.getString(r0, r4);
        showSimpleAlert(r9, r0);
        goto L_0x0037;
    L_0x004f:
        r0 = new java.lang.StringBuilder;
        r0.<init>();
        r1 = "ErrorOccurred";
        r1 = org.telegram.messenger.LocaleController.getString(r1, r5);
        r0 = r0.append(r1);
        r1 = "\n";
        r0 = r0.append(r1);
        r1 = r8.text;
        r0 = r0.append(r1);
        r0 = r0.toString();
        showSimpleAlert(r9, r0);
        goto L_0x0037;
    L_0x0074:
        r0 = r10 instanceof org.telegram.tgnet.TLRPC.TL_channels_joinChannel;
        if (r0 != 0) goto L_0x008c;
    L_0x0078:
        r0 = r10 instanceof org.telegram.tgnet.TLRPC.TL_channels_editAdmin;
        if (r0 != 0) goto L_0x008c;
    L_0x007c:
        r0 = r10 instanceof org.telegram.tgnet.TLRPC.TL_channels_inviteToChannel;
        if (r0 != 0) goto L_0x008c;
    L_0x0080:
        r0 = r10 instanceof org.telegram.tgnet.TLRPC.TL_messages_addChatUser;
        if (r0 != 0) goto L_0x008c;
    L_0x0084:
        r0 = r10 instanceof org.telegram.tgnet.TLRPC.TL_messages_startBot;
        if (r0 != 0) goto L_0x008c;
    L_0x0088:
        r0 = r10 instanceof org.telegram.tgnet.TLRPC.TL_channels_editBanned;
        if (r0 == 0) goto L_0x00ba;
    L_0x008c:
        if (r9 == 0) goto L_0x009c;
    L_0x008e:
        r2 = r8.text;
        r0 = r11[r1];
        r0 = (java.lang.Boolean) r0;
        r0 = r0.booleanValue();
        showAddUserAlert(r2, r9, r0);
        goto L_0x0037;
    L_0x009c:
        r0 = r8.text;
        r3 = "PEER_FLOOD";
        r0 = r0.equals(r3);
        if (r0 == 0) goto L_0x0037;
    L_0x00a7:
        r0 = org.telegram.messenger.NotificationCenter.getInstance(r7);
        r3 = org.telegram.messenger.NotificationCenter.needShowAlert;
        r4 = new java.lang.Object[r2];
        r2 = java.lang.Integer.valueOf(r2);
        r4[r1] = r2;
        r0.postNotificationName(r3, r4);
        goto L_0x0037;
    L_0x00ba:
        r0 = r10 instanceof org.telegram.tgnet.TLRPC.TL_messages_createChat;
        if (r0 == 0) goto L_0x00d7;
    L_0x00be:
        r0 = r8.text;
        r2 = "FLOOD_WAIT";
        r0 = r0.startsWith(r2);
        if (r0 == 0) goto L_0x00d0;
    L_0x00c9:
        r0 = r8.text;
        showFloodWaitAlert(r0, r9);
        goto L_0x0037;
    L_0x00d0:
        r0 = r8.text;
        showAddUserAlert(r0, r9, r1);
        goto L_0x0037;
    L_0x00d7:
        r0 = r10 instanceof org.telegram.tgnet.TLRPC.TL_channels_createChannel;
        if (r0 == 0) goto L_0x00f4;
    L_0x00db:
        r0 = r8.text;
        r2 = "FLOOD_WAIT";
        r0 = r0.startsWith(r2);
        if (r0 == 0) goto L_0x00ed;
    L_0x00e6:
        r0 = r8.text;
        showFloodWaitAlert(r0, r9);
        goto L_0x0037;
    L_0x00ed:
        r0 = r8.text;
        showAddUserAlert(r0, r9, r1);
        goto L_0x0037;
    L_0x00f4:
        r0 = r10 instanceof org.telegram.tgnet.TLRPC.TL_messages_editMessage;
        if (r0 == 0) goto L_0x0123;
    L_0x00f8:
        r0 = r8.text;
        r1 = "MESSAGE_NOT_MODIFIED";
        r0 = r0.equals(r1);
        if (r0 != 0) goto L_0x0037;
    L_0x0103:
        if (r9 == 0) goto L_0x0114;
    L_0x0105:
        r0 = "EditMessageError";
        r1 = 2131493450; // 0x7f0c024a float:1.861038E38 double:1.053097688E-314;
        r0 = org.telegram.messenger.LocaleController.getString(r0, r1);
        showSimpleAlert(r9, r0);
        goto L_0x0037;
    L_0x0114:
        r0 = "EditMessageError";
        r1 = 2131493450; // 0x7f0c024a float:1.861038E38 double:1.053097688E-314;
        r0 = org.telegram.messenger.LocaleController.getString(r0, r1);
        showSimpleToast(r9, r0);
        goto L_0x0037;
    L_0x0123:
        r0 = r10 instanceof org.telegram.tgnet.TLRPC.TL_messages_sendMessage;
        if (r0 != 0) goto L_0x0137;
    L_0x0127:
        r0 = r10 instanceof org.telegram.tgnet.TLRPC.TL_messages_sendMedia;
        if (r0 != 0) goto L_0x0137;
    L_0x012b:
        r0 = r10 instanceof org.telegram.tgnet.TLRPC.TL_messages_sendBroadcast;
        if (r0 != 0) goto L_0x0137;
    L_0x012f:
        r0 = r10 instanceof org.telegram.tgnet.TLRPC.TL_messages_sendInlineBotResult;
        if (r0 != 0) goto L_0x0137;
    L_0x0133:
        r0 = r10 instanceof org.telegram.tgnet.TLRPC.TL_messages_forwardMessages;
        if (r0 == 0) goto L_0x0155;
    L_0x0137:
        r0 = r8.text;
        r3 = "PEER_FLOOD";
        r0 = r0.equals(r3);
        if (r0 == 0) goto L_0x0037;
    L_0x0142:
        r0 = org.telegram.messenger.NotificationCenter.getInstance(r7);
        r3 = org.telegram.messenger.NotificationCenter.needShowAlert;
        r2 = new java.lang.Object[r2];
        r4 = java.lang.Integer.valueOf(r1);
        r2[r1] = r4;
        r0.postNotificationName(r3, r2);
        goto L_0x0037;
    L_0x0155:
        r0 = r10 instanceof org.telegram.tgnet.TLRPC.TL_messages_importChatInvite;
        if (r0 == 0) goto L_0x0199;
    L_0x0159:
        r0 = r8.text;
        r1 = "FLOOD_WAIT";
        r0 = r0.startsWith(r1);
        if (r0 == 0) goto L_0x0170;
    L_0x0164:
        r0 = "FloodWait";
        r0 = org.telegram.messenger.LocaleController.getString(r0, r4);
        showSimpleAlert(r9, r0);
        goto L_0x0037;
    L_0x0170:
        r0 = r8.text;
        r1 = "USERS_TOO_MUCH";
        r0 = r0.equals(r1);
        if (r0 == 0) goto L_0x018a;
    L_0x017b:
        r0 = "JoinToGroupErrorFull";
        r1 = 2131493754; // 0x7f0c037a float:1.8610997E38 double:1.053097838E-314;
        r0 = org.telegram.messenger.LocaleController.getString(r0, r1);
        showSimpleAlert(r9, r0);
        goto L_0x0037;
    L_0x018a:
        r0 = "JoinToGroupErrorNotExist";
        r1 = 2131493755; // 0x7f0c037b float:1.8611E38 double:1.0530978387E-314;
        r0 = org.telegram.messenger.LocaleController.getString(r0, r1);
        showSimpleAlert(r9, r0);
        goto L_0x0037;
    L_0x0199:
        r0 = r10 instanceof org.telegram.tgnet.TLRPC.TL_messages_getAttachedStickers;
        if (r0 == 0) goto L_0x01d3;
    L_0x019d:
        if (r9 == 0) goto L_0x0037;
    L_0x019f:
        r0 = r9.getParentActivity();
        if (r0 == 0) goto L_0x0037;
    L_0x01a5:
        r0 = r9.getParentActivity();
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r3 = "ErrorOccurred";
        r3 = org.telegram.messenger.LocaleController.getString(r3, r5);
        r2 = r2.append(r3);
        r3 = "\n";
        r2 = r2.append(r3);
        r3 = r8.text;
        r2 = r2.append(r3);
        r2 = r2.toString();
        r0 = android.widget.Toast.makeText(r0, r2, r1);
        r0.show();
        goto L_0x0037;
    L_0x01d3:
        r0 = r10 instanceof org.telegram.tgnet.TLRPC.TL_account_confirmPhone;
        if (r0 != 0) goto L_0x01df;
    L_0x01d7:
        r0 = r10 instanceof org.telegram.tgnet.TLRPC.TL_account_verifyPhone;
        if (r0 != 0) goto L_0x01df;
    L_0x01db:
        r0 = r10 instanceof org.telegram.tgnet.TLRPC.TL_account_verifyEmail;
        if (r0 == 0) goto L_0x025e;
    L_0x01df:
        r0 = r8.text;
        r1 = "PHONE_CODE_EMPTY";
        r0 = r0.contains(r1);
        if (r0 != 0) goto L_0x020b;
    L_0x01ea:
        r0 = r8.text;
        r1 = "PHONE_CODE_INVALID";
        r0 = r0.contains(r1);
        if (r0 != 0) goto L_0x020b;
    L_0x01f5:
        r0 = r8.text;
        r1 = "CODE_INVALID";
        r0 = r0.contains(r1);
        if (r0 != 0) goto L_0x020b;
    L_0x0200:
        r0 = r8.text;
        r1 = "CODE_EMPTY";
        r0 = r0.contains(r1);
        if (r0 == 0) goto L_0x021b;
    L_0x020b:
        r0 = "InvalidCode";
        r1 = 2131493722; // 0x7f0c035a float:1.8610932E38 double:1.0530978224E-314;
        r0 = org.telegram.messenger.LocaleController.getString(r0, r1);
        r0 = showSimpleAlert(r9, r0);
        goto L_0x0016;
    L_0x021b:
        r0 = r8.text;
        r1 = "PHONE_CODE_EXPIRED";
        r0 = r0.contains(r1);
        if (r0 != 0) goto L_0x0231;
    L_0x0226:
        r0 = r8.text;
        r1 = "EMAIL_VERIFY_EXPIRED";
        r0 = r0.contains(r1);
        if (r0 == 0) goto L_0x023e;
    L_0x0231:
        r0 = "CodeExpired";
        r0 = org.telegram.messenger.LocaleController.getString(r0, r6);
        r0 = showSimpleAlert(r9, r0);
        goto L_0x0016;
    L_0x023e:
        r0 = r8.text;
        r1 = "FLOOD_WAIT";
        r0 = r0.startsWith(r1);
        if (r0 == 0) goto L_0x0256;
    L_0x0249:
        r0 = "FloodWait";
        r0 = org.telegram.messenger.LocaleController.getString(r0, r4);
        r0 = showSimpleAlert(r9, r0);
        goto L_0x0016;
    L_0x0256:
        r0 = r8.text;
        r0 = showSimpleAlert(r9, r0);
        goto L_0x0016;
    L_0x025e:
        r0 = r10 instanceof org.telegram.tgnet.TLRPC.TL_auth_resendCode;
        if (r0 == 0) goto L_0x0300;
    L_0x0262:
        r0 = r8.text;
        r1 = "PHONE_NUMBER_INVALID";
        r0 = r0.contains(r1);
        if (r0 == 0) goto L_0x027d;
    L_0x026d:
        r0 = "InvalidPhoneNumber";
        r1 = 2131493725; // 0x7f0c035d float:1.8610938E38 double:1.053097824E-314;
        r0 = org.telegram.messenger.LocaleController.getString(r0, r1);
        r0 = showSimpleAlert(r9, r0);
        goto L_0x0016;
    L_0x027d:
        r0 = r8.text;
        r1 = "PHONE_CODE_EMPTY";
        r0 = r0.contains(r1);
        if (r0 != 0) goto L_0x0293;
    L_0x0288:
        r0 = r8.text;
        r1 = "PHONE_CODE_INVALID";
        r0 = r0.contains(r1);
        if (r0 == 0) goto L_0x02a3;
    L_0x0293:
        r0 = "InvalidCode";
        r1 = 2131493722; // 0x7f0c035a float:1.8610932E38 double:1.0530978224E-314;
        r0 = org.telegram.messenger.LocaleController.getString(r0, r1);
        r0 = showSimpleAlert(r9, r0);
        goto L_0x0016;
    L_0x02a3:
        r0 = r8.text;
        r1 = "PHONE_CODE_EXPIRED";
        r0 = r0.contains(r1);
        if (r0 == 0) goto L_0x02bb;
    L_0x02ae:
        r0 = "CodeExpired";
        r0 = org.telegram.messenger.LocaleController.getString(r0, r6);
        r0 = showSimpleAlert(r9, r0);
        goto L_0x0016;
    L_0x02bb:
        r0 = r8.text;
        r1 = "FLOOD_WAIT";
        r0 = r0.startsWith(r1);
        if (r0 == 0) goto L_0x02d3;
    L_0x02c6:
        r0 = "FloodWait";
        r0 = org.telegram.messenger.LocaleController.getString(r0, r4);
        r0 = showSimpleAlert(r9, r0);
        goto L_0x0016;
    L_0x02d3:
        r0 = r8.code;
        r1 = -1000; // 0xfffffffffffffc18 float:NaN double:NaN;
        if (r0 == r1) goto L_0x0037;
    L_0x02d9:
        r0 = new java.lang.StringBuilder;
        r0.<init>();
        r1 = "ErrorOccurred";
        r1 = org.telegram.messenger.LocaleController.getString(r1, r5);
        r0 = r0.append(r1);
        r1 = "\n";
        r0 = r0.append(r1);
        r1 = r8.text;
        r0 = r0.append(r1);
        r0 = r0.toString();
        r0 = showSimpleAlert(r9, r0);
        goto L_0x0016;
    L_0x0300:
        r0 = r10 instanceof org.telegram.tgnet.TLRPC.TL_account_sendConfirmPhoneCode;
        if (r0 == 0) goto L_0x0343;
    L_0x0304:
        r0 = r8.code;
        r1 = 400; // 0x190 float:5.6E-43 double:1.976E-321;
        if (r0 != r1) goto L_0x031a;
    L_0x030a:
        r0 = "CancelLinkExpired";
        r1 = 2131493152; // 0x7f0c0120 float:1.8609776E38 double:1.0530975407E-314;
        r0 = org.telegram.messenger.LocaleController.getString(r0, r1);
        r0 = showSimpleAlert(r9, r0);
        goto L_0x0016;
    L_0x031a:
        r0 = r8.text;
        if (r0 == 0) goto L_0x0037;
    L_0x031e:
        r0 = r8.text;
        r1 = "FLOOD_WAIT";
        r0 = r0.startsWith(r1);
        if (r0 == 0) goto L_0x0336;
    L_0x0329:
        r0 = "FloodWait";
        r0 = org.telegram.messenger.LocaleController.getString(r0, r4);
        r0 = showSimpleAlert(r9, r0);
        goto L_0x0016;
    L_0x0336:
        r0 = "ErrorOccurred";
        r0 = org.telegram.messenger.LocaleController.getString(r0, r5);
        r0 = showSimpleAlert(r9, r0);
        goto L_0x0016;
    L_0x0343:
        r0 = r10 instanceof org.telegram.tgnet.TLRPC.TL_account_changePhone;
        if (r0 == 0) goto L_0x03bb;
    L_0x0347:
        r0 = r8.text;
        r1 = "PHONE_NUMBER_INVALID";
        r0 = r0.contains(r1);
        if (r0 == 0) goto L_0x0361;
    L_0x0352:
        r0 = "InvalidPhoneNumber";
        r1 = 2131493725; // 0x7f0c035d float:1.8610938E38 double:1.053097824E-314;
        r0 = org.telegram.messenger.LocaleController.getString(r0, r1);
        showSimpleAlert(r9, r0);
        goto L_0x0037;
    L_0x0361:
        r0 = r8.text;
        r1 = "PHONE_CODE_EMPTY";
        r0 = r0.contains(r1);
        if (r0 != 0) goto L_0x0377;
    L_0x036c:
        r0 = r8.text;
        r1 = "PHONE_CODE_INVALID";
        r0 = r0.contains(r1);
        if (r0 == 0) goto L_0x0386;
    L_0x0377:
        r0 = "InvalidCode";
        r1 = 2131493722; // 0x7f0c035a float:1.8610932E38 double:1.0530978224E-314;
        r0 = org.telegram.messenger.LocaleController.getString(r0, r1);
        showSimpleAlert(r9, r0);
        goto L_0x0037;
    L_0x0386:
        r0 = r8.text;
        r1 = "PHONE_CODE_EXPIRED";
        r0 = r0.contains(r1);
        if (r0 == 0) goto L_0x039d;
    L_0x0391:
        r0 = "CodeExpired";
        r0 = org.telegram.messenger.LocaleController.getString(r0, r6);
        showSimpleAlert(r9, r0);
        goto L_0x0037;
    L_0x039d:
        r0 = r8.text;
        r1 = "FLOOD_WAIT";
        r0 = r0.startsWith(r1);
        if (r0 == 0) goto L_0x03b4;
    L_0x03a8:
        r0 = "FloodWait";
        r0 = org.telegram.messenger.LocaleController.getString(r0, r4);
        showSimpleAlert(r9, r0);
        goto L_0x0037;
    L_0x03b4:
        r0 = r8.text;
        showSimpleAlert(r9, r0);
        goto L_0x0037;
    L_0x03bb:
        r0 = r10 instanceof org.telegram.tgnet.TLRPC.TL_account_sendChangePhoneCode;
        if (r0 == 0) goto L_0x045a;
    L_0x03bf:
        r0 = r8.text;
        r3 = "PHONE_NUMBER_INVALID";
        r0 = r0.contains(r3);
        if (r0 == 0) goto L_0x03d9;
    L_0x03ca:
        r0 = "InvalidPhoneNumber";
        r1 = 2131493725; // 0x7f0c035d float:1.8610938E38 double:1.053097824E-314;
        r0 = org.telegram.messenger.LocaleController.getString(r0, r1);
        showSimpleAlert(r9, r0);
        goto L_0x0037;
    L_0x03d9:
        r0 = r8.text;
        r3 = "PHONE_CODE_EMPTY";
        r0 = r0.contains(r3);
        if (r0 != 0) goto L_0x03ef;
    L_0x03e4:
        r0 = r8.text;
        r3 = "PHONE_CODE_INVALID";
        r0 = r0.contains(r3);
        if (r0 == 0) goto L_0x03fe;
    L_0x03ef:
        r0 = "InvalidCode";
        r1 = 2131493722; // 0x7f0c035a float:1.8610932E38 double:1.0530978224E-314;
        r0 = org.telegram.messenger.LocaleController.getString(r0, r1);
        showSimpleAlert(r9, r0);
        goto L_0x0037;
    L_0x03fe:
        r0 = r8.text;
        r3 = "PHONE_CODE_EXPIRED";
        r0 = r0.contains(r3);
        if (r0 == 0) goto L_0x0415;
    L_0x0409:
        r0 = "CodeExpired";
        r0 = org.telegram.messenger.LocaleController.getString(r0, r6);
        showSimpleAlert(r9, r0);
        goto L_0x0037;
    L_0x0415:
        r0 = r8.text;
        r3 = "FLOOD_WAIT";
        r0 = r0.startsWith(r3);
        if (r0 == 0) goto L_0x042c;
    L_0x0420:
        r0 = "FloodWait";
        r0 = org.telegram.messenger.LocaleController.getString(r0, r4);
        showSimpleAlert(r9, r0);
        goto L_0x0037;
    L_0x042c:
        r0 = r8.text;
        r3 = "PHONE_NUMBER_OCCUPIED";
        r0 = r0.startsWith(r3);
        if (r0 == 0) goto L_0x044e;
    L_0x0437:
        r3 = "ChangePhoneNumberOccupied";
        r4 = 2131493164; // 0x7f0c012c float:1.86098E38 double:1.0530975467E-314;
        r2 = new java.lang.Object[r2];
        r0 = r11[r1];
        r0 = (java.lang.String) r0;
        r2[r1] = r0;
        r0 = org.telegram.messenger.LocaleController.formatString(r3, r4, r2);
        showSimpleAlert(r9, r0);
        goto L_0x0037;
    L_0x044e:
        r0 = "ErrorOccurred";
        r0 = org.telegram.messenger.LocaleController.getString(r0, r5);
        showSimpleAlert(r9, r0);
        goto L_0x0037;
    L_0x045a:
        r0 = r10 instanceof org.telegram.tgnet.TLRPC.TL_updateUserName;
        if (r0 == 0) goto L_0x04ab;
    L_0x045e:
        r3 = r8.text;
        r0 = -1;
        r4 = r3.hashCode();
        switch(r4) {
            case 288843630: goto L_0x0478;
            case 533175271: goto L_0x0482;
            default: goto L_0x0468;
        };
    L_0x0468:
        r1 = r0;
    L_0x0469:
        switch(r1) {
            case 0: goto L_0x048d;
            case 1: goto L_0x049c;
            default: goto L_0x046c;
        };
    L_0x046c:
        r0 = "ErrorOccurred";
        r0 = org.telegram.messenger.LocaleController.getString(r0, r5);
        showSimpleAlert(r9, r0);
        goto L_0x0037;
    L_0x0478:
        r2 = "USERNAME_INVALID";
        r2 = r3.equals(r2);
        if (r2 == 0) goto L_0x0468;
    L_0x0481:
        goto L_0x0469;
    L_0x0482:
        r1 = "USERNAME_OCCUPIED";
        r1 = r3.equals(r1);
        if (r1 == 0) goto L_0x0468;
    L_0x048b:
        r1 = r2;
        goto L_0x0469;
    L_0x048d:
        r0 = "UsernameInvalid";
        r1 = 2131494758; // 0x7f0c0766 float:1.8613033E38 double:1.053098334E-314;
        r0 = org.telegram.messenger.LocaleController.getString(r0, r1);
        showSimpleAlert(r9, r0);
        goto L_0x0037;
    L_0x049c:
        r0 = "UsernameInUse";
        r1 = 2131494757; // 0x7f0c0765 float:1.8613031E38 double:1.0530983337E-314;
        r0 = org.telegram.messenger.LocaleController.getString(r0, r1);
        showSimpleAlert(r9, r0);
        goto L_0x0037;
    L_0x04ab:
        r0 = r10 instanceof org.telegram.tgnet.TLRPC.TL_contacts_importContacts;
        if (r0 == 0) goto L_0x04ee;
    L_0x04af:
        if (r8 == 0) goto L_0x04bc;
    L_0x04b1:
        r0 = r8.text;
        r1 = "FLOOD_WAIT";
        r0 = r0.startsWith(r1);
        if (r0 == 0) goto L_0x04c8;
    L_0x04bc:
        r0 = "FloodWait";
        r0 = org.telegram.messenger.LocaleController.getString(r0, r4);
        showSimpleAlert(r9, r0);
        goto L_0x0037;
    L_0x04c8:
        r0 = new java.lang.StringBuilder;
        r0.<init>();
        r1 = "ErrorOccurred";
        r1 = org.telegram.messenger.LocaleController.getString(r1, r5);
        r0 = r0.append(r1);
        r1 = "\n";
        r0 = r0.append(r1);
        r1 = r8.text;
        r0 = r0.append(r1);
        r0 = r0.toString();
        showSimpleAlert(r9, r0);
        goto L_0x0037;
    L_0x04ee:
        r0 = r10 instanceof org.telegram.tgnet.TLRPC.TL_account_getPassword;
        if (r0 != 0) goto L_0x04f6;
    L_0x04f2:
        r0 = r10 instanceof org.telegram.tgnet.TLRPC.TL_account_getTmpPassword;
        if (r0 == 0) goto L_0x0513;
    L_0x04f6:
        r0 = r8.text;
        r1 = "FLOOD_WAIT";
        r0 = r0.startsWith(r1);
        if (r0 == 0) goto L_0x050c;
    L_0x0501:
        r0 = r8.text;
        r0 = getFloodWaitString(r0);
        showSimpleToast(r9, r0);
        goto L_0x0037;
    L_0x050c:
        r0 = r8.text;
        showSimpleToast(r9, r0);
        goto L_0x0037;
    L_0x0513:
        r0 = r10 instanceof org.telegram.tgnet.TLRPC.TL_payments_sendPaymentForm;
        if (r0 == 0) goto L_0x055f;
    L_0x0517:
        r3 = r8.text;
        r0 = -1;
        r4 = r3.hashCode();
        switch(r4) {
            case -1144062453: goto L_0x052c;
            case -784238410: goto L_0x0536;
            default: goto L_0x0521;
        };
    L_0x0521:
        r1 = r0;
    L_0x0522:
        switch(r1) {
            case 0: goto L_0x0541;
            case 1: goto L_0x0550;
            default: goto L_0x0525;
        };
    L_0x0525:
        r0 = r8.text;
        showSimpleToast(r9, r0);
        goto L_0x0037;
    L_0x052c:
        r2 = "BOT_PRECHECKOUT_FAILED";
        r2 = r3.equals(r2);
        if (r2 == 0) goto L_0x0521;
    L_0x0535:
        goto L_0x0522;
    L_0x0536:
        r1 = "PAYMENT_FAILED";
        r1 = r3.equals(r1);
        if (r1 == 0) goto L_0x0521;
    L_0x053f:
        r1 = r2;
        goto L_0x0522;
    L_0x0541:
        r0 = "PaymentPrecheckoutFailed";
        r1 = 2131494267; // 0x7f0c057b float:1.8612038E38 double:1.0530980916E-314;
        r0 = org.telegram.messenger.LocaleController.getString(r0, r1);
        showSimpleToast(r9, r0);
        goto L_0x0037;
    L_0x0550:
        r0 = "PaymentFailed";
        r1 = 2131494254; // 0x7f0c056e float:1.8612011E38 double:1.053098085E-314;
        r0 = org.telegram.messenger.LocaleController.getString(r0, r1);
        showSimpleToast(r9, r0);
        goto L_0x0037;
    L_0x055f:
        r0 = r10 instanceof org.telegram.tgnet.TLRPC.TL_payments_validateRequestedInfo;
        if (r0 == 0) goto L_0x0037;
    L_0x0563:
        r2 = r8.text;
        r0 = -1;
        r3 = r2.hashCode();
        switch(r3) {
            case 1758025548: goto L_0x0577;
            default: goto L_0x056d;
        };
    L_0x056d:
        switch(r0) {
            case 0: goto L_0x0582;
            default: goto L_0x0570;
        };
    L_0x0570:
        r0 = r8.text;
        showSimpleToast(r9, r0);
        goto L_0x0037;
    L_0x0577:
        r3 = "SHIPPING_NOT_AVAILABLE";
        r2 = r2.equals(r3);
        if (r2 == 0) goto L_0x056d;
    L_0x0580:
        r0 = r1;
        goto L_0x056d;
    L_0x0582:
        r0 = "PaymentNoShippingMethod";
        r1 = 2131494256; // 0x7f0c0570 float:1.8612015E38 double:1.053098086E-314;
        r0 = org.telegram.messenger.LocaleController.getString(r0, r1);
        showSimpleToast(r9, r0);
        goto L_0x0037;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.ui.Components.AlertsCreator.processError(int, org.telegram.tgnet.TLRPC$TL_error, org.telegram.ui.ActionBar.BaseFragment, org.telegram.tgnet.TLObject, java.lang.Object[]):android.app.Dialog");
    }

    public static Toast showSimpleToast(BaseFragment baseFragment, String text) {
        if (text == null) {
            return null;
        }
        Context context;
        if (baseFragment == null || baseFragment.getParentActivity() == null) {
            context = ApplicationLoader.applicationContext;
        } else {
            context = baseFragment.getParentActivity();
        }
        Toast toast = Toast.makeText(context, text, 1);
        toast.show();
        return toast;
    }

    public static AlertDialog showUpdateAppAlert(final Context context, String text, boolean updateApp) {
        if (context == null || text == null) {
            return null;
        }
        Builder builder = new Builder(context);
        builder.setTitle(LocaleController.getString("AppName", R.string.AppName));
        builder.setMessage(text);
        builder.setPositiveButton(LocaleController.getString("OK", R.string.OK), null);
        if (updateApp) {
            builder.setNegativeButton(LocaleController.getString("UpdateApp", R.string.UpdateApp), new OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Browser.openUrl(context, BuildVars.PLAYSTORE_APP_URL);
                }
            });
        }
        return builder.show();
    }

    public static Builder createSimpleAlert(Context context, String text) {
        if (text == null) {
            return null;
        }
        Builder builder = new Builder(context);
        builder.setTitle(LocaleController.getString("AppName", R.string.AppName));
        builder.setMessage(text);
        builder.setPositiveButton(LocaleController.getString("OK", R.string.OK), null);
        return builder;
    }

    public static Dialog showSimpleAlert(BaseFragment baseFragment, String text) {
        if (text == null || baseFragment == null || baseFragment.getParentActivity() == null) {
            return null;
        }
        Dialog dialog = createSimpleAlert(baseFragment.getParentActivity(), text).create();
        baseFragment.showDialog(dialog);
        return dialog;
    }

    private static void updateDayPicker(NumberPicker dayPicker, NumberPicker monthPicker, NumberPicker yearPicker) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2, monthPicker.getValue());
        calendar.set(1, yearPicker.getValue());
        dayPicker.setMinValue(1);
        dayPicker.setMaxValue(calendar.getActualMaximum(5));
    }

    private static void checkPickerDate(NumberPicker dayPicker, NumberPicker monthPicker, NumberPicker yearPicker) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        int currentYear = calendar.get(1);
        int currentMonth = calendar.get(2);
        int currentDay = calendar.get(5);
        if (currentYear > yearPicker.getValue()) {
            yearPicker.setValue(currentYear);
        }
        if (yearPicker.getValue() == currentYear) {
            if (currentMonth > monthPicker.getValue()) {
                monthPicker.setValue(currentMonth);
            }
            if (currentMonth == monthPicker.getValue() && currentDay > dayPicker.getValue()) {
                dayPicker.setValue(currentDay);
            }
        }
    }

    public static Builder createDatePickerDialog(Context context, int minYear, int maxYear, String title, final boolean checkMinDate, DatePickerDelegate datePickerDelegate) {
        if (context == null) {
            return null;
        }
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(0);
        linearLayout.setWeightSum(1.0f);
        final NumberPicker monthPicker = new NumberPicker(context);
        final NumberPicker dayPicker = new NumberPicker(context);
        final NumberPicker yearPicker = new NumberPicker(context);
        monthPicker.setMinValue(0);
        monthPicker.setMaxValue(11);
        linearLayout.addView(monthPicker, LayoutHelper.createLinear(0, -2, 0.4f));
        monthPicker.setFormatter(new Formatter() {
            public String format(int value) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(2, value);
                return calendar.getDisplayName(2, 1, Locale.getDefault());
            }
        });
        monthPicker.setOnValueChangedListener(new OnValueChangeListener() {
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                AlertsCreator.updateDayPicker(dayPicker, monthPicker, yearPicker);
            }
        });
        monthPicker.setOnScrollListener(new OnScrollListener() {
            public void onScrollStateChange(NumberPicker view, int scrollState) {
                if (checkMinDate && scrollState == 0) {
                    AlertsCreator.checkPickerDate(dayPicker, monthPicker, yearPicker);
                }
            }
        });
        linearLayout.addView(dayPicker, LayoutHelper.createLinear(0, -2, 0.2f));
        dayPicker.setOnScrollListener(new OnScrollListener() {
            public void onScrollStateChange(NumberPicker view, int scrollState) {
                if (checkMinDate && scrollState == 0) {
                    AlertsCreator.checkPickerDate(dayPicker, monthPicker, yearPicker);
                }
            }
        });
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        int currentYear = calendar.get(1);
        yearPicker.setMinValue(currentYear + minYear);
        yearPicker.setMaxValue(currentYear + maxYear);
        yearPicker.setValue(currentYear);
        linearLayout.addView(yearPicker, LayoutHelper.createLinear(0, -2, 0.4f));
        yearPicker.setOnValueChangedListener(new OnValueChangeListener() {
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                AlertsCreator.updateDayPicker(dayPicker, monthPicker, yearPicker);
            }
        });
        yearPicker.setOnScrollListener(new OnScrollListener() {
            public void onScrollStateChange(NumberPicker view, int scrollState) {
                if (checkMinDate && scrollState == 0) {
                    AlertsCreator.checkPickerDate(dayPicker, monthPicker, yearPicker);
                }
            }
        });
        updateDayPicker(dayPicker, monthPicker, yearPicker);
        if (checkMinDate) {
            checkPickerDate(dayPicker, monthPicker, yearPicker);
        }
        Builder builder = new Builder(context);
        builder.setTitle(title);
        builder.setView(linearLayout);
        final boolean z = checkMinDate;
        final DatePickerDelegate datePickerDelegate2 = datePickerDelegate;
        builder.setPositiveButton(LocaleController.getString("Set", R.string.Set), new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (z) {
                    AlertsCreator.checkPickerDate(dayPicker, monthPicker, yearPicker);
                }
                datePickerDelegate2.didSelectDate(yearPicker.getValue(), monthPicker.getValue(), dayPicker.getValue());
            }
        });
        builder.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null);
        return builder;
    }

    public static Dialog createMuteAlert(Context context, final long dialog_id) {
        if (context == null) {
            return null;
        }
        BottomSheet.Builder builder = new BottomSheet.Builder(context);
        builder.setTitle(LocaleController.getString("Notifications", R.string.Notifications));
        CharSequence[] items = new CharSequence[4];
        items[0] = LocaleController.formatString("MuteFor", R.string.MuteFor, LocaleController.formatPluralString("Hours", 1));
        items[1] = LocaleController.formatString("MuteFor", R.string.MuteFor, LocaleController.formatPluralString("Hours", 8));
        items[2] = LocaleController.formatString("MuteFor", R.string.MuteFor, LocaleController.formatPluralString("Days", 2));
        items[3] = LocaleController.getString("MuteDisable", R.string.MuteDisable);
        builder.setItems(items, new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                long flags;
                int untilTime = ConnectionsManager.getInstance(UserConfig.selectedAccount).getCurrentTime();
                if (i == 0) {
                    untilTime += 3600;
                } else if (i == 1) {
                    untilTime += 28800;
                } else if (i == 2) {
                    untilTime += 172800;
                } else if (i == 3) {
                    untilTime = ConnectionsManager.DEFAULT_DATACENTER_ID;
                }
                Editor editor = MessagesController.getNotificationsSettings(UserConfig.selectedAccount).edit();
                if (i == 3) {
                    editor.putInt("notify2_" + dialog_id, 2);
                    flags = 1;
                } else {
                    editor.putInt("notify2_" + dialog_id, 3);
                    editor.putInt("notifyuntil_" + dialog_id, untilTime);
                    flags = (((long) untilTime) << 32) | 1;
                }
                NotificationsController.getInstance(UserConfig.selectedAccount).removeNotificationsForDialog(dialog_id);
                MessagesStorage.getInstance(UserConfig.selectedAccount).setDialogFlags(dialog_id, flags);
                editor.commit();
                TL_dialog dialog = (TL_dialog) MessagesController.getInstance(UserConfig.selectedAccount).dialogs_dict.get(dialog_id);
                if (dialog != null) {
                    dialog.notify_settings = new TL_peerNotifySettings();
                    dialog.notify_settings.mute_until = untilTime;
                }
                NotificationsController.getInstance(UserConfig.selectedAccount).updateServerNotificationsSettings(dialog_id);
            }
        });
        return builder.create();
    }

    public static Dialog createReportAlert(Context context, long dialog_id, int messageId, BaseFragment parentFragment) {
        if (context == null || parentFragment == null) {
            return null;
        }
        BottomSheet.Builder builder = new BottomSheet.Builder(context);
        builder.setTitle(LocaleController.getString("ReportChat", R.string.ReportChat));
        final long j = dialog_id;
        final int i = messageId;
        final BaseFragment baseFragment = parentFragment;
        final Context context2 = context;
        builder.setItems(new CharSequence[]{LocaleController.getString("ReportChatSpam", R.string.ReportChatSpam), LocaleController.getString("ReportChatViolence", R.string.ReportChatViolence), LocaleController.getString("ReportChatPornography", R.string.ReportChatPornography), LocaleController.getString("ReportChatOther", R.string.ReportChatOther)}, new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == 3) {
                    Bundle args = new Bundle();
                    args.putLong("dialog_id", j);
                    args.putLong("message_id", (long) i);
                    baseFragment.presentFragment(new ReportOtherActivity(args));
                    return;
                }
                TLObject req;
                InputPeer peer = MessagesController.getInstance(UserConfig.selectedAccount).getInputPeer((int) j);
                TLObject request;
                if (i != 0) {
                    request = new TL_messages_report();
                    request.peer = peer;
                    request.id.add(Integer.valueOf(i));
                    if (i == 0) {
                        request.reason = new TL_inputReportReasonSpam();
                    } else if (i == 1) {
                        request.reason = new TL_inputReportReasonViolence();
                    } else if (i == 2) {
                        request.reason = new TL_inputReportReasonPornography();
                    }
                    req = request;
                } else {
                    request = new TL_account_reportPeer();
                    request.peer = peer;
                    if (i == 0) {
                        request.reason = new TL_inputReportReasonSpam();
                    } else if (i == 1) {
                        request.reason = new TL_inputReportReasonViolence();
                    } else if (i == 2) {
                        request.reason = new TL_inputReportReasonPornography();
                    }
                    req = request;
                }
                ConnectionsManager.getInstance(UserConfig.selectedAccount).sendRequest(req, new RequestDelegate() {
                    public void run(TLObject response, TL_error error) {
                    }
                });
                Toast.makeText(context2, LocaleController.getString("ReportChatSent", R.string.ReportChatSent), 0).show();
            }
        });
        return builder.create();
    }

    private static String getFloodWaitString(String error) {
        String timeString;
        int time = Utilities.parseInt(error).intValue();
        if (time < 60) {
            timeString = LocaleController.formatPluralString("Seconds", time);
        } else {
            timeString = LocaleController.formatPluralString("Minutes", time / 60);
        }
        return LocaleController.formatString("FloodWaitTime", R.string.FloodWaitTime, timeString);
    }

    public static void showFloodWaitAlert(String error, BaseFragment fragment) {
        if (error != null && error.startsWith("FLOOD_WAIT") && fragment != null && fragment.getParentActivity() != null) {
            String timeString;
            int time = Utilities.parseInt(error).intValue();
            if (time < 60) {
                timeString = LocaleController.formatPluralString("Seconds", time);
            } else {
                timeString = LocaleController.formatPluralString("Minutes", time / 60);
            }
            Builder builder = new Builder(fragment.getParentActivity());
            builder.setTitle(LocaleController.getString("AppName", R.string.AppName));
            builder.setMessage(LocaleController.formatString("FloodWaitTime", R.string.FloodWaitTime, timeString));
            builder.setPositiveButton(LocaleController.getString("OK", R.string.OK), null);
            fragment.showDialog(builder.create(), true, null);
        }
    }

    public static void showSendMediaAlert(int result, BaseFragment fragment) {
        if (result != 0) {
            Builder builder = new Builder(fragment.getParentActivity());
            builder.setTitle(LocaleController.getString("AppName", R.string.AppName));
            if (result == 1) {
                builder.setMessage(LocaleController.getString("ErrorSendRestrictedStickers", R.string.ErrorSendRestrictedStickers));
            } else if (result == 2) {
                builder.setMessage(LocaleController.getString("ErrorSendRestrictedMedia", R.string.ErrorSendRestrictedMedia));
            }
            builder.setPositiveButton(LocaleController.getString("OK", R.string.OK), null);
            fragment.showDialog(builder.create(), true, null);
        }
    }

    public static void showAddUserAlert(String error, final BaseFragment fragment, boolean isChannel) {
        if (error != null && fragment != null && fragment.getParentActivity() != null) {
            Builder builder = new Builder(fragment.getParentActivity());
            builder.setTitle(LocaleController.getString("AppName", R.string.AppName));
            boolean z = true;
            switch (error.hashCode()) {
                case -1763467626:
                    if (error.equals("USERS_TOO_FEW")) {
                        z = true;
                        break;
                    }
                    break;
                case -538116776:
                    if (error.equals("USER_BLOCKED")) {
                        z = true;
                        break;
                    }
                    break;
                case -512775857:
                    if (error.equals("USER_RESTRICTED")) {
                        z = true;
                        break;
                    }
                    break;
                case -454039871:
                    if (error.equals("PEER_FLOOD")) {
                        z = false;
                        break;
                    }
                    break;
                case -420079733:
                    if (error.equals("BOTS_TOO_MUCH")) {
                        z = true;
                        break;
                    }
                    break;
                case 98635865:
                    if (error.equals("USER_KICKED")) {
                        z = true;
                        break;
                    }
                    break;
                case 517420851:
                    if (error.equals("USER_BOT")) {
                        z = true;
                        break;
                    }
                    break;
                case 845559454:
                    if (error.equals("YOU_BLOCKED_USER")) {
                        z = true;
                        break;
                    }
                    break;
                case 916342611:
                    if (error.equals("USER_ADMIN_INVALID")) {
                        z = true;
                        break;
                    }
                    break;
                case 1047173446:
                    if (error.equals("CHAT_ADMIN_BAN_REQUIRED")) {
                        z = true;
                        break;
                    }
                    break;
                case 1167301807:
                    if (error.equals("USERS_TOO_MUCH")) {
                        z = true;
                        break;
                    }
                    break;
                case 1227003815:
                    if (error.equals("USER_ID_INVALID")) {
                        z = true;
                        break;
                    }
                    break;
                case 1253103379:
                    if (error.equals("ADMINS_TOO_MUCH")) {
                        z = true;
                        break;
                    }
                    break;
                case 1623167701:
                    if (error.equals("USER_NOT_MUTUAL_CONTACT")) {
                        z = true;
                        break;
                    }
                    break;
                case 1754587486:
                    if (error.equals("CHAT_ADMIN_INVITE_REQUIRED")) {
                        z = true;
                        break;
                    }
                    break;
                case 1916725894:
                    if (error.equals("USER_PRIVACY_RESTRICTED")) {
                        z = true;
                        break;
                    }
                    break;
            }
            switch (z) {
                case false:
                    builder.setMessage(LocaleController.getString("NobodyLikesSpam2", R.string.NobodyLikesSpam2));
                    builder.setNegativeButton(LocaleController.getString("MoreInfo", R.string.MoreInfo), new OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            MessagesController.getInstance(fragment.getCurrentAccount()).openByUserName("spambot", fragment, 1);
                        }
                    });
                    break;
                case true:
                case true:
                case true:
                    if (!isChannel) {
                        builder.setMessage(LocaleController.getString("GroupUserCantAdd", R.string.GroupUserCantAdd));
                        break;
                    } else {
                        builder.setMessage(LocaleController.getString("ChannelUserCantAdd", R.string.ChannelUserCantAdd));
                        break;
                    }
                case true:
                    if (!isChannel) {
                        builder.setMessage(LocaleController.getString("GroupUserAddLimit", R.string.GroupUserAddLimit));
                        break;
                    } else {
                        builder.setMessage(LocaleController.getString("ChannelUserAddLimit", R.string.ChannelUserAddLimit));
                        break;
                    }
                case true:
                    if (!isChannel) {
                        builder.setMessage(LocaleController.getString("GroupUserLeftError", R.string.GroupUserLeftError));
                        break;
                    } else {
                        builder.setMessage(LocaleController.getString("ChannelUserLeftError", R.string.ChannelUserLeftError));
                        break;
                    }
                case true:
                    if (!isChannel) {
                        builder.setMessage(LocaleController.getString("GroupUserCantAdmin", R.string.GroupUserCantAdmin));
                        break;
                    } else {
                        builder.setMessage(LocaleController.getString("ChannelUserCantAdmin", R.string.ChannelUserCantAdmin));
                        break;
                    }
                case true:
                    if (!isChannel) {
                        builder.setMessage(LocaleController.getString("GroupUserCantBot", R.string.GroupUserCantBot));
                        break;
                    } else {
                        builder.setMessage(LocaleController.getString("ChannelUserCantBot", R.string.ChannelUserCantBot));
                        break;
                    }
                case true:
                    if (!isChannel) {
                        builder.setMessage(LocaleController.getString("InviteToGroupError", R.string.InviteToGroupError));
                        break;
                    } else {
                        builder.setMessage(LocaleController.getString("InviteToChannelError", R.string.InviteToChannelError));
                        break;
                    }
                case true:
                    builder.setMessage(LocaleController.getString("CreateGroupError", R.string.CreateGroupError));
                    break;
                case true:
                    builder.setMessage(LocaleController.getString("UserRestricted", R.string.UserRestricted));
                    break;
                case true:
                    builder.setMessage(LocaleController.getString("YouBlockedUser", R.string.YouBlockedUser));
                    break;
                case true:
                case true:
                    builder.setMessage(LocaleController.getString("AddAdminErrorBlacklisted", R.string.AddAdminErrorBlacklisted));
                    break;
                case true:
                    builder.setMessage(LocaleController.getString("AddAdminErrorNotAMember", R.string.AddAdminErrorNotAMember));
                    break;
                case true:
                    builder.setMessage(LocaleController.getString("AddBannedErrorAdmin", R.string.AddBannedErrorAdmin));
                    break;
                default:
                    builder.setMessage(LocaleController.getString("ErrorOccurred", R.string.ErrorOccurred) + "\n" + error);
                    break;
            }
            builder.setPositiveButton(LocaleController.getString("OK", R.string.OK), null);
            fragment.showDialog(builder.create(), true, null);
        }
    }

    public static Dialog createColorSelectDialog(Activity parentActivity, long dialog_id, boolean globalGroup, boolean globalAll, Runnable onSelect) {
        int currentColor;
        SharedPreferences preferences = MessagesController.getNotificationsSettings(UserConfig.selectedAccount);
        if (globalGroup) {
            currentColor = preferences.getInt("GroupLed", -16776961);
        } else if (globalAll) {
            currentColor = preferences.getInt("MessagesLed", -16776961);
        } else {
            if (preferences.contains("color_" + dialog_id)) {
                currentColor = preferences.getInt("color_" + dialog_id, -16776961);
            } else if (((int) dialog_id) < 0) {
                currentColor = preferences.getInt("GroupLed", -16776961);
            } else {
                currentColor = preferences.getInt("MessagesLed", -16776961);
            }
        }
        View linearLayout = new LinearLayout(parentActivity);
        linearLayout.setOrientation(1);
        String[] descriptions = new String[]{LocaleController.getString("ColorRed", R.string.ColorRed), LocaleController.getString("ColorOrange", R.string.ColorOrange), LocaleController.getString("ColorYellow", R.string.ColorYellow), LocaleController.getString("ColorGreen", R.string.ColorGreen), LocaleController.getString("ColorCyan", R.string.ColorCyan), LocaleController.getString("ColorBlue", R.string.ColorBlue), LocaleController.getString("ColorViolet", R.string.ColorViolet), LocaleController.getString("ColorPink", R.string.ColorPink), LocaleController.getString("ColorWhite", R.string.ColorWhite)};
        final int[] selectedColor = new int[]{currentColor};
        for (int a = 0; a < 9; a++) {
            RadioColorCell cell = new RadioColorCell(parentActivity);
            cell.setPadding(AndroidUtilities.dp(4.0f), 0, AndroidUtilities.dp(4.0f), 0);
            cell.setTag(Integer.valueOf(a));
            cell.setCheckColor(TextColorCell.colors[a], TextColorCell.colors[a]);
            cell.setTextAndValue(descriptions[a], currentColor == TextColorCell.colorsToSave[a]);
            linearLayout.addView(cell);
            linearLayout = linearLayout;
            cell.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    int count = linearLayout.getChildCount();
                    for (int a = 0; a < count; a++) {
                        boolean z;
                        View cell = (RadioColorCell) linearLayout.getChildAt(a);
                        if (cell == v) {
                            z = true;
                        } else {
                            z = false;
                        }
                        cell.setChecked(z, true);
                    }
                    selectedColor[0] = TextColorCell.colorsToSave[((Integer) v.getTag()).intValue()];
                }
            });
        }
        Builder builder = new Builder((Context) parentActivity);
        builder.setTitle(LocaleController.getString("LedColor", R.string.LedColor));
        builder.setView(linearLayout);
        final boolean z = globalAll;
        final boolean z2 = globalGroup;
        final long j = dialog_id;
        final Runnable runnable = onSelect;
        builder.setPositiveButton(LocaleController.getString("Set", R.string.Set), new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int which) {
                Editor editor = MessagesController.getNotificationsSettings(UserConfig.selectedAccount).edit();
                if (z) {
                    editor.putInt("MessagesLed", selectedColor[0]);
                } else if (z2) {
                    editor.putInt("GroupLed", selectedColor[0]);
                } else {
                    editor.putInt("color_" + j, selectedColor[0]);
                }
                editor.commit();
                if (runnable != null) {
                    runnable.run();
                }
            }
        });
        final boolean z3 = globalAll;
        final boolean z4 = globalGroup;
        final long j2 = dialog_id;
        final Runnable runnable2 = onSelect;
        builder.setNeutralButton(LocaleController.getString("LedDisabled", R.string.LedDisabled), new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Editor editor = MessagesController.getNotificationsSettings(UserConfig.selectedAccount).edit();
                if (z3) {
                    editor.putInt("MessagesLed", 0);
                } else if (z4) {
                    editor.putInt("GroupLed", 0);
                } else {
                    editor.putInt("color_" + j2, 0);
                }
                editor.commit();
                if (runnable2 != null) {
                    runnable2.run();
                }
            }
        });
        if (!(globalAll || globalGroup)) {
            final long j3 = dialog_id;
            final Runnable runnable3 = onSelect;
            builder.setNegativeButton(LocaleController.getString("Default", R.string.Default), new OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Editor editor = MessagesController.getNotificationsSettings(UserConfig.selectedAccount).edit();
                    editor.remove("color_" + j3);
                    editor.commit();
                    if (runnable3 != null) {
                        runnable3.run();
                    }
                }
            });
        }
        return builder.create();
    }

    public static Dialog createVibrationSelectDialog(Activity parentActivity, BaseFragment parentFragment, long dialog_id, boolean globalGroup, boolean globalAll, Runnable onSelect) {
        String prefix;
        if (dialog_id != 0) {
            prefix = "vibrate_";
        } else {
            prefix = globalGroup ? "vibrate_group" : "vibrate_messages";
        }
        return createVibrationSelectDialog(parentActivity, parentFragment, dialog_id, prefix, onSelect);
    }

    public static Dialog createVibrationSelectDialog(Activity parentActivity, BaseFragment parentFragment, long dialog_id, String prefKeyPrefix, Runnable onSelect) {
        String[] descriptions;
        SharedPreferences preferences = MessagesController.getNotificationsSettings(UserConfig.selectedAccount);
        final int[] selected = new int[1];
        if (dialog_id != 0) {
            selected[0] = preferences.getInt(prefKeyPrefix + dialog_id, 0);
            if (selected[0] == 3) {
                selected[0] = 2;
            } else if (selected[0] == 2) {
                selected[0] = 3;
            }
            descriptions = new String[]{LocaleController.getString("VibrationDefault", R.string.VibrationDefault), LocaleController.getString("Short", R.string.Short), LocaleController.getString("Long", R.string.Long), LocaleController.getString("VibrationDisabled", R.string.VibrationDisabled)};
        } else {
            selected[0] = preferences.getInt(prefKeyPrefix, 0);
            if (selected[0] == 0) {
                selected[0] = 1;
            } else if (selected[0] == 1) {
                selected[0] = 2;
            } else if (selected[0] == 2) {
                selected[0] = 0;
            }
            descriptions = new String[]{LocaleController.getString("VibrationDisabled", R.string.VibrationDisabled), LocaleController.getString("VibrationDefault", R.string.VibrationDefault), LocaleController.getString("Short", R.string.Short), LocaleController.getString("Long", R.string.Long), LocaleController.getString("OnlyIfSilent", R.string.OnlyIfSilent)};
        }
        LinearLayout linearLayout = new LinearLayout(parentActivity);
        linearLayout.setOrientation(1);
        int a = 0;
        while (a < descriptions.length) {
            RadioColorCell cell = new RadioColorCell(parentActivity);
            cell.setPadding(AndroidUtilities.dp(4.0f), 0, AndroidUtilities.dp(4.0f), 0);
            cell.setTag(Integer.valueOf(a));
            cell.setCheckColor(Theme.getColor(Theme.key_radioBackground), Theme.getColor(Theme.key_dialogRadioBackgroundChecked));
            cell.setTextAndValue(descriptions[a], selected[0] == a);
            linearLayout.addView(cell);
            final long j = dialog_id;
            final String str = prefKeyPrefix;
            final BaseFragment baseFragment = parentFragment;
            final Runnable runnable = onSelect;
            cell.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    selected[0] = ((Integer) v.getTag()).intValue();
                    Editor editor = MessagesController.getNotificationsSettings(UserConfig.selectedAccount).edit();
                    if (j != 0) {
                        if (selected[0] == 0) {
                            editor.putInt(str + j, 0);
                        } else if (selected[0] == 1) {
                            editor.putInt(str + j, 1);
                        } else if (selected[0] == 2) {
                            editor.putInt(str + j, 3);
                        } else if (selected[0] == 3) {
                            editor.putInt(str + j, 2);
                        }
                    } else if (selected[0] == 0) {
                        editor.putInt(str, 2);
                    } else if (selected[0] == 1) {
                        editor.putInt(str, 0);
                    } else if (selected[0] == 2) {
                        editor.putInt(str, 1);
                    } else if (selected[0] == 3) {
                        editor.putInt(str, 3);
                    } else if (selected[0] == 4) {
                        editor.putInt(str, 4);
                    }
                    editor.commit();
                    if (baseFragment != null) {
                        baseFragment.dismissCurrentDialig();
                    }
                    if (runnable != null) {
                        runnable.run();
                    }
                }
            });
            a++;
        }
        Builder builder = new Builder((Context) parentActivity);
        builder.setTitle(LocaleController.getString("Vibrate", R.string.Vibrate));
        builder.setView(linearLayout);
        builder.setPositiveButton(LocaleController.getString("Cancel", R.string.Cancel), null);
        return builder.create();
    }

    public static Dialog createLocationUpdateDialog(Activity parentActivity, User user, IntCallback callback) {
        final int[] selected = new int[1];
        String[] descriptions = new String[]{LocaleController.getString("SendLiveLocationFor15m", R.string.SendLiveLocationFor15m), LocaleController.getString("SendLiveLocationFor1h", R.string.SendLiveLocationFor1h), LocaleController.getString("SendLiveLocationFor8h", R.string.SendLiveLocationFor8h)};
        final LinearLayout linearLayout = new LinearLayout(parentActivity);
        linearLayout.setOrientation(1);
        TextView titleTextView = new TextView(parentActivity);
        if (user != null) {
            titleTextView.setText(LocaleController.formatString("LiveLocationAlertPrivate", R.string.LiveLocationAlertPrivate, UserObject.getFirstName(user)));
        } else {
            titleTextView.setText(LocaleController.getString("LiveLocationAlertGroup", R.string.LiveLocationAlertGroup));
        }
        titleTextView.setTextColor(Theme.getColor(Theme.key_dialogTextBlack));
        titleTextView.setTextSize(1, 16.0f);
        titleTextView.setGravity((LocaleController.isRTL ? 5 : 3) | 48);
        linearLayout.addView(titleTextView, LayoutHelper.createLinear(-2, -2, (LocaleController.isRTL ? 5 : 3) | 48, 24, 0, 24, 8));
        int a = 0;
        while (a < descriptions.length) {
            RadioColorCell cell = new RadioColorCell(parentActivity);
            cell.setPadding(AndroidUtilities.dp(4.0f), 0, AndroidUtilities.dp(4.0f), 0);
            cell.setTag(Integer.valueOf(a));
            cell.setCheckColor(Theme.getColor(Theme.key_radioBackground), Theme.getColor(Theme.key_dialogRadioBackgroundChecked));
            cell.setTextAndValue(descriptions[a], selected[0] == a);
            linearLayout.addView(cell);
            cell.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    selected[0] = ((Integer) v.getTag()).intValue();
                    int count = linearLayout.getChildCount();
                    for (int a = 0; a < count; a++) {
                        View child = linearLayout.getChildAt(a);
                        if (child instanceof RadioColorCell) {
                            boolean z;
                            RadioColorCell radioColorCell = (RadioColorCell) child;
                            if (child == v) {
                                z = true;
                            } else {
                                z = false;
                            }
                            radioColorCell.setChecked(z, true);
                        }
                    }
                }
            });
            a++;
        }
        Builder builder = new Builder((Context) parentActivity);
        builder.setTopImage(new ShareLocationDrawable(parentActivity, false), Theme.getColor(Theme.key_dialogTopBackground));
        builder.setView(linearLayout);
        final IntCallback intCallback = callback;
        builder.setPositiveButton(LocaleController.getString("ShareFile", R.string.ShareFile), new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                int time;
                if (selected[0] == 0) {
                    time = 900;
                } else if (selected[0] == 1) {
                    time = 3600;
                } else {
                    time = 28800;
                }
                intCallback.run(time);
            }
        });
        builder.setNeutralButton(LocaleController.getString("Cancel", R.string.Cancel), null);
        return builder.create();
    }

    public static Builder createContactsPermissionDialog(Activity parentActivity, final IntCallback callback) {
        Builder builder = new Builder((Context) parentActivity);
        builder.setTopImage((int) R.drawable.permissions_contacts, Theme.getColor(Theme.key_dialogTopBackground));
        builder.setMessage(AndroidUtilities.replaceTags(LocaleController.getString("ContactsPermissionAlert", R.string.ContactsPermissionAlert)));
        builder.setPositiveButton(LocaleController.getString("ContactsPermissionAlertContinue", R.string.ContactsPermissionAlertContinue), new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                callback.run(1);
            }
        });
        builder.setNegativeButton(LocaleController.getString("ContactsPermissionAlertNotNow", R.string.ContactsPermissionAlertNotNow), new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                callback.run(0);
            }
        });
        return builder;
    }

    public static Dialog createFreeSpaceDialog(LaunchActivity parentActivity) {
        final int[] selected = new int[1];
        int keepMedia = MessagesController.getGlobalMainSettings().getInt("keep_media", 2);
        if (keepMedia == 2) {
            selected[0] = 3;
        } else if (keepMedia == 0) {
            selected[0] = 1;
        } else if (keepMedia == 1) {
            selected[0] = 2;
        } else if (keepMedia == 3) {
            selected[0] = 0;
        }
        String[] descriptions = new String[]{LocaleController.formatPluralString("Days", 3), LocaleController.formatPluralString("Weeks", 1), LocaleController.formatPluralString("Months", 1), LocaleController.getString("LowDiskSpaceNeverRemove", R.string.LowDiskSpaceNeverRemove)};
        final LinearLayout linearLayout = new LinearLayout(parentActivity);
        linearLayout.setOrientation(1);
        View titleTextView = new TextView(parentActivity);
        titleTextView.setText(LocaleController.getString("LowDiskSpaceTitle2", R.string.LowDiskSpaceTitle2));
        titleTextView.setTextColor(Theme.getColor(Theme.key_dialogTextBlack));
        titleTextView.setTextSize(1, 16.0f);
        titleTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        titleTextView.setGravity((LocaleController.isRTL ? 5 : 3) | 48);
        linearLayout.addView(titleTextView, LayoutHelper.createLinear(-2, -2, (LocaleController.isRTL ? 5 : 3) | 48, 24, 0, 24, 8));
        int a = 0;
        while (a < descriptions.length) {
            RadioColorCell cell = new RadioColorCell(parentActivity);
            cell.setPadding(AndroidUtilities.dp(4.0f), 0, AndroidUtilities.dp(4.0f), 0);
            cell.setTag(Integer.valueOf(a));
            cell.setCheckColor(Theme.getColor(Theme.key_radioBackground), Theme.getColor(Theme.key_dialogRadioBackgroundChecked));
            cell.setTextAndValue(descriptions[a], selected[0] == a);
            linearLayout.addView(cell);
            cell.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    int num = ((Integer) v.getTag()).intValue();
                    if (num == 0) {
                        selected[0] = 3;
                    } else if (num == 1) {
                        selected[0] = 0;
                    } else if (num == 2) {
                        selected[0] = 1;
                    } else if (num == 3) {
                        selected[0] = 2;
                    }
                    int count = linearLayout.getChildCount();
                    for (int a = 0; a < count; a++) {
                        View child = linearLayout.getChildAt(a);
                        if (child instanceof RadioColorCell) {
                            boolean z;
                            RadioColorCell radioColorCell = (RadioColorCell) child;
                            if (child == v) {
                                z = true;
                            } else {
                                z = false;
                            }
                            radioColorCell.setChecked(z, true);
                        }
                    }
                }
            });
            a++;
        }
        Builder builder = new Builder((Context) parentActivity);
        builder.setTitle(LocaleController.getString("LowDiskSpaceTitle", R.string.LowDiskSpaceTitle));
        builder.setMessage(LocaleController.getString("LowDiskSpaceMessage", R.string.LowDiskSpaceMessage));
        builder.setView(linearLayout);
        builder.setPositiveButton(LocaleController.getString("OK", R.string.OK), new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                MessagesController.getGlobalMainSettings().edit().putInt("keep_media", selected[0]).commit();
            }
        });
        final LaunchActivity launchActivity = parentActivity;
        builder.setNeutralButton(LocaleController.getString("ClearMediaCache", R.string.ClearMediaCache), new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                launchActivity.presentFragment(new CacheControlActivity());
            }
        });
        return builder.create();
    }

    public static Dialog createPrioritySelectDialog(Activity parentActivity, BaseFragment parentFragment, long dialog_id, boolean globalGroup, boolean globalAll, Runnable onSelect) {
        String[] descriptions;
        SharedPreferences preferences = MessagesController.getNotificationsSettings(UserConfig.selectedAccount);
        final int[] selected = new int[1];
        if (dialog_id != 0) {
            selected[0] = preferences.getInt("priority_" + dialog_id, 3);
            if (selected[0] == 3) {
                selected[0] = 0;
            } else if (selected[0] == 4) {
                selected[0] = 1;
            } else if (selected[0] == 5) {
                selected[0] = 2;
            } else if (selected[0] == 0) {
                selected[0] = 3;
            } else {
                selected[0] = 4;
            }
            descriptions = new String[]{LocaleController.getString("NotificationsPrioritySettings", R.string.NotificationsPrioritySettings), LocaleController.getString("NotificationsPriorityLow", R.string.NotificationsPriorityLow), LocaleController.getString("NotificationsPriorityMedium", R.string.NotificationsPriorityMedium), LocaleController.getString("NotificationsPriorityHigh", R.string.NotificationsPriorityHigh), LocaleController.getString("NotificationsPriorityUrgent", R.string.NotificationsPriorityUrgent)};
        } else {
            if (globalAll) {
                selected[0] = preferences.getInt("priority_messages", 1);
            } else if (globalGroup) {
                selected[0] = preferences.getInt("priority_group", 1);
            }
            if (selected[0] == 4) {
                selected[0] = 0;
            } else if (selected[0] == 5) {
                selected[0] = 1;
            } else if (selected[0] == 0) {
                selected[0] = 2;
            } else {
                selected[0] = 3;
            }
            descriptions = new String[]{LocaleController.getString("NotificationsPriorityLow", R.string.NotificationsPriorityLow), LocaleController.getString("NotificationsPriorityMedium", R.string.NotificationsPriorityMedium), LocaleController.getString("NotificationsPriorityHigh", R.string.NotificationsPriorityHigh), LocaleController.getString("NotificationsPriorityUrgent", R.string.NotificationsPriorityUrgent)};
        }
        LinearLayout linearLayout = new LinearLayout(parentActivity);
        linearLayout.setOrientation(1);
        int a = 0;
        while (a < descriptions.length) {
            RadioColorCell cell = new RadioColorCell(parentActivity);
            cell.setPadding(AndroidUtilities.dp(4.0f), 0, AndroidUtilities.dp(4.0f), 0);
            cell.setTag(Integer.valueOf(a));
            cell.setCheckColor(Theme.getColor(Theme.key_radioBackground), Theme.getColor(Theme.key_dialogRadioBackgroundChecked));
            cell.setTextAndValue(descriptions[a], selected[0] == a);
            linearLayout.addView(cell);
            final long j = dialog_id;
            final boolean z = globalGroup;
            final BaseFragment baseFragment = parentFragment;
            final Runnable runnable = onSelect;
            cell.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    selected[0] = ((Integer) v.getTag()).intValue();
                    Editor editor = MessagesController.getNotificationsSettings(UserConfig.selectedAccount).edit();
                    int option;
                    if (j != 0) {
                        if (selected[0] == 0) {
                            option = 3;
                        } else if (selected[0] == 1) {
                            option = 4;
                        } else if (selected[0] == 2) {
                            option = 5;
                        } else if (selected[0] == 3) {
                            option = 0;
                        } else {
                            option = 1;
                        }
                        editor.putInt("priority_" + j, option);
                    } else {
                        String str;
                        if (selected[0] == 0) {
                            option = 4;
                        } else if (selected[0] == 1) {
                            option = 5;
                        } else if (selected[0] == 2) {
                            option = 0;
                        } else {
                            option = 1;
                        }
                        if (z) {
                            str = "priority_group";
                        } else {
                            str = "priority_messages";
                        }
                        editor.putInt(str, option);
                    }
                    editor.commit();
                    if (baseFragment != null) {
                        baseFragment.dismissCurrentDialig();
                    }
                    if (runnable != null) {
                        runnable.run();
                    }
                }
            });
            a++;
        }
        Builder builder = new Builder((Context) parentActivity);
        builder.setTitle(LocaleController.getString("NotificationsImportance", R.string.NotificationsImportance));
        builder.setView(linearLayout);
        builder.setPositiveButton(LocaleController.getString("Cancel", R.string.Cancel), null);
        return builder.create();
    }

    public static Dialog createPopupSelectDialog(Activity parentActivity, final BaseFragment parentFragment, final boolean globalGroup, boolean globalAll, final Runnable onSelect) {
        SharedPreferences preferences = MessagesController.getNotificationsSettings(UserConfig.selectedAccount);
        final int[] selected = new int[1];
        if (globalAll) {
            selected[0] = preferences.getInt("popupAll", 0);
        } else if (globalGroup) {
            selected[0] = preferences.getInt("popupGroup", 0);
        }
        String[] descriptions = new String[]{LocaleController.getString("NoPopup", R.string.NoPopup), LocaleController.getString("OnlyWhenScreenOn", R.string.OnlyWhenScreenOn), LocaleController.getString("OnlyWhenScreenOff", R.string.OnlyWhenScreenOff), LocaleController.getString("AlwaysShowPopup", R.string.AlwaysShowPopup)};
        LinearLayout linearLayout = new LinearLayout(parentActivity);
        linearLayout.setOrientation(1);
        int a = 0;
        while (a < descriptions.length) {
            RadioColorCell cell = new RadioColorCell(parentActivity);
            cell.setTag(Integer.valueOf(a));
            cell.setPadding(AndroidUtilities.dp(4.0f), 0, AndroidUtilities.dp(4.0f), 0);
            cell.setCheckColor(Theme.getColor(Theme.key_radioBackground), Theme.getColor(Theme.key_dialogRadioBackgroundChecked));
            cell.setTextAndValue(descriptions[a], selected[0] == a);
            linearLayout.addView(cell);
            cell.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    selected[0] = ((Integer) v.getTag()).intValue();
                    Editor editor = MessagesController.getNotificationsSettings(UserConfig.selectedAccount).edit();
                    editor.putInt(globalGroup ? "popupGroup" : "popupAll", selected[0]);
                    editor.commit();
                    if (parentFragment != null) {
                        parentFragment.dismissCurrentDialig();
                    }
                    if (onSelect != null) {
                        onSelect.run();
                    }
                }
            });
            a++;
        }
        Builder builder = new Builder((Context) parentActivity);
        builder.setTitle(LocaleController.getString("PopupNotification", R.string.PopupNotification));
        builder.setView(linearLayout);
        builder.setPositiveButton(LocaleController.getString("Cancel", R.string.Cancel), null);
        return builder.create();
    }

    public static Dialog createSingleChoiceDialog(Activity parentActivity, final BaseFragment parentFragment, String[] options, String title, int selected, final OnClickListener listener) {
        LinearLayout linearLayout = new LinearLayout(parentActivity);
        linearLayout.setOrientation(1);
        Builder builder = new Builder((Context) parentActivity);
        for (int a = 0; a < options.length; a++) {
            boolean z;
            RadioColorCell cell = new RadioColorCell(parentActivity);
            cell.setPadding(AndroidUtilities.dp(4.0f), 0, AndroidUtilities.dp(4.0f), 0);
            cell.setTag(Integer.valueOf(a));
            cell.setCheckColor(Theme.getColor(Theme.key_radioBackground), Theme.getColor(Theme.key_dialogRadioBackgroundChecked));
            String str = options[a];
            if (selected == a) {
                z = true;
            } else {
                z = false;
            }
            cell.setTextAndValue(str, z);
            linearLayout.addView(cell);
            cell.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    int sel = ((Integer) v.getTag()).intValue();
                    if (parentFragment != null) {
                        parentFragment.dismissCurrentDialig();
                    }
                    listener.onClick(null, sel);
                }
            });
        }
        builder.setTitle(title);
        builder.setView(linearLayout);
        builder.setPositiveButton(LocaleController.getString("Cancel", R.string.Cancel), null);
        return builder.create();
    }

    public static Builder createTTLAlert(Context context, final EncryptedChat encryptedChat) {
        Builder builder = new Builder(context);
        builder.setTitle(LocaleController.getString("MessageLifetime", R.string.MessageLifetime));
        final NumberPicker numberPicker = new NumberPicker(context);
        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(20);
        if (encryptedChat.ttl > 0 && encryptedChat.ttl < 16) {
            numberPicker.setValue(encryptedChat.ttl);
        } else if (encryptedChat.ttl == 30) {
            numberPicker.setValue(16);
        } else if (encryptedChat.ttl == 60) {
            numberPicker.setValue(17);
        } else if (encryptedChat.ttl == 3600) {
            numberPicker.setValue(18);
        } else if (encryptedChat.ttl == 86400) {
            numberPicker.setValue(19);
        } else if (encryptedChat.ttl == 604800) {
            numberPicker.setValue(20);
        } else if (encryptedChat.ttl == 0) {
            numberPicker.setValue(0);
        }
        numberPicker.setFormatter(new Formatter() {
            public String format(int value) {
                if (value == 0) {
                    return LocaleController.getString("ShortMessageLifetimeForever", R.string.ShortMessageLifetimeForever);
                }
                if (value >= 1 && value < 16) {
                    return LocaleController.formatTTLString(value);
                }
                if (value == 16) {
                    return LocaleController.formatTTLString(30);
                }
                if (value == 17) {
                    return LocaleController.formatTTLString(60);
                }
                if (value == 18) {
                    return LocaleController.formatTTLString(3600);
                }
                if (value == 19) {
                    return LocaleController.formatTTLString(86400);
                }
                if (value == 20) {
                    return LocaleController.formatTTLString(604800);
                }
                return TtmlNode.ANONYMOUS_REGION_ID;
            }
        });
        builder.setView(numberPicker);
        builder.setNegativeButton(LocaleController.getString("Done", R.string.Done), new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                int oldValue = encryptedChat.ttl;
                which = numberPicker.getValue();
                if (which >= 0 && which < 16) {
                    encryptedChat.ttl = which;
                } else if (which == 16) {
                    encryptedChat.ttl = 30;
                } else if (which == 17) {
                    encryptedChat.ttl = 60;
                } else if (which == 18) {
                    encryptedChat.ttl = 3600;
                } else if (which == 19) {
                    encryptedChat.ttl = 86400;
                } else if (which == 20) {
                    encryptedChat.ttl = 604800;
                }
                if (oldValue != encryptedChat.ttl) {
                    SecretChatHelper.getInstance(UserConfig.selectedAccount).sendTTLMessage(encryptedChat, null);
                    MessagesStorage.getInstance(UserConfig.selectedAccount).updateEncryptedChatTTL(encryptedChat);
                }
            }
        });
        return builder;
    }

    public static AlertDialog createAccountSelectDialog(Activity parentActivity, final AccountSelectDelegate delegate) {
        if (UserConfig.getActivatedAccountsCount() < 2) {
            return null;
        }
        Builder builder = new Builder((Context) parentActivity);
        final Runnable dismissRunnable = builder.getDismissRunnable();
        final AlertDialog[] alertDialog = new AlertDialog[1];
        LinearLayout linearLayout = new LinearLayout(parentActivity);
        linearLayout.setOrientation(1);
        for (int a = 0; a < 3; a++) {
            if (UserConfig.getInstance(a).getCurrentUser() != null) {
                AccountSelectCell cell = new AccountSelectCell(parentActivity);
                cell.setAccount(a, false);
                cell.setPadding(AndroidUtilities.dp(14.0f), 0, AndroidUtilities.dp(14.0f), 0);
                cell.setBackgroundDrawable(Theme.getSelectorDrawable(false));
                linearLayout.addView(cell, LayoutHelper.createLinear(-1, 48));
                cell.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        if (alertDialog[0] != null) {
                            alertDialog[0].setOnDismissListener(null);
                        }
                        dismissRunnable.run();
                        delegate.didSelectAccount(((AccountSelectCell) v).getAccountNumber());
                    }
                });
            }
        }
        builder.setTitle(LocaleController.getString("SelectAccount", R.string.SelectAccount));
        builder.setView(linearLayout);
        builder.setPositiveButton(LocaleController.getString("Cancel", R.string.Cancel), null);
        AlertDialog create = builder.create();
        alertDialog[0] = create;
        return create;
    }
}
