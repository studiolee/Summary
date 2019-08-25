package singleton;

import java.net.Socket;
import java.sql.Connection;

/**
 * @author leeray
 * @version 1.0
 * @date 2019/8/24 0024 14:29
 * 双重检查
 */
public final class DoubleCheck {
    //实例变量
    private byte[] data = new byte[1024];

    //定义实例，但不初始化。
    //volatile可以防止重排序而导致的空指针异常
    private /*volatile*/ static DoubleCheck instance = null;

    Connection conn;

    Socket socket;

    private DoubleCheck() {
        //本来是要对conn socket实例化的，这里方便起见，直接null了
        this.conn = null;
        this.socket = null;
    }

    /**
     * 当两个线程同时发现null == instance 时，只有一个线程拿到锁对instance实例化。
     * 随后的线程发现null == instance不成立，什么都不用做，退出就行。
     * 但是这样还是有问题，在多线程的情况下会有空指针异常。
     * jvm运行时的指令重排序和Happens-Before规则，conn,sockect,instance这三者的实例化顺序并无前后约束。
     * 那么如果时instance先被初始化，那么后两者可能还没有实例化就开始被调用，此时发生控制异常。
     * 为了防止这种情况的发生，可以在instance上加volatile关键字
     */
    public static DoubleCheck getInstance() {
        if (null == instance) {
            //当instance为null 时，进入同步代码块，同时避免每次都需要进入同步代码块，可以提高效率。
            synchronized (DoubleCheck.class) {
                //判断如果instance为null时则创建
                if (null == instance) {
                    instance = new DoubleCheck();
                }
            }
        }
        return instance;
    }
}
