package com.google.android.gms.signin;

import com.google.android.gms.common.api.Api.ApiOptions.Optional;

public final class SignInOptions implements Optional {
    public static final SignInOptions DEFAULT = new Builder().build();
    private final boolean zzadb;
    private final boolean zzadc;
    private final Long zzadd;
    private final Long zzade;
    private final boolean zzt;
    private final boolean zzv;
    private final String zzw;
    private final String zzx;

    public static final class Builder {
        private boolean zzadf;
        private boolean zzadg;
        private String zzadh;
        private boolean zzadi;
        private String zzadj;
        private boolean zzadk;
        private Long zzadl;
        private Long zzadm;

        public final SignInOptions build() {
            return new SignInOptions(this.zzadf, this.zzadg, this.zzadh, this.zzadi, this.zzadj, this.zzadk, this.zzadl, this.zzadm);
        }
    }

    private SignInOptions(boolean z, boolean z2, String str, boolean z3, String str2, boolean z4, Long l, Long l2) {
        this.zzadb = z;
        this.zzt = z2;
        this.zzw = str;
        this.zzv = z3;
        this.zzadc = z4;
        this.zzx = str2;
        this.zzadd = l;
        this.zzade = l2;
    }

    public final Long getAuthApiSignInModuleVersion() {
        return this.zzadd;
    }

    public final String getHostedDomain() {
        return this.zzx;
    }

    public final Long getRealClientLibraryVersion() {
        return this.zzade;
    }

    public final String getServerClientId() {
        return this.zzw;
    }

    public final boolean isForceCodeForRefreshToken() {
        return this.zzv;
    }

    public final boolean isIdTokenRequested() {
        return this.zzt;
    }

    public final boolean isOfflineAccessRequested() {
        return this.zzadb;
    }

    public final boolean waitForAccessTokenRefresh() {
        return this.zzadc;
    }
}
