package bk.thread;

import bk.model.Candidate;
import bk.model.Var;
import bk.repository.CandidateRepository;
import bk.ulti.CandidateService;
import bk.ulti.CoAuthorShipSimplyService;

import java.util.List;

public class CandidateMeasureThread implements Runnable {

    CandidateRepository candidateRepository;
    CandidateService candidateService;
    List<Candidate> list;
    CoAuthorShipSimplyService coAuthorShipSimplyService;

    public CoAuthorShipSimplyService getCoAuthorShipSimplyService() {
        return coAuthorShipSimplyService;
    }

    public void setCoAuthorShipSimplyService(CoAuthorShipSimplyService coAuthorShipSimplyService) {
        this.coAuthorShipSimplyService = coAuthorShipSimplyService;
    }

    public List<Candidate> getList() {
        return list;
    }

    public void setList(List<Candidate> list) {
        this.list = list;
    }

    public CandidateRepository getCandidateRepository() {
        return candidateRepository;
    }



    public void setCandidateRepository(CandidateRepository candidateRepository) {
        this.candidateRepository = candidateRepository;
    }

    public CandidateService getCandidateService() {
        return candidateService;
    }

    public void setCandidateService(CandidateService candidateService) {
        this.candidateService = candidateService;
    }

    @Override
    public void run() {
        int yearStart = Var.yearStart;
        int yearEnd = Var.yearEnd;
        for(Candidate candidate: list){
            candidate.setYearStart(yearStart);
            candidate.setYearEnd(yearEnd);
            List<String> listCommon=coAuthorShipSimplyService.getCommonNeighbours(candidate.getAuthorId1(),candidate.getAuthorId2(),candidate.getYearStart(),candidate.getYearEnd());
            candidate.setAA(candidateService.AAMeasure(candidate,listCommon, candidate.getYearStart(), candidate.getYearEnd() ));
            candidate.setCN(listCommon.size());
            candidate.setJC(candidateService.JCMeasure(candidate,listCommon, candidate.getYearStart(), candidate.getYearEnd()));
            candidate.setWAA(candidateService.WAAMeasure(candidate,listCommon, candidate.getYearStart(), candidate.getYearEnd()));
            candidate.setWCN(candidateService.WCNMeasure(candidate,listCommon, candidate.getYearStart(), candidate.getYearEnd()));
            candidate.setWJC(candidateService.WJCMeasure(candidate,listCommon ,candidate.getYearStart(), candidate.getYearEnd()));
            candidateRepository.save(candidate);
            System.out.println("new candidate " + candidate);
        }

    }
}
