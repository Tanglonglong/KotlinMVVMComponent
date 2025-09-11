# KotlinMVVMComponent-
Android-kotlin-mvvm-component 


<img width="831" height="461" alt="Android组件化mvvm架构" src="https://github.com/user-attachments/assets/9861eca0-958b-46e8-a0f5-c7bf94d38ae2" />


1.module模块都可以独立运行调试  通过gradle.properties里面配置 isModuleLibrary=false
2.library基础库不能独立运行

<img width="277" height="464" alt="8473f437-c42f-416e-9a57-98ff5293b818" src="https://github.com/user-attachments/assets/15fe5363-a828-4a6c-b447-a069465d2a79" />              <img width="238" height="474" alt="1b54eff6-c1cf-4550-b4ee-093f6ef66220" src="https://github.com/user-attachments/assets/7b1b812a-f110-41f2-80c2-9373ba1be66e" />





知识点总结：


协程：就是对之前的线程的一个封装的api，suspend 挂起关键字主要是声明这个函数可以运行在线程并被挂起，只是声明给调用者的信息；
可以切换线程作用域，主线程、IO线程，cpu密集线程等


常用协程作用域：


viewModelScope：与 ViewModel 生命周期绑定，自动取消

lifecycleScope：与 Activity/Fragment 生命周期绑定

GlobalScope：全局作用域，需谨慎使用，避免内存泄漏

挂起： 就是指在协程作用域里面，被suspend声明的函数，执行耗时操作时，下面的代码就被挂起了，等耗时操作完成再切回来，
     这样就实现了异步操作，在代码里可以同步的写法，代码阅读流畅

然后就是挂起非阻塞：协程是运行在线程里面的，这边的非阻塞就是不阻塞运行它的线程

kotlin 里面object关键字声明的就是饿汉式单列，在使用时就加载一次，线程安全
如果需要懒汉式单列 就需要用伴生对象companion object 结合 by lazy 并指定线程安全模式（如 LazyThreadSafetyMode.SYNCHRONIZED

依赖注入：在依赖的地方不要实现，编译时动态加载，可以达到数据共享和解耦
比如：在组件化中，a组件想使用b组件的功能，但是双方又不能相互依赖，这时候就可以在a、和b共同依赖的c模块里定义接口对外提供能力
然后在b里面去实现这些能力，通过依赖注入将实现注入到c，这样a拿到的接口实现能力就是b的；
ARout里面有依赖注入，kotlin里面有Hilt依赖注入都可以



问题记录：


组件化ARouter配置


java：


api 'com.alibaba:arouter-api:1.5.2'
annotationProcessor 'com.alibaba:arouter-compiler:1.5.2'



kotlin：


api 'com.alibaba:arouter-api:1.5.2'
kapt 'com.alibaba:arouter-compiler:1.5.2'  
// java和kotlin不一样，关键修改点java用的 annotationProcessor 'com.alibaba:arouter-compiler:1.5.2'
//kotlin还要apply plugin: 'kotlin-kapt'引入插件，直接用java的就一直生成不了路由，动态注入也有问题

