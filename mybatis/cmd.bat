@echo off
set curdir=%~dp0
cd /d %curdir%
rd/s/q "..\bean\src\main\java\com\bl\nop\dao"
rd/s/q "..\bean\src\main\java\com\bl\nop\entity"
rd/s/q "..\bean\src\main\resources\com"

for %%i in (configfile\*.xml) do ( 
    echo %%i 
    java -jar mybatis-generator-core-1.4.0.jar -configfile %%i -overwrite
)

@pause