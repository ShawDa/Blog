## Reference

[JAVA线上故障排查全套路](https://fredal.xin/java-error-check#toc_h1_13)

[线上linux系统故障排查之一：CPU使用率过高](https://www.jianshu.com/p/6d573e42310a)

[线上linux系统故障排查之二：内存占用过高](https://www.jianshu.com/p/43b2ecdfe005)



## Content

线上故障主要会包括CPU、磁盘、内存以及网络问题，而大多数故障可能会包含不止一个层面的问题，所以进行排查时候尽量四个方面依次排查一遍。同时例如jstack、jmap等工具也是不囿于一个方面的问题的，基本上出问题就是df、free、top 三连，然后依次jstack、jmap伺候，具体问题具体分析即可。