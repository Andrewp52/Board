package com.pashenko.Board.services;

import com.pashenko.Board.entities.ConfirmationToken;
import com.pashenko.Board.entities.Role;
import com.pashenko.Board.entities.User;
import com.pashenko.Board.entities.dto.PassChangeDto;
import com.pashenko.Board.entities.dto.UserDto;
import com.pashenko.Board.entities.dto.UserRegDto;
import com.pashenko.Board.exceptions.profile.WrongPasswordException;
import com.pashenko.Board.exceptions.registration.*;
import com.pashenko.Board.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
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

    public User registerNewUser(UserRegDto dto) {
        if(dto.getId() != null){
            throw new UserIdIsNotNullException();
        }
        userRepo.findByEmail(dto.getEmail()).ifPresent(u -> { throw new EmailOccupiedExceprtion(); });
        User user = new User(
                dto.getFirstName(),
                dto.getLastName(),
                dto.getPhone(),
                dto.getEmail(),
                passwordEncoder.encode(dto.getPassword())
        );
        user.setEnabled(false);
        return userRepo.save(user);
    }

    public void confirmRegistration(String tokenStr){
        ConfirmationToken token = tokenService.getTokenByUUID(tokenStr);
        if(token.getExpirationDate().isBefore(LocalDateTime.now())){
            throw new ConfirmTokenExpiredException();
        }
        User user = token.getUser();
        user.setRoles(List.of(roleService.getByRole("USER")));
        user.setEnabled(true);
        tokenService.save(token);       // Can't save through userRepo (Inv. Tgt. Exc)
        tokenService.delete(token);
    }

    public ConfirmationToken getConfirmToken(User user){
        return this.tokenService.generateAndGetConfirmToken(user);
    }

    @Transactional
    public void deleteExpiredTokensAndUsers() {
        tokenService.getExpiredTokens().forEach(t -> {
            if(!t.getUser().getEnabled()){      // Account is not activated
                userRepo.delete(t.getUser());
            }
            tokenService.delete(t);
        });
    }

    public User updateProfile(User caller, UserDto newProfile) {
        if(!caller.getId().equals(newProfile.getId())){
            if(!isUserAdmin(caller)){
                throw new AccessDeniedException("Insufficient rights to edit user's account");
            }
        }
        User toChange = userRepo.getById(newProfile.getId());
        toChange.setFirstName(newProfile.getFirstName());
        toChange.setLastName(newProfile.getLastName());
        toChange.setPhone(newProfile.getPhone());
        toChange.setEmail(newProfile.getEmail());
        return userRepo.save(toChange);
    }

    public void changeUserPassword(User caller, PassChangeDto dto){
        if(!passwordEncoder.matches(dto.getOldPass(), caller.getPassword())){
            throw new WrongPasswordException();
        }
        caller.setPassword(passwordEncoder.encode(dto.getNewPass()));
        userRepo.save(caller);
    }

    private boolean isUserAdmin(User user){
        return user.getRoles().stream().map(Role::getRole).anyMatch(s -> s.equals("ADMIN"));
    }
}
