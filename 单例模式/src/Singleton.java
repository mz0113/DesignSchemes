/**
 * 懒汉式
 * -》线程不安全
 * -》延迟加载
 * 这种方式是最基本的实现方式，这种实现最大的问题就是不支持多线程。因为没有加锁 synchronized，所以严格意义上它并不算单例模式。
 * 这种方式 lazy loading 很明显，不要求线程安全，在多线程不能正常工作。
 */
public class Singleton {
    private static Singleton instance;
    private Singleton(){};

    public static Singleton getInstance(){
        if(instance==null){
            instance = new Singleton();
        }
        return instance;
    }
}


/**
 * 懒汉式(同步锁)
 * -》线程不安全
 * -》延迟加载
 * 这种方式具备很好的 lazy loading，能够在多线程中很好的工作，但是，效率很低，99% 情况下不需要同步。
 * 优点：第一次调用才初始化，避免内存浪费。
 * 缺点：必须加锁 synchronized 才能保证单例，但加锁会影响效率。
 * getInstance() 的性能对应用程序不是很关键（该方法使用不太频繁）。
 */
class Singleton_2 {
    private static Singleton_2 instance;
    private Singleton_2(){};

    public synchronized static Singleton_2 getInstance(){
        if(instance==null){
            instance = new Singleton_2();
        }
        return instance;
    }
}

/**
 * 饿汉式
 * -》线程安全
 * -》非延迟加载
 * 优点：没有加锁，执行效率会提高。
 * 缺点：类加载时就初始化，浪费内存。
 * 它基于 classloader 机制避免了多线程的同步问题，不过，instance 在类装载时就实例化，虽然导致类装载的原因有很多种，在单例模式中大多数都是调用 getInstance 方法，
 * 但是也不能确定有其他的方式（或者其他的静态方法）导致类装载，这时候初始化 instance 显然没有达到 lazy loading 的效果。
 */
class Singleton_3 {
    private static Singleton_3 instance = new Singleton_3();
    private Singleton_3 (){}
    public static Singleton_3 getInstance() {
        return instance;
    }
}

/**
 * 双检锁/双重校验锁（DCL，即 double-checked locking）
 * 是否 Lazy 初始化：是
 * 是否多线程安全：是
 * 实现难度：较复杂
 * 描述：这种方式采用双锁机制，安全且在多线程情况下能保持高性能。
 * getInstance() 的性能对应用程序很关键。
 *
 *
 * 似乎解决了之前提到的问题，将synchronized关键字加在了内部，也就是说当调用的时候是不需要加锁的，只有在instance为null，并创建对象的时候才需要加锁，性能有一定的提升。但是，这样的情况，还是有可能有问题的，看下面的情况：在Java指令中创建对象和赋值操作是分开进行的，也就是说instance = new Singleton();语句是分两步执行的。但是JVM并不保证这两个操作的先后顺序，也就是说有可能JVM会为新的Singleton实例分配空间，然后直接赋值给instance成员，然后再去初始化这个Singleton实例。这样就可能出错了，我们以A、B两个线程为例：
 * a>A、B线程同时进入了第一个if判断
 * b>A首先进入synchronized块，由于instance为null，所以它执行instance = new Singleton();
 * c>由于JVM内部的优化机制，JVM先画出了一些分配给Singleton实例的空白内存，并赋值给instance成员（注意此时JVM没有开始初始化这个实例），然后A离开了synchronized块。
 * d>B进入synchronized块，由于instance此时不是null，因此它马上离开了synchronized块并将结果返回给调用该方法的程序。
 * e>此时B线程打算使用Singleton实例，却发现它没有被初始化，于是错误发生了。
 */
class Singleton_4 {
    private volatile static Singleton_4 singleton;
    private Singleton_4 (){}
    public static Singleton_4 getSingleton() {
        if (singleton == null) {
            synchronized (Singleton.class) {
                if (singleton == null) {
                    singleton = new Singleton_4();
                }
            }
        }
        return singleton;
    }
}

/**
 * 登记式/静态内部类
 *
 * 是否 Lazy 初始化：是
 * 是否多线程安全：是
 *
 * 这种方式能达到双检锁方式一样的功效，但实现更简单。对静态域使用延迟初始化，应使用这种方式而不是双检锁方式。这种方式只适用于静态域的情况，
 * 双检锁方式可在实例域需要延迟初始化时使用。
 * 这种方式同样利用了 classloader 机制来保证初始化 instance 时只有一个线程，它跟第 3 种方式不同的是：
 * 第 3 种方式只要 Singleton 类被装载了，那么 instance 就会被实例化（没有达到 lazy loading 效果），
 * 而这种方式是 Singleton 类被装载了，instance 不一定被初始化。因为 SingletonHolder 类没有被主动使用，
 * 只有通过显式调用 getInstance 方法时，才会显式装载 SingletonHolder 类，从而实例化 instance。想象一下，
 * 如果实例化 instance 很消耗资源，所以想让它延迟加载，另外一方面，又不希望在 Singleton 类加载时就实例化，
 * 因为不能确保 Singleton 类还可能在其他的地方被主动使用从而被加载，
 * 那么这个时候实例化 instance 显然是不合适的。这个时候，这种方式相比第 3 种方式就显得很合理。
 */
class Singleton_5 {
    private static class SingletonHolder {
        private static final Singleton_5 INSTANCE = new Singleton_5();
    }
    private Singleton_5 (){}
    public static final Singleton_5 getInstance() {
        return SingletonHolder.INSTANCE;
    }
}

/**
 * 是否 Lazy 初始化：否
 * 是否多线程安全：是
 *
 * 这种实现方式还没有被广泛采用，但这是实现单例模式的最佳方法。它更简洁，自动支持序列化机制，绝对防止多次实例化。
 * 它不仅能避免多线程同步问题，而且还自动支持序列化机制，防止反序列化重新创建新的对象，绝对防止多次实例化
 */
enum Singleton_6 {
    INSTANCE;
    public void whateverMethod() {
    }
}