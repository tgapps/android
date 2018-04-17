package org.telegram.messenger.exoplayer2.extractor.mkv;

import android.util.SparseArray;
import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import org.telegram.messenger.exoplayer2.C;
import org.telegram.messenger.exoplayer2.ParserException;
import org.telegram.messenger.exoplayer2.audio.Ac3Util;
import org.telegram.messenger.exoplayer2.drm.DrmInitData;
import org.telegram.messenger.exoplayer2.drm.DrmInitData.SchemeData;
import org.telegram.messenger.exoplayer2.extractor.Extractor;
import org.telegram.messenger.exoplayer2.extractor.ExtractorInput;
import org.telegram.messenger.exoplayer2.extractor.ExtractorOutput;
import org.telegram.messenger.exoplayer2.extractor.ExtractorsFactory;
import org.telegram.messenger.exoplayer2.extractor.PositionHolder;
import org.telegram.messenger.exoplayer2.extractor.SeekMap.Unseekable;
import org.telegram.messenger.exoplayer2.extractor.TrackOutput;
import org.telegram.messenger.exoplayer2.extractor.TrackOutput.CryptoData;
import org.telegram.messenger.exoplayer2.util.LongArray;
import org.telegram.messenger.exoplayer2.util.MimeTypes;
import org.telegram.messenger.exoplayer2.util.NalUnitUtil;
import org.telegram.messenger.exoplayer2.util.ParsableByteArray;
import org.telegram.messenger.exoplayer2.util.Util;

public final class MatroskaExtractor implements Extractor {
    private static final int BLOCK_STATE_DATA = 2;
    private static final int BLOCK_STATE_HEADER = 1;
    private static final int BLOCK_STATE_START = 0;
    private static final String CODEC_ID_AAC = "A_AAC";
    private static final String CODEC_ID_AC3 = "A_AC3";
    private static final String CODEC_ID_ACM = "A_MS/ACM";
    private static final String CODEC_ID_ASS = "S_TEXT/ASS";
    private static final String CODEC_ID_DTS = "A_DTS";
    private static final String CODEC_ID_DTS_EXPRESS = "A_DTS/EXPRESS";
    private static final String CODEC_ID_DTS_LOSSLESS = "A_DTS/LOSSLESS";
    private static final String CODEC_ID_DVBSUB = "S_DVBSUB";
    private static final String CODEC_ID_E_AC3 = "A_EAC3";
    private static final String CODEC_ID_FLAC = "A_FLAC";
    private static final String CODEC_ID_FOURCC = "V_MS/VFW/FOURCC";
    private static final String CODEC_ID_H264 = "V_MPEG4/ISO/AVC";
    private static final String CODEC_ID_H265 = "V_MPEGH/ISO/HEVC";
    private static final String CODEC_ID_MP2 = "A_MPEG/L2";
    private static final String CODEC_ID_MP3 = "A_MPEG/L3";
    private static final String CODEC_ID_MPEG2 = "V_MPEG2";
    private static final String CODEC_ID_MPEG4_AP = "V_MPEG4/ISO/AP";
    private static final String CODEC_ID_MPEG4_ASP = "V_MPEG4/ISO/ASP";
    private static final String CODEC_ID_MPEG4_SP = "V_MPEG4/ISO/SP";
    private static final String CODEC_ID_OPUS = "A_OPUS";
    private static final String CODEC_ID_PCM_INT_LIT = "A_PCM/INT/LIT";
    private static final String CODEC_ID_PGS = "S_HDMV/PGS";
    private static final String CODEC_ID_SUBRIP = "S_TEXT/UTF8";
    private static final String CODEC_ID_THEORA = "V_THEORA";
    private static final String CODEC_ID_TRUEHD = "A_TRUEHD";
    private static final String CODEC_ID_VOBSUB = "S_VOBSUB";
    private static final String CODEC_ID_VORBIS = "A_VORBIS";
    private static final String CODEC_ID_VP8 = "V_VP8";
    private static final String CODEC_ID_VP9 = "V_VP9";
    private static final String DOC_TYPE_MATROSKA = "matroska";
    private static final String DOC_TYPE_WEBM = "webm";
    private static final int ENCRYPTION_IV_SIZE = 8;
    public static final ExtractorsFactory FACTORY = new ExtractorsFactory() {
        public Extractor[] createExtractors() {
            return new Extractor[]{new MatroskaExtractor()};
        }
    };
    public static final int FLAG_DISABLE_SEEK_FOR_CUES = 1;
    private static final int FOURCC_COMPRESSION_VC1 = 826496599;
    private static final int ID_AUDIO = 225;
    private static final int ID_AUDIO_BIT_DEPTH = 25188;
    private static final int ID_BLOCK = 161;
    private static final int ID_BLOCK_DURATION = 155;
    private static final int ID_BLOCK_GROUP = 160;
    private static final int ID_CHANNELS = 159;
    private static final int ID_CLUSTER = 524531317;
    private static final int ID_CODEC_DELAY = 22186;
    private static final int ID_CODEC_ID = 134;
    private static final int ID_CODEC_PRIVATE = 25506;
    private static final int ID_COLOUR = 21936;
    private static final int ID_COLOUR_PRIMARIES = 21947;
    private static final int ID_COLOUR_RANGE = 21945;
    private static final int ID_COLOUR_TRANSFER = 21946;
    private static final int ID_CONTENT_COMPRESSION = 20532;
    private static final int ID_CONTENT_COMPRESSION_ALGORITHM = 16980;
    private static final int ID_CONTENT_COMPRESSION_SETTINGS = 16981;
    private static final int ID_CONTENT_ENCODING = 25152;
    private static final int ID_CONTENT_ENCODINGS = 28032;
    private static final int ID_CONTENT_ENCODING_ORDER = 20529;
    private static final int ID_CONTENT_ENCODING_SCOPE = 20530;
    private static final int ID_CONTENT_ENCRYPTION = 20533;
    private static final int ID_CONTENT_ENCRYPTION_AES_SETTINGS = 18407;
    private static final int ID_CONTENT_ENCRYPTION_AES_SETTINGS_CIPHER_MODE = 18408;
    private static final int ID_CONTENT_ENCRYPTION_ALGORITHM = 18401;
    private static final int ID_CONTENT_ENCRYPTION_KEY_ID = 18402;
    private static final int ID_CUES = 475249515;
    private static final int ID_CUE_CLUSTER_POSITION = 241;
    private static final int ID_CUE_POINT = 187;
    private static final int ID_CUE_TIME = 179;
    private static final int ID_CUE_TRACK_POSITIONS = 183;
    private static final int ID_DEFAULT_DURATION = 2352003;
    private static final int ID_DISPLAY_HEIGHT = 21690;
    private static final int ID_DISPLAY_UNIT = 21682;
    private static final int ID_DISPLAY_WIDTH = 21680;
    private static final int ID_DOC_TYPE = 17026;
    private static final int ID_DOC_TYPE_READ_VERSION = 17029;
    private static final int ID_DURATION = 17545;
    private static final int ID_EBML = 440786851;
    private static final int ID_EBML_READ_VERSION = 17143;
    private static final int ID_FLAG_DEFAULT = 136;
    private static final int ID_FLAG_FORCED = 21930;
    private static final int ID_INFO = 357149030;
    private static final int ID_LANGUAGE = 2274716;
    private static final int ID_LUMNINANCE_MAX = 21977;
    private static final int ID_LUMNINANCE_MIN = 21978;
    private static final int ID_MASTERING_METADATA = 21968;
    private static final int ID_MAX_CLL = 21948;
    private static final int ID_MAX_FALL = 21949;
    private static final int ID_PIXEL_HEIGHT = 186;
    private static final int ID_PIXEL_WIDTH = 176;
    private static final int ID_PRIMARY_B_CHROMATICITY_X = 21973;
    private static final int ID_PRIMARY_B_CHROMATICITY_Y = 21974;
    private static final int ID_PRIMARY_G_CHROMATICITY_X = 21971;
    private static final int ID_PRIMARY_G_CHROMATICITY_Y = 21972;
    private static final int ID_PRIMARY_R_CHROMATICITY_X = 21969;
    private static final int ID_PRIMARY_R_CHROMATICITY_Y = 21970;
    private static final int ID_PROJECTION = 30320;
    private static final int ID_PROJECTION_PRIVATE = 30322;
    private static final int ID_REFERENCE_BLOCK = 251;
    private static final int ID_SAMPLING_FREQUENCY = 181;
    private static final int ID_SEEK = 19899;
    private static final int ID_SEEK_HEAD = 290298740;
    private static final int ID_SEEK_ID = 21419;
    private static final int ID_SEEK_POSITION = 21420;
    private static final int ID_SEEK_PRE_ROLL = 22203;
    private static final int ID_SEGMENT = 408125543;
    private static final int ID_SEGMENT_INFO = 357149030;
    private static final int ID_SIMPLE_BLOCK = 163;
    private static final int ID_STEREO_MODE = 21432;
    private static final int ID_TIMECODE_SCALE = 2807729;
    private static final int ID_TIME_CODE = 231;
    private static final int ID_TRACKS = 374648427;
    private static final int ID_TRACK_ENTRY = 174;
    private static final int ID_TRACK_NUMBER = 215;
    private static final int ID_TRACK_TYPE = 131;
    private static final int ID_VIDEO = 224;
    private static final int ID_WHITE_POINT_CHROMATICITY_X = 21975;
    private static final int ID_WHITE_POINT_CHROMATICITY_Y = 21976;
    private static final int LACING_EBML = 3;
    private static final int LACING_FIXED_SIZE = 2;
    private static final int LACING_NONE = 0;
    private static final int LACING_XIPH = 1;
    private static final int OPUS_MAX_INPUT_SIZE = 5760;
    private static final byte[] SSA_DIALOGUE_FORMAT = Util.getUtf8Bytes("Format: Start, End, ReadOrder, Layer, Style, Name, MarginL, MarginR, MarginV, Effect, Text");
    private static final byte[] SSA_PREFIX = new byte[]{(byte) 68, (byte) 105, (byte) 97, (byte) 108, (byte) 111, (byte) 103, (byte) 117, (byte) 101, (byte) 58, (byte) 32, (byte) 48, (byte) 58, (byte) 48, (byte) 48, (byte) 58, (byte) 48, (byte) 48, (byte) 58, (byte) 48, (byte) 48, (byte) 44, (byte) 48, (byte) 58, (byte) 48, (byte) 48, (byte) 58, (byte) 48, (byte) 48, (byte) 58, (byte) 48, (byte) 48, (byte) 44};
    private static final int SSA_PREFIX_END_TIMECODE_OFFSET = 21;
    private static final byte[] SSA_TIMECODE_EMPTY = new byte[]{(byte) 32, (byte) 32, (byte) 32, (byte) 32, (byte) 32, (byte) 32, (byte) 32, (byte) 32, (byte) 32, (byte) 32};
    private static final String SSA_TIMECODE_FORMAT = "%01d:%02d:%02d:%02d";
    private static final long SSA_TIMECODE_LAST_VALUE_SCALING_FACTOR = 10000;
    private static final byte[] SUBRIP_PREFIX = new byte[]{(byte) 49, (byte) 10, (byte) 48, (byte) 48, (byte) 58, (byte) 48, (byte) 48, (byte) 58, (byte) 48, (byte) 48, (byte) 44, (byte) 48, (byte) 48, (byte) 48, (byte) 32, (byte) 45, (byte) 45, (byte) 62, (byte) 32, (byte) 48, (byte) 48, (byte) 58, (byte) 48, (byte) 48, (byte) 58, (byte) 48, (byte) 48, (byte) 44, (byte) 48, (byte) 48, (byte) 48, (byte) 10};
    private static final int SUBRIP_PREFIX_END_TIMECODE_OFFSET = 19;
    private static final byte[] SUBRIP_TIMECODE_EMPTY = new byte[]{(byte) 32, (byte) 32, (byte) 32, (byte) 32, (byte) 32, (byte) 32, (byte) 32, (byte) 32, (byte) 32, (byte) 32, (byte) 32, (byte) 32};
    private static final String SUBRIP_TIMECODE_FORMAT = "%02d:%02d:%02d,%03d";
    private static final long SUBRIP_TIMECODE_LAST_VALUE_SCALING_FACTOR = 1000;
    private static final String TAG = "MatroskaExtractor";
    private static final int TRACK_TYPE_AUDIO = 2;
    private static final int UNSET_ENTRY_ID = -1;
    private static final int VORBIS_MAX_INPUT_SIZE = 8192;
    private static final int WAVE_FORMAT_EXTENSIBLE = 65534;
    private static final int WAVE_FORMAT_PCM = 1;
    private static final int WAVE_FORMAT_SIZE = 18;
    private static final UUID WAVE_SUBFORMAT_PCM = new UUID(72057594037932032L, -9223371306706625679L);
    private long blockDurationUs;
    private int blockFlags;
    private int blockLacingSampleCount;
    private int blockLacingSampleIndex;
    private int[] blockLacingSampleSizes;
    private int blockState;
    private long blockTimeUs;
    private int blockTrackNumber;
    private int blockTrackNumberLength;
    private long clusterTimecodeUs;
    private LongArray cueClusterPositions;
    private LongArray cueTimesUs;
    private long cuesContentPosition;
    private Track currentTrack;
    private long durationTimecode;
    private long durationUs;
    private final ParsableByteArray encryptionInitializationVector;
    private final ParsableByteArray encryptionSubsampleData;
    private ByteBuffer encryptionSubsampleDataBuffer;
    private ExtractorOutput extractorOutput;
    private final ParsableByteArray nalLength;
    private final ParsableByteArray nalStartCode;
    private final EbmlReader reader;
    private int sampleBytesRead;
    private int sampleBytesWritten;
    private int sampleCurrentNalBytesRemaining;
    private boolean sampleEncodingHandled;
    private boolean sampleInitializationVectorRead;
    private int samplePartitionCount;
    private boolean samplePartitionCountRead;
    private boolean sampleRead;
    private boolean sampleSeenReferenceBlock;
    private byte sampleSignalByte;
    private boolean sampleSignalByteRead;
    private final ParsableByteArray sampleStrippedBytes;
    private final ParsableByteArray scratch;
    private int seekEntryId;
    private final ParsableByteArray seekEntryIdBytes;
    private long seekEntryPosition;
    private boolean seekForCues;
    private final boolean seekForCuesEnabled;
    private long seekPositionAfterBuildingCues;
    private boolean seenClusterPositionForCurrentCuePoint;
    private long segmentContentPosition;
    private long segmentContentSize;
    private boolean sentSeekMap;
    private final ParsableByteArray subtitleSample;
    private long timecodeScale;
    private final SparseArray<Track> tracks;
    private final VarintReader varintReader;
    private final ParsableByteArray vorbisNumPageSamples;

