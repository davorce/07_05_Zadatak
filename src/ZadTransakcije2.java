import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import javax.sql.DataSource;
import java.sql.*;
import java.util.Scanner;

public class ZadTransakcije2 {

    public static void main(String[] args) {
        DataSource dataSource = createDataSource();
        Scanner scan = new Scanner(System.in);

        try (Connection connection = dataSource.getConnection()) {
            System.out.println("Uspjesno ste spojeni na bazu podataka!");

            System.out.println("Unesi ID stavke ciju cijenu po komadu zelis promijeniti: ");
            int idStavke = scan.nextInt();

            double tempCijena;
            tempCijena = trenutnaCijena(connection, idStavke);
            if (tempCijena == -1) {
                System.out.println("Stavka s IDem " + idStavke + " ne postoji!");
                return;
            }

            System.out.println("Trenutna cijena stavke: " + tempCijena);
            System.out.println("Unesi novu cijenu: ");
            double novaCijena = scan.nextDouble();


            // try - catch blok za isvrsavanje transakcije
            try (Statement stmt = connection.createStatement()) {
                connection.setAutoCommit(false); // iskljucivanje automatskog commita transakcije
                stmt.executeUpdate("UPDATE Stavka SET CijenaPoKomadu = " + novaCijena + "WHERE IDStavka = " + idStavke);

                connection.commit();
                System.out.println("Transakcija izvrsena! Cijena promijenjena!");

            } catch (SQLException e) {
                connection.rollback();
                System.err.println("Transakcija ponistena!");
            }

        } catch (SQLException e) {
            System.out.println("Greska pri povezivanju na bazu podataka!");
            e.printStackTrace();
        }


    }

    private static double trenutnaCijena(Connection conn, int idStavke) {
        String sql = "SELECT CijenaPoKomadu FROM Stavka WHERE IDStavka = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idStavke);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("CijenaPoKomadu");
            }
        } catch (SQLException e) {
            System.err.println("Doslo je do greske prilikom dohvacanja trenutne cijene.");
            ;
        }
        return -1;
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
