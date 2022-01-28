local connection = SqliteConnection("C:/Users/86182/Desktop/person.db")
local statement = connection:createStatement()
statement:setQueryTimeout(30)
statement:executeUpdate("drop table if exists person")
statement:executeUpdate("create table person (id integer, name string)")
statement:executeUpdate("insert into person values(1, 'leo')")
statement:executeUpdate("insert into person values(2, 'yui')")
local rs = statement:executeQuery("select * from person")
while rs:next() do
    print("name = " .. rs:getString("name"))
    print("id = " .. tostring(rs:getInt("id")))
end
""".trimIndent(), MiraiCoreGlobals()