package android.support.v4.app;

import android.os.Bundle;
import android.support.v4.graphics.drawable.IconCompat;

public class Person {
    IconCompat mIcon;
    boolean mIsBot;
    boolean mIsImportant;
    String mKey;
    CharSequence mName;
    String mUri;

    public static class Builder {
        IconCompat mIcon;
        boolean mIsBot;
        boolean mIsImportant;
        String mKey;
        CharSequence mName;
        String mUri;

        public Builder setName(CharSequence name) {
            this.mName = name;
            return this;
        }

        public Builder setIcon(IconCompat icon) {
            this.mIcon = icon;
            return this;
        }

        public Person build() {
            return new Person(this);
        }
    }

    Person(Builder builder) {
        this.mName = builder.mName;
        this.mIcon = builder.mIcon;
        this.mUri = builder.mUri;
        this.mKey = builder.mKey;
        this.mIsBot = builder.mIsBot;
        this.mIsImportant = builder.mIsImportant;
    }

    public Bundle toBundle() {
        Bundle result = new Bundle();
        result.putCharSequence("name", this.mName);
        result.putBundle("icon", this.mIcon != null ? this.mIcon.toBundle() : null);
        result.putString("uri", this.mUri);
        result.putString("key", this.mKey);
        result.putBoolean("isBot", this.mIsBot);
        result.putBoolean("isImportant", this.mIsImportant);
        return result;
    }

    public android.app.Person toAndroidPerson() {
        return new android.app.Person.Builder().setName(getName()).setIcon(getIcon() != null ? getIcon().toIcon() : null).setUri(getUri()).setKey(getKey()).setBot(isBot()).setImportant(isImportant()).build();
    }

    public CharSequence getName() {
        return this.mName;
    }

    public IconCompat getIcon() {
        return this.mIcon;
    }

    public String getUri() {
        return this.mUri;
    }

    public String getKey() {
        return this.mKey;
    }

    public boolean isBot() {
        return this.mIsBot;
    }

    public boolean isImportant() {
        return this.mIsImportant;
    }
}
