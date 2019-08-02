# JDK8新特性：lambda表达式和Stream()接口

##第一章，函数式接口

### 1.1 概念

函数式接口在ava中是指:有且仅有一个抽象方法的接口。

函数式接口,即适用于函数式编程场景的接口。而ava中的函数式编程体现就是 Lambda,所以函数式接口就是可以适用于 Lambda使用的接口。只有确保接口中有且仅有一个抽象方法,Java中的 Lambda才能顺利地进行推导。

<!--备注:“语法糖”是指使用更加方便,但是原理不变的代码语法。例如在遍历集合时使用的for-each语法,其实底层的实现原理仍然是送代器,这便是语法糖。从应用层面来讲,Java中的 Lambda可以被当做是匿名内部类的语法糖,但是二者在原理上是不同的。-->

###1.2 格式

只要确保接口中有且仅有一个抽象方法即可

```java
修饰符 interface 接口名称{
    public abstract 返回值类型 方法名称(参数列表);
    //其他方法
    ......
}
```

### 1.3 注解判断方法

```java
@FunctionalInterface
```

使用该接口可以判断一个接口是不是函数式接口

### 1.4 函数式接口的用法

#### 	1. 作为方法的参数

#### 	2.作为方法的返回值

## 第二章，常用函数式接口

### 1.Supplier

```java
@FunctionalInterface
public interface Supplier<T> {

    /**
     * Gets a result.
     *
     * @return a result
     */
    T get();
}
```

Supplier接口又被称为生产者接口。故名思意，该接口用于生产数据。

### 2.Consumer

```java
@FunctionalInterface
public interface Consumer<T> {

    /**
     * Performs this operation on the given argument.
     *
     * @param t the input argument
     */
    void accept(T t);

    /**
     * Returns a composed {@code Consumer} that performs, in sequence, this
     * operation followed by the {@code after} operation. If performing either
     * operation throws an exception, it is relayed to the caller of the
     * composed operation.  If performing this operation throws an exception,
     * the {@code after} operation will not be performed.
     *
     * @param after the operation to perform after this operation
     * @return a composed {@code Consumer} that performs in sequence this
     * operation followed by the {@code after} operation
     * @throws NullPointerException if {@code after} is null
     */
    default Consumer<T> andThen(Consumer<? super T> after) {
        Objects.requireNonNull(after);
        return (T t) -> { accept(t); after.accept(t); };
    }
}
```

Consumer是一个消费者接口，用于消费数据，与Supplier恰好相反。该接口中除了一个抽象方法accept(T t)外，还有一个默认方法：andThen（）。andThen()用于连接两个消费接口。

### 3. Predicate 

```java
@FunctionalInterface
public interface Predicate<T> {

    /**
     * Evaluates this predicate on the given argument.
     *
     * @param t the input argument
     * @return {@code true} if the input argument matches the predicate,
     * otherwise {@code false}
     */
    boolean test(T t);

    default Predicate<T> and(Predicate<? super T> other) {
        Objects.requireNonNull(other);
        return (t) -> test(t) && other.test(t);
    }

    default Predicate<T> negate() {
        return (t) -> !test(t);
    }

    default Predicate<T> or(Predicate<? super T> other) {
        Objects.requireNonNull(other);
        return (t) -> test(t) || other.test(t);
    }

    static <T> Predicate<T> isEqual(Object targetRef) {
        return (null == targetRef)
                ? Objects::isNull
                : object -> targetRef.equals(object);
    }
}
```

Predicate接口用于判断，其中还有许多默认方法，and()，or()，negate()分别表示与，或，非。

### 4. Function

```java
@FunctionalInterface
public interface Function<T, R> {

    /**
     * Applies this function to the given argument.
     *
     * @param t the function argument
     * @return the function result
     */
    R apply(T t);

    default <V> Function<V, R> compose(Function<? super V, ? extends T> before) {
        Objects.requireNonNull(before);
        return (V v) -> apply(before.apply(v));
    }

    default <V> Function<T, V> andThen(Function<? super R, ? extends V> after) {
        Objects.requireNonNull(after);
        return (T t) -> after.apply(apply(t));
    }

    static <T> Function<T, T> identity() {
        return t -> t;
    }
}
```

```java
Function<T, R>用于转换数据，将T类型数据转成R类型数据。
```

###5. Comparator

```java
@FunctionalInterface
public interface Comparator<T> {  
    int compare(T o1, T o2);
}
```

用于比较。

## 第三章，Stream流

Stream接口是jdk1.8的新接口。与lambda表达式一样同属于1.8的新特性。

Stream流的特点：一次性，延迟加载。

### 1.获取流的方式

### 2.方法种类

	#### 	1.延迟方法：返回值是Stream，除以下两种方法外都是延迟方法。

#### 	2.终结方法：返回的不是Stream，如：count,foreach。

### 3.常见方法

#### 1.forEach

#### 2.filter

#### 3.map

#### 4.count

#### 5.limit

#### 6.skip

#### 7.concat







​				



