package com.company;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.DriverManager;
import java.util.Date;
import java.text.SimpleDateFormat;


public class Main {

    public static Statement statement=null,statement1=null;
    public static  Connection connection=null;
    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/kapturecrm";
          
//             String url = "jdbc:mysql://localhost:3306/kapturecrm";||  Enter the database link

            //---------------------------------------------------------Longest Call---------------------------------------------------

            connection = DriverManager.getConnection(url,"root","root");
//             connection = DriverManager.getConnection(url,"root_username","root_password");
          
          
            statement = connection.createStatement();


            ResultSet rs = (statement.executeQuery("SELECT Start_Time,End_Time FROM call_center_abc WHERE Duration = (SELECT MAX(Duration) FROM call_center_abc)"));
            int val1=0,val2=0;
            drawBreaker();
            System.out.println("Longest Call/Call's: ");
            drawBreaker();
            while(rs.next()) {
                val1 = Integer.parseInt(getHourValue(String.valueOf(rs.getString("Start_Time"))));
                val2 = Integer.parseInt(getHourValue(String.valueOf(rs.getString("End_Time"))))+1;
                System.out.println(getDateValue(String.valueOf(rs.getString("Start_Time"))));
                System.out.println(timeformat(val1)+" - "+timeformat(val2));
                System.out.println(getDayofWeek(String.valueOf(rs.getString("Start_Time")))+"\n");
            }

            //---------------------------------------------------Highest Call Volume-----------------------------------------------

            statement1 = connection.createStatement(
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY
            );

            ResultSet resultHighest = (statement1.executeQuery(
                    "select CAST(Start_Time AS DATE),count(*) FROM call_center_abc  group by CAST(Start_Time AS DATE) ORDER BY count(*) desc LIMIT 20000;")
            );

            System.out.println("Highest Volume of Calls On : ");

            resultHighest.first();

            System.out.println(resultHighest.getString(1));
            System.out.println(resultHighest.getString(2));
            System.out.println(getDayofWeek(String.valueOf(resultHighest.getString(1)))+"\n");


        }
        catch(Exception e) {
            System.out.println(e);
        }


    }

    private static String getHourValue(String val){
        val = val.substring(val.lastIndexOf(" ")+1);
        val = val.substring(0, val.indexOf(":"));
        return val;
    }
    private static String getDateValue(String val){
        val = val.substring(0, val.indexOf(" "));
        return val;
    }
    private static String getDayValue(String val){
        val = val.substring(0, val.indexOf(" "));
        return val+"-day";
    }

    private static String getDayofWeek(String value){
        String val="";
        SimpleDateFormat sf = new  SimpleDateFormat("yyyy-MM-dd");
        try {

            Date datee =sf.parse(value);
            val = getDayValue(String.valueOf(datee));
        }
        catch (Exception e){
            System.out.println(e);
        }

        return val;
    }

    private static String timeformat(int val){
        if(val==00){
            return "12 AM";
        }
        else if(val==12){
            return "12 PM";
        }
        else if(val<12){
            return String.valueOf(val) + " AM";
        }

        else{
            return String.valueOf(12-val) + " PM";
        }
    }
}
