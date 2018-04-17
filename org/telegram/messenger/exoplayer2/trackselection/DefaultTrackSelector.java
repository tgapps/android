package org.telegram.messenger.exoplayer2.trackselection;

import android.content.Context;
import android.graphics.Point;
import android.text.TextUtils;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import org.telegram.messenger.exoplayer2.C;
import org.telegram.messenger.exoplayer2.ExoPlaybackException;
import org.telegram.messenger.exoplayer2.Format;
import org.telegram.messenger.exoplayer2.RendererCapabilities;
import org.telegram.messenger.exoplayer2.source.TrackGroup;
import org.telegram.messenger.exoplayer2.source.TrackGroupArray;
import org.telegram.messenger.exoplayer2.trackselection.TrackSelection.Factory;
import org.telegram.messenger.exoplayer2.upstream.BandwidthMeter;
import org.telegram.messenger.exoplayer2.util.Assertions;
import org.telegram.messenger.exoplayer2.util.Util;
import org.telegram.tgnet.ConnectionsManager;

public class DefaultTrackSelector extends MappingTrackSelector {
    private static final float FRACTION_TO_CONSIDER_FULLSCREEN = 0.98f;
    private static final int[] NO_TRACKS = new int[0];
    private static final int WITHIN_RENDERER_CAPABILITIES_BONUS = 1000;
    private final Factory adaptiveTrackSelectionFactory;
    private final AtomicReference<Parameters> paramsReference;

    private static final class AudioConfigurationTuple {
        public final int channelCount;
        public final String mimeType;
        public final int sampleRate;

        public AudioConfigurationTuple(int channelCount, int sampleRate, String mimeType) {
            this.channelCount = channelCount;
            this.sampleRate = sampleRate;
            this.mimeType = mimeType;
        }

        public boolean equals(Object obj) {
            boolean z = true;
            if (this == obj) {
                return true;
            }
            if (obj != null) {
                if (getClass() == obj.getClass()) {
                    AudioConfigurationTuple other = (AudioConfigurationTuple) obj;
                    if (this.channelCount != other.channelCount || this.sampleRate != other.sampleRate || !TextUtils.equals(this.mimeType, other.mimeType)) {
                        z = false;
                    }
                    return z;
                }
            }
            return false;
        }

        public int hashCode() {
            return (31 * ((31 * this.channelCount) + this.sampleRate)) + (this.mimeType != null ? this.mimeType.hashCode() : 0);
        }
    }

    private static final class AudioTrackScore implements Comparable<AudioTrackScore> {
        private final int bitrate;
        private final int channelCount;
        private final int defaultSelectionFlagScore;
        private final int matchLanguageScore;
        private final Parameters parameters;
        private final int sampleRate;
        private final int withinRendererCapabilitiesScore;

        public AudioTrackScore(Format format, Parameters parameters, int formatSupport) {
            this.parameters = parameters;
            int i = 0;
            this.withinRendererCapabilitiesScore = DefaultTrackSelector.isSupported(formatSupport, false);
            this.matchLanguageScore = DefaultTrackSelector.formatHasLanguage(format, parameters.preferredAudioLanguage);
            if ((format.selectionFlags & 1) != 0) {
                i = 1;
            }
            this.defaultSelectionFlagScore = i;
            this.channelCount = format.channelCount;
            this.sampleRate = format.sampleRate;
            this.bitrate = format.bitrate;
        }

        public int compareTo(AudioTrackScore other) {
            if (this.withinRendererCapabilitiesScore != other.withinRendererCapabilitiesScore) {
                return DefaultTrackSelector.compareInts(this.withinRendererCapabilitiesScore, other.withinRendererCapabilitiesScore);
            }
            if (this.matchLanguageScore != other.matchLanguageScore) {
                return DefaultTrackSelector.compareInts(this.matchLanguageScore, other.matchLanguageScore);
            }
            if (this.defaultSelectionFlagScore != other.defaultSelectionFlagScore) {
                return DefaultTrackSelector.compareInts(this.defaultSelectionFlagScore, other.defaultSelectionFlagScore);
            }
            if (this.parameters.forceLowestBitrate) {
                return DefaultTrackSelector.compareInts(other.bitrate, this.bitrate);
            }
            int i = 1;
            if (this.withinRendererCapabilitiesScore != 1) {
                i = -1;
            }
            int resultSign = i;
            if (this.channelCount != other.channelCount) {
                return DefaultTrackSelector.compareInts(this.channelCount, other.channelCount) * resultSign;
            }
            if (this.sampleRate != other.sampleRate) {
                return DefaultTrackSelector.compareInts(this.sampleRate, other.sampleRate) * resultSign;
            }
            return DefaultTrackSelector.compareInts(this.bitrate, other.bitrate) * resultSign;
        }

        public boolean equals(Object o) {
            boolean z = true;
            if (this == o) {
                return true;
            }
            if (o != null) {
                if (getClass() == o.getClass()) {
                    AudioTrackScore that = (AudioTrackScore) o;
                    if (this.withinRendererCapabilitiesScore != that.withinRendererCapabilitiesScore || this.matchLanguageScore != that.matchLanguageScore || this.defaultSelectionFlagScore != that.defaultSelectionFlagScore || this.channelCount != that.channelCount || this.sampleRate != that.sampleRate || this.bitrate != that.bitrate) {
                        z = false;
                    }
                    return z;
                }
            }
            return false;
        }

        public int hashCode() {
            return (31 * ((31 * ((31 * ((31 * ((31 * this.withinRendererCapabilitiesScore) + this.matchLanguageScore)) + this.defaultSelectionFlagScore)) + this.channelCount)) + this.sampleRate)) + this.bitrate;
        }
    }

    public static final class Parameters {
        public static final Parameters DEFAULT = new Parameters();
        public final boolean allowMixedMimeAdaptiveness;
        public final boolean allowNonSeamlessAdaptiveness;
        public final int disabledTextTrackSelectionFlags;
        public final boolean exceedRendererCapabilitiesIfNecessary;
        public final boolean exceedVideoConstraintsIfNecessary;
        public final boolean forceLowestBitrate;
        public final int maxVideoBitrate;
        public final int maxVideoHeight;
        public final int maxVideoWidth;
        public final String preferredAudioLanguage;
        public final String preferredTextLanguage;
        public final boolean selectUndeterminedTextLanguage;
        public final int viewportHeight;
        public final boolean viewportOrientationMayChange;
        public final int viewportWidth;

