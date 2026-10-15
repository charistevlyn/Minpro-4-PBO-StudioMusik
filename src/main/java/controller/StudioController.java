
package controller;

import java.util.ArrayList;

import model.Studio;
import model.Pelanggan;
import model.PelangganUmum;
import model.PelangganMember;
import model.Booking;
import model.CRUD;

import dao.StudioDAO;
import dao.PelangganDAO;
import dao.BookingDAO;

import view.MenuView;

public class StudioController implements CRUD {

    private StudioDAO studioDAO;
    private PelangganDAO pelangganDAO;
    private BookingDAO bookingDAO;
    private MenuView view;

    public StudioController() {
        studioDAO = new StudioDAO();
        pelangganDAO = new PelangganDAO();
        bookingDAO = new BookingDAO();
        view = new MenuView();
    }

    public void jalankanProgram() {
        int pilihan;

        do {
            pilihan = view.tampilkanMenu();

            switch (pilihan) {
                case 1:
                    tambah();
                    break;
                case 2:
                    lihat();
                    break;
                case 3:
                    ubah();
                    break;
                case 4:
                    hapus();
                    break;
                case 5:
                    tambahPelanggan();
                    break;
                case 6:
                    lihatPelanggan();
                    break;
                case 7:
                    tambahBooking();
                    break;
                case 8:
                    lihatBooking();
                    break;
                case 9:
                    view.tampilkanPesan("Program selesai.");
                    break;
                default:
                    view.tampilkanPesan("Menu tidak tersedia!");
            }
        } while (pilihan != 9);
    }

    // ================= STUDIO =================

    @Override
    public void tambah() {
        System.out.println("\n--- TAMBAH STUDIO ---");

        String id = buatIdStudio();
        String nama = view.inputTeks("Nama studio: ");
        String jenis = view.inputTeks("Jenis studio: ");
        double harga = view.inputDouble("Harga per jam: ");

        if (harga <= 0) {
            view.tampilkanPesan("Harga harus lebih dari 0!");
            return;
        }

        Studio studio = new Studio(id, nama, jenis, harga);

        if (studioDAO.tambah(studio)) {
            view.tampilkanPesan("Studio berhasil ditambahkan. ID: " + id);
        } else {
            view.tampilkanPesan("Studio gagal ditambahkan!");
        }
    }

    @Override
    public void lihat() {
        System.out.println("\n--- DAFTAR STUDIO ---");

        ArrayList<Studio> daftar = studioDAO.lihatSemua();

        if (daftar.isEmpty()) {
            System.out.println("Belum ada data studio.");
            return;
        }

        for (Studio s : daftar) {
            System.out.println("ID       : " + s.getIdStudio());
            System.out.println("Nama     : " + s.getNamaStudio());
            System.out.println("Jenis    : " + s.getJenisStudio());
            System.out.println("Harga    : " + s.getHargaPerJam());
            System.out.println("-------------------------");
        }
    }

    @Override
    public void ubah() {
        System.out.println("\n--- UBAH STUDIO ---");
        lihat();

        String id = view.inputTeks("ID studio yang diubah: ");
        Studio lama = cariStudio(id);

        if (lama == null) {
            view.tampilkanPesan("Studio tidak ditemukan!");
            return;
        }

        String nama = view.inputTeks("Nama baru: ");
        String jenis = view.inputTeks("Jenis baru: ");
        double harga = view.inputDouble("Harga baru: ");

        if (harga <= 0) {
            view.tampilkanPesan("Harga harus lebih dari 0!");
            return;
        }

        Studio baru = new Studio(id, nama, jenis, harga);

        if (studioDAO.ubah(baru)) {
            view.tampilkanPesan("Studio berhasil diubah!");
        } else {
            view.tampilkanPesan("Studio gagal diubah!");
        }
    }

    @Override
    public void hapus() {
        System.out.println("\n--- HAPUS STUDIO ---");
        lihat();

        String id = view.inputTeks("ID studio yang dihapus: ");

        if (studioDAO.hapus(id)) {
            view.tampilkanPesan("Studio berhasil dihapus!");
        } else {
            view.tampilkanPesan(
                "Studio gagal dihapus. Periksa ID atau booking terkait."
            );
        }
    }

    // ================= PELANGGAN =================

    private void tambahPelanggan() {
        System.out.println("\n--- TAMBAH PELANGGAN ---");

        String id = buatIdPelanggan();
        String nama = view.inputTeks("Nama pelanggan: ");
        String telepon = view.inputTeks("No. telepon: ");
        String alamat = view.inputTeks("Alamat: ");

        System.out.println("1. Pelanggan Umum");
        System.out.println("2. Pelanggan Member");

        int pilihan = view.inputAngka("Pilih jenis pelanggan: ");

        Pelanggan pelanggan;
        String jenisMember = null;

        if (pilihan == 1) {
            pelanggan = new PelangganUmum(
                id, nama, telepon, alamat
            );
        } else if (pilihan == 2) {
            jenisMember = view.inputTeks("Jenis member: ");
            pelanggan = new PelangganMember(
                id, nama, telepon, alamat, jenisMember
            );
        } else {
            view.tampilkanPesan("Pilihan tidak tersedia!");
            return;
        }

        if (pelangganDAO.tambah(pelanggan, jenisMember)) {
            view.tampilkanPesan(
                "Pelanggan berhasil ditambahkan. ID: " + id
            );
        } else {
            view.tampilkanPesan("Pelanggan gagal ditambahkan!");
        }
    }

