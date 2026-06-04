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

## Java Interop

### Future Features
- [ ] Support for Comments
- [ ] Support for floating point numbers
- [ ] Standard library
- [ ] Use of precompile of files