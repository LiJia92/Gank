## @SerializedName
现在与服务端通信通常都是Json，但是服务器返回的Json串的key是服务端自己的命名规则（可能是中文key，或者大写开头），我们直接通过Gson解析就必须一一对应，但是这样的命名在Java里很不友好。
那么就可以通过@SerializedName注解给Bean类进行“重命名”，这样在解析的时候便可以直接利用我们自己的命名规则命名变量了。

举个栗子：http://gank.io/api/day/2017/08/03　返回的数据包含``Android``，``福利``等不符合Java规范的key，那么就可以这样：
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