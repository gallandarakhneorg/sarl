# SARL Command-line Compiler - sarlc

[:Outline:]

[:Fact:]{(io.sarl.lang.sarlc.Main)::getDefaultCompilerProgramName == '[:name](sarlc)'}

A command-line compiler is a tool that could be invoked from the command-line shell in order to compiler a language source file.
[:name:] is the command-line compiler for the SARL language.

## Note on the application classpath

The [:name!] tool does not deal with the run-time classpath of the application.
It means that it does not check if a SARL runtime environment is installed and used in your application.

For launching a SARL application, please refer to one of:

* [Running an agent from the command-line shell](../getstarted/RunSARLAgentCLI.md)
* [Running an agent inside SARL Eclipse environment](../getstarted/RunSARLAgentEclipse.md)
* [Running an agent from a Java program progammatically](../getstarted/RunSARLAgentJava.md)

## Usage

The [:name:] tool takes arguments:


```text
[:name!] [OPTIONS] <[:srcfolder](source folder)>...
```


The [:name:] tool takes at least one [:srcfolder:] from which the SARL files are read.

You could change the behavior of the [:name:] compiler with the command-line options.
For obtaining a list of the options, type:

```text
[:name!] -h
```


## Command-Line Options

The complete list of the options is:



| Option | Description |
| ------ | ----------- |
[:Dynamic:]{
	runShellSilently(whichShellCommand("sarlc"), "generatemarkdownhelp".makeCliOption)
}


[:Include:](../includes/legal.inc)

