package com.example.moviemate.user.service;

import com.example.moviemate.user.dto.UpdateUserDto;
import com.example.moviemate.user.dto.UpdateUserResponse;
import com.example.moviemate.user.dto.UserDto;

public interface UserService {

  UserDto userInfoInquiry(String username);

  UpdateUserResponse updateUserInfo(UpdateUserDto updateUserDto, String username);



}
