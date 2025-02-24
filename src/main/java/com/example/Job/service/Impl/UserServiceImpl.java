package com.example.Job.service.Impl;

import com.example.Job.dto.UserDto;
import com.example.Job.entity.User;
import com.example.Job.exception.ResourceNotFoundException;
import com.example.Job.repository.UserRepository;
import com.example.Job.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    UserRepository userRepository;
    ModelMapper modelMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }


    @Override
    public UserDto updateUser(UserDto userDto, long id) {
        User user = userRepository.findById( id).orElseThrow(()-> new ResourceNotFoundException("User", "id", id));

        user.setName(userDto.getName());
        user.setBirthday(userDto.getBirthday());
        user.setGender(userDto.getGender());

        UserDto responseUser = modelMapper.map( userRepository.save(user),UserDto.class );

        return responseUser;
    }

    @Override
    public void deleteUser(long id) {
        User user = userRepository.findById( id).orElseThrow(()-> new ResourceNotFoundException("User", "id", id));

        userRepository.delete(user);

    }

    @Override
    public UserDto getUserById(long id) {
        User user = userRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("User", "id", id));

        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
    }

    @Override
    public void updateUserToken(String token, String email) {
        User user = getUserByEmail(email);
        if(user != null){
//            user.setRefreshToken(token);
            userRepository.save(user);
        }
    }
}
