/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Kyle
 */
public class DatabaseResults {
    
    public static ResultSet getAllBoards() {
        try (Connection conn = credentials.Credentials.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM board;");
            return pstmt.executeQuery();
        }
        catch (SQLException ex) {
            return null;
        }
    }
}
