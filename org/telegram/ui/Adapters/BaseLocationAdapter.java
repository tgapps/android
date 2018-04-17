package org.telegram.ui.Adapters;

import android.location.Location;
import android.os.AsyncTask;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import org.json.JSONArray;
import org.json.JSONObject;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.exoplayer2.C;
import org.telegram.tgnet.TLRPC.TL_geoPoint;
import org.telegram.tgnet.TLRPC.TL_messageMediaVenue;
import org.telegram.ui.Components.RecyclerListView.SelectionAdapter;

public abstract class BaseLocationAdapter extends SelectionAdapter {
    private AsyncTask<Void, Void, JSONObject> currentTask;
    private BaseLocationAdapterDelegate delegate;
    protected ArrayList<String> iconUrls = new ArrayList();
    private Location lastSearchLocation;
    protected ArrayList<TL_messageMediaVenue> places = new ArrayList();
    private Timer searchTimer;
    protected boolean searching;

    public interface BaseLocationAdapterDelegate {
        void didLoadedSearchResult(ArrayList<TL_messageMediaVenue> arrayList);
    }

    public void destroy() {
        if (this.currentTask != null) {
            this.currentTask.cancel(true);
            this.currentTask = null;
        }
    }

    public void setDelegate(BaseLocationAdapterDelegate delegate) {
        this.delegate = delegate;
    }

    public void searchDelayed(final String query, final Location coordinate) {
        if (query != null) {
            if (query.length() != 0) {
                try {
                    if (this.searchTimer != null) {
                        this.searchTimer.cancel();
                    }
                } catch (Throwable e) {
                    FileLog.e(e);
                }
                this.searchTimer = new Timer();
                this.searchTimer.schedule(new TimerTask() {
                    public void run() {
                        try {
                            BaseLocationAdapter.this.searchTimer.cancel();
                            BaseLocationAdapter.this.searchTimer = null;
                        } catch (Throwable e) {
                            FileLog.e(e);
                        }
                        AndroidUtilities.runOnUIThread(new Runnable() {
                            public void run() {
                                BaseLocationAdapter.this.lastSearchLocation = null;
                                BaseLocationAdapter.this.searchGooglePlacesWithQuery(query, coordinate);
                            }
                        });
                    }
                }, 200, 500);
                return;
            }
        }
        this.places.clear();
        notifyDataSetChanged();
    }

