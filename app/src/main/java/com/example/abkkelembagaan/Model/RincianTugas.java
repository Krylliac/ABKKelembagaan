package com.example.abkkelembagaan.Model;

public class RincianTugas {
    String id_nama_jabatan;
    String id_uraian_tugas;
    String id_rincian_tugas;
    String nama_rincian_tugas;
    String sifat_pekerjaan;
    String beban_kerja;
    String waktu_penyelesaian;
    String nama_satuan;
    String satuan;
    String perhitungan_hasil;
    String value_sifat_pekerjaan;
    String pegawai_yang_dibutuhkan;
    String tanggal_buat;
    //String nama_jabatan;
    //String beban_kerja_setahun;
    //String nama_uraian_tugas;
    //String aksi;
    //String unit_kerja;
    //String sifat_pekerjaan_setahun;
    //String sifat_pekerjaan_menit;
    //String norma_waktu;

    public RincianTugas(){

    }

    public String getTanggal_buat() {
        return tanggal_buat;
    }

    public void setTanggal_buat(String tanggal_buat) {
        this.tanggal_buat = tanggal_buat;
    }

    public RincianTugas(String id_nama_jabatan, String id_uraian_tugas, String id_rincian_tugas, String nama_rincian_tugas, String sifat_pekerjaan, String beban_kerja, String waktu_penyelesaian, String nama_satuan, String satuan, String perhitungan_hasil, String value_sifat_pekerjaan, String pegawai_yang_dibutuhkan, String tanggal_buat) {
        this.id_nama_jabatan = id_nama_jabatan;
        this.id_uraian_tugas = id_uraian_tugas;
        this.id_rincian_tugas = id_rincian_tugas;
        this.nama_rincian_tugas = nama_rincian_tugas;
        this.sifat_pekerjaan = sifat_pekerjaan;
        this.beban_kerja = beban_kerja;
        this.waktu_penyelesaian = waktu_penyelesaian;
        this.nama_satuan = nama_satuan;
        this.satuan = satuan;
        this.perhitungan_hasil = perhitungan_hasil;
        this.value_sifat_pekerjaan = value_sifat_pekerjaan;
        this.pegawai_yang_dibutuhkan = pegawai_yang_dibutuhkan;
        this.tanggal_buat = tanggal_buat;
    }

    public String getId_nama_jabatan() {
        return id_nama_jabatan;
    }

    public void setId_nama_jabatan(String id_nama_jabatan) {
        this.id_nama_jabatan = id_nama_jabatan;
    }

    public String getId_uraian_tugas() {
        return id_uraian_tugas;
    }

    public void setId_uraian_tugas(String id_uraian_tugas) {
        this.id_uraian_tugas = id_uraian_tugas;
    }

    public String getId_rincian_tugas() {
        return id_rincian_tugas;
    }

    public void setId_rincian_tugas(String id_rincian_tugas) {
        this.id_rincian_tugas = id_rincian_tugas;
    }

    public String getNama_rincian_tugas() {
        return nama_rincian_tugas;
    }

    public void setNama_rincian_tugas(String nama_rincian_tugas) {
        this.nama_rincian_tugas = nama_rincian_tugas;
    }

    public String getSifat_pekerjaan() {
        return sifat_pekerjaan;
    }

    public void setSifat_pekerjaan(String sifat_pekerjaan) {
        this.sifat_pekerjaan = sifat_pekerjaan;
    }

    public String getBeban_kerja() {
        return beban_kerja;
    }

    public void setBeban_kerja(String beban_kerja) {
        this.beban_kerja = beban_kerja;
    }

    public String getWaktu_penyelesaian() {
        return waktu_penyelesaian;
    }

    public void setWaktu_penyelesaian(String waktu_penyelesaian) {
        this.waktu_penyelesaian = waktu_penyelesaian;
    }

    public String getNama_satuan() {
        return nama_satuan;
    }

    public void setNama_satuan(String nama_satuan) {
        this.nama_satuan = nama_satuan;
    }

    public String getSatuan() {
        return satuan;
    }

    public void setSatuan(String satuan) {
        this.satuan = satuan;
    }

    public String getPerhitungan_hasil() {
        return perhitungan_hasil;
    }

    public void setPerhitungan_hasil(String perhitungan_hasil) {
        this.perhitungan_hasil = perhitungan_hasil;
    }

    public String getValue_sifat_pekerjaan() {
        return value_sifat_pekerjaan;
    }

    public void setValue_sifat_pekerjaan(String value_sifat_pekerjaan) {
        this.value_sifat_pekerjaan = value_sifat_pekerjaan;
    }

    public String getPegawai_yang_dibutuhkan() {
        return pegawai_yang_dibutuhkan;
    }

    public void setPegawai_yang_dibutuhkan(String pegawai_yang_dibutuhkan) {
        this.pegawai_yang_dibutuhkan = pegawai_yang_dibutuhkan;
    }
}
