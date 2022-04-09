package com.pashenko.Board.repositories;

import com.pashenko.Board.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role getByRole(String role);
}
