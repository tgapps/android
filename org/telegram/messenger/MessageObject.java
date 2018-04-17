package org.telegram.messenger;

import android.text.Layout.Alignment;
import android.text.Spannable;
import android.text.Spannable.Factory;
import android.text.SpannableStringBuilder;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.URLSpan;
import android.text.util.Linkify;
import android.util.SparseArray;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.telegram.PhoneFormat.PhoneFormat;
import org.telegram.messenger.beta.R;
import org.telegram.messenger.exoplayer2.text.Cue;
import org.telegram.messenger.exoplayer2.upstream.cache.CacheDataSink;
import org.telegram.messenger.exoplayer2.util.MimeTypes;
import org.telegram.messenger.support.widget.helper.ItemTouchHelper.Callback;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.BotInlineResult;
import org.telegram.tgnet.TLRPC.Chat;
import org.telegram.tgnet.TLRPC.Document;
import org.telegram.tgnet.TLRPC.DocumentAttribute;
import org.telegram.tgnet.TLRPC.InputStickerSet;
import org.telegram.tgnet.TLRPC.KeyboardButton;
import org.telegram.tgnet.TLRPC.Message;
import org.telegram.tgnet.TLRPC.MessageEntity;
import org.telegram.tgnet.TLRPC.PageBlock;
import org.telegram.tgnet.TLRPC.Photo;
import org.telegram.tgnet.TLRPC.PhotoSize;
import org.telegram.tgnet.TLRPC.TL_channelAdminLogEvent;
import org.telegram.tgnet.TLRPC.TL_decryptedMessageActionScreenshotMessages;
import org.telegram.tgnet.TLRPC.TL_decryptedMessageActionSetMessageTTL;
import org.telegram.tgnet.TLRPC.TL_documentAttributeAnimated;
import org.telegram.tgnet.TLRPC.TL_documentAttributeAudio;
import org.telegram.tgnet.TLRPC.TL_documentAttributeImageSize;
import org.telegram.tgnet.TLRPC.TL_documentAttributeSticker;
import org.telegram.tgnet.TLRPC.TL_documentAttributeVideo;
import org.telegram.tgnet.TLRPC.TL_documentEmpty;
import org.telegram.tgnet.TLRPC.TL_game;
import org.telegram.tgnet.TLRPC.TL_inputMessageEntityMentionName;
import org.telegram.tgnet.TLRPC.TL_inputStickerSetEmpty;
import org.telegram.tgnet.TLRPC.TL_keyboardButtonBuy;
import org.telegram.tgnet.TLRPC.TL_keyboardButtonRow;
import org.telegram.tgnet.TLRPC.TL_message;
import org.telegram.tgnet.TLRPC.TL_messageActionChatEditPhoto;
import org.telegram.tgnet.TLRPC.TL_messageActionEmpty;
import org.telegram.tgnet.TLRPC.TL_messageActionHistoryClear;
import org.telegram.tgnet.TLRPC.TL_messageActionLoginUnknownLocation;
import org.telegram.tgnet.TLRPC.TL_messageActionPhoneCall;
import org.telegram.tgnet.TLRPC.TL_messageActionUserUpdatedPhoto;
import org.telegram.tgnet.TLRPC.TL_messageEmpty;
import org.telegram.tgnet.TLRPC.TL_messageEncryptedAction;
import org.telegram.tgnet.TLRPC.TL_messageEntityBold;
import org.telegram.tgnet.TLRPC.TL_messageEntityBotCommand;
import org.telegram.tgnet.TLRPC.TL_messageEntityCashtag;
import org.telegram.tgnet.TLRPC.TL_messageEntityCode;
import org.telegram.tgnet.TLRPC.TL_messageEntityEmail;
import org.telegram.tgnet.TLRPC.TL_messageEntityHashtag;
import org.telegram.tgnet.TLRPC.TL_messageEntityItalic;
import org.telegram.tgnet.TLRPC.TL_messageEntityMention;
import org.telegram.tgnet.TLRPC.TL_messageEntityMentionName;
import org.telegram.tgnet.TLRPC.TL_messageEntityPhone;
import org.telegram.tgnet.TLRPC.TL_messageEntityPre;
import org.telegram.tgnet.TLRPC.TL_messageEntityTextUrl;
import org.telegram.tgnet.TLRPC.TL_messageEntityUrl;
import org.telegram.tgnet.TLRPC.TL_messageForwarded_old2;
import org.telegram.tgnet.TLRPC.TL_messageMediaContact;
import org.telegram.tgnet.TLRPC.TL_messageMediaDocument;
import org.telegram.tgnet.TLRPC.TL_messageMediaDocument_layer68;
import org.telegram.tgnet.TLRPC.TL_messageMediaDocument_layer74;
import org.telegram.tgnet.TLRPC.TL_messageMediaDocument_old;
import org.telegram.tgnet.TLRPC.TL_messageMediaEmpty;
import org.telegram.tgnet.TLRPC.TL_messageMediaGame;
import org.telegram.tgnet.TLRPC.TL_messageMediaGeo;
import org.telegram.tgnet.TLRPC.TL_messageMediaGeoLive;
import org.telegram.tgnet.TLRPC.TL_messageMediaInvoice;
import org.telegram.tgnet.TLRPC.TL_messageMediaPhoto;
import org.telegram.tgnet.TLRPC.TL_messageMediaPhoto_layer68;
import org.telegram.tgnet.TLRPC.TL_messageMediaPhoto_layer74;
import org.telegram.tgnet.TLRPC.TL_messageMediaPhoto_old;
import org.telegram.tgnet.TLRPC.TL_messageMediaUnsupported;
import org.telegram.tgnet.TLRPC.TL_messageMediaVenue;
import org.telegram.tgnet.TLRPC.TL_messageMediaWebPage;
import org.telegram.tgnet.TLRPC.TL_messageService;
import org.telegram.tgnet.TLRPC.TL_message_secret;
import org.telegram.tgnet.TLRPC.TL_pageBlockCollage;
import org.telegram.tgnet.TLRPC.TL_pageBlockPhoto;
import org.telegram.tgnet.TLRPC.TL_pageBlockSlideshow;
import org.telegram.tgnet.TLRPC.TL_pageBlockVideo;
import org.telegram.tgnet.TLRPC.TL_photo;
import org.telegram.tgnet.TLRPC.TL_photoEmpty;
import org.telegram.tgnet.TLRPC.TL_photoSizeEmpty;
import org.telegram.tgnet.TLRPC.TL_replyInlineMarkup;
import org.telegram.tgnet.TLRPC.TL_webDocument;
import org.telegram.tgnet.TLRPC.TL_webPage;
import org.telegram.tgnet.TLRPC.User;
import org.telegram.tgnet.TLRPC.WebDocument;
import org.telegram.tgnet.TLRPC.WebPage;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.TypefaceSpan;
import org.telegram.ui.Components.URLSpanBotCommand;
import org.telegram.ui.Components.URLSpanBrowser;
import org.telegram.ui.Components.URLSpanMono;
import org.telegram.ui.Components.URLSpanNoUnderline;
import org.telegram.ui.Components.URLSpanNoUnderlineBold;
import org.telegram.ui.Components.URLSpanReplacement;
import org.telegram.ui.Components.URLSpanUserMention;

public class MessageObject {
    private static final int LINES_PER_BLOCK = 10;
    public static final int MESSAGE_SEND_STATE_SENDING = 1;
    public static final int MESSAGE_SEND_STATE_SEND_ERROR = 2;
    public static final int MESSAGE_SEND_STATE_SENT = 0;
    public static final int POSITION_FLAG_BOTTOM = 8;
    public static final int POSITION_FLAG_LEFT = 1;
    public static final int POSITION_FLAG_RIGHT = 2;
    public static final int POSITION_FLAG_TOP = 4;
    public static Pattern urlPattern;
    public boolean attachPathExists;
    public int audioPlayerDuration;
    public float audioProgress;
    public int audioProgressSec;
    public StringBuilder botButtonsLayout;
    public float bufferedProgress;
    public CharSequence caption;
    public int contentType;
    public int currentAccount;
    public TL_channelAdminLogEvent currentEvent;
    public String customReplyName;
    public String dateKey;
    public boolean deleted;
    public long eventId;
    public boolean forceUpdate;
    private int generatedWithMinSize;
    public float gifState;
    public boolean hasRtl;
    public boolean isDateObject;
    private int isRoundVideoCached;
    public int lastLineWidth;
    private boolean layoutCreated;
    public int linesCount;
    public CharSequence linkDescription;
    public boolean localChannel;
    public long localGroupId;
    public String localName;
    public int localType;
    public String localUserName;
    public boolean mediaExists;
    public Message messageOwner;
    public CharSequence messageText;
    public String monthKey;
    public ArrayList<PhotoSize> photoThumbs;
    public ArrayList<PhotoSize> photoThumbs2;
    public MessageObject replyMessageObject;
    public boolean resendAsIs;
    public int textHeight;
    public ArrayList<TextLayoutBlock> textLayoutBlocks;
    public int textWidth;
    public float textXOffset;
    public int type;
    public boolean useCustomPhoto;
    public VideoEditedInfo videoEditedInfo;
    public boolean viewsReloaded;
    public int wantedBotKeyboardWidth;

    public static class GroupedMessagePosition {
        public float aspectRatio;
        public boolean edge;
        public int flags;
        public boolean last;
        public int leftSpanOffset;
        public byte maxX;
        public byte maxY;
        public byte minX;
        public byte minY;
        public float ph;
        public int pw;
        public float[] siblingHeights;
        public int spanSize;

        public void set(int minX, int maxX, int minY, int maxY, int w, float h, int flags) {
            this.minX = (byte) minX;
            this.maxX = (byte) maxX;
            this.minY = (byte) minY;
            this.maxY = (byte) maxY;
            this.pw = w;
            this.spanSize = w;
            this.ph = h;
            this.flags = (byte) flags;
        }
    }

    public static class GroupedMessages {
        private int firstSpanAdditionalSize = Callback.DEFAULT_DRAG_ANIMATION_DURATION;
        public long groupId;
        public boolean hasSibling;
        private int maxSizeWidth = 800;
        public ArrayList<MessageObject> messages = new ArrayList();
        public ArrayList<GroupedMessagePosition> posArray = new ArrayList();
        public HashMap<MessageObject, GroupedMessagePosition> positions = new HashMap();

        private class MessageGroupedLayoutAttempt {
            public float[] heights;
            public int[] lineCounts;

            public MessageGroupedLayoutAttempt(int i1, int i2, float f1, float f2) {
                this.lineCounts = new int[]{i1, i2};
                this.heights = new float[]{f1, f2};
            }

            public MessageGroupedLayoutAttempt(int i1, int i2, int i3, float f1, float f2, float f3) {
                this.lineCounts = new int[]{i1, i2, i3};
                this.heights = new float[]{f1, f2, f3};
            }

            public MessageGroupedLayoutAttempt(int i1, int i2, int i3, int i4, float f1, float f2, float f3, float f4) {
                this.lineCounts = new int[]{i1, i2, i3, i4};
                this.heights = new float[]{f1, f2, f3, f4};
            }
        }

        private float multiHeight(float[] array, int start, int end) {
            float sum = 0.0f;
            for (int a = start; a < end; a++) {
                sum += array[a];
            }
            return ((float) this.maxSizeWidth) / sum;
        }

        public void calculate() {
            this.posArray.clear();
            this.positions.clear();
            int count = this.messages.size();
            if (count > 1) {
                MessageObject messageObject;
                boolean isOut;
                GroupedMessagePosition position;
                int i;
                float averageAspectRatio;
                float f;
                byte maxX;
                GroupedMessagePosition position2;
                int firstWidth;
                int i2;
                float thirdHeight;
                int leftWidth;
                int i3;
                int width;
                float min;
                float h;
                int w1;
                int i4;
                int i5;
                int i6;
                float f2;
                byte maxX2;
                float averageAspectRatio2;
                float maxAspectRatio;
                int i7;
                MessageGroupedLayoutAttempt messageGroupedLayoutAttempt;
                int minHeight;
                int i8;
                int i9;
                float f3;
                float f4;
                float f5;
                float maxSizeHeight = 814.0f;
                StringBuilder proportions = new StringBuilder();
                byte minWidth = (byte) 0;
                r10.hasSibling = false;
                boolean isOut2 = false;
                boolean forceCalc = false;
                boolean needShare = false;
                float averageAspectRatio3 = 1.0f;
                int a = 0;
                while (a < count) {
                    messageObject = (MessageObject) r10.messages.get(a);
                    if (a == 0) {
                        isOut = messageObject.isOutOwner();
                        boolean needShare2 = !isOut && (!(messageObject.messageOwner.fwd_from == null || messageObject.messageOwner.fwd_from.saved_from_peer == null) || (messageObject.messageOwner.from_id > 0 && (messageObject.messageOwner.to_id.channel_id != 0 || messageObject.messageOwner.to_id.chat_id != 0 || (messageObject.messageOwner.media instanceof TL_messageMediaGame) || (messageObject.messageOwner.media instanceof TL_messageMediaInvoice))));
                        isOut2 = isOut;
                        needShare = needShare2;
                    }
                    PhotoSize photoSize = FileLoader.getClosestPhotoSizeWithSize(messageObject.photoThumbs, AndroidUtilities.getPhotoSize());
                    position = new GroupedMessagePosition();
                    position.last = a == count + -1;
                    position.aspectRatio = photoSize == null ? 1.0f : ((float) photoSize.w) / ((float) photoSize.h);
                    if (position.aspectRatio > 1.2f) {
                        proportions.append("w");
                    } else if (position.aspectRatio < 0.8f) {
                        proportions.append("n");
                    } else {
                        proportions.append("q");
                    }
                    averageAspectRatio3 += position.aspectRatio;
                    if (position.aspectRatio > 2.0f) {
                        forceCalc = true;
                    }
                    r10.positions.put(messageObject, position);
                    r10.posArray.add(position);
                    a++;
                }
                if (needShare) {
                    r10.maxSizeWidth -= 50;
                    r10.firstSpanAdditionalSize += 50;
                }
                int minHeight2 = AndroidUtilities.dp(120.0f);
                int minWidth2 = (int) (((float) AndroidUtilities.dp(120.0f)) / (((float) Math.min(AndroidUtilities.displaySize.x, AndroidUtilities.displaySize.y)) / ((float) r10.maxSizeWidth)));
                int paddingsWidth = (int) (((float) AndroidUtilities.dp(40.0f)) / (((float) Math.min(AndroidUtilities.displaySize.x, AndroidUtilities.displaySize.y)) / ((float) r10.maxSizeWidth)));
                float maxAspectRatio2 = ((float) r10.maxSizeWidth) / 814.0f;
                float averageAspectRatio4 = averageAspectRatio3 / ((float) count);
                int i10 = 4;
                StringBuilder stringBuilder;
                if (forceCalc) {
                    i = 2;
                    averageAspectRatio = averageAspectRatio4;
                    f = maxAspectRatio2;
                    stringBuilder = proportions;
                    maxX = (byte) 0;
                } else {
                    if (!(count == 2 || count == 3)) {
                        if (count != 4) {
                            i = 2;
                            averageAspectRatio = averageAspectRatio4;
                            f = maxAspectRatio2;
                            maxX = (byte) 0;
                        }
                    }
                    GroupedMessagePosition position1;
                    GroupedMessagePosition position22;
                    float round;
                    if (count == 2) {
                        GroupedMessagePosition position12;
                        position1 = (GroupedMessagePosition) r10.posArray.get(0);
                        position2 = (GroupedMessagePosition) r10.posArray.get(1);
                        String pString = proportions.toString();
                        if (pString.equals("ww")) {
                            GroupedMessagePosition position23 = position2;
                            GroupedMessagePosition position13 = position1;
                            if (((double) averageAspectRatio4) > 1.4d * ((double) maxAspectRatio2)) {
                                position12 = position13;
                                position22 = position23;
                                averageAspectRatio = averageAspectRatio4;
                                if (((double) (position12.aspectRatio - position22.aspectRatio)) < 0.2d) {
                                    round = ((float) Math.round(Math.min(((float) r10.maxSizeWidth) / position12.aspectRatio, Math.min(((float) r10.maxSizeWidth) / position22.aspectRatio, 814.0f / 2.0f)))) / 1145798656;
                                    position12.set(0, 0, 0, 0, r10.maxSizeWidth, round, 7);
                                    position22.set(0, 0, 1, 1, r10.maxSizeWidth, round, 11);
                                    f = maxAspectRatio2;
                                }
                            } else {
                                averageAspectRatio = averageAspectRatio4;
                                position22 = position23;
                                position12 = position13;
                            }
                        } else {
                            position12 = position1;
                            position22 = position2;
                            averageAspectRatio = averageAspectRatio4;
                        }
                        if (pString.equals("ww")) {
                            f = maxAspectRatio2;
                        } else if (pString.equals("qq")) {
                            f = maxAspectRatio2;
                        } else {
                            f = maxAspectRatio2;
                            i10 = (int) Math.max(((float) r10.maxSizeWidth) * 0.4f, (float) Math.round((((float) r10.maxSizeWidth) / position12.aspectRatio) / ((1.0f / position12.aspectRatio) + (1.0f / position22.aspectRatio))));
                            firstWidth = r10.maxSizeWidth - i10;
                            if (firstWidth < minWidth2) {
                                averageAspectRatio4 = minWidth2 - firstWidth;
                                firstWidth = minWidth2;
                                i10 -= averageAspectRatio4;
                            }
                            round = Math.min(814.0f, (float) Math.round(Math.min(((float) firstWidth) / position12.aspectRatio, ((float) i10) / position22.aspectRatio))) / 814.0f;
                            position12.set(0, 0, 0, 0, firstWidth, round, 13);
                            position22.set(1, 1, 0, 0, i10, round, 14);
                            minWidth = (byte) 1;
                        }
                        i10 = r10.maxSizeWidth / 2;
                        i2 = i10;
                        round = ((float) Math.round(Math.min(((float) i10) / position12.aspectRatio, Math.min(((float) i10) / position22.aspectRatio, 814.0f)))) / 814.0f;
                        position12.set(0, 0, 0, 0, i2, round, 13);
                        position22.set(Cue.DIMEN_UNSET, Cue.DIMEN_UNSET, 0, 0, i2, round, 14);
                        minWidth = (byte) 1;
                    } else {
                        averageAspectRatio = averageAspectRatio4;
                        f = maxAspectRatio2;
                        GroupedMessagePosition position3;
                        float firstHeight;
                        if (count == 3) {
                            byte firstHeight2;
                            position1 = (GroupedMessagePosition) r10.posArray.get(0);
                            position2 = (GroupedMessagePosition) r10.posArray.get(1);
                            position3 = (GroupedMessagePosition) r10.posArray.get(2);
                            int maxX3;
                            if (proportions.charAt(0) == 'n') {
                                thirdHeight = Math.min(814.0f * 0.5f, (float) Math.round((position2.aspectRatio * ((float) r10.maxSizeWidth)) / (position3.aspectRatio + position2.aspectRatio)));
                                maxAspectRatio2 = 814.0f - thirdHeight;
                                maxX3 = 0;
                                a = (int) Math.max((float) minWidth2, Math.min(((float) r10.maxSizeWidth) * 0.5f, (float) Math.round(Math.min(position3.aspectRatio * thirdHeight, position2.aspectRatio * maxAspectRatio2))));
                                leftWidth = Math.round(Math.min((position1.aspectRatio * 814.0f) + ((float) paddingsWidth), (float) (r10.maxSizeWidth - a)));
                                position1.set(0, 0, 0, 1, leftWidth, 1.0f, 13);
                                i3 = a;
                                position2.set(1, 1, 0, 0, i3, maxAspectRatio2 / 814.0f, 6);
                                position3.set(0, 1, 1, 1, i3, thirdHeight / 814.0f, 10);
                                position3.spanSize = r10.maxSizeWidth;
                                position1.siblingHeights = new float[]{thirdHeight / 814.0f, maxAspectRatio2 / 814.0f};
                                if (isOut2) {
                                    position1.spanSize = r10.maxSizeWidth - a;
                                } else {
                                    position2.spanSize = r10.maxSizeWidth - leftWidth;
                                    position3.leftSpanOffset = leftWidth;
                                }
                                r10.hasSibling = true;
                                firstHeight2 = (byte) 1;
                            } else {
                                maxX3 = 0;
                                firstHeight = ((float) Math.round(Math.min(((float) r10.maxSizeWidth) / position1.aspectRatio, 0.66f * 814.0f))) / 814.0f;
                                position1.set(0, 1, 0, 0, r10.maxSizeWidth, firstHeight, 7);
                                width = r10.maxSizeWidth / 2;
                                i3 = width;
                                min = Math.min(814.0f - firstHeight, (float) Math.round(Math.min(((float) width) / position2.aspectRatio, ((float) width) / position3.aspectRatio))) / 814.0f;
                                position2.set(0, 0, 1, 1, i3, min, 9);
                                position3.set(1, 1, 1, 1, i3, min, 10);
                                firstHeight2 = (byte) 1;
                            }
                            minWidth = firstHeight2;
                        } else {
                            maxX = (byte) 0;
                            if (count == 4) {
                                position1 = (GroupedMessagePosition) r10.posArray.get(0);
                                position22 = (GroupedMessagePosition) r10.posArray.get(1);
                                position3 = (GroupedMessagePosition) r10.posArray.get(2);
                                GroupedMessagePosition maxAspectRatio3 = (GroupedMessagePosition) r10.posArray.get(3);
                                if (proportions.charAt(0) == 'w') {
                                    firstHeight = ((float) Math.round(Math.min(((float) r10.maxSizeWidth) / position1.aspectRatio, 0.66f * 814.0f))) / 814.0f;
                                    position1.set(0, 2, 0, 0, r10.maxSizeWidth, firstHeight, 7);
                                    h = (float) Math.round(((float) r10.maxSizeWidth) / ((position22.aspectRatio + position3.aspectRatio) + maxAspectRatio3.aspectRatio));
                                    leftWidth = (int) Math.max((float) minWidth2, Math.min(((float) r10.maxSizeWidth) * 0.4f, position22.aspectRatio * h));
                                    int w2 = (int) Math.max(Math.max((float) minWidth2, ((float) r10.maxSizeWidth) * 0.33f), maxAspectRatio3.aspectRatio * h);
                                    w1 = (r10.maxSizeWidth - leftWidth) - w2;
                                    stringBuilder = proportions;
                                    round = Math.min(1145798656 - firstHeight, h) / 814.0f;
                                    position22.set(0, 0, 1, 1, leftWidth, round, 9);
                                    position3.set(1, 1, 1, 1, w1, round, 8);
                                    maxAspectRatio3.set(2, 2, 1, 1, w2, round, 10);
                                    minWidth = (byte) 2;
                                } else {
                                    a = Math.max(minWidth2, Math.round(814.0f / (((1.0f / position22.aspectRatio) + (1.0f / position3.aspectRatio)) + (1.0f / ((GroupedMessagePosition) r10.posArray.get(3)).aspectRatio))));
                                    float h0 = Math.min(0.33f, Math.max((float) minHeight2, ((float) a) / position22.aspectRatio) / 814.0f);
                                    h = Math.min(0.33f, Math.max((float) minHeight2, ((float) a) / position3.aspectRatio) / 814.0f);
                                    float h2 = (1.0f - h0) - h;
                                    proportions = Math.round(Math.min((position1.aspectRatio * 1145798656) + ((float) paddingsWidth), (float) (r10.maxSizeWidth - a)));
                                    position1.set(0, 0, 0, 2, proportions, (h0 + h) + h2, 13);
                                    i2 = a;
                                    position22.set(1, 1, 0, 0, i2, h0, 6);
                                    position3.set(0, 1, 1, 1, i2, h, 2);
                                    position3.spanSize = r10.maxSizeWidth;
                                    maxAspectRatio3.set(0, 1, 2, 2, i2, h2, 10);
                                    maxAspectRatio3.spanSize = r10.maxSizeWidth;
                                    if (isOut2) {
                                        position1.spanSize = r10.maxSizeWidth - a;
                                    } else {
                                        position22.spanSize = r10.maxSizeWidth - proportions;
                                        position3.leftSpanOffset = proportions;
                                        maxAspectRatio3.leftSpanOffset = proportions;
                                    }
                                    r15 = new float[3];
                                    r15[1] = h;
                                    r15[2] = h2;
                                    position1.siblingHeights = r15;
                                    r10.hasSibling = true;
                                    minWidth = (byte) 1;
                                }
                                i4 = paddingsWidth;
                                i5 = minHeight2;
                                i6 = count;
                                f2 = 814.0f;
                                maxX2 = minWidth;
                                averageAspectRatio2 = averageAspectRatio;
                                maxAspectRatio = f;
                                i7 = 0;
                                w1 = minWidth2;
                                while (true) {
                                    i10 = i7;
                                    width = i6;
                                    if (i10 >= width) {
                                        position2 = (GroupedMessagePosition) r10.posArray.get(i10);
                                        if (isOut2) {
                                            if (position2.maxX == maxX2 || (position2.flags & 2) != 0) {
                                                position2.spanSize += r10.firstSpanAdditionalSize;
                                            }
                                            if ((position2.flags & 1) != 0) {
                                                position2.edge = true;
                                            }
                                        } else {
                                            if (position2.minX == (byte) 0) {
                                                position2.spanSize += r10.firstSpanAdditionalSize;
                                            }
                                            if ((position2.flags & 2) == 0) {
                                                position2.edge = true;
                                                isOut = true;
                                            }
                                        }
                                        messageObject = (MessageObject) r10.messages.get(i10);
                                        if (!isOut2 && messageObject.needDrawAvatar()) {
                                            if (position2.edge) {
                                                if ((position2.flags & 2) == 0) {
                                                    if (position2.spanSize == 1000) {
                                                        position2.spanSize -= 108;
                                                    } else if (position2.leftSpanOffset == 0) {
                                                        position2.leftSpanOffset += 108;
                                                    }
                                                }
                                                i7 = i10 + 1;
                                                i6 = width;
                                            } else {
                                                if (position2.spanSize != 1000) {
                                                    position2.spanSize += 108;
                                                }
                                                position2.pw += 108;
                                            }
                                        }
                                        i7 = i10 + 1;
                                        i6 = width;
                                    } else {
                                        return;
                                    }
                                }
                            }
                            i4 = paddingsWidth;
                            w1 = minWidth2;
                            i5 = minHeight2;
                            i6 = count;
                            f2 = 814.0f;
                            averageAspectRatio2 = averageAspectRatio;
                            maxAspectRatio = f;
                            maxX2 = maxX;
                            i7 = 0;
                            while (true) {
                                i10 = i7;
                                width = i6;
                                if (i10 >= width) {
                                    position2 = (GroupedMessagePosition) r10.posArray.get(i10);
                                    if (isOut2) {
                                        position2.spanSize += r10.firstSpanAdditionalSize;
                                        if ((position2.flags & 1) != 0) {
                                            position2.edge = true;
                                        }
                                    } else {
                                        if (position2.minX == (byte) 0) {
                                            position2.spanSize += r10.firstSpanAdditionalSize;
                                        }
                                        if ((position2.flags & 2) == 0) {
                                            position2.edge = true;
                                            isOut = true;
                                        }
                                    }
                                    messageObject = (MessageObject) r10.messages.get(i10);
                                    if (position2.edge) {
                                        if ((position2.flags & 2) == 0) {
                                            if (position2.spanSize == 1000) {
                                                position2.spanSize -= 108;
                                            } else if (position2.leftSpanOffset == 0) {
                                                position2.leftSpanOffset += 108;
                                            }
                                        }
                                        i7 = i10 + 1;
                                        i6 = width;
                                    } else {
                                        if (position2.spanSize != 1000) {
                                            position2.spanSize += 108;
                                        }
                                        position2.pw += 108;
                                        i7 = i10 + 1;
                                        i6 = width;
                                    }
                                } else {
                                    return;
                                }
                            }
                        }
                    }
                    i6 = count;
                    f2 = 814.0f;
                    maxX2 = minWidth;
                    averageAspectRatio2 = averageAspectRatio;
                    maxAspectRatio = f;
                    i7 = 0;
                    w1 = minWidth2;
                    while (true) {
                        i10 = i7;
                        width = i6;
                        if (i10 >= width) {
                            position2 = (GroupedMessagePosition) r10.posArray.get(i10);
                            if (isOut2) {
                                if (position2.minX == (byte) 0) {
                                    position2.spanSize += r10.firstSpanAdditionalSize;
                                }
                                if ((position2.flags & 2) == 0) {
                                    position2.edge = true;
                                    isOut = true;
                                }
                            } else {
                                position2.spanSize += r10.firstSpanAdditionalSize;
                                if ((position2.flags & 1) != 0) {
                                    position2.edge = true;
                                }
                            }
                            messageObject = (MessageObject) r10.messages.get(i10);
                            if (position2.edge) {
                                if (position2.spanSize != 1000) {
                                    position2.spanSize += 108;
                                }
                                position2.pw += 108;
                                i7 = i10 + 1;
                                i6 = width;
                            } else {
                                if ((position2.flags & 2) == 0) {
                                    if (position2.spanSize == 1000) {
                                        position2.spanSize -= 108;
                                    } else if (position2.leftSpanOffset == 0) {
                                        position2.leftSpanOffset += 108;
                                    }
                                }
                                i7 = i10 + 1;
                                i6 = width;
                            }
                        } else {
                            return;
                        }
                    }
                }
                float[] croppedRatios = new float[r10.posArray.size()];
                for (a = 0; a < count; a++) {
                    if (averageAspectRatio > 1.1f) {
                        croppedRatios[a] = Math.max(1.0f, ((GroupedMessagePosition) r10.posArray.get(a)).aspectRatio);
                    } else {
                        croppedRatios[a] = Math.min(1.0f, ((GroupedMessagePosition) r10.posArray.get(a)).aspectRatio);
                    }
                    croppedRatios[a] = Math.max(0.66667f, Math.min(1.7f, croppedRatios[a]));
                }
                ArrayList<MessageGroupedLayoutAttempt> attempts = new ArrayList();
                a = 1;
                while (true) {
                    leftWidth = a;
                    if (leftWidth >= croppedRatios.length) {
                        break;
                    }
                    w1 = croppedRatios.length - leftWidth;
                    if (leftWidth > 3) {
                        i = i10;
                        i6 = count;
                        averageAspectRatio2 = averageAspectRatio;
                        maxAspectRatio = f;
                    } else if (w1 > 3) {
                        i = i10;
                        i6 = count;
                        averageAspectRatio2 = averageAspectRatio;
                        maxAspectRatio = f;
                    } else {
                        width = i;
                        i = i10;
                        i6 = count;
                        count = width;
                        count = 3;
                        averageAspectRatio2 = averageAspectRatio;
                        MessageGroupedLayoutAttempt messageGroupedLayoutAttempt2 = messageGroupedLayoutAttempt;
                        maxAspectRatio = f;
                        messageGroupedLayoutAttempt = new MessageGroupedLayoutAttempt(leftWidth, w1, multiHeight(croppedRatios, 0, leftWidth), multiHeight(croppedRatios, leftWidth, croppedRatios.length));
                        attempts.add(messageGroupedLayoutAttempt2);
                    }
                    a = leftWidth + 1;
                    i10 = i;
                    averageAspectRatio = averageAspectRatio2;
                    f = maxAspectRatio;
                    count = i6;
                    i = 2;
                }
                i = i10;
                i6 = count;
                averageAspectRatio2 = averageAspectRatio;
                maxAspectRatio = f;
                for (leftWidth = 1; leftWidth < croppedRatios.length - 1; leftWidth++) {
                    a = 1;
                    while (true) {
                        count = a;
                        if (count >= croppedRatios.length - leftWidth) {
                            break;
                        }
                        int i11;
                        w1 = (croppedRatios.length - leftWidth) - count;
                        if (leftWidth <= 3) {
                            if (count <= (averageAspectRatio2 < 0.85f ? i : 3)) {
                                if (w1 > 3) {
                                    i4 = paddingsWidth;
                                    minHeight = minHeight2;
                                    i11 = w1;
                                    w1 = minWidth2;
                                } else {
                                    minHeight = minHeight2;
                                    MessageGroupedLayoutAttempt messageGroupedLayoutAttempt3 = messageGroupedLayoutAttempt;
                                    i4 = paddingsWidth;
                                    w1 = minWidth2;
                                    messageGroupedLayoutAttempt = new MessageGroupedLayoutAttempt(leftWidth, count, w1, multiHeight(croppedRatios, 0, leftWidth), multiHeight(croppedRatios, leftWidth, leftWidth + count), multiHeight(croppedRatios, leftWidth + count, croppedRatios.length));
                                    attempts.add(messageGroupedLayoutAttempt3);
                                }
                                a = count + 1;
                                minWidth2 = w1;
                                paddingsWidth = i4;
                                minHeight2 = minHeight;
                            }
                        }
                        i4 = paddingsWidth;
                        minHeight = minHeight2;
                        i11 = w1;
                        w1 = minWidth2;
                        a = count + 1;
                        minWidth2 = w1;
                        paddingsWidth = i4;
                        minHeight2 = minHeight;
                    }
                    w1 = minWidth2;
                    minHeight = minHeight2;
                }
                w1 = minWidth2;
                minHeight = minHeight2;
                for (count = 1; count < croppedRatios.length - 2; count++) {
                    a = 1;
                    while (true) {
                        leftWidth = a;
                        if (leftWidth >= croppedRatios.length - count) {
                            break;
                        }
                        int thirdLine;
                        a = 1;
                        while (true) {
                            minHeight2 = a;
                            if (minHeight2 >= (croppedRatios.length - count) - leftWidth) {
                                break;
                            }
                            minWidth2 = ((croppedRatios.length - count) - leftWidth) - minHeight2;
                            if (count > 3 || leftWidth > 3 || minHeight2 > 3) {
                                thirdLine = minHeight2;
                                i2 = leftWidth;
                                f2 = maxSizeHeight;
                                i5 = minHeight;
                            } else if (minWidth2 > 3) {
                                i3 = minWidth2;
                                thirdLine = minHeight2;
                                i2 = leftWidth;
                                f2 = maxSizeHeight;
                                i5 = minHeight;
                            } else {
                                float multiHeight = multiHeight(croppedRatios, 0, count);
                                min = multiHeight(croppedRatios, count, count + leftWidth);
                                float multiHeight2 = multiHeight(croppedRatios, count + leftWidth, (count + leftWidth) + minHeight2);
                                int multiHeight3 = multiHeight(croppedRatios, (count + leftWidth) + minHeight2, croppedRatios.length);
                                i8 = minHeight2;
                                i7 = 0;
                                i9 = minWidth2;
                                f2 = maxSizeHeight;
                                MessageGroupedLayoutAttempt messageGroupedLayoutAttempt4 = messageGroupedLayoutAttempt;
                                f3 = multiHeight;
                                f4 = min;
                                thirdLine = minHeight2;
                                i5 = minHeight;
                                f5 = multiHeight2;
                                i2 = leftWidth;
                                messageGroupedLayoutAttempt = new MessageGroupedLayoutAttempt(count, leftWidth, i8, i9, f3, f4, f5, multiHeight3);
                                attempts.add(messageGroupedLayoutAttempt4);
                            }
                            a = thirdLine + 1;
                            minHeight = i5;
                            leftWidth = i2;
                            maxSizeHeight = f2;
                        }
                        thirdLine = minHeight2;
                        f2 = maxSizeHeight;
                        i5 = minHeight;
                        a = leftWidth + 1;
                    }
                    i2 = leftWidth;
                    f2 = maxSizeHeight;
                    i5 = minHeight;
                }
                f2 = maxSizeHeight;
                i5 = minHeight;
                i7 = 0;
                thirdHeight = (float) ((r10.maxSizeWidth / 3) * 4);
                h = 0.0f;
                MessageGroupedLayoutAttempt optimal = null;
                for (a = 0; a < attempts.size(); a++) {
                    MessageGroupedLayoutAttempt attempt = (MessageGroupedLayoutAttempt) attempts.get(a);
                    f4 = Float.MAX_VALUE;
                    f3 = 0.0f;
                    for (i9 = 0; i9 < attempt.heights.length; i9++) {
                        f3 += attempt.heights[i9];
                        if (attempt.heights[i9] < f4) {
                            f4 = attempt.heights[i9];
                        }
                    }
                    maxAspectRatio2 = Math.abs(f3 - thirdHeight);
                    if (attempt.lineCounts.length > 1) {
                        if (attempt.lineCounts[0] <= attempt.lineCounts[1] && (attempt.lineCounts.length <= 2 || attempt.lineCounts[1] <= attempt.lineCounts[2])) {
                            if (attempt.lineCounts.length > 3 && attempt.lineCounts[2] > attempt.lineCounts[3]) {
                            }
                        }
                        maxAspectRatio2 *= 1.2f;
                    }
                    if (f4 < ((float) w1)) {
                        maxAspectRatio2 *= 1.5f;
                    }
                    if (optimal == null || maxAspectRatio2 < optimalDiff) {
                        optimal = attempt;
                        h = maxAspectRatio2;
                    }
                }
                if (optimal != null) {
                    maxAspectRatio2 = 0.0f;
                    maxX2 = maxX;
                    i8 = 0;
                    a = 0;
                    while (a < optimal.lineCounts.length) {
                        byte maxX4;
                        minWidth2 = optimal.lineCounts[a];
                        f5 = optimal.heights[a];
                        leftWidth = r10.maxSizeWidth;
                        GroupedMessagePosition posToFix = null;
                        float maxHeight = thirdHeight;
                        maxX2 = Math.max(maxX2, minWidth2 - 1);
                        width = 0;
                        while (width < minWidth2) {
                            float optimalDiff = h;
                            firstWidth = (int) (croppedRatios[i8] * f5);
                            leftWidth -= firstWidth;
                            maxX4 = maxX2;
                            position = (GroupedMessagePosition) r10.posArray.get(i8);
                            i = 0;
                            if (a == 0) {
                                i = 0 | 4;
                            }
                            int spanLeft = leftWidth;
                            if (a == optimal.lineCounts.length - 1) {
                                i |= 8;
                            }
                            if (width == 0) {
                                i |= 1;
                                if (isOut2) {
                                    posToFix = position;
                                }
                            }
                            if (width == minWidth2 - 1) {
                                leftWidth = i | 2;
                                if (!isOut2) {
                                    posToFix = position;
                                }
                            } else {
                                leftWidth = i;
                            }
                            position.set(width, width, a, a, firstWidth, f5 / f2, leftWidth);
                            i8++;
                            width++;
                            h = optimalDiff;
                            maxX2 = maxX4;
                            leftWidth = spanLeft;
                        }
                        maxX4 = maxX2;
                        posToFix.pw += leftWidth;
                        posToFix.spanSize += leftWidth;
                        maxAspectRatio2 += f5;
                        a++;
                        thirdHeight = maxHeight;
                    }
                    while (true) {
                        i10 = i7;
                        width = i6;
                        if (i10 >= width) {
                            position2 = (GroupedMessagePosition) r10.posArray.get(i10);
                            if (isOut2) {
                                position2.spanSize += r10.firstSpanAdditionalSize;
                                if ((position2.flags & 1) != 0) {
                                    position2.edge = true;
                                }
                            } else {
                                if (position2.minX == (byte) 0) {
                                    position2.spanSize += r10.firstSpanAdditionalSize;
                                }
                                if ((position2.flags & 2) == 0) {
                                    position2.edge = true;
                                    isOut = true;
                                }
                            }
                            messageObject = (MessageObject) r10.messages.get(i10);
                            if (position2.edge) {
                                if ((position2.flags & 2) == 0) {
                                    if (position2.spanSize == 1000) {
                                        position2.spanSize -= 108;
                                    } else if (position2.leftSpanOffset == 0) {
                                        position2.leftSpanOffset += 108;
                                    }
                                }
                                i7 = i10 + 1;
                                i6 = width;
                            } else {
                                if (position2.spanSize != 1000) {
                                    position2.spanSize += 108;
                                }
                                position2.pw += 108;
                                i7 = i10 + 1;
                                i6 = width;
                            }
                        } else {
                            return;
                        }
                    }
                }
            }
        }
    }

