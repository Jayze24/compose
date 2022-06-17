package kr.co.itcha.linker.utils

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import kr.co.itcha.linker.module.log.Log

class VisualTransformationFormatString(
    private val digit: List<Int> = listOf(4),
    private val stringAppend: String = " - ",
    private val isReversed: Boolean = false,
    private val max: Int = 25
) : VisualTransformation {

    private val creditCardOffsetTranslator = object : OffsetMapping {
        override fun originalToTransformed(offset: Int): Int {
            var searching = true
            var padding = 0
            var sumOfDigit = 0
            while (searching) {
                for (it in digit) {
                    sumOfDigit += it
                    if (offset <= sumOfDigit || sumOfDigit >= max) {
                        searching = false
                        break
                    } else {
                        padding += stringAppend.length
                    }
                }
            }
            val result = if (offset >= max) {
                max + padding
            } else {
                offset + padding
            }
            return result
        }

        override fun transformedToOriginal(offset: Int): Int {
            var searching = true
            var result = 0
            var sumOfDigit = 0
            while (searching) {
                digit.forEach {
                    sumOfDigit += it + stringAppend.length
                    if (offset < sumOfDigit) {
                        searching = false
                    } else {
                        result += stringAppend.length
                    }
                }
            }
            return offset - result
        }
    }

    override fun filter(text: AnnotatedString): TransformedText {
        var value = if (text.text.length > max) text.text.substring(0 until max) else text.text
        Log.send(value)
        val out = StringBuilder()
        var indexDigit = 0
        var count = 0
        if (isReversed) {
            value = value.reversed()
        }
        value.forEach { c ->
            if (count == digit[indexDigit]) {
                out.append(stringAppend)
                indexDigit = if (digit.size - 1 > indexDigit) {
                    indexDigit + 1
                } else {
                    0
                }
                count = 0
            }
            count++
            out.append(c)
        }
        val valueFormatted = if (isReversed) {
            out.toString().reversed()
        } else {
            out.toString()
        }
        Log.send("out : $valueFormatted")

        return TransformedText(AnnotatedString(valueFormatted), creditCardOffsetTranslator)
    }
}