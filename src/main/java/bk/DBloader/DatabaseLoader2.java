package bk.DBloader;

import bk.model.Author;
import bk.model.Paper;
import bk.repository.AuthorRepository;
import bk.repository.PaperRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by quangminh on 16/11/2017.
 */
@Component
public class DatabaseLoader2 implements CommandLineRunner {
    @Autowired
    PaperRepository paperRepository;

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
                sql = "SELECT * FROM paper";
                rs = stmt.executeQuery(sql);
            } catch (SQLException e) {
                System.out.println("Connection Failed! Check output console");
                e.printStackTrace();
                return;
            }

            while (rs.next()) {
                //Retrieve by column name
                try {
                    Paper paper = new Paper();
                    paper.setId(rs.getString("id"));
                    paper.setDate(rs.getDate("coverDate"));
                    paper.setPaperAbstract(rs.getString("abstract"));
                    paper.setTitle(rs.getString("title"));
                    paper.setUrl(rs.getString("url"));
                    paper.setIssn(rs.getString("issn"));
                    paper.setKeywords(rs.getString("keywords"));
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(paper.getDate());
                    int year = cal.get(Calendar.YEAR);
                    paper.setYear(year);
                    paperRepository.save(paper);
                } catch (Exception e) {
                    System.out.println(e);
                }
            }


        }
    }
}