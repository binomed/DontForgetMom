package com.binomed.dont.forget.mom.service.contact;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;

import com.binomed.dont.forget.mom.model.Address;
import com.binomed.dont.forget.mom.model.Contact;
import com.binomed.dont.forget.mom.model.Email;
import com.binomed.dont.forget.mom.model.Phone;

public class ContactService {

	public static List<Contact> getContactsWithNumber(ContentResolver contentResolver, String number) {

		HashMap<String, ArrayList<Phone>> mapPhones = new HashMap<String, ArrayList<Phone>>();
		ArrayList<Phone> phones = null;
		String[] projectionNumber = new String[] { //
		ContactsContract.CommonDataKinds.Phone.CONTACT_ID//
				, ContactsContract.CommonDataKinds.Phone.NUMBER //
		};
		String selectionNumber = ContactsContract.CommonDataKinds.Phone.NUMBER + " like ?";
		String[] selectionArgsNumber = new String[] { number + "%" };
		if (number == null || number.length() == 0) {
			selectionArgsNumber = null;
			selectionNumber = null;
		}
		Cursor pCur = contentResolver.query( //
				ContactsContract.CommonDataKinds.Phone.CONTENT_URI //
				, projectionNumber // projection
				, selectionNumber // selection
				, selectionArgsNumber // selectionArg
				, null);

		Phone phone = null;
		String contactId = null;
		while (pCur.moveToNext()) {
			phone = new Phone();
			phone.setData(pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
			contactId = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
			phones = mapPhones.get(contactId);
			if (phones == null) {
				phones = new ArrayList<Phone>();
				mapPhones.put(contactId, phones);
			}
			phones.add(phone);

		}
		pCur.close();

		List<Contact> contacts = new ArrayList<Contact>();

		String[] projection = new String[] { //
		ContactsContract.Contacts._ID //
				, ContactsContract.Contacts.DISPLAY_NAME //
		};
		StringBuilder selection = new StringBuilder(ContactsContract.Contacts._ID).append(" in (");
		String[] selectionArg = new String[mapPhones.size()];
		boolean first = true;
		int i = 0;
		for (String contactIdTmp : mapPhones.keySet()) {
			if (!first) {
				selection.append(",");
			}
			first = false;
			selection.append(contactId).append("?");
			selectionArg[i] = contactIdTmp;
			i++;
		}
		selection.append(")");
		Cursor cur = contentResolver.query(ContactsContract.Contacts.CONTENT_URI //
				, projection //
				, selection.toString() //
				, selectionArg //
				, null);
		if (cur.getCount() > 0) {
			Contact contactTmp = null;
			while (cur.moveToNext()) {
				contactTmp = getContact(cur, contentResolver);
				if (contactTmp != null) {
					contactTmp.setPhone(mapPhones.get(contactTmp.getId()));
					contacts.add(contactTmp);

				}
			}
		}

		return contacts;

	}

	public static List<Contact> getContactsWithAdress(ContentResolver contentResolver, String adress) {

		HashMap<String, ArrayList<Address>> mapAdress = new HashMap<String, ArrayList<Address>>();
		ArrayList<Address> addrList = new ArrayList<Address>();

		String[] projectionAdress = new String[] { //
		ContactsContract.CommonDataKinds.StructuredPostal.FORMATTED_ADDRESS, //
				ContactsContract.CommonDataKinds.StructuredPostal.CONTACT_ID //
		};
		String where = ContactsContract.CommonDataKinds.StructuredPostal.FORMATTED_ADDRESS + " like ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
		String[] whereParameters = new String[] { "%" + adress + "%", ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE };
		if (adress == null || adress.length() == 0) {
			where = ContactsContract.Data.MIMETYPE + " = ?";
			whereParameters = new String[] { ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE };
		}

		Cursor addrCur = contentResolver.query( //
				ContactsContract.Data.CONTENT_URI //
				, projectionAdress //
				, where //
				, whereParameters //
				, null //
				);
		Address address = null;
		String contactId = null;
		while (addrCur.moveToNext()) {
			address = new Address();
			address.setFormattedAddress(addrCur.getString(addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.FORMATTED_ADDRESS)));
			contactId = addrCur.getString(addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.CONTACT_ID));
			addrList = mapAdress.get(contactId);
			if (addrList == null) {
				addrList = new ArrayList<Address>();
				mapAdress.put(contactId, addrList);
			}
			addrList.add(address);
		}
		addrCur.close();

