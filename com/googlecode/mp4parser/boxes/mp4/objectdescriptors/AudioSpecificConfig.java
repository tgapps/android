package com.googlecode.mp4parser.boxes.mp4.objectdescriptors;

import com.coremedia.iso.Hex;
import com.coremedia.iso.IsoTypeWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.telegram.messenger.exoplayer2.RendererCapabilities;
import org.telegram.messenger.exoplayer2.extractor.ts.TsExtractor;

@Descriptor(objectTypeIndication = 64, tags = {5})
public class AudioSpecificConfig extends BaseDescriptor {
    public static Map<Integer, String> audioObjectTypeMap;
    public static Map<Integer, Integer> samplingFrequencyIndexMap;
    public boolean aacScalefactorDataResilienceFlag;
    public boolean aacSectionDataResilienceFlag;
    public boolean aacSpectralDataResilienceFlag;
    public int audioObjectType;
    public int channelConfiguration;
    byte[] configBytes;
    public int coreCoderDelay;
    public int dependsOnCoreCoder;
    public int directMapping;
    public ELDSpecificConfig eldSpecificConfig;
    public int epConfig;
    public int erHvxcExtensionFlag;
    public int extensionAudioObjectType;
    public int extensionChannelConfiguration;
    public int extensionFlag;
    public int extensionFlag3;
    public int extensionSamplingFrequency;
    public int extensionSamplingFrequencyIndex;
    public int fillBits;
    public int frameLengthFlag;
    public boolean gaSpecificConfig;
    public int hilnContMode;
    public int hilnEnhaLayer;
    public int hilnEnhaQuantMode;
    public int hilnFrameLength;
    public int hilnMaxNumLine;
    public int hilnQuantMode;
    public int hilnSampleRateCode;
    public int hvxcRateMode;
    public int hvxcVarMode;
    public int isBaseLayer;
    public int layerNr;
    public int layer_length;
    public int numOfSubFrame;
    public int paraExtensionFlag;
    public int paraMode;
    public boolean parametricSpecificConfig;
    public boolean psPresentFlag;
    public int sacPayloadEmbedding;
    public int samplingFrequency;
    public int samplingFrequencyIndex;
    public boolean sbrPresentFlag;
    public int syncExtensionType;
    public int var_ScalableFlag;

    public class ELDSpecificConfig {
        public boolean aacScalefactorDataResilienceFlag;
        public boolean aacSectionDataResilienceFlag;
        public boolean aacSpectralDataResilienceFlag;
        public boolean frameLengthFlag;
        public boolean ldSbrCrcFlag;
        public boolean ldSbrPresentFlag;
        public boolean ldSbrSamplingRate;
        final /* synthetic */ AudioSpecificConfig this$0;

        public ELDSpecificConfig(com.googlecode.mp4parser.boxes.mp4.objectdescriptors.AudioSpecificConfig r1, int r2, com.googlecode.mp4parser.boxes.mp4.objectdescriptors.BitReaderBuffer r3) {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: com.googlecode.mp4parser.boxes.mp4.objectdescriptors.AudioSpecificConfig.ELDSpecificConfig.<init>(com.googlecode.mp4parser.boxes.mp4.objectdescriptors.AudioSpecificConfig, int, com.googlecode.mp4parser.boxes.mp4.objectdescriptors.BitReaderBuffer):void
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
            r5.this$0 = r6;
            r5.<init>();
            r6 = r8.readBool();
            r5.frameLengthFlag = r6;
            r6 = r8.readBool();
            r5.aacSectionDataResilienceFlag = r6;
            r6 = r8.readBool();
            r5.aacScalefactorDataResilienceFlag = r6;
            r6 = r8.readBool();
            r5.aacSpectralDataResilienceFlag = r6;
            r6 = r8.readBool();
            r5.ldSbrPresentFlag = r6;
            r6 = r5.ldSbrPresentFlag;
            if (r6 == 0) goto L_0x0036;
        L_0x0027:
            r6 = r8.readBool();
            r5.ldSbrSamplingRate = r6;
            r6 = r8.readBool();
            r5.ldSbrCrcFlag = r6;
            r5.ld_sbr_header(r7, r8);
        L_0x0036:
            r6 = 4;
            r0 = r8.readBits(r6);
            r1 = r0;
            if (r0 != 0) goto L_0x003f;
        L_0x003e:
            return;
            r6 = r8.readBits(r6);
            r0 = r6;
            r2 = 0;
            r3 = 15;
            r4 = 8;
            if (r6 != r3) goto L_0x0051;
            r2 = r8.readBits(r4);
            r0 = r0 + r2;
            r3 = 255; // 0xff float:3.57E-43 double:1.26E-321;
            if (r2 != r3) goto L_0x005c;
            r3 = 16;
            r3 = r8.readBits(r3);
            r0 = r0 + r3;
            r3 = 0;
            if (r3 < r0) goto L_0x0061;
            goto L_0x0036;
            r8.readBits(r4);
            r3 = r3 + 1;
            goto L_0x005e;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.googlecode.mp4parser.boxes.mp4.objectdescriptors.AudioSpecificConfig.ELDSpecificConfig.<init>(com.googlecode.mp4parser.boxes.mp4.objectdescriptors.AudioSpecificConfig, int, com.googlecode.mp4parser.boxes.mp4.objectdescriptors.BitReaderBuffer):void");
        }

