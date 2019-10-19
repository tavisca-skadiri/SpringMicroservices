package com.tavisca.gce.RequestHandlerApi.repository;

import com.tavisca.gce.RequestHandlerApi.model.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestRepository extends JpaRepository<Request, Integer> {
}
