package org.telegram.ui.Components;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.os.Build.VERSION;
import android.os.Looper;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.Bitmaps;
import org.telegram.messenger.DispatchQueue;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.beta.R;
import org.telegram.tgnet.TLRPC.Document;
import org.telegram.tgnet.TLRPC.DocumentAttribute;
import org.telegram.tgnet.TLRPC.InputDocument;
import org.telegram.tgnet.TLRPC.TL_documentAttributeSticker;
import org.telegram.tgnet.TLRPC.TL_inputDocument;
import org.telegram.tgnet.TLRPC.TL_maskCoords;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.ActionBarPopupWindow;
import org.telegram.ui.ActionBar.ActionBarPopupWindow.ActionBarPopupWindowLayout;
import org.telegram.ui.ActionBar.ActionBarPopupWindow.OnDispatchKeyEventListener;
import org.telegram.ui.ActionBar.AlertDialog.Builder;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.Paint.Brush;
import org.telegram.ui.Components.Paint.Brush.Elliptical;
import org.telegram.ui.Components.Paint.Brush.Neon;
import org.telegram.ui.Components.Paint.Brush.Radial;
import org.telegram.ui.Components.Paint.Painting;
import org.telegram.ui.Components.Paint.PhotoFace;
import org.telegram.ui.Components.Paint.RenderView;
import org.telegram.ui.Components.Paint.RenderView.RenderViewDelegate;
import org.telegram.ui.Components.Paint.Swatch;
import org.telegram.ui.Components.Paint.UndoStore;
import org.telegram.ui.Components.Paint.UndoStore.UndoStoreDelegate;
import org.telegram.ui.Components.Paint.Views.ColorPicker;
import org.telegram.ui.Components.Paint.Views.ColorPicker.ColorPickerDelegate;
import org.telegram.ui.Components.Paint.Views.EditTextOutline;
import org.telegram.ui.Components.Paint.Views.EntitiesContainerView;
import org.telegram.ui.Components.Paint.Views.EntitiesContainerView.EntitiesContainerViewDelegate;
import org.telegram.ui.Components.Paint.Views.EntityView;
import org.telegram.ui.Components.Paint.Views.EntityView.EntityViewDelegate;
import org.telegram.ui.Components.Paint.Views.StickerView;
import org.telegram.ui.Components.Paint.Views.TextPaintView;
import org.telegram.ui.Components.StickerMasksView.Listener;
import org.telegram.ui.PhotoViewer;

@SuppressLint({"NewApi"})
public class PhotoPaintView extends FrameLayout implements EntityViewDelegate {
    private static final int gallery_menu_done = 1;
    private Bitmap bitmapToEdit;
    private Brush[] brushes = new Brush[]{new Radial(), new Elliptical(), new Neon()};
    private TextView cancelTextView;
    private ColorPicker colorPicker;
    private Animator colorPickerAnimator;
    int currentBrush;
    private EntityView currentEntityView;
    private FrameLayout curtainView;
    private FrameLayout dimView;
    private TextView doneTextView;
    private Point editedTextPosition;
    private float editedTextRotation;
    private float editedTextScale;
    private boolean editingText;
    private EntitiesContainerView entitiesView;
    private ArrayList<PhotoFace> faces;
    private String initialText;
    private int orientation;
    private ImageView paintButton;
    private Size paintingSize;
    private boolean pickingSticker;
    private ActionBarPopupWindowLayout popupLayout;
    private Rect popupRect;
    private ActionBarPopupWindow popupWindow;
    private DispatchQueue queue = new DispatchQueue("Paint");
    private RenderView renderView;
    private boolean selectedStroke = true;
    private FrameLayout selectionContainerView;
    private StickerMasksView stickersView;
    private FrameLayout textDimView;
    private FrameLayout toolsView;
    private UndoStore undoStore;

    private class StickerPosition {
        private float angle;
        private Point position;
        private float scale;

        StickerPosition(Point position, float scale, float angle) {
            this.position = position;
            this.scale = scale;
            this.angle = angle;
        }
    }

