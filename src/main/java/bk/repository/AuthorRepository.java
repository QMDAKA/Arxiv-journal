package bk.repository;

import bk.model.Author;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

/**
 * Created by quangminh on 16/11/2017.
 */
@RepositoryRestResource
public interface AuthorRepository extends JpaRepository<Author,String>{
    Author findById(String id);
    Page<Author> findAll(Pageable pageable);
    Page<Author> findById(String id, Pageable pageable);
    @Query(value = "select Distinct(a.id),a.affiliation,a.email,a.given_name,a.subject,a.surname,a.url from co_authorship_simply as c, author as a where (c.author_id1 = a.id or c.author_id2 = a.id) AND (a.id LIKE ?1 OR a.surname LIKE ?1) limit 10",
            nativeQuery = true)
    List<Author> findByIdPrefix(String id);
    @Query(value = "select Distinct(a.id),a.affiliation,a.email,a.given_name,a.subject,a.surname,a.url from author as a WHERE (a.id LIKE ?1 OR a.surname LIKE ?1) limit 10",
            nativeQuery = true)
    List<Author> findByNamePrefix(String name);

}
