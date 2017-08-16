## @SerializedName
现在与服务端通信通常都是Json，但是服务器返回的Json串的key是服务端自己的命名规则（可能是中文key，或者大写开头），我们直接通过Gson解析就必须一一对应，但是这样的命名在Java里很不友好。
那么就可以通过@SerializedName注解给Bean类进行“重命名”，这样在解析的时候便可以直接利用我们自己的命名规则命名变量了。

举个栗子：http://gank.io/api/day/2017/08/03

返回的数据包含``Android``，``福利``等不符合Java规范的key，那么就可以这样：
```
public class DataResults {
    @SerializedName("Android")
    List<ItemData> androidList;

    @SerializedName("iOS")
    List<ItemData> iOSList;

    @SerializedName("休息视频")
    List<ItemData> restList;

    @SerializedName("前端")
    List<ItemData> jsList;

    @SerializedName("福利")
    List<ItemData> welfareList;
}
```
如此便可以直接通过Gson解析，并且变量名是以我们自己的命名规则进行命令的了。

## Retrofit
研究下Retrofit相关的源码，写下感触颇深的几点：
1. 动态代理生成实现接口的对象
```
public <T> T create(final Class<T> service) {
    Utils.validateServiceInterface(service);
    if (validateEagerly) {
      eagerlyValidateMethods(service);
    }
    return (T) Proxy.newProxyInstance(service.getClassLoader(), new Class<?>[] { service },
        new InvocationHandler() {
          private final Platform platform = Platform.get();
    
          @Override public Object invoke(Object proxy, Method method, @Nullable Object[] args)
              throws Throwable {
            // If the method is a method from Object then defer to normal invocation.
            if (method.getDeclaringClass() == Object.class) {
              return method.invoke(this, args);
            }
            if (platform.isDefaultMethod(method)) {
              return platform.invokeDefaultMethod(method, service, proxy, args);
            }
            ServiceMethod<Object, Object> serviceMethod =
                (ServiceMethod<Object, Object>) loadServiceMethod(method);
            OkHttpCall<Object> okHttpCall = new OkHttpCall<>(serviceMethod, args);
            return serviceMethod.callAdapter.adapt(okHttpCall);
          }
        });
}
```

2. 通过Handler回调到主线程
```
public Retrofit build() {
  if (baseUrl == null) {
    throw new IllegalStateException("Base URL required.");
  }

  okhttp3.Call.Factory callFactory = this.callFactory;
  if (callFactory == null) {
    callFactory = new OkHttpClient();
  }

  Executor callbackExecutor = this.callbackExecutor;
  if (callbackExecutor == null) {
    callbackExecutor = platform.defaultCallbackExecutor();
  }

  // Make a defensive copy of the adapters and add the default Call adapter.
  List<CallAdapter.Factory> adapterFactories = new ArrayList<>(this.adapterFactories);
  adapterFactories.add(platform.defaultCallAdapterFactory(callbackExecutor));

  // Make a defensive copy of the converters.
  List<Converter.Factory> converterFactories = new ArrayList<>(this.converterFactories);

  return new Retrofit(callFactory, baseUrl, converterFactories, adapterFactories,
      callbackExecutor, validateEagerly);
}

platform = Platform.get();

private static Platform findPlatform() {
    try {
      Class.forName("android.os.Build");
      if (Build.VERSION.SDK_INT != 0) {
        return new Android();
      }
    } catch (ClassNotFoundException ignored) {
    }
    try {
      Class.forName("java.util.Optional");
      return new Java8();
    } catch (ClassNotFoundException ignored) {
    }
    return new Platform();
}

static class Android extends Platform {
    @Override public Executor defaultCallbackExecutor() {
      return new MainThreadExecutor();
    }

    @Override CallAdapter.Factory defaultCallAdapterFactory(@Nullable Executor callbackExecutor) {
      if (callbackExecutor == null) throw new AssertionError();
      return new ExecutorCallAdapterFactory(callbackExecutor);
    }

    static class MainThreadExecutor implements Executor {
      private final Handler handler = new Handler(Looper.getMainLooper());

      @Override public void execute(Runnable r) {
        handler.post(r);
      }
    }
}

@Override public void enqueue(final Callback<T> callback) {
  checkNotNull(callback, "callback == null");

  delegate.enqueue(new Callback<T>() {
    @Override public void onResponse(Call<T> call, final Response<T> response) {
      callbackExecutor.execute(new Runnable() {
        @Override public void run() {
          if (delegate.isCanceled()) {
            // Emulate OkHttp's behavior of throwing/delivering an IOException on cancellation.
            callback.onFailure(ExecutorCallbackCall.this, new IOException("Canceled"));
          } else {
            callback.onResponse(ExecutorCallbackCall.this, response);
          }
        }
      });
    }

    @Override public void onFailure(Call<T> call, final Throwable t) {
      callbackExecutor.execute(new Runnable() {
        @Override public void run() {
          callback.onFailure(ExecutorCallbackCall.this, t);
        }
      });
    }
  });
}
```

