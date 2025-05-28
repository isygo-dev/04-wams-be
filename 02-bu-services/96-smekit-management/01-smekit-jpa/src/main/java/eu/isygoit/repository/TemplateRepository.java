package eu.isygoit.repository;

import eu.isygoit.enums.IEnumTemplateStatus;
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

    @Query("SELECT COUNT(t) FROM Template t WHERE t.category.id = :categoryId")
    int countByCategoryId(@Param("categoryId") Long categoryId);

    @Query("SELECT t.category.id, COUNT(t) FROM Template t GROUP BY t.category.id")
    List<Object[]> countByCategory();

    @Query("SELECT COUNT(t) FROM Template t WHERE t.category IS NULL")
    int countWithoutCategory();


    @Query("SELECT COUNT(t) FROM Template t WHERE t.createDate BETWEEN :start AND :end")
    Long countByCreateDateBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT COUNT(t) FROM Template t WHERE t.state = :type")
    Long countByStatus(@Param("type") IEnumTemplateStatus.Types type);

    Long countByFavoritesContaining(String userName);

    @Query("SELECT COUNT(t) FROM Template t JOIN t.favorites f WHERE f = :userName")
    Long countTemplatesWithFavoriteUser(@Param("userName") String userName);

    List<Template> findAllByFavoritesContaining(String userName);

    Template findAllByIdAndFavoritesContaining(Long id, String userName);
}