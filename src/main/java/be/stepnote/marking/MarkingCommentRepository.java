package be.stepnote.marking;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MarkingCommentRepository extends JpaRepository<MarkingComment, Long>,MarkingCommentCustomRepository{

}
