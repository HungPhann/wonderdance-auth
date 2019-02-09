package tk.wonderdance.auth.repository;

import org.springframework.data.repository.CrudRepository;
import tk.wonderdance.auth.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findUserByEmail(String email);

    Optional<User> findUserById(long id);

    Boolean existsByEmail(String email);
}
