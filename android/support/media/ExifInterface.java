package android.support.media;

import android.content.res.AssetManager.AssetInputStream;
import android.util.Log;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.TimeZone;
import java.util.regex.Pattern;
import org.telegram.messenger.exoplayer2.C;
import org.telegram.messenger.exoplayer2.DefaultLoadControl;

public class ExifInterface {
    private static final Charset ASCII = Charset.forName(C.ASCII_NAME);
    public static final int[] BITS_PER_SAMPLE_GREYSCALE_1 = new int[]{4};
    public static final int[] BITS_PER_SAMPLE_GREYSCALE_2 = new int[]{8};
    public static final int[] BITS_PER_SAMPLE_RGB = new int[]{8, 8, 8};
    private static final byte[] EXIF_ASCII_PREFIX = new byte[]{(byte) 65, (byte) 83, (byte) 67, (byte) 73, (byte) 73, (byte) 0, (byte) 0, (byte) 0};
    private static final ExifTag[] EXIF_POINTER_TAGS = new ExifTag[]{new ExifTag("SubIFDPointer", 330, 4), new ExifTag("ExifIFDPointer", 34665, 4), new ExifTag("GPSInfoIFDPointer", 34853, 4), new ExifTag("InteroperabilityIFDPointer", 40965, 4), new ExifTag("CameraSettingsIFDPointer", 8224, 1), new ExifTag("ImageProcessingIFDPointer", 8256, 1)};
    static final ExifTag[][] EXIF_TAGS = new ExifTag[][]{IFD_TIFF_TAGS, IFD_EXIF_TAGS, IFD_GPS_TAGS, IFD_INTEROPERABILITY_TAGS, IFD_THUMBNAIL_TAGS, IFD_TIFF_TAGS, ORF_MAKER_NOTE_TAGS, ORF_CAMERA_SETTINGS_TAGS, ORF_IMAGE_PROCESSING_TAGS, PEF_TAGS};
    private static final List<Integer> FLIPPED_ROTATION_ORDER = Arrays.asList(new Integer[]{Integer.valueOf(2), Integer.valueOf(7), Integer.valueOf(4), Integer.valueOf(5)});
    static final byte[] IDENTIFIER_EXIF_APP1 = "Exif\u0000\u0000".getBytes(ASCII);
    private static final ExifTag[] IFD_EXIF_TAGS = new ExifTag[]{new ExifTag("ExposureTime", 33434, 5), new ExifTag("FNumber", 33437, 5), new ExifTag("ExposureProgram", 34850, 3), new ExifTag("SpectralSensitivity", 34852, 2), new ExifTag("PhotographicSensitivity", 34855, 3), new ExifTag("OECF", 34856, 7), new ExifTag("ExifVersion", 36864, 2), new ExifTag("DateTimeOriginal", 36867, 2), new ExifTag("DateTimeDigitized", 36868, 2), new ExifTag("ComponentsConfiguration", 37121, 7), new ExifTag("CompressedBitsPerPixel", 37122, 5), new ExifTag("ShutterSpeedValue", 37377, 10), new ExifTag("ApertureValue", 37378, 5), new ExifTag("BrightnessValue", 37379, 10), new ExifTag("ExposureBiasValue", 37380, 10), new ExifTag("MaxApertureValue", 37381, 5), new ExifTag("SubjectDistance", 37382, 5), new ExifTag("MeteringMode", 37383, 3), new ExifTag("LightSource", 37384, 3), new ExifTag("Flash", 37385, 3), new ExifTag("FocalLength", 37386, 5), new ExifTag("SubjectArea", 37396, 3), new ExifTag("MakerNote", 37500, 7), new ExifTag("UserComment", 37510, 7), new ExifTag("SubSecTime", 37520, 2), new ExifTag("SubSecTimeOriginal", 37521, 2), new ExifTag("SubSecTimeDigitized", 37522, 2), new ExifTag("FlashpixVersion", 40960, 7), new ExifTag("ColorSpace", 40961, 3), new ExifTag("PixelXDimension", 40962, 3, 4), new ExifTag("PixelYDimension", 40963, 3, 4), new ExifTag("RelatedSoundFile", 40964, 2), new ExifTag("InteroperabilityIFDPointer", 40965, 4), new ExifTag("FlashEnergy", 41483, 5), new ExifTag("SpatialFrequencyResponse", 41484, 7), new ExifTag("FocalPlaneXResolution", 41486, 5), new ExifTag("FocalPlaneYResolution", 41487, 5), new ExifTag("FocalPlaneResolutionUnit", 41488, 3), new ExifTag("SubjectLocation", 41492, 3), new ExifTag("ExposureIndex", 41493, 5), new ExifTag("SensingMethod", 41495, 3), new ExifTag("FileSource", 41728, 7), new ExifTag("SceneType", 41729, 7), new ExifTag("CFAPattern", 41730, 7), new ExifTag("CustomRendered", 41985, 3), new ExifTag("ExposureMode", 41986, 3), new ExifTag("WhiteBalance", 41987, 3), new ExifTag("DigitalZoomRatio", 41988, 5), new ExifTag("FocalLengthIn35mmFilm", 41989, 3), new ExifTag("SceneCaptureType", 41990, 3), new ExifTag("GainControl", 41991, 3), new ExifTag("Contrast", 41992, 3), new ExifTag("Saturation", 41993, 3), new ExifTag("Sharpness", 41994, 3), new ExifTag("DeviceSettingDescription", 41995, 7), new ExifTag("SubjectDistanceRange", 41996, 3), new ExifTag("ImageUniqueID", 42016, 2), new ExifTag("DNGVersion", 50706, 1), new ExifTag("DefaultCropSize", 50720, 3, 4)};
    static final int[] IFD_FORMAT_BYTES_PER_FORMAT = new int[]{0, 1, 1, 2, 4, 8, 1, 1, 2, 4, 8, 4, 8, 1};
    static final String[] IFD_FORMAT_NAMES = new String[]{TtmlNode.ANONYMOUS_REGION_ID, "BYTE", "STRING", "USHORT", "ULONG", "URATIONAL", "SBYTE", "UNDEFINED", "SSHORT", "SLONG", "SRATIONAL", "SINGLE", "DOUBLE"};
    private static final ExifTag[] IFD_GPS_TAGS = new ExifTag[]{new ExifTag("GPSVersionID", 0, 1), new ExifTag("GPSLatitudeRef", 1, 2), new ExifTag("GPSLatitude", 2, 5), new ExifTag("GPSLongitudeRef", 3, 2), new ExifTag("GPSLongitude", 4, 5), new ExifTag("GPSAltitudeRef", 5, 1), new ExifTag("GPSAltitude", 6, 5), new ExifTag("GPSTimeStamp", 7, 5), new ExifTag("GPSSatellites", 8, 2), new ExifTag("GPSStatus", 9, 2), new ExifTag("GPSMeasureMode", 10, 2), new ExifTag("GPSDOP", 11, 5), new ExifTag("GPSSpeedRef", 12, 2), new ExifTag("GPSSpeed", 13, 5), new ExifTag("GPSTrackRef", 14, 2), new ExifTag("GPSTrack", 15, 5), new ExifTag("GPSImgDirectionRef", 16, 2), new ExifTag("GPSImgDirection", 17, 5), new ExifTag("GPSMapDatum", 18, 2), new ExifTag("GPSDestLatitudeRef", 19, 2), new ExifTag("GPSDestLatitude", 20, 5), new ExifTag("GPSDestLongitudeRef", 21, 2), new ExifTag("GPSDestLongitude", 22, 5), new ExifTag("GPSDestBearingRef", 23, 2), new ExifTag("GPSDestBearing", 24, 5), new ExifTag("GPSDestDistanceRef", 25, 2), new ExifTag("GPSDestDistance", 26, 5), new ExifTag("GPSProcessingMethod", 27, 7), new ExifTag("GPSAreaInformation", 28, 7), new ExifTag("GPSDateStamp", 29, 2), new ExifTag("GPSDifferential", 30, 3)};
    private static final ExifTag[] IFD_INTEROPERABILITY_TAGS = new ExifTag[]{new ExifTag("InteroperabilityIndex", 1, 2)};
    private static final ExifTag[] IFD_THUMBNAIL_TAGS = new ExifTag[]{new ExifTag("NewSubfileType", 254, 4), new ExifTag("SubfileType", 255, 4), new ExifTag("ThumbnailImageWidth", 256, 3, 4), new ExifTag("ThumbnailImageLength", 257, 3, 4), new ExifTag("BitsPerSample", 258, 3), new ExifTag("Compression", 259, 3), new ExifTag("PhotometricInterpretation", 262, 3), new ExifTag("ImageDescription", 270, 2), new ExifTag("Make", 271, 2), new ExifTag("Model", 272, 2), new ExifTag("StripOffsets", 273, 3, 4), new ExifTag("Orientation", 274, 3), new ExifTag("SamplesPerPixel", 277, 3), new ExifTag("RowsPerStrip", 278, 3, 4), new ExifTag("StripByteCounts", 279, 3, 4), new ExifTag("XResolution", 282, 5), new ExifTag("YResolution", 283, 5), new ExifTag("PlanarConfiguration", 284, 3), new ExifTag("ResolutionUnit", 296, 3), new ExifTag("TransferFunction", 301, 3), new ExifTag("Software", 305, 2), new ExifTag("DateTime", 306, 2), new ExifTag("Artist", 315, 2), new ExifTag("WhitePoint", 318, 5), new ExifTag("PrimaryChromaticities", 319, 5), new ExifTag("SubIFDPointer", 330, 4), new ExifTag("JPEGInterchangeFormat", 513, 4), new ExifTag("JPEGInterchangeFormatLength", 514, 4), new ExifTag("YCbCrCoefficients", 529, 5), new ExifTag("YCbCrSubSampling", 530, 3), new ExifTag("YCbCrPositioning", 531, 3), new ExifTag("ReferenceBlackWhite", 532, 5), new ExifTag("Copyright", 33432, 2), new ExifTag("ExifIFDPointer", 34665, 4), new ExifTag("GPSInfoIFDPointer", 34853, 4), new ExifTag("DNGVersion", 50706, 1), new ExifTag("DefaultCropSize", 50720, 3, 4)};
    private static final ExifTag[] IFD_TIFF_TAGS = new ExifTag[]{new ExifTag("NewSubfileType", 254, 4), new ExifTag("SubfileType", 255, 4), new ExifTag("ImageWidth", 256, 3, 4), new ExifTag("ImageLength", 257, 3, 4), new ExifTag("BitsPerSample", 258, 3), new ExifTag("Compression", 259, 3), new ExifTag("PhotometricInterpretation", 262, 3), new ExifTag("ImageDescription", 270, 2), new ExifTag("Make", 271, 2), new ExifTag("Model", 272, 2), new ExifTag("StripOffsets", 273, 3, 4), new ExifTag("Orientation", 274, 3), new ExifTag("SamplesPerPixel", 277, 3), new ExifTag("RowsPerStrip", 278, 3, 4), new ExifTag("StripByteCounts", 279, 3, 4), new ExifTag("XResolution", 282, 5), new ExifTag("YResolution", 283, 5), new ExifTag("PlanarConfiguration", 284, 3), new ExifTag("ResolutionUnit", 296, 3), new ExifTag("TransferFunction", 301, 3), new ExifTag("Software", 305, 2), new ExifTag("DateTime", 306, 2), new ExifTag("Artist", 315, 2), new ExifTag("WhitePoint", 318, 5), new ExifTag("PrimaryChromaticities", 319, 5), new ExifTag("SubIFDPointer", 330, 4), new ExifTag("JPEGInterchangeFormat", 513, 4), new ExifTag("JPEGInterchangeFormatLength", 514, 4), new ExifTag("YCbCrCoefficients", 529, 5), new ExifTag("YCbCrSubSampling", 530, 3), new ExifTag("YCbCrPositioning", 531, 3), new ExifTag("ReferenceBlackWhite", 532, 5), new ExifTag("Copyright", 33432, 2), new ExifTag("ExifIFDPointer", 34665, 4), new ExifTag("GPSInfoIFDPointer", 34853, 4), new ExifTag("SensorTopBorder", 4, 4), new ExifTag("SensorLeftBorder", 5, 4), new ExifTag("SensorBottomBorder", 6, 4), new ExifTag("SensorRightBorder", 7, 4), new ExifTag("ISO", 23, 3), new ExifTag("JpgFromRaw", 46, 7)};
    private static final ExifTag JPEG_INTERCHANGE_FORMAT_LENGTH_TAG = new ExifTag("JPEGInterchangeFormatLength", 514, 4);
    private static final ExifTag JPEG_INTERCHANGE_FORMAT_TAG = new ExifTag("JPEGInterchangeFormat", 513, 4);
    static final byte[] JPEG_SIGNATURE = new byte[]{(byte) -1, (byte) -40, (byte) -1};
    private static final ExifTag[] ORF_CAMERA_SETTINGS_TAGS = new ExifTag[]{new ExifTag("PreviewImageStart", 257, 4), new ExifTag("PreviewImageLength", 258, 4)};
    private static final ExifTag[] ORF_IMAGE_PROCESSING_TAGS = new ExifTag[]{new ExifTag("AspectFrame", 4371, 3)};
    private static final byte[] ORF_MAKER_NOTE_HEADER_1 = new byte[]{(byte) 79, (byte) 76, (byte) 89, (byte) 77, (byte) 80, (byte) 0};
    private static final byte[] ORF_MAKER_NOTE_HEADER_2 = new byte[]{(byte) 79, (byte) 76, (byte) 89, (byte) 77, (byte) 80, (byte) 85, (byte) 83, (byte) 0, (byte) 73, (byte) 73};
    private static final ExifTag[] ORF_MAKER_NOTE_TAGS = new ExifTag[]{new ExifTag("ThumbnailImage", 256, 7), new ExifTag("CameraSettingsIFDPointer", 8224, 4), new ExifTag("ImageProcessingIFDPointer", 8256, 4)};
    private static final ExifTag[] PEF_TAGS = new ExifTag[]{new ExifTag("ColorSpace", 55, 3)};
    private static final List<Integer> ROTATION_ORDER = Arrays.asList(new Integer[]{Integer.valueOf(1), Integer.valueOf(6), Integer.valueOf(3), Integer.valueOf(8)});
    private static final ExifTag TAG_RAF_IMAGE_SIZE = new ExifTag("StripOffsets", 273, 3);
    private static final HashMap<Integer, Integer> sExifPointerTagMap = new HashMap();
    private static final HashMap<Integer, ExifTag>[] sExifTagMapsForReading = new HashMap[EXIF_TAGS.length];
    private static final HashMap<String, ExifTag>[] sExifTagMapsForWriting = new HashMap[EXIF_TAGS.length];
    private static SimpleDateFormat sFormatter = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
    private static final Pattern sGpsTimestampPattern = Pattern.compile("^([0-9][0-9]):([0-9][0-9]):([0-9][0-9])$");
    private static final Pattern sNonZeroTimePattern = Pattern.compile(".*[1-9].*");
    private static final HashSet<String> sTagSetForCompatibility = new HashSet(Arrays.asList(new String[]{"FNumber", "DigitalZoomRatio", "ExposureTime", "SubjectDistance", "GPSTimeStamp"}));
    private final AssetInputStream mAssetInputStream;
    private final HashMap<String, ExifAttribute>[] mAttributes = new HashMap[EXIF_TAGS.length];
    private ByteOrder mExifByteOrder = ByteOrder.BIG_ENDIAN;
    private int mExifOffset;
    private final String mFilename;
    private boolean mHasThumbnail;
    private boolean mIsSupportedFile;
    private int mMimeType;
    private int mOrfMakerNoteOffset;
    private int mOrfThumbnailLength;
    private int mOrfThumbnailOffset;
    private int mRw2JpgFromRawOffset;
    private byte[] mThumbnailBytes;
    private int mThumbnailCompression;
    private int mThumbnailLength;
    private int mThumbnailOffset;

