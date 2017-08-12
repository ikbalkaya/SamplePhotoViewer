package ikbal.com.cookpadphotogallery;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import ikbal.com.cookpadphotogallery.utils.ConnectivityUtil;

/**
 * Created by ikbal on 12/08/2017.
 */

public class NetworkChangeReceiver extends BroadcastReceiver {
    private static final String TAG = NetworkChangeReceiver.class.getSimpleName();

    @Override
    public void onReceive(final Context context, final Intent intent) {
        int status = ConnectivityUtil.getConnectivityStatusString(context);
        if ("android.net.conn.CONNECTIVITY_CHANGE".equals(intent.getAction())) {
            if (status != ConnectivityUtil.NETWORK_STATUS_NOT_CONNECTED) {
                Log.d(TAG, "onReceive: should cache resources");

              //  InitialResourceCacher.getInstance().start(ZapmapApplication.getInstance().getApplicationContext(), this);
            }
        }
    }


}
