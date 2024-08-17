package com.coresaken.mcserverlist.auth.service;

import com.coresaken.mcserverlist.auth.dto.request.SignInRequestDto;
import com.coresaken.mcserverlist.auth.dto.request.SignUpRequestDto;
import com.coresaken.mcserverlist.auth.dto.response.AuthenticationResponse;
import com.coresaken.mcserverlist.database.model.ResetPasswordToken;
import com.coresaken.mcserverlist.database.model.User;
import com.coresaken.mcserverlist.database.repository.ActiveAccountTokenRepository;
import com.coresaken.mcserverlist.database.repository.ResetPasswordTokenRepository;
import com.coresaken.mcserverlist.database.repository.UserRepository;
import com.coresaken.mcserverlist.service.EmailSenderService;
import com.coresaken.mcserverlist.service.async.AsyncAccountService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.CookieClearingLogoutHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    final JwtService jwtService;

    final UserRepository userRepository;
    final ActiveAccountTokenRepository activeAccountTokenRepository;
    final ResetPasswordTokenRepository resetPasswordTokenRepository;

    final PasswordEncoder passwordEncoder;
    final AuthenticationManager authenticationManager;
    final EmailSenderService emailSenderService;
    final AsyncAccountService asyncAccountService;

    @Transactional
    public AuthenticationResponse signUp(SignUpRequestDto request, HttpServletResponse response) {
        String login = request.getLogin().trim();
        String email = request.getEmail().trim();
        String password = request.getPassword().trim();

        if(login.length()>30){
            return AuthenticationResponse.builder().status(HttpStatus.BAD_REQUEST).message("Login jest zbyt długi").build();
        }
        if(login.length()<4){
            return AuthenticationResponse.builder().status(HttpStatus.BAD_REQUEST).message("Login jest zbyt krótki").build();
        }
        if(password.length()<4){
            return AuthenticationResponse.builder().status(HttpStatus.BAD_REQUEST).message("Hasło jest zbyt krótkie").build();
        }
        if(email.length()>60){
            return AuthenticationResponse.builder().status(HttpStatus.BAD_REQUEST).message("Email jest zbyt długi").build();
        }
        User savedUser = userRepository.findByEmailOrLogin(email, login).orElse(null);
        if(savedUser != null){
            return AuthenticationResponse.builder().status(HttpStatus.BAD_REQUEST).message("Login lub email jest już zajęty!").build();
        }

        User user = User.builder()
                .login(request.getLogin())
                .email(request.getEmail())
                .uuid(UUID.randomUUID().toString())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(User.Role.USER)
                .build();

        try{
            user = userRepository.save(user);
        }catch (DataIntegrityViolationException e){
            return AuthenticationResponse.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .message("Login lub email jest już zajęty")
                    .build();
        }

        String activeAccountToken = generateActiveAccountToken();
        asyncAccountService.processAccountActivation(user.getId(), email, activeAccountToken);

        return AuthenticationResponse.builder().status(HttpStatus.OK).build();
    }

    public AuthenticationResponse signIn(SignInRequestDto request, HttpServletResponse response) {
        String identifier = request.getIdentifier();

        User user = userRepository.findByEmailOrLogin(identifier, identifier).orElse(null);
        if(user == null){
            return AuthenticationResponse.builder().status(HttpStatus.BAD_REQUEST).message("Niepoprawne dane logowania!").build();
        }

        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())){
            return AuthenticationResponse.builder().status(HttpStatus.BAD_REQUEST).message("Niepoprawne dane logowania!").build();
        }

        if(activeAccountTokenRepository.findByUserId(user.getId()).isPresent()){
            return AuthenticationResponse.builder().status(HttpStatus.BAD_REQUEST).message("Konto nie zostało aktywowane. Wyszukaj email i aktywuj konto!").build();
        }

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                identifier,
                request.getPassword()
        ));
        return getResponseWithToken(user, response);
    }

    public void logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }

        new CookieClearingLogoutHandler("jwt_token").logout(request, response, null);
    }

    public void activeAccount(String code){
        activeAccountTokenRepository.deleteByCode(code);
    }
    private String generateActiveAccountToken() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(30);

        for (int i = 0; i < 30; i++) {
            int randomIndex = random.nextInt(characters.length());
            sb.append(characters.charAt(randomIndex));
        }

        return sb.toString();
    }

    public AuthenticationResponse resetPassword(String email){
        User user = userRepository.findByEmail(email).orElse(null);
        if(user==null){
            return AuthenticationResponse.builder().status(HttpStatus.BAD_REQUEST).build();
        }

        String token = UUID.randomUUID().toString();

        ResetPasswordToken resetToken = resetPasswordTokenRepository.findByUserId(user.getId())
                .orElse(null);
        if(resetToken!=null){
            resetToken.setToken(token);
            resetToken.setExpiredAt(LocalDateTime.now().plusMinutes(10));
        }
        else{
            resetToken = new ResetPasswordToken(user, token);
        }

        resetPasswordTokenRepository.save(resetToken);

        try{
            emailSenderService.sendResetPasswordEmail(email, token);
        }
        catch (MessagingException e){
            e.printStackTrace();
        }

        return AuthenticationResponse.builder().status(HttpStatus.OK).build();
    }
    public String getResetPasswordPage(@RequestParam String token, Model model) {
        ResetPasswordToken resetToken = resetPasswordTokenRepository.findByToken(token).orElse(null);
        if(resetToken==null || resetToken.getExpiredAt().isBefore(LocalDateTime.now())){
            if(resetToken!=null){
                resetPasswordTokenRepository.delete(resetToken);
            }

            model.addAttribute("expired", true);
        }

        model.addAttribute("token", token);
        return "auth/resetPassword";
    }
    public AuthenticationResponse handleResetPassword(@RequestParam String token, @RequestParam String newPassword) {
        ResetPasswordToken resetToken = resetPasswordTokenRepository.findByToken(token).orElse(null);
        if(resetToken==null || resetToken.getExpiredAt().isBefore(LocalDateTime.now())){
            if(resetToken!=null){
                resetPasswordTokenRepository.delete(resetToken);
            }

            return AuthenticationResponse.builder().status(HttpStatus.BAD_REQUEST).message("Twój token wygasł. Zresetuj hasło ponownie!").build();
        }

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        resetPasswordTokenRepository.delete(resetToken);

        return AuthenticationResponse.builder().status(HttpStatus.OK).build();
    }

    private AuthenticationResponse getResponseWithToken(User user, HttpServletResponse response){
        var jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse.builder()
                .status(HttpStatus.OK)
                .token(jwtToken)
                .build();
    }
}
