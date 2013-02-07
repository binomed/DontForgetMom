package com.binomed.dont.forget.mom.service.contact;

import java.io.InputStream;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.database.MergeCursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;

import com.binomed.dont.forget.mom.model.Adress;
import com.binomed.dont.forget.mom.model.Contact;
import com.binomed.dont.forget.mom.model.Email;
import com.binomed.dont.forget.mom.model.Phone;

public class ContactService {

	public static Cursor getContactsWithAdressCursor(ContentResolver contentResolver, String adress) {

		String[] projectionAdress = new String[] { //
		ContactsContract.CommonDataKinds.StructuredPostal._ID, //
				ContactsContract.CommonDataKinds.StructuredPostal.FORMATTED_ADDRESS, //
				ContactsContract.CommonDataKinds.StructuredPostal.CONTACT_ID, //
				ContactsContract.CommonDataKinds.StructuredPostal.DISPLAY_NAME //
		};
		String where = "( " + ContactsContract.CommonDataKinds.StructuredPostal.DISPLAY_NAME + " like ? " //
				+ " OR " + ContactsContract.CommonDataKinds.StructuredPostal.FORMATTED_ADDRESS + " like ? )" //
				+ " AND " + ContactsContract.Data.MIMETYPE + " = ?";
		String[] whereParameters = new String[] { "%" + adress + "%", "%" + adress + "%", ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE };
		if (adress == null || adress.length() == 0) {
			where = ContactsContract.Data.MIMETYPE + " = ?";
			whereParameters = new String[] { ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE };
		}

		Cursor addrCur = contentResolver.query( //
				ContactsContract.Data.CONTENT_URI //
				, projectionAdress //
				, where //
				, whereParameters //
				, ContactsContract.CommonDataKinds.StructuredPostal.DISPLAY_NAME //
				);
		return addrCur;
	}

	public static Cursor getContactsWithPhoneNumberCursor(ContentResolver contentResolver, String phoneNumber) {

		String[] projectionPhone = new String[] { //
		ContactsContract.CommonDataKinds.Phone._ID, //
				ContactsContract.CommonDataKinds.Phone.NUMBER, //
				ContactsContract.CommonDataKinds.Phone.CONTACT_ID, //
				ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME //
		};
		String where = "( " + ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " like ? " //
				+ " OR " + ContactsContract.CommonDataKinds.Phone.NUMBER + " like ? )" //
				+ " AND " + ContactsContract.Data.MIMETYPE + " = ?";
		String[] whereParameters = new String[] { "%" + phoneNumber + "%", "%" + phoneNumber + "%", ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE };
		if (phoneNumber == null || phoneNumber.length() == 0) {
			where = ContactsContract.Data.MIMETYPE + " = ?";
			whereParameters = new String[] { ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE };
		}

		Cursor addrCur = contentResolver.query( //
				ContactsContract.CommonDataKinds.Phone.CONTENT_URI //
				, projectionPhone //
				, where //
				, whereParameters //
				, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME //
				);
		return addrCur;
	}

	public static Cursor getContactsWithEmailCursor(ContentResolver contentResolver, String email) {

		String[] projectionEmail = new String[] { //
		ContactsContract.CommonDataKinds.Email._ID, //
				ContactsContract.CommonDataKinds.Email.ADDRESS, //
				ContactsContract.CommonDataKinds.Email.CONTACT_ID, //
				ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME //
		};
		String where = "( " + ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " like ? " //
				+ " OR " + ContactsContract.CommonDataKinds.Email.ADDRESS + " like ? )" //
				+ " AND " + ContactsContract.Data.MIMETYPE + " = ?";
		String[] whereParameters = new String[] { "%" + email + "%", "%" + email + "%", ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE };
		if (email == null || email.length() == 0) {
			where = ContactsContract.Data.MIMETYPE + " = ?";
			whereParameters = new String[] { ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE };
		}

		Cursor addrCur = contentResolver.query( //
				ContactsContract.CommonDataKinds.Email.CONTENT_URI //
				, projectionEmail //
				, where //
				, whereParameters //
				, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME //
				);
		return addrCur;
	}

