package com.google.android.gms.internal.measurement;

import java.io.IOException;
import java.util.List;
import org.telegram.tgnet.ConnectionsManager;

final class zzur implements zzxi {
    private int tag;
    private final zzuo zzbur;
    private int zzbus;
    private int zzbut = 0;

    public static zzur zza(zzuo com_google_android_gms_internal_measurement_zzuo) {
        if (com_google_android_gms_internal_measurement_zzuo.zzbuk != null) {
            return com_google_android_gms_internal_measurement_zzuo.zzbuk;
        }
        return new zzur(com_google_android_gms_internal_measurement_zzuo);
    }

    private zzur(zzuo com_google_android_gms_internal_measurement_zzuo) {
        this.zzbur = (zzuo) zzvo.zza(com_google_android_gms_internal_measurement_zzuo, "input");
        this.zzbur.zzbuk = this;
    }

    public final int zzve() throws IOException {
        if (this.zzbut != 0) {
            this.tag = this.zzbut;
            this.zzbut = 0;
        } else {
            this.tag = this.zzbur.zzug();
        }
        if (this.tag == 0 || this.tag == this.zzbus) {
            return ConnectionsManager.DEFAULT_DATACENTER_ID;
        }
        return this.tag >>> 3;
    }

    public final int getTag() {
        return this.tag;
    }

    public final boolean zzvf() throws IOException {
        if (this.zzbur.zzuw() || this.tag == this.zzbus) {
            return false;
        }
        return this.zzbur.zzao(this.tag);
    }

    private final void zzat(int i) throws IOException {
        if ((this.tag & 7) != i) {
            throw zzvt.zzwo();
        }
    }

    public final double readDouble() throws IOException {
        zzat(1);
        return this.zzbur.readDouble();
    }

    public final float readFloat() throws IOException {
        zzat(5);
        return this.zzbur.readFloat();
    }

    public final long zzuh() throws IOException {
        zzat(0);
        return this.zzbur.zzuh();
    }

    public final long zzui() throws IOException {
        zzat(0);
        return this.zzbur.zzui();
    }

    public final int zzuj() throws IOException {
        zzat(0);
        return this.zzbur.zzuj();
    }

    public final long zzuk() throws IOException {
        zzat(1);
        return this.zzbur.zzuk();
    }

    public final int zzul() throws IOException {
        zzat(5);
        return this.zzbur.zzul();
    }

    public final boolean zzum() throws IOException {
        zzat(0);
        return this.zzbur.zzum();
    }

    public final String readString() throws IOException {
        zzat(2);
        return this.zzbur.readString();
    }

    public final String zzun() throws IOException {
        zzat(2);
        return this.zzbur.zzun();
    }

    public final <T> T zza(zzxj<T> com_google_android_gms_internal_measurement_zzxj_T, zzuz com_google_android_gms_internal_measurement_zzuz) throws IOException {
        zzat(2);
        return zzc(com_google_android_gms_internal_measurement_zzxj_T, com_google_android_gms_internal_measurement_zzuz);
    }

    public final <T> T zzb(zzxj<T> com_google_android_gms_internal_measurement_zzxj_T, zzuz com_google_android_gms_internal_measurement_zzuz) throws IOException {
        zzat(3);
        return zzd(com_google_android_gms_internal_measurement_zzxj_T, com_google_android_gms_internal_measurement_zzuz);
    }

    private final <T> T zzc(zzxj<T> com_google_android_gms_internal_measurement_zzxj_T, zzuz com_google_android_gms_internal_measurement_zzuz) throws IOException {
        int zzup = this.zzbur.zzup();
        if (this.zzbur.zzbuh >= this.zzbur.zzbui) {
            throw zzvt.zzwp();
        }
        zzup = this.zzbur.zzaq(zzup);
        T newInstance = com_google_android_gms_internal_measurement_zzxj_T.newInstance();
        zzuo com_google_android_gms_internal_measurement_zzuo = this.zzbur;
        com_google_android_gms_internal_measurement_zzuo.zzbuh++;
        com_google_android_gms_internal_measurement_zzxj_T.zza(newInstance, this, com_google_android_gms_internal_measurement_zzuz);
        com_google_android_gms_internal_measurement_zzxj_T.zzu(newInstance);
        this.zzbur.zzan(0);
        com_google_android_gms_internal_measurement_zzuo = this.zzbur;
        com_google_android_gms_internal_measurement_zzuo.zzbuh--;
        this.zzbur.zzar(zzup);
        return newInstance;
    }

