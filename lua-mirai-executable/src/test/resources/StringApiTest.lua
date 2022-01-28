local a = "你好hello"
print(a:sub(0,3)) -- 你
print(a:substring(0,2)) --你好
b = a:encode("UTF-8","GBK")
c = b:encode("GBK","UTF-8")
print(b) -- 浣犲ソhello
print(c) -- 你好hello
print(a:length()) -- 7
print(a:len()) -- 11
d = a:encodeURL("UTF-8")
e = d:decodeURL("UTF-8")
print(d) -- %E4%BD%A0%E5%A5%BDhello
print(e) -- 你好hello