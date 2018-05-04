package bk.thread;

import bk.model.CoAuthorshipSimply;
import bk.model.Var;
import bk.repository.CoAuthorshipRepository;
import bk.ulti.CandidateService;
import bk.ulti.CoAuthorShipService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;
import java.util.List;

public class CandidateThread implements Runnable{

    CoAuthorShipService coAuthorShipService;

    public CandidateService getCandidateService() {
        return candidateService;
    }

    public void setCandidateService(CandidateService candidateService) {
        this.candidateService = candidateService;
    }

    @Autowired
    CoAuthorshipRepository coAuthorshipRepository;
    @Autowired
    CandidateService candidateService;


    List<String> list;

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    @Transactional
    @Override
    public void run() {
        int yearStart = Var.yearStart;
        int yearEnd = Var.yearEnd;
        for(String id: list){
            candidateService.saveCandidateFromPartnerResult(id,yearStart,yearEnd);
        }

    }
}
