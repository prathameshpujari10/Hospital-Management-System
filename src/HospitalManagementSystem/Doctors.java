package HospitalManagementSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Doctors {
    private Connection connection;


    public Doctors(Connection connection)
    {
        this.connection=connection;

    }


    public void viewDoctors()
    {
        String query = "select * from doctors";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            System.out.println("Doctors : ");
            System.out.println("+------------+--------------+---------------------+--------+");
            System.out.println("| Doctors ID | Name         | specialization      | Age    |");
            System.out.println("+------------+--------------+---------------------+--------+");

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String specialization = resultSet.getString("specialization");
                int age = resultSet.getInt("age");
                System.out.printf("|%-12s|%-14s|%-21s|%-8s|\n", id, name, specialization, age);
                System.out.println("+------------+---------------+---------------------+--------+");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public boolean getDoctorById(int id)
    {
        String query= "SELECT * FROM doctors WHERE id = ?";
        try{
            PreparedStatement preparedStatement=connection.prepareStatement(query);
            preparedStatement.setInt(1,id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next())
            {
                return true;
            }
            else {
                return false;
            }
        }catch (SQLException e)
        {
            e.printStackTrace();
        }
        return false;
    }
}