		List<Contact> contacts = new ArrayList<Contact>();

		String[] projection = new String[] { //
		ContactsContract.Contacts._ID //
				, ContactsContract.Contacts.DISPLAY_NAME //
		};
		StringBuilder selection = new StringBuilder(ContactsContract.Contacts._ID).append(" in (");
		String[] selectionArg = new String[mapAdress.size()];
		boolean first = true;
		int i = 0;
		for (String contactIdTmp : mapAdress.keySet()) {
			if (!first) {
				selection.append(",");
			}
			first = false;
			selection.append(contactId).append("?");
			selectionArg[i] = contactIdTmp;
			i++;
		}
		selection.append(")");
		Cursor cur = contentResolver.query(ContactsContract.Contacts.CONTENT_URI //
				, projection //
				, selection.toString() //
				, selectionArg //
				, null);
		if (cur.getCount() > 0) {
			Contact contactTmp = null;
			while (cur.moveToNext()) {
				contactTmp = getContact(cur, contentResolver);
				if (contactTmp != null) {
					contactTmp.setAddresses(mapAdress.get(contactTmp.getId()));
					contacts.add(contactTmp);

				}
			}
		}

		return contacts;

	}

	public static Cursor getContactsWithAdressCursor(ContentResolver contentResolver, String adress) {

		HashMap<String, ArrayList<Address>> mapAdress = new HashMap<String, ArrayList<Address>>();
		ArrayList<Address> addrList = new ArrayList<Address>();

		String[] projectionAdress = new String[] { //
		ContactsContract.CommonDataKinds.StructuredPostal.FORMATTED_ADDRESS, //
				ContactsContract.CommonDataKinds.StructuredPostal.CONTACT_ID //
		};
		String where = ContactsContract.CommonDataKinds.StructuredPostal.FORMATTED_ADDRESS + " like ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
		String[] whereParameters = new String[] { "%" + adress + "%", ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE };
		if (adress == null || adress.length() == 0) {
			where = ContactsContract.Data.MIMETYPE + " = ?";
			whereParameters = new String[] { ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE };
		}

		Cursor addrCur = contentResolver.query( //
				ContactsContract.Data.CONTENT_URI //
				, projectionAdress //
				, where //
				, whereParameters //
				, null //
				);
		Address address = null;
		String contactId = null;
		while (addrCur.moveToNext()) {
			address = new Address();
			address.setFormattedAddress(addrCur.getString(addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.FORMATTED_ADDRESS)));
			contactId = addrCur.getString(addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.CONTACT_ID));
			addrList = mapAdress.get(contactId);
			if (addrList == null) {
				addrList = new ArrayList<Address>();
				mapAdress.put(contactId, addrList);
			}
			addrList.add(address);
		}
		addrCur.close();

		String[] projection = new String[] { //
		ContactsContract.Contacts._ID //
				, ContactsContract.Contacts.DISPLAY_NAME //
		};
		StringBuilder selection = new StringBuilder(ContactsContract.Contacts._ID).append(" in (");
		String[] selectionArg = new String[mapAdress.size()];
		boolean first = true;
		int i = 0;
		for (String contactIdTmp : mapAdress.keySet()) {
			if (!first) {
				selection.append(",");
			}
			first = false;
			selection./* append(contactId). */append("?");
			selectionArg[i] = contactIdTmp;
			i++;
		}
		selection.append(")");
		return contentResolver.query(ContactsContract.Contacts.CONTENT_URI //
				, projection //
				, selection.toString() //
				, selectionArg //
				, null);

	}

	public static List<Contact> getContacts(ContentResolver contentResolver) {
		List<Contact> contacts = new ArrayList<Contact>();

		String[] projection = new String[] { //
		ContactsContract.Contacts._ID //
				, ContactsContract.Contacts.DISPLAY_NAME //
		};
		String selection = null;
		String[] selectionArg = null;
		Cursor cur = contentResolver.query(ContactsContract.Contacts.CONTENT_URI //
				, projection //
				, selection //
				, selectionArg //
				, null);
		if (cur.getCount() > 0) {
			Contact contactTmp = null;
			while (cur.moveToNext()) {
				contactTmp = getContact(cur, contentResolver);
				if (contactTmp != null) {
					contacts.add(contactTmp);
				}
			}
		}

		return contacts;

	}

	public static Cursor getContactsCursor(ContentResolver contentResolver) {
		List<Contact> contacts = new ArrayList<Contact>();

		String[] projection = new String[] { //
		ContactsContract.Contacts._ID //
				, ContactsContract.Contacts.DISPLAY_NAME //
		};
		String selection = null;
		String[] selectionArg = null;
		return contentResolver.query(ContactsContract.Contacts.CONTENT_URI //
				, projection //
				, selection //
				, selectionArg //
				, null);

	}

	public static Contact getContact(Cursor cur, ContentResolver contentResolver) {
		Contact contact = new Contact();
		String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
		contact.setId(id);
		contact.setDisplayName(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));

		// contact.setPhotoBitmap(getPhotoStream(id, contentResolver));
		// contact.setPhone(getPhoneNumbers(id, contentResolver));

		// contact.setEmail(getEmailAddresses(id, contentResolver));
		// contact.setAddresses(getContactAddresses(id, contentResolver));

		return contact;
	}

	private static ArrayList<Phone> getPhoneNumbers(String id, ContentResolver contentResolver) {
		ArrayList<Phone> phones = new ArrayList<Phone>();

		Cursor pCur = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[] { id }, null);
		Phone phone = null;
		while (pCur.moveToNext()) {
			phone = new Phone();
			phone.setData(pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
			phone.setType(pCur.getInt(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE)));
			phone.setLabel(pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.LABEL)));
			phones.add(phone);

		}
		pCur.close();
		return phones;
	}

	private static Bitmap getPhotoStream(String id, ContentResolver contentResolver) {
		Bitmap result = null;
		Uri uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, Long.valueOf(id));
		InputStream stream = ContactsContract.Contacts.openContactPhotoInputStream(contentResolver, uri);
		if (stream != null) {
			result = BitmapFactory.decodeStream(stream);
		}
		return result;
	}

	private static ArrayList<Email> getEmailAddresses(String id, ContentResolver contentResolver) {
		ArrayList<Email> emails = new ArrayList<Email>();

		Cursor emailCur = contentResolver.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null, ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?", new String[] { id }, null);
		Email email = null;
		while (emailCur.moveToNext()) {
			// This would allow you get several email addresses
			email = new Email();
			email.setData(emailCur.getString(emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA)));
			email.setType(emailCur.getInt(emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE)));
			email.setLabel(emailCur.getString(emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.LABEL)));
			emails.add(email);
		}
		emailCur.close();
		return emails;
	}

	private static ArrayList<Address> getContactAddresses(String id, ContentResolver contentResolver) {
		ArrayList<Address> addrList = new ArrayList<Address>();

		String where = ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
		String[] whereParameters = new String[] { id, ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE };

		Cursor addrCur = contentResolver.query(ContactsContract.Data.CONTENT_URI, null, where, whereParameters, null);
		Address address = null;
		while (addrCur.moveToNext()) {
			address = new Address();
			address.setPoBox(addrCur.getString(addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.POBOX)));
			address.setStreet(addrCur.getString(addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.STREET)));
			address.setCity(addrCur.getString(addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.CITY)));
			address.setState(addrCur.getString(addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.REGION)));
			address.setPostalCode(addrCur.getString(addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.POSTCODE)));
			address.setCountry(addrCur.getString(addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.COUNTRY)));
			address.setNeighborhood(addrCur.getString(addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.NEIGHBORHOOD)));
			address.setFormattedAddress(addrCur.getString(addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.FORMATTED_ADDRESS)));
			address.setType(addrCur.getInt(addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.TYPE)));
			addrList.add(address);
		}
		addrCur.close();
		return addrList;
	}

}
