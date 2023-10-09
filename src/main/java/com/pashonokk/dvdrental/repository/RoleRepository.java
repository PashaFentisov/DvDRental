package com.pashonokk.dvdrental.repository;

import com.pashonokk.dvdrental.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findRoleByName(String name);

    @Query("select r from Role r left join fetch r.permissions where r.id in :ids")
    List<Role> findAllByIdAndPermissions(@Param("ids") Iterable<Long> ids);
}
