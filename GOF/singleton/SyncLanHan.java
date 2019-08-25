package singleton;

/**
 * @author leeray
 * @version 1.0
 * @date 2019/8/24 0024 14:19
 * 带锁的懒汉模式
 */
public final class SyncLanHan {
    /**
     * 懒汉模式虽然可以保证实例的懒加载，但是有并发问题；
     * 解决并发问题就是加锁嘛
     * 给getInstance()方法加锁，每个线程要并发地获取instance(共享资源)
     */
    //实例变量
    private byte[] data = new byte[1024];

    //定义实例，但不初始化。
    private static SyncLanHan instance = null;

    //私有构造器
    private SyncLanHan() {
    }

    //    加锁后的获取实例方法能够保证每次只有一个线程能够获取实例方法，保证实例的一致性。
//    同时这种方法的缺点也显现出来了，性能太差。
    public static synchronized SyncLanHan getInstance() {
        if (null == instance) {
            instance = new SyncLanHan();
        }
        return instance;
    }
}
