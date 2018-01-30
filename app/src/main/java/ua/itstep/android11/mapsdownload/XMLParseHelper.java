package ua.itstep.android11.mapsdownload;

import android.content.Context;
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
    private final String FILE_EXT = "2.obf.zip";
    private final String FILE_ = "_";

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
        int depth = 1;

        //<region_list>
        region.setDepth(depth);
        region.setItemDownloadRootUrl("");

        while (parser.next() != XmlPullParser.END_DOCUMENT) {
            //String tmp = "";
            switch (parser.getEventType()) {
                // document start
                case XmlPullParser.START_DOCUMENT:
                    Log.d(Prefs.TAG, "START_DOCUMENT");
                    break;

                case XmlPullParser.START_TAG:

                    //child
                    if (parser.getDepth() > region.getDepth() + 1) {
                        region = child;
                    }

                    if (parser.getDepth() == region.getDepth() + 1) {
                        child = new RegionModel();
                        child.setParent(region);
                        child.setDepth(parser.getDepth());
                        child.setMapAvailable(true);
                        child.setDownloadingStatus(DownloadingStatus.NOT_DOWNLOADED);
                        for (int i = 0; i < parser.getAttributeCount(); i++) {
                            if (parser.getAttributeName(i).equals(ATTR_NAME)) {
                                child.setRegionName(parser.getAttributeValue(i));
                                child.setItemDownloadRootUrl(region.getItemDownloadRootUrl());
                                //Log.d(Prefs.TAG, parser.getDepth() + " " + parser.getAttributeValue(i));
                            }
                            else if (parser.getAttributeName(i).equals(ATTR_MAP)) {
                                child.setMapAvailable(parser.getAttributeValue(i).equals("yes") ? true : false);
                                //Log.d(Prefs.TAG, parser.getDepth() + " " + parser.getAttributeValue(i));
                            }
                            else if (parser.getAttributeName(i).equals(ATTR_TYPE)) {
                                child.setMapAvailable(false);
                            }

                        }
                        if (parser.getDepth() == 2) {
                            StringBuilder filename = new StringBuilder(child.getRegionName());
                            filename.append(FILE_);
                            filename.append(FILE_EXT);
                            child.setItemDownloadRootUrl(filename.toString());

                            //ItemDownloadRootUrl=>europe_2.obf.zip
                        }
                        if (parser.getDepth() > 2) {
                            StringBuilder filename = new StringBuilder(region.getItemDownloadUrl());
                            filename.append(child.getRegionName());
                            filename.append(FILE_);
                            child.setItemDownloadUrl(filename.toString());
                            //1.ItemDownloadUrl=>france_
                            //2.ItemDownloadUrl=>france_corse_
                        }

                        region.addChildren(child);
                    }

                    break;

                case XmlPullParser.END_TAG:
                    //Log.d(Prefs.TAG, "END_TAG: = " + parser.getDepth());

                    if (parser.getDepth() == region.getDepth()) {
                        child = region;
                        region = region.getParent();
                    }
                    StringBuilder s = new StringBuilder(child.getItemDownloadUrl());
                    s.append(child.getItemDownloadRootUrl()); //france_corse_ + europe_2.obf.zip
                    String name = s.toString();
                    name = capitalize(name);  //France_corse_europe_2.obf.zip
                    child.setItemDownloadUrl(name);
                    name = child.getRegionName();
                    name = capitalize(name);
                    child.setRegionName(name);
                    //Log.d(Prefs.TAG, getClass().getSimpleName() +" ItemDownloadUrl = " +child.getRegionName()+ " " +child.getItemDownloadUrl());
                    break;

                case XmlPullParser.TEXT:
                    //Log.d(Prefs.TAG, "text = " + parser.getText());
                    break;

                default:
                    break;
            }
        }
    }

    //Capitalize first letter
    private String capitalize(final String line) {
        if (line.length() == 0) { return line; }
        return Character.toUpperCase(line.charAt(0)) + line.substring(1);
    }



}