        public void ld_sbr_header(int r1, com.googlecode.mp4parser.boxes.mp4.objectdescriptors.BitReaderBuffer r2) {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: com.googlecode.mp4parser.boxes.mp4.objectdescriptors.AudioSpecificConfig.ELDSpecificConfig.ld_sbr_header(int, com.googlecode.mp4parser.boxes.mp4.objectdescriptors.BitReaderBuffer):void
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
            switch(r5) {
                case 1: goto L_0x000b;
                case 2: goto L_0x000b;
                case 3: goto L_0x0009;
                case 4: goto L_0x0007;
                case 5: goto L_0x0007;
                case 6: goto L_0x0007;
                case 7: goto L_0x0005;
                default: goto L_0x0003;
            };
        L_0x0003:
            r0 = 0;
            goto L_0x000d;
        L_0x0005:
            r0 = 4;
            goto L_0x000d;
        L_0x0007:
            r0 = 3;
            goto L_0x000d;
        L_0x0009:
            r0 = 2;
            goto L_0x000d;
        L_0x000b:
            r0 = 1;
            r1 = 0;
            if (r1 < r0) goto L_0x0012;
            return;
            r2 = new com.googlecode.mp4parser.boxes.mp4.objectdescriptors.AudioSpecificConfig$sbr_header;
            r3 = r4.this$0;
            r2.<init>(r3, r6);
            r1 = r1 + 1;
            goto L_0x000f;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.googlecode.mp4parser.boxes.mp4.objectdescriptors.AudioSpecificConfig.ELDSpecificConfig.ld_sbr_header(int, com.googlecode.mp4parser.boxes.mp4.objectdescriptors.BitReaderBuffer):void");
        }
    }

    public class sbr_header {
        public boolean bs_alter_scale;
        public boolean bs_amp_res;
        public int bs_freq_scale;
        public boolean bs_header_extra_1;
        public boolean bs_header_extra_2;
        public boolean bs_interpol_freq;
        public int bs_limiter_bands;
        public int bs_limiter_gains;
        public int bs_noise_bands;
        public int bs_reserved;
        public boolean bs_smoothing_mode;
        public int bs_start_freq;
        public int bs_stop_freq;
        public int bs_xover_band;
        final /* synthetic */ AudioSpecificConfig this$0;

        public sbr_header(AudioSpecificConfig audioSpecificConfig, BitReaderBuffer b) {
            this.this$0 = audioSpecificConfig;
            this.bs_amp_res = b.readBool();
            this.bs_start_freq = b.readBits(4);
            this.bs_stop_freq = b.readBits(4);
            this.bs_xover_band = b.readBits(3);
            this.bs_reserved = b.readBits(2);
            this.bs_header_extra_1 = b.readBool();
            this.bs_header_extra_2 = b.readBool();
            if (this.bs_header_extra_1) {
                this.bs_freq_scale = b.readBits(2);
                this.bs_alter_scale = b.readBool();
                this.bs_noise_bands = b.readBits(2);
            }
            if (this.bs_header_extra_2) {
                this.bs_limiter_bands = b.readBits(2);
                this.bs_limiter_gains = b.readBits(2);
                this.bs_interpol_freq = b.readBool();
            }
            this.bs_smoothing_mode = b.readBool();
        }
    }

    public AudioSpecificConfig() {
    }

