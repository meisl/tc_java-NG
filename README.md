### tc_java: Total Commander Java Plugin Interface

##### Based on and continuing [Ken Händel](mailto:kschwiersch@yahoo.de)'s **tc_java** [on totalcmd.net](http://www.totalcmd.net/plugring/tc_java.html).

**tc_java** enables plugins for [Total Commander](http://ghisler.com) (TC) written in **Java**. All four types are supported: Lister (WLX), File System (WFX), Content plugins (WDX) and Packer (WCX) plugins.

#### Getting Started (for *users*)
* **First time only:**
    * Install the [latest Java runtime](http://www.java.com/en/download/manual.jsp) ("JRE", standard edition)
    * Download [javalib.tgz](http://www.totalcmd.net/download.php?id=tc_java) and extract into TC's installation directory (eg `C:\Progam Files\totalcmd\`) s.t. you have a sub-directory `javalib\` next to `plugins\` and `LANGUAGE\`.
* **Now install the plugins of your choice, like so:**
    * Download and **open in TC**, by double-clicking on it. This will auto-install it or auto-update an older version of it.
    * Close and restart TC (TODO: really necessary?)

#### Plugins to try
* [Those contained in here](https://github.com/meisl/tc_java-NG/blob/master/dist/README.md) (plz click on the links ***there*** for downloading, otherwise you might not get what you're expecting)
* Ken Händel's [Java Plugin Examples Page](http://java.totalcmd.net/V1.7/examples.html) on totalcmd.net
* http://crc83.blogspot.de/2013/10/gitdetails-plugin-for-total-commander.html
* ...

#### For developers
Currently (v1.7), the full TC v7 SDK API is supported, including GUI creation using Swing or SWT.
The interface, however, doesn't really "feel" much like Java. It's more like C, which is due to the fact that TC's API is exposed *as is* to the Java programmer, more or less.

The next version will build on top of this basic layer and **take full advantage of  Java's features**, such as: the type system (including generics, **but made easy for the developer**), `java.nio`, threading (again: made easy), and more.

#### Getting Started (for *developers*)
* Install the [latest JDK](http://www.oracle.com/technetwork/java/javase/downloads/index.html) (SE is enough)
* Git-clone this repo locally (recommended), or [download as .zip](https://github.com/meisl/tc_java-NG/archive/master.zip) and unpack; eg to `C:\tc_java\`
* Open a DOS-window there, ***from within TC*** (active panel/tab shows `C:\tc_java\`, then "Commands" | "Open command prompt window"). This makes sure that the env var `COMMANDER_PATH` is properly set for this DOS-window; you may verify this via `ECHO %COMMANDER_PATH%`.
* [???] Run `run-me-once.bat`, which will try to determine the JDK's and TC's installation directories and ask you in case. For example, you might have to tell it `%PROGRAMFILES%\Java\jdk1.7.0_51`, or, respectively, `%PROGRAMFILES%\totalcmd8.5b13`. It then puts `javalib` into TC's installation directory if necessary and prepares the env for development (TODO: what exactly it's doing)
* You have now a working dev-env. Create a new plugin with `newWDX.bat MyPlugin`, and edit `example-plugins\MyPlugin\src\MyPlugin.java`

#### Resources
* [API documentation](https://htmlpreview.github.com?https://github.com/meisl/tc_java-NG/master/doc/api/index.html) (javadoc)
* [Java Plugin Writer's Guide](http://java.totalcmd.net/V1.7/PluginWritersGuide.txt)
* In the TC forum: ["Let's make writing Java plugins fun!"](http://ghisler.ch/board/viewtopic.php?t=39016) (**users** are heartily invited, too)
* [Apache's chainsaw](http://logging.apache.org/chainsaw/download.html) (for debugging)
* [Java 7 API docs](http://docs.oracle.com/javase/7/docs/api/)
