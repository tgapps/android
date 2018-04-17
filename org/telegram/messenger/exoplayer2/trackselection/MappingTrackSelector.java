package org.telegram.messenger.exoplayer2.trackselection;

import android.util.SparseArray;
import android.util.SparseBooleanArray;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.telegram.messenger.exoplayer2.ExoPlaybackException;
import org.telegram.messenger.exoplayer2.RendererCapabilities;
import org.telegram.messenger.exoplayer2.RendererConfiguration;
import org.telegram.messenger.exoplayer2.source.TrackGroup;
import org.telegram.messenger.exoplayer2.source.TrackGroupArray;
import org.telegram.messenger.exoplayer2.trackselection.TrackSelection.Factory;
import org.telegram.messenger.exoplayer2.util.Util;

public abstract class MappingTrackSelector extends TrackSelector {
    private MappedTrackInfo currentMappedTrackInfo;
    private final SparseBooleanArray rendererDisabledFlags = new SparseBooleanArray();
    private final SparseArray<Map<TrackGroupArray, SelectionOverride>> selectionOverrides = new SparseArray();
    private int tunnelingAudioSessionId = 0;

    public static final class MappedTrackInfo {
        public static final int RENDERER_SUPPORT_EXCEEDS_CAPABILITIES_TRACKS = 2;
        public static final int RENDERER_SUPPORT_NO_TRACKS = 0;
        public static final int RENDERER_SUPPORT_PLAYABLE_TRACKS = 3;
        public static final int RENDERER_SUPPORT_UNSUPPORTED_TRACKS = 1;
        private final int[][][] formatSupport;
        public final int length;
        private final int[] mixedMimeTypeAdaptiveSupport;
        private final int[] rendererTrackTypes;
        private final TrackGroupArray[] trackGroups;
        private final TrackGroupArray unassociatedTrackGroups;

        public int getRendererSupport(int r1) {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.exoplayer2.trackselection.MappingTrackSelector.MappedTrackInfo.getRendererSupport(int):int
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
            r0 = 0;
            r1 = r6.formatSupport;
            r1 = r1[r7];
            r2 = 0;
            r3 = r0;
            r0 = r2;
            r4 = r1.length;
            if (r0 >= r4) goto L_0x002d;
        L_0x000b:
            r4 = r3;
            r3 = r2;
            r5 = r1[r0];
            r5 = r5.length;
            if (r3 >= r5) goto L_0x0029;
        L_0x0012:
            r5 = r1[r0];
            r5 = r5[r3];
            r5 = r5 & 7;
            switch(r5) {
                case 3: goto L_0x001f;
                case 4: goto L_0x001d;
                default: goto L_0x001b;
            };
        L_0x001b:
            r5 = 1;
            goto L_0x0021;
        L_0x001d:
            r2 = 3;
            return r2;
        L_0x001f:
            r5 = 2;
            r4 = java.lang.Math.max(r4, r5);
            r3 = r3 + 1;
            goto L_0x000d;
        L_0x0029:
            r0 = r0 + 1;
            r3 = r4;
            goto L_0x0008;
        L_0x002d:
            return r3;
            */
            throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.exoplayer2.trackselection.MappingTrackSelector.MappedTrackInfo.getRendererSupport(int):int");
        }

        MappedTrackInfo(int[] rendererTrackTypes, TrackGroupArray[] trackGroups, int[] mixedMimeTypeAdaptiveSupport, int[][][] formatSupport, TrackGroupArray unassociatedTrackGroups) {
            this.rendererTrackTypes = rendererTrackTypes;
            this.trackGroups = trackGroups;
            this.formatSupport = formatSupport;
            this.mixedMimeTypeAdaptiveSupport = mixedMimeTypeAdaptiveSupport;
            this.unassociatedTrackGroups = unassociatedTrackGroups;
            this.length = trackGroups.length;
        }

        public TrackGroupArray getTrackGroups(int rendererIndex) {
            return this.trackGroups[rendererIndex];
        }

        public int getTrackTypeRendererSupport(int trackType) {
            int bestRendererSupport = 0;
            for (int i = 0; i < this.length; i++) {
                if (this.rendererTrackTypes[i] == trackType) {
                    bestRendererSupport = Math.max(bestRendererSupport, getRendererSupport(i));
                }
            }
            return bestRendererSupport;
        }

