package bk.ulti;


import bk.model.Candidate;

import java.util.List;

/**
 * Created by quangminh on 18/09/2017.
 */
public interface CandidateService {
    List<String> findProcessAuthorId();
    List<Candidate> findByAuthorId1OrAuthorId2AndYearStart(String id, String id2, int year);
    List<Candidate> findByYearStart(int year);
    void saveCandidateFromPartnerResult(String id, int year1, int year2);
    double CNMeasure(Candidate candidate, List<String> listCommon , int year1, int year2);
    double AAMeasure(Candidate candidate,List<String> listCommon , int year1,int year2);
    double JCMeasure(Candidate candidate,List<String> listCommon , int year1,int year2);
    double WCNMeasure(Candidate candidate,List<String> listCommon ,int year1, int year2);
    double WAAMeasure(Candidate candidate,List<String> listCommon , int year1,int year2);
    double WJCMeasure(Candidate candidate,List<String> listCommon , int year1,int year2);
}
