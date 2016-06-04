package org.ibatis.spring.test;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.xsd.XSDSchema;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureCollections;
import org.geotools.feature.GeometryAttributeImpl;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.LiteCoordinateSequence;
import org.geotools.geometry.jts.LiteCoordinateSequenceFactory;
import org.geotools.gml3.GML;
import org.geotools.gml3.GMLConfiguration;
import org.geotools.xml.Encoder;
import org.geotools.xml.Parser;
import org.junit.Test;
import org.opengis.feature.GeometryAttribute;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.xml.sax.SAXException;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

public class GemoTest {
	
	static GeometryFactory gf = new GeometryFactory();
    
    static LiteCoordinateSequenceFactory liteCSF = new LiteCoordinateSequenceFactory();
    static GeometryFactory liteGF = new GeometryFactory(liteCSF);
    
   

	@Test
	public void test() {
		fail("Not yet implemented");
	}
	
	
	@Test
	public void PointToGeom() 
	{
		WKTReader fromText = new WKTReader();
		Point p;
		try {
			p = (Point)fromText.read("Point(1 1)");
			p.setSRID(4326);
			//System.out.println(p);
			
			
			Geometry  geom = fromText.read("Point(1 1)");
			
			Geometry  geom2 = fromText.read("Point(2 2)");
			//geom.setSRID(4326);
			
			//System.out.println(geom2.distance(geom));
			
			//System.out.println(geom);
			
			//System.out.println(geom.toText());
			
			/*
			
			SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
	        tb.setName("test");
	        tb.setCRS(null);
	        tb.add("testGeometry", Point.class);
	        tb.setDefaultGeometry("testGeometry");
			
			
			SimpleFeatureType sft = tb.buildFeatureType();
		
			
			
			
			SimpleFeatureBuilder sfb = new SimpleFeatureBuilder(sft);
			
			
			
			SimpleFeature sf = sfb.buildFeature("123");
				
			sf.setDefaultGeometry(geom);
			
			System.out.println(sf.getDefaultGeometry()+"--");
			
			*/
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	@Test
	public  void PolygonToGemo()
	{
		WKTReader fromText = new WKTReader();
		Polygon p ;
		
		String text = "Polygon((10 10, 10 20, 20 20, 20 15, 10 10))";
		
		try {
			p = (Polygon) fromText.read(text);
			p.setSRID(4326);
			
			System.out.println(p);
			
			Geometry  geom = fromText.read(text);
			
			
			System.out.println(geom.toText());
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void LineStringToGeom()
	{
		WKTReader fromText = new WKTReader();
		LineString ls ;
		
		String text = "LineString( 10 10, 20 20, 30 40)";
		
		try {
			ls = (LineString) fromText.read(text);
			ls.setSRID(4326);
			
			System.out.println(ls);
			
			Geometry  geom = fromText.read(text);
			
			
			System.out.println(geom.toText());
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	@Test
	public void MultiPointToGeom()
	{
		WKTReader fromText = new WKTReader();
		MultiPoint p;
		try {
			p = (MultiPoint)fromText.read("MultiPoint((10 10),(20 20))");
			p.setSRID(4326);
			System.out.println(p);
			
			
			Geometry  geom = fromText.read("MultiPoint((10 10),(20 20))");
			geom.setSRID(4326);
			System.out.println(geom);
			
			System.out.println(geom.toText());
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Test
	public void MultiLineStringToGeom()
	{
		WKTReader fromText = new WKTReader();
		MultiLineString p ;
		
		String text = "MultiLineString((10 10, 20 20),(15 15, 30 15))";
		
		try {
			p = (MultiLineString) fromText.read(text);
			p.setSRID(4326);
			
			System.out.println(p);
			
			Geometry  geom = fromText.read(text);
			
			
			System.out.println(geom.toText());
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Test
	public void MultiPolygonToGeom()
	{
		WKTReader fromText = new WKTReader();
		MultiPolygon p ;
		
		String text = "MultiPolygon(((10 10, 10 20, 20 20, 20 15, 10 10)),((60 60, 70 70, 80 60, 60 60 )))";
		
		try {
			p = (MultiPolygon) fromText.read(text);
			p.setSRID(4326);
			
			System.out.println(p);
			
			Geometry  geom = fromText.read(text);
			
			
			System.out.println(geom.toText());
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	@Test
	public void GeometryCollectionToGeom()
	{
		WKTReader fromText = new WKTReader();
		GeometryCollection p ;
		
		String text = "GeometryCollection(POINT (10 10),POINT (30 30),LINESTRING (15 15, 20 20))";
		
		try {
			p = (GeometryCollection) fromText.read(text);
			p.setSRID(4326);
			
			System.out.println(p);
			
			Geometry  geom = fromText.read(text);
			
			
			System.out.println(geom.toText());
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Test
	public void GeomToSimpleFeature()
	{
		WKTReader fromText = new WKTReader();
		
		FeatureCollection results = FeatureCollections.newCollection();
		Point p;
		try {
			p = (Point)fromText.read("Point(1 1)");
			p.setSRID(4326);
			//System.out.println(p);
			
			
			Geometry  geom = fromText.read("MultiPolygon(((10 10, 10 20, 20 20, 20 15, 10 10)),((60 60, 70 70, 80 60, 60 60 )))");
			
			Geometry  geom2 = fromText.read("Point(2 2)");
			//geom.setSRID(4326);
			
			//System.out.println(geom2.distance(geom));
			
			//System.out.println(geom);
			
			//System.out.println(geom.toText());
			
			
			
			SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
	        tb.setName("test");
	        tb.setCRS(null);
	        tb.add("testGeometry", Point.class);
	        
	        tb.add("Geometry", Geometry.class);
	        tb.setDefaultGeometry("Geometry");
			
			SimpleFeatureType sft = tb.buildFeatureType();
		
			
			
			
			SimpleFeatureBuilder sfb = new SimpleFeatureBuilder(sft);
			
			
			
			SimpleFeature sf = sfb.buildFeature("123");
				
			sf.setDefaultGeometry(geom);
			
			results.add(sf);
			
			System.out.println(results.size()+"--");
			
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Test 
	public void createPoint()
	{
		Point p = liteGF.createPoint(new LiteCoordinateSequence(new double[] { 1, 2, 100}, 3));
		
		System.out.println(p.toText());
		
		
		
	}
	
	@Test 
	public void createLineString3D()
	{
		
		
		LineString ls = liteGF.createLineString(liteCSF.create(new double[] { 1, 2, 100,  3, 4, 200}, 3));
		
		System.out.println(ls);
	}
	
	@Test
	public void createGML()
	{
		WKTReader fromText = new WKTReader();
		GMLConfiguration configuration = new GMLConfiguration();
		Parser parser = new Parser(configuration);
		try {
			
			Geometry  geom = fromText.read("MultiPolygon(((10 10, 10 20, 20 20, 20 15, 10 10)),((60 60, 70 70, 80 60, 60 60 )))");
			
			SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
	        tb.setName("go");
	        tb.setCRS(null);
	        tb.add("testGeometry", Point.class);
	        
	        tb.add("Geometry", Geometry.class);
	        tb.setDefaultGeometry("Geometry");
			
			SimpleFeatureType sft = tb.buildFeatureType();
		
			
			
			
			SimpleFeatureBuilder sfb = new SimpleFeatureBuilder(sft);
			
			
			
			SimpleFeature sf = sfb.buildFeature("statu");
				
			sf.setDefaultGeometry(geom);
			
			
			
			FeatureCollection fc = FeatureCollections.newCollection(); ;
			
			fc.add(sf);
			
			
			
			XSDSchema schema = GML.getInstance().getSchema();
			
			Encoder encoder = new Encoder(configuration, schema);
			
			
			ByteArrayOutputStream output = new ByteArrayOutputStream();
	        encoder.write(fc, GML._FeatureCollection, output);
	        
	        System.out.println(output);
			
			
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}
	
	
	@Test
	public void createKml()
	{
		
	}
	
	
	
}
