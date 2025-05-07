import com.microsoft.sqlserver.jdbc.SQLServerDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class ZadTransakcije1 {
    public static void main(String[] args) {
        DataSource dataSource = createDataSource();
        try (Connection connection = dataSource.getConnection()) {
            System.out.println("Uspjesno ste spojeni na bazu podataka!");

            // try - catch blok za isvrsavanje transakcije
            try (Statement stmt1 = connection.createStatement(); Statement stmt2 = connection.createStatement()) {
                connection.setAutoCommit(false); // iskljucivanje automatskog commita transakcije
                stmt1.executeUpdate("UPDATE Stavka SET CijenaPoKomadu = 38.8404 WHERE IDStavka = 8");
                stmt2.executeUpdate("UPDATE Stavka SET CijenaPoKomadu = 18.8404 WHERE IDStavka = 9");


                connection.commit();
                System.out.println("Transakcija izvrsena!");

            } catch (SQLException e) {
                connection.rollback();
                System.err.println("Transakcija ponistena!");
            }

        } catch (SQLException e) {
            System.out.println("Greska pri povezivanju na bazu podataka!");
            e.printStackTrace();
        }


    }

    private static DataSource createDataSource() {
        SQLServerDataSource ds = new SQLServerDataSource();
        ds.setServerName("localhost");
        //ds.setPortNumber(1433);
        ds.setDatabaseName("AdventureWorksOBP");
        ds.setUser("sa");
        ds.setPassword("SQL");
        ds.setEncrypt(false);
        return ds;
    }
}
