package com.mumukji.service;

import com.mumukji.entity.User;
import com.mumukji.exception.InvalidFormatException;
import com.mumukji.repository.UserRepository;
import com.mumukji.util.UserPreferenceMapper;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;
@Transactional
@Service
public class UserService {
	
	Set<String> tempId = Set.of(
		    "00000000", "12345678", "11111111", "22222222",
		    "33333333", "44444444", "55555555", "66666666",
		    "77777777", "88888888", "99999999"
		);

    private final UserRepository userRepository;
    private final UserPreferenceMapper userPreferenceMapper;

    public UserService(UserRepository userRepository, UserPreferenceMapper userPreferenceMapper) {
        this.userRepository = userRepository;
        this.userPreferenceMapper = userPreferenceMapper;
    }
    
    public void loginorregisterUser(String userId) {
    	isValidFormat(userId);
    	
    	if (tempId.contains(userId)) {
	        userId = "00000000";
	    }
	    if (!userRepository.existsByUserId(userId)) {
	        User newUser = new User();
	        newUser.setUserId(userId);
	        userRepository.save(newUser);
	    }
    }
    private void isValidFormat(String userId) {
    	if(!userId.matches("^\\d{8}$")) {
    		throw new InvalidFormatException("숫자 8자리를 입력해주세요");
    	}
    }
    
    public void updateUserPreference(String userId, Map<String, Integer> responses) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다: " + userId));
        
        userPreferenceMapper.applyPreferenceToUser(user, responses);
        userRepository.save(user);
    }
}
