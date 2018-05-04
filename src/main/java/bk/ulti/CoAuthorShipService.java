package bk.ulti;

import bk.model.Author;
import bk.model.CoAuthorship;

import java.util.List;

/**
 * Created by quangminh on 08/09/2017.
 */
public interface CoAuthorShipService {

    int findMaxYear(String id, String id2, int year1, int year2);
    List<CoAuthorship> findByYearBetween(int year1, int year2);
    List<CoAuthorship> findByAuthorId1OrAuthorId2AndYear(String id1, String id2, int year);
    List<CoAuthorship> findByAuthorId1OrAuthorId2AndYearBetween(String id1, String id2, int year1,int year2);
    int findCommonArticle(String id1,String id2, int year1, int year2);
    List<Author> getPartnerAuthor(String id, int year);
    List<String> getPartnerAuthorBetweenYear(String id, int year1, int year2);
    List<String> getCandidateAuthorBetweenYear(String id, int year1, int year2);
    List<CoAuthorship> getByBetweenYear(int year1, int year2);
    List<String> getCommonNeighbours(String id1, String id2, int year1, int year2);
    List<String> getTotalNeighbours(String id1, String id2, int year1, int year2);

}
