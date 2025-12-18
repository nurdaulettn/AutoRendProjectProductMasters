package kz.nurdaulet.AutoRent.repository;

import kz.nurdaulet.AutoRent.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
