package com.mumukji.service;

import com.mumukji.entity.User;
import com.mumukji.repository.UserRepository;
import com.mumukji.util.UserPreferenceMapper;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import java.util.Map;
@Transactional
@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserPreferenceMapper userPreferenceMapper;

    public UserService(UserRepository userRepository, UserPreferenceMapper userPreferenceMapper) {
        this.userRepository = userRepository;
        this.userPreferenceMapper = userPreferenceMapper;
    }

    /**
     * 세션의 응답 데이터를 바탕으로 유저 선호 속성 업데이트
     */
    public void updateUserPreference(String userId, Map<String, Integer> responses) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다: " + userId));
        
        userPreferenceMapper.applyPreferenceToUser(user, responses);
        userRepository.save(user);
    }
}
