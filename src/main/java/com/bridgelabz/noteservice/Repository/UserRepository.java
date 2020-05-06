package com.bridgelabz.noteservice.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bridgelabz.noteservice.Entity.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity,String> {

}
