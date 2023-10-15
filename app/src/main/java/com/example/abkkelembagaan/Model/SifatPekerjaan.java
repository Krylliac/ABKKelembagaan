package com.example.abkkelembagaan.Model;

public class SifatPekerjaan {
    String nama_sifat_pekerjaan;
    String sifat_pekerjaan_menit;
    String sifat_pekerjaan_setahun;
    String tanggal_buat;
    String aksi;


    public SifatPekerjaan(){

    }

    public SifatPekerjaan(String nama_sifat_pekerjaan, String sifat_pekerjaan_menit, String sifat_pekerjaan_setahun, String tanggal_buat, String aksi) {
        this.nama_sifat_pekerjaan = nama_sifat_pekerjaan;
        this.sifat_pekerjaan_menit = sifat_pekerjaan_menit;
        this.sifat_pekerjaan_setahun = sifat_pekerjaan_setahun;
        this.tanggal_buat = tanggal_buat;
        this.aksi = aksi;
    }

    public String getNama_sifat_pekerjaan() {
        return nama_sifat_pekerjaan;
    }

    public void setNama_sifat_pekerjaan(String nama_sifat_pekerjaan) {
        this.nama_sifat_pekerjaan = nama_sifat_pekerjaan;
    }

    public String getSifat_pekerjaan_menit() {
        return sifat_pekerjaan_menit;
    }

    public void setSifat_pekerjaan_menit(String sifat_pekerjaan_menit) {
        this.sifat_pekerjaan_menit = sifat_pekerjaan_menit;
    }

    public String getSifat_pekerjaan_setahun() {
        return sifat_pekerjaan_setahun;
    }

    public void setSifat_pekerjaan_setahun(String sifat_pekerjaan_setahun) {
        this.sifat_pekerjaan_setahun = sifat_pekerjaan_setahun;
    }

    public String getTanggal_buat() {
        return tanggal_buat;
    }

    public void setTanggal_buat(String tanggal_buat) {
        this.tanggal_buat = tanggal_buat;
    }

    public String getAksi() {
        return aksi;
    }

    public void setAksi(String aksi) {
        this.aksi = aksi;
    }
}
