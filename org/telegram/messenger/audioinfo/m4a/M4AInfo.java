package org.telegram.messenger.audioinfo.m4a;

import com.coremedia.iso.boxes.FileTypeBox;
import com.coremedia.iso.boxes.MediaBox;
import com.coremedia.iso.boxes.MediaHeaderBox;
import com.coremedia.iso.boxes.MetaBox;
import com.coremedia.iso.boxes.MovieBox;
import com.coremedia.iso.boxes.MovieHeaderBox;
import com.coremedia.iso.boxes.TrackBox;
import com.coremedia.iso.boxes.UserDataBox;
import com.coremedia.iso.boxes.apple.AppleItemListBox;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.telegram.messenger.audioinfo.AudioInfo;
import org.telegram.messenger.exoplayer2.upstream.DataSchemeDataSource;

public class M4AInfo extends AudioInfo {
    private static final String ASCII = "ISO8859_1";
    static final Logger LOGGER = Logger.getLogger(M4AInfo.class.getName());
    private static final String UTF_8 = "UTF-8";
    private final Level debugLevel;
    private byte rating;
    private BigDecimal speed;
    private short tempo;
    private BigDecimal volume;

    public M4AInfo(InputStream input) throws IOException {
        this(input, Level.FINEST);
    }

    public M4AInfo(InputStream input, Level debugLevel) throws IOException {
        this.debugLevel = debugLevel;
        MP4Input mp4 = new MP4Input(input);
        if (LOGGER.isLoggable(debugLevel)) {
            LOGGER.log(debugLevel, mp4.toString());
        }
        ftyp(mp4.nextChild(FileTypeBox.TYPE));
        moov(mp4.nextChildUpTo(MovieBox.TYPE));
    }

    void ftyp(MP4Atom atom) throws IOException {
        if (LOGGER.isLoggable(this.debugLevel)) {
            LOGGER.log(this.debugLevel, atom.toString());
        }
        this.brand = atom.readString(4, ASCII).trim();
        Logger logger;
        StringBuilder stringBuilder;
        if (this.brand.matches("M4V|MP4|mp42|isom")) {
            logger = LOGGER;
            stringBuilder = new StringBuilder();
            stringBuilder.append(atom.getPath());
            stringBuilder.append(": brand=");
            stringBuilder.append(this.brand);
            stringBuilder.append(" (experimental)");
            logger.warning(stringBuilder.toString());
        } else if (!this.brand.matches("M4A|M4P")) {
            logger = LOGGER;
            stringBuilder = new StringBuilder();
            stringBuilder.append(atom.getPath());
            stringBuilder.append(": brand=");
            stringBuilder.append(this.brand);
            stringBuilder.append(" (expected M4A or M4P)");
            logger.warning(stringBuilder.toString());
        }
        this.version = String.valueOf(atom.readInt());
    }

    void moov(MP4Atom atom) throws IOException {
        if (LOGGER.isLoggable(this.debugLevel)) {
            LOGGER.log(this.debugLevel, atom.toString());
        }
        while (atom.hasMoreChildren()) {
            MP4Atom child = atom.nextChild();
            String type = child.getType();
            Object obj = -1;
            int hashCode = type.hashCode();
            if (hashCode != 3363941) {
                if (hashCode != 3568424) {
                    if (hashCode == 3585340) {
                        if (type.equals(UserDataBox.TYPE)) {
                            obj = 2;
                        }
                    }
                } else if (type.equals(TrackBox.TYPE)) {
                    obj = 1;
                }
            } else if (type.equals(MovieHeaderBox.TYPE)) {
                obj = null;
            }
            switch (obj) {
                case null:
                    mvhd(child);
                    break;
                case 1:
                    trak(child);
                    break;
                case 2:
                    udta(child);
                    break;
                default:
                    break;
            }
        }
    }

