package eu.isygoit.repository;

import eu.isygoit.model.TempCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository  extends JpaPagingAndSortingSAASRepository<TempCategory,Long> {
}
