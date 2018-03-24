package org.telegram.ui.Adapters;

import android.content.Context;
import android.view.ViewGroup;
import java.util.ArrayList;
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

    public interface StickersAdapterDelegate {
        void needChangePanelVisibility(boolean z);
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
        boolean z = false;
        if ((id == NotificationCenter.FileDidLoaded || id == NotificationCenter.FileDidFailedLoad) && this.stickers != null && !this.stickers.isEmpty() && !this.stickersToLoad.isEmpty() && this.visible) {
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
                if (attribute.alt.contains(emoji)) {
                    return true;
                }
                return false;
            }
        }
        return false;
    }

    private void addStickerToResult(Document document) {
        if (document != null) {
            String key = document.dc_id + "_" + document.id;
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
        if (documents != null && !documents.isEmpty()) {
            int size = documents.size();
            for (int a = 0; a < size; a++) {
                Document document = (Document) documents.get(a);
                String key = document.dc_id + "_" + document.id;
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

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void loadStikersForEmoji(java.lang.CharSequence r17) {
        /*
        r16 = this;
        r12 = org.telegram.messenger.SharedConfig.suggestStickers;
        r13 = 2;
        if (r12 != r13) goto L_0x0006;
    L_0x0005:
        return;
    L_0x0006:
        if (r17 == 0) goto L_0x009c;
    L_0x0008:
        r12 = r17.length();
        if (r12 <= 0) goto L_0x009c;
    L_0x000e:
        r12 = r17.length();
        r13 = 14;
        if (r12 > r13) goto L_0x009c;
    L_0x0016:
        r10 = 1;
    L_0x0017:
        if (r10 == 0) goto L_0x0220;
    L_0x0019:
        r12 = 0;
        r0 = r16;
        r0.stickers = r12;
        r12 = 0;
        r0 = r16;
        r0.stickersMap = r12;
        r6 = r17.length();
        r1 = 0;
    L_0x0028:
        if (r1 >= r6) goto L_0x00cf;
    L_0x002a:
        r12 = r6 + -1;
        if (r1 >= r12) goto L_0x009f;
    L_0x002e:
        r0 = r17;
        r12 = r0.charAt(r1);
        r13 = 55356; // 0xd83c float:7.757E-41 double:2.73495E-319;
        if (r12 != r13) goto L_0x0053;
    L_0x0039:
        r12 = r1 + 1;
        r0 = r17;
        r12 = r0.charAt(r12);
        r13 = 57339; // 0xdffb float:8.0349E-41 double:2.8329E-319;
        if (r12 < r13) goto L_0x0053;
    L_0x0046:
        r12 = r1 + 1;
        r0 = r17;
        r12 = r0.charAt(r12);
        r13 = 57343; // 0xdfff float:8.0355E-41 double:2.8331E-319;
        if (r12 <= r13) goto L_0x0075;
    L_0x0053:
        r0 = r17;
        r12 = r0.charAt(r1);
        r13 = 8205; // 0x200d float:1.1498E-41 double:4.054E-320;
        if (r12 != r13) goto L_0x009f;
    L_0x005d:
        r12 = r1 + 1;
        r0 = r17;
        r12 = r0.charAt(r12);
        r13 = 9792; // 0x2640 float:1.3722E-41 double:4.838E-320;
        if (r12 == r13) goto L_0x0075;
    L_0x0069:
        r12 = r1 + 1;
        r0 = r17;
        r12 = r0.charAt(r12);
        r13 = 9794; // 0x2642 float:1.3724E-41 double:4.839E-320;
        if (r12 != r13) goto L_0x009f;
    L_0x0075:
        r12 = 2;
        r12 = new java.lang.CharSequence[r12];
        r13 = 0;
        r14 = 0;
        r0 = r17;
        r14 = r0.subSequence(r14, r1);
        r12[r13] = r14;
        r13 = 1;
        r14 = r1 + 2;
        r15 = r17.length();
        r0 = r17;
        r14 = r0.subSequence(r14, r15);
        r12[r13] = r14;
        r17 = android.text.TextUtils.concat(r12);
        r6 = r6 + -2;
        r1 = r1 + -1;
    L_0x0099:
        r1 = r1 + 1;
        goto L_0x0028;
    L_0x009c:
        r10 = 0;
        goto L_0x0017;
    L_0x009f:
        r0 = r17;
        r12 = r0.charAt(r1);
        r13 = 65039; // 0xfe0f float:9.1139E-41 double:3.21335E-319;
        if (r12 != r13) goto L_0x0099;
    L_0x00aa:
        r12 = 2;
        r12 = new java.lang.CharSequence[r12];
        r13 = 0;
        r14 = 0;
        r0 = r17;
        r14 = r0.subSequence(r14, r1);
        r12[r13] = r14;
        r13 = 1;
        r14 = r1 + 1;
        r15 = r17.length();
        r0 = r17;
        r14 = r0.subSequence(r14, r15);
        r12[r13] = r14;
        r17 = android.text.TextUtils.concat(r12);
        r6 = r6 + -1;
        r1 = r1 + -1;
        goto L_0x0099;
    L_0x00cf:
        r12 = r17.toString();
        r12 = r12.trim();
        r0 = r16;
        r0.lastSticker = r12;
        r0 = r16;
        r12 = r0.lastSticker;
        r12 = org.telegram.messenger.Emoji.isValidEmoji(r12);
        if (r12 != 0) goto L_0x00f7;
    L_0x00e5:
        r12 = 0;
        r0 = r16;
        r0.visible = r12;
        r0 = r16;
        r12 = r0.delegate;
        r13 = 0;
        r12.needChangePanelVisibility(r13);
        r16.notifyDataSetChanged();
        goto L_0x0005;
    L_0x00f7:
        r12 = 0;
        r0 = r16;
        r0.delayLocalResults = r12;
        r0 = r16;
        r12 = r0.currentAccount;
        r12 = org.telegram.messenger.DataQuery.getInstance(r12);
        r13 = 0;
        r8 = r12.getRecentStickersNoCopy(r13);
        r0 = r16;
        r12 = r0.currentAccount;
        r12 = org.telegram.messenger.DataQuery.getInstance(r12);
        r13 = 2;
        r5 = r12.getRecentStickersNoCopy(r13);
        r9 = 0;
        r1 = 0;
        r11 = r8.size();
    L_0x011c:
        if (r1 >= r11) goto L_0x013a;
    L_0x011e:
        r4 = r8.get(r1);
        r4 = (org.telegram.tgnet.TLRPC.Document) r4;
        r0 = r16;
        r12 = r0.lastSticker;
        r0 = r16;
        r12 = r0.isValidSticker(r4, r12);
        if (r12 == 0) goto L_0x015b;
    L_0x0130:
        r0 = r16;
        r0.addStickerToResult(r4);
        r9 = r9 + 1;
        r12 = 5;
        if (r9 < r12) goto L_0x015b;
    L_0x013a:
        r1 = 0;
        r11 = r5.size();
    L_0x013f:
        if (r1 >= r11) goto L_0x015e;
    L_0x0141:
        r4 = r5.get(r1);
        r4 = (org.telegram.tgnet.TLRPC.Document) r4;
        r0 = r16;
        r12 = r0.lastSticker;
        r0 = r16;
        r12 = r0.isValidSticker(r4, r12);
        if (r12 == 0) goto L_0x0158;
    L_0x0153:
        r0 = r16;
        r0.addStickerToResult(r4);
    L_0x0158:
        r1 = r1 + 1;
        goto L_0x013f;
    L_0x015b:
        r1 = r1 + 1;
        goto L_0x011c;
    L_0x015e:
        r0 = r16;
        r12 = r0.currentAccount;
        r12 = org.telegram.messenger.DataQuery.getInstance(r12);
        r2 = r12.getAllStickers();
        if (r2 == 0) goto L_0x01dc;
    L_0x016c:
        r0 = r16;
        r12 = r0.lastSticker;
        r12 = r2.get(r12);
        r12 = (java.util.ArrayList) r12;
        r7 = r12;
    L_0x0177:
        if (r7 == 0) goto L_0x0199;
    L_0x0179:
        r12 = r7.isEmpty();
        if (r12 != 0) goto L_0x0199;
    L_0x017f:
        r3 = new java.util.ArrayList;
        r3.<init>(r7);
        r12 = r8.isEmpty();
        if (r12 != 0) goto L_0x0194;
    L_0x018a:
        r12 = new org.telegram.ui.Adapters.StickersAdapter$1;
        r0 = r16;
        r12.<init>(r5, r8);
        java.util.Collections.sort(r3, r12);
    L_0x0194:
        r0 = r16;
        r0.addStickersToResult(r3);
    L_0x0199:
        r12 = org.telegram.messenger.SharedConfig.suggestStickers;
        if (r12 != 0) goto L_0x01a6;
    L_0x019d:
        r0 = r16;
        r12 = r0.lastSticker;
        r0 = r16;
        r0.searchServerStickers(r12);
    L_0x01a6:
        r0 = r16;
        r12 = r0.stickers;
        if (r12 == 0) goto L_0x020b;
    L_0x01ac:
        r0 = r16;
        r12 = r0.stickers;
        r12 = r12.isEmpty();
        if (r12 != 0) goto L_0x020b;
    L_0x01b6:
        r12 = org.telegram.messenger.SharedConfig.suggestStickers;
        if (r12 != 0) goto L_0x01de;
    L_0x01ba:
        r0 = r16;
        r12 = r0.stickers;
        r12 = r12.size();
        r13 = 5;
        if (r12 >= r13) goto L_0x01de;
    L_0x01c5:
        r12 = 1;
        r0 = r16;
        r0.delayLocalResults = r12;
        r0 = r16;
        r12 = r0.delegate;
        r13 = 0;
        r12.needChangePanelVisibility(r13);
        r12 = 0;
        r0 = r16;
        r0.visible = r12;
    L_0x01d7:
        r16.notifyDataSetChanged();
        goto L_0x0005;
    L_0x01dc:
        r7 = 0;
        goto L_0x0177;
    L_0x01de:
        r16.checkStickerFilesExistAndDownload();
        r0 = r16;
        r13 = r0.delegate;
        r0 = r16;
        r12 = r0.stickers;
        if (r12 == 0) goto L_0x0209;
    L_0x01eb:
        r0 = r16;
        r12 = r0.stickers;
        r12 = r12.isEmpty();
        if (r12 != 0) goto L_0x0209;
    L_0x01f5:
        r0 = r16;
        r12 = r0.stickersToLoad;
        r12 = r12.isEmpty();
        if (r12 == 0) goto L_0x0209;
    L_0x01ff:
        r12 = 1;
    L_0x0200:
        r13.needChangePanelVisibility(r12);
        r12 = 1;
        r0 = r16;
        r0.visible = r12;
        goto L_0x01d7;
    L_0x0209:
        r12 = 0;
        goto L_0x0200;
    L_0x020b:
        r0 = r16;
        r12 = r0.visible;
        if (r12 == 0) goto L_0x0005;
    L_0x0211:
        r0 = r16;
        r12 = r0.delegate;
        r13 = 0;
        r12.needChangePanelVisibility(r13);
        r12 = 0;
        r0 = r16;
        r0.visible = r12;
        goto L_0x0005;
    L_0x0220:
        r12 = "";
        r0 = r16;
        r0.lastSticker = r12;
        r0 = r16;
        r12 = r0.visible;
        if (r12 == 0) goto L_0x0005;
    L_0x022d:
        r0 = r16;
        r12 = r0.stickers;
        if (r12 == 0) goto L_0x0005;
    L_0x0233:
        r12 = 0;
        r0 = r16;
        r0.visible = r12;
        r0 = r16;
        r12 = r0.delegate;
        r13 = 0;
        r12.needChangePanelVisibility(r13);
        goto L_0x0005;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.ui.Adapters.StickersAdapter.loadStikersForEmoji(java.lang.CharSequence):void");
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
                        if (emoji.equals(StickersAdapter.this.lastSticker) && (response instanceof TL_messages_stickers)) {
                            int oldCount;
                            int newCount;
                            StickersAdapter.this.delayLocalResults = false;
                            TL_messages_stickers res = response;
                            if (StickersAdapter.this.stickers != null) {
                                oldCount = StickersAdapter.this.stickers.size();
                            } else {
                                oldCount = 0;
                            }
                            StickersAdapter.this.addStickersToResult(res.stickers);
                            if (StickersAdapter.this.stickers != null) {
                                newCount = StickersAdapter.this.stickers.size();
                            } else {
                                newCount = 0;
                            }
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
