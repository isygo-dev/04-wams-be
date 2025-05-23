package eu.isygoit.repository;

import eu.isygoit.model.Category;
import feign.Param;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository

public interface CategoryRepository extends JpaPagingAndSortingSAASRepository<Category, Long> {
    @Query("SELECT COUNT(c) FROM Category c WHERE c.createDate BETWEEN :start AND :end")
    long countByCreateDateBetween(@Param("start") Date startDate,
                                  @Param("end") Date endDate);

}

