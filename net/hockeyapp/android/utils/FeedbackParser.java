package net.hockeyapp.android.utils;

public class FeedbackParser {

    private static class FeedbackParserHolder {
        static final FeedbackParser INSTANCE = new FeedbackParser();
    }

    public net.hockeyapp.android.objects.FeedbackResponse parseFeedbackResponse(java.lang.String r1) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: net.hockeyapp.android.utils.FeedbackParser.parseFeedbackResponse(java.lang.String):net.hockeyapp.android.objects.FeedbackResponse
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
        r1 = r35;
        r2 = 0;
        if (r1 == 0) goto L_0x0281;
    L_0x0005:
        r3 = new org.json.JSONObject;	 Catch:{ JSONException -> 0x0275 }
        r3.<init>(r1);	 Catch:{ JSONException -> 0x0275 }
        r4 = "feedback";	 Catch:{ JSONException -> 0x0275 }
        r4 = r3.getJSONObject(r4);	 Catch:{ JSONException -> 0x0275 }
        r5 = new net.hockeyapp.android.objects.Feedback;	 Catch:{ JSONException -> 0x0275 }
        r5.<init>();	 Catch:{ JSONException -> 0x0275 }
        r6 = "messages";	 Catch:{ JSONException -> 0x0275 }
        r6 = r4.getJSONArray(r6);	 Catch:{ JSONException -> 0x0275 }
        r7 = 0;	 Catch:{ JSONException -> 0x0275 }
        r8 = r6.length();	 Catch:{ JSONException -> 0x0275 }
        if (r8 <= 0) goto L_0x01dc;	 Catch:{ JSONException -> 0x0275 }
    L_0x0022:
        r8 = new java.util.ArrayList;	 Catch:{ JSONException -> 0x0275 }
        r8.<init>();	 Catch:{ JSONException -> 0x0275 }
        r7 = r8;	 Catch:{ JSONException -> 0x0275 }
        r9 = 0;	 Catch:{ JSONException -> 0x0275 }
    L_0x0029:
        r10 = r6.length();	 Catch:{ JSONException -> 0x0275 }
        if (r9 >= r10) goto L_0x01d0;	 Catch:{ JSONException -> 0x0275 }
    L_0x002f:
        r10 = r6.getJSONObject(r9);	 Catch:{ JSONException -> 0x0275 }
        r11 = "subject";	 Catch:{ JSONException -> 0x0275 }
        r10 = r10.getString(r11);	 Catch:{ JSONException -> 0x0275 }
        r11 = r6.getJSONObject(r9);	 Catch:{ JSONException -> 0x0275 }
        r12 = "text";	 Catch:{ JSONException -> 0x0275 }
        r11 = r11.getString(r12);	 Catch:{ JSONException -> 0x0275 }
        r12 = r6.getJSONObject(r9);	 Catch:{ JSONException -> 0x0275 }
        r13 = "oem";	 Catch:{ JSONException -> 0x0275 }
        r12 = r12.getString(r13);	 Catch:{ JSONException -> 0x0275 }
        r13 = r6.getJSONObject(r9);	 Catch:{ JSONException -> 0x0275 }
        r14 = "model";	 Catch:{ JSONException -> 0x0275 }
        r13 = r13.getString(r14);	 Catch:{ JSONException -> 0x0275 }
        r14 = r6.getJSONObject(r9);	 Catch:{ JSONException -> 0x0275 }
        r15 = "os_version";	 Catch:{ JSONException -> 0x0275 }
        r14 = r14.getString(r15);	 Catch:{ JSONException -> 0x0275 }
        r15 = r6.getJSONObject(r9);	 Catch:{ JSONException -> 0x0275 }
        r8 = "created_at";	 Catch:{ JSONException -> 0x0275 }
        r8 = r15.getString(r8);	 Catch:{ JSONException -> 0x0275 }
        r15 = r6.getJSONObject(r9);	 Catch:{ JSONException -> 0x0275 }
        r1 = "id";	 Catch:{ JSONException -> 0x0275 }
        r1 = r15.getInt(r1);	 Catch:{ JSONException -> 0x0275 }
        r15 = r6.getJSONObject(r9);	 Catch:{ JSONException -> 0x0275 }
        r16 = r2;
        r2 = "token";	 Catch:{ JSONException -> 0x0270 }
        r2 = r15.getString(r2);	 Catch:{ JSONException -> 0x0270 }
        r15 = r6.getJSONObject(r9);	 Catch:{ JSONException -> 0x0270 }
        r17 = r3;	 Catch:{ JSONException -> 0x0270 }
        r3 = "via";	 Catch:{ JSONException -> 0x0270 }
        r3 = r15.getInt(r3);	 Catch:{ JSONException -> 0x0270 }
        r15 = r6.getJSONObject(r9);	 Catch:{ JSONException -> 0x0270 }
        r18 = r4;	 Catch:{ JSONException -> 0x0270 }
        r4 = "user_string";	 Catch:{ JSONException -> 0x0270 }
        r4 = r15.getString(r4);	 Catch:{ JSONException -> 0x0270 }
        r15 = r6.getJSONObject(r9);	 Catch:{ JSONException -> 0x0270 }
        r19 = r5;	 Catch:{ JSONException -> 0x0270 }
        r5 = "clean_text";	 Catch:{ JSONException -> 0x0270 }
        r5 = r15.getString(r5);	 Catch:{ JSONException -> 0x0270 }
        r15 = r6.getJSONObject(r9);	 Catch:{ JSONException -> 0x0270 }
        r20 = r7;	 Catch:{ JSONException -> 0x0270 }
        r7 = "name";	 Catch:{ JSONException -> 0x0270 }
        r7 = r15.getString(r7);	 Catch:{ JSONException -> 0x0270 }
        r15 = r6.getJSONObject(r9);	 Catch:{ JSONException -> 0x0270 }
        r21 = r3;	 Catch:{ JSONException -> 0x0270 }
        r3 = "app_id";	 Catch:{ JSONException -> 0x0270 }
        r3 = r15.getString(r3);	 Catch:{ JSONException -> 0x0270 }
        r15 = r6.getJSONObject(r9);	 Catch:{ JSONException -> 0x0270 }
        r22 = r6;	 Catch:{ JSONException -> 0x0270 }
        r6 = "attachments";	 Catch:{ JSONException -> 0x0270 }
        r6 = r15.optJSONArray(r6);	 Catch:{ JSONException -> 0x0270 }
        r15 = java.util.Collections.emptyList();	 Catch:{ JSONException -> 0x0270 }
        if (r6 == 0) goto L_0x0169;	 Catch:{ JSONException -> 0x0270 }
    L_0x00cf:
        r23 = r15;	 Catch:{ JSONException -> 0x0270 }
        r15 = new java.util.ArrayList;	 Catch:{ JSONException -> 0x0270 }
        r15.<init>();	 Catch:{ JSONException -> 0x0270 }
        r23 = 0;	 Catch:{ JSONException -> 0x0270 }
    L_0x00d8:
        r24 = r23;	 Catch:{ JSONException -> 0x0270 }
        r25 = r9;	 Catch:{ JSONException -> 0x0270 }
        r9 = r6.length();	 Catch:{ JSONException -> 0x0270 }
        r26 = r4;	 Catch:{ JSONException -> 0x0270 }
        r4 = r24;	 Catch:{ JSONException -> 0x0270 }
        if (r4 >= r9) goto L_0x015c;	 Catch:{ JSONException -> 0x0270 }
    L_0x00e6:
        r9 = r6.getJSONObject(r4);	 Catch:{ JSONException -> 0x0270 }
        r27 = r2;	 Catch:{ JSONException -> 0x0270 }
        r2 = "id";	 Catch:{ JSONException -> 0x0270 }
        r2 = r9.getInt(r2);	 Catch:{ JSONException -> 0x0270 }
        r9 = r6.getJSONObject(r4);	 Catch:{ JSONException -> 0x0270 }
        r28 = r11;	 Catch:{ JSONException -> 0x0270 }
        r11 = "feedback_message_id";	 Catch:{ JSONException -> 0x0270 }
        r9 = r9.getInt(r11);	 Catch:{ JSONException -> 0x0270 }
        r11 = r6.getJSONObject(r4);	 Catch:{ JSONException -> 0x0270 }
        r29 = r10;	 Catch:{ JSONException -> 0x0270 }
        r10 = "file_name";	 Catch:{ JSONException -> 0x0270 }
        r10 = r11.getString(r10);	 Catch:{ JSONException -> 0x0270 }
        r11 = r6.getJSONObject(r4);	 Catch:{ JSONException -> 0x0270 }
        r30 = r14;	 Catch:{ JSONException -> 0x0270 }
        r14 = "url";	 Catch:{ JSONException -> 0x0270 }
        r11 = r11.getString(r14);	 Catch:{ JSONException -> 0x0270 }
        r14 = r6.getJSONObject(r4);	 Catch:{ JSONException -> 0x0270 }
        r31 = r12;	 Catch:{ JSONException -> 0x0270 }
        r12 = "created_at";	 Catch:{ JSONException -> 0x0270 }
        r12 = r14.getString(r12);	 Catch:{ JSONException -> 0x0270 }
        r14 = r6.getJSONObject(r4);	 Catch:{ JSONException -> 0x0270 }
        r32 = r6;	 Catch:{ JSONException -> 0x0270 }
        r6 = "updated_at";	 Catch:{ JSONException -> 0x0270 }
        r6 = r14.getString(r6);	 Catch:{ JSONException -> 0x0270 }
        r14 = new net.hockeyapp.android.objects.FeedbackAttachment;	 Catch:{ JSONException -> 0x0270 }
        r14.<init>();	 Catch:{ JSONException -> 0x0270 }
        r14.setId(r2);	 Catch:{ JSONException -> 0x0270 }
        r14.setMessageId(r9);	 Catch:{ JSONException -> 0x0270 }
        r14.setFilename(r10);	 Catch:{ JSONException -> 0x0270 }
        r14.setUrl(r11);	 Catch:{ JSONException -> 0x0270 }
        r14.setCreatedAt(r12);	 Catch:{ JSONException -> 0x0270 }
        r14.setUpdatedAt(r6);	 Catch:{ JSONException -> 0x0270 }
        r15.add(r14);	 Catch:{ JSONException -> 0x0270 }
        r23 = r4 + 1;	 Catch:{ JSONException -> 0x0270 }
        r9 = r25;	 Catch:{ JSONException -> 0x0270 }
        r4 = r26;	 Catch:{ JSONException -> 0x0270 }
        r2 = r27;	 Catch:{ JSONException -> 0x0270 }
        r11 = r28;	 Catch:{ JSONException -> 0x0270 }
        r10 = r29;	 Catch:{ JSONException -> 0x0270 }
        r14 = r30;	 Catch:{ JSONException -> 0x0270 }
        r12 = r31;	 Catch:{ JSONException -> 0x0270 }
        r6 = r32;	 Catch:{ JSONException -> 0x0270 }
        goto L_0x00d8;	 Catch:{ JSONException -> 0x0270 }
    L_0x015c:
        r27 = r2;	 Catch:{ JSONException -> 0x0270 }
        r32 = r6;	 Catch:{ JSONException -> 0x0270 }
        r29 = r10;	 Catch:{ JSONException -> 0x0270 }
        r28 = r11;	 Catch:{ JSONException -> 0x0270 }
        r31 = r12;	 Catch:{ JSONException -> 0x0270 }
        r30 = r14;	 Catch:{ JSONException -> 0x0270 }
        goto L_0x017b;	 Catch:{ JSONException -> 0x0270 }
    L_0x0169:
        r27 = r2;	 Catch:{ JSONException -> 0x0270 }
        r26 = r4;	 Catch:{ JSONException -> 0x0270 }
        r32 = r6;	 Catch:{ JSONException -> 0x0270 }
        r25 = r9;	 Catch:{ JSONException -> 0x0270 }
        r29 = r10;	 Catch:{ JSONException -> 0x0270 }
        r28 = r11;	 Catch:{ JSONException -> 0x0270 }
        r31 = r12;	 Catch:{ JSONException -> 0x0270 }
        r30 = r14;	 Catch:{ JSONException -> 0x0270 }
        r23 = r15;	 Catch:{ JSONException -> 0x0270 }
    L_0x017b:
        r2 = new net.hockeyapp.android.objects.FeedbackMessage;	 Catch:{ JSONException -> 0x0270 }
        r2.<init>();	 Catch:{ JSONException -> 0x0270 }
        r2.setAppId(r3);	 Catch:{ JSONException -> 0x0270 }
        r2.setCleanText(r5);	 Catch:{ JSONException -> 0x0270 }
        r2.setCreatedAt(r8);	 Catch:{ JSONException -> 0x0270 }
        r2.setId(r1);	 Catch:{ JSONException -> 0x0270 }
        r2.setModel(r13);	 Catch:{ JSONException -> 0x0270 }
        r2.setName(r7);	 Catch:{ JSONException -> 0x0270 }
        r4 = r31;	 Catch:{ JSONException -> 0x0270 }
        r2.setOem(r4);	 Catch:{ JSONException -> 0x0270 }
        r6 = r30;	 Catch:{ JSONException -> 0x0270 }
        r2.setOsVersion(r6);	 Catch:{ JSONException -> 0x0270 }
        r9 = r29;	 Catch:{ JSONException -> 0x0270 }
        r2.setSubject(r9);	 Catch:{ JSONException -> 0x0270 }
        r10 = r28;	 Catch:{ JSONException -> 0x0270 }
        r2.setText(r10);	 Catch:{ JSONException -> 0x0270 }
        r11 = r27;	 Catch:{ JSONException -> 0x0270 }
        r2.setToken(r11);	 Catch:{ JSONException -> 0x0270 }
        r12 = r26;	 Catch:{ JSONException -> 0x0270 }
        r2.setUserString(r12);	 Catch:{ JSONException -> 0x0270 }
        r14 = r21;	 Catch:{ JSONException -> 0x0270 }
        r2.setVia(r14);	 Catch:{ JSONException -> 0x0270 }
        r2.setFeedbackAttachments(r15);	 Catch:{ JSONException -> 0x0270 }
        r33 = r1;	 Catch:{ JSONException -> 0x0270 }
        r1 = r20;	 Catch:{ JSONException -> 0x0270 }
        r1.add(r2);	 Catch:{ JSONException -> 0x0270 }
        r9 = r25 + 1;	 Catch:{ JSONException -> 0x0270 }
        r7 = r1;	 Catch:{ JSONException -> 0x0270 }
        r2 = r16;	 Catch:{ JSONException -> 0x0270 }
        r3 = r17;	 Catch:{ JSONException -> 0x0270 }
        r4 = r18;	 Catch:{ JSONException -> 0x0270 }
        r5 = r19;	 Catch:{ JSONException -> 0x0270 }
        r6 = r22;	 Catch:{ JSONException -> 0x0270 }
        r1 = r35;	 Catch:{ JSONException -> 0x0270 }
        goto L_0x0029;	 Catch:{ JSONException -> 0x0270 }
    L_0x01d0:
        r16 = r2;	 Catch:{ JSONException -> 0x0270 }
        r17 = r3;	 Catch:{ JSONException -> 0x0270 }
        r18 = r4;	 Catch:{ JSONException -> 0x0270 }
        r19 = r5;	 Catch:{ JSONException -> 0x0270 }
        r22 = r6;	 Catch:{ JSONException -> 0x0270 }
        r1 = r7;	 Catch:{ JSONException -> 0x0270 }
        goto L_0x01e7;	 Catch:{ JSONException -> 0x0270 }
    L_0x01dc:
        r16 = r2;	 Catch:{ JSONException -> 0x0270 }
        r17 = r3;	 Catch:{ JSONException -> 0x0270 }
        r18 = r4;	 Catch:{ JSONException -> 0x0270 }
        r19 = r5;	 Catch:{ JSONException -> 0x0270 }
        r22 = r6;	 Catch:{ JSONException -> 0x0270 }
        r1 = r7;	 Catch:{ JSONException -> 0x0270 }
    L_0x01e7:
        r2 = r19;	 Catch:{ JSONException -> 0x0270 }
        r2.setMessages(r1);	 Catch:{ JSONException -> 0x0270 }
        r3 = "name";	 Catch:{ JSONException -> 0x01fb }
        r4 = r18;
        r3 = r4.getString(r3);	 Catch:{ JSONException -> 0x01f8 }
        r2.setName(r3);	 Catch:{ JSONException -> 0x01f8 }
        goto L_0x0204;
    L_0x01f8:
        r0 = move-exception;
        r3 = r0;
        goto L_0x01ff;
    L_0x01fb:
        r0 = move-exception;
        r4 = r18;
        r3 = r0;
    L_0x01ff:
        r5 = "Failed to parse \"name\" in feedback response";	 Catch:{ JSONException -> 0x0270 }
        net.hockeyapp.android.utils.HockeyLog.error(r5, r3);	 Catch:{ JSONException -> 0x0270 }
    L_0x0204:
        r3 = "email";	 Catch:{ JSONException -> 0x020e }
        r3 = r4.getString(r3);	 Catch:{ JSONException -> 0x020e }
        r2.setEmail(r3);	 Catch:{ JSONException -> 0x020e }
        goto L_0x0215;
    L_0x020e:
        r0 = move-exception;
        r3 = r0;
        r5 = "Failed to parse \"email\" in feedback response";	 Catch:{ JSONException -> 0x0270 }
        net.hockeyapp.android.utils.HockeyLog.error(r5, r3);	 Catch:{ JSONException -> 0x0270 }
    L_0x0215:
        r3 = "id";	 Catch:{ JSONException -> 0x021f }
        r3 = r4.getInt(r3);	 Catch:{ JSONException -> 0x021f }
        r2.setId(r3);	 Catch:{ JSONException -> 0x021f }
        goto L_0x0226;
    L_0x021f:
        r0 = move-exception;
        r3 = r0;
        r5 = "Failed to parse \"id\" in feedback response";	 Catch:{ JSONException -> 0x0270 }
        net.hockeyapp.android.utils.HockeyLog.error(r5, r3);	 Catch:{ JSONException -> 0x0270 }
    L_0x0226:
        r3 = "created_at";	 Catch:{ JSONException -> 0x0230 }
        r3 = r4.getString(r3);	 Catch:{ JSONException -> 0x0230 }
        r2.setCreatedAt(r3);	 Catch:{ JSONException -> 0x0230 }
        goto L_0x0237;
    L_0x0230:
        r0 = move-exception;
        r3 = r0;
        r5 = "Failed to parse \"created_at\" in feedback response";	 Catch:{ JSONException -> 0x0270 }
        net.hockeyapp.android.utils.HockeyLog.error(r5, r3);	 Catch:{ JSONException -> 0x0270 }
    L_0x0237:
        r3 = new net.hockeyapp.android.objects.FeedbackResponse;	 Catch:{ JSONException -> 0x0270 }
        r3.<init>();	 Catch:{ JSONException -> 0x0270 }
        r3.setFeedback(r2);	 Catch:{ JSONException -> 0x026c }
        r5 = "status";	 Catch:{ JSONException -> 0x024e }
        r6 = r17;
        r5 = r6.getString(r5);	 Catch:{ JSONException -> 0x024b }
        r3.setStatus(r5);	 Catch:{ JSONException -> 0x024b }
        goto L_0x0257;
    L_0x024b:
        r0 = move-exception;
        r5 = r0;
        goto L_0x0252;
    L_0x024e:
        r0 = move-exception;
        r6 = r17;
        r5 = r0;
    L_0x0252:
        r7 = "Failed to parse \"status\" in feedback response";	 Catch:{ JSONException -> 0x026c }
        net.hockeyapp.android.utils.HockeyLog.error(r7, r5);	 Catch:{ JSONException -> 0x026c }
    L_0x0257:
        r5 = "token";	 Catch:{ JSONException -> 0x0261 }
        r5 = r6.getString(r5);	 Catch:{ JSONException -> 0x0261 }
        r3.setToken(r5);	 Catch:{ JSONException -> 0x0261 }
        goto L_0x0268;
    L_0x0261:
        r0 = move-exception;
        r5 = r0;
        r7 = "Failed to parse \"token\" in feedback response";	 Catch:{ JSONException -> 0x026c }
        net.hockeyapp.android.utils.HockeyLog.error(r7, r5);	 Catch:{ JSONException -> 0x026c }
        r16 = r3;
        goto L_0x0283;
    L_0x026c:
        r0 = move-exception;
        r1 = r0;
        r2 = r3;
        goto L_0x0279;
    L_0x0270:
        r0 = move-exception;
        r1 = r0;
        r2 = r16;
        goto L_0x0279;
    L_0x0275:
        r0 = move-exception;
        r16 = r2;
        r1 = r0;
        r3 = "Failed to parse feedback response";
        net.hockeyapp.android.utils.HockeyLog.error(r3, r1);
        r16 = r2;
        goto L_0x0283;
    L_0x0281:
        r16 = r2;
        return r16;
        */
        throw new UnsupportedOperationException("Method not decompiled: net.hockeyapp.android.utils.FeedbackParser.parseFeedbackResponse(java.lang.String):net.hockeyapp.android.objects.FeedbackResponse");
    }

    private FeedbackParser() {
    }

    public static FeedbackParser getInstance() {
        return FeedbackParserHolder.INSTANCE;
    }
}
