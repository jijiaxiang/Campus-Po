package com.campuspo.activity;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;

import com.campuspo.R;
import com.campuspo.fragment.HomePageFragment;
import com.campuspo.fragment.PersonalPageFragment;
import com.campuspo.fragment.SpecialPageFragment;
import com.campuspo.util.ImageLoader;
import com.campuspo.util.Logger;

public class MainActivity extends ActionBarActivity {

	public static final String TAG = "MainActivity";

	public static final String KEY_PREF_CLEAR_CACHE = "pref_clear_cache";
	
	private ViewPager mViewPager;
	private TabsAdapter mTabsAdapter;
	private String[] mTabTitleArray;
	

	public static final String TAB_POS = "tab_pos";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		
		mViewPager = new ViewPager(this);
		mViewPager.setId(R.id.pager);
		//requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(mViewPager);
		//setProgressBarIndeterminateVisibility(true);
		
		mTabTitleArray = getResources().getStringArray(R.array.tab_title);
		mTabsAdapter = new TabsAdapter(this, mViewPager);

		ActionBar bar = getSupportActionBar();
		bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		// bar.setDisplayOptions(0, ActionBar.DISPLAY_SHOW_TITLE);

		//Add tabs to Actionbar
		mTabsAdapter.addTab(bar.newTab().setText(mTabTitleArray[0]), 
				HomePageFragment.class, null);
		mTabsAdapter.addTab(bar.newTab().setText(mTabTitleArray[1]),
				SpecialPageFragment.class, null);
		mTabsAdapter.addTab(bar.newTab().setText(mTabTitleArray[2]),
				PersonalPageFragment.class, null);

		//retain the state from "savedInstanceState"
		if (savedInstanceState != null)
			bar.setSelectedNavigationItem(savedInstanceState.getInt(TAB_POS, 0));
		
		//bar.setSelectedNavigationItem(arg0)

		// prepareData();
		// initUIComponents();
	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);
		savedInstanceState.putInt(TAB_POS, getSupportActionBar()
				.getSelectedNavigationIndex());
	}
	
	@Override
	public boolean onCreateOptionsMenu (Menu menu) {
		return super.onCreateOptionsMenu(menu);
	}
	
	
	public static class TabsAdapter extends FragmentPagerAdapter implements
			ActionBar.TabListener, ViewPager.OnPageChangeListener {

		private Context mContext;
		private ActionBar mActionBar;
		private ViewPager mViewPager;
		private ArrayList<TabInfo> mTabInfoList = new ArrayList<TabInfo>();

		static final class TabInfo {
			private final Class<?> mClass;
			private final Bundle mArgs;

			TabInfo(Class<?> cls, Bundle args) {
				mClass = cls;
				mArgs = args;
			}
		}

		public TabsAdapter(ActionBarActivity activity, ViewPager pager) {
			super(activity.getSupportFragmentManager());

			mContext = activity;
			mActionBar = activity.getSupportActionBar();
			mViewPager = pager;
			mViewPager.setAdapter(this);
			mViewPager.setOnPageChangeListener(this);
		}

		public void addTab(ActionBar.Tab tab, Class<?> cls, Bundle args) {
			TabInfo info = new TabInfo(cls, args);
			tab.setTag(info);
			tab.setTabListener(this);
			mTabInfoList.add(info);
			mActionBar.addTab(tab);
			notifyDataSetChanged();
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		@Override
		public void onPageSelected(int index) {
			mActionBar.setSelectedNavigationItem(index);
		}

		@Override
		public void onTabReselected(Tab arg0, FragmentTransaction arg1) {

		}

		@Override
		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			Object info = tab.getTag();
			int size = mTabInfoList.size();
			for (int i = 0; i < size; i++) {
				if (info == mTabInfoList.get(i))
					mViewPager.setCurrentItem(i);
			}

		}

		@Override
		public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {

		}

		@Override
		public Fragment getItem(int pos) {

			Logger.debug(TAG, "getItem()...");
			TabInfo info = mTabInfoList.get(pos);

			return Fragment.instantiate(mContext, info.mClass.getName(),
					info.mArgs);
		}

		@Override
		public int getCount() {
			return mTabInfoList.size();
		}

	}

	@Override
	public void onBackPressed() {
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		
		builder.setMessage("Exit?")	
			
			.setPositiveButton(android.R.string.ok,
					new DialogInterface.OnClickListener(){

				@Override
				public void onClick(DialogInterface dialog, int which) {
					SharedPreferences pref = 
							PreferenceManager.getDefaultSharedPreferences(
									MainActivity.this);
					
					boolean clearCache = pref.getBoolean(KEY_PREF_CLEAR_CACHE, false);
					Logger.debug(TAG, String.valueOf(clearCache));
					if(clearCache){
						ImageLoader.getInstance(getApplicationContext()).clearDiskCache();
					}
					MainActivity.this.finish();
				}		
			})
			.setNegativeButton(android.R.string.cancel, null);
		builder.create().show();
	}
	

}
