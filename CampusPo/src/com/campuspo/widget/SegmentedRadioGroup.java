

package com.campuspo.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RadioGroup;

public class SegmentedRadioGroup extends RadioGroup {

	public SegmentedRadioGroup(Context context) {
		super(context);
	}

	public SegmentedRadioGroup(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		changeButtonsImages();
	}

	private void changeButtonsImages(){
		int count = super.getChildCount();

		if(count > 1){
			super.getChildAt(0).setBackgroundResource(com.campuspo.R.drawable.segment_left);
			for(int i=1; i < count-1; i++){
				super.getChildAt(i).setBackgroundResource(com.campuspo.R.drawable.segment_middle);
			}
			super.getChildAt(count-1).setBackgroundResource(com.campuspo.R.drawable.segment_right);
		}else if (count == 1){
			super.getChildAt(0).setBackgroundResource(com.campuspo.R.drawable.segment_button);
		}
	}
}