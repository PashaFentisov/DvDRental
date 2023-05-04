package repository;

import entity.Category;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategotyRepository extends CrudRepository<Category, Long> {
}
