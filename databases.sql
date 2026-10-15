
CREATE DATABASE IF NOT EXISTS studio_musik;
USE studio_musik;

CREATE TABLE IF NOT EXISTS studio (
    id_studio VARCHAR(10) PRIMARY KEY,
    nama_studio VARCHAR(100) NOT NULL,
    jenis_studio VARCHAR(50) NOT NULL,
    harga_per_jam DOUBLE NOT NULL
);

CREATE TABLE IF NOT EXISTS pelanggan (
    id_pelanggan VARCHAR(10) PRIMARY KEY,
    nama VARCHAR(100) NOT NULL,
    no_telepon VARCHAR(20) NOT NULL,
    alamat VARCHAR(150) NOT NULL,
    jenis_pelanggan VARCHAR(20) NOT NULL,
    jenis_member VARCHAR(30)
);

CREATE TABLE IF NOT EXISTS booking (
    id_booking VARCHAR(10) PRIMARY KEY,
    id_pelanggan VARCHAR(10) NOT NULL,
    id_studio VARCHAR(10) NOT NULL,
    tanggal_booking DATE NOT NULL,
    jam_booking TIME NOT NULL,
    durasi INT NOT NULL,
    FOREIGN KEY (id_pelanggan) REFERENCES pelanggan(id_pelanggan),
    FOREIGN KEY (id_studio) REFERENCES studio(id_studio)
);

INSERT IGNORE INTO studio
VALUES
('ST001', 'Grace Room Band', 'Band', 75000),
('ST002', 'Eliana Music Room', 'Akustik', 50000);

INSERT IGNORE INTO pelanggan
VALUES
('PL001', 'Caleb', '081234567890', 'Samarinda', 'Umum', NULL),
('PL002', 'Eliana', '082345678901', 'Balikpapan', 'Member', 'Gold');

INSERT IGNORE INTO booking
VALUES
('BK001', 'PL001', 'ST001', '2026-10-15', '14:00:00', 2),
('BK002', 'PL002', 'ST002', '2026-10-16', '16:00:00', 1);