    void mvhd(MP4Atom atom) throws IOException {
        if (LOGGER.isLoggable(this.debugLevel)) {
            LOGGER.log(this.debugLevel, atom.toString());
        }
        byte version = atom.readByte();
        atom.skip(3);
        atom.skip(version == (byte) 1 ? 16 : 8);
        int scale = atom.readInt();
        long units = version == (byte) 1 ? atom.readLong() : (long) atom.readInt();
        if (this.duration == 0) {
            this.duration = (1000 * units) / ((long) scale);
        } else if (LOGGER.isLoggable(this.debugLevel) && Math.abs(this.duration - ((1000 * units) / ((long) scale))) > 2) {
            Logger logger = LOGGER;
            Level level = this.debugLevel;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("mvhd: duration ");
            stringBuilder.append(this.duration);
            stringBuilder.append(" -> ");
            stringBuilder.append((1000 * units) / ((long) scale));
            logger.log(level, stringBuilder.toString());
        }
        this.speed = atom.readIntegerFixedPoint();
        this.volume = atom.readShortFixedPoint();
    }

    void trak(MP4Atom atom) throws IOException {
        if (LOGGER.isLoggable(this.debugLevel)) {
            LOGGER.log(this.debugLevel, atom.toString());
        }
        mdia(atom.nextChildUpTo(MediaBox.TYPE));
    }

    void mdia(MP4Atom atom) throws IOException {
        if (LOGGER.isLoggable(this.debugLevel)) {
            LOGGER.log(this.debugLevel, atom.toString());
        }
        mdhd(atom.nextChild(MediaHeaderBox.TYPE));
    }

    void mdhd(MP4Atom atom) throws IOException {
        if (LOGGER.isLoggable(this.debugLevel)) {
            LOGGER.log(this.debugLevel, atom.toString());
        }
        byte version = atom.readByte();
        atom.skip(3);
        atom.skip(version == (byte) 1 ? 16 : 8);
        int sampleRate = atom.readInt();
        long samples = version == (byte) 1 ? atom.readLong() : (long) atom.readInt();
        if (this.duration == 0) {
            this.duration = (1000 * samples) / ((long) sampleRate);
        } else if (LOGGER.isLoggable(this.debugLevel) && Math.abs(this.duration - ((1000 * samples) / ((long) sampleRate))) > 2) {
            Logger logger = LOGGER;
            Level level = this.debugLevel;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("mdhd: duration ");
            stringBuilder.append(this.duration);
            stringBuilder.append(" -> ");
            stringBuilder.append((1000 * samples) / ((long) sampleRate));
            logger.log(level, stringBuilder.toString());
        }
    }

    void udta(MP4Atom atom) throws IOException {
        if (LOGGER.isLoggable(this.debugLevel)) {
            LOGGER.log(this.debugLevel, atom.toString());
        }
        while (atom.hasMoreChildren()) {
            MP4Atom child = atom.nextChild();
            if (MetaBox.TYPE.equals(child.getType())) {
                meta(child);
                return;
            }
        }
    }

    void meta(MP4Atom atom) throws IOException {
        if (LOGGER.isLoggable(this.debugLevel)) {
            LOGGER.log(this.debugLevel, atom.toString());
        }
        atom.skip(4);
        while (atom.hasMoreChildren()) {
            MP4Atom child = atom.nextChild();
            if (AppleItemListBox.TYPE.equals(child.getType())) {
                ilst(child);
                return;
            }
        }
    }

