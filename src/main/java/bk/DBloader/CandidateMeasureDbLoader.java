package bk.DBloader;

import bk.model.Candidate;
import bk.model.CoAuthorshipSimply;
import bk.model.Var;
import bk.repository.CandidateRepository;
import bk.thread.CandidateMeasureThread;
import bk.thread.CandidateThread;
import bk.ulti.CandidateService;
import bk.ulti.CoAuthorShipSimplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CandidateMeasureDbLoader implements CommandLineRunner{
    int numThread = 20;
    @Autowired
    CandidateService candidateService;
    @Autowired
    CandidateRepository candidateRepository;
    @Autowired
    CoAuthorShipSimplyService coAuthorShipSimplyService;
    @Transactional
    @Override
    public void run(String... args) throws Exception {
        if(false){
            int yearStart= Var.yearStart;
            int yearEnd = Var.yearEnd;
            List<Candidate> candidateList = candidateRepository.findByCn();
            List<String> checkedIdList = new ArrayList<>();

            Map<Integer, List<Candidate>> listHashMap = splitByHashId(candidateList);

            for(int i = 0; i < numThread; i++){
                //check list id size > 0
                List<Candidate> list = listHashMap.get(i);
                if(list.size()>0) {
                    CandidateMeasureThread candidateThread = new CandidateMeasureThread();
                    candidateThread.setList(list);
                    candidateThread.setCoAuthorShipSimplyService(coAuthorShipSimplyService);
                    candidateThread.setCandidateService(candidateService);
                    candidateThread.setCandidateRepository(candidateRepository);
                    Thread thread = new Thread(candidateThread);
                    thread.start();
                }

            }
        }
    }


    Map<Integer,List<Candidate>> splitByHashId(List<Candidate> candidateList){
        Map<Integer,List<Candidate>> map = new HashMap<>();
        for(Candidate candidate : candidateList){
            int k = (int) (candidate.getId() % numThread);
            List<Candidate> coAuthorshipSimplies = map.get(k);
            if(coAuthorshipSimplies == null )
            {
                coAuthorshipSimplies = new ArrayList<>();
            }
            coAuthorshipSimplies.add(candidate);
            map.put(k,coAuthorshipSimplies);
        }
        return map;
    }
}
