package com.HowTo.spring_boot_HowTo.repository;

import  com.HowTo.spring_boot_HowTo.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepositoryI extends JpaRepository<Role, Long> {

    Role findByDescription(String description);
	
    @Override
    void delete(Role role);

}