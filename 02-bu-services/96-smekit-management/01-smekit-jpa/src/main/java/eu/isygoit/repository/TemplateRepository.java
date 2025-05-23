package eu.isygoit.repository;

import eu.isygoit.enums.IEnumDocTempStatus;
import eu.isygoit.model.Template;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TemplateRepository extends JpaPagingAndSortingSAASRepository<Template, Long>, PagingAndSortingRepository<Template, Long> {

    List<Template> findByCategoryId(Long categoryId);

    //    @Query("SELECT t FROM Template t WHERE t.category.id = :categoryId")
//    List<Template> findByCategoryId(@PathVariable Long categoryId);
    @Query("SELECT COUNT(t) FROM Template t WHERE t.category.id = :categoryId")
    int countByCategoryId(@Param("categoryId") Long categoryId);

    @Query("SELECT t.category.id, COUNT(t) FROM Template t GROUP BY t.category.id")
    List<Object[]> countTemplatesByCategory();

    @Query("SELECT COUNT(t) FROM Template t WHERE t.category IS NULL")
    int countTemplatesWithoutCategory();


    @Query("SELECT COUNT(t) FROM Template t WHERE t.createDate BETWEEN :start AND :end")
    long countByCreateDateBetween(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);

    @Query("SELECT COUNT(t) FROM Template t WHERE t.typeTs = :type")
    long countByTypeTs(@Param("type") IEnumDocTempStatus.Types type);
}