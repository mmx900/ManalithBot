0.0.0 / 2012-05-03
==================

2011. 10. 31
CER ( Currency Exchange Rate ) is added. ( -> darkcircle )
 - Requirement
   > sqlitejdbc-v056.jar : JDBC SQLite 커넥터 사용하기 위한 파일. 컴파일 할때 필요.
     -> http://files.zentus.com/sqlitejdbc/sqlitejdbc-v056.jar 에서 받았음.
   > 그놈 채널에 봇 입장전 테스트 채널에서 !cer fupdate를 실행. (프로퍼티, DB 갱신 및 준비)
   > LatestUpdatedDatetime.prop과 currency.db는 클래스 경로 루트상에 있어야 함.

Calc is updated ( -> darkcircle )
 > factorial operator is added.
 > sqrt function is added.

2011. 11. 01
CER ( Currency Exchange Rate ) is updated ( -> darkcircle )
 - Requirement
  > (2011. 10. 31일자 첫번째 요구사항 삭제)
  > Maven 에서 org.xerial.sqlite-jdbc.3.7.2 를 받아야 됨. 
    ( 실행시 -cp 옵션에 MAVEN_REPOS/org/xerial/sqlite-jdbc/3.7.2/sqlite-jdbc.3.7.2.jar 추가. )
  > 그놈 채널에 봇 입장 전 테스트 채널에서 !cer fupdate를 실행. 
    ( 프로퍼티, DB 갱신 및 준비 : org/manalith/ircbot/plugin/CER/data 에 저장됨 )
  > (2011. 10. 31일자 세번째 요구사항 삭제)

2011. 11. 05
KVL ( Kernel Version List ) is added ( -> darkcircle )

2011. 11. 06
CER ( Currency Exchange Rate ) is updated
 - NPE 유발 코드 제거됨. ( -> mmx900 ) 


2011. 11. 07
Calc is updated ( -> darkcircle )
 > 인자가 하나밖에 없을 때의 문제 수정
 > 팩토리얼 연산 문제 수정
 > sqrt computation problem is fixed

2011. 11. 12
CER ( Currency Exchange Rate ) is updated ( -> darkcircle )
 > Database engine is changed
   - SQLite -> HSQLDB

2011. 11. 29
CER ( Currency Exchange Rate ) is updated ( -> darkcircle )
 > Database engine is set back due to logger problem
   - HSQLDB -> SQLite

2011. 12. 16
Calc is updated ( -> darkcircle )
 > CalcPlugin's parent object is changed to AbstractBotPlugin

KVL is updated ( -> darkcircle )
 > KVLTokenAnalyzer's token analyzing problem is fixed
   ( TextString's subtype was Unknown : FIXED )
 > KVLPlugin's parent object is changed to AbstractBotPlugin

CER is updated ( -> darkcircle )
 > Datapath problem is fixed by using getResourcePath()
   which is a brand new method from AbstractBotPlugin
 > CERPlugin's parent object is changed to AbstractBotPlugin
 
DistroPkgFinder comes as new feature! ( -> darkcircle )
 > Debian, Ubuntu, Fedora is supported to find a package we want to know ( !deb, !ubu, !fed )

2011. 12. 17
DistroPkgFinder's bug is fixed ( -> setzer )
 > DistroPkgFinder's three or more word imprudent rejection problem is fixed

AbstractBotPlugin is modified ( -> setzer )
 > Separator is added at the end of ResourcePath

weather plugin is added ( -> setzer )
 > This is derivering weather information using google API

Gentoo Distro's package searching feature is added ( -> darkcircle )
 > DistroPkgFinder->GentooPkgFinderJsoupRunner

2011. 12. 24
CER is fully implemented again ( -> darkcircle )
 > Complicating command grammar is changed

2011. 12. 25
fdbot is added ( -> darkcircle )
 > This module is for bot waiting original bot

WaitBDBot is added ( -> darkcircle )
 > This module is for bot waiting original bot. if original bot joined in the channel, this module will terminate itself

2011. 12. 27
TwitReader is added ( -> darkcircle )
 > This module helps to show text of twitter's article
  ex ) !twit https://twitter.com/#!/somebody/status/xxxxxxxxxxxxxxxxxxxxx
       => 작성자: ... 작성시각: ... 작성 클라이언트: ...
	      본문: ...

newCER command is changed to "!curex"

Custom setting feature is added to newCER
 > !curex:sub [list_of_comma_separated_currency_unit] : set a custom view for his/herself
 > !curex:unsub : remove his/her own custom view

2011. 12. 29
NvidiaDriverNews is added ( -> darkcircle )

2012. 12. 31
newCER is renamed to cer2 ( -> mmx900 )

2012. 01 .01
google is modified to search an information using google API ( -> mmx900 )

2012. 01. 02
missedmessage is added ( -> darkcircle )
 > It can says missed message by anyone to someone who was left and returned into the channel again.

