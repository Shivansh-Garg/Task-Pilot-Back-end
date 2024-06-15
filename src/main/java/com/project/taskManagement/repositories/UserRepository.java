package com.project.taskManagement.repositories;

import com.project.taskManagement.entities.Employees;
import com.project.taskManagement.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Employees,Long> {
    Optional<Employees> findFirstByEmail(String username);

    Optional<Employees> findByUserRole(UserRole userRole);
}
