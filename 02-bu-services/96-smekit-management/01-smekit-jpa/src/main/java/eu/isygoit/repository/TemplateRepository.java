package eu.isygoit.repository;

import eu.isygoit.model.Template;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TemplateRepository  extends JpaPagingAndSortingSAASRepository<Template,Long> {
}
