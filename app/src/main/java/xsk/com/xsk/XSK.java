package xsk.com.xsk;

import android.app.Application;

import io.realm.Realm;

/**
 * @time: 2019-06-05$ 10:35$
 * @Author: 如梦一般
 * @email: 994158307@qq.com
 */
public class XSK extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FakeX509TrustManager.allowAllSSL();
        Realm.init(this);
    }
}
