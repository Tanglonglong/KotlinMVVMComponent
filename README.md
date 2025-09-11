# KotlinMVVMComponent-
Android-kotlin-mvvm-component 


<img width="831" height="461" alt="Android组件化mvvm架构" src="https://github.com/user-attachments/assets/9861eca0-958b-46e8-a0f5-c7bf94d38ae2" />


1.module模块都可以独立运行调试  通过gradle.properties里面配置 isModuleLibrary=false
2.library基础库不能独立运行


项目目录

<img width="277" height="464" alt="8473f437-c42f-416e-9a57-98ff5293b818" src="https://github.com/user-attachments/assets/15fe5363-a828-4a6c-b447-a069465d2a79" />              <img width="238" height="474" alt="1b54eff6-c1cf-4550-b4ee-093f6ef66220" src="https://github.com/user-attachments/assets/7b1b812a-f110-41f2-80c2-9373ba1be66e" />



gradle 文件说明

config.gradle  里面配置一些公共依赖库版本

library.build.gradle  配置基础库的公共依赖

module.build.gradle  配置可以独立运行调试的依赖库

这么配置可以从配置上解决一些不必要的问题比如：循环依赖问题、版本冲突等，每个人可以安心负责自己的业务模块


<img width="270" height="392" alt="146c3edb-1a86-4ffe-a9be-1b295507a927" src="https://github.com/user-attachments/assets/a60803fd-9b19-44b7-aa13-2044cf4078e0" />










阶段一效果展示：





https://github.com/user-attachments/assets/831b406f-cd4c-47d5-963b-17bf547f5738




代码说明：





      /**
          * IO中处理请求
          * 普通协程请求
     **/

     
    suspend fun <T> requestResponse(requestCall: suspend () -> BaseResponse<T>?): T? {
        val response = withContext(Dispatchers.IO) {
            withTimeout(mTimeOut) {
                //这边refit okhttp可能抛出http异常
                requestCall()
            }
        } ?: return null

        //已经能拿到数据了，业务层异常抛出ApiException
        if (response.isFailed()) {
            throw ApiException(response.errorCode, response.errorMsg)
        }
        return response.data
    }

    /**
     * IO中处理请求
     * 配合LiveData{}
     */
    suspend fun <T> requestResponseWithRequestResult(requestCall: suspend () -> BaseResponse<T>?): RequestResult<T?> {
        try {
            // 1. 执行网络请求
            val response = withContext(Dispatchers.IO) {
                withTimeout(mTimeOut) {
                    requestCall()
                }
            }
            response?.let {
                //业务逻辑异常处理
                if (response.isFailed()) {
                    val exception = ApiException(response.errorCode, response.errorMsg)
                    return RequestResult.Error(exception)
                } else {
                    //成功返回数据
                    return RequestResult.Success(response.data)
                }
            }
        } catch (e: Exception) {
            //http异常处理
            val exception = ExceptionHandler.handleException(e)
            LogUtil.v("exception.errCode:" + exception.errCode + " exception.errMsg:" + exception.errMsg)
           return RequestResult.Error(e)
        }
        return RequestResult.Error(null)
    }



    

    /**
     * IO中处理请求
     * 使用Flow流
     */
    suspend fun <T> requestFlowResponse(requestCall: suspend () -> BaseResponse<T>?): Flow<RequestResult<T?>> {
        //1.执行请求
        val flow = flow {
            //设置超时时间
            val response = withTimeout(mTimeOut) {
                requestCall()
            }
            response?.let {
                //业务逻辑异常处理
                if (response.isFailed()) {
                    val exception = ApiException(response.errorCode, response.errorMsg)
                    emit (RequestResult.Error(exception))
                } else {
                    //成功返回数据
                    emit (RequestResult.Success(response.data))
                }
            }
        //3.指定运行的线程，flow {}执行的线程
        }.flowOn(Dispatchers.IO)
            .onStart {
                //4.请求开始，展示加载框
                emit(RequestResult.Loading)
            }
            //5.捕获异常
            .catch { e ->
                LogUtil.e(e)
                val exception = ExceptionHandler.handleException(e)
                emit(RequestResult.Error(exception))
            }
            //6.请求完成，包括成功和失败
            .onCompletion {
                emit(RequestResult.Completion)
            }
        return flow
    }

    

     /**
     * 普通协程
     * 首页banner
     * **/
    fun getBannerList() {
        viewModelScope.launch {
            try {
                _bannerList.value = homeRepository.getHomeBanner()
            } catch (e: Exception) {
                // 处理错误
                _bannerList.value = null
                //http异常转换Api异常，统一api异常处理逻辑
                val exception = ExceptionHandler.handleException(e)
                LogUtil.v("exception.errCode:" + exception.errCode + " exception.errMsg:" + exception.errMsg)
            }
        }
    }


    viewMode里面的业务接口

    

    /**
     * 首页banner
     * 使用liveData{}自带扩展协程封装请求，并将返回值封装到一个密封类里，方便业务层处理
     * **/

    fun getBannerListLiveData(): LiveData<RequestResult<MutableList<Banner>?>> {
        return liveData {
            emit(RequestResult.Loading)
            emit(homeRepository.getHomeBannerResult())
        }
    }

    /**
     * 首页banner
     * 使用Flow封装请求，并将返回值封装到一个密封类里，方便业务层处理
     *
     *
     * **/

    suspend fun getBannerWithFlow(): Flow<RequestResult<MutableList<Banner>?>> {
        return homeRepository.getHomeBannerResultFlow()
    }


activity/fragment 里面调用




        //普通协程搭配LiveData变化监听数据变化来处理接口返回数据
        mViewModel.bannerList.observe(viewLifecycleOwner) { banners ->
            banners?.let {
                mBinding.bannerHome.visibility = View.VISIBLE
                mBinding.bannerHome.setPages(CBViewHolderCreator {
                    BannerHolder()
                }, banners).apply {
                    setPointViewVisible(true)
                    startTurning(5000)
                }
            } ?: kotlin.run {
                mBinding.bannerHome.visibility = View.GONE
            }
        }

        //LiveData{} 协程这个无需在viewModel里面定义livedata，直接方法返回的就是，并且对于成功失败有封装
        mViewModel.getBannerListLiveData().observe(viewLifecycleOwner) { result ->
            bannerDataProcess(result)
        }

        //Flow请求
        lifecycleScope.launch {
            mViewModel.getBannerWithFlow().collect {
                    result ->
                bannerDataProcess(result)
            }
        }

    










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

