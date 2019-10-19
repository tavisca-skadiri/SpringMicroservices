package com.tavisca.gce.DBAccessApi.repository;

import com.tavisca.gce.DBAccessApi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
}
