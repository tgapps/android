package android.support.v13.app;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.support.annotation.RequiresApi;

@RequiresApi(15)
@TargetApi(15)
class FragmentCompatICSMR1 {
    FragmentCompatICSMR1() {
    }

    public static void setUserVisibleHint(Fragment f, boolean isVisible) {
        if (f.getFragmentManager() != null) {
            f.setUserVisibleHint(isVisible);
        }
    }
}
