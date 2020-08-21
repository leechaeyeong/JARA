

### :feet: 20.08.03

##### Firebase Cloud Messaging에 대해 공부

> Spring boot에서 토큰과 로직을 처리하고 Vue.js에서 알림을 줄 수 있도록
>
> - https://firebase.google.com/docs/cloud-messaging
> - [https://velog.io/@skygl/FCM-Spring-Boot%EB%A5%BC-%EC%82%AC%EC%9A%A9%ED%95%98%EC%97%AC-%EC%9B%B9-%ED%91%B8%EC%8B%9C-%EA%B8%B0%EB%8A%A5-%EA%B5%AC%ED%98%84%ED%95%98%EA%B8%B0](https://velog.io/@skygl/FCM-Spring-Boot를-사용하여-웹-푸시-기능-구현하기)
>
> Firebase에 project 생성
>
> Admin SDK 구성 스니펫 비공개 키 생성
>
> Spring boot에 관련 java 코드 구현

-----



### :feet: 20.08.04

##### Firebase Clude Messaging을 구현하려 노력

##### https://firebase.google.com/docs/cloud-messaging/migrate-v1#windows 에서 제공해준 코드를 이용하였으나, 구현 실패

``` bash
package com.ssafy.jara.common.firebase;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Scanner;

import org.springframework.core.io.ClassPathResource;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

public class Messaging {
	private static final String PROJECT_ID = "fcm-jara";
	private static final String BASE_URL = "https://fcm.googleapis.com";
	private static final String FCM_SEND_ENDPOINT = "/v1/projects/" + PROJECT_ID + "/messages:send";

	private static final String MESSAGING_SCOPE = "https://www.googleapis.com/auth/firebase.messaging";
	private static final String[] SCOPES = { MESSAGING_SCOPE };

	private static final String TITLE = "새로운 알림이 도착!";
	private static final String BODY = "새로운 게시물/팁이 등록되었어요! 어서 가보세요!";
	public static final String MESSAGE_KEY = "message";
	
	private static final String GOOGLE_APPLICATION_CREDENTIALS = "jara-fcm-firebase-adminsdk-68twq-d0e84cbd44.json";

	/**
	 * Retrieve a valid access token that can be use to authorize requests to the
	 * FCM REST API.
	 *
	 * @return Access token.
	 * @throws IOException
	 */
	// [START retrieve_access_token]
	private static String getAccessToken() throws IOException {
		GoogleCredential googleCredential = GoogleCredential.fromStream(new ClassPathResource(GOOGLE_APPLICATION_CREDENTIALS).getInputStream()) // jara-fcm-firebase-adminsdk-68twq-d0e84cbd44.json
				.createScoped(Arrays.asList(SCOPES));
		googleCredential.refreshToken();
		return googleCredential.getAccessToken();
	}
	// [END retrieve_access_token]

	/**
	 * Create HttpURLConnection that can be used for both retrieving and publishing.
	 *
	 * @return Base HttpURLConnection.
	 * @throws IOException
	 */
	private static HttpURLConnection getConnection() throws IOException {
		// [START use_access_token]
		URL url = new URL(BASE_URL + FCM_SEND_ENDPOINT);
		HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
		httpURLConnection.setRequestProperty("Authorization", "Bearer " + getAccessToken());
		httpURLConnection.setRequestProperty("Content-Type", "application/json; UTF-8");
		return httpURLConnection;
		// [END use_access_token]
	}

	/**
	 * Send request to FCM message using HTTP.
	 *
	 * @param fcmMessage Body of the HTTP request.
	 * @throws IOException
	 */
	private static void sendMessage(JsonObject fcmMessage) throws IOException {
		HttpURLConnection connection = getConnection();
		connection.setDoOutput(true);
		DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
		outputStream.writeBytes(fcmMessage.toString());
		outputStream.flush();
		outputStream.close();

		int responseCode = connection.getResponseCode();
		if (responseCode == 200) {
			String response = inputstreamToString(connection.getInputStream());
			System.out.println("Message sent to Firebase for delivery, response:");
			System.out.println(response);
		} else {
			System.out.println("Unable to send message to Firebase:");
			String response = inputstreamToString(connection.getErrorStream());
			System.out.println(response);
		}
	}

	/**
	 * Send a message that uses the common FCM fields to send a notification message
	 * to all platforms. Also platform specific overrides are used to customize how
	 * the message is received on Android and iOS.
	 *
	 * @throws IOException
	 */
	private static void sendOverrideMessage() throws IOException {
		JsonObject overrideMessage = buildOverrideMessage();
		System.out.println("FCM request body for override message:");
		prettyPrint(overrideMessage);
		sendMessage(overrideMessage);
	}

	/**
	 * Build the body of an FCM request. This body defines the common notification
	 * object as well as platform specific customizations using the android and apns
	 * objects.
	 *
	 * @return JSON representation of the FCM request body.
	 */
	private static JsonObject buildOverrideMessage() {
		JsonObject jNotificationMessage = buildNotificationMessage();

		JsonObject messagePayload = jNotificationMessage.get(MESSAGE_KEY).getAsJsonObject();
		messagePayload.add("android", buildAndroidOverridePayload());

		JsonObject apnsPayload = new JsonObject();
		apnsPayload.add("headers", buildApnsHeadersOverridePayload());
		apnsPayload.add("payload", buildApsOverridePayload());

		messagePayload.add("apns", apnsPayload);

		jNotificationMessage.add(MESSAGE_KEY, messagePayload);

		return jNotificationMessage;
	}

	/**
	 * Build the android payload that will customize how a message is received on
	 * Android.
	 *
	 * @return android payload of an FCM request.
	 */
	private static JsonObject buildAndroidOverridePayload() {
		JsonObject androidNotification = new JsonObject();
		androidNotification.addProperty("click_action", "android.intent.action.MAIN");

		JsonObject androidNotificationPayload = new JsonObject();
		androidNotificationPayload.add("notification", androidNotification);

		return androidNotificationPayload;
	}

	/**
	 * Build the apns payload that will customize how a message is received on iOS.
	 *
	 * @return apns payload of an FCM request.
	 */
	private static JsonObject buildApnsHeadersOverridePayload() {
		JsonObject apnsHeaders = new JsonObject();
		apnsHeaders.addProperty("apns-priority", "10");

		return apnsHeaders;
	}

	/**
	 * Build aps payload that will add a badge field to the message being sent to
	 * iOS devices.
	 *
	 * @return JSON object with aps payload defined.
	 */
	private static JsonObject buildApsOverridePayload() {
		JsonObject badgePayload = new JsonObject();
		badgePayload.addProperty("badge", 1);

		JsonObject apsPayload = new JsonObject();
		apsPayload.add("aps", badgePayload);

		return apsPayload;
	}

	/**
	 * Send notification message to FCM for delivery to registered devices.
	 *
	 * @throws IOException
	 */
	public static void sendCommonMessage() throws IOException {
		JsonObject notificationMessage = buildNotificationMessage();
		System.out.println("FCM request body for message using common notification object:");
		prettyPrint(notificationMessage);
		sendMessage(notificationMessage);
	}

	/**
	 * Construct the body of a notification message request.
	 *
	 * @return JSON of notification message.
	 */
	private static JsonObject buildNotificationMessage() {
		JsonObject jNotification = new JsonObject();
		jNotification.addProperty("title", TITLE);
		jNotification.addProperty("body", BODY);

		JsonObject jMessage = new JsonObject();
		jMessage.add("notification", jNotification);
		jMessage.addProperty("topic", "news");

		JsonObject jFcm = new JsonObject();
		jFcm.add(MESSAGE_KEY, jMessage);

		return jFcm;
	}

	/**
	 * Read contents of InputStream into String.
	 *
	 * @param inputStream InputStream to read.
	 * @return String containing contents of InputStream.
	 * @throws IOException
	 */
	private static String inputstreamToString(InputStream inputStream) throws IOException {
		StringBuilder stringBuilder = new StringBuilder();
		Scanner scanner = new Scanner(inputStream);
		while (scanner.hasNext()) {
			stringBuilder.append(scanner.nextLine());
		}
		return stringBuilder.toString();
	}

	/**
	 * Pretty print a JsonObject.
	 *
	 * @param jsonObject JsonObject to pretty print.
	 */
	private static void prettyPrint(JsonObject jsonObject) {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		System.out.println(gson.toJson(jsonObject) + "\n");
	}

	public static void main(String[] args) throws IOException {
		if (args.length == 1 && args[0].equals("common-message")) {
			sendCommonMessage();
		} else if (args.length == 1 && args[0].equals("override-message")) {
			sendOverrideMessage();
		} else {
			System.err.println("Invalid command. Please use one of the following commands:");
			// To send a simple notification message that is sent to all platforms using the
			// common
			// fields.
			System.err.println("./gradlew run -Pmessage=common-message");
			// To send a simple notification message to all platforms using the common
			// fields as well as
			// applying platform specific overrides.
			System.err.println("./gradlew run -Pmessage=override-message");
		}
	}
}
```

##### Firebase 사용이 쉽다고 하였는데, 쉽지 않았음



##### 유저 페이지 기능 추가

>Account Dto에 myArticleList(유저가 작성한 게시글(피드)), scrapTipList(스크랩한 팁) 추가
>
>/account/{user_id} GET 요청 시, List들을 같이 넘겨 줌

-----



### :feet: 20.08.05

##### Firebase Clude Messaging을 구현하기 위해 다시 도전

###### pom.xml

```bash
<!-- Firebase -->
<dependency>
	<groupId>com.google.firebase</groupId>
	<artifactId>firebase-admin</artifactId>
	<version>6.8.1</version>
</dependency>
		
<!-- OKHttp 3 -->
<dependency>
	<groupId>com.squareup.okhttp3</groupId>
	<artifactId>okhttp</artifactId>
	<version>4.2.2</version>
</dependency>

```



###### FcmMessage.java

```b
package com.ssafy.jara.common.firebase;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class FcmMessage {

	private boolean validate_only;
	private Message message;
	
	public FcmMessage() {}
	
	public FcmMessage(boolean validate_only, Message message) {
		this.validate_only = validate_only;
		this.message = message;
	}
	
	@AllArgsConstructor
	@Getter
	@Setter
	public static class Message {
		private Notification notification;
		private String token;
		
		public Message() {}
		
		public Message(Notification notification, String token) {
			this.notification = notification;
			this.token = token;
		}
	}
	
	@AllArgsConstructor
	@Getter
	@Setter
	public static class Notification {
		private String title;
		private String body;
		private String image;
		
		public Notification() {}
		
		public Notification(String title, String body, String image) {
			this.title = title;
			this.body = body;
			this.image = image;
		}
	}

}
```



###### FcmService.java

```b
package com.ssafy.jara.common.firebase;

import java.io.IOException;
import java.util.List;

import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.gson.JsonObject;
import com.ssafy.jara.common.firebase.FcmMessage.Message;
import com.ssafy.jara.common.firebase.FcmMessage.Notification;

import lombok.RequiredArgsConstructor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


@Component
@RequiredArgsConstructor
public class FcmService {
	
	public final String API_URL = "https://fcm.googleapis.com/v1/projects/jara-fcm/messages:send";
	
	public void sendMessageTo(String targetToken, String title, String body) throws IOException {
//		String message = makeMessage(targetToken, title, body);
		
		JsonObject jNotification = new JsonObject();
		jNotification.addProperty("title", title);
		jNotification.addProperty("body", body);

		JsonObject jMessage = new JsonObject();
		jMessage.add("notification", jNotification);
//		jMessage.addProperty("topic", "news");
		jMessage.addProperty("token", targetToken);

		JsonObject jFcm = new JsonObject();
		jFcm.add("message", jMessage);
		
		System.out.println(jFcm);
		
		OkHttpClient client = new OkHttpClient();
//		RequestBody requestBody = RequestBody.create(message, MediaType.get("application/json; charset=utf-8"));
		RequestBody requestBody = RequestBody.create(jFcm.toString(), MediaType.get("application/json; charset=utf-8"));
		
		Request request = new Request.Builder()
									.url(API_URL)
									.post((okhttp3.RequestBody) requestBody)
									.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
									.addHeader(HttpHeaders.CONTENT_TYPE, "application/json; UTF-8")
									.build();
		
		Response response = client.newCall(request).execute();
		
		System.out.println(response.body().string());
	}
	
	private String makeMessage(String targetToken, String title, String body) throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		
//		FcmMessage fcmMessage = FcmMessage.builder()
//									.message(FcmMessage.Message.builder()
//									.token(targetToken)
//									.notification(FcmMessage.Notification.builder()
//										.title(title)
//										.body(body)
//										.image(null)
//										.build()
//									)
//									.build()
//									)
//									.validate_only(false)
//									.build();
		
		Notification notification = new Notification(title, body, null);
		Message message = new Message(notification, targetToken);
		FcmMessage fcmMessage = new FcmMessage(true, message); 
		
		return objectMapper.writeValueAsString(fcmMessage);
	}

	private String getAccessToken() throws IOException {
		String GOOGLE_APPLICATION_CREDENTIALS = "jara-fcm-firebase-adminsdk-68twq-d0e84cbd44.json";
		
		// GoogleCredentials : GoogleApi 사용을 위한 oauth2를 이용해 인증한 대상을 나타내는 객체
		GoogleCredentials googleCredentials = GoogleCredentials.fromStream(new ClassPathResource(GOOGLE_APPLICATION_CREDENTIALS).getInputStream()) // json 객체의 인스턴스를 얻음
				.createScoped(List.of("https://www.googleapis.com/auth/cloud-platform")); // 인증하는 서버에서 필요로 하는 권한 지정
		
		googleCredentials.refreshIfExpired(); // AccessToken 생성
		
		return googleCredentials.getAccessToken().getTokenValue(); // getAccessToken()으로 토큰을 받아옴 / getTokenValue()로 토큰을 얻음
	}
}
```



##### AticleController에서 sendMessageTo()를 호출하여 메시지를 보내보았으나, 토큰이 denied 되어서 실패



##### Tips 댓글 등록 시, DB에 등록하고 해당 댓글 객체 프론트로 전송

##### Tips 상세 조회 시, 좋아하는 user_id 목록 및 댓글 목록 추가 전송

##### Tips 좋아요 기능 구현 변경



##### 비록 FCM을 구현하지 못하였지만, Lombok에 대해서 알게 되었다.

-----



### :feet: 20.08.07

##### Naver Lucy XSS Filter를 이용하여 XSS 보안 강화

>src/main/resources/lucy-xss-servlet-filter-rule.xml

```bash
<?xml version="1.0" encoding="UTF-8"?>

<config xmlns="http://www.navercorp.com/lucy-xss-servlet">
    <defenders>
        <!-- XssPreventer 등록 -->
        <defender>
            <name>xssPreventerDefender</name>
            <class>com.navercorp.lucy.security.xss.servletfilter.defender.XssPreventerDefender</class>
        </defender>

        <!-- XssSaxFilter 등록 -->
        <defender>
            <name>xssSaxFilterDefender</name>
            <class>com.navercorp.lucy.security.xss.servletfilter.defender.XssSaxFilterDefender</class>
            <init-param>
                <param-value>lucy-xss-sax.xml</param-value>   <!-- lucy-xss-filter의 sax용 설정파일 -->
                <param-value>false</param-value>        <!-- 필터링된 코멘트를 남길지 여부, 성능 효율상 false 추천 -->
            </init-param>
        </defender>

        <!-- XssFilter 등록 -->
        <defender>
            <name>xssFilterDefender</name>
            <class>com.navercorp.lucy.security.xss.servletfilter.defender.XssFilterDefender</class>
            <init-param>
                <param-value>lucy-xss.xml</param-value>    <!-- lucy-xss-filter의 dom용 설정파일 -->
                <param-value>false</param-value>         <!-- 필터링된 코멘트를 남길지 여부, 성능 효율상 false 추천 -->
            </init-param>
        </defender>
    </defenders>

    <!-- default defender 선언, 필터링 시 지정한 defender가 없으면 여기 정의된 default defender를 사용해 필터링 한다. -->
    <default>
        <defender>xssPreventerDefender</defender>
    </default>

</config>
```

>pom.xml

```bash
<!-- Lucy XSS Filter -->
<dependency>
	<groupId>com.navercorp.lucy</groupId>
	<artifactId>lucy-xss-servlet</artifactId>
	<version>2.0.0</version>
</dependency>
```



##### 그러나 Lucy XSS Filter는 @RequestBody로 들어오는 JSON 객체를 필터링 해주지 않음. 그래서 따로 필터를 구현 - JsonFilter

> com/ssafy/jara/config/FilterConfig.java

```bash
package com.ssafy.jara.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.navercorp.lucy.security.xss.servletfilter.XssEscapeServletFilter;
import com.ssafy.jara.filter.JsonFilter;

@Configuration
public class FilterConfig implements WebMvcConfigurer {

	// Naver Lucy XSS Filter bean 등록
	@Bean
    public FilterRegistrationBean<XssEscapeServletFilter> getFilterRegistraionBean() {
    	FilterRegistrationBean<XssEscapeServletFilter> registraionBean = new FilterRegistrationBean<XssEscapeServletFilter>();
    	registraionBean.setFilter(new XssEscapeServletFilter());
    	registraionBean.setOrder(1);
    	registraionBean.addUrlPatterns("/*");
    	
    	return registraionBean;
    }
	
	// Json XSS Filter bean 등록
	@Bean
	public FilterRegistrationBean<JsonFilter> getJsonFilterRegistrationBean() {
		FilterRegistrationBean<JsonFilter> registrationBean = new FilterRegistrationBean<JsonFilter>(new JsonFilter());
		registrationBean.setOrder(2);
		registrationBean.addUrlPatterns("/*");
		
		return registrationBean;
	}
	
}
```

>com/ssafy/jara/filter/JsonFilter.java
>
><script></script> , #{, #{}, ${, ${} 등 특정 문자열만 필터링 

```bash
package com.ssafy.jara.filter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;

import javax.servlet.FilterChain;
import javax.servlet.ReadListener;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.web.filter.OncePerRequestFilter;

public class JsonFilter extends OncePerRequestFilter {
	
	// Json value XSS Filter를 위한 필터링 값 리스트
	public static ArrayList<String> checkList = new ArrayList<String>(Arrays.asList("<script>", "</script>", "#{", "#{}", "${", "${}"));   
	
	@Override
	protected void doFilterInternal(
		HttpServletRequest req, HttpServletResponse resp, FilterChain chain)
		throws ServletException, IOException {

		chain.doFilter(new SimpleXssFilterInternal(req), resp);
	}
	
	public static class SimpleXssFilterInternal extends HttpServletRequestWrapper {
		
		private byte[] body;
		
		public SimpleXssFilterInternal(HttpServletRequest request) {
			super(request);
			
			try {
				InputStream is = request.getInputStream();
				
				if(is != null) {
					StringBuffer sb = new StringBuffer();
					
					while(true) {
						int data = is.read();
						
						if(data < 0) 
							break;
						
						sb.append((char) data);
					}
					
					String result = doWork(sb.toString());
					
					body = result.getBytes(StandardCharsets.UTF_8);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		@Override
		public ServletInputStream getInputStream() throws IOException {
			ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(this.body);
			
			return new ServletInputStream() {
				
				@Override
				public int read() throws IOException {
					return byteArrayInputStream.read();
				}
				
				@Override
				public void setReadListener(ReadListener listener) {}
				
				@Override
				public boolean isReady() {
					return true;
				}
				
				@Override
				public boolean isFinished() {
					return byteArrayInputStream.available() == 0;
				}
			};
		}
		
		private String doWork(String input) {
			
			for (int i = 0; i < checkList.size(); i++) {
				String s = checkList.get(i);
				
				if(input.indexOf(s) >= 0) {
					input = input.replaceAll(s, StringEscapeUtils.escapeHtml4(s));
				}
			}
			
			return input;
		}
	}
	
}

```

-----



### :feet: 20.08.07

##### Json Filter 사용 시, 한글 깨짐 현상 해결

```bash
package com.ssafy.jara.filter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;

import javax.servlet.FilterChain;
import javax.servlet.ReadListener;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.filter.OncePerRequestFilter;

public class JsonFilter extends OncePerRequestFilter {
	
	protected static Log log = LogFactory.getLog(JsonFilter.class);
	
	// Json value XSS Filter를 위한 필터링 값 리스트
	public static ArrayList<String> checkList = new ArrayList<String>(Arrays.asList("<script>", "</script>", "#{", "#{}", "${", "${}"));   
	
	@Override
	protected void doFilterInternal(
		HttpServletRequest req, HttpServletResponse resp, FilterChain chain)
		throws ServletException, IOException {

		chain.doFilter(new SimpleXssFilterInternal(req), resp);
	}
	
	public static class SimpleXssFilterInternal extends HttpServletRequestWrapper {
		
		private byte[] body;
		
		public SimpleXssFilterInternal(HttpServletRequest request) {
			super(request);
			
			try {
				
				InputStream is = request.getInputStream();
				
				InputStreamReader isr = new InputStreamReader(is, "UTF-8"); // 한글 깨짐 해결
				
				if(isr != null) {
					StringBuffer sb = new StringBuffer();
					
					while(true) {
						int data = isr.read();
						
						if(data < 0) 
							break;
						
						sb.append((char) data);
					}
					
					String result = doWork(sb.toString());
					body = result.getBytes(StandardCharsets.UTF_8);
					
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		@Override
		public ServletInputStream getInputStream() throws IOException {
			ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(this.body);
			
			return new ServletInputStream() {
				
				@Override
				public int read() throws IOException {
					return byteArrayInputStream.read();
				}
				
				@Override
				public void setReadListener(ReadListener listener) {}
				
				@Override
				public boolean isReady() {
					return true;
				}
				
				@Override
				public boolean isFinished() {
					return byteArrayInputStream.available() == 0;
				}
			};
		}
		
		private String doWork(String input) {
			
			for (int i = 0; i < checkList.size(); i++) {
				String s = checkList.get(i);
				
				if(input.indexOf(s) >= 0) {
					input = input.replaceAll(s, StringEscapeUtils.escapeHtml4(s));
				} 
			}
						
			return input;
		}
	}
	
}
```

에서

```bash
InputStream is = request.getInputStream();
				
InputStreamReader isr = new InputStreamReader(is, "UTF-8"); // 한글 깨짐 해결
```

이 부분의 코드로 해결



##### Sub3 1차 평가

- 발표자를 맡게 되서 발표 준비
  - 기존 PPT에 내용을 삭제하고 추가해서 compact한 PPT 자료 준비
  - AWS 서버를 사용한 시연 준비
- PPT 자료로 최종 기획안 / 이번주 구현 내용 / AWS 배포 주기 발표 후 시연

-----



### :feet: 20.08.10

##### Article, Either Comment 수정 시 반환값 수정

##### Location 테이블에 격자 위도/경도 (nx/ny) 정보 추가

##### 기상청 동네기상예보 api 연동

```bash
package com.ssafy.jara.common.weather;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/weather")
public class WeatherController {

	@GetMapping("")
	public String getWeather() throws IOException {
		StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1360000/VilageFcstInfoService/getUltraSrtFcst"); /*URL*/
//		StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1360000/VilageFcstInfoService/getVilageFcst"); /*URL*/
//        urlBuilder.append("?" + URLEncoder.encode("ServiceKey","UTF-8") + "=서비스키"); /*Service Key*/
//        urlBuilder.append("&" + URLEncoder.encode("ServiceKey","UTF-8") + "=" + URLEncoder.encode("DysdYWgqnU55g9Q7PoDso1H3%2BQJFLffwk4YkWWCS4cSluMp9qKnSyq0J0u1PXFITKErf1yqWK%2FrwEUKlHTVePw%3D%3D", "UTF-8")); /*공공데이터포털에서 받은 인증키*/
        urlBuilder.append("?" + URLEncoder.encode("ServiceKey","UTF-8") + "=" + "DysdYWgqnU55g9Q7PoDso1H3%2BQJFLffwk4YkWWCS4cSluMp9qKnSyq0J0u1PXFITKErf1yqWK%2FrwEUKlHTVePw%3D%3D"); /*공공데이터포털에서 받은 인증키*/
        urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지번호*/
        urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("40", "UTF-8")); /*한 페이지 결과 수*/
        urlBuilder.append("&" + URLEncoder.encode("dataType","UTF-8") + "=" + URLEncoder.encode("JSON", "UTF-8")); /*요청자료형식(XML/JSON)Default: XML*/
        
        Calendar cal = Calendar.getInstance(Locale.KOREA);
//        System.out.println(cal);
//        System.out.println(cal.get(cal.YEAR));
//        System.out.println(cal.get(cal.MONTH) + 1);
//        System.out.println(cal.get(cal.DATE));
//        System.out.println(cal.get(cal.HOUR_OF_DAY));
        
        String year = String.valueOf(cal.get(cal.YEAR));
        String month = String.valueOf(cal.get(cal.MONTH) + 1);
        String day = String.valueOf(cal.get(cal.DATE));
//        String time = String.valueOf(cal.get(cal.HOUR_OF_DAY)); 
        
//        System.out.println(year + ((month.length() < 2)? "0" + month : month) + day);
//        System.out.println((time.length() < 2 ? "0" + time : time) + "00");
        
        urlBuilder.append("&" + URLEncoder.encode("base_date","UTF-8") + "=" + URLEncoder.encode(year + ((month.length() < 2)? "0" + month : month) + day, "UTF-8")); /*현재날짜(20200810) 발표*/
//        urlBuilder.append("&" + URLEncoder.encode("base_time","UTF-8") + "=" + URLEncoder.encode((time.length() < 2 ? "0" + time : time) + "00", "UTF-8")); /*현재시각 발표(정시단위)*/
        urlBuilder.append("&" + URLEncoder.encode("base_time","UTF-8") + "=" + URLEncoder.encode("1750", "UTF-8")); /*현재시각 발표(정시단위)*/
        urlBuilder.append("&" + URLEncoder.encode("nx","UTF-8") + "=" + URLEncoder.encode("61", "UTF-8")); /*예보지점의 X 좌표값*/
        urlBuilder.append("&" + URLEncoder.encode("ny","UTF-8") + "=" + URLEncoder.encode("129", "UTF-8")); /*예보지점 Y 좌표*/
       
        System.out.println(urlBuilder.toString());
        
        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        System.out.println("Response code: " + conn.getResponseCode());
        BufferedReader rd;
        if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();
        System.out.println(sb.toString());
        
        return sb.toString();
	}
}
```

##### Schedule 기능 추가

```bash
package com.ssafy.jara.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

@Configuration
public class ScheduleConfig implements SchedulingConfigurer {

	@Override
	public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
		ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();

        threadPoolTaskScheduler.setPoolSize(10);
        threadPoolTaskScheduler.setThreadNamePrefix("my-scheduled-task-pool-");
        threadPoolTaskScheduler.initialize();

        taskRegistrar.setTaskScheduler(threadPoolTaskScheduler);
	}

}
```

```bash
package com.ssafy.jara.common.weather;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class WeatherServer extends SpringBootServletInitializer {
	public static void main(String[] args) {
		SpringApplication.run(WeatherServer.class, args);
	}
}
```



### :sweat_smile: 어려웠던 점

##### 공공데이터포털에서 Service Key를 UTF-8로 인코딩한 값을 줘서 SERVICE ERROR SERVICE_KEY_IS_NOT_REGISTERED_ERROR 30 발생

##### api 호출 시, 위도/경도 값이 격자 위도/경도라서 기존 Location 테이블 위도/경도 값과 맞지 않음

##### Spring boot Schedule에 대한 구현 경험 부족

-----



### :feet: 20.08.11

##### 기상청 api에서 위도/경도와 시간에 맞는 강수형태, 하늘상태, 기온 정보 추출해서 Location 테이블 수정

```bash
package com.ssafy.jara.common.weather;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.ssafy.jara.dao.WeatherDao;
import com.ssafy.jara.dto.Location;

@Component
public class WeatherService {

	protected static Log log = LogFactory.getLog(WeatherService.class);
	
	@Autowired
	WeatherDao weatherDao;
	
	// 강수형태(PTY) 코드 : 없음(0), 비(1), 비/눈(2), 눈(3), 소나기(4), 빗방울(5), 빗방울/눈날림(6), 눈날림(7)
	private static String[] PTYCode = new String[] {"없음", "비", "비/눈", "눈", "소나기", "빗방울", "빗방울/눈날림", "눈날림"};
	
	// 하늘상태(SKY) 코드 : 맑음(1), 구름많음(3), 흐림(4) 
	private static String[] SKYCode = new String[] {"", "맑음", "", "구름많음", "흐림"};
	
	// 0시 6시 12시 18시에 자동 실행
	@Scheduled(cron = "0 0 0,6,12,18 * * *")
	public void WeatherScheduled() throws IOException, ParseException {
		log.info("Weather 정보");
		
		Calendar cal = Calendar.getInstance(Locale.KOREA);
        
        String year = String.valueOf(cal.get(cal.YEAR));
        String month = String.valueOf(cal.get(cal.MONTH) + 1);
        String day = String.valueOf(cal.get(cal.DATE));
        String time = String.valueOf(cal.get(cal.HOUR_OF_DAY) - 1); // 해당 시간 예보 검색을 위한 (현재 시간 - 1)
        String originTime = String.valueOf(cal.get(cal.HOUR_OF_DAY)); // 현재 시간
        
        String base_date = year + ((month.length() < 2)? "0" + month : month) + day; // (200811 201225 형태)
        String base_time = (time.length() < 2 ? "0" + time : time) + "00"; // 현재 시간 - 1 기준 (0900 1200 형태) 
        String orgTime = (originTime.length() < 2 ? "0" + originTime : originTime) + "00"; // 현재 시간 기준
		
		List<Location> locationList = (List<Location>) weatherDao.selectLocation(); // Location에 저장되어 있는 구 정보 받아옴
		
		for (int i = 0; i < locationList.size(); i++) {
			Location location = locationList.get(i);
			
			// 받아온 기상 정보를 JSON parsing을 통해 원하는 시간의 원하는 정보(강수형태, 하늘상태, 기온)만 추출
			jsonParse(apiConnection(base_date, base_time, location.getNx(), location.getNy()), location.getName(), orgTime);
		}
	}

	// 발표날짜, 발표시각, 격자위도, 격자경도로 기상청 api와 통신하여 해당 위치의 기상 정보를 JSON으로 받아옴
	public String apiConnection(String base_date, String base_time, String nx, String ny) throws IOException {
		StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1360000/VilageFcstInfoService/getUltraSrtFcst"); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("ServiceKey","UTF-8") + "=" + "DysdYWgqnU55g9Q7PoDso1H3%2BQJFLffwk4YkWWCS4cSluMp9qKnSyq0J0u1PXFITKErf1yqWK%2FrwEUKlHTVePw%3D%3D"); /*공공데이터포털에서 받은 인증키*/
        urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지번호*/
        urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("100", "UTF-8")); /*한 페이지 결과 수*/
        urlBuilder.append("&" + URLEncoder.encode("dataType","UTF-8") + "=" + URLEncoder.encode("JSON", "UTF-8")); /*요청자료형식(XML/JSON)Default: XML*/
        
        urlBuilder.append("&" + URLEncoder.encode("base_date","UTF-8") + "=" + URLEncoder.encode(base_date, "UTF-8")); /*현재날짜(20200810) 발표*/
        urlBuilder.append("&" + URLEncoder.encode("base_time","UTF-8") + "=" + URLEncoder.encode(base_time, "UTF-8")); /*현재시각 발표(정시단위)*/
        urlBuilder.append("&" + URLEncoder.encode("nx","UTF-8") + "=" + URLEncoder.encode(nx, "UTF-8")); /*예보지점의 X 좌표값*/
        urlBuilder.append("&" + URLEncoder.encode("ny","UTF-8") + "=" + URLEncoder.encode(ny, "UTF-8")); /*예보지점 Y 좌표*/
       
        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        
        BufferedReader rd;
        
        if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
        
        StringBuilder sb = new StringBuilder();
        
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();
        
		return sb.toString();
	}
	
	
	// JSON에서 원하는 시간의 원하는 정보(강수형태, 하늘상태, 기온)을 추출하여 해당 구(name) 테이블의 기상 정보(컬럼 PTY, SKY, T1H) 수정
	public void jsonParse(String jsonData, String name, String orgTime) throws ParseException {
		
		Location location = new Location();
		
		JSONParser jsonParser = new JSONParser();
		JSONObject jsonObject = (JSONObject)jsonParser.parse(jsonData);
		JSONObject parse_response = (JSONObject)jsonObject.get("response");
		JSONObject parse_body = (JSONObject)parse_response.get("body");
		JSONObject parse_items = (JSONObject)parse_body.get("items");
		JSONArray parse_item = (JSONArray)parse_items.get("item");
		
		JSONObject weather;
		
		for (int i = 0; i < parse_item.size(); i++) {
			weather = (JSONObject)parse_item.get(i);
			
			if(weather.get("category").equals("PTY") && weather.get("fcstTime").equals(orgTime)) {
				log.info(weather.get("category") + " / " + weather.get("fcstTime") + " / " + weather.get("fcstValue"));
				location.setPTY(PTYCode[Integer.parseInt((String) weather.get("fcstValue"))]); // PTY 정보 추출
			}
			
			if(weather.get("category").equals("SKY") && weather.get("fcstTime").equals(orgTime)) {
				log.info(weather.get("category") + " / " + weather.get("fcstTime") + " / " + weather.get("fcstValue"));
				location.setSKY(SKYCode[Integer.parseInt((String)weather.get("fcstValue"))]); // SKY 정보 추출
			}
			
			if(weather.get("category").equals("T1H") && weather.get("fcstTime").equals(orgTime)) {
				log.info(weather.get("category") + " / " + weather.get("fcstTime") + " / " + weather.get("fcstValue"));
				location.setT1H((String) weather.get("fcstValue")); // T1H 정보 추출
			}
		}
		
		location.setName(name);
		
		weatherDao.updateLocationWeather(location);
	}
}
```

##### Tips Top 5 목록 기능 구현

-----



### :feet: 20.08.12

##### Location 테이블이 업데이트가 제대로 됬는지 확인되지 않아서 updated_at 컬럼을 추가하여 수정일 표시

##### Spring Boot에서 WebSocket을 사용해 실시간 채팅 1차 구현

>pom.xml

```bash
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-websocket</artifactId>
</dependency>

<!-- View JSP-->
<dependency>
	<groupId>javax.servlet</groupId>
	<artifactId>jstl</artifactId>
</dependency>
<dependency>
	<groupId>org.apache.tomcat.embed</groupId>
	<artifactId>tomcat-embed-jasper</artifactId>
	<scope>provided</scope>
</dependency>

<!-- Json Simple -->
<dependency>
	<groupId>com.googlecode.json-simple</groupId>
	<artifactId>json-simple</artifactId>
	<version>1.1.1</version>		
</dependency>
```

> application.properties

```bash
# Tomcat Server Port
server.port=80

# JSP, HTML ModelAndView Path
spring.mvc.view.prefix=/WEB-INF/view/
spring.mvc.view.suffix=.jsp

# JSP to Modify Not Restart Server
server.servlet.jsp.init-parameters.development=true
```

> ChatController.java

```bash
package com.chat.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ChatController {

	@RequestMapping("/chat")
	public ModelAndView chat() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("chat");
		
		return mv;
	}
	
}
```

> SocketHandler.java

```bash
package com.chat.handler;

import java.util.HashMap;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class SocketHandler extends TextWebSocketHandler {
	// TextWebSocketHandler는 handleTextMessage를 실행시킴
	// 메시지 타입에 따라서 handleBinaryMessage 또는 handleTextMessage 실행

	HashMap<String, WebSocketSession> sessionMap = new HashMap<>(); // 웹소켓 세션을 담아둘 맵
	
	// 메시지 발송 시 실행
	@Override
	public void handleTextMessage(WebSocketSession session, TextMessage message) {
		String msg = message.getPayload(); // message.getPayload()로 메시지 전송받음
		JSONObject obj = JsonToObjectParser(msg); // JSON형태로 전송된 메시지 파싱
		
		for(String key : sessionMap.keySet()) {
			WebSocketSession wss = sessionMap.get(key);
			try {
				wss.sendMessage(new TextMessage(obj.toJSONString()));
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	// 웹소켓 연결 시 동작
	@SuppressWarnings("unchecked")
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		super.afterConnectionEstablished(session);
		sessionMap.put(session.getId(), session);
		
		JSONObject obj = new JSONObject();
		obj.put("type", "getId"); // 발신메시지의 타입
		obj.put("sessionId", session.getId()); // 생성된 세션 Id
		
		session.sendMessage(new TextMessage(obj.toJSONString()));
	}
	
	// 웹소켓 종료 시 동작
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		sessionMap.remove(session.getId());
		super.afterConnectionClosed(session, status);
	}
	
	// JSON 파싱
	private static JSONObject JsonToObjectParser(String jsonStr) {
		JSONParser parser = new JSONParser();
		JSONObject obj = null;
		
		try {
			obj = (JSONObject) parser.parse(jsonStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return obj;
	}
}
```

> WebSocketConfig.java

```bash
package com.chat.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import com.chat.handler.SocketHandler;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
	// 생성한 구현체 등록
	
	@Autowired
	SocketHandler socketHandler;
	
	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry.addHandler(socketHandler, "/chating");
	}

}
```

> chat.jsp

```bash
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
<meta charset="UTF-8">
<title>Chat</title>
<style>
* {
	margin: 0;
	padding: 0;
}

.container {
	width: 500px;
	margin: 0 auto;
	padding: 25px
}

.container h1 {
	text-align: left;
	padding: 5px 5px 5px 15px;
	color: #FFBB00;
	border-left: 3px solid #FFBB00;
	margin-bottom: 20px;
}

.chating {
	background-color: #000;
	width: 500px;
	height: 500px;
	overflow: auto;
}

.chating p {
	color: #fff;
	text-align: left;
}

.chating .me {
	color: #F6F6F6;
	text-align: right;
}

.chating .others {
	color: #FFE400;
	text-align: left;
}

input {
	width: 330px;
	height: 25px;
}

#yourMsg {
	display: none;
}
</style>
</head>
<script type="text/javascript">
	var ws;

	function wsOpen() {
		ws = new WebSocket("ws://" + location.host + "/chating");
		wsEvt();
	}

	function wsEvt() {
		ws.onopen = function(data) {
			// 소켓이 열리면 동작(초기화 세팅)
		}

		ws.onmessage = function(data) {
			// 메시지 받으면 동작
			var msg = data.data;
			
			if (msg != null && msg.trim() != '') {
				var d = JSON.parse(msg);
				
				if (d.type == "getId") {
					var si = d.sessionId != null ? d.sessionId : "";

					if (si != '') {
						$("#sessionId").val(si);
					}
					
				} else if (d.type == "message") {
					if (d.sessionId == $("#sessionId").val()) {
						$("#chating").append("<p class='me'>나 :" + d.msg + "</p>");
					} else {
						$("#chating").append("<p class='others'>" + d.userName + " :" + d.msg + "</p>");
					}
					
				} else {
					console.warn("unknown type!")
				}
				/* $("#chating").append("<p>" + msg + "</p>"); */
			}
		}

		document.addEventListener("keypress", function(e) {
			if (e.keyCode == 13) { //enter press
				send();
			}
		});
	}

	function chatName() {
		var userName = $("#userName").val();
		if (userName == null || userName.trim() == "") {
			alert("사용자 이름을 입력해주세요.");
			$("#userName").focus();
		} else {
			wsOpen();
			$("#yourName").hide();
			$("#yourMsg").show();
		}
	}

	function send() {
		/* var uN = $("#userName").val();
		var msg = $("#chatting").val();
		ws.send(uN + " : " + msg);
		$('#chatting').val(""); */

		var option = {
			type : "message",
			sessionId : $("#sessionId").val(),
			userName : $("#userName").val(),
			msg : $("#chatting").val()
		}
		ws.send(JSON.stringify(option))
		$('#chatting').val("");
	}
</script>
<body>
	<div id="container" class="container">
		<h1>채팅</h1>
		<input type="hidden" id="sessionId" value="">
		
		<div id="chating" class="chating"></div>

		<div id="yourName">
			<table class="inputTable">
				<tr>
					<th>사용자명</th>
					<th><input type="text" name="userName" id="userName"></th>
					<th><button onclick="chatName()" id="startBtn">이름 등록</button></th>
				</tr>
			</table>
		</div>
		<div id="yourMsg">
			<table class="inputTable">
				<tr>
					<th>메시지</th>
					<th><input id="chatting" placeholder="보내실 메시지를 입력하세요."></th>
					<th><button onclick="send()" id="sendBtn">보내기</button></th>
				</tr>
			</table>
		</div>
	</div>
</body>
</html>
```

[참고] https://myhappyman.tistory.com/100

-----



### :feet: 20.08.13

##### Tips 이미지 업로드 기능 구현

##### Spring Boot와 Vue.js를 연동해서 소켓 통신 실시간 채팅 구현

> WebSocketConfig.java

```b
package com.ssafy.jara.common.chating;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocket
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
	// 생성한 구현체 등록
	
	// Vue.js로 구현한 채팅
	// 클라이언트가 메시지를 구독할 endpoint 정의
	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		registry.enableSimpleBroker("/send");
	}
	
	// Connection을 맺을 때, CORS 허용
	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/").setAllowedOrigins("*").withSockJS();
	}

}
```

> Socket.java

```b
package com.ssafy.jara.common.chating;

public class Socket {
	private String userName;
	private String content;

	public Socket() {}

	public Socket(String userName, String content) {
		super();
		this.userName = userName;
		this.content = content;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "Socket [userName=" + userName + ", content=" + content + "]";
	}

}
```

> SocketHandler

```b
package com.ssafy.jara.common.chating;

import java.util.HashMap;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class SocketHandler extends TextWebSocketHandler {
	// TextWebSocketHandler는 handleTextMessage를 실행시킴
	// 메시지 타입에 따라서 handleBinaryMessage 또는 handleTextMessage 실행

	HashMap<String, WebSocketSession> sessionMap = new HashMap<>(); // 웹소켓 세션을 담아둘 맵
	
	// 메시지 발송 시 실행
	@Override
	public void handleTextMessage(WebSocketSession session, TextMessage message) {
		String msg = message.getPayload(); // message.getPayload()로 메시지 전송받음
		JSONObject obj = JsonToObjectParser(msg); // JSON형태로 전송된 메시지 파싱
		
		for(String key : sessionMap.keySet()) {
			WebSocketSession wss = sessionMap.get(key);
			try {
				wss.sendMessage(new TextMessage(obj.toJSONString()));
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	// 웹소켓 연결 시 동작
	@SuppressWarnings("unchecked")
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		super.afterConnectionEstablished(session);
		sessionMap.put(session.getId(), session);
		
		JSONObject obj = new JSONObject();
		obj.put("type", "getId"); // 발신메시지의 타입
		obj.put("sessionId", session.getId()); // 생성된 세션 Id
		
		session.sendMessage(new TextMessage(obj.toJSONString()));
	}
	
	// 웹소켓 종료 시 동작
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		sessionMap.remove(session.getId());
		super.afterConnectionClosed(session, status);
	}
	
	// JSON 파싱
	private static JSONObject JsonToObjectParser(String jsonStr) {
		JSONParser parser = new JSONParser();
		JSONObject obj = null;
		
		try {
			obj = (JSONObject) parser.parse(jsonStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return obj;
	}
}
```

> SocketController.java

```b
package com.ssafy.jara.common.chating;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class SocketController {

	@MessageMapping("/receive")
	@SendTo("/send")
	
	public Socket SocketHandler(Socket socket) {
		String userName = socket.getUserName();
		String content = socket.getContent();
		
		Socket result = new Socket(userName, content);
		
		return result;
	}
}
```

> App.vue

```bash
<template>
  <div id="container">
    <div id="app">
      유저이름 :
      <input
        v-model="userName"
        type="text"
      >
      내용 : 
      <input
        v-model="message"
        type="text"
        @keyup="sendMessage"
      >
      <div id="dialog"
        v-for="(item, idx) in recvList"
        :key="idx"
      >
        <div id="line">
          <h6 v-if="item.userName == userName" id="me">{{item.userName}}  {{item.content}}</h6>
          <h6 v-else id="other">{{item.userName}}  {{item.content}}</h6>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import Stomp from 'webstomp-client'
import SockJS from 'sockjs-client'

export default {
  name: 'App',
  data() {
    return {
      userName: "",
      message: "",
      recvList: [],
      me: ""
    }
  },
  created() {
    // App.vue 생성되면 소켓 연결 시도
    this.connect()
  },
  methods: {
    sendMessage(e) {
      if(e.keyCode === 13 && this.userName !== '' && this.message !== '') {
        this.send()
        this.message = ''
      }
    },
    send() {
      console.log("Send message: " + this.message)

      if(this.stompClient && this.stompClient.connected) {
        const msg = {
          userName: this.userName,
          content: this.message
        };
        this.stompClient.send("/receive", JSON.stringify(msg), {});
      }
    },
    connect() {
      const serverURL = "http://localhost:8081"
      let socket = new SockJS(serverURL);
      this.stompClient = Stomp.over(socket);

      console.log('소켓 연결 시도. 서버 주소: ${serverURL}')
      
      this.stompClient.connect (
        {},
        frame => {
          this.connected  = true;

          console.log('소켓 연결 성공', frame);
          
          // 서버 메시지 전송 endpoint 구독
          // pub sub 구조
          this.stompClient.subscribe("/send", res => {
            console.log('구독으로 받은 메시지', res.body);
            
            this.recvList.push(JSON.parse(res.body))
          });
        },
        error => {
          // 소켓 연결 실패
          console.log('소켓 연결 실패', error);
          this.connected = false;
        }
      );
    }
  }  
}
</script>

<style>
#container {
  width: auto;
  height: auto;
}

#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}

#dialog {
  width: 100%;
  height: auto;
  margin: 0 auto;
}

#line {
  width: 100%;
  height: 20px;
  float: none;
}

h6 {
  padding: 0;
  margin: 0;
}

h6#me {
  float: right;
  width: 40%;
}

h6#other {
  float: left;
  width: 40%;
}

#nav {
  padding: 30px;
}

#nav a {
  font-weight: bold;
  color: #2c3e50;
}

#nav a.router-link-exact-active {
  color: #42b983;
}
</style>

```

-----



### :feet: 20.08.15

##### Tips 태그마다 다른 자라 기본 이미지 설정

| 요리 자라                                                    | 세탁 자라                                                    | 청소 자라                                                    | 보관 자라                                                    |
| ------------------------------------------------------------ | ------------------------------------------------------------ | ------------------------------------------------------------ | ------------------------------------------------------------ |
| ![KakaoTalk_20200813_150605789](C:\Users\multicampus\Documents\카카오톡 받은 파일\KakaoTalk_20200813_150605789.png) | ![KakaoTalk_20200813_150605608](C:\Users\multicampus\Documents\카카오톡 받은 파일\KakaoTalk_20200813_150605608.png) | ![KakaoTalk_20200813_150605423](C:\Users\multicampus\Documents\카카오톡 받은 파일\KakaoTalk_20200813_150605423.png) | ![KakaoTalk_20200813_150605270](C:\Users\multicampus\Documents\카카오톡 받은 파일\KakaoTalk_20200813_150605270.png) |

##### Accounts 프로필 이미지 기능 추가

##### 네이버 로그인 (네아로) api 이용 소셜 로그인 구현

```b
@ApiOperation(value = "네이버 로그인")
	@GetMapping("/signin/naver")
	private ResponseEntity<String> loginNaver() throws UnsupportedEncodingException {
		String clientId = "y_9J6LuNu9tyN5tgnmEN";
//		String redirectURI = URLEncoder.encode("https://i3a308.p.ssafy.io/accounts/signin/naver/access", "UTF-8");
		String redirectURI = URLEncoder.encode("http://localhost:8081/jara/accounts/signin/naver/access", "UTF-8");
		SecureRandom random = new SecureRandom();
		String state = new BigInteger(130, random).toString();
		String apiURL = "https://nid.naver.com/oauth2.0/authorize?response_type=code";
		apiURL += "&client_id=" + clientId;
		apiURL += "&redirect_uri=" + redirectURI;
		apiURL += "&state=" + state;
		
		System.out.println(apiURL);
		
		return new ResponseEntity<String>(apiURL, HttpStatus.OK);
	}
	
	@ApiOperation(value = "네이버 로그인 접근 토큰")
	@GetMapping("/signin/naver/access")
	private ResponseEntity<Account> accessTokenNaver(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		String clientId = "y_9J6LuNu9tyN5tgnmEN";// 애플리케이션 클라이언트 아이디값";
		String clientSecret = "8bMro7T5Dt";// 애플리케이션 클라이언트 시크릿값";
		String code = request.getParameter("code");
		String state = request.getParameter("state");
//		String redirectURI = URLEncoder.encode("https://i3a308.p.ssafy.io/accounts/signin/naver", "UTF-8");
		String redirectURI = URLEncoder.encode("http://localhost:8081/jara/signin/naver", "UTF-8");
		String apiURL;
		apiURL = "https://nid.naver.com/oauth2.0/token?grant_type=authorization_code&";
		apiURL += "client_id=" + clientId;
		apiURL += "&client_secret=" + clientSecret;
		apiURL += "&redirect_uri=" + redirectURI;
		apiURL += "&code=" + code;
		apiURL += "&state=" + state;
		
		String access_token = "";
		String refresh_token = "";
		
		try {
			URL url = new URL(apiURL);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			int responseCode = con.getResponseCode();
			BufferedReader br;
			
			System.out.println("responseCode=" + responseCode);
			
			if (responseCode == 200) { // 정상 호출
				br = new BufferedReader(new InputStreamReader(con.getInputStream()));
			} else { // 에러 발생
				br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
			}
			
			String inputLine;
			StringBuffer res = new StringBuffer();
			
			while ((inputLine = br.readLine()) != null) {
				res.append(inputLine);
			}
			br.close();
			
			if (responseCode == 200) {
				JSONParser parser = new JSONParser();
				JSONObject obj = (JSONObject) parser.parse(res.toString());
				access_token = (String) obj.get("access_token");
				refresh_token = (String) obj.get("refresh_token");
				
				// 회원 프로필 조회
				String header = "Bearer " + access_token; // Bearer 다음에 공백 추가

				apiURL = "https://openapi.naver.com/v1/nid/me";

				Map<String, String> requestHeaders = new HashMap<>();
				requestHeaders.put("Authorization", header);
				String responseBody = get(apiURL, requestHeaders);

				JSONObject resBody = (JSONObject)parser.parse(responseBody);
				JSONObject resObj = (JSONObject) resBody.get("response");
				
				String nickname = URLDecoder.decode((String) resObj.get("nickname"));
				String sex = (String) resObj.get("gender");
				String email = (String) resObj.get("email");
				String birthday = (String) resObj.get("birthday");
				
				System.out.println(nickname + " " + sex + " " + email + " " + birthday);
				
				if(accountService.findEmail(email) > 0) {
					Account account = accountService.findPartAccount(accountService.findIdByEmail(email));
					
					if(!account.equals(null)) {
						String token = jwtService.create(account);
						response.setHeader("jwt-auth-token", token);
					}
					
					return new ResponseEntity<Account>(account, HttpStatus.OK); // 처음 소셜 로그인 사용자가 아닌 경우
				} 
				
				Account account = new Account();
				account.setNickname(nickname);
				account.setSex(sex.equals("F") ? true : false);
				account.setEmail(email);
				account.setAccess_token(access_token);
				account.setRefresh_token(refresh_token);
				
				accountService.insertNaverAccount(account);
				
				Account resultAccount = accountService.findPartAccount(account.getId());
				
				return new ResponseEntity<Account>(resultAccount, HttpStatus.OK); // 처음 소셜 로그인 사용자인 경우 -> 지역 입력 페이지로 가야한다!!!!!!
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		
		return new ResponseEntity<Account>(HttpStatus.BAD_REQUEST);
	}
	
	@ApiOperation(value = "네이버 로그인으로 회원가입 시 주소 수정")
	@PutMapping("/signin/naver/access")
	private ResponseEntity<Account> updateNaverAccount(Account account) {
		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		hashMap.put("id", account.getId());
		hashMap.put("location", account.getLocation());
		
		if(accountService.updateNaverAccount(hashMap) > 0) {
			return new ResponseEntity<Account>(accountService.findPartAccount(account.getId()), HttpStatus.OK);
		}
		
		return new ResponseEntity<Account>(HttpStatus.NO_CONTENT);
	}
	
	
	/* 네이버 회원 프로필 조회에 필요한 메서드 */
	private static String get(String apiUrl, Map<String, String> requestHeaders) {
		HttpURLConnection con = connect(apiUrl);
		try {
			con.setRequestMethod("GET");
			for (Map.Entry<String, String> header : requestHeaders.entrySet()) {
				con.setRequestProperty(header.getKey(), header.getValue());
			}

			int responseCode = con.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) { // 정상 호출
				return readBody(con.getInputStream());
			} else { // 에러 발생
				return readBody(con.getErrorStream());
			}
		} catch (IOException e) {
			throw new RuntimeException("API 요청과 응답 실패", e);
		} finally {
			con.disconnect();
		}
	}

	private static HttpURLConnection connect(String apiUrl) {
		try {
			URL url = new URL(apiUrl);
			return (HttpURLConnection) url.openConnection();
		} catch (MalformedURLException e) {
			throw new RuntimeException("API URL이 잘못되었습니다. : " + apiUrl, e);
		} catch (IOException e) {
			throw new RuntimeException("연결이 실패했습니다. : " + apiUrl, e);
		}
	}

	private static String readBody(InputStream body) {
		InputStreamReader streamReader = new InputStreamReader(body);

		try (BufferedReader lineReader = new BufferedReader(streamReader)) {
			StringBuilder responseBody = new StringBuilder();

			String line;
			while ((line = lineReader.readLine()) != null) {
				responseBody.append(line);
			}

			return responseBody.toString();
		} catch (IOException e) {
			throw new RuntimeException("API 응답을 읽는데 실패했습니다.", e);
		}
	}
```

-----



### :feet: 20.08.15 ~ 20.08.16

##### 네이버 소셜 로그인

> 백과 프론트 연동 성공
>
> 네아로 검수 요청
>
> AWS 서버에 올려서 연동 성공

##### 실시간 채팅

> 백과 프론트 소켓 통신 성공
>
> AWS 서버에 올려서 통신 성공
>
> Swagger와 충돌나던 SocketConfig 문제 해결

```b
package com.ssafy.jara.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@EnableWebSocketMessageBroker
public class SwaggerSocketConfig implements WebSocketMessageBrokerConfigurer {

	@Bean
	public Docket postsApi() {
		return new Docket(DocumentationType.SWAGGER_2)
				.groupName("SSAFY_JARA")
				.apiInfo(apiInfo())
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.ssafy.jara.controller"))
				.paths(PathSelectors.ant("/**"))
				.build();
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title("SSAFY_JARA API")
				.description("SSAFY_JARA API Reference for Developers")
				.termsOfServiceUrl("https://edu.ssafy.com")
				.license("SSAFY_JARA License")
				.licenseUrl("ssafy_jara@ssafy.com").version("1.0").build();
	}
	
	// Vue.js로 구현한 채팅
	// 클라이언트가 메시지를 구독할 endpoint 정의
	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		registry.enableSimpleBroker("/send");
	}

	// Connection을 맺을 때, CORS 허용
	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/jara").setAllowedOrigins("*").withSockJS();
	}
}
```



##### Tips 전체 조회 시, 댓글 목록도 함께 조회

-----



### :feet: 20.08.17

##### Tips Top5 조회 시 댓글 목록, 스크랩 사용자 목록, 댓글수, 스크랩수 함께 조회

-----



### :feet: 20.08.18

##### UCC 시나리오 회의

> 어떤 느낌으로 촬영하면 좋을지 여러 앱 광고를 찾아봄
>
> - 현대카드 - 현대카드 앱
> - 시부랄필름 - 변호사 찾아주는 앱
>
> 다른 팀원이 찾아온 앱 광고
>
> - 굿닥 앱(택시)
> - Apple Don't blink
>
> 결론은 현대카드 + Apple
>
> - 앱 이름이 '자라'인 이유 소개
> - 주요 기능에 대해 필요성 및 구현 화면 소개
> - 끄트머리에 SNS 기본 기능(게시글, 채팅) 소개

##### 실시간 채팅에 참여한 사용자수, 입장 및 퇴장 메시지를 설정하려고 했으나 소켓을 제대로 이해하지 못한 상태로 기능 구현을 하여서 구현하지 못함

##### 빌드된 서버에서 기능 테스트 및 데이터 추가(돌아다니면서 댓글 달거나, 좋아요, 스크랩)



### :sweat_smile: 어려웠던 점

##### 소켓을 모르겠다. 구글링을 통해 블로그마다 어떻게 구현하였는지 정보를 수집하였으나, 비슷한듯 다르고 사용 기술 스펙이 달라서 JS를 완벽 숙지하지 못한 나에게는 어려웠다

##### JS 공부해야겠다.

-----



### :feet: 20.08.19

##### API, Method 사용하지 않는 코드 삭제 및 주석 정리

##### API 문서 정리 및 Swagger 정보 정리

##### UCC 1차 영상에 대한 회의

##### 발표 PPT 준비

> ##### DB ERD
>
> ![jara ERD](C:\Users\multicampus\Desktop\jara\jara ERD.png)
>
> ##### 간트차트
>
> ![image-20200819182414873](C:\Users\multicampus\AppData\Roaming\Typora\typora-user-images\image-20200819182414873.png)

-----



### :feet: 20.08.20

##### 발표 PPT 확인

##### 컨설턴트님, 코치님과 14시 50분에 팀미팅

> ###### AWS 시연
>
> ###### 어려웠던 점, 소감 발표

##### 팀원들과 마지막 소소한 이야기 나눔

-----



### :feet: 20.08.21

##### 공통 프로젝트 마지막 날

##### 최종 UCC, PPT 확인

------



#### :happy: 열정넘치고 배려 깊은 팀원들을 만나서 행복했던 프로젝트 끝 :happy: 