        private Parameters() {
            this(null, null, false, 0, false, false, true, ConnectionsManager.DEFAULT_DATACENTER_ID, ConnectionsManager.DEFAULT_DATACENTER_ID, ConnectionsManager.DEFAULT_DATACENTER_ID, true, true, ConnectionsManager.DEFAULT_DATACENTER_ID, ConnectionsManager.DEFAULT_DATACENTER_ID, true);
        }

        private Parameters(String preferredAudioLanguage, String preferredTextLanguage, boolean selectUndeterminedTextLanguage, int disabledTextTrackSelectionFlags, boolean forceLowestBitrate, boolean allowMixedMimeAdaptiveness, boolean allowNonSeamlessAdaptiveness, int maxVideoWidth, int maxVideoHeight, int maxVideoBitrate, boolean exceedVideoConstraintsIfNecessary, boolean exceedRendererCapabilitiesIfNecessary, int viewportWidth, int viewportHeight, boolean viewportOrientationMayChange) {
            this.preferredAudioLanguage = Util.normalizeLanguageCode(preferredAudioLanguage);
            this.preferredTextLanguage = Util.normalizeLanguageCode(preferredTextLanguage);
            this.selectUndeterminedTextLanguage = selectUndeterminedTextLanguage;
            this.disabledTextTrackSelectionFlags = disabledTextTrackSelectionFlags;
            this.forceLowestBitrate = forceLowestBitrate;
            this.allowMixedMimeAdaptiveness = allowMixedMimeAdaptiveness;
            this.allowNonSeamlessAdaptiveness = allowNonSeamlessAdaptiveness;
            this.maxVideoWidth = maxVideoWidth;
            this.maxVideoHeight = maxVideoHeight;
            this.maxVideoBitrate = maxVideoBitrate;
            this.exceedVideoConstraintsIfNecessary = exceedVideoConstraintsIfNecessary;
            this.exceedRendererCapabilitiesIfNecessary = exceedRendererCapabilitiesIfNecessary;
            this.viewportWidth = viewportWidth;
            this.viewportHeight = viewportHeight;
            this.viewportOrientationMayChange = viewportOrientationMayChange;
        }

        public ParametersBuilder buildUpon() {
            return new ParametersBuilder();
        }

        public boolean equals(Object obj) {
            boolean z = true;
            if (this == obj) {
                return true;
            }
            if (obj != null) {
                if (getClass() == obj.getClass()) {
                    Parameters other = (Parameters) obj;
                    if (this.selectUndeterminedTextLanguage != other.selectUndeterminedTextLanguage || this.disabledTextTrackSelectionFlags != other.disabledTextTrackSelectionFlags || this.forceLowestBitrate != other.forceLowestBitrate || this.allowMixedMimeAdaptiveness != other.allowMixedMimeAdaptiveness || this.allowNonSeamlessAdaptiveness != other.allowNonSeamlessAdaptiveness || this.maxVideoWidth != other.maxVideoWidth || this.maxVideoHeight != other.maxVideoHeight || this.exceedVideoConstraintsIfNecessary != other.exceedVideoConstraintsIfNecessary || this.exceedRendererCapabilitiesIfNecessary != other.exceedRendererCapabilitiesIfNecessary || this.viewportOrientationMayChange != other.viewportOrientationMayChange || this.viewportWidth != other.viewportWidth || this.viewportHeight != other.viewportHeight || this.maxVideoBitrate != other.maxVideoBitrate || !TextUtils.equals(this.preferredAudioLanguage, other.preferredAudioLanguage) || !TextUtils.equals(this.preferredTextLanguage, other.preferredTextLanguage)) {
                        z = false;
                    }
                    return z;
                }
            }
            return false;
        }

