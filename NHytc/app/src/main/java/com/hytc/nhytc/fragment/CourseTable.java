package com.hytc.nhytc.fragment;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.hytc.nhytc.R;
import com.hytc.nhytc.activity.CourseDetailActivity;
import com.hytc.nhytc.activity.MainCourseActivity;
import com.hytc.nhytc.dbDAO.CourseDao;
import com.hytc.nhytc.domain.CustomDate;
import com.hytc.nhytc.domain.Lesson;
import com.hytc.nhytc.tool.GestureListener;
import com.hytc.nhytc.util.ColorUtils;
import com.hytc.nhytc.util.DateUtil;
import com.hytc.nhytc.util.DensityUtils;
import com.hytc.nhytc.util.LessonDAO;
import com.hytc.nhytc.util.ScreenUtils;
import com.hytc.nhytc.view.ScrollViewExtend;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;


public class CourseTable extends Fragment {
	protected static final String TAG = "CourseTable";
	/** 第一个月份格子 */
	@Bind(R.id.tv_month)
	TextView mMonth;


	/**周一至周日格子*/
	@Bind({R.id.tv_monday_course,R.id.tv_tuesday_course,R.id.tv_wednesday_course,R.id.tv_thursday_course,R.id.tv_friday_course,R.id.tv_saturday_course,R.id.tv_sunday_course})
	List<TextView> mWeekViews;

	/**日期*/
	@Bind({R.id.ll_layout1,R.id.ll_layout2,R.id.ll_layout3,R.id.ll_layout4,R.id.ll_layout5,R.id.ll_layout6,R.id.ll_layout7})
	List<LinearLayout> mLayouts;

	@Bind({R.id.tv_day1,R.id.tv_day2,R.id.tv_day3,R.id.tv_day4,R.id.tv_day5,R.id.tv_day6,R.id.tv_day7})
	List<TextView> mTextViews;

	/** 课程表 */
	@Bind({R.id.ll_weekView})
	LinearLayout courseTableLayout;

	/**时间轴*/
	@Bind(R.id.ll_time)
	LinearLayout timeLayout; 

	/**课程轴*/
	@Bind(R.id.rl_courses)
	RelativeLayout courseLayout; 

	/**课程轴*/
	@Bind(R.id.rl_user_courses)
	RelativeLayout mUserCourseLayout; 

	@Bind(R.id.scroll_body)
	ScrollViewExtend scrollView;

	/** 课程格子平均宽度 高度 **/
	protected int aveWidth,gridHeight;
	//第一个格子宽度
	private int firstGridWidth;
	//一小时区域
	RelativeLayout.LayoutParams mHourParams;
	//时间文字
	RelativeLayout.LayoutParams mHourTextParams;
	//时间截止线
	RelativeLayout.LayoutParams mHourLineParams;

	//七种颜色的背景
	int[] background = {R.color.week_view_gray,R.color.week_view_red, R.color.week_view_bule, R.color.week_view_green,R.color.week_view_orange,R.color.week_view_sky,R.color.week_view_pubple,R.color.week_view_pubple1};

	//24小时
	public final static int HOUR = 24;

	private static final int WEEK = 7;
	private static final int TOTAL_COL = 7;
	private static final int TOTAL_ROW = 1;
	private Row rows[] = new Row[TOTAL_ROW];

	private CustomDate mShowDate;//自定义的日期  包括year month day
	private Context mContext;
	private View view;

	public List<Lesson> lessons = null;
	private View curClickView;

	private boolean isInit;

	public static final int[] HOURS = {1,2,3,4,5,6,7,8,9,10,11,12,13};

	private int month = 0,year = 0;

	private CourseDao dao;

	private int week = 6;


	public void setmContext(Context mContext) {
		this.mContext = mContext;
	}

