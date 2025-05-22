package eu.isygoit.repository;

import eu.isygoit.dto.data.CategoryDto;
import eu.isygoit.model.Category;
import eu.isygoit.model.Tag;
import feign.Param;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Repository

public interface CategoryRepository  extends JpaPagingAndSortingSAASRepository<Category,Long> {
    @Query("SELECT COUNT(c) FROM Category c WHERE c.createDate BETWEEN :start AND :end")
    long countByCreateDateBetween(@Param("start") Date startDate,
                                  @Param("end") Date endDate);

}