    static {
        samplingFrequencyIndexMap = new HashMap();
        audioObjectTypeMap = new HashMap();
        samplingFrequencyIndexMap.put(Integer.valueOf(0), Integer.valueOf(96000));
        samplingFrequencyIndexMap.put(Integer.valueOf(1), Integer.valueOf(88200));
        samplingFrequencyIndexMap.put(Integer.valueOf(2), Integer.valueOf(64000));
        samplingFrequencyIndexMap.put(Integer.valueOf(3), Integer.valueOf(48000));
        samplingFrequencyIndexMap.put(Integer.valueOf(4), Integer.valueOf(44100));
        samplingFrequencyIndexMap.put(Integer.valueOf(5), Integer.valueOf(32000));
        samplingFrequencyIndexMap.put(Integer.valueOf(6), Integer.valueOf(24000));
        samplingFrequencyIndexMap.put(Integer.valueOf(7), Integer.valueOf(22050));
        samplingFrequencyIndexMap.put(Integer.valueOf(8), Integer.valueOf(16000));
        samplingFrequencyIndexMap.put(Integer.valueOf(9), Integer.valueOf(12000));
        samplingFrequencyIndexMap.put(Integer.valueOf(10), Integer.valueOf(11025));
        samplingFrequencyIndexMap.put(Integer.valueOf(11), Integer.valueOf(8000));
        audioObjectTypeMap.put(Integer.valueOf(1), "AAC main");
        audioObjectTypeMap.put(Integer.valueOf(2), "AAC LC");
        audioObjectTypeMap.put(Integer.valueOf(3), "AAC SSR");
        audioObjectTypeMap.put(Integer.valueOf(4), "AAC LTP");
        audioObjectTypeMap.put(Integer.valueOf(5), "SBR");
        audioObjectTypeMap.put(Integer.valueOf(6), "AAC Scalable");
        audioObjectTypeMap.put(Integer.valueOf(7), "TwinVQ");
        audioObjectTypeMap.put(Integer.valueOf(8), "CELP");
        audioObjectTypeMap.put(Integer.valueOf(9), "HVXC");
        audioObjectTypeMap.put(Integer.valueOf(10), "(reserved)");
        audioObjectTypeMap.put(Integer.valueOf(11), "(reserved)");
        audioObjectTypeMap.put(Integer.valueOf(12), "TTSI");
        audioObjectTypeMap.put(Integer.valueOf(13), "Main synthetic");
        audioObjectTypeMap.put(Integer.valueOf(14), "Wavetable synthesis");
        audioObjectTypeMap.put(Integer.valueOf(15), "General MIDI");
        audioObjectTypeMap.put(Integer.valueOf(16), "Algorithmic Synthesis and Audio FX");
        audioObjectTypeMap.put(Integer.valueOf(17), "ER AAC LC");
        audioObjectTypeMap.put(Integer.valueOf(18), "(reserved)");
        audioObjectTypeMap.put(Integer.valueOf(19), "ER AAC LTP");
        audioObjectTypeMap.put(Integer.valueOf(20), "ER AAC Scalable");
        audioObjectTypeMap.put(Integer.valueOf(21), "ER TwinVQ");
        audioObjectTypeMap.put(Integer.valueOf(22), "ER BSAC");
        audioObjectTypeMap.put(Integer.valueOf(23), "ER AAC LD");
        audioObjectTypeMap.put(Integer.valueOf(24), "ER CELP");
        audioObjectTypeMap.put(Integer.valueOf(25), "ER HVXC");
        audioObjectTypeMap.put(Integer.valueOf(26), "ER HILN");
        audioObjectTypeMap.put(Integer.valueOf(27), "ER Parametric");
        audioObjectTypeMap.put(Integer.valueOf(28), "SSC");
        audioObjectTypeMap.put(Integer.valueOf(29), "PS");
        audioObjectTypeMap.put(Integer.valueOf(30), "MPEG Surround");
        audioObjectTypeMap.put(Integer.valueOf(31), "(escape)");
        audioObjectTypeMap.put(Integer.valueOf(32), "Layer-1");
        audioObjectTypeMap.put(Integer.valueOf(33), "Layer-2");
        audioObjectTypeMap.put(Integer.valueOf(34), "Layer-3");
        audioObjectTypeMap.put(Integer.valueOf(35), "DST");
        audioObjectTypeMap.put(Integer.valueOf(36), "ALS");
        audioObjectTypeMap.put(Integer.valueOf(37), "SLS");
        audioObjectTypeMap.put(Integer.valueOf(38), "SLS non-core");
        audioObjectTypeMap.put(Integer.valueOf(39), "ER AAC ELD");
        audioObjectTypeMap.put(Integer.valueOf(40), "SMR Simple");
        audioObjectTypeMap.put(Integer.valueOf(41), "SMR Main");
    }