    public PhotoPaintView(Context context, Bitmap bitmap, int rotation) {
        Context context2 = context;
        Bitmap bitmap2 = bitmap;
        super(context);
        this.bitmapToEdit = bitmap2;
        this.orientation = rotation;
        this.undoStore = new UndoStore();
        this.undoStore.setDelegate(new UndoStoreDelegate() {
            public void historyChanged() {
                PhotoPaintView.this.colorPicker.setUndoEnabled(PhotoPaintView.this.undoStore.canUndo());
            }
        });
        this.curtainView = new FrameLayout(context2);
        this.curtainView.setBackgroundColor(Theme.ACTION_BAR_VIDEO_EDIT_COLOR);
        this.curtainView.setVisibility(4);
        addView(this.curtainView);
        this.renderView = new RenderView(context2, new Painting(getPaintingSize()), bitmap2, this.orientation);
        this.renderView.setDelegate(new RenderViewDelegate() {
            public void onBeganDrawing() {
                if (PhotoPaintView.this.currentEntityView != null) {
                    PhotoPaintView.this.selectEntity(null);
                }
            }

            public void onFinishedDrawing(boolean moved) {
                PhotoPaintView.this.colorPicker.setUndoEnabled(PhotoPaintView.this.undoStore.canUndo());
            }

            public boolean shouldDraw() {
                boolean draw = PhotoPaintView.this.currentEntityView == null;
                if (!draw) {
                    PhotoPaintView.this.selectEntity(null);
                }
                return draw;
            }
        });
        this.renderView.setUndoStore(this.undoStore);
        this.renderView.setQueue(this.queue);
        this.renderView.setVisibility(4);
        this.renderView.setBrush(this.brushes[0]);
        addView(this.renderView, LayoutHelper.createFrame(-1, -1, 51));
        this.entitiesView = new EntitiesContainerView(context2, new EntitiesContainerViewDelegate() {
            public boolean shouldReceiveTouches() {
                return PhotoPaintView.this.textDimView.getVisibility() != 0;
            }

            public EntityView onSelectedEntityRequest() {
                return PhotoPaintView.this.currentEntityView;
            }

            public void onEntityDeselect() {
                PhotoPaintView.this.selectEntity(null);
            }
        });
        this.entitiesView.setPivotX(0.0f);
        this.entitiesView.setPivotY(0.0f);
        addView(this.entitiesView);
        this.dimView = new FrameLayout(context2);
        this.dimView.setAlpha(0.0f);
        this.dimView.setBackgroundColor(1711276032);
        this.dimView.setVisibility(8);
        addView(this.dimView);
        this.textDimView = new FrameLayout(context2);
        this.textDimView.setAlpha(0.0f);
        this.textDimView.setBackgroundColor(1711276032);
        this.textDimView.setVisibility(8);
        this.textDimView.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                PhotoPaintView.this.closeTextEnter(true);
            }
        });
        this.selectionContainerView = new FrameLayout(context2) {
            public boolean onTouchEvent(MotionEvent event) {
                return false;
            }
        };
        addView(this.selectionContainerView);
        this.colorPicker = new ColorPicker(context2);
        addView(this.colorPicker);
        this.colorPicker.setDelegate(new ColorPickerDelegate() {
            public void onBeganColorPicking() {
                if (!(PhotoPaintView.this.currentEntityView instanceof TextPaintView)) {
                    PhotoPaintView.this.setDimVisibility(true);
                }
            }

            public void onColorValueChanged() {
                PhotoPaintView.this.setCurrentSwatch(PhotoPaintView.this.colorPicker.getSwatch(), false);
            }

            public void onFinishedColorPicking() {
                PhotoPaintView.this.setCurrentSwatch(PhotoPaintView.this.colorPicker.getSwatch(), false);
                if (!(PhotoPaintView.this.currentEntityView instanceof TextPaintView)) {
                    PhotoPaintView.this.setDimVisibility(false);
                }
            }

            public void onSettingsPressed() {
                if (PhotoPaintView.this.currentEntityView == null) {
                    PhotoPaintView.this.showBrushSettings();
                } else if (PhotoPaintView.this.currentEntityView instanceof StickerView) {
                    PhotoPaintView.this.mirrorSticker();
                } else if (PhotoPaintView.this.currentEntityView instanceof TextPaintView) {
                    PhotoPaintView.this.showTextSettings();
                }
            }

            public void onUndoPressed() {
                PhotoPaintView.this.undoStore.undo();
            }
        });
        this.toolsView = new FrameLayout(context2);
        this.toolsView.setBackgroundColor(Theme.ACTION_BAR_VIDEO_EDIT_COLOR);
        addView(this.toolsView, LayoutHelper.createFrame(-1, 48, 83));
        this.cancelTextView = new TextView(context2);
        this.cancelTextView.setTextSize(1, 14.0f);
        this.cancelTextView.setTextColor(-1);
        this.cancelTextView.setGravity(17);
        this.cancelTextView.setBackgroundDrawable(Theme.createSelectorDrawable(Theme.ACTION_BAR_PICKER_SELECTOR_COLOR, 0));
        this.cancelTextView.setPadding(AndroidUtilities.dp(20.0f), 0, AndroidUtilities.dp(20.0f), 0);
        this.cancelTextView.setText(LocaleController.getString("Cancel", R.string.Cancel).toUpperCase());
        this.cancelTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        this.toolsView.addView(this.cancelTextView, LayoutHelper.createFrame(-2, -1, 51));
        this.doneTextView = new TextView(context2);
        this.doneTextView.setTextSize(1, 14.0f);
        this.doneTextView.setTextColor(-11420173);
        this.doneTextView.setGravity(17);
        this.doneTextView.setBackgroundDrawable(Theme.createSelectorDrawable(Theme.ACTION_BAR_PICKER_SELECTOR_COLOR, 0));
        this.doneTextView.setPadding(AndroidUtilities.dp(20.0f), 0, AndroidUtilities.dp(20.0f), 0);
        this.doneTextView.setText(LocaleController.getString("Done", R.string.Done).toUpperCase());
        this.doneTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        this.toolsView.addView(this.doneTextView, LayoutHelper.createFrame(-2, -1, 53));
        this.paintButton = new ImageView(context2);
        this.paintButton.setScaleType(ScaleType.CENTER);
        this.paintButton.setImageResource(R.drawable.photo_paint);
        this.paintButton.setBackgroundDrawable(Theme.createSelectorDrawable(Theme.ACTION_BAR_WHITE_SELECTOR_COLOR));
        this.toolsView.addView(this.paintButton, LayoutHelper.createFrame(54, -1.0f, 17, 0.0f, 0.0f, 56.0f, 0.0f));
        this.paintButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                PhotoPaintView.this.selectEntity(null);
            }
        });
        ImageView stickerButton = new ImageView(context2);
        stickerButton.setScaleType(ScaleType.CENTER);
        stickerButton.setImageResource(R.drawable.photo_sticker);
        stickerButton.setBackgroundDrawable(Theme.createSelectorDrawable(Theme.ACTION_BAR_WHITE_SELECTOR_COLOR));
        this.toolsView.addView(stickerButton, LayoutHelper.createFrame(54, -1, 17));
        stickerButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                PhotoPaintView.this.openStickersView();
            }
        });
        ImageView textButton = new ImageView(context2);
        textButton.setScaleType(ScaleType.CENTER);
        textButton.setImageResource(R.drawable.photo_paint_text);
        textButton.setBackgroundDrawable(Theme.createSelectorDrawable(Theme.ACTION_BAR_WHITE_SELECTOR_COLOR));
        this.toolsView.addView(textButton, LayoutHelper.createFrame(54, -1.0f, 17, 56.0f, 0.0f, 0.0f, 0.0f));
        textButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                PhotoPaintView.this.createText();
            }
        });
        this.colorPicker.setUndoEnabled(false);
        setCurrentSwatch(this.colorPicker.getSwatch(), false);
        updateSettingsButton();
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (this.currentEntityView != null) {
            if (this.editingText) {
                closeTextEnter(true);
            } else {
                selectEntity(null);
            }
        }
        return true;
    }

    private Size getPaintingSize() {
        if (this.paintingSize != null) {
            return this.paintingSize;
        }
        float width = (float) (isSidewardOrientation() ? this.bitmapToEdit.getHeight() : this.bitmapToEdit.getWidth());
        float height = (float) (isSidewardOrientation() ? this.bitmapToEdit.getWidth() : this.bitmapToEdit.getHeight());
        Size size = new Size(width, height);
        size.width = 1280.0f;
        size.height = (float) Math.floor((double) ((size.width * height) / width));
        if (size.height > 1280.0f) {
            size.height = 1280.0f;
            size.width = (float) Math.floor((double) ((size.height * width) / height));
        }
        this.paintingSize = size;
        return size;
    }

    private boolean isSidewardOrientation() {
        if (this.orientation % 360 != 90) {
            if (this.orientation % 360 != 270) {
                return false;
            }
        }
        return true;
    }

    private void updateSettingsButton() {
        int resource = R.drawable.photo_paint_brush;
        if (this.currentEntityView != null) {
            if (this.currentEntityView instanceof StickerView) {
                resource = R.drawable.photo_flip;
            } else if (this.currentEntityView instanceof TextPaintView) {
                resource = R.drawable.photo_outline;
            }
            this.paintButton.setImageResource(R.drawable.photo_paint);
            this.paintButton.setColorFilter(null);
        } else {
            this.paintButton.setColorFilter(new PorterDuffColorFilter(-11420173, Mode.MULTIPLY));
            this.paintButton.setImageResource(R.drawable.photo_paint);
        }
        this.colorPicker.setSettingsButtonImage(resource);
    }

    public void init() {
        this.renderView.setVisibility(0);
        detectFaces();
    }

    public void shutdown() {
        this.renderView.shutdown();
        this.entitiesView.setVisibility(8);
        this.selectionContainerView.setVisibility(8);
        this.queue.postRunnable(new Runnable() {
            public void run() {
                Looper looper = Looper.myLooper();
                if (looper != null) {
                    looper.quit();
                }
            }
        });
    }

    public FrameLayout getToolsView() {
        return this.toolsView;
    }

    public TextView getDoneTextView() {
        return this.doneTextView;
    }

    public TextView getCancelTextView() {
        return this.cancelTextView;
    }

    public ColorPicker getColorPicker() {
        return this.colorPicker;
    }

    private boolean hasChanges() {
        if (!this.undoStore.canUndo()) {
            if (this.entitiesView.entitiesCount() <= 0) {
                return false;
            }
        }
        return true;
    }

    public Bitmap getBitmap() {
        Bitmap bitmap = this.renderView.getResultBitmap();
        if (bitmap != null && this.entitiesView.entitiesCount() > 0) {
            Canvas canvas = new Canvas(bitmap);
            for (int i = 0; i < this.entitiesView.getChildCount(); i++) {
                View v = this.entitiesView.getChildAt(i);
                canvas.save();
                if (v instanceof EntityView) {
                    EntityView entity = (EntityView) v;
                    canvas.translate(entity.getPosition().x, entity.getPosition().y);
                    canvas.scale(v.getScaleX(), v.getScaleY());
                    canvas.rotate(v.getRotation());
                    canvas.translate((float) ((-entity.getWidth()) / 2), (float) ((-entity.getHeight()) / 2));
                    if (v instanceof TextPaintView) {
                        Bitmap b = Bitmaps.createBitmap(v.getWidth(), v.getHeight(), Config.ARGB_8888);
                        Canvas c = new Canvas(b);
                        v.draw(c);
                        canvas.drawBitmap(b, null, new Rect(0, 0, b.getWidth(), b.getHeight()), null);
                        try {
                            c.setBitmap(null);
                        } catch (Throwable e) {
                            FileLog.e(e);
                        }
                        b.recycle();
                    } else {
                        v.draw(canvas);
                    }
                }
                canvas.restore();
            }
        }
        return bitmap;
    }

    public void maybeShowDismissalAlert(PhotoViewer photoViewer, Activity parentActivity, final Runnable okRunnable) {
        if (this.editingText) {
            closeTextEnter(false);
        } else if (this.pickingSticker) {
            closeStickersView();
        } else {
            if (!hasChanges()) {
                okRunnable.run();
            } else if (parentActivity != null) {
                Builder builder = new Builder((Context) parentActivity);
                builder.setMessage(LocaleController.getString("DiscardChanges", R.string.DiscardChanges));
                builder.setTitle(LocaleController.getString("AppName", R.string.AppName));
                builder.setPositiveButton(LocaleController.getString("OK", R.string.OK), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        okRunnable.run();
                    }
                });
                builder.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null);
                photoViewer.showAlertDialog(builder);
            }
        }
    }

    private void setCurrentSwatch(Swatch swatch, boolean updateInterface) {
        this.renderView.setColor(swatch.color);
        this.renderView.setBrushSize(swatch.brushWeight);
        if (updateInterface) {
            this.colorPicker.setSwatch(swatch);
        }
        if (this.currentEntityView instanceof TextPaintView) {
            ((TextPaintView) this.currentEntityView).setSwatch(swatch);
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void setDimVisibility(final boolean r4) {
        /*
        r3 = this;
        r0 = 2;
        if (r4 == 0) goto L_0x0017;
    L_0x0003:
        r1 = r3.dimView;
        r2 = 0;
        r1.setVisibility(r2);
        r1 = r3.dimView;
        r2 = "alpha";
        r0 = new float[r0];
        r0 = {0, 1065353216};
        r0 = android.animation.ObjectAnimator.ofFloat(r1, r2, r0);
        goto L_0x0024;
    L_0x0017:
        r1 = r3.dimView;
        r2 = "alpha";
        r0 = new float[r0];
        r0 = {1065353216, 0};
        r0 = android.animation.ObjectAnimator.ofFloat(r1, r2, r0);
    L_0x0024:
        r1 = new org.telegram.ui.Components.PhotoPaintView$12;
        r1.<init>(r4);
        r0.addListener(r1);
        r1 = 200; // 0xc8 float:2.8E-43 double:9.9E-322;
        r0.setDuration(r1);
        r0.start();
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.ui.Components.PhotoPaintView.setDimVisibility(boolean):void");
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void setTextDimVisibility(final boolean r4, org.telegram.ui.Components.Paint.Views.EntityView r5) {
        /*
        r3 = this;
        if (r4 == 0) goto L_0x0028;
    L_0x0002:
        if (r5 == 0) goto L_0x0028;
    L_0x0004:
        r0 = r5.getParent();
        r0 = (android.view.ViewGroup) r0;
        r1 = r3.textDimView;
        r1 = r1.getParent();
        if (r1 == 0) goto L_0x001f;
    L_0x0012:
        r1 = r3.textDimView;
        r1 = r1.getParent();
        r1 = (org.telegram.ui.Components.Paint.Views.EntitiesContainerView) r1;
        r2 = r3.textDimView;
        r1.removeView(r2);
    L_0x001f:
        r1 = r3.textDimView;
        r2 = r0.indexOfChild(r5);
        r0.addView(r1, r2);
    L_0x0028:
        r0 = r4 ^ 1;
        r5.setSelectionVisibility(r0);
        r0 = 2;
        if (r4 == 0) goto L_0x0044;
    L_0x0030:
        r1 = r3.textDimView;
        r2 = 0;
        r1.setVisibility(r2);
        r1 = r3.textDimView;
        r2 = "alpha";
        r0 = new float[r0];
        r0 = {0, 1065353216};
        r0 = android.animation.ObjectAnimator.ofFloat(r1, r2, r0);
        goto L_0x0051;
    L_0x0044:
        r1 = r3.textDimView;
        r2 = "alpha";
        r0 = new float[r0];
        r0 = {1065353216, 0};
        r0 = android.animation.ObjectAnimator.ofFloat(r1, r2, r0);
    L_0x0051:
        r1 = new org.telegram.ui.Components.PhotoPaintView$13;
        r1.<init>(r4);
        r0.addListener(r1);
        r1 = 200; // 0xc8 float:2.8E-43 double:9.9E-322;
        r0.setDuration(r1);
        r0.start();
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.ui.Components.PhotoPaintView.setTextDimVisibility(boolean, org.telegram.ui.Components.Paint.Views.EntityView):void");
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        float bitmapW;
        float bitmapH;
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(width, height);
        int maxHeight = (AndroidUtilities.displaySize.y - ActionBar.getCurrentActionBarHeight()) - AndroidUtilities.dp(48.0f);
        if (this.bitmapToEdit != null) {
            bitmapW = (float) (isSidewardOrientation() ? this.bitmapToEdit.getHeight() : this.bitmapToEdit.getWidth());
            bitmapH = (float) (isSidewardOrientation() ? this.bitmapToEdit.getWidth() : this.bitmapToEdit.getHeight());
        } else {
            bitmapW = (float) width;
            bitmapH = (float) ((height - ActionBar.getCurrentActionBarHeight()) - AndroidUtilities.dp(48.0f));
        }
        float renderWidth = (float) width;
        float renderHeight = (float) Math.floor((double) ((renderWidth * bitmapH) / bitmapW));
        if (renderHeight > ((float) maxHeight)) {
            renderHeight = (float) maxHeight;
            renderWidth = (float) Math.floor((double) ((renderHeight * bitmapW) / bitmapH));
        }
        this.renderView.measure(MeasureSpec.makeMeasureSpec((int) renderWidth, 1073741824), MeasureSpec.makeMeasureSpec((int) renderHeight, 1073741824));
        this.entitiesView.measure(MeasureSpec.makeMeasureSpec((int) this.paintingSize.width, 1073741824), MeasureSpec.makeMeasureSpec((int) this.paintingSize.height, 1073741824));
        this.dimView.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(maxHeight, Integer.MIN_VALUE));
        this.selectionContainerView.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(maxHeight, 1073741824));
        this.colorPicker.measure(MeasureSpec.makeMeasureSpec(width, 1073741824), MeasureSpec.makeMeasureSpec(maxHeight, 1073741824));
        this.toolsView.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(48.0f), 1073741824));
        if (this.stickersView != null) {
            this.stickersView.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(AndroidUtilities.displaySize.y, 1073741824));
        }
    }

    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        float bitmapW;
        float bitmapH;
        PhotoPaintView photoPaintView = this;
        int width = right - left;
        int height = bottom - top;
        int status = VERSION.SDK_INT >= 21 ? AndroidUtilities.statusBarHeight : 0;
        int actionBarHeight = ActionBar.getCurrentActionBarHeight();
        int actionBarHeight2 = ActionBar.getCurrentActionBarHeight() + status;
        int maxHeight = (AndroidUtilities.displaySize.y - actionBarHeight) - AndroidUtilities.dp(48.0f);
        if (photoPaintView.bitmapToEdit != null) {
            bitmapW = (float) (isSidewardOrientation() ? photoPaintView.bitmapToEdit.getHeight() : photoPaintView.bitmapToEdit.getWidth());
            bitmapH = (float) (isSidewardOrientation() ? photoPaintView.bitmapToEdit.getWidth() : photoPaintView.bitmapToEdit.getHeight());
        } else {
            bitmapW = (float) width;
            bitmapH = (float) ((height - actionBarHeight) - AndroidUtilities.dp(48.0f));
        }
        float renderWidth = (float) width;
        float bitmapW2 = bitmapW;
        if (((float) Math.floor((double) ((renderWidth * bitmapH) / bitmapW))) > ((float) maxHeight)) {
            renderWidth = (float) Math.floor((double) ((((float) maxHeight) * bitmapW2) / bitmapH));
        }
        int x = (int) Math.ceil((double) ((width - photoPaintView.renderView.getMeasuredWidth()) / 2));
        int y = ((((((height - actionBarHeight2) - AndroidUtilities.dp(48.0f)) - photoPaintView.renderView.getMeasuredHeight()) / 2) + actionBarHeight2) - ActionBar.getCurrentActionBarHeight()) + AndroidUtilities.dp(8.0f);
        photoPaintView.renderView.layout(x, y, photoPaintView.renderView.getMeasuredWidth() + x, photoPaintView.renderView.getMeasuredHeight() + y);
        float scale = renderWidth / photoPaintView.paintingSize.width;
        photoPaintView.entitiesView.setScaleX(scale);
        photoPaintView.entitiesView.setScaleY(scale);
        photoPaintView.entitiesView.layout(x, y, photoPaintView.entitiesView.getMeasuredWidth() + x, photoPaintView.entitiesView.getMeasuredHeight() + y);
        photoPaintView.dimView.layout(0, status, photoPaintView.dimView.getMeasuredWidth(), photoPaintView.dimView.getMeasuredHeight() + status);
        photoPaintView.selectionContainerView.layout(0, status, photoPaintView.selectionContainerView.getMeasuredWidth(), photoPaintView.selectionContainerView.getMeasuredHeight() + status);
        photoPaintView.colorPicker.layout(0, actionBarHeight2, photoPaintView.colorPicker.getMeasuredWidth(), photoPaintView.colorPicker.getMeasuredHeight() + actionBarHeight2);
        photoPaintView.toolsView.layout(0, height - photoPaintView.toolsView.getMeasuredHeight(), photoPaintView.toolsView.getMeasuredWidth(), height);
        photoPaintView.curtainView.layout(0, 0, width, maxHeight);
        if (photoPaintView.stickersView != null) {
            photoPaintView.stickersView.layout(0, status, photoPaintView.stickersView.getMeasuredWidth(), photoPaintView.stickersView.getMeasuredHeight() + status);
        }
        if (photoPaintView.currentEntityView != null) {
            photoPaintView.currentEntityView.updateSelectionView();
            photoPaintView.currentEntityView.setOffset(photoPaintView.entitiesView.getLeft() - photoPaintView.selectionContainerView.getLeft(), photoPaintView.entitiesView.getTop() - photoPaintView.selectionContainerView.getTop());
        }
    }

    public boolean onEntitySelected(EntityView entityView) {
        return selectEntity(entityView);
    }

    public boolean onEntityLongClicked(EntityView entityView) {
        showMenuForEntity(entityView);
        return true;
    }

    public boolean allowInteraction(EntityView entityView) {
        return this.editingText ^ 1;
    }

    private Point centerPositionForEntity() {
        Size paintingSize = getPaintingSize();
        return new Point(paintingSize.width / 2.0f, paintingSize.height / 2.0f);
    }

    private Point startPositionRelativeToEntity(EntityView entityView) {
        if (entityView != null) {
            Point position = entityView.getPosition();
            return new Point(position.x + 200.0f, position.y + 200.0f);
        }
        Point position2 = centerPositionForEntity();
        while (true) {
            boolean occupied = false;
            for (int index = 0; index < this.entitiesView.getChildCount(); index++) {
                View view = this.entitiesView.getChildAt(index);
                if (view instanceof EntityView) {
                    Point location = ((EntityView) view).getPosition();
                    if (((float) Math.sqrt(Math.pow((double) (location.x - position2.x), 2.0d) + Math.pow((double) (location.y - position2.y), 2.0d))) < 100.0f) {
                        occupied = true;
                    }
                }
            }
            if (!occupied) {
                return position2;
            }
            position2 = new Point(position2.x + 200.0f, position2.y + 200.0f);
        }
    }

    public ArrayList<InputDocument> getMasks() {
        ArrayList<InputDocument> result = null;
        int count = this.entitiesView.getChildCount();
        for (int a = 0; a < count; a++) {
            View child = this.entitiesView.getChildAt(a);
            if (child instanceof StickerView) {
                Document document = ((StickerView) child).getSticker();
                if (result == null) {
                    result = new ArrayList();
                }
                TL_inputDocument inputDocument = new TL_inputDocument();
                inputDocument.id = document.id;
                inputDocument.access_hash = document.access_hash;
                result.add(inputDocument);
            }
        }
        return result;
    }

    private boolean selectEntity(EntityView entityView) {
        boolean changed = false;
        if (this.currentEntityView != null) {
            if (this.currentEntityView == entityView) {
                if (!this.editingText) {
                    showMenuForEntity(this.currentEntityView);
                }
                return true;
            }
            this.currentEntityView.deselect();
            changed = true;
        }
        this.currentEntityView = entityView;
        if (this.currentEntityView != null) {
            this.currentEntityView.select(this.selectionContainerView);
            this.entitiesView.bringViewToFront(this.currentEntityView);
            if (this.currentEntityView instanceof TextPaintView) {
                setCurrentSwatch(((TextPaintView) this.currentEntityView).getSwatch(), true);
            }
            changed = true;
        }
        updateSettingsButton();
        return changed;
    }

    private void removeEntity(EntityView entityView) {
        if (entityView == this.currentEntityView) {
            this.currentEntityView.deselect();
            if (this.editingText) {
                closeTextEnter(false);
            }
            this.currentEntityView = null;
            updateSettingsButton();
        }
        this.entitiesView.removeView(entityView);
        this.undoStore.unregisterUndo(entityView.getUUID());
    }

    private void duplicateSelectedEntity() {
        if (this.currentEntityView != null) {
            EntityView entityView = null;
            Point position = startPositionRelativeToEntity(this.currentEntityView);
            EntityView newStickerView;
            if (this.currentEntityView instanceof StickerView) {
                newStickerView = new StickerView(getContext(), (StickerView) this.currentEntityView, position);
                newStickerView.setDelegate(this);
                this.entitiesView.addView(newStickerView);
                entityView = newStickerView;
            } else if (this.currentEntityView instanceof TextPaintView) {
                newStickerView = new TextPaintView(getContext(), (TextPaintView) this.currentEntityView, position);
                newStickerView.setDelegate(this);
                newStickerView.setMaxWidth((int) (getPaintingSize().width - 20.0f));
                this.entitiesView.addView(newStickerView, LayoutHelper.createFrame(-2, -2.0f));
                entityView = newStickerView;
            }
            registerRemovalUndo(entityView);
            selectEntity(entityView);
            updateSettingsButton();
        }
    }

    private void openStickersView() {
        if (this.stickersView == null || this.stickersView.getVisibility() != 0) {
            this.pickingSticker = true;
            if (this.stickersView == null) {
                this.stickersView = new StickerMasksView(getContext());
                this.stickersView.setListener(new Listener() {
                    public void onStickerSelected(Document sticker) {
                        PhotoPaintView.this.closeStickersView();
                        PhotoPaintView.this.createSticker(sticker);
                    }

                    public void onTypeChanged() {
                    }
                });
                addView(this.stickersView, LayoutHelper.createFrame(-1, -1, 51));
            }
            this.stickersView.setVisibility(0);
            Animator a = ObjectAnimator.ofFloat(this.stickersView, "alpha", new float[]{0.0f, 1.0f});
            a.setDuration(200);
            a.start();
        }
    }

    private void closeStickersView() {
        if (this.stickersView != null) {
            if (this.stickersView.getVisibility() == 0) {
                this.pickingSticker = false;
                Animator a = ObjectAnimator.ofFloat(this.stickersView, "alpha", new float[]{1.0f, 0.0f});
                a.setDuration(200);
                a.addListener(new AnimatorListenerAdapter() {
                    public void onAnimationEnd(Animator animator) {
                        PhotoPaintView.this.stickersView.setVisibility(8);
                    }
                });
                a.start();
            }
        }
    }

    private Size baseStickerSize() {
        float side = (float) Math.floor(((double) getPaintingSize().width) * 0.5d);
        return new Size(side, side);
    }

    private void registerRemovalUndo(final EntityView entityView) {
        this.undoStore.registerUndo(entityView.getUUID(), new Runnable() {
            public void run() {
                PhotoPaintView.this.removeEntity(entityView);
            }
        });
    }

    private void createSticker(Document sticker) {
        StickerPosition position = calculateStickerPosition(sticker);
        StickerView view = new StickerView(getContext(), position.position, position.angle, position.scale, baseStickerSize(), sticker);
        view.setDelegate(this);
        this.entitiesView.addView(view);
        registerRemovalUndo(view);
        selectEntity(view);
    }

    private void mirrorSticker() {
        if (this.currentEntityView instanceof StickerView) {
            ((StickerView) this.currentEntityView).mirror();
        }
    }

    private int baseFontSize() {
        return (int) (getPaintingSize().width / 9.0f);
    }

    private void createText() {
        Swatch currentSwatch = this.colorPicker.getSwatch();
        setCurrentSwatch(this.selectedStroke ? new Swatch(Theme.ACTION_BAR_VIDEO_EDIT_COLOR, 0.85f, currentSwatch.brushWeight) : new Swatch(-1, 1.0f, currentSwatch.brushWeight), true);
        TextPaintView textPaintView = new TextPaintView(getContext(), startPositionRelativeToEntity(null), baseFontSize(), TtmlNode.ANONYMOUS_REGION_ID, this.colorPicker.getSwatch(), this.selectedStroke);
        textPaintView.setDelegate(this);
        textPaintView.setMaxWidth((int) (getPaintingSize().width - 20.0f));
        this.entitiesView.addView(textPaintView, LayoutHelper.createFrame(-2, -2.0f));
        registerRemovalUndo(textPaintView);
        selectEntity(textPaintView);
        editSelectedTextEntity();
    }

    private void editSelectedTextEntity() {
        if (this.currentEntityView instanceof TextPaintView) {
            if (!this.editingText) {
                this.curtainView.setVisibility(0);
                TextPaintView textPaintView = this.currentEntityView;
                this.initialText = textPaintView.getText();
                this.editingText = true;
                this.editedTextPosition = textPaintView.getPosition();
                this.editedTextRotation = textPaintView.getRotation();
                this.editedTextScale = textPaintView.getScale();
                textPaintView.setPosition(centerPositionForEntity());
                textPaintView.setRotation(0.0f);
                textPaintView.setScale(1.0f);
                this.toolsView.setVisibility(8);
                setTextDimVisibility(true, textPaintView);
                textPaintView.beginEditing();
                ((InputMethodManager) ApplicationLoader.applicationContext.getSystemService("input_method")).toggleSoftInputFromWindow(textPaintView.getFocusedView().getWindowToken(), 2, 0);
            }
        }
    }

    public void closeTextEnter(boolean apply) {
        if (this.editingText) {
            if (this.currentEntityView instanceof TextPaintView) {
                TextPaintView textPaintView = this.currentEntityView;
                this.toolsView.setVisibility(0);
                AndroidUtilities.hideKeyboard(textPaintView.getFocusedView());
                textPaintView.getFocusedView().clearFocus();
                textPaintView.endEditing();
                if (!apply) {
                    textPaintView.setText(this.initialText);
                }
                if (textPaintView.getText().trim().length() == 0) {
                    this.entitiesView.removeView(textPaintView);
                    selectEntity(null);
                } else {
                    textPaintView.setPosition(this.editedTextPosition);
                    textPaintView.setRotation(this.editedTextRotation);
                    textPaintView.setScale(this.editedTextScale);
                    this.editedTextPosition = null;
                    this.editedTextRotation = 0.0f;
                    this.editedTextScale = 0.0f;
                }
                setTextDimVisibility(false, textPaintView);
                this.editingText = false;
                this.initialText = null;
                this.curtainView.setVisibility(8);
            }
        }
    }

    private void setBrush(int brush) {
        RenderView renderView = this.renderView;
        Brush[] brushArr = this.brushes;
        this.currentBrush = brush;
        renderView.setBrush(brushArr[brush]);
    }

    private void setStroke(boolean stroke) {
        this.selectedStroke = stroke;
        if (this.currentEntityView instanceof TextPaintView) {
            Swatch currentSwatch = this.colorPicker.getSwatch();
            if (stroke && currentSwatch.color == -1) {
                setCurrentSwatch(new Swatch(Theme.ACTION_BAR_VIDEO_EDIT_COLOR, 0.85f, currentSwatch.brushWeight), true);
            } else if (!stroke && currentSwatch.color == Theme.ACTION_BAR_VIDEO_EDIT_COLOR) {
                setCurrentSwatch(new Swatch(-1, 1.0f, currentSwatch.brushWeight), true);
            }
            ((TextPaintView) this.currentEntityView).setStroke(stroke);
        }
    }

    private void showMenuForEntity(final EntityView entityView) {
        showPopup(new Runnable() {
            public void run() {
                TextView editView;
                LinearLayout parent = new LinearLayout(PhotoPaintView.this.getContext());
                parent.setOrientation(0);
                TextView deleteView = new TextView(PhotoPaintView.this.getContext());
                deleteView.setTextColor(Theme.getColor(Theme.key_actionBarDefaultSubmenuItem));
                deleteView.setBackgroundDrawable(Theme.getSelectorDrawable(false));
                deleteView.setGravity(16);
                deleteView.setPadding(AndroidUtilities.dp(16.0f), 0, AndroidUtilities.dp(14.0f), 0);
                deleteView.setTextSize(1, 18.0f);
                deleteView.setTag(Integer.valueOf(0));
                deleteView.setText(LocaleController.getString("PaintDelete", R.string.PaintDelete));
                deleteView.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        PhotoPaintView.this.removeEntity(entityView);
                        if (PhotoPaintView.this.popupWindow != null && PhotoPaintView.this.popupWindow.isShowing()) {
                            PhotoPaintView.this.popupWindow.dismiss(true);
                        }
                    }
                });
                parent.addView(deleteView, LayoutHelper.createLinear(-2, 48));
                if (entityView instanceof TextPaintView) {
                    editView = new TextView(PhotoPaintView.this.getContext());
                    editView.setTextColor(Theme.getColor(Theme.key_actionBarDefaultSubmenuItem));
                    editView.setBackgroundDrawable(Theme.getSelectorDrawable(false));
                    editView.setGravity(16);
                    editView.setPadding(AndroidUtilities.dp(16.0f), 0, AndroidUtilities.dp(16.0f), 0);
                    editView.setTextSize(1, 18.0f);
                    editView.setTag(Integer.valueOf(1));
                    editView.setText(LocaleController.getString("PaintEdit", R.string.PaintEdit));
                    editView.setOnClickListener(new OnClickListener() {
                        public void onClick(View v) {
                            PhotoPaintView.this.editSelectedTextEntity();
                            if (PhotoPaintView.this.popupWindow != null && PhotoPaintView.this.popupWindow.isShowing()) {
                                PhotoPaintView.this.popupWindow.dismiss(true);
                            }
                        }
                    });
                    parent.addView(editView, LayoutHelper.createLinear(-2, 48));
                }
                editView = new TextView(PhotoPaintView.this.getContext());
                editView.setTextColor(Theme.getColor(Theme.key_actionBarDefaultSubmenuItem));
                editView.setBackgroundDrawable(Theme.getSelectorDrawable(false));
                editView.setGravity(16);
                editView.setPadding(AndroidUtilities.dp(14.0f), 0, AndroidUtilities.dp(16.0f), 0);
                editView.setTextSize(1, 18.0f);
                editView.setTag(Integer.valueOf(2));
                editView.setText(LocaleController.getString("PaintDuplicate", R.string.PaintDuplicate));
                editView.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        PhotoPaintView.this.duplicateSelectedEntity();
                        if (PhotoPaintView.this.popupWindow != null && PhotoPaintView.this.popupWindow.isShowing()) {
                            PhotoPaintView.this.popupWindow.dismiss(true);
                        }
                    }
                });
                parent.addView(editView, LayoutHelper.createLinear(-2, 48));
                PhotoPaintView.this.popupLayout.addView(parent);
                LayoutParams params = (LayoutParams) parent.getLayoutParams();
                params.width = -2;
                params.height = -2;
                parent.setLayoutParams(params);
            }
        }, entityView, 17, (int) ((entityView.getPosition().x - ((float) (this.entitiesView.getWidth() / 2))) * this.entitiesView.getScaleX()), ((int) (((entityView.getPosition().y - ((((float) entityView.getHeight()) * entityView.getScale()) / 2.0f)) - ((float) (this.entitiesView.getHeight() / 2))) * this.entitiesView.getScaleY())) - AndroidUtilities.dp(32.0f));
    }

    private FrameLayout buttonForBrush(final int brush, int resource, boolean selected) {
        FrameLayout button = new FrameLayout(getContext());
        button.setBackgroundDrawable(Theme.getSelectorDrawable(false));
        button.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                PhotoPaintView.this.setBrush(brush);
                if (PhotoPaintView.this.popupWindow != null && PhotoPaintView.this.popupWindow.isShowing()) {
                    PhotoPaintView.this.popupWindow.dismiss(true);
                }
            }
        });
        ImageView preview = new ImageView(getContext());
        preview.setImageResource(resource);
        button.addView(preview, LayoutHelper.createFrame(165, 44.0f, 19, 46.0f, 0.0f, 8.0f, 0.0f));
        if (selected) {
            ImageView check = new ImageView(getContext());
            check.setImageResource(R.drawable.ic_ab_done);
            check.setScaleType(ScaleType.CENTER);
            button.addView(check, LayoutHelper.createFrame(50, -1.0f));
        }
        return button;
    }

    private void showBrushSettings() {
        showPopup(new Runnable() {
            public void run() {
                View neon = null;
                View radial = PhotoPaintView.this.buttonForBrush(0, R.drawable.paint_radial_preview, PhotoPaintView.this.currentBrush == 0);
                PhotoPaintView.this.popupLayout.addView(radial);
                LayoutParams layoutParams = (LayoutParams) radial.getLayoutParams();
                layoutParams.width = -1;
                layoutParams.height = AndroidUtilities.dp(52.0f);
                radial.setLayoutParams(layoutParams);
                View elliptical = PhotoPaintView.this.buttonForBrush(1, R.drawable.paint_elliptical_preview, PhotoPaintView.this.currentBrush == 1);
                PhotoPaintView.this.popupLayout.addView(elliptical);
                layoutParams = (LayoutParams) elliptical.getLayoutParams();
                layoutParams.width = -1;
                layoutParams.height = AndroidUtilities.dp(52.0f);
                elliptical.setLayoutParams(layoutParams);
                PhotoPaintView photoPaintView = PhotoPaintView.this;
                if (PhotoPaintView.this.currentBrush == 2) {
                    neon = 1;
                }
                neon = photoPaintView.buttonForBrush(2, R.drawable.paint_neon_preview, neon);
                PhotoPaintView.this.popupLayout.addView(neon);
                layoutParams = (LayoutParams) neon.getLayoutParams();
                layoutParams.width = -1;
                layoutParams.height = AndroidUtilities.dp(52.0f);
                neon.setLayoutParams(layoutParams);
            }
        }, this, 85, 0, AndroidUtilities.dp(48.0f));
    }

    private FrameLayout buttonForText(final boolean stroke, String text, boolean selected) {
        FrameLayout button = new FrameLayout(getContext()) {
            public boolean onInterceptTouchEvent(MotionEvent ev) {
                return true;
            }
        };
        button.setBackgroundDrawable(Theme.getSelectorDrawable(false));
        button.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                PhotoPaintView.this.setStroke(stroke);
                if (PhotoPaintView.this.popupWindow != null && PhotoPaintView.this.popupWindow.isShowing()) {
                    PhotoPaintView.this.popupWindow.dismiss(true);
                }
            }
        });
        EditTextOutline textView = new EditTextOutline(getContext());
        textView.setBackgroundColor(0);
        textView.setEnabled(false);
        textView.setStrokeWidth((float) AndroidUtilities.dp(3.0f));
        int i = Theme.ACTION_BAR_VIDEO_EDIT_COLOR;
        textView.setTextColor(stroke ? -1 : Theme.ACTION_BAR_VIDEO_EDIT_COLOR);
        if (!stroke) {
            i = 0;
        }
        textView.setStrokeColor(i);
        textView.setPadding(AndroidUtilities.dp(2.0f), 0, AndroidUtilities.dp(2.0f), 0);
        textView.setTextSize(1, 18.0f);
        textView.setTypeface(null, 1);
        textView.setTag(Boolean.valueOf(stroke));
        textView.setText(text);
        button.addView(textView, LayoutHelper.createFrame(-2, -2.0f, 19, 46.0f, 0.0f, 16.0f, 0.0f));
        if (selected) {
            ImageView check = new ImageView(getContext());
            check.setImageResource(R.drawable.ic_ab_done);
            check.setScaleType(ScaleType.CENTER);
            button.addView(check, LayoutHelper.createFrame(50, -1.0f));
        }
        return button;
    }

    private void showTextSettings() {
        showPopup(new Runnable() {
            public void run() {
                View outline = PhotoPaintView.this.buttonForText(true, LocaleController.getString("PaintOutlined", R.string.PaintOutlined), PhotoPaintView.this.selectedStroke);
                PhotoPaintView.this.popupLayout.addView(outline);
                LayoutParams layoutParams = (LayoutParams) outline.getLayoutParams();
                layoutParams.width = -1;
                layoutParams.height = AndroidUtilities.dp(48.0f);
                outline.setLayoutParams(layoutParams);
                View regular = PhotoPaintView.this.buttonForText(false, LocaleController.getString("PaintRegular", R.string.PaintRegular), 1 ^ PhotoPaintView.this.selectedStroke);
                PhotoPaintView.this.popupLayout.addView(regular);
                layoutParams = (LayoutParams) regular.getLayoutParams();
                layoutParams.width = -1;
                layoutParams.height = AndroidUtilities.dp(48.0f);
                regular.setLayoutParams(layoutParams);
            }
        }, this, 85, 0, AndroidUtilities.dp(48.0f));
    }

    private void showPopup(Runnable setupRunnable, View parent, int gravity, int x, int y) {
        if (this.popupWindow == null || !this.popupWindow.isShowing()) {
            if (this.popupLayout == null) {
                this.popupRect = new Rect();
                this.popupLayout = new ActionBarPopupWindowLayout(getContext());
                this.popupLayout.setAnimationEnabled(false);
                this.popupLayout.setOnTouchListener(new OnTouchListener() {
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getActionMasked() == 0 && PhotoPaintView.this.popupWindow != null && PhotoPaintView.this.popupWindow.isShowing()) {
                            v.getHitRect(PhotoPaintView.this.popupRect);
                            if (!PhotoPaintView.this.popupRect.contains((int) event.getX(), (int) event.getY())) {
                                PhotoPaintView.this.popupWindow.dismiss();
                            }
                        }
                        return false;
                    }
                });
                this.popupLayout.setDispatchKeyEventListener(new OnDispatchKeyEventListener() {
                    public void onDispatchKeyEvent(KeyEvent keyEvent) {
                        if (keyEvent.getKeyCode() == 4 && keyEvent.getRepeatCount() == 0 && PhotoPaintView.this.popupWindow != null && PhotoPaintView.this.popupWindow.isShowing()) {
                            PhotoPaintView.this.popupWindow.dismiss();
                        }
                    }
                });
                this.popupLayout.setShowedFromBotton(true);
            }
            this.popupLayout.removeInnerViews();
            setupRunnable.run();
            if (this.popupWindow == null) {
                this.popupWindow = new ActionBarPopupWindow(this.popupLayout, -2, -2);
                this.popupWindow.setAnimationEnabled(false);
                this.popupWindow.setAnimationStyle(R.style.PopupAnimation);
                this.popupWindow.setOutsideTouchable(true);
                this.popupWindow.setClippingEnabled(true);
                this.popupWindow.setInputMethodMode(2);
                this.popupWindow.setSoftInputMode(0);
                this.popupWindow.getContentView().setFocusableInTouchMode(true);
                this.popupWindow.setOnDismissListener(new OnDismissListener() {
                    public void onDismiss() {
                        PhotoPaintView.this.popupLayout.removeInnerViews();
                    }
                });
            }
            this.popupLayout.measure(MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(1000.0f), Integer.MIN_VALUE), MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(1000.0f), Integer.MIN_VALUE));
            this.popupWindow.setFocusable(true);
            this.popupWindow.showAtLocation(parent, gravity, x, y);
            this.popupWindow.startAnimation();
            return;
        }
        this.popupWindow.dismiss();
    }

    private int getFrameRotation() {
        int i = this.orientation;
        if (i == 90) {
            return 1;
        }
        if (i == 180) {
            return 2;
        }
        if (i != 270) {
            return 0;
        }
        return 3;
    }

    private void detectFaces() {
        this.queue.postRunnable(new Runnable() {
            /* JADX WARNING: inconsistent code. */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public void run() {
                /*
                r11 = this;
                r0 = 0;
                r1 = new com.google.android.gms.vision.face.FaceDetector$Builder;	 Catch:{ Exception -> 0x00a7 }
                r2 = org.telegram.ui.Components.PhotoPaintView.this;	 Catch:{ Exception -> 0x00a7 }
                r2 = r2.getContext();	 Catch:{ Exception -> 0x00a7 }
                r1.<init>(r2);	 Catch:{ Exception -> 0x00a7 }
                r2 = 1;
                r1 = r1.setMode(r2);	 Catch:{ Exception -> 0x00a7 }
                r1 = r1.setLandmarkType(r2);	 Catch:{ Exception -> 0x00a7 }
                r2 = 0;
                r1 = r1.setTrackingEnabled(r2);	 Catch:{ Exception -> 0x00a7 }
                r1 = r1.build();	 Catch:{ Exception -> 0x00a7 }
                r0 = r1;
                r1 = r0.isOperational();	 Catch:{ Exception -> 0x00a7 }
                if (r1 != 0) goto L_0x0034;
            L_0x0025:
                r1 = org.telegram.messenger.BuildVars.LOGS_ENABLED;	 Catch:{ Exception -> 0x00a7 }
                if (r1 == 0) goto L_0x002e;
            L_0x0029:
                r1 = "face detection is not operational";
                org.telegram.messenger.FileLog.e(r1);	 Catch:{ Exception -> 0x00a7 }
            L_0x002e:
                if (r0 == 0) goto L_0x0033;
            L_0x0030:
                r0.release();
            L_0x0033:
                return;
            L_0x0034:
                r1 = new com.google.android.gms.vision.Frame$Builder;	 Catch:{ Exception -> 0x00a7 }
                r1.<init>();	 Catch:{ Exception -> 0x00a7 }
                r3 = org.telegram.ui.Components.PhotoPaintView.this;	 Catch:{ Exception -> 0x00a7 }
                r3 = r3.bitmapToEdit;	 Catch:{ Exception -> 0x00a7 }
                r1 = r1.setBitmap(r3);	 Catch:{ Exception -> 0x00a7 }
                r3 = org.telegram.ui.Components.PhotoPaintView.this;	 Catch:{ Exception -> 0x00a7 }
                r3 = r3.getFrameRotation();	 Catch:{ Exception -> 0x00a7 }
                r1 = r1.setRotation(r3);	 Catch:{ Exception -> 0x00a7 }
                r1 = r1.build();	 Catch:{ Exception -> 0x00a7 }
                r3 = r0.detect(r1);	 Catch:{ Throwable -> 0x009b }
                r4 = new java.util.ArrayList;	 Catch:{ Exception -> 0x00a7 }
                r4.<init>();	 Catch:{ Exception -> 0x00a7 }
                r5 = org.telegram.ui.Components.PhotoPaintView.this;	 Catch:{ Exception -> 0x00a7 }
                r5 = r5.getPaintingSize();	 Catch:{ Exception -> 0x00a7 }
            L_0x0063:
                r6 = r3.size();	 Catch:{ Exception -> 0x00a7 }
                if (r2 >= r6) goto L_0x0090;
            L_0x0069:
                r6 = r3.keyAt(r2);	 Catch:{ Exception -> 0x00a7 }
                r7 = r3.get(r6);	 Catch:{ Exception -> 0x00a7 }
                r7 = (com.google.android.gms.vision.face.Face) r7;	 Catch:{ Exception -> 0x00a7 }
                r8 = new org.telegram.ui.Components.Paint.PhotoFace;	 Catch:{ Exception -> 0x00a7 }
                r9 = org.telegram.ui.Components.PhotoPaintView.this;	 Catch:{ Exception -> 0x00a7 }
                r9 = r9.bitmapToEdit;	 Catch:{ Exception -> 0x00a7 }
                r10 = org.telegram.ui.Components.PhotoPaintView.this;	 Catch:{ Exception -> 0x00a7 }
                r10 = r10.isSidewardOrientation();	 Catch:{ Exception -> 0x00a7 }
                r8.<init>(r7, r9, r5, r10);	 Catch:{ Exception -> 0x00a7 }
                r9 = r8.isSufficient();	 Catch:{ Exception -> 0x00a7 }
                if (r9 == 0) goto L_0x008d;
            L_0x008a:
                r4.add(r8);	 Catch:{ Exception -> 0x00a7 }
            L_0x008d:
                r2 = r2 + 1;
                goto L_0x0063;
            L_0x0090:
                r2 = org.telegram.ui.Components.PhotoPaintView.this;	 Catch:{ Exception -> 0x00a7 }
                r2.faces = r4;	 Catch:{ Exception -> 0x00a7 }
                if (r0 == 0) goto L_0x00ae;
            L_0x0097:
                r0.release();
                goto L_0x00ae;
            L_0x009b:
                r2 = move-exception;
                org.telegram.messenger.FileLog.e(r2);	 Catch:{ Exception -> 0x00a7 }
                if (r0 == 0) goto L_0x00a4;
            L_0x00a1:
                r0.release();
            L_0x00a4:
                return;
            L_0x00a5:
                r1 = move-exception;
                goto L_0x00af;
            L_0x00a7:
                r1 = move-exception;
                org.telegram.messenger.FileLog.e(r1);	 Catch:{ all -> 0x00a5 }
                if (r0 == 0) goto L_0x00ae;
            L_0x00ad:
                goto L_0x0097;
            L_0x00ae:
                return;
            L_0x00af:
                if (r0 == 0) goto L_0x00b4;
            L_0x00b1:
                r0.release();
            L_0x00b4:
                throw r1;
                */
                throw new UnsupportedOperationException("Method not decompiled: org.telegram.ui.Components.PhotoPaintView.26.run():void");
            }
        });
    }

    private StickerPosition calculateStickerPosition(Document document) {
        float yCompX = this;
        Document document2 = document;
        TL_maskCoords maskCoords = null;
        for (int a = 0; a < document2.attributes.size(); a++) {
            DocumentAttribute attribute = (DocumentAttribute) document2.attributes.get(a);
            if (attribute instanceof TL_documentAttributeSticker) {
                maskCoords = attribute.mask_coords;
                break;
            }
        }
        StickerPosition defaultPosition = new StickerPosition(centerPositionForEntity(), 0.75f, 0.0f);
        if (!(maskCoords == null || yCompX.faces == null)) {
            if (yCompX.faces.size() != 0) {
                int anchor = maskCoords.n;
                PhotoFace face = getRandomFaceWithVacantAnchor(anchor, document2.id, maskCoords);
                if (face == null) {
                    return defaultPosition;
                }
                Point referencePoint = face.getPointForAnchor(anchor);
                float referenceWidth = face.getWidthForAnchor(anchor);
                float angle = face.getAngle();
                Size baseSize = baseStickerSize();
                float radAngle = (float) Math.toRadians((double) angle);
                return new StickerPosition(new Point((referencePoint.x + ((float) ((Math.sin(1.5707963267948966d - ((double) radAngle)) * ((double) referenceWidth)) * maskCoords.x))) + ((float) ((Math.cos(((double) radAngle) + 1.5707963267948966d) * ((double) referenceWidth)) * maskCoords.y)), (referencePoint.y + ((float) ((Math.cos(1.5707963267948966d - ((double) radAngle)) * ((double) referenceWidth)) * maskCoords.x))) + ((float) ((Math.sin(1.5707963267948966d + ((double) radAngle)) * ((double) referenceWidth)) * maskCoords.y))), (float) (((double) (referenceWidth / baseSize.width)) * maskCoords.zoom), angle);
            }
        }
        Object obj = yCompX;
        return defaultPosition;
    }

    private PhotoFace getRandomFaceWithVacantAnchor(int anchor, long documentId, TL_maskCoords maskCoords) {
        PhotoPaintView photoPaintView = this;
        int i = anchor;
        if (i >= 0 && i <= 3) {
            if (!photoPaintView.faces.isEmpty()) {
                int count = photoPaintView.faces.size();
                int remaining = count;
                int i2 = Utilities.random.nextInt(count);
                while (true) {
                    int i3 = i2;
                    if (remaining <= 0) {
                        return null;
                    }
                    PhotoFace face = (PhotoFace) photoPaintView.faces.get(i3);
                    if (!isFaceAnchorOccupied(face, i, documentId, maskCoords)) {
                        return face;
                    }
                    i2 = (i3 + 1) % count;
                    remaining--;
                }
            }
        }
        return null;
    }

    private boolean isFaceAnchorOccupied(PhotoFace face, int anchor, long documentId, TL_maskCoords maskCoords) {
        PhotoPaintView photoPaintView = this;
        Point anchorPoint = face.getPointForAnchor(anchor);
        if (anchorPoint == null) {
            return true;
        }
        int i;
        float minDistance = face.getWidthForAnchor(0) * 1.1f;
        for (int index = 0; index < photoPaintView.entitiesView.getChildCount(); index++) {
            View view = photoPaintView.entitiesView.getChildAt(index);
            if (view instanceof StickerView) {
                StickerView stickerView = (StickerView) view;
                if (stickerView.getAnchor() == anchor) {
                    Point location = stickerView.getPosition();
                    float distance = (float) Math.hypot((double) (location.x - anchorPoint.x), (double) (location.y - anchorPoint.y));
                    if ((documentId == stickerView.getSticker().id || photoPaintView.faces.size() > 1) && distance < minDistance) {
                        return true;
                    }
                }
            } else {
                i = anchor;
            }
        }
        i = anchor;
        return false;
    }
}