## RxJava

> a library for composing asynchronous and event-based programs using observable sequences for the Java VM

一个在 Java VM 上使用可观测的序列来组成异步的、基于事件的程序的库。
通常我们涉及到异步的操作会用 AsyncTask、Thread + Handler、IntentService 等等的方式，然后通过回调、或者发广播的形式接收返回值。
但是当我们有嵌套的异步任务调用时，就会产生 **回调地狱(迷之缩进)**，导致代码非常难以阅读。
通过 RxJava 可以将这一连串的操作通过一条链展示，提高代码的可读性。

基于观察者模式，最核心的几个要素 **Observable Observer subscribe**。目前仍然觉得 RxJava 难以入门，很多概念都需要花时间去理解。
觉得有什么弄不懂的，再去[给 Android 开发者的 RxJava 详解](http://gank.io/post/560e15be2dca930e00da1083)找找答案吧。

## Retrofit打印网络日志
在网络请求的时候想要打印请求的url或其他参数，需要通过拦截来实现:
```
OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder();
okHttpClient.addInterceptor(new Interceptor() {
    @Override
    public Response intercept(Chain chain) throws IOException {
        //获得请求信息，此处如有需要可以添加headers信息
        Request request = chain.request();
        //打印请求信息
        Logger.e("url:" + request.url());

        //记录请求耗时
        long startNs = System.nanoTime();
        okhttp3.Response response;
        try {
            //发送请求，获得相应，
            response = chain.proceed(request);
        } catch (Exception e) {
            throw e;
        }
        long tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);
        //打印请求耗时
        Logger.e("耗时:" + tookMs + "ms");
        //使用response获得headers(),可以更新本地Cookie。
        Headers headers = response.headers();
        Logger.e(headers.toString());

        //获得返回的body，注意此处不要使用responseBody.string()获取返回数据，原因在于这个方法会消耗返回结果的数据(buffer)
        ResponseBody responseBody = response.body();

        //为了不消耗buffer，我们这里使用source先获得buffer对象，然后clone()后使用
        BufferedSource source = responseBody.source();
        source.request(Long.MAX_VALUE); // Buffer the entire body.
        return response;
    }
});

Retrofit retrofit = new Retrofit.Builder()
        .baseUrl("http://gank.io/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .client(okHttpClient.build())
        .build();
```
## 水波纹效果

Android 5.0 及以上才有水波纹效果。简单实现:
```
<ripple xmlns:android="http://schemas.android.com/apk/res/android"
    android:color="?android:colorControlHighlight">

</ripple>
```
然后设置给布局的 background，布局必须可点击。同时为了兼容，最好建立``drawable-v21``来区分开来。建立低版本的 drawable 去对应 5.0 以下的版本。当然，目前也有很多想法可以实现 5.0 以下的水波纹效果，可以参考。
![android5.0 水波纹点击效果](http://www.jianshu.com/p/7d2a8a5836e0)
