package com.tavisca.gce.DBAccessApi.repository;

import com.tavisca.gce.DBAccessApi.model.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestRepository extends JpaRepository<Request, Integer> {
}
