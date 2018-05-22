package android.support.v4.content;

import android.content.Context;
import android.os.Binder;
import android.os.Process;
import android.support.v4.app.AppOpsManagerCompat;

public final class PermissionChecker {
    public static int checkPermission(Context context, String permission, int pid, int uid, String packageName) {
        if (context.checkPermission(permission, pid, uid) == -1) {
            return -1;
        }
        String op = AppOpsManagerCompat.permissionToOp(permission);
        if (op == null) {
            return 0;
        }
        if (packageName == null) {
            String[] packageNames = context.getPackageManager().getPackagesForUid(uid);
            if (packageNames == null || packageNames.length <= 0) {
                return -1;
            }
            packageName = packageNames[0];
        }
        return AppOpsManagerCompat.noteProxyOpNoThrow(context, op, packageName) != 0 ? -2 : 0;
    }

    public static int checkCallingOrSelfPermission(Context context, String permission) {
        return checkPermission(context, permission, Binder.getCallingPid(), Binder.getCallingUid(), Binder.getCallingPid() == Process.myPid() ? context.getPackageName() : null);
    }
}
