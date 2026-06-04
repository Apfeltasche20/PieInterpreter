# Pie-Lang

A lightweight, simple, customizable and secure Language written and interpreted in Java.

## Features:
- simple python and javascript inspired syntax
- simple integration into java projects
- secure and customizable due to explicitly exposed java functionality

## Usage:

### Usage as Standalone:
    java -jar Interpreter2000.jar <MainFile> [-I <includeDir>]

### Usage in Java Project:

```
import main.interpreter.Interpreter;

List<String> includeDirectories = new ArrayList<>();
Interpreter interpreter = new Interpreter(includeDirectories);

interpreter.executeFile("File Path");
interpreter.execute("Source Code");
```

## Syntax:

### General:
- Pie-Lang is a dynamically-typed language

### Functions:
- Functions do not declare a return type
- Functions do always return a value
- If a function return without an explicit value, it returns null

```
function example_function()
{
   return; // returns null
}

function example_function_with_arguments(arg1, arg2, arg3)
{
    return 5; // returns 5
}
```

### Variables
- Variables are declared with the var keyword
```
var my_variable = 5;
```

### Control Flow
```
if(true)
{

}
else
{

}

for(int i = 0;i<10;i = i + 1)
{

}

while(true)
{

}
```
### Imports

```
import system
```

### Classes
```
class myClass()
{
    var myClassVariable = 5;
    
    // Constructor is a function with the same name as the class
    function myClass()
    {
        
    }
    
    function myClassFunction()
    {
    
    }
}

var myObject = new myClass();
myObject.myClassFunction();
```

### Arrays
- Arrays can store values of multiple types
- Arrays are bound checked on setting or getting a value
```
var myArray = [4];
myArray.set(0, 5); // setting the 0-index to 5
var value = myArray.get(0); // get the 0-index from the array
```

## Java Interop
### Registering a Function
On the java side:

```java

import main.interpreter.Interpreter;
import main.interpreter.variable.Variable;
import main.interpreter.variable.VariableString;
import main.interpreter.variable.VariableType;

import java.util.List;

/**
 *
 * @param interpreter the interpreter instance that runs the code
 * @param args the list of variables given to the function
 * @return return value of the function
 */
public static Variable myFunction(Interpreter interpreter, List<Variable> args)
{
    Variable firstArgument = args.getFirst();
    // getting type of the variable
    VariableType variableType = firstArgument.getCurrentType();
    // get variable value as a string
    String firstArgumentValue = firstArgument.asString();

    // returns null
    return new Variable();
    // returns "Hello World"
    return new VariableString("Hello World");
}

public static void main(String[] args)
{
    Interpreter interpreter = new Interpreter();
    interpreter.addInternFunction("myFunction", (interpreter1, args1) -> myFunction(interpreter, args));
}

```
On the Pie-Lang Side:
```
function myFunction(arg1)
{
    // Calls the function registers as myFunction on the Java Side
    var returnValue = intern myFunction(arg1);
}
```

### Registering a Java Class
On the java side:

```java
import main.interpreter.intern.classes.ExposedFunction;

/*
 * To Use a Java Class in Pie-Lang it needs to be Annotated with the ExposedClass Annotation
 * arrays can not be used as arguments or return values currently
 * 
 * internModule is the file name where the class is defined in Pie-Lang
 * internName is the name of the class in Pie-Lang
 */
@ExposedClass(internModule = "string", internName = "CustomString")
public class CustomString
{
    private String value;
    
    // Constructors don't need the @ExposedFunction Annotation
    public CustomString(String value)
    {
        this.value = value;
    }

    // @ExposedFunction exposed the Function to Pie-Lang
    // Functions without this Annotation can not be called from Pie-Lang
    @ExposedFunction
    public void append(String value)
    {
        this.value += value;
    }
    
    // Setting the name attribute changes the name of the function in Pie-Lang
    @ExposedFunction(name = "toString")
    public String asString()
    {
        return value;
    }
}
```

On the Pie-Lang side:
- return types or arguments are not defined for the intern_classes
- When calling the functions the runtime checks if the given arguments match the real function arguments

```
string.txt
------------------------------------------------
intern_class CustomString
{
    intern_function CustomString(); 
    intern_function append();
    intern_function toString();
}
```


```
main.txt
------------------------------------------------
var myString = new CustomString("Hello ");
myString.append("World!");
var result = myString.toString();
```

### Future Features
- [ ] Support for Comments
- [ ] Support for floating point numbers
- [ ] Standard library
- [ ] Use of precompile of files