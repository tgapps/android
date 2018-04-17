package org.telegram.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ActionMode;
import android.view.ActionMode.Callback;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.ContactsController.Contact;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.NotificationCenter.NotificationCenterDelegate;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.beta.R;
import org.telegram.messenger.support.widget.LinearLayoutManager;
import org.telegram.messenger.support.widget.RecyclerView;
import org.telegram.messenger.support.widget.RecyclerView.Adapter;
import org.telegram.messenger.support.widget.RecyclerView.ItemDecoration;
import org.telegram.messenger.support.widget.RecyclerView.OnScrollListener;
import org.telegram.messenger.support.widget.RecyclerView.ViewHolder;
import org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.ActionBar.ThemeDescription.ThemeDescriptionDelegate;
import org.telegram.ui.Cells.GroupCreateSectionCell;
import org.telegram.ui.Cells.InviteTextCell;
import org.telegram.ui.Cells.InviteUserCell;
import org.telegram.ui.Components.EditTextBoldCursor;
import org.telegram.ui.Components.EmptyTextProgressView;
import org.telegram.ui.Components.GroupCreateDividerItemDecoration;
import org.telegram.ui.Components.GroupCreateSpan;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.RecyclerListView.Holder;
import org.telegram.ui.Components.RecyclerListView.OnItemClickListener;
import org.telegram.ui.Components.RecyclerListView.SelectionAdapter;

public class InviteContactsActivity extends BaseFragment implements OnClickListener, NotificationCenterDelegate {
    private InviteAdapter adapter;
    private ArrayList<GroupCreateSpan> allSpans = new ArrayList();
    private int containerHeight;
    private TextView counterTextView;
    private FrameLayout counterView;
    private GroupCreateSpan currentDeletingSpan;
    private GroupCreateDividerItemDecoration decoration;
    private EditTextBoldCursor editText;
    private EmptyTextProgressView emptyView;
    private int fieldY;
    private boolean ignoreScrollEvent;
    private TextView infoTextView;
    private RecyclerListView listView;
    private ArrayList<Contact> phoneBookContacts;
    private ScrollView scrollView;
    private boolean searchWas;
    private boolean searching;
    private HashMap<String, GroupCreateSpan> selectedContacts = new HashMap();
    private SpansContainer spansContainer;
    private TextView textView;

    private class SpansContainer extends ViewGroup {
        private View addingSpan;
        private boolean animationStarted;
        private ArrayList<Animator> animators = new ArrayList();
        private AnimatorSet currentAnimation;
        private View removingSpan;

