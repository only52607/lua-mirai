# 集合支持库
集合支持库提供一系列方法，用于将Java/Kotlin中的集合类型（Map/Set/List/Array）与Lua原生table值互相转换。

## Lua -> Kotlin

### tomap(table)

### tolist(table)

### toset(table)

### toarray(table)

### tobytearray(value)
生成一个Java Byte数组（Byte[] / ByteArray），value可为string或包含number的table

## Kotlin -> Lua

### totable(value)
将kotlin中的集合类型（Map/Set/List/Array）转换为lua table