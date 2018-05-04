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
    @Query(value = "select * from author where id LIKE ?1 LIMIT 5",
            nativeQuery = true)
    List<Author> findByIdPrefix(String id);
}