    private static class ByteOrderedDataInputStream extends InputStream implements DataInput {
        private static final ByteOrder BIG_ENDIAN = ByteOrder.BIG_ENDIAN;
        private static final ByteOrder LITTLE_ENDIAN = ByteOrder.LITTLE_ENDIAN;
        private ByteOrder mByteOrder;
        private DataInputStream mDataInputStream;
        private final int mLength;
        private int mPosition;

        public ByteOrderedDataInputStream(InputStream in) throws IOException {
            this.mByteOrder = ByteOrder.BIG_ENDIAN;
            this.mDataInputStream = new DataInputStream(in);
            this.mLength = this.mDataInputStream.available();
            this.mPosition = 0;
            this.mDataInputStream.mark(this.mLength);
        }

        public ByteOrderedDataInputStream(byte[] bytes) throws IOException {
            this(new ByteArrayInputStream(bytes));
        }

        public void setByteOrder(ByteOrder byteOrder) {
            this.mByteOrder = byteOrder;
        }

        public void seek(long byteCount) throws IOException {
            if (((long) this.mPosition) > byteCount) {
                this.mPosition = 0;
                this.mDataInputStream.reset();
                this.mDataInputStream.mark(this.mLength);
            } else {
                byteCount -= (long) this.mPosition;
            }
            if (skipBytes((int) byteCount) != ((int) byteCount)) {
                throw new IOException("Couldn't seek up to the byteCount");
            }
        }

        public int peek() {
            return this.mPosition;
        }

        public int available() throws IOException {
            return this.mDataInputStream.available();
        }

        public int read() throws IOException {
            this.mPosition++;
            return this.mDataInputStream.read();
        }

        public int read(byte[] b, int off, int len) throws IOException {
            int bytesRead = this.mDataInputStream.read(b, off, len);
            this.mPosition += bytesRead;
            return bytesRead;
        }

        public int readUnsignedByte() throws IOException {
            this.mPosition++;
            return this.mDataInputStream.readUnsignedByte();
        }

        public String readLine() throws IOException {
            Log.d("ExifInterface", "Currently unsupported");
            return null;
        }

        public boolean readBoolean() throws IOException {
            this.mPosition++;
            return this.mDataInputStream.readBoolean();
        }

        public char readChar() throws IOException {
            this.mPosition += 2;
            return this.mDataInputStream.readChar();
        }

        public String readUTF() throws IOException {
            this.mPosition += 2;
            return this.mDataInputStream.readUTF();
        }

        public void readFully(byte[] buffer, int offset, int length) throws IOException {
            this.mPosition += length;
            if (this.mPosition > this.mLength) {
                throw new EOFException();
            } else if (this.mDataInputStream.read(buffer, offset, length) != length) {
                throw new IOException("Couldn't read up to the length of buffer");
            }
        }

        public void readFully(byte[] buffer) throws IOException {
            this.mPosition += buffer.length;
            if (this.mPosition > this.mLength) {
                throw new EOFException();
            } else if (this.mDataInputStream.read(buffer, 0, buffer.length) != buffer.length) {
                throw new IOException("Couldn't read up to the length of buffer");
            }
        }

        public byte readByte() throws IOException {
            this.mPosition++;
            if (this.mPosition > this.mLength) {
                throw new EOFException();
            }
            int ch = this.mDataInputStream.read();
            if (ch >= 0) {
                return (byte) ch;
            }
            throw new EOFException();
        }

        public short readShort() throws IOException {
            this.mPosition += 2;
            if (this.mPosition > this.mLength) {
                throw new EOFException();
            }
            int ch1 = this.mDataInputStream.read();
            int ch2 = this.mDataInputStream.read();
            if ((ch1 | ch2) < 0) {
                throw new EOFException();
            } else if (this.mByteOrder == LITTLE_ENDIAN) {
                return (short) ((ch2 << 8) + ch1);
            } else {
                if (this.mByteOrder == BIG_ENDIAN) {
                    return (short) ((ch1 << 8) + ch2);
                }
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Invalid byte order: ");
                stringBuilder.append(this.mByteOrder);
                throw new IOException(stringBuilder.toString());
            }
        }

        public int readInt() throws IOException {
            this.mPosition += 4;
            if (this.mPosition > this.mLength) {
                throw new EOFException();
            }
            int ch1 = this.mDataInputStream.read();
            int ch2 = this.mDataInputStream.read();
            int ch3 = this.mDataInputStream.read();
            int ch4 = this.mDataInputStream.read();
            if ((((ch1 | ch2) | ch3) | ch4) < 0) {
                throw new EOFException();
            } else if (this.mByteOrder == LITTLE_ENDIAN) {
                return (((ch4 << 24) + (ch3 << 16)) + (ch2 << 8)) + ch1;
            } else {
                if (this.mByteOrder == BIG_ENDIAN) {
                    return (((ch1 << 24) + (ch2 << 16)) + (ch3 << 8)) + ch4;
                }
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Invalid byte order: ");
                stringBuilder.append(this.mByteOrder);
                throw new IOException(stringBuilder.toString());
            }
        }

        public int skipBytes(int byteCount) throws IOException {
            int totalSkip = Math.min(byteCount, this.mLength - this.mPosition);
            int skipped = 0;
            while (skipped < totalSkip) {
                skipped += this.mDataInputStream.skipBytes(totalSkip - skipped);
            }
            this.mPosition += skipped;
            return skipped;
        }

        public int readUnsignedShort() throws IOException {
            this.mPosition += 2;
            if (this.mPosition > this.mLength) {
                throw new EOFException();
            }
            int ch1 = this.mDataInputStream.read();
            int ch2 = this.mDataInputStream.read();
            if ((ch1 | ch2) < 0) {
                throw new EOFException();
            } else if (this.mByteOrder == LITTLE_ENDIAN) {
                return (ch2 << 8) + ch1;
            } else {
                if (this.mByteOrder == BIG_ENDIAN) {
                    return (ch1 << 8) + ch2;
                }
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Invalid byte order: ");
                stringBuilder.append(this.mByteOrder);
                throw new IOException(stringBuilder.toString());
            }
        }

        public long readUnsignedInt() throws IOException {
            return ((long) readInt()) & 4294967295L;
        }

        public long readLong() throws IOException {
            this.mPosition += 8;
            if (this.mPosition > this.mLength) {
                throw new EOFException();
            }
            int ch1 = r0.mDataInputStream.read();
            int ch2 = r0.mDataInputStream.read();
            int ch3 = r0.mDataInputStream.read();
            int ch4 = r0.mDataInputStream.read();
            int ch5 = r0.mDataInputStream.read();
            int ch6 = r0.mDataInputStream.read();
            int ch7 = r0.mDataInputStream.read();
            int ch8 = r0.mDataInputStream.read();
            if ((((((((ch1 | ch2) | ch3) | ch4) | ch5) | ch6) | ch7) | ch8) < 0) {
                throw new EOFException();
            } else if (r0.mByteOrder == LITTLE_ENDIAN) {
                return (((((((((long) ch8) << 56) + (((long) ch7) << 48)) + (((long) ch6) << 40)) + (((long) ch5) << 32)) + (((long) ch4) << 24)) + (((long) ch3) << 16)) + (((long) ch2) << 8)) + ((long) ch1);
            } else {
                int ch22 = ch2;
                if (r0.mByteOrder == BIG_ENDIAN) {
                    return (((((((((long) ch1) << 56) + (((long) ch22) << 48)) + (((long) ch3) << 40)) + (((long) ch4) << 32)) + (((long) ch5) << 24)) + (((long) ch6) << 16)) + (((long) ch7) << 8)) + ((long) ch8);
                }
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Invalid byte order: ");
                stringBuilder.append(r0.mByteOrder);
                throw new IOException(stringBuilder.toString());
            }
        }

        public float readFloat() throws IOException {
            return Float.intBitsToFloat(readInt());
        }

        public double readDouble() throws IOException {
            return Double.longBitsToDouble(readLong());
        }
    }

    private static class ExifAttribute {
        public final byte[] bytes;
        public final int format;
        public final int numberOfComponents;

        private java.lang.Object getValue(java.nio.ByteOrder r1) {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: android.support.media.ExifInterface.ExifAttribute.getValue(java.nio.ByteOrder):java.lang.Object
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:116)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:249)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:256)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
Caused by: java.lang.NullPointerException
	at jadx.core.dex.nodes.MethodNode.addJump(MethodNode.java:370)
	at jadx.core.dex.nodes.MethodNode.initJumps(MethodNode.java:356)
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:106)
	... 6 more
