package com.stripe.android.net;

import com.stripe.android.model.Card;
import com.stripe.android.util.StripeJsonUtils;
import com.stripe.android.util.StripeTextUtils;
import org.json.JSONException;
import org.json.JSONObject;

public class CardParser {
    public static Card parseCard(JSONObject objectCard) throws JSONException {
        JSONObject jSONObject = objectCard;
        return new Card(null, Integer.valueOf(jSONObject.getInt("exp_month")), Integer.valueOf(jSONObject.getInt("exp_year")), null, StripeJsonUtils.optString(jSONObject, "name"), StripeJsonUtils.optString(jSONObject, "address_line1"), StripeJsonUtils.optString(jSONObject, "address_line2"), StripeJsonUtils.optString(jSONObject, "address_city"), StripeJsonUtils.optString(jSONObject, "address_state"), StripeJsonUtils.optString(jSONObject, "address_zip"), StripeJsonUtils.optString(jSONObject, "address_country"), StripeTextUtils.asCardBrand(StripeJsonUtils.optString(jSONObject, "brand")), StripeJsonUtils.optString(jSONObject, "last4"), StripeJsonUtils.optString(jSONObject, "fingerprint"), StripeTextUtils.asFundingType(StripeJsonUtils.optString(jSONObject, "funding")), StripeJsonUtils.optString(jSONObject, "country"), StripeJsonUtils.optString(jSONObject, "currency"));
    }
}
