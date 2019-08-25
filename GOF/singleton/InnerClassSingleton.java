package singleton;

/**
 * @author leeray
 * @version 1.0
 * @date 2019/8/24 0024 15:14
 * 通过匿名内部类
 */
public final class InnerClassSingleton {

    private byte[] data = new byte[1024];

    private InnerClassSingleton() {
    }

    //在静态内部类中持有InnerClassSingleton的实例，并且可以被直接初始化
    private static class Holder {
        private static InnerClassSingleton instance = new InnerClassSingleton();
    }

    //调用getInstance()方法，实际上是获得Holder的instance静态属性。
    public static InnerClassSingleton getInstance() {
        return Holder.instance;
    }
    /**
     * 当Holder被主动引用时则会创建InnerClassSingleton实例，同饿汉方法一样
     * 这个实例在程序编译时期收集至<clinit>()方法中，保证了同步，内存可见性，jvm指令的顺序性和原子性。
     * 非常常用。
     */
}
