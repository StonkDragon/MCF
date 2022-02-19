# MCFunction Compiler

MCFunction Compiler (`mcf`) is a Java App that compiles `.mcf` Script files to `mcfunction`.
These `.mcf`-Files follow the same Syntax as standard MCFunction (`.mcfunction`), but can have things such as Custom Commands

# Features

- PreProcessor

MCF contains a PreProcessor.

More info: [https://en.wikipedia.org/wiki/Preprocessor]

- Syntax

PreProcessor-"Commands" are prefixed with `#!`.
To declare a new Label, use `#!define <label>`.

To check later, if that Label exists, use `#!ifdef <label>`.

To check, if a label is NOT set, use `#!ifndef <label>`.

You can also use `#!else`, which only triggers, if any proceeding IFs didn't trigger.

Finally, any IF-Statement ends with `#!endif`.

You can also unset a Label later using `#!unset <label>`

- Global Declarations

Global labels allow you to easily manage a huge amount of Commands across multiple files.
Global labels are defined as `#!globaldef <label>` and used the same way normal Labels are.

- Using Statement

The Using Statement allows you to easily include other Datapacks in one line.
Using is invoked with `#!using <pack>` where `<pack>` is the `Datapack Identifier`
More on that later

- Config Folder

The Config folder is located at `~/MCF` where `~` is your Users Home Directory
In it you can find 4 Files/Folders:

- settings.json

This File contains your MCF-Configuration
It contains default Values from the Compiler and general Datapack Configuration

In it you will also find 2 Lists:

These lists already have example Values to help you understand how they work

- Extensions

On Program Start, MCF will download all Extensions in this list.
I am going to talk about Extensions later.

`file` is the Extensions File Name

`url` is the URL from which the Extension can be downloaded.
This has to be the direct download Link!

- Repos

This List contains all Datapacks that you want to be able to access using `#!using`
These will also be downloaded on Start

`url` is the URL from which the Datapack can be downloaded.
This has to be the direct download Link!

`identifier` is the name of the MCF-Package.

Note:

How a Package is structured can be seen at [https://github.com/StonkDragon/MCF/tree/main/mcf.test.pkg]

# Extensions

Extensions are `.jar` Files that are run for every file before any preprocessing happens.
They are called with the following arguments:

- Source File
- Destination File

Extensions should make use of `mcf-api.jar` which contains some Functions for editing Files on-the-fly
When logging in an Extension, you should use the `Logger`-Class

An Example can be seen at [https://github.com/StonkDragon/MCF/tree/main/ExampleExtension]

# Local Config

You can also have a local File for your Datapack that can override some Settings and some more customizability.
This file is named `mcf.json`.

This is an Example:

```
{
    "source_dir": "src",
    "target_dir": "target",
    "obfuscate_scores": false,
    "obfuscate_functions": false,
    "ignored_scores": []
}
```

- source_dir

This is the folder that contains your datapack in `.mcf`-Format, meaning that all your `.mcf` Files are located in this Directory.
It follows the same file structure as standard Datapacks

- target_dir

This is the Folder that contains your compiled and (hopefully) working Datapack

Both source_dir and target_dir can be `.` as in "This Folder"

- obfuscate_scores and obfuscate_functions

These are pretty easy to understand so I won't go into them much.
They say, if Function names and scores should be obfuscated

- ignored_scores

This List contains all scores that should NOT be obfuscated. Scoreboards that are triggers are never obfuscated

# Running

Put the jar File in the root directory of your Datapack (The folder containing your src (and target) folders).

Then open a new Command Prompt or Terminal and enter following Command: `java -jar mcfunction-compiler.java <args>`
Replace <args> with one of the following:

`-c - Compile Datapack`

`-d - Create new .mcf Files from Datapack`

`-f <file> - Create new .mcf File from <file>`

Running without any arguments defaults to -c
