package ar.com.grooming.sexualgroomingreports.repository;

import ar.com.grooming.sexualgroomingreports.domain.ReportComments;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Spring Data JPA repository for the ReportComments entity.
 */
public interface ReportCommentsRepository extends JpaRepository<ReportComments, Long> {
    @Query(
        "select reportComments from ReportComments reportComments where reportComments.commentedBy.login = ?#{principal.preferredUsername}"
    )
    List<ReportComments> findByCommentedByIsCurrentUser();
}
