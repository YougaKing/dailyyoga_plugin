package com.dailyyoga.h2;

import android.app.Activity;
import android.app.Application;

import com.dailyyoga.cn.detection.Lib;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author: YougaKingWu@gmail.com
 * @created on: 2019/02/28 10:33
 * @description:
 */
public abstract class PlatformProvider {

    private static final Iterable<Class<?>> HARDCODED_CLASSES = new HardcodedClasses();

    private static PlatformProvider provider = PlatformProviderLoader.load(
            PlatformProvider.class,
            HARDCODED_CLASSES);

    public static PlatformProvider provider() {
        if (provider == null) {
            provider = new PlatformProvider() {
                @Override
                public void initialize(Application application) {
                }

                @Override
                public void connect(Activity activity) {
                }

                @Override
                public LoginProvider loginProvider() {
                    return new LoginProvider() {};
                }

                @Override
                public PushProvider pushProvider() {
                    return new PushProvider() {};
                }

                @Override
                public PayProvider payProvider() {
                    return new PayProvider() {};
                }

                @Override
                public List<Lib> necessaryLib() {
                    return new ArrayList<>();
                }
            };
        }
        return provider;
    }

    public abstract void initialize(Application application);

    public abstract void connect(Activity activity);

    public abstract LoginProvider loginProvider();

    public abstract PushProvider pushProvider();

    public abstract PayProvider payProvider();

    public abstract List<Lib> necessaryLib();

    public interface PlatformLoginListener {
        void onComplete(Map<String, String> map);

        void showToast(String message);

        void resultCode(int result);
    }

    public interface PlatformPushListener {
        default void setPushType() {}

        void onFail();

        void onSuccess();
    }

    public interface PlatformPayListener {
        default void onLoginComplete(Map<String, String> map) {}

        void onSuccess();

        void onFail(String message);

        default void onCancel() {}
    }

    private static final class HardcodedClasses implements Iterable<Class<?>> {
        @Override
        public Iterator<Class<?>> iterator() {
            List<Class<?>> list = new ArrayList<>();
            try {
                list.add(Class.forName("com.dailyyoga.h2.HuaWeiProvider"));
            } catch (ClassNotFoundException ex) {
                // ignore
            }
            try {
                list.add(Class.forName("com.dailyyoga.h2.OppoProvider"));
            } catch (ClassNotFoundException ex) {
                // ignore
            }
            try {
                list.add(Class.forName("com.dailyyoga.h2.VivoProvider"));
            } catch (ClassNotFoundException ex) {
                // ignore
            }
            try {
                list.add(Class.forName("com.dailyyoga.h2.XiaoMiProvider"));
            } catch (ClassNotFoundException ex) {
                // ignore
            }
            return list.iterator();
        }
    }
}
