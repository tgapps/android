package com.google.android.gms.common.internal;

import android.accounts.Account;
import android.content.Context;
import android.os.Binder;
import android.os.RemoteException;
import android.util.Log;
import com.google.android.gms.common.GooglePlayServicesUtilLight;
import com.google.android.gms.common.internal.IAccountAccessor.Stub;

public class AccountAccessor extends Stub {
    private Context mContext;
    private int zzqu;
    private Account zzs;

    public static Account getAccountBinderSafe(IAccountAccessor iAccountAccessor) {
        if (iAccountAccessor != null) {
            long clearCallingIdentity = Binder.clearCallingIdentity();
            Account account;
            try {
                account = iAccountAccessor.getAccount();
                return account;
            } catch (RemoteException e) {
                account = "AccountAccessor";
                Log.w(account, "Remote account accessor probably died");
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }
        return null;
    }

    public boolean equals(Object obj) {
        return this == obj ? true : !(obj instanceof AccountAccessor) ? false : this.zzs.equals(((AccountAccessor) obj).zzs);
    }

    public Account getAccount() {
        int callingUid = Binder.getCallingUid();
        if (callingUid == this.zzqu) {
            return this.zzs;
        }
        if (GooglePlayServicesUtilLight.isGooglePlayServicesUid(this.mContext, callingUid)) {
            this.zzqu = callingUid;
            return this.zzs;
        }
        throw new SecurityException("Caller is not GooglePlayServices");
    }
}
