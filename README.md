# compose
code for compose



## VisualTransformationFormatter
 HOW TO USE

  example)
  
   TextField(..., visualTransformation = VisualTransformationFormatString(digit = listOf(3, 4, 4), stringAppend = " - "), ..)

  parameter)
  
   digit : string format rules. 
           For example, in "lisOf(3)", the specified character is entered for every 3rd character. like this "123 - 456 - 789 - 01"
           if you set like this "listOf(2,1,5)", it will be like this "12 - 3 - 45678 - 90 - 1"
           
   stringAppend : Characters to be inserted between strings.
   
   isReversed : if you set "true", it will count from behind.
   
   max : max return value.
