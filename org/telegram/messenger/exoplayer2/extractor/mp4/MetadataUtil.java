package org.telegram.messenger.exoplayer2.extractor.mp4;

import android.util.Log;
import com.coremedia.iso.boxes.GenreBox;
import com.coremedia.iso.boxes.RatingBox;
import org.telegram.messenger.exoplayer2.metadata.id3.ApicFrame;
import org.telegram.messenger.exoplayer2.metadata.id3.CommentFrame;
import org.telegram.messenger.exoplayer2.metadata.id3.Id3Frame;
import org.telegram.messenger.exoplayer2.metadata.id3.TextInformationFrame;
import org.telegram.messenger.exoplayer2.util.ParsableByteArray;
import org.telegram.messenger.exoplayer2.util.Util;

final class MetadataUtil {
    private static final String LANGUAGE_UNDEFINED = "und";
    private static final int SHORT_TYPE_ALBUM = Util.getIntegerCodeForString("alb");
    private static final int SHORT_TYPE_ARTIST = Util.getIntegerCodeForString("ART");
    private static final int SHORT_TYPE_COMMENT = Util.getIntegerCodeForString("cmt");
    private static final int SHORT_TYPE_COMPOSER_1 = Util.getIntegerCodeForString("com");
    private static final int SHORT_TYPE_COMPOSER_2 = Util.getIntegerCodeForString("wrt");
    private static final int SHORT_TYPE_ENCODER = Util.getIntegerCodeForString("too");
    private static final int SHORT_TYPE_GENRE = Util.getIntegerCodeForString("gen");
    private static final int SHORT_TYPE_LYRICS = Util.getIntegerCodeForString("lyr");
    private static final int SHORT_TYPE_NAME_1 = Util.getIntegerCodeForString("nam");
    private static final int SHORT_TYPE_NAME_2 = Util.getIntegerCodeForString("trk");
    private static final int SHORT_TYPE_YEAR = Util.getIntegerCodeForString("day");
    private static final String[] STANDARD_GENRES = new String[]{"Blues", "Classic Rock", "Country", "Dance", "Disco", "Funk", "Grunge", "Hip-Hop", "Jazz", "Metal", "New Age", "Oldies", "Other", "Pop", "R&B", "Rap", "Reggae", "Rock", "Techno", "Industrial", "Alternative", "Ska", "Death Metal", "Pranks", "Soundtrack", "Euro-Techno", "Ambient", "Trip-Hop", "Vocal", "Jazz+Funk", "Fusion", "Trance", "Classical", "Instrumental", "Acid", "House", "Game", "Sound Clip", "Gospel", "Noise", "AlternRock", "Bass", "Soul", "Punk", "Space", "Meditative", "Instrumental Pop", "Instrumental Rock", "Ethnic", "Gothic", "Darkwave", "Techno-Industrial", "Electronic", "Pop-Folk", "Eurodance", "Dream", "Southern Rock", "Comedy", "Cult", "Gangsta", "Top 40", "Christian Rap", "Pop/Funk", "Jungle", "Native American", "Cabaret", "New Wave", "Psychadelic", "Rave", "Showtunes", "Trailer", "Lo-Fi", "Tribal", "Acid Punk", "Acid Jazz", "Polka", "Retro", "Musical", "Rock & Roll", "Hard Rock", "Folk", "Folk-Rock", "National Folk", "Swing", "Fast Fusion", "Bebob", "Latin", "Revival", "Celtic", "Bluegrass", "Avantgarde", "Gothic Rock", "Progressive Rock", "Psychedelic Rock", "Symphonic Rock", "Slow Rock", "Big Band", "Chorus", "Easy Listening", "Acoustic", "Humour", "Speech", "Chanson", "Opera", "Chamber Music", "Sonata", "Symphony", "Booty Bass", "Primus", "Porn Groove", "Satire", "Slow Jam", "Club", "Tango", "Samba", "Folklore", "Ballad", "Power Ballad", "Rhythmic Soul", "Freestyle", "Duet", "Punk Rock", "Drum Solo", "A capella", "Euro-House", "Dance Hall", "Goa", "Drum & Bass", "Club-House", "Hardcore", "Terror", "Indie", "BritPop", "Negerpunk", "Polsk Punk", "Beat", "Christian Gangsta Rap", "Heavy Metal", "Black Metal", "Crossover", "Contemporary Christian", "Christian Rock", "Merengue", "Salsa", "Thrash Metal", "Anime", "Jpop", "Synthpop"};
    private static final String TAG = "MetadataUtil";
    private static final int TYPE_ALBUM_ARTIST = Util.getIntegerCodeForString("aART");
    private static final int TYPE_COMPILATION = Util.getIntegerCodeForString("cpil");
    private static final int TYPE_COVER_ART = Util.getIntegerCodeForString("covr");
    private static final int TYPE_DISK_NUMBER = Util.getIntegerCodeForString("disk");
    private static final int TYPE_GAPLESS_ALBUM = Util.getIntegerCodeForString("pgap");
    private static final int TYPE_GENRE = Util.getIntegerCodeForString(GenreBox.TYPE);
    private static final int TYPE_GROUPING = Util.getIntegerCodeForString("grp");
    private static final int TYPE_INTERNAL = Util.getIntegerCodeForString("----");
    private static final int TYPE_RATING = Util.getIntegerCodeForString(RatingBox.TYPE);
    private static final int TYPE_SORT_ALBUM = Util.getIntegerCodeForString("soal");
    private static final int TYPE_SORT_ALBUM_ARTIST = Util.getIntegerCodeForString("soaa");
    private static final int TYPE_SORT_ARTIST = Util.getIntegerCodeForString("soar");
    private static final int TYPE_SORT_COMPOSER = Util.getIntegerCodeForString("soco");
    private static final int TYPE_SORT_TRACK_NAME = Util.getIntegerCodeForString("sonm");
    private static final int TYPE_TEMPO = Util.getIntegerCodeForString("tmpo");
    private static final int TYPE_TRACK_NUMBER = Util.getIntegerCodeForString("trkn");
    private static final int TYPE_TV_SHOW = Util.getIntegerCodeForString("tvsh");
    private static final int TYPE_TV_SORT_SHOW = Util.getIntegerCodeForString("sosn");

