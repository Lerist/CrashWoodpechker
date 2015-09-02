#CrashWoodpecker

A Uncaught Exception Handler library like as Square's [LeakCanary](https://raw.githubusercontent.com/square/leakcanary).

![screenshot.png](art/s2.png)

## Getting started

In your `Application` class:

```java
public class ExampleApplication extends Application {

  @Override public void onCreate() {
    super.onCreate();
    CrashWoodpecker.fly().to(this)
  }
}
```

**That is all!** CrashWoodpecker will automatically show a Activity when your app crash with uncaught exceptions in your debug build.
