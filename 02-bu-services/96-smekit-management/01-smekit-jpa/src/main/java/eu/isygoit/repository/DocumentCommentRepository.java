package eu.isygoit.repository;

import eu.isygoit.model.DocumentComment;
import eu.isygoit.repository.tenancy.JpaPagingAndSortingTenantAndCodeAssignableRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface DocumentCommentRepository extends JpaPagingAndSortingRepository<DocumentComment, Long> {
}
