package org.telegram.ui.Components;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import java.util.ArrayList;
import java.util.regex.Pattern;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.beta.R;
import org.telegram.messenger.support.widget.LinearLayoutManager;
import org.telegram.messenger.support.widget.RecyclerView;
import org.telegram.messenger.support.widget.RecyclerView.Adapter;
import org.telegram.messenger.support.widget.RecyclerView.OnScrollListener;
import org.telegram.messenger.support.widget.RecyclerView.ViewHolder;
import org.telegram.tgnet.TLRPC.ChannelParticipant;
import org.telegram.tgnet.TLRPC.TL_channelAdminLogEventsFilter;
import org.telegram.tgnet.TLRPC.User;
import org.telegram.ui.ActionBar.BottomSheet;
import org.telegram.ui.ActionBar.BottomSheet.BottomSheetCell;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.CheckBoxCell;
import org.telegram.ui.Cells.CheckBoxUserCell;
import org.telegram.ui.Cells.ShadowSectionCell;
import org.telegram.ui.Components.RecyclerListView.Holder;
import org.telegram.ui.Components.RecyclerListView.OnItemClickListener;
import org.telegram.ui.Components.RecyclerListView.SelectionAdapter;
import org.telegram.ui.StickerPreviewViewer;

public class AdminLogFilterAlert extends BottomSheet {
    private ListAdapter adapter;
    private int adminsRow;
    private int allAdminsRow;
    private ArrayList<ChannelParticipant> currentAdmins;
    private TL_channelAdminLogEventsFilter currentFilter;
    private AdminLogFilterAlertDelegate delegate;
    private int deleteRow;
    private int editRow;
    private boolean ignoreLayout;
    private int infoRow;
    private boolean isMegagroup;
    private int leavingRow;
    private RecyclerListView listView;
    private int membersRow;
    private FrameLayout pickerBottomLayout;
    private int pinnedRow;
    private int reqId;
    private int restrictionsRow;
    private BottomSheetCell saveButton;
    private int scrollOffsetY;
    private SparseArray<User> selectedAdmins;
    private Drawable shadowDrawable;
    private Pattern urlPattern;

    public interface AdminLogFilterAlertDelegate {
        void didSelectRights(TL_channelAdminLogEventsFilter tL_channelAdminLogEventsFilter, SparseArray<User> sparseArray);
    }

    private class ListAdapter extends SelectionAdapter {
        private Context context;

        public ListAdapter(Context context) {
            this.context = context;
        }

        public int getItemCount() {
            return (AdminLogFilterAlert.this.isMegagroup ? 9 : 7) + (AdminLogFilterAlert.this.currentAdmins != null ? 2 + AdminLogFilterAlert.this.currentAdmins.size() : 0);
        }

        public int getItemViewType(int position) {
            if (position >= AdminLogFilterAlert.this.allAdminsRow - 1) {
                if (position != AdminLogFilterAlert.this.allAdminsRow) {
                    if (position == AdminLogFilterAlert.this.allAdminsRow - 1) {
                        return 1;
                    }
                    return 2;
                }
            }
            return 0;
        }

        public boolean isEnabled(ViewHolder holder) {
            return holder.getItemViewType() != 1;
        }

        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = null;
            switch (viewType) {
                case 0:
                    view = new CheckBoxCell(this.context, 1);
                    view.setBackgroundDrawable(Theme.getSelectorDrawable(false));
                    break;
                case 1:
                    ShadowSectionCell shadowSectionCell = new ShadowSectionCell(this.context);
                    shadowSectionCell.setSize(18);
                    view = new FrameLayout(this.context);
                    ((FrameLayout) view).addView(shadowSectionCell, LayoutHelper.createFrame(-1, -1.0f));
                    view.setBackgroundColor(Theme.getColor(Theme.key_dialogBackgroundGray));
                    break;
                case 2:
                    view = new CheckBoxUserCell(this.context, true);
                    break;
                default:
                    break;
            }
            return new Holder(view);
        }