    public void searchGooglePlacesWithQuery(String query, Location coordinate) {
        if (this.lastSearchLocation == null || coordinate.distanceTo(this.lastSearchLocation) >= 200.0f) {
            this.lastSearchLocation = coordinate;
            if (this.searching) {
                this.searching = false;
                if (this.currentTask != null) {
                    this.currentTask.cancel(true);
                    this.currentTask = null;
                }
            }
            try {
                String str;
                this.searching = true;
                String url = Locale.US;
                String str2 = "https://api.foursquare.com/v2/venues/search/?v=%s&locale=en&limit=25&client_id=%s&client_secret=%s&ll=%s%s";
                r5 = new Object[5];
                r5[3] = String.format(Locale.US, "%f,%f", new Object[]{Double.valueOf(coordinate.getLatitude()), Double.valueOf(coordinate.getLongitude())});
                if (query == null || query.length() <= 0) {
                    str = TtmlNode.ANONYMOUS_REGION_ID;
                } else {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("&query=");
                    stringBuilder.append(URLEncoder.encode(query, C.UTF8_NAME));
                    str = stringBuilder.toString();
                }
                r5[4] = str;
                url = String.format(url, str2, r5);
                this.currentTask = new AsyncTask<Void, Void, JSONObject>() {
                    private boolean canRetry = true;

                    private java.lang.String downloadUrlContent(java.lang.String r1) {
                        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.ui.Adapters.BaseLocationAdapter.2.downloadUrlContent(java.lang.String):java.lang.String
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:116)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:249)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:256)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
Caused by: java.lang.NullPointerException
*/
                        /*
                        r0 = this;
                        r0 = 1;
                        r1 = 0;
                        r2 = 0;
                        r3 = 0;
                        r4 = 0;
                        r5 = r4;
                        r6 = new java.net.URL;	 Catch:{ Throwable -> 0x0090 }
                        r6.<init>(r14);	 Catch:{ Throwable -> 0x0090 }
                        r7 = r6.openConnection();	 Catch:{ Throwable -> 0x0090 }
                        r5 = r7;	 Catch:{ Throwable -> 0x0090 }
                        r7 = "User-Agent";	 Catch:{ Throwable -> 0x0090 }
                        r8 = "Mozilla/5.0 (X11; Linux x86_64; rv:10.0) Gecko/20150101 Firefox/47.0 (Chrome)";	 Catch:{ Throwable -> 0x0090 }
                        r5.addRequestProperty(r7, r8);	 Catch:{ Throwable -> 0x0090 }
                        r7 = "Accept-Language";	 Catch:{ Throwable -> 0x0090 }
                        r8 = "en-us,en;q=0.5";	 Catch:{ Throwable -> 0x0090 }
                        r5.addRequestProperty(r7, r8);	 Catch:{ Throwable -> 0x0090 }
                        r7 = "Accept";	 Catch:{ Throwable -> 0x0090 }
                        r8 = "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8";	 Catch:{ Throwable -> 0x0090 }
                        r5.addRequestProperty(r7, r8);	 Catch:{ Throwable -> 0x0090 }
                        r7 = "Accept-Charset";	 Catch:{ Throwable -> 0x0090 }
                        r8 = "ISO-8859-1,utf-8;q=0.7,*;q=0.7";	 Catch:{ Throwable -> 0x0090 }
                        r5.addRequestProperty(r7, r8);	 Catch:{ Throwable -> 0x0090 }
                        r7 = 5000; // 0x1388 float:7.006E-42 double:2.4703E-320;	 Catch:{ Throwable -> 0x0090 }
                        r5.setConnectTimeout(r7);	 Catch:{ Throwable -> 0x0090 }
                        r5.setReadTimeout(r7);	 Catch:{ Throwable -> 0x0090 }
                        r7 = r5 instanceof java.net.HttpURLConnection;	 Catch:{ Throwable -> 0x0090 }
                        if (r7 == 0) goto L_0x0087;	 Catch:{ Throwable -> 0x0090 }
                    L_0x0038:
                        r7 = r5;	 Catch:{ Throwable -> 0x0090 }
                        r7 = (java.net.HttpURLConnection) r7;	 Catch:{ Throwable -> 0x0090 }
                        r8 = 1;	 Catch:{ Throwable -> 0x0090 }
                        r7.setInstanceFollowRedirects(r8);	 Catch:{ Throwable -> 0x0090 }
                        r8 = r7.getResponseCode();	 Catch:{ Throwable -> 0x0090 }
                        r9 = 302; // 0x12e float:4.23E-43 double:1.49E-321;	 Catch:{ Throwable -> 0x0090 }
                        if (r8 == r9) goto L_0x004f;	 Catch:{ Throwable -> 0x0090 }
                    L_0x0047:
                        r9 = 301; // 0x12d float:4.22E-43 double:1.487E-321;	 Catch:{ Throwable -> 0x0090 }
                        if (r8 == r9) goto L_0x004f;	 Catch:{ Throwable -> 0x0090 }
                    L_0x004b:
                        r9 = 303; // 0x12f float:4.25E-43 double:1.497E-321;	 Catch:{ Throwable -> 0x0090 }
                        if (r8 != r9) goto L_0x0087;	 Catch:{ Throwable -> 0x0090 }
                    L_0x004f:
                        r9 = "Location";	 Catch:{ Throwable -> 0x0090 }
                        r9 = r7.getHeaderField(r9);	 Catch:{ Throwable -> 0x0090 }
                        r10 = "Set-Cookie";	 Catch:{ Throwable -> 0x0090 }
                        r10 = r7.getHeaderField(r10);	 Catch:{ Throwable -> 0x0090 }
                        r11 = new java.net.URL;	 Catch:{ Throwable -> 0x0090 }
                        r11.<init>(r9);	 Catch:{ Throwable -> 0x0090 }
                        r6 = r11;	 Catch:{ Throwable -> 0x0090 }
                        r11 = r6.openConnection();	 Catch:{ Throwable -> 0x0090 }
                        r5 = r11;	 Catch:{ Throwable -> 0x0090 }
                        r11 = "Cookie";	 Catch:{ Throwable -> 0x0090 }
                        r5.setRequestProperty(r11, r10);	 Catch:{ Throwable -> 0x0090 }
                        r11 = "User-Agent";	 Catch:{ Throwable -> 0x0090 }
                        r12 = "Mozilla/5.0 (X11; Linux x86_64; rv:10.0) Gecko/20150101 Firefox/47.0 (Chrome)";	 Catch:{ Throwable -> 0x0090 }
                        r5.addRequestProperty(r11, r12);	 Catch:{ Throwable -> 0x0090 }
                        r11 = "Accept-Language";	 Catch:{ Throwable -> 0x0090 }
                        r12 = "en-us,en;q=0.5";	 Catch:{ Throwable -> 0x0090 }
                        r5.addRequestProperty(r11, r12);	 Catch:{ Throwable -> 0x0090 }
                        r11 = "Accept";	 Catch:{ Throwable -> 0x0090 }
                        r12 = "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8";	 Catch:{ Throwable -> 0x0090 }
                        r5.addRequestProperty(r11, r12);	 Catch:{ Throwable -> 0x0090 }
                        r11 = "Accept-Charset";	 Catch:{ Throwable -> 0x0090 }
                        r12 = "ISO-8859-1,utf-8;q=0.7,*;q=0.7";	 Catch:{ Throwable -> 0x0090 }
                        r5.addRequestProperty(r11, r12);	 Catch:{ Throwable -> 0x0090 }
                    L_0x0087:
                        r5.connect();	 Catch:{ Throwable -> 0x0090 }
                        r7 = r5.getInputStream();	 Catch:{ Throwable -> 0x0090 }
                        r1 = r7;
                        goto L_0x00c3;
                    L_0x0090:
                        r6 = move-exception;
                        r7 = r6 instanceof java.net.SocketTimeoutException;
                        if (r7 == 0) goto L_0x009d;
                    L_0x0095:
                        r7 = org.telegram.tgnet.ConnectionsManager.isNetworkOnline();
                        if (r7 == 0) goto L_0x00c0;
                    L_0x009b:
                        r0 = 0;
                        goto L_0x00c0;
                    L_0x009d:
                        r7 = r6 instanceof java.net.UnknownHostException;
                        if (r7 == 0) goto L_0x00a3;
                    L_0x00a1:
                        r0 = 0;
                        goto L_0x00c0;
                    L_0x00a3:
                        r7 = r6 instanceof java.net.SocketException;
                        if (r7 == 0) goto L_0x00bb;
                    L_0x00a7:
                        r7 = r6.getMessage();
                        if (r7 == 0) goto L_0x00c0;
                    L_0x00ad:
                        r7 = r6.getMessage();
                        r8 = "ECONNRESET";
                        r7 = r7.contains(r8);
                        if (r7 == 0) goto L_0x00c0;
                    L_0x00b9:
                        r0 = 0;
                        goto L_0x00c0;
                    L_0x00bb:
                        r7 = r6 instanceof java.io.FileNotFoundException;
                        if (r7 == 0) goto L_0x00c0;
                    L_0x00bf:
                        r0 = 0;
                    L_0x00c0:
                        org.telegram.messenger.FileLog.e(r6);
                    L_0x00c3:
                        if (r0 == 0) goto L_0x0125;
                    L_0x00c5:
                        if (r5 == 0) goto L_0x00e0;
                    L_0x00c7:
                        r6 = r5 instanceof java.net.HttpURLConnection;	 Catch:{ Exception -> 0x00db }
                        if (r6 == 0) goto L_0x00e0;	 Catch:{ Exception -> 0x00db }
                        r6 = r5;	 Catch:{ Exception -> 0x00db }
                        r6 = (java.net.HttpURLConnection) r6;	 Catch:{ Exception -> 0x00db }
                        r6 = r6.getResponseCode();	 Catch:{ Exception -> 0x00db }
                        r7 = 200; // 0xc8 float:2.8E-43 double:9.9E-322;
                        if (r6 == r7) goto L_0x00e0;
                        r7 = 202; // 0xca float:2.83E-43 double:1.0E-321;
                        if (r6 == r7) goto L_0x00e0;
                        goto L_0x00e0;
                    L_0x00db:
                        r6 = move-exception;
                        org.telegram.messenger.FileLog.e(r6);
                        goto L_0x00e1;
                        if (r1 == 0) goto L_0x0119;
                        r6 = 32768; // 0x8000 float:4.5918E-41 double:1.61895E-319;
                        r6 = new byte[r6];	 Catch:{ Throwable -> 0x0115 }
                        r7 = r13.isCancelled();	 Catch:{ Throwable -> 0x0115 }
                        if (r7 == 0) goto L_0x00ef;
                        goto L_0x0114;
                        r7 = r1.read(r6);	 Catch:{ Exception -> 0x010f }
                        if (r7 <= 0) goto L_0x0109;	 Catch:{ Exception -> 0x010f }
                        if (r3 != 0) goto L_0x00fd;	 Catch:{ Exception -> 0x010f }
                        r8 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x010f }
                        r8.<init>();	 Catch:{ Exception -> 0x010f }
                        r3 = r8;	 Catch:{ Exception -> 0x010f }
                        r8 = new java.lang.String;	 Catch:{ Exception -> 0x010f }
                        r9 = 0;	 Catch:{ Exception -> 0x010f }
                        r10 = "UTF-8";	 Catch:{ Exception -> 0x010f }
                        r8.<init>(r6, r9, r7, r10);	 Catch:{ Exception -> 0x010f }
                        r3.append(r8);	 Catch:{ Exception -> 0x010f }
                        goto L_0x00e8;
                        r8 = -1;
                        if (r7 != r8) goto L_0x010e;
                        r2 = 1;
                        goto L_0x0114;
                        goto L_0x0114;
                    L_0x010f:
                        r7 = move-exception;
                        org.telegram.messenger.FileLog.e(r7);	 Catch:{ Throwable -> 0x0115 }
                        goto L_0x0119;
                    L_0x0115:
                        r6 = move-exception;
                        org.telegram.messenger.FileLog.e(r6);
                        if (r1 == 0) goto L_0x0124;
                        r1.close();	 Catch:{ Throwable -> 0x011f }
                        goto L_0x0124;
                    L_0x011f:
                        r6 = move-exception;
                        org.telegram.messenger.FileLog.e(r6);
                        goto L_0x0125;
                    L_0x0125:
                        if (r2 == 0) goto L_0x012c;
                        r4 = r3.toString();
                        return r4;
                        */
                        throw new UnsupportedOperationException("Method not decompiled: org.telegram.ui.Adapters.BaseLocationAdapter.2.downloadUrlContent(java.lang.String):java.lang.String");
                    }

                    protected JSONObject doInBackground(Void... voids) {
                        String code = downloadUrlContent(url);
                        if (isCancelled()) {
                            return null;
                        }
                        try {
                            return new JSONObject(code);
                        } catch (Throwable e) {
                            FileLog.e(e);
                            return null;
                        }
                    }

                    protected void onPostExecute(JSONObject response) {
                        if (response != null) {
                            try {
                                BaseLocationAdapter.this.places.clear();
                                BaseLocationAdapter.this.iconUrls.clear();
                                JSONArray result = response.getJSONObject("response").getJSONArray("venues");
                                for (int a = 0; a < result.length(); a++) {
                                    try {
                                        JSONObject object = result.getJSONObject(a);
                                        String iconUrl = null;
                                        if (object.has("categories")) {
                                            JSONArray categories = object.getJSONArray("categories");
                                            if (categories.length() > 0) {
                                                JSONObject category = categories.getJSONObject(0);
                                                if (category.has("icon")) {
                                                    JSONObject icon = category.getJSONObject("icon");
                                                    iconUrl = String.format(Locale.US, "%s64%s", new Object[]{icon.getString("prefix"), icon.getString("suffix")});
                                                }
                                            }
                                        }
                                        BaseLocationAdapter.this.iconUrls.add(iconUrl);
                                        JSONObject location = object.getJSONObject("location");
                                        TL_messageMediaVenue venue = new TL_messageMediaVenue();
                                        venue.geo = new TL_geoPoint();
                                        venue.geo.lat = location.getDouble("lat");
                                        venue.geo._long = location.getDouble("lng");
                                        if (location.has("address")) {
                                            venue.address = location.getString("address");
                                        } else if (location.has("city")) {
                                            venue.address = location.getString("city");
                                        } else if (location.has("state")) {
                                            venue.address = location.getString("state");
                                        } else if (location.has("country")) {
                                            venue.address = location.getString("country");
                                        } else {
                                            venue.address = String.format(Locale.US, "%f,%f", new Object[]{Double.valueOf(venue.geo.lat), Double.valueOf(venue.geo._long)});
                                        }
                                        if (object.has("name")) {
                                            venue.title = object.getString("name");
                                        }
                                        venue.venue_type = TtmlNode.ANONYMOUS_REGION_ID;
                                        venue.venue_id = object.getString(TtmlNode.ATTR_ID);
                                        venue.provider = "foursquare";
                                        BaseLocationAdapter.this.places.add(venue);
                                    } catch (Throwable e) {
                                        FileLog.e(e);
                                    }
                                }
                            } catch (Throwable e2) {
                                FileLog.e(e2);
                            }
                            BaseLocationAdapter.this.searching = false;
                            BaseLocationAdapter.this.notifyDataSetChanged();
                            if (BaseLocationAdapter.this.delegate != null) {
                                BaseLocationAdapter.this.delegate.didLoadedSearchResult(BaseLocationAdapter.this.places);
                                return;
                            }
                            return;
                        }
                        BaseLocationAdapter.this.searching = false;
                        BaseLocationAdapter.this.notifyDataSetChanged();
                        if (BaseLocationAdapter.this.delegate != null) {
                            BaseLocationAdapter.this.delegate.didLoadedSearchResult(BaseLocationAdapter.this.places);
                        }
                    }
                };
                this.currentTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[]{null, null, null});
            } catch (Throwable e) {
                FileLog.e(e);
                this.searching = false;
                if (this.delegate != null) {
                    this.delegate.didLoadedSearchResult(this.places);
                }
            }
            notifyDataSetChanged();
        }
    }
}
