package com.example.contact;

public class Nhanvien {
    private String manhanvien, hoten, chucvu, email, sdt, avatar, madonvi;

    public Nhanvien(String manhanvien, String hoten, String chucvu, String email, String sdt, String avatar, String madonvi) {
        this.manhanvien = manhanvien;
        this.hoten = hoten;
        this.chucvu = chucvu;
        this.email = email;
        this.sdt = sdt;
        this.avatar = avatar;
        this.madonvi = madonvi;
    }

    public String getManhanvien() {
        return manhanvien;
    }

    public void setManhanvien(String manhanvien) {
        this.manhanvien = manhanvien;
    }

    public String getHoten() {
        return hoten;
    }

    public void setHoten(String hoten) {
        this.hoten = hoten;
    }

    public String getChucvu() {
        return chucvu;
    }

    public void setChucvu(String chucvu) {
        this.chucvu = chucvu;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getMadonvi() {
        return madonvi;
    }

    public void setMadonvi(String madonvi) {
        this.madonvi = madonvi;
    }
}
