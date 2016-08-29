package io.github.whataa.dater;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateTimeChooseDialog implements OnDateChangedListener,View.OnClickListener,
		OnTimeChangedListener, ViewPager.OnPageChangeListener {

	private Context context;

	// 日期时间选择器
	private DatePicker datePicker;
	private TimePicker timePicker;

	private TextView tvTitle;
	private TextView tvDate, tvTime,tvBtn1,tvBtn2;
	private ViewPager viewPager;

	protected AlertDialog dialog;
	private Calendar calendar;

	// 初始时间
	private long initDateTime = 0;
	private String format = null;
	private String initDateTimeStr = null;
	private DateTimeChooseDialogCallBack mCallBack;

	public DateTimeChooseDialog(Context context, String initDateTime,
			String format) {
		this.context = context;
		this.initDateTimeStr = initDateTime;
	}

	public DateTimeChooseDialog(Context context, long initDateTime) {
		this.context = context;
		this.initDateTime = initDateTime;
	}

	/**
	 * 初始化日期时间
	 * 
	 * @param datePicker
	 * @param timePicker
	 */
	@SuppressWarnings({ "deprecation", "unused" })
	public void initDate(DatePicker datePicker, TimePicker timePicker) {
		if (initDateTime == 0) {
			if (initDateTimeStr != null) {
				SimpleDateFormat dateFormat = null;
				if (format == null) {
					dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
				} else {
					dateFormat = new SimpleDateFormat(format, Locale.CHINA);
				}
				Date date = null;
				try {
					dateFormat.parse(initDateTimeStr);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				if (date != null) {
					if (date.getYear() == 70) {
						if (format.indexOf('y') == -1) {
							date.setYear(new Date().getYear());
						}
					}
					initDateTime = date.getTime();
				}
			}
		}
		if (initDateTime == 0) {
			initDateTime = System.currentTimeMillis();
		}
		calendar = Calendar.getInstance();
		calendar.setTimeInMillis(initDateTime);
		datePicker.init(calendar.get(Calendar.YEAR),
				calendar.get(Calendar.MONTH),
				calendar.get(Calendar.DAY_OF_MONTH), this);
		timePicker.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));
		timePicker.setCurrentMinute(calendar.get(Calendar.MINUTE));
	}

	/**
	 * 显示日期时间选择器
	 */
	public void show(DateTimeChooseDialogCallBack callBack) {
		mCallBack = callBack;
		LinearLayout dateTimeLayout = (LinearLayout) View.inflate(context, R.layout.dialog_common_alter_time_choose, null);
		tvTitle = (TextView) dateTimeLayout.findViewById(R.id.picker_title);
		tvDate = (TextView) dateTimeLayout.findViewById(R.id.picker_date_set);
		tvTime = (TextView) dateTimeLayout.findViewById(R.id.picker_time_set);
		tvBtn1 = (TextView) dateTimeLayout.findViewById(R.id.picker_btn_1);
		tvBtn2 = (TextView) dateTimeLayout.findViewById(R.id.picker_btn_2);
		tvDate.setOnClickListener(this);
		tvTime.setOnClickListener(this);
		tvBtn1.setOnClickListener(this);
		tvBtn2.setOnClickListener(this);

		datePicker = new DatePicker(new ContextThemeWrapper(context, android.R.style.Theme_Holo_Light));
		datePicker.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		chargeToHideYearPicker();
		datePicker.setCalendarViewShown(false);
		timePicker = new TimePicker(new ContextThemeWrapper(context, android.R.style.Theme_Holo_Light));
		timePicker.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

		initDate(datePicker, timePicker);
		timePicker.setIs24HourView(true);
		timePicker.setOnTimeChangedListener(this);

		viewPager = (ViewPager) dateTimeLayout.findViewById(R.id.picker_viewpager);
		viewPager.setAdapter(new PickerAdapter(datePicker, timePicker));
		viewPager.addOnPageChangeListener(this);

		dialog = new AlertDialog.Builder(new ContextThemeWrapper(context,
				android.R.style.Theme_Holo_Light))
				.setView(dateTimeLayout)
				.show();
		WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
		params.width = context.getResources().getDisplayMetrics().widthPixels - dp2px(context,32);
		params.height = dp2px(context, 360);
		dialog.getWindow().setAttributes(params);
		refreshDialogTitle();
	}

	// 如果当前时间在年末7天内，则显示“年”选择
	private void chargeToHideYearPicker() {
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR));
		calendar.roll(Calendar.DAY_OF_YEAR, -1);
		if (Math.abs(System.currentTimeMillis() - calendar.getTimeInMillis()) > 7*24*60*60) {
			// 隐藏“年”
			((ViewGroup) (((ViewGroup) datePicker.getChildAt(0)).getChildAt(0))).getChildAt(0).setVisibility(View.GONE);
		}
	}

	/**
	 * 刷新标题
	 */
	public void refreshDialogTitle() {
		calendar = Calendar.getInstance();
		calendar.set(datePicker.getYear(), datePicker.getMonth(),
				datePicker.getDayOfMonth(), timePicker.getCurrentHour(),
				timePicker.getCurrentMinute());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy",Locale.CHINA);
		SimpleDateFormat sdf1 = new SimpleDateFormat("MM月dd日E",Locale.CHINA);
		SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm",Locale.CHINA);
		tvTitle.setText(sdf.format(calendar.getTime()));
		tvDate.setText(sdf1.format(calendar.getTime()));
		tvTime.setText(sdf2.format(calendar.getTime()));
	}

	@Override
	public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
		refreshDialogTitle();
	}

	@Override
	public void onDateChanged(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {
		refreshDialogTitle();
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.picker_date_set) {
			viewPager.setCurrentItem(0);
		} else if (v.getId() == R.id.picker_time_set){
			viewPager.setCurrentItem(1);
		} else if (v.getId() == R.id.picker_btn_1) {
			try {
				dialog.dismiss();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (v.getId() == R.id.picker_btn_2) {
			if (viewPager.getCurrentItem() == 0) {
				viewPager.setCurrentItem(1);
			} else {
				if (mCallBack != null) {
					mCallBack.callBack(calendar.getTimeInMillis());
				}
				try {
					dialog.dismiss();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

	}

	@Override
	public void onPageSelected(int position) {
		if (position == 0) {
			tvDate.setTextColor(Color.WHITE);
			tvTime.setTextColor(Color.parseColor("#77ffffff"));
			tvBtn2.setText("下一步");
		} else {
			tvTime.setTextColor(Color.WHITE);
			tvDate.setTextColor(Color.parseColor("#77ffffff"));
			tvBtn2.setText("确定");
		}
	}

	@Override
	public void onPageScrollStateChanged(int state) {
	}

	public interface DateTimeChooseDialogCallBack {
		void callBack(long time);
	}

	static int dp2px(Context context, int dp) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dp * scale + 0.5f);
	}

	static class PickerAdapter extends PagerAdapter {

		DatePicker datePicker;
		TimePicker timePicker;
		public PickerAdapter(DatePicker datePicker, TimePicker timePicker) {
			this.datePicker = datePicker;
			this.timePicker = timePicker;
		}

		@Override
		public int getCount() {
			return 2;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			container.addView(position == 0 ? datePicker : timePicker);
			return position == 0 ? datePicker : timePicker;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(position == 0 ? datePicker : timePicker);
		}
	}
}
