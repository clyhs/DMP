package org.ibatis.spring.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.xsd.XSDSchema;
import org.geotools.GML;
import org.geotools.GML.Version;
import org.geotools.data.DataUtilities;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureCollections;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.WKTReader2;

import org.geotools.xml.Encoder;
import org.geotools.xml.Parser;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;
import org.xml.sax.SAXException;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;


@Controller
@RequestMapping("/me")
public class XMLController extends MultiActionController {
	
	@RequestMapping("/new")
	public ModelAndView _new(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("method01 controller");
		
		Map model =new HashMap(); 
		
		
		
			
		SimpleFeatureType TYPE = DataUtilities.createType("Location", "geom:Point,name:String");

        SimpleFeatureCollection collection = FeatureCollections.newCollection("internal");
        WKTReader2 wkt = new WKTReader2();

        collection.add(SimpleFeatureBuilder.build(TYPE, new Object[] { wkt.read("POINT (1 2)"),
                "name1" }, null));
        collection.add(SimpleFeatureBuilder.build(TYPE, new Object[] { wkt.read("POINT (4 4)"),
                "name2" }, null));

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GML encode = new GML(Version.GML2);
        encode.setNamespace("Location", "http://localhost/Location.xsd");
        encode.setLegacy(true);
        encode.encode(out, collection);

        out.close();

        String gml = out.toString();
        
        model.put("1", gml);
		
		return new ModelAndView("/me/new",model);
	}

}