    private final <T> T zzd(zzxj<T> com_google_android_gms_internal_measurement_zzxj_T, zzuz com_google_android_gms_internal_measurement_zzuz) throws IOException {
        int i = this.zzbus;
        this.zzbus = ((this.tag >>> 3) << 3) | 4;
        try {
            T newInstance = com_google_android_gms_internal_measurement_zzxj_T.newInstance();
            com_google_android_gms_internal_measurement_zzxj_T.zza(newInstance, this, com_google_android_gms_internal_measurement_zzuz);
            com_google_android_gms_internal_measurement_zzxj_T.zzu(newInstance);
            if (this.tag == this.zzbus) {
                return newInstance;
            }
            throw zzvt.zzwq();
        } finally {
            this.zzbus = i;
        }
    }

    public final zzud zzuo() throws IOException {
        zzat(2);
        return this.zzbur.zzuo();
    }

    public final int zzup() throws IOException {
        zzat(0);
        return this.zzbur.zzup();
    }

    public final int zzuq() throws IOException {
        zzat(0);
        return this.zzbur.zzuq();
    }

    public final int zzur() throws IOException {
        zzat(5);
        return this.zzbur.zzur();
    }

    public final long zzus() throws IOException {
        zzat(1);
        return this.zzbur.zzus();
    }

    public final int zzut() throws IOException {
        zzat(0);
        return this.zzbur.zzut();
    }

    public final long zzuu() throws IOException {
        zzat(0);
        return this.zzbur.zzuu();
    }

    public final void zzh(List<Double> list) throws IOException {
        int zzup;
        if (list instanceof zzuw) {
            zzuw com_google_android_gms_internal_measurement_zzuw = (zzuw) list;
            switch (this.tag & 7) {
                case 1:
                    break;
                case 2:
                    zzup = this.zzbur.zzup();
                    zzau(zzup);
                    zzup += this.zzbur.zzux();
                    do {
                        com_google_android_gms_internal_measurement_zzuw.zzd(this.zzbur.readDouble());
                    } while (this.zzbur.zzux() < zzup);
                    return;
                default:
                    throw zzvt.zzwo();
            }
            do {
                com_google_android_gms_internal_measurement_zzuw.zzd(this.zzbur.readDouble());
                if (!this.zzbur.zzuw()) {
                    zzup = this.zzbur.zzug();
                } else {
                    return;
                }
            } while (zzup == this.tag);
            this.zzbut = zzup;
            return;
        }
        switch (this.tag & 7) {
            case 1:
                break;
            case 2:
                zzup = this.zzbur.zzup();
                zzau(zzup);
                zzup += this.zzbur.zzux();
                do {
                    list.add(Double.valueOf(this.zzbur.readDouble()));
                } while (this.zzbur.zzux() < zzup);
                return;
            default:
                throw zzvt.zzwo();
        }
        do {
            list.add(Double.valueOf(this.zzbur.readDouble()));
            if (!this.zzbur.zzuw()) {
                zzup = this.zzbur.zzug();
            } else {
                return;
            }
        } while (zzup == this.tag);
        this.zzbut = zzup;
    }

    public final void zzi(List<Float> list) throws IOException {
        int zzup;
        if (list instanceof zzvj) {
            zzvj com_google_android_gms_internal_measurement_zzvj = (zzvj) list;
            switch (this.tag & 7) {
                case 2:
                    zzup = this.zzbur.zzup();
                    zzav(zzup);
                    zzup += this.zzbur.zzux();
                    do {
                        com_google_android_gms_internal_measurement_zzvj.zzc(this.zzbur.readFloat());
                    } while (this.zzbur.zzux() < zzup);
                    return;
                case 5:
                    break;
                default:
                    throw zzvt.zzwo();
            }
            do {
                com_google_android_gms_internal_measurement_zzvj.zzc(this.zzbur.readFloat());
                if (!this.zzbur.zzuw()) {
                    zzup = this.zzbur.zzug();
                } else {
                    return;
                }
            } while (zzup == this.tag);
            this.zzbut = zzup;
            return;
        }
        switch (this.tag & 7) {
            case 2:
                zzup = this.zzbur.zzup();
                zzav(zzup);
                zzup += this.zzbur.zzux();
                do {
                    list.add(Float.valueOf(this.zzbur.readFloat()));
                } while (this.zzbur.zzux() < zzup);
                return;
            case 5:
                break;
            default:
                throw zzvt.zzwo();
        }
        do {
            list.add(Float.valueOf(this.zzbur.readFloat()));
            if (!this.zzbur.zzuw()) {
                zzup = this.zzbur.zzug();
            } else {
                return;
            }
        } while (zzup == this.tag);
        this.zzbut = zzup;
    }

