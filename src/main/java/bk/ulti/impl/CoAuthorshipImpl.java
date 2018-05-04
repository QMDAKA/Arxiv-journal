package bk.ulti.impl;

import bk.model.Author;
import bk.model.CoAuthorship;
import bk.repository.AuthorRepository;
import bk.repository.CoAuthorshipRepository;
import bk.ulti.CoAuthorShipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by quangminh on 08/09/2017.
 */
@Service
public class CoAuthorshipImpl implements CoAuthorShipService {

    @Autowired
    CoAuthorshipRepository coAuthorshipRepository;

    @Autowired
    AuthorRepository authorRepository;


    @Override
    public int findMaxYear(String id, String id2, int year1, int year2) {
        return coAuthorshipRepository.findMaxYear(id,id2,year1,year2);
    }

    @Override
    public List<CoAuthorship> findByYearBetween(int year1, int year2) {
        return coAuthorshipRepository.findByYearBetween(year1,year2);
    }

    @Override
    public List<CoAuthorship> findByAuthorId1OrAuthorId2AndYear(String id1, String id2, int year) {
        return coAuthorshipRepository.findByAuthorId1OrAuthorId2AndYear(id1,id2,year);
    }

    @Override
    public List<CoAuthorship> findByAuthorId1OrAuthorId2AndYearBetween(String id1, String id2, int year1, int year2) {
        return coAuthorshipRepository.findByAuthorId1OrAuthorId2AndYearBetween(id1,id2,year1,year2);
    }

    @Override
    public int findCommonArticle(String id1, String id2, int year1, int year2) {
        return coAuthorshipRepository.findCommonArticle(id1,id2,year1,year2);
    }

    // tim cac
    @Override
    public List<Author> getPartnerAuthor(String id, int year) {
        List<Author> authors = new ArrayList<>();
        for (CoAuthorship coAuthorship : this.findByAuthorId1OrAuthorId2AndYear(id, id, year)) {
            Author author;
            if (coAuthorship.getAuthorId1().compareTo(id)!=0) {
                author = authorRepository.findOne(coAuthorship.getAuthorId1());
            } else {
                author = authorRepository.findOne(coAuthorship.getAuthorId2());
            }
            if (authors.indexOf(author) == -1) {
                authors.add(author);
            }
        }
        return authors;
    }

    @Override
    public List<String> getPartnerAuthorBetweenYear(String id, int year1, int year2) {
        List<String> authors = new ArrayList<>();
        String idPartner;
        for (CoAuthorship coAuthorship : this.findByAuthorId1OrAuthorId2AndYearBetween(id, id, year1, year2)) {
            Author author;
            if (coAuthorship.getAuthorId1().compareTo(id)!=0) {
                idPartner = coAuthorship.getAuthorId1();
            } else {
                idPartner = coAuthorship.getAuthorId2();
            }
            if (authors.indexOf(idPartner) == -1) {
                authors.add(idPartner);
            }
        }
        return authors;
    }

    @Override
    public List<String> getCandidateAuthorBetweenYear(String id, int year1, int year2) {
        List<String> authors = new ArrayList<>();
        List<String> candidateAuthors = new ArrayList<>();

        for (CoAuthorship coAuthorship : this.findByAuthorId1OrAuthorId2AndYearBetween(id, id, year1, year2)) {
            Author author;
            String idPartner;
            if (coAuthorship.getAuthorId1().compareTo(id)!=0) {
                idPartner = coAuthorship.getAuthorId1();
            } else {
                idPartner = coAuthorship.getAuthorId2();
            }
            if (authors.indexOf(idPartner) == -1) {
                String idCandidate = null;
                authors.add(idPartner);
                for (CoAuthorship coAuthorshipCandidate : this.findByAuthorId1OrAuthorId2AndYearBetween(idPartner, idPartner, year1, year2)) {
                    if (coAuthorshipCandidate.getAuthorId1().compareTo(id)!=0 && coAuthorshipCandidate.getAuthorId1().compareTo(idPartner)!=0) {
                        idCandidate = coAuthorshipCandidate.getAuthorId1();
                    }
                    else if (coAuthorshipCandidate.getAuthorId2().compareTo(id)!=0 && coAuthorshipCandidate.getAuthorId2() != idPartner) {
                        idCandidate = coAuthorshipCandidate.getAuthorId2();
                    }
                    else{
                        continue;
                    }
                    if(!candidateAuthors.contains(idCandidate)){
                        candidateAuthors.add(idCandidate);
                    }
                }
            }
        }


        return candidateAuthors;
    }

    @Override
    public List<CoAuthorship> getByBetweenYear(int year1, int year2) {
        return coAuthorshipRepository.findByYearBetween(year1, year2);
    }

    @Override
    public List<String> getCommonNeighbours(String id1, String id2, int year1, int year2) {

        List<String> common =new ArrayList<>();
        List<String> listFriendA = this.getPartnerAuthorBetweenYear(id1,year1,year2);
        List<String> listFriendB = this.getPartnerAuthorBetweenYear(id2,year1,year2);
        for(String idA:listFriendA)
        {
            if(listFriendB.contains(idA)){
                common.add(idA);
            }
        }
        return common;
    }

    @Override
    public List<String> getTotalNeighbours(String id1, String id2, int year1, int year2) {
        List<Long> common =new ArrayList<>();
        List<Long> total =new ArrayList<>();

        List<String> listFriendA = this.getPartnerAuthorBetweenYear(id1,year1,year2);
        List<String> listFriendB = this.getPartnerAuthorBetweenYear(id2,year1,year2);
        listFriendA.removeAll(listFriendB);
        listFriendA.addAll(listFriendB);

        return listFriendA;
    }
}
