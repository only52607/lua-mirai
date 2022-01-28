local html = [[
    <html>
        <head>
            <title>First parse</title>
        </head>
        <body>
            <p id = "content" >Parsed HTML into a doc.</p>
        </body>
    </html>
]]
local doc = Jsoup.parse(html)
local ele = doc:getElementById("content")
print(ele:text())