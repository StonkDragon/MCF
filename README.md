# MCFunction Preprocessor
# What is MCFunction Preprocessor

MCFunction Preprocessor (or MCPP short) provides a way of quickly and easily changing functions using if-Statements.
If you know how to use C-Style Preprocessor Directives, you know how to use MCPP.

# How to use
- Files
Firstly, MCPP uses a different File Extention from standard MCFunction to avoid confusion:
```.mcf```
These follow the same Syntax as standard MCFunction (`.mcfunction`)

- Syntax
MCPP-"Commands" are prefixed with ```#!```.
To declare a new Label, use ```#!DEFINE <label>```.

To check later, if that Label exists, use ```#!IFDEF <label>```.

To check, if a label is NOT set, use ```#!IFNDEF <label>```.

You can also use `#!ELSE`, which only triggers, if any proceeding IFs didn't trigger.

Finally, any IF-Statement ends with `#!ENDIF`.

You can also unset a Label later using `#!UNSET <label>`

- Global Declarations
Global labels allow you to easily manage a huge amount of Commands across multiple files.
Global labels are defined as `#!GLOBALDEF <label>` and used the same way normal Labels are.

