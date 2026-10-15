
package dao;

import config.Koneksi;
import model.Pelanggan;
import model.PelangganUmum;
import model.PelangganMember;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PelangganDAO {

    public ArrayList<Pelanggan> lihatSemua() {
        ArrayList<Pelanggan> daftar = new ArrayList<>();

        String sql = "SELECT * FROM pelanggan";

        try (Connection conn = Koneksi.getKoneksi();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String jenis = rs.getString("jenis_pelanggan");
                Pelanggan pelanggan;

                if ("Member".equalsIgnoreCase(jenis)) {
                    pelanggan = new PelangganMember(
                            rs.getString("id_pelanggan"),
                            rs.getString("nama"),
                            rs.getString("no_telepon"),
                            rs.getString("alamat"),
                            rs.getString("jenis_member")
                    );
                } else {
                    pelanggan = new PelangganUmum(
                            rs.getString("id_pelanggan"),
                            rs.getString("nama"),
                            rs.getString("no_telepon"),
                            rs.getString("alamat")
                    );
                }

                daftar.add(pelanggan);
            }

        } catch (SQLException e) {
            System.out.println("Gagal membaca pelanggan: " + e.getMessage());
        }

        return daftar;
    }

    public boolean tambah(Pelanggan pelanggan, String jenisMember) {
        String jenisPelanggan;
        String member = null;

        if (pelanggan instanceof PelangganMember) {
            jenisPelanggan = "Member";
            member = jenisMember;
        } else {
            jenisPelanggan = "Umum";
        }

        String sql = "INSERT INTO pelanggan "
                + "(id_pelanggan, nama, no_telepon, alamat, "
                + "jenis_pelanggan, jenis_member) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = Koneksi.getKoneksi();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, pelanggan.getIdPelanggan());
            ps.setString(2, pelanggan.getNama());
            ps.setString(3, pelanggan.getNoTelepon());
            ps.setString(4, pelanggan.getAlamat());
            ps.setString(5, jenisPelanggan);
            ps.setString(6, member);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Gagal menambah pelanggan: " + e.getMessage());
            return false;
        }
    }
}
