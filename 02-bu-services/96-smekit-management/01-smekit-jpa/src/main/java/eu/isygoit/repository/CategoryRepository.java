package eu.isygoit.repository;

import eu.isygoit.model.Category;
import org.springframework.stereotype.Repository;

@Repository

public interface CategoryRepository  extends JpaPagingAndSortingSAASRepository<Category,Long> {
}