        public int getTrackFormatSupport(int rendererIndex, int groupIndex, int trackIndex) {
            return this.formatSupport[rendererIndex][groupIndex][trackIndex] & 7;
        }

        public int getAdaptiveSupport(int rendererIndex, int groupIndex, boolean includeCapabilitiesExceededTracks) {
            int trackCount = this.trackGroups[rendererIndex].get(groupIndex).length;
            int[] trackIndices = new int[trackCount];
            int trackIndexCount = 0;
            for (int i = 0; i < trackCount; i++) {
                int fixedSupport = getTrackFormatSupport(rendererIndex, groupIndex, i);
                if (fixedSupport == 4 || (includeCapabilitiesExceededTracks && fixedSupport == 3)) {
                    int trackIndexCount2 = trackIndexCount + 1;
                    trackIndices[trackIndexCount] = i;
                    trackIndexCount = trackIndexCount2;
                }
            }
            return getAdaptiveSupport(rendererIndex, groupIndex, Arrays.copyOf(trackIndices, trackIndexCount));
        }

        public int getAdaptiveSupport(int rendererIndex, int groupIndex, int[] trackIndices) {
            int handledTrackCount = 0;
            int adaptiveSupport = 16;
            boolean multipleMimeTypes = false;
            String firstSampleMimeType = null;
            int i = 0;
            while (i < trackIndices.length) {
                String sampleMimeType = this.trackGroups[rendererIndex].get(groupIndex).getFormat(trackIndices[i]).sampleMimeType;
                int handledTrackCount2 = handledTrackCount + 1;
                if (handledTrackCount == 0) {
                    firstSampleMimeType = sampleMimeType;
                } else {
                    multipleMimeTypes = (Util.areEqual(firstSampleMimeType, sampleMimeType) ^ 1) | multipleMimeTypes;
                }
                adaptiveSupport = Math.min(adaptiveSupport, this.formatSupport[rendererIndex][groupIndex][i] & 24);
                i++;
                handledTrackCount = handledTrackCount2;
            }
            if (multipleMimeTypes) {
                return Math.min(adaptiveSupport, this.mixedMimeTypeAdaptiveSupport[rendererIndex]);
            }
            return adaptiveSupport;
        }

        public TrackGroupArray getUnassociatedTrackGroups() {
            return this.unassociatedTrackGroups;
        }
    }

    public static final class SelectionOverride {
        public final Factory factory;
        public final int groupIndex;
        public final int length;
        public final int[] tracks;

        public SelectionOverride(Factory factory, int groupIndex, int... tracks) {
            this.factory = factory;
            this.groupIndex = groupIndex;
            this.tracks = tracks;
            this.length = tracks.length;
        }

        public TrackSelection createTrackSelection(TrackGroupArray groups) {
            return this.factory.createTrackSelection(groups.get(this.groupIndex), this.tracks);
        }

        public boolean containsTrack(int track) {
            for (int overrideTrack : this.tracks) {
                if (overrideTrack == track) {
                    return true;
                }
            }
            return false;
        }
    }

    protected abstract TrackSelection[] selectTracks(RendererCapabilities[] rendererCapabilitiesArr, TrackGroupArray[] trackGroupArrayArr, int[][][] iArr) throws ExoPlaybackException;

    public final MappedTrackInfo getCurrentMappedTrackInfo() {
        return this.currentMappedTrackInfo;
    }

    public final void setRendererDisabled(int rendererIndex, boolean disabled) {
        if (this.rendererDisabledFlags.get(rendererIndex) != disabled) {
            this.rendererDisabledFlags.put(rendererIndex, disabled);
            invalidate();
        }
    }

    public final boolean getRendererDisabled(int rendererIndex) {
        return this.rendererDisabledFlags.get(rendererIndex);
    }

    public final void setSelectionOverride(int rendererIndex, TrackGroupArray groups, SelectionOverride override) {
        Map<TrackGroupArray, SelectionOverride> overrides = (Map) this.selectionOverrides.get(rendererIndex);
        if (overrides == null) {
            overrides = new HashMap();
            this.selectionOverrides.put(rendererIndex, overrides);
        }
        if (!overrides.containsKey(groups) || !Util.areEqual(overrides.get(groups), override)) {
            overrides.put(groups, override);
            invalidate();
        }
    }

