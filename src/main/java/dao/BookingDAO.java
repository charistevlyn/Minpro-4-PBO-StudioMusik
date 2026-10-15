
package dao;

import config.Koneksi;
import model.Booking;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;

public class BookingDAO {

    public ArrayList<Booking> lihatSemua() {
        ArrayList<Booking> daftar = new ArrayList<>();

        String sql = "SELECT * FROM booking";

        try (Connection conn = Koneksi.getKoneksi();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Booking booking = new Booking(
                    rs.getString("id_booking"),
                    rs.getString("id_pelanggan"),
                    rs.getString("id_studio"),
                    rs.getDate("tanggal_booking").toString(),
                    rs.getTime("jam_booking").toString(),
                    rs.getInt("durasi")
                );

                daftar.add(booking);
            }

        } catch (SQLException e) {
            System.out.println(
                "Gagal membaca data booking: " + e.getMessage()
            );
        }

        return daftar;
    }

    public boolean tambah(Booking booking) {
        String sql = "INSERT INTO booking "
                + "(id_booking, id_pelanggan, id_studio, "
                + "tanggal_booking, jam_booking, durasi) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = Koneksi.getKoneksi();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, booking.getIdBooking());
            ps.setString(2, booking.getIdPelanggan());
            ps.setString(3, booking.getIdStudio());
            ps.setDate(
                4, Date.valueOf(booking.getTanggalBooking())
            );
            ps.setTime(
                5, Time.valueOf(booking.getJamBooking())
            );
            ps.setInt(6, booking.getDurasi());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println(
                "Gagal menambah booking: " + e.getMessage()
            );
            return false;
        }
    }
}