        public int hashCode() {
            return (31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * this.selectUndeterminedTextLanguage) + this.disabledTextTrackSelectionFlags)) + this.forceLowestBitrate)) + this.allowMixedMimeAdaptiveness)) + this.allowNonSeamlessAdaptiveness)) + this.maxVideoWidth)) + this.maxVideoHeight)) + this.exceedVideoConstraintsIfNecessary)) + this.exceedRendererCapabilitiesIfNecessary)) + this.viewportOrientationMayChange)) + this.viewportWidth)) + this.viewportHeight)) + this.maxVideoBitrate)) + this.preferredAudioLanguage.hashCode())) + this.preferredTextLanguage.hashCode();
        }
    }

    public static final class ParametersBuilder {
        private boolean allowMixedMimeAdaptiveness;
        private boolean allowNonSeamlessAdaptiveness;
        private int disabledTextTrackSelectionFlags;
        private boolean exceedRendererCapabilitiesIfNecessary;
        private boolean exceedVideoConstraintsIfNecessary;
        private boolean forceLowestBitrate;
        private int maxVideoBitrate;
        private int maxVideoHeight;
        private int maxVideoWidth;
        private String preferredAudioLanguage;
        private String preferredTextLanguage;
        private boolean selectUndeterminedTextLanguage;
        private int viewportHeight;
        private boolean viewportOrientationMayChange;
        private int viewportWidth;

        public ParametersBuilder() {
            this(Parameters.DEFAULT);
        }

        private ParametersBuilder(Parameters initialValues) {
            this.preferredAudioLanguage = initialValues.preferredAudioLanguage;
            this.preferredTextLanguage = initialValues.preferredTextLanguage;
            this.selectUndeterminedTextLanguage = initialValues.selectUndeterminedTextLanguage;
            this.disabledTextTrackSelectionFlags = initialValues.disabledTextTrackSelectionFlags;
            this.forceLowestBitrate = initialValues.forceLowestBitrate;
            this.allowMixedMimeAdaptiveness = initialValues.allowMixedMimeAdaptiveness;
            this.allowNonSeamlessAdaptiveness = initialValues.allowNonSeamlessAdaptiveness;
            this.maxVideoWidth = initialValues.maxVideoWidth;
            this.maxVideoHeight = initialValues.maxVideoHeight;
            this.maxVideoBitrate = initialValues.maxVideoBitrate;
            this.exceedVideoConstraintsIfNecessary = initialValues.exceedVideoConstraintsIfNecessary;
            this.exceedRendererCapabilitiesIfNecessary = initialValues.exceedRendererCapabilitiesIfNecessary;
            this.viewportWidth = initialValues.viewportWidth;
            this.viewportHeight = initialValues.viewportHeight;
            this.viewportOrientationMayChange = initialValues.viewportOrientationMayChange;
        }

        public ParametersBuilder setPreferredAudioLanguage(String preferredAudioLanguage) {
            this.preferredAudioLanguage = preferredAudioLanguage;
            return this;
        }

        public ParametersBuilder setPreferredTextLanguage(String preferredTextLanguage) {
            this.preferredTextLanguage = preferredTextLanguage;
            return this;
        }

        public ParametersBuilder setSelectUndeterminedTextLanguage(boolean selectUndeterminedTextLanguage) {
            this.selectUndeterminedTextLanguage = selectUndeterminedTextLanguage;
            return this;
        }

        public ParametersBuilder setDisabledTextTrackSelectionFlags(int disabledTextTrackSelectionFlags) {
            this.disabledTextTrackSelectionFlags = disabledTextTrackSelectionFlags;
            return this;
        }

        public ParametersBuilder setForceLowestBitrate(boolean forceLowestBitrate) {
            this.forceLowestBitrate = forceLowestBitrate;
            return this;
        }

        public ParametersBuilder setAllowMixedMimeAdaptiveness(boolean allowMixedMimeAdaptiveness) {
            this.allowMixedMimeAdaptiveness = allowMixedMimeAdaptiveness;
            return this;
        }

        public ParametersBuilder setAllowNonSeamlessAdaptiveness(boolean allowNonSeamlessAdaptiveness) {
            this.allowNonSeamlessAdaptiveness = allowNonSeamlessAdaptiveness;
            return this;
        }

        public ParametersBuilder setMaxVideoSizeSd() {
            return setMaxVideoSize(1279, 719);
        }

        public ParametersBuilder clearVideoSizeConstraints() {
            return setMaxVideoSize(ConnectionsManager.DEFAULT_DATACENTER_ID, ConnectionsManager.DEFAULT_DATACENTER_ID);
        }

        public ParametersBuilder setMaxVideoSize(int maxVideoWidth, int maxVideoHeight) {
            this.maxVideoWidth = maxVideoWidth;
            this.maxVideoHeight = maxVideoHeight;
            return this;
        }

        public ParametersBuilder setMaxVideoBitrate(int maxVideoBitrate) {
            this.maxVideoBitrate = maxVideoBitrate;
            return this;
        }

        public ParametersBuilder setExceedVideoConstraintsIfNecessary(boolean exceedVideoConstraintsIfNecessary) {
            this.exceedVideoConstraintsIfNecessary = exceedVideoConstraintsIfNecessary;
            return this;
        }

        public ParametersBuilder setExceedRendererCapabilitiesIfNecessary(boolean exceedRendererCapabilitiesIfNecessary) {
            this.exceedRendererCapabilitiesIfNecessary = exceedRendererCapabilitiesIfNecessary;
            return this;
        }

        public ParametersBuilder setViewportSizeToPhysicalDisplaySize(Context context, boolean viewportOrientationMayChange) {
            Point viewportSize = Util.getPhysicalDisplaySize(context);
            return setViewportSize(viewportSize.x, viewportSize.y, viewportOrientationMayChange);
        }

        public ParametersBuilder clearViewportSizeConstraints() {
            return setViewportSize(ConnectionsManager.DEFAULT_DATACENTER_ID, ConnectionsManager.DEFAULT_DATACENTER_ID, true);
        }

        public ParametersBuilder setViewportSize(int viewportWidth, int viewportHeight, boolean viewportOrientationMayChange) {
            this.viewportWidth = viewportWidth;
            this.viewportHeight = viewportHeight;
            this.viewportOrientationMayChange = viewportOrientationMayChange;
            return this;
        }

        public Parameters build() {
            return new Parameters(this.preferredAudioLanguage, this.preferredTextLanguage, this.selectUndeterminedTextLanguage, this.disabledTextTrackSelectionFlags, this.forceLowestBitrate, this.allowMixedMimeAdaptiveness, this.allowNonSeamlessAdaptiveness, this.maxVideoWidth, this.maxVideoHeight, this.maxVideoBitrate, this.exceedVideoConstraintsIfNecessary, this.exceedRendererCapabilitiesIfNecessary, this.viewportWidth, this.viewportHeight, this.viewportOrientationMayChange);
        }
    }

    private static org.telegram.messenger.exoplayer2.trackselection.TrackSelection selectAdaptiveVideoTrack(org.telegram.messenger.exoplayer2.RendererCapabilities r1, org.telegram.messenger.exoplayer2.source.TrackGroupArray r2, int[][] r3, org.telegram.messenger.exoplayer2.trackselection.DefaultTrackSelector.Parameters r4, org.telegram.messenger.exoplayer2.trackselection.TrackSelection.Factory r5) throws org.telegram.messenger.exoplayer2.ExoPlaybackException {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.exoplayer2.trackselection.DefaultTrackSelector.selectAdaptiveVideoTrack(org.telegram.messenger.exoplayer2.RendererCapabilities, org.telegram.messenger.exoplayer2.source.TrackGroupArray, int[][], org.telegram.messenger.exoplayer2.trackselection.DefaultTrackSelector$Parameters, org.telegram.messenger.exoplayer2.trackselection.TrackSelection$Factory):org.telegram.messenger.exoplayer2.trackselection.TrackSelection
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:116)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:249)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
Caused by: java.lang.NullPointerException
*/
        /*
        r0 = r17;
        r1 = r19;
        r2 = r1.allowNonSeamlessAdaptiveness;
        if (r2 == 0) goto L_0x000b;
    L_0x0008:
        r2 = 24;
        goto L_0x000d;
    L_0x000b:
        r2 = 16;
    L_0x000d:
        r3 = r1.allowMixedMimeAdaptiveness;
        r4 = 0;
        if (r3 == 0) goto L_0x001c;
    L_0x0012:
        r3 = r16.supportsMixedMimeTypeAdaptation();
        r3 = r3 & r2;
        if (r3 == 0) goto L_0x001c;
    L_0x0019:
        r3 = 1;
        r5 = r3;
        goto L_0x001d;
    L_0x001c:
        r5 = r4;
        r13 = r4;
        r3 = r0.length;
        if (r13 >= r3) goto L_0x004b;
        r14 = r0.get(r13);
        r4 = r18[r13];
        r7 = r1.maxVideoWidth;
        r8 = r1.maxVideoHeight;
        r9 = r1.maxVideoBitrate;
        r10 = r1.viewportWidth;
        r11 = r1.viewportHeight;
        r12 = r1.viewportOrientationMayChange;
        r3 = r14;
        r6 = r2;
        r3 = getAdaptiveVideoTracksForGroup(r3, r4, r5, r6, r7, r8, r9, r10, r11, r12);
        r4 = r3.length;
        if (r4 <= 0) goto L_0x0045;
        r4 = r20;
        r6 = r4.createTrackSelection(r14, r3);
        return r6;
        r4 = r20;
        r3 = r13 + 1;
        r4 = r3;
        goto L_0x001e;
        r4 = r20;
        r3 = 0;
        return r3;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.exoplayer2.trackselection.DefaultTrackSelector.selectAdaptiveVideoTrack(org.telegram.messenger.exoplayer2.RendererCapabilities, org.telegram.messenger.exoplayer2.source.TrackGroupArray, int[][], org.telegram.messenger.exoplayer2.trackselection.DefaultTrackSelector$Parameters, org.telegram.messenger.exoplayer2.trackselection.TrackSelection$Factory):org.telegram.messenger.exoplayer2.trackselection.TrackSelection");
    }

    protected org.telegram.messenger.exoplayer2.trackselection.TrackSelection selectTextTrack(org.telegram.messenger.exoplayer2.source.TrackGroupArray r1, int[][] r2, org.telegram.messenger.exoplayer2.trackselection.DefaultTrackSelector.Parameters r3) throws org.telegram.messenger.exoplayer2.ExoPlaybackException {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.exoplayer2.trackselection.DefaultTrackSelector.selectTextTrack(org.telegram.messenger.exoplayer2.source.TrackGroupArray, int[][], org.telegram.messenger.exoplayer2.trackselection.DefaultTrackSelector$Parameters):org.telegram.messenger.exoplayer2.trackselection.TrackSelection
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
        r1 = r19;
        r2 = 0;
        r3 = 0;
        r4 = 0;
        r6 = r4;
        r4 = r3;
        r3 = r2;
        r2 = 0;
        r7 = r0.length;
        if (r2 >= r7) goto L_0x0096;
    L_0x000f:
        r7 = r0.get(r2);
        r8 = r18[r2];
        r9 = r6;
        r6 = r4;
        r4 = r3;
        r3 = 0;
        r10 = r7.length;
        if (r3 >= r10) goto L_0x008a;
    L_0x001d:
        r10 = r8[r3];
        r11 = r1.exceedRendererCapabilitiesIfNecessary;
        r10 = isSupported(r10, r11);
        if (r10 == 0) goto L_0x0082;
    L_0x0027:
        r10 = r7.getFormat(r3);
        r11 = r10.selectionFlags;
        r12 = r1.disabledTextTrackSelectionFlags;
        r12 = r12 ^ -1;
        r11 = r11 & r12;
        r12 = r11 & 1;
        if (r12 == 0) goto L_0x0038;
    L_0x0036:
        r12 = 1;
        goto L_0x0039;
    L_0x0038:
        r12 = 0;
    L_0x0039:
        r14 = r11 & 2;
        if (r14 == 0) goto L_0x003f;
    L_0x003d:
        r14 = 1;
        goto L_0x0040;
    L_0x003f:
        r14 = 0;
    L_0x0040:
        r13 = r1.preferredTextLanguage;
        r13 = formatHasLanguage(r10, r13);
        if (r13 != 0) goto L_0x0065;
    L_0x0048:
        r5 = r1.selectUndeterminedTextLanguage;
        if (r5 == 0) goto L_0x0053;
    L_0x004c:
        r5 = formatHasNoLanguage(r10);
        if (r5 == 0) goto L_0x0053;
    L_0x0052:
        goto L_0x0065;
    L_0x0053:
        if (r12 == 0) goto L_0x0057;
    L_0x0055:
        r5 = 3;
        goto L_0x0070;
    L_0x0057:
        if (r14 == 0) goto L_0x0082;
        r5 = r1.preferredAudioLanguage;
        r5 = formatHasLanguage(r10, r5);
        if (r5 == 0) goto L_0x0063;
        r5 = 2;
        goto L_0x0056;
        r5 = 1;
        goto L_0x0070;
    L_0x0065:
        if (r12 == 0) goto L_0x006a;
        r5 = 8;
        goto L_0x006f;
        if (r14 != 0) goto L_0x006e;
        r5 = 6;
        goto L_0x0069;
        r5 = 4;
        r5 = r5 + r13;
        r0 = r8[r3];
        r1 = 0;
        r0 = isSupported(r0, r1);
        if (r0 == 0) goto L_0x007c;
        r5 = r5 + 1000;
        if (r5 <= r9) goto L_0x0083;
        r4 = r7;
        r6 = r3;
        r9 = r5;
        goto L_0x0083;
    L_0x0082:
        r1 = 0;
        r3 = r3 + 1;
        r0 = r17;
        r1 = r19;
        goto L_0x0019;
    L_0x008a:
        r1 = 0;
        r2 = r2 + 1;
        r3 = r4;
        r4 = r6;
        r6 = r9;
        r0 = r17;
        r1 = r19;
        goto L_0x000b;
    L_0x0096:
        if (r3 != 0) goto L_0x009a;
        r0 = 0;
        goto L_0x009f;
        r0 = new org.telegram.messenger.exoplayer2.trackselection.FixedTrackSelection;
        r0.<init>(r3, r4);
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.exoplayer2.trackselection.DefaultTrackSelector.selectTextTrack(org.telegram.messenger.exoplayer2.source.TrackGroupArray, int[][], org.telegram.messenger.exoplayer2.trackselection.DefaultTrackSelector$Parameters):org.telegram.messenger.exoplayer2.trackselection.TrackSelection");
    }

    public DefaultTrackSelector() {
        this((Factory) null);
    }

    public DefaultTrackSelector(BandwidthMeter bandwidthMeter) {
        this(new AdaptiveTrackSelection.Factory(bandwidthMeter));
    }

    public DefaultTrackSelector(Factory adaptiveTrackSelectionFactory) {
        this.adaptiveTrackSelectionFactory = adaptiveTrackSelectionFactory;
        this.paramsReference = new AtomicReference(Parameters.DEFAULT);
    }

    public void setParameters(Parameters params) {
        Assertions.checkNotNull(params);
        if (!((Parameters) this.paramsReference.getAndSet(params)).equals(params)) {
            invalidate();
        }
    }

    public Parameters getParameters() {
        return (Parameters) this.paramsReference.get();
    }

    protected TrackSelection[] selectTracks(RendererCapabilities[] rendererCapabilities, TrackGroupArray[] rendererTrackGroupArrays, int[][][] rendererFormatSupports) throws ExoPlaybackException {
        RendererCapabilities[] rendererCapabilitiesArr = rendererCapabilities;
        int rendererCount = rendererCapabilitiesArr.length;
        TrackSelection[] rendererTrackSelections = new TrackSelection[rendererCount];
        Parameters params = (Parameters) this.paramsReference.get();
        boolean seenVideoRendererWithMappedTracks = false;
        boolean selectedVideoTracks = false;
        int i = 0;
        while (true) {
            int i2 = i;
            int i3 = 1;
            if (i2 >= rendererCount) {
                break;
            }
            int i4;
            if (2 == rendererCapabilitiesArr[i2].getTrackType()) {
                if (selectedVideoTracks) {
                    i4 = i2;
                } else {
                    i4 = i2;
                    rendererTrackSelections[i4] = selectVideoTrack(rendererCapabilitiesArr[i2], rendererTrackGroupArrays[i2], rendererFormatSupports[i2], params, r6.adaptiveTrackSelectionFactory);
                    selectedVideoTracks = rendererTrackSelections[i4] != null;
                }
                if (rendererTrackGroupArrays[i4].length <= 0) {
                    i3 = 0;
                }
                seenVideoRendererWithMappedTracks |= i3;
            } else {
                i4 = i2;
            }
            i = i4 + 1;
        }
        boolean selectedTextTracks = false;
        boolean selectedAudioTracks = false;
        for (i = 0; i < rendererCount; i++) {
            switch (rendererCapabilitiesArr[i].getTrackType()) {
                case 1:
                    if (!selectedAudioTracks) {
                        rendererTrackSelections[i] = selectAudioTrack(rendererTrackGroupArrays[i], rendererFormatSupports[i], params, seenVideoRendererWithMappedTracks ? null : r6.adaptiveTrackSelectionFactory);
                        selectedAudioTracks = rendererTrackSelections[i] != null;
                        break;
                    }
                    break;
                case 2:
                    break;
                case 3:
                    if (!selectedTextTracks) {
                        rendererTrackSelections[i] = selectTextTrack(rendererTrackGroupArrays[i], rendererFormatSupports[i], params);
                        selectedTextTracks = rendererTrackSelections[i] != null;
                        break;
                    }
                    break;
                default:
                    rendererTrackSelections[i] = selectOtherTrack(rendererCapabilitiesArr[i].getTrackType(), rendererTrackGroupArrays[i], rendererFormatSupports[i], params);
                    break;
            }
        }
        return rendererTrackSelections;
    }

    protected TrackSelection selectVideoTrack(RendererCapabilities rendererCapabilities, TrackGroupArray groups, int[][] formatSupport, Parameters params, Factory adaptiveTrackSelectionFactory) throws ExoPlaybackException {
        TrackSelection selection = null;
        if (!(params.forceLowestBitrate || adaptiveTrackSelectionFactory == null)) {
            selection = selectAdaptiveVideoTrack(rendererCapabilities, groups, formatSupport, params, adaptiveTrackSelectionFactory);
        }
        if (selection == null) {
            return selectFixedVideoTrack(groups, formatSupport, params);
        }
        return selection;
    }

    private static int[] getAdaptiveVideoTracksForGroup(TrackGroup group, int[] formatSupport, boolean allowMixedMimeTypes, int requiredAdaptiveSupport, int maxVideoWidth, int maxVideoHeight, int maxVideoBitrate, int viewportWidth, int viewportHeight, boolean viewportOrientationMayChange) {
        TrackGroup trackGroup = group;
        if (trackGroup.length < 2) {
            return NO_TRACKS;
        }
        List<Integer> selectedTrackIndices = getViewportFilteredTrackIndices(trackGroup, viewportWidth, viewportHeight, viewportOrientationMayChange);
        if (selectedTrackIndices.size() < 2) {
            return NO_TRACKS;
        }
        String selectedMimeType;
        if (!allowMixedMimeTypes) {
            HashSet<String> seenMimeTypes = new HashSet();
            int i = 0;
            selectedMimeType = null;
            int selectedMimeTypeTrackCount = 0;
            while (true) {
                int i2 = i;
                if (i2 >= selectedTrackIndices.size()) {
                    break;
                }
                int i3;
                int selectedMimeTypeTrackCount2;
                int trackIndex = ((Integer) selectedTrackIndices.get(i2)).intValue();
                String sampleMimeType = trackGroup.getFormat(trackIndex).sampleMimeType;
                if (seenMimeTypes.add(sampleMimeType) != null) {
                    String sampleMimeType2 = sampleMimeType;
                    i3 = i2;
                    selectedMimeTypeTrackCount2 = selectedMimeTypeTrackCount;
                    String selectedMimeType2 = getAdaptiveVideoTrackCountForMimeType(trackGroup, formatSupport, requiredAdaptiveSupport, sampleMimeType, maxVideoWidth, maxVideoHeight, maxVideoBitrate, selectedTrackIndices);
                    if (selectedMimeType2 > selectedMimeTypeTrackCount2) {
                        selectedMimeTypeTrackCount = selectedMimeType2;
                        selectedMimeType = sampleMimeType2;
                        i = i3 + 1;
                    }
                } else {
                    i3 = i2;
                    selectedMimeTypeTrackCount2 = selectedMimeTypeTrackCount;
                }
                selectedMimeTypeTrackCount = selectedMimeTypeTrackCount2;
                i = i3 + 1;
            }
        } else {
            selectedMimeType = null;
        }
        filterAdaptiveVideoTrackCountForMimeType(trackGroup, formatSupport, requiredAdaptiveSupport, selectedMimeType, maxVideoWidth, maxVideoHeight, maxVideoBitrate, selectedTrackIndices);
        return selectedTrackIndices.size() < 2 ? NO_TRACKS : Util.toArray(selectedTrackIndices);
    }

    private static int getAdaptiveVideoTrackCountForMimeType(TrackGroup group, int[] formatSupport, int requiredAdaptiveSupport, String mimeType, int maxVideoWidth, int maxVideoHeight, int maxVideoBitrate, List<Integer> selectedTrackIndices) {
        int adaptiveTrackCount = 0;
        for (int i = 0; i < selectedTrackIndices.size(); i++) {
            int trackIndex = ((Integer) selectedTrackIndices.get(i)).intValue();
            if (isSupportedAdaptiveVideoTrack(group.getFormat(trackIndex), mimeType, formatSupport[trackIndex], requiredAdaptiveSupport, maxVideoWidth, maxVideoHeight, maxVideoBitrate)) {
                adaptiveTrackCount++;
            }
        }
        TrackGroup trackGroup = group;
        List<Integer> list = selectedTrackIndices;
        return adaptiveTrackCount;
    }

    private static void filterAdaptiveVideoTrackCountForMimeType(TrackGroup group, int[] formatSupport, int requiredAdaptiveSupport, String mimeType, int maxVideoWidth, int maxVideoHeight, int maxVideoBitrate, List<Integer> selectedTrackIndices) {
        List<Integer> list = selectedTrackIndices;
        for (int i = selectedTrackIndices.size() - 1; i >= 0; i--) {
            int trackIndex = ((Integer) list.get(i)).intValue();
            if (!isSupportedAdaptiveVideoTrack(group.getFormat(trackIndex), mimeType, formatSupport[trackIndex], requiredAdaptiveSupport, maxVideoWidth, maxVideoHeight, maxVideoBitrate)) {
                list.remove(i);
            }
        }
        TrackGroup trackGroup = group;
    }

    private static boolean isSupportedAdaptiveVideoTrack(Format format, String mimeType, int formatSupport, int requiredAdaptiveSupport, int maxVideoWidth, int maxVideoHeight, int maxVideoBitrate) {
        if (!isSupported(formatSupport, false) || (formatSupport & requiredAdaptiveSupport) == 0) {
            return false;
        }
        if (mimeType != null && !Util.areEqual(format.sampleMimeType, mimeType)) {
            return false;
        }
        if (format.width != -1 && format.width > maxVideoWidth) {
            return false;
        }
        if (format.height != -1 && format.height > maxVideoHeight) {
            return false;
        }
        if (format.bitrate == -1 || format.bitrate <= maxVideoBitrate) {
            return true;
        }
        return false;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static org.telegram.messenger.exoplayer2.trackselection.TrackSelection selectFixedVideoTrack(org.telegram.messenger.exoplayer2.source.TrackGroupArray r21, int[][] r22, org.telegram.messenger.exoplayer2.trackselection.DefaultTrackSelector.Parameters r23) {
        /*
        r0 = r21;
        r1 = r23;
        r2 = 0;
        r3 = 0;
        r4 = 0;
        r5 = -1;
        r6 = -1;
        r8 = r6;
        r6 = r5;
        r5 = r4;
        r4 = r3;
        r3 = r2;
        r2 = 0;
    L_0x000f:
        r9 = r0.length;
        if (r2 >= r9) goto L_0x00ee;
    L_0x0013:
        r9 = r0.get(r2);
        r10 = r1.viewportWidth;
        r11 = r1.viewportHeight;
        r12 = r1.viewportOrientationMayChange;
        r10 = getViewportFilteredTrackIndices(r9, r10, r11, r12);
        r12 = r22[r2];
        r13 = r8;
        r8 = r6;
        r6 = r5;
        r5 = r4;
        r4 = r3;
        r3 = 0;
    L_0x0029:
        r14 = r9.length;
        if (r3 >= r14) goto L_0x00de;
    L_0x002d:
        r14 = r12[r3];
        r15 = r1.exceedRendererCapabilitiesIfNecessary;
        r14 = isSupported(r14, r15);
        if (r14 == 0) goto L_0x00d2;
    L_0x0037:
        r14 = r9.getFormat(r3);
        r15 = java.lang.Integer.valueOf(r3);
        r15 = r10.contains(r15);
        r16 = 1;
        if (r15 == 0) goto L_0x006b;
    L_0x0047:
        r15 = r14.width;
        r7 = -1;
        if (r15 == r7) goto L_0x0052;
    L_0x004c:
        r15 = r14.width;
        r7 = r1.maxVideoWidth;
        if (r15 > r7) goto L_0x006b;
    L_0x0052:
        r7 = r14.height;
        r15 = -1;
        if (r7 == r15) goto L_0x005d;
    L_0x0057:
        r7 = r14.height;
        r15 = r1.maxVideoHeight;
        if (r7 > r15) goto L_0x006b;
    L_0x005d:
        r7 = r14.bitrate;
        r15 = -1;
        if (r7 == r15) goto L_0x0068;
    L_0x0062:
        r7 = r14.bitrate;
        r15 = r1.maxVideoBitrate;
        if (r7 > r15) goto L_0x006b;
    L_0x0068:
        r7 = r16;
        goto L_0x006c;
    L_0x006b:
        r7 = 0;
    L_0x006c:
        if (r7 != 0) goto L_0x0077;
    L_0x006e:
        r15 = r1.exceedVideoConstraintsIfNecessary;
        if (r15 != 0) goto L_0x0077;
    L_0x0073:
        r19 = r5;
        goto L_0x00d4;
    L_0x0077:
        if (r7 == 0) goto L_0x007b;
    L_0x0079:
        r15 = 2;
        goto L_0x007d;
    L_0x007b:
        r15 = r16;
    L_0x007d:
        r0 = r12[r3];
        r19 = r5;
        r5 = 0;
        r0 = isSupported(r0, r5);
        if (r0 == 0) goto L_0x008a;
    L_0x0088:
        r15 = r15 + 1000;
    L_0x008a:
        if (r15 <= r6) goto L_0x008f;
    L_0x008c:
        r17 = r16;
        goto L_0x0091;
    L_0x008f:
        r17 = r5;
    L_0x0091:
        if (r15 != r6) goto L_0x00c6;
    L_0x0093:
        r5 = r1.forceLowestBitrate;
        if (r5 == 0) goto L_0x00a5;
    L_0x0097:
        r5 = r14.bitrate;
        r5 = compareFormatValues(r5, r8);
        if (r5 >= 0) goto L_0x00a0;
    L_0x009f:
        goto L_0x00a2;
    L_0x00a0:
        r16 = 0;
    L_0x00a2:
        r17 = r16;
        goto L_0x00c6;
    L_0x00a5:
        r5 = r14.getPixelCount();
        if (r5 == r13) goto L_0x00b0;
    L_0x00ab:
        r18 = compareFormatValues(r5, r13);
        goto L_0x00b6;
    L_0x00b0:
        r1 = r14.bitrate;
        r18 = compareFormatValues(r1, r8);
    L_0x00b6:
        r1 = r18;
        if (r0 == 0) goto L_0x00bf;
    L_0x00ba:
        if (r7 == 0) goto L_0x00bf;
    L_0x00bc:
        if (r1 <= 0) goto L_0x00c2;
    L_0x00be:
        goto L_0x00c1;
    L_0x00bf:
        if (r1 >= 0) goto L_0x00c2;
    L_0x00c1:
        goto L_0x00c4;
    L_0x00c2:
        r16 = 0;
    L_0x00c4:
        r17 = r16;
    L_0x00c6:
        if (r17 == 0) goto L_0x00d4;
    L_0x00c8:
        r4 = r9;
        r5 = r3;
        r6 = r15;
        r8 = r14.bitrate;
        r13 = r14.getPixelCount();
        goto L_0x00d6;
    L_0x00d2:
        r19 = r5;
    L_0x00d4:
        r5 = r19;
    L_0x00d6:
        r3 = r3 + 1;
        r0 = r21;
        r1 = r23;
        goto L_0x0029;
    L_0x00de:
        r19 = r5;
        r2 = r2 + 1;
        r3 = r4;
        r5 = r6;
        r6 = r8;
        r8 = r13;
        r4 = r19;
        r0 = r21;
        r1 = r23;
        goto L_0x000f;
    L_0x00ee:
        r0 = r5;
        if (r3 != 0) goto L_0x00f3;
    L_0x00f1:
        r1 = 0;
        goto L_0x00f8;
    L_0x00f3:
        r1 = new org.telegram.messenger.exoplayer2.trackselection.FixedTrackSelection;
        r1.<init>(r3, r4);
    L_0x00f8:
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.exoplayer2.trackselection.DefaultTrackSelector.selectFixedVideoTrack(org.telegram.messenger.exoplayer2.source.TrackGroupArray, int[][], org.telegram.messenger.exoplayer2.trackselection.DefaultTrackSelector$Parameters):org.telegram.messenger.exoplayer2.trackselection.TrackSelection");
    }

    private static int compareFormatValues(int first, int second) {
        return first == -1 ? second == -1 ? 0 : -1 : second == -1 ? 1 : first - second;
    }

    protected TrackSelection selectAudioTrack(TrackGroupArray groups, int[][] formatSupport, Parameters params, Factory adaptiveTrackSelectionFactory) throws ExoPlaybackException {
        int selectedGroupIndex = -1;
        AudioTrackScore selectedTrackScore = null;
        int selectedTrackIndex = -1;
        int groupIndex = 0;
        while (groupIndex < groups.length) {
            TrackGroup trackGroup = groups.get(groupIndex);
            int[] trackFormatSupport = formatSupport[groupIndex];
            AudioTrackScore selectedTrackScore2 = selectedTrackScore;
            int selectedGroupIndex2 = selectedGroupIndex;
            for (selectedGroupIndex = 0; selectedGroupIndex < trackGroup.length; selectedGroupIndex++) {
                if (isSupported(trackFormatSupport[selectedGroupIndex], params.exceedRendererCapabilitiesIfNecessary)) {
                    AudioTrackScore trackScore = new AudioTrackScore(trackGroup.getFormat(selectedGroupIndex), params, trackFormatSupport[selectedGroupIndex]);
                    if (selectedTrackScore2 == null || trackScore.compareTo(selectedTrackScore2) > 0) {
                        selectedGroupIndex2 = groupIndex;
                        selectedTrackIndex = selectedGroupIndex;
                        selectedTrackScore2 = trackScore;
                    }
                }
            }
            groupIndex++;
            selectedGroupIndex = selectedGroupIndex2;
            selectedTrackScore = selectedTrackScore2;
        }
        if (selectedGroupIndex == -1) {
            return null;
        }
        TrackGroup selectedGroup = groups.get(selectedGroupIndex);
        if (!(params.forceLowestBitrate || adaptiveTrackSelectionFactory == null)) {
            int[] adaptiveTracks = getAdaptiveAudioTracks(selectedGroup, formatSupport[selectedGroupIndex], params.allowMixedMimeAdaptiveness);
            if (adaptiveTracks.length > 0) {
                return adaptiveTrackSelectionFactory.createTrackSelection(selectedGroup, adaptiveTracks);
            }
        }
        return new FixedTrackSelection(selectedGroup, selectedTrackIndex);
    }

    private static int[] getAdaptiveAudioTracks(TrackGroup group, int[] formatSupport, boolean allowMixedMimeTypes) {
        HashSet<AudioConfigurationTuple> seenConfigurationTuples = new HashSet();
        int i = 0;
        AudioConfigurationTuple selectedConfiguration = null;
        int selectedConfigurationTrackCount = 0;
        for (int i2 = 0; i2 < group.length; i2++) {
            Format format = group.getFormat(i2);
            AudioConfigurationTuple configuration = new AudioConfigurationTuple(format.channelCount, format.sampleRate, allowMixedMimeTypes ? null : format.sampleMimeType);
            if (seenConfigurationTuples.add(configuration)) {
                int configurationCount = getAdaptiveAudioTrackCount(group, formatSupport, configuration);
                if (configurationCount > selectedConfigurationTrackCount) {
                    selectedConfiguration = configuration;
                    selectedConfigurationTrackCount = configurationCount;
                }
            }
        }
        if (selectedConfigurationTrackCount <= 1) {
            return NO_TRACKS;
        }
        int[] adaptiveIndices = new int[selectedConfigurationTrackCount];
        int index = 0;
        while (i < group.length) {
            if (isSupportedAdaptiveAudioTrack(group.getFormat(i), formatSupport[i], selectedConfiguration)) {
                int index2 = index + 1;
                adaptiveIndices[index] = i;
                index = index2;
            }
            i++;
        }
        return adaptiveIndices;
    }

    private static int getAdaptiveAudioTrackCount(TrackGroup group, int[] formatSupport, AudioConfigurationTuple configuration) {
        int count = 0;
        for (int i = 0; i < group.length; i++) {
            if (isSupportedAdaptiveAudioTrack(group.getFormat(i), formatSupport[i], configuration)) {
                count++;
            }
        }
        return count;
    }

    private static boolean isSupportedAdaptiveAudioTrack(Format format, int formatSupport, AudioConfigurationTuple configuration) {
        if (!isSupported(formatSupport, false) || format.channelCount != configuration.channelCount || format.sampleRate != configuration.sampleRate) {
            return false;
        }
        if (configuration.mimeType == null || TextUtils.equals(configuration.mimeType, format.sampleMimeType)) {
            return true;
        }
        return false;
    }

    protected TrackSelection selectOtherTrack(int trackType, TrackGroupArray groups, int[][] formatSupport, Parameters params) throws ExoPlaybackException {
        Parameters parameters;
        TrackGroupArray trackGroupArray = groups;
        int selectedTrackScore = 0;
        int selectedTrackIndex = 0;
        TrackGroup selectedGroup = null;
        int groupIndex = 0;
        while (groupIndex < trackGroupArray.length) {
            TrackGroup trackGroup = trackGroupArray.get(groupIndex);
            int[] trackFormatSupport = formatSupport[groupIndex];
            int selectedTrackScore2 = selectedTrackScore;
            selectedTrackScore = selectedTrackIndex;
            TrackGroup selectedGroup2 = selectedGroup;
            for (int trackIndex = 0; trackIndex < trackGroup.length; trackIndex++) {
                if (isSupported(trackFormatSupport[trackIndex], params.exceedRendererCapabilitiesIfNecessary)) {
                    int trackScore = 1;
                    if ((trackGroup.getFormat(trackIndex).selectionFlags & 1) != 0) {
                        trackScore = 2;
                    }
                    if (isSupported(trackFormatSupport[trackIndex], false)) {
                        trackScore += WITHIN_RENDERER_CAPABILITIES_BONUS;
                    }
                    if (trackScore > selectedTrackScore2) {
                        selectedGroup2 = trackGroup;
                        selectedTrackScore = trackIndex;
                        selectedTrackScore2 = trackScore;
                    }
                }
            }
            parameters = params;
            groupIndex++;
            selectedGroup = selectedGroup2;
            selectedTrackIndex = selectedTrackScore;
            selectedTrackScore = selectedTrackScore2;
        }
        parameters = params;
        return selectedGroup == null ? null : new FixedTrackSelection(selectedGroup, selectedTrackIndex);
    }

    protected static boolean isSupported(int formatSupport, boolean allowExceedsCapabilities) {
        int maskedSupport = formatSupport & 7;
        if (maskedSupport != 4) {
            if (!allowExceedsCapabilities || maskedSupport != 3) {
                return false;
            }
        }
        return true;
    }

    protected static boolean formatHasNoLanguage(Format format) {
        if (!TextUtils.isEmpty(format.language)) {
            if (!formatHasLanguage(format, C.LANGUAGE_UNDETERMINED)) {
                return false;
            }
        }
        return true;
    }

    protected static boolean formatHasLanguage(Format format, String language) {
        return language != null && TextUtils.equals(language, Util.normalizeLanguageCode(format.language));
    }

    private static List<Integer> getViewportFilteredTrackIndices(TrackGroup group, int viewportWidth, int viewportHeight, boolean orientationMayChange) {
        ArrayList<Integer> selectedTrackIndices = new ArrayList(group.length);
        int i = 0;
        for (int i2 = 0; i2 < group.length; i2++) {
            selectedTrackIndices.add(Integer.valueOf(i2));
        }
        if (viewportWidth != ConnectionsManager.DEFAULT_DATACENTER_ID) {
            if (viewportHeight != ConnectionsManager.DEFAULT_DATACENTER_ID) {
                int maxVideoPixelsToRetain = ConnectionsManager.DEFAULT_DATACENTER_ID;
                while (i < group.length) {
                    Format format = group.getFormat(i);
                    if (format.width > 0 && format.height > 0) {
                        Point maxVideoSizeInViewport = getMaxVideoSizeInViewport(orientationMayChange, viewportWidth, viewportHeight, format.width, format.height);
                        int videoPixels = format.width * format.height;
                        if (format.width >= ((int) (((float) maxVideoSizeInViewport.x) * FRACTION_TO_CONSIDER_FULLSCREEN)) && format.height >= ((int) (((float) maxVideoSizeInViewport.y) * FRACTION_TO_CONSIDER_FULLSCREEN)) && videoPixels < maxVideoPixelsToRetain) {
                            maxVideoPixelsToRetain = videoPixels;
                        }
                    }
                    i++;
                }
                if (maxVideoPixelsToRetain != ConnectionsManager.DEFAULT_DATACENTER_ID) {
                    for (i = selectedTrackIndices.size() - 1; i >= 0; i--) {
                        int pixelCount = group.getFormat(((Integer) selectedTrackIndices.get(i)).intValue()).getPixelCount();
                        if (pixelCount == -1 || pixelCount > maxVideoPixelsToRetain) {
                            selectedTrackIndices.remove(i);
                        }
                    }
                }
                return selectedTrackIndices;
            }
        }
        return selectedTrackIndices;
    }

    private static Point getMaxVideoSizeInViewport(boolean orientationMayChange, int viewportWidth, int viewportHeight, int videoWidth, int videoHeight) {
        if (orientationMayChange) {
            Object obj = null;
            Object obj2 = videoWidth > videoHeight ? 1 : null;
            if (viewportWidth > viewportHeight) {
                obj = 1;
            }
            if (obj2 != obj) {
                int tempViewportWidth = viewportWidth;
                viewportWidth = viewportHeight;
                viewportHeight = tempViewportWidth;
            }
        }
        if (videoWidth * viewportHeight >= videoHeight * viewportWidth) {
            return new Point(viewportWidth, Util.ceilDivide(viewportWidth * videoHeight, videoWidth));
        }
        return new Point(Util.ceilDivide(viewportHeight * videoWidth, videoHeight), viewportHeight);
    }

    private static int compareInts(int first, int second) {
        if (first > second) {
            return 1;
        }
        return second > first ? -1 : 0;
    }
}
