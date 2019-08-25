package singleton;

/**
 * @author leeray
 * @version 1.0
 * @date 2019/8/24 0024 13:32
 * 饿汉模式
 */
public final class EHanSingleton {
    //实例变量
    private byte[] data = new byte[1024];

    //在定义实例对象的时候直接初始化
    private static EHanSingleton instance = new EHanSingleton();

    //私有构造器，不允许使用new来创建实例对象
    private EHanSingleton() {
    }

    //获取实例对象的方法
    public static EHanSingleton getInstance() {
        return instance;
    }

    /*
     * 饿汉模式的关键在于instance作为类变量并且直接得到初始化
     * 如果直接使用EHanSingleton类，那么Instance实例会直接完成创建，包括其中的实例变量都会得到初始化。
     * 比如1K的 data 会得到初始化。
     * instance在作为类变量在初始化的过程中会被收集进<clinit>()方法中，该方法能够100%保证同步。
     *
     * 使用情况：类的成员属性较少，占用资源不多的情况。
     * 缺点：无法实现懒加载。
     */


    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                for (int j = 0; j < 10; j++) {
                    System.out.println(EHanSingleton.getInstance());
                }
            }, "thread-" + i).start();
        }
    }
}
