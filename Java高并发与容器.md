# Java高并发总结

## 1. synchronized

###	1,synchronized关键字锁的是什么？

​		synchronized锁的是对象，而不是代码块。当synchronized修饰方法时，锁定的对象时当前类的实例对象，当当前方法是静态的时候，你们synchronized锁定的就是当前类的对象(.class,反射)。synchronized锁定堆中的对象，当一个线程获取到该锁时，其他线程要等待该线程释放锁，才能请求到锁，执行业务代码。

```java
public class T {
    private int count = 1000;
    private final Object lock = new Object();

    public void m() {
        synchronized (lock) { // 任何线程要执行下面的代码，都必须先拿到lock锁，锁信息记录在堆内存对象中的，不是在栈引用中
            // 如果lock已经被锁定，其他线程再进入时，就会进行阻塞等待
            // 所以 synchronized 是互斥锁
            count--;
            System.out.println(Thread.currentThread().getName() + " count = " + count);
        }
        // 当代码块执行完毕后，锁就会被释放，然后被其他线程获取
    }

    public static void main(String[] args) {
        T t = new T();
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                for (int j = 0;j<100;j++){
                    t.m();
                    try {
                        TimeUnit.MILLISECONDS.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }, "thread-" + i).start();
        }
    }
}
```

在上面的程序中，synchronized锁定的是t对象中的一个对象lock,所有线程在执行m方法时都要向lock申请锁，如果锁被占用就要等待。直到锁被释放。这里synchronized也可以锁定当前实例对象，即写法：synchronized(this)

	### 	2.synchronized修饰方法

```java
public class T implements Runnable{

    private int count = 10;

    @Override
    public synchronized void run() {
        count--;
        System.out.println(Thread.currentThread().getName() + " count = " + count);
    }

    public static void main(String[] args) {
        T t = new T();
        for (int i = 0; i < 5; i++) {
            new Thread(t).start();
        }
    }
}
```

### 	3.同步方法与非同步方法是否可以同时调用？

当然可以，同步方法需要获得锁，而非同步方法不需要，二者不会因为锁而发生冲突。

### 	4.脏读问题

```java
/**
 * 对业务写方法加锁，而对业务读方法不加锁，
 * 容易出现 脏读问题
 * <p>
 * 因为，在执行写的过程中，因为读操作没有加锁，所以读会读取到写未改完的脏数据
 * 解决办法，给读写都加锁
 */
public class Account {

    /**
     * 银行账户名称
     */
    String name;
    /**
     * 银行账余额
     */
    double balance;

    public synchronized void set(String name, double balance) {
        this.name = name;
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.balance = balance;
    }

    public /*synchronized*/ double getBalance() {
        return this.balance;
    }

    public static void main(String[] args) {
        Account a = new Account();
        new Thread(() -> a.set("张三", 100.0)).start();
        System.out.println(a.getBalance()); // 0.0 
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(a.getBalance()); // 100.0
    }
}
```

### 	5.synchronized是可重入锁

```java
/**
 * synchronized 是可重入锁
 * 即一个同步方法可以调用另外一个同步方法，一个线程已经拥有某个对象的锁，再次申请时仍然会得到该对象的锁
 */
public class T {

    synchronized void m1() {
        System.out.println("m1 start ");
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        m2();
    }

    synchronized void m2() {
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(" m2"); // 这句话会打印，调用m2时，不会发生死锁
    }
}
```

假设此时main方法中起了一个线程，线程调用m1方法，此时线程获得当前对象锁，计数器加1，然后m1中又要调用m2,m2也是加锁的，此时会使计数器再加1。m2执行完后，计数器减1，然后m1执行完，计数器再减1，释放锁。

### 	6.子类同步方法可以调用父类的同步方法吗？

可以的，synchonized是可重入的。

### 7.synchronized 代码块中，如果发生异常，锁会被释放 

```java
public class T {

    int count = 0;
    
    synchronized void m() {
        System.out.println(Thread.currentThread().getName() + " start");
        while (true) {
            count++;
            System.out.println(Thread.currentThread().getName() + " count=" + count);
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (count == 5) {  // 当count == 5 时，synchronized代码块会抛出异常
                int i = 1 / 0; 
            }
        }
    }

    public static void main(String[] args) {
        T t = new T();
        Runnable r = new Runnable() {
            @Override
            public void run() {
                t.m();
            }
        };
        new Thread(r, "t1").start(); // 执行到第5秒时，抛出 ArithmeticException 
        // 如果抛出异常后，t2 会继续执行，就代表t2拿到了锁，即t1在抛出异常后释放了锁
        
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        new Thread(r, "t2").start();
    }

}
```

###	8.volatile  

使一个变量在多个线程间可见 。

JMM(Java Memory Model) :所有的对象及其信息放在主内存中，而线程有单独的内存空间，只加载自己需要的数据，加载时在主内存中取数据。但是有这样的情况，几个线程读取主内存中数据后做修改并写回主内存，但是其他几个同时运行的线程还不知道该数据已经发生变化，继续使用旧数据做运算，得到的错误数据又写回主内存，发生错误，volatile修饰的变量保证它在主内存与各个线程内存区的可见性，当主内存中数据修改，会自动提示线程刷新数据，拿到最新的正确的数据。

注意： volatile并不能保证多个线程共同修改running变量所带来的不一致的问题，也就是说volatile不能替代synchronized，即 volatile只能保证**可见性**，不能保证原子性

### 	9.同步代码块中的语句越少越好 

### 	10.synchonized锁定的对象不要变

 锁定某个对象o，如果o属性发生变化，不影响锁的使用， 但是如果o编程另一个对象，则锁定的对象发生变化，所以锁对象通常要设置为 final类型，保证引用不可以变

### 	11.不要以字符串常量作为锁的对象。

### 	12.CountDownLatch & wait和notify 线程间通信

## 2. 并发容器

## 3.线程池

