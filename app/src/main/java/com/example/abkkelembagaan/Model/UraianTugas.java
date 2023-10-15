package com.example.abkkelembagaan.Model;

public class UraianTugas {
    String id_jabatan;
    String uraian_id;
    String nama_uraian_tugas;
    String tanggal_buat;
    String aksi;

    public UraianTugas(){

    }

    public UraianTugas(String id_jabatan, String uraian_id, String nama_uraian_tugas, String tanggal_buat, String aksi) {
        this.id_jabatan = id_jabatan;
        this.uraian_id = uraian_id;
        this.nama_uraian_tugas = nama_uraian_tugas;
        this.tanggal_buat = tanggal_buat;
        this.aksi = aksi;
    }

    public String getId_jabatan() {
        return id_jabatan;
    }

    public void setId_jabatan(String id_jabatan) {
        this.id_jabatan = id_jabatan;
    }

    public String getUraian_id() {
        return uraian_id;
    }

    public void setUraian_id(String uraian_id) {
        this.uraian_id = uraian_id;
    }

    public String getNama_uraian_tugas() {
        return nama_uraian_tugas;
    }

    public void setNama_uraian_tugas(String nama_uraian_tugas) {
        this.nama_uraian_tugas = nama_uraian_tugas;
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
