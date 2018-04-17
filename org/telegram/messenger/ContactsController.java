package org.telegram.messenger;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentProviderOperation;
import android.content.ContentProviderOperation.Builder;
import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.RawContacts;
import android.text.TextUtils;
import android.util.SparseArray;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import org.telegram.PhoneFormat.PhoneFormat;
import org.telegram.messenger.beta.R;
import org.telegram.messenger.support.SparseLongArray;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.InputUser;
import org.telegram.tgnet.TLRPC.PrivacyRule;
import org.telegram.tgnet.TLRPC.TL_account_getAccountTTL;
import org.telegram.tgnet.TLRPC.TL_account_getPrivacy;
import org.telegram.tgnet.TLRPC.TL_account_privacyRules;
import org.telegram.tgnet.TLRPC.TL_contact;
import org.telegram.tgnet.TLRPC.TL_contactStatus;
import org.telegram.tgnet.TLRPC.TL_contacts_contactsNotModified;
import org.telegram.tgnet.TLRPC.TL_contacts_deleteContacts;
import org.telegram.tgnet.TLRPC.TL_contacts_getContacts;
import org.telegram.tgnet.TLRPC.TL_contacts_getStatuses;
import org.telegram.tgnet.TLRPC.TL_contacts_importContacts;
import org.telegram.tgnet.TLRPC.TL_contacts_importedContacts;
import org.telegram.tgnet.TLRPC.TL_contacts_resetSaved;
import org.telegram.tgnet.TLRPC.TL_error;
import org.telegram.tgnet.TLRPC.TL_help_getInviteText;
import org.telegram.tgnet.TLRPC.TL_help_inviteText;
import org.telegram.tgnet.TLRPC.TL_inputPhoneContact;
import org.telegram.tgnet.TLRPC.TL_inputPrivacyKeyChatInvite;
import org.telegram.tgnet.TLRPC.TL_inputPrivacyKeyPhoneCall;
import org.telegram.tgnet.TLRPC.TL_inputPrivacyKeyStatusTimestamp;
import org.telegram.tgnet.TLRPC.TL_user;
import org.telegram.tgnet.TLRPC.TL_userStatusLastMonth;
import org.telegram.tgnet.TLRPC.TL_userStatusLastWeek;
import org.telegram.tgnet.TLRPC.TL_userStatusRecently;
import org.telegram.tgnet.TLRPC.User;
import org.telegram.tgnet.TLRPC.Vector;
import org.telegram.tgnet.TLRPC.contacts_Contacts;

public class ContactsController {
    private static volatile ContactsController[] Instance = new ContactsController[3];
    private ArrayList<PrivacyRule> callPrivacyRules;
    private int completedRequestsCount;
    public ArrayList<TL_contact> contacts = new ArrayList();
    public HashMap<String, Contact> contactsBook = new HashMap();
    private boolean contactsBookLoaded;
    public HashMap<String, Contact> contactsBookSPhones = new HashMap();
    public HashMap<String, TL_contact> contactsByPhone = new HashMap();
    public HashMap<String, TL_contact> contactsByShortPhone = new HashMap();
    public ConcurrentHashMap<Integer, TL_contact> contactsDict = new ConcurrentHashMap(20, 1.0f, 2);
    public boolean contactsLoaded;
    private boolean contactsSyncInProgress;
    private int currentAccount;
    private ArrayList<Integer> delayedContactsUpdate = new ArrayList();
    private int deleteAccountTTL;
    private ArrayList<PrivacyRule> groupPrivacyRules;
    private boolean ignoreChanges;
    private String inviteLink;
    private String lastContactsVersions = TtmlNode.ANONYMOUS_REGION_ID;
    private final Object loadContactsSync = new Object();
    private int loadingCallsInfo;
    private boolean loadingContacts;
    private int loadingDeleteInfo;
    private int loadingGroupInfo;
    private int loadingLastSeenInfo;
    private boolean migratingContacts;
    private final Object observerLock = new Object();
    public ArrayList<Contact> phoneBookContacts = new ArrayList();
    private ArrayList<PrivacyRule> privacyRules;
    private String[] projectionNames = new String[]{"lookup", "data2", "data3", "data5"};
    private String[] projectionPhones = new String[]{"lookup", "data1", "data2", "data3", "display_name", "account_type"};
    private HashMap<String, String> sectionsToReplace = new HashMap();
    public ArrayList<String> sortedUsersMutualSectionsArray = new ArrayList();
    public ArrayList<String> sortedUsersSectionsArray = new ArrayList();
    private Account systemAccount;
    private boolean updatingInviteLink;
    public HashMap<String, ArrayList<TL_contact>> usersMutualSectionsDict = new HashMap();
    public HashMap<String, ArrayList<TL_contact>> usersSectionsDict = new HashMap();

    class AnonymousClass17 implements Runnable {
        final /* synthetic */ Integer val$uid;

        AnonymousClass17(Integer num) {
            this.val$uid = num;
        }

        public void run() {
            ContactsController.this.deleteContactFromPhoneBook(this.val$uid.intValue());
        }
    }

    class AnonymousClass19 implements Runnable {
        final /* synthetic */ ArrayList val$contactsToDelete;
        final /* synthetic */ ArrayList val$newContacts;

        AnonymousClass19(ArrayList arrayList, ArrayList arrayList2) {
            this.val$newContacts = arrayList;
            this.val$contactsToDelete = arrayList2;
        }

        public void run() {
            int a;
            for (a = 0; a < this.val$newContacts.size(); a++) {
                TL_contact contact = (TL_contact) this.val$newContacts.get(a);
                if (ContactsController.this.contactsDict.get(Integer.valueOf(contact.user_id)) == null) {
                    ContactsController.this.contacts.add(contact);
                    ContactsController.this.contactsDict.put(Integer.valueOf(contact.user_id), contact);
                }
            }
            for (a = 0; a < this.val$contactsToDelete.size(); a++) {
                Integer uid = (Integer) this.val$contactsToDelete.get(a);
                TL_contact contact2 = (TL_contact) ContactsController.this.contactsDict.get(uid);
                if (contact2 != null) {
                    ContactsController.this.contacts.remove(contact2);
                    ContactsController.this.contactsDict.remove(uid);
                }
            }
            if (!this.val$newContacts.isEmpty()) {
                ContactsController.this.updateUnregisteredContacts(ContactsController.this.contacts);
                ContactsController.this.performWriteContactsToPhoneBook();
            }
            ContactsController.this.performSyncPhoneBook(ContactsController.this.getContactsCopy(ContactsController.this.contactsBook), false, false, false, false, true, false);
            ContactsController.this.buildContactsSectionsArrays(this.val$newContacts.isEmpty() ^ 1);
            NotificationCenter.getInstance(ContactsController.this.currentAccount).postNotificationName(NotificationCenter.contactsDidLoaded, new Object[0]);
        }
    }

    public static class Contact {
        public int contact_id;
        public String first_name;
        public int imported;
        public boolean isGoodProvider;
        public String key;
        public String last_name;
        public boolean namesFilled;
        public ArrayList<Integer> phoneDeleted = new ArrayList(4);
        public ArrayList<String> phoneTypes = new ArrayList(4);
        public ArrayList<String> phones = new ArrayList(4);
        public String provider;
        public ArrayList<String> shortPhones = new ArrayList(4);
    }

