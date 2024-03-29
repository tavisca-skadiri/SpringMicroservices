package com.tavisca.gce.DBValidatorApi.repository;

import com.tavisca.gce.DBValidatorApi.model.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestRepository extends JpaRepository<Request, Integer> {
}
