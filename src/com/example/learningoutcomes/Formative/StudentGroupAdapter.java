package com.example.learningoutcomes.Formative;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.learningoutcomes.R;

public class StudentGroupAdapter extends ArrayAdapter<Group> {

	private final List<Group> list;
	private final Activity context;

	public StudentGroupAdapter(Activity context, List<Group> list) {
		super(context, R.layout.group_listview_row, list);
		this.context = context;
		this.list = list;
	}

	static class ViewHolder {
		protected TextView tvGroupName;
		protected TextView tvStudentNames;
	}

	public List<Group> getData() {
		return this.list;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;
		if (convertView == null) {
			LayoutInflater inflator = context.getLayoutInflater();
			view = inflator.inflate(R.layout.group_listview_row, null);
			final ViewHolder viewHolder = new ViewHolder();
			viewHolder.tvGroupName = (TextView) view
					.findViewById(R.id.tvGroupName);
			viewHolder.tvStudentNames = (TextView) view
					.findViewById(R.id.tvGroupStudents);
			view.setTag(viewHolder);
		} else {
			view = convertView;
		}
		ViewHolder holder = (ViewHolder) view.getTag();
		holder.tvGroupName.setText(list.get(position).groupName);
		List<String> temp = new ArrayList<String>();
		temp = list.get(position).studentname;
		String names = "";
		for (int i = 0; i < temp.size(); i++) {
			names += list.get(position).studentname.get(i);
			names += ", ";
		}
		/* Remove the last comma which was put in the for loop above */
		names = names.substring(0, names.length() - 2);
		holder.tvStudentNames.setText(names);
		return view;
	}
}
