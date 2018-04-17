package com.coremedia.iso;

import com.coremedia.iso.boxes.Box;
import com.coremedia.iso.boxes.UserBox;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PropertyBoxParserImpl extends AbstractBoxParser {
    static String[] EMPTY_STRING_ARRAY = new String[0];
    StringBuilder buildLookupStrings = new StringBuilder();
    String clazzName;
    Pattern constuctorPattern = Pattern.compile("(.*)\\((.*?)\\)");
    Properties mapping;
    String[] param;

    public PropertyBoxParserImpl(String... customProperties) {
        InputStream is = getClass().getResourceAsStream("/isoparser-default.properties");
        try {
            this.mapping = new Properties();
            this.mapping.load(is);
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            if (cl == null) {
                cl = ClassLoader.getSystemClassLoader();
            }
            Enumeration<URL> enumeration = cl.getResources("isoparser-custom.properties");
            while (enumeration.hasMoreElements()) {
                InputStream customIS = ((URL) enumeration.nextElement()).openStream();
                try {
                    this.mapping.load(customIS);
                    customIS.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (Throwable th) {
                    customIS.close();
                }
            }
            for (String customProperty : customProperties) {
                this.mapping.load(getClass().getResourceAsStream(customProperty));
            }
            try {
                is.close();
            } catch (ClassLoader cl2) {
                cl2.printStackTrace();
            }
        } catch (Throwable th2) {
            try {
                is.close();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }
    }

    public PropertyBoxParserImpl(Properties mapping) {
        this.mapping = mapping;
    }

    public Box createBox(String type, byte[] userType, String parent) {
        invoke(type, userType, parent);
        try {
            Class<Box> clazz = Class.forName(this.clazzName);
            if (this.param.length <= 0) {
                return (Box) clazz.newInstance();
            }
            Class[] constructorArgsClazz = new Class[this.param.length];
            Object[] constructorArgs = new Object[this.param.length];
            for (int i = 0; i < this.param.length; i++) {
                if ("userType".equals(this.param[i])) {
                    constructorArgs[i] = userType;
                    constructorArgsClazz[i] = byte[].class;
                } else if ("type".equals(this.param[i])) {
                    constructorArgs[i] = type;
                    constructorArgsClazz[i] = String.class;
                } else if ("parent".equals(this.param[i])) {
                    constructorArgs[i] = parent;
                    constructorArgsClazz[i] = String.class;
                } else {
                    StringBuilder stringBuilder = new StringBuilder("No such param: ");
                    stringBuilder.append(this.param[i]);
                    throw new InternalError(stringBuilder.toString());
                }
            }
            return (Box) clazz.getConstructor(constructorArgsClazz).newInstance(constructorArgs);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e2) {
            throw new RuntimeException(e2);
        } catch (IllegalAccessException e3) {
            throw new RuntimeException(e3);
        } catch (InvocationTargetException e4) {
            throw new RuntimeException(e4);
        } catch (NoSuchMethodException e5) {
            throw new RuntimeException(e5);
        }
    }

    public void invoke(String type, byte[] userType, String parent) {
        String constructor;
        if (userType == null) {
            constructor = this.mapping.getProperty(type);
            if (constructor == null) {
                String lookup = this.buildLookupStrings;
                lookup.append(parent);
                lookup.append('-');
                lookup.append(type);
                lookup = lookup.toString();
                this.buildLookupStrings.setLength(0);
                constructor = this.mapping.getProperty(lookup);
            }
        } else if (UserBox.TYPE.equals(type)) {
            constructor = this.mapping;
            StringBuilder stringBuilder = new StringBuilder("uuid[");
            stringBuilder.append(Hex.encodeHex(userType).toUpperCase());
            stringBuilder.append("]");
            constructor = constructor.getProperty(stringBuilder.toString());
            if (constructor == null) {
                Properties properties = this.mapping;
                StringBuilder stringBuilder2 = new StringBuilder(String.valueOf(parent));
                stringBuilder2.append("-uuid[");
                stringBuilder2.append(Hex.encodeHex(userType).toUpperCase());
                stringBuilder2.append("]");
                constructor = properties.getProperty(stringBuilder2.toString());
            }
            if (constructor == null) {
                constructor = this.mapping.getProperty(UserBox.TYPE);
            }
        } else {
            throw new RuntimeException("we have a userType but no uuid box type. Something's wrong");
        }
        if (constructor == null) {
            constructor = this.mapping.getProperty("default");
        }
        if (constructor == null) {
            stringBuilder = new StringBuilder("No box object found for ");
            stringBuilder.append(type);
            throw new RuntimeException(stringBuilder.toString());
        } else if (constructor.endsWith(")")) {
            Matcher m = this.constuctorPattern.matcher(constructor);
            if (m.matches()) {
                this.clazzName = m.group(1);
                if (m.group(2).length() == 0) {
                    this.param = EMPTY_STRING_ARRAY;
                    return;
                } else {
                    this.param = m.group(2).length() > 0 ? m.group(2).split(",") : new String[0];
                    return;
                }
            }
            StringBuilder stringBuilder3 = new StringBuilder("Cannot work with that constructor: ");
            stringBuilder3.append(constructor);
            throw new RuntimeException(stringBuilder3.toString());
        } else {
            this.param = EMPTY_STRING_ARRAY;
            this.clazzName = constructor;
        }
    }
}
