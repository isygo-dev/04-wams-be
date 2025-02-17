package eu.isygoit.repository;

import eu.isygoit.model.Template;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface TemplateRepository  extends JpaPagingAndSortingSAASRepository<Template,Long> {
}
