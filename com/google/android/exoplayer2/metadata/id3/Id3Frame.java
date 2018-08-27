package com.google.android.exoplayer2.metadata.id3;

import com.google.android.exoplayer2.metadata.Metadata.Entry;

public abstract class Id3Frame implements Entry {
    public final String id;

    public Id3Frame(String id) {
        this.id = id;
    }

    public String toString() {
        return this.id;
    }

    public int describeContents() {
        return 0;
    }
}
