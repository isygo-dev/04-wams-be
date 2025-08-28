package eu.isygoit.repository;

import eu.isygoit.model.Category;
import eu.isygoit.repository.tenancy.JpaPagingAndSortingTenantAndCodeAssignableRepository;
import feign.Param;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository

public interface CategoryRepository extends JpaPagingAndSortingTenantAndCodeAssignableRepository<Category, Long> {
    @Query("SELECT COUNT(c) FROM Category c WHERE c.createDate BETWEEN :start AND :end")
    Long countByCreateDateBetween(@Param("start") Date startDate,
                                  @Param("end") Date endDate);

}

