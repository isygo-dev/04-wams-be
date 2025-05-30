package eu.isygoit.repository;

import eu.isygoit.model.Author;
import feign.Param;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository

public interface AuthorRepository extends JpaPagingAndSortingCodeAssingnableRepository<Author, Long> {
    @Query("SELECT COUNT(a) FROM Author a WHERE a.createDate BETWEEN :start AND :end")
    Long countByCreateDateBetween(@Param("start") Date startDate,
                                  @Param("end") Date endDate);
}