    public final void zzj(List<Long> list) throws IOException {
        int zzup;
        if (list instanceof zzwh) {
            zzwh com_google_android_gms_internal_measurement_zzwh = (zzwh) list;
            switch (this.tag & 7) {
                case 0:
                    break;
                case 2:
                    zzup = this.zzbur.zzup() + this.zzbur.zzux();
                    do {
                        com_google_android_gms_internal_measurement_zzwh.zzbg(this.zzbur.zzuh());
                    } while (this.zzbur.zzux() < zzup);
                    zzaw(zzup);
                    return;
                default:
                    throw zzvt.zzwo();
            }
            do {
                com_google_android_gms_internal_measurement_zzwh.zzbg(this.zzbur.zzuh());
                if (!this.zzbur.zzuw()) {
                    zzup = this.zzbur.zzug();
                } else {
                    return;
                }
            } while (zzup == this.tag);
            this.zzbut = zzup;
            return;
        }
        switch (this.tag & 7) {
            case 0:
                break;
            case 2:
                zzup = this.zzbur.zzup() + this.zzbur.zzux();
                do {
                    list.add(Long.valueOf(this.zzbur.zzuh()));
                } while (this.zzbur.zzux() < zzup);
                zzaw(zzup);
                return;
            default:
                throw zzvt.zzwo();
        }
        do {
            list.add(Long.valueOf(this.zzbur.zzuh()));
            if (!this.zzbur.zzuw()) {
                zzup = this.zzbur.zzug();
            } else {
                return;
            }
        } while (zzup == this.tag);
        this.zzbut = zzup;
    }

    public final void zzk(List<Long> list) throws IOException {
        int zzup;
        if (list instanceof zzwh) {
            zzwh com_google_android_gms_internal_measurement_zzwh = (zzwh) list;
            switch (this.tag & 7) {
                case 0:
                    break;
                case 2:
                    zzup = this.zzbur.zzup() + this.zzbur.zzux();
                    do {
                        com_google_android_gms_internal_measurement_zzwh.zzbg(this.zzbur.zzui());
                    } while (this.zzbur.zzux() < zzup);
                    zzaw(zzup);
                    return;
                default:
                    throw zzvt.zzwo();
            }
            do {
                com_google_android_gms_internal_measurement_zzwh.zzbg(this.zzbur.zzui());
                if (!this.zzbur.zzuw()) {
                    zzup = this.zzbur.zzug();
                } else {
                    return;
                }
            } while (zzup == this.tag);
            this.zzbut = zzup;
            return;
        }
        switch (this.tag & 7) {
            case 0:
                break;
            case 2:
                zzup = this.zzbur.zzup() + this.zzbur.zzux();
                do {
                    list.add(Long.valueOf(this.zzbur.zzui()));
                } while (this.zzbur.zzux() < zzup);
                zzaw(zzup);
                return;
            default:
                throw zzvt.zzwo();
        }
        do {
            list.add(Long.valueOf(this.zzbur.zzui()));
            if (!this.zzbur.zzuw()) {
                zzup = this.zzbur.zzug();
            } else {
                return;
            }
        } while (zzup == this.tag);
        this.zzbut = zzup;
    }

    public final void zzl(List<Integer> list) throws IOException {
        int zzup;
        if (list instanceof zzvn) {
            zzvn com_google_android_gms_internal_measurement_zzvn = (zzvn) list;
            switch (this.tag & 7) {
                case 0:
                    break;
                case 2:
                    zzup = this.zzbur.zzup() + this.zzbur.zzux();
                    do {
                        com_google_android_gms_internal_measurement_zzvn.zzbm(this.zzbur.zzuj());
                    } while (this.zzbur.zzux() < zzup);
                    zzaw(zzup);
                    return;
                default:
                    throw zzvt.zzwo();
            }
            do {
                com_google_android_gms_internal_measurement_zzvn.zzbm(this.zzbur.zzuj());
                if (!this.zzbur.zzuw()) {
                    zzup = this.zzbur.zzug();
                } else {
                    return;
                }
            } while (zzup == this.tag);
            this.zzbut = zzup;
            return;
        }
        switch (this.tag & 7) {
            case 0:
                break;
            case 2:
                zzup = this.zzbur.zzup() + this.zzbur.zzux();
                do {
                    list.add(Integer.valueOf(this.zzbur.zzuj()));
                } while (this.zzbur.zzux() < zzup);
                zzaw(zzup);
                return;
            default:
                throw zzvt.zzwo();
        }
        do {
            list.add(Integer.valueOf(this.zzbur.zzuj()));
            if (!this.zzbur.zzuw()) {
                zzup = this.zzbur.zzug();
            } else {
                return;
            }
        } while (zzup == this.tag);
        this.zzbut = zzup;
    }

