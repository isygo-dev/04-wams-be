package eu.isygoit.repository;

import eu.isygoit.model.ResumeShareInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The interface Resume shared info repository.
 */
@Repository
public interface ResumeSharedInfoRepository extends JpaRepository<ResumeShareInfo, Long> {
}
