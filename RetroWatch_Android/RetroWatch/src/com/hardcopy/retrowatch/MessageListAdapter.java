/*
 * Copyright (C) 2014 The Retro Watch - Open source smart watch project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hardcopy.retrowatch;

import java.util.ArrayList;

import com.hardcopy.retrowatch.contents.objects.ContentObject;
import com.hardcopy.retrowatch.utils.Utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MessageListAdapter extends ArrayAdapter<ContentObject> {

	public static final String TAG = "MessageListAdapter";
	
	private Context mContext = null;
	private ArrayList<ContentObject> mMessageList = null;
	
	
	public MessageListAdapter(Context c, int resId, ArrayList<ContentObject> itemList) {
		super(c, resId, itemList);
		mContext = c;
		if(itemList == null)
			mMessageList = new ArrayList<ContentObject>();
		else
			mMessageList = itemList;
	}
	
	
	public void addMessage(ContentObject co) {
		mMessageList.add(co);
	}
	
	public void addMessageAll(ArrayList<ContentObject> itemList) {
		if(itemList == null)
			return;
		for(int i=0; i<itemList.size(); i++)
			addMessage(itemList.get(i));
	}
	
	public void deleteMessage(int id) {
		for(int i = mMessageList.size() - 1; -1 < i; i--) {
			ContentObject co = mMessageList.get(i);
			if(co.mId == id) {
				mMessageList.remove(i);
			}
		}
	}
	
	public void deleteMessageByType(int type) {
		for(int i = mMessageList.size() - 1; -1 < i; i--) {
			ContentObject co = mMessageList.get(i);
			if(co.mContentType == type) {
				mMessageList.remove(i);
			}
		}
	}
	
	public void deleteMessageByTypeAndName(int type, String packageName) {
		for(int i = mMessageList.size() - 1; -1 < i; i--) {
			ContentObject co = mMessageList.get(i);
			if(co.mContentType == type) {
				if(co.mPackageName != null && co.mPackageName.contains(packageName))
					mMessageList.remove(i);
			}
		}
	}
	
	public void deleteMessageAll() {
		mMessageList.clear();
	}
	
	@Override
	public int getCount() {
		return mMessageList.size();
	}
	@Override
	public ContentObject getItem(int position) { 
		return mMessageList.get(position); 
	}
	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		View v = convertView;
		ContentObject co = getItem(position);
		
		if(v == null) {
			LayoutInflater li = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = li.inflate(R.layout.list_message_list, null);
			holder = new ViewHolder();
			
			holder.mItemContainer = (LinearLayout) v.findViewById(R.id.msg_item_container);
			holder.mTextInfo = (TextView) v.findViewById(R.id.msg_info);
			holder.mTextOrigin = (TextView) v.findViewById(R.id.msg_origin);
			holder.mTextConverted = (TextView) v.findViewById(R.id.msg_converted);
			
			v.setTag(holder);
		} else {
			holder = (ViewHolder) v.getTag();
		}
		
		if (co != null && holder != null) {
			if(co.mIsEnabled)
				holder.mItemContainer.setBackgroundColor(mContext.getResources().getColor(R.color.lightblue1));
			else
				holder.mItemContainer.setBackgroundColor(mContext.getResources().getColor(R.color.graye));
			
			holder.mTextInfo.setText(Utils.getMessageTypeString(co.mContentType) + " : " + co.mPackageName);
			if(co.mOriginalString != null) {
				holder.mTextOrigin.setText(co.mOriginalString);
			} else {
				holder.mTextOrigin.setText("");
			}
			if(co.mFilteredString != null) {
				holder.mTextConverted.setText("--> " + co.mFilteredString);
			} else {
				holder.mTextConverted.setText("");
			}
		}
		
		return v;
	}	// End of getView()
	
	
	private OnTouchListener mListItemTouchListener = new OnTouchListener() {
		private float startx = 0;
		private float starty = 0;
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if(event.getAction()==MotionEvent.ACTION_DOWN){
				startx = event.getX();
				starty = event.getY();
			}
			if(event.getAction()==MotionEvent.ACTION_UP){
				// if action-up occurred within 30px from start, process as click event. 
				if( (startx - event.getX())*(startx - event.getX()) + (starty - event.getY())*(starty - event.getY()) < 900 ) {
					processOnClickEvent(v);
				}
			}
			return true;
		}
	};	// End of new OnTouchListener
	
	private void processOnClickEvent(View v) {
		switch(v.getId())
		{
//			case R.id.layout_item_row_container:
//				break;
		}	// End of switch()
	}
	
	public class ViewHolder {
		public LinearLayout mItemContainer = null;
		public TextView mTextInfo = null;
		public TextView mTextOrigin = null;
		public TextView mTextConverted = null;
	}
	
}
