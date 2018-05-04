package bk.DBloader;

import bk.model.CoAuthorship;
import bk.model.CoAuthorshipSimply;
import bk.model.Var;
import bk.repository.CoAuthorshipRepository;
import bk.thread.CandidateThread;
import bk.ulti.CandidateService;
import bk.ulti.CoAuthorShipService;
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
public class CandidateDbLoader implements CommandLineRunner {
    int numThread = 19;
    @Autowired
    CoAuthorShipSimplyService coAuthorShipSimplyService;
    @Autowired
    CoAuthorshipRepository coAuthorshipRepository;
    @Autowired
    CandidateService candidateService;

    @Transactional
    @Override
    public void run(String... args) throws Exception {
        if (false) {
            List<String> checkedIdAuthor = new ArrayList<>();
            int yearStart= Var.yearStart;
            int yearEnd = Var.yearEnd;
            List<CoAuthorshipSimply> coAuthorshipList = coAuthorShipSimplyService.findByLastYearBetween(yearStart,yearEnd);
            List<String> processedList = candidateService.findProcessAuthorId();
            for (CoAuthorshipSimply coAuthorship : coAuthorshipList) {
                if(checkedIdAuthor.size()> 1000)
                {
                    break;
                }
                if (!checkedIdAuthor.contains(coAuthorship.getAuthorId1()) && !processedList.contains(coAuthorship.getAuthorId1())) {
                    checkedIdAuthor.add(coAuthorship.getAuthorId1());
                }
                if (!checkedIdAuthor.contains(coAuthorship.getAuthorId2()) && !processedList.contains(coAuthorship.getAuthorId2())) {
                    checkedIdAuthor.add(coAuthorship.getAuthorId2());
                }
            }


            Map<Integer, List<String>> listHashMap = splitByHashId(checkedIdAuthor);

            for(int i = 0; i < numThread; i++){
                //check list id size > 0
                List<String> list = listHashMap.get(i);
                if(list.size()>0) {
                    CandidateThread candidateThread = new CandidateThread();
                    candidateThread.setList(list);
                    candidateThread.setCandidateService(candidateService);
                    Thread thread = new Thread(candidateThread);
                    thread.start();
                }

            }
            /*for (CoAuthorship coAuthorship : coAuthorshipList) {
                if (!checkedIdAuthor.contains(coAuthorship.getAuthorId1())) {
                    checkedIdAuthor.add(coAuthorship.getAuthorId1());
                    candidateService.saveCandidateFromPartnerResult(coAuthorship.getAuthorId1(),yearStart,yearEnd);

                }
                if (!checkedIdAuthor.contains(coAuthorship.getAuthorId2())) {
                    checkedIdAuthor.add(coAuthorship.getAuthorId2());
                    candidateService.saveCandidateFromPartnerResult(coAuthorship.getAuthorId1(),yearStart,yearEnd);
                }
            }*/

        }
    }

    Map<Integer,List<String>> splitByHashId(List<String> listId){
        Map<Integer,List<String>> map = new HashMap<>();
        for(String id : listId){
            int k = (int) (Long.parseLong(id) % numThread);
            List<String> coAuthorshipSimplies = map.get(k);
            if(coAuthorshipSimplies == null )
            {
                coAuthorshipSimplies = new ArrayList<>();
            }
            coAuthorshipSimplies.add(id);
            map.put(k,coAuthorshipSimplies);
        }
        return map;
    }

}
