package net.hockeyapp.android.utils;

public class FeedbackParser {

    private static class FeedbackParserHolder {
        static final FeedbackParser INSTANCE = new FeedbackParser();
    }

    private FeedbackParser() {
    }

    public static FeedbackParser getInstance() {
        return FeedbackParserHolder.INSTANCE;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public net.hockeyapp.android.objects.FeedbackResponse parseFeedbackResponse(java.lang.String r38) {
        /*
        r37 = this;
        r15 = 0;
        if (r38 == 0) goto L_0x025c;
    L_0x0003:
        r22 = new org.json.JSONObject;	 Catch:{ JSONException -> 0x0267 }
        r0 = r22;
        r1 = r38;
        r0.<init>(r1);	 Catch:{ JSONException -> 0x0267 }
        r35 = "feedback";
        r0 = r22;
        r1 = r35;
        r14 = r0.getJSONObject(r1);	 Catch:{ JSONException -> 0x0267 }
        r10 = new net.hockeyapp.android.objects.Feedback;	 Catch:{ JSONException -> 0x0267 }
        r10.<init>();	 Catch:{ JSONException -> 0x0267 }
        r35 = "messages";
        r0 = r35;
        r24 = r14.getJSONArray(r0);	 Catch:{ JSONException -> 0x0267 }
        r23 = 0;
        r35 = r24.length();	 Catch:{ JSONException -> 0x0267 }
        if (r35 <= 0) goto L_0x01ef;
    L_0x002d:
        r23 = new java.util.ArrayList;	 Catch:{ JSONException -> 0x0267 }
        r23.<init>();	 Catch:{ JSONException -> 0x0267 }
        r18 = 0;
    L_0x0034:
        r35 = r24.length();	 Catch:{ JSONException -> 0x0267 }
        r0 = r18;
        r1 = r35;
        if (r0 >= r1) goto L_0x01ef;
    L_0x003e:
        r0 = r24;
        r1 = r18;
        r35 = r0.getJSONObject(r1);	 Catch:{ JSONException -> 0x0267 }
        r36 = "subject";
        r29 = r35.getString(r36);	 Catch:{ JSONException -> 0x0267 }
        r0 = r24;
        r1 = r18;
        r35 = r0.getJSONObject(r1);	 Catch:{ JSONException -> 0x0267 }
        r36 = "text";
        r30 = r35.getString(r36);	 Catch:{ JSONException -> 0x0267 }
        r0 = r24;
        r1 = r18;
        r35 = r0.getJSONObject(r1);	 Catch:{ JSONException -> 0x0267 }
        r36 = "oem";
        r27 = r35.getString(r36);	 Catch:{ JSONException -> 0x0267 }
        r0 = r24;
        r1 = r18;
        r35 = r0.getJSONObject(r1);	 Catch:{ JSONException -> 0x0267 }
        r36 = "model";
        r25 = r35.getString(r36);	 Catch:{ JSONException -> 0x0267 }
        r0 = r24;
        r1 = r18;
        r35 = r0.getJSONObject(r1);	 Catch:{ JSONException -> 0x0267 }
        r36 = "os_version";
        r28 = r35.getString(r36);	 Catch:{ JSONException -> 0x0267 }
        r0 = r24;
        r1 = r18;
        r35 = r0.getJSONObject(r1);	 Catch:{ JSONException -> 0x0267 }
        r36 = "created_at";
        r8 = r35.getString(r36);	 Catch:{ JSONException -> 0x0267 }
        r0 = r24;
        r1 = r18;
        r35 = r0.getJSONObject(r1);	 Catch:{ JSONException -> 0x0267 }
        r36 = "id";
        r19 = r35.getInt(r36);	 Catch:{ JSONException -> 0x0267 }
        r0 = r24;
        r1 = r18;
        r35 = r0.getJSONObject(r1);	 Catch:{ JSONException -> 0x0267 }
        r36 = "token";
        r31 = r35.getString(r36);	 Catch:{ JSONException -> 0x0267 }
        r0 = r24;
        r1 = r18;
        r35 = r0.getJSONObject(r1);	 Catch:{ JSONException -> 0x0267 }
        r36 = "via";
        r34 = r35.getInt(r36);	 Catch:{ JSONException -> 0x0267 }
        r0 = r24;
        r1 = r18;
        r35 = r0.getJSONObject(r1);	 Catch:{ JSONException -> 0x0267 }
        r36 = "user_string";
        r33 = r35.getString(r36);	 Catch:{ JSONException -> 0x0267 }
        r0 = r24;
        r1 = r18;
        r35 = r0.getJSONObject(r1);	 Catch:{ JSONException -> 0x0267 }
        r36 = "clean_text";
        r7 = r35.getString(r36);	 Catch:{ JSONException -> 0x0267 }
        r0 = r24;
        r1 = r18;
        r35 = r0.getJSONObject(r1);	 Catch:{ JSONException -> 0x0267 }
        r36 = "name";
        r26 = r35.getString(r36);	 Catch:{ JSONException -> 0x0267 }
        r0 = r24;
        r1 = r18;
        r35 = r0.getJSONObject(r1);	 Catch:{ JSONException -> 0x0267 }
        r36 = "app_id";
        r2 = r35.getString(r36);	 Catch:{ JSONException -> 0x0267 }
        r0 = r24;
        r1 = r18;
        r35 = r0.getJSONObject(r1);	 Catch:{ JSONException -> 0x0267 }
        r36 = "attachments";
        r21 = r35.optJSONArray(r36);	 Catch:{ JSONException -> 0x0267 }
        r12 = java.util.Collections.emptyList();	 Catch:{ JSONException -> 0x0267 }
        if (r21 == 0) goto L_0x01a3;
    L_0x0116:
        r12 = new java.util.ArrayList;	 Catch:{ JSONException -> 0x0267 }
        r12.<init>();	 Catch:{ JSONException -> 0x0267 }
        r20 = 0;
    L_0x011d:
        r35 = r21.length();	 Catch:{ JSONException -> 0x0267 }
        r0 = r20;
        r1 = r35;
        if (r0 >= r1) goto L_0x01a3;
    L_0x0127:
        r0 = r21;
        r1 = r20;
        r35 = r0.getJSONObject(r1);	 Catch:{ JSONException -> 0x0267 }
        r36 = "id";
        r4 = r35.getInt(r36);	 Catch:{ JSONException -> 0x0267 }
        r0 = r21;
        r1 = r20;
        r35 = r0.getJSONObject(r1);	 Catch:{ JSONException -> 0x0267 }
        r36 = "feedback_message_id";
        r5 = r35.getInt(r36);	 Catch:{ JSONException -> 0x0267 }
        r0 = r21;
        r1 = r20;
        r35 = r0.getJSONObject(r1);	 Catch:{ JSONException -> 0x0267 }
        r36 = "file_name";
        r17 = r35.getString(r36);	 Catch:{ JSONException -> 0x0267 }
        r0 = r21;
        r1 = r20;
        r35 = r0.getJSONObject(r1);	 Catch:{ JSONException -> 0x0267 }
        r36 = "url";
        r32 = r35.getString(r36);	 Catch:{ JSONException -> 0x0267 }
        r0 = r21;
        r1 = r20;
        r35 = r0.getJSONObject(r1);	 Catch:{ JSONException -> 0x0267 }
        r36 = "created_at";
        r3 = r35.getString(r36);	 Catch:{ JSONException -> 0x0267 }
        r0 = r21;
        r1 = r20;
        r35 = r0.getJSONObject(r1);	 Catch:{ JSONException -> 0x0267 }
        r36 = "updated_at";
        r6 = r35.getString(r36);	 Catch:{ JSONException -> 0x0267 }
        r11 = new net.hockeyapp.android.objects.FeedbackAttachment;	 Catch:{ JSONException -> 0x0267 }
        r11.<init>();	 Catch:{ JSONException -> 0x0267 }
        r11.setId(r4);	 Catch:{ JSONException -> 0x0267 }
        r11.setMessageId(r5);	 Catch:{ JSONException -> 0x0267 }
        r0 = r17;
        r11.setFilename(r0);	 Catch:{ JSONException -> 0x0267 }
        r0 = r32;
        r11.setUrl(r0);	 Catch:{ JSONException -> 0x0267 }
        r11.setCreatedAt(r3);	 Catch:{ JSONException -> 0x0267 }
        r11.setUpdatedAt(r6);	 Catch:{ JSONException -> 0x0267 }
        r12.add(r11);	 Catch:{ JSONException -> 0x0267 }
        r20 = r20 + 1;
        goto L_0x011d;
    L_0x01a3:
        r13 = new net.hockeyapp.android.objects.FeedbackMessage;	 Catch:{ JSONException -> 0x0267 }
        r13.<init>();	 Catch:{ JSONException -> 0x0267 }
        r13.setAppId(r2);	 Catch:{ JSONException -> 0x0267 }
        r13.setCleanText(r7);	 Catch:{ JSONException -> 0x0267 }
        r13.setCreatedAt(r8);	 Catch:{ JSONException -> 0x0267 }
        r0 = r19;
        r13.setId(r0);	 Catch:{ JSONException -> 0x0267 }
        r0 = r25;
        r13.setModel(r0);	 Catch:{ JSONException -> 0x0267 }
        r0 = r26;
        r13.setName(r0);	 Catch:{ JSONException -> 0x0267 }
        r0 = r27;
        r13.setOem(r0);	 Catch:{ JSONException -> 0x0267 }
        r0 = r28;
        r13.setOsVersion(r0);	 Catch:{ JSONException -> 0x0267 }
        r0 = r29;
        r13.setSubject(r0);	 Catch:{ JSONException -> 0x0267 }
        r0 = r30;
        r13.setText(r0);	 Catch:{ JSONException -> 0x0267 }
        r0 = r31;
        r13.setToken(r0);	 Catch:{ JSONException -> 0x0267 }
        r0 = r33;
        r13.setUserString(r0);	 Catch:{ JSONException -> 0x0267 }
        r0 = r34;
        r13.setVia(r0);	 Catch:{ JSONException -> 0x0267 }
        r13.setFeedbackAttachments(r12);	 Catch:{ JSONException -> 0x0267 }
        r0 = r23;
        r0.add(r13);	 Catch:{ JSONException -> 0x0267 }
        r18 = r18 + 1;
        goto L_0x0034;
    L_0x01ef:
        r0 = r23;
        r10.setMessages(r0);	 Catch:{ JSONException -> 0x0267 }
        r35 = "name";
        r0 = r35;
        r35 = r14.getString(r0);	 Catch:{ JSONException -> 0x025d }
        r0 = r35;
        r10.setName(r0);	 Catch:{ JSONException -> 0x025d }
    L_0x0202:
        r35 = "email";
        r0 = r35;
        r35 = r14.getString(r0);	 Catch:{ JSONException -> 0x0271 }
        r0 = r35;
        r10.setEmail(r0);	 Catch:{ JSONException -> 0x0271 }
    L_0x0210:
        r35 = "id";
        r0 = r35;
        r35 = r14.getInt(r0);	 Catch:{ JSONException -> 0x027b }
        r0 = r35;
        r10.setId(r0);	 Catch:{ JSONException -> 0x027b }
    L_0x021e:
        r35 = "created_at";
        r0 = r35;
        r35 = r14.getString(r0);	 Catch:{ JSONException -> 0x0285 }
        r0 = r35;
        r10.setCreatedAt(r0);	 Catch:{ JSONException -> 0x0285 }
    L_0x022c:
        r16 = new net.hockeyapp.android.objects.FeedbackResponse;	 Catch:{ JSONException -> 0x0267 }
        r16.<init>();	 Catch:{ JSONException -> 0x0267 }
        r0 = r16;
        r0.setFeedback(r10);	 Catch:{ JSONException -> 0x0299 }
        r35 = "status";
        r0 = r22;
        r1 = r35;
        r35 = r0.getString(r1);	 Catch:{ JSONException -> 0x028f }
        r0 = r16;
        r1 = r35;
        r0.setStatus(r1);	 Catch:{ JSONException -> 0x028f }
    L_0x0248:
        r35 = "token";
        r0 = r22;
        r1 = r35;
        r35 = r0.getString(r1);	 Catch:{ JSONException -> 0x029d }
        r0 = r16;
        r1 = r35;
        r0.setToken(r1);	 Catch:{ JSONException -> 0x029d }
    L_0x025a:
        r15 = r16;
    L_0x025c:
        return r15;
    L_0x025d:
        r9 = move-exception;
        r35 = "Failed to parse \"name\" in feedback response";
        r0 = r35;
        net.hockeyapp.android.utils.HockeyLog.error(r0, r9);	 Catch:{ JSONException -> 0x0267 }
        goto L_0x0202;
    L_0x0267:
        r9 = move-exception;
    L_0x0268:
        r35 = "Failed to parse feedback response";
        r0 = r35;
        net.hockeyapp.android.utils.HockeyLog.error(r0, r9);
        goto L_0x025c;
    L_0x0271:
        r9 = move-exception;
        r35 = "Failed to parse \"email\" in feedback response";
        r0 = r35;
        net.hockeyapp.android.utils.HockeyLog.error(r0, r9);	 Catch:{ JSONException -> 0x0267 }
        goto L_0x0210;
    L_0x027b:
        r9 = move-exception;
        r35 = "Failed to parse \"id\" in feedback response";
        r0 = r35;
        net.hockeyapp.android.utils.HockeyLog.error(r0, r9);	 Catch:{ JSONException -> 0x0267 }
        goto L_0x021e;
    L_0x0285:
        r9 = move-exception;
        r35 = "Failed to parse \"created_at\" in feedback response";
        r0 = r35;
        net.hockeyapp.android.utils.HockeyLog.error(r0, r9);	 Catch:{ JSONException -> 0x0267 }
        goto L_0x022c;
    L_0x028f:
        r9 = move-exception;
        r35 = "Failed to parse \"status\" in feedback response";
        r0 = r35;
        net.hockeyapp.android.utils.HockeyLog.error(r0, r9);	 Catch:{ JSONException -> 0x0299 }
        goto L_0x0248;
    L_0x0299:
        r9 = move-exception;
        r15 = r16;
        goto L_0x0268;
    L_0x029d:
        r9 = move-exception;
        r35 = "Failed to parse \"token\" in feedback response";
        r0 = r35;
        net.hockeyapp.android.utils.HockeyLog.error(r0, r9);	 Catch:{ JSONException -> 0x0299 }
        goto L_0x025a;
        */
        throw new UnsupportedOperationException("Method not decompiled: net.hockeyapp.android.utils.FeedbackParser.parseFeedbackResponse(java.lang.String):net.hockeyapp.android.objects.FeedbackResponse");
    }
}
