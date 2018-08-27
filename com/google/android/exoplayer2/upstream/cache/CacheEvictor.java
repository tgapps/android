package com.google.android.exoplayer2.upstream.cache;

import com.google.android.exoplayer2.upstream.cache.Cache.Listener;

public interface CacheEvictor extends Listener {
    void onCacheInitialized();

    void onStartFile(Cache cache, String str, long j, long j2);
}
