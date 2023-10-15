package com.example.abkkelembagaan.Model;

public class Jabatan {
    String id_jabatan;
    String nama_jabatan;
    String unit_kerja;
    String tanggal_buat;
    String aksi;

    public Jabatan(String id_jabatan, String nama_jabatan, String unit_kerja, String tanggal_buat, String aksi) {
        this.id_jabatan = id_jabatan;
        this.nama_jabatan = nama_jabatan;
        this.unit_kerja = unit_kerja;
        this.tanggal_buat = tanggal_buat;
        this.aksi = aksi;
    }

    public Jabatan(){

    }

    public String getId_jabatan() {
        return id_jabatan;
    }

    public void setId_jabatan(String id_jabatan) {
        this.id_jabatan = id_jabatan;
    }

    public String getNama_jabatan() {
        return nama_jabatan;
    }

    public void setNama_jabatan(String nama_jabatan) {
        this.nama_jabatan = nama_jabatan;
    }

    public String getUnit_kerja() {
        return unit_kerja;
    }

    public void setUnit_kerja(String unit_kerja) {
        this.unit_kerja = unit_kerja;
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
