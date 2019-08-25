package singleton;

/**
 * @author leeray
 * @version 1.0
 * @date 2019/8/24 0024 15:32
 * 枚举类型实现单例模式
 * 枚举本来就是final的，同时也是线程安全的。
 * 枚举类型不能够实现懒加载。
 */
public enum EnumSingleton {
    INSTANCE;

    private byte[] data = new byte[1024];

    EnumSingleton() {
        System.out.println("INSTANCE will be initialized immediately");
    }

    public static void method() {
    }

    public static EnumSingleton getInstance() {
        return INSTANCE;
    }
}