    public final void zzm(List<Long> list) throws IOException {
        int zzup;
        if (list instanceof zzwh) {
            zzwh com_google_android_gms_internal_measurement_zzwh = (zzwh) list;
            switch (this.tag & 7) {
                case 1:
                    break;
                case 2:
                    zzup = this.zzbur.zzup();
                    zzau(zzup);
                    zzup += this.zzbur.zzux();
                    do {
                        com_google_android_gms_internal_measurement_zzwh.zzbg(this.zzbur.zzuk());
                    } while (this.zzbur.zzux() < zzup);
                    return;
                default:
                    throw zzvt.zzwo();
            }
            do {
                com_google_android_gms_internal_measurement_zzwh.zzbg(this.zzbur.zzuk());
                if (!this.zzbur.zzuw()) {
                    zzup = this.zzbur.zzug();
                } else {
                    return;
                }
            } while (zzup == this.tag);
            this.zzbut = zzup;
            return;
        }
        switch (this.tag & 7) {
            case 1:
                break;
            case 2:
                zzup = this.zzbur.zzup();
                zzau(zzup);
                zzup += this.zzbur.zzux();
                do {
                    list.add(Long.valueOf(this.zzbur.zzuk()));
                } while (this.zzbur.zzux() < zzup);
                return;
            default:
                throw zzvt.zzwo();
        }
        do {
            list.add(Long.valueOf(this.zzbur.zzuk()));
            if (!this.zzbur.zzuw()) {
                zzup = this.zzbur.zzug();
            } else {
                return;
            }
        } while (zzup == this.tag);
        this.zzbut = zzup;
    }

    public final void zzn(List<Integer> list) throws IOException {
        int zzup;
        if (list instanceof zzvn) {
            zzvn com_google_android_gms_internal_measurement_zzvn = (zzvn) list;
            switch (this.tag & 7) {
                case 2:
                    zzup = this.zzbur.zzup();
                    zzav(zzup);
                    zzup += this.zzbur.zzux();
                    do {
                        com_google_android_gms_internal_measurement_zzvn.zzbm(this.zzbur.zzul());
                    } while (this.zzbur.zzux() < zzup);
                    return;
                case 5:
                    break;
                default:
                    throw zzvt.zzwo();
            }
            do {
                com_google_android_gms_internal_measurement_zzvn.zzbm(this.zzbur.zzul());
                if (!this.zzbur.zzuw()) {
                    zzup = this.zzbur.zzug();
                } else {
                    return;
                }
            } while (zzup == this.tag);
            this.zzbut = zzup;
            return;
        }
        switch (this.tag & 7) {
            case 2:
                zzup = this.zzbur.zzup();
                zzav(zzup);
                zzup += this.zzbur.zzux();
                do {
                    list.add(Integer.valueOf(this.zzbur.zzul()));
                } while (this.zzbur.zzux() < zzup);
                return;
            case 5:
                break;
            default:
                throw zzvt.zzwo();
        }
        do {
            list.add(Integer.valueOf(this.zzbur.zzul()));
            if (!this.zzbur.zzuw()) {
                zzup = this.zzbur.zzug();
            } else {
                return;
            }
        } while (zzup == this.tag);
        this.zzbut = zzup;
    }

    public final void zzo(List<Boolean> list) throws IOException {
        int zzup;
        if (list instanceof zzub) {
            zzub com_google_android_gms_internal_measurement_zzub = (zzub) list;
            switch (this.tag & 7) {
                case 0:
                    break;
                case 2:
                    zzup = this.zzbur.zzup() + this.zzbur.zzux();
                    do {
                        com_google_android_gms_internal_measurement_zzub.addBoolean(this.zzbur.zzum());
                    } while (this.zzbur.zzux() < zzup);
                    zzaw(zzup);
                    return;
                default:
                    throw zzvt.zzwo();
            }
            do {
                com_google_android_gms_internal_measurement_zzub.addBoolean(this.zzbur.zzum());
                if (!this.zzbur.zzuw()) {
                    zzup = this.zzbur.zzug();
                } else {
                    return;
                }
            } while (zzup == this.tag);
            this.zzbut = zzup;
            return;
        }
        switch (this.tag & 7) {
            case 0:
                break;
            case 2:
                zzup = this.zzbur.zzup() + this.zzbur.zzux();
                do {
                    list.add(Boolean.valueOf(this.zzbur.zzum()));
                } while (this.zzbur.zzux() < zzup);
                zzaw(zzup);
                return;
            default:
                throw zzvt.zzwo();
        }
        do {
            list.add(Boolean.valueOf(this.zzbur.zzum()));
            if (!this.zzbur.zzuw()) {
                zzup = this.zzbur.zzug();
            } else {
                return;
            }
        } while (zzup == this.tag);
        this.zzbut = zzup;
    }

