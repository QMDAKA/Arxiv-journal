package bk.repository;

import bk.model.CoAuthorship;
import bk.model.CoAuthorshipSimply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

/**
 * Created by quangminh on 18/11/2017.
 */
@RepositoryRestResource
public interface CoAuthorshipRepository extends JpaRepository<CoAuthorship,Long>{
    List<CoAuthorship> findByYear(int year);

    List<CoAuthorship> findByYearBetween(int year1, int year2);


    @Query(value = "select * from co_authorship where (author_id1=?1 or author_id2= ?2) and year = ?3",
            nativeQuery = true)
    List<CoAuthorship> findByAuthorId1OrAuthorId2AndYear(String id, String id2, int year);


    @Query(value = "select * from co_authorship where (author_id1=?1 or author_id2= ?2) and (year >= ?3 and year <= ?4 )",
            nativeQuery = true)
    List<CoAuthorship> findByAuthorId1OrAuthorId2AndYearBetween(String id, String id2, int year1, int year2);


    @Query(value = "SELECT count(*) from co_authorship where ((author_id1=?1 and author_id2=?2) or (author_id1=?2 and author_id2=?1)) and (year>=?3 and year<=?4)",
            nativeQuery = true)
    int findCommonArticle(String id, String id2, int year1, int year2);


    @Query(value = "SELECT count(*) from co_authorship where paper_id = ?1",
            nativeQuery = true)
    int findCountArticle(String id);


    @Query(value = "SELECT MAX(year) from co_authorship where ((author_id1=?1 and author_id2=?2) or (author_id1=?2 and author_id2=?1)) and (year>=?3 and year<=?4)",
            nativeQuery = true)
    int findMaxYear(String id, String id2, int year1, int year2);

    @Query(value = "SELECT * FROM co_authorship WHERE flag = ?1 and (year >=?2 and year <= ?3)",
            nativeQuery = true)
    List<CoAuthorship> findByFlag(boolean flag, int yearStart, int yearEnd);


}




