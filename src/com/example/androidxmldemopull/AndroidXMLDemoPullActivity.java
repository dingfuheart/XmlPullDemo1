package com.example.androidxmldemopull;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class AndroidXMLDemoPullActivity extends Activity {
	/** Called when the activity is first created. */
	
	// 定义显示的List相关变量
	ListView list;
	ArrayAdapter<EarthquakeEntry> adapter;
	ArrayList<EarthquakeEntry> earthquakeEntryList;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// 用ListView进行显示
		list = (ListView) this.findViewById(R.id.list);
		
		// 调用 execute() 方法将启动 AsyncTask ，继而触发后台线程并调用 doInBackground(...)方法
		new FetchItemsTask().execute();
		setupAdapter();
	}

	/**
	 * 异步提交技术 使用后台线程最简便的方式是使用 AsyncTask 工具类。 AsyncTask 创建后台线程后，我们便 可在该线程上调用
	 * doInBackground(...) 方法运行代码
	 * 
	 * 在PhotoGalleryFragment.java中，添加一个名为 FetchItemsTask 的内部类。 覆盖
	 * AsyncTask.doInBackground(...) 方法，从目标网站获取数据并记录下日志
	 */
	public class FetchItemsTask extends
			AsyncTask<Void, Void, ArrayList<EarthquakeEntry>> {

		@Override
		protected ArrayList<EarthquakeEntry> doInBackground(Void... arg0) {

//			return new Pull_XML_Handler()
//					.fetchItems(readEarthquakeDataFromFile());
			return new Pull_XML_Handler()
			.fetchItems(readEarthquakeDataFromInternet());
		}

		/**
		 * AsyncTask 提供有另一个可覆盖的 onPostExecute(...) 方法。 onPostExecute(...) 方法在
		 * doInBackground(...) 方法执行完毕后才会运行，而且它是在 主线程而非后台线程上运行的。 因此，在该方法中更新UI比较安全
		 */
		@Override
		protected void onPostExecute(ArrayList<EarthquakeEntry> items) {
			earthquakeEntryList = items;
			setupAdapter();//这边不能省
		}
	}

	void setupAdapter() {
		// 设置adapter前，应检查 getActivity() 的返回结果是否为空。这是因为fragment可脱离activity而存在。
		// 这之前没出现这种情况是因为所有的方法调用都是由系统框架的回调方法驱动的。
		if (list == null)
			return;

		if (earthquakeEntryList != null) {
			// list.setAdapter(new ArrayAdapter<EarthquakeEntry>(this,
			// android.R.layout.simple_list_item_1, earthquakeEntryList));
			list.setAdapter(new mAdapter(this));

		} else {

			list.setAdapter(null);

		}
	}

	/** 创建ViewHolder静态类 */
	public static class ViewHolder {
		public TextView earthName;
		public TextView time;
		public TextView longitude;
		public TextView latitude;
		public TextView depth;
		public TextView uncertainty;
	}

	/** 自定义Adapter,继承BaseAdapter,并重写方法 */
	public class mAdapter extends BaseAdapter {
		/** 新建一个LayoutInfalter对象用来导入布局 */
		private LayoutInflater mInflater = null;

		/** 构造函数 */
		public mAdapter(Context context) {
			// 根据context上下文加载布局，这里的是ActivityMain_MyBaseAdapter本身，即this
			this.mInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			// 在此适配器中所代表的数据集中的条目数
			return earthquakeEntryList.size();
		}

		@Override
		public Object getItem(int position) {
			// 获取数据集中与指定索引对应的数据项
			return position;
		}

		@Override
		public long getItemId(int position) {
			// 获取在列表中与指定索引对应的行id
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				// 根据自定义的Item布局加载布局
				convertView = mInflater.inflate(R.layout.list_item, null);
				holder.earthName = (TextView) convertView.findViewById(R.id.AndroidXMLDemoPull_EarthName);
				holder.time = (TextView) convertView.findViewById(R.id.AndroidXMLDemoPull_time);
				holder.longitude = (TextView) convertView.findViewById(R.id.AndroidXMLDemoPull_longitude);
				holder.latitude = (TextView) convertView.findViewById(R.id.AndroidXMLDemoPull_latitude);
				holder.depth = (TextView) convertView.findViewById(R.id.AndroidXMLDemoPull_depth);
				holder.uncertainty = (TextView) convertView.findViewById(R.id.AndroidXMLDemoPull_uncertainty);
				convertView.setTag(holder);// 将设置好的布局保存到缓存中，并将其设置到TAG中
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
				holder.earthName.setText(earthquakeEntryList.get(position)
						.getEarthQuakeName().toString());
				holder.time
						.setText(earthquakeEntryList.get(position).getTime().toString());
				holder.longitude.setText(earthquakeEntryList.get(position)
						.getLongitude().toString());
				holder.latitude.setText(earthquakeEntryList.get(position)
						.getLatitude().toString());
				holder.depth.setText(earthquakeEntryList.get(position)
						.getDepth().toString());
				holder.uncertainty.setText(earthquakeEntryList.get(position)
						.getUncertainty().toString());
			return convertView;

		}

	}

	/**
	 * xml文件的获取 1.xml文件放在res的xml文件夹下（推荐）使用 XmlResourceParser xmlParser
	 * =this.getResources().getXml(R.xml.XXX); 2.xml文件放在raw的xml文件夹下使用
	 * InputStream inputStream =this.getResources().openRawResource(R.raw.XXX);
	 * 3.xml文件放在assets文件夹下(本人测试发现通过此方法获取的XML文档不能带有首行：<?xml version="1.0"
	 * encoding="utf-8"?>， 否则解析报错，具体原因未查明: InputStream inputStream
	 * =getResources().getAssets().open(fileName);
	 * 4.xml文件放在SD卡，path路径根据实际项目修改，此次获取SDcard根目录： String path
	 * =Environment.getExternalStorageDirectory().toString(); File xmlFlie = new
	 * File(path+fileName); InputStream inputStream = new
	 * FileInputStream(xmlFlie);
	 */
	private InputStream readEarthquakeDataFromFile() {
		// 从本地获取地震数据
		InputStream inStream = null;
		try {
			inStream = this.getAssets().open("2.5_day.quakeml.xml");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return inStream;
	}

	/**
	 * 从网上读取XML数据
	 * 
	 * @return
	 */
	private InputStream readEarthquakeDataFromInternet() {
		// 从网络上获取实时地震数据
		URL infoUrl = null;
		InputStream inStream = null;
		try {
			infoUrl = new URL(
					"http://earthquake.usgs.gov/realtime/product/phase-data/us20004vvx/us/1454127141040/quakeml.xml");
			URLConnection connection = infoUrl.openConnection();
			HttpURLConnection httpConnection = (HttpURLConnection) connection;
			int responseCode = httpConnection.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) {
				inStream = httpConnection.getInputStream();
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return inStream;
	}

}