	public static Cursor getContactsWithEmailAndPhoneCursor(ContentResolver contentResolver, String text) {

		String[] projectionEmail = new String[] { //
		ContactsContract.CommonDataKinds.Email._ID, //
				ContactsContract.CommonDataKinds.Email.ADDRESS, //
				ContactsContract.CommonDataKinds.Email.CONTACT_ID, //
				ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME //
		};
		String[] projectionPhone = new String[] { //
		ContactsContract.CommonDataKinds.Phone._ID, //
				ContactsContract.CommonDataKinds.Phone.NUMBER, //
				ContactsContract.CommonDataKinds.Phone.CONTACT_ID, //
				ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME //
		};
		String whereEmail = "( " + ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " like ? " //
				+ " OR " + ContactsContract.CommonDataKinds.Email.ADDRESS + " like ? )" //
				+ " AND " + ContactsContract.Data.MIMETYPE + " = ?";
		String[] whereEmailParameters = new String[] { "%" + text + "%", "%" + text + "%", ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE };
		String wherePhone = "( " + ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " like ? " //
				+ " OR " + ContactsContract.CommonDataKinds.Phone.NUMBER + " like ? )" //
				+ " AND " + ContactsContract.Data.MIMETYPE + " = ?";
		String[] wherePhoneParameters = new String[] { "%" + text + "%", "%" + text + "%", ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE };
		if (text == null || text.length() == 0) {
			whereEmail = ContactsContract.Data.MIMETYPE + " = ?";
			whereEmailParameters = new String[] { ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE };
			wherePhone = ContactsContract.Data.MIMETYPE + " = ?";
			wherePhoneParameters = new String[] { ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE };
		}

		Cursor emailCur = contentResolver.query( //
				ContactsContract.CommonDataKinds.Email.CONTENT_URI //
				, projectionEmail //
				, whereEmail //
				, whereEmailParameters //
				, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME //
				);
		Cursor phoneCur = contentResolver.query( //
				ContactsContract.CommonDataKinds.Phone.CONTENT_URI //
				, projectionPhone //
				, wherePhone //
				, wherePhoneParameters //
				, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME //
				);
		return new MergeCursor(new Cursor[] { emailCur, phoneCur });
	}

	public static Contact getContactWithAdress(Cursor cur, ContentResolver contentResolver) {
		Contact contact = new Contact();
		String id = cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.CONTACT_ID));
		contact.setId(id);
		contact.setDisplayName(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));
		Adress adress = new Adress();
		adress.setFormattedAddress(cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.FORMATTED_ADDRESS)));
		contact.setAdress(adress);
		contact.setPhotoBitmap(getPhotoStream(id, contentResolver));

		return contact;
	}

	public static Contact getContactWithPhoneNumber(Cursor cur, ContentResolver contentResolver) {

		Contact contact = new Contact();
		String id = cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
		contact.setId(id);
		contact.setDisplayName(cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)));
		Phone phone = new Phone();
		phone.setData(cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
		contact.setPhone(phone);
		contact.setPhotoBitmap(getPhotoStream(id, contentResolver));
		return contact;
	}

	public static Contact getContactWithEmail(Cursor cur, ContentResolver contentResolver) {
		Contact contact = new Contact();
		String id = cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Email.CONTACT_ID));
		contact.setId(id);
		contact.setDisplayName(cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)));
		Email email = new Email();
		email.setData(cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA)));
		contact.setEmail(email);
		contact.setPhotoBitmap(getPhotoStream(id, contentResolver));
		return contact;
	}

	public static Contact getContactWithEmailOrPhone(Cursor cur, ContentResolver contentResolver) {
		Contact contact = new Contact();
		String id = null;

		if (cur.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA) != -1) {
			id = cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Email.CONTACT_ID));
			contact.setId(id);
			contact.setDisplayName(cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)));
			Email email = new Email();
			email.setData(cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA)));
			contact.setEmail(email);
		}

		if (cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER) != -1) {
			id = cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
			contact.setId(id);
			contact.setDisplayName(cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)));
			Phone phone = new Phone();
			phone.setData(cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
			contact.setPhone(phone);
		}
		contact.setPhotoBitmap(getPhotoStream(id, contentResolver));
		return contact;
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

}