    public final void readStringList(List<String> list) throws IOException {
        zza((List) list, false);
    }

    public final void zzp(List<String> list) throws IOException {
        zza((List) list, true);
    }

    private final void zza(List<String> list, boolean z) throws IOException {
        if ((this.tag & 7) != 2) {
            throw zzvt.zzwo();
        } else if (!(list instanceof zzwc) || z) {
            do {
                list.add(z ? zzun() : readString());
                if (!this.zzbur.zzuw()) {
                    r0 = this.zzbur.zzug();
                } else {
                    return;
                }
            } while (r0 == this.tag);
            this.zzbut = r0;
        } else {
            zzwc com_google_android_gms_internal_measurement_zzwc = (zzwc) list;
            do {
                com_google_android_gms_internal_measurement_zzwc.zzc(zzuo());
                if (!this.zzbur.zzuw()) {
                    r0 = this.zzbur.zzug();
                } else {
                    return;
                }
            } while (r0 == this.tag);
            this.zzbut = r0;
        }
    }

    public final <T> void zza(List<T> list, zzxj<T> com_google_android_gms_internal_measurement_zzxj_T, zzuz com_google_android_gms_internal_measurement_zzuz) throws IOException {
        if ((this.tag & 7) != 2) {
            throw zzvt.zzwo();
        }
        int zzug;
        int i = this.tag;
        do {
            list.add(zzc(com_google_android_gms_internal_measurement_zzxj_T, com_google_android_gms_internal_measurement_zzuz));
            if (!this.zzbur.zzuw() && this.zzbut == 0) {
                zzug = this.zzbur.zzug();
            } else {
                return;
            }
        } while (zzug == i);
        this.zzbut = zzug;
    }

    public final <T> void zzb(List<T> list, zzxj<T> com_google_android_gms_internal_measurement_zzxj_T, zzuz com_google_android_gms_internal_measurement_zzuz) throws IOException {
        if ((this.tag & 7) != 3) {
            throw zzvt.zzwo();
        }
        int zzug;
        int i = this.tag;
        do {
            list.add(zzd(com_google_android_gms_internal_measurement_zzxj_T, com_google_android_gms_internal_measurement_zzuz));
            if (!this.zzbur.zzuw() && this.zzbut == 0) {
                zzug = this.zzbur.zzug();
            } else {
                return;
            }
        } while (zzug == i);
        this.zzbut = zzug;
    }

    public final void zzq(List<zzud> list) throws IOException {
        if ((this.tag & 7) != 2) {
            throw zzvt.zzwo();
        }
        int zzug;
        do {
            list.add(zzuo());
            if (!this.zzbur.zzuw()) {
                zzug = this.zzbur.zzug();
            } else {
                return;
            }
        } while (zzug == this.tag);
        this.zzbut = zzug;
    }

    public final void zzr(List<Integer> list) throws IOException {
        int zzup;
        if (list instanceof zzvn) {
            zzvn com_google_android_gms_internal_measurement_zzvn = (zzvn) list;
            switch (this.tag & 7) {
                case 0:
                    break;
                case 2:
                    zzup = this.zzbur.zzup() + this.zzbur.zzux();
                    do {
                        com_google_android_gms_internal_measurement_zzvn.zzbm(this.zzbur.zzup());
                    } while (this.zzbur.zzux() < zzup);
                    zzaw(zzup);
                    return;
                default:
                    throw zzvt.zzwo();
            }
            do {
                com_google_android_gms_internal_measurement_zzvn.zzbm(this.zzbur.zzup());
                if (!this.zzbur.zzuw()) {
                    zzup = this.zzbur.zzug();
                } else {
                    return;
                }
            } while (zzup == this.tag);
            this.zzbut = zzup;
            return;
        }
        switch (this.tag & 7) {
            case 0:
                break;
            case 2:
                zzup = this.zzbur.zzup() + this.zzbur.zzux();
                do {
                    list.add(Integer.valueOf(this.zzbur.zzup()));
                } while (this.zzbur.zzux() < zzup);
                zzaw(zzup);
                return;
            default:
                throw zzvt.zzwo();
        }
        do {
            list.add(Integer.valueOf(this.zzbur.zzup()));
            if (!this.zzbur.zzuw()) {
                zzup = this.zzbur.zzug();
            } else {
                return;
            }
        } while (zzup == this.tag);
        this.zzbut = zzup;
    }

