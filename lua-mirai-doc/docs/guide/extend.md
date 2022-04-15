# 扩展

## 启动原生线程及延迟

示例:
```lua
thread(function ()
    sleep(3000)
    print("3秒后输出结果")
end)
```

> 了解更多：[lua扩展支持库](../reference/libs/luaex.md)

## 查看对象包含的所有方法名以及属性
示例：

``` lua
for k,v in pairs(a.__functions) do
    print(v)
end
print()
for k,v in pairs(a.__properties) do
    print(k .. ":" .. tostring(v))
end
print()
```

> 了解更多：[lua扩展支持库](../reference/libs/luaex.md)