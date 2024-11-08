package com.ktdsuniversity.edu.hello_spring.common.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.validation.BindingResult;

public final class ErrorMapUtil {

	
	
	// 동시성 문제가 발생할 가능성 매우 높음. 왜? static이니까. (reply를 동시에 100명이 작성하고 있는데 2~3명이 content를 작성하지 않는다면 동시성 문제 발생)
	// Validation 체크가 동시에 이루어질 경우 데이터가 꼬일 수 있음.
	//private static Map<String, Object> errorMap; 
	
	public static Map<String, Object> getErrorMap(BindingResult bindingResult) {
		// 반환타입 Map<String, Object>
		
		Map<String, Object> errorMap = new HashMap<>();
		
		/*
		if (errorMap == null) {
			errorMap = new HashMap<>(); // 초기화
		}
		*/
		// getFieldErrors의 결과가 list
		bindingResult.getFieldErrors()
					 .forEach(error -> {
			String fieldName = error.getField();
			String errorMessage = error.getDefaultMessage();
			
			if (!errorMap.containsKey(fieldName)) {
				errorMap.put(fieldName, new ArrayList<>()); // content(키)를 넣고 리스트(밸류)를 넣고
			}

			List<String> errorList = (List<String>)errorMap.get(fieldName);
			errorList.add(errorMessage); // 리스트에 밸류를 쌓아준다. 하나의 키
		});
		// errorMap 반환
		return errorMap;
	}
	
//	public static Map<String, Object> getErrorMap() {
//		return errorMap;
//	}
}
