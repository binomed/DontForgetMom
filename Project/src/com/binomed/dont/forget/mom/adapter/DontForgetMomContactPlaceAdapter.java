package com.binomed.dont.forget.mom.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.binomed.dont.forget.mom.model.Contact;
import com.binomed.dont.forget.mom.service.contact.ContactService;
import com.binomed.dont.forget.mom.view.ContactView;

public class DontForgetMomContactPlaceAdapter extends CursorAdapter implements Filterable {

	public DontForgetMomContactPlaceAdapter(Context context, Cursor c) {
		super(context, c, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
	}

	@Override
	public void bindView(View view, Context ctx, Cursor cursor) {
		ContactView txtView = null;
		if (view == null) {
			txtView = new ContactView(ctx);
		} else {
			txtView = (ContactView) view;
		}

		Contact contact = ContactService.getContact(cursor, null);

		txtView.setContact(contact.getDisplayName(), "", null);

	}

	@Override
	public View newView(Context ctx, Cursor cursor, ViewGroup root) {
		ContactView txtView = null;
		txtView = new ContactView(ctx);

		Contact contact = ContactService.getContact(cursor, null);

		txtView.setContact(contact.getDisplayName(), "", null);
		return txtView;
	}

	@Override
	public Filter getFilter() {
		// TODO Auto-generated method stub
		return super.getFilter();
	}

}
