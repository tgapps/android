package org.telegram.ui;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnShowListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.TimePicker;
import android.widget.Toast;
import java.util.Calendar;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.NotificationCenter.NotificationCenterDelegate;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.beta.R;
import org.telegram.messenger.support.widget.DefaultItemAnimator;
import org.telegram.messenger.support.widget.LinearLayoutManager;
import org.telegram.messenger.support.widget.RecyclerView.ViewHolder;
import org.telegram.messenger.time.SunDate;
import org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.BottomSheet.Builder;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.Theme.ThemeInfo;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Cells.BrightnessControlCell;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.ShadowSectionCell;
import org.telegram.ui.Cells.TextCheckCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Cells.TextSettingsCell;
import org.telegram.ui.Cells.ThemeCell;
import org.telegram.ui.Cells.ThemeTypeCell;
import org.telegram.ui.Components.EditTextBoldCursor;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.RecyclerListView.Holder;
import org.telegram.ui.Components.RecyclerListView.OnItemClickListener;
import org.telegram.ui.Components.RecyclerListView.SelectionAdapter;
import org.telegram.ui.Components.ThemeEditorView;

public class ThemeActivity extends BaseFragment implements NotificationCenterDelegate {
    public static final int THEME_TYPE_BASIC = 0;
    public static final int THEME_TYPE_NIGHT = 1;
    private int automaticBrightnessInfoRow;
    private int automaticBrightnessRow;
    private int automaticHeaderRow;
    private int currentType;
    private GpsLocationListener gpsLocationListener = new GpsLocationListener();
    private ListAdapter listAdapter;
    private RecyclerListView listView;
    private GpsLocationListener networkLocationListener = new GpsLocationListener();
    private int newThemeInfoRow;
    private int newThemeRow;
    private int nightAutomaticRow;
    private int nightDisabledRow;
    private int nightScheduledRow;
    private int nightThemeRow;
    private int nightTypeInfoRow;
    private int preferedHeaderRow;
    private boolean previousByLocation;
    private int previousUpdatedType;
    private int rowCount;
    private int scheduleFromRow;
    private int scheduleFromToInfoRow;
    private int scheduleHeaderRow;
    private int scheduleLocationInfoRow;
    private int scheduleLocationRow;
    private int scheduleToRow;
    private int scheduleUpdateLocationRow;
    private int themeEndRow;
    private int themeInfoRow;
    private int themeStartRow;
    private boolean updatingLocation;

    private class GpsLocationListener implements LocationListener {
        private GpsLocationListener() {
        }

        public void onLocationChanged(Location location) {
            if (location != null) {
                ThemeActivity.this.stopLocationUpdate();
                ThemeActivity.this.updateSunTime(location, false);
            }
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onProviderDisabled(String provider) {
        }
    }

    private class ListAdapter extends SelectionAdapter {
        private Context mContext;

        public ListAdapter(Context context) {
            this.mContext = context;
        }

        public int getItemCount() {
            return ThemeActivity.this.rowCount;
        }