    private void lihatPelanggan() {
        System.out.println("\n--- DAFTAR PELANGGAN ---");

        ArrayList<Pelanggan> daftar = pelangganDAO.lihatSemua();

        if (daftar.isEmpty()) {
            System.out.println("Belum ada data pelanggan.");
            return;
        }

        for (Pelanggan p : daftar) {
            System.out.println("ID       : " + p.getIdPelanggan());
            System.out.println("Nama     : " + p.getNama());
            System.out.println("Telepon  : " + p.getNoTelepon());
            System.out.println("Alamat   : " + p.getAlamat());
            System.out.println("Jenis    : " + p.getInfo());
            System.out.println("-------------------------");
        }
    }

    // ================= BOOKING =================

    private void tambahBooking() {
        System.out.println("\n--- TAMBAH BOOKING ---");

        lihatPelanggan();
        lihat();

        String id = buatIdBooking();
        String idPelanggan = view.inputTeks("ID pelanggan: ");
        String idStudio = view.inputTeks("ID studio: ");

        if (!pelangganAda(idPelanggan)) {
            view.tampilkanPesan("Pelanggan tidak ditemukan!");
            return;
        }

        if (cariStudio(idStudio) == null) {
            view.tampilkanPesan("Studio tidak ditemukan!");
            return;
        }

        String tanggal = view.inputTeks(
            "Tanggal (yyyy-MM-dd): "
        );
        String jam = view.inputTeks(
            "Jam (HH:mm:ss): "
        );
        int durasi = view.inputAngka("Durasi dalam jam: ");

        if (durasi <= 0) {
            view.tampilkanPesan("Durasi harus lebih dari 0!");
            return;
        }

        try {
            java.sql.Date.valueOf(tanggal);
            java.sql.Time.valueOf(jam);
        } catch (IllegalArgumentException e) {
            view.tampilkanPesan("Format tanggal atau jam salah!");
            return;
        }

        Booking booking = new Booking(
            id, idPelanggan, idStudio, tanggal, jam, durasi
        );

        if (bookingDAO.tambah(booking)) {
            view.tampilkanPesan(
                "Booking berhasil ditambahkan. ID: " + id
            );
        } else {
            view.tampilkanPesan("Booking gagal ditambahkan!");
        }
    }

    private void lihatBooking() {
        System.out.println("\n--- DAFTAR BOOKING ---");

        ArrayList<Booking> daftar = bookingDAO.lihatSemua();

        if (daftar.isEmpty()) {
            System.out.println("Belum ada data booking.");
            return;
        }

        for (Booking b : daftar) {
            System.out.println("ID Booking   : " + b.getIdBooking());
            System.out.println("ID Pelanggan : " + b.getIdPelanggan());
            System.out.println("ID Studio    : " + b.getIdStudio());
            System.out.println("Tanggal      : " + b.getTanggalBooking());
            System.out.println("Jam          : " + b.getJamBooking());
            System.out.println("Durasi       : " + b.getDurasi() + " jam");
            System.out.println("-------------------------");
        }
    }

    // ================= PENCARIAN =================

    private Studio cariStudio(String id) {
        for (Studio s : studioDAO.lihatSemua()) {
            if (s.getIdStudio().equalsIgnoreCase(id)) {
                return s;
            }
        }
        return null;
    }

    private boolean pelangganAda(String id) {
        for (Pelanggan p : pelangganDAO.lihatSemua()) {
            if (p.getIdPelanggan().equalsIgnoreCase(id)) {
                return true;
            }
        }
        return false;
    }

    // ================= ID OTOMATIS =================

    private String buatIdStudio() {
        int nomor = 1;
        String id;
        ArrayList<Studio> daftar = studioDAO.lihatSemua();

        do {
            id = String.format("ST%03d", nomor++);
        } while (studioAda(daftar, id));

        return id;
    }

    private boolean studioAda(ArrayList<Studio> daftar, String id) {
        for (Studio s : daftar) {
            if (s.getIdStudio().equals(id)) {
                return true;
            }
        }
        return false;
    }

    private String buatIdPelanggan() {
        int nomor = 1;
        String id;
        ArrayList<Pelanggan> daftar = pelangganDAO.lihatSemua();

        do {
            id = String.format("PL%03d", nomor++);
        } while (pelangganAda(daftar, id));

        return id;
    }

    private boolean pelangganAda(
            ArrayList<Pelanggan> daftar, String id) {
        for (Pelanggan p : daftar) {
            if (p.getIdPelanggan().equals(id)) {
                return true;
            }
        }
        return false;
    }

    private String buatIdBooking() {
        int nomor = 1;
        String id;
        ArrayList<Booking> daftar = bookingDAO.lihatSemua();

        do {
            id = String.format("BK%03d", nomor++);
        } while (bookingAda(daftar, id));

        return id;
    }

    private boolean bookingAda(
            ArrayList<Booking> daftar, String id) {
        for (Booking b : daftar) {
            if (b.getIdBooking().equals(id)) {
                return true;
            }
        }
        return false;
    }
}
