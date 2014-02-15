package com.ggacalendar.test;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.ggacalendar.GGACalendar;

public class MainActivity extends FragmentActivity
{
	private GGACalendar calendar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		if(null == savedInstanceState)
		{
			Bundle b = new Bundle();
			b.putInt(GGACalendar.FIRST_DAY_OF_WEEK, GGACalendar.DAY_MON);
			
			calendar = new GGACalendar();
			calendar.setArguments(b);
			
			getSupportFragmentManager().beginTransaction().add(R.id.main_layout, calendar).commit();
		}
	}
}