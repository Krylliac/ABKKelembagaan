package com.example.abkkelembagaan.Model;

public class Satuan {
    String nama_satuan;
    String jumlah_satuan;

    public Satuan(){

    }

    public Satuan(String nama_satuan, String jumlah_satuan) {
        this.nama_satuan = nama_satuan;
        this.jumlah_satuan = jumlah_satuan;
    }

    public String getNama_satuan() {
        return nama_satuan;
    }

    public void setNama_satuan(String nama_satuan) {
        this.nama_satuan = nama_satuan;
    }

    public String getJumlah_satuan() {
        return jumlah_satuan;
    }

    public void setJumlah_satuan(String jumlah_satuan) {
        this.jumlah_satuan = jumlah_satuan;
    }
}
