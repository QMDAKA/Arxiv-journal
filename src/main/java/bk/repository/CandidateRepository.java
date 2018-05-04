package bk.repository;

import bk.model.Candidate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * Created by quangminh on 14/09/2017.
 */
public interface CandidateRepository extends JpaRepository<Candidate,Long> {
    @Query(value = "select * from candidate where ((author_id1=?1 and author_id2= ?2)or(author_id1=?2 and author_id2=?1)) and (year_start=?3)",
            nativeQuery = true)
    List<Candidate> findByAuthorId1OrAuthorId2AndYearStart(String id, String id2, int year);

    @Query(value = "select distinct author_id1 from candidate",
            nativeQuery = true)
    List<String> findProcessAuthorId();
    List<Candidate> findByYearStart(int year);

    @Query(value = "select * from candidate where cn = 0",
            nativeQuery = true)
    List<Candidate> findByCn();

    Page<Candidate> findAll(Pageable pageable);


    Page<Candidate> findByCNGreaterThan(int value,Pageable pageable);

    Page<Candidate> findByYearStartGreaterThanEqualAndYearEndLessThanEqual(int yearStart, int yearEnd, Pageable pageable);
}
