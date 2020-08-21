# Sub PJT III - 모바일 웹 디자인 및 기본 구성

## :bar_chart: 진척률(%)
- 이슈 진행률 : 100%

## :computer: Getting Started (개발환경 및 빌드 실행)

### Front-End
- 개발환경 설치
    - yarn 설치 : https://classic.yarnpkg.com/en/docs/install#windows-stable
    - Vue-cli 설치 : `yarn global add @vue/cli`
    - Sass 설정 : `yarn add node-sass sass-loader`
    - Vue-router, Vuex 설치 : `yarn add vue-router vuex`
- 서버 실행 : `yarn serve --port 3030`
- URL : localhost:3030

### Back-End
- 개발환경 설치
    - Spring Boot 설치 (jdk 다운로드 & 환경변수 설정)
    - Maven 설치 (Maven 다운로드 & 환경변수 설정)
- 서버 실행 : WebCurationApplication.java --> Run
- URL : localhost:8081
- Swagger URL : localhost:8081/swagger-ui.html

### DB (MariaDB)
- AWS 서버에서 Docker에 MariaDB를 올려 사용.
- mariadb 인코딩 (맨 처음에 한 번만 설정)
    - 참고 : https://app-developer.tistory.com/121
- mariadb timezone 변경 (맨 처음에 한 번만 설정) (데이터베이스들의 시간을 한국시간으로 맞추기 위해서)
    - 컨테이터 안으로 들어가기: `docker exec -it maria-db bash`
    - 설정
```
// 기존 시간 설정 제거
sudo rm /etc/localtime
// Asia/Seoul로 설정
sudo ln -s /usr/share/zoneinfo/Asia/Seoul /etc/localtime
// 시간 확인
sudo date
```

### AWS & Docker
- AWS 서버 접속
    - `ssh -i [pem key] ubuntu@[URL]`
        - ex) `ssh -i "I3A308T.pem" ubuntu@i3a308.p.ssafy.io`
- AWS 서버 timezone 설정 (맨 처음에 한 번만 설정)
```
// 기존 시간 설정 제거
sudo rm /etc/localtime
// Asia/Seoul로 설정
sudo ln -s /usr/share/zoneinfo/Asia/Seoul /etc/localtime
// 시간 확인
sudo date
```
- Docker에서 구동 중인 Container 확인
    - `docker container ps`
- Dcoker의 모든 Container 확인
    - `docker container ps -a`
- mariadb 컨테이터 안으로 들어가기
    - `docker exec -it maria-db bash`
- mariadb 접속
    - `mariadb -u root -p`
- Container 안으로 들어가지 않고 바로 mariadb 접속
    - `docker exec -it maria-db mysql -u root -p`
- Container가 꺼졌을 때 키는법
    - `docker start [Container 이름]`
    - ex) `docker start maria-db`

### Nginx 사용 명령어
```
// 시작
$ sudo service nginx start
$ sudo systemctl start nginx
$ sudo /etc/init.d/nginx start

// 재시작
$ sudo service nginx restart
$ sudo systemctl restart nginx
$ sudo /etc/init.d/nginx restart

// 중지
$ sudo service nginx stop
$ sudo systemctl stop nginx
$ sudo /etc/init.d/nginx stop

// 상태
$ sudo service nginx status
$ sudo systemctl status nginx

// 설정 reload
$ sudo service nginx reload
$ sudo systemctl reload nginx
$ sudo nginx -s reload

// configuration file syntax check
$ sudo nginx -t
```