package com.binomed.dont.forget.mom.model;

import java.util.ArrayList;

import android.graphics.Bitmap;

public class Contact {
	private String id;
	private String displayName;

	private Bitmap photoBitmap;
	private ArrayList<Address> addresses;
	private ArrayList<Phone> phone;
	private ArrayList<Email> email;

	public ArrayList<Address> getAddresses() {
		return addresses;
	}

	public void setAddresses(ArrayList<Address> addresses) {
		this.addresses = addresses;
	}

	public void addAddress(Address address) {
		this.addresses.add(address);
	}

	public ArrayList<Email> getEmails() {
		return email;
	}

	public void setEmail(ArrayList<Email> email) {
		this.email = email;
	}

	public void addEmail(Email e) {
		this.email.add(e);
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

	public void setDisplayName(String dName) {
		this.displayName = dName;
	}

	public ArrayList<Phone> getPhones() {
		return phone;
	}

	public void setPhone(ArrayList<Phone> phone) {
		this.phone = phone;
	}

	public void addPhone(Phone phone) {
		this.phone.add(phone);
	}

	public Bitmap getPhotoBitmap() {
		return photoBitmap;
	}

	public void setPhotoBitmap(Bitmap photoBitmap) {
		this.photoBitmap = photoBitmap;
	}

}
