package eu.isygoit.repository;

import eu.isygoit.model.Template;
import feign.Param;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TemplateRepository  extends JpaPagingAndSortingRepository<Template,Long> {

}
