# 12306-java
12306java版本刷票购票
springboot项目，利用selenium和云打码平台进行自动刷票购票

项目中只带了selenium操作google浏览器的驱动，支持71.0.3578.98版本的google浏览器，其他浏览器或者不支持的版本的google浏览器需要下载相应的selenuim驱动
https://www.cnblogs.com/nancyzhu/p/8589764.html

需要注册云打码帮忙识别12306的验证码，项目中带了java使用云打码的64位和32位的dll文件，注册云打码地址：http://www.yundama.com/about.html，

项目中需要自己手动代码写入12306网址的始发站，终点站及发车日期的参数，可以先在12306网站上选择乘车区间及日期，跳转页面后将各个参数填写到对应的代码中

需要将发车时间的车次写入到代码中

需要将12306账号密码及云打码的账号密码的一些信息写入到AppConstant中，需要为购票页面的第几位乘客买票，就修改normalPassenger后面的数字，从0开始
