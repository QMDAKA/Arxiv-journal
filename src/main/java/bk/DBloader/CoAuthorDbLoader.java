package bk.DBloader;

import bk.model.Author;
import bk.model.CoAuthorship;
import bk.model.Paper;
import bk.repository.CoAuthorshipRepository;
import bk.repository.PaperRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by quangminh on 19/11/2017.
 */
@Component
public class CoAuthorDbLoader implements CommandLineRunner{
    @Autowired
    PaperRepository paperRepository;
    @Autowired
    CoAuthorshipRepository coAuthorshipRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if(false) {
            for (Paper paper : paperRepository.findAll()) {
                if (coAuthorshipRepository.findCountArticle(paper.getId()) == 0) {
                    List<String> authors = new ArrayList<>();
                    for (Author author : paper.getAuthors()) {
                        authors.add(author.getId());
                    }
                    makeCoRelashionship(authors,paper);
                }
            }
        }
    }

    void makeCoRelashionship(List<String> list,Paper paper){
        if(list.size()==1){
            return;
        }
        else {
            for (int i = 0; i < list.size(); i++) {
                for (int j = i + 1; j < list.size(); j++) {
                    CoAuthorship coAuthorship = new CoAuthorship();
                    coAuthorship.setAuthorId1(list.get(i));
                    coAuthorship.setAuthorId2(list.get(j));
                    coAuthorship.setYear(paper.getYear());
                    coAuthorship.setPaperId(paper.getId());
                    coAuthorshipRepository.save(coAuthorship);
                }
            }
        }
    }
}
