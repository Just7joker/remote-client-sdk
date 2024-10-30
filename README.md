# remote-client-sdk
## 概述
remote-client-sdk是一个高性能、可扩展、专门用来提供第三方远程调用服务接口的项目
## 特性
* 高性能：基于Spring Boot，提供快速响应和高并发处理能力。
* 易于集成：提供简单易用的API，支持快速集成到现有项目中。
* 扩展性强：支持自定义消息处理逻辑和模型配置，满足复杂业务需求。
## 快速开始
### 环境要求
* JDK 8或更高版本
* Maven 3.6.0或更高版本
* SpringBoot 2.X 版本
### 配置
1. 在pom.xml文件中添加以下依赖：
```xml
<dependency>
    <groupId>io.github.lonelykkk</groupId>
    <artifactId>remote-client-sdk</artifactId>
    <version>0.0.2</version>
</dependency>
```
### 提供的第三方接口服务(持续更新中)
#### 1. openAI官方chatgpt-3.5增量模型接口调用
> 入门案例
``` java
public static void main(String[] args) {
        RemoteClient remoteClient = new RemoteClient();
        String chat = remoteClient.getAiChat("输入你的问题");  //以字符串类型，返回ai回答
        System.out.println(chat); //输出答案
    }
```
#### 2.快速生成二维码接口
> 入门案例
* 第一个参数msg为String类型，输入你要存入即将要生成的二维码里面的数据
* 第二行参数size为Integer输入你需要生成二维码的大小
* 生成后的二维码图片默认保存在你当前springboot项目中的resources/imgCode目录下
* 该方法的返回值为你生成的二维码的路径，以String类型返回
```java
public static void main(String[] args) {
        RemoteClient remoteClient = new RemoteClient();
        //1.第一个参数msg为String类型，输入你要存入即将要生成的二维码里面的数据，如我输入 https://www.baidu.com/,扫描生成后的二维码将跳转至百度链接
        //2.第二行参数size为Integer输入你需要生成二维码的大小,如输入150则二维码大小为 150*150px
        //3.生成后的二维码图片默认保存在你当前springboot项目中的resources/imgCode目录下
        //4.该方法的返回值为你生成的二维码的路径，以String类型返回
        String url = remoteClient.getImgCode("https://www.baidu.com/", 150);
        System.out.println(url);
    }
```
#### 3.英汉互译接口调用
> 入门案例
```java
 public static void main(String[] args) {
        RemoteClient remoteClient = new RemoteClient();
        String translation = remoteClient.getTranslation("输入你需要翻译的内容，中英文均可");
        System.out.println(translation);  //输出翻译后的结果,为String类型
    }
```
### 4.天气查询接口调用
> 接口描述
> 该api可用于查询当前地区24小时内的天气信息，只需要输入你需要查询的天气的地址，便可以返回对应地址的天气24小时的实体对象

> 入门案例
```java
public static void main(String[] args) {
        RemoteClient remoteClient = new RemoteClient();
        try {
            HourWeatherList hourWeatherList = remoteClient.getWeather("上海"); //在此处输入你的需要查询的天气的地点
            // 定义日期格式
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmm");
            for (HourForecast forecast : hourWeatherList.getHourList()) {
                // 解析字符串为 Date 对象
                Date date = dateFormat.parse(forecast.getTime());
                System.out.println("Time: " + date);
                System.out.println("Weather: " + forecast.getWeather());
                System.out.println("Temperature: " + forecast.getTemperature());
                System.out.println("Wind Direction: " + forecast.getWindDirection());
                System.out.println("Wind Power: " + forecast.getWindPower());
                System.out.println("---------------------------");
            }
        } catch (Exception e) {
            throw new RuntimeException();
        }
}
```
> 响应参数
```json
"showapi_res_body": {
		"ret_code": 0,
		"area": "上海",//查到的地区名
		"areaid": "",//查到的地区id
		"hourList": [//24小时预报列表
			{
				"weather_code": "01",//天气编码
				"time": "201611061000",//预报时间
				"wind_direction": "东风",//风向
				"wind_power": "4-5级 8.0~10.7m/s",//风力
				"weather": "多云",//天气名称
				"temperature": "21"//温度
			},
			{
				"weather_code": "01",//天气编码
				"time": "201611060900",//预报时间
				"wind_direction": "东风",//风向
				"wind_power": "3-4级 5.5~7.9m/s",//风力
				"weather": "多云",//天气名称
				"temperature": "19"//温度
			}
            ...24小时的天气一小时为一间隔
		]
	}
```
> 对应的实体类
```java
public class HourWeatherList {
    @JsonProperty("remark")
    private String remark;

    @JsonProperty("ret_code")
    private int retCode;

    @JsonProperty("areaid")
    private String areaId;

    @JsonProperty("area")
    private String area;

    @JsonProperty("areaCode")
    private String areaCode;

    private List<HourForecast> hourList;
}
```
```java
public class HourForecast {
    @JsonProperty("time")
    private String time;

    @JsonProperty("wind_direction")
    private String windDirection;

    @JsonProperty("wind_power")
    private String windPower;

    @JsonProperty("areaid")
    private String areaId;

    @JsonProperty("weather_code")
    private String weatherCode;

    @JsonProperty("temperature")
    private String temperature;

    @JsonProperty("area")
    private String area;

    @JsonProperty("weather")
    private String weather;

}
```

## 第三方接口将持续更新中

