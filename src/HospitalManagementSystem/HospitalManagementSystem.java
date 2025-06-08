package HospitalManagementSystem;

import java.sql.*;
import java.util.Scanner;

import static java.lang.Class.forName;

public class HospitalManagementSystem {
    private static final String url ="jdbc:mysql://localhost:3306/hospital";
    private static final String username="root";
    private static final String password="root12345";

    public static void main(String[] args) {
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
        }catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        Scanner scanner =new Scanner(System.in);
        try {
            Connection connection= DriverManager.getConnection(url,username,password);
            Patient patient=new Patient(connection,scanner);
            Doctors doctors=new Doctors(connection);

            while(true)
            {
                System.out.println("HOSPITAL MANAGEMENT SYSTEM");
                System.out.println("1. Add Patients");
                System.out.println("2. View Patients");
                System.out.println("3. View Doctors");
                System.out.println("4. Book Appointment");
                System.out.println("5. Exit");
                System.out.println("Enter Your Choice");

                int choice =scanner.nextInt();


                switch (choice)
                {
                    case 1:
                        //add patient
                        patient.addPatient();
                        System.out.println();
                        break;
                    case 2:
                        // view patient
                        patient.viewPatients();
                        System.out.println();
                        break;
                    case 3:
                        // voiw doctors
                        doctors.viewDoctors();
                        System.out.println();
                        break;
                    case 4:
                        //Book appointment
                        bookAppointment(patient,doctors,connection,scanner);
                        System.out.println();
                        break;
                    case 5:
                        System.out.println("THANK YOU FOR USING HOSPITAL MANAGEMENT SYSTEM ");
                       return;
                    default:
                        System.out.println("Enter valid choice ");
                        break;
                }
            }

        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }

    public static void bookAppointment(Patient patient,Doctors doctors,Connection connection,Scanner scanner)
    {
        System.out.println("Enter patient ID ");
        int patientId =scanner.nextInt();
        System.out.println("Enter Doctors ID ");
        int doctorsId= scanner.nextInt();
        System.out.println("Enter Appointment Date (YYYY:MM:DD): ");
        String appointmentDate= scanner.next();

        if(patient.getPatientById(patientId) && doctors.getDoctorById(doctorsId))
        {
            if(checkDoctorsAvailability(doctorsId,appointmentDate,connection))
            {
                String appiontmentquery ="INSERT INTO appoinments(patient_id,doctor_id, appointment_date) VALUES(?,?,?)";
                try
                {
                    PreparedStatement preparedStatement=connection.prepareStatement(appiontmentquery);
                    preparedStatement.setInt(1,patientId);
                    preparedStatement.setInt(2,doctorsId);
                    preparedStatement.setString(3,appointmentDate);

                    int rowsAffected =preparedStatement.executeUpdate();
                    if(rowsAffected>0)
                    {
                        System.out.println("Appointment Booked successfully");
                    }
                    else {
                        System.out.println("Failed to Book Appointment");
                    }
                }catch(SQLException e)
                {
                    e.printStackTrace();
                }
            }
        }
        else {
            System.out.println("Either Doctor or Patient is doesn't exit  ");
        }
    }

    public static boolean checkDoctorsAvailability(int doctorId, String appointmentDate,Connection connection)
    {
       String query="SELECT COUNT(*) FROM appoinments WHERE doctor_id = ? AND appointment_date = ?";
       try {
           PreparedStatement preparedStatement = connection.prepareStatement(query);
           preparedStatement.setInt(1,doctorId);
           preparedStatement.setString(2,appointmentDate);
           ResultSet resultSet=preparedStatement.executeQuery();
           if (resultSet.next())
           {
               int count =resultSet.getInt(1);
               if (count==0)
               {
                   return true;
               }
               else {
                   return false;
               }
           }
       }catch (SQLException e)
       {
           e.printStackTrace();
       }
       return false;
    }

}