    public final boolean hasSelectionOverride(int rendererIndex, TrackGroupArray groups) {
        Map<TrackGroupArray, SelectionOverride> overrides = (Map) this.selectionOverrides.get(rendererIndex);
        return overrides != null && overrides.containsKey(groups);
    }

    public final SelectionOverride getSelectionOverride(int rendererIndex, TrackGroupArray groups) {
        Map<TrackGroupArray, SelectionOverride> overrides = (Map) this.selectionOverrides.get(rendererIndex);
        return overrides != null ? (SelectionOverride) overrides.get(groups) : null;
    }

    public final void clearSelectionOverride(int rendererIndex, TrackGroupArray groups) {
        Map<TrackGroupArray, SelectionOverride> overrides = (Map) this.selectionOverrides.get(rendererIndex);
        if (overrides != null) {
            if (overrides.containsKey(groups)) {
                overrides.remove(groups);
                if (overrides.isEmpty()) {
                    this.selectionOverrides.remove(rendererIndex);
                }
                invalidate();
            }
        }
    }

    public final void clearSelectionOverrides(int rendererIndex) {
        Map<TrackGroupArray, ?> overrides = (Map) this.selectionOverrides.get(rendererIndex);
        if (overrides != null) {
            if (!overrides.isEmpty()) {
                this.selectionOverrides.remove(rendererIndex);
                invalidate();
            }
        }
    }

    public final void clearSelectionOverrides() {
        if (this.selectionOverrides.size() != 0) {
            this.selectionOverrides.clear();
            invalidate();
        }
    }

    public void setTunnelingAudioSessionId(int tunnelingAudioSessionId) {
        if (this.tunnelingAudioSessionId != tunnelingAudioSessionId) {
            this.tunnelingAudioSessionId = tunnelingAudioSessionId;
            invalidate();
        }
    }

