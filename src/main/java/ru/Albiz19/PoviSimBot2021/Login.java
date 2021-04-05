package ru.Albiz19.PoviSimBot2021;

import java.sql.*;

public class Login {
    private Connection con;
    private Statement st;
    private ResultSet rs;
    private int count = 1;

    Login() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/povisimdb?serverTimezone=UTC", "root", "");
            System.out.println(con);
            st = con.createStatement();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /* public String addWord(String input_word){
         String tmp;
         String sql = "INSERT INTO wordpull(words) VALUE(?)";
         try{
             PreparedStatement st = con.prepareStatement(sql);
             st.setString(1, input_word);

         } catch (SQLException e){
             System.out.println(e);
             return "Error " + e;
         }
         return "Successfully added word";
     }
     */
    public String getWord() {
        String query = "SELECT words FROM wordpull ORDER BY rand() LIMIT 1";
        String tmp = "";
        try {
            PreparedStatement st = con.prepareStatement(query);
            rs = st.executeQuery();
            rs.next();
            tmp = rs.getString("words");
        } catch (SQLException e) {
            System.out.println(e);
        }
        return tmp;
    }
}

