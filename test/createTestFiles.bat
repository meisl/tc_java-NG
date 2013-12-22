@echo off

del Zone.Identifier.tst 2>NUL
echo [ZoneTransfer]>>"Zone.Identifier.tst:Zone.Identifier"
echo ZoneId=3 >>"Zone.Identifier.tst:Zone.Identifier"

mkdir foldersCanHaveStreamsToo 2>NUL
echo bar >"foldersCanHaveStreamsToo:foo"

del weirdStreamNames.tst 2>NUL
echo >"weirdStreamNames.tst:$"
echo >"weirdStreamNames.tst:&"
echo >"weirdStreamNames.tst:#"
echo >"weirdStreamNames.tst:?"
echo >"weirdStreamNames.tst:|"
echo >"weirdStreamNames.tst:*"
echo >"weirdStreamNames.tst:>"
echo >"weirdStreamNames.tst:<"
echo \u0001 ^A >"weirdStreamNames.tst:(0x01|^A)"
echo \u0002 ^B >"weirdStreamNames.tst:(0x02|^B)"
echo \u0003 ^C >"weirdStreamNames.tst:(0x03|^C)"
echo \u0004 ^D >"weirdStreamNames.tst:(0x04|^D)"
echo \u0005 ^E >"weirdStreamNames.tst:(0x05|^E)"
echo \u0006 ^F >"weirdStreamNames.tst:(0x06|^F)"
echo \u0007 ^G >"weirdStreamNames.tst:(0x07|^G)"
REM \u0008 ^H BS
echo \u0009 ^I >"weirdStreamNames.tst:	(0x09)|^I"
REM \u000a ^J
REM \u000b ^K
REM \u000c ^L
REM \u000d ^M
echo \u000e ^N >"weirdStreamNames.tst:(0x0e|^N)"
echo \u000f ^O >"weirdStreamNames.tst:(0x0f|^O)"
echo \u0010 ^P >"weirdStreamNames.tst:(0x10|^P)"
echo \u0011 ^Q >"weirdStreamNames.tst:(0x11|^Q)"
echo \u0012 ^R >"weirdStreamNames.tst:(0x12|^R)"
echo \u0013 ^S >"weirdStreamNames.tst:(0x13|^S)"
echo \u0014 ^T >"weirdStreamNames.tst:(0x14|^T)"
echo \u0015 ^U >"weirdStreamNames.tst:(0x15|^U)"
echo \u0016 ^V >"weirdStreamNames.tst:(0x16|^V)"
echo \u0017 ^W >"weirdStreamNames.tst:(0x17|^W)"
echo \u0018 ^X >"weirdStreamNames.tst:(0x18|^X)"
echo \u0019 ^Y >"weirdStreamNames.tst:(0x19|^Y)"
REM \u001a ^Z EOF
echo \u001b >"weirdStreamNames.tst:(0x1b)"
echo \u001c >"weirdStreamNames.tst:(0x1c)"
echo \u001d >"weirdStreamNames.tst:(0x1d)"
echo \u001e >"weirdStreamNames.tst:(0x1e)"
echo \u001f >"weirdStreamNames.tst:(0x1f)"
REM \u0020 SPACE

:DONE