    public final TrackSelectorResult selectTracks(RendererCapabilities[] rendererCapabilities, TrackGroupArray trackGroups) throws ExoPlaybackException {
        int i;
        MappingTrackSelector mappingTrackSelector = this;
        RendererCapabilities[] rendererCapabilitiesArr = rendererCapabilities;
        TrackGroupArray trackGroupArray = trackGroups;
        int i2 = 0;
        int[] rendererTrackGroupCounts = new int[(rendererCapabilitiesArr.length + 1)];
        TrackGroup[][] rendererTrackGroups = new TrackGroup[(rendererCapabilitiesArr.length + 1)][];
        int[][][] rendererFormatSupports = new int[(rendererCapabilitiesArr.length + 1)][][];
        for (i = 0; i < rendererTrackGroups.length; i++) {
            rendererTrackGroups[i] = new TrackGroup[trackGroupArray.length];
            rendererFormatSupports[i] = new int[trackGroupArray.length][];
        }
        int[] mixedMimeTypeAdaptationSupport = getMixedMimeTypeAdaptationSupport(rendererCapabilities);
        for (i = 0; i < trackGroupArray.length; i++) {
            int[] rendererFormatSupport;
            TrackGroup group = trackGroupArray.get(i);
            int rendererIndex = findRenderer(rendererCapabilitiesArr, group);
            if (rendererIndex == rendererCapabilitiesArr.length) {
                rendererFormatSupport = new int[group.length];
            } else {
                rendererFormatSupport = getFormatSupport(rendererCapabilitiesArr[rendererIndex], group);
            }
            int rendererTrackGroupCount = rendererTrackGroupCounts[rendererIndex];
            rendererTrackGroups[rendererIndex][rendererTrackGroupCount] = group;
            rendererFormatSupports[rendererIndex][rendererTrackGroupCount] = rendererFormatSupport;
            rendererTrackGroupCounts[rendererIndex] = rendererTrackGroupCounts[rendererIndex] + 1;
        }
        TrackGroupArray[] rendererTrackGroupArrays = new TrackGroupArray[rendererCapabilitiesArr.length];
        int[] rendererTrackTypes = new int[rendererCapabilitiesArr.length];
        for (i = 0; i < rendererCapabilitiesArr.length; i++) {
            int rendererTrackGroupCount2 = rendererTrackGroupCounts[i];
            rendererTrackGroupArrays[i] = new TrackGroupArray((TrackGroup[]) Arrays.copyOf(rendererTrackGroups[i], rendererTrackGroupCount2));
            rendererFormatSupports[i] = (int[][]) Arrays.copyOf(rendererFormatSupports[i], rendererTrackGroupCount2);
            rendererTrackTypes[i] = rendererCapabilitiesArr[i].getTrackType();
        }
        int unassociatedTrackGroupCount = rendererTrackGroupCounts[rendererCapabilitiesArr.length];
        TrackGroupArray unassociatedTrackGroupArray = new TrackGroupArray((TrackGroup[]) Arrays.copyOf(rendererTrackGroups[rendererCapabilitiesArr.length], unassociatedTrackGroupCount));
        TrackSelection[] trackSelections = selectTracks(rendererCapabilitiesArr, rendererTrackGroupArrays, rendererFormatSupports);
        i = 0;
        while (true) {
            TrackSelection trackSelection = null;
            if (i >= rendererCapabilitiesArr.length) {
                break;
            }
            if (mappingTrackSelector.rendererDisabledFlags.get(i)) {
                trackSelections[i] = null;
            } else {
                TrackGroupArray rendererTrackGroup = rendererTrackGroupArrays[i];
                if (hasSelectionOverride(i, rendererTrackGroup)) {
                    SelectionOverride override = (SelectionOverride) ((Map) mappingTrackSelector.selectionOverrides.get(i)).get(rendererTrackGroup);
                    if (override != null) {
                        trackSelection = override.createTrackSelection(rendererTrackGroup);
                    }
                    trackSelections[i] = trackSelection;
                }
            }
            i++;
        }
        boolean[] rendererEnabled = determineEnabledRenderers(rendererCapabilitiesArr, trackSelections);
        MappedTrackInfo mappedTrackInfo = new MappedTrackInfo(rendererTrackTypes, rendererTrackGroupArrays, mixedMimeTypeAdaptationSupport, rendererFormatSupports, unassociatedTrackGroupArray);
        RendererConfiguration[] rendererConfigurations = new RendererConfiguration[rendererCapabilitiesArr.length];
        while (i2 < rendererCapabilitiesArr.length) {
            rendererConfigurations[i2] = rendererEnabled[i2] ? RendererConfiguration.DEFAULT : null;
            i2++;
        }
        int i3 = mappingTrackSelector.tunnelingAudioSessionId;
        RendererConfiguration[] rendererConfigurations2 = rendererConfigurations;
        TrackSelection[] trackSelections2 = trackSelections;
        maybeConfigureRenderersForTunneling(rendererCapabilitiesArr, rendererTrackGroupArrays, rendererFormatSupports, rendererConfigurations, trackSelections, i3);
        TrackSelectionArray trackSelectionArray = new TrackSelectionArray(trackSelections2);
        return new TrackSelectorResult(trackGroupArray, rendererEnabled, trackSelectionArray, mappedTrackInfo, rendererConfigurations2);
    }

    private boolean[] determineEnabledRenderers(RendererCapabilities[] rendererCapabilities, TrackSelection[] trackSelections) {
        boolean[] rendererEnabled = new boolean[trackSelections.length];
        for (int i = 0; i < rendererEnabled.length; i++) {
            boolean z = !this.rendererDisabledFlags.get(i) && (rendererCapabilities[i].getTrackType() == 5 || trackSelections[i] != null);
            rendererEnabled[i] = z;
        }
        return rendererEnabled;
    }

    public final void onSelectionActivated(Object info) {
        this.currentMappedTrackInfo = (MappedTrackInfo) info;
    }

    private static int findRenderer(RendererCapabilities[] rendererCapabilities, TrackGroup group) throws ExoPlaybackException {
        int bestFormatSupportLevel = 0;
        int bestRendererIndex = rendererCapabilities.length;
        int rendererIndex = 0;
        while (rendererIndex < rendererCapabilities.length) {
            RendererCapabilities rendererCapability = rendererCapabilities[rendererIndex];
            int bestRendererIndex2 = bestRendererIndex;
            for (bestRendererIndex = 0; bestRendererIndex < group.length; bestRendererIndex++) {
                int formatSupportLevel = rendererCapability.supportsFormat(group.getFormat(bestRendererIndex)) & 7;
                if (formatSupportLevel > bestFormatSupportLevel) {
                    bestRendererIndex2 = rendererIndex;
                    bestFormatSupportLevel = formatSupportLevel;
                    if (bestFormatSupportLevel == 4) {
                        return bestRendererIndex2;
                    }
                }
            }
            rendererIndex++;
            bestRendererIndex = bestRendererIndex2;
        }
        return bestRendererIndex;
    }