    @Retention(RetentionPolicy.SOURCE)
    public @interface Flags {
    }

    private static final class Track {
        private static final int DEFAULT_MAX_CLL = 1000;
        private static final int DEFAULT_MAX_FALL = 200;
        private static final int DISPLAY_UNIT_PIXELS = 0;
        private static final int MAX_CHROMATICITY = 50000;
        public int audioBitDepth;
        public int channelCount;
        public long codecDelayNs;
        public String codecId;
        public byte[] codecPrivate;
        public int colorRange;
        public int colorSpace;
        public int colorTransfer;
        public CryptoData cryptoData;
        public int defaultSampleDurationNs;
        public int displayHeight;
        public int displayUnit;
        public int displayWidth;
        public DrmInitData drmInitData;
        public boolean flagDefault;
        public boolean flagForced;
        public boolean hasColorInfo;
        public boolean hasContentEncryption;
        public int height;
        private String language;
        public int maxContentLuminance;
        public int maxFrameAverageLuminance;
        public float maxMasteringLuminance;
        public float minMasteringLuminance;
        public int nalUnitLengthFieldLength;
        public int number;
        public TrackOutput output;
        public float primaryBChromaticityX;
        public float primaryBChromaticityY;
        public float primaryGChromaticityX;
        public float primaryGChromaticityY;
        public float primaryRChromaticityX;
        public float primaryRChromaticityY;
        public byte[] projectionData;
        public int sampleRate;
        public byte[] sampleStrippedBytes;
        public long seekPreRollNs;
        public int stereoMode;
        public TrueHdSampleRechunker trueHdSampleRechunker;
        public int type;
        public float whitePointChromaticityX;
        public float whitePointChromaticityY;
        public int width;

