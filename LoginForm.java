package Oasis_intern_tasks;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;


public class LoginForm extends JDialog {
    int count=0;
    private JTextField tfemail;
    private JPasswordField pfConfirm;
    private JButton btnlogin;
    private JButton btncancle;
    private JPanel loginPanel;
    private JPasswordField pfPassword;
    private JButton accountButton;
    private Login_user logUser;

    public LoginForm(JFrame parent){
        super(parent);
        setTitle("Create a new account");
        setContentPane(loginPanel);
        setMinimumSize(new Dimension(450 ,474));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        btnlogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email=tfemail.getText();
                String password=String.valueOf(pfPassword.getPassword());
                user=getAuthenticatedUser(email,password);
                if(user!=null)
                {
                    dispose();
                }
                else {
                    JOptionPane.showMessageDialog(LoginForm.this,"Email or password invalid","Try again",JOptionPane.ERROR_MESSAGE);
                }
                //login();
            }

        });
        btncancle.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        accountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login();
            }
        });
        setVisible(true);
    }
    public Login_user user;
    private Login_user getAuthenticatedUser(String email, String password) {
        Login_user user=null;
        final String DB_url="jdbc:mysql://localhost/railway_reservation?serverTimezone=UTC";
        final String username ="root";
        final String password_db="";
        try{
            Connection conn = DriverManager.getConnection(DB_url,username,password_db);
            Statement stat=conn.createStatement();
            String sql="SELECT * FROM login WHERE email=? AND password=?";
            PreparedStatement preparedStatement =conn.prepareStatement(sql);
            preparedStatement.setString(1,email);
            preparedStatement.setString(2,password);
            ResultSet result=preparedStatement.executeQuery();
            if(result.next()){
                user=new Login_user();
                user.email=result.getString("email");
                user.password=result.getString("password");
                user.confirm_password=result.getString("Confirm password");
            }
            /*int addedRows =preparedStatement.executeUpdate();
            if(addedRows>0)
            {
                user=new Login_user();
                user.email=email;
                user.password=password;
            }*/

            stat.close();
            conn.close();

        }catch(Exception e){
            e.printStackTrace();
        }
        return user;

    }


    private void login() {
        String email=tfemail.getText();
        String password=String.valueOf(pfPassword.getPassword());
        String confirm_password=String.valueOf(pfConfirm.getPassword());

        if(email.isEmpty() || password.isEmpty() ||confirm_password.isEmpty())
        {
            JOptionPane.showMessageDialog(this,"please enter all fields","Try again",JOptionPane.ERROR_MESSAGE);
            return;
        }
        if(!password.equals(confirm_password)){
            JOptionPane.showMessageDialog(this,"Confirm password does not match","Try again",JOptionPane.ERROR_MESSAGE);
            return;
        }
        user= addUserToDatabase(email,password,confirm_password);

        if(user !=null)
        {
            count=1;
            dispose();
        }
        else {
            JOptionPane.showMessageDialog(this,"Failed to register new user","Try again",JOptionPane.ERROR_MESSAGE);
        }
    }


    private Login_user addUserToDatabase(String email, String password, String confirm_password) {
        Login_user user=null;
        final String DB_url="jdbc:mysql://localhost/railway_reservation?serverTimezone=UTC";
        final String username ="root";
        final String password_db="";
        try{
            Connection conn = DriverManager.getConnection(DB_url,username,password_db);
            Statement stat=conn.createStatement();
            String sql="INSERT INTO login(email,password,confirm_password)"+"VALUES(?,?,?)";
            PreparedStatement preparedStatement =conn.prepareStatement(sql);
            preparedStatement.setString(1,email);
            preparedStatement.setString(2,password);
            preparedStatement.setString(3,confirm_password);

            int addedRows =preparedStatement.executeUpdate();
            if(addedRows>0)
            {
                user=new Login_user();
                user.email=email;
                user.password=password;
                user.confirm_password=confirm_password;
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
        LoginForm obj = new LoginForm(null);
        Login_user User=obj.user;
        if(obj.count==0) {
            if (User != null) {
                System.out.println("Authenticated user :" + User.email);
            } else {
                System.out.println("registation cancel");
            }
        }
        else {
            if (User != null) {
                System.out.println("Account created Sucessfully  Now you can reserv the train:" + User.email);
            } else {
                System.out.println("registation cancel");
            }
        }

    }
}
