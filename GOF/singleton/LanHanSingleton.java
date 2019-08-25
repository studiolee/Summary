package singleton;

import java.util.HashSet;
import java.util.Set;

/**
 * @author leeray
 * @version 1.0
 * @date 2019/8/24 0024 13:50
 * 懒汉模式：所谓懒汉模式就是在使用类的时候再去创建（用时创建）这样可以避免类在初始化时提前创建。
 */
public final class LanHanSingleton {
    //实例变量
    private byte[] data = new byte[1024];

    //定义实例，但不初始化。
    private static LanHanSingleton instance = null;

    //私有构造器
    private LanHanSingleton() {
    }

    public static LanHanSingleton getInstance() {
        if (null == instance) {
            instance = new LanHanSingleton();
        }
        return instance;
    }

    /**
     * LanHanSingleton 的类变量instance = null ，因此当该类被初始化的时候instance并不会被实例化。
     * 在getInstance()方法中会判断instance实例是否会被实例化。
     * 懒汉模式会有并发问题
     * 比如：多个线程并发访问LanHanSingleton的getInstance方法，某时间点有两个线程同时看到instance==null
     * 那么两个线程都会new 实例导致单例失败。
     */


    public static void main(String[] args) {
        Set<LanHanSingleton> set = new HashSet<>();
        for (int i = 0; i < 10000; i++) {
            new Thread(() -> {
                for (int j = 0; j < 10; j++) {
                    set.add(LanHanSingleton.getInstance());
                }
            }).start();
        }
        System.out.println(set.size());
    }
}
