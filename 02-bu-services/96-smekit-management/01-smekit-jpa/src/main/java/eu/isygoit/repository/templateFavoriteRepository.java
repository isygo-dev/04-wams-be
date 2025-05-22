package eu.isygoit.repository;

import eu.isygoit.model.TemplateFavorite;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Repository
public interface templateFavoriteRepository extends JpaPagingAndSortingRepository<TemplateFavorite, Long> {
    List<TemplateFavorite> findByUserIdentifier(String userIdentifier);

    boolean existsByUserIdentifierAndTemplateId(String userIdentifier, Long templateId);

    @Transactional
    @Modifying
    @Query("DELETE FROM TemplateFavorite tf WHERE tf.userIdentifier = :userIdentifier AND tf.template.id = :templateId")
    void deleteByUserAndTemplate(
            @Param("userIdentifier") String userIdentifier,
            @Param("templateId") Long templateId
    );
    @Query("SELECT CASE WHEN COUNT(tf) > 0 THEN true ELSE false END " +
            "FROM TemplateFavorite tf " +
            "WHERE tf.userIdentifier = :userIdentifier " +
            "AND tf.template.id = :templateId")
    boolean isTemplateFavorite(
            @Param("userIdentifier") String userIdentifier,
            @Param("templateId") Long templateId
    );

    @Query(value = "SELECT * FROM t_template_favorite WHERE user_identifier = ?1 AND template_id = ?2", nativeQuery = true)
    List<TemplateFavorite> debugFindFavorite(String userIdentifier, Long templateId);

    @Query("SELECT COUNT(tf) FROM TemplateFavorite tf WHERE tf.userIdentifier = :userIdentifier")
    long countByUserIdentifier(@Param("userIdentifier") String userIdentifier);

}