package com.ilh.alpro_telkom.model;

import com.google.gson.annotations.SerializedName;

public class PelaporModel{

	@SerializedName("id_user_akun")
	private String idUserAkun;

	@SerializedName("id_user_validator")
	private String idUserValidator;

	@SerializedName("id_pelapor")
	private String idPelapor;

	@SerializedName("deskripsi")
	private String deskripsi;

	@SerializedName("tanggal")
	private String tanggal;

	@SerializedName("judul")
	private Object judul;

	@SerializedName("id_user_teknisi")
	private String idUserTeknisi;

	@SerializedName("url_image")
	private String urlImage;

	@SerializedName("alamat")
	private String alamat;

	@SerializedName("status")
	private String status;

	@SerializedName("status_feedback")
	private String status_feedback;

	public String getStatus_feedback() {
		return status_feedback;
	}

	public void setStatus_feedback(String status_feedback) {
		this.status_feedback = status_feedback;
	}

	public void setIdUserAkun(String idUserAkun){
		this.idUserAkun = idUserAkun;
	}

	public String getIdUserAkun(){
		return idUserAkun;
	}

	public void setIdUserValidator(String idUserValidator){
		this.idUserValidator = idUserValidator;
	}

	public String getIdUserValidator(){
		return idUserValidator;
	}

	public void setIdPelapor(String idPelapor){
		this.idPelapor = idPelapor;
	}

	public String getIdPelapor(){
		return idPelapor;
	}

	public void setDeskripsi(String deskripsi){
		this.deskripsi = deskripsi;
	}

	public String getDeskripsi(){
		return deskripsi;
	}

	public void setTanggal(String tanggal){
		this.tanggal = tanggal;
	}

	public String getTanggal(){
		return tanggal;
	}

	public void setJudul(Object judul){
		this.judul = judul;
	}

	public Object getJudul(){
		return judul;
	}

	public void setIdUserTeknisi(String idUserTeknisi){
		this.idUserTeknisi = idUserTeknisi;
	}

	public String getIdUserTeknisi(){
		return idUserTeknisi;
	}

	public void setUrlImage(String urlImage){
		this.urlImage = urlImage;
	}

	public String getUrlImage(){
		return urlImage;
	}

	public void setAlamat(String alamat){
		this.alamat = alamat;
	}

	public String getAlamat(){
		return alamat;
	}

	public void setStatus(String status){
		this.status = status;
	}

	public String getStatus(){
		return status;
	}
}