    private static int[] getFormatSupport(RendererCapabilities rendererCapabilities, TrackGroup group) throws ExoPlaybackException {
        int[] formatSupport = new int[group.length];
        for (int i = 0; i < group.length; i++) {
            formatSupport[i] = rendererCapabilities.supportsFormat(group.getFormat(i));
        }
        return formatSupport;
    }

    private static int[] getMixedMimeTypeAdaptationSupport(RendererCapabilities[] rendererCapabilities) throws ExoPlaybackException {
        int[] mixedMimeTypeAdaptationSupport = new int[rendererCapabilities.length];
        for (int i = 0; i < mixedMimeTypeAdaptationSupport.length; i++) {
            mixedMimeTypeAdaptationSupport[i] = rendererCapabilities[i].supportsMixedMimeTypeAdaptation();
        }
        return mixedMimeTypeAdaptationSupport;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static void maybeConfigureRenderersForTunneling(org.telegram.messenger.exoplayer2.RendererCapabilities[] r14, org.telegram.messenger.exoplayer2.source.TrackGroupArray[] r15, int[][][] r16, org.telegram.messenger.exoplayer2.RendererConfiguration[] r17, org.telegram.messenger.exoplayer2.trackselection.TrackSelection[] r18, int r19) {
        /*
        r0 = r14;
        r1 = r19;
        if (r1 != 0) goto L_0x0006;
    L_0x0005:
        return;
    L_0x0006:
        r2 = -1;
        r3 = -1;
        r4 = 1;
        r6 = r3;
        r3 = r2;
        r2 = 0;
    L_0x000c:
        r7 = -1;
        r8 = 1;
        r9 = r0.length;
        if (r2 >= r9) goto L_0x003b;
    L_0x0011:
        r9 = r0[r2];
        r9 = r9.getTrackType();
        r10 = r18[r2];
        if (r9 == r8) goto L_0x001e;
    L_0x001b:
        r11 = 2;
        if (r9 != r11) goto L_0x0038;
    L_0x001e:
        if (r10 == 0) goto L_0x0038;
    L_0x0020:
        r12 = r16[r2];
        r5 = r15[r2];
        r5 = rendererSupportsTunneling(r12, r5, r10);
        if (r5 == 0) goto L_0x0038;
    L_0x002a:
        if (r9 != r8) goto L_0x0032;
    L_0x002c:
        if (r3 == r7) goto L_0x0030;
    L_0x002e:
        r4 = 0;
        goto L_0x003b;
    L_0x0030:
        r3 = r2;
        goto L_0x0038;
    L_0x0032:
        if (r6 == r7) goto L_0x0036;
    L_0x0034:
        r4 = 0;
        goto L_0x003b;
    L_0x0036:
        r5 = r2;
        r6 = r5;
    L_0x0038:
        r2 = r2 + 1;
        goto L_0x000c;
    L_0x003b:
        if (r3 == r7) goto L_0x0040;
    L_0x003d:
        if (r6 == r7) goto L_0x0040;
    L_0x003f:
        goto L_0x0041;
    L_0x0040:
        r8 = 0;
    L_0x0041:
        r2 = r4 & r8;
        if (r2 == 0) goto L_0x004e;
    L_0x0045:
        r4 = new org.telegram.messenger.exoplayer2.RendererConfiguration;
        r4.<init>(r1);
        r17[r3] = r4;
        r17[r6] = r4;
    L_0x004e:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.exoplayer2.trackselection.MappingTrackSelector.maybeConfigureRenderersForTunneling(org.telegram.messenger.exoplayer2.RendererCapabilities[], org.telegram.messenger.exoplayer2.source.TrackGroupArray[], int[][][], org.telegram.messenger.exoplayer2.RendererConfiguration[], org.telegram.messenger.exoplayer2.trackselection.TrackSelection[], int):void");
    }

    private static boolean rendererSupportsTunneling(int[][] formatSupport, TrackGroupArray trackGroups, TrackSelection selection) {
        if (selection == null) {
            return false;
        }
        int trackGroupIndex = trackGroups.indexOf(selection.getTrackGroup());
        for (int i = 0; i < selection.length(); i++) {
            if ((formatSupport[trackGroupIndex][selection.getIndexInTrackGroup(i)] & 32) != 32) {
                return false;
            }
        }
        return true;
    }
}
