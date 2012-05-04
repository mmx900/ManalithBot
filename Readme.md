# ManalithBot
마나리스 봇(Manalith Bot)은 PircBotX 라이브러리 기반의 오픈소스 IRC 봇입니다.
Ozinger IRC 서버의 #setzer 에서 개발하고 있습니다.

## 사양
OpenJDK 혹은 Oracle JRE 7 이상 환경에서 구동 가능합니다. 자바 하위 버전에서 동작할수도 있으나 고려하지는 않습니다.

## 빌드
Maven 2.x 이상에서 빌드 가능합니다. build.sh 혹은 build.bat를 실행하세요.

## 구동
config.xml의 내용을 수정한 후, bot.sh 혹은 bot.bat를 실행하세요.

## 플러그인 작성
다음 경로의 예제 코드를 참고하세요.
https://github.com/mmx900/ManalithBot/tree/master/ManalithBot/src/main/java/org/manalith/ircbot/plugin/sample

세 개의 예제는 각각 다음의 플러그인 작성 방식을 설명합니다.

- `HelloPlugin.java`       어노테이션을 이용한 자바 플러그인 작성 샘플. 작성이 쉬운 장점이 있습니다.
- `HelloScalaPlugin.java`  스칼라를 이용한 플러그인 작성 샘플.
- `PlainOldBotPlugin.java` 어노테이션을 이용하지 않는 플러그인 작성 샘플. 자유도가 높은 장점이 있습니다.

## 라이선스
GNU GPL 3를 따릅니다. 사용되는 라이브러리들은 각각의 라이선스를 따릅니다.

## 고장 문의
이슈로 등록해주시면 감사하겠습니다.
