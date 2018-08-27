package com.google.android.gms.wallet.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.FrameLayout;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.GooglePlayServicesUtilLight;
import com.google.android.gms.dynamic.DeferredLifecycleHelper;
import com.google.android.gms.dynamic.FragmentWrapper;
import com.google.android.gms.dynamic.LifecycleDelegate;
import com.google.android.gms.dynamic.ObjectWrapper;
import com.google.android.gms.dynamic.OnDelegateCreatedListener;
import com.google.android.gms.internal.wallet.zzam;
import com.google.android.gms.internal.wallet.zzn;
import com.google.android.gms.internal.wallet.zzr;
import com.google.android.gms.wallet.MaskedWallet;
import com.google.android.gms.wallet.MaskedWalletRequest;
import com.google.android.gms.wallet.R;

@TargetApi(12)
public final class WalletFragment extends Fragment {
    private boolean zzfe = false;
    private WalletFragmentOptions zzfi;
    private WalletFragmentInitParams zzfj;
    private MaskedWalletRequest zzfk;
    private MaskedWallet zzfl;
    private Boolean zzfm;
    private zzb zzfr;
    private final FragmentWrapper zzfs = FragmentWrapper.wrap(this);
    private final zzc zzft = new zzc();
    private zza zzfu = new zza(this);
    private final Fragment zzfv = this;

    public interface OnStateChangedListener {
        void onStateChanged(WalletFragment walletFragment, int i, int i2, Bundle bundle);
    }

    private static class zzb implements LifecycleDelegate {
        private final zzn zzfp;

        private zzb(zzn com_google_android_gms_internal_wallet_zzn) {
            this.zzfp = com_google_android_gms_internal_wallet_zzn;
        }