    void ilst(MP4Atom atom) throws IOException {
        if (LOGGER.isLoggable(this.debugLevel)) {
            LOGGER.log(this.debugLevel, atom.toString());
        }
        while (atom.hasMoreChildren()) {
            MP4Atom child = atom.nextChild();
            if (LOGGER.isLoggable(this.debugLevel)) {
                LOGGER.log(this.debugLevel, child.toString());
            }
            if (child.getRemaining() != 0) {
                data(child.nextChildUpTo(DataSchemeDataSource.SCHEME_DATA));
            } else if (LOGGER.isLoggable(this.debugLevel)) {
                Logger logger = LOGGER;
                Level level = this.debugLevel;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(child.getPath());
                stringBuilder.append(": contains no value");
                logger.log(level, stringBuilder.toString());
            }
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    void data(org.telegram.messenger.audioinfo.m4a.MP4Atom r9) throws java.io.IOException {
        /*
        r8 = this;
        r0 = LOGGER;
        r1 = r8.debugLevel;
        r0 = r0.isLoggable(r1);
        if (r0 == 0) goto L_0x0015;
    L_0x000a:
        r0 = LOGGER;
        r1 = r8.debugLevel;
        r2 = r9.toString();
        r0.log(r1, r2);
    L_0x0015:
        r0 = 4;
        r9.skip(r0);
        r9.skip(r0);
        r1 = r9.getParent();
        r1 = r1.getType();
        r2 = -1;
        r3 = r1.hashCode();
        r4 = 1;
        r5 = 2;
        r6 = 0;
        switch(r3) {
            case 2954818: goto L_0x0106;
            case 3059752: goto L_0x00fc;
            case 3060304: goto L_0x00f2;
            case 3060591: goto L_0x00e7;
            case 3083677: goto L_0x00dc;
            case 3177818: goto L_0x00d1;
            case 3511163: goto L_0x00c6;
            case 3564088: goto L_0x00bb;
            case 3568737: goto L_0x00b0;
            case 5099770: goto L_0x00a5;
            case 5131342: goto L_0x009a;
            case 5133313: goto L_0x008f;
            case 5133368: goto L_0x0084;
            case 5133411: goto L_0x0078;
            case 5133907: goto L_0x006c;
            case 5136903: goto L_0x0060;
            case 5137308: goto L_0x0054;
            case 5142332: goto L_0x0048;
            case 5143505: goto L_0x003c;
            case 5152688: goto L_0x0031;
            default: goto L_0x002f;
        };
    L_0x002f:
        goto L_0x0110;
    L_0x0031:
        r3 = "©wrt";
        r1 = r1.equals(r3);
        if (r1 == 0) goto L_0x0110;
    L_0x0039:
        r1 = 5;
        goto L_0x0111;
    L_0x003c:
        r3 = "©nam";
        r1 = r1.equals(r3);
        if (r1 == 0) goto L_0x0110;
    L_0x0044:
        r1 = 16;
        goto L_0x0111;
    L_0x0048:
        r3 = "©lyr";
        r1 = r1.equals(r3);
        if (r1 == 0) goto L_0x0110;
    L_0x0050:
        r1 = 15;
        goto L_0x0111;
    L_0x0054:
        r3 = "©grp";
        r1 = r1.equals(r3);
        if (r1 == 0) goto L_0x0110;
    L_0x005c:
        r1 = 14;
        goto L_0x0111;
    L_0x0060:
        r3 = "©gen";
        r1 = r1.equals(r3);
        if (r1 == 0) goto L_0x0110;
    L_0x0068:
        r1 = 13;
        goto L_0x0111;
    L_0x006c:
        r3 = "©day";
        r1 = r1.equals(r3);
        if (r1 == 0) goto L_0x0110;
    L_0x0074:
        r1 = 10;
        goto L_0x0111;
    L_0x0078:
        r3 = "©cpy";
        r1 = r1.equals(r3);
        if (r1 == 0) goto L_0x0110;
    L_0x0080:
        r1 = 9;
        goto L_0x0111;
    L_0x0084:
        r3 = "©com";
        r1 = r1.equals(r3);
        if (r1 == 0) goto L_0x0110;
    L_0x008c:
        r1 = r0;
        goto L_0x0111;
    L_0x008f:
        r3 = "©cmt";
        r1 = r1.equals(r3);
        if (r1 == 0) goto L_0x0110;
    L_0x0097:
        r1 = 3;
        goto L_0x0111;
    L_0x009a:
        r3 = "©alb";
        r1 = r1.equals(r3);
        if (r1 == 0) goto L_0x0110;
    L_0x00a2:
        r1 = r6;
        goto L_0x0111;
    L_0x00a5:
        r3 = "©ART";
        r1 = r1.equals(r3);
        if (r1 == 0) goto L_0x0110;
    L_0x00ad:
        r1 = r5;
        goto L_0x0111;
    L_0x00b0:
        r3 = "trkn";
        r1 = r1.equals(r3);
        if (r1 == 0) goto L_0x0110;
    L_0x00b8:
        r1 = 19;
        goto L_0x0111;
    L_0x00bb:
        r3 = "tmpo";
        r1 = r1.equals(r3);
        if (r1 == 0) goto L_0x0110;
    L_0x00c3:
        r1 = 18;
        goto L_0x0111;
    L_0x00c6:
        r3 = "rtng";
        r1 = r1.equals(r3);
        if (r1 == 0) goto L_0x0110;
    L_0x00ce:
        r1 = 17;
        goto L_0x0111;
    L_0x00d1:
        r3 = "gnre";
        r1 = r1.equals(r3);
        if (r1 == 0) goto L_0x0110;
    L_0x00d9:
        r1 = 12;
        goto L_0x0111;
    L_0x00dc:
        r3 = "disk";
        r1 = r1.equals(r3);
        if (r1 == 0) goto L_0x0110;
    L_0x00e4:
        r1 = 11;
        goto L_0x0111;
    L_0x00e7:
        r3 = "cprt";
        r1 = r1.equals(r3);
        if (r1 == 0) goto L_0x0110;
    L_0x00ef:
        r1 = 8;
        goto L_0x0111;
    L_0x00f2:
        r3 = "cpil";
        r1 = r1.equals(r3);
        if (r1 == 0) goto L_0x0110;
    L_0x00fa:
        r1 = 7;
        goto L_0x0111;
    L_0x00fc:
        r3 = "covr";
        r1 = r1.equals(r3);
        if (r1 == 0) goto L_0x0110;
    L_0x0104:
        r1 = 6;
        goto L_0x0111;
    L_0x0106:
        r3 = "aART";
        r1 = r1.equals(r3);
        if (r1 == 0) goto L_0x0110;
    L_0x010e:
        r1 = r4;
        goto L_0x0111;
    L_0x0110:
        r1 = r2;
    L_0x0111:
        switch(r1) {
            case 0: goto L_0x02b0;
            case 1: goto L_0x02a7;
            case 2: goto L_0x029e;
            case 3: goto L_0x0295;
            case 4: goto L_0x027c;
            case 5: goto L_0x027c;
            case 6: goto L_0x01fb;
            case 7: goto L_0x01f3;
            case 8: goto L_0x01d9;
            case 9: goto L_0x01d9;
            case 10: goto L_0x01b7;
            case 11: goto L_0x01a6;
            case 12: goto L_0x016f;
            case 13: goto L_0x0155;
            case 14: goto L_0x014b;
            case 15: goto L_0x0141;
            case 16: goto L_0x0137;
            case 17: goto L_0x012f;
            case 18: goto L_0x0127;
            case 19: goto L_0x0116;
            default: goto L_0x0114;
        };
    L_0x0114:
        goto L_0x02b9;
    L_0x0116:
        r9.skip(r5);
        r0 = r9.readShort();
        r8.track = r0;
        r0 = r9.readShort();
        r8.tracks = r0;
        goto L_0x02b9;
    L_0x0127:
        r0 = r9.readShort();
        r8.tempo = r0;
        goto L_0x02b9;
    L_0x012f:
        r0 = r9.readByte();
        r8.rating = r0;
        goto L_0x02b9;
    L_0x0137:
        r0 = "UTF-8";
        r0 = r9.readString(r0);
        r8.title = r0;
        goto L_0x02b9;
    L_0x0141:
        r0 = "UTF-8";
        r0 = r9.readString(r0);
        r8.lyrics = r0;
        goto L_0x02b9;
    L_0x014b:
        r0 = "UTF-8";
        r0 = r9.readString(r0);
        r8.grouping = r0;
        goto L_0x02b9;
    L_0x0155:
        r0 = r8.genre;
        if (r0 == 0) goto L_0x0165;
    L_0x0159:
        r0 = r8.genre;
        r0 = r0.trim();
        r0 = r0.length();
        if (r0 != 0) goto L_0x02b9;
    L_0x0165:
        r0 = "UTF-8";
        r0 = r9.readString(r0);
        r8.genre = r0;
        goto L_0x02b9;
    L_0x016f:
        r0 = r8.genre;
        if (r0 == 0) goto L_0x017f;
    L_0x0173:
        r0 = r8.genre;
        r0 = r0.trim();
        r0 = r0.length();
        if (r0 != 0) goto L_0x02b9;
    L_0x017f:
        r0 = r9.getRemaining();
        r2 = 2;
        r5 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1));
        if (r5 != 0) goto L_0x019c;
    L_0x0189:
        r0 = r9.readShort();
        r0 = r0 - r4;
        r1 = org.telegram.messenger.audioinfo.mp3.ID3v1Genre.getGenre(r0);
        if (r1 == 0) goto L_0x019a;
    L_0x0194:
        r2 = r1.getDescription();
        r8.genre = r2;
    L_0x019a:
        goto L_0x02b9;
    L_0x019c:
        r0 = "UTF-8";
        r0 = r9.readString(r0);
        r8.genre = r0;
        goto L_0x02b9;
    L_0x01a6:
        r9.skip(r5);
        r0 = r9.readShort();
        r8.disc = r0;
        r0 = r9.readShort();
        r8.discs = r0;
        goto L_0x02b9;
    L_0x01b7:
        r1 = "UTF-8";
        r1 = r9.readString(r1);
        r1 = r1.trim();
        r2 = r1.length();
        if (r2 < r0) goto L_0x02b9;
    L_0x01c7:
        r0 = r1.substring(r6, r0);	 Catch:{ NumberFormatException -> 0x01d6 }
        r0 = java.lang.Short.valueOf(r0);	 Catch:{ NumberFormatException -> 0x01d6 }
        r0 = r0.shortValue();	 Catch:{ NumberFormatException -> 0x01d6 }
        r8.year = r0;	 Catch:{ NumberFormatException -> 0x01d6 }
        goto L_0x01d7;
    L_0x01d6:
        r0 = move-exception;
    L_0x01d7:
        goto L_0x02b9;
    L_0x01d9:
        r0 = r8.copyright;
        if (r0 == 0) goto L_0x01e9;
    L_0x01dd:
        r0 = r8.copyright;
        r0 = r0.trim();
        r0 = r0.length();
        if (r0 != 0) goto L_0x02b9;
    L_0x01e9:
        r0 = "UTF-8";
        r0 = r9.readString(r0);
        r8.copyright = r0;
        goto L_0x02b9;
    L_0x01f3:
        r0 = r9.readBoolean();
        r8.compilation = r0;
        goto L_0x02b9;
    L_0x01fb:
        r0 = r9.readBytes();	 Catch:{ Exception -> 0x0277 }
        r1 = new android.graphics.BitmapFactory$Options;	 Catch:{ Exception -> 0x0277 }
        r1.<init>();	 Catch:{ Exception -> 0x0277 }
        r1.inJustDecodeBounds = r4;	 Catch:{ Exception -> 0x0277 }
        r1.inSampleSize = r4;	 Catch:{ Exception -> 0x0277 }
        r2 = r0.length;	 Catch:{ Exception -> 0x0277 }
        android.graphics.BitmapFactory.decodeByteArray(r0, r6, r2, r1);	 Catch:{ Exception -> 0x0277 }
        r2 = r1.outWidth;	 Catch:{ Exception -> 0x0277 }
        r3 = 800; // 0x320 float:1.121E-42 double:3.953E-321;
        if (r2 > r3) goto L_0x0216;
    L_0x0212:
        r2 = r1.outHeight;	 Catch:{ Exception -> 0x0277 }
        if (r2 <= r3) goto L_0x0229;
    L_0x0216:
        r2 = r1.outWidth;	 Catch:{ Exception -> 0x0277 }
        r7 = r1.outHeight;	 Catch:{ Exception -> 0x0277 }
        r2 = java.lang.Math.max(r2, r7);	 Catch:{ Exception -> 0x0277 }
    L_0x021e:
        if (r2 <= r3) goto L_0x0229;
    L_0x0220:
        r7 = r1.inSampleSize;	 Catch:{ Exception -> 0x0277 }
        r7 = r7 * r5;
        r1.inSampleSize = r7;	 Catch:{ Exception -> 0x0277 }
        r7 = r2 / 2;
        r2 = r7;
        goto L_0x021e;
    L_0x0229:
        r1.inJustDecodeBounds = r6;	 Catch:{ Exception -> 0x0277 }
        r2 = r0.length;	 Catch:{ Exception -> 0x0277 }
        r2 = android.graphics.BitmapFactory.decodeByteArray(r0, r6, r2, r1);	 Catch:{ Exception -> 0x0277 }
        r8.cover = r2;	 Catch:{ Exception -> 0x0277 }
        r2 = r8.cover;	 Catch:{ Exception -> 0x0277 }
        if (r2 == 0) goto L_0x0276;
    L_0x0236:
        r2 = r8.cover;	 Catch:{ Exception -> 0x0277 }
        r2 = r2.getWidth();	 Catch:{ Exception -> 0x0277 }
        r3 = r8.cover;	 Catch:{ Exception -> 0x0277 }
        r3 = r3.getHeight();	 Catch:{ Exception -> 0x0277 }
        r2 = java.lang.Math.max(r2, r3);	 Catch:{ Exception -> 0x0277 }
        r2 = (float) r2;	 Catch:{ Exception -> 0x0277 }
        r3 = 1123024896; // 0x42f00000 float:120.0 double:5.548480205E-315;
        r2 = r2 / r3;
        r3 = 0;
        r3 = (r2 > r3 ? 1 : (r2 == r3 ? 0 : -1));
        if (r3 <= 0) goto L_0x026a;
    L_0x024f:
        r3 = r8.cover;	 Catch:{ Exception -> 0x0277 }
        r5 = r8.cover;	 Catch:{ Exception -> 0x0277 }
        r5 = r5.getWidth();	 Catch:{ Exception -> 0x0277 }
        r5 = (float) r5;	 Catch:{ Exception -> 0x0277 }
        r5 = r5 / r2;
        r5 = (int) r5;	 Catch:{ Exception -> 0x0277 }
        r6 = r8.cover;	 Catch:{ Exception -> 0x0277 }
        r6 = r6.getHeight();	 Catch:{ Exception -> 0x0277 }
        r6 = (float) r6;	 Catch:{ Exception -> 0x0277 }
        r6 = r6 / r2;
        r6 = (int) r6;	 Catch:{ Exception -> 0x0277 }
        r3 = android.graphics.Bitmap.createScaledBitmap(r3, r5, r6, r4);	 Catch:{ Exception -> 0x0277 }
        r8.smallCover = r3;	 Catch:{ Exception -> 0x0277 }
        goto L_0x026e;
    L_0x026a:
        r3 = r8.cover;	 Catch:{ Exception -> 0x0277 }
        r8.smallCover = r3;	 Catch:{ Exception -> 0x0277 }
    L_0x026e:
        r3 = r8.smallCover;	 Catch:{ Exception -> 0x0277 }
        if (r3 != 0) goto L_0x0276;
    L_0x0272:
        r3 = r8.cover;	 Catch:{ Exception -> 0x0277 }
        r8.smallCover = r3;	 Catch:{ Exception -> 0x0277 }
    L_0x0276:
        goto L_0x02b9;
    L_0x0277:
        r0 = move-exception;
        r0.printStackTrace();
        goto L_0x02b9;
    L_0x027c:
        r0 = r8.composer;
        if (r0 == 0) goto L_0x028c;
    L_0x0280:
        r0 = r8.composer;
        r0 = r0.trim();
        r0 = r0.length();
        if (r0 != 0) goto L_0x02b9;
    L_0x028c:
        r0 = "UTF-8";
        r0 = r9.readString(r0);
        r8.composer = r0;
        goto L_0x02b9;
    L_0x0295:
        r0 = "UTF-8";
        r0 = r9.readString(r0);
        r8.comment = r0;
        goto L_0x02b9;
    L_0x029e:
        r0 = "UTF-8";
        r0 = r9.readString(r0);
        r8.artist = r0;
        goto L_0x02b9;
    L_0x02a7:
        r0 = "UTF-8";
        r0 = r9.readString(r0);
        r8.albumArtist = r0;
        goto L_0x02b9;
    L_0x02b0:
        r0 = "UTF-8";
        r0 = r9.readString(r0);
        r8.album = r0;
    L_0x02b9:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.audioinfo.m4a.M4AInfo.data(org.telegram.messenger.audioinfo.m4a.MP4Atom):void");
    }

    public short getTempo() {
        return this.tempo;
    }

    public byte getRating() {
        return this.rating;
    }

    public BigDecimal getSpeed() {
        return this.speed;
    }

    public BigDecimal getVolume() {
        return this.volume;
    }
}
