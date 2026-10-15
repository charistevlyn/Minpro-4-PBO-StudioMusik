
package dao;

import config.Koneksi;
import model.Studio;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class StudioDAO {

    public ArrayList<Studio> lihatSemua() {
        ArrayList<Studio> daftar = new ArrayList<>();

        String sql = "SELECT * FROM studio";

        try (Connection conn = Koneksi.getKoneksi();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Studio studio = new Studio(
                    rs.getString("id_studio"),
                    rs.getString("nama_studio"),
                    rs.getString("jenis_studio"),
                    rs.getDouble("harga_per_jam")
                );

                daftar.add(studio);
            }

        } catch (SQLException e) {
            System.out.println(
                "Gagal membaca data studio: " + e.getMessage()
            );
        }

        return daftar;
    }

    public boolean tambah(Studio studio) {
        String sql = "INSERT INTO studio "
                + "(id_studio, nama_studio, jenis_studio, harga_per_jam) "
                + "VALUES (?, ?, ?, ?)";

        try (Connection conn = Koneksi.getKoneksi();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, studio.getIdStudio());
            ps.setString(2, studio.getNamaStudio());
            ps.setString(3, studio.getJenisStudio());
            ps.setDouble(4, studio.getHargaPerJam());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println(
                "Gagal menambah studio: " + e.getMessage()
            );
            return false;
        }
    }

    public boolean ubah(Studio studio) {
        String sql = "UPDATE studio SET nama_studio = ?, "
                + "jenis_studio = ?, harga_per_jam = ? "
                + "WHERE id_studio = ?";

        try (Connection conn = Koneksi.getKoneksi();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, studio.getNamaStudio());
            ps.setString(2, studio.getJenisStudio());
            ps.setDouble(3, studio.getHargaPerJam());
            ps.setString(4, studio.getIdStudio());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println(
                "Gagal mengubah studio: " + e.getMessage()
            );
            return false;
        }
    }

    public boolean hapus(String idStudio) {
        String sql = "DELETE FROM studio WHERE id_studio = ?";

        try (Connection conn = Koneksi.getKoneksi();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, idStudio);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println(
                "Gagal menghapus studio: " + e.getMessage()
            );
            return false;
        }
    }
}