    public final void zzs(List<Integer> list) throws IOException {
        int zzup;
        if (list instanceof zzvn) {
            zzvn com_google_android_gms_internal_measurement_zzvn = (zzvn) list;
            switch (this.tag & 7) {
                case 0:
                    break;
                case 2:
                    zzup = this.zzbur.zzup() + this.zzbur.zzux();
                    do {
                        com_google_android_gms_internal_measurement_zzvn.zzbm(this.zzbur.zzuq());
                    } while (this.zzbur.zzux() < zzup);
                    zzaw(zzup);
                    return;
                default:
                    throw zzvt.zzwo();
            }
            do {
                com_google_android_gms_internal_measurement_zzvn.zzbm(this.zzbur.zzuq());
                if (!this.zzbur.zzuw()) {
                    zzup = this.zzbur.zzug();
                } else {
                    return;
                }
            } while (zzup == this.tag);
            this.zzbut = zzup;
            return;
        }
        switch (this.tag & 7) {
            case 0:
                break;
            case 2:
                zzup = this.zzbur.zzup() + this.zzbur.zzux();
                do {
                    list.add(Integer.valueOf(this.zzbur.zzuq()));
                } while (this.zzbur.zzux() < zzup);
                zzaw(zzup);
                return;
            default:
                throw zzvt.zzwo();
        }
        do {
            list.add(Integer.valueOf(this.zzbur.zzuq()));
            if (!this.zzbur.zzuw()) {
                zzup = this.zzbur.zzug();
            } else {
                return;
            }
        } while (zzup == this.tag);
        this.zzbut = zzup;
    }

    public final void zzt(List<Integer> list) throws IOException {
        int zzup;
        if (list instanceof zzvn) {
            zzvn com_google_android_gms_internal_measurement_zzvn = (zzvn) list;
            switch (this.tag & 7) {
                case 2:
                    zzup = this.zzbur.zzup();
                    zzav(zzup);
                    zzup += this.zzbur.zzux();
                    do {
                        com_google_android_gms_internal_measurement_zzvn.zzbm(this.zzbur.zzur());
                    } while (this.zzbur.zzux() < zzup);
                    return;
                case 5:
                    break;
                default:
                    throw zzvt.zzwo();
            }
            do {
                com_google_android_gms_internal_measurement_zzvn.zzbm(this.zzbur.zzur());
                if (!this.zzbur.zzuw()) {
                    zzup = this.zzbur.zzug();
                } else {
                    return;
                }
            } while (zzup == this.tag);
            this.zzbut = zzup;
            return;
        }
        switch (this.tag & 7) {
            case 2:
                zzup = this.zzbur.zzup();
                zzav(zzup);
                zzup += this.zzbur.zzux();
                do {
                    list.add(Integer.valueOf(this.zzbur.zzur()));
                } while (this.zzbur.zzux() < zzup);
                return;
            case 5:
                break;
            default:
                throw zzvt.zzwo();
        }
        do {
            list.add(Integer.valueOf(this.zzbur.zzur()));
            if (!this.zzbur.zzuw()) {
                zzup = this.zzbur.zzug();
            } else {
                return;
            }
        } while (zzup == this.tag);
        this.zzbut = zzup;
    }

    public final void zzu(List<Long> list) throws IOException {
        int zzup;
        if (list instanceof zzwh) {
            zzwh com_google_android_gms_internal_measurement_zzwh = (zzwh) list;
            switch (this.tag & 7) {
                case 1:
                    break;
                case 2:
                    zzup = this.zzbur.zzup();
                    zzau(zzup);
                    zzup += this.zzbur.zzux();
                    do {
                        com_google_android_gms_internal_measurement_zzwh.zzbg(this.zzbur.zzus());
                    } while (this.zzbur.zzux() < zzup);
                    return;
                default:
                    throw zzvt.zzwo();
            }
            do {
                com_google_android_gms_internal_measurement_zzwh.zzbg(this.zzbur.zzus());
                if (!this.zzbur.zzuw()) {
                    zzup = this.zzbur.zzug();
                } else {
                    return;
                }
            } while (zzup == this.tag);
            this.zzbut = zzup;
            return;
        }
        switch (this.tag & 7) {
            case 1:
                break;
            case 2:
                zzup = this.zzbur.zzup();
                zzau(zzup);
                zzup += this.zzbur.zzux();
                do {
                    list.add(Long.valueOf(this.zzbur.zzus()));
                } while (this.zzbur.zzux() < zzup);
                return;
            default:
                throw zzvt.zzwo();
        }
        do {
            list.add(Long.valueOf(this.zzbur.zzus()));
            if (!this.zzbur.zzuw()) {
                zzup = this.zzbur.zzug();
            } else {
                return;
            }
        } while (zzup == this.tag);
        this.zzbut = zzup;
    }