        public void initializeOutput(org.telegram.messenger.exoplayer2.extractor.ExtractorOutput r1, int r2) throws org.telegram.messenger.exoplayer2.ParserException {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.exoplayer2.extractor.mkv.MatroskaExtractor.Track.initializeOutput(org.telegram.messenger.exoplayer2.extractor.ExtractorOutput, int):void
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:116)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:249)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:256)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
Caused by: java.lang.NullPointerException
*/
            /*
            r0 = this;
            r0 = r28;
            r1 = -1;
            r2 = -1;
            r3 = 0;
            r4 = r0.codecId;
            r5 = r4.hashCode();
            r6 = 4;
            r7 = 8;
            r8 = 1;
            r9 = 3;
            r10 = 0;
            r11 = 2;
            r12 = -1;
            switch(r5) {
                case -2095576542: goto L_0x0158;
                case -2095575984: goto L_0x014e;
                case -1985379776: goto L_0x0143;
                case -1784763192: goto L_0x0138;
                case -1730367663: goto L_0x012d;
                case -1482641358: goto L_0x0122;
                case -1482641357: goto L_0x0117;
                case -1373388978: goto L_0x010d;
                case -933872740: goto L_0x0102;
                case -538363189: goto L_0x00f7;
                case -538363109: goto L_0x00ec;
                case -425012669: goto L_0x00e0;
                case -356037306: goto L_0x00d4;
                case 62923557: goto L_0x00c8;
                case 62923603: goto L_0x00bc;
                case 62927045: goto L_0x00b0;
                case 82338133: goto L_0x00a5;
                case 82338134: goto L_0x009a;
                case 99146302: goto L_0x008e;
                case 444813526: goto L_0x0082;
                case 542569478: goto L_0x0076;
                case 725957860: goto L_0x006a;
                case 738597099: goto L_0x005e;
                case 855502857: goto L_0x0053;
                case 1422270023: goto L_0x0047;
                case 1809237540: goto L_0x003c;
                case 1950749482: goto L_0x0030;
                case 1950789798: goto L_0x0024;
                case 1951062397: goto L_0x0018;
                default: goto L_0x0016;
            };
        L_0x0016:
            goto L_0x0162;
        L_0x0018:
            r5 = "A_OPUS";
            r4 = r4.equals(r5);
            if (r4 == 0) goto L_0x0162;
        L_0x0020:
            r4 = 11;
            goto L_0x0163;
        L_0x0024:
            r5 = "A_FLAC";
            r4 = r4.equals(r5);
            if (r4 == 0) goto L_0x0162;
        L_0x002c:
            r4 = 21;
            goto L_0x0163;
        L_0x0030:
            r5 = "A_EAC3";
            r4 = r4.equals(r5);
            if (r4 == 0) goto L_0x0162;
        L_0x0038:
            r4 = 16;
            goto L_0x0163;
        L_0x003c:
            r5 = "V_MPEG2";
            r4 = r4.equals(r5);
            if (r4 == 0) goto L_0x0162;
        L_0x0044:
            r4 = r11;
            goto L_0x0163;
        L_0x0047:
            r5 = "S_TEXT/UTF8";
            r4 = r4.equals(r5);
            if (r4 == 0) goto L_0x0162;
        L_0x004f:
            r4 = 24;
            goto L_0x0163;
        L_0x0053:
            r5 = "V_MPEGH/ISO/HEVC";
            r4 = r4.equals(r5);
            if (r4 == 0) goto L_0x0162;
        L_0x005b:
            r4 = 7;
            goto L_0x0163;
        L_0x005e:
            r5 = "S_TEXT/ASS";
            r4 = r4.equals(r5);
            if (r4 == 0) goto L_0x0162;
        L_0x0066:
            r4 = 25;
            goto L_0x0163;
        L_0x006a:
            r5 = "A_PCM/INT/LIT";
            r4 = r4.equals(r5);
            if (r4 == 0) goto L_0x0162;
        L_0x0072:
            r4 = 23;
            goto L_0x0163;
        L_0x0076:
            r5 = "A_DTS/EXPRESS";
            r4 = r4.equals(r5);
            if (r4 == 0) goto L_0x0162;
        L_0x007e:
            r4 = 19;
            goto L_0x0163;
        L_0x0082:
            r5 = "V_THEORA";
            r4 = r4.equals(r5);
            if (r4 == 0) goto L_0x0162;
        L_0x008a:
            r4 = 9;
            goto L_0x0163;
        L_0x008e:
            r5 = "S_HDMV/PGS";
            r4 = r4.equals(r5);
            if (r4 == 0) goto L_0x0162;
        L_0x0096:
            r4 = 27;
            goto L_0x0163;
        L_0x009a:
            r5 = "V_VP9";
            r4 = r4.equals(r5);
            if (r4 == 0) goto L_0x0162;
        L_0x00a2:
            r4 = r8;
            goto L_0x0163;
        L_0x00a5:
            r5 = "V_VP8";
            r4 = r4.equals(r5);
            if (r4 == 0) goto L_0x0162;
        L_0x00ad:
            r4 = r10;
            goto L_0x0163;
        L_0x00b0:
            r5 = "A_DTS";
            r4 = r4.equals(r5);
            if (r4 == 0) goto L_0x0162;
        L_0x00b8:
            r4 = 18;
            goto L_0x0163;
        L_0x00bc:
            r5 = "A_AC3";
            r4 = r4.equals(r5);
            if (r4 == 0) goto L_0x0162;
        L_0x00c4:
            r4 = 15;
            goto L_0x0163;
        L_0x00c8:
            r5 = "A_AAC";
            r4 = r4.equals(r5);
            if (r4 == 0) goto L_0x0162;
        L_0x00d0:
            r4 = 12;
            goto L_0x0163;
        L_0x00d4:
            r5 = "A_DTS/LOSSLESS";
            r4 = r4.equals(r5);
            if (r4 == 0) goto L_0x0162;
        L_0x00dc:
            r4 = 20;
            goto L_0x0163;
        L_0x00e0:
            r5 = "S_VOBSUB";
            r4 = r4.equals(r5);
            if (r4 == 0) goto L_0x0162;
        L_0x00e8:
            r4 = 26;
            goto L_0x0163;
        L_0x00ec:
            r5 = "V_MPEG4/ISO/AVC";
            r4 = r4.equals(r5);
            if (r4 == 0) goto L_0x0162;
        L_0x00f4:
            r4 = 6;
            goto L_0x0163;
        L_0x00f7:
            r5 = "V_MPEG4/ISO/ASP";
            r4 = r4.equals(r5);
            if (r4 == 0) goto L_0x0162;
        L_0x00ff:
            r4 = r6;
            goto L_0x0163;
        L_0x0102:
            r5 = "S_DVBSUB";
            r4 = r4.equals(r5);
            if (r4 == 0) goto L_0x0162;
        L_0x010a:
            r4 = 28;
            goto L_0x0163;
        L_0x010d:
            r5 = "V_MS/VFW/FOURCC";
            r4 = r4.equals(r5);
            if (r4 == 0) goto L_0x0162;
        L_0x0115:
            r4 = r7;
            goto L_0x0163;
        L_0x0117:
            r5 = "A_MPEG/L3";
            r4 = r4.equals(r5);
            if (r4 == 0) goto L_0x0162;
        L_0x011f:
            r4 = 14;
            goto L_0x0163;
        L_0x0122:
            r5 = "A_MPEG/L2";
            r4 = r4.equals(r5);
            if (r4 == 0) goto L_0x0162;
        L_0x012a:
            r4 = 13;
            goto L_0x0163;
        L_0x012d:
            r5 = "A_VORBIS";
            r4 = r4.equals(r5);
            if (r4 == 0) goto L_0x0162;
        L_0x0135:
            r4 = 10;
            goto L_0x0163;
        L_0x0138:
            r5 = "A_TRUEHD";
            r4 = r4.equals(r5);
            if (r4 == 0) goto L_0x0162;
        L_0x0140:
            r4 = 17;
            goto L_0x0163;
        L_0x0143:
            r5 = "A_MS/ACM";
            r4 = r4.equals(r5);
            if (r4 == 0) goto L_0x0162;
        L_0x014b:
            r4 = 22;
            goto L_0x0163;
        L_0x014e:
            r5 = "V_MPEG4/ISO/SP";
            r4 = r4.equals(r5);
            if (r4 == 0) goto L_0x0162;
        L_0x0156:
            r4 = r9;
            goto L_0x0163;
        L_0x0158:
            r5 = "V_MPEG4/ISO/AP";
            r4 = r4.equals(r5);
            if (r4 == 0) goto L_0x0162;
        L_0x0160:
            r4 = 5;
            goto L_0x0163;
        L_0x0162:
            r4 = r12;
        L_0x0163:
            switch(r4) {
                case 0: goto L_0x0313;
                case 1: goto L_0x0310;
                case 2: goto L_0x030d;
                case 3: goto L_0x02fd;
                case 4: goto L_0x02fd;
                case 5: goto L_0x02fd;
                case 6: goto L_0x02e9;
                case 7: goto L_0x02d5;
                case 8: goto L_0x02bb;
                case 9: goto L_0x02b8;
                case 10: goto L_0x02ad;
                case 11: goto L_0x0268;
                case 12: goto L_0x025e;
                case 13: goto L_0x0258;
                case 14: goto L_0x0252;
                case 15: goto L_0x024e;
                case 16: goto L_0x024a;
                case 17: goto L_0x023f;
                case 18: goto L_0x023b;
                case 19: goto L_0x023b;
                case 20: goto L_0x0237;
                case 21: goto L_0x022d;
                case 22: goto L_0x01d7;
                case 23: goto L_0x01a8;
                case 24: goto L_0x01a4;
                case 25: goto L_0x01a0;
                case 26: goto L_0x0196;
                case 27: goto L_0x0192;
                case 28: goto L_0x0170;
                default: goto L_0x0166;
            };
        L_0x0166:
            r9 = r29;
            r4 = new org.telegram.messenger.exoplayer2.ParserException;
            r5 = "Unrecognized codec identifier.";
            r4.<init>(r5);
            throw r4;
        L_0x0170:
            r4 = "application/dvbsubs";
            r5 = new byte[r6];
            r6 = r0.codecPrivate;
            r6 = r6[r10];
            r5[r10] = r6;
            r6 = r0.codecPrivate;
            r6 = r6[r8];
            r5[r8] = r6;
            r6 = r0.codecPrivate;
            r6 = r6[r11];
            r5[r11] = r6;
            r6 = r0.codecPrivate;
            r6 = r6[r9];
            r5[r9] = r6;
            r3 = java.util.Collections.singletonList(r5);
            goto L_0x0316;
        L_0x0192:
            r4 = "application/pgs";
            goto L_0x0316;
        L_0x0196:
            r4 = "application/vobsub";
            r5 = r0.codecPrivate;
            r3 = java.util.Collections.singletonList(r5);
            goto L_0x0316;
        L_0x01a0:
            r4 = "text/x-ssa";
            goto L_0x0316;
        L_0x01a4:
            r4 = "application/x-subrip";
            goto L_0x0316;
        L_0x01a8:
            r4 = "audio/raw";
            r5 = r0.audioBitDepth;
            r2 = org.telegram.messenger.exoplayer2.util.Util.getPcmEncoding(r5);
            if (r2 != 0) goto L_0x0316;
            r2 = -1;
            r4 = "audio/x-unknown";
            r5 = "MatroskaExtractor";
            r6 = new java.lang.StringBuilder;
            r6.<init>();
            r7 = "Unsupported PCM bit depth: ";
            r6.append(r7);
            r7 = r0.audioBitDepth;
            r6.append(r7);
            r7 = ". Setting mimeType to ";
            r6.append(r7);
            r6.append(r4);
            r6 = r6.toString();
            android.util.Log.w(r5, r6);
            goto L_0x0316;
        L_0x01d7:
            r4 = "audio/raw";
            r5 = new org.telegram.messenger.exoplayer2.util.ParsableByteArray;
            r6 = r0.codecPrivate;
            r5.<init>(r6);
            r5 = parseMsAcmCodecPrivate(r5);
            if (r5 == 0) goto L_0x0213;
            r5 = r0.audioBitDepth;
            r2 = org.telegram.messenger.exoplayer2.util.Util.getPcmEncoding(r5);
            if (r2 != 0) goto L_0x0316;
            r2 = -1;
            r4 = "audio/x-unknown";
            r5 = "MatroskaExtractor";
            r6 = new java.lang.StringBuilder;
            r6.<init>();
            r7 = "Unsupported PCM bit depth: ";
            r6.append(r7);
            r7 = r0.audioBitDepth;
            r6.append(r7);
            r7 = ". Setting mimeType to ";
            r6.append(r7);
            r6.append(r4);
            r6 = r6.toString();
            android.util.Log.w(r5, r6);
            goto L_0x0316;
            r4 = "audio/x-unknown";
            r5 = "MatroskaExtractor";
            r6 = new java.lang.StringBuilder;
            r6.<init>();
            r7 = "Non-PCM MS/ACM is unsupported. Setting mimeType to ";
            r6.append(r7);
            r6.append(r4);
            r6 = r6.toString();
            android.util.Log.w(r5, r6);
            goto L_0x0316;
        L_0x022d:
            r4 = "audio/flac";
            r5 = r0.codecPrivate;
            r3 = java.util.Collections.singletonList(r5);
            goto L_0x0316;
        L_0x0237:
            r4 = "audio/vnd.dts.hd";
            goto L_0x0316;
        L_0x023b:
            r4 = "audio/vnd.dts";
            goto L_0x0316;
        L_0x023f:
            r4 = "audio/true-hd";
            r5 = new org.telegram.messenger.exoplayer2.extractor.mkv.MatroskaExtractor$TrueHdSampleRechunker;
            r5.<init>();
            r0.trueHdSampleRechunker = r5;
            goto L_0x0316;
        L_0x024a:
            r4 = "audio/eac3";
            goto L_0x0316;
        L_0x024e:
            r4 = "audio/ac3";
            goto L_0x0316;
        L_0x0252:
            r4 = "audio/mpeg";
            r1 = 4096; // 0x1000 float:5.74E-42 double:2.0237E-320;
            goto L_0x0316;
        L_0x0258:
            r4 = "audio/mpeg-L2";
            r1 = 4096; // 0x1000 float:5.74E-42 double:2.0237E-320;
            goto L_0x0316;
        L_0x025e:
            r4 = "audio/mp4a-latm";
            r5 = r0.codecPrivate;
            r3 = java.util.Collections.singletonList(r5);
            goto L_0x0316;
        L_0x0268:
            r4 = "audio/opus";
            r1 = 5760; // 0x1680 float:8.071E-42 double:2.846E-320;
            r5 = new java.util.ArrayList;
            r5.<init>(r9);
            r3 = r5;
            r5 = r0.codecPrivate;
            r3.add(r5);
            r5 = java.nio.ByteBuffer.allocate(r7);
            r6 = java.nio.ByteOrder.nativeOrder();
            r5 = r5.order(r6);
            r8 = r0.codecDelayNs;
            r5 = r5.putLong(r8);
            r5 = r5.array();
            r3.add(r5);
            r5 = java.nio.ByteBuffer.allocate(r7);
            r6 = java.nio.ByteOrder.nativeOrder();
            r5 = r5.order(r6);
            r6 = r0.seekPreRollNs;
            r5 = r5.putLong(r6);
            r5 = r5.array();
            r3.add(r5);
            goto L_0x0316;
        L_0x02ad:
            r4 = "audio/vorbis";
            r1 = 8192; // 0x2000 float:1.14794E-41 double:4.0474E-320;
            r5 = r0.codecPrivate;
            r3 = parseVorbisCodecPrivate(r5);
            goto L_0x0316;
        L_0x02b8:
            r4 = "video/x-unknown";
            goto L_0x0316;
        L_0x02bb:
            r4 = new org.telegram.messenger.exoplayer2.util.ParsableByteArray;
            r5 = r0.codecPrivate;
            r4.<init>(r5);
            r3 = parseFourCcVc1Private(r4);
            if (r3 == 0) goto L_0x02cb;
            r4 = "video/wvc1";
            goto L_0x0316;
            r4 = "MatroskaExtractor";
            r5 = "Unsupported FourCC. Setting mimeType to video/x-unknown";
            android.util.Log.w(r4, r5);
            r4 = "video/x-unknown";
            goto L_0x0316;
        L_0x02d5:
            r4 = "video/hevc";
            r5 = new org.telegram.messenger.exoplayer2.util.ParsableByteArray;
            r6 = r0.codecPrivate;
            r5.<init>(r6);
            r5 = org.telegram.messenger.exoplayer2.video.HevcConfig.parse(r5);
            r3 = r5.initializationData;
            r6 = r5.nalUnitLengthFieldLength;
            r0.nalUnitLengthFieldLength = r6;
            goto L_0x0316;
        L_0x02e9:
            r4 = "video/avc";
            r5 = new org.telegram.messenger.exoplayer2.util.ParsableByteArray;
            r6 = r0.codecPrivate;
            r5.<init>(r6);
            r5 = org.telegram.messenger.exoplayer2.video.AvcConfig.parse(r5);
            r3 = r5.initializationData;
            r6 = r5.nalUnitLengthFieldLength;
            r0.nalUnitLengthFieldLength = r6;
            goto L_0x0316;
        L_0x02fd:
            r4 = "video/mp4v-es";
            r5 = r0.codecPrivate;
            if (r5 != 0) goto L_0x0305;
            r5 = 0;
            goto L_0x030b;
            r5 = r0.codecPrivate;
            r5 = java.util.Collections.singletonList(r5);
            r3 = r5;
            goto L_0x0316;
        L_0x030d:
            r4 = "video/mpeg2";
            goto L_0x0316;
        L_0x0310:
            r4 = "video/x-vnd.on2.vp9";
            goto L_0x0316;
        L_0x0313:
            r4 = "video/x-vnd.on2.vp8";
            r5 = 0;
            r6 = r0.flagDefault;
            r5 = r5 | r6;
            r6 = r0.flagForced;
            if (r6 == 0) goto L_0x0321;
            r10 = r11;
            r5 = r5 | r10;
            r6 = org.telegram.messenger.exoplayer2.util.MimeTypes.isAudio(r4);
            if (r6 == 0) goto L_0x034f;
            r6 = 1;
            r13 = java.lang.Integer.toString(r30);
            r15 = 0;
            r16 = -1;
            r7 = r0.channelCount;
            r8 = r0.sampleRate;
            r9 = r0.drmInitData;
            r10 = r0.language;
            r14 = r4;
            r17 = r1;
            r18 = r7;
            r19 = r8;
            r20 = r2;
            r21 = r3;
            r22 = r9;
            r23 = r5;
            r24 = r10;
            r7 = org.telegram.messenger.exoplayer2.Format.createAudioSampleFormat(r13, r14, r15, r16, r17, r18, r19, r20, r21, r22, r23, r24);
            goto L_0x045b;
            r6 = org.telegram.messenger.exoplayer2.util.MimeTypes.isVideo(r4);
            if (r6 == 0) goto L_0x03cb;
            r6 = 2;
            r7 = r0.displayUnit;
            if (r7 != 0) goto L_0x0370;
            r7 = r0.displayWidth;
            if (r7 != r12) goto L_0x0361;
            r7 = r0.width;
            goto L_0x0363;
            r7 = r0.displayWidth;
            r0.displayWidth = r7;
            r7 = r0.displayHeight;
            if (r7 != r12) goto L_0x036c;
            r7 = r0.height;
            goto L_0x036e;
            r7 = r0.displayHeight;
            r0.displayHeight = r7;
            r7 = -1082130432; // 0xffffffffbf800000 float:-1.0 double:NaN;
            r8 = r0.displayWidth;
            if (r8 == r12) goto L_0x0388;
            r8 = r0.displayHeight;
            if (r8 == r12) goto L_0x0388;
            r8 = r0.height;
            r9 = r0.displayWidth;
            r8 = r8 * r9;
            r8 = (float) r8;
            r9 = r0.width;
            r10 = r0.displayHeight;
            r9 = r9 * r10;
            r9 = (float) r9;
            r8 = r8 / r9;
            r7 = r8;
            r8 = 0;
            r9 = r0.hasColorInfo;
            if (r9 == 0) goto L_0x039d;
            r9 = r28.getHdrStaticInfo();
            r10 = new org.telegram.messenger.exoplayer2.video.ColorInfo;
            r11 = r0.colorSpace;
            r12 = r0.colorRange;
            r13 = r0.colorTransfer;
            r10.<init>(r11, r12, r13, r9);
            r8 = r10;
            r13 = java.lang.Integer.toString(r30);
            r15 = 0;
            r16 = -1;
            r9 = r0.width;
            r10 = r0.height;
            r20 = -1082130432; // 0xffffffffbf800000 float:-1.0 double:NaN;
            r22 = -1;
            r11 = r0.projectionData;
            r12 = r0.stereoMode;
            r14 = r0.drmInitData;
            r27 = r14;
            r14 = r4;
            r17 = r1;
            r18 = r9;
            r19 = r10;
            r21 = r3;
            r23 = r7;
            r24 = r11;
            r25 = r12;
            r26 = r8;
            r7 = org.telegram.messenger.exoplayer2.Format.createVideoSampleFormat(r13, r14, r15, r16, r17, r18, r19, r20, r21, r22, r23, r24, r25, r26, r27);
            goto L_0x045b;
            r6 = "application/x-subrip";
            r6 = r6.equals(r4);
            if (r6 == 0) goto L_0x03e2;
            r6 = 3;
            r7 = java.lang.Integer.toString(r30);
            r8 = r0.language;
            r9 = r0.drmInitData;
            r7 = org.telegram.messenger.exoplayer2.Format.createTextSampleFormat(r7, r4, r5, r8, r9);
            goto L_0x034d;
            r6 = "text/x-ssa";
            r6 = r6.equals(r4);
            if (r6 == 0) goto L_0x041e;
            r6 = 3;
            r7 = new java.util.ArrayList;
            r7.<init>(r11);
            r3 = r7;
            r7 = org.telegram.messenger.exoplayer2.extractor.mkv.MatroskaExtractor.SSA_DIALOGUE_FORMAT;
            r3.add(r7);
            r7 = r0.codecPrivate;
            r3.add(r7);
            r13 = java.lang.Integer.toString(r30);
            r15 = 0;
            r16 = -1;
            r7 = r0.language;
            r19 = -1;
            r8 = r0.drmInitData;
            r21 = 9223372036854775807; // 0x7fffffffffffffff float:NaN double:NaN;
            r14 = r4;
            r17 = r5;
            r18 = r7;
            r20 = r8;
            r23 = r3;
            r7 = org.telegram.messenger.exoplayer2.Format.createTextSampleFormat(r13, r14, r15, r16, r17, r18, r19, r20, r21, r23);
            goto L_0x034d;
            r6 = "application/vobsub";
            r6 = r6.equals(r4);
            if (r6 != 0) goto L_0x043f;
            r6 = "application/pgs";
            r6 = r6.equals(r4);
            if (r6 != 0) goto L_0x043f;
            r6 = "application/dvbsubs";
            r6 = r6.equals(r4);
            if (r6 == 0) goto L_0x0437;
            goto L_0x043f;
            r6 = new org.telegram.messenger.exoplayer2.ParserException;
            r7 = "Unexpected MIME type.";
            r6.<init>(r7);
            throw r6;
            r6 = 3;
            r13 = java.lang.Integer.toString(r30);
            r15 = 0;
            r16 = -1;
            r7 = r0.language;
            r8 = r0.drmInitData;
            r14 = r4;
            r17 = r5;
            r18 = r3;
            r19 = r7;
            r20 = r8;
            r7 = org.telegram.messenger.exoplayer2.Format.createImageSampleFormat(r13, r14, r15, r16, r17, r18, r19, r20);
            goto L_0x034d;
            r8 = r0.number;
            r9 = r29;
            r8 = r9.track(r8, r6);
            r0.output = r8;
            r8 = r0.output;
            r8.format(r7);
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.exoplayer2.extractor.mkv.MatroskaExtractor.Track.initializeOutput(org.telegram.messenger.exoplayer2.extractor.ExtractorOutput, int):void");
        }

        private Track() {
            this.width = -1;
            this.height = -1;
            this.displayWidth = -1;
            this.displayHeight = -1;
            this.displayUnit = 0;
            this.projectionData = null;
            this.stereoMode = -1;
            this.hasColorInfo = false;
            this.colorSpace = -1;
            this.colorTransfer = -1;
            this.colorRange = -1;
            this.maxContentLuminance = DEFAULT_MAX_CLL;
            this.maxFrameAverageLuminance = 200;
            this.primaryRChromaticityX = -1.0f;
            this.primaryRChromaticityY = -1.0f;
            this.primaryGChromaticityX = -1.0f;
            this.primaryGChromaticityY = -1.0f;
            this.primaryBChromaticityX = -1.0f;
            this.primaryBChromaticityY = -1.0f;
            this.whitePointChromaticityX = -1.0f;
            this.whitePointChromaticityY = -1.0f;
            this.maxMasteringLuminance = -1.0f;
            this.minMasteringLuminance = -1.0f;
            this.channelCount = 1;
            this.audioBitDepth = -1;
            this.sampleRate = 8000;
            this.codecDelayNs = 0;
            this.seekPreRollNs = 0;
            this.flagDefault = true;
            this.language = "eng";
        }

        public void outputPendingSampleMetadata() {
            if (this.trueHdSampleRechunker != null) {
                this.trueHdSampleRechunker.outputPendingSampleMetadata(this);
            }
        }

        public void reset() {
            if (this.trueHdSampleRechunker != null) {
                this.trueHdSampleRechunker.reset();
            }
        }

        private byte[] getHdrStaticInfo() {
            if (!(this.primaryRChromaticityX == -1.0f || this.primaryRChromaticityY == -1.0f || this.primaryGChromaticityX == -1.0f || this.primaryGChromaticityY == -1.0f || this.primaryBChromaticityX == -1.0f || this.primaryBChromaticityY == -1.0f || this.whitePointChromaticityX == -1.0f || this.whitePointChromaticityY == -1.0f || this.maxMasteringLuminance == -1.0f)) {
                if (this.minMasteringLuminance != -1.0f) {
                    byte[] hdrStaticInfoData = new byte[25];
                    ByteBuffer hdrStaticInfo = ByteBuffer.wrap(hdrStaticInfoData);
                    hdrStaticInfo.put((byte) 0);
                    hdrStaticInfo.putShort((short) ((int) ((this.primaryRChromaticityX * 50000.0f) + 0.5f)));
                    hdrStaticInfo.putShort((short) ((int) ((this.primaryRChromaticityY * 50000.0f) + 0.5f)));
                    hdrStaticInfo.putShort((short) ((int) ((this.primaryGChromaticityX * 50000.0f) + 0.5f)));
                    hdrStaticInfo.putShort((short) ((int) ((this.primaryGChromaticityY * 50000.0f) + 0.5f)));
                    hdrStaticInfo.putShort((short) ((int) ((this.primaryBChromaticityX * 50000.0f) + 0.5f)));
                    hdrStaticInfo.putShort((short) ((int) ((this.primaryBChromaticityY * 50000.0f) + 0.5f)));
                    hdrStaticInfo.putShort((short) ((int) ((this.whitePointChromaticityX * 50000.0f) + 0.5f)));
                    hdrStaticInfo.putShort((short) ((int) ((this.whitePointChromaticityY * 50000.0f) + 0.5f)));
                    hdrStaticInfo.putShort((short) ((int) (this.maxMasteringLuminance + 0.5f)));
                    hdrStaticInfo.putShort((short) ((int) (this.minMasteringLuminance + 0.5f)));
                    hdrStaticInfo.putShort((short) this.maxContentLuminance);
                    hdrStaticInfo.putShort((short) this.maxFrameAverageLuminance);
                    return hdrStaticInfoData;
                }
            }
            return null;
        }

        private static List<byte[]> parseFourCcVc1Private(ParsableByteArray buffer) throws ParserException {
            try {
                buffer.skipBytes(16);
                if (buffer.readLittleEndianUnsignedInt() != 826496599) {
                    return null;
                }
                int startOffset = buffer.getPosition() + 20;
                byte[] bufferData = buffer.data;
                int offset = startOffset;
                while (offset < bufferData.length - 4) {
                    if (bufferData[offset] == (byte) 0 && bufferData[offset + 1] == (byte) 0 && bufferData[offset + 2] == (byte) 1 && bufferData[offset + 3] == (byte) 15) {
                        return Collections.singletonList(Arrays.copyOfRange(bufferData, offset, bufferData.length));
                    }
                    offset++;
                }
                throw new ParserException("Failed to find FourCC VC1 initialization data");
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new ParserException("Error parsing FourCC VC1 codec private");
            }
        }

        private static List<byte[]> parseVorbisCodecPrivate(byte[] codecPrivate) throws ParserException {
            try {
                if (codecPrivate[0] != (byte) 2) {
                    throw new ParserException("Error parsing vorbis codec private");
                }
                int offset = 1;
                int vorbisInfoLength = 0;
                while (codecPrivate[offset] == (byte) -1) {
                    vorbisInfoLength += 255;
                    offset++;
                }
                int offset2 = offset + 1;
                vorbisInfoLength += codecPrivate[offset];
                offset = 0;
                while (codecPrivate[offset2] == (byte) -1) {
                    offset += 255;
                    offset2++;
                }
                int offset3 = offset2 + 1;
                offset += codecPrivate[offset2];
                if (codecPrivate[offset3] != (byte) 1) {
                    throw new ParserException("Error parsing vorbis codec private");
                }
                byte[] vorbisInfo = new byte[vorbisInfoLength];
                System.arraycopy(codecPrivate, offset3, vorbisInfo, 0, vorbisInfoLength);
                offset3 += vorbisInfoLength;
                if (codecPrivate[offset3] != (byte) 3) {
                    throw new ParserException("Error parsing vorbis codec private");
                }
                offset3 += offset;
                if (codecPrivate[offset3] != (byte) 5) {
                    throw new ParserException("Error parsing vorbis codec private");
                }
                byte[] vorbisBooks = new byte[(codecPrivate.length - offset3)];
                System.arraycopy(codecPrivate, offset3, vorbisBooks, 0, codecPrivate.length - offset3);
                List<byte[]> initializationData = new ArrayList(2);
                initializationData.add(vorbisInfo);
                initializationData.add(vorbisBooks);
                return initializationData;
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new ParserException("Error parsing vorbis codec private");
            }
        }

        private static boolean parseMsAcmCodecPrivate(ParsableByteArray buffer) throws ParserException {
            try {
                int formatTag = buffer.readLittleEndianUnsignedShort();
                boolean z = true;
                if (formatTag == 1) {
                    return true;
                }
                if (formatTag != MatroskaExtractor.WAVE_FORMAT_EXTENSIBLE) {
                    return false;
                }
                buffer.setPosition(24);
                if (buffer.readLong() != MatroskaExtractor.WAVE_SUBFORMAT_PCM.getMostSignificantBits() || buffer.readLong() != MatroskaExtractor.WAVE_SUBFORMAT_PCM.getLeastSignificantBits()) {
                    z = false;
                }
                return z;
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new ParserException("Error parsing MS/ACM codec private");
            }
        }
    }

    private static final class TrueHdSampleRechunker {
        private int blockFlags;
        private int chunkSize;
        private boolean foundSyncframe;
        private int sampleCount;
        private final byte[] syncframePrefix = new byte[12];
        private long timeUs;

        public void reset() {
            this.foundSyncframe = false;
        }

        public void startSample(ExtractorInput input, int blockFlags, int size) throws IOException, InterruptedException {
            if (!this.foundSyncframe) {
                input.peekFully(this.syncframePrefix, 0, 12);
                input.resetPeekPosition();
                if (Ac3Util.parseTrueHdSyncframeAudioSampleCount(this.syncframePrefix) != -1) {
                    this.foundSyncframe = true;
                    this.sampleCount = 0;
                } else {
                    return;
                }
            }
            if (this.sampleCount == 0) {
                this.blockFlags = blockFlags;
                this.chunkSize = 0;
            }
            this.chunkSize += size;
        }

        public void sampleMetadata(Track track, long timeUs) {
            if (this.foundSyncframe) {
                int i = this.sampleCount;
                this.sampleCount = i + 1;
                if (i == 0) {
                    this.timeUs = timeUs;
                }
                if (this.sampleCount >= 8) {
                    track.output.sampleMetadata(this.timeUs, this.blockFlags, this.chunkSize, 0, track.cryptoData);
                    this.sampleCount = 0;
                }
            }
        }

        public void outputPendingSampleMetadata(Track track) {
            if (this.foundSyncframe && this.sampleCount > 0) {
                track.output.sampleMetadata(this.timeUs, this.blockFlags, this.chunkSize, 0, track.cryptoData);
                this.sampleCount = 0;
            }
        }
    }

    private final class InnerEbmlReaderOutput implements EbmlReaderOutput {
        private InnerEbmlReaderOutput() {
        }

        public int getElementType(int id) {
            return MatroskaExtractor.this.getElementType(id);
        }

        public boolean isLevel1Element(int id) {
            return MatroskaExtractor.this.isLevel1Element(id);
        }

        public void startMasterElement(int id, long contentPosition, long contentSize) throws ParserException {
            MatroskaExtractor.this.startMasterElement(id, contentPosition, contentSize);
        }

        public void endMasterElement(int id) throws ParserException {
            MatroskaExtractor.this.endMasterElement(id);
        }

        public void integerElement(int id, long value) throws ParserException {
            MatroskaExtractor.this.integerElement(id, value);
        }

        public void floatElement(int id, double value) throws ParserException {
            MatroskaExtractor.this.floatElement(id, value);
        }

        public void stringElement(int id, String value) throws ParserException {
            MatroskaExtractor.this.stringElement(id, value);
        }

        public void binaryElement(int id, int contentsSize, ExtractorInput input) throws IOException, InterruptedException {
            MatroskaExtractor.this.binaryElement(id, contentsSize, input);
        }
    }

    private org.telegram.messenger.exoplayer2.extractor.SeekMap buildSeekMap() {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.exoplayer2.extractor.mkv.MatroskaExtractor.buildSeekMap():org.telegram.messenger.exoplayer2.extractor.SeekMap
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
        r0 = r14.segmentContentPosition;
        r2 = -1;
        r4 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1));
        r0 = 0;
        if (r4 == 0) goto L_0x00a0;
    L_0x0009:
        r1 = r14.durationUs;
        r3 = -9223372036854775807; // 0x8000000000000001 float:1.4E-45 double:-4.9E-324;
        r5 = (r1 > r3 ? 1 : (r1 == r3 ? 0 : -1));
        if (r5 == 0) goto L_0x00a0;
    L_0x0014:
        r1 = r14.cueTimesUs;
        if (r1 == 0) goto L_0x00a0;
    L_0x0018:
        r1 = r14.cueTimesUs;
        r1 = r1.size();
        if (r1 == 0) goto L_0x00a0;
    L_0x0020:
        r1 = r14.cueClusterPositions;
        if (r1 == 0) goto L_0x00a0;
    L_0x0024:
        r1 = r14.cueClusterPositions;
        r1 = r1.size();
        r2 = r14.cueTimesUs;
        r2 = r2.size();
        if (r1 == r2) goto L_0x0033;
    L_0x0032:
        goto L_0x00a0;
    L_0x0033:
        r1 = r14.cueTimesUs;
        r1 = r1.size();
        r2 = new int[r1];
        r3 = new long[r1];
        r4 = new long[r1];
        r5 = new long[r1];
        r6 = 0;
        r7 = r6;
        if (r7 >= r1) goto L_0x005c;
    L_0x0045:
        r8 = r14.cueTimesUs;
        r8 = r8.get(r7);
        r5[r7] = r8;
        r8 = r14.segmentContentPosition;
        r10 = r14.cueClusterPositions;
        r10 = r10.get(r7);
        r12 = r8 + r10;
        r3[r7] = r12;
        r7 = r7 + 1;
        goto L_0x0043;
        r7 = r1 + -1;
        if (r6 >= r7) goto L_0x0079;
        r7 = r6 + 1;
        r7 = r3[r7];
        r9 = r3[r6];
        r11 = r7 - r9;
        r7 = (int) r11;
        r2[r6] = r7;
        r7 = r6 + 1;
        r7 = r5[r7];
        r9 = r5[r6];
        r11 = r7 - r9;
        r4[r6] = r11;
        r6 = r6 + 1;
        goto L_0x005d;
        r6 = r1 + -1;
        r7 = r14.segmentContentPosition;
        r9 = r14.segmentContentSize;
        r11 = r7 + r9;
        r7 = r1 + -1;
        r7 = r3[r7];
        r9 = r11 - r7;
        r7 = (int) r9;
        r2[r6] = r7;
        r6 = r1 + -1;
        r7 = r14.durationUs;
        r9 = r1 + -1;
        r9 = r5[r9];
        r11 = r7 - r9;
        r4[r6] = r11;
        r14.cueTimesUs = r0;
        r14.cueClusterPositions = r0;
        r0 = new org.telegram.messenger.exoplayer2.extractor.ChunkIndex;
        r0.<init>(r2, r3, r4, r5);
        return r0;
    L_0x00a0:
        r14.cueTimesUs = r0;
        r14.cueClusterPositions = r0;
        r0 = new org.telegram.messenger.exoplayer2.extractor.SeekMap$Unseekable;
        r1 = r14.durationUs;
        r0.<init>(r1);
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.exoplayer2.extractor.mkv.MatroskaExtractor.buildSeekMap():org.telegram.messenger.exoplayer2.extractor.SeekMap");
    }

    private void writeSampleData(org.telegram.messenger.exoplayer2.extractor.ExtractorInput r1, org.telegram.messenger.exoplayer2.extractor.mkv.MatroskaExtractor.Track r2, int r3) throws java.io.IOException, java.lang.InterruptedException {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.exoplayer2.extractor.mkv.MatroskaExtractor.writeSampleData(org.telegram.messenger.exoplayer2.extractor.ExtractorInput, org.telegram.messenger.exoplayer2.extractor.mkv.MatroskaExtractor$Track, int):void
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
        r1 = r18;
        r2 = r19;
        r3 = r20;
        r4 = "S_TEXT/UTF8";
        r5 = r2.codecId;
        r4 = r4.equals(r5);
        if (r4 == 0) goto L_0x0018;
    L_0x0012:
        r4 = SUBRIP_PREFIX;
        r0.writeSubtitleSampleData(r1, r4, r3);
        return;
    L_0x0018:
        r4 = "S_TEXT/ASS";
        r5 = r2.codecId;
        r4 = r4.equals(r5);
        if (r4 == 0) goto L_0x0028;
    L_0x0022:
        r4 = SSA_PREFIX;
        r0.writeSubtitleSampleData(r1, r4, r3);
        return;
    L_0x0028:
        r4 = r2.output;
        r5 = r0.sampleEncodingHandled;
        r6 = 4;
        r7 = 2;
        r8 = 1;
        r9 = 0;
        if (r5 != 0) goto L_0x0187;
    L_0x0032:
        r5 = r2.hasContentEncryption;
        if (r5 == 0) goto L_0x0176;
    L_0x0036:
        r5 = r0.blockFlags;
        r10 = -1073741825; // 0xffffffffbfffffff float:-1.9999999 double:NaN;
        r5 = r5 & r10;
        r0.blockFlags = r5;
        r5 = r0.sampleSignalByteRead;
        r10 = 128; // 0x80 float:1.794E-43 double:6.32E-322;
        if (r5 != 0) goto L_0x006b;
    L_0x0044:
        r5 = r0.scratch;
        r5 = r5.data;
        r1.readFully(r5, r9, r8);
        r5 = r0.sampleBytesRead;
        r5 = r5 + r8;
        r0.sampleBytesRead = r5;
        r5 = r0.scratch;
        r5 = r5.data;
        r5 = r5[r9];
        r5 = r5 & r10;
        if (r5 != r10) goto L_0x0061;
    L_0x0059:
        r5 = new org.telegram.messenger.exoplayer2.ParserException;
        r6 = "Extension bit is set in signal byte";
        r5.<init>(r6);
        throw r5;
    L_0x0061:
        r5 = r0.scratch;
        r5 = r5.data;
        r5 = r5[r9];
        r0.sampleSignalByte = r5;
        r0.sampleSignalByteRead = r8;
    L_0x006b:
        r5 = r0.sampleSignalByte;
        r5 = r5 & r8;
        if (r5 != r8) goto L_0x0072;
    L_0x0070:
        r5 = r8;
        goto L_0x0073;
    L_0x0072:
        r5 = r9;
    L_0x0073:
        if (r5 == 0) goto L_0x0175;
    L_0x0075:
        r11 = r0.sampleSignalByte;
        r11 = r11 & r7;
        if (r11 != r7) goto L_0x007c;
    L_0x007a:
        r11 = r8;
        goto L_0x007d;
    L_0x007c:
        r11 = r9;
    L_0x007d:
        r12 = r0.blockFlags;
        r13 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r12 = r12 | r13;
        r0.blockFlags = r12;
        r12 = r0.sampleInitializationVectorRead;
        if (r12 != 0) goto L_0x00c2;
    L_0x0088:
        r12 = r0.encryptionInitializationVector;
        r12 = r12.data;
        r13 = 8;
        r1.readFully(r12, r9, r13);
        r12 = r0.sampleBytesRead;
        r12 = r12 + r13;
        r0.sampleBytesRead = r12;
        r0.sampleInitializationVectorRead = r8;
        r12 = r0.scratch;
        r12 = r12.data;
        if (r11 == 0) goto L_0x009f;
    L_0x009e:
        goto L_0x00a0;
    L_0x009f:
        r10 = r9;
    L_0x00a0:
        r10 = r10 | r13;
        r10 = (byte) r10;
        r12[r9] = r10;
        r10 = r0.scratch;
        r10.setPosition(r9);
        r10 = r0.scratch;
        r4.sampleData(r10, r8);
        r10 = r0.sampleBytesWritten;
        r10 = r10 + r8;
        r0.sampleBytesWritten = r10;
        r10 = r0.encryptionInitializationVector;
        r10.setPosition(r9);
        r10 = r0.encryptionInitializationVector;
        r4.sampleData(r10, r13);
        r10 = r0.sampleBytesWritten;
        r10 = r10 + r13;
        r0.sampleBytesWritten = r10;
    L_0x00c2:
        if (r11 == 0) goto L_0x0175;
    L_0x00c4:
        r10 = r0.samplePartitionCountRead;
        if (r10 != 0) goto L_0x00e3;
    L_0x00c8:
        r10 = r0.scratch;
        r10 = r10.data;
        r1.readFully(r10, r9, r8);
        r10 = r0.sampleBytesRead;
        r10 = r10 + r8;
        r0.sampleBytesRead = r10;
        r10 = r0.scratch;
        r10.setPosition(r9);
        r10 = r0.scratch;
        r10 = r10.readUnsignedByte();
        r0.samplePartitionCount = r10;
        r0.samplePartitionCountRead = r8;
    L_0x00e3:
        r10 = r0.samplePartitionCount;
        r10 = r10 * r6;
        r12 = r0.scratch;
        r12.reset(r10);
        r12 = r0.scratch;
        r12 = r12.data;
        r1.readFully(r12, r9, r10);
        r12 = r0.sampleBytesRead;
        r12 = r12 + r10;
        r0.sampleBytesRead = r12;
        r12 = r0.samplePartitionCount;
        r12 = r12 / r7;
        r12 = r12 + r8;
        r12 = (short) r12;
        r13 = 6;
        r13 = r13 * r12;
        r13 = r13 + r7;
        r14 = r0.encryptionSubsampleDataBuffer;
        if (r14 == 0) goto L_0x010b;
    L_0x0103:
        r14 = r0.encryptionSubsampleDataBuffer;
        r14 = r14.capacity();
        if (r14 >= r13) goto L_0x0111;
    L_0x010b:
        r14 = java.nio.ByteBuffer.allocate(r13);
        r0.encryptionSubsampleDataBuffer = r14;
    L_0x0111:
        r14 = r0.encryptionSubsampleDataBuffer;
        r14.position(r9);
        r14 = r0.encryptionSubsampleDataBuffer;
        r14.putShort(r12);
        r14 = 0;
        r15 = r14;
        r14 = r9;
    L_0x011e:
        r6 = r0.samplePartitionCount;
        if (r14 >= r6) goto L_0x0143;
    L_0x0122:
        r6 = r15;
        r9 = r0.scratch;
        r15 = r9.readUnsignedIntToInt();
        r9 = r14 % 2;
        if (r9 != 0) goto L_0x0136;
    L_0x012d:
        r9 = r0.encryptionSubsampleDataBuffer;
        r8 = r15 - r6;
        r8 = (short) r8;
        r9.putShort(r8);
        goto L_0x013d;
    L_0x0136:
        r8 = r0.encryptionSubsampleDataBuffer;
        r9 = r15 - r6;
        r8.putInt(r9);
    L_0x013d:
        r14 = r14 + 1;
        r6 = 4;
        r8 = 1;
        r9 = 0;
        goto L_0x011e;
    L_0x0143:
        r6 = r0.sampleBytesRead;
        r6 = r3 - r6;
        r6 = r6 - r15;
        r8 = r0.samplePartitionCount;
        r8 = r8 % r7;
        r9 = 1;
        if (r8 != r9) goto L_0x0154;
    L_0x014e:
        r8 = r0.encryptionSubsampleDataBuffer;
        r8.putInt(r6);
        goto L_0x0160;
    L_0x0154:
        r8 = r0.encryptionSubsampleDataBuffer;
        r9 = (short) r6;
        r8.putShort(r9);
        r8 = r0.encryptionSubsampleDataBuffer;
        r9 = 0;
        r8.putInt(r9);
    L_0x0160:
        r8 = r0.encryptionSubsampleData;
        r9 = r0.encryptionSubsampleDataBuffer;
        r9 = r9.array();
        r8.reset(r9, r13);
        r8 = r0.encryptionSubsampleData;
        r4.sampleData(r8, r13);
        r8 = r0.sampleBytesWritten;
        r8 = r8 + r13;
        r0.sampleBytesWritten = r8;
    L_0x0175:
        goto L_0x0184;
    L_0x0176:
        r5 = r2.sampleStrippedBytes;
        if (r5 == 0) goto L_0x0184;
    L_0x017a:
        r5 = r0.sampleStrippedBytes;
        r6 = r2.sampleStrippedBytes;
        r8 = r2.sampleStrippedBytes;
        r8 = r8.length;
        r5.reset(r6, r8);
    L_0x0184:
        r5 = 1;
        r0.sampleEncodingHandled = r5;
    L_0x0187:
        r5 = r0.sampleStrippedBytes;
        r5 = r5.limit();
        r3 = r3 + r5;
        r5 = "V_MPEG4/ISO/AVC";
        r6 = r2.codecId;
        r5 = r5.equals(r6);
        if (r5 != 0) goto L_0x01c8;
    L_0x0198:
        r5 = "V_MPEGH/ISO/HEVC";
        r6 = r2.codecId;
        r5 = r5.equals(r6);
        if (r5 == 0) goto L_0x01a3;
    L_0x01a2:
        goto L_0x01c8;
    L_0x01a3:
        r5 = r2.trueHdSampleRechunker;
        if (r5 == 0) goto L_0x01bc;
    L_0x01a7:
        r5 = r0.sampleStrippedBytes;
        r5 = r5.limit();
        if (r5 != 0) goto L_0x01b1;
    L_0x01af:
        r5 = 1;
        goto L_0x01b2;
    L_0x01b1:
        r5 = 0;
    L_0x01b2:
        org.telegram.messenger.exoplayer2.util.Assertions.checkState(r5);
        r5 = r2.trueHdSampleRechunker;
        r6 = r0.blockFlags;
        r5.startSample(r1, r6, r3);
    L_0x01bc:
        r5 = r0.sampleBytesRead;
        if (r5 >= r3) goto L_0x0212;
    L_0x01c0:
        r5 = r0.sampleBytesRead;
        r5 = r3 - r5;
        r0.readToOutput(r1, r4, r5);
        goto L_0x01bc;
    L_0x01c8:
        r5 = r0.nalLength;
        r5 = r5.data;
        r6 = 0;
        r5[r6] = r6;
        r8 = 1;
        r5[r8] = r6;
        r5[r7] = r6;
        r6 = r2.nalUnitLengthFieldLength;
        r7 = r2.nalUnitLengthFieldLength;
        r8 = 4;
        r7 = 4 - r7;
        r8 = r0.sampleBytesRead;
        if (r8 >= r3) goto L_0x0211;
    L_0x01df:
        r8 = r0.sampleCurrentNalBytesRemaining;
        if (r8 != 0) goto L_0x0205;
        r0.readToTarget(r1, r5, r7, r6);
        r8 = r0.nalLength;
        r9 = 0;
        r8.setPosition(r9);
        r8 = r0.nalLength;
        r8 = r8.readUnsignedIntToInt();
        r0.sampleCurrentNalBytesRemaining = r8;
        r8 = r0.nalStartCode;
        r8.setPosition(r9);
        r8 = r0.nalStartCode;
        r9 = 4;
        r4.sampleData(r8, r9);
        r8 = r0.sampleBytesWritten;
        r8 = r8 + r9;
        r0.sampleBytesWritten = r8;
        goto L_0x01db;
        r8 = r0.sampleCurrentNalBytesRemaining;
        r9 = r0.sampleCurrentNalBytesRemaining;
        r9 = r0.readToOutput(r1, r4, r9);
        r8 = r8 - r9;
        r0.sampleCurrentNalBytesRemaining = r8;
        goto L_0x01db;
    L_0x0212:
        r5 = "A_VORBIS";
        r6 = r2.codecId;
        r5 = r5.equals(r6);
        if (r5 == 0) goto L_0x022d;
        r5 = r0.vorbisNumPageSamples;
        r6 = 0;
        r5.setPosition(r6);
        r5 = r0.vorbisNumPageSamples;
        r6 = 4;
        r4.sampleData(r5, r6);
        r5 = r0.sampleBytesWritten;
        r5 = r5 + r6;
        r0.sampleBytesWritten = r5;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.exoplayer2.extractor.mkv.MatroskaExtractor.writeSampleData(org.telegram.messenger.exoplayer2.extractor.ExtractorInput, org.telegram.messenger.exoplayer2.extractor.mkv.MatroskaExtractor$Track, int):void");
    }

    public MatroskaExtractor() {
        this(0);
    }

    public MatroskaExtractor(int flags) {
        this(new DefaultEbmlReader(), flags);
    }

    MatroskaExtractor(EbmlReader reader, int flags) {
        this.segmentContentPosition = -1;
        this.timecodeScale = C.TIME_UNSET;
        this.durationTimecode = C.TIME_UNSET;
        this.durationUs = C.TIME_UNSET;
        this.cuesContentPosition = -1;
        this.seekPositionAfterBuildingCues = -1;
        this.clusterTimecodeUs = C.TIME_UNSET;
        this.reader = reader;
        this.reader.init(new InnerEbmlReaderOutput());
        this.seekForCuesEnabled = (flags & 1) == 0;
        this.varintReader = new VarintReader();
        this.tracks = new SparseArray();
        this.scratch = new ParsableByteArray(4);
        this.vorbisNumPageSamples = new ParsableByteArray(ByteBuffer.allocate(4).putInt(-1).array());
        this.seekEntryIdBytes = new ParsableByteArray(4);
        this.nalStartCode = new ParsableByteArray(NalUnitUtil.NAL_START_CODE);
        this.nalLength = new ParsableByteArray(4);
        this.sampleStrippedBytes = new ParsableByteArray();
        this.subtitleSample = new ParsableByteArray();
        this.encryptionInitializationVector = new ParsableByteArray(8);
        this.encryptionSubsampleData = new ParsableByteArray();
    }

    public boolean sniff(ExtractorInput input) throws IOException, InterruptedException {
        return new Sniffer().sniff(input);
    }

    public void init(ExtractorOutput output) {
        this.extractorOutput = output;
    }

    public void seek(long position, long timeUs) {
        this.clusterTimecodeUs = C.TIME_UNSET;
        int i = 0;
        this.blockState = 0;
        this.reader.reset();
        this.varintReader.reset();
        resetSample();
        while (i < this.tracks.size()) {
            ((Track) this.tracks.valueAt(i)).reset();
            i++;
        }
    }

    public void release() {
    }

    public int read(ExtractorInput input, PositionHolder seekPosition) throws IOException, InterruptedException {
        int i = 0;
        this.sampleRead = false;
        boolean continueReading = true;
        while (continueReading && !this.sampleRead) {
            continueReading = this.reader.read(input);
            if (continueReading && maybeSeekForCues(seekPosition, input.getPosition())) {
                return 1;
            }
        }
        if (continueReading) {
            return 0;
        }
        while (i < this.tracks.size()) {
            ((Track) this.tracks.valueAt(i)).outputPendingSampleMetadata();
            i++;
        }
        return -1;
    }

    int getElementType(int id) {
        switch (id) {
            case ID_TRACK_TYPE /*131*/:
            case ID_FLAG_DEFAULT /*136*/:
            case ID_BLOCK_DURATION /*155*/:
            case ID_CHANNELS /*159*/:
            case ID_PIXEL_WIDTH /*176*/:
            case ID_CUE_TIME /*179*/:
            case ID_PIXEL_HEIGHT /*186*/:
            case ID_TRACK_NUMBER /*215*/:
            case ID_TIME_CODE /*231*/:
            case ID_CUE_CLUSTER_POSITION /*241*/:
            case ID_REFERENCE_BLOCK /*251*/:
            case ID_CONTENT_COMPRESSION_ALGORITHM /*16980*/:
            case ID_DOC_TYPE_READ_VERSION /*17029*/:
            case ID_EBML_READ_VERSION /*17143*/:
            case ID_CONTENT_ENCRYPTION_ALGORITHM /*18401*/:
            case ID_CONTENT_ENCRYPTION_AES_SETTINGS_CIPHER_MODE /*18408*/:
            case ID_CONTENT_ENCODING_ORDER /*20529*/:
            case ID_CONTENT_ENCODING_SCOPE /*20530*/:
            case ID_SEEK_POSITION /*21420*/:
            case ID_STEREO_MODE /*21432*/:
            case ID_DISPLAY_WIDTH /*21680*/:
            case ID_DISPLAY_UNIT /*21682*/:
            case ID_DISPLAY_HEIGHT /*21690*/:
            case ID_FLAG_FORCED /*21930*/:
            case ID_COLOUR_RANGE /*21945*/:
            case ID_COLOUR_TRANSFER /*21946*/:
            case ID_COLOUR_PRIMARIES /*21947*/:
            case ID_MAX_CLL /*21948*/:
            case ID_MAX_FALL /*21949*/:
            case ID_CODEC_DELAY /*22186*/:
            case ID_SEEK_PRE_ROLL /*22203*/:
            case ID_AUDIO_BIT_DEPTH /*25188*/:
            case ID_DEFAULT_DURATION /*2352003*/:
            case ID_TIMECODE_SCALE /*2807729*/:
                return 2;
            case 134:
            case ID_DOC_TYPE /*17026*/:
            case ID_LANGUAGE /*2274716*/:
                return 3;
            case ID_BLOCK_GROUP /*160*/:
            case ID_TRACK_ENTRY /*174*/:
            case ID_CUE_TRACK_POSITIONS /*183*/:
            case ID_CUE_POINT /*187*/:
            case 224:
            case ID_AUDIO /*225*/:
            case ID_CONTENT_ENCRYPTION_AES_SETTINGS /*18407*/:
            case ID_SEEK /*19899*/:
            case ID_CONTENT_COMPRESSION /*20532*/:
            case ID_CONTENT_ENCRYPTION /*20533*/:
            case ID_COLOUR /*21936*/:
            case ID_MASTERING_METADATA /*21968*/:
            case ID_CONTENT_ENCODING /*25152*/:
            case ID_CONTENT_ENCODINGS /*28032*/:
            case ID_PROJECTION /*30320*/:
            case ID_SEEK_HEAD /*290298740*/:
            case 357149030:
            case ID_TRACKS /*374648427*/:
            case ID_SEGMENT /*408125543*/:
            case ID_EBML /*440786851*/:
            case ID_CUES /*475249515*/:
            case ID_CLUSTER /*524531317*/:
                return 1;
            case ID_BLOCK /*161*/:
            case ID_SIMPLE_BLOCK /*163*/:
            case ID_CONTENT_COMPRESSION_SETTINGS /*16981*/:
            case ID_CONTENT_ENCRYPTION_KEY_ID /*18402*/:
            case ID_SEEK_ID /*21419*/:
            case ID_CODEC_PRIVATE /*25506*/:
            case ID_PROJECTION_PRIVATE /*30322*/:
                return 4;
            case ID_SAMPLING_FREQUENCY /*181*/:
            case ID_DURATION /*17545*/:
            case ID_PRIMARY_R_CHROMATICITY_X /*21969*/:
            case ID_PRIMARY_R_CHROMATICITY_Y /*21970*/:
            case ID_PRIMARY_G_CHROMATICITY_X /*21971*/:
            case ID_PRIMARY_G_CHROMATICITY_Y /*21972*/:
            case ID_PRIMARY_B_CHROMATICITY_X /*21973*/:
            case ID_PRIMARY_B_CHROMATICITY_Y /*21974*/:
            case ID_WHITE_POINT_CHROMATICITY_X /*21975*/:
            case ID_WHITE_POINT_CHROMATICITY_Y /*21976*/:
            case ID_LUMNINANCE_MAX /*21977*/:
            case ID_LUMNINANCE_MIN /*21978*/:
                return 5;
            default:
                return 0;
        }
    }

    boolean isLevel1Element(int id) {
        if (!(id == 357149030 || id == ID_CLUSTER || id == ID_CUES)) {
            if (id != ID_TRACKS) {
                return false;
            }
        }
        return true;
    }

    void startMasterElement(int id, long contentPosition, long contentSize) throws ParserException {
        if (id == ID_BLOCK_GROUP) {
            this.sampleSeenReferenceBlock = false;
        } else if (id == ID_TRACK_ENTRY) {
            this.currentTrack = new Track();
        } else if (id == ID_CUE_POINT) {
            this.seenClusterPositionForCurrentCuePoint = false;
        } else if (id == ID_SEEK) {
            this.seekEntryId = -1;
            this.seekEntryPosition = -1;
        } else if (id == ID_CONTENT_ENCRYPTION) {
            this.currentTrack.hasContentEncryption = true;
        } else if (id == ID_MASTERING_METADATA) {
            this.currentTrack.hasColorInfo = true;
        } else if (id == ID_CONTENT_ENCODING) {
        } else {
            if (id != ID_SEGMENT) {
                if (id == ID_CUES) {
                    this.cueTimesUs = new LongArray();
                    this.cueClusterPositions = new LongArray();
                } else if (id == ID_CLUSTER) {
                    if (!this.sentSeekMap) {
                        if (!this.seekForCuesEnabled || this.cuesContentPosition == -1) {
                            this.extractorOutput.seekMap(new Unseekable(this.durationUs));
                            this.sentSeekMap = true;
                            return;
                        }
                        this.seekForCues = true;
                    }
                }
            } else if (this.segmentContentPosition == -1 || this.segmentContentPosition == contentPosition) {
                this.segmentContentPosition = contentPosition;
                this.segmentContentSize = contentSize;
            } else {
                throw new ParserException("Multiple Segment elements not supported");
            }
        }
    }

    void endMasterElement(int id) throws ParserException {
        if (id != ID_BLOCK_GROUP) {
            if (id == ID_TRACK_ENTRY) {
                if (isCodecSupported(this.currentTrack.codecId)) {
                    this.currentTrack.initializeOutput(this.extractorOutput, this.currentTrack.number);
                    this.tracks.put(this.currentTrack.number, this.currentTrack);
                }
                this.currentTrack = null;
            } else if (id == ID_SEEK) {
                if (this.seekEntryId != -1) {
                    if (this.seekEntryPosition != -1) {
                        if (this.seekEntryId == ID_CUES) {
                            this.cuesContentPosition = this.seekEntryPosition;
                        }
                    }
                }
                throw new ParserException("Mandatory element SeekID or SeekPosition not found");
            } else if (id != ID_CONTENT_ENCODING) {
                if (id != ID_CONTENT_ENCODINGS) {
                    if (id == 357149030) {
                        if (this.timecodeScale == C.TIME_UNSET) {
                            this.timecodeScale = C.MICROS_PER_SECOND;
                        }
                        if (this.durationTimecode != C.TIME_UNSET) {
                            this.durationUs = scaleTimecodeToUs(this.durationTimecode);
                        }
                    } else if (id != ID_TRACKS) {
                        if (id == ID_CUES) {
                            if (!this.sentSeekMap) {
                                this.extractorOutput.seekMap(buildSeekMap());
                                this.sentSeekMap = true;
                            }
                        }
                    } else if (this.tracks.size() == 0) {
                        throw new ParserException("No valid tracks were found");
                    } else {
                        this.extractorOutput.endTracks();
                    }
                } else if (this.currentTrack.hasContentEncryption && this.currentTrack.sampleStrippedBytes != null) {
                    throw new ParserException("Combining encryption and compression is not supported");
                }
            } else if (this.currentTrack.hasContentEncryption) {
                if (this.currentTrack.cryptoData == null) {
                    throw new ParserException("Encrypted Track found but ContentEncKeyID was not found");
                }
                this.currentTrack.drmInitData = new DrmInitData(new SchemeData(C.UUID_NIL, MimeTypes.VIDEO_WEBM, this.currentTrack.cryptoData.encryptionKey));
            }
        } else if (this.blockState == 2) {
            if (!this.sampleSeenReferenceBlock) {
                this.blockFlags |= 1;
            }
            commitSampleToOutput((Track) this.tracks.get(this.blockTrackNumber), this.blockTimeUs);
            this.blockState = 0;
        }
    }

    void integerElement(int id, long value) throws ParserException {
        boolean z = false;
        Track track;
        StringBuilder stringBuilder;
        int i;
        switch (id) {
            case ID_TRACK_TYPE /*131*/:
                this.currentTrack.type = (int) value;
                return;
            case ID_FLAG_DEFAULT /*136*/:
                track = this.currentTrack;
                if (value == 1) {
                    z = true;
                }
                track.flagForced = z;
                return;
            case ID_BLOCK_DURATION /*155*/:
                this.blockDurationUs = scaleTimecodeToUs(value);
                return;
            case ID_CHANNELS /*159*/:
                this.currentTrack.channelCount = (int) value;
                return;
            case ID_PIXEL_WIDTH /*176*/:
                this.currentTrack.width = (int) value;
                return;
            case ID_CUE_TIME /*179*/:
                this.cueTimesUs.add(scaleTimecodeToUs(value));
                return;
            case ID_PIXEL_HEIGHT /*186*/:
                this.currentTrack.height = (int) value;
                return;
            case ID_TRACK_NUMBER /*215*/:
                this.currentTrack.number = (int) value;
                return;
            case ID_TIME_CODE /*231*/:
                this.clusterTimecodeUs = scaleTimecodeToUs(value);
                return;
            case ID_CUE_CLUSTER_POSITION /*241*/:
                if (!this.seenClusterPositionForCurrentCuePoint) {
                    this.cueClusterPositions.add(value);
                    this.seenClusterPositionForCurrentCuePoint = true;
                    return;
                }
                return;
            case ID_REFERENCE_BLOCK /*251*/:
                this.sampleSeenReferenceBlock = true;
                return;
            case ID_CONTENT_COMPRESSION_ALGORITHM /*16980*/:
                if (value != 3) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("ContentCompAlgo ");
                    stringBuilder.append(value);
                    stringBuilder.append(" not supported");
                    throw new ParserException(stringBuilder.toString());
                }
                return;
            case ID_DOC_TYPE_READ_VERSION /*17029*/:
                if (value < 1 || value > 2) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("DocTypeReadVersion ");
                    stringBuilder.append(value);
                    stringBuilder.append(" not supported");
                    throw new ParserException(stringBuilder.toString());
                }
                return;
            case ID_EBML_READ_VERSION /*17143*/:
                if (value != 1) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("EBMLReadVersion ");
                    stringBuilder.append(value);
                    stringBuilder.append(" not supported");
                    throw new ParserException(stringBuilder.toString());
                }
                return;
            case ID_CONTENT_ENCRYPTION_ALGORITHM /*18401*/:
                if (value != 5) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("ContentEncAlgo ");
                    stringBuilder.append(value);
                    stringBuilder.append(" not supported");
                    throw new ParserException(stringBuilder.toString());
                }
                return;
            case ID_CONTENT_ENCRYPTION_AES_SETTINGS_CIPHER_MODE /*18408*/:
                if (value != 1) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("AESSettingsCipherMode ");
                    stringBuilder.append(value);
                    stringBuilder.append(" not supported");
                    throw new ParserException(stringBuilder.toString());
                }
                return;
            case ID_CONTENT_ENCODING_ORDER /*20529*/:
                if (value != 0) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("ContentEncodingOrder ");
                    stringBuilder.append(value);
                    stringBuilder.append(" not supported");
                    throw new ParserException(stringBuilder.toString());
                }
                return;
            case ID_CONTENT_ENCODING_SCOPE /*20530*/:
                if (value != 1) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("ContentEncodingScope ");
                    stringBuilder.append(value);
                    stringBuilder.append(" not supported");
                    throw new ParserException(stringBuilder.toString());
                }
                return;
            case ID_SEEK_POSITION /*21420*/:
                this.seekEntryPosition = value + this.segmentContentPosition;
                return;
            case ID_STEREO_MODE /*21432*/:
                int layout = (int) value;
                if (layout == 3) {
                    this.currentTrack.stereoMode = 1;
                    return;
                } else if (layout != 15) {
                    switch (layout) {
                        case 0:
                            this.currentTrack.stereoMode = 0;
                            return;
                        case 1:
                            this.currentTrack.stereoMode = 2;
                            return;
                        default:
                            return;
                    }
                } else {
                    this.currentTrack.stereoMode = 3;
                    return;
                }
            case ID_DISPLAY_WIDTH /*21680*/:
                this.currentTrack.displayWidth = (int) value;
                return;
            case ID_DISPLAY_UNIT /*21682*/:
                this.currentTrack.displayUnit = (int) value;
                return;
            case ID_DISPLAY_HEIGHT /*21690*/:
                this.currentTrack.displayHeight = (int) value;
                return;
            case ID_FLAG_FORCED /*21930*/:
                track = this.currentTrack;
                if (value == 1) {
                    z = true;
                }
                track.flagDefault = z;
                return;
            case ID_COLOUR_RANGE /*21945*/:
                switch ((int) value) {
                    case 1:
                        this.currentTrack.colorRange = 2;
                        return;
                    case 2:
                        this.currentTrack.colorRange = 1;
                        return;
                    default:
                        return;
                }
            case ID_COLOUR_TRANSFER /*21946*/:
                i = (int) value;
                if (i != 1) {
                    if (i == 16) {
                        this.currentTrack.colorTransfer = 6;
                        return;
                    } else if (i != 18) {
                        switch (i) {
                            case 6:
                            case 7:
                                break;
                            default:
                                return;
                        }
                    } else {
                        this.currentTrack.colorTransfer = 7;
                        return;
                    }
                }
                this.currentTrack.colorTransfer = 3;
                return;
            case ID_COLOUR_PRIMARIES /*21947*/:
                this.currentTrack.hasColorInfo = true;
                i = (int) value;
                if (i == 1) {
                    this.currentTrack.colorSpace = 1;
                    return;
                } else if (i != 9) {
                    switch (i) {
                        case 4:
                        case 5:
                        case 6:
                        case 7:
                            this.currentTrack.colorSpace = 2;
                            return;
                        default:
                            return;
                    }
                } else {
                    this.currentTrack.colorSpace = 6;
                    return;
                }
            case ID_MAX_CLL /*21948*/:
                this.currentTrack.maxContentLuminance = (int) value;
                return;
            case ID_MAX_FALL /*21949*/:
                this.currentTrack.maxFrameAverageLuminance = (int) value;
                return;
            case ID_CODEC_DELAY /*22186*/:
                this.currentTrack.codecDelayNs = value;
                return;
            case ID_SEEK_PRE_ROLL /*22203*/:
                this.currentTrack.seekPreRollNs = value;
                return;
            case ID_AUDIO_BIT_DEPTH /*25188*/:
                this.currentTrack.audioBitDepth = (int) value;
                return;
            case ID_DEFAULT_DURATION /*2352003*/:
                this.currentTrack.defaultSampleDurationNs = (int) value;
                return;
            case ID_TIMECODE_SCALE /*2807729*/:
                this.timecodeScale = value;
                return;
            default:
                return;
        }
    }

    void floatElement(int id, double value) {
        if (id == ID_SAMPLING_FREQUENCY) {
            this.currentTrack.sampleRate = (int) value;
        } else if (id != ID_DURATION) {
            switch (id) {
                case ID_PRIMARY_R_CHROMATICITY_X /*21969*/:
                    this.currentTrack.primaryRChromaticityX = (float) value;
                    return;
                case ID_PRIMARY_R_CHROMATICITY_Y /*21970*/:
                    this.currentTrack.primaryRChromaticityY = (float) value;
                    return;
                case ID_PRIMARY_G_CHROMATICITY_X /*21971*/:
                    this.currentTrack.primaryGChromaticityX = (float) value;
                    return;
                case ID_PRIMARY_G_CHROMATICITY_Y /*21972*/:
                    this.currentTrack.primaryGChromaticityY = (float) value;
                    return;
                case ID_PRIMARY_B_CHROMATICITY_X /*21973*/:
                    this.currentTrack.primaryBChromaticityX = (float) value;
                    return;
                case ID_PRIMARY_B_CHROMATICITY_Y /*21974*/:
                    this.currentTrack.primaryBChromaticityY = (float) value;
                    return;
                case ID_WHITE_POINT_CHROMATICITY_X /*21975*/:
                    this.currentTrack.whitePointChromaticityX = (float) value;
                    return;
                case ID_WHITE_POINT_CHROMATICITY_Y /*21976*/:
                    this.currentTrack.whitePointChromaticityY = (float) value;
                    return;
                case ID_LUMNINANCE_MAX /*21977*/:
                    this.currentTrack.maxMasteringLuminance = (float) value;
                    return;
                case ID_LUMNINANCE_MIN /*21978*/:
                    this.currentTrack.minMasteringLuminance = (float) value;
                    return;
                default:
                    return;
            }
        } else {
            this.durationTimecode = (long) value;
        }
    }

    void stringElement(int id, String value) throws ParserException {
        if (id == 134) {
            this.currentTrack.codecId = value;
        } else if (id != ID_DOC_TYPE) {
            if (id == ID_LANGUAGE) {
                this.currentTrack.language = value;
            }
        } else if (!DOC_TYPE_WEBM.equals(value) && !DOC_TYPE_MATROSKA.equals(value)) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("DocType ");
            stringBuilder.append(value);
            stringBuilder.append(" not supported");
            throw new ParserException(stringBuilder.toString());
        }
    }

    void binaryElement(int id, int contentSize, ExtractorInput input) throws IOException, InterruptedException {
        MatroskaExtractor matroskaExtractor = this;
        int i = id;
        int i2 = contentSize;
        ExtractorInput extractorInput = input;
        boolean z = false;
        int i3 = 1;
        if (i == ID_BLOCK || i == ID_SIMPLE_BLOCK) {
            long j = 8;
            if (matroskaExtractor.blockState == 0) {
                matroskaExtractor.blockTrackNumber = (int) matroskaExtractor.varintReader.readUnsignedVarint(extractorInput, false, true, 8);
                matroskaExtractor.blockTrackNumberLength = matroskaExtractor.varintReader.getLastLength();
                matroskaExtractor.blockDurationUs = C.TIME_UNSET;
                matroskaExtractor.blockState = 1;
                matroskaExtractor.scratch.reset();
            }
            Track track = (Track) matroskaExtractor.tracks.get(matroskaExtractor.blockTrackNumber);
            if (track == null) {
                extractorInput.skipFully(i2 - matroskaExtractor.blockTrackNumberLength);
                matroskaExtractor.blockState = 0;
                return;
            }
            if (matroskaExtractor.blockState == 1) {
                readScratch(extractorInput, 3);
                int i4 = 6;
                int lacing = (matroskaExtractor.scratch.data[2] & 6) >> 1;
                int i5 = 255;
                if (lacing == 0) {
                    matroskaExtractor.blockLacingSampleCount = 1;
                    matroskaExtractor.blockLacingSampleSizes = ensureArrayCapacity(matroskaExtractor.blockLacingSampleSizes, 1);
                    matroskaExtractor.blockLacingSampleSizes[0] = (i2 - matroskaExtractor.blockTrackNumberLength) - 3;
                } else if (i != ID_SIMPLE_BLOCK) {
                    throw new ParserException("Lacing only supported in SimpleBlocks.");
                } else {
                    readScratch(extractorInput, 4);
                    matroskaExtractor.blockLacingSampleCount = (matroskaExtractor.scratch.data[3] & 255) + 1;
                    matroskaExtractor.blockLacingSampleSizes = ensureArrayCapacity(matroskaExtractor.blockLacingSampleSizes, matroskaExtractor.blockLacingSampleCount);
                    if (lacing == 2) {
                        Arrays.fill(matroskaExtractor.blockLacingSampleSizes, 0, matroskaExtractor.blockLacingSampleCount, ((i2 - matroskaExtractor.blockTrackNumberLength) - 4) / matroskaExtractor.blockLacingSampleCount);
                    } else if (lacing == 1) {
                        headerSize = 4;
                        totalSamplesSize = 0;
                        for (sampleIndex = 0; sampleIndex < matroskaExtractor.blockLacingSampleCount - 1; sampleIndex++) {
                            matroskaExtractor.blockLacingSampleSizes[sampleIndex] = 0;
                            do {
                                headerSize++;
                                readScratch(extractorInput, headerSize);
                                i4 = matroskaExtractor.scratch.data[headerSize - 1] & 255;
                                int[] iArr = matroskaExtractor.blockLacingSampleSizes;
                                iArr[sampleIndex] = iArr[sampleIndex] + i4;
                            } while (i4 == 255);
                            totalSamplesSize += matroskaExtractor.blockLacingSampleSizes[sampleIndex];
                        }
                        matroskaExtractor.blockLacingSampleSizes[matroskaExtractor.blockLacingSampleCount - 1] = ((i2 - matroskaExtractor.blockTrackNumberLength) - headerSize) - totalSamplesSize;
                    } else if (lacing == 3) {
                        headerSize = 4;
                        totalSamplesSize = 0;
                        sampleIndex = 0;
                        while (sampleIndex < matroskaExtractor.blockLacingSampleCount - i3) {
                            matroskaExtractor.blockLacingSampleSizes[sampleIndex] = z;
                            headerSize++;
                            readScratch(extractorInput, headerSize);
                            if (matroskaExtractor.scratch.data[headerSize - 1] == (byte) 0) {
                                throw new ParserException("No valid varint length mask found");
                            }
                            int readPosition;
                            int i6;
                            long readValue;
                            long readValue2 = 0;
                            int i7 = z;
                            while (i7 < j) {
                                int lengthMask = i3 << (7 - i7);
                                if ((matroskaExtractor.scratch.data[headerSize - 1] & lengthMask) != 0) {
                                    int readPosition2 = headerSize - 1;
                                    headerSize += i7;
                                    readScratch(extractorInput, headerSize);
                                    readValue2 = (long) ((matroskaExtractor.scratch.data[readPosition2] & i5) & (lengthMask ^ -1));
                                    readPosition = readPosition2 + 1;
                                    while (readPosition < headerSize) {
                                        readPosition++;
                                        readValue2 = (readValue2 << j) | ((long) (matroskaExtractor.scratch.data[readPosition] & 255));
                                        j = 8;
                                    }
                                    if (sampleIndex > 0) {
                                        i6 = 6;
                                        readValue = readValue2 - ((1 << (6 + (i7 * 7))) - 1);
                                        if (readValue >= -2147483648L) {
                                            if (readValue > 2147483647L) {
                                                readPosition = (int) readValue;
                                                matroskaExtractor.blockLacingSampleSizes[sampleIndex] = sampleIndex == 0 ? readPosition : matroskaExtractor.blockLacingSampleSizes[sampleIndex - 1] + readPosition;
                                                totalSamplesSize += matroskaExtractor.blockLacingSampleSizes[sampleIndex];
                                                sampleIndex++;
                                                i4 = i6;
                                                z = false;
                                                i3 = 1;
                                                j = 8;
                                                i5 = 255;
                                            }
                                        }
                                        throw new ParserException("EBML lacing sample size out of range.");
                                    }
                                    i6 = 6;
                                    readValue = readValue2;
                                    if (readValue >= -2147483648L) {
                                        if (readValue > 2147483647L) {
                                            readPosition = (int) readValue;
                                            if (sampleIndex == 0) {
                                            }
                                            matroskaExtractor.blockLacingSampleSizes[sampleIndex] = sampleIndex == 0 ? readPosition : matroskaExtractor.blockLacingSampleSizes[sampleIndex - 1] + readPosition;
                                            totalSamplesSize += matroskaExtractor.blockLacingSampleSizes[sampleIndex];
                                            sampleIndex++;
                                            i4 = i6;
                                            z = false;
                                            i3 = 1;
                                            j = 8;
                                            i5 = 255;
                                        }
                                    }
                                    throw new ParserException("EBML lacing sample size out of range.");
                                }
                                i6 = i4;
                                i7++;
                                i3 = 1;
                                j = 8;
                                i5 = 255;
                            }
                            i6 = i4;
                            readValue = readValue2;
                            if (readValue >= -2147483648L) {
                                if (readValue > 2147483647L) {
                                    readPosition = (int) readValue;
                                    if (sampleIndex == 0) {
                                    }
                                    matroskaExtractor.blockLacingSampleSizes[sampleIndex] = sampleIndex == 0 ? readPosition : matroskaExtractor.blockLacingSampleSizes[sampleIndex - 1] + readPosition;
                                    totalSamplesSize += matroskaExtractor.blockLacingSampleSizes[sampleIndex];
                                    sampleIndex++;
                                    i4 = i6;
                                    z = false;
                                    i3 = 1;
                                    j = 8;
                                    i5 = 255;
                                }
                            }
                            throw new ParserException("EBML lacing sample size out of range.");
                        }
                        matroskaExtractor.blockLacingSampleSizes[matroskaExtractor.blockLacingSampleCount - 1] = ((i2 - matroskaExtractor.blockTrackNumberLength) - headerSize) - totalSamplesSize;
                    } else {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("Unexpected lacing value: ");
                        stringBuilder.append(lacing);
                        throw new ParserException(stringBuilder.toString());
                    }
                }
                matroskaExtractor.blockTimeUs = matroskaExtractor.clusterTimecodeUs + scaleTimecodeToUs((long) ((matroskaExtractor.scratch.data[0] << 8) | (matroskaExtractor.scratch.data[1] & 255)));
                boolean isInvisible = (matroskaExtractor.scratch.data[2] & 8) == 8;
                if (track.type != 2) {
                    if (i != ID_SIMPLE_BLOCK || (matroskaExtractor.scratch.data[2] & 128) != 128) {
                        z = false;
                        matroskaExtractor.blockFlags = (z ? 1 : 0) | (isInvisible ? Integer.MIN_VALUE : 0);
                        matroskaExtractor.blockState = 2;
                        matroskaExtractor.blockLacingSampleIndex = 0;
                    }
                }
                z = true;
                if (z) {
                }
                if (isInvisible) {
                }
                matroskaExtractor.blockFlags = (z ? 1 : 0) | (isInvisible ? Integer.MIN_VALUE : 0);
                matroskaExtractor.blockState = 2;
                matroskaExtractor.blockLacingSampleIndex = 0;
            }
            if (i == ID_SIMPLE_BLOCK) {
                while (matroskaExtractor.blockLacingSampleIndex < matroskaExtractor.blockLacingSampleCount) {
                    writeSampleData(extractorInput, track, matroskaExtractor.blockLacingSampleSizes[matroskaExtractor.blockLacingSampleIndex]);
                    commitSampleToOutput(track, matroskaExtractor.blockTimeUs + ((long) ((matroskaExtractor.blockLacingSampleIndex * track.defaultSampleDurationNs) / 1000)));
                    matroskaExtractor.blockLacingSampleIndex++;
                }
                matroskaExtractor.blockState = 0;
            } else {
                writeSampleData(extractorInput, track, matroskaExtractor.blockLacingSampleSizes[0]);
            }
        } else if (i == ID_CONTENT_COMPRESSION_SETTINGS) {
            matroskaExtractor.currentTrack.sampleStrippedBytes = new byte[i2];
            extractorInput.readFully(matroskaExtractor.currentTrack.sampleStrippedBytes, 0, i2);
        } else if (i == ID_CONTENT_ENCRYPTION_KEY_ID) {
            byte[] encryptionKey = new byte[i2];
            extractorInput.readFully(encryptionKey, 0, i2);
            matroskaExtractor.currentTrack.cryptoData = new CryptoData(1, encryptionKey, 0, 0);
        } else if (i == ID_SEEK_ID) {
            Arrays.fill(matroskaExtractor.seekEntryIdBytes.data, (byte) 0);
            extractorInput.readFully(matroskaExtractor.seekEntryIdBytes.data, 4 - i2, i2);
            matroskaExtractor.seekEntryIdBytes.setPosition(0);
            matroskaExtractor.seekEntryId = (int) matroskaExtractor.seekEntryIdBytes.readUnsignedInt();
        } else if (i == ID_CODEC_PRIVATE) {
            matroskaExtractor.currentTrack.codecPrivate = new byte[i2];
            extractorInput.readFully(matroskaExtractor.currentTrack.codecPrivate, 0, i2);
        } else if (i != ID_PROJECTION_PRIVATE) {
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("Unexpected id: ");
            stringBuilder2.append(i);
            throw new ParserException(stringBuilder2.toString());
        } else {
            matroskaExtractor.currentTrack.projectionData = new byte[i2];
            extractorInput.readFully(matroskaExtractor.currentTrack.projectionData, 0, i2);
        }
    }

    private void commitSampleToOutput(Track track, long timeUs) {
        MatroskaExtractor matroskaExtractor = this;
        Track track2 = track;
        if (track2.trueHdSampleRechunker != null) {
            track2.trueHdSampleRechunker.sampleMetadata(track2, timeUs);
        } else {
            long j = timeUs;
            if (CODEC_ID_SUBRIP.equals(track2.codecId)) {
                commitSubtitleSample(track2, SUBRIP_TIMECODE_FORMAT, 19, SUBRIP_TIMECODE_LAST_VALUE_SCALING_FACTOR, SUBRIP_TIMECODE_EMPTY);
            } else if (CODEC_ID_ASS.equals(track2.codecId)) {
                commitSubtitleSample(track2, SSA_TIMECODE_FORMAT, 21, SSA_TIMECODE_LAST_VALUE_SCALING_FACTOR, SSA_TIMECODE_EMPTY);
            }
            track2.output.sampleMetadata(j, matroskaExtractor.blockFlags, matroskaExtractor.sampleBytesWritten, 0, track2.cryptoData);
        }
        matroskaExtractor.sampleRead = true;
        resetSample();
    }

    private void resetSample() {
        this.sampleBytesRead = 0;
        this.sampleBytesWritten = 0;
        this.sampleCurrentNalBytesRemaining = 0;
        this.sampleEncodingHandled = false;
        this.sampleSignalByteRead = false;
        this.samplePartitionCountRead = false;
        this.samplePartitionCount = 0;
        this.sampleSignalByte = (byte) 0;
        this.sampleInitializationVectorRead = false;
        this.sampleStrippedBytes.reset();
    }

    private void readScratch(ExtractorInput input, int requiredLength) throws IOException, InterruptedException {
        if (this.scratch.limit() < requiredLength) {
            if (this.scratch.capacity() < requiredLength) {
                this.scratch.reset(Arrays.copyOf(this.scratch.data, Math.max(this.scratch.data.length * 2, requiredLength)), this.scratch.limit());
            }
            input.readFully(this.scratch.data, this.scratch.limit(), requiredLength - this.scratch.limit());
            this.scratch.setLimit(requiredLength);
        }
    }

    private void writeSubtitleSampleData(ExtractorInput input, byte[] samplePrefix, int size) throws IOException, InterruptedException {
        int sizeWithPrefix = samplePrefix.length + size;
        if (this.subtitleSample.capacity() < sizeWithPrefix) {
            this.subtitleSample.data = Arrays.copyOf(samplePrefix, sizeWithPrefix + size);
        } else {
            System.arraycopy(samplePrefix, 0, this.subtitleSample.data, 0, samplePrefix.length);
        }
        input.readFully(this.subtitleSample.data, samplePrefix.length, size);
        this.subtitleSample.reset(sizeWithPrefix);
    }

    private void commitSubtitleSample(Track track, String timecodeFormat, int endTimecodeOffset, long lastTimecodeValueScalingFactor, byte[] emptyTimecode) {
        setSampleDuration(this.subtitleSample.data, this.blockDurationUs, timecodeFormat, endTimecodeOffset, lastTimecodeValueScalingFactor, emptyTimecode);
        track.output.sampleData(this.subtitleSample, this.subtitleSample.limit());
        this.sampleBytesWritten += this.subtitleSample.limit();
    }

    private static void setSampleDuration(byte[] subripSampleData, long durationUs, String timecodeFormat, int endTimecodeOffset, long lastTimecodeValueScalingFactor, byte[] emptyTimecode) {
        byte[] timeCodeData;
        if (durationUs == C.TIME_UNSET) {
            timeCodeData = emptyTimecode;
            long j = durationUs;
            String str = timecodeFormat;
        } else {
            long durationUs2 = durationUs - (((long) (((int) (durationUs / 3600000000L)) * 3600)) * C.MICROS_PER_SECOND);
            long durationUs3 = durationUs2 - (((long) (((int) (durationUs2 / 60000000)) * 60)) * C.MICROS_PER_SECOND);
            int lastValue = (int) ((durationUs3 - (((long) ((int) (durationUs3 / C.MICROS_PER_SECOND))) * C.MICROS_PER_SECOND)) / lastTimecodeValueScalingFactor);
            timeCodeData = Util.getUtf8Bytes(String.format(Locale.US, timecodeFormat, new Object[]{Integer.valueOf(hours), Integer.valueOf(minutes), Integer.valueOf(seconds), Integer.valueOf(lastValue)}));
        }
        System.arraycopy(timeCodeData, 0, subripSampleData, endTimecodeOffset, emptyTimecode.length);
    }

    private void readToTarget(ExtractorInput input, byte[] target, int offset, int length) throws IOException, InterruptedException {
        int pendingStrippedBytes = Math.min(length, this.sampleStrippedBytes.bytesLeft());
        input.readFully(target, offset + pendingStrippedBytes, length - pendingStrippedBytes);
        if (pendingStrippedBytes > 0) {
            this.sampleStrippedBytes.readBytes(target, offset, pendingStrippedBytes);
        }
        this.sampleBytesRead += length;
    }

    private int readToOutput(ExtractorInput input, TrackOutput output, int length) throws IOException, InterruptedException {
        int bytesRead;
        int strippedBytesLeft = this.sampleStrippedBytes.bytesLeft();
        if (strippedBytesLeft > 0) {
            bytesRead = Math.min(length, strippedBytesLeft);
            output.sampleData(this.sampleStrippedBytes, bytesRead);
        } else {
            bytesRead = output.sampleData(input, length, false);
        }
        this.sampleBytesRead += bytesRead;
        this.sampleBytesWritten += bytesRead;
        return bytesRead;
    }

    private boolean maybeSeekForCues(PositionHolder seekPosition, long currentPosition) {
        if (this.seekForCues) {
            this.seekPositionAfterBuildingCues = currentPosition;
            seekPosition.position = this.cuesContentPosition;
            this.seekForCues = false;
            return true;
        } else if (!this.sentSeekMap || this.seekPositionAfterBuildingCues == -1) {
            return false;
        } else {
            seekPosition.position = this.seekPositionAfterBuildingCues;
            this.seekPositionAfterBuildingCues = -1;
            return true;
        }
    }

    private long scaleTimecodeToUs(long unscaledTimecode) throws ParserException {
        if (this.timecodeScale == C.TIME_UNSET) {
            throw new ParserException("Can't scale timecode prior to timecodeScale being set.");
        }
        return Util.scaleLargeTimestamp(unscaledTimecode, this.timecodeScale, SUBRIP_TIMECODE_LAST_VALUE_SCALING_FACTOR);
    }

    private static boolean isCodecSupported(String codecId) {
        if (!(CODEC_ID_VP8.equals(codecId) || CODEC_ID_VP9.equals(codecId) || CODEC_ID_MPEG2.equals(codecId) || CODEC_ID_MPEG4_SP.equals(codecId) || CODEC_ID_MPEG4_ASP.equals(codecId) || CODEC_ID_MPEG4_AP.equals(codecId) || CODEC_ID_H264.equals(codecId) || CODEC_ID_H265.equals(codecId) || CODEC_ID_FOURCC.equals(codecId) || CODEC_ID_THEORA.equals(codecId) || CODEC_ID_OPUS.equals(codecId) || CODEC_ID_VORBIS.equals(codecId) || CODEC_ID_AAC.equals(codecId) || CODEC_ID_MP2.equals(codecId) || CODEC_ID_MP3.equals(codecId) || CODEC_ID_AC3.equals(codecId) || CODEC_ID_E_AC3.equals(codecId) || CODEC_ID_TRUEHD.equals(codecId) || CODEC_ID_DTS.equals(codecId) || CODEC_ID_DTS_EXPRESS.equals(codecId) || CODEC_ID_DTS_LOSSLESS.equals(codecId) || CODEC_ID_FLAC.equals(codecId) || CODEC_ID_ACM.equals(codecId) || CODEC_ID_PCM_INT_LIT.equals(codecId) || CODEC_ID_SUBRIP.equals(codecId) || CODEC_ID_ASS.equals(codecId) || CODEC_ID_VOBSUB.equals(codecId) || CODEC_ID_PGS.equals(codecId))) {
            if (!CODEC_ID_DVBSUB.equals(codecId)) {
                return false;
            }
        }
        return true;
    }

    private static int[] ensureArrayCapacity(int[] array, int length) {
        if (array == null) {
            return new int[length];
        }
        if (array.length >= length) {
            return array;
        }
        return new int[Math.max(array.length * 2, length)];
    }
}
