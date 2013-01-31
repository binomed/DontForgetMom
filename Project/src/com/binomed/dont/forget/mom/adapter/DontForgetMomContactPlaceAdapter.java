package com.binomed.dont.forget.mom.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.binomed.dont.forget.mom.model.Contact;
import com.binomed.dont.forget.mom.service.contact.ContactService;

public class DontForgetMomContactPlaceAdapter extends CursorAdapter implements Filterable {

	public DontForgetMomContactPlaceAdapter(Context context, Cursor c) {
		super(context, c, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
	}

	@Override
	public void bindView(View view, Context ctx, Cursor cursor) {
		TextView txtView = null;
		if (view == null) {
			txtView = new TextView(ctx);
		} else {
			txtView = (TextView) view;
		}

		Contact contact = ContactService.getContact(cursor, null);

		txtView.setText(contact.getDisplayName());

	}

	@Override
	public View newView(Context ctx, Cursor cursor, ViewGroup root) {
		TextView txtView = null;
		txtView = new TextView(ctx);

		Contact contact = ContactService.getContact(cursor, null);

		txtView.setText(contact.getDisplayName());
		return txtView;
	}

	@Override
	public Filter getFilter() {
		// TODO Auto-generated method stub
		return super.getFilter();
	}

}
