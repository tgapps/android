package com.google.android.search.verification.client;

import android.app.IntentService;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import com.google.android.search.verification.api.ISearchActionVerificationService;
import com.google.android.search.verification.api.ISearchActionVerificationService.Stub;

public abstract class SearchActionVerificationClientService extends IntentService {
    private static final int CONNECTION_TIMEOUT_IN_MS = 1000;
    public static final String EXTRA_INTENT = "SearchActionVerificationClientExtraIntent";
    private static final long MS_TO_NS = 1000000;
    private static final String REMOTE_SERVICE_ACTION = "com.google.android.googlequicksearchbox.SEARCH_ACTION_VERIFICATION_SERVICE";
    private static final String SEARCH_APP_PACKAGE = "com.google.android.googlequicksearchbox";
    private static final String TAG = "SAVerificationClientS";
    private static final String TESTING_APP_PACKAGE = "com.google.verificationdemo.fakeverification";
    private static final int TIME_TO_SLEEP_IN_MS = 50;
    private final boolean DBG = isTestingMode();
    private final long mConnectionTimeout;
    private ISearchActionVerificationService mIRemoteService = null;
    private SearchActionVerificationServiceConnection mSearchActionVerificationServiceConnection;
    private final Intent mServiceIntent = new Intent(REMOTE_SERVICE_ACTION).setPackage(SEARCH_APP_PACKAGE);

    class SearchActionVerificationServiceConnection implements ServiceConnection {
        SearchActionVerificationServiceConnection() {
        }

        public void onServiceConnected(ComponentName componentName, IBinder binder) {
            if (SearchActionVerificationClientService.this.DBG) {
                Log.d(SearchActionVerificationClientService.TAG, "onServiceConnected");
            }
            SearchActionVerificationClientService.this.mIRemoteService = Stub.asInterface(binder);
        }

        public void onServiceDisconnected(ComponentName componentName) {
            SearchActionVerificationClientService.this.mIRemoteService = null;
            if (SearchActionVerificationClientService.this.DBG) {
                Log.d(SearchActionVerificationClientService.TAG, "onServiceDisconnected");
            }
        }
    }

    public abstract boolean performAction(Intent intent, boolean z, Bundle bundle);

    public long getConnectionTimeout() {
        return 1000;
    }

    public boolean isTestingMode() {
        return false;
    }

    public SearchActionVerificationClientService() {
        super("SearchActionVerificationClientService");
        if (isTestingMode()) {
            this.mServiceIntent.setPackage(TESTING_APP_PACKAGE);
        }
        this.mConnectionTimeout = getConnectionTimeout();
    }

    private boolean isConnected() {
        return this.mIRemoteService != null;
    }

    protected final void onHandleIntent(Intent intent) {
        if (intent == null) {
            if (this.DBG) {
                Log.d(TAG, "Unable to verify null intent");
            }
            return;
        }
        long startTime = System.nanoTime();
        while (!isConnected() && System.nanoTime() - startTime < this.mConnectionTimeout * 1000000) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException exception) {
                if (this.DBG) {
                    String str = TAG;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Unexpected InterruptedException: ");
                    stringBuilder.append(exception);
                    Log.d(str, stringBuilder.toString());
                }
            }
        }
        String str2;
        StringBuilder stringBuilder2;
        if (!isConnected()) {
            str2 = TAG;
            stringBuilder2 = new StringBuilder();
            stringBuilder2.append("VerificationService is not connected, unable to check intent: ");
            stringBuilder2.append(intent);
            Log.e(str2, stringBuilder2.toString());
        } else if (intent.hasExtra(EXTRA_INTENT)) {
            Intent extraIntent = (Intent) intent.getParcelableExtra(EXTRA_INTENT);
            if (this.DBG) {
                logIntentWithExtras(extraIntent);
            }
            try {
                str = TAG;
                stringBuilder = new StringBuilder();
                stringBuilder.append("API version: ");
                stringBuilder.append(this.mIRemoteService.getVersion());
                Log.i(str, stringBuilder.toString());
                Bundle options = new Bundle();
                performAction(extraIntent, this.mIRemoteService.isSearchAction(extraIntent, options), options);
            } catch (RemoteException exception2) {
                String str3 = TAG;
                StringBuilder stringBuilder3 = new StringBuilder();
                stringBuilder3.append("Remote exception: ");
                stringBuilder3.append(exception2.getMessage());
                Log.e(str3, stringBuilder3.toString());
            }
        } else if (this.DBG) {
            str2 = TAG;
            stringBuilder2 = new StringBuilder();
            stringBuilder2.append("No extra, nothing to check: ");
            stringBuilder2.append(intent);
            Log.d(str2, stringBuilder2.toString());
        }
    }

    public final void onCreate() {
        if (this.DBG) {
            Log.d(TAG, "onCreate");
        }
        super.onCreate();
        this.mSearchActionVerificationServiceConnection = new SearchActionVerificationServiceConnection();
        bindService(this.mServiceIntent, this.mSearchActionVerificationServiceConnection, 1);
    }

    public final void onDestroy() {
        if (this.DBG) {
            Log.d(TAG, "onDestroy");
        }
        super.onDestroy();
        unbindService(this.mSearchActionVerificationServiceConnection);
    }

    private static void logIntentWithExtras(Intent intent) {
        Log.d(TAG, "Intent:");
        String str = TAG;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\t");
        stringBuilder.append(intent);
        Log.d(str, stringBuilder.toString());
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            Log.d(TAG, "Extras:");
            for (String key : bundle.keySet()) {
                Log.d(TAG, String.format("\t%s: %s", new Object[]{key, bundle.get(key)}));
            }
        }
    }
}
