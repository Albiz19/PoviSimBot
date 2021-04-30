package ru.Albiz19.PoviSimBot2021.res;


import java.sql.*;

public class Login {
    private Connection con;
    private Statement st;
    private ResultSet rs;
    private int count = 1;

    public Login() {
        try {
            //Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(
                    //------! This string requires database link input !-------
                    "jdbc:mysql://localhost:3306/povisimdb?serverTimezone=UTC", //insert here
                    //insert from    ^                       ^ to here
                    "root", "");
            System.out.println(con);
            st = con.createStatement();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
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

