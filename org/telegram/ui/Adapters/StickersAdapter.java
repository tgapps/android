package org.telegram.ui.Adapters;

import android.content.Context;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.DataQuery;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.NotificationCenter.NotificationCenterDelegate;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.support.widget.RecyclerView.ViewHolder;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.Document;
import org.telegram.tgnet.TLRPC.DocumentAttribute;
import org.telegram.tgnet.TLRPC.TL_documentAttributeSticker;
import org.telegram.tgnet.TLRPC.TL_error;
import org.telegram.tgnet.TLRPC.TL_messages_getStickers;
import org.telegram.tgnet.TLRPC.TL_messages_stickers;
import org.telegram.ui.Cells.StickerCell;
import org.telegram.ui.Components.RecyclerListView.Holder;
import org.telegram.ui.Components.RecyclerListView.SelectionAdapter;

public class StickersAdapter extends SelectionAdapter implements NotificationCenterDelegate {
    private int currentAccount = UserConfig.selectedAccount;
    private boolean delayLocalResults;
    private StickersAdapterDelegate delegate;
    private int lastReqId;
    private String lastSticker;
    private Context mContext;
    private ArrayList<Document> stickers;
    private HashMap<String, Document> stickersMap;
    private ArrayList<String> stickersToLoad = new ArrayList();
    private boolean visible;

    class AnonymousClass1 implements Comparator<Document> {
        final /* synthetic */ ArrayList val$favsStickers;
        final /* synthetic */ ArrayList val$recentStickers;

        private int getIndex(long r1) {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.ui.Adapters.StickersAdapter.1.getIndex(long):int
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
            r0 = 0;
            r1 = r0;
            r2 = r5.val$favsStickers;
            r2 = r2.size();
            if (r1 >= r2) goto L_0x001e;
        L_0x000a:
            r2 = r5.val$favsStickers;
            r2 = r2.get(r1);
            r2 = (org.telegram.tgnet.TLRPC.Document) r2;
            r2 = r2.id;
            r4 = (r2 > r6 ? 1 : (r2 == r6 ? 0 : -1));
            if (r4 != 0) goto L_0x001b;
            r0 = r1 + 1000;
            return r0;
            r1 = r1 + 1;
            goto L_0x0002;
            r1 = r5.val$recentStickers;
            r1 = r1.size();
            if (r0 >= r1) goto L_0x0039;
            r1 = r5.val$recentStickers;
            r1 = r1.get(r0);
            r1 = (org.telegram.tgnet.TLRPC.Document) r1;
            r1 = r1.id;
            r3 = (r1 > r6 ? 1 : (r1 == r6 ? 0 : -1));
            if (r3 != 0) goto L_0x0036;
            return r0;
            r0 = r0 + 1;
            goto L_0x001f;
            r0 = -1;
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: org.telegram.ui.Adapters.StickersAdapter.1.getIndex(long):int");
        }

        AnonymousClass1(ArrayList arrayList, ArrayList arrayList2) {
            this.val$favsStickers = arrayList;
            this.val$recentStickers = arrayList2;
        }

        public int compare(Document lhs, Document rhs) {
            int idx1 = getIndex(lhs.id);
            int idx2 = getIndex(rhs.id);
            if (idx1 > idx2) {
                return -1;
            }
            if (idx1 < idx2) {
                return 1;
            }
            return 0;
        }
    }

    public interface StickersAdapterDelegate {
        void needChangePanelVisibility(boolean z);
    }

