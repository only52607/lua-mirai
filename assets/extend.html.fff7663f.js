import{_ as p,r as c,o as i,a as l,b as n,d as e,w as t,F as u,c as o,e as s}from"./app.6d305f2e.js";const r={},d=o(`<h1 id="\u6269\u5C55" tabindex="-1"><a class="header-anchor" href="#\u6269\u5C55" aria-hidden="true">#</a> \u6269\u5C55</h1><h2 id="\u542F\u52A8\u539F\u751F\u7EBF\u7A0B\u53CA\u5EF6\u8FDF" tabindex="-1"><a class="header-anchor" href="#\u542F\u52A8\u539F\u751F\u7EBF\u7A0B\u53CA\u5EF6\u8FDF" aria-hidden="true">#</a> \u542F\u52A8\u539F\u751F\u7EBF\u7A0B\u53CA\u5EF6\u8FDF</h2><p>\u793A\u4F8B:</p><div class="language-lua ext-lua line-numbers-mode"><pre class="language-lua"><code><span class="token function">thread</span><span class="token punctuation">(</span><span class="token keyword">function</span> <span class="token punctuation">(</span><span class="token punctuation">)</span>
    <span class="token function">sleep</span><span class="token punctuation">(</span><span class="token number">3000</span><span class="token punctuation">)</span>
    <span class="token function">print</span><span class="token punctuation">(</span><span class="token string">&quot;3\u79D2\u540E\u8F93\u51FA\u7ED3\u679C&quot;</span><span class="token punctuation">)</span>
<span class="token keyword">end</span><span class="token punctuation">)</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div>`,4),k=s("\u4E86\u89E3\u66F4\u591A\uFF1A"),v=s("lua\u6269\u5C55\u652F\u6301\u5E93"),h=o(`<h2 id="\u67E5\u770B\u5BF9\u8C61\u5305\u542B\u7684\u6240\u6709\u65B9\u6CD5\u540D\u4EE5\u53CA\u5C5E\u6027" tabindex="-1"><a class="header-anchor" href="#\u67E5\u770B\u5BF9\u8C61\u5305\u542B\u7684\u6240\u6709\u65B9\u6CD5\u540D\u4EE5\u53CA\u5C5E\u6027" aria-hidden="true">#</a> \u67E5\u770B\u5BF9\u8C61\u5305\u542B\u7684\u6240\u6709\u65B9\u6CD5\u540D\u4EE5\u53CA\u5C5E\u6027</h2><p>\u793A\u4F8B\uFF1A</p><div class="language-lua ext-lua line-numbers-mode"><pre class="language-lua"><code><span class="token keyword">for</span> k<span class="token punctuation">,</span>v <span class="token keyword">in</span> <span class="token function">pairs</span><span class="token punctuation">(</span>a<span class="token punctuation">.</span>__functions<span class="token punctuation">)</span> <span class="token keyword">do</span>
    <span class="token function">print</span><span class="token punctuation">(</span>v<span class="token punctuation">)</span>
<span class="token keyword">end</span>
<span class="token function">print</span><span class="token punctuation">(</span><span class="token punctuation">)</span>
<span class="token keyword">for</span> k<span class="token punctuation">,</span>v <span class="token keyword">in</span> <span class="token function">pairs</span><span class="token punctuation">(</span>a<span class="token punctuation">.</span>__properties<span class="token punctuation">)</span> <span class="token keyword">do</span>
    <span class="token function">print</span><span class="token punctuation">(</span>k <span class="token operator">..</span> <span class="token string">&quot;:&quot;</span> <span class="token operator">..</span> <span class="token function">tostring</span><span class="token punctuation">(</span>v<span class="token punctuation">)</span><span class="token punctuation">)</span>
<span class="token keyword">end</span>
<span class="token function">print</span><span class="token punctuation">(</span><span class="token punctuation">)</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div>`,3),_=s("\u4E86\u89E3\u66F4\u591A\uFF1A"),m=s("lua\u6269\u5C55\u652F\u6301\u5E93");function f(b,x){const a=c("RouterLink");return i(),l(u,null,[d,n("blockquote",null,[n("p",null,[k,e(a,{to:"/reference/libs/luaex.html"},{default:t(()=>[v]),_:1})])]),h,n("blockquote",null,[n("p",null,[_,e(a,{to:"/reference/libs/luaex.html"},{default:t(()=>[m]),_:1})])])],64)}var w=p(r,[["render",f],["__file","extend.html.vue"]]);export{w as default};
