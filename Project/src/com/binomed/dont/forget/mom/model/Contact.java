package com.binomed.dont.forget.mom.model;

import android.graphics.Bitmap;

public class Contact {
	private String id;
	private String displayName;

	private Bitmap photoBitmap;
	private Adress adress;
	private Phone phone;
	private Email email;

	public Adress getAdress() {
		return adress;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public Bitmap getPhotoBitmap() {
		return photoBitmap;
	}

	public void setPhotoBitmap(Bitmap photoBitmap) {
		this.photoBitmap = photoBitmap;
	}

	public Phone getPhone() {
		return phone;
	}

	public void setPhone(Phone phone) {
		this.phone = phone;
	}

	public Email getEmail() {
		return email;
	}

	public void setEmail(Email email) {
		this.email = email;
	}

	public void setAdress(Adress adress) {
		this.adress = adress;
	}

}