        public boolean isEnabled(ViewHolder holder) {
            int type = holder.getItemViewType();
            if (type == 0 || type == 1 || type == 4) {
                return true;
            }
            return type == 7;
        }

        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view;
            switch (viewType) {
                case 0:
                    Context context = this.mContext;
                    boolean z = true;
                    if (ThemeActivity.this.currentType != 1) {
                        z = false;
                    }
                    view = new ThemeCell(context, z);
                    view.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                    if (ThemeActivity.this.currentType == 0) {
                        ((ThemeCell) view).setOnOptionsClick(new OnClickListener() {
                            public void onClick(View v) {
                                final ThemeInfo themeInfo = ((ThemeCell) v.getParent()).getCurrentThemeInfo();
                                if (ThemeActivity.this.getParentActivity() != null) {
                                    Builder builder = new Builder(ThemeActivity.this.getParentActivity());
                                    builder.setItems(themeInfo.pathToFile == null ? new CharSequence[]{LocaleController.getString("ShareFile", R.string.ShareFile)} : new CharSequence[]{LocaleController.getString("ShareFile", R.string.ShareFile), LocaleController.getString("Edit", R.string.Edit), LocaleController.getString("Delete", R.string.Delete)}, new DialogInterface.OnClickListener() {
                                        public void onClick(android.content.DialogInterface r1, int r2) {
                                            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.ui.ThemeActivity.ListAdapter.1.1.onClick(android.content.DialogInterface, int):void
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:116)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:249)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:256)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:256)
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
                                            r1 = 1;
                                            if (r9 != 0) goto L_0x0117;
                                        L_0x0004:
                                            r2 = r0;
                                            r2 = r2.pathToFile;
                                            if (r2 != 0) goto L_0x008b;
                                        L_0x000a:
                                            r2 = r0;
                                            r2 = r2.assetName;
                                            if (r2 != 0) goto L_0x008b;
                                        L_0x0010:
                                            r2 = new java.lang.StringBuilder;
                                            r2.<init>();
                                            r3 = org.telegram.ui.ActionBar.Theme.getDefaultColors();
                                            r3 = r3.entrySet();
                                            r3 = r3.iterator();
                                        L_0x0021:
                                            r4 = r3.hasNext();
                                            if (r4 == 0) goto L_0x0048;
                                        L_0x0027:
                                            r4 = r3.next();
                                            r4 = (java.util.Map.Entry) r4;
                                            r5 = r4.getKey();
                                            r5 = (java.lang.String) r5;
                                            r2.append(r5);
                                            r5 = "=";
                                            r2.append(r5);
                                            r5 = r4.getValue();
                                            r2.append(r5);
                                            r5 = "\n";
                                            r2.append(r5);
                                            goto L_0x0021;
                                        L_0x0048:
                                            r3 = new java.io.File;
                                            r4 = org.telegram.messenger.ApplicationLoader.getFilesDirFixed();
                                            r5 = "default_theme.attheme";
                                            r3.<init>(r4, r5);
                                            r4 = new java.io.FileOutputStream;	 Catch:{ Exception -> 0x006d }
                                            r4.<init>(r3);	 Catch:{ Exception -> 0x006d }
                                            r0 = r4;	 Catch:{ Exception -> 0x006d }
                                            r4 = r2.toString();	 Catch:{ Exception -> 0x006d }
                                            r4 = r4.getBytes();	 Catch:{ Exception -> 0x006d }
                                            r0.write(r4);	 Catch:{ Exception -> 0x006d }
                                            if (r0 == 0) goto L_0x007c;
                                        L_0x0067:
                                            r0.close();	 Catch:{ Exception -> 0x0077 }
                                            goto L_0x007c;
                                        L_0x006b:
                                            r1 = move-exception;
                                            goto L_0x007e;
                                        L_0x006d:
                                            r4 = move-exception;
                                            org.telegram.messenger.FileLog.e(r4);	 Catch:{ all -> 0x006b }
                                            if (r0 == 0) goto L_0x007c;
                                            r0.close();	 Catch:{ Exception -> 0x0077 }
                                            goto L_0x007c;
                                        L_0x0077:
                                            r4 = move-exception;
                                            org.telegram.messenger.FileLog.e(r4);
                                            goto L_0x007d;
                                            goto L_0x00a3;
                                            if (r0 == 0) goto L_0x008a;
                                            r0.close();	 Catch:{ Exception -> 0x0085 }
                                            goto L_0x008a;
                                        L_0x0085:
                                            r4 = move-exception;
                                            org.telegram.messenger.FileLog.e(r4);
                                            throw r1;
                                        L_0x008b:
                                            r0 = r0;
                                            r0 = r0.assetName;
                                            if (r0 == 0) goto L_0x009a;
                                            r0 = r0;
                                            r0 = r0.assetName;
                                            r3 = org.telegram.ui.ActionBar.Theme.getAssetFile(r0);
                                            goto L_0x00a3;
                                            r3 = new java.io.File;
                                            r0 = r0;
                                            r0 = r0.pathToFile;
                                            r3.<init>(r0);
                                            r0 = r3;
                                            r2 = new java.io.File;
                                            r3 = 4;
                                            r3 = org.telegram.messenger.FileLoader.getDirectory(r3);
                                            r4 = r0.getName();
                                            r2.<init>(r3, r4);
                                            r3 = org.telegram.messenger.AndroidUtilities.copyFile(r0, r2);	 Catch:{ Exception -> 0x0111 }
                                            if (r3 != 0) goto L_0x00b9;	 Catch:{ Exception -> 0x0111 }
                                            return;	 Catch:{ Exception -> 0x0111 }
                                            r3 = new android.content.Intent;	 Catch:{ Exception -> 0x0111 }
                                            r4 = "android.intent.action.SEND";	 Catch:{ Exception -> 0x0111 }
                                            r3.<init>(r4);	 Catch:{ Exception -> 0x0111 }
                                            r4 = "text/xml";	 Catch:{ Exception -> 0x0111 }
                                            r3.setType(r4);	 Catch:{ Exception -> 0x0111 }
                                            r4 = android.os.Build.VERSION.SDK_INT;	 Catch:{ Exception -> 0x0111 }
                                            r5 = 24;
                                            if (r4 < r5) goto L_0x00ef;
                                            r4 = "android.intent.extra.STREAM";	 Catch:{ Exception -> 0x00e4 }
                                            r5 = org.telegram.ui.ThemeActivity.ListAdapter.AnonymousClass1.this;	 Catch:{ Exception -> 0x00e4 }
                                            r5 = org.telegram.ui.ThemeActivity.ListAdapter.this;	 Catch:{ Exception -> 0x00e4 }
                                            r5 = org.telegram.ui.ThemeActivity.this;	 Catch:{ Exception -> 0x00e4 }
                                            r5 = r5.getParentActivity();	 Catch:{ Exception -> 0x00e4 }
                                            r6 = "org.telegram.messenger.beta.provider";	 Catch:{ Exception -> 0x00e4 }
                                            r5 = android.support.v4.content.FileProvider.getUriForFile(r5, r6, r2);	 Catch:{ Exception -> 0x00e4 }
                                            r3.putExtra(r4, r5);	 Catch:{ Exception -> 0x00e4 }
                                            r3.setFlags(r1);	 Catch:{ Exception -> 0x00e4 }
                                            goto L_0x00ee;
                                        L_0x00e4:
                                            r1 = move-exception;
                                            r4 = "android.intent.extra.STREAM";	 Catch:{ Exception -> 0x0111 }
                                            r5 = android.net.Uri.fromFile(r2);	 Catch:{ Exception -> 0x0111 }
                                            r3.putExtra(r4, r5);	 Catch:{ Exception -> 0x0111 }
                                            goto L_0x00f8;	 Catch:{ Exception -> 0x0111 }
                                            r1 = "android.intent.extra.STREAM";	 Catch:{ Exception -> 0x0111 }
                                            r4 = android.net.Uri.fromFile(r2);	 Catch:{ Exception -> 0x0111 }
                                            r3.putExtra(r1, r4);	 Catch:{ Exception -> 0x0111 }
                                            r1 = org.telegram.ui.ThemeActivity.ListAdapter.AnonymousClass1.this;	 Catch:{ Exception -> 0x0111 }
                                            r1 = org.telegram.ui.ThemeActivity.ListAdapter.this;	 Catch:{ Exception -> 0x0111 }
                                            r1 = org.telegram.ui.ThemeActivity.this;	 Catch:{ Exception -> 0x0111 }
                                            r4 = "ShareFile";	 Catch:{ Exception -> 0x0111 }
                                            r5 = 2131494383; // 0x7f0c05ef float:1.8612273E38 double:1.053098149E-314;	 Catch:{ Exception -> 0x0111 }
                                            r4 = org.telegram.messenger.LocaleController.getString(r4, r5);	 Catch:{ Exception -> 0x0111 }
                                            r4 = android.content.Intent.createChooser(r3, r4);	 Catch:{ Exception -> 0x0111 }
                                            r5 = 500; // 0x1f4 float:7.0E-43 double:2.47E-321;	 Catch:{ Exception -> 0x0111 }
                                            r1.startActivityForResult(r4, r5);	 Catch:{ Exception -> 0x0111 }
                                            goto L_0x0115;
                                        L_0x0111:
                                            r1 = move-exception;
                                            org.telegram.messenger.FileLog.e(r1);
                                            goto L_0x01ac;
                                        L_0x0117:
                                            if (r9 != r1) goto L_0x014e;
                                            r0 = org.telegram.ui.ThemeActivity.ListAdapter.AnonymousClass1.this;
                                            r0 = org.telegram.ui.ThemeActivity.ListAdapter.this;
                                            r0 = org.telegram.ui.ThemeActivity.this;
                                            r0 = r0.parentLayout;
                                            if (r0 == 0) goto L_0x01ac;
                                            r0 = r0;
                                            org.telegram.ui.ActionBar.Theme.applyTheme(r0);
                                            r0 = org.telegram.ui.ThemeActivity.ListAdapter.AnonymousClass1.this;
                                            r0 = org.telegram.ui.ThemeActivity.ListAdapter.this;
                                            r0 = org.telegram.ui.ThemeActivity.this;
                                            r0 = r0.parentLayout;
                                            r0.rebuildAllFragmentViews(r1, r1);
                                            r0 = new org.telegram.ui.Components.ThemeEditorView;
                                            r0.<init>();
                                            r1 = org.telegram.ui.ThemeActivity.ListAdapter.AnonymousClass1.this;
                                            r1 = org.telegram.ui.ThemeActivity.ListAdapter.this;
                                            r1 = org.telegram.ui.ThemeActivity.this;
                                            r1 = r1.getParentActivity();
                                            r2 = r0;
                                            r2 = r2.name;
                                            r0.show(r1, r2);
                                            goto L_0x01ac;
                                            r1 = org.telegram.ui.ThemeActivity.ListAdapter.AnonymousClass1.this;
                                            r1 = org.telegram.ui.ThemeActivity.ListAdapter.this;
                                            r1 = org.telegram.ui.ThemeActivity.this;
                                            r1 = r1.getParentActivity();
                                            if (r1 != 0) goto L_0x015b;
                                            return;
                                            r1 = new org.telegram.ui.ActionBar.AlertDialog$Builder;
                                            r2 = org.telegram.ui.ThemeActivity.ListAdapter.AnonymousClass1.this;
                                            r2 = org.telegram.ui.ThemeActivity.ListAdapter.this;
                                            r2 = org.telegram.ui.ThemeActivity.this;
                                            r2 = r2.getParentActivity();
                                            r1.<init>(r2);
                                            r2 = "DeleteThemeAlert";
                                            r3 = 2131493376; // 0x7f0c0200 float:1.861023E38 double:1.0530976514E-314;
                                            r2 = org.telegram.messenger.LocaleController.getString(r2, r3);
                                            r1.setMessage(r2);
                                            r2 = "AppName";
                                            r3 = 2131492981; // 0x7f0c0075 float:1.860943E38 double:1.0530974563E-314;
                                            r2 = org.telegram.messenger.LocaleController.getString(r2, r3);
                                            r1.setTitle(r2);
                                            r2 = "Delete";
                                            r3 = 2131493356; // 0x7f0c01ec float:1.861019E38 double:1.0530976415E-314;
                                            r2 = org.telegram.messenger.LocaleController.getString(r2, r3);
                                            r3 = new org.telegram.ui.ThemeActivity$ListAdapter$1$1$1;
                                            r3.<init>();
                                            r1.setPositiveButton(r2, r3);
                                            r2 = "Cancel";
                                            r3 = 2131493127; // 0x7f0c0107 float:1.8609725E38 double:1.0530975284E-314;
                                            r2 = org.telegram.messenger.LocaleController.getString(r2, r3);
                                            r1.setNegativeButton(r2, r0);
                                            r0 = org.telegram.ui.ThemeActivity.ListAdapter.AnonymousClass1.this;
                                            r0 = org.telegram.ui.ThemeActivity.ListAdapter.this;
                                            r0 = org.telegram.ui.ThemeActivity.this;
                                            r2 = r1.create();
                                            r0.showDialog(r2);
                                            return;
                                            */
                                            throw new UnsupportedOperationException("Method not decompiled: org.telegram.ui.ThemeActivity.ListAdapter.1.1.onClick(android.content.DialogInterface, int):void");
                                        }
                                    });
                                    ThemeActivity.this.showDialog(builder.create());
                                }
                            }
                        });
                        break;
                    }
                    break;
                case 1:
                    view = new TextSettingsCell(this.mContext);
                    view.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                    break;
                case 2:
                    view = new TextInfoPrivacyCell(this.mContext);
                    view.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, R.drawable.greydivider, Theme.key_windowBackgroundGrayShadow));
                    break;
                case 3:
                    view = new ShadowSectionCell(this.mContext);
                    view.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, R.drawable.greydivider_bottom, Theme.key_windowBackgroundGrayShadow));
                    break;
                case 4:
                    view = new ThemeTypeCell(this.mContext);
                    view.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                    break;
                case 5:
                    view = new HeaderCell(this.mContext);
                    view.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                    break;
                case 6:
                    view = new BrightnessControlCell(this.mContext) {
                        protected void didChangedValue(float value) {
                            int oldValue = (int) (Theme.autoNightBrighnessThreshold * 1120403456);
                            int newValue = (int) (value * 1120403456);
                            Theme.autoNightBrighnessThreshold = value;
                            if (oldValue != newValue) {
                                Holder holder = (Holder) ThemeActivity.this.listView.findViewHolderForAdapterPosition(ThemeActivity.this.automaticBrightnessInfoRow);
                                if (holder != null) {
                                    holder.itemView.setText(LocaleController.formatString("AutoNightBrightnessInfo", R.string.AutoNightBrightnessInfo, Integer.valueOf((int) (100.0f * Theme.autoNightBrighnessThreshold))));
                                }
                                Theme.checkAutoNightThemeConditions(true);
                            }
                        }
                    };
                    view.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                    break;
                default:
                    view = new TextCheckCell(this.mContext);
                    view.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                    break;
            }
            return new Holder(view);
        }

        public void onBindViewHolder(ViewHolder holder, int position) {
            boolean z = false;
            boolean z2 = true;
            switch (holder.getItemViewType()) {
                case 0:
                    position -= ThemeActivity.this.themeStartRow;
                    ThemeInfo themeInfo = (ThemeInfo) Theme.themes.get(position);
                    ThemeCell themeCell = (ThemeCell) holder.itemView;
                    if (position != Theme.themes.size() - 1) {
                        z = true;
                    }
                    themeCell.setTheme(themeInfo, z);
                    return;
                case 1:
                    TextSettingsCell cell = holder.itemView;
                    if (position == ThemeActivity.this.newThemeRow) {
                        cell.setText(LocaleController.getString("CreateNewTheme", R.string.CreateNewTheme), false);
                        return;
                    } else if (position == ThemeActivity.this.nightThemeRow) {
                        if (Theme.selectedAutoNightType != 0) {
                            if (Theme.getCurrentNightTheme() != null) {
                                cell.setTextAndValue(LocaleController.getString("AutoNightTheme", R.string.AutoNightTheme), Theme.getCurrentNightThemeName(), true);
                                return;
                            }
                        }
                        cell.setText(LocaleController.getString("AutoNightTheme", R.string.AutoNightTheme), true);
                        return;
                    } else if (position == ThemeActivity.this.scheduleFromRow) {
                        currentMinute = Theme.autoNightDayStartTime - ((Theme.autoNightDayStartTime / 60) * 60);
                        cell.setTextAndValue(LocaleController.getString("AutoNightFrom", R.string.AutoNightFrom), String.format("%02d:%02d", new Object[]{Integer.valueOf(currentHour), Integer.valueOf(currentMinute)}), true);
                        return;
                    } else if (position == ThemeActivity.this.scheduleToRow) {
                        currentMinute = Theme.autoNightDayEndTime - ((Theme.autoNightDayEndTime / 60) * 60);
                        cell.setTextAndValue(LocaleController.getString("AutoNightTo", R.string.AutoNightTo), String.format("%02d:%02d", new Object[]{Integer.valueOf(currentHour), Integer.valueOf(currentMinute)}), false);
                        return;
                    } else if (position == ThemeActivity.this.scheduleUpdateLocationRow) {
                        cell.setTextAndValue(LocaleController.getString("AutoNightUpdateLocation", R.string.AutoNightUpdateLocation), Theme.autoNightCityName, false);
                        return;
                    } else {
                        return;
                    }
                case 2:
                    TextInfoPrivacyCell cell2 = holder.itemView;
                    if (position == ThemeActivity.this.newThemeInfoRow) {
                        cell2.setText(LocaleController.getString("CreateNewThemeInfo", R.string.CreateNewThemeInfo));
                        return;
                    } else if (position == ThemeActivity.this.automaticBrightnessInfoRow) {
                        cell2.setText(LocaleController.formatString("AutoNightBrightnessInfo", R.string.AutoNightBrightnessInfo, Integer.valueOf((int) (100.0f * Theme.autoNightBrighnessThreshold))));
                        return;
                    } else if (position == ThemeActivity.this.scheduleLocationInfoRow) {
                        cell2.setText(ThemeActivity.this.getLocationSunString());
                        return;
                    } else {
                        return;
                    }
                case 4:
                    ThemeTypeCell typeCell = holder.itemView;
                    String string;
                    if (position == ThemeActivity.this.nightDisabledRow) {
                        string = LocaleController.getString("AutoNightDisabled", R.string.AutoNightDisabled);
                        if (Theme.selectedAutoNightType == 0) {
                            z = true;
                        }
                        typeCell.setValue(string, z, true);
                        return;
                    } else if (position == ThemeActivity.this.nightScheduledRow) {
                        string = LocaleController.getString("AutoNightScheduled", R.string.AutoNightScheduled);
                        if (Theme.selectedAutoNightType == 1) {
                            z = true;
                        }
                        typeCell.setValue(string, z, true);
                        return;
                    } else if (position == ThemeActivity.this.nightAutomaticRow) {
                        String string2 = LocaleController.getString("AutoNightAutomatic", R.string.AutoNightAutomatic);
                        if (Theme.selectedAutoNightType != 2) {
                            z2 = false;
                        }
                        typeCell.setValue(string2, z2, false);
                        return;
                    } else {
                        return;
                    }
                case 5:
                    HeaderCell headerCell = holder.itemView;
                    if (position == ThemeActivity.this.scheduleHeaderRow) {
                        headerCell.setText(LocaleController.getString("AutoNightSchedule", R.string.AutoNightSchedule));
                        return;
                    } else if (position == ThemeActivity.this.automaticHeaderRow) {
                        headerCell.setText(LocaleController.getString("AutoNightBrightness", R.string.AutoNightBrightness));
                        return;
                    } else if (position == ThemeActivity.this.preferedHeaderRow) {
                        headerCell.setText(LocaleController.getString("AutoNightPreferred", R.string.AutoNightPreferred));
                        return;
                    } else {
                        return;
                    }
                case 6:
                    holder.itemView.setProgress(Theme.autoNightBrighnessThreshold);
                    return;
                case 7:
                    TextCheckCell textCheckCell = holder.itemView;
                    if (position == ThemeActivity.this.scheduleLocationRow) {
                        textCheckCell.setTextAndCheck(LocaleController.getString("AutoNightLocation", R.string.AutoNightLocation), Theme.autoNightScheduleByLocation, true);
                        return;
                    }
                    return;
                default:
                    return;
            }
        }

        public void onViewAttachedToWindow(ViewHolder holder) {
            int type = holder.getItemViewType();
            if (type == 4) {
                ((ThemeTypeCell) holder.itemView).setTypeChecked(holder.getAdapterPosition() == Theme.selectedAutoNightType);
            } else if (type == 0) {
                ((ThemeCell) holder.itemView).updateCurrentThemeCheck();
            }
            if (type != 2 && type != 3) {
                holder.itemView.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
            }
        }

        public int getItemViewType(int i) {
            if (!(i == ThemeActivity.this.newThemeRow || i == ThemeActivity.this.nightThemeRow || i == ThemeActivity.this.scheduleFromRow || i == ThemeActivity.this.scheduleToRow)) {
                if (i != ThemeActivity.this.scheduleUpdateLocationRow) {
                    if (!(i == ThemeActivity.this.newThemeInfoRow || i == ThemeActivity.this.automaticBrightnessInfoRow)) {
                        if (i != ThemeActivity.this.scheduleLocationInfoRow) {
                            if (!(i == ThemeActivity.this.themeInfoRow || i == ThemeActivity.this.nightTypeInfoRow)) {
                                if (i != ThemeActivity.this.scheduleFromToInfoRow) {
                                    if (!(i == ThemeActivity.this.nightDisabledRow || i == ThemeActivity.this.nightScheduledRow)) {
                                        if (i != ThemeActivity.this.nightAutomaticRow) {
                                            if (!(i == ThemeActivity.this.scheduleHeaderRow || i == ThemeActivity.this.automaticHeaderRow)) {
                                                if (i != ThemeActivity.this.preferedHeaderRow) {
                                                    if (i == ThemeActivity.this.automaticBrightnessRow) {
                                                        return 6;
                                                    }
                                                    if (i == ThemeActivity.this.scheduleLocationRow) {
                                                        return 7;
                                                    }
                                                    return 0;
                                                }
                                            }
                                            return 5;
                                        }
                                    }
                                    return 4;
                                }
                            }
                            return 3;
                        }
                    }
                    return 2;
                }
            }
            return 1;
        }
    }

    public ThemeActivity(int type) {
        this.currentType = type;
        updateRows();
    }

    private void updateRows() {
        int i;
        int oldRowCount = this.rowCount;
        this.rowCount = 0;
        this.scheduleLocationRow = -1;
        this.scheduleUpdateLocationRow = -1;
        this.scheduleLocationInfoRow = -1;
        this.nightDisabledRow = -1;
        this.nightScheduledRow = -1;
        this.nightAutomaticRow = -1;
        this.nightTypeInfoRow = -1;
        this.scheduleHeaderRow = -1;
        this.nightThemeRow = -1;
        this.newThemeRow = -1;
        this.newThemeInfoRow = -1;
        this.scheduleFromRow = -1;
        this.scheduleToRow = -1;
        this.scheduleFromToInfoRow = -1;
        this.themeStartRow = -1;
        this.themeEndRow = -1;
        this.themeInfoRow = -1;
        this.preferedHeaderRow = -1;
        this.automaticHeaderRow = -1;
        this.automaticBrightnessRow = -1;
        this.automaticBrightnessInfoRow = -1;
        int i2 = 2;
        if (this.currentType == 0) {
            i = this.rowCount;
            this.rowCount = i + 1;
            this.nightThemeRow = i;
            i = this.rowCount;
            this.rowCount = i + 1;
            this.newThemeRow = i;
            i = this.rowCount;
            this.rowCount = i + 1;
            this.newThemeInfoRow = i;
            this.themeStartRow = this.rowCount;
            this.rowCount += Theme.themes.size();
            this.themeEndRow = this.rowCount;
            i = this.rowCount;
            this.rowCount = i + 1;
            this.themeInfoRow = i;
        } else {
            i = this.rowCount;
            this.rowCount = i + 1;
            this.nightDisabledRow = i;
            i = this.rowCount;
            this.rowCount = i + 1;
            this.nightScheduledRow = i;
            i = this.rowCount;
            this.rowCount = i + 1;
            this.nightAutomaticRow = i;
            i = this.rowCount;
            this.rowCount = i + 1;
            this.nightTypeInfoRow = i;
            if (Theme.selectedAutoNightType == 1) {
                i = this.rowCount;
                this.rowCount = i + 1;
                this.scheduleHeaderRow = i;
                i = this.rowCount;
                this.rowCount = i + 1;
                this.scheduleLocationRow = i;
                if (Theme.autoNightScheduleByLocation) {
                    i = this.rowCount;
                    this.rowCount = i + 1;
                    this.scheduleUpdateLocationRow = i;
                    i = this.rowCount;
                    this.rowCount = i + 1;
                    this.scheduleLocationInfoRow = i;
                } else {
                    i = this.rowCount;
                    this.rowCount = i + 1;
                    this.scheduleFromRow = i;
                    i = this.rowCount;
                    this.rowCount = i + 1;
                    this.scheduleToRow = i;
                    i = this.rowCount;
                    this.rowCount = i + 1;
                    this.scheduleFromToInfoRow = i;
                }
            } else if (Theme.selectedAutoNightType == 2) {
                i = this.rowCount;
                this.rowCount = i + 1;
                this.automaticHeaderRow = i;
                i = this.rowCount;
                this.rowCount = i + 1;
                this.automaticBrightnessRow = i;
                i = this.rowCount;
                this.rowCount = i + 1;
                this.automaticBrightnessInfoRow = i;
            }
            if (Theme.selectedAutoNightType != 0) {
                i = this.rowCount;
                this.rowCount = i + 1;
                this.preferedHeaderRow = i;
                this.themeStartRow = this.rowCount;
                this.rowCount += Theme.themes.size();
                this.themeEndRow = this.rowCount;
                i = this.rowCount;
                this.rowCount = i + 1;
                this.themeInfoRow = i;
            }
        }
        if (this.listAdapter != null) {
            if (this.currentType != 0) {
                if (this.previousUpdatedType != -1) {
                    int start = this.nightTypeInfoRow + 1;
                    ListAdapter listAdapter;
                    if (this.previousUpdatedType != Theme.selectedAutoNightType) {
                        i = 0;
                        while (i < 3) {
                            Holder holder = (Holder) this.listView.findViewHolderForAdapterPosition(i);
                            if (holder != null) {
                                ((ThemeTypeCell) holder.itemView).setTypeChecked(i == Theme.selectedAutoNightType);
                            }
                            i++;
                        }
                        if (Theme.selectedAutoNightType == 0) {
                            this.listAdapter.notifyItemRangeRemoved(start, oldRowCount - start);
                        } else {
                            i = 5;
                            if (Theme.selectedAutoNightType == 1) {
                                if (this.previousUpdatedType == 0) {
                                    this.listAdapter.notifyItemRangeInserted(start, this.rowCount - start);
                                } else if (this.previousUpdatedType == 2) {
                                    this.listAdapter.notifyItemRangeRemoved(start, 3);
                                    listAdapter = this.listAdapter;
                                    if (Theme.autoNightScheduleByLocation) {
                                        i = 4;
                                    }
                                    listAdapter.notifyItemRangeInserted(start, i);
                                }
                            } else if (Theme.selectedAutoNightType == 2) {
                                if (this.previousUpdatedType == 0) {
                                    this.listAdapter.notifyItemRangeInserted(start, this.rowCount - start);
                                } else if (this.previousUpdatedType == 1) {
                                    listAdapter = this.listAdapter;
                                    if (Theme.autoNightScheduleByLocation) {
                                        i = 4;
                                    }
                                    listAdapter.notifyItemRangeRemoved(start, i);
                                    this.listAdapter.notifyItemRangeInserted(start, 3);
                                }
                            }
                        }
                    } else if (this.previousByLocation != Theme.autoNightScheduleByLocation) {
                        this.listAdapter.notifyItemRangeRemoved(start + 2, Theme.autoNightScheduleByLocation ? 3 : 2);
                        listAdapter = this.listAdapter;
                        i = start + 2;
                        if (!Theme.autoNightScheduleByLocation) {
                            i2 = 3;
                        }
                        listAdapter.notifyItemRangeInserted(i, i2);
                    }
                }
            }
            this.listAdapter.notifyDataSetChanged();
        }
        if (this.currentType == 1) {
            this.previousByLocation = Theme.autoNightScheduleByLocation;
            this.previousUpdatedType = Theme.selectedAutoNightType;
        }
    }

    public boolean onFragmentCreate() {
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.locationPermissionGranted);
        return super.onFragmentCreate();
    }

    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        stopLocationUpdate();
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.locationPermissionGranted);
        Theme.saveAutoNightThemeConfig();
    }

    public void didReceivedNotification(int id, int account, Object... args) {
        if (id == NotificationCenter.locationPermissionGranted) {
            updateSunTime(null, true);
        }
    }

    public View createView(Context context) {
        this.actionBar.setBackButtonImage(R.drawable.ic_ab_back);
        this.actionBar.setAllowOverlayTitle(false);
        if (this.currentType == 0) {
            this.actionBar.setTitle(LocaleController.getString("Theme", R.string.Theme));
        } else {
            this.actionBar.setTitle(LocaleController.getString("AutoNightTheme", R.string.AutoNightTheme));
        }
        this.actionBar.setActionBarMenuOnItemClick(new ActionBarMenuOnItemClick() {
            public void onItemClick(int id) {
                if (id == -1) {
                    ThemeActivity.this.finishFragment();
                }
            }
        });
        this.listAdapter = new ListAdapter(context);
        FrameLayout frameLayout = new FrameLayout(context);
        frameLayout.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundGray));
        this.fragmentView = frameLayout;
        this.listView = new RecyclerListView(context);
        this.listView.setLayoutManager(new LinearLayoutManager(context, 1, false));
        this.listView.setVerticalScrollBarEnabled(false);
        this.listView.setAdapter(this.listAdapter);
        ((DefaultItemAnimator) this.listView.getItemAnimator()).setDelayAnimations(false);
        frameLayout.addView(this.listView, LayoutHelper.createFrame(-1, -1.0f));
        this.listView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(View view, int position) {
                final int i = position;
                int a = 0;
                if (i == ThemeActivity.this.newThemeRow) {
                    if (ThemeActivity.this.getParentActivity() != null) {
                        final EditTextBoldCursor editText = new EditTextBoldCursor(ThemeActivity.this.getParentActivity());
                        editText.setBackgroundDrawable(Theme.createEditTextDrawable(ThemeActivity.this.getParentActivity(), true));
                        AlertDialog.Builder builder = new AlertDialog.Builder(ThemeActivity.this.getParentActivity());
                        builder.setTitle(LocaleController.getString("NewTheme", R.string.NewTheme));
                        builder.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null);
                        builder.setPositiveButton(LocaleController.getString("OK", R.string.OK), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        LinearLayout linearLayout = new LinearLayout(ThemeActivity.this.getParentActivity());
                        linearLayout.setOrientation(1);
                        builder.setView(linearLayout);
                        TextView message = new TextView(ThemeActivity.this.getParentActivity());
                        message.setText(LocaleController.formatString("EnterThemeName", R.string.EnterThemeName, new Object[0]));
                        message.setTextSize(16.0f);
                        message.setPadding(AndroidUtilities.dp(23.0f), AndroidUtilities.dp(12.0f), AndroidUtilities.dp(23.0f), AndroidUtilities.dp(6.0f));
                        message.setTextColor(Theme.getColor(Theme.key_dialogTextBlack));
                        linearLayout.addView(message, LayoutHelper.createLinear(-1, -2));
                        editText.setTextSize(1, 16.0f);
                        editText.setTextColor(Theme.getColor(Theme.key_dialogTextBlack));
                        editText.setMaxLines(1);
                        editText.setLines(1);
                        editText.setInputType(16385);
                        editText.setGravity(51);
                        editText.setSingleLine(true);
                        editText.setImeOptions(6);
                        editText.setCursorColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText));
                        editText.setCursorSize(AndroidUtilities.dp(20.0f));
                        editText.setCursorWidth(1.5f);
                        editText.setPadding(0, AndroidUtilities.dp(4.0f), 0, 0);
                        linearLayout.addView(editText, LayoutHelper.createLinear(-1, 36, 51, 24, 6, 24, 0));
                        editText.setOnEditorActionListener(new OnEditorActionListener() {
                            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                                AndroidUtilities.hideKeyboard(textView);
                                return false;
                            }
                        });
                        final AlertDialog alertDialog = builder.create();
                        alertDialog.setOnShowListener(new OnShowListener() {
                            public void onShow(DialogInterface dialog) {
                                AndroidUtilities.runOnUIThread(new Runnable() {
                                    public void run() {
                                        editText.requestFocus();
                                        AndroidUtilities.showKeyboard(editText);
                                    }
                                });
                            }
                        });
                        ThemeActivity.this.showDialog(alertDialog);
                        alertDialog.getButton(-1).setOnClickListener(new OnClickListener() {
                            public void onClick(View v) {
                                if (editText.length() == 0) {
                                    Vibrator vibrator = (Vibrator) ApplicationLoader.applicationContext.getSystemService("vibrator");
                                    if (vibrator != null) {
                                        vibrator.vibrate(200);
                                    }
                                    AndroidUtilities.shakeView(editText, 2.0f, 0);
                                    return;
                                }
                                ThemeEditorView themeEditorView = new ThemeEditorView();
                                String name = new StringBuilder();
                                name.append(editText.getText().toString());
                                name.append(".attheme");
                                name = name.toString();
                                themeEditorView.show(ThemeActivity.this.getParentActivity(), name);
                                Theme.saveCurrentTheme(name, true);
                                ThemeActivity.this.updateRows();
                                alertDialog.dismiss();
                                SharedPreferences preferences = MessagesController.getGlobalMainSettings();
                                if (!preferences.getBoolean("themehint", false)) {
                                    preferences.edit().putBoolean("themehint", true).commit();
                                    try {
                                        Toast.makeText(ThemeActivity.this.getParentActivity(), LocaleController.getString("CreateNewThemeHelp", R.string.CreateNewThemeHelp), 1).show();
                                    } catch (Throwable e) {
                                        FileLog.e(e);
                                    }
                                }
                            }
                        });
                    }
                } else if (i >= ThemeActivity.this.themeStartRow && i < ThemeActivity.this.themeEndRow) {
                    p = i - ThemeActivity.this.themeStartRow;
                    if (p >= 0 && p < Theme.themes.size()) {
                        ThemeInfo themeInfo = (ThemeInfo) Theme.themes.get(p);
                        if (ThemeActivity.this.currentType == 0) {
                            Theme.applyTheme(themeInfo);
                            if (ThemeActivity.this.parentLayout != null) {
                                ThemeActivity.this.parentLayout.rebuildAllFragmentViews(false, false);
                            }
                            ThemeActivity.this.finishFragment();
                        } else {
                            Theme.setCurrentNightTheme(themeInfo);
                            int count = ThemeActivity.this.listView.getChildCount();
                            while (a < count) {
                                View child = ThemeActivity.this.listView.getChildAt(a);
                                if (child instanceof ThemeCell) {
                                    ((ThemeCell) child).updateCurrentThemeCheck();
                                }
                                a++;
                            }
                        }
                    }
                } else if (i == ThemeActivity.this.nightThemeRow) {
                    ThemeActivity.this.presentFragment(new ThemeActivity(1));
                } else if (i == ThemeActivity.this.nightDisabledRow) {
                    Theme.selectedAutoNightType = 0;
                    ThemeActivity.this.updateRows();
                    Theme.checkAutoNightThemeConditions();
                } else if (i == ThemeActivity.this.nightScheduledRow) {
                    Theme.selectedAutoNightType = 1;
                    if (Theme.autoNightScheduleByLocation) {
                        ThemeActivity.this.updateSunTime(null, true);
                    }
                    ThemeActivity.this.updateRows();
                    Theme.checkAutoNightThemeConditions();
                } else if (i == ThemeActivity.this.nightAutomaticRow) {
                    Theme.selectedAutoNightType = 2;
                    ThemeActivity.this.updateRows();
                    Theme.checkAutoNightThemeConditions();
                } else if (i == ThemeActivity.this.scheduleLocationRow) {
                    Theme.autoNightScheduleByLocation ^= true;
                    ((TextCheckCell) view).setChecked(Theme.autoNightScheduleByLocation);
                    ThemeActivity.this.updateRows();
                    if (Theme.autoNightScheduleByLocation) {
                        ThemeActivity.this.updateSunTime(null, true);
                    }
                    Theme.checkAutoNightThemeConditions();
                } else {
                    if (i != ThemeActivity.this.scheduleFromRow) {
                        if (i != ThemeActivity.this.scheduleToRow) {
                            if (i == ThemeActivity.this.scheduleUpdateLocationRow) {
                                ThemeActivity.this.updateSunTime(null, true);
                            }
                        }
                    }
                    if (ThemeActivity.this.getParentActivity() != null) {
                        int i2;
                        if (i == ThemeActivity.this.scheduleFromRow) {
                            p = Theme.autoNightDayStartTime / 60;
                            i2 = Theme.autoNightDayStartTime - (p * 60);
                        } else {
                            p = Theme.autoNightDayEndTime / 60;
                            i2 = Theme.autoNightDayEndTime - (p * 60);
                        }
                        int currentMinute = i2;
                        final TextSettingsCell cell = (TextSettingsCell) view;
                        ThemeActivity.this.showDialog(new TimePickerDialog(ThemeActivity.this.getParentActivity(), new OnTimeSetListener() {
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                int time = (hourOfDay * 60) + minute;
                                if (i == ThemeActivity.this.scheduleFromRow) {
                                    Theme.autoNightDayStartTime = time;
                                    cell.setTextAndValue(LocaleController.getString("AutoNightFrom", R.string.AutoNightFrom), String.format("%02d:%02d", new Object[]{Integer.valueOf(hourOfDay), Integer.valueOf(minute)}), true);
                                    return;
                                }
                                Theme.autoNightDayEndTime = time;
                                cell.setTextAndValue(LocaleController.getString("AutoNightTo", R.string.AutoNightTo), String.format("%02d:%02d", new Object[]{Integer.valueOf(hourOfDay), Integer.valueOf(minute)}), true);
                            }
                        }, p, currentMinute, true));
                    }
                }
            }
        });
        return this.fragmentView;
    }

    public void onResume() {
        super.onResume();
        if (this.listAdapter != null) {
            this.listAdapter.notifyDataSetChanged();
        }
    }

    private void updateSunTime(Location lastKnownLocation, boolean forceUpdate) {
        LocationManager locationManager = (LocationManager) ApplicationLoader.applicationContext.getSystemService("location");
        if (VERSION.SDK_INT >= 23) {
            Activity activity = getParentActivity();
            if (!(activity == null || activity.checkSelfPermission("android.permission.ACCESS_COARSE_LOCATION") == 0)) {
                activity.requestPermissions(new String[]{"android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"}, 2);
                return;
            }
        }
        if (getParentActivity() != null) {
            if (getParentActivity().getPackageManager().hasSystemFeature("android.hardware.location.gps")) {
                try {
                    if (!((LocationManager) ApplicationLoader.applicationContext.getSystemService("location")).isProviderEnabled("gps")) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
                        builder.setTitle(LocaleController.getString("AppName", R.string.AppName));
                        builder.setMessage(LocaleController.getString("GpsDisabledAlert", R.string.GpsDisabledAlert));
                        builder.setPositiveButton(LocaleController.getString("ConnectingToProxyEnable", R.string.ConnectingToProxyEnable), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                if (ThemeActivity.this.getParentActivity() != null) {
                                    try {
                                        ThemeActivity.this.getParentActivity().startActivity(new Intent("android.settings.LOCATION_SOURCE_SETTINGS"));
                                    } catch (Exception e) {
                                    }
                                }
                            }
                        });
                        builder.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null);
                        showDialog(builder.create());
                        return;
                    }
                } catch (Throwable e) {
                    FileLog.e(e);
                }
            } else {
                return;
            }
        }
        try {
            lastKnownLocation = locationManager.getLastKnownLocation("gps");
            if (lastKnownLocation == null) {
                lastKnownLocation = locationManager.getLastKnownLocation("network");
            } else if (lastKnownLocation == null) {
                lastKnownLocation = locationManager.getLastKnownLocation("passive");
            }
        } catch (Throwable e2) {
            FileLog.e(e2);
        }
        if (lastKnownLocation == null || forceUpdate) {
            startLocationUpdate();
            if (lastKnownLocation == null) {
                return;
            }
        }
        Theme.autoNightLocationLatitude = lastKnownLocation.getLatitude();
        Theme.autoNightLocationLongitude = lastKnownLocation.getLongitude();
        int[] time = SunDate.calculateSunriseSunset(Theme.autoNightLocationLatitude, Theme.autoNightLocationLongitude);
        Theme.autoNightSunriseTime = time[0];
        Theme.autoNightSunsetTime = time[1];
        Theme.autoNightCityName = null;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        Theme.autoNightLastSunCheckDay = calendar.get(5);
        Utilities.globalQueue.postRunnable(new Runnable() {

            class AnonymousClass1 implements Runnable {
                final /* synthetic */ String val$nameFinal;

                AnonymousClass1(String str) {
                    this.val$nameFinal = str;
                }

                public void run() {
                    Theme.autoNightCityName = this.val$nameFinal;
                    if (Theme.autoNightCityName == null) {
                        Theme.autoNightCityName = String.format("(%.06f, %.06f)", new Object[]{Double.valueOf(Theme.autoNightLocationLatitude), Double.valueOf(Theme.autoNightLocationLongitude)});
                    }
                    Theme.saveAutoNightThemeConfig();
                    if (ThemeActivity.this.listView != null) {
                        Holder holder = (Holder) ThemeActivity.this.listView.findViewHolderForAdapterPosition(ThemeActivity.this.scheduleUpdateLocationRow);
                        if (holder != null && (holder.itemView instanceof TextSettingsCell)) {
                            ((TextSettingsCell) holder.itemView).setTextAndValue(LocaleController.getString("AutoNightUpdateLocation", R.string.AutoNightUpdateLocation), Theme.autoNightCityName, false);
                        }
                    }
                }
            }

            public void run() {
                /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.ui.ThemeActivity.4.run():void
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
                r1 = new android.location.Geocoder;	 Catch:{ Exception -> 0x002a }
                r2 = org.telegram.messenger.ApplicationLoader.applicationContext;	 Catch:{ Exception -> 0x002a }
                r3 = java.util.Locale.getDefault();	 Catch:{ Exception -> 0x002a }
                r1.<init>(r2, r3);	 Catch:{ Exception -> 0x002a }
                r2 = org.telegram.ui.ActionBar.Theme.autoNightLocationLatitude;	 Catch:{ Exception -> 0x002a }
                r4 = org.telegram.ui.ActionBar.Theme.autoNightLocationLongitude;	 Catch:{ Exception -> 0x002a }
                r6 = 1;	 Catch:{ Exception -> 0x002a }
                r2 = r1.getFromLocation(r2, r4, r6);	 Catch:{ Exception -> 0x002a }
                r3 = r2.size();	 Catch:{ Exception -> 0x002a }
                if (r3 <= 0) goto L_0x0028;	 Catch:{ Exception -> 0x002a }
            L_0x001b:
                r3 = 0;	 Catch:{ Exception -> 0x002a }
                r3 = r2.get(r3);	 Catch:{ Exception -> 0x002a }
                r3 = (android.location.Address) r3;	 Catch:{ Exception -> 0x002a }
                r3 = r3.getLocality();	 Catch:{ Exception -> 0x002a }
                r0 = r3;
                goto L_0x0029;
                goto L_0x002c;
            L_0x002a:
                r1 = move-exception;
                r1 = r0;
                r2 = new org.telegram.ui.ThemeActivity$4$1;
                r2.<init>(r1);
                org.telegram.messenger.AndroidUtilities.runOnUIThread(r2);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: org.telegram.ui.ThemeActivity.4.run():void");
            }
        });
        Holder holder = (Holder) this.listView.findViewHolderForAdapterPosition(this.scheduleLocationInfoRow);
        if (holder != null && (holder.itemView instanceof TextInfoPrivacyCell)) {
            ((TextInfoPrivacyCell) holder.itemView).setText(getLocationSunString());
        }
        if (Theme.autoNightScheduleByLocation && Theme.selectedAutoNightType == 1) {
            Theme.checkAutoNightThemeConditions();
        }
    }

    private void startLocationUpdate() {
        if (!this.updatingLocation) {
            this.updatingLocation = true;
            LocationManager locationManager = (LocationManager) ApplicationLoader.applicationContext.getSystemService("location");
            try {
                locationManager.requestLocationUpdates("gps", 1, 0.0f, this.gpsLocationListener);
            } catch (Throwable e) {
                FileLog.e(e);
            }
            try {
                locationManager.requestLocationUpdates("network", 1, 0.0f, this.networkLocationListener);
            } catch (Throwable e2) {
                FileLog.e(e2);
            }
        }
    }

    private void stopLocationUpdate() {
        this.updatingLocation = false;
        LocationManager locationManager = (LocationManager) ApplicationLoader.applicationContext.getSystemService("location");
        locationManager.removeUpdates(this.gpsLocationListener);
        locationManager.removeUpdates(this.networkLocationListener);
    }

    private void showPermissionAlert(boolean byButton) {
        if (getParentActivity() != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
            builder.setTitle(LocaleController.getString("AppName", R.string.AppName));
            if (byButton) {
                builder.setMessage(LocaleController.getString("PermissionNoLocationPosition", R.string.PermissionNoLocationPosition));
            } else {
                builder.setMessage(LocaleController.getString("PermissionNoLocation", R.string.PermissionNoLocation));
            }
            builder.setNegativeButton(LocaleController.getString("PermissionOpenSettings", R.string.PermissionOpenSettings), new DialogInterface.OnClickListener() {
                @TargetApi(9)
                public void onClick(DialogInterface dialog, int which) {
                    if (ThemeActivity.this.getParentActivity() != null) {
                        try {
                            Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append("package:");
                            stringBuilder.append(ApplicationLoader.applicationContext.getPackageName());
                            intent.setData(Uri.parse(stringBuilder.toString()));
                            ThemeActivity.this.getParentActivity().startActivity(intent);
                        } catch (Throwable e) {
                            FileLog.e(e);
                        }
                    }
                }
            });
            builder.setPositiveButton(LocaleController.getString("OK", R.string.OK), null);
            showDialog(builder.create());
        }
    }

    private String getLocationSunString() {
        int currentMinute = Theme.autoNightSunriseTime - ((Theme.autoNightSunriseTime / 60) * 60);
        String sunriseTimeStr = String.format("%02d:%02d", new Object[]{Integer.valueOf(Theme.autoNightSunriseTime / 60), Integer.valueOf(currentMinute)});
        int currentMinute2 = Theme.autoNightSunsetTime - ((Theme.autoNightSunsetTime / 60) * 60);
        String sunsetTimeStr = String.format("%02d:%02d", new Object[]{Integer.valueOf(Theme.autoNightSunsetTime / 60), Integer.valueOf(currentMinute2)});
        return LocaleController.formatString("AutoNightUpdateLocationInfo", R.string.AutoNightUpdateLocationInfo, sunsetTimeStr, sunriseTimeStr);
    }

    public ThemeDescription[] getThemeDescriptions() {
        r1 = new ThemeDescription[28];
        r1[0] = new ThemeDescription(this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{TextSettingsCell.class, TextCheckCell.class, HeaderCell.class, BrightnessControlCell.class, ThemeTypeCell.class, ThemeCell.class}, null, null, null, Theme.key_windowBackgroundWhite);
        r1[1] = new ThemeDescription(this.fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_windowBackgroundGray);
        r1[2] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_actionBarDefault);
        r1[3] = new ThemeDescription(this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, Theme.key_actionBarDefault);
        r1[4] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, Theme.key_actionBarDefaultIcon);
        r1[5] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, Theme.key_actionBarDefaultTitle);
        r1[6] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, Theme.key_actionBarDefaultSelector);
        r1[7] = new ThemeDescription(this.listView, ThemeDescription.FLAG_SELECTOR, null, null, null, null, Theme.key_listSelector);
        r1[8] = new ThemeDescription(this.listView, 0, new Class[]{View.class}, Theme.dividerPaint, null, null, Theme.key_divider);
        r1[9] = new ThemeDescription(this.listView, 0, new Class[]{ThemeCell.class}, new String[]{"textView"}, null, null, null, Theme.key_windowBackgroundWhiteBlackText);
        r1[10] = new ThemeDescription(this.listView, 0, new Class[]{ThemeCell.class}, new String[]{"checkImage"}, null, null, null, Theme.key_featuredStickers_addedIcon);
        r1[11] = new ThemeDescription(this.listView, 0, new Class[]{ThemeCell.class}, new String[]{"optionsButton"}, null, null, null, Theme.key_stickers_menu);
        r1[12] = new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{ShadowSectionCell.class}, null, null, null, Theme.key_windowBackgroundGrayShadow);
        r1[13] = new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{TextInfoPrivacyCell.class}, null, null, null, Theme.key_windowBackgroundGrayShadow);
        r1[14] = new ThemeDescription(this.listView, 0, new Class[]{TextInfoPrivacyCell.class}, new String[]{"textView"}, null, null, null, Theme.key_windowBackgroundWhiteGrayText4);
        r1[15] = new ThemeDescription(this.listView, 0, new Class[]{TextSettingsCell.class}, new String[]{"textView"}, null, null, null, Theme.key_windowBackgroundWhiteBlackText);
        r1[16] = new ThemeDescription(this.listView, 0, new Class[]{HeaderCell.class}, new String[]{"textView"}, null, null, null, Theme.key_windowBackgroundWhiteBlueHeader);
        r1[17] = new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"textView"}, null, null, null, Theme.key_windowBackgroundWhiteBlackText);
        r1[18] = new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"checkBox"}, null, null, null, Theme.key_switchThumb);
        r1[19] = new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"checkBox"}, null, null, null, Theme.key_switchTrack);
        r1[20] = new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"checkBox"}, null, null, null, Theme.key_switchThumbChecked);
        r1[21] = new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"checkBox"}, null, null, null, Theme.key_switchTrackChecked);
        r1[22] = new ThemeDescription(this.listView, ThemeDescription.FLAG_IMAGECOLOR, new Class[]{BrightnessControlCell.class}, new String[]{"leftImageView"}, null, null, null, Theme.key_profile_actionIcon);
        r1[23] = new ThemeDescription(this.listView, ThemeDescription.FLAG_IMAGECOLOR, new Class[]{BrightnessControlCell.class}, new String[]{"rightImageView"}, null, null, null, Theme.key_profile_actionIcon);
        r1[24] = new ThemeDescription(this.listView, 0, new Class[]{BrightnessControlCell.class}, new String[]{"seekBarView"}, null, null, null, Theme.key_player_progressBackground);
        r1[25] = new ThemeDescription(this.listView, ThemeDescription.FLAG_PROGRESSBAR, new Class[]{BrightnessControlCell.class}, new String[]{"seekBarView"}, null, null, null, Theme.key_player_progress);
        r1[26] = new ThemeDescription(this.listView, 0, new Class[]{ThemeTypeCell.class}, new String[]{"textView"}, null, null, null, Theme.key_windowBackgroundWhiteBlackText);
        r1[27] = new ThemeDescription(this.listView, 0, new Class[]{ThemeTypeCell.class}, new String[]{"checkImage"}, null, null, null, Theme.key_featuredStickers_addedIcon);
        return r1;
    }
}
