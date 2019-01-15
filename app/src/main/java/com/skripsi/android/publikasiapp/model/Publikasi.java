package com.skripsi.android.publikasiapp.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "tb_publikasi")
public class Publikasi {

    @PrimaryKey()
//    private int id;
    @ColumnInfo(name = "pub_id")
    private int pub_id;
    @ColumnInfo(name = "title")
    private String title;
    @ColumnInfo(name = "kat_no")
    private String kat_no;
    @ColumnInfo(name = "pub_no")
    private String pub_no;
    @ColumnInfo(name = "issn")
    private String issn;
    @ColumnInfo(name = "abstract")
    private String _abstract;
    @ColumnInfo(name = "sch_date")
    private String sch_date;
    @ColumnInfo(name = "rl_date")
    private String rl_date;
    @ColumnInfo(name = "cover")
    private String cover;
    @ColumnInfo(name = "pdf")
    private String pdf;
    @ColumnInfo(name = "size")
    private String size;

    @Ignore
    public Publikasi() {
    }

    public Publikasi(int pub_id, String title, String kat_no, String pub_no, String issn, String _abstract, String sch_date, String rl_date, String cover, String pdf, String size) {
        this.pub_id = pub_id;
        this.title = title;
        this.kat_no = kat_no;
        this.pub_no = pub_no;
        this.issn = issn;
        this._abstract = _abstract;
        this.sch_date = sch_date;
        this.rl_date = rl_date;
        this.cover = cover;
        this.pdf = pdf;
        this.size = size;
    }


    public int getPub_id() {
        return pub_id;
    }

    public void setPub_id(int pub_id) {
        this.pub_id = pub_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getKat_no() {
        return kat_no;
    }

    public void setKat_no(String kat_no) {
        this.kat_no = kat_no;
    }

    public String getPub_no() {
        return pub_no;
    }

    public void setPub_no(String pub_no) {
        this.pub_no = pub_no;
    }

    public String getIssn() {
        return issn;
    }

    public void setIssn(String issn) {
        this.issn = issn;
    }

    public String get_abstract() {
        return _abstract;
    }

    public void set_abstract(String _abstract) {
        this._abstract = _abstract;
    }

    public String getSch_date() {
        return sch_date;
    }

    public void setSch_date(String sch_date) {
        this.sch_date = sch_date;
    }

    public String getRl_date() {
        return rl_date;
    }

    public void setRl_date(String rl_date) {
        this.rl_date = rl_date;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getPdf() {
        return pdf;
    }

    public void setPdf(String pdf) {
        this.pdf = pdf;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

//    public int getId() {
//        return id;
//    }
//
//    public void setId(int id) {
//        this.id = id;
//    }
}
