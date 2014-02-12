package com.example.learningoutcomes.Formative;

import com.example.learningoutcomes.R;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

/**
 * Decorator Adapter to allow a Spinner to show a 'Nothing Selected...'
 * initially displayed instead of the first choice in the Adapter.
 */
public class NothingSelectedSpinnerAdapter implements SpinnerAdapter,
		ListAdapter {

	protected static final int EXTRA = 1;
	protected SpinnerAdapter adapter;
	protected Context context;
	protected int nothingSelectedLayout;
	protected int nothingSelectedDropdownLayout;
	protected LayoutInflater layoutInflater;
	protected LayoutInflater viewLayoutInflator;
	protected String promptessage;

	protected String scrollTitle;

	/**
	 * Use this constructor to have NO 'Select One...' item, instead use the
	 * standard prompt or nothing at all.
	 * 
	 * @param spinnerAdapter
	 *            wrapped Adapter.
	 * @param nothingSelectedLayout
	 *            layout for nothing selected, perhaps you want text grayed out
	 *            like a prompt...
	 * @param context
	 */
	public NothingSelectedSpinnerAdapter(SpinnerAdapter spinnerAdapter,
			int nothingSelectedLayout, Context context, String message,
			String scrollTitle) {

		this(spinnerAdapter, nothingSelectedLayout, -1, context, message,
				scrollTitle);
	}

	/**
	 * Use this constructor to Define your 'Select One...' layout as the first
	 * row in the returned choices. If you do this, you probably don't want a
	 * prompt on your spinner or it'll have two 'Select' rows.
	 * 
	 * @param spinnerAdapter
	 *            wrapped Adapter. Should probably return false for isEnabled(0)
	 * @param nothingSelectedLayout
	 *            layout for nothing selected, perhaps you want text grayed out
	 *            like a prompt...
	 * @param nothingSelectedDropdownLayout
	 *            layout for your 'Select an Item...' in the dropdown.
	 * @param context
	 */
	public NothingSelectedSpinnerAdapter(SpinnerAdapter spinnerAdapter,
			int nothingSelectedLayout, int nothingSelectedDropdownLayout,
			Context context, String message, String scrollTitle) {
		this.adapter = spinnerAdapter;
		this.context = context;
		this.nothingSelectedLayout = nothingSelectedLayout;
		this.nothingSelectedDropdownLayout = nothingSelectedDropdownLayout;
		viewLayoutInflator = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.promptessage = message;
		this.scrollTitle = scrollTitle;
	}

	@Override
	public final View getView(int position, View convertView, ViewGroup parent) {
		// This provides the View for the Selected Item in the Spinner, not
		// the dropdown (unless dropdownView is not set).
		if (position == 0) {
			return getNothingSelectedView(parent);
		}
		// return adapter.getView(position - EXTRA, null, parent); // Could
		// re-use
		// the convertView if possible.
		View view;
		view = viewLayoutInflator.inflate(R.layout.spinner_main_view, parent,
				false);

		/* Get the item in the adapter */
		String myObject = (String) getItem(position);

		/* Get the widget with id name which is defined in the xml of the row */
		TextView title = (TextView) view.findViewById(R.id.spinner_title);
		TextView subtitle = (TextView) view.findViewById(R.id.spinner_value);

		/* Populate the row's xml with info from the item */
		title.setText(this.scrollTitle);

		subtitle.setText(myObject);
		/* Return the generated view */
		return view;

	}

	/**
	 * View to show in Spinner with Nothing Selected Override this to do
	 * something dynamic... e.g. "37 Options Found"
	 * 
	 * @param parent
	 * @return
	 */
	protected View getNothingSelectedView(ViewGroup parent) {
		View view = viewLayoutInflator.inflate(
				R.layout.spinner_row_nothing_selected, parent, false);
		String message = this.promptessage;
		TextView prompt = (TextView) view.findViewById(R.id.dropdown_title);

		/* Populate the row's xml with info from the item */
		prompt.setText(message);
		return view;

	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		// BUG! Vote to fix!!
		// http://code.google.com/p/android/issues/detail?id=17128 -
		// Spinner does not support multiple view types
		if (position == 0) {
			return nothingSelectedDropdownLayout == -1 ? new View(context)
					: getNothingSelectedDropdownView(parent);
		}

		// Could re-use the convertView if possible, use setTag...
		// return adapter.getDropDownView(position - EXTRA, null, parent);
		View view;

		view = viewLayoutInflator.inflate(R.layout.spinner_dropdown_view,
				parent, false);

		/* Get the widget with id name which is defined in the xml of the row */
		TextView title = (TextView) view
				.findViewById(R.id.spinner_dropdown_value);

		/* Populate the row's xml with info from the item */
		title.setText((CharSequence) getItem(position));

		/* Return the generated view */
		return view;

	}

	/**
	 * Override this to do something dynamic... For example, "Pick your favorite
	 * of these 37".
	 * 
	 * @param parent
	 * @return
	 */
	protected View getNothingSelectedDropdownView(ViewGroup parent) {
		return null;
	}

	@Override
	public int getCount() {
		int count = adapter.getCount();
		return count == 0 ? 0 : count + EXTRA;
	}

	@Override
	public Object getItem(int position) {
		return position == 0 ? null : adapter.getItem(position - EXTRA);
	}

	@Override
	public int getItemViewType(int position) {
		// Doesn't work!! Vote to Fix!
		// http://code.google.com/p/android/issues/detail?id=17128 -
		// Spinner does not support multiple view types
		// This method determines what is the convertView, this should
		// return 1 for pos 0 or return 0 otherwise.
		return position == 0 ? getViewTypeCount() - EXTRA : adapter
				.getItemViewType(position - EXTRA);
	}

	@Override
	public int getViewTypeCount() {
		return adapter.getViewTypeCount() + EXTRA;
	}

	@Override
	public long getItemId(int position) {
		return adapter.getItemId(position - EXTRA);
	}

	@Override
	public boolean hasStableIds() {
		return adapter.hasStableIds();
	}

	@Override
	public boolean isEmpty() {
		return adapter.isEmpty();
	}

	@Override
	public void registerDataSetObserver(DataSetObserver observer) {
		adapter.registerDataSetObserver(observer);
	}

	@Override
	public void unregisterDataSetObserver(DataSetObserver observer) {
		adapter.unregisterDataSetObserver(observer);
	}

	@Override
	public boolean areAllItemsEnabled() {
		return false;
	}

	@Override
	public boolean isEnabled(int position) {
		return position == 0 ? false : true; // Don't allow the 'nothing
												// selected'
												// item to be picked.
	}

}