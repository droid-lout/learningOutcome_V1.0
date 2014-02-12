package com.example.learningoutcomes.Formative;

import java.util.List;

import com.example.learningoutcomes.R;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class TaskListRowDataAdapter extends ArrayAdapter<TaskListRow> {

	private Activity context;
	private List<TaskListRow> list;
	private View.OnClickListener editTaskListener, scoreTaskListener, viewTaskListener;
	
	public TaskListRowDataAdapter(Activity context, 
			List<TaskListRow> list,
			View.OnClickListener editTaskListener,
			View.OnClickListener scoreTaskListener, 
			View.OnClickListener viewTaskListener) {
		super(context, R.layout.task_list_lv_row, list);
		this.context = context;
		this.list = list;
		this.editTaskListener = editTaskListener;
		this.scoreTaskListener = scoreTaskListener;
		this.viewTaskListener = viewTaskListener;
	}

	static class ViewHolder {
		protected TextView taskNum;
		protected TextView taskName;
		protected TextView date;
		protected TextView topicName;
		protected TextView group;
		protected TextView maxMarks;
		
		//Buttons
		protected TextView taskEdit;
		protected TextView taskScore;
		protected TextView taskView;
	}

	public List<TaskListRow> getData() {
		return this.list;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;
		if (convertView == null) {
			LayoutInflater inflator = context.getLayoutInflater();
			view = inflator.inflate(R.layout.task_list_lv_row, null);
			final ViewHolder viewHolder = new ViewHolder();
			viewHolder.taskNum = (TextView) view.findViewById(R.id.tvTaskNum);
			viewHolder.taskName = (TextView) view.findViewById(R.id.tvTaskName);
			viewHolder.date = (TextView) view.findViewById(R.id.tvDate);
			viewHolder.topicName = (TextView) view.findViewById(R.id.tvTopicName);
			viewHolder.group = (TextView) view.findViewById(R.id.tvGroup);
			viewHolder.maxMarks = (TextView) view.findViewById(R.id.tvMaxMarks);
			viewHolder.taskEdit = (TextView) view.findViewById(R.id.tvTaskEdit);
			viewHolder.taskScore = (TextView) view.findViewById(R.id.tvTaskScore);
			viewHolder.taskView = (TextView) view.findViewById(R.id.tvTaskView);
			view.setTag(viewHolder);
		} else {
			view = convertView;
		}
		ViewHolder holder = (ViewHolder) view.getTag();
		holder.taskNum.setText(list.get(position).getTaskNumber() + "");
		holder.taskName.setText(list.get(position).getTaskName());
		holder.date.setText(list.get(position).getTaskDate());
		holder.topicName.setText(list.get(position).getTaskTopicName());
		if(list.get(position).ifGroup())
			holder.group.setText("G");
		else
			holder.group.setText("");
		holder.maxMarks.setText("Max - " + list.get(position).getTaskMaxMarks());
		
		//Buttons
		holder.taskEdit.setOnClickListener(editTaskListener);
		holder.taskScore.setOnClickListener(scoreTaskListener);
		holder.taskView.setOnClickListener(viewTaskListener);

		return view;
	}
	
}