    public void parseDetail(ByteBuffer bb) throws IOException {
        int i;
        ByteBuffer configBytes = bb.slice();
        configBytes.limit(this.sizeOfInstance);
        bb.position(bb.position() + this.sizeOfInstance);
        this.configBytes = new byte[this.sizeOfInstance];
        configBytes.get(this.configBytes);
        configBytes.rewind();
        BitReaderBuffer bitReaderBuffer = new BitReaderBuffer(configBytes);
        this.audioObjectType = getAudioObjectType(bitReaderBuffer);
        this.samplingFrequencyIndex = bitReaderBuffer.readBits(4);
        if (this.samplingFrequencyIndex == 15) {
            this.samplingFrequency = bitReaderBuffer.readBits(24);
        }
        this.channelConfiguration = bitReaderBuffer.readBits(4);
        if (this.audioObjectType != 5) {
            if (this.audioObjectType != 29) {
                this.extensionAudioObjectType = 0;
                switch (this.audioObjectType) {
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                    case 6:
                    case 7:
                    case 17:
                    case 19:
                    case 20:
                    case 21:
                    case 22:
                    case 23:
                        parseGaSpecificConfig(this.samplingFrequencyIndex, this.channelConfiguration, this.audioObjectType, bitReaderBuffer);
                        break;
                    case 8:
                        throw new UnsupportedOperationException("can't parse CelpSpecificConfig yet");
                    case 9:
                        throw new UnsupportedOperationException("can't parse HvxcSpecificConfig yet");
                    case 12:
                        throw new UnsupportedOperationException("can't parse TTSSpecificConfig yet");
                    case 13:
                    case 14:
                    case 15:
                    case 16:
                        throw new UnsupportedOperationException("can't parse StructuredAudioSpecificConfig yet");
                    case RendererCapabilities.ADAPTIVE_SUPPORT_MASK /*24*/:
                        throw new UnsupportedOperationException("can't parse ErrorResilientCelpSpecificConfig yet");
                    case 25:
                        throw new UnsupportedOperationException("can't parse ErrorResilientHvxcSpecificConfig yet");
                    case 26:
                    case 27:
                        parseParametricSpecificConfig(this.samplingFrequencyIndex, this.channelConfiguration, this.audioObjectType, bitReaderBuffer);
                        break;
                    case 28:
                        throw new UnsupportedOperationException("can't parse SSCSpecificConfig yet");
                    case 30:
                        this.sacPayloadEmbedding = bitReaderBuffer.readBits(1);
                        throw new UnsupportedOperationException("can't parse SpatialSpecificConfig yet");
                    case 32:
                    case 33:
                    case 34:
                        throw new UnsupportedOperationException("can't parse MPEG_1_2_SpecificConfig yet");
                    case 35:
                        throw new UnsupportedOperationException("can't parse DSTSpecificConfig yet");
                    case TsExtractor.TS_STREAM_TYPE_H265 /*36*/:
                        this.fillBits = bitReaderBuffer.readBits(5);
                        throw new UnsupportedOperationException("can't parse ALSSpecificConfig yet");
                    case 37:
                    case 38:
                        throw new UnsupportedOperationException("can't parse SLSSpecificConfig yet");
                    case 39:
                        this.eldSpecificConfig = new ELDSpecificConfig(this, this.channelConfiguration, bitReaderBuffer);
                        break;
                    case 40:
                    case 41:
                        throw new UnsupportedOperationException("can't parse SymbolicMusicSpecificConfig yet");
                    default:
                        break;
                }
                i = this.audioObjectType;
                if (!(i == 17 || i == 39)) {
                    switch (i) {
                        case 19:
                        case 20:
                        case 21:
                        case 22:
                        case 23:
                        case RendererCapabilities.ADAPTIVE_SUPPORT_MASK /*24*/:
                        case 25:
                        case 26:
                        case 27:
                            break;
                        default:
                            break;
                    }
                }
                this.epConfig = bitReaderBuffer.readBits(2);
                if (this.epConfig != 2) {
                    if (this.epConfig == 3) {
                        if (this.epConfig == 3) {
                            this.directMapping = bitReaderBuffer.readBits(1);
                            if (this.directMapping == 0) {
                                throw new RuntimeException("not implemented");
                            }
                        }
                        if (this.extensionAudioObjectType != 5 && bitReaderBuffer.remainingBits() >= 16) {
                            this.syncExtensionType = bitReaderBuffer.readBits(11);
                            if (this.syncExtensionType == 695) {
                                this.extensionAudioObjectType = getAudioObjectType(bitReaderBuffer);
                                if (this.extensionAudioObjectType == 5) {
                                    this.sbrPresentFlag = bitReaderBuffer.readBool();
                                    if (this.sbrPresentFlag) {
                                        this.extensionSamplingFrequencyIndex = bitReaderBuffer.readBits(4);
                                        if (this.extensionSamplingFrequencyIndex == 15) {
                                            this.extensionSamplingFrequency = bitReaderBuffer.readBits(24);
                                        }
                                        if (bitReaderBuffer.remainingBits() >= 12) {
                                            this.syncExtensionType = bitReaderBuffer.readBits(11);
                                            if (this.syncExtensionType == 1352) {
                                                this.psPresentFlag = bitReaderBuffer.readBool();
                                            }
                                        }
                                    }
                                }
                                if (this.extensionAudioObjectType == 22) {
                                    this.sbrPresentFlag = bitReaderBuffer.readBool();
                                    if (this.sbrPresentFlag) {
                                        this.extensionSamplingFrequencyIndex = bitReaderBuffer.readBits(4);
                                        if (this.extensionSamplingFrequencyIndex == 15) {
                                            this.extensionSamplingFrequency = bitReaderBuffer.readBits(24);
                                        }
                                    }
                                    this.extensionChannelConfiguration = bitReaderBuffer.readBits(4);
                                    return;
                                }
                                return;
                            }
                            return;
                        }
                        return;
                    }
                }
                throw new UnsupportedOperationException("can't parse ErrorProtectionSpecificConfig yet");
            }
        }
        this.extensionAudioObjectType = 5;
        this.sbrPresentFlag = true;
        if (this.audioObjectType == 29) {
            this.psPresentFlag = true;
        }
        this.extensionSamplingFrequencyIndex = bitReaderBuffer.readBits(4);
        if (this.extensionSamplingFrequencyIndex == 15) {
            this.extensionSamplingFrequency = bitReaderBuffer.readBits(24);
        }
        this.audioObjectType = getAudioObjectType(bitReaderBuffer);
        if (this.audioObjectType == 22) {
            this.extensionChannelConfiguration = bitReaderBuffer.readBits(4);
        }
        switch (this.audioObjectType) {
            case 1:
            case 2:
            case 3:
            case 4:
            case 6:
            case 7:
            case 17:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
                parseGaSpecificConfig(this.samplingFrequencyIndex, this.channelConfiguration, this.audioObjectType, bitReaderBuffer);
                break;
            case 8:
                throw new UnsupportedOperationException("can't parse CelpSpecificConfig yet");
            case 9:
                throw new UnsupportedOperationException("can't parse HvxcSpecificConfig yet");
            case 12:
                throw new UnsupportedOperationException("can't parse TTSSpecificConfig yet");
            case 13:
            case 14:
            case 15:
            case 16:
                throw new UnsupportedOperationException("can't parse StructuredAudioSpecificConfig yet");
            case RendererCapabilities.ADAPTIVE_SUPPORT_MASK /*24*/:
                throw new UnsupportedOperationException("can't parse ErrorResilientCelpSpecificConfig yet");
            case 25:
                throw new UnsupportedOperationException("can't parse ErrorResilientHvxcSpecificConfig yet");
            case 26:
            case 27:
                parseParametricSpecificConfig(this.samplingFrequencyIndex, this.channelConfiguration, this.audioObjectType, bitReaderBuffer);
                break;
            case 28:
                throw new UnsupportedOperationException("can't parse SSCSpecificConfig yet");
            case 30:
                this.sacPayloadEmbedding = bitReaderBuffer.readBits(1);
                throw new UnsupportedOperationException("can't parse SpatialSpecificConfig yet");
            case 32:
            case 33:
            case 34:
                throw new UnsupportedOperationException("can't parse MPEG_1_2_SpecificConfig yet");
            case 35:
                throw new UnsupportedOperationException("can't parse DSTSpecificConfig yet");
            case TsExtractor.TS_STREAM_TYPE_H265 /*36*/:
                this.fillBits = bitReaderBuffer.readBits(5);
                throw new UnsupportedOperationException("can't parse ALSSpecificConfig yet");
            case 37:
            case 38:
                throw new UnsupportedOperationException("can't parse SLSSpecificConfig yet");
            case 39:
                this.eldSpecificConfig = new ELDSpecificConfig(this, this.channelConfiguration, bitReaderBuffer);
                break;
            case 40:
            case 41:
                throw new UnsupportedOperationException("can't parse SymbolicMusicSpecificConfig yet");
            default:
                break;
        }
        i = this.audioObjectType;
        switch (i) {
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
            case RendererCapabilities.ADAPTIVE_SUPPORT_MASK /*24*/:
            case 25:
            case 26:
            case 27:
                break;
            default:
                break;
        }
        this.epConfig = bitReaderBuffer.readBits(2);
        if (this.epConfig != 2) {
            if (this.epConfig == 3) {
                if (this.epConfig == 3) {
                    this.directMapping = bitReaderBuffer.readBits(1);
                    if (this.directMapping == 0) {
                        throw new RuntimeException("not implemented");
                    }
                }
                if (this.extensionAudioObjectType != 5) {
                    return;
                }
                return;
            }
        }
        throw new UnsupportedOperationException("can't parse ErrorProtectionSpecificConfig yet");
    }