	public void setShowDate(CustomDate mShowDate){
		this.mShowDate = mShowDate;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		week = getArguments().getInt("week");
		Log.e("coursetable", "week == " + week );
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_course_table, container, false);
		ButterKnife.bind(this, view);
		return view;
	}



	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {

		if(!isInit){
			isInit = true;
			//第一个格子宽度
			firstGridWidth = DensityUtils.dp2px(mContext, 30 + 2);
			//格子平均宽度
			aveWidth = (ScreenUtils.getScreenWidth(mContext)-firstGridWidth) / 7;
			//格子平均高度
			gridHeight =ScreenUtils.getScreenHeight(mContext) / 12;
		}

		//初始化24小时view
		initTwentyFourHourViews();
		//导入日期
		initDate();
		//添加手势 
		scrollView.setGestureDetector(new MyGestureListener(mContext).getGestureDetector());
	}


	/**
	 * 监听手势方向 切换显示不同的课程数据
	 *
	 */
	public class MyGestureListener extends GestureListener {
		public MyGestureListener(Context context) {
			super(context);
		}

		@Override
		public boolean left() {
			//rightSilde();
			return super.left();
		}

		@Override
		public boolean right() {
			//leftSilde();
			return super.right();
		}
	}

	/**
	 * 初始化日期数据
	 */
	public void initDate(){
		if(mShowDate == null){
			mShowDate = DateUtil.getNextSunday();
		}
		fillDate();
		//mMonth.setText(mShowDate.getMonth() < 10 ? "0" + mShowDate.getMonth() : mShowDate.getMonth() + "月");
		mMonth.setText(DateUtil.getMonth() + "月");
	}

	/**
	 * 更新周视图上的课程数据
	 */
	public void updateClassData(){
		//周视图上的数据是按每月查询一次 通过更改月份来达到更新数据的效果
		month = 0;
		fillDate();
	}


	/**
	 * 更新UI＋数据
	 */
	public void update(){
		fillDate();
		mMonth.setText(String.valueOf(mShowDate.getMonth()) + "月");
	}

	
	/**
	 * 导入日期数据
	 */
	public void fillDate(){
		fillWeekDate();

		if(lessons == null){
			lessons = new ArrayList<Lesson>();
		}

		//每月查询一次
		/*if(month!=mShowDate.month || year !=mShowDate.year){
			month = mShowDate.month;
			year = mShowDate.year;
			
			lessons = LessonDAO.getLessonsByYearAndMonth(mShowDate.year, mShowDate.month);
		}*/

		//更新数据前 移除view
		mUserCourseLayout.removeAllViews();
		for (int i = 0; i < TOTAL_ROW; i++) {
			if (rows[i] != null)
				rows[i].drawCells();
		}
	}


	public void initViewParams(){
		if(mHourParams == null){
			mHourParams = new RelativeLayout.LayoutParams(firstGridWidth,gridHeight);
		}

		if(mHourTextParams == null){
			mHourTextParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
			mHourTextParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
			mHourTextParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		}

		if(mHourLineParams == null){
			mHourLineParams = new RelativeLayout.LayoutParams(DensityUtils.dp2px(mContext, 10),DensityUtils.dp2px(mContext, (float)1.5));
			mHourLineParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			mHourLineParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		}
	}

	/**
	 * 初始化24小时*8view
	 */
	public void initTwentyFourHourViews(){
		initViewParams();
		for(int i=1;i<HOURS.length;i++){
			for(int j=1;j<=8;j++){
				if(j==1){
					addTimeView(i-1);//时间轴
				}else{
					addDotView(i,j-1);//小黑点

					//可以点击的TextView 用来添加课程
					TextView tx = new TextView(mContext);
					tx.setId((i - 1) * 7  + j);
					//相对布局参数
					RelativeLayout.LayoutParams rp = new RelativeLayout.LayoutParams(aveWidth,gridHeight);
					//设置他们的相对位置
					if(j == 2){
						if(i > 1){
							rp.addRule(RelativeLayout.BELOW, (i - 2) * 7+j);
						}
					}else{
						//字体样式
						tx.setTextAppearance(mContext, R.style.courseTableText);
						rp.addRule(RelativeLayout.RIGHT_OF, (i - 1) * 7  + j - 1);
						rp.addRule(RelativeLayout.ALIGN_TOP, (i - 1) * 7  + j - 1);
						tx.setText("");
					}

					tx.setLayoutParams(rp);
					tx.setOnClickListener(new OnClickListener() {
						@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
						@Override
						public void onClick(View v) {
							int hour = (v.getId()-2)/7;
							int weekofday=(v.getId() -1)%7;
							if(weekofday == 0){
								weekofday = 7;
							}

							if(hour < 0){
								hour = 0;
							}
							//莫忘了计算点击的时间时 加上开始时间
							hour += HOURS[0];
							if(curClickView!=null){
								curClickView.setBackground(null);
								if(curClickView.getId() == v.getId()){
									curClickView = null;
									//跳转到添加课程界面
									return;
								}
							}
							curClickView = v;
							curClickView.setBackground(getResources().getDrawable(R.mipmap.bg_course_add));
							curClickView.setAlpha((float)0.5);
						}
					});
					courseLayout.addView(tx);
				}
			}
		}
	}


	/**
	 * 周视图上面的小点
	 * @param hour
	 * @param j  
	 */
	public void addDotView(int hour,int j){
		if(j == 7){
			return;
		}
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(DensityUtils.dp2px(mContext, 3),DensityUtils.dp2px(mContext, 3));
		params.topMargin = hour*gridHeight;
		params.leftMargin = aveWidth*j;

		ImageView view = new ImageView(mContext);
		view.setLayoutParams(params);
		view.setBackgroundColor(getResources().getColor(R.color.week_view_text_date));

		courseLayout.addView(view);
	}


	/**
	 * 时间轴
	 * @param hour 几点
	 */
	public void addTimeView(int hour){
		RelativeLayout layout = new RelativeLayout(mContext);
		layout.setLayoutParams(mHourParams);

		RelativeLayout.LayoutParams layoutParams= new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
		layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
		layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);

		//第一个格子里显示2个时间 24点 1点
		/*if(hour == 0)
		{
			TextView textView = new TextView(mContext);
			textView.setText("0"+HOURS[0]+":00");
			textView.setLayoutParams(layoutParams);
			textView.setTextAppearance(mContext, R.style.weekViewTimeText);
			layout.addView(textView);
		}*/
		//第一个时间显示在格子顶部 其它时间显示在底部
		hour = HOURS[hour];

		TextView textView = new TextView(mContext);
		textView.setLayoutParams(mHourTextParams);
		textView.setTextAppearance(mContext, R.style.weekViewTimeText);
		
		/*StringBuilder builder = new StringBuilder();
		builder.append(hour < 10?("0"+hour):hour);
		builder.append(":00");*/
		
		//22点不显示
		//textView.setText(builder.toString());

		textView.setText(String.valueOf(hour));
		
		/*if(hour == 22){
			textView.setVisibility(View.INVISIBLE);
		}*/

		textView.setLayoutParams(layoutParams);
		layout.addView(textView);
		
		//22点横线不显示
		//if(hour != 22){
			TextView lineView = new TextView(mContext);
			lineView.setLayoutParams(mHourLineParams);
			lineView.setBackgroundColor(getResources().getColor(R.color.week_view_black));
			layout.addView(lineView);
		//}
		timeLayout.addView(layout);
	}

	/**
	 * 根据课程时间绘制课程显示的位置*/
	 //* @param name    课程名称
	 //* @param sHour   课程开始时间
	 //* @param sHour   课程开始分钟
	 //* @param min     这节课有多少分钟
	 //* @param weekday 周几

	public void addCourseView(final Lesson bean){

		/*int[] sParams = DateUtil.getTime(bean.getsTime());
		int[] eParams = DateUtil.getTime(bean.geteTime());
		if(sParams == null || eParams == null){
			return;
		}*/

		//开始时间 分钟  总时间
		int sHour,sMin,eHour,eMin,courses;//min

		/*sHour = sParams[0] - 1;
		sMin = sParams[1];

		eHour= eParams[0] - 1;
		eMin = eParams[1];*/

		//if(eHour > sHour || eMin > sMin){
		if(bean.getEnd_course() >= bean.getStart_course()){
			//min = (eHour - sHour)*60+(eMin - sMin);
			courses = bean.getEnd_course() - bean.getStart_course() + 1;
			//Log.e("maincourse", bean.getName() + " : " + bean.getEnd_course() + "-" + bean.getStart_course() + " + 1 = " + courses);
			/*if(min < 40){
				min = 40;
			}*/
			//课程高度
			//float minHeight = (float)gridHeight/(float)60;//获取1分钟的高度
			int cHeight = (int)((float)gridHeight * (float)courses) - 4;//获取要显示的课程的高度,-4是为了把课表的每隔阁子分开
			int weekday = bean.getWeek();
					//DateUtil.getWeekOfDate(bean.getLessonDate());
			if(weekday == 0){
				weekday = 7;
			}


			//1.添一个相对布局 来存储图片和文字
			RelativeLayout layout = new RelativeLayout(mContext);
			layout.setTag(String.valueOf(bean.getId()));
			layout.setGravity(RelativeLayout.CENTER_IN_PARENT);
			RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(aveWidth - 4,cHeight);//-4是为了把阁子分开
			//Calculate the location of the course Since the beginning of the course is from 7 points  
			//rlp.topMargin = (sHour-7)*gridHeight+(int)(minHeight*sMin);

			rlp.topMargin = (bean.getStart_course() - 1)*gridHeight + 4;
			rlp.leftMargin = firstGridWidth + (weekday - 1) * aveWidth + 4;
			layout.setLayoutParams(rlp);
			//设置背景框
			layout.setBackgroundResource(R.drawable.course_bg);

			//设置背景颜色
			GradientDrawable  drawable =(GradientDrawable) layout.getBackground();
			java.util.Random random = new java.util.Random();// 定义随机类
			int result=random.nextInt(10);// 返回[0,10)集合中的整数，注意不包括10
			drawable.setColor(ColorUtils.getFillColor(result + 1));
			//设置描边颜色
			//drawable.setStroke(DensityUtils.dp2px(mContext, 2),ColorUtils.getStrokeColor(2));

			//bean.status == ClientCode.YS?mContext.getResources().getColor(background[0]):mContext.getResources().getColor(background[DateUtil.getWeekDay()])
			
			//设置不透明度
			layout.getBackground().setAlpha(222);

			//2.添加课程
			TextView courseInfo = new TextView(mContext);

			StringBuilder builder = new StringBuilder();
			builder.append(bean.getName()).append("@").append(bean.getAdddress());
			courseInfo.setText(builder.toString());

			//偏移由这节课是星期几决定
			//rlp.addRule(RelativeLayout.RIGHT_OF, 1);
			//字体居中
			courseInfo.setGravity(Gravity.CENTER);
			RelativeLayout.LayoutParams layoutParams= new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.MATCH_PARENT);
			courseInfo.setTextSize(12);
			courseInfo.setLayoutParams(layoutParams);
			courseInfo.setTextColor(Color.WHITE);

			layout.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Bundle bundle = new Bundle();
					bundle.putSerializable("courseid",bean);
					Intent intent = new Intent(getActivity(), CourseDetailActivity.class);
					intent.putExtras(bundle);
					getActivity().startActivity(intent);
				}
			});

			/**
			 * 首先判断当前的周数是不是在学期周数范围内，不在即显示灰色背景
			 * 例如，如果当前是第1周，但是有一门课叫形式与政策，它是第9到12周的课，那么
			 * 此时就应该显示灰色背景
			 */
			if(bean.getBo_we()){
				boolean b = true;
				for(int[] i : bean.getWeeks()){
					if(week >= i[0] && week <=i[1]){
						b = false;
					}
				}
				if(b){
					drawable.setColor(ColorUtils.getFillColor(20));
					courseInfo.setTextColor(0xff999999);
				}
			}else {
				if (week < bean.getStart_week() || week > bean.getEnd_week()) {
					drawable.setColor(ColorUtils.getFillColor(20));
					courseInfo.setTextColor(0xff999999);
				}
				//根据单双周来设定背景颜色
				if (week % 2 == 1) {//如果本周是单周，但是课是双周，那么就把背景设置为灰色
					if (bean.getIsdsh() == 2) {
						drawable.setColor(ColorUtils.getFillColor(20));
						courseInfo.setTextColor(0xff999999);
					}
				} else {//如果本周是双周，但是课是单周，那么就把背景设置为灰色
					if (bean.getIsdsh() == 1) {
						drawable.setColor(ColorUtils.getFillColor(20));
						courseInfo.setTextColor(0xff999999);
					}
				}
			}


			layout.addView(courseInfo);

			//课程以上显示已经图标
			/*if(bean.getStatus() == 1){
				layout.addView(addClassOkIcon());
			}*/
			mUserCourseLayout.addView(layout);
		}
	}


	/**
	 * 添加确认上课图标
	 */
	public ImageView addClassOkIcon(){
		//图标显示在右下角
		RelativeLayout.LayoutParams params= new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		//设置图标
		ImageView view = new ImageView(mContext);
		view.setBackgroundResource(R.mipmap.btn_week_class_ok);
		view.setLayoutParams(params);
		return view;
	}

	/**
	 * 填充星期模式下的数据 默认通过当前日期得到所在星期天的日期，然后依次填充日期
	 */
	private void fillWeekDate() {
		int lastMonthDays = DateUtil.getMonthDays(mShowDate.year, mShowDate.month-1);
		rows[0] = new Row(0);
		int year = mShowDate.year;
		int month = mShowDate.month;
		int day = mShowDate.day;
		for (int i = TOTAL_COL -1; i >= 0 ; i--) {
			day -= 1;
			if (day < 1) {
				if (month == 1) {
					year--;
					month = 12;
				}else{
					month--;
				}
				day = lastMonthDays;
			}
			CustomDate date = CustomDate.modifiDayForObject(year,month, day);
			date.year = year;
			date.week = i;
			//今天
			if(DateUtil.isToday(date)){
				rows[0].cells[i] =  new Cell(date, State.CLICK_DAY, i, 0);
				continue;
			}
			rows[0].cells[i] = new Cell(date, State.CURRENT_MONTH_DAY,i, 0);
		}
	}

	/**
	 * 
	 * cell的state
	 *当前月日期，过去的月的日期，下个月的日期，今天，点击的日期
	 *
	 */
	enum State {
		CURRENT_MONTH_DAY, PAST_MONTH_DAY, NEXT_MONTH_DAY, TODAY, CLICK_DAY;
	}

	// 组
	class Row {
		public int j;

		Row(int j) {
			this.j = j;
		}

		public Cell[] cells = new Cell[TOTAL_COL];

		public void drawCells(){
			for (int i = 0; i < cells.length; i++) {
				if (cells[i] != null){
					TextView view =  mTextViews.get(i);
					view.setText(String.valueOf(cells[i].date.day));
					if(cells[i].state == State.CLICK_DAY){
						mTextViews.get(i).setTextColor(mContext.getResources().getColor(R.color.white));
						mLayouts.get(i).setBackgroundColor(mContext.getResources().getColor(R.color.week_view_date_highted));
					}else{
						mTextViews.get(i).setTextColor(mContext.getResources().getColor(R.color.textColor_black));
						mLayouts.get(i).setBackgroundColor(mContext.getResources().getColor(R.color.transparent));
					}

					//不是本月数据 从数据库查询
					//if(cells[i].date.year != mShowDate.year || cells[i].date.month != mShowDate.month){
					dao = new CourseDao(getActivity());
						//List<Lesson> list = LessonDAO.getLessonsByDate(mShowDate.toString(), mShowDate.nextDay());
					List<Lesson> list = dao.getCourseData();
					list = mergeCourse(list);
					list = doWithLessons(list);
					for(Lesson bean:list){
                        addCourseView(bean);
                    }
					/*}else{
						if(lessons!=null){
							//本月数据 从缓存中读取
							for(Lesson bean:lessons){
								if(bean.getLessonDate().substring(0, 10).equals(cells[i].date.toString())){
									addCourseView(bean);
								}
							}
						}
					}*/
				}
			}
		}
	}

	/**
	 * 把类似于
	 * 	 算法设计与分析
		 周五第3,4节{第1-15周|单周}
		 张巍
		 理工楼西阶101

		 算法设计与分析
		 周五第3,4节{第2-16周|双周}
		 张巍
		 理工楼北楼419

		 周几是一致的
		 开始上课节数是一致的
		 结束上课节数是一致的
		 然后把单双周不符合条件的去掉
	 * @param list
	 */
	public List<Lesson> doWithLessons(List<Lesson> list){
		//用来存放需要删除的文件的索引
		List<Integer> dedetes = new ArrayList<>();
		//用来存那些单双周课的索引
		List<int[]> dshweek = new ArrayList<>();
		/**
		 * 用来存那些一节完整的课被拆分成两次课的课程
		 * 例如：C++课程设计，在xxx教室，是周五的第1，2，3节课，但是
		 * 教务系统却把课程给分开了，这样说
		 * 1、 C++课程设计，在xxx教室，是周五的第 1，2 节课
		 * 2、 C++课程设计，在xxx教室，是周五的第 3 节课
		 * 所以这个集合就用来存这些课程的索引的
		 */
		List<int[]> courses = new ArrayList<>();

		/**
		 *   此集合是用来存那些完整的课因为周数被拆分开来的情况
		 *
		 *
		     消费者行为研究
			 周一第10,11,12节{第12-16周}
			 欧阳芬
			 教B阶301

			 消费者行为研究
			 周一第10,11,12节{第1-2周}
			 欧阳芬
			 教B阶301

			 消费者行为研究
			 周一第10,11,12节{第4-10周}
			 欧阳芬
			 教B阶301

		 * 比如说上面的数据，处理办法是三条bean数据只保留一条，然后再课程bean中重新设置一个私有成员变量
		 * 数据类型为List<int[]>,里面存放着 int[]{1,2}, int[]{4,10}, int[]{12,16},
		 * 同时设置start_course和end_course都为30，在存入数据的时候，如果发现
		 * start_course和end_course相等且其值为30，那么就作特殊处理
		 */

		ArrayList<int[]> weeks;
		Map<String, ArrayList<int[]>> weekss = new HashMap<>();

		for(int i = 0;i < list.size() - 1;i ++){
			for(int j = i + 1;j < list.size();j++){
				/**
				 * 当星期几相同，课程节数相同，并且是单双周的课时
				 */
				if(list.get(i).getWeek() == list.get(j).getWeek() &&
						list.get(i).getStart_course() == list.get(j).getStart_course() &&
						list.get(i).getEnd_course() == list.get(j).getEnd_course() &&
						(list.get(i).getIsdsh() == 1 || list.get(i).getIsdsh() == 2)){
					int[] data = new int[2];
					data[0] = i;
					data[1] = j;
					dshweek.add(data);
				}else if(list.get(i).getName().equals(list.get(j).getName()) &&
						 list.get(i).getAdddress().equals(list.get(j).getAdddress()) &&
						 list.get(i).getWeek() == list.get(j).getWeek()){
					/**
					 * 当课程名字相同，上课地点相同，星期几相同，上课的节数不同时
					 */
					if(list.get(i).getStart_course() != list.get(j).getStart_course()) {
						int[] data = new int[2];
						data[0] = i;
						data[1] = j;
						courses.add(data);
					}
				}
			}
		}

		if(dshweek.size() != 0){
			for(int[] i : dshweek){
				if(week % 2 == 1){//如果是单周;
					if(list.get(i[0]).getIsdsh() == 1){
						dedetes.add(i[1]);
					}else {
						dedetes.add(i[0]);
					}
				}else {//如果是双周
					if(list.get(i[0]).getIsdsh() == 1){
						dedetes.add(i[0]);
					}else {
						dedetes.add(i[1]);
					}
				}
			}
		}

		if(courses.size() != 0){
			for(int[] i : courses){
				if(list.get(i[0]).getEnd_course() + 1 == list.get(i[1]).getStart_course()){
					list.get(i[0]).setEnd_course(list.get(i[1]).getEnd_course());
					dedetes.add(i[1]);
				}else if(list.get(i[1]).getEnd_course() + 1 == list.get(i[0]).getStart_course()){
					list.get(i[1]).setEnd_course(list.get(i[0]).getEnd_course());
					dedetes.add(i[0]);
				}
			}
		}

		Collections.sort(dedetes);
		Collections.reverse(dedetes);
		for(Integer i : dedetes){
			list.remove((int)i);
		}
		return list;
	}

	/**
	 * true:不存在
	 * false：存在
	 * @param weeks
	 * @param i
	 * @return
	 */
	public boolean isExistWeek(List<int[]> weeks,int i){
		boolean boo = true;
		for(int[] week : weeks){
			if(week[0] == i){
				boo = false;
			}
		}
		return boo;
	}

	public List<Lesson> mergeCourse(List<Lesson> list){
		/**
		 *   此集合是用来存那些完整的课因为周数被拆分开来的情况
		 *
		 *
		 消费者行为研究
		 周一第10,11,12节{第12-16周}
		 欧阳芬
		 教B阶301

		 消费者行为研究
		 周一第10,11,12节{第1-2周}
		 欧阳芬
		 教B阶301

		 消费者行为研究
		 周一第10,11,12节{第4-10周}
		 欧阳芬
		 教B阶301

		 * 比如说上面的数据，处理办法是三条bean数据只保留一条，然后再课程bean中重新设置一个私有成员变量
		 * 数据类型为List<int[]>,里面存放着 int[]{1,2}, int[]{4,10}, int[]{12,16},
		 * 同时设置start_course和end_course都为30，在存入数据的时候，如果发现
		 * start_course和end_course相等且其值为30，那么就作特殊处理
		 */

		ArrayList<int[]> weeks;
		Map<String, ArrayList<int[]>> weekss = new HashMap<>();
		for(int i = 0;i < list.size() - 1;i ++) {
			for (int j = i + 1; j < list.size(); j++) {

				if(list.get(i).getName().equals(list.get(j).getName()) &&
				   list.get(i).getWeek() == list.get(j).getWeek() &&
				   list.get(i).getStart_week() != list.get(j).getStart_week() &&
				   list.get(i).getStart_course() == list.get(j).getStart_course()){
					if(weekss.containsKey(list.get(i).getName() + list.get(i).getStart_course())){
						weeks = weekss.get(list.get(i).getName() + list.get(i).getStart_course());
						if(isExistWeek(weeks,list.get(i).getStart_week())){
							int[] data = new int[3];
							data[0] = list.get(i).getStart_week();
							data[1] = list.get(i).getEnd_week();
							data[2] = i;
							weeks.add(data);
						}
						weekss.put(list.get(i).getName() + list.get(i).getStart_course(),weeks);
					}else {
						weeks = new ArrayList<>();
						int[] data = new int[3];
						data[0] = list.get(i).getStart_week();
						data[1] = list.get(i).getEnd_week();
						data[2] = i;
						weeks.add(data);
						weekss.put(list.get(i).getName() + list.get(i).getStart_course(),weeks);
					}
					if(weekss.containsKey(list.get(j).getName() + list.get(j).getStart_course())){
						weeks = weekss.get(list.get(j).getName() + list.get(j).getStart_course());
						if(isExistWeek(weeks,list.get(j).getStart_week())){
							int[] data = new int[3];
							data[0] = list.get(j).getStart_week();
							data[1] = list.get(j).getEnd_week();
							data[2] = j;
							weeks.add(data);
						}
						weekss.put(list.get(j).getName() + list.get(j).getStart_course(),weeks);
					}else {
						weeks = new ArrayList<>();
						int[] data = new int[3];
						data[0] = list.get(j).getStart_week();
						data[1] = list.get(j).getEnd_week();
						data[2] = j;
						weeks.add(data);
						weekss.put(list.get(j).getName() + list.get(j).getStart_course(),weeks);
					}
				}

			}
		}
		//用来存放需要删除的文件的索引
		List<Integer> dedetes = new ArrayList<>();
		ArrayList<int[]> temps;
		boolean boo;
		if(weekss.keySet().size() != 0){
			for(String key : weekss.keySet()){
				boo = true;
				temps = weekss.get(key);
				for(int[] ii : temps){
					if(boo){
						list.get(ii[2]).setWeeks(temps);
						list.get(ii[2]).setBo_we(true);
						boo = false;
					}else {
						dedetes.add(ii[2]);
					}
				}
			}
		}

		Collections.sort(dedetes);
		Collections.reverse(dedetes);
		for(Integer i : dedetes){
			list.remove((int)i);
		}
		return list;
	}

	// 单元格
	class Cell {
		public CustomDate date;
		public State state;
		public int i;
		public int j;

		public Cell(CustomDate date, State state, int i, int j) {
			super();
			this.date = date;
			this.state = state;
			this.i = i;
			this.j = j;
		}
	}

	/**
	 * 向右滑动 
	 */
	public void rightSilde() {
		int currentMonthDays = DateUtil.getMonthDays(mShowDate.year, mShowDate.month);
		startAnimation();
		if (mShowDate.day + WEEK > currentMonthDays) {
			if (mShowDate.month == 12) {
				mShowDate.month = 1;
				mShowDate.year += 1;
			} else {
				mShowDate.month += 1;
			}
			mShowDate.day = WEEK - currentMonthDays + mShowDate.day;	
		}else{
			mShowDate.day += WEEK;
		}
		//更新课程数据
		update();
	}

	/**
	 * 向左滑动
	 */
	public void leftSilde() {
		int lastMonthDays = DateUtil.getMonthDays(mShowDate.year, mShowDate.month);
		startAnimation();
		if (mShowDate.day - WEEK < 1) {
			if (mShowDate.month == 1) {
				mShowDate.month = 12;
				mShowDate.year -= 1;
			} else {
				mShowDate.month -= 1;
			}
			mShowDate.day = lastMonthDays - WEEK + mShowDate.day;
		}else{
			mShowDate.day -= WEEK;
		}
		update();
	}

	/**
	 * 滑动页面动画
	 */
	private void startAnimation() {
		AlphaAnimation alphaAnim=new AlphaAnimation(1.0f, 0.4f);
		alphaAnim.setDuration(400);
		alphaAnim.setInterpolator(new LinearInterpolator());
		alphaAnim.setFillAfter(false);
		view.startAnimation(alphaAnim);

		AlphaAnimation alphaAnim1=new AlphaAnimation(0.4f, 1.0f);
		alphaAnim1.setDuration(400);
		alphaAnim1.setInterpolator(new LinearInterpolator());
		alphaAnim1.setFillAfter(false);
		view.startAnimation(alphaAnim);

	}

	/**
	 * 返回今天
	 */
	public void backToday(){
		mShowDate = null;
		initDate();
	}

	public void removeAllViews(){
		courseLayout.removeAllViews();
		mUserCourseLayout.removeAllViews();
		timeLayout.removeAllViews();
	}
}