    private void applyContactsUpdates(java.util.ArrayList<java.lang.Integer> r1, java.util.concurrent.ConcurrentHashMap<java.lang.Integer, org.telegram.tgnet.TLRPC.User> r2, java.util.ArrayList<org.telegram.tgnet.TLRPC.TL_contact> r3, java.util.ArrayList<java.lang.Integer> r4) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.ContactsController.applyContactsUpdates(java.util.ArrayList, java.util.concurrent.ConcurrentHashMap, java.util.ArrayList, java.util.ArrayList):void
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
        r0 = r17;
        r1 = r19;
        r2 = 0;
        if (r20 == 0) goto L_0x0011;
    L_0x0007:
        if (r21 != 0) goto L_0x000a;
    L_0x0009:
        goto L_0x0011;
    L_0x000a:
        r6 = r18;
        r3 = r20;
        r4 = r21;
        goto L_0x0058;
    L_0x0011:
        r5 = new java.util.ArrayList;
        r5.<init>();
        r3 = r5;
        r5 = new java.util.ArrayList;
        r5.<init>();
        r4 = r5;
        r5 = r2;
    L_0x001e:
        r6 = r18.size();
        if (r5 >= r6) goto L_0x0056;
    L_0x0024:
        r6 = r18;
        r7 = r6.get(r5);
        r7 = (java.lang.Integer) r7;
        r8 = r7.intValue();
        if (r8 <= 0) goto L_0x0041;
    L_0x0032:
        r8 = new org.telegram.tgnet.TLRPC$TL_contact;
        r8.<init>();
        r9 = r7.intValue();
        r8.user_id = r9;
        r3.add(r8);
        goto L_0x0053;
    L_0x0041:
        r8 = r7.intValue();
        if (r8 >= 0) goto L_0x0053;
    L_0x0047:
        r8 = r7.intValue();
        r8 = -r8;
        r8 = java.lang.Integer.valueOf(r8);
        r4.add(r8);
    L_0x0053:
        r5 = r5 + 1;
        goto L_0x001e;
    L_0x0056:
        r6 = r18;
    L_0x0058:
        r5 = org.telegram.messenger.BuildVars.LOGS_ENABLED;
        if (r5 == 0) goto L_0x0080;
    L_0x005c:
        r5 = new java.lang.StringBuilder;
        r5.<init>();
        r7 = "process update - contacts add = ";
        r5.append(r7);
        r7 = r3.size();
        r5.append(r7);
        r7 = " delete = ";
        r5.append(r7);
        r7 = r4.size();
        r5.append(r7);
        r5 = r5.toString();
        org.telegram.messenger.FileLog.d(r5);
    L_0x0080:
        r5 = new java.lang.StringBuilder;
        r5.<init>();
        r7 = new java.lang.StringBuilder;
        r7.<init>();
        r8 = 0;
        r9 = r8;
        r8 = r2;
        r10 = r3.size();
        r11 = -1;
        r12 = 1;
        if (r8 >= r10) goto L_0x0107;
    L_0x0095:
        r10 = r3.get(r8);
        r10 = (org.telegram.tgnet.TLRPC.TL_contact) r10;
        r13 = 0;
        if (r1 == 0) goto L_0x00ab;
        r14 = r10.user_id;
        r14 = java.lang.Integer.valueOf(r14);
        r14 = r1.get(r14);
        r13 = r14;
        r13 = (org.telegram.tgnet.TLRPC.User) r13;
        if (r13 != 0) goto L_0x00be;
        r12 = r0.currentAccount;
        r12 = org.telegram.messenger.MessagesController.getInstance(r12);
        r14 = r10.user_id;
        r14 = java.lang.Integer.valueOf(r14);
        r13 = r12.getUser(r14);
        goto L_0x00c7;
        r14 = r0.currentAccount;
        r14 = org.telegram.messenger.MessagesController.getInstance(r14);
        r14.putUser(r13, r12);
        if (r13 == 0) goto L_0x0102;
        r12 = r13.phone;
        r12 = android.text.TextUtils.isEmpty(r12);
        if (r12 == 0) goto L_0x00d2;
        goto L_0x0102;
        r12 = r0.contactsBookSPhones;
        r14 = r13.phone;
        r12 = r12.get(r14);
        r12 = (org.telegram.messenger.ContactsController.Contact) r12;
        if (r12 == 0) goto L_0x00f1;
        r14 = r12.shortPhones;
        r15 = r13.phone;
        r14 = r14.indexOf(r15);
        if (r14 == r11) goto L_0x00f1;
        r11 = r12.phoneDeleted;
        r15 = java.lang.Integer.valueOf(r2);
        r11.set(r14, r15);
        r11 = r5.length();
        if (r11 == 0) goto L_0x00fc;
        r11 = ",";
        r5.append(r11);
        r11 = r13.phone;
        r5.append(r11);
        goto L_0x0104;
        r9 = 1;
        r8 = r8 + 1;
        goto L_0x008d;
        r8 = r4.size();
        if (r2 >= r8) goto L_0x017d;
        r8 = r4.get(r2);
        r8 = (java.lang.Integer) r8;
        r10 = org.telegram.messenger.Utilities.phoneBookQueue;
        r13 = new org.telegram.messenger.ContactsController$17;
        r13.<init>(r8);
        r10.postRunnable(r13);
        r10 = 0;
        if (r1 == 0) goto L_0x0128;
        r13 = r1.get(r8);
        r10 = r13;
        r10 = (org.telegram.tgnet.TLRPC.User) r10;
        if (r10 != 0) goto L_0x0135;
        r13 = r0.currentAccount;
        r13 = org.telegram.messenger.MessagesController.getInstance(r13);
        r10 = r13.getUser(r8);
        goto L_0x013e;
        r13 = r0.currentAccount;
        r13 = org.telegram.messenger.MessagesController.getInstance(r13);
        r13.putUser(r10, r12);
        if (r10 != 0) goto L_0x0142;
        r9 = 1;
        goto L_0x0179;
        r13 = r10.phone;
        r13 = android.text.TextUtils.isEmpty(r13);
        if (r13 != 0) goto L_0x0179;
        r13 = r0.contactsBookSPhones;
        r14 = r10.phone;
        r13 = r13.get(r14);
        r13 = (org.telegram.messenger.ContactsController.Contact) r13;
        if (r13 == 0) goto L_0x0169;
        r14 = r13.shortPhones;
        r15 = r10.phone;
        r14 = r14.indexOf(r15);
        if (r14 == r11) goto L_0x0169;
        r15 = r13.phoneDeleted;
        r11 = java.lang.Integer.valueOf(r12);
        r15.set(r14, r11);
        r11 = r7.length();
        if (r11 == 0) goto L_0x0174;
        r11 = ",";
        r7.append(r11);
        r11 = r10.phone;
        r7.append(r11);
        r2 = r2 + 1;
        r11 = -1;
        goto L_0x0108;
        r2 = r5.length();
        if (r2 != 0) goto L_0x0189;
        r2 = r7.length();
        if (r2 == 0) goto L_0x019a;
        r2 = r0.currentAccount;
        r2 = org.telegram.messenger.MessagesStorage.getInstance(r2);
        r8 = r5.toString();
        r10 = r7.toString();
        r2.applyPhoneBookUpdates(r8, r10);
        if (r9 == 0) goto L_0x01a7;
        r2 = org.telegram.messenger.Utilities.stageQueue;
        r8 = new org.telegram.messenger.ContactsController$18;
        r8.<init>();
        r2.postRunnable(r8);
        goto L_0x01b1;
        r2 = r3;
        r8 = r4;
        r10 = new org.telegram.messenger.ContactsController$19;
        r10.<init>(r2, r8);
        org.telegram.messenger.AndroidUtilities.runOnUIThread(r10);
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.ContactsController.applyContactsUpdates(java.util.ArrayList, java.util.concurrent.ConcurrentHashMap, java.util.ArrayList, java.util.ArrayList):void");
    }

    private boolean hasContactsPermission() {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.ContactsController.hasContactsPermission():boolean
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
        r0 = android.os.Build.VERSION.SDK_INT;
        r1 = 0;
        r2 = 1;
        r3 = 23;
        if (r0 < r3) goto L_0x0015;
    L_0x0008:
        r0 = org.telegram.messenger.ApplicationLoader.applicationContext;
        r3 = "android.permission.READ_CONTACTS";
        r0 = r0.checkSelfPermission(r3);
        if (r0 != 0) goto L_0x0014;
    L_0x0012:
        r1 = r2;
    L_0x0014:
        return r1;
    L_0x0015:
        r0 = 0;
        r3 = org.telegram.messenger.ApplicationLoader.applicationContext;	 Catch:{ Throwable -> 0x004d }
        r4 = r3.getContentResolver();	 Catch:{ Throwable -> 0x004d }
        r5 = android.provider.ContactsContract.CommonDataKinds.Phone.CONTENT_URI;	 Catch:{ Throwable -> 0x004d }
        r6 = r10.projectionPhones;	 Catch:{ Throwable -> 0x004d }
        r7 = 0;	 Catch:{ Throwable -> 0x004d }
        r8 = 0;	 Catch:{ Throwable -> 0x004d }
        r9 = 0;	 Catch:{ Throwable -> 0x004d }
        r3 = r4.query(r5, r6, r7, r8, r9);	 Catch:{ Throwable -> 0x004d }
        r0 = r3;	 Catch:{ Throwable -> 0x004d }
        if (r0 == 0) goto L_0x003d;	 Catch:{ Throwable -> 0x004d }
    L_0x002a:
        r3 = r0.getCount();	 Catch:{ Throwable -> 0x004d }
        if (r3 != 0) goto L_0x0031;
        goto L_0x003d;
        if (r0 == 0) goto L_0x003c;
        r0.close();	 Catch:{ Exception -> 0x0037 }
        goto L_0x003c;
    L_0x0037:
        r1 = move-exception;
        org.telegram.messenger.FileLog.e(r1);
        goto L_0x0057;
        goto L_0x0057;
        if (r0 == 0) goto L_0x0049;
        r0.close();	 Catch:{ Exception -> 0x0044 }
        goto L_0x0049;
    L_0x0044:
        r2 = move-exception;
        org.telegram.messenger.FileLog.e(r2);
        goto L_0x004a;
        return r1;
    L_0x004b:
        r1 = move-exception;
        goto L_0x0058;
    L_0x004d:
        r1 = move-exception;
        org.telegram.messenger.FileLog.e(r1);	 Catch:{ all -> 0x004b }
        if (r0 == 0) goto L_0x003c;
        r0.close();	 Catch:{ Exception -> 0x0037 }
        goto L_0x003c;
        return r2;
        if (r0 == 0) goto L_0x0064;
        r0.close();	 Catch:{ Exception -> 0x005f }
        goto L_0x0064;
    L_0x005f:
        r2 = move-exception;
        org.telegram.messenger.FileLog.e(r2);
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.ContactsController.hasContactsPermission():boolean");
    }

    public static ContactsController getInstance(int num) {
        ContactsController localInstance = Instance[num];
        if (localInstance == null) {
            synchronized (ContactsController.class) {
                localInstance = Instance[num];
                if (localInstance == null) {
                    ContactsController[] contactsControllerArr = Instance;
                    ContactsController contactsController = new ContactsController(num);
                    localInstance = contactsController;
                    contactsControllerArr[num] = contactsController;
                }
            }
        }
        return localInstance;
    }

    public ContactsController(int instance) {
        this.currentAccount = instance;
        if (MessagesController.getMainSettings(this.currentAccount).getBoolean("needGetStatuses", false)) {
            reloadContactsStatuses();
        }
        this.sectionsToReplace.put("À", "A");
        this.sectionsToReplace.put("Á", "A");
        this.sectionsToReplace.put("Ä", "A");
        this.sectionsToReplace.put("Ù", "U");
        this.sectionsToReplace.put("Ú", "U");
        this.sectionsToReplace.put("Ü", "U");
        this.sectionsToReplace.put("Ì", "I");
        this.sectionsToReplace.put("Í", "I");
        this.sectionsToReplace.put("Ï", "I");
        this.sectionsToReplace.put("È", "E");
        this.sectionsToReplace.put("É", "E");
        this.sectionsToReplace.put("Ê", "E");
        this.sectionsToReplace.put("Ë", "E");
        this.sectionsToReplace.put("Ò", "O");
        this.sectionsToReplace.put("Ó", "O");
        this.sectionsToReplace.put("Ö", "O");
        this.sectionsToReplace.put("Ç", "C");
        this.sectionsToReplace.put("Ñ", "N");
        this.sectionsToReplace.put("Ÿ", "Y");
        this.sectionsToReplace.put("Ý", "Y");
        this.sectionsToReplace.put("Ţ", "Y");
    }

    public void cleanup() {
        this.contactsBook.clear();
        this.contactsBookSPhones.clear();
        this.phoneBookContacts.clear();
        this.contacts.clear();
        this.contactsDict.clear();
        this.usersSectionsDict.clear();
        this.usersMutualSectionsDict.clear();
        this.sortedUsersSectionsArray.clear();
        this.sortedUsersMutualSectionsArray.clear();
        this.delayedContactsUpdate.clear();
        this.contactsByPhone.clear();
        this.contactsByShortPhone.clear();
        this.loadingContacts = false;
        this.contactsSyncInProgress = false;
        this.contactsLoaded = false;
        this.contactsBookLoaded = false;
        this.lastContactsVersions = TtmlNode.ANONYMOUS_REGION_ID;
        this.loadingDeleteInfo = 0;
        this.deleteAccountTTL = 0;
        this.loadingLastSeenInfo = 0;
        this.loadingGroupInfo = 0;
        this.loadingCallsInfo = 0;
        Utilities.globalQueue.postRunnable(new Runnable() {
            public void run() {
                ContactsController.this.migratingContacts = false;
                ContactsController.this.completedRequestsCount = 0;
            }
        });
        this.privacyRules = null;
    }

    public void checkInviteText() {
        SharedPreferences preferences = MessagesController.getMainSettings(this.currentAccount);
        this.inviteLink = preferences.getString("invitelink", null);
        int time = preferences.getInt("invitelinktime", 0);
        if (!this.updatingInviteLink) {
            if (this.inviteLink == null || Math.abs((System.currentTimeMillis() / 1000) - ((long) time)) >= 86400) {
                this.updatingInviteLink = true;
                ConnectionsManager.getInstance(this.currentAccount).sendRequest(new TL_help_getInviteText(), new RequestDelegate() {
                    public void run(TLObject response, TL_error error) {
                        if (response != null) {
                            final TL_help_inviteText res = (TL_help_inviteText) response;
                            if (res.message.length() != 0) {
                                AndroidUtilities.runOnUIThread(new Runnable() {
                                    public void run() {
                                        ContactsController.this.updatingInviteLink = false;
                                        Editor editor = MessagesController.getMainSettings(ContactsController.this.currentAccount).edit();
                                        editor.putString("invitelink", ContactsController.this.inviteLink = res.message);
                                        editor.putInt("invitelinktime", (int) (System.currentTimeMillis() / 1000));
                                        editor.commit();
                                    }
                                });
                            }
                        }
                    }
                }, 2);
            }
        }
    }

    public String getInviteText(int contacts) {
        String link = this.inviteLink == null ? "https://telegram.org/dl" : this.inviteLink;
        if (contacts <= 1) {
            return LocaleController.formatString("InviteText2", R.string.InviteText2, link);
        }
        try {
            return String.format(LocaleController.getPluralString("InviteTextNum", contacts), new Object[]{Integer.valueOf(contacts), link});
        } catch (Exception e) {
            return LocaleController.formatString("InviteText2", R.string.InviteText2, link);
        }
    }

    public void checkAppAccount() {
        AccountManager am = AccountManager.get(ApplicationLoader.applicationContext);
        try {
            Account[] accounts = am.getAccountsByType("org.telegram.messenger");
            this.systemAccount = null;
            for (int a = 0; a < accounts.length; a++) {
                Account acc = accounts[a];
                boolean found = false;
                for (int b = 0; b < 3; b++) {
                    User user = UserConfig.getInstance(b).getCurrentUser();
                    if (user != null) {
                        String str = acc.name;
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append(TtmlNode.ANONYMOUS_REGION_ID);
                        stringBuilder.append(user.id);
                        if (str.equals(stringBuilder.toString())) {
                            if (b == this.currentAccount) {
                                this.systemAccount = acc;
                            }
                            found = true;
                            if (!found) {
                                try {
                                    am.removeAccount(accounts[a], null, null);
                                } catch (Exception e) {
                                }
                            }
                        }
                    }
                }
                if (!found) {
                    am.removeAccount(accounts[a], null, null);
                }
            }
        } catch (Throwable th) {
        }
        if (UserConfig.getInstance(this.currentAccount).isClientActivated()) {
            readContacts();
            if (this.systemAccount == null) {
                try {
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append(TtmlNode.ANONYMOUS_REGION_ID);
                    stringBuilder2.append(UserConfig.getInstance(this.currentAccount).getClientUserId());
                    this.systemAccount = new Account(stringBuilder2.toString(), "org.telegram.messenger");
                    am.addAccountExplicitly(this.systemAccount, TtmlNode.ANONYMOUS_REGION_ID, null);
                } catch (Exception e2) {
                }
            }
        }
    }

    public void deleteUnknownAppAccounts() {
        try {
            this.systemAccount = null;
            AccountManager am = AccountManager.get(ApplicationLoader.applicationContext);
            Account[] accounts = am.getAccountsByType("org.telegram.messenger");
            for (int a = 0; a < accounts.length; a++) {
                Account acc = accounts[a];
                boolean found = false;
                for (int b = 0; b < 3; b++) {
                    User user = UserConfig.getInstance(b).getCurrentUser();
                    if (user != null) {
                        String str = acc.name;
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append(TtmlNode.ANONYMOUS_REGION_ID);
                        stringBuilder.append(user.id);
                        if (str.equals(stringBuilder.toString())) {
                            found = true;
                            break;
                        }
                    }
                }
                if (!found) {
                    try {
                        am.removeAccount(accounts[a], null, null);
                    } catch (Exception e) {
                    }
                }
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    public void checkContacts() {
        Utilities.globalQueue.postRunnable(new Runnable() {
            public void run() {
                if (ContactsController.this.checkContactsInternal()) {
                    if (BuildVars.LOGS_ENABLED) {
                        FileLog.d("detected contacts change");
                    }
                    ContactsController.this.performSyncPhoneBook(ContactsController.this.getContactsCopy(ContactsController.this.contactsBook), true, false, true, false, true, false);
                }
            }
        });
    }

    public void forceImportContacts() {
        Utilities.globalQueue.postRunnable(new Runnable() {
            public void run() {
                if (BuildVars.LOGS_ENABLED) {
                    FileLog.d("force import contacts");
                }
                ContactsController.this.performSyncPhoneBook(new HashMap(), true, true, true, true, false, false);
            }
        });
    }

    public void syncPhoneBookByAlert(HashMap<String, Contact> contacts, boolean first, boolean schedule, boolean cancel) {
        final HashMap<String, Contact> hashMap = contacts;
        final boolean z = first;
        final boolean z2 = schedule;
        final boolean z3 = cancel;
        Utilities.globalQueue.postRunnable(new Runnable() {
            public void run() {
                if (BuildVars.LOGS_ENABLED) {
                    FileLog.d("sync contacts by alert");
                }
                ContactsController.this.performSyncPhoneBook(hashMap, true, z, z2, false, false, z3);
            }
        });
    }

    public void resetImportedContacts() {
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(new TL_contacts_resetSaved(), new RequestDelegate() {
            public void run(TLObject response, TL_error error) {
            }
        });
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean checkContactsInternal() {
        /*
        r9 = this;
        r0 = 0;
        r1 = r0;
        r2 = r9.hasContactsPermission();	 Catch:{ Exception -> 0x0069 }
        if (r2 != 0) goto L_0x0009;
    L_0x0008:
        return r0;
    L_0x0009:
        r2 = org.telegram.messenger.ApplicationLoader.applicationContext;	 Catch:{ Exception -> 0x0069 }
        r3 = r2.getContentResolver();	 Catch:{ Exception -> 0x0069 }
        r2 = 0;
        r4 = android.provider.ContactsContract.RawContacts.CONTENT_URI;	 Catch:{ Exception -> 0x005b }
        r5 = 1;
        r5 = new java.lang.String[r5];	 Catch:{ Exception -> 0x005b }
        r6 = "version";
        r5[r0] = r6;	 Catch:{ Exception -> 0x005b }
        r6 = 0;
        r7 = 0;
        r8 = 0;
        r0 = r3.query(r4, r5, r6, r7, r8);	 Catch:{ Exception -> 0x005b }
        r2 = r0;
        if (r2 == 0) goto L_0x0053;
    L_0x0023:
        r0 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x005b }
        r0.<init>();	 Catch:{ Exception -> 0x005b }
    L_0x0028:
        r4 = r2.moveToNext();	 Catch:{ Exception -> 0x005b }
        if (r4 == 0) goto L_0x003c;
    L_0x002e:
        r4 = "version";
        r4 = r2.getColumnIndex(r4);	 Catch:{ Exception -> 0x005b }
        r4 = r2.getString(r4);	 Catch:{ Exception -> 0x005b }
        r0.append(r4);	 Catch:{ Exception -> 0x005b }
        goto L_0x0028;
    L_0x003c:
        r4 = r0.toString();	 Catch:{ Exception -> 0x005b }
        r5 = r9.lastContactsVersions;	 Catch:{ Exception -> 0x005b }
        r5 = r5.length();	 Catch:{ Exception -> 0x005b }
        if (r5 == 0) goto L_0x0051;
    L_0x0048:
        r5 = r9.lastContactsVersions;	 Catch:{ Exception -> 0x005b }
        r5 = r5.equals(r4);	 Catch:{ Exception -> 0x005b }
        if (r5 != 0) goto L_0x0051;
    L_0x0050:
        r1 = 1;
    L_0x0051:
        r9.lastContactsVersions = r4;	 Catch:{ Exception -> 0x005b }
    L_0x0053:
        if (r2 == 0) goto L_0x0062;
    L_0x0055:
        r2.close();	 Catch:{ Exception -> 0x0069 }
        goto L_0x0062;
    L_0x0059:
        r0 = move-exception;
        goto L_0x0063;
    L_0x005b:
        r0 = move-exception;
        org.telegram.messenger.FileLog.e(r0);	 Catch:{ all -> 0x0059 }
        if (r2 == 0) goto L_0x0062;
    L_0x0061:
        goto L_0x0055;
    L_0x0062:
        goto L_0x006d;
    L_0x0063:
        if (r2 == 0) goto L_0x0068;
    L_0x0065:
        r2.close();	 Catch:{ Exception -> 0x0069 }
    L_0x0068:
        throw r0;	 Catch:{ Exception -> 0x0069 }
    L_0x0069:
        r0 = move-exception;
        org.telegram.messenger.FileLog.e(r0);
    L_0x006d:
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.ContactsController.checkContactsInternal():boolean");
    }

    public void readContacts() {
        synchronized (this.loadContactsSync) {
            if (this.loadingContacts) {
                return;
            }
            this.loadingContacts = true;
            Utilities.stageQueue.postRunnable(new Runnable() {
                public void run() {
                    if (ContactsController.this.contacts.isEmpty()) {
                        if (!ContactsController.this.contactsLoaded) {
                            ContactsController.this.loadContacts(true, 0);
                            return;
                        }
                    }
                    synchronized (ContactsController.this.loadContactsSync) {
                        ContactsController.this.loadingContacts = false;
                    }
                }
            });
        }
    }

    private boolean isNotValidNameString(String src) {
        boolean z = true;
        if (TextUtils.isEmpty(src)) {
            return true;
        }
        int count = 0;
        int len = src.length();
        for (int a = 0; a < len; a++) {
            char c = src.charAt(a);
            if (c >= '0' && c <= '9') {
                count++;
            }
        }
        if (count <= 3) {
            z = false;
        }
        return z;
    }

    private HashMap<String, Contact> readContactsFromPhoneBook() {
        Throwable e;
        Throwable th;
        if (!UserConfig.getInstance(this.currentAccount).syncContacts) {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.d("contacts sync disabled");
            }
            return new HashMap();
        } else if (hasContactsPermission()) {
            Cursor pCur = null;
            HashMap<String, Contact> contactsMap = null;
            ContactsController contactsController;
            ContactsController contactsController2;
            try {
                String accountType;
                String shortNumber;
                ContentResolver contentResolver;
                ArrayList<String> arrayList;
                String ids;
                Uri uri;
                StringBuilder escaper = new StringBuilder();
                ContentResolver cr = ApplicationLoader.applicationContext.getContentResolver();
                HashMap<String, Contact> shortContacts = new HashMap();
                ArrayList<String> idsArr = new ArrayList();
                pCur = cr.query(Phone.CONTENT_URI, contactsController.projectionPhones, null, null, null);
                int lastContactId = 1;
                boolean z = false;
                boolean z2 = true;
                if (pCur != null) {
                    int count;
                    int count2 = pCur.getCount();
                    if (count2 > 0) {
                        if (null == null) {
                            contactsMap = new HashMap(count2);
                        }
                        while (pCur.moveToNext()) {
                            String number = pCur.getString(z2);
                            accountType = pCur.getString(5);
                            if (accountType == null) {
                                accountType = TtmlNode.ANONYMOUS_REGION_ID;
                            }
                            boolean isGoodAccountType = accountType.indexOf(".sim") != 0 ? z2 : z;
                            if (!TextUtils.isEmpty(number)) {
                                number = PhoneFormat.stripExceptNumbers(number, z2);
                                if (!TextUtils.isEmpty(number)) {
                                    String shortNumber2 = number;
                                    if (number.startsWith("+")) {
                                        shortNumber = number.substring(z2);
                                    } else {
                                        shortNumber = shortNumber2;
                                    }
                                    String lookup_key = pCur.getString(z);
                                    escaper.setLength(z);
                                    String lookup_key2 = lookup_key;
                                    DatabaseUtils.appendEscapedSQLString(escaper, lookup_key2);
                                    String key = escaper.toString();
                                    Contact existingContact = (Contact) shortContacts.get(shortNumber);
                                    ArrayList idsArr2;
                                    if (existingContact != null) {
                                        count = count2;
                                        if (existingContact.isGoodProvider == 0 && !accountType.equals(existingContact.provider)) {
                                            escaper.setLength(0);
                                            DatabaseUtils.appendEscapedSQLString(escaper, existingContact.key);
                                            idsArr2.remove(escaper.toString());
                                            idsArr2.add(key);
                                            existingContact.key = lookup_key2;
                                            existingContact.isGoodProvider = isGoodAccountType;
                                            existingContact.provider = accountType;
                                        }
                                        count2 = count;
                                        z = false;
                                        z2 = true;
                                    } else {
                                        count = count2;
                                        String key2 = key;
                                        if (!idsArr2.contains(key2)) {
                                            idsArr2.add(key2);
                                        }
                                        StringBuilder escaper2 = escaper;
                                        int type = pCur.getInt(2);
                                        Contact contact = (Contact) contactsMap.get(lookup_key2);
                                        if (contact == null) {
                                            existingContact = new Contact();
                                            key2 = pCur.getString(4);
                                            if (key2 == null) {
                                                key2 = TtmlNode.ANONYMOUS_REGION_ID;
                                            } else {
                                                key2 = key2.trim();
                                            }
                                            if (contactsController.isNotValidNameString(key2)) {
                                                existingContact.first_name = key2;
                                                contentResolver = cr;
                                                existingContact.last_name = TtmlNode.ANONYMOUS_REGION_ID;
                                                arrayList = idsArr2;
                                            } else {
                                                contentResolver = cr;
                                                cr = key2.lastIndexOf(32);
                                                if (cr != -1) {
                                                    arrayList = idsArr2;
                                                    try {
                                                        existingContact.first_name = key2.substring(0, cr).trim();
                                                        existingContact.last_name = key2.substring(cr + 1, key2.length()).trim();
                                                    } catch (Throwable th2) {
                                                        e = th2;
                                                        contactsController2 = this;
                                                    }
                                                } else {
                                                    arrayList = idsArr2;
                                                    existingContact.first_name = key2;
                                                    existingContact.last_name = TtmlNode.ANONYMOUS_REGION_ID;
                                                }
                                            }
                                            existingContact.provider = accountType;
                                            existingContact.isGoodProvider = isGoodAccountType;
                                            existingContact.key = lookup_key2;
                                            int lastContactId2 = lastContactId + 1;
                                            existingContact.contact_id = lastContactId;
                                            contactsMap.put(lookup_key2, existingContact);
                                            lastContactId = lastContactId2;
                                        } else {
                                            contentResolver = cr;
                                            Contact contact2 = existingContact;
                                            arrayList = idsArr2;
                                            String str = key2;
                                            existingContact = contact;
                                        }
                                        existingContact.shortPhones.add(shortNumber);
                                        existingContact.phones.add(number);
                                        existingContact.phoneDeleted.add(Integer.valueOf(0));
                                        if (type == 0) {
                                            String custom = pCur.getString(3);
                                            existingContact.phoneTypes.add(custom != null ? custom : LocaleController.getString("PhoneMobile", R.string.PhoneMobile));
                                        } else if (type == 1) {
                                            existingContact.phoneTypes.add(LocaleController.getString("PhoneHome", R.string.PhoneHome));
                                        } else if (type == 2) {
                                            existingContact.phoneTypes.add(LocaleController.getString("PhoneMobile", R.string.PhoneMobile));
                                        } else if (type == 3) {
                                            existingContact.phoneTypes.add(LocaleController.getString("PhoneWork", R.string.PhoneWork));
                                        } else if (type == 12) {
                                            existingContact.phoneTypes.add(LocaleController.getString("PhoneMain", R.string.PhoneMain));
                                        } else {
                                            existingContact.phoneTypes.add(LocaleController.getString("PhoneOther", R.string.PhoneOther));
                                        }
                                        shortContacts.put(shortNumber, existingContact);
                                        count2 = count;
                                        escaper = escaper2;
                                        cr = contentResolver;
                                        idsArr2 = arrayList;
                                        contactsController = this;
                                        z = false;
                                        z2 = true;
                                    }
                                }
                            }
                            count = count2;
                            count2 = count;
                            z = false;
                            z2 = true;
                        }
                    }
                    contentResolver = cr;
                    arrayList = idsArr;
                    count = count2;
                    try {
                        pCur.close();
                    } catch (Exception e2) {
                    }
                    pCur = null;
                } else {
                    contentResolver = cr;
                    arrayList = idsArr;
                }
                try {
                    ids = TextUtils.join(",", arrayList);
                    uri = Data.CONTENT_URI;
                } catch (Throwable th3) {
                    th2 = th3;
                    contactsController2 = this;
                    e = th2;
                    if (pCur != null) {
                        pCur.close();
                    }
                    throw e;
                }
                try {
                    String[] strArr = this.projectionNames;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("lookup IN (");
                    stringBuilder.append(ids);
                    stringBuilder.append(") AND ");
                    stringBuilder.append("mimetype");
                    stringBuilder.append(" = '");
                    stringBuilder.append("vnd.android.cursor.item/name");
                    stringBuilder.append("'");
                    pCur = contentResolver.query(uri, strArr, stringBuilder.toString(), null, null);
                    if (pCur != null) {
                        while (pCur.moveToNext()) {
                            shortNumber = pCur.getString(0);
                            String fname = pCur.getString(1);
                            String sname = pCur.getString(2);
                            accountType = pCur.getString(3);
                            Contact contact3 = (Contact) contactsMap.get(shortNumber);
                            if (contact3 != null && !contact3.namesFilled) {
                                StringBuilder stringBuilder2;
                                if (contact3.isGoodProvider) {
                                    if (fname != null) {
                                        contact3.first_name = fname;
                                    } else {
                                        contact3.first_name = TtmlNode.ANONYMOUS_REGION_ID;
                                    }
                                    if (sname != null) {
                                        contact3.last_name = sname;
                                    } else {
                                        contact3.last_name = TtmlNode.ANONYMOUS_REGION_ID;
                                    }
                                    if (!TextUtils.isEmpty(accountType)) {
                                        if (TextUtils.isEmpty(contact3.first_name)) {
                                            contact3.first_name = accountType;
                                        } else {
                                            stringBuilder2 = new StringBuilder();
                                            stringBuilder2.append(contact3.first_name);
                                            stringBuilder2.append(" ");
                                            stringBuilder2.append(accountType);
                                            contact3.first_name = stringBuilder2.toString();
                                        }
                                    }
                                } else if ((!isNotValidNameString(fname) && (contact3.first_name.contains(fname) || fname.contains(contact3.first_name))) || (!isNotValidNameString(sname) && (contact3.last_name.contains(sname) || fname.contains(contact3.last_name)))) {
                                    if (fname != null) {
                                        contact3.first_name = fname;
                                    } else {
                                        contact3.first_name = TtmlNode.ANONYMOUS_REGION_ID;
                                    }
                                    if (!TextUtils.isEmpty(accountType)) {
                                        if (TextUtils.isEmpty(contact3.first_name)) {
                                            contact3.first_name = accountType;
                                        } else {
                                            stringBuilder2 = new StringBuilder();
                                            stringBuilder2.append(contact3.first_name);
                                            stringBuilder2.append(" ");
                                            stringBuilder2.append(accountType);
                                            contact3.first_name = stringBuilder2.toString();
                                        }
                                    }
                                    if (sname != null) {
                                        contact3.last_name = sname;
                                    } else {
                                        contact3.last_name = TtmlNode.ANONYMOUS_REGION_ID;
                                    }
                                }
                                contact3.namesFilled = true;
                            }
                        }
                        try {
                            pCur.close();
                        } catch (Exception e3) {
                        }
                        pCur = null;
                    }
                    if (pCur != null) {
                        try {
                            pCur.close();
                        } catch (Throwable th22) {
                            FileLog.e(th22);
                        }
                    }
                } catch (Throwable th4) {
                    th22 = th4;
                    e = th22;
                    FileLog.e(e);
                    if (contactsMap != null) {
                        contactsMap.clear();
                    }
                    if (pCur != null) {
                        pCur.close();
                    }
                    if (contactsMap == null) {
                    }
                    return contactsMap == null ? contactsMap : new HashMap();
                }
            } catch (Throwable th5) {
                th22 = th5;
                contactsController2 = contactsController;
                e = th22;
                if (pCur != null) {
                    pCur.close();
                }
                throw e;
            }
            if (contactsMap == null) {
            }
            return contactsMap == null ? contactsMap : new HashMap();
        } else {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.d("app has no contacts permissions");
            }
            return new HashMap();
        }
    }

    public HashMap<String, Contact> getContactsCopy(HashMap<String, Contact> original) {
        HashMap<String, Contact> ret = new HashMap();
        for (Entry<String, Contact> entry : original.entrySet()) {
            Contact copyContact = new Contact();
            Contact originalContact = (Contact) entry.getValue();
            copyContact.phoneDeleted.addAll(originalContact.phoneDeleted);
            copyContact.phones.addAll(originalContact.phones);
            copyContact.phoneTypes.addAll(originalContact.phoneTypes);
            copyContact.shortPhones.addAll(originalContact.shortPhones);
            copyContact.first_name = originalContact.first_name;
            copyContact.last_name = originalContact.last_name;
            copyContact.contact_id = originalContact.contact_id;
            copyContact.key = originalContact.key;
            ret.put(copyContact.key, copyContact);
        }
        return ret;
    }

    protected void migratePhoneBookToV7(final SparseArray<Contact> contactHashMap) {
        Utilities.globalQueue.postRunnable(new Runnable() {
            public void run() {
                if (!ContactsController.this.migratingContacts) {
                    ContactsController.this.migratingContacts = true;
                    HashMap<String, Contact> migratedMap = new HashMap();
                    HashMap<String, Contact> contactsMap = ContactsController.this.readContactsFromPhoneBook();
                    HashMap<String, String> contactsBookShort = new HashMap();
                    Iterator it = contactsMap.entrySet().iterator();
                    while (true) {
                        int a = 0;
                        if (!it.hasNext()) {
                            break;
                        }
                        Contact value = (Contact) ((Entry) it.next()).getValue();
                        while (a < value.shortPhones.size()) {
                            contactsBookShort.put(value.shortPhones.get(a), value.key);
                            a++;
                        }
                    }
                    for (int b = 0; b < contactHashMap.size(); b++) {
                        Contact value2 = (Contact) contactHashMap.valueAt(b);
                        for (int a2 = 0; a2 < value2.shortPhones.size(); a2++) {
                            String key = (String) contactsBookShort.get((String) value2.shortPhones.get(a2));
                            if (key != null) {
                                value2.key = key;
                                migratedMap.put(key, value2);
                                break;
                            }
                        }
                    }
                    if (BuildVars.LOGS_ENABLED) {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("migrated contacts ");
                        stringBuilder.append(migratedMap.size());
                        stringBuilder.append(" of ");
                        stringBuilder.append(contactHashMap.size());
                        FileLog.d(stringBuilder.toString());
                    }
                    MessagesStorage.getInstance(ContactsController.this.currentAccount).putCachedPhoneBook(migratedMap, true);
                }
            }
        });
    }

    protected void performSyncPhoneBook(HashMap<String, Contact> contactHashMap, boolean request, boolean first, boolean schedule, boolean force, boolean checkCount, boolean canceled) {
        ContactsController contactsController;
        if (first) {
            contactsController = this;
        } else if (!this.contactsBookLoaded) {
            return;
        }
        final HashMap<String, Contact> hashMap = contactHashMap;
        final boolean z = schedule;
        final boolean z2 = request;
        final boolean z3 = first;
        final boolean z4 = force;
        final boolean z5 = checkCount;
        final boolean z6 = canceled;
        Utilities.globalQueue.postRunnable(new Runnable() {
            /* JADX WARNING: inconsistent code. */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public void run() {
                /*
                r47 = this;
                r9 = r47;
                r0 = 0;
                r1 = 0;
                r10 = 1;
                r2 = new java.util.HashMap;
                r2.<init>();
                r11 = r2;
                r2 = r2;
                r2 = r2.entrySet();
                r2 = r2.iterator();
            L_0x0015:
                r3 = r2.hasNext();
                if (r3 == 0) goto L_0x003d;
            L_0x001b:
                r3 = r2.next();
                r3 = (java.util.Map.Entry) r3;
                r4 = r3.getValue();
                r4 = (org.telegram.messenger.ContactsController.Contact) r4;
                r5 = 0;
            L_0x0028:
                r6 = r4.shortPhones;
                r6 = r6.size();
                if (r5 >= r6) goto L_0x003c;
            L_0x0030:
                r6 = r4.shortPhones;
                r6 = r6.get(r5);
                r11.put(r6, r4);
                r5 = r5 + 1;
                goto L_0x0028;
            L_0x003c:
                goto L_0x0015;
            L_0x003d:
                r2 = org.telegram.messenger.BuildVars.LOGS_ENABLED;
                if (r2 == 0) goto L_0x0046;
            L_0x0041:
                r2 = "start read contacts from phone";
                org.telegram.messenger.FileLog.d(r2);
            L_0x0046:
                r2 = r3;
                if (r2 != 0) goto L_0x004f;
            L_0x004a:
                r2 = org.telegram.messenger.ContactsController.this;
                r2.checkContactsInternal();
            L_0x004f:
                r2 = org.telegram.messenger.ContactsController.this;
                r12 = r2.readContactsFromPhoneBook();
                r2 = new java.util.HashMap;
                r2.<init>();
                r13 = r2;
                r2 = r2;
                r14 = r2.size();
                r2 = new java.util.ArrayList;
                r2.<init>();
                r15 = r2;
                r2 = r2;
                r2 = r2.isEmpty();
                if (r2 != 0) goto L_0x03f8;
            L_0x006f:
                r2 = r12.entrySet();
                r2 = r2.iterator();
            L_0x0077:
                r6 = r2.hasNext();
                if (r6 == 0) goto L_0x0389;
            L_0x007d:
                r6 = r2.next();
                r6 = (java.util.Map.Entry) r6;
                r7 = r6.getKey();
                r7 = (java.lang.String) r7;
                r8 = r6.getValue();
                r8 = (org.telegram.messenger.ContactsController.Contact) r8;
                r4 = r2;
                r4 = r4.get(r7);
                r4 = (org.telegram.messenger.ContactsController.Contact) r4;
                if (r4 != 0) goto L_0x00bc;
            L_0x0099:
                r17 = 0;
            L_0x009b:
                r18 = r17;
                r3 = r8.shortPhones;
                r3 = r3.size();
                r5 = r18;
                if (r5 >= r3) goto L_0x00bc;
            L_0x00a7:
                r3 = r8.shortPhones;
                r3 = r3.get(r5);
                r3 = r11.get(r3);
                r3 = (org.telegram.messenger.ContactsController.Contact) r3;
                if (r3 == 0) goto L_0x00b9;
            L_0x00b5:
                r4 = r3;
                r7 = r4.key;
                goto L_0x00bc;
            L_0x00b9:
                r17 = r5 + 1;
                goto L_0x009b;
            L_0x00bc:
                if (r4 == 0) goto L_0x00c2;
            L_0x00be:
                r3 = r4.imported;
                r8.imported = r3;
            L_0x00c2:
                if (r4 == 0) goto L_0x00ea;
            L_0x00c4:
                r3 = r8.first_name;
                r3 = android.text.TextUtils.isEmpty(r3);
                if (r3 != 0) goto L_0x00d6;
            L_0x00cc:
                r3 = r4.first_name;
                r5 = r8.first_name;
                r3 = r3.equals(r5);
                if (r3 == 0) goto L_0x00e8;
            L_0x00d6:
                r3 = r8.last_name;
                r3 = android.text.TextUtils.isEmpty(r3);
                if (r3 != 0) goto L_0x00ea;
            L_0x00de:
                r3 = r4.last_name;
                r5 = r8.last_name;
                r3 = r3.equals(r5);
                if (r3 != 0) goto L_0x00ea;
            L_0x00e8:
                r3 = 1;
                goto L_0x00eb;
            L_0x00ea:
                r3 = 0;
            L_0x00eb:
                if (r4 == 0) goto L_0x02b6;
            L_0x00ed:
                if (r3 == 0) goto L_0x00fd;
            L_0x00ef:
                r22 = r2;
                r23 = r6;
                r26 = r10;
                r25 = r11;
                r32 = r12;
                r33 = r13;
                goto L_0x02c2;
            L_0x00fd:
                r17 = r1;
                r1 = r0;
                r0 = 0;
            L_0x0101:
                r5 = r8.phones;
                r5 = r5.size();
                if (r0 >= r5) goto L_0x0294;
            L_0x0109:
                r5 = r8.shortPhones;
                r5 = r5.get(r0);
                r5 = (java.lang.String) r5;
                r18 = r5.length();
                r22 = r2;
                r2 = r18 + -7;
                r23 = r6;
                r6 = 0;
                r2 = java.lang.Math.max(r6, r2);
                r2 = r5.substring(r2);
                r13.put(r5, r8);
                r6 = r4.shortPhones;
                r6 = r6.indexOf(r5);
                r18 = 0;
                r24 = r6;
                r6 = r4;
                if (r6 == 0) goto L_0x0195;
            L_0x0135:
                r6 = org.telegram.messenger.ContactsController.this;
                r6 = r6.contactsByPhone;
                r6 = r6.get(r5);
                r6 = (org.telegram.tgnet.TLRPC.TL_contact) r6;
                if (r6 == 0) goto L_0x0184;
            L_0x0141:
                r25 = r11;
                r11 = org.telegram.messenger.ContactsController.this;
                r11 = r11.currentAccount;
                r11 = org.telegram.messenger.MessagesController.getInstance(r11);
                r26 = r10;
                r10 = r6.user_id;
                r10 = java.lang.Integer.valueOf(r10);
                r10 = r11.getUser(r10);
                if (r10 == 0) goto L_0x0183;
            L_0x015b:
                r17 = r17 + 1;
                r11 = r10.first_name;
                r11 = android.text.TextUtils.isEmpty(r11);
                if (r11 == 0) goto L_0x0183;
            L_0x0165:
                r11 = r10.last_name;
                r11 = android.text.TextUtils.isEmpty(r11);
                if (r11 == 0) goto L_0x0183;
            L_0x016d:
                r11 = r8.first_name;
                r11 = android.text.TextUtils.isEmpty(r11);
                if (r11 == 0) goto L_0x017d;
            L_0x0175:
                r11 = r8.last_name;
                r11 = android.text.TextUtils.isEmpty(r11);
                if (r11 != 0) goto L_0x0183;
            L_0x017d:
                r11 = -1;
                r10 = 1;
                r18 = r10;
                r24 = r11;
            L_0x0183:
                goto L_0x0199;
            L_0x0184:
                r26 = r10;
                r25 = r11;
                r10 = org.telegram.messenger.ContactsController.this;
                r10 = r10.contactsByShortPhone;
                r10 = r10.containsKey(r2);
                if (r10 == 0) goto L_0x0199;
            L_0x0192:
                r17 = r17 + 1;
                goto L_0x0199;
            L_0x0195:
                r26 = r10;
                r25 = r11;
            L_0x0199:
                r6 = r24;
                r10 = -1;
                if (r6 != r10) goto L_0x025d;
            L_0x019e:
                r10 = r4;
                if (r10 == 0) goto L_0x0258;
            L_0x01a2:
                if (r18 != 0) goto L_0x0220;
            L_0x01a4:
                r10 = org.telegram.messenger.ContactsController.this;
                r10 = r10.contactsByPhone;
                r10 = r10.get(r5);
                r10 = (org.telegram.tgnet.TLRPC.TL_contact) r10;
                if (r10 == 0) goto L_0x020f;
            L_0x01b0:
                r11 = org.telegram.messenger.ContactsController.this;
                r11 = r11.currentAccount;
                r11 = org.telegram.messenger.MessagesController.getInstance(r11);
                r27 = r5;
                r5 = r10.user_id;
                r5 = java.lang.Integer.valueOf(r5);
                r5 = r11.getUser(r5);
                if (r5 == 0) goto L_0x0208;
            L_0x01c8:
                r17 = r17 + 1;
                r11 = r5.first_name;
                if (r11 == 0) goto L_0x01d1;
            L_0x01ce:
                r11 = r5.first_name;
                goto L_0x01d3;
            L_0x01d1:
                r11 = "";
            L_0x01d3:
                r28 = r10;
                r10 = r5.last_name;
                if (r10 == 0) goto L_0x01dc;
            L_0x01d9:
                r10 = r5.last_name;
                goto L_0x01de;
            L_0x01dc:
                r10 = "";
            L_0x01de:
                r29 = r5;
                r5 = r8.first_name;
                r5 = r11.equals(r5);
                if (r5 == 0) goto L_0x01f0;
            L_0x01e8:
                r5 = r8.last_name;
                r5 = r10.equals(r5);
                if (r5 != 0) goto L_0x0201;
            L_0x01f0:
                r5 = r8.first_name;
                r5 = android.text.TextUtils.isEmpty(r5);
                if (r5 == 0) goto L_0x0207;
            L_0x01f8:
                r5 = r8.last_name;
                r5 = android.text.TextUtils.isEmpty(r5);
                if (r5 == 0) goto L_0x0207;
            L_0x0201:
                r32 = r12;
                r33 = r13;
                goto L_0x0284;
            L_0x0207:
                goto L_0x020e;
            L_0x0208:
                r29 = r5;
                r28 = r10;
                r1 = r1 + 1;
            L_0x020e:
                goto L_0x0222;
            L_0x020f:
                r27 = r5;
                r28 = r10;
                r5 = org.telegram.messenger.ContactsController.this;
                r5 = r5.contactsByShortPhone;
                r5 = r5.containsKey(r2);
                if (r5 == 0) goto L_0x0222;
            L_0x021d:
                r17 = r17 + 1;
                goto L_0x0222;
            L_0x0220:
                r27 = r5;
            L_0x0222:
                r5 = new org.telegram.tgnet.TLRPC$TL_inputPhoneContact;
                r5.<init>();
                r10 = r8.contact_id;
                r10 = (long) r10;
                r5.client_id = r10;
                r10 = r5.client_id;
                r31 = r1;
                r30 = r2;
                r1 = (long) r0;
                r19 = 32;
                r1 = r1 << r19;
                r32 = r12;
                r33 = r13;
                r12 = r10 | r1;
                r5.client_id = r12;
                r1 = r8.first_name;
                r5.first_name = r1;
                r1 = r8.last_name;
                r5.last_name = r1;
                r1 = r8.phones;
                r1 = r1.get(r0);
                r1 = (java.lang.String) r1;
                r5.phone = r1;
                r15.add(r5);
                r1 = r31;
                goto L_0x0284;
            L_0x0258:
                r32 = r12;
                r33 = r13;
                goto L_0x0284;
            L_0x025d:
                r30 = r2;
                r27 = r5;
                r32 = r12;
                r33 = r13;
                r2 = r8.phoneDeleted;
                r5 = r4.phoneDeleted;
                r5 = r5.get(r6);
                r2.set(r0, r5);
                r2 = r4.phones;
                r2.remove(r6);
                r2 = r4.shortPhones;
                r2.remove(r6);
                r2 = r4.phoneDeleted;
                r2.remove(r6);
                r2 = r4.phoneTypes;
                r2.remove(r6);
            L_0x0284:
                r0 = r0 + 1;
                r2 = r22;
                r6 = r23;
                r11 = r25;
                r10 = r26;
                r12 = r32;
                r13 = r33;
                goto L_0x0101;
            L_0x0294:
                r22 = r2;
                r23 = r6;
                r26 = r10;
                r25 = r11;
                r32 = r12;
                r33 = r13;
                r0 = r4.phones;
                r0 = r0.isEmpty();
                if (r0 == 0) goto L_0x02ad;
            L_0x02a8:
                r0 = r2;
                r0.remove(r7);
            L_0x02ad:
                r0 = r1;
                r37 = r14;
                r1 = r17;
                r10 = r33;
                goto L_0x037c;
            L_0x02b6:
                r22 = r2;
                r23 = r6;
                r26 = r10;
                r25 = r11;
                r32 = r12;
                r33 = r13;
            L_0x02c2:
                r2 = r1;
                r1 = r0;
                r0 = 0;
            L_0x02c5:
                r5 = r8.phones;
                r5 = r5.size();
                if (r0 >= r5) goto L_0x036c;
            L_0x02cd:
                r5 = r8.shortPhones;
                r5 = r5.get(r0);
                r5 = (java.lang.String) r5;
                r6 = r5.length();
                r6 = r6 + -7;
                r10 = 0;
                r6 = java.lang.Math.max(r10, r6);
                r6 = r5.substring(r6);
                r10 = r33;
                r10.put(r5, r8);
                if (r4 == 0) goto L_0x0310;
            L_0x02eb:
                r11 = r4.shortPhones;
                r11 = r11.indexOf(r5);
                r12 = -1;
                if (r11 == r12) goto L_0x0310;
            L_0x02f4:
                r13 = r4.phoneDeleted;
                r13 = r13.get(r11);
                r13 = (java.lang.Integer) r13;
                r12 = r8.phoneDeleted;
                r12.set(r0, r13);
                r12 = r13.intValue();
                r34 = r6;
                r6 = 1;
                if (r12 != r6) goto L_0x0312;
            L_0x030b:
                r35 = r2;
                r37 = r14;
                goto L_0x0362;
            L_0x0310:
                r34 = r6;
            L_0x0312:
                r6 = r4;
                if (r6 == 0) goto L_0x035e;
            L_0x0316:
                if (r3 != 0) goto L_0x032a;
            L_0x0318:
                r6 = org.telegram.messenger.ContactsController.this;
                r6 = r6.contactsByPhone;
                r6 = r6.containsKey(r5);
                if (r6 == 0) goto L_0x0328;
            L_0x0322:
                r2 = r2 + 1;
                r37 = r14;
                goto L_0x0364;
            L_0x0328:
                r1 = r1 + 1;
            L_0x032a:
                r6 = new org.telegram.tgnet.TLRPC$TL_inputPhoneContact;
                r6.<init>();
                r11 = r8.contact_id;
                r11 = (long) r11;
                r6.client_id = r11;
                r11 = r6.client_id;
                r36 = r1;
                r35 = r2;
                r1 = (long) r0;
                r13 = 32;
                r1 = r1 << r13;
                r37 = r14;
                r13 = r11 | r1;
                r6.client_id = r13;
                r1 = r8.first_name;
                r6.first_name = r1;
                r1 = r8.last_name;
                r6.last_name = r1;
                r1 = r8.phones;
                r1 = r1.get(r0);
                r1 = (java.lang.String) r1;
                r6.phone = r1;
                r15.add(r6);
                r2 = r35;
                r1 = r36;
                goto L_0x0364;
            L_0x035e:
                r35 = r2;
                r37 = r14;
            L_0x0362:
                r2 = r35;
            L_0x0364:
                r0 = r0 + 1;
                r33 = r10;
                r14 = r37;
                goto L_0x02c5;
            L_0x036c:
                r35 = r2;
                r37 = r14;
                r10 = r33;
                if (r4 == 0) goto L_0x0379;
            L_0x0374:
                r0 = r2;
                r0.remove(r7);
            L_0x0379:
                r0 = r1;
                r1 = r35;
            L_0x037c:
                r13 = r10;
                r2 = r22;
                r11 = r25;
                r10 = r26;
                r12 = r32;
                r14 = r37;
                goto L_0x0077;
            L_0x0389:
                r26 = r10;
                r25 = r11;
                r32 = r12;
                r10 = r13;
                r37 = r14;
                r2 = r5;
                if (r2 != 0) goto L_0x03b8;
            L_0x0396:
                r2 = r2;
                r2 = r2.isEmpty();
                if (r2 == 0) goto L_0x03b8;
            L_0x039e:
                r2 = r15.isEmpty();
                if (r2 == 0) goto L_0x03b8;
            L_0x03a4:
                r11 = r32;
                r2 = r11.size();
                r12 = r37;
                if (r12 != r2) goto L_0x03bc;
            L_0x03ae:
                r2 = org.telegram.messenger.BuildVars.LOGS_ENABLED;
                if (r2 == 0) goto L_0x03b7;
            L_0x03b2:
                r2 = "contacts not changed!";
                org.telegram.messenger.FileLog.d(r2);
            L_0x03b7:
                return;
            L_0x03b8:
                r11 = r32;
                r12 = r37;
            L_0x03bc:
                r2 = r4;
                if (r2 == 0) goto L_0x03f4;
            L_0x03c0:
                r2 = r2;
                r2 = r2.isEmpty();
                if (r2 != 0) goto L_0x03f4;
            L_0x03c8:
                r2 = r11.isEmpty();
                if (r2 != 0) goto L_0x03f4;
            L_0x03ce:
                r2 = r15.isEmpty();
                if (r2 == 0) goto L_0x03e2;
            L_0x03d4:
                r2 = org.telegram.messenger.ContactsController.this;
                r2 = r2.currentAccount;
                r2 = org.telegram.messenger.MessagesStorage.getInstance(r2);
                r3 = 0;
                r2.putCachedPhoneBook(r11, r3);
            L_0x03e2:
                if (r26 != 0) goto L_0x03f4;
            L_0x03e4:
                r2 = r2;
                r2 = r2.isEmpty();
                if (r2 != 0) goto L_0x03f4;
            L_0x03ec:
                r2 = new org.telegram.messenger.ContactsController$9$1;
                r2.<init>();
                org.telegram.messenger.AndroidUtilities.runOnUIThread(r2);
            L_0x03f4:
                r13 = r0;
                r14 = r1;
                goto L_0x0501;
            L_0x03f8:
                r26 = r10;
                r25 = r11;
                r11 = r12;
                r10 = r13;
                r12 = r14;
                r2 = r4;
                if (r2 == 0) goto L_0x04fc;
            L_0x0403:
                r2 = r11.entrySet();
                r2 = r2.iterator();
            L_0x040b:
                r3 = r2.hasNext();
                if (r3 == 0) goto L_0x04fc;
            L_0x0411:
                r3 = r2.next();
                r3 = (java.util.Map.Entry) r3;
                r4 = r3.getValue();
                r4 = (org.telegram.messenger.ContactsController.Contact) r4;
                r5 = r3.getKey();
                r5 = (java.lang.String) r5;
                r6 = r1;
                r1 = 0;
            L_0x0425:
                r7 = r4.phones;
                r7 = r7.size();
                if (r1 >= r7) goto L_0x04f5;
            L_0x042d:
                r7 = r6;
                if (r7 != 0) goto L_0x04bc;
            L_0x0431:
                r7 = r4.shortPhones;
                r7 = r7.get(r1);
                r7 = (java.lang.String) r7;
                r8 = r7.length();
                r8 = r8 + -7;
                r13 = 0;
                r8 = java.lang.Math.max(r13, r8);
                r8 = r7.substring(r8);
                r13 = org.telegram.messenger.ContactsController.this;
                r13 = r13.contactsByPhone;
                r13 = r13.get(r7);
                r13 = (org.telegram.tgnet.TLRPC.TL_contact) r13;
                if (r13 == 0) goto L_0x04ab;
            L_0x0454:
                r14 = org.telegram.messenger.ContactsController.this;
                r14 = r14.currentAccount;
                r14 = org.telegram.messenger.MessagesController.getInstance(r14);
                r38 = r0;
                r0 = r13.user_id;
                r0 = java.lang.Integer.valueOf(r0);
                r0 = r14.getUser(r0);
                if (r0 == 0) goto L_0x04a8;
            L_0x046c:
                r6 = r6 + 1;
                r14 = r0.first_name;
                if (r14 == 0) goto L_0x0475;
            L_0x0472:
                r14 = r0.first_name;
                goto L_0x0477;
            L_0x0475:
                r14 = "";
            L_0x0477:
                r39 = r2;
                r2 = r0.last_name;
                if (r2 == 0) goto L_0x0480;
            L_0x047d:
                r2 = r0.last_name;
                goto L_0x0482;
            L_0x0480:
                r2 = "";
            L_0x0482:
                r40 = r0;
                r0 = r4.first_name;
                r0 = r14.equals(r0);
                if (r0 == 0) goto L_0x0494;
            L_0x048c:
                r0 = r4.last_name;
                r0 = r2.equals(r0);
                if (r0 != 0) goto L_0x04a5;
            L_0x0494:
                r0 = r4.first_name;
                r0 = android.text.TextUtils.isEmpty(r0);
                if (r0 == 0) goto L_0x04aa;
            L_0x049c:
                r0 = r4.last_name;
                r0 = android.text.TextUtils.isEmpty(r0);
                if (r0 == 0) goto L_0x04aa;
            L_0x04a5:
                r41 = r3;
                goto L_0x04eb;
            L_0x04a8:
                r39 = r2;
            L_0x04aa:
                goto L_0x04c0;
            L_0x04ab:
                r38 = r0;
                r39 = r2;
                r0 = org.telegram.messenger.ContactsController.this;
                r0 = r0.contactsByShortPhone;
                r0 = r0.containsKey(r8);
                if (r0 == 0) goto L_0x04c0;
            L_0x04b9:
                r6 = r6 + 1;
                goto L_0x04c0;
            L_0x04bc:
                r38 = r0;
                r39 = r2;
            L_0x04c0:
                r0 = new org.telegram.tgnet.TLRPC$TL_inputPhoneContact;
                r0.<init>();
                r2 = r4.contact_id;
                r7 = (long) r2;
                r0.client_id = r7;
                r7 = r0.client_id;
                r13 = (long) r1;
                r2 = 32;
                r13 = r13 << r2;
                r41 = r3;
                r2 = r7 | r13;
                r0.client_id = r2;
                r2 = r4.first_name;
                r0.first_name = r2;
                r2 = r4.last_name;
                r0.last_name = r2;
                r2 = r4.phones;
                r2 = r2.get(r1);
                r2 = (java.lang.String) r2;
                r0.phone = r2;
                r15.add(r0);
            L_0x04eb:
                r1 = r1 + 1;
                r0 = r38;
                r2 = r39;
                r3 = r41;
                goto L_0x0425;
            L_0x04f5:
                r38 = r0;
                r39 = r2;
                r1 = r6;
                goto L_0x040b;
            L_0x04fc:
                r38 = r0;
                r14 = r1;
                r13 = r38;
            L_0x0501:
                r0 = org.telegram.messenger.BuildVars.LOGS_ENABLED;
                if (r0 == 0) goto L_0x050a;
            L_0x0505:
                r0 = "done processing contacts";
                org.telegram.messenger.FileLog.d(r0);
            L_0x050a:
                r0 = r4;
                if (r0 == 0) goto L_0x065b;
            L_0x050e:
                r0 = r15.isEmpty();
                if (r0 != 0) goto L_0x064e;
            L_0x0514:
                r0 = org.telegram.messenger.BuildVars.LOGS_ENABLED;
                if (r0 == 0) goto L_0x051d;
            L_0x0518:
                r0 = "start import contacts";
                org.telegram.messenger.FileLog.e(r0);
            L_0x051d:
                r0 = r7;
                if (r0 == 0) goto L_0x054a;
            L_0x0521:
                if (r13 == 0) goto L_0x054a;
            L_0x0523:
                r0 = 30;
                if (r13 < r0) goto L_0x0529;
            L_0x0527:
                r5 = 1;
            L_0x0528:
                goto L_0x054b;
            L_0x0529:
                r0 = r5;
                if (r0 == 0) goto L_0x0548;
            L_0x052d:
                if (r12 != 0) goto L_0x0548;
            L_0x052f:
                r0 = org.telegram.messenger.ContactsController.this;
                r0 = r0.contactsByPhone;
                r0 = r0.size();
                r0 = r0 - r14;
                r1 = org.telegram.messenger.ContactsController.this;
                r1 = r1.contactsByPhone;
                r1 = r1.size();
                r1 = r1 / 3;
                r1 = r1 * 2;
                if (r0 <= r1) goto L_0x0548;
            L_0x0546:
                r5 = 2;
                goto L_0x0528;
            L_0x0548:
                r5 = 0;
                goto L_0x0528;
            L_0x054a:
                r5 = 0;
            L_0x054b:
                r8 = r5;
                r0 = org.telegram.messenger.BuildVars.LOGS_ENABLED;
                if (r0 == 0) goto L_0x057c;
            L_0x0550:
                r0 = new java.lang.StringBuilder;
                r0.<init>();
                r1 = "new phone book contacts ";
                r0.append(r1);
                r0.append(r13);
                r1 = " serverContactsInPhonebook ";
                r0.append(r1);
                r0.append(r14);
                r1 = " totalContacts ";
                r0.append(r1);
                r1 = org.telegram.messenger.ContactsController.this;
                r1 = r1.contactsByPhone;
                r1 = r1.size();
                r0.append(r1);
                r0 = r0.toString();
                org.telegram.messenger.FileLog.d(r0);
            L_0x057c:
                if (r8 == 0) goto L_0x0587;
            L_0x057e:
                r0 = new org.telegram.messenger.ContactsController$9$2;
                r0.<init>(r8);
                org.telegram.messenger.AndroidUtilities.runOnUIThread(r0);
                return;
            L_0x0587:
                r0 = r8;
                if (r0 == 0) goto L_0x0596;
            L_0x058b:
                r0 = org.telegram.messenger.Utilities.stageQueue;
                r1 = new org.telegram.messenger.ContactsController$9$3;
                r1.<init>(r10, r11);
                r0.postRunnable(r1);
                return;
            L_0x0596:
                r0 = 1;
                r4 = new boolean[r0];
                r0 = 0;
                r4[r0] = r0;
                r0 = new java.util.HashMap;
                r0.<init>(r11);
                r7 = r0;
                r0 = new android.util.SparseArray;
                r0.<init>();
                r6 = r0;
                r0 = r7.entrySet();
                r0 = r0.iterator();
            L_0x05b0:
                r1 = r0.hasNext();
                if (r1 == 0) goto L_0x05ca;
            L_0x05b6:
                r1 = r0.next();
                r1 = (java.util.Map.Entry) r1;
                r2 = r1.getValue();
                r2 = (org.telegram.messenger.ContactsController.Contact) r2;
                r3 = r2.contact_id;
                r5 = r2.key;
                r6.put(r3, r5);
                goto L_0x05b0;
            L_0x05ca:
                r0 = org.telegram.messenger.ContactsController.this;
                r1 = 0;
                r0.completedRequestsCount = r1;
                r0 = r15.size();
                r0 = (double) r0;
                r2 = 4647503709213818880; // 0x407f400000000000 float:0.0 double:500.0;
                r0 = r0 / r2;
                r0 = java.lang.Math.ceil(r0);
                r5 = (int) r0;
                r20 = 0;
            L_0x05e2:
                r3 = r20;
                if (r3 >= r5) goto L_0x064b;
            L_0x05e6:
                r0 = new org.telegram.tgnet.TLRPC$TL_contacts_importContacts;
                r0.<init>();
                r2 = r0;
                r1 = r3 * 500;
                r0 = r1 + 500;
                r42 = r3;
                r3 = r15.size();
                r3 = java.lang.Math.min(r0, r3);
                r0 = new java.util.ArrayList;
                r43 = r5;
                r5 = r15.subList(r1, r3);
                r0.<init>(r5);
                r2.contacts = r0;
                r0 = org.telegram.messenger.ContactsController.this;
                r0 = r0.currentAccount;
                r5 = org.telegram.tgnet.ConnectionsManager.getInstance(r0);
                r0 = new org.telegram.messenger.ContactsController$9$4;
                r44 = r0;
                r16 = r1;
                r1 = r9;
                r45 = r2;
                r2 = r7;
                r18 = r3;
                r17 = r42;
                r3 = r6;
                r46 = r12;
                r19 = r43;
                r12 = r5;
                r5 = r11;
                r20 = r6;
                r6 = r45;
                r21 = r7;
                r7 = r19;
                r22 = r8;
                r8 = r10;
                r0.<init>(r2, r3, r4, r5, r6, r7, r8);
                r0 = 6;
                r2 = r44;
                r1 = r45;
                r12.sendRequest(r1, r2, r0);
                r0 = r17 + 1;
                r5 = r19;
                r6 = r20;
                r7 = r21;
                r8 = r22;
                r12 = r46;
                r20 = r0;
                goto L_0x05e2;
            L_0x064b:
                r46 = r12;
                goto L_0x067b;
            L_0x064e:
                r46 = r12;
                r0 = org.telegram.messenger.Utilities.stageQueue;
                r1 = new org.telegram.messenger.ContactsController$9$5;
                r1.<init>(r10, r11);
                r0.postRunnable(r1);
                goto L_0x067b;
            L_0x065b:
                r46 = r12;
                r0 = org.telegram.messenger.Utilities.stageQueue;
                r1 = new org.telegram.messenger.ContactsController$9$6;
                r1.<init>(r10, r11);
                r0.postRunnable(r1);
                r0 = r11.isEmpty();
                if (r0 != 0) goto L_0x067b;
            L_0x066d:
                r0 = org.telegram.messenger.ContactsController.this;
                r0 = r0.currentAccount;
                r0 = org.telegram.messenger.MessagesStorage.getInstance(r0);
                r1 = 0;
                r0.putCachedPhoneBook(r11, r1);
            L_0x067b:
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.ContactsController.9.run():void");
            }
        });
    }

    public boolean isLoadingContacts() {
        boolean z;
        synchronized (this.loadContactsSync) {
            z = this.loadingContacts;
        }
        return z;
    }

    private int getContactsHash(ArrayList<TL_contact> contacts) {
        contacts = new ArrayList(contacts);
        Collections.sort(contacts, new Comparator<TL_contact>() {
            public int compare(TL_contact tl_contact, TL_contact tl_contact2) {
                if (tl_contact.user_id > tl_contact2.user_id) {
                    return 1;
                }
                if (tl_contact.user_id < tl_contact2.user_id) {
                    return -1;
                }
                return 0;
            }
        });
        int count = contacts.size();
        long acc = 0;
        for (int a = -1; a < count; a++) {
            long j;
            if (a == -1) {
                j = (((20261 * acc) + 2147483648L) + ((long) UserConfig.getInstance(this.currentAccount).contactsSavedCount)) % 2147483648L;
            } else {
                j = (((20261 * acc) + 2147483648L) + ((long) ((TL_contact) contacts.get(a)).user_id)) % 2147483648L;
            }
            acc = j;
        }
        return (int) acc;
    }

    public void loadContacts(boolean fromCache, final int hash) {
        synchronized (this.loadContactsSync) {
            this.loadingContacts = true;
        }
        if (fromCache) {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.d("load contacts from cache");
            }
            MessagesStorage.getInstance(this.currentAccount).getContacts();
            return;
        }
        if (BuildVars.LOGS_ENABLED) {
            FileLog.d("load contacts from server");
        }
        TL_contacts_getContacts req = new TL_contacts_getContacts();
        req.hash = hash;
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(req, new RequestDelegate() {
            public void run(TLObject response, TL_error error) {
                if (error == null) {
                    contacts_Contacts res = (contacts_Contacts) response;
                    if (hash == 0 || !(res instanceof TL_contacts_contactsNotModified)) {
                        UserConfig.getInstance(ContactsController.this.currentAccount).contactsSavedCount = res.saved_count;
                        UserConfig.getInstance(ContactsController.this.currentAccount).saveConfig(false);
                        ContactsController.this.processLoadedContacts(res.contacts, res.users, 0);
                    } else {
                        ContactsController.this.contactsLoaded = true;
                        if (!ContactsController.this.delayedContactsUpdate.isEmpty() && ContactsController.this.contactsBookLoaded) {
                            ContactsController.this.applyContactsUpdates(ContactsController.this.delayedContactsUpdate, null, null, null);
                            ContactsController.this.delayedContactsUpdate.clear();
                        }
                        UserConfig.getInstance(ContactsController.this.currentAccount).lastContactsSyncTime = (int) (System.currentTimeMillis() / 1000);
                        UserConfig.getInstance(ContactsController.this.currentAccount).saveConfig(false);
                        AndroidUtilities.runOnUIThread(new Runnable() {
                            public void run() {
                                synchronized (ContactsController.this.loadContactsSync) {
                                    ContactsController.this.loadingContacts = false;
                                }
                                NotificationCenter.getInstance(ContactsController.this.currentAccount).postNotificationName(NotificationCenter.contactsDidLoaded, new Object[0]);
                            }
                        });
                        if (BuildVars.LOGS_ENABLED) {
                            FileLog.d("load contacts don't change");
                        }
                    }
                }
            }
        });
    }

    public void processLoadedContacts(final ArrayList<TL_contact> contactsArr, final ArrayList<User> usersArr, final int from) {
        AndroidUtilities.runOnUIThread(new Runnable() {

            class AnonymousClass1 implements Runnable {
                final /* synthetic */ boolean val$isEmpty;
                final /* synthetic */ SparseArray val$usersDict;

                AnonymousClass1(SparseArray sparseArray, boolean z) {
                    this.val$usersDict = sparseArray;
                    this.val$isEmpty = z;
                }

                public void run() {
                    HashMap<String, TL_contact> contactsByPhonesShortDictFinal;
                    AnonymousClass1 anonymousClass1 = this;
                    if (BuildVars.LOGS_ENABLED) {
                        FileLog.d("done loading contacts");
                    }
                    int i = 0;
                    if (from == 1 && (contactsArr.isEmpty() || Math.abs((System.currentTimeMillis() / 1000) - ((long) UserConfig.getInstance(ContactsController.this.currentAccount).lastContactsSyncTime)) >= 86400)) {
                        ContactsController.this.loadContacts(false, ContactsController.this.getContactsHash(contactsArr));
                        if (contactsArr.isEmpty()) {
                            return;
                        }
                    }
                    if (from == 0) {
                        UserConfig.getInstance(ContactsController.this.currentAccount).lastContactsSyncTime = (int) (System.currentTimeMillis() / 1000);
                        UserConfig.getInstance(ContactsController.this.currentAccount).saveConfig(false);
                    }
                    int a = 0;
                    while (a < contactsArr.size()) {
                        TL_contact contact = (TL_contact) contactsArr.get(a);
                        if (anonymousClass1.val$usersDict.get(contact.user_id) != null || contact.user_id == UserConfig.getInstance(ContactsController.this.currentAccount).getClientUserId()) {
                            a++;
                        } else {
                            ContactsController.this.loadContacts(false, 0);
                            if (BuildVars.LOGS_ENABLED) {
                                FileLog.d("contacts are broken, load from server");
                            }
                            return;
                        }
                    }
                    if (from != 1) {
                        MessagesStorage.getInstance(ContactsController.this.currentAccount).putUsersAndChats(usersArr, null, true, true);
                        MessagesStorage.getInstance(ContactsController.this.currentAccount).putContacts(contactsArr, from != 2);
                    }
                    Collections.sort(contactsArr, new Comparator<TL_contact>() {
                        public int compare(TL_contact tl_contact, TL_contact tl_contact2) {
                            return UserObject.getFirstName((User) AnonymousClass1.this.val$usersDict.get(tl_contact.user_id)).compareTo(UserObject.getFirstName((User) AnonymousClass1.this.val$usersDict.get(tl_contact2.user_id)));
                        }
                    });
                    ConcurrentHashMap<Integer, TL_contact> contactsDictionary = new ConcurrentHashMap(20, 1.0f, 2);
                    HashMap<String, ArrayList<TL_contact>> sectionsDict = new HashMap();
                    HashMap<String, ArrayList<TL_contact>> sectionsDictMutual = new HashMap();
                    ArrayList<String> sortedSectionsArray = new ArrayList();
                    ArrayList<String> sortedSectionsArrayMutual = new ArrayList();
                    HashMap<String, TL_contact> contactsByPhonesDict = null;
                    HashMap<String, TL_contact> contactsByPhonesShortDict = null;
                    if (!ContactsController.this.contactsBookLoaded) {
                        contactsByPhonesDict = new HashMap();
                        contactsByPhonesShortDict = new HashMap();
                    }
                    HashMap<String, TL_contact> contactsByPhonesDict2 = contactsByPhonesDict;
                    HashMap<String, TL_contact> contactsByPhonesShortDict2 = contactsByPhonesShortDict;
                    HashMap<String, TL_contact> contactsByPhonesDictFinal = contactsByPhonesDict2;
                    HashMap<String, TL_contact> contactsByPhonesShortDictFinal2 = contactsByPhonesShortDict2;
                    a = 0;
                    while (a < contactsArr.size()) {
                        contact = (TL_contact) contactsArr.get(a);
                        User user = (User) anonymousClass1.val$usersDict.get(contact.user_id);
                        if (user == null) {
                            contactsByPhonesShortDictFinal = contactsByPhonesShortDictFinal2;
                        } else {
                            String key;
                            ArrayList<TL_contact> arr;
                            contactsDictionary.put(Integer.valueOf(contact.user_id), contact);
                            if (!(contactsByPhonesDict2 == null || TextUtils.isEmpty(user.phone))) {
                                contactsByPhonesDict2.put(user.phone, contact);
                                contactsByPhonesShortDict2.put(user.phone.substring(Math.max(i, user.phone.length() - 7)), contact);
                            }
                            String key2 = UserObject.getFirstName(user);
                            if (key2.length() > 1) {
                                key2 = key2.substring(0, 1);
                            }
                            if (key2.length() == 0) {
                                key = "#";
                            } else {
                                key = key2.toUpperCase();
                            }
                            key2 = (String) ContactsController.this.sectionsToReplace.get(key);
                            if (key2 != null) {
                                key = key2;
                            }
                            ArrayList<TL_contact> arr2 = (ArrayList) sectionsDict.get(key);
                            if (arr2 == null) {
                                arr = new ArrayList();
                                sectionsDict.put(key, arr);
                                sortedSectionsArray.add(key);
                            } else {
                                arr = arr2;
                            }
                            arr.add(contact);
                            contactsByPhonesShortDictFinal = contactsByPhonesShortDictFinal2;
                            if (user.mutual_contact != null) {
                                contactsByPhonesShortDictFinal2 = (ArrayList) sectionsDictMutual.get(key);
                                if (contactsByPhonesShortDictFinal2 == null) {
                                    contactsByPhonesShortDictFinal2 = new ArrayList();
                                    sectionsDictMutual.put(key, contactsByPhonesShortDictFinal2);
                                    sortedSectionsArrayMutual.add(key);
                                }
                                contactsByPhonesShortDictFinal2.add(contact);
                            }
                        }
                        a++;
                        contactsByPhonesShortDictFinal2 = contactsByPhonesShortDictFinal;
                        i = 0;
                    }
                    contactsByPhonesShortDictFinal = contactsByPhonesShortDictFinal2;
                    Collections.sort(sortedSectionsArray, new Comparator<String>() {
                        public int compare(String s, String s2) {
                            char cv1 = s.charAt(0);
                            char cv2 = s2.charAt(0);
                            if (cv1 == '#') {
                                return 1;
                            }
                            if (cv2 == '#') {
                                return -1;
                            }
                            return s.compareTo(s2);
                        }
                    });
                    Collections.sort(sortedSectionsArrayMutual, new Comparator<String>() {
                        public int compare(String s, String s2) {
                            char cv1 = s.charAt(0);
                            char cv2 = s2.charAt(0);
                            if (cv1 == '#') {
                                return 1;
                            }
                            if (cv2 == '#') {
                                return -1;
                            }
                            return s.compareTo(s2);
                        }
                    });
                    final ConcurrentHashMap<Integer, TL_contact> concurrentHashMap = contactsDictionary;
                    final HashMap<String, ArrayList<TL_contact>> hashMap = sectionsDict;
                    final HashMap<String, TL_contact> contactsByPhonesShortDictFinal3 = contactsByPhonesShortDictFinal;
                    final HashMap<String, ArrayList<TL_contact>> hashMap2 = sectionsDictMutual;
                    final HashMap<String, TL_contact> contactsByPhonesDictFinal2 = contactsByPhonesDictFinal;
                    final ArrayList<String> arrayList = sortedSectionsArray;
                    final ArrayList<String> contactsByPhonesShortDict3 = sortedSectionsArrayMutual;
                    AndroidUtilities.runOnUIThread(new Runnable() {
                        public void run() {
                            ContactsController.this.contacts = contactsArr;
                            ContactsController.this.contactsDict = concurrentHashMap;
                            ContactsController.this.usersSectionsDict = hashMap;
                            ContactsController.this.usersMutualSectionsDict = hashMap2;
                            ContactsController.this.sortedUsersSectionsArray = arrayList;
                            ContactsController.this.sortedUsersMutualSectionsArray = contactsByPhonesShortDict3;
                            if (from != 2) {
                                synchronized (ContactsController.this.loadContactsSync) {
                                    ContactsController.this.loadingContacts = false;
                                }
                            }
                            ContactsController.this.performWriteContactsToPhoneBook();
                            ContactsController.this.updateUnregisteredContacts(contactsArr);
                            NotificationCenter.getInstance(ContactsController.this.currentAccount).postNotificationName(NotificationCenter.contactsDidLoaded, new Object[0]);
                            if (from == 1 || AnonymousClass1.this.val$isEmpty) {
                                ContactsController.this.reloadContactsStatusesMaybe();
                            } else {
                                ContactsController.this.saveContactsLoadTime();
                            }
                        }
                    });
                    if (!ContactsController.this.delayedContactsUpdate.isEmpty() && ContactsController.this.contactsLoaded && ContactsController.this.contactsBookLoaded) {
                        ContactsController.this.applyContactsUpdates(ContactsController.this.delayedContactsUpdate, null, null, null);
                        ContactsController.this.delayedContactsUpdate.clear();
                    }
                    if (contactsByPhonesDictFinal2 != null) {
                        AndroidUtilities.runOnUIThread(new Runnable() {
                            public void run() {
                                Utilities.globalQueue.postRunnable(new Runnable() {
                                    public void run() {
                                        ContactsController.this.contactsByPhone = contactsByPhonesDictFinal2;
                                        ContactsController.this.contactsByShortPhone = contactsByPhonesShortDictFinal3;
                                    }
                                });
                                if (!ContactsController.this.contactsSyncInProgress) {
                                    ContactsController.this.contactsSyncInProgress = true;
                                    MessagesStorage.getInstance(ContactsController.this.currentAccount).getCachedPhoneBook(false);
                                }
                            }
                        });
                    } else {
                        ContactsController.this.contactsLoaded = true;
                    }
                }
            }

            public void run() {
                /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.ContactsController.12.run():void
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
                r0 = org.telegram.messenger.ContactsController.this;
                r0 = r0.currentAccount;
                r0 = org.telegram.messenger.MessagesController.getInstance(r0);
                r1 = r3;
                r2 = r4;
                r3 = 0;
                r4 = 1;
                if (r2 != r4) goto L_0x0014;
            L_0x0012:
                r2 = r4;
                goto L_0x0015;
            L_0x0014:
                r2 = r3;
            L_0x0015:
                r0.putUsers(r1, r2);
                r0 = new android.util.SparseArray;
                r0.<init>();
                r1 = r2;
                r1 = r1.isEmpty();
                r2 = org.telegram.messenger.ContactsController.this;
                r2 = r2.contacts;
                r2 = r2.isEmpty();
                if (r2 != 0) goto L_0x0060;
            L_0x002d:
                r2 = r3;
                r5 = r2;
                r5 = r5.size();
                if (r2 >= r5) goto L_0x0057;
                r5 = r2;
                r5 = r5.get(r2);
                r5 = (org.telegram.tgnet.TLRPC.TL_contact) r5;
                r6 = org.telegram.messenger.ContactsController.this;
                r6 = r6.contactsDict;
                r7 = r5.user_id;
                r7 = java.lang.Integer.valueOf(r7);
                r6 = r6.get(r7);
                if (r6 == 0) goto L_0x0055;
                r6 = r2;
                r6.remove(r2);
                r2 = r2 + -1;
                r2 = r2 + r4;
                goto L_0x002e;
                r2 = r2;
                r4 = org.telegram.messenger.ContactsController.this;
                r4 = r4.contacts;
                r2.addAll(r4);
                r2 = r3;
                r3 = r2;
                r3 = r3.size();
                if (r2 >= r3) goto L_0x0090;
                r3 = org.telegram.messenger.ContactsController.this;
                r3 = r3.currentAccount;
                r3 = org.telegram.messenger.MessagesController.getInstance(r3);
                r4 = r2;
                r4 = r4.get(r2);
                r4 = (org.telegram.tgnet.TLRPC.TL_contact) r4;
                r4 = r4.user_id;
                r4 = java.lang.Integer.valueOf(r4);
                r3 = r3.getUser(r4);
                if (r3 == 0) goto L_0x008d;
                r4 = r3.id;
                r0.put(r4, r3);
                r3 = r2 + 1;
                goto L_0x0061;
                r2 = org.telegram.messenger.Utilities.stageQueue;
                r3 = new org.telegram.messenger.ContactsController$12$1;
                r3.<init>(r0, r1);
                r2.postRunnable(r3);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.ContactsController.12.run():void");
            }
        });
    }

    private void reloadContactsStatusesMaybe() {
        try {
            if (MessagesController.getMainSettings(this.currentAccount).getLong("lastReloadStatusTime", 0) < System.currentTimeMillis() - 86400000) {
                reloadContactsStatuses();
            }
        } catch (Throwable e) {
            FileLog.e(e);
        }
    }

    private void saveContactsLoadTime() {
        try {
            MessagesController.getMainSettings(this.currentAccount).edit().putLong("lastReloadStatusTime", System.currentTimeMillis()).commit();
        } catch (Throwable e) {
            FileLog.e(e);
        }
    }

    private void updateUnregisteredContacts(ArrayList<TL_contact> contactsArr) {
        HashMap<String, TL_contact> contactsPhonesShort = new HashMap();
        for (int a = 0; a < contactsArr.size(); a++) {
            TL_contact value = (TL_contact) contactsArr.get(a);
            User user = MessagesController.getInstance(this.currentAccount).getUser(Integer.valueOf(value.user_id));
            if (user != null) {
                if (!TextUtils.isEmpty(user.phone)) {
                    contactsPhonesShort.put(user.phone, value);
                }
            }
        }
        ArrayList<Contact> sortedPhoneBookContacts = new ArrayList();
        for (Entry<String, Contact> pair : this.contactsBook.entrySet()) {
            Contact value2 = (Contact) pair.getValue();
            boolean skip = false;
            int a2 = 0;
            while (a2 < value2.phones.size()) {
                if (!contactsPhonesShort.containsKey((String) value2.shortPhones.get(a2))) {
                    if (((Integer) value2.phoneDeleted.get(a2)).intValue() != 1) {
                        a2++;
                    }
                }
                skip = true;
            }
            if (!skip) {
                sortedPhoneBookContacts.add(value2);
            }
        }
        Collections.sort(sortedPhoneBookContacts, new Comparator<Contact>() {
            public int compare(Contact contact, Contact contact2) {
                String toComapre1 = contact.first_name;
                if (toComapre1.length() == 0) {
                    toComapre1 = contact.last_name;
                }
                String toComapre2 = contact2.first_name;
                if (toComapre2.length() == 0) {
                    toComapre2 = contact2.last_name;
                }
                return toComapre1.compareTo(toComapre2);
            }
        });
        this.phoneBookContacts = sortedPhoneBookContacts;
    }

    private void buildContactsSectionsArrays(boolean sort) {
        if (sort) {
            Collections.sort(this.contacts, new Comparator<TL_contact>() {
                public int compare(TL_contact tl_contact, TL_contact tl_contact2) {
                    return UserObject.getFirstName(MessagesController.getInstance(ContactsController.this.currentAccount).getUser(Integer.valueOf(tl_contact.user_id))).compareTo(UserObject.getFirstName(MessagesController.getInstance(ContactsController.this.currentAccount).getUser(Integer.valueOf(tl_contact2.user_id))));
                }
            });
        }
        HashMap<String, ArrayList<TL_contact>> sectionsDict = new HashMap();
        ArrayList<String> sortedSectionsArray = new ArrayList();
        for (int a = 0; a < this.contacts.size(); a++) {
            TL_contact value = (TL_contact) this.contacts.get(a);
            User user = MessagesController.getInstance(this.currentAccount).getUser(Integer.valueOf(value.user_id));
            if (user != null) {
                String key = UserObject.getFirstName(user);
                if (key.length() > 1) {
                    key = key.substring(0, 1);
                }
                if (key.length() == 0) {
                    key = "#";
                } else {
                    key = key.toUpperCase();
                }
                String replace = (String) this.sectionsToReplace.get(key);
                if (replace != null) {
                    key = replace;
                }
                ArrayList<TL_contact> arr = (ArrayList) sectionsDict.get(key);
                if (arr == null) {
                    arr = new ArrayList();
                    sectionsDict.put(key, arr);
                    sortedSectionsArray.add(key);
                }
                arr.add(value);
            }
        }
        Collections.sort(sortedSectionsArray, new Comparator<String>() {
            public int compare(String s, String s2) {
                char cv1 = s.charAt(0);
                char cv2 = s2.charAt(0);
                if (cv1 == '#') {
                    return 1;
                }
                if (cv2 == '#') {
                    return -1;
                }
                return s.compareTo(s2);
            }
        });
        this.usersSectionsDict = sectionsDict;
        this.sortedUsersSectionsArray = sortedSectionsArray;
    }

    private void performWriteContactsToPhoneBookInternal(ArrayList<TL_contact> contactsArray) {
        try {
            if (hasContactsPermission()) {
                Uri rawContactUri = RawContacts.CONTENT_URI.buildUpon().appendQueryParameter("account_name", this.systemAccount.name).appendQueryParameter("account_type", this.systemAccount.type).build();
                Cursor c1 = ApplicationLoader.applicationContext.getContentResolver().query(rawContactUri, new String[]{"_id", "sync2"}, null, null, null);
                SparseLongArray bookContacts = new SparseLongArray();
                if (c1 != null) {
                    while (c1.moveToNext()) {
                        bookContacts.put(c1.getInt(1), c1.getLong(0));
                    }
                    c1.close();
                    for (int a = 0; a < contactsArray.size(); a++) {
                        TL_contact u = (TL_contact) contactsArray.get(a);
                        if (bookContacts.indexOfKey(u.user_id) < 0) {
                            addContactToPhoneBook(MessagesController.getInstance(this.currentAccount).getUser(Integer.valueOf(u.user_id)), false);
                        }
                    }
                }
            }
        } catch (Throwable e) {
            FileLog.e(e);
        }
    }

    private void performWriteContactsToPhoneBook() {
        final ArrayList<TL_contact> contactsArray = new ArrayList();
        contactsArray.addAll(this.contacts);
        Utilities.phoneBookQueue.postRunnable(new Runnable() {
            public void run() {
                ContactsController.this.performWriteContactsToPhoneBookInternal(contactsArray);
            }
        });
    }

    public void processContactsUpdates(ArrayList<Integer> ids, ConcurrentHashMap<Integer, User> userDict) {
        ArrayList<TL_contact> newContacts = new ArrayList();
        ArrayList<Integer> contactsToDelete = new ArrayList();
        Iterator it = ids.iterator();
        while (it.hasNext()) {
            Integer uid = (Integer) it.next();
            if (uid.intValue() > 0) {
                TL_contact contact = new TL_contact();
                contact.user_id = uid.intValue();
                newContacts.add(contact);
                if (!this.delayedContactsUpdate.isEmpty()) {
                    int idx = this.delayedContactsUpdate.indexOf(Integer.valueOf(-uid.intValue()));
                    if (idx != -1) {
                        this.delayedContactsUpdate.remove(idx);
                    }
                }
            } else if (uid.intValue() < 0) {
                contactsToDelete.add(Integer.valueOf(-uid.intValue()));
                if (!this.delayedContactsUpdate.isEmpty()) {
                    int idx2 = this.delayedContactsUpdate.indexOf(Integer.valueOf(-uid.intValue()));
                    if (idx2 != -1) {
                        this.delayedContactsUpdate.remove(idx2);
                    }
                }
            }
        }
        if (!contactsToDelete.isEmpty()) {
            MessagesStorage.getInstance(this.currentAccount).deleteContacts(contactsToDelete);
        }
        if (!newContacts.isEmpty()) {
            MessagesStorage.getInstance(this.currentAccount).putContacts(newContacts, false);
        }
        if (this.contactsLoaded) {
            if (this.contactsBookLoaded) {
                applyContactsUpdates(ids, userDict, newContacts, contactsToDelete);
                return;
            }
        }
        this.delayedContactsUpdate.addAll(ids);
        if (BuildVars.LOGS_ENABLED) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("delay update - contacts add = ");
            stringBuilder.append(newContacts.size());
            stringBuilder.append(" delete = ");
            stringBuilder.append(contactsToDelete.size());
            FileLog.d(stringBuilder.toString());
        }
    }

    public long addContactToPhoneBook(User user, boolean check) {
        if (!(this.systemAccount == null || user == null)) {
            if (!TextUtils.isEmpty(user.phone)) {
                if (!hasContactsPermission()) {
                    return -1;
                }
                long res = -1;
                synchronized (this.observerLock) {
                    this.ignoreChanges = true;
                }
                ContentResolver contentResolver = ApplicationLoader.applicationContext.getContentResolver();
                if (check) {
                    try {
                        Uri rawContactUri = RawContacts.CONTENT_URI.buildUpon().appendQueryParameter("caller_is_syncadapter", "true").appendQueryParameter("account_name", this.systemAccount.name).appendQueryParameter("account_type", this.systemAccount.type).build();
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("sync2 = ");
                        stringBuilder.append(user.id);
                        contentResolver.delete(rawContactUri, stringBuilder.toString(), null);
                    } catch (Throwable e) {
                        FileLog.e(e);
                    }
                }
                ArrayList<ContentProviderOperation> query = new ArrayList();
                Builder builder = ContentProviderOperation.newInsert(RawContacts.CONTENT_URI);
                builder.withValue("account_name", this.systemAccount.name);
                builder.withValue("account_type", this.systemAccount.type);
                builder.withValue("sync1", user.phone);
                builder.withValue("sync2", Integer.valueOf(user.id));
                query.add(builder.build());
                builder = ContentProviderOperation.newInsert(Data.CONTENT_URI);
                builder.withValueBackReference("raw_contact_id", 0);
                builder.withValue("mimetype", "vnd.android.cursor.item/name");
                builder.withValue("data2", user.first_name);
                builder.withValue("data3", user.last_name);
                query.add(builder.build());
                Builder builder2 = ContentProviderOperation.newInsert(Data.CONTENT_URI);
                builder2.withValueBackReference("raw_contact_id", 0);
                builder2.withValue("mimetype", "vnd.android.cursor.item/vnd.org.telegram.messenger.android.profile");
                builder2.withValue("data1", Integer.valueOf(user.id));
                builder2.withValue("data2", "Telegram Profile");
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append("+");
                stringBuilder2.append(user.phone);
                builder2.withValue("data3", stringBuilder2.toString());
                builder2.withValue("data4", Integer.valueOf(user.id));
                query.add(builder2.build());
                try {
                    ContentProviderResult[] result = contentResolver.applyBatch("com.android.contacts", query);
                    if (!(result == null || result.length <= 0 || result[0].uri == null)) {
                        res = Long.parseLong(result[0].uri.getLastPathSegment());
                    }
                } catch (Throwable e2) {
                    FileLog.e(e2);
                }
                long res2 = res;
                synchronized (this.observerLock) {
                    this.ignoreChanges = false;
                }
                return res2;
            }
        }
        return -1;
    }

    private void deleteContactFromPhoneBook(int uid) {
        if (hasContactsPermission()) {
            synchronized (this.observerLock) {
                this.ignoreChanges = true;
            }
            try {
                ContentResolver contentResolver = ApplicationLoader.applicationContext.getContentResolver();
                Uri rawContactUri = RawContacts.CONTENT_URI.buildUpon().appendQueryParameter("caller_is_syncadapter", "true").appendQueryParameter("account_name", this.systemAccount.name).appendQueryParameter("account_type", this.systemAccount.type).build();
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("sync2 = ");
                stringBuilder.append(uid);
                contentResolver.delete(rawContactUri, stringBuilder.toString(), null);
            } catch (Throwable e) {
                FileLog.e(e);
            }
            synchronized (this.observerLock) {
                this.ignoreChanges = false;
            }
        }
    }

    protected void markAsContacted(final String contactId) {
        if (contactId != null) {
            Utilities.phoneBookQueue.postRunnable(new Runnable() {
                public void run() {
                    Uri uri = Uri.parse(contactId);
                    ContentValues values = new ContentValues();
                    values.put("last_time_contacted", Long.valueOf(System.currentTimeMillis()));
                    ApplicationLoader.applicationContext.getContentResolver().update(uri, values, null, null);
                }
            });
        }
    }

    public void addContact(User user) {
        if (user != null) {
            if (!TextUtils.isEmpty(user.phone)) {
                TL_contacts_importContacts req = new TL_contacts_importContacts();
                ArrayList<TL_inputPhoneContact> contactsParams = new ArrayList();
                TL_inputPhoneContact c = new TL_inputPhoneContact();
                c.phone = user.phone;
                if (!c.phone.startsWith("+")) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("+");
                    stringBuilder.append(c.phone);
                    c.phone = stringBuilder.toString();
                }
                c.first_name = user.first_name;
                c.last_name = user.last_name;
                c.client_id = 0;
                contactsParams.add(c);
                req.contacts = contactsParams;
                ConnectionsManager.getInstance(this.currentAccount).sendRequest(req, new RequestDelegate() {
                    public void run(TLObject response, TL_error error) {
                        if (error == null) {
                            final TL_contacts_importedContacts res = (TL_contacts_importedContacts) response;
                            MessagesStorage.getInstance(ContactsController.this.currentAccount).putUsersAndChats(res.users, null, true, true);
                            for (int a = 0; a < res.users.size(); a++) {
                                final User u = (User) res.users.get(a);
                                Utilities.phoneBookQueue.postRunnable(new Runnable() {
                                    public void run() {
                                        ContactsController.this.addContactToPhoneBook(u, true);
                                    }
                                });
                                TL_contact newContact = new TL_contact();
                                newContact.user_id = u.id;
                                ArrayList<TL_contact> arrayList = new ArrayList();
                                arrayList.add(newContact);
                                MessagesStorage.getInstance(ContactsController.this.currentAccount).putContacts(arrayList, false);
                                if (!TextUtils.isEmpty(u.phone)) {
                                    CharSequence name = ContactsController.formatName(u.first_name, u.last_name);
                                    MessagesStorage.getInstance(ContactsController.this.currentAccount).applyPhoneBookUpdates(u.phone, TtmlNode.ANONYMOUS_REGION_ID);
                                    Contact contact = (Contact) ContactsController.this.contactsBookSPhones.get(u.phone);
                                    if (contact != null) {
                                        int index = contact.shortPhones.indexOf(u.phone);
                                        if (index != -1) {
                                            contact.phoneDeleted.set(index, Integer.valueOf(0));
                                        }
                                    }
                                }
                            }
                            AndroidUtilities.runOnUIThread(new Runnable() {
                                public void run() {
                                    Iterator it = res.users.iterator();
                                    while (it.hasNext()) {
                                        User u = (User) it.next();
                                        MessagesController.getInstance(ContactsController.this.currentAccount).putUser(u, false);
                                        if (ContactsController.this.contactsDict.get(Integer.valueOf(u.id)) == null) {
                                            TL_contact newContact = new TL_contact();
                                            newContact.user_id = u.id;
                                            ContactsController.this.contacts.add(newContact);
                                            ContactsController.this.contactsDict.put(Integer.valueOf(newContact.user_id), newContact);
                                        }
                                    }
                                    ContactsController.this.buildContactsSectionsArrays(true);
                                    NotificationCenter.getInstance(ContactsController.this.currentAccount).postNotificationName(NotificationCenter.contactsDidLoaded, new Object[0]);
                                }
                            });
                        }
                    }
                }, 6);
            }
        }
    }

    public void deleteContact(final ArrayList<User> users) {
        if (users != null) {
            if (!users.isEmpty()) {
                TL_contacts_deleteContacts req = new TL_contacts_deleteContacts();
                final ArrayList<Integer> uids = new ArrayList();
                Iterator it = users.iterator();
                while (it.hasNext()) {
                    User user = (User) it.next();
                    InputUser inputUser = MessagesController.getInstance(this.currentAccount).getInputUser(user);
                    if (inputUser != null) {
                        uids.add(Integer.valueOf(user.id));
                        req.id.add(inputUser);
                    }
                }
                ConnectionsManager.getInstance(this.currentAccount).sendRequest(req, new RequestDelegate() {
                    public void run(TLObject response, TL_error error) {
                        if (error == null) {
                            MessagesStorage.getInstance(ContactsController.this.currentAccount).deleteContacts(uids);
                            Utilities.phoneBookQueue.postRunnable(new Runnable() {
                                public void run() {
                                    Iterator it = users.iterator();
                                    while (it.hasNext()) {
                                        ContactsController.this.deleteContactFromPhoneBook(((User) it.next()).id);
                                    }
                                }
                            });
                            for (int a = 0; a < users.size(); a++) {
                                User user = (User) users.get(a);
                                if (!TextUtils.isEmpty(user.phone)) {
                                    CharSequence name = UserObject.getUserName(user);
                                    MessagesStorage.getInstance(ContactsController.this.currentAccount).applyPhoneBookUpdates(user.phone, TtmlNode.ANONYMOUS_REGION_ID);
                                    Contact contact = (Contact) ContactsController.this.contactsBookSPhones.get(user.phone);
                                    if (contact != null) {
                                        int index = contact.shortPhones.indexOf(user.phone);
                                        if (index != -1) {
                                            contact.phoneDeleted.set(index, Integer.valueOf(1));
                                        }
                                    }
                                }
                            }
                            AndroidUtilities.runOnUIThread(new Runnable() {
                                public void run() {
                                    boolean remove = false;
                                    Iterator it = users.iterator();
                                    while (it.hasNext()) {
                                        User user = (User) it.next();
                                        TL_contact contact = (TL_contact) ContactsController.this.contactsDict.get(Integer.valueOf(user.id));
                                        if (contact != null) {
                                            remove = true;
                                            ContactsController.this.contacts.remove(contact);
                                            ContactsController.this.contactsDict.remove(Integer.valueOf(user.id));
                                        }
                                    }
                                    if (remove) {
                                        ContactsController.this.buildContactsSectionsArrays(false);
                                    }
                                    NotificationCenter.getInstance(ContactsController.this.currentAccount).postNotificationName(NotificationCenter.updateInterfaces, Integer.valueOf(1));
                                    NotificationCenter.getInstance(ContactsController.this.currentAccount).postNotificationName(NotificationCenter.contactsDidLoaded, new Object[0]);
                                }
                            });
                        }
                    }
                });
            }
        }
    }

    public void reloadContactsStatuses() {
        saveContactsLoadTime();
        MessagesController.getInstance(this.currentAccount).clearFullUsers();
        final Editor editor = MessagesController.getMainSettings(this.currentAccount).edit();
        editor.putBoolean("needGetStatuses", true).commit();
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(new TL_contacts_getStatuses(), new RequestDelegate() {
            public void run(final TLObject response, TL_error error) {
                if (error == null) {
                    AndroidUtilities.runOnUIThread(new Runnable() {
                        public void run() {
                            editor.remove("needGetStatuses").commit();
                            Vector vector = response;
                            if (!vector.objects.isEmpty()) {
                                ArrayList<User> dbUsersStatus = new ArrayList();
                                Iterator it = vector.objects.iterator();
                                while (it.hasNext()) {
                                    TL_contactStatus object = it.next();
                                    User toDbUser = new TL_user();
                                    TL_contactStatus status = object;
                                    if (status != null) {
                                        if (status.status instanceof TL_userStatusRecently) {
                                            status.status.expires = -100;
                                        } else if (status.status instanceof TL_userStatusLastWeek) {
                                            status.status.expires = -101;
                                        } else if (status.status instanceof TL_userStatusLastMonth) {
                                            status.status.expires = -102;
                                        }
                                        User user = MessagesController.getInstance(ContactsController.this.currentAccount).getUser(Integer.valueOf(status.user_id));
                                        if (user != null) {
                                            user.status = status.status;
                                        }
                                        toDbUser.status = status.status;
                                        dbUsersStatus.add(toDbUser);
                                    }
                                }
                                MessagesStorage.getInstance(ContactsController.this.currentAccount).updateUsers(dbUsersStatus, true, true, true);
                            }
                            NotificationCenter.getInstance(ContactsController.this.currentAccount).postNotificationName(NotificationCenter.updateInterfaces, Integer.valueOf(4));
                        }
                    });
                }
            }
        });
    }

    public void loadPrivacySettings() {
        if (this.loadingDeleteInfo == 0) {
            this.loadingDeleteInfo = 1;
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(new TL_account_getAccountTTL(), new RequestDelegate() {
                public void run(final TLObject response, final TL_error error) {
                    AndroidUtilities.runOnUIThread(new Runnable() {
                        public void run() {
                            if (error == null) {
                                ContactsController.this.deleteAccountTTL = response.days;
                                ContactsController.this.loadingDeleteInfo = 2;
                            } else {
                                ContactsController.this.loadingDeleteInfo = 0;
                            }
                            NotificationCenter.getInstance(ContactsController.this.currentAccount).postNotificationName(NotificationCenter.privacyRulesUpdated, new Object[0]);
                        }
                    });
                }
            });
        }
        if (this.loadingLastSeenInfo == 0) {
            this.loadingLastSeenInfo = 1;
            TL_account_getPrivacy req = new TL_account_getPrivacy();
            req.key = new TL_inputPrivacyKeyStatusTimestamp();
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(req, new RequestDelegate() {
                public void run(final TLObject response, final TL_error error) {
                    AndroidUtilities.runOnUIThread(new Runnable() {
                        public void run() {
                            if (error == null) {
                                TL_account_privacyRules rules = response;
                                MessagesController.getInstance(ContactsController.this.currentAccount).putUsers(rules.users, false);
                                ContactsController.this.privacyRules = rules.rules;
                                ContactsController.this.loadingLastSeenInfo = 2;
                            } else {
                                ContactsController.this.loadingLastSeenInfo = 0;
                            }
                            NotificationCenter.getInstance(ContactsController.this.currentAccount).postNotificationName(NotificationCenter.privacyRulesUpdated, new Object[0]);
                        }
                    });
                }
            });
        }
        if (this.loadingCallsInfo == 0) {
            this.loadingCallsInfo = 1;
            req = new TL_account_getPrivacy();
            req.key = new TL_inputPrivacyKeyPhoneCall();
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(req, new RequestDelegate() {
                public void run(final TLObject response, final TL_error error) {
                    AndroidUtilities.runOnUIThread(new Runnable() {
                        public void run() {
                            if (error == null) {
                                TL_account_privacyRules rules = response;
                                MessagesController.getInstance(ContactsController.this.currentAccount).putUsers(rules.users, false);
                                ContactsController.this.callPrivacyRules = rules.rules;
                                ContactsController.this.loadingCallsInfo = 2;
                            } else {
                                ContactsController.this.loadingCallsInfo = 0;
                            }
                            NotificationCenter.getInstance(ContactsController.this.currentAccount).postNotificationName(NotificationCenter.privacyRulesUpdated, new Object[0]);
                        }
                    });
                }
            });
        }
        if (this.loadingGroupInfo == 0) {
            this.loadingGroupInfo = 1;
            req = new TL_account_getPrivacy();
            req.key = new TL_inputPrivacyKeyChatInvite();
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(req, new RequestDelegate() {
                public void run(final TLObject response, final TL_error error) {
                    AndroidUtilities.runOnUIThread(new Runnable() {
                        public void run() {
                            if (error == null) {
                                TL_account_privacyRules rules = response;
                                MessagesController.getInstance(ContactsController.this.currentAccount).putUsers(rules.users, false);
                                ContactsController.this.groupPrivacyRules = rules.rules;
                                ContactsController.this.loadingGroupInfo = 2;
                            } else {
                                ContactsController.this.loadingGroupInfo = 0;
                            }
                            NotificationCenter.getInstance(ContactsController.this.currentAccount).postNotificationName(NotificationCenter.privacyRulesUpdated, new Object[0]);
                        }
                    });
                }
            });
        }
        NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.privacyRulesUpdated, new Object[0]);
    }

    public void setDeleteAccountTTL(int ttl) {
        this.deleteAccountTTL = ttl;
    }

    public int getDeleteAccountTTL() {
        return this.deleteAccountTTL;
    }

    public boolean getLoadingDeleteInfo() {
        return this.loadingDeleteInfo != 2;
    }

    public boolean getLoadingLastSeenInfo() {
        return this.loadingLastSeenInfo != 2;
    }

    public boolean getLoadingCallsInfo() {
        return this.loadingCallsInfo != 2;
    }

    public boolean getLoadingGroupInfo() {
        return this.loadingGroupInfo != 2;
    }

    public ArrayList<PrivacyRule> getPrivacyRules(int type) {
        if (type == 2) {
            return this.callPrivacyRules;
        }
        if (type == 1) {
            return this.groupPrivacyRules;
        }
        return this.privacyRules;
    }

    public void setPrivacyRules(ArrayList<PrivacyRule> rules, int type) {
        if (type == 2) {
            this.callPrivacyRules = rules;
        } else if (type == 1) {
            this.groupPrivacyRules = rules;
        } else {
            this.privacyRules = rules;
        }
        NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.privacyRulesUpdated, new Object[0]);
        reloadContactsStatuses();
    }

    public static String formatName(String firstName, String lastName) {
        if (firstName != null) {
            firstName = firstName.trim();
        }
        if (lastName != null) {
            lastName = lastName.trim();
        }
        int i = 0;
        int length = firstName != null ? firstName.length() : 0;
        if (lastName != null) {
            i = lastName.length();
        }
        StringBuilder result = new StringBuilder((length + i) + 1);
        if (LocaleController.nameDisplayOrder == 1) {
            if (firstName != null && firstName.length() > 0) {
                result.append(firstName);
                if (lastName != null && lastName.length() > 0) {
                    result.append(" ");
                    result.append(lastName);
                }
            } else if (lastName != null && lastName.length() > 0) {
                result.append(lastName);
            }
        } else if (lastName != null && lastName.length() > 0) {
            result.append(lastName);
            if (firstName != null && firstName.length() > 0) {
                result.append(" ");
                result.append(firstName);
            }
        } else if (firstName != null && firstName.length() > 0) {
            result.append(firstName);
        }
        return result.toString();
    }
}
