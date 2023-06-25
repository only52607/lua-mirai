package com.github.only52607.luamirai.configuration

import com.github.only52607.luamirai.core.ScriptSource
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.*
import java.io.File
import java.net.URL

/**
 * ClassName: ConfigurableScriptSourceSerializer
 * Description:
 * date: 2022/5/21 10:47
 * @author ooooonly
 * @version
 */
object ConfigurableScriptSourceSerializer : KSerializer<ConfigurableScriptSource> {
    private const val TYPE_FILE = "file"
    private const val TYPE_CONTENT = "content"
    private const val TYPE_URL = "url"
    private const val TYPE_UNKNOWN = "unknown"

    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor("ConfigurableScriptSource") {
            element<String>("type")
            element<String>("value")
            element<String>("alias")
            element<Boolean>("autoStart")
            element<String>("lang")
        }

    override fun deserialize(decoder: Decoder): ConfigurableScriptSource {
        decoder.decodeStructure(descriptor) {
            var type = "unknown"
            var value = ""
            var alias = ""
            var autoStart = false
            var lang = "lua"
            while (true) {
                when (val index = decodeElementIndex(descriptor)) {
                    0 -> type = decodeStringElement(descriptor, 0)
                    1 -> value = decodeStringElement(descriptor, 1)
                    2 -> alias = decodeStringElement(descriptor, 2)
                    3 -> autoStart = decodeBooleanElement(descriptor, 3)
                    4 -> lang = decodeStringElement(descriptor, 4)
                    CompositeDecoder.DECODE_DONE -> break
                    else -> error("Unexpected index: $index")
                }
            }
            val source = when (type) {
                TYPE_FILE -> ScriptSource.FileSource(File(value), lang)
                TYPE_CONTENT -> ScriptSource.StringSource(value, lang)
                TYPE_URL -> ScriptSource.URLSource(URL(value), lang)
                else -> throw IllegalArgumentException("Unsupported script type $type")
            }
            return ConfigurableScriptSource(source, alias, autoStart)
        }
    }

    override fun serialize(encoder: Encoder, value: ConfigurableScriptSource) {
        return encoder.encodeStructure(descriptor) {
            when (value.source) {
                is ScriptSource.FileSource -> {
                    encodeStringElement(descriptor, 0, TYPE_FILE)
                    encodeStringElement(descriptor, 1, value.source.file.path)
                }
                is ScriptSource.StringSource -> {
                    encodeStringElement(descriptor, 0, TYPE_CONTENT)
                    encodeStringElement(descriptor, 1, value.source.content)
                }
                is ScriptSource.URLSource -> {
                    encodeStringElement(descriptor, 0, TYPE_URL)
                    encodeStringElement(descriptor, 1, value.source.url.toString())
                }
                else -> {
                    encodeStringElement(descriptor, 0, TYPE_UNKNOWN)
                    encodeStringElement(descriptor, 1, "")
                }
            }
            encodeStringElement(descriptor, 2, value.alias ?: "")
            encodeBooleanElement(descriptor, 3, value.autoStart)
            encodeStringElement(descriptor, 4, value.source.lang)
        }
    }

}