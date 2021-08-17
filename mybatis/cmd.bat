@echo off
rd/s/q "G:\ServProject\trunk\code_2020\java\blaz-nop\bean\src\main\java\com\bl\nop\dao"
rd/s/q "G:\ServProject\trunk\code_2020\java\blaz-nop\bean\src\main\java\com\bl\nop\entity"
rd/s/q "G:\ServProject\trunk\code_2020\java\blaz-nop\bean\src\main\resources\com"

java -jar G:\ServProject\trunk\code_2020\java\blaz-nop\mybatis\mybatis-generator-core-1.4.0.jar -configfile G:\ServProject\trunk\code_2020\java\blaz-nop\mybatis\generator-system.xml -overwrite
java -jar G:\ServProject\trunk\code_2020\java\blaz-nop\mybatis\mybatis-generator-core-1.4.0.jar -configfile G:\ServProject\trunk\code_2020\java\blaz-nop\mybatis\generator-device.xml -overwrite
java -jar G:\ServProject\trunk\code_2020\java\blaz-nop\mybatis\mybatis-generator-core-1.4.0.jar -configfile G:\ServProject\trunk\code_2020\java\blaz-nop\mybatis\generator-resources.xml -overwrite
java -jar G:\ServProject\trunk\code_2020\java\blaz-nop\mybatis\mybatis-generator-core-1.4.0.jar -configfile G:\ServProject\trunk\code_2020\java\blaz-nop\mybatis\generator-game.xml -overwrite
java -jar G:\ServProject\trunk\code_2020\java\blaz-nop\mybatis\mybatis-generator-core-1.4.0.jar -configfile G:\ServProject\trunk\code_2020\java\blaz-nop\mybatis\generator-advert.xml -overwrite
java -jar G:\ServProject\trunk\code_2020\java\blaz-nop\mybatis\mybatis-generator-core-1.4.0.jar -configfile G:\ServProject\trunk\code_2020\java\blaz-nop\mybatis\generator-version.xml -overwrite

@pause