    public void loadStikersForEmoji(java.lang.CharSequence r1) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.ui.Adapters.StickersAdapter.loadStikersForEmoji(java.lang.CharSequence):void
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
        r0 = org.telegram.messenger.SharedConfig.suggestStickers;
        r1 = 2;
        if (r0 != r1) goto L_0x0006;
    L_0x0005:
        return;
    L_0x0006:
        r0 = 1;
        r2 = 0;
        if (r14 == 0) goto L_0x001a;
    L_0x000a:
        r3 = r14.length();
        if (r3 <= 0) goto L_0x001a;
    L_0x0010:
        r3 = r14.length();
        r4 = 14;
        if (r3 > r4) goto L_0x001a;
    L_0x0018:
        r3 = r0;
        goto L_0x001b;
    L_0x001a:
        r3 = r2;
    L_0x001b:
        if (r3 == 0) goto L_0x01b2;
    L_0x001d:
        r4 = r14.length();
        r5 = r14;
        r14 = r2;
    L_0x0023:
        if (r14 >= r4) goto L_0x00a9;
    L_0x0025:
        r6 = r4 + -1;
        if (r14 >= r6) goto L_0x0081;
    L_0x0029:
        r6 = r5.charAt(r14);
        r7 = 55356; // 0xd83c float:7.757E-41 double:2.73495E-319;
        if (r6 != r7) goto L_0x0048;
    L_0x0032:
        r6 = r14 + 1;
        r6 = r5.charAt(r6);
        r7 = 57339; // 0xdffb float:8.0349E-41 double:2.8329E-319;
        if (r6 < r7) goto L_0x0048;
    L_0x003d:
        r6 = r14 + 1;
        r6 = r5.charAt(r6);
        r7 = 57343; // 0xdfff float:8.0355E-41 double:2.8331E-319;
        if (r6 <= r7) goto L_0x0064;
    L_0x0048:
        r6 = r5.charAt(r14);
        r7 = 8205; // 0x200d float:1.1498E-41 double:4.054E-320;
        if (r6 != r7) goto L_0x0081;
    L_0x0050:
        r6 = r14 + 1;
        r6 = r5.charAt(r6);
        r7 = 9792; // 0x2640 float:1.3722E-41 double:4.838E-320;
        if (r6 == r7) goto L_0x0064;
    L_0x005a:
        r6 = r14 + 1;
        r6 = r5.charAt(r6);
        r7 = 9794; // 0x2642 float:1.3724E-41 double:4.839E-320;
        if (r6 != r7) goto L_0x0081;
    L_0x0064:
        r6 = new java.lang.CharSequence[r1];
        r7 = r5.subSequence(r2, r14);
        r6[r2] = r7;
        r7 = r14 + 2;
        r8 = r5.length();
        r7 = r5.subSequence(r7, r8);
        r6[r0] = r7;
        r5 = android.text.TextUtils.concat(r6);
        r4 = r4 + -2;
        r14 = r14 + -1;
        goto L_0x00a6;
    L_0x0081:
        r6 = r5.charAt(r14);
        r7 = 65039; // 0xfe0f float:9.1139E-41 double:3.21335E-319;
        if (r6 != r7) goto L_0x00a6;
    L_0x008a:
        r6 = new java.lang.CharSequence[r1];
        r7 = r5.subSequence(r2, r14);
        r6[r2] = r7;
        r7 = r14 + 1;
        r8 = r5.length();
        r7 = r5.subSequence(r7, r8);
        r6[r0] = r7;
        r5 = android.text.TextUtils.concat(r6);
        r4 = r4 + -1;
        r14 = r14 + -1;
    L_0x00a6:
        r14 = r14 + r0;
        goto L_0x0023;
    L_0x00a9:
        r14 = r5.toString();
        r14 = r14.trim();
        r13.lastSticker = r14;
        r14 = r13.lastSticker;
        r14 = org.telegram.messenger.Emoji.isValidEmoji(r14);
        if (r14 != 0) goto L_0x00ca;
    L_0x00bb:
        r14 = r13.visible;
        if (r14 == 0) goto L_0x00c9;
    L_0x00bf:
        r13.visible = r2;
        r14 = r13.delegate;
        r14.needChangePanelVisibility(r2);
        r13.notifyDataSetChanged();
    L_0x00c9:
        return;
    L_0x00ca:
        r14 = 0;
        r13.stickers = r14;
        r13.stickersMap = r14;
        r13.delayLocalResults = r2;
        r6 = r13.currentAccount;
        r6 = org.telegram.messenger.DataQuery.getInstance(r6);
        r6 = r6.getRecentStickersNoCopy(r2);
        r7 = r13.currentAccount;
        r7 = org.telegram.messenger.DataQuery.getInstance(r7);
        r1 = r7.getRecentStickersNoCopy(r1);
        r7 = 0;
        r8 = 0;
        r9 = r6.size();
    L_0x00eb:
        r10 = 5;
        if (r8 >= r9) goto L_0x0107;
    L_0x00ee:
        r11 = r6.get(r8);
        r11 = (org.telegram.tgnet.TLRPC.Document) r11;
        r12 = r13.lastSticker;
        r12 = r13.isValidSticker(r11, r12);
        if (r12 == 0) goto L_0x0104;
    L_0x00fc:
        r13.addStickerToResult(r11);
        r7 = r7 + 1;
        if (r7 < r10) goto L_0x0104;
    L_0x0103:
        goto L_0x0107;
    L_0x0104:
        r8 = r8 + 1;
        goto L_0x00eb;
    L_0x0107:
        r8 = 0;
        r9 = r1.size();
    L_0x010c:
        if (r8 >= r9) goto L_0x0122;
    L_0x010e:
        r11 = r1.get(r8);
        r11 = (org.telegram.tgnet.TLRPC.Document) r11;
        r12 = r13.lastSticker;
        r12 = r13.isValidSticker(r11, r12);
        if (r12 == 0) goto L_0x011f;
    L_0x011c:
        r13.addStickerToResult(r11);
    L_0x011f:
        r8 = r8 + 1;
        goto L_0x010c;
    L_0x0122:
        r8 = r13.currentAccount;
        r8 = org.telegram.messenger.DataQuery.getInstance(r8);
        r8 = r8.getAllStickers();
        if (r8 == 0) goto L_0x0137;
    L_0x012e:
        r14 = r13.lastSticker;
        r14 = r8.get(r14);
        r14 = (java.util.ArrayList) r14;
    L_0x0137:
        if (r14 == 0) goto L_0x0155;
    L_0x0139:
        r9 = r14.isEmpty();
        if (r9 != 0) goto L_0x0155;
    L_0x013f:
        r9 = new java.util.ArrayList;
        r9.<init>(r14);
        r11 = r6.isEmpty();
        if (r11 != 0) goto L_0x0152;
    L_0x014a:
        r11 = new org.telegram.ui.Adapters.StickersAdapter$1;
        r11.<init>(r1, r6);
        java.util.Collections.sort(r9, r11);
    L_0x0152:
        r13.addStickersToResult(r9);
    L_0x0155:
        r9 = org.telegram.messenger.SharedConfig.suggestStickers;
        if (r9 != 0) goto L_0x015e;
    L_0x0159:
        r9 = r13.lastSticker;
        r13.searchServerStickers(r9);
    L_0x015e:
        r9 = r13.stickers;
        if (r9 == 0) goto L_0x01a4;
    L_0x0162:
        r9 = r13.stickers;
        r9 = r9.isEmpty();
        if (r9 != 0) goto L_0x01a4;
    L_0x016a:
        r9 = org.telegram.messenger.SharedConfig.suggestStickers;
        if (r9 != 0) goto L_0x0180;
    L_0x016e:
        r9 = r13.stickers;
        r9 = r9.size();
        if (r9 >= r10) goto L_0x0180;
    L_0x0176:
        r13.delayLocalResults = r0;
        r0 = r13.delegate;
        r0.needChangePanelVisibility(r2);
        r13.visible = r2;
        goto L_0x01a0;
    L_0x0180:
        r13.checkStickerFilesExistAndDownload();
        r9 = r13.delegate;
        r10 = r13.stickers;
        if (r10 == 0) goto L_0x019b;
    L_0x0189:
        r10 = r13.stickers;
        r10 = r10.isEmpty();
        if (r10 != 0) goto L_0x019b;
    L_0x0191:
        r10 = r13.stickersToLoad;
        r10 = r10.isEmpty();
        if (r10 == 0) goto L_0x019b;
    L_0x0199:
        r2 = r0;
    L_0x019b:
        r9.needChangePanelVisibility(r2);
        r13.visible = r0;
    L_0x01a0:
        r13.notifyDataSetChanged();
        goto L_0x01af;
    L_0x01a4:
        r0 = r13.visible;
        if (r0 == 0) goto L_0x01af;
        r0 = r13.delegate;
        r0.needChangePanelVisibility(r2);
        r13.visible = r2;
        r14 = r5;
        goto L_0x01c5;
    L_0x01b2:
        r0 = "";
        r13.lastSticker = r0;
        r0 = r13.visible;
        if (r0 == 0) goto L_0x01c5;
        r0 = r13.stickers;
        if (r0 == 0) goto L_0x01c5;
        r13.visible = r2;
        r0 = r13.delegate;
        r0.needChangePanelVisibility(r2);
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.ui.Adapters.StickersAdapter.loadStikersForEmoji(java.lang.CharSequence):void");
    }

    public StickersAdapter(Context context, StickersAdapterDelegate delegate) {
        this.mContext = context;
        this.delegate = delegate;
        DataQuery.getInstance(this.currentAccount).checkStickers(0);
        DataQuery.getInstance(this.currentAccount).checkStickers(1);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.FileDidLoaded);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.FileDidFailedLoad);
    }

    public void onDestroy() {
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.FileDidLoaded);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.FileDidFailedLoad);
    }

    public void didReceivedNotification(int id, int account, Object... args) {
        if ((id == NotificationCenter.FileDidLoaded || id == NotificationCenter.FileDidFailedLoad) && this.stickers != null && !this.stickers.isEmpty() && !this.stickersToLoad.isEmpty() && this.visible) {
            boolean z = false;
            this.stickersToLoad.remove(args[0]);
            if (this.stickersToLoad.isEmpty()) {
                StickersAdapterDelegate stickersAdapterDelegate = this.delegate;
                if (!(this.stickers == null || this.stickers.isEmpty() || !this.stickersToLoad.isEmpty())) {
                    z = true;
                }
                stickersAdapterDelegate.needChangePanelVisibility(z);
            }
        }
    }

    private boolean checkStickerFilesExistAndDownload() {
        if (this.stickers == null) {
            return false;
        }
        this.stickersToLoad.clear();
        int size = Math.min(10, this.stickers.size());
        for (int a = 0; a < size; a++) {
            Document document = (Document) this.stickers.get(a);
            if (!FileLoader.getPathToAttach(document.thumb, "webp", true).exists()) {
                this.stickersToLoad.add(FileLoader.getAttachFileName(document.thumb, "webp"));
                FileLoader.getInstance(this.currentAccount).loadFile(document.thumb.location, "webp", 0, 1);
            }
        }
        return this.stickersToLoad.isEmpty();
    }

    private boolean isValidSticker(Document document, String emoji) {
        int size2 = document.attributes.size();
        for (int b = 0; b < size2; b++) {
            DocumentAttribute attribute = (DocumentAttribute) document.attributes.get(b);
            if (attribute instanceof TL_documentAttributeSticker) {
                if (attribute.alt != null && attribute.alt.contains(emoji)) {
                    return true;
                }
                return false;
            }
        }
        return false;
    }

    private void addStickerToResult(Document document) {
        if (document != null) {
            String key = new StringBuilder();
            key.append(document.dc_id);
            key.append("_");
            key.append(document.id);
            key = key.toString();
            if (this.stickersMap == null || !this.stickersMap.containsKey(key)) {
                if (this.stickers == null) {
                    this.stickers = new ArrayList();
                    this.stickersMap = new HashMap();
                }
                this.stickers.add(document);
                this.stickersMap.put(key, document);
            }
        }
    }

    private void addStickersToResult(ArrayList<Document> documents) {
        if (documents != null) {
            if (!documents.isEmpty()) {
                int size = documents.size();
                for (int a = 0; a < size; a++) {
                    Document document = (Document) documents.get(a);
                    String key = new StringBuilder();
                    key.append(document.dc_id);
                    key.append("_");
                    key.append(document.id);
                    key = key.toString();
                    if (this.stickersMap == null || !this.stickersMap.containsKey(key)) {
                        if (this.stickers == null) {
                            this.stickers = new ArrayList();
                            this.stickersMap = new HashMap();
                        }
                        this.stickers.add(document);
                        this.stickersMap.put(key, document);
                    }
                }
            }
        }
    }

    private void searchServerStickers(final String emoji) {
        if (this.lastReqId != 0) {
            ConnectionsManager.getInstance(this.currentAccount).cancelRequest(this.lastReqId, true);
        }
        TL_messages_getStickers req = new TL_messages_getStickers();
        req.emoticon = emoji;
        req.hash = TtmlNode.ANONYMOUS_REGION_ID;
        req.exclude_featured = false;
        this.lastReqId = ConnectionsManager.getInstance(this.currentAccount).sendRequest(req, new RequestDelegate() {
            public void run(final TLObject response, TL_error error) {
                AndroidUtilities.runOnUIThread(new Runnable() {
                    public void run() {
                        boolean z = false;
                        StickersAdapter.this.lastReqId = 0;
                        if (emoji.equals(StickersAdapter.this.lastSticker)) {
                            if (response instanceof TL_messages_stickers) {
                                StickersAdapter.this.delayLocalResults = false;
                                TL_messages_stickers res = response;
                                int oldCount = StickersAdapter.this.stickers != null ? StickersAdapter.this.stickers.size() : 0;
                                StickersAdapter.this.addStickersToResult(res.stickers);
                                int newCount = StickersAdapter.this.stickers != null ? StickersAdapter.this.stickers.size() : 0;
                                if (!(StickersAdapter.this.visible || StickersAdapter.this.stickers == null || StickersAdapter.this.stickers.isEmpty())) {
                                    StickersAdapter.this.checkStickerFilesExistAndDownload();
                                    StickersAdapterDelegate access$800 = StickersAdapter.this.delegate;
                                    if (!(StickersAdapter.this.stickers == null || StickersAdapter.this.stickers.isEmpty() || !StickersAdapter.this.stickersToLoad.isEmpty())) {
                                        z = true;
                                    }
                                    access$800.needChangePanelVisibility(z);
                                    StickersAdapter.this.visible = true;
                                }
                                if (oldCount != newCount) {
                                    StickersAdapter.this.notifyDataSetChanged();
                                }
                            }
                        }
                    }
                });
            }
        });
    }

    public void clearStickers() {
        this.lastSticker = null;
        this.stickers = null;
        this.stickersMap = null;
        this.stickersToLoad.clear();
        notifyDataSetChanged();
        if (this.lastReqId != 0) {
            ConnectionsManager.getInstance(this.currentAccount).cancelRequest(this.lastReqId, true);
            this.lastReqId = 0;
        }
    }

    public int getItemCount() {
        return (this.delayLocalResults || this.stickers == null) ? 0 : this.stickers.size();
    }

    public Document getItem(int i) {
        return (this.stickers == null || i < 0 || i >= this.stickers.size()) ? null : (Document) this.stickers.get(i);
    }

    public boolean isEnabled(ViewHolder holder) {
        return true;
    }

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new Holder(new StickerCell(this.mContext));
    }

    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        int side = 0;
        if (i == 0) {
            if (this.stickers.size() == 1) {
                side = 2;
            } else {
                side = -1;
            }
        } else if (i == this.stickers.size() - 1) {
            side = 1;
        }
        ((StickerCell) viewHolder.itemView).setSticker((Document) this.stickers.get(i), side);
    }
}
