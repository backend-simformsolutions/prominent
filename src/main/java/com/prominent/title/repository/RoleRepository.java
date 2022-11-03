package com.prominent.title.repository;

import com.prominent.title.entity.user.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {

    Role findByRoleCode(String roleCode);
}