        public void onViewAttachedToWindow(ViewHolder holder) {
            int position = holder.getAdapterPosition();
            int itemViewType = holder.getItemViewType();
            boolean z = true;
            if (itemViewType == 0) {
                CheckBoxCell cell = holder.itemView;
                if (position == 0) {
                    if (AdminLogFilterAlert.this.currentFilter != null) {
                        z = false;
                    }
                    cell.setChecked(z, false);
                } else if (position == AdminLogFilterAlert.this.restrictionsRow) {
                    if (AdminLogFilterAlert.this.currentFilter != null) {
                        if (!AdminLogFilterAlert.this.currentFilter.kick || !AdminLogFilterAlert.this.currentFilter.ban || !AdminLogFilterAlert.this.currentFilter.unkick || !AdminLogFilterAlert.this.currentFilter.unban) {
                            z = false;
                        }
                    }
                    cell.setChecked(z, false);
                } else if (position == AdminLogFilterAlert.this.adminsRow) {
                    if (AdminLogFilterAlert.this.currentFilter != null) {
                        if (!AdminLogFilterAlert.this.currentFilter.promote || !AdminLogFilterAlert.this.currentFilter.demote) {
                            z = false;
                        }
                    }
                    cell.setChecked(z, false);
                } else if (position == AdminLogFilterAlert.this.membersRow) {
                    if (AdminLogFilterAlert.this.currentFilter != null) {
                        if (!AdminLogFilterAlert.this.currentFilter.invite || !AdminLogFilterAlert.this.currentFilter.join) {
                            z = false;
                        }
                    }
                    cell.setChecked(z, false);
                } else if (position == AdminLogFilterAlert.this.infoRow) {
                    if (AdminLogFilterAlert.this.currentFilter != null) {
                        if (!AdminLogFilterAlert.this.currentFilter.info) {
                            z = false;
                        }
                    }
                    cell.setChecked(z, false);
                } else if (position == AdminLogFilterAlert.this.deleteRow) {
                    if (AdminLogFilterAlert.this.currentFilter != null) {
                        if (!AdminLogFilterAlert.this.currentFilter.delete) {
                            z = false;
                        }
                    }
                    cell.setChecked(z, false);
                } else if (position == AdminLogFilterAlert.this.editRow) {
                    if (AdminLogFilterAlert.this.currentFilter != null) {
                        if (!AdminLogFilterAlert.this.currentFilter.edit) {
                            z = false;
                        }
                    }
                    cell.setChecked(z, false);
                } else if (position == AdminLogFilterAlert.this.pinnedRow) {
                    if (AdminLogFilterAlert.this.currentFilter != null) {
                        if (!AdminLogFilterAlert.this.currentFilter.pinned) {
                            z = false;
                        }
                    }
                    cell.setChecked(z, false);
                } else if (position == AdminLogFilterAlert.this.leavingRow) {
                    if (AdminLogFilterAlert.this.currentFilter != null) {
                        if (!AdminLogFilterAlert.this.currentFilter.leave) {
                            z = false;
                        }
                    }
                    cell.setChecked(z, false);
                } else if (position == AdminLogFilterAlert.this.allAdminsRow) {
                    if (AdminLogFilterAlert.this.selectedAdmins != null) {
                        z = false;
                    }
                    cell.setChecked(z, false);
                }
            } else if (itemViewType == 2) {
                CheckBoxUserCell userCell = holder.itemView;
                int userId = ((ChannelParticipant) AdminLogFilterAlert.this.currentAdmins.get((position - AdminLogFilterAlert.this.allAdminsRow) - 1)).user_id;
                if (AdminLogFilterAlert.this.selectedAdmins != null) {
                    if (AdminLogFilterAlert.this.selectedAdmins.indexOfKey(userId) < 0) {
                        z = false;
                    }
                }
                userCell.setChecked(z, false);
            }
        }

