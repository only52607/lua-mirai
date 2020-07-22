# 在Lua中与Java交互

## 在lua中调用java类

### 使用import命令导入java类

#### 示例：

``` lua
    import "java.lang.Thread" 
```

### 使用java类静态成员和静态方法

``` lua
    类名.静态成员名或方法名(参数)
```

#### 示例

``` lua
    import "java.lang.String"
    print(String.join("-","lua","mirai"))
```

### 创建java类对象，并调用方法

``` lua
    对象=类名(构造参数)
    对象:方法名(参数)
```

#### 示例：

``` lua
    import "java.lang.String"
    instance = String("lua-mirai")
    print(instance:toUpperCase())
```

### 实现java类方法，并生成对象

``` lua
对象 = 类名{
    方法名=function (参数)
    end
}
```

#### 示例：

``` lua
--实现Runnable接口并创建线程
import "java.lang.Thread"
import "java.lang.Runnable"
Thread(
    Runnable{
        run = function() print "Hello World!" end
    }
):start()
```

参考： [`CSDN:在Lua中操作Java对象`](https://blog.csdn.net/lgj123xj/article/details/81677036)