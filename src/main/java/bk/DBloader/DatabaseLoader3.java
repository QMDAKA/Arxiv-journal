package bk.DBloader;

import bk.model.Author;
import bk.model.Paper;
import bk.repository.AuthorRepository;
import bk.repository.PaperRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.persistence.Entity;
import javax.transaction.Transactional;
import java.sql.*;

/**
 * Created by quangminh on 18/11/2017.
 */
@Component
public class DatabaseLoader3 implements CommandLineRunner {
    @Autowired
    PaperRepository paperRepository;
    @Autowired
    AuthorRepository authorRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (false) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                System.out.println("Where is your MySQL JDBC Driver?");
                e.printStackTrace();
                return;
            }

            System.out.println("MySQL JDBC Driver Registered!");
            Connection connection = null;
            ResultSet rs;
            try {
                connection = DriverManager
                        .getConnection("jdbc:mysql://localhost:3306/arxiv2", "root", "");
                Statement stmt = connection.createStatement();
                String sql;
                sql = "SELECT * FROM author_paper limit 20000 offset 100000";
                rs = stmt.executeQuery(sql);
            } catch (SQLException e) {
                System.out.println("Connection Failed! Check output console");
                e.printStackTrace();
                return;
            }
            while (rs.next()) {
                try {
                    Paper paper = paperRepository.findById(rs.getString("paperid"));
                    Author author = authorRepository.findById(rs.getString("authorid"));

                    if (author != null && paper != null) {
                        if(paper.getAuthors().indexOf(author)==-1) {
                            author.getPapers().add(paper);
                            paper.getAuthors().add(author);
                            authorRepository.save(author);
                            paperRepository.save(paper);
                        }
                    }
                } catch (Exception err) {
                    System.out.println(err);
                }
            }
        }
    }
}
