package com.coresaken.mcserverlist.service;

import com.coresaken.mcserverlist.auth.dto.response.AuthenticationResponse;
import com.coresaken.mcserverlist.data.dto.ChangePasswordDto;
import com.coresaken.mcserverlist.data.response.Response;
import com.coresaken.mcserverlist.database.model.User;
import com.coresaken.mcserverlist.database.repository.UserRepository;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {
    final UserRepository userRepository;
    final PasswordEncoder passwordEncoder;

    @Nullable
    public User getLoggedUser(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof User userDetails) {
            return userDetails;
        }
        return null;
    }

    @Nullable
    public User getUserByEmailOrLogin(String identifier){
        return userRepository.findByEmailOrLogin(identifier, identifier).orElse(null);
    }


    public Response changePassword(ChangePasswordDto changePasswordDto) {
        if(changePasswordDto.getNewPassword().length()<4){
            return Response.builder().status(HttpStatus.BAD_REQUEST).message("Hasło jest zbyt krótkie").build();
        }

        User user = getLoggedUser();
        if(user == null){
            return Response.builder().status(HttpStatus.BAD_REQUEST).message("Twoja sesja wygasła. Zaloguj się ponownie").build();
        }

        if(passwordEncoder.matches(changePasswordDto.getCurrentPassword(), user.getPassword())){
            user.setPassword(passwordEncoder.encode(changePasswordDto.getNewPassword()));
            userRepository.save(user);
            return Response.builder().status(HttpStatus.OK).message("Hasło zostało prawidłowo zmienione").build();
        }
        return Response.builder().status(HttpStatus.BAD_REQUEST).message("Twoje obecne hasło jest niepoprawne.").build();
    }

    public Response changeLogin(String newLogin){
        if(newLogin.length()>30){
            return Response.builder().status(HttpStatus.BAD_REQUEST).message("Login jest zbyt długi").build();
        }
        if(newLogin.length()<4){
            return Response.builder().status(HttpStatus.BAD_REQUEST).message("Login jest zbyt krótki").build();
        }

        User user = getLoggedUser();
        if(user == null){
            return Response.builder().status(HttpStatus.BAD_REQUEST).message("Twoja sesja wygasła. Zaloguj się ponownie").build();
        }

        Optional<User> userWithLogin = userRepository.findByLogin(newLogin);
        if(userWithLogin.isPresent()){
            return Response.builder().status(HttpStatus.BAD_REQUEST).message("Login jest już zajęty").build();
        }

        user.setLogin(newLogin);
        userRepository.save(user);
        return Response.builder().status(HttpStatus.OK).message("Login został prawidłowo zmieniony").build();
    }

    public Response changeEmail(String newEmail) {
        if(newEmail.length()>60){
            return Response.builder().status(HttpStatus.BAD_REQUEST).message("Email jest zbyt długi").build();
        }

        User user = getLoggedUser();
        if(user == null){
            return Response.builder().status(HttpStatus.BAD_REQUEST).message("Twoja sesja wygasła. Zaloguj się ponownie").build();
        }

        Optional<User> userWithEmail = userRepository.findByEmail(newEmail);
        if(userWithEmail.isPresent()){
            return Response.builder().status(HttpStatus.BAD_REQUEST).message("E-mail jest już zajęty").build();
        }

        user.setEmail(newEmail);
        userRepository.save(user);
        return Response.builder().status(HttpStatus.OK).message("E-mail został prawidłowo zmieniony").build();
    }
}
