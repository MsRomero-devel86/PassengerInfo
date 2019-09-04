package passengerBean;

import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import java.sql.Types;

/**
 *
 * @author Meghan
 */
@Named(value = "db")
@ApplicationScoped
public class DatabaseBean implements Serializable
{
    String pname;
    int pid;
    int age;
    String result;

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getConnectionResponse()
    {
        Connection con = connection();
        if (con == null)
          {
            result = "cannot connect to database";
            return null;
          }
        if (con != null)
          {
            return "<p style=\"color:green\">Connection succesfull! <br />";
          }
        else
          {
            connection();
            return "<p style=\"color:red\">Connection failed! <br />";
          }

    }

    private Connection connection() //throws InstantiationException, IllegalAccessException
    {

        String databaseName = "travel";
        String userName = "root";
        String password = "root";
        String URL2 = "com.mysql.jdbc.Driver";
        Connection con = null;
        try
          {// Load Sun's jdbc driver
            Class.forName(URL2).newInstance();
            System.out.println("JDBC Driver loaded!");
          }
        catch (Exception e) // driver not found
          {
            System.err.println("Unable to load database driver");
            System.err.println("Details : " + e);
            return null;
          }
        String ip = "localhost"; //internet connection
        String url = "jdbc:mysql://" + ip + ":3308/" + databaseName;
        try
          {
            con = DriverManager.getConnection(url, userName, password);
            con.setReadOnly(false);
          }
        catch (Exception e)
          {
            System.err.println(e.toString());
            return null;
          }
        System.out.println("connection successfull");
        return con;
    }

    public void clear()
    {   pname = "";
        pid = 0;
        age=0;
        result = "";
    }

    public void listAll()
    {
        Connection con = connection();
        if (con == null)
          {
            result = "cannot connect to database";
            return;
          }
        String table = "passengers";
        PreparedStatement ps = null;
        ResultSet rs =  null;
        String sqlStr = "SELECT  *  FROM passenger";
        try
          {
            //prepare statement
            ps = con.prepareStatement(sqlStr);
            //execute
            rs = ps.executeQuery();
            while (rs.next())
              {
                int pid = rs.getInt(1) ;
                String pname = rs.getString(2) + " ";
                int age = rs.getInt(3);
                
                table += pid+ pname + age + "</br>";
              }
          }
        catch (Exception ex)
          {
            ex.printStackTrace();
          }
        finally
          {
            try
              {
                closeDatabaseConnection(con);
                // close the resources 
                if (ps != null)
                  {
                    ps.close();
                  }
              }
            catch (SQLException sqle)
              {
                sqle.printStackTrace();
              }
          }
        result = table;
    }

    public void viewSupplier()
    {
        Connection con = connection();
        if (con == null)
          {
            result = "cannot connect to database";
            return;
          }
        String ret = "";
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sqlStr = "SELECT *  FROM passenegrs WHERE pname= SELECT SUBSTRING(@string, CHARINDEX(' ', @string) +1, 20)";
        try
          {
            //prepare statement
            ps = con.prepareStatement(sqlStr);
            ps.setInt(1, pid);
            //execute
            rs = ps.executeQuery();
            if (rs.next())
              {
                this.pid = rs.getInt("pid");
                ret += this.pid + " ";
                this.pname = rs.getString("pname");
                ret += this.pname + " ";
                this.age = rs.getInt("age");
                

              }
            else
              {
                ret = this.pid + " doesn't exist.";
              }
          }
        catch (Exception ex)
          {
            ex.printStackTrace();
          }
        finally
          {
            try
              {
                this.closeDatabaseConnection(con);
                if (ps != null)
                  {
                    ps.close();
                  }
              }
            catch (SQLException sqle)
              {
                sqle.printStackTrace();
              }
          }
        this.result = ret;
    }

    public String getResult()
    {
        return "<p style=\"color:green\">passenger <br />" + result;

    }

    public void closeDatabaseConnection(Connection con)
    {
        try
          {
            if (con != null)
              {
                con.close();
              }
          }
        catch (SQLException e)
          {
            result = e.toString();
            e.printStackTrace();
          }
    }


    public void callgSQLprocedure()
    {
        String fromDatabase = "travel";
        Connection con = connection();
        CallableStatement callProcedure = null;
        if (con == null)
          {
            result = "cannot connect to database";
            return;
          }
        try
          {
            callProcedure = con.prepareCall("{call demo_sp(?, ?)}");
            callProcedure.registerOutParameter(2, Types.INTEGER);
            callProcedure.setString(1, "this is coming from java code");
            callProcedure.setInt(2, 1000);

            // Alternatively, set the 'in/out' parameter
            //callProcedure.setInt("inOutParam", 1); 
            boolean resultsFromCall = callProcedure.execute();
            while (resultsFromCall)
              {
                //here process the results
                //fromDatabase =  resultsFromCall.
                resultsFromCall = callProcedure.getMoreResults();
              }
                     
            int outputValue1 = callProcedure.getInt(2); // index-based

            int outputValueCopy = callProcedure.getInt("inOutParam"); // name-based
             result =  " the results from callling demo_sp(() are: " 
                        + Integer.toString(outputValue1 ) + ", " + 
                         Integer.toString(outputValueCopy );
          }
        catch (Exception ex)
          {
            System.err.println(ex.toString());
            result =  ex.toString();
          }
        finally
          {
            try
              {
                this.closeDatabaseConnection(con);
                // close the resources 
                if (callProcedure != null)
                  {
                    callProcedure.close();
                  }

              }
            catch (SQLException e)
              {
                System.err.println(e.toString());
                result = e.toString();
              }
          }

    }
      
    
}
