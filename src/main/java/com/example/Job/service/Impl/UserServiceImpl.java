package com.example.Job.service.Impl;

import com.example.Job.models.dtos.UserDto;
import com.example.Job.entity.User;
import com.example.Job.exception.ResourceNotFoundException;
import com.example.Job.repository.UserRepository;
import com.example.Job.service.IUserService;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Service
public class UserServiceImpl implements IUserService {
    UserRepository userRepository;
    ModelMapper modelMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserDto updateUser(UserDto userDto, long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User", "id", id));

        user.setName(userDto.getName());
        user.setBirthday(userDto.getBirthday());
        user.setGender(userDto.getGender());

        UserDto responseUser = modelMapper.map(userRepository.save(user), UserDto.class);

        return responseUser;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteUser(long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User", "id", id));

        userRepository.delete(user);

    }

    @Override
    public UserDto getUserById(long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User", "id", id));

        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
    }

    @Override
    public User createUser(User newUser) {
        return userRepository.save(newUser);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUserToken(String token, String email) {
        User user = getUserByEmail(email);
        if (user != null) {
            // user.setRefreshToken(token);
            userRepository.save(user);
        }
    }

    @Override
    public List<UserDto> getUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(user -> modelMapper.map(user, UserDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<UserDto> getUsersByPage(int currentPage, int pageSize) {
        Pageable pageable = PageRequest.of(currentPage - 1, pageSize, Sort.by("id").ascending());

        // Lấy danh sách phân trang từ repository
        Page<User> userPage = userRepository.findAll(pageable);

        // Chuyển đổi từ Page<User> sang List<UserDto>
        return userPage.getContent().stream()
                .map(user -> modelMapper.map(user, UserDto.class))
                .collect(Collectors.toList());
    }
}