        public void onBindViewHolder(ViewHolder holder, int position) {
            int itemViewType = holder.getItemViewType();
            boolean z = false;
            boolean z2 = true;
            if (itemViewType == 0) {
                CheckBoxCell cell = holder.itemView;
                String string;
                String str;
                if (position == 0) {
                    string = LocaleController.getString("EventLogFilterAll", R.string.EventLogFilterAll);
                    str = TtmlNode.ANONYMOUS_REGION_ID;
                    if (AdminLogFilterAlert.this.currentFilter == null) {
                        z = true;
                    }
                    cell.setText(string, str, z, true);
                } else if (position == AdminLogFilterAlert.this.restrictionsRow) {
                    string = LocaleController.getString("EventLogFilterNewRestrictions", R.string.EventLogFilterNewRestrictions);
                    str = TtmlNode.ANONYMOUS_REGION_ID;
                    if (AdminLogFilterAlert.this.currentFilter != null) {
                        if (!AdminLogFilterAlert.this.currentFilter.kick || !AdminLogFilterAlert.this.currentFilter.ban || !AdminLogFilterAlert.this.currentFilter.unkick || !AdminLogFilterAlert.this.currentFilter.unban) {
                            cell.setText(string, str, z, true);
                        }
                    }
                    z = true;
                    cell.setText(string, str, z, true);
                } else if (position == AdminLogFilterAlert.this.adminsRow) {
                    string = LocaleController.getString("EventLogFilterNewAdmins", R.string.EventLogFilterNewAdmins);
                    str = TtmlNode.ANONYMOUS_REGION_ID;
                    if (AdminLogFilterAlert.this.currentFilter != null) {
                        if (!AdminLogFilterAlert.this.currentFilter.promote || !AdminLogFilterAlert.this.currentFilter.demote) {
                            cell.setText(string, str, z, true);
                        }
                    }
                    z = true;
                    cell.setText(string, str, z, true);
                } else if (position == AdminLogFilterAlert.this.membersRow) {
                    string = LocaleController.getString("EventLogFilterNewMembers", R.string.EventLogFilterNewMembers);
                    str = TtmlNode.ANONYMOUS_REGION_ID;
                    if (AdminLogFilterAlert.this.currentFilter != null) {
                        if (!AdminLogFilterAlert.this.currentFilter.invite || !AdminLogFilterAlert.this.currentFilter.join) {
                            cell.setText(string, str, z, true);
                        }
                    }
                    z = true;
                    cell.setText(string, str, z, true);
                } else if (position == AdminLogFilterAlert.this.infoRow) {
                    if (AdminLogFilterAlert.this.isMegagroup) {
                        string = LocaleController.getString("EventLogFilterGroupInfo", R.string.EventLogFilterGroupInfo);
                        str = TtmlNode.ANONYMOUS_REGION_ID;
                        if (AdminLogFilterAlert.this.currentFilter != null) {
                            if (!AdminLogFilterAlert.this.currentFilter.info) {
                                cell.setText(string, str, z, true);
                                return;
                            }
                        }
                        z = true;
                        cell.setText(string, str, z, true);
                        return;
                    }
                    string = LocaleController.getString("EventLogFilterChannelInfo", R.string.EventLogFilterChannelInfo);
                    str = TtmlNode.ANONYMOUS_REGION_ID;
                    if (AdminLogFilterAlert.this.currentFilter != null) {
                        if (!AdminLogFilterAlert.this.currentFilter.info) {
                            cell.setText(string, str, z, true);
                        }
                    }
                    z = true;
                    cell.setText(string, str, z, true);
                } else if (position == AdminLogFilterAlert.this.deleteRow) {
                    string = LocaleController.getString("EventLogFilterDeletedMessages", R.string.EventLogFilterDeletedMessages);
                    str = TtmlNode.ANONYMOUS_REGION_ID;
                    if (AdminLogFilterAlert.this.currentFilter != null) {
                        if (!AdminLogFilterAlert.this.currentFilter.delete) {
                            cell.setText(string, str, z, true);
                        }
                    }
                    z = true;
                    cell.setText(string, str, z, true);
                } else if (position == AdminLogFilterAlert.this.editRow) {
                    string = LocaleController.getString("EventLogFilterEditedMessages", R.string.EventLogFilterEditedMessages);
                    str = TtmlNode.ANONYMOUS_REGION_ID;
                    if (AdminLogFilterAlert.this.currentFilter != null) {
                        if (!AdminLogFilterAlert.this.currentFilter.edit) {
                            cell.setText(string, str, z, true);
                        }
                    }
                    z = true;
                    cell.setText(string, str, z, true);
                } else if (position == AdminLogFilterAlert.this.pinnedRow) {
                    string = LocaleController.getString("EventLogFilterPinnedMessages", R.string.EventLogFilterPinnedMessages);
                    str = TtmlNode.ANONYMOUS_REGION_ID;
                    if (AdminLogFilterAlert.this.currentFilter != null) {
                        if (!AdminLogFilterAlert.this.currentFilter.pinned) {
                            cell.setText(string, str, z, true);
                        }
                    }
                    z = true;
                    cell.setText(string, str, z, true);
                } else if (position == AdminLogFilterAlert.this.leavingRow) {
                    string = LocaleController.getString("EventLogFilterLeavingMembers", R.string.EventLogFilterLeavingMembers);
                    str = TtmlNode.ANONYMOUS_REGION_ID;
                    if (AdminLogFilterAlert.this.currentFilter != null) {
                        if (!AdminLogFilterAlert.this.currentFilter.leave) {
                            z2 = false;
                        }
                    }
                    cell.setText(string, str, z2, false);
                } else if (position == AdminLogFilterAlert.this.allAdminsRow) {
                    string = LocaleController.getString("EventLogAllAdmins", R.string.EventLogAllAdmins);
                    str = TtmlNode.ANONYMOUS_REGION_ID;
                    if (AdminLogFilterAlert.this.selectedAdmins == null) {
                        z = true;
                    }
                    cell.setText(string, str, z, true);
                }
            } else if (itemViewType == 2) {
                boolean z3;
                CheckBoxUserCell userCell = holder.itemView;
                int userId = ((ChannelParticipant) AdminLogFilterAlert.this.currentAdmins.get((position - AdminLogFilterAlert.this.allAdminsRow) - 1)).user_id;
                User user = MessagesController.getInstance(AdminLogFilterAlert.this.currentAccount).getUser(Integer.valueOf(userId));
                if (AdminLogFilterAlert.this.selectedAdmins != null) {
                    if (AdminLogFilterAlert.this.selectedAdmins.indexOfKey(userId) < 0) {
                        z3 = false;
                        if (position != getItemCount() - 1) {
                            z = true;
                        }
                        userCell.setUser(user, z3, z);
                    }
                }
                z3 = true;
                if (position != getItemCount() - 1) {
                    z = true;
                }
                userCell.setUser(user, z3, z);
            }
        }
    }

    public AdminLogFilterAlert(Context context, TL_channelAdminLogEventsFilter filter, SparseArray<User> admins, boolean megagroup) {
        int rowCount;
        Context context2 = context;
        TL_channelAdminLogEventsFilter tL_channelAdminLogEventsFilter = filter;
        super(context2, false);
        if (tL_channelAdminLogEventsFilter != null) {
            r0.currentFilter = new TL_channelAdminLogEventsFilter();
            r0.currentFilter.join = tL_channelAdminLogEventsFilter.join;
            r0.currentFilter.leave = tL_channelAdminLogEventsFilter.leave;
            r0.currentFilter.invite = tL_channelAdminLogEventsFilter.invite;
            r0.currentFilter.ban = tL_channelAdminLogEventsFilter.ban;
            r0.currentFilter.unban = tL_channelAdminLogEventsFilter.unban;
            r0.currentFilter.kick = tL_channelAdminLogEventsFilter.kick;
            r0.currentFilter.unkick = tL_channelAdminLogEventsFilter.unkick;
            r0.currentFilter.promote = tL_channelAdminLogEventsFilter.promote;
            r0.currentFilter.demote = tL_channelAdminLogEventsFilter.demote;
            r0.currentFilter.info = tL_channelAdminLogEventsFilter.info;
            r0.currentFilter.settings = tL_channelAdminLogEventsFilter.settings;
            r0.currentFilter.pinned = tL_channelAdminLogEventsFilter.pinned;
            r0.currentFilter.edit = tL_channelAdminLogEventsFilter.edit;
            r0.currentFilter.delete = tL_channelAdminLogEventsFilter.delete;
        }
        if (admins != null) {
            r0.selectedAdmins = admins.clone();
        }
        r0.isMegagroup = megagroup;
        int rowCount2 = 1;
        if (r0.isMegagroup) {
            rowCount = 1 + 1;
            r0.restrictionsRow = 1;
            rowCount2 = rowCount;
        } else {
            r0.restrictionsRow = -1;
        }
        rowCount = rowCount2 + 1;
        r0.adminsRow = rowCount2;
        rowCount2 = rowCount + 1;
        r0.membersRow = rowCount;
        rowCount = rowCount2 + 1;
        r0.infoRow = rowCount2;
        rowCount2 = rowCount + 1;
        r0.deleteRow = rowCount;
        rowCount = rowCount2 + 1;
        r0.editRow = rowCount2;
        if (r0.isMegagroup) {
            rowCount2 = rowCount + 1;
            r0.pinnedRow = rowCount;
        } else {
            r0.pinnedRow = -1;
            rowCount2 = rowCount;
        }
        r0.leavingRow = rowCount2;
        r0.allAdminsRow = rowCount2 + 2;
        r0.shadowDrawable = context.getResources().getDrawable(R.drawable.sheet_shadow).mutate();
        r0.shadowDrawable.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_dialogBackground), Mode.MULTIPLY));
        r0.containerView = new FrameLayout(context2) {
            public boolean onInterceptTouchEvent(MotionEvent ev) {
                if (ev.getAction() != 0 || AdminLogFilterAlert.this.scrollOffsetY == 0 || ev.getY() >= ((float) AdminLogFilterAlert.this.scrollOffsetY)) {
                    return super.onInterceptTouchEvent(ev);
                }
                AdminLogFilterAlert.this.dismiss();
                return true;
            }

            public boolean onTouchEvent(MotionEvent e) {
                return !AdminLogFilterAlert.this.isDismissed() && super.onTouchEvent(e);
            }

            protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
                int height = MeasureSpec.getSize(heightMeasureSpec);
                if (VERSION.SDK_INT >= 21) {
                    height -= AndroidUtilities.statusBarHeight;
                }
                int measuredWidth = getMeasuredWidth();
                int contentSize = (AndroidUtilities.dp(48.0f) + ((AdminLogFilterAlert.this.isMegagroup ? 9 : 7) * AndroidUtilities.dp(48.0f))) + AdminLogFilterAlert.backgroundPaddingTop;
                if (AdminLogFilterAlert.this.currentAdmins != null) {
                    contentSize += ((AdminLogFilterAlert.this.currentAdmins.size() + 1) * AndroidUtilities.dp(48.0f)) + AndroidUtilities.dp(20.0f);
                }
                int padding = ((float) contentSize) < ((float) (height / 5)) * 3.2f ? 0 : (height / 5) * 2;
                if (padding != 0 && contentSize < height) {
                    padding -= height - contentSize;
                }
                if (padding == 0) {
                    padding = AdminLogFilterAlert.backgroundPaddingTop;
                }
                if (AdminLogFilterAlert.this.listView.getPaddingTop() != padding) {
                    AdminLogFilterAlert.this.ignoreLayout = true;
                    AdminLogFilterAlert.this.listView.setPadding(0, padding, 0, 0);
                    AdminLogFilterAlert.this.ignoreLayout = false;
                }
                super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(Math.min(contentSize, height), 1073741824));
            }

            protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
                super.onLayout(changed, left, top, right, bottom);
                AdminLogFilterAlert.this.updateLayout();
            }

            public void requestLayout() {
                if (!AdminLogFilterAlert.this.ignoreLayout) {
                    super.requestLayout();
                }
            }

            protected void onDraw(Canvas canvas) {
                AdminLogFilterAlert.this.shadowDrawable.setBounds(0, AdminLogFilterAlert.this.scrollOffsetY - AdminLogFilterAlert.backgroundPaddingTop, getMeasuredWidth(), getMeasuredHeight());
                AdminLogFilterAlert.this.shadowDrawable.draw(canvas);
            }
        };
        r0.containerView.setWillNotDraw(false);
        r0.containerView.setPadding(backgroundPaddingLeft, 0, backgroundPaddingLeft, 0);
        r0.listView = new RecyclerListView(context2) {
            public boolean onInterceptTouchEvent(MotionEvent event) {
                boolean result = StickerPreviewViewer.getInstance().onInterceptTouchEvent(event, AdminLogFilterAlert.this.listView, 0, null);
                if (!super.onInterceptTouchEvent(event)) {
                    if (!result) {
                        return false;
                    }
                }
                return true;
            }

            public void requestLayout() {
                if (!AdminLogFilterAlert.this.ignoreLayout) {
                    super.requestLayout();
                }
            }
        };
        r0.listView.setLayoutManager(new LinearLayoutManager(getContext(), 1, false));
        RecyclerListView recyclerListView = r0.listView;
        Adapter listAdapter = new ListAdapter(context2);
        r0.adapter = listAdapter;
        recyclerListView.setAdapter(listAdapter);
        r0.listView.setVerticalScrollBarEnabled(false);
        r0.listView.setClipToPadding(false);
        r0.listView.setEnabled(true);
        r0.listView.setGlowColor(Theme.getColor(Theme.key_dialogScrollGlow));
        r0.listView.setOnScrollListener(new OnScrollListener() {
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                AdminLogFilterAlert.this.updateLayout();
            }
        });
        r0.listView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(android.view.View r1, int r2) {
                /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.ui.Components.AdminLogFilterAlert.4.onItemClick(android.view.View, int):void
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
                r0 = r19;
                r1 = r20;
                r2 = r21;
                r3 = r1 instanceof org.telegram.ui.Cells.CheckBoxCell;
                r5 = 1;
                if (r3 == 0) goto L_0x03b7;
            L_0x000b:
                r3 = r1;
                r3 = (org.telegram.ui.Cells.CheckBoxCell) r3;
                r6 = r3.isChecked();
                r7 = r6 ^ 1;
                r3.setChecked(r7, r5);
                r7 = 0;
                if (r2 != 0) goto L_0x00ec;
            L_0x001a:
                if (r6 == 0) goto L_0x009c;
            L_0x001c:
                r7 = org.telegram.ui.Components.AdminLogFilterAlert.this;
                r8 = new org.telegram.tgnet.TLRPC$TL_channelAdminLogEventsFilter;
                r8.<init>();
                r7.currentFilter = r8;
                r7 = org.telegram.ui.Components.AdminLogFilterAlert.this;
                r7 = r7.currentFilter;
                r8 = org.telegram.ui.Components.AdminLogFilterAlert.this;
                r8 = r8.currentFilter;
                r9 = org.telegram.ui.Components.AdminLogFilterAlert.this;
                r9 = r9.currentFilter;
                r10 = org.telegram.ui.Components.AdminLogFilterAlert.this;
                r10 = r10.currentFilter;
                r11 = org.telegram.ui.Components.AdminLogFilterAlert.this;
                r11 = r11.currentFilter;
                r12 = org.telegram.ui.Components.AdminLogFilterAlert.this;
                r12 = r12.currentFilter;
                r13 = org.telegram.ui.Components.AdminLogFilterAlert.this;
                r13 = r13.currentFilter;
                r14 = org.telegram.ui.Components.AdminLogFilterAlert.this;
                r14 = r14.currentFilter;
                r15 = org.telegram.ui.Components.AdminLogFilterAlert.this;
                r15 = r15.currentFilter;
                r5 = org.telegram.ui.Components.AdminLogFilterAlert.this;
                r5 = r5.currentFilter;
                r4 = org.telegram.ui.Components.AdminLogFilterAlert.this;
                r4 = r4.currentFilter;
                r16 = r3;
                r3 = org.telegram.ui.Components.AdminLogFilterAlert.this;
                r3 = r3.currentFilter;
                r1 = org.telegram.ui.Components.AdminLogFilterAlert.this;
                r1 = r1.currentFilter;
                r2 = org.telegram.ui.Components.AdminLogFilterAlert.this;
                r2 = r2.currentFilter;
                r17 = r6;
                r6 = 0;
                r2.delete = r6;
                r1.edit = r6;
                r3.pinned = r6;
                r4.settings = r6;
                r5.info = r6;
                r15.demote = r6;
                r14.promote = r6;
                r13.unkick = r6;
                r12.kick = r6;
                r11.unban = r6;
                r10.ban = r6;
                r9.invite = r6;
                r8.leave = r6;
                r7.join = r6;
                goto L_0x00a5;
            L_0x009c:
                r16 = r3;
                r17 = r6;
                r1 = org.telegram.ui.Components.AdminLogFilterAlert.this;
                r1.currentFilter = r7;
            L_0x00a5:
                r1 = org.telegram.ui.Components.AdminLogFilterAlert.this;
                r1 = r1.listView;
                r1 = r1.getChildCount();
                r2 = 0;
                if (r2 >= r1) goto L_0x00e7;
            L_0x00b2:
                r3 = org.telegram.ui.Components.AdminLogFilterAlert.this;
                r3 = r3.listView;
                r3 = r3.getChildAt(r2);
                r4 = org.telegram.ui.Components.AdminLogFilterAlert.this;
                r4 = r4.listView;
                r4 = r4.findContainingViewHolder(r3);
                r5 = r4.getAdapterPosition();
                r6 = r4.getItemViewType();
                if (r6 != 0) goto L_0x00e4;
                if (r5 <= 0) goto L_0x00e4;
                r6 = org.telegram.ui.Components.AdminLogFilterAlert.this;
                r6 = r6.allAdminsRow;
                r7 = 1;
                r6 = r6 - r7;
                if (r5 >= r6) goto L_0x00e4;
                r6 = r3;
                r6 = (org.telegram.ui.Cells.CheckBoxCell) r6;
                r8 = r17 ^ 1;
                r6.setChecked(r8, r7);
                r2 = r2 + 1;
                goto L_0x00b0;
                r2 = r21;
                goto L_0x02e9;
            L_0x00ec:
                r16 = r3;
                r17 = r6;
                r1 = org.telegram.ui.Components.AdminLogFilterAlert.this;
                r1 = r1.allAdminsRow;
                r2 = r21;
                if (r2 != r1) goto L_0x0146;
                if (r17 == 0) goto L_0x0107;
                r1 = org.telegram.ui.Components.AdminLogFilterAlert.this;
                r3 = new android.util.SparseArray;
                r3.<init>();
                r1.selectedAdmins = r3;
                goto L_0x010c;
                r1 = org.telegram.ui.Components.AdminLogFilterAlert.this;
                r1.selectedAdmins = r7;
                r1 = org.telegram.ui.Components.AdminLogFilterAlert.this;
                r1 = r1.listView;
                r1 = r1.getChildCount();
                r3 = 0;
                if (r3 >= r1) goto L_0x0144;
                r4 = org.telegram.ui.Components.AdminLogFilterAlert.this;
                r4 = r4.listView;
                r4 = r4.getChildAt(r3);
                r5 = org.telegram.ui.Components.AdminLogFilterAlert.this;
                r5 = r5.listView;
                r5 = r5.findContainingViewHolder(r4);
                r6 = r5.getAdapterPosition();
                r7 = r5.getItemViewType();
                r8 = 2;
                if (r7 != r8) goto L_0x0141;
                r7 = r4;
                r7 = (org.telegram.ui.Cells.CheckBoxUserCell) r7;
                r8 = r17 ^ 1;
                r9 = 1;
                r7.setChecked(r8, r9);
                r3 = r3 + 1;
                goto L_0x0117;
                goto L_0x02e9;
                r1 = org.telegram.ui.Components.AdminLogFilterAlert.this;
                r1 = r1.currentFilter;
                if (r1 != 0) goto L_0x01dd;
                r1 = org.telegram.ui.Components.AdminLogFilterAlert.this;
                r3 = new org.telegram.tgnet.TLRPC$TL_channelAdminLogEventsFilter;
                r3.<init>();
                r1.currentFilter = r3;
                r1 = org.telegram.ui.Components.AdminLogFilterAlert.this;
                r1 = r1.currentFilter;
                r3 = org.telegram.ui.Components.AdminLogFilterAlert.this;
                r3 = r3.currentFilter;
                r4 = org.telegram.ui.Components.AdminLogFilterAlert.this;
                r4 = r4.currentFilter;
                r5 = org.telegram.ui.Components.AdminLogFilterAlert.this;
                r5 = r5.currentFilter;
                r6 = org.telegram.ui.Components.AdminLogFilterAlert.this;
                r6 = r6.currentFilter;
                r7 = org.telegram.ui.Components.AdminLogFilterAlert.this;
                r7 = r7.currentFilter;
                r8 = org.telegram.ui.Components.AdminLogFilterAlert.this;
                r8 = r8.currentFilter;
                r9 = org.telegram.ui.Components.AdminLogFilterAlert.this;
                r9 = r9.currentFilter;
                r10 = org.telegram.ui.Components.AdminLogFilterAlert.this;
                r10 = r10.currentFilter;
                r11 = org.telegram.ui.Components.AdminLogFilterAlert.this;
                r11 = r11.currentFilter;
                r12 = org.telegram.ui.Components.AdminLogFilterAlert.this;
                r12 = r12.currentFilter;
                r13 = org.telegram.ui.Components.AdminLogFilterAlert.this;
                r13 = r13.currentFilter;
                r14 = org.telegram.ui.Components.AdminLogFilterAlert.this;
                r14 = r14.currentFilter;
                r15 = org.telegram.ui.Components.AdminLogFilterAlert.this;
                r15 = r15.currentFilter;
                r2 = 1;
                r15.delete = r2;
                r14.edit = r2;
                r13.pinned = r2;
                r12.settings = r2;
                r11.info = r2;
                r10.demote = r2;
                r9.promote = r2;
                r8.unkick = r2;
                r7.kick = r2;
                r6.unban = r2;
                r5.ban = r2;
                r4.invite = r2;
                r3.leave = r2;
                r1.join = r2;
                r1 = org.telegram.ui.Components.AdminLogFilterAlert.this;
                r1 = r1.listView;
                r3 = 0;
                r1 = r1.findViewHolderForAdapterPosition(r3);
                if (r1 == 0) goto L_0x01dd;
                r4 = r1.itemView;
                r4 = (org.telegram.ui.Cells.CheckBoxCell) r4;
                r4.setChecked(r3, r2);
                r1 = org.telegram.ui.Components.AdminLogFilterAlert.this;
                r1 = r1.restrictionsRow;
                r2 = r21;
                if (r2 != r1) goto L_0x0213;
                r1 = org.telegram.ui.Components.AdminLogFilterAlert.this;
                r1 = r1.currentFilter;
                r3 = org.telegram.ui.Components.AdminLogFilterAlert.this;
                r3 = r3.currentFilter;
                r4 = org.telegram.ui.Components.AdminLogFilterAlert.this;
                r4 = r4.currentFilter;
                r5 = org.telegram.ui.Components.AdminLogFilterAlert.this;
                r5 = r5.currentFilter;
                r6 = org.telegram.ui.Components.AdminLogFilterAlert.this;
                r6 = r6.currentFilter;
                r6 = r6.kick;
                r7 = 1;
                r6 = r6 ^ r7;
                r5.unban = r6;
                r4.unkick = r6;
                r3.ban = r6;
                r1.kick = r6;
                goto L_0x02e9;
                r1 = org.telegram.ui.Components.AdminLogFilterAlert.this;
                r1 = r1.adminsRow;
                if (r2 != r1) goto L_0x0237;
                r1 = org.telegram.ui.Components.AdminLogFilterAlert.this;
                r1 = r1.currentFilter;
                r3 = org.telegram.ui.Components.AdminLogFilterAlert.this;
                r3 = r3.currentFilter;
                r4 = org.telegram.ui.Components.AdminLogFilterAlert.this;
                r4 = r4.currentFilter;
                r4 = r4.demote;
                r5 = 1;
                r4 = r4 ^ r5;
                r3.demote = r4;
                r1.promote = r4;
                goto L_0x02e9;
                r1 = org.telegram.ui.Components.AdminLogFilterAlert.this;
                r1 = r1.membersRow;
                if (r2 != r1) goto L_0x025b;
                r1 = org.telegram.ui.Components.AdminLogFilterAlert.this;
                r1 = r1.currentFilter;
                r3 = org.telegram.ui.Components.AdminLogFilterAlert.this;
                r3 = r3.currentFilter;
                r4 = org.telegram.ui.Components.AdminLogFilterAlert.this;
                r4 = r4.currentFilter;
                r4 = r4.join;
                r5 = 1;
                r4 = r4 ^ r5;
                r3.join = r4;
                r1.invite = r4;
                goto L_0x02e9;
                r1 = org.telegram.ui.Components.AdminLogFilterAlert.this;
                r1 = r1.infoRow;
                if (r2 != r1) goto L_0x027e;
                r1 = org.telegram.ui.Components.AdminLogFilterAlert.this;
                r1 = r1.currentFilter;
                r3 = org.telegram.ui.Components.AdminLogFilterAlert.this;
                r3 = r3.currentFilter;
                r4 = org.telegram.ui.Components.AdminLogFilterAlert.this;
                r4 = r4.currentFilter;
                r4 = r4.info;
                r5 = 1;
                r4 = r4 ^ r5;
                r3.settings = r4;
                r1.info = r4;
                goto L_0x02e9;
                r1 = org.telegram.ui.Components.AdminLogFilterAlert.this;
                r1 = r1.deleteRow;
                if (r2 != r1) goto L_0x0299;
                r1 = org.telegram.ui.Components.AdminLogFilterAlert.this;
                r1 = r1.currentFilter;
                r3 = org.telegram.ui.Components.AdminLogFilterAlert.this;
                r3 = r3.currentFilter;
                r3 = r3.delete;
                r4 = 1;
                r3 = r3 ^ r4;
                r1.delete = r3;
                goto L_0x02e9;
                r1 = org.telegram.ui.Components.AdminLogFilterAlert.this;
                r1 = r1.editRow;
                if (r2 != r1) goto L_0x02b4;
                r1 = org.telegram.ui.Components.AdminLogFilterAlert.this;
                r1 = r1.currentFilter;
                r3 = org.telegram.ui.Components.AdminLogFilterAlert.this;
                r3 = r3.currentFilter;
                r3 = r3.edit;
                r4 = 1;
                r3 = r3 ^ r4;
                r1.edit = r3;
                goto L_0x02e9;
                r1 = org.telegram.ui.Components.AdminLogFilterAlert.this;
                r1 = r1.pinnedRow;
                if (r2 != r1) goto L_0x02cf;
                r1 = org.telegram.ui.Components.AdminLogFilterAlert.this;
                r1 = r1.currentFilter;
                r3 = org.telegram.ui.Components.AdminLogFilterAlert.this;
                r3 = r3.currentFilter;
                r3 = r3.pinned;
                r4 = 1;
                r3 = r3 ^ r4;
                r1.pinned = r3;
                goto L_0x02e9;
                r1 = org.telegram.ui.Components.AdminLogFilterAlert.this;
                r1 = r1.leavingRow;
                if (r2 != r1) goto L_0x02e9;
                r1 = org.telegram.ui.Components.AdminLogFilterAlert.this;
                r1 = r1.currentFilter;
                r3 = org.telegram.ui.Components.AdminLogFilterAlert.this;
                r3 = r3.currentFilter;
                r3 = r3.leave;
                r4 = 1;
                r3 = r3 ^ r4;
                r1.leave = r3;
                r1 = org.telegram.ui.Components.AdminLogFilterAlert.this;
                r1 = r1.currentFilter;
                if (r1 == 0) goto L_0x039d;
                r1 = org.telegram.ui.Components.AdminLogFilterAlert.this;
                r1 = r1.currentFilter;
                r1 = r1.join;
                if (r1 != 0) goto L_0x039d;
                r1 = org.telegram.ui.Components.AdminLogFilterAlert.this;
                r1 = r1.currentFilter;
                r1 = r1.leave;
                if (r1 != 0) goto L_0x039d;
                r1 = org.telegram.ui.Components.AdminLogFilterAlert.this;
                r1 = r1.currentFilter;
                r1 = r1.leave;
                if (r1 != 0) goto L_0x039d;
                r1 = org.telegram.ui.Components.AdminLogFilterAlert.this;
                r1 = r1.currentFilter;
                r1 = r1.invite;
                if (r1 != 0) goto L_0x039d;
                r1 = org.telegram.ui.Components.AdminLogFilterAlert.this;
                r1 = r1.currentFilter;
                r1 = r1.ban;
                if (r1 != 0) goto L_0x039d;
                r1 = org.telegram.ui.Components.AdminLogFilterAlert.this;
                r1 = r1.currentFilter;
                r1 = r1.unban;
                if (r1 != 0) goto L_0x039d;
                r1 = org.telegram.ui.Components.AdminLogFilterAlert.this;
                r1 = r1.currentFilter;
                r1 = r1.kick;
                if (r1 != 0) goto L_0x039d;
                r1 = org.telegram.ui.Components.AdminLogFilterAlert.this;
                r1 = r1.currentFilter;
                r1 = r1.unkick;
                if (r1 != 0) goto L_0x039d;
                r1 = org.telegram.ui.Components.AdminLogFilterAlert.this;
                r1 = r1.currentFilter;
                r1 = r1.promote;
                if (r1 != 0) goto L_0x039d;
                r1 = org.telegram.ui.Components.AdminLogFilterAlert.this;
                r1 = r1.currentFilter;
                r1 = r1.demote;
                if (r1 != 0) goto L_0x039d;
                r1 = org.telegram.ui.Components.AdminLogFilterAlert.this;
                r1 = r1.currentFilter;
                r1 = r1.info;
                if (r1 != 0) goto L_0x039d;
                r1 = org.telegram.ui.Components.AdminLogFilterAlert.this;
                r1 = r1.currentFilter;
                r1 = r1.settings;
                if (r1 != 0) goto L_0x039d;
                r1 = org.telegram.ui.Components.AdminLogFilterAlert.this;
                r1 = r1.currentFilter;
                r1 = r1.pinned;
                if (r1 != 0) goto L_0x039d;
                r1 = org.telegram.ui.Components.AdminLogFilterAlert.this;
                r1 = r1.currentFilter;
                r1 = r1.edit;
                if (r1 != 0) goto L_0x039d;
                r1 = org.telegram.ui.Components.AdminLogFilterAlert.this;
                r1 = r1.currentFilter;
                r1 = r1.delete;
                if (r1 != 0) goto L_0x039d;
                r1 = org.telegram.ui.Components.AdminLogFilterAlert.this;
                r1 = r1.saveButton;
                r3 = 0;
                r1.setEnabled(r3);
                r1 = org.telegram.ui.Components.AdminLogFilterAlert.this;
                r1 = r1.saveButton;
                r3 = 1056964608; // 0x3f000000 float:0.5 double:5.222099017E-315;
                r1.setAlpha(r3);
                goto L_0x03b2;
                r1 = org.telegram.ui.Components.AdminLogFilterAlert.this;
                r1 = r1.saveButton;
                r3 = 1;
                r1.setEnabled(r3);
                r1 = org.telegram.ui.Components.AdminLogFilterAlert.this;
                r1 = r1.saveButton;
                r3 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
                r1.setAlpha(r3);
                r1 = r20;
                goto L_0x0451;
            L_0x03b7:
                r1 = r20;
                r3 = r1 instanceof org.telegram.ui.Cells.CheckBoxUserCell;
                if (r3 == 0) goto L_0x0451;
                r3 = r1;
                r3 = (org.telegram.ui.Cells.CheckBoxUserCell) r3;
                r4 = org.telegram.ui.Components.AdminLogFilterAlert.this;
                r4 = r4.selectedAdmins;
                if (r4 != 0) goto L_0x042a;
                r4 = org.telegram.ui.Components.AdminLogFilterAlert.this;
                r5 = new android.util.SparseArray;
                r5.<init>();
                r4.selectedAdmins = r5;
                r4 = org.telegram.ui.Components.AdminLogFilterAlert.this;
                r4 = r4.listView;
                r5 = org.telegram.ui.Components.AdminLogFilterAlert.this;
                r5 = r5.allAdminsRow;
                r4 = r4.findViewHolderForAdapterPosition(r5);
                if (r4 == 0) goto L_0x03ee;
                r5 = r4.itemView;
                r5 = (org.telegram.ui.Cells.CheckBoxCell) r5;
                r6 = 1;
                r7 = 0;
                r5.setChecked(r7, r6);
                goto L_0x03ef;
                r7 = 0;
                r5 = r7;
                r6 = org.telegram.ui.Components.AdminLogFilterAlert.this;
                r6 = r6.currentAdmins;
                r6 = r6.size();
                if (r5 >= r6) goto L_0x042a;
                r6 = org.telegram.ui.Components.AdminLogFilterAlert.this;
                r6 = r6.currentAccount;
                r6 = org.telegram.messenger.MessagesController.getInstance(r6);
                r7 = org.telegram.ui.Components.AdminLogFilterAlert.this;
                r7 = r7.currentAdmins;
                r7 = r7.get(r5);
                r7 = (org.telegram.tgnet.TLRPC.ChannelParticipant) r7;
                r7 = r7.user_id;
                r7 = java.lang.Integer.valueOf(r7);
                r6 = r6.getUser(r7);
                r7 = org.telegram.ui.Components.AdminLogFilterAlert.this;
                r7 = r7.selectedAdmins;
                r8 = r6.id;
                r7.put(r8, r6);
                r7 = r5 + 1;
                goto L_0x03ef;
                r4 = r3.isChecked();
                r5 = r3.getCurrentUser();
                if (r4 == 0) goto L_0x0440;
                r6 = org.telegram.ui.Components.AdminLogFilterAlert.this;
                r6 = r6.selectedAdmins;
                r7 = r5.id;
                r6.remove(r7);
                goto L_0x044b;
                r6 = org.telegram.ui.Components.AdminLogFilterAlert.this;
                r6 = r6.selectedAdmins;
                r7 = r5.id;
                r6.put(r7, r5);
                r6 = r4 ^ 1;
                r7 = 1;
                r3.setChecked(r6, r7);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: org.telegram.ui.Components.AdminLogFilterAlert.4.onItemClick(android.view.View, int):void");
            }
        });
        r0.containerView.addView(r0.listView, LayoutHelper.createFrame(-1, -1.0f, 51, 0.0f, 0.0f, 0.0f, 48.0f));
        View shadow = new View(context2);
        shadow.setBackgroundResource(R.drawable.header_shadow_reverse);
        r0.containerView.addView(shadow, LayoutHelper.createFrame(-1, 3.0f, 83, 0.0f, 0.0f, 0.0f, 48.0f));
        r0.saveButton = new BottomSheetCell(context2, 1);
        r0.saveButton.setBackgroundDrawable(Theme.getSelectorDrawable(false));
        r0.saveButton.setTextAndIcon(LocaleController.getString("Save", R.string.Save).toUpperCase(), 0);
        r0.saveButton.setTextColor(Theme.getColor(Theme.key_dialogTextBlue2));
        r0.saveButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                AdminLogFilterAlert.this.delegate.didSelectRights(AdminLogFilterAlert.this.currentFilter, AdminLogFilterAlert.this.selectedAdmins);
                AdminLogFilterAlert.this.dismiss();
            }
        });
        r0.containerView.addView(r0.saveButton, LayoutHelper.createFrame(-1, 48, 83));
        r0.adapter.notifyDataSetChanged();
    }

    public void setCurrentAdmins(ArrayList<ChannelParticipant> admins) {
        this.currentAdmins = admins;
        if (this.adapter != null) {
            this.adapter.notifyDataSetChanged();
        }
    }

    protected boolean canDismissWithSwipe() {
        return false;
    }

    public void setAdminLogFilterAlertDelegate(AdminLogFilterAlertDelegate adminLogFilterAlertDelegate) {
        this.delegate = adminLogFilterAlertDelegate;
    }

    @SuppressLint({"NewApi"})
    private void updateLayout() {
        if (this.listView.getChildCount() <= 0) {
            RecyclerListView recyclerListView = this.listView;
            int paddingTop = this.listView.getPaddingTop();
            this.scrollOffsetY = paddingTop;
            recyclerListView.setTopGlowOffset(paddingTop);
            this.containerView.invalidate();
            return;
        }
        paddingTop = 0;
        View child = this.listView.getChildAt(0);
        Holder holder = (Holder) this.listView.findContainingViewHolder(child);
        int top = child.getTop() - AndroidUtilities.dp(8.0f);
        if (top > 0 && holder != null && holder.getAdapterPosition() == 0) {
            paddingTop = top;
        }
        if (this.scrollOffsetY != paddingTop) {
            RecyclerListView recyclerListView2 = this.listView;
            this.scrollOffsetY = paddingTop;
            recyclerListView2.setTopGlowOffset(paddingTop);
            this.containerView.invalidate();
        }
    }
}
