package com.prominent.title.repository;

import com.prominent.title.entity.user.UserRoles;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRolesRepository extends PagingAndSortingRepository<UserRoles, Integer> {
}
