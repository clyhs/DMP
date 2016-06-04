package org.ibatis.spring.typehandler;

import java.sql.SQLException;

import com.ibatis.sqlmap.client.extensions.ParameterSetter;
import com.ibatis.sqlmap.client.extensions.ResultGetter;
import com.ibatis.sqlmap.client.extensions.TypeHandlerCallback;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

public class PointTypeHandlerCallback implements TypeHandlerCallback {

	@Override
	public Object getResult(ResultGetter arg0) throws SQLException {
		// TODO Auto-generated method stub
		
		String Text = arg0.getString();
		
		WKTReader fromText = new WKTReader();
		
		Point p = null;
		try {
			p = (Point) fromText.read(Text);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return p;
	}

	@Override
	public void setParameter(ParameterSetter arg0, Object arg1)
			throws SQLException {
		// TODO Auto-generated method stub
		
		Point p = (Point)arg1;
		
		arg0.setString(p.toText());

	}

	@Override
	public Object valueOf(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}
