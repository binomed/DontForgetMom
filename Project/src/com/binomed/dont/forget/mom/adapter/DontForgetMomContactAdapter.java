package com.binomed.dont.forget.mom.adapter;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.support.v4.widget.CursorAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FilterQueryProvider;
import android.widget.Filterable;

import com.binomed.dont.forget.mom.model.Contact;
import com.binomed.dont.forget.mom.service.contact.ContactService;
import com.binomed.dont.forget.mom.view.ContactView;

public class DontForgetMomContactAdapter extends CursorAdapter implements Filterable {

	private final ContentResolver contentResolver;
	private TypeDatas type;
	private Context context;

	public void setType(TypeDatas type) {
		this.type = type;
	}

	public DontForgetMomContactAdapter(Context context, Cursor c, TypeDatas type) {
		super(context, c, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
		contentResolver = context.getContentResolver();
		this.type = type;
		this.context = context;
	}

	@Override
	public void bindView(View view, Context ctx, Cursor cursor) {
		ContactView contactView = null;
		if (view == null) {
			contactView = new ContactView(ctx);
		} else {
			contactView = (ContactView) view;
		}

		fillView(cursor, contactView);

	}

	private void fillView(Cursor cursor, ContactView txtView) {
		Contact contact = null;
		String detail = null;
		switch (type) {
		case ADRESS:
			contact = ContactService.getContactWithAdress(cursor, this.contentResolver);
			detail = contact.getAdress().getFormatted_address();
			break;
		case MAIL:
			contact = ContactService.getContactWithEmail(cursor, this.contentResolver);
			detail = contact.getEmail().getData();
			break;
		case PHONE:
			contact = ContactService.getContactWithPhoneNumber(cursor, this.contentResolver);

			detail = contact.getPhone().getData() + " - " + Phone.getTypeLabel(context.getResources(), contact.getPhone().getType(), contact.getPhone().getLabel());// contact.getPhone().getLabel();

			break;
		case PHONE_MAIL:
			contact = ContactService.getContactWithEmailOrPhone(cursor, this.contentResolver);
			detail = contact.getPhone() != null ? contact.getPhone().getData() : contact.getEmail().getData();

			break;

		default:
			break;
		}

		txtView.setContact(contact.getDisplayName(), detail, contact.getPhotoBitmap());
	}

	@Override
	public View newView(Context ctx, Cursor cursor, ViewGroup root) {
		ContactView view = new ContactView(ctx);
		fillView(cursor, view);
		return view;
	}

	@Override
	public String convertToString(Cursor cursor) {
		Contact contact = null;
		switch (type) {
		case ADRESS:
			contact = ContactService.getContactWithAdress(cursor, this.contentResolver);
			return contact.getAdress().getFormatted_address();
		case MAIL:
			contact = ContactService.getContactWithEmail(cursor, this.contentResolver);
			return contact.getDisplayName() + "<" + contact.getEmail().getData() + ">";
		case PHONE:
			contact = ContactService.getContactWithPhoneNumber(cursor, this.contentResolver);
			return contact.getDisplayName() + "<" + contact.getPhone().getData() + ">";
		case PHONE_MAIL:
			contact = ContactService.getContactWithEmailOrPhone(cursor, this.contentResolver);
			return contact.getDisplayName() + "<" + (contact.getPhone() != null ? contact.getPhone().getData() : contact.getEmail().getData()) + ">";

		default:
			return null;
		}
	}

	@Override
	public Cursor runQueryOnBackgroundThread(CharSequence constraint) {
		FilterQueryProvider filter = getFilterQueryProvider();
		if (filter != null) {
			return filter.runQuery(constraint);
		}
		switch (type) {
		case ADRESS:
		default:
			return ContactService.getContactsWithAdressCursor(contentResolver, constraint.toString());
		case MAIL:
			return ContactService.getContactsWithEmailCursor(contentResolver, constraint.toString());
		case PHONE:
			return ContactService.getContactsWithPhoneNumberCursor(contentResolver, constraint.toString());

		case PHONE_MAIL:
			return ContactService.getContactsWithEmailAndPhoneCursor(contentResolver, constraint.toString());
		}

	}

	public static enum TypeDatas {
		ADRESS, //
		PHONE, //
		MAIL, //
		PHONE_MAIL;
	}

}
