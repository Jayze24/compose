package compose.utils

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

/**
* made by Yu JaeHa
* ----------
* HOW TO USE
* ----------
*  example)
*   TextField(..., visualTransformation = VisualTransformationFormatString(digit = listOf(3, 4, 4), stringAppend = " - "), ..)
*
*  parameter)
*   digit : string format rules. 
*           For example, in "lisOf(3)", the specified character is entered for every 3rd character. like this "123 - 456 - 789 - 01"
*           if you set like this "listOf(2,1,5)", it will be like this "12 - 3 - 45678 - 90 - 1"
*   stringAppend : Characters to be inserted between strings.
*   isReversed : if you set "true", it will count from behind.
*   max : max return value.
**/

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

        return TransformedText(AnnotatedString(valueFormatted), creditCardOffsetTranslator)
    }
}
