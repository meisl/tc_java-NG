API docs: https://htmlpreview.github.com/meisl/tc_java-NG/master/doc/api/index.html


TODO (a lot...!)

...for now let me just point you to this thread on the TotalCommander forum:
* [Let's make writing Java plugins fun!](http://ghisler.ch/board/viewtopic.php?t=39016)

and then some links you will be going to over and over:
* [Ken Händel's "PluginWritersGuide.txt"](http://java.totalcmd.net/V1.7/PluginWritersGuide.txt)
* [Ken Händel's "javadoc for tc_plugin"](http://java.totalcmd.net/V1.7/javadoc/index.html)
* [Java 7 API docs](http://docs.oracle.com/javase/7/docs/api/)
* [Apache's chainsaw](http://logging.apache.org/chainsaw/download.html) (for debugging)

NtfsStreamsJ itself relies on these:
* [Ken Händel's tc_java](http://www.totalcmd.net/plugring/tc_java.html) of course
* [Mark Russinovich's streams.exe](http://technet.microsoft.com/de-de/sysinternals/bb897440), v1.56
* [Frank Heyne's LADS.exe](http://www.heysoft.de/en/software/lads.php?lang=EN), v4.10
* since Windows Vista: the built-in `dir /r` [TODO]

If you're below Windows Vista or just feel like trying 'em out you need to download and unpack
`streams` and/or `lads` into `vendor\streams\` and/or `vendor\lads\` under NtfsStreamsj's plugin folder
(probably `"C:\Program Files\totalcmd\plugins\wdx\NtfsStreamsJ"`).

```
SET JAVA_HOME=%PROGRAMFILES%\Java\jdk1.7.0_25
SET COMMANDER_PATH=%PROGRAMFILES%\totalcmd
```
