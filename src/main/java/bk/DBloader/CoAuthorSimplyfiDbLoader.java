package bk.DBloader;

import bk.model.CoAuthorship;
import bk.model.CoAuthorshipSimply;
import bk.model.Var;
import bk.repository.CoAuthorshipRepository;
import bk.repository.CoAuthorshipSimplyRepository;
import bk.ulti.CoAuthorShipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CoAuthorSimplyfiDbLoader implements CommandLineRunner {

    @Autowired
    CoAuthorShipService coAuthorShipService;
    @Autowired
    CoAuthorshipRepository coAuthorshipRepository;
    @Autowired
    CoAuthorshipSimplyRepository coAuthorshipSimplyRepository;

    @Override
    public void run(String... args) throws Exception {
        if (false) {
            int yearStart = Var.yearStart;
            int yearEnd = Var.yearEnd;
            for (CoAuthorship coAuthorship : coAuthorshipRepository.findByFlag(false,yearStart,yearEnd)) {
                try {

                    String id1 = coAuthorship.getAuthorId1();
                    String id2 = coAuthorship.getAuthorId2();
                    //check xem db CoAuthorshipSimply da ton tai cap Id nay chua
                    CoAuthorshipSimply checkCoAuthorshipSimply = coAuthorshipSimplyRepository.findByAuthorId1OrAuthorId2(id1,id2);
                    if (checkCoAuthorshipSimply == null) {
                        CoAuthorshipSimply coAuthorshipSimply = new CoAuthorshipSimply();
                        coAuthorshipSimply.setAuthorId1(id1);
                        coAuthorshipSimply.setAuthorId2(id2);
                        coAuthorshipSimply.setFlag(false);
                        coAuthorshipSimply.setLastYear(coAuthorshipRepository.findMaxYear(
                                id1,id2,yearStart,yearEnd
                        ));
                        int lastYear = coAuthorshipSimply.getLastYear();
                        coAuthorshipSimply.setCoAuthorshipTime(coAuthorshipRepository.findCommonArticle(
                                id1,id2,yearStart, lastYear
                        ));
                        coAuthorshipSimplyRepository.save(coAuthorshipSimply);
                    }
                    else{
                        System.out.println("LMAO YOU");
                    }
                    coAuthorship.setFlag(true);
                    coAuthorshipRepository.save(coAuthorship);
                } catch (Exception ex) {
                    System.out.println(ex);
                }
            }
        }
    }
}
