package org.telegram.ui.Components;

import android.content.Context;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffColorFilter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.beta.R;
import org.telegram.ui.ActionBar.Theme;

public class ChatBigEmptyView extends LinearLayout {
    private ArrayList<ImageView> imageViews = new ArrayList();
    private TextView secretViewStatusTextView;
    private ArrayList<TextView> textViews = new ArrayList();

    public void setTextColor(int r1) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.ui.Components.ChatBigEmptyView.setTextColor(int):void
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:116)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:249)
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
        r0 = 0;
        r1 = r0;
        r2 = r5.textViews;
        r2 = r2.size();
        if (r1 >= r2) goto L_0x0018;
    L_0x000a:
        r2 = r5.textViews;
        r2 = r2.get(r1);
        r2 = (android.widget.TextView) r2;
        r2.setTextColor(r6);
        r1 = r1 + 1;
        goto L_0x0002;
        r1 = r5.imageViews;
        r1 = r1.size();
        if (r0 >= r1) goto L_0x003c;
        r1 = r5.imageViews;
        r1 = r1.get(r0);
        r1 = (android.widget.ImageView) r1;
        r2 = new android.graphics.PorterDuffColorFilter;
        r3 = "chat_serviceText";
        r3 = org.telegram.ui.ActionBar.Theme.getColor(r3);
        r4 = android.graphics.PorterDuff.Mode.MULTIPLY;
        r2.<init>(r3, r4);
        r1.setColorFilter(r2);
        r0 = r0 + 1;
        goto L_0x0019;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.ui.Components.ChatBigEmptyView.setTextColor(int):void");
    }

    public ChatBigEmptyView(Context context, boolean secretChat) {
        Context context2 = context;
        super(context);
        setBackgroundResource(R.drawable.system);
        getBackground().setColorFilter(Theme.colorFilter);
        setPadding(AndroidUtilities.dp(16.0f), AndroidUtilities.dp(12.0f), AndroidUtilities.dp(16.0f), AndroidUtilities.dp(12.0f));
        setOrientation(1);
        if (secretChat) {
            r0.secretViewStatusTextView = new TextView(context2);
            r0.secretViewStatusTextView.setTextSize(1, 15.0f);
            r0.secretViewStatusTextView.setTextColor(Theme.getColor(Theme.key_chat_serviceText));
            r0.secretViewStatusTextView.setGravity(1);
            r0.secretViewStatusTextView.setMaxWidth(AndroidUtilities.dp(210.0f));
            r0.textViews.add(r0.secretViewStatusTextView);
            addView(r0.secretViewStatusTextView, LayoutHelper.createLinear(-2, -2, 49));
        } else {
            ImageView imageView = new ImageView(context2);
            imageView.setImageResource(R.drawable.cloud_big);
            addView(imageView, LayoutHelper.createLinear(-2, -2, 49, 0, 2, 0, 0));
        }
        TextView textView = new TextView(context2);
        if (secretChat) {
            textView.setText(LocaleController.getString("EncryptedDescriptionTitle", R.string.EncryptedDescriptionTitle));
            textView.setTextSize(1, 15.0f);
        } else {
            textView.setText(LocaleController.getString("ChatYourSelfTitle", R.string.ChatYourSelfTitle));
            textView.setTextSize(1, 16.0f);
            textView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
            textView.setGravity(1);
        }
        textView.setTextColor(Theme.getColor(Theme.key_chat_serviceText));
        r0.textViews.add(textView);
        textView.setMaxWidth(AndroidUtilities.dp(260.0f));
        int i = secretChat ? LocaleController.isRTL ? 5 : 3 : 1;
        int i2 = 0;
        addView(textView, LayoutHelper.createLinear(-2, -2, i | 48, 0, 8, 0, secretChat ? 0 : 8));
        for (int a = i2; a < 4; a++) {
            LinearLayout linearLayout = new LinearLayout(context2);
            linearLayout.setOrientation(i2);
            addView(linearLayout, LayoutHelper.createLinear(-2, -2, LocaleController.isRTL ? 5 : 3, 0, 8, 0, 0));
            ImageView imageView2 = new ImageView(context2);
            imageView2.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_chat_serviceText), Mode.MULTIPLY));
            imageView2.setImageResource(secretChat ? R.drawable.ic_lock_white : R.drawable.list_circle);
            r0.imageViews.add(imageView2);
            TextView textView2 = new TextView(context2);
            textView2.setTextSize(1, 15.0f);
            textView2.setTextColor(Theme.getColor(Theme.key_chat_serviceText));
            r0.textViews.add(textView2);
            textView2.setGravity(16 | (LocaleController.isRTL ? 5 : 3));
            textView2.setMaxWidth(AndroidUtilities.dp(260.0f));
            switch (a) {
                case 0:
                    if (!secretChat) {
                        textView2.setText(LocaleController.getString("ChatYourSelfDescription1", R.string.ChatYourSelfDescription1));
                        break;
                    } else {
                        textView2.setText(LocaleController.getString("EncryptedDescription1", R.string.EncryptedDescription1));
                        break;
                    }
                case 1:
                    if (!secretChat) {
                        textView2.setText(LocaleController.getString("ChatYourSelfDescription2", R.string.ChatYourSelfDescription2));
                        break;
                    } else {
                        textView2.setText(LocaleController.getString("EncryptedDescription2", R.string.EncryptedDescription2));
                        break;
                    }
                case 2:
                    if (!secretChat) {
                        textView2.setText(LocaleController.getString("ChatYourSelfDescription3", R.string.ChatYourSelfDescription3));
                        break;
                    } else {
                        textView2.setText(LocaleController.getString("EncryptedDescription3", R.string.EncryptedDescription3));
                        break;
                    }
                case 3:
                    if (!secretChat) {
                        textView2.setText(LocaleController.getString("ChatYourSelfDescription4", R.string.ChatYourSelfDescription4));
                        break;
                    } else {
                        textView2.setText(LocaleController.getString("EncryptedDescription4", R.string.EncryptedDescription4));
                        break;
                    }
                default:
                    break;
            }
            if (LocaleController.isRTL) {
                linearLayout.addView(textView2, LayoutHelper.createLinear(-2, -2));
                if (secretChat) {
                    linearLayout.addView(imageView2, LayoutHelper.createLinear(-2, -2, 8.0f, 3.0f, 0.0f, 0.0f));
                } else {
                    linearLayout.addView(imageView2, LayoutHelper.createLinear(-2, -2, 8.0f, 7.0f, 0.0f, 0.0f));
                }
            } else {
                if (secretChat) {
                    linearLayout.addView(imageView2, LayoutHelper.createLinear(-2, -2, 0.0f, 4.0f, 8.0f, 0.0f));
                } else {
                    linearLayout.addView(imageView2, LayoutHelper.createLinear(-2, -2, 0.0f, 8.0f, 8.0f, 0.0f));
                }
                linearLayout.addView(textView2, LayoutHelper.createLinear(-2, -2));
            }
        }
    }

    public void setSecretText(String text) {
        this.secretViewStatusTextView.setText(text);
    }
}
