package Oasis_intern_tasks;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class deshboard extends JFrame {
    private JButton btnreserv;
    private JLabel lbwelcome;
    private JLabel lbreservation;
    private JLabel lbresrv;
    private JPanel deshboardpanal;

    public deshboard(){
        setTitle("Deshboard");
        setContentPane(deshboardpanal);
        setMinimumSize(new Dimension(500,429));
        setSize(1200,700);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        boolean hasRegistredUsers= connectToDatabase();
        if(!hasRegistredUsers)
        {
            LoginForm obj=new LoginForm(this);
            Login_user User=obj.user;

            if(User!=null){
                lbresrv.setText("user"+User.email);
                setLocationRelativeTo(null);
                setVisible(true);
            }
            else {
                dispose();
            }
        }
        else {
            reservation_form reserv=new reservation_form(this);
            Login_user User=reserv.user;
            if(User!=null)
            {
                lbresrv.setText("YES NOW YOU CAN RESERV THE TRAIN  "+User.email);
                setLocationRelativeTo(null);
                setVisible(true);
            }
            else {
                dispose();
            }
        }
        btnreserv.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                reservation_form reserv =new reservation_form(deshboard.this);
                Login_user user =reserv.user;
                if(user !=null)
                {
                    JOptionPane.showMessageDialog(deshboard.this,"New user","Sucessful reservation",JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
    }

    private boolean connectToDatabase() {
        boolean  hasRegistredUsers =false;
        final String server="jdbc:mysql://localhost/";
        final String DB_URL="jdbc:mysql://localhost/railway_reservation?serverTimezone=UTC";
        final String USERNAME ="root";
        final String PASSWORD_DB="";
        try
        {
            Connection conn= DriverManager.getConnection(server,USERNAME,PASSWORD_DB);
            Statement stat=conn.createStatement();
            stat.executeUpdate("CREATE DATABASE IF NOT railway_reservation");
            stat.close();
            conn.close();

            conn=DriverManager.getConnection(DB_URL,USERNAME,PASSWORD_DB);
            stat =conn.createStatement();
            String sql="CREATE TABLE IF NOT EXISTS user("
                    +"id INT(10) NOT NULL PRIMARY KEY AUTO_INCREMENT,"
                    +"email VARCHAR(50) NOT NULL,"
                    +"password VARCHAR(10) NOT NULL,"
                    +"confirm password VARCHAR(10) NOT NULL"
                    +")";
            stat.executeUpdate(sql);
            stat=conn.createStatement();
            ResultSet resultSet=stat.executeQuery("SELECT COUNT(*) FROM login");
            if(resultSet.next()){
                int numuser=resultSet.getInt(1);
                if(numuser>0){
                    hasRegistredUsers=true;
                }
            }
            stat.close();
            conn.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return hasRegistredUsers;
    }
    public static void main(String[] args)
    {
        deshboard obj=new deshboard();
    }
}
