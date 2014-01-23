### tc_java: Total Commander Java Plugin Interface

##### Based on and continuing [Ken Händel](mailto:kschwiersch@yahoo.de)'s **tc_java** [on totalcmd.net](http://www.totalcmd.net/plugring/tc_java.html).

**tc_java** enables plugins for [Total Commander](http://ghisler.com) (TC) written in **Java**. All four types are supported: Lister (WLX), File System (WFX), Content plugins (WDX) and Packer (WCX) plugins.

#### Getting Started (for *users*)
* Install the [latest Java runtime](http://www.java.com/en/download/manual.jsp) ("JRE", standard edition)
* Download [javalib.tgz](http://www.totalcmd.net/download.php?id=tc_java) and extract into TC's installation directory (eg `C:\Progam Files\totalcmd\`) s.t. you have a sub-directory `javalib\` next to `plugins\` and `LANGUAGE\`. Now you're prepared to install the Java plugins you like:
* Download the Java plugin of your choice (see below)
* Safe and **open it in TC**, by double-clicking on it. This will auto-install it or auto-update an older version of it.
* Close and restart TC (TODO: really necessary?)

#### Plugins to try
* [list of ready-for-download-and-install plugins](https://github.com/meisl/tc_java-NG/blob/master/README.md) contained in this repo (plz use this if you don't know github - you might not get what you're expecting otherwise)
* [Java Plugin Examples Page](http://java.totalcmd.net/V1.7/examples.html) on totalcmd.net
* http://crc83.blogspot.de/2013/10/gitdetails-plugin-for-total-commander.html
* ...

#### For developers
The current status (v1.7) is full support of the TC v7 SDK API, including support for GUI creation using Swing or SWT. The interface, however, doesn't really "feel" much like Java. It's more like C, which is due to the fact that TC's API is exposed to the Java programmer more or less as is.

The next version will build on top of this basic layer and **take full advantage of  Java's features**, such as: the type system (including generics, **but made easy for the developer**), `java.nio`, threading (again: made easy), and more.

#### Resources
* [API documentation](https://htmlpreview.github.com?https://github.com/meisl/tc_java-NG/master/doc/api/index.html) (javadoc)
* [Java Plugin Writer's Guide](http://java.totalcmd.net/V1.7/PluginWritersGuide.txt)
* In the TC forum: ["Let's make writing Java plugins fun!"](http://ghisler.ch/board/viewtopic.php?t=39016) (**users** are heartily invited, too)
* [Apache's chainsaw](http://logging.apache.org/chainsaw/download.html) (for debugging)
* [Java 7 API docs](http://docs.oracle.com/javase/7/docs/api/)

#### Getting Started (for *developers*)
* Install the [latest JDK](http://www.oracle.com/technetwork/java/javase/downloads/index.html) (SE is enough)
* Git-clone this repo locally (recommended), or [download as .zip](https://github.com/meisl/tc_java-NG/archive/master.zip)
* Run `run-me-once.bat` in the console. It'll try to find the JDK and TC's installation directories and ask you in case. For example, you might have to tell it `%PROGRAMFILES%\Java\jdk1.7.0_51`, or, respectively, `%PROGRAMFILES%\totalcmd8.5b13`. It then puts `javalib` into TC's installation directory if necessary and prepare the env for development (TODO: what exactly it's doing)
* You have now a working dev-env. Create a new plugin with `newWDX.bat MyPlugin`, and edit `example-plugins\MyPlugin\src\MyPlugin.java`
