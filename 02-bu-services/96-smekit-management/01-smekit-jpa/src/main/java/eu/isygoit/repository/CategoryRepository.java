package eu.isygoit.repository;

import eu.isygoit.model.TempCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface CategoryRepository  extends JpaPagingAndSortingSAASRepository<TempCategory,Long> {
}
