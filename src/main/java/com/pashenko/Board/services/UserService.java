package com.pashenko.Board.services;

import com.pashenko.Board.entities.ConfirmToken;
import com.pashenko.Board.entities.User;
import com.pashenko.Board.exceptions.registration.*;
import com.pashenko.Board.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserService implements UserDetailsService {
    private final RoleService roleService;
    private final ConfirmTokenService tokenService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepo;

    @Autowired
    public UserService(RoleService roleService,
                       ConfirmTokenService tokenService,
                       PasswordEncoder passwordEncoder,
                       UserRepository userRepo) {

        this.roleService = roleService;
        this.tokenService = tokenService;
        this.passwordEncoder = passwordEncoder;
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findByEmail(username).orElseThrow(
                () -> new UsernameNotFoundException("Confirmation token not found!")
        );
    }

    public User registerNewUser(User user) {
        if(user.getId() != null){
            throw new UserIdIsNotNullException();
        }
        userRepo.findByEmail(user.getEmail()).ifPresent(u -> { throw new EmailOccupiedExceprtion(); });
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnabled(false);
        return userRepo.save(user);
    }

    public void confirmRegistration(String tokenStr){
        ConfirmToken token = tokenService.getTokenByUUID(tokenStr);
        if(token.getExpirationDate().isBefore(LocalDateTime.now())){
            throw new ConfirmTokenExpiredException();
        }
        User user = token.getUser();
        user.setRoles(List.of(roleService.getByRole("USER")));
        user.setEnabled(true);
        tokenService.save(token);       // Can't save through userRepo (Inv. Tgt. Exc)
        tokenService.delete(token);
    }

    public String getConfirmToken(User user){
        return this.tokenService.generateAndGetConfirmToken(user);
    }

    @Transactional
    public void deleteExpiredTokensAndUsers() {
        tokenService.getExpiredTokens().forEach(t -> {
            userRepo.delete(t.getUser());
            tokenService.delete(t);
        });
    }
}
