package com.ktdsuniversity.edu.hello_spring.common.beans;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.ktdsuniversity.edu.hello_spring.common.vo.StoreResultVO;

/**
 * @Component => Spring Bean으로 생성해라.
 */
@Component
public class FileHandler {

	@Value("${app.multipart.base-dir}")
	private String baseDirectory;
	
	@Value("${app.multipart.obfuscation.enable}")
	private boolean enableObfuscation; // enable이 boolean 값이므로.
	
	@Value("${app.multipart.obfuscation.hide-ext.enable}")
	private boolean enableHideExtension;
	
	// upload MultipartFile
	public StoreResultVO storeFile(MultipartFile multipartFile) {

		// 1. 클라이언트가 파일을 전송했는지 확인
		// 2. 클라이언트가 파일을 전송했다면
		if (multipartFile != null && !multipartFile.isEmpty()) {
			// 3. 파일을 서버 컴퓨터의 특정 폴더로 저장시킨다.

			// 난독화 옵션이 꺼져 있는 경우 원본 파일명을 그대로 사용한다.
			String obfuscatedFileName = multipartFile.getOriginalFilename(); // 원래 파일의 이름을 넣어줌.(난독화 여부 - false 일 경우)
			
			// 난독화가 활성화된 경우, 파일명을 난독화
			if (this.enableObfuscation ) {
				
				// 확장자 추출 (마지막 '.' 이후의 문자열)
				String ext = obfuscatedFileName.substring(obfuscatedFileName.lastIndexOf("."));
				
				// 난독화된 파일명을 UUID로 설정 (obfuscation: true)
				obfuscatedFileName = UUID.randomUUID().toString();
				
				// 난독화는 하되 확장자는 유지
				if (! this.enableHideExtension) {
					obfuscatedFileName += ext;
				}
			} // 클라이언트에게 원본 파일명과 난독화된 파일명을 모두 제공하기 위해, 두 값을 포함한 VO 객체(StoreResultVO)를 반환한다.(하나의 메서드는 하나의 값만 반환)
			
			// 파일 인스턴스 생성. 사용자가 보내준 파일의 이름(A.txt)
			// 저장할 파일 객체를 생성. 경로는 설정된 baseDirectory와 난독화된 파일명으로 설정.
			File uploadFile = new File(this.baseDirectory, obfuscatedFileName);
			
			// 저장할 디렉토리가 존재하지 않으면, 디렉토리를 생성 (필요한 상위 디렉토리도 재귀적으로 생성).
			if (!uploadFile.getParentFile().exists()) {
				uploadFile.getParentFile().mkdirs(); 
			}
			
			// 파일을 지정된 위치에 저장.
			try {
				//transferTo 파일이 저장될 위치
				multipartFile.transferTo(uploadFile);
			} catch (IllegalStateException | IOException e) {
				// 용량이 가득참 -> IOException, 허가되지 않은 경로로 접근 -> IllegalStateException(윈도우의 경우 C드라이브에 접근할때:관리자권한 체크)
				// 이런 checkedException은 할 수 있는게 없음. 사용자에게 에러를 안내하는 것에서 마무리.
				e.printStackTrace();
				throw new RuntimeException("예기치 못한 이유로 업로드에 실패하였습니다. 잠시후 다시 시도해 주세요.");
			}
			// 결과값을 StoreResultVO로 반환한다.
			return new StoreResultVO(multipartFile.getOriginalFilename(), obfuscatedFileName);
		}
		// 파일이 비어 있거나 유효하지 않은 경우, null을 반환하여 업로드가 수행되지 않았음을 나타낸다.
		return null; 
	}
	
	// download fileName                                  난독화              실제파일 명
	public ResponseEntity<Resource> downloadFile(String fileName, String originFileName) {
		
		// 2. 다운로드 할 파일의 정보를 가지고 있는 파일 인스턴스를 생성한다. (난독화된 파일 이름)
		File downloadFile = new File(this.baseDirectory, fileName);
		
		// 3. http Header의 파일 다운로드 정보를 설정한다.
		HttpHeaders header = new HttpHeaders();
		
		// 다운로드시킬 파일 이름의 인코딩을 변경한다. (파일 다운로드에만 한해서 인코딩을 변경한다.)
		try {
			// 파일명 인코딩(UTF-8에서 ISO-8859-1로 변환 -> 최근에는 URLEncoder.encode 고려
			originFileName = new String(originFileName.getBytes("UTF-8"), "ISO-8859-1");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		// HTTP Response에 파일을 첨부해서 전송할 건데 파일의 이름은 "~~~"이다. (실제 파일 이름)
		// 파일 다운로드 헤더 설정
		header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + originFileName);
		
		// 4. 파일 리소스를 준비하고 브라우저에게 파일을 전송한다.
		InputStreamResource resource = null;
		
		try {
			// 파일의 스트림을 읽어서 바이트 단위로 브라우저에 전송한다.
			resource = new InputStreamResource(new FileInputStream(downloadFile));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new IllegalArgumentException("파일이 존재하지 않습니다.");
		}
		// 브라우저에게 보낼 응답 데이터를 생성한다.
		return ResponseEntity.ok() // status 200
				// 응답 데이터의 http header 정보를 셋팅한다. (파일 다운로드 정보)
				.headers(header)
				// 다운로드시킬 파일의 크기를 전달한다. -> 브라우저가 파일 다운로드 진행 상태를 관리하기 위해서.
				.contentLength(downloadFile.length())
				// 다운로드시킬 파일의 타입을 지정한다.
				// 보통 png 파일이라면 image/png 이렇게 지정하는데, 타입과 관계없이 강제 다운로드를 시키려면 "application/octet-stream"을 이용한다.
				.contentType(MediaType.parseMediaType("application/octet-stream"))
				// 파일을 응답데이터에 전달한다.
				.body(resource);
	}
	
	// delete fileName
	public void deleteFile(String fileName) {
		if (fileName == null || fileName.isEmpty()) {
			// 파일명이 Null 이거나 빈 문자열일 경우 예외 발생
			throw new IllegalArgumentException("유효하지 않은 파일명입니다.");
		}
		
		// 삭제할 파일의 인스턴스를 생성한다.
		File file = new File(this.baseDirectory, fileName);
		
		if (file.exists() && file.isFile()) {
			// 파일 삭제 성공 시 true 반환
			if (file.delete()) {
				System.out.println("파일이 성공적으로 삭제되었습니다: " + fileName);
			} else {
				throw new RuntimeException("파일 삭제에 실패하였습니다.");
			}
		}
	}
}