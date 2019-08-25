package singleton;

/**
 * @author leeray
 * @version 1.0
 * @date 2019/8/24 0024 15:39
 * 用枚举实现的单例是无法实现懒加载的
 * 但是我们可以修改 代码，使用Holder的方式实现懒加载
 */
public class LazyEnumSingleton {

    //实例变量
    private byte[] data = new byte[1024];

    private LazyEnumSingleton() {
    }

    //使用枚举充当holder
    private enum EnumHolder {
        INSTANCE;

        private LazyEnumSingleton instance;

        EnumHolder() {
            this.instance = new LazyEnumSingleton();
        }

        private LazyEnumSingleton getInsance() {
            return instance;
        }
    }

    public static LazyEnumSingleton getInstance() {
        return EnumHolder.INSTANCE.getInsance();
    }
}