*/
            /*
            r0 = this;
            r1 = r18;
            r2 = 0;
            r3 = r2;
            r4 = new android.support.media.ExifInterface$ByteOrderedDataInputStream;	 Catch:{ IOException -> 0x0215, all -> 0x0210 }
            r5 = r1.bytes;	 Catch:{ IOException -> 0x0215, all -> 0x0210 }
            r4.<init>(r5);	 Catch:{ IOException -> 0x0215, all -> 0x0210 }
            r3 = r4;
            r4 = r19;
            r3.setByteOrder(r4);	 Catch:{ IOException -> 0x020e, all -> 0x020c }
            r5 = r1.format;	 Catch:{ IOException -> 0x020e, all -> 0x020c }
            r6 = 0;
            switch(r5) {
                case 1: goto L_0x01b4;
                case 2: goto L_0x0153;
                case 3: goto L_0x0130;
                case 4: goto L_0x010d;
                case 5: goto L_0x00df;
                case 6: goto L_0x01b4;
                case 7: goto L_0x0153;
                case 8: goto L_0x00bc;
                case 9: goto L_0x0099;
                case 10: goto L_0x0066;
                case 11: goto L_0x0042;
                case 12: goto L_0x001f;
                default: goto L_0x0017;
            };
        L_0x0018:
            if (r3 == 0) goto L_0x020b;
        L_0x001a:
            r3.close();	 Catch:{ IOException -> 0x0202 }
            goto L_0x0201;
        L_0x001f:
            r5 = r1.numberOfComponents;	 Catch:{ IOException -> 0x020e, all -> 0x020c }
            r5 = new double[r5];	 Catch:{ IOException -> 0x020e, all -> 0x020c }
            r7 = r1.numberOfComponents;	 Catch:{ IOException -> 0x020e, all -> 0x020c }
            if (r6 >= r7) goto L_0x0031;	 Catch:{ IOException -> 0x020e, all -> 0x020c }
        L_0x0028:
            r7 = r3.readDouble();	 Catch:{ IOException -> 0x020e, all -> 0x020c }
            r5[r6] = r7;	 Catch:{ IOException -> 0x020e, all -> 0x020c }
            r6 = r6 + 1;
            goto L_0x0024;
            if (r3 == 0) goto L_0x0041;
            r3.close();	 Catch:{ IOException -> 0x0038 }
            goto L_0x0041;
        L_0x0038:
            r0 = move-exception;
            r2 = r0;
            r6 = "ExifInterface";
            r7 = "IOException occurred while closing InputStream";
            android.util.Log.e(r6, r7, r2);
            return r5;
        L_0x0042:
            r5 = r1.numberOfComponents;	 Catch:{ IOException -> 0x020e, all -> 0x020c }
            r5 = new double[r5];	 Catch:{ IOException -> 0x020e, all -> 0x020c }
            r7 = r1.numberOfComponents;	 Catch:{ IOException -> 0x020e, all -> 0x020c }
            if (r6 >= r7) goto L_0x0055;	 Catch:{ IOException -> 0x020e, all -> 0x020c }
            r7 = r3.readFloat();	 Catch:{ IOException -> 0x020e, all -> 0x020c }
            r7 = (double) r7;	 Catch:{ IOException -> 0x020e, all -> 0x020c }
            r5[r6] = r7;	 Catch:{ IOException -> 0x020e, all -> 0x020c }
            r6 = r6 + 1;
            goto L_0x0047;
            if (r3 == 0) goto L_0x0065;
            r3.close();	 Catch:{ IOException -> 0x005c }
            goto L_0x0065;
        L_0x005c:
            r0 = move-exception;
            r2 = r0;
            r6 = "ExifInterface";
            r7 = "IOException occurred while closing InputStream";
            android.util.Log.e(r6, r7, r2);
            return r5;
        L_0x0066:
            r5 = r1.numberOfComponents;	 Catch:{ IOException -> 0x020e, all -> 0x020c }
            r5 = new android.support.media.ExifInterface.Rational[r5];	 Catch:{ IOException -> 0x020e, all -> 0x020c }
            r7 = r1.numberOfComponents;	 Catch:{ IOException -> 0x020e, all -> 0x020c }
            if (r6 >= r7) goto L_0x0088;	 Catch:{ IOException -> 0x020e, all -> 0x020c }
            r7 = r3.readInt();	 Catch:{ IOException -> 0x020e, all -> 0x020c }
            r14 = (long) r7;	 Catch:{ IOException -> 0x020e, all -> 0x020c }
            r7 = r3.readInt();	 Catch:{ IOException -> 0x020e, all -> 0x020c }
            r11 = (long) r7;	 Catch:{ IOException -> 0x020e, all -> 0x020c }
            r7 = new android.support.media.ExifInterface$Rational;	 Catch:{ IOException -> 0x020e, all -> 0x020c }
            r13 = 0;	 Catch:{ IOException -> 0x020e, all -> 0x020c }
            r8 = r7;	 Catch:{ IOException -> 0x020e, all -> 0x020c }
            r9 = r14;	 Catch:{ IOException -> 0x020e, all -> 0x020c }
            r16 = r11;	 Catch:{ IOException -> 0x020e, all -> 0x020c }
            r8.<init>(r9, r11);	 Catch:{ IOException -> 0x020e, all -> 0x020c }
            r5[r6] = r7;	 Catch:{ IOException -> 0x020e, all -> 0x020c }
            r6 = r6 + 1;
            goto L_0x006b;
            if (r3 == 0) goto L_0x0098;
            r3.close();	 Catch:{ IOException -> 0x008f }
            goto L_0x0098;
        L_0x008f:
            r0 = move-exception;
            r2 = r0;
            r6 = "ExifInterface";
            r7 = "IOException occurred while closing InputStream";
            android.util.Log.e(r6, r7, r2);
            return r5;
        L_0x0099:
            r5 = r1.numberOfComponents;	 Catch:{ IOException -> 0x020e, all -> 0x020c }
            r5 = new int[r5];	 Catch:{ IOException -> 0x020e, all -> 0x020c }
            r7 = r1.numberOfComponents;	 Catch:{ IOException -> 0x020e, all -> 0x020c }
            if (r6 >= r7) goto L_0x00ab;	 Catch:{ IOException -> 0x020e, all -> 0x020c }
            r7 = r3.readInt();	 Catch:{ IOException -> 0x020e, all -> 0x020c }
            r5[r6] = r7;	 Catch:{ IOException -> 0x020e, all -> 0x020c }
            r6 = r6 + 1;
            goto L_0x009e;
            if (r3 == 0) goto L_0x00bb;
            r3.close();	 Catch:{ IOException -> 0x00b2 }
            goto L_0x00bb;
        L_0x00b2:
            r0 = move-exception;
            r2 = r0;
            r6 = "ExifInterface";
            r7 = "IOException occurred while closing InputStream";
            android.util.Log.e(r6, r7, r2);
            return r5;
        L_0x00bc:
            r5 = r1.numberOfComponents;	 Catch:{ IOException -> 0x020e, all -> 0x020c }
            r5 = new int[r5];	 Catch:{ IOException -> 0x020e, all -> 0x020c }
            r7 = r1.numberOfComponents;	 Catch:{ IOException -> 0x020e, all -> 0x020c }
            if (r6 >= r7) goto L_0x00ce;	 Catch:{ IOException -> 0x020e, all -> 0x020c }
            r7 = r3.readShort();	 Catch:{ IOException -> 0x020e, all -> 0x020c }
            r5[r6] = r7;	 Catch:{ IOException -> 0x020e, all -> 0x020c }
            r6 = r6 + 1;
            goto L_0x00c1;
            if (r3 == 0) goto L_0x00de;
            r3.close();	 Catch:{ IOException -> 0x00d5 }
            goto L_0x00de;
        L_0x00d5:
            r0 = move-exception;
            r2 = r0;
            r6 = "ExifInterface";
            r7 = "IOException occurred while closing InputStream";
            android.util.Log.e(r6, r7, r2);
            return r5;
        L_0x00df:
            r5 = r1.numberOfComponents;	 Catch:{ IOException -> 0x020e, all -> 0x020c }
            r5 = new android.support.media.ExifInterface.Rational[r5];	 Catch:{ IOException -> 0x020e, all -> 0x020c }
            r7 = r1.numberOfComponents;	 Catch:{ IOException -> 0x020e, all -> 0x020c }
            if (r6 >= r7) goto L_0x00fc;	 Catch:{ IOException -> 0x020e, all -> 0x020c }
            r9 = r3.readUnsignedInt();	 Catch:{ IOException -> 0x020e, all -> 0x020c }
            r11 = r3.readUnsignedInt();	 Catch:{ IOException -> 0x020e, all -> 0x020c }
            r7 = new android.support.media.ExifInterface$Rational;	 Catch:{ IOException -> 0x020e, all -> 0x020c }
            r13 = 0;	 Catch:{ IOException -> 0x020e, all -> 0x020c }
            r8 = r7;	 Catch:{ IOException -> 0x020e, all -> 0x020c }
            r8.<init>(r9, r11);	 Catch:{ IOException -> 0x020e, all -> 0x020c }
            r5[r6] = r7;	 Catch:{ IOException -> 0x020e, all -> 0x020c }
            r6 = r6 + 1;
            goto L_0x00e4;
            if (r3 == 0) goto L_0x010c;
            r3.close();	 Catch:{ IOException -> 0x0103 }
            goto L_0x010c;
        L_0x0103:
            r0 = move-exception;
            r2 = r0;
            r6 = "ExifInterface";
            r7 = "IOException occurred while closing InputStream";
            android.util.Log.e(r6, r7, r2);
            return r5;
        L_0x010d:
            r5 = r1.numberOfComponents;	 Catch:{ IOException -> 0x020e, all -> 0x020c }
            r5 = new long[r5];	 Catch:{ IOException -> 0x020e, all -> 0x020c }
            r7 = r1.numberOfComponents;	 Catch:{ IOException -> 0x020e, all -> 0x020c }
            if (r6 >= r7) goto L_0x011f;	 Catch:{ IOException -> 0x020e, all -> 0x020c }
            r7 = r3.readUnsignedInt();	 Catch:{ IOException -> 0x020e, all -> 0x020c }
            r5[r6] = r7;	 Catch:{ IOException -> 0x020e, all -> 0x020c }
            r6 = r6 + 1;
            goto L_0x0112;
            if (r3 == 0) goto L_0x012f;
            r3.close();	 Catch:{ IOException -> 0x0126 }
            goto L_0x012f;
        L_0x0126:
            r0 = move-exception;
            r2 = r0;
            r6 = "ExifInterface";
            r7 = "IOException occurred while closing InputStream";
            android.util.Log.e(r6, r7, r2);
            return r5;
        L_0x0130:
            r5 = r1.numberOfComponents;	 Catch:{ IOException -> 0x020e, all -> 0x020c }
            r5 = new int[r5];	 Catch:{ IOException -> 0x020e, all -> 0x020c }
            r7 = r1.numberOfComponents;	 Catch:{ IOException -> 0x020e, all -> 0x020c }
            if (r6 >= r7) goto L_0x0142;	 Catch:{ IOException -> 0x020e, all -> 0x020c }
            r7 = r3.readUnsignedShort();	 Catch:{ IOException -> 0x020e, all -> 0x020c }
            r5[r6] = r7;	 Catch:{ IOException -> 0x020e, all -> 0x020c }
            r6 = r6 + 1;
            goto L_0x0135;
            if (r3 == 0) goto L_0x0152;
            r3.close();	 Catch:{ IOException -> 0x0149 }
            goto L_0x0152;
        L_0x0149:
            r0 = move-exception;
            r2 = r0;
            r6 = "ExifInterface";
            r7 = "IOException occurred while closing InputStream";
            android.util.Log.e(r6, r7, r2);
            return r5;
        L_0x0153:
            r5 = 0;
            r7 = r1.numberOfComponents;	 Catch:{ IOException -> 0x020e, all -> 0x020c }
            r8 = android.support.media.ExifInterface.EXIF_ASCII_PREFIX;	 Catch:{ IOException -> 0x020e, all -> 0x020c }
            r8 = r8.length;	 Catch:{ IOException -> 0x020e, all -> 0x020c }
            if (r7 < r8) goto L_0x017f;	 Catch:{ IOException -> 0x020e, all -> 0x020c }
            r7 = 1;	 Catch:{ IOException -> 0x020e, all -> 0x020c }
            r8 = android.support.media.ExifInterface.EXIF_ASCII_PREFIX;	 Catch:{ IOException -> 0x020e, all -> 0x020c }
            r8 = r8.length;	 Catch:{ IOException -> 0x020e, all -> 0x020c }
            if (r6 >= r8) goto L_0x0177;	 Catch:{ IOException -> 0x020e, all -> 0x020c }
            r8 = r1.bytes;	 Catch:{ IOException -> 0x020e, all -> 0x020c }
            r8 = r8[r6];	 Catch:{ IOException -> 0x020e, all -> 0x020c }
            r9 = android.support.media.ExifInterface.EXIF_ASCII_PREFIX;	 Catch:{ IOException -> 0x020e, all -> 0x020c }
            r9 = r9[r6];	 Catch:{ IOException -> 0x020e, all -> 0x020c }
            if (r8 == r9) goto L_0x0174;	 Catch:{ IOException -> 0x020e, all -> 0x020c }
            r7 = 0;	 Catch:{ IOException -> 0x020e, all -> 0x020c }
            goto L_0x0177;	 Catch:{ IOException -> 0x020e, all -> 0x020c }
            r6 = r6 + 1;	 Catch:{ IOException -> 0x020e, all -> 0x020c }
            goto L_0x015f;	 Catch:{ IOException -> 0x020e, all -> 0x020c }
            if (r7 == 0) goto L_0x017f;	 Catch:{ IOException -> 0x020e, all -> 0x020c }
            r6 = android.support.media.ExifInterface.EXIF_ASCII_PREFIX;	 Catch:{ IOException -> 0x020e, all -> 0x020c }
            r6 = r6.length;	 Catch:{ IOException -> 0x020e, all -> 0x020c }
            r5 = r6;	 Catch:{ IOException -> 0x020e, all -> 0x020c }
            r6 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x020e, all -> 0x020c }
            r6.<init>();	 Catch:{ IOException -> 0x020e, all -> 0x020c }
            r7 = r1.numberOfComponents;	 Catch:{ IOException -> 0x020e, all -> 0x020c }
            if (r5 >= r7) goto L_0x01a0;	 Catch:{ IOException -> 0x020e, all -> 0x020c }
            r7 = r1.bytes;	 Catch:{ IOException -> 0x020e, all -> 0x020c }
            r7 = r7[r5];	 Catch:{ IOException -> 0x020e, all -> 0x020c }
            if (r7 != 0) goto L_0x018f;	 Catch:{ IOException -> 0x020e, all -> 0x020c }
            goto L_0x01a0;	 Catch:{ IOException -> 0x020e, all -> 0x020c }
            r8 = 32;	 Catch:{ IOException -> 0x020e, all -> 0x020c }
            if (r7 < r8) goto L_0x0198;	 Catch:{ IOException -> 0x020e, all -> 0x020c }
            r8 = (char) r7;	 Catch:{ IOException -> 0x020e, all -> 0x020c }
            r6.append(r8);	 Catch:{ IOException -> 0x020e, all -> 0x020c }
            goto L_0x019d;	 Catch:{ IOException -> 0x020e, all -> 0x020c }
            r8 = 63;	 Catch:{ IOException -> 0x020e, all -> 0x020c }
            r6.append(r8);	 Catch:{ IOException -> 0x020e, all -> 0x020c }
            r5 = r5 + 1;	 Catch:{ IOException -> 0x020e, all -> 0x020c }
            goto L_0x0184;	 Catch:{ IOException -> 0x020e, all -> 0x020c }
            r7 = r6.toString();	 Catch:{ IOException -> 0x020e, all -> 0x020c }
            if (r3 == 0) goto L_0x01b3;
            r3.close();	 Catch:{ IOException -> 0x01aa }
            goto L_0x01b3;
        L_0x01aa:
            r0 = move-exception;
            r2 = r0;
            r8 = "ExifInterface";
            r9 = "IOException occurred while closing InputStream";
            android.util.Log.e(r8, r9, r2);
            return r7;
        L_0x01b4:
            r5 = r1.bytes;	 Catch:{ IOException -> 0x020e, all -> 0x020c }
            r5 = r5.length;	 Catch:{ IOException -> 0x020e, all -> 0x020c }
            r7 = 1;	 Catch:{ IOException -> 0x020e, all -> 0x020c }
            if (r5 != r7) goto L_0x01e6;	 Catch:{ IOException -> 0x020e, all -> 0x020c }
            r5 = r1.bytes;	 Catch:{ IOException -> 0x020e, all -> 0x020c }
            r5 = r5[r6];	 Catch:{ IOException -> 0x020e, all -> 0x020c }
            if (r5 < 0) goto L_0x01e6;	 Catch:{ IOException -> 0x020e, all -> 0x020c }
            r5 = r1.bytes;	 Catch:{ IOException -> 0x020e, all -> 0x020c }
            r5 = r5[r6];	 Catch:{ IOException -> 0x020e, all -> 0x020c }
            if (r5 > r7) goto L_0x01e6;	 Catch:{ IOException -> 0x020e, all -> 0x020c }
            r5 = new java.lang.String;	 Catch:{ IOException -> 0x020e, all -> 0x020c }
            r7 = new char[r7];	 Catch:{ IOException -> 0x020e, all -> 0x020c }
            r8 = r1.bytes;	 Catch:{ IOException -> 0x020e, all -> 0x020c }
            r8 = r8[r6];	 Catch:{ IOException -> 0x020e, all -> 0x020c }
            r8 = r8 + 48;	 Catch:{ IOException -> 0x020e, all -> 0x020c }
            r8 = (char) r8;	 Catch:{ IOException -> 0x020e, all -> 0x020c }
            r7[r6] = r8;	 Catch:{ IOException -> 0x020e, all -> 0x020c }
            r5.<init>(r7);	 Catch:{ IOException -> 0x020e, all -> 0x020c }
            if (r3 == 0) goto L_0x01e5;
            r3.close();	 Catch:{ IOException -> 0x01dc }
            goto L_0x01e5;
        L_0x01dc:
            r0 = move-exception;
            r2 = r0;
            r6 = "ExifInterface";
            r7 = "IOException occurred while closing InputStream";
            android.util.Log.e(r6, r7, r2);
            return r5;
            r5 = new java.lang.String;	 Catch:{ IOException -> 0x020e, all -> 0x020c }
            r6 = r1.bytes;	 Catch:{ IOException -> 0x020e, all -> 0x020c }
            r7 = android.support.media.ExifInterface.ASCII;	 Catch:{ IOException -> 0x020e, all -> 0x020c }
            r5.<init>(r6, r7);	 Catch:{ IOException -> 0x020e, all -> 0x020c }
            if (r3 == 0) goto L_0x0200;
            r3.close();	 Catch:{ IOException -> 0x01f7 }
            goto L_0x0200;
        L_0x01f7:
            r0 = move-exception;
            r2 = r0;
            r6 = "ExifInterface";
            r7 = "IOException occurred while closing InputStream";
            android.util.Log.e(r6, r7, r2);
            return r5;
        L_0x0201:
            goto L_0x020b;
        L_0x0202:
            r0 = move-exception;
            r5 = r0;
            r6 = "ExifInterface";
            r7 = "IOException occurred while closing InputStream";
            android.util.Log.e(r6, r7, r5);
        L_0x020b:
            return r2;
        L_0x020c:
            r0 = move-exception;
            goto L_0x0213;
        L_0x020e:
            r0 = move-exception;
            goto L_0x0218;
        L_0x0210:
            r0 = move-exception;
            r4 = r19;
            r2 = r0;
            goto L_0x0235;
        L_0x0215:
            r0 = move-exception;
            r4 = r19;
            r5 = r3;
            r3 = r0;
            r6 = "ExifInterface";	 Catch:{ all -> 0x0232 }
            r7 = "IOException occurred during reading a value";	 Catch:{ all -> 0x0232 }
            android.util.Log.w(r6, r7, r3);	 Catch:{ all -> 0x0232 }
            if (r5 == 0) goto L_0x0231;
            r5.close();	 Catch:{ IOException -> 0x0228 }
            goto L_0x0231;
        L_0x0228:
            r0 = move-exception;
            r6 = r0;
            r7 = "ExifInterface";
            r8 = "IOException occurred while closing InputStream";
            android.util.Log.e(r7, r8, r6);
            return r2;
        L_0x0232:
            r0 = move-exception;
            r2 = r0;
            r3 = r5;
            if (r3 == 0) goto L_0x0244;
            r3.close();	 Catch:{ IOException -> 0x023b }
            goto L_0x0244;
        L_0x023b:
            r0 = move-exception;
            r5 = r0;
            r6 = "ExifInterface";
            r7 = "IOException occurred while closing InputStream";
            android.util.Log.e(r6, r7, r5);
            throw r2;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.support.media.ExifInterface.ExifAttribute.getValue(java.nio.ByteOrder):java.lang.Object");
        }

        private ExifAttribute(int format, int numberOfComponents, byte[] bytes) {
            this.format = format;
            this.numberOfComponents = numberOfComponents;
            this.bytes = bytes;
        }

        public static ExifAttribute createUShort(int[] values, ByteOrder byteOrder) {
            ByteBuffer buffer = ByteBuffer.wrap(new byte[(ExifInterface.IFD_FORMAT_BYTES_PER_FORMAT[3] * values.length)]);
            buffer.order(byteOrder);
            for (int value : values) {
                buffer.putShort((short) value);
            }
            return new ExifAttribute(3, values.length, buffer.array());
        }

        public static ExifAttribute createUShort(int value, ByteOrder byteOrder) {
            return createUShort(new int[]{value}, byteOrder);
        }

        public static ExifAttribute createULong(long[] values, ByteOrder byteOrder) {
            ByteBuffer buffer = ByteBuffer.wrap(new byte[(ExifInterface.IFD_FORMAT_BYTES_PER_FORMAT[4] * values.length)]);
            buffer.order(byteOrder);
            for (long value : values) {
                buffer.putInt((int) value);
            }
            return new ExifAttribute(4, values.length, buffer.array());
        }

        public static ExifAttribute createULong(long value, ByteOrder byteOrder) {
            return createULong(new long[]{value}, byteOrder);
        }

        public static ExifAttribute createString(String value) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(value);
            stringBuilder.append('\u0000');
            byte[] ascii = stringBuilder.toString().getBytes(ExifInterface.ASCII);
            return new ExifAttribute(2, ascii.length, ascii);
        }

        public static ExifAttribute createURational(Rational[] values, ByteOrder byteOrder) {
            ByteBuffer buffer = ByteBuffer.wrap(new byte[(ExifInterface.IFD_FORMAT_BYTES_PER_FORMAT[5] * values.length)]);
            buffer.order(byteOrder);
            for (Rational value : values) {
                buffer.putInt((int) value.numerator);
                buffer.putInt((int) value.denominator);
            }
            return new ExifAttribute(5, values.length, buffer.array());
        }

        public static ExifAttribute createURational(Rational value, ByteOrder byteOrder) {
            return createURational(new Rational[]{value}, byteOrder);
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("(");
            stringBuilder.append(ExifInterface.IFD_FORMAT_NAMES[this.format]);
            stringBuilder.append(", data length:");
            stringBuilder.append(this.bytes.length);
            stringBuilder.append(")");
            return stringBuilder.toString();
        }

        public double getDoubleValue(ByteOrder byteOrder) {
            Object value = getValue(byteOrder);
            if (value == null) {
                throw new NumberFormatException("NULL can't be converted to a double value");
            } else if (value instanceof String) {
                return Double.parseDouble((String) value);
            } else {
                if (value instanceof long[]) {
                    long[] array = (long[]) value;
                    if (array.length == 1) {
                        return (double) array[0];
                    }
                    throw new NumberFormatException("There are more than one component");
                } else if (value instanceof int[]) {
                    int[] array2 = (int[]) value;
                    if (array2.length == 1) {
                        return (double) array2[0];
                    }
                    throw new NumberFormatException("There are more than one component");
                } else if (value instanceof double[]) {
                    double[] array3 = (double[]) value;
                    if (array3.length == 1) {
                        return array3[0];
                    }
                    throw new NumberFormatException("There are more than one component");
                } else if (value instanceof Rational[]) {
                    Rational[] array4 = (Rational[]) value;
                    if (array4.length == 1) {
                        return array4[0].calculate();
                    }
                    throw new NumberFormatException("There are more than one component");
                } else {
                    throw new NumberFormatException("Couldn't find a double value");
                }
            }
        }

        public int getIntValue(ByteOrder byteOrder) {
            Object value = getValue(byteOrder);
            if (value == null) {
                throw new NumberFormatException("NULL can't be converted to a integer value");
            } else if (value instanceof String) {
                return Integer.parseInt((String) value);
            } else {
                if (value instanceof long[]) {
                    long[] array = (long[]) value;
                    if (array.length == 1) {
                        return (int) array[0];
                    }
                    throw new NumberFormatException("There are more than one component");
                } else if (value instanceof int[]) {
                    int[] array2 = (int[]) value;
                    if (array2.length == 1) {
                        return array2[0];
                    }
                    throw new NumberFormatException("There are more than one component");
                } else {
                    throw new NumberFormatException("Couldn't find a integer value");
                }
            }
        }

        public String getStringValue(ByteOrder byteOrder) {
            Object value = getValue(byteOrder);
            if (value == null) {
                return null;
            }
            if (value instanceof String) {
                return (String) value;
            }
            StringBuilder stringBuilder = new StringBuilder();
            int i = 0;
            int i2;
            if (value instanceof long[]) {
                long[] array = (long[]) value;
                while (true) {
                    i2 = i;
                    if (i2 >= array.length) {
                        return stringBuilder.toString();
                    }
                    stringBuilder.append(array[i2]);
                    if (i2 + 1 != array.length) {
                        stringBuilder.append(",");
                    }
                    i = i2 + 1;
                }
            } else if (value instanceof int[]) {
                int[] array2 = (int[]) value;
                while (true) {
                    i2 = i;
                    if (i2 >= array2.length) {
                        return stringBuilder.toString();
                    }
                    stringBuilder.append(array2[i2]);
                    if (i2 + 1 != array2.length) {
                        stringBuilder.append(",");
                    }
                    i = i2 + 1;
                }
            } else if (value instanceof double[]) {
                double[] array3 = (double[]) value;
                while (true) {
                    i2 = i;
                    if (i2 >= array3.length) {
                        return stringBuilder.toString();
                    }
                    stringBuilder.append(array3[i2]);
                    if (i2 + 1 != array3.length) {
                        stringBuilder.append(",");
                    }
                    i = i2 + 1;
                }
            } else if (!(value instanceof Rational[])) {
                return null;
            } else {
                Rational[] array4 = (Rational[]) value;
                while (true) {
                    i2 = i;
                    if (i2 >= array4.length) {
                        return stringBuilder.toString();
                    }
                    stringBuilder.append(array4[i2].numerator);
                    stringBuilder.append('/');
                    stringBuilder.append(array4[i2].denominator);
                    if (i2 + 1 != array4.length) {
                        stringBuilder.append(",");
                    }
                    i = i2 + 1;
                }
            }
        }
    }

    static class ExifTag {
        public final String name;
        public final int number;
        public final int primaryFormat;
        public final int secondaryFormat;

        private ExifTag(String name, int number, int format) {
            this.name = name;
            this.number = number;
            this.primaryFormat = format;
            this.secondaryFormat = -1;
        }

        private ExifTag(String name, int number, int primaryFormat, int secondaryFormat) {
            this.name = name;
            this.number = number;
            this.primaryFormat = primaryFormat;
            this.secondaryFormat = secondaryFormat;
        }

        private boolean isFormatCompatible(int format) {
            if (this.primaryFormat != 7) {
                if (format != 7) {
                    if (this.primaryFormat != format) {
                        if (this.secondaryFormat != format) {
                            if ((this.primaryFormat == 4 || this.secondaryFormat == 4) && format == 3) {
                                return true;
                            }
                            if ((this.primaryFormat == 9 || this.secondaryFormat == 9) && format == 8) {
                                return true;
                            }
                            if ((this.primaryFormat == 12 || this.secondaryFormat == 12) && format == 11) {
                                return true;
                            }
                            return false;
                        }
                    }
                    return true;
                }
            }
            return true;
        }
    }

    private static class Rational {
        public final long denominator;
        public final long numerator;

        private Rational(long numerator, long denominator) {
            if (denominator == 0) {
                this.numerator = 0;
                this.denominator = 1;
                return;
            }
            this.numerator = numerator;
            this.denominator = denominator;
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(this.numerator);
            stringBuilder.append("/");
            stringBuilder.append(this.denominator);
            return stringBuilder.toString();
        }

        public double calculate() {
            return ((double) this.numerator) / ((double) this.denominator);
        }
    }

    static {
        sFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        for (int ifdType = 0; ifdType < EXIF_TAGS.length; ifdType++) {
            sExifTagMapsForReading[ifdType] = new HashMap();
            sExifTagMapsForWriting[ifdType] = new HashMap();
            for (ExifTag tag : EXIF_TAGS[ifdType]) {
                sExifTagMapsForReading[ifdType].put(Integer.valueOf(tag.number), tag);
                sExifTagMapsForWriting[ifdType].put(tag.name, tag);
            }
        }
        sExifPointerTagMap.put(Integer.valueOf(EXIF_POINTER_TAGS[0].number), Integer.valueOf(5));
        sExifPointerTagMap.put(Integer.valueOf(EXIF_POINTER_TAGS[1].number), Integer.valueOf(1));
        sExifPointerTagMap.put(Integer.valueOf(EXIF_POINTER_TAGS[2].number), Integer.valueOf(2));
        sExifPointerTagMap.put(Integer.valueOf(EXIF_POINTER_TAGS[3].number), Integer.valueOf(3));
        sExifPointerTagMap.put(Integer.valueOf(EXIF_POINTER_TAGS[4].number), Integer.valueOf(7));
        sExifPointerTagMap.put(Integer.valueOf(EXIF_POINTER_TAGS[5].number), Integer.valueOf(8));
    }

    public ExifInterface(String filename) throws IOException {
        if (filename == null) {
            throw new IllegalArgumentException("filename cannot be null");
        }
        FileInputStream in = null;
        this.mAssetInputStream = null;
        this.mFilename = filename;
        try {
            in = new FileInputStream(filename);
            loadAttributes(in);
        } finally {
            closeQuietly(in);
        }
    }

    private ExifAttribute getExifAttribute(String tag) {
        if ("ISOSpeedRatings".equals(tag)) {
            tag = "PhotographicSensitivity";
        }
        for (int i = 0; i < EXIF_TAGS.length; i++) {
            ExifAttribute value = (ExifAttribute) this.mAttributes[i].get(tag);
            if (value != null) {
                return value;
            }
        }
        return null;
    }

    public String getAttribute(String tag) {
        ExifAttribute attribute = getExifAttribute(tag);
        if (attribute == null) {
            return null;
        }
        if (!sTagSetForCompatibility.contains(tag)) {
            return attribute.getStringValue(this.mExifByteOrder);
        }
        if (!tag.equals("GPSTimeStamp")) {
            try {
                return Double.toString(attribute.getDoubleValue(this.mExifByteOrder));
            } catch (NumberFormatException e) {
                return null;
            }
        } else if (attribute.format == 5 || attribute.format == 10) {
            Rational[] array = (Rational[]) attribute.getValue(this.mExifByteOrder);
            if (array != null) {
                if (array.length == 3) {
                    return String.format("%02d:%02d:%02d", new Object[]{Integer.valueOf((int) (((float) array[0].numerator) / ((float) array[0].denominator))), Integer.valueOf((int) (((float) array[1].numerator) / ((float) array[1].denominator))), Integer.valueOf((int) (((float) array[2].numerator) / ((float) array[2].denominator)))});
                }
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Invalid GPS Timestamp array. array=");
            stringBuilder.append(Arrays.toString(array));
            Log.w("ExifInterface", stringBuilder.toString());
            return null;
        } else {
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("GPS Timestamp format is not rational. format=");
            stringBuilder2.append(attribute.format);
            Log.w("ExifInterface", stringBuilder2.toString());
            return null;
        }
    }

    public int getAttributeInt(String tag, int defaultValue) {
        ExifAttribute exifAttribute = getExifAttribute(tag);
        if (exifAttribute == null) {
            return defaultValue;
        }
        try {
            return exifAttribute.getIntValue(this.mExifByteOrder);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    private void loadAttributes(InputStream in) throws IOException {
        int i = 0;
        while (i < EXIF_TAGS.length) {
            try {
                this.mAttributes[i] = new HashMap();
                i++;
            } catch (IOException e) {
                this.mIsSupportedFile = false;
            } catch (Throwable th) {
                addDefaultValuesForCompatibility();
            }
        }
        in = new BufferedInputStream(in, DefaultLoadControl.DEFAULT_BUFFER_FOR_PLAYBACK_AFTER_REBUFFER_MS);
        this.mMimeType = getMimeType((BufferedInputStream) in);
        ByteOrderedDataInputStream inputStream = new ByteOrderedDataInputStream(in);
        switch (this.mMimeType) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 5:
            case 6:
            case 8:
            case 11:
                getRawAttributes(inputStream);
                break;
            case 4:
                getJpegAttributes(inputStream, 0, 0);
                break;
            case 7:
                getOrfAttributes(inputStream);
                break;
            case 9:
                getRafAttributes(inputStream);
                break;
            case 10:
                getRw2Attributes(inputStream);
                break;
            default:
                break;
        }
        setThumbnailData(inputStream);
        this.mIsSupportedFile = true;
        addDefaultValuesForCompatibility();
    }

    private int getMimeType(BufferedInputStream in) throws IOException {
        in.mark(DefaultLoadControl.DEFAULT_BUFFER_FOR_PLAYBACK_AFTER_REBUFFER_MS);
        byte[] signatureCheckBytes = new byte[DefaultLoadControl.DEFAULT_BUFFER_FOR_PLAYBACK_AFTER_REBUFFER_MS];
        in.read(signatureCheckBytes);
        in.reset();
        if (isJpegFormat(signatureCheckBytes)) {
            return 4;
        }
        if (isRafFormat(signatureCheckBytes)) {
            return 9;
        }
        if (isOrfFormat(signatureCheckBytes)) {
            return 7;
        }
        if (isRw2Format(signatureCheckBytes)) {
            return 10;
        }
        return 0;
    }

    private static boolean isJpegFormat(byte[] signatureCheckBytes) throws IOException {
        for (int i = 0; i < JPEG_SIGNATURE.length; i++) {
            if (signatureCheckBytes[i] != JPEG_SIGNATURE[i]) {
                return false;
            }
        }
        return true;
    }

    private boolean isRafFormat(byte[] signatureCheckBytes) throws IOException {
        byte[] rafSignatureBytes = "FUJIFILMCCD-RAW".getBytes(Charset.defaultCharset());
        for (int i = 0; i < rafSignatureBytes.length; i++) {
            if (signatureCheckBytes[i] != rafSignatureBytes[i]) {
                return false;
            }
        }
        return true;
    }

    private boolean isOrfFormat(byte[] signatureCheckBytes) throws IOException {
        ByteOrderedDataInputStream signatureInputStream = new ByteOrderedDataInputStream(signatureCheckBytes);
        this.mExifByteOrder = readByteOrder(signatureInputStream);
        signatureInputStream.setByteOrder(this.mExifByteOrder);
        short orfSignature = signatureInputStream.readShort();
        signatureInputStream.close();
        if (orfSignature != (short) 20306) {
            if (orfSignature != (short) 21330) {
                return false;
            }
        }
        return true;
    }

    private boolean isRw2Format(byte[] signatureCheckBytes) throws IOException {
        ByteOrderedDataInputStream signatureInputStream = new ByteOrderedDataInputStream(signatureCheckBytes);
        this.mExifByteOrder = readByteOrder(signatureInputStream);
        signatureInputStream.setByteOrder(this.mExifByteOrder);
        short signatureByte = signatureInputStream.readShort();
        signatureInputStream.close();
        return signatureByte == (short) 85;
    }

    private void getJpegAttributes(ByteOrderedDataInputStream in, int jpegOffset, int imageType) throws IOException {
        in.setByteOrder(ByteOrder.BIG_ENDIAN);
        in.seek((long) jpegOffset);
        int bytesRead = jpegOffset;
        byte readByte = in.readByte();
        byte marker = readByte;
        if (readByte != (byte) -1) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Invalid marker: ");
            stringBuilder.append(Integer.toHexString(marker & 255));
            throw new IOException(stringBuilder.toString());
        }
        bytesRead++;
        if (in.readByte() != (byte) -40) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("Invalid marker: ");
            stringBuilder.append(Integer.toHexString(marker & 255));
            throw new IOException(stringBuilder.toString());
        }
        bytesRead++;
        while (true) {
            marker = in.readByte();
            if (marker != (byte) -1) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("Invalid marker:");
                stringBuilder.append(Integer.toHexString(marker & 255));
                throw new IOException(stringBuilder.toString());
            }
            bytesRead++;
            marker = in.readByte();
            bytesRead++;
            if (marker != (byte) -39) {
                if (marker != (byte) -38) {
                    int length = in.readUnsignedShort() - 2;
                    bytesRead += 2;
                    if (length < 0) {
                        throw new IOException("Invalid length");
                    }
                    byte[] bytes;
                    if (marker != (byte) -31) {
                        if (marker != (byte) -2) {
                            switch (marker) {
                                case (byte) -64:
                                case (byte) -63:
                                case (byte) -62:
                                case (byte) -61:
                                    if (in.skipBytes(1) != 1) {
                                        this.mAttributes[imageType].put("ImageLength", ExifAttribute.createULong((long) in.readUnsignedShort(), this.mExifByteOrder));
                                        this.mAttributes[imageType].put("ImageWidth", ExifAttribute.createULong((long) in.readUnsignedShort(), this.mExifByteOrder));
                                        length -= 5;
                                        break;
                                    }
                                    throw new IOException("Invalid SOFx");
                                default:
                                    switch (marker) {
                                        case (byte) -59:
                                        case (byte) -58:
                                        case (byte) -57:
                                            break;
                                        default:
                                            switch (marker) {
                                                case (byte) -55:
                                                case (byte) -54:
                                                case (byte) -53:
                                                    break;
                                                default:
                                                    switch (marker) {
                                                        case (byte) -51:
                                                        case (byte) -50:
                                                        case (byte) -49:
                                                            break;
                                                        default:
                                                            break;
                                                    }
                                            }
                                    }
                                    if (in.skipBytes(1) != 1) {
                                        this.mAttributes[imageType].put("ImageLength", ExifAttribute.createULong((long) in.readUnsignedShort(), this.mExifByteOrder));
                                        this.mAttributes[imageType].put("ImageWidth", ExifAttribute.createULong((long) in.readUnsignedShort(), this.mExifByteOrder));
                                        length -= 5;
                                        break;
                                    }
                                    throw new IOException("Invalid SOFx");
                            }
                        }
                        bytes = new byte[length];
                        if (in.read(bytes) != length) {
                            throw new IOException("Invalid exif");
                        }
                        length = 0;
                        if (getAttribute("UserComment") == null) {
                            this.mAttributes[1].put("UserComment", ExifAttribute.createString(new String(bytes, ASCII)));
                        }
                    } else if (length >= 6) {
                        byte[] identifier = new byte[6];
                        if (in.read(identifier) != 6) {
                            throw new IOException("Invalid exif");
                        }
                        bytesRead += 6;
                        length -= 6;
                        if (Arrays.equals(identifier, IDENTIFIER_EXIF_APP1)) {
                            if (length <= 0) {
                                throw new IOException("Invalid exif");
                            }
                            this.mExifOffset = bytesRead;
                            bytes = new byte[length];
                            if (in.read(bytes) != length) {
                                throw new IOException("Invalid exif");
                            }
                            bytesRead += length;
                            length = 0;
                            readExifSegment(bytes, imageType);
                        }
                    }
                    if (length < 0) {
                        throw new IOException("Invalid length");
                    } else if (in.skipBytes(length) != length) {
                        throw new IOException("Invalid JPEG segment");
                    } else {
                        bytesRead += length;
                    }
                }
            }
            in.setByteOrder(this.mExifByteOrder);
            return;
        }
    }

    private void getRawAttributes(ByteOrderedDataInputStream in) throws IOException {
        parseTiffHeaders(in, in.available());
        readImageFileDirectory(in, 0);
        updateImageSizeValues(in, 0);
        updateImageSizeValues(in, 5);
        updateImageSizeValues(in, 4);
        validateImages(in);
        if (this.mMimeType == 8) {
            ExifAttribute makerNoteAttribute = (ExifAttribute) this.mAttributes[1].get("MakerNote");
            if (makerNoteAttribute != null) {
                ByteOrderedDataInputStream makerNoteDataInputStream = new ByteOrderedDataInputStream(makerNoteAttribute.bytes);
                makerNoteDataInputStream.setByteOrder(this.mExifByteOrder);
                makerNoteDataInputStream.seek(6);
                readImageFileDirectory(makerNoteDataInputStream, 9);
                ExifAttribute colorSpaceAttribute = (ExifAttribute) this.mAttributes[9].get("ColorSpace");
                if (colorSpaceAttribute != null) {
                    this.mAttributes[1].put("ColorSpace", colorSpaceAttribute);
                }
            }
        }
    }

    private void getRafAttributes(ByteOrderedDataInputStream in) throws IOException {
        ByteOrderedDataInputStream byteOrderedDataInputStream = in;
        byteOrderedDataInputStream.skipBytes(84);
        byte[] jpegOffsetBytes = new byte[4];
        byte[] cfaHeaderOffsetBytes = new byte[4];
        byteOrderedDataInputStream.read(jpegOffsetBytes);
        byteOrderedDataInputStream.skipBytes(4);
        byteOrderedDataInputStream.read(cfaHeaderOffsetBytes);
        int rafJpegOffset = ByteBuffer.wrap(jpegOffsetBytes).getInt();
        int rafCfaHeaderOffset = ByteBuffer.wrap(cfaHeaderOffsetBytes).getInt();
        getJpegAttributes(byteOrderedDataInputStream, rafJpegOffset, 5);
        byteOrderedDataInputStream.seek((long) rafCfaHeaderOffset);
        byteOrderedDataInputStream.setByteOrder(ByteOrder.BIG_ENDIAN);
        int numberOfDirectoryEntry = in.readInt();
        for (int i = 0; i < numberOfDirectoryEntry; i++) {
            int tagNumber = in.readUnsignedShort();
            int numberOfBytes = in.readUnsignedShort();
            if (tagNumber == TAG_RAF_IMAGE_SIZE.number) {
                int imageLength = in.readShort();
                int imageWidth = in.readShort();
                ExifAttribute imageLengthAttribute = ExifAttribute.createUShort(imageLength, r0.mExifByteOrder);
                ExifAttribute imageWidthAttribute = ExifAttribute.createUShort(imageWidth, r0.mExifByteOrder);
                r0.mAttributes[0].put("ImageLength", imageLengthAttribute);
                r0.mAttributes[0].put("ImageWidth", imageWidthAttribute);
                return;
            }
            int i2 = 0;
            byteOrderedDataInputStream.skipBytes(numberOfBytes);
        }
    }

    private void getOrfAttributes(ByteOrderedDataInputStream in) throws IOException {
        getRawAttributes(in);
        ExifAttribute makerNoteAttribute = (ExifAttribute) this.mAttributes[1].get("MakerNote");
        if (makerNoteAttribute != null) {
            ByteOrderedDataInputStream makerNoteDataInputStream = new ByteOrderedDataInputStream(makerNoteAttribute.bytes);
            makerNoteDataInputStream.setByteOrder(r0.mExifByteOrder);
            byte[] makerNoteHeader1Bytes = new byte[ORF_MAKER_NOTE_HEADER_1.length];
            makerNoteDataInputStream.readFully(makerNoteHeader1Bytes);
            makerNoteDataInputStream.seek(0);
            byte[] makerNoteHeader2Bytes = new byte[ORF_MAKER_NOTE_HEADER_2.length];
            makerNoteDataInputStream.readFully(makerNoteHeader2Bytes);
            if (Arrays.equals(makerNoteHeader1Bytes, ORF_MAKER_NOTE_HEADER_1)) {
                makerNoteDataInputStream.seek(8);
            } else if (Arrays.equals(makerNoteHeader2Bytes, ORF_MAKER_NOTE_HEADER_2)) {
                makerNoteDataInputStream.seek(12);
            }
            readImageFileDirectory(makerNoteDataInputStream, 6);
            ExifAttribute imageStartAttribute = (ExifAttribute) r0.mAttributes[7].get("PreviewImageStart");
            ExifAttribute imageLengthAttribute = (ExifAttribute) r0.mAttributes[7].get("PreviewImageLength");
            if (!(imageStartAttribute == null || imageLengthAttribute == null)) {
                r0.mAttributes[5].put("JPEGInterchangeFormat", imageStartAttribute);
                r0.mAttributes[5].put("JPEGInterchangeFormatLength", imageLengthAttribute);
            }
            ExifAttribute aspectFrameAttribute = (ExifAttribute) r0.mAttributes[8].get("AspectFrame");
            if (aspectFrameAttribute != null) {
                int[] aspectFrameValues = (int[]) aspectFrameAttribute.getValue(r0.mExifByteOrder);
                if (aspectFrameValues != null) {
                    if (aspectFrameValues.length == 4) {
                        if (aspectFrameValues[2] > aspectFrameValues[0] && aspectFrameValues[3] > aspectFrameValues[1]) {
                            int primaryImageWidth = (aspectFrameValues[2] - aspectFrameValues[0]) + 1;
                            int primaryImageLength = (aspectFrameValues[3] - aspectFrameValues[1]) + 1;
                            if (primaryImageWidth < primaryImageLength) {
                                primaryImageWidth += primaryImageLength;
                                primaryImageLength = primaryImageWidth - primaryImageLength;
                                primaryImageWidth -= primaryImageLength;
                            }
                            ExifAttribute primaryImageWidthAttribute = ExifAttribute.createUShort(primaryImageWidth, r0.mExifByteOrder);
                            ExifAttribute primaryImageLengthAttribute = ExifAttribute.createUShort(primaryImageLength, r0.mExifByteOrder);
                            r0.mAttributes[0].put("ImageWidth", primaryImageWidthAttribute);
                            r0.mAttributes[0].put("ImageLength", primaryImageLengthAttribute);
                        }
                    }
                }
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Invalid aspect frame values. frame=");
                stringBuilder.append(Arrays.toString(aspectFrameValues));
                Log.w("ExifInterface", stringBuilder.toString());
            }
        }
    }

    private void getRw2Attributes(ByteOrderedDataInputStream in) throws IOException {
        getRawAttributes(in);
        if (((ExifAttribute) this.mAttributes[0].get("JpgFromRaw")) != null) {
            getJpegAttributes(in, this.mRw2JpgFromRawOffset, 5);
        }
        ExifAttribute rw2IsoAttribute = (ExifAttribute) this.mAttributes[0].get("ISO");
        ExifAttribute exifIsoAttribute = (ExifAttribute) this.mAttributes[1].get("PhotographicSensitivity");
        if (rw2IsoAttribute != null && exifIsoAttribute == null) {
            this.mAttributes[1].put("PhotographicSensitivity", rw2IsoAttribute);
        }
    }

    private void readExifSegment(byte[] exifBytes, int imageType) throws IOException {
        ByteOrderedDataInputStream dataInputStream = new ByteOrderedDataInputStream(exifBytes);
        parseTiffHeaders(dataInputStream, exifBytes.length);
        readImageFileDirectory(dataInputStream, imageType);
    }

    private void addDefaultValuesForCompatibility() {
        String valueOfDateTimeOriginal = getAttribute("DateTimeOriginal");
        if (valueOfDateTimeOriginal != null && getAttribute("DateTime") == null) {
            this.mAttributes[0].put("DateTime", ExifAttribute.createString(valueOfDateTimeOriginal));
        }
        if (getAttribute("ImageWidth") == null) {
            this.mAttributes[0].put("ImageWidth", ExifAttribute.createULong(0, this.mExifByteOrder));
        }
        if (getAttribute("ImageLength") == null) {
            this.mAttributes[0].put("ImageLength", ExifAttribute.createULong(0, this.mExifByteOrder));
        }
        if (getAttribute("Orientation") == null) {
            this.mAttributes[0].put("Orientation", ExifAttribute.createULong(0, this.mExifByteOrder));
        }
        if (getAttribute("LightSource") == null) {
            this.mAttributes[1].put("LightSource", ExifAttribute.createULong(0, this.mExifByteOrder));
        }
    }

    private ByteOrder readByteOrder(ByteOrderedDataInputStream dataInputStream) throws IOException {
        short byteOrder = dataInputStream.readShort();
        if (byteOrder == (short) 18761) {
            return ByteOrder.LITTLE_ENDIAN;
        }
        if (byteOrder == (short) 19789) {
            return ByteOrder.BIG_ENDIAN;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Invalid byte order: ");
        stringBuilder.append(Integer.toHexString(byteOrder));
        throw new IOException(stringBuilder.toString());
    }

    private void parseTiffHeaders(ByteOrderedDataInputStream dataInputStream, int exifBytesLength) throws IOException {
        this.mExifByteOrder = readByteOrder(dataInputStream);
        dataInputStream.setByteOrder(this.mExifByteOrder);
        int startCode = dataInputStream.readUnsignedShort();
        if (this.mMimeType == 7 || this.mMimeType == 10 || startCode == 42) {
            StringBuilder stringBuilder;
            int firstIfdOffset = dataInputStream.readInt();
            if (firstIfdOffset >= 8) {
                if (firstIfdOffset < exifBytesLength) {
                    firstIfdOffset -= 8;
                    if (firstIfdOffset > 0 && dataInputStream.skipBytes(firstIfdOffset) != firstIfdOffset) {
                        stringBuilder = new StringBuilder();
                        stringBuilder.append("Couldn't jump to first Ifd: ");
                        stringBuilder.append(firstIfdOffset);
                        throw new IOException(stringBuilder.toString());
                    }
                    return;
                }
            }
            stringBuilder = new StringBuilder();
            stringBuilder.append("Invalid first Ifd offset: ");
            stringBuilder.append(firstIfdOffset);
            throw new IOException(stringBuilder.toString());
        }
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append("Invalid start code: ");
        stringBuilder2.append(Integer.toHexString(startCode));
        throw new IOException(stringBuilder2.toString());
    }

    private void readImageFileDirectory(ByteOrderedDataInputStream dataInputStream, int ifdType) throws IOException {
        ExifInterface exifInterface = this;
        ByteOrderedDataInputStream byteOrderedDataInputStream = dataInputStream;
        short s = ifdType;
        if (dataInputStream.mPosition + 2 <= dataInputStream.mLength) {
            short numberOfDirectoryEntry = dataInputStream.readShort();
            if (dataInputStream.mPosition + (12 * numberOfDirectoryEntry) <= dataInputStream.mLength) {
                short i = (short) 0;
                while (i < numberOfDirectoryEntry) {
                    short numberOfDirectoryEntry2;
                    int tagNumber = dataInputStream.readUnsignedShort();
                    int dataFormat = dataInputStream.readUnsignedShort();
                    int numberOfComponents = dataInputStream.readInt();
                    long nextEntryOffset = ((long) dataInputStream.peek()) + 4;
                    ExifTag tag = (ExifTag) sExifTagMapsForReading[s].get(Integer.valueOf(tagNumber));
                    long byteCount = 0;
                    boolean valid = false;
                    StringBuilder stringBuilder;
                    if (tag == null) {
                        stringBuilder = new StringBuilder();
                        stringBuilder.append("Skip the tag entry since tag number is not defined: ");
                        stringBuilder.append(tagNumber);
                        Log.w("ExifInterface", stringBuilder.toString());
                    } else {
                        StringBuilder stringBuilder2;
                        if (dataFormat > 0) {
                            if (dataFormat < IFD_FORMAT_BYTES_PER_FORMAT.length) {
                                if (tag.isFormatCompatible(dataFormat)) {
                                    if (dataFormat == 7) {
                                        dataFormat = tag.primaryFormat;
                                    }
                                    byteCount = ((long) numberOfComponents) * ((long) IFD_FORMAT_BYTES_PER_FORMAT[dataFormat]);
                                    if (byteCount >= 0) {
                                        if (byteCount <= 2147483647L) {
                                            valid = true;
                                        }
                                    }
                                    stringBuilder2 = new StringBuilder();
                                    stringBuilder2.append("Skip the tag entry since the number of components is invalid: ");
                                    stringBuilder2.append(numberOfComponents);
                                    Log.w("ExifInterface", stringBuilder2.toString());
                                } else {
                                    stringBuilder = new StringBuilder();
                                    stringBuilder.append("Skip the tag entry since data format (");
                                    stringBuilder.append(IFD_FORMAT_NAMES[dataFormat]);
                                    stringBuilder.append(") is unexpected for tag: ");
                                    stringBuilder.append(tag.name);
                                    Log.w("ExifInterface", stringBuilder.toString());
                                }
                            }
                        }
                        stringBuilder2 = new StringBuilder();
                        stringBuilder2.append("Skip the tag entry since data format is invalid: ");
                        stringBuilder2.append(dataFormat);
                        Log.w("ExifInterface", stringBuilder2.toString());
                    }
                    long byteCount2 = byteCount;
                    if (valid) {
                        int dataFormat2;
                        int numberOfComponents2;
                        long nextEntryOffset2;
                        if (byteCount2 > 4) {
                            long nextEntryOffset3;
                            int offset = dataInputStream.readInt();
                            numberOfDirectoryEntry2 = numberOfDirectoryEntry;
                            if (exifInterface.mMimeType != (short) 7) {
                                dataFormat2 = dataFormat;
                                numberOfComponents2 = numberOfComponents;
                                nextEntryOffset3 = nextEntryOffset;
                                if (exifInterface.mMimeType == (short) 10 && "JpgFromRaw".equals(tag.name) != (short) 0) {
                                    exifInterface.mRw2JpgFromRawOffset = offset;
                                }
                            } else if ("MakerNote".equals(tag.name) != (short) 0) {
                                exifInterface.mOrfMakerNoteOffset = offset;
                                dataFormat2 = dataFormat;
                                numberOfComponents2 = numberOfComponents;
                                nextEntryOffset3 = nextEntryOffset;
                            } else if (s != (short) 6 || "ThumbnailImage".equals(tag.name) == (short) 0) {
                                dataFormat2 = dataFormat;
                                numberOfComponents2 = numberOfComponents;
                                nextEntryOffset3 = nextEntryOffset;
                            } else {
                                exifInterface.mOrfThumbnailOffset = offset;
                                exifInterface.mOrfThumbnailLength = numberOfComponents;
                                numberOfDirectoryEntry = ExifAttribute.createUShort(6, exifInterface.mExifByteOrder);
                                dataFormat2 = dataFormat;
                                numberOfComponents2 = numberOfComponents;
                                dataFormat = ExifAttribute.createULong((long) exifInterface.mOrfThumbnailOffset, exifInterface.mExifByteOrder);
                                nextEntryOffset3 = nextEntryOffset;
                                numberOfComponents = ExifAttribute.createULong((long) exifInterface.mOrfThumbnailLength, exifInterface.mExifByteOrder);
                                exifInterface.mAttributes[4].put("Compression", numberOfDirectoryEntry);
                                exifInterface.mAttributes[4].put("JPEGInterchangeFormat", dataFormat);
                                exifInterface.mAttributes[4].put("JPEGInterchangeFormatLength", numberOfComponents);
                            }
                            if (((long) offset) + byteCount2 <= ((long) dataInputStream.mLength)) {
                                byteOrderedDataInputStream.seek((long) offset);
                                nextEntryOffset2 = nextEntryOffset3;
                            } else {
                                StringBuilder stringBuilder3 = new StringBuilder();
                                stringBuilder3.append("Skip the tag entry since data offset is invalid: ");
                                stringBuilder3.append(offset);
                                Log.w("ExifInterface", stringBuilder3.toString());
                                byteOrderedDataInputStream.seek(nextEntryOffset3);
                            }
                        } else {
                            numberOfDirectoryEntry2 = numberOfDirectoryEntry;
                            dataFormat2 = dataFormat;
                            numberOfComponents2 = numberOfComponents;
                            nextEntryOffset2 = nextEntryOffset;
                        }
                        Integer numberOfDirectoryEntry3 = (Integer) sExifPointerTagMap.get(Integer.valueOf(tagNumber));
                        Integer nextIfdType;
                        if (numberOfDirectoryEntry3 != null) {
                            long offset2 = -1;
                            switch (dataFormat2) {
                                case 3:
                                    offset2 = (long) dataInputStream.readUnsignedShort();
                                    break;
                                case 4:
                                    offset2 = dataInputStream.readUnsignedInt();
                                    break;
                                case 8:
                                    offset2 = (long) dataInputStream.readShort();
                                    break;
                                case 9:
                                case 13:
                                    offset2 = (long) dataInputStream.readInt();
                                    break;
                                default:
                                    break;
                            }
                            if (offset2 <= 0 || offset2 >= ((long) dataInputStream.mLength)) {
                                StringBuilder stringBuilder4 = new StringBuilder();
                                nextIfdType = numberOfDirectoryEntry3;
                                stringBuilder4.append("Skip jump into the IFD since its offset is invalid: ");
                                stringBuilder4.append(offset2);
                                Log.w("ExifInterface", stringBuilder4.toString());
                            } else {
                                byteOrderedDataInputStream.seek(offset2);
                                readImageFileDirectory(byteOrderedDataInputStream, numberOfDirectoryEntry3.intValue());
                                nextIfdType = numberOfDirectoryEntry3;
                            }
                            byteOrderedDataInputStream.seek(nextEntryOffset2);
                        } else {
                            nextIfdType = numberOfDirectoryEntry3;
                            numberOfDirectoryEntry = new byte[((int) byteCount2)];
                            byteOrderedDataInputStream.readFully(numberOfDirectoryEntry);
                            ExifAttribute attribute = new ExifAttribute(dataFormat2, numberOfComponents2, numberOfDirectoryEntry);
                            exifInterface.mAttributes[s].put(tag.name, attribute);
                            if ("DNGVersion".equals(tag.name)) {
                                exifInterface.mMimeType = 3;
                            }
                            if ((("Make".equals(tag.name) || "Model".equals(tag.name)) && attribute.getStringValue(exifInterface.mExifByteOrder).contains("PENTAX")) || ("Compression".equals(tag.name) && attribute.getIntValue(exifInterface.mExifByteOrder) == 65535)) {
                                exifInterface.mMimeType = 8;
                            }
                            byte[] bytes = numberOfDirectoryEntry;
                            if (((long) dataInputStream.peek()) != nextEntryOffset2) {
                                byteOrderedDataInputStream.seek(nextEntryOffset2);
                            }
                        }
                    } else {
                        byteOrderedDataInputStream.seek(nextEntryOffset);
                        numberOfDirectoryEntry2 = numberOfDirectoryEntry;
                    }
                    i = (short) (i + 1);
                    numberOfDirectoryEntry = numberOfDirectoryEntry2;
                    s = ifdType;
                }
                if (dataInputStream.peek() + 4 <= dataInputStream.mLength) {
                    int nextIfdOffset = dataInputStream.readInt();
                    if (nextIfdOffset > 8 && nextIfdOffset < dataInputStream.mLength) {
                        byteOrderedDataInputStream.seek((long) nextIfdOffset);
                        if (exifInterface.mAttributes[4].isEmpty()) {
                            readImageFileDirectory(byteOrderedDataInputStream, 4);
                        } else if (exifInterface.mAttributes[5].isEmpty()) {
                            readImageFileDirectory(byteOrderedDataInputStream, 5);
                        }
                    }
                }
            }
        }
    }

    private void retrieveJpegImageSize(ByteOrderedDataInputStream in, int imageType) throws IOException {
        ExifAttribute imageWidthAttribute = (ExifAttribute) this.mAttributes[imageType].get("ImageWidth");
        if (((ExifAttribute) this.mAttributes[imageType].get("ImageLength")) == null || imageWidthAttribute == null) {
            ExifAttribute jpegInterchangeFormatAttribute = (ExifAttribute) this.mAttributes[imageType].get("JPEGInterchangeFormat");
            if (jpegInterchangeFormatAttribute != null) {
                getJpegAttributes(in, jpegInterchangeFormatAttribute.getIntValue(this.mExifByteOrder), imageType);
            }
        }
    }

    private void setThumbnailData(ByteOrderedDataInputStream in) throws IOException {
        HashMap thumbnailData = this.mAttributes[4];
        ExifAttribute compressionAttribute = (ExifAttribute) thumbnailData.get("Compression");
        if (compressionAttribute != null) {
            this.mThumbnailCompression = compressionAttribute.getIntValue(this.mExifByteOrder);
            int i = this.mThumbnailCompression;
            if (i != 1) {
                switch (i) {
                    case 6:
                        handleThumbnailFromJfif(in, thumbnailData);
                        return;
                    case 7:
                        break;
                    default:
                        return;
                }
            }
            if (isSupportedDataType(thumbnailData)) {
                handleThumbnailFromStrips(in, thumbnailData);
                return;
            }
            return;
        }
        this.mThumbnailCompression = 6;
        handleThumbnailFromJfif(in, thumbnailData);
    }

    private void handleThumbnailFromJfif(ByteOrderedDataInputStream in, HashMap thumbnailData) throws IOException {
        ExifAttribute jpegInterchangeFormatAttribute = (ExifAttribute) thumbnailData.get("JPEGInterchangeFormat");
        ExifAttribute jpegInterchangeFormatLengthAttribute = (ExifAttribute) thumbnailData.get("JPEGInterchangeFormatLength");
        if (jpegInterchangeFormatAttribute != null && jpegInterchangeFormatLengthAttribute != null) {
            int thumbnailOffset = jpegInterchangeFormatAttribute.getIntValue(this.mExifByteOrder);
            int thumbnailLength = Math.min(jpegInterchangeFormatLengthAttribute.getIntValue(this.mExifByteOrder), in.available() - thumbnailOffset);
            if (!(this.mMimeType == 4 || this.mMimeType == 9)) {
                if (this.mMimeType != 10) {
                    if (this.mMimeType == 7) {
                        thumbnailOffset += this.mOrfMakerNoteOffset;
                    }
                    if (thumbnailOffset > 0 && thumbnailLength > 0) {
                        this.mHasThumbnail = true;
                        this.mThumbnailOffset = thumbnailOffset;
                        this.mThumbnailLength = thumbnailLength;
                        if (this.mFilename == null && this.mAssetInputStream == null) {
                            byte[] thumbnailBytes = new byte[thumbnailLength];
                            in.seek((long) thumbnailOffset);
                            in.readFully(thumbnailBytes);
                            this.mThumbnailBytes = thumbnailBytes;
                            return;
                        }
                        return;
                    }
                }
            }
            thumbnailOffset += this.mExifOffset;
            if (thumbnailOffset > 0) {
            }
        }
    }

    private void handleThumbnailFromStrips(ByteOrderedDataInputStream in, HashMap thumbnailData) throws IOException {
        ExifInterface exifInterface = this;
        ByteOrderedDataInputStream byteOrderedDataInputStream = in;
        HashMap hashMap = thumbnailData;
        ExifAttribute stripOffsetsAttribute = (ExifAttribute) hashMap.get("StripOffsets");
        ExifAttribute stripByteCountsAttribute = (ExifAttribute) hashMap.get("StripByteCounts");
        if (stripOffsetsAttribute == null || stripByteCountsAttribute == null) {
        } else {
            long[] stripOffsets = convertToLongArray(stripOffsetsAttribute.getValue(exifInterface.mExifByteOrder));
            long[] stripByteCounts = convertToLongArray(stripByteCountsAttribute.getValue(exifInterface.mExifByteOrder));
            if (stripOffsets == null) {
                Log.w("ExifInterface", "stripOffsets should not be null.");
            } else if (stripByteCounts == null) {
                Log.w("ExifInterface", "stripByteCounts should not be null.");
            } else {
                int bytesRead;
                long totalStripByteCount = 0;
                int totalStripByteCount2 = 0;
                while (totalStripByteCount2 < stripByteCounts.length) {
                    totalStripByteCount2++;
                    totalStripByteCount += stripByteCounts[totalStripByteCount2];
                }
                byte[] totalStripBytes = new byte[((int) totalStripByteCount)];
                int bytesAdded = 0;
                int bytesRead2 = 0;
                int i = 0;
                while (i < stripOffsets.length) {
                    bytesRead = bytesRead2;
                    int stripByteCount = (int) stripByteCounts[i];
                    bytesRead2 = ((int) stripOffsets[i]) - bytesRead;
                    if (bytesRead2 < 0) {
                        Log.d("ExifInterface", "Invalid strip offset value");
                    }
                    ExifAttribute stripOffsetsAttribute2 = stripOffsetsAttribute;
                    byteOrderedDataInputStream.seek((long) bytesRead2);
                    int bytesRead3 = bytesRead + bytesRead2;
                    stripOffsetsAttribute = new byte[stripByteCount];
                    byteOrderedDataInputStream.read(stripOffsetsAttribute);
                    bytesRead3 += stripByteCount;
                    System.arraycopy(stripOffsetsAttribute, 0, totalStripBytes, bytesAdded, stripOffsetsAttribute.length);
                    bytesAdded += stripOffsetsAttribute.length;
                    i++;
                    bytesRead2 = bytesRead3;
                    stripOffsetsAttribute = stripOffsetsAttribute2;
                    byteOrderedDataInputStream = in;
                    bytesRead3 = thumbnailData;
                }
                bytesRead = bytesRead2;
                exifInterface.mHasThumbnail = true;
                exifInterface.mThumbnailBytes = totalStripBytes;
                exifInterface.mThumbnailLength = totalStripBytes.length;
            }
        }
    }

    private boolean isSupportedDataType(HashMap thumbnailData) throws IOException {
        ExifAttribute bitsPerSampleAttribute = (ExifAttribute) thumbnailData.get("BitsPerSample");
        if (bitsPerSampleAttribute != null) {
            int[] bitsPerSampleValue = (int[]) bitsPerSampleAttribute.getValue(this.mExifByteOrder);
            if (Arrays.equals(BITS_PER_SAMPLE_RGB, bitsPerSampleValue)) {
                return true;
            }
            if (this.mMimeType == 3) {
                ExifAttribute photometricInterpretationAttribute = (ExifAttribute) thumbnailData.get("PhotometricInterpretation");
                if (photometricInterpretationAttribute != null) {
                    int photometricInterpretationValue = photometricInterpretationAttribute.getIntValue(this.mExifByteOrder);
                    if ((photometricInterpretationValue == 1 && Arrays.equals(bitsPerSampleValue, BITS_PER_SAMPLE_GREYSCALE_2)) || (photometricInterpretationValue == 6 && Arrays.equals(bitsPerSampleValue, BITS_PER_SAMPLE_RGB))) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean isThumbnail(HashMap map) throws IOException {
        ExifAttribute imageLengthAttribute = (ExifAttribute) map.get("ImageLength");
        ExifAttribute imageWidthAttribute = (ExifAttribute) map.get("ImageWidth");
        if (!(imageLengthAttribute == null || imageWidthAttribute == null)) {
            int imageLengthValue = imageLengthAttribute.getIntValue(this.mExifByteOrder);
            int imageWidthValue = imageWidthAttribute.getIntValue(this.mExifByteOrder);
            if (imageLengthValue <= 512 && imageWidthValue <= 512) {
                return true;
            }
        }
        return false;
    }

    private void validateImages(InputStream in) throws IOException {
        swapBasedOnImageSize(0, 5);
        swapBasedOnImageSize(0, 4);
        swapBasedOnImageSize(5, 4);
        ExifAttribute pixelXDimAttribute = (ExifAttribute) this.mAttributes[1].get("PixelXDimension");
        ExifAttribute pixelYDimAttribute = (ExifAttribute) this.mAttributes[1].get("PixelYDimension");
        if (!(pixelXDimAttribute == null || pixelYDimAttribute == null)) {
            this.mAttributes[0].put("ImageWidth", pixelXDimAttribute);
            this.mAttributes[0].put("ImageLength", pixelYDimAttribute);
        }
        if (this.mAttributes[4].isEmpty() && isThumbnail(this.mAttributes[5])) {
            this.mAttributes[4] = this.mAttributes[5];
            this.mAttributes[5] = new HashMap();
        }
        if (!isThumbnail(this.mAttributes[4])) {
            Log.d("ExifInterface", "No image meets the size requirements of a thumbnail image.");
        }
    }

    private void updateImageSizeValues(ByteOrderedDataInputStream in, int imageType) throws IOException {
        ExifAttribute defaultCropSizeAttribute = (ExifAttribute) this.mAttributes[imageType].get("DefaultCropSize");
        ExifAttribute topBorderAttribute = (ExifAttribute) this.mAttributes[imageType].get("SensorTopBorder");
        ExifAttribute leftBorderAttribute = (ExifAttribute) this.mAttributes[imageType].get("SensorLeftBorder");
        ExifAttribute bottomBorderAttribute = (ExifAttribute) this.mAttributes[imageType].get("SensorBottomBorder");
        ExifAttribute rightBorderAttribute = (ExifAttribute) this.mAttributes[imageType].get("SensorRightBorder");
        if (defaultCropSizeAttribute != null) {
            ExifAttribute defaultCropSizeXAttribute;
            int[] defaultCropSizeValue;
            StringBuilder stringBuilder;
            if (defaultCropSizeAttribute.format == 5) {
                Rational[] defaultCropSizeValue2 = (Rational[]) defaultCropSizeAttribute.getValue(r0.mExifByteOrder);
                if (defaultCropSizeValue2 != null) {
                    if (defaultCropSizeValue2.length == 2) {
                        defaultCropSizeXAttribute = ExifAttribute.createURational(defaultCropSizeValue2[0], r0.mExifByteOrder);
                        defaultCropSizeValue = ExifAttribute.createURational(defaultCropSizeValue2[1], r0.mExifByteOrder);
                    }
                }
                stringBuilder = new StringBuilder();
                stringBuilder.append("Invalid crop size values. cropSize=");
                stringBuilder.append(Arrays.toString(defaultCropSizeValue2));
                Log.w("ExifInterface", stringBuilder.toString());
                return;
            }
            defaultCropSizeValue = (int[]) defaultCropSizeAttribute.getValue(r0.mExifByteOrder);
            if (defaultCropSizeValue != null) {
                if (defaultCropSizeValue.length == 2) {
                    defaultCropSizeXAttribute = ExifAttribute.createUShort(defaultCropSizeValue[0], r0.mExifByteOrder);
                    defaultCropSizeValue = ExifAttribute.createUShort(defaultCropSizeValue[1], r0.mExifByteOrder);
                }
            }
            stringBuilder = new StringBuilder();
            stringBuilder.append("Invalid crop size values. cropSize=");
            stringBuilder.append(Arrays.toString(defaultCropSizeValue));
            Log.w("ExifInterface", stringBuilder.toString());
            return;
            r0.mAttributes[imageType].put("ImageWidth", defaultCropSizeXAttribute);
            r0.mAttributes[imageType].put("ImageLength", defaultCropSizeValue);
            ExifAttribute exifAttribute = defaultCropSizeAttribute;
        } else if (topBorderAttribute == null || leftBorderAttribute == null || bottomBorderAttribute == null || rightBorderAttribute == null) {
            retrieveJpegImageSize(in, imageType);
        } else {
            int topBorderValue = topBorderAttribute.getIntValue(r0.mExifByteOrder);
            int bottomBorderValue = bottomBorderAttribute.getIntValue(r0.mExifByteOrder);
            int rightBorderValue = rightBorderAttribute.getIntValue(r0.mExifByteOrder);
            int leftBorderValue = leftBorderAttribute.getIntValue(r0.mExifByteOrder);
            if (bottomBorderValue <= topBorderValue || rightBorderValue <= leftBorderValue) {
            } else {
                int width = rightBorderValue - leftBorderValue;
                ExifAttribute imageLengthAttribute = ExifAttribute.createUShort(bottomBorderValue - topBorderValue, r0.mExifByteOrder);
                ExifAttribute imageWidthAttribute = ExifAttribute.createUShort(width, r0.mExifByteOrder);
                r0.mAttributes[imageType].put("ImageLength", imageLengthAttribute);
                r0.mAttributes[imageType].put("ImageWidth", imageWidthAttribute);
            }
        }
    }

    private void swapBasedOnImageSize(int firstIfdType, int secondIfdType) throws IOException {
        if (!this.mAttributes[firstIfdType].isEmpty()) {
            if (!this.mAttributes[secondIfdType].isEmpty()) {
                ExifAttribute firstImageLengthAttribute = (ExifAttribute) this.mAttributes[firstIfdType].get("ImageLength");
                ExifAttribute firstImageWidthAttribute = (ExifAttribute) this.mAttributes[firstIfdType].get("ImageWidth");
                ExifAttribute secondImageLengthAttribute = (ExifAttribute) this.mAttributes[secondIfdType].get("ImageLength");
                ExifAttribute secondImageWidthAttribute = (ExifAttribute) this.mAttributes[secondIfdType].get("ImageWidth");
                if (firstImageLengthAttribute != null) {
                    if (firstImageWidthAttribute != null) {
                        if (secondImageLengthAttribute != null) {
                            if (secondImageWidthAttribute != null) {
                                int firstImageLengthValue = firstImageLengthAttribute.getIntValue(this.mExifByteOrder);
                                int firstImageWidthValue = firstImageWidthAttribute.getIntValue(this.mExifByteOrder);
                                int secondImageLengthValue = secondImageLengthAttribute.getIntValue(this.mExifByteOrder);
                                int secondImageWidthValue = secondImageWidthAttribute.getIntValue(this.mExifByteOrder);
                                if (firstImageLengthValue < secondImageLengthValue && firstImageWidthValue < secondImageWidthValue) {
                                    HashMap<String, ExifAttribute> tempMap = this.mAttributes[firstIfdType];
                                    this.mAttributes[firstIfdType] = this.mAttributes[secondIfdType];
                                    this.mAttributes[secondIfdType] = tempMap;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (RuntimeException rethrown) {
                throw rethrown;
            } catch (Exception e) {
            }
        }
    }

    private static long[] convertToLongArray(Object inputObj) {
        if (inputObj instanceof int[]) {
            int[] input = (int[]) inputObj;
            long[] result = new long[input.length];
            for (int i = 0; i < input.length; i++) {
                result[i] = (long) input[i];
            }
            return result;
        } else if (inputObj instanceof long[]) {
            return (long[]) inputObj;
        } else {
            return null;
        }
    }
}