        private final void initialize(WalletFragmentInitParams walletFragmentInitParams) {
            try {
                this.zzfp.initialize(walletFragmentInitParams);
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }

        private final void setEnabled(boolean z) {
            try {
                this.zzfp.setEnabled(z);
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }

        private final void updateMaskedWalletRequest(MaskedWalletRequest maskedWalletRequest) {
            try {
                this.zzfp.updateMaskedWalletRequest(maskedWalletRequest);
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }

        private final void updateMaskedWallet(MaskedWallet maskedWallet) {
            try {
                this.zzfp.updateMaskedWallet(maskedWallet);
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }

        public final void onInflate(Activity activity, Bundle bundle, Bundle bundle2) {
            try {
                this.zzfp.zza(ObjectWrapper.wrap(activity), (WalletFragmentOptions) bundle.getParcelable("extraWalletFragmentOptions"), bundle2);
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }

        public final void onCreate(Bundle bundle) {
            try {
                this.zzfp.onCreate(bundle);
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }

        public final View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
            try {
                return (View) ObjectWrapper.unwrap(this.zzfp.onCreateView(ObjectWrapper.wrap(layoutInflater), ObjectWrapper.wrap(viewGroup), bundle));
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }

        public final void onStart() {
            try {
                this.zzfp.onStart();
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }

        public final void onResume() {
            try {
                this.zzfp.onResume();
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }

        public final void onPause() {
            try {
                this.zzfp.onPause();
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }

        public final void onStop() {
            try {
                this.zzfp.onStop();
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }

        public final void onDestroy() {
        }

        public final void onLowMemory() {
        }

        public final void onSaveInstanceState(Bundle bundle) {
            try {
                this.zzfp.onSaveInstanceState(bundle);
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }

        private final void onActivityResult(int i, int i2, Intent intent) {
            try {
                this.zzfp.onActivityResult(i, i2, intent);
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }
    }

    private class zzc extends DeferredLifecycleHelper<zzb> implements OnClickListener {
        private final /* synthetic */ WalletFragment zzfy;

        private zzc(WalletFragment walletFragment) {
            this.zzfy = walletFragment;
        }

        protected final void createDelegate(OnDelegateCreatedListener<zzb> onDelegateCreatedListener) {
            Activity activity = this.zzfy.zzfv.getActivity();
            if (this.zzfy.zzfr == null && this.zzfy.zzfe && activity != null) {
                try {
                    this.zzfy.zzfr = new zzb(zzam.zza(activity, this.zzfy.zzfs, this.zzfy.zzfi, this.zzfy.zzfu));
                    this.zzfy.zzfi = null;
                    onDelegateCreatedListener.onDelegateCreated(this.zzfy.zzfr);
                    if (this.zzfy.zzfj != null) {
                        this.zzfy.zzfr.initialize(this.zzfy.zzfj);
                        this.zzfy.zzfj = null;
                    }
                    if (this.zzfy.zzfk != null) {
                        this.zzfy.zzfr.updateMaskedWalletRequest(this.zzfy.zzfk);
                        this.zzfy.zzfk = null;
                    }
                    if (this.zzfy.zzfl != null) {
                        this.zzfy.zzfr.updateMaskedWallet(this.zzfy.zzfl);
                        this.zzfy.zzfl = null;
                    }
                    if (this.zzfy.zzfm != null) {
                        this.zzfy.zzfr.setEnabled(this.zzfy.zzfm.booleanValue());
                        this.zzfy.zzfm = null;
                    }
                } catch (GooglePlayServicesNotAvailableException e) {
                }
            }
        }

        protected final void handleGooglePlayUnavailable(FrameLayout frameLayout) {
            int i = -1;
            int i2 = -2;
            View button = new Button(this.zzfy.zzfv.getActivity());
            button.setText(R.string.wallet_buy_button_place_holder);
            if (this.zzfy.zzfi != null) {
                WalletFragmentStyle fragmentStyle = this.zzfy.zzfi.getFragmentStyle();
                if (fragmentStyle != null) {
                    DisplayMetrics displayMetrics = this.zzfy.zzfv.getResources().getDisplayMetrics();
                    i = fragmentStyle.zza("buyButtonWidth", displayMetrics, -1);
                    i2 = fragmentStyle.zza("buyButtonHeight", displayMetrics, -2);
                }
            }
            button.setLayoutParams(new LayoutParams(i, i2));
            button.setOnClickListener(this);
            frameLayout.addView(button);
        }

        public final void onClick(View view) {
            Context activity = this.zzfy.zzfv.getActivity();
            GooglePlayServicesUtil.showErrorDialogFragment(GooglePlayServicesUtilLight.isGooglePlayServicesAvailable(activity, 12451000), activity, -1);
        }
    }

    static class zza extends zzr {
        private OnStateChangedListener zzfw;
        private final WalletFragment zzfx;

        zza(WalletFragment walletFragment) {
            this.zzfx = walletFragment;
        }

        public final void zza(int i, int i2, Bundle bundle) {
            if (this.zzfw != null) {
                this.zzfw.onStateChanged(this.zzfx, i, i2, bundle);
            }
        }
    }

    public static WalletFragment newInstance(WalletFragmentOptions walletFragmentOptions) {
        WalletFragment walletFragment = new WalletFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("extraWalletFragmentOptions", walletFragmentOptions);
        walletFragment.zzfv.setArguments(bundle);
        return walletFragment;
    }

    public final void initialize(WalletFragmentInitParams walletFragmentInitParams) {
        if (this.zzfr != null) {
            this.zzfr.initialize(walletFragmentInitParams);
            this.zzfj = null;
        } else if (this.zzfj == null) {
            this.zzfj = walletFragmentInitParams;
            if (this.zzfk != null) {
                Log.w("WalletFragment", "updateMaskedWalletRequest() was called before initialize()");
            }
            if (this.zzfl != null) {
                Log.w("WalletFragment", "updateMaskedWallet() was called before initialize()");
            }
        } else {
            Log.w("WalletFragment", "initialize(WalletFragmentInitParams) was called more than once. Ignoring.");
        }
    }

    public final void onInflate(Activity activity, AttributeSet attributeSet, Bundle bundle) {
        super.onInflate(activity, attributeSet, bundle);
        if (this.zzfi == null) {
            this.zzfi = WalletFragmentOptions.zza((Context) activity, attributeSet);
        }
        Bundle bundle2 = new Bundle();
        bundle2.putParcelable("attrKeyWalletFragmentOptions", this.zzfi);
        this.zzft.onInflate(activity, bundle2, bundle);
    }

    public final void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (bundle != null) {
            bundle.setClassLoader(WalletFragmentOptions.class.getClassLoader());
            WalletFragmentInitParams walletFragmentInitParams = (WalletFragmentInitParams) bundle.getParcelable("walletFragmentInitParams");
            if (walletFragmentInitParams != null) {
                if (this.zzfj != null) {
                    Log.w("WalletFragment", "initialize(WalletFragmentInitParams) was called more than once.Ignoring.");
                }
                this.zzfj = walletFragmentInitParams;
            }
            if (this.zzfk == null) {
                this.zzfk = (MaskedWalletRequest) bundle.getParcelable("maskedWalletRequest");
            }
            if (this.zzfl == null) {
                this.zzfl = (MaskedWallet) bundle.getParcelable("maskedWallet");
            }
            if (bundle.containsKey("walletFragmentOptions")) {
                this.zzfi = (WalletFragmentOptions) bundle.getParcelable("walletFragmentOptions");
            }
            if (bundle.containsKey("enabled")) {
                this.zzfm = Boolean.valueOf(bundle.getBoolean("enabled"));
            }
        } else if (this.zzfv.getArguments() != null) {
            WalletFragmentOptions walletFragmentOptions = (WalletFragmentOptions) this.zzfv.getArguments().getParcelable("extraWalletFragmentOptions");
            if (walletFragmentOptions != null) {
                walletFragmentOptions.zza(this.zzfv.getActivity());
                this.zzfi = walletFragmentOptions;
            }
        }
        this.zzfe = true;
        this.zzft.onCreate(bundle);
    }

    public final View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return this.zzft.onCreateView(layoutInflater, viewGroup, bundle);
    }

    public final void onStart() {
        super.onStart();
        this.zzft.onStart();
    }

    public final void onResume() {
        super.onResume();
        this.zzft.onResume();
        FragmentManager fragmentManager = this.zzfv.getActivity().getFragmentManager();
        Fragment findFragmentByTag = fragmentManager.findFragmentByTag("GooglePlayServicesErrorDialog");
        if (findFragmentByTag != null) {
            fragmentManager.beginTransaction().remove(findFragmentByTag).commit();
            GooglePlayServicesUtil.showErrorDialogFragment(GooglePlayServicesUtilLight.isGooglePlayServicesAvailable(this.zzfv.getActivity(), 12451000), this.zzfv.getActivity(), -1);
        }
    }

    public final void onPause() {
        super.onPause();
        this.zzft.onPause();
    }

    public final void onStop() {
        super.onStop();
        this.zzft.onStop();
    }

    public final void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.setClassLoader(WalletFragmentOptions.class.getClassLoader());
        this.zzft.onSaveInstanceState(bundle);
        if (this.zzfj != null) {
            bundle.putParcelable("walletFragmentInitParams", this.zzfj);
            this.zzfj = null;
        }
        if (this.zzfk != null) {
            bundle.putParcelable("maskedWalletRequest", this.zzfk);
            this.zzfk = null;
        }
        if (this.zzfl != null) {
            bundle.putParcelable("maskedWallet", this.zzfl);
            this.zzfl = null;
        }
        if (this.zzfi != null) {
            bundle.putParcelable("walletFragmentOptions", this.zzfi);
            this.zzfi = null;
        }
        if (this.zzfm != null) {
            bundle.putBoolean("enabled", this.zzfm.booleanValue());
            this.zzfm = null;
        }
    }

    public final void onDestroy() {
        super.onDestroy();
        this.zzfe = false;
    }

    public final void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (this.zzfr != null) {
            this.zzfr.onActivityResult(i, i2, intent);
        }
    }
}
