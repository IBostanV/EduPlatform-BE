package com.play.quiz.service.impl;

import com.play.quiz.domain.UserOccupation;
import com.play.quiz.dto.UserOccupationDto;
import com.play.quiz.mapper.UserOccupationMapper;
import com.play.quiz.repository.UserOccupationRepository;
import com.play.quiz.service.UserOccupationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserOccupationServiceImpl implements UserOccupationService {

    private final UserOccupationMapper userOccupationMapper;
    private final UserOccupationRepository userOccupationRepository;

    public List<UserOccupationDto> getAllOccupations() {
        List<UserOccupation> userOccupationList = userOccupationRepository.findAll(Sort.by("name"));
        return userOccupationMapper.toDtoList(userOccupationList);
    }
}