    private int gaSpecificConfigSize() {
        return 0;
    }

    public int serializedSize() {
        if (this.audioObjectType == 2) {
            return 4 + gaSpecificConfigSize();
        }
        throw new UnsupportedOperationException("can't serialize that yet");
    }

    public ByteBuffer serialize() {
        ByteBuffer out = ByteBuffer.allocate(serializedSize());
        IsoTypeWriter.writeUInt8(out, 5);
        IsoTypeWriter.writeUInt8(out, serializedSize() - 2);
        BitWriterBuffer bwb = new BitWriterBuffer(out);
        bwb.writeBits(this.audioObjectType, 5);
        bwb.writeBits(this.samplingFrequencyIndex, 4);
        if (this.samplingFrequencyIndex == 15) {
            throw new UnsupportedOperationException("can't serialize that yet");
        }
        bwb.writeBits(this.channelConfiguration, 4);
        return out;
    }

    private int getAudioObjectType(BitReaderBuffer in) throws IOException {
        int audioObjectType = in.readBits(5);
        if (audioObjectType == 31) {
            return 32 + in.readBits(6);
        }
        return audioObjectType;
    }

    private void parseGaSpecificConfig(int samplingFrequencyIndex, int channelConfiguration, int audioObjectType, BitReaderBuffer in) throws IOException {
        this.frameLengthFlag = in.readBits(1);
        this.dependsOnCoreCoder = in.readBits(1);
        if (this.dependsOnCoreCoder == 1) {
            this.coreCoderDelay = in.readBits(14);
        }
        this.extensionFlag = in.readBits(1);
        if (channelConfiguration == 0) {
            throw new UnsupportedOperationException("can't parse program_config_element yet");
        }
        if (audioObjectType == 6 || audioObjectType == 20) {
            this.layerNr = in.readBits(3);
        }
        if (this.extensionFlag == 1) {
            if (audioObjectType == 22) {
                this.numOfSubFrame = in.readBits(5);
                this.layer_length = in.readBits(11);
            }
            if (audioObjectType == 17 || audioObjectType == 19 || audioObjectType == 20 || audioObjectType == 23) {
                this.aacSectionDataResilienceFlag = in.readBool();
                this.aacScalefactorDataResilienceFlag = in.readBool();
                this.aacSpectralDataResilienceFlag = in.readBool();
            }
            this.extensionFlag3 = in.readBits(1);
        }
        this.gaSpecificConfig = true;
    }

