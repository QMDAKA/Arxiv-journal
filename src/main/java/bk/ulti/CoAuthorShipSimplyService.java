package bk.ulti;

import bk.model.Author;
import bk.model.CoAuthorship;
import bk.model.CoAuthorshipSimply;
import org.springframework.stereotype.Service;

import java.util.List;

public interface CoAuthorShipSimplyService {
    CoAuthorshipSimply findByAuthorId1OrAuthorId2(String id1, String id2);
    List<CoAuthorshipSimply> findByLastYearBetween(int year1, int year2);
    List<CoAuthorshipSimply> findByAuthorId1OrAuthorId2AndLastYear(String id1, String id2, int year);
    List<CoAuthorshipSimply> findByAuthorId1OrAuthorId2AndYearBetween(String id1, String id2, int year1, int year2);
    List<CoAuthorshipSimply> findByFlag(boolean flag);

    List<Author> getPartnerAuthor(String id, int year);
    List<String> getPartnerAuthorBetweenYear(String id, int year1, int year2);
    List<String> getCandidateAuthorBetweenYear(String id, int year1, int year2);
    List<String> getCommonNeighbours(String id1, String id2, int year1, int year2);
    List<String> getTotalNeighbours(String id1, String id2, int year1, int year2);
    int findMaxYear(String authorId1, String authorId2);
}
