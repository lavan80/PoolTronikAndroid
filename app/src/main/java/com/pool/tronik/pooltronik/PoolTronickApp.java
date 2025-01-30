package com.pool.tronik.pooltronik;

import android.app.Application;

import com.pool.tronik.pooltronik.net.NetConfig;
import com.pool.tronik.pooltronik.utils.FileUtil;
import com.facebook.stetho.Stetho;


public class PoolTronickApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        NetConfig.BASE_CONTROLLER_URL = FileUtil.getIp(this);
        NetConfig.BASE_SERVER_URL = FileUtil.getServerIp(this);

        /*if(BuildConfig.DEBUG) {
            // Create an InitializerBuilder
            Stetho.InitializerBuilder initializerBuilder =
                    Stetho.newInitializerBuilder(this);

            // Enable Chrome DevTools
            initializerBuilder.enableWebKitInspector(
                    Stetho.defaultInspectorModulesProvider(this)
            );

            // Enable command line interface
            initializerBuilder.enableDumpapp(
                    Stetho.defaultDumperPluginsProvider(this)
            );

            // Use the InitializerBuilder to generate an Initializer
            Stetho.Initializer initializer = initializerBuilder.build();

            // Initiali
            // ze Stetho with the Initializer
            Stetho.initialize(initializer);
        }*/

    }
}
