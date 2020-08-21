## :pig2: 8월 3일(월) 오늘 한 일 :full_moon:

* 인증 이메일 보내기 (대체로 삽질.. 하..)

  > * 참고했던 링크 - spring boot rest 
  >
  >   [https://medium.com/@js230023/spring-boot-%EB%A9%94%EC%9D%BC-%EB%B3%B4%EB%82%B4%EA%B8%B0-f01751da4c02](https://medium.com/@js230023/spring-boot-메일-보내기-f01751da4c02)
  >
  >   https://gofnrk.tistory.com/82
  >
  >   https://a1010100z.tistory.com/6
  >
  >   https://drsggg.tistory.com/403 | 발신인의 이메일을 프로퍼티에 넣는 거였음 | 꼼꼼하게 읽기
  >
  >   ======================
  >
  >   https://newehblog.tistory.com/12
  >
  >   https://infondgndg91.blogspot.com/2018/01/spring-mybatismysql.html
  >
  >   -> 이사람 git 
  >
  >   https://github.com/ndgndg91/BalPoom
  >
  >   https://www.baeldung.com/spring-email
  >
  >   [https://medium.com/@js230023/spring-boot-%EB%A9%94%EC%9D%BC-%EB%B3%B4%EB%82%B4%EA%B8%B0-f01751da4c02](https://medium.com/@js230023/spring-boot-메일-보내기-f01751da4c02)
  >
  > * 계속 회원가입 시 인증메일을 보내려는 노력을 했고 (실패)
  >
  >   혹시나 싶어서 그냥 /accounts/email 로 메일을 보냈더니 그냥 메일을 보내는 것은 성공함
  >
  >   * MailHandler, AccountContoller, application.properties, pom.xml 수정
  >
  > * 구글 이메일 : lcy00707@gmail | 비밀번호 : ssafy1234 | property에 사용중인 비밀번호 : yguphiqgtiwcblae : 구글 2차 인증 코드 
  >
  > * ![image-20200803204011691](C:\Users\multicampus\AppData\Roaming\Typora\typora-user-images\image-20200803204011691.png)

* 오늘의 깃오류 : PULL PUSH 충돌 오류

  > Pull is not possible because you have unmerged files 에러 : git pull 시에 발생한 오류
  >
  > * 해결
  >
  > git status
  >
  > git commit -am "원래 커밋메세지"
  >
  > 



## :pig_nose: 8월 4일 (화) 오늘 한 일 :fire:

* 회원가입 시 메일 발송

  > 1. db update 
  >    * account table에 status : boolean, code : int 추가
  > 2. 회원 가입 
  >    * account 에 정보 추가
  >    * status = false(0) 
  >    * code = 랜덤 값이 들어감
  >    * 이메일 발송 (code 값 전송)
  > 3. 인증 이메일
  >    * /accounts/certification 이동
  >    * code 발급 확인
  > 4. 회원가입 확인
  >    * code 값 입력
  >    * status = true(1) 
  >    * 로그인 가능 상태

  > * 추가 사항
  >   * 코드값 변경 : 현재는 6자리 숫자 (다른 것으로 변경이 가능한지)
  >   * 로그인이 status = true(1)인 상태에 되도록 했는지 확인하기

* 비밀번호 찾기 시 메일 발송

  > 1. 비밀번호 변경을 위한 코드 변경
  >    * /accounts/changepw
  >    * code 값 변경
  >    * 이메일 발송 (code 값 전송)
  > 2. 인증 이메일
  >    * /accounts/setnewpw 이동
  >    * 변경된 code 발급 확인
  > 3. 비밀번호 변경하기
  >    * 변경된 code 값 입력
  >    * 변경할 비밀번호 입력

  > * 추가 사항
  >   * api 수정할 가능성이 있음





## :pig2: 8월 5일 (수) 오늘 한 일 :droplet:

* user code 값 변경

  > * 이전 - round(rand()*999999) : 6자리 숫자 랜덤 코드 
  >
  > * 현재 - left(md5(rand()),10) : 10자리 문자, 숫자 조합 랜덤 코드
  >
  > ![image-20200807005734481](C:\Users\multicampus\AppData\Roaming\Typora\typora-user-images\image-20200807005734481.png)

* 이메일 삽질

  > ![image-20200807005901844](C:\Users\multicampus\AppData\Roaming\Typora\typora-user-images\image-20200807005901844.png)
  >
  > * spring.mail.port 는 구글 stmp를 사용하기 위해서 고정된 포트번호로 예측됨
  >
  >   그런데 멀캠에 포트를 막아뒀는지 멀캠에서는 안되고 카페에서는 됨.. 
  >
  >   거의 4시간 정도를 삽질함
  >
  > * 아래주석 코드는 사용하지 않아도 작동함

* 이메일 수신 html 변경

  > ![image-20200807010215058](C:\Users\multicampus\AppData\Roaming\Typora\typora-user-images\image-20200807010215058.png)
  >
  > * button 부분이 안먹혀서 링크로 대체함 - 추후 디자인 작업이 필요할 예정
  > * **추가 : 비밀번호 변경 api가 프론트에서 잘 못 매핑되어있음**

* db에서 비밀번호 암호화 작업

  > * /accounts 로 검색 시 타인의 계정에 접근하지 못하도록 암호화 설정
  > * ![image-20200807010456508](C:\Users\multicampus\AppData\Roaming\Typora\typora-user-images\image-20200807010456508.png)
  > * ![image-20200807010545719](C:\Users\multicampus\AppData\Roaming\Typora\typora-user-images\image-20200807010545719.png)
  >
  > ​        암호화 작업 : hashpw()
  >
  > * 복호화 작업이 아닌, 입력된 값과 암호화 된 코드를 비교하는 함수가 존재
  >
  > ​       그래서 mapper의 sql문에서 password 비교문 삭제
  >
  > ​       ![image-20200807010829019](C:\Users\multicampus\AppData\Roaming\Typora\typora-user-images\image-20200807010829019.png)
  >
  > ​        이런식으로 비교해서 로그인 컨트롤러 만듬
  >
  > * 비밀번호 변경시에도 암호화하여 db에 저장함



## :pig2: 8월 7일 (금) 오늘 한 일 :eight_pointed_black_star:

* 혜선님한테 /accounts/setnewpw 로 가는지 확인하기 -> 확인하고 빌드하기

  > 회원가입도 인증메일 받기까지만 된다

* 빌드 상태에서 이메일 되는지 확인하기

  > 안된다

* 현수 발표 도와주기

* 프론트쪽에 버튼을 포함한 html 수정 요청

* jaraauth@gmail.com

  jara987654

  wcdiwstdbdudicnv : 최신

  발신 메일 서버 포트 = 587

* stmp 블로그에 정리하기

* 날씨 클론, 크론 2번 요청 -> 디비에 저장 (weather 테이블??

* 관리자 계정 추가하기 : 신고 처리하기

* 회원 삭제 기능 추가하기 (지금 디비에서도 안되는데 그것을 수정하기)



## :pig2: 8월 10일 (월) 오늘 한 일 :full_moon:

* 비밀번호 암호화 - 백이 아니라 db를 수정해서 더 이상 db에서 비번을 찾을 수 없도록 만들기

  > 새로 회원가입 한 유저만 암호화 처리됨
  >
  > 추후에 데이터 베이스를 정리 한 후 사용하거나 아예 적용하지 않거나 생각해보기

* 회원 삭제 + 신고기능

  > * db 정리 
  >
  >   * 외래키 정리하기 
  >
  >   alter table add 하는 방법은 오류가 발생함
  >
  >   테이블을 새로 구성하거나 / 워크벤치 기능을 사용해서 ON DELETE CASCADE 문을 추가한다
  >
  > * 신고 기능 추가했음
  >
  >   * 세번 신고당한 유저는 바로 삭제되도록 구현



## :pig2: 8월 11일 (화) 오늘 한 일 :fire:

* location 기능 - cron 사용

  > 이미 있는 location 에 cron으로 받아온 실시간 데이터를 update 시켜준다
  >
  > /info 로 location에 해당하는 날씨 값을 넘겨줌

  

## :pig2: 8월 12일 (수) 오늘 한 일 :droplet:

* 로그인 오류 찾아내기 : firebase와 frontend 부분의 연결이 복잡 (연동 순서) - 해결
* 날씨 기능 backend 마무리



## :pig2: 8월 13일 (목) 오늘 한 일 :deciduous_tree:

* 관리자 페이지 만들기 : **신고 기능 관련** - swagger test 완료

  > 현재는 관리자 페이지에서 회원을 삭제하면 database에서 delete user
  >
  > 추후에 변경시  - database에서 삭제하지않고 관리자계정에서도 신고 리스트를 보여줌
  >
  > ​							user의 status = 2로 적용해서 확인가능하도록 (복잡)

* 이메일 인증 확인하기 : 회원가입 & 비밀번호 변경 메일 발송 완료

  > 인증코드 입력 기능 해결해야함 (회원가입/비밀번호변경)

  

## :pig2: 8월 14일 (금) 오늘 한 일 :eight_pointed_black_star:

* 발표 + 시연준비 : 시나리오 생각하기 - 발표 안함

  > * 사진 업로드
  > * XSS 필터 - 저번에 시연안됐던 것
  > * barter - 디테일
  > * tip - 디테일
  > * 날씨 정보 노출
  > * 마이페이지 
  > * 다른 유저 검색 : 글 선택가능, 글보러가기 이동
  > * 신고 기능 : 신고하면 관리자 계정으로 회원 탈퇴 가능
  > * 체크리스트 - todo list
  > * 회원가입 : 약관추가 
  > * 이메일 (회원가입시, 비밀번호 변경시)

* 회원 정보 반환 수정정도..(아무것도 안함.. 실화냐)



## :pig2: 8월 16일 (일) 오늘 한 일 :sun_with_face:

* 카카오 로그인 api 사용해서 로그인 

  > 1. redirect uri, 서비스 url 설정
  >
  > 2. 맨 처음 들어가는 링크 
  >
  >    > https://kauth.kakao.com/oauth/authorize?client_id=2e50ed388c52dc3ef17eb1c332285923&redirect_uri=https://i3a308.p.ssafy.io/jara/accounts/signin/kakao/access&response_type=code 
  >
  > 3. authorize_code 값을 받음 (함수 생성)
  >
  > 4. authorize_code 값을 사용해서 access_token, refresh_token을 받음
  >
  > 5. access_token으로 json 객체로 로그인 정보를 받음
  >
  > 6. json객체로 받은 값 -> 회원가입 및 로그인을 위해 매핑 추가, 함수 추가
  >
  > * 문제들
  >   * 카카오 로그인 api가 친절하지 않음
  >   * redirect uri 설정
  >   * 스프링이 지맘대로임 됐다~ 안됐다~ 그래서 json 객체를 받아오는데 오래걸림



## :pig2: 8월 17일 (월) 오늘 한 일 :full_moon:

* 비밀번호 찾기 - 회원정보에 등록되지 않은 사람들한테도 이메일 발송 (해결)

  > api 요청을 모두 pwd로 수정

* 회원가입 인증 메일 - 인증은 되나 메일에 인증코드 값이 null로 감 (해결)

* 올바른 DB 값 채우기




## :pig2: 8월 18일 (화) 오늘 한 일 :fire:

* 신고기능 - 신고자 id 값에 따른 신고자 nickname 전달

* 최종 발표 회의



## :pig2: 8월 19일 (수) 오늘 한 일 :droplet:

* 회원가입 시 자동으로 관리자 계정 팔로우 하기

* 코드 정리

  > * 주석 달기
  > * print 삭제
  >
  > * 안타까운 내 암호화 코드.. 여전히 주석처리 
  > * 그 외에 불필요한 코드 삭제  - account, report 



## :pig2: 8월 20일 (목) 오늘 한 일 :deciduous_tree:

* ppt 완성



## :pig2: 8월 21일 (금) 오늘 한 일 :eight_pointed_black_star:

* ppt 에 영상 추가
* 발표준비