package com.infonuascape.osrshelper.views;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.infonuascape.osrshelper.R;

public class HiscoresDialogFragment extends DialogFragment {
	/**
	 * The system calls this to get the DialogFragment's layout, regardless of
	 * whether it's being displayed as a dialog or an embedded fragment.
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout to use as dialog or embedded fragment
		return inflater.inflate(R.layout.hiscores_dialog, container, false);
	}

	/** The system calls this only when creating the layout in a dialog. */
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Dialog dialog = super.onCreateDialog(savedInstanceState);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		return dialog;
	}
}