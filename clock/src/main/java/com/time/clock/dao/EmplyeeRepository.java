package com.time.clock.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.time.clock.model.Employee;

public interface EmplyeeRepository extends JpaRepository<Employee, Long> {

}