    public static org.telegram.messenger.exoplayer2.metadata.Metadata.Entry parseIlstElement(org.telegram.messenger.exoplayer2.util.ParsableByteArray r1) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.exoplayer2.extractor.mp4.MetadataUtil.parseIlstElement(org.telegram.messenger.exoplayer2.util.ParsableByteArray):org.telegram.messenger.exoplayer2.metadata.Metadata$Entry
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:116)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:249)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
Caused by: java.lang.NullPointerException
*/
        /*
        r0 = r7.getPosition();
        r1 = r7.readInt();
        r1 = r1 + r0;
        r2 = r7.readInt();
        r3 = r2 >> 24;
        r3 = r3 & 255;
        r4 = 169; // 0xa9 float:2.37E-43 double:8.35E-322;
        if (r3 == r4) goto L_0x0106;
    L_0x0015:
        r4 = 65533; // 0xfffd float:9.1831E-41 double:3.23776E-319;
        if (r3 != r4) goto L_0x001c;
    L_0x001a:
        goto L_0x0106;
    L_0x001c:
        r4 = TYPE_GENRE;	 Catch:{ all -> 0x01c0 }
        if (r2 != r4) goto L_0x0028;	 Catch:{ all -> 0x01c0 }
    L_0x0020:
        r4 = parseStandardGenreAttribute(r7);	 Catch:{ all -> 0x01c0 }
        r7.setPosition(r1);
        return r4;
    L_0x0028:
        r4 = TYPE_DISK_NUMBER;	 Catch:{ all -> 0x01c0 }
        if (r2 != r4) goto L_0x0036;	 Catch:{ all -> 0x01c0 }
    L_0x002c:
        r4 = "TPOS";	 Catch:{ all -> 0x01c0 }
        r4 = parseIndexAndCountAttribute(r2, r4, r7);	 Catch:{ all -> 0x01c0 }
        r7.setPosition(r1);
        return r4;
    L_0x0036:
        r4 = TYPE_TRACK_NUMBER;	 Catch:{ all -> 0x01c0 }
        if (r2 != r4) goto L_0x0044;	 Catch:{ all -> 0x01c0 }
    L_0x003a:
        r4 = "TRCK";	 Catch:{ all -> 0x01c0 }
        r4 = parseIndexAndCountAttribute(r2, r4, r7);	 Catch:{ all -> 0x01c0 }
        r7.setPosition(r1);
        return r4;
    L_0x0044:
        r4 = TYPE_TEMPO;	 Catch:{ all -> 0x01c0 }
        r5 = 0;	 Catch:{ all -> 0x01c0 }
        r6 = 1;	 Catch:{ all -> 0x01c0 }
        if (r2 != r4) goto L_0x0054;	 Catch:{ all -> 0x01c0 }
    L_0x004a:
        r4 = "TBPM";	 Catch:{ all -> 0x01c0 }
        r4 = parseUint8Attribute(r2, r4, r7, r6, r5);	 Catch:{ all -> 0x01c0 }
        r7.setPosition(r1);
        return r4;
    L_0x0054:
        r4 = TYPE_COMPILATION;	 Catch:{ all -> 0x01c0 }
        if (r2 != r4) goto L_0x0062;	 Catch:{ all -> 0x01c0 }
    L_0x0058:
        r4 = "TCMP";	 Catch:{ all -> 0x01c0 }
        r4 = parseUint8Attribute(r2, r4, r7, r6, r6);	 Catch:{ all -> 0x01c0 }
        r7.setPosition(r1);
        return r4;
    L_0x0062:
        r4 = TYPE_COVER_ART;	 Catch:{ all -> 0x01c0 }
        if (r2 != r4) goto L_0x006e;	 Catch:{ all -> 0x01c0 }
    L_0x0066:
        r4 = parseCoverArt(r7);	 Catch:{ all -> 0x01c0 }
        r7.setPosition(r1);
        return r4;
    L_0x006e:
        r4 = TYPE_ALBUM_ARTIST;	 Catch:{ all -> 0x01c0 }
        if (r2 != r4) goto L_0x007c;	 Catch:{ all -> 0x01c0 }
    L_0x0072:
        r4 = "TPE2";	 Catch:{ all -> 0x01c0 }
        r4 = parseTextAttribute(r2, r4, r7);	 Catch:{ all -> 0x01c0 }
        r7.setPosition(r1);
        return r4;
    L_0x007c:
        r4 = TYPE_SORT_TRACK_NAME;	 Catch:{ all -> 0x01c0 }
        if (r2 != r4) goto L_0x008a;	 Catch:{ all -> 0x01c0 }
    L_0x0080:
        r4 = "TSOT";	 Catch:{ all -> 0x01c0 }
        r4 = parseTextAttribute(r2, r4, r7);	 Catch:{ all -> 0x01c0 }
        r7.setPosition(r1);
        return r4;
    L_0x008a:
        r4 = TYPE_SORT_ALBUM;	 Catch:{ all -> 0x01c0 }
        if (r2 != r4) goto L_0x0098;	 Catch:{ all -> 0x01c0 }
    L_0x008e:
        r4 = "TSO2";	 Catch:{ all -> 0x01c0 }
        r4 = parseTextAttribute(r2, r4, r7);	 Catch:{ all -> 0x01c0 }
        r7.setPosition(r1);
        return r4;
    L_0x0098:
        r4 = TYPE_SORT_ARTIST;	 Catch:{ all -> 0x01c0 }
        if (r2 != r4) goto L_0x00a6;	 Catch:{ all -> 0x01c0 }
    L_0x009c:
        r4 = "TSOA";	 Catch:{ all -> 0x01c0 }
        r4 = parseTextAttribute(r2, r4, r7);	 Catch:{ all -> 0x01c0 }
        r7.setPosition(r1);
        return r4;
    L_0x00a6:
        r4 = TYPE_SORT_ALBUM_ARTIST;	 Catch:{ all -> 0x01c0 }
        if (r2 != r4) goto L_0x00b4;	 Catch:{ all -> 0x01c0 }
    L_0x00aa:
        r4 = "TSOP";	 Catch:{ all -> 0x01c0 }
        r4 = parseTextAttribute(r2, r4, r7);	 Catch:{ all -> 0x01c0 }
        r7.setPosition(r1);
        return r4;
    L_0x00b4:
        r4 = TYPE_SORT_COMPOSER;	 Catch:{ all -> 0x01c0 }
        if (r2 != r4) goto L_0x00c2;	 Catch:{ all -> 0x01c0 }
    L_0x00b8:
        r4 = "TSOC";	 Catch:{ all -> 0x01c0 }
        r4 = parseTextAttribute(r2, r4, r7);	 Catch:{ all -> 0x01c0 }
        r7.setPosition(r1);
        return r4;
    L_0x00c2:
        r4 = TYPE_RATING;	 Catch:{ all -> 0x01c0 }
        if (r2 != r4) goto L_0x00d0;	 Catch:{ all -> 0x01c0 }
    L_0x00c6:
        r4 = "ITUNESADVISORY";	 Catch:{ all -> 0x01c0 }
        r4 = parseUint8Attribute(r2, r4, r7, r5, r5);	 Catch:{ all -> 0x01c0 }
        r7.setPosition(r1);
        return r4;
    L_0x00d0:
        r4 = TYPE_GAPLESS_ALBUM;	 Catch:{ all -> 0x01c0 }
        if (r2 != r4) goto L_0x00de;	 Catch:{ all -> 0x01c0 }
    L_0x00d4:
        r4 = "ITUNESGAPLESS";	 Catch:{ all -> 0x01c0 }
        r4 = parseUint8Attribute(r2, r4, r7, r5, r6);	 Catch:{ all -> 0x01c0 }
        r7.setPosition(r1);
        return r4;
    L_0x00de:
        r4 = TYPE_TV_SORT_SHOW;	 Catch:{ all -> 0x01c0 }
        if (r2 != r4) goto L_0x00ec;	 Catch:{ all -> 0x01c0 }
    L_0x00e2:
        r4 = "TVSHOWSORT";	 Catch:{ all -> 0x01c0 }
        r4 = parseTextAttribute(r2, r4, r7);	 Catch:{ all -> 0x01c0 }
        r7.setPosition(r1);
        return r4;
    L_0x00ec:
        r4 = TYPE_TV_SHOW;	 Catch:{ all -> 0x01c0 }
        if (r2 != r4) goto L_0x00fa;	 Catch:{ all -> 0x01c0 }
    L_0x00f0:
        r4 = "TVSHOW";	 Catch:{ all -> 0x01c0 }
        r4 = parseTextAttribute(r2, r4, r7);	 Catch:{ all -> 0x01c0 }
        r7.setPosition(r1);
        return r4;
    L_0x00fa:
        r4 = TYPE_INTERNAL;	 Catch:{ all -> 0x01c0 }
        if (r2 != r4) goto L_0x018d;	 Catch:{ all -> 0x01c0 }
    L_0x00fe:
        r4 = parseInternalAttribute(r7, r1);	 Catch:{ all -> 0x01c0 }
        r7.setPosition(r1);
        return r4;
    L_0x0106:
        r4 = 16777215; // 0xffffff float:2.3509886E-38 double:8.2890456E-317;
        r4 = r4 & r2;
        r5 = SHORT_TYPE_COMMENT;	 Catch:{ all -> 0x01c0 }
        if (r4 != r5) goto L_0x0116;	 Catch:{ all -> 0x01c0 }
    L_0x010e:
        r5 = parseCommentAttribute(r2, r7);	 Catch:{ all -> 0x01c0 }
        r7.setPosition(r1);
        return r5;
    L_0x0116:
        r5 = SHORT_TYPE_NAME_1;	 Catch:{ all -> 0x01c0 }
        if (r4 == r5) goto L_0x01b6;	 Catch:{ all -> 0x01c0 }
    L_0x011a:
        r5 = SHORT_TYPE_NAME_2;	 Catch:{ all -> 0x01c0 }
        if (r4 != r5) goto L_0x0120;	 Catch:{ all -> 0x01c0 }
    L_0x011e:
        goto L_0x01b6;	 Catch:{ all -> 0x01c0 }
    L_0x0120:
        r5 = SHORT_TYPE_COMPOSER_1;	 Catch:{ all -> 0x01c0 }
        if (r4 == r5) goto L_0x01ac;	 Catch:{ all -> 0x01c0 }
    L_0x0124:
        r5 = SHORT_TYPE_COMPOSER_2;	 Catch:{ all -> 0x01c0 }
        if (r4 != r5) goto L_0x012a;	 Catch:{ all -> 0x01c0 }
    L_0x0128:
        goto L_0x01ac;	 Catch:{ all -> 0x01c0 }
    L_0x012a:
        r5 = SHORT_TYPE_YEAR;	 Catch:{ all -> 0x01c0 }
        if (r4 != r5) goto L_0x0138;	 Catch:{ all -> 0x01c0 }
    L_0x012e:
        r5 = "TDRC";	 Catch:{ all -> 0x01c0 }
        r5 = parseTextAttribute(r2, r5, r7);	 Catch:{ all -> 0x01c0 }
        r7.setPosition(r1);
        return r5;
    L_0x0138:
        r5 = SHORT_TYPE_ARTIST;	 Catch:{ all -> 0x01c0 }
        if (r4 != r5) goto L_0x0146;	 Catch:{ all -> 0x01c0 }
    L_0x013c:
        r5 = "TPE1";	 Catch:{ all -> 0x01c0 }
        r5 = parseTextAttribute(r2, r5, r7);	 Catch:{ all -> 0x01c0 }
        r7.setPosition(r1);
        return r5;
    L_0x0146:
        r5 = SHORT_TYPE_ENCODER;	 Catch:{ all -> 0x01c0 }
        if (r4 != r5) goto L_0x0154;	 Catch:{ all -> 0x01c0 }
    L_0x014a:
        r5 = "TSSE";	 Catch:{ all -> 0x01c0 }
        r5 = parseTextAttribute(r2, r5, r7);	 Catch:{ all -> 0x01c0 }
        r7.setPosition(r1);
        return r5;
    L_0x0154:
        r5 = SHORT_TYPE_ALBUM;	 Catch:{ all -> 0x01c0 }
        if (r4 != r5) goto L_0x0162;	 Catch:{ all -> 0x01c0 }
    L_0x0158:
        r5 = "TALB";	 Catch:{ all -> 0x01c0 }
        r5 = parseTextAttribute(r2, r5, r7);	 Catch:{ all -> 0x01c0 }
        r7.setPosition(r1);
        return r5;
    L_0x0162:
        r5 = SHORT_TYPE_LYRICS;	 Catch:{ all -> 0x01c0 }
        if (r4 != r5) goto L_0x0170;	 Catch:{ all -> 0x01c0 }
    L_0x0166:
        r5 = "USLT";	 Catch:{ all -> 0x01c0 }
        r5 = parseTextAttribute(r2, r5, r7);	 Catch:{ all -> 0x01c0 }
        r7.setPosition(r1);
        return r5;
    L_0x0170:
        r5 = SHORT_TYPE_GENRE;	 Catch:{ all -> 0x01c0 }
        if (r4 != r5) goto L_0x017e;	 Catch:{ all -> 0x01c0 }
    L_0x0174:
        r5 = "TCON";	 Catch:{ all -> 0x01c0 }
        r5 = parseTextAttribute(r2, r5, r7);	 Catch:{ all -> 0x01c0 }
        r7.setPosition(r1);
        return r5;
    L_0x017e:
        r5 = TYPE_GROUPING;	 Catch:{ all -> 0x01c0 }
        if (r4 != r5) goto L_0x018c;	 Catch:{ all -> 0x01c0 }
    L_0x0182:
        r5 = "TIT1";	 Catch:{ all -> 0x01c0 }
        r5 = parseTextAttribute(r2, r5, r7);	 Catch:{ all -> 0x01c0 }
        r7.setPosition(r1);
        return r5;
    L_0x018d:
        r4 = "MetadataUtil";	 Catch:{ all -> 0x01c0 }
        r5 = new java.lang.StringBuilder;	 Catch:{ all -> 0x01c0 }
        r5.<init>();	 Catch:{ all -> 0x01c0 }
        r6 = "Skipped unknown metadata entry: ";	 Catch:{ all -> 0x01c0 }
        r5.append(r6);	 Catch:{ all -> 0x01c0 }
        r6 = org.telegram.messenger.exoplayer2.extractor.mp4.Atom.getAtomTypeString(r2);	 Catch:{ all -> 0x01c0 }
        r5.append(r6);	 Catch:{ all -> 0x01c0 }
        r5 = r5.toString();	 Catch:{ all -> 0x01c0 }
        android.util.Log.d(r4, r5);	 Catch:{ all -> 0x01c0 }
        r4 = 0;
        r7.setPosition(r1);
        return r4;
    L_0x01ac:
        r5 = "TCOM";	 Catch:{ all -> 0x01c0 }
        r5 = parseTextAttribute(r2, r5, r7);	 Catch:{ all -> 0x01c0 }
        r7.setPosition(r1);
        return r5;
    L_0x01b6:
        r5 = "TIT2";	 Catch:{ all -> 0x01c0 }
        r5 = parseTextAttribute(r2, r5, r7);	 Catch:{ all -> 0x01c0 }
        r7.setPosition(r1);
        return r5;
    L_0x01c0:
        r4 = move-exception;
        r7.setPosition(r1);
        throw r4;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.exoplayer2.extractor.mp4.MetadataUtil.parseIlstElement(org.telegram.messenger.exoplayer2.util.ParsableByteArray):org.telegram.messenger.exoplayer2.metadata.Metadata$Entry");
    }

    private MetadataUtil() {
    }

    private static TextInformationFrame parseTextAttribute(int type, String id, ParsableByteArray data) {
        int atomSize = data.readInt();
        if (data.readInt() == Atom.TYPE_data) {
            data.skipBytes(8);
            return new TextInformationFrame(id, null, data.readNullTerminatedString(atomSize - 16));
        }
        String str = TAG;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Failed to parse text attribute: ");
        stringBuilder.append(Atom.getAtomTypeString(type));
        Log.w(str, stringBuilder.toString());
        return null;
    }

    private static CommentFrame parseCommentAttribute(int type, ParsableByteArray data) {
        int atomSize = data.readInt();
        if (data.readInt() == Atom.TYPE_data) {
            data.skipBytes(8);
            String value = data.readNullTerminatedString(atomSize - 16);
            return new CommentFrame("und", value, value);
        }
        value = TAG;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Failed to parse comment attribute: ");
        stringBuilder.append(Atom.getAtomTypeString(type));
        Log.w(value, stringBuilder.toString());
        return null;
    }

    private static Id3Frame parseUint8Attribute(int type, String id, ParsableByteArray data, boolean isTextInformationFrame, boolean isBoolean) {
        int value = parseUint8AttributeValue(data);
        if (isBoolean) {
            value = Math.min(1, value);
        }
        if (value >= 0) {
            Id3Frame textInformationFrame;
            if (isTextInformationFrame) {
                textInformationFrame = new TextInformationFrame(id, null, Integer.toString(value));
            } else {
                textInformationFrame = new CommentFrame("und", id, Integer.toString(value));
            }
            return textInformationFrame;
        }
        String str = TAG;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Failed to parse uint8 attribute: ");
        stringBuilder.append(Atom.getAtomTypeString(type));
        Log.w(str, stringBuilder.toString());
        return null;
    }

    private static TextInformationFrame parseIndexAndCountAttribute(int type, String attributeName, ParsableByteArray data) {
        int atomSize = data.readInt();
        if (data.readInt() == Atom.TYPE_data && atomSize >= 22) {
            data.skipBytes(10);
            int index = data.readUnsignedShort();
            if (index > 0) {
                String value = new StringBuilder();
                value.append(TtmlNode.ANONYMOUS_REGION_ID);
                value.append(index);
                value = value.toString();
                int count = data.readUnsignedShort();
                if (count > 0) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(value);
                    stringBuilder.append("/");
                    stringBuilder.append(count);
                    value = stringBuilder.toString();
                }
                return new TextInformationFrame(attributeName, null, value);
            }
        }
        String str = TAG;
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append("Failed to parse index/count attribute: ");
        stringBuilder2.append(Atom.getAtomTypeString(type));
        Log.w(str, stringBuilder2.toString());
        return null;
    }

    private static TextInformationFrame parseStandardGenreAttribute(ParsableByteArray data) {
        int genreCode = parseUint8AttributeValue(data);
        String genreString = (genreCode <= 0 || genreCode > STANDARD_GENRES.length) ? null : STANDARD_GENRES[genreCode - 1];
        if (genreString != null) {
            return new TextInformationFrame("TCON", null, genreString);
        }
        Log.w(TAG, "Failed to parse standard genre code");
        return null;
    }

    private static ApicFrame parseCoverArt(ParsableByteArray data) {
        int atomSize = data.readInt();
        if (data.readInt() == Atom.TYPE_data) {
            int flags = Atom.parseFullAtomFlags(data.readInt());
            String mimeType = flags == 13 ? "image/jpeg" : flags == 14 ? "image/png" : null;
            if (mimeType == null) {
                String str = TAG;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Unrecognized cover art flags: ");
                stringBuilder.append(flags);
                Log.w(str, stringBuilder.toString());
                return null;
            }
            data.skipBytes(4);
            byte[] pictureData = new byte[(atomSize - 16)];
            data.readBytes(pictureData, 0, pictureData.length);
            return new ApicFrame(mimeType, null, 3, pictureData);
        }
        Log.w(TAG, "Failed to parse cover art attribute");
        return null;
    }

    private static Id3Frame parseInternalAttribute(ParsableByteArray data, int endPosition) {
        int dataAtomPosition = -1;
        String name = null;
        String domain = null;
        int dataAtomSize = -1;
        while (data.getPosition() < endPosition) {
            int atomPosition = data.getPosition();
            int atomSize = data.readInt();
            int atomType = data.readInt();
            data.skipBytes(4);
            if (atomType == Atom.TYPE_mean) {
                domain = data.readNullTerminatedString(atomSize - 12);
            } else if (atomType == Atom.TYPE_name) {
                name = data.readNullTerminatedString(atomSize - 12);
            } else {
                if (atomType == Atom.TYPE_data) {
                    dataAtomPosition = atomPosition;
                    dataAtomSize = atomSize;
                }
                data.skipBytes(atomSize - 12);
            }
        }
        if ("com.apple.iTunes".equals(domain) && "iTunSMPB".equals(name)) {
            if (dataAtomPosition != -1) {
                data.setPosition(dataAtomPosition);
                data.skipBytes(16);
                return new CommentFrame("und", name, data.readNullTerminatedString(dataAtomSize - 16));
            }
        }
        return null;
    }

    private static int parseUint8AttributeValue(ParsableByteArray data) {
        data.skipBytes(4);
        if (data.readInt() == Atom.TYPE_data) {
            data.skipBytes(8);
            return data.readUnsignedByte();
        }
        Log.w(TAG, "Failed to parse uint8 attribute value");
        return -1;
    }
}
