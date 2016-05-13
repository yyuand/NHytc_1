package com.hytc.nhytc.activity;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hytc.nhytc.R;
import com.hytc.nhytc.dbDAO.CourseDao;
import com.hytc.nhytc.domain.ForResulltMsg;
import com.hytc.nhytc.domain.User;
import com.hytc.nhytc.fragment.Activityfragment;
import com.hytc.nhytc.fragment.Homefragment;
import com.hytc.nhytc.fragment.Infofragment;
import com.hytc.nhytc.fragment.ShuoShuoFragment;
import com.hytc.nhytc.fragment.Sunscribefragment;
import com.hytc.nhytc.tool.BitmapHelper;
import com.hytc.nhytc.tool.MyRYTokenManager;
import com.hytc.nhytc.view.CircleImageView;
import com.hytc.nhytc.view.DragLayout;
import com.hytc.nhytc.view.activity_transition.ActivityTransitionLauncher;
import com.lidroid.xutils.BitmapUtils;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.update.BmobUpdateAgent;
import de.greenrobot.event.EventBus;

public class MainActivity extends FragmentActivity implements View.OnClickListener{

	private FrameLayout frameLayout;
	private Fragment fragment;

	private long exitTime = 0;

	/**
	 *
	 */
	private RelativeLayout rl_shouye;
	private ImageView iv_shouye;
	private TextView tv_shouye;

	/**
	 *
	 */
	private RelativeLayout rl_info;
	private ImageView iv_info;
	private TextView tv_info;

	/**
	 *
	 */
	private RelativeLayout rl_sunscribe;
	private ImageView iv_sunscribe;
	private TextView tv_sunscribe;

	/**
	 *
	 */
	private RelativeLayout rl_activity;
	private ImageView iv_activity;
	private TextView tv_activity;

	/**
	 *
	 */
	private RelativeLayout rl_more;
	private ImageView iv_more;

	private Fragment shuofragment;
	private Fragment homefragment;
	private Fragment infofragment;
	private Fragment sunscribefragment;
	private Fragment activityfragment;

	/**
	 * 最底下的现对布局
	 */
	private LinearLayout llbutton_main;


	public static DragLayout dl;

	private CircleImageView userHeader;
	private TextView saysomething;
	private BitmapUtils bitmapUtils;
	private TextView username;
	private ListView lv;

	private TextView SGL;

	/**
	 * 发布按钮弹出菜单
	 */
	private PopupWindow mpopupWindow;
	private View view;
	static GridLayout gl;
	private ImageView iv_pop_more;

	private User user;

	private CourseDao courseDao;

