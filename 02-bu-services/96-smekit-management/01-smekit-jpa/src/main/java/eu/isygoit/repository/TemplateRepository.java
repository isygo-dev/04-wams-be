package eu.isygoit.repository;

import eu.isygoit.dto.data.TemplateDto;
import eu.isygoit.enums.IEnumQuestionType;
import eu.isygoit.model.Category;
import eu.isygoit.model.Template;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TemplateRepository extends JpaPagingAndSortingSAASRepository<Template, Long>, PagingAndSortingRepository<Template, Long>  {

    List<Template> findByCategoryId(Long categoryId);
    @Query("SELECT COUNT(t) FROM Template t WHERE t.category.id = :categoryId")
    int countByCategoryId(@Param("categoryId") Long categoryId);
    @Query("SELECT t.category.id, COUNT(t) FROM Template t GROUP BY t.category.id")
    List<Object[]> countTemplatesByCategory();
    @Query("SELECT COUNT(t) FROM Template t WHERE t.category IS NULL")
    int countTemplatesWithoutCategory();
}