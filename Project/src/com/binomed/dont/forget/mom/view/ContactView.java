package com.binomed.dont.forget.mom.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.binomed.dont.forget.mom.R;

public class ContactView extends RelativeLayout {

	// @InjectView(R.id.contactImg)
	ImageView contactImg;
	// @InjectView(R.id.contactDisplayName)
	TextView contactDisplayName;
	// @InjectView(R.id.contactDetail)
	TextView contactDetail;

	public ContactView(Context context) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.view_contacts, this);
		// RoboGuice.getInjector(context).injectMembers(this);
		contactImg = (ImageView) findViewById(R.id.contactImg);
		contactDisplayName = (TextView) findViewById(R.id.contactDisplayName);
		contactDetail = (TextView) findViewById(R.id.contactDetail);

	}

	public void setContact(String displayName, String detail, Bitmap img) {
		contactDisplayName.setText(displayName);
		contactDetail.setText(detail);
		if (img == null) {
			contactImg.setImageResource(R.drawable.ic_contact_picture);
		} else {
			contactImg.setImageBitmap(img);
		}

	}
}
