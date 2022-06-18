package com.github.only52607.luamirai.js.utils

//interface will be converted to js lambda "() => { }" by Mozilla Rhino automatically.
class KtLambdaInterfaceBridge {
    interface NoArgument<RT> {
        fun call(): RT
    }

    interface SingleArgument<AT, RT> {
        fun call(arg: AT): RT
    }

    interface DoubleArgument<A1T, A2T, RT> {
        fun call(arg1: A1T, arg2: A2T): RT
    }

    interface TripleArgument<A1T, A2T, A3T, RT> {
        fun call(arg1: A1T, arg2: A2T, arg3: A3T): RT
    }
}