    public final void zzv(List<Integer> list) throws IOException {
        int zzup;
        if (list instanceof zzvn) {
            zzvn com_google_android_gms_internal_measurement_zzvn = (zzvn) list;
            switch (this.tag & 7) {
                case 0:
                    break;
                case 2:
                    zzup = this.zzbur.zzup() + this.zzbur.zzux();
                    do {
                        com_google_android_gms_internal_measurement_zzvn.zzbm(this.zzbur.zzut());
                    } while (this.zzbur.zzux() < zzup);
                    zzaw(zzup);
                    return;
                default:
                    throw zzvt.zzwo();
            }
            do {
                com_google_android_gms_internal_measurement_zzvn.zzbm(this.zzbur.zzut());
                if (!this.zzbur.zzuw()) {
                    zzup = this.zzbur.zzug();
                } else {
                    return;
                }
            } while (zzup == this.tag);
            this.zzbut = zzup;
            return;
        }
        switch (this.tag & 7) {
            case 0:
                break;
            case 2:
                zzup = this.zzbur.zzup() + this.zzbur.zzux();
                do {
                    list.add(Integer.valueOf(this.zzbur.zzut()));
                } while (this.zzbur.zzux() < zzup);
                zzaw(zzup);
                return;
            default:
                throw zzvt.zzwo();
        }
        do {
            list.add(Integer.valueOf(this.zzbur.zzut()));
            if (!this.zzbur.zzuw()) {
                zzup = this.zzbur.zzug();
            } else {
                return;
            }
        } while (zzup == this.tag);
        this.zzbut = zzup;
    }

    public final void zzw(List<Long> list) throws IOException {
        int zzup;
        if (list instanceof zzwh) {
            zzwh com_google_android_gms_internal_measurement_zzwh = (zzwh) list;
            switch (this.tag & 7) {
                case 0:
                    break;
                case 2:
                    zzup = this.zzbur.zzup() + this.zzbur.zzux();
                    do {
                        com_google_android_gms_internal_measurement_zzwh.zzbg(this.zzbur.zzuu());
                    } while (this.zzbur.zzux() < zzup);
                    zzaw(zzup);
                    return;
                default:
                    throw zzvt.zzwo();
            }
            do {
                com_google_android_gms_internal_measurement_zzwh.zzbg(this.zzbur.zzuu());
                if (!this.zzbur.zzuw()) {
                    zzup = this.zzbur.zzug();
                } else {
                    return;
                }
            } while (zzup == this.tag);
            this.zzbut = zzup;
            return;
        }
        switch (this.tag & 7) {
            case 0:
                break;
            case 2:
                zzup = this.zzbur.zzup() + this.zzbur.zzux();
                do {
                    list.add(Long.valueOf(this.zzbur.zzuu()));
                } while (this.zzbur.zzux() < zzup);
                zzaw(zzup);
                return;
            default:
                throw zzvt.zzwo();
        }
        do {
            list.add(Long.valueOf(this.zzbur.zzuu()));
            if (!this.zzbur.zzuw()) {
                zzup = this.zzbur.zzug();
            } else {
                return;
            }
        } while (zzup == this.tag);
        this.zzbut = zzup;
    }