    private void parseParametricSpecificConfig(int samplingFrequencyIndex, int channelConfiguration, int audioObjectType, BitReaderBuffer in) throws IOException {
        this.isBaseLayer = in.readBits(1);
        if (this.isBaseLayer == 1) {
            parseParaConfig(samplingFrequencyIndex, channelConfiguration, audioObjectType, in);
        } else {
            parseHilnEnexConfig(samplingFrequencyIndex, channelConfiguration, audioObjectType, in);
        }
    }

    private void parseParaConfig(int samplingFrequencyIndex, int channelConfiguration, int audioObjectType, BitReaderBuffer in) throws IOException {
        this.paraMode = in.readBits(2);
        if (this.paraMode != 1) {
            parseErHvxcConfig(samplingFrequencyIndex, channelConfiguration, audioObjectType, in);
        }
        if (this.paraMode != 0) {
            parseHilnConfig(samplingFrequencyIndex, channelConfiguration, audioObjectType, in);
        }
        this.paraExtensionFlag = in.readBits(1);
        this.parametricSpecificConfig = true;
    }

    private void parseErHvxcConfig(int samplingFrequencyIndex, int channelConfiguration, int audioObjectType, BitReaderBuffer in) throws IOException {
        this.hvxcVarMode = in.readBits(1);
        this.hvxcRateMode = in.readBits(2);
        this.erHvxcExtensionFlag = in.readBits(1);
        if (this.erHvxcExtensionFlag == 1) {
            this.var_ScalableFlag = in.readBits(1);
        }
    }

    private void parseHilnConfig(int samplingFrequencyIndex, int channelConfiguration, int audioObjectType, BitReaderBuffer in) throws IOException {
        this.hilnQuantMode = in.readBits(1);
        this.hilnMaxNumLine = in.readBits(8);
        this.hilnSampleRateCode = in.readBits(4);
        this.hilnFrameLength = in.readBits(12);
        this.hilnContMode = in.readBits(2);
    }

