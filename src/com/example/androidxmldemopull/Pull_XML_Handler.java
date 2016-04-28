package com.example.androidxmldemopull;

import java.io.InputStream;
import java.util.ArrayList;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import android.util.Log;

/**
 * Pull方式解析XML数据
 * 
 * @author jy_dingsufu
 * 
 */
public class Pull_XML_Handler {
	// xml解析用到的Tag
	private String kEventName = "event";
	private String kTextName = "text";
	private String kTimeElementName = "time";
	private String klongitudeElementName = "longitude";
	private String kLatitudeElementName = "latitude";
	private String kDepthElementName = "depth";
	private String kUncertaintyElevElementName = "uncertainty";
	// 用于保存xml解析获取的结果
	private ArrayList<EarthquakeEntry> earthquakeEntryList = null;
	private EarthquakeEntry earthquakeEntry = null;
	private Boolean startEntryElementFlag = false;
	private Boolean startDescriptionElementFlag = false;
	private Boolean startOriginElementFlag = false;
	private Boolean startTimeElementFlag = false;
	private Boolean startLongitudeElementFlag = false;
	private Boolean startLatitudeElementFlag = false;
	private Boolean startDepthElementFlag = false;

	// 解析xml数据
	public ArrayList<EarthquakeEntry> parse(InputStream inStream) {
		try {
			// 创建XmlPullParser,有两种方式
			// 方式一:使用工厂类XmlPullParserFactory
			XmlPullParserFactory pullFactory = XmlPullParserFactory
					.newInstance();
			XmlPullParser xmlPullParser = pullFactory.newPullParser();
			// //方式二:使用Android提供的实用工具类android.util.Xml
			// XmlPullParser xmlPullParser = Xml.newPullParser();
			xmlPullParser.setInput(inStream, "UTF-8");
			int eventType = xmlPullParser.getEventType();
			Log.i("tag", String.valueOf(eventType));
			// 用于判断是否中断读取xml其他数据的标志
			boolean isDone = false;
			// 具体解析xml
			while ((eventType != XmlPullParser.END_DOCUMENT)
					&& (isDone != true)) {
				String localName = null;
				switch (eventType) {
				case XmlPullParser.START_DOCUMENT: {
					earthquakeEntryList = new ArrayList<EarthquakeEntry>();
				}
					break;
				case XmlPullParser.START_TAG: {
					localName = xmlPullParser.getName();
					if (localName.equalsIgnoreCase(kEventName)) {
						earthquakeEntry = new EarthquakeEntry();
						startEntryElementFlag = true;

					} else if (startEntryElementFlag == true) {
						String currentData = null;
						if (localName.equalsIgnoreCase("description")) {
							startDescriptionElementFlag = true;

						} else if (startDescriptionElementFlag == true) {
							if (localName.equalsIgnoreCase(kTextName)) {
								currentData = xmlPullParser.nextText();
								earthquakeEntry.setEarthQuakeName(currentData);

							}
						} else if (localName.equalsIgnoreCase("origin")) {

							startOriginElementFlag = true;
						} else if (startOriginElementFlag == true) {
							if (localName.equalsIgnoreCase(kTimeElementName)) {
								startTimeElementFlag = true;
							} else if (startTimeElementFlag == true) {
								if (localName.equalsIgnoreCase("value")) {
									currentData = xmlPullParser.nextText();
									earthquakeEntry.setTime(currentData);
								}
							} else if (localName
									.equalsIgnoreCase(klongitudeElementName)) {
								startLongitudeElementFlag = true;
							} else if (startLongitudeElementFlag == true) {
								if (localName.equalsIgnoreCase("value")) {
									currentData = xmlPullParser.nextText();

									earthquakeEntry.setLongitude(currentData);
								}
							} else if (localName
									.equalsIgnoreCase(kLatitudeElementName)) {
								startLatitudeElementFlag = true;
							} else if (startLatitudeElementFlag == true) {
								if (localName.equalsIgnoreCase("value")) {
									currentData = xmlPullParser.nextText();
									earthquakeEntry.setLatitude(currentData);
								}
							} else if (localName
									.equalsIgnoreCase(kDepthElementName)) {
								startDepthElementFlag = true;
							} else if (startDepthElementFlag == true) {
								if (localName.equalsIgnoreCase("value")) {
									currentData = xmlPullParser.nextText();
									earthquakeEntry.setDepth(currentData);
								} else if (localName
										.equalsIgnoreCase(kUncertaintyElevElementName)) {
									// xmlPullParser.nextText()方法可以获取下一个Text类型元素的值
									currentData = xmlPullParser.nextText();
									earthquakeEntry.setUncertainty(currentData);
								}
							}

						}

					}
				}
					break;
				case XmlPullParser.END_TAG: {
					localName = xmlPullParser.getName();
					if (localName.equalsIgnoreCase(kEventName)
							&& (startEntryElementFlag == true)) {
						earthquakeEntryList.add(earthquakeEntry);
						startEntryElementFlag = false;
					} else if (localName.equalsIgnoreCase("description")
							&& (startDescriptionElementFlag == true)) {
						startDescriptionElementFlag = false;
					} else if (localName.equalsIgnoreCase("origin")
							&& (startOriginElementFlag == true)) {
						startOriginElementFlag = false;
					} else if (localName.equalsIgnoreCase("time")
							&& (startTimeElementFlag == true)) {
						startTimeElementFlag = false;
					} else if (localName.equalsIgnoreCase("longitude")
							&& (startLongitudeElementFlag == true)) {
						startLongitudeElementFlag = false;
					} else if (localName.equalsIgnoreCase("latitude")
							&& (startLatitudeElementFlag == true)) {
						startLatitudeElementFlag = false;
					} else if (localName.equalsIgnoreCase("depth")
							&& (startDepthElementFlag == true)) {
						startDepthElementFlag = false;
					}
				}
					break;
				default:
					break;
				}

				// 使用parser.next()可以进入下一个元素并触发相应事件
				eventType = xmlPullParser.next();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		Log.v("Pull", "End");
		return earthquakeEntryList;
	}


	/**
	 * 获取ArrayList<EarthquakeEntry> items数据
	 * 
	 * @param inStream
	 * @return
	 */
	public ArrayList<EarthquakeEntry> fetchItems(InputStream inStream) {
		ArrayList<EarthquakeEntry> items = new ArrayList<EarthquakeEntry>();
		items = parse(inStream);
		return items;
	}

}