        public SpansContainer(Context context) {
            super(context);
        }

        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            SpansContainer spansContainer = this;
            int count = getChildCount();
            int width = MeasureSpec.getSize(widthMeasureSpec);
            int maxWidth = width - AndroidUtilities.dp(32.0f);
            float f = 12.0f;
            int y = AndroidUtilities.dp(12.0f);
            int allY = AndroidUtilities.dp(12.0f);
            int allCurrentLineWidth = 0;
            int y2 = y;
            y = 0;
            int a = 0;
            while (a < count) {
                int x;
                View child = getChildAt(a);
                if (child instanceof GroupCreateSpan) {
                    child.measure(MeasureSpec.makeMeasureSpec(width, Integer.MIN_VALUE), MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(32.0f), 1073741824));
                    if (child != spansContainer.removingSpan && child.getMeasuredWidth() + y > maxWidth) {
                        y2 += child.getMeasuredHeight() + AndroidUtilities.dp(f);
                        y = 0;
                    }
                    if (child.getMeasuredWidth() + allCurrentLineWidth > maxWidth) {
                        allY += child.getMeasuredHeight() + AndroidUtilities.dp(f);
                        allCurrentLineWidth = 0;
                    }
                    x = AndroidUtilities.dp(16.0f) + y;
                    if (!spansContainer.animationStarted) {
                        if (child == spansContainer.removingSpan) {
                            child.setTranslationX((float) (AndroidUtilities.dp(16.0f) + allCurrentLineWidth));
                            child.setTranslationY((float) allY);
                        } else if (spansContainer.removingSpan != null) {
                            if (child.getTranslationX() != ((float) x)) {
                                spansContainer.animators.add(ObjectAnimator.ofFloat(child, "translationX", new float[]{(float) x}));
                            }
                            if (child.getTranslationY() != ((float) y2)) {
                                spansContainer.animators.add(ObjectAnimator.ofFloat(child, "translationY", new float[]{(float) y2}));
                            }
                        } else {
                            child.setTranslationX((float) x);
                            child.setTranslationY((float) y2);
                        }
                    }
                    if (child != spansContainer.removingSpan) {
                        y += child.getMeasuredWidth() + AndroidUtilities.dp(9.0f);
                    }
                    allCurrentLineWidth += child.getMeasuredWidth() + AndroidUtilities.dp(9.0f);
                }
                a++;
                f = 12.0f;
            }
            if (AndroidUtilities.isTablet()) {
                a = AndroidUtilities.dp(366.0f) / 3;
            } else {
                a = (Math.min(AndroidUtilities.displaySize.x, AndroidUtilities.displaySize.y) - AndroidUtilities.dp(164.0f)) / 3;
            }
            if (maxWidth - y < a) {
                y = 0;
                y2 += AndroidUtilities.dp(44.0f);
            }
            if (maxWidth - allCurrentLineWidth < a) {
                allY += AndroidUtilities.dp(44.0f);
            }
            InviteContactsActivity.this.editText.measure(MeasureSpec.makeMeasureSpec(maxWidth - y, 1073741824), MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(32.0f), 1073741824));
            int i;
            if (spansContainer.animationStarted) {
                i = maxWidth;
                if (!(spansContainer.currentAnimation == null || InviteContactsActivity.this.ignoreScrollEvent || spansContainer.removingSpan != null)) {
                    InviteContactsActivity.this.editText.bringPointIntoView(InviteContactsActivity.this.editText.getSelectionStart());
                }
            } else {
                int currentHeight = AndroidUtilities.dp(44.0f) + allY;
                int fieldX = AndroidUtilities.dp(16.0f) + y;
                InviteContactsActivity.this.fieldY = y2;
                if (spansContainer.currentAnimation != null) {
                    boolean z;
                    if (InviteContactsActivity.this.containerHeight != AndroidUtilities.dp(44.0f) + y2) {
                        spansContainer.animators.add(ObjectAnimator.ofInt(InviteContactsActivity.this, "containerHeight", new int[]{x}));
                    }
                    if (InviteContactsActivity.this.editText.getTranslationX() != ((float) fieldX)) {
                        spansContainer.animators.add(ObjectAnimator.ofFloat(InviteContactsActivity.this.editText, "translationX", new float[]{(float) fieldX}));
                    }
                    if (InviteContactsActivity.this.editText.getTranslationY() != ((float) InviteContactsActivity.this.fieldY)) {
                        ArrayList arrayList = spansContainer.animators;
                        float[] fArr = new float[1];
                        z = false;
                        fArr[0] = (float) InviteContactsActivity.this.fieldY;
                        arrayList.add(ObjectAnimator.ofFloat(InviteContactsActivity.this.editText, "translationY", fArr));
                    } else {
                        z = false;
                    }
                    InviteContactsActivity.this.editText.setAllowDrawCursor(z);
                    spansContainer.currentAnimation.playTogether(spansContainer.animators);
                    spansContainer.currentAnimation.start();
                    spansContainer.animationStarted = true;
                } else {
                    i = maxWidth;
                    InviteContactsActivity.this.containerHeight = currentHeight;
                    InviteContactsActivity.this.editText.setTranslationX((float) fieldX);
                    InviteContactsActivity.this.editText.setTranslationY((float) InviteContactsActivity.this.fieldY);
                }
            }
            setMeasuredDimension(width, InviteContactsActivity.this.containerHeight);
        }

        protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
            int count = getChildCount();
            for (int a = 0; a < count; a++) {
                View child = getChildAt(a);
                child.layout(0, 0, child.getMeasuredWidth(), child.getMeasuredHeight());
            }
        }

        public void addSpan(GroupCreateSpan span) {
            InviteContactsActivity.this.allSpans.add(span);
            InviteContactsActivity.this.selectedContacts.put(span.getKey(), span);
            InviteContactsActivity.this.editText.setHintVisible(false);
            if (this.currentAnimation != null) {
                this.currentAnimation.setupEndValues();
                this.currentAnimation.cancel();
            }
            this.animationStarted = false;
            this.currentAnimation = new AnimatorSet();
            this.currentAnimation.addListener(new AnimatorListenerAdapter() {
                public void onAnimationEnd(Animator animator) {
                    SpansContainer.this.addingSpan = null;
                    SpansContainer.this.currentAnimation = null;
                    SpansContainer.this.animationStarted = false;
                    InviteContactsActivity.this.editText.setAllowDrawCursor(true);
                }
            });
            this.currentAnimation.setDuration(150);
            this.addingSpan = span;
            this.animators.clear();
            this.animators.add(ObjectAnimator.ofFloat(this.addingSpan, "scaleX", new float[]{0.01f, 1.0f}));
            this.animators.add(ObjectAnimator.ofFloat(this.addingSpan, "scaleY", new float[]{0.01f, 1.0f}));
            this.animators.add(ObjectAnimator.ofFloat(this.addingSpan, "alpha", new float[]{0.0f, 1.0f}));
            addView(span);
        }

        public void removeSpan(final GroupCreateSpan span) {
            InviteContactsActivity.this.ignoreScrollEvent = true;
            InviteContactsActivity.this.selectedContacts.remove(span.getKey());
            InviteContactsActivity.this.allSpans.remove(span);
            span.setOnClickListener(null);
            if (this.currentAnimation != null) {
                this.currentAnimation.setupEndValues();
                this.currentAnimation.cancel();
            }
            this.animationStarted = false;
            this.currentAnimation = new AnimatorSet();
            this.currentAnimation.addListener(new AnimatorListenerAdapter() {
                public void onAnimationEnd(Animator animator) {
                    SpansContainer.this.removeView(span);
                    SpansContainer.this.removingSpan = null;
                    SpansContainer.this.currentAnimation = null;
                    SpansContainer.this.animationStarted = false;
                    InviteContactsActivity.this.editText.setAllowDrawCursor(true);
                    if (InviteContactsActivity.this.allSpans.isEmpty()) {
                        InviteContactsActivity.this.editText.setHintVisible(true);
                    }
                }
            });
            this.currentAnimation.setDuration(150);
            this.removingSpan = span;
            this.animators.clear();
            this.animators.add(ObjectAnimator.ofFloat(this.removingSpan, "scaleX", new float[]{1.0f, 0.01f}));
            this.animators.add(ObjectAnimator.ofFloat(this.removingSpan, "scaleY", new float[]{1.0f, 0.01f}));
            this.animators.add(ObjectAnimator.ofFloat(this.removingSpan, "alpha", new float[]{1.0f, 0.0f}));
            requestLayout();
        }
    }

    public class InviteAdapter extends SelectionAdapter {
        private Context context;
        private ArrayList<Contact> searchResult = new ArrayList();
        private ArrayList<CharSequence> searchResultNames = new ArrayList();
        private Timer searchTimer;
        private boolean searching;

        public InviteAdapter(Context ctx) {
            this.context = ctx;
        }

        public void setSearching(boolean value) {
            if (this.searching != value) {
                this.searching = value;
                notifyDataSetChanged();
            }
        }

        public int getItemCount() {
            if (this.searching) {
                return this.searchResult.size();
            }
            return InviteContactsActivity.this.phoneBookContacts.size() + 1;
        }

        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view;
            if (viewType != 1) {
                view = new InviteUserCell(this.context, true);
            } else {
                view = new InviteTextCell(this.context);
                ((InviteTextCell) view).setTextAndIcon(LocaleController.getString("ShareTelegram", R.string.ShareTelegram), R.drawable.share);
            }
            return new Holder(view);
        }

        public void onBindViewHolder(ViewHolder holder, int position) {
            if (holder.getItemViewType() == 0) {
                Contact contact;
                CharSequence name;
                InviteUserCell cell = holder.itemView;
                if (this.searching) {
                    contact = (Contact) this.searchResult.get(position);
                    name = (CharSequence) this.searchResultNames.get(position);
                } else {
                    contact = (Contact) InviteContactsActivity.this.phoneBookContacts.get(position - 1);
                    name = null;
                }
                cell.setUser(contact, name);
                cell.setChecked(InviteContactsActivity.this.selectedContacts.containsKey(contact.key), false);
            }
        }

        public int getItemViewType(int position) {
            if (this.searching || position != 0) {
                return 0;
            }
            return 1;
        }

        public void onViewRecycled(ViewHolder holder) {
            if (holder.itemView instanceof InviteUserCell) {
                ((InviteUserCell) holder.itemView).recycle();
            }
        }

        public boolean isEnabled(ViewHolder holder) {
            return true;
        }

        public void searchDialogs(final String query) {
            try {
                if (this.searchTimer != null) {
                    this.searchTimer.cancel();
                }
            } catch (Throwable e) {
                FileLog.e(e);
            }
            if (query == null) {
                this.searchResult.clear();
                this.searchResultNames.clear();
                notifyDataSetChanged();
                return;
            }
            this.searchTimer = new Timer();
            this.searchTimer.schedule(new TimerTask() {
                public void run() {
                    try {
                        InviteAdapter.this.searchTimer.cancel();
                        InviteAdapter.this.searchTimer = null;
                    } catch (Throwable e) {
                        FileLog.e(e);
                    }
                    AndroidUtilities.runOnUIThread(new Runnable() {
                        public void run() {
                            Utilities.searchQueue.postRunnable(new Runnable() {
                                /* JADX WARNING: inconsistent code. */
                                /* Code decompiled incorrectly, please refer to instructions dump. */
                                public void run() {
                                    /*
                                    r17 = this;
                                    r0 = r17;
                                    r1 = org.telegram.ui.InviteContactsActivity.InviteAdapter.1.AnonymousClass1.this;
                                    r1 = org.telegram.ui.InviteContactsActivity.InviteAdapter.AnonymousClass1.this;
                                    r1 = r8;
                                    r1 = r1.trim();
                                    r1 = r1.toLowerCase();
                                    r2 = r1.length();
                                    if (r2 != 0) goto L_0x002a;
                                L_0x0016:
                                    r2 = org.telegram.ui.InviteContactsActivity.InviteAdapter.1.AnonymousClass1.this;
                                    r2 = org.telegram.ui.InviteContactsActivity.InviteAdapter.AnonymousClass1.this;
                                    r2 = org.telegram.ui.InviteContactsActivity.InviteAdapter.this;
                                    r3 = new java.util.ArrayList;
                                    r3.<init>();
                                    r4 = new java.util.ArrayList;
                                    r4.<init>();
                                    r2.updateSearchResults(r3, r4);
                                    return;
                                L_0x002a:
                                    r2 = org.telegram.messenger.LocaleController.getInstance();
                                    r2 = r2.getTranslitString(r1);
                                    r3 = r1.equals(r2);
                                    if (r3 != 0) goto L_0x003e;
                                L_0x0038:
                                    r3 = r2.length();
                                    if (r3 != 0) goto L_0x003f;
                                L_0x003e:
                                    r2 = 0;
                                L_0x003f:
                                    r3 = 0;
                                    r4 = 1;
                                    if (r2 == 0) goto L_0x0045;
                                L_0x0043:
                                    r5 = r4;
                                    goto L_0x0046;
                                L_0x0045:
                                    r5 = r3;
                                L_0x0046:
                                    r5 = r5 + r4;
                                    r5 = new java.lang.String[r5];
                                    r5[r3] = r1;
                                    if (r2 == 0) goto L_0x004f;
                                L_0x004d:
                                    r5[r4] = r2;
                                L_0x004f:
                                    r4 = new java.util.ArrayList;
                                    r4.<init>();
                                    r6 = new java.util.ArrayList;
                                    r6.<init>();
                                    r7 = r3;
                                L_0x005a:
                                    r8 = org.telegram.ui.InviteContactsActivity.InviteAdapter.1.AnonymousClass1.this;
                                    r8 = org.telegram.ui.InviteContactsActivity.InviteAdapter.AnonymousClass1.this;
                                    r8 = org.telegram.ui.InviteContactsActivity.InviteAdapter.this;
                                    r8 = org.telegram.ui.InviteContactsActivity.this;
                                    r8 = r8.phoneBookContacts;
                                    r8 = r8.size();
                                    if (r7 >= r8) goto L_0x00f9;
                                L_0x006c:
                                    r8 = org.telegram.ui.InviteContactsActivity.InviteAdapter.1.AnonymousClass1.this;
                                    r8 = org.telegram.ui.InviteContactsActivity.InviteAdapter.AnonymousClass1.this;
                                    r8 = org.telegram.ui.InviteContactsActivity.InviteAdapter.this;
                                    r8 = org.telegram.ui.InviteContactsActivity.this;
                                    r8 = r8.phoneBookContacts;
                                    r8 = r8.get(r7);
                                    r8 = (org.telegram.messenger.ContactsController.Contact) r8;
                                    r9 = r8.first_name;
                                    r10 = r8.last_name;
                                    r9 = org.telegram.messenger.ContactsController.formatName(r9, r10);
                                    r9 = r9.toLowerCase();
                                    r10 = org.telegram.messenger.LocaleController.getInstance();
                                    r10 = r10.getTranslitString(r9);
                                    r11 = r9.equals(r10);
                                    if (r11 == 0) goto L_0x0099;
                                L_0x0098:
                                    r10 = 0;
                                L_0x0099:
                                    r11 = 0;
                                    r12 = r5.length;
                                    r13 = r11;
                                    r11 = r3;
                                L_0x009d:
                                    if (r11 >= r12) goto L_0x00f4;
                                L_0x009f:
                                    r14 = r5[r11];
                                    r15 = r9.startsWith(r14);
                                    if (r15 != 0) goto L_0x00dd;
                                L_0x00a7:
                                    r15 = new java.lang.StringBuilder;
                                    r15.<init>();
                                    r3 = " ";
                                    r15.append(r3);
                                    r15.append(r14);
                                    r3 = r15.toString();
                                    r3 = r9.contains(r3);
                                    if (r3 != 0) goto L_0x00dd;
                                L_0x00be:
                                    if (r10 == 0) goto L_0x00df;
                                L_0x00c0:
                                    r3 = r10.startsWith(r14);
                                    if (r3 != 0) goto L_0x00dd;
                                L_0x00c6:
                                    r3 = new java.lang.StringBuilder;
                                    r3.<init>();
                                    r15 = " ";
                                    r3.append(r15);
                                    r3.append(r14);
                                    r3 = r3.toString();
                                    r3 = r10.contains(r3);
                                    if (r3 == 0) goto L_0x00df;
                                L_0x00dd:
                                    r3 = 1;
                                    r13 = r3;
                                L_0x00df:
                                    if (r13 == 0) goto L_0x00f0;
                                L_0x00e1:
                                    r3 = r8.first_name;
                                    r11 = r8.last_name;
                                    r3 = org.telegram.messenger.AndroidUtilities.generateSearchName(r3, r11, r14);
                                    r6.add(r3);
                                    r4.add(r8);
                                    goto L_0x00f4;
                                L_0x00f0:
                                    r11 = r11 + 1;
                                    r3 = 0;
                                    goto L_0x009d;
                                L_0x00f4:
                                    r7 = r7 + 1;
                                    r3 = 0;
                                    goto L_0x005a;
                                L_0x00f9:
                                    r3 = org.telegram.ui.InviteContactsActivity.InviteAdapter.1.AnonymousClass1.this;
                                    r3 = org.telegram.ui.InviteContactsActivity.InviteAdapter.AnonymousClass1.this;
                                    r3 = org.telegram.ui.InviteContactsActivity.InviteAdapter.this;
                                    r3.updateSearchResults(r4, r6);
                                    return;
                                    */
                                    throw new UnsupportedOperationException("Method not decompiled: org.telegram.ui.InviteContactsActivity.InviteAdapter.1.1.1.run():void");
                                }
                            });
                        }
                    });
                }
            }, 200, 300);
        }

        private void updateSearchResults(final ArrayList<Contact> users, final ArrayList<CharSequence> names) {
            AndroidUtilities.runOnUIThread(new Runnable() {
                public void run() {
                    InviteAdapter.this.searchResult = users;
                    InviteAdapter.this.searchResultNames = names;
                    InviteAdapter.this.notifyDataSetChanged();
                }
            });
        }

        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
            int count = getItemCount();
            boolean z = false;
            InviteContactsActivity.this.emptyView.setVisibility(count == 1 ? 0 : 4);
            GroupCreateDividerItemDecoration access$3100 = InviteContactsActivity.this.decoration;
            if (count == 1) {
                z = true;
            }
            access$3100.setSingle(z);
        }
    }

    public boolean onFragmentCreate() {
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.contactsImported);
        fetchContacts();
        if (!UserConfig.getInstance(this.currentAccount).contactsReimported) {
            ContactsController.getInstance(this.currentAccount).forceImportContacts();
            UserConfig.getInstance(this.currentAccount).contactsReimported = true;
            UserConfig.getInstance(this.currentAccount).saveConfig(false);
        }
        return super.onFragmentCreate();
    }

    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.contactsImported);
    }

    public void onClick(View v) {
        GroupCreateSpan span = (GroupCreateSpan) v;
        if (span.isDeleting()) {
            this.currentDeletingSpan = null;
            this.spansContainer.removeSpan(span);
            updateHint();
            checkVisibleRows();
            return;
        }
        if (this.currentDeletingSpan != null) {
            this.currentDeletingSpan.cancelDeleteAnimation();
        }
        this.currentDeletingSpan = span;
        span.startDeleteAnimation();
    }

    public View createView(Context context) {
        Context context2 = context;
        this.searching = false;
        this.searchWas = false;
        this.allSpans.clear();
        this.selectedContacts.clear();
        this.currentDeletingSpan = null;
        this.actionBar.setBackButtonImage(R.drawable.ic_ab_back);
        this.actionBar.setAllowOverlayTitle(true);
        this.actionBar.setTitle(LocaleController.getString("InviteFriends", R.string.InviteFriends));
        this.actionBar.setActionBarMenuOnItemClick(new ActionBarMenuOnItemClick() {
            public void onItemClick(int id) {
                if (id == -1) {
                    InviteContactsActivity.this.finishFragment();
                }
            }
        });
        this.fragmentView = new ViewGroup(context2) {
            protected void onMeasure(int r1, int r2) {
                /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.ui.InviteContactsActivity.2.onMeasure(int, int):void
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
                r0 = android.view.View.MeasureSpec.getSize(r10);
                r1 = android.view.View.MeasureSpec.getSize(r11);
                r9.setMeasuredDimension(r0, r1);
                r2 = org.telegram.messenger.AndroidUtilities.isTablet();
                if (r2 != 0) goto L_0x001b;
            L_0x0011:
                if (r1 <= r0) goto L_0x0014;
            L_0x0013:
                goto L_0x001b;
            L_0x0014:
                r2 = 1113587712; // 0x42600000 float:56.0 double:5.50185432E-315;
                r2 = org.telegram.messenger.AndroidUtilities.dp(r2);
                goto L_0x0021;
            L_0x001b:
                r2 = 1125122048; // 0x43100000 float:144.0 double:5.558841513E-315;
                r2 = org.telegram.messenger.AndroidUtilities.dp(r2);
                r3 = org.telegram.ui.InviteContactsActivity.this;
                r3 = r3.infoTextView;
                r4 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
                r5 = android.view.View.MeasureSpec.makeMeasureSpec(r0, r4);
                r6 = -2147483648; // 0xffffffff80000000 float:-0.0 double:NaN;
                r7 = android.view.View.MeasureSpec.makeMeasureSpec(r2, r6);
                r3.measure(r5, r7);
                r3 = org.telegram.ui.InviteContactsActivity.this;
                r3 = r3.counterView;
                r5 = android.view.View.MeasureSpec.makeMeasureSpec(r0, r4);
                r7 = 1111490560; // 0x42400000 float:48.0 double:5.491493014E-315;
                r7 = org.telegram.messenger.AndroidUtilities.dp(r7);
                r7 = android.view.View.MeasureSpec.makeMeasureSpec(r7, r4);
                r3.measure(r5, r7);
                r3 = org.telegram.ui.InviteContactsActivity.this;
                r3 = r3.infoTextView;
                r3 = r3.getVisibility();
                if (r3 != 0) goto L_0x0065;
                r3 = org.telegram.ui.InviteContactsActivity.this;
                r3 = r3.infoTextView;
                r3 = r3.getMeasuredHeight();
                goto L_0x006f;
                r3 = org.telegram.ui.InviteContactsActivity.this;
                r3 = r3.counterView;
                r3 = r3.getMeasuredHeight();
                r5 = org.telegram.ui.InviteContactsActivity.this;
                r5 = r5.scrollView;
                r7 = android.view.View.MeasureSpec.makeMeasureSpec(r0, r4);
                r6 = android.view.View.MeasureSpec.makeMeasureSpec(r2, r6);
                r5.measure(r7, r6);
                r5 = org.telegram.ui.InviteContactsActivity.this;
                r5 = r5.listView;
                r6 = android.view.View.MeasureSpec.makeMeasureSpec(r0, r4);
                r7 = org.telegram.ui.InviteContactsActivity.this;
                r7 = r7.scrollView;
                r7 = r7.getMeasuredHeight();
                r7 = r1 - r7;
                r7 = r7 - r3;
                r7 = android.view.View.MeasureSpec.makeMeasureSpec(r7, r4);
                r5.measure(r6, r7);
                r5 = org.telegram.ui.InviteContactsActivity.this;
                r5 = r5.emptyView;
                r6 = android.view.View.MeasureSpec.makeMeasureSpec(r0, r4);
                r7 = org.telegram.ui.InviteContactsActivity.this;
                r7 = r7.scrollView;
                r7 = r7.getMeasuredHeight();
                r7 = r1 - r7;
                r8 = 1116733440; // 0x42900000 float:72.0 double:5.517396283E-315;
                r8 = org.telegram.messenger.AndroidUtilities.dp(r8);
                r7 = r7 - r8;
                r4 = android.view.View.MeasureSpec.makeMeasureSpec(r7, r4);
                r5.measure(r6, r4);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: org.telegram.ui.InviteContactsActivity.2.onMeasure(int, int):void");
            }

            protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
                InviteContactsActivity.this.scrollView.layout(0, 0, InviteContactsActivity.this.scrollView.getMeasuredWidth(), InviteContactsActivity.this.scrollView.getMeasuredHeight());
                InviteContactsActivity.this.listView.layout(0, InviteContactsActivity.this.scrollView.getMeasuredHeight(), InviteContactsActivity.this.listView.getMeasuredWidth(), InviteContactsActivity.this.scrollView.getMeasuredHeight() + InviteContactsActivity.this.listView.getMeasuredHeight());
                InviteContactsActivity.this.emptyView.layout(0, InviteContactsActivity.this.scrollView.getMeasuredHeight() + AndroidUtilities.dp(72.0f), InviteContactsActivity.this.emptyView.getMeasuredWidth(), InviteContactsActivity.this.scrollView.getMeasuredHeight() + InviteContactsActivity.this.emptyView.getMeasuredHeight());
                int y = (bottom - top) - InviteContactsActivity.this.infoTextView.getMeasuredHeight();
                InviteContactsActivity.this.infoTextView.layout(0, y, InviteContactsActivity.this.infoTextView.getMeasuredWidth(), InviteContactsActivity.this.infoTextView.getMeasuredHeight() + y);
                int y2 = (bottom - top) - InviteContactsActivity.this.counterView.getMeasuredHeight();
                InviteContactsActivity.this.counterView.layout(0, y2, InviteContactsActivity.this.counterView.getMeasuredWidth(), InviteContactsActivity.this.counterView.getMeasuredHeight() + y2);
            }

            protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
                boolean result = super.drawChild(canvas, child, drawingTime);
                if (child == InviteContactsActivity.this.listView || child == InviteContactsActivity.this.emptyView) {
                    InviteContactsActivity.this.parentLayout.drawHeaderShadow(canvas, InviteContactsActivity.this.scrollView.getMeasuredHeight());
                }
                return result;
            }
        };
        ViewGroup frameLayout = this.fragmentView;
        this.scrollView = new ScrollView(context2) {
            public boolean requestChildRectangleOnScreen(View child, Rect rectangle, boolean immediate) {
                if (InviteContactsActivity.this.ignoreScrollEvent) {
                    InviteContactsActivity.this.ignoreScrollEvent = false;
                    return false;
                }
                rectangle.offset(child.getLeft() - child.getScrollX(), child.getTop() - child.getScrollY());
                rectangle.top += InviteContactsActivity.this.fieldY + AndroidUtilities.dp(20.0f);
                rectangle.bottom += InviteContactsActivity.this.fieldY + AndroidUtilities.dp(50.0f);
                return super.requestChildRectangleOnScreen(child, rectangle, immediate);
            }
        };
        this.scrollView.setVerticalScrollBarEnabled(false);
        AndroidUtilities.setScrollViewEdgeEffectColor(this.scrollView, Theme.getColor(Theme.key_windowBackgroundWhite));
        frameLayout.addView(this.scrollView);
        this.spansContainer = new SpansContainer(context2);
        this.scrollView.addView(this.spansContainer, LayoutHelper.createFrame(-1, -2.0f));
        this.editText = new EditTextBoldCursor(context2) {
            public boolean onTouchEvent(MotionEvent event) {
                if (InviteContactsActivity.this.currentDeletingSpan != null) {
                    InviteContactsActivity.this.currentDeletingSpan.cancelDeleteAnimation();
                    InviteContactsActivity.this.currentDeletingSpan = null;
                }
                return super.onTouchEvent(event);
            }
        };
        this.editText.setTextSize(1, 18.0f);
        this.editText.setHintColor(Theme.getColor(Theme.key_groupcreate_hintText));
        this.editText.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText));
        this.editText.setCursorColor(Theme.getColor(Theme.key_groupcreate_cursor));
        this.editText.setCursorWidth(1.5f);
        this.editText.setInputType(655536);
        this.editText.setSingleLine(true);
        this.editText.setBackgroundDrawable(null);
        this.editText.setVerticalScrollBarEnabled(false);
        this.editText.setHorizontalScrollBarEnabled(false);
        this.editText.setTextIsSelectable(false);
        this.editText.setPadding(0, 0, 0, 0);
        this.editText.setImeOptions(268435462);
        this.editText.setGravity((LocaleController.isRTL ? 5 : 3) | 16);
        r0.spansContainer.addView(r0.editText);
        r0.editText.setHintText(LocaleController.getString("SearchFriends", R.string.SearchFriends));
        r0.editText.setCustomSelectionActionModeCallback(new Callback() {
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            public void onDestroyActionMode(ActionMode mode) {
            }

            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return false;
            }
        });
        r0.editText.setOnKeyListener(new OnKeyListener() {
            private boolean wasEmpty;

            public boolean onKey(View v, int keyCode, KeyEvent event) {
                boolean z = true;
                if (event.getAction() == 0) {
                    if (InviteContactsActivity.this.editText.length() != 0) {
                        z = false;
                    }
                    this.wasEmpty = z;
                } else if (event.getAction() == 1 && this.wasEmpty && !InviteContactsActivity.this.allSpans.isEmpty()) {
                    InviteContactsActivity.this.spansContainer.removeSpan((GroupCreateSpan) InviteContactsActivity.this.allSpans.get(InviteContactsActivity.this.allSpans.size() - 1));
                    InviteContactsActivity.this.updateHint();
                    InviteContactsActivity.this.checkVisibleRows();
                    return true;
                }
                return false;
            }
        });
        r0.editText.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void afterTextChanged(Editable editable) {
                if (InviteContactsActivity.this.editText.length() != 0) {
                    InviteContactsActivity.this.searching = true;
                    InviteContactsActivity.this.searchWas = true;
                    InviteContactsActivity.this.adapter.setSearching(true);
                    InviteContactsActivity.this.adapter.searchDialogs(InviteContactsActivity.this.editText.getText().toString());
                    InviteContactsActivity.this.listView.setFastScrollVisible(false);
                    InviteContactsActivity.this.listView.setVerticalScrollBarEnabled(true);
                    InviteContactsActivity.this.emptyView.setText(LocaleController.getString("NoResult", R.string.NoResult));
                    return;
                }
                InviteContactsActivity.this.closeSearch();
            }
        });
        r0.emptyView = new EmptyTextProgressView(context2);
        if (ContactsController.getInstance(r0.currentAccount).isLoadingContacts()) {
            r0.emptyView.showProgress();
        } else {
            r0.emptyView.showTextView();
        }
        r0.emptyView.setText(LocaleController.getString("NoContacts", R.string.NoContacts));
        frameLayout.addView(r0.emptyView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context2, 1, false);
        r0.listView = new RecyclerListView(context2);
        r0.listView.setEmptyView(r0.emptyView);
        RecyclerListView recyclerListView = r0.listView;
        Adapter inviteAdapter = new InviteAdapter(context2);
        r0.adapter = inviteAdapter;
        recyclerListView.setAdapter(inviteAdapter);
        r0.listView.setLayoutManager(linearLayoutManager);
        r0.listView.setVerticalScrollBarEnabled(true);
        r0.listView.setVerticalScrollbarPosition(LocaleController.isRTL ? 1 : 2);
        recyclerListView = r0.listView;
        ItemDecoration groupCreateDividerItemDecoration = new GroupCreateDividerItemDecoration();
        r0.decoration = groupCreateDividerItemDecoration;
        recyclerListView.addItemDecoration(groupCreateDividerItemDecoration);
        frameLayout.addView(r0.listView);
        r0.listView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(View view, int position) {
                boolean z = false;
                if (position == 0 && !InviteContactsActivity.this.searching) {
                    try {
                        Intent intent = new Intent("android.intent.action.SEND");
                        intent.setType("text/plain");
                        String text = ContactsController.getInstance(InviteContactsActivity.this.currentAccount).getInviteText(0);
                        intent.putExtra("android.intent.extra.TEXT", text);
                        InviteContactsActivity.this.getParentActivity().startActivityForResult(Intent.createChooser(intent, text), 500);
                    } catch (Throwable e) {
                        FileLog.e(e);
                    }
                } else if (view instanceof InviteUserCell) {
                    InviteUserCell cell = (InviteUserCell) view;
                    Contact contact = cell.getContact();
                    if (contact != null) {
                        boolean containsKey = InviteContactsActivity.this.selectedContacts.containsKey(contact.key);
                        boolean exists = containsKey;
                        if (containsKey) {
                            InviteContactsActivity.this.spansContainer.removeSpan((GroupCreateSpan) InviteContactsActivity.this.selectedContacts.get(contact.key));
                        } else {
                            GroupCreateSpan span = new GroupCreateSpan(InviteContactsActivity.this.editText.getContext(), contact);
                            InviteContactsActivity.this.spansContainer.addSpan(span);
                            span.setOnClickListener(InviteContactsActivity.this);
                        }
                        InviteContactsActivity.this.updateHint();
                        if (!InviteContactsActivity.this.searching) {
                            if (!InviteContactsActivity.this.searchWas) {
                                if (!exists) {
                                    z = true;
                                }
                                cell.setChecked(z, true);
                                if (InviteContactsActivity.this.editText.length() > 0) {
                                    InviteContactsActivity.this.editText.setText(null);
                                }
                            }
                        }
                        AndroidUtilities.showKeyboard(InviteContactsActivity.this.editText);
                        if (InviteContactsActivity.this.editText.length() > 0) {
                            InviteContactsActivity.this.editText.setText(null);
                        }
                    }
                }
            }
        });
        r0.listView.setOnScrollListener(new OnScrollListener() {
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == 1) {
                    AndroidUtilities.hideKeyboard(InviteContactsActivity.this.editText);
                }
            }
        });
        r0.infoTextView = new TextView(context2);
        r0.infoTextView.setBackgroundColor(Theme.getColor(Theme.key_contacts_inviteBackground));
        r0.infoTextView.setTextColor(Theme.getColor(Theme.key_contacts_inviteText));
        r0.infoTextView.setGravity(17);
        r0.infoTextView.setText(LocaleController.getString("InviteFriendsHelp", R.string.InviteFriendsHelp));
        r0.infoTextView.setTextSize(1, 13.0f);
        r0.infoTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        r0.infoTextView.setPadding(AndroidUtilities.dp(17.0f), AndroidUtilities.dp(9.0f), AndroidUtilities.dp(17.0f), AndroidUtilities.dp(9.0f));
        frameLayout.addView(r0.infoTextView, LayoutHelper.createFrame(-1, -2, 83));
        r0.counterView = new FrameLayout(context2);
        r0.counterView.setBackgroundColor(Theme.getColor(Theme.key_contacts_inviteBackground));
        r0.counterView.setVisibility(4);
        frameLayout.addView(r0.counterView, LayoutHelper.createFrame(-1, 48, 83));
        r0.counterView.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                try {
                    StringBuilder builder = new StringBuilder();
                    int num = 0;
                    for (int a = 0; a < InviteContactsActivity.this.allSpans.size(); a++) {
                        Contact contact = ((GroupCreateSpan) InviteContactsActivity.this.allSpans.get(a)).getContact();
                        if (builder.length() != 0) {
                            builder.append(';');
                        }
                        builder.append((String) contact.phones.get(0));
                        if (a == 0 && InviteContactsActivity.this.allSpans.size() == 1) {
                            num = contact.imported;
                        }
                    }
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("smsto:");
                    stringBuilder.append(builder.toString());
                    Intent intent = new Intent("android.intent.action.SENDTO", Uri.parse(stringBuilder.toString()));
                    intent.putExtra("sms_body", ContactsController.getInstance(InviteContactsActivity.this.currentAccount).getInviteText(num));
                    InviteContactsActivity.this.getParentActivity().startActivityForResult(intent, 500);
                    MediaController.getInstance().startSmsObserver();
                } catch (Throwable e) {
                    FileLog.e(e);
                }
                InviteContactsActivity.this.finishFragment();
            }
        });
        LinearLayout linearLayout = new LinearLayout(context2);
        linearLayout.setOrientation(0);
        r0.counterView.addView(linearLayout, LayoutHelper.createFrame(-2, -1, 17));
        r0.counterTextView = new TextView(context2);
        r0.counterTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        r0.counterTextView.setTextSize(1, 14.0f);
        r0.counterTextView.setTextColor(Theme.getColor(Theme.key_contacts_inviteBackground));
        r0.counterTextView.setGravity(17);
        r0.counterTextView.setBackgroundDrawable(Theme.createRoundRectDrawable(AndroidUtilities.dp(10.0f), -1));
        r0.counterTextView.setMinWidth(AndroidUtilities.dp(20.0f));
        r0.counterTextView.setPadding(AndroidUtilities.dp(6.0f), 0, AndroidUtilities.dp(6.0f), AndroidUtilities.dp(1.0f));
        linearLayout.addView(r0.counterTextView, LayoutHelper.createLinear(-2, 20, 16, 0, 0, 10, 0));
        r0.textView = new TextView(context2);
        r0.textView.setTextSize(1, 14.0f);
        r0.textView.setTextColor(Theme.getColor(Theme.key_contacts_inviteText));
        r0.textView.setGravity(17);
        r0.textView.setCompoundDrawablePadding(AndroidUtilities.dp(8.0f));
        r0.textView.setText(LocaleController.getString("InviteToTelegram", R.string.InviteToTelegram).toUpperCase());
        r0.textView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        linearLayout.addView(r0.textView, LayoutHelper.createLinear(-2, -2, 16));
        updateHint();
        r0.adapter.notifyDataSetChanged();
        return r0.fragmentView;
    }

    public void onResume() {
        super.onResume();
        if (this.editText != null) {
            this.editText.requestFocus();
        }
    }

    public void didReceivedNotification(int id, int account, Object... args) {
        if (id == NotificationCenter.contactsImported) {
            fetchContacts();
        }
    }

    public void setContainerHeight(int value) {
        this.containerHeight = value;
        if (this.spansContainer != null) {
            this.spansContainer.requestLayout();
        }
    }

    public int getContainerHeight() {
        return this.containerHeight;
    }

    private void checkVisibleRows() {
        int count = this.listView.getChildCount();
        for (int a = 0; a < count; a++) {
            View child = this.listView.getChildAt(a);
            if (child instanceof InviteUserCell) {
                InviteUserCell cell = (InviteUserCell) child;
                Contact contact = cell.getContact();
                if (contact != null) {
                    cell.setChecked(this.selectedContacts.containsKey(contact.key), true);
                }
            }
        }
    }

    private void updateHint() {
        if (this.selectedContacts.isEmpty()) {
            this.infoTextView.setVisibility(0);
            this.counterView.setVisibility(4);
            return;
        }
        this.infoTextView.setVisibility(4);
        this.counterView.setVisibility(0);
        this.counterTextView.setText(String.format("%d", new Object[]{Integer.valueOf(this.selectedContacts.size())}));
    }

    private void closeSearch() {
        this.searching = false;
        this.searchWas = false;
        this.adapter.setSearching(false);
        this.adapter.searchDialogs(null);
        this.listView.setFastScrollVisible(true);
        this.listView.setVerticalScrollBarEnabled(false);
        this.emptyView.setText(LocaleController.getString("NoContacts", R.string.NoContacts));
    }

    private void fetchContacts() {
        this.phoneBookContacts = new ArrayList(ContactsController.getInstance(this.currentAccount).phoneBookContacts);
        Collections.sort(this.phoneBookContacts, new Comparator<Contact>() {
            public int compare(Contact o1, Contact o2) {
                if (o1.imported > o2.imported) {
                    return -1;
                }
                if (o1.imported < o2.imported) {
                    return 1;
                }
                return 0;
            }
        });
        if (this.emptyView != null) {
            this.emptyView.showTextView();
        }
        if (this.adapter != null) {
            this.adapter.notifyDataSetChanged();
        }
    }

    public ThemeDescription[] getThemeDescriptions() {
        ThemeDescriptionDelegate сellDelegate = new ThemeDescriptionDelegate() {
            public void didSetColor() {
                if (InviteContactsActivity.this.listView != null) {
                    int count = InviteContactsActivity.this.listView.getChildCount();
                    for (int a = 0; a < count; a++) {
                        View child = InviteContactsActivity.this.listView.getChildAt(a);
                        if (child instanceof InviteUserCell) {
                            ((InviteUserCell) child).update(0);
                        }
                    }
                }
            }
        };
        r9 = new ThemeDescription[44];
        r9[11] = new ThemeDescription(this.listView, 0, new Class[]{View.class}, Theme.dividerPaint, null, null, Theme.key_divider);
        r9[12] = new ThemeDescription(this.emptyView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, Theme.key_emptyListPlaceholder);
        r9[13] = new ThemeDescription(this.emptyView, ThemeDescription.FLAG_PROGRESSBAR, null, null, null, null, Theme.key_progressCircle);
        r9[14] = new ThemeDescription(this.editText, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, Theme.key_windowBackgroundWhiteBlackText);
        r9[15] = new ThemeDescription(this.editText, ThemeDescription.FLAG_HINTTEXTCOLOR, null, null, null, null, Theme.key_groupcreate_hintText);
        r9[16] = new ThemeDescription(this.editText, ThemeDescription.FLAG_CURSORCOLOR, null, null, null, null, Theme.key_groupcreate_cursor);
        r9[17] = new ThemeDescription(this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{GroupCreateSectionCell.class}, null, null, null, Theme.key_graySection);
        r9[18] = new ThemeDescription(this.listView, 0, new Class[]{GroupCreateSectionCell.class}, new String[]{"drawable"}, null, null, null, Theme.key_groupcreate_sectionShadow);
        r9[19] = new ThemeDescription(this.listView, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{GroupCreateSectionCell.class}, new String[]{"textView"}, null, null, null, Theme.key_groupcreate_sectionText);
        r9[20] = new ThemeDescription(this.listView, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{InviteUserCell.class}, new String[]{"textView"}, null, null, null, Theme.key_groupcreate_sectionText);
        r9[21] = new ThemeDescription(this.listView, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{InviteUserCell.class}, new String[]{"checkBox"}, null, null, null, Theme.key_groupcreate_checkbox);
        r9[22] = new ThemeDescription(this.listView, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{InviteUserCell.class}, new String[]{"checkBox"}, null, null, null, Theme.key_groupcreate_checkboxCheck);
        r9[23] = new ThemeDescription(this.listView, ThemeDescription.FLAG_TEXTCOLOR | ThemeDescription.FLAG_CHECKTAG, new Class[]{InviteUserCell.class}, new String[]{"statusTextView"}, null, null, null, Theme.key_groupcreate_onlineText);
        r9[24] = new ThemeDescription(this.listView, ThemeDescription.FLAG_TEXTCOLOR | ThemeDescription.FLAG_CHECKTAG, new Class[]{InviteUserCell.class}, new String[]{"statusTextView"}, null, null, null, Theme.key_groupcreate_offlineText);
        r9[25] = new ThemeDescription(this.listView, 0, new Class[]{InviteUserCell.class}, null, new Drawable[]{Theme.avatar_photoDrawable, Theme.avatar_broadcastDrawable, Theme.avatar_savedDrawable}, null, Theme.key_avatar_text);
        r9[26] = new ThemeDescription(null, 0, null, null, null, сellDelegate, Theme.key_avatar_backgroundRed);
        r9[27] = new ThemeDescription(null, 0, null, null, null, сellDelegate, Theme.key_avatar_backgroundOrange);
        ThemeDescriptionDelegate themeDescriptionDelegate = сellDelegate;
        r9[28] = new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_avatar_backgroundViolet);
        r9[29] = new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_avatar_backgroundGreen);
        r9[30] = new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_avatar_backgroundCyan);
        r9[31] = new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_avatar_backgroundBlue);
        r9[32] = new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_avatar_backgroundPink);
        r9[33] = new ThemeDescription(this.listView, 0, new Class[]{InviteTextCell.class}, new String[]{"textView"}, null, null, null, Theme.key_windowBackgroundWhiteBlackText);
        r9[34] = new ThemeDescription(this.listView, 0, new Class[]{InviteTextCell.class}, new String[]{"imageView"}, null, null, null, Theme.key_windowBackgroundWhiteGrayIcon);
        r9[35] = new ThemeDescription(this.spansContainer, 0, new Class[]{GroupCreateSpan.class}, null, null, null, Theme.key_avatar_backgroundGroupCreateSpanBlue);
        r9[36] = new ThemeDescription(this.spansContainer, 0, new Class[]{GroupCreateSpan.class}, null, null, null, Theme.key_groupcreate_spanBackground);
        r9[37] = new ThemeDescription(this.spansContainer, 0, new Class[]{GroupCreateSpan.class}, null, null, null, Theme.key_groupcreate_spanText);
        r9[38] = new ThemeDescription(this.spansContainer, 0, new Class[]{GroupCreateSpan.class}, null, null, null, Theme.key_avatar_backgroundBlue);
        r9[39] = new ThemeDescription(this.infoTextView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, Theme.key_contacts_inviteText);
        r9[40] = new ThemeDescription(this.infoTextView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_contacts_inviteBackground);
        r9[41] = new ThemeDescription(this.counterView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, Theme.key_contacts_inviteBackground);
        r9[42] = new ThemeDescription(this.counterTextView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, Theme.key_contacts_inviteBackground);
        r9[43] = new ThemeDescription(this.textView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, Theme.key_contacts_inviteText);
        return r9;
    }
}
