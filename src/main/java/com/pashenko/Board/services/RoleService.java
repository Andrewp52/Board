package com.pashenko.Board.services;

import com.pashenko.Board.entities.Role;
import com.pashenko.Board.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {
    @Autowired
    private RoleRepository repository;
    public Role getByRole(String role) {
        return repository.getByRole(role);
    }
}
