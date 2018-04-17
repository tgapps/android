package org.telegram.messenger;

import android.text.TextUtils;
import android.util.LongSparseArray;
import android.util.SparseArray;
import android.util.SparseIntArray;
import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;
import org.telegram.SQLite.SQLiteCursor;
import org.telegram.SQLite.SQLiteDatabase;
import org.telegram.SQLite.SQLitePreparedStatement;
import org.telegram.messenger.ContactsController.Contact;
import org.telegram.messenger.MediaController.SearchImage;
import org.telegram.messenger.support.SparseLongArray;
import org.telegram.messenger.support.widget.helper.ItemTouchHelper.Callback;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.NativeByteBuffer;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.BotInfo;
import org.telegram.tgnet.TLRPC.ChannelParticipant;
import org.telegram.tgnet.TLRPC.Chat;
import org.telegram.tgnet.TLRPC.ChatFull;
import org.telegram.tgnet.TLRPC.ChatParticipant;
import org.telegram.tgnet.TLRPC.ChatParticipants;
import org.telegram.tgnet.TLRPC.Document;
import org.telegram.tgnet.TLRPC.EncryptedChat;
import org.telegram.tgnet.TLRPC.InputChannel;
import org.telegram.tgnet.TLRPC.InputMedia;
import org.telegram.tgnet.TLRPC.InputPeer;
import org.telegram.tgnet.TLRPC.Message;
import org.telegram.tgnet.TLRPC.MessageEntity;
import org.telegram.tgnet.TLRPC.MessageMedia;
import org.telegram.tgnet.TLRPC.Photo;
import org.telegram.tgnet.TLRPC.PhotoSize;
import org.telegram.tgnet.TLRPC.TL_channelFull;
import org.telegram.tgnet.TLRPC.TL_channels_deleteMessages;
import org.telegram.tgnet.TLRPC.TL_chatChannelParticipant;
import org.telegram.tgnet.TLRPC.TL_chatFull;
import org.telegram.tgnet.TLRPC.TL_chatInviteEmpty;
import org.telegram.tgnet.TLRPC.TL_chatParticipant;
import org.telegram.tgnet.TLRPC.TL_chatParticipantAdmin;
import org.telegram.tgnet.TLRPC.TL_chatParticipants;
import org.telegram.tgnet.TLRPC.TL_contact;
import org.telegram.tgnet.TLRPC.TL_decryptedMessageActionScreenshotMessages;
import org.telegram.tgnet.TLRPC.TL_decryptedMessageActionSetMessageTTL;
import org.telegram.tgnet.TLRPC.TL_dialog;
import org.telegram.tgnet.TLRPC.TL_documentEmpty;
import org.telegram.tgnet.TLRPC.TL_inputMediaGame;
import org.telegram.tgnet.TLRPC.TL_inputMessageEntityMentionName;
import org.telegram.tgnet.TLRPC.TL_message;
import org.telegram.tgnet.TLRPC.TL_messageActionHistoryClear;
import org.telegram.tgnet.TLRPC.TL_messageEncryptedAction;
import org.telegram.tgnet.TLRPC.TL_messageEntityMentionName;
import org.telegram.tgnet.TLRPC.TL_messageMediaDocument;
import org.telegram.tgnet.TLRPC.TL_messageMediaPhoto;
import org.telegram.tgnet.TLRPC.TL_messageMediaUnsupported;
import org.telegram.tgnet.TLRPC.TL_messageMediaUnsupported_old;
import org.telegram.tgnet.TLRPC.TL_messageMediaWebPage;
import org.telegram.tgnet.TLRPC.TL_message_secret;
import org.telegram.tgnet.TLRPC.TL_messages_botCallbackAnswer;
import org.telegram.tgnet.TLRPC.TL_messages_botResults;
import org.telegram.tgnet.TLRPC.TL_messages_deleteMessages;
import org.telegram.tgnet.TLRPC.TL_messages_dialogs;
import org.telegram.tgnet.TLRPC.TL_peerChannel;
import org.telegram.tgnet.TLRPC.TL_peerNotifySettingsEmpty;
import org.telegram.tgnet.TLRPC.TL_photoEmpty;
import org.telegram.tgnet.TLRPC.TL_photos_photos;
import org.telegram.tgnet.TLRPC.TL_replyInlineMarkup;
import org.telegram.tgnet.TLRPC.TL_updates_channelDifferenceTooLong;
import org.telegram.tgnet.TLRPC.TL_userStatusLastMonth;
import org.telegram.tgnet.TLRPC.TL_userStatusLastWeek;
import org.telegram.tgnet.TLRPC.TL_userStatusRecently;
import org.telegram.tgnet.TLRPC.User;
import org.telegram.tgnet.TLRPC.WallPaper;
import org.telegram.tgnet.TLRPC.WebPage;
import org.telegram.tgnet.TLRPC.messages_BotResults;
import org.telegram.tgnet.TLRPC.messages_Dialogs;
import org.telegram.tgnet.TLRPC.messages_Messages;
import org.telegram.tgnet.TLRPC.photos_Photos;

public class MessagesStorage {
    private static volatile MessagesStorage[] Instance = new MessagesStorage[3];
    private File cacheFile;
    private int currentAccount;
    private SQLiteDatabase database;
    private int lastDateValue = 0;
    private int lastPtsValue = 0;
    private int lastQtsValue = 0;
    private int lastSavedDate = 0;
    private int lastSavedPts = 0;
    private int lastSavedQts = 0;
    private int lastSavedSeq = 0;
    private int lastSecretVersion = 0;
    private int lastSeqValue = 0;
    private AtomicLong lastTaskId = new AtomicLong(System.currentTimeMillis());
    private CountDownLatch openSync = new CountDownLatch(1);
    private int secretG = 0;
    private byte[] secretPBytes = null;
    private File shmCacheFile;
    private DispatchQueue storageQueue = new DispatchQueue("storageQueue");
    private File walCacheFile;

    private class Hole {
        public int end;
        public int start;
        public int type;

        public Hole(int s, int e) {
            this.start = s;
            this.end = e;
        }

        public Hole(int t, int s, int e) {
            this.type = t;
            this.start = s;
            this.end = e;
        }
    }

    public interface IntCallback {
        void run(int i);
    }

    private void closeHolesInTable(java.lang.String r1, long r2, int r4, int r5) throws java.lang.Exception {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.MessagesStorage.closeHolesInTable(java.lang.String, long, int, int):void
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
        r1 = r22;
        r2 = r23;
        r3 = r24;
        r5 = r26;
        r6 = r27;
        r7 = 0;
        r8 = r1.database;	 Catch:{ Exception -> 0x0264 }
        r9 = java.util.Locale.US;	 Catch:{ Exception -> 0x0264 }
        r10 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0264 }
        r10.<init>();	 Catch:{ Exception -> 0x0264 }
        r11 = "SELECT start, end FROM ";	 Catch:{ Exception -> 0x0264 }
        r10.append(r11);	 Catch:{ Exception -> 0x0264 }
        r10.append(r2);	 Catch:{ Exception -> 0x0264 }
        r11 = " WHERE uid = %d AND ((end >= %d AND end <= %d) OR (start >= %d AND start <= %d) OR (start >= %d AND end <= %d) OR (start <= %d AND end >= %d))";	 Catch:{ Exception -> 0x0264 }
        r10.append(r11);	 Catch:{ Exception -> 0x0264 }
        r10 = r10.toString();	 Catch:{ Exception -> 0x0264 }
        r11 = 9;	 Catch:{ Exception -> 0x0264 }
        r11 = new java.lang.Object[r11];	 Catch:{ Exception -> 0x0264 }
        r12 = java.lang.Long.valueOf(r24);	 Catch:{ Exception -> 0x0264 }
        r13 = 0;	 Catch:{ Exception -> 0x0264 }
        r11[r13] = r12;	 Catch:{ Exception -> 0x0264 }
        r12 = java.lang.Integer.valueOf(r26);	 Catch:{ Exception -> 0x0264 }
        r14 = 1;	 Catch:{ Exception -> 0x0264 }
        r11[r14] = r12;	 Catch:{ Exception -> 0x0264 }
        r12 = java.lang.Integer.valueOf(r27);	 Catch:{ Exception -> 0x0264 }
        r15 = 2;	 Catch:{ Exception -> 0x0264 }
        r11[r15] = r12;	 Catch:{ Exception -> 0x0264 }
        r12 = java.lang.Integer.valueOf(r26);	 Catch:{ Exception -> 0x0264 }
        r15 = 3;	 Catch:{ Exception -> 0x0264 }
        r11[r15] = r12;	 Catch:{ Exception -> 0x0264 }
        r12 = java.lang.Integer.valueOf(r27);	 Catch:{ Exception -> 0x0264 }
        r15 = 4;	 Catch:{ Exception -> 0x0264 }
        r11[r15] = r12;	 Catch:{ Exception -> 0x0264 }
        r12 = 5;	 Catch:{ Exception -> 0x0264 }
        r18 = java.lang.Integer.valueOf(r26);	 Catch:{ Exception -> 0x0264 }
        r11[r12] = r18;	 Catch:{ Exception -> 0x0264 }
        r12 = 6;	 Catch:{ Exception -> 0x0264 }
        r18 = java.lang.Integer.valueOf(r27);	 Catch:{ Exception -> 0x0264 }
        r11[r12] = r18;	 Catch:{ Exception -> 0x0264 }
        r12 = 7;	 Catch:{ Exception -> 0x0264 }
        r18 = java.lang.Integer.valueOf(r26);	 Catch:{ Exception -> 0x0264 }
        r11[r12] = r18;	 Catch:{ Exception -> 0x0264 }
        r12 = 8;	 Catch:{ Exception -> 0x0264 }
        r18 = java.lang.Integer.valueOf(r27);	 Catch:{ Exception -> 0x0264 }
        r11[r12] = r18;	 Catch:{ Exception -> 0x0264 }
        r9 = java.lang.String.format(r9, r10, r11);	 Catch:{ Exception -> 0x0264 }
        r10 = new java.lang.Object[r13];	 Catch:{ Exception -> 0x0264 }
        r8 = r8.queryFinalized(r9, r10);	 Catch:{ Exception -> 0x0264 }
        r9 = 0;	 Catch:{ Exception -> 0x0264 }
    L_0x0074:
        r10 = r8.next();	 Catch:{ Exception -> 0x0264 }
        if (r10 == 0) goto L_0x0098;	 Catch:{ Exception -> 0x0264 }
    L_0x007a:
        if (r9 != 0) goto L_0x0082;	 Catch:{ Exception -> 0x0264 }
    L_0x007c:
        r10 = new java.util.ArrayList;	 Catch:{ Exception -> 0x0264 }
        r10.<init>();	 Catch:{ Exception -> 0x0264 }
        r9 = r10;	 Catch:{ Exception -> 0x0264 }
    L_0x0082:
        r10 = r8.intValue(r13);	 Catch:{ Exception -> 0x0264 }
        r11 = r8.intValue(r14);	 Catch:{ Exception -> 0x0264 }
        if (r10 != r11) goto L_0x008f;	 Catch:{ Exception -> 0x0264 }
    L_0x008c:
        if (r10 != r14) goto L_0x008f;	 Catch:{ Exception -> 0x0264 }
    L_0x008e:
        goto L_0x0074;	 Catch:{ Exception -> 0x0264 }
    L_0x008f:
        r12 = new org.telegram.messenger.MessagesStorage$Hole;	 Catch:{ Exception -> 0x0264 }
        r12.<init>(r10, r11);	 Catch:{ Exception -> 0x0264 }
        r9.add(r12);	 Catch:{ Exception -> 0x0264 }
        goto L_0x0074;	 Catch:{ Exception -> 0x0264 }
    L_0x0098:
        r8.dispose();	 Catch:{ Exception -> 0x0264 }
        if (r9 == 0) goto L_0x0263;	 Catch:{ Exception -> 0x0264 }
    L_0x009d:
        r10 = r13;	 Catch:{ Exception -> 0x0264 }
        r11 = r9.size();	 Catch:{ Exception -> 0x0264 }
        if (r10 >= r11) goto L_0x0263;	 Catch:{ Exception -> 0x0264 }
    L_0x00a4:
        r11 = r9.get(r10);	 Catch:{ Exception -> 0x0264 }
        r11 = (org.telegram.messenger.MessagesStorage.Hole) r11;	 Catch:{ Exception -> 0x0264 }
        r12 = r11.end;	 Catch:{ Exception -> 0x0264 }
        r12 = r12 - r14;	 Catch:{ Exception -> 0x0264 }
        if (r6 < r12) goto L_0x00ff;	 Catch:{ Exception -> 0x0264 }
    L_0x00af:
        r12 = r11.start;	 Catch:{ Exception -> 0x0264 }
        r12 = r12 + r14;	 Catch:{ Exception -> 0x0264 }
        if (r5 > r12) goto L_0x00ff;	 Catch:{ Exception -> 0x0264 }
    L_0x00b4:
        r12 = r1.database;	 Catch:{ Exception -> 0x0264 }
        r15 = java.util.Locale.US;	 Catch:{ Exception -> 0x0264 }
        r14 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0264 }
        r14.<init>();	 Catch:{ Exception -> 0x0264 }
        r13 = "DELETE FROM ";	 Catch:{ Exception -> 0x0264 }
        r14.append(r13);	 Catch:{ Exception -> 0x0264 }
        r14.append(r2);	 Catch:{ Exception -> 0x0264 }
        r13 = " WHERE uid = %d AND start = %d AND end = %d";	 Catch:{ Exception -> 0x0264 }
        r14.append(r13);	 Catch:{ Exception -> 0x0264 }
        r13 = r14.toString();	 Catch:{ Exception -> 0x0264 }
        r20 = r7;	 Catch:{ Exception -> 0x0264 }
        r14 = 3;	 Catch:{ Exception -> 0x0264 }
        r7 = new java.lang.Object[r14];	 Catch:{ Exception -> 0x0264 }
        r14 = java.lang.Long.valueOf(r24);	 Catch:{ Exception -> 0x0264 }
        r18 = 0;	 Catch:{ Exception -> 0x0264 }
        r7[r18] = r14;	 Catch:{ Exception -> 0x0264 }
        r14 = r11.start;	 Catch:{ Exception -> 0x0264 }
        r14 = java.lang.Integer.valueOf(r14);	 Catch:{ Exception -> 0x0264 }
        r18 = 1;	 Catch:{ Exception -> 0x0264 }
        r7[r18] = r14;	 Catch:{ Exception -> 0x0264 }
        r14 = r11.end;	 Catch:{ Exception -> 0x0264 }
        r14 = java.lang.Integer.valueOf(r14);	 Catch:{ Exception -> 0x0264 }
        r16 = 2;	 Catch:{ Exception -> 0x0264 }
        r7[r16] = r14;	 Catch:{ Exception -> 0x0264 }
        r7 = java.lang.String.format(r15, r13, r7);	 Catch:{ Exception -> 0x0264 }
        r7 = r12.executeFast(r7);	 Catch:{ Exception -> 0x0264 }
        r7 = r7.stepThis();	 Catch:{ Exception -> 0x0264 }
        r7.dispose();	 Catch:{ Exception -> 0x0264 }
        goto L_0x0162;	 Catch:{ Exception -> 0x0264 }
    L_0x00ff:
        r20 = r7;	 Catch:{ Exception -> 0x0264 }
        r7 = r11.end;	 Catch:{ Exception -> 0x0264 }
        r12 = 1;	 Catch:{ Exception -> 0x0264 }
        r7 = r7 - r12;	 Catch:{ Exception -> 0x0264 }
        if (r6 < r7) goto L_0x0169;	 Catch:{ Exception -> 0x0264 }
    L_0x0107:
        r7 = r11.end;	 Catch:{ Exception -> 0x0264 }
        if (r7 == r5) goto L_0x0162;
    L_0x010b:
        r7 = r1.database;	 Catch:{ Exception -> 0x015c }
        r12 = java.util.Locale.US;	 Catch:{ Exception -> 0x015c }
        r13 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x015c }
        r13.<init>();	 Catch:{ Exception -> 0x015c }
        r14 = "UPDATE ";	 Catch:{ Exception -> 0x015c }
        r13.append(r14);	 Catch:{ Exception -> 0x015c }
        r13.append(r2);	 Catch:{ Exception -> 0x015c }
        r14 = " SET end = %d WHERE uid = %d AND start = %d AND end = %d";	 Catch:{ Exception -> 0x015c }
        r13.append(r14);	 Catch:{ Exception -> 0x015c }
        r13 = r13.toString();	 Catch:{ Exception -> 0x015c }
        r14 = 4;	 Catch:{ Exception -> 0x015c }
        r15 = new java.lang.Object[r14];	 Catch:{ Exception -> 0x015c }
        r14 = java.lang.Integer.valueOf(r26);	 Catch:{ Exception -> 0x015c }
        r18 = 0;	 Catch:{ Exception -> 0x015c }
        r15[r18] = r14;	 Catch:{ Exception -> 0x015c }
        r14 = java.lang.Long.valueOf(r24);	 Catch:{ Exception -> 0x015c }
        r18 = 1;	 Catch:{ Exception -> 0x015c }
        r15[r18] = r14;	 Catch:{ Exception -> 0x015c }
        r14 = r11.start;	 Catch:{ Exception -> 0x015c }
        r14 = java.lang.Integer.valueOf(r14);	 Catch:{ Exception -> 0x015c }
        r16 = 2;	 Catch:{ Exception -> 0x015c }
        r15[r16] = r14;	 Catch:{ Exception -> 0x015c }
        r14 = r11.end;	 Catch:{ Exception -> 0x015c }
        r14 = java.lang.Integer.valueOf(r14);	 Catch:{ Exception -> 0x015c }
        r17 = 3;	 Catch:{ Exception -> 0x015c }
        r15[r17] = r14;	 Catch:{ Exception -> 0x015c }
        r12 = java.lang.String.format(r12, r13, r15);	 Catch:{ Exception -> 0x015c }
        r7 = r7.executeFast(r12);	 Catch:{ Exception -> 0x015c }
        r7 = r7.stepThis();	 Catch:{ Exception -> 0x015c }
        r7.dispose();	 Catch:{ Exception -> 0x015c }
        goto L_0x0161;
    L_0x015c:
        r0 = move-exception;
        r7 = r0;
        org.telegram.messenger.FileLog.e(r7);	 Catch:{ Exception -> 0x0264 }
    L_0x0162:
        r12 = 1;	 Catch:{ Exception -> 0x0264 }
        r13 = 2;	 Catch:{ Exception -> 0x0264 }
        r15 = 3;	 Catch:{ Exception -> 0x0264 }
        r18 = 0;	 Catch:{ Exception -> 0x0264 }
        goto L_0x0259;	 Catch:{ Exception -> 0x0264 }
    L_0x0169:
        r7 = r11.start;	 Catch:{ Exception -> 0x0264 }
        r12 = 1;	 Catch:{ Exception -> 0x0264 }
        r7 = r7 + r12;	 Catch:{ Exception -> 0x0264 }
        if (r5 > r7) goto L_0x01ca;	 Catch:{ Exception -> 0x0264 }
        r7 = r11.start;	 Catch:{ Exception -> 0x0264 }
        if (r7 == r6) goto L_0x0162;
        r7 = r1.database;	 Catch:{ Exception -> 0x01c4 }
        r12 = java.util.Locale.US;	 Catch:{ Exception -> 0x01c4 }
        r13 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x01c4 }
        r13.<init>();	 Catch:{ Exception -> 0x01c4 }
        r14 = "UPDATE ";	 Catch:{ Exception -> 0x01c4 }
        r13.append(r14);	 Catch:{ Exception -> 0x01c4 }
        r13.append(r2);	 Catch:{ Exception -> 0x01c4 }
        r14 = " SET start = %d WHERE uid = %d AND start = %d AND end = %d";	 Catch:{ Exception -> 0x01c4 }
        r13.append(r14);	 Catch:{ Exception -> 0x01c4 }
        r13 = r13.toString();	 Catch:{ Exception -> 0x01c4 }
        r14 = 4;	 Catch:{ Exception -> 0x01c4 }
        r15 = new java.lang.Object[r14];	 Catch:{ Exception -> 0x01c4 }
        r18 = java.lang.Integer.valueOf(r27);	 Catch:{ Exception -> 0x01c4 }
        r19 = 0;	 Catch:{ Exception -> 0x01c4 }
        r15[r19] = r18;	 Catch:{ Exception -> 0x01c4 }
        r18 = java.lang.Long.valueOf(r24);	 Catch:{ Exception -> 0x01c4 }
        r19 = 1;	 Catch:{ Exception -> 0x01c4 }
        r15[r19] = r18;	 Catch:{ Exception -> 0x01c4 }
        r14 = r11.start;	 Catch:{ Exception -> 0x01c4 }
        r14 = java.lang.Integer.valueOf(r14);	 Catch:{ Exception -> 0x01c4 }
        r16 = 2;	 Catch:{ Exception -> 0x01c4 }
        r15[r16] = r14;	 Catch:{ Exception -> 0x01c4 }
        r14 = r11.end;	 Catch:{ Exception -> 0x01c4 }
        r14 = java.lang.Integer.valueOf(r14);	 Catch:{ Exception -> 0x01c4 }
        r17 = 3;	 Catch:{ Exception -> 0x01c4 }
        r15[r17] = r14;	 Catch:{ Exception -> 0x01c4 }
        r12 = java.lang.String.format(r12, r13, r15);	 Catch:{ Exception -> 0x01c4 }
        r7 = r7.executeFast(r12);	 Catch:{ Exception -> 0x01c4 }
        r7 = r7.stepThis();	 Catch:{ Exception -> 0x01c4 }
        r7.dispose();	 Catch:{ Exception -> 0x01c4 }
        goto L_0x01c9;
    L_0x01c4:
        r0 = move-exception;
        r7 = r0;
        org.telegram.messenger.FileLog.e(r7);	 Catch:{ Exception -> 0x0264 }
        goto L_0x0162;	 Catch:{ Exception -> 0x0264 }
        r7 = r1.database;	 Catch:{ Exception -> 0x0264 }
        r12 = java.util.Locale.US;	 Catch:{ Exception -> 0x0264 }
        r13 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0264 }
        r13.<init>();	 Catch:{ Exception -> 0x0264 }
        r14 = "DELETE FROM ";	 Catch:{ Exception -> 0x0264 }
        r13.append(r14);	 Catch:{ Exception -> 0x0264 }
        r13.append(r2);	 Catch:{ Exception -> 0x0264 }
        r14 = " WHERE uid = %d AND start = %d AND end = %d";	 Catch:{ Exception -> 0x0264 }
        r13.append(r14);	 Catch:{ Exception -> 0x0264 }
        r13 = r13.toString();	 Catch:{ Exception -> 0x0264 }
        r14 = 3;	 Catch:{ Exception -> 0x0264 }
        r15 = new java.lang.Object[r14];	 Catch:{ Exception -> 0x0264 }
        r14 = java.lang.Long.valueOf(r24);	 Catch:{ Exception -> 0x0264 }
        r18 = 0;	 Catch:{ Exception -> 0x0264 }
        r15[r18] = r14;	 Catch:{ Exception -> 0x0264 }
        r14 = r11.start;	 Catch:{ Exception -> 0x0264 }
        r14 = java.lang.Integer.valueOf(r14);	 Catch:{ Exception -> 0x0264 }
        r19 = 1;	 Catch:{ Exception -> 0x0264 }
        r15[r19] = r14;	 Catch:{ Exception -> 0x0264 }
        r14 = r11.end;	 Catch:{ Exception -> 0x0264 }
        r14 = java.lang.Integer.valueOf(r14);	 Catch:{ Exception -> 0x0264 }
        r16 = 2;	 Catch:{ Exception -> 0x0264 }
        r15[r16] = r14;	 Catch:{ Exception -> 0x0264 }
        r12 = java.lang.String.format(r12, r13, r15);	 Catch:{ Exception -> 0x0264 }
        r7 = r7.executeFast(r12);	 Catch:{ Exception -> 0x0264 }
        r7 = r7.stepThis();	 Catch:{ Exception -> 0x0264 }
        r7.dispose();	 Catch:{ Exception -> 0x0264 }
        r7 = r1.database;	 Catch:{ Exception -> 0x0264 }
        r12 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0264 }
        r12.<init>();	 Catch:{ Exception -> 0x0264 }
        r13 = "REPLACE INTO ";	 Catch:{ Exception -> 0x0264 }
        r12.append(r13);	 Catch:{ Exception -> 0x0264 }
        r12.append(r2);	 Catch:{ Exception -> 0x0264 }
        r13 = " VALUES(?, ?, ?)";	 Catch:{ Exception -> 0x0264 }
        r12.append(r13);	 Catch:{ Exception -> 0x0264 }
        r12 = r12.toString();	 Catch:{ Exception -> 0x0264 }
        r7 = r7.executeFast(r12);	 Catch:{ Exception -> 0x0264 }
        r7.requery();	 Catch:{ Exception -> 0x0264 }
        r12 = 1;	 Catch:{ Exception -> 0x0264 }
        r7.bindLong(r12, r3);	 Catch:{ Exception -> 0x0264 }
        r12 = r11.start;	 Catch:{ Exception -> 0x0264 }
        r13 = 2;	 Catch:{ Exception -> 0x0264 }
        r7.bindInteger(r13, r12);	 Catch:{ Exception -> 0x0264 }
        r12 = 3;	 Catch:{ Exception -> 0x0264 }
        r7.bindInteger(r12, r5);	 Catch:{ Exception -> 0x0264 }
        r7.step();	 Catch:{ Exception -> 0x0264 }
        r7.requery();	 Catch:{ Exception -> 0x0264 }
        r12 = 1;	 Catch:{ Exception -> 0x0264 }
        r7.bindLong(r12, r3);	 Catch:{ Exception -> 0x0264 }
        r13 = 2;	 Catch:{ Exception -> 0x0264 }
        r7.bindInteger(r13, r6);	 Catch:{ Exception -> 0x0264 }
        r14 = r11.end;	 Catch:{ Exception -> 0x0264 }
        r15 = 3;	 Catch:{ Exception -> 0x0264 }
        r7.bindInteger(r15, r14);	 Catch:{ Exception -> 0x0264 }
        r7.step();	 Catch:{ Exception -> 0x0264 }
        r7.dispose();	 Catch:{ Exception -> 0x0264 }
        r10 = r10 + 1;
        r14 = r12;
        r13 = r18;
        r7 = r20;
        r15 = 4;
        goto L_0x009e;
    L_0x0263:
        goto L_0x0269;
    L_0x0264:
        r0 = move-exception;
        r7 = r0;
        org.telegram.messenger.FileLog.e(r7);
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.MessagesStorage.closeHolesInTable(java.lang.String, long, int, int):void");
    }

    private java.util.ArrayList<java.lang.Long> markMessagesAsDeletedInternal(java.util.ArrayList<java.lang.Integer> r1, int r2) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.MessagesStorage.markMessagesAsDeletedInternal(java.util.ArrayList, int):java.util.ArrayList<java.lang.Long>
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
        r1 = r23;
        r2 = r24;
        r3 = r25;
        r4 = new java.util.ArrayList;	 Catch:{ Exception -> 0x02ae }
        r4.<init>();	 Catch:{ Exception -> 0x02ae }
        r5 = new android.util.LongSparseArray;	 Catch:{ Exception -> 0x02ae }
        r5.<init>();	 Catch:{ Exception -> 0x02ae }
        r6 = 0;	 Catch:{ Exception -> 0x02ae }
        if (r3 == 0) goto L_0x004b;	 Catch:{ Exception -> 0x02ae }
    L_0x0013:
        r7 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x02ae }
        r8 = r24.size();	 Catch:{ Exception -> 0x02ae }
        r7.<init>(r8);	 Catch:{ Exception -> 0x02ae }
        r8 = r6;	 Catch:{ Exception -> 0x02ae }
    L_0x001d:
        r9 = r24.size();	 Catch:{ Exception -> 0x02ae }
        if (r8 >= r9) goto L_0x0045;	 Catch:{ Exception -> 0x02ae }
    L_0x0023:
        r9 = r2.get(r8);	 Catch:{ Exception -> 0x02ae }
        r9 = (java.lang.Integer) r9;	 Catch:{ Exception -> 0x02ae }
        r9 = r9.intValue();	 Catch:{ Exception -> 0x02ae }
        r9 = (long) r9;	 Catch:{ Exception -> 0x02ae }
        r11 = (long) r3;	 Catch:{ Exception -> 0x02ae }
        r13 = 32;	 Catch:{ Exception -> 0x02ae }
        r11 = r11 << r13;	 Catch:{ Exception -> 0x02ae }
        r13 = r9 | r11;	 Catch:{ Exception -> 0x02ae }
        r9 = r7.length();	 Catch:{ Exception -> 0x02ae }
        if (r9 <= 0) goto L_0x003f;	 Catch:{ Exception -> 0x02ae }
    L_0x003a:
        r9 = 44;	 Catch:{ Exception -> 0x02ae }
        r7.append(r9);	 Catch:{ Exception -> 0x02ae }
    L_0x003f:
        r7.append(r13);	 Catch:{ Exception -> 0x02ae }
        r8 = r8 + 1;	 Catch:{ Exception -> 0x02ae }
        goto L_0x001d;	 Catch:{ Exception -> 0x02ae }
    L_0x0045:
        r8 = r7.toString();	 Catch:{ Exception -> 0x02ae }
        r7 = r8;	 Catch:{ Exception -> 0x02ae }
        goto L_0x0051;	 Catch:{ Exception -> 0x02ae }
    L_0x004b:
        r7 = ",";	 Catch:{ Exception -> 0x02ae }
        r7 = android.text.TextUtils.join(r7, r2);	 Catch:{ Exception -> 0x02ae }
    L_0x0051:
        r8 = new java.util.ArrayList;	 Catch:{ Exception -> 0x02ae }
        r8.<init>();	 Catch:{ Exception -> 0x02ae }
        r9 = r1.currentAccount;	 Catch:{ Exception -> 0x02ae }
        r9 = org.telegram.messenger.UserConfig.getInstance(r9);	 Catch:{ Exception -> 0x02ae }
        r9 = r9.getClientUserId();	 Catch:{ Exception -> 0x02ae }
        r10 = r1.database;	 Catch:{ Exception -> 0x02ae }
        r11 = java.util.Locale.US;	 Catch:{ Exception -> 0x02ae }
        r12 = "SELECT uid, data, read_state, out, mention FROM messages WHERE mid IN(%s)";	 Catch:{ Exception -> 0x02ae }
        r13 = 1;	 Catch:{ Exception -> 0x02ae }
        r14 = new java.lang.Object[r13];	 Catch:{ Exception -> 0x02ae }
        r14[r6] = r7;	 Catch:{ Exception -> 0x02ae }
        r11 = java.lang.String.format(r11, r12, r14);	 Catch:{ Exception -> 0x02ae }
        r12 = new java.lang.Object[r6];	 Catch:{ Exception -> 0x02ae }
        r10 = r10.queryFinalized(r11, r12);	 Catch:{ Exception -> 0x02ae }
        r11 = 3;
        r12 = 2;
        r14 = r10.next();	 Catch:{ Exception -> 0x0188 }
        if (r14 == 0) goto L_0x0185;	 Catch:{ Exception -> 0x0188 }
    L_0x007d:
        r14 = r10.longValue(r6);	 Catch:{ Exception -> 0x0188 }
        r17 = r7;
        r6 = (long) r9;
        r18 = (r14 > r6 ? 1 : (r14 == r6 ? 0 : -1));
        if (r18 != 0) goto L_0x008a;
    L_0x0088:
        goto L_0x017a;
    L_0x008a:
        r6 = r10.intValue(r12);	 Catch:{ Exception -> 0x0182 }
        r7 = r10.intValue(r11);	 Catch:{ Exception -> 0x0182 }
        if (r7 != 0) goto L_0x00d6;	 Catch:{ Exception -> 0x0182 }
    L_0x0094:
        r7 = r5.get(r14);	 Catch:{ Exception -> 0x0182 }
        r7 = (java.lang.Integer[]) r7;	 Catch:{ Exception -> 0x0182 }
        if (r7 != 0) goto L_0x00af;	 Catch:{ Exception -> 0x0182 }
    L_0x009c:
        r11 = new java.lang.Integer[r12];	 Catch:{ Exception -> 0x0182 }
        r12 = 0;	 Catch:{ Exception -> 0x0182 }
        r16 = java.lang.Integer.valueOf(r12);	 Catch:{ Exception -> 0x0182 }
        r11[r12] = r16;	 Catch:{ Exception -> 0x0182 }
        r18 = java.lang.Integer.valueOf(r12);	 Catch:{ Exception -> 0x0182 }
        r11[r13] = r18;	 Catch:{ Exception -> 0x0182 }
        r7 = r11;	 Catch:{ Exception -> 0x0182 }
        r5.put(r14, r7);	 Catch:{ Exception -> 0x0182 }
    L_0x00af:
        r11 = 2;	 Catch:{ Exception -> 0x0182 }
        if (r6 >= r11) goto L_0x00c1;	 Catch:{ Exception -> 0x0182 }
    L_0x00b2:
        r11 = r7[r13];	 Catch:{ Exception -> 0x0182 }
        r11 = r7[r13];	 Catch:{ Exception -> 0x0182 }
        r11 = r11.intValue();	 Catch:{ Exception -> 0x0182 }
        r11 = r11 + r13;	 Catch:{ Exception -> 0x0182 }
        r11 = java.lang.Integer.valueOf(r11);	 Catch:{ Exception -> 0x0182 }
        r7[r13] = r11;	 Catch:{ Exception -> 0x0182 }
    L_0x00c1:
        if (r6 == 0) goto L_0x00c6;	 Catch:{ Exception -> 0x0182 }
    L_0x00c3:
        r11 = 2;	 Catch:{ Exception -> 0x0182 }
        if (r6 != r11) goto L_0x00d6;	 Catch:{ Exception -> 0x0182 }
    L_0x00c6:
        r11 = 0;	 Catch:{ Exception -> 0x0182 }
        r12 = r7[r11];	 Catch:{ Exception -> 0x0182 }
        r12 = r7[r11];	 Catch:{ Exception -> 0x0182 }
        r12 = r12.intValue();	 Catch:{ Exception -> 0x0182 }
        r12 = r12 + r13;	 Catch:{ Exception -> 0x0182 }
        r12 = java.lang.Integer.valueOf(r12);	 Catch:{ Exception -> 0x0182 }
        r7[r11] = r12;	 Catch:{ Exception -> 0x0182 }
    L_0x00d6:
        r7 = (int) r14;	 Catch:{ Exception -> 0x0182 }
        if (r7 == 0) goto L_0x00db;	 Catch:{ Exception -> 0x0182 }
    L_0x00d9:
        goto L_0x017a;	 Catch:{ Exception -> 0x0182 }
    L_0x00db:
        r7 = r10.byteBufferValue(r13);	 Catch:{ Exception -> 0x0182 }
        if (r7 == 0) goto L_0x0179;	 Catch:{ Exception -> 0x0182 }
    L_0x00e1:
        r11 = 0;	 Catch:{ Exception -> 0x0182 }
        r12 = r7.readInt32(r11);	 Catch:{ Exception -> 0x0182 }
        r12 = org.telegram.tgnet.TLRPC.Message.TLdeserialize(r7, r12, r11);	 Catch:{ Exception -> 0x0182 }
        r11 = r12;	 Catch:{ Exception -> 0x0182 }
        r12 = r1.currentAccount;	 Catch:{ Exception -> 0x0182 }
        r12 = org.telegram.messenger.UserConfig.getInstance(r12);	 Catch:{ Exception -> 0x0182 }
        r12 = r12.clientUserId;	 Catch:{ Exception -> 0x0182 }
        r11.readAttachPath(r7, r12);	 Catch:{ Exception -> 0x0182 }
        r7.reuse();	 Catch:{ Exception -> 0x0182 }
        if (r11 == 0) goto L_0x0179;	 Catch:{ Exception -> 0x0182 }
        r12 = r11.media;	 Catch:{ Exception -> 0x0182 }
        r12 = r12 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaPhoto;	 Catch:{ Exception -> 0x0182 }
        if (r12 == 0) goto L_0x0140;	 Catch:{ Exception -> 0x0182 }
        r12 = r11.media;	 Catch:{ Exception -> 0x0182 }
        r12 = r12.photo;	 Catch:{ Exception -> 0x0182 }
        r12 = r12.sizes;	 Catch:{ Exception -> 0x0182 }
        r12 = r12.iterator();	 Catch:{ Exception -> 0x0182 }
        r18 = r12.hasNext();	 Catch:{ Exception -> 0x0182 }
        if (r18 == 0) goto L_0x013d;	 Catch:{ Exception -> 0x0182 }
        r18 = r12.next();	 Catch:{ Exception -> 0x0182 }
        r18 = (org.telegram.tgnet.TLRPC.PhotoSize) r18;	 Catch:{ Exception -> 0x0182 }
        r19 = r18;	 Catch:{ Exception -> 0x0182 }
        r13 = r19;	 Catch:{ Exception -> 0x0182 }
        r18 = org.telegram.messenger.FileLoader.getPathToAttach(r13);	 Catch:{ Exception -> 0x0182 }
        r20 = r18;	 Catch:{ Exception -> 0x0182 }
        r3 = r20;	 Catch:{ Exception -> 0x0182 }
        if (r3 == 0) goto L_0x0135;	 Catch:{ Exception -> 0x0182 }
        r21 = r6;	 Catch:{ Exception -> 0x0182 }
        r6 = r3.toString();	 Catch:{ Exception -> 0x0182 }
        r6 = r6.length();	 Catch:{ Exception -> 0x0182 }
        if (r6 <= 0) goto L_0x0137;	 Catch:{ Exception -> 0x0182 }
        r8.add(r3);	 Catch:{ Exception -> 0x0182 }
        goto L_0x0137;	 Catch:{ Exception -> 0x0182 }
        r21 = r6;	 Catch:{ Exception -> 0x0182 }
        r6 = r21;	 Catch:{ Exception -> 0x0182 }
        r3 = r25;	 Catch:{ Exception -> 0x0182 }
        r13 = 1;	 Catch:{ Exception -> 0x0182 }
        goto L_0x010b;	 Catch:{ Exception -> 0x0182 }
        r21 = r6;	 Catch:{ Exception -> 0x0182 }
        goto L_0x0179;	 Catch:{ Exception -> 0x0182 }
        r21 = r6;	 Catch:{ Exception -> 0x0182 }
        r3 = r11.media;	 Catch:{ Exception -> 0x0182 }
        r3 = r3 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaDocument;	 Catch:{ Exception -> 0x0182 }
        if (r3 == 0) goto L_0x0179;	 Catch:{ Exception -> 0x0182 }
        r3 = r11.media;	 Catch:{ Exception -> 0x0182 }
        r3 = r3.document;	 Catch:{ Exception -> 0x0182 }
        r3 = org.telegram.messenger.FileLoader.getPathToAttach(r3);	 Catch:{ Exception -> 0x0182 }
        if (r3 == 0) goto L_0x015f;	 Catch:{ Exception -> 0x0182 }
        r6 = r3.toString();	 Catch:{ Exception -> 0x0182 }
        r6 = r6.length();	 Catch:{ Exception -> 0x0182 }
        if (r6 <= 0) goto L_0x015f;	 Catch:{ Exception -> 0x0182 }
        r8.add(r3);	 Catch:{ Exception -> 0x0182 }
        r6 = r11.media;	 Catch:{ Exception -> 0x0182 }
        r6 = r6.document;	 Catch:{ Exception -> 0x0182 }
        r6 = r6.thumb;	 Catch:{ Exception -> 0x0182 }
        r6 = org.telegram.messenger.FileLoader.getPathToAttach(r6);	 Catch:{ Exception -> 0x0182 }
        r3 = r6;	 Catch:{ Exception -> 0x0182 }
        if (r3 == 0) goto L_0x0179;	 Catch:{ Exception -> 0x0182 }
        r6 = r3.toString();	 Catch:{ Exception -> 0x0182 }
        r6 = r6.length();	 Catch:{ Exception -> 0x0182 }
        if (r6 <= 0) goto L_0x0179;	 Catch:{ Exception -> 0x0182 }
        r8.add(r3);	 Catch:{ Exception -> 0x0182 }
    L_0x017a:
        r7 = r17;
        r3 = r25;
        r6 = 0;
        r13 = 1;
        goto L_0x0075;
    L_0x0182:
        r0 = move-exception;
        r3 = r0;
        goto L_0x018c;
    L_0x0185:
        r17 = r7;
        goto L_0x018f;
    L_0x0188:
        r0 = move-exception;
        r17 = r7;
        r3 = r0;
        org.telegram.messenger.FileLog.e(r3);	 Catch:{ Exception -> 0x02ae }
        r10.dispose();	 Catch:{ Exception -> 0x02ae }
        r3 = r1.currentAccount;	 Catch:{ Exception -> 0x02ae }
        r3 = org.telegram.messenger.FileLoader.getInstance(r3);	 Catch:{ Exception -> 0x02ae }
        r6 = 0;	 Catch:{ Exception -> 0x02ae }
        r3.deleteFiles(r8, r6);	 Catch:{ Exception -> 0x02ae }
        r3 = 0;	 Catch:{ Exception -> 0x02ae }
        r6 = r5.size();	 Catch:{ Exception -> 0x02ae }
        if (r3 >= r6) goto L_0x0225;	 Catch:{ Exception -> 0x02ae }
        r6 = r5.keyAt(r3);	 Catch:{ Exception -> 0x02ae }
        r11 = r5.valueAt(r3);	 Catch:{ Exception -> 0x02ae }
        r11 = (java.lang.Integer[]) r11;	 Catch:{ Exception -> 0x02ae }
        r12 = r1.database;	 Catch:{ Exception -> 0x02ae }
        r13 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x02ae }
        r13.<init>();	 Catch:{ Exception -> 0x02ae }
        r14 = "SELECT unread_count, unread_count_i FROM dialogs WHERE did = ";	 Catch:{ Exception -> 0x02ae }
        r13.append(r14);	 Catch:{ Exception -> 0x02ae }
        r13.append(r6);	 Catch:{ Exception -> 0x02ae }
        r13 = r13.toString();	 Catch:{ Exception -> 0x02ae }
        r14 = 0;	 Catch:{ Exception -> 0x02ae }
        r15 = new java.lang.Object[r14];	 Catch:{ Exception -> 0x02ae }
        r12 = r12.queryFinalized(r13, r15);	 Catch:{ Exception -> 0x02ae }
        r10 = r12;	 Catch:{ Exception -> 0x02ae }
        r12 = 0;	 Catch:{ Exception -> 0x02ae }
        r13 = 0;	 Catch:{ Exception -> 0x02ae }
        r14 = r10.next();	 Catch:{ Exception -> 0x02ae }
        if (r14 == 0) goto L_0x01dc;	 Catch:{ Exception -> 0x02ae }
        r14 = 0;	 Catch:{ Exception -> 0x02ae }
        r15 = r10.intValue(r14);	 Catch:{ Exception -> 0x02ae }
        r12 = r15;	 Catch:{ Exception -> 0x02ae }
        r14 = 1;	 Catch:{ Exception -> 0x02ae }
        r15 = r10.intValue(r14);	 Catch:{ Exception -> 0x02ae }
        r13 = r15;	 Catch:{ Exception -> 0x02ae }
        r10.dispose();	 Catch:{ Exception -> 0x02ae }
        r14 = java.lang.Long.valueOf(r6);	 Catch:{ Exception -> 0x02ae }
        r4.add(r14);	 Catch:{ Exception -> 0x02ae }
        r14 = r1.database;	 Catch:{ Exception -> 0x02ae }
        r15 = "UPDATE dialogs SET unread_count = ?, unread_count_i = ? WHERE did = ?";	 Catch:{ Exception -> 0x02ae }
        r14 = r14.executeFast(r15);	 Catch:{ Exception -> 0x02ae }
        r14.requery();	 Catch:{ Exception -> 0x02ae }
        r22 = r5;	 Catch:{ Exception -> 0x02ae }
        r15 = 0;	 Catch:{ Exception -> 0x02ae }
        r5 = r11[r15];	 Catch:{ Exception -> 0x02ae }
        r5 = r5.intValue();	 Catch:{ Exception -> 0x02ae }
        r5 = r12 - r5;	 Catch:{ Exception -> 0x02ae }
        r5 = java.lang.Math.max(r15, r5);	 Catch:{ Exception -> 0x02ae }
        r15 = 1;	 Catch:{ Exception -> 0x02ae }
        r14.bindInteger(r15, r5);	 Catch:{ Exception -> 0x02ae }
        r5 = r11[r15];	 Catch:{ Exception -> 0x02ae }
        r5 = r5.intValue();	 Catch:{ Exception -> 0x02ae }
        r5 = r13 - r5;	 Catch:{ Exception -> 0x02ae }
        r15 = 0;	 Catch:{ Exception -> 0x02ae }
        r5 = java.lang.Math.max(r15, r5);	 Catch:{ Exception -> 0x02ae }
        r15 = 2;	 Catch:{ Exception -> 0x02ae }
        r14.bindInteger(r15, r5);	 Catch:{ Exception -> 0x02ae }
        r5 = 3;	 Catch:{ Exception -> 0x02ae }
        r14.bindLong(r5, r6);	 Catch:{ Exception -> 0x02ae }
        r14.step();	 Catch:{ Exception -> 0x02ae }
        r14.dispose();	 Catch:{ Exception -> 0x02ae }
        r3 = r3 + 1;	 Catch:{ Exception -> 0x02ae }
        r5 = r22;	 Catch:{ Exception -> 0x02ae }
        goto L_0x019d;	 Catch:{ Exception -> 0x02ae }
        r22 = r5;	 Catch:{ Exception -> 0x02ae }
        r3 = r1.database;	 Catch:{ Exception -> 0x02ae }
        r5 = java.util.Locale.US;	 Catch:{ Exception -> 0x02ae }
        r6 = "DELETE FROM messages WHERE mid IN(%s)";	 Catch:{ Exception -> 0x02ae }
        r7 = 1;	 Catch:{ Exception -> 0x02ae }
        r11 = new java.lang.Object[r7];	 Catch:{ Exception -> 0x02ae }
        r7 = 0;	 Catch:{ Exception -> 0x02ae }
        r11[r7] = r17;	 Catch:{ Exception -> 0x02ae }
        r5 = java.lang.String.format(r5, r6, r11);	 Catch:{ Exception -> 0x02ae }
        r3 = r3.executeFast(r5);	 Catch:{ Exception -> 0x02ae }
        r3 = r3.stepThis();	 Catch:{ Exception -> 0x02ae }
        r3.dispose();	 Catch:{ Exception -> 0x02ae }
        r3 = r1.database;	 Catch:{ Exception -> 0x02ae }
        r5 = java.util.Locale.US;	 Catch:{ Exception -> 0x02ae }
        r6 = "DELETE FROM bot_keyboard WHERE mid IN(%s)";	 Catch:{ Exception -> 0x02ae }
        r7 = 1;	 Catch:{ Exception -> 0x02ae }
        r11 = new java.lang.Object[r7];	 Catch:{ Exception -> 0x02ae }
        r7 = 0;	 Catch:{ Exception -> 0x02ae }
        r11[r7] = r17;	 Catch:{ Exception -> 0x02ae }
        r5 = java.lang.String.format(r5, r6, r11);	 Catch:{ Exception -> 0x02ae }
        r3 = r3.executeFast(r5);	 Catch:{ Exception -> 0x02ae }
        r3 = r3.stepThis();	 Catch:{ Exception -> 0x02ae }
        r3.dispose();	 Catch:{ Exception -> 0x02ae }
        r3 = r1.database;	 Catch:{ Exception -> 0x02ae }
        r5 = java.util.Locale.US;	 Catch:{ Exception -> 0x02ae }
        r6 = "DELETE FROM messages_seq WHERE mid IN(%s)";	 Catch:{ Exception -> 0x02ae }
        r7 = 1;	 Catch:{ Exception -> 0x02ae }
        r11 = new java.lang.Object[r7];	 Catch:{ Exception -> 0x02ae }
        r7 = 0;	 Catch:{ Exception -> 0x02ae }
        r11[r7] = r17;	 Catch:{ Exception -> 0x02ae }
        r5 = java.lang.String.format(r5, r6, r11);	 Catch:{ Exception -> 0x02ae }
        r3 = r3.executeFast(r5);	 Catch:{ Exception -> 0x02ae }
        r3 = r3.stepThis();	 Catch:{ Exception -> 0x02ae }
        r3.dispose();	 Catch:{ Exception -> 0x02ae }
        r3 = r1.database;	 Catch:{ Exception -> 0x02ae }
        r5 = java.util.Locale.US;	 Catch:{ Exception -> 0x02ae }
        r6 = "DELETE FROM media_v2 WHERE mid IN(%s)";	 Catch:{ Exception -> 0x02ae }
        r7 = 1;	 Catch:{ Exception -> 0x02ae }
        r7 = new java.lang.Object[r7];	 Catch:{ Exception -> 0x02ae }
        r11 = 0;	 Catch:{ Exception -> 0x02ae }
        r7[r11] = r17;	 Catch:{ Exception -> 0x02ae }
        r5 = java.lang.String.format(r5, r6, r7);	 Catch:{ Exception -> 0x02ae }
        r3 = r3.executeFast(r5);	 Catch:{ Exception -> 0x02ae }
        r3 = r3.stepThis();	 Catch:{ Exception -> 0x02ae }
        r3.dispose();	 Catch:{ Exception -> 0x02ae }
        r3 = r1.database;	 Catch:{ Exception -> 0x02ae }
        r5 = "DELETE FROM media_counts_v2 WHERE 1";	 Catch:{ Exception -> 0x02ae }
        r3 = r3.executeFast(r5);	 Catch:{ Exception -> 0x02ae }
        r3 = r3.stepThis();	 Catch:{ Exception -> 0x02ae }
        r3.dispose();	 Catch:{ Exception -> 0x02ae }
        r3 = r1.currentAccount;	 Catch:{ Exception -> 0x02ae }
        r3 = org.telegram.messenger.DataQuery.getInstance(r3);	 Catch:{ Exception -> 0x02ae }
        r5 = 0;	 Catch:{ Exception -> 0x02ae }
        r3.clearBotKeyboard(r5, r2);	 Catch:{ Exception -> 0x02ae }
        return r4;
    L_0x02ae:
        r0 = move-exception;
        r3 = r0;
        org.telegram.messenger.FileLog.e(r3);
        r3 = 0;
        return r3;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.MessagesStorage.markMessagesAsDeletedInternal(java.util.ArrayList, int):java.util.ArrayList<java.lang.Long>");
    }

    private void putUsersAndChatsInternal(java.util.ArrayList<org.telegram.tgnet.TLRPC.User> r1, java.util.ArrayList<org.telegram.tgnet.TLRPC.Chat> r2, boolean r3) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.MessagesStorage.putUsersAndChatsInternal(java.util.ArrayList, java.util.ArrayList, boolean):void
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
        if (r4 == 0) goto L_0x000a;
    L_0x0002:
        r0 = r1.database;	 Catch:{ Exception -> 0x0008 }
        r0.beginTransaction();	 Catch:{ Exception -> 0x0008 }
        goto L_0x000a;	 Catch:{ Exception -> 0x0008 }
    L_0x0008:
        r0 = move-exception;	 Catch:{ Exception -> 0x0008 }
        goto L_0x0018;	 Catch:{ Exception -> 0x0008 }
    L_0x000a:
        r1.putUsersInternal(r2);	 Catch:{ Exception -> 0x0008 }
        r1.putChatsInternal(r3);	 Catch:{ Exception -> 0x0008 }
        if (r4 == 0) goto L_0x0017;	 Catch:{ Exception -> 0x0008 }
        r0 = r1.database;	 Catch:{ Exception -> 0x0008 }
        r0.commitTransaction();	 Catch:{ Exception -> 0x0008 }
        goto L_0x001c;
        org.telegram.messenger.FileLog.e(r0);
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.MessagesStorage.putUsersAndChatsInternal(java.util.ArrayList, java.util.ArrayList, boolean):void");
    }

    private void updateDialogsWithDeletedMessagesInternal(java.util.ArrayList<java.lang.Integer> r1, java.util.ArrayList<java.lang.Long> r2, int r3) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.MessagesStorage.updateDialogsWithDeletedMessagesInternal(java.util.ArrayList, java.util.ArrayList, int):void
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
        r1 = r17;
        r2 = r19;
        r3 = r20;
        r4 = java.lang.Thread.currentThread();
        r4 = r4.getId();
        r6 = r1.storageQueue;
        r6 = r6.getId();
        r8 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1));
        if (r8 == 0) goto L_0x0020;
    L_0x0018:
        r4 = new java.lang.RuntimeException;
        r5 = "wrong db thread";
        r4.<init>(r5);
        throw r4;
    L_0x0020:
        r4 = new java.util.ArrayList;	 Catch:{ Exception -> 0x026f }
        r4.<init>();	 Catch:{ Exception -> 0x026f }
        r5 = r18.isEmpty();	 Catch:{ Exception -> 0x026f }
        r6 = 3;	 Catch:{ Exception -> 0x026f }
        r7 = 2;	 Catch:{ Exception -> 0x026f }
        r8 = 1;	 Catch:{ Exception -> 0x026f }
        r9 = 0;	 Catch:{ Exception -> 0x026f }
        if (r5 != 0) goto L_0x00b1;	 Catch:{ Exception -> 0x026f }
    L_0x002f:
        if (r3 == 0) goto L_0x0046;	 Catch:{ Exception -> 0x026f }
    L_0x0031:
        r5 = -r3;	 Catch:{ Exception -> 0x026f }
        r10 = (long) r5;	 Catch:{ Exception -> 0x026f }
        r5 = java.lang.Long.valueOf(r10);	 Catch:{ Exception -> 0x026f }
        r4.add(r5);	 Catch:{ Exception -> 0x026f }
        r5 = r1.database;	 Catch:{ Exception -> 0x026f }
        r10 = "UPDATE dialogs SET last_mid = (SELECT mid FROM messages WHERE uid = ? AND date = (SELECT MAX(date) FROM messages WHERE uid = ?)) WHERE did = ?";	 Catch:{ Exception -> 0x026f }
        r5 = r5.executeFast(r10);	 Catch:{ Exception -> 0x026f }
        r10 = r18;	 Catch:{ Exception -> 0x026f }
        r12 = r5;	 Catch:{ Exception -> 0x026f }
        goto L_0x007f;	 Catch:{ Exception -> 0x026f }
    L_0x0046:
        r5 = ",";	 Catch:{ Exception -> 0x026f }
        r10 = r18;
        r5 = android.text.TextUtils.join(r5, r10);	 Catch:{ Exception -> 0x026d }
        r11 = r1.database;	 Catch:{ Exception -> 0x026d }
        r12 = java.util.Locale.US;	 Catch:{ Exception -> 0x026d }
        r13 = "SELECT did FROM dialogs WHERE last_mid IN(%s)";	 Catch:{ Exception -> 0x026d }
        r14 = new java.lang.Object[r8];	 Catch:{ Exception -> 0x026d }
        r14[r9] = r5;	 Catch:{ Exception -> 0x026d }
        r12 = java.lang.String.format(r12, r13, r14);	 Catch:{ Exception -> 0x026d }
        r13 = new java.lang.Object[r9];	 Catch:{ Exception -> 0x026d }
        r11 = r11.queryFinalized(r12, r13);	 Catch:{ Exception -> 0x026d }
    L_0x0062:
        r12 = r11.next();	 Catch:{ Exception -> 0x026d }
        if (r12 == 0) goto L_0x0074;	 Catch:{ Exception -> 0x026d }
    L_0x0068:
        r12 = r11.longValue(r9);	 Catch:{ Exception -> 0x026d }
        r12 = java.lang.Long.valueOf(r12);	 Catch:{ Exception -> 0x026d }
        r4.add(r12);	 Catch:{ Exception -> 0x026d }
        goto L_0x0062;	 Catch:{ Exception -> 0x026d }
    L_0x0074:
        r11.dispose();	 Catch:{ Exception -> 0x026d }
        r12 = r1.database;	 Catch:{ Exception -> 0x026d }
        r13 = "UPDATE dialogs SET last_mid = (SELECT mid FROM messages WHERE uid = ? AND date = (SELECT MAX(date) FROM messages WHERE uid = ? AND date != 0)) WHERE did = ?";	 Catch:{ Exception -> 0x026d }
        r12 = r12.executeFast(r13);	 Catch:{ Exception -> 0x026d }
    L_0x007f:
        r5 = r12;	 Catch:{ Exception -> 0x026d }
        r11 = r1.database;	 Catch:{ Exception -> 0x026d }
        r11.beginTransaction();	 Catch:{ Exception -> 0x026d }
        r11 = r9;	 Catch:{ Exception -> 0x026d }
    L_0x0086:
        r12 = r4.size();	 Catch:{ Exception -> 0x026d }
        if (r11 >= r12) goto L_0x00a8;	 Catch:{ Exception -> 0x026d }
    L_0x008c:
        r12 = r4.get(r11);	 Catch:{ Exception -> 0x026d }
        r12 = (java.lang.Long) r12;	 Catch:{ Exception -> 0x026d }
        r12 = r12.longValue();	 Catch:{ Exception -> 0x026d }
        r5.requery();	 Catch:{ Exception -> 0x026d }
        r5.bindLong(r8, r12);	 Catch:{ Exception -> 0x026d }
        r5.bindLong(r7, r12);	 Catch:{ Exception -> 0x026d }
        r5.bindLong(r6, r12);	 Catch:{ Exception -> 0x026d }
        r5.step();	 Catch:{ Exception -> 0x026d }
        r11 = r11 + 1;	 Catch:{ Exception -> 0x026d }
        goto L_0x0086;	 Catch:{ Exception -> 0x026d }
    L_0x00a8:
        r5.dispose();	 Catch:{ Exception -> 0x026d }
        r11 = r1.database;	 Catch:{ Exception -> 0x026d }
        r11.commitTransaction();	 Catch:{ Exception -> 0x026d }
        goto L_0x00bc;	 Catch:{ Exception -> 0x026d }
    L_0x00b1:
        r10 = r18;	 Catch:{ Exception -> 0x026d }
        r5 = -r3;	 Catch:{ Exception -> 0x026d }
        r11 = (long) r5;	 Catch:{ Exception -> 0x026d }
        r5 = java.lang.Long.valueOf(r11);	 Catch:{ Exception -> 0x026d }
        r4.add(r5);	 Catch:{ Exception -> 0x026d }
    L_0x00bc:
        if (r2 == 0) goto L_0x00d7;	 Catch:{ Exception -> 0x026d }
    L_0x00be:
        r5 = r9;	 Catch:{ Exception -> 0x026d }
    L_0x00bf:
        r11 = r19.size();	 Catch:{ Exception -> 0x026d }
        if (r5 >= r11) goto L_0x00d7;	 Catch:{ Exception -> 0x026d }
    L_0x00c5:
        r11 = r2.get(r5);	 Catch:{ Exception -> 0x026d }
        r11 = (java.lang.Long) r11;	 Catch:{ Exception -> 0x026d }
        r12 = r4.contains(r11);	 Catch:{ Exception -> 0x026d }
        if (r12 != 0) goto L_0x00d4;	 Catch:{ Exception -> 0x026d }
    L_0x00d1:
        r4.add(r11);	 Catch:{ Exception -> 0x026d }
    L_0x00d4:
        r5 = r5 + 1;	 Catch:{ Exception -> 0x026d }
        goto L_0x00bf;	 Catch:{ Exception -> 0x026d }
    L_0x00d7:
        r5 = ",";	 Catch:{ Exception -> 0x026d }
        r5 = android.text.TextUtils.join(r5, r4);	 Catch:{ Exception -> 0x026d }
        r11 = new org.telegram.tgnet.TLRPC$TL_messages_dialogs;	 Catch:{ Exception -> 0x026d }
        r11.<init>();	 Catch:{ Exception -> 0x026d }
        r12 = new java.util.ArrayList;	 Catch:{ Exception -> 0x026d }
        r12.<init>();	 Catch:{ Exception -> 0x026d }
        r13 = new java.util.ArrayList;	 Catch:{ Exception -> 0x026d }
        r13.<init>();	 Catch:{ Exception -> 0x026d }
        r14 = new java.util.ArrayList;	 Catch:{ Exception -> 0x026d }
        r14.<init>();	 Catch:{ Exception -> 0x026d }
        r15 = new java.util.ArrayList;	 Catch:{ Exception -> 0x026d }
        r15.<init>();	 Catch:{ Exception -> 0x026d }
        r6 = r1.database;	 Catch:{ Exception -> 0x026d }
        r7 = java.util.Locale.US;	 Catch:{ Exception -> 0x026d }
        r9 = "SELECT d.did, d.last_mid, d.unread_count, d.date, m.data, m.read_state, m.mid, m.send_state, m.date, d.pts, d.inbox_max, d.outbox_max, d.pinned, d.unread_count_i FROM dialogs as d LEFT JOIN messages as m ON d.last_mid = m.mid WHERE d.did IN(%s)";	 Catch:{ Exception -> 0x026d }
        r2 = new java.lang.Object[r8];	 Catch:{ Exception -> 0x026d }
        r8 = 0;	 Catch:{ Exception -> 0x026d }
        r2[r8] = r5;	 Catch:{ Exception -> 0x026d }
        r2 = java.lang.String.format(r7, r9, r2);	 Catch:{ Exception -> 0x026d }
        r7 = new java.lang.Object[r8];	 Catch:{ Exception -> 0x026d }
        r2 = r6.queryFinalized(r2, r7);	 Catch:{ Exception -> 0x026d }
        r6 = r2.next();	 Catch:{ Exception -> 0x026d }
        if (r6 == 0) goto L_0x021f;	 Catch:{ Exception -> 0x026d }
    L_0x0111:
        r6 = new org.telegram.tgnet.TLRPC$TL_dialog;	 Catch:{ Exception -> 0x026d }
        r6.<init>();	 Catch:{ Exception -> 0x026d }
        r7 = 0;	 Catch:{ Exception -> 0x026d }
        r8 = r2.longValue(r7);	 Catch:{ Exception -> 0x026d }
        r6.id = r8;	 Catch:{ Exception -> 0x026d }
        r7 = 1;	 Catch:{ Exception -> 0x026d }
        r8 = r2.intValue(r7);	 Catch:{ Exception -> 0x026d }
        r6.top_message = r8;	 Catch:{ Exception -> 0x026d }
        r7 = 10;	 Catch:{ Exception -> 0x026d }
        r7 = r2.intValue(r7);	 Catch:{ Exception -> 0x026d }
        r6.read_inbox_max_id = r7;	 Catch:{ Exception -> 0x026d }
        r7 = 11;	 Catch:{ Exception -> 0x026d }
        r7 = r2.intValue(r7);	 Catch:{ Exception -> 0x026d }
        r6.read_outbox_max_id = r7;	 Catch:{ Exception -> 0x026d }
        r7 = 2;	 Catch:{ Exception -> 0x026d }
        r8 = r2.intValue(r7);	 Catch:{ Exception -> 0x026d }
        r6.unread_count = r8;	 Catch:{ Exception -> 0x026d }
        r8 = 13;	 Catch:{ Exception -> 0x026d }
        r8 = r2.intValue(r8);	 Catch:{ Exception -> 0x026d }
        r6.unread_mentions_count = r8;	 Catch:{ Exception -> 0x026d }
        r8 = 3;	 Catch:{ Exception -> 0x026d }
        r9 = r2.intValue(r8);	 Catch:{ Exception -> 0x026d }
        r6.last_message_date = r9;	 Catch:{ Exception -> 0x026d }
        r9 = 9;	 Catch:{ Exception -> 0x026d }
        r9 = r2.intValue(r9);	 Catch:{ Exception -> 0x026d }
        r6.pts = r9;	 Catch:{ Exception -> 0x026d }
        if (r3 != 0) goto L_0x0156;	 Catch:{ Exception -> 0x026d }
    L_0x0154:
        r9 = 0;	 Catch:{ Exception -> 0x026d }
        goto L_0x0157;	 Catch:{ Exception -> 0x026d }
    L_0x0156:
        r9 = 1;	 Catch:{ Exception -> 0x026d }
    L_0x0157:
        r6.flags = r9;	 Catch:{ Exception -> 0x026d }
        r9 = 12;	 Catch:{ Exception -> 0x026d }
        r9 = r2.intValue(r9);	 Catch:{ Exception -> 0x026d }
        r6.pinnedNum = r9;	 Catch:{ Exception -> 0x026d }
        r9 = r6.pinnedNum;	 Catch:{ Exception -> 0x026d }
        if (r9 == 0) goto L_0x0167;	 Catch:{ Exception -> 0x026d }
    L_0x0165:
        r9 = 1;	 Catch:{ Exception -> 0x026d }
        goto L_0x0168;	 Catch:{ Exception -> 0x026d }
    L_0x0167:
        r9 = 0;	 Catch:{ Exception -> 0x026d }
    L_0x0168:
        r6.pinned = r9;	 Catch:{ Exception -> 0x026d }
        r9 = r11.dialogs;	 Catch:{ Exception -> 0x026d }
        r9.add(r6);	 Catch:{ Exception -> 0x026d }
        r9 = 4;	 Catch:{ Exception -> 0x026d }
        r9 = r2.byteBufferValue(r9);	 Catch:{ Exception -> 0x026d }
        if (r9 == 0) goto L_0x01bc;	 Catch:{ Exception -> 0x026d }
    L_0x0176:
        r7 = 0;	 Catch:{ Exception -> 0x026d }
        r8 = r9.readInt32(r7);	 Catch:{ Exception -> 0x026d }
        r8 = org.telegram.tgnet.TLRPC.Message.TLdeserialize(r9, r8, r7);	 Catch:{ Exception -> 0x026d }
        r7 = r1.currentAccount;	 Catch:{ Exception -> 0x026d }
        r7 = org.telegram.messenger.UserConfig.getInstance(r7);	 Catch:{ Exception -> 0x026d }
        r7 = r7.clientUserId;	 Catch:{ Exception -> 0x026d }
        r8.readAttachPath(r9, r7);	 Catch:{ Exception -> 0x026d }
        r9.reuse();	 Catch:{ Exception -> 0x026d }
        r7 = 5;	 Catch:{ Exception -> 0x026d }
        r7 = r2.intValue(r7);	 Catch:{ Exception -> 0x026d }
        org.telegram.messenger.MessageObject.setUnreadFlags(r8, r7);	 Catch:{ Exception -> 0x026d }
        r7 = 6;	 Catch:{ Exception -> 0x026d }
        r7 = r2.intValue(r7);	 Catch:{ Exception -> 0x026d }
        r8.id = r7;	 Catch:{ Exception -> 0x026d }
        r7 = 7;	 Catch:{ Exception -> 0x026d }
        r7 = r2.intValue(r7);	 Catch:{ Exception -> 0x026d }
        r8.send_state = r7;	 Catch:{ Exception -> 0x026d }
        r7 = 8;	 Catch:{ Exception -> 0x026d }
        r7 = r2.intValue(r7);	 Catch:{ Exception -> 0x026d }
        if (r7 == 0) goto L_0x01ad;	 Catch:{ Exception -> 0x026d }
    L_0x01ab:
        r6.last_message_date = r7;	 Catch:{ Exception -> 0x026d }
    L_0x01ad:
        r16 = r4;	 Catch:{ Exception -> 0x026d }
        r3 = r6.id;	 Catch:{ Exception -> 0x026d }
        r8.dialog_id = r3;	 Catch:{ Exception -> 0x026d }
        r3 = r11.messages;	 Catch:{ Exception -> 0x026d }
        r3.add(r8);	 Catch:{ Exception -> 0x026d }
        addUsersAndChatsFromMessage(r8, r13, r14);	 Catch:{ Exception -> 0x026d }
        goto L_0x01be;	 Catch:{ Exception -> 0x026d }
    L_0x01bc:
        r16 = r4;	 Catch:{ Exception -> 0x026d }
    L_0x01be:
        r3 = r6.id;	 Catch:{ Exception -> 0x026d }
        r3 = (int) r3;	 Catch:{ Exception -> 0x026d }
        r7 = r6.id;	 Catch:{ Exception -> 0x026d }
        r4 = 32;	 Catch:{ Exception -> 0x026d }
        r7 = r7 >> r4;	 Catch:{ Exception -> 0x026d }
        r4 = (int) r7;	 Catch:{ Exception -> 0x026d }
        if (r3 == 0) goto L_0x0206;	 Catch:{ Exception -> 0x026d }
    L_0x01c9:
        r7 = 1;	 Catch:{ Exception -> 0x026d }
        if (r4 != r7) goto L_0x01de;	 Catch:{ Exception -> 0x026d }
    L_0x01cc:
        r8 = java.lang.Integer.valueOf(r3);	 Catch:{ Exception -> 0x026d }
        r8 = r14.contains(r8);	 Catch:{ Exception -> 0x026d }
        if (r8 != 0) goto L_0x0218;	 Catch:{ Exception -> 0x026d }
    L_0x01d6:
        r8 = java.lang.Integer.valueOf(r3);	 Catch:{ Exception -> 0x026d }
        r14.add(r8);	 Catch:{ Exception -> 0x026d }
        goto L_0x0218;	 Catch:{ Exception -> 0x026d }
    L_0x01de:
        if (r3 <= 0) goto L_0x01f2;	 Catch:{ Exception -> 0x026d }
        r8 = java.lang.Integer.valueOf(r3);	 Catch:{ Exception -> 0x026d }
        r8 = r13.contains(r8);	 Catch:{ Exception -> 0x026d }
        if (r8 != 0) goto L_0x0218;	 Catch:{ Exception -> 0x026d }
        r8 = java.lang.Integer.valueOf(r3);	 Catch:{ Exception -> 0x026d }
        r13.add(r8);	 Catch:{ Exception -> 0x026d }
        goto L_0x0218;	 Catch:{ Exception -> 0x026d }
        r8 = -r3;	 Catch:{ Exception -> 0x026d }
        r8 = java.lang.Integer.valueOf(r8);	 Catch:{ Exception -> 0x026d }
        r8 = r14.contains(r8);	 Catch:{ Exception -> 0x026d }
        if (r8 != 0) goto L_0x0218;	 Catch:{ Exception -> 0x026d }
        r8 = -r3;	 Catch:{ Exception -> 0x026d }
        r8 = java.lang.Integer.valueOf(r8);	 Catch:{ Exception -> 0x026d }
        r14.add(r8);	 Catch:{ Exception -> 0x026d }
        goto L_0x0218;	 Catch:{ Exception -> 0x026d }
    L_0x0206:
        r7 = 1;	 Catch:{ Exception -> 0x026d }
        r8 = java.lang.Integer.valueOf(r4);	 Catch:{ Exception -> 0x026d }
        r8 = r15.contains(r8);	 Catch:{ Exception -> 0x026d }
        if (r8 != 0) goto L_0x0218;	 Catch:{ Exception -> 0x026d }
        r8 = java.lang.Integer.valueOf(r4);	 Catch:{ Exception -> 0x026d }
        r15.add(r8);	 Catch:{ Exception -> 0x026d }
        r4 = r16;	 Catch:{ Exception -> 0x026d }
        r3 = r20;	 Catch:{ Exception -> 0x026d }
        goto L_0x010b;	 Catch:{ Exception -> 0x026d }
    L_0x021f:
        r16 = r4;	 Catch:{ Exception -> 0x026d }
        r2.dispose();	 Catch:{ Exception -> 0x026d }
        r3 = r15.isEmpty();	 Catch:{ Exception -> 0x026d }
        if (r3 != 0) goto L_0x0233;	 Catch:{ Exception -> 0x026d }
        r3 = ",";	 Catch:{ Exception -> 0x026d }
        r3 = android.text.TextUtils.join(r3, r15);	 Catch:{ Exception -> 0x026d }
        r1.getEncryptedChatsInternal(r3, r12, r13);	 Catch:{ Exception -> 0x026d }
        r3 = r14.isEmpty();	 Catch:{ Exception -> 0x026d }
        if (r3 != 0) goto L_0x0244;	 Catch:{ Exception -> 0x026d }
        r3 = ",";	 Catch:{ Exception -> 0x026d }
        r3 = android.text.TextUtils.join(r3, r14);	 Catch:{ Exception -> 0x026d }
        r4 = r11.chats;	 Catch:{ Exception -> 0x026d }
        r1.getChatsInternal(r3, r4);	 Catch:{ Exception -> 0x026d }
        r3 = r13.isEmpty();	 Catch:{ Exception -> 0x026d }
        if (r3 != 0) goto L_0x0255;	 Catch:{ Exception -> 0x026d }
        r3 = ",";	 Catch:{ Exception -> 0x026d }
        r3 = android.text.TextUtils.join(r3, r13);	 Catch:{ Exception -> 0x026d }
        r4 = r11.users;	 Catch:{ Exception -> 0x026d }
        r1.getUsersInternal(r3, r4);	 Catch:{ Exception -> 0x026d }
        r3 = r11.dialogs;	 Catch:{ Exception -> 0x026d }
        r3 = r3.isEmpty();	 Catch:{ Exception -> 0x026d }
        if (r3 == 0) goto L_0x0263;	 Catch:{ Exception -> 0x026d }
        r3 = r12.isEmpty();	 Catch:{ Exception -> 0x026d }
        if (r3 != 0) goto L_0x026c;	 Catch:{ Exception -> 0x026d }
        r3 = r1.currentAccount;	 Catch:{ Exception -> 0x026d }
        r3 = org.telegram.messenger.MessagesController.getInstance(r3);	 Catch:{ Exception -> 0x026d }
        r3.processDialogsUpdate(r11, r12);	 Catch:{ Exception -> 0x026d }
        goto L_0x0276;
    L_0x026d:
        r0 = move-exception;
        goto L_0x0272;
    L_0x026f:
        r0 = move-exception;
        r10 = r18;
        r2 = r0;
        org.telegram.messenger.FileLog.e(r2);
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.MessagesStorage.updateDialogsWithDeletedMessagesInternal(java.util.ArrayList, java.util.ArrayList, int):void");
    }

    private void updateDialogsWithReadMessagesInternal(java.util.ArrayList<java.lang.Integer> r1, org.telegram.messenger.support.SparseLongArray r2, org.telegram.messenger.support.SparseLongArray r3, java.util.ArrayList<java.lang.Long> r4) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.MessagesStorage.updateDialogsWithReadMessagesInternal(java.util.ArrayList, org.telegram.messenger.support.SparseLongArray, org.telegram.messenger.support.SparseLongArray, java.util.ArrayList):void
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
        r1 = r22;
        r2 = r24;
        r3 = r25;
        r4 = r26;
        r5 = new android.util.LongSparseArray;	 Catch:{ Exception -> 0x02b7 }
        r5.<init>();	 Catch:{ Exception -> 0x02b7 }
        r6 = new android.util.LongSparseArray;	 Catch:{ Exception -> 0x02b7 }
        r6.<init>();	 Catch:{ Exception -> 0x02b7 }
        r7 = new java.util.ArrayList;	 Catch:{ Exception -> 0x02b7 }
        r7.<init>();	 Catch:{ Exception -> 0x02b7 }
        r8 = isEmpty(r23);	 Catch:{ Exception -> 0x02b7 }
        r10 = 0;	 Catch:{ Exception -> 0x02b7 }
        r11 = 1;	 Catch:{ Exception -> 0x02b7 }
        if (r8 != 0) goto L_0x008f;	 Catch:{ Exception -> 0x02b7 }
    L_0x001f:
        r8 = ",";	 Catch:{ Exception -> 0x02b7 }
        r12 = r23;	 Catch:{ Exception -> 0x02b7 }
        r8 = android.text.TextUtils.join(r8, r12);	 Catch:{ Exception -> 0x02b7 }
        r13 = r1.database;	 Catch:{ Exception -> 0x02b7 }
        r14 = java.util.Locale.US;	 Catch:{ Exception -> 0x02b7 }
        r15 = "SELECT uid, read_state, out FROM messages WHERE mid IN(%s)";	 Catch:{ Exception -> 0x02b7 }
        r9 = new java.lang.Object[r11];	 Catch:{ Exception -> 0x02b7 }
        r9[r10] = r8;	 Catch:{ Exception -> 0x02b7 }
        r9 = java.lang.String.format(r14, r15, r9);	 Catch:{ Exception -> 0x02b7 }
        r14 = new java.lang.Object[r10];	 Catch:{ Exception -> 0x02b7 }
        r9 = r13.queryFinalized(r9, r14);	 Catch:{ Exception -> 0x02b7 }
        r13 = r9.next();	 Catch:{ Exception -> 0x02b7 }
        if (r13 == 0) goto L_0x0086;	 Catch:{ Exception -> 0x02b7 }
    L_0x0041:
        r13 = 2;	 Catch:{ Exception -> 0x02b7 }
        r14 = r9.intValue(r13);	 Catch:{ Exception -> 0x02b7 }
        r13 = r14;	 Catch:{ Exception -> 0x02b7 }
        if (r13 == 0) goto L_0x004d;	 Catch:{ Exception -> 0x02b7 }
    L_0x004a:
        r20 = r8;	 Catch:{ Exception -> 0x02b7 }
        goto L_0x007f;	 Catch:{ Exception -> 0x02b7 }
    L_0x004d:
        r14 = r9.intValue(r11);	 Catch:{ Exception -> 0x02b7 }
        if (r14 == 0) goto L_0x0054;	 Catch:{ Exception -> 0x02b7 }
    L_0x0053:
        goto L_0x004a;	 Catch:{ Exception -> 0x02b7 }
    L_0x0054:
        r16 = r9.longValue(r10);	 Catch:{ Exception -> 0x02b7 }
        r18 = r16;	 Catch:{ Exception -> 0x02b7 }
        r10 = r18;	 Catch:{ Exception -> 0x02b7 }
        r15 = r5.get(r10);	 Catch:{ Exception -> 0x02b7 }
        r15 = (java.lang.Integer) r15;	 Catch:{ Exception -> 0x02b7 }
        if (r15 != 0) goto L_0x006f;	 Catch:{ Exception -> 0x02b7 }
    L_0x0064:
        r20 = r8;	 Catch:{ Exception -> 0x02b7 }
        r8 = 1;	 Catch:{ Exception -> 0x02b7 }
        r12 = java.lang.Integer.valueOf(r8);	 Catch:{ Exception -> 0x02b7 }
        r5.put(r10, r12);	 Catch:{ Exception -> 0x02b7 }
        goto L_0x007e;	 Catch:{ Exception -> 0x02b7 }
    L_0x006f:
        r20 = r8;	 Catch:{ Exception -> 0x02b7 }
        r8 = r15.intValue();	 Catch:{ Exception -> 0x02b7 }
        r12 = 1;	 Catch:{ Exception -> 0x02b7 }
        r8 = r8 + r12;	 Catch:{ Exception -> 0x02b7 }
        r8 = java.lang.Integer.valueOf(r8);	 Catch:{ Exception -> 0x02b7 }
        r5.put(r10, r8);	 Catch:{ Exception -> 0x02b7 }
    L_0x007f:
        r8 = r20;	 Catch:{ Exception -> 0x02b7 }
        r10 = 0;	 Catch:{ Exception -> 0x02b7 }
        r11 = 1;	 Catch:{ Exception -> 0x02b7 }
        r12 = r23;	 Catch:{ Exception -> 0x02b7 }
        goto L_0x003b;	 Catch:{ Exception -> 0x02b7 }
    L_0x0086:
        r20 = r8;	 Catch:{ Exception -> 0x02b7 }
        r9.dispose();	 Catch:{ Exception -> 0x02b7 }
        r13 = 0;	 Catch:{ Exception -> 0x02b7 }
        goto L_0x0219;	 Catch:{ Exception -> 0x02b7 }
    L_0x008f:
        r8 = isEmpty(r24);	 Catch:{ Exception -> 0x02b7 }
        if (r8 != 0) goto L_0x0103;	 Catch:{ Exception -> 0x02b7 }
        r8 = 0;	 Catch:{ Exception -> 0x02b7 }
        r10 = r24.size();	 Catch:{ Exception -> 0x02b7 }
        if (r8 >= r10) goto L_0x0103;	 Catch:{ Exception -> 0x02b7 }
        r10 = r2.keyAt(r8);	 Catch:{ Exception -> 0x02b7 }
        r11 = r2.get(r10);	 Catch:{ Exception -> 0x02b7 }
        r13 = r1.database;	 Catch:{ Exception -> 0x02b7 }
        r14 = java.util.Locale.US;	 Catch:{ Exception -> 0x02b7 }
        r15 = "SELECT COUNT(mid) FROM messages WHERE uid = %d AND mid > %d AND read_state IN(0,2) AND out = 0";	 Catch:{ Exception -> 0x02b7 }
        r9 = 2;	 Catch:{ Exception -> 0x02b7 }
        r2 = new java.lang.Object[r9];	 Catch:{ Exception -> 0x02b7 }
        r9 = java.lang.Integer.valueOf(r10);	 Catch:{ Exception -> 0x02b7 }
        r16 = 0;	 Catch:{ Exception -> 0x02b7 }
        r2[r16] = r9;	 Catch:{ Exception -> 0x02b7 }
        r9 = java.lang.Long.valueOf(r11);	 Catch:{ Exception -> 0x02b7 }
        r16 = 1;	 Catch:{ Exception -> 0x02b7 }
        r2[r16] = r9;	 Catch:{ Exception -> 0x02b7 }
        r2 = java.lang.String.format(r14, r15, r2);	 Catch:{ Exception -> 0x02b7 }
        r9 = 0;	 Catch:{ Exception -> 0x02b7 }
        r14 = new java.lang.Object[r9];	 Catch:{ Exception -> 0x02b7 }
        r2 = r13.queryFinalized(r2, r14);	 Catch:{ Exception -> 0x02b7 }
        r9 = r2.next();	 Catch:{ Exception -> 0x02b7 }
        if (r9 == 0) goto L_0x00db;	 Catch:{ Exception -> 0x02b7 }
        r13 = (long) r10;	 Catch:{ Exception -> 0x02b7 }
        r9 = 0;	 Catch:{ Exception -> 0x02b7 }
        r15 = r2.intValue(r9);	 Catch:{ Exception -> 0x02b7 }
        r9 = java.lang.Integer.valueOf(r15);	 Catch:{ Exception -> 0x02b7 }
        r5.put(r13, r9);	 Catch:{ Exception -> 0x02b7 }
        r2.dispose();	 Catch:{ Exception -> 0x02b7 }
        r9 = r1.database;	 Catch:{ Exception -> 0x02b7 }
        r13 = "UPDATE dialogs SET inbox_max = max((SELECT inbox_max FROM dialogs WHERE did = ?), ?) WHERE did = ?";	 Catch:{ Exception -> 0x02b7 }
        r9 = r9.executeFast(r13);	 Catch:{ Exception -> 0x02b7 }
        r9.requery();	 Catch:{ Exception -> 0x02b7 }
        r13 = (long) r10;	 Catch:{ Exception -> 0x02b7 }
        r15 = 1;	 Catch:{ Exception -> 0x02b7 }
        r9.bindLong(r15, r13);	 Catch:{ Exception -> 0x02b7 }
        r13 = (int) r11;	 Catch:{ Exception -> 0x02b7 }
        r14 = 2;	 Catch:{ Exception -> 0x02b7 }
        r9.bindInteger(r14, r13);	 Catch:{ Exception -> 0x02b7 }
        r13 = (long) r10;	 Catch:{ Exception -> 0x02b7 }
        r15 = 3;	 Catch:{ Exception -> 0x02b7 }
        r9.bindLong(r15, r13);	 Catch:{ Exception -> 0x02b7 }
        r9.step();	 Catch:{ Exception -> 0x02b7 }
        r9.dispose();	 Catch:{ Exception -> 0x02b7 }
        r8 = r8 + 1;	 Catch:{ Exception -> 0x02b7 }
        r2 = r24;	 Catch:{ Exception -> 0x02b7 }
        goto L_0x0096;	 Catch:{ Exception -> 0x02b7 }
        r2 = isEmpty(r26);	 Catch:{ Exception -> 0x02b7 }
        if (r2 != 0) goto L_0x01e0;	 Catch:{ Exception -> 0x02b7 }
        r2 = new java.util.ArrayList;	 Catch:{ Exception -> 0x02b7 }
        r2.<init>(r4);	 Catch:{ Exception -> 0x02b7 }
        r8 = ",";	 Catch:{ Exception -> 0x02b7 }
        r8 = android.text.TextUtils.join(r8, r4);	 Catch:{ Exception -> 0x02b7 }
        r9 = r1.database;	 Catch:{ Exception -> 0x02b7 }
        r10 = java.util.Locale.US;	 Catch:{ Exception -> 0x02b7 }
        r11 = "SELECT uid, read_state, out, mention, mid FROM messages WHERE mid IN(%s)";	 Catch:{ Exception -> 0x02b7 }
        r12 = 1;	 Catch:{ Exception -> 0x02b7 }
        r13 = new java.lang.Object[r12];	 Catch:{ Exception -> 0x02b7 }
        r12 = 0;	 Catch:{ Exception -> 0x02b7 }
        r13[r12] = r8;	 Catch:{ Exception -> 0x02b7 }
        r10 = java.lang.String.format(r10, r11, r13);	 Catch:{ Exception -> 0x02b7 }
        r11 = new java.lang.Object[r12];	 Catch:{ Exception -> 0x02b7 }
        r9 = r9.queryFinalized(r10, r11);	 Catch:{ Exception -> 0x02b7 }
        r10 = r9.next();	 Catch:{ Exception -> 0x02b7 }
        if (r10 == 0) goto L_0x01b1;	 Catch:{ Exception -> 0x02b7 }
        r10 = 0;	 Catch:{ Exception -> 0x02b7 }
        r11 = r9.longValue(r10);	 Catch:{ Exception -> 0x02b7 }
        r10 = r11;	 Catch:{ Exception -> 0x02b7 }
        r12 = 4;	 Catch:{ Exception -> 0x02b7 }
        r12 = r9.longValue(r12);	 Catch:{ Exception -> 0x02b7 }
        r12 = java.lang.Long.valueOf(r12);	 Catch:{ Exception -> 0x02b7 }
        r2.remove(r12);	 Catch:{ Exception -> 0x02b7 }
        r12 = 1;	 Catch:{ Exception -> 0x02b7 }
        r13 = r9.intValue(r12);	 Catch:{ Exception -> 0x02b7 }
        r12 = 2;	 Catch:{ Exception -> 0x02b7 }
        if (r13 >= r12) goto L_0x01ac;	 Catch:{ Exception -> 0x02b7 }
        r13 = r9.intValue(r12);	 Catch:{ Exception -> 0x02b7 }
        if (r13 != 0) goto L_0x01ac;	 Catch:{ Exception -> 0x02b7 }
        r12 = 3;	 Catch:{ Exception -> 0x02b7 }
        r13 = r9.intValue(r12);	 Catch:{ Exception -> 0x02b7 }
        r12 = 1;	 Catch:{ Exception -> 0x02b7 }
        if (r13 != r12) goto L_0x01ac;	 Catch:{ Exception -> 0x02b7 }
        r12 = r6.get(r10);	 Catch:{ Exception -> 0x02b7 }
        r12 = (java.lang.Integer) r12;	 Catch:{ Exception -> 0x02b7 }
        if (r12 != 0) goto L_0x0199;	 Catch:{ Exception -> 0x02b7 }
        r13 = r1.database;	 Catch:{ Exception -> 0x02b7 }
        r14 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x02b7 }
        r14.<init>();	 Catch:{ Exception -> 0x02b7 }
        r15 = "SELECT unread_count_i FROM dialogs WHERE did = ";	 Catch:{ Exception -> 0x02b7 }
        r14.append(r15);	 Catch:{ Exception -> 0x02b7 }
        r14.append(r10);	 Catch:{ Exception -> 0x02b7 }
        r14 = r14.toString();	 Catch:{ Exception -> 0x02b7 }
        r15 = 0;	 Catch:{ Exception -> 0x02b7 }
        r4 = new java.lang.Object[r15];	 Catch:{ Exception -> 0x02b7 }
        r4 = r13.queryFinalized(r14, r4);	 Catch:{ Exception -> 0x02b7 }
        r13 = 0;	 Catch:{ Exception -> 0x02b7 }
        r14 = r4.next();	 Catch:{ Exception -> 0x02b7 }
        if (r14 == 0) goto L_0x0187;	 Catch:{ Exception -> 0x02b7 }
        r14 = 0;	 Catch:{ Exception -> 0x02b7 }
        r15 = r4.intValue(r14);	 Catch:{ Exception -> 0x02b7 }
        r13 = r15;	 Catch:{ Exception -> 0x02b7 }
        r4.dispose();	 Catch:{ Exception -> 0x02b7 }
        r14 = r13 + -1;	 Catch:{ Exception -> 0x02b7 }
        r15 = 0;	 Catch:{ Exception -> 0x02b7 }
        r14 = java.lang.Math.max(r15, r14);	 Catch:{ Exception -> 0x02b7 }
        r14 = java.lang.Integer.valueOf(r14);	 Catch:{ Exception -> 0x02b7 }
        r6.put(r10, r14);	 Catch:{ Exception -> 0x02b7 }
        goto L_0x01ac;	 Catch:{ Exception -> 0x02b7 }
        r4 = r12.intValue();	 Catch:{ Exception -> 0x02b7 }
        r13 = 1;	 Catch:{ Exception -> 0x02b7 }
        r4 = r4 - r13;	 Catch:{ Exception -> 0x02b7 }
        r13 = 0;	 Catch:{ Exception -> 0x02b7 }
        r4 = java.lang.Math.max(r13, r4);	 Catch:{ Exception -> 0x02b7 }
        r4 = java.lang.Integer.valueOf(r4);	 Catch:{ Exception -> 0x02b7 }
        r6.put(r10, r4);	 Catch:{ Exception -> 0x02b7 }
        goto L_0x01ad;	 Catch:{ Exception -> 0x02b7 }
        r13 = 0;	 Catch:{ Exception -> 0x02b7 }
        r4 = r26;	 Catch:{ Exception -> 0x02b7 }
        goto L_0x012a;	 Catch:{ Exception -> 0x02b7 }
        r13 = 0;	 Catch:{ Exception -> 0x02b7 }
        r9.dispose();	 Catch:{ Exception -> 0x02b7 }
        r4 = r13;	 Catch:{ Exception -> 0x02b7 }
        r10 = r2.size();	 Catch:{ Exception -> 0x02b7 }
        if (r4 >= r10) goto L_0x01e1;	 Catch:{ Exception -> 0x02b7 }
        r10 = r2.get(r4);	 Catch:{ Exception -> 0x02b7 }
        r10 = (java.lang.Long) r10;	 Catch:{ Exception -> 0x02b7 }
        r10 = r10.longValue();	 Catch:{ Exception -> 0x02b7 }
        r12 = 32;	 Catch:{ Exception -> 0x02b7 }
        r10 = r10 >> r12;	 Catch:{ Exception -> 0x02b7 }
        r10 = (int) r10;	 Catch:{ Exception -> 0x02b7 }
        if (r10 <= 0) goto L_0x01dd;	 Catch:{ Exception -> 0x02b7 }
        r11 = java.lang.Integer.valueOf(r10);	 Catch:{ Exception -> 0x02b7 }
        r11 = r7.contains(r11);	 Catch:{ Exception -> 0x02b7 }
        if (r11 != 0) goto L_0x01dd;	 Catch:{ Exception -> 0x02b7 }
        r11 = java.lang.Integer.valueOf(r10);	 Catch:{ Exception -> 0x02b7 }
        r7.add(r11);	 Catch:{ Exception -> 0x02b7 }
        r4 = r4 + 1;	 Catch:{ Exception -> 0x02b7 }
        goto L_0x01b6;	 Catch:{ Exception -> 0x02b7 }
        r13 = 0;	 Catch:{ Exception -> 0x02b7 }
        r2 = isEmpty(r25);	 Catch:{ Exception -> 0x02b7 }
        if (r2 != 0) goto L_0x0219;	 Catch:{ Exception -> 0x02b7 }
        r2 = r13;	 Catch:{ Exception -> 0x02b7 }
        r4 = r25.size();	 Catch:{ Exception -> 0x02b7 }
        if (r2 >= r4) goto L_0x0219;	 Catch:{ Exception -> 0x02b7 }
        r4 = r3.keyAt(r2);	 Catch:{ Exception -> 0x02b7 }
        r8 = r3.get(r4);	 Catch:{ Exception -> 0x02b7 }
        r10 = r1.database;	 Catch:{ Exception -> 0x02b7 }
        r11 = "UPDATE dialogs SET outbox_max = max((SELECT outbox_max FROM dialogs WHERE did = ?), ?) WHERE did = ?";	 Catch:{ Exception -> 0x02b7 }
        r10 = r10.executeFast(r11);	 Catch:{ Exception -> 0x02b7 }
        r10.requery();	 Catch:{ Exception -> 0x02b7 }
        r11 = (long) r4;	 Catch:{ Exception -> 0x02b7 }
        r14 = 1;	 Catch:{ Exception -> 0x02b7 }
        r10.bindLong(r14, r11);	 Catch:{ Exception -> 0x02b7 }
        r11 = (int) r8;	 Catch:{ Exception -> 0x02b7 }
        r12 = 2;	 Catch:{ Exception -> 0x02b7 }
        r10.bindInteger(r12, r11);	 Catch:{ Exception -> 0x02b7 }
        r11 = (long) r4;	 Catch:{ Exception -> 0x02b7 }
        r14 = 3;	 Catch:{ Exception -> 0x02b7 }
        r10.bindLong(r14, r11);	 Catch:{ Exception -> 0x02b7 }
        r10.step();	 Catch:{ Exception -> 0x02b7 }
        r10.dispose();	 Catch:{ Exception -> 0x02b7 }
        r2 = r2 + 1;	 Catch:{ Exception -> 0x02b7 }
        goto L_0x01e8;	 Catch:{ Exception -> 0x02b7 }
        r2 = r5.size();	 Catch:{ Exception -> 0x02b7 }
        if (r2 > 0) goto L_0x0225;	 Catch:{ Exception -> 0x02b7 }
        r2 = r6.size();	 Catch:{ Exception -> 0x02b7 }
        if (r2 <= 0) goto L_0x029e;	 Catch:{ Exception -> 0x02b7 }
        r2 = r1.database;	 Catch:{ Exception -> 0x02b7 }
        r2.beginTransaction();	 Catch:{ Exception -> 0x02b7 }
        r2 = r5.size();	 Catch:{ Exception -> 0x02b7 }
        if (r2 <= 0) goto L_0x0261;	 Catch:{ Exception -> 0x02b7 }
        r2 = r1.database;	 Catch:{ Exception -> 0x02b7 }
        r4 = "UPDATE dialogs SET unread_count = ? WHERE did = ?";	 Catch:{ Exception -> 0x02b7 }
        r2 = r2.executeFast(r4);	 Catch:{ Exception -> 0x02b7 }
        r4 = r13;	 Catch:{ Exception -> 0x02b7 }
        r8 = r5.size();	 Catch:{ Exception -> 0x02b7 }
        if (r4 >= r8) goto L_0x025e;	 Catch:{ Exception -> 0x02b7 }
        r2.requery();	 Catch:{ Exception -> 0x02b7 }
        r8 = r5.valueAt(r4);	 Catch:{ Exception -> 0x02b7 }
        r8 = (java.lang.Integer) r8;	 Catch:{ Exception -> 0x02b7 }
        r8 = r8.intValue();	 Catch:{ Exception -> 0x02b7 }
        r9 = 1;	 Catch:{ Exception -> 0x02b7 }
        r2.bindInteger(r9, r8);	 Catch:{ Exception -> 0x02b7 }
        r8 = r5.keyAt(r4);	 Catch:{ Exception -> 0x02b7 }
        r10 = 2;	 Catch:{ Exception -> 0x02b7 }
        r2.bindLong(r10, r8);	 Catch:{ Exception -> 0x02b7 }
        r2.step();	 Catch:{ Exception -> 0x02b7 }
        r4 = r4 + 1;	 Catch:{ Exception -> 0x02b7 }
        goto L_0x0239;	 Catch:{ Exception -> 0x02b7 }
        r2.dispose();	 Catch:{ Exception -> 0x02b7 }
        r2 = r6.size();	 Catch:{ Exception -> 0x02b7 }
        if (r2 <= 0) goto L_0x0299;	 Catch:{ Exception -> 0x02b7 }
        r2 = r1.database;	 Catch:{ Exception -> 0x02b7 }
        r4 = "UPDATE dialogs SET unread_count_i = ? WHERE did = ?";	 Catch:{ Exception -> 0x02b7 }
        r2 = r2.executeFast(r4);	 Catch:{ Exception -> 0x02b7 }
        r4 = r13;	 Catch:{ Exception -> 0x02b7 }
        r8 = r6.size();	 Catch:{ Exception -> 0x02b7 }
        if (r4 >= r8) goto L_0x0296;	 Catch:{ Exception -> 0x02b7 }
        r2.requery();	 Catch:{ Exception -> 0x02b7 }
        r8 = r6.valueAt(r4);	 Catch:{ Exception -> 0x02b7 }
        r8 = (java.lang.Integer) r8;	 Catch:{ Exception -> 0x02b7 }
        r8 = r8.intValue();	 Catch:{ Exception -> 0x02b7 }
        r9 = 1;	 Catch:{ Exception -> 0x02b7 }
        r2.bindInteger(r9, r8);	 Catch:{ Exception -> 0x02b7 }
        r10 = r6.keyAt(r4);	 Catch:{ Exception -> 0x02b7 }
        r8 = 2;	 Catch:{ Exception -> 0x02b7 }
        r2.bindLong(r8, r10);	 Catch:{ Exception -> 0x02b7 }
        r2.step();	 Catch:{ Exception -> 0x02b7 }
        r13 = r4 + 1;	 Catch:{ Exception -> 0x02b7 }
        goto L_0x0270;	 Catch:{ Exception -> 0x02b7 }
        r2.dispose();	 Catch:{ Exception -> 0x02b7 }
        r2 = r1.database;	 Catch:{ Exception -> 0x02b7 }
        r2.commitTransaction();	 Catch:{ Exception -> 0x02b7 }
        r2 = r1.currentAccount;	 Catch:{ Exception -> 0x02b7 }
        r2 = org.telegram.messenger.MessagesController.getInstance(r2);	 Catch:{ Exception -> 0x02b7 }
        r2.processDialogsUpdateRead(r5, r6);	 Catch:{ Exception -> 0x02b7 }
        r2 = r7.isEmpty();	 Catch:{ Exception -> 0x02b7 }
        if (r2 != 0) goto L_0x02b6;	 Catch:{ Exception -> 0x02b7 }
        r2 = r1.currentAccount;	 Catch:{ Exception -> 0x02b7 }
        r2 = org.telegram.messenger.MessagesController.getInstance(r2);	 Catch:{ Exception -> 0x02b7 }
        r2.reloadMentionsCountForChannels(r7);	 Catch:{ Exception -> 0x02b7 }
        goto L_0x02bc;
    L_0x02b7:
        r0 = move-exception;
        r2 = r0;
        org.telegram.messenger.FileLog.e(r2);
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.MessagesStorage.updateDialogsWithReadMessagesInternal(java.util.ArrayList, org.telegram.messenger.support.SparseLongArray, org.telegram.messenger.support.SparseLongArray, java.util.ArrayList):void");
    }

    public void closeHolesInMedia(long r1, int r3, int r4, int r5) throws java.lang.Exception {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.MessagesStorage.closeHolesInMedia(long, int, int, int):void
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
        r1 = r23;
        r2 = r24;
        r4 = r26;
        r5 = r27;
        r6 = 0;
        r10 = 9;
        r12 = 4;
        r13 = 3;
        r14 = 2;
        r15 = 0;
        r7 = 1;
        if (r28 >= 0) goto L_0x0067;
    L_0x0012:
        r8 = r1.database;	 Catch:{ Exception -> 0x0063 }
        r9 = java.util.Locale.US;	 Catch:{ Exception -> 0x0063 }
        r11 = "SELECT type, start, end FROM media_holes_v2 WHERE uid = %d AND type >= 0 AND ((end >= %d AND end <= %d) OR (start >= %d AND start <= %d) OR (start >= %d AND end <= %d) OR (start <= %d AND end >= %d))";	 Catch:{ Exception -> 0x0063 }
        r10 = new java.lang.Object[r10];	 Catch:{ Exception -> 0x0063 }
        r20 = java.lang.Long.valueOf(r24);	 Catch:{ Exception -> 0x0063 }
        r10[r15] = r20;	 Catch:{ Exception -> 0x0063 }
        r20 = java.lang.Integer.valueOf(r26);	 Catch:{ Exception -> 0x0063 }
        r10[r7] = r20;	 Catch:{ Exception -> 0x0063 }
        r20 = java.lang.Integer.valueOf(r27);	 Catch:{ Exception -> 0x0063 }
        r10[r14] = r20;	 Catch:{ Exception -> 0x0063 }
        r20 = java.lang.Integer.valueOf(r26);	 Catch:{ Exception -> 0x0063 }
        r10[r13] = r20;	 Catch:{ Exception -> 0x0063 }
        r20 = java.lang.Integer.valueOf(r27);	 Catch:{ Exception -> 0x0063 }
        r10[r12] = r20;	 Catch:{ Exception -> 0x0063 }
        r20 = java.lang.Integer.valueOf(r26);	 Catch:{ Exception -> 0x0063 }
        r19 = 5;	 Catch:{ Exception -> 0x0063 }
        r10[r19] = r20;	 Catch:{ Exception -> 0x0063 }
        r20 = java.lang.Integer.valueOf(r27);	 Catch:{ Exception -> 0x0063 }
        r18 = 6;	 Catch:{ Exception -> 0x0063 }
        r10[r18] = r20;	 Catch:{ Exception -> 0x0063 }
        r18 = java.lang.Integer.valueOf(r26);	 Catch:{ Exception -> 0x0063 }
        r17 = 7;	 Catch:{ Exception -> 0x0063 }
        r10[r17] = r18;	 Catch:{ Exception -> 0x0063 }
        r17 = java.lang.Integer.valueOf(r27);	 Catch:{ Exception -> 0x0063 }
        r16 = 8;	 Catch:{ Exception -> 0x0063 }
        r10[r16] = r17;	 Catch:{ Exception -> 0x0063 }
        r9 = java.lang.String.format(r9, r11, r10);	 Catch:{ Exception -> 0x0063 }
        r10 = new java.lang.Object[r15];	 Catch:{ Exception -> 0x0063 }
        r8 = r8.queryFinalized(r9, r10);	 Catch:{ Exception -> 0x0063 }
        goto L_0x00c1;	 Catch:{ Exception -> 0x0063 }
    L_0x0063:
        r0 = move-exception;	 Catch:{ Exception -> 0x0063 }
        r6 = r0;	 Catch:{ Exception -> 0x0063 }
        goto L_0x0285;	 Catch:{ Exception -> 0x0063 }
    L_0x0067:
        r8 = r1.database;	 Catch:{ Exception -> 0x0063 }
        r9 = java.util.Locale.US;	 Catch:{ Exception -> 0x0063 }
        r11 = "SELECT type, start, end FROM media_holes_v2 WHERE uid = %d AND type = %d AND ((end >= %d AND end <= %d) OR (start >= %d AND start <= %d) OR (start >= %d AND end <= %d) OR (start <= %d AND end >= %d))";	 Catch:{ Exception -> 0x0063 }
        r10 = 10;	 Catch:{ Exception -> 0x0063 }
        r10 = new java.lang.Object[r10];	 Catch:{ Exception -> 0x0063 }
        r20 = java.lang.Long.valueOf(r24);	 Catch:{ Exception -> 0x0063 }
        r10[r15] = r20;	 Catch:{ Exception -> 0x0063 }
        r20 = java.lang.Integer.valueOf(r28);	 Catch:{ Exception -> 0x0063 }
        r10[r7] = r20;	 Catch:{ Exception -> 0x0063 }
        r20 = java.lang.Integer.valueOf(r26);	 Catch:{ Exception -> 0x0063 }
        r10[r14] = r20;	 Catch:{ Exception -> 0x0063 }
        r20 = java.lang.Integer.valueOf(r27);	 Catch:{ Exception -> 0x0063 }
        r10[r13] = r20;	 Catch:{ Exception -> 0x0063 }
        r20 = java.lang.Integer.valueOf(r26);	 Catch:{ Exception -> 0x0063 }
        r10[r12] = r20;	 Catch:{ Exception -> 0x0063 }
        r20 = java.lang.Integer.valueOf(r27);	 Catch:{ Exception -> 0x0063 }
        r19 = 5;	 Catch:{ Exception -> 0x0063 }
        r10[r19] = r20;	 Catch:{ Exception -> 0x0063 }
        r20 = java.lang.Integer.valueOf(r26);	 Catch:{ Exception -> 0x0063 }
        r18 = 6;	 Catch:{ Exception -> 0x0063 }
        r10[r18] = r20;	 Catch:{ Exception -> 0x0063 }
        r18 = java.lang.Integer.valueOf(r27);	 Catch:{ Exception -> 0x0063 }
        r17 = 7;	 Catch:{ Exception -> 0x0063 }
        r10[r17] = r18;	 Catch:{ Exception -> 0x0063 }
        r17 = java.lang.Integer.valueOf(r26);	 Catch:{ Exception -> 0x0063 }
        r16 = 8;	 Catch:{ Exception -> 0x0063 }
        r10[r16] = r17;	 Catch:{ Exception -> 0x0063 }
        r16 = java.lang.Integer.valueOf(r27);	 Catch:{ Exception -> 0x0063 }
        r17 = 9;	 Catch:{ Exception -> 0x0063 }
        r10[r17] = r16;	 Catch:{ Exception -> 0x0063 }
        r9 = java.lang.String.format(r9, r11, r10);	 Catch:{ Exception -> 0x0063 }
        r10 = new java.lang.Object[r15];	 Catch:{ Exception -> 0x0063 }
        r8 = r8.queryFinalized(r9, r10);	 Catch:{ Exception -> 0x0063 }
    L_0x00c1:
        r9 = 0;	 Catch:{ Exception -> 0x0063 }
        r10 = r8.next();	 Catch:{ Exception -> 0x0063 }
        if (r10 == 0) goto L_0x00f1;	 Catch:{ Exception -> 0x0063 }
        if (r9 != 0) goto L_0x00d0;	 Catch:{ Exception -> 0x0063 }
        r10 = new java.util.ArrayList;	 Catch:{ Exception -> 0x0063 }
        r10.<init>();	 Catch:{ Exception -> 0x0063 }
        r9 = r10;	 Catch:{ Exception -> 0x0063 }
        r10 = r8.intValue(r15);	 Catch:{ Exception -> 0x0063 }
        r11 = r8.intValue(r7);	 Catch:{ Exception -> 0x0063 }
        r16 = r8.intValue(r14);	 Catch:{ Exception -> 0x0063 }
        r21 = r16;	 Catch:{ Exception -> 0x0063 }
        r13 = r21;	 Catch:{ Exception -> 0x0063 }
        if (r11 != r13) goto L_0x00e5;	 Catch:{ Exception -> 0x0063 }
        if (r11 != r7) goto L_0x00e5;	 Catch:{ Exception -> 0x0063 }
        goto L_0x00ee;	 Catch:{ Exception -> 0x0063 }
        r14 = new org.telegram.messenger.MessagesStorage$Hole;	 Catch:{ Exception -> 0x0063 }
        r14.<init>(r10, r11, r13);	 Catch:{ Exception -> 0x0063 }
        r9.add(r14);	 Catch:{ Exception -> 0x0063 }
        r13 = 3;	 Catch:{ Exception -> 0x0063 }
        r14 = 2;	 Catch:{ Exception -> 0x0063 }
        goto L_0x00c2;	 Catch:{ Exception -> 0x0063 }
        r8.dispose();	 Catch:{ Exception -> 0x0063 }
        if (r9 == 0) goto L_0x0284;	 Catch:{ Exception -> 0x0063 }
        r10 = r15;	 Catch:{ Exception -> 0x0063 }
        r11 = r9.size();	 Catch:{ Exception -> 0x0063 }
        if (r10 >= r11) goto L_0x0284;	 Catch:{ Exception -> 0x0063 }
        r11 = r9.get(r10);	 Catch:{ Exception -> 0x0063 }
        r11 = (org.telegram.messenger.MessagesStorage.Hole) r11;	 Catch:{ Exception -> 0x0063 }
        r13 = r11.end;	 Catch:{ Exception -> 0x0063 }
        r13 = r13 - r7;	 Catch:{ Exception -> 0x0063 }
        if (r5 < r13) goto L_0x014b;	 Catch:{ Exception -> 0x0063 }
        r13 = r11.start;	 Catch:{ Exception -> 0x0063 }
        r13 = r13 + r7;	 Catch:{ Exception -> 0x0063 }
        if (r4 > r13) goto L_0x014b;	 Catch:{ Exception -> 0x0063 }
        r13 = r1.database;	 Catch:{ Exception -> 0x0063 }
        r14 = java.util.Locale.US;	 Catch:{ Exception -> 0x0063 }
        r7 = "DELETE FROM media_holes_v2 WHERE uid = %d AND type = %d AND start = %d AND end = %d";	 Catch:{ Exception -> 0x0063 }
        r15 = new java.lang.Object[r12];	 Catch:{ Exception -> 0x0063 }
        r16 = java.lang.Long.valueOf(r24);	 Catch:{ Exception -> 0x0063 }
        r17 = 0;	 Catch:{ Exception -> 0x0063 }
        r15[r17] = r16;	 Catch:{ Exception -> 0x0063 }
        r12 = r11.type;	 Catch:{ Exception -> 0x0063 }
        r12 = java.lang.Integer.valueOf(r12);	 Catch:{ Exception -> 0x0063 }
        r16 = 1;	 Catch:{ Exception -> 0x0063 }
        r15[r16] = r12;	 Catch:{ Exception -> 0x0063 }
        r12 = r11.start;	 Catch:{ Exception -> 0x0063 }
        r12 = java.lang.Integer.valueOf(r12);	 Catch:{ Exception -> 0x0063 }
        r16 = 2;	 Catch:{ Exception -> 0x0063 }
        r15[r16] = r12;	 Catch:{ Exception -> 0x0063 }
        r12 = r11.end;	 Catch:{ Exception -> 0x0063 }
        r12 = java.lang.Integer.valueOf(r12);	 Catch:{ Exception -> 0x0063 }
        r16 = 3;	 Catch:{ Exception -> 0x0063 }
        r15[r16] = r12;	 Catch:{ Exception -> 0x0063 }
        r7 = java.lang.String.format(r14, r7, r15);	 Catch:{ Exception -> 0x0063 }
        r7 = r13.executeFast(r7);	 Catch:{ Exception -> 0x0063 }
        r7 = r7.stepThis();	 Catch:{ Exception -> 0x0063 }
        r7.dispose();	 Catch:{ Exception -> 0x0063 }
        goto L_0x01a2;	 Catch:{ Exception -> 0x0063 }
        r7 = r11.end;	 Catch:{ Exception -> 0x0063 }
        r12 = 1;	 Catch:{ Exception -> 0x0063 }
        r7 = r7 - r12;	 Catch:{ Exception -> 0x0063 }
        if (r5 < r7) goto L_0x01a9;	 Catch:{ Exception -> 0x0063 }
        r7 = r11.end;	 Catch:{ Exception -> 0x0063 }
        if (r7 == r4) goto L_0x01a2;
        r7 = r1.database;	 Catch:{ Exception -> 0x019c }
        r12 = java.util.Locale.US;	 Catch:{ Exception -> 0x019c }
        r13 = "UPDATE media_holes_v2 SET end = %d WHERE uid = %d AND type = %d AND start = %d AND end = %d";	 Catch:{ Exception -> 0x019c }
        r14 = 5;	 Catch:{ Exception -> 0x019c }
        r15 = new java.lang.Object[r14];	 Catch:{ Exception -> 0x019c }
        r14 = java.lang.Integer.valueOf(r26);	 Catch:{ Exception -> 0x019c }
        r16 = 0;	 Catch:{ Exception -> 0x019c }
        r15[r16] = r14;	 Catch:{ Exception -> 0x019c }
        r14 = java.lang.Long.valueOf(r24);	 Catch:{ Exception -> 0x019c }
        r16 = 1;	 Catch:{ Exception -> 0x019c }
        r15[r16] = r14;	 Catch:{ Exception -> 0x019c }
        r14 = r11.type;	 Catch:{ Exception -> 0x019c }
        r14 = java.lang.Integer.valueOf(r14);	 Catch:{ Exception -> 0x019c }
        r16 = 2;	 Catch:{ Exception -> 0x019c }
        r15[r16] = r14;	 Catch:{ Exception -> 0x019c }
        r14 = r11.start;	 Catch:{ Exception -> 0x019c }
        r14 = java.lang.Integer.valueOf(r14);	 Catch:{ Exception -> 0x019c }
        r16 = 3;	 Catch:{ Exception -> 0x019c }
        r15[r16] = r14;	 Catch:{ Exception -> 0x019c }
        r14 = r11.end;	 Catch:{ Exception -> 0x019c }
        r14 = java.lang.Integer.valueOf(r14);	 Catch:{ Exception -> 0x019c }
        r16 = 4;	 Catch:{ Exception -> 0x019c }
        r15[r16] = r14;	 Catch:{ Exception -> 0x019c }
        r12 = java.lang.String.format(r12, r13, r15);	 Catch:{ Exception -> 0x019c }
        r7 = r7.executeFast(r12);	 Catch:{ Exception -> 0x019c }
        r7 = r7.stepThis();	 Catch:{ Exception -> 0x019c }
        r7.dispose();	 Catch:{ Exception -> 0x019c }
        goto L_0x01a1;
    L_0x019c:
        r0 = move-exception;
        r7 = r0;
        org.telegram.messenger.FileLog.e(r7);	 Catch:{ Exception -> 0x0063 }
        r12 = 4;	 Catch:{ Exception -> 0x0063 }
        r13 = 3;	 Catch:{ Exception -> 0x0063 }
        r14 = 2;	 Catch:{ Exception -> 0x0063 }
        r16 = 0;	 Catch:{ Exception -> 0x0063 }
        goto L_0x027d;	 Catch:{ Exception -> 0x0063 }
        r7 = r11.start;	 Catch:{ Exception -> 0x0063 }
        r12 = 1;	 Catch:{ Exception -> 0x0063 }
        r7 = r7 + r12;	 Catch:{ Exception -> 0x0063 }
        if (r4 > r7) goto L_0x0200;	 Catch:{ Exception -> 0x0063 }
        r7 = r11.start;	 Catch:{ Exception -> 0x0063 }
        if (r7 == r5) goto L_0x01a2;
        r7 = r1.database;	 Catch:{ Exception -> 0x01fa }
        r12 = java.util.Locale.US;	 Catch:{ Exception -> 0x01fa }
        r13 = "UPDATE media_holes_v2 SET start = %d WHERE uid = %d AND type = %d AND start = %d AND end = %d";	 Catch:{ Exception -> 0x01fa }
        r14 = 5;	 Catch:{ Exception -> 0x01fa }
        r15 = new java.lang.Object[r14];	 Catch:{ Exception -> 0x01fa }
        r16 = java.lang.Integer.valueOf(r27);	 Catch:{ Exception -> 0x01fa }
        r17 = 0;	 Catch:{ Exception -> 0x01fa }
        r15[r17] = r16;	 Catch:{ Exception -> 0x01fa }
        r16 = java.lang.Long.valueOf(r24);	 Catch:{ Exception -> 0x01fa }
        r17 = 1;	 Catch:{ Exception -> 0x01fa }
        r15[r17] = r16;	 Catch:{ Exception -> 0x01fa }
        r14 = r11.type;	 Catch:{ Exception -> 0x01fa }
        r14 = java.lang.Integer.valueOf(r14);	 Catch:{ Exception -> 0x01fa }
        r16 = 2;	 Catch:{ Exception -> 0x01fa }
        r15[r16] = r14;	 Catch:{ Exception -> 0x01fa }
        r14 = r11.start;	 Catch:{ Exception -> 0x01fa }
        r14 = java.lang.Integer.valueOf(r14);	 Catch:{ Exception -> 0x01fa }
        r16 = 3;	 Catch:{ Exception -> 0x01fa }
        r15[r16] = r14;	 Catch:{ Exception -> 0x01fa }
        r14 = r11.end;	 Catch:{ Exception -> 0x01fa }
        r14 = java.lang.Integer.valueOf(r14);	 Catch:{ Exception -> 0x01fa }
        r16 = 4;	 Catch:{ Exception -> 0x01fa }
        r15[r16] = r14;	 Catch:{ Exception -> 0x01fa }
        r12 = java.lang.String.format(r12, r13, r15);	 Catch:{ Exception -> 0x01fa }
        r7 = r7.executeFast(r12);	 Catch:{ Exception -> 0x01fa }
        r7 = r7.stepThis();	 Catch:{ Exception -> 0x01fa }
        r7.dispose();	 Catch:{ Exception -> 0x01fa }
        goto L_0x01ff;
    L_0x01fa:
        r0 = move-exception;
        r7 = r0;
        org.telegram.messenger.FileLog.e(r7);	 Catch:{ Exception -> 0x0063 }
        goto L_0x01a2;	 Catch:{ Exception -> 0x0063 }
        r7 = r1.database;	 Catch:{ Exception -> 0x0063 }
        r12 = java.util.Locale.US;	 Catch:{ Exception -> 0x0063 }
        r13 = "DELETE FROM media_holes_v2 WHERE uid = %d AND type = %d AND start = %d AND end = %d";	 Catch:{ Exception -> 0x0063 }
        r14 = 4;	 Catch:{ Exception -> 0x0063 }
        r15 = new java.lang.Object[r14];	 Catch:{ Exception -> 0x0063 }
        r14 = java.lang.Long.valueOf(r24);	 Catch:{ Exception -> 0x0063 }
        r16 = 0;	 Catch:{ Exception -> 0x0063 }
        r15[r16] = r14;	 Catch:{ Exception -> 0x0063 }
        r14 = r11.type;	 Catch:{ Exception -> 0x0063 }
        r14 = java.lang.Integer.valueOf(r14);	 Catch:{ Exception -> 0x0063 }
        r17 = 1;	 Catch:{ Exception -> 0x0063 }
        r15[r17] = r14;	 Catch:{ Exception -> 0x0063 }
        r14 = r11.start;	 Catch:{ Exception -> 0x0063 }
        r14 = java.lang.Integer.valueOf(r14);	 Catch:{ Exception -> 0x0063 }
        r17 = 2;	 Catch:{ Exception -> 0x0063 }
        r15[r17] = r14;	 Catch:{ Exception -> 0x0063 }
        r14 = r11.end;	 Catch:{ Exception -> 0x0063 }
        r14 = java.lang.Integer.valueOf(r14);	 Catch:{ Exception -> 0x0063 }
        r17 = 3;	 Catch:{ Exception -> 0x0063 }
        r15[r17] = r14;	 Catch:{ Exception -> 0x0063 }
        r12 = java.lang.String.format(r12, r13, r15);	 Catch:{ Exception -> 0x0063 }
        r7 = r7.executeFast(r12);	 Catch:{ Exception -> 0x0063 }
        r7 = r7.stepThis();	 Catch:{ Exception -> 0x0063 }
        r7.dispose();	 Catch:{ Exception -> 0x0063 }
        r7 = r1.database;	 Catch:{ Exception -> 0x0063 }
        r12 = "REPLACE INTO media_holes_v2 VALUES(?, ?, ?, ?)";	 Catch:{ Exception -> 0x0063 }
        r7 = r7.executeFast(r12);	 Catch:{ Exception -> 0x0063 }
        r7.requery();	 Catch:{ Exception -> 0x0063 }
        r12 = 1;	 Catch:{ Exception -> 0x0063 }
        r7.bindLong(r12, r2);	 Catch:{ Exception -> 0x0063 }
        r12 = r11.type;	 Catch:{ Exception -> 0x0063 }
        r13 = 2;	 Catch:{ Exception -> 0x0063 }
        r7.bindInteger(r13, r12);	 Catch:{ Exception -> 0x0063 }
        r12 = r11.start;	 Catch:{ Exception -> 0x0063 }
        r13 = 3;	 Catch:{ Exception -> 0x0063 }
        r7.bindInteger(r13, r12);	 Catch:{ Exception -> 0x0063 }
        r12 = 4;	 Catch:{ Exception -> 0x0063 }
        r7.bindInteger(r12, r4);	 Catch:{ Exception -> 0x0063 }
        r7.step();	 Catch:{ Exception -> 0x0063 }
        r7.requery();	 Catch:{ Exception -> 0x0063 }
        r12 = 1;	 Catch:{ Exception -> 0x0063 }
        r7.bindLong(r12, r2);	 Catch:{ Exception -> 0x0063 }
        r13 = r11.type;	 Catch:{ Exception -> 0x0063 }
        r14 = 2;	 Catch:{ Exception -> 0x0063 }
        r7.bindInteger(r14, r13);	 Catch:{ Exception -> 0x0063 }
        r13 = 3;	 Catch:{ Exception -> 0x0063 }
        r7.bindInteger(r13, r5);	 Catch:{ Exception -> 0x0063 }
        r15 = r11.end;	 Catch:{ Exception -> 0x0063 }
        r12 = 4;	 Catch:{ Exception -> 0x0063 }
        r7.bindInteger(r12, r15);	 Catch:{ Exception -> 0x0063 }
        r7.step();	 Catch:{ Exception -> 0x0063 }
        r7.dispose();	 Catch:{ Exception -> 0x0063 }
        r10 = r10 + 1;
        r15 = r16;
        r7 = 1;
        goto L_0x00f7;
        goto L_0x0289;
        org.telegram.messenger.FileLog.e(r6);
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.MessagesStorage.closeHolesInMedia(long, int, int, int):void");
    }

    public static MessagesStorage getInstance(int num) {
        MessagesStorage localInstance = Instance[num];
        if (localInstance == null) {
            synchronized (MessagesStorage.class) {
                localInstance = Instance[num];
                if (localInstance == null) {
                    MessagesStorage[] messagesStorageArr = Instance;
                    MessagesStorage messagesStorage = new MessagesStorage(num);
                    localInstance = messagesStorage;
                    messagesStorageArr[num] = messagesStorage;
                }
            }
        }
        return localInstance;
    }

    private void ensureOpened() {
        try {
            this.openSync.await();
        } catch (Throwable th) {
        }
    }

    public int getLastDateValue() {
        ensureOpened();
        return this.lastDateValue;
    }

    public void setLastDateValue(int value) {
        ensureOpened();
        this.lastDateValue = value;
    }

    public int getLastPtsValue() {
        ensureOpened();
        return this.lastPtsValue;
    }

    public void setLastPtsValue(int value) {
        ensureOpened();
        this.lastPtsValue = value;
    }

    public int getLastQtsValue() {
        ensureOpened();
        return this.lastQtsValue;
    }

    public void setLastQtsValue(int value) {
        ensureOpened();
        this.lastQtsValue = value;
    }

    public int getLastSeqValue() {
        ensureOpened();
        return this.lastSeqValue;
    }

    public void setLastSeqValue(int value) {
        ensureOpened();
        this.lastSeqValue = value;
    }

    public int getLastSecretVersion() {
        ensureOpened();
        return this.lastSecretVersion;
    }

    public void setLastSecretVersion(int value) {
        ensureOpened();
        this.lastSecretVersion = value;
    }

    public byte[] getSecretPBytes() {
        ensureOpened();
        return this.secretPBytes;
    }

    public void setSecretPBytes(byte[] value) {
        ensureOpened();
        this.secretPBytes = value;
    }

    public int getSecretG() {
        ensureOpened();
        return this.secretG;
    }

    public void setSecretG(int value) {
        ensureOpened();
        this.secretG = value;
    }

    public MessagesStorage(int instance) {
        this.currentAccount = instance;
        this.storageQueue.postRunnable(new Runnable() {
            public void run() {
                MessagesStorage.this.openDatabase(true);
            }
        });
    }

    public SQLiteDatabase getDatabase() {
        return this.database;
    }

    public DispatchQueue getStorageQueue() {
        return this.storageQueue;
    }

    public long getDatabaseSize() {
        long size = 0;
        if (this.cacheFile != null) {
            size = 0 + this.cacheFile.length();
        }
        if (this.shmCacheFile != null) {
            return size + this.shmCacheFile.length();
        }
        return size;
    }

    public void openDatabase(boolean first) {
        File filesDir = ApplicationLoader.getFilesDirFixed();
        if (this.currentAccount != 0) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("account");
            stringBuilder.append(this.currentAccount);
            stringBuilder.append("/");
            filesDir = new File(filesDir, stringBuilder.toString());
            filesDir.mkdirs();
        }
        this.cacheFile = new File(filesDir, "cache4.db");
        this.walCacheFile = new File(filesDir, "cache4.db-wal");
        this.shmCacheFile = new File(filesDir, "cache4.db-shm");
        boolean createTable = false;
        if (!this.cacheFile.exists()) {
            createTable = true;
        }
        try {
            this.database = new SQLiteDatabase(this.cacheFile.getPath());
            this.database.executeFast("PRAGMA secure_delete = ON").stepThis().dispose();
            this.database.executeFast("PRAGMA temp_store = 1").stepThis().dispose();
            this.database.executeFast("PRAGMA journal_mode = WAL").stepThis().dispose();
            if (createTable) {
                if (BuildVars.LOGS_ENABLED) {
                    FileLog.d("create new database");
                }
                this.database.executeFast("CREATE TABLE messages_holes(uid INTEGER, start INTEGER, end INTEGER, PRIMARY KEY(uid, start));").stepThis().dispose();
                this.database.executeFast("CREATE INDEX IF NOT EXISTS uid_end_messages_holes ON messages_holes(uid, end);").stepThis().dispose();
                this.database.executeFast("CREATE TABLE media_holes_v2(uid INTEGER, type INTEGER, start INTEGER, end INTEGER, PRIMARY KEY(uid, type, start));").stepThis().dispose();
                this.database.executeFast("CREATE INDEX IF NOT EXISTS uid_end_media_holes_v2 ON media_holes_v2(uid, type, end);").stepThis().dispose();
                this.database.executeFast("CREATE TABLE messages(mid INTEGER PRIMARY KEY, uid INTEGER, read_state INTEGER, send_state INTEGER, date INTEGER, data BLOB, out INTEGER, ttl INTEGER, media INTEGER, replydata BLOB, imp INTEGER, mention INTEGER)").stepThis().dispose();
                this.database.executeFast("CREATE INDEX IF NOT EXISTS uid_mid_idx_messages ON messages(uid, mid);").stepThis().dispose();
                this.database.executeFast("CREATE INDEX IF NOT EXISTS uid_date_mid_idx_messages ON messages(uid, date, mid);").stepThis().dispose();
                this.database.executeFast("CREATE INDEX IF NOT EXISTS mid_out_idx_messages ON messages(mid, out);").stepThis().dispose();
                this.database.executeFast("CREATE INDEX IF NOT EXISTS task_idx_messages ON messages(uid, out, read_state, ttl, date, send_state);").stepThis().dispose();
                this.database.executeFast("CREATE INDEX IF NOT EXISTS send_state_idx_messages ON messages(mid, send_state, date) WHERE mid < 0 AND send_state = 1;").stepThis().dispose();
                this.database.executeFast("CREATE INDEX IF NOT EXISTS uid_mention_idx_messages ON messages(uid, mention, read_state);").stepThis().dispose();
                this.database.executeFast("CREATE TABLE download_queue(uid INTEGER, type INTEGER, date INTEGER, data BLOB, PRIMARY KEY (uid, type));").stepThis().dispose();
                this.database.executeFast("CREATE INDEX IF NOT EXISTS type_date_idx_download_queue ON download_queue(type, date);").stepThis().dispose();
                this.database.executeFast("CREATE TABLE user_contacts_v7(key TEXT PRIMARY KEY, uid INTEGER, fname TEXT, sname TEXT, imported INTEGER)").stepThis().dispose();
                this.database.executeFast("CREATE TABLE user_phones_v7(key TEXT, phone TEXT, sphone TEXT, deleted INTEGER, PRIMARY KEY (key, phone))").stepThis().dispose();
                this.database.executeFast("CREATE INDEX IF NOT EXISTS sphone_deleted_idx_user_phones ON user_phones_v7(sphone, deleted);").stepThis().dispose();
                this.database.executeFast("CREATE TABLE dialogs(did INTEGER PRIMARY KEY, date INTEGER, unread_count INTEGER, last_mid INTEGER, inbox_max INTEGER, outbox_max INTEGER, last_mid_i INTEGER, unread_count_i INTEGER, pts INTEGER, date_i INTEGER, pinned INTEGER)").stepThis().dispose();
                this.database.executeFast("CREATE INDEX IF NOT EXISTS date_idx_dialogs ON dialogs(date);").stepThis().dispose();
                this.database.executeFast("CREATE INDEX IF NOT EXISTS last_mid_idx_dialogs ON dialogs(last_mid);").stepThis().dispose();
                this.database.executeFast("CREATE INDEX IF NOT EXISTS unread_count_idx_dialogs ON dialogs(unread_count);").stepThis().dispose();
                this.database.executeFast("CREATE INDEX IF NOT EXISTS last_mid_i_idx_dialogs ON dialogs(last_mid_i);").stepThis().dispose();
                this.database.executeFast("CREATE INDEX IF NOT EXISTS unread_count_i_idx_dialogs ON dialogs(unread_count_i);").stepThis().dispose();
                this.database.executeFast("CREATE TABLE randoms(random_id INTEGER, mid INTEGER, PRIMARY KEY (random_id, mid))").stepThis().dispose();
                this.database.executeFast("CREATE INDEX IF NOT EXISTS mid_idx_randoms ON randoms(mid);").stepThis().dispose();
                this.database.executeFast("CREATE TABLE enc_tasks_v2(mid INTEGER PRIMARY KEY, date INTEGER)").stepThis().dispose();
                this.database.executeFast("CREATE INDEX IF NOT EXISTS date_idx_enc_tasks_v2 ON enc_tasks_v2(date);").stepThis().dispose();
                this.database.executeFast("CREATE TABLE messages_seq(mid INTEGER PRIMARY KEY, seq_in INTEGER, seq_out INTEGER);").stepThis().dispose();
                this.database.executeFast("CREATE INDEX IF NOT EXISTS seq_idx_messages_seq ON messages_seq(seq_in, seq_out);").stepThis().dispose();
                this.database.executeFast("CREATE TABLE params(id INTEGER PRIMARY KEY, seq INTEGER, pts INTEGER, date INTEGER, qts INTEGER, lsv INTEGER, sg INTEGER, pbytes BLOB)").stepThis().dispose();
                this.database.executeFast("INSERT INTO params VALUES(1, 0, 0, 0, 0, 0, 0, NULL)").stepThis().dispose();
                this.database.executeFast("CREATE TABLE media_v2(mid INTEGER PRIMARY KEY, uid INTEGER, date INTEGER, type INTEGER, data BLOB)").stepThis().dispose();
                this.database.executeFast("CREATE INDEX IF NOT EXISTS uid_mid_type_date_idx_media ON media_v2(uid, mid, type, date);").stepThis().dispose();
                this.database.executeFast("CREATE TABLE bot_keyboard(uid INTEGER PRIMARY KEY, mid INTEGER, info BLOB)").stepThis().dispose();
                this.database.executeFast("CREATE INDEX IF NOT EXISTS bot_keyboard_idx_mid ON bot_keyboard(mid);").stepThis().dispose();
                this.database.executeFast("CREATE TABLE chat_settings_v2(uid INTEGER PRIMARY KEY, info BLOB, pinned INTEGER)").stepThis().dispose();
                this.database.executeFast("CREATE INDEX IF NOT EXISTS chat_settings_pinned_idx ON chat_settings_v2(uid, pinned) WHERE pinned != 0;").stepThis().dispose();
                this.database.executeFast("CREATE TABLE chat_pinned(uid INTEGER PRIMARY KEY, pinned INTEGER, data BLOB)").stepThis().dispose();
                this.database.executeFast("CREATE INDEX IF NOT EXISTS chat_pinned_mid_idx ON chat_pinned(uid, pinned) WHERE pinned != 0;").stepThis().dispose();
                this.database.executeFast("CREATE TABLE chat_hints(did INTEGER, type INTEGER, rating REAL, date INTEGER, PRIMARY KEY(did, type))").stepThis().dispose();
                this.database.executeFast("CREATE INDEX IF NOT EXISTS chat_hints_rating_idx ON chat_hints(rating);").stepThis().dispose();
                this.database.executeFast("CREATE TABLE botcache(id TEXT PRIMARY KEY, date INTEGER, data BLOB)").stepThis().dispose();
                this.database.executeFast("CREATE INDEX IF NOT EXISTS botcache_date_idx ON botcache(date);").stepThis().dispose();
                this.database.executeFast("CREATE TABLE users_data(uid INTEGER PRIMARY KEY, about TEXT)").stepThis().dispose();
                this.database.executeFast("CREATE TABLE users(uid INTEGER PRIMARY KEY, name TEXT, status INTEGER, data BLOB)").stepThis().dispose();
                this.database.executeFast("CREATE TABLE chats(uid INTEGER PRIMARY KEY, name TEXT, data BLOB)").stepThis().dispose();
                this.database.executeFast("CREATE TABLE enc_chats(uid INTEGER PRIMARY KEY, user INTEGER, name TEXT, data BLOB, g BLOB, authkey BLOB, ttl INTEGER, layer INTEGER, seq_in INTEGER, seq_out INTEGER, use_count INTEGER, exchange_id INTEGER, key_date INTEGER, fprint INTEGER, fauthkey BLOB, khash BLOB, in_seq_no INTEGER, admin_id INTEGER, mtproto_seq INTEGER)").stepThis().dispose();
                this.database.executeFast("CREATE TABLE channel_users_v2(did INTEGER, uid INTEGER, date INTEGER, data BLOB, PRIMARY KEY(did, uid))").stepThis().dispose();
                this.database.executeFast("CREATE TABLE channel_admins(did INTEGER, uid INTEGER, PRIMARY KEY(did, uid))").stepThis().dispose();
                this.database.executeFast("CREATE TABLE contacts(uid INTEGER PRIMARY KEY, mutual INTEGER)").stepThis().dispose();
                this.database.executeFast("CREATE TABLE wallpapers(uid INTEGER PRIMARY KEY, data BLOB)").stepThis().dispose();
                this.database.executeFast("CREATE TABLE user_photos(uid INTEGER, id INTEGER, data BLOB, PRIMARY KEY (uid, id))").stepThis().dispose();
                this.database.executeFast("CREATE TABLE blocked_users(uid INTEGER PRIMARY KEY)").stepThis().dispose();
                this.database.executeFast("CREATE TABLE dialog_settings(did INTEGER PRIMARY KEY, flags INTEGER);").stepThis().dispose();
                this.database.executeFast("CREATE TABLE web_recent_v3(id TEXT, type INTEGER, image_url TEXT, thumb_url TEXT, local_url TEXT, width INTEGER, height INTEGER, size INTEGER, date INTEGER, document BLOB, PRIMARY KEY (id, type));").stepThis().dispose();
                this.database.executeFast("CREATE TABLE stickers_v2(id INTEGER PRIMARY KEY, data BLOB, date INTEGER, hash TEXT);").stepThis().dispose();
                this.database.executeFast("CREATE TABLE stickers_featured(id INTEGER PRIMARY KEY, data BLOB, unread BLOB, date INTEGER, hash TEXT);").stepThis().dispose();
                this.database.executeFast("CREATE TABLE hashtag_recent_v2(id TEXT PRIMARY KEY, date INTEGER);").stepThis().dispose();
                this.database.executeFast("CREATE TABLE webpage_pending(id INTEGER, mid INTEGER, PRIMARY KEY (id, mid));").stepThis().dispose();
                this.database.executeFast("CREATE TABLE sent_files_v2(uid TEXT, type INTEGER, data BLOB, PRIMARY KEY (uid, type))").stepThis().dispose();
                this.database.executeFast("CREATE TABLE search_recent(did INTEGER PRIMARY KEY, date INTEGER);").stepThis().dispose();
                this.database.executeFast("CREATE TABLE media_counts_v2(uid INTEGER, type INTEGER, count INTEGER, PRIMARY KEY(uid, type))").stepThis().dispose();
                this.database.executeFast("CREATE TABLE keyvalue(id TEXT PRIMARY KEY, value TEXT)").stepThis().dispose();
                this.database.executeFast("CREATE TABLE bot_info(uid INTEGER PRIMARY KEY, info BLOB)").stepThis().dispose();
                this.database.executeFast("CREATE TABLE pending_tasks(id INTEGER PRIMARY KEY, data BLOB);").stepThis().dispose();
                this.database.executeFast("CREATE TABLE requested_holes(uid INTEGER, seq_out_start INTEGER, seq_out_end INTEGER, PRIMARY KEY (uid, seq_out_start, seq_out_end));").stepThis().dispose();
                this.database.executeFast("CREATE TABLE sharing_locations(uid INTEGER PRIMARY KEY, mid INTEGER, date INTEGER, period INTEGER, message BLOB);").stepThis().dispose();
                this.database.executeFast("PRAGMA user_version = 47").stepThis().dispose();
            } else {
                int version = this.database.executeInt("PRAGMA user_version", new Object[0]).intValue();
                if (BuildVars.LOGS_ENABLED) {
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("current db version = ");
                    stringBuilder2.append(version);
                    FileLog.d(stringBuilder2.toString());
                }
                if (version == 0) {
                    throw new Exception("malformed");
                }
                try {
                    SQLiteCursor cursor = this.database.queryFinalized("SELECT seq, pts, date, qts, lsv, sg, pbytes FROM params WHERE id = 1", new Object[0]);
                    if (cursor.next()) {
                        this.lastSeqValue = cursor.intValue(0);
                        this.lastPtsValue = cursor.intValue(1);
                        this.lastDateValue = cursor.intValue(2);
                        this.lastQtsValue = cursor.intValue(3);
                        this.lastSecretVersion = cursor.intValue(4);
                        this.secretG = cursor.intValue(5);
                        if (cursor.isNull(6)) {
                            this.secretPBytes = null;
                        } else {
                            this.secretPBytes = cursor.byteArrayValue(6);
                            if (this.secretPBytes != null && this.secretPBytes.length == 1) {
                                this.secretPBytes = null;
                            }
                        }
                    }
                    cursor.dispose();
                } catch (Throwable e) {
                    FileLog.e(e);
                    try {
                        this.database.executeFast("CREATE TABLE IF NOT EXISTS params(id INTEGER PRIMARY KEY, seq INTEGER, pts INTEGER, date INTEGER, qts INTEGER, lsv INTEGER, sg INTEGER, pbytes BLOB)").stepThis().dispose();
                        this.database.executeFast("INSERT INTO params VALUES(1, 0, 0, 0, 0, 0, 0, NULL)").stepThis().dispose();
                    } catch (Throwable e2) {
                        FileLog.e(e2);
                    }
                }
                if (version < 47) {
                    updateDbToLastVersion(version);
                }
            }
        } catch (Throwable e3) {
            FileLog.e(e3);
            if (first && e3.getMessage().contains("malformed")) {
                cleanupInternal();
                UserConfig.getInstance(this.currentAccount).dialogsLoadOffsetId = 0;
                UserConfig.getInstance(this.currentAccount).totalDialogsLoadCount = 0;
                UserConfig.getInstance(this.currentAccount).dialogsLoadOffsetDate = 0;
                UserConfig.getInstance(this.currentAccount).dialogsLoadOffsetUserId = 0;
                UserConfig.getInstance(this.currentAccount).dialogsLoadOffsetChatId = 0;
                UserConfig.getInstance(this.currentAccount).dialogsLoadOffsetChannelId = 0;
                UserConfig.getInstance(this.currentAccount).dialogsLoadOffsetAccess = 0;
                UserConfig.getInstance(this.currentAccount).saveConfig(false);
                openDatabase(false);
            }
        }
        loadUnreadMessages();
        loadPendingTasks();
        try {
            this.openSync.countDown();
        } catch (Throwable th) {
        }
    }

    private void updateDbToLastVersion(final int currentVersion) {
        this.storageQueue.postRunnable(new Runnable() {
            public void run() {
                try {
                    SQLiteCursor cursor;
                    SQLitePreparedStatement state;
                    int date;
                    NativeByteBuffer data;
                    int version = currentVersion;
                    if (version < 4) {
                        MessagesStorage.this.database.executeFast("CREATE TABLE IF NOT EXISTS user_photos(uid INTEGER, id INTEGER, data BLOB, PRIMARY KEY (uid, id))").stepThis().dispose();
                        MessagesStorage.this.database.executeFast("DROP INDEX IF EXISTS read_state_out_idx_messages;").stepThis().dispose();
                        MessagesStorage.this.database.executeFast("DROP INDEX IF EXISTS ttl_idx_messages;").stepThis().dispose();
                        MessagesStorage.this.database.executeFast("DROP INDEX IF EXISTS date_idx_messages;").stepThis().dispose();
                        MessagesStorage.this.database.executeFast("CREATE INDEX IF NOT EXISTS mid_out_idx_messages ON messages(mid, out);").stepThis().dispose();
                        MessagesStorage.this.database.executeFast("CREATE INDEX IF NOT EXISTS task_idx_messages ON messages(uid, out, read_state, ttl, date, send_state);").stepThis().dispose();
                        MessagesStorage.this.database.executeFast("CREATE INDEX IF NOT EXISTS uid_date_mid_idx_messages ON messages(uid, date, mid);").stepThis().dispose();
                        MessagesStorage.this.database.executeFast("CREATE TABLE IF NOT EXISTS user_contacts_v6(uid INTEGER PRIMARY KEY, fname TEXT, sname TEXT)").stepThis().dispose();
                        MessagesStorage.this.database.executeFast("CREATE TABLE IF NOT EXISTS user_phones_v6(uid INTEGER, phone TEXT, sphone TEXT, deleted INTEGER, PRIMARY KEY (uid, phone))").stepThis().dispose();
                        MessagesStorage.this.database.executeFast("CREATE INDEX IF NOT EXISTS sphone_deleted_idx_user_phones ON user_phones_v6(sphone, deleted);").stepThis().dispose();
                        MessagesStorage.this.database.executeFast("CREATE INDEX IF NOT EXISTS mid_idx_randoms ON randoms(mid);").stepThis().dispose();
                        MessagesStorage.this.database.executeFast("CREATE TABLE IF NOT EXISTS sent_files_v2(uid TEXT, type INTEGER, data BLOB, PRIMARY KEY (uid, type))").stepThis().dispose();
                        MessagesStorage.this.database.executeFast("CREATE TABLE IF NOT EXISTS blocked_users(uid INTEGER PRIMARY KEY)").stepThis().dispose();
                        MessagesStorage.this.database.executeFast("CREATE TABLE IF NOT EXISTS download_queue(uid INTEGER, type INTEGER, date INTEGER, data BLOB, PRIMARY KEY (uid, type));").stepThis().dispose();
                        MessagesStorage.this.database.executeFast("CREATE INDEX IF NOT EXISTS type_date_idx_download_queue ON download_queue(type, date);").stepThis().dispose();
                        MessagesStorage.this.database.executeFast("CREATE TABLE IF NOT EXISTS dialog_settings(did INTEGER PRIMARY KEY, flags INTEGER);").stepThis().dispose();
                        MessagesStorage.this.database.executeFast("CREATE INDEX IF NOT EXISTS send_state_idx_messages ON messages(mid, send_state, date) WHERE mid < 0 AND send_state = 1;").stepThis().dispose();
                        MessagesStorage.this.database.executeFast("CREATE INDEX IF NOT EXISTS unread_count_idx_dialogs ON dialogs(unread_count);").stepThis().dispose();
                        MessagesStorage.this.database.executeFast("UPDATE messages SET send_state = 2 WHERE mid < 0 AND send_state = 1").stepThis().dispose();
                        MessagesStorage.this.fixNotificationSettings();
                        MessagesStorage.this.database.executeFast("PRAGMA user_version = 4").stepThis().dispose();
                        version = 4;
                    }
                    if (version == 4) {
                        MessagesStorage.this.database.executeFast("CREATE TABLE IF NOT EXISTS enc_tasks_v2(mid INTEGER PRIMARY KEY, date INTEGER)").stepThis().dispose();
                        MessagesStorage.this.database.executeFast("CREATE INDEX IF NOT EXISTS date_idx_enc_tasks_v2 ON enc_tasks_v2(date);").stepThis().dispose();
                        MessagesStorage.this.database.beginTransaction();
                        cursor = MessagesStorage.this.database.queryFinalized("SELECT date, data FROM enc_tasks WHERE 1", new Object[0]);
                        state = MessagesStorage.this.database.executeFast("REPLACE INTO enc_tasks_v2 VALUES(?, ?)");
                        if (cursor.next()) {
                            date = cursor.intValue(0);
                            data = cursor.byteBufferValue(1);
                            if (data != null) {
                                int length = data.limit();
                                for (int a = 0; a < length / 4; a++) {
                                    state.requery();
                                    state.bindInteger(1, data.readInt32(false));
                                    state.bindInteger(2, date);
                                    state.step();
                                }
                                data.reuse();
                            }
                        }
                        state.dispose();
                        cursor.dispose();
                        MessagesStorage.this.database.commitTransaction();
                        MessagesStorage.this.database.executeFast("DROP INDEX IF EXISTS date_idx_enc_tasks;").stepThis().dispose();
                        MessagesStorage.this.database.executeFast("DROP TABLE IF EXISTS enc_tasks;").stepThis().dispose();
                        MessagesStorage.this.database.executeFast("ALTER TABLE messages ADD COLUMN media INTEGER default 0").stepThis().dispose();
                        MessagesStorage.this.database.executeFast("PRAGMA user_version = 6").stepThis().dispose();
                        version = 6;
                    }
                    if (version == 6) {
                        MessagesStorage.this.database.executeFast("CREATE TABLE IF NOT EXISTS messages_seq(mid INTEGER PRIMARY KEY, seq_in INTEGER, seq_out INTEGER);").stepThis().dispose();
                        MessagesStorage.this.database.executeFast("CREATE INDEX IF NOT EXISTS seq_idx_messages_seq ON messages_seq(seq_in, seq_out);").stepThis().dispose();
                        MessagesStorage.this.database.executeFast("ALTER TABLE enc_chats ADD COLUMN layer INTEGER default 0").stepThis().dispose();
                        MessagesStorage.this.database.executeFast("ALTER TABLE enc_chats ADD COLUMN seq_in INTEGER default 0").stepThis().dispose();
                        MessagesStorage.this.database.executeFast("ALTER TABLE enc_chats ADD COLUMN seq_out INTEGER default 0").stepThis().dispose();
                        MessagesStorage.this.database.executeFast("PRAGMA user_version = 7").stepThis().dispose();
                        version = 7;
                    }
                    if (version == 7 || version == 8 || version == 9) {
                        MessagesStorage.this.database.executeFast("ALTER TABLE enc_chats ADD COLUMN use_count INTEGER default 0").stepThis().dispose();
                        MessagesStorage.this.database.executeFast("ALTER TABLE enc_chats ADD COLUMN exchange_id INTEGER default 0").stepThis().dispose();
                        MessagesStorage.this.database.executeFast("ALTER TABLE enc_chats ADD COLUMN key_date INTEGER default 0").stepThis().dispose();
                        MessagesStorage.this.database.executeFast("ALTER TABLE enc_chats ADD COLUMN fprint INTEGER default 0").stepThis().dispose();
                        MessagesStorage.this.database.executeFast("ALTER TABLE enc_chats ADD COLUMN fauthkey BLOB default NULL").stepThis().dispose();
                        MessagesStorage.this.database.executeFast("ALTER TABLE enc_chats ADD COLUMN khash BLOB default NULL").stepThis().dispose();
                        MessagesStorage.this.database.executeFast("PRAGMA user_version = 10").stepThis().dispose();
                        version = 10;
                    }
                    if (version == 10) {
                        MessagesStorage.this.database.executeFast("CREATE TABLE IF NOT EXISTS web_recent_v3(id TEXT, type INTEGER, image_url TEXT, thumb_url TEXT, local_url TEXT, width INTEGER, height INTEGER, size INTEGER, date INTEGER, PRIMARY KEY (id, type));").stepThis().dispose();
                        MessagesStorage.this.database.executeFast("PRAGMA user_version = 11").stepThis().dispose();
                        version = 11;
                    }
                    if (version == 11 || version == 12) {
                        MessagesStorage.this.database.executeFast("DROP INDEX IF EXISTS uid_mid_idx_media;").stepThis().dispose();
                        MessagesStorage.this.database.executeFast("DROP INDEX IF EXISTS mid_idx_media;").stepThis().dispose();
                        MessagesStorage.this.database.executeFast("DROP INDEX IF EXISTS uid_date_mid_idx_media;").stepThis().dispose();
                        MessagesStorage.this.database.executeFast("DROP TABLE IF EXISTS media;").stepThis().dispose();
                        MessagesStorage.this.database.executeFast("DROP TABLE IF EXISTS media_counts;").stepThis().dispose();
                        MessagesStorage.this.database.executeFast("CREATE TABLE IF NOT EXISTS media_v2(mid INTEGER PRIMARY KEY, uid INTEGER, date INTEGER, type INTEGER, data BLOB)").stepThis().dispose();
                        MessagesStorage.this.database.executeFast("CREATE TABLE IF NOT EXISTS media_counts_v2(uid INTEGER, type INTEGER, count INTEGER, PRIMARY KEY(uid, type))").stepThis().dispose();
                        MessagesStorage.this.database.executeFast("CREATE INDEX IF NOT EXISTS uid_mid_type_date_idx_media ON media_v2(uid, mid, type, date);").stepThis().dispose();
                        MessagesStorage.this.database.executeFast("CREATE TABLE IF NOT EXISTS keyvalue(id TEXT PRIMARY KEY, value TEXT)").stepThis().dispose();
                        MessagesStorage.this.database.executeFast("PRAGMA user_version = 13").stepThis().dispose();
                        version = 13;
                    }
                    if (version == 13) {
                        MessagesStorage.this.database.executeFast("ALTER TABLE messages ADD COLUMN replydata BLOB default NULL").stepThis().dispose();
                        MessagesStorage.this.database.executeFast("PRAGMA user_version = 14").stepThis().dispose();
                        version = 14;
                    }
                    if (version == 14) {
                        MessagesStorage.this.database.executeFast("CREATE TABLE IF NOT EXISTS hashtag_recent_v2(id TEXT PRIMARY KEY, date INTEGER);").stepThis().dispose();
                        MessagesStorage.this.database.executeFast("PRAGMA user_version = 15").stepThis().dispose();
                        version = 15;
                    }
                    if (version == 15) {
                        MessagesStorage.this.database.executeFast("CREATE TABLE IF NOT EXISTS webpage_pending(id INTEGER, mid INTEGER, PRIMARY KEY (id, mid));").stepThis().dispose();
                        MessagesStorage.this.database.executeFast("PRAGMA user_version = 16").stepThis().dispose();
                        version = 16;
                    }
                    if (version == 16) {
                        MessagesStorage.this.database.executeFast("ALTER TABLE dialogs ADD COLUMN inbox_max INTEGER default 0").stepThis().dispose();
                        MessagesStorage.this.database.executeFast("ALTER TABLE dialogs ADD COLUMN outbox_max INTEGER default 0").stepThis().dispose();
                        MessagesStorage.this.database.executeFast("PRAGMA user_version = 17").stepThis().dispose();
                        version = 17;
                    }
                    if (version == 17) {
                        MessagesStorage.this.database.executeFast("CREATE TABLE bot_info(uid INTEGER PRIMARY KEY, info BLOB)").stepThis().dispose();
                        MessagesStorage.this.database.executeFast("PRAGMA user_version = 18").stepThis().dispose();
                        version = 18;
                    }
                    if (version == 18) {
                        MessagesStorage.this.database.executeFast("DROP TABLE IF EXISTS stickers;").stepThis().dispose();
                        MessagesStorage.this.database.executeFast("CREATE TABLE IF NOT EXISTS stickers_v2(id INTEGER PRIMARY KEY, data BLOB, date INTEGER, hash TEXT);").stepThis().dispose();
                        MessagesStorage.this.database.executeFast("PRAGMA user_version = 19").stepThis().dispose();
                        version = 19;
                    }
                    if (version == 19) {
                        MessagesStorage.this.database.executeFast("CREATE TABLE IF NOT EXISTS bot_keyboard(uid INTEGER PRIMARY KEY, mid INTEGER, info BLOB)").stepThis().dispose();
                        MessagesStorage.this.database.executeFast("CREATE INDEX IF NOT EXISTS bot_keyboard_idx_mid ON bot_keyboard(mid);").stepThis().dispose();
                        MessagesStorage.this.database.executeFast("PRAGMA user_version = 20").stepThis().dispose();
                        version = 20;
                    }
                    if (version == 20) {
                        MessagesStorage.this.database.executeFast("CREATE TABLE search_recent(did INTEGER PRIMARY KEY, date INTEGER);").stepThis().dispose();
                        MessagesStorage.this.database.executeFast("PRAGMA user_version = 21").stepThis().dispose();
                        version = 21;
                    }
                    if (version == 21) {
                        MessagesStorage.this.database.executeFast("CREATE TABLE IF NOT EXISTS chat_settings_v2(uid INTEGER PRIMARY KEY, info BLOB)").stepThis().dispose();
                        cursor = MessagesStorage.this.database.queryFinalized("SELECT uid, participants FROM chat_settings WHERE uid < 0", new Object[0]);
                        state = MessagesStorage.this.database.executeFast("REPLACE INTO chat_settings_v2 VALUES(?, ?)");
                        while (cursor.next()) {
                            date = cursor.intValue(0);
                            data = cursor.byteBufferValue(1);
                            if (data != null) {
                                ChatParticipants participants = ChatParticipants.TLdeserialize(data, data.readInt32(false), false);
                                data.reuse();
                                if (participants != null) {
                                    TL_chatFull chatFull = new TL_chatFull();
                                    chatFull.id = date;
                                    chatFull.chat_photo = new TL_photoEmpty();
                                    chatFull.notify_settings = new TL_peerNotifySettingsEmpty();
                                    chatFull.exported_invite = new TL_chatInviteEmpty();
                                    chatFull.participants = participants;
                                    NativeByteBuffer data2 = new NativeByteBuffer(chatFull.getObjectSize());
                                    chatFull.serializeToStream(data2);
                                    state.requery();
                                    state.bindInteger(1, date);
                                    state.bindByteBuffer(2, data2);
                                    state.step();
                                    data2.reuse();
                                }
                            }
                        }
                        state.dispose();
                        cursor.dispose();
                        MessagesStorage.this.database.executeFast("DROP TABLE IF EXISTS chat_settings;").stepThis().dispose();
                        MessagesStorage.this.database.executeFast("ALTER TABLE dialogs ADD COLUMN last_mid_i INTEGER default 0").stepThis().dispose();
                        MessagesStorage.this.database.executeFast("ALTER TABLE dialogs ADD COLUMN unread_count_i INTEGER default 0").stepThis().dispose();
                        MessagesStorage.this.database.executeFast("ALTER TABLE dialogs ADD COLUMN pts INTEGER default 0").stepThis().dispose();
                        MessagesStorage.this.database.executeFast("ALTER TABLE dialogs ADD COLUMN date_i INTEGER default 0").stepThis().dispose();
                        MessagesStorage.this.database.executeFast("CREATE INDEX IF NOT EXISTS last_mid_i_idx_dialogs ON dialogs(last_mid_i);").stepThis().dispose();
                        MessagesStorage.this.database.executeFast("CREATE INDEX IF NOT EXISTS unread_count_i_idx_dialogs ON dialogs(unread_count_i);").stepThis().dispose();
                        MessagesStorage.this.database.executeFast("ALTER TABLE messages ADD COLUMN imp INTEGER default 0").stepThis().dispose();
                        MessagesStorage.this.database.executeFast("CREATE TABLE IF NOT EXISTS messages_holes(uid INTEGER, start INTEGER, end INTEGER, PRIMARY KEY(uid, start));").stepThis().dispose();
                        MessagesStorage.this.database.executeFast("CREATE INDEX IF NOT EXISTS uid_end_messages_holes ON messages_holes(uid, end);").stepThis().dispose();
                        MessagesStorage.this.database.executeFast("PRAGMA user_version = 22").stepThis().dispose();
                        version = 22;
                    }
                    if (version == 22) {
                        MessagesStorage.this.database.executeFast("CREATE TABLE IF NOT EXISTS media_holes_v2(uid INTEGER, type INTEGER, start INTEGER, end INTEGER, PRIMARY KEY(uid, type, start));").stepThis().dispose();
                        MessagesStorage.this.database.executeFast("CREATE INDEX IF NOT EXISTS uid_end_media_holes_v2 ON media_holes_v2(uid, type, end);").stepThis().dispose();
                        MessagesStorage.this.database.executeFast("PRAGMA user_version = 23").stepThis().dispose();
                        version = 23;
                    }
                    if (version == 23 || version == 24) {
                        MessagesStorage.this.database.executeFast("DELETE FROM media_holes_v2 WHERE uid != 0 AND type >= 0 AND start IN (0, 1)").stepThis().dispose();
                        MessagesStorage.this.database.executeFast("PRAGMA user_version = 25").stepThis().dispose();
                        version = 25;
                    }
                    if (version == 25 || version == 26) {
                        MessagesStorage.this.database.executeFast("CREATE TABLE IF NOT EXISTS channel_users_v2(did INTEGER, uid INTEGER, date INTEGER, data BLOB, PRIMARY KEY(did, uid))").stepThis().dispose();
                        MessagesStorage.this.database.executeFast("PRAGMA user_version = 27").stepThis().dispose();
                        version = 27;
                    }
                    if (version == 27) {
                        MessagesStorage.this.database.executeFast("ALTER TABLE web_recent_v3 ADD COLUMN document BLOB default NULL").stepThis().dispose();
                        MessagesStorage.this.database.executeFast("PRAGMA user_version = 28").stepThis().dispose();
                        version = 28;
                    }
                    if (version == 28 || version == 29) {
                        MessagesStorage.this.database.executeFast("DELETE FROM sent_files_v2 WHERE 1").stepThis().dispose();
                        MessagesStorage.this.database.executeFast("DELETE FROM download_queue WHERE 1").stepThis().dispose();
                        MessagesStorage.this.database.executeFast("PRAGMA user_version = 30").stepThis().dispose();
                        version = 30;
                    }
                    if (version == 30) {
                        MessagesStorage.this.database.executeFast("ALTER TABLE chat_settings_v2 ADD COLUMN pinned INTEGER default 0").stepThis().dispose();
                        MessagesStorage.this.database.executeFast("CREATE INDEX IF NOT EXISTS chat_settings_pinned_idx ON chat_settings_v2(uid, pinned) WHERE pinned != 0;").stepThis().dispose();
                        MessagesStorage.this.database.executeFast("CREATE TABLE IF NOT EXISTS chat_pinned(uid INTEGER PRIMARY KEY, pinned INTEGER, data BLOB)").stepThis().dispose();
                        MessagesStorage.this.database.executeFast("CREATE INDEX IF NOT EXISTS chat_pinned_mid_idx ON chat_pinned(uid, pinned) WHERE pinned != 0;").stepThis().dispose();
                        MessagesStorage.this.database.executeFast("CREATE TABLE IF NOT EXISTS users_data(uid INTEGER PRIMARY KEY, about TEXT)").stepThis().dispose();
                        MessagesStorage.this.database.executeFast("PRAGMA user_version = 31").stepThis().dispose();
                        version = 31;
                    }
                    if (version == 31) {
                        MessagesStorage.this.database.executeFast("DROP TABLE IF EXISTS bot_recent;").stepThis().dispose();
                        MessagesStorage.this.database.executeFast("CREATE TABLE IF NOT EXISTS chat_hints(did INTEGER, type INTEGER, rating REAL, date INTEGER, PRIMARY KEY(did, type))").stepThis().dispose();
                        MessagesStorage.this.database.executeFast("CREATE INDEX IF NOT EXISTS chat_hints_rating_idx ON chat_hints(rating);").stepThis().dispose();
                        MessagesStorage.this.database.executeFast("PRAGMA user_version = 32").stepThis().dispose();
                        version = 32;
                    }
                    if (version == 32) {
                        MessagesStorage.this.database.executeFast("DROP INDEX IF EXISTS uid_mid_idx_imp_messages;").stepThis().dispose();
                        MessagesStorage.this.database.executeFast("DROP INDEX IF EXISTS uid_date_mid_imp_idx_messages;").stepThis().dispose();
                        MessagesStorage.this.database.executeFast("PRAGMA user_version = 33").stepThis().dispose();
                        version = 33;
                    }
                    if (version == 33) {
                        MessagesStorage.this.database.executeFast("CREATE TABLE IF NOT EXISTS pending_tasks(id INTEGER PRIMARY KEY, data BLOB);").stepThis().dispose();
                        MessagesStorage.this.database.executeFast("PRAGMA user_version = 34").stepThis().dispose();
                        version = 34;
                    }
                    if (version == 34) {
                        MessagesStorage.this.database.executeFast("CREATE TABLE IF NOT EXISTS stickers_featured(id INTEGER PRIMARY KEY, data BLOB, unread BLOB, date INTEGER, hash TEXT);").stepThis().dispose();
                        MessagesStorage.this.database.executeFast("PRAGMA user_version = 35").stepThis().dispose();
                        version = 35;
                    }
                    if (version == 35) {
                        MessagesStorage.this.database.executeFast("CREATE TABLE IF NOT EXISTS requested_holes(uid INTEGER, seq_out_start INTEGER, seq_out_end INTEGER, PRIMARY KEY (uid, seq_out_start, seq_out_end));").stepThis().dispose();
                        MessagesStorage.this.database.executeFast("PRAGMA user_version = 36").stepThis().dispose();
                        version = 36;
                    }
                    if (version == 36) {
                        MessagesStorage.this.database.executeFast("ALTER TABLE enc_chats ADD COLUMN in_seq_no INTEGER default 0").stepThis().dispose();
                        MessagesStorage.this.database.executeFast("PRAGMA user_version = 37").stepThis().dispose();
                        version = 37;
                    }
                    if (version == 37) {
                        MessagesStorage.this.database.executeFast("CREATE TABLE IF NOT EXISTS botcache(id TEXT PRIMARY KEY, date INTEGER, data BLOB)").stepThis().dispose();
                        MessagesStorage.this.database.executeFast("CREATE INDEX IF NOT EXISTS botcache_date_idx ON botcache(date);").stepThis().dispose();
                        MessagesStorage.this.database.executeFast("PRAGMA user_version = 38").stepThis().dispose();
                        version = 38;
                    }
                    if (version == 38) {
                        MessagesStorage.this.database.executeFast("ALTER TABLE dialogs ADD COLUMN pinned INTEGER default 0").stepThis().dispose();
                        MessagesStorage.this.database.executeFast("PRAGMA user_version = 39").stepThis().dispose();
                        version = 39;
                    }
                    if (version == 39) {
                        MessagesStorage.this.database.executeFast("ALTER TABLE enc_chats ADD COLUMN admin_id INTEGER default 0").stepThis().dispose();
                        MessagesStorage.this.database.executeFast("PRAGMA user_version = 40").stepThis().dispose();
                        version = 40;
                    }
                    if (version == 40) {
                        MessagesStorage.this.fixNotificationSettings();
                        MessagesStorage.this.database.executeFast("PRAGMA user_version = 41").stepThis().dispose();
                        version = 41;
                    }
                    if (version == 41) {
                        MessagesStorage.this.database.executeFast("ALTER TABLE messages ADD COLUMN mention INTEGER default 0").stepThis().dispose();
                        MessagesStorage.this.database.executeFast("ALTER TABLE user_contacts_v6 ADD COLUMN imported INTEGER default 0").stepThis().dispose();
                        MessagesStorage.this.database.executeFast("CREATE INDEX IF NOT EXISTS uid_mention_idx_messages ON messages(uid, mention, read_state);").stepThis().dispose();
                        MessagesStorage.this.database.executeFast("PRAGMA user_version = 42").stepThis().dispose();
                        version = 42;
                    }
                    if (version == 42) {
                        MessagesStorage.this.database.executeFast("CREATE TABLE IF NOT EXISTS sharing_locations(uid INTEGER PRIMARY KEY, mid INTEGER, date INTEGER, period INTEGER, message BLOB);").stepThis().dispose();
                        MessagesStorage.this.database.executeFast("PRAGMA user_version = 43").stepThis().dispose();
                        version = 43;
                    }
                    if (version == 43) {
                        MessagesStorage.this.database.executeFast("CREATE TABLE IF NOT EXISTS channel_admins(did INTEGER, uid INTEGER, PRIMARY KEY(did, uid))").stepThis().dispose();
                        MessagesStorage.this.database.executeFast("PRAGMA user_version = 44").stepThis().dispose();
                        version = 44;
                    }
                    if (version == 44) {
                        MessagesStorage.this.database.executeFast("CREATE TABLE IF NOT EXISTS user_contacts_v7(key TEXT PRIMARY KEY, uid INTEGER, fname TEXT, sname TEXT, imported INTEGER)").stepThis().dispose();
                        MessagesStorage.this.database.executeFast("CREATE TABLE IF NOT EXISTS user_phones_v7(key TEXT, phone TEXT, sphone TEXT, deleted INTEGER, PRIMARY KEY (key, phone))").stepThis().dispose();
                        MessagesStorage.this.database.executeFast("CREATE INDEX IF NOT EXISTS sphone_deleted_idx_user_phones ON user_phones_v7(sphone, deleted);").stepThis().dispose();
                        MessagesStorage.this.database.executeFast("PRAGMA user_version = 45").stepThis().dispose();
                        version = 45;
                    }
                    if (version == 45) {
                        MessagesStorage.this.database.executeFast("ALTER TABLE enc_chats ADD COLUMN mtproto_seq INTEGER default 0").stepThis().dispose();
                        MessagesStorage.this.database.executeFast("PRAGMA user_version = 46").stepThis().dispose();
                        version = 46;
                    }
                    if (version == 46) {
                        MessagesStorage.this.database.executeFast("DELETE FROM botcache WHERE 1").stepThis().dispose();
                        MessagesStorage.this.database.executeFast("PRAGMA user_version = 47").stepThis().dispose();
                    }
                } catch (Throwable e) {
                    FileLog.e(e);
                }
            }
        });
    }

    private void cleanupInternal() {
        this.lastDateValue = 0;
        this.lastSeqValue = 0;
        this.lastPtsValue = 0;
        this.lastQtsValue = 0;
        this.lastSecretVersion = 0;
        this.lastSavedSeq = 0;
        this.lastSavedPts = 0;
        this.lastSavedDate = 0;
        this.lastSavedQts = 0;
        this.secretPBytes = null;
        this.secretG = 0;
        if (this.database != null) {
            this.database.close();
            this.database = null;
        }
        if (this.cacheFile != null) {
            this.cacheFile.delete();
            this.cacheFile = null;
        }
        if (this.walCacheFile != null) {
            this.walCacheFile.delete();
            this.walCacheFile = null;
        }
        if (this.shmCacheFile != null) {
            this.shmCacheFile.delete();
            this.shmCacheFile = null;
        }
    }

    public void cleanup(final boolean isLogin) {
        this.storageQueue.cleanupQueue();
        this.storageQueue.postRunnable(new Runnable() {
            public void run() {
                MessagesStorage.this.cleanupInternal();
                MessagesStorage.this.openDatabase(false);
                if (isLogin) {
                    Utilities.stageQueue.postRunnable(new Runnable() {
                        public void run() {
                            MessagesController.getInstance(MessagesStorage.this.currentAccount).getDifference();
                        }
                    });
                }
            }
        });
    }

    public void saveSecretParams(final int lsv, final int sg, final byte[] pbytes) {
        this.storageQueue.postRunnable(new Runnable() {
            public void run() {
                try {
                    SQLitePreparedStatement state = MessagesStorage.this.database.executeFast("UPDATE params SET lsv = ?, sg = ?, pbytes = ? WHERE id = 1");
                    int i = 1;
                    state.bindInteger(1, lsv);
                    state.bindInteger(2, sg);
                    if (pbytes != null) {
                        i = pbytes.length;
                    }
                    NativeByteBuffer data = new NativeByteBuffer(i);
                    if (pbytes != null) {
                        data.writeBytes(pbytes);
                    }
                    state.bindByteBuffer(3, data);
                    state.step();
                    state.dispose();
                    data.reuse();
                } catch (Throwable e) {
                    FileLog.e(e);
                }
            }
        });
    }

    private void fixNotificationSettings() {
        this.storageQueue.postRunnable(new Runnable() {
            public void run() {
                try {
                    LongSparseArray<Long> ids = new LongSparseArray();
                    Map<String, ?> values = MessagesController.getNotificationsSettings(MessagesStorage.this.currentAccount).getAll();
                    for (Entry<String, ?> entry : values.entrySet()) {
                        String key = (String) entry.getKey();
                        if (key.startsWith("notify2_")) {
                            Integer value = (Integer) entry.getValue();
                            if (value.intValue() != 2) {
                                if (value.intValue() != 3) {
                                    value.intValue();
                                }
                            }
                            key = key.replace("notify2_", TtmlNode.ANONYMOUS_REGION_ID);
                            long flags = 1;
                            if (value.intValue() == 2) {
                                flags = 1;
                            } else {
                                StringBuilder stringBuilder = new StringBuilder();
                                stringBuilder.append("notifyuntil_");
                                stringBuilder.append(key);
                                Integer time = (Integer) values.get(stringBuilder.toString());
                                if (time != null) {
                                    flags = (((long) time.intValue()) << 32) | 1;
                                }
                            }
                            ids.put(Long.parseLong(key), Long.valueOf(flags));
                        }
                    }
                    try {
                        MessagesStorage.this.database.beginTransaction();
                        SQLitePreparedStatement state = MessagesStorage.this.database.executeFast("REPLACE INTO dialog_settings VALUES(?, ?)");
                        for (int a = 0; a < ids.size(); a++) {
                            state.requery();
                            state.bindLong(1, ids.keyAt(a));
                            state.bindLong(2, ((Long) ids.valueAt(a)).longValue());
                            state.step();
                        }
                        state.dispose();
                        MessagesStorage.this.database.commitTransaction();
                    } catch (Throwable e) {
                        FileLog.e(e);
                    }
                } catch (Exception e2) {
                    e2.printStackTrace();
                } catch (Throwable e3) {
                    FileLog.e(e3);
                }
            }
        });
    }

    public long createPendingTask(final NativeByteBuffer data) {
        if (data == null) {
            return 0;
        }
        final long id = this.lastTaskId.getAndAdd(1);
        this.storageQueue.postRunnable(new Runnable() {
            public void run() {
                try {
                    SQLitePreparedStatement state = MessagesStorage.this.database.executeFast("REPLACE INTO pending_tasks VALUES(?, ?)");
                    state.bindLong(1, id);
                    state.bindByteBuffer(2, data);
                    state.step();
                    state.dispose();
                } catch (Throwable e) {
                    FileLog.e(e);
                } catch (Throwable th) {
                    data.reuse();
                }
                data.reuse();
            }
        });
        return id;
    }

    public void removePendingTask(final long id) {
        this.storageQueue.postRunnable(new Runnable() {
            public void run() {
                try {
                    SQLiteDatabase access$000 = MessagesStorage.this.database;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("DELETE FROM pending_tasks WHERE id = ");
                    stringBuilder.append(id);
                    access$000.executeFast(stringBuilder.toString()).stepThis().dispose();
                } catch (Throwable e) {
                    FileLog.e(e);
                }
            }
        });
    }

    private void loadPendingTasks() {
        this.storageQueue.postRunnable(new Runnable() {
            public void run() {
                try {
                    SQLiteCursor cursor = MessagesStorage.this.database.queryFinalized("SELECT id, data FROM pending_tasks WHERE 1", new Object[0]);
                    while (cursor.next()) {
                        final long taskId = cursor.longValue(0);
                        NativeByteBuffer data = cursor.byteBufferValue(1);
                        if (data != null) {
                            int type = data.readInt32(false);
                            final int channelId;
                            final int newDialogType;
                            final long j;
                            long random_id;
                            switch (type) {
                                case 0:
                                    final Chat chat = Chat.TLdeserialize(data, data.readInt32(false), false);
                                    if (chat != null) {
                                        Utilities.stageQueue.postRunnable(new Runnable() {
                                            public void run() {
                                                MessagesController.getInstance(MessagesStorage.this.currentAccount).loadUnknownChannel(chat, taskId);
                                            }
                                        });
                                        break;
                                    }
                                    break;
                                case 1:
                                    channelId = data.readInt32(false);
                                    newDialogType = data.readInt32(false);
                                    j = taskId;
                                    Utilities.stageQueue.postRunnable(new Runnable() {
                                        public void run() {
                                            MessagesController.getInstance(MessagesStorage.this.currentAccount).getChannelDifference(channelId, newDialogType, j, null);
                                        }
                                    });
                                    break;
                                case 2:
                                case 5:
                                case 8:
                                    TL_dialog dialog = new TL_dialog();
                                    dialog.id = data.readInt64(false);
                                    dialog.top_message = data.readInt32(false);
                                    dialog.read_inbox_max_id = data.readInt32(false);
                                    dialog.read_outbox_max_id = data.readInt32(false);
                                    dialog.unread_count = data.readInt32(false);
                                    dialog.last_message_date = data.readInt32(false);
                                    dialog.pts = data.readInt32(false);
                                    dialog.flags = data.readInt32(false);
                                    if (type >= 5) {
                                        dialog.pinned = data.readBool(false);
                                        dialog.pinnedNum = data.readInt32(false);
                                    }
                                    if (type >= 8) {
                                        dialog.unread_mentions_count = data.readInt32(false);
                                    }
                                    final InputPeer peer = InputPeer.TLdeserialize(data, data.readInt32(false), false);
                                    final TL_dialog tL_dialog = dialog;
                                    j = taskId;
                                    AndroidUtilities.runOnUIThread(new Runnable() {
                                        public void run() {
                                            MessagesController.getInstance(MessagesStorage.this.currentAccount).checkLastDialogMessage(tL_dialog, peer, j);
                                        }
                                    });
                                    break;
                                case 3:
                                    random_id = data.readInt64(false);
                                    SendMessagesHelper.getInstance(MessagesStorage.this.currentAccount).sendGame(InputPeer.TLdeserialize(data, data.readInt32(false), false), (TL_inputMediaGame) InputMedia.TLdeserialize(data, data.readInt32(false), false), random_id, taskId);
                                    break;
                                case 4:
                                    final long did = data.readInt64(false);
                                    final boolean pin = data.readBool(false);
                                    final InputPeer peer2 = InputPeer.TLdeserialize(data, data.readInt32(false), false);
                                    final long j2 = taskId;
                                    AndroidUtilities.runOnUIThread(new Runnable() {
                                        public void run() {
                                            MessagesController.getInstance(MessagesStorage.this.currentAccount).pinDialog(did, pin, peer2, j2);
                                        }
                                    });
                                    break;
                                case 6:
                                    channelId = data.readInt32(false);
                                    newDialogType = data.readInt32(false);
                                    final InputChannel inputChannel = InputChannel.TLdeserialize(data, data.readInt32(false), false);
                                    j = taskId;
                                    Utilities.stageQueue.postRunnable(new Runnable() {
                                        public void run() {
                                            MessagesController.getInstance(MessagesStorage.this.currentAccount).getChannelDifference(channelId, newDialogType, j, inputChannel);
                                        }
                                    });
                                    break;
                                case 7:
                                    channelId = data.readInt32(false);
                                    int constructor = data.readInt32(false);
                                    TLObject request = TL_messages_deleteMessages.TLdeserialize(data, constructor, false);
                                    if (request == null) {
                                        request = TL_channels_deleteMessages.TLdeserialize(data, constructor, false);
                                    }
                                    TLObject request2 = request;
                                    if (request2 != null) {
                                        final TLObject finalRequest = request2;
                                        random_id = taskId;
                                        AndroidUtilities.runOnUIThread(new Runnable() {
                                            public void run() {
                                                MessagesController.getInstance(MessagesStorage.this.currentAccount).deleteMessages(null, null, null, channelId, true, random_id, finalRequest);
                                            }
                                        });
                                        break;
                                    }
                                    MessagesStorage.this.removePendingTask(taskId);
                                    break;
                                default:
                                    break;
                            }
                            data.reuse();
                        }
                    }
                    cursor.dispose();
                } catch (Throwable e) {
                    FileLog.e(e);
                }
            }
        });
    }

    public void saveChannelPts(final int channelId, final int pts) {
        this.storageQueue.postRunnable(new Runnable() {
            public void run() {
                try {
                    SQLitePreparedStatement state = MessagesStorage.this.database.executeFast("UPDATE dialogs SET pts = ? WHERE did = ?");
                    state.bindInteger(1, pts);
                    state.bindInteger(2, -channelId);
                    state.step();
                    state.dispose();
                } catch (Throwable e) {
                    FileLog.e(e);
                }
            }
        });
    }

    private void saveDiffParamsInternal(int seq, int pts, int date, int qts) {
        try {
            if (this.lastSavedSeq != seq || this.lastSavedPts != pts || this.lastSavedDate != date || this.lastQtsValue != qts) {
                SQLitePreparedStatement state = this.database.executeFast("UPDATE params SET seq = ?, pts = ?, date = ?, qts = ? WHERE id = 1");
                state.bindInteger(1, seq);
                state.bindInteger(2, pts);
                state.bindInteger(3, date);
                state.bindInteger(4, qts);
                state.step();
                state.dispose();
                this.lastSavedSeq = seq;
                this.lastSavedPts = pts;
                this.lastSavedDate = date;
                this.lastSavedQts = qts;
            }
        } catch (Throwable e) {
            FileLog.e(e);
        }
    }

    public void saveDiffParams(int seq, int pts, int date, int qts) {
        final int i = seq;
        final int i2 = pts;
        final int i3 = date;
        final int i4 = qts;
        this.storageQueue.postRunnable(new Runnable() {
            public void run() {
                MessagesStorage.this.saveDiffParamsInternal(i, i2, i3, i4);
            }
        });
    }

    public void setDialogFlags(long did, long flags) {
        final long j = did;
        final long j2 = flags;
        this.storageQueue.postRunnable(new Runnable() {
            public void run() {
                try {
                    MessagesStorage.this.database.executeFast(String.format(Locale.US, "REPLACE INTO dialog_settings VALUES(%d, %d)", new Object[]{Long.valueOf(j), Long.valueOf(j2)})).stepThis().dispose();
                } catch (Throwable e) {
                    FileLog.e(e);
                }
            }
        });
    }

    public void loadUnreadMessages() {
        this.storageQueue.postRunnable(new Runnable() {

            class AnonymousClass1 implements Runnable {
                final /* synthetic */ ArrayList val$chats;
                final /* synthetic */ ArrayList val$encryptedChats;
                final /* synthetic */ ArrayList val$messages;
                final /* synthetic */ LongSparseArray val$pushDialogs;
                final /* synthetic */ ArrayList val$users;

                AnonymousClass1(LongSparseArray longSparseArray, ArrayList arrayList, ArrayList arrayList2, ArrayList arrayList3, ArrayList arrayList4) {
                    this.val$pushDialogs = longSparseArray;
                    this.val$messages = arrayList;
                    this.val$users = arrayList2;
                    this.val$chats = arrayList3;
                    this.val$encryptedChats = arrayList4;
                }

                public void run() {
                    NotificationsController.getInstance(MessagesStorage.this.currentAccount).processLoadedUnreadMessages(this.val$pushDialogs, this.val$messages, this.val$users, this.val$chats, this.val$encryptedChats);
                }
            }

            public void run() {
                /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.MessagesStorage.12.run():void
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:116)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:249)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:256)
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
                r8 = r43;
                r1 = new java.util.ArrayList;	 Catch:{ Exception -> 0x04d8 }
                r1.<init>();	 Catch:{ Exception -> 0x04d8 }
                r9 = r1;	 Catch:{ Exception -> 0x04d8 }
                r1 = new java.util.ArrayList;	 Catch:{ Exception -> 0x04d8 }
                r1.<init>();	 Catch:{ Exception -> 0x04d8 }
                r10 = r1;	 Catch:{ Exception -> 0x04d8 }
                r1 = new java.util.ArrayList;	 Catch:{ Exception -> 0x04d8 }
                r1.<init>();	 Catch:{ Exception -> 0x04d8 }
                r11 = r1;	 Catch:{ Exception -> 0x04d8 }
                r1 = new android.util.LongSparseArray;	 Catch:{ Exception -> 0x04d8 }
                r1.<init>();	 Catch:{ Exception -> 0x04d8 }
                r12 = r1;	 Catch:{ Exception -> 0x04d8 }
                r1 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x04d8 }
                r1 = r1.database;	 Catch:{ Exception -> 0x04d8 }
                r2 = "SELECT d.did, d.unread_count, s.flags FROM dialogs as d LEFT JOIN dialog_settings as s ON d.did = s.did WHERE d.unread_count != 0";	 Catch:{ Exception -> 0x04d8 }
                r3 = 0;	 Catch:{ Exception -> 0x04d8 }
                r4 = new java.lang.Object[r3];	 Catch:{ Exception -> 0x04d8 }
                r1 = r1.queryFinalized(r2, r4);	 Catch:{ Exception -> 0x04d8 }
                r2 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x04d8 }
                r2.<init>();	 Catch:{ Exception -> 0x04d8 }
                r13 = r2;	 Catch:{ Exception -> 0x04d8 }
                r2 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x04d8 }
                r2 = r2.currentAccount;	 Catch:{ Exception -> 0x04d8 }
                r2 = org.telegram.tgnet.ConnectionsManager.getInstance(r2);	 Catch:{ Exception -> 0x04d8 }
                r2 = r2.getCurrentTime();	 Catch:{ Exception -> 0x04d8 }
                r14 = r2;	 Catch:{ Exception -> 0x04d8 }
                r2 = r1.next();	 Catch:{ Exception -> 0x04d8 }
                r4 = 32;	 Catch:{ Exception -> 0x04d8 }
                r5 = 2;	 Catch:{ Exception -> 0x04d8 }
                if (r2 == 0) goto L_0x00d3;	 Catch:{ Exception -> 0x04d8 }
            L_0x0047:
                r15 = r1.longValue(r5);	 Catch:{ Exception -> 0x04d8 }
                r17 = 1;	 Catch:{ Exception -> 0x04d8 }
                r19 = r15 & r17;	 Catch:{ Exception -> 0x04d8 }
                r17 = 0;	 Catch:{ Exception -> 0x04d8 }
                r2 = (r19 > r17 ? 1 : (r19 == r17 ? 0 : -1));	 Catch:{ Exception -> 0x04d8 }
                if (r2 == 0) goto L_0x0057;	 Catch:{ Exception -> 0x04d8 }
            L_0x0055:
                r2 = 1;	 Catch:{ Exception -> 0x04d8 }
                goto L_0x0058;	 Catch:{ Exception -> 0x04d8 }
            L_0x0057:
                r2 = r3;	 Catch:{ Exception -> 0x04d8 }
            L_0x0058:
                r6 = r15 >> r4;	 Catch:{ Exception -> 0x04d8 }
                r6 = (int) r6;	 Catch:{ Exception -> 0x04d8 }
                r5 = r1.isNull(r5);	 Catch:{ Exception -> 0x04d8 }
                if (r5 != 0) goto L_0x0067;	 Catch:{ Exception -> 0x04d8 }
            L_0x0061:
                if (r2 == 0) goto L_0x0067;	 Catch:{ Exception -> 0x04d8 }
            L_0x0063:
                if (r6 == 0) goto L_0x00ce;	 Catch:{ Exception -> 0x04d8 }
            L_0x0065:
                if (r6 >= r14) goto L_0x00ce;	 Catch:{ Exception -> 0x04d8 }
            L_0x0067:
                r17 = r1.longValue(r3);	 Catch:{ Exception -> 0x04d8 }
                r21 = r17;	 Catch:{ Exception -> 0x04d8 }
                r5 = 1;	 Catch:{ Exception -> 0x04d8 }
                r5 = r1.intValue(r5);	 Catch:{ Exception -> 0x04d8 }
                r7 = java.lang.Integer.valueOf(r5);	 Catch:{ Exception -> 0x04d8 }
                r3 = r21;	 Catch:{ Exception -> 0x04d8 }
                r12.put(r3, r7);	 Catch:{ Exception -> 0x04d8 }
                r7 = r13.length();	 Catch:{ Exception -> 0x04d8 }
                if (r7 == 0) goto L_0x0086;	 Catch:{ Exception -> 0x04d8 }
                r7 = ",";	 Catch:{ Exception -> 0x04d8 }
                r13.append(r7);	 Catch:{ Exception -> 0x04d8 }
                r13.append(r3);	 Catch:{ Exception -> 0x04d8 }
                r7 = (int) r3;	 Catch:{ Exception -> 0x04d8 }
                r24 = r5;	 Catch:{ Exception -> 0x04d8 }
                r23 = r6;	 Catch:{ Exception -> 0x04d8 }
                r17 = 32;	 Catch:{ Exception -> 0x04d8 }
                r5 = r3 >> r17;	 Catch:{ Exception -> 0x04d8 }
                r5 = (int) r5;	 Catch:{ Exception -> 0x04d8 }
                if (r7 == 0) goto L_0x00bd;	 Catch:{ Exception -> 0x04d8 }
                if (r7 >= 0) goto L_0x00ab;	 Catch:{ Exception -> 0x04d8 }
                r6 = -r7;	 Catch:{ Exception -> 0x04d8 }
                r6 = java.lang.Integer.valueOf(r6);	 Catch:{ Exception -> 0x04d8 }
                r6 = r10.contains(r6);	 Catch:{ Exception -> 0x04d8 }
                if (r6 != 0) goto L_0x00ce;	 Catch:{ Exception -> 0x04d8 }
                r6 = -r7;	 Catch:{ Exception -> 0x04d8 }
                r6 = java.lang.Integer.valueOf(r6);	 Catch:{ Exception -> 0x04d8 }
                r10.add(r6);	 Catch:{ Exception -> 0x04d8 }
                goto L_0x00ce;	 Catch:{ Exception -> 0x04d8 }
                r6 = java.lang.Integer.valueOf(r7);	 Catch:{ Exception -> 0x04d8 }
                r6 = r9.contains(r6);	 Catch:{ Exception -> 0x04d8 }
                if (r6 != 0) goto L_0x00ce;	 Catch:{ Exception -> 0x04d8 }
                r6 = java.lang.Integer.valueOf(r7);	 Catch:{ Exception -> 0x04d8 }
                r9.add(r6);	 Catch:{ Exception -> 0x04d8 }
                goto L_0x00ce;	 Catch:{ Exception -> 0x04d8 }
                r6 = java.lang.Integer.valueOf(r5);	 Catch:{ Exception -> 0x04d8 }
                r6 = r11.contains(r6);	 Catch:{ Exception -> 0x04d8 }
                if (r6 != 0) goto L_0x00ce;	 Catch:{ Exception -> 0x04d8 }
                r6 = java.lang.Integer.valueOf(r5);	 Catch:{ Exception -> 0x04d8 }
                r11.add(r6);	 Catch:{ Exception -> 0x04d8 }
                r2 = r14;	 Catch:{ Exception -> 0x04d8 }
                r3 = 0;	 Catch:{ Exception -> 0x04d8 }
                goto L_0x003d;	 Catch:{ Exception -> 0x04d8 }
            L_0x00d3:
                r1.dispose();	 Catch:{ Exception -> 0x04d8 }
                r2 = new java.util.ArrayList;	 Catch:{ Exception -> 0x04d8 }
                r2.<init>();	 Catch:{ Exception -> 0x04d8 }
                r15 = r2;	 Catch:{ Exception -> 0x04d8 }
                r2 = new android.util.SparseArray;	 Catch:{ Exception -> 0x04d8 }
                r2.<init>();	 Catch:{ Exception -> 0x04d8 }
                r7 = r2;	 Catch:{ Exception -> 0x04d8 }
                r2 = new java.util.ArrayList;	 Catch:{ Exception -> 0x04d8 }
                r2.<init>();	 Catch:{ Exception -> 0x04d8 }
                r6 = r2;	 Catch:{ Exception -> 0x04d8 }
                r2 = new java.util.ArrayList;	 Catch:{ Exception -> 0x04d8 }
                r2.<init>();	 Catch:{ Exception -> 0x04d8 }
                r4 = r2;	 Catch:{ Exception -> 0x04d8 }
                r2 = new java.util.ArrayList;	 Catch:{ Exception -> 0x04d8 }
                r2.<init>();	 Catch:{ Exception -> 0x04d8 }
                r3 = r2;	 Catch:{ Exception -> 0x04d8 }
                r2 = new java.util.ArrayList;	 Catch:{ Exception -> 0x04d8 }
                r2.<init>();	 Catch:{ Exception -> 0x04d8 }
                r16 = r13.length();	 Catch:{ Exception -> 0x04d8 }
                if (r16 <= 0) goto L_0x04a9;	 Catch:{ Exception -> 0x04d8 }
                r5 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x04d8 }
                r5 = r5.database;	 Catch:{ Exception -> 0x04d8 }
                r25 = r1;	 Catch:{ Exception -> 0x04d8 }
                r1 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x04d8 }
                r1.<init>();	 Catch:{ Exception -> 0x04d8 }
                r26 = r14;	 Catch:{ Exception -> 0x04d8 }
                r14 = "SELECT read_state, data, send_state, mid, date, uid, replydata FROM messages WHERE uid IN (";	 Catch:{ Exception -> 0x04d8 }
                r1.append(r14);	 Catch:{ Exception -> 0x04d8 }
                r14 = r13.toString();	 Catch:{ Exception -> 0x04d8 }
                r1.append(r14);	 Catch:{ Exception -> 0x04d8 }
                r14 = ") AND out = 0 AND read_state IN(0,2) ORDER BY date DESC LIMIT 50";	 Catch:{ Exception -> 0x04d8 }
                r1.append(r14);	 Catch:{ Exception -> 0x04d8 }
                r1 = r1.toString();	 Catch:{ Exception -> 0x04d8 }
                r27 = r13;	 Catch:{ Exception -> 0x04d8 }
                r14 = 0;	 Catch:{ Exception -> 0x04d8 }
                r13 = new java.lang.Object[r14];	 Catch:{ Exception -> 0x04d8 }
                r1 = r5.queryFinalized(r1, r13);	 Catch:{ Exception -> 0x04d8 }
                r5 = r1.next();	 Catch:{ Exception -> 0x04d8 }
                if (r5 == 0) goto L_0x02c0;	 Catch:{ Exception -> 0x04d8 }
                r5 = 1;	 Catch:{ Exception -> 0x04d8 }
                r16 = r1.byteBufferValue(r5);	 Catch:{ Exception -> 0x04d8 }
                r5 = r16;	 Catch:{ Exception -> 0x04d8 }
                if (r5 == 0) goto L_0x02b0;	 Catch:{ Exception -> 0x04d8 }
                r13 = 0;	 Catch:{ Exception -> 0x04d8 }
                r14 = r5.readInt32(r13);	 Catch:{ Exception -> 0x04d8 }
                r14 = org.telegram.tgnet.TLRPC.Message.TLdeserialize(r5, r14, r13);	 Catch:{ Exception -> 0x04d8 }
                r13 = r14;	 Catch:{ Exception -> 0x04d8 }
                r14 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x04d8 }
                r14 = r14.currentAccount;	 Catch:{ Exception -> 0x04d8 }
                r14 = org.telegram.messenger.UserConfig.getInstance(r14);	 Catch:{ Exception -> 0x04d8 }
                r14 = r14.clientUserId;	 Catch:{ Exception -> 0x04d8 }
                r13.readAttachPath(r5, r14);	 Catch:{ Exception -> 0x04d8 }
                r5.reuse();	 Catch:{ Exception -> 0x04d8 }
                r28 = r5;	 Catch:{ Exception -> 0x04d8 }
                r14 = 0;	 Catch:{ Exception -> 0x04d8 }
                r5 = r1.intValue(r14);	 Catch:{ Exception -> 0x04d8 }
                org.telegram.messenger.MessageObject.setUnreadFlags(r13, r5);	 Catch:{ Exception -> 0x04d8 }
                r5 = 3;	 Catch:{ Exception -> 0x04d8 }
                r5 = r1.intValue(r5);	 Catch:{ Exception -> 0x04d8 }
                r13.id = r5;	 Catch:{ Exception -> 0x04d8 }
                r5 = 4;	 Catch:{ Exception -> 0x04d8 }
                r5 = r1.intValue(r5);	 Catch:{ Exception -> 0x04d8 }
                r13.date = r5;	 Catch:{ Exception -> 0x04d8 }
                r5 = 5;	 Catch:{ Exception -> 0x04d8 }
                r30 = r3;	 Catch:{ Exception -> 0x04d8 }
                r29 = r4;	 Catch:{ Exception -> 0x04d8 }
                r3 = r1.longValue(r5);	 Catch:{ Exception -> 0x04d8 }
                r13.dialog_id = r3;	 Catch:{ Exception -> 0x04d8 }
                r6.add(r13);	 Catch:{ Exception -> 0x04d8 }
                r3 = r13.dialog_id;	 Catch:{ Exception -> 0x04d8 }
                r3 = (int) r3;	 Catch:{ Exception -> 0x04d8 }
                org.telegram.messenger.MessagesStorage.addUsersAndChatsFromMessage(r13, r9, r10);	 Catch:{ Exception -> 0x04d8 }
                r4 = 2;	 Catch:{ Exception -> 0x04d8 }
                r14 = r1.intValue(r4);	 Catch:{ Exception -> 0x04d8 }
                r13.send_state = r14;	 Catch:{ Exception -> 0x04d8 }
                r4 = r13.to_id;	 Catch:{ Exception -> 0x04d8 }
                r4 = r4.channel_id;	 Catch:{ Exception -> 0x04d8 }
                if (r4 != 0) goto L_0x0198;	 Catch:{ Exception -> 0x04d8 }
                r4 = org.telegram.messenger.MessageObject.isUnread(r13);	 Catch:{ Exception -> 0x04d8 }
                if (r4 != 0) goto L_0x0198;	 Catch:{ Exception -> 0x04d8 }
                if (r3 != 0) goto L_0x019c;	 Catch:{ Exception -> 0x04d8 }
                r4 = r13.id;	 Catch:{ Exception -> 0x04d8 }
                if (r4 <= 0) goto L_0x019f;	 Catch:{ Exception -> 0x04d8 }
                r4 = 0;	 Catch:{ Exception -> 0x04d8 }
                r13.send_state = r4;	 Catch:{ Exception -> 0x04d8 }
                if (r3 != 0) goto L_0x01ad;	 Catch:{ Exception -> 0x04d8 }
                r4 = r1.isNull(r5);	 Catch:{ Exception -> 0x04d8 }
                if (r4 != 0) goto L_0x01ad;	 Catch:{ Exception -> 0x04d8 }
                r4 = r1.longValue(r5);	 Catch:{ Exception -> 0x04d8 }
                r13.random_id = r4;	 Catch:{ Exception -> 0x04d8 }
                r4 = r13.reply_to_msg_id;	 Catch:{ Exception -> 0x02a2 }
                if (r4 == 0) goto L_0x029b;	 Catch:{ Exception -> 0x02a2 }
                r4 = r13.action;	 Catch:{ Exception -> 0x02a2 }
                r4 = r4 instanceof org.telegram.tgnet.TLRPC.TL_messageActionPinMessage;	 Catch:{ Exception -> 0x02a2 }
                if (r4 != 0) goto L_0x01d8;
                r4 = r13.action;	 Catch:{ Exception -> 0x01cc }
                r4 = r4 instanceof org.telegram.tgnet.TLRPC.TL_messageActionPaymentSent;	 Catch:{ Exception -> 0x01cc }
                if (r4 != 0) goto L_0x01d8;	 Catch:{ Exception -> 0x01cc }
                r4 = r13.action;	 Catch:{ Exception -> 0x01cc }
                r4 = r4 instanceof org.telegram.tgnet.TLRPC.TL_messageActionGameScore;	 Catch:{ Exception -> 0x01cc }
                if (r4 == 0) goto L_0x01c4;
                goto L_0x01d8;
                r31 = r3;
                r32 = r6;
                r14 = 32;
                goto L_0x02a1;
            L_0x01cc:
                r0 = move-exception;
                r31 = r3;
                r32 = r6;
                r33 = r28;
                r14 = 32;
                r3 = r0;
                goto L_0x02ac;
                r4 = 6;
                r5 = r1.isNull(r4);	 Catch:{ Exception -> 0x02a2 }
                if (r5 != 0) goto L_0x0227;
                r4 = r1.byteBufferValue(r4);	 Catch:{ Exception -> 0x01cc }
                r5 = r4;
                if (r5 == 0) goto L_0x0229;
                r4 = 0;
                r14 = r5.readInt32(r4);	 Catch:{ Exception -> 0x021f }
                r14 = org.telegram.tgnet.TLRPC.Message.TLdeserialize(r5, r14, r4);	 Catch:{ Exception -> 0x021f }
                r13.replyMessage = r14;	 Catch:{ Exception -> 0x021f }
                r4 = r13.replyMessage;	 Catch:{ Exception -> 0x021f }
                r14 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x021f }
                r14 = r14.currentAccount;	 Catch:{ Exception -> 0x021f }
                r14 = org.telegram.messenger.UserConfig.getInstance(r14);	 Catch:{ Exception -> 0x021f }
                r14 = r14.clientUserId;	 Catch:{ Exception -> 0x021f }
                r4.readAttachPath(r5, r14);	 Catch:{ Exception -> 0x021f }
                r5.reuse();	 Catch:{ Exception -> 0x021f }
                r4 = r13.replyMessage;	 Catch:{ Exception -> 0x021f }
                if (r4 == 0) goto L_0x0229;	 Catch:{ Exception -> 0x021f }
                r4 = org.telegram.messenger.MessageObject.isMegagroup(r13);	 Catch:{ Exception -> 0x021f }
                if (r4 == 0) goto L_0x0219;	 Catch:{ Exception -> 0x021f }
                r4 = r13.replyMessage;	 Catch:{ Exception -> 0x021f }
                r14 = r4.flags;	 Catch:{ Exception -> 0x021f }
                r16 = -2147483648; // 0xffffffff80000000 float:-0.0 double:NaN;	 Catch:{ Exception -> 0x021f }
                r14 = r14 | r16;	 Catch:{ Exception -> 0x021f }
                r4.flags = r14;	 Catch:{ Exception -> 0x021f }
                r4 = r13.replyMessage;	 Catch:{ Exception -> 0x021f }
                org.telegram.messenger.MessagesStorage.addUsersAndChatsFromMessage(r4, r9, r10);	 Catch:{ Exception -> 0x021f }
                goto L_0x0229;
            L_0x021f:
                r0 = move-exception;
                r31 = r3;
                r33 = r5;
                r32 = r6;
                goto L_0x01d3;
                r5 = r28;
                r4 = r13.replyMessage;	 Catch:{ Exception -> 0x0290 }
                if (r4 != 0) goto L_0x0285;	 Catch:{ Exception -> 0x0290 }
                r4 = r13.reply_to_msg_id;	 Catch:{ Exception -> 0x0290 }
                r31 = r3;
                r3 = (long) r4;
                r14 = r13.to_id;	 Catch:{ Exception -> 0x027c }
                r14 = r14.channel_id;	 Catch:{ Exception -> 0x027c }
                if (r14 == 0) goto L_0x0249;	 Catch:{ Exception -> 0x027c }
                r14 = r13.to_id;	 Catch:{ Exception -> 0x027c }
                r14 = r14.channel_id;	 Catch:{ Exception -> 0x027c }
                r33 = r5;
                r32 = r6;
                r5 = (long) r14;
                r14 = 32;
                r5 = r5 << r14;
                r16 = r3 | r5;
                r3 = r16;
                goto L_0x024f;
                r33 = r5;
                r32 = r6;
                r14 = 32;
                r5 = java.lang.Long.valueOf(r3);	 Catch:{ Exception -> 0x0279 }
                r5 = r15.contains(r5);	 Catch:{ Exception -> 0x0279 }
                if (r5 != 0) goto L_0x0260;	 Catch:{ Exception -> 0x0279 }
                r5 = java.lang.Long.valueOf(r3);	 Catch:{ Exception -> 0x0279 }
                r15.add(r5);	 Catch:{ Exception -> 0x0279 }
                r5 = r13.reply_to_msg_id;	 Catch:{ Exception -> 0x0279 }
                r5 = r7.get(r5);	 Catch:{ Exception -> 0x0279 }
                r5 = (java.util.ArrayList) r5;	 Catch:{ Exception -> 0x0279 }
                if (r5 != 0) goto L_0x0275;	 Catch:{ Exception -> 0x0279 }
                r6 = new java.util.ArrayList;	 Catch:{ Exception -> 0x0279 }
                r6.<init>();	 Catch:{ Exception -> 0x0279 }
                r5 = r6;	 Catch:{ Exception -> 0x0279 }
                r6 = r13.reply_to_msg_id;	 Catch:{ Exception -> 0x0279 }
                r7.put(r6, r5);	 Catch:{ Exception -> 0x0279 }
                r5.add(r13);	 Catch:{ Exception -> 0x0279 }
                goto L_0x028d;
            L_0x0279:
                r0 = move-exception;
                goto L_0x01d5;
            L_0x027c:
                r0 = move-exception;
                r33 = r5;
                r32 = r6;
                r14 = 32;
                r3 = r0;
                goto L_0x02ac;
                r31 = r3;
                r33 = r5;
                r32 = r6;
                r14 = 32;
                r28 = r33;
                goto L_0x02a1;
            L_0x0290:
                r0 = move-exception;
                r31 = r3;
                r33 = r5;
                r32 = r6;
                r14 = 32;
                r3 = r0;
                goto L_0x02ac;
                r31 = r3;
                r32 = r6;
                r14 = 32;
                goto L_0x02b8;
            L_0x02a2:
                r0 = move-exception;
                r31 = r3;
                r32 = r6;
                r14 = 32;
                r3 = r0;
                r33 = r28;
                org.telegram.messenger.FileLog.e(r3);	 Catch:{ Exception -> 0x04d8 }
                goto L_0x02b8;	 Catch:{ Exception -> 0x04d8 }
                r30 = r3;	 Catch:{ Exception -> 0x04d8 }
                r29 = r4;	 Catch:{ Exception -> 0x04d8 }
                r32 = r6;	 Catch:{ Exception -> 0x04d8 }
                r14 = 32;	 Catch:{ Exception -> 0x04d8 }
                r4 = r29;	 Catch:{ Exception -> 0x04d8 }
                r3 = r30;	 Catch:{ Exception -> 0x04d8 }
                r6 = r32;	 Catch:{ Exception -> 0x04d8 }
                goto L_0x012c;	 Catch:{ Exception -> 0x04d8 }
                r30 = r3;	 Catch:{ Exception -> 0x04d8 }
                r29 = r4;	 Catch:{ Exception -> 0x04d8 }
                r32 = r6;	 Catch:{ Exception -> 0x04d8 }
                r1.dispose();	 Catch:{ Exception -> 0x04d8 }
                r3 = r15.isEmpty();	 Catch:{ Exception -> 0x04d8 }
                if (r3 != 0) goto L_0x036d;	 Catch:{ Exception -> 0x04d8 }
                r3 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x04d8 }
                r3 = r3.database;	 Catch:{ Exception -> 0x04d8 }
                r4 = java.util.Locale.US;	 Catch:{ Exception -> 0x04d8 }
                r5 = "SELECT data, mid, date, uid FROM messages WHERE mid IN(%s)";	 Catch:{ Exception -> 0x04d8 }
                r6 = 1;	 Catch:{ Exception -> 0x04d8 }
                r13 = new java.lang.Object[r6];	 Catch:{ Exception -> 0x04d8 }
                r6 = ",";	 Catch:{ Exception -> 0x04d8 }
                r6 = android.text.TextUtils.join(r6, r15);	 Catch:{ Exception -> 0x04d8 }
                r14 = 0;	 Catch:{ Exception -> 0x04d8 }
                r13[r14] = r6;	 Catch:{ Exception -> 0x04d8 }
                r4 = java.lang.String.format(r4, r5, r13);	 Catch:{ Exception -> 0x04d8 }
                r5 = new java.lang.Object[r14];	 Catch:{ Exception -> 0x04d8 }
                r3 = r3.queryFinalized(r4, r5);	 Catch:{ Exception -> 0x04d8 }
                r1 = r3;	 Catch:{ Exception -> 0x04d8 }
                r3 = r1.next();	 Catch:{ Exception -> 0x04d8 }
                if (r3 == 0) goto L_0x036a;	 Catch:{ Exception -> 0x04d8 }
                r3 = 0;	 Catch:{ Exception -> 0x04d8 }
                r4 = r1.byteBufferValue(r3);	 Catch:{ Exception -> 0x04d8 }
                if (r4 == 0) goto L_0x0367;	 Catch:{ Exception -> 0x04d8 }
                r5 = r4.readInt32(r3);	 Catch:{ Exception -> 0x04d8 }
                r5 = org.telegram.tgnet.TLRPC.Message.TLdeserialize(r4, r5, r3);	 Catch:{ Exception -> 0x04d8 }
                r3 = r5;	 Catch:{ Exception -> 0x04d8 }
                r5 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x04d8 }
                r5 = r5.currentAccount;	 Catch:{ Exception -> 0x04d8 }
                r5 = org.telegram.messenger.UserConfig.getInstance(r5);	 Catch:{ Exception -> 0x04d8 }
                r5 = r5.clientUserId;	 Catch:{ Exception -> 0x04d8 }
                r3.readAttachPath(r4, r5);	 Catch:{ Exception -> 0x04d8 }
                r4.reuse();	 Catch:{ Exception -> 0x04d8 }
                r5 = 1;	 Catch:{ Exception -> 0x04d8 }
                r6 = r1.intValue(r5);	 Catch:{ Exception -> 0x04d8 }
                r3.id = r6;	 Catch:{ Exception -> 0x04d8 }
                r5 = 2;	 Catch:{ Exception -> 0x04d8 }
                r6 = r1.intValue(r5);	 Catch:{ Exception -> 0x04d8 }
                r3.date = r6;	 Catch:{ Exception -> 0x04d8 }
                r6 = 3;	 Catch:{ Exception -> 0x04d8 }
                r13 = r1.longValue(r6);	 Catch:{ Exception -> 0x04d8 }
                r3.dialog_id = r13;	 Catch:{ Exception -> 0x04d8 }
                org.telegram.messenger.MessagesStorage.addUsersAndChatsFromMessage(r3, r9, r10);	 Catch:{ Exception -> 0x04d8 }
                r13 = r3.id;	 Catch:{ Exception -> 0x04d8 }
                r13 = r7.get(r13);	 Catch:{ Exception -> 0x04d8 }
                r13 = (java.util.ArrayList) r13;	 Catch:{ Exception -> 0x04d8 }
                if (r13 == 0) goto L_0x0367;	 Catch:{ Exception -> 0x04d8 }
                r14 = 0;	 Catch:{ Exception -> 0x04d8 }
                r5 = r13.size();	 Catch:{ Exception -> 0x04d8 }
                if (r14 >= r5) goto L_0x0367;	 Catch:{ Exception -> 0x04d8 }
                r5 = r13.get(r14);	 Catch:{ Exception -> 0x04d8 }
                r5 = (org.telegram.tgnet.TLRPC.Message) r5;	 Catch:{ Exception -> 0x04d8 }
                r5.replyMessage = r3;	 Catch:{ Exception -> 0x04d8 }
                r16 = org.telegram.messenger.MessageObject.isMegagroup(r5);	 Catch:{ Exception -> 0x04d8 }
                if (r16 == 0) goto L_0x035c;	 Catch:{ Exception -> 0x04d8 }
                r6 = r5.replyMessage;	 Catch:{ Exception -> 0x04d8 }
                r34 = r3;	 Catch:{ Exception -> 0x04d8 }
                r3 = r6.flags;	 Catch:{ Exception -> 0x04d8 }
                r16 = -2147483648; // 0xffffffff80000000 float:-0.0 double:NaN;	 Catch:{ Exception -> 0x04d8 }
                r3 = r3 | r16;	 Catch:{ Exception -> 0x04d8 }
                r6.flags = r3;	 Catch:{ Exception -> 0x04d8 }
                goto L_0x0360;	 Catch:{ Exception -> 0x04d8 }
                r34 = r3;	 Catch:{ Exception -> 0x04d8 }
                r16 = -2147483648; // 0xffffffff80000000 float:-0.0 double:NaN;	 Catch:{ Exception -> 0x04d8 }
                r14 = r14 + 1;	 Catch:{ Exception -> 0x04d8 }
                r3 = r34;	 Catch:{ Exception -> 0x04d8 }
                r5 = 2;	 Catch:{ Exception -> 0x04d8 }
                r6 = 3;	 Catch:{ Exception -> 0x04d8 }
                goto L_0x033b;	 Catch:{ Exception -> 0x04d8 }
                r16 = -2147483648; // 0xffffffff80000000 float:-0.0 double:NaN;	 Catch:{ Exception -> 0x04d8 }
                goto L_0x02f0;	 Catch:{ Exception -> 0x04d8 }
                r1.dispose();	 Catch:{ Exception -> 0x04d8 }
                r3 = r11.isEmpty();	 Catch:{ Exception -> 0x04d8 }
                if (r3 != 0) goto L_0x037e;	 Catch:{ Exception -> 0x04d8 }
                r3 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x04d8 }
                r4 = ",";	 Catch:{ Exception -> 0x04d8 }
                r4 = android.text.TextUtils.join(r4, r11);	 Catch:{ Exception -> 0x04d8 }
                r3.getEncryptedChatsInternal(r4, r2, r9);	 Catch:{ Exception -> 0x04d8 }
                r3 = r9.isEmpty();	 Catch:{ Exception -> 0x04d8 }
                if (r3 != 0) goto L_0x0392;	 Catch:{ Exception -> 0x04d8 }
                r3 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x04d8 }
                r4 = ",";	 Catch:{ Exception -> 0x04d8 }
                r4 = android.text.TextUtils.join(r4, r9);	 Catch:{ Exception -> 0x04d8 }
                r5 = r29;	 Catch:{ Exception -> 0x04d8 }
                r3.getUsersInternal(r4, r5);	 Catch:{ Exception -> 0x04d8 }
                goto L_0x0394;	 Catch:{ Exception -> 0x04d8 }
                r5 = r29;	 Catch:{ Exception -> 0x04d8 }
                r3 = r10.isEmpty();	 Catch:{ Exception -> 0x04d8 }
                if (r3 != 0) goto L_0x0498;	 Catch:{ Exception -> 0x04d8 }
                r3 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x04d8 }
                r4 = ",";	 Catch:{ Exception -> 0x04d8 }
                r4 = android.text.TextUtils.join(r4, r10);	 Catch:{ Exception -> 0x04d8 }
                r6 = r30;	 Catch:{ Exception -> 0x04d8 }
                r3.getChatsInternal(r4, r6);	 Catch:{ Exception -> 0x04d8 }
                r3 = 0;	 Catch:{ Exception -> 0x04d8 }
                r4 = r6.size();	 Catch:{ Exception -> 0x04d8 }
                if (r3 >= r4) goto L_0x0489;	 Catch:{ Exception -> 0x04d8 }
                r4 = r6.get(r3);	 Catch:{ Exception -> 0x04d8 }
                r4 = (org.telegram.tgnet.TLRPC.Chat) r4;	 Catch:{ Exception -> 0x04d8 }
                if (r4 == 0) goto L_0x0464;	 Catch:{ Exception -> 0x04d8 }
                r13 = r4.left;	 Catch:{ Exception -> 0x04d8 }
                if (r13 != 0) goto L_0x03d1;	 Catch:{ Exception -> 0x04d8 }
                r13 = r4.migrated_to;	 Catch:{ Exception -> 0x04d8 }
                if (r13 == 0) goto L_0x03bf;	 Catch:{ Exception -> 0x04d8 }
                goto L_0x03d1;	 Catch:{ Exception -> 0x04d8 }
                r35 = r1;	 Catch:{ Exception -> 0x04d8 }
                r36 = r2;	 Catch:{ Exception -> 0x04d8 }
                r37 = r5;	 Catch:{ Exception -> 0x04d8 }
                r40 = r6;	 Catch:{ Exception -> 0x04d8 }
                r38 = r7;	 Catch:{ Exception -> 0x04d8 }
                r39 = r9;	 Catch:{ Exception -> 0x04d8 }
                r7 = r32;	 Catch:{ Exception -> 0x04d8 }
                r16 = 0;	 Catch:{ Exception -> 0x04d8 }
                goto L_0x0474;	 Catch:{ Exception -> 0x04d8 }
                r13 = r4.id;	 Catch:{ Exception -> 0x04d8 }
                r13 = -r13;	 Catch:{ Exception -> 0x04d8 }
                r13 = (long) r13;	 Catch:{ Exception -> 0x04d8 }
                r35 = r1;	 Catch:{ Exception -> 0x04d8 }
                r1 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x04d8 }
                r1 = r1.database;	 Catch:{ Exception -> 0x04d8 }
                r36 = r2;	 Catch:{ Exception -> 0x04d8 }
                r2 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x04d8 }
                r2.<init>();	 Catch:{ Exception -> 0x04d8 }
                r37 = r5;	 Catch:{ Exception -> 0x04d8 }
                r5 = "UPDATE dialogs SET unread_count = 0 WHERE did = ";	 Catch:{ Exception -> 0x04d8 }
                r2.append(r5);	 Catch:{ Exception -> 0x04d8 }
                r2.append(r13);	 Catch:{ Exception -> 0x04d8 }
                r2 = r2.toString();	 Catch:{ Exception -> 0x04d8 }
                r1 = r1.executeFast(r2);	 Catch:{ Exception -> 0x04d8 }
                r1 = r1.stepThis();	 Catch:{ Exception -> 0x04d8 }
                r1.dispose();	 Catch:{ Exception -> 0x04d8 }
                r1 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x04d8 }
                r1 = r1.database;	 Catch:{ Exception -> 0x04d8 }
                r2 = java.util.Locale.US;	 Catch:{ Exception -> 0x04d8 }
                r5 = "UPDATE messages SET read_state = 3 WHERE uid = %d AND mid > 0 AND read_state IN(0,2) AND out = 0";	 Catch:{ Exception -> 0x04d8 }
                r38 = r7;	 Catch:{ Exception -> 0x04d8 }
                r39 = r9;	 Catch:{ Exception -> 0x04d8 }
                r7 = 1;	 Catch:{ Exception -> 0x04d8 }
                r9 = new java.lang.Object[r7];	 Catch:{ Exception -> 0x04d8 }
                r7 = java.lang.Long.valueOf(r13);	 Catch:{ Exception -> 0x04d8 }
                r16 = 0;	 Catch:{ Exception -> 0x04d8 }
                r9[r16] = r7;	 Catch:{ Exception -> 0x04d8 }
                r2 = java.lang.String.format(r2, r5, r9);	 Catch:{ Exception -> 0x04d8 }
                r1 = r1.executeFast(r2);	 Catch:{ Exception -> 0x04d8 }
                r1 = r1.stepThis();	 Catch:{ Exception -> 0x04d8 }
                r1.dispose();	 Catch:{ Exception -> 0x04d8 }
                r6.remove(r3);	 Catch:{ Exception -> 0x04d8 }
                r3 = r3 + -1;	 Catch:{ Exception -> 0x04d8 }
                r1 = r4.id;	 Catch:{ Exception -> 0x04d8 }
                r1 = -r1;	 Catch:{ Exception -> 0x04d8 }
                r1 = (long) r1;	 Catch:{ Exception -> 0x04d8 }
                r12.remove(r1);	 Catch:{ Exception -> 0x04d8 }
                r1 = r16;	 Catch:{ Exception -> 0x04d8 }
                r7 = r32;	 Catch:{ Exception -> 0x04d8 }
                r2 = r7.size();	 Catch:{ Exception -> 0x04d8 }
                if (r1 >= r2) goto L_0x045f;	 Catch:{ Exception -> 0x04d8 }
                r2 = r7.get(r1);	 Catch:{ Exception -> 0x04d8 }
                r2 = (org.telegram.tgnet.TLRPC.Message) r2;	 Catch:{ Exception -> 0x04d8 }
                r40 = r6;	 Catch:{ Exception -> 0x04d8 }
                r5 = r2.dialog_id;	 Catch:{ Exception -> 0x04d8 }
                r9 = r4.id;	 Catch:{ Exception -> 0x04d8 }
                r9 = -r9;	 Catch:{ Exception -> 0x04d8 }
                r42 = r2;	 Catch:{ Exception -> 0x04d8 }
                r41 = r3;	 Catch:{ Exception -> 0x04d8 }
                r2 = (long) r9;	 Catch:{ Exception -> 0x04d8 }
                r9 = (r5 > r2 ? 1 : (r5 == r2 ? 0 : -1));	 Catch:{ Exception -> 0x04d8 }
                if (r9 != 0) goto L_0x0456;	 Catch:{ Exception -> 0x04d8 }
                r7.remove(r1);	 Catch:{ Exception -> 0x04d8 }
                r1 = r1 + -1;	 Catch:{ Exception -> 0x04d8 }
                r2 = 1;	 Catch:{ Exception -> 0x04d8 }
                r1 = r1 + r2;	 Catch:{ Exception -> 0x04d8 }
                r32 = r7;	 Catch:{ Exception -> 0x04d8 }
                r6 = r40;	 Catch:{ Exception -> 0x04d8 }
                r3 = r41;	 Catch:{ Exception -> 0x04d8 }
                goto L_0x0433;	 Catch:{ Exception -> 0x04d8 }
                r41 = r3;	 Catch:{ Exception -> 0x04d8 }
                r40 = r6;	 Catch:{ Exception -> 0x04d8 }
                goto L_0x0476;	 Catch:{ Exception -> 0x04d8 }
                r35 = r1;	 Catch:{ Exception -> 0x04d8 }
                r36 = r2;	 Catch:{ Exception -> 0x04d8 }
                r37 = r5;	 Catch:{ Exception -> 0x04d8 }
                r40 = r6;	 Catch:{ Exception -> 0x04d8 }
                r38 = r7;	 Catch:{ Exception -> 0x04d8 }
                r39 = r9;	 Catch:{ Exception -> 0x04d8 }
                r7 = r32;	 Catch:{ Exception -> 0x04d8 }
                r16 = 0;	 Catch:{ Exception -> 0x04d8 }
                r41 = r3;	 Catch:{ Exception -> 0x04d8 }
                r1 = 1;	 Catch:{ Exception -> 0x04d8 }
                r3 = r41 + 1;	 Catch:{ Exception -> 0x04d8 }
                r32 = r7;	 Catch:{ Exception -> 0x04d8 }
                r1 = r35;	 Catch:{ Exception -> 0x04d8 }
                r2 = r36;	 Catch:{ Exception -> 0x04d8 }
                r5 = r37;	 Catch:{ Exception -> 0x04d8 }
                r7 = r38;	 Catch:{ Exception -> 0x04d8 }
                r9 = r39;	 Catch:{ Exception -> 0x04d8 }
                r6 = r40;	 Catch:{ Exception -> 0x04d8 }
                goto L_0x03a8;	 Catch:{ Exception -> 0x04d8 }
                r35 = r1;	 Catch:{ Exception -> 0x04d8 }
                r36 = r2;	 Catch:{ Exception -> 0x04d8 }
                r37 = r5;	 Catch:{ Exception -> 0x04d8 }
                r40 = r6;	 Catch:{ Exception -> 0x04d8 }
                r38 = r7;	 Catch:{ Exception -> 0x04d8 }
                r39 = r9;	 Catch:{ Exception -> 0x04d8 }
                r7 = r32;	 Catch:{ Exception -> 0x04d8 }
                goto L_0x04a6;	 Catch:{ Exception -> 0x04d8 }
                r35 = r1;	 Catch:{ Exception -> 0x04d8 }
                r36 = r2;	 Catch:{ Exception -> 0x04d8 }
                r37 = r5;	 Catch:{ Exception -> 0x04d8 }
                r38 = r7;	 Catch:{ Exception -> 0x04d8 }
                r39 = r9;	 Catch:{ Exception -> 0x04d8 }
                r40 = r30;	 Catch:{ Exception -> 0x04d8 }
                r7 = r32;	 Catch:{ Exception -> 0x04d8 }
                r25 = r35;	 Catch:{ Exception -> 0x04d8 }
                goto L_0x04ba;	 Catch:{ Exception -> 0x04d8 }
                r25 = r1;	 Catch:{ Exception -> 0x04d8 }
                r36 = r2;	 Catch:{ Exception -> 0x04d8 }
                r40 = r3;	 Catch:{ Exception -> 0x04d8 }
                r37 = r4;	 Catch:{ Exception -> 0x04d8 }
                r38 = r7;	 Catch:{ Exception -> 0x04d8 }
                r39 = r9;	 Catch:{ Exception -> 0x04d8 }
                r27 = r13;	 Catch:{ Exception -> 0x04d8 }
                r26 = r14;	 Catch:{ Exception -> 0x04d8 }
                r7 = r6;	 Catch:{ Exception -> 0x04d8 }
                java.util.Collections.reverse(r7);	 Catch:{ Exception -> 0x04d8 }
                r9 = new org.telegram.messenger.MessagesStorage$12$1;	 Catch:{ Exception -> 0x04d8 }
                r1 = r9;	 Catch:{ Exception -> 0x04d8 }
                r13 = r36;	 Catch:{ Exception -> 0x04d8 }
                r2 = r8;	 Catch:{ Exception -> 0x04d8 }
                r14 = r40;	 Catch:{ Exception -> 0x04d8 }
                r3 = r12;	 Catch:{ Exception -> 0x04d8 }
                r16 = r37;	 Catch:{ Exception -> 0x04d8 }
                r4 = r7;	 Catch:{ Exception -> 0x04d8 }
                r5 = r16;	 Catch:{ Exception -> 0x04d8 }
                r17 = r7;	 Catch:{ Exception -> 0x04d8 }
                r6 = r14;	 Catch:{ Exception -> 0x04d8 }
                r18 = r38;	 Catch:{ Exception -> 0x04d8 }
                r7 = r13;	 Catch:{ Exception -> 0x04d8 }
                r1.<init>(r3, r4, r5, r6, r7);	 Catch:{ Exception -> 0x04d8 }
                org.telegram.messenger.AndroidUtilities.runOnUIThread(r9);	 Catch:{ Exception -> 0x04d8 }
                goto L_0x04dd;
            L_0x04d8:
                r0 = move-exception;
                r1 = r0;
                org.telegram.messenger.FileLog.e(r1);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.MessagesStorage.12.run():void");
            }
        });
    }

    public void putWallpapers(final ArrayList<WallPaper> wallPapers) {
        this.storageQueue.postRunnable(new Runnable() {
            public void run() {
                int num = 0;
                try {
                    MessagesStorage.this.database.executeFast("DELETE FROM wallpapers WHERE 1").stepThis().dispose();
                    MessagesStorage.this.database.beginTransaction();
                    SQLitePreparedStatement state = MessagesStorage.this.database.executeFast("REPLACE INTO wallpapers VALUES(?, ?)");
                    Iterator it = wallPapers.iterator();
                    while (it.hasNext()) {
                        WallPaper wallPaper = (WallPaper) it.next();
                        state.requery();
                        NativeByteBuffer data = new NativeByteBuffer(wallPaper.getObjectSize());
                        wallPaper.serializeToStream(data);
                        state.bindInteger(1, num);
                        state.bindByteBuffer(2, data);
                        state.step();
                        num++;
                        data.reuse();
                    }
                    state.dispose();
                    MessagesStorage.this.database.commitTransaction();
                } catch (Throwable e) {
                    FileLog.e(e);
                }
            }
        });
    }

    public void loadWebRecent(final int type) {
        this.storageQueue.postRunnable(new Runnable() {
            public void run() {
                try {
                    SQLiteCursor cursor = MessagesStorage.this.database;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("SELECT id, image_url, thumb_url, local_url, width, height, size, date, document FROM web_recent_v3 WHERE type = ");
                    stringBuilder.append(type);
                    stringBuilder.append(" ORDER BY date DESC");
                    cursor = cursor.queryFinalized(stringBuilder.toString(), new Object[0]);
                    final ArrayList<SearchImage> arrayList = new ArrayList();
                    while (cursor.next()) {
                        SearchImage searchImage = new SearchImage();
                        searchImage.id = cursor.stringValue(0);
                        searchImage.imageUrl = cursor.stringValue(1);
                        searchImage.thumbUrl = cursor.stringValue(2);
                        searchImage.localUrl = cursor.stringValue(3);
                        searchImage.width = cursor.intValue(4);
                        searchImage.height = cursor.intValue(5);
                        searchImage.size = cursor.intValue(6);
                        searchImage.date = cursor.intValue(7);
                        if (!cursor.isNull(8)) {
                            NativeByteBuffer data = cursor.byteBufferValue(8);
                            if (data != null) {
                                searchImage.document = Document.TLdeserialize(data, data.readInt32(false), false);
                                data.reuse();
                            }
                        }
                        searchImage.type = type;
                        arrayList.add(searchImage);
                    }
                    cursor.dispose();
                    AndroidUtilities.runOnUIThread(new Runnable() {
                        public void run() {
                            NotificationCenter.getInstance(MessagesStorage.this.currentAccount).postNotificationName(NotificationCenter.recentImagesDidLoaded, Integer.valueOf(type), arrayList);
                        }
                    });
                } catch (Throwable e) {
                    FileLog.e(e);
                }
            }
        });
    }

    public void addRecentLocalFile(final String imageUrl, final String localUrl, final Document document) {
        if (!(imageUrl == null || imageUrl.length() == 0)) {
            if ((localUrl != null && localUrl.length() != 0) || document != null) {
                this.storageQueue.postRunnable(new Runnable() {
                    public void run() {
                        try {
                            SQLitePreparedStatement state;
                            if (document != null) {
                                state = MessagesStorage.this.database.executeFast("UPDATE web_recent_v3 SET document = ? WHERE image_url = ?");
                                state.requery();
                                NativeByteBuffer data = new NativeByteBuffer(document.getObjectSize());
                                document.serializeToStream(data);
                                state.bindByteBuffer(1, data);
                                state.bindString(2, imageUrl);
                                state.step();
                                state.dispose();
                                data.reuse();
                            } else {
                                state = MessagesStorage.this.database.executeFast("UPDATE web_recent_v3 SET local_url = ? WHERE image_url = ?");
                                state.requery();
                                state.bindString(1, localUrl);
                                state.bindString(2, imageUrl);
                                state.step();
                                state.dispose();
                            }
                        } catch (Throwable e) {
                            FileLog.e(e);
                        }
                    }
                });
            }
        }
    }

    public void clearWebRecent(final int type) {
        this.storageQueue.postRunnable(new Runnable() {
            public void run() {
                try {
                    SQLiteDatabase access$000 = MessagesStorage.this.database;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("DELETE FROM web_recent_v3 WHERE type = ");
                    stringBuilder.append(type);
                    access$000.executeFast(stringBuilder.toString()).stepThis().dispose();
                } catch (Throwable e) {
                    FileLog.e(e);
                }
            }
        });
    }

    public void putWebRecent(final ArrayList<SearchImage> arrayList) {
        this.storageQueue.postRunnable(new Runnable() {
            public void run() {
                try {
                    int a;
                    MessagesStorage.this.database.beginTransaction();
                    SQLitePreparedStatement state = MessagesStorage.this.database.executeFast("REPLACE INTO web_recent_v3 VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
                    int a2 = 0;
                    while (true) {
                        int size = arrayList.size();
                        a = Callback.DEFAULT_DRAG_ANIMATION_DURATION;
                        if (a2 >= size) {
                            break;
                        } else if (a2 == Callback.DEFAULT_DRAG_ANIMATION_DURATION) {
                            break;
                        } else {
                            SearchImage searchImage = (SearchImage) arrayList.get(a2);
                            state.requery();
                            state.bindString(1, searchImage.id);
                            state.bindInteger(2, searchImage.type);
                            state.bindString(3, searchImage.imageUrl != null ? searchImage.imageUrl : TtmlNode.ANONYMOUS_REGION_ID);
                            state.bindString(4, searchImage.thumbUrl != null ? searchImage.thumbUrl : TtmlNode.ANONYMOUS_REGION_ID);
                            state.bindString(5, searchImage.localUrl != null ? searchImage.localUrl : TtmlNode.ANONYMOUS_REGION_ID);
                            state.bindInteger(6, searchImage.width);
                            state.bindInteger(7, searchImage.height);
                            state.bindInteger(8, searchImage.size);
                            state.bindInteger(9, searchImage.date);
                            NativeByteBuffer data = null;
                            if (searchImage.document != null) {
                                data = new NativeByteBuffer(searchImage.document.getObjectSize());
                                searchImage.document.serializeToStream(data);
                                state.bindByteBuffer(10, data);
                            } else {
                                state.bindNull(10);
                            }
                            state.step();
                            if (data != null) {
                                data.reuse();
                            }
                            a2++;
                        }
                    }
                    state.dispose();
                    MessagesStorage.this.database.commitTransaction();
                    if (arrayList.size() >= Callback.DEFAULT_DRAG_ANIMATION_DURATION) {
                        MessagesStorage.this.database.beginTransaction();
                        while (true) {
                            a2 = a;
                            if (a2 >= arrayList.size()) {
                                break;
                            }
                            SQLiteDatabase access$000 = MessagesStorage.this.database;
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append("DELETE FROM web_recent_v3 WHERE id = '");
                            stringBuilder.append(((SearchImage) arrayList.get(a2)).id);
                            stringBuilder.append("'");
                            access$000.executeFast(stringBuilder.toString()).stepThis().dispose();
                            a = a2 + 1;
                        }
                        MessagesStorage.this.database.commitTransaction();
                    }
                } catch (Throwable e) {
                    FileLog.e(e);
                }
            }
        });
    }

    public void getWallpapers() {
        this.storageQueue.postRunnable(new Runnable() {
            public void run() {
                try {
                    SQLiteCursor cursor = MessagesStorage.this.database.queryFinalized("SELECT data FROM wallpapers WHERE 1", new Object[0]);
                    final ArrayList<WallPaper> wallPapers = new ArrayList();
                    while (cursor.next()) {
                        NativeByteBuffer data = cursor.byteBufferValue(0);
                        if (data != null) {
                            WallPaper wallPaper = WallPaper.TLdeserialize(data, data.readInt32(false), false);
                            data.reuse();
                            wallPapers.add(wallPaper);
                        }
                    }
                    cursor.dispose();
                    AndroidUtilities.runOnUIThread(new Runnable() {
                        public void run() {
                            NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.wallpapersDidLoaded, wallPapers);
                        }
                    });
                } catch (Throwable e) {
                    FileLog.e(e);
                }
            }
        });
    }

    public void getBlockedUsers() {
        this.storageQueue.postRunnable(new Runnable() {
            public void run() {
                try {
                    ArrayList<Integer> ids = new ArrayList();
                    ArrayList<User> users = new ArrayList();
                    SQLiteCursor cursor = MessagesStorage.this.database.queryFinalized("SELECT * FROM blocked_users WHERE 1", new Object[0]);
                    StringBuilder usersToLoad = new StringBuilder();
                    while (cursor.next()) {
                        int user_id = cursor.intValue(0);
                        ids.add(Integer.valueOf(user_id));
                        if (usersToLoad.length() != 0) {
                            usersToLoad.append(",");
                        }
                        usersToLoad.append(user_id);
                    }
                    cursor.dispose();
                    if (usersToLoad.length() != 0) {
                        MessagesStorage.this.getUsersInternal(usersToLoad.toString(), users);
                    }
                    MessagesController.getInstance(MessagesStorage.this.currentAccount).processLoadedBlockedUsers(ids, users, true);
                } catch (Throwable e) {
                    FileLog.e(e);
                }
            }
        });
    }

    public void deleteBlockedUser(final int id) {
        this.storageQueue.postRunnable(new Runnable() {
            public void run() {
                try {
                    SQLiteDatabase access$000 = MessagesStorage.this.database;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("DELETE FROM blocked_users WHERE uid = ");
                    stringBuilder.append(id);
                    access$000.executeFast(stringBuilder.toString()).stepThis().dispose();
                } catch (Throwable e) {
                    FileLog.e(e);
                }
            }
        });
    }

    public void putBlockedUsers(final ArrayList<Integer> ids, final boolean replace) {
        if (ids != null) {
            if (!ids.isEmpty()) {
                this.storageQueue.postRunnable(new Runnable() {
                    public void run() {
                        try {
                            if (replace) {
                                MessagesStorage.this.database.executeFast("DELETE FROM blocked_users WHERE 1").stepThis().dispose();
                            }
                            MessagesStorage.this.database.beginTransaction();
                            SQLitePreparedStatement state = MessagesStorage.this.database.executeFast("REPLACE INTO blocked_users VALUES(?)");
                            Iterator it = ids.iterator();
                            while (it.hasNext()) {
                                Integer id = (Integer) it.next();
                                state.requery();
                                state.bindInteger(1, id.intValue());
                                state.step();
                            }
                            state.dispose();
                            MessagesStorage.this.database.commitTransaction();
                        } catch (Throwable e) {
                            FileLog.e(e);
                        }
                    }
                });
            }
        }
    }

    public void deleteUserChannelHistory(final int channelId, final int uid) {
        this.storageQueue.postRunnable(new Runnable() {
            public void run() {
                try {
                    long did = (long) (-channelId);
                    final ArrayList<Integer> mids = new ArrayList();
                    SQLiteCursor cursor = MessagesStorage.this.database;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("SELECT data FROM messages WHERE uid = ");
                    stringBuilder.append(did);
                    cursor = cursor.queryFinalized(stringBuilder.toString(), new Object[0]);
                    ArrayList<File> filesToDelete = new ArrayList();
                    while (cursor.next()) {
                        try {
                            NativeByteBuffer data = cursor.byteBufferValue(0);
                            if (data != null) {
                                Message message = Message.TLdeserialize(data, data.readInt32(false), false);
                                message.readAttachPath(data, UserConfig.getInstance(MessagesStorage.this.currentAccount).clientUserId);
                                data.reuse();
                                if (!(message == null || message.from_id != uid || message.id == 1)) {
                                    mids.add(Integer.valueOf(message.id));
                                    if (message.media instanceof TL_messageMediaPhoto) {
                                        Iterator it = message.media.photo.sizes.iterator();
                                        while (it.hasNext()) {
                                            File file = FileLoader.getPathToAttach((PhotoSize) it.next());
                                            if (file != null && file.toString().length() > 0) {
                                                filesToDelete.add(file);
                                            }
                                        }
                                    } else if (message.media instanceof TL_messageMediaDocument) {
                                        File file2 = FileLoader.getPathToAttach(message.media.document);
                                        if (file2 != null && file2.toString().length() > 0) {
                                            filesToDelete.add(file2);
                                        }
                                        file2 = FileLoader.getPathToAttach(message.media.document.thumb);
                                        if (file2 != null && file2.toString().length() > 0) {
                                            filesToDelete.add(file2);
                                        }
                                    }
                                }
                            }
                        } catch (Throwable e) {
                            FileLog.e(e);
                        }
                    }
                    cursor.dispose();
                    AndroidUtilities.runOnUIThread(new Runnable() {
                        public void run() {
                            MessagesController.getInstance(MessagesStorage.this.currentAccount).markChannelDialogMessageAsDeleted(mids, channelId);
                        }
                    });
                    MessagesStorage.this.markMessagesAsDeletedInternal((ArrayList) mids, channelId);
                    MessagesStorage.this.updateDialogsWithDeletedMessagesInternal(mids, null, channelId);
                    FileLoader.getInstance(MessagesStorage.this.currentAccount).deleteFiles(filesToDelete, 0);
                    if (!mids.isEmpty()) {
                        AndroidUtilities.runOnUIThread(new Runnable() {
                            public void run() {
                                NotificationCenter.getInstance(MessagesStorage.this.currentAccount).postNotificationName(NotificationCenter.messagesDeleted, mids, Integer.valueOf(channelId));
                            }
                        });
                    }
                } catch (Throwable e2) {
                    FileLog.e(e2);
                }
            }
        });
    }

    public void deleteDialog(final long did, final int messagesOnly) {
        this.storageQueue.postRunnable(new Runnable() {
            public void run() {
                /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.MessagesStorage.23.run():void
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:116)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:249)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:256)
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
                r0 = r5;	 Catch:{ Exception -> 0x04f4 }
                r1 = 3;	 Catch:{ Exception -> 0x04f4 }
                r2 = 0;	 Catch:{ Exception -> 0x04f4 }
                if (r0 != r1) goto L_0x0037;	 Catch:{ Exception -> 0x04f4 }
            L_0x0006:
                r0 = -1;	 Catch:{ Exception -> 0x04f4 }
                r3 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x04f4 }
                r3 = r3.database;	 Catch:{ Exception -> 0x04f4 }
                r4 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x04f4 }
                r4.<init>();	 Catch:{ Exception -> 0x04f4 }
                r5 = "SELECT last_mid FROM dialogs WHERE did = ";	 Catch:{ Exception -> 0x04f4 }
                r4.append(r5);	 Catch:{ Exception -> 0x04f4 }
                r5 = r3;	 Catch:{ Exception -> 0x04f4 }
                r4.append(r5);	 Catch:{ Exception -> 0x04f4 }
                r4 = r4.toString();	 Catch:{ Exception -> 0x04f4 }
                r5 = new java.lang.Object[r2];	 Catch:{ Exception -> 0x04f4 }
                r3 = r3.queryFinalized(r4, r5);	 Catch:{ Exception -> 0x04f4 }
                r4 = r3.next();	 Catch:{ Exception -> 0x04f4 }
                if (r4 == 0) goto L_0x0031;	 Catch:{ Exception -> 0x04f4 }
            L_0x002c:
                r4 = r3.intValue(r2);	 Catch:{ Exception -> 0x04f4 }
                r0 = r4;	 Catch:{ Exception -> 0x04f4 }
            L_0x0031:
                r3.dispose();	 Catch:{ Exception -> 0x04f4 }
                if (r0 == 0) goto L_0x0037;	 Catch:{ Exception -> 0x04f4 }
            L_0x0036:
                return;	 Catch:{ Exception -> 0x04f4 }
            L_0x0037:
                r3 = r3;	 Catch:{ Exception -> 0x04f4 }
                r0 = (int) r3;	 Catch:{ Exception -> 0x04f4 }
                r3 = 2;	 Catch:{ Exception -> 0x04f4 }
                if (r0 == 0) goto L_0x0041;	 Catch:{ Exception -> 0x04f4 }
            L_0x003d:
                r0 = r5;	 Catch:{ Exception -> 0x04f4 }
                if (r0 != r3) goto L_0x0111;	 Catch:{ Exception -> 0x04f4 }
            L_0x0041:
                r0 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x04f4 }
                r0 = r0.database;	 Catch:{ Exception -> 0x04f4 }
                r4 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x04f4 }
                r4.<init>();	 Catch:{ Exception -> 0x04f4 }
                r5 = "SELECT data FROM messages WHERE uid = ";	 Catch:{ Exception -> 0x04f4 }
                r4.append(r5);	 Catch:{ Exception -> 0x04f4 }
                r5 = r3;	 Catch:{ Exception -> 0x04f4 }
                r4.append(r5);	 Catch:{ Exception -> 0x04f4 }
                r4 = r4.toString();	 Catch:{ Exception -> 0x04f4 }
                r5 = new java.lang.Object[r2];	 Catch:{ Exception -> 0x04f4 }
                r0 = r0.queryFinalized(r4, r5);	 Catch:{ Exception -> 0x04f4 }
                r4 = new java.util.ArrayList;	 Catch:{ Exception -> 0x04f4 }
                r4.<init>();	 Catch:{ Exception -> 0x04f4 }
            L_0x0065:
                r5 = r0.next();	 Catch:{ Exception -> 0x00fb }
                if (r5 == 0) goto L_0x00fa;	 Catch:{ Exception -> 0x00fb }
            L_0x006b:
                r5 = r0.byteBufferValue(r2);	 Catch:{ Exception -> 0x00fb }
                if (r5 == 0) goto L_0x00f8;	 Catch:{ Exception -> 0x00fb }
            L_0x0071:
                r6 = r5.readInt32(r2);	 Catch:{ Exception -> 0x00fb }
                r6 = org.telegram.tgnet.TLRPC.Message.TLdeserialize(r5, r6, r2);	 Catch:{ Exception -> 0x00fb }
                r7 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x00fb }
                r7 = r7.currentAccount;	 Catch:{ Exception -> 0x00fb }
                r7 = org.telegram.messenger.UserConfig.getInstance(r7);	 Catch:{ Exception -> 0x00fb }
                r7 = r7.clientUserId;	 Catch:{ Exception -> 0x00fb }
                r6.readAttachPath(r5, r7);	 Catch:{ Exception -> 0x00fb }
                r5.reuse();	 Catch:{ Exception -> 0x00fb }
                if (r6 == 0) goto L_0x00f8;	 Catch:{ Exception -> 0x00fb }
            L_0x008d:
                r7 = r6.media;	 Catch:{ Exception -> 0x00fb }
                if (r7 == 0) goto L_0x00f8;	 Catch:{ Exception -> 0x00fb }
            L_0x0091:
                r7 = r6.media;	 Catch:{ Exception -> 0x00fb }
                r7 = r7 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaPhoto;	 Catch:{ Exception -> 0x00fb }
                if (r7 == 0) goto L_0x00c1;	 Catch:{ Exception -> 0x00fb }
            L_0x0097:
                r7 = r6.media;	 Catch:{ Exception -> 0x00fb }
                r7 = r7.photo;	 Catch:{ Exception -> 0x00fb }
                r7 = r7.sizes;	 Catch:{ Exception -> 0x00fb }
                r7 = r7.iterator();	 Catch:{ Exception -> 0x00fb }
            L_0x00a1:
                r8 = r7.hasNext();	 Catch:{ Exception -> 0x00fb }
                if (r8 == 0) goto L_0x00f8;	 Catch:{ Exception -> 0x00fb }
            L_0x00a7:
                r8 = r7.next();	 Catch:{ Exception -> 0x00fb }
                r8 = (org.telegram.tgnet.TLRPC.PhotoSize) r8;	 Catch:{ Exception -> 0x00fb }
                r9 = org.telegram.messenger.FileLoader.getPathToAttach(r8);	 Catch:{ Exception -> 0x00fb }
                if (r9 == 0) goto L_0x00c0;	 Catch:{ Exception -> 0x00fb }
            L_0x00b3:
                r10 = r9.toString();	 Catch:{ Exception -> 0x00fb }
                r10 = r10.length();	 Catch:{ Exception -> 0x00fb }
                if (r10 <= 0) goto L_0x00c0;	 Catch:{ Exception -> 0x00fb }
            L_0x00bd:
                r4.add(r9);	 Catch:{ Exception -> 0x00fb }
            L_0x00c0:
                goto L_0x00a1;	 Catch:{ Exception -> 0x00fb }
            L_0x00c1:
                r7 = r6.media;	 Catch:{ Exception -> 0x00fb }
                r7 = r7 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaDocument;	 Catch:{ Exception -> 0x00fb }
                if (r7 == 0) goto L_0x00f8;	 Catch:{ Exception -> 0x00fb }
            L_0x00c7:
                r7 = r6.media;	 Catch:{ Exception -> 0x00fb }
                r7 = r7.document;	 Catch:{ Exception -> 0x00fb }
                r7 = org.telegram.messenger.FileLoader.getPathToAttach(r7);	 Catch:{ Exception -> 0x00fb }
                if (r7 == 0) goto L_0x00de;	 Catch:{ Exception -> 0x00fb }
            L_0x00d1:
                r8 = r7.toString();	 Catch:{ Exception -> 0x00fb }
                r8 = r8.length();	 Catch:{ Exception -> 0x00fb }
                if (r8 <= 0) goto L_0x00de;	 Catch:{ Exception -> 0x00fb }
            L_0x00db:
                r4.add(r7);	 Catch:{ Exception -> 0x00fb }
            L_0x00de:
                r8 = r6.media;	 Catch:{ Exception -> 0x00fb }
                r8 = r8.document;	 Catch:{ Exception -> 0x00fb }
                r8 = r8.thumb;	 Catch:{ Exception -> 0x00fb }
                r8 = org.telegram.messenger.FileLoader.getPathToAttach(r8);	 Catch:{ Exception -> 0x00fb }
                r7 = r8;	 Catch:{ Exception -> 0x00fb }
                if (r7 == 0) goto L_0x00f8;	 Catch:{ Exception -> 0x00fb }
            L_0x00eb:
                r8 = r7.toString();	 Catch:{ Exception -> 0x00fb }
                r8 = r8.length();	 Catch:{ Exception -> 0x00fb }
                if (r8 <= 0) goto L_0x00f8;	 Catch:{ Exception -> 0x00fb }
            L_0x00f5:
                r4.add(r7);	 Catch:{ Exception -> 0x00fb }
            L_0x00f8:
                goto L_0x0065;
            L_0x00fa:
                goto L_0x00ff;
            L_0x00fb:
                r5 = move-exception;
                org.telegram.messenger.FileLog.e(r5);	 Catch:{ Exception -> 0x04f4 }
            L_0x00ff:
                r0.dispose();	 Catch:{ Exception -> 0x04f4 }
                r5 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x04f4 }
                r5 = r5.currentAccount;	 Catch:{ Exception -> 0x04f4 }
                r5 = org.telegram.messenger.FileLoader.getInstance(r5);	 Catch:{ Exception -> 0x04f4 }
                r6 = r5;	 Catch:{ Exception -> 0x04f4 }
                r5.deleteFiles(r4, r6);	 Catch:{ Exception -> 0x04f4 }
            L_0x0111:
                r0 = r5;	 Catch:{ Exception -> 0x04f4 }
                r4 = 0;	 Catch:{ Exception -> 0x04f4 }
                r5 = 1;	 Catch:{ Exception -> 0x04f4 }
                if (r0 == 0) goto L_0x02d8;	 Catch:{ Exception -> 0x04f4 }
            L_0x0117:
                r0 = r5;	 Catch:{ Exception -> 0x04f4 }
                if (r0 != r1) goto L_0x011d;	 Catch:{ Exception -> 0x04f4 }
            L_0x011b:
                goto L_0x02d8;	 Catch:{ Exception -> 0x04f4 }
            L_0x011d:
                r0 = r5;	 Catch:{ Exception -> 0x04f4 }
                if (r0 != r3) goto L_0x03e0;	 Catch:{ Exception -> 0x04f4 }
            L_0x0121:
                r0 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x04f4 }
                r0 = r0.database;	 Catch:{ Exception -> 0x04f4 }
                r1 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x04f4 }
                r1.<init>();	 Catch:{ Exception -> 0x04f4 }
                r3 = "SELECT last_mid_i, last_mid FROM dialogs WHERE did = ";	 Catch:{ Exception -> 0x04f4 }
                r1.append(r3);	 Catch:{ Exception -> 0x04f4 }
                r6 = r3;	 Catch:{ Exception -> 0x04f4 }
                r1.append(r6);	 Catch:{ Exception -> 0x04f4 }
                r1 = r1.toString();	 Catch:{ Exception -> 0x04f4 }
                r3 = new java.lang.Object[r2];	 Catch:{ Exception -> 0x04f4 }
                r0 = r0.queryFinalized(r1, r3);	 Catch:{ Exception -> 0x04f4 }
                r1 = -1;	 Catch:{ Exception -> 0x04f4 }
                r3 = r0.next();	 Catch:{ Exception -> 0x04f4 }
                if (r3 == 0) goto L_0x02d4;	 Catch:{ Exception -> 0x04f4 }
            L_0x0147:
                r6 = r0.longValue(r2);	 Catch:{ Exception -> 0x04f4 }
                r8 = r0.longValue(r5);	 Catch:{ Exception -> 0x04f4 }
                r3 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x04f4 }
                r3 = r3.database;	 Catch:{ Exception -> 0x04f4 }
                r5 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x04f4 }
                r5.<init>();	 Catch:{ Exception -> 0x04f4 }
                r10 = "SELECT data FROM messages WHERE uid = ";	 Catch:{ Exception -> 0x04f4 }
                r5.append(r10);	 Catch:{ Exception -> 0x04f4 }
                r10 = r3;	 Catch:{ Exception -> 0x04f4 }
                r5.append(r10);	 Catch:{ Exception -> 0x04f4 }
                r10 = " AND mid IN (";	 Catch:{ Exception -> 0x04f4 }
                r5.append(r10);	 Catch:{ Exception -> 0x04f4 }
                r5.append(r6);	 Catch:{ Exception -> 0x04f4 }
                r10 = ",";	 Catch:{ Exception -> 0x04f4 }
                r5.append(r10);	 Catch:{ Exception -> 0x04f4 }
                r5.append(r8);	 Catch:{ Exception -> 0x04f4 }
                r10 = ")";	 Catch:{ Exception -> 0x04f4 }
                r5.append(r10);	 Catch:{ Exception -> 0x04f4 }
                r5 = r5.toString();	 Catch:{ Exception -> 0x04f4 }
                r10 = new java.lang.Object[r2];	 Catch:{ Exception -> 0x04f4 }
                r3 = r3.queryFinalized(r5, r10);	 Catch:{ Exception -> 0x04f4 }
            L_0x0183:
                r5 = r3.next();	 Catch:{ Exception -> 0x01b0 }
                if (r5 == 0) goto L_0x01af;	 Catch:{ Exception -> 0x01b0 }
            L_0x0189:
                r5 = r3.byteBufferValue(r2);	 Catch:{ Exception -> 0x01b0 }
                if (r5 == 0) goto L_0x01ae;	 Catch:{ Exception -> 0x01b0 }
            L_0x018f:
                r10 = r5.readInt32(r2);	 Catch:{ Exception -> 0x01b0 }
                r10 = org.telegram.tgnet.TLRPC.Message.TLdeserialize(r5, r10, r2);	 Catch:{ Exception -> 0x01b0 }
                r11 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x01b0 }
                r11 = r11.currentAccount;	 Catch:{ Exception -> 0x01b0 }
                r11 = org.telegram.messenger.UserConfig.getInstance(r11);	 Catch:{ Exception -> 0x01b0 }
                r11 = r11.clientUserId;	 Catch:{ Exception -> 0x01b0 }
                r10.readAttachPath(r5, r11);	 Catch:{ Exception -> 0x01b0 }
                r5.reuse();	 Catch:{ Exception -> 0x01b0 }
                if (r10 == 0) goto L_0x01ae;	 Catch:{ Exception -> 0x01b0 }
            L_0x01ab:
                r11 = r10.id;	 Catch:{ Exception -> 0x01b0 }
                r1 = r11;
            L_0x01ae:
                goto L_0x0183;
            L_0x01af:
                goto L_0x01b4;
            L_0x01b0:
                r2 = move-exception;
                org.telegram.messenger.FileLog.e(r2);	 Catch:{ Exception -> 0x04f4 }
            L_0x01b4:
                r3.dispose();	 Catch:{ Exception -> 0x04f4 }
                r2 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x04f4 }
                r2 = r2.database;	 Catch:{ Exception -> 0x04f4 }
                r5 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x04f4 }
                r5.<init>();	 Catch:{ Exception -> 0x04f4 }
                r10 = "DELETE FROM messages WHERE uid = ";	 Catch:{ Exception -> 0x04f4 }
                r5.append(r10);	 Catch:{ Exception -> 0x04f4 }
                r10 = r3;	 Catch:{ Exception -> 0x04f4 }
                r5.append(r10);	 Catch:{ Exception -> 0x04f4 }
                r10 = " AND mid != ";	 Catch:{ Exception -> 0x04f4 }
                r5.append(r10);	 Catch:{ Exception -> 0x04f4 }
                r5.append(r6);	 Catch:{ Exception -> 0x04f4 }
                r10 = " AND mid != ";	 Catch:{ Exception -> 0x04f4 }
                r5.append(r10);	 Catch:{ Exception -> 0x04f4 }
                r5.append(r8);	 Catch:{ Exception -> 0x04f4 }
                r5 = r5.toString();	 Catch:{ Exception -> 0x04f4 }
                r2 = r2.executeFast(r5);	 Catch:{ Exception -> 0x04f4 }
                r2 = r2.stepThis();	 Catch:{ Exception -> 0x04f4 }
                r2.dispose();	 Catch:{ Exception -> 0x04f4 }
                r2 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x04f4 }
                r2 = r2.database;	 Catch:{ Exception -> 0x04f4 }
                r5 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x04f4 }
                r5.<init>();	 Catch:{ Exception -> 0x04f4 }
                r10 = "DELETE FROM messages_holes WHERE uid = ";	 Catch:{ Exception -> 0x04f4 }
                r5.append(r10);	 Catch:{ Exception -> 0x04f4 }
                r10 = r3;	 Catch:{ Exception -> 0x04f4 }
                r5.append(r10);	 Catch:{ Exception -> 0x04f4 }
                r5 = r5.toString();	 Catch:{ Exception -> 0x04f4 }
                r2 = r2.executeFast(r5);	 Catch:{ Exception -> 0x04f4 }
                r2 = r2.stepThis();	 Catch:{ Exception -> 0x04f4 }
                r2.dispose();	 Catch:{ Exception -> 0x04f4 }
                r2 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x04f4 }
                r2 = r2.database;	 Catch:{ Exception -> 0x04f4 }
                r5 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x04f4 }
                r5.<init>();	 Catch:{ Exception -> 0x04f4 }
                r10 = "DELETE FROM bot_keyboard WHERE uid = ";	 Catch:{ Exception -> 0x04f4 }
                r5.append(r10);	 Catch:{ Exception -> 0x04f4 }
                r10 = r3;	 Catch:{ Exception -> 0x04f4 }
                r5.append(r10);	 Catch:{ Exception -> 0x04f4 }
                r5 = r5.toString();	 Catch:{ Exception -> 0x04f4 }
                r2 = r2.executeFast(r5);	 Catch:{ Exception -> 0x04f4 }
                r2 = r2.stepThis();	 Catch:{ Exception -> 0x04f4 }
                r2.dispose();	 Catch:{ Exception -> 0x04f4 }
                r2 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x04f4 }
                r2 = r2.database;	 Catch:{ Exception -> 0x04f4 }
                r5 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x04f4 }
                r5.<init>();	 Catch:{ Exception -> 0x04f4 }
                r10 = "DELETE FROM media_counts_v2 WHERE uid = ";	 Catch:{ Exception -> 0x04f4 }
                r5.append(r10);	 Catch:{ Exception -> 0x04f4 }
                r10 = r3;	 Catch:{ Exception -> 0x04f4 }
                r5.append(r10);	 Catch:{ Exception -> 0x04f4 }
                r5 = r5.toString();	 Catch:{ Exception -> 0x04f4 }
                r2 = r2.executeFast(r5);	 Catch:{ Exception -> 0x04f4 }
                r2 = r2.stepThis();	 Catch:{ Exception -> 0x04f4 }
                r2.dispose();	 Catch:{ Exception -> 0x04f4 }
                r2 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x04f4 }
                r2 = r2.database;	 Catch:{ Exception -> 0x04f4 }
                r5 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x04f4 }
                r5.<init>();	 Catch:{ Exception -> 0x04f4 }
                r10 = "DELETE FROM media_v2 WHERE uid = ";	 Catch:{ Exception -> 0x04f4 }
                r5.append(r10);	 Catch:{ Exception -> 0x04f4 }
                r10 = r3;	 Catch:{ Exception -> 0x04f4 }
                r5.append(r10);	 Catch:{ Exception -> 0x04f4 }
                r5 = r5.toString();	 Catch:{ Exception -> 0x04f4 }
                r2 = r2.executeFast(r5);	 Catch:{ Exception -> 0x04f4 }
                r2 = r2.stepThis();	 Catch:{ Exception -> 0x04f4 }
                r2.dispose();	 Catch:{ Exception -> 0x04f4 }
                r2 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x04f4 }
                r2 = r2.database;	 Catch:{ Exception -> 0x04f4 }
                r5 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x04f4 }
                r5.<init>();	 Catch:{ Exception -> 0x04f4 }
                r10 = "DELETE FROM media_holes_v2 WHERE uid = ";	 Catch:{ Exception -> 0x04f4 }
                r5.append(r10);	 Catch:{ Exception -> 0x04f4 }
                r10 = r3;	 Catch:{ Exception -> 0x04f4 }
                r5.append(r10);	 Catch:{ Exception -> 0x04f4 }
                r5 = r5.toString();	 Catch:{ Exception -> 0x04f4 }
                r2 = r2.executeFast(r5);	 Catch:{ Exception -> 0x04f4 }
                r2 = r2.stepThis();	 Catch:{ Exception -> 0x04f4 }
                r2.dispose();	 Catch:{ Exception -> 0x04f4 }
                r2 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x04f4 }
                r2 = r2.currentAccount;	 Catch:{ Exception -> 0x04f4 }
                r2 = org.telegram.messenger.DataQuery.getInstance(r2);	 Catch:{ Exception -> 0x04f4 }
                r10 = r3;	 Catch:{ Exception -> 0x04f4 }
                r2.clearBotKeyboard(r10, r4);	 Catch:{ Exception -> 0x04f4 }
                r2 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x04f4 }
                r2 = r2.database;	 Catch:{ Exception -> 0x04f4 }
                r4 = "REPLACE INTO messages_holes VALUES(?, ?, ?)";	 Catch:{ Exception -> 0x04f4 }
                r2 = r2.executeFast(r4);	 Catch:{ Exception -> 0x04f4 }
                r4 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x04f4 }
                r4 = r4.database;	 Catch:{ Exception -> 0x04f4 }
                r5 = "REPLACE INTO media_holes_v2 VALUES(?, ?, ?, ?)";	 Catch:{ Exception -> 0x04f4 }
                r4 = r4.executeFast(r5);	 Catch:{ Exception -> 0x04f4 }
                r5 = -1;	 Catch:{ Exception -> 0x04f4 }
                if (r1 == r5) goto L_0x02ce;	 Catch:{ Exception -> 0x04f4 }
            L_0x02c9:
                r10 = r3;	 Catch:{ Exception -> 0x04f4 }
                org.telegram.messenger.MessagesStorage.createFirstHoles(r10, r2, r4, r1);	 Catch:{ Exception -> 0x04f4 }
            L_0x02ce:
                r2.dispose();	 Catch:{ Exception -> 0x04f4 }
                r4.dispose();	 Catch:{ Exception -> 0x04f4 }
            L_0x02d4:
                r0.dispose();	 Catch:{ Exception -> 0x04f4 }
                return;	 Catch:{ Exception -> 0x04f4 }
            L_0x02d8:
                r0 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x04f4 }
                r0 = r0.database;	 Catch:{ Exception -> 0x04f4 }
                r1 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x04f4 }
                r1.<init>();	 Catch:{ Exception -> 0x04f4 }
                r2 = "DELETE FROM dialogs WHERE did = ";	 Catch:{ Exception -> 0x04f4 }
                r1.append(r2);	 Catch:{ Exception -> 0x04f4 }
                r2 = r3;	 Catch:{ Exception -> 0x04f4 }
                r1.append(r2);	 Catch:{ Exception -> 0x04f4 }
                r1 = r1.toString();	 Catch:{ Exception -> 0x04f4 }
                r0 = r0.executeFast(r1);	 Catch:{ Exception -> 0x04f4 }
                r0 = r0.stepThis();	 Catch:{ Exception -> 0x04f4 }
                r0.dispose();	 Catch:{ Exception -> 0x04f4 }
                r0 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x04f4 }
                r0 = r0.database;	 Catch:{ Exception -> 0x04f4 }
                r1 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x04f4 }
                r1.<init>();	 Catch:{ Exception -> 0x04f4 }
                r2 = "DELETE FROM chat_settings_v2 WHERE uid = ";	 Catch:{ Exception -> 0x04f4 }
                r1.append(r2);	 Catch:{ Exception -> 0x04f4 }
                r2 = r3;	 Catch:{ Exception -> 0x04f4 }
                r1.append(r2);	 Catch:{ Exception -> 0x04f4 }
                r1 = r1.toString();	 Catch:{ Exception -> 0x04f4 }
                r0 = r0.executeFast(r1);	 Catch:{ Exception -> 0x04f4 }
                r0 = r0.stepThis();	 Catch:{ Exception -> 0x04f4 }
                r0.dispose();	 Catch:{ Exception -> 0x04f4 }
                r0 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x04f4 }
                r0 = r0.database;	 Catch:{ Exception -> 0x04f4 }
                r1 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x04f4 }
                r1.<init>();	 Catch:{ Exception -> 0x04f4 }
                r2 = "DELETE FROM chat_pinned WHERE uid = ";	 Catch:{ Exception -> 0x04f4 }
                r1.append(r2);	 Catch:{ Exception -> 0x04f4 }
                r2 = r3;	 Catch:{ Exception -> 0x04f4 }
                r1.append(r2);	 Catch:{ Exception -> 0x04f4 }
                r1 = r1.toString();	 Catch:{ Exception -> 0x04f4 }
                r0 = r0.executeFast(r1);	 Catch:{ Exception -> 0x04f4 }
                r0 = r0.stepThis();	 Catch:{ Exception -> 0x04f4 }
                r0.dispose();	 Catch:{ Exception -> 0x04f4 }
                r0 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x04f4 }
                r0 = r0.database;	 Catch:{ Exception -> 0x04f4 }
                r1 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x04f4 }
                r1.<init>();	 Catch:{ Exception -> 0x04f4 }
                r2 = "DELETE FROM channel_users_v2 WHERE did = ";	 Catch:{ Exception -> 0x04f4 }
                r1.append(r2);	 Catch:{ Exception -> 0x04f4 }
                r2 = r3;	 Catch:{ Exception -> 0x04f4 }
                r1.append(r2);	 Catch:{ Exception -> 0x04f4 }
                r1 = r1.toString();	 Catch:{ Exception -> 0x04f4 }
                r0 = r0.executeFast(r1);	 Catch:{ Exception -> 0x04f4 }
                r0 = r0.stepThis();	 Catch:{ Exception -> 0x04f4 }
                r0.dispose();	 Catch:{ Exception -> 0x04f4 }
                r0 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x04f4 }
                r0 = r0.database;	 Catch:{ Exception -> 0x04f4 }
                r1 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x04f4 }
                r1.<init>();	 Catch:{ Exception -> 0x04f4 }
                r2 = "DELETE FROM search_recent WHERE did = ";	 Catch:{ Exception -> 0x04f4 }
                r1.append(r2);	 Catch:{ Exception -> 0x04f4 }
                r2 = r3;	 Catch:{ Exception -> 0x04f4 }
                r1.append(r2);	 Catch:{ Exception -> 0x04f4 }
                r1 = r1.toString();	 Catch:{ Exception -> 0x04f4 }
                r0 = r0.executeFast(r1);	 Catch:{ Exception -> 0x04f4 }
                r0 = r0.stepThis();	 Catch:{ Exception -> 0x04f4 }
                r0.dispose();	 Catch:{ Exception -> 0x04f4 }
                r0 = r3;	 Catch:{ Exception -> 0x04f4 }
                r0 = (int) r0;	 Catch:{ Exception -> 0x04f4 }
                r1 = r3;	 Catch:{ Exception -> 0x04f4 }
                r3 = 32;	 Catch:{ Exception -> 0x04f4 }
                r1 = r1 >> r3;	 Catch:{ Exception -> 0x04f4 }
                r1 = (int) r1;	 Catch:{ Exception -> 0x04f4 }
                if (r0 == 0) goto L_0x03bd;	 Catch:{ Exception -> 0x04f4 }
            L_0x0397:
                if (r1 != r5) goto L_0x03bc;	 Catch:{ Exception -> 0x04f4 }
            L_0x0399:
                r2 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x04f4 }
                r2 = r2.database;	 Catch:{ Exception -> 0x04f4 }
                r3 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x04f4 }
                r3.<init>();	 Catch:{ Exception -> 0x04f4 }
                r5 = "DELETE FROM chats WHERE uid = ";	 Catch:{ Exception -> 0x04f4 }
                r3.append(r5);	 Catch:{ Exception -> 0x04f4 }
                r3.append(r0);	 Catch:{ Exception -> 0x04f4 }
                r3 = r3.toString();	 Catch:{ Exception -> 0x04f4 }
                r2 = r2.executeFast(r3);	 Catch:{ Exception -> 0x04f4 }
                r2 = r2.stepThis();	 Catch:{ Exception -> 0x04f4 }
                r2.dispose();	 Catch:{ Exception -> 0x04f4 }
                goto L_0x03df;	 Catch:{ Exception -> 0x04f4 }
            L_0x03bc:
                goto L_0x03df;	 Catch:{ Exception -> 0x04f4 }
            L_0x03bd:
                r2 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x04f4 }
                r2 = r2.database;	 Catch:{ Exception -> 0x04f4 }
                r3 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x04f4 }
                r3.<init>();	 Catch:{ Exception -> 0x04f4 }
                r5 = "DELETE FROM enc_chats WHERE uid = ";	 Catch:{ Exception -> 0x04f4 }
                r3.append(r5);	 Catch:{ Exception -> 0x04f4 }
                r3.append(r1);	 Catch:{ Exception -> 0x04f4 }
                r3 = r3.toString();	 Catch:{ Exception -> 0x04f4 }
                r2 = r2.executeFast(r3);	 Catch:{ Exception -> 0x04f4 }
                r2 = r2.stepThis();	 Catch:{ Exception -> 0x04f4 }
                r2.dispose();	 Catch:{ Exception -> 0x04f4 }
            L_0x03e0:
                r0 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x04f4 }
                r0 = r0.database;	 Catch:{ Exception -> 0x04f4 }
                r1 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x04f4 }
                r1.<init>();	 Catch:{ Exception -> 0x04f4 }
                r2 = "UPDATE dialogs SET unread_count = 0 WHERE did = ";	 Catch:{ Exception -> 0x04f4 }
                r1.append(r2);	 Catch:{ Exception -> 0x04f4 }
                r2 = r3;	 Catch:{ Exception -> 0x04f4 }
                r1.append(r2);	 Catch:{ Exception -> 0x04f4 }
                r1 = r1.toString();	 Catch:{ Exception -> 0x04f4 }
                r0 = r0.executeFast(r1);	 Catch:{ Exception -> 0x04f4 }
                r0 = r0.stepThis();	 Catch:{ Exception -> 0x04f4 }
                r0.dispose();	 Catch:{ Exception -> 0x04f4 }
                r0 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x04f4 }
                r0 = r0.database;	 Catch:{ Exception -> 0x04f4 }
                r1 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x04f4 }
                r1.<init>();	 Catch:{ Exception -> 0x04f4 }
                r2 = "DELETE FROM messages WHERE uid = ";	 Catch:{ Exception -> 0x04f4 }
                r1.append(r2);	 Catch:{ Exception -> 0x04f4 }
                r2 = r3;	 Catch:{ Exception -> 0x04f4 }
                r1.append(r2);	 Catch:{ Exception -> 0x04f4 }
                r1 = r1.toString();	 Catch:{ Exception -> 0x04f4 }
                r0 = r0.executeFast(r1);	 Catch:{ Exception -> 0x04f4 }
                r0 = r0.stepThis();	 Catch:{ Exception -> 0x04f4 }
                r0.dispose();	 Catch:{ Exception -> 0x04f4 }
                r0 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x04f4 }
                r0 = r0.database;	 Catch:{ Exception -> 0x04f4 }
                r1 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x04f4 }
                r1.<init>();	 Catch:{ Exception -> 0x04f4 }
                r2 = "DELETE FROM bot_keyboard WHERE uid = ";	 Catch:{ Exception -> 0x04f4 }
                r1.append(r2);	 Catch:{ Exception -> 0x04f4 }
                r2 = r3;	 Catch:{ Exception -> 0x04f4 }
                r1.append(r2);	 Catch:{ Exception -> 0x04f4 }
                r1 = r1.toString();	 Catch:{ Exception -> 0x04f4 }
                r0 = r0.executeFast(r1);	 Catch:{ Exception -> 0x04f4 }
                r0 = r0.stepThis();	 Catch:{ Exception -> 0x04f4 }
                r0.dispose();	 Catch:{ Exception -> 0x04f4 }
                r0 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x04f4 }
                r0 = r0.database;	 Catch:{ Exception -> 0x04f4 }
                r1 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x04f4 }
                r1.<init>();	 Catch:{ Exception -> 0x04f4 }
                r2 = "DELETE FROM media_counts_v2 WHERE uid = ";	 Catch:{ Exception -> 0x04f4 }
                r1.append(r2);	 Catch:{ Exception -> 0x04f4 }
                r2 = r3;	 Catch:{ Exception -> 0x04f4 }
                r1.append(r2);	 Catch:{ Exception -> 0x04f4 }
                r1 = r1.toString();	 Catch:{ Exception -> 0x04f4 }
                r0 = r0.executeFast(r1);	 Catch:{ Exception -> 0x04f4 }
                r0 = r0.stepThis();	 Catch:{ Exception -> 0x04f4 }
                r0.dispose();	 Catch:{ Exception -> 0x04f4 }
                r0 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x04f4 }
                r0 = r0.database;	 Catch:{ Exception -> 0x04f4 }
                r1 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x04f4 }
                r1.<init>();	 Catch:{ Exception -> 0x04f4 }
                r2 = "DELETE FROM media_v2 WHERE uid = ";	 Catch:{ Exception -> 0x04f4 }
                r1.append(r2);	 Catch:{ Exception -> 0x04f4 }
                r2 = r3;	 Catch:{ Exception -> 0x04f4 }
                r1.append(r2);	 Catch:{ Exception -> 0x04f4 }
                r1 = r1.toString();	 Catch:{ Exception -> 0x04f4 }
                r0 = r0.executeFast(r1);	 Catch:{ Exception -> 0x04f4 }
                r0 = r0.stepThis();	 Catch:{ Exception -> 0x04f4 }
                r0.dispose();	 Catch:{ Exception -> 0x04f4 }
                r0 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x04f4 }
                r0 = r0.database;	 Catch:{ Exception -> 0x04f4 }
                r1 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x04f4 }
                r1.<init>();	 Catch:{ Exception -> 0x04f4 }
                r2 = "DELETE FROM messages_holes WHERE uid = ";	 Catch:{ Exception -> 0x04f4 }
                r1.append(r2);	 Catch:{ Exception -> 0x04f4 }
                r2 = r3;	 Catch:{ Exception -> 0x04f4 }
                r1.append(r2);	 Catch:{ Exception -> 0x04f4 }
                r1 = r1.toString();	 Catch:{ Exception -> 0x04f4 }
                r0 = r0.executeFast(r1);	 Catch:{ Exception -> 0x04f4 }
                r0 = r0.stepThis();	 Catch:{ Exception -> 0x04f4 }
                r0.dispose();	 Catch:{ Exception -> 0x04f4 }
                r0 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x04f4 }
                r0 = r0.database;	 Catch:{ Exception -> 0x04f4 }
                r1 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x04f4 }
                r1.<init>();	 Catch:{ Exception -> 0x04f4 }
                r2 = "DELETE FROM media_holes_v2 WHERE uid = ";	 Catch:{ Exception -> 0x04f4 }
                r1.append(r2);	 Catch:{ Exception -> 0x04f4 }
                r2 = r3;	 Catch:{ Exception -> 0x04f4 }
                r1.append(r2);	 Catch:{ Exception -> 0x04f4 }
                r1 = r1.toString();	 Catch:{ Exception -> 0x04f4 }
                r0 = r0.executeFast(r1);	 Catch:{ Exception -> 0x04f4 }
                r0 = r0.stepThis();	 Catch:{ Exception -> 0x04f4 }
                r0.dispose();	 Catch:{ Exception -> 0x04f4 }
                r0 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x04f4 }
                r0 = r0.currentAccount;	 Catch:{ Exception -> 0x04f4 }
                r0 = org.telegram.messenger.DataQuery.getInstance(r0);	 Catch:{ Exception -> 0x04f4 }
                r1 = r3;	 Catch:{ Exception -> 0x04f4 }
                r0.clearBotKeyboard(r1, r4);	 Catch:{ Exception -> 0x04f4 }
                r0 = new org.telegram.messenger.MessagesStorage$23$1;	 Catch:{ Exception -> 0x04f4 }
                r0.<init>();	 Catch:{ Exception -> 0x04f4 }
                org.telegram.messenger.AndroidUtilities.runOnUIThread(r0);	 Catch:{ Exception -> 0x04f4 }
                goto L_0x04f8;
            L_0x04f4:
                r0 = move-exception;
                org.telegram.messenger.FileLog.e(r0);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.MessagesStorage.23.run():void");
            }
        });
    }

    public void getDialogPhotos(int did, int count, long max_id, int classGuid) {
        final long j = max_id;
        final int i = did;
        final int i2 = count;
        final int i3 = classGuid;
        this.storageQueue.postRunnable(new Runnable() {
            public void run() {
                try {
                    SQLiteCursor cursor;
                    if (j != 0) {
                        cursor = MessagesStorage.this.database.queryFinalized(String.format(Locale.US, "SELECT data FROM user_photos WHERE uid = %d AND id < %d ORDER BY id DESC LIMIT %d", new Object[]{Integer.valueOf(i), Long.valueOf(j), Integer.valueOf(i2)}), new Object[0]);
                    } else {
                        cursor = MessagesStorage.this.database.queryFinalized(String.format(Locale.US, "SELECT data FROM user_photos WHERE uid = %d ORDER BY id DESC LIMIT %d", new Object[]{Integer.valueOf(i), Integer.valueOf(i2)}), new Object[0]);
                    }
                    final photos_Photos res = new TL_photos_photos();
                    while (cursor.next()) {
                        NativeByteBuffer data = cursor.byteBufferValue(0);
                        if (data != null) {
                            Photo photo = Photo.TLdeserialize(data, data.readInt32(false), false);
                            data.reuse();
                            res.photos.add(photo);
                        }
                    }
                    cursor.dispose();
                    Utilities.stageQueue.postRunnable(new Runnable() {
                        public void run() {
                            MessagesController.getInstance(MessagesStorage.this.currentAccount).processLoadedUserPhotos(res, i, i2, j, true, i3);
                        }
                    });
                } catch (Throwable e) {
                    FileLog.e(e);
                }
            }
        });
    }

    public void clearUserPhotos(final int uid) {
        this.storageQueue.postRunnable(new Runnable() {
            public void run() {
                try {
                    SQLiteDatabase access$000 = MessagesStorage.this.database;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("DELETE FROM user_photos WHERE uid = ");
                    stringBuilder.append(uid);
                    access$000.executeFast(stringBuilder.toString()).stepThis().dispose();
                } catch (Throwable e) {
                    FileLog.e(e);
                }
            }
        });
    }

    public void clearUserPhoto(final int uid, final long pid) {
        this.storageQueue.postRunnable(new Runnable() {
            public void run() {
                try {
                    SQLiteDatabase access$000 = MessagesStorage.this.database;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("DELETE FROM user_photos WHERE uid = ");
                    stringBuilder.append(uid);
                    stringBuilder.append(" AND id = ");
                    stringBuilder.append(pid);
                    access$000.executeFast(stringBuilder.toString()).stepThis().dispose();
                } catch (Throwable e) {
                    FileLog.e(e);
                }
            }
        });
    }

    public void resetDialogs(messages_Dialogs dialogsRes, int messagesCount, int seq, int newPts, int date, int qts, LongSparseArray<TL_dialog> new_dialogs_dict, LongSparseArray<MessageObject> new_dialogMessage, Message lastMessage, int dialogsCount) {
        final messages_Dialogs org_telegram_tgnet_TLRPC_messages_Dialogs = dialogsRes;
        final int i = dialogsCount;
        final int i2 = seq;
        final int i3 = newPts;
        final int i4 = date;
        final int i5 = qts;
        final Message message = lastMessage;
        final int i6 = messagesCount;
        final LongSparseArray<TL_dialog> longSparseArray = new_dialogs_dict;
        final LongSparseArray<MessageObject> longSparseArray2 = new_dialogMessage;
        this.storageQueue.postRunnable(new Runnable() {

            class AnonymousClass1 implements Comparator<Long> {
                final /* synthetic */ LongSparseArray val$oldPinnedDialogNums;

                AnonymousClass1(LongSparseArray longSparseArray) {
                    this.val$oldPinnedDialogNums = longSparseArray;
                }

                public int compare(Long o1, Long o2) {
                    Integer val1 = (Integer) this.val$oldPinnedDialogNums.get(o1.longValue());
                    Integer val2 = (Integer) this.val$oldPinnedDialogNums.get(o2.longValue());
                    if (val1.intValue() < val2.intValue()) {
                        return 1;
                    }
                    if (val1.intValue() > val2.intValue()) {
                        return -1;
                    }
                    return 0;
                }
            }

            public void run() {
                /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.MessagesStorage.27.run():void
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:116)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:249)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:256)
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
                r1 = r30;
                r2 = 0;
                r3 = new java.util.ArrayList;	 Catch:{ Exception -> 0x0424 }
                r3.<init>();	 Catch:{ Exception -> 0x0424 }
                r4 = r2;	 Catch:{ Exception -> 0x0424 }
                r4 = r4.dialogs;	 Catch:{ Exception -> 0x0424 }
                r4 = r4.size();	 Catch:{ Exception -> 0x0424 }
                r5 = r3;	 Catch:{ Exception -> 0x0424 }
                r4 = r4 - r5;	 Catch:{ Exception -> 0x0424 }
                r5 = new android.util.LongSparseArray;	 Catch:{ Exception -> 0x0424 }
                r5.<init>();	 Catch:{ Exception -> 0x0424 }
                r6 = new java.util.ArrayList;	 Catch:{ Exception -> 0x0424 }
                r6.<init>();	 Catch:{ Exception -> 0x0424 }
                r7 = new java.util.ArrayList;	 Catch:{ Exception -> 0x0424 }
                r7.<init>();	 Catch:{ Exception -> 0x0424 }
                r8 = r3;	 Catch:{ Exception -> 0x0424 }
            L_0x0024:
                r9 = r2;	 Catch:{ Exception -> 0x0424 }
                r9 = r9.dialogs;	 Catch:{ Exception -> 0x0424 }
                r9 = r9.size();	 Catch:{ Exception -> 0x0424 }
                if (r8 >= r9) goto L_0x0044;	 Catch:{ Exception -> 0x0424 }
            L_0x002e:
                r9 = r2;	 Catch:{ Exception -> 0x0424 }
                r9 = r9.dialogs;	 Catch:{ Exception -> 0x0424 }
                r9 = r9.get(r8);	 Catch:{ Exception -> 0x0424 }
                r9 = (org.telegram.tgnet.TLRPC.TL_dialog) r9;	 Catch:{ Exception -> 0x0424 }
                r10 = r9.id;	 Catch:{ Exception -> 0x0424 }
                r10 = java.lang.Long.valueOf(r10);	 Catch:{ Exception -> 0x0424 }
                r7.add(r10);	 Catch:{ Exception -> 0x0424 }
                r8 = r8 + 1;	 Catch:{ Exception -> 0x0424 }
                goto L_0x0024;	 Catch:{ Exception -> 0x0424 }
            L_0x0044:
                r8 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x0424 }
                r8 = r8.database;	 Catch:{ Exception -> 0x0424 }
                r9 = "SELECT did, pinned FROM dialogs WHERE 1";	 Catch:{ Exception -> 0x0424 }
                r10 = 0;	 Catch:{ Exception -> 0x0424 }
                r11 = new java.lang.Object[r10];	 Catch:{ Exception -> 0x0424 }
                r8 = r8.queryFinalized(r9, r11);	 Catch:{ Exception -> 0x0424 }
            L_0x0053:
                r9 = r8.next();	 Catch:{ Exception -> 0x0424 }
                if (r9 == 0) goto L_0x0082;	 Catch:{ Exception -> 0x0424 }
            L_0x0059:
                r11 = r8.longValue(r10);	 Catch:{ Exception -> 0x0424 }
                r9 = 1;	 Catch:{ Exception -> 0x0424 }
                r9 = r8.intValue(r9);	 Catch:{ Exception -> 0x0424 }
                r13 = (int) r11;	 Catch:{ Exception -> 0x0424 }
                if (r13 == 0) goto L_0x0081;	 Catch:{ Exception -> 0x0424 }
            L_0x0065:
                r14 = java.lang.Integer.valueOf(r13);	 Catch:{ Exception -> 0x0424 }
                r3.add(r14);	 Catch:{ Exception -> 0x0424 }
                if (r9 <= 0) goto L_0x0081;	 Catch:{ Exception -> 0x0424 }
            L_0x006e:
                r14 = java.lang.Math.max(r9, r2);	 Catch:{ Exception -> 0x0424 }
                r2 = r14;	 Catch:{ Exception -> 0x0424 }
                r14 = java.lang.Integer.valueOf(r9);	 Catch:{ Exception -> 0x0424 }
                r5.put(r11, r14);	 Catch:{ Exception -> 0x0424 }
                r14 = java.lang.Long.valueOf(r11);	 Catch:{ Exception -> 0x0424 }
                r6.add(r14);	 Catch:{ Exception -> 0x0424 }
            L_0x0081:
                goto L_0x0053;	 Catch:{ Exception -> 0x0424 }
            L_0x0082:
                r9 = new org.telegram.messenger.MessagesStorage$27$1;	 Catch:{ Exception -> 0x0424 }
                r9.<init>(r5);	 Catch:{ Exception -> 0x0424 }
                java.util.Collections.sort(r6, r9);	 Catch:{ Exception -> 0x0424 }
            L_0x008a:
                r9 = r6.size();	 Catch:{ Exception -> 0x0424 }
                if (r9 >= r4) goto L_0x009a;	 Catch:{ Exception -> 0x0424 }
            L_0x0090:
                r11 = 0;	 Catch:{ Exception -> 0x0424 }
                r9 = java.lang.Long.valueOf(r11);	 Catch:{ Exception -> 0x0424 }
                r6.add(r10, r9);	 Catch:{ Exception -> 0x0424 }
                goto L_0x008a;	 Catch:{ Exception -> 0x0424 }
            L_0x009a:
                r8.dispose();	 Catch:{ Exception -> 0x0424 }
                r9 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0424 }
                r9.<init>();	 Catch:{ Exception -> 0x0424 }
                r11 = "(";	 Catch:{ Exception -> 0x0424 }
                r9.append(r11);	 Catch:{ Exception -> 0x0424 }
                r11 = ",";	 Catch:{ Exception -> 0x0424 }
                r11 = android.text.TextUtils.join(r11, r3);	 Catch:{ Exception -> 0x0424 }
                r9.append(r11);	 Catch:{ Exception -> 0x0424 }
                r11 = ")";	 Catch:{ Exception -> 0x0424 }
                r9.append(r11);	 Catch:{ Exception -> 0x0424 }
                r9 = r9.toString();	 Catch:{ Exception -> 0x0424 }
                r11 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x0424 }
                r11 = r11.database;	 Catch:{ Exception -> 0x0424 }
                r11.beginTransaction();	 Catch:{ Exception -> 0x0424 }
                r11 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x0424 }
                r11 = r11.database;	 Catch:{ Exception -> 0x0424 }
                r12 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0424 }
                r12.<init>();	 Catch:{ Exception -> 0x0424 }
                r13 = "DELETE FROM dialogs WHERE did IN ";	 Catch:{ Exception -> 0x0424 }
                r12.append(r13);	 Catch:{ Exception -> 0x0424 }
                r12.append(r9);	 Catch:{ Exception -> 0x0424 }
                r12 = r12.toString();	 Catch:{ Exception -> 0x0424 }
                r11 = r11.executeFast(r12);	 Catch:{ Exception -> 0x0424 }
                r11 = r11.stepThis();	 Catch:{ Exception -> 0x0424 }
                r11.dispose();	 Catch:{ Exception -> 0x0424 }
                r11 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x0424 }
                r11 = r11.database;	 Catch:{ Exception -> 0x0424 }
                r12 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0424 }
                r12.<init>();	 Catch:{ Exception -> 0x0424 }
                r13 = "DELETE FROM messages WHERE uid IN ";	 Catch:{ Exception -> 0x0424 }
                r12.append(r13);	 Catch:{ Exception -> 0x0424 }
                r12.append(r9);	 Catch:{ Exception -> 0x0424 }
                r12 = r12.toString();	 Catch:{ Exception -> 0x0424 }
                r11 = r11.executeFast(r12);	 Catch:{ Exception -> 0x0424 }
                r11 = r11.stepThis();	 Catch:{ Exception -> 0x0424 }
                r11.dispose();	 Catch:{ Exception -> 0x0424 }
                r11 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x0424 }
                r11 = r11.database;	 Catch:{ Exception -> 0x0424 }
                r12 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0424 }
                r12.<init>();	 Catch:{ Exception -> 0x0424 }
                r13 = "DELETE FROM bot_keyboard WHERE uid IN ";	 Catch:{ Exception -> 0x0424 }
                r12.append(r13);	 Catch:{ Exception -> 0x0424 }
                r12.append(r9);	 Catch:{ Exception -> 0x0424 }
                r12 = r12.toString();	 Catch:{ Exception -> 0x0424 }
                r11 = r11.executeFast(r12);	 Catch:{ Exception -> 0x0424 }
                r11 = r11.stepThis();	 Catch:{ Exception -> 0x0424 }
                r11.dispose();	 Catch:{ Exception -> 0x0424 }
                r11 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x0424 }
                r11 = r11.database;	 Catch:{ Exception -> 0x0424 }
                r12 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0424 }
                r12.<init>();	 Catch:{ Exception -> 0x0424 }
                r13 = "DELETE FROM media_counts_v2 WHERE uid IN ";	 Catch:{ Exception -> 0x0424 }
                r12.append(r13);	 Catch:{ Exception -> 0x0424 }
                r12.append(r9);	 Catch:{ Exception -> 0x0424 }
                r12 = r12.toString();	 Catch:{ Exception -> 0x0424 }
                r11 = r11.executeFast(r12);	 Catch:{ Exception -> 0x0424 }
                r11 = r11.stepThis();	 Catch:{ Exception -> 0x0424 }
                r11.dispose();	 Catch:{ Exception -> 0x0424 }
                r11 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x0424 }
                r11 = r11.database;	 Catch:{ Exception -> 0x0424 }
                r12 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0424 }
                r12.<init>();	 Catch:{ Exception -> 0x0424 }
                r13 = "DELETE FROM media_v2 WHERE uid IN ";	 Catch:{ Exception -> 0x0424 }
                r12.append(r13);	 Catch:{ Exception -> 0x0424 }
                r12.append(r9);	 Catch:{ Exception -> 0x0424 }
                r12 = r12.toString();	 Catch:{ Exception -> 0x0424 }
                r11 = r11.executeFast(r12);	 Catch:{ Exception -> 0x0424 }
                r11 = r11.stepThis();	 Catch:{ Exception -> 0x0424 }
                r11.dispose();	 Catch:{ Exception -> 0x0424 }
                r11 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x0424 }
                r11 = r11.database;	 Catch:{ Exception -> 0x0424 }
                r12 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0424 }
                r12.<init>();	 Catch:{ Exception -> 0x0424 }
                r13 = "DELETE FROM messages_holes WHERE uid IN ";	 Catch:{ Exception -> 0x0424 }
                r12.append(r13);	 Catch:{ Exception -> 0x0424 }
                r12.append(r9);	 Catch:{ Exception -> 0x0424 }
                r12 = r12.toString();	 Catch:{ Exception -> 0x0424 }
                r11 = r11.executeFast(r12);	 Catch:{ Exception -> 0x0424 }
                r11 = r11.stepThis();	 Catch:{ Exception -> 0x0424 }
                r11.dispose();	 Catch:{ Exception -> 0x0424 }
                r11 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x0424 }
                r11 = r11.database;	 Catch:{ Exception -> 0x0424 }
                r12 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0424 }
                r12.<init>();	 Catch:{ Exception -> 0x0424 }
                r13 = "DELETE FROM media_holes_v2 WHERE uid IN ";	 Catch:{ Exception -> 0x0424 }
                r12.append(r13);	 Catch:{ Exception -> 0x0424 }
                r12.append(r9);	 Catch:{ Exception -> 0x0424 }
                r12 = r12.toString();	 Catch:{ Exception -> 0x0424 }
                r11 = r11.executeFast(r12);	 Catch:{ Exception -> 0x0424 }
                r11 = r11.stepThis();	 Catch:{ Exception -> 0x0424 }
                r11.dispose();	 Catch:{ Exception -> 0x0424 }
                r11 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x0424 }
                r11 = r11.database;	 Catch:{ Exception -> 0x0424 }
                r11.commitTransaction();	 Catch:{ Exception -> 0x0424 }
                r11 = r10;	 Catch:{ Exception -> 0x0424 }
                if (r11 >= r4) goto L_0x022d;	 Catch:{ Exception -> 0x0424 }
            L_0x01bc:
                r12 = r2;	 Catch:{ Exception -> 0x0424 }
                r12 = r12.dialogs;	 Catch:{ Exception -> 0x0424 }
                r13 = r3;	 Catch:{ Exception -> 0x0424 }
                r13 = r13 + r11;	 Catch:{ Exception -> 0x0424 }
                r12 = r12.get(r13);	 Catch:{ Exception -> 0x0424 }
                r12 = (org.telegram.tgnet.TLRPC.TL_dialog) r12;	 Catch:{ Exception -> 0x0424 }
                r13 = r12.id;	 Catch:{ Exception -> 0x0424 }
                r13 = java.lang.Long.valueOf(r13);	 Catch:{ Exception -> 0x0424 }
                r13 = r6.indexOf(r13);	 Catch:{ Exception -> 0x0424 }
                r14 = r12.id;	 Catch:{ Exception -> 0x0424 }
                r14 = java.lang.Long.valueOf(r14);	 Catch:{ Exception -> 0x0424 }
                r14 = r7.indexOf(r14);	 Catch:{ Exception -> 0x0424 }
                r15 = -1;	 Catch:{ Exception -> 0x0424 }
                if (r13 == r15) goto L_0x021a;	 Catch:{ Exception -> 0x0424 }
            L_0x01e0:
                if (r14 == r15) goto L_0x021a;	 Catch:{ Exception -> 0x0424 }
            L_0x01e2:
                if (r13 != r14) goto L_0x01fa;	 Catch:{ Exception -> 0x0424 }
            L_0x01e4:
                r16 = r11;	 Catch:{ Exception -> 0x0424 }
                r10 = r12.id;	 Catch:{ Exception -> 0x0424 }
                r10 = r5.get(r10);	 Catch:{ Exception -> 0x0424 }
                r10 = (java.lang.Integer) r10;	 Catch:{ Exception -> 0x0424 }
                if (r10 == 0) goto L_0x01f6;	 Catch:{ Exception -> 0x0424 }
            L_0x01f0:
                r11 = r10.intValue();	 Catch:{ Exception -> 0x0424 }
                r12.pinnedNum = r11;	 Catch:{ Exception -> 0x0424 }
                r17 = r3;	 Catch:{ Exception -> 0x0424 }
                goto L_0x021e;	 Catch:{ Exception -> 0x0424 }
            L_0x01fa:
                r16 = r11;	 Catch:{ Exception -> 0x0424 }
                r10 = r6.get(r14);	 Catch:{ Exception -> 0x0424 }
                r10 = (java.lang.Long) r10;	 Catch:{ Exception -> 0x0424 }
                r10 = r10.longValue();	 Catch:{ Exception -> 0x0424 }
                r15 = r5.get(r10);	 Catch:{ Exception -> 0x0424 }
                r15 = (java.lang.Integer) r15;	 Catch:{ Exception -> 0x0424 }
                if (r15 == 0) goto L_0x0217;	 Catch:{ Exception -> 0x0424 }
                r17 = r3;	 Catch:{ Exception -> 0x0424 }
                r3 = r15.intValue();	 Catch:{ Exception -> 0x0424 }
                r12.pinnedNum = r3;	 Catch:{ Exception -> 0x0424 }
                goto L_0x021e;	 Catch:{ Exception -> 0x0424 }
                r17 = r3;	 Catch:{ Exception -> 0x0424 }
                goto L_0x021e;	 Catch:{ Exception -> 0x0424 }
            L_0x021a:
                r17 = r3;	 Catch:{ Exception -> 0x0424 }
                r16 = r11;	 Catch:{ Exception -> 0x0424 }
                r3 = r12.pinnedNum;	 Catch:{ Exception -> 0x0424 }
                if (r3 != 0) goto L_0x0227;	 Catch:{ Exception -> 0x0424 }
                r3 = r4 - r16;	 Catch:{ Exception -> 0x0424 }
                r3 = r3 + r2;	 Catch:{ Exception -> 0x0424 }
                r12.pinnedNum = r3;	 Catch:{ Exception -> 0x0424 }
                r11 = r16 + 1;	 Catch:{ Exception -> 0x0424 }
                r3 = r17;	 Catch:{ Exception -> 0x0424 }
                r10 = 0;	 Catch:{ Exception -> 0x0424 }
                goto L_0x01ba;	 Catch:{ Exception -> 0x0424 }
            L_0x022d:
                r17 = r3;	 Catch:{ Exception -> 0x0424 }
                r3 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x0424 }
                r10 = r2;	 Catch:{ Exception -> 0x0424 }
                r11 = 0;	 Catch:{ Exception -> 0x0424 }
                r3.putDialogsInternal(r10, r11);	 Catch:{ Exception -> 0x0424 }
                r3 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x0424 }
                r10 = r4;	 Catch:{ Exception -> 0x0424 }
                r11 = r5;	 Catch:{ Exception -> 0x0424 }
                r12 = r6;	 Catch:{ Exception -> 0x0424 }
                r13 = r7;	 Catch:{ Exception -> 0x0424 }
                r3.saveDiffParamsInternal(r10, r11, r12, r13);	 Catch:{ Exception -> 0x0424 }
                r3 = r8;	 Catch:{ Exception -> 0x0424 }
                if (r3 == 0) goto L_0x03d1;	 Catch:{ Exception -> 0x0424 }
                r3 = r8;	 Catch:{ Exception -> 0x0424 }
                r3 = r3.id;	 Catch:{ Exception -> 0x0424 }
                r10 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x0424 }
                r10 = r10.currentAccount;	 Catch:{ Exception -> 0x0424 }
                r10 = org.telegram.messenger.UserConfig.getInstance(r10);	 Catch:{ Exception -> 0x0424 }
                r10 = r10.dialogsLoadOffsetId;	 Catch:{ Exception -> 0x0424 }
                if (r3 == r10) goto L_0x03d1;	 Catch:{ Exception -> 0x0424 }
                r3 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x0424 }
                r3 = r3.currentAccount;	 Catch:{ Exception -> 0x0424 }
                r3 = org.telegram.messenger.UserConfig.getInstance(r3);	 Catch:{ Exception -> 0x0424 }
                r10 = r2;	 Catch:{ Exception -> 0x0424 }
                r10 = r10.dialogs;	 Catch:{ Exception -> 0x0424 }
                r10 = r10.size();	 Catch:{ Exception -> 0x0424 }
                r3.totalDialogsLoadCount = r10;	 Catch:{ Exception -> 0x0424 }
                r3 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x0424 }
                r3 = r3.currentAccount;	 Catch:{ Exception -> 0x0424 }
                r3 = org.telegram.messenger.UserConfig.getInstance(r3);	 Catch:{ Exception -> 0x0424 }
                r10 = r8;	 Catch:{ Exception -> 0x0424 }
                r10 = r10.id;	 Catch:{ Exception -> 0x0424 }
                r3.dialogsLoadOffsetId = r10;	 Catch:{ Exception -> 0x0424 }
                r3 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x0424 }
                r3 = r3.currentAccount;	 Catch:{ Exception -> 0x0424 }
                r3 = org.telegram.messenger.UserConfig.getInstance(r3);	 Catch:{ Exception -> 0x0424 }
                r10 = r8;	 Catch:{ Exception -> 0x0424 }
                r10 = r10.date;	 Catch:{ Exception -> 0x0424 }
                r3.dialogsLoadOffsetDate = r10;	 Catch:{ Exception -> 0x0424 }
                r3 = r8;	 Catch:{ Exception -> 0x0424 }
                r3 = r3.to_id;	 Catch:{ Exception -> 0x0424 }
                r3 = r3.channel_id;	 Catch:{ Exception -> 0x0424 }
                if (r3 == 0) goto L_0x02fa;	 Catch:{ Exception -> 0x0424 }
                r3 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x0424 }
                r3 = r3.currentAccount;	 Catch:{ Exception -> 0x0424 }
                r3 = org.telegram.messenger.UserConfig.getInstance(r3);	 Catch:{ Exception -> 0x0424 }
                r10 = r8;	 Catch:{ Exception -> 0x0424 }
                r10 = r10.to_id;	 Catch:{ Exception -> 0x0424 }
                r10 = r10.channel_id;	 Catch:{ Exception -> 0x0424 }
                r3.dialogsLoadOffsetChannelId = r10;	 Catch:{ Exception -> 0x0424 }
                r3 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x0424 }
                r3 = r3.currentAccount;	 Catch:{ Exception -> 0x0424 }
                r3 = org.telegram.messenger.UserConfig.getInstance(r3);	 Catch:{ Exception -> 0x0424 }
                r10 = 0;	 Catch:{ Exception -> 0x0424 }
                r3.dialogsLoadOffsetChatId = r10;	 Catch:{ Exception -> 0x0424 }
                r3 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x0424 }
                r3 = r3.currentAccount;	 Catch:{ Exception -> 0x0424 }
                r3 = org.telegram.messenger.UserConfig.getInstance(r3);	 Catch:{ Exception -> 0x0424 }
                r10 = 0;	 Catch:{ Exception -> 0x0424 }
                r3.dialogsLoadOffsetUserId = r10;	 Catch:{ Exception -> 0x0424 }
                r3 = 0;	 Catch:{ Exception -> 0x0424 }
                r10 = r2;	 Catch:{ Exception -> 0x0424 }
                r10 = r10.chats;	 Catch:{ Exception -> 0x0424 }
                r10 = r10.size();	 Catch:{ Exception -> 0x0424 }
                if (r3 >= r10) goto L_0x03e0;	 Catch:{ Exception -> 0x0424 }
                r10 = r2;	 Catch:{ Exception -> 0x0424 }
                r10 = r10.chats;	 Catch:{ Exception -> 0x0424 }
                r10 = r10.get(r3);	 Catch:{ Exception -> 0x0424 }
                r10 = (org.telegram.tgnet.TLRPC.Chat) r10;	 Catch:{ Exception -> 0x0424 }
                r11 = r10.id;	 Catch:{ Exception -> 0x0424 }
                r12 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x0424 }
                r12 = r12.currentAccount;	 Catch:{ Exception -> 0x0424 }
                r12 = org.telegram.messenger.UserConfig.getInstance(r12);	 Catch:{ Exception -> 0x0424 }
                r12 = r12.dialogsLoadOffsetChannelId;	 Catch:{ Exception -> 0x0424 }
                if (r11 != r12) goto L_0x02f7;	 Catch:{ Exception -> 0x0424 }
                r11 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x0424 }
                r11 = r11.currentAccount;	 Catch:{ Exception -> 0x0424 }
                r11 = org.telegram.messenger.UserConfig.getInstance(r11);	 Catch:{ Exception -> 0x0424 }
                r12 = r10.access_hash;	 Catch:{ Exception -> 0x0424 }
                r11.dialogsLoadOffsetAccess = r12;	 Catch:{ Exception -> 0x0424 }
                goto L_0x03e0;	 Catch:{ Exception -> 0x0424 }
                r3 = r3 + 1;	 Catch:{ Exception -> 0x0424 }
                goto L_0x02c3;	 Catch:{ Exception -> 0x0424 }
                r3 = r8;	 Catch:{ Exception -> 0x0424 }
                r3 = r3.to_id;	 Catch:{ Exception -> 0x0424 }
                r3 = r3.chat_id;	 Catch:{ Exception -> 0x0424 }
                if (r3 == 0) goto L_0x0366;	 Catch:{ Exception -> 0x0424 }
                r3 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x0424 }
                r3 = r3.currentAccount;	 Catch:{ Exception -> 0x0424 }
                r3 = org.telegram.messenger.UserConfig.getInstance(r3);	 Catch:{ Exception -> 0x0424 }
                r10 = r8;	 Catch:{ Exception -> 0x0424 }
                r10 = r10.to_id;	 Catch:{ Exception -> 0x0424 }
                r10 = r10.chat_id;	 Catch:{ Exception -> 0x0424 }
                r3.dialogsLoadOffsetChatId = r10;	 Catch:{ Exception -> 0x0424 }
                r3 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x0424 }
                r3 = r3.currentAccount;	 Catch:{ Exception -> 0x0424 }
                r3 = org.telegram.messenger.UserConfig.getInstance(r3);	 Catch:{ Exception -> 0x0424 }
                r10 = 0;	 Catch:{ Exception -> 0x0424 }
                r3.dialogsLoadOffsetChannelId = r10;	 Catch:{ Exception -> 0x0424 }
                r3 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x0424 }
                r3 = r3.currentAccount;	 Catch:{ Exception -> 0x0424 }
                r3 = org.telegram.messenger.UserConfig.getInstance(r3);	 Catch:{ Exception -> 0x0424 }
                r10 = 0;	 Catch:{ Exception -> 0x0424 }
                r3.dialogsLoadOffsetUserId = r10;	 Catch:{ Exception -> 0x0424 }
                r3 = 0;	 Catch:{ Exception -> 0x0424 }
                r10 = r2;	 Catch:{ Exception -> 0x0424 }
                r10 = r10.chats;	 Catch:{ Exception -> 0x0424 }
                r10 = r10.size();	 Catch:{ Exception -> 0x0424 }
                if (r3 >= r10) goto L_0x03e0;	 Catch:{ Exception -> 0x0424 }
                r10 = r2;	 Catch:{ Exception -> 0x0424 }
                r10 = r10.chats;	 Catch:{ Exception -> 0x0424 }
                r10 = r10.get(r3);	 Catch:{ Exception -> 0x0424 }
                r10 = (org.telegram.tgnet.TLRPC.Chat) r10;	 Catch:{ Exception -> 0x0424 }
                r11 = r10.id;	 Catch:{ Exception -> 0x0424 }
                r12 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x0424 }
                r12 = r12.currentAccount;	 Catch:{ Exception -> 0x0424 }
                r12 = org.telegram.messenger.UserConfig.getInstance(r12);	 Catch:{ Exception -> 0x0424 }
                r12 = r12.dialogsLoadOffsetChatId;	 Catch:{ Exception -> 0x0424 }
                if (r11 != r12) goto L_0x0363;	 Catch:{ Exception -> 0x0424 }
                r11 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x0424 }
                r11 = r11.currentAccount;	 Catch:{ Exception -> 0x0424 }
                r11 = org.telegram.messenger.UserConfig.getInstance(r11);	 Catch:{ Exception -> 0x0424 }
                r12 = r10.access_hash;	 Catch:{ Exception -> 0x0424 }
                r11.dialogsLoadOffsetAccess = r12;	 Catch:{ Exception -> 0x0424 }
                goto L_0x03e0;	 Catch:{ Exception -> 0x0424 }
                r3 = r3 + 1;	 Catch:{ Exception -> 0x0424 }
                goto L_0x032f;	 Catch:{ Exception -> 0x0424 }
                r3 = r8;	 Catch:{ Exception -> 0x0424 }
                r3 = r3.to_id;	 Catch:{ Exception -> 0x0424 }
                r3 = r3.user_id;	 Catch:{ Exception -> 0x0424 }
                if (r3 == 0) goto L_0x03e0;	 Catch:{ Exception -> 0x0424 }
                r3 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x0424 }
                r3 = r3.currentAccount;	 Catch:{ Exception -> 0x0424 }
                r3 = org.telegram.messenger.UserConfig.getInstance(r3);	 Catch:{ Exception -> 0x0424 }
                r10 = r8;	 Catch:{ Exception -> 0x0424 }
                r10 = r10.to_id;	 Catch:{ Exception -> 0x0424 }
                r10 = r10.user_id;	 Catch:{ Exception -> 0x0424 }
                r3.dialogsLoadOffsetUserId = r10;	 Catch:{ Exception -> 0x0424 }
                r3 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x0424 }
                r3 = r3.currentAccount;	 Catch:{ Exception -> 0x0424 }
                r3 = org.telegram.messenger.UserConfig.getInstance(r3);	 Catch:{ Exception -> 0x0424 }
                r10 = 0;	 Catch:{ Exception -> 0x0424 }
                r3.dialogsLoadOffsetChatId = r10;	 Catch:{ Exception -> 0x0424 }
                r3 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x0424 }
                r3 = r3.currentAccount;	 Catch:{ Exception -> 0x0424 }
                r3 = org.telegram.messenger.UserConfig.getInstance(r3);	 Catch:{ Exception -> 0x0424 }
                r10 = 0;	 Catch:{ Exception -> 0x0424 }
                r3.dialogsLoadOffsetChannelId = r10;	 Catch:{ Exception -> 0x0424 }
                r3 = 0;	 Catch:{ Exception -> 0x0424 }
                r10 = r2;	 Catch:{ Exception -> 0x0424 }
                r10 = r10.users;	 Catch:{ Exception -> 0x0424 }
                r10 = r10.size();	 Catch:{ Exception -> 0x0424 }
                if (r3 >= r10) goto L_0x03e0;	 Catch:{ Exception -> 0x0424 }
                r10 = r2;	 Catch:{ Exception -> 0x0424 }
                r10 = r10.users;	 Catch:{ Exception -> 0x0424 }
                r10 = r10.get(r3);	 Catch:{ Exception -> 0x0424 }
                r10 = (org.telegram.tgnet.TLRPC.User) r10;	 Catch:{ Exception -> 0x0424 }
                r11 = r10.id;	 Catch:{ Exception -> 0x0424 }
                r12 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x0424 }
                r12 = r12.currentAccount;	 Catch:{ Exception -> 0x0424 }
                r12 = org.telegram.messenger.UserConfig.getInstance(r12);	 Catch:{ Exception -> 0x0424 }
                r12 = r12.dialogsLoadOffsetUserId;	 Catch:{ Exception -> 0x0424 }
                if (r11 != r12) goto L_0x03ce;	 Catch:{ Exception -> 0x0424 }
                r11 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x0424 }
                r11 = r11.currentAccount;	 Catch:{ Exception -> 0x0424 }
                r11 = org.telegram.messenger.UserConfig.getInstance(r11);	 Catch:{ Exception -> 0x0424 }
                r12 = r10.access_hash;	 Catch:{ Exception -> 0x0424 }
                r11.dialogsLoadOffsetAccess = r12;	 Catch:{ Exception -> 0x0424 }
                goto L_0x03e0;	 Catch:{ Exception -> 0x0424 }
                r3 = r3 + 1;	 Catch:{ Exception -> 0x0424 }
                goto L_0x039b;	 Catch:{ Exception -> 0x0424 }
                r3 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x0424 }
                r3 = r3.currentAccount;	 Catch:{ Exception -> 0x0424 }
                r3 = org.telegram.messenger.UserConfig.getInstance(r3);	 Catch:{ Exception -> 0x0424 }
                r10 = 2147483647; // 0x7fffffff float:NaN double:1.060997895E-314;	 Catch:{ Exception -> 0x0424 }
                r3.dialogsLoadOffsetId = r10;	 Catch:{ Exception -> 0x0424 }
                r3 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x0424 }
                r3 = r3.currentAccount;	 Catch:{ Exception -> 0x0424 }
                r3 = org.telegram.messenger.UserConfig.getInstance(r3);	 Catch:{ Exception -> 0x0424 }
                r10 = 0;	 Catch:{ Exception -> 0x0424 }
                r3.saveConfig(r10);	 Catch:{ Exception -> 0x0424 }
                r3 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x0424 }
                r3 = r3.currentAccount;	 Catch:{ Exception -> 0x0424 }
                r18 = org.telegram.messenger.MessagesController.getInstance(r3);	 Catch:{ Exception -> 0x0424 }
                r3 = r2;	 Catch:{ Exception -> 0x0424 }
                r10 = r9;	 Catch:{ Exception -> 0x0424 }
                r11 = r4;	 Catch:{ Exception -> 0x0424 }
                r12 = r5;	 Catch:{ Exception -> 0x0424 }
                r13 = r6;	 Catch:{ Exception -> 0x0424 }
                r14 = r7;	 Catch:{ Exception -> 0x0424 }
                r15 = r10;	 Catch:{ Exception -> 0x0424 }
                r28 = r2;	 Catch:{ Exception -> 0x0424 }
                r2 = r11;	 Catch:{ Exception -> 0x0424 }
                r29 = r4;	 Catch:{ Exception -> 0x0424 }
                r4 = r8;	 Catch:{ Exception -> 0x0424 }
                r19 = r3;	 Catch:{ Exception -> 0x0424 }
                r20 = r10;	 Catch:{ Exception -> 0x0424 }
                r21 = r11;	 Catch:{ Exception -> 0x0424 }
                r22 = r12;	 Catch:{ Exception -> 0x0424 }
                r23 = r13;	 Catch:{ Exception -> 0x0424 }
                r24 = r14;	 Catch:{ Exception -> 0x0424 }
                r25 = r15;	 Catch:{ Exception -> 0x0424 }
                r26 = r2;	 Catch:{ Exception -> 0x0424 }
                r27 = r4;	 Catch:{ Exception -> 0x0424 }
                r18.completeDialogsReset(r19, r20, r21, r22, r23, r24, r25, r26, r27);	 Catch:{ Exception -> 0x0424 }
                goto L_0x0429;
            L_0x0424:
                r0 = move-exception;
                r2 = r0;
                org.telegram.messenger.FileLog.e(r2);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.MessagesStorage.27.run():void");
            }
        });
    }

    public void putDialogPhotos(final int did, final photos_Photos photos) {
        if (photos != null) {
            if (!photos.photos.isEmpty()) {
                this.storageQueue.postRunnable(new Runnable() {
                    public void run() {
                        try {
                            SQLitePreparedStatement state = MessagesStorage.this.database.executeFast("REPLACE INTO user_photos VALUES(?, ?, ?)");
                            Iterator it = photos.photos.iterator();
                            while (it.hasNext()) {
                                Photo photo = (Photo) it.next();
                                if (!(photo instanceof TL_photoEmpty)) {
                                    state.requery();
                                    NativeByteBuffer data = new NativeByteBuffer(photo.getObjectSize());
                                    photo.serializeToStream(data);
                                    state.bindInteger(1, did);
                                    state.bindLong(2, photo.id);
                                    state.bindByteBuffer(3, data);
                                    state.step();
                                    data.reuse();
                                }
                            }
                            state.dispose();
                        } catch (Throwable e) {
                            FileLog.e(e);
                        }
                    }
                });
            }
        }
    }

    public void emptyMessagesMedia(final ArrayList<Integer> mids) {
        this.storageQueue.postRunnable(new Runnable() {
            public void run() {
                try {
                    ArrayList<File> filesToDelete = new ArrayList();
                    final ArrayList<Message> messages = new ArrayList();
                    SQLiteCursor cursor = MessagesStorage.this.database.queryFinalized(String.format(Locale.US, "SELECT data, mid, date, uid FROM messages WHERE mid IN (%s)", new Object[]{TextUtils.join(",", mids)}), new Object[0]);
                    while (cursor.next()) {
                        NativeByteBuffer data = cursor.byteBufferValue(0);
                        if (data != null) {
                            Message message = Message.TLdeserialize(data, data.readInt32(false), false);
                            message.readAttachPath(data, UserConfig.getInstance(MessagesStorage.this.currentAccount).clientUserId);
                            data.reuse();
                            if (message.media != null) {
                                if (message.media.document != null) {
                                    File file = FileLoader.getPathToAttach(message.media.document, true);
                                    if (file != null && file.toString().length() > 0) {
                                        filesToDelete.add(file);
                                    }
                                    file = FileLoader.getPathToAttach(message.media.document.thumb, true);
                                    if (file != null && file.toString().length() > 0) {
                                        filesToDelete.add(file);
                                    }
                                    message.media.document = new TL_documentEmpty();
                                } else if (message.media.photo != null) {
                                    Iterator it = message.media.photo.sizes.iterator();
                                    while (it.hasNext()) {
                                        File file2 = FileLoader.getPathToAttach((PhotoSize) it.next(), true);
                                        if (file2 != null && file2.toString().length() > 0) {
                                            filesToDelete.add(file2);
                                        }
                                    }
                                    message.media.photo = new TL_photoEmpty();
                                }
                                message.media.flags &= -2;
                                message.id = cursor.intValue(1);
                                message.date = cursor.intValue(2);
                                message.dialog_id = cursor.longValue(3);
                                messages.add(message);
                            }
                        }
                    }
                    cursor.dispose();
                    if (!messages.isEmpty()) {
                        SQLitePreparedStatement state = MessagesStorage.this.database.executeFast("REPLACE INTO messages VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, NULL, ?, ?)");
                        for (int a = 0; a < messages.size(); a++) {
                            Message message2 = (Message) messages.get(a);
                            NativeByteBuffer data2 = new NativeByteBuffer(message2.getObjectSize());
                            message2.serializeToStream(data2);
                            state.requery();
                            state.bindLong(1, (long) message2.id);
                            state.bindLong(2, message2.dialog_id);
                            state.bindInteger(3, MessageObject.getUnreadFlags(message2));
                            state.bindInteger(4, message2.send_state);
                            state.bindInteger(5, message2.date);
                            state.bindByteBuffer(6, data2);
                            state.bindInteger(7, MessageObject.isOut(message2));
                            state.bindInteger(8, message2.ttl);
                            if ((message2.flags & 1024) != 0) {
                                state.bindInteger(9, message2.views);
                            } else {
                                state.bindInteger(9, MessagesStorage.this.getMessageMediaType(message2));
                            }
                            state.bindInteger(10, 0);
                            state.bindInteger(11, message2.mentioned);
                            state.step();
                            data2.reuse();
                        }
                        state.dispose();
                        AndroidUtilities.runOnUIThread(new Runnable() {
                            public void run() {
                                for (int a = 0; a < messages.size(); a++) {
                                    NotificationCenter.getInstance(MessagesStorage.this.currentAccount).postNotificationName(NotificationCenter.updateMessageMedia, messages.get(a));
                                }
                            }
                        });
                    }
                    FileLoader.getInstance(MessagesStorage.this.currentAccount).deleteFiles(filesToDelete, 0);
                } catch (Throwable e) {
                    FileLog.e(e);
                }
            }
        });
    }

    public void getNewTask(final ArrayList<Integer> oldTask, int channelId) {
        this.storageQueue.postRunnable(new Runnable() {
            public void run() {
                try {
                    if (oldTask != null) {
                        String ids = TextUtils.join(",", oldTask);
                        MessagesStorage.this.database.executeFast(String.format(Locale.US, "DELETE FROM enc_tasks_v2 WHERE mid IN(%s)", new Object[]{ids})).stepThis().dispose();
                    }
                    int date = 0;
                    int channelId = -1;
                    ArrayList<Integer> arr = null;
                    SQLiteCursor cursor = MessagesStorage.this.database.queryFinalized("SELECT mid, date FROM enc_tasks_v2 WHERE date = (SELECT min(date) FROM enc_tasks_v2)", new Object[0]);
                    while (cursor.next()) {
                        long mid = cursor.longValue(0);
                        if (channelId == -1) {
                            channelId = (int) (mid >> 32);
                            if (channelId < 0) {
                                channelId = 0;
                            }
                        }
                        date = cursor.intValue(1);
                        if (arr == null) {
                            arr = new ArrayList();
                        }
                        arr.add(Integer.valueOf((int) mid));
                    }
                    cursor.dispose();
                    MessagesController.getInstance(MessagesStorage.this.currentAccount).processLoadedDeleteTask(date, arr, channelId);
                } catch (Throwable e) {
                    FileLog.e(e);
                }
            }
        });
    }

    public void markMentionMessageAsRead(int messageId, int channelId, long did) {
        final int i = messageId;
        final int i2 = channelId;
        final long j = did;
        this.storageQueue.postRunnable(new Runnable() {
            public void run() {
                try {
                    long mid = (long) i;
                    if (i2 != 0) {
                        mid |= ((long) i2) << 32;
                    }
                    MessagesStorage.this.database.executeFast(String.format(Locale.US, "UPDATE messages SET read_state = read_state | 2 WHERE mid = %d", new Object[]{Long.valueOf(mid)})).stepThis().dispose();
                    SQLiteCursor cursor = MessagesStorage.this.database;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("SELECT unread_count_i FROM dialogs WHERE did = ");
                    stringBuilder.append(j);
                    cursor = cursor.queryFinalized(stringBuilder.toString(), new Object[0]);
                    int old_mentions_count = 0;
                    if (cursor.next()) {
                        old_mentions_count = Math.max(0, cursor.intValue(0) - 1);
                    }
                    cursor.dispose();
                    MessagesStorage.this.database.executeFast(String.format(Locale.US, "UPDATE dialogs SET unread_count_i = %d WHERE did = %d", new Object[]{Integer.valueOf(old_mentions_count), Long.valueOf(j)})).stepThis().dispose();
                    LongSparseArray<Integer> sparseArray = new LongSparseArray(1);
                    sparseArray.put(j, Integer.valueOf(old_mentions_count));
                    MessagesController.getInstance(MessagesStorage.this.currentAccount).processDialogsUpdateRead(null, sparseArray);
                } catch (Throwable e) {
                    FileLog.e(e);
                }
            }
        });
    }

    public void markMessageAsMention(final long mid) {
        this.storageQueue.postRunnable(new Runnable() {
            public void run() {
                try {
                    MessagesStorage.this.database.executeFast(String.format(Locale.US, "UPDATE messages SET mention = 1, read_state = read_state & ~2 WHERE mid = %d", new Object[]{Long.valueOf(mid)})).stepThis().dispose();
                } catch (Throwable e) {
                    FileLog.e(e);
                }
            }
        });
    }

    public void resetMentionsCount(final long did, final int count) {
        this.storageQueue.postRunnable(new Runnable() {
            public void run() {
                try {
                    if (count == 0) {
                        MessagesStorage.this.database.executeFast(String.format(Locale.US, "UPDATE messages SET read_state = read_state | 2 WHERE uid = %d AND mention = 1 AND read_state IN(0, 1)", new Object[]{Long.valueOf(did)})).stepThis().dispose();
                    }
                    MessagesStorage.this.database.executeFast(String.format(Locale.US, "UPDATE dialogs SET unread_count_i = %d WHERE did = %d", new Object[]{Integer.valueOf(count), Long.valueOf(did)})).stepThis().dispose();
                    LongSparseArray<Integer> sparseArray = new LongSparseArray(1);
                    sparseArray.put(did, Integer.valueOf(count));
                    MessagesController.getInstance(MessagesStorage.this.currentAccount).processDialogsUpdateRead(null, sparseArray);
                } catch (Throwable e) {
                    FileLog.e(e);
                }
            }
        });
    }

    public void createTaskForMid(int messageId, int channelId, int time, int readTime, int ttl, boolean inner) {
        final int i = time;
        final int i2 = readTime;
        final int i3 = ttl;
        final int i4 = messageId;
        final int i5 = channelId;
        final boolean z = inner;
        this.storageQueue.postRunnable(new Runnable() {
            public void run() {
                try {
                    int minDate = (i > i2 ? i : i2) + i3;
                    SparseArray<ArrayList<Long>> messages = new SparseArray();
                    final ArrayList<Long> midsArray = new ArrayList();
                    long mid = (long) i4;
                    if (i5 != 0) {
                        mid |= ((long) i5) << 32;
                    }
                    midsArray.add(Long.valueOf(mid));
                    messages.put(minDate, midsArray);
                    AndroidUtilities.runOnUIThread(new Runnable() {
                        public void run() {
                            if (!z) {
                                MessagesStorage.this.markMessagesContentAsRead(midsArray, 0);
                            }
                            NotificationCenter.getInstance(MessagesStorage.this.currentAccount).postNotificationName(NotificationCenter.messagesReadContent, midsArray);
                        }
                    });
                    SQLitePreparedStatement state = MessagesStorage.this.database.executeFast("REPLACE INTO enc_tasks_v2 VALUES(?, ?)");
                    for (int a = 0; a < messages.size(); a++) {
                        int key = messages.keyAt(a);
                        ArrayList<Long> arr = (ArrayList) messages.get(key);
                        for (int b = 0; b < arr.size(); b++) {
                            state.requery();
                            state.bindLong(1, ((Long) arr.get(b)).longValue());
                            state.bindInteger(2, key);
                            state.step();
                        }
                    }
                    state.dispose();
                    MessagesStorage.this.database.executeFast(String.format(Locale.US, "UPDATE messages SET ttl = 0 WHERE mid = %d", new Object[]{Long.valueOf(mid)})).stepThis().dispose();
                    MessagesController.getInstance(MessagesStorage.this.currentAccount).didAddedNewTask(minDate, messages);
                } catch (Throwable e) {
                    FileLog.e(e);
                }
            }
        });
    }

    public void createTaskForSecretChat(int chatId, int time, int readTime, int isOut, ArrayList<Long> random_ids) {
        final ArrayList<Long> arrayList = random_ids;
        final int i = chatId;
        final int i2 = isOut;
        final int i3 = time;
        final int i4 = readTime;
        this.storageQueue.postRunnable(new Runnable() {
            public void run() {
                int minDate = ConnectionsManager.DEFAULT_DATACENTER_ID;
                try {
                    SQLiteCursor cursor;
                    SparseArray<ArrayList<Long>> messages = new SparseArray();
                    final ArrayList<Long> midsArray = new ArrayList();
                    StringBuilder mids = new StringBuilder();
                    if (arrayList == null) {
                        cursor = MessagesStorage.this.database.queryFinalized(String.format(Locale.US, "SELECT mid, ttl FROM messages WHERE uid = %d AND out = %d AND read_state != 0 AND ttl > 0 AND date <= %d AND send_state = 0 AND media != 1", new Object[]{Long.valueOf(((long) i) << 32), Integer.valueOf(i2), Integer.valueOf(i3)}), new Object[0]);
                    } else {
                        String ids = TextUtils.join(",", arrayList);
                        cursor = MessagesStorage.this.database.queryFinalized(String.format(Locale.US, "SELECT m.mid, m.ttl FROM messages as m INNER JOIN randoms as r ON m.mid = r.mid WHERE r.random_id IN (%s)", new Object[]{ids}), new Object[0]);
                    }
                    while (cursor.next()) {
                        int ttl = cursor.intValue(1);
                        long mid = (long) cursor.intValue(0);
                        if (arrayList != null) {
                            midsArray.add(Long.valueOf(mid));
                        }
                        if (ttl > 0) {
                            int date = (i3 > i4 ? i3 : i4) + ttl;
                            minDate = Math.min(minDate, date);
                            ArrayList<Long> arr = (ArrayList) messages.get(date);
                            if (arr == null) {
                                arr = new ArrayList();
                                messages.put(date, arr);
                            }
                            if (mids.length() != 0) {
                                mids.append(",");
                            }
                            mids.append(mid);
                            arr.add(Long.valueOf(mid));
                        }
                    }
                    cursor.dispose();
                    if (arrayList != null) {
                        AndroidUtilities.runOnUIThread(new Runnable() {
                            public void run() {
                                MessagesStorage.this.markMessagesContentAsRead(midsArray, 0);
                                NotificationCenter.getInstance(MessagesStorage.this.currentAccount).postNotificationName(NotificationCenter.messagesReadContent, midsArray);
                            }
                        });
                    }
                    if (messages.size() != 0) {
                        MessagesStorage.this.database.beginTransaction();
                        SQLitePreparedStatement state = MessagesStorage.this.database.executeFast("REPLACE INTO enc_tasks_v2 VALUES(?, ?)");
                        for (int a = 0; a < messages.size(); a++) {
                            int key = messages.keyAt(a);
                            ArrayList<Long> arr2 = (ArrayList) messages.get(key);
                            for (int b = 0; b < arr2.size(); b++) {
                                state.requery();
                                state.bindLong(1, ((Long) arr2.get(b)).longValue());
                                state.bindInteger(2, key);
                                state.step();
                            }
                        }
                        state.dispose();
                        MessagesStorage.this.database.commitTransaction();
                        MessagesStorage.this.database.executeFast(String.format(Locale.US, "UPDATE messages SET ttl = 0 WHERE mid IN(%s)", new Object[]{mids.toString()})).stepThis().dispose();
                        MessagesController.getInstance(MessagesStorage.this.currentAccount).didAddedNewTask(minDate, messages);
                    }
                } catch (Throwable e) {
                    FileLog.e(e);
                }
            }
        });
    }

    private static boolean isEmpty(SparseArray<?> array) {
        if (array != null) {
            if (array.size() != 0) {
                return false;
            }
        }
        return true;
    }

    private static boolean isEmpty(SparseLongArray array) {
        if (array != null) {
            if (array.size() != 0) {
                return false;
            }
        }
        return true;
    }

    private static boolean isEmpty(List<?> array) {
        if (array != null) {
            if (!array.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    private static boolean isEmpty(SparseIntArray array) {
        if (array != null) {
            if (array.size() != 0) {
                return false;
            }
        }
        return true;
    }

    private static boolean isEmpty(LongSparseArray<?> array) {
        if (array != null) {
            if (array.size() != 0) {
                return false;
            }
        }
        return true;
    }

    public void updateDialogsWithReadMessages(final SparseLongArray inbox, final SparseLongArray outbox, final ArrayList<Long> mentions, boolean useQueue) {
        if (!isEmpty(inbox) || !isEmpty((List) mentions)) {
            if (useQueue) {
                this.storageQueue.postRunnable(new Runnable() {
                    public void run() {
                        MessagesStorage.this.updateDialogsWithReadMessagesInternal(null, inbox, outbox, mentions);
                    }
                });
            } else {
                updateDialogsWithReadMessagesInternal(null, inbox, outbox, mentions);
            }
        }
    }

    public void updateChatParticipants(final ChatParticipants participants) {
        if (participants != null) {
            this.storageQueue.postRunnable(new Runnable() {
                public void run() {
                    try {
                        SQLiteCursor cursor = MessagesStorage.this.database;
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("SELECT info, pinned FROM chat_settings_v2 WHERE uid = ");
                        stringBuilder.append(participants.chat_id);
                        cursor = cursor.queryFinalized(stringBuilder.toString(), new Object[0]);
                        ChatFull info = null;
                        ArrayList<User> loadedUsers = new ArrayList();
                        if (cursor.next()) {
                            NativeByteBuffer data = cursor.byteBufferValue(0);
                            if (data != null) {
                                info = ChatFull.TLdeserialize(data, data.readInt32(false), false);
                                data.reuse();
                                info.pinned_msg_id = cursor.intValue(1);
                            }
                        }
                        cursor.dispose();
                        if (info instanceof TL_chatFull) {
                            info.participants = participants;
                            final ChatFull finalInfo = info;
                            AndroidUtilities.runOnUIThread(new Runnable() {
                                public void run() {
                                    NotificationCenter.getInstance(MessagesStorage.this.currentAccount).postNotificationName(NotificationCenter.chatInfoDidLoaded, finalInfo, Integer.valueOf(0), Boolean.valueOf(false), null);
                                }
                            });
                            SQLitePreparedStatement state = MessagesStorage.this.database.executeFast("REPLACE INTO chat_settings_v2 VALUES(?, ?, ?)");
                            NativeByteBuffer data2 = new NativeByteBuffer(info.getObjectSize());
                            info.serializeToStream(data2);
                            state.bindInteger(1, info.id);
                            state.bindByteBuffer(2, data2);
                            state.bindInteger(3, info.pinned_msg_id);
                            state.step();
                            state.dispose();
                            data2.reuse();
                        }
                    } catch (Throwable e) {
                        FileLog.e(e);
                    }
                }
            });
        }
    }

    public void loadChannelAdmins(final int chatId) {
        this.storageQueue.postRunnable(new Runnable() {
            public void run() {
                try {
                    SQLiteCursor cursor = MessagesStorage.this.database;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("SELECT uid FROM channel_admins WHERE did = ");
                    stringBuilder.append(chatId);
                    cursor = cursor.queryFinalized(stringBuilder.toString(), new Object[0]);
                    ArrayList<Integer> ids = new ArrayList();
                    while (cursor.next()) {
                        ids.add(Integer.valueOf(cursor.intValue(0)));
                    }
                    cursor.dispose();
                    MessagesController.getInstance(MessagesStorage.this.currentAccount).processLoadedChannelAdmins(ids, chatId, true);
                } catch (Throwable e) {
                    FileLog.e(e);
                }
            }
        });
    }

    public void putChannelAdmins(final int chatId, final ArrayList<Integer> ids) {
        this.storageQueue.postRunnable(new Runnable() {
            public void run() {
                try {
                    SQLiteDatabase access$000 = MessagesStorage.this.database;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("DELETE FROM channel_admins WHERE did = ");
                    stringBuilder.append(chatId);
                    access$000.executeFast(stringBuilder.toString()).stepThis().dispose();
                    MessagesStorage.this.database.beginTransaction();
                    SQLitePreparedStatement state = MessagesStorage.this.database.executeFast("REPLACE INTO channel_admins VALUES(?, ?)");
                    int date = (int) (System.currentTimeMillis() / 1000);
                    for (int a = 0; a < ids.size(); a++) {
                        state.requery();
                        state.bindInteger(1, chatId);
                        state.bindInteger(2, ((Integer) ids.get(a)).intValue());
                        state.step();
                    }
                    state.dispose();
                    MessagesStorage.this.database.commitTransaction();
                } catch (Throwable e) {
                    FileLog.e(e);
                }
            }
        });
    }

    public void updateChannelUsers(final int channel_id, final ArrayList<ChannelParticipant> participants) {
        this.storageQueue.postRunnable(new Runnable() {
            public void run() {
                try {
                    long did = (long) (-channel_id);
                    SQLiteDatabase access$000 = MessagesStorage.this.database;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("DELETE FROM channel_users_v2 WHERE did = ");
                    stringBuilder.append(did);
                    access$000.executeFast(stringBuilder.toString()).stepThis().dispose();
                    MessagesStorage.this.database.beginTransaction();
                    SQLitePreparedStatement state = MessagesStorage.this.database.executeFast("REPLACE INTO channel_users_v2 VALUES(?, ?, ?, ?)");
                    int date = (int) (System.currentTimeMillis() / 1000);
                    for (int a = 0; a < participants.size(); a++) {
                        ChannelParticipant participant = (ChannelParticipant) participants.get(a);
                        state.requery();
                        state.bindLong(1, did);
                        state.bindInteger(2, participant.user_id);
                        state.bindInteger(3, date);
                        NativeByteBuffer data = new NativeByteBuffer(participant.getObjectSize());
                        participant.serializeToStream(data);
                        state.bindByteBuffer(4, data);
                        data.reuse();
                        state.step();
                        date--;
                    }
                    state.dispose();
                    MessagesStorage.this.database.commitTransaction();
                    MessagesStorage.this.loadChatInfo(channel_id, null, false, true);
                } catch (Throwable e) {
                    FileLog.e(e);
                }
            }
        });
    }

    public void saveBotCache(final String key, final TLObject result) {
        if (result != null) {
            if (!TextUtils.isEmpty(key)) {
                this.storageQueue.postRunnable(new Runnable() {
                    public void run() {
                        try {
                            int currentDate = ConnectionsManager.getInstance(MessagesStorage.this.currentAccount).getCurrentTime();
                            if (result instanceof TL_messages_botCallbackAnswer) {
                                currentDate += ((TL_messages_botCallbackAnswer) result).cache_time;
                            } else if (result instanceof TL_messages_botResults) {
                                currentDate += ((TL_messages_botResults) result).cache_time;
                            }
                            SQLitePreparedStatement state = MessagesStorage.this.database.executeFast("REPLACE INTO botcache VALUES(?, ?, ?)");
                            NativeByteBuffer data = new NativeByteBuffer(result.getObjectSize());
                            result.serializeToStream(data);
                            state.bindString(1, key);
                            state.bindInteger(2, currentDate);
                            state.bindByteBuffer(3, data);
                            state.step();
                            state.dispose();
                            data.reuse();
                        } catch (Throwable e) {
                            FileLog.e(e);
                        }
                    }
                });
            }
        }
    }

    public void getBotCache(final String key, final RequestDelegate requestDelegate) {
        if (key != null) {
            if (requestDelegate != null) {
                final int currentDate = ConnectionsManager.getInstance(this.currentAccount).getCurrentTime();
                this.storageQueue.postRunnable(new Runnable() {
                    public void run() {
                        TLObject result = null;
                        try {
                            SQLiteDatabase access$000 = MessagesStorage.this.database;
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append("DELETE FROM botcache WHERE date < ");
                            stringBuilder.append(currentDate);
                            access$000.executeFast(stringBuilder.toString()).stepThis().dispose();
                            SQLiteCursor cursor = MessagesStorage.this.database.queryFinalized(String.format(Locale.US, "SELECT data FROM botcache WHERE id = '%s'", new Object[]{key}), new Object[0]);
                            if (cursor.next()) {
                                try {
                                    NativeByteBuffer data = cursor.byteBufferValue(0);
                                    if (data != null) {
                                        int constructor = data.readInt32(false);
                                        if (constructor == TL_messages_botCallbackAnswer.constructor) {
                                            result = TL_messages_botCallbackAnswer.TLdeserialize(data, constructor, false);
                                        } else {
                                            result = messages_BotResults.TLdeserialize(data, constructor, false);
                                        }
                                        data.reuse();
                                    }
                                } catch (Throwable e) {
                                    FileLog.e(e);
                                }
                            }
                            cursor.dispose();
                        } catch (Throwable e2) {
                            FileLog.e(e2);
                        } catch (Throwable th) {
                            requestDelegate.run(result, null);
                        }
                        requestDelegate.run(result, null);
                    }
                });
            }
        }
    }

    public void updateChatInfo(final ChatFull info, final boolean ifExist) {
        this.storageQueue.postRunnable(new Runnable() {
            public void run() {
                try {
                    if (ifExist) {
                        SQLiteCursor cursor = MessagesStorage.this.database;
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("SELECT uid FROM chat_settings_v2 WHERE uid = ");
                        stringBuilder.append(info.id);
                        cursor = cursor.queryFinalized(stringBuilder.toString(), new Object[0]);
                        boolean exist = cursor.next();
                        cursor.dispose();
                        if (!exist) {
                            return;
                        }
                    }
                    SQLitePreparedStatement state = MessagesStorage.this.database.executeFast("REPLACE INTO chat_settings_v2 VALUES(?, ?, ?)");
                    NativeByteBuffer data = new NativeByteBuffer(info.getObjectSize());
                    info.serializeToStream(data);
                    state.bindInteger(1, info.id);
                    state.bindByteBuffer(2, data);
                    state.bindInteger(3, info.pinned_msg_id);
                    state.step();
                    state.dispose();
                    data.reuse();
                    if (info instanceof TL_channelFull) {
                        SQLiteCursor cursor2 = MessagesStorage.this.database;
                        StringBuilder stringBuilder2 = new StringBuilder();
                        stringBuilder2.append("SELECT date, pts, last_mid, inbox_max, outbox_max, pinned, unread_count_i FROM dialogs WHERE did = ");
                        stringBuilder2.append(-info.id);
                        cursor2 = cursor2.queryFinalized(stringBuilder2.toString(), new Object[0]);
                        if (cursor2.next()) {
                            int inbox_max = cursor2.intValue(3);
                            if (inbox_max < info.read_inbox_max_id) {
                                int dialog_date = cursor2.intValue(0);
                                int pts = cursor2.intValue(1);
                                long last_mid = cursor2.longValue(2);
                                int outbox_max = cursor2.intValue(4);
                                int pinned = cursor2.intValue(5);
                                int mentions = cursor2.intValue(6);
                                state = MessagesStorage.this.database.executeFast("REPLACE INTO dialogs VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
                                state.bindLong(1, (long) (-info.id));
                                state.bindInteger(2, dialog_date);
                                state.bindInteger(3, info.unread_count);
                                state.bindLong(4, last_mid);
                                state.bindInteger(5, info.read_inbox_max_id);
                                state.bindInteger(6, Math.max(outbox_max, info.read_outbox_max_id));
                                state.bindLong(7, 0);
                                state.bindInteger(8, mentions);
                                state.bindInteger(9, pts);
                                state.bindInteger(10, 0);
                                state.bindInteger(11, pinned);
                                state.step();
                                state.dispose();
                            }
                        }
                        cursor2.dispose();
                    }
                } catch (Throwable e) {
                    FileLog.e(e);
                }
            }
        });
    }

    public void updateChannelPinnedMessage(final int channelId, final int messageId) {
        this.storageQueue.postRunnable(new Runnable() {
            public void run() {
                try {
                    SQLiteCursor cursor = MessagesStorage.this.database;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("SELECT info, pinned FROM chat_settings_v2 WHERE uid = ");
                    stringBuilder.append(channelId);
                    cursor = cursor.queryFinalized(stringBuilder.toString(), new Object[0]);
                    ChatFull info = null;
                    ArrayList<User> loadedUsers = new ArrayList();
                    if (cursor.next()) {
                        NativeByteBuffer data = cursor.byteBufferValue(0);
                        if (data != null) {
                            info = ChatFull.TLdeserialize(data, data.readInt32(false), false);
                            data.reuse();
                            info.pinned_msg_id = cursor.intValue(1);
                        }
                    }
                    cursor.dispose();
                    if (info instanceof TL_channelFull) {
                        info.pinned_msg_id = messageId;
                        info.flags |= 32;
                        final ChatFull finalInfo = info;
                        AndroidUtilities.runOnUIThread(new Runnable() {
                            public void run() {
                                NotificationCenter.getInstance(MessagesStorage.this.currentAccount).postNotificationName(NotificationCenter.chatInfoDidLoaded, finalInfo, Integer.valueOf(0), Boolean.valueOf(false), null);
                            }
                        });
                        SQLitePreparedStatement state = MessagesStorage.this.database.executeFast("REPLACE INTO chat_settings_v2 VALUES(?, ?, ?)");
                        NativeByteBuffer data2 = new NativeByteBuffer(info.getObjectSize());
                        info.serializeToStream(data2);
                        state.bindInteger(1, channelId);
                        state.bindByteBuffer(2, data2);
                        state.bindInteger(3, info.pinned_msg_id);
                        state.step();
                        state.dispose();
                        data2.reuse();
                    }
                } catch (Throwable e) {
                    FileLog.e(e);
                }
            }
        });
    }

    public void updateChatInfo(int chat_id, int user_id, int what, int invited_id, int version) {
        final int i = chat_id;
        final int i2 = what;
        final int i3 = user_id;
        final int i4 = invited_id;
        final int i5 = version;
        this.storageQueue.postRunnable(new Runnable() {
            public void run() {
                try {
                    SQLiteCursor cursor = MessagesStorage.this.database;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("SELECT info, pinned FROM chat_settings_v2 WHERE uid = ");
                    stringBuilder.append(i);
                    int a = 0;
                    cursor = cursor.queryFinalized(stringBuilder.toString(), new Object[0]);
                    ChatFull info = null;
                    ArrayList<User> loadedUsers = new ArrayList();
                    if (cursor.next()) {
                        NativeByteBuffer data = cursor.byteBufferValue(0);
                        if (data != null) {
                            info = ChatFull.TLdeserialize(data, data.readInt32(false), false);
                            data.reuse();
                            info.pinned_msg_id = cursor.intValue(1);
                        }
                    }
                    cursor.dispose();
                    if (info instanceof TL_chatFull) {
                        if (i2 == 1) {
                            while (a < info.participants.participants.size()) {
                                if (((ChatParticipant) info.participants.participants.get(a)).user_id == i3) {
                                    info.participants.participants.remove(a);
                                    break;
                                }
                                a++;
                            }
                        } else if (i2 == 0) {
                            Iterator it = info.participants.participants.iterator();
                            while (it.hasNext()) {
                                if (((ChatParticipant) it.next()).user_id == i3) {
                                    return;
                                }
                            }
                            a = new TL_chatParticipant();
                            a.user_id = i3;
                            a.inviter_id = i4;
                            a.date = ConnectionsManager.getInstance(MessagesStorage.this.currentAccount).getCurrentTime();
                            info.participants.participants.add(a);
                        } else if (i2 == 2) {
                            while (a < info.participants.participants.size()) {
                                ChatParticipant participant = (ChatParticipant) info.participants.participants.get(a);
                                if (participant.user_id == i3) {
                                    ChatParticipant newParticipant;
                                    if (i4 == 1) {
                                        newParticipant = new TL_chatParticipantAdmin();
                                        newParticipant.user_id = participant.user_id;
                                        newParticipant.date = participant.date;
                                        newParticipant.inviter_id = participant.inviter_id;
                                    } else {
                                        newParticipant = new TL_chatParticipant();
                                        newParticipant.user_id = participant.user_id;
                                        newParticipant.date = participant.date;
                                        newParticipant.inviter_id = participant.inviter_id;
                                    }
                                    info.participants.participants.set(a, newParticipant);
                                } else {
                                    a++;
                                }
                            }
                        }
                        info.participants.version = i5;
                        final ChatFull finalInfo = info;
                        AndroidUtilities.runOnUIThread(new Runnable() {
                            public void run() {
                                NotificationCenter.getInstance(MessagesStorage.this.currentAccount).postNotificationName(NotificationCenter.chatInfoDidLoaded, finalInfo, Integer.valueOf(0), Boolean.valueOf(false), null);
                            }
                        });
                        SQLitePreparedStatement state = MessagesStorage.this.database.executeFast("REPLACE INTO chat_settings_v2 VALUES(?, ?, ?)");
                        NativeByteBuffer data2 = new NativeByteBuffer(info.getObjectSize());
                        info.serializeToStream(data2);
                        state.bindInteger(1, i);
                        state.bindByteBuffer(2, data2);
                        state.bindInteger(3, info.pinned_msg_id);
                        state.step();
                        state.dispose();
                        data2.reuse();
                    }
                } catch (Throwable e) {
                    FileLog.e(e);
                }
            }
        });
    }

    public boolean isMigratedChat(final int chat_id) {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        final boolean[] result = new boolean[1];
        this.storageQueue.postRunnable(new Runnable() {
            public void run() {
                /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find block by offset: 0x006d in list []
	at jadx.core.utils.BlockUtils.getBlockByOffset(BlockUtils.java:42)
	at jadx.core.dex.instructions.IfNode.initBlocks(IfNode.java:60)
	at jadx.core.dex.visitors.blocksmaker.BlockFinish.initBlocksInIfNodes(BlockFinish.java:48)
	at jadx.core.dex.visitors.blocksmaker.BlockFinish.visit(BlockFinish.java:33)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
                /*
                r6 = this;
                r0 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x0060, all -> 0x005e }
                r0 = r0.database;	 Catch:{ Exception -> 0x0060, all -> 0x005e }
                r1 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0060, all -> 0x005e }
                r1.<init>();	 Catch:{ Exception -> 0x0060, all -> 0x005e }
                r2 = "SELECT info FROM chat_settings_v2 WHERE uid = ";	 Catch:{ Exception -> 0x0060, all -> 0x005e }
                r1.append(r2);	 Catch:{ Exception -> 0x0060, all -> 0x005e }
                r2 = r5;	 Catch:{ Exception -> 0x0060, all -> 0x005e }
                r1.append(r2);	 Catch:{ Exception -> 0x0060, all -> 0x005e }
                r1 = r1.toString();	 Catch:{ Exception -> 0x0060, all -> 0x005e }
                r2 = 0;	 Catch:{ Exception -> 0x0060, all -> 0x005e }
                r3 = new java.lang.Object[r2];	 Catch:{ Exception -> 0x0060, all -> 0x005e }
                r0 = r0.queryFinalized(r1, r3);	 Catch:{ Exception -> 0x0060, all -> 0x005e }
                r1 = 0;	 Catch:{ Exception -> 0x0060, all -> 0x005e }
                r3 = new java.util.ArrayList;	 Catch:{ Exception -> 0x0060, all -> 0x005e }
                r3.<init>();	 Catch:{ Exception -> 0x0060, all -> 0x005e }
                r4 = r0.next();	 Catch:{ Exception -> 0x0060, all -> 0x005e }
                if (r4 == 0) goto L_0x003e;	 Catch:{ Exception -> 0x0060, all -> 0x005e }
            L_0x002c:
                r4 = r0.byteBufferValue(r2);	 Catch:{ Exception -> 0x0060, all -> 0x005e }
                if (r4 == 0) goto L_0x003e;	 Catch:{ Exception -> 0x0060, all -> 0x005e }
            L_0x0032:
                r5 = r4.readInt32(r2);	 Catch:{ Exception -> 0x0060, all -> 0x005e }
                r5 = org.telegram.tgnet.TLRPC.ChatFull.TLdeserialize(r4, r5, r2);	 Catch:{ Exception -> 0x0060, all -> 0x005e }
                r1 = r5;	 Catch:{ Exception -> 0x0060, all -> 0x005e }
                r4.reuse();	 Catch:{ Exception -> 0x0060, all -> 0x005e }
            L_0x003e:
                r0.dispose();	 Catch:{ Exception -> 0x0060, all -> 0x005e }
                r4 = r1;	 Catch:{ Exception -> 0x0060, all -> 0x005e }
                r5 = r1 instanceof org.telegram.tgnet.TLRPC.TL_channelFull;	 Catch:{ Exception -> 0x0060, all -> 0x005e }
                if (r5 == 0) goto L_0x004d;	 Catch:{ Exception -> 0x0060, all -> 0x005e }
            L_0x0047:
                r5 = r1.migrated_from_chat_id;	 Catch:{ Exception -> 0x0060, all -> 0x005e }
                if (r5 == 0) goto L_0x004d;	 Catch:{ Exception -> 0x0060, all -> 0x005e }
            L_0x004b:
                r5 = 1;	 Catch:{ Exception -> 0x0060, all -> 0x005e }
                goto L_0x004e;	 Catch:{ Exception -> 0x0060, all -> 0x005e }
            L_0x004d:
                r5 = r2;	 Catch:{ Exception -> 0x0060, all -> 0x005e }
            L_0x004e:
                r4[r2] = r5;	 Catch:{ Exception -> 0x0060, all -> 0x005e }
                r2 = r0;	 Catch:{ Exception -> 0x0060, all -> 0x005e }
                if (r2 == 0) goto L_0x0059;	 Catch:{ Exception -> 0x0060, all -> 0x005e }
            L_0x0054:
                r2 = r0;	 Catch:{ Exception -> 0x0060, all -> 0x005e }
                r2.countDown();	 Catch:{ Exception -> 0x0060, all -> 0x005e }
            L_0x0059:
                r0 = r0;
                if (r0 == 0) goto L_0x006d;
            L_0x005d:
                goto L_0x0068;
            L_0x005e:
                r0 = move-exception;
                goto L_0x006e;
            L_0x0060:
                r0 = move-exception;
                org.telegram.messenger.FileLog.e(r0);	 Catch:{ Exception -> 0x0060, all -> 0x005e }
                r0 = r0;
                if (r0 == 0) goto L_0x006d;
            L_0x0068:
                r0 = r0;
                r0.countDown();
            L_0x006d:
                return;
            L_0x006e:
                r1 = r0;
                if (r1 == 0) goto L_0x0077;
            L_0x0072:
                r1 = r0;
                r1.countDown();
            L_0x0077:
                throw r0;
                */
                throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.MessagesStorage.46.run():void");
            }
        });
        try {
            countDownLatch.await();
        } catch (Throwable e) {
            FileLog.e(e);
        }
        return result[0];
    }

    public void loadChatInfo(int chat_id, CountDownLatch countDownLatch, boolean force, boolean byChannelUsers) {
        final int i = chat_id;
        final CountDownLatch countDownLatch2 = countDownLatch;
        final boolean z = force;
        final boolean z2 = byChannelUsers;
        this.storageQueue.postRunnable(new Runnable() {
            public void run() {
                ChatFull chatFull;
                ChatFull info;
                ArrayList<User> loadedUsers;
                AnonymousClass47 anonymousClass47 = this;
                MessageObject pinnedMessageObject = null;
                ChatFull info2 = null;
                ArrayList<User> loadedUsers2 = new ArrayList();
                try {
                    SQLiteCursor cursor = MessagesStorage.this.database;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("SELECT info, pinned FROM chat_settings_v2 WHERE uid = ");
                    stringBuilder.append(i);
                    cursor = cursor.queryFinalized(stringBuilder.toString(), new Object[0]);
                    if (cursor.next()) {
                        NativeByteBuffer data = cursor.byteBufferValue(0);
                        if (data != null) {
                            info2 = ChatFull.TLdeserialize(data, data.readInt32(false), false);
                            data.reuse();
                            info2.pinned_msg_id = cursor.intValue(1);
                        }
                    }
                    cursor.dispose();
                    int a;
                    if (info2 instanceof TL_chatFull) {
                        stringBuilder = new StringBuilder();
                        for (a = 0; a < info2.participants.participants.size(); a++) {
                            ChatParticipant c = (ChatParticipant) info2.participants.participants.get(a);
                            if (stringBuilder.length() != 0) {
                                stringBuilder.append(",");
                            }
                            stringBuilder.append(c.user_id);
                        }
                        if (stringBuilder.length() != 0) {
                            MessagesStorage.this.getUsersInternal(stringBuilder.toString(), loadedUsers2);
                        }
                    } else if (info2 instanceof TL_channelFull) {
                        SQLiteDatabase access$000 = MessagesStorage.this.database;
                        StringBuilder stringBuilder2 = new StringBuilder();
                        stringBuilder2.append("SELECT us.data, us.status, cu.data, cu.date FROM channel_users_v2 as cu LEFT JOIN users as us ON us.uid = cu.uid WHERE cu.did = ");
                        stringBuilder2.append(-i);
                        stringBuilder2.append(" ORDER BY cu.date DESC");
                        cursor = access$000.queryFinalized(stringBuilder2.toString(), new Object[0]);
                        info2.participants = new TL_chatParticipants();
                        while (cursor.next()) {
                            User user = null;
                            ChannelParticipant participant = null;
                            try {
                                NativeByteBuffer data2 = cursor.byteBufferValue(0);
                                if (data2 != null) {
                                    user = User.TLdeserialize(data2, data2.readInt32(false), false);
                                    data2.reuse();
                                }
                                data2 = cursor.byteBufferValue(2);
                                if (data2 != null) {
                                    participant = ChannelParticipant.TLdeserialize(data2, data2.readInt32(false), false);
                                    data2.reuse();
                                }
                                if (!(user == null || participant == null)) {
                                    if (user.status != null) {
                                        user.status.expires = cursor.intValue(1);
                                    }
                                    loadedUsers2.add(user);
                                    participant.date = cursor.intValue(3);
                                    TL_chatChannelParticipant chatChannelParticipant = new TL_chatChannelParticipant();
                                    chatChannelParticipant.user_id = participant.user_id;
                                    chatChannelParticipant.date = participant.date;
                                    chatChannelParticipant.inviter_id = participant.inviter_id;
                                    chatChannelParticipant.channelParticipant = participant;
                                    info2.participants.participants.add(chatChannelParticipant);
                                }
                            } catch (Throwable e) {
                                FileLog.e(e);
                            } catch (Throwable e2) {
                                chatFull = e2;
                                info = info2;
                                loadedUsers = loadedUsers2;
                            }
                        }
                        cursor.dispose();
                        stringBuilder = new StringBuilder();
                        for (a = 0; a < info2.bot_info.size(); a++) {
                            BotInfo botInfo = (BotInfo) info2.bot_info.get(a);
                            if (stringBuilder.length() != 0) {
                                stringBuilder.append(",");
                            }
                            stringBuilder.append(botInfo.user_id);
                        }
                        if (stringBuilder.length() != 0) {
                            MessagesStorage.this.getUsersInternal(stringBuilder.toString(), loadedUsers2);
                        }
                    }
                    if (countDownLatch2 != null) {
                        countDownLatch2.countDown();
                    }
                    if ((info2 instanceof TL_channelFull) && info2.pinned_msg_id != 0) {
                        pinnedMessageObject = DataQuery.getInstance(MessagesStorage.this.currentAccount).loadPinnedMessage(i, info2.pinned_msg_id, false);
                    }
                    MessagesController.getInstance(MessagesStorage.this.currentAccount).processChatInfo(i, info2, loadedUsers2, true, z, z2, pinnedMessageObject);
                    if (countDownLatch2 != null) {
                        countDownLatch2.countDown();
                    }
                    loadedUsers = loadedUsers2;
                    return;
                } catch (Throwable e22) {
                    chatFull = info2;
                    FileLog.e(e22);
                    MessagesController.getInstance(MessagesStorage.this.currentAccount).processChatInfo(i, chatFull, loadedUsers2, true, z, z2, null);
                    if (countDownLatch2 != null) {
                        countDownLatch2.countDown();
                    }
                    return;
                } catch (Throwable e222) {
                    loadedUsers = loadedUsers2;
                    info = chatFull;
                    chatFull = e222;
                }
                MessagesController.getInstance(MessagesStorage.this.currentAccount).processChatInfo(i, info, loadedUsers, true, z, z2, null);
                if (countDownLatch2 != null) {
                    countDownLatch2.countDown();
                }
                throw chatFull;
            }
        });
    }

    public void processPendingRead(long dialog_id, long maxPositiveId, long maxNegativeId, int max_date, boolean isChannel) {
        final long j = dialog_id;
        final long j2 = maxPositiveId;
        final boolean z = isChannel;
        final long j3 = maxNegativeId;
        this.storageQueue.postRunnable(new Runnable() {
            public void run() {
                long currentMaxId = 0;
                int unreadCount = 0;
                long last_mid = 0;
                try {
                    SQLiteCursor cursor = MessagesStorage.this.database;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("SELECT unread_count, inbox_max, last_mid FROM dialogs WHERE did = ");
                    stringBuilder.append(j);
                    cursor = cursor.queryFinalized(stringBuilder.toString(), new Object[0]);
                    if (cursor.next()) {
                        unreadCount = cursor.intValue(0);
                        currentMaxId = (long) cursor.intValue(1);
                        last_mid = cursor.longValue(2);
                    }
                    cursor.dispose();
                    MessagesStorage.this.database.beginTransaction();
                    int lower_id = (int) j;
                    SQLitePreparedStatement state;
                    int updatedCount;
                    if (lower_id != 0) {
                        currentMaxId = Math.max(currentMaxId, (long) ((int) j2));
                        if (z) {
                            currentMaxId |= ((long) (-lower_id)) << 32;
                        }
                        state = MessagesStorage.this.database.executeFast("UPDATE messages SET read_state = read_state | 1 WHERE uid = ? AND mid <= ? AND read_state IN(0,2) AND out = 0");
                        state.requery();
                        state.bindLong(1, j);
                        state.bindLong(2, currentMaxId);
                        state.step();
                        state.dispose();
                        if (currentMaxId >= last_mid) {
                            unreadCount = 0;
                        } else {
                            updatedCount = 0;
                            cursor = MessagesStorage.this.database.queryFinalized("SELECT changes()", new Object[0]);
                            if (cursor.next()) {
                                updatedCount = cursor.intValue(0);
                            }
                            cursor.dispose();
                            unreadCount = Math.max(0, unreadCount - updatedCount);
                        }
                    } else {
                        currentMaxId = (long) ((int) j3);
                        state = MessagesStorage.this.database.executeFast("UPDATE messages SET read_state = read_state | 1 WHERE uid = ? AND mid >= ? AND read_state IN(0,2) AND out = 0");
                        state.requery();
                        state.bindLong(1, j);
                        state.bindLong(2, currentMaxId);
                        state.step();
                        state.dispose();
                        if (currentMaxId <= last_mid) {
                            unreadCount = 0;
                        } else {
                            updatedCount = 0;
                            cursor = MessagesStorage.this.database.queryFinalized("SELECT changes()", new Object[0]);
                            if (cursor.next()) {
                                updatedCount = cursor.intValue(0);
                            }
                            cursor.dispose();
                            unreadCount = Math.max(0, unreadCount - updatedCount);
                        }
                    }
                    SQLitePreparedStatement state2 = MessagesStorage.this.database.executeFast("UPDATE dialogs SET unread_count = ?, inbox_max = ? WHERE did = ?");
                    state2.requery();
                    state2.bindInteger(1, unreadCount);
                    state2.bindInteger(2, (int) currentMaxId);
                    state2.bindLong(3, j);
                    state2.step();
                    state2.dispose();
                    MessagesStorage.this.database.commitTransaction();
                } catch (Throwable e) {
                    FileLog.e(e);
                }
            }
        });
    }

    public void putContacts(ArrayList<TL_contact> contacts, final boolean deleteAll) {
        if (!contacts.isEmpty()) {
            final ArrayList<TL_contact> contactsCopy = new ArrayList(contacts);
            this.storageQueue.postRunnable(new Runnable() {
                public void run() {
                    try {
                        if (deleteAll) {
                            MessagesStorage.this.database.executeFast("DELETE FROM contacts WHERE 1").stepThis().dispose();
                        }
                        MessagesStorage.this.database.beginTransaction();
                        SQLitePreparedStatement state = MessagesStorage.this.database.executeFast("REPLACE INTO contacts VALUES(?, ?)");
                        for (int a = 0; a < contactsCopy.size(); a++) {
                            TL_contact contact = (TL_contact) contactsCopy.get(a);
                            state.requery();
                            state.bindInteger(1, contact.user_id);
                            state.bindInteger(2, contact.mutual);
                            state.step();
                        }
                        state.dispose();
                        MessagesStorage.this.database.commitTransaction();
                    } catch (Throwable e) {
                        FileLog.e(e);
                    }
                }
            });
        }
    }

    public void deleteContacts(final ArrayList<Integer> uids) {
        if (uids != null) {
            if (!uids.isEmpty()) {
                this.storageQueue.postRunnable(new Runnable() {
                    public void run() {
                        try {
                            String ids = TextUtils.join(",", uids);
                            SQLiteDatabase access$000 = MessagesStorage.this.database;
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append("DELETE FROM contacts WHERE uid IN(");
                            stringBuilder.append(ids);
                            stringBuilder.append(")");
                            access$000.executeFast(stringBuilder.toString()).stepThis().dispose();
                        } catch (Throwable e) {
                            FileLog.e(e);
                        }
                    }
                });
            }
        }
    }

    public void applyPhoneBookUpdates(final String adds, final String deletes) {
        if (adds.length() != 0 || deletes.length() != 0) {
            this.storageQueue.postRunnable(new Runnable() {
                public void run() {
                    try {
                        if (adds.length() != 0) {
                            MessagesStorage.this.database.executeFast(String.format(Locale.US, "UPDATE user_phones_v7 SET deleted = 0 WHERE sphone IN(%s)", new Object[]{adds})).stepThis().dispose();
                        }
                        if (deletes.length() != 0) {
                            MessagesStorage.this.database.executeFast(String.format(Locale.US, "UPDATE user_phones_v7 SET deleted = 1 WHERE sphone IN(%s)", new Object[]{deletes})).stepThis().dispose();
                        }
                    } catch (Throwable e) {
                        FileLog.e(e);
                    }
                }
            });
        }
    }

    public void putCachedPhoneBook(final HashMap<String, Contact> contactHashMap, final boolean migrate) {
        if (contactHashMap != null) {
            if (!contactHashMap.isEmpty() || migrate) {
                this.storageQueue.postRunnable(new Runnable() {
                    public void run() {
                        try {
                            if (BuildVars.LOGS_ENABLED) {
                                StringBuilder stringBuilder = new StringBuilder();
                                stringBuilder.append(MessagesStorage.this.currentAccount);
                                stringBuilder.append(" save contacts to db ");
                                stringBuilder.append(contactHashMap.size());
                                FileLog.d(stringBuilder.toString());
                            }
                            MessagesStorage.this.database.executeFast("DELETE FROM user_contacts_v7 WHERE 1").stepThis().dispose();
                            MessagesStorage.this.database.executeFast("DELETE FROM user_phones_v7 WHERE 1").stepThis().dispose();
                            MessagesStorage.this.database.beginTransaction();
                            SQLitePreparedStatement state = MessagesStorage.this.database.executeFast("REPLACE INTO user_contacts_v7 VALUES(?, ?, ?, ?, ?)");
                            SQLitePreparedStatement state2 = MessagesStorage.this.database.executeFast("REPLACE INTO user_phones_v7 VALUES(?, ?, ?, ?)");
                            Iterator it = contactHashMap.entrySet().iterator();
                            while (true) {
                                int a = 0;
                                if (!it.hasNext()) {
                                    break;
                                }
                                Contact contact = (Contact) ((Entry) it.next()).getValue();
                                if (!contact.phones.isEmpty()) {
                                    if (!contact.shortPhones.isEmpty()) {
                                        state.requery();
                                        state.bindString(1, contact.key);
                                        state.bindInteger(2, contact.contact_id);
                                        state.bindString(3, contact.first_name);
                                        state.bindString(4, contact.last_name);
                                        state.bindInteger(5, contact.imported);
                                        state.step();
                                        while (a < contact.phones.size()) {
                                            state2.requery();
                                            state2.bindString(1, contact.key);
                                            state2.bindString(2, (String) contact.phones.get(a));
                                            state2.bindString(3, (String) contact.shortPhones.get(a));
                                            state2.bindInteger(4, ((Integer) contact.phoneDeleted.get(a)).intValue());
                                            state2.step();
                                            a++;
                                        }
                                    }
                                }
                            }
                            state.dispose();
                            state2.dispose();
                            MessagesStorage.this.database.commitTransaction();
                            if (migrate) {
                                MessagesStorage.this.database.executeFast("DROP TABLE IF EXISTS user_contacts_v6;").stepThis().dispose();
                                MessagesStorage.this.database.executeFast("DROP TABLE IF EXISTS user_phones_v6;").stepThis().dispose();
                                MessagesStorage.this.getCachedPhoneBook(false);
                            }
                        } catch (Throwable e) {
                            FileLog.e(e);
                        }
                    }
                });
            }
        }
    }

    public void getCachedPhoneBook(final boolean byError) {
        this.storageQueue.postRunnable(new Runnable() {
            public void run() {
                /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.MessagesStorage.53.run():void
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:116)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:249)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:256)
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
                r1 = r27;
                r2 = 0;
                r4 = 4;
                r5 = 3;
                r6 = 6;
                r7 = 2;
                r8 = 1;
                r9 = 8;
                r10 = 5000; // 0x1388 float:7.006E-42 double:2.4703E-320;
                r11 = 0;
                r12 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Throwable -> 0x010f, all -> 0x010a }
                r12 = r12.database;	 Catch:{ Throwable -> 0x010f, all -> 0x010a }
                r13 = "SELECT name FROM sqlite_master WHERE type='table' AND name='user_contacts_v6'";	 Catch:{ Throwable -> 0x010f, all -> 0x010a }
                r14 = new java.lang.Object[r11];	 Catch:{ Throwable -> 0x010f, all -> 0x010a }
                r12 = r12.queryFinalized(r13, r14);	 Catch:{ Throwable -> 0x010f, all -> 0x010a }
                r2 = r12;	 Catch:{ Throwable -> 0x010f, all -> 0x010a }
                r12 = r2.next();	 Catch:{ Throwable -> 0x010f, all -> 0x010a }
                r2.dispose();	 Catch:{ Throwable -> 0x010f, all -> 0x010a }
                r2 = 0;	 Catch:{ Throwable -> 0x010f, all -> 0x010a }
                if (r12 == 0) goto L_0x0104;	 Catch:{ Throwable -> 0x010f, all -> 0x010a }
            L_0x0026:
                r13 = 16;	 Catch:{ Throwable -> 0x010f, all -> 0x010a }
                r14 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Throwable -> 0x010f, all -> 0x010a }
                r14 = r14.database;	 Catch:{ Throwable -> 0x010f, all -> 0x010a }
                r15 = "SELECT COUNT(uid) FROM user_contacts_v6 WHERE 1";	 Catch:{ Throwable -> 0x010f, all -> 0x010a }
                r3 = new java.lang.Object[r11];	 Catch:{ Throwable -> 0x010f, all -> 0x010a }
                r3 = r14.queryFinalized(r15, r3);	 Catch:{ Throwable -> 0x010f, all -> 0x010a }
                r2 = r3;	 Catch:{ Throwable -> 0x010f, all -> 0x010a }
                r3 = r2.next();	 Catch:{ Throwable -> 0x010f, all -> 0x010a }
                if (r3 == 0) goto L_0x0046;	 Catch:{ Throwable -> 0x010f, all -> 0x010a }
            L_0x003d:
                r3 = r2.intValue(r11);	 Catch:{ Throwable -> 0x010f, all -> 0x010a }
                r3 = java.lang.Math.min(r10, r3);	 Catch:{ Throwable -> 0x010f, all -> 0x010a }
                r13 = r3;	 Catch:{ Throwable -> 0x010f, all -> 0x010a }
            L_0x0046:
                r2.dispose();	 Catch:{ Throwable -> 0x010f, all -> 0x010a }
                r3 = new android.util.SparseArray;	 Catch:{ Throwable -> 0x010f, all -> 0x010a }
                r3.<init>(r13);	 Catch:{ Throwable -> 0x010f, all -> 0x010a }
                r14 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Throwable -> 0x010f, all -> 0x010a }
                r14 = r14.database;	 Catch:{ Throwable -> 0x010f, all -> 0x010a }
                r15 = "SELECT us.uid, us.fname, us.sname, up.phone, up.sphone, up.deleted, us.imported FROM user_contacts_v6 as us LEFT JOIN user_phones_v6 as up ON us.uid = up.uid WHERE 1";	 Catch:{ Throwable -> 0x010f, all -> 0x010a }
                r10 = new java.lang.Object[r11];	 Catch:{ Throwable -> 0x010f, all -> 0x010a }
                r10 = r14.queryFinalized(r15, r10);	 Catch:{ Throwable -> 0x010f, all -> 0x010a }
                r2 = r10;	 Catch:{ Throwable -> 0x010f, all -> 0x010a }
            L_0x005d:
                r10 = r2.next();	 Catch:{ Throwable -> 0x010f, all -> 0x010a }
                if (r10 == 0) goto L_0x00ed;	 Catch:{ Throwable -> 0x010f, all -> 0x010a }
            L_0x0063:
                r10 = r2.intValue(r11);	 Catch:{ Throwable -> 0x010f, all -> 0x010a }
                r14 = r3.get(r10);	 Catch:{ Throwable -> 0x010f, all -> 0x010a }
                r14 = (org.telegram.messenger.ContactsController.Contact) r14;	 Catch:{ Throwable -> 0x010f, all -> 0x010a }
                if (r14 != 0) goto L_0x009c;	 Catch:{ Throwable -> 0x010f, all -> 0x010a }
            L_0x006f:
                r15 = new org.telegram.messenger.ContactsController$Contact;	 Catch:{ Throwable -> 0x010f, all -> 0x010a }
                r15.<init>();	 Catch:{ Throwable -> 0x010f, all -> 0x010a }
                r14 = r15;	 Catch:{ Throwable -> 0x010f, all -> 0x010a }
                r15 = r2.stringValue(r8);	 Catch:{ Throwable -> 0x010f, all -> 0x010a }
                r14.first_name = r15;	 Catch:{ Throwable -> 0x010f, all -> 0x010a }
                r15 = r2.stringValue(r7);	 Catch:{ Throwable -> 0x010f, all -> 0x010a }
                r14.last_name = r15;	 Catch:{ Throwable -> 0x010f, all -> 0x010a }
                r15 = r2.intValue(r6);	 Catch:{ Throwable -> 0x010f, all -> 0x010a }
                r14.imported = r15;	 Catch:{ Throwable -> 0x010f, all -> 0x010a }
                r15 = r14.first_name;	 Catch:{ Throwable -> 0x010f, all -> 0x010a }
                if (r15 != 0) goto L_0x008f;	 Catch:{ Throwable -> 0x010f, all -> 0x010a }
            L_0x008b:
                r15 = "";	 Catch:{ Throwable -> 0x010f, all -> 0x010a }
                r14.first_name = r15;	 Catch:{ Throwable -> 0x010f, all -> 0x010a }
            L_0x008f:
                r15 = r14.last_name;	 Catch:{ Throwable -> 0x010f, all -> 0x010a }
                if (r15 != 0) goto L_0x0097;	 Catch:{ Throwable -> 0x010f, all -> 0x010a }
            L_0x0093:
                r15 = "";	 Catch:{ Throwable -> 0x010f, all -> 0x010a }
                r14.last_name = r15;	 Catch:{ Throwable -> 0x010f, all -> 0x010a }
            L_0x0097:
                r14.contact_id = r10;	 Catch:{ Throwable -> 0x010f, all -> 0x010a }
                r3.put(r10, r14);	 Catch:{ Throwable -> 0x010f, all -> 0x010a }
            L_0x009c:
                r15 = r2.stringValue(r5);	 Catch:{ Throwable -> 0x010f, all -> 0x010a }
                if (r15 != 0) goto L_0x00a3;	 Catch:{ Throwable -> 0x010f, all -> 0x010a }
            L_0x00a2:
                goto L_0x005d;	 Catch:{ Throwable -> 0x010f, all -> 0x010a }
            L_0x00a3:
                r6 = r14.phones;	 Catch:{ Throwable -> 0x010f, all -> 0x010a }
                r6.add(r15);	 Catch:{ Throwable -> 0x010f, all -> 0x010a }
                r6 = r2.stringValue(r4);	 Catch:{ Throwable -> 0x010f, all -> 0x010a }
                if (r6 != 0) goto L_0x00b1;	 Catch:{ Throwable -> 0x010f, all -> 0x010a }
            L_0x00af:
                r6 = 6;	 Catch:{ Throwable -> 0x010f, all -> 0x010a }
                goto L_0x005d;	 Catch:{ Throwable -> 0x010f, all -> 0x010a }
            L_0x00b1:
                r4 = r6.length();	 Catch:{ Throwable -> 0x010f, all -> 0x010a }
                if (r4 != r9) goto L_0x00c2;	 Catch:{ Throwable -> 0x010f, all -> 0x010a }
            L_0x00b7:
                r4 = r15.length();	 Catch:{ Throwable -> 0x010f, all -> 0x010a }
                if (r4 == r9) goto L_0x00c2;	 Catch:{ Throwable -> 0x010f, all -> 0x010a }
            L_0x00bd:
                r4 = org.telegram.PhoneFormat.PhoneFormat.stripExceptNumbers(r15);	 Catch:{ Throwable -> 0x010f, all -> 0x010a }
                r6 = r4;	 Catch:{ Throwable -> 0x010f, all -> 0x010a }
            L_0x00c2:
                r4 = r14.shortPhones;	 Catch:{ Throwable -> 0x010f, all -> 0x010a }
                r4.add(r6);	 Catch:{ Throwable -> 0x010f, all -> 0x010a }
                r4 = r14.phoneDeleted;	 Catch:{ Throwable -> 0x010f, all -> 0x010a }
                r9 = 5;	 Catch:{ Throwable -> 0x010f, all -> 0x010a }
                r5 = r2.intValue(r9);	 Catch:{ Throwable -> 0x010f, all -> 0x010a }
                r5 = java.lang.Integer.valueOf(r5);	 Catch:{ Throwable -> 0x010f, all -> 0x010a }
                r4.add(r5);	 Catch:{ Throwable -> 0x010f, all -> 0x010a }
                r4 = r14.phoneTypes;	 Catch:{ Throwable -> 0x010f, all -> 0x010a }
                r5 = "";	 Catch:{ Throwable -> 0x010f, all -> 0x010a }
                r4.add(r5);	 Catch:{ Throwable -> 0x010f, all -> 0x010a }
                r4 = r3.size();	 Catch:{ Throwable -> 0x010f, all -> 0x010a }
                r5 = 5000; // 0x1388 float:7.006E-42 double:2.4703E-320;	 Catch:{ Throwable -> 0x010f, all -> 0x010a }
                if (r4 != r5) goto L_0x00e5;	 Catch:{ Throwable -> 0x010f, all -> 0x010a }
            L_0x00e4:
                goto L_0x00ed;	 Catch:{ Throwable -> 0x010f, all -> 0x010a }
                r4 = 4;	 Catch:{ Throwable -> 0x010f, all -> 0x010a }
                r5 = 3;	 Catch:{ Throwable -> 0x010f, all -> 0x010a }
                r6 = 6;	 Catch:{ Throwable -> 0x010f, all -> 0x010a }
                r9 = 8;	 Catch:{ Throwable -> 0x010f, all -> 0x010a }
                goto L_0x005d;	 Catch:{ Throwable -> 0x010f, all -> 0x010a }
            L_0x00ed:
                r2.dispose();	 Catch:{ Throwable -> 0x010f, all -> 0x010a }
                r2 = 0;	 Catch:{ Throwable -> 0x010f, all -> 0x010a }
                r4 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Throwable -> 0x010f, all -> 0x010a }
                r4 = r4.currentAccount;	 Catch:{ Throwable -> 0x010f, all -> 0x010a }
                r4 = org.telegram.messenger.ContactsController.getInstance(r4);	 Catch:{ Throwable -> 0x010f, all -> 0x010a }
                r4.migratePhoneBookToV7(r3);	 Catch:{ Throwable -> 0x010f, all -> 0x010a }
                if (r2 == 0) goto L_0x0103;
                r2.dispose();
                return;
            L_0x0104:
                if (r2 == 0) goto L_0x011b;
                r2.dispose();
                goto L_0x011b;
            L_0x010a:
                r0 = move-exception;
                r3 = r2;
                r2 = r0;
                goto L_0x02b2;
            L_0x010f:
                r0 = move-exception;
                r3 = r2;
                r2 = r0;
                org.telegram.messenger.FileLog.e(r2);	 Catch:{ all -> 0x02af }
                if (r3 == 0) goto L_0x011a;
                r3.dispose();
                r2 = r3;
                r3 = 16;
                r4 = 0;
                r5 = r11;
                r6 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Throwable -> 0x0172, all -> 0x016b }
                r6 = r6.database;	 Catch:{ Throwable -> 0x0172, all -> 0x016b }
                r9 = "SELECT COUNT(key) FROM user_contacts_v7 WHERE 1";	 Catch:{ Throwable -> 0x0172, all -> 0x016b }
                r10 = new java.lang.Object[r11];	 Catch:{ Throwable -> 0x0172, all -> 0x016b }
                r6 = r6.queryFinalized(r9, r10);	 Catch:{ Throwable -> 0x0172, all -> 0x016b }
                r2 = r6;	 Catch:{ Throwable -> 0x0172, all -> 0x016b }
                r6 = r2.next();	 Catch:{ Throwable -> 0x0172, all -> 0x016b }
                if (r6 == 0) goto L_0x0165;	 Catch:{ Throwable -> 0x0172, all -> 0x016b }
                r6 = r2.intValue(r11);	 Catch:{ Throwable -> 0x0172, all -> 0x016b }
                r4 = r6;	 Catch:{ Throwable -> 0x0172, all -> 0x016b }
                r6 = 5000; // 0x1388 float:7.006E-42 double:2.4703E-320;	 Catch:{ Throwable -> 0x0172, all -> 0x016b }
                r9 = java.lang.Math.min(r6, r4);	 Catch:{ Throwable -> 0x0172, all -> 0x016b }
                r3 = r9;	 Catch:{ Throwable -> 0x0172, all -> 0x016b }
                if (r4 <= r6) goto L_0x0144;	 Catch:{ Throwable -> 0x0172, all -> 0x016b }
                r5 = r4 + -5000;	 Catch:{ Throwable -> 0x0172, all -> 0x016b }
                r6 = org.telegram.messenger.BuildVars.LOGS_ENABLED;	 Catch:{ Throwable -> 0x0172, all -> 0x016b }
                if (r6 == 0) goto L_0x0165;	 Catch:{ Throwable -> 0x0172, all -> 0x016b }
                r6 = new java.lang.StringBuilder;	 Catch:{ Throwable -> 0x0172, all -> 0x016b }
                r6.<init>();	 Catch:{ Throwable -> 0x0172, all -> 0x016b }
                r9 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Throwable -> 0x0172, all -> 0x016b }
                r9 = r9.currentAccount;	 Catch:{ Throwable -> 0x0172, all -> 0x016b }
                r6.append(r9);	 Catch:{ Throwable -> 0x0172, all -> 0x016b }
                r9 = " current cached contacts count = ";	 Catch:{ Throwable -> 0x0172, all -> 0x016b }
                r6.append(r9);	 Catch:{ Throwable -> 0x0172, all -> 0x016b }
                r6.append(r4);	 Catch:{ Throwable -> 0x0172, all -> 0x016b }
                r6 = r6.toString();	 Catch:{ Throwable -> 0x0172, all -> 0x016b }
                org.telegram.messenger.FileLog.d(r6);	 Catch:{ Throwable -> 0x0172, all -> 0x016b }
                if (r2 == 0) goto L_0x0182;
                r2.dispose();
                goto L_0x0182;
            L_0x016b:
                r0 = move-exception;
                r6 = r4;
                r4 = r3;
                r3 = r2;
                r2 = r0;
                goto L_0x02a9;
            L_0x0172:
                r0 = move-exception;
                r6 = r4;
                r4 = r3;
                r3 = r2;
                r2 = r0;
                org.telegram.messenger.FileLog.e(r2);	 Catch:{ all -> 0x02a6 }
                if (r3 == 0) goto L_0x017f;
                r3.dispose();
                r2 = r3;
                r3 = r4;
                r4 = r6;
                r6 = new java.util.HashMap;
                r6.<init>(r3);
                if (r5 == 0) goto L_0x01b2;
                r9 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x01ad, all -> 0x01a8 }
                r9 = r9.database;	 Catch:{ Exception -> 0x01ad, all -> 0x01a8 }
                r10 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x01ad, all -> 0x01a8 }
                r10.<init>();	 Catch:{ Exception -> 0x01ad, all -> 0x01a8 }
                r12 = "SELECT us.key, us.uid, us.fname, us.sname, up.phone, up.sphone, up.deleted, us.imported FROM user_contacts_v7 as us LEFT JOIN user_phones_v7 as up ON us.key = up.key WHERE 1 LIMIT 0,";	 Catch:{ Exception -> 0x01ad, all -> 0x01a8 }
                r10.append(r12);	 Catch:{ Exception -> 0x01ad, all -> 0x01a8 }
                r10.append(r4);	 Catch:{ Exception -> 0x01ad, all -> 0x01a8 }
                r10 = r10.toString();	 Catch:{ Exception -> 0x01ad, all -> 0x01a8 }
                r12 = new java.lang.Object[r11];	 Catch:{ Exception -> 0x01ad, all -> 0x01a8 }
                r9 = r9.queryFinalized(r10, r12);	 Catch:{ Exception -> 0x01ad, all -> 0x01a8 }
                r2 = r9;	 Catch:{ Exception -> 0x01ad, all -> 0x01a8 }
                goto L_0x01c1;	 Catch:{ Exception -> 0x01ad, all -> 0x01a8 }
            L_0x01a8:
                r0 = move-exception;	 Catch:{ Exception -> 0x01ad, all -> 0x01a8 }
                r7 = r2;	 Catch:{ Exception -> 0x01ad, all -> 0x01a8 }
                r2 = r0;	 Catch:{ Exception -> 0x01ad, all -> 0x01a8 }
                goto L_0x02a0;	 Catch:{ Exception -> 0x01ad, all -> 0x01a8 }
            L_0x01ad:
                r0 = move-exception;	 Catch:{ Exception -> 0x01ad, all -> 0x01a8 }
                r7 = r2;	 Catch:{ Exception -> 0x01ad, all -> 0x01a8 }
                r2 = r0;	 Catch:{ Exception -> 0x01ad, all -> 0x01a8 }
                goto L_0x0272;	 Catch:{ Exception -> 0x01ad, all -> 0x01a8 }
                r9 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x01ad, all -> 0x01a8 }
                r9 = r9.database;	 Catch:{ Exception -> 0x01ad, all -> 0x01a8 }
                r10 = "SELECT us.key, us.uid, us.fname, us.sname, up.phone, up.sphone, up.deleted, us.imported FROM user_contacts_v7 as us LEFT JOIN user_phones_v7 as up ON us.key = up.key WHERE 1";	 Catch:{ Exception -> 0x01ad, all -> 0x01a8 }
                r12 = new java.lang.Object[r11];	 Catch:{ Exception -> 0x01ad, all -> 0x01a8 }
                r9 = r9.queryFinalized(r10, r12);	 Catch:{ Exception -> 0x01ad, all -> 0x01a8 }
                r2 = r9;	 Catch:{ Exception -> 0x01ad, all -> 0x01a8 }
                r9 = r2.next();	 Catch:{ Exception -> 0x01ad, all -> 0x01a8 }
                if (r9 == 0) goto L_0x0268;	 Catch:{ Exception -> 0x01ad, all -> 0x01a8 }
                r9 = r2.stringValue(r11);	 Catch:{ Exception -> 0x01ad, all -> 0x01a8 }
                r10 = r6.get(r9);	 Catch:{ Exception -> 0x01ad, all -> 0x01a8 }
                r10 = (org.telegram.messenger.ContactsController.Contact) r10;	 Catch:{ Exception -> 0x01ad, all -> 0x01a8 }
                if (r10 != 0) goto L_0x0207;	 Catch:{ Exception -> 0x01ad, all -> 0x01a8 }
                r12 = new org.telegram.messenger.ContactsController$Contact;	 Catch:{ Exception -> 0x01ad, all -> 0x01a8 }
                r12.<init>();	 Catch:{ Exception -> 0x01ad, all -> 0x01a8 }
                r10 = r12;	 Catch:{ Exception -> 0x01ad, all -> 0x01a8 }
                r12 = r2.intValue(r8);	 Catch:{ Exception -> 0x01ad, all -> 0x01a8 }
                r10.contact_id = r12;	 Catch:{ Exception -> 0x01ad, all -> 0x01a8 }
                r12 = r2.stringValue(r7);	 Catch:{ Exception -> 0x01ad, all -> 0x01a8 }
                r10.first_name = r12;	 Catch:{ Exception -> 0x01ad, all -> 0x01a8 }
                r12 = 3;	 Catch:{ Exception -> 0x01ad, all -> 0x01a8 }
                r13 = r2.stringValue(r12);	 Catch:{ Exception -> 0x01ad, all -> 0x01a8 }
                r10.last_name = r13;	 Catch:{ Exception -> 0x01ad, all -> 0x01a8 }
                r13 = 7;	 Catch:{ Exception -> 0x01ad, all -> 0x01a8 }
                r13 = r2.intValue(r13);	 Catch:{ Exception -> 0x01ad, all -> 0x01a8 }
                r10.imported = r13;	 Catch:{ Exception -> 0x01ad, all -> 0x01a8 }
                r13 = r10.first_name;	 Catch:{ Exception -> 0x01ad, all -> 0x01a8 }
                if (r13 != 0) goto L_0x01fb;	 Catch:{ Exception -> 0x01ad, all -> 0x01a8 }
                r13 = "";	 Catch:{ Exception -> 0x01ad, all -> 0x01a8 }
                r10.first_name = r13;	 Catch:{ Exception -> 0x01ad, all -> 0x01a8 }
                r13 = r10.last_name;	 Catch:{ Exception -> 0x01ad, all -> 0x01a8 }
                if (r13 != 0) goto L_0x0203;	 Catch:{ Exception -> 0x01ad, all -> 0x01a8 }
                r13 = "";	 Catch:{ Exception -> 0x01ad, all -> 0x01a8 }
                r10.last_name = r13;	 Catch:{ Exception -> 0x01ad, all -> 0x01a8 }
                r6.put(r9, r10);	 Catch:{ Exception -> 0x01ad, all -> 0x01a8 }
                goto L_0x0208;	 Catch:{ Exception -> 0x01ad, all -> 0x01a8 }
                r12 = 3;	 Catch:{ Exception -> 0x01ad, all -> 0x01a8 }
                r13 = 4;	 Catch:{ Exception -> 0x01ad, all -> 0x01a8 }
                r14 = r2.stringValue(r13);	 Catch:{ Exception -> 0x01ad, all -> 0x01a8 }
                if (r14 != 0) goto L_0x0215;	 Catch:{ Exception -> 0x01ad, all -> 0x01a8 }
                r12 = 6;	 Catch:{ Exception -> 0x01ad, all -> 0x01a8 }
                r13 = 5000; // 0x1388 float:7.006E-42 double:2.4703E-320;	 Catch:{ Exception -> 0x01ad, all -> 0x01a8 }
                r15 = 5;	 Catch:{ Exception -> 0x01ad, all -> 0x01a8 }
                goto L_0x0264;	 Catch:{ Exception -> 0x01ad, all -> 0x01a8 }
                r15 = r10.phones;	 Catch:{ Exception -> 0x01ad, all -> 0x01a8 }
                r15.add(r14);	 Catch:{ Exception -> 0x01ad, all -> 0x01a8 }
                r15 = 5;	 Catch:{ Exception -> 0x01ad, all -> 0x01a8 }
                r16 = r2.stringValue(r15);	 Catch:{ Exception -> 0x01ad, all -> 0x01a8 }
                r25 = r16;	 Catch:{ Exception -> 0x01ad, all -> 0x01a8 }
                r7 = r25;	 Catch:{ Exception -> 0x01ad, all -> 0x01a8 }
                if (r7 != 0) goto L_0x022a;	 Catch:{ Exception -> 0x01ad, all -> 0x01a8 }
                r12 = 6;	 Catch:{ Exception -> 0x01ad, all -> 0x01a8 }
                r13 = 5000; // 0x1388 float:7.006E-42 double:2.4703E-320;	 Catch:{ Exception -> 0x01ad, all -> 0x01a8 }
                goto L_0x0264;	 Catch:{ Exception -> 0x01ad, all -> 0x01a8 }
                r11 = r7.length();	 Catch:{ Exception -> 0x01ad, all -> 0x01a8 }
                r12 = 8;	 Catch:{ Exception -> 0x01ad, all -> 0x01a8 }
                if (r11 != r12) goto L_0x0240;	 Catch:{ Exception -> 0x01ad, all -> 0x01a8 }
                r11 = r14.length();	 Catch:{ Exception -> 0x01ad, all -> 0x01a8 }
                if (r11 == r12) goto L_0x0240;	 Catch:{ Exception -> 0x01ad, all -> 0x01a8 }
                r11 = org.telegram.PhoneFormat.PhoneFormat.stripExceptNumbers(r14);	 Catch:{ Exception -> 0x01ad, all -> 0x01a8 }
                r25 = r11;	 Catch:{ Exception -> 0x01ad, all -> 0x01a8 }
                r7 = r25;	 Catch:{ Exception -> 0x01ad, all -> 0x01a8 }
                r11 = r10.shortPhones;	 Catch:{ Exception -> 0x01ad, all -> 0x01a8 }
                r11.add(r7);	 Catch:{ Exception -> 0x01ad, all -> 0x01a8 }
                r11 = r10.phoneDeleted;	 Catch:{ Exception -> 0x01ad, all -> 0x01a8 }
                r12 = 6;	 Catch:{ Exception -> 0x01ad, all -> 0x01a8 }
                r13 = r2.intValue(r12);	 Catch:{ Exception -> 0x01ad, all -> 0x01a8 }
                r13 = java.lang.Integer.valueOf(r13);	 Catch:{ Exception -> 0x01ad, all -> 0x01a8 }
                r11.add(r13);	 Catch:{ Exception -> 0x01ad, all -> 0x01a8 }
                r11 = r10.phoneTypes;	 Catch:{ Exception -> 0x01ad, all -> 0x01a8 }
                r13 = "";	 Catch:{ Exception -> 0x01ad, all -> 0x01a8 }
                r11.add(r13);	 Catch:{ Exception -> 0x01ad, all -> 0x01a8 }
                r11 = r6.size();	 Catch:{ Exception -> 0x01ad, all -> 0x01a8 }
                r13 = 5000; // 0x1388 float:7.006E-42 double:2.4703E-320;	 Catch:{ Exception -> 0x01ad, all -> 0x01a8 }
                if (r11 != r13) goto L_0x0263;	 Catch:{ Exception -> 0x01ad, all -> 0x01a8 }
                goto L_0x0268;	 Catch:{ Exception -> 0x01ad, all -> 0x01a8 }
                r7 = 2;	 Catch:{ Exception -> 0x01ad, all -> 0x01a8 }
                r11 = 0;	 Catch:{ Exception -> 0x01ad, all -> 0x01a8 }
                goto L_0x01c1;	 Catch:{ Exception -> 0x01ad, all -> 0x01a8 }
                r2.dispose();	 Catch:{ Exception -> 0x01ad, all -> 0x01a8 }
                r2 = 0;
                if (r2 == 0) goto L_0x027f;
                r2.dispose();
                goto L_0x027f;
                r6.clear();	 Catch:{ all -> 0x029d }
                org.telegram.messenger.FileLog.e(r2);	 Catch:{ all -> 0x029d }
                if (r7 == 0) goto L_0x027e;
                r7.dispose();
                r2 = r7;
                r7 = org.telegram.messenger.MessagesStorage.this;
                r7 = r7.currentAccount;
                r17 = org.telegram.messenger.ContactsController.getInstance(r7);
                r19 = 1;
                r20 = 1;
                r21 = 0;
                r22 = 0;
                r7 = r3;
                r23 = r7 ^ 1;
                r24 = 0;
                r18 = r6;
                r17.performSyncPhoneBook(r18, r19, r20, r21, r22, r23, r24);
                return;
            L_0x029d:
                r0 = move-exception;
                goto L_0x01aa;
                if (r7 == 0) goto L_0x02a5;
                r7.dispose();
                throw r2;
            L_0x02a6:
                r0 = move-exception;
                goto L_0x016f;
                if (r3 == 0) goto L_0x02ae;
                r3.dispose();
                throw r2;
            L_0x02af:
                r0 = move-exception;
                goto L_0x010c;
                if (r3 == 0) goto L_0x02b7;
                r3.dispose();
                throw r2;
                */
                throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.MessagesStorage.53.run():void");
            }
        });
    }

    public void getContacts() {
        this.storageQueue.postRunnable(new Runnable() {
            public void run() {
                ArrayList<TL_contact> contacts = new ArrayList();
                ArrayList<User> users = new ArrayList();
                try {
                    SQLiteCursor cursor = MessagesStorage.this.database.queryFinalized("SELECT * FROM contacts WHERE 1", new Object[0]);
                    StringBuilder uids = new StringBuilder();
                    while (cursor.next()) {
                        int user_id = cursor.intValue(0);
                        TL_contact contact = new TL_contact();
                        contact.user_id = user_id;
                        contact.mutual = cursor.intValue(1) == 1;
                        if (uids.length() != 0) {
                            uids.append(",");
                        }
                        contacts.add(contact);
                        uids.append(contact.user_id);
                    }
                    cursor.dispose();
                    if (uids.length() != 0) {
                        MessagesStorage.this.getUsersInternal(uids.toString(), users);
                    }
                } catch (Throwable e) {
                    contacts.clear();
                    users.clear();
                    FileLog.e(e);
                }
                ContactsController.getInstance(MessagesStorage.this.currentAccount).processLoadedContacts(contacts, users, 1);
            }
        });
    }

    public void getUnsentMessages(final int count) {
        this.storageQueue.postRunnable(new Runnable() {
            public void run() {
                /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.MessagesStorage.55.run():void
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:116)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:249)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:256)
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
                r1 = r23;
                r2 = new android.util.SparseArray;	 Catch:{ Exception -> 0x01f5 }
                r2.<init>();	 Catch:{ Exception -> 0x01f5 }
                r3 = new java.util.ArrayList;	 Catch:{ Exception -> 0x01f5 }
                r3.<init>();	 Catch:{ Exception -> 0x01f5 }
                r4 = new java.util.ArrayList;	 Catch:{ Exception -> 0x01f5 }
                r4.<init>();	 Catch:{ Exception -> 0x01f5 }
                r5 = new java.util.ArrayList;	 Catch:{ Exception -> 0x01f5 }
                r5.<init>();	 Catch:{ Exception -> 0x01f5 }
                r6 = new java.util.ArrayList;	 Catch:{ Exception -> 0x01f5 }
                r6.<init>();	 Catch:{ Exception -> 0x01f5 }
                r7 = new java.util.ArrayList;	 Catch:{ Exception -> 0x01f5 }
                r7.<init>();	 Catch:{ Exception -> 0x01f5 }
                r8 = new java.util.ArrayList;	 Catch:{ Exception -> 0x01f5 }
                r8.<init>();	 Catch:{ Exception -> 0x01f5 }
                r9 = new java.util.ArrayList;	 Catch:{ Exception -> 0x01f5 }
                r9.<init>();	 Catch:{ Exception -> 0x01f5 }
                r10 = new java.util.ArrayList;	 Catch:{ Exception -> 0x01f5 }
                r10.<init>();	 Catch:{ Exception -> 0x01f5 }
                r11 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x01f5 }
                r11 = r11.database;	 Catch:{ Exception -> 0x01f5 }
                r12 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x01f5 }
                r12.<init>();	 Catch:{ Exception -> 0x01f5 }
                r13 = "SELECT m.read_state, m.data, m.send_state, m.mid, m.date, r.random_id, m.uid, s.seq_in, s.seq_out, m.ttl FROM messages as m LEFT JOIN randoms as r ON r.mid = m.mid LEFT JOIN messages_seq as s ON m.mid = s.mid WHERE m.mid < 0 AND m.send_state = 1 ORDER BY m.mid DESC LIMIT ";	 Catch:{ Exception -> 0x01f5 }
                r12.append(r13);	 Catch:{ Exception -> 0x01f5 }
                r13 = r3;	 Catch:{ Exception -> 0x01f5 }
                r12.append(r13);	 Catch:{ Exception -> 0x01f5 }
                r12 = r12.toString();	 Catch:{ Exception -> 0x01f5 }
                r13 = 0;	 Catch:{ Exception -> 0x01f5 }
                r14 = new java.lang.Object[r13];	 Catch:{ Exception -> 0x01f5 }
                r11 = r11.queryFinalized(r12, r14);	 Catch:{ Exception -> 0x01f5 }
                r12 = r11.next();	 Catch:{ Exception -> 0x01f5 }
                if (r12 == 0) goto L_0x0164;	 Catch:{ Exception -> 0x01f5 }
            L_0x0055:
                r12 = 1;	 Catch:{ Exception -> 0x01f5 }
                r14 = r11.byteBufferValue(r12);	 Catch:{ Exception -> 0x01f5 }
                if (r14 == 0) goto L_0x0160;	 Catch:{ Exception -> 0x01f5 }
            L_0x005c:
                r15 = r14.readInt32(r13);	 Catch:{ Exception -> 0x01f5 }
                r15 = org.telegram.tgnet.TLRPC.Message.TLdeserialize(r14, r15, r13);	 Catch:{ Exception -> 0x01f5 }
                r12 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x01f5 }
                r12 = r12.currentAccount;	 Catch:{ Exception -> 0x01f5 }
                r12 = org.telegram.messenger.UserConfig.getInstance(r12);	 Catch:{ Exception -> 0x01f5 }
                r12 = r12.clientUserId;	 Catch:{ Exception -> 0x01f5 }
                r15.readAttachPath(r14, r12);	 Catch:{ Exception -> 0x01f5 }
                r14.reuse();	 Catch:{ Exception -> 0x01f5 }
                r12 = r15.id;	 Catch:{ Exception -> 0x01f5 }
                r12 = r2.indexOfKey(r12);	 Catch:{ Exception -> 0x01f5 }
                if (r12 >= 0) goto L_0x0160;	 Catch:{ Exception -> 0x01f5 }
                r12 = r11.intValue(r13);	 Catch:{ Exception -> 0x01f5 }
                org.telegram.messenger.MessageObject.setUnreadFlags(r15, r12);	 Catch:{ Exception -> 0x01f5 }
                r12 = 3;	 Catch:{ Exception -> 0x01f5 }
                r12 = r11.intValue(r12);	 Catch:{ Exception -> 0x01f5 }
                r15.id = r12;	 Catch:{ Exception -> 0x01f5 }
                r12 = 4;	 Catch:{ Exception -> 0x01f5 }
                r12 = r11.intValue(r12);	 Catch:{ Exception -> 0x01f5 }
                r15.date = r12;	 Catch:{ Exception -> 0x01f5 }
                r12 = 5;	 Catch:{ Exception -> 0x01f5 }
                r17 = r11.isNull(r12);	 Catch:{ Exception -> 0x01f5 }
                if (r17 != 0) goto L_0x00a3;	 Catch:{ Exception -> 0x01f5 }
                r18 = r14;	 Catch:{ Exception -> 0x01f5 }
                r13 = r11.longValue(r12);	 Catch:{ Exception -> 0x01f5 }
                r15.random_id = r13;	 Catch:{ Exception -> 0x01f5 }
                goto L_0x00a5;	 Catch:{ Exception -> 0x01f5 }
                r18 = r14;	 Catch:{ Exception -> 0x01f5 }
                r13 = 6;	 Catch:{ Exception -> 0x01f5 }
                r13 = r11.longValue(r13);	 Catch:{ Exception -> 0x01f5 }
                r15.dialog_id = r13;	 Catch:{ Exception -> 0x01f5 }
                r13 = 7;	 Catch:{ Exception -> 0x01f5 }
                r13 = r11.intValue(r13);	 Catch:{ Exception -> 0x01f5 }
                r15.seq_in = r13;	 Catch:{ Exception -> 0x01f5 }
                r13 = 8;	 Catch:{ Exception -> 0x01f5 }
                r13 = r11.intValue(r13);	 Catch:{ Exception -> 0x01f5 }
                r15.seq_out = r13;	 Catch:{ Exception -> 0x01f5 }
                r13 = 9;	 Catch:{ Exception -> 0x01f5 }
                r13 = r11.intValue(r13);	 Catch:{ Exception -> 0x01f5 }
                r15.ttl = r13;	 Catch:{ Exception -> 0x01f5 }
                r3.add(r15);	 Catch:{ Exception -> 0x01f5 }
                r13 = r15.id;	 Catch:{ Exception -> 0x01f5 }
                r2.put(r13, r15);	 Catch:{ Exception -> 0x01f5 }
                r13 = r15.dialog_id;	 Catch:{ Exception -> 0x01f5 }
                r13 = (int) r13;	 Catch:{ Exception -> 0x01f5 }
                r19 = r13;	 Catch:{ Exception -> 0x01f5 }
                r12 = r15.dialog_id;	 Catch:{ Exception -> 0x01f5 }
                r14 = 32;	 Catch:{ Exception -> 0x01f5 }
                r12 = r12 >> r14;	 Catch:{ Exception -> 0x01f5 }
                r12 = (int) r12;	 Catch:{ Exception -> 0x01f5 }
                if (r19 == 0) goto L_0x0119;	 Catch:{ Exception -> 0x01f5 }
                r13 = 1;	 Catch:{ Exception -> 0x01f5 }
                if (r12 != r13) goto L_0x00ef;	 Catch:{ Exception -> 0x01f5 }
                r13 = r19;	 Catch:{ Exception -> 0x01f5 }
                r14 = java.lang.Integer.valueOf(r13);	 Catch:{ Exception -> 0x01f5 }
                r14 = r9.contains(r14);	 Catch:{ Exception -> 0x01f5 }
                if (r14 != 0) goto L_0x012c;	 Catch:{ Exception -> 0x01f5 }
                r14 = java.lang.Integer.valueOf(r13);	 Catch:{ Exception -> 0x01f5 }
                r9.add(r14);	 Catch:{ Exception -> 0x01f5 }
                goto L_0x012c;	 Catch:{ Exception -> 0x01f5 }
                r13 = r19;	 Catch:{ Exception -> 0x01f5 }
                if (r13 >= 0) goto L_0x0107;	 Catch:{ Exception -> 0x01f5 }
                r14 = -r13;	 Catch:{ Exception -> 0x01f5 }
                r14 = java.lang.Integer.valueOf(r14);	 Catch:{ Exception -> 0x01f5 }
                r14 = r8.contains(r14);	 Catch:{ Exception -> 0x01f5 }
                if (r14 != 0) goto L_0x012c;	 Catch:{ Exception -> 0x01f5 }
                r14 = -r13;	 Catch:{ Exception -> 0x01f5 }
                r14 = java.lang.Integer.valueOf(r14);	 Catch:{ Exception -> 0x01f5 }
                r8.add(r14);	 Catch:{ Exception -> 0x01f5 }
                goto L_0x012c;	 Catch:{ Exception -> 0x01f5 }
                r14 = java.lang.Integer.valueOf(r13);	 Catch:{ Exception -> 0x01f5 }
                r14 = r7.contains(r14);	 Catch:{ Exception -> 0x01f5 }
                if (r14 != 0) goto L_0x012c;	 Catch:{ Exception -> 0x01f5 }
                r14 = java.lang.Integer.valueOf(r13);	 Catch:{ Exception -> 0x01f5 }
                r7.add(r14);	 Catch:{ Exception -> 0x01f5 }
                goto L_0x012c;	 Catch:{ Exception -> 0x01f5 }
                r13 = r19;	 Catch:{ Exception -> 0x01f5 }
                r14 = java.lang.Integer.valueOf(r12);	 Catch:{ Exception -> 0x01f5 }
                r14 = r10.contains(r14);	 Catch:{ Exception -> 0x01f5 }
                if (r14 != 0) goto L_0x012c;	 Catch:{ Exception -> 0x01f5 }
                r14 = java.lang.Integer.valueOf(r12);	 Catch:{ Exception -> 0x01f5 }
                r10.add(r14);	 Catch:{ Exception -> 0x01f5 }
                org.telegram.messenger.MessagesStorage.addUsersAndChatsFromMessage(r15, r7, r8);	 Catch:{ Exception -> 0x01f5 }
                r14 = 2;	 Catch:{ Exception -> 0x01f5 }
                r14 = r11.intValue(r14);	 Catch:{ Exception -> 0x01f5 }
                r15.send_state = r14;	 Catch:{ Exception -> 0x01f5 }
                r14 = r15.to_id;	 Catch:{ Exception -> 0x01f5 }
                r14 = r14.channel_id;	 Catch:{ Exception -> 0x01f5 }
                if (r14 != 0) goto L_0x0144;	 Catch:{ Exception -> 0x01f5 }
                r14 = org.telegram.messenger.MessageObject.isUnread(r15);	 Catch:{ Exception -> 0x01f5 }
                if (r14 != 0) goto L_0x0144;	 Catch:{ Exception -> 0x01f5 }
                if (r13 != 0) goto L_0x0148;	 Catch:{ Exception -> 0x01f5 }
                r14 = r15.id;	 Catch:{ Exception -> 0x01f5 }
                if (r14 <= 0) goto L_0x014c;	 Catch:{ Exception -> 0x01f5 }
                r14 = 0;	 Catch:{ Exception -> 0x01f5 }
                r15.send_state = r14;	 Catch:{ Exception -> 0x01f5 }
                goto L_0x014d;	 Catch:{ Exception -> 0x01f5 }
                r14 = 0;	 Catch:{ Exception -> 0x01f5 }
                if (r13 != 0) goto L_0x0160;	 Catch:{ Exception -> 0x01f5 }
                r14 = 5;	 Catch:{ Exception -> 0x01f5 }
                r16 = r11.isNull(r14);	 Catch:{ Exception -> 0x01f5 }
                if (r16 != 0) goto L_0x0160;	 Catch:{ Exception -> 0x01f5 }
                r21 = r12;	 Catch:{ Exception -> 0x01f5 }
                r22 = r13;	 Catch:{ Exception -> 0x01f5 }
                r12 = r11.longValue(r14);	 Catch:{ Exception -> 0x01f5 }
                r15.random_id = r12;	 Catch:{ Exception -> 0x01f5 }
                r13 = 0;	 Catch:{ Exception -> 0x01f5 }
                goto L_0x004f;	 Catch:{ Exception -> 0x01f5 }
            L_0x0164:
                r11.dispose();	 Catch:{ Exception -> 0x01f5 }
                r12 = r10.isEmpty();	 Catch:{ Exception -> 0x01f5 }
                if (r12 != 0) goto L_0x0178;	 Catch:{ Exception -> 0x01f5 }
                r12 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x01f5 }
                r13 = ",";	 Catch:{ Exception -> 0x01f5 }
                r13 = android.text.TextUtils.join(r13, r10);	 Catch:{ Exception -> 0x01f5 }
                r12.getEncryptedChatsInternal(r13, r6, r7);	 Catch:{ Exception -> 0x01f5 }
                r12 = r7.isEmpty();	 Catch:{ Exception -> 0x01f5 }
                if (r12 != 0) goto L_0x0189;	 Catch:{ Exception -> 0x01f5 }
                r12 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x01f5 }
                r13 = ",";	 Catch:{ Exception -> 0x01f5 }
                r13 = android.text.TextUtils.join(r13, r7);	 Catch:{ Exception -> 0x01f5 }
                r12.getUsersInternal(r13, r4);	 Catch:{ Exception -> 0x01f5 }
                r12 = r8.isEmpty();	 Catch:{ Exception -> 0x01f5 }
                if (r12 == 0) goto L_0x0195;	 Catch:{ Exception -> 0x01f5 }
                r12 = r9.isEmpty();	 Catch:{ Exception -> 0x01f5 }
                if (r12 != 0) goto L_0x01e7;	 Catch:{ Exception -> 0x01f5 }
                r12 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x01f5 }
                r12.<init>();	 Catch:{ Exception -> 0x01f5 }
                r13 = 0;	 Catch:{ Exception -> 0x01f5 }
                r14 = r8.size();	 Catch:{ Exception -> 0x01f5 }
                if (r13 >= r14) goto L_0x01b8;	 Catch:{ Exception -> 0x01f5 }
                r14 = r8.get(r13);	 Catch:{ Exception -> 0x01f5 }
                r14 = (java.lang.Integer) r14;	 Catch:{ Exception -> 0x01f5 }
                r15 = r12.length();	 Catch:{ Exception -> 0x01f5 }
                if (r15 == 0) goto L_0x01b2;	 Catch:{ Exception -> 0x01f5 }
                r15 = ",";	 Catch:{ Exception -> 0x01f5 }
                r12.append(r15);	 Catch:{ Exception -> 0x01f5 }
                r12.append(r14);	 Catch:{ Exception -> 0x01f5 }
                r13 = r13 + 1;	 Catch:{ Exception -> 0x01f5 }
                goto L_0x019b;	 Catch:{ Exception -> 0x01f5 }
                r20 = 0;	 Catch:{ Exception -> 0x01f5 }
                r13 = r20;	 Catch:{ Exception -> 0x01f5 }
                r14 = r9.size();	 Catch:{ Exception -> 0x01f5 }
                if (r13 >= r14) goto L_0x01de;	 Catch:{ Exception -> 0x01f5 }
                r14 = r9.get(r13);	 Catch:{ Exception -> 0x01f5 }
                r14 = (java.lang.Integer) r14;	 Catch:{ Exception -> 0x01f5 }
                r15 = r12.length();	 Catch:{ Exception -> 0x01f5 }
                if (r15 == 0) goto L_0x01d3;	 Catch:{ Exception -> 0x01f5 }
                r15 = ",";	 Catch:{ Exception -> 0x01f5 }
                r12.append(r15);	 Catch:{ Exception -> 0x01f5 }
                r15 = r14.intValue();	 Catch:{ Exception -> 0x01f5 }
                r15 = -r15;	 Catch:{ Exception -> 0x01f5 }
                r12.append(r15);	 Catch:{ Exception -> 0x01f5 }
                r20 = r13 + 1;	 Catch:{ Exception -> 0x01f5 }
                goto L_0x01ba;	 Catch:{ Exception -> 0x01f5 }
                r13 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x01f5 }
                r14 = r12.toString();	 Catch:{ Exception -> 0x01f5 }
                r13.getChatsInternal(r14, r5);	 Catch:{ Exception -> 0x01f5 }
                r12 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x01f5 }
                r12 = r12.currentAccount;	 Catch:{ Exception -> 0x01f5 }
                r12 = org.telegram.messenger.SendMessagesHelper.getInstance(r12);	 Catch:{ Exception -> 0x01f5 }
                r12.processUnsentMessages(r3, r4, r5, r6);	 Catch:{ Exception -> 0x01f5 }
                goto L_0x01fa;
            L_0x01f5:
                r0 = move-exception;
                r2 = r0;
                org.telegram.messenger.FileLog.e(r2);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.MessagesStorage.55.run():void");
            }
        });
    }

    public boolean checkMessageId(long dialog_id, int mid) {
        boolean[] result = new boolean[1];
        CountDownLatch countDownLatch = new CountDownLatch(1);
        final long j = dialog_id;
        final int i = mid;
        final boolean[] zArr = result;
        final CountDownLatch countDownLatch2 = countDownLatch;
        this.storageQueue.postRunnable(new Runnable() {
            /* JADX WARNING: inconsistent code. */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public void run() {
                /*
                r8 = this;
                r0 = 0;
                r1 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x003d }
                r1 = r1.database;	 Catch:{ Exception -> 0x003d }
                r2 = java.util.Locale.US;	 Catch:{ Exception -> 0x003d }
                r3 = "SELECT mid FROM messages WHERE uid = %d AND mid = %d";
                r4 = 2;
                r4 = new java.lang.Object[r4];	 Catch:{ Exception -> 0x003d }
                r5 = r3;	 Catch:{ Exception -> 0x003d }
                r5 = java.lang.Long.valueOf(r5);	 Catch:{ Exception -> 0x003d }
                r6 = 0;
                r4[r6] = r5;	 Catch:{ Exception -> 0x003d }
                r5 = r5;	 Catch:{ Exception -> 0x003d }
                r5 = java.lang.Integer.valueOf(r5);	 Catch:{ Exception -> 0x003d }
                r7 = 1;
                r4[r7] = r5;	 Catch:{ Exception -> 0x003d }
                r2 = java.lang.String.format(r2, r3, r4);	 Catch:{ Exception -> 0x003d }
                r3 = new java.lang.Object[r6];	 Catch:{ Exception -> 0x003d }
                r1 = r1.queryFinalized(r2, r3);	 Catch:{ Exception -> 0x003d }
                r0 = r1;
                r1 = r0.next();	 Catch:{ Exception -> 0x003d }
                if (r1 == 0) goto L_0x0035;
            L_0x0031:
                r1 = r6;	 Catch:{ Exception -> 0x003d }
                r1[r6] = r7;	 Catch:{ Exception -> 0x003d }
            L_0x0035:
                if (r0 == 0) goto L_0x0044;
            L_0x0037:
                r0.dispose();
                goto L_0x0044;
            L_0x003b:
                r1 = move-exception;
                goto L_0x004a;
            L_0x003d:
                r1 = move-exception;
                org.telegram.messenger.FileLog.e(r1);	 Catch:{ all -> 0x003b }
                if (r0 == 0) goto L_0x0044;
            L_0x0043:
                goto L_0x0037;
            L_0x0044:
                r1 = r7;
                r1.countDown();
                return;
            L_0x004a:
                if (r0 == 0) goto L_0x004f;
            L_0x004c:
                r0.dispose();
            L_0x004f:
                throw r1;
                */
                throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.MessagesStorage.56.run():void");
            }
        });
        try {
            countDownLatch.await();
        } catch (Throwable e) {
            FileLog.e(e);
        }
        return result[0];
    }

    public void getUnreadMention(final long dialog_id, final IntCallback callback) {
        this.storageQueue.postRunnable(new Runnable() {

            class AnonymousClass1 implements Runnable {
                final /* synthetic */ int val$result;

                AnonymousClass1(int i) {
                    this.val$result = i;
                }

                public void run() {
                    callback.run(this.val$result);
                }
            }

            public void run() {
                /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.MessagesStorage.57.run():void
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:116)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:249)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:256)
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
                r0 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x003a }
                r0 = r0.database;	 Catch:{ Exception -> 0x003a }
                r1 = java.util.Locale.US;	 Catch:{ Exception -> 0x003a }
                r2 = "SELECT MIN(mid) FROM messages WHERE uid = %d AND mention = 1 AND read_state IN(0, 1)";	 Catch:{ Exception -> 0x003a }
                r3 = 1;	 Catch:{ Exception -> 0x003a }
                r3 = new java.lang.Object[r3];	 Catch:{ Exception -> 0x003a }
                r4 = r3;	 Catch:{ Exception -> 0x003a }
                r4 = java.lang.Long.valueOf(r4);	 Catch:{ Exception -> 0x003a }
                r5 = 0;	 Catch:{ Exception -> 0x003a }
                r3[r5] = r4;	 Catch:{ Exception -> 0x003a }
                r1 = java.lang.String.format(r1, r2, r3);	 Catch:{ Exception -> 0x003a }
                r2 = new java.lang.Object[r5];	 Catch:{ Exception -> 0x003a }
                r0 = r0.queryFinalized(r1, r2);	 Catch:{ Exception -> 0x003a }
                r1 = r0.next();	 Catch:{ Exception -> 0x003a }
                if (r1 == 0) goto L_0x002c;	 Catch:{ Exception -> 0x003a }
            L_0x0026:
                r1 = r0.intValue(r5);	 Catch:{ Exception -> 0x003a }
                r5 = r1;	 Catch:{ Exception -> 0x003a }
                goto L_0x002d;	 Catch:{ Exception -> 0x003a }
                r1 = r5;	 Catch:{ Exception -> 0x003a }
                r0.dispose();	 Catch:{ Exception -> 0x003a }
                r2 = new org.telegram.messenger.MessagesStorage$57$1;	 Catch:{ Exception -> 0x003a }
                r2.<init>(r1);	 Catch:{ Exception -> 0x003a }
                org.telegram.messenger.AndroidUtilities.runOnUIThread(r2);	 Catch:{ Exception -> 0x003a }
                goto L_0x003e;
            L_0x003a:
                r0 = move-exception;
                org.telegram.messenger.FileLog.e(r0);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.MessagesStorage.57.run():void");
            }
        });
    }

    public void getMessages(long dialog_id, int count, int max_id, int offset_date, int minDate, int classGuid, int load_type, boolean isChannel, int loadIndex) {
        final int i = count;
        final int i2 = max_id;
        final boolean z = isChannel;
        final long j = dialog_id;
        final int i3 = load_type;
        final int i4 = minDate;
        final int i5 = offset_date;
        final int i6 = classGuid;
        final int i7 = loadIndex;
        this.storageQueue.postRunnable(new Runnable() {
            public void run() {
                /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.MessagesStorage.58.run():void
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:116)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:249)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:256)
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
                r1 = r96;
                r2 = new org.telegram.tgnet.TLRPC$TL_messages_messages;
                r2.<init>();
                r3 = 0;
                r4 = 0;
                r5 = r2;
                r6 = 0;
                r7 = 0;
                r8 = 0;
                r9 = 0;
                r10 = 0;
                r11 = r3;
                r11 = (long) r11;
                r13 = r3;
                r14 = 0;
                r15 = r3;
                r16 = 0;
                r22 = r3;
                r3 = r4;
                if (r3 == 0) goto L_0x0028;
            L_0x0020:
                r23 = r4;
                r3 = r5;
                r3 = (int) r3;
                r3 = -r3;
                r4 = r3;
                goto L_0x002c;
            L_0x0028:
                r23 = r4;
                r4 = r16;
            L_0x002c:
                r24 = r7;
                r25 = r8;
                r7 = 0;
                r3 = (r11 > r7 ? 1 : (r11 == r7 ? 0 : -1));
                r16 = 32;
                if (r3 == 0) goto L_0x0041;
            L_0x0038:
                if (r4 == 0) goto L_0x0041;
            L_0x003a:
                r7 = (long) r4;
                r7 = r7 << r16;
                r17 = r11 | r7;
                r11 = r17;
            L_0x0041:
                r3 = 0;
                r7 = r5;
                r17 = 777000; // 0xbdb28 float:1.088809E-39 double:3.83889E-318;
                r19 = (r7 > r17 ? 1 : (r7 == r17 ? 0 : -1));
                if (r19 != 0) goto L_0x004e;
            L_0x004b:
                r8 = 10;
                goto L_0x004f;
            L_0x004e:
                r8 = 1;
            L_0x004f:
                r7 = new java.util.ArrayList;	 Catch:{ Exception -> 0x23b6, all -> 0x238e }
                r7.<init>();	 Catch:{ Exception -> 0x23b6, all -> 0x238e }
                r28 = r3;
                r3 = new java.util.ArrayList;	 Catch:{ Exception -> 0x2369, all -> 0x2343 }
                r3.<init>();	 Catch:{ Exception -> 0x2369, all -> 0x2343 }
                r29 = r9;
                r9 = new java.util.ArrayList;	 Catch:{ Exception -> 0x231f, all -> 0x22fb }
                r9.<init>();	 Catch:{ Exception -> 0x231f, all -> 0x22fb }
                r30 = r10;
                r10 = new android.util.SparseArray;	 Catch:{ Exception -> 0x22d9, all -> 0x22b7 }
                r10.<init>();	 Catch:{ Exception -> 0x22d9, all -> 0x22b7 }
                r31 = r13;
                r13 = new android.util.LongSparseArray;	 Catch:{ Exception -> 0x2297, all -> 0x2277 }
                r13.<init>();	 Catch:{ Exception -> 0x2297, all -> 0x2277 }
                r32 = r14;
                r33 = r15;
                r14 = r5;	 Catch:{ Exception -> 0x225b, all -> 0x223f }
                r14 = (int) r14;
                if (r14 == 0) goto L_0x1485;
            L_0x0079:
                r15 = r7;	 Catch:{ Exception -> 0x146b, all -> 0x1451 }
                r34 = r13;
                r13 = 3;
                if (r15 != r13) goto L_0x01d5;
            L_0x0080:
                r13 = r8;	 Catch:{ Exception -> 0x01bb, all -> 0x01a1 }
                if (r13 != 0) goto L_0x01d5;	 Catch:{ Exception -> 0x01bb, all -> 0x01a1 }
            L_0x0084:
                r13 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x01bb, all -> 0x01a1 }
                r13 = r13.database;	 Catch:{ Exception -> 0x01bb, all -> 0x01a1 }
                r15 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x01bb, all -> 0x01a1 }
                r15.<init>();	 Catch:{ Exception -> 0x01bb, all -> 0x01a1 }
                r35 = r10;	 Catch:{ Exception -> 0x01bb, all -> 0x01a1 }
                r10 = "SELECT inbox_max, unread_count, date, unread_count_i FROM dialogs WHERE did = ";	 Catch:{ Exception -> 0x01bb, all -> 0x01a1 }
                r15.append(r10);	 Catch:{ Exception -> 0x01bb, all -> 0x01a1 }
                r36 = r9;	 Catch:{ Exception -> 0x01bb, all -> 0x01a1 }
                r9 = r5;	 Catch:{ Exception -> 0x01bb, all -> 0x01a1 }
                r15.append(r9);	 Catch:{ Exception -> 0x01bb, all -> 0x01a1 }
                r9 = r15.toString();	 Catch:{ Exception -> 0x01bb, all -> 0x01a1 }
                r10 = 0;	 Catch:{ Exception -> 0x01bb, all -> 0x01a1 }
                r15 = new java.lang.Object[r10];	 Catch:{ Exception -> 0x01bb, all -> 0x01a1 }
                r9 = r13.queryFinalized(r9, r15);	 Catch:{ Exception -> 0x01bb, all -> 0x01a1 }
                r10 = r9.next();	 Catch:{ Exception -> 0x01bb, all -> 0x01a1 }
                if (r10 == 0) goto L_0x0155;	 Catch:{ Exception -> 0x01bb, all -> 0x01a1 }
            L_0x00ae:
                r10 = 0;	 Catch:{ Exception -> 0x01bb, all -> 0x01a1 }
                r13 = r9.intValue(r10);	 Catch:{ Exception -> 0x01bb, all -> 0x01a1 }
                r10 = 1;
                r13 = r13 + r10;
                r15 = r9.intValue(r10);	 Catch:{ Exception -> 0x0140, all -> 0x012d }
                r10 = r15;
                r15 = 2;
                r17 = r9.intValue(r15);	 Catch:{ Exception -> 0x0111, all -> 0x00f5 }
                r15 = r17;
                r37 = r10;
                r10 = 3;
                r17 = r9.intValue(r10);	 Catch:{ Exception -> 0x00e2, all -> 0x00cf }
                r10 = r17;
                r23 = r10;
                r10 = r15;
                goto L_0x015b;
            L_0x00cf:
                r0 = move-exception;
                r22 = r2;
                r26 = r4;
                r67 = r5;
                r48 = r6;
                r27 = r8;
                r65 = r11;
                r50 = r13;
                r51 = r15;
                goto L_0x0182;
            L_0x00e2:
                r0 = move-exception;
                r95 = r2;
                r26 = r4;
                r67 = r5;
                r48 = r6;
                r27 = r8;
                r65 = r11;
                r50 = r13;
                r51 = r15;
                goto L_0x019a;
            L_0x00f5:
                r0 = move-exception;
                r37 = r10;
                r22 = r2;
                r26 = r4;
                r67 = r5;
                r48 = r6;
                r27 = r8;
                r65 = r11;
                r50 = r13;
                r47 = r23;
                r51 = r30;
                r53 = r31;
                r59 = r37;
                r2 = r0;
                goto L_0x242b;
            L_0x0111:
                r0 = move-exception;
                r37 = r10;
                r95 = r2;
                r26 = r4;
                r67 = r5;
                r48 = r6;
                r27 = r8;
                r65 = r11;
                r50 = r13;
                r47 = r23;
                r51 = r30;
                r53 = r31;
                r59 = r37;
                r2 = r0;
                goto L_0x23dc;
            L_0x012d:
                r0 = move-exception;
                r26 = r4;
                r67 = r5;
                r48 = r6;
                r27 = r8;
                r65 = r11;
                r50 = r13;
                r59 = r22;
                r47 = r23;
                goto L_0x01b2;
            L_0x0140:
                r0 = move-exception;
                r95 = r2;
                r26 = r4;
                r67 = r5;
                r48 = r6;
                r27 = r8;
                r65 = r11;
                r50 = r13;
                r59 = r22;
                r47 = r23;
                goto L_0x01ce;
            L_0x0155:
                r37 = r22;
                r13 = r24;
                r10 = r30;
            L_0x015b:
                r9.dispose();	 Catch:{ Exception -> 0x0189, all -> 0x0171 }
                r38 = r2;
                r39 = r3;
                r49 = r7;
                r7 = r13;
                r46 = r14;
                r47 = r23;
                r9 = r29;
                r13 = r31;
                r3 = r37;
                goto L_0x0800;
            L_0x0171:
                r0 = move-exception;
                r22 = r2;
                r26 = r4;
                r67 = r5;
                r48 = r6;
                r27 = r8;
                r51 = r10;
                r65 = r11;
                r50 = r13;
            L_0x0182:
                r47 = r23;
                r53 = r31;
                r59 = r37;
                goto L_0x01b8;
            L_0x0189:
                r0 = move-exception;
                r95 = r2;
                r26 = r4;
                r67 = r5;
                r48 = r6;
                r27 = r8;
                r51 = r10;
                r65 = r11;
                r50 = r13;
            L_0x019a:
                r47 = r23;
                r53 = r31;
                r59 = r37;
                goto L_0x01d2;
            L_0x01a1:
                r0 = move-exception;
                r26 = r4;
                r67 = r5;
                r48 = r6;
                r27 = r8;
                r65 = r11;
                r59 = r22;
                r47 = r23;
                r50 = r24;
            L_0x01b2:
                r51 = r30;
                r53 = r31;
                r22 = r2;
            L_0x01b8:
                r2 = r0;
                goto L_0x242b;
            L_0x01bb:
                r0 = move-exception;
                r95 = r2;
                r26 = r4;
                r67 = r5;
                r48 = r6;
                r27 = r8;
                r65 = r11;
                r59 = r22;
                r47 = r23;
                r50 = r24;
            L_0x01ce:
                r51 = r30;
                r53 = r31;
            L_0x01d2:
                r2 = r0;
                goto L_0x23dc;
            L_0x01d5:
                r36 = r9;
                r35 = r10;
                r9 = r7;	 Catch:{ Exception -> 0x146b, all -> 0x1451 }
                r10 = 1;
                if (r9 == r10) goto L_0x07e8;
            L_0x01de:
                r9 = r7;	 Catch:{ Exception -> 0x07ce, all -> 0x07b4 }
                r10 = 3;	 Catch:{ Exception -> 0x07ce, all -> 0x07b4 }
                if (r9 == r10) goto L_0x07e8;	 Catch:{ Exception -> 0x07ce, all -> 0x07b4 }
            L_0x01e3:
                r9 = r7;	 Catch:{ Exception -> 0x07ce, all -> 0x07b4 }
                r10 = 4;	 Catch:{ Exception -> 0x07ce, all -> 0x07b4 }
                if (r9 == r10) goto L_0x07e8;	 Catch:{ Exception -> 0x07ce, all -> 0x07b4 }
            L_0x01e8:
                r9 = r8;	 Catch:{ Exception -> 0x07ce, all -> 0x07b4 }
                if (r9 != 0) goto L_0x07e8;	 Catch:{ Exception -> 0x07ce, all -> 0x07b4 }
            L_0x01ec:
                r9 = r7;	 Catch:{ Exception -> 0x07ce, all -> 0x07b4 }
                r10 = 2;	 Catch:{ Exception -> 0x07ce, all -> 0x07b4 }
                if (r9 != r10) goto L_0x0750;	 Catch:{ Exception -> 0x07ce, all -> 0x07b4 }
            L_0x01f1:
                r9 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x07ce, all -> 0x07b4 }
                r9 = r9.database;	 Catch:{ Exception -> 0x07ce, all -> 0x07b4 }
                r10 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x07ce, all -> 0x07b4 }
                r10.<init>();	 Catch:{ Exception -> 0x07ce, all -> 0x07b4 }
                r13 = "SELECT inbox_max, unread_count, date, unread_count_i FROM dialogs WHERE did = ";	 Catch:{ Exception -> 0x07ce, all -> 0x07b4 }
                r10.append(r13);	 Catch:{ Exception -> 0x07ce, all -> 0x07b4 }
                r38 = r2;
                r39 = r3;
                r2 = r5;	 Catch:{ Exception -> 0x0736, all -> 0x071c }
                r10.append(r2);	 Catch:{ Exception -> 0x0736, all -> 0x071c }
                r2 = r10.toString();	 Catch:{ Exception -> 0x0736, all -> 0x071c }
                r3 = 0;	 Catch:{ Exception -> 0x0736, all -> 0x071c }
                r10 = new java.lang.Object[r3];	 Catch:{ Exception -> 0x0736, all -> 0x071c }
                r2 = r9.queryFinalized(r2, r10);	 Catch:{ Exception -> 0x0736, all -> 0x071c }
                r3 = r2.next();	 Catch:{ Exception -> 0x0736, all -> 0x071c }
                if (r3 == 0) goto L_0x0328;
            L_0x021b:
                r3 = 0;
                r9 = r2.intValue(r3);	 Catch:{ Exception -> 0x0310, all -> 0x02f8 }
                r3 = r9;
                r13 = r9;
                r11 = (long) r9;
                r9 = 1;
                r10 = r2.intValue(r9);	 Catch:{ Exception -> 0x02e0, all -> 0x02c8 }
                r9 = r10;
                r10 = 2;
                r15 = r2.intValue(r10);	 Catch:{ Exception -> 0x02ac, all -> 0x0290 }
                r10 = r15;
                r15 = 3;
                r17 = r2.intValue(r15);	 Catch:{ Exception -> 0x0272, all -> 0x0254 }
                r15 = r17;
                r17 = 1;
                r18 = 0;
                r20 = (r11 > r18 ? 1 : (r11 == r18 ? 0 : -1));
                if (r20 == 0) goto L_0x024c;
            L_0x023e:
                if (r4 == 0) goto L_0x024c;
            L_0x0240:
                r40 = r9;
                r41 = r10;
                r9 = (long) r4;
                r9 = r9 << r16;
                r18 = r11 | r9;
                r11 = r18;
                goto L_0x0250;
            L_0x024c:
                r40 = r9;
                r41 = r10;
            L_0x0250:
                r9 = r40;
                goto L_0x0334;
            L_0x0254:
                r0 = move-exception;
                r40 = r9;
                r41 = r10;
                r2 = r0;
                r50 = r3;
                r26 = r4;
                r67 = r5;
                r48 = r6;
                r27 = r8;
                r65 = r11;
                r53 = r13;
                r47 = r23;
                r22 = r38;
                r59 = r40;
                r51 = r41;
                goto L_0x242b;
            L_0x0272:
                r0 = move-exception;
                r40 = r9;
                r41 = r10;
                r2 = r0;
                r50 = r3;
                r26 = r4;
                r67 = r5;
                r48 = r6;
                r27 = r8;
                r65 = r11;
                r53 = r13;
                r47 = r23;
                r95 = r38;
                r59 = r40;
                r51 = r41;
                goto L_0x23dc;
            L_0x0290:
                r0 = move-exception;
                r40 = r9;
                r2 = r0;
                r50 = r3;
                r26 = r4;
                r67 = r5;
                r48 = r6;
                r27 = r8;
                r65 = r11;
                r53 = r13;
                r47 = r23;
                r51 = r30;
                r22 = r38;
                r59 = r40;
                goto L_0x242b;
            L_0x02ac:
                r0 = move-exception;
                r40 = r9;
                r2 = r0;
                r50 = r3;
                r26 = r4;
                r67 = r5;
                r48 = r6;
                r27 = r8;
                r65 = r11;
                r53 = r13;
                r47 = r23;
                r51 = r30;
                r95 = r38;
                r59 = r40;
                goto L_0x23dc;
            L_0x02c8:
                r0 = move-exception;
                r2 = r0;
                r50 = r3;
                r26 = r4;
                r67 = r5;
                r48 = r6;
                r27 = r8;
                r65 = r11;
                r53 = r13;
                r59 = r22;
                r47 = r23;
                r51 = r30;
                goto L_0x0798;
            L_0x02e0:
                r0 = move-exception;
                r2 = r0;
                r50 = r3;
                r26 = r4;
                r67 = r5;
                r48 = r6;
                r27 = r8;
                r65 = r11;
                r53 = r13;
                r59 = r22;
                r47 = r23;
                r51 = r30;
                goto L_0x07b0;
            L_0x02f8:
                r0 = move-exception;
                r2 = r0;
                r26 = r4;
                r67 = r5;
                r48 = r6;
                r27 = r8;
                r65 = r11;
                r59 = r22;
                r47 = r23;
                r50 = r24;
                r51 = r30;
                r53 = r31;
                goto L_0x0798;
            L_0x0310:
                r0 = move-exception;
                r2 = r0;
                r26 = r4;
                r67 = r5;
                r48 = r6;
                r27 = r8;
                r65 = r11;
                r59 = r22;
                r47 = r23;
                r50 = r24;
                r51 = r30;
                r53 = r31;
                goto L_0x07b0;
            L_0x0328:
                r9 = r22;
                r15 = r23;
                r3 = r24;
                r17 = r29;
                r41 = r30;
                r13 = r31;
            L_0x0334:
                r2.dispose();	 Catch:{ Exception -> 0x06fc, all -> 0x06dc }
                if (r17 != 0) goto L_0x0518;	 Catch:{ Exception -> 0x06fc, all -> 0x06dc }
            L_0x0339:
                r10 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x06fc, all -> 0x06dc }
                r10 = r10.database;	 Catch:{ Exception -> 0x06fc, all -> 0x06dc }
                r42 = r2;	 Catch:{ Exception -> 0x06fc, all -> 0x06dc }
                r2 = java.util.Locale.US;	 Catch:{ Exception -> 0x06fc, all -> 0x06dc }
                r43 = r3;
                r3 = "SELECT min(mid), max(date) FROM messages WHERE uid = %d AND out = 0 AND read_state IN(0,2) AND mid > 0";	 Catch:{ Exception -> 0x04fa, all -> 0x04dc }
                r44 = r11;
                r11 = 1;
                r12 = new java.lang.Object[r11];	 Catch:{ Exception -> 0x04c0, all -> 0x04a4 }
                r46 = r14;
                r47 = r15;
                r14 = r5;	 Catch:{ Exception -> 0x048a, all -> 0x0470 }
                r11 = java.lang.Long.valueOf(r14);	 Catch:{ Exception -> 0x048a, all -> 0x0470 }
                r14 = 0;	 Catch:{ Exception -> 0x048a, all -> 0x0470 }
                r12[r14] = r11;	 Catch:{ Exception -> 0x048a, all -> 0x0470 }
                r2 = java.lang.String.format(r2, r3, r12);	 Catch:{ Exception -> 0x048a, all -> 0x0470 }
                r3 = new java.lang.Object[r14];	 Catch:{ Exception -> 0x048a, all -> 0x0470 }
                r2 = r10.queryFinalized(r2, r3);	 Catch:{ Exception -> 0x048a, all -> 0x0470 }
                r3 = r2.next();	 Catch:{ Exception -> 0x048a, all -> 0x0470 }
                if (r3 == 0) goto L_0x03bb;
            L_0x0369:
                r3 = 0;
                r10 = r2.intValue(r3);	 Catch:{ Exception -> 0x03b1, all -> 0x03a7 }
                r3 = r10;
                r10 = 1;
                r11 = r2.intValue(r10);	 Catch:{ Exception -> 0x038f, all -> 0x0377 }
                r41 = r11;
                goto L_0x03bd;
            L_0x0377:
                r0 = move-exception;
                r2 = r0;
                r50 = r3;
                r26 = r4;
                r67 = r5;
                r48 = r6;
            L_0x0381:
                r27 = r8;
                r59 = r9;
            L_0x0385:
                r53 = r13;
                r29 = r17;
                r22 = r38;
                r51 = r41;
                goto L_0x05f6;
            L_0x038f:
                r0 = move-exception;
                r2 = r0;
                r50 = r3;
                r26 = r4;
                r67 = r5;
                r48 = r6;
            L_0x0399:
                r27 = r8;
                r59 = r9;
            L_0x039d:
                r53 = r13;
                r29 = r17;
                r95 = r38;
                r51 = r41;
                goto L_0x060e;
            L_0x03a7:
                r0 = move-exception;
                r2 = r0;
                r26 = r4;
                r67 = r5;
                r48 = r6;
                goto L_0x05e8;
            L_0x03b1:
                r0 = move-exception;
                r2 = r0;
                r26 = r4;
                r67 = r5;
                r48 = r6;
                goto L_0x0600;
            L_0x03bb:
                r3 = r43;
            L_0x03bd:
                r2.dispose();	 Catch:{ Exception -> 0x0459, all -> 0x0442 }
                if (r3 == 0) goto L_0x0434;	 Catch:{ Exception -> 0x0459, all -> 0x0442 }
            L_0x03c2:
                r10 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x0459, all -> 0x0442 }
                r10 = r10.database;	 Catch:{ Exception -> 0x0459, all -> 0x0442 }
                r11 = java.util.Locale.US;	 Catch:{ Exception -> 0x0459, all -> 0x0442 }
                r12 = "SELECT COUNT(*) FROM messages WHERE uid = %d AND mid >= %d AND out = 0 AND read_state IN(0,2)";	 Catch:{ Exception -> 0x0459, all -> 0x0442 }
                r14 = 2;	 Catch:{ Exception -> 0x0459, all -> 0x0442 }
                r15 = new java.lang.Object[r14];	 Catch:{ Exception -> 0x0459, all -> 0x0442 }
                r48 = r6;
                r49 = r7;
                r6 = r5;	 Catch:{ Exception -> 0x042a, all -> 0x0420 }
                r6 = java.lang.Long.valueOf(r6);	 Catch:{ Exception -> 0x042a, all -> 0x0420 }
                r7 = 0;	 Catch:{ Exception -> 0x042a, all -> 0x0420 }
                r15[r7] = r6;	 Catch:{ Exception -> 0x042a, all -> 0x0420 }
                r6 = java.lang.Integer.valueOf(r3);	 Catch:{ Exception -> 0x042a, all -> 0x0420 }
                r7 = 1;	 Catch:{ Exception -> 0x042a, all -> 0x0420 }
                r15[r7] = r6;	 Catch:{ Exception -> 0x042a, all -> 0x0420 }
                r6 = java.lang.String.format(r11, r12, r15);	 Catch:{ Exception -> 0x042a, all -> 0x0420 }
                r7 = 0;	 Catch:{ Exception -> 0x042a, all -> 0x0420 }
                r11 = new java.lang.Object[r7];	 Catch:{ Exception -> 0x042a, all -> 0x0420 }
                r6 = r10.queryFinalized(r6, r11);	 Catch:{ Exception -> 0x042a, all -> 0x0420 }
                r2 = r6;	 Catch:{ Exception -> 0x042a, all -> 0x0420 }
                r6 = r2.next();	 Catch:{ Exception -> 0x042a, all -> 0x0420 }
                if (r6 == 0) goto L_0x03fc;	 Catch:{ Exception -> 0x042a, all -> 0x0420 }
            L_0x03f5:
                r6 = 0;	 Catch:{ Exception -> 0x042a, all -> 0x0420 }
                r7 = r2.intValue(r6);	 Catch:{ Exception -> 0x042a, all -> 0x0420 }
                r6 = r7;
                goto L_0x03fd;
            L_0x03fc:
                r6 = r9;
            L_0x03fd:
                r2.dispose();	 Catch:{ Exception -> 0x0412, all -> 0x0404 }
                r7 = r3;
                r3 = r6;
                goto L_0x05da;
            L_0x0404:
                r0 = move-exception;
                r2 = r0;
                r50 = r3;
                r26 = r4;
                r67 = r5;
                r59 = r6;
                r27 = r8;
                goto L_0x0385;
            L_0x0412:
                r0 = move-exception;
                r2 = r0;
                r50 = r3;
                r26 = r4;
                r67 = r5;
                r59 = r6;
                r27 = r8;
                goto L_0x039d;
            L_0x0420:
                r0 = move-exception;
                r2 = r0;
                r50 = r3;
                r26 = r4;
                r67 = r5;
                goto L_0x0381;
            L_0x042a:
                r0 = move-exception;
                r2 = r0;
                r50 = r3;
                r26 = r4;
                r67 = r5;
                goto L_0x0399;
            L_0x0434:
                r48 = r6;
                r49 = r7;
                r7 = r3;
                r3 = r9;
                r9 = r17;
                r10 = r41;
                r11 = r44;
                goto L_0x0766;
            L_0x0442:
                r0 = move-exception;
                r48 = r6;
                r2 = r0;
                r50 = r3;
                r26 = r4;
                r67 = r5;
                r27 = r8;
                r59 = r9;
                r53 = r13;
                r29 = r17;
                r22 = r38;
                r51 = r41;
                goto L_0x0486;
            L_0x0459:
                r0 = move-exception;
                r48 = r6;
                r2 = r0;
                r50 = r3;
                r26 = r4;
                r67 = r5;
                r27 = r8;
                r59 = r9;
                r53 = r13;
                r29 = r17;
                r95 = r38;
                r51 = r41;
                goto L_0x04a0;
            L_0x0470:
                r0 = move-exception;
                r48 = r6;
                r2 = r0;
                r26 = r4;
                r67 = r5;
                r27 = r8;
                r59 = r9;
                r53 = r13;
                r29 = r17;
                r22 = r38;
                r51 = r41;
                r50 = r43;
            L_0x0486:
                r65 = r44;
                goto L_0x0734;
            L_0x048a:
                r0 = move-exception;
                r48 = r6;
                r2 = r0;
                r26 = r4;
                r67 = r5;
                r27 = r8;
                r59 = r9;
                r53 = r13;
                r29 = r17;
                r95 = r38;
                r51 = r41;
                r50 = r43;
            L_0x04a0:
                r65 = r44;
                goto L_0x074e;
            L_0x04a4:
                r0 = move-exception;
                r48 = r6;
                r47 = r15;
                r2 = r0;
                r26 = r4;
                r67 = r5;
                r27 = r8;
                r59 = r9;
                r53 = r13;
                r29 = r17;
                r22 = r38;
                r51 = r41;
                r50 = r43;
                r65 = r44;
                goto L_0x242b;
            L_0x04c0:
                r0 = move-exception;
                r48 = r6;
                r47 = r15;
                r2 = r0;
                r26 = r4;
                r67 = r5;
                r27 = r8;
                r59 = r9;
                r53 = r13;
                r29 = r17;
                r95 = r38;
                r51 = r41;
                r50 = r43;
                r65 = r44;
                goto L_0x23dc;
            L_0x04dc:
                r0 = move-exception;
                r48 = r6;
                r44 = r11;
                r47 = r15;
                r2 = r0;
                r26 = r4;
                r67 = r5;
                r27 = r8;
                r59 = r9;
                r53 = r13;
                r29 = r17;
                r22 = r38;
                r51 = r41;
                r50 = r43;
                r65 = r44;
                goto L_0x242b;
            L_0x04fa:
                r0 = move-exception;
                r48 = r6;
                r44 = r11;
                r47 = r15;
                r2 = r0;
                r26 = r4;
                r67 = r5;
                r27 = r8;
                r59 = r9;
                r53 = r13;
                r29 = r17;
                r95 = r38;
                r51 = r41;
                r50 = r43;
                r65 = r44;
                goto L_0x23dc;
            L_0x0518:
                r42 = r2;
                r43 = r3;
                r48 = r6;
                r49 = r7;
                r44 = r11;
                r46 = r14;
                r47 = r15;
                if (r13 != 0) goto L_0x0612;
            L_0x0528:
                r2 = 0;
                r3 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x05fa, all -> 0x05e2 }
                r3 = r3.database;	 Catch:{ Exception -> 0x05fa, all -> 0x05e2 }
                r6 = java.util.Locale.US;	 Catch:{ Exception -> 0x05fa, all -> 0x05e2 }
                r7 = "SELECT COUNT(*) FROM messages WHERE uid = %d AND mid > 0 AND out = 0 AND read_state IN(0,2)";	 Catch:{ Exception -> 0x05fa, all -> 0x05e2 }
                r10 = 1;	 Catch:{ Exception -> 0x05fa, all -> 0x05e2 }
                r11 = new java.lang.Object[r10];	 Catch:{ Exception -> 0x05fa, all -> 0x05e2 }
                r14 = r5;	 Catch:{ Exception -> 0x05fa, all -> 0x05e2 }
                r10 = java.lang.Long.valueOf(r14);	 Catch:{ Exception -> 0x05fa, all -> 0x05e2 }
                r12 = 0;	 Catch:{ Exception -> 0x05fa, all -> 0x05e2 }
                r11[r12] = r10;	 Catch:{ Exception -> 0x05fa, all -> 0x05e2 }
                r6 = java.lang.String.format(r6, r7, r11);	 Catch:{ Exception -> 0x05fa, all -> 0x05e2 }
                r7 = new java.lang.Object[r12];	 Catch:{ Exception -> 0x05fa, all -> 0x05e2 }
                r3 = r3.queryFinalized(r6, r7);	 Catch:{ Exception -> 0x05fa, all -> 0x05e2 }
                r6 = r3.next();	 Catch:{ Exception -> 0x05fa, all -> 0x05e2 }
                if (r6 == 0) goto L_0x0555;	 Catch:{ Exception -> 0x05fa, all -> 0x05e2 }
            L_0x054f:
                r6 = 0;	 Catch:{ Exception -> 0x05fa, all -> 0x05e2 }
                r7 = r3.intValue(r6);	 Catch:{ Exception -> 0x05fa, all -> 0x05e2 }
                r2 = r7;	 Catch:{ Exception -> 0x05fa, all -> 0x05e2 }
            L_0x0555:
                r3.dispose();	 Catch:{ Exception -> 0x05fa, all -> 0x05e2 }
                if (r2 != r9) goto L_0x05d7;	 Catch:{ Exception -> 0x05fa, all -> 0x05e2 }
            L_0x055a:
                r6 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x05fa, all -> 0x05e2 }
                r6 = r6.database;	 Catch:{ Exception -> 0x05fa, all -> 0x05e2 }
                r7 = java.util.Locale.US;	 Catch:{ Exception -> 0x05fa, all -> 0x05e2 }
                r10 = "SELECT min(mid) FROM messages WHERE uid = %d AND out = 0 AND read_state IN(0,2) AND mid > 0";	 Catch:{ Exception -> 0x05fa, all -> 0x05e2 }
                r11 = 1;	 Catch:{ Exception -> 0x05fa, all -> 0x05e2 }
                r12 = new java.lang.Object[r11];	 Catch:{ Exception -> 0x05fa, all -> 0x05e2 }
                r14 = r5;	 Catch:{ Exception -> 0x05fa, all -> 0x05e2 }
                r11 = java.lang.Long.valueOf(r14);	 Catch:{ Exception -> 0x05fa, all -> 0x05e2 }
                r14 = 0;	 Catch:{ Exception -> 0x05fa, all -> 0x05e2 }
                r12[r14] = r11;	 Catch:{ Exception -> 0x05fa, all -> 0x05e2 }
                r7 = java.lang.String.format(r7, r10, r12);	 Catch:{ Exception -> 0x05fa, all -> 0x05e2 }
                r10 = new java.lang.Object[r14];	 Catch:{ Exception -> 0x05fa, all -> 0x05e2 }
                r6 = r6.queryFinalized(r7, r10);	 Catch:{ Exception -> 0x05fa, all -> 0x05e2 }
                r3 = r6;	 Catch:{ Exception -> 0x05fa, all -> 0x05e2 }
                r6 = r3.next();	 Catch:{ Exception -> 0x05fa, all -> 0x05e2 }
                if (r6 == 0) goto L_0x059c;	 Catch:{ Exception -> 0x05fa, all -> 0x05e2 }
            L_0x0581:
                r6 = 0;	 Catch:{ Exception -> 0x05fa, all -> 0x05e2 }
                r7 = r3.intValue(r6);	 Catch:{ Exception -> 0x05fa, all -> 0x05e2 }
                r6 = r7;
                r10 = r7;
                r11 = (long) r7;
                r13 = 0;
                r7 = (r11 > r13 ? 1 : (r11 == r13 ? 0 : -1));
                if (r7 == 0) goto L_0x059a;
            L_0x058f:
                if (r4 == 0) goto L_0x059a;
            L_0x0591:
                r13 = (long) r4;
                r13 = r13 << r16;
                r18 = r11 | r13;
                r13 = r10;
                r11 = r18;
                goto L_0x05a0;
            L_0x059a:
                r13 = r10;
                goto L_0x05a0;
            L_0x059c:
                r6 = r43;
                r11 = r44;
            L_0x05a0:
                r3.dispose();	 Catch:{ Exception -> 0x05bf, all -> 0x05a7 }
                r7 = r6;
                r44 = r11;
                goto L_0x05d9;
            L_0x05a7:
                r0 = move-exception;
                r2 = r0;
                r26 = r4;
                r67 = r5;
                r50 = r6;
                r27 = r8;
                r59 = r9;
                r65 = r11;
                r53 = r13;
                r29 = r17;
                r22 = r38;
                r51 = r41;
                goto L_0x242b;
            L_0x05bf:
                r0 = move-exception;
                r2 = r0;
                r26 = r4;
                r67 = r5;
                r50 = r6;
                r27 = r8;
                r59 = r9;
                r65 = r11;
                r53 = r13;
                r29 = r17;
                r95 = r38;
                r51 = r41;
                goto L_0x23dc;
            L_0x05d7:
                r7 = r43;
            L_0x05d9:
                r3 = r9;
            L_0x05da:
                r9 = r17;
                r10 = r41;
            L_0x05de:
                r11 = r44;
                goto L_0x0766;
            L_0x05e2:
                r0 = move-exception;
                r2 = r0;
                r26 = r4;
                r67 = r5;
            L_0x05e8:
                r27 = r8;
                r59 = r9;
                r53 = r13;
                r29 = r17;
                r22 = r38;
                r51 = r41;
                r50 = r43;
            L_0x05f6:
                r65 = r44;
                goto L_0x242b;
            L_0x05fa:
                r0 = move-exception;
                r2 = r0;
                r26 = r4;
                r67 = r5;
            L_0x0600:
                r27 = r8;
                r59 = r9;
                r53 = r13;
                r29 = r17;
                r95 = r38;
                r51 = r41;
                r50 = r43;
            L_0x060e:
                r65 = r44;
                goto L_0x23dc;
            L_0x0612:
                r2 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x05fa, all -> 0x05e2 }
                r2 = r2.database;	 Catch:{ Exception -> 0x05fa, all -> 0x05e2 }
                r3 = java.util.Locale.US;	 Catch:{ Exception -> 0x05fa, all -> 0x05e2 }
                r6 = "SELECT start, end FROM messages_holes WHERE uid = %d AND start < %d AND end > %d";	 Catch:{ Exception -> 0x05fa, all -> 0x05e2 }
                r7 = 3;	 Catch:{ Exception -> 0x05fa, all -> 0x05e2 }
                r10 = new java.lang.Object[r7];	 Catch:{ Exception -> 0x05fa, all -> 0x05e2 }
                r11 = r5;	 Catch:{ Exception -> 0x05fa, all -> 0x05e2 }
                r7 = java.lang.Long.valueOf(r11);	 Catch:{ Exception -> 0x05fa, all -> 0x05e2 }
                r11 = 0;	 Catch:{ Exception -> 0x05fa, all -> 0x05e2 }
                r10[r11] = r7;	 Catch:{ Exception -> 0x05fa, all -> 0x05e2 }
                r7 = java.lang.Integer.valueOf(r13);	 Catch:{ Exception -> 0x05fa, all -> 0x05e2 }
                r11 = 1;	 Catch:{ Exception -> 0x05fa, all -> 0x05e2 }
                r10[r11] = r7;	 Catch:{ Exception -> 0x05fa, all -> 0x05e2 }
                r7 = java.lang.Integer.valueOf(r13);	 Catch:{ Exception -> 0x05fa, all -> 0x05e2 }
                r11 = 2;	 Catch:{ Exception -> 0x05fa, all -> 0x05e2 }
                r10[r11] = r7;	 Catch:{ Exception -> 0x05fa, all -> 0x05e2 }
                r3 = java.lang.String.format(r3, r6, r10);	 Catch:{ Exception -> 0x05fa, all -> 0x05e2 }
                r6 = 0;	 Catch:{ Exception -> 0x05fa, all -> 0x05e2 }
                r7 = new java.lang.Object[r6];	 Catch:{ Exception -> 0x05fa, all -> 0x05e2 }
                r2 = r2.queryFinalized(r3, r7);	 Catch:{ Exception -> 0x05fa, all -> 0x05e2 }
                r3 = r2.next();	 Catch:{ Exception -> 0x05fa, all -> 0x05e2 }
                r6 = 1;	 Catch:{ Exception -> 0x05fa, all -> 0x05e2 }
                r3 = r3 ^ r6;	 Catch:{ Exception -> 0x05fa, all -> 0x05e2 }
                r2.dispose();	 Catch:{ Exception -> 0x05fa, all -> 0x05e2 }
                if (r3 == 0) goto L_0x06d3;	 Catch:{ Exception -> 0x05fa, all -> 0x05e2 }
            L_0x064c:
                r6 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x05fa, all -> 0x05e2 }
                r6 = r6.database;	 Catch:{ Exception -> 0x05fa, all -> 0x05e2 }
                r7 = java.util.Locale.US;	 Catch:{ Exception -> 0x05fa, all -> 0x05e2 }
                r10 = "SELECT min(mid) FROM messages WHERE uid = %d AND out = 0 AND read_state IN(0,2) AND mid > %d";	 Catch:{ Exception -> 0x05fa, all -> 0x05e2 }
                r11 = 2;	 Catch:{ Exception -> 0x05fa, all -> 0x05e2 }
                r12 = new java.lang.Object[r11];	 Catch:{ Exception -> 0x05fa, all -> 0x05e2 }
                r14 = r5;	 Catch:{ Exception -> 0x05fa, all -> 0x05e2 }
                r11 = java.lang.Long.valueOf(r14);	 Catch:{ Exception -> 0x05fa, all -> 0x05e2 }
                r14 = 0;	 Catch:{ Exception -> 0x05fa, all -> 0x05e2 }
                r12[r14] = r11;	 Catch:{ Exception -> 0x05fa, all -> 0x05e2 }
                r11 = java.lang.Integer.valueOf(r13);	 Catch:{ Exception -> 0x05fa, all -> 0x05e2 }
                r14 = 1;	 Catch:{ Exception -> 0x05fa, all -> 0x05e2 }
                r12[r14] = r11;	 Catch:{ Exception -> 0x05fa, all -> 0x05e2 }
                r7 = java.lang.String.format(r7, r10, r12);	 Catch:{ Exception -> 0x05fa, all -> 0x05e2 }
                r10 = 0;	 Catch:{ Exception -> 0x05fa, all -> 0x05e2 }
                r11 = new java.lang.Object[r10];	 Catch:{ Exception -> 0x05fa, all -> 0x05e2 }
                r6 = r6.queryFinalized(r7, r11);	 Catch:{ Exception -> 0x05fa, all -> 0x05e2 }
                r2 = r6;	 Catch:{ Exception -> 0x05fa, all -> 0x05e2 }
                r6 = r2.next();	 Catch:{ Exception -> 0x05fa, all -> 0x05e2 }
                if (r6 == 0) goto L_0x0695;	 Catch:{ Exception -> 0x05fa, all -> 0x05e2 }
            L_0x067b:
                r6 = 0;	 Catch:{ Exception -> 0x05fa, all -> 0x05e2 }
                r7 = r2.intValue(r6);	 Catch:{ Exception -> 0x05fa, all -> 0x05e2 }
                r6 = r7;
                r10 = (long) r7;
                r12 = 0;
                r7 = (r10 > r12 ? 1 : (r10 == r12 ? 0 : -1));
                if (r7 == 0) goto L_0x0692;
            L_0x0688:
                if (r4 == 0) goto L_0x0692;
            L_0x068a:
                r12 = (long) r4;
                r12 = r12 << r16;
                r14 = r10 | r12;
                r13 = r6;
                r11 = r14;
                goto L_0x0697;
            L_0x0692:
                r13 = r6;
                r11 = r10;
                goto L_0x0697;
            L_0x0695:
                r11 = r44;
            L_0x0697:
                r2.dispose();	 Catch:{ Exception -> 0x06bb, all -> 0x06a3 }
                r3 = r9;
                r9 = r17;
                r10 = r41;
                r7 = r43;
                goto L_0x0766;
            L_0x06a3:
                r0 = move-exception;
                r2 = r0;
                r26 = r4;
                r67 = r5;
                r27 = r8;
                r59 = r9;
                r65 = r11;
                r53 = r13;
                r29 = r17;
                r22 = r38;
                r51 = r41;
                r50 = r43;
                goto L_0x242b;
            L_0x06bb:
                r0 = move-exception;
                r2 = r0;
                r26 = r4;
                r67 = r5;
                r27 = r8;
                r59 = r9;
                r65 = r11;
                r53 = r13;
                r29 = r17;
                r95 = r38;
                r51 = r41;
                r50 = r43;
                goto L_0x23dc;
            L_0x06d3:
                r3 = r9;
                r9 = r17;
                r10 = r41;
                r7 = r43;
                goto L_0x05de;
            L_0x06dc:
                r0 = move-exception;
                r43 = r3;
                r48 = r6;
                r44 = r11;
                r47 = r15;
                r2 = r0;
                r26 = r4;
                r67 = r5;
                r27 = r8;
                r59 = r9;
                r53 = r13;
                r29 = r17;
                r22 = r38;
                r51 = r41;
                r50 = r43;
                r65 = r44;
                goto L_0x242b;
            L_0x06fc:
                r0 = move-exception;
                r43 = r3;
                r48 = r6;
                r44 = r11;
                r47 = r15;
                r2 = r0;
                r26 = r4;
                r67 = r5;
                r27 = r8;
                r59 = r9;
                r53 = r13;
                r29 = r17;
                r95 = r38;
                r51 = r41;
                r50 = r43;
                r65 = r44;
                goto L_0x23dc;
            L_0x071c:
                r0 = move-exception;
                r48 = r6;
                r2 = r0;
                r26 = r4;
                r67 = r5;
                r27 = r8;
                r65 = r11;
                r59 = r22;
                r47 = r23;
                r50 = r24;
                r51 = r30;
                r53 = r31;
                r22 = r38;
            L_0x0734:
                goto L_0x242b;
            L_0x0736:
                r0 = move-exception;
                r48 = r6;
                r2 = r0;
                r26 = r4;
                r67 = r5;
                r27 = r8;
                r65 = r11;
                r59 = r22;
                r47 = r23;
                r50 = r24;
                r51 = r30;
                r53 = r31;
                r95 = r38;
            L_0x074e:
                goto L_0x23dc;
            L_0x0750:
                r38 = r2;
                r39 = r3;
                r48 = r6;
                r49 = r7;
                r46 = r14;
                r3 = r22;
                r47 = r23;
                r7 = r24;
                r9 = r29;
                r10 = r30;
                r13 = r31;
            L_0x0766:
                if (r5 > r3) goto L_0x0771;
            L_0x0768:
                if (r3 >= r8) goto L_0x076b;
            L_0x076a:
                goto L_0x0771;
            L_0x076b:
                r6 = r3 - r5;
                r5 = r5 + 10;
                goto L_0x0800;
            L_0x0771:
                r2 = r3 + 10;
                r2 = java.lang.Math.max(r5, r2);	 Catch:{ Exception -> 0x079c, all -> 0x0784 }
                r5 = r2;
                if (r3 >= r8) goto L_0x07fe;
            L_0x077a:
                r3 = 0;
                r7 = 0;
                r11 = 0;
                r2 = 0;
                r9 = 0;
                r25 = r2;
                goto L_0x07fe;
            L_0x0784:
                r0 = move-exception;
                r2 = r0;
                r59 = r3;
                r26 = r4;
                r67 = r5;
                r50 = r7;
                r27 = r8;
                r29 = r9;
                r51 = r10;
                r65 = r11;
                r53 = r13;
            L_0x0798:
                r22 = r38;
                goto L_0x242b;
            L_0x079c:
                r0 = move-exception;
                r2 = r0;
                r59 = r3;
                r26 = r4;
                r67 = r5;
                r50 = r7;
                r27 = r8;
                r29 = r9;
                r51 = r10;
                r65 = r11;
                r53 = r13;
            L_0x07b0:
                r95 = r38;
                goto L_0x23dc;
            L_0x07b4:
                r0 = move-exception;
                r48 = r6;
                r26 = r4;
                r67 = r5;
                r27 = r8;
                r65 = r11;
                r59 = r22;
                r47 = r23;
                r50 = r24;
                r51 = r30;
                r53 = r31;
                r22 = r2;
                r2 = r0;
                goto L_0x242b;
            L_0x07ce:
                r0 = move-exception;
                r48 = r6;
                r95 = r2;
                r26 = r4;
                r67 = r5;
                r27 = r8;
                r65 = r11;
                r59 = r22;
                r47 = r23;
                r50 = r24;
                r51 = r30;
                r53 = r31;
                r2 = r0;
                goto L_0x23dc;
            L_0x07e8:
                r38 = r2;
                r39 = r3;
                r48 = r6;
                r49 = r7;
                r46 = r14;
                r3 = r22;
                r47 = r23;
                r7 = r24;
                r9 = r29;
                r10 = r30;
                r13 = r31;
            L_0x07fe:
                r6 = r48;
            L_0x0800:
                r2 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x1432, all -> 0x1413 }
                r2 = r2.database;	 Catch:{ Exception -> 0x1432, all -> 0x1413 }
                r14 = java.util.Locale.US;	 Catch:{ Exception -> 0x1432, all -> 0x1413 }
                r15 = "SELECT start FROM messages_holes WHERE uid = %d AND start IN (0, 1)";	 Catch:{ Exception -> 0x1432, all -> 0x1413 }
                r50 = r7;
                r51 = r10;
                r7 = 1;
                r10 = new java.lang.Object[r7];	 Catch:{ Exception -> 0x13f8, all -> 0x13dd }
                r52 = r8;
                r7 = r5;	 Catch:{ Exception -> 0x13c2, all -> 0x13a7 }
                r7 = java.lang.Long.valueOf(r7);	 Catch:{ Exception -> 0x13c2, all -> 0x13a7 }
                r8 = 0;	 Catch:{ Exception -> 0x13c2, all -> 0x13a7 }
                r10[r8] = r7;	 Catch:{ Exception -> 0x13c2, all -> 0x13a7 }
                r7 = java.lang.String.format(r14, r15, r10);	 Catch:{ Exception -> 0x13c2, all -> 0x13a7 }
                r10 = new java.lang.Object[r8];	 Catch:{ Exception -> 0x13c2, all -> 0x13a7 }
                r2 = r2.queryFinalized(r7, r10);	 Catch:{ Exception -> 0x13c2, all -> 0x13a7 }
                r7 = r2.next();	 Catch:{ Exception -> 0x13c2, all -> 0x13a7 }
                if (r7 == 0) goto L_0x0886;
            L_0x082c:
                r7 = 0;
                r8 = r2.intValue(r7);	 Catch:{ Exception -> 0x0870, all -> 0x085a }
                r7 = 1;
                if (r8 != r7) goto L_0x0836;
            L_0x0834:
                r7 = 1;
                goto L_0x0837;
            L_0x0836:
                r7 = 0;
            L_0x0837:
                r2.dispose();	 Catch:{ Exception -> 0x084d, all -> 0x0840 }
                r28 = r7;
                r53 = r13;
                goto L_0x08e1;
            L_0x0840:
                r0 = move-exception;
                r2 = r0;
                r59 = r3;
                r26 = r4;
                r67 = r5;
                r48 = r6;
                r28 = r7;
                goto L_0x0864;
            L_0x084d:
                r0 = move-exception;
                r2 = r0;
                r59 = r3;
                r26 = r4;
                r67 = r5;
                r48 = r6;
                r28 = r7;
                goto L_0x087a;
            L_0x085a:
                r0 = move-exception;
                r2 = r0;
                r59 = r3;
                r26 = r4;
                r67 = r5;
                r48 = r6;
            L_0x0864:
                r29 = r9;
                r65 = r11;
                r53 = r13;
            L_0x086a:
                r22 = r38;
                r27 = r52;
                goto L_0x242b;
            L_0x0870:
                r0 = move-exception;
                r2 = r0;
                r59 = r3;
                r26 = r4;
                r67 = r5;
                r48 = r6;
            L_0x087a:
                r29 = r9;
                r65 = r11;
                r53 = r13;
            L_0x0880:
                r95 = r38;
                r27 = r52;
                goto L_0x23dc;
            L_0x0886:
                r2.dispose();	 Catch:{ Exception -> 0x13c2, all -> 0x13a7 }
                r7 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x13c2, all -> 0x13a7 }
                r7 = r7.database;	 Catch:{ Exception -> 0x13c2, all -> 0x13a7 }
                r8 = java.util.Locale.US;	 Catch:{ Exception -> 0x13c2, all -> 0x13a7 }
                r10 = "SELECT min(mid) FROM messages WHERE uid = %d AND mid > 0";	 Catch:{ Exception -> 0x13c2, all -> 0x13a7 }
                r14 = 1;	 Catch:{ Exception -> 0x13c2, all -> 0x13a7 }
                r15 = new java.lang.Object[r14];	 Catch:{ Exception -> 0x13c2, all -> 0x13a7 }
                r53 = r13;
                r13 = r5;	 Catch:{ Exception -> 0x138e, all -> 0x1375 }
                r13 = java.lang.Long.valueOf(r13);	 Catch:{ Exception -> 0x138e, all -> 0x1375 }
                r14 = 0;	 Catch:{ Exception -> 0x138e, all -> 0x1375 }
                r15[r14] = r13;	 Catch:{ Exception -> 0x138e, all -> 0x1375 }
                r8 = java.lang.String.format(r8, r10, r15);	 Catch:{ Exception -> 0x138e, all -> 0x1375 }
                r10 = new java.lang.Object[r14];	 Catch:{ Exception -> 0x138e, all -> 0x1375 }
                r7 = r7.queryFinalized(r8, r10);	 Catch:{ Exception -> 0x138e, all -> 0x1375 }
                r2 = r7;	 Catch:{ Exception -> 0x138e, all -> 0x1375 }
                r7 = r2.next();	 Catch:{ Exception -> 0x138e, all -> 0x1375 }
                if (r7 == 0) goto L_0x08de;
            L_0x08b2:
                r7 = 0;
                r8 = r2.intValue(r7);	 Catch:{ Exception -> 0x090b, all -> 0x08fb }
                r7 = r8;	 Catch:{ Exception -> 0x090b, all -> 0x08fb }
                if (r7 == 0) goto L_0x08de;	 Catch:{ Exception -> 0x090b, all -> 0x08fb }
            L_0x08ba:
                r8 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x090b, all -> 0x08fb }
                r8 = r8.database;	 Catch:{ Exception -> 0x090b, all -> 0x08fb }
                r10 = "REPLACE INTO messages_holes VALUES(?, ?, ?)";	 Catch:{ Exception -> 0x090b, all -> 0x08fb }
                r8 = r8.executeFast(r10);	 Catch:{ Exception -> 0x090b, all -> 0x08fb }
                r8.requery();	 Catch:{ Exception -> 0x090b, all -> 0x08fb }
                r13 = r5;	 Catch:{ Exception -> 0x090b, all -> 0x08fb }
                r10 = 1;	 Catch:{ Exception -> 0x090b, all -> 0x08fb }
                r8.bindLong(r10, r13);	 Catch:{ Exception -> 0x090b, all -> 0x08fb }
                r10 = 0;	 Catch:{ Exception -> 0x090b, all -> 0x08fb }
                r13 = 2;	 Catch:{ Exception -> 0x090b, all -> 0x08fb }
                r8.bindInteger(r13, r10);	 Catch:{ Exception -> 0x090b, all -> 0x08fb }
                r10 = 3;	 Catch:{ Exception -> 0x090b, all -> 0x08fb }
                r8.bindInteger(r10, r7);	 Catch:{ Exception -> 0x090b, all -> 0x08fb }
                r8.step();	 Catch:{ Exception -> 0x090b, all -> 0x08fb }
                r8.dispose();	 Catch:{ Exception -> 0x090b, all -> 0x08fb }
            L_0x08de:
                r2.dispose();	 Catch:{ Exception -> 0x138e, all -> 0x1375 }
            L_0x08e1:
                r7 = r7;	 Catch:{ Exception -> 0x138e, all -> 0x1375 }
                r8 = 3;
                if (r7 == r8) goto L_0x0e2c;
            L_0x08e6:
                r7 = r7;	 Catch:{ Exception -> 0x0e18, all -> 0x0e04 }
                r8 = 4;
                if (r7 == r8) goto L_0x0e2c;
            L_0x08eb:
                if (r9 == 0) goto L_0x091b;
            L_0x08ed:
                r7 = r7;	 Catch:{ Exception -> 0x090b, all -> 0x08fb }
                r8 = 2;
                if (r7 != r8) goto L_0x091b;
            L_0x08f2:
                r54 = r2;
                r59 = r3;
                r7 = r4;
                r65 = r11;
                goto L_0x0e33;
            L_0x08fb:
                r0 = move-exception;
                r2 = r0;
                r59 = r3;
                r26 = r4;
                r67 = r5;
                r48 = r6;
                r29 = r9;
                r65 = r11;
                goto L_0x086a;
            L_0x090b:
                r0 = move-exception;
                r2 = r0;
                r59 = r3;
                r26 = r4;
                r67 = r5;
                r48 = r6;
                r29 = r9;
                r65 = r11;
                goto L_0x0880;
            L_0x091b:
                r7 = r7;	 Catch:{ Exception -> 0x0e18, all -> 0x0e04 }
                r8 = 1;
                if (r7 != r8) goto L_0x0a54;
            L_0x0920:
                r7 = 0;
                r10 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x0a40, all -> 0x0a2c }
                r10 = r10.database;	 Catch:{ Exception -> 0x0a40, all -> 0x0a2c }
                r13 = java.util.Locale.US;	 Catch:{ Exception -> 0x0a40, all -> 0x0a2c }
                r14 = "SELECT start, end FROM messages_holes WHERE uid = %d AND start >= %d AND start != 1 AND end != 1 ORDER BY start ASC LIMIT 1";	 Catch:{ Exception -> 0x0a40, all -> 0x0a2c }
                r54 = r2;	 Catch:{ Exception -> 0x0a40, all -> 0x0a2c }
                r15 = 2;	 Catch:{ Exception -> 0x0a40, all -> 0x0a2c }
                r2 = new java.lang.Object[r15];	 Catch:{ Exception -> 0x0a40, all -> 0x0a2c }
                r55 = r7;	 Catch:{ Exception -> 0x0a40, all -> 0x0a2c }
                r7 = r5;	 Catch:{ Exception -> 0x0a40, all -> 0x0a2c }
                r7 = java.lang.Long.valueOf(r7);	 Catch:{ Exception -> 0x0a40, all -> 0x0a2c }
                r8 = 0;	 Catch:{ Exception -> 0x0a40, all -> 0x0a2c }
                r2[r8] = r7;	 Catch:{ Exception -> 0x0a40, all -> 0x0a2c }
                r7 = r3;	 Catch:{ Exception -> 0x0a40, all -> 0x0a2c }
                r7 = java.lang.Integer.valueOf(r7);	 Catch:{ Exception -> 0x0a40, all -> 0x0a2c }
                r8 = 1;	 Catch:{ Exception -> 0x0a40, all -> 0x0a2c }
                r2[r8] = r7;	 Catch:{ Exception -> 0x0a40, all -> 0x0a2c }
                r2 = java.lang.String.format(r13, r14, r2);	 Catch:{ Exception -> 0x0a40, all -> 0x0a2c }
                r7 = 0;	 Catch:{ Exception -> 0x0a40, all -> 0x0a2c }
                r8 = new java.lang.Object[r7];	 Catch:{ Exception -> 0x0a40, all -> 0x0a2c }
                r2 = r10.queryFinalized(r2, r8);	 Catch:{ Exception -> 0x0a40, all -> 0x0a2c }
                r7 = r2.next();	 Catch:{ Exception -> 0x0a40, all -> 0x0a2c }
                if (r7 == 0) goto L_0x0966;
            L_0x0956:
                r7 = 0;
                r8 = r2.intValue(r7);	 Catch:{ Exception -> 0x090b, all -> 0x08fb }
                r7 = (long) r8;
                if (r4 == 0) goto L_0x0968;
            L_0x095e:
                r13 = (long) r4;
                r13 = r13 << r16;
                r17 = r7 | r13;
                r7 = r17;
                goto L_0x0968;
            L_0x0966:
                r7 = r55;
            L_0x0968:
                r2.dispose();	 Catch:{ Exception -> 0x0a40, all -> 0x0a2c }
                r13 = 0;	 Catch:{ Exception -> 0x0a40, all -> 0x0a2c }
                r10 = (r7 > r13 ? 1 : (r7 == r13 ? 0 : -1));	 Catch:{ Exception -> 0x0a40, all -> 0x0a2c }
                if (r10 == 0) goto L_0x09b7;	 Catch:{ Exception -> 0x0a40, all -> 0x0a2c }
            L_0x0971:
                r10 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x0a40, all -> 0x0a2c }
                r10 = r10.database;	 Catch:{ Exception -> 0x0a40, all -> 0x0a2c }
                r13 = java.util.Locale.US;	 Catch:{ Exception -> 0x0a40, all -> 0x0a2c }
                r14 = "SELECT m.read_state, m.data, m.send_state, m.mid, m.date, r.random_id, m.replydata, m.media, m.ttl, m.mention FROM messages as m LEFT JOIN randoms as r ON r.mid = m.mid WHERE m.uid = %d AND m.date >= %d AND m.mid > %d AND m.mid <= %d ORDER BY m.date ASC, m.mid ASC LIMIT %d";	 Catch:{ Exception -> 0x0a40, all -> 0x0a2c }
                r57 = r2;	 Catch:{ Exception -> 0x0a40, all -> 0x0a2c }
                r15 = 5;	 Catch:{ Exception -> 0x0a40, all -> 0x0a2c }
                r2 = new java.lang.Object[r15];	 Catch:{ Exception -> 0x0a40, all -> 0x0a2c }
                r59 = r3;
                r58 = r4;
                r3 = r5;	 Catch:{ Exception -> 0x0a1a, all -> 0x0a08 }
                r3 = java.lang.Long.valueOf(r3);	 Catch:{ Exception -> 0x0a1a, all -> 0x0a08 }
                r4 = 0;	 Catch:{ Exception -> 0x0a1a, all -> 0x0a08 }
                r2[r4] = r3;	 Catch:{ Exception -> 0x0a1a, all -> 0x0a08 }
                r3 = r8;	 Catch:{ Exception -> 0x0a1a, all -> 0x0a08 }
                r3 = java.lang.Integer.valueOf(r3);	 Catch:{ Exception -> 0x0a1a, all -> 0x0a08 }
                r4 = 1;	 Catch:{ Exception -> 0x0a1a, all -> 0x0a08 }
                r2[r4] = r3;	 Catch:{ Exception -> 0x0a1a, all -> 0x0a08 }
                r3 = java.lang.Long.valueOf(r11);	 Catch:{ Exception -> 0x0a1a, all -> 0x0a08 }
                r4 = 2;	 Catch:{ Exception -> 0x0a1a, all -> 0x0a08 }
                r2[r4] = r3;	 Catch:{ Exception -> 0x0a1a, all -> 0x0a08 }
                r3 = java.lang.Long.valueOf(r7);	 Catch:{ Exception -> 0x0a1a, all -> 0x0a08 }
                r4 = 3;	 Catch:{ Exception -> 0x0a1a, all -> 0x0a08 }
                r2[r4] = r3;	 Catch:{ Exception -> 0x0a1a, all -> 0x0a08 }
                r3 = java.lang.Integer.valueOf(r5);	 Catch:{ Exception -> 0x0a1a, all -> 0x0a08 }
                r4 = 4;	 Catch:{ Exception -> 0x0a1a, all -> 0x0a08 }
                r2[r4] = r3;	 Catch:{ Exception -> 0x0a1a, all -> 0x0a08 }
                r2 = java.lang.String.format(r13, r14, r2);	 Catch:{ Exception -> 0x0a1a, all -> 0x0a08 }
                r3 = 0;	 Catch:{ Exception -> 0x0a1a, all -> 0x0a08 }
                r4 = new java.lang.Object[r3];	 Catch:{ Exception -> 0x0a1a, all -> 0x0a08 }
                r2 = r10.queryFinalized(r2, r4);	 Catch:{ Exception -> 0x0a1a, all -> 0x0a08 }
                goto L_0x09f5;	 Catch:{ Exception -> 0x0a1a, all -> 0x0a08 }
            L_0x09b7:
                r57 = r2;	 Catch:{ Exception -> 0x0a1a, all -> 0x0a08 }
                r59 = r3;	 Catch:{ Exception -> 0x0a1a, all -> 0x0a08 }
                r58 = r4;	 Catch:{ Exception -> 0x0a1a, all -> 0x0a08 }
                r2 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x0a1a, all -> 0x0a08 }
                r2 = r2.database;	 Catch:{ Exception -> 0x0a1a, all -> 0x0a08 }
                r3 = java.util.Locale.US;	 Catch:{ Exception -> 0x0a1a, all -> 0x0a08 }
                r4 = "SELECT m.read_state, m.data, m.send_state, m.mid, m.date, r.random_id, m.replydata, m.media, m.ttl, m.mention FROM messages as m LEFT JOIN randoms as r ON r.mid = m.mid WHERE m.uid = %d AND m.date >= %d AND m.mid > %d ORDER BY m.date ASC, m.mid ASC LIMIT %d";	 Catch:{ Exception -> 0x0a1a, all -> 0x0a08 }
                r10 = 4;	 Catch:{ Exception -> 0x0a1a, all -> 0x0a08 }
                r13 = new java.lang.Object[r10];	 Catch:{ Exception -> 0x0a1a, all -> 0x0a08 }
                r14 = r5;	 Catch:{ Exception -> 0x0a1a, all -> 0x0a08 }
                r10 = java.lang.Long.valueOf(r14);	 Catch:{ Exception -> 0x0a1a, all -> 0x0a08 }
                r14 = 0;	 Catch:{ Exception -> 0x0a1a, all -> 0x0a08 }
                r13[r14] = r10;	 Catch:{ Exception -> 0x0a1a, all -> 0x0a08 }
                r10 = r8;	 Catch:{ Exception -> 0x0a1a, all -> 0x0a08 }
                r10 = java.lang.Integer.valueOf(r10);	 Catch:{ Exception -> 0x0a1a, all -> 0x0a08 }
                r14 = 1;	 Catch:{ Exception -> 0x0a1a, all -> 0x0a08 }
                r13[r14] = r10;	 Catch:{ Exception -> 0x0a1a, all -> 0x0a08 }
                r10 = java.lang.Long.valueOf(r11);	 Catch:{ Exception -> 0x0a1a, all -> 0x0a08 }
                r14 = 2;	 Catch:{ Exception -> 0x0a1a, all -> 0x0a08 }
                r13[r14] = r10;	 Catch:{ Exception -> 0x0a1a, all -> 0x0a08 }
                r10 = java.lang.Integer.valueOf(r5);	 Catch:{ Exception -> 0x0a1a, all -> 0x0a08 }
                r14 = 3;	 Catch:{ Exception -> 0x0a1a, all -> 0x0a08 }
                r13[r14] = r10;	 Catch:{ Exception -> 0x0a1a, all -> 0x0a08 }
                r3 = java.lang.String.format(r3, r4, r13);	 Catch:{ Exception -> 0x0a1a, all -> 0x0a08 }
                r4 = 0;	 Catch:{ Exception -> 0x0a1a, all -> 0x0a08 }
                r10 = new java.lang.Object[r4];	 Catch:{ Exception -> 0x0a1a, all -> 0x0a08 }
                r2 = r2.queryFinalized(r3, r10);	 Catch:{ Exception -> 0x0a1a, all -> 0x0a08 }
                r67 = r5;
                r48 = r6;
                r29 = r9;
                r65 = r11;
                r3 = r47;
                r9 = r52;
                r15 = r53;
                r77 = r58;
                goto L_0x1b77;
            L_0x0a08:
                r0 = move-exception;
                r2 = r0;
                r67 = r5;
                r48 = r6;
                r29 = r9;
                r65 = r11;
                r22 = r38;
                r27 = r52;
                r26 = r58;
                goto L_0x242b;
            L_0x0a1a:
                r0 = move-exception;
                r2 = r0;
                r67 = r5;
                r48 = r6;
                r29 = r9;
                r65 = r11;
                r95 = r38;
                r27 = r52;
                r26 = r58;
                goto L_0x23dc;
            L_0x0a2c:
                r0 = move-exception;
                r59 = r3;
                r2 = r0;
                r26 = r4;
                r67 = r5;
                r48 = r6;
                r29 = r9;
                r65 = r11;
                r22 = r38;
                r27 = r52;
                goto L_0x242b;
            L_0x0a40:
                r0 = move-exception;
                r59 = r3;
                r2 = r0;
                r26 = r4;
                r67 = r5;
                r48 = r6;
                r29 = r9;
                r65 = r11;
                r95 = r38;
                r27 = r52;
                goto L_0x23dc;
            L_0x0a54:
                r54 = r2;
                r59 = r3;
                r58 = r4;
                r2 = r8;	 Catch:{ Exception -> 0x0df2, all -> 0x0de0 }
                if (r2 == 0) goto L_0x0c77;
                r2 = 0;
                r4 = (r11 > r2 ? 1 : (r11 == r2 ? 0 : -1));
                if (r4 == 0) goto L_0x0bdb;
                r2 = 0;
                r4 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x0bc7, all -> 0x0bb3 }
                r4 = r4.database;	 Catch:{ Exception -> 0x0bc7, all -> 0x0bb3 }
                r7 = java.util.Locale.US;	 Catch:{ Exception -> 0x0bc7, all -> 0x0bb3 }
                r8 = "SELECT end FROM messages_holes WHERE uid = %d AND end <= %d ORDER BY end DESC LIMIT 1";	 Catch:{ Exception -> 0x0bc7, all -> 0x0bb3 }
                r10 = 2;	 Catch:{ Exception -> 0x0bc7, all -> 0x0bb3 }
                r13 = new java.lang.Object[r10];	 Catch:{ Exception -> 0x0bc7, all -> 0x0bb3 }
                r14 = r5;	 Catch:{ Exception -> 0x0bc7, all -> 0x0bb3 }
                r10 = java.lang.Long.valueOf(r14);	 Catch:{ Exception -> 0x0bc7, all -> 0x0bb3 }
                r14 = 0;	 Catch:{ Exception -> 0x0bc7, all -> 0x0bb3 }
                r13[r14] = r10;	 Catch:{ Exception -> 0x0bc7, all -> 0x0bb3 }
                r10 = r3;	 Catch:{ Exception -> 0x0bc7, all -> 0x0bb3 }
                r10 = java.lang.Integer.valueOf(r10);	 Catch:{ Exception -> 0x0bc7, all -> 0x0bb3 }
                r14 = 1;	 Catch:{ Exception -> 0x0bc7, all -> 0x0bb3 }
                r13[r14] = r10;	 Catch:{ Exception -> 0x0bc7, all -> 0x0bb3 }
                r7 = java.lang.String.format(r7, r8, r13);	 Catch:{ Exception -> 0x0bc7, all -> 0x0bb3 }
                r8 = 0;	 Catch:{ Exception -> 0x0bc7, all -> 0x0bb3 }
                r10 = new java.lang.Object[r8];	 Catch:{ Exception -> 0x0bc7, all -> 0x0bb3 }
                r4 = r4.queryFinalized(r7, r10);	 Catch:{ Exception -> 0x0bc7, all -> 0x0bb3 }
                r7 = r4.next();	 Catch:{ Exception -> 0x0bc7, all -> 0x0bb3 }
                if (r7 == 0) goto L_0x0acc;
                r7 = 0;
                r8 = r4.intValue(r7);	 Catch:{ Exception -> 0x0aba, all -> 0x0aa8 }
                r2 = (long) r8;
                if (r58 == 0) goto L_0x0acc;
                r7 = r58;
                r13 = (long) r7;
                r13 = r13 << r16;
                r17 = r2 | r13;
                r2 = r17;
                goto L_0x0ace;
            L_0x0aa8:
                r0 = move-exception;
                r2 = r0;
                r67 = r5;
                r48 = r6;
                r29 = r9;
                r65 = r11;
                r22 = r38;
                r27 = r52;
                r26 = r58;
                goto L_0x242b;
            L_0x0aba:
                r0 = move-exception;
                r2 = r0;
                r67 = r5;
                r48 = r6;
                r29 = r9;
                r65 = r11;
                r95 = r38;
                r27 = r52;
                r26 = r58;
                goto L_0x23dc;
                r7 = r58;
                r4.dispose();	 Catch:{ Exception -> 0x0b9f, all -> 0x0b8b }
                r13 = 0;	 Catch:{ Exception -> 0x0b9f, all -> 0x0b8b }
                r8 = (r2 > r13 ? 1 : (r2 == r13 ? 0 : -1));	 Catch:{ Exception -> 0x0b9f, all -> 0x0b8b }
                if (r8 == 0) goto L_0x0b1d;	 Catch:{ Exception -> 0x0b9f, all -> 0x0b8b }
                r8 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x0b9f, all -> 0x0b8b }
                r8 = r8.database;	 Catch:{ Exception -> 0x0b9f, all -> 0x0b8b }
                r10 = java.util.Locale.US;	 Catch:{ Exception -> 0x0b9f, all -> 0x0b8b }
                r13 = "SELECT m.read_state, m.data, m.send_state, m.mid, m.date, r.random_id, m.replydata, m.media, m.ttl, m.mention FROM messages as m LEFT JOIN randoms as r ON r.mid = m.mid WHERE m.uid = %d AND m.date <= %d AND m.mid < %d AND (m.mid >= %d OR m.mid < 0) ORDER BY m.date DESC, m.mid DESC LIMIT %d";	 Catch:{ Exception -> 0x0b9f, all -> 0x0b8b }
                r14 = 5;	 Catch:{ Exception -> 0x0b9f, all -> 0x0b8b }
                r15 = new java.lang.Object[r14];	 Catch:{ Exception -> 0x0b9f, all -> 0x0b8b }
                r60 = r6;
                r61 = r7;
                r6 = r5;	 Catch:{ Exception -> 0x0b7b, all -> 0x0b6b }
                r6 = java.lang.Long.valueOf(r6);	 Catch:{ Exception -> 0x0b7b, all -> 0x0b6b }
                r7 = 0;	 Catch:{ Exception -> 0x0b7b, all -> 0x0b6b }
                r15[r7] = r6;	 Catch:{ Exception -> 0x0b7b, all -> 0x0b6b }
                r6 = r8;	 Catch:{ Exception -> 0x0b7b, all -> 0x0b6b }
                r6 = java.lang.Integer.valueOf(r6);	 Catch:{ Exception -> 0x0b7b, all -> 0x0b6b }
                r7 = 1;	 Catch:{ Exception -> 0x0b7b, all -> 0x0b6b }
                r15[r7] = r6;	 Catch:{ Exception -> 0x0b7b, all -> 0x0b6b }
                r6 = java.lang.Long.valueOf(r11);	 Catch:{ Exception -> 0x0b7b, all -> 0x0b6b }
                r7 = 2;	 Catch:{ Exception -> 0x0b7b, all -> 0x0b6b }
                r15[r7] = r6;	 Catch:{ Exception -> 0x0b7b, all -> 0x0b6b }
                r6 = java.lang.Long.valueOf(r2);	 Catch:{ Exception -> 0x0b7b, all -> 0x0b6b }
                r7 = 3;	 Catch:{ Exception -> 0x0b7b, all -> 0x0b6b }
                r15[r7] = r6;	 Catch:{ Exception -> 0x0b7b, all -> 0x0b6b }
                r6 = java.lang.Integer.valueOf(r5);	 Catch:{ Exception -> 0x0b7b, all -> 0x0b6b }
                r7 = 4;	 Catch:{ Exception -> 0x0b7b, all -> 0x0b6b }
                r15[r7] = r6;	 Catch:{ Exception -> 0x0b7b, all -> 0x0b6b }
                r6 = java.lang.String.format(r10, r13, r15);	 Catch:{ Exception -> 0x0b7b, all -> 0x0b6b }
                r7 = 0;	 Catch:{ Exception -> 0x0b7b, all -> 0x0b6b }
                r10 = new java.lang.Object[r7];	 Catch:{ Exception -> 0x0b7b, all -> 0x0b6b }
                r6 = r8.queryFinalized(r6, r10);	 Catch:{ Exception -> 0x0b7b, all -> 0x0b6b }
                r4 = r6;	 Catch:{ Exception -> 0x0b7b, all -> 0x0b6b }
                r2 = r4;	 Catch:{ Exception -> 0x0b7b, all -> 0x0b6b }
                goto L_0x0b5a;	 Catch:{ Exception -> 0x0b7b, all -> 0x0b6b }
                r60 = r6;	 Catch:{ Exception -> 0x0b7b, all -> 0x0b6b }
                r61 = r7;	 Catch:{ Exception -> 0x0b7b, all -> 0x0b6b }
                r6 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x0b7b, all -> 0x0b6b }
                r6 = r6.database;	 Catch:{ Exception -> 0x0b7b, all -> 0x0b6b }
                r7 = java.util.Locale.US;	 Catch:{ Exception -> 0x0b7b, all -> 0x0b6b }
                r8 = "SELECT m.read_state, m.data, m.send_state, m.mid, m.date, r.random_id, m.replydata, m.media, m.ttl, m.mention FROM messages as m LEFT JOIN randoms as r ON r.mid = m.mid WHERE m.uid = %d AND m.date <= %d AND m.mid < %d ORDER BY m.date DESC, m.mid DESC LIMIT %d";	 Catch:{ Exception -> 0x0b7b, all -> 0x0b6b }
                r10 = 4;	 Catch:{ Exception -> 0x0b7b, all -> 0x0b6b }
                r13 = new java.lang.Object[r10];	 Catch:{ Exception -> 0x0b7b, all -> 0x0b6b }
                r14 = r5;	 Catch:{ Exception -> 0x0b7b, all -> 0x0b6b }
                r10 = java.lang.Long.valueOf(r14);	 Catch:{ Exception -> 0x0b7b, all -> 0x0b6b }
                r14 = 0;	 Catch:{ Exception -> 0x0b7b, all -> 0x0b6b }
                r13[r14] = r10;	 Catch:{ Exception -> 0x0b7b, all -> 0x0b6b }
                r10 = r8;	 Catch:{ Exception -> 0x0b7b, all -> 0x0b6b }
                r10 = java.lang.Integer.valueOf(r10);	 Catch:{ Exception -> 0x0b7b, all -> 0x0b6b }
                r14 = 1;	 Catch:{ Exception -> 0x0b7b, all -> 0x0b6b }
                r13[r14] = r10;	 Catch:{ Exception -> 0x0b7b, all -> 0x0b6b }
                r10 = java.lang.Long.valueOf(r11);	 Catch:{ Exception -> 0x0b7b, all -> 0x0b6b }
                r14 = 2;	 Catch:{ Exception -> 0x0b7b, all -> 0x0b6b }
                r13[r14] = r10;	 Catch:{ Exception -> 0x0b7b, all -> 0x0b6b }
                r10 = java.lang.Integer.valueOf(r5);	 Catch:{ Exception -> 0x0b7b, all -> 0x0b6b }
                r14 = 3;	 Catch:{ Exception -> 0x0b7b, all -> 0x0b6b }
                r13[r14] = r10;	 Catch:{ Exception -> 0x0b7b, all -> 0x0b6b }
                r7 = java.lang.String.format(r7, r8, r13);	 Catch:{ Exception -> 0x0b7b, all -> 0x0b6b }
                r8 = 0;	 Catch:{ Exception -> 0x0b7b, all -> 0x0b6b }
                r10 = new java.lang.Object[r8];	 Catch:{ Exception -> 0x0b7b, all -> 0x0b6b }
                r6 = r6.queryFinalized(r7, r10);	 Catch:{ Exception -> 0x0b7b, all -> 0x0b6b }
                r2 = r6;
                r67 = r5;
                r29 = r9;
                r65 = r11;
                r3 = r47;
                r9 = r52;
                r15 = r53;
                r48 = r60;
                goto L_0x0c27;
            L_0x0b6b:
                r0 = move-exception;
                r2 = r0;
                r67 = r5;
                r29 = r9;
                r65 = r11;
                r22 = r38;
                r27 = r52;
                r48 = r60;
                goto L_0x0c39;
            L_0x0b7b:
                r0 = move-exception;
                r2 = r0;
                r67 = r5;
                r29 = r9;
                r65 = r11;
                r95 = r38;
                r27 = r52;
                r48 = r60;
                goto L_0x0c4b;
            L_0x0b8b:
                r0 = move-exception;
                r60 = r6;
                r2 = r0;
                r67 = r5;
                r26 = r7;
                r29 = r9;
                r65 = r11;
                r22 = r38;
                r27 = r52;
                r48 = r60;
                goto L_0x242b;
            L_0x0b9f:
                r0 = move-exception;
                r60 = r6;
                r2 = r0;
                r67 = r5;
                r26 = r7;
                r29 = r9;
                r65 = r11;
                r95 = r38;
                r27 = r52;
                r48 = r60;
                goto L_0x23dc;
            L_0x0bb3:
                r0 = move-exception;
                r60 = r6;
                r2 = r0;
                r67 = r5;
                r29 = r9;
                r65 = r11;
                r22 = r38;
                r27 = r52;
                r26 = r58;
                r48 = r60;
                goto L_0x242b;
            L_0x0bc7:
                r0 = move-exception;
                r60 = r6;
                r2 = r0;
                r67 = r5;
                r29 = r9;
                r65 = r11;
                r95 = r38;
                r27 = r52;
                r26 = r58;
                r48 = r60;
                goto L_0x23dc;
                r60 = r6;
                r61 = r58;
                r2 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x0c63, all -> 0x0c4f }
                r2 = r2.database;	 Catch:{ Exception -> 0x0c63, all -> 0x0c4f }
                r3 = java.util.Locale.US;	 Catch:{ Exception -> 0x0c63, all -> 0x0c4f }
                r4 = "SELECT m.read_state, m.data, m.send_state, m.mid, m.date, r.random_id, m.replydata, m.media, m.ttl, m.mention FROM messages as m LEFT JOIN randoms as r ON r.mid = m.mid WHERE m.uid = %d AND m.date <= %d ORDER BY m.date DESC, m.mid DESC LIMIT %d,%d";	 Catch:{ Exception -> 0x0c63, all -> 0x0c4f }
                r6 = 4;	 Catch:{ Exception -> 0x0c63, all -> 0x0c4f }
                r7 = new java.lang.Object[r6];	 Catch:{ Exception -> 0x0c63, all -> 0x0c4f }
                r13 = r5;	 Catch:{ Exception -> 0x0c63, all -> 0x0c4f }
                r6 = java.lang.Long.valueOf(r13);	 Catch:{ Exception -> 0x0c63, all -> 0x0c4f }
                r8 = 0;	 Catch:{ Exception -> 0x0c63, all -> 0x0c4f }
                r7[r8] = r6;	 Catch:{ Exception -> 0x0c63, all -> 0x0c4f }
                r6 = r8;	 Catch:{ Exception -> 0x0c63, all -> 0x0c4f }
                r6 = java.lang.Integer.valueOf(r6);	 Catch:{ Exception -> 0x0c63, all -> 0x0c4f }
                r8 = 1;	 Catch:{ Exception -> 0x0c63, all -> 0x0c4f }
                r7[r8] = r6;	 Catch:{ Exception -> 0x0c63, all -> 0x0c4f }
                r6 = r60;
                r8 = java.lang.Integer.valueOf(r6);	 Catch:{ Exception -> 0x0c3d, all -> 0x0c2b }
                r10 = 2;	 Catch:{ Exception -> 0x0c3d, all -> 0x0c2b }
                r7[r10] = r8;	 Catch:{ Exception -> 0x0c3d, all -> 0x0c2b }
                r8 = java.lang.Integer.valueOf(r5);	 Catch:{ Exception -> 0x0c3d, all -> 0x0c2b }
                r10 = 3;	 Catch:{ Exception -> 0x0c3d, all -> 0x0c2b }
                r7[r10] = r8;	 Catch:{ Exception -> 0x0c3d, all -> 0x0c2b }
                r3 = java.lang.String.format(r3, r4, r7);	 Catch:{ Exception -> 0x0c3d, all -> 0x0c2b }
                r4 = 0;	 Catch:{ Exception -> 0x0c3d, all -> 0x0c2b }
                r7 = new java.lang.Object[r4];	 Catch:{ Exception -> 0x0c3d, all -> 0x0c2b }
                r2 = r2.queryFinalized(r3, r7);	 Catch:{ Exception -> 0x0c3d, all -> 0x0c2b }
                r67 = r5;
                r48 = r6;
                r29 = r9;
                r65 = r11;
                r3 = r47;
                r9 = r52;
                r15 = r53;
                r77 = r61;
                goto L_0x1b77;
            L_0x0c2b:
                r0 = move-exception;
                r2 = r0;
                r67 = r5;
                r48 = r6;
                r29 = r9;
                r65 = r11;
                r22 = r38;
                r27 = r52;
                r26 = r61;
                goto L_0x242b;
            L_0x0c3d:
                r0 = move-exception;
                r2 = r0;
                r67 = r5;
                r48 = r6;
                r29 = r9;
                r65 = r11;
                r95 = r38;
                r27 = r52;
                r26 = r61;
                goto L_0x23dc;
            L_0x0c4f:
                r0 = move-exception;
                r6 = r60;
                r2 = r0;
                r67 = r5;
                r48 = r6;
                r29 = r9;
                r65 = r11;
                r22 = r38;
                r27 = r52;
                r26 = r61;
                goto L_0x242b;
            L_0x0c63:
                r0 = move-exception;
                r6 = r60;
                r2 = r0;
                r67 = r5;
                r48 = r6;
                r29 = r9;
                r65 = r11;
                r95 = r38;
                r27 = r52;
                r26 = r61;
                goto L_0x23dc;
                r61 = r58;
                r2 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x0dce, all -> 0x0dbc }
                r2 = r2.database;	 Catch:{ Exception -> 0x0dce, all -> 0x0dbc }
                r3 = java.util.Locale.US;	 Catch:{ Exception -> 0x0dce, all -> 0x0dbc }
                r4 = "SELECT max(mid) FROM messages WHERE uid = %d AND mid > 0";	 Catch:{ Exception -> 0x0dce, all -> 0x0dbc }
                r7 = 1;	 Catch:{ Exception -> 0x0dce, all -> 0x0dbc }
                r8 = new java.lang.Object[r7];	 Catch:{ Exception -> 0x0dce, all -> 0x0dbc }
                r13 = r5;	 Catch:{ Exception -> 0x0dce, all -> 0x0dbc }
                r7 = java.lang.Long.valueOf(r13);	 Catch:{ Exception -> 0x0dce, all -> 0x0dbc }
                r10 = 0;	 Catch:{ Exception -> 0x0dce, all -> 0x0dbc }
                r8[r10] = r7;	 Catch:{ Exception -> 0x0dce, all -> 0x0dbc }
                r3 = java.lang.String.format(r3, r4, r8);	 Catch:{ Exception -> 0x0dce, all -> 0x0dbc }
                r4 = new java.lang.Object[r10];	 Catch:{ Exception -> 0x0dce, all -> 0x0dbc }
                r2 = r2.queryFinalized(r3, r4);	 Catch:{ Exception -> 0x0dce, all -> 0x0dbc }
                r3 = r2.next();	 Catch:{ Exception -> 0x0dce, all -> 0x0dbc }
                if (r3 == 0) goto L_0x0ca6;
                r3 = 0;
                r4 = r2.intValue(r3);	 Catch:{ Exception -> 0x0c3d, all -> 0x0c2b }
                r25 = r4;
                r2.dispose();	 Catch:{ Exception -> 0x0dce, all -> 0x0dbc }
                r3 = 0;	 Catch:{ Exception -> 0x0dce, all -> 0x0dbc }
                r7 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x0dce, all -> 0x0dbc }
                r7 = r7.database;	 Catch:{ Exception -> 0x0dce, all -> 0x0dbc }
                r8 = java.util.Locale.US;	 Catch:{ Exception -> 0x0dce, all -> 0x0dbc }
                r10 = "SELECT max(end) FROM messages_holes WHERE uid = %d";	 Catch:{ Exception -> 0x0dce, all -> 0x0dbc }
                r13 = 1;	 Catch:{ Exception -> 0x0dce, all -> 0x0dbc }
                r14 = new java.lang.Object[r13];	 Catch:{ Exception -> 0x0dce, all -> 0x0dbc }
                r62 = r2;	 Catch:{ Exception -> 0x0dce, all -> 0x0dbc }
                r63 = r3;	 Catch:{ Exception -> 0x0dce, all -> 0x0dbc }
                r2 = r5;	 Catch:{ Exception -> 0x0dce, all -> 0x0dbc }
                r2 = java.lang.Long.valueOf(r2);	 Catch:{ Exception -> 0x0dce, all -> 0x0dbc }
                r3 = 0;	 Catch:{ Exception -> 0x0dce, all -> 0x0dbc }
                r14[r3] = r2;	 Catch:{ Exception -> 0x0dce, all -> 0x0dbc }
                r2 = java.lang.String.format(r8, r10, r14);	 Catch:{ Exception -> 0x0dce, all -> 0x0dbc }
                r4 = new java.lang.Object[r3];	 Catch:{ Exception -> 0x0dce, all -> 0x0dbc }
                r2 = r7.queryFinalized(r2, r4);	 Catch:{ Exception -> 0x0dce, all -> 0x0dbc }
                r3 = r2.next();	 Catch:{ Exception -> 0x0dce, all -> 0x0dbc }
                if (r3 == 0) goto L_0x0d0e;
                r3 = 0;
                r4 = r2.intValue(r3);	 Catch:{ Exception -> 0x0cfc, all -> 0x0cea }
                r3 = (long) r4;
                if (r61 == 0) goto L_0x0ce7;
                r7 = r61;
                r13 = (long) r7;
                r13 = r13 << r16;
                r17 = r3 | r13;
                r3 = r17;
                goto L_0x0d12;
                r7 = r61;
                goto L_0x0d12;
            L_0x0cea:
                r0 = move-exception;
                r2 = r0;
                r67 = r5;
                r48 = r6;
                r29 = r9;
                r65 = r11;
                r22 = r38;
                r27 = r52;
                r26 = r61;
                goto L_0x242b;
            L_0x0cfc:
                r0 = move-exception;
                r2 = r0;
                r67 = r5;
                r48 = r6;
                r29 = r9;
                r65 = r11;
                r95 = r38;
                r27 = r52;
                r26 = r61;
                goto L_0x23dc;
                r7 = r61;
                r3 = r63;
                r2.dispose();	 Catch:{ Exception -> 0x0daa, all -> 0x0d98 }
                r13 = 0;	 Catch:{ Exception -> 0x0daa, all -> 0x0d98 }
                r8 = (r3 > r13 ? 1 : (r3 == r13 ? 0 : -1));	 Catch:{ Exception -> 0x0daa, all -> 0x0d98 }
                if (r8 == 0) goto L_0x0d55;	 Catch:{ Exception -> 0x0daa, all -> 0x0d98 }
                r8 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x0daa, all -> 0x0d98 }
                r8 = r8.database;	 Catch:{ Exception -> 0x0daa, all -> 0x0d98 }
                r10 = java.util.Locale.US;	 Catch:{ Exception -> 0x0daa, all -> 0x0d98 }
                r13 = "SELECT m.read_state, m.data, m.send_state, m.mid, m.date, r.random_id, m.replydata, m.media, m.ttl, m.mention FROM messages as m LEFT JOIN randoms as r ON r.mid = m.mid WHERE m.uid = %d AND (m.mid >= %d OR m.mid < 0) ORDER BY m.date DESC, m.mid DESC LIMIT %d,%d";	 Catch:{ Exception -> 0x0daa, all -> 0x0d98 }
                r14 = 4;	 Catch:{ Exception -> 0x0daa, all -> 0x0d98 }
                r15 = new java.lang.Object[r14];	 Catch:{ Exception -> 0x0daa, all -> 0x0d98 }
                r65 = r11;
                r11 = r5;	 Catch:{ Exception -> 0x0e6d, all -> 0x0e61 }
                r11 = java.lang.Long.valueOf(r11);	 Catch:{ Exception -> 0x0e6d, all -> 0x0e61 }
                r12 = 0;	 Catch:{ Exception -> 0x0e6d, all -> 0x0e61 }
                r15[r12] = r11;	 Catch:{ Exception -> 0x0e6d, all -> 0x0e61 }
                r11 = java.lang.Long.valueOf(r3);	 Catch:{ Exception -> 0x0e6d, all -> 0x0e61 }
                r12 = 1;	 Catch:{ Exception -> 0x0e6d, all -> 0x0e61 }
                r15[r12] = r11;	 Catch:{ Exception -> 0x0e6d, all -> 0x0e61 }
                r11 = java.lang.Integer.valueOf(r6);	 Catch:{ Exception -> 0x0e6d, all -> 0x0e61 }
                r12 = 2;	 Catch:{ Exception -> 0x0e6d, all -> 0x0e61 }
                r15[r12] = r11;	 Catch:{ Exception -> 0x0e6d, all -> 0x0e61 }
                r11 = java.lang.Integer.valueOf(r5);	 Catch:{ Exception -> 0x0e6d, all -> 0x0e61 }
                r12 = 3;	 Catch:{ Exception -> 0x0e6d, all -> 0x0e61 }
                r15[r12] = r11;	 Catch:{ Exception -> 0x0e6d, all -> 0x0e61 }
                r10 = java.lang.String.format(r10, r13, r15);	 Catch:{ Exception -> 0x0e6d, all -> 0x0e61 }
                r11 = 0;	 Catch:{ Exception -> 0x0e6d, all -> 0x0e61 }
                r12 = new java.lang.Object[r11];	 Catch:{ Exception -> 0x0e6d, all -> 0x0e61 }
                r8 = r8.queryFinalized(r10, r12);	 Catch:{ Exception -> 0x0e6d, all -> 0x0e61 }
                r2 = r8;	 Catch:{ Exception -> 0x0e6d, all -> 0x0e61 }
                goto L_0x0d87;	 Catch:{ Exception -> 0x0e6d, all -> 0x0e61 }
                r65 = r11;	 Catch:{ Exception -> 0x0e6d, all -> 0x0e61 }
                r8 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x0e6d, all -> 0x0e61 }
                r8 = r8.database;	 Catch:{ Exception -> 0x0e6d, all -> 0x0e61 }
                r10 = java.util.Locale.US;	 Catch:{ Exception -> 0x0e6d, all -> 0x0e61 }
                r11 = "SELECT m.read_state, m.data, m.send_state, m.mid, m.date, r.random_id, m.replydata, m.media, m.ttl, m.mention FROM messages as m LEFT JOIN randoms as r ON r.mid = m.mid WHERE m.uid = %d ORDER BY m.date DESC, m.mid DESC LIMIT %d,%d";	 Catch:{ Exception -> 0x0e6d, all -> 0x0e61 }
                r12 = 3;	 Catch:{ Exception -> 0x0e6d, all -> 0x0e61 }
                r13 = new java.lang.Object[r12];	 Catch:{ Exception -> 0x0e6d, all -> 0x0e61 }
                r14 = r5;	 Catch:{ Exception -> 0x0e6d, all -> 0x0e61 }
                r12 = java.lang.Long.valueOf(r14);	 Catch:{ Exception -> 0x0e6d, all -> 0x0e61 }
                r14 = 0;	 Catch:{ Exception -> 0x0e6d, all -> 0x0e61 }
                r13[r14] = r12;	 Catch:{ Exception -> 0x0e6d, all -> 0x0e61 }
                r12 = java.lang.Integer.valueOf(r6);	 Catch:{ Exception -> 0x0e6d, all -> 0x0e61 }
                r14 = 1;	 Catch:{ Exception -> 0x0e6d, all -> 0x0e61 }
                r13[r14] = r12;	 Catch:{ Exception -> 0x0e6d, all -> 0x0e61 }
                r12 = java.lang.Integer.valueOf(r5);	 Catch:{ Exception -> 0x0e6d, all -> 0x0e61 }
                r14 = 2;	 Catch:{ Exception -> 0x0e6d, all -> 0x0e61 }
                r13[r14] = r12;	 Catch:{ Exception -> 0x0e6d, all -> 0x0e61 }
                r10 = java.lang.String.format(r10, r11, r13);	 Catch:{ Exception -> 0x0e6d, all -> 0x0e61 }
                r11 = 0;	 Catch:{ Exception -> 0x0e6d, all -> 0x0e61 }
                r12 = new java.lang.Object[r11];	 Catch:{ Exception -> 0x0e6d, all -> 0x0e61 }
                r8 = r8.queryFinalized(r10, r12);	 Catch:{ Exception -> 0x0e6d, all -> 0x0e61 }
                r2 = r8;
                r67 = r5;
                r48 = r6;
                r77 = r7;
                r29 = r9;
                r3 = r47;
                r9 = r52;
                r15 = r53;
                goto L_0x1b77;
            L_0x0d98:
                r0 = move-exception;
                r65 = r11;
                r2 = r0;
                r67 = r5;
                r48 = r6;
                r26 = r7;
                r29 = r9;
                r22 = r38;
                r27 = r52;
                goto L_0x242b;
            L_0x0daa:
                r0 = move-exception;
                r65 = r11;
                r2 = r0;
                r67 = r5;
                r48 = r6;
                r26 = r7;
                r29 = r9;
                r95 = r38;
                r27 = r52;
                goto L_0x23dc;
            L_0x0dbc:
                r0 = move-exception;
                r65 = r11;
                r2 = r0;
                r67 = r5;
                r48 = r6;
                r29 = r9;
                r22 = r38;
                r27 = r52;
                r26 = r61;
                goto L_0x242b;
            L_0x0dce:
                r0 = move-exception;
                r65 = r11;
                r2 = r0;
                r67 = r5;
                r48 = r6;
                r29 = r9;
                r95 = r38;
                r27 = r52;
                r26 = r61;
                goto L_0x23dc;
            L_0x0de0:
                r0 = move-exception;
                r65 = r11;
                r2 = r0;
                r67 = r5;
                r48 = r6;
                r29 = r9;
                r22 = r38;
                r27 = r52;
                r26 = r58;
                goto L_0x242b;
            L_0x0df2:
                r0 = move-exception;
                r65 = r11;
                r2 = r0;
                r67 = r5;
                r48 = r6;
                r29 = r9;
                r95 = r38;
                r27 = r52;
                r26 = r58;
                goto L_0x23dc;
            L_0x0e04:
                r0 = move-exception;
                r59 = r3;
                r65 = r11;
                r2 = r0;
                r26 = r4;
                r67 = r5;
                r48 = r6;
                r29 = r9;
                r22 = r38;
                r27 = r52;
                goto L_0x242b;
            L_0x0e18:
                r0 = move-exception;
                r59 = r3;
                r65 = r11;
                r2 = r0;
                r26 = r4;
                r67 = r5;
                r48 = r6;
                r29 = r9;
                r95 = r38;
                r27 = r52;
                goto L_0x23dc;
            L_0x0e2c:
                r54 = r2;
                r59 = r3;
                r7 = r4;
                r65 = r11;
            L_0x0e33:
                r2 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x1361, all -> 0x134d }
                r2 = r2.database;	 Catch:{ Exception -> 0x1361, all -> 0x134d }
                r3 = java.util.Locale.US;	 Catch:{ Exception -> 0x1361, all -> 0x134d }
                r4 = "SELECT max(mid) FROM messages WHERE uid = %d AND mid > 0";	 Catch:{ Exception -> 0x1361, all -> 0x134d }
                r8 = 1;	 Catch:{ Exception -> 0x1361, all -> 0x134d }
                r10 = new java.lang.Object[r8];	 Catch:{ Exception -> 0x1361, all -> 0x134d }
                r11 = r5;	 Catch:{ Exception -> 0x1361, all -> 0x134d }
                r8 = java.lang.Long.valueOf(r11);	 Catch:{ Exception -> 0x1361, all -> 0x134d }
                r11 = 0;	 Catch:{ Exception -> 0x1361, all -> 0x134d }
                r10[r11] = r8;	 Catch:{ Exception -> 0x1361, all -> 0x134d }
                r3 = java.lang.String.format(r3, r4, r10);	 Catch:{ Exception -> 0x1361, all -> 0x134d }
                r4 = new java.lang.Object[r11];	 Catch:{ Exception -> 0x1361, all -> 0x134d }
                r2 = r2.queryFinalized(r3, r4);	 Catch:{ Exception -> 0x1361, all -> 0x134d }
                r3 = r2.next();	 Catch:{ Exception -> 0x1361, all -> 0x134d }
                if (r3 == 0) goto L_0x0e79;
                r3 = 0;
                r4 = r2.intValue(r3);	 Catch:{ Exception -> 0x0e6d, all -> 0x0e61 }
                r25 = r4;
                goto L_0x0e79;
            L_0x0e61:
                r0 = move-exception;
                r2 = r0;
                r67 = r5;
                r48 = r6;
                r26 = r7;
                r29 = r9;
                goto L_0x086a;
            L_0x0e6d:
                r0 = move-exception;
                r2 = r0;
                r67 = r5;
                r48 = r6;
                r26 = r7;
                r29 = r9;
                goto L_0x0880;
                r2.dispose();	 Catch:{ Exception -> 0x1361, all -> 0x134d }
                r3 = r7;	 Catch:{ Exception -> 0x1361, all -> 0x134d }
                r4 = 4;
                if (r3 != r4) goto L_0x0fc9;
                r3 = r9;	 Catch:{ Exception -> 0x0fb9, all -> 0x0fa9 }
                if (r3 == 0) goto L_0x0fc9;	 Catch:{ Exception -> 0x0fb9, all -> 0x0fa9 }
                r3 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x0fb9, all -> 0x0fa9 }
                r3 = r3.database;	 Catch:{ Exception -> 0x0fb9, all -> 0x0fa9 }
                r4 = java.util.Locale.US;	 Catch:{ Exception -> 0x0fb9, all -> 0x0fa9 }
                r8 = "SELECT max(mid) FROM messages WHERE uid = %d AND date <= %d AND mid > 0";	 Catch:{ Exception -> 0x0fb9, all -> 0x0fa9 }
                r10 = 2;	 Catch:{ Exception -> 0x0fb9, all -> 0x0fa9 }
                r11 = new java.lang.Object[r10];	 Catch:{ Exception -> 0x0fb9, all -> 0x0fa9 }
                r12 = r5;	 Catch:{ Exception -> 0x0fb9, all -> 0x0fa9 }
                r10 = java.lang.Long.valueOf(r12);	 Catch:{ Exception -> 0x0fb9, all -> 0x0fa9 }
                r12 = 0;	 Catch:{ Exception -> 0x0fb9, all -> 0x0fa9 }
                r11[r12] = r10;	 Catch:{ Exception -> 0x0fb9, all -> 0x0fa9 }
                r10 = r9;	 Catch:{ Exception -> 0x0fb9, all -> 0x0fa9 }
                r10 = java.lang.Integer.valueOf(r10);	 Catch:{ Exception -> 0x0fb9, all -> 0x0fa9 }
                r12 = 1;	 Catch:{ Exception -> 0x0fb9, all -> 0x0fa9 }
                r11[r12] = r10;	 Catch:{ Exception -> 0x0fb9, all -> 0x0fa9 }
                r4 = java.lang.String.format(r4, r8, r11);	 Catch:{ Exception -> 0x0fb9, all -> 0x0fa9 }
                r8 = 0;	 Catch:{ Exception -> 0x0fb9, all -> 0x0fa9 }
                r10 = new java.lang.Object[r8];	 Catch:{ Exception -> 0x0fb9, all -> 0x0fa9 }
                r3 = r3.queryFinalized(r4, r10);	 Catch:{ Exception -> 0x0fb9, all -> 0x0fa9 }
                r2 = r3;	 Catch:{ Exception -> 0x0fb9, all -> 0x0fa9 }
                r3 = r2.next();	 Catch:{ Exception -> 0x0fb9, all -> 0x0fa9 }
                r4 = -1;
                if (r3 == 0) goto L_0x0ebe;
                r3 = 0;
                r8 = r2.intValue(r3);	 Catch:{ Exception -> 0x0e6d, all -> 0x0e61 }
                r3 = r8;
                goto L_0x0ebf;
                r3 = r4;
                r2.dispose();	 Catch:{ Exception -> 0x0fb9, all -> 0x0fa9 }
                r8 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x0fb9, all -> 0x0fa9 }
                r8 = r8.database;	 Catch:{ Exception -> 0x0fb9, all -> 0x0fa9 }
                r10 = java.util.Locale.US;	 Catch:{ Exception -> 0x0fb9, all -> 0x0fa9 }
                r11 = "SELECT min(mid) FROM messages WHERE uid = %d AND date >= %d AND mid > 0";	 Catch:{ Exception -> 0x0fb9, all -> 0x0fa9 }
                r12 = 2;	 Catch:{ Exception -> 0x0fb9, all -> 0x0fa9 }
                r13 = new java.lang.Object[r12];	 Catch:{ Exception -> 0x0fb9, all -> 0x0fa9 }
                r14 = r5;	 Catch:{ Exception -> 0x0fb9, all -> 0x0fa9 }
                r12 = java.lang.Long.valueOf(r14);	 Catch:{ Exception -> 0x0fb9, all -> 0x0fa9 }
                r14 = 0;	 Catch:{ Exception -> 0x0fb9, all -> 0x0fa9 }
                r13[r14] = r12;	 Catch:{ Exception -> 0x0fb9, all -> 0x0fa9 }
                r12 = r9;	 Catch:{ Exception -> 0x0fb9, all -> 0x0fa9 }
                r12 = java.lang.Integer.valueOf(r12);	 Catch:{ Exception -> 0x0fb9, all -> 0x0fa9 }
                r14 = 1;	 Catch:{ Exception -> 0x0fb9, all -> 0x0fa9 }
                r13[r14] = r12;	 Catch:{ Exception -> 0x0fb9, all -> 0x0fa9 }
                r10 = java.lang.String.format(r10, r11, r13);	 Catch:{ Exception -> 0x0fb9, all -> 0x0fa9 }
                r11 = 0;	 Catch:{ Exception -> 0x0fb9, all -> 0x0fa9 }
                r12 = new java.lang.Object[r11];	 Catch:{ Exception -> 0x0fb9, all -> 0x0fa9 }
                r8 = r8.queryFinalized(r10, r12);	 Catch:{ Exception -> 0x0fb9, all -> 0x0fa9 }
                r2 = r8;	 Catch:{ Exception -> 0x0fb9, all -> 0x0fa9 }
                r8 = r2.next();	 Catch:{ Exception -> 0x0fb9, all -> 0x0fa9 }
                if (r8 == 0) goto L_0x0efa;
                r8 = 0;
                r10 = r2.intValue(r8);	 Catch:{ Exception -> 0x0e6d, all -> 0x0e61 }
                r8 = r10;
                goto L_0x0efb;
                r8 = r4;
                r2.dispose();	 Catch:{ Exception -> 0x0fb9, all -> 0x0fa9 }
                if (r3 == r4) goto L_0x0fc9;	 Catch:{ Exception -> 0x0fb9, all -> 0x0fa9 }
                if (r8 == r4) goto L_0x0fc9;	 Catch:{ Exception -> 0x0fb9, all -> 0x0fa9 }
                if (r3 != r8) goto L_0x0f0a;	 Catch:{ Exception -> 0x0fb9, all -> 0x0fa9 }
                r4 = r3;	 Catch:{ Exception -> 0x0fb9, all -> 0x0fa9 }
                r13 = r4;	 Catch:{ Exception -> 0x0fb9, all -> 0x0fa9 }
                r67 = r5;	 Catch:{ Exception -> 0x0fb9, all -> 0x0fa9 }
                goto L_0x0fcd;	 Catch:{ Exception -> 0x0fb9, all -> 0x0fa9 }
                r10 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x0fb9, all -> 0x0fa9 }
                r10 = r10.database;	 Catch:{ Exception -> 0x0fb9, all -> 0x0fa9 }
                r11 = java.util.Locale.US;	 Catch:{ Exception -> 0x0fb9, all -> 0x0fa9 }
                r12 = "SELECT start FROM messages_holes WHERE uid = %d AND start <= %d AND end > %d";	 Catch:{ Exception -> 0x0fb9, all -> 0x0fa9 }
                r13 = 3;	 Catch:{ Exception -> 0x0fb9, all -> 0x0fa9 }
                r14 = new java.lang.Object[r13];	 Catch:{ Exception -> 0x0fb9, all -> 0x0fa9 }
                r67 = r5;
                r4 = r5;	 Catch:{ Exception -> 0x0fa5, all -> 0x0fa1 }
                r4 = java.lang.Long.valueOf(r4);	 Catch:{ Exception -> 0x0fa5, all -> 0x0fa1 }
                r5 = 0;	 Catch:{ Exception -> 0x0fa5, all -> 0x0fa1 }
                r14[r5] = r4;	 Catch:{ Exception -> 0x0fa5, all -> 0x0fa1 }
                r4 = java.lang.Integer.valueOf(r3);	 Catch:{ Exception -> 0x0fa5, all -> 0x0fa1 }
                r5 = 1;	 Catch:{ Exception -> 0x0fa5, all -> 0x0fa1 }
                r14[r5] = r4;	 Catch:{ Exception -> 0x0fa5, all -> 0x0fa1 }
                r4 = java.lang.Integer.valueOf(r3);	 Catch:{ Exception -> 0x0fa5, all -> 0x0fa1 }
                r5 = 2;	 Catch:{ Exception -> 0x0fa5, all -> 0x0fa1 }
                r14[r5] = r4;	 Catch:{ Exception -> 0x0fa5, all -> 0x0fa1 }
                r4 = java.lang.String.format(r11, r12, r14);	 Catch:{ Exception -> 0x0fa5, all -> 0x0fa1 }
                r5 = 0;	 Catch:{ Exception -> 0x0fa5, all -> 0x0fa1 }
                r11 = new java.lang.Object[r5];	 Catch:{ Exception -> 0x0fa5, all -> 0x0fa1 }
                r4 = r10.queryFinalized(r4, r11);	 Catch:{ Exception -> 0x0fa5, all -> 0x0fa1 }
                r2 = r4;	 Catch:{ Exception -> 0x0fa5, all -> 0x0fa1 }
                r4 = r2.next();	 Catch:{ Exception -> 0x0fa5, all -> 0x0fa1 }
                if (r4 == 0) goto L_0x0f43;	 Catch:{ Exception -> 0x0fa5, all -> 0x0fa1 }
                r3 = -1;	 Catch:{ Exception -> 0x0fa5, all -> 0x0fa1 }
                r2.dispose();	 Catch:{ Exception -> 0x0fa5, all -> 0x0fa1 }
                r4 = -1;	 Catch:{ Exception -> 0x0fa5, all -> 0x0fa1 }
                if (r3 == r4) goto L_0x0fcb;	 Catch:{ Exception -> 0x0fa5, all -> 0x0fa1 }
                r4 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x0fa5, all -> 0x0fa1 }
                r4 = r4.database;	 Catch:{ Exception -> 0x0fa5, all -> 0x0fa1 }
                r5 = java.util.Locale.US;	 Catch:{ Exception -> 0x0fa5, all -> 0x0fa1 }
                r10 = "SELECT start FROM messages_holes WHERE uid = %d AND start <= %d AND end > %d";	 Catch:{ Exception -> 0x0fa5, all -> 0x0fa1 }
                r11 = 3;	 Catch:{ Exception -> 0x0fa5, all -> 0x0fa1 }
                r12 = new java.lang.Object[r11];	 Catch:{ Exception -> 0x0fa5, all -> 0x0fa1 }
                r13 = r5;	 Catch:{ Exception -> 0x0fa5, all -> 0x0fa1 }
                r11 = java.lang.Long.valueOf(r13);	 Catch:{ Exception -> 0x0fa5, all -> 0x0fa1 }
                r13 = 0;	 Catch:{ Exception -> 0x0fa5, all -> 0x0fa1 }
                r12[r13] = r11;	 Catch:{ Exception -> 0x0fa5, all -> 0x0fa1 }
                r11 = java.lang.Integer.valueOf(r8);	 Catch:{ Exception -> 0x0fa5, all -> 0x0fa1 }
                r13 = 1;	 Catch:{ Exception -> 0x0fa5, all -> 0x0fa1 }
                r12[r13] = r11;	 Catch:{ Exception -> 0x0fa5, all -> 0x0fa1 }
                r11 = java.lang.Integer.valueOf(r8);	 Catch:{ Exception -> 0x0fa5, all -> 0x0fa1 }
                r13 = 2;	 Catch:{ Exception -> 0x0fa5, all -> 0x0fa1 }
                r12[r13] = r11;	 Catch:{ Exception -> 0x0fa5, all -> 0x0fa1 }
                r5 = java.lang.String.format(r5, r10, r12);	 Catch:{ Exception -> 0x0fa5, all -> 0x0fa1 }
                r10 = 0;	 Catch:{ Exception -> 0x0fa5, all -> 0x0fa1 }
                r11 = new java.lang.Object[r10];	 Catch:{ Exception -> 0x0fa5, all -> 0x0fa1 }
                r4 = r4.queryFinalized(r5, r11);	 Catch:{ Exception -> 0x0fa5, all -> 0x0fa1 }
                r2 = r4;	 Catch:{ Exception -> 0x0fa5, all -> 0x0fa1 }
                r4 = r2.next();	 Catch:{ Exception -> 0x0fa5, all -> 0x0fa1 }
                if (r4 == 0) goto L_0x0f80;	 Catch:{ Exception -> 0x0fa5, all -> 0x0fa1 }
                r8 = -1;	 Catch:{ Exception -> 0x0fa5, all -> 0x0fa1 }
                r2.dispose();	 Catch:{ Exception -> 0x0fa5, all -> 0x0fa1 }
                r4 = -1;
                if (r8 == r4) goto L_0x0fcb;
                r15 = r8;
                r4 = r8;
                r10 = (long) r8;
                r12 = 0;
                r5 = (r10 > r12 ? 1 : (r10 == r12 ? 0 : -1));
                if (r5 == 0) goto L_0x0f9c;
                if (r7 == 0) goto L_0x0f9c;
                r12 = (long) r7;
                r12 = r12 << r16;
                r17 = r10 | r12;
                r13 = r4;
                r33 = r15;
                r11 = r17;
                goto L_0x0fcf;
                r13 = r4;
                r11 = r10;
                r33 = r15;
                goto L_0x0fcf;
            L_0x0fa1:
                r0 = move-exception;
                r2 = r0;
                goto L_0x0e65;
            L_0x0fa5:
                r0 = move-exception;
                r2 = r0;
                goto L_0x0e71;
            L_0x0fa9:
                r0 = move-exception;
                r67 = r5;
                r2 = r0;
                r48 = r6;
                r26 = r7;
                r29 = r9;
                r22 = r38;
                r27 = r52;
                goto L_0x242b;
            L_0x0fb9:
                r0 = move-exception;
                r67 = r5;
                r2 = r0;
                r48 = r6;
                r26 = r7;
                r29 = r9;
                r95 = r38;
                r27 = r52;
                goto L_0x23dc;
                r67 = r5;
                r13 = r53;
                r11 = r65;
                if (r13 == 0) goto L_0x0fd3;
                r3 = 1;
                goto L_0x0fd4;
                r3 = 0;
                if (r3 == 0) goto L_0x102b;
                r4 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x1023, all -> 0x101b }
                r4 = r4.database;	 Catch:{ Exception -> 0x1023, all -> 0x101b }
                r5 = java.util.Locale.US;	 Catch:{ Exception -> 0x1023, all -> 0x101b }
                r8 = "SELECT start FROM messages_holes WHERE uid = %d AND start < %d AND end > %d";	 Catch:{ Exception -> 0x1023, all -> 0x101b }
                r10 = 3;	 Catch:{ Exception -> 0x1023, all -> 0x101b }
                r14 = new java.lang.Object[r10];	 Catch:{ Exception -> 0x1023, all -> 0x101b }
                r68 = r2;	 Catch:{ Exception -> 0x1023, all -> 0x101b }
                r69 = r3;	 Catch:{ Exception -> 0x1023, all -> 0x101b }
                r2 = r5;	 Catch:{ Exception -> 0x1023, all -> 0x101b }
                r2 = java.lang.Long.valueOf(r2);	 Catch:{ Exception -> 0x1023, all -> 0x101b }
                r3 = 0;	 Catch:{ Exception -> 0x1023, all -> 0x101b }
                r14[r3] = r2;	 Catch:{ Exception -> 0x1023, all -> 0x101b }
                r2 = java.lang.Integer.valueOf(r13);	 Catch:{ Exception -> 0x1023, all -> 0x101b }
                r3 = 1;	 Catch:{ Exception -> 0x1023, all -> 0x101b }
                r14[r3] = r2;	 Catch:{ Exception -> 0x1023, all -> 0x101b }
                r2 = java.lang.Integer.valueOf(r13);	 Catch:{ Exception -> 0x1023, all -> 0x101b }
                r3 = 2;	 Catch:{ Exception -> 0x1023, all -> 0x101b }
                r14[r3] = r2;	 Catch:{ Exception -> 0x1023, all -> 0x101b }
                r2 = java.lang.String.format(r5, r8, r14);	 Catch:{ Exception -> 0x1023, all -> 0x101b }
                r3 = 0;	 Catch:{ Exception -> 0x1023, all -> 0x101b }
                r5 = new java.lang.Object[r3];	 Catch:{ Exception -> 0x1023, all -> 0x101b }
                r2 = r4.queryFinalized(r2, r5);	 Catch:{ Exception -> 0x1023, all -> 0x101b }
                r3 = r2.next();	 Catch:{ Exception -> 0x1023, all -> 0x101b }
                if (r3 == 0) goto L_0x1011;	 Catch:{ Exception -> 0x1023, all -> 0x101b }
                r3 = 0;	 Catch:{ Exception -> 0x1023, all -> 0x101b }
                goto L_0x1013;	 Catch:{ Exception -> 0x1023, all -> 0x101b }
                r3 = r69;	 Catch:{ Exception -> 0x1023, all -> 0x101b }
                r2.dispose();	 Catch:{ Exception -> 0x1023, all -> 0x101b }
                r68 = r2;
                r69 = r3;
                goto L_0x102f;
            L_0x101b:
                r0 = move-exception;
                r2 = r0;
                r48 = r6;
                r26 = r7;
                goto L_0x0864;
            L_0x1023:
                r0 = move-exception;
                r2 = r0;
                r48 = r6;
                r26 = r7;
                goto L_0x087a;
                r68 = r2;
                r69 = r3;
                if (r69 == 0) goto L_0x1240;
                r2 = 0;
                r4 = 1;
                r8 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x122a, all -> 0x1214 }
                r8 = r8.database;	 Catch:{ Exception -> 0x122a, all -> 0x1214 }
                r10 = java.util.Locale.US;	 Catch:{ Exception -> 0x122a, all -> 0x1214 }
                r14 = "SELECT start FROM messages_holes WHERE uid = %d AND start >= %d ORDER BY start ASC LIMIT 1";	 Catch:{ Exception -> 0x122a, all -> 0x1214 }
                r70 = r2;	 Catch:{ Exception -> 0x122a, all -> 0x1214 }
                r15 = 2;	 Catch:{ Exception -> 0x122a, all -> 0x1214 }
                r2 = new java.lang.Object[r15];	 Catch:{ Exception -> 0x122a, all -> 0x1214 }
                r72 = r4;	 Catch:{ Exception -> 0x122a, all -> 0x1214 }
                r3 = r5;	 Catch:{ Exception -> 0x122a, all -> 0x1214 }
                r3 = java.lang.Long.valueOf(r3);	 Catch:{ Exception -> 0x122a, all -> 0x1214 }
                r4 = 0;	 Catch:{ Exception -> 0x122a, all -> 0x1214 }
                r2[r4] = r3;	 Catch:{ Exception -> 0x122a, all -> 0x1214 }
                r3 = java.lang.Integer.valueOf(r13);	 Catch:{ Exception -> 0x122a, all -> 0x1214 }
                r4 = 1;	 Catch:{ Exception -> 0x122a, all -> 0x1214 }
                r2[r4] = r3;	 Catch:{ Exception -> 0x122a, all -> 0x1214 }
                r2 = java.lang.String.format(r10, r14, r2);	 Catch:{ Exception -> 0x122a, all -> 0x1214 }
                r3 = 0;	 Catch:{ Exception -> 0x122a, all -> 0x1214 }
                r4 = new java.lang.Object[r3];	 Catch:{ Exception -> 0x122a, all -> 0x1214 }
                r2 = r8.queryFinalized(r2, r4);	 Catch:{ Exception -> 0x122a, all -> 0x1214 }
                r3 = r2.next();	 Catch:{ Exception -> 0x122a, all -> 0x1214 }
                if (r3 == 0) goto L_0x1079;
                r3 = 0;
                r4 = r2.intValue(r3);	 Catch:{ Exception -> 0x1023, all -> 0x101b }
                r3 = (long) r4;
                if (r7 == 0) goto L_0x1077;
                r14 = (long) r7;
                r14 = r14 << r16;
                r17 = r3 | r14;
                r70 = r17;
                goto L_0x1079;
                r70 = r3;
                r2.dispose();	 Catch:{ Exception -> 0x122a, all -> 0x1214 }
                r3 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x122a, all -> 0x1214 }
                r3 = r3.database;	 Catch:{ Exception -> 0x122a, all -> 0x1214 }
                r4 = java.util.Locale.US;	 Catch:{ Exception -> 0x122a, all -> 0x1214 }
                r5 = "SELECT end FROM messages_holes WHERE uid = %d AND end <= %d ORDER BY end DESC LIMIT 1";	 Catch:{ Exception -> 0x122a, all -> 0x1214 }
                r8 = 2;	 Catch:{ Exception -> 0x122a, all -> 0x1214 }
                r10 = new java.lang.Object[r8];	 Catch:{ Exception -> 0x122a, all -> 0x1214 }
                r14 = r5;	 Catch:{ Exception -> 0x122a, all -> 0x1214 }
                r8 = java.lang.Long.valueOf(r14);	 Catch:{ Exception -> 0x122a, all -> 0x1214 }
                r14 = 0;	 Catch:{ Exception -> 0x122a, all -> 0x1214 }
                r10[r14] = r8;	 Catch:{ Exception -> 0x122a, all -> 0x1214 }
                r8 = java.lang.Integer.valueOf(r13);	 Catch:{ Exception -> 0x122a, all -> 0x1214 }
                r14 = 1;	 Catch:{ Exception -> 0x122a, all -> 0x1214 }
                r10[r14] = r8;	 Catch:{ Exception -> 0x122a, all -> 0x1214 }
                r4 = java.lang.String.format(r4, r5, r10);	 Catch:{ Exception -> 0x122a, all -> 0x1214 }
                r5 = 0;	 Catch:{ Exception -> 0x122a, all -> 0x1214 }
                r8 = new java.lang.Object[r5];	 Catch:{ Exception -> 0x122a, all -> 0x1214 }
                r3 = r3.queryFinalized(r4, r8);	 Catch:{ Exception -> 0x122a, all -> 0x1214 }
                r2 = r3;	 Catch:{ Exception -> 0x122a, all -> 0x1214 }
                r3 = r2.next();	 Catch:{ Exception -> 0x122a, all -> 0x1214 }
                if (r3 == 0) goto L_0x10bb;
                r3 = 0;
                r4 = r2.intValue(r3);	 Catch:{ Exception -> 0x1023, all -> 0x101b }
                r4 = (long) r4;
                if (r7 == 0) goto L_0x10bd;
                r14 = (long) r7;
                r14 = r14 << r16;
                r17 = r4 | r14;
                r4 = r17;
                goto L_0x10bd;
                r4 = r72;
                r2.dispose();	 Catch:{ Exception -> 0x122a, all -> 0x1214 }
                r14 = 0;
                r3 = (r70 > r14 ? 1 : (r70 == r14 ? 0 : -1));
                if (r3 != 0) goto L_0x116f;
                r14 = 1;
                r3 = (r4 > r14 ? 1 : (r4 == r14 ? 0 : -1));
                if (r3 == 0) goto L_0x10d0;
                r74 = r13;
                goto L_0x1171;
                r3 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x115b, all -> 0x1147 }
                r3 = r3.database;	 Catch:{ Exception -> 0x115b, all -> 0x1147 }
                r8 = java.util.Locale.US;	 Catch:{ Exception -> 0x115b, all -> 0x1147 }
                r10 = "SELECT * FROM (SELECT m.read_state, m.data, m.send_state, m.mid, m.date, r.random_id, m.replydata, m.media, m.ttl, m.mention FROM messages as m LEFT JOIN randoms as r ON r.mid = m.mid WHERE m.uid = %d AND m.mid <= %d ORDER BY m.date DESC, m.mid DESC LIMIT %d) UNION SELECT * FROM (SELECT m.read_state, m.data, m.send_state, m.mid, m.date, r.random_id, m.replydata, m.media, m.ttl, m.mention FROM messages as m LEFT JOIN randoms as r ON r.mid = m.mid WHERE m.uid = %d AND m.mid > %d ORDER BY m.date ASC, m.mid ASC LIMIT %d)";	 Catch:{ Exception -> 0x115b, all -> 0x1147 }
                r14 = 6;	 Catch:{ Exception -> 0x115b, all -> 0x1147 }
                r15 = new java.lang.Object[r14];	 Catch:{ Exception -> 0x115b, all -> 0x1147 }
                r74 = r13;
                r13 = r5;	 Catch:{ Exception -> 0x1135, all -> 0x1123 }
                r13 = java.lang.Long.valueOf(r13);	 Catch:{ Exception -> 0x1135, all -> 0x1123 }
                r14 = 0;	 Catch:{ Exception -> 0x1135, all -> 0x1123 }
                r15[r14] = r13;	 Catch:{ Exception -> 0x1135, all -> 0x1123 }
                r13 = java.lang.Long.valueOf(r11);	 Catch:{ Exception -> 0x1135, all -> 0x1123 }
                r14 = 1;	 Catch:{ Exception -> 0x1135, all -> 0x1123 }
                r15[r14] = r13;	 Catch:{ Exception -> 0x1135, all -> 0x1123 }
                r13 = r67 / 2;	 Catch:{ Exception -> 0x1135, all -> 0x1123 }
                r13 = java.lang.Integer.valueOf(r13);	 Catch:{ Exception -> 0x1135, all -> 0x1123 }
                r14 = 2;	 Catch:{ Exception -> 0x1135, all -> 0x1123 }
                r15[r14] = r13;	 Catch:{ Exception -> 0x1135, all -> 0x1123 }
                r13 = r5;	 Catch:{ Exception -> 0x1135, all -> 0x1123 }
                r13 = java.lang.Long.valueOf(r13);	 Catch:{ Exception -> 0x1135, all -> 0x1123 }
                r14 = 3;	 Catch:{ Exception -> 0x1135, all -> 0x1123 }
                r15[r14] = r13;	 Catch:{ Exception -> 0x1135, all -> 0x1123 }
                r13 = java.lang.Long.valueOf(r11);	 Catch:{ Exception -> 0x1135, all -> 0x1123 }
                r14 = 4;	 Catch:{ Exception -> 0x1135, all -> 0x1123 }
                r15[r14] = r13;	 Catch:{ Exception -> 0x1135, all -> 0x1123 }
                r13 = r67 / 2;	 Catch:{ Exception -> 0x1135, all -> 0x1123 }
                r13 = java.lang.Integer.valueOf(r13);	 Catch:{ Exception -> 0x1135, all -> 0x1123 }
                r14 = 5;	 Catch:{ Exception -> 0x1135, all -> 0x1123 }
                r15[r14] = r13;	 Catch:{ Exception -> 0x1135, all -> 0x1123 }
                r8 = java.lang.String.format(r8, r10, r15);	 Catch:{ Exception -> 0x1135, all -> 0x1123 }
                r10 = 0;	 Catch:{ Exception -> 0x1135, all -> 0x1123 }
                r13 = new java.lang.Object[r10];	 Catch:{ Exception -> 0x1135, all -> 0x1123 }
                r3 = r3.queryFinalized(r8, r13);	 Catch:{ Exception -> 0x1135, all -> 0x1123 }
                r2 = r3;
                r76 = r6;
                r77 = r7;
                goto L_0x11e5;
            L_0x1123:
                r0 = move-exception;
                r2 = r0;
                r48 = r6;
                r26 = r7;
                r29 = r9;
                r65 = r11;
                r22 = r38;
                r27 = r52;
                r53 = r74;
                goto L_0x242b;
            L_0x1135:
                r0 = move-exception;
                r2 = r0;
                r48 = r6;
                r26 = r7;
                r29 = r9;
                r65 = r11;
                r95 = r38;
                r27 = r52;
                r53 = r74;
                goto L_0x23dc;
            L_0x1147:
                r0 = move-exception;
                r74 = r13;
                r2 = r0;
                r48 = r6;
                r26 = r7;
                r29 = r9;
                r65 = r11;
                r22 = r38;
                r27 = r52;
                r53 = r74;
                goto L_0x242b;
            L_0x115b:
                r0 = move-exception;
                r74 = r13;
                r2 = r0;
                r48 = r6;
                r26 = r7;
                r29 = r9;
                r65 = r11;
                r95 = r38;
                r27 = r52;
                r53 = r74;
                goto L_0x23dc;
                r74 = r13;
                r13 = 0;
                r3 = (r70 > r13 ? 1 : (r70 == r13 ? 0 : -1));
                if (r3 != 0) goto L_0x1184;
                r70 = 1000000000; // 0x3b9aca00 float:0.0047237873 double:4.94065646E-315;
                if (r7 == 0) goto L_0x1184;
                r13 = (long) r7;
                r13 = r13 << r16;
                r17 = r70 | r13;
                r13 = r17;
                goto L_0x1186;
                r13 = r70;
                r3 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x1200, all -> 0x11ec }
                r3 = r3.database;	 Catch:{ Exception -> 0x1200, all -> 0x11ec }
                r8 = java.util.Locale.US;	 Catch:{ Exception -> 0x1200, all -> 0x11ec }
                r10 = "SELECT * FROM (SELECT m.read_state, m.data, m.send_state, m.mid, m.date, r.random_id, m.replydata, m.media, m.ttl, m.mention FROM messages as m LEFT JOIN randoms as r ON r.mid = m.mid WHERE m.uid = %d AND m.mid <= %d AND m.mid >= %d ORDER BY m.date DESC, m.mid DESC LIMIT %d) UNION SELECT * FROM (SELECT m.read_state, m.data, m.send_state, m.mid, m.date, r.random_id, m.replydata, m.media, m.ttl, m.mention FROM messages as m LEFT JOIN randoms as r ON r.mid = m.mid WHERE m.uid = %d AND m.mid > %d AND m.mid <= %d ORDER BY m.date ASC, m.mid ASC LIMIT %d)";	 Catch:{ Exception -> 0x1200, all -> 0x11ec }
                r75 = r2;	 Catch:{ Exception -> 0x1200, all -> 0x11ec }
                r15 = 8;	 Catch:{ Exception -> 0x1200, all -> 0x11ec }
                r2 = new java.lang.Object[r15];	 Catch:{ Exception -> 0x1200, all -> 0x11ec }
                r76 = r6;
                r77 = r7;
                r6 = r5;	 Catch:{ Exception -> 0x1289, all -> 0x1279 }
                r6 = java.lang.Long.valueOf(r6);	 Catch:{ Exception -> 0x1289, all -> 0x1279 }
                r7 = 0;	 Catch:{ Exception -> 0x1289, all -> 0x1279 }
                r2[r7] = r6;	 Catch:{ Exception -> 0x1289, all -> 0x1279 }
                r6 = java.lang.Long.valueOf(r11);	 Catch:{ Exception -> 0x1289, all -> 0x1279 }
                r7 = 1;	 Catch:{ Exception -> 0x1289, all -> 0x1279 }
                r2[r7] = r6;	 Catch:{ Exception -> 0x1289, all -> 0x1279 }
                r6 = java.lang.Long.valueOf(r4);	 Catch:{ Exception -> 0x1289, all -> 0x1279 }
                r7 = 2;	 Catch:{ Exception -> 0x1289, all -> 0x1279 }
                r2[r7] = r6;	 Catch:{ Exception -> 0x1289, all -> 0x1279 }
                r6 = r67 / 2;	 Catch:{ Exception -> 0x1289, all -> 0x1279 }
                r6 = java.lang.Integer.valueOf(r6);	 Catch:{ Exception -> 0x1289, all -> 0x1279 }
                r7 = 3;	 Catch:{ Exception -> 0x1289, all -> 0x1279 }
                r2[r7] = r6;	 Catch:{ Exception -> 0x1289, all -> 0x1279 }
                r6 = r5;	 Catch:{ Exception -> 0x1289, all -> 0x1279 }
                r6 = java.lang.Long.valueOf(r6);	 Catch:{ Exception -> 0x1289, all -> 0x1279 }
                r7 = 4;	 Catch:{ Exception -> 0x1289, all -> 0x1279 }
                r2[r7] = r6;	 Catch:{ Exception -> 0x1289, all -> 0x1279 }
                r6 = java.lang.Long.valueOf(r11);	 Catch:{ Exception -> 0x1289, all -> 0x1279 }
                r7 = 5;	 Catch:{ Exception -> 0x1289, all -> 0x1279 }
                r2[r7] = r6;	 Catch:{ Exception -> 0x1289, all -> 0x1279 }
                r6 = java.lang.Long.valueOf(r13);	 Catch:{ Exception -> 0x1289, all -> 0x1279 }
                r7 = 6;	 Catch:{ Exception -> 0x1289, all -> 0x1279 }
                r2[r7] = r6;	 Catch:{ Exception -> 0x1289, all -> 0x1279 }
                r6 = r67 / 2;	 Catch:{ Exception -> 0x1289, all -> 0x1279 }
                r6 = java.lang.Integer.valueOf(r6);	 Catch:{ Exception -> 0x1289, all -> 0x1279 }
                r7 = 7;	 Catch:{ Exception -> 0x1289, all -> 0x1279 }
                r2[r7] = r6;	 Catch:{ Exception -> 0x1289, all -> 0x1279 }
                r2 = java.lang.String.format(r8, r10, r2);	 Catch:{ Exception -> 0x1289, all -> 0x1279 }
                r6 = 0;	 Catch:{ Exception -> 0x1289, all -> 0x1279 }
                r7 = new java.lang.Object[r6];	 Catch:{ Exception -> 0x1289, all -> 0x1279 }
                r2 = r3.queryFinalized(r2, r7);	 Catch:{ Exception -> 0x1289, all -> 0x1279 }
                r14 = r32;
                r4 = r59;
                goto L_0x1313;
            L_0x11ec:
                r0 = move-exception;
                r76 = r6;
                r2 = r0;
                r26 = r7;
                r29 = r9;
                r65 = r11;
                r22 = r38;
                r27 = r52;
                r53 = r74;
                r48 = r76;
                goto L_0x242b;
            L_0x1200:
                r0 = move-exception;
                r76 = r6;
                r2 = r0;
                r26 = r7;
                r29 = r9;
                r65 = r11;
                r95 = r38;
                r27 = r52;
                r53 = r74;
                r48 = r76;
                goto L_0x23dc;
            L_0x1214:
                r0 = move-exception;
                r76 = r6;
                r74 = r13;
                r2 = r0;
                r26 = r7;
                r29 = r9;
                r65 = r11;
                r22 = r38;
                r27 = r52;
                r53 = r74;
                r48 = r76;
                goto L_0x242b;
            L_0x122a:
                r0 = move-exception;
                r76 = r6;
                r74 = r13;
                r2 = r0;
                r26 = r7;
                r29 = r9;
                r65 = r11;
                r95 = r38;
                r27 = r52;
                r53 = r74;
                r48 = r76;
                goto L_0x23dc;
                r76 = r6;
                r77 = r7;
                r74 = r13;
                r2 = r7;	 Catch:{ Exception -> 0x1339, all -> 0x1325 }
                r3 = 2;	 Catch:{ Exception -> 0x1339, all -> 0x1325 }
                if (r2 != r3) goto L_0x130e;	 Catch:{ Exception -> 0x1339, all -> 0x1325 }
                r2 = 0;	 Catch:{ Exception -> 0x1339, all -> 0x1325 }
                r3 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x1339, all -> 0x1325 }
                r3 = r3.database;	 Catch:{ Exception -> 0x1339, all -> 0x1325 }
                r4 = java.util.Locale.US;	 Catch:{ Exception -> 0x1339, all -> 0x1325 }
                r5 = "SELECT COUNT(*) FROM messages WHERE uid = %d AND mid != 0 AND out = 0 AND read_state IN(0,2)";	 Catch:{ Exception -> 0x1339, all -> 0x1325 }
                r6 = 1;	 Catch:{ Exception -> 0x1339, all -> 0x1325 }
                r7 = new java.lang.Object[r6];	 Catch:{ Exception -> 0x1339, all -> 0x1325 }
                r13 = r5;	 Catch:{ Exception -> 0x1339, all -> 0x1325 }
                r6 = java.lang.Long.valueOf(r13);	 Catch:{ Exception -> 0x1339, all -> 0x1325 }
                r8 = 0;	 Catch:{ Exception -> 0x1339, all -> 0x1325 }
                r7[r8] = r6;	 Catch:{ Exception -> 0x1339, all -> 0x1325 }
                r4 = java.lang.String.format(r4, r5, r7);	 Catch:{ Exception -> 0x1339, all -> 0x1325 }
                r5 = new java.lang.Object[r8];	 Catch:{ Exception -> 0x1339, all -> 0x1325 }
                r3 = r3.queryFinalized(r4, r5);	 Catch:{ Exception -> 0x1339, all -> 0x1325 }
                r4 = r3.next();	 Catch:{ Exception -> 0x1339, all -> 0x1325 }
                if (r4 == 0) goto L_0x1299;
                r4 = 0;
                r5 = r3.intValue(r4);	 Catch:{ Exception -> 0x1289, all -> 0x1279 }
                r2 = r5;
                goto L_0x1299;
            L_0x1279:
                r0 = move-exception;
                r2 = r0;
                r29 = r9;
                r65 = r11;
                r22 = r38;
                r27 = r52;
                r53 = r74;
                r48 = r76;
                goto L_0x1bd7;
            L_0x1289:
                r0 = move-exception;
                r2 = r0;
                r29 = r9;
                r65 = r11;
                r95 = r38;
                r27 = r52;
                r53 = r74;
                r48 = r76;
                goto L_0x1be5;
                r3.dispose();	 Catch:{ Exception -> 0x1339, all -> 0x1325 }
                r4 = r59;
                if (r2 != r4) goto L_0x1306;
                r14 = 1;
                r5 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x12fb, all -> 0x12f0 }
                r5 = r5.database;	 Catch:{ Exception -> 0x12fb, all -> 0x12f0 }
                r6 = java.util.Locale.US;	 Catch:{ Exception -> 0x12fb, all -> 0x12f0 }
                r7 = "SELECT * FROM (SELECT m.read_state, m.data, m.send_state, m.mid, m.date, r.random_id, m.replydata, m.media, m.ttl, m.mention FROM messages as m LEFT JOIN randoms as r ON r.mid = m.mid WHERE m.uid = %d AND m.mid <= %d ORDER BY m.date DESC, m.mid DESC LIMIT %d) UNION SELECT * FROM (SELECT m.read_state, m.data, m.send_state, m.mid, m.date, r.random_id, m.replydata, m.media, m.ttl, m.mention FROM messages as m LEFT JOIN randoms as r ON r.mid = m.mid WHERE m.uid = %d AND m.mid > %d ORDER BY m.date ASC, m.mid ASC LIMIT %d)";	 Catch:{ Exception -> 0x12fb, all -> 0x12f0 }
                r8 = 6;	 Catch:{ Exception -> 0x12fb, all -> 0x12f0 }
                r10 = new java.lang.Object[r8];	 Catch:{ Exception -> 0x12fb, all -> 0x12f0 }
                r79 = r2;	 Catch:{ Exception -> 0x12fb, all -> 0x12f0 }
                r78 = r3;	 Catch:{ Exception -> 0x12fb, all -> 0x12f0 }
                r2 = r5;	 Catch:{ Exception -> 0x12fb, all -> 0x12f0 }
                r2 = java.lang.Long.valueOf(r2);	 Catch:{ Exception -> 0x12fb, all -> 0x12f0 }
                r3 = 0;	 Catch:{ Exception -> 0x12fb, all -> 0x12f0 }
                r10[r3] = r2;	 Catch:{ Exception -> 0x12fb, all -> 0x12f0 }
                r2 = java.lang.Long.valueOf(r11);	 Catch:{ Exception -> 0x12fb, all -> 0x12f0 }
                r3 = 1;	 Catch:{ Exception -> 0x12fb, all -> 0x12f0 }
                r10[r3] = r2;	 Catch:{ Exception -> 0x12fb, all -> 0x12f0 }
                r2 = r67 / 2;	 Catch:{ Exception -> 0x12fb, all -> 0x12f0 }
                r2 = java.lang.Integer.valueOf(r2);	 Catch:{ Exception -> 0x12fb, all -> 0x12f0 }
                r3 = 2;	 Catch:{ Exception -> 0x12fb, all -> 0x12f0 }
                r10[r3] = r2;	 Catch:{ Exception -> 0x12fb, all -> 0x12f0 }
                r2 = r5;	 Catch:{ Exception -> 0x12fb, all -> 0x12f0 }
                r2 = java.lang.Long.valueOf(r2);	 Catch:{ Exception -> 0x12fb, all -> 0x12f0 }
                r3 = 3;	 Catch:{ Exception -> 0x12fb, all -> 0x12f0 }
                r10[r3] = r2;	 Catch:{ Exception -> 0x12fb, all -> 0x12f0 }
                r2 = java.lang.Long.valueOf(r11);	 Catch:{ Exception -> 0x12fb, all -> 0x12f0 }
                r3 = 4;	 Catch:{ Exception -> 0x12fb, all -> 0x12f0 }
                r10[r3] = r2;	 Catch:{ Exception -> 0x12fb, all -> 0x12f0 }
                r2 = r67 / 2;	 Catch:{ Exception -> 0x12fb, all -> 0x12f0 }
                r2 = java.lang.Integer.valueOf(r2);	 Catch:{ Exception -> 0x12fb, all -> 0x12f0 }
                r3 = 5;	 Catch:{ Exception -> 0x12fb, all -> 0x12f0 }
                r10[r3] = r2;	 Catch:{ Exception -> 0x12fb, all -> 0x12f0 }
                r2 = java.lang.String.format(r6, r7, r10);	 Catch:{ Exception -> 0x12fb, all -> 0x12f0 }
                r3 = 0;	 Catch:{ Exception -> 0x12fb, all -> 0x12f0 }
                r6 = new java.lang.Object[r3];	 Catch:{ Exception -> 0x12fb, all -> 0x12f0 }
                r2 = r5.queryFinalized(r2, r6);	 Catch:{ Exception -> 0x12fb, all -> 0x12f0 }
                goto L_0x130d;
            L_0x12f0:
                r0 = move-exception;
                r2 = r0;
                r59 = r4;
                r29 = r9;
                r65 = r11;
                r32 = r14;
                goto L_0x127f;
            L_0x12fb:
                r0 = move-exception;
                r2 = r0;
                r59 = r4;
                r29 = r9;
                r65 = r11;
                r32 = r14;
                goto L_0x128f;
                r79 = r2;
                r78 = r3;
                r2 = 0;
                r14 = r32;
                goto L_0x1313;
                r4 = r59;
                r2 = 0;
                r14 = r32;
                r59 = r4;
                r29 = r9;
                r65 = r11;
                r32 = r14;
                r3 = r47;
                r9 = r52;
                r15 = r74;
                r48 = r76;
                goto L_0x1b77;
            L_0x1325:
                r0 = move-exception;
                r4 = r59;
                r2 = r0;
                r29 = r9;
                r65 = r11;
                r22 = r38;
                r27 = r52;
                r53 = r74;
                r48 = r76;
                r26 = r77;
                goto L_0x242b;
            L_0x1339:
                r0 = move-exception;
                r4 = r59;
                r2 = r0;
                r29 = r9;
                r65 = r11;
                r95 = r38;
                r27 = r52;
                r53 = r74;
                r48 = r76;
                r26 = r77;
                goto L_0x23dc;
            L_0x134d:
                r0 = move-exception;
                r67 = r5;
                r76 = r6;
                r4 = r59;
                r2 = r0;
                r26 = r7;
                r29 = r9;
                r22 = r38;
                r27 = r52;
                r48 = r76;
                goto L_0x242b;
            L_0x1361:
                r0 = move-exception;
                r67 = r5;
                r76 = r6;
                r4 = r59;
                r2 = r0;
                r26 = r7;
                r29 = r9;
                r95 = r38;
                r27 = r52;
                r48 = r76;
                goto L_0x23dc;
            L_0x1375:
                r0 = move-exception;
                r77 = r4;
                r67 = r5;
                r76 = r6;
                r65 = r11;
                r4 = r3;
                r2 = r0;
                r59 = r4;
                r29 = r9;
                r22 = r38;
                r27 = r52;
                r48 = r76;
                r26 = r77;
                goto L_0x242b;
            L_0x138e:
                r0 = move-exception;
                r77 = r4;
                r67 = r5;
                r76 = r6;
                r65 = r11;
                r4 = r3;
                r2 = r0;
                r59 = r4;
                r29 = r9;
                r95 = r38;
                r27 = r52;
                r48 = r76;
                r26 = r77;
                goto L_0x23dc;
            L_0x13a7:
                r0 = move-exception;
                r77 = r4;
                r67 = r5;
                r76 = r6;
                r65 = r11;
                r53 = r13;
                r4 = r3;
                r2 = r0;
                r59 = r4;
                r29 = r9;
                r22 = r38;
                r27 = r52;
                r48 = r76;
                r26 = r77;
                goto L_0x242b;
            L_0x13c2:
                r0 = move-exception;
                r77 = r4;
                r67 = r5;
                r76 = r6;
                r65 = r11;
                r53 = r13;
                r4 = r3;
                r2 = r0;
                r59 = r4;
                r29 = r9;
                r95 = r38;
                r27 = r52;
                r48 = r76;
                r26 = r77;
                goto L_0x23dc;
            L_0x13dd:
                r0 = move-exception;
                r77 = r4;
                r67 = r5;
                r76 = r6;
                r65 = r11;
                r53 = r13;
                r4 = r3;
                r2 = r0;
                r59 = r4;
                r27 = r8;
                r29 = r9;
                r22 = r38;
                r48 = r76;
                r26 = r77;
                goto L_0x242b;
            L_0x13f8:
                r0 = move-exception;
                r77 = r4;
                r67 = r5;
                r76 = r6;
                r65 = r11;
                r53 = r13;
                r4 = r3;
                r2 = r0;
                r59 = r4;
                r27 = r8;
                r29 = r9;
                r95 = r38;
                r48 = r76;
                r26 = r77;
                goto L_0x23dc;
            L_0x1413:
                r0 = move-exception;
                r77 = r4;
                r67 = r5;
                r76 = r6;
                r50 = r7;
                r51 = r10;
                r65 = r11;
                r53 = r13;
                r4 = r3;
                r2 = r0;
                r59 = r4;
                r27 = r8;
                r29 = r9;
                r22 = r38;
                r48 = r76;
                r26 = r77;
                goto L_0x242b;
            L_0x1432:
                r0 = move-exception;
                r77 = r4;
                r67 = r5;
                r76 = r6;
                r50 = r7;
                r51 = r10;
                r65 = r11;
                r53 = r13;
                r4 = r3;
                r2 = r0;
                r59 = r4;
                r27 = r8;
                r29 = r9;
                r95 = r38;
                r48 = r76;
                r26 = r77;
                goto L_0x23dc;
            L_0x1451:
                r0 = move-exception;
                r48 = r6;
                r26 = r4;
                r67 = r5;
                r27 = r8;
                r65 = r11;
                r59 = r22;
                r47 = r23;
                r50 = r24;
                r51 = r30;
                r53 = r31;
                r22 = r2;
                r2 = r0;
                goto L_0x242b;
            L_0x146b:
                r0 = move-exception;
                r48 = r6;
                r95 = r2;
                r26 = r4;
                r67 = r5;
                r27 = r8;
                r65 = r11;
                r59 = r22;
                r47 = r23;
                r50 = r24;
                r51 = r30;
                r53 = r31;
                r2 = r0;
                goto L_0x23dc;
            L_0x1485:
                r38 = r2;
                r39 = r3;
                r77 = r4;
                r48 = r6;
                r49 = r7;
                r52 = r8;
                r36 = r9;
                r35 = r10;
                r34 = r13;
                r46 = r14;
                r2 = 1;
                r3 = r7;	 Catch:{ Exception -> 0x2221, all -> 0x2203 }
                r4 = 3;
                if (r3 != r4) goto L_0x163b;
                r3 = r8;	 Catch:{ Exception -> 0x161f, all -> 0x1603 }
                if (r3 != 0) goto L_0x163b;	 Catch:{ Exception -> 0x161f, all -> 0x1603 }
                r3 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x161f, all -> 0x1603 }
                r3 = r3.database;	 Catch:{ Exception -> 0x161f, all -> 0x1603 }
                r4 = java.util.Locale.US;	 Catch:{ Exception -> 0x161f, all -> 0x1603 }
                r6 = "SELECT min(mid) FROM messages WHERE uid = %d AND mid < 0";	 Catch:{ Exception -> 0x161f, all -> 0x1603 }
                r7 = 1;	 Catch:{ Exception -> 0x161f, all -> 0x1603 }
                r8 = new java.lang.Object[r7];	 Catch:{ Exception -> 0x161f, all -> 0x1603 }
                r9 = r5;	 Catch:{ Exception -> 0x161f, all -> 0x1603 }
                r7 = java.lang.Long.valueOf(r9);	 Catch:{ Exception -> 0x161f, all -> 0x1603 }
                r9 = 0;	 Catch:{ Exception -> 0x161f, all -> 0x1603 }
                r8[r9] = r7;	 Catch:{ Exception -> 0x161f, all -> 0x1603 }
                r4 = java.lang.String.format(r4, r6, r8);	 Catch:{ Exception -> 0x161f, all -> 0x1603 }
                r6 = new java.lang.Object[r9];	 Catch:{ Exception -> 0x161f, all -> 0x1603 }
                r3 = r3.queryFinalized(r4, r6);	 Catch:{ Exception -> 0x161f, all -> 0x1603 }
                r4 = r3.next();	 Catch:{ Exception -> 0x161f, all -> 0x1603 }
                if (r4 == 0) goto L_0x1502;
                r4 = 0;
                r6 = r3.intValue(r4);	 Catch:{ Exception -> 0x14e9, all -> 0x14d0 }
                r7 = r6;
                goto L_0x1504;
            L_0x14d0:
                r0 = move-exception;
                r28 = r2;
                r67 = r5;
                r65 = r11;
                r59 = r22;
                r47 = r23;
                r50 = r24;
                r51 = r30;
                r53 = r31;
                r22 = r38;
                r27 = r52;
                r26 = r77;
                goto L_0x01b8;
            L_0x14e9:
                r0 = move-exception;
                r28 = r2;
                r67 = r5;
                r65 = r11;
                r59 = r22;
                r47 = r23;
                r50 = r24;
                r51 = r30;
                r53 = r31;
                r95 = r38;
                r27 = r52;
                r26 = r77;
                goto L_0x01d2;
                r7 = r24;
                r3.dispose();	 Catch:{ Exception -> 0x15f4, all -> 0x15e5 }
                r4 = 0;	 Catch:{ Exception -> 0x15f4, all -> 0x15e5 }
                r6 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x15f4, all -> 0x15e5 }
                r6 = r6.database;	 Catch:{ Exception -> 0x15f4, all -> 0x15e5 }
                r8 = java.util.Locale.US;	 Catch:{ Exception -> 0x15f4, all -> 0x15e5 }
                r9 = "SELECT max(mid), max(date) FROM messages WHERE uid = %d AND out = 0 AND read_state IN(0,2) AND mid < 0";	 Catch:{ Exception -> 0x15f4, all -> 0x15e5 }
                r10 = 1;	 Catch:{ Exception -> 0x15f4, all -> 0x15e5 }
                r13 = new java.lang.Object[r10];	 Catch:{ Exception -> 0x15f4, all -> 0x15e5 }
                r14 = r5;	 Catch:{ Exception -> 0x15f4, all -> 0x15e5 }
                r10 = java.lang.Long.valueOf(r14);	 Catch:{ Exception -> 0x15f4, all -> 0x15e5 }
                r14 = 0;	 Catch:{ Exception -> 0x15f4, all -> 0x15e5 }
                r13[r14] = r10;	 Catch:{ Exception -> 0x15f4, all -> 0x15e5 }
                r8 = java.lang.String.format(r8, r9, r13);	 Catch:{ Exception -> 0x15f4, all -> 0x15e5 }
                r9 = new java.lang.Object[r14];	 Catch:{ Exception -> 0x15f4, all -> 0x15e5 }
                r6 = r6.queryFinalized(r8, r9);	 Catch:{ Exception -> 0x15f4, all -> 0x15e5 }
                r3 = r6;	 Catch:{ Exception -> 0x15f4, all -> 0x15e5 }
                r6 = r3.next();	 Catch:{ Exception -> 0x15f4, all -> 0x15e5 }
                if (r6 == 0) goto L_0x1559;
                r6 = 0;
                r8 = r3.intValue(r6);	 Catch:{ Exception -> 0x154b, all -> 0x153d }
                r4 = r8;	 Catch:{ Exception -> 0x154b, all -> 0x153d }
                r6 = 1;	 Catch:{ Exception -> 0x154b, all -> 0x153d }
                r8 = r3.intValue(r6);	 Catch:{ Exception -> 0x154b, all -> 0x153d }
                r6 = r8;
                r10 = r6;
                goto L_0x155b;
            L_0x153d:
                r0 = move-exception;
                r28 = r2;
                r67 = r5;
                r50 = r7;
                r65 = r11;
                r59 = r22;
                r47 = r23;
                goto L_0x14dd;
            L_0x154b:
                r0 = move-exception;
                r28 = r2;
                r67 = r5;
                r50 = r7;
                r65 = r11;
                r59 = r22;
                r47 = r23;
                goto L_0x14f6;
                r10 = r30;
                r3.dispose();	 Catch:{ Exception -> 0x15d4, all -> 0x15c3 }
                if (r4 == 0) goto L_0x15bf;	 Catch:{ Exception -> 0x15d4, all -> 0x15c3 }
                r7 = r4;	 Catch:{ Exception -> 0x15d4, all -> 0x15c3 }
                r6 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x15d4, all -> 0x15c3 }
                r6 = r6.database;	 Catch:{ Exception -> 0x15d4, all -> 0x15c3 }
                r8 = java.util.Locale.US;	 Catch:{ Exception -> 0x15d4, all -> 0x15c3 }
                r9 = "SELECT COUNT(*) FROM messages WHERE uid = %d AND mid <= %d AND out = 0 AND read_state IN(0,2)";	 Catch:{ Exception -> 0x15d4, all -> 0x15c3 }
                r13 = 2;	 Catch:{ Exception -> 0x15d4, all -> 0x15c3 }
                r14 = new java.lang.Object[r13];	 Catch:{ Exception -> 0x15d4, all -> 0x15c3 }
                r80 = r2;
                r81 = r3;
                r2 = r5;	 Catch:{ Exception -> 0x16b7, all -> 0x169d }
                r2 = java.lang.Long.valueOf(r2);	 Catch:{ Exception -> 0x16b7, all -> 0x169d }
                r3 = 0;	 Catch:{ Exception -> 0x16b7, all -> 0x169d }
                r14[r3] = r2;	 Catch:{ Exception -> 0x16b7, all -> 0x169d }
                r2 = java.lang.Integer.valueOf(r4);	 Catch:{ Exception -> 0x16b7, all -> 0x169d }
                r3 = 1;	 Catch:{ Exception -> 0x16b7, all -> 0x169d }
                r14[r3] = r2;	 Catch:{ Exception -> 0x16b7, all -> 0x169d }
                r2 = java.lang.String.format(r8, r9, r14);	 Catch:{ Exception -> 0x16b7, all -> 0x169d }
                r3 = 0;	 Catch:{ Exception -> 0x16b7, all -> 0x169d }
                r8 = new java.lang.Object[r3];	 Catch:{ Exception -> 0x16b7, all -> 0x169d }
                r2 = r6.queryFinalized(r2, r8);	 Catch:{ Exception -> 0x16b7, all -> 0x169d }
                r3 = r2.next();	 Catch:{ Exception -> 0x16b7, all -> 0x169d }
                if (r3 == 0) goto L_0x159a;	 Catch:{ Exception -> 0x16b7, all -> 0x169d }
                r3 = 0;	 Catch:{ Exception -> 0x16b7, all -> 0x169d }
                r6 = r2.intValue(r3);	 Catch:{ Exception -> 0x16b7, all -> 0x169d }
                r3 = r6;
                goto L_0x159c;
                r3 = r22;
                r2.dispose();	 Catch:{ Exception -> 0x15b1, all -> 0x15a3 }
                r22 = r3;
                goto L_0x1641;
            L_0x15a3:
                r0 = move-exception;
                r2 = r0;
                r59 = r3;
                r67 = r5;
                r50 = r7;
                r51 = r10;
                r65 = r11;
                goto L_0x16a9;
            L_0x15b1:
                r0 = move-exception;
                r2 = r0;
                r59 = r3;
                r67 = r5;
                r50 = r7;
                r51 = r10;
                r65 = r11;
                goto L_0x16c3;
                r80 = r2;
                goto L_0x1641;
            L_0x15c3:
                r0 = move-exception;
                r80 = r2;
                r2 = r0;
                r67 = r5;
                r50 = r7;
                r51 = r10;
                r65 = r11;
                r59 = r22;
                r47 = r23;
                goto L_0x1613;
            L_0x15d4:
                r0 = move-exception;
                r80 = r2;
                r2 = r0;
                r67 = r5;
                r50 = r7;
                r51 = r10;
                r65 = r11;
                r59 = r22;
                r47 = r23;
                goto L_0x162f;
            L_0x15e5:
                r0 = move-exception;
                r80 = r2;
                r2 = r0;
                r67 = r5;
                r50 = r7;
                r65 = r11;
                r59 = r22;
                r47 = r23;
                goto L_0x1611;
            L_0x15f4:
                r0 = move-exception;
                r80 = r2;
                r2 = r0;
                r67 = r5;
                r50 = r7;
                r65 = r11;
                r59 = r22;
                r47 = r23;
                goto L_0x162d;
            L_0x1603:
                r0 = move-exception;
                r80 = r2;
                r2 = r0;
                r67 = r5;
                r65 = r11;
                r59 = r22;
                r47 = r23;
                r50 = r24;
                r51 = r30;
                r53 = r31;
                r22 = r38;
                r27 = r52;
                r26 = r77;
                r28 = r80;
                goto L_0x242b;
            L_0x161f:
                r0 = move-exception;
                r80 = r2;
                r2 = r0;
                r67 = r5;
                r65 = r11;
                r59 = r22;
                r47 = r23;
                r50 = r24;
                r51 = r30;
                r53 = r31;
                r95 = r38;
                r27 = r52;
                r26 = r77;
                r28 = r80;
                goto L_0x23dc;
                r80 = r2;
                r7 = r24;
                r10 = r30;
                r2 = r7;	 Catch:{ Exception -> 0x21e5, all -> 0x21c7 }
                r3 = 3;
                if (r2 == r3) goto L_0x1aa8;
                r2 = r7;	 Catch:{ Exception -> 0x1a8a, all -> 0x1a6c }
                r3 = 4;	 Catch:{ Exception -> 0x1a8a, all -> 0x1a6c }
                if (r2 != r3) goto L_0x1653;	 Catch:{ Exception -> 0x1a8a, all -> 0x1a6c }
                r82 = r7;	 Catch:{ Exception -> 0x1a8a, all -> 0x1a6c }
                r83 = r48;	 Catch:{ Exception -> 0x1a8a, all -> 0x1a6c }
                r9 = r52;	 Catch:{ Exception -> 0x1a8a, all -> 0x1a6c }
                goto L_0x1aae;	 Catch:{ Exception -> 0x1a8a, all -> 0x1a6c }
                r2 = r7;	 Catch:{ Exception -> 0x1a8a, all -> 0x1a6c }
                r3 = 1;
                if (r2 != r3) goto L_0x16d1;
                r2 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x16b7, all -> 0x169d }
                r2 = r2.database;	 Catch:{ Exception -> 0x16b7, all -> 0x169d }
                r3 = java.util.Locale.US;	 Catch:{ Exception -> 0x16b7, all -> 0x169d }
                r4 = "SELECT m.read_state, m.data, m.send_state, m.mid, m.date, r.random_id, m.replydata, m.media, m.ttl, m.mention FROM messages as m LEFT JOIN randoms as r ON r.mid = m.mid WHERE m.uid = %d AND m.mid < %d ORDER BY m.mid DESC LIMIT %d";	 Catch:{ Exception -> 0x16b7, all -> 0x169d }
                r6 = 3;	 Catch:{ Exception -> 0x16b7, all -> 0x169d }
                r8 = new java.lang.Object[r6];	 Catch:{ Exception -> 0x16b7, all -> 0x169d }
                r13 = r5;	 Catch:{ Exception -> 0x16b7, all -> 0x169d }
                r6 = java.lang.Long.valueOf(r13);	 Catch:{ Exception -> 0x16b7, all -> 0x169d }
                r9 = 0;	 Catch:{ Exception -> 0x16b7, all -> 0x169d }
                r8[r9] = r6;	 Catch:{ Exception -> 0x16b7, all -> 0x169d }
                r6 = r3;	 Catch:{ Exception -> 0x16b7, all -> 0x169d }
                r6 = java.lang.Integer.valueOf(r6);	 Catch:{ Exception -> 0x16b7, all -> 0x169d }
                r9 = 1;	 Catch:{ Exception -> 0x16b7, all -> 0x169d }
                r8[r9] = r6;	 Catch:{ Exception -> 0x16b7, all -> 0x169d }
                r6 = java.lang.Integer.valueOf(r5);	 Catch:{ Exception -> 0x16b7, all -> 0x169d }
                r9 = 2;	 Catch:{ Exception -> 0x16b7, all -> 0x169d }
                r8[r9] = r6;	 Catch:{ Exception -> 0x16b7, all -> 0x169d }
                r3 = java.lang.String.format(r3, r4, r8);	 Catch:{ Exception -> 0x16b7, all -> 0x169d }
                r4 = 0;	 Catch:{ Exception -> 0x16b7, all -> 0x169d }
                r6 = new java.lang.Object[r4];	 Catch:{ Exception -> 0x16b7, all -> 0x169d }
                r2 = r2.queryFinalized(r3, r6);	 Catch:{ Exception -> 0x16b7, all -> 0x169d }
                r67 = r5;
                r50 = r7;
                r51 = r10;
                r65 = r11;
                r59 = r22;
                r3 = r23;
                r15 = r31;
                r9 = r52;
                r28 = r80;
                goto L_0x1b77;
            L_0x169d:
                r0 = move-exception;
                r2 = r0;
                r67 = r5;
                r50 = r7;
                r51 = r10;
                r65 = r11;
                r59 = r22;
                r47 = r23;
                r53 = r31;
                r22 = r38;
                r27 = r52;
                r26 = r77;
                r28 = r80;
                goto L_0x242b;
            L_0x16b7:
                r0 = move-exception;
                r2 = r0;
                r67 = r5;
                r50 = r7;
                r51 = r10;
                r65 = r11;
                r59 = r22;
                r47 = r23;
                r53 = r31;
                r95 = r38;
                r27 = r52;
                r26 = r77;
                r28 = r80;
                goto L_0x23dc;
                r2 = r8;	 Catch:{ Exception -> 0x1a8a, all -> 0x1a6c }
                if (r2 == 0) goto L_0x1794;
                r2 = r3;	 Catch:{ Exception -> 0x1778, all -> 0x175c }
                if (r2 == 0) goto L_0x170c;
                r2 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x16b7, all -> 0x169d }
                r2 = r2.database;	 Catch:{ Exception -> 0x16b7, all -> 0x169d }
                r3 = java.util.Locale.US;	 Catch:{ Exception -> 0x16b7, all -> 0x169d }
                r4 = "SELECT m.read_state, m.data, m.send_state, m.mid, m.date, r.random_id, m.replydata, m.media, m.ttl, m.mention FROM messages as m LEFT JOIN randoms as r ON r.mid = m.mid WHERE m.uid = %d AND m.mid > %d ORDER BY m.mid ASC LIMIT %d";	 Catch:{ Exception -> 0x16b7, all -> 0x169d }
                r6 = 3;	 Catch:{ Exception -> 0x16b7, all -> 0x169d }
                r8 = new java.lang.Object[r6];	 Catch:{ Exception -> 0x16b7, all -> 0x169d }
                r13 = r5;	 Catch:{ Exception -> 0x16b7, all -> 0x169d }
                r6 = java.lang.Long.valueOf(r13);	 Catch:{ Exception -> 0x16b7, all -> 0x169d }
                r9 = 0;	 Catch:{ Exception -> 0x16b7, all -> 0x169d }
                r8[r9] = r6;	 Catch:{ Exception -> 0x16b7, all -> 0x169d }
                r6 = r3;	 Catch:{ Exception -> 0x16b7, all -> 0x169d }
                r6 = java.lang.Integer.valueOf(r6);	 Catch:{ Exception -> 0x16b7, all -> 0x169d }
                r9 = 1;	 Catch:{ Exception -> 0x16b7, all -> 0x169d }
                r8[r9] = r6;	 Catch:{ Exception -> 0x16b7, all -> 0x169d }
                r6 = java.lang.Integer.valueOf(r5);	 Catch:{ Exception -> 0x16b7, all -> 0x169d }
                r9 = 2;	 Catch:{ Exception -> 0x16b7, all -> 0x169d }
                r8[r9] = r6;	 Catch:{ Exception -> 0x16b7, all -> 0x169d }
                r3 = java.lang.String.format(r3, r4, r8);	 Catch:{ Exception -> 0x16b7, all -> 0x169d }
                r4 = 0;	 Catch:{ Exception -> 0x16b7, all -> 0x169d }
                r6 = new java.lang.Object[r4];	 Catch:{ Exception -> 0x16b7, all -> 0x169d }
                r2 = r2.queryFinalized(r3, r6);	 Catch:{ Exception -> 0x16b7, all -> 0x169d }
                goto L_0x1689;
                r2 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x1778, all -> 0x175c }
                r2 = r2.database;	 Catch:{ Exception -> 0x1778, all -> 0x175c }
                r3 = java.util.Locale.US;	 Catch:{ Exception -> 0x1778, all -> 0x175c }
                r4 = "SELECT m.read_state, m.data, m.send_state, m.mid, m.date, r.random_id, m.replydata, m.media, m.ttl, m.mention FROM messages as m LEFT JOIN randoms as r ON r.mid = m.mid WHERE m.uid = %d AND m.date <= %d ORDER BY m.mid ASC LIMIT %d,%d";	 Catch:{ Exception -> 0x1778, all -> 0x175c }
                r6 = 4;	 Catch:{ Exception -> 0x1778, all -> 0x175c }
                r8 = new java.lang.Object[r6];	 Catch:{ Exception -> 0x1778, all -> 0x175c }
                r13 = r5;	 Catch:{ Exception -> 0x1778, all -> 0x175c }
                r6 = java.lang.Long.valueOf(r13);	 Catch:{ Exception -> 0x1778, all -> 0x175c }
                r9 = 0;	 Catch:{ Exception -> 0x1778, all -> 0x175c }
                r8[r9] = r6;	 Catch:{ Exception -> 0x1778, all -> 0x175c }
                r6 = r8;	 Catch:{ Exception -> 0x1778, all -> 0x175c }
                r6 = java.lang.Integer.valueOf(r6);	 Catch:{ Exception -> 0x1778, all -> 0x175c }
                r9 = 1;	 Catch:{ Exception -> 0x1778, all -> 0x175c }
                r8[r9] = r6;	 Catch:{ Exception -> 0x1778, all -> 0x175c }
                r6 = r48;
                r9 = java.lang.Integer.valueOf(r6);	 Catch:{ Exception -> 0x1754, all -> 0x174c }
                r13 = 2;	 Catch:{ Exception -> 0x1754, all -> 0x174c }
                r8[r13] = r9;	 Catch:{ Exception -> 0x1754, all -> 0x174c }
                r9 = java.lang.Integer.valueOf(r5);	 Catch:{ Exception -> 0x1754, all -> 0x174c }
                r13 = 3;	 Catch:{ Exception -> 0x1754, all -> 0x174c }
                r8[r13] = r9;	 Catch:{ Exception -> 0x1754, all -> 0x174c }
                r3 = java.lang.String.format(r3, r4, r8);	 Catch:{ Exception -> 0x1754, all -> 0x174c }
                r4 = 0;	 Catch:{ Exception -> 0x1754, all -> 0x174c }
                r8 = new java.lang.Object[r4];	 Catch:{ Exception -> 0x1754, all -> 0x174c }
                r2 = r2.queryFinalized(r3, r8);	 Catch:{ Exception -> 0x1754, all -> 0x174c }
                r67 = r5;
                r48 = r6;
                goto L_0x168b;
            L_0x174c:
                r0 = move-exception;
                r2 = r0;
                r67 = r5;
                r48 = r6;
                goto L_0x16a1;
            L_0x1754:
                r0 = move-exception;
                r2 = r0;
                r67 = r5;
                r48 = r6;
                goto L_0x16bb;
            L_0x175c:
                r0 = move-exception;
                r6 = r48;
                r2 = r0;
                r67 = r5;
                r50 = r7;
                r51 = r10;
                r65 = r11;
                r59 = r22;
                r47 = r23;
                r53 = r31;
                r22 = r38;
                r27 = r52;
                r26 = r77;
                r28 = r80;
                goto L_0x242b;
            L_0x1778:
                r0 = move-exception;
                r6 = r48;
                r2 = r0;
                r67 = r5;
                r50 = r7;
                r51 = r10;
                r65 = r11;
                r59 = r22;
                r47 = r23;
                r53 = r31;
                r95 = r38;
                r27 = r52;
                r26 = r77;
                r28 = r80;
                goto L_0x23dc;
                r6 = r48;
                r2 = r7;	 Catch:{ Exception -> 0x1a4c, all -> 0x1a2c }
                r3 = 2;
                if (r2 != r3) goto L_0x1906;
                r2 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x18e6, all -> 0x18c6 }
                r2 = r2.database;	 Catch:{ Exception -> 0x18e6, all -> 0x18c6 }
                r3 = java.util.Locale.US;	 Catch:{ Exception -> 0x18e6, all -> 0x18c6 }
                r4 = "SELECT min(mid) FROM messages WHERE uid = %d AND mid < 0";	 Catch:{ Exception -> 0x18e6, all -> 0x18c6 }
                r8 = 1;	 Catch:{ Exception -> 0x18e6, all -> 0x18c6 }
                r9 = new java.lang.Object[r8];	 Catch:{ Exception -> 0x18e6, all -> 0x18c6 }
                r13 = r5;	 Catch:{ Exception -> 0x18e6, all -> 0x18c6 }
                r8 = java.lang.Long.valueOf(r13);	 Catch:{ Exception -> 0x18e6, all -> 0x18c6 }
                r13 = 0;	 Catch:{ Exception -> 0x18e6, all -> 0x18c6 }
                r9[r13] = r8;	 Catch:{ Exception -> 0x18e6, all -> 0x18c6 }
                r3 = java.lang.String.format(r3, r4, r9);	 Catch:{ Exception -> 0x18e6, all -> 0x18c6 }
                r4 = new java.lang.Object[r13];	 Catch:{ Exception -> 0x18e6, all -> 0x18c6 }
                r2 = r2.queryFinalized(r3, r4);	 Catch:{ Exception -> 0x18e6, all -> 0x18c6 }
                r3 = r2.next();	 Catch:{ Exception -> 0x18e6, all -> 0x18c6 }
                if (r3 == 0) goto L_0x17c9;
                r3 = 0;
                r4 = r2.intValue(r3);	 Catch:{ Exception -> 0x1754, all -> 0x174c }
                r3 = r4;
                r8 = r3;
                goto L_0x17cb;
                r8 = r25;
                r2.dispose();	 Catch:{ Exception -> 0x18bb, all -> 0x18b0 }
                r3 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x18bb, all -> 0x18b0 }
                r3 = r3.database;	 Catch:{ Exception -> 0x18bb, all -> 0x18b0 }
                r4 = java.util.Locale.US;	 Catch:{ Exception -> 0x18bb, all -> 0x18b0 }
                r9 = "SELECT max(mid), max(date) FROM messages WHERE uid = %d AND out = 0 AND read_state IN(0,2) AND mid < 0";	 Catch:{ Exception -> 0x18bb, all -> 0x18b0 }
                r13 = 1;	 Catch:{ Exception -> 0x18bb, all -> 0x18b0 }
                r14 = new java.lang.Object[r13];	 Catch:{ Exception -> 0x18bb, all -> 0x18b0 }
                r83 = r6;
                r82 = r7;
                r6 = r5;	 Catch:{ Exception -> 0x189a, all -> 0x1884 }
                r6 = java.lang.Long.valueOf(r6);	 Catch:{ Exception -> 0x189a, all -> 0x1884 }
                r7 = 0;	 Catch:{ Exception -> 0x189a, all -> 0x1884 }
                r14[r7] = r6;	 Catch:{ Exception -> 0x189a, all -> 0x1884 }
                r4 = java.lang.String.format(r4, r9, r14);	 Catch:{ Exception -> 0x189a, all -> 0x1884 }
                r6 = new java.lang.Object[r7];	 Catch:{ Exception -> 0x189a, all -> 0x1884 }
                r3 = r3.queryFinalized(r4, r6);	 Catch:{ Exception -> 0x189a, all -> 0x1884 }
                r2 = r3;	 Catch:{ Exception -> 0x189a, all -> 0x1884 }
                r3 = r2.next();	 Catch:{ Exception -> 0x189a, all -> 0x1884 }
                if (r3 == 0) goto L_0x1807;	 Catch:{ Exception -> 0x189a, all -> 0x1884 }
                r3 = 0;	 Catch:{ Exception -> 0x189a, all -> 0x1884 }
                r4 = r2.intValue(r3);	 Catch:{ Exception -> 0x189a, all -> 0x1884 }
                r7 = r4;
                r3 = 1;
                r4 = r2.intValue(r3);	 Catch:{ Exception -> 0x1868, all -> 0x184c }
                r3 = r4;	 Catch:{ Exception -> 0x1868, all -> 0x184c }
                r10 = r3;	 Catch:{ Exception -> 0x1868, all -> 0x184c }
                goto L_0x1809;	 Catch:{ Exception -> 0x1868, all -> 0x184c }
                r7 = r82;	 Catch:{ Exception -> 0x1868, all -> 0x184c }
                r2.dispose();	 Catch:{ Exception -> 0x1868, all -> 0x184c }
                if (r7 == 0) goto L_0x1848;	 Catch:{ Exception -> 0x1868, all -> 0x184c }
                r3 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x1868, all -> 0x184c }
                r3 = r3.database;	 Catch:{ Exception -> 0x1868, all -> 0x184c }
                r4 = java.util.Locale.US;	 Catch:{ Exception -> 0x1868, all -> 0x184c }
                r6 = "SELECT COUNT(*) FROM messages WHERE uid = %d AND mid <= %d AND out = 0 AND read_state IN(0,2)";	 Catch:{ Exception -> 0x1868, all -> 0x184c }
                r9 = 2;	 Catch:{ Exception -> 0x1868, all -> 0x184c }
                r13 = new java.lang.Object[r9];	 Catch:{ Exception -> 0x1868, all -> 0x184c }
                r14 = r5;	 Catch:{ Exception -> 0x1868, all -> 0x184c }
                r9 = java.lang.Long.valueOf(r14);	 Catch:{ Exception -> 0x1868, all -> 0x184c }
                r14 = 0;	 Catch:{ Exception -> 0x1868, all -> 0x184c }
                r13[r14] = r9;	 Catch:{ Exception -> 0x1868, all -> 0x184c }
                r9 = java.lang.Integer.valueOf(r7);	 Catch:{ Exception -> 0x1868, all -> 0x184c }
                r14 = 1;	 Catch:{ Exception -> 0x1868, all -> 0x184c }
                r13[r14] = r9;	 Catch:{ Exception -> 0x1868, all -> 0x184c }
                r4 = java.lang.String.format(r4, r6, r13);	 Catch:{ Exception -> 0x1868, all -> 0x184c }
                r6 = 0;	 Catch:{ Exception -> 0x1868, all -> 0x184c }
                r9 = new java.lang.Object[r6];	 Catch:{ Exception -> 0x1868, all -> 0x184c }
                r3 = r3.queryFinalized(r4, r9);	 Catch:{ Exception -> 0x1868, all -> 0x184c }
                r2 = r3;	 Catch:{ Exception -> 0x1868, all -> 0x184c }
                r3 = r2.next();	 Catch:{ Exception -> 0x1868, all -> 0x184c }
                if (r3 == 0) goto L_0x1845;	 Catch:{ Exception -> 0x1868, all -> 0x184c }
                r3 = 0;	 Catch:{ Exception -> 0x1868, all -> 0x184c }
                r4 = r2.intValue(r3);	 Catch:{ Exception -> 0x1868, all -> 0x184c }
                r3 = r4;	 Catch:{ Exception -> 0x1868, all -> 0x184c }
                r22 = r3;	 Catch:{ Exception -> 0x1868, all -> 0x184c }
                r2.dispose();	 Catch:{ Exception -> 0x1868, all -> 0x184c }
                r2 = r22;
                goto L_0x190e;
            L_0x184c:
                r0 = move-exception;
                r2 = r0;
                r67 = r5;
                r50 = r7;
                r25 = r8;
                r51 = r10;
                r65 = r11;
                r59 = r22;
                r47 = r23;
                r53 = r31;
                r22 = r38;
                r27 = r52;
                r26 = r77;
                r28 = r80;
                goto L_0x1af3;
            L_0x1868:
                r0 = move-exception;
                r2 = r0;
                r67 = r5;
                r50 = r7;
                r25 = r8;
                r51 = r10;
                r65 = r11;
                r59 = r22;
                r47 = r23;
                r53 = r31;
                r95 = r38;
                r27 = r52;
                r26 = r77;
                r28 = r80;
                goto L_0x1b0f;
            L_0x1884:
                r0 = move-exception;
                r2 = r0;
                r67 = r5;
                r25 = r8;
                r51 = r10;
                r65 = r11;
                r59 = r22;
                r47 = r23;
                r53 = r31;
                r22 = r38;
                r27 = r52;
                goto L_0x1aed;
            L_0x189a:
                r0 = move-exception;
                r2 = r0;
                r67 = r5;
                r25 = r8;
                r51 = r10;
                r65 = r11;
                r59 = r22;
                r47 = r23;
                r53 = r31;
                r95 = r38;
                r27 = r52;
                goto L_0x1b09;
            L_0x18b0:
                r0 = move-exception;
                r83 = r6;
                r82 = r7;
                r2 = r0;
                r67 = r5;
                r25 = r8;
                goto L_0x18ce;
            L_0x18bb:
                r0 = move-exception;
                r83 = r6;
                r82 = r7;
                r2 = r0;
                r67 = r5;
                r25 = r8;
                goto L_0x18ee;
            L_0x18c6:
                r0 = move-exception;
                r83 = r6;
                r82 = r7;
                r2 = r0;
                r67 = r5;
                r51 = r10;
                r65 = r11;
                r59 = r22;
                r47 = r23;
                r53 = r31;
                r22 = r38;
                r27 = r52;
                r26 = r77;
                r28 = r80;
                r50 = r82;
                r48 = r83;
                goto L_0x242b;
            L_0x18e6:
                r0 = move-exception;
                r83 = r6;
                r82 = r7;
                r2 = r0;
                r67 = r5;
                r51 = r10;
                r65 = r11;
                r59 = r22;
                r47 = r23;
                r53 = r31;
                r95 = r38;
                r27 = r52;
                r26 = r77;
                r28 = r80;
                r50 = r82;
                r48 = r83;
                goto L_0x23dc;
                r83 = r6;
                r82 = r7;
                r2 = r22;
                r8 = r25;
                if (r5 > r2) goto L_0x191a;
                r9 = r52;
                if (r2 >= r9) goto L_0x1915;
                goto L_0x191c;
                r6 = r2 - r5;
                r5 = r5 + 10;
                goto L_0x192a;
                r9 = r52;
                r3 = r2 + 10;
                r3 = java.lang.Math.max(r5, r3);	 Catch:{ Exception -> 0x1a0f, all -> 0x19f2 }
                r5 = r3;
                if (r2 >= r9) goto L_0x1928;
                r2 = 0;
                r7 = 0;
                r8 = 0;
                r6 = r83;
                r3 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x19d0, all -> 0x19ae }
                r3 = r3.database;	 Catch:{ Exception -> 0x19d0, all -> 0x19ae }
                r4 = java.util.Locale.US;	 Catch:{ Exception -> 0x19d0, all -> 0x19ae }
                r13 = "SELECT m.read_state, m.data, m.send_state, m.mid, m.date, r.random_id, m.replydata, m.media, m.ttl, m.mention FROM messages as m LEFT JOIN randoms as r ON r.mid = m.mid WHERE m.uid = %d ORDER BY m.mid ASC LIMIT %d,%d";	 Catch:{ Exception -> 0x19d0, all -> 0x19ae }
                r14 = 3;	 Catch:{ Exception -> 0x19d0, all -> 0x19ae }
                r15 = new java.lang.Object[r14];	 Catch:{ Exception -> 0x19d0, all -> 0x19ae }
                r84 = r7;
                r85 = r8;
                r7 = r5;	 Catch:{ Exception -> 0x1991, all -> 0x1974 }
                r7 = java.lang.Long.valueOf(r7);	 Catch:{ Exception -> 0x1991, all -> 0x1974 }
                r8 = 0;	 Catch:{ Exception -> 0x1991, all -> 0x1974 }
                r15[r8] = r7;	 Catch:{ Exception -> 0x1991, all -> 0x1974 }
                r7 = java.lang.Integer.valueOf(r6);	 Catch:{ Exception -> 0x1991, all -> 0x1974 }
                r8 = 1;	 Catch:{ Exception -> 0x1991, all -> 0x1974 }
                r15[r8] = r7;	 Catch:{ Exception -> 0x1991, all -> 0x1974 }
                r7 = java.lang.Integer.valueOf(r5);	 Catch:{ Exception -> 0x1991, all -> 0x1974 }
                r8 = 2;	 Catch:{ Exception -> 0x1991, all -> 0x1974 }
                r15[r8] = r7;	 Catch:{ Exception -> 0x1991, all -> 0x1974 }
                r4 = java.lang.String.format(r4, r13, r15);	 Catch:{ Exception -> 0x1991, all -> 0x1974 }
                r7 = 0;	 Catch:{ Exception -> 0x1991, all -> 0x1974 }
                r8 = new java.lang.Object[r7];	 Catch:{ Exception -> 0x1991, all -> 0x1974 }
                r3 = r3.queryFinalized(r4, r8);	 Catch:{ Exception -> 0x1991, all -> 0x1974 }
                r59 = r2;
                r2 = r3;
                r67 = r5;
                r48 = r6;
                r51 = r10;
                r65 = r11;
                r3 = r23;
                r15 = r31;
                r28 = r80;
                r50 = r84;
                r25 = r85;
                goto L_0x1b77;
            L_0x1974:
                r0 = move-exception;
                r59 = r2;
                r67 = r5;
                r48 = r6;
                r27 = r9;
                r51 = r10;
                r65 = r11;
                r47 = r23;
                r53 = r31;
                r22 = r38;
                r26 = r77;
                r28 = r80;
                r50 = r84;
                r25 = r85;
                goto L_0x01b8;
            L_0x1991:
                r0 = move-exception;
                r59 = r2;
                r67 = r5;
                r48 = r6;
                r27 = r9;
                r51 = r10;
                r65 = r11;
                r47 = r23;
                r53 = r31;
                r95 = r38;
                r26 = r77;
                r28 = r80;
                r50 = r84;
                r25 = r85;
                goto L_0x01d2;
            L_0x19ae:
                r0 = move-exception;
                r84 = r7;
                r85 = r8;
                r59 = r2;
                r67 = r5;
                r48 = r6;
                r27 = r9;
                r51 = r10;
                r65 = r11;
                r47 = r23;
                r53 = r31;
                r22 = r38;
                r26 = r77;
                r28 = r80;
                r50 = r84;
                r25 = r85;
                r2 = r0;
                goto L_0x242b;
            L_0x19d0:
                r0 = move-exception;
                r84 = r7;
                r85 = r8;
                r59 = r2;
                r67 = r5;
                r48 = r6;
                r27 = r9;
                r51 = r10;
                r65 = r11;
                r47 = r23;
                r53 = r31;
                r95 = r38;
                r26 = r77;
                r28 = r80;
                r50 = r84;
                r25 = r85;
                r2 = r0;
                goto L_0x23dc;
            L_0x19f2:
                r0 = move-exception;
                r59 = r2;
                r67 = r5;
                r50 = r7;
                r25 = r8;
                r27 = r9;
                r51 = r10;
                r65 = r11;
                r47 = r23;
                r53 = r31;
                r22 = r38;
                r26 = r77;
                r28 = r80;
                r48 = r83;
                goto L_0x01b8;
            L_0x1a0f:
                r0 = move-exception;
                r59 = r2;
                r67 = r5;
                r50 = r7;
                r25 = r8;
                r27 = r9;
                r51 = r10;
                r65 = r11;
                r47 = r23;
                r53 = r31;
                r95 = r38;
                r26 = r77;
                r28 = r80;
                r48 = r83;
                goto L_0x01d2;
            L_0x1a2c:
                r0 = move-exception;
                r83 = r6;
                r82 = r7;
                r2 = r0;
                r67 = r5;
                r51 = r10;
                r65 = r11;
                r59 = r22;
                r47 = r23;
                r53 = r31;
                r22 = r38;
                r27 = r52;
                r26 = r77;
                r28 = r80;
                r50 = r82;
                r48 = r83;
                goto L_0x242b;
            L_0x1a4c:
                r0 = move-exception;
                r83 = r6;
                r82 = r7;
                r2 = r0;
                r67 = r5;
                r51 = r10;
                r65 = r11;
                r59 = r22;
                r47 = r23;
                r53 = r31;
                r95 = r38;
                r27 = r52;
                r26 = r77;
                r28 = r80;
                r50 = r82;
                r48 = r83;
                goto L_0x23dc;
            L_0x1a6c:
                r0 = move-exception;
                r82 = r7;
                r83 = r48;
                r2 = r0;
                r67 = r5;
                r51 = r10;
                r65 = r11;
                r59 = r22;
                r47 = r23;
                r53 = r31;
                r22 = r38;
                r27 = r52;
                r26 = r77;
                r28 = r80;
                r50 = r82;
                goto L_0x242b;
            L_0x1a8a:
                r0 = move-exception;
                r82 = r7;
                r83 = r48;
                r2 = r0;
                r67 = r5;
                r51 = r10;
                r65 = r11;
                r59 = r22;
                r47 = r23;
                r53 = r31;
                r95 = r38;
                r27 = r52;
                r26 = r77;
                r28 = r80;
                r50 = r82;
                goto L_0x23dc;
                r82 = r7;
                r83 = r48;
                r9 = r52;
                r2 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x21ab, all -> 0x218f }
                r2 = r2.database;	 Catch:{ Exception -> 0x21ab, all -> 0x218f }
                r3 = java.util.Locale.US;	 Catch:{ Exception -> 0x21ab, all -> 0x218f }
                r4 = "SELECT min(mid) FROM messages WHERE uid = %d AND mid < 0";	 Catch:{ Exception -> 0x21ab, all -> 0x218f }
                r6 = 1;	 Catch:{ Exception -> 0x21ab, all -> 0x218f }
                r7 = new java.lang.Object[r6];	 Catch:{ Exception -> 0x21ab, all -> 0x218f }
                r13 = r5;	 Catch:{ Exception -> 0x21ab, all -> 0x218f }
                r6 = java.lang.Long.valueOf(r13);	 Catch:{ Exception -> 0x21ab, all -> 0x218f }
                r8 = 0;	 Catch:{ Exception -> 0x21ab, all -> 0x218f }
                r7[r8] = r6;	 Catch:{ Exception -> 0x21ab, all -> 0x218f }
                r3 = java.lang.String.format(r3, r4, r7);	 Catch:{ Exception -> 0x21ab, all -> 0x218f }
                r4 = new java.lang.Object[r8];	 Catch:{ Exception -> 0x21ab, all -> 0x218f }
                r2 = r2.queryFinalized(r3, r4);	 Catch:{ Exception -> 0x21ab, all -> 0x218f }
                r3 = r2.next();	 Catch:{ Exception -> 0x21ab, all -> 0x218f }
                if (r3 == 0) goto L_0x1b13;
                r3 = 0;
                r4 = r2.intValue(r3);	 Catch:{ Exception -> 0x1af7, all -> 0x1adb }
                r8 = r4;
                goto L_0x1b15;
            L_0x1adb:
                r0 = move-exception;
                r2 = r0;
                r67 = r5;
                r27 = r9;
                r51 = r10;
                r65 = r11;
                r59 = r22;
                r47 = r23;
                r53 = r31;
                r22 = r38;
                r26 = r77;
                r28 = r80;
                r50 = r82;
                r48 = r83;
                goto L_0x242b;
            L_0x1af7:
                r0 = move-exception;
                r2 = r0;
                r67 = r5;
                r27 = r9;
                r51 = r10;
                r65 = r11;
                r59 = r22;
                r47 = r23;
                r53 = r31;
                r95 = r38;
                r26 = r77;
                r28 = r80;
                r50 = r82;
                r48 = r83;
                goto L_0x23dc;
                r8 = r25;
                r2.dispose();	 Catch:{ Exception -> 0x2182, all -> 0x2177 }
                r3 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x2182, all -> 0x2177 }
                r3 = r3.database;	 Catch:{ Exception -> 0x2182, all -> 0x2177 }
                r4 = java.util.Locale.US;	 Catch:{ Exception -> 0x2182, all -> 0x2177 }
                r6 = "SELECT * FROM (SELECT m.read_state, m.data, m.send_state, m.mid, m.date, r.random_id, m.replydata, m.media, m.ttl, m.mention FROM messages as m LEFT JOIN randoms as r ON r.mid = m.mid WHERE m.uid = %d AND m.mid <= %d ORDER BY m.mid DESC LIMIT %d) UNION SELECT * FROM (SELECT m.read_state, m.data, m.send_state, m.mid, m.date, r.random_id, m.replydata, m.media, m.ttl, m.mention FROM messages as m LEFT JOIN randoms as r ON r.mid = m.mid WHERE m.uid = %d AND m.mid > %d ORDER BY m.mid ASC LIMIT %d)";	 Catch:{ Exception -> 0x2182, all -> 0x2177 }
                r7 = 6;	 Catch:{ Exception -> 0x2182, all -> 0x2177 }
                r13 = new java.lang.Object[r7];	 Catch:{ Exception -> 0x2182, all -> 0x2177 }
                r14 = r5;	 Catch:{ Exception -> 0x2182, all -> 0x2177 }
                r7 = java.lang.Long.valueOf(r14);	 Catch:{ Exception -> 0x2182, all -> 0x2177 }
                r14 = 0;	 Catch:{ Exception -> 0x2182, all -> 0x2177 }
                r13[r14] = r7;	 Catch:{ Exception -> 0x2182, all -> 0x2177 }
                r7 = java.lang.Long.valueOf(r11);	 Catch:{ Exception -> 0x2182, all -> 0x2177 }
                r14 = 1;	 Catch:{ Exception -> 0x2182, all -> 0x2177 }
                r13[r14] = r7;	 Catch:{ Exception -> 0x2182, all -> 0x2177 }
                r7 = r5 / 2;	 Catch:{ Exception -> 0x2182, all -> 0x2177 }
                r7 = java.lang.Integer.valueOf(r7);	 Catch:{ Exception -> 0x2182, all -> 0x2177 }
                r14 = 2;	 Catch:{ Exception -> 0x2182, all -> 0x2177 }
                r13[r14] = r7;	 Catch:{ Exception -> 0x2182, all -> 0x2177 }
                r14 = r5;	 Catch:{ Exception -> 0x2182, all -> 0x2177 }
                r7 = java.lang.Long.valueOf(r14);	 Catch:{ Exception -> 0x2182, all -> 0x2177 }
                r14 = 3;	 Catch:{ Exception -> 0x2182, all -> 0x2177 }
                r13[r14] = r7;	 Catch:{ Exception -> 0x2182, all -> 0x2177 }
                r7 = java.lang.Long.valueOf(r11);	 Catch:{ Exception -> 0x2182, all -> 0x2177 }
                r14 = 4;	 Catch:{ Exception -> 0x2182, all -> 0x2177 }
                r13[r14] = r7;	 Catch:{ Exception -> 0x2182, all -> 0x2177 }
                r7 = r5 / 2;	 Catch:{ Exception -> 0x2182, all -> 0x2177 }
                r7 = java.lang.Integer.valueOf(r7);	 Catch:{ Exception -> 0x2182, all -> 0x2177 }
                r14 = 5;	 Catch:{ Exception -> 0x2182, all -> 0x2177 }
                r13[r14] = r7;	 Catch:{ Exception -> 0x2182, all -> 0x2177 }
                r4 = java.lang.String.format(r4, r6, r13);	 Catch:{ Exception -> 0x2182, all -> 0x2177 }
                r6 = 0;	 Catch:{ Exception -> 0x2182, all -> 0x2177 }
                r7 = new java.lang.Object[r6];	 Catch:{ Exception -> 0x2182, all -> 0x2177 }
                r3 = r3.queryFinalized(r4, r7);	 Catch:{ Exception -> 0x2182, all -> 0x2177 }
                r2 = r3;
                r67 = r5;
                r25 = r8;
                r51 = r10;
                r65 = r11;
                r59 = r22;
                r3 = r23;
                r15 = r31;
                r28 = r80;
                r50 = r82;
                r48 = r83;
                r4 = -2147483648; // 0xffffffff80000000 float:-0.0 double:NaN;
                if (r2 == 0) goto L_0x1e5c;
                r5 = r2.next();	 Catch:{ Exception -> 0x1e4c, all -> 0x1e3c }
                if (r5 == 0) goto L_0x1e0c;	 Catch:{ Exception -> 0x1e4c, all -> 0x1e3c }
                r5 = 1;	 Catch:{ Exception -> 0x1e4c, all -> 0x1e3c }
                r6 = r2.byteBufferValue(r5);	 Catch:{ Exception -> 0x1e4c, all -> 0x1e3c }
                r5 = r6;	 Catch:{ Exception -> 0x1e4c, all -> 0x1e3c }
                if (r5 == 0) goto L_0x1de8;	 Catch:{ Exception -> 0x1e4c, all -> 0x1e3c }
                r6 = 0;	 Catch:{ Exception -> 0x1e4c, all -> 0x1e3c }
                r7 = r5.readInt32(r6);	 Catch:{ Exception -> 0x1e4c, all -> 0x1e3c }
                r7 = org.telegram.tgnet.TLRPC.Message.TLdeserialize(r5, r7, r6);	 Catch:{ Exception -> 0x1e4c, all -> 0x1e3c }
                r6 = r7;	 Catch:{ Exception -> 0x1e4c, all -> 0x1e3c }
                r7 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x1e4c, all -> 0x1e3c }
                r7 = r7.currentAccount;	 Catch:{ Exception -> 0x1e4c, all -> 0x1e3c }
                r7 = org.telegram.messenger.UserConfig.getInstance(r7);	 Catch:{ Exception -> 0x1e4c, all -> 0x1e3c }
                r7 = r7.clientUserId;	 Catch:{ Exception -> 0x1e4c, all -> 0x1e3c }
                r6.readAttachPath(r5, r7);	 Catch:{ Exception -> 0x1e4c, all -> 0x1e3c }
                r5.reuse();	 Catch:{ Exception -> 0x1e4c, all -> 0x1e3c }
                r7 = 0;	 Catch:{ Exception -> 0x1e4c, all -> 0x1e3c }
                r8 = r2.intValue(r7);	 Catch:{ Exception -> 0x1e4c, all -> 0x1e3c }
                org.telegram.messenger.MessageObject.setUnreadFlags(r6, r8);	 Catch:{ Exception -> 0x1e4c, all -> 0x1e3c }
                r7 = 3;	 Catch:{ Exception -> 0x1e4c, all -> 0x1e3c }
                r8 = r2.intValue(r7);	 Catch:{ Exception -> 0x1e4c, all -> 0x1e3c }
                r6.id = r8;	 Catch:{ Exception -> 0x1e4c, all -> 0x1e3c }
                r7 = 4;	 Catch:{ Exception -> 0x1e4c, all -> 0x1e3c }
                r8 = r2.intValue(r7);	 Catch:{ Exception -> 0x1e4c, all -> 0x1e3c }
                r6.date = r8;	 Catch:{ Exception -> 0x1e4c, all -> 0x1e3c }
                r7 = r5;	 Catch:{ Exception -> 0x1e4c, all -> 0x1e3c }
                r6.dialog_id = r7;	 Catch:{ Exception -> 0x1e4c, all -> 0x1e3c }
                r7 = r6.flags;	 Catch:{ Exception -> 0x1e4c, all -> 0x1e3c }
                r7 = r7 & 1024;
                if (r7 == 0) goto L_0x1be9;
                r7 = 7;
                r8 = r2.intValue(r7);	 Catch:{ Exception -> 0x1bdb, all -> 0x1bcd }
                r6.views = r8;	 Catch:{ Exception -> 0x1bdb, all -> 0x1bcd }
                goto L_0x1bea;	 Catch:{ Exception -> 0x1bdb, all -> 0x1bcd }
            L_0x1bcd:
                r0 = move-exception;	 Catch:{ Exception -> 0x1bdb, all -> 0x1bcd }
                r2 = r0;	 Catch:{ Exception -> 0x1bdb, all -> 0x1bcd }
                r47 = r3;	 Catch:{ Exception -> 0x1bdb, all -> 0x1bcd }
                r27 = r9;	 Catch:{ Exception -> 0x1bdb, all -> 0x1bcd }
                r53 = r15;	 Catch:{ Exception -> 0x1bdb, all -> 0x1bcd }
                r22 = r38;	 Catch:{ Exception -> 0x1bdb, all -> 0x1bcd }
                r26 = r77;	 Catch:{ Exception -> 0x1bdb, all -> 0x1bcd }
                goto L_0x242b;	 Catch:{ Exception -> 0x1bdb, all -> 0x1bcd }
            L_0x1bdb:
                r0 = move-exception;	 Catch:{ Exception -> 0x1bdb, all -> 0x1bcd }
                r2 = r0;	 Catch:{ Exception -> 0x1bdb, all -> 0x1bcd }
                r47 = r3;	 Catch:{ Exception -> 0x1bdb, all -> 0x1bcd }
                r27 = r9;	 Catch:{ Exception -> 0x1bdb, all -> 0x1bcd }
                r53 = r15;	 Catch:{ Exception -> 0x1bdb, all -> 0x1bcd }
                r95 = r38;	 Catch:{ Exception -> 0x1bdb, all -> 0x1bcd }
                r26 = r77;	 Catch:{ Exception -> 0x1bdb, all -> 0x1bcd }
                goto L_0x23dc;	 Catch:{ Exception -> 0x1bdb, all -> 0x1bcd }
                r7 = 7;	 Catch:{ Exception -> 0x1bdb, all -> 0x1bcd }
                if (r46 == 0) goto L_0x1bf9;	 Catch:{ Exception -> 0x1bdb, all -> 0x1bcd }
                r8 = r6.ttl;	 Catch:{ Exception -> 0x1bdb, all -> 0x1bcd }
                if (r8 != 0) goto L_0x1bf9;	 Catch:{ Exception -> 0x1bdb, all -> 0x1bcd }
                r8 = 8;	 Catch:{ Exception -> 0x1bdb, all -> 0x1bcd }
                r10 = r2.intValue(r8);	 Catch:{ Exception -> 0x1bdb, all -> 0x1bcd }
                r6.ttl = r10;	 Catch:{ Exception -> 0x1bdb, all -> 0x1bcd }
                goto L_0x1bfb;
                r8 = 8;
                r10 = 9;
                r10 = r2.intValue(r10);	 Catch:{ Exception -> 0x1e4c, all -> 0x1e3c }
                if (r10 == 0) goto L_0x1c06;
                r10 = 1;
                r6.mentioned = r10;	 Catch:{ Exception -> 0x1bdb, all -> 0x1bcd }
                r14 = r38;
                r10 = r14.messages;	 Catch:{ Exception -> 0x1dd8, all -> 0x1dc8 }
                r10.add(r6);	 Catch:{ Exception -> 0x1dd8, all -> 0x1dc8 }
                r11 = r39;	 Catch:{ Exception -> 0x1dd8, all -> 0x1dc8 }
                r10 = r49;	 Catch:{ Exception -> 0x1dd8, all -> 0x1dc8 }
                org.telegram.messenger.MessagesStorage.addUsersAndChatsFromMessage(r6, r10, r11);	 Catch:{ Exception -> 0x1dd8, all -> 0x1dc8 }
                r12 = r6.reply_to_msg_id;	 Catch:{ Exception -> 0x1dd8, all -> 0x1dc8 }
                if (r12 != 0) goto L_0x1c41;
                r12 = r6.reply_to_random_id;	 Catch:{ Exception -> 0x1c36, all -> 0x1c2b }
                r17 = 0;
                r19 = (r12 > r17 ? 1 : (r12 == r17 ? 0 : -1));
                if (r19 == 0) goto L_0x1c21;
                goto L_0x1c41;
                r86 = r9;
                r4 = r34;
                r13 = r35;
                r12 = r36;
                goto L_0x1d2d;
            L_0x1c2b:
                r0 = move-exception;
                r2 = r0;
                r47 = r3;
                r27 = r9;
                r22 = r14;
                r53 = r15;
                goto L_0x1bd7;
            L_0x1c36:
                r0 = move-exception;
                r2 = r0;
                r47 = r3;
                r27 = r9;
                r95 = r14;
                r53 = r15;
                goto L_0x1be5;
                r12 = 6;
                r13 = r2.isNull(r12);	 Catch:{ Exception -> 0x1dd8, all -> 0x1dc8 }
                if (r13 != 0) goto L_0x1c84;
                r13 = r2.byteBufferValue(r12);	 Catch:{ Exception -> 0x1c36, all -> 0x1c2b }
                r5 = r13;	 Catch:{ Exception -> 0x1c36, all -> 0x1c2b }
                if (r5 == 0) goto L_0x1c84;	 Catch:{ Exception -> 0x1c36, all -> 0x1c2b }
                r13 = 0;	 Catch:{ Exception -> 0x1c36, all -> 0x1c2b }
                r7 = r5.readInt32(r13);	 Catch:{ Exception -> 0x1c36, all -> 0x1c2b }
                r7 = org.telegram.tgnet.TLRPC.Message.TLdeserialize(r5, r7, r13);	 Catch:{ Exception -> 0x1c36, all -> 0x1c2b }
                r6.replyMessage = r7;	 Catch:{ Exception -> 0x1c36, all -> 0x1c2b }
                r7 = r6.replyMessage;	 Catch:{ Exception -> 0x1c36, all -> 0x1c2b }
                r13 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x1c36, all -> 0x1c2b }
                r13 = r13.currentAccount;	 Catch:{ Exception -> 0x1c36, all -> 0x1c2b }
                r13 = org.telegram.messenger.UserConfig.getInstance(r13);	 Catch:{ Exception -> 0x1c36, all -> 0x1c2b }
                r13 = r13.clientUserId;	 Catch:{ Exception -> 0x1c36, all -> 0x1c2b }
                r7.readAttachPath(r5, r13);	 Catch:{ Exception -> 0x1c36, all -> 0x1c2b }
                r5.reuse();	 Catch:{ Exception -> 0x1c36, all -> 0x1c2b }
                r7 = r6.replyMessage;	 Catch:{ Exception -> 0x1c36, all -> 0x1c2b }
                if (r7 == 0) goto L_0x1c84;	 Catch:{ Exception -> 0x1c36, all -> 0x1c2b }
                r7 = org.telegram.messenger.MessageObject.isMegagroup(r6);	 Catch:{ Exception -> 0x1c36, all -> 0x1c2b }
                if (r7 == 0) goto L_0x1c7f;	 Catch:{ Exception -> 0x1c36, all -> 0x1c2b }
                r7 = r6.replyMessage;	 Catch:{ Exception -> 0x1c36, all -> 0x1c2b }
                r13 = r7.flags;	 Catch:{ Exception -> 0x1c36, all -> 0x1c2b }
                r13 = r13 | r4;	 Catch:{ Exception -> 0x1c36, all -> 0x1c2b }
                r7.flags = r13;	 Catch:{ Exception -> 0x1c36, all -> 0x1c2b }
                r7 = r6.replyMessage;	 Catch:{ Exception -> 0x1c36, all -> 0x1c2b }
                org.telegram.messenger.MessagesStorage.addUsersAndChatsFromMessage(r7, r10, r11);	 Catch:{ Exception -> 0x1c36, all -> 0x1c2b }
                r7 = r6.replyMessage;	 Catch:{ Exception -> 0x1dd8, all -> 0x1dc8 }
                if (r7 != 0) goto L_0x1d25;
                r7 = r6.reply_to_msg_id;	 Catch:{ Exception -> 0x1d17, all -> 0x1d09 }
                if (r7 == 0) goto L_0x1cd3;	 Catch:{ Exception -> 0x1d17, all -> 0x1d09 }
                r7 = r6.reply_to_msg_id;	 Catch:{ Exception -> 0x1d17, all -> 0x1d09 }
                r86 = r9;
                r8 = (long) r7;
                r7 = r6.to_id;	 Catch:{ Exception -> 0x207f, all -> 0x2071 }
                r7 = r7.channel_id;	 Catch:{ Exception -> 0x207f, all -> 0x2071 }
                if (r7 == 0) goto L_0x1ca2;	 Catch:{ Exception -> 0x207f, all -> 0x2071 }
                r7 = r6.to_id;	 Catch:{ Exception -> 0x207f, all -> 0x2071 }
                r7 = r7.channel_id;	 Catch:{ Exception -> 0x207f, all -> 0x2071 }
                r12 = (long) r7;	 Catch:{ Exception -> 0x207f, all -> 0x2071 }
                r12 = r12 << r16;	 Catch:{ Exception -> 0x207f, all -> 0x2071 }
                r17 = r8 | r12;	 Catch:{ Exception -> 0x207f, all -> 0x2071 }
                r8 = r17;	 Catch:{ Exception -> 0x207f, all -> 0x2071 }
                r7 = java.lang.Long.valueOf(r8);	 Catch:{ Exception -> 0x207f, all -> 0x2071 }
                r12 = r36;	 Catch:{ Exception -> 0x207f, all -> 0x2071 }
                r7 = r12.contains(r7);	 Catch:{ Exception -> 0x207f, all -> 0x2071 }
                if (r7 != 0) goto L_0x1cb5;	 Catch:{ Exception -> 0x207f, all -> 0x2071 }
                r7 = java.lang.Long.valueOf(r8);	 Catch:{ Exception -> 0x207f, all -> 0x2071 }
                r12.add(r7);	 Catch:{ Exception -> 0x207f, all -> 0x2071 }
                r7 = r6.reply_to_msg_id;	 Catch:{ Exception -> 0x207f, all -> 0x2071 }
                r13 = r35;	 Catch:{ Exception -> 0x207f, all -> 0x2071 }
                r7 = r13.get(r7);	 Catch:{ Exception -> 0x207f, all -> 0x2071 }
                r7 = (java.util.ArrayList) r7;	 Catch:{ Exception -> 0x207f, all -> 0x2071 }
                if (r7 != 0) goto L_0x1ccc;	 Catch:{ Exception -> 0x207f, all -> 0x2071 }
                r4 = new java.util.ArrayList;	 Catch:{ Exception -> 0x207f, all -> 0x2071 }
                r4.<init>();	 Catch:{ Exception -> 0x207f, all -> 0x2071 }
                r7 = r4;	 Catch:{ Exception -> 0x207f, all -> 0x2071 }
                r4 = r6.reply_to_msg_id;	 Catch:{ Exception -> 0x207f, all -> 0x2071 }
                r13.put(r4, r7);	 Catch:{ Exception -> 0x207f, all -> 0x2071 }
                r7.add(r6);	 Catch:{ Exception -> 0x207f, all -> 0x2071 }
                r4 = r34;	 Catch:{ Exception -> 0x207f, all -> 0x2071 }
                goto L_0x1d2d;	 Catch:{ Exception -> 0x207f, all -> 0x2071 }
                r86 = r9;	 Catch:{ Exception -> 0x207f, all -> 0x2071 }
                r13 = r35;	 Catch:{ Exception -> 0x207f, all -> 0x2071 }
                r12 = r36;	 Catch:{ Exception -> 0x207f, all -> 0x2071 }
                r7 = r6.reply_to_random_id;	 Catch:{ Exception -> 0x207f, all -> 0x2071 }
                r4 = java.lang.Long.valueOf(r7);	 Catch:{ Exception -> 0x207f, all -> 0x2071 }
                r4 = r12.contains(r4);	 Catch:{ Exception -> 0x207f, all -> 0x2071 }
                if (r4 != 0) goto L_0x1cee;	 Catch:{ Exception -> 0x207f, all -> 0x2071 }
                r7 = r6.reply_to_random_id;	 Catch:{ Exception -> 0x207f, all -> 0x2071 }
                r4 = java.lang.Long.valueOf(r7);	 Catch:{ Exception -> 0x207f, all -> 0x2071 }
                r12.add(r4);	 Catch:{ Exception -> 0x207f, all -> 0x2071 }
                r7 = r6.reply_to_random_id;	 Catch:{ Exception -> 0x207f, all -> 0x2071 }
                r4 = r34;	 Catch:{ Exception -> 0x207f, all -> 0x2071 }
                r7 = r4.get(r7);	 Catch:{ Exception -> 0x207f, all -> 0x2071 }
                r7 = (java.util.ArrayList) r7;	 Catch:{ Exception -> 0x207f, all -> 0x2071 }
                if (r7 != 0) goto L_0x1d05;	 Catch:{ Exception -> 0x207f, all -> 0x2071 }
                r8 = new java.util.ArrayList;	 Catch:{ Exception -> 0x207f, all -> 0x2071 }
                r8.<init>();	 Catch:{ Exception -> 0x207f, all -> 0x2071 }
                r7 = r8;	 Catch:{ Exception -> 0x207f, all -> 0x2071 }
                r8 = r6.reply_to_random_id;	 Catch:{ Exception -> 0x207f, all -> 0x2071 }
                r4.put(r8, r7);	 Catch:{ Exception -> 0x207f, all -> 0x2071 }
                r7.add(r6);	 Catch:{ Exception -> 0x207f, all -> 0x2071 }
                goto L_0x1d2d;
            L_0x1d09:
                r0 = move-exception;
                r2 = r0;
                r47 = r3;
                r27 = r9;
                r22 = r14;
                r53 = r15;
                r26 = r77;
                goto L_0x242b;
            L_0x1d17:
                r0 = move-exception;
                r2 = r0;
                r47 = r3;
                r27 = r9;
                r95 = r14;
                r53 = r15;
                r26 = r77;
                goto L_0x23dc;
                r86 = r9;
                r4 = r34;
                r13 = r35;
                r12 = r36;
                r7 = 2;
                r8 = r2.intValue(r7);	 Catch:{ Exception -> 0x1db8, all -> 0x1da8 }
                r6.send_state = r8;	 Catch:{ Exception -> 0x1db8, all -> 0x1da8 }
                r7 = r6.id;	 Catch:{ Exception -> 0x1db8, all -> 0x1da8 }
                if (r7 <= 0) goto L_0x1d3f;
                r7 = r6.send_state;	 Catch:{ Exception -> 0x207f, all -> 0x2071 }
                if (r7 == 0) goto L_0x1d3f;	 Catch:{ Exception -> 0x207f, all -> 0x2071 }
                r7 = 0;	 Catch:{ Exception -> 0x207f, all -> 0x2071 }
                r6.send_state = r7;	 Catch:{ Exception -> 0x207f, all -> 0x2071 }
                if (r46 != 0) goto L_0x1d4f;	 Catch:{ Exception -> 0x207f, all -> 0x2071 }
                r7 = 5;	 Catch:{ Exception -> 0x207f, all -> 0x2071 }
                r8 = r2.isNull(r7);	 Catch:{ Exception -> 0x207f, all -> 0x2071 }
                if (r8 != 0) goto L_0x1d50;	 Catch:{ Exception -> 0x207f, all -> 0x2071 }
                r8 = r2.longValue(r7);	 Catch:{ Exception -> 0x207f, all -> 0x2071 }
                r6.random_id = r8;	 Catch:{ Exception -> 0x207f, all -> 0x2071 }
                goto L_0x1d50;
                r7 = 5;
                r8 = org.telegram.messenger.MessageObject.isSecretPhotoOrVideo(r6);	 Catch:{ Exception -> 0x1db8, all -> 0x1da8 }
                if (r8 == 0) goto L_0x1da3;
                r8 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x1d96, all -> 0x1da8 }
                r8 = r8.database;	 Catch:{ Exception -> 0x1d96, all -> 0x1da8 }
                r9 = java.util.Locale.US;	 Catch:{ Exception -> 0x1d96, all -> 0x1da8 }
                r7 = "SELECT date FROM enc_tasks_v2 WHERE mid = %d";	 Catch:{ Exception -> 0x1d96, all -> 0x1da8 }
                r88 = r3;
                r87 = r5;
                r5 = 1;
                r3 = new java.lang.Object[r5];	 Catch:{ Exception -> 0x1d91, all -> 0x1e20 }
                r5 = r6.id;	 Catch:{ Exception -> 0x1d91, all -> 0x1e20 }
                r5 = java.lang.Integer.valueOf(r5);	 Catch:{ Exception -> 0x1d91, all -> 0x1e20 }
                r89 = r4;
                r4 = 0;
                r3[r4] = r5;	 Catch:{ Exception -> 0x1d8e, all -> 0x1e20 }
                r3 = java.lang.String.format(r9, r7, r3);	 Catch:{ Exception -> 0x1d8e, all -> 0x1e20 }
                r5 = new java.lang.Object[r4];	 Catch:{ Exception -> 0x1d8e, all -> 0x1e20 }
                r3 = r8.queryFinalized(r3, r5);	 Catch:{ Exception -> 0x1d8e, all -> 0x1e20 }
                r4 = r3.next();	 Catch:{ Exception -> 0x1d8e, all -> 0x1e20 }
                if (r4 == 0) goto L_0x1d89;	 Catch:{ Exception -> 0x1d8e, all -> 0x1e20 }
                r4 = 0;	 Catch:{ Exception -> 0x1d8e, all -> 0x1e20 }
                r5 = r3.intValue(r4);	 Catch:{ Exception -> 0x1d8e, all -> 0x1e20 }
                r6.destroyTime = r5;	 Catch:{ Exception -> 0x1d8e, all -> 0x1e20 }
                r3.dispose();	 Catch:{ Exception -> 0x1d8e, all -> 0x1e20 }
                goto L_0x1df8;
            L_0x1d8e:
                r0 = move-exception;
                r3 = r0;
                goto L_0x1d9e;
            L_0x1d91:
                r0 = move-exception;
                r89 = r4;
                r3 = r0;
                goto L_0x1d9e;
            L_0x1d96:
                r0 = move-exception;
                r88 = r3;
                r89 = r4;
                r87 = r5;
                r3 = r0;
                org.telegram.messenger.FileLog.e(r3);	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                goto L_0x1df8;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r88 = r3;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r89 = r4;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                goto L_0x1df8;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
            L_0x1da8:
                r0 = move-exception;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r88 = r3;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r2 = r0;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r22 = r14;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r53 = r15;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r26 = r77;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r27 = r86;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r47 = r88;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                goto L_0x242b;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
            L_0x1db8:
                r0 = move-exception;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r88 = r3;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r2 = r0;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r95 = r14;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r53 = r15;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r26 = r77;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r27 = r86;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r47 = r88;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                goto L_0x23dc;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
            L_0x1dc8:
                r0 = move-exception;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r88 = r3;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r2 = r0;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r27 = r9;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r22 = r14;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r53 = r15;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r26 = r77;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r47 = r88;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                goto L_0x242b;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
            L_0x1dd8:
                r0 = move-exception;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r88 = r3;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r2 = r0;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r27 = r9;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r95 = r14;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r53 = r15;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r26 = r77;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r47 = r88;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                goto L_0x23dc;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r88 = r3;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r86 = r9;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r89 = r34;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r13 = r35;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r12 = r36;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r14 = r38;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r11 = r39;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r10 = r49;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r49 = r10;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r39 = r11;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r36 = r12;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r35 = r13;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r38 = r14;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r9 = r86;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r3 = r88;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r34 = r89;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r4 = -2147483648; // 0xffffffff80000000 float:-0.0 double:NaN;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                goto L_0x1b7b;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r88 = r3;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r86 = r9;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r89 = r34;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r13 = r35;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r12 = r36;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r14 = r38;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r11 = r39;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r10 = r49;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r2.dispose();	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                goto L_0x1e6c;
            L_0x1e20:
                r0 = move-exception;
                r2 = r0;
                r22 = r14;
                r53 = r15;
                r26 = r77;
                r27 = r86;
                r47 = r88;
                goto L_0x242b;
            L_0x1e2e:
                r0 = move-exception;
                r2 = r0;
                r95 = r14;
                r53 = r15;
                r26 = r77;
                r27 = r86;
                r47 = r88;
                goto L_0x23dc;
            L_0x1e3c:
                r0 = move-exception;
                r88 = r3;
                r2 = r0;
                r27 = r9;
                r53 = r15;
                r22 = r38;
                r26 = r77;
                r47 = r88;
                goto L_0x242b;
            L_0x1e4c:
                r0 = move-exception;
                r88 = r3;
                r2 = r0;
                r27 = r9;
                r53 = r15;
                r95 = r38;
                r26 = r77;
                r47 = r88;
                goto L_0x23dc;
                r88 = r3;
                r86 = r9;
                r89 = r34;
                r13 = r35;
                r12 = r36;
                r14 = r38;
                r11 = r39;
                r10 = r49;
                r3 = r14.messages;	 Catch:{ Exception -> 0x2165, all -> 0x2153 }
                r4 = new org.telegram.messenger.MessagesStorage$58$1;	 Catch:{ Exception -> 0x2165, all -> 0x2153 }
                r4.<init>();	 Catch:{ Exception -> 0x2165, all -> 0x2153 }
                java.util.Collections.sort(r3, r4);	 Catch:{ Exception -> 0x2165, all -> 0x2153 }
                if (r46 == 0) goto L_0x1eda;
                r3 = r7;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r4 = 3;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                if (r3 == r4) goto L_0x1e8b;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r3 = r7;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r4 = 4;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                if (r3 == r4) goto L_0x1e8b;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r3 = r7;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r4 = 2;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                if (r3 != r4) goto L_0x1ec2;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                if (r29 == 0) goto L_0x1ec2;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                if (r32 != 0) goto L_0x1ec2;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r3 = r14.messages;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r3 = r3.isEmpty();	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                if (r3 != 0) goto L_0x1ec2;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r3 = r14.messages;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r4 = r14.messages;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r4 = r4.size();	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r5 = 1;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r4 = r4 - r5;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r3 = r3.get(r4);	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r3 = (org.telegram.tgnet.TLRPC.Message) r3;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r3 = r3.id;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r4 = r14.messages;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r5 = 0;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r4 = r4.get(r5);	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r4 = (org.telegram.tgnet.TLRPC.Message) r4;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r4 = r4.id;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                if (r3 > r15) goto L_0x1eb4;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                if (r4 >= r15) goto L_0x1ec2;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r12.clear();	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r10.clear();	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r11.clear();	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r5 = r14.messages;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r5.clear();	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r3 = r7;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r4 = 4;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                if (r3 == r4) goto L_0x1ecc;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r3 = r7;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r4 = 3;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                if (r3 != r4) goto L_0x1eda;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r3 = r14.messages;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r3 = r3.size();	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r4 = 1;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                if (r3 != r4) goto L_0x1eda;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r3 = r14.messages;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r3.clear();	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r3 = r12.isEmpty();	 Catch:{ Exception -> 0x2165, all -> 0x2153 }
                if (r3 != 0) goto L_0x202e;
                r3 = r13.size();	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                if (r3 <= 0) goto L_0x1f08;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r3 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r3 = r3.database;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r4 = java.util.Locale.US;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r5 = "SELECT data, mid, date FROM messages WHERE mid IN(%s)";	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r6 = 1;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r7 = new java.lang.Object[r6];	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r6 = ",";	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r6 = android.text.TextUtils.join(r6, r12);	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r8 = 0;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r7[r8] = r6;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r4 = java.lang.String.format(r4, r5, r7);	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r5 = new java.lang.Object[r8];	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r3 = r3.queryFinalized(r4, r5);	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r2 = r3;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                goto L_0x1f29;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r3 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r3 = r3.database;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r4 = java.util.Locale.US;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r5 = "SELECT m.data, m.mid, m.date, r.random_id FROM randoms as r INNER JOIN messages as m ON r.mid = m.mid WHERE r.random_id IN(%s)";	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r6 = 1;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r7 = new java.lang.Object[r6];	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r6 = ",";	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r6 = android.text.TextUtils.join(r6, r12);	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r8 = 0;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r7[r8] = r6;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r4 = java.lang.String.format(r4, r5, r7);	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r5 = new java.lang.Object[r8];	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r3 = r3.queryFinalized(r4, r5);	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r2 = r3;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r3 = r2.next();	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                if (r3 == 0) goto L_0x1ff2;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r3 = 0;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r4 = r2.byteBufferValue(r3);	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                if (r4 == 0) goto L_0x1fea;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r5 = r4.readInt32(r3);	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r5 = org.telegram.tgnet.TLRPC.Message.TLdeserialize(r4, r5, r3);	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r3 = r5;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r5 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r5 = r5.currentAccount;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r5 = org.telegram.messenger.UserConfig.getInstance(r5);	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r5 = r5.clientUserId;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r3.readAttachPath(r4, r5);	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r4.reuse();	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r5 = 1;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r6 = r2.intValue(r5);	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r3.id = r6;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r5 = 2;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r6 = r2.intValue(r5);	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r3.date = r6;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r6 = r5;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r3.dialog_id = r6;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                org.telegram.messenger.MessagesStorage.addUsersAndChatsFromMessage(r3, r10, r11);	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r6 = r13.size();	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                if (r6 <= 0) goto L_0x1f9f;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r6 = r3.id;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r6 = r13.get(r6);	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r6 = (java.util.ArrayList) r6;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                if (r6 == 0) goto L_0x1f99;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r7 = 0;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r8 = r6.size();	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                if (r7 >= r8) goto L_0x1f99;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r8 = r6.get(r7);	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r8 = (org.telegram.tgnet.TLRPC.Message) r8;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r8.replyMessage = r3;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r9 = org.telegram.messenger.MessageObject.isMegagroup(r8);	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                if (r9 == 0) goto L_0x1f95;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r9 = r8.replyMessage;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r5 = r9.flags;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r16 = -2147483648; // 0xffffffff80000000 float:-0.0 double:NaN;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r5 = r5 | r16;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r9.flags = r5;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r7 = r7 + 1;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r5 = 2;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                goto L_0x1f77;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r8 = r89;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r16 = -2147483648; // 0xffffffff80000000 float:-0.0 double:NaN;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                goto L_0x1fee;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r5 = 3;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r6 = r2.longValue(r5);	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r8 = r89;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r9 = r8.get(r6);	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r9 = (java.util.ArrayList) r9;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r8.remove(r6);	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                if (r9 == 0) goto L_0x1f9c;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r16 = 0;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r90 = r16;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r5 = r9.size();	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r91 = r4;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r4 = r90;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                if (r4 >= r5) goto L_0x1f9c;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r5 = r9.get(r4);	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r5 = (org.telegram.tgnet.TLRPC.Message) r5;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r5.replyMessage = r3;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r92 = r6;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r6 = r3.id;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r5.reply_to_msg_id = r6;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r6 = org.telegram.messenger.MessageObject.isMegagroup(r5);	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                if (r6 == 0) goto L_0x1fde;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r6 = r5.replyMessage;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r7 = r6.flags;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r16 = -2147483648; // 0xffffffff80000000 float:-0.0 double:NaN;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r7 = r7 | r16;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r6.flags = r7;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                goto L_0x1fe0;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r16 = -2147483648; // 0xffffffff80000000 float:-0.0 double:NaN;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r4 = r4 + 1;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r16 = r4;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r4 = r91;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r6 = r92;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r5 = 3;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                goto L_0x1fb3;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r8 = r89;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r16 = -2147483648; // 0xffffffff80000000 float:-0.0 double:NaN;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r89 = r8;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                goto L_0x1f29;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r8 = r89;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r2.dispose();	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r3 = r8.size();	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                if (r3 <= 0) goto L_0x202b;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r3 = 0;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r4 = r8.size();	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                if (r3 >= r4) goto L_0x202b;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r4 = r8.valueAt(r3);	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r4 = (java.util.ArrayList) r4;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r5 = 0;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r6 = r4.size();	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                if (r5 >= r6) goto L_0x2022;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r6 = r4.get(r5);	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r6 = (org.telegram.tgnet.TLRPC.Message) r6;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r94 = r8;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r7 = 0;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r6.reply_to_random_id = r7;	 Catch:{ Exception -> 0x1e2e, all -> 0x1e20 }
                r5 = r5 + 1;
                r8 = r94;
                goto L_0x200b;
                r94 = r8;
                r7 = 0;
                r3 = r3 + 1;
                r8 = r94;
                goto L_0x1ffe;
                r94 = r8;
                goto L_0x2030;
                r94 = r89;
                if (r88 == 0) goto L_0x20a8;
                r3 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x2098, all -> 0x2088 }
                r3 = r3.database;	 Catch:{ Exception -> 0x2098, all -> 0x2088 }
                r4 = java.util.Locale.US;	 Catch:{ Exception -> 0x2098, all -> 0x2088 }
                r5 = "SELECT COUNT(mid) FROM messages WHERE uid = %d AND mention = 1 AND read_state IN(0, 1)";	 Catch:{ Exception -> 0x2098, all -> 0x2088 }
                r6 = 1;	 Catch:{ Exception -> 0x2098, all -> 0x2088 }
                r6 = new java.lang.Object[r6];	 Catch:{ Exception -> 0x2098, all -> 0x2088 }
                r7 = r5;	 Catch:{ Exception -> 0x2098, all -> 0x2088 }
                r7 = java.lang.Long.valueOf(r7);	 Catch:{ Exception -> 0x2098, all -> 0x2088 }
                r8 = 0;	 Catch:{ Exception -> 0x2098, all -> 0x2088 }
                r6[r8] = r7;	 Catch:{ Exception -> 0x2098, all -> 0x2088 }
                r4 = java.lang.String.format(r4, r5, r6);	 Catch:{ Exception -> 0x2098, all -> 0x2088 }
                r5 = new java.lang.Object[r8];	 Catch:{ Exception -> 0x2098, all -> 0x2088 }
                r3 = r3.queryFinalized(r4, r5);	 Catch:{ Exception -> 0x2098, all -> 0x2088 }
                r2 = r3;	 Catch:{ Exception -> 0x2098, all -> 0x2088 }
                r3 = r2.next();	 Catch:{ Exception -> 0x2098, all -> 0x2088 }
                if (r3 == 0) goto L_0x2067;	 Catch:{ Exception -> 0x2098, all -> 0x2088 }
                r3 = 0;	 Catch:{ Exception -> 0x2098, all -> 0x2088 }
                r3 = r2.intValue(r3);	 Catch:{ Exception -> 0x2098, all -> 0x2088 }
                r4 = r88;
                if (r4 == r3) goto L_0x2065;
                r3 = r4 * -1;
                goto L_0x206b;
                r3 = r4;
                goto L_0x206b;
                r4 = r88;
                r3 = r4 * -1;
                r2.dispose();	 Catch:{ Exception -> 0x207f, all -> 0x2071 }
                r22 = r3;
                goto L_0x20ac;
            L_0x2071:
                r0 = move-exception;
                r2 = r0;
                r47 = r3;
                r22 = r14;
                r53 = r15;
                r26 = r77;
                r27 = r86;
                goto L_0x242b;
            L_0x207f:
                r0 = move-exception;
                r2 = r0;
                r47 = r3;
                r95 = r14;
                r53 = r15;
                goto L_0x20d6;
            L_0x2088:
                r0 = move-exception;
                r4 = r88;
                r2 = r0;
                r47 = r4;
                r22 = r14;
                r53 = r15;
                r26 = r77;
                r27 = r86;
                goto L_0x242b;
            L_0x2098:
                r0 = move-exception;
                r4 = r88;
                r2 = r0;
                r47 = r4;
                r95 = r14;
                r53 = r15;
                r26 = r77;
                r27 = r86;
                goto L_0x23dc;
                r4 = r88;
                r22 = r4;
                r3 = r10.isEmpty();	 Catch:{ Exception -> 0x2143, all -> 0x2133 }
                if (r3 != 0) goto L_0x20dc;
                r3 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x20ce, all -> 0x20c0 }
                r4 = ",";	 Catch:{ Exception -> 0x20ce, all -> 0x20c0 }
                r4 = android.text.TextUtils.join(r4, r10);	 Catch:{ Exception -> 0x20ce, all -> 0x20c0 }
                r5 = r14.users;	 Catch:{ Exception -> 0x20ce, all -> 0x20c0 }
                r3.getUsersInternal(r4, r5);	 Catch:{ Exception -> 0x20ce, all -> 0x20c0 }
                goto L_0x20dc;
            L_0x20c0:
                r0 = move-exception;
                r2 = r0;
                r53 = r15;
                r47 = r22;
                r26 = r77;
                r27 = r86;
                r22 = r14;
                goto L_0x242b;
            L_0x20ce:
                r0 = move-exception;
                r2 = r0;
                r95 = r14;
                r53 = r15;
                r47 = r22;
                r26 = r77;
                r27 = r86;
                goto L_0x23dc;
                r3 = r11.isEmpty();	 Catch:{ Exception -> 0x2143, all -> 0x2133 }
                if (r3 != 0) goto L_0x20ef;
                r3 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x20ce, all -> 0x20c0 }
                r4 = ",";	 Catch:{ Exception -> 0x20ce, all -> 0x20c0 }
                r4 = android.text.TextUtils.join(r4, r11);	 Catch:{ Exception -> 0x20ce, all -> 0x20c0 }
                r5 = r14.chats;	 Catch:{ Exception -> 0x20ce, all -> 0x20c0 }
                r3.getChatsInternal(r4, r5);	 Catch:{ Exception -> 0x20ce, all -> 0x20c0 }
                r2 = org.telegram.messenger.MessagesStorage.this;
                r2 = r2.currentAccount;
                r3 = org.telegram.messenger.MessagesController.getInstance(r2);
                r5 = r5;
                r9 = r9;
                r10 = 1;
                r11 = r10;
                r2 = r7;
                r13 = r4;
                r12 = r11;
                r26 = r77;
                r4 = r14;
                r7 = r67;
                r27 = r86;
                r8 = r33;
                r19 = r12;
                r12 = r50;
                r17 = r13;
                r13 = r25;
                r95 = r14;
                r14 = r59;
                r31 = r15;
                r15 = r51;
                r16 = r2;
                r18 = r28;
                r20 = r29;
                r21 = r22;
                r3.processLoadedMessages(r4, r5, r7, r8, r9, r10, r11, r12, r13, r14, r15, r16, r17, r18, r19, r20, r21);
                r47 = r22;
                r53 = r31;
                r22 = r95;
                goto L_0x2426;
            L_0x2133:
                r0 = move-exception;
                r31 = r15;
                r26 = r77;
                r27 = r86;
                r2 = r0;
                r47 = r22;
                r53 = r31;
                r22 = r14;
                goto L_0x242b;
            L_0x2143:
                r0 = move-exception;
                r95 = r14;
                r31 = r15;
                r26 = r77;
                r27 = r86;
                r2 = r0;
                r47 = r22;
                r53 = r31;
                goto L_0x23dc;
            L_0x2153:
                r0 = move-exception;
                r31 = r15;
                r26 = r77;
                r27 = r86;
                r4 = r88;
                r2 = r0;
                r47 = r4;
                r22 = r14;
                r53 = r31;
                goto L_0x242b;
            L_0x2165:
                r0 = move-exception;
                r95 = r14;
                r31 = r15;
                r26 = r77;
                r27 = r86;
                r4 = r88;
                r2 = r0;
                r47 = r4;
                r53 = r31;
                goto L_0x23dc;
            L_0x2177:
                r0 = move-exception;
                r27 = r9;
                r26 = r77;
                r2 = r0;
                r67 = r5;
                r25 = r8;
                goto L_0x2197;
            L_0x2182:
                r0 = move-exception;
                r27 = r9;
                r95 = r38;
                r26 = r77;
                r2 = r0;
                r67 = r5;
                r25 = r8;
                goto L_0x21b5;
            L_0x218f:
                r0 = move-exception;
                r27 = r9;
                r26 = r77;
                r2 = r0;
                r67 = r5;
                r51 = r10;
                r65 = r11;
                r59 = r22;
                r47 = r23;
                r53 = r31;
                r22 = r38;
                r28 = r80;
                r50 = r82;
                r48 = r83;
                goto L_0x242b;
            L_0x21ab:
                r0 = move-exception;
                r27 = r9;
                r95 = r38;
                r26 = r77;
                r2 = r0;
                r67 = r5;
                r51 = r10;
                r65 = r11;
                r59 = r22;
                r47 = r23;
                r53 = r31;
                r28 = r80;
                r50 = r82;
                r48 = r83;
                goto L_0x23dc;
            L_0x21c7:
                r0 = move-exception;
                r82 = r7;
                r83 = r48;
                r27 = r52;
                r26 = r77;
                r2 = r0;
                r67 = r5;
                r51 = r10;
                r65 = r11;
                r59 = r22;
                r47 = r23;
                r53 = r31;
                r22 = r38;
                r28 = r80;
                r50 = r82;
                goto L_0x242b;
            L_0x21e5:
                r0 = move-exception;
                r82 = r7;
                r95 = r38;
                r83 = r48;
                r27 = r52;
                r26 = r77;
                r2 = r0;
                r67 = r5;
                r51 = r10;
                r65 = r11;
                r59 = r22;
                r47 = r23;
                r53 = r31;
                r28 = r80;
                r50 = r82;
                goto L_0x23dc;
            L_0x2203:
                r0 = move-exception;
                r80 = r2;
                r83 = r48;
                r27 = r52;
                r26 = r77;
                r2 = r0;
                r67 = r5;
                r65 = r11;
                r59 = r22;
                r47 = r23;
                r50 = r24;
                r51 = r30;
                r53 = r31;
                r22 = r38;
                r28 = r80;
                goto L_0x242b;
            L_0x2221:
                r0 = move-exception;
                r80 = r2;
                r95 = r38;
                r83 = r48;
                r27 = r52;
                r26 = r77;
                r2 = r0;
                r67 = r5;
                r65 = r11;
                r59 = r22;
                r47 = r23;
                r50 = r24;
                r51 = r30;
                r53 = r31;
                r28 = r80;
                goto L_0x23dc;
            L_0x223f:
                r0 = move-exception;
                r26 = r4;
                r83 = r6;
                r27 = r8;
                r67 = r5;
                r65 = r11;
                r59 = r22;
                r47 = r23;
                r50 = r24;
                r51 = r30;
                r53 = r31;
                r48 = r83;
                r22 = r2;
                r2 = r0;
                goto L_0x242b;
            L_0x225b:
                r0 = move-exception;
                r95 = r2;
                r26 = r4;
                r83 = r6;
                r27 = r8;
                r2 = r0;
                r67 = r5;
                r65 = r11;
                r59 = r22;
                r47 = r23;
                r50 = r24;
                r51 = r30;
                r53 = r31;
                r48 = r83;
                goto L_0x23dc;
            L_0x2277:
                r0 = move-exception;
                r26 = r4;
                r83 = r6;
                r27 = r8;
                r32 = r14;
                r33 = r15;
                r67 = r5;
                r65 = r11;
                r59 = r22;
                r47 = r23;
                r50 = r24;
                r51 = r30;
                r53 = r31;
                r48 = r83;
                r22 = r2;
                r2 = r0;
                goto L_0x242b;
            L_0x2297:
                r0 = move-exception;
                r95 = r2;
                r26 = r4;
                r83 = r6;
                r27 = r8;
                r32 = r14;
                r33 = r15;
                r2 = r0;
                r67 = r5;
                r65 = r11;
                r59 = r22;
                r47 = r23;
                r50 = r24;
                r51 = r30;
                r53 = r31;
                r48 = r83;
                goto L_0x23dc;
            L_0x22b7:
                r0 = move-exception;
                r26 = r4;
                r83 = r6;
                r27 = r8;
                r31 = r13;
                r32 = r14;
                r33 = r15;
                r67 = r5;
                r65 = r11;
                r59 = r22;
                r47 = r23;
                r50 = r24;
                r51 = r30;
                r53 = r31;
                r48 = r83;
                r22 = r2;
                r2 = r0;
                goto L_0x242b;
            L_0x22d9:
                r0 = move-exception;
                r95 = r2;
                r26 = r4;
                r83 = r6;
                r27 = r8;
                r31 = r13;
                r32 = r14;
                r33 = r15;
                r2 = r0;
                r67 = r5;
                r65 = r11;
                r59 = r22;
                r47 = r23;
                r50 = r24;
                r51 = r30;
                r53 = r31;
                r48 = r83;
                goto L_0x23dc;
            L_0x22fb:
                r0 = move-exception;
                r26 = r4;
                r83 = r6;
                r27 = r8;
                r30 = r10;
                r31 = r13;
                r32 = r14;
                r33 = r15;
                r67 = r5;
                r65 = r11;
                r59 = r22;
                r47 = r23;
                r50 = r24;
                r51 = r30;
                r53 = r31;
                r48 = r83;
                r22 = r2;
                r2 = r0;
                goto L_0x242b;
            L_0x231f:
                r0 = move-exception;
                r95 = r2;
                r26 = r4;
                r83 = r6;
                r27 = r8;
                r30 = r10;
                r31 = r13;
                r32 = r14;
                r33 = r15;
                r2 = r0;
                r67 = r5;
                r65 = r11;
                r59 = r22;
                r47 = r23;
                r50 = r24;
                r51 = r30;
                r53 = r31;
                r48 = r83;
                goto L_0x23dc;
            L_0x2343:
                r0 = move-exception;
                r26 = r4;
                r83 = r6;
                r27 = r8;
                r29 = r9;
                r30 = r10;
                r31 = r13;
                r32 = r14;
                r33 = r15;
                r67 = r5;
                r65 = r11;
                r59 = r22;
                r47 = r23;
                r50 = r24;
                r51 = r30;
                r53 = r31;
                r48 = r83;
                r22 = r2;
                r2 = r0;
                goto L_0x242b;
            L_0x2369:
                r0 = move-exception;
                r95 = r2;
                r26 = r4;
                r83 = r6;
                r27 = r8;
                r29 = r9;
                r30 = r10;
                r31 = r13;
                r32 = r14;
                r33 = r15;
                r2 = r0;
                r67 = r5;
                r65 = r11;
                r59 = r22;
                r47 = r23;
                r50 = r24;
                r51 = r30;
                r53 = r31;
                r48 = r83;
                goto L_0x23dc;
            L_0x238e:
                r0 = move-exception;
                r28 = r3;
                r26 = r4;
                r83 = r6;
                r27 = r8;
                r29 = r9;
                r30 = r10;
                r31 = r13;
                r32 = r14;
                r33 = r15;
                r67 = r5;
                r65 = r11;
                r59 = r22;
                r47 = r23;
                r50 = r24;
                r51 = r30;
                r53 = r31;
                r48 = r83;
                r22 = r2;
                r2 = r0;
                goto L_0x242b;
            L_0x23b6:
                r0 = move-exception;
                r95 = r2;
                r28 = r3;
                r26 = r4;
                r83 = r6;
                r27 = r8;
                r29 = r9;
                r30 = r10;
                r31 = r13;
                r32 = r14;
                r33 = r15;
                r2 = r0;
                r67 = r5;
                r65 = r11;
                r59 = r22;
                r47 = r23;
                r50 = r24;
                r51 = r30;
                r53 = r31;
                r48 = r83;
            L_0x23dc:
                r15 = r95;
                r3 = r15.messages;	 Catch:{ all -> 0x2427 }
                r3.clear();	 Catch:{ all -> 0x2427 }
                r3 = r15.chats;	 Catch:{ all -> 0x2427 }
                r3.clear();	 Catch:{ all -> 0x2427 }
                r3 = r15.users;	 Catch:{ all -> 0x2427 }
                r3.clear();	 Catch:{ all -> 0x2427 }
                org.telegram.messenger.FileLog.e(r2);	 Catch:{ all -> 0x2427 }
                r2 = org.telegram.messenger.MessagesStorage.this;
                r2 = r2.currentAccount;
                r3 = org.telegram.messenger.MessagesController.getInstance(r2);
                r5 = r5;
                r9 = r9;
                r10 = 1;
                r11 = r10;
                r2 = r7;
                r14 = r4;
                r13 = r11;
                r4 = r15;
                r7 = r67;
                r8 = r33;
                r12 = r50;
                r19 = r13;
                r13 = r25;
                r17 = r14;
                r14 = r59;
                r22 = r15;
                r15 = r51;
                r16 = r2;
                r18 = r28;
                r20 = r29;
                r21 = r47;
                r3.processLoadedMessages(r4, r5, r7, r8, r9, r10, r11, r12, r13, r14, r15, r16, r17, r18, r19, r20, r21);
                return;
            L_0x2427:
                r0 = move-exception;
                r22 = r15;
                r2 = r0;
            L_0x242b:
                r3 = org.telegram.messenger.MessagesStorage.this;
                r3 = r3.currentAccount;
                r3 = org.telegram.messenger.MessagesController.getInstance(r3);
                r5 = r5;
                r9 = r9;
                r10 = 1;
                r11 = r10;
                r15 = r7;
                r14 = r4;
                r13 = r11;
                r4 = r22;
                r7 = r67;
                r8 = r33;
                r12 = r50;
                r19 = r13;
                r13 = r25;
                r17 = r14;
                r14 = r59;
                r16 = r15;
                r15 = r51;
                r18 = r28;
                r20 = r29;
                r21 = r47;
                r3.processLoadedMessages(r4, r5, r7, r8, r9, r10, r11, r12, r13, r14, r15, r16, r17, r18, r19, r20, r21);
                throw r2;
                */
                throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.MessagesStorage.58.run():void");
            }
        });
    }

    public void clearSentMedia() {
        this.storageQueue.postRunnable(new Runnable() {
            public void run() {
                try {
                    MessagesStorage.this.database.executeFast("DELETE FROM sent_files_v2 WHERE 1").stepThis().dispose();
                } catch (Throwable e) {
                    FileLog.e(e);
                }
            }
        });
    }

    public TLObject getSentFile(String path, int type) {
        TLObject tLObject = null;
        if (path != null) {
            if (!path.toLowerCase().endsWith("attheme")) {
                CountDownLatch countDownLatch = new CountDownLatch(1);
                ArrayList<TLObject> result = new ArrayList();
                final String str = path;
                final int i = type;
                final ArrayList<TLObject> arrayList = result;
                final CountDownLatch countDownLatch2 = countDownLatch;
                this.storageQueue.postRunnable(new Runnable() {
                    public void run() {
                        try {
                            if (Utilities.MD5(str) != null) {
                                SQLiteCursor cursor = MessagesStorage.this.database.queryFinalized(String.format(Locale.US, "SELECT data FROM sent_files_v2 WHERE uid = '%s' AND type = %d", new Object[]{id, Integer.valueOf(i)}), new Object[0]);
                                if (cursor.next()) {
                                    NativeByteBuffer data = cursor.byteBufferValue(0);
                                    if (data != null) {
                                        TLObject file = MessageMedia.TLdeserialize(data, data.readInt32(false), false);
                                        data.reuse();
                                        if (file instanceof TL_messageMediaDocument) {
                                            arrayList.add(((TL_messageMediaDocument) file).document);
                                        } else if (file instanceof TL_messageMediaPhoto) {
                                            arrayList.add(((TL_messageMediaPhoto) file).photo);
                                        }
                                    }
                                }
                                cursor.dispose();
                            }
                        } catch (Throwable e) {
                            FileLog.e(e);
                        } catch (Throwable th) {
                            countDownLatch2.countDown();
                        }
                        countDownLatch2.countDown();
                    }
                });
                try {
                    countDownLatch.await();
                } catch (Throwable e) {
                    FileLog.e(e);
                }
                if (!result.isEmpty()) {
                    tLObject = (TLObject) result.get(0);
                }
                return tLObject;
            }
        }
        return null;
    }

    public void putSentFile(final String path, final TLObject file, final int type) {
        if (path != null) {
            if (file != null) {
                this.storageQueue.postRunnable(new Runnable() {
                    /* JADX WARNING: inconsistent code. */
                    /* Code decompiled incorrectly, please refer to instructions dump. */
                    public void run() {
                        /*
                        r6 = this;
                        r0 = 0;
                        r1 = r3;	 Catch:{ Exception -> 0x0079 }
                        r1 = org.telegram.messenger.Utilities.MD5(r1);	 Catch:{ Exception -> 0x0079 }
                        if (r1 == 0) goto L_0x0071;
                    L_0x0009:
                        r2 = 0;
                        r3 = r4;	 Catch:{ Exception -> 0x0079 }
                        r3 = r3 instanceof org.telegram.tgnet.TLRPC.Photo;	 Catch:{ Exception -> 0x0079 }
                        r4 = 1;
                        if (r3 == 0) goto L_0x0023;
                    L_0x0011:
                        r3 = new org.telegram.tgnet.TLRPC$TL_messageMediaPhoto;	 Catch:{ Exception -> 0x0079 }
                        r3.<init>();	 Catch:{ Exception -> 0x0079 }
                        r2 = r3;
                        r3 = r4;	 Catch:{ Exception -> 0x0079 }
                        r3 = (org.telegram.tgnet.TLRPC.Photo) r3;	 Catch:{ Exception -> 0x0079 }
                        r2.photo = r3;	 Catch:{ Exception -> 0x0079 }
                        r3 = r2.flags;	 Catch:{ Exception -> 0x0079 }
                        r3 = r3 | r4;
                        r2.flags = r3;	 Catch:{ Exception -> 0x0079 }
                        goto L_0x003a;
                    L_0x0023:
                        r3 = r4;	 Catch:{ Exception -> 0x0079 }
                        r3 = r3 instanceof org.telegram.tgnet.TLRPC.Document;	 Catch:{ Exception -> 0x0079 }
                        if (r3 == 0) goto L_0x003a;
                    L_0x0029:
                        r3 = new org.telegram.tgnet.TLRPC$TL_messageMediaDocument;	 Catch:{ Exception -> 0x0079 }
                        r3.<init>();	 Catch:{ Exception -> 0x0079 }
                        r2 = r3;
                        r3 = r4;	 Catch:{ Exception -> 0x0079 }
                        r3 = (org.telegram.tgnet.TLRPC.Document) r3;	 Catch:{ Exception -> 0x0079 }
                        r2.document = r3;	 Catch:{ Exception -> 0x0079 }
                        r3 = r2.flags;	 Catch:{ Exception -> 0x0079 }
                        r3 = r3 | r4;
                        r2.flags = r3;	 Catch:{ Exception -> 0x0079 }
                    L_0x003a:
                        if (r2 != 0) goto L_0x0042;
                    L_0x003c:
                        if (r0 == 0) goto L_0x0041;
                    L_0x003e:
                        r0.dispose();
                    L_0x0041:
                        return;
                    L_0x0042:
                        r3 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x0079 }
                        r3 = r3.database;	 Catch:{ Exception -> 0x0079 }
                        r5 = "REPLACE INTO sent_files_v2 VALUES(?, ?, ?)";
                        r3 = r3.executeFast(r5);	 Catch:{ Exception -> 0x0079 }
                        r0 = r3;
                        r0.requery();	 Catch:{ Exception -> 0x0079 }
                        r3 = new org.telegram.tgnet.NativeByteBuffer;	 Catch:{ Exception -> 0x0079 }
                        r5 = r2.getObjectSize();	 Catch:{ Exception -> 0x0079 }
                        r3.<init>(r5);	 Catch:{ Exception -> 0x0079 }
                        r2.serializeToStream(r3);	 Catch:{ Exception -> 0x0079 }
                        r0.bindString(r4, r1);	 Catch:{ Exception -> 0x0079 }
                        r4 = 2;
                        r5 = r5;	 Catch:{ Exception -> 0x0079 }
                        r0.bindInteger(r4, r5);	 Catch:{ Exception -> 0x0079 }
                        r4 = 3;
                        r0.bindByteBuffer(r4, r3);	 Catch:{ Exception -> 0x0079 }
                        r0.step();	 Catch:{ Exception -> 0x0079 }
                        r3.reuse();	 Catch:{ Exception -> 0x0079 }
                    L_0x0071:
                        if (r0 == 0) goto L_0x0080;
                    L_0x0073:
                        r0.dispose();
                        goto L_0x0080;
                    L_0x0077:
                        r1 = move-exception;
                        goto L_0x0081;
                    L_0x0079:
                        r1 = move-exception;
                        org.telegram.messenger.FileLog.e(r1);	 Catch:{ all -> 0x0077 }
                        if (r0 == 0) goto L_0x0080;
                    L_0x007f:
                        goto L_0x0073;
                    L_0x0080:
                        return;
                    L_0x0081:
                        if (r0 == 0) goto L_0x0086;
                    L_0x0083:
                        r0.dispose();
                    L_0x0086:
                        throw r1;
                        */
                        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.MessagesStorage.61.run():void");
                    }
                });
            }
        }
    }

    public void updateEncryptedChatSeq(final EncryptedChat chat, final boolean cleanup) {
        if (chat != null) {
            this.storageQueue.postRunnable(new Runnable() {
                public void run() {
                    SQLitePreparedStatement state = null;
                    try {
                        state = MessagesStorage.this.database.executeFast("UPDATE enc_chats SET seq_in = ?, seq_out = ?, use_count = ?, in_seq_no = ?, mtproto_seq = ? WHERE uid = ?");
                        state.bindInteger(1, chat.seq_in);
                        state.bindInteger(2, chat.seq_out);
                        state.bindInteger(3, (chat.key_use_count_in << 16) | chat.key_use_count_out);
                        state.bindInteger(4, chat.in_seq_no);
                        state.bindInteger(5, chat.mtproto_seq);
                        state.bindInteger(6, chat.id);
                        state.step();
                        if (cleanup) {
                            long did = ((long) chat.id) << 32;
                            MessagesStorage.this.database.executeFast(String.format(Locale.US, "DELETE FROM messages WHERE mid IN (SELECT m.mid FROM messages as m LEFT JOIN messages_seq as s ON m.mid = s.mid WHERE m.uid = %d AND m.date = 0 AND m.mid < 0 AND s.seq_out <= %d)", new Object[]{Long.valueOf(did), Integer.valueOf(chat.in_seq_no)})).stepThis().dispose();
                        }
                        if (state == null) {
                            return;
                        }
                    } catch (Throwable e) {
                        FileLog.e(e);
                        if (state == null) {
                            return;
                        }
                    } catch (Throwable th) {
                        if (state != null) {
                            state.dispose();
                        }
                    }
                    state.dispose();
                }
            });
        }
    }

    public void updateEncryptedChatTTL(final EncryptedChat chat) {
        if (chat != null) {
            this.storageQueue.postRunnable(new Runnable() {
                public void run() {
                    SQLitePreparedStatement state = null;
                    try {
                        state = MessagesStorage.this.database.executeFast("UPDATE enc_chats SET ttl = ? WHERE uid = ?");
                        state.bindInteger(1, chat.ttl);
                        state.bindInteger(2, chat.id);
                        state.step();
                        if (state == null) {
                            return;
                        }
                    } catch (Throwable e) {
                        FileLog.e(e);
                        if (state == null) {
                            return;
                        }
                    } catch (Throwable th) {
                        if (state != null) {
                            state.dispose();
                        }
                    }
                    state.dispose();
                }
            });
        }
    }

    public void updateEncryptedChatLayer(final EncryptedChat chat) {
        if (chat != null) {
            this.storageQueue.postRunnable(new Runnable() {
                public void run() {
                    SQLitePreparedStatement state = null;
                    try {
                        state = MessagesStorage.this.database.executeFast("UPDATE enc_chats SET layer = ? WHERE uid = ?");
                        state.bindInteger(1, chat.layer);
                        state.bindInteger(2, chat.id);
                        state.step();
                        if (state == null) {
                            return;
                        }
                    } catch (Throwable e) {
                        FileLog.e(e);
                        if (state == null) {
                            return;
                        }
                    } catch (Throwable th) {
                        if (state != null) {
                            state.dispose();
                        }
                    }
                    state.dispose();
                }
            });
        }
    }

    public void updateEncryptedChat(final EncryptedChat chat) {
        if (chat != null) {
            this.storageQueue.postRunnable(new Runnable() {
                public void run() {
                    SQLitePreparedStatement state = null;
                    try {
                        if ((chat.key_hash == null || chat.key_hash.length < 16) && chat.auth_key != null) {
                            chat.key_hash = AndroidUtilities.calcAuthKeyHash(chat.auth_key);
                        }
                        state = MessagesStorage.this.database.executeFast("UPDATE enc_chats SET data = ?, g = ?, authkey = ?, ttl = ?, layer = ?, seq_in = ?, seq_out = ?, use_count = ?, exchange_id = ?, key_date = ?, fprint = ?, fauthkey = ?, khash = ?, in_seq_no = ?, admin_id = ?, mtproto_seq = ? WHERE uid = ?");
                        NativeByteBuffer data = new NativeByteBuffer(chat.getObjectSize());
                        NativeByteBuffer data2 = new NativeByteBuffer(chat.a_or_b != null ? chat.a_or_b.length : 1);
                        NativeByteBuffer data3 = new NativeByteBuffer(chat.auth_key != null ? chat.auth_key.length : 1);
                        NativeByteBuffer data4 = new NativeByteBuffer(chat.future_auth_key != null ? chat.future_auth_key.length : 1);
                        NativeByteBuffer data5 = new NativeByteBuffer(chat.key_hash != null ? chat.key_hash.length : 1);
                        chat.serializeToStream(data);
                        state.bindByteBuffer(1, data);
                        if (chat.a_or_b != null) {
                            data2.writeBytes(chat.a_or_b);
                        }
                        if (chat.auth_key != null) {
                            data3.writeBytes(chat.auth_key);
                        }
                        if (chat.future_auth_key != null) {
                            data4.writeBytes(chat.future_auth_key);
                        }
                        if (chat.key_hash != null) {
                            data5.writeBytes(chat.key_hash);
                        }
                        state.bindByteBuffer(2, data2);
                        state.bindByteBuffer(3, data3);
                        state.bindInteger(4, chat.ttl);
                        state.bindInteger(5, chat.layer);
                        state.bindInteger(6, chat.seq_in);
                        state.bindInteger(7, chat.seq_out);
                        state.bindInteger(8, (chat.key_use_count_in << 16) | chat.key_use_count_out);
                        state.bindLong(9, chat.exchange_id);
                        state.bindInteger(10, chat.key_create_date);
                        state.bindLong(11, chat.future_key_fingerprint);
                        state.bindByteBuffer(12, data4);
                        state.bindByteBuffer(13, data5);
                        state.bindInteger(14, chat.in_seq_no);
                        state.bindInteger(15, chat.admin_id);
                        state.bindInteger(16, chat.mtproto_seq);
                        state.bindInteger(17, chat.id);
                        state.step();
                        data.reuse();
                        data2.reuse();
                        data3.reuse();
                        data4.reuse();
                        data5.reuse();
                        if (state == null) {
                            return;
                        }
                    } catch (Throwable e) {
                        FileLog.e(e);
                        if (state == null) {
                            return;
                        }
                    } catch (Throwable th) {
                        if (state != null) {
                            state.dispose();
                        }
                    }
                    state.dispose();
                }
            });
        }
    }

    public boolean isDialogHasMessages(long did) {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        boolean[] result = new boolean[1];
        final long j = did;
        final boolean[] zArr = result;
        final CountDownLatch countDownLatch2 = countDownLatch;
        this.storageQueue.postRunnable(new Runnable() {
            public void run() {
                try {
                    SQLiteCursor cursor = MessagesStorage.this.database.queryFinalized(String.format(Locale.US, "SELECT mid FROM messages WHERE uid = %d LIMIT 1", new Object[]{Long.valueOf(j)}), new Object[0]);
                    zArr[0] = cursor.next();
                    cursor.dispose();
                } catch (Throwable e) {
                    FileLog.e(e);
                } catch (Throwable th) {
                    countDownLatch2.countDown();
                }
                countDownLatch2.countDown();
            }
        });
        try {
            countDownLatch.await();
        } catch (Throwable e) {
            FileLog.e(e);
        }
        return result[0];
    }

    public boolean hasAuthMessage(final int date) {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        final boolean[] result = new boolean[1];
        this.storageQueue.postRunnable(new Runnable() {
            public void run() {
                try {
                    SQLiteCursor cursor = MessagesStorage.this.database.queryFinalized(String.format(Locale.US, "SELECT mid FROM messages WHERE uid = 777000 AND date = %d AND mid < 0 LIMIT 1", new Object[]{Integer.valueOf(date)}), new Object[0]);
                    result[0] = cursor.next();
                    cursor.dispose();
                } catch (Throwable e) {
                    FileLog.e(e);
                } catch (Throwable th) {
                    countDownLatch.countDown();
                }
                countDownLatch.countDown();
            }
        });
        try {
            countDownLatch.await();
        } catch (Throwable e) {
            FileLog.e(e);
        }
        return result[0];
    }

    public void getEncryptedChat(final int chat_id, final CountDownLatch countDownLatch, final ArrayList<TLObject> result) {
        if (countDownLatch != null) {
            if (result != null) {
                this.storageQueue.postRunnable(new Runnable() {
                    public void run() {
                        try {
                            ArrayList<Integer> usersToLoad = new ArrayList();
                            ArrayList<EncryptedChat> encryptedChats = new ArrayList();
                            MessagesStorage messagesStorage = MessagesStorage.this;
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append(TtmlNode.ANONYMOUS_REGION_ID);
                            stringBuilder.append(chat_id);
                            messagesStorage.getEncryptedChatsInternal(stringBuilder.toString(), encryptedChats, usersToLoad);
                            if (!(encryptedChats.isEmpty() || usersToLoad.isEmpty())) {
                                ArrayList<User> users = new ArrayList();
                                MessagesStorage.this.getUsersInternal(TextUtils.join(",", usersToLoad), users);
                                if (!users.isEmpty()) {
                                    result.add(encryptedChats.get(0));
                                    result.add(users.get(0));
                                }
                            }
                        } catch (Throwable e) {
                            FileLog.e(e);
                        } catch (Throwable th) {
                            countDownLatch.countDown();
                        }
                        countDownLatch.countDown();
                    }
                });
            }
        }
    }

    public void putEncryptedChat(final EncryptedChat chat, final User user, final TL_dialog dialog) {
        if (chat != null) {
            this.storageQueue.postRunnable(new Runnable() {
                public void run() {
                    try {
                        if ((chat.key_hash == null || chat.key_hash.length < 16) && chat.auth_key != null) {
                            chat.key_hash = AndroidUtilities.calcAuthKeyHash(chat.auth_key);
                        }
                        SQLitePreparedStatement state = MessagesStorage.this.database.executeFast("REPLACE INTO enc_chats VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
                        NativeByteBuffer data = new NativeByteBuffer(chat.getObjectSize());
                        NativeByteBuffer data2 = new NativeByteBuffer(chat.a_or_b != null ? chat.a_or_b.length : 1);
                        NativeByteBuffer data3 = new NativeByteBuffer(chat.auth_key != null ? chat.auth_key.length : 1);
                        NativeByteBuffer data4 = new NativeByteBuffer(chat.future_auth_key != null ? chat.future_auth_key.length : 1);
                        NativeByteBuffer data5 = new NativeByteBuffer(chat.key_hash != null ? chat.key_hash.length : 1);
                        chat.serializeToStream(data);
                        state.bindInteger(1, chat.id);
                        state.bindInteger(2, user.id);
                        state.bindString(3, MessagesStorage.this.formatUserSearchName(user));
                        state.bindByteBuffer(4, data);
                        if (chat.a_or_b != null) {
                            data2.writeBytes(chat.a_or_b);
                        }
                        if (chat.auth_key != null) {
                            data3.writeBytes(chat.auth_key);
                        }
                        if (chat.future_auth_key != null) {
                            data4.writeBytes(chat.future_auth_key);
                        }
                        if (chat.key_hash != null) {
                            data5.writeBytes(chat.key_hash);
                        }
                        state.bindByteBuffer(5, data2);
                        state.bindByteBuffer(6, data3);
                        state.bindInteger(7, chat.ttl);
                        state.bindInteger(8, chat.layer);
                        state.bindInteger(9, chat.seq_in);
                        state.bindInteger(10, chat.seq_out);
                        state.bindInteger(11, chat.key_use_count_out | (chat.key_use_count_in << 16));
                        state.bindLong(12, chat.exchange_id);
                        state.bindInteger(13, chat.key_create_date);
                        state.bindLong(14, chat.future_key_fingerprint);
                        state.bindByteBuffer(15, data4);
                        state.bindByteBuffer(16, data5);
                        state.bindInteger(17, chat.in_seq_no);
                        state.bindInteger(18, chat.admin_id);
                        state.bindInteger(19, chat.mtproto_seq);
                        state.step();
                        state.dispose();
                        data.reuse();
                        data2.reuse();
                        data3.reuse();
                        data4.reuse();
                        data5.reuse();
                        if (dialog != null) {
                            state = MessagesStorage.this.database.executeFast("REPLACE INTO dialogs VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
                            state.bindLong(1, dialog.id);
                            state.bindInteger(2, dialog.last_message_date);
                            state.bindInteger(3, dialog.unread_count);
                            state.bindInteger(4, dialog.top_message);
                            state.bindInteger(5, dialog.read_inbox_max_id);
                            state.bindInteger(6, dialog.read_outbox_max_id);
                            state.bindInteger(7, 0);
                            state.bindInteger(8, dialog.unread_mentions_count);
                            state.bindInteger(9, dialog.pts);
                            state.bindInteger(10, 0);
                            state.bindInteger(11, dialog.pinnedNum);
                            state.step();
                            state.dispose();
                        }
                    } catch (Throwable e) {
                        FileLog.e(e);
                    }
                }
            });
        }
    }

    private String formatUserSearchName(User user) {
        StringBuilder str = new StringBuilder(TtmlNode.ANONYMOUS_REGION_ID);
        if (user.first_name != null && user.first_name.length() > 0) {
            str.append(user.first_name);
        }
        if (user.last_name != null && user.last_name.length() > 0) {
            if (str.length() > 0) {
                str.append(" ");
            }
            str.append(user.last_name);
        }
        str.append(";;;");
        if (user.username != null && user.username.length() > 0) {
            str.append(user.username);
        }
        return str.toString().toLowerCase();
    }

    private void putUsersInternal(ArrayList<User> users) throws Exception {
        if (users != null) {
            if (!users.isEmpty()) {
                SQLitePreparedStatement state = this.database.executeFast("REPLACE INTO users VALUES(?, ?, ?, ?)");
                for (int a = 0; a < users.size(); a++) {
                    User user = (User) users.get(a);
                    if (user.min) {
                        SQLiteCursor cursor = this.database.queryFinalized(String.format(Locale.US, "SELECT data FROM users WHERE uid = %d", new Object[]{Integer.valueOf(user.id)}), new Object[0]);
                        if (cursor.next()) {
                            try {
                                NativeByteBuffer data = cursor.byteBufferValue(0);
                                if (data != null) {
                                    User oldUser = User.TLdeserialize(data, data.readInt32(false), false);
                                    data.reuse();
                                    if (oldUser != null) {
                                        if (user.username != null) {
                                            oldUser.username = user.username;
                                            oldUser.flags |= 8;
                                        } else {
                                            oldUser.username = null;
                                            oldUser.flags &= -9;
                                        }
                                        if (user.photo != null) {
                                            oldUser.photo = user.photo;
                                            oldUser.flags |= 32;
                                        } else {
                                            oldUser.photo = null;
                                            oldUser.flags &= -33;
                                        }
                                        user = oldUser;
                                    }
                                }
                            } catch (Throwable e) {
                                FileLog.e(e);
                            }
                        }
                        cursor.dispose();
                    }
                    state.requery();
                    NativeByteBuffer data2 = new NativeByteBuffer(user.getObjectSize());
                    user.serializeToStream(data2);
                    state.bindInteger(1, user.id);
                    state.bindString(2, formatUserSearchName(user));
                    if (user.status != null) {
                        if (user.status instanceof TL_userStatusRecently) {
                            user.status.expires = -100;
                        } else if (user.status instanceof TL_userStatusLastWeek) {
                            user.status.expires = -101;
                        } else if (user.status instanceof TL_userStatusLastMonth) {
                            user.status.expires = -102;
                        }
                        state.bindInteger(3, user.status.expires);
                    } else {
                        state.bindInteger(3, 0);
                    }
                    state.bindByteBuffer(4, data2);
                    state.step();
                    data2.reuse();
                }
                state.dispose();
            }
        }
    }

    private void putChatsInternal(ArrayList<Chat> chats) throws Exception {
        if (chats != null) {
            if (!chats.isEmpty()) {
                SQLitePreparedStatement state = this.database.executeFast("REPLACE INTO chats VALUES(?, ?, ?)");
                for (int a = 0; a < chats.size(); a++) {
                    Chat chat = (Chat) chats.get(a);
                    if (chat.min) {
                        SQLiteCursor cursor = this.database.queryFinalized(String.format(Locale.US, "SELECT data FROM chats WHERE uid = %d", new Object[]{Integer.valueOf(chat.id)}), new Object[0]);
                        if (cursor.next()) {
                            try {
                                NativeByteBuffer data = cursor.byteBufferValue(0);
                                if (data != null) {
                                    Chat oldChat = Chat.TLdeserialize(data, data.readInt32(false), false);
                                    data.reuse();
                                    if (oldChat != null) {
                                        oldChat.title = chat.title;
                                        oldChat.photo = chat.photo;
                                        oldChat.broadcast = chat.broadcast;
                                        oldChat.verified = chat.verified;
                                        oldChat.megagroup = chat.megagroup;
                                        oldChat.democracy = chat.democracy;
                                        if (chat.username != null) {
                                            oldChat.username = chat.username;
                                            oldChat.flags |= 64;
                                        } else {
                                            oldChat.username = null;
                                            oldChat.flags &= -65;
                                        }
                                        chat = oldChat;
                                    }
                                }
                            } catch (Throwable e) {
                                FileLog.e(e);
                            }
                        }
                        cursor.dispose();
                    }
                    state.requery();
                    NativeByteBuffer data2 = new NativeByteBuffer(chat.getObjectSize());
                    chat.serializeToStream(data2);
                    state.bindInteger(1, chat.id);
                    if (chat.title != null) {
                        state.bindString(2, chat.title.toLowerCase());
                    } else {
                        state.bindString(2, TtmlNode.ANONYMOUS_REGION_ID);
                    }
                    state.bindByteBuffer(3, data2);
                    state.step();
                    data2.reuse();
                }
                state.dispose();
            }
        }
    }

    public void getUsersInternal(String usersToLoad, ArrayList<User> result) throws Exception {
        if (!(usersToLoad == null || usersToLoad.length() == 0)) {
            if (result != null) {
                SQLiteCursor cursor = this.database.queryFinalized(String.format(Locale.US, "SELECT data, status FROM users WHERE uid IN(%s)", new Object[]{usersToLoad}), new Object[0]);
                while (cursor.next()) {
                    try {
                        NativeByteBuffer data = cursor.byteBufferValue(0);
                        if (data != null) {
                            User user = User.TLdeserialize(data, data.readInt32(false), false);
                            data.reuse();
                            if (user != null) {
                                if (user.status != null) {
                                    user.status.expires = cursor.intValue(1);
                                }
                                result.add(user);
                            }
                        }
                    } catch (Throwable e) {
                        FileLog.e(e);
                    }
                }
                cursor.dispose();
            }
        }
    }

    public void getChatsInternal(String chatsToLoad, ArrayList<Chat> result) throws Exception {
        if (!(chatsToLoad == null || chatsToLoad.length() == 0)) {
            if (result != null) {
                SQLiteCursor cursor = this.database.queryFinalized(String.format(Locale.US, "SELECT data FROM chats WHERE uid IN(%s)", new Object[]{chatsToLoad}), new Object[0]);
                while (cursor.next()) {
                    try {
                        NativeByteBuffer data = cursor.byteBufferValue(0);
                        if (data != null) {
                            Chat chat = Chat.TLdeserialize(data, data.readInt32(false), false);
                            data.reuse();
                            if (chat != null) {
                                result.add(chat);
                            }
                        }
                    } catch (Throwable e) {
                        FileLog.e(e);
                    }
                }
                cursor.dispose();
            }
        }
    }

    public void getEncryptedChatsInternal(String chatsToLoad, ArrayList<EncryptedChat> result, ArrayList<Integer> usersToLoad) throws Exception {
        if (!(chatsToLoad == null || chatsToLoad.length() == 0)) {
            if (result != null) {
                SQLiteCursor cursor = this.database.queryFinalized(String.format(Locale.US, "SELECT data, user, g, authkey, ttl, layer, seq_in, seq_out, use_count, exchange_id, key_date, fprint, fauthkey, khash, in_seq_no, admin_id, mtproto_seq FROM enc_chats WHERE uid IN(%s)", new Object[]{chatsToLoad}), new Object[0]);
                while (cursor.next()) {
                    try {
                        NativeByteBuffer data = cursor.byteBufferValue(0);
                        if (data != null) {
                            EncryptedChat chat = EncryptedChat.TLdeserialize(data, data.readInt32(false), false);
                            data.reuse();
                            if (chat != null) {
                                chat.user_id = cursor.intValue(1);
                                if (!(usersToLoad == null || usersToLoad.contains(Integer.valueOf(chat.user_id)))) {
                                    usersToLoad.add(Integer.valueOf(chat.user_id));
                                }
                                chat.a_or_b = cursor.byteArrayValue(2);
                                chat.auth_key = cursor.byteArrayValue(3);
                                chat.ttl = cursor.intValue(4);
                                chat.layer = cursor.intValue(5);
                                chat.seq_in = cursor.intValue(6);
                                chat.seq_out = cursor.intValue(7);
                                int use_count = cursor.intValue(8);
                                chat.key_use_count_in = (short) (use_count >> 16);
                                chat.key_use_count_out = (short) use_count;
                                chat.exchange_id = cursor.longValue(9);
                                chat.key_create_date = cursor.intValue(10);
                                chat.future_key_fingerprint = cursor.longValue(11);
                                chat.future_auth_key = cursor.byteArrayValue(12);
                                chat.key_hash = cursor.byteArrayValue(13);
                                chat.in_seq_no = cursor.intValue(14);
                                int admin_id = cursor.intValue(15);
                                if (admin_id != 0) {
                                    chat.admin_id = admin_id;
                                }
                                chat.mtproto_seq = cursor.intValue(16);
                                result.add(chat);
                            }
                        }
                    } catch (Throwable e) {
                        FileLog.e(e);
                    }
                }
                cursor.dispose();
            }
        }
    }

    public void putUsersAndChats(final ArrayList<User> users, final ArrayList<Chat> chats, final boolean withTransaction, boolean useQueue) {
        if (users == null || !users.isEmpty() || chats == null || !chats.isEmpty()) {
            if (useQueue) {
                this.storageQueue.postRunnable(new Runnable() {
                    public void run() {
                        MessagesStorage.this.putUsersAndChatsInternal(users, chats, withTransaction);
                    }
                });
            } else {
                putUsersAndChatsInternal(users, chats, withTransaction);
            }
        }
    }

    public void removeFromDownloadQueue(long id, int type, boolean move) {
        final boolean z = move;
        final int i = type;
        final long j = id;
        this.storageQueue.postRunnable(new Runnable() {
            public void run() {
                try {
                    if (z) {
                        int minDate = -1;
                        SQLiteCursor cursor = MessagesStorage.this.database.queryFinalized(String.format(Locale.US, "SELECT min(date) FROM download_queue WHERE type = %d", new Object[]{Integer.valueOf(i)}), new Object[0]);
                        if (cursor.next()) {
                            minDate = cursor.intValue(0);
                        }
                        cursor.dispose();
                        if (minDate != -1) {
                            MessagesStorage.this.database.executeFast(String.format(Locale.US, "UPDATE download_queue SET date = %d WHERE uid = %d AND type = %d", new Object[]{Integer.valueOf(minDate - 1), Long.valueOf(j), Integer.valueOf(i)})).stepThis().dispose();
                        }
                    } else {
                        MessagesStorage.this.database.executeFast(String.format(Locale.US, "DELETE FROM download_queue WHERE uid = %d AND type = %d", new Object[]{Long.valueOf(j), Integer.valueOf(i)})).stepThis().dispose();
                    }
                } catch (Throwable e) {
                    FileLog.e(e);
                }
            }
        });
    }

    public void clearDownloadQueue(final int type) {
        this.storageQueue.postRunnable(new Runnable() {
            public void run() {
                try {
                    if (type == 0) {
                        MessagesStorage.this.database.executeFast("DELETE FROM download_queue WHERE 1").stepThis().dispose();
                    } else {
                        MessagesStorage.this.database.executeFast(String.format(Locale.US, "DELETE FROM download_queue WHERE type = %d", new Object[]{Integer.valueOf(type)})).stepThis().dispose();
                    }
                } catch (Throwable e) {
                    FileLog.e(e);
                }
            }
        });
    }

    public void getDownloadQueue(final int type) {
        this.storageQueue.postRunnable(new Runnable() {
            public void run() {
                try {
                    final ArrayList<DownloadObject> objects = new ArrayList();
                    SQLiteCursor cursor = MessagesStorage.this.database.queryFinalized(String.format(Locale.US, "SELECT uid, type, data FROM download_queue WHERE type = %d ORDER BY date DESC LIMIT 3", new Object[]{Integer.valueOf(type)}), new Object[0]);
                    while (cursor.next()) {
                        DownloadObject downloadObject = new DownloadObject();
                        downloadObject.type = cursor.intValue(1);
                        downloadObject.id = cursor.longValue(0);
                        NativeByteBuffer data = cursor.byteBufferValue(2);
                        if (data != null) {
                            MessageMedia messageMedia = MessageMedia.TLdeserialize(data, data.readInt32(false), false);
                            data.reuse();
                            if (messageMedia.document != null) {
                                downloadObject.object = messageMedia.document;
                            } else if (messageMedia.photo != null) {
                                downloadObject.object = FileLoader.getClosestPhotoSizeWithSize(messageMedia.photo.sizes, AndroidUtilities.getPhotoSize());
                            }
                            downloadObject.secret = messageMedia.ttl_seconds != 0;
                        }
                        objects.add(downloadObject);
                    }
                    cursor.dispose();
                    AndroidUtilities.runOnUIThread(new Runnable() {
                        public void run() {
                            DownloadController.getInstance(MessagesStorage.this.currentAccount).processDownloadObjects(type, objects);
                        }
                    });
                } catch (Throwable e) {
                    FileLog.e(e);
                }
            }
        });
    }

    private int getMessageMediaType(Message message) {
        if (message instanceof TL_message_secret) {
            if (!((((message.media instanceof TL_messageMediaPhoto) || MessageObject.isGifMessage(message)) && message.ttl > 0 && message.ttl <= 60) || MessageObject.isVoiceMessage(message) || MessageObject.isVideoMessage(message))) {
                if (!MessageObject.isRoundVideoMessage(message)) {
                    if ((message.media instanceof TL_messageMediaPhoto) || MessageObject.isVideoMessage(message)) {
                        return 0;
                    }
                }
            }
            return 1;
        } else if ((message instanceof TL_message) && (((message.media instanceof TL_messageMediaPhoto) || (message.media instanceof TL_messageMediaDocument)) && message.media.ttl_seconds != 0)) {
            return 1;
        } else {
            if (!(message.media instanceof TL_messageMediaPhoto)) {
                if (MessageObject.isVideoMessage(message)) {
                }
            }
            return 0;
        }
        return -1;
    }

    public void putWebPages(final LongSparseArray<WebPage> webPages) {
        if (!isEmpty((LongSparseArray) webPages)) {
            this.storageQueue.postRunnable(new Runnable() {
                public void run() {
                    try {
                        final ArrayList<Message> messages = new ArrayList();
                        int a = 0;
                        for (int a2 = 0; a2 < webPages.size(); a2++) {
                            SQLiteCursor cursor = MessagesStorage.this.database;
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append("SELECT mid FROM webpage_pending WHERE id = ");
                            stringBuilder.append(webPages.keyAt(a2));
                            cursor = cursor.queryFinalized(stringBuilder.toString(), new Object[0]);
                            ArrayList<Long> mids = new ArrayList();
                            while (cursor.next()) {
                                mids.add(Long.valueOf(cursor.longValue(0)));
                            }
                            cursor.dispose();
                            if (!mids.isEmpty()) {
                                cursor = MessagesStorage.this.database.queryFinalized(String.format(Locale.US, "SELECT mid, data FROM messages WHERE mid IN (%s)", new Object[]{TextUtils.join(",", mids)}), new Object[0]);
                                while (cursor.next()) {
                                    int mid = cursor.intValue(0);
                                    NativeByteBuffer data = cursor.byteBufferValue(1);
                                    if (data != null) {
                                        Message message = Message.TLdeserialize(data, data.readInt32(false), false);
                                        message.readAttachPath(data, UserConfig.getInstance(MessagesStorage.this.currentAccount).clientUserId);
                                        data.reuse();
                                        if (message.media instanceof TL_messageMediaWebPage) {
                                            message.id = mid;
                                            message.media.webpage = (WebPage) webPages.valueAt(a2);
                                            messages.add(message);
                                        }
                                    }
                                }
                                cursor.dispose();
                            }
                        }
                        if (!messages.isEmpty()) {
                            MessagesStorage.this.database.beginTransaction();
                            SQLitePreparedStatement state = MessagesStorage.this.database.executeFast("UPDATE messages SET data = ? WHERE mid = ?");
                            SQLitePreparedStatement state2 = MessagesStorage.this.database.executeFast("UPDATE media_v2 SET data = ? WHERE mid = ?");
                            while (a < messages.size()) {
                                Message message2 = (Message) messages.get(a);
                                NativeByteBuffer data2 = new NativeByteBuffer(message2.getObjectSize());
                                message2.serializeToStream(data2);
                                long messageId = (long) message2.id;
                                if (message2.to_id.channel_id != 0) {
                                    messageId |= ((long) message2.to_id.channel_id) << 32;
                                }
                                state.requery();
                                state.bindByteBuffer(1, data2);
                                state.bindLong(2, messageId);
                                state.step();
                                state2.requery();
                                state2.bindByteBuffer(1, data2);
                                state2.bindLong(2, messageId);
                                state2.step();
                                data2.reuse();
                                a++;
                            }
                            state.dispose();
                            state2.dispose();
                            MessagesStorage.this.database.commitTransaction();
                            AndroidUtilities.runOnUIThread(new Runnable() {
                                public void run() {
                                    NotificationCenter.getInstance(MessagesStorage.this.currentAccount).postNotificationName(NotificationCenter.didReceivedWebpages, messages);
                                }
                            });
                        }
                    } catch (Throwable e) {
                        FileLog.e(e);
                    }
                }
            });
        }
    }

    public void overwriteChannel(final int channel_id, final TL_updates_channelDifferenceTooLong difference, final int newDialogType) {
        this.storageQueue.postRunnable(new Runnable() {
            public void run() {
                boolean checkInvite = false;
                try {
                    final long did = (long) (-channel_id);
                    int pinned = 0;
                    SQLiteCursor cursor = MessagesStorage.this.database;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("SELECT pts, pinned FROM dialogs WHERE did = ");
                    stringBuilder.append(did);
                    cursor = cursor.queryFinalized(stringBuilder.toString(), new Object[0]);
                    if (cursor.next()) {
                        pinned = cursor.intValue(1);
                    } else if (newDialogType != 0) {
                        checkInvite = true;
                    }
                    cursor.dispose();
                    SQLiteDatabase access$000 = MessagesStorage.this.database;
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("DELETE FROM messages WHERE uid = ");
                    stringBuilder2.append(did);
                    access$000.executeFast(stringBuilder2.toString()).stepThis().dispose();
                    access$000 = MessagesStorage.this.database;
                    stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("DELETE FROM bot_keyboard WHERE uid = ");
                    stringBuilder2.append(did);
                    access$000.executeFast(stringBuilder2.toString()).stepThis().dispose();
                    access$000 = MessagesStorage.this.database;
                    stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("DELETE FROM media_counts_v2 WHERE uid = ");
                    stringBuilder2.append(did);
                    access$000.executeFast(stringBuilder2.toString()).stepThis().dispose();
                    access$000 = MessagesStorage.this.database;
                    stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("DELETE FROM media_v2 WHERE uid = ");
                    stringBuilder2.append(did);
                    access$000.executeFast(stringBuilder2.toString()).stepThis().dispose();
                    access$000 = MessagesStorage.this.database;
                    stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("DELETE FROM messages_holes WHERE uid = ");
                    stringBuilder2.append(did);
                    access$000.executeFast(stringBuilder2.toString()).stepThis().dispose();
                    access$000 = MessagesStorage.this.database;
                    stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("DELETE FROM media_holes_v2 WHERE uid = ");
                    stringBuilder2.append(did);
                    access$000.executeFast(stringBuilder2.toString()).stepThis().dispose();
                    DataQuery.getInstance(MessagesStorage.this.currentAccount).clearBotKeyboard(did, null);
                    TL_messages_dialogs dialogs = new TL_messages_dialogs();
                    dialogs.chats.addAll(difference.chats);
                    dialogs.users.addAll(difference.users);
                    dialogs.messages.addAll(difference.messages);
                    TL_dialog dialog = new TL_dialog();
                    dialog.id = did;
                    dialog.flags = 1;
                    dialog.peer = new TL_peerChannel();
                    dialog.peer.channel_id = channel_id;
                    dialog.top_message = difference.top_message;
                    dialog.read_inbox_max_id = difference.read_inbox_max_id;
                    dialog.read_outbox_max_id = difference.read_outbox_max_id;
                    dialog.unread_count = difference.unread_count;
                    dialog.unread_mentions_count = difference.unread_mentions_count;
                    dialog.notify_settings = null;
                    dialog.pinned = pinned != 0;
                    dialog.pinnedNum = pinned;
                    dialog.pts = difference.pts;
                    dialogs.dialogs.add(dialog);
                    MessagesStorage.this.putDialogsInternal(dialogs, false);
                    MessagesStorage.this.updateDialogsWithDeletedMessages(new ArrayList(), null, false, channel_id);
                    AndroidUtilities.runOnUIThread(new Runnable() {
                        public void run() {
                            NotificationCenter.getInstance(MessagesStorage.this.currentAccount).postNotificationName(NotificationCenter.removeAllMessagesFromDialog, Long.valueOf(did), Boolean.valueOf(true));
                        }
                    });
                    if (checkInvite) {
                        if (newDialogType == 1) {
                            MessagesController.getInstance(MessagesStorage.this.currentAccount).checkChannelInviter(channel_id);
                        } else {
                            MessagesController.getInstance(MessagesStorage.this.currentAccount).generateJoinMessage(channel_id, false);
                        }
                    }
                } catch (Throwable e) {
                    FileLog.e(e);
                }
            }
        });
    }

    public void putChannelViews(final SparseArray<SparseIntArray> channelViews, final boolean isChannel) {
        if (!isEmpty((SparseArray) channelViews)) {
            this.storageQueue.postRunnable(new Runnable() {
                public void run() {
                    try {
                        MessagesStorage.this.database.beginTransaction();
                        SQLitePreparedStatement state = MessagesStorage.this.database.executeFast("UPDATE messages SET media = max((SELECT media FROM messages WHERE mid = ?), ?) WHERE mid = ?");
                        for (int a = 0; a < channelViews.size(); a++) {
                            int peer = channelViews.keyAt(a);
                            SparseIntArray messages = (SparseIntArray) channelViews.get(peer);
                            for (int b = 0; b < messages.size(); b++) {
                                int views = messages.get(messages.keyAt(b));
                                long messageId = (long) messages.keyAt(b);
                                if (isChannel) {
                                    messageId |= ((long) (-peer)) << 32;
                                }
                                state.requery();
                                state.bindLong(1, messageId);
                                state.bindInteger(2, views);
                                state.bindLong(3, messageId);
                                state.step();
                            }
                        }
                        state.dispose();
                        MessagesStorage.this.database.commitTransaction();
                    } catch (Throwable e) {
                        FileLog.e(e);
                    }
                }
            });
        }
    }

    private boolean isValidKeyboardToSave(Message message) {
        return (message.reply_markup == null || (message.reply_markup instanceof TL_replyInlineMarkup) || (message.reply_markup.selective && !message.mentioned)) ? false : true;
    }

    private void putMessagesInternal(ArrayList<Message> messages, boolean withTransaction, boolean doNotUpdateDialogDate, int downloadMask, boolean ifNoLastMessage) {
        Message lastMessage;
        int lastMid;
        LongSparseArray<Message> messagesMap;
        SQLitePreparedStatement state;
        LongSparseArray<Integer> mentionCounts;
        LongSparseArray<Integer> mediaTypes;
        LongSparseArray<Long> messagesMediaIdsMap;
        int a;
        SparseArray<LongSparseArray<Integer>> sparseArray;
        int a2;
        Integer count;
        int a3;
        LongSparseArray<Integer> mentionCounts2;
        LongSparseArray<Long> mentionsIdsMap;
        LongSparseArray<Message> botKeyboards;
        LongSparseArray<Integer> mediaTypes2;
        LongSparseArray<Long> messagesMediaIdsMap2;
        LongSparseArray<Message> messagesMap2;
        SparseArray<LongSparseArray<Integer>> mediaCounts;
        LongSparseArray<Integer> messagesCounts;
        SQLitePreparedStatement state5;
        SQLitePreparedStatement state4;
        int downloadMediaMask;
        SQLitePreparedStatement state42;
        LongSparseArray<Integer> mentionCounts3;
        SQLitePreparedStatement state52;
        SQLitePreparedStatement state3;
        LongSparseArray<Message> messagesMap3;
        LongSparseArray<Integer> messagesCounts2;
        MessagesStorage messagesStorage = this;
        ArrayList arrayList = messages;
        if (ifNoLastMessage) {
            lastMessage = (Message) arrayList.get(0);
            if (lastMessage.dialog_id == 0) {
                if (lastMessage.to_id.user_id != 0) {
                    lastMessage.dialog_id = (long) lastMessage.to_id.user_id;
                } else if (lastMessage.to_id.chat_id != 0) {
                    lastMessage.dialog_id = (long) (-lastMessage.to_id.chat_id);
                } else {
                    lastMessage.dialog_id = (long) (-lastMessage.to_id.channel_id);
                }
            }
            lastMid = -1;
            SQLiteCursor cursor = messagesStorage.database;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("SELECT last_mid FROM dialogs WHERE did = ");
            stringBuilder.append(lastMessage.dialog_id);
            cursor = cursor.queryFinalized(stringBuilder.toString(), new Object[0]);
            if (cursor.next()) {
                lastMid = cursor.intValue(0);
            }
            cursor.dispose();
            if (lastMid != 0) {
                return;
            }
        }
        if (withTransaction) {
            messagesStorage.database.beginTransaction();
        }
        LongSparseArray<Message> messagesMap4 = new LongSparseArray();
        LongSparseArray<Integer> messagesCounts3 = new LongSparseArray();
        LongSparseArray<Integer> mentionCounts4 = new LongSparseArray();
        LongSparseArray<Message> botKeyboards2 = new LongSparseArray();
        StringBuilder messageIds = new StringBuilder();
        LongSparseArray<Integer> dialogsReadMax = new LongSparseArray();
        LongSparseArray<Long> messagesIdsMap = new LongSparseArray();
        LongSparseArray<Long> mentionsIdsMap2 = new LongSparseArray();
        SparseArray<LongSparseArray<Integer>> mediaCounts2 = null;
        LongSparseArray<Long> messagesMediaIdsMap3 = null;
        SQLitePreparedStatement state2 = messagesStorage.database.executeFast("REPLACE INTO messages VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, NULL, ?, ?)");
        SQLitePreparedStatement state22 = null;
        StringBuilder messageMediaIds = null;
        SQLitePreparedStatement state32 = messagesStorage.database.executeFast("REPLACE INTO randoms VALUES(?, ?)");
        LongSparseArray<Integer> mediaTypes3 = null;
        SQLitePreparedStatement state43 = messagesStorage.database.executeFast("REPLACE INTO download_queue VALUES(?, ?, ?, ?)");
        SQLitePreparedStatement state53 = messagesStorage.database.executeFast("REPLACE INTO webpage_pending VALUES(?, ?)");
        SQLitePreparedStatement state33 = state32;
        SQLitePreparedStatement state44 = state43;
        LongSparseArray<Long> messagesMediaIdsMap4 = messagesMediaIdsMap3;
        StringBuilder messageMediaIds2 = messageMediaIds;
        LongSparseArray<Integer> mediaTypes4 = mediaTypes3;
        int a4 = 0;
        while (true) {
            messagesMap = messagesMap4;
            if (a4 >= messages.size()) {
                break;
            }
            lastMessage = (Message) arrayList.get(a4);
            state = state2;
            mentionCounts = mentionCounts4;
            mentionCounts4 = (long) lastMessage.id;
            mediaTypes = mediaTypes4;
            messagesMediaIdsMap = messagesMediaIdsMap4;
            if (lastMessage.dialog_id == 0) {
                if (lastMessage.to_id.user_id != 0) {
                    lastMessage.dialog_id = (long) lastMessage.to_id.user_id;
                } else if (lastMessage.to_id.chat_id != 0) {
                    lastMessage.dialog_id = (long) (-lastMessage.to_id.chat_id);
                } else {
                    lastMessage.dialog_id = (long) (-lastMessage.to_id.channel_id);
                }
            }
            if (lastMessage.to_id.channel_id != 0) {
                mentionCounts4 |= ((long) lastMessage.to_id.channel_id) << 32;
            }
            if (lastMessage.mentioned && lastMessage.media_unread) {
                mentionsIdsMap2.put(mentionCounts4, Long.valueOf(lastMessage.dialog_id));
            }
            if (!((lastMessage.action instanceof TL_messageActionHistoryClear) || MessageObject.isOut(lastMessage) || (lastMessage.id <= 0 && !MessageObject.isUnread(lastMessage)))) {
                Integer currentMaxId = (Integer) dialogsReadMax.get(lastMessage.dialog_id);
                if (currentMaxId == null) {
                    SQLiteDatabase sQLiteDatabase = messagesStorage.database;
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("SELECT inbox_max FROM dialogs WHERE did = ");
                    try {
                        stringBuilder2.append(lastMessage.dialog_id);
                        SQLiteCursor cursor2 = sQLiteDatabase.queryFinalized(stringBuilder2.toString(), new Object[0]);
                        if (cursor2.next()) {
                            currentMaxId = Integer.valueOf(cursor2.intValue(0));
                        } else {
                            currentMaxId = Integer.valueOf(0);
                            Integer num = currentMaxId;
                        }
                        cursor2.dispose();
                        dialogsReadMax.put(lastMessage.dialog_id, currentMaxId);
                    } catch (Throwable e) {
                        Throwable e2;
                        Throwable e3 = e2;
                        messagesStorage = this;
                    }
                } else {
                    Integer num2 = currentMaxId;
                }
                if (lastMessage.id < 0 || currentMaxId.intValue() < lastMessage.id) {
                    if (messageIds.length() > 0) {
                        messageIds.append(",");
                    }
                    messageIds.append(mentionCounts4);
                    messagesIdsMap.put(mentionCounts4, Long.valueOf(lastMessage.dialog_id));
                }
            }
            if (DataQuery.canAddMessageToMedia(lastMessage)) {
                if (messageMediaIds2 == null) {
                    messageMediaIds2 = new StringBuilder();
                    messagesMediaIdsMap4 = new LongSparseArray();
                    mediaTypes4 = new LongSparseArray();
                } else {
                    messagesMediaIdsMap4 = messagesMediaIdsMap;
                    mediaTypes4 = mediaTypes;
                }
                if (messageMediaIds2.length() > 0) {
                    messageMediaIds2.append(",");
                }
                messageMediaIds2.append(mentionCounts4);
                messagesMediaIdsMap4.put(mentionCounts4, Long.valueOf(lastMessage.dialog_id));
                mediaTypes4.put(mentionCounts4, Integer.valueOf(DataQuery.getMediaType(lastMessage)));
            } else {
                messagesMediaIdsMap4 = messagesMediaIdsMap;
                mediaTypes4 = mediaTypes;
            }
            try {
                StringBuilder messageMediaIds3;
                if (isValidKeyboardToSave(lastMessage)) {
                    messageMediaIds3 = messageMediaIds2;
                    Message oldMessage = (Message) botKeyboards2.get(lastMessage.dialog_id);
                    if (oldMessage != null) {
                        if (oldMessage.id < lastMessage.id) {
                        }
                    }
                    botKeyboards2.put(lastMessage.dialog_id, lastMessage);
                } else {
                    messageMediaIds3 = messageMediaIds2;
                }
                a4++;
                messagesMap4 = messagesMap;
                state2 = state;
                mentionCounts4 = mentionCounts;
                messageMediaIds2 = messageMediaIds3;
                ArrayList<Message> arrayList2 = messages;
            } catch (Exception e4) {
                e2 = e4;
            }
        }
        mentionCounts = mentionCounts4;
        state = state2;
        mediaTypes = mediaTypes4;
        messagesMediaIdsMap = messagesMediaIdsMap4;
        for (a = 0; a < botKeyboards2.size(); a++) {
            DataQuery.getInstance(messagesStorage.currentAccount).putBotKeyboard(botKeyboards2.keyAt(a), (Message) botKeyboards2.valueAt(a));
        }
        LongSparseArray<Integer> dialogsReadMax2;
        if (messageMediaIds2 != null) {
            SQLiteCursor cursor3 = messagesStorage.database;
            StringBuilder stringBuilder3 = new StringBuilder();
            stringBuilder3.append("SELECT mid FROM media_v2 WHERE mid IN(");
            stringBuilder3.append(messageMediaIds2.toString());
            stringBuilder3.append(")");
            cursor3 = cursor3.queryFinalized(stringBuilder3.toString(), new Object[0]);
            while (cursor3.next()) {
                messagesMediaIdsMap4 = messagesMediaIdsMap;
                messagesMediaIdsMap4.remove(cursor3.longValue(0));
                messagesMediaIdsMap = messagesMediaIdsMap4;
            }
            messagesMediaIdsMap4 = messagesMediaIdsMap;
            cursor3.dispose();
            sparseArray = new SparseArray();
            a2 = 0;
            while (a2 < messagesMediaIdsMap4.size()) {
                Integer count2;
                SQLiteCursor cursor4;
                long key = messagesMediaIdsMap4.keyAt(a2);
                long value = ((Long) messagesMediaIdsMap4.valueAt(a2)).longValue();
                StringBuilder messageMediaIds4 = messageMediaIds2;
                mediaTypes4 = mediaTypes;
                long key2 = key;
                Integer type = (Integer) mediaTypes4.get(key2);
                LongSparseArray<Integer> counts = (LongSparseArray) sparseArray.get(type.intValue());
                if (counts == null) {
                    counts = new LongSparseArray();
                    count2 = Integer.valueOf(null);
                    sparseArray.put(type.intValue(), counts);
                    dialogsReadMax2 = dialogsReadMax;
                    messageMediaIds2 = value;
                } else {
                    dialogsReadMax2 = dialogsReadMax;
                    messageMediaIds2 = value;
                    count2 = (Integer) counts.get(messageMediaIds2);
                }
                Integer count3 = count2;
                if (count3 == null) {
                    cursor4 = cursor3;
                    count = Integer.valueOf(null);
                } else {
                    cursor4 = cursor3;
                    count = count3;
                }
                counts.put(messageMediaIds2, Integer.valueOf(count.intValue() + 1));
                a2++;
                mediaTypes = mediaTypes4;
                messageMediaIds2 = messageMediaIds4;
                dialogsReadMax = dialogsReadMax2;
                cursor3 = cursor4;
            }
            dialogsReadMax2 = dialogsReadMax;
            mediaTypes4 = mediaTypes;
        } else {
            dialogsReadMax2 = dialogsReadMax;
            messagesMediaIdsMap4 = messagesMediaIdsMap;
            mediaTypes4 = mediaTypes;
            sparseArray = mediaCounts2;
        }
        if (messageIds.length() > 0) {
            long dialog_id;
            SQLiteCursor cursor5 = messagesStorage.database;
            messageMediaIds2 = new StringBuilder();
            messageMediaIds2.append("SELECT mid FROM messages WHERE mid IN(");
            messageMediaIds2.append(messageIds.toString());
            messageMediaIds2.append(")");
            cursor5 = cursor5.queryFinalized(messageMediaIds2.toString(), new Object[0]);
            while (cursor5.next()) {
                long mid = cursor5.longValue(0);
                messagesIdsMap.remove(mid);
                mentionsIdsMap2.remove(mid);
            }
            cursor5.dispose();
            a3 = 0;
            while (a3 < messagesIdsMap.size()) {
                LongSparseArray<Long> messagesIdsMap2 = messagesIdsMap;
                dialog_id = ((Long) messagesIdsMap.valueAt(a3)).longValue();
                count = (Integer) messagesCounts3.get(dialog_id);
                if (count == null) {
                    count = Integer.valueOf(0);
                }
                messagesCounts3.put(dialog_id, Integer.valueOf(count.intValue() + 1));
                a3++;
                messagesIdsMap = messagesIdsMap2;
            }
            a3 = 0;
            while (a3 < mentionsIdsMap2.size()) {
                dialog_id = ((Long) mentionsIdsMap2.valueAt(a3)).longValue();
                mentionCounts2 = mentionCounts;
                Integer count4 = (Integer) mentionCounts2.get(dialog_id);
                if (count4 == null) {
                    count4 = Integer.valueOf(0);
                }
                mentionCounts2.put(dialog_id, Integer.valueOf(count4.intValue() + 1));
                a3++;
                mentionCounts = mentionCounts2;
            }
            mentionCounts2 = mentionCounts;
        } else {
            mentionCounts2 = mentionCounts;
        }
        int downloadMediaMask2 = 0;
        SQLitePreparedStatement state23 = state22;
        a = 0;
        while (true) {
            ArrayList<Message> arrayList3 = messages;
            if (a >= messages.size()) {
                break;
            }
            long messageId;
            boolean updateDialog;
            SQLitePreparedStatement state24;
            Message message = (Message) arrayList3.get(a);
            messagesStorage.fixUnsupportedMedia(message);
            SQLitePreparedStatement state6 = state;
            state6.requery();
            StringBuilder messageIds2 = messageIds;
            long messageId2 = (long) message.id;
            if (message.local_id != null) {
                messageId2 = (long) message.local_id;
            }
            if (message.to_id.channel_id != 0) {
                messageId = messageId2 | (((long) message.to_id.channel_id) << 32);
            } else {
                messageId = messageId2;
            }
            mentionsIdsMap = mentionsIdsMap2;
            botKeyboards = botKeyboards2;
            NativeByteBuffer data = new NativeByteBuffer(message.getObjectSize());
            message.serializeToStream(data);
            boolean updateDialog2 = true;
            if (!message.action || !(message.action instanceof TL_messageEncryptedAction) || (message.action.encryptedAction instanceof TL_decryptedMessageActionSetMessageTTL) || (message.action.encryptedAction instanceof TL_decryptedMessageActionScreenshotMessages)) {
                updateDialog = updateDialog2;
            } else {
                updateDialog = false;
            }
            if (updateDialog) {
                mediaTypes2 = mediaTypes4;
                messagesMediaIdsMap2 = messagesMediaIdsMap4;
                messagesMap2 = messagesMap;
                Message updateDialog3 = (Message) messagesMap2.get(message.dialog_id);
                if (updateDialog3 != null) {
                    mediaCounts = sparseArray;
                    if (message.date <= updateDialog3.date && (message.id <= 0 || updateDialog3.id <= 0 || message.id <= updateDialog3.id)) {
                        if (message.id < 0 && updateDialog3.id < 0 && message.id < updateDialog3.id) {
                        }
                    }
                } else {
                    mediaCounts = sparseArray;
                }
                Message lastMessage2 = updateDialog3;
                messagesMap2.put(message.dialog_id, message);
            } else {
                mediaCounts = sparseArray;
                boolean z = updateDialog;
                mediaTypes2 = mediaTypes4;
                messagesMediaIdsMap2 = messagesMediaIdsMap4;
                messagesMap2 = messagesMap;
            }
            state6.bindLong(1, messageId);
            state6.bindLong(2, message.dialog_id);
            state6.bindInteger(3, MessageObject.getUnreadFlags(message));
            state6.bindInteger(4, message.send_state);
            state6.bindInteger(5, message.date);
            state6.bindByteBuffer(6, data);
            state6.bindInteger(7, MessageObject.isOut(message));
            state6.bindInteger(8, message.ttl);
            if ((message.flags & 1024) != 0) {
                state6.bindInteger(9, message.views);
            } else {
                state6.bindInteger(9, messagesStorage.getMessageMediaType(message));
            }
            state6.bindInteger(10, 0);
            state6.bindInteger(11, message.mentioned);
            state6.step();
            if (message.random_id != 0) {
                state2 = state33;
                state2.requery();
                messagesCounts = messagesCounts3;
                state2.bindLong(1, message.random_id);
                state2.bindLong(2, messageId);
                state2.step();
            } else {
                messagesCounts = messagesCounts3;
                state2 = state33;
            }
            if (DataQuery.canAddMessageToMedia(message)) {
                if (state23 == null) {
                    state23 = messagesStorage.database.executeFast("REPLACE INTO media_v2 VALUES(?, ?, ?, ?, ?)");
                }
                state23.requery();
                state23.bindLong(1, messageId);
                state23.bindLong(2, message.dialog_id);
                state23.bindInteger(3, message.date);
                state23.bindInteger(4, DataQuery.getMediaType(message));
                state23.bindByteBuffer(5, data);
                state23.step();
            }
            if (message.media instanceof TL_messageMediaWebPage) {
                state5 = state53;
                state5.requery();
                state24 = state23;
                state5.bindLong(1, message.media.webpage.id);
                state5.bindLong(2, messageId);
                state5.step();
            } else {
                state24 = state23;
                state5 = state53;
            }
            data.reuse();
            if (downloadMask != 0) {
                if (message.to_id.channel_id != 0) {
                    if (message.post) {
                    }
                    state4 = state44;
                    a++;
                    state44 = state4;
                    state53 = state5;
                    state33 = state2;
                    messagesMap = messagesMap2;
                    state = state6;
                    messageIds = messageIds2;
                    mentionsIdsMap2 = mentionsIdsMap;
                    botKeyboards2 = botKeyboards;
                    mediaTypes4 = mediaTypes2;
                    messagesMediaIdsMap4 = messagesMediaIdsMap2;
                    sparseArray = mediaCounts;
                    messagesCounts3 = messagesCounts;
                    state23 = state24;
                }
                if (message.date >= ConnectionsManager.getInstance(messagesStorage.currentAccount).getCurrentTime() - 3600 && DownloadController.getInstance(messagesStorage.currentAccount).canDownloadMedia(message)) {
                    if ((message.media instanceof TL_messageMediaPhoto) || (message.media instanceof TL_messageMediaDocument)) {
                        int downloadMediaMask3;
                        NativeByteBuffer data2;
                        long id = 0;
                        MessageMedia object = null;
                        int type2;
                        if (MessageObject.isVoiceMessage(message)) {
                            type2 = 0;
                            id = message.media.document.id;
                            a3 = 2;
                            object = new TL_messageMediaDocument();
                            object.document = message.media.document;
                            object.flags |= 1;
                        } else {
                            type2 = 0;
                            long j = messageId;
                            if (MessageObject.isRoundVideoMessage(message)) {
                                id = message.media.document.id;
                                a3 = 64;
                                object = new TL_messageMediaDocument();
                                object.document = message.media.document;
                                object.flags |= 1;
                            } else if (message.media instanceof TL_messageMediaPhoto) {
                                if (FileLoader.getClosestPhotoSizeWithSize(message.media.photo.sizes, AndroidUtilities.getPhotoSize()) != null) {
                                    id = message.media.photo.id;
                                    object = new TL_messageMediaPhoto();
                                    object.photo = message.media.photo;
                                    object.flags |= 1;
                                    a3 = 1;
                                } else {
                                    a3 = type2;
                                }
                            } else if (MessageObject.isVideoMessage(message)) {
                                id = message.media.document.id;
                                a3 = 4;
                                object = new TL_messageMediaDocument();
                                object.document = message.media.document;
                                object.flags |= 1;
                            } else if (!(message.media instanceof TL_messageMediaDocument) || MessageObject.isMusicMessage(message) || MessageObject.isGifDocument(message.media.document)) {
                                messageId = 0;
                                a3 = type2;
                                if (object != null) {
                                    if (message.media.ttl_seconds != 0) {
                                        object.ttl_seconds = message.media.ttl_seconds;
                                        object.flags |= 4;
                                    }
                                    downloadMediaMask2 |= a3;
                                    state4 = state44;
                                    state4.requery();
                                    downloadMediaMask3 = downloadMediaMask2;
                                    data2 = new NativeByteBuffer(object.getObjectSize());
                                    object.serializeToStream(data2);
                                    state4.bindLong(1, messageId);
                                    state4.bindInteger(2, a3);
                                    state4.bindInteger(3, message.date);
                                    state4.bindByteBuffer(4, data2);
                                    state4.step();
                                    data2.reuse();
                                    downloadMediaMask2 = downloadMediaMask3;
                                    a++;
                                    state44 = state4;
                                    state53 = state5;
                                    state33 = state2;
                                    messagesMap = messagesMap2;
                                    state = state6;
                                    messageIds = messageIds2;
                                    mentionsIdsMap2 = mentionsIdsMap;
                                    botKeyboards2 = botKeyboards;
                                    mediaTypes4 = mediaTypes2;
                                    messagesMediaIdsMap4 = messagesMediaIdsMap2;
                                    sparseArray = mediaCounts;
                                    messagesCounts3 = messagesCounts;
                                    state23 = state24;
                                }
                            } else {
                                id = message.media.document.id;
                                a3 = 8;
                                object = new TL_messageMediaDocument();
                                object.document = message.media.document;
                                object.flags |= 1;
                            }
                        }
                        messageId = id;
                        if (object != null) {
                            if (message.media.ttl_seconds != 0) {
                                object.ttl_seconds = message.media.ttl_seconds;
                                object.flags |= 4;
                            }
                            downloadMediaMask2 |= a3;
                            state4 = state44;
                            state4.requery();
                            downloadMediaMask3 = downloadMediaMask2;
                            data2 = new NativeByteBuffer(object.getObjectSize());
                            object.serializeToStream(data2);
                            state4.bindLong(1, messageId);
                            state4.bindInteger(2, a3);
                            state4.bindInteger(3, message.date);
                            state4.bindByteBuffer(4, data2);
                            state4.step();
                            data2.reuse();
                            downloadMediaMask2 = downloadMediaMask3;
                            a++;
                            state44 = state4;
                            state53 = state5;
                            state33 = state2;
                            messagesMap = messagesMap2;
                            state = state6;
                            messageIds = messageIds2;
                            mentionsIdsMap2 = mentionsIdsMap;
                            botKeyboards2 = botKeyboards;
                            mediaTypes4 = mediaTypes2;
                            messagesMediaIdsMap4 = messagesMediaIdsMap2;
                            sparseArray = mediaCounts;
                            messagesCounts3 = messagesCounts;
                            state23 = state24;
                        }
                    }
                    state4 = state44;
                    a++;
                    state44 = state4;
                    state53 = state5;
                    state33 = state2;
                    messagesMap = messagesMap2;
                    state = state6;
                    messageIds = messageIds2;
                    mentionsIdsMap2 = mentionsIdsMap;
                    botKeyboards2 = botKeyboards;
                    mediaTypes4 = mediaTypes2;
                    messagesMediaIdsMap4 = messagesMediaIdsMap2;
                    sparseArray = mediaCounts;
                    messagesCounts3 = messagesCounts;
                    state23 = state24;
                }
            }
            state4 = state44;
            a++;
            state44 = state4;
            state53 = state5;
            state33 = state2;
            messagesMap = messagesMap2;
            state = state6;
            messageIds = messageIds2;
            mentionsIdsMap2 = mentionsIdsMap;
            botKeyboards2 = botKeyboards;
            mediaTypes4 = mediaTypes2;
            messagesMediaIdsMap4 = messagesMediaIdsMap2;
            sparseArray = mediaCounts;
            messagesCounts3 = messagesCounts;
            state23 = state24;
        }
        mentionsIdsMap = mentionsIdsMap2;
        messagesCounts = messagesCounts3;
        mediaCounts = sparseArray;
        botKeyboards = botKeyboards2;
        mediaTypes2 = mediaTypes4;
        messagesMediaIdsMap2 = messagesMediaIdsMap4;
        state4 = state44;
        state5 = state53;
        state2 = state33;
        messagesMap2 = messagesMap;
        state.dispose();
        if (state23 != null) {
            state23.dispose();
        }
        state2.dispose();
        state4.dispose();
        state5.dispose();
        SQLitePreparedStatement state7 = messagesStorage.database.executeFast("REPLACE INTO dialogs VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
        int a5 = 0;
        while (a5 < messagesMap2.size()) {
            SQLitePreparedStatement state25;
            int a6;
            long key3 = messagesMap2.keyAt(a5);
            if (key3 == 0) {
                state25 = state23;
                downloadMediaMask = downloadMediaMask2;
                a6 = a5;
                state42 = state4;
                mentionCounts3 = mentionCounts2;
                state52 = state5;
                state3 = state2;
                messagesMap3 = messagesMap2;
                messagesCounts2 = messagesCounts;
            } else {
                int pts;
                int inbox_max;
                int pinned;
                int dialog_date;
                int pinned2;
                int outbox_max;
                long messageId3;
                int i;
                Message message2;
                long messageId4;
                Message message3 = (Message) messagesMap2.valueAt(a5);
                a2 = 0;
                if (message3 != null) {
                    a2 = message3.to_id.channel_id;
                }
                SQLiteDatabase sQLiteDatabase2 = messagesStorage.database;
                state25 = state23;
                state23 = new StringBuilder();
                state42 = state4;
                state23.append("SELECT date, unread_count, pts, last_mid, inbox_max, outbox_max, pinned, unread_count_i FROM dialogs WHERE did = ");
                state23.append(key3);
                state52 = state5;
                state23 = sQLiteDatabase2.queryFinalized(state23.toString(), new Object[0]);
                lastMid = 0;
                a4 = 0;
                int pts2 = a2 != 0 ? 1 : 0;
                int old_mentions_count = 0;
                int dialog_date2;
                if (state23.next()) {
                    dialog_date2 = 0;
                    int dialog_date3 = state23.intValue(0);
                    a4 = state23.intValue(1);
                    pts2 = state23.intValue(2);
                    lastMid = state23.intValue(3);
                    int inbox_max2 = state23.intValue(4);
                    int outbox_max2 = state23.intValue(5);
                    int pinned3 = state23.intValue(6);
                    old_mentions_count = state23.intValue(7);
                    downloadMediaMask = downloadMediaMask2;
                    state3 = state2;
                    messagesMap3 = messagesMap2;
                    pts = pts2;
                    inbox_max = inbox_max2;
                    downloadMediaMask2 = outbox_max2;
                    pinned = pinned3;
                    dialog_date = dialog_date3;
                } else {
                    dialog_date2 = 0;
                    if (a2 != 0) {
                        MessagesController.getInstance(messagesStorage.currentAccount).checkChannelInviter(a2);
                    }
                    downloadMediaMask = downloadMediaMask2;
                    state3 = state2;
                    messagesMap3 = messagesMap2;
                    pts = pts2;
                    inbox_max = 0;
                    downloadMediaMask2 = 0;
                    pinned = 0;
                    dialog_date = dialog_date2;
                }
                state23.dispose();
                Integer mentions_count = (Integer) mentionCounts2.get(key3);
                SQLitePreparedStatement cursor6 = state23;
                LongSparseArray<Integer> state26 = messagesCounts;
                a6 = a5;
                Integer unread_count = (Integer) state26.get(key3);
                if (unread_count == null) {
                    pinned2 = pinned;
                    unread_count = Integer.valueOf(0);
                } else {
                    pinned2 = pinned;
                    state26.put(key3, Integer.valueOf(unread_count.intValue() + a4));
                }
                Integer mentions_count2 = mentions_count;
                if (mentions_count2 == null) {
                    messagesCounts2 = state26;
                    mentions_count2 = Integer.valueOf(null);
                } else {
                    messagesCounts2 = state26;
                    mentionCounts2.put(key3, Integer.valueOf(mentions_count2.intValue() + old_mentions_count));
                }
                if (message3 != null) {
                    outbox_max = downloadMediaMask2;
                    state23 = (long) message3.id;
                } else {
                    outbox_max = downloadMediaMask2;
                    state23 = (long) lastMid;
                }
                if (message3 != null) {
                    messageId3 = state23;
                    if (message3.local_id != null) {
                        state23 = (long) message3.local_id;
                        if (a2 == 0) {
                            mentionCounts3 = mentionCounts2;
                            state23 |= ((long) a2) << 32;
                        } else {
                            mentionCounts3 = mentionCounts2;
                            i = lastMid;
                        }
                        state7.requery();
                        state7.bindLong(1, key3);
                        if (message3 == null) {
                            if (doNotUpdateDialogDate) {
                                if (dialog_date == 0) {
                                    message2 = message3;
                                    message3 = 2;
                                }
                            }
                            state7.bindInteger(2, message3.date);
                            state7.bindInteger(3, unread_count.intValue() + a4);
                            state7.bindLong(4, state23);
                            state7.bindInteger(5, inbox_max);
                            state7.bindInteger(6, outbox_max);
                            messageId4 = state23;
                            state7.bindLong(7, 0);
                            state7.bindInteger(8, old_mentions_count + mentions_count2.intValue());
                            state7.bindInteger(9, pts);
                            state7.bindInteger(10, null);
                            state7.bindInteger(11, pinned2);
                            state7.step();
                        } else {
                            message3 = 2;
                        }
                        state7.bindInteger(message3, dialog_date);
                        state7.bindInteger(3, unread_count.intValue() + a4);
                        state7.bindLong(4, state23);
                        state7.bindInteger(5, inbox_max);
                        state7.bindInteger(6, outbox_max);
                        messageId4 = state23;
                        state7.bindLong(7, 0);
                        state7.bindInteger(8, old_mentions_count + mentions_count2.intValue());
                        state7.bindInteger(9, pts);
                        state7.bindInteger(10, null);
                        state7.bindInteger(11, pinned2);
                        state7.step();
                    }
                } else {
                    messageId3 = state23;
                }
                state23 = messageId3;
                if (a2 == 0) {
                    mentionCounts3 = mentionCounts2;
                    i = lastMid;
                } else {
                    mentionCounts3 = mentionCounts2;
                    state23 |= ((long) a2) << 32;
                }
                state7.requery();
                state7.bindLong(1, key3);
                if (message3 == null) {
                    message3 = 2;
                } else {
                    if (doNotUpdateDialogDate) {
                        if (dialog_date == 0) {
                            message2 = message3;
                            message3 = 2;
                        }
                    }
                    state7.bindInteger(2, message3.date);
                    state7.bindInteger(3, unread_count.intValue() + a4);
                    state7.bindLong(4, state23);
                    state7.bindInteger(5, inbox_max);
                    state7.bindInteger(6, outbox_max);
                    messageId4 = state23;
                    state7.bindLong(7, 0);
                    state7.bindInteger(8, old_mentions_count + mentions_count2.intValue());
                    state7.bindInteger(9, pts);
                    state7.bindInteger(10, null);
                    state7.bindInteger(11, pinned2);
                    state7.step();
                }
                state7.bindInteger(message3, dialog_date);
                state7.bindInteger(3, unread_count.intValue() + a4);
                state7.bindLong(4, state23);
                state7.bindInteger(5, inbox_max);
                state7.bindInteger(6, outbox_max);
                messageId4 = state23;
                state7.bindLong(7, 0);
                state7.bindInteger(8, old_mentions_count + mentions_count2.intValue());
                state7.bindInteger(9, pts);
                state7.bindInteger(10, null);
                state7.bindInteger(11, pinned2);
                state7.step();
            }
            a5 = a6 + 1;
            state23 = state25;
            state4 = state42;
            state5 = state52;
            state2 = state3;
            messagesMap2 = messagesMap3;
            downloadMediaMask2 = downloadMediaMask;
            messagesCounts = messagesCounts2;
            mentionCounts2 = mentionCounts3;
            messagesStorage = this;
        }
        downloadMediaMask = downloadMediaMask2;
        state42 = state4;
        mentionCounts3 = mentionCounts2;
        state52 = state5;
        state3 = state2;
        messagesMap3 = messagesMap2;
        messagesCounts2 = messagesCounts;
        try {
            state7.dispose();
            if (mediaCounts != null) {
                state32 = this.database.executeFast("REPLACE INTO media_counts_v2 VALUES(?, ?, ?)");
                a3 = 0;
                while (true) {
                    sparseArray = mediaCounts;
                    if (a3 >= sparseArray.size()) {
                        break;
                    }
                    downloadMediaMask2 = sparseArray.keyAt(a3);
                    LongSparseArray<Integer> value2 = (LongSparseArray) sparseArray.valueAt(a3);
                    int b = 0;
                    while (b < value2.size()) {
                        long uid = value2.keyAt(b);
                        int count5 = -1;
                        SQLitePreparedStatement state8 = state7;
                        int lower_part = (int) uid;
                        state7 = messagesStorage.database.queryFinalized(String.format(Locale.US, "SELECT count FROM media_counts_v2 WHERE uid = %d AND type = %d LIMIT 1", new Object[]{Long.valueOf(uid), Integer.valueOf(downloadMediaMask2)}), new Object[0]);
                        if (state7.next()) {
                            count5 = state7.intValue(0);
                        }
                        state7.dispose();
                        if (count5 != -1) {
                            state32.requery();
                            count5 += ((Integer) value2.valueAt(b)).intValue();
                            state32.bindLong(1, uid);
                            state32.bindInteger(2, downloadMediaMask2);
                            state32.bindInteger(3, count5);
                            state32.step();
                        }
                        b++;
                        state7 = state8;
                    }
                    a3++;
                    mediaCounts = sparseArray;
                }
                state32.dispose();
                state3 = state32;
            } else {
                sparseArray = mediaCounts;
                messagesStorage = this;
            }
            if (withTransaction) {
                messagesStorage.database.commitTransaction();
            }
            MessagesController.getInstance(messagesStorage.currentAccount).processDialogsUpdateRead(messagesCounts2, mentionCounts3);
            if (downloadMediaMask != 0) {
                a3 = downloadMediaMask;
                AndroidUtilities.runOnUIThread(new Runnable() {
                    public void run() {
                        DownloadController.getInstance(MessagesStorage.this.currentAccount).newDownloadObjectsAvailable(a3);
                    }
                });
            }
        } catch (Exception e5) {
            e2 = e5;
            messagesStorage = this;
            e3 = e2;
            FileLog.e(e3);
        }
    }

    public void putMessages(ArrayList<Message> messages, boolean withTransaction, boolean useQueue, boolean doNotUpdateDialogDate, int downloadMask) {
        putMessages(messages, withTransaction, useQueue, doNotUpdateDialogDate, downloadMask, false);
    }

    public void putMessages(ArrayList<Message> messages, boolean withTransaction, boolean useQueue, boolean doNotUpdateDialogDate, int downloadMask, boolean ifNoLastMessage) {
        if (messages.size() != 0) {
            if (useQueue) {
                final ArrayList<Message> arrayList = messages;
                final boolean z = withTransaction;
                final boolean z2 = doNotUpdateDialogDate;
                final int i = downloadMask;
                final boolean z3 = ifNoLastMessage;
                this.storageQueue.postRunnable(new Runnable() {
                    public void run() {
                        MessagesStorage.this.putMessagesInternal(arrayList, z, z2, i, z3);
                    }
                });
            } else {
                putMessagesInternal(messages, withTransaction, doNotUpdateDialogDate, downloadMask, ifNoLastMessage);
            }
        }
    }

    public void markMessageAsSendError(final Message message) {
        this.storageQueue.postRunnable(new Runnable() {
            public void run() {
                try {
                    long messageId = (long) message.id;
                    if (message.to_id.channel_id != 0) {
                        messageId |= ((long) message.to_id.channel_id) << 32;
                    }
                    SQLiteDatabase access$000 = MessagesStorage.this.database;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("UPDATE messages SET send_state = 2 WHERE mid = ");
                    stringBuilder.append(messageId);
                    access$000.executeFast(stringBuilder.toString()).stepThis().dispose();
                } catch (Throwable e) {
                    FileLog.e(e);
                }
            }
        });
    }

    public void setMessageSeq(final int mid, final int seq_in, final int seq_out) {
        this.storageQueue.postRunnable(new Runnable() {
            public void run() {
                try {
                    SQLitePreparedStatement state = MessagesStorage.this.database.executeFast("REPLACE INTO messages_seq VALUES(?, ?, ?)");
                    state.requery();
                    state.bindInteger(1, mid);
                    state.bindInteger(2, seq_in);
                    state.bindInteger(3, seq_out);
                    state.step();
                    state.dispose();
                } catch (Throwable e) {
                    FileLog.e(e);
                }
            }
        });
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private long[] updateMessageStateAndIdInternal(long r21, java.lang.Integer r23, int r24, int r25, int r26) {
        /*
        r20 = this;
        r1 = r20;
        r2 = r24;
        r3 = r25;
        r4 = r26;
        r5 = 0;
        r6 = (long) r2;
        r8 = 0;
        r9 = 0;
        r10 = 1;
        if (r23 != 0) goto L_0x005d;
    L_0x000f:
        r12 = r1.database;	 Catch:{ Exception -> 0x0044, all -> 0x0040 }
        r13 = java.util.Locale.US;	 Catch:{ Exception -> 0x0044, all -> 0x0040 }
        r14 = "SELECT mid FROM randoms WHERE random_id = %d LIMIT 1";
        r15 = new java.lang.Object[r10];	 Catch:{ Exception -> 0x0044, all -> 0x0040 }
        r16 = java.lang.Long.valueOf(r21);	 Catch:{ Exception -> 0x0044, all -> 0x0040 }
        r15[r9] = r16;	 Catch:{ Exception -> 0x0044, all -> 0x0040 }
        r13 = java.lang.String.format(r13, r14, r15);	 Catch:{ Exception -> 0x0044, all -> 0x0040 }
        r14 = new java.lang.Object[r9];	 Catch:{ Exception -> 0x0044, all -> 0x0040 }
        r12 = r12.queryFinalized(r13, r14);	 Catch:{ Exception -> 0x0044, all -> 0x0040 }
        r5 = r12;
        r12 = r5.next();	 Catch:{ Exception -> 0x0044, all -> 0x0040 }
        if (r12 == 0) goto L_0x0038;
    L_0x002e:
        r12 = r5.intValue(r9);	 Catch:{ Exception -> 0x0044, all -> 0x0040 }
        r12 = java.lang.Integer.valueOf(r12);	 Catch:{ Exception -> 0x0044, all -> 0x0040 }
        r11 = r12;
        goto L_0x003a;
    L_0x0038:
        r11 = r23;
    L_0x003a:
        if (r5 == 0) goto L_0x0052;
    L_0x003c:
        r5.dispose();
        goto L_0x0052;
    L_0x0040:
        r0 = move-exception;
        r12 = r5;
    L_0x0042:
        r5 = r0;
        goto L_0x0057;
    L_0x0044:
        r0 = move-exception;
        r12 = r5;
        r5 = r0;
        org.telegram.messenger.FileLog.e(r5);	 Catch:{ all -> 0x0055 }
        if (r12 == 0) goto L_0x004f;
    L_0x004c:
        r12.dispose();
    L_0x004f:
        r11 = r23;
        r5 = r12;
    L_0x0052:
        if (r11 != 0) goto L_0x005f;
    L_0x0054:
        return r8;
    L_0x0055:
        r0 = move-exception;
        goto L_0x0042;
    L_0x0057:
        if (r12 == 0) goto L_0x005c;
    L_0x0059:
        r12.dispose();
    L_0x005c:
        throw r5;
    L_0x005d:
        r11 = r23;
    L_0x005f:
        r12 = r11.intValue();
        r12 = (long) r12;
        if (r4 == 0) goto L_0x0075;
    L_0x0066:
        r14 = (long) r4;
        r16 = 32;
        r14 = r14 << r16;
        r17 = r12 | r14;
        r12 = (long) r4;
        r12 = r12 << r16;
        r14 = r6 | r12;
        r12 = r17;
        goto L_0x0076;
    L_0x0075:
        r14 = r6;
    L_0x0076:
        r6 = 0;
        r16 = r6;
        r8 = r1.database;	 Catch:{ Exception -> 0x00ac, all -> 0x00a8 }
        r6 = java.util.Locale.US;	 Catch:{ Exception -> 0x00ac, all -> 0x00a8 }
        r7 = "SELECT uid FROM messages WHERE mid = %d LIMIT 1";
        r9 = new java.lang.Object[r10];	 Catch:{ Exception -> 0x00ac, all -> 0x00a8 }
        r18 = java.lang.Long.valueOf(r12);	 Catch:{ Exception -> 0x00ac, all -> 0x00a8 }
        r10 = 0;
        r9[r10] = r18;	 Catch:{ Exception -> 0x00ac, all -> 0x00a8 }
        r6 = java.lang.String.format(r6, r7, r9);	 Catch:{ Exception -> 0x00ac, all -> 0x00a8 }
        r7 = new java.lang.Object[r10];	 Catch:{ Exception -> 0x00ac, all -> 0x00a8 }
        r6 = r8.queryFinalized(r6, r7);	 Catch:{ Exception -> 0x00ac, all -> 0x00a8 }
        r5 = r6;
        r6 = r5.next();	 Catch:{ Exception -> 0x00ac, all -> 0x00a8 }
        if (r6 == 0) goto L_0x00a2;
    L_0x009a:
        r6 = 0;
        r7 = r5.longValue(r6);	 Catch:{ Exception -> 0x00ac, all -> 0x00a8 }
        r6 = r7;
        r16 = r6;
    L_0x00a2:
        if (r5 == 0) goto L_0x00b8;
    L_0x00a4:
        r5.dispose();
        goto L_0x00b8;
    L_0x00a8:
        r0 = move-exception;
        r2 = r0;
        goto L_0x020d;
    L_0x00ac:
        r0 = move-exception;
        r6 = r5;
        r5 = r0;
        org.telegram.messenger.FileLog.e(r5);	 Catch:{ all -> 0x020a }
        if (r6 == 0) goto L_0x00b7;
    L_0x00b4:
        r6.dispose();
    L_0x00b7:
        r5 = r6;
    L_0x00b8:
        r6 = 0;
        r8 = (r16 > r6 ? 1 : (r16 == r6 ? 0 : -1));
        if (r8 != 0) goto L_0x00c0;
    L_0x00be:
        r6 = 0;
        return r6;
    L_0x00c0:
        r6 = 0;
        r7 = (r12 > r14 ? 1 : (r12 == r14 ? 0 : -1));
        r8 = 2;
        if (r7 != 0) goto L_0x0104;
    L_0x00c6:
        if (r3 == 0) goto L_0x0104;
    L_0x00c9:
        r7 = r1.database;	 Catch:{ Exception -> 0x00e7, all -> 0x00e3 }
        r9 = "UPDATE messages SET send_state = 0, date = ? WHERE mid = ?";
        r7 = r7.executeFast(r9);	 Catch:{ Exception -> 0x00e7, all -> 0x00e3 }
        r6 = r7;
        r7 = 1;
        r6.bindInteger(r7, r3);	 Catch:{ Exception -> 0x00e7, all -> 0x00e3 }
        r6.bindLong(r8, r14);	 Catch:{ Exception -> 0x00e7, all -> 0x00e3 }
        r6.step();	 Catch:{ Exception -> 0x00e7, all -> 0x00e3 }
        if (r6 == 0) goto L_0x00e1;
    L_0x00de:
        r6.dispose();
    L_0x00e1:
        r7 = r6;
        goto L_0x00f2;
    L_0x00e3:
        r0 = move-exception;
        r7 = r6;
    L_0x00e5:
        r6 = r0;
        goto L_0x00fe;
    L_0x00e7:
        r0 = move-exception;
        r7 = r6;
        r6 = r0;
        org.telegram.messenger.FileLog.e(r6);	 Catch:{ all -> 0x00fc }
        if (r7 == 0) goto L_0x00f2;
    L_0x00ef:
        r7.dispose();
    L_0x00f2:
        r6 = new long[r8];
        r8 = 0;
        r6[r8] = r16;
        r8 = (long) r2;
        r10 = 1;
        r6[r10] = r8;
        return r6;
    L_0x00fc:
        r0 = move-exception;
        goto L_0x00e5;
    L_0x00fe:
        if (r7 == 0) goto L_0x0103;
    L_0x0100:
        r7.dispose();
    L_0x0103:
        throw r6;
    L_0x0104:
        r6 = 0;
        r7 = r1.database;	 Catch:{ Exception -> 0x0124, all -> 0x011f }
        r9 = "UPDATE messages SET mid = ?, send_state = 0 WHERE mid = ?";
        r7 = r7.executeFast(r9);	 Catch:{ Exception -> 0x0124, all -> 0x011f }
        r6 = r7;
        r7 = 1;
        r6.bindLong(r7, r14);	 Catch:{ Exception -> 0x0124, all -> 0x011f }
        r6.bindLong(r8, r12);	 Catch:{ Exception -> 0x0124, all -> 0x011f }
        r6.step();	 Catch:{ Exception -> 0x0124, all -> 0x011f }
        if (r6 == 0) goto L_0x0179;
    L_0x011a:
        r6.dispose();
        r6 = 0;
        goto L_0x0179;
    L_0x011f:
        r0 = move-exception;
        r2 = r0;
        r7 = r6;
        goto L_0x0203;
    L_0x0124:
        r0 = move-exception;
        r7 = r6;
        r6 = r0;
        r9 = r1.database;	 Catch:{ Exception -> 0x016c }
        r10 = java.util.Locale.US;	 Catch:{ Exception -> 0x016c }
        r8 = "DELETE FROM messages WHERE mid = %d";
        r2 = 1;
        r3 = new java.lang.Object[r2];	 Catch:{ Exception -> 0x016c }
        r2 = java.lang.Long.valueOf(r12);	 Catch:{ Exception -> 0x016c }
        r18 = 0;
        r3[r18] = r2;	 Catch:{ Exception -> 0x016c }
        r2 = java.lang.String.format(r10, r8, r3);	 Catch:{ Exception -> 0x016c }
        r2 = r9.executeFast(r2);	 Catch:{ Exception -> 0x016c }
        r2 = r2.stepThis();	 Catch:{ Exception -> 0x016c }
        r2.dispose();	 Catch:{ Exception -> 0x016c }
        r2 = r1.database;	 Catch:{ Exception -> 0x016c }
        r3 = java.util.Locale.US;	 Catch:{ Exception -> 0x016c }
        r8 = "DELETE FROM messages_seq WHERE mid = %d";
        r9 = 1;
        r10 = new java.lang.Object[r9];	 Catch:{ Exception -> 0x016c }
        r9 = java.lang.Long.valueOf(r12);	 Catch:{ Exception -> 0x016c }
        r18 = 0;
        r10[r18] = r9;	 Catch:{ Exception -> 0x016c }
        r3 = java.lang.String.format(r3, r8, r10);	 Catch:{ Exception -> 0x016c }
        r2 = r2.executeFast(r3);	 Catch:{ Exception -> 0x016c }
        r2 = r2.stepThis();	 Catch:{ Exception -> 0x016c }
        r2.dispose();	 Catch:{ Exception -> 0x016c }
        goto L_0x0171;
    L_0x0168:
        r0 = move-exception;
        r2 = r0;
        goto L_0x0203;
    L_0x016c:
        r0 = move-exception;
        r2 = r0;
        org.telegram.messenger.FileLog.e(r2);	 Catch:{ all -> 0x0168 }
    L_0x0171:
        if (r7 == 0) goto L_0x0178;
    L_0x0173:
        r7.dispose();
        r6 = 0;
        goto L_0x0179;
    L_0x0178:
        r6 = r7;
    L_0x0179:
        r2 = r1.database;	 Catch:{ Exception -> 0x0194 }
        r3 = "UPDATE media_v2 SET mid = ? WHERE mid = ?";
        r2 = r2.executeFast(r3);	 Catch:{ Exception -> 0x0194 }
        r6 = r2;
        r2 = 1;
        r6.bindLong(r2, r14);	 Catch:{ Exception -> 0x0194 }
        r2 = 2;
        r6.bindLong(r2, r12);	 Catch:{ Exception -> 0x0194 }
        r6.step();	 Catch:{ Exception -> 0x0194 }
        if (r6 == 0) goto L_0x01c2;
    L_0x018f:
        goto L_0x01be;
    L_0x0190:
        r0 = move-exception;
        r2 = r0;
        goto L_0x01fc;
    L_0x0194:
        r0 = move-exception;
        r2 = r0;
        r3 = r1.database;	 Catch:{ Exception -> 0x01b7 }
        r7 = java.util.Locale.US;	 Catch:{ Exception -> 0x01b7 }
        r8 = "DELETE FROM media_v2 WHERE mid = %d";
        r9 = 1;
        r10 = new java.lang.Object[r9];	 Catch:{ Exception -> 0x01b7 }
        r9 = java.lang.Long.valueOf(r12);	 Catch:{ Exception -> 0x01b7 }
        r18 = 0;
        r10[r18] = r9;	 Catch:{ Exception -> 0x01b7 }
        r7 = java.lang.String.format(r7, r8, r10);	 Catch:{ Exception -> 0x01b7 }
        r3 = r3.executeFast(r7);	 Catch:{ Exception -> 0x01b7 }
        r3 = r3.stepThis();	 Catch:{ Exception -> 0x01b7 }
        r3.dispose();	 Catch:{ Exception -> 0x01b7 }
        goto L_0x01bc;
    L_0x01b7:
        r0 = move-exception;
        r3 = r0;
        org.telegram.messenger.FileLog.e(r3);	 Catch:{ all -> 0x0190 }
    L_0x01bc:
        if (r6 == 0) goto L_0x01c2;
    L_0x01be:
        r6.dispose();
        r6 = 0;
    L_0x01c2:
        r2 = r1.database;	 Catch:{ Exception -> 0x01df }
        r3 = "UPDATE dialogs SET last_mid = ? WHERE last_mid = ?";
        r2 = r2.executeFast(r3);	 Catch:{ Exception -> 0x01df }
        r6 = r2;
        r2 = 1;
        r6.bindLong(r2, r14);	 Catch:{ Exception -> 0x01df }
        r2 = 2;
        r6.bindLong(r2, r12);	 Catch:{ Exception -> 0x01df }
        r6.step();	 Catch:{ Exception -> 0x01df }
        if (r6 == 0) goto L_0x01e7;
    L_0x01d8:
        r6.dispose();
        goto L_0x01e7;
    L_0x01dc:
        r0 = move-exception;
        r2 = r0;
        goto L_0x01f6;
    L_0x01df:
        r0 = move-exception;
        r2 = r0;
        org.telegram.messenger.FileLog.e(r2);	 Catch:{ all -> 0x01dc }
        if (r6 == 0) goto L_0x01e7;
    L_0x01e6:
        goto L_0x01d8;
    L_0x01e7:
        r2 = 2;
        r2 = new long[r2];
        r3 = 0;
        r2[r3] = r16;
        r3 = r11.intValue();
        r7 = (long) r3;
        r3 = 1;
        r2[r3] = r7;
        return r2;
    L_0x01f6:
        if (r6 == 0) goto L_0x01fb;
    L_0x01f8:
        r6.dispose();
    L_0x01fb:
        throw r2;
    L_0x01fc:
        if (r6 == 0) goto L_0x0202;
    L_0x01fe:
        r6.dispose();
        r6 = 0;
    L_0x0202:
        throw r2;
    L_0x0203:
        if (r7 == 0) goto L_0x0209;
    L_0x0205:
        r7.dispose();
        r7 = 0;
    L_0x0209:
        throw r2;
    L_0x020a:
        r0 = move-exception;
        r2 = r0;
        r5 = r6;
    L_0x020d:
        if (r5 == 0) goto L_0x0212;
    L_0x020f:
        r5.dispose();
    L_0x0212:
        throw r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.MessagesStorage.updateMessageStateAndIdInternal(long, java.lang.Integer, int, int, int):long[]");
    }

    public long[] updateMessageStateAndId(long random_id, Integer _oldId, int newId, int date, boolean useQueue, int channelId) {
        if (!useQueue) {
            return updateMessageStateAndIdInternal(random_id, _oldId, newId, date, channelId);
        }
        final long j = random_id;
        final Integer num = _oldId;
        final int i = newId;
        final int i2 = date;
        final int i3 = channelId;
        this.storageQueue.postRunnable(new Runnable() {
            public void run() {
                MessagesStorage.this.updateMessageStateAndIdInternal(j, num, i, i2, i3);
            }
        });
        return null;
    }

    private void updateUsersInternal(ArrayList<User> users, boolean onlyStatus, boolean withTransaction) {
        if (Thread.currentThread().getId() != this.storageQueue.getId()) {
            throw new RuntimeException("wrong db thread");
        }
        if (onlyStatus) {
            if (withTransaction) {
                try {
                    this.database.beginTransaction();
                } catch (Throwable e) {
                    FileLog.e(e);
                    return;
                }
            }
            SQLitePreparedStatement state = this.database.executeFast("UPDATE users SET status = ? WHERE uid = ?");
            Iterator it = users.iterator();
            while (it.hasNext()) {
                User user = (User) it.next();
                state.requery();
                if (user.status != null) {
                    state.bindInteger(1, user.status.expires);
                } else {
                    state.bindInteger(1, 0);
                }
                state.bindInteger(2, user.id);
                state.step();
            }
            state.dispose();
            if (withTransaction) {
                this.database.commitTransaction();
            }
        } else {
            StringBuilder ids = new StringBuilder();
            SparseArray<User> usersDict = new SparseArray();
            Iterator it2 = users.iterator();
            while (it2.hasNext()) {
                User user2 = (User) it2.next();
                if (ids.length() != 0) {
                    ids.append(",");
                }
                ids.append(user2.id);
                usersDict.put(user2.id, user2);
            }
            ArrayList<User> loadedUsers = new ArrayList();
            getUsersInternal(ids.toString(), loadedUsers);
            Iterator it3 = loadedUsers.iterator();
            while (it3.hasNext()) {
                User user3 = (User) it3.next();
                User updateUser = (User) usersDict.get(user3.id);
                if (updateUser != null) {
                    if (updateUser.first_name != null && updateUser.last_name != null) {
                        if (!UserObject.isContact(user3)) {
                            user3.first_name = updateUser.first_name;
                            user3.last_name = updateUser.last_name;
                        }
                        user3.username = updateUser.username;
                    } else if (updateUser.photo != null) {
                        user3.photo = updateUser.photo;
                    } else if (updateUser.phone != null) {
                        user3.phone = updateUser.phone;
                    }
                }
            }
            if (!loadedUsers.isEmpty()) {
                if (withTransaction) {
                    this.database.beginTransaction();
                }
                putUsersInternal(loadedUsers);
                if (withTransaction) {
                    this.database.commitTransaction();
                }
            }
        }
    }

    public void updateUsers(final ArrayList<User> users, final boolean onlyStatus, final boolean withTransaction, boolean useQueue) {
        if (!users.isEmpty()) {
            if (useQueue) {
                this.storageQueue.postRunnable(new Runnable() {
                    public void run() {
                        MessagesStorage.this.updateUsersInternal(users, onlyStatus, withTransaction);
                    }
                });
            } else {
                updateUsersInternal(users, onlyStatus, withTransaction);
            }
        }
    }

    private void markMessagesAsReadInternal(SparseLongArray inbox, SparseLongArray outbox, SparseIntArray encryptedMessages) {
        try {
            int b;
            long messageId;
            int a = 0;
            if (!isEmpty(inbox)) {
                for (b = 0; b < inbox.size(); b++) {
                    messageId = inbox.get(inbox.keyAt(b));
                    this.database.executeFast(String.format(Locale.US, "UPDATE messages SET read_state = read_state | 1 WHERE uid = %d AND mid > 0 AND mid <= %d AND read_state IN(0,2) AND out = 0", new Object[]{Integer.valueOf(key), Long.valueOf(messageId)})).stepThis().dispose();
                }
            }
            if (!isEmpty(outbox)) {
                for (b = 0; b < outbox.size(); b++) {
                    messageId = outbox.get(outbox.keyAt(b));
                    this.database.executeFast(String.format(Locale.US, "UPDATE messages SET read_state = read_state | 1 WHERE uid = %d AND mid > 0 AND mid <= %d AND read_state IN(0,2) AND out = 1", new Object[]{Integer.valueOf(key), Long.valueOf(messageId)})).stepThis().dispose();
                }
            }
            if (encryptedMessages != null && !isEmpty(encryptedMessages)) {
                while (true) {
                    b = a;
                    if (b >= encryptedMessages.size()) {
                        break;
                    }
                    long dialog_id = ((long) encryptedMessages.keyAt(b)) << 32;
                    a = encryptedMessages.valueAt(b);
                    SQLitePreparedStatement state = this.database.executeFast("UPDATE messages SET read_state = read_state | 1 WHERE uid = ? AND date <= ? AND read_state IN(0,2) AND out = 1");
                    state.requery();
                    state.bindLong(1, dialog_id);
                    state.bindInteger(2, a);
                    state.step();
                    state.dispose();
                    a = b + 1;
                }
            }
        } catch (Throwable e) {
            FileLog.e(e);
        }
    }

    public void markMessagesContentAsRead(final ArrayList<Long> mids, final int date) {
        if (!isEmpty((List) mids)) {
            this.storageQueue.postRunnable(new Runnable() {
                public void run() {
                    try {
                        String midsStr = TextUtils.join(",", mids);
                        MessagesStorage.this.database.executeFast(String.format(Locale.US, "UPDATE messages SET read_state = read_state | 2 WHERE mid IN (%s)", new Object[]{midsStr})).stepThis().dispose();
                        if (date != 0) {
                            SQLiteCursor cursor = MessagesStorage.this.database.queryFinalized(String.format(Locale.US, "SELECT mid, ttl FROM messages WHERE mid IN (%s) AND ttl > 0", new Object[]{midsStr}), new Object[0]);
                            ArrayList<Integer> arrayList = null;
                            while (cursor.next()) {
                                if (arrayList == null) {
                                    arrayList = new ArrayList();
                                }
                                arrayList.add(Integer.valueOf(cursor.intValue(0)));
                            }
                            if (arrayList != null) {
                                MessagesStorage.this.emptyMessagesMedia(arrayList);
                            }
                            cursor.dispose();
                        }
                    } catch (Throwable e) {
                        FileLog.e(e);
                    }
                }
            });
        }
    }

    public void markMessagesAsRead(final SparseLongArray inbox, final SparseLongArray outbox, final SparseIntArray encryptedMessages, boolean useQueue) {
        if (useQueue) {
            this.storageQueue.postRunnable(new Runnable() {
                public void run() {
                    MessagesStorage.this.markMessagesAsReadInternal(inbox, outbox, encryptedMessages);
                }
            });
        } else {
            markMessagesAsReadInternal(inbox, outbox, encryptedMessages);
        }
    }

    public void markMessagesAsDeletedByRandoms(final ArrayList<Long> messages) {
        if (!messages.isEmpty()) {
            this.storageQueue.postRunnable(new Runnable() {
                public void run() {
                    try {
                        String ids = TextUtils.join(",", messages);
                        SQLiteCursor cursor = MessagesStorage.this.database.queryFinalized(String.format(Locale.US, "SELECT mid FROM randoms WHERE random_id IN(%s)", new Object[]{ids}), new Object[0]);
                        final ArrayList<Integer> mids = new ArrayList();
                        while (cursor.next()) {
                            mids.add(Integer.valueOf(cursor.intValue(0)));
                        }
                        cursor.dispose();
                        if (!mids.isEmpty()) {
                            AndroidUtilities.runOnUIThread(new Runnable() {
                                public void run() {
                                    NotificationCenter.getInstance(MessagesStorage.this.currentAccount).postNotificationName(NotificationCenter.messagesDeleted, mids, Integer.valueOf(0));
                                }
                            });
                            MessagesStorage.this.updateDialogsWithReadMessagesInternal(mids, null, null, null);
                            MessagesStorage.this.markMessagesAsDeletedInternal((ArrayList) mids, 0);
                            MessagesStorage.this.updateDialogsWithDeletedMessagesInternal(mids, null, 0);
                        }
                    } catch (Throwable e) {
                        FileLog.e(e);
                    }
                }
            });
        }
    }

    public void updateDialogsWithDeletedMessages(final ArrayList<Integer> messages, final ArrayList<Long> additionalDialogsToUpdate, boolean useQueue, final int channelId) {
        if (!messages.isEmpty() || channelId != 0) {
            if (useQueue) {
                this.storageQueue.postRunnable(new Runnable() {
                    public void run() {
                        MessagesStorage.this.updateDialogsWithDeletedMessagesInternal(messages, additionalDialogsToUpdate, channelId);
                    }
                });
            } else {
                updateDialogsWithDeletedMessagesInternal(messages, additionalDialogsToUpdate, channelId);
            }
        }
    }

    public ArrayList<Long> markMessagesAsDeleted(final ArrayList<Integer> messages, boolean useQueue, final int channelId) {
        if (messages.isEmpty()) {
            return null;
        }
        if (!useQueue) {
            return markMessagesAsDeletedInternal((ArrayList) messages, channelId);
        }
        this.storageQueue.postRunnable(new Runnable() {
            public void run() {
                MessagesStorage.this.markMessagesAsDeletedInternal(messages, channelId);
            }
        });
        return null;
    }

    private ArrayList<Long> markMessagesAsDeletedInternal(int channelId, int mid) {
        MessagesStorage messagesStorage = this;
        int i = channelId;
        try {
            long maxMessageId;
            Throwable e;
            int i2;
            long did;
            Integer[] counts;
            SQLiteDatabase sQLiteDatabase;
            StringBuilder stringBuilder;
            int old_unread_count;
            int old_mentions_count;
            SQLitePreparedStatement state;
            ArrayList<Long> dialogsIds = new ArrayList();
            LongSparseArray<Integer[]> dialogsToUpdate = new LongSparseArray();
            long maxMessageId2 = ((long) mid) | (((long) i) << 32);
            ArrayList<File> filesToDelete = new ArrayList();
            int currentUser = UserConfig.getInstance(messagesStorage.currentAccount).getClientUserId();
            SQLiteCursor cursor = messagesStorage.database;
            Object[] objArr = new Object[2];
            objArr[0] = Integer.valueOf(-i);
            int i3 = 1;
            objArr[1] = Long.valueOf(maxMessageId2);
            cursor = cursor.queryFinalized(String.format(Locale.US, "SELECT uid, data, read_state, out, mention FROM messages WHERE uid = %d AND mid <= %d", objArr), new Object[0]);
            while (cursor.next()) {
                int i4;
                try {
                    long did2 = cursor.longValue(0);
                    maxMessageId = maxMessageId2;
                    if (did2 != ((long) currentUser)) {
                        int read_state = cursor.intValue(2);
                        if (cursor.intValue(3) == 0) {
                            try {
                                Integer[] unread_count = (Integer[]) dialogsToUpdate.get(did2);
                                if (unread_count == null) {
                                    unread_count = new Integer[]{Integer.valueOf(0), Integer.valueOf(0)};
                                    dialogsToUpdate.put(did2, unread_count);
                                }
                                if (read_state < 2) {
                                    Integer num = unread_count[i3];
                                    unread_count[i3] = Integer.valueOf(unread_count[i3].intValue() + i3);
                                }
                                if (read_state == 0 || read_state == 2) {
                                    Integer num2 = unread_count[0];
                                    unread_count[0] = Integer.valueOf(unread_count[0].intValue() + i3);
                                }
                            } catch (Throwable e2) {
                                e = e2;
                                i4 = currentUser;
                            }
                        }
                        if (((int) did2) == 0) {
                            try {
                                NativeByteBuffer data = cursor.byteBufferValue(i3);
                                if (data != null) {
                                    Message message = Message.TLdeserialize(data, data.readInt32(false), false);
                                    message.readAttachPath(data, UserConfig.getInstance(messagesStorage.currentAccount).clientUserId);
                                    data.reuse();
                                    if (message != null) {
                                        File file;
                                        if (message.media instanceof TL_messageMediaPhoto) {
                                            Iterator it = message.media.photo.sizes.iterator();
                                            while (it.hasNext()) {
                                                file = FileLoader.getPathToAttach((PhotoSize) it.next());
                                                if (file != null) {
                                                    i4 = currentUser;
                                                    try {
                                                        if (file.toString().length() > 0) {
                                                            filesToDelete.add(file);
                                                        }
                                                    } catch (Throwable e22) {
                                                        e = e22;
                                                    }
                                                } else {
                                                    i4 = currentUser;
                                                }
                                                currentUser = i4;
                                                i2 = mid;
                                            }
                                        } else {
                                            i4 = currentUser;
                                            if (message.media instanceof TL_messageMediaDocument) {
                                                file = FileLoader.getPathToAttach(message.media.document);
                                                if (file != null && file.toString().length() > 0) {
                                                    filesToDelete.add(file);
                                                }
                                                file = FileLoader.getPathToAttach(message.media.document.thumb);
                                                if (file != null && file.toString().length() > 0) {
                                                    filesToDelete.add(file);
                                                }
                                            }
                                            maxMessageId2 = maxMessageId;
                                            currentUser = i4;
                                            i2 = mid;
                                            i3 = 1;
                                        }
                                    }
                                }
                                i4 = currentUser;
                                maxMessageId2 = maxMessageId;
                                currentUser = i4;
                                i2 = mid;
                                i3 = 1;
                            } catch (Throwable e222) {
                                i4 = currentUser;
                                e = e222;
                            }
                        }
                    }
                    i4 = currentUser;
                    maxMessageId2 = maxMessageId;
                    currentUser = i4;
                    i2 = mid;
                    i3 = 1;
                } catch (Throwable e2222) {
                    i4 = currentUser;
                    maxMessageId = maxMessageId2;
                    e = e2222;
                }
            }
            maxMessageId = maxMessageId2;
            cursor.dispose();
            FileLoader.getInstance(messagesStorage.currentAccount).deleteFiles(filesToDelete, 0);
            for (i2 = 0; i2 < dialogsToUpdate.size(); i2++) {
                did = dialogsToUpdate.keyAt(i2);
                counts = (Integer[]) dialogsToUpdate.valueAt(i2);
                sQLiteDatabase = messagesStorage.database;
                stringBuilder = new StringBuilder();
                stringBuilder.append("SELECT unread_count, unread_count_i FROM dialogs WHERE did = ");
                stringBuilder.append(did);
                cursor = sQLiteDatabase.queryFinalized(stringBuilder.toString(), new Object[0]);
                old_unread_count = 0;
                old_mentions_count = 0;
                if (cursor.next()) {
                    old_unread_count = cursor.intValue(0);
                    old_mentions_count = cursor.intValue(1);
                }
                cursor.dispose();
                dialogsIds.add(Long.valueOf(did));
                state = messagesStorage.database.executeFast("UPDATE dialogs SET unread_count = ?, unread_count_i = ? WHERE did = ?");
                state.requery();
                state.bindInteger(1, Math.max(0, old_unread_count - counts[0].intValue()));
                state.bindInteger(2, Math.max(0, old_mentions_count - counts[1].intValue()));
                state.bindLong(3, did);
                state.step();
                state.dispose();
            }
            SQLiteDatabase sQLiteDatabase2 = messagesStorage.database;
            Object[] objArr2 = new Object[2];
            objArr2[0] = Integer.valueOf(-i);
            objArr2[1] = Long.valueOf(maxMessageId);
            sQLiteDatabase2.executeFast(String.format(Locale.US, "DELETE FROM messages WHERE uid = %d AND mid <= %d", objArr2)).stepThis().dispose();
            messagesStorage.database.executeFast(String.format(Locale.US, "DELETE FROM media_v2 WHERE uid = %d AND mid <= %d", new Object[]{Integer.valueOf(-i), Long.valueOf(maxMessageId)})).stepThis().dispose();
            messagesStorage.database.executeFast("DELETE FROM media_counts_v2 WHERE 1").stepThis().dispose();
            return dialogsIds;
            FileLog.e(e);
            cursor.dispose();
            FileLoader.getInstance(messagesStorage.currentAccount).deleteFiles(filesToDelete, 0);
            for (i2 = 0; i2 < dialogsToUpdate.size(); i2++) {
                did = dialogsToUpdate.keyAt(i2);
                counts = (Integer[]) dialogsToUpdate.valueAt(i2);
                sQLiteDatabase = messagesStorage.database;
                stringBuilder = new StringBuilder();
                stringBuilder.append("SELECT unread_count, unread_count_i FROM dialogs WHERE did = ");
                stringBuilder.append(did);
                cursor = sQLiteDatabase.queryFinalized(stringBuilder.toString(), new Object[0]);
                old_unread_count = 0;
                old_mentions_count = 0;
                if (cursor.next()) {
                    old_unread_count = cursor.intValue(0);
                    old_mentions_count = cursor.intValue(1);
                }
                cursor.dispose();
                dialogsIds.add(Long.valueOf(did));
                state = messagesStorage.database.executeFast("UPDATE dialogs SET unread_count = ?, unread_count_i = ? WHERE did = ?");
                state.requery();
                state.bindInteger(1, Math.max(0, old_unread_count - counts[0].intValue()));
                state.bindInteger(2, Math.max(0, old_mentions_count - counts[1].intValue()));
                state.bindLong(3, did);
                state.step();
                state.dispose();
            }
            SQLiteDatabase sQLiteDatabase22 = messagesStorage.database;
            Object[] objArr22 = new Object[2];
            objArr22[0] = Integer.valueOf(-i);
            objArr22[1] = Long.valueOf(maxMessageId);
            sQLiteDatabase22.executeFast(String.format(Locale.US, "DELETE FROM messages WHERE uid = %d AND mid <= %d", objArr22)).stepThis().dispose();
            messagesStorage.database.executeFast(String.format(Locale.US, "DELETE FROM media_v2 WHERE uid = %d AND mid <= %d", new Object[]{Integer.valueOf(-i), Long.valueOf(maxMessageId)})).stepThis().dispose();
            messagesStorage.database.executeFast("DELETE FROM media_counts_v2 WHERE 1").stepThis().dispose();
            return dialogsIds;
        } catch (Throwable e22222) {
            FileLog.e(e22222);
            return null;
        }
    }

    public ArrayList<Long> markMessagesAsDeleted(final int channelId, final int mid, boolean useQueue) {
        if (!useQueue) {
            return markMessagesAsDeletedInternal(channelId, mid);
        }
        this.storageQueue.postRunnable(new Runnable() {
            public void run() {
                MessagesStorage.this.markMessagesAsDeletedInternal(channelId, mid);
            }
        });
        return null;
    }

    private void fixUnsupportedMedia(Message message) {
        if (message != null) {
            if (message.media instanceof TL_messageMediaUnsupported_old) {
                if (message.media.bytes.length == 0) {
                    message.media.bytes = new byte[1];
                    message.media.bytes[0] = (byte) 76;
                }
            } else if (message.media instanceof TL_messageMediaUnsupported) {
                message.media = new TL_messageMediaUnsupported_old();
                message.media.bytes = new byte[1];
                message.media.bytes[0] = (byte) 76;
                message.flags |= 512;
            }
        }
    }

    private void doneHolesInTable(String table, long did, int max_id) throws Exception {
        SQLiteDatabase sQLiteDatabase;
        Locale locale;
        StringBuilder stringBuilder;
        if (max_id == 0) {
            sQLiteDatabase = this.database;
            locale = Locale.US;
            stringBuilder = new StringBuilder();
            stringBuilder.append("DELETE FROM ");
            stringBuilder.append(table);
            stringBuilder.append(" WHERE uid = %d");
            sQLiteDatabase.executeFast(String.format(locale, stringBuilder.toString(), new Object[]{Long.valueOf(did)})).stepThis().dispose();
        } else {
            sQLiteDatabase = this.database;
            locale = Locale.US;
            stringBuilder = new StringBuilder();
            stringBuilder.append("DELETE FROM ");
            stringBuilder.append(table);
            stringBuilder.append(" WHERE uid = %d AND start = 0");
            sQLiteDatabase.executeFast(String.format(locale, stringBuilder.toString(), new Object[]{Long.valueOf(did)})).stepThis().dispose();
        }
        SQLitePreparedStatement state = this.database;
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append("REPLACE INTO ");
        stringBuilder2.append(table);
        stringBuilder2.append(" VALUES(?, ?, ?)");
        state = state.executeFast(stringBuilder2.toString());
        state.requery();
        state.bindLong(1, did);
        state.bindInteger(2, 1);
        state.bindInteger(3, 1);
        state.step();
        state.dispose();
    }

    public void doneHolesInMedia(long did, int max_id, int type) throws Exception {
        int a = 0;
        if (type == -1) {
            if (max_id == 0) {
                this.database.executeFast(String.format(Locale.US, "DELETE FROM media_holes_v2 WHERE uid = %d", new Object[]{Long.valueOf(did)})).stepThis().dispose();
            } else {
                this.database.executeFast(String.format(Locale.US, "DELETE FROM media_holes_v2 WHERE uid = %d AND start = 0", new Object[]{Long.valueOf(did)})).stepThis().dispose();
            }
            SQLitePreparedStatement state = this.database.executeFast("REPLACE INTO media_holes_v2 VALUES(?, ?, ?, ?)");
            while (a < 5) {
                state.requery();
                state.bindLong(1, did);
                state.bindInteger(2, a);
                state.bindInteger(3, 1);
                state.bindInteger(4, 1);
                state.step();
                a++;
            }
            state.dispose();
            return;
        }
        if (max_id == 0) {
            this.database.executeFast(String.format(Locale.US, "DELETE FROM media_holes_v2 WHERE uid = %d AND type = %d", new Object[]{Long.valueOf(did), Integer.valueOf(type)})).stepThis().dispose();
        } else {
            this.database.executeFast(String.format(Locale.US, "DELETE FROM media_holes_v2 WHERE uid = %d AND type = %d AND start = 0", new Object[]{Long.valueOf(did), Integer.valueOf(type)})).stepThis().dispose();
        }
        SQLitePreparedStatement state2 = this.database.executeFast("REPLACE INTO media_holes_v2 VALUES(?, ?, ?, ?)");
        state2.requery();
        state2.bindLong(1, did);
        state2.bindInteger(2, type);
        state2.bindInteger(3, 1);
        state2.bindInteger(4, 1);
        state2.step();
        state2.dispose();
    }

    public void putMessages(messages_Messages messages, long dialog_id, int load_type, int max_id, boolean createDialog) {
        final messages_Messages org_telegram_tgnet_TLRPC_messages_Messages = messages;
        final int i = load_type;
        final long j = dialog_id;
        final int i2 = max_id;
        final boolean z = createDialog;
        this.storageQueue.postRunnable(new Runnable() {
            public void run() {
                try {
                    if (org_telegram_tgnet_TLRPC_messages_Messages.messages.isEmpty()) {
                        if (i == 0) {
                            MessagesStorage.this.doneHolesInTable("messages_holes", j, i2);
                            MessagesStorage.this.doneHolesInMedia(j, i2, -1);
                        }
                        return;
                    }
                    int minId;
                    int maxId;
                    int minChannelMessageId;
                    int i;
                    int mentionCountUpdate;
                    int i2;
                    MessagesStorage.this.database.beginTransaction();
                    if (i == 0) {
                        minId = ((Message) org_telegram_tgnet_TLRPC_messages_Messages.messages.get(org_telegram_tgnet_TLRPC_messages_Messages.messages.size() - 1)).id;
                        MessagesStorage.this.closeHolesInTable("messages_holes", j, minId, i2);
                        MessagesStorage.this.closeHolesInMedia(j, minId, i2, -1);
                    } else if (i == 1) {
                        maxId = ((Message) org_telegram_tgnet_TLRPC_messages_Messages.messages.get(0)).id;
                        MessagesStorage.this.closeHolesInTable("messages_holes", j, i2, maxId);
                        MessagesStorage.this.closeHolesInMedia(j, i2, maxId, -1);
                    } else if (i == 3 || i == 2 || i == 4) {
                        maxId = (i2 != 0 || i == 4) ? ((Message) org_telegram_tgnet_TLRPC_messages_Messages.messages.get(0)).id : ConnectionsManager.DEFAULT_DATACENTER_ID;
                        minId = ((Message) org_telegram_tgnet_TLRPC_messages_Messages.messages.get(org_telegram_tgnet_TLRPC_messages_Messages.messages.size() - 1)).id;
                        MessagesStorage.this.closeHolesInTable("messages_holes", j, minId, maxId);
                        MessagesStorage.this.closeHolesInMedia(j, minId, maxId, -1);
                    }
                    int count = org_telegram_tgnet_TLRPC_messages_Messages.messages.size();
                    SQLitePreparedStatement state = MessagesStorage.this.database.executeFast("REPLACE INTO messages VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, NULL, ?, ?)");
                    SQLitePreparedStatement state2 = MessagesStorage.this.database.executeFast("REPLACE INTO media_v2 VALUES(?, ?, ?, ?, ?)");
                    SQLitePreparedStatement state5 = null;
                    Message botKeyboard = null;
                    minId = ConnectionsManager.DEFAULT_DATACENTER_ID;
                    maxId = 0;
                    int mentionCountUpdate2 = ConnectionsManager.DEFAULT_DATACENTER_ID;
                    int a = 0;
                    int channelId = 0;
                    while (a < count) {
                        int count2;
                        int channelId2;
                        NativeByteBuffer data;
                        int readState;
                        Message message;
                        int i3;
                        int i4;
                        Message message2 = (Message) org_telegram_tgnet_TLRPC_messages_Messages.messages.get(a);
                        SQLitePreparedStatement state3 = state;
                        long messageId = (long) message2.id;
                        if (channelId == 0) {
                            channelId = message2.to_id.channel_id;
                        }
                        if (message2.to_id.channel_id != 0) {
                            messageId |= ((long) channelId) << 32;
                        }
                        if (i == -2) {
                            SQLiteDatabase access$000 = MessagesStorage.this.database;
                            count2 = count;
                            channelId2 = channelId;
                            Object[] objArr = new Object[1];
                            minChannelMessageId = minId;
                            objArr[0] = Long.valueOf(messageId);
                            count = access$000.queryFinalized(String.format(Locale.US, "SELECT mid, data, ttl, mention, read_state FROM messages WHERE mid = %d", objArr), new Object[0]);
                            boolean next = count.next();
                            boolean exist = next;
                            if (next) {
                                data = count.byteBufferValue(1);
                                if (data != null) {
                                    Message oldMessage = Message.TLdeserialize(data, data.readInt32(false), false);
                                    oldMessage.readAttachPath(data, UserConfig.getInstance(MessagesStorage.this.currentAccount).clientUserId);
                                    data.reuse();
                                    if (oldMessage != null) {
                                        message2.attachPath = oldMessage.attachPath;
                                        message2.ttl = count.intValue(2);
                                    }
                                }
                                boolean oldMention = count.intValue(3) != 0;
                                readState = count.intValue(4);
                                if (oldMention != message2.mentioned) {
                                    minId = mentionCountUpdate2;
                                    if (minId == ConnectionsManager.DEFAULT_DATACENTER_ID) {
                                        SQLiteCursor cursor2 = MessagesStorage.this.database;
                                        i = maxId;
                                        StringBuilder stringBuilder = new StringBuilder();
                                        mentionCountUpdate = minId;
                                        stringBuilder.append("SELECT unread_count_i FROM dialogs WHERE did = ");
                                        message = botKeyboard;
                                        stringBuilder.append(j);
                                        cursor2 = cursor2.queryFinalized(stringBuilder.toString(), new Object[0]);
                                        if (cursor2.next() != null) {
                                            mentionCountUpdate2 = cursor2.intValue(null);
                                        } else {
                                            mentionCountUpdate2 = mentionCountUpdate;
                                        }
                                        cursor2.dispose();
                                        mentionCountUpdate = mentionCountUpdate2;
                                    } else {
                                        message = botKeyboard;
                                        mentionCountUpdate = minId;
                                        i = maxId;
                                    }
                                    if (oldMention) {
                                        if (readState <= 1) {
                                            mentionCountUpdate--;
                                        }
                                    } else if (message2.media_unread != 0) {
                                        mentionCountUpdate++;
                                    }
                                    mentionCountUpdate2 = mentionCountUpdate;
                                    count.dispose();
                                    if (exist) {
                                        count = state3;
                                        botKeyboard = message;
                                        i3 = 3;
                                        readState = 4;
                                        a++;
                                        state = count;
                                        i2 = i3;
                                        i4 = readState;
                                        count = count2;
                                        channelId = channelId2;
                                        minId = minChannelMessageId;
                                        maxId = i;
                                    } else {
                                        mentionCountUpdate = mentionCountUpdate2;
                                    }
                                }
                            }
                            message = botKeyboard;
                            i = maxId;
                            mentionCountUpdate = mentionCountUpdate2;
                            mentionCountUpdate2 = mentionCountUpdate;
                            count.dispose();
                            if (exist) {
                                mentionCountUpdate = mentionCountUpdate2;
                            } else {
                                count = state3;
                                botKeyboard = message;
                                i3 = 3;
                                readState = 4;
                                a++;
                                state = count;
                                i2 = i3;
                                i4 = readState;
                                count = count2;
                                channelId = channelId2;
                                minId = minChannelMessageId;
                                maxId = i;
                            }
                        } else {
                            count2 = count;
                            channelId2 = channelId;
                            message = botKeyboard;
                            minChannelMessageId = minId;
                            i = maxId;
                            mentionCountUpdate = mentionCountUpdate2;
                        }
                        if (a == 0 && z) {
                            maxId = 0;
                            int mentions = 0;
                            count = MessagesStorage.this.database;
                            StringBuilder stringBuilder2 = new StringBuilder();
                            stringBuilder2.append("SELECT pinned, unread_count_i FROM dialogs WHERE did = ");
                            stringBuilder2.append(j);
                            count = count.queryFinalized(stringBuilder2.toString(), new Object[0]);
                            if (count.next()) {
                                maxId = count.intValue(0);
                                mentions = count.intValue(1);
                            }
                            channelId = mentions;
                            count.dispose();
                            SQLitePreparedStatement state32 = MessagesStorage.this.database.executeFast("REPLACE INTO dialogs VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
                            int pinned = maxId;
                            state32.bindLong(1, j);
                            state32.bindInteger(2, message2.date);
                            state32.bindInteger(3, 0);
                            state32.bindLong(4, messageId);
                            state32.bindInteger(5, message2.id);
                            state32.bindInteger(6, 0);
                            state32.bindLong(7, messageId);
                            state32.bindInteger(8, channelId);
                            state32.bindInteger(9, org_telegram_tgnet_TLRPC_messages_Messages.pts);
                            state32.bindInteger(10, message2.date);
                            state32.bindInteger(11, pinned);
                            state32.step();
                            state32.dispose();
                        }
                        MessagesStorage.this.fixUnsupportedMedia(message2);
                        count = state3;
                        count.requery();
                        data = new NativeByteBuffer(message2.getObjectSize());
                        message2.serializeToStream(data);
                        count.bindLong(1, messageId);
                        count.bindLong(2, j);
                        count.bindInteger(3, MessageObject.getUnreadFlags(message2));
                        count.bindInteger(4, message2.send_state);
                        count.bindInteger(5, message2.date);
                        count.bindByteBuffer(6, data);
                        count.bindInteger(7, MessageObject.isOut(message2));
                        count.bindInteger(8, message2.ttl);
                        if ((message2.flags & 1024) != 0) {
                            count.bindInteger(9, message2.views);
                        } else {
                            count.bindInteger(9, MessagesStorage.this.getMessageMediaType(message2));
                        }
                        count.bindInteger(10, 0);
                        count.bindInteger(11, message2.mentioned);
                        count.step();
                        if (DataQuery.canAddMessageToMedia(message2)) {
                            state2.requery();
                            state2.bindLong(1, messageId);
                            state2.bindLong(2, j);
                            i3 = 3;
                            state2.bindInteger(3, message2.date);
                            readState = 4;
                            state2.bindInteger(4, DataQuery.getMediaType(message2));
                            state2.bindByteBuffer(5, data);
                            state2.step();
                        } else {
                            i3 = 3;
                            readState = 4;
                        }
                        data.reuse();
                        if (message2.media instanceof TL_messageMediaWebPage) {
                            if (state5 == null) {
                                state5 = MessagesStorage.this.database.executeFast("REPLACE INTO webpage_pending VALUES(?, ?)");
                            }
                            state5.requery();
                            state5.bindLong(1, message2.media.webpage.id);
                            state5.bindLong(2, messageId);
                            state5.step();
                        }
                        if (i == 0 && MessagesStorage.this.isValidKeyboardToSave(message2)) {
                            if (message != null) {
                                botKeyboard = message;
                                if (botKeyboard.id < message2.id) {
                                }
                            }
                            botKeyboard = message2;
                        } else {
                            botKeyboard = message;
                        }
                        mentionCountUpdate2 = mentionCountUpdate;
                        a++;
                        state = count;
                        i2 = i3;
                        i4 = readState;
                        count = count2;
                        channelId = channelId2;
                        minId = minChannelMessageId;
                        maxId = i;
                    }
                    minChannelMessageId = minId;
                    i = maxId;
                    mentionCountUpdate = mentionCountUpdate2;
                    state.dispose();
                    state2.dispose();
                    if (state5 != null) {
                        state5.dispose();
                    }
                    if (botKeyboard != null) {
                        DataQuery.getInstance(MessagesStorage.this.currentAccount).putBotKeyboard(j, botKeyboard);
                    }
                    MessagesStorage.this.putUsersInternal(org_telegram_tgnet_TLRPC_messages_Messages.users);
                    MessagesStorage.this.putChatsInternal(org_telegram_tgnet_TLRPC_messages_Messages.chats);
                    i2 = mentionCountUpdate;
                    if (i2 != ConnectionsManager.DEFAULT_DATACENTER_ID) {
                        MessagesStorage.this.database.executeFast(String.format(Locale.US, "UPDATE dialogs SET unread_count_i = %d WHERE did = %d", new Object[]{Integer.valueOf(i2), Long.valueOf(j)})).stepThis().dispose();
                        LongSparseArray<Integer> sparseArray = new LongSparseArray(1);
                        sparseArray.put(j, Integer.valueOf(i2));
                        MessagesController.getInstance(MessagesStorage.this.currentAccount).processDialogsUpdateRead(null, sparseArray);
                    }
                    MessagesStorage.this.database.commitTransaction();
                    if (z) {
                        MessagesStorage.this.updateDialogsWithDeletedMessages(new ArrayList(), null, false, channelId);
                    }
                } catch (Throwable e) {
                    FileLog.e(e);
                }
            }
        });
    }

    public static void addUsersAndChatsFromMessage(Message message, ArrayList<Integer> usersToLoad, ArrayList<Integer> chatsToLoad) {
        int a;
        if (message.from_id != 0) {
            if (message.from_id > 0) {
                if (!usersToLoad.contains(Integer.valueOf(message.from_id))) {
                    usersToLoad.add(Integer.valueOf(message.from_id));
                }
            } else if (!chatsToLoad.contains(Integer.valueOf(-message.from_id))) {
                chatsToLoad.add(Integer.valueOf(-message.from_id));
            }
        }
        if (!(message.via_bot_id == 0 || usersToLoad.contains(Integer.valueOf(message.via_bot_id)))) {
            usersToLoad.add(Integer.valueOf(message.via_bot_id));
        }
        int a2 = 0;
        if (message.action != null) {
            if (!(message.action.user_id == 0 || usersToLoad.contains(Integer.valueOf(message.action.user_id)))) {
                usersToLoad.add(Integer.valueOf(message.action.user_id));
            }
            if (!(message.action.channel_id == 0 || chatsToLoad.contains(Integer.valueOf(message.action.channel_id)))) {
                chatsToLoad.add(Integer.valueOf(message.action.channel_id));
            }
            if (!(message.action.chat_id == 0 || chatsToLoad.contains(Integer.valueOf(message.action.chat_id)))) {
                chatsToLoad.add(Integer.valueOf(message.action.chat_id));
            }
            if (!message.action.users.isEmpty()) {
                for (a = 0; a < message.action.users.size(); a++) {
                    Integer uid = (Integer) message.action.users.get(a);
                    if (!usersToLoad.contains(uid)) {
                        usersToLoad.add(uid);
                    }
                }
            }
        }
        if (!message.entities.isEmpty()) {
            while (true) {
                a = a2;
                if (a >= message.entities.size()) {
                    break;
                }
                MessageEntity entity = (MessageEntity) message.entities.get(a);
                if (entity instanceof TL_messageEntityMentionName) {
                    usersToLoad.add(Integer.valueOf(((TL_messageEntityMentionName) entity).user_id));
                } else if (entity instanceof TL_inputMessageEntityMentionName) {
                    usersToLoad.add(Integer.valueOf(((TL_inputMessageEntityMentionName) entity).user_id.user_id));
                }
                a2 = a + 1;
            }
        }
        if (!(message.media == null || message.media.user_id == 0 || usersToLoad.contains(Integer.valueOf(message.media.user_id)))) {
            usersToLoad.add(Integer.valueOf(message.media.user_id));
        }
        if (message.fwd_from != null) {
            if (!(message.fwd_from.from_id == 0 || usersToLoad.contains(Integer.valueOf(message.fwd_from.from_id)))) {
                usersToLoad.add(Integer.valueOf(message.fwd_from.from_id));
            }
            if (!(message.fwd_from.channel_id == 0 || chatsToLoad.contains(Integer.valueOf(message.fwd_from.channel_id)))) {
                chatsToLoad.add(Integer.valueOf(message.fwd_from.channel_id));
            }
            if (message.fwd_from.saved_from_peer != null) {
                if (message.fwd_from.saved_from_peer.user_id != 0) {
                    if (!chatsToLoad.contains(Integer.valueOf(message.fwd_from.saved_from_peer.user_id))) {
                        usersToLoad.add(Integer.valueOf(message.fwd_from.saved_from_peer.user_id));
                    }
                } else if (message.fwd_from.saved_from_peer.channel_id != 0) {
                    if (!chatsToLoad.contains(Integer.valueOf(message.fwd_from.saved_from_peer.channel_id))) {
                        chatsToLoad.add(Integer.valueOf(message.fwd_from.saved_from_peer.channel_id));
                    }
                } else if (!(message.fwd_from.saved_from_peer.chat_id == 0 || chatsToLoad.contains(Integer.valueOf(message.fwd_from.saved_from_peer.chat_id)))) {
                    chatsToLoad.add(Integer.valueOf(message.fwd_from.saved_from_peer.chat_id));
                }
            }
        }
        if (message.ttl < 0 && !chatsToLoad.contains(Integer.valueOf(-message.ttl))) {
            chatsToLoad.add(Integer.valueOf(-message.ttl));
        }
    }

    public void getDialogs(final int offset, final int count) {
        this.storageQueue.postRunnable(new Runnable() {
            public void run() {
                /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.MessagesStorage.90.run():void
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:116)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:249)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:256)
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
                r1 = r29;
                r2 = new org.telegram.tgnet.TLRPC$TL_messages_dialogs;
                r2.<init>();
                r3 = new java.util.ArrayList;
                r3.<init>();
                r12 = r3;
                r3 = new java.util.ArrayList;	 Catch:{ Exception -> 0x03f7 }
                r3.<init>();	 Catch:{ Exception -> 0x03f7 }
                r13 = r3;	 Catch:{ Exception -> 0x03f7 }
                r3 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x03f7 }
                r3 = r3.currentAccount;	 Catch:{ Exception -> 0x03f7 }
                r3 = org.telegram.messenger.UserConfig.getInstance(r3);	 Catch:{ Exception -> 0x03f7 }
                r3 = r3.getClientUserId();	 Catch:{ Exception -> 0x03f7 }
                r3 = java.lang.Integer.valueOf(r3);	 Catch:{ Exception -> 0x03f7 }
                r13.add(r3);	 Catch:{ Exception -> 0x03f7 }
                r3 = new java.util.ArrayList;	 Catch:{ Exception -> 0x03f7 }
                r3.<init>();	 Catch:{ Exception -> 0x03f7 }
                r14 = r3;	 Catch:{ Exception -> 0x03f7 }
                r3 = new java.util.ArrayList;	 Catch:{ Exception -> 0x03f7 }
                r3.<init>();	 Catch:{ Exception -> 0x03f7 }
                r15 = r3;	 Catch:{ Exception -> 0x03f7 }
                r3 = new java.util.ArrayList;	 Catch:{ Exception -> 0x03f7 }
                r3.<init>();	 Catch:{ Exception -> 0x03f7 }
                r11 = r3;	 Catch:{ Exception -> 0x03f7 }
                r3 = new android.util.LongSparseArray;	 Catch:{ Exception -> 0x03f7 }
                r3.<init>();	 Catch:{ Exception -> 0x03f7 }
                r10 = r3;	 Catch:{ Exception -> 0x03f7 }
                r3 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x03f7 }
                r3 = r3.database;	 Catch:{ Exception -> 0x03f7 }
                r4 = java.util.Locale.US;	 Catch:{ Exception -> 0x03f7 }
                r5 = "SELECT d.did, d.last_mid, d.unread_count, d.date, m.data, m.read_state, m.mid, m.send_state, s.flags, m.date, d.pts, d.inbox_max, d.outbox_max, m.replydata, d.pinned, d.unread_count_i FROM dialogs as d LEFT JOIN messages as m ON d.last_mid = m.mid LEFT JOIN dialog_settings as s ON d.did = s.did ORDER BY d.pinned DESC, d.date DESC LIMIT %d,%d";	 Catch:{ Exception -> 0x03f7 }
                r6 = 2;	 Catch:{ Exception -> 0x03f7 }
                r7 = new java.lang.Object[r6];	 Catch:{ Exception -> 0x03f7 }
                r8 = r3;	 Catch:{ Exception -> 0x03f7 }
                r8 = java.lang.Integer.valueOf(r8);	 Catch:{ Exception -> 0x03f7 }
                r9 = 0;	 Catch:{ Exception -> 0x03f7 }
                r7[r9] = r8;	 Catch:{ Exception -> 0x03f7 }
                r8 = r4;	 Catch:{ Exception -> 0x03f7 }
                r8 = java.lang.Integer.valueOf(r8);	 Catch:{ Exception -> 0x03f7 }
                r6 = 1;	 Catch:{ Exception -> 0x03f7 }
                r7[r6] = r8;	 Catch:{ Exception -> 0x03f7 }
                r4 = java.lang.String.format(r4, r5, r7);	 Catch:{ Exception -> 0x03f7 }
                r5 = new java.lang.Object[r9];	 Catch:{ Exception -> 0x03f7 }
                r3 = r3.queryFinalized(r4, r5);	 Catch:{ Exception -> 0x03f7 }
                r4 = r3.next();	 Catch:{ Exception -> 0x03f7 }
                if (r4 == 0) goto L_0x02d1;
            L_0x006f:
                r4 = new org.telegram.tgnet.TLRPC$TL_dialog;	 Catch:{ Exception -> 0x02cb }
                r4.<init>();	 Catch:{ Exception -> 0x02cb }
                r7 = r3.longValue(r9);	 Catch:{ Exception -> 0x02cb }
                r4.id = r7;	 Catch:{ Exception -> 0x02cb }
                r7 = r3.intValue(r6);	 Catch:{ Exception -> 0x02cb }
                r4.top_message = r7;	 Catch:{ Exception -> 0x02cb }
                r7 = 2;	 Catch:{ Exception -> 0x02cb }
                r8 = r3.intValue(r7);	 Catch:{ Exception -> 0x02cb }
                r4.unread_count = r8;	 Catch:{ Exception -> 0x02cb }
                r7 = 3;	 Catch:{ Exception -> 0x02cb }
                r7 = r3.intValue(r7);	 Catch:{ Exception -> 0x02cb }
                r4.last_message_date = r7;	 Catch:{ Exception -> 0x02cb }
                r7 = 10;	 Catch:{ Exception -> 0x02cb }
                r7 = r3.intValue(r7);	 Catch:{ Exception -> 0x02cb }
                r4.pts = r7;	 Catch:{ Exception -> 0x02cb }
                r7 = r4.pts;	 Catch:{ Exception -> 0x02cb }
                if (r7 == 0) goto L_0x00a8;
            L_0x009a:
                r7 = r4.id;	 Catch:{ Exception -> 0x00a2 }
                r7 = (int) r7;
                if (r7 <= 0) goto L_0x00a0;
            L_0x009f:
                goto L_0x00a8;
            L_0x00a0:
                r7 = r6;
                goto L_0x00a9;
            L_0x00a2:
                r0 = move-exception;
                r28 = r2;
                r2 = r0;
                goto L_0x03fb;
            L_0x00a8:
                r7 = r9;
            L_0x00a9:
                r4.flags = r7;	 Catch:{ Exception -> 0x02cb }
                r7 = 11;	 Catch:{ Exception -> 0x02cb }
                r7 = r3.intValue(r7);	 Catch:{ Exception -> 0x02cb }
                r4.read_inbox_max_id = r7;	 Catch:{ Exception -> 0x02cb }
                r7 = 12;	 Catch:{ Exception -> 0x02cb }
                r7 = r3.intValue(r7);	 Catch:{ Exception -> 0x02cb }
                r4.read_outbox_max_id = r7;	 Catch:{ Exception -> 0x02cb }
                r7 = 14;	 Catch:{ Exception -> 0x02cb }
                r7 = r3.intValue(r7);	 Catch:{ Exception -> 0x02cb }
                r4.pinnedNum = r7;	 Catch:{ Exception -> 0x02cb }
                r7 = r4.pinnedNum;	 Catch:{ Exception -> 0x02cb }
                if (r7 == 0) goto L_0x00c9;	 Catch:{ Exception -> 0x02cb }
            L_0x00c7:
                r7 = r6;	 Catch:{ Exception -> 0x02cb }
                goto L_0x00ca;	 Catch:{ Exception -> 0x02cb }
            L_0x00c9:
                r7 = r9;	 Catch:{ Exception -> 0x02cb }
            L_0x00ca:
                r4.pinned = r7;	 Catch:{ Exception -> 0x02cb }
                r7 = 15;	 Catch:{ Exception -> 0x02cb }
                r7 = r3.intValue(r7);	 Catch:{ Exception -> 0x02cb }
                r4.unread_mentions_count = r7;	 Catch:{ Exception -> 0x02cb }
                r7 = 8;	 Catch:{ Exception -> 0x02cb }
                r7 = r3.longValue(r7);	 Catch:{ Exception -> 0x02cb }
                r6 = (int) r7;	 Catch:{ Exception -> 0x02cb }
                r5 = new org.telegram.tgnet.TLRPC$TL_peerNotifySettings;	 Catch:{ Exception -> 0x02cb }
                r5.<init>();	 Catch:{ Exception -> 0x02cb }
                r4.notify_settings = r5;	 Catch:{ Exception -> 0x02cb }
                r5 = r6 & 1;
                r17 = 32;
                if (r5 == 0) goto L_0x00ff;
            L_0x00e8:
                r5 = r4.notify_settings;	 Catch:{ Exception -> 0x00a2 }
                r20 = r10;	 Catch:{ Exception -> 0x00a2 }
                r9 = r7 >> r17;	 Catch:{ Exception -> 0x00a2 }
                r9 = (int) r9;	 Catch:{ Exception -> 0x00a2 }
                r5.mute_until = r9;	 Catch:{ Exception -> 0x00a2 }
                r5 = r4.notify_settings;	 Catch:{ Exception -> 0x00a2 }
                r5 = r5.mute_until;	 Catch:{ Exception -> 0x00a2 }
                if (r5 != 0) goto L_0x0101;	 Catch:{ Exception -> 0x00a2 }
            L_0x00f7:
                r5 = r4.notify_settings;	 Catch:{ Exception -> 0x00a2 }
                r9 = 2147483647; // 0x7fffffff float:NaN double:1.060997895E-314;	 Catch:{ Exception -> 0x00a2 }
                r5.mute_until = r9;	 Catch:{ Exception -> 0x00a2 }
                goto L_0x0101;
            L_0x00ff:
                r20 = r10;
            L_0x0101:
                r5 = r2.dialogs;	 Catch:{ Exception -> 0x02cb }
                r5.add(r4);	 Catch:{ Exception -> 0x02cb }
                r5 = 4;	 Catch:{ Exception -> 0x02cb }
                r5 = r3.byteBufferValue(r5);	 Catch:{ Exception -> 0x02cb }
                if (r5 == 0) goto L_0x0255;	 Catch:{ Exception -> 0x02cb }
            L_0x010d:
                r9 = 0;	 Catch:{ Exception -> 0x02cb }
                r10 = r5.readInt32(r9);	 Catch:{ Exception -> 0x02cb }
                r10 = org.telegram.tgnet.TLRPC.Message.TLdeserialize(r5, r10, r9);	 Catch:{ Exception -> 0x02cb }
                r9 = r10;	 Catch:{ Exception -> 0x02cb }
                r10 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x02cb }
                r10 = r10.currentAccount;	 Catch:{ Exception -> 0x02cb }
                r10 = org.telegram.messenger.UserConfig.getInstance(r10);	 Catch:{ Exception -> 0x02cb }
                r10 = r10.clientUserId;	 Catch:{ Exception -> 0x02cb }
                r9.readAttachPath(r5, r10);	 Catch:{ Exception -> 0x02cb }
                r5.reuse();	 Catch:{ Exception -> 0x02cb }
                if (r9 == 0) goto L_0x0255;	 Catch:{ Exception -> 0x02cb }
            L_0x012b:
                r10 = 5;	 Catch:{ Exception -> 0x02cb }
                r10 = r3.intValue(r10);	 Catch:{ Exception -> 0x02cb }
                org.telegram.messenger.MessageObject.setUnreadFlags(r9, r10);	 Catch:{ Exception -> 0x02cb }
                r10 = 6;	 Catch:{ Exception -> 0x02cb }
                r10 = r3.intValue(r10);	 Catch:{ Exception -> 0x02cb }
                r9.id = r10;	 Catch:{ Exception -> 0x02cb }
                r10 = 9;	 Catch:{ Exception -> 0x02cb }
                r10 = r3.intValue(r10);	 Catch:{ Exception -> 0x02cb }
                if (r10 == 0) goto L_0x0144;
            L_0x0142:
                r4.last_message_date = r10;	 Catch:{ Exception -> 0x00a2 }
            L_0x0144:
                r21 = r5;
                r5 = 7;
                r5 = r3.intValue(r5);	 Catch:{ Exception -> 0x02cb }
                r9.send_state = r5;	 Catch:{ Exception -> 0x02cb }
                r22 = r6;	 Catch:{ Exception -> 0x02cb }
                r5 = r4.id;	 Catch:{ Exception -> 0x02cb }
                r9.dialog_id = r5;	 Catch:{ Exception -> 0x02cb }
                r5 = r2.messages;	 Catch:{ Exception -> 0x02cb }
                r5.add(r9);	 Catch:{ Exception -> 0x02cb }
                org.telegram.messenger.MessagesStorage.addUsersAndChatsFromMessage(r9, r13, r14);	 Catch:{ Exception -> 0x02cb }
                r5 = r9.reply_to_msg_id;	 Catch:{ Exception -> 0x0247 }
                if (r5 == 0) goto L_0x023e;	 Catch:{ Exception -> 0x0247 }
            L_0x015f:
                r5 = r9.action;	 Catch:{ Exception -> 0x0247 }
                r5 = r5 instanceof org.telegram.tgnet.TLRPC.TL_messageActionPinMessage;	 Catch:{ Exception -> 0x0247 }
                if (r5 != 0) goto L_0x0186;
            L_0x0165:
                r5 = r9.action;	 Catch:{ Exception -> 0x017a }
                r5 = r5 instanceof org.telegram.tgnet.TLRPC.TL_messageActionPaymentSent;	 Catch:{ Exception -> 0x017a }
                if (r5 != 0) goto L_0x0186;	 Catch:{ Exception -> 0x017a }
            L_0x016b:
                r5 = r9.action;	 Catch:{ Exception -> 0x017a }
                r5 = r5 instanceof org.telegram.tgnet.TLRPC.TL_messageActionGameScore;	 Catch:{ Exception -> 0x017a }
                if (r5 == 0) goto L_0x0172;
            L_0x0171:
                goto L_0x0186;
            L_0x0172:
                r25 = r2;
                r23 = r7;
                r8 = r20;
                goto L_0x0244;
            L_0x017a:
                r0 = move-exception;
                r1 = r0;
                r25 = r2;
                r23 = r7;
                r8 = r20;
                r5 = r21;
                goto L_0x0251;
            L_0x0186:
                r5 = 13;
                r6 = r3.isNull(r5);	 Catch:{ Exception -> 0x0247 }
                if (r6 != 0) goto L_0x01e5;
            L_0x018e:
                r5 = r3.byteBufferValue(r5);	 Catch:{ Exception -> 0x01d9 }
                if (r5 == 0) goto L_0x01d6;
            L_0x0194:
                r23 = r7;
                r6 = 0;
                r7 = r5.readInt32(r6);	 Catch:{ Exception -> 0x01ce }
                r7 = org.telegram.tgnet.TLRPC.Message.TLdeserialize(r5, r7, r6);	 Catch:{ Exception -> 0x01ce }
                r9.replyMessage = r7;	 Catch:{ Exception -> 0x01ce }
                r6 = r9.replyMessage;	 Catch:{ Exception -> 0x01ce }
                r7 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x01ce }
                r7 = r7.currentAccount;	 Catch:{ Exception -> 0x01ce }
                r7 = org.telegram.messenger.UserConfig.getInstance(r7);	 Catch:{ Exception -> 0x01ce }
                r7 = r7.clientUserId;	 Catch:{ Exception -> 0x01ce }
                r6.readAttachPath(r5, r7);	 Catch:{ Exception -> 0x01ce }
                r5.reuse();	 Catch:{ Exception -> 0x01ce }
                r6 = r9.replyMessage;	 Catch:{ Exception -> 0x01ce }
                if (r6 == 0) goto L_0x01e9;	 Catch:{ Exception -> 0x01ce }
            L_0x01b9:
                r6 = org.telegram.messenger.MessageObject.isMegagroup(r9);	 Catch:{ Exception -> 0x01ce }
                if (r6 == 0) goto L_0x01c8;	 Catch:{ Exception -> 0x01ce }
            L_0x01bf:
                r6 = r9.replyMessage;	 Catch:{ Exception -> 0x01ce }
                r7 = r6.flags;	 Catch:{ Exception -> 0x01ce }
                r8 = -2147483648; // 0xffffffff80000000 float:-0.0 double:NaN;	 Catch:{ Exception -> 0x01ce }
                r7 = r7 | r8;	 Catch:{ Exception -> 0x01ce }
                r6.flags = r7;	 Catch:{ Exception -> 0x01ce }
            L_0x01c8:
                r6 = r9.replyMessage;	 Catch:{ Exception -> 0x01ce }
                org.telegram.messenger.MessagesStorage.addUsersAndChatsFromMessage(r6, r13, r14);	 Catch:{ Exception -> 0x01ce }
                goto L_0x01e9;
            L_0x01ce:
                r0 = move-exception;
                r1 = r0;
                r25 = r2;
            L_0x01d2:
                r8 = r20;
                goto L_0x0251;
            L_0x01d6:
                r23 = r7;
                goto L_0x01e9;
            L_0x01d9:
                r0 = move-exception;
                r23 = r7;
                r1 = r0;
                r25 = r2;
                r8 = r20;
                r5 = r21;
                goto L_0x0251;
            L_0x01e5:
                r23 = r7;
                r5 = r21;
            L_0x01e9:
                r6 = r9.replyMessage;	 Catch:{ Exception -> 0x0237 }
                if (r6 != 0) goto L_0x0232;	 Catch:{ Exception -> 0x0237 }
            L_0x01ed:
                r6 = r9.reply_to_msg_id;	 Catch:{ Exception -> 0x0237 }
                r6 = (long) r6;	 Catch:{ Exception -> 0x0237 }
                r8 = r9.to_id;	 Catch:{ Exception -> 0x0237 }
                r8 = r8.channel_id;	 Catch:{ Exception -> 0x0237 }
                if (r8 == 0) goto L_0x020b;
            L_0x01f6:
                r8 = r9.to_id;	 Catch:{ Exception -> 0x0204 }
                r8 = r8.channel_id;	 Catch:{ Exception -> 0x0204 }
                r25 = r2;
                r1 = (long) r8;
                r1 = r1 << r17;
                r26 = r6 | r1;
                r6 = r26;
                goto L_0x020d;
            L_0x0204:
                r0 = move-exception;
                r25 = r2;
                r1 = r0;
                r8 = r20;
                goto L_0x0251;
            L_0x020b:
                r25 = r2;
            L_0x020d:
                r1 = java.lang.Long.valueOf(r6);	 Catch:{ Exception -> 0x022d }
                r1 = r11.contains(r1);	 Catch:{ Exception -> 0x022d }
                if (r1 != 0) goto L_0x0222;
            L_0x0217:
                r1 = java.lang.Long.valueOf(r6);	 Catch:{ Exception -> 0x021f }
                r11.add(r1);	 Catch:{ Exception -> 0x021f }
                goto L_0x0222;
            L_0x021f:
                r0 = move-exception;
                r1 = r0;
                goto L_0x01d2;
            L_0x0222:
                r1 = r4.id;	 Catch:{ Exception -> 0x022d }
                r8 = r20;
                r8.put(r1, r9);	 Catch:{ Exception -> 0x022a }
                goto L_0x0246;
            L_0x022a:
                r0 = move-exception;
                r1 = r0;
                goto L_0x0251;
            L_0x022d:
                r0 = move-exception;
                r8 = r20;
                r1 = r0;
                goto L_0x0251;
            L_0x0232:
                r25 = r2;
                r8 = r20;
                goto L_0x0246;
            L_0x0237:
                r0 = move-exception;
                r25 = r2;
                r8 = r20;
                r1 = r0;
                goto L_0x0251;
            L_0x023e:
                r25 = r2;
                r23 = r7;
                r8 = r20;
            L_0x0244:
                r5 = r21;
            L_0x0246:
                goto L_0x0261;
            L_0x0247:
                r0 = move-exception;
                r25 = r2;
                r23 = r7;
                r8 = r20;
                r1 = r0;
                r5 = r21;
            L_0x0251:
                org.telegram.messenger.FileLog.e(r1);	 Catch:{ Exception -> 0x02c3 }
                goto L_0x0261;	 Catch:{ Exception -> 0x02c3 }
            L_0x0255:
                r25 = r2;	 Catch:{ Exception -> 0x02c3 }
                r21 = r5;	 Catch:{ Exception -> 0x02c3 }
                r22 = r6;	 Catch:{ Exception -> 0x02c3 }
                r23 = r7;	 Catch:{ Exception -> 0x02c3 }
                r8 = r20;	 Catch:{ Exception -> 0x02c3 }
                r5 = r21;	 Catch:{ Exception -> 0x02c3 }
            L_0x0261:
                r1 = r4.id;	 Catch:{ Exception -> 0x02c3 }
                r1 = (int) r1;	 Catch:{ Exception -> 0x02c3 }
                r6 = r4.id;	 Catch:{ Exception -> 0x02c3 }
                r6 = r6 >> r17;	 Catch:{ Exception -> 0x02c3 }
                r2 = (int) r6;	 Catch:{ Exception -> 0x02c3 }
                if (r1 == 0) goto L_0x02a8;	 Catch:{ Exception -> 0x02c3 }
            L_0x026b:
                r6 = 1;	 Catch:{ Exception -> 0x02c3 }
                if (r2 != r6) goto L_0x0280;	 Catch:{ Exception -> 0x02c3 }
            L_0x026e:
                r6 = java.lang.Integer.valueOf(r1);	 Catch:{ Exception -> 0x02c3 }
                r6 = r14.contains(r6);	 Catch:{ Exception -> 0x02c3 }
                if (r6 != 0) goto L_0x02b9;	 Catch:{ Exception -> 0x02c3 }
            L_0x0278:
                r6 = java.lang.Integer.valueOf(r1);	 Catch:{ Exception -> 0x02c3 }
                r14.add(r6);	 Catch:{ Exception -> 0x02c3 }
                goto L_0x02b9;	 Catch:{ Exception -> 0x02c3 }
            L_0x0280:
                if (r1 <= 0) goto L_0x0294;	 Catch:{ Exception -> 0x02c3 }
                r6 = java.lang.Integer.valueOf(r1);	 Catch:{ Exception -> 0x02c3 }
                r6 = r13.contains(r6);	 Catch:{ Exception -> 0x02c3 }
                if (r6 != 0) goto L_0x02b9;	 Catch:{ Exception -> 0x02c3 }
                r6 = java.lang.Integer.valueOf(r1);	 Catch:{ Exception -> 0x02c3 }
                r13.add(r6);	 Catch:{ Exception -> 0x02c3 }
                goto L_0x02b9;	 Catch:{ Exception -> 0x02c3 }
                r6 = -r1;	 Catch:{ Exception -> 0x02c3 }
                r6 = java.lang.Integer.valueOf(r6);	 Catch:{ Exception -> 0x02c3 }
                r6 = r14.contains(r6);	 Catch:{ Exception -> 0x02c3 }
                if (r6 != 0) goto L_0x02b9;	 Catch:{ Exception -> 0x02c3 }
                r6 = -r1;	 Catch:{ Exception -> 0x02c3 }
                r6 = java.lang.Integer.valueOf(r6);	 Catch:{ Exception -> 0x02c3 }
                r14.add(r6);	 Catch:{ Exception -> 0x02c3 }
                goto L_0x02b9;	 Catch:{ Exception -> 0x02c3 }
            L_0x02a8:
                r6 = java.lang.Integer.valueOf(r2);	 Catch:{ Exception -> 0x02c3 }
                r6 = r15.contains(r6);	 Catch:{ Exception -> 0x02c3 }
                if (r6 != 0) goto L_0x02b9;	 Catch:{ Exception -> 0x02c3 }
                r6 = java.lang.Integer.valueOf(r2);	 Catch:{ Exception -> 0x02c3 }
                r15.add(r6);	 Catch:{ Exception -> 0x02c3 }
                r10 = r8;
                r2 = r25;
                r1 = r29;
                r6 = 1;
                r9 = 0;
                goto L_0x0069;
            L_0x02c3:
                r0 = move-exception;
                r2 = r0;
                r28 = r25;
                r1 = r29;
                goto L_0x03fb;
            L_0x02cb:
                r0 = move-exception;
                r28 = r2;
                r2 = r0;
                goto L_0x03fb;
            L_0x02d1:
                r25 = r2;
                r8 = r10;
                r3.dispose();	 Catch:{ Exception -> 0x03f0 }
                r1 = r11.isEmpty();	 Catch:{ Exception -> 0x03f0 }
                if (r1 != 0) goto L_0x036c;
                r1 = r29;
                r2 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x0366 }
                r2 = r2.database;	 Catch:{ Exception -> 0x0366 }
                r4 = java.util.Locale.US;	 Catch:{ Exception -> 0x0366 }
                r5 = "SELECT data, mid, date, uid FROM messages WHERE mid IN(%s)";	 Catch:{ Exception -> 0x0366 }
                r6 = 1;	 Catch:{ Exception -> 0x0366 }
                r7 = new java.lang.Object[r6];	 Catch:{ Exception -> 0x0366 }
                r6 = ",";	 Catch:{ Exception -> 0x0366 }
                r6 = android.text.TextUtils.join(r6, r11);	 Catch:{ Exception -> 0x0366 }
                r9 = 0;	 Catch:{ Exception -> 0x0366 }
                r7[r9] = r6;	 Catch:{ Exception -> 0x0366 }
                r4 = java.lang.String.format(r4, r5, r7);	 Catch:{ Exception -> 0x0366 }
                r5 = new java.lang.Object[r9];	 Catch:{ Exception -> 0x0366 }
                r2 = r2.queryFinalized(r4, r5);	 Catch:{ Exception -> 0x0366 }
                r3 = r2;	 Catch:{ Exception -> 0x0366 }
                r2 = r3.next();	 Catch:{ Exception -> 0x0366 }
                if (r2 == 0) goto L_0x0362;	 Catch:{ Exception -> 0x0366 }
                r2 = 0;	 Catch:{ Exception -> 0x0366 }
                r4 = r3.byteBufferValue(r2);	 Catch:{ Exception -> 0x0366 }
                if (r4 == 0) goto L_0x035f;	 Catch:{ Exception -> 0x0366 }
                r5 = r4.readInt32(r2);	 Catch:{ Exception -> 0x0366 }
                r5 = org.telegram.tgnet.TLRPC.Message.TLdeserialize(r4, r5, r2);	 Catch:{ Exception -> 0x0366 }
                r6 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x0366 }
                r6 = r6.currentAccount;	 Catch:{ Exception -> 0x0366 }
                r6 = org.telegram.messenger.UserConfig.getInstance(r6);	 Catch:{ Exception -> 0x0366 }
                r6 = r6.clientUserId;	 Catch:{ Exception -> 0x0366 }
                r5.readAttachPath(r4, r6);	 Catch:{ Exception -> 0x0366 }
                r4.reuse();	 Catch:{ Exception -> 0x0366 }
                r6 = 1;	 Catch:{ Exception -> 0x0366 }
                r7 = r3.intValue(r6);	 Catch:{ Exception -> 0x0366 }
                r5.id = r7;	 Catch:{ Exception -> 0x0366 }
                r7 = 2;	 Catch:{ Exception -> 0x0366 }
                r9 = r3.intValue(r7);	 Catch:{ Exception -> 0x0366 }
                r5.date = r9;	 Catch:{ Exception -> 0x0366 }
                r9 = 3;	 Catch:{ Exception -> 0x0366 }
                r6 = r3.longValue(r9);	 Catch:{ Exception -> 0x0366 }
                r5.dialog_id = r6;	 Catch:{ Exception -> 0x0366 }
                org.telegram.messenger.MessagesStorage.addUsersAndChatsFromMessage(r5, r13, r14);	 Catch:{ Exception -> 0x0366 }
                r6 = r5.dialog_id;	 Catch:{ Exception -> 0x0366 }
                r6 = r8.get(r6);	 Catch:{ Exception -> 0x0366 }
                r6 = (org.telegram.tgnet.TLRPC.Message) r6;	 Catch:{ Exception -> 0x0366 }
                if (r6 == 0) goto L_0x035f;	 Catch:{ Exception -> 0x0366 }
                r6.replyMessage = r5;	 Catch:{ Exception -> 0x0366 }
                r9 = r6.dialog_id;	 Catch:{ Exception -> 0x0366 }
                r5.dialog_id = r9;	 Catch:{ Exception -> 0x0366 }
                r7 = org.telegram.messenger.MessageObject.isMegagroup(r6);	 Catch:{ Exception -> 0x0366 }
                if (r7 == 0) goto L_0x035f;	 Catch:{ Exception -> 0x0366 }
                r7 = r6.replyMessage;	 Catch:{ Exception -> 0x0366 }
                r9 = r7.flags;	 Catch:{ Exception -> 0x0366 }
                r10 = -2147483648; // 0xffffffff80000000 float:-0.0 double:NaN;	 Catch:{ Exception -> 0x0366 }
                r9 = r9 | r10;	 Catch:{ Exception -> 0x0366 }
                r7.flags = r9;	 Catch:{ Exception -> 0x0366 }
                goto L_0x0361;	 Catch:{ Exception -> 0x0366 }
                r10 = -2147483648; // 0xffffffff80000000 float:-0.0 double:NaN;	 Catch:{ Exception -> 0x0366 }
                goto L_0x0300;	 Catch:{ Exception -> 0x0366 }
                r3.dispose();	 Catch:{ Exception -> 0x0366 }
                goto L_0x036e;
            L_0x0366:
                r0 = move-exception;
                r2 = r0;
                r28 = r25;
                goto L_0x03fb;
                r1 = r29;
                r2 = r3;
                r3 = r15.isEmpty();	 Catch:{ Exception -> 0x03ec }
                if (r3 != 0) goto L_0x0380;
                r3 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x0366 }
                r4 = ",";	 Catch:{ Exception -> 0x0366 }
                r4 = android.text.TextUtils.join(r4, r15);	 Catch:{ Exception -> 0x0366 }
                r3.getEncryptedChatsInternal(r4, r12, r13);	 Catch:{ Exception -> 0x0366 }
                r3 = r14.isEmpty();	 Catch:{ Exception -> 0x03ec }
                if (r3 != 0) goto L_0x039c;
                r3 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x0396 }
                r4 = ",";	 Catch:{ Exception -> 0x0396 }
                r4 = android.text.TextUtils.join(r4, r14);	 Catch:{ Exception -> 0x0396 }
                r10 = r25;
                r5 = r10.chats;	 Catch:{ Exception -> 0x03b2 }
                r3.getChatsInternal(r4, r5);	 Catch:{ Exception -> 0x03b2 }
                goto L_0x039e;
            L_0x0396:
                r0 = move-exception;
                r2 = r0;
                r28 = r25;
                goto L_0x03fb;
                r10 = r25;
                r3 = r13.isEmpty();	 Catch:{ Exception -> 0x03e7 }
                if (r3 != 0) goto L_0x03b7;
                r3 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x03b2 }
                r4 = ",";	 Catch:{ Exception -> 0x03b2 }
                r4 = android.text.TextUtils.join(r4, r13);	 Catch:{ Exception -> 0x03b2 }
                r5 = r10.users;	 Catch:{ Exception -> 0x03b2 }
                r3.getUsersInternal(r4, r5);	 Catch:{ Exception -> 0x03b2 }
                goto L_0x03b7;
            L_0x03b2:
                r0 = move-exception;
                r2 = r0;
                r28 = r10;
                goto L_0x03fb;
                r3 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x03e7 }
                r3 = r3.currentAccount;	 Catch:{ Exception -> 0x03e7 }
                r3 = org.telegram.messenger.MessagesController.getInstance(r3);	 Catch:{ Exception -> 0x03e7 }
                r6 = r3;	 Catch:{ Exception -> 0x03e7 }
                r7 = r4;	 Catch:{ Exception -> 0x03e7 }
                r9 = 1;
                r16 = 0;
                r17 = 0;
                r18 = 1;
                r4 = r10;
                r5 = r12;
                r19 = r8;
                r8 = r9;
                r9 = r16;
                r28 = r10;
                r16 = r19;
                r10 = r17;
                r17 = r11;
                r11 = r18;
                r3.processLoadedDialogs(r4, r5, r6, r7, r8, r9, r10, r11);	 Catch:{ Exception -> 0x03e4 }
                r13 = r28;
                goto L_0x0428;
            L_0x03e4:
                r0 = move-exception;
                goto L_0x00a5;
            L_0x03e7:
                r0 = move-exception;
                r28 = r10;
                r2 = r0;
                goto L_0x03fb;
            L_0x03ec:
                r0 = move-exception;
                r28 = r25;
                goto L_0x03f5;
            L_0x03f0:
                r0 = move-exception;
                r28 = r25;
                r1 = r29;
                r2 = r0;
                goto L_0x03fb;
            L_0x03f7:
                r0 = move-exception;
                r28 = r2;
                r2 = r0;
            L_0x03fb:
                r13 = r28;
                r3 = r13.dialogs;
                r3.clear();
                r3 = r13.users;
                r3.clear();
                r3 = r13.chats;
                r3.clear();
                r12.clear();
                org.telegram.messenger.FileLog.e(r2);
                r3 = org.telegram.messenger.MessagesStorage.this;
                r3 = r3.currentAccount;
                r3 = org.telegram.messenger.MessagesController.getInstance(r3);
                r6 = 0;
                r7 = 100;
                r8 = 1;
                r9 = 1;
                r10 = 0;
                r11 = 1;
                r4 = r13;
                r5 = r12;
                r3.processLoadedDialogs(r4, r5, r6, r7, r8, r9, r10, r11);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.MessagesStorage.90.run():void");
            }
        });
    }

    public static void createFirstHoles(long did, SQLitePreparedStatement state5, SQLitePreparedStatement state6, int messageId) throws Exception {
        state5.requery();
        state5.bindLong(1, did);
        state5.bindInteger(2, messageId == 1 ? 1 : 0);
        state5.bindInteger(3, messageId);
        state5.step();
        for (int b = 0; b < 5; b++) {
            state6.requery();
            state6.bindLong(1, did);
            state6.bindInteger(2, b);
            state6.bindInteger(3, messageId == 1 ? 1 : 0);
            state6.bindInteger(4, messageId);
            state6.step();
        }
    }

    private void putDialogsInternal(messages_Dialogs dialogs, boolean check) {
        MessagesStorage messagesStorage;
        Throwable e;
        Throwable e2;
        messages_Dialogs org_telegram_tgnet_TLRPC_messages_Dialogs = dialogs;
        messages_Dialogs org_telegram_tgnet_TLRPC_messages_Dialogs2;
        MessagesStorage new_dialogMessage;
        try {
            this.database.beginTransaction();
            LongSparseArray<Message> new_dialogMessage2 = new LongSparseArray(org_telegram_tgnet_TLRPC_messages_Dialogs.messages.size());
            for (int a = 0; a < org_telegram_tgnet_TLRPC_messages_Dialogs.messages.size(); a++) {
                Message message = (Message) org_telegram_tgnet_TLRPC_messages_Dialogs.messages.get(a);
                new_dialogMessage2.put(MessageObject.getDialogId(message), message);
            }
            LongSparseArray<Message> longSparseArray;
            if (org_telegram_tgnet_TLRPC_messages_Dialogs.dialogs.isEmpty()) {
                longSparseArray = new_dialogMessage2;
            } else {
                SQLitePreparedStatement state = messagesStorage.database.executeFast("REPLACE INTO messages VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, NULL, ?, ?)");
                SQLitePreparedStatement state2 = messagesStorage.database.executeFast("REPLACE INTO dialogs VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
                SQLitePreparedStatement state3 = messagesStorage.database.executeFast("REPLACE INTO media_v2 VALUES(?, ?, ?, ?, ?)");
                SQLitePreparedStatement state4 = messagesStorage.database.executeFast("REPLACE INTO dialog_settings VALUES(?, ?)");
                SQLitePreparedStatement state5 = messagesStorage.database.executeFast("REPLACE INTO messages_holes VALUES(?, ?, ?)");
                SQLitePreparedStatement state6 = messagesStorage.database.executeFast("REPLACE INTO media_holes_v2 VALUES(?, ?, ?, ?)");
                int a2 = 0;
                while (a2 < org_telegram_tgnet_TLRPC_messages_Dialogs.dialogs.size()) {
                    Object state7;
                    TL_dialog dialog = (TL_dialog) org_telegram_tgnet_TLRPC_messages_Dialogs.dialogs.get(a2);
                    SQLitePreparedStatement state8 = state;
                    if (dialog.id == 0) {
                        if (dialog.peer.user_id != 0) {
                            dialog.id = (long) dialog.peer.user_id;
                        } else if (dialog.peer.chat_id != 0) {
                            dialog.id = (long) (-dialog.peer.chat_id);
                        } else {
                            dialog.id = (long) (-dialog.peer.channel_id);
                        }
                    }
                    if (check) {
                        SQLiteDatabase sQLiteDatabase = messagesStorage.database;
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("SELECT did FROM dialogs WHERE did = ");
                        stringBuilder.append(dialog.id);
                        SQLiteCursor cursor = sQLiteDatabase.queryFinalized(stringBuilder.toString(), new Object[0]);
                        boolean exists = cursor.next();
                        cursor.dispose();
                        if (exists) {
                            longSparseArray = new_dialogMessage2;
                            new_dialogMessage2 = state8;
                            a2++;
                            state7 = new_dialogMessage2;
                            new_dialogMessage2 = longSparseArray;
                            messagesStorage = this;
                            org_telegram_tgnet_TLRPC_messages_Dialogs = dialogs;
                        }
                    }
                    int messageDate = 0;
                    Message message2 = (Message) new_dialogMessage2.get(dialog.id);
                    if (message2 != null) {
                        int messageDate2;
                        messageDate = Math.max(message2.date, 0);
                        if (messagesStorage.isValidKeyboardToSave(message2)) {
                            DataQuery.getInstance(messagesStorage.currentAccount).putBotKeyboard(dialog.id, message2);
                        }
                        messagesStorage.fixUnsupportedMedia(message2);
                        NativeByteBuffer data = new NativeByteBuffer(message2.getObjectSize());
                        message2.serializeToStream(data);
                        long messageId = (long) message2.id;
                        longSparseArray = new_dialogMessage2;
                        if (message2.to_id.channel_id != null) {
                            messageDate2 = messageDate;
                            messageId |= ((long) message2.to_id.channel_id) << 32;
                        } else {
                            messageDate2 = messageDate;
                        }
                        new_dialogMessage2 = state8;
                        new_dialogMessage2.requery();
                        new_dialogMessage2.bindLong(1, messageId);
                        try {
                            new_dialogMessage2.bindLong(2, dialog.id);
                            new_dialogMessage2.bindInteger(3, MessageObject.getUnreadFlags(message2));
                            new_dialogMessage2.bindInteger(4, message2.send_state);
                            new_dialogMessage2.bindInteger(5, message2.date);
                            new_dialogMessage2.bindByteBuffer(6, data);
                            new_dialogMessage2.bindInteger(7, MessageObject.isOut(message2));
                            new_dialogMessage2.bindInteger(8, 0);
                            new_dialogMessage2.bindInteger(9, (message2.flags & 1024) != 0 ? message2.views : 0);
                            new_dialogMessage2.bindInteger(10, 0);
                            new_dialogMessage2.bindInteger(11, message2.mentioned);
                            new_dialogMessage2.step();
                            if (DataQuery.canAddMessageToMedia(message2)) {
                                state3.requery();
                                state3.bindLong(1, messageId);
                                state3.bindLong(2, dialog.id);
                                state3.bindInteger(3, message2.date);
                                state3.bindInteger(4, DataQuery.getMediaType(message2));
                                state3.bindByteBuffer(5, data);
                                state3.step();
                            }
                            data.reuse();
                            createFirstHoles(dialog.id, state5, state6, message2.id);
                            messageDate = messageDate2;
                        } catch (Throwable e22) {
                            e = e22;
                            org_telegram_tgnet_TLRPC_messages_Dialogs2 = dialogs;
                            new_dialogMessage = this;
                        }
                    } else {
                        longSparseArray = new_dialogMessage2;
                        new_dialogMessage2 = state8;
                    }
                    long topMessage = (long) dialog.top_message;
                    if (dialog.peer.channel_id != 0) {
                        topMessage |= ((long) dialog.peer.channel_id) << 32;
                    }
                    state2.requery();
                    state2.bindLong(1, dialog.id);
                    state2.bindInteger(2, messageDate);
                    state2.bindInteger(3, dialog.unread_count);
                    state2.bindLong(4, topMessage);
                    state2.bindInteger(5, dialog.read_inbox_max_id);
                    state2.bindInteger(6, dialog.read_outbox_max_id);
                    state2.bindLong(7, 0);
                    state2.bindInteger(8, dialog.unread_mentions_count);
                    state2.bindInteger(9, dialog.pts);
                    state2.bindInteger(10, 0);
                    state2.bindInteger(11, dialog.pinnedNum);
                    state2.step();
                    if (dialog.notify_settings != null) {
                        state4.requery();
                        int i = 1;
                        state4.bindLong(1, dialog.id);
                        if (dialog.notify_settings.mute_until == 0) {
                            i = 0;
                        }
                        state4.bindInteger(2, i);
                        state4.step();
                    }
                    a2++;
                    state7 = new_dialogMessage2;
                    new_dialogMessage2 = longSparseArray;
                    messagesStorage = this;
                    org_telegram_tgnet_TLRPC_messages_Dialogs = dialogs;
                }
                state.dispose();
                state2.dispose();
                state3.dispose();
                state4.dispose();
                state5.dispose();
                state6.dispose();
            }
            org_telegram_tgnet_TLRPC_messages_Dialogs2 = dialogs;
            try {
            } catch (Exception e3) {
                e22 = e3;
                new_dialogMessage = this;
                e = e22;
                FileLog.e(e);
            }
            try {
                putUsersInternal(org_telegram_tgnet_TLRPC_messages_Dialogs2.users);
                putChatsInternal(org_telegram_tgnet_TLRPC_messages_Dialogs2.chats);
                new_dialogMessage.database.commitTransaction();
            } catch (Exception e4) {
                e22 = e4;
                e = e22;
                FileLog.e(e);
            }
        } catch (Exception e5) {
            e22 = e5;
            new_dialogMessage = messagesStorage;
            org_telegram_tgnet_TLRPC_messages_Dialogs2 = org_telegram_tgnet_TLRPC_messages_Dialogs;
            e = e22;
            FileLog.e(e);
        }
    }

    public void unpinAllDialogsExceptNew(final ArrayList<Long> dids) {
        this.storageQueue.postRunnable(new Runnable() {
            public void run() {
                try {
                    ArrayList<Long> unpinnedDialogs = new ArrayList();
                    SQLiteCursor cursor = MessagesStorage.this.database.queryFinalized(String.format(Locale.US, "SELECT did FROM dialogs WHERE pinned != 0 AND did NOT IN (%s)", new Object[]{TextUtils.join(",", dids)}), new Object[0]);
                    while (cursor.next()) {
                        if (((int) cursor.longValue(0)) != 0) {
                            unpinnedDialogs.add(Long.valueOf(cursor.longValue(0)));
                        }
                    }
                    cursor.dispose();
                    if (!unpinnedDialogs.isEmpty()) {
                        SQLitePreparedStatement state = MessagesStorage.this.database.executeFast("UPDATE dialogs SET pinned = ? WHERE did = ?");
                        for (int a = 0; a < unpinnedDialogs.size(); a++) {
                            long did = ((Long) unpinnedDialogs.get(a)).longValue();
                            state.requery();
                            state.bindInteger(1, 0);
                            state.bindLong(2, did);
                            state.step();
                        }
                        state.dispose();
                    }
                } catch (Throwable e) {
                    FileLog.e(e);
                }
            }
        });
    }

    public void setDialogPinned(final long did, final int pinned) {
        this.storageQueue.postRunnable(new Runnable() {
            public void run() {
                try {
                    SQLitePreparedStatement state = MessagesStorage.this.database.executeFast("UPDATE dialogs SET pinned = ? WHERE did = ?");
                    state.bindInteger(1, pinned);
                    state.bindLong(2, did);
                    state.step();
                    state.dispose();
                } catch (Throwable e) {
                    FileLog.e(e);
                }
            }
        });
    }

    public void putDialogs(final messages_Dialogs dialogs, final boolean check) {
        if (!dialogs.dialogs.isEmpty()) {
            this.storageQueue.postRunnable(new Runnable() {
                public void run() {
                    MessagesStorage.this.putDialogsInternal(dialogs, check);
                    try {
                        MessagesStorage.this.loadUnreadMessages();
                    } catch (Throwable e) {
                        FileLog.e(e);
                    }
                }
            });
        }
    }

    public int getDialogReadMax(boolean outbox, long dialog_id) {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        Integer[] max = new Integer[]{Integer.valueOf(0)};
        final boolean z = outbox;
        final long j = dialog_id;
        final Integer[] numArr = max;
        final CountDownLatch countDownLatch2 = countDownLatch;
        this.storageQueue.postRunnable(new Runnable() {
            /* JADX WARNING: inconsistent code. */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public void run() {
                /*
                r6 = this;
                r0 = 0;
                r1 = r4;	 Catch:{ Exception -> 0x0061 }
                r2 = 0;
                if (r1 == 0) goto L_0x0027;
            L_0x0006:
                r1 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x0061 }
                r1 = r1.database;	 Catch:{ Exception -> 0x0061 }
                r3 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0061 }
                r3.<init>();	 Catch:{ Exception -> 0x0061 }
                r4 = "SELECT outbox_max FROM dialogs WHERE did = ";
                r3.append(r4);	 Catch:{ Exception -> 0x0061 }
                r4 = r5;	 Catch:{ Exception -> 0x0061 }
                r3.append(r4);	 Catch:{ Exception -> 0x0061 }
                r3 = r3.toString();	 Catch:{ Exception -> 0x0061 }
                r4 = new java.lang.Object[r2];	 Catch:{ Exception -> 0x0061 }
                r1 = r1.queryFinalized(r3, r4);	 Catch:{ Exception -> 0x0061 }
                r0 = r1;
                goto L_0x0047;
            L_0x0027:
                r1 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x0061 }
                r1 = r1.database;	 Catch:{ Exception -> 0x0061 }
                r3 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0061 }
                r3.<init>();	 Catch:{ Exception -> 0x0061 }
                r4 = "SELECT inbox_max FROM dialogs WHERE did = ";
                r3.append(r4);	 Catch:{ Exception -> 0x0061 }
                r4 = r5;	 Catch:{ Exception -> 0x0061 }
                r3.append(r4);	 Catch:{ Exception -> 0x0061 }
                r3 = r3.toString();	 Catch:{ Exception -> 0x0061 }
                r4 = new java.lang.Object[r2];	 Catch:{ Exception -> 0x0061 }
                r1 = r1.queryFinalized(r3, r4);	 Catch:{ Exception -> 0x0061 }
                r0 = r1;
            L_0x0047:
                r1 = r0.next();	 Catch:{ Exception -> 0x0061 }
                if (r1 == 0) goto L_0x0059;
            L_0x004d:
                r1 = r7;	 Catch:{ Exception -> 0x0061 }
                r3 = r0.intValue(r2);	 Catch:{ Exception -> 0x0061 }
                r3 = java.lang.Integer.valueOf(r3);	 Catch:{ Exception -> 0x0061 }
                r1[r2] = r3;	 Catch:{ Exception -> 0x0061 }
            L_0x0059:
                if (r0 == 0) goto L_0x0068;
            L_0x005b:
                r0.dispose();
                goto L_0x0068;
            L_0x005f:
                r1 = move-exception;
                goto L_0x006e;
            L_0x0061:
                r1 = move-exception;
                org.telegram.messenger.FileLog.e(r1);	 Catch:{ all -> 0x005f }
                if (r0 == 0) goto L_0x0068;
            L_0x0067:
                goto L_0x005b;
            L_0x0068:
                r1 = r8;
                r1.countDown();
                return;
            L_0x006e:
                if (r0 == 0) goto L_0x0073;
            L_0x0070:
                r0.dispose();
            L_0x0073:
                throw r1;
                */
                throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.MessagesStorage.94.run():void");
            }
        });
        try {
            countDownLatch.await();
        } catch (Throwable e) {
            FileLog.e(e);
        }
        return max[0].intValue();
    }

    public int getChannelPtsSync(final int channelId) {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        final Integer[] pts = new Integer[]{Integer.valueOf(0)};
        this.storageQueue.postRunnable(new Runnable() {
            /* JADX WARNING: inconsistent code. */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public void run() {
                /*
                r5 = this;
                r0 = 0;
                r1 = org.telegram.messenger.MessagesStorage.this;	 Catch:{ Exception -> 0x003d }
                r1 = r1.database;	 Catch:{ Exception -> 0x003d }
                r2 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x003d }
                r2.<init>();	 Catch:{ Exception -> 0x003d }
                r3 = "SELECT pts FROM dialogs WHERE did = ";
                r2.append(r3);	 Catch:{ Exception -> 0x003d }
                r3 = r6;	 Catch:{ Exception -> 0x003d }
                r3 = -r3;
                r2.append(r3);	 Catch:{ Exception -> 0x003d }
                r2 = r2.toString();	 Catch:{ Exception -> 0x003d }
                r3 = 0;
                r4 = new java.lang.Object[r3];	 Catch:{ Exception -> 0x003d }
                r1 = r1.queryFinalized(r2, r4);	 Catch:{ Exception -> 0x003d }
                r0 = r1;
                r1 = r0.next();	 Catch:{ Exception -> 0x003d }
                if (r1 == 0) goto L_0x0035;
            L_0x0029:
                r1 = r1;	 Catch:{ Exception -> 0x003d }
                r2 = r0.intValue(r3);	 Catch:{ Exception -> 0x003d }
                r2 = java.lang.Integer.valueOf(r2);	 Catch:{ Exception -> 0x003d }
                r1[r3] = r2;	 Catch:{ Exception -> 0x003d }
            L_0x0035:
                if (r0 == 0) goto L_0x0044;
            L_0x0037:
                r0.dispose();
                goto L_0x0044;
            L_0x003b:
                r1 = move-exception;
                goto L_0x0053;
            L_0x003d:
                r1 = move-exception;
                org.telegram.messenger.FileLog.e(r1);	 Catch:{ all -> 0x003b }
                if (r0 == 0) goto L_0x0044;
            L_0x0043:
                goto L_0x0037;
            L_0x0044:
                r1 = r0;	 Catch:{ Exception -> 0x004e }
                if (r1 == 0) goto L_0x004d;
            L_0x0048:
                r1 = r0;	 Catch:{ Exception -> 0x004e }
                r1.countDown();	 Catch:{ Exception -> 0x004e }
            L_0x004d:
                goto L_0x0052;
            L_0x004e:
                r1 = move-exception;
                org.telegram.messenger.FileLog.e(r1);
            L_0x0052:
                return;
            L_0x0053:
                if (r0 == 0) goto L_0x0058;
            L_0x0055:
                r0.dispose();
            L_0x0058:
                throw r1;
                */
                throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.MessagesStorage.95.run():void");
            }
        });
        try {
            countDownLatch.await();
        } catch (Throwable e) {
            FileLog.e(e);
        }
        return pts[0].intValue();
    }

    public User getUserSync(final int user_id) {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        final User[] user = new User[1];
        this.storageQueue.postRunnable(new Runnable() {
            public void run() {
                user[0] = MessagesStorage.this.getUser(user_id);
                countDownLatch.countDown();
            }
        });
        try {
            countDownLatch.await();
        } catch (Throwable e) {
            FileLog.e(e);
        }
        return user[0];
    }

    public Chat getChatSync(final int chat_id) {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        final Chat[] chat = new Chat[1];
        this.storageQueue.postRunnable(new Runnable() {
            public void run() {
                chat[0] = MessagesStorage.this.getChat(chat_id);
                countDownLatch.countDown();
            }
        });
        try {
            countDownLatch.await();
        } catch (Throwable e) {
            FileLog.e(e);
        }
        return chat[0];
    }

    public User getUser(int user_id) {
        User user = null;
        try {
            ArrayList<User> users = new ArrayList();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(TtmlNode.ANONYMOUS_REGION_ID);
            stringBuilder.append(user_id);
            getUsersInternal(stringBuilder.toString(), users);
            if (!users.isEmpty()) {
                user = (User) users.get(0);
            }
        } catch (Throwable e) {
            FileLog.e(e);
        }
        return user;
    }

    public ArrayList<User> getUsers(ArrayList<Integer> uids) {
        ArrayList<User> users = new ArrayList();
        try {
            getUsersInternal(TextUtils.join(",", uids), users);
        } catch (Throwable e) {
            users.clear();
            FileLog.e(e);
        }
        return users;
    }

    public Chat getChat(int chat_id) {
        Chat chat = null;
        try {
            ArrayList<Chat> chats = new ArrayList();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(TtmlNode.ANONYMOUS_REGION_ID);
            stringBuilder.append(chat_id);
            getChatsInternal(stringBuilder.toString(), chats);
            if (!chats.isEmpty()) {
                chat = (Chat) chats.get(0);
            }
        } catch (Throwable e) {
            FileLog.e(e);
        }
        return chat;
    }

    public EncryptedChat getEncryptedChat(int chat_id) {
        EncryptedChat chat = null;
        try {
            ArrayList<EncryptedChat> encryptedChats = new ArrayList();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(TtmlNode.ANONYMOUS_REGION_ID);
            stringBuilder.append(chat_id);
            getEncryptedChatsInternal(stringBuilder.toString(), encryptedChats, null);
            if (!encryptedChats.isEmpty()) {
                chat = (EncryptedChat) encryptedChats.get(0);
            }
        } catch (Throwable e) {
            FileLog.e(e);
        }
        return chat;
    }
}
