package eu.isygoit.repository;

import eu.isygoit.dto.data.CategoryDto;
import eu.isygoit.model.Category;
import eu.isygoit.model.Tag;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface CategoryRepository  extends JpaPagingAndSortingSAASRepository<Category,Long> {


}

