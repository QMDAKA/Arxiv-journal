package bk.ulti.impl;

import bk.model.Author;
import bk.model.CoAuthorship;
import bk.model.CoAuthorshipSimply;
import bk.repository.CoAuthorshipSimplyRepository;
import bk.ulti.CoAuthorShipSimplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CoAuthorShipSimplyImpl implements CoAuthorShipSimplyService {
    @Autowired
    CoAuthorshipSimplyRepository coAuthorshipSimplyRepository;


    @Override
    public CoAuthorshipSimply findByAuthorId1OrAuthorId2(String id1, String id2) {
        return coAuthorshipSimplyRepository.findByAuthorId1OrAuthorId2(id1, id2);
    }

    @Override
    public List<CoAuthorshipSimply> findByLastYearBetween(int year1, int year2) {
        return coAuthorshipSimplyRepository.findByLastYearBetween(year1, year2);
    }

    @Override
    public List<CoAuthorshipSimply> findByAuthorId1OrAuthorId2AndLastYear(String id1, String id2, int year) {
        return coAuthorshipSimplyRepository.findByAuthorId1OrAuthorId2AndLastYear(id1, id2, year);
    }

    @Override
    public List<CoAuthorshipSimply> findByAuthorId1OrAuthorId2AndYearBetween(String id1, String id2, int year1, int year2) {
        return coAuthorshipSimplyRepository.findByAuthorId1OrAuthorId2AndYearBetween(id1,  id2 , year1,year2);
    }

    @Override
    public List<CoAuthorshipSimply> findByFlag(boolean flag) {
        return coAuthorshipSimplyRepository.findByFlag(flag);
    }

    @Override
    public List<Author> getPartnerAuthor(String id, int year) {
        return null;
    }

    // lay ra nhung dong tac gia
    @Override
    public List<String> getPartnerAuthorBetweenYear(String id, int year1, int year2) {
        List<String> authors = new ArrayList<>();
        String idPartner;
        for (CoAuthorshipSimply coAuthorshipSimply : this.findByAuthorId1OrAuthorId2AndYearBetween(id, id, year1, year2)) {
            if (coAuthorshipSimply.getAuthorId1().compareTo(id) != 0) {
                idPartner = coAuthorshipSimply.getAuthorId1();
            } else {
                idPartner = coAuthorshipSimply.getAuthorId2();
            }
            if (authors.indexOf(idPartner) == -1) {
                authors.add(idPartner);
            }
        }
        return authors;
    }

    // lay ra hang xom chung co do dai = 1
    @Override
    public List<String> getCandidateAuthorBetweenYear(String id, int year1, int year2) {
        List<String> authors = new ArrayList<>();
        List<String> candidateAuthors = new ArrayList<>();

        //tim ra cac hang xom
        for (CoAuthorshipSimply coAuthorship : this.findByAuthorId1OrAuthorId2AndYearBetween(id, id, year1, year2)) {
            Author author;
            String idPartner;
            if (coAuthorship.getAuthorId1().compareTo(id) != 0) {
                idPartner = coAuthorship.getAuthorId1();
            } else {
                idPartner = coAuthorship.getAuthorId2();
            }
            if (authors.indexOf(idPartner) == -1) {
                authors.add(idPartner);
                String idCandidate = null;
                //tim ra hang xom cua hang xom
                for (CoAuthorshipSimply coAuthorshipCandidate : this.findByAuthorId1OrAuthorId2AndYearBetween(idPartner, idPartner, year1, year2)) {
                    if (coAuthorshipCandidate.getAuthorId1().compareTo(id) != 0 && coAuthorshipCandidate.getAuthorId1().compareTo(idPartner) != 0) {
                        idCandidate = coAuthorshipCandidate.getAuthorId1();
                    } else if (coAuthorshipCandidate.getAuthorId2().compareTo(id) != 0 && coAuthorshipCandidate.getAuthorId2().compareTo(idPartner) != 0) {
                        idCandidate = coAuthorshipCandidate.getAuthorId2();
                    } else {
                        continue;
                    }
                    if (!candidateAuthors.contains(idCandidate)) {
                        candidateAuthors.add(idCandidate);
                    }
                }
            }
        }


        return candidateAuthors;
    }

    // tim ra ban chung cua id1 va id2
    @Override
    public List<String> getCommonNeighbours(String id1, String id2, int year1, int year2) {
        List<String> common = new ArrayList<>();
        List<String> listFriendA = this.getPartnerAuthorBetweenYear(id1, year1, year2);
        List<String> listFriendB = this.getPartnerAuthorBetweenYear(id2, year1, year2);
        for (String idA : listFriendA) {
            if (listFriendB.contains(idA)) {
                common.add(idA);
            }
        }
        return common;
    }


    // tim ra cac ban cua ca 2 id1, id2
    @Override
    public List<String> getTotalNeighbours(String id1, String id2, int year1, int year2) {
        List<Long> common = new ArrayList<>();
        List<Long> total = new ArrayList<>();

        List<String> listFriendA = this.getPartnerAuthorBetweenYear(id1, year1, year2);
        List<String> listFriendB = this.getPartnerAuthorBetweenYear(id2, year1, year2);
        listFriendA.removeAll(listFriendB);
        listFriendA.addAll(listFriendB);

        return listFriendA;
    }

    @Override
    public int findMaxYear(String authorId1, String authorId2) {
        try {
            CoAuthorshipSimply coAuthorshipSimply = this.findByAuthorId1OrAuthorId2(authorId1, authorId2);
            if (coAuthorshipSimply != null) {
                return coAuthorshipSimply.getLastYear();
            } else {
                return -1;
            }
        } catch (Exception e) {
            System.out.println(e);
            return -1;
        }
    }
}