	/**
	 * 发布
	 */
	private ImageButton imgbtGoPublish;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initDragLayout();
		initView();
		initcontrol();
		initbutton();
		/**
		 * 调用自动更新接口
		 */
		BmobUpdateAgent.update(this);
		/**
		 * 考虑到用户流量的限制，目前我们默认在WiFi接入情况下才进行自动提醒。如需要在任意网络环境下都进行更新自动提醒，则请在update调用之前添加以下代码：
		 */
		BmobUpdateAgent.setUpdateOnlyWifi(false);
	}

	private void initDragLayout() {
		dl = (DragLayout) findViewById(R.id.dl);
		dl.setDragListener(new DragLayout.DragListener() {
			@Override
			public void onOpen() {
			}

			@Override
			public void onClose() {
			}

			@Override
			public void onDrag(float percent) {

			}
		});
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		getuser(user.getObjectId());
	}

	private void initView() {
		userHeader = (CircleImageView) this.findViewById(R.id.iv_userheader);
		saysomething = (TextView) this.findViewById(R.id.tv_say_something_main_activity);
		SGL = (TextView) this.findViewById(R.id.tv_mail);
		bitmapUtils = BitmapHelper.getBitmapUtils(this);

		user = BmobUser.getCurrentUser(this, User.class);

		courseDao = new CourseDao(this);

		bitmapUtils.display(userHeader, user.getHeadSculpture());
		saysomething.setText(user.getAutograhp());

		try {
			user.getGender();
			user.getFaculty().toString();
		}catch (Exception e){
			getuser(user.getObjectId());
		}

		if("".equals(user.getHeadSculpture())){
			getuser(user.getObjectId());
		}

		saysomething = (TextView) this.findViewById(R.id.tv_say_something_main_activity);

		userHeader.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				ArrayList<String> picurls = new ArrayList<>();
				picurls.add(user.getHeadSculpture());
				intent.putExtra("imgurls", picurls);
				intent.putExtra("pagenum", 1);
				intent.setClass(MainActivity.this, ImageViewActivity.class);
				ActivityTransitionLauncher.with(MainActivity.this).from(v).launch(intent);
			}
		});

		username = (TextView) findViewById(R.id.tv_username_main_activity);
		username.setText(BmobUser.getCurrentUser(this, User.class).getUsername());
		lv = (ListView) findViewById(R.id.lv);
		lv.setAdapter(new ArrayAdapter<String>(MainActivity.this,
				R.layout.item_text, new String[]{"我的说说", "我的表白",
				"我的商品", "我发布的失物", "个人资料","与我相关",
				"修改密码", "检查更新", "注销退出"}));
		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				switch (position) {
					case 0:
						Intent intent = new Intent();
						intent.setClass(MainActivity.this, ShuoshuoMyActivity.class);
						MainActivity.this.startActivity(intent);
						dl.close();
						break;
					case 1:
						Intent intent1 = new Intent();
						intent1.setClass(MainActivity.this, ShowLoveMyActivity.class);
						MainActivity.this.startActivity(intent1);
						dl.close();
						break;
					case 2:
						Intent intent2 = new Intent();
						intent2.setClass(MainActivity.this, MerchandiseMyActivity.class);
						MainActivity.this.startActivity(intent2);
						dl.close();
						break;
					case 3:
						Intent intent3 = new Intent();
						intent3.setClass(MainActivity.this, LostMyActivity.class);
						MainActivity.this.startActivity(intent3);
						dl.close();
						break;
					case 4:
						Intent intent4 = new Intent();
						intent4.setClass(MainActivity.this, PersonalDataActivity.class);
						MainActivity.this.startActivity(intent4);
						dl.close();
						break;
					case 5:
						Intent intent5 = new Intent();
						intent5.setClass(MainActivity.this, MyInfoActivity.class);
						MainActivity.this.startActivity(intent5);
						dl.close();
						break;
					case 6:
						Intent intent6 = new Intent();
						intent6.setClass(MainActivity.this, SetPasswordActivity.class);
						MainActivity.this.startActivity(intent6);
						dl.close();
						break;
					case 7:
						BmobUpdateAgent.forceUpdate(MainActivity.this);
						dl.close();
						break;
					case 8:
						AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
						builder.setMessage("亲，您确定要退出账号，回到登录界面么？");
						builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								BmobUser.logOut(MainActivity.this);//清除本地bmob缓存
								MyRYTokenManager.saveToken(MainActivity.this, "");//清楚本地融云token缓存
								courseDao.deleteAllData();//清楚本地课表缓存
								courseDao.deleteSpData();//清楚本地课表账户缓存
								Intent intentgotologin = new Intent();
								intentgotologin.setClass(MainActivity.this, LoginActivity.class);
								startActivity(intentgotologin);
								MainActivity.this.finish();
							}//211401005     950605
						});
						builder.setNegativeButton("取消", null);
						builder.create();
						builder.show();
						dl.close();
						break;
				}
				//Util.t(getApplicationContext(), "click " + position);
			}
		});


		SGL.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this,IntroduceActivity.class);
				MainActivity.this.startActivity(intent);
				dl.close();
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	private void initcontrol() {
		view = View.inflate(getApplicationContext(), R.layout.popwindow, null);

		frameLayout = (FrameLayout) this.findViewById(R.id.fl_main);

		rl_shouye = (RelativeLayout) this.findViewById(R.id.rl_shouye_main);
		iv_shouye = (ImageView) this.findViewById(R.id.iv_shouye_main);
		tv_shouye = (TextView) this.findViewById(R.id.tv_shouye_main);

		rl_info = (RelativeLayout) this.findViewById(R.id.rl_infomation_main);
		iv_info = (ImageView) this.findViewById(R.id.iv_infomation_main);
		tv_info = (TextView) this.findViewById(R.id.tv_infomation_main);

		rl_sunscribe = (RelativeLayout) this.findViewById(R.id.rl_sunscribe_main);
		iv_sunscribe = (ImageView) this.findViewById(R.id.iv_sunscribe_main);
		tv_sunscribe = (TextView) this.findViewById(R.id.tv_sunscribe_main);

		rl_activity = (RelativeLayout) this.findViewById(R.id.rl_activity_main);
		iv_activity = (ImageView) this.findViewById(R.id.iv_activity_main);
		tv_activity = (TextView) this.findViewById(R.id.tv_activity_main);

		rl_more = (RelativeLayout) this.findViewById(R.id.rl_more_main);
		iv_more = (ImageView) this.findViewById(R.id.iv_more_main);

		llbutton_main = (LinearLayout) this.findViewById(R.id.llbutton_main);


		shuofragment = new ShuoShuoFragment();
		homefragment = new Homefragment();
		infofragment = new Infofragment();
		sunscribefragment = new Sunscribefragment();
		activityfragment = new Activityfragment();

		fragment = shuofragment;
		setFregment(fragment);
	}

	public void setFregment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fl_main,fragment).commit();
    }

	private void initbutton() {
		rl_shouye.setOnClickListener(this);
		rl_info.setOnClickListener(this);
		rl_sunscribe.setOnClickListener(this);
		rl_activity.setOnClickListener(this);
		rl_more.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.rl_shouye_main:
				setButtonNo();
				iv_shouye.setImageResource(R.mipmap.messagey);
				tv_shouye.setTextColor(getResources().getColor(R.color.title));
				fragment = shuofragment;
				setFregment(fragment);
				break;
			case R.id.rl_infomation_main:
				setButtonNo();
				iv_info.setImageResource(R.mipmap.myy);
				tv_info.setTextColor(getResources().getColor(R.color.title));
				fragment = infofragment;
				setFregment(fragment);
				break;
			case R.id.rl_sunscribe_main:
				setButtonNo();
				iv_sunscribe.setImageResource(R.mipmap.ready);
				tv_sunscribe.setTextColor(getResources().getColor(R.color.title));
				fragment = sunscribefragment;
				setFregment(fragment);
				break;
			case R.id.rl_activity_main:
				setButtonNo();
				iv_activity.setImageResource(R.mipmap.activitysy);
				tv_activity.setTextColor(getResources().getColor(R.color.title));
				fragment = activityfragment;
				setFregment(fragment);
				break;
			case R.id.rl_more_main:
				rotateToRight(iv_more);
				publish();
				break;
		}
	}


	public void setButtonNo(){
		iv_shouye.setImageResource(R.mipmap.messagen);
		tv_shouye.setTextColor(getResources().getColor(R.color.main_no));

		iv_info.setImageResource(R.mipmap.myn);
		tv_info.setTextColor(getResources().getColor(R.color.main_no));

		iv_sunscribe.setImageResource(R.mipmap.readn);
		tv_sunscribe.setTextColor(getResources().getColor(R.color.main_no));

		iv_activity.setImageResource(R.mipmap.activitysn);
		tv_activity.setTextColor(getResources().getColor(R.color.main_no));

	}


	/**
	 *
	 * @param keyCode
	 * @param event
	 * @return
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			if ((System.currentTimeMillis() - exitTime) > 2000) {
				Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis();
			} else {
				finish();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}


	private void publish() {
		if (mpopupWindow == null) {
			view.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					mpopupWindow.dismiss();
				}
			});
			gl = (GridLayout) view.findViewById(R.id.gl);
			iv_pop_more = (ImageView) view.findViewById(R.id.iv_pop_more);
			mpopupWindow = new PopupWindow(this);
			mpopupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
			mpopupWindow.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
			mpopupWindow.setBackgroundDrawable(new BitmapDrawable());
			mpopupWindow.setFocusable(true);
			mpopupWindow.setOutsideTouchable(true);
			mpopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
				@Override
				public void onDismiss() {
					rotateToLeft(iv_more);
					rotateToLeft(iv_pop_more);
				}
			});
			RelativeLayout rlPublishMerchandise = (RelativeLayout) view.findViewById(R.id.rlGoPublishMerchandise);
			rlPublishMerchandise.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent();
					intent.setClass(MainActivity.this, ShuoshuoPubishActivity.class);
					startActivity(intent);
					rotateToLeft(iv_pop_more);
					mpopupWindow.dismiss();
				}
			});
			RelativeLayout rlGoPublishNeed = (RelativeLayout) view.findViewById(R.id.rlGoPublishNeed);
			rlGoPublishNeed.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent();
					intent.setClass(MainActivity.this, LostPublishActivity.class);
					startActivity(intent);
					rotateToLeft(iv_pop_more);
					mpopupWindow.dismiss();
				}
			});
			RelativeLayout rlPublishCourierBring = (RelativeLayout) view.findViewById(R.id.rlGoPublishcourierbring);
			rlPublishCourierBring.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent();
					intent.setClass(MainActivity.this, MerchandisePublishActivity.class);
					startActivity(intent);
					rotateToLeft(iv_pop_more);
					mpopupWindow.dismiss();
				}
			});
			RelativeLayout rlPublishCourierTake = (RelativeLayout) view.findViewById(R.id.rlGoPublishcouriertake);
			rlPublishCourierTake.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent();
					intent.setClass(MainActivity.this, ShowLovePublishActivity.class);
					startActivity(intent);
					rotateToLeft(iv_pop_more);
					mpopupWindow.dismiss();
				}
			});
			RelativeLayout rlPublishjob = (RelativeLayout) view.findViewById(R.id.rlGoPublishjobe);
			rlPublishjob.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent();
					intent.setClass(MainActivity.this, ActNewPartjobPublish.class);
					startActivity(intent);
					rotateToLeft(iv_pop_more);
					mpopupWindow.dismiss();
				}
			});
			RelativeLayout rlPublishactivities = (RelativeLayout) view.findViewById(R.id.rlGoPublishactivitys);
			rlPublishactivities.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent();
					intent.setClass(MainActivity.this, ShowLovePublishActivity.class);
					startActivity(intent);
					rotateToLeft(iv_pop_more);
					mpopupWindow.dismiss();
				}
			});

		}
		gl.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.push_bottom_in));
		rotateToRight(iv_pop_more);
		mpopupWindow.setContentView(view);
		mpopupWindow.showAtLocation(rl_more, Gravity.BOTTOM, 0, 0);
		mpopupWindow.update();
	}


	public void rotateToRight(ImageView imageView){
		final RotateAnimation animation =new RotateAnimation(0,135,Animation.RELATIVE_TO_SELF, 0.5f,Animation.RELATIVE_TO_SELF,0.5f);
		//RotateAnimation rotateAnimation = new RotateAnimation(0,135,imageView.getPivotX(),imageView.getPivotY());
		animation.setDuration(230);
		animation.setFillAfter(true);
		imageView.startAnimation(animation);
	}

	public void rotateToLeft(ImageView imageView){
		final RotateAnimation animation =new RotateAnimation(135,0,Animation.RELATIVE_TO_SELF, 0.5f,Animation.RELATIVE_TO_SELF,0.5f);
		//RotateAnimation rotateAnimation = new RotateAnimation(135,0,imageView.getPivotX(),imageView.getPivotY());
		animation.setDuration(230);
		animation.setFillAfter(true);
		imageView.startAnimation(animation);
	}


	public void getuser(String userid) {
		final BmobQuery<User> query = new BmobQuery<>();
		query.addWhereEqualTo("objectId", userid);
		query.findObjects(this, new FindListener<User>() {
			@Override
			public void onSuccess(List<User> list) {
				if (list.size() != 0) {
					user = list.get(0);
					bitmapUtils.display(userHeader, list.get(0).getHeadSculpture());
					saysomething.setText(list.get(0).getAutograhp());
				}
			}

			@Override
			public void onError(int i, String s) {
				Toast.makeText(MainActivity.this, "信息获取失败\n请检查网络连接", Toast.LENGTH_SHORT).show();
			}
		});
	}

	/**
	 * 当是第一个fragment的时候，也就是说说列表的界面，当点击说说详情的时候，关闭说说详情，会返回部分点赞和评论状态值的
	 * 信息，就返回到这里
	 * @param requestCode
	 * @param resultCode
	 * @param data
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(data != null){
			EventBus.getDefault().post(new ForResulltMsg(data.getBooleanExtra("appstatus", false), data.getIntExtra("position", 0), data.getStringExtra("comcounts"), data.getStringExtra("appcounts")));
		}


	}
}