    public static class TextLayoutBlock {
        public int charactersEnd;
        public int charactersOffset;
        public byte directionFlags;
        public int height;
        public int heightByOffset;
        public StaticLayout textLayout;
        public float textYOffset;

        public boolean isRtl() {
            return (this.directionFlags & 1) != 0 && (this.directionFlags & 2) == 0;
        }
    }

    public MessageObject(int r1, org.telegram.tgnet.TLRPC.Message r2, java.util.AbstractMap<java.lang.Integer, org.telegram.tgnet.TLRPC.User> r3, java.util.AbstractMap<java.lang.Integer, org.telegram.tgnet.TLRPC.Chat> r4, android.util.SparseArray<org.telegram.tgnet.TLRPC.User> r5, android.util.SparseArray<org.telegram.tgnet.TLRPC.Chat> r6, boolean r7, long r8) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.MessageObject.<init>(int, org.telegram.tgnet.TLRPC$Message, java.util.AbstractMap, java.util.AbstractMap, android.util.SparseArray, android.util.SparseArray, boolean, long):void
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
        r6 = r21;
        r7 = r23;
        r5 = r24;
        r4 = r25;
        r3 = r26;
        r2 = r27;
        r1 = r28;
        r21.<init>();
        r0 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
        r6.type = r0;
        r0 = 0;
        r15 = 1;
        org.telegram.ui.ActionBar.Theme.createChatResources(r0, r15);
        r14 = r22;
        r6.currentAccount = r14;
        r6.messageOwner = r7;
        r12 = r29;
        r6.eventId = r12;
        r8 = r7.replyMessage;
        if (r8 == 0) goto L_0x0040;
    L_0x0028:
        r11 = new org.telegram.messenger.MessageObject;
        r10 = r7.replyMessage;
        r16 = 0;
        r8 = r11;
        r9 = r14;
        r0 = r11;
        r11 = r5;
        r12 = r4;
        r13 = r3;
        r14 = r2;
        r4 = r15;
        r15 = r16;
        r16 = r29;
        r8.<init>(r9, r10, r11, r12, r13, r14, r15, r16);
        r6.replyMessageObject = r0;
        goto L_0x0041;
    L_0x0040:
        r4 = r15;
    L_0x0041:
        r0 = 0;
        r8 = r7.from_id;
        if (r8 <= 0) goto L_0x0071;
    L_0x0046:
        if (r5 == 0) goto L_0x0056;
    L_0x0048:
        r8 = r7.from_id;
        r8 = java.lang.Integer.valueOf(r8);
        r8 = r5.get(r8);
        r0 = r8;
        r0 = (org.telegram.tgnet.TLRPC.User) r0;
        goto L_0x0061;
    L_0x0056:
        if (r3 == 0) goto L_0x0061;
    L_0x0058:
        r8 = r7.from_id;
        r8 = r3.get(r8);
        r0 = r8;
        r0 = (org.telegram.tgnet.TLRPC.User) r0;
    L_0x0061:
        if (r0 != 0) goto L_0x0071;
    L_0x0063:
        r8 = org.telegram.messenger.MessagesController.getInstance(r22);
        r9 = r7.from_id;
        r9 = java.lang.Integer.valueOf(r9);
        r0 = r8.getUser(r9);
    L_0x0071:
        r8 = r0;
        r0 = r7 instanceof org.telegram.tgnet.TLRPC.TL_messageService;
        r12 = 2;
        r13 = 0;
        if (r0 == 0) goto L_0x0872;
    L_0x0078:
        r0 = r7.action;
        if (r0 == 0) goto L_0x086b;
    L_0x007c:
        r0 = r7.action;
        r0 = r0 instanceof org.telegram.tgnet.TLRPC.TL_messageActionCustomAction;
        if (r0 == 0) goto L_0x008a;
    L_0x0082:
        r0 = r7.action;
        r0 = r0.message;
        r6.messageText = r0;
        goto L_0x086b;
    L_0x008a:
        r0 = r7.action;
        r0 = r0 instanceof org.telegram.tgnet.TLRPC.TL_messageActionChatCreate;
        if (r0 == 0) goto L_0x00b6;
    L_0x0090:
        r0 = r21.isOut();
        if (r0 == 0) goto L_0x00a3;
    L_0x0096:
        r0 = "ActionYouCreateGroup";
        r14 = 2131492914; // 0x7f0c0032 float:1.8609293E38 double:1.053097423E-314;
        r0 = org.telegram.messenger.LocaleController.getString(r0, r14);
        r6.messageText = r0;
        goto L_0x086b;
    L_0x00a3:
        r0 = "ActionCreateGroup";
        r14 = 2131492883; // 0x7f0c0013 float:1.860923E38 double:1.053097408E-314;
        r0 = org.telegram.messenger.LocaleController.getString(r0, r14);
        r14 = "un1";
        r0 = r6.replaceWithLink(r0, r14, r8);
        r6.messageText = r0;
        goto L_0x086b;
    L_0x00b6:
        r0 = r7.action;
        r0 = r0 instanceof org.telegram.tgnet.TLRPC.TL_messageActionChatDeleteUser;
        if (r0 == 0) goto L_0x0173;
    L_0x00bc:
        r0 = r7.action;
        r0 = r0.user_id;
        r14 = r7.from_id;
        if (r0 != r14) goto L_0x00ea;
    L_0x00c4:
        r0 = r21.isOut();
        if (r0 == 0) goto L_0x00d7;
    L_0x00ca:
        r0 = "ActionYouLeftUser";
        r14 = 2131492916; // 0x7f0c0034 float:1.8609297E38 double:1.053097424E-314;
        r0 = org.telegram.messenger.LocaleController.getString(r0, r14);
        r6.messageText = r0;
        goto L_0x086b;
    L_0x00d7:
        r0 = "ActionLeftUser";
        r14 = 2131492889; // 0x7f0c0019 float:1.8609243E38 double:1.053097411E-314;
        r0 = org.telegram.messenger.LocaleController.getString(r0, r14);
        r14 = "un1";
        r0 = r6.replaceWithLink(r0, r14, r8);
        r6.messageText = r0;
        goto L_0x086b;
    L_0x00ea:
        r0 = 0;
        if (r5 == 0) goto L_0x00fd;
    L_0x00ed:
        r14 = r7.action;
        r14 = r14.user_id;
        r14 = java.lang.Integer.valueOf(r14);
        r14 = r5.get(r14);
        r0 = r14;
        r0 = (org.telegram.tgnet.TLRPC.User) r0;
        goto L_0x010a;
    L_0x00fd:
        if (r3 == 0) goto L_0x010a;
    L_0x00ff:
        r14 = r7.action;
        r14 = r14.user_id;
        r14 = r3.get(r14);
        r0 = r14;
        r0 = (org.telegram.tgnet.TLRPC.User) r0;
    L_0x010a:
        if (r0 != 0) goto L_0x011c;
    L_0x010c:
        r14 = org.telegram.messenger.MessagesController.getInstance(r22);
        r15 = r7.action;
        r15 = r15.user_id;
        r15 = java.lang.Integer.valueOf(r15);
        r0 = r14.getUser(r15);
    L_0x011c:
        r14 = r21.isOut();
        if (r14 == 0) goto L_0x0134;
    L_0x0122:
        r14 = "ActionYouKickUser";
        r15 = 2131492915; // 0x7f0c0033 float:1.8609295E38 double:1.0530974237E-314;
        r14 = org.telegram.messenger.LocaleController.getString(r14, r15);
        r15 = "un2";
        r14 = r6.replaceWithLink(r14, r15, r0);
        r6.messageText = r14;
        goto L_0x0171;
    L_0x0134:
        r14 = r7.action;
        r14 = r14.user_id;
        r15 = r6.currentAccount;
        r15 = org.telegram.messenger.UserConfig.getInstance(r15);
        r15 = r15.getClientUserId();
        if (r14 != r15) goto L_0x0156;
    L_0x0144:
        r14 = "ActionKickUserYou";
        r15 = 2131492888; // 0x7f0c0018 float:1.860924E38 double:1.0530974103E-314;
        r14 = org.telegram.messenger.LocaleController.getString(r14, r15);
        r15 = "un1";
        r14 = r6.replaceWithLink(r14, r15, r8);
        r6.messageText = r14;
        goto L_0x0171;
    L_0x0156:
        r14 = "ActionKickUser";
        r15 = 2131492887; // 0x7f0c0017 float:1.8609239E38 double:1.05309741E-314;
        r14 = org.telegram.messenger.LocaleController.getString(r14, r15);
        r15 = "un2";
        r14 = r6.replaceWithLink(r14, r15, r0);
        r6.messageText = r14;
        r14 = r6.messageText;
        r15 = "un1";
        r14 = r6.replaceWithLink(r14, r15, r8);
        r6.messageText = r14;
    L_0x0171:
        goto L_0x086b;
    L_0x0173:
        r0 = r7.action;
        r0 = r0 instanceof org.telegram.tgnet.TLRPC.TL_messageActionChatAddUser;
        if (r0 == 0) goto L_0x0318;
    L_0x0179:
        r0 = r6.messageOwner;
        r0 = r0.action;
        r0 = r0.user_id;
        if (r0 != 0) goto L_0x019d;
    L_0x0181:
        r14 = r6.messageOwner;
        r14 = r14.action;
        r14 = r14.users;
        r14 = r14.size();
        if (r14 != r4) goto L_0x019d;
    L_0x018d:
        r14 = r6.messageOwner;
        r14 = r14.action;
        r14 = r14.users;
        r14 = r14.get(r13);
        r14 = (java.lang.Integer) r14;
        r0 = r14.intValue();
    L_0x019d:
        r14 = r0;
        r0 = 2131492871; // 0x7f0c0007 float:1.8609206E38 double:1.053097402E-314;
        r15 = 2131492911; // 0x7f0c002f float:1.8609287E38 double:1.0530974217E-314;
        if (r14 == 0) goto L_0x02ce;
    L_0x01a6:
        r16 = 0;
        if (r5 == 0) goto L_0x01b7;
    L_0x01aa:
        r4 = java.lang.Integer.valueOf(r14);
        r4 = r5.get(r4);
        r16 = r4;
        r16 = (org.telegram.tgnet.TLRPC.User) r16;
        goto L_0x01c1;
    L_0x01b7:
        if (r3 == 0) goto L_0x01c1;
    L_0x01b9:
        r4 = r3.get(r14);
        r16 = r4;
        r16 = (org.telegram.tgnet.TLRPC.User) r16;
    L_0x01c1:
        if (r16 != 0) goto L_0x01cf;
    L_0x01c3:
        r4 = org.telegram.messenger.MessagesController.getInstance(r22);
        r11 = java.lang.Integer.valueOf(r14);
        r16 = r4.getUser(r11);
    L_0x01cf:
        r4 = r16;
        r11 = r7.from_id;
        if (r14 != r11) goto L_0x024c;
    L_0x01d5:
        r0 = r7.to_id;
        r0 = r0.channel_id;
        if (r0 == 0) goto L_0x01ee;
    L_0x01db:
        r0 = r21.isMegagroup();
        if (r0 != 0) goto L_0x01ee;
    L_0x01e1:
        r0 = "ChannelJoined";
        r11 = 2131493172; // 0x7f0c0134 float:1.8609817E38 double:1.0530975506E-314;
        r0 = org.telegram.messenger.LocaleController.getString(r0, r11);
        r6.messageText = r0;
        goto L_0x02c7;
    L_0x01ee:
        r0 = r7.to_id;
        r0 = r0.channel_id;
        if (r0 == 0) goto L_0x0226;
        r0 = r21.isMegagroup();
        if (r0 == 0) goto L_0x0226;
        r0 = r6.currentAccount;
        r0 = org.telegram.messenger.UserConfig.getInstance(r0);
        r0 = r0.getClientUserId();
        if (r14 != r0) goto L_0x0213;
        r0 = "ChannelMegaJoined";
        r11 = 2131493176; // 0x7f0c0138 float:1.8609825E38 double:1.0530975526E-314;
        r0 = org.telegram.messenger.LocaleController.getString(r0, r11);
        r6.messageText = r0;
        goto L_0x02c7;
        r0 = "ActionAddUserSelfMega";
        r11 = 2131492873; // 0x7f0c0009 float:1.860921E38 double:1.053097403E-314;
        r0 = org.telegram.messenger.LocaleController.getString(r0, r11);
        r11 = "un1";
        r0 = r6.replaceWithLink(r0, r11, r8);
        r6.messageText = r0;
        goto L_0x02c7;
        r0 = r21.isOut();
        if (r0 == 0) goto L_0x0239;
        r0 = "ActionAddUserSelfYou";
        r11 = 2131492874; // 0x7f0c000a float:1.8609212E38 double:1.0530974034E-314;
        r0 = org.telegram.messenger.LocaleController.getString(r0, r11);
        r6.messageText = r0;
        goto L_0x02c7;
        r0 = "ActionAddUserSelf";
        r11 = 2131492872; // 0x7f0c0008 float:1.8609208E38 double:1.0530974024E-314;
        r0 = org.telegram.messenger.LocaleController.getString(r0, r11);
        r11 = "un1";
        r0 = r6.replaceWithLink(r0, r11, r8);
        r6.messageText = r0;
        goto L_0x02c7;
    L_0x024c:
        r11 = r21.isOut();
        if (r11 == 0) goto L_0x0261;
        r0 = "ActionYouAddUser";
        r0 = org.telegram.messenger.LocaleController.getString(r0, r15);
        r11 = "un2";
        r0 = r6.replaceWithLink(r0, r11, r4);
        r6.messageText = r0;
        goto L_0x02c7;
        r11 = r6.currentAccount;
        r11 = org.telegram.messenger.UserConfig.getInstance(r11);
        r11 = r11.getClientUserId();
        if (r14 != r11) goto L_0x02af;
        r0 = r7.to_id;
        r0 = r0.channel_id;
        if (r0 == 0) goto L_0x029d;
        r0 = r21.isMegagroup();
        if (r0 == 0) goto L_0x028b;
        r0 = "MegaAddedBy";
        r11 = 2131493798; // 0x7f0c03a6 float:1.8611086E38 double:1.05309786E-314;
        r0 = org.telegram.messenger.LocaleController.getString(r0, r11);
        r11 = "un1";
        r0 = r6.replaceWithLink(r0, r11, r8);
        r6.messageText = r0;
        goto L_0x02c7;
        r0 = "ChannelAddedBy";
        r11 = 2131493147; // 0x7f0c011b float:1.8609766E38 double:1.0530975383E-314;
        r0 = org.telegram.messenger.LocaleController.getString(r0, r11);
        r11 = "un1";
        r0 = r6.replaceWithLink(r0, r11, r8);
        r6.messageText = r0;
        goto L_0x02c7;
        r0 = "ActionAddUserYou";
        r11 = 2131492875; // 0x7f0c000b float:1.8609214E38 double:1.053097404E-314;
        r0 = org.telegram.messenger.LocaleController.getString(r0, r11);
        r11 = "un1";
        r0 = r6.replaceWithLink(r0, r11, r8);
        r6.messageText = r0;
        goto L_0x02c7;
        r11 = "ActionAddUser";
        r0 = org.telegram.messenger.LocaleController.getString(r11, r0);
        r11 = "un2";
        r0 = r6.replaceWithLink(r0, r11, r4);
        r6.messageText = r0;
        r0 = r6.messageText;
        r11 = "un1";
        r0 = r6.replaceWithLink(r0, r11, r8);
        r6.messageText = r0;
        r11 = r3;
        r10 = r5;
        r9 = 1;
        r15 = r25;
        goto L_0x0317;
    L_0x02ce:
        r4 = r21.isOut();
        if (r4 == 0) goto L_0x02f3;
        r0 = "ActionYouAddUser";
        r4 = org.telegram.messenger.LocaleController.getString(r0, r15);
        r11 = "un2";
        r0 = r7.action;
        r15 = r0.users;
        r0 = r6;
        r1 = r4;
        r4 = r2;
        r2 = r11;
        r11 = r3;
        r3 = r15;
        r9 = 1;
        r15 = r25;
        r4 = r5;
        r10 = r5;
        r5 = r11;
        r0 = r0.replaceWithLink(r1, r2, r3, r4, r5);
        r6.messageText = r0;
        goto L_0x0317;
        r11 = r3;
        r10 = r5;
        r9 = 1;
        r15 = r25;
        r1 = "ActionAddUser";
        r1 = org.telegram.messenger.LocaleController.getString(r1, r0);
        r2 = "un2";
        r0 = r7.action;
        r3 = r0.users;
        r0 = r6;
        r4 = r10;
        r5 = r11;
        r0 = r0.replaceWithLink(r1, r2, r3, r4, r5);
        r6.messageText = r0;
        r0 = r6.messageText;
        r1 = "un1";
        r0 = r6.replaceWithLink(r0, r1, r8);
        r6.messageText = r0;
        goto L_0x0334;
    L_0x0318:
        r11 = r3;
        r9 = r4;
        r10 = r5;
        r15 = r25;
        r0 = r7.action;
        r0 = r0 instanceof org.telegram.tgnet.TLRPC.TL_messageActionChatJoinedByLink;
        if (r0 == 0) goto L_0x034a;
        r0 = r21.isOut();
        if (r0 == 0) goto L_0x0338;
        r0 = "ActionInviteYou";
        r1 = 2131492886; // 0x7f0c0016 float:1.8609237E38 double:1.0530974093E-314;
        r0 = org.telegram.messenger.LocaleController.getString(r0, r1);
        r6.messageText = r0;
        r0 = r27;
        goto L_0x09d1;
        r0 = "ActionInviteUser";
        r1 = 2131492885; // 0x7f0c0015 float:1.8609235E38 double:1.053097409E-314;
        r0 = org.telegram.messenger.LocaleController.getString(r0, r1);
        r1 = "un1";
        r0 = r6.replaceWithLink(r0, r1, r8);
        r6.messageText = r0;
        goto L_0x0334;
        r0 = r7.action;
        r0 = r0 instanceof org.telegram.tgnet.TLRPC.TL_messageActionChatEditPhoto;
        if (r0 == 0) goto L_0x038c;
        r0 = r7.to_id;
        r0 = r0.channel_id;
        if (r0 == 0) goto L_0x0368;
        r0 = r21.isMegagroup();
        if (r0 != 0) goto L_0x0368;
        r0 = "ActionChannelChangedPhoto";
        r1 = 2131492879; // 0x7f0c000f float:1.8609222E38 double:1.053097406E-314;
        r0 = org.telegram.messenger.LocaleController.getString(r0, r1);
        r6.messageText = r0;
        goto L_0x0334;
        r0 = r21.isOut();
        if (r0 == 0) goto L_0x037a;
        r0 = "ActionYouChangedPhoto";
        r1 = 2131492912; // 0x7f0c0030 float:1.860929E38 double:1.053097422E-314;
        r0 = org.telegram.messenger.LocaleController.getString(r0, r1);
        r6.messageText = r0;
        goto L_0x0334;
        r0 = "ActionChangedPhoto";
        r1 = 2131492877; // 0x7f0c000d float:1.8609218E38 double:1.053097405E-314;
        r0 = org.telegram.messenger.LocaleController.getString(r0, r1);
        r1 = "un1";
        r0 = r6.replaceWithLink(r0, r1, r8);
        r6.messageText = r0;
        goto L_0x0334;
        r0 = r7.action;
        r0 = r0 instanceof org.telegram.tgnet.TLRPC.TL_messageActionChatEditTitle;
        if (r0 == 0) goto L_0x03ee;
        r0 = r7.to_id;
        r0 = r0.channel_id;
        if (r0 == 0) goto L_0x03b4;
        r0 = r21.isMegagroup();
        if (r0 != 0) goto L_0x03b4;
        r0 = "ActionChannelChangedTitle";
        r1 = 2131492880; // 0x7f0c0010 float:1.8609224E38 double:1.0530974064E-314;
        r0 = org.telegram.messenger.LocaleController.getString(r0, r1);
        r1 = "un2";
        r2 = r7.action;
        r2 = r2.title;
        r0 = r0.replace(r1, r2);
        r6.messageText = r0;
        goto L_0x0334;
        r0 = r21.isOut();
        if (r0 == 0) goto L_0x03d1;
        r0 = "ActionYouChangedTitle";
        r1 = 2131492913; // 0x7f0c0031 float:1.8609291E38 double:1.0530974227E-314;
        r0 = org.telegram.messenger.LocaleController.getString(r0, r1);
        r1 = "un2";
        r2 = r7.action;
        r2 = r2.title;
        r0 = r0.replace(r1, r2);
        r6.messageText = r0;
        goto L_0x0334;
        r0 = "ActionChangedTitle";
        r1 = 2131492878; // 0x7f0c000e float:1.860922E38 double:1.0530974054E-314;
        r0 = org.telegram.messenger.LocaleController.getString(r0, r1);
        r1 = "un2";
        r2 = r7.action;
        r2 = r2.title;
        r0 = r0.replace(r1, r2);
        r1 = "un1";
        r0 = r6.replaceWithLink(r0, r1, r8);
        r6.messageText = r0;
        goto L_0x0334;
        r0 = r7.action;
        r0 = r0 instanceof org.telegram.tgnet.TLRPC.TL_messageActionChatDeletePhoto;
        if (r0 == 0) goto L_0x0433;
        r0 = r7.to_id;
        r0 = r0.channel_id;
        if (r0 == 0) goto L_0x040d;
        r0 = r21.isMegagroup();
        if (r0 != 0) goto L_0x040d;
        r0 = "ActionChannelRemovedPhoto";
        r1 = 2131492881; // 0x7f0c0011 float:1.8609226E38 double:1.053097407E-314;
        r0 = org.telegram.messenger.LocaleController.getString(r0, r1);
        r6.messageText = r0;
        goto L_0x0334;
        r0 = r21.isOut();
        if (r0 == 0) goto L_0x0420;
        r0 = "ActionYouRemovedPhoto";
        r1 = 2131492917; // 0x7f0c0035 float:1.86093E38 double:1.0530974246E-314;
        r0 = org.telegram.messenger.LocaleController.getString(r0, r1);
        r6.messageText = r0;
        goto L_0x0334;
        r0 = "ActionRemovedPhoto";
        r1 = 2131492906; // 0x7f0c002a float:1.8609277E38 double:1.053097419E-314;
        r0 = org.telegram.messenger.LocaleController.getString(r0, r1);
        r1 = "un1";
        r0 = r6.replaceWithLink(r0, r1, r8);
        r6.messageText = r0;
        goto L_0x0334;
        r0 = r7.action;
        r0 = r0 instanceof org.telegram.tgnet.TLRPC.TL_messageActionTTLChange;
        r1 = 2131493822; // 0x7f0c03be float:1.8611135E38 double:1.053097872E-314;
        r2 = 2131493824; // 0x7f0c03c0 float:1.861114E38 double:1.053097873E-314;
        r3 = 2131493819; // 0x7f0c03bb float:1.8611129E38 double:1.0530978703E-314;
        r4 = 2131493820; // 0x7f0c03bc float:1.861113E38 double:1.053097871E-314;
        if (r0 == 0) goto L_0x04a5;
        r0 = r7.action;
        r0 = r0.ttl;
        if (r0 == 0) goto L_0x0483;
        r0 = r21.isOut();
        if (r0 == 0) goto L_0x0467;
        r0 = "MessageLifetimeChangedOutgoing";
        r1 = new java.lang.Object[r9];
        r2 = r7.action;
        r2 = r2.ttl;
        r2 = org.telegram.messenger.LocaleController.formatTTLString(r2);
        r1[r13] = r2;
        r0 = org.telegram.messenger.LocaleController.formatString(r0, r4, r1);
        r6.messageText = r0;
        goto L_0x0334;
        r0 = "MessageLifetimeChanged";
        r1 = new java.lang.Object[r12];
        r2 = org.telegram.messenger.UserObject.getFirstName(r8);
        r1[r13] = r2;
        r2 = r7.action;
        r2 = r2.ttl;
        r2 = org.telegram.messenger.LocaleController.formatTTLString(r2);
        r1[r9] = r2;
        r0 = org.telegram.messenger.LocaleController.formatString(r0, r3, r1);
        r6.messageText = r0;
        goto L_0x0334;
        r0 = r21.isOut();
        if (r0 == 0) goto L_0x0493;
        r0 = "MessageLifetimeYouRemoved";
        r0 = org.telegram.messenger.LocaleController.getString(r0, r2);
        r6.messageText = r0;
        goto L_0x0334;
        r0 = "MessageLifetimeRemoved";
        r2 = new java.lang.Object[r9];
        r3 = org.telegram.messenger.UserObject.getFirstName(r8);
        r2[r13] = r3;
        r0 = org.telegram.messenger.LocaleController.formatString(r0, r1, r2);
        r6.messageText = r0;
        goto L_0x0334;
        r0 = r7.action;
        r0 = r0 instanceof org.telegram.tgnet.TLRPC.TL_messageActionLoginUnknownLocation;
        if (r0 == 0) goto L_0x0566;
        r0 = r7.date;
        r0 = (long) r0;
        r2 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
        r0 = r0 * r2;
        r2 = org.telegram.messenger.LocaleController.getInstance();
        r2 = r2.formatterDay;
        if (r2 == 0) goto L_0x04e5;
        r2 = org.telegram.messenger.LocaleController.getInstance();
        r2 = r2.formatterYear;
        if (r2 == 0) goto L_0x04e5;
        r2 = "formatDateAtTime";
        r3 = 2131494696; // 0x7f0c0728 float:1.8612908E38 double:1.0530983036E-314;
        r4 = new java.lang.Object[r12];
        r5 = org.telegram.messenger.LocaleController.getInstance();
        r5 = r5.formatterYear;
        r5 = r5.format(r0);
        r4[r13] = r5;
        r5 = org.telegram.messenger.LocaleController.getInstance();
        r5 = r5.formatterDay;
        r5 = r5.format(r0);
        r4[r9] = r5;
        r2 = org.telegram.messenger.LocaleController.formatString(r2, r3, r4);
        goto L_0x04f8;
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r3 = "";
        r2.append(r3);
        r3 = r7.date;
        r2.append(r3);
        r2 = r2.toString();
        r3 = r6.currentAccount;
        r3 = org.telegram.messenger.UserConfig.getInstance(r3);
        r3 = r3.getCurrentUser();
        if (r3 != 0) goto L_0x053b;
        if (r10 == 0) goto L_0x0518;
        r4 = r6.messageOwner;
        r4 = r4.to_id;
        r4 = r4.user_id;
        r4 = java.lang.Integer.valueOf(r4);
        r4 = r10.get(r4);
        r3 = r4;
        r3 = (org.telegram.tgnet.TLRPC.User) r3;
        goto L_0x0527;
        if (r11 == 0) goto L_0x0527;
        r4 = r6.messageOwner;
        r4 = r4.to_id;
        r4 = r4.user_id;
        r4 = r11.get(r4);
        r3 = r4;
        r3 = (org.telegram.tgnet.TLRPC.User) r3;
        if (r3 != 0) goto L_0x053b;
        r4 = org.telegram.messenger.MessagesController.getInstance(r22);
        r5 = r6.messageOwner;
        r5 = r5.to_id;
        r5 = r5.user_id;
        r5 = java.lang.Integer.valueOf(r5);
        r3 = r4.getUser(r5);
        if (r3 == 0) goto L_0x0542;
        r4 = org.telegram.messenger.UserObject.getFirstName(r3);
        goto L_0x0544;
        r4 = "";
        r5 = "NotificationUnrecognizedDevice";
        r14 = 4;
        r14 = new java.lang.Object[r14];
        r14[r13] = r4;
        r14[r9] = r2;
        r13 = r7.action;
        r13 = r13.title;
        r14[r12] = r13;
        r13 = r7.action;
        r13 = r13.address;
        r16 = 3;
        r14[r16] = r13;
        r13 = 2131494003; // 0x7f0c0473 float:1.8611502E38 double:1.053097961E-314;
        r5 = org.telegram.messenger.LocaleController.formatString(r5, r13, r14);
        r6.messageText = r5;
        goto L_0x0334;
        r0 = r7.action;
        r0 = r0 instanceof org.telegram.tgnet.TLRPC.TL_messageActionUserJoined;
        if (r0 == 0) goto L_0x0582;
        r0 = "NotificationContactJoined";
        r1 = 2131493952; // 0x7f0c0440 float:1.8611399E38 double:1.053097936E-314;
        r2 = new java.lang.Object[r9];
        r3 = org.telegram.messenger.UserObject.getUserName(r8);
        r4 = 0;
        r2[r4] = r3;
        r0 = org.telegram.messenger.LocaleController.formatString(r0, r1, r2);
        r6.messageText = r0;
        goto L_0x0334;
        r0 = r7.action;
        r0 = r0 instanceof org.telegram.tgnet.TLRPC.TL_messageActionUserUpdatedPhoto;
        if (r0 == 0) goto L_0x059e;
        r0 = "NotificationContactNewPhoto";
        r1 = 2131493953; // 0x7f0c0441 float:1.86114E38 double:1.0530979365E-314;
        r2 = new java.lang.Object[r9];
        r3 = org.telegram.messenger.UserObject.getUserName(r8);
        r4 = 0;
        r2[r4] = r3;
        r0 = org.telegram.messenger.LocaleController.formatString(r0, r1, r2);
        r6.messageText = r0;
        goto L_0x0334;
        r0 = r7.action;
        r0 = r0 instanceof org.telegram.tgnet.TLRPC.TL_messageEncryptedAction;
        r5 = 2131492907; // 0x7f0c002b float:1.860928E38 double:1.0530974197E-314;
        r13 = 2131492908; // 0x7f0c002c float:1.8609281E38 double:1.05309742E-314;
        if (r0 == 0) goto L_0x063d;
        r0 = r7.action;
        r0 = r0.encryptedAction;
        r0 = r0 instanceof org.telegram.tgnet.TLRPC.TL_decryptedMessageActionScreenshotMessages;
        if (r0 == 0) goto L_0x05d5;
        r0 = r21.isOut();
        if (r0 == 0) goto L_0x05c5;
        r0 = "ActionTakeScreenshootYou";
        r1 = 0;
        r2 = new java.lang.Object[r1];
        r0 = org.telegram.messenger.LocaleController.formatString(r0, r13, r2);
        r6.messageText = r0;
        goto L_0x0334;
        r0 = "ActionTakeScreenshoot";
        r0 = org.telegram.messenger.LocaleController.getString(r0, r5);
        r1 = "un1";
        r0 = r6.replaceWithLink(r0, r1, r8);
        r6.messageText = r0;
        goto L_0x0334;
        r0 = r7.action;
        r0 = r0.encryptedAction;
        r0 = r0 instanceof org.telegram.tgnet.TLRPC.TL_decryptedMessageActionSetMessageTTL;
        if (r0 == 0) goto L_0x0334;
        r0 = r7.action;
        r0 = r0.encryptedAction;
        r0 = (org.telegram.tgnet.TLRPC.TL_decryptedMessageActionSetMessageTTL) r0;
        r5 = r0.ttl_seconds;
        if (r5 == 0) goto L_0x061b;
        r1 = r21.isOut();
        if (r1 == 0) goto L_0x0601;
        r1 = "MessageLifetimeChangedOutgoing";
        r2 = new java.lang.Object[r9];
        r3 = r0.ttl_seconds;
        r3 = org.telegram.messenger.LocaleController.formatTTLString(r3);
        r5 = 0;
        r2[r5] = r3;
        r1 = org.telegram.messenger.LocaleController.formatString(r1, r4, r2);
        r6.messageText = r1;
        goto L_0x063b;
        r5 = 0;
        r1 = "MessageLifetimeChanged";
        r2 = new java.lang.Object[r12];
        r4 = org.telegram.messenger.UserObject.getFirstName(r8);
        r2[r5] = r4;
        r4 = r0.ttl_seconds;
        r4 = org.telegram.messenger.LocaleController.formatTTLString(r4);
        r2[r9] = r4;
        r1 = org.telegram.messenger.LocaleController.formatString(r1, r3, r2);
        r6.messageText = r1;
        goto L_0x063b;
        r3 = r21.isOut();
        if (r3 == 0) goto L_0x062a;
        r1 = "MessageLifetimeYouRemoved";
        r1 = org.telegram.messenger.LocaleController.getString(r1, r2);
        r6.messageText = r1;
        goto L_0x063b;
        r2 = "MessageLifetimeRemoved";
        r3 = new java.lang.Object[r9];
        r4 = org.telegram.messenger.UserObject.getFirstName(r8);
        r5 = 0;
        r3[r5] = r4;
        r1 = org.telegram.messenger.LocaleController.formatString(r2, r1, r3);
        r6.messageText = r1;
        goto L_0x0334;
        r0 = r7.action;
        r0 = r0 instanceof org.telegram.tgnet.TLRPC.TL_messageActionScreenshotTaken;
        if (r0 == 0) goto L_0x0666;
        r0 = r21.isOut();
        if (r0 == 0) goto L_0x0656;
        r0 = "ActionTakeScreenshootYou";
        r1 = 0;
        r2 = new java.lang.Object[r1];
        r0 = org.telegram.messenger.LocaleController.formatString(r0, r13, r2);
        r6.messageText = r0;
        goto L_0x0334;
        r0 = "ActionTakeScreenshoot";
        r0 = org.telegram.messenger.LocaleController.getString(r0, r5);
        r1 = "un1";
        r0 = r6.replaceWithLink(r0, r1, r8);
        r6.messageText = r0;
        goto L_0x0334;
        r0 = r7.action;
        r0 = r0 instanceof org.telegram.tgnet.TLRPC.TL_messageActionCreatedBroadcastList;
        if (r0 == 0) goto L_0x067c;
        r0 = "YouCreatedBroadcastList";
        r1 = 2131494656; // 0x7f0c0700 float:1.8612827E38 double:1.053098284E-314;
        r2 = 0;
        r3 = new java.lang.Object[r2];
        r0 = org.telegram.messenger.LocaleController.formatString(r0, r1, r3);
        r6.messageText = r0;
        goto L_0x0334;
        r0 = r7.action;
        r0 = r0 instanceof org.telegram.tgnet.TLRPC.TL_messageActionChannelCreate;
        if (r0 == 0) goto L_0x06a2;
        r0 = r21.isMegagroup();
        if (r0 == 0) goto L_0x0695;
        r0 = "ActionCreateMega";
        r1 = 2131492884; // 0x7f0c0014 float:1.8609233E38 double:1.0530974083E-314;
        r0 = org.telegram.messenger.LocaleController.getString(r0, r1);
        r6.messageText = r0;
        goto L_0x0334;
        r0 = "ActionCreateChannel";
        r1 = 2131492882; // 0x7f0c0012 float:1.8609228E38 double:1.0530974074E-314;
        r0 = org.telegram.messenger.LocaleController.getString(r0, r1);
        r6.messageText = r0;
        goto L_0x0334;
        r0 = r7.action;
        r0 = r0 instanceof org.telegram.tgnet.TLRPC.TL_messageActionChatMigrateTo;
        r1 = 2131492890; // 0x7f0c001a float:1.8609245E38 double:1.0530974113E-314;
        if (r0 == 0) goto L_0x06b5;
        r0 = "ActionMigrateFromGroup";
        r0 = org.telegram.messenger.LocaleController.getString(r0, r1);
        r6.messageText = r0;
        goto L_0x0334;
        r0 = r7.action;
        r0 = r0 instanceof org.telegram.tgnet.TLRPC.TL_messageActionChannelMigrateFrom;
        if (r0 == 0) goto L_0x06c5;
        r0 = "ActionMigrateFromGroup";
        r0 = org.telegram.messenger.LocaleController.getString(r0, r1);
        r6.messageText = r0;
        goto L_0x0334;
        r0 = r7.action;
        r0 = r0 instanceof org.telegram.tgnet.TLRPC.TL_messageActionPinMessage;
        if (r0 == 0) goto L_0x06fa;
        if (r8 != 0) goto L_0x06f2;
        if (r15 == 0) goto L_0x06e1;
        r0 = r7.to_id;
        r0 = r0.channel_id;
        r0 = java.lang.Integer.valueOf(r0);
        r0 = r15.get(r0);
        r0 = (org.telegram.tgnet.TLRPC.Chat) r0;
        r1 = r0;
        r0 = r27;
        goto L_0x06f5;
        r0 = r27;
        if (r0 == 0) goto L_0x06f0;
        r1 = r7.to_id;
        r1 = r1.channel_id;
        r1 = r0.get(r1);
        r1 = (org.telegram.tgnet.TLRPC.Chat) r1;
        goto L_0x06f5;
        r1 = 0;
        goto L_0x06ef;
        r0 = r27;
        r1 = 0;
        r6.generatePinMessageText(r8, r1);
        goto L_0x09d1;
        r0 = r27;
        r1 = r7.action;
        r1 = r1 instanceof org.telegram.tgnet.TLRPC.TL_messageActionHistoryClear;
        if (r1 == 0) goto L_0x070f;
        r1 = "HistoryCleared";
        r2 = 2131493650; // 0x7f0c0312 float:1.8610786E38 double:1.053097787E-314;
        r1 = org.telegram.messenger.LocaleController.getString(r1, r2);
        r6.messageText = r1;
        goto L_0x09d1;
        r1 = r7.action;
        r1 = r1 instanceof org.telegram.tgnet.TLRPC.TL_messageActionGameScore;
        if (r1 == 0) goto L_0x071a;
        r6.generateGameMessageText(r8);
        goto L_0x09d1;
        r1 = r7.action;
        r1 = r1 instanceof org.telegram.tgnet.TLRPC.TL_messageActionPhoneCall;
        if (r1 == 0) goto L_0x07e4;
        r1 = r6.messageOwner;
        r1 = r1.action;
        r1 = (org.telegram.tgnet.TLRPC.TL_messageActionPhoneCall) r1;
        r2 = r1.reason;
        r2 = r2 instanceof org.telegram.tgnet.TLRPC.TL_phoneCallDiscardReasonMissed;
        r3 = r6.messageOwner;
        r3 = r3.from_id;
        r4 = r6.currentAccount;
        r4 = org.telegram.messenger.UserConfig.getInstance(r4);
        r4 = r4.getClientUserId();
        if (r3 != r4) goto L_0x0754;
        if (r2 == 0) goto L_0x0748;
        r3 = "CallMessageOutgoingMissed";
        r4 = 2131493113; // 0x7f0c00f9 float:1.8609697E38 double:1.0530975215E-314;
        r3 = org.telegram.messenger.LocaleController.getString(r3, r4);
        r6.messageText = r3;
        goto L_0x077f;
        r3 = "CallMessageOutgoing";
        r4 = 2131493112; // 0x7f0c00f8 float:1.8609695E38 double:1.053097521E-314;
        r3 = org.telegram.messenger.LocaleController.getString(r3, r4);
        r6.messageText = r3;
        goto L_0x077f;
        if (r2 == 0) goto L_0x0762;
        r3 = "CallMessageIncomingMissed";
        r4 = 2131493111; // 0x7f0c00f7 float:1.8609693E38 double:1.0530975205E-314;
        r3 = org.telegram.messenger.LocaleController.getString(r3, r4);
        r6.messageText = r3;
        goto L_0x077f;
        r3 = r1.reason;
        r3 = r3 instanceof org.telegram.tgnet.TLRPC.TL_phoneCallDiscardReasonBusy;
        if (r3 == 0) goto L_0x0774;
        r3 = "CallMessageIncomingDeclined";
        r4 = 2131493110; // 0x7f0c00f6 float:1.860969E38 double:1.05309752E-314;
        r3 = org.telegram.messenger.LocaleController.getString(r3, r4);
        r6.messageText = r3;
        goto L_0x077f;
        r3 = "CallMessageIncoming";
        r4 = 2131493109; // 0x7f0c00f5 float:1.8609689E38 double:1.0530975195E-314;
        r3 = org.telegram.messenger.LocaleController.getString(r3, r4);
        r6.messageText = r3;
        r3 = r1.duration;
        if (r3 <= 0) goto L_0x07e2;
        r3 = r1.duration;
        r3 = org.telegram.messenger.LocaleController.formatCallDuration(r3);
        r4 = "CallMessageWithDuration";
        r5 = 2131493115; // 0x7f0c00fb float:1.8609701E38 double:1.0530975225E-314;
        r13 = new java.lang.Object[r12];
        r14 = r6.messageText;
        r16 = 0;
        r13[r16] = r14;
        r13[r9] = r3;
        r4 = org.telegram.messenger.LocaleController.formatString(r4, r5, r13);
        r6.messageText = r4;
        r4 = r6.messageText;
        r4 = r4.toString();
        r5 = r4.indexOf(r3);
        r13 = -1;
        if (r5 == r13) goto L_0x07e2;
        r13 = new android.text.SpannableString;
        r14 = r6.messageText;
        r13.<init>(r14);
        r14 = r3.length();
        r14 = r14 + r5;
        if (r5 <= 0) goto L_0x07c5;
        r12 = r5 + -1;
        r12 = r4.charAt(r12);
        r9 = 40;
        if (r12 != r9) goto L_0x07c5;
        r5 = r5 + -1;
        r9 = r4.length();
        if (r14 >= r9) goto L_0x07d5;
        r9 = r4.charAt(r14);
        r12 = 41;
        if (r9 != r12) goto L_0x07d5;
        r14 = r14 + 1;
        r9 = new org.telegram.ui.Components.TypefaceSpan;
        r12 = android.graphics.Typeface.DEFAULT;
        r9.<init>(r12);
        r12 = 0;
        r13.setSpan(r9, r5, r14, r12);
        r6.messageText = r13;
        goto L_0x09d1;
        r1 = r7.action;
        r1 = r1 instanceof org.telegram.tgnet.TLRPC.TL_messageActionPaymentSent;
        if (r1 == 0) goto L_0x081b;
        r1 = 0;
        r2 = r21.getDialogId();
        r2 = (int) r2;
        if (r10 == 0) goto L_0x07fe;
        r3 = java.lang.Integer.valueOf(r2);
        r3 = r10.get(r3);
        r8 = r3;
        r8 = (org.telegram.tgnet.TLRPC.User) r8;
        goto L_0x0807;
        if (r11 == 0) goto L_0x0807;
        r3 = r11.get(r2);
        r8 = r3;
        r8 = (org.telegram.tgnet.TLRPC.User) r8;
        if (r8 != 0) goto L_0x0816;
        r3 = org.telegram.messenger.MessagesController.getInstance(r22);
        r4 = java.lang.Integer.valueOf(r2);
        r3 = r3.getUser(r4);
        r8 = r3;
        r6.generatePaymentSentMessageText(r1);
        goto L_0x09d1;
        r1 = r7.action;
        r1 = r1 instanceof org.telegram.tgnet.TLRPC.TL_messageActionBotAllowed;
        if (r1 == 0) goto L_0x09d1;
        r1 = r7.action;
        r1 = (org.telegram.tgnet.TLRPC.TL_messageActionBotAllowed) r1;
        r1 = r1.domain;
        r2 = "ActionBotAllowed";
        r3 = 2131492876; // 0x7f0c000c float:1.8609216E38 double:1.0530974044E-314;
        r2 = org.telegram.messenger.LocaleController.getString(r2, r3);
        r3 = "%1$s";
        r3 = r2.indexOf(r3);
        r4 = new android.text.SpannableString;
        r5 = 1;
        r9 = new java.lang.Object[r5];
        r5 = 0;
        r9[r5] = r1;
        r5 = java.lang.String.format(r2, r9);
        r4.<init>(r5);
        if (r3 < 0) goto L_0x0867;
        r5 = new org.telegram.ui.Components.URLSpanNoUnderlineBold;
        r9 = new java.lang.StringBuilder;
        r9.<init>();
        r12 = "http://";
        r9.append(r12);
        r9.append(r1);
        r9 = r9.toString();
        r5.<init>(r9);
        r9 = r1.length();
        r9 = r9 + r3;
        r12 = 33;
        r4.setSpan(r5, r3, r9, r12);
        r6.messageText = r4;
        goto L_0x09d1;
    L_0x086b:
        r0 = r2;
        r11 = r3;
        r10 = r5;
        r15 = r25;
        goto L_0x09d1;
    L_0x0872:
        r0 = r2;
        r11 = r3;
        r10 = r5;
        r15 = r25;
        r1 = r21.isMediaEmpty();
        if (r1 != 0) goto L_0x09cd;
        r1 = r7.media;
        r1 = r1 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaPhoto;
        if (r1 == 0) goto L_0x0890;
        r1 = "AttachPhoto";
        r2 = 2131493037; // 0x7f0c00ad float:1.8609543E38 double:1.053097484E-314;
        r1 = org.telegram.messenger.LocaleController.getString(r1, r2);
        r6.messageText = r1;
        goto L_0x09d1;
        r1 = r21.isVideo();
        if (r1 != 0) goto L_0x09c1;
        r1 = r7.media;
        r1 = r1 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaDocument;
        if (r1 == 0) goto L_0x08ac;
        r1 = r7.media;
        r1 = r1.document;
        r1 = r1 instanceof org.telegram.tgnet.TLRPC.TL_documentEmpty;
        if (r1 == 0) goto L_0x08ac;
        r1 = r7.media;
        r1 = r1.ttl_seconds;
        if (r1 == 0) goto L_0x08ac;
        goto L_0x09c1;
        r1 = r21.isVoice();
        if (r1 == 0) goto L_0x08bf;
        r1 = "AttachAudio";
        r2 = 2131493023; // 0x7f0c009f float:1.8609514E38 double:1.053097477E-314;
        r1 = org.telegram.messenger.LocaleController.getString(r1, r2);
        r6.messageText = r1;
        goto L_0x09d1;
        r1 = r21.isRoundVideo();
        if (r1 == 0) goto L_0x08d2;
        r1 = "AttachRound";
        r2 = 2131493039; // 0x7f0c00af float:1.8609547E38 double:1.053097485E-314;
        r1 = org.telegram.messenger.LocaleController.getString(r1, r2);
        r6.messageText = r1;
        goto L_0x09d1;
        r1 = r7.media;
        r1 = r1 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaGeo;
        if (r1 != 0) goto L_0x09b5;
        r1 = r7.media;
        r1 = r1 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaVenue;
        if (r1 == 0) goto L_0x08e0;
        goto L_0x09b5;
        r1 = r7.media;
        r1 = r1 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaGeoLive;
        if (r1 == 0) goto L_0x08f3;
        r1 = "AttachLiveLocation";
        r2 = 2131493031; // 0x7f0c00a7 float:1.860953E38 double:1.053097481E-314;
        r1 = org.telegram.messenger.LocaleController.getString(r1, r2);
        r6.messageText = r1;
        goto L_0x09d1;
        r1 = r7.media;
        r1 = r1 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaContact;
        if (r1 == 0) goto L_0x0906;
        r1 = "AttachContact";
        r2 = 2131493025; // 0x7f0c00a1 float:1.8609518E38 double:1.053097478E-314;
        r1 = org.telegram.messenger.LocaleController.getString(r1, r2);
        r6.messageText = r1;
        goto L_0x09d1;
        r1 = r7.media;
        r1 = r1 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaGame;
        if (r1 == 0) goto L_0x0912;
        r1 = r7.message;
        r6.messageText = r1;
        goto L_0x09d1;
        r1 = r7.media;
        r1 = r1 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaInvoice;
        if (r1 == 0) goto L_0x0920;
        r1 = r7.media;
        r1 = r1.description;
        r6.messageText = r1;
        goto L_0x09d1;
        r1 = r7.media;
        r1 = r1 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaUnsupported;
        if (r1 == 0) goto L_0x0933;
        r1 = "UnsupportedMedia";
        r2 = 2131494518; // 0x7f0c0676 float:1.8612547E38 double:1.0530982156E-314;
        r1 = org.telegram.messenger.LocaleController.getString(r1, r2);
        r6.messageText = r1;
        goto L_0x09d1;
        r1 = r7.media;
        r1 = r1 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaDocument;
        if (r1 == 0) goto L_0x09d1;
        r1 = r21.isSticker();
        if (r1 == 0) goto L_0x0972;
        r1 = r21.getStrickerChar();
        if (r1 == 0) goto L_0x0966;
        r2 = r1.length();
        if (r2 <= 0) goto L_0x0966;
        r2 = "%s %s";
        r3 = 2;
        r4 = new java.lang.Object[r3];
        r3 = 0;
        r4[r3] = r1;
        r3 = "AttachSticker";
        r5 = 2131493040; // 0x7f0c00b0 float:1.8609549E38 double:1.0530974854E-314;
        r3 = org.telegram.messenger.LocaleController.getString(r3, r5);
        r5 = 1;
        r4[r5] = r3;
        r2 = java.lang.String.format(r2, r4);
        r6.messageText = r2;
        goto L_0x0971;
        r2 = "AttachSticker";
        r3 = 2131493040; // 0x7f0c00b0 float:1.8609549E38 double:1.0530974854E-314;
        r2 = org.telegram.messenger.LocaleController.getString(r2, r3);
        r6.messageText = r2;
        goto L_0x09d1;
        r1 = r21.isMusic();
        if (r1 == 0) goto L_0x0984;
        r1 = "AttachMusic";
        r2 = 2131493036; // 0x7f0c00ac float:1.860954E38 double:1.0530974834E-314;
        r1 = org.telegram.messenger.LocaleController.getString(r1, r2);
        r6.messageText = r1;
        goto L_0x09d1;
        r1 = r21.isGif();
        if (r1 == 0) goto L_0x0996;
        r1 = "AttachGif";
        r2 = 2131493028; // 0x7f0c00a4 float:1.8609525E38 double:1.0530974795E-314;
        r1 = org.telegram.messenger.LocaleController.getString(r1, r2);
        r6.messageText = r1;
        goto L_0x09d1;
        r1 = r7.media;
        r1 = r1.document;
        r1 = org.telegram.messenger.FileLoader.getDocumentFileName(r1);
        if (r1 == 0) goto L_0x09a9;
        r2 = r1.length();
        if (r2 <= 0) goto L_0x09a9;
        r6.messageText = r1;
        goto L_0x09b4;
        r2 = "AttachDocument";
        r3 = 2131493026; // 0x7f0c00a2 float:1.860952E38 double:1.0530974785E-314;
        r2 = org.telegram.messenger.LocaleController.getString(r2, r3);
        r6.messageText = r2;
        goto L_0x09d1;
        r1 = "AttachLocation";
        r2 = 2131493033; // 0x7f0c00a9 float:1.8609535E38 double:1.053097482E-314;
        r1 = org.telegram.messenger.LocaleController.getString(r1, r2);
        r6.messageText = r1;
        goto L_0x09d1;
        r1 = "AttachVideo";
        r2 = 2131493043; // 0x7f0c00b3 float:1.8609555E38 double:1.053097487E-314;
        r1 = org.telegram.messenger.LocaleController.getString(r1, r2);
        r6.messageText = r1;
        goto L_0x09d1;
        r1 = r7.message;
        r6.messageText = r1;
        r1 = r6.messageText;
        if (r1 != 0) goto L_0x09d9;
        r1 = "";
        r6.messageText = r1;
        r21.setType();
        r21.measureInlineBotButtons();
        r1 = new java.util.GregorianCalendar;
        r1.<init>();
        r2 = r6.messageOwner;
        r2 = r2.date;
        r2 = (long) r2;
        r4 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
        r2 = r2 * r4;
        r1.setTimeInMillis(r2);
        r2 = 6;
        r2 = r1.get(r2);
        r3 = 1;
        r4 = r1.get(r3);
        r5 = 2;
        r9 = r1.get(r5);
        r12 = "%d_%02d_%02d";
        r13 = 3;
        r14 = new java.lang.Object[r13];
        r13 = java.lang.Integer.valueOf(r4);
        r16 = 0;
        r14[r16] = r13;
        r13 = java.lang.Integer.valueOf(r9);
        r14[r3] = r13;
        r13 = java.lang.Integer.valueOf(r2);
        r14[r5] = r13;
        r12 = java.lang.String.format(r12, r14);
        r6.dateKey = r12;
        r12 = "%d_%02d";
        r5 = new java.lang.Object[r5];
        r13 = java.lang.Integer.valueOf(r4);
        r5[r16] = r13;
        r13 = java.lang.Integer.valueOf(r9);
        r5[r3] = r13;
        r3 = java.lang.String.format(r12, r5);
        r6.monthKey = r3;
        r3 = r6.messageOwner;
        r3 = r3.message;
        if (r3 == 0) goto L_0x0a83;
        r3 = r6.messageOwner;
        r3 = r3.id;
        if (r3 >= 0) goto L_0x0a83;
        r3 = r6.messageOwner;
        r3 = r3.params;
        if (r3 == 0) goto L_0x0a83;
        r3 = r6.messageOwner;
        r3 = r3.params;
        r5 = "ve";
        r3 = r3.get(r5);
        r3 = (java.lang.String) r3;
        r5 = r3;
        if (r3 == 0) goto L_0x0a83;
        r3 = r21.isVideo();
        if (r3 != 0) goto L_0x0a66;
        r3 = r21.isNewGif();
        if (r3 != 0) goto L_0x0a66;
        r3 = r21.isRoundVideo();
        if (r3 == 0) goto L_0x0a83;
        r3 = new org.telegram.messenger.VideoEditedInfo;
        r3.<init>();
        r6.videoEditedInfo = r3;
        r3 = r6.videoEditedInfo;
        r3 = r3.parseString(r5);
        if (r3 != 0) goto L_0x0a79;
        r3 = 0;
        r6.videoEditedInfo = r3;
        goto L_0x0a84;
        r3 = 0;
        r12 = r6.videoEditedInfo;
        r13 = r21.isRoundVideo();
        r12.roundVideo = r13;
        goto L_0x0a84;
        r3 = 0;
        r21.generateCaption();
        r5 = r28;
        if (r5 == 0) goto L_0x0b18;
        r12 = r6.messageOwner;
        r12 = r12.media;
        r12 = r12 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaGame;
        if (r12 == 0) goto L_0x0a96;
        r12 = org.telegram.ui.ActionBar.Theme.chat_msgGameTextPaint;
        goto L_0x0a98;
        r12 = org.telegram.ui.ActionBar.Theme.chat_msgTextPaint;
        r13 = org.telegram.messenger.SharedConfig.allowBigEmoji;
        if (r13 == 0) goto L_0x0aa0;
        r3 = 1;
        r13 = new int[r3];
        goto L_0x0aa1;
        r13 = r3;
        r3 = r13;
        r13 = r6.messageText;
        r14 = r12.getFontMetricsInt();
        r0 = 1101004800; // 0x41a00000 float:20.0 double:5.439686476E-315;
        r0 = org.telegram.messenger.AndroidUtilities.dp(r0);
        r18 = r1;
        r1 = 0;
        r0 = org.telegram.messenger.Emoji.replaceEmoji(r13, r14, r0, r1, r3);
        r6.messageText = r0;
        if (r3 == 0) goto L_0x0b10;
        r0 = r3[r1];
        r13 = 1;
        if (r0 < r13) goto L_0x0b10;
        r0 = r3[r1];
        r13 = 3;
        if (r0 > r13) goto L_0x0b10;
        r0 = r3[r1];
        switch(r0) {
            case 1: goto L_0x0ada;
            case 2: goto L_0x0ad1;
            default: goto L_0x0ac8;
        };
        r0 = org.telegram.ui.ActionBar.Theme.chat_msgTextPaintThreeEmoji;
        r1 = 1103101952; // 0x41c00000 float:24.0 double:5.450047783E-315;
        r1 = org.telegram.messenger.AndroidUtilities.dp(r1);
        goto L_0x0ae3;
        r0 = org.telegram.ui.ActionBar.Theme.chat_msgTextPaintTwoEmoji;
        r1 = 1105199104; // 0x41e00000 float:28.0 double:5.46040909E-315;
        r1 = org.telegram.messenger.AndroidUtilities.dp(r1);
        goto L_0x0ae3;
        r0 = org.telegram.ui.ActionBar.Theme.chat_msgTextPaintOneEmoji;
        r1 = 1107296256; // 0x42000000 float:32.0 double:5.4707704E-315;
        r1 = org.telegram.messenger.AndroidUtilities.dp(r1);
        r13 = r6.messageText;
        r13 = (android.text.Spannable) r13;
        r14 = r6.messageText;
        r14 = r14.length();
        r19 = r2;
        r2 = org.telegram.messenger.Emoji.EmojiSpan.class;
        r20 = r3;
        r3 = 0;
        r2 = r13.getSpans(r3, r14, r2);
        r2 = (org.telegram.messenger.Emoji.EmojiSpan[]) r2;
        if (r2 == 0) goto L_0x0b14;
        r3 = r2.length;
        if (r3 <= 0) goto L_0x0b14;
        r3 = 0;
        r13 = r2.length;
        if (r3 >= r13) goto L_0x0b14;
        r13 = r2[r3];
        r14 = r0.getFontMetricsInt();
        r13.replaceFontMetrics(r14, r1);
        r3 = r3 + 1;
        goto L_0x0b01;
        r19 = r2;
        r20 = r3;
        r6.generateLayout(r8);
        goto L_0x0b1c;
        r18 = r1;
        r19 = r2;
        r6.layoutCreated = r5;
        r0 = 0;
        r6.generateThumbs(r0);
        r21.checkMediaExistance();
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.MessageObject.<init>(int, org.telegram.tgnet.TLRPC$Message, java.util.AbstractMap, java.util.AbstractMap, android.util.SparseArray, android.util.SparseArray, boolean, long):void");
    }

    public MessageObject(int r1, org.telegram.tgnet.TLRPC.TL_channelAdminLogEvent r2, java.util.ArrayList<org.telegram.messenger.MessageObject> r3, java.util.HashMap<java.lang.String, java.util.ArrayList<org.telegram.messenger.MessageObject>> r4, org.telegram.tgnet.TLRPC.Chat r5, int[] r6) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.MessageObject.<init>(int, org.telegram.tgnet.TLRPC$TL_channelAdminLogEvent, java.util.ArrayList, java.util.HashMap, org.telegram.tgnet.TLRPC$Chat, int[]):void
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
        r0 = r31;
        r1 = r33;
        r2 = r34;
        r3 = r36;
        r31.<init>();
        r4 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
        r0.type = r4;
        r4 = 0;
        r5 = r1.user_id;
        if (r5 <= 0) goto L_0x0024;
    L_0x0014:
        if (r4 != 0) goto L_0x0024;
    L_0x0016:
        r5 = org.telegram.messenger.MessagesController.getInstance(r32);
        r6 = r1.user_id;
        r6 = java.lang.Integer.valueOf(r6);
        r4 = r5.getUser(r6);
    L_0x0024:
        r0.currentEvent = r1;
        r5 = new java.util.GregorianCalendar;
        r5.<init>();
        r6 = r1.date;
        r6 = (long) r6;
        r8 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
        r6 = r6 * r8;
        r5.setTimeInMillis(r6);
        r6 = 6;
        r7 = r5.get(r6);
        r8 = 1;
        r9 = r5.get(r8);
        r10 = 2;
        r11 = r5.get(r10);
        r12 = "%d_%02d_%02d";
        r13 = 3;
        r14 = new java.lang.Object[r13];
        r15 = java.lang.Integer.valueOf(r9);
        r6 = 0;
        r14[r6] = r15;
        r15 = java.lang.Integer.valueOf(r11);
        r14[r8] = r15;
        r15 = java.lang.Integer.valueOf(r7);
        r14[r10] = r15;
        r12 = java.lang.String.format(r12, r14);
        r0.dateKey = r12;
        r12 = "%d_%02d";
        r14 = new java.lang.Object[r10];
        r15 = java.lang.Integer.valueOf(r9);
        r14[r6] = r15;
        r15 = java.lang.Integer.valueOf(r11);
        r14[r8] = r15;
        r12 = java.lang.String.format(r12, r14);
        r0.monthKey = r12;
        r12 = new org.telegram.tgnet.TLRPC$TL_peerChannel;
        r12.<init>();
        r14 = r3.id;
        r12.channel_id = r14;
        r14 = 0;
        r10 = r1.action;
        r10 = r10 instanceof org.telegram.tgnet.TLRPC.TL_channelAdminLogEventActionChangeTitle;
        if (r10 == 0) goto L_0x00cd;
    L_0x0087:
        r10 = r1.action;
        r10 = (org.telegram.tgnet.TLRPC.TL_channelAdminLogEventActionChangeTitle) r10;
        r10 = r10.new_value;
        r13 = r3.megagroup;
        if (r13 == 0) goto L_0x00ab;
    L_0x0091:
        r13 = "EventLogEditedGroupTitle";
        r16 = r5;
        r5 = 2131493473; // 0x7f0c0261 float:1.8610427E38 double:1.0530976993E-314;
        r17 = r7;
        r7 = new java.lang.Object[r8];
        r7[r6] = r10;
        r5 = org.telegram.messenger.LocaleController.formatString(r13, r5, r7);
        r7 = "un1";
        r5 = r0.replaceWithLink(r5, r7, r4);
        r0.messageText = r5;
        goto L_0x00c4;
    L_0x00ab:
        r16 = r5;
        r17 = r7;
        r5 = "EventLogEditedChannelTitle";
        r7 = 2131493470; // 0x7f0c025e float:1.8610421E38 double:1.053097698E-314;
        r13 = new java.lang.Object[r8];
        r13[r6] = r10;
        r5 = org.telegram.messenger.LocaleController.formatString(r5, r7, r13);
        r7 = "un1";
        r5 = r0.replaceWithLink(r5, r7, r4);
        r0.messageText = r5;
        r20 = r9;
        r21 = r11;
        r24 = r14;
        goto L_0x0ac4;
    L_0x00cd:
        r16 = r5;
        r17 = r7;
        r5 = r1.action;
        r5 = r5 instanceof org.telegram.tgnet.TLRPC.TL_channelAdminLogEventActionChangePhoto;
        if (r5 == 0) goto L_0x0199;
        r5 = new org.telegram.tgnet.TLRPC$TL_messageService;
        r5.<init>();
        r0.messageOwner = r5;
        r5 = r1.action;
        r5 = r5.new_photo;
        r5 = r5 instanceof org.telegram.tgnet.TLRPC.TL_chatPhotoEmpty;
        if (r5 == 0) goto L_0x0117;
        r5 = r0.messageOwner;
        r7 = new org.telegram.tgnet.TLRPC$TL_messageActionChatDeletePhoto;
        r7.<init>();
        r5.action = r7;
        r5 = r3.megagroup;
        if (r5 == 0) goto L_0x0105;
        r5 = "EventLogRemovedWGroupPhoto";
        r7 = 2131493515; // 0x7f0c028b float:1.8610512E38 double:1.05309772E-314;
        r5 = org.telegram.messenger.LocaleController.getString(r5, r7);
        r7 = "un1";
        r5 = r0.replaceWithLink(r5, r7, r4);
        r0.messageText = r5;
        goto L_0x00c5;
        r5 = "EventLogRemovedChannelPhoto";
        r7 = 2131493512; // 0x7f0c0288 float:1.8610506E38 double:1.0530977186E-314;
        r5 = org.telegram.messenger.LocaleController.getString(r5, r7);
        r7 = "un1";
        r5 = r0.replaceWithLink(r5, r7, r4);
        r0.messageText = r5;
        goto L_0x00c5;
        r5 = r0.messageOwner;
        r7 = new org.telegram.tgnet.TLRPC$TL_messageActionChatEditPhoto;
        r7.<init>();
        r5.action = r7;
        r5 = r0.messageOwner;
        r5 = r5.action;
        r7 = new org.telegram.tgnet.TLRPC$TL_photo;
        r7.<init>();
        r5.photo = r7;
        r5 = new org.telegram.tgnet.TLRPC$TL_photoSize;
        r5.<init>();
        r7 = r1.action;
        r7 = r7.new_photo;
        r7 = r7.photo_small;
        r5.location = r7;
        r7 = "s";
        r5.type = r7;
        r7 = 80;
        r5.h = r7;
        r5.w = r7;
        r7 = r0.messageOwner;
        r7 = r7.action;
        r7 = r7.photo;
        r7 = r7.sizes;
        r7.add(r5);
        r7 = new org.telegram.tgnet.TLRPC$TL_photoSize;
        r7.<init>();
        r5 = r7;
        r7 = r1.action;
        r7 = r7.new_photo;
        r7 = r7.photo_big;
        r5.location = r7;
        r7 = "m";
        r5.type = r7;
        r7 = 640; // 0x280 float:8.97E-43 double:3.16E-321;
        r5.h = r7;
        r5.w = r7;
        r7 = r0.messageOwner;
        r7 = r7.action;
        r7 = r7.photo;
        r7 = r7.sizes;
        r7.add(r5);
        r7 = r3.megagroup;
        if (r7 == 0) goto L_0x0186;
        r7 = "EventLogEditedGroupPhoto";
        r10 = 2131493472; // 0x7f0c0260 float:1.8610425E38 double:1.053097699E-314;
        r7 = org.telegram.messenger.LocaleController.getString(r7, r10);
        r10 = "un1";
        r7 = r0.replaceWithLink(r7, r10, r4);
        r0.messageText = r7;
        goto L_0x0197;
        r7 = "EventLogEditedChannelPhoto";
        r10 = 2131493469; // 0x7f0c025d float:1.861042E38 double:1.0530976974E-314;
        r7 = org.telegram.messenger.LocaleController.getString(r7, r10);
        r10 = "un1";
        r7 = r0.replaceWithLink(r7, r10, r4);
        r0.messageText = r7;
        goto L_0x00c5;
        r5 = r1.action;
        r5 = r5 instanceof org.telegram.tgnet.TLRPC.TL_channelAdminLogEventActionParticipantJoin;
        r7 = 2131493463; // 0x7f0c0257 float:1.8610407E38 double:1.0530976944E-314;
        r10 = 2131493489; // 0x7f0c0271 float:1.861046E38 double:1.0530977072E-314;
        if (r5 == 0) goto L_0x01c9;
        r5 = r3.megagroup;
        if (r5 == 0) goto L_0x01b9;
        r5 = "EventLogGroupJoined";
        r5 = org.telegram.messenger.LocaleController.getString(r5, r10);
        r7 = "un1";
        r5 = r0.replaceWithLink(r5, r7, r4);
        r0.messageText = r5;
        goto L_0x00c5;
        r5 = "EventLogChannelJoined";
        r5 = org.telegram.messenger.LocaleController.getString(r5, r7);
        r7 = "un1";
        r5 = r0.replaceWithLink(r5, r7, r4);
        r0.messageText = r5;
        goto L_0x00c5;
        r5 = r1.action;
        r5 = r5 instanceof org.telegram.tgnet.TLRPC.TL_channelAdminLogEventActionParticipantLeave;
        if (r5 == 0) goto L_0x0211;
        r5 = new org.telegram.tgnet.TLRPC$TL_messageService;
        r5.<init>();
        r0.messageOwner = r5;
        r5 = r0.messageOwner;
        r7 = new org.telegram.tgnet.TLRPC$TL_messageActionChatDeleteUser;
        r7.<init>();
        r5.action = r7;
        r5 = r0.messageOwner;
        r5 = r5.action;
        r7 = r1.user_id;
        r5.user_id = r7;
        r5 = r3.megagroup;
        if (r5 == 0) goto L_0x01fe;
        r5 = "EventLogLeftGroup";
        r7 = 2131493494; // 0x7f0c0276 float:1.861047E38 double:1.0530977097E-314;
        r5 = org.telegram.messenger.LocaleController.getString(r5, r7);
        r7 = "un1";
        r5 = r0.replaceWithLink(r5, r7, r4);
        r0.messageText = r5;
        goto L_0x00c5;
        r5 = "EventLogLeftChannel";
        r7 = 2131493493; // 0x7f0c0275 float:1.8610468E38 double:1.053097709E-314;
        r5 = org.telegram.messenger.LocaleController.getString(r5, r7);
        r7 = "un1";
        r5 = r0.replaceWithLink(r5, r7, r4);
        r0.messageText = r5;
        goto L_0x00c5;
        r5 = r1.action;
        r5 = r5 instanceof org.telegram.tgnet.TLRPC.TL_channelAdminLogEventActionParticipantInvite;
        if (r5 == 0) goto L_0x0284;
        r5 = new org.telegram.tgnet.TLRPC$TL_messageService;
        r5.<init>();
        r0.messageOwner = r5;
        r5 = r0.messageOwner;
        r13 = new org.telegram.tgnet.TLRPC$TL_messageActionChatAddUser;
        r13.<init>();
        r5.action = r13;
        r5 = org.telegram.messenger.MessagesController.getInstance(r32);
        r13 = r1.action;
        r13 = r13.participant;
        r13 = r13.user_id;
        r13 = java.lang.Integer.valueOf(r13);
        r5 = r5.getUser(r13);
        r13 = r1.action;
        r13 = r13.participant;
        r13 = r13.user_id;
        r6 = r0.messageOwner;
        r6 = r6.from_id;
        if (r13 != r6) goto L_0x0267;
        r6 = r3.megagroup;
        if (r6 == 0) goto L_0x0258;
        r6 = "EventLogGroupJoined";
        r6 = org.telegram.messenger.LocaleController.getString(r6, r10);
        r7 = "un1";
        r6 = r0.replaceWithLink(r6, r7, r4);
        r0.messageText = r6;
        goto L_0x0282;
        r6 = "EventLogChannelJoined";
        r6 = org.telegram.messenger.LocaleController.getString(r6, r7);
        r7 = "un1";
        r6 = r0.replaceWithLink(r6, r7, r4);
        r0.messageText = r6;
        goto L_0x0282;
        r6 = "EventLogAdded";
        r7 = 2131493457; // 0x7f0c0251 float:1.8610395E38 double:1.0530976914E-314;
        r6 = org.telegram.messenger.LocaleController.getString(r6, r7);
        r7 = "un2";
        r6 = r0.replaceWithLink(r6, r7, r5);
        r0.messageText = r6;
        r6 = r0.messageText;
        r7 = "un1";
        r6 = r0.replaceWithLink(r6, r7, r4);
        r0.messageText = r6;
        goto L_0x00c5;
        r5 = r1.action;
        r5 = r5 instanceof org.telegram.tgnet.TLRPC.TL_channelAdminLogEventActionParticipantToggleAdmin;
        if (r5 == 0) goto L_0x044f;
        r5 = new org.telegram.tgnet.TLRPC$TL_message;
        r5.<init>();
        r0.messageOwner = r5;
        r5 = org.telegram.messenger.MessagesController.getInstance(r32);
        r7 = r1.action;
        r7 = r7.prev_participant;
        r7 = r7.user_id;
        r7 = java.lang.Integer.valueOf(r7);
        r5 = r5.getUser(r7);
        r7 = "EventLogPromoted";
        r10 = 2131493501; // 0x7f0c027d float:1.8610484E38 double:1.053097713E-314;
        r7 = org.telegram.messenger.LocaleController.getString(r7, r10);
        r10 = "%1$s";
        r10 = r7.indexOf(r10);
        r6 = new java.lang.StringBuilder;
        r13 = new java.lang.Object[r8];
        r8 = r0.messageOwner;
        r8 = r8.entities;
        r8 = r0.getUserName(r5, r8, r10);
        r15 = 0;
        r13[r15] = r8;
        r8 = java.lang.String.format(r7, r13);
        r6.<init>(r8);
        r8 = "\n";
        r6.append(r8);
        r8 = r1.action;
        r8 = r8.prev_participant;
        r8 = r8.admin_rights;
        r13 = r1.action;
        r13 = r13.new_participant;
        r13 = r13.admin_rights;
        if (r8 != 0) goto L_0x02e4;
        r18 = r5;
        r5 = new org.telegram.tgnet.TLRPC$TL_channelAdminRights;
        r5.<init>();
        r8 = r5;
        goto L_0x02e6;
        r18 = r5;
        if (r13 != 0) goto L_0x02ee;
        r5 = new org.telegram.tgnet.TLRPC$TL_channelAdminRights;
        r5.<init>();
        r13 = r5;
        r5 = r8.change_info;
        r19 = r7;
        r7 = r13.change_info;
        if (r5 == r7) goto L_0x0323;
        r5 = 10;
        r6.append(r5);
        r5 = r13.change_info;
        if (r5 == 0) goto L_0x0302;
        r5 = 43;
        goto L_0x0304;
        r5 = 45;
        r6.append(r5);
        r5 = 32;
        r6.append(r5);
        r5 = r3.megagroup;
        if (r5 == 0) goto L_0x031a;
        r5 = "EventLogPromotedChangeGroupInfo";
        r7 = 2131493506; // 0x7f0c0282 float:1.8610494E38 double:1.0530977156E-314;
        r5 = org.telegram.messenger.LocaleController.getString(r5, r7);
        goto L_0x0320;
        r5 = "EventLogPromotedChangeChannelInfo";
        r7 = 2131493505; // 0x7f0c0281 float:1.8610492E38 double:1.053097715E-314;
        goto L_0x0315;
        r6.append(r5);
        r5 = r3.megagroup;
        if (r5 != 0) goto L_0x0377;
        r5 = r8.post_messages;
        r7 = r13.post_messages;
        if (r5 == r7) goto L_0x034f;
        r5 = 10;
        r6.append(r5);
        r5 = r13.post_messages;
        if (r5 == 0) goto L_0x0339;
        r5 = 43;
        goto L_0x033b;
        r5 = 45;
        r6.append(r5);
        r5 = 32;
        r6.append(r5);
        r5 = "EventLogPromotedPostMessages";
        r7 = 2131493510; // 0x7f0c0286 float:1.8610502E38 double:1.0530977176E-314;
        r5 = org.telegram.messenger.LocaleController.getString(r5, r7);
        r6.append(r5);
        r5 = r8.edit_messages;
        r7 = r13.edit_messages;
        if (r5 == r7) goto L_0x0377;
        r5 = 10;
        r6.append(r5);
        r5 = r13.edit_messages;
        if (r5 == 0) goto L_0x0361;
        r5 = 43;
        goto L_0x0363;
        r5 = 45;
        r6.append(r5);
        r5 = 32;
        r6.append(r5);
        r5 = "EventLogPromotedEditMessages";
        r7 = 2131493508; // 0x7f0c0284 float:1.8610498E38 double:1.0530977166E-314;
        r5 = org.telegram.messenger.LocaleController.getString(r5, r7);
        r6.append(r5);
        r5 = r8.delete_messages;
        r7 = r13.delete_messages;
        if (r5 == r7) goto L_0x039f;
        r5 = 10;
        r6.append(r5);
        r5 = r13.delete_messages;
        if (r5 == 0) goto L_0x0389;
        r5 = 43;
        goto L_0x038b;
        r5 = 45;
        r6.append(r5);
        r5 = 32;
        r6.append(r5);
        r5 = "EventLogPromotedDeleteMessages";
        r7 = 2131493507; // 0x7f0c0283 float:1.8610496E38 double:1.053097716E-314;
        r5 = org.telegram.messenger.LocaleController.getString(r5, r7);
        r6.append(r5);
        r5 = r8.add_admins;
        r7 = r13.add_admins;
        if (r5 == r7) goto L_0x03c7;
        r5 = 10;
        r6.append(r5);
        r5 = r13.add_admins;
        if (r5 == 0) goto L_0x03b1;
        r5 = 43;
        goto L_0x03b3;
        r5 = 45;
        r6.append(r5);
        r5 = 32;
        r6.append(r5);
        r5 = "EventLogPromotedAddAdmins";
        r7 = 2131493502; // 0x7f0c027e float:1.8610486E38 double:1.0530977137E-314;
        r5 = org.telegram.messenger.LocaleController.getString(r5, r7);
        r6.append(r5);
        r5 = r3.megagroup;
        if (r5 == 0) goto L_0x03f3;
        r5 = r8.ban_users;
        r7 = r13.ban_users;
        if (r5 == r7) goto L_0x03f3;
        r5 = 10;
        r6.append(r5);
        r5 = r13.ban_users;
        if (r5 == 0) goto L_0x03dd;
        r5 = 43;
        goto L_0x03df;
        r5 = 45;
        r6.append(r5);
        r5 = 32;
        r6.append(r5);
        r5 = "EventLogPromotedBanUsers";
        r7 = 2131493504; // 0x7f0c0280 float:1.861049E38 double:1.0530977147E-314;
        r5 = org.telegram.messenger.LocaleController.getString(r5, r7);
        r6.append(r5);
        r5 = r8.invite_users;
        r7 = r13.invite_users;
        if (r5 == r7) goto L_0x041b;
        r5 = 10;
        r6.append(r5);
        r5 = r13.invite_users;
        if (r5 == 0) goto L_0x0405;
        r5 = 43;
        goto L_0x0407;
        r5 = 45;
        r6.append(r5);
        r5 = 32;
        r6.append(r5);
        r5 = "EventLogPromotedAddUsers";
        r7 = 2131493503; // 0x7f0c027f float:1.8610488E38 double:1.053097714E-314;
        r5 = org.telegram.messenger.LocaleController.getString(r5, r7);
        r6.append(r5);
        r5 = r3.megagroup;
        if (r5 == 0) goto L_0x0447;
        r5 = r8.pin_messages;
        r7 = r13.pin_messages;
        if (r5 == r7) goto L_0x0447;
        r5 = 10;
        r6.append(r5);
        r5 = r13.pin_messages;
        if (r5 == 0) goto L_0x0431;
        r5 = 43;
        goto L_0x0433;
        r5 = 45;
        r6.append(r5);
        r5 = 32;
        r6.append(r5);
        r5 = "EventLogPromotedPinMessages";
        r7 = 2131493509; // 0x7f0c0285 float:1.86105E38 double:1.053097717E-314;
        r5 = org.telegram.messenger.LocaleController.getString(r5, r7);
        r6.append(r5);
        r5 = r6.toString();
        r0.messageText = r5;
        goto L_0x00c5;
        r5 = r1.action;
        r5 = r5 instanceof org.telegram.tgnet.TLRPC.TL_channelAdminLogEventActionParticipantToggleBan;
        if (r5 == 0) goto L_0x06d6;
        r5 = new org.telegram.tgnet.TLRPC$TL_message;
        r5.<init>();
        r0.messageOwner = r5;
        r5 = org.telegram.messenger.MessagesController.getInstance(r32);
        r6 = r1.action;
        r6 = r6.prev_participant;
        r6 = r6.user_id;
        r6 = java.lang.Integer.valueOf(r6);
        r5 = r5.getUser(r6);
        r6 = r1.action;
        r6 = r6.prev_participant;
        r6 = r6.banned_rights;
        r7 = r1.action;
        r7 = r7.new_participant;
        r7 = r7.banned_rights;
        r8 = r3.megagroup;
        if (r8 == 0) goto L_0x0699;
        if (r7 == 0) goto L_0x0497;
        r8 = r7.view_messages;
        if (r8 == 0) goto L_0x0497;
        if (r7 == 0) goto L_0x048f;
        if (r6 == 0) goto L_0x048f;
        r8 = r7.until_date;
        r10 = r6.until_date;
        if (r8 == r10) goto L_0x048f;
        goto L_0x0497;
        r20 = r9;
        r21 = r11;
        r24 = r14;
        goto L_0x069f;
        if (r7 == 0) goto L_0x0538;
        r8 = r7.until_date;
        r8 = org.telegram.messenger.AndroidUtilities.isBannedForever(r8);
        if (r8 != 0) goto L_0x0538;
        r8 = new java.lang.StringBuilder;
        r8.<init>();
        r10 = r7.until_date;
        r13 = r1.date;
        r10 = r10 - r13;
        r13 = r10 / 60;
        r13 = r13 / 60;
        r13 = r13 / 24;
        r15 = r13 * 60;
        r15 = r15 * 60;
        r15 = r15 * 24;
        r10 = r10 - r15;
        r15 = r10 / 60;
        r20 = r9;
        r9 = r15 / 60;
        r15 = r9 * 60;
        r15 = r15 * 60;
        r10 = r10 - r15;
        r21 = r11;
        r11 = r10 / 60;
        r15 = 0;
        r18 = r15;
        r15 = 0;
        r22 = r15;
        r23 = r10;
        r24 = r14;
        r10 = r22;
        r14 = 3;
        if (r10 >= r14) goto L_0x0537;
        r14 = 0;
        if (r10 != 0) goto L_0x04ed;
        if (r13 == 0) goto L_0x04ea;
        r25 = r14;
        r14 = "Days";
        r14 = org.telegram.messenger.LocaleController.formatPluralString(r14, r13);
        r18 = r18 + 1;
        r26 = r9;
        r9 = r18;
        goto L_0x050e;
        r25 = r14;
        goto L_0x0508;
        r25 = r14;
        r14 = 1;
        if (r10 != r14) goto L_0x04fd;
        if (r9 == 0) goto L_0x0508;
        r14 = "Hours";
        r14 = org.telegram.messenger.LocaleController.formatPluralString(r14, r9);
        r18 = r18 + 1;
        goto L_0x04e5;
        if (r11 == 0) goto L_0x0508;
        r14 = "Minutes";
        r14 = org.telegram.messenger.LocaleController.formatPluralString(r14, r11);
        r18 = r18 + 1;
        goto L_0x04e5;
        r26 = r9;
        r9 = r18;
        r14 = r25;
        if (r14 == 0) goto L_0x0524;
        r15 = r8.length();
        if (r15 <= 0) goto L_0x051e;
        r27 = r11;
        r11 = ", ";
        r8.append(r11);
        goto L_0x0520;
        r27 = r11;
        r8.append(r14);
        goto L_0x0526;
        r27 = r11;
        r11 = 2;
        if (r9 != r11) goto L_0x052a;
        goto L_0x0537;
        r15 = r10 + 1;
        r18 = r9;
        r10 = r23;
        r14 = r24;
        r9 = r26;
        r11 = r27;
        goto L_0x04cb;
        goto L_0x054c;
        r20 = r9;
        r21 = r11;
        r24 = r14;
        r8 = new java.lang.StringBuilder;
        r9 = "UserRestrictionsUntilForever";
        r10 = 2131494555; // 0x7f0c069b float:1.8612622E38 double:1.053098234E-314;
        r9 = org.telegram.messenger.LocaleController.getString(r9, r10);
        r8.<init>(r9);
        r9 = "EventLogRestrictedUntil";
        r10 = 2131493521; // 0x7f0c0291 float:1.8610524E38 double:1.053097723E-314;
        r9 = org.telegram.messenger.LocaleController.getString(r9, r10);
        r10 = "%1$s";
        r10 = r9.indexOf(r10);
        r11 = new java.lang.StringBuilder;
        r13 = 2;
        r13 = new java.lang.Object[r13];
        r14 = r0.messageOwner;
        r14 = r14.entities;
        r14 = r0.getUserName(r5, r14, r10);
        r15 = 0;
        r13[r15] = r14;
        r14 = r8.toString();
        r15 = 1;
        r13[r15] = r14;
        r13 = java.lang.String.format(r9, r13);
        r11.<init>(r13);
        r13 = 0;
        if (r6 != 0) goto L_0x0582;
        r14 = new org.telegram.tgnet.TLRPC$TL_channelBannedRights;
        r14.<init>();
        r6 = r14;
        if (r7 != 0) goto L_0x058a;
        r14 = new org.telegram.tgnet.TLRPC$TL_channelBannedRights;
        r14.<init>();
        r7 = r14;
        r14 = r6.view_messages;
        r28 = r8;
        r8 = r7.view_messages;
        if (r14 == r8) goto L_0x05bd;
        if (r13 != 0) goto L_0x059b;
        r8 = 10;
        r11.append(r8);
        r13 = 1;
        goto L_0x059d;
        r8 = 10;
        r11.append(r8);
        r8 = r7.view_messages;
        if (r8 != 0) goto L_0x05a7;
        r8 = 43;
        goto L_0x05a9;
        r8 = 45;
        r11.append(r8);
        r8 = 32;
        r11.append(r8);
        r8 = "EventLogRestrictedReadMessages";
        r14 = 2131493516; // 0x7f0c028c float:1.8610514E38 double:1.0530977206E-314;
        r8 = org.telegram.messenger.LocaleController.getString(r8, r14);
        r11.append(r8);
        r8 = r6.send_messages;
        r14 = r7.send_messages;
        if (r8 == r14) goto L_0x05ee;
        if (r13 != 0) goto L_0x05cc;
        r8 = 10;
        r11.append(r8);
        r13 = 1;
        goto L_0x05ce;
        r8 = 10;
        r11.append(r8);
        r8 = r7.send_messages;
        if (r8 != 0) goto L_0x05d8;
        r8 = 43;
        goto L_0x05da;
        r8 = 45;
        r11.append(r8);
        r8 = 32;
        r11.append(r8);
        r8 = "EventLogRestrictedSendMessages";
        r14 = 2131493519; // 0x7f0c028f float:1.861052E38 double:1.053097722E-314;
        r8 = org.telegram.messenger.LocaleController.getString(r8, r14);
        r11.append(r8);
        r8 = r6.send_stickers;
        r14 = r7.send_stickers;
        if (r8 != r14) goto L_0x0606;
        r8 = r6.send_inline;
        r14 = r7.send_inline;
        if (r8 != r14) goto L_0x0606;
        r8 = r6.send_gifs;
        r14 = r7.send_gifs;
        if (r8 != r14) goto L_0x0606;
        r8 = r6.send_games;
        r14 = r7.send_games;
        if (r8 == r14) goto L_0x0631;
        if (r13 != 0) goto L_0x060f;
        r8 = 10;
        r11.append(r8);
        r13 = 1;
        goto L_0x0611;
        r8 = 10;
        r11.append(r8);
        r8 = r7.send_stickers;
        if (r8 != 0) goto L_0x061b;
        r8 = 43;
        goto L_0x061d;
        r8 = 45;
        r11.append(r8);
        r8 = 32;
        r11.append(r8);
        r8 = "EventLogRestrictedSendStickers";
        r14 = 2131493520; // 0x7f0c0290 float:1.8610522E38 double:1.0530977226E-314;
        r8 = org.telegram.messenger.LocaleController.getString(r8, r14);
        r11.append(r8);
        r8 = r6.send_media;
        r14 = r7.send_media;
        if (r8 == r14) goto L_0x0662;
        if (r13 != 0) goto L_0x0640;
        r8 = 10;
        r11.append(r8);
        r13 = 1;
        goto L_0x0642;
        r8 = 10;
        r11.append(r8);
        r8 = r7.send_media;
        if (r8 != 0) goto L_0x064c;
        r8 = 43;
        goto L_0x064e;
        r8 = 45;
        r11.append(r8);
        r8 = 32;
        r11.append(r8);
        r8 = "EventLogRestrictedSendMedia";
        r14 = 2131493518; // 0x7f0c028e float:1.8610518E38 double:1.0530977216E-314;
        r8 = org.telegram.messenger.LocaleController.getString(r8, r14);
        r11.append(r8);
        r8 = r6.embed_links;
        r14 = r7.embed_links;
        if (r8 == r14) goto L_0x0692;
        if (r13 != 0) goto L_0x0670;
        r8 = 10;
        r11.append(r8);
        goto L_0x0672;
        r8 = 10;
        r11.append(r8);
        r8 = r7.embed_links;
        if (r8 != 0) goto L_0x067c;
        r8 = 43;
        goto L_0x067e;
        r8 = 45;
        r11.append(r8);
        r8 = 32;
        r11.append(r8);
        r8 = "EventLogRestrictedSendEmbed";
        r14 = 2131493517; // 0x7f0c028d float:1.8610516E38 double:1.053097721E-314;
        r8 = org.telegram.messenger.LocaleController.getString(r8, r14);
        r11.append(r8);
        r8 = r11.toString();
        r0.messageText = r8;
        goto L_0x06d4;
        r20 = r9;
        r21 = r11;
        r24 = r14;
        if (r7 == 0) goto L_0x06b1;
        if (r6 == 0) goto L_0x06a7;
        r8 = r7.view_messages;
        if (r8 == 0) goto L_0x06b1;
        r8 = "EventLogChannelRestricted";
        r9 = 2131493464; // 0x7f0c0258 float:1.8610409E38 double:1.053097695E-314;
        r8 = org.telegram.messenger.LocaleController.getString(r8, r9);
        goto L_0x06ba;
        r8 = "EventLogChannelUnrestricted";
        r9 = 2131493465; // 0x7f0c0259 float:1.861041E38 double:1.0530976954E-314;
        r8 = org.telegram.messenger.LocaleController.getString(r8, r9);
        r9 = "%1$s";
        r9 = r8.indexOf(r9);
        r10 = 1;
        r11 = new java.lang.Object[r10];
        r10 = r0.messageOwner;
        r10 = r10.entities;
        r10 = r0.getUserName(r5, r10, r9);
        r13 = 0;
        r11[r13] = r10;
        r10 = java.lang.String.format(r8, r11);
        r0.messageText = r10;
        goto L_0x0ac4;
        r20 = r9;
        r21 = r11;
        r24 = r14;
        r5 = r1.action;
        r5 = r5 instanceof org.telegram.tgnet.TLRPC.TL_channelAdminLogEventActionUpdatePinned;
        if (r5 == 0) goto L_0x0710;
        r5 = r1.action;
        r5 = r5.message;
        r5 = r5 instanceof org.telegram.tgnet.TLRPC.TL_messageEmpty;
        if (r5 == 0) goto L_0x06fd;
        r5 = "EventLogUnpinnedMessages";
        r6 = 2131493529; // 0x7f0c0299 float:1.861054E38 double:1.053097727E-314;
        r5 = org.telegram.messenger.LocaleController.getString(r5, r6);
        r6 = "un1";
        r5 = r0.replaceWithLink(r5, r6, r4);
        r0.messageText = r5;
        goto L_0x0ac4;
        r5 = "EventLogPinnedMessages";
        r6 = 2131493498; // 0x7f0c027a float:1.8610478E38 double:1.0530977117E-314;
        r5 = org.telegram.messenger.LocaleController.getString(r5, r6);
        r6 = "un1";
        r5 = r0.replaceWithLink(r5, r6, r4);
        r0.messageText = r5;
        goto L_0x0ac4;
        r5 = r1.action;
        r5 = r5 instanceof org.telegram.tgnet.TLRPC.TL_channelAdminLogEventActionToggleSignatures;
        if (r5 == 0) goto L_0x0744;
        r5 = r1.action;
        r5 = (org.telegram.tgnet.TLRPC.TL_channelAdminLogEventActionToggleSignatures) r5;
        r5 = r5.new_value;
        if (r5 == 0) goto L_0x0731;
        r5 = "EventLogToggledSignaturesOn";
        r6 = 2131493528; // 0x7f0c0298 float:1.8610539E38 double:1.0530977265E-314;
        r5 = org.telegram.messenger.LocaleController.getString(r5, r6);
        r6 = "un1";
        r5 = r0.replaceWithLink(r5, r6, r4);
        r0.messageText = r5;
        goto L_0x0ac4;
        r5 = "EventLogToggledSignaturesOff";
        r6 = 2131493527; // 0x7f0c0297 float:1.8610537E38 double:1.053097726E-314;
        r5 = org.telegram.messenger.LocaleController.getString(r5, r6);
        r6 = "un1";
        r5 = r0.replaceWithLink(r5, r6, r4);
        r0.messageText = r5;
        goto L_0x0ac4;
        r5 = r1.action;
        r5 = r5 instanceof org.telegram.tgnet.TLRPC.TL_channelAdminLogEventActionToggleInvites;
        if (r5 == 0) goto L_0x0778;
        r5 = r1.action;
        r5 = (org.telegram.tgnet.TLRPC.TL_channelAdminLogEventActionToggleInvites) r5;
        r5 = r5.new_value;
        if (r5 == 0) goto L_0x0765;
        r5 = "EventLogToggledInvitesOn";
        r6 = 2131493526; // 0x7f0c0296 float:1.8610535E38 double:1.0530977255E-314;
        r5 = org.telegram.messenger.LocaleController.getString(r5, r6);
        r6 = "un1";
        r5 = r0.replaceWithLink(r5, r6, r4);
        r0.messageText = r5;
        goto L_0x0ac4;
        r5 = "EventLogToggledInvitesOff";
        r6 = 2131493525; // 0x7f0c0295 float:1.8610533E38 double:1.053097725E-314;
        r5 = org.telegram.messenger.LocaleController.getString(r5, r6);
        r6 = "un1";
        r5 = r0.replaceWithLink(r5, r6, r4);
        r0.messageText = r5;
        goto L_0x0ac4;
        r5 = r1.action;
        r5 = r5 instanceof org.telegram.tgnet.TLRPC.TL_channelAdminLogEventActionDeleteMessage;
        if (r5 == 0) goto L_0x0791;
        r5 = "EventLogDeletedMessages";
        r6 = 2131493466; // 0x7f0c025a float:1.8610413E38 double:1.053097696E-314;
        r5 = org.telegram.messenger.LocaleController.getString(r5, r6);
        r6 = "un1";
        r5 = r0.replaceWithLink(r5, r6, r4);
        r0.messageText = r5;
        goto L_0x0ac4;
        r5 = r1.action;
        r5 = r5 instanceof org.telegram.tgnet.TLRPC.TL_channelAdminLogEventActionTogglePreHistoryHidden;
        if (r5 == 0) goto L_0x07c5;
        r5 = r1.action;
        r5 = (org.telegram.tgnet.TLRPC.TL_channelAdminLogEventActionTogglePreHistoryHidden) r5;
        r5 = r5.new_value;
        if (r5 == 0) goto L_0x07b2;
        r5 = "EventLogToggledInvitesHistoryOff";
        r6 = 2131493523; // 0x7f0c0293 float:1.8610529E38 double:1.053097724E-314;
        r5 = org.telegram.messenger.LocaleController.getString(r5, r6);
        r6 = "un1";
        r5 = r0.replaceWithLink(r5, r6, r4);
        r0.messageText = r5;
        goto L_0x0ac4;
        r5 = "EventLogToggledInvitesHistoryOn";
        r6 = 2131493524; // 0x7f0c0294 float:1.861053E38 double:1.0530977245E-314;
        r5 = org.telegram.messenger.LocaleController.getString(r5, r6);
        r6 = "un1";
        r5 = r0.replaceWithLink(r5, r6, r4);
        r0.messageText = r5;
        goto L_0x0ac4;
        r5 = r1.action;
        r5 = r5 instanceof org.telegram.tgnet.TLRPC.TL_channelAdminLogEventActionChangeAbout;
        if (r5 == 0) goto L_0x085e;
        r5 = r3.megagroup;
        if (r5 == 0) goto L_0x07d9;
        r5 = "EventLogEditedGroupDescription";
        r6 = 2131493471; // 0x7f0c025f float:1.8610423E38 double:1.0530976984E-314;
        r5 = org.telegram.messenger.LocaleController.getString(r5, r6);
        goto L_0x07df;
        r5 = "EventLogEditedChannelDescription";
        r6 = 2131493468; // 0x7f0c025c float:1.8610417E38 double:1.053097697E-314;
        goto L_0x07d4;
        r6 = "un1";
        r5 = r0.replaceWithLink(r5, r6, r4);
        r0.messageText = r5;
        r5 = new org.telegram.tgnet.TLRPC$TL_message;
        r5.<init>();
        r14 = r5;
        r5 = 0;
        r14.out = r5;
        r14.unread = r5;
        r5 = r1.user_id;
        r14.from_id = r5;
        r14.to_id = r12;
        r5 = r1.date;
        r14.date = r5;
        r5 = r1.action;
        r5 = (org.telegram.tgnet.TLRPC.TL_channelAdminLogEventActionChangeAbout) r5;
        r5 = r5.new_value;
        r14.message = r5;
        r5 = r1.action;
        r5 = (org.telegram.tgnet.TLRPC.TL_channelAdminLogEventActionChangeAbout) r5;
        r5 = r5.prev_value;
        r5 = android.text.TextUtils.isEmpty(r5);
        if (r5 != 0) goto L_0x0855;
        r5 = new org.telegram.tgnet.TLRPC$TL_messageMediaWebPage;
        r5.<init>();
        r14.media = r5;
        r5 = r14.media;
        r6 = new org.telegram.tgnet.TLRPC$TL_webPage;
        r6.<init>();
        r5.webpage = r6;
        r5 = r14.media;
        r5 = r5.webpage;
        r6 = 10;
        r5.flags = r6;
        r5 = r14.media;
        r5 = r5.webpage;
        r6 = "";
        r5.display_url = r6;
        r5 = r14.media;
        r5 = r5.webpage;
        r6 = "";
        r5.url = r6;
        r5 = r14.media;
        r5 = r5.webpage;
        r6 = "EventLogPreviousGroupDescription";
        r7 = 2131493499; // 0x7f0c027b float:1.861048E38 double:1.053097712E-314;
        r6 = org.telegram.messenger.LocaleController.getString(r6, r7);
        r5.site_name = r6;
        r5 = r14.media;
        r5 = r5.webpage;
        r6 = r1.action;
        r6 = (org.telegram.tgnet.TLRPC.TL_channelAdminLogEventActionChangeAbout) r6;
        r6 = r6.prev_value;
        r5.description = r6;
        goto L_0x0ac6;
        r5 = new org.telegram.tgnet.TLRPC$TL_messageMediaEmpty;
        r5.<init>();
        r14.media = r5;
        goto L_0x0ac6;
        r5 = r1.action;
        r5 = r5 instanceof org.telegram.tgnet.TLRPC.TL_channelAdminLogEventActionChangeUsername;
        if (r5 == 0) goto L_0x0977;
        r5 = r1.action;
        r5 = (org.telegram.tgnet.TLRPC.TL_channelAdminLogEventActionChangeUsername) r5;
        r5 = r5.new_value;
        r6 = android.text.TextUtils.isEmpty(r5);
        if (r6 != 0) goto L_0x088d;
        r6 = r3.megagroup;
        if (r6 == 0) goto L_0x087e;
        r6 = "EventLogChangedGroupLink";
        r7 = 2131493461; // 0x7f0c0255 float:1.8610403E38 double:1.0530976934E-314;
        r6 = org.telegram.messenger.LocaleController.getString(r6, r7);
        goto L_0x0884;
        r6 = "EventLogChangedChannelLink";
        r7 = 2131493460; // 0x7f0c0254 float:1.86104E38 double:1.053097693E-314;
        goto L_0x0879;
        r7 = "un1";
        r6 = r0.replaceWithLink(r6, r7, r4);
        r0.messageText = r6;
        goto L_0x08a9;
        r6 = r3.megagroup;
        if (r6 == 0) goto L_0x089b;
        r6 = "EventLogRemovedGroupLink";
        r7 = 2131493513; // 0x7f0c0289 float:1.8610508E38 double:1.053097719E-314;
        r6 = org.telegram.messenger.LocaleController.getString(r6, r7);
        goto L_0x08a1;
        r6 = "EventLogRemovedChannelLink";
        r7 = 2131493511; // 0x7f0c0287 float:1.8610504E38 double:1.053097718E-314;
        goto L_0x0896;
        r7 = "un1";
        r6 = r0.replaceWithLink(r6, r7, r4);
        r0.messageText = r6;
        r6 = new org.telegram.tgnet.TLRPC$TL_message;
        r6.<init>();
        r14 = r6;
        r6 = 0;
        r14.out = r6;
        r14.unread = r6;
        r6 = r1.user_id;
        r14.from_id = r6;
        r14.to_id = r12;
        r6 = r1.date;
        r14.date = r6;
        r6 = android.text.TextUtils.isEmpty(r5);
        if (r6 != 0) goto L_0x08e6;
        r6 = new java.lang.StringBuilder;
        r6.<init>();
        r7 = "https://";
        r6.append(r7);
        r7 = org.telegram.messenger.MessagesController.getInstance(r32);
        r7 = r7.linkPrefix;
        r6.append(r7);
        r7 = "/";
        r6.append(r7);
        r6.append(r5);
        r6 = r6.toString();
        r14.message = r6;
        goto L_0x08ea;
        r6 = "";
        r14.message = r6;
        r6 = new org.telegram.tgnet.TLRPC$TL_messageEntityUrl;
        r6.<init>();
        r7 = 0;
        r6.offset = r7;
        r7 = r14.message;
        r7 = r7.length();
        r6.length = r7;
        r7 = r14.entities;
        r7.add(r6);
        r7 = r1.action;
        r7 = (org.telegram.tgnet.TLRPC.TL_channelAdminLogEventActionChangeUsername) r7;
        r7 = r7.prev_value;
        r7 = android.text.TextUtils.isEmpty(r7);
        if (r7 != 0) goto L_0x096e;
        r7 = new org.telegram.tgnet.TLRPC$TL_messageMediaWebPage;
        r7.<init>();
        r14.media = r7;
        r7 = r14.media;
        r8 = new org.telegram.tgnet.TLRPC$TL_webPage;
        r8.<init>();
        r7.webpage = r8;
        r7 = r14.media;
        r7 = r7.webpage;
        r8 = 10;
        r7.flags = r8;
        r7 = r14.media;
        r7 = r7.webpage;
        r8 = "";
        r7.display_url = r8;
        r7 = r14.media;
        r7 = r7.webpage;
        r8 = "";
        r7.url = r8;
        r7 = r14.media;
        r7 = r7.webpage;
        r8 = "EventLogPreviousLink";
        r9 = 2131493500; // 0x7f0c027c float:1.8610482E38 double:1.0530977127E-314;
        r8 = org.telegram.messenger.LocaleController.getString(r8, r9);
        r7.site_name = r8;
        r7 = r14.media;
        r7 = r7.webpage;
        r8 = new java.lang.StringBuilder;
        r8.<init>();
        r9 = "https://";
        r8.append(r9);
        r9 = org.telegram.messenger.MessagesController.getInstance(r32);
        r9 = r9.linkPrefix;
        r8.append(r9);
        r9 = "/";
        r8.append(r9);
        r9 = r1.action;
        r9 = (org.telegram.tgnet.TLRPC.TL_channelAdminLogEventActionChangeUsername) r9;
        r9 = r9.prev_value;
        r8.append(r9);
        r8 = r8.toString();
        r7.description = r8;
        goto L_0x0975;
        r7 = new org.telegram.tgnet.TLRPC$TL_messageMediaEmpty;
        r7.<init>();
        r14.media = r7;
        goto L_0x0ac6;
        r5 = r1.action;
        r5 = r5 instanceof org.telegram.tgnet.TLRPC.TL_channelAdminLogEventActionEditMessage;
        if (r5 == 0) goto L_0x0a72;
        r5 = new org.telegram.tgnet.TLRPC$TL_message;
        r5.<init>();
        r14 = r5;
        r5 = 0;
        r14.out = r5;
        r14.unread = r5;
        r5 = r1.user_id;
        r14.from_id = r5;
        r14.to_id = r12;
        r5 = r1.date;
        r14.date = r5;
        r5 = r1.action;
        r5 = (org.telegram.tgnet.TLRPC.TL_channelAdminLogEventActionEditMessage) r5;
        r5 = r5.new_message;
        r6 = r1.action;
        r6 = (org.telegram.tgnet.TLRPC.TL_channelAdminLogEventActionEditMessage) r6;
        r6 = r6.prev_message;
        r7 = r5.media;
        r8 = 2131493496; // 0x7f0c0278 float:1.8610474E38 double:1.0530977107E-314;
        if (r7 == 0) goto L_0x0a04;
        r7 = r5.media;
        r7 = r7 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaEmpty;
        if (r7 != 0) goto L_0x0a04;
        r7 = r5.media;
        r7 = r7 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaWebPage;
        if (r7 != 0) goto L_0x0a04;
        r7 = r5.message;
        r7 = android.text.TextUtils.isEmpty(r7);
        if (r7 == 0) goto L_0x0a04;
        r7 = "EventLogEditedCaption";
        r9 = 2131493467; // 0x7f0c025b float:1.8610415E38 double:1.0530976964E-314;
        r7 = org.telegram.messenger.LocaleController.getString(r7, r9);
        r9 = "un1";
        r7 = r0.replaceWithLink(r7, r9, r4);
        r0.messageText = r7;
        r7 = r5.media;
        r14.media = r7;
        r7 = r14.media;
        r9 = new org.telegram.tgnet.TLRPC$TL_webPage;
        r9.<init>();
        r7.webpage = r9;
        r7 = r14.media;
        r7 = r7.webpage;
        r9 = "EventLogOriginalCaption";
        r10 = 2131493495; // 0x7f0c0277 float:1.8610472E38 double:1.05309771E-314;
        r9 = org.telegram.messenger.LocaleController.getString(r9, r10);
        r7.site_name = r9;
        r7 = r6.message;
        r7 = android.text.TextUtils.isEmpty(r7);
        if (r7 == 0) goto L_0x09fb;
        r7 = r14.media;
        r7 = r7.webpage;
        r9 = "EventLogOriginalCaptionEmpty";
        r8 = org.telegram.messenger.LocaleController.getString(r9, r8);
        r7.description = r8;
        goto L_0x0a55;
        r7 = r14.media;
        r7 = r7.webpage;
        r8 = r6.message;
        r7.description = r8;
        goto L_0x0a55;
        r7 = "EventLogEditedMessages";
        r9 = 2131493474; // 0x7f0c0262 float:1.861043E38 double:1.0530977E-314;
        r7 = org.telegram.messenger.LocaleController.getString(r7, r9);
        r9 = "un1";
        r7 = r0.replaceWithLink(r7, r9, r4);
        r0.messageText = r7;
        r7 = r5.message;
        r14.message = r7;
        r7 = new org.telegram.tgnet.TLRPC$TL_messageMediaWebPage;
        r7.<init>();
        r14.media = r7;
        r7 = r14.media;
        r9 = new org.telegram.tgnet.TLRPC$TL_webPage;
        r9.<init>();
        r7.webpage = r9;
        r7 = r14.media;
        r7 = r7.webpage;
        r9 = "EventLogOriginalMessages";
        r10 = 2131493497; // 0x7f0c0279 float:1.8610476E38 double:1.053097711E-314;
        r9 = org.telegram.messenger.LocaleController.getString(r9, r10);
        r7.site_name = r9;
        r7 = r6.message;
        r7 = android.text.TextUtils.isEmpty(r7);
        if (r7 == 0) goto L_0x0a4d;
        r7 = r14.media;
        r7 = r7.webpage;
        r9 = "EventLogOriginalCaptionEmpty";
        r8 = org.telegram.messenger.LocaleController.getString(r9, r8);
        r7.description = r8;
        goto L_0x0a55;
        r7 = r14.media;
        r7 = r7.webpage;
        r8 = r6.message;
        r7.description = r8;
        r7 = r5.reply_markup;
        r14.reply_markup = r7;
        r7 = r14.media;
        r7 = r7.webpage;
        r8 = 10;
        r7.flags = r8;
        r7 = r14.media;
        r7 = r7.webpage;
        r8 = "";
        r7.display_url = r8;
        r7 = r14.media;
        r7 = r7.webpage;
        r8 = "";
        r7.url = r8;
        goto L_0x0ac6;
        r5 = r1.action;
        r5 = r5 instanceof org.telegram.tgnet.TLRPC.TL_channelAdminLogEventActionChangeStickerSet;
        if (r5 == 0) goto L_0x0aaf;
        r5 = r1.action;
        r5 = (org.telegram.tgnet.TLRPC.TL_channelAdminLogEventActionChangeStickerSet) r5;
        r5 = r5.new_stickerset;
        r6 = r1.action;
        r6 = (org.telegram.tgnet.TLRPC.TL_channelAdminLogEventActionChangeStickerSet) r6;
        r6 = r6.new_stickerset;
        if (r5 == 0) goto L_0x0a9d;
        r7 = r5 instanceof org.telegram.tgnet.TLRPC.TL_inputStickerSetEmpty;
        if (r7 == 0) goto L_0x0a8b;
        goto L_0x0a9d;
        r7 = "EventLogChangedStickersSet";
        r8 = 2131493462; // 0x7f0c0256 float:1.8610405E38 double:1.053097694E-314;
        r7 = org.telegram.messenger.LocaleController.getString(r7, r8);
        r8 = "un1";
        r7 = r0.replaceWithLink(r7, r8, r4);
        r0.messageText = r7;
        goto L_0x0aae;
        r7 = "EventLogRemovedStickersSet";
        r8 = 2131493514; // 0x7f0c028a float:1.861051E38 double:1.0530977196E-314;
        r7 = org.telegram.messenger.LocaleController.getString(r7, r8);
        r8 = "un1";
        r7 = r0.replaceWithLink(r7, r8, r4);
        r0.messageText = r7;
        goto L_0x0ac4;
        r5 = new java.lang.StringBuilder;
        r5.<init>();
        r6 = "unsupported ";
        r5.append(r6);
        r6 = r1.action;
        r5.append(r6);
        r5 = r5.toString();
        r0.messageText = r5;
        r14 = r24;
        r5 = r0.messageOwner;
        if (r5 != 0) goto L_0x0ad1;
        r5 = new org.telegram.tgnet.TLRPC$TL_messageService;
        r5.<init>();
        r0.messageOwner = r5;
        r5 = r0.messageOwner;
        r6 = r0.messageText;
        r6 = r6.toString();
        r5.message = r6;
        r5 = r0.messageOwner;
        r6 = r1.user_id;
        r5.from_id = r6;
        r5 = r0.messageOwner;
        r6 = r1.date;
        r5.date = r6;
        r5 = r0.messageOwner;
        r6 = 0;
        r7 = r37[r6];
        r8 = r7 + 1;
        r37[r6] = r8;
        r5.id = r7;
        r7 = r1.id;
        r0.eventId = r7;
        r5 = r0.messageOwner;
        r5.out = r6;
        r5 = r0.messageOwner;
        r6 = new org.telegram.tgnet.TLRPC$TL_peerChannel;
        r6.<init>();
        r5.to_id = r6;
        r5 = r0.messageOwner;
        r5 = r5.to_id;
        r6 = r3.id;
        r5.channel_id = r6;
        r5 = r0.messageOwner;
        r6 = 0;
        r5.unread = r6;
        r5 = r3.megagroup;
        r6 = -2147483648; // 0xffffffff80000000 float:-0.0 double:NaN;
        if (r5 == 0) goto L_0x0b1d;
        r5 = r0.messageOwner;
        r7 = r5.flags;
        r7 = r7 | r6;
        r5.flags = r7;
        r5 = org.telegram.messenger.MediaController.getInstance();
        r7 = r1.action;
        r7 = r7.message;
        if (r7 == 0) goto L_0x0b33;
        r7 = r1.action;
        r7 = r7.message;
        r7 = r7 instanceof org.telegram.tgnet.TLRPC.TL_messageEmpty;
        if (r7 != 0) goto L_0x0b33;
        r7 = r1.action;
        r14 = r7.message;
        if (r14 == 0) goto L_0x0b8e;
        r7 = 0;
        r14.out = r7;
        r8 = r37[r7];
        r9 = r8 + 1;
        r37[r7] = r9;
        r14.id = r8;
        r14.reply_to_msg_id = r7;
        r7 = r14.flags;
        r8 = -32769; // 0xffffffffffff7fff float:NaN double:NaN;
        r7 = r7 & r8;
        r14.flags = r7;
        r7 = r3.megagroup;
        if (r7 == 0) goto L_0x0b53;
        r7 = r14.flags;
        r6 = r6 | r7;
        r14.flags = r6;
        r6 = new org.telegram.messenger.MessageObject;
        r25 = 0;
        r26 = 0;
        r27 = 1;
        r7 = r0.eventId;
        r22 = r6;
        r23 = r32;
        r24 = r14;
        r28 = r7;
        r22.<init>(r23, r24, r25, r26, r27, r28);
        r7 = r6.contentType;
        if (r7 < 0) goto L_0x0b8b;
        r7 = r5.isPlayingMessage(r6);
        if (r7 == 0) goto L_0x0b7e;
        r7 = r5.getPlayingMessageObject();
        r8 = r7.audioProgress;
        r6.audioProgress = r8;
        r8 = r7.audioProgressSec;
        r6.audioProgressSec = r8;
        r31.createDateArray(r32, r33, r34, r35);
        r7 = r34.size();
        r8 = 1;
        r7 = r7 - r8;
        r2.add(r7, r6);
        goto L_0x0b8e;
        r7 = -1;
        r0.contentType = r7;
        r6 = r0.contentType;
        if (r6 < 0) goto L_0x0c97;
        r31.createDateArray(r32, r33, r34, r35);
        r6 = r34.size();
        r7 = 1;
        r6 = r6 - r7;
        r2.add(r6, r0);
        r6 = r0.messageText;
        if (r6 != 0) goto L_0x0ba6;
        r6 = "";
        r0.messageText = r6;
        r31.setType();
        r31.measureInlineBotButtons();
        r6 = r0.messageOwner;
        r6 = r6.message;
        if (r6 == 0) goto L_0x0bf4;
        r6 = r0.messageOwner;
        r6 = r6.id;
        if (r6 >= 0) goto L_0x0bf4;
        r6 = r0.messageOwner;
        r6 = r6.message;
        r6 = r6.length();
        r7 = 6;
        if (r6 <= r7) goto L_0x0bf4;
        r6 = r31.isVideo();
        if (r6 != 0) goto L_0x0bd5;
        r6 = r31.isNewGif();
        if (r6 != 0) goto L_0x0bd5;
        r6 = r31.isRoundVideo();
        if (r6 == 0) goto L_0x0bf4;
        r6 = new org.telegram.messenger.VideoEditedInfo;
        r6.<init>();
        r0.videoEditedInfo = r6;
        r6 = r0.videoEditedInfo;
        r7 = r0.messageOwner;
        r7 = r7.message;
        r6 = r6.parseString(r7);
        if (r6 != 0) goto L_0x0bec;
        r6 = 0;
        r0.videoEditedInfo = r6;
        goto L_0x0bf4;
        r6 = r0.videoEditedInfo;
        r7 = r31.isRoundVideo();
        r6.roundVideo = r7;
        r31.generateCaption();
        r6 = r0.messageOwner;
        r6 = r6.media;
        r6 = r6 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaGame;
        if (r6 == 0) goto L_0x0c02;
        r6 = org.telegram.ui.ActionBar.Theme.chat_msgGameTextPaint;
        goto L_0x0c04;
        r6 = org.telegram.ui.ActionBar.Theme.chat_msgTextPaint;
        r7 = org.telegram.messenger.SharedConfig.allowBigEmoji;
        if (r7 == 0) goto L_0x0c0c;
        r7 = 1;
        r8 = new int[r7];
        goto L_0x0c0d;
        r8 = 0;
        r7 = r8;
        r8 = r0.messageText;
        r9 = r6.getFontMetricsInt();
        r10 = 1101004800; // 0x41a00000 float:20.0 double:5.439686476E-315;
        r10 = org.telegram.messenger.AndroidUtilities.dp(r10);
        r11 = 0;
        r8 = org.telegram.messenger.Emoji.replaceEmoji(r8, r9, r10, r11, r7);
        r0.messageText = r8;
        if (r7 == 0) goto L_0x0c77;
        r8 = r7[r11];
        r9 = 1;
        if (r8 < r9) goto L_0x0c77;
        r8 = r7[r11];
        r9 = 3;
        if (r8 > r9) goto L_0x0c77;
        r8 = r7[r11];
        switch(r8) {
            case 1: goto L_0x0c44;
            case 2: goto L_0x0c3b;
            default: goto L_0x0c32;
        };
        r8 = org.telegram.ui.ActionBar.Theme.chat_msgTextPaintThreeEmoji;
        r9 = 1103101952; // 0x41c00000 float:24.0 double:5.450047783E-315;
        r9 = org.telegram.messenger.AndroidUtilities.dp(r9);
        goto L_0x0c4d;
        r8 = org.telegram.ui.ActionBar.Theme.chat_msgTextPaintTwoEmoji;
        r9 = 1105199104; // 0x41e00000 float:28.0 double:5.46040909E-315;
        r9 = org.telegram.messenger.AndroidUtilities.dp(r9);
        goto L_0x0c4d;
        r8 = org.telegram.ui.ActionBar.Theme.chat_msgTextPaintOneEmoji;
        r9 = 1107296256; // 0x42000000 float:32.0 double:5.4707704E-315;
        r9 = org.telegram.messenger.AndroidUtilities.dp(r9);
        r10 = r0.messageText;
        r10 = (android.text.Spannable) r10;
        r11 = r0.messageText;
        r11 = r11.length();
        r13 = org.telegram.messenger.Emoji.EmojiSpan.class;
        r1 = 0;
        r10 = r10.getSpans(r1, r11, r13);
        r1 = r10;
        r1 = (org.telegram.messenger.Emoji.EmojiSpan[]) r1;
        if (r1 == 0) goto L_0x0c77;
        r10 = r1.length;
        if (r10 <= 0) goto L_0x0c77;
        r10 = 0;
        r11 = r1.length;
        if (r10 >= r11) goto L_0x0c77;
        r11 = r1[r10];
        r13 = r8.getFontMetricsInt();
        r11.replaceFontMetrics(r13, r9);
        r10 = r10 + 1;
        goto L_0x0c68;
        r1 = r5.isPlayingMessage(r0);
        if (r1 == 0) goto L_0x0c89;
        r1 = r5.getPlayingMessageObject();
        r8 = r1.audioProgress;
        r0.audioProgress = r8;
        r8 = r1.audioProgressSec;
        r0.audioProgressSec = r8;
        r0.generateLayout(r4);
        r1 = 1;
        r0.layoutCreated = r1;
        r1 = 0;
        r0.generateThumbs(r1);
        r31.checkMediaExistance();
        return;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.MessageObject.<init>(int, org.telegram.tgnet.TLRPC$TL_channelAdminLogEvent, java.util.ArrayList, java.util.HashMap, org.telegram.tgnet.TLRPC$Chat, int[]):void");
    }

    public void checkMediaExistance() {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.MessageObject.checkMediaExistance():void
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
        r0 = 0;
        r1 = 0;
        r6.attachPathExists = r1;
        r6.mediaExists = r1;
        r1 = r6.type;
        r2 = 1;
        if (r1 != r2) goto L_0x004f;
    L_0x000b:
        r1 = r6.photoThumbs;
        r2 = org.telegram.messenger.AndroidUtilities.getPhotoSize();
        r1 = org.telegram.messenger.FileLoader.getClosestPhotoSizeWithSize(r1, r2);
        if (r1 == 0) goto L_0x004d;
    L_0x0017:
        r2 = r6.messageOwner;
        r2 = org.telegram.messenger.FileLoader.getPathToMessage(r2);
        r3 = r6.needDrawBluredPreview();
        if (r3 == 0) goto L_0x0043;
    L_0x0023:
        r3 = new java.io.File;
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r5 = r2.getAbsolutePath();
        r4.append(r5);
        r5 = ".enc";
        r4.append(r5);
        r4 = r4.toString();
        r3.<init>(r4);
        r3 = r3.exists();
        r6.mediaExists = r3;
    L_0x0043:
        r3 = r6.mediaExists;
        if (r3 != 0) goto L_0x004d;
    L_0x0047:
        r3 = r2.exists();
        r6.mediaExists = r3;
    L_0x004d:
        goto L_0x00ff;
    L_0x004f:
        r1 = r6.type;
        r3 = 8;
        r4 = 3;
        if (r1 == r3) goto L_0x00a1;
    L_0x0056:
        r1 = r6.type;
        if (r1 == r4) goto L_0x00a1;
    L_0x005a:
        r1 = r6.type;
        r3 = 9;
        if (r1 == r3) goto L_0x00a1;
    L_0x0060:
        r1 = r6.type;
        r3 = 2;
        if (r1 == r3) goto L_0x00a1;
    L_0x0065:
        r1 = r6.type;
        r3 = 14;
        if (r1 == r3) goto L_0x00a1;
    L_0x006b:
        r1 = r6.type;
        r3 = 5;
        if (r1 != r3) goto L_0x0071;
    L_0x0070:
        goto L_0x00a1;
    L_0x0071:
        r1 = r6.getDocument();
        if (r1 == 0) goto L_0x0083;
    L_0x0077:
        r2 = org.telegram.messenger.FileLoader.getPathToAttach(r1);
        r2 = r2.exists();
        r6.mediaExists = r2;
        goto L_0x00ff;
    L_0x0083:
        r3 = r6.type;
        if (r3 != 0) goto L_0x00ff;
    L_0x0087:
        r3 = r6.photoThumbs;
        r4 = org.telegram.messenger.AndroidUtilities.getPhotoSize();
        r3 = org.telegram.messenger.FileLoader.getClosestPhotoSizeWithSize(r3, r4);
        if (r3 != 0) goto L_0x0094;
    L_0x0093:
        return;
    L_0x0094:
        if (r3 == 0) goto L_0x00ff;
    L_0x0096:
        r2 = org.telegram.messenger.FileLoader.getPathToAttach(r3, r2);
        r2 = r2.exists();
        r6.mediaExists = r2;
        goto L_0x00ff;
    L_0x00a1:
        r1 = r6.messageOwner;
        r1 = r1.attachPath;
        if (r1 == 0) goto L_0x00c0;
    L_0x00a7:
        r1 = r6.messageOwner;
        r1 = r1.attachPath;
        r1 = r1.length();
        if (r1 <= 0) goto L_0x00c0;
    L_0x00b1:
        r1 = new java.io.File;
        r2 = r6.messageOwner;
        r2 = r2.attachPath;
        r1.<init>(r2);
        r2 = r1.exists();
        r6.attachPathExists = r2;
    L_0x00c0:
        r1 = r6.attachPathExists;
        if (r1 != 0) goto L_0x00ff;
    L_0x00c4:
        r1 = r6.messageOwner;
        r1 = org.telegram.messenger.FileLoader.getPathToMessage(r1);
        r2 = r6.type;
        if (r2 != r4) goto L_0x00f4;
    L_0x00ce:
        r2 = r6.needDrawBluredPreview();
        if (r2 == 0) goto L_0x00f4;
    L_0x00d4:
        r2 = new java.io.File;
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r4 = r1.getAbsolutePath();
        r3.append(r4);
        r4 = ".enc";
        r3.append(r4);
        r3 = r3.toString();
        r2.<init>(r3);
        r2 = r2.exists();
        r6.mediaExists = r2;
    L_0x00f4:
        r2 = r6.mediaExists;
        if (r2 != 0) goto L_0x00fe;
    L_0x00f8:
        r2 = r1.exists();
        r6.mediaExists = r2;
    L_0x00ff:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.MessageObject.checkMediaExistance():void");
    }

    public void generateLayout(org.telegram.tgnet.TLRPC.User r1) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.MessageObject.generateLayout(org.telegram.tgnet.TLRPC$User):void
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
        r1 = r36;
        r2 = r37;
        r3 = r1.type;
        if (r3 != 0) goto L_0x0526;
    L_0x0008:
        r3 = r1.messageOwner;
        r3 = r3.to_id;
        if (r3 == 0) goto L_0x0526;
    L_0x000e:
        r3 = r1.messageText;
        r3 = android.text.TextUtils.isEmpty(r3);
        if (r3 == 0) goto L_0x0018;
    L_0x0016:
        goto L_0x0526;
    L_0x0018:
        r36.generateLinkDescription();
        r3 = new java.util.ArrayList;
        r3.<init>();
        r1.textLayoutBlocks = r3;
        r3 = 0;
        r1.textWidth = r3;
        r4 = r1.messageOwner;
        r4 = r4.send_state;
        r5 = 1;
        if (r4 == 0) goto L_0x0049;
    L_0x002c:
        r4 = 0;
        r6 = r3;
    L_0x002e:
        r7 = r1.messageOwner;
        r7 = r7.entities;
        r7 = r7.size();
        if (r6 >= r7) goto L_0x0052;
    L_0x0038:
        r7 = r1.messageOwner;
        r7 = r7.entities;
        r7 = r7.get(r6);
        r7 = r7 instanceof org.telegram.tgnet.TLRPC.TL_inputMessageEntityMentionName;
        if (r7 != 0) goto L_0x0046;
    L_0x0044:
        r4 = 1;
        goto L_0x0052;
    L_0x0046:
        r6 = r6 + 1;
        goto L_0x002e;
    L_0x0049:
        r4 = r1.messageOwner;
        r4 = r4.entities;
        r4 = r4.isEmpty();
        r4 = r4 ^ r5;
    L_0x0052:
        r6 = 0;
        if (r4 != 0) goto L_0x00aa;
    L_0x0056:
        r8 = r1.eventId;
        r10 = (r8 > r6 ? 1 : (r8 == r6 ? 0 : -1));
        if (r10 != 0) goto L_0x00a8;
    L_0x005c:
        r8 = r1.messageOwner;
        r8 = r8 instanceof org.telegram.tgnet.TLRPC.TL_message_old;
        if (r8 != 0) goto L_0x00a8;
    L_0x0062:
        r8 = r1.messageOwner;
        r8 = r8 instanceof org.telegram.tgnet.TLRPC.TL_message_old2;
        if (r8 != 0) goto L_0x00a8;
    L_0x0068:
        r8 = r1.messageOwner;
        r8 = r8 instanceof org.telegram.tgnet.TLRPC.TL_message_old3;
        if (r8 != 0) goto L_0x00a8;
    L_0x006e:
        r8 = r1.messageOwner;
        r8 = r8 instanceof org.telegram.tgnet.TLRPC.TL_message_old4;
        if (r8 != 0) goto L_0x00a8;
    L_0x0074:
        r8 = r1.messageOwner;
        r8 = r8 instanceof org.telegram.tgnet.TLRPC.TL_messageForwarded_old;
        if (r8 != 0) goto L_0x00a8;
    L_0x007a:
        r8 = r1.messageOwner;
        r8 = r8 instanceof org.telegram.tgnet.TLRPC.TL_messageForwarded_old2;
        if (r8 != 0) goto L_0x00a8;
    L_0x0080:
        r8 = r1.messageOwner;
        r8 = r8 instanceof org.telegram.tgnet.TLRPC.TL_message_secret;
        if (r8 != 0) goto L_0x00a8;
    L_0x0086:
        r8 = r1.messageOwner;
        r8 = r8.media;
        r8 = r8 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaInvoice;
        if (r8 != 0) goto L_0x00a8;
    L_0x008e:
        r8 = r36.isOut();
        if (r8 == 0) goto L_0x009a;
    L_0x0094:
        r8 = r1.messageOwner;
        r8 = r8.send_state;
        if (r8 != 0) goto L_0x00a8;
    L_0x009a:
        r8 = r1.messageOwner;
        r8 = r8.id;
        if (r8 < 0) goto L_0x00a8;
    L_0x00a0:
        r8 = r1.messageOwner;
        r8 = r8.media;
        r8 = r8 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaUnsupported;
        if (r8 == 0) goto L_0x00aa;
    L_0x00a8:
        r8 = r5;
        goto L_0x00ab;
    L_0x00aa:
        r8 = r3;
    L_0x00ab:
        if (r8 == 0) goto L_0x00b7;
    L_0x00ad:
        r9 = r36.isOutOwner();
        r10 = r1.messageText;
        addLinks(r9, r10);
        goto L_0x00d5;
    L_0x00b7:
        r9 = r1.messageText;
        r9 = r9 instanceof android.text.Spannable;
        if (r9 == 0) goto L_0x00d5;
    L_0x00bd:
        r9 = r1.messageText;
        r9 = r9.length();
        r10 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
        if (r9 >= r10) goto L_0x00d5;
    L_0x00c7:
        r9 = r1.messageText;	 Catch:{ Throwable -> 0x00d0 }
        r9 = (android.text.Spannable) r9;	 Catch:{ Throwable -> 0x00d0 }
        r10 = 4;	 Catch:{ Throwable -> 0x00d0 }
        android.text.util.Linkify.addLinks(r9, r10);	 Catch:{ Throwable -> 0x00d0 }
        goto L_0x00d5;
    L_0x00d0:
        r0 = move-exception;
        r9 = r0;
        org.telegram.messenger.FileLog.e(r9);
    L_0x00d5:
        r9 = r1.messageText;
        r9 = r1.addEntitiesToText(r9, r8);
        r10 = r1.eventId;
        r12 = (r10 > r6 ? 1 : (r10 == r6 ? 0 : -1));
        if (r12 != 0) goto L_0x012d;
    L_0x00e1:
        r10 = r36.isOutOwner();
        if (r10 != 0) goto L_0x012d;
    L_0x00e7:
        r10 = r1.messageOwner;
        r10 = r10.fwd_from;
        if (r10 == 0) goto L_0x0105;
    L_0x00ed:
        r10 = r1.messageOwner;
        r10 = r10.fwd_from;
        r10 = r10.saved_from_peer;
        if (r10 != 0) goto L_0x012b;
    L_0x00f5:
        r10 = r1.messageOwner;
        r10 = r10.fwd_from;
        r10 = r10.from_id;
        if (r10 != 0) goto L_0x012b;
    L_0x00fd:
        r10 = r1.messageOwner;
        r10 = r10.fwd_from;
        r10 = r10.channel_id;
        if (r10 != 0) goto L_0x012b;
    L_0x0105:
        r10 = r1.messageOwner;
        r10 = r10.from_id;
        if (r10 <= 0) goto L_0x012d;
    L_0x010b:
        r10 = r1.messageOwner;
        r10 = r10.to_id;
        r10 = r10.channel_id;
        if (r10 != 0) goto L_0x012b;
    L_0x0113:
        r10 = r1.messageOwner;
        r10 = r10.to_id;
        r10 = r10.chat_id;
        if (r10 != 0) goto L_0x012b;
    L_0x011b:
        r10 = r1.messageOwner;
        r10 = r10.media;
        r10 = r10 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaGame;
        if (r10 != 0) goto L_0x012b;
    L_0x0123:
        r10 = r1.messageOwner;
        r10 = r10.media;
        r10 = r10 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaInvoice;
        if (r10 == 0) goto L_0x012d;
    L_0x012b:
        r10 = r5;
        goto L_0x012e;
    L_0x012d:
        r10 = r3;
    L_0x012e:
        r11 = org.telegram.messenger.AndroidUtilities.isTablet();
        if (r11 == 0) goto L_0x0139;
    L_0x0134:
        r11 = org.telegram.messenger.AndroidUtilities.getMinTabletSide();
        goto L_0x013d;
    L_0x0139:
        r11 = org.telegram.messenger.AndroidUtilities.displaySize;
        r11 = r11.x;
    L_0x013d:
        r1.generatedWithMinSize = r11;
        r11 = r1.generatedWithMinSize;
        if (r10 != 0) goto L_0x014d;
    L_0x0143:
        r12 = r1.eventId;
        r14 = (r12 > r6 ? 1 : (r12 == r6 ? 0 : -1));
        if (r14 == 0) goto L_0x014a;
    L_0x0149:
        goto L_0x014d;
    L_0x014a:
        r6 = 1117782016; // 0x42a00000 float:80.0 double:5.522576936E-315;
        goto L_0x014f;
    L_0x014d:
        r6 = 1124335616; // 0x43040000 float:132.0 double:5.554956023E-315;
    L_0x014f:
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r11 = r11 - r6;
        if (r2 == 0) goto L_0x015a;
    L_0x0156:
        r6 = r2.bot;
        if (r6 != 0) goto L_0x0174;
    L_0x015a:
        r6 = r36.isMegagroup();
        if (r6 != 0) goto L_0x016e;
    L_0x0160:
        r6 = r1.messageOwner;
        r6 = r6.fwd_from;
        if (r6 == 0) goto L_0x017b;
    L_0x0166:
        r6 = r1.messageOwner;
        r6 = r6.fwd_from;
        r6 = r6.channel_id;
        if (r6 == 0) goto L_0x017b;
    L_0x016e:
        r6 = r36.isOut();
        if (r6 != 0) goto L_0x017b;
    L_0x0174:
        r6 = 1101004800; // 0x41a00000 float:20.0 double:5.439686476E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r11 = r11 - r6;
    L_0x017b:
        r6 = r1.messageOwner;
        r6 = r6.media;
        r6 = r6 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaGame;
        r7 = 1092616192; // 0x41200000 float:10.0 double:5.398241246E-315;
        if (r6 == 0) goto L_0x018a;
    L_0x0185:
        r6 = org.telegram.messenger.AndroidUtilities.dp(r7);
        r11 = r11 - r6;
    L_0x018a:
        r6 = r1.messageOwner;
        r6 = r6.media;
        r6 = r6 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaGame;
        if (r6 == 0) goto L_0x0195;
    L_0x0192:
        r6 = org.telegram.ui.ActionBar.Theme.chat_msgGameTextPaint;
        goto L_0x0197;
    L_0x0195:
        r6 = org.telegram.ui.ActionBar.Theme.chat_msgTextPaint;
    L_0x0197:
        r12 = android.os.Build.VERSION.SDK_INT;	 Catch:{ Exception -> 0x0516 }
        r15 = 24;
        if (r12 < r15) goto L_0x01cb;
    L_0x019d:
        r12 = r1.messageText;	 Catch:{ Exception -> 0x01bd }
        r13 = r1.messageText;	 Catch:{ Exception -> 0x01bd }
        r13 = r13.length();	 Catch:{ Exception -> 0x01bd }
        r12 = android.text.StaticLayout.Builder.obtain(r12, r3, r13, r6, r11);	 Catch:{ Exception -> 0x01bd }
        r12 = r12.setBreakStrategy(r5);	 Catch:{ Exception -> 0x01bd }
        r12 = r12.setHyphenationFrequency(r3);	 Catch:{ Exception -> 0x01bd }
        r13 = android.text.Layout.Alignment.ALIGN_NORMAL;	 Catch:{ Exception -> 0x01bd }
        r12 = r12.setAlignment(r13);	 Catch:{ Exception -> 0x01bd }
        r12 = r12.build();	 Catch:{ Exception -> 0x01bd }
        r3 = r15;
        goto L_0x01e1;
    L_0x01bd:
        r0 = move-exception;
        r2 = r0;
        r23 = r4;
        r29 = r6;
        r24 = r8;
        r25 = r9;
        r26 = r10;
        goto L_0x0522;
    L_0x01cb:
        r20 = new android.text.StaticLayout;	 Catch:{ Exception -> 0x0516 }
        r13 = r1.messageText;	 Catch:{ Exception -> 0x0516 }
        r16 = android.text.Layout.Alignment.ALIGN_NORMAL;	 Catch:{ Exception -> 0x0516 }
        r17 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;	 Catch:{ Exception -> 0x0516 }
        r18 = 0;	 Catch:{ Exception -> 0x0516 }
        r19 = 0;	 Catch:{ Exception -> 0x0516 }
        r12 = r20;	 Catch:{ Exception -> 0x0516 }
        r14 = r6;	 Catch:{ Exception -> 0x0516 }
        r3 = r15;	 Catch:{ Exception -> 0x0516 }
        r15 = r11;	 Catch:{ Exception -> 0x0516 }
        r12.<init>(r13, r14, r15, r16, r17, r18, r19);	 Catch:{ Exception -> 0x0516 }
        r12 = r20;
    L_0x01e1:
        r15 = r12;
        r12 = r15.getHeight();
        r1.textHeight = r12;
        r12 = r15.getLineCount();
        r1.linesCount = r12;
        r12 = android.os.Build.VERSION.SDK_INT;
        if (r12 < r3) goto L_0x01f6;
    L_0x01f4:
        r7 = 1;
        goto L_0x0200;
    L_0x01f6:
        r12 = r1.linesCount;
        r12 = (float) r12;
        r12 = r12 / r7;
        r12 = (double) r12;
        r12 = java.lang.Math.ceil(r12);
        r7 = (int) r12;
    L_0x0200:
        r12 = 0;
        r13 = 0;
        r14 = r12;
        r22 = r13;
        r12 = 0;
        r13 = r12;
        if (r13 >= r7) goto L_0x0507;
    L_0x0209:
        r12 = android.os.Build.VERSION.SDK_INT;
        if (r12 < r3) goto L_0x0210;
    L_0x020d:
        r12 = r1.linesCount;
        goto L_0x0219;
    L_0x0210:
        r12 = 10;
        r3 = r1.linesCount;
        r3 = r3 - r14;
        r12 = java.lang.Math.min(r12, r3);
    L_0x0219:
        r3 = r12;
        r12 = new org.telegram.messenger.MessageObject$TextLayoutBlock;
        r12.<init>();
        r2 = 0;
        if (r7 != r5) goto L_0x023b;
    L_0x0222:
        r12.textLayout = r15;
        r12.textYOffset = r2;
        r2 = 0;
        r12.charactersOffset = r2;
        r2 = r1.textHeight;
        r12.height = r2;
        r23 = r4;
        r24 = r8;
        r25 = r9;
        r26 = r10;
        r9 = r12;
        r10 = r13;
        r8 = r14;
        r2 = r15;
        goto L_0x0351;
    L_0x023b:
        r2 = r15.getLineStart(r14);
        r16 = r14 + r3;
        r23 = r4;
        r4 = r16 + -1;
        r4 = r15.getLineEnd(r4);
        if (r4 >= r2) goto L_0x025c;
    L_0x024c:
        r29 = r6;
        r24 = r8;
        r25 = r9;
        r26 = r10;
        r10 = r13;
        r32 = r14;
        r28 = r15;
        r8 = r5;
        goto L_0x04f0;
    L_0x025c:
        r12.charactersOffset = r2;
        r12.charactersEnd = r4;
        if (r9 == 0) goto L_0x02d1;
    L_0x0262:
        r5 = android.os.Build.VERSION.SDK_INT;	 Catch:{ Exception -> 0x02bc }
        r24 = r8;
        r8 = 24;
        if (r5 < r8) goto L_0x02d3;
    L_0x026a:
        r5 = r1.messageText;	 Catch:{ Exception -> 0x02a9 }
        r8 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;	 Catch:{ Exception -> 0x02a9 }
        r8 = org.telegram.messenger.AndroidUtilities.dp(r8);	 Catch:{ Exception -> 0x02a9 }
        r8 = r8 + r11;	 Catch:{ Exception -> 0x02a9 }
        r5 = android.text.StaticLayout.Builder.obtain(r5, r2, r4, r6, r8);	 Catch:{ Exception -> 0x02a9 }
        r8 = 1;
        r5 = r5.setBreakStrategy(r8);	 Catch:{ Exception -> 0x0299 }
        r8 = 0;
        r5 = r5.setHyphenationFrequency(r8);	 Catch:{ Exception -> 0x02a9 }
        r8 = android.text.Layout.Alignment.ALIGN_NORMAL;	 Catch:{ Exception -> 0x02a9 }
        r5 = r5.setAlignment(r8);	 Catch:{ Exception -> 0x02a9 }
        r5 = r5.build();	 Catch:{ Exception -> 0x02a9 }
        r12.textLayout = r5;	 Catch:{ Exception -> 0x02a9 }
        r27 = r2;
        r25 = r9;
        r26 = r10;
        r9 = r12;
        r10 = r13;
        r8 = r14;
        r2 = r15;
        goto L_0x02f6;
    L_0x0299:
        r0 = move-exception;
        r27 = r2;
        r29 = r6;
        r25 = r9;
        r26 = r10;
        r9 = r12;
        r10 = r13;
        r32 = r14;
        r28 = r15;
        goto L_0x02b9;
    L_0x02a9:
        r0 = move-exception;
        r27 = r2;
        r29 = r6;
        r25 = r9;
        r26 = r10;
        r9 = r12;
        r10 = r13;
        r32 = r14;
        r28 = r15;
    L_0x02b8:
        r8 = 1;
    L_0x02b9:
        r2 = r0;
        goto L_0x04ec;
    L_0x02bc:
        r0 = move-exception;
        r24 = r8;
        r27 = r2;
        r29 = r6;
        r25 = r9;
        r26 = r10;
        r9 = r12;
        r10 = r13;
        r32 = r14;
        r28 = r15;
        r8 = 1;
        r2 = r0;
        goto L_0x04ec;
    L_0x02d1:
        r24 = r8;
    L_0x02d3:
        r5 = new android.text.StaticLayout;	 Catch:{ Exception -> 0x04db }
        r8 = r1.messageText;	 Catch:{ Exception -> 0x04db }
        r18 = android.text.Layout.Alignment.ALIGN_NORMAL;	 Catch:{ Exception -> 0x04db }
        r19 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r20 = 0;
        r21 = 0;
        r25 = r9;
        r9 = r12;
        r12 = r5;
        r26 = r10;
        r10 = r13;
        r13 = r8;
        r8 = r14;
        r14 = r2;
        r27 = r2;
        r2 = r15;
        r15 = r4;
        r16 = r6;
        r17 = r11;
        r12.<init>(r13, r14, r15, r16, r17, r18, r19, r20, r21);	 Catch:{ Exception -> 0x04d1 }
        r9.textLayout = r5;	 Catch:{ Exception -> 0x04d1 }
    L_0x02f6:
        r5 = r2.getLineTop(r8);	 Catch:{ Exception -> 0x04d1 }
        r5 = (float) r5;	 Catch:{ Exception -> 0x04d1 }
        r9.textYOffset = r5;	 Catch:{ Exception -> 0x04d1 }
        if (r10 == 0) goto L_0x030f;
    L_0x02ff:
        r5 = r9.textYOffset;	 Catch:{ Exception -> 0x0307 }
        r5 = r5 - r22;	 Catch:{ Exception -> 0x0307 }
        r5 = (int) r5;	 Catch:{ Exception -> 0x0307 }
        r9.height = r5;	 Catch:{ Exception -> 0x0307 }
        goto L_0x030f;
    L_0x0307:
        r0 = move-exception;
        r28 = r2;
        r29 = r6;
        r32 = r8;
        goto L_0x02b8;
    L_0x030f:
        r5 = r9.height;	 Catch:{ Exception -> 0x04d1 }
        r12 = r9.textLayout;	 Catch:{ Exception -> 0x04d1 }
        r13 = r9.textLayout;	 Catch:{ Exception -> 0x04d1 }
        r13 = r13.getLineCount();	 Catch:{ Exception -> 0x04d1 }
        r14 = 1;	 Catch:{ Exception -> 0x04d1 }
        r13 = r13 - r14;	 Catch:{ Exception -> 0x04d1 }
        r12 = r12.getLineBottom(r13);	 Catch:{ Exception -> 0x04d1 }
        r5 = java.lang.Math.max(r5, r12);	 Catch:{ Exception -> 0x04d1 }
        r9.height = r5;	 Catch:{ Exception -> 0x04d1 }
        r5 = r9.textYOffset;	 Catch:{ Exception -> 0x04d1 }
        r22 = r5;
        r5 = r7 + -1;
        if (r10 != r5) goto L_0x0351;
    L_0x032e:
        r5 = r9.textLayout;
        r5 = r5.getLineCount();
        r3 = java.lang.Math.max(r3, r5);
        r5 = r1.textHeight;	 Catch:{ Exception -> 0x034c }
        r12 = r9.textYOffset;	 Catch:{ Exception -> 0x034c }
        r13 = r9.textLayout;	 Catch:{ Exception -> 0x034c }
        r13 = r13.getHeight();	 Catch:{ Exception -> 0x034c }
        r13 = (float) r13;	 Catch:{ Exception -> 0x034c }
        r12 = r12 + r13;	 Catch:{ Exception -> 0x034c }
        r12 = (int) r12;	 Catch:{ Exception -> 0x034c }
        r5 = java.lang.Math.max(r5, r12);	 Catch:{ Exception -> 0x034c }
        r1.textHeight = r5;	 Catch:{ Exception -> 0x034c }
        goto L_0x0351;
    L_0x034c:
        r0 = move-exception;
        r5 = r0;
        org.telegram.messenger.FileLog.e(r5);
    L_0x0351:
        r4 = r1.textLayoutBlocks;
        r4.add(r9);
        r4 = r9.textLayout;	 Catch:{ Exception -> 0x0368 }
        r5 = r3 + -1;	 Catch:{ Exception -> 0x0368 }
        r4 = r4.getLineLeft(r5);	 Catch:{ Exception -> 0x0368 }
        if (r10 != 0) goto L_0x0367;	 Catch:{ Exception -> 0x0368 }
    L_0x0360:
        r5 = 0;	 Catch:{ Exception -> 0x0368 }
        r12 = (r4 > r5 ? 1 : (r4 == r5 ? 0 : -1));	 Catch:{ Exception -> 0x0368 }
        if (r12 < 0) goto L_0x0367;	 Catch:{ Exception -> 0x0368 }
    L_0x0365:
        r1.textXOffset = r4;	 Catch:{ Exception -> 0x0368 }
    L_0x0367:
        goto L_0x0374;
    L_0x0368:
        r0 = move-exception;
        r4 = r0;
        r5 = 0;
        if (r10 != 0) goto L_0x0370;
    L_0x036d:
        r12 = 0;
        r1.textXOffset = r12;
    L_0x0370:
        org.telegram.messenger.FileLog.e(r4);
        r4 = r5;
    L_0x0374:
        r5 = r9.textLayout;	 Catch:{ Exception -> 0x037f }
        r12 = r3 + -1;	 Catch:{ Exception -> 0x037f }
        r5 = r5.getLineWidth(r12);	 Catch:{ Exception -> 0x037f }
        r12 = r5;
        goto L_0x0385;
    L_0x037f:
        r0 = move-exception;
        r5 = r0;
        r12 = 0;
        org.telegram.messenger.FileLog.e(r5);
    L_0x0385:
        r13 = (double) r12;
        r13 = java.lang.Math.ceil(r13);
        r5 = (int) r13;
        r13 = r7 + -1;
        if (r10 != r13) goto L_0x0391;
    L_0x038f:
        r1.lastLineWidth = r5;
    L_0x0391:
        r13 = r12 + r4;
        r13 = (double) r13;
        r13 = java.lang.Math.ceil(r13);
        r13 = (int) r13;
        r14 = r13;
        r15 = 1;
        if (r3 <= r15) goto L_0x047e;
    L_0x039d:
        r15 = 0;
        r16 = 0;
        r17 = 0;
        r28 = r2;
        r29 = r6;
        r30 = r12;
        r12 = r13;
        r2 = r16;
        r13 = r17;
        r6 = r5;
        r5 = 0;
        if (r5 >= r3) goto L_0x044a;
    L_0x03b1:
        r31 = r3;
        r3 = r9.textLayout;	 Catch:{ Exception -> 0x03ba }
        r3 = r3.getLineWidth(r5);	 Catch:{ Exception -> 0x03ba }
        goto L_0x03c1;
    L_0x03ba:
        r0 = move-exception;
        r3 = r0;
        org.telegram.messenger.FileLog.e(r3);
        r3 = 0;
    L_0x03c1:
        r32 = r8;
        r8 = r11 + 20;
        r8 = (float) r8;
        r8 = (r3 > r8 ? 1 : (r3 == r8 ? 0 : -1));
        if (r8 <= 0) goto L_0x03cb;
    L_0x03ca:
        r3 = (float) r11;
    L_0x03cb:
        r8 = r9.textLayout;	 Catch:{ Exception -> 0x03d2 }
        r8 = r8.getLineLeft(r5);	 Catch:{ Exception -> 0x03d2 }
        goto L_0x03d9;
    L_0x03d2:
        r0 = move-exception;
        r8 = r0;
        org.telegram.messenger.FileLog.e(r8);
        r8 = 0;
    L_0x03d9:
        r16 = 0;
        r17 = (r8 > r16 ? 1 : (r8 == r16 ? 0 : -1));
        if (r17 <= 0) goto L_0x03f5;
    L_0x03df:
        r33 = r11;
        r11 = r1.textXOffset;
        r11 = java.lang.Math.min(r11, r8);
        r1.textXOffset = r11;
        r11 = r9.directionFlags;
        r34 = r4;
        r4 = 1;
        r11 = r11 | r4;
        r11 = (byte) r11;
        r9.directionFlags = r11;
        r1.hasRtl = r4;
        goto L_0x0400;
    L_0x03f5:
        r34 = r4;
        r33 = r11;
        r4 = r9.directionFlags;
        r4 = r4 | 2;
        r4 = (byte) r4;
        r9.directionFlags = r4;
    L_0x0400:
        if (r15 != 0) goto L_0x0417;
    L_0x0402:
        r4 = 0;
        r11 = (r8 > r4 ? 1 : (r8 == r4 ? 0 : -1));
        if (r11 != 0) goto L_0x0417;
        r4 = r9.textLayout;	 Catch:{ Exception -> 0x0412 }
        r4 = r4.getParagraphDirection(r5);	 Catch:{ Exception -> 0x0412 }
        r11 = 1;
        if (r4 != r11) goto L_0x0417;
        r15 = 1;
        goto L_0x0417;
    L_0x0412:
        r0 = move-exception;
        r4 = r0;
        r4 = 1;
        r15 = r4;
        goto L_0x0418;
        r2 = java.lang.Math.max(r2, r3);
        r4 = r3 + r8;
        r13 = java.lang.Math.max(r13, r4);
        r35 = r2;
        r1 = (double) r3;
        r1 = java.lang.Math.ceil(r1);
        r1 = (int) r1;
        r6 = java.lang.Math.max(r6, r1);
        r1 = r3 + r8;
        r1 = (double) r1;
        r1 = java.lang.Math.ceil(r1);
        r1 = (int) r1;
        r12 = java.lang.Math.max(r12, r1);
        r5 = r5 + 1;
        r3 = r31;
        r8 = r32;
        r11 = r33;
        r4 = r34;
        r2 = r35;
        r1 = r36;
        goto L_0x03af;
    L_0x044a:
        r31 = r3;
        r34 = r4;
        r32 = r8;
        r33 = r11;
        if (r15 == 0) goto L_0x0461;
        r2 = r13;
        r1 = r7 + -1;
        if (r10 != r1) goto L_0x045e;
        r1 = r36;
        r1.lastLineWidth = r14;
        goto L_0x0469;
        r1 = r36;
        goto L_0x0469;
        r1 = r36;
        r3 = r7 + -1;
        if (r10 != r3) goto L_0x0469;
        r1.lastLineWidth = r6;
        r3 = r1.textWidth;
        r4 = (double) r2;
        r4 = java.lang.Math.ceil(r4);
        r4 = (int) r4;
        r3 = java.lang.Math.max(r3, r4);
        r1.textWidth = r3;
        r11 = r33;
        r4 = r34;
        r8 = 1;
        goto L_0x04ce;
    L_0x047e:
        r28 = r2;
        r31 = r3;
        r34 = r4;
        r29 = r6;
        r32 = r8;
        r33 = r11;
        r30 = r12;
        r2 = 0;
        r3 = (r34 > r2 ? 1 : (r34 == r2 ? 0 : -1));
        if (r3 <= 0) goto L_0x04b4;
        r3 = r1.textXOffset;
        r4 = r34;
        r3 = java.lang.Math.min(r3, r4);
        r1.textXOffset = r3;
        r3 = r1.textXOffset;
        r2 = (r3 > r2 ? 1 : (r3 == r2 ? 0 : -1));
        if (r2 != 0) goto L_0x04a5;
        r2 = (float) r5;
        r2 = r2 + r4;
        r2 = (int) r2;
        r5 = r2;
        r8 = 1;
        if (r7 == r8) goto L_0x04aa;
        r2 = r8;
        goto L_0x04ab;
        r2 = 0;
        r1.hasRtl = r2;
        r2 = r9.directionFlags;
        r2 = r2 | r8;
        r2 = (byte) r2;
        r9.directionFlags = r2;
        goto L_0x04be;
        r4 = r34;
        r8 = 1;
        r2 = r9.directionFlags;
        r2 = r2 | 2;
        r2 = (byte) r2;
        r9.directionFlags = r2;
        r6 = r5;
        r2 = r1.textWidth;
        r11 = r33;
        r3 = java.lang.Math.min(r11, r6);
        r2 = java.lang.Math.max(r2, r3);
        r1.textWidth = r2;
        r12 = r13;
        r14 = r32 + r31;
        goto L_0x04f2;
    L_0x04d1:
        r0 = move-exception;
        r28 = r2;
        r29 = r6;
        r32 = r8;
        r8 = 1;
        r2 = r0;
        goto L_0x04ec;
    L_0x04db:
        r0 = move-exception;
        r27 = r2;
        r29 = r6;
        r25 = r9;
        r26 = r10;
        r9 = r12;
        r10 = r13;
        r32 = r14;
        r28 = r15;
        r8 = 1;
        r2 = r0;
    L_0x04ec:
        org.telegram.messenger.FileLog.e(r2);
    L_0x04f0:
        r14 = r32;
        r12 = r10 + 1;
        r5 = r8;
        r4 = r23;
        r8 = r24;
        r9 = r25;
        r10 = r26;
        r15 = r28;
        r6 = r29;
        r2 = r37;
        r3 = 24;
        goto L_0x0206;
    L_0x0507:
        r23 = r4;
        r29 = r6;
        r24 = r8;
        r25 = r9;
        r26 = r10;
        r32 = r14;
        r28 = r15;
        return;
    L_0x0516:
        r0 = move-exception;
        r23 = r4;
        r29 = r6;
        r24 = r8;
        r25 = r9;
        r26 = r10;
        r2 = r0;
    L_0x0522:
        org.telegram.messenger.FileLog.e(r2);
        return;
    L_0x0526:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.MessageObject.generateLayout(org.telegram.tgnet.TLRPC$User):void");
    }

    public MessageObject(int accountNum, Message message, String formattedMessage, String name, String userName, boolean localMessage, boolean isChannel) {
        this.type = 1000;
        this.localType = localMessage ? 2 : 1;
        this.currentAccount = accountNum;
        this.localName = name;
        this.localUserName = userName;
        this.messageText = formattedMessage;
        this.messageOwner = message;
        this.localChannel = isChannel;
    }

    public MessageObject(int accountNum, Message message, AbstractMap<Integer, User> users, boolean generateLayout) {
        this(accountNum, message, (AbstractMap) users, null, generateLayout);
    }

    public MessageObject(int accountNum, Message message, SparseArray<User> users, boolean generateLayout) {
        this(accountNum, message, (SparseArray) users, null, generateLayout);
    }

    public MessageObject(int accountNum, Message message, boolean generateLayout) {
        this(accountNum, message, null, null, null, null, generateLayout, 0);
    }

    public MessageObject(int accountNum, Message message, AbstractMap<Integer, User> users, AbstractMap<Integer, Chat> chats, boolean generateLayout) {
        this(accountNum, message, (AbstractMap) users, (AbstractMap) chats, generateLayout, 0);
    }

    public MessageObject(int accountNum, Message message, SparseArray<User> users, SparseArray<Chat> chats, boolean generateLayout) {
        this(accountNum, message, null, null, users, chats, generateLayout, 0);
    }

    public MessageObject(int accountNum, Message message, AbstractMap<Integer, User> users, AbstractMap<Integer, Chat> chats, boolean generateLayout, long eid) {
        this(accountNum, message, users, chats, null, null, generateLayout, eid);
    }

    private void createDateArray(int accountNum, TL_channelAdminLogEvent event, ArrayList<MessageObject> messageObjects, HashMap<String, ArrayList<MessageObject>> messagesByDays) {
        if (((ArrayList) messagesByDays.get(this.dateKey)) == null) {
            messagesByDays.put(this.dateKey, new ArrayList());
            TL_message dateMsg = new TL_message();
            dateMsg.message = LocaleController.formatDateChat((long) event.date);
            dateMsg.id = 0;
            dateMsg.date = event.date;
            MessageObject dateObj = new MessageObject(accountNum, dateMsg, false);
            dateObj.type = 10;
            dateObj.contentType = 1;
            dateObj.isDateObject = true;
            messageObjects.add(dateObj);
        }
    }

    private String getUserName(User user, ArrayList<MessageEntity> entities, int offset) {
        String name;
        if (user == null) {
            name = TtmlNode.ANONYMOUS_REGION_ID;
        } else {
            name = ContactsController.formatName(user.first_name, user.last_name);
        }
        if (offset >= 0) {
            TL_messageEntityMentionName entity = new TL_messageEntityMentionName();
            entity.user_id = user.id;
            entity.offset = offset;
            entity.length = name.length();
            entities.add(entity);
        }
        if (TextUtils.isEmpty(user.username)) {
            return name;
        }
        if (offset >= 0) {
            TL_messageEntityMentionName entity2 = new TL_messageEntityMentionName();
            entity2.user_id = user.id;
            entity2.offset = (name.length() + offset) + 2;
            entity2.length = user.username.length() + 1;
            entities.add(entity2);
        }
        return String.format("%1$s (@%2$s)", new Object[]{name, user.username});
    }

    public void applyNewText() {
        if (!TextUtils.isEmpty(this.messageOwner.message)) {
            TextPaint paint;
            User fromUser = null;
            if (isFromUser()) {
                fromUser = MessagesController.getInstance(this.currentAccount).getUser(Integer.valueOf(this.messageOwner.from_id));
            }
            this.messageText = this.messageOwner.message;
            if (this.messageOwner.media instanceof TL_messageMediaGame) {
                paint = Theme.chat_msgGameTextPaint;
            } else {
                paint = Theme.chat_msgTextPaint;
            }
            this.messageText = Emoji.replaceEmoji(this.messageText, paint.getFontMetricsInt(), AndroidUtilities.dp(20.0f), false);
            generateLayout(fromUser);
        }
    }

    public void generateGameMessageText(User fromUser) {
        if (fromUser == null && this.messageOwner.from_id > 0) {
            fromUser = MessagesController.getInstance(this.currentAccount).getUser(Integer.valueOf(this.messageOwner.from_id));
        }
        TL_game game = null;
        if (!(this.replyMessageObject == null || this.replyMessageObject.messageOwner.media == null || this.replyMessageObject.messageOwner.media.game == null)) {
            game = this.replyMessageObject.messageOwner.media.game;
        }
        if (game != null) {
            if (fromUser == null || fromUser.id != UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                this.messageText = replaceWithLink(LocaleController.formatString("ActionUserScoredInGame", R.string.ActionUserScoredInGame, LocaleController.formatPluralString("Points", this.messageOwner.action.score)), "un1", fromUser);
            } else {
                this.messageText = LocaleController.formatString("ActionYouScoredInGame", R.string.ActionYouScoredInGame, LocaleController.formatPluralString("Points", this.messageOwner.action.score));
            }
            this.messageText = replaceWithLink(this.messageText, "un2", game);
        } else if (fromUser == null || fromUser.id != UserConfig.getInstance(this.currentAccount).getClientUserId()) {
            this.messageText = replaceWithLink(LocaleController.formatString("ActionUserScored", R.string.ActionUserScored, LocaleController.formatPluralString("Points", this.messageOwner.action.score)), "un1", fromUser);
        } else {
            this.messageText = LocaleController.formatString("ActionYouScored", R.string.ActionYouScored, LocaleController.formatPluralString("Points", this.messageOwner.action.score));
        }
    }

    public boolean hasValidReplyMessageObject() {
        return (this.replyMessageObject == null || (this.replyMessageObject.messageOwner instanceof TL_messageEmpty) || (this.replyMessageObject.messageOwner.action instanceof TL_messageActionHistoryClear)) ? false : true;
    }

    public void generatePaymentSentMessageText(User fromUser) {
        String name;
        if (fromUser == null) {
            fromUser = MessagesController.getInstance(this.currentAccount).getUser(Integer.valueOf((int) getDialogId()));
        }
        if (fromUser != null) {
            name = UserObject.getFirstName(fromUser);
        } else {
            name = TtmlNode.ANONYMOUS_REGION_ID;
        }
        if (this.replyMessageObject == null || !(this.replyMessageObject.messageOwner.media instanceof TL_messageMediaInvoice)) {
            this.messageText = LocaleController.formatString("PaymentSuccessfullyPaidNoItem", R.string.PaymentSuccessfullyPaidNoItem, LocaleController.getInstance().formatCurrencyString(this.messageOwner.action.total_amount, this.messageOwner.action.currency), name);
            return;
        }
        this.messageText = LocaleController.formatString("PaymentSuccessfullyPaid", R.string.PaymentSuccessfullyPaid, LocaleController.getInstance().formatCurrencyString(this.messageOwner.action.total_amount, this.messageOwner.action.currency), name, this.replyMessageObject.messageOwner.media.title);
    }

    public void generatePinMessageText(User fromUser, Chat chat) {
        TLObject fromUser2;
        TLObject chat2;
        if (fromUser == null && chat == null) {
            if (this.messageOwner.from_id > 0) {
                fromUser2 = MessagesController.getInstance(this.currentAccount).getUser(Integer.valueOf(this.messageOwner.from_id));
            }
            if (fromUser2 == null) {
                chat2 = MessagesController.getInstance(this.currentAccount).getChat(Integer.valueOf(this.messageOwner.to_id.channel_id));
            }
        }
        if (!(this.replyMessageObject == null || (this.replyMessageObject.messageOwner instanceof TL_messageEmpty))) {
            if (!(this.replyMessageObject.messageOwner.action instanceof TL_messageActionHistoryClear)) {
                if (this.replyMessageObject.isMusic()) {
                    this.messageText = replaceWithLink(LocaleController.getString("ActionPinnedMusic", R.string.ActionPinnedMusic), "un1", fromUser2 != null ? fromUser2 : chat2);
                    return;
                } else if (this.replyMessageObject.isVideo()) {
                    this.messageText = replaceWithLink(LocaleController.getString("ActionPinnedVideo", R.string.ActionPinnedVideo), "un1", fromUser2 != null ? fromUser2 : chat2);
                    return;
                } else if (this.replyMessageObject.isGif()) {
                    this.messageText = replaceWithLink(LocaleController.getString("ActionPinnedGif", R.string.ActionPinnedGif), "un1", fromUser2 != null ? fromUser2 : chat2);
                    return;
                } else if (this.replyMessageObject.isVoice()) {
                    this.messageText = replaceWithLink(LocaleController.getString("ActionPinnedVoice", R.string.ActionPinnedVoice), "un1", fromUser2 != null ? fromUser2 : chat2);
                    return;
                } else if (this.replyMessageObject.isRoundVideo()) {
                    this.messageText = replaceWithLink(LocaleController.getString("ActionPinnedRound", R.string.ActionPinnedRound), "un1", fromUser2 != null ? fromUser2 : chat2);
                    return;
                } else if (this.replyMessageObject.isSticker()) {
                    this.messageText = replaceWithLink(LocaleController.getString("ActionPinnedSticker", R.string.ActionPinnedSticker), "un1", fromUser2 != null ? fromUser2 : chat2);
                    return;
                } else if (this.replyMessageObject.messageOwner.media instanceof TL_messageMediaDocument) {
                    this.messageText = replaceWithLink(LocaleController.getString("ActionPinnedFile", R.string.ActionPinnedFile), "un1", fromUser2 != null ? fromUser2 : chat2);
                    return;
                } else if (this.replyMessageObject.messageOwner.media instanceof TL_messageMediaGeo) {
                    this.messageText = replaceWithLink(LocaleController.getString("ActionPinnedGeo", R.string.ActionPinnedGeo), "un1", fromUser2 != null ? fromUser2 : chat2);
                    return;
                } else if (this.replyMessageObject.messageOwner.media instanceof TL_messageMediaGeoLive) {
                    this.messageText = replaceWithLink(LocaleController.getString("ActionPinnedGeoLive", R.string.ActionPinnedGeoLive), "un1", fromUser2 != null ? fromUser2 : chat2);
                    return;
                } else if (this.replyMessageObject.messageOwner.media instanceof TL_messageMediaContact) {
                    this.messageText = replaceWithLink(LocaleController.getString("ActionPinnedContact", R.string.ActionPinnedContact), "un1", fromUser2 != null ? fromUser2 : chat2);
                    return;
                } else if (this.replyMessageObject.messageOwner.media instanceof TL_messageMediaPhoto) {
                    this.messageText = replaceWithLink(LocaleController.getString("ActionPinnedPhoto", R.string.ActionPinnedPhoto), "un1", fromUser2 != null ? fromUser2 : chat2);
                    return;
                } else if (this.replyMessageObject.messageOwner.media instanceof TL_messageMediaGame) {
                    Object[] objArr = new Object[1];
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("🎮 ");
                    stringBuilder.append(this.replyMessageObject.messageOwner.media.game.title);
                    objArr[0] = stringBuilder.toString();
                    this.messageText = replaceWithLink(LocaleController.formatString("ActionPinnedGame", R.string.ActionPinnedGame, objArr), "un1", fromUser2 != null ? fromUser2 : chat2);
                    this.messageText = Emoji.replaceEmoji(this.messageText, Theme.chat_msgTextPaint.getFontMetricsInt(), AndroidUtilities.dp(20.0f), false);
                    return;
                } else if (this.replyMessageObject.messageText == null || this.replyMessageObject.messageText.length() <= 0) {
                    this.messageText = replaceWithLink(LocaleController.getString("ActionPinnedNoText", R.string.ActionPinnedNoText), "un1", fromUser2 != null ? fromUser2 : chat2);
                    return;
                } else {
                    CharSequence mess = this.replyMessageObject.messageText;
                    if (mess.length() > 20) {
                        StringBuilder stringBuilder2 = new StringBuilder();
                        stringBuilder2.append(mess.subSequence(0, 20));
                        stringBuilder2.append("...");
                        mess = stringBuilder2.toString();
                    }
                    mess = Emoji.replaceEmoji(mess, Theme.chat_msgTextPaint.getFontMetricsInt(), AndroidUtilities.dp(20.0f), false);
                    this.messageText = replaceWithLink(LocaleController.formatString("ActionPinnedText", R.string.ActionPinnedText, mess), "un1", fromUser2 != null ? fromUser2 : chat2);
                    return;
                }
            }
        }
        this.messageText = replaceWithLink(LocaleController.getString("ActionPinnedNoText", R.string.ActionPinnedNoText), "un1", fromUser2 != null ? fromUser2 : chat2);
    }

    private Photo getPhotoWithId(WebPage webPage, long id) {
        if (webPage != null) {
            if (webPage.cached_page != null) {
                if (webPage.photo != null && webPage.photo.id == id) {
                    return webPage.photo;
                }
                for (int a = 0; a < webPage.cached_page.photos.size(); a++) {
                    Photo photo = (Photo) webPage.cached_page.photos.get(a);
                    if (photo.id == id) {
                        return photo;
                    }
                }
                return null;
            }
        }
        return null;
    }

    private Document getDocumentWithId(WebPage webPage, long id) {
        if (webPage != null) {
            if (webPage.cached_page != null) {
                if (webPage.document != null && webPage.document.id == id) {
                    return webPage.document;
                }
                for (int a = 0; a < webPage.cached_page.documents.size(); a++) {
                    Document document = (Document) webPage.cached_page.documents.get(a);
                    if (document.id == id) {
                        return document;
                    }
                }
                return null;
            }
        }
        return null;
    }

    private MessageObject getMessageObjectForBlock(WebPage webPage, PageBlock pageBlock) {
        TL_message message = null;
        if (pageBlock instanceof TL_pageBlockPhoto) {
            Photo photo = getPhotoWithId(webPage, pageBlock.photo_id);
            if (photo == webPage.photo) {
                return this;
            }
            message = new TL_message();
            message.media = new TL_messageMediaPhoto();
            message.media.photo = photo;
        } else if (pageBlock instanceof TL_pageBlockVideo) {
            if (getDocumentWithId(webPage, pageBlock.video_id) == webPage.document) {
                return this;
            }
            message = new TL_message();
            message.media = new TL_messageMediaDocument();
            message.media.document = getDocumentWithId(webPage, pageBlock.video_id);
        }
        message.message = TtmlNode.ANONYMOUS_REGION_ID;
        message.id = Utilities.random.nextInt();
        message.date = this.messageOwner.date;
        message.to_id = this.messageOwner.to_id;
        message.out = this.messageOwner.out;
        message.from_id = this.messageOwner.from_id;
        return new MessageObject(this.currentAccount, message, false);
    }

    public ArrayList<MessageObject> getWebPagePhotos(ArrayList<MessageObject> array, ArrayList<PageBlock> blocksToSearch) {
        WebPage webPage = this.messageOwner.media.webpage;
        ArrayList<MessageObject> messageObjects = array == null ? new ArrayList() : array;
        if (webPage.cached_page == null) {
            return messageObjects;
        }
        ArrayList<PageBlock> blocks = blocksToSearch == null ? webPage.cached_page.blocks : blocksToSearch;
        for (int a = 0; a < blocks.size(); a++) {
            PageBlock block = (PageBlock) blocks.get(a);
            int b;
            if (block instanceof TL_pageBlockSlideshow) {
                TL_pageBlockSlideshow slideshow = (TL_pageBlockSlideshow) block;
                for (b = 0; b < slideshow.items.size(); b++) {
                    messageObjects.add(getMessageObjectForBlock(webPage, (PageBlock) slideshow.items.get(b)));
                }
            } else if (block instanceof TL_pageBlockCollage) {
                TL_pageBlockCollage slideshow2 = (TL_pageBlockCollage) block;
                for (b = 0; b < slideshow2.items.size(); b++) {
                    messageObjects.add(getMessageObjectForBlock(webPage, (PageBlock) slideshow2.items.get(b)));
                }
            }
        }
        return messageObjects;
    }

    public void measureInlineBotButtons() {
        this.wantedBotKeyboardWidth = 0;
        if (this.messageOwner.reply_markup instanceof TL_replyInlineMarkup) {
            Theme.createChatResources(null, true);
            if (r0.botButtonsLayout == null) {
                r0.botButtonsLayout = new StringBuilder();
            } else {
                r0.botButtonsLayout.setLength(0);
            }
            for (int a = 0; a < r0.messageOwner.reply_markup.rows.size(); a++) {
                TL_keyboardButtonRow row = (TL_keyboardButtonRow) r0.messageOwner.reply_markup.rows.get(a);
                int size = row.buttons.size();
                int maxButtonSize = 0;
                for (int b = 0; b < size; b++) {
                    String replaceEmoji;
                    KeyboardButton button = (KeyboardButton) row.buttons.get(b);
                    StringBuilder stringBuilder = r0.botButtonsLayout;
                    stringBuilder.append(a);
                    stringBuilder.append(b);
                    if (!(button instanceof TL_keyboardButtonBuy) || (r0.messageOwner.media.flags & 4) == 0) {
                        replaceEmoji = Emoji.replaceEmoji(button.text, Theme.chat_msgBotButtonPaint.getFontMetricsInt(), AndroidUtilities.dp(15.0f), false);
                    } else {
                        replaceEmoji = LocaleController.getString("PaymentReceipt", R.string.PaymentReceipt);
                    }
                    StaticLayout staticLayout = new StaticLayout(replaceEmoji, Theme.chat_msgBotButtonPaint, AndroidUtilities.dp(2000.0f), Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                    if (staticLayout.getLineCount() > 0) {
                        float width = staticLayout.getLineWidth(0);
                        float left = staticLayout.getLineLeft(0);
                        if (left < width) {
                            width -= left;
                        }
                        maxButtonSize = Math.max(maxButtonSize, ((int) Math.ceil((double) width)) + AndroidUtilities.dp(4.0f));
                    }
                }
                r0.wantedBotKeyboardWidth = Math.max(r0.wantedBotKeyboardWidth, ((AndroidUtilities.dp(12.0f) + maxButtonSize) * size) + (AndroidUtilities.dp(5.0f) * (size - 1)));
            }
        }
    }

    public boolean isFcmMessage() {
        return this.localType != 0;
    }

    public void setType() {
        int oldType = this.type;
        this.isRoundVideoCached = 0;
        if (!(this.messageOwner instanceof TL_message)) {
            if (!(this.messageOwner instanceof TL_messageForwarded_old2)) {
                if (this.messageOwner instanceof TL_messageService) {
                    if (this.messageOwner.action instanceof TL_messageActionLoginUnknownLocation) {
                        this.type = 0;
                    } else {
                        if (!(this.messageOwner.action instanceof TL_messageActionChatEditPhoto)) {
                            if (!(this.messageOwner.action instanceof TL_messageActionUserUpdatedPhoto)) {
                                if (this.messageOwner.action instanceof TL_messageEncryptedAction) {
                                    if (!(this.messageOwner.action.encryptedAction instanceof TL_decryptedMessageActionScreenshotMessages)) {
                                        if (!(this.messageOwner.action.encryptedAction instanceof TL_decryptedMessageActionSetMessageTTL)) {
                                            this.contentType = -1;
                                            this.type = -1;
                                        }
                                    }
                                    this.contentType = 1;
                                    this.type = 10;
                                } else if (this.messageOwner.action instanceof TL_messageActionHistoryClear) {
                                    this.contentType = -1;
                                    this.type = -1;
                                } else if (this.messageOwner.action instanceof TL_messageActionPhoneCall) {
                                    this.type = 16;
                                } else {
                                    this.contentType = 1;
                                    this.type = 10;
                                }
                            }
                        }
                        this.contentType = 1;
                        this.type = 11;
                    }
                }
                if (oldType != 1000 && oldType != this.type) {
                    generateThumbs(false);
                    return;
                }
            }
        }
        if (isMediaEmpty()) {
            this.type = 0;
            if (TextUtils.isEmpty(this.messageText) && this.eventId == 0) {
                this.messageText = "Empty message";
            }
        } else if (this.messageOwner.media.ttl_seconds != 0 && ((this.messageOwner.media.photo instanceof TL_photoEmpty) || (this.messageOwner.media.document instanceof TL_documentEmpty))) {
            this.contentType = 1;
            this.type = 10;
        } else if (this.messageOwner.media instanceof TL_messageMediaPhoto) {
            this.type = 1;
        } else {
            if (!((this.messageOwner.media instanceof TL_messageMediaGeo) || (this.messageOwner.media instanceof TL_messageMediaVenue))) {
                if (!(this.messageOwner.media instanceof TL_messageMediaGeoLive)) {
                    if (isRoundVideo()) {
                        this.type = 5;
                    } else if (isVideo()) {
                        this.type = 3;
                    } else if (isVoice()) {
                        this.type = 2;
                    } else if (isMusic()) {
                        this.type = 14;
                    } else if (this.messageOwner.media instanceof TL_messageMediaContact) {
                        this.type = 12;
                    } else if (this.messageOwner.media instanceof TL_messageMediaUnsupported) {
                        this.type = 0;
                    } else if (this.messageOwner.media instanceof TL_messageMediaDocument) {
                        if (this.messageOwner.media.document == null || this.messageOwner.media.document.mime_type == null) {
                            this.type = 9;
                        } else if (isGifDocument(this.messageOwner.media.document)) {
                            this.type = 8;
                        } else if (this.messageOwner.media.document.mime_type.equals("image/webp") && isSticker()) {
                            this.type = 13;
                        } else {
                            this.type = 9;
                        }
                    } else if (this.messageOwner.media instanceof TL_messageMediaGame) {
                        this.type = 0;
                    } else if (this.messageOwner.media instanceof TL_messageMediaInvoice) {
                        this.type = 0;
                    }
                }
            }
            this.type = 4;
        }
        if (oldType != 1000) {
        }
    }

    public boolean checkLayout() {
        if (!(this.type != 0 || this.messageOwner.to_id == null || this.messageText == null)) {
            if (this.messageText.length() != 0) {
                if (this.layoutCreated) {
                    if (Math.abs(this.generatedWithMinSize - (AndroidUtilities.isTablet() ? AndroidUtilities.getMinTabletSide() : AndroidUtilities.displaySize.x)) > AndroidUtilities.dp(52.0f)) {
                        this.layoutCreated = false;
                    }
                }
                if (this.layoutCreated) {
                    return false;
                }
                TextPaint paint;
                this.layoutCreated = true;
                User fromUser = null;
                if (isFromUser()) {
                    fromUser = MessagesController.getInstance(this.currentAccount).getUser(Integer.valueOf(this.messageOwner.from_id));
                }
                if (this.messageOwner.media instanceof TL_messageMediaGame) {
                    paint = Theme.chat_msgGameTextPaint;
                } else {
                    paint = Theme.chat_msgTextPaint;
                }
                this.messageText = Emoji.replaceEmoji(this.messageText, paint.getFontMetricsInt(), AndroidUtilities.dp(20.0f), false);
                generateLayout(fromUser);
                return true;
            }
        }
        return false;
    }

    public String getMimeType() {
        if (this.messageOwner.media instanceof TL_messageMediaDocument) {
            return this.messageOwner.media.document.mime_type;
        }
        if (this.messageOwner.media instanceof TL_messageMediaInvoice) {
            WebDocument photo = ((TL_messageMediaInvoice) this.messageOwner.media).photo;
            if (photo != null) {
                return photo.mime_type;
            }
        } else if (this.messageOwner.media instanceof TL_messageMediaPhoto) {
            return "image/jpeg";
        } else {
            if (this.messageOwner.media instanceof TL_messageMediaWebPage) {
                if (this.messageOwner.media.webpage.document != null) {
                    return this.messageOwner.media.document.mime_type;
                }
                if (this.messageOwner.media.webpage.photo != null) {
                    return "image/jpeg";
                }
            }
        }
        return TtmlNode.ANONYMOUS_REGION_ID;
    }

    public static boolean isGifDocument(TL_webDocument document) {
        return document != null && (document.mime_type.equals("image/gif") || isNewGifDocument(document));
    }

    public static boolean isGifDocument(Document document) {
        return (document == null || document.thumb == null || document.mime_type == null || (!document.mime_type.equals("image/gif") && !isNewGifDocument(document))) ? false : true;
    }

    public static boolean isRoundVideoDocument(Document document) {
        if (!(document == null || document.mime_type == null || !document.mime_type.equals(MimeTypes.VIDEO_MP4))) {
            boolean round = false;
            int height = 0;
            int width = 0;
            for (int a = 0; a < document.attributes.size(); a++) {
                DocumentAttribute attribute = (DocumentAttribute) document.attributes.get(a);
                if (attribute instanceof TL_documentAttributeVideo) {
                    width = attribute.w;
                    height = attribute.w;
                    round = attribute.round_message;
                }
            }
            if (round && width <= 1280 && height <= 1280) {
                return true;
            }
        }
        return false;
    }

    public static boolean isNewGifDocument(TL_webDocument document) {
        if (!(document == null || document.mime_type == null || !document.mime_type.equals(MimeTypes.VIDEO_MP4))) {
            boolean animated = false;
            int height = 0;
            int width = 0;
            for (int a = 0; a < document.attributes.size(); a++) {
                DocumentAttribute attribute = (DocumentAttribute) document.attributes.get(a);
                if (!(attribute instanceof TL_documentAttributeAnimated)) {
                    if (attribute instanceof TL_documentAttributeVideo) {
                        width = attribute.w;
                        height = attribute.w;
                    }
                }
            }
            if (width <= 1280 && height <= 1280) {
                return true;
            }
        }
        return false;
    }

    public static boolean isNewGifDocument(Document document) {
        if (!(document == null || document.mime_type == null || !document.mime_type.equals(MimeTypes.VIDEO_MP4))) {
            boolean animated = false;
            int height = 0;
            int width = 0;
            for (int a = 0; a < document.attributes.size(); a++) {
                DocumentAttribute attribute = (DocumentAttribute) document.attributes.get(a);
                if (attribute instanceof TL_documentAttributeAnimated) {
                    animated = true;
                } else if (attribute instanceof TL_documentAttributeVideo) {
                    width = attribute.w;
                    height = attribute.w;
                }
            }
            if (animated && width <= 1280 && height <= 1280) {
                return true;
            }
        }
        return false;
    }

    public void generateThumbs(boolean update) {
        int a;
        PhotoSize photoObject;
        int b;
        PhotoSize size;
        if (this.messageOwner instanceof TL_messageService) {
            if (!(this.messageOwner.action instanceof TL_messageActionChatEditPhoto)) {
                return;
            }
            if (!update) {
                this.photoThumbs = new ArrayList(this.messageOwner.action.photo.sizes);
            } else if (this.photoThumbs != null && !this.photoThumbs.isEmpty()) {
                for (a = 0; a < this.photoThumbs.size(); a++) {
                    photoObject = (PhotoSize) this.photoThumbs.get(a);
                    for (b = 0; b < this.messageOwner.action.photo.sizes.size(); b++) {
                        size = (PhotoSize) this.messageOwner.action.photo.sizes.get(b);
                        if (!(size instanceof TL_photoSizeEmpty)) {
                            if (size.type.equals(photoObject.type)) {
                                photoObject.location = size.location;
                                break;
                            }
                        }
                    }
                }
            }
        } else if (this.messageOwner.media != null && !(this.messageOwner.media instanceof TL_messageMediaEmpty)) {
            if (this.messageOwner.media instanceof TL_messageMediaPhoto) {
                if (update) {
                    if (this.photoThumbs == null || this.photoThumbs.size() == this.messageOwner.media.photo.sizes.size()) {
                        if (this.photoThumbs != null && !this.photoThumbs.isEmpty()) {
                            for (a = 0; a < this.photoThumbs.size(); a++) {
                                photoObject = (PhotoSize) this.photoThumbs.get(a);
                                for (b = 0; b < this.messageOwner.media.photo.sizes.size(); b++) {
                                    size = (PhotoSize) this.messageOwner.media.photo.sizes.get(b);
                                    if (!(size instanceof TL_photoSizeEmpty)) {
                                        if (size.type.equals(photoObject.type)) {
                                            photoObject.location = size.location;
                                            break;
                                        }
                                    }
                                }
                            }
                            return;
                        }
                        return;
                    }
                }
                this.photoThumbs = new ArrayList(this.messageOwner.media.photo.sizes);
            } else if (this.messageOwner.media instanceof TL_messageMediaDocument) {
                if (!(this.messageOwner.media.document.thumb instanceof TL_photoSizeEmpty)) {
                    if (!update) {
                        this.photoThumbs = new ArrayList();
                        this.photoThumbs.add(this.messageOwner.media.document.thumb);
                    } else if (this.photoThumbs != null && !this.photoThumbs.isEmpty() && this.messageOwner.media.document.thumb != null) {
                        PhotoSize photoObject2 = (PhotoSize) this.photoThumbs.get(0);
                        photoObject2.location = this.messageOwner.media.document.thumb.location;
                        photoObject2.w = this.messageOwner.media.document.thumb.w;
                        photoObject2.h = this.messageOwner.media.document.thumb.h;
                    }
                }
            } else if (this.messageOwner.media instanceof TL_messageMediaGame) {
                if (!(this.messageOwner.media.game.document == null || (this.messageOwner.media.game.document.thumb instanceof TL_photoSizeEmpty))) {
                    if (!update) {
                        this.photoThumbs = new ArrayList();
                        this.photoThumbs.add(this.messageOwner.media.game.document.thumb);
                    } else if (!(this.photoThumbs == null || this.photoThumbs.isEmpty() || this.messageOwner.media.game.document.thumb == null)) {
                        ((PhotoSize) this.photoThumbs.get(0)).location = this.messageOwner.media.game.document.thumb.location;
                    }
                }
                if (this.messageOwner.media.game.photo != null) {
                    if (update) {
                        if (this.photoThumbs2 != null) {
                            if (!this.photoThumbs2.isEmpty()) {
                                for (a = 0; a < this.photoThumbs2.size(); a++) {
                                    photoObject = (PhotoSize) this.photoThumbs2.get(a);
                                    for (b = 0; b < this.messageOwner.media.game.photo.sizes.size(); b++) {
                                        size = (PhotoSize) this.messageOwner.media.game.photo.sizes.get(b);
                                        if (!(size instanceof TL_photoSizeEmpty)) {
                                            if (size.type.equals(photoObject.type)) {
                                                photoObject.location = size.location;
                                                break;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    this.photoThumbs2 = new ArrayList(this.messageOwner.media.game.photo.sizes);
                }
                if (this.photoThumbs == null && this.photoThumbs2 != null) {
                    this.photoThumbs = this.photoThumbs2;
                    this.photoThumbs2 = null;
                }
            } else if (!(this.messageOwner.media instanceof TL_messageMediaWebPage)) {
            } else {
                if (this.messageOwner.media.webpage.photo != null) {
                    if (update) {
                        if (this.photoThumbs != null) {
                            if (!this.photoThumbs.isEmpty()) {
                                for (a = 0; a < this.photoThumbs.size(); a++) {
                                    photoObject = (PhotoSize) this.photoThumbs.get(a);
                                    for (b = 0; b < this.messageOwner.media.webpage.photo.sizes.size(); b++) {
                                        size = (PhotoSize) this.messageOwner.media.webpage.photo.sizes.get(b);
                                        if (!(size instanceof TL_photoSizeEmpty)) {
                                            if (size.type.equals(photoObject.type)) {
                                                photoObject.location = size.location;
                                                break;
                                            }
                                        }
                                    }
                                }
                                return;
                            }
                            return;
                        }
                    }
                    this.photoThumbs = new ArrayList(this.messageOwner.media.webpage.photo.sizes);
                } else if (this.messageOwner.media.webpage.document != null && !(this.messageOwner.media.webpage.document.thumb instanceof TL_photoSizeEmpty)) {
                    if (!update) {
                        this.photoThumbs = new ArrayList();
                        this.photoThumbs.add(this.messageOwner.media.webpage.document.thumb);
                    } else if (this.photoThumbs != null && !this.photoThumbs.isEmpty() && this.messageOwner.media.webpage.document.thumb != null) {
                        ((PhotoSize) this.photoThumbs.get(0)).location = this.messageOwner.media.webpage.document.thumb.location;
                    }
                }
            }
        }
    }

    public CharSequence replaceWithLink(CharSequence source, String param, ArrayList<Integer> uids, AbstractMap<Integer, User> usersDict, SparseArray<User> sUsersDict) {
        int start = TextUtils.indexOf(source, param);
        if (start < 0) {
            return source;
        }
        SpannableStringBuilder names = new SpannableStringBuilder(TtmlNode.ANONYMOUS_REGION_ID);
        for (start = 0; start < uids.size(); start++) {
            User user = null;
            if (usersDict != null) {
                user = (User) usersDict.get(uids.get(start));
            } else if (sUsersDict != null) {
                user = (User) sUsersDict.get(((Integer) uids.get(start)).intValue());
            }
            if (user == null) {
                user = MessagesController.getInstance(this.currentAccount).getUser((Integer) uids.get(start));
            }
            if (user != null) {
                String name = UserObject.getUserName(user);
                int start2 = names.length();
                if (names.length() != 0) {
                    names.append(", ");
                }
                names.append(name);
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(TtmlNode.ANONYMOUS_REGION_ID);
                stringBuilder.append(user.id);
                names.setSpan(new URLSpanNoUnderlineBold(stringBuilder.toString()), start2, name.length() + start2, 33);
            }
        }
        return TextUtils.replace(source, new String[]{param}, new CharSequence[]{names});
    }

    public CharSequence replaceWithLink(CharSequence source, String param, TLObject object) {
        int start = TextUtils.indexOf(source, param);
        if (start < 0) {
            return source;
        }
        String name;
        String id;
        SpannableStringBuilder builder;
        StringBuilder stringBuilder;
        if (object instanceof User) {
            name = UserObject.getUserName((User) object);
            id = new StringBuilder();
            id.append(TtmlNode.ANONYMOUS_REGION_ID);
            id.append(((User) object).id);
            id = id.toString();
        } else if (object instanceof Chat) {
            name = ((Chat) object).title;
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append(TtmlNode.ANONYMOUS_REGION_ID);
            stringBuilder2.append(-((Chat) object).id);
            id = stringBuilder2.toString();
        } else {
            if (object instanceof TL_game) {
                String str = ((TL_game) object).title;
                id = "game";
                name = str;
            } else {
                name = TtmlNode.ANONYMOUS_REGION_ID;
                id = "0";
            }
            builder = new SpannableStringBuilder(TextUtils.replace(source, new String[]{param}, new String[]{name.replace('\n', ' ')}));
            stringBuilder = new StringBuilder();
            stringBuilder.append(TtmlNode.ANONYMOUS_REGION_ID);
            stringBuilder.append(id);
            builder.setSpan(new URLSpanNoUnderlineBold(stringBuilder.toString()), start, name.length() + start, 33);
            return builder;
        }
        builder = new SpannableStringBuilder(TextUtils.replace(source, new String[]{param}, new String[]{name.replace('\n', ' ')}));
        stringBuilder = new StringBuilder();
        stringBuilder.append(TtmlNode.ANONYMOUS_REGION_ID);
        stringBuilder.append(id);
        builder.setSpan(new URLSpanNoUnderlineBold(stringBuilder.toString()), start, name.length() + start, 33);
        return builder;
    }

    public String getExtension() {
        String fileName = getFileName();
        int idx = fileName.lastIndexOf(46);
        String ext = null;
        if (idx != -1) {
            ext = fileName.substring(idx + 1);
        }
        if (ext == null || ext.length() == 0) {
            ext = this.messageOwner.media.document.mime_type;
        }
        if (ext == null) {
            ext = TtmlNode.ANONYMOUS_REGION_ID;
        }
        return ext.toUpperCase();
    }

    public String getFileName() {
        if (this.messageOwner.media instanceof TL_messageMediaDocument) {
            return FileLoader.getAttachFileName(this.messageOwner.media.document);
        }
        if (this.messageOwner.media instanceof TL_messageMediaPhoto) {
            ArrayList<PhotoSize> sizes = this.messageOwner.media.photo.sizes;
            if (sizes.size() > 0) {
                PhotoSize sizeFull = FileLoader.getClosestPhotoSizeWithSize(sizes, AndroidUtilities.getPhotoSize());
                if (sizeFull != null) {
                    return FileLoader.getAttachFileName(sizeFull);
                }
            }
        } else if (this.messageOwner.media instanceof TL_messageMediaWebPage) {
            return FileLoader.getAttachFileName(this.messageOwner.media.webpage.document);
        }
        return TtmlNode.ANONYMOUS_REGION_ID;
    }

    public int getFileType() {
        if (isVideo()) {
            return 2;
        }
        if (isVoice()) {
            return 1;
        }
        if (this.messageOwner.media instanceof TL_messageMediaDocument) {
            return 3;
        }
        if (this.messageOwner.media instanceof TL_messageMediaPhoto) {
            return 0;
        }
        return 4;
    }

    private static boolean containsUrls(CharSequence message) {
        if (message != null && message.length() >= 2) {
            if (message.length() <= CacheDataSink.DEFAULT_BUFFER_SIZE) {
                int length = message.length();
                char lastChar = '\u0000';
                int dotSequence = 0;
                int schemeSequence = 0;
                int digitsInRow = 0;
                int i = 0;
                while (i < length) {
                    char c = message.charAt(i);
                    if (c >= '0' && c <= '9') {
                        digitsInRow++;
                        if (digitsInRow >= 6) {
                            return true;
                        }
                        schemeSequence = 0;
                        dotSequence = 0;
                    } else if (c == ' ' || digitsInRow <= 0) {
                        digitsInRow = 0;
                    }
                    if ((c != '@' && c != '#' && c != '/' && c != '$') || i != 0) {
                        if (i != 0) {
                            if (message.charAt(i - 1) != ' ') {
                                if (message.charAt(i - 1) == '\n') {
                                }
                            }
                        }
                        if (c == ':') {
                            if (schemeSequence == 0) {
                                schemeSequence = 1;
                            } else {
                                schemeSequence = 0;
                            }
                        } else if (c == '/') {
                            if (schemeSequence == 2) {
                                return true;
                            }
                            if (schemeSequence == 1) {
                                schemeSequence++;
                            } else {
                                schemeSequence = 0;
                            }
                        } else if (c == '.') {
                            if (dotSequence != 0 || lastChar == ' ') {
                                dotSequence = 0;
                            } else {
                                dotSequence++;
                            }
                        } else if (c != ' ' && lastChar == '.' && dotSequence == 1) {
                            return true;
                        } else {
                            dotSequence = 0;
                        }
                        lastChar = c;
                        i++;
                    }
                    return true;
                }
                return false;
            }
        }
        return false;
    }

    public void generateLinkDescription() {
        if (this.linkDescription == null) {
            if ((this.messageOwner.media instanceof TL_messageMediaWebPage) && (this.messageOwner.media.webpage instanceof TL_webPage) && this.messageOwner.media.webpage.description != null) {
                this.linkDescription = Factory.getInstance().newSpannable(this.messageOwner.media.webpage.description);
            } else if ((this.messageOwner.media instanceof TL_messageMediaGame) && this.messageOwner.media.game.description != null) {
                this.linkDescription = Factory.getInstance().newSpannable(this.messageOwner.media.game.description);
            } else if ((this.messageOwner.media instanceof TL_messageMediaInvoice) && this.messageOwner.media.description != null) {
                this.linkDescription = Factory.getInstance().newSpannable(this.messageOwner.media.description);
            }
            if (this.linkDescription != null) {
                if (containsUrls(this.linkDescription)) {
                    try {
                        Linkify.addLinks((Spannable) this.linkDescription, 1);
                    } catch (Throwable e) {
                        FileLog.e(e);
                    }
                }
                this.linkDescription = Emoji.replaceEmoji(this.linkDescription, Theme.chat_msgTextPaint.getFontMetricsInt(), AndroidUtilities.dp(20.0f), false);
            }
        }
    }

    public void generateCaption() {
        if (this.caption == null) {
            if (!isRoundVideo()) {
                if (!(isMediaEmpty() || (this.messageOwner.media instanceof TL_messageMediaGame) || TextUtils.isEmpty(this.messageOwner.message))) {
                    boolean hasEntities;
                    boolean z = false;
                    this.caption = Emoji.replaceEmoji(this.messageOwner.message, Theme.chat_msgTextPaint.getFontMetricsInt(), AndroidUtilities.dp(20.0f), false);
                    if (this.messageOwner.send_state != 0) {
                        hasEntities = false;
                        for (int a = 0; a < this.messageOwner.entities.size(); a++) {
                            if (!(this.messageOwner.entities.get(a) instanceof TL_inputMessageEntityMentionName)) {
                                hasEntities = true;
                                break;
                            }
                        }
                    } else {
                        hasEntities = this.messageOwner.entities.isEmpty() ^ true;
                    }
                    if (!hasEntities && (this.eventId != 0 || (this.messageOwner.media instanceof TL_messageMediaPhoto_old) || (this.messageOwner.media instanceof TL_messageMediaPhoto_layer68) || (this.messageOwner.media instanceof TL_messageMediaPhoto_layer74) || (this.messageOwner.media instanceof TL_messageMediaDocument_old) || (this.messageOwner.media instanceof TL_messageMediaDocument_layer68) || (this.messageOwner.media instanceof TL_messageMediaDocument_layer74) || ((isOut() && this.messageOwner.send_state != 0) || this.messageOwner.id < 0))) {
                        z = true;
                    }
                    boolean useManualParse = z;
                    if (useManualParse) {
                        if (containsUrls(this.caption)) {
                            try {
                                Linkify.addLinks((Spannable) this.caption, 5);
                            } catch (Throwable e) {
                                FileLog.e(e);
                            }
                        }
                        addUsernamesAndHashtags(isOutOwner(), this.caption, true);
                    } else {
                        try {
                            Linkify.addLinks((Spannable) this.caption, 4);
                        } catch (Throwable e2) {
                            FileLog.e(e2);
                        }
                    }
                    addEntitiesToText(this.caption, useManualParse);
                }
            }
        }
    }

    private static void addUsernamesAndHashtags(boolean isOut, CharSequence charSequence, boolean botCommands) {
        try {
            if (urlPattern == null) {
                urlPattern = Pattern.compile("(^|\\s)/[a-zA-Z@\\d_]{1,255}|(^|\\s)@[a-zA-Z\\d_]{1,32}|(^|\\s)#[\\w\\.]+|(^|\\s)\\$[A-Z]{3,8}([ ,.]|$)");
            }
            Matcher matcher = urlPattern.matcher(charSequence);
            while (matcher.find()) {
                int start = matcher.start();
                int end = matcher.end();
                char ch = charSequence.charAt(start);
                if (!(ch == '@' || ch == '#' || ch == '/' || ch == '$')) {
                    start++;
                }
                URLSpanNoUnderline url = null;
                if (charSequence.charAt(start) != '/') {
                    url = new URLSpanNoUnderline(charSequence.subSequence(start, end).toString());
                } else if (botCommands) {
                    url = new URLSpanBotCommand(charSequence.subSequence(start, end).toString(), isOut);
                }
                if (url != null) {
                    ((Spannable) charSequence).setSpan(url, start, end, 0);
                }
            }
        } catch (Throwable e) {
            FileLog.e(e);
        }
    }

    public static int[] getWebDocumentWidthAndHeight(WebDocument document) {
        if (document == null) {
            return null;
        }
        int a = 0;
        int size = document.attributes.size();
        while (a < size) {
            DocumentAttribute attribute = (DocumentAttribute) document.attributes.get(a);
            if (attribute instanceof TL_documentAttributeImageSize) {
                return new int[]{attribute.w, attribute.h};
            } else if (attribute instanceof TL_documentAttributeVideo) {
                return new int[]{attribute.w, attribute.h};
            } else {
                a++;
            }
        }
        return null;
    }

    public static int getWebDocumentDuration(WebDocument document) {
        if (document == null) {
            return 0;
        }
        int size = document.attributes.size();
        for (int a = 0; a < size; a++) {
            DocumentAttribute attribute = (DocumentAttribute) document.attributes.get(a);
            if (attribute instanceof TL_documentAttributeVideo) {
                return attribute.duration;
            }
            if (attribute instanceof TL_documentAttributeAudio) {
                return attribute.duration;
            }
        }
        return 0;
    }

    public static int[] getInlineResultWidthAndHeight(BotInlineResult inlineResult) {
        int[] result = getWebDocumentWidthAndHeight(inlineResult.content);
        if (result != null) {
            return result;
        }
        result = getWebDocumentWidthAndHeight(inlineResult.thumb);
        if (result == null) {
            return new int[]{0, 0};
        }
        return result;
    }

    public static int getInlineResultDuration(BotInlineResult inlineResult) {
        int result = getWebDocumentDuration(inlineResult.content);
        if (result == 0) {
            return getWebDocumentDuration(inlineResult.thumb);
        }
        return result;
    }

    public boolean hasValidGroupId() {
        return (getGroupId() == 0 || this.photoThumbs == null || this.photoThumbs.isEmpty()) ? false : true;
    }

    public long getGroupId() {
        return this.localGroupId != 0 ? this.localGroupId : this.messageOwner.grouped_id;
    }

    public static void addLinks(boolean isOut, CharSequence messageText) {
        addLinks(isOut, messageText, true);
    }

    public static void addLinks(boolean isOut, CharSequence messageText, boolean botCommands) {
        if ((messageText instanceof Spannable) && containsUrls(messageText)) {
            if (messageText.length() < 1000) {
                try {
                    Linkify.addLinks((Spannable) messageText, 5);
                } catch (Throwable e) {
                    FileLog.e(e);
                }
            } else {
                try {
                    Linkify.addLinks((Spannable) messageText, 1);
                } catch (Throwable e2) {
                    FileLog.e(e2);
                }
            }
            addUsernamesAndHashtags(isOut, messageText, botCommands);
        }
    }

    public void resetPlayingProgress() {
        this.audioProgress = 0.0f;
        this.audioProgressSec = 0;
        this.bufferedProgress = 0.0f;
    }

    private boolean addEntitiesToText(CharSequence text, boolean useManualParse) {
        return addEntitiesToText(text, false, useManualParse);
    }

    public boolean addEntitiesToText(CharSequence text, boolean photoViewer, boolean useManualParse) {
        MessageObject messageObject = this;
        CharSequence charSequence = text;
        boolean hasUrls = false;
        boolean z = false;
        if (!(charSequence instanceof Spannable)) {
            return false;
        }
        Spannable spannable = (Spannable) charSequence;
        int count = messageObject.messageOwner.entities.size();
        URLSpan[] spans = (URLSpan[]) spannable.getSpans(0, text.length(), URLSpan.class);
        if (spans != null && spans.length > 0) {
            hasUrls = true;
        }
        boolean hasUrls2 = hasUrls;
        int a = 0;
        while (a < count) {
            MessageEntity entity = (MessageEntity) messageObject.messageOwner.entities.get(a);
            if (entity.length > 0 && entity.offset >= 0) {
                if (entity.offset < text.length()) {
                    if (entity.offset + entity.length > text.length()) {
                        entity.length = text.length() - entity.offset;
                    }
                    if ((!useManualParse || (entity instanceof TL_messageEntityBold) || (entity instanceof TL_messageEntityItalic) || (entity instanceof TL_messageEntityCode) || (entity instanceof TL_messageEntityPre) || (entity instanceof TL_messageEntityMentionName) || (entity instanceof TL_inputMessageEntityMentionName)) && spans != null && spans.length > 0) {
                        for (int b = z; b < spans.length; b++) {
                            if (spans[b] != null) {
                                int start = spannable.getSpanStart(spans[b]);
                                int end = spannable.getSpanEnd(spans[b]);
                                if ((entity.offset <= start && entity.offset + entity.length >= start) || (entity.offset <= end && entity.offset + entity.length >= end)) {
                                    spannable.removeSpan(spans[b]);
                                    spans[b] = null;
                                }
                            }
                        }
                    }
                    if (entity instanceof TL_messageEntityBold) {
                        spannable.setSpan(new TypefaceSpan(AndroidUtilities.getTypeface("fonts/rmedium.ttf")), entity.offset, entity.offset + entity.length, 33);
                    } else if (entity instanceof TL_messageEntityItalic) {
                        spannable.setSpan(new TypefaceSpan(AndroidUtilities.getTypeface("fonts/ritalic.ttf")), entity.offset, entity.offset + entity.length, 33);
                    } else {
                        byte type;
                        if (!(entity instanceof TL_messageEntityCode)) {
                            if (!(entity instanceof TL_messageEntityPre)) {
                                StringBuilder stringBuilder;
                                if (entity instanceof TL_messageEntityMentionName) {
                                    stringBuilder = new StringBuilder();
                                    stringBuilder.append(TtmlNode.ANONYMOUS_REGION_ID);
                                    stringBuilder.append(((TL_messageEntityMentionName) entity).user_id);
                                    spannable.setSpan(new URLSpanUserMention(stringBuilder.toString(), messageObject.type), entity.offset, entity.offset + entity.length, 33);
                                } else if (entity instanceof TL_inputMessageEntityMentionName) {
                                    stringBuilder = new StringBuilder();
                                    stringBuilder.append(TtmlNode.ANONYMOUS_REGION_ID);
                                    stringBuilder.append(((TL_inputMessageEntityMentionName) entity).user_id.user_id);
                                    spannable.setSpan(new URLSpanUserMention(stringBuilder.toString(), messageObject.type), entity.offset, entity.offset + entity.length, 33);
                                } else if (!useManualParse) {
                                    String url = TextUtils.substring(charSequence, entity.offset, entity.offset + entity.length);
                                    if (entity instanceof TL_messageEntityBotCommand) {
                                        spannable.setSpan(new URLSpanBotCommand(url, messageObject.type), entity.offset, entity.offset + entity.length, 33);
                                    } else {
                                        if (!((entity instanceof TL_messageEntityHashtag) || (entity instanceof TL_messageEntityMention))) {
                                            if (!(entity instanceof TL_messageEntityCashtag)) {
                                                StringBuilder stringBuilder2;
                                                if (entity instanceof TL_messageEntityEmail) {
                                                    stringBuilder2 = new StringBuilder();
                                                    stringBuilder2.append("mailto:");
                                                    stringBuilder2.append(url);
                                                    spannable.setSpan(new URLSpanReplacement(stringBuilder2.toString()), entity.offset, entity.offset + entity.length, 33);
                                                } else if (entity instanceof TL_messageEntityUrl) {
                                                    hasUrls2 = true;
                                                    if (url.toLowerCase().startsWith("http") || url.toLowerCase().startsWith("tg://")) {
                                                        spannable.setSpan(new URLSpanBrowser(url), entity.offset, entity.offset + entity.length, 33);
                                                    } else {
                                                        stringBuilder2 = new StringBuilder();
                                                        stringBuilder2.append("http://");
                                                        stringBuilder2.append(url);
                                                        spannable.setSpan(new URLSpanBrowser(stringBuilder2.toString()), entity.offset, entity.offset + entity.length, 33);
                                                    }
                                                } else if (entity instanceof TL_messageEntityPhone) {
                                                    hasUrls2 = true;
                                                    String tel = PhoneFormat.stripExceptNumbers(url);
                                                    if (url.startsWith("+")) {
                                                        stringBuilder2 = new StringBuilder();
                                                        stringBuilder2.append("+");
                                                        stringBuilder2.append(tel);
                                                        tel = stringBuilder2.toString();
                                                    }
                                                    StringBuilder stringBuilder3 = new StringBuilder();
                                                    stringBuilder3.append("tel://");
                                                    stringBuilder3.append(tel);
                                                    spannable.setSpan(new URLSpanBrowser(stringBuilder3.toString()), entity.offset, entity.offset + entity.length, 33);
                                                } else if (entity instanceof TL_messageEntityTextUrl) {
                                                    spannable.setSpan(new URLSpanReplacement(entity.url), entity.offset, entity.offset + entity.length, 33);
                                                }
                                            }
                                        }
                                        spannable.setSpan(new URLSpanNoUnderline(url), entity.offset, entity.offset + entity.length, 33);
                                    }
                                }
                            }
                        }
                        if (photoViewer) {
                            type = (byte) 2;
                        } else if (isOutOwner()) {
                            type = (byte) 1;
                        } else {
                            type = (byte) 0;
                            spannable.setSpan(new URLSpanMono(spannable, entity.offset, entity.offset + entity.length, type), entity.offset, entity.offset + entity.length, 33);
                        }
                        spannable.setSpan(new URLSpanMono(spannable, entity.offset, entity.offset + entity.length, type), entity.offset, entity.offset + entity.length, 33);
                    }
                }
            }
            a++;
            z = false;
        }
        return hasUrls2;
    }

    public boolean isOut() {
        return this.messageOwner.out;
    }

    public boolean isOutOwner() {
        boolean z = false;
        if (this.messageOwner.out && this.messageOwner.from_id > 0) {
            if (!this.messageOwner.post) {
                if (this.messageOwner.fwd_from == null) {
                    return true;
                }
                int selfUserId = UserConfig.getInstance(this.currentAccount).getClientUserId();
                if (getDialogId() == ((long) selfUserId)) {
                    if (this.messageOwner.fwd_from.from_id != selfUserId) {
                        if (this.messageOwner.fwd_from.saved_from_peer == null || this.messageOwner.fwd_from.saved_from_peer.user_id != selfUserId) {
                            return z;
                        }
                    }
                    z = true;
                    return z;
                }
                if (this.messageOwner.fwd_from.saved_from_peer != null) {
                    if (this.messageOwner.fwd_from.saved_from_peer.user_id != selfUserId) {
                        return z;
                    }
                }
                z = true;
                return z;
            }
        }
        return false;
    }

    public boolean needDrawAvatar() {
        if (!isFromUser() && this.eventId == 0) {
            if (this.messageOwner.fwd_from == null || this.messageOwner.fwd_from.saved_from_peer == null) {
                return false;
            }
        }
        return true;
    }

    public boolean isFromUser() {
        return this.messageOwner.from_id > 0 && !this.messageOwner.post;
    }

    public boolean isUnread() {
        return this.messageOwner.unread;
    }

    public boolean isContentUnread() {
        return this.messageOwner.media_unread;
    }

    public void setIsRead() {
        this.messageOwner.unread = false;
    }

    public int getUnradFlags() {
        return getUnreadFlags(this.messageOwner);
    }

    public static int getUnreadFlags(Message message) {
        int flags = 0;
        if (!message.unread) {
            flags = 0 | 1;
        }
        if (message.media_unread) {
            return flags;
        }
        return flags | 2;
    }

    public void setContentIsRead() {
        this.messageOwner.media_unread = false;
    }

    public int getId() {
        return this.messageOwner.id;
    }

    public static int getMessageSize(Message message) {
        if (message.media == null || message.media.document == null) {
            return 0;
        }
        return message.media.document.size;
    }

    public int getSize() {
        return getMessageSize(this.messageOwner);
    }

    public long getIdWithChannel() {
        long id = (long) this.messageOwner.id;
        if (this.messageOwner.to_id == null || this.messageOwner.to_id.channel_id == 0) {
            return id;
        }
        return id | (((long) this.messageOwner.to_id.channel_id) << 32);
    }

    public int getChannelId() {
        if (this.messageOwner.to_id != null) {
            return this.messageOwner.to_id.channel_id;
        }
        return 0;
    }

    public static boolean shouldEncryptPhotoOrVideo(Message message) {
        boolean z = false;
        if (message instanceof TL_message_secret) {
            if (((message.media instanceof TL_messageMediaPhoto) || isVideoMessage(message)) && message.ttl > 0 && message.ttl <= 60) {
                z = true;
            }
            return z;
        }
        if (((message.media instanceof TL_messageMediaPhoto) || (message.media instanceof TL_messageMediaDocument)) && message.media.ttl_seconds != 0) {
            z = true;
        }
        return z;
    }

    public boolean shouldEncryptPhotoOrVideo() {
        return shouldEncryptPhotoOrVideo(this.messageOwner);
    }

    public static boolean isSecretPhotoOrVideo(Message message) {
        boolean z = true;
        if (message instanceof TL_message_secret) {
            if ((!(message.media instanceof TL_messageMediaPhoto) && !isRoundVideoMessage(message) && !isVideoMessage(message)) || message.ttl <= 0 || message.ttl > 60) {
                z = false;
            }
            return z;
        } else if (!(message instanceof TL_message)) {
            return false;
        } else {
            if ((!(message.media instanceof TL_messageMediaPhoto) && !(message.media instanceof TL_messageMediaDocument)) || message.media.ttl_seconds == 0) {
                z = false;
            }
            return z;
        }
    }

    public boolean needDrawBluredPreview() {
        boolean z = true;
        if (this.messageOwner instanceof TL_message_secret) {
            int ttl = Math.max(this.messageOwner.ttl, this.messageOwner.media.ttl_seconds);
            if (ttl <= 0 || ((!((this.messageOwner.media instanceof TL_messageMediaPhoto) || isVideo() || isGif()) || ttl > 60) && !isRoundVideo())) {
                z = false;
            }
            return z;
        } else if (!(this.messageOwner instanceof TL_message)) {
            return false;
        } else {
            if ((!(this.messageOwner.media instanceof TL_messageMediaPhoto) && !(this.messageOwner.media instanceof TL_messageMediaDocument)) || this.messageOwner.media.ttl_seconds == 0) {
                z = false;
            }
            return z;
        }
    }

    public boolean isSecretMedia() {
        boolean z = true;
        if (this.messageOwner instanceof TL_message_secret) {
            if (!((((this.messageOwner.media instanceof TL_messageMediaPhoto) || isGif()) && this.messageOwner.ttl > 0 && this.messageOwner.ttl <= 60) || isVoice() || isRoundVideo())) {
                if (!isVideo()) {
                    z = false;
                }
            }
            return z;
        } else if (!(this.messageOwner instanceof TL_message)) {
            return false;
        } else {
            if ((!(this.messageOwner.media instanceof TL_messageMediaPhoto) && !(this.messageOwner.media instanceof TL_messageMediaDocument)) || this.messageOwner.media.ttl_seconds == 0) {
                z = false;
            }
            return z;
        }
    }

    public static void setUnreadFlags(Message message, int flag) {
        boolean z = false;
        message.unread = (flag & 1) == 0;
        if ((flag & 2) == 0) {
            z = true;
        }
        message.media_unread = z;
    }

    public static boolean isUnread(Message message) {
        return message.unread;
    }

    public static boolean isContentUnread(Message message) {
        return message.media_unread;
    }

    public boolean isMegagroup() {
        return isMegagroup(this.messageOwner);
    }

    public boolean isSavedFromMegagroup() {
        if (this.messageOwner.fwd_from == null || this.messageOwner.fwd_from.saved_from_peer == null || this.messageOwner.fwd_from.saved_from_peer.channel_id == 0) {
            return false;
        }
        return ChatObject.isMegagroup(MessagesController.getInstance(this.currentAccount).getChat(Integer.valueOf(this.messageOwner.fwd_from.saved_from_peer.channel_id)));
    }

    public static boolean isMegagroup(Message message) {
        return (message.flags & Integer.MIN_VALUE) != 0;
    }

    public static boolean isOut(Message message) {
        return message.out;
    }

    public long getDialogId() {
        return getDialogId(this.messageOwner);
    }

    public boolean canStreamVideo() {
        Document document = getDocument();
        if (document == null) {
            return false;
        }
        if (SharedConfig.streamAllVideo) {
            return true;
        }
        for (int a = 0; a < document.attributes.size(); a++) {
            DocumentAttribute attribute = (DocumentAttribute) document.attributes.get(a);
            if (attribute instanceof TL_documentAttributeVideo) {
                return attribute.supports_streaming;
            }
        }
        return false;
    }

    public static long getDialogId(Message message) {
        if (message.dialog_id == 0 && message.to_id != null) {
            if (message.to_id.chat_id != 0) {
                if (message.to_id.chat_id < 0) {
                    message.dialog_id = AndroidUtilities.makeBroadcastId(message.to_id.chat_id);
                } else {
                    message.dialog_id = (long) (-message.to_id.chat_id);
                }
            } else if (message.to_id.channel_id != 0) {
                message.dialog_id = (long) (-message.to_id.channel_id);
            } else if (isOut(message)) {
                message.dialog_id = (long) message.to_id.user_id;
            } else {
                message.dialog_id = (long) message.from_id;
            }
        }
        return message.dialog_id;
    }

    public boolean isSending() {
        return this.messageOwner.send_state == 1 && this.messageOwner.id < 0;
    }

    public boolean isSendError() {
        return this.messageOwner.send_state == 2 && this.messageOwner.id < 0;
    }

    public boolean isSent() {
        if (this.messageOwner.send_state != 0) {
            if (this.messageOwner.id <= 0) {
                return false;
            }
        }
        return true;
    }

    public int getSecretTimeLeft() {
        int secondsLeft = this.messageOwner.ttl;
        if (this.messageOwner.destroyTime != 0) {
            return Math.max(0, this.messageOwner.destroyTime - ConnectionsManager.getInstance(this.currentAccount).getCurrentTime());
        }
        return secondsLeft;
    }

    public String getSecretTimeString() {
        if (!isSecretMedia()) {
            return null;
        }
        String str;
        int secondsLeft = getSecretTimeLeft();
        if (secondsLeft < 60) {
            str = new StringBuilder();
            str.append(secondsLeft);
            str.append("s");
            str = str.toString();
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(secondsLeft / 60);
            stringBuilder.append("m");
            str = stringBuilder.toString();
        }
        return str;
    }

    public String getDocumentName() {
        if (this.messageOwner.media instanceof TL_messageMediaDocument) {
            return FileLoader.getDocumentFileName(this.messageOwner.media.document);
        }
        if (this.messageOwner.media instanceof TL_messageMediaWebPage) {
            return FileLoader.getDocumentFileName(this.messageOwner.media.webpage.document);
        }
        return TtmlNode.ANONYMOUS_REGION_ID;
    }

    public static boolean isStickerDocument(Document document) {
        if (document != null) {
            for (int a = 0; a < document.attributes.size(); a++) {
                if (((DocumentAttribute) document.attributes.get(a)) instanceof TL_documentAttributeSticker) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isMaskDocument(Document document) {
        if (document != null) {
            for (int a = 0; a < document.attributes.size(); a++) {
                DocumentAttribute attribute = (DocumentAttribute) document.attributes.get(a);
                if ((attribute instanceof TL_documentAttributeSticker) && attribute.mask) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isVoiceDocument(Document document) {
        if (document != null) {
            for (int a = 0; a < document.attributes.size(); a++) {
                DocumentAttribute attribute = (DocumentAttribute) document.attributes.get(a);
                if (attribute instanceof TL_documentAttributeAudio) {
                    return attribute.voice;
                }
            }
        }
        return false;
    }

    public static boolean isVoiceWebDocument(TL_webDocument webDocument) {
        return webDocument != null && webDocument.mime_type.equals("audio/ogg");
    }

    public static boolean isImageWebDocument(TL_webDocument webDocument) {
        return (webDocument == null || isGifDocument(webDocument) || !webDocument.mime_type.startsWith("image/")) ? false : true;
    }

    public static boolean isVideoWebDocument(TL_webDocument webDocument) {
        return webDocument != null && webDocument.mime_type.startsWith("video/");
    }

    public static boolean isMusicDocument(Document document) {
        if (document != null) {
            for (int a = 0; a < document.attributes.size(); a++) {
                DocumentAttribute attribute = (DocumentAttribute) document.attributes.get(a);
                if (attribute instanceof TL_documentAttributeAudio) {
                    return attribute.voice ^ true;
                }
            }
            if (!TextUtils.isEmpty(document.mime_type)) {
                String mime = document.mime_type.toLowerCase();
                if (!(mime.equals(MimeTypes.AUDIO_FLAC) || mime.equals("audio/ogg") || mime.equals(MimeTypes.AUDIO_OPUS))) {
                    if (!mime.equals("audio/x-opus+ogg")) {
                        if (mime.equals("application/octet-stream") && FileLoader.getDocumentFileName(document).endsWith(".opus")) {
                            return true;
                        }
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    public static boolean isVideoDocument(Document document) {
        boolean z = false;
        if (document == null) {
            return false;
        }
        int width = 0;
        int height = 0;
        boolean isVideo = false;
        boolean isAnimated = false;
        for (int a = 0; a < document.attributes.size(); a++) {
            DocumentAttribute attribute = (DocumentAttribute) document.attributes.get(a);
            if (attribute instanceof TL_documentAttributeVideo) {
                if (attribute.round_message) {
                    return false;
                }
                isVideo = true;
                width = attribute.w;
                height = attribute.h;
            } else if (attribute instanceof TL_documentAttributeAnimated) {
                isAnimated = true;
            }
        }
        if (isAnimated && (width > 1280 || height > 1280)) {
            isAnimated = false;
        }
        if (isVideo && !isAnimated) {
            z = true;
        }
        return z;
    }

    public Document getDocument() {
        if (this.messageOwner.media instanceof TL_messageMediaWebPage) {
            return this.messageOwner.media.webpage.document;
        }
        return this.messageOwner.media != null ? this.messageOwner.media.document : null;
    }

    public static boolean isStickerMessage(Message message) {
        return (message.media == null || message.media.document == null || !isStickerDocument(message.media.document)) ? false : true;
    }

    public static boolean isMaskMessage(Message message) {
        return (message.media == null || message.media.document == null || !isMaskDocument(message.media.document)) ? false : true;
    }

    public static boolean isMusicMessage(Message message) {
        if (message.media instanceof TL_messageMediaWebPage) {
            return isMusicDocument(message.media.webpage.document);
        }
        boolean z = (message.media == null || message.media.document == null || !isMusicDocument(message.media.document)) ? false : true;
        return z;
    }

    public static boolean isGifMessage(Message message) {
        return (message.media == null || message.media.document == null || !isGifDocument(message.media.document)) ? false : true;
    }

    public static boolean isRoundVideoMessage(Message message) {
        if (message.media instanceof TL_messageMediaWebPage) {
            return isRoundVideoDocument(message.media.webpage.document);
        }
        boolean z = (message.media == null || message.media.document == null || !isRoundVideoDocument(message.media.document)) ? false : true;
        return z;
    }

    public static boolean isPhoto(Message message) {
        if (message.media instanceof TL_messageMediaWebPage) {
            return message.media.webpage.photo instanceof TL_photo;
        }
        return message.media instanceof TL_messageMediaPhoto;
    }

    public static boolean isVoiceMessage(Message message) {
        if (message.media instanceof TL_messageMediaWebPage) {
            return isVoiceDocument(message.media.webpage.document);
        }
        boolean z = (message.media == null || message.media.document == null || !isVoiceDocument(message.media.document)) ? false : true;
        return z;
    }

    public static boolean isNewGifMessage(Message message) {
        if (message.media instanceof TL_messageMediaWebPage) {
            return isNewGifDocument(message.media.webpage.document);
        }
        boolean z = (message.media == null || message.media.document == null || !isNewGifDocument(message.media.document)) ? false : true;
        return z;
    }

    public static boolean isLiveLocationMessage(Message message) {
        return message.media instanceof TL_messageMediaGeoLive;
    }

    public static boolean isVideoMessage(Message message) {
        if (message.media instanceof TL_messageMediaWebPage) {
            return isVideoDocument(message.media.webpage.document);
        }
        boolean z = (message.media == null || message.media.document == null || !isVideoDocument(message.media.document)) ? false : true;
        return z;
    }

    public static boolean isGameMessage(Message message) {
        return message.media instanceof TL_messageMediaGame;
    }

    public static boolean isInvoiceMessage(Message message) {
        return message.media instanceof TL_messageMediaInvoice;
    }

    public static InputStickerSet getInputStickerSet(Message message) {
        if (!(message.media == null || message.media.document == null)) {
            Iterator it = message.media.document.attributes.iterator();
            while (it.hasNext()) {
                DocumentAttribute attribute = (DocumentAttribute) it.next();
                if (attribute instanceof TL_documentAttributeSticker) {
                    if (attribute.stickerset instanceof TL_inputStickerSetEmpty) {
                        return null;
                    }
                    return attribute.stickerset;
                }
            }
        }
        return null;
    }

    public static long getStickerSetId(Document document) {
        if (document == null) {
            return -1;
        }
        int a = 0;
        while (a < document.attributes.size()) {
            DocumentAttribute attribute = (DocumentAttribute) document.attributes.get(a);
            if (!(attribute instanceof TL_documentAttributeSticker)) {
                a++;
            } else if (attribute.stickerset instanceof TL_inputStickerSetEmpty) {
                return -1;
            } else {
                return attribute.stickerset.id;
            }
        }
        return -1;
    }

    public String getStrickerChar() {
        if (!(this.messageOwner.media == null || this.messageOwner.media.document == null)) {
            Iterator it = this.messageOwner.media.document.attributes.iterator();
            while (it.hasNext()) {
                DocumentAttribute attribute = (DocumentAttribute) it.next();
                if (attribute instanceof TL_documentAttributeSticker) {
                    return attribute.alt;
                }
            }
        }
        return null;
    }

    public int getApproximateHeight() {
        int height;
        if (this.type == 0) {
            height = this.textHeight;
            int dp = ((this.messageOwner.media instanceof TL_messageMediaWebPage) && (this.messageOwner.media.webpage instanceof TL_webPage)) ? AndroidUtilities.dp(100.0f) : 0;
            height += dp;
            if (isReply()) {
                height += AndroidUtilities.dp(42.0f);
            }
            return height;
        } else if (this.type == 2) {
            return AndroidUtilities.dp(72.0f);
        } else {
            if (this.type == 12) {
                return AndroidUtilities.dp(71.0f);
            }
            if (this.type == 9) {
                return AndroidUtilities.dp(100.0f);
            }
            if (this.type == 4) {
                return AndroidUtilities.dp(114.0f);
            }
            if (this.type == 14) {
                return AndroidUtilities.dp(82.0f);
            }
            if (this.type == 10) {
                return AndroidUtilities.dp(30.0f);
            }
            if (this.type == 11) {
                return AndroidUtilities.dp(50.0f);
            }
            if (this.type == 5) {
                return AndroidUtilities.roundMessageSize;
            }
            if (this.type == 13) {
                float maxWidth;
                float maxHeight = ((float) AndroidUtilities.displaySize.y) * 0.4f;
                if (AndroidUtilities.isTablet()) {
                    maxWidth = ((float) AndroidUtilities.getMinTabletSide()) * 0.5f;
                } else {
                    maxWidth = ((float) AndroidUtilities.displaySize.x) * 0.5f;
                }
                int photoHeight = 0;
                int photoWidth = 0;
                Iterator it = this.messageOwner.media.document.attributes.iterator();
                while (it.hasNext()) {
                    DocumentAttribute attribute = (DocumentAttribute) it.next();
                    if (attribute instanceof TL_documentAttributeImageSize) {
                        photoWidth = attribute.w;
                        photoHeight = attribute.h;
                        break;
                    }
                }
                if (photoWidth == 0) {
                    photoHeight = (int) maxHeight;
                    photoWidth = photoHeight + AndroidUtilities.dp(100.0f);
                }
                if (((float) photoHeight) > maxHeight) {
                    photoWidth = (int) (((float) photoWidth) * (maxHeight / ((float) photoHeight)));
                    photoHeight = (int) maxHeight;
                }
                if (((float) photoWidth) > maxWidth) {
                    photoHeight = (int) (((float) photoHeight) * (maxWidth / ((float) photoWidth)));
                }
                return AndroidUtilities.dp(14.0f) + photoHeight;
            }
            if (AndroidUtilities.isTablet()) {
                height = (int) (((float) AndroidUtilities.getMinTabletSide()) * 1060320051);
            } else {
                height = (int) (((float) Math.min(AndroidUtilities.displaySize.x, AndroidUtilities.displaySize.y)) * 0.7f);
            }
            int photoHeight2 = AndroidUtilities.dp(100.0f) + height;
            if (height > AndroidUtilities.getPhotoSize()) {
                height = AndroidUtilities.getPhotoSize();
            }
            if (photoHeight2 > AndroidUtilities.getPhotoSize()) {
                photoHeight2 = AndroidUtilities.getPhotoSize();
            }
            PhotoSize currentPhotoObject = FileLoader.getClosestPhotoSizeWithSize(this.photoThumbs, AndroidUtilities.getPhotoSize());
            if (currentPhotoObject != null) {
                int h = (int) (((float) currentPhotoObject.h) / (((float) currentPhotoObject.w) / ((float) height)));
                if (h == 0) {
                    h = AndroidUtilities.dp(100.0f);
                }
                if (h > photoHeight2) {
                    h = photoHeight2;
                } else if (h < AndroidUtilities.dp(120.0f)) {
                    h = AndroidUtilities.dp(120.0f);
                }
                if (needDrawBluredPreview()) {
                    if (AndroidUtilities.isTablet()) {
                        h = (int) (((float) AndroidUtilities.getMinTabletSide()) * 0.5f);
                    } else {
                        h = (int) (((float) Math.min(AndroidUtilities.displaySize.x, AndroidUtilities.displaySize.y)) * 0.5f);
                    }
                }
                photoHeight2 = h;
            }
            return AndroidUtilities.dp(14.0f) + photoHeight2;
        }
    }

    public String getStickerEmoji() {
        String str;
        int a = 0;
        while (true) {
            str = null;
            if (a >= this.messageOwner.media.document.attributes.size()) {
                return null;
            }
            DocumentAttribute attribute = (DocumentAttribute) this.messageOwner.media.document.attributes.get(a);
            if (attribute instanceof TL_documentAttributeSticker) {
                break;
            }
            a++;
        }
        if (attribute.alt != null && attribute.alt.length() > 0) {
            str = attribute.alt;
        }
        return str;
    }

    public boolean isSticker() {
        if (this.type == 1000) {
            return isStickerMessage(this.messageOwner);
        }
        return this.type == 13;
    }

    public boolean isMask() {
        return isMaskMessage(this.messageOwner);
    }

    public boolean isMusic() {
        return isMusicMessage(this.messageOwner);
    }

    public boolean isVoice() {
        return isVoiceMessage(this.messageOwner);
    }

    public boolean isVideo() {
        return isVideoMessage(this.messageOwner);
    }

    public boolean isLiveLocation() {
        return isLiveLocationMessage(this.messageOwner);
    }

    public boolean isGame() {
        return isGameMessage(this.messageOwner);
    }

    public boolean isInvoice() {
        return isInvoiceMessage(this.messageOwner);
    }

    public boolean isRoundVideo() {
        if (this.isRoundVideoCached == 0) {
            int i;
            if (this.type != 5) {
                if (!isRoundVideoMessage(this.messageOwner)) {
                    i = 2;
                    this.isRoundVideoCached = i;
                }
            }
            i = 1;
            this.isRoundVideoCached = i;
        }
        if (this.isRoundVideoCached == 1) {
            return true;
        }
        return false;
    }

    public boolean hasPhotoStickers() {
        return (this.messageOwner.media == null || this.messageOwner.media.photo == null || !this.messageOwner.media.photo.has_stickers) ? false : true;
    }

    public boolean isGif() {
        return isGifMessage(this.messageOwner);
    }

    public boolean isWebpageDocument() {
        return (!(this.messageOwner.media instanceof TL_messageMediaWebPage) || this.messageOwner.media.webpage.document == null || isGifDocument(this.messageOwner.media.webpage.document)) ? false : true;
    }

    public boolean isWebpage() {
        return this.messageOwner.media instanceof TL_messageMediaWebPage;
    }

    public boolean isNewGif() {
        return this.messageOwner.media != null && isNewGifDocument(this.messageOwner.media.document);
    }

    public String getMusicTitle() {
        return getMusicTitle(true);
    }

    public String getMusicTitle(boolean unknown) {
        Document document;
        if (this.type == 0) {
            document = this.messageOwner.media.webpage.document;
        } else {
            document = this.messageOwner.media.document;
        }
        int a = 0;
        while (a < document.attributes.size()) {
            DocumentAttribute attribute = (DocumentAttribute) document.attributes.get(a);
            if (attribute instanceof TL_documentAttributeAudio) {
                if (!attribute.voice) {
                    String title = attribute.title;
                    if (title == null || title.length() == 0) {
                        title = FileLoader.getDocumentFileName(document);
                        if (TextUtils.isEmpty(title) && unknown) {
                            title = LocaleController.getString("AudioUnknownTitle", R.string.AudioUnknownTitle);
                        }
                    }
                    return title;
                } else if (unknown) {
                    return LocaleController.formatDateAudio((long) this.messageOwner.date);
                } else {
                    return null;
                }
            } else if ((attribute instanceof TL_documentAttributeVideo) && attribute.round_message) {
                return LocaleController.formatDateAudio((long) this.messageOwner.date);
            } else {
                a++;
            }
        }
        String fileName = FileLoader.getDocumentFileName(document);
        return TextUtils.isEmpty(fileName) ? LocaleController.getString("AudioUnknownTitle", R.string.AudioUnknownTitle) : fileName;
    }

    public int getDuration() {
        Document document;
        if (this.type == 0) {
            document = this.messageOwner.media.webpage.document;
        } else {
            document = this.messageOwner.media.document;
        }
        for (int a = 0; a < document.attributes.size(); a++) {
            DocumentAttribute attribute = (DocumentAttribute) document.attributes.get(a);
            if (attribute instanceof TL_documentAttributeAudio) {
                return attribute.duration;
            }
            if (attribute instanceof TL_documentAttributeVideo) {
                return attribute.duration;
            }
        }
        return this.audioPlayerDuration;
    }

    public String getMusicAuthor() {
        return getMusicAuthor(true);
    }

    public String getMusicAuthor(boolean unknown) {
        Document document;
        if (this.type == 0) {
            document = this.messageOwner.media.webpage.document;
        } else {
            document = this.messageOwner.media.document;
        }
        boolean isVoice = false;
        for (int a = 0; a < document.attributes.size(); a++) {
            DocumentAttribute attribute = (DocumentAttribute) document.attributes.get(a);
            if (attribute instanceof TL_documentAttributeAudio) {
                if (attribute.voice) {
                    isVoice = true;
                } else {
                    String performer = attribute.performer;
                    if (TextUtils.isEmpty(performer) && unknown) {
                        performer = LocaleController.getString("AudioUnknownArtist", R.string.AudioUnknownArtist);
                    }
                    return performer;
                }
            } else if ((attribute instanceof TL_documentAttributeVideo) && attribute.round_message) {
                isVoice = true;
            }
            if (isVoice) {
                if (!unknown) {
                    return null;
                }
                if (!isOutOwner()) {
                    if (this.messageOwner.fwd_from == null || this.messageOwner.fwd_from.from_id != UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                        User user = null;
                        Chat chat = null;
                        if (this.messageOwner.fwd_from != null && this.messageOwner.fwd_from.channel_id != 0) {
                            chat = MessagesController.getInstance(this.currentAccount).getChat(Integer.valueOf(this.messageOwner.fwd_from.channel_id));
                        } else if (this.messageOwner.fwd_from != null && this.messageOwner.fwd_from.from_id != 0) {
                            user = MessagesController.getInstance(this.currentAccount).getUser(Integer.valueOf(this.messageOwner.fwd_from.from_id));
                        } else if (this.messageOwner.from_id < 0) {
                            chat = MessagesController.getInstance(this.currentAccount).getChat(Integer.valueOf(-this.messageOwner.from_id));
                        } else {
                            user = MessagesController.getInstance(this.currentAccount).getUser(Integer.valueOf(this.messageOwner.from_id));
                        }
                        if (user != null) {
                            return UserObject.getUserName(user);
                        }
                        if (chat != null) {
                            return chat.title;
                        }
                    }
                }
                return LocaleController.getString("FromYou", R.string.FromYou);
            }
        }
        return LocaleController.getString("AudioUnknownArtist", R.string.AudioUnknownArtist);
    }

    public InputStickerSet getInputStickerSet() {
        return getInputStickerSet(this.messageOwner);
    }

    public boolean isForwarded() {
        return isForwardedMessage(this.messageOwner);
    }

    public boolean needDrawForwarded() {
        return ((this.messageOwner.flags & 4) == 0 || this.messageOwner.fwd_from == null || this.messageOwner.fwd_from.saved_from_peer != null || ((long) UserConfig.getInstance(this.currentAccount).getClientUserId()) == getDialogId()) ? false : true;
    }

    public static boolean isForwardedMessage(Message message) {
        return ((message.flags & 4) == 0 || message.fwd_from == null) ? false : true;
    }

    public boolean isReply() {
        return (this.replyMessageObject == null || !(this.replyMessageObject.messageOwner instanceof TL_messageEmpty)) && !((this.messageOwner.reply_to_msg_id == 0 && this.messageOwner.reply_to_random_id == 0) || (this.messageOwner.flags & 8) == 0);
    }

    public boolean isMediaEmpty() {
        return isMediaEmpty(this.messageOwner);
    }

    public static boolean isMediaEmpty(Message message) {
        if (!(message == null || message.media == null || (message.media instanceof TL_messageMediaEmpty))) {
            if (!(message.media instanceof TL_messageMediaWebPage)) {
                return false;
            }
        }
        return true;
    }

    public boolean canEditMessage(Chat chat) {
        return canEditMessage(this.currentAccount, this.messageOwner, chat);
    }

    public boolean canEditMessageAnytime(Chat chat) {
        return canEditMessageAnytime(this.currentAccount, this.messageOwner, chat);
    }

    public static boolean canEditMessageAnytime(int currentAccount, Message message, Chat chat) {
        if (!(message == null || message.to_id == null || ((message.media != null && (isRoundVideoDocument(message.media.document) || isStickerDocument(message.media.document))) || ((message.action != null && !(message.action instanceof TL_messageActionEmpty)) || isForwardedMessage(message) || message.via_bot_id != 0)))) {
            if (message.id >= 0) {
                if (message.from_id == message.to_id.user_id && message.from_id == UserConfig.getInstance(currentAccount).getClientUserId() && !isLiveLocationMessage(message)) {
                    return true;
                }
                if (chat == null && message.to_id.channel_id != 0) {
                    chat = MessagesController.getInstance(UserConfig.selectedAccount).getChat(Integer.valueOf(message.to_id.channel_id));
                    if (chat == null) {
                        return false;
                    }
                }
                if (!message.out || chat == null || !chat.megagroup || (!chat.creator && (chat.admin_rights == null || !chat.admin_rights.pin_messages))) {
                    return false;
                }
                return true;
            }
        }
        return false;
    }

    public static boolean canEditMessage(int currentAccount, Message message, Chat chat) {
        boolean z = false;
        if (!(message == null || message.to_id == null || ((message.media != null && (isRoundVideoDocument(message.media.document) || isStickerDocument(message.media.document))) || ((message.action != null && !(message.action instanceof TL_messageActionEmpty)) || isForwardedMessage(message) || message.via_bot_id != 0)))) {
            if (message.id >= 0) {
                if (message.from_id == message.to_id.user_id && message.from_id == UserConfig.getInstance(currentAccount).getClientUserId() && !isLiveLocationMessage(message) && !(message.media instanceof TL_messageMediaContact)) {
                    return true;
                }
                if (chat == null && message.to_id.channel_id != 0) {
                    chat = MessagesController.getInstance(currentAccount).getChat(Integer.valueOf(message.to_id.channel_id));
                    if (chat == null) {
                        return false;
                    }
                }
                if (message.media != null && !(message.media instanceof TL_messageMediaEmpty) && !(message.media instanceof TL_messageMediaPhoto) && !(message.media instanceof TL_messageMediaDocument) && !(message.media instanceof TL_messageMediaWebPage)) {
                    return false;
                }
                if (message.out && chat != null && chat.megagroup && (chat.creator || (chat.admin_rights != null && chat.admin_rights.pin_messages))) {
                    return true;
                }
                if (Math.abs(message.date - ConnectionsManager.getInstance(currentAccount).getCurrentTime()) > MessagesController.getInstance(currentAccount).maxEditTime) {
                    return false;
                }
                if (message.to_id.channel_id == 0) {
                    if (message.out || message.from_id == UserConfig.getInstance(currentAccount).getClientUserId()) {
                        if (!((message.media instanceof TL_messageMediaPhoto) || (((message.media instanceof TL_messageMediaDocument) && !isStickerMessage(message)) || (message.media instanceof TL_messageMediaEmpty) || (message.media instanceof TL_messageMediaWebPage)))) {
                            if (message.media == null) {
                            }
                        }
                        z = true;
                        return z;
                    }
                    return z;
                } else if (((!chat.megagroup || !message.out) && (chat.megagroup || ((!chat.creator && (chat.admin_rights == null || (!chat.admin_rights.edit_messages && !message.out))) || !message.post))) || (!(message.media instanceof TL_messageMediaPhoto) && ((!(message.media instanceof TL_messageMediaDocument) || isStickerMessage(message)) && !(message.media instanceof TL_messageMediaEmpty) && !(message.media instanceof TL_messageMediaWebPage) && message.media != null))) {
                    return false;
                } else {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean canDeleteMessage(Chat chat) {
        return this.eventId == 0 && canDeleteMessage(this.currentAccount, this.messageOwner, chat);
    }

    public static boolean canDeleteMessage(int currentAccount, Message message, Chat chat) {
        boolean z = true;
        if (message.id < 0) {
            return true;
        }
        if (chat == null && message.to_id.channel_id != 0) {
            chat = MessagesController.getInstance(currentAccount).getChat(Integer.valueOf(message.to_id.channel_id));
        }
        if (ChatObject.isChannel(chat)) {
            if (message.id == 1 || (!chat.creator && ((chat.admin_rights == null || !(chat.admin_rights.delete_messages || message.out)) && !(chat.megagroup && message.out && message.from_id > 0)))) {
                z = false;
            }
            return z;
        }
        if (!isOut(message)) {
            if (ChatObject.isChannel(chat)) {
                z = false;
            }
        }
        return z;
    }

    public String getForwardedName() {
        if (this.messageOwner.fwd_from != null) {
            if (this.messageOwner.fwd_from.channel_id != 0) {
                Chat chat = MessagesController.getInstance(this.currentAccount).getChat(Integer.valueOf(this.messageOwner.fwd_from.channel_id));
                if (chat != null) {
                    return chat.title;
                }
            } else if (this.messageOwner.fwd_from.from_id != 0) {
                User user = MessagesController.getInstance(this.currentAccount).getUser(Integer.valueOf(this.messageOwner.fwd_from.from_id));
                if (user != null) {
                    return UserObject.getUserName(user);
                }
            }
        }
        return null;
    }

    public int getFromId() {
        if (this.messageOwner.fwd_from == null || this.messageOwner.fwd_from.saved_from_peer == null) {
            if (this.messageOwner.from_id != 0) {
                return this.messageOwner.from_id;
            }
            if (this.messageOwner.post) {
                return this.messageOwner.to_id.channel_id;
            }
        } else if (this.messageOwner.fwd_from.saved_from_peer.user_id != 0) {
            if (this.messageOwner.fwd_from.from_id != 0) {
                return this.messageOwner.fwd_from.from_id;
            }
            return this.messageOwner.fwd_from.saved_from_peer.user_id;
        } else if (this.messageOwner.fwd_from.saved_from_peer.channel_id != 0) {
            if (isSavedFromMegagroup() && this.messageOwner.fwd_from.from_id != 0) {
                return this.messageOwner.fwd_from.from_id;
            }
            if (this.messageOwner.fwd_from.channel_id != 0) {
                return -this.messageOwner.fwd_from.channel_id;
            }
            return -this.messageOwner.fwd_from.saved_from_peer.channel_id;
        } else if (this.messageOwner.fwd_from.saved_from_peer.chat_id != 0) {
            if (this.messageOwner.fwd_from.from_id != 0) {
                return this.messageOwner.fwd_from.from_id;
            }
            if (this.messageOwner.fwd_from.channel_id != 0) {
                return -this.messageOwner.fwd_from.channel_id;
            }
            return -this.messageOwner.fwd_from.saved_from_peer.chat_id;
        }
        return 0;
    }
}
