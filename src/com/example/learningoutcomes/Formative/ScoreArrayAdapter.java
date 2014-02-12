package com.example.learningoutcomes.Formative;

import java.util.List;

import com.example.learningoutcomes.R;

import android.app.Activity;
import android.content.Context;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

public class ScoreArrayAdapter extends ArrayAdapter<ScoreClass> {

	private final List<ScoreClass> list;
	private final Activity context;
	TextView totalScore;
	/* To determine whether it is student score screen or Activity screen */
	Boolean studentScore;
	TextView grade;

	public ScoreArrayAdapter(Activity context, List<ScoreClass> list,
			TextView totalScore, TextView grade, Boolean studentScore) {
		super(context, R.layout.parameter_score_row, list);
		this.context = context;
		this.list = list;
		this.totalScore = totalScore;
		this.grade = grade;
		this.studentScore = studentScore;
	}

	static class ViewHolder {
		protected TextView textName;
		protected TextView textScore;
		protected ImageView downArrow;
		protected ImageView upArrow;
		protected EditText editText;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;
		if (convertView == null) {
			LayoutInflater inflator = context.getLayoutInflater();
			view = inflator.inflate(R.layout.parameter_score_row, null);
			final ViewHolder viewHolder = new ViewHolder();
			viewHolder.textName = (TextView) view
					.findViewById(R.id.tvScoreName);
			viewHolder.textScore = (TextView) view.findViewById(R.id.tvScore);
			viewHolder.downArrow = (ImageView) view
					.findViewById(R.id.ibDownArrow);
			viewHolder.upArrow = (ImageView) view
					.findViewById(R.id.ibUpArrow);
			viewHolder.editText = (EditText) view.findViewById(R.id.etScore);
			viewHolder.textScore.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
							0, LayoutParams.WRAP_CONTENT, 4.0f);
					viewHolder.editText.setLayoutParams(param);
					viewHolder.editText.setVisibility(View.VISIBLE);
					viewHolder.textScore.setVisibility(View.INVISIBLE);
					viewHolder.upArrow.setVisibility(View.INVISIBLE);
					viewHolder.downArrow.setVisibility(View.INVISIBLE);
					viewHolder.editText.requestFocus();
					final InputMethodManager inputMethodManager = (InputMethodManager) context
							.getSystemService(Context.INPUT_METHOD_SERVICE);
					inputMethodManager.showSoftInput(viewHolder.editText,
							InputMethodManager.SHOW_IMPLICIT);
					/*
					 * String temp = viewHolder.textScore.getText().toString()
					 * .split("/")[0];
					 * viewHolder.editText.setText(viewHolder.textScore
					 * .getText() .toString());
					 * 
					 * viewHolder.editText.setSelection(viewHolder.editText
					 * .length());
					 */
					/*
					 * if (viewHolder.textScore.getText().toString().equals(""))
					 * { viewHolder.editText.setText(0 + "");
					 * viewHolder.editText.setSelection(viewHolder.editText
					 * .length()); }
					 */

				}
			});

			viewHolder.editText
					.setOnEditorActionListener(new OnEditorActionListener() {

						@Override
						public boolean onEditorAction(TextView v, int actionId,
								KeyEvent event) {
							// TODO Auto-generated method stub
							if (actionId == EditorInfo.IME_ACTION_DONE) {
								final InputMethodManager inputMethodManager = (InputMethodManager) context
										.getSystemService(Context.INPUT_METHOD_SERVICE);
								inputMethodManager.hideSoftInputFromWindow(
										viewHolder.editText.getWindowToken(), 0);

								LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
										0, LayoutParams.WRAP_CONTENT, 0.0f);
								viewHolder.editText.setLayoutParams(param);
								viewHolder.editText
										.setVisibility(View.INVISIBLE);
								viewHolder.textScore
										.setVisibility(View.VISIBLE);
								viewHolder.upArrow.setVisibility(View.VISIBLE);
								viewHolder.downArrow
										.setVisibility(View.VISIBLE);
								ScoreClass element = (ScoreClass) viewHolder.downArrow
										.getTag();
								if (viewHolder.editText.getText().toString()
										.contentEquals("")) {
									return false;
								}
								int score = Integer
										.parseInt(viewHolder.editText.getText()
												.toString());
								if (score < 0)
									Toast.makeText(context,
											"Score cannot be negative",
											Toast.LENGTH_SHORT).show();
								else if ((studentScore)
										& (element.getMaxScore() < score)) {
									Toast.makeText(
											context,
											"Score cannot be more than maximum marks",
											Toast.LENGTH_SHORT).show();
								} else {
									int total = Integer.parseInt(totalScore
											.getText().toString());
									total += score - element.getScore();
									totalScore.setText("" + total);
									if (studentScore) {
										/* Set Grade Here */
									}
									element.setScore(score);

								}
								if (studentScore) {
									viewHolder.textScore.setText(+element
											.getScore()
											+ "/"
											+ element.getMaxScore());
								} else
									viewHolder.textScore.setText(""
											+ element.getScore());

							}
							return true;
						}
					});
			viewHolder.downArrow.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					ScoreClass element = (ScoreClass) viewHolder.downArrow
							.getTag();
					if (element.getScore() < 1)
						Toast.makeText(context, "Score cannot be negative",
								Toast.LENGTH_SHORT).show();
					else {
						int total = Integer.parseInt(totalScore.getText()
								.toString());
						total -= 1;
						totalScore.setText("" + total);
						if (studentScore) {
							/* Set Grade Here */
						}
						element.setScore(element.getScore() - 1);
					}
					if (studentScore) {
						viewHolder.textScore.setText(element.getScore() + "/"
								+ element.getMaxScore());
					} else
						viewHolder.textScore.setText("" + element.getScore());
				}
			});
			viewHolder.upArrow.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					ScoreClass element = (ScoreClass) viewHolder.downArrow
							.getTag();
					if (element.getScore() >= element.getMaxScore()
							& studentScore)
						Toast.makeText(context,
								"Score cannot be more than Max Marks",
								Toast.LENGTH_SHORT).show();
					else {
						int total = Integer.parseInt(totalScore.getText()
								.toString());
						total += 1;
						totalScore.setText("" + total);
						if (studentScore) {
							/* Set Grade Here */
						}
						element.setScore(element.getScore() + 1);
					}

					if (studentScore) {
						viewHolder.textScore.setText(element.getScore() + "/"
								+ element.getMaxScore());
					} else
						viewHolder.textScore.setText("" + element.getScore());
				}
			});
			view.setTag(viewHolder);
			viewHolder.downArrow.setTag(list.get(position));
		} else {
			view = convertView;
			((ViewHolder) view.getTag()).downArrow.setTag(list.get(position));
		}
		ViewHolder holder = (ViewHolder) view.getTag();

		if (studentScore) {
			holder.textScore.setText(list.get(position).getScore() + "/"
					+ list.get(position).getMaxScore());
		} else
			holder.textScore.setText(list.get(position).getScore() + "");
		holder.textName.setText(list.get(position).getName());
		return view;
	}

	public List<ScoreClass> getData() {
		return this.list;
	}
}
