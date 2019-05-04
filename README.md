# wechat_generator_client
通过简单的设置和获取班级成员信息批量生成微信朋友圈截图

* 注：此项目已经排除包含敏感信息的cn.compscosys.config.DatabaseConfigureReader类，该类使用方法getConfigure()来获取数据库配置信息。
有关此函数的说明：
函数原型：public static final String[] getConfigure()
返回值：String[] {"Mysql URL", "USERNAME", "PASSWD"}
如：返回值为String[] {"jdbc:mysql://ip:port/databaseName?useSSL=true&useUnicode=true&serverTimezone=GMT", "root", "root"}
