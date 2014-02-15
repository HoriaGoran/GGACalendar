GGACalendar
===========

Calendar Library for Android.
Can be used with Android 2.2 and above.

Setup
==============
- reference the GGACalendar library in your project propreties.
- create an instance of the GGACalendar fragment:
```
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
```

![GGACalendarLib](https://raw2.github.com/HoriaGoran/GGACalendar/master/GGACalendarLib/Screenshot.png)
