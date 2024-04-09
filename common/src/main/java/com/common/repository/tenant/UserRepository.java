package com.common.repository.tenant;

import com.common.entity.tenant.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
