package com.infonuascape.osrshelper.views;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;

import com.infonuascape.osrshelper.R;
import com.infonuascape.osrshelper.utils.Skill;

public class RSViewOnClickListener implements OnClickListener {
	private Skill skill;
	private Activity activity;
	private boolean mIsLargeLayout;
	
	public RSViewOnClickListener(final Activity activity, final Skill skill){
		this.skill = skill;
		this.activity = activity;
		
		mIsLargeLayout = activity.getResources().getBoolean(R.bool.large_layout);
	}

	@Override
	public void onClick(View v) {
		showDialog(skill);
	}
	
	private void showDialog(Skill skill) {
		FragmentManager fragmentManager = activity.getFragmentManager();
		HiscoresDialogFragment newFragment = new HiscoresDialogFragment(skill);

		if (mIsLargeLayout) {
			// The device is using a large layout, so show the fragment as a
			// dialog
			newFragment.show(fragmentManager, "dialog");
		} else {
			// The device is smaller, so show the fragment fullscreen
			FragmentTransaction transaction = fragmentManager.beginTransaction();
			// For a little polish, specify a transition animation
			//transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
			// To make it fullscreen, use the 'content' root view as the
			// container
			// for the fragment, which is always the root view for the activity
			transaction.add(android.R.id.content, newFragment).addToBackStack(null).commit();
		}
	}

}