    private static void zzau(int i) throws IOException {
        if ((i & 7) != 0) {
            throw zzvt.zzwq();
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final <K, V> void zza(java.util.Map<K, V> r7, com.google.android.gms.internal.measurement.zzwm<K, V> r8, com.google.android.gms.internal.measurement.zzuz r9) throws java.io.IOException {
        /*
        r6 = this;
        r0 = 2;
        r6.zzat(r0);
        r0 = r6.zzbur;
        r0 = r0.zzup();
        r1 = r6.zzbur;
        r2 = r1.zzaq(r0);
        r1 = r8.zzcas;
        r0 = r8.zzbre;
    L_0x0014:
        r3 = r6.zzve();	 Catch:{ all -> 0x0047 }
        r4 = 2147483647; // 0x7fffffff float:NaN double:1.060997895E-314;
        if (r3 == r4) goto L_0x0064;
    L_0x001d:
        r4 = r6.zzbur;	 Catch:{ all -> 0x0047 }
        r4 = r4.zzuw();	 Catch:{ all -> 0x0047 }
        if (r4 != 0) goto L_0x0064;
    L_0x0025:
        switch(r3) {
            case 1: goto L_0x004e;
            case 2: goto L_0x0057;
            default: goto L_0x0028;
        };
    L_0x0028:
        r3 = r6.zzvf();	 Catch:{ zzvu -> 0x0037 }
        if (r3 != 0) goto L_0x0014;
    L_0x002e:
        r3 = new com.google.android.gms.internal.measurement.zzvt;	 Catch:{ zzvu -> 0x0037 }
        r4 = "Unable to parse map entry.";
        r3.<init>(r4);	 Catch:{ zzvu -> 0x0037 }
        throw r3;	 Catch:{ zzvu -> 0x0037 }
    L_0x0037:
        r3 = move-exception;
        r3 = r6.zzvf();	 Catch:{ all -> 0x0047 }
        if (r3 != 0) goto L_0x0014;
    L_0x003e:
        r0 = new com.google.android.gms.internal.measurement.zzvt;	 Catch:{ all -> 0x0047 }
        r1 = "Unable to parse map entry.";
        r0.<init>(r1);	 Catch:{ all -> 0x0047 }
        throw r0;	 Catch:{ all -> 0x0047 }
    L_0x0047:
        r0 = move-exception;
        r1 = r6.zzbur;
        r1.zzar(r2);
        throw r0;
    L_0x004e:
        r3 = r8.zzcar;	 Catch:{ zzvu -> 0x0037 }
        r4 = 0;
        r5 = 0;
        r1 = r6.zza(r3, r4, r5);	 Catch:{ zzvu -> 0x0037 }
        goto L_0x0014;
    L_0x0057:
        r3 = r8.zzcat;	 Catch:{ zzvu -> 0x0037 }
        r4 = r8.zzbre;	 Catch:{ zzvu -> 0x0037 }
        r4 = r4.getClass();	 Catch:{ zzvu -> 0x0037 }
        r0 = r6.zza(r3, r4, r9);	 Catch:{ zzvu -> 0x0037 }
        goto L_0x0014;
    L_0x0064:
        r7.put(r1, r0);	 Catch:{ all -> 0x0047 }
        r0 = r6.zzbur;
        r0.zzar(r2);
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.measurement.zzur.zza(java.util.Map, com.google.android.gms.internal.measurement.zzwm, com.google.android.gms.internal.measurement.zzuz):void");
    }

    private final Object zza(zzyq com_google_android_gms_internal_measurement_zzyq, Class<?> cls, zzuz com_google_android_gms_internal_measurement_zzuz) throws IOException {
        switch (zzus.zzbuu[com_google_android_gms_internal_measurement_zzyq.ordinal()]) {
            case 1:
                return Boolean.valueOf(zzum());
            case 2:
                return zzuo();
            case 3:
                return Double.valueOf(readDouble());
            case 4:
                return Integer.valueOf(zzuq());
            case 5:
                return Integer.valueOf(zzul());
            case 6:
                return Long.valueOf(zzuk());
            case 7:
                return Float.valueOf(readFloat());
            case 8:
                return Integer.valueOf(zzuj());
            case 9:
                return Long.valueOf(zzui());
            case 10:
                zzat(2);
                return zzc(zzxf.zzxn().zzi(cls), com_google_android_gms_internal_measurement_zzuz);
            case 11:
                return Integer.valueOf(zzur());
            case 12:
                return Long.valueOf(zzus());
            case 13:
                return Integer.valueOf(zzut());
            case 14:
                return Long.valueOf(zzuu());
            case 15:
                return zzun();
            case 16:
                return Integer.valueOf(zzup());
            case 17:
                return Long.valueOf(zzuh());
            default:
                throw new RuntimeException("unsupported field type.");
        }
    }

    private static void zzav(int i) throws IOException {
        if ((i & 3) != 0) {
            throw zzvt.zzwq();
        }
    }

    private final void zzaw(int i) throws IOException {
        if (this.zzbur.zzux() != i) {
            throw zzvt.zzwk();
        }
    }
}
