package bk.repository;

import bk.model.Author;
import bk.model.CoAuthorship;
import bk.model.CoAuthorshipSimply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CoAuthorshipSimplyRepository extends JpaRepository<CoAuthorshipSimply,Long>{
    @Query(value = "select * from co_authorship_simply where (author_id1=?1 AND author_id2= ?2) or (author_id1=?2 AND author_id2= ?1)",
            nativeQuery = true)
    CoAuthorshipSimply findByAuthorId1OrAuthorId2(String id, String id2);


    List<CoAuthorshipSimply> findByLastYearBetween(int year1, int year2);


    @Query(value = "select * from co_authorship_simply where (author_id1=?1 or author_id2= ?2) and last_year = ?3",
            nativeQuery = true)
    List<CoAuthorshipSimply> findByAuthorId1OrAuthorId2AndLastYear(String id, String id2, int year);


    @Query(value = "select * from co_authorship_simply where (author_id1=?1 or author_id2= ?2) and (last_year >= ?3 and last_year <= ?4 )",
            nativeQuery = true)
    List<CoAuthorshipSimply> findByAuthorId1OrAuthorId2AndYearBetween(String id, String id2, int year1, int year2);


    @Query(value = "SELECT count(*) from co_authorship_simply where ((author_id1=?1 and author_id2=?2) or (author_id1=?2 and author_id2=?1)) and (last_year>=?3 and last_year<=?4)",
            nativeQuery = true)
    int findCommonArticle(String id, String id2, int year1, int year2);


    List<CoAuthorshipSimply> findByFlag(boolean flag);
    @Query(value = "select DISTINCT author_id1  from co_authorship_simply where author_id1 LIKE ?1 LIMIT 10",
            nativeQuery = true)
    List<String> findByIdPrefix(String id);
}
