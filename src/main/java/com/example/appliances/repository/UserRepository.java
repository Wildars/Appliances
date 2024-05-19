package com.example.appliances.repository;


import com.example.appliances.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User getByPin(String pin);

    Optional<User> findById(Long id);

    @Query("select u from User u " +
           "where u.pin like ?1")
    User getUserByPinQuery(String username);

    Page<User> findByIsActiveTrue(Pageable pageable);

    Page<User> findByIsActiveFalse(Pageable pageable);

    User findByPin(String pin);

//    @Query("SELECT u.organizations FROM User u WHERE u = :expert")
//    List<Organization> findOrganizationsByExpert(User expert);

}
