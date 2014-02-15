package com.ggacalendar;

import java.util.Calendar;
import java.util.Random;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class GGACalendar extends Fragment
{
	public static final int DAY_SUN = Calendar.SUNDAY;
	public static final int DAY_MON = Calendar.MONDAY;
	public static final String FIRST_DAY_OF_WEEK = "first_day_of_week";
	private final int COLS = 7;
	private final int ROWS = 6;
	private final String [] weekDaysMonFirst = { "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun" };
	private final String [] weekDaysSunFirst = { "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat" };
	private Activity activity;
	private LinearLayout calendar_layout;
	private ImageView arrow_left;
	private ImageView arrow_right;
	private TextView month_name;
	private int current_day;
	private int current_month;
	private int first_day_of_week;
	private int month;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.calendar_layout, container, false);
	}


	@Override
	public void onViewCreated(View view, Bundle savedInstanceState)
	{
		super.onViewCreated(view, savedInstanceState);
		
		Bundle b = getArguments();
		if(null != b)
		{
			first_day_of_week = b.getInt(FIRST_DAY_OF_WEEK, DAY_MON);
		}

		activity = getActivity();
		calendar_layout = (LinearLayout) view.findViewById(R.id.calendar_layout);
		arrow_left = (ImageView) view.findViewById(R.id.month_left);
		arrow_right = (ImageView) view.findViewById(R.id.month_right);
		month_name = (TextView) view.findViewById(R.id.month_name);
		
		arrow_left.setOnClickListener(onClick);
		arrow_right.setOnClickListener(onClick);
		
		Calendar cal = Calendar.getInstance();
		current_day = cal.get(Calendar.DAY_OF_MONTH);
		current_month = cal.get(Calendar.MONTH);
		month = current_month;
		
		doCalendar(month);
	}

	
	private void doCalendar(int month)
	{
		calendar_layout.removeAllViews();
		
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MONTH, month);
		cal.setFirstDayOfWeek(first_day_of_week);
		
		if(DAY_MON == first_day_of_week)
		{
			cal.set(Calendar.DAY_OF_MONTH, 0);
		}
		else if(DAY_SUN == first_day_of_week)
		{
			cal.set(Calendar.DAY_OF_MONTH, 1);
		}
		
		final int daysSinceMonday = cal.get(Calendar.DAY_OF_WEEK);
		final int cell_size = getScreenSize(activity).x / 7;
		
		cal = Calendar.getInstance();
		cal.set(Calendar.MONTH, month);
		month_name.setText(getMonth(cal.get(Calendar.MONTH)) + ", " + cal.get(Calendar.YEAR));
		
	    int maxDaysPreviuosMonth = getMaxDaysPreviousMonth(month);
	    int maxDaysCurrentMonth = getMaxDaysCurrentMonth(month);
	    int header_color = Color.parseColor("#FF6600");
	    int color_res = R.drawable.bkg_white_orange;
	    int img_res = R.drawable.mark_with_color;
	    int count = 1;
	    String day = null;
	    Random rand = new Random();
		for(int i = 0; i <= ROWS; i++)
		{
			LinearLayout row = new LinearLayout(activity);
			
			for(int j = 1; j <= COLS; j++)
			{
				View cell = View.inflate(activity, R.layout.cell, null);
				TextView cell_text = (TextView) cell.findViewById(R.id.cell_text);
				ImageView cell_mark = (ImageView) cell.findViewById(R.id.cell_mark);
				
				if(i > 0)
				{
					cell_mark.setVisibility(rand.nextInt(10) == 1 ? View.VISIBLE : View.GONE);
					cell.setOnClickListener(new OnClickListener()
					{
						@Override
						public void onClick(View cell)
						{
							TextView cell_text = (TextView) cell.findViewById(R.id.cell_text);
							if(null != cell_text)
							{
								String text = cell_text.getText().toString();
								Toast.makeText(activity, text, Toast.LENGTH_SHORT).show();
							}
						}
					});
				}
				
				if(i == 0)
				{//the days header
					if(DAY_MON == first_day_of_week)
					{
						day = weekDaysMonFirst[j - 1];
					}
					else if(DAY_SUN == first_day_of_week)
					{
						day = weekDaysSunFirst[j - 1];
					}
					
					cell.setLayoutParams(new RelativeLayout.LayoutParams(cell_size, cell_size / 2));
					cell.setBackgroundColor(header_color);
					cell_text.setTextSize(16);
				}
				else if(i == 1 && j < daysSinceMonday)
				{//the days from the previous month
					day = String.valueOf(maxDaysPreviuosMonth - daysSinceMonday + j + 1);
					cell_mark.setImageResource(R.drawable.mark_no_color);
					cell.setLayoutParams(new LinearLayout.LayoutParams(cell_size, cell_size));
					cell.setBackgroundResource(R.drawable.bkg_gray_orange);
				}
				else if(count > maxDaysCurrentMonth)
				{//the days from the next month
					count = 1;
					color_res = R.drawable.bkg_gray_orange;
					img_res = R.drawable.mark_no_color;
					day = String.valueOf(count++);
					cell_mark.setImageResource(img_res);
					cell.setLayoutParams(new RelativeLayout.LayoutParams(cell_size, cell_size));
					cell.setBackgroundResource(color_res);
				}
				else
				{//the days from the current month
					if(count == current_day && month == current_month)
					{
						cell.setBackgroundResource(R.drawable.bkg_cyan_orange);
					}
					else
					{
						cell.setBackgroundResource(color_res);
					}
					
					day = String.valueOf(count++);
					cell.setLayoutParams(new RelativeLayout.LayoutParams(cell_size, cell_size));
					cell_mark.setImageResource(img_res);
				}
			
				cell_text.setText(day);
				row.setOrientation(LinearLayout.HORIZONTAL);
				row.setGravity(Gravity.CENTER);
				row.addView(cell);
				
				if(i > 0 && j < COLS)
				{
					row.addView(newVerticalSeparator(Color.BLACK));
				}
				else
				{
					row.addView(newVerticalSeparator(header_color));
				}
			}
			
			calendar_layout.addView(row);
			calendar_layout.addView(newHorizontalSeparator());
		}
		cal = null;
	}
	
	
	@Override
	public void onDetach()
	{
		super.onDetach();
		activity = null;
	}


	private OnClickListener onClick = new OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			int id = v.getId();
			if (id == R.id.month_left)
			{
				doCalendar(--month);
			}
			else if (id == R.id.month_right)
			{
				doCalendar(++month);
			}
		}
	};
	
	
	private String getMonth(int month)
	{
		switch(month)
		{
			case Calendar.JANUARY:
				return "January";
			case Calendar.FEBRUARY:
				return "February";
			case Calendar.MARCH:
				return "March";
			case Calendar.APRIL:
				return "April";
			case Calendar.MAY:
				return "May";
			case Calendar.JUNE:
				return "June";
			case Calendar.JULY:
				return "July";
			case Calendar.AUGUST:
				return "August";
			case Calendar.SEPTEMBER:
				return "September";
			case Calendar.OCTOBER:
				return "October";
			case Calendar.NOVEMBER:
				return "November";
			case Calendar.DECEMBER:
				return "December";
			default:
				return "wrong month";
		}
	}
	
	
	private int getMaxDaysPreviousMonth(int month)
	{
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MONTH, month - 1);
		int max = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		cal = null;
		
		return max;
	}
	
	
	private int getMaxDaysCurrentMonth(int month)
	{
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MONTH, month);
		int max = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		cal = null;
		
		return max;
	}
	
	
	private View newHorizontalSeparator()
	{
		View separator_horizontal = new View(activity);
		separator_horizontal.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 1));
		separator_horizontal.setBackgroundColor(Color.BLACK);
		return separator_horizontal;
	}
	
	
	private View newVerticalSeparator(int color)
	{
		View separator_vertical = new View(activity);
		separator_vertical.setLayoutParams(new LayoutParams(1, LayoutParams.MATCH_PARENT));
		separator_vertical.setBackgroundColor(color);
		return separator_vertical;
	}
	
	

	@SuppressLint("NewApi")
	private Point getScreenSize(Context context)
	{
		if(null == context)
		{
			return null;
		}
		
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		Point size = new Point();
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2)
		{
			display.getSize(size);
		}
		else
		{
			size.x = display.getWidth();
			size.y = display.getHeight();
        }
		
		wm = null;
		display = null;
		context = null;
		
		return size;
	}
}