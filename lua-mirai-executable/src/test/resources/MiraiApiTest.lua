print("hello")
print(Bot)
for i, v in pairs(Message) do
    print(tostring(i) .. "=" .. tostring(v))
end
print(Text)
local a = Text("xx")
print(type(a))
for i, v in pairs(a.__functions) do
    print(tostring(i) .. "=" .. tostring(v))
end