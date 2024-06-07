package com.subodh.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.subodh.model.RolesModel;

@Repository
public interface RoleRepository extends JpaRepository<RolesModel, Integer> {
    List<RolesModel> findByRoleName(String roleName);
}