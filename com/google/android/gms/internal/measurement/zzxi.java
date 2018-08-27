package com.google.android.gms.internal.measurement;

import java.io.IOException;
import java.util.List;
import java.util.Map;

interface zzxi {
    int getTag();

    double readDouble() throws IOException;

    float readFloat() throws IOException;

    String readString() throws IOException;

    void readStringList(List<String> list) throws IOException;

    <T> T zza(zzxj<T> com_google_android_gms_internal_measurement_zzxj_T, zzuz com_google_android_gms_internal_measurement_zzuz) throws IOException;

    <T> void zza(List<T> list, zzxj<T> com_google_android_gms_internal_measurement_zzxj_T, zzuz com_google_android_gms_internal_measurement_zzuz) throws IOException;

    <K, V> void zza(Map<K, V> map, zzwm<K, V> com_google_android_gms_internal_measurement_zzwm_K__V, zzuz com_google_android_gms_internal_measurement_zzuz) throws IOException;

    @Deprecated
    <T> T zzb(zzxj<T> com_google_android_gms_internal_measurement_zzxj_T, zzuz com_google_android_gms_internal_measurement_zzuz) throws IOException;

    @Deprecated
    <T> void zzb(List<T> list, zzxj<T> com_google_android_gms_internal_measurement_zzxj_T, zzuz com_google_android_gms_internal_measurement_zzuz) throws IOException;

    void zzh(List<Double> list) throws IOException;

    void zzi(List<Float> list) throws IOException;

    void zzj(List<Long> list) throws IOException;

    void zzk(List<Long> list) throws IOException;

    void zzl(List<Integer> list) throws IOException;

    void zzm(List<Long> list) throws IOException;

    void zzn(List<Integer> list) throws IOException;

    void zzo(List<Boolean> list) throws IOException;

    void zzp(List<String> list) throws IOException;

    void zzq(List<zzud> list) throws IOException;

    void zzr(List<Integer> list) throws IOException;

    void zzs(List<Integer> list) throws IOException;

    void zzt(List<Integer> list) throws IOException;

    void zzu(List<Long> list) throws IOException;

    long zzuh() throws IOException;

    long zzui() throws IOException;

    int zzuj() throws IOException;

    long zzuk() throws IOException;

    int zzul() throws IOException;

    boolean zzum() throws IOException;

    String zzun() throws IOException;

    zzud zzuo() throws IOException;

    int zzup() throws IOException;

    int zzuq() throws IOException;

    int zzur() throws IOException;

    long zzus() throws IOException;

    int zzut() throws IOException;

    long zzuu() throws IOException;

    void zzv(List<Integer> list) throws IOException;

    int zzve() throws IOException;

    boolean zzvf() throws IOException;

    void zzw(List<Long> list) throws IOException;
}
