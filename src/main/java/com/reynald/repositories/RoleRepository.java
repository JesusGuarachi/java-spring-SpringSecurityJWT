package com.reynald.repositories;

import com.reynald.models.RoleEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface RoleRepository extends CrudRepository<RoleEntity, Long> {
    Set<RoleEntity> findByNameIn(Set<String> roles);
}
