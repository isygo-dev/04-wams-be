package eu.isygoit.repository;

import eu.isygoit.enums.IEnumDocCommentsStaus;
import eu.isygoit.model.DocComment;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Repository

public interface DocCommentsRepository  extends  JpaPagingAndSortingRepository<DocComment,Long> {

    @Query("SELECT COUNT(dc) FROM DocComment dc " +
            "WHERE dc.document.id = :documentId " +
            "AND dc.startOffset = :startOffset " +
            "AND dc.endOffset = :endOffset " +
            "AND dc.type = :type")
    long countExistingComment(
            @Param("documentId") Long documentId,
            @Param("startOffset") Integer startOffset,
            @Param("endOffset") Integer endOffset,
            @Param("type") IEnumDocCommentsStaus.Types type
    );
    @Query("SELECT dc FROM DocComment dc WHERE dc.document.id = :documentId AND dc.checkCancel = false")
    List<DocComment> findByDocumentId(@Param("documentId") Long documentId);
}
