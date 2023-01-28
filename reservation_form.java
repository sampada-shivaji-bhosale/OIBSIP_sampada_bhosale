package Oasis_intern_tasks;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class reservation_form extends JDialog {
    private JTextField tfFull_name;
    private JTextField tfemail_resrv;
    private JPasswordField pfpassword_resrv;
    private JTextField tfAddress;
    private JTextField tfGender;
    private JButton btnSubmit;
    private JButton btnCancal;
    private JPanel reservePanel;

    public reservation_form(JFrame parent){
        super(parent);
        setTitle("Create a new account");
        setContentPane(reservePanel);
        setMinimumSize(new Dimension(450 ,474));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        btnSubmit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                reserve();
            }
        });
        btnCancal.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        setVisible(true);
    }

    private void reserve() {
        String full_name=tfFull_name.getText();
        String email_resrv=tfemail_resrv.getText();
        String password_resrv=String.valueOf(pfpassword_resrv.getPassword());
        String address=tfAddress.getText();
        String gender=tfGender.getText();

        if(email_resrv.isEmpty() || password_resrv.isEmpty() ||full_name.isEmpty() || address.isEmpty() ||gender.isEmpty())
        {
            JOptionPane.showMessageDialog(this,"please enter all fields","Try again",JOptionPane.ERROR_MESSAGE);
            return;
        }
        user= addToDatabase(full_name,email_resrv,password_resrv,address,gender);
        if(user !=null)
        {
            dispose();
        }
        else {
            JOptionPane.showMessageDialog(this,"Failed to reservation new user","Try again",JOptionPane.ERROR_MESSAGE);
        }
    }
    public Login_user user;
    private Login_user addToDatabase(String full_name, String email_resrv, String password_resrv, String address, String gender) {
        Login_user user=null;
        final String DB_url="jdbc:mysql://localhost/railway_reservation?serverTimezone=UTC";
        final String username ="root";
        final String password_db="";
        try{
            Connection conn = DriverManager.getConnection(DB_url,username,password_db);
            Statement stat=conn.createStatement();
            String sql="INSERT INTO reservation(full_name,email_resrv,password_resrv,address,gender)"+"VALUES(?,?,?,?,?)";
            PreparedStatement preparedStatement =conn.prepareStatement(sql);
            preparedStatement.setString(1,full_name);
            preparedStatement.setString(2,email_resrv);
            preparedStatement.setString(3,password_resrv);
            preparedStatement.setString(4,address);
            preparedStatement.setString(5,gender);


            int addedRows =preparedStatement.executeUpdate();
            if(addedRows>0)
            {
                user=new Login_user();
                user.full_name=full_name;
                user.email_resrv=email_resrv;
                user.password_resrv=password_resrv;
                user.address=address;
                user.gender=gender;
            }
            stat.close();
            conn.close();

        }catch(Exception e){
            e.printStackTrace();
        }
        return user;
    }



    public static void main(String[] args)
    {
        reservation_form obj1 = new reservation_form(null);
        Login_user User=obj1.user;
        if(User !=null)
        {
            System.out.println("Successful registration of :"+User.email_resrv);
            System.out.println("passenger:"+User.full_name);
            System.out.println("address :"+User.address);
        }
        else {
            System.out.println("Registration cancle");
        }
    }
}
