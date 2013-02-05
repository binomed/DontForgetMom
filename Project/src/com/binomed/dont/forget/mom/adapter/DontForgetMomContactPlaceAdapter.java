package com.binomed.dont.forget.mom.adapter;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FilterQueryProvider;
import android.widget.Filterable;

import com.binomed.dont.forget.mom.model.Contact;
import com.binomed.dont.forget.mom.service.contact.ContactService;
import com.binomed.dont.forget.mom.view.ContactView;

public class DontForgetMomContactPlaceAdapter extends CursorAdapter implements Filterable {

	private ContentResolver contentResolver;

	public DontForgetMomContactPlaceAdapter(Context context, Cursor c) {
		super(context, c, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
		contentResolver = context.getContentResolver();
	}

	@Override
	public void bindView(View view, Context ctx, Cursor cursor) {
		ContactView txtView = null;
		if (view == null) {
			txtView = new ContactView(ctx);
		} else {
			txtView = (ContactView) view;
		}

		Contact contact = ContactService.getContact(cursor, this.contentResolver);

		txtView.setContact(contact.getDisplayName(), contact.getAddresses().size() > 0 ? contact.getAddresses().get(0).getFormatted_address() : null, contact.getPhotoBitmap());

	}

	@Override
	public View newView(Context ctx, Cursor cursor, ViewGroup root) {
		ContactView txtView = new ContactView(ctx);
		Contact contact = ContactService.getContact(cursor, this.contentResolver);
		txtView.setContact(contact.getDisplayName(), contact.getAddresses().size() > 0 ? contact.getAddresses().get(0).getFormatted_address() : null, contact.getPhotoBitmap());
		return txtView;
	}

	@Override
	public String convertToString(Cursor cursor) {
		Contact contact = ContactService.getContact(cursor, this.contentResolver);
		return contact.getDisplayName();
	}

	@Override
	public Cursor runQueryOnBackgroundThread(CharSequence constraint) {
		FilterQueryProvider filter = getFilterQueryProvider();
		if (filter != null) {
			return filter.runQuery(constraint);
		}

		return ContactService.getContactsWithAdressCursor(contentResolver, constraint.toString());
		// return ContactService.getContactsCursor(contentResolver);

		// Uri uri = Uri.withAppendedPath(Contacts.CONTENT_FILTER_URI, Uri.encode(constraint.toString()));
		// return mContent.query(uri, CONTACT_PROJECTION, null, null, null);
	}

}
