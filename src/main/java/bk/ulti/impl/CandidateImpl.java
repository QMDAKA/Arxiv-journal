package bk.ulti.impl;

import bk.model.Candidate;
import bk.repository.CandidateRepository;
import bk.ulti.CandidateService;
import bk.ulti.CoAuthorShipService;
import bk.ulti.CoAuthorShipSimplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CandidateImpl implements CandidateService
{
    @Autowired
    CoAuthorShipService coAuthorShipService;
    @Autowired
    CoAuthorShipSimplyService coAuthorShipSimplyService;
    @Autowired
    CandidateRepository candidateRepository;


    @Override
    public List<String> findProcessAuthorId() {

        return candidateRepository.findProcessAuthorId();
    }

    @Override
    public List<Candidate> findByAuthorId1OrAuthorId2AndYearStart(String id, String id2, int year) {
        return candidateRepository.findByAuthorId1OrAuthorId2AndYearStart(id,id2,year);

    }

    @Override
    public List<Candidate> findByYearStart(int year) {
        return candidateRepository.findByYearStart(year);
    }

    @Override
    public void saveCandidateFromPartnerResult(String id, int yearStart, int yearEnd) {
        List<String> coAuthorshipList = coAuthorShipSimplyService.getCandidateAuthorBetweenYear(id,yearStart,yearEnd);
        for(String idCandidate: coAuthorshipList){
            //check ton tai author id hay chua
            if(candidateRepository.findByAuthorId1OrAuthorId2AndYearStart(id,idCandidate,yearStart).size()==0){
                Candidate candidate = new Candidate();
                candidate.setAuthorId1(id);
                candidate.setAuthorId2(idCandidate);
                candidate.setYearStart(yearStart);
                try {
                    int yearOption = coAuthorShipSimplyService.findMaxYear(id, idCandidate);
                    if(yearOption != -1){
                        candidate.setYearEnd(yearOption);
                        candidate.setLabel(true);
                    }
                    else {
                        candidate.setLabel(false);
                        candidate.setYearEnd(yearEnd);
                    }

                }catch (Exception ex){
                    candidate.setLabel(false);
                    candidate.setYearEnd(yearEnd);
                }
                candidateRepository.save(candidate);
            }
            else
            {
                System.out.println("Yo Yo");
            }

        }
    }

    @Override
    public double CNMeasure(Candidate candidate,List<String> listCommon , int year1, int year2) {
        return listCommon.size();
    }

    @Override
    public double AAMeasure(Candidate candidate , List<String> listCommon ,int year1, int year2) {
        double result=0;


        for(String idCommon:listCommon){
            int countFriend = coAuthorShipSimplyService.getPartnerAuthorBetweenYear(idCommon,year1,year2).size();
            result += 1/(Math.log(countFriend));
        }
        return result;
    }

    @Override
    public double JCMeasure(Candidate candidate,List<String> listCommon , int year1, int year2) {

        String author1 = candidate.getAuthorId1();
        String author2 = candidate.getAuthorId2();

        double common = listCommon.size();
        double total = coAuthorShipSimplyService.getTotalNeighbours(author1,author2,year1,year2).size();
        return common/total;
    }

    @Override
    public double WCNMeasure(Candidate candidate,List<String> listCommon , int year1, int year2) {
        String author1 = candidate.getAuthorId1();
        String author2 = candidate.getAuthorId2();
        double wcn =0;

        for(String idCommon: listCommon){
            wcn += (coAuthorShipSimplyService.findByAuthorId1OrAuthorId2(author1,idCommon).getCoAuthorshipTime()+coAuthorShipSimplyService.findByAuthorId1OrAuthorId2(author2,idCommon).getCoAuthorshipTime())/2.0;
        }
        return wcn;

    }

    @Override
    public double WAAMeasure(Candidate candidate,List<String> listCommon , int year1, int year2) {
        String author1 = candidate.getAuthorId1();
        String author2 = candidate.getAuthorId2();

        double waa =0;
        for(String idCommon: listCommon){
            double logTotal=0;
            for(String idOfPartnerCommon:coAuthorShipSimplyService.getPartnerAuthorBetweenYear(idCommon,year1,year2)){
                logTotal += coAuthorShipSimplyService.findByAuthorId1OrAuthorId2(idCommon,idOfPartnerCommon).getCoAuthorshipTime();
            }
            logTotal = 1/(Math.log(logTotal));
            double averageOfTwoWeight =((coAuthorShipSimplyService.findByAuthorId1OrAuthorId2(author1,idCommon).getCoAuthorshipTime()+coAuthorShipSimplyService.findByAuthorId1OrAuthorId2(author2,idCommon).getCoAuthorshipTime())/2.0);
            waa +=  averageOfTwoWeight*logTotal;
        }
        return waa;
    }

    @Override
    public double WJCMeasure(Candidate candidate, List<String> listCommon ,int year1, int year2) {

        String author1 = candidate.getAuthorId1();
        String author2 = candidate.getAuthorId2();
        double wcn=this.WCNMeasure(candidate, listCommon ,year1,year2);
        double totalA=0;
        double totalB=0;
        for(String idOfPartner:coAuthorShipSimplyService.getPartnerAuthorBetweenYear(author1,year1,year2)){
            totalA += coAuthorShipSimplyService.findByAuthorId1OrAuthorId2(author1,idOfPartner).getCoAuthorshipTime();
        }
        for(String idOfPartner:coAuthorShipSimplyService.getPartnerAuthorBetweenYear(author2,year1,year2)){
            totalB += coAuthorShipSimplyService.findByAuthorId1OrAuthorId2(author2,idOfPartner).getCoAuthorshipTime();
        }
        double wjc = wcn/(totalA+totalB);
        return wjc;
    }
}
