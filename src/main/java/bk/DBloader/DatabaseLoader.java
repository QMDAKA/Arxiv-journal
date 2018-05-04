package bk.DBloader;

import bk.model.Author;
import bk.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.sql.*;

/**
 * Created by quangminh on 16/11/2017.
 */
@Component
public class DatabaseLoader implements CommandLineRunner {

    @Autowired
    AuthorRepository authorRepository;


    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if(false) {
            System.out.println("-------- MySQL JDBC Connection Testing ------------");

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
                sql = "SELECT * FROM author";
                rs = stmt.executeQuery(sql);
            } catch (SQLException e) {
                System.out.println("Connection Failed! Check output console");
                e.printStackTrace();
                return;
            }

            while (rs.next()) {
                //Retrieve by column name
                try {
                    Author author = new Author();
                    author.setId(rs.getString("id"));
                    author.setGivenName(rs.getString("givenName"));
                    author.setSurname(rs.getString("surname"));
                    author.setEmail(rs.getString("email"));
                    author.setAffiliation(rs.getString("affiliation"));
                    author.setSubject(rs.getString("subjects"));
                    author.setUrl(rs.getString("url"));
                    authorRepository.save(author);
                } catch (Exception e) {
                    System.out.println(e);
                }
            }

        }
    }
}
