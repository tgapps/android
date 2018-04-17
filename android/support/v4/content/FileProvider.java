package android.support.v4.content;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.ProviderInfo;
import android.content.res.XmlResourceParser;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Build.VERSION;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.text.TextUtils;
import android.webkit.MimeTypeMap;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;
import org.xmlpull.v1.XmlPullParserException;

public class FileProvider extends ContentProvider {
    private static final String[] COLUMNS = new String[]{"_display_name", "_size"};
    private static final File DEVICE_ROOT = new File("/");
    private static HashMap<String, PathStrategy> sCache = new HashMap();
    private PathStrategy mStrategy;

    interface PathStrategy {
        File getFileForUri(Uri uri);

        Uri getUriForFile(File file);
    }

    static class SimplePathStrategy implements PathStrategy {
        private final String mAuthority;
        private final HashMap<String, File> mRoots = new HashMap();

        SimplePathStrategy(String authority) {
            this.mAuthority = authority;
        }

        void addRoot(String name, File root) {
            if (TextUtils.isEmpty(name)) {
                throw new IllegalArgumentException("Name must not be empty");
            }
            try {
                this.mRoots.put(name, root.getCanonicalFile());
            } catch (IOException e) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Failed to resolve canonical path for ");
                stringBuilder.append(root);
                throw new IllegalArgumentException(stringBuilder.toString(), e);
            }
        }

        public Uri getUriForFile(File file) {
            try {
                String path = file.getCanonicalPath();
                Entry<String, File> mostSpecific = null;
                for (Entry<String, File> root : this.mRoots.entrySet()) {
                    String rootPath = ((File) root.getValue()).getPath();
                    if (path.startsWith(rootPath) && (mostSpecific == null || rootPath.length() > ((File) mostSpecific.getValue()).getPath().length())) {
                        mostSpecific = root;
                    }
                }
                StringBuilder stringBuilder;
                if (mostSpecific == null) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("Failed to find configured root that contains ");
                    stringBuilder.append(path);
                    throw new IllegalArgumentException(stringBuilder.toString());
                }
                String rootPath2 = ((File) mostSpecific.getValue()).getPath();
                if (rootPath2.endsWith("/")) {
                    path = path.substring(rootPath2.length());
                } else {
                    path = path.substring(rootPath2.length() + 1);
                }
                stringBuilder = new StringBuilder();
                stringBuilder.append(Uri.encode((String) mostSpecific.getKey()));
                stringBuilder.append('/');
                stringBuilder.append(Uri.encode(path, "/"));
                return new Builder().scheme("content").authority(this.mAuthority).encodedPath(stringBuilder.toString()).build();
            } catch (IOException e) {
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append("Failed to resolve canonical path for ");
                stringBuilder2.append(file);
                throw new IllegalArgumentException(stringBuilder2.toString());
            }
        }

        public File getFileForUri(Uri uri) {
            String path = uri.getEncodedPath();
            int splitIndex = path.indexOf(47, 1);
            String tag = Uri.decode(path.substring(1, splitIndex));
            path = Uri.decode(path.substring(splitIndex + 1));
            File root = (File) this.mRoots.get(tag);
            if (root == null) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Unable to find configured root for ");
                stringBuilder.append(uri);
                throw new IllegalArgumentException(stringBuilder.toString());
            }
            File file = new File(root, path);
            try {
                file = file.getCanonicalFile();
                if (file.getPath().startsWith(root.getPath())) {
                    return file;
                }
                throw new SecurityException("Resolved path jumped beyond configured root");
            } catch (IOException e) {
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append("Failed to resolve canonical path for ");
                stringBuilder2.append(file);
                throw new IllegalArgumentException(stringBuilder2.toString());
            }
        }
    }

    private static int modeToMode(java.lang.String r1) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: android.support.v4.content.FileProvider.modeToMode(java.lang.String):int
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:116)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:249)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
Caused by: java.lang.NullPointerException
*/
        /*
        r0 = "r";
        r0 = r0.equals(r3);
        if (r0 == 0) goto L_0x000b;
    L_0x0008:
        r0 = 268435456; // 0x10000000 float:2.5243549E-29 double:1.32624737E-315;
        goto L_0x0057;
    L_0x000b:
        r0 = "w";
        r0 = r0.equals(r3);
        if (r0 != 0) goto L_0x0054;
        r0 = "wt";
        r0 = r0.equals(r3);
        if (r0 == 0) goto L_0x001c;
        goto L_0x0054;
        r0 = "wa";
        r0 = r0.equals(r3);
        if (r0 == 0) goto L_0x0027;
        r0 = 704643072; // 0x2a000000 float:1.1368684E-13 double:3.481399345E-315;
        goto L_0x000a;
        r0 = "rw";
        r0 = r0.equals(r3);
        if (r0 == 0) goto L_0x0032;
        r0 = 939524096; // 0x38000000 float:3.0517578E-5 double:4.641865793E-315;
        goto L_0x000a;
        r0 = "rwt";
        r0 = r0.equals(r3);
        if (r0 == 0) goto L_0x003d;
        r0 = 1006632960; // 0x3c000000 float:0.0078125 double:4.973427635E-315;
        goto L_0x000a;
        r0 = new java.lang.IllegalArgumentException;
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "Invalid mode: ";
        r1.append(r2);
        r1.append(r3);
        r1 = r1.toString();
        r0.<init>(r1);
        throw r0;
        r0 = 738197504; // 0x2c000000 float:1.8189894E-12 double:3.647180266E-315;
        goto L_0x000a;
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v4.content.FileProvider.modeToMode(java.lang.String):int");
    }

    public boolean onCreate() {
        return true;
    }

    public void attachInfo(Context context, ProviderInfo info) {
        super.attachInfo(context, info);
        if (info.exported) {
            throw new SecurityException("Provider must not be exported");
        } else if (info.grantUriPermissions) {
            this.mStrategy = getPathStrategy(context, info.authority);
        } else {
            throw new SecurityException("Provider must grant uri permissions");
        }
    }

    public static Uri getUriForFile(Context context, String authority, File file) {
        return getPathStrategy(context, authority).getUriForFile(file);
    }

    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        File file = this.mStrategy.getFileForUri(uri);
        if (projection == null) {
            projection = COLUMNS;
        }
        String[] cols = new String[projection.length];
        Object[] values = new Object[projection.length];
        int i = 0;
        for (String col : projection) {
            int i2;
            if ("_display_name".equals(col)) {
                cols[i] = "_display_name";
                i2 = i + 1;
                values[i] = file.getName();
            } else if ("_size".equals(col)) {
                cols[i] = "_size";
                i2 = i + 1;
                values[i] = Long.valueOf(file.length());
            } else {
            }
            i = i2;
        }
        cols = copyOf(cols, i);
        values = copyOf(values, i);
        MatrixCursor cursor = new MatrixCursor(cols, 1);
        cursor.addRow(values);
        return cursor;
    }

    public String getType(Uri uri) {
        File file = this.mStrategy.getFileForUri(uri);
        int lastDot = file.getName().lastIndexOf(46);
        if (lastDot >= 0) {
            String mime = MimeTypeMap.getSingleton().getMimeTypeFromExtension(file.getName().substring(lastDot + 1));
            if (mime != null) {
                return mime;
            }
        }
        return "application/octet-stream";
    }

    public Uri insert(Uri uri, ContentValues values) {
        throw new UnsupportedOperationException("No external inserts");
    }

    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        throw new UnsupportedOperationException("No external updates");
    }

    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return this.mStrategy.getFileForUri(uri).delete();
    }

    public ParcelFileDescriptor openFile(Uri uri, String mode) throws FileNotFoundException {
        return ParcelFileDescriptor.open(this.mStrategy.getFileForUri(uri), modeToMode(mode));
    }

    private static PathStrategy getPathStrategy(Context context, String authority) {
        PathStrategy strat;
        synchronized (sCache) {
            strat = (PathStrategy) sCache.get(authority);
            if (strat == null) {
                try {
                    strat = parsePathStrategy(context, authority);
                    sCache.put(authority, strat);
                } catch (IOException e) {
                    throw new IllegalArgumentException("Failed to parse android.support.FILE_PROVIDER_PATHS meta-data", e);
                } catch (XmlPullParserException e2) {
                    throw new IllegalArgumentException("Failed to parse android.support.FILE_PROVIDER_PATHS meta-data", e2);
                }
            }
        }
        return strat;
    }

    private static PathStrategy parsePathStrategy(Context context, String authority) throws IOException, XmlPullParserException {
        SimplePathStrategy strat = new SimplePathStrategy(authority);
        XmlResourceParser in = context.getPackageManager().resolveContentProvider(authority, 128).loadXmlMetaData(context.getPackageManager(), "android.support.FILE_PROVIDER_PATHS");
        if (in == null) {
            throw new IllegalArgumentException("Missing android.support.FILE_PROVIDER_PATHS meta-data");
        }
        while (true) {
            int next = in.next();
            int type = next;
            if (next == 1) {
                return strat;
            }
            if (type == 2) {
                String tag = in.getName();
                String name = in.getAttributeValue(null, "name");
                String path = in.getAttributeValue(null, "path");
                File target = null;
                if ("root-path".equals(tag)) {
                    target = DEVICE_ROOT;
                } else if ("files-path".equals(tag)) {
                    target = context.getFilesDir();
                } else if ("cache-path".equals(tag)) {
                    target = context.getCacheDir();
                } else if ("external-path".equals(tag)) {
                    target = Environment.getExternalStorageDirectory();
                } else if ("external-files-path".equals(tag)) {
                    externalFilesDirs = ContextCompat.getExternalFilesDirs(context, null);
                    if (externalFilesDirs.length > 0) {
                        target = externalFilesDirs[0];
                    }
                } else if ("external-cache-path".equals(tag)) {
                    externalFilesDirs = ContextCompat.getExternalCacheDirs(context);
                    if (externalFilesDirs.length > 0) {
                        target = externalFilesDirs[0];
                    }
                } else if (VERSION.SDK_INT >= 21 && "external-media-path".equals(tag)) {
                    externalFilesDirs = context.getExternalMediaDirs();
                    if (externalFilesDirs.length > 0) {
                        target = externalFilesDirs[0];
                    }
                }
                if (target != null) {
                    strat.addRoot(name, buildPath(target, path));
                }
            }
        }
    }

    private static File buildPath(File base, String... segments) {
        File cur = base;
        for (String segment : segments) {
            if (segment != null) {
                cur = new File(cur, segment);
            }
        }
        return cur;
    }

    private static String[] copyOf(String[] original, int newLength) {
        String[] result = new String[newLength];
        System.arraycopy(original, 0, result, 0, newLength);
        return result;
    }

    private static Object[] copyOf(Object[] original, int newLength) {
        Object[] result = new Object[newLength];
        System.arraycopy(original, 0, result, 0, newLength);
        return result;
    }
}