    private void parseHilnEnexConfig(int samplingFrequencyIndex, int channelConfiguration, int audioObjectType, BitReaderBuffer in) throws IOException {
        this.hilnEnhaLayer = in.readBits(1);
        if (this.hilnEnhaLayer == 1) {
            this.hilnEnhaQuantMode = in.readBits(2);
        }
    }

    public void setAudioObjectType(int audioObjectType) {
        this.audioObjectType = audioObjectType;
    }

    public void setSamplingFrequencyIndex(int samplingFrequencyIndex) {
        this.samplingFrequencyIndex = samplingFrequencyIndex;
    }

    public void setChannelConfiguration(int channelConfiguration) {
        this.channelConfiguration = channelConfiguration;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("AudioSpecificConfig");
        sb.append("{configBytes=");
        sb.append(Hex.encodeHex(this.configBytes));
        sb.append(", audioObjectType=");
        sb.append(this.audioObjectType);
        sb.append(" (");
        sb.append((String) audioObjectTypeMap.get(Integer.valueOf(this.audioObjectType)));
        sb.append(")");
        sb.append(", samplingFrequencyIndex=");
        sb.append(this.samplingFrequencyIndex);
        sb.append(" (");
        sb.append(samplingFrequencyIndexMap.get(Integer.valueOf(this.samplingFrequencyIndex)));
        sb.append(")");
        sb.append(", samplingFrequency=");
        sb.append(this.samplingFrequency);
        sb.append(", channelConfiguration=");
        sb.append(this.channelConfiguration);
        if (this.extensionAudioObjectType > 0) {
            sb.append(", extensionAudioObjectType=");
            sb.append(this.extensionAudioObjectType);
            sb.append(" (");
            sb.append((String) audioObjectTypeMap.get(Integer.valueOf(this.extensionAudioObjectType)));
            sb.append(")");
            sb.append(", sbrPresentFlag=");
            sb.append(this.sbrPresentFlag);
            sb.append(", psPresentFlag=");
            sb.append(this.psPresentFlag);
            sb.append(", extensionSamplingFrequencyIndex=");
            sb.append(this.extensionSamplingFrequencyIndex);
            sb.append(" (");
            sb.append(samplingFrequencyIndexMap.get(Integer.valueOf(this.extensionSamplingFrequencyIndex)));
            sb.append(")");
            sb.append(", extensionSamplingFrequency=");
            sb.append(this.extensionSamplingFrequency);
            sb.append(", extensionChannelConfiguration=");
            sb.append(this.extensionChannelConfiguration);
        }
        sb.append(", syncExtensionType=");
        sb.append(this.syncExtensionType);
        if (this.gaSpecificConfig) {
            sb.append(", frameLengthFlag=");
            sb.append(this.frameLengthFlag);
            sb.append(", dependsOnCoreCoder=");
            sb.append(this.dependsOnCoreCoder);
            sb.append(", coreCoderDelay=");
            sb.append(this.coreCoderDelay);
            sb.append(", extensionFlag=");
            sb.append(this.extensionFlag);
            sb.append(", layerNr=");
            sb.append(this.layerNr);
            sb.append(", numOfSubFrame=");
            sb.append(this.numOfSubFrame);
            sb.append(", layer_length=");
            sb.append(this.layer_length);
            sb.append(", aacSectionDataResilienceFlag=");
            sb.append(this.aacSectionDataResilienceFlag);
            sb.append(", aacScalefactorDataResilienceFlag=");
            sb.append(this.aacScalefactorDataResilienceFlag);
            sb.append(", aacSpectralDataResilienceFlag=");
            sb.append(this.aacSpectralDataResilienceFlag);
            sb.append(", extensionFlag3=");
            sb.append(this.extensionFlag3);
        }
        if (this.parametricSpecificConfig) {
            sb.append(", isBaseLayer=");
            sb.append(this.isBaseLayer);
            sb.append(", paraMode=");
            sb.append(this.paraMode);
            sb.append(", paraExtensionFlag=");
            sb.append(this.paraExtensionFlag);
            sb.append(", hvxcVarMode=");
            sb.append(this.hvxcVarMode);
            sb.append(", hvxcRateMode=");
            sb.append(this.hvxcRateMode);
            sb.append(", erHvxcExtensionFlag=");
            sb.append(this.erHvxcExtensionFlag);
            sb.append(", var_ScalableFlag=");
            sb.append(this.var_ScalableFlag);
            sb.append(", hilnQuantMode=");
            sb.append(this.hilnQuantMode);
            sb.append(", hilnMaxNumLine=");
            sb.append(this.hilnMaxNumLine);
            sb.append(", hilnSampleRateCode=");
            sb.append(this.hilnSampleRateCode);
            sb.append(", hilnFrameLength=");
            sb.append(this.hilnFrameLength);
            sb.append(", hilnContMode=");
            sb.append(this.hilnContMode);
            sb.append(", hilnEnhaLayer=");
            sb.append(this.hilnEnhaLayer);
            sb.append(", hilnEnhaQuantMode=");
            sb.append(this.hilnEnhaQuantMode);
        }
        sb.append('}');
        return sb.toString();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o != null) {
            if (getClass() == o.getClass()) {
                AudioSpecificConfig that = (AudioSpecificConfig) o;
                if (this.aacScalefactorDataResilienceFlag == that.aacScalefactorDataResilienceFlag && this.aacSectionDataResilienceFlag == that.aacSectionDataResilienceFlag && this.aacSpectralDataResilienceFlag == that.aacSpectralDataResilienceFlag && this.audioObjectType == that.audioObjectType && this.channelConfiguration == that.channelConfiguration && this.coreCoderDelay == that.coreCoderDelay && this.dependsOnCoreCoder == that.dependsOnCoreCoder && this.directMapping == that.directMapping && this.epConfig == that.epConfig && this.erHvxcExtensionFlag == that.erHvxcExtensionFlag && this.extensionAudioObjectType == that.extensionAudioObjectType && this.extensionChannelConfiguration == that.extensionChannelConfiguration && this.extensionFlag == that.extensionFlag && this.extensionFlag3 == that.extensionFlag3 && this.extensionSamplingFrequency == that.extensionSamplingFrequency && this.extensionSamplingFrequencyIndex == that.extensionSamplingFrequencyIndex && this.fillBits == that.fillBits && this.frameLengthFlag == that.frameLengthFlag && this.gaSpecificConfig == that.gaSpecificConfig && this.hilnContMode == that.hilnContMode && this.hilnEnhaLayer == that.hilnEnhaLayer && this.hilnEnhaQuantMode == that.hilnEnhaQuantMode && this.hilnFrameLength == that.hilnFrameLength && this.hilnMaxNumLine == that.hilnMaxNumLine && this.hilnQuantMode == that.hilnQuantMode && this.hilnSampleRateCode == that.hilnSampleRateCode && this.hvxcRateMode == that.hvxcRateMode && this.hvxcVarMode == that.hvxcVarMode && this.isBaseLayer == that.isBaseLayer && this.layerNr == that.layerNr && this.layer_length == that.layer_length && this.numOfSubFrame == that.numOfSubFrame && this.paraExtensionFlag == that.paraExtensionFlag && this.paraMode == that.paraMode && this.parametricSpecificConfig == that.parametricSpecificConfig && this.psPresentFlag == that.psPresentFlag && this.sacPayloadEmbedding == that.sacPayloadEmbedding && this.samplingFrequency == that.samplingFrequency && this.samplingFrequencyIndex == that.samplingFrequencyIndex && this.sbrPresentFlag == that.sbrPresentFlag && this.syncExtensionType == that.syncExtensionType && this.var_ScalableFlag == that.var_ScalableFlag && Arrays.equals(this.configBytes, that.configBytes)) {
                    return true;
                }
                return false;
            }
        }
        return false;
    }

    public int hashCode() {
        return (31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * (this.configBytes != null ? Arrays.hashCode(this.configBytes) : 0)) + this.audioObjectType)) + this.samplingFrequencyIndex)) + this.samplingFrequency)) + this.channelConfiguration)) + this.extensionAudioObjectType)) + this.sbrPresentFlag)) + this.psPresentFlag)) + this.extensionSamplingFrequencyIndex)) + this.extensionSamplingFrequency)) + this.extensionChannelConfiguration)) + this.sacPayloadEmbedding)) + this.fillBits)) + this.epConfig)) + this.directMapping)) + this.syncExtensionType)) + this.frameLengthFlag)) + this.dependsOnCoreCoder)) + this.coreCoderDelay)) + this.extensionFlag)) + this.layerNr)) + this.numOfSubFrame)) + this.layer_length)) + this.aacSectionDataResilienceFlag)) + this.aacScalefactorDataResilienceFlag)) + this.aacSpectralDataResilienceFlag)) + this.extensionFlag3)) + this.gaSpecificConfig)) + this.isBaseLayer)) + this.paraMode)) + this.paraExtensionFlag)) + this.hvxcVarMode)) + this.hvxcRateMode)) + this.erHvxcExtensionFlag)) + this.var_ScalableFlag)) + this.hilnQuantMode)) + this.hilnMaxNumLine)) + this.hilnSampleRateCode)) + this.hilnFrameLength)) + this.hilnContMode)) + this.hilnEnhaLayer)) + this.hilnEnhaQuantMode)) + this.parametricSpecificConfig;
    }
}
