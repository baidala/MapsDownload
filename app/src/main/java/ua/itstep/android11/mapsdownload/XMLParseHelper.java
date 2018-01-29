package ua.itstep.android11.mapsdownload;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.kxml2.io.KXmlParser;

import java.io.IOException;

/**
 * Created by Maksim Baydala on 26/01/18.
 */

public class XMLParseHelper {

    private final String ATTR_NAME = "name";
    private final String ATTR_MAP = "map";
    private final String ATTR_TYPE = "type";

    private Context ctx;
    private RegionModel region;
    private RegionModel child;


    public XMLParseHelper(Context c) {
        this.ctx = c;
    }

    public void setData(RegionModel r) throws IOException, XmlPullParserException {
        region = r;
        parseXML();
    }

    private XmlPullParser newXMLPullParser() throws XmlPullParserException {
        return new KXmlParser();
    }

    private XmlPullParser prepareXpp() {
        return ctx.getResources().getXml(R.xml.regions);
    }



    private synchronized void parseXML() throws IOException, XmlPullParserException {

        XmlPullParser parser = prepareXpp();
        //parser.setInput(stream, "UTF-8");
        //parser();


        String map = " map = yes";
        int depth = 1;

        //<region_list>
        region.setDepth(depth);

        /*
        if (parser.next() != XmlPullParser.END_DOCUMENT) {

            Log.d(Prefs.TAG, "<region_list> " + parser.getName() + " "+ parser.getDepth() );
        }*/


        while (parser.next() != XmlPullParser.END_DOCUMENT) {
            String tmp = "";
            switch (parser.getEventType()) {
                // начало документа
                case XmlPullParser.START_DOCUMENT:
                    Log.d(Prefs.TAG, "START_DOCUMENT");
                    break;
                // начало тэга
                case XmlPullParser.START_TAG:

                    /*Log.d(Prefs.TAG, "START_TAG: name = " + parser.getName()
                            + ", depth = " + parser.getDepth() + ", attrCount = "
                            + parser.getAttributeCount());*/
                    //child
                    if (parser.getDepth() > region.getDepth() + 1) {
                        region = child;
                    }
                    if(parser.getDepth() == region.getDepth() + 1) {
                        child = new RegionModel();
                        child.setParent(region);
                        child.setDepth(parser.getDepth());
                        child.setMapAvailable(true);
                        child.setDownloadingStatus(DownloadingStatus.NOT_DOWNLOADED);
                        child.setItemDownloadUrl("Denmark_europe_2.obf.zip");
                        for (int i = 0; i < parser.getAttributeCount(); i++) {
                            if (parser.getAttributeName(i).equals(ATTR_NAME)) {
                                tmp += " " + parser.getAttributeValue(i);
                                child.setRegionName(parser.getAttributeValue(i));
                                //Log.d(Prefs.TAG, parser.getDepth() + " " + parser.getAttributeValue(i));
                                //i = parser.getAttributeCount();
                            }
                            else if (parser.getAttributeName(i).equals(ATTR_MAP)) {
                                map = " map = " + parser.getAttributeValue(i);
                                child.setMapAvailable(parser.getAttributeValue(i).equals("yes") ? true : false);
                                //Log.d(Prefs.TAG, parser.getDepth() + " " + parser.getAttributeValue(i));
                            }
                            else if (parser.getAttributeName(i).equals(ATTR_TYPE)) {
                                child.setMapAvailable(false);
                            }
                        /*tmp = tmp + parser.getAttributeName(i) + " = "
                                + parser.getAttributeValue(i) + ", ";*/
                        }

                        region.addChildren(child);
                    }

                    tmp += " " + parser.getDepth();

                    tmp += map;
                    if (!tmp.isEmpty()) {
                        //Log.d(Prefs.TAG, "Attributes: " + tmp);

                        Log.d(Prefs.TAG, tmp);
                    }
                    break;
                // конец тэга
                case XmlPullParser.END_TAG:
                    Log.d(Prefs.TAG, "END_TAG: = " + parser.getDepth());
                    if (parser.getDepth() == region.getDepth()) {
                        region = region.getParent();
                    }
                    break;
                // содержимое тэга
                case XmlPullParser.TEXT:
                    //Log.d(Prefs.TAG, "text = " + parser.getText());
                    break;

                default:
                    break;
            }
        }
    }



}
