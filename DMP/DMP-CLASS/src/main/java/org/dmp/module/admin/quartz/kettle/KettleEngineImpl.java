package org.dmp.module.admin.quartz.kettle;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.dmp.core.util.StrUtil;
import org.dmp.pojo.admin.quartz.MonitorDataBean;
import org.pentaho.di.core.KettleEnvironment;
import org.pentaho.di.core.ProgressMonitorListener;
import org.pentaho.di.core.database.Database;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.logging.JobLogTable;
import org.pentaho.di.core.logging.LogChannel;
import org.pentaho.di.core.logging.TransLogTable;
import org.pentaho.di.core.plugins.PluginRegistry;
import org.pentaho.di.core.plugins.RepositoryPluginType;
import org.pentaho.di.core.util.EnvUtil;
import org.pentaho.di.job.Job;
import org.pentaho.di.job.JobConfiguration;
import org.pentaho.di.job.JobExecutionConfiguration;
import org.pentaho.di.job.JobMeta;
import org.pentaho.di.repository.ObjectId;
import org.pentaho.di.repository.RepositoriesMeta;
import org.pentaho.di.repository.Repository;
import org.pentaho.di.repository.RepositoryDirectory;
import org.pentaho.di.repository.RepositoryDirectoryInterface;
import org.pentaho.di.repository.RepositoryMeta;
import org.pentaho.di.repository.StringObjectId;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransConfiguration;
import org.pentaho.di.trans.TransExecutionConfiguration;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.www.CarteObjectEntry;
import org.pentaho.di.www.JobMap;
import org.pentaho.di.www.TransformationMap;






public class KettleEngineImpl implements IKettleEngine{
	
	
	private  static JobMap jobMap = new JobMap();
	private  static TransformationMap transMap = new TransformationMap();
	private  static LogChannel logChannel ;
	private  static String[] filetypes = new String[]{TYPE_TRANS,TYPE_JOB};
	private  static Map<String,String> varMap = new HashMap<String,String>();
	
	{
		try{
		    KettleEnvironment.init();
		    setVariables();
		    logChannel = new LogChannel("spoon");
		}catch(Exception ex){
			
		}
	}
	
	private static void setVariables(){
		for(String s:EnvUtil.getEnvironmentVariablesForRuntimeExec()){
			if(s.indexOf("MCP")!=-1){
				String[] vs = s.split("=");
				//System.out.println(vs[0]+"="+vs[1]);
				varMap.put(vs[0], vs[1]);
			}
		}
	}
	
	/**
	 * 
	 * @param filepath
	 * @param filetype
	 * @return
	 */
	public final synchronized boolean execute(String filepath,String filetype){
		boolean flag = false;
		
		try{
			if(filetype.equals(this.filetypes[0])){
				TransMeta tm = new TransMeta(filepath);
				
				flag = this.executeTrans(tm);
			}
			if(filetype.equals(this.filetypes[1])){
				JobMeta jm = new JobMeta(filepath,null);
				flag = this.executeJob(jm, null, null);
				
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return flag;
	}
	public final synchronized boolean execute(String repName, String username,
			String password, String filePath, String fileName,
			String fileType) {
		
		boolean flag = false;
		
		Repository r = null;
		int i = 0;
		try{
			r = this.getRep(repName, username, password);
			RepositoryDirectoryInterface tree = this.getDirectory(r, filePath);
			ProgressMonitorListener pml = null;
			if(tree!=null){
				if(fileType.equals(filetypes[0])){
					TransMeta tm =r.loadTransformation(fileName, tree, null, true, null);
					
					flag = this.executeTrans(tm);
				}
				if(fileType.equals(filetypes[1])){
					JobMeta jm =r.loadJob(fileName, tree, null, null);
					
					flag = this.executeJob(jm, r, null);
				}
			}else{
				r.disconnect();
			}
		}catch(Exception e){
			
		}
		
		return flag;
	}
	
	public final List getReps(){
		ArrayList list = new ArrayList();
		RepositoriesMeta rms = new RepositoriesMeta();
		
		int i = 0;
		try {
			rms.readData();
		    i= rms.nrRepositories();
			
			for (int j = 0; j < i; j++){
				RepositoryMeta rm =  rms.getRepository(j);
				String name = rm.getName();
				list.add(name);
			}
		} catch (Exception e) {
		}
		return list;
	}
	
	public int checkRepLogin(String Repositories, String userName, String password) {
		int i = 1;
		try{
			Repository r = this.getRep(Repositories, userName, password);
			r.disconnect();
			i = 0;
		}catch(Exception e){
			
		}
		return i;
	}
	
	private static Repository getRep(String repName, String username, String password){
		
		RepositoriesMeta rsMeta = new RepositoriesMeta();
		RepositoryMeta   rMeta  = null;
		Repository       r  = null;
		PluginRegistry   pr = null;   
		RepositoryPluginType rpy = RepositoryPluginType.getInstance();
		try {
			rsMeta.readData();
			rMeta = rsMeta.findRepository(repName);
			pr = PluginRegistry.getInstance();
			
			r =  pr.loadClass(RepositoryPluginType.class, rMeta, Repository.class); 
			r.init(rMeta);
			r.connect(username, password);
			return r;
		} catch (Exception e) {
		}
		
		return null;
	}
	
	private static RepositoryDirectoryInterface getDirectory(Repository rep, String filePath){
		RepositoryDirectoryInterface tree = null;
		try{
			tree = rep.loadRepositoryDirectoryTree();
			for(int i=0;i<tree.getNrSubdirectories();i++){
				RepositoryDirectory subdir = tree.getSubdirectory(i); 
	            System.out.println(" id " + subdir.getObjectId() + " name " + subdir.getName()); 			
			}
			if ((filePath != null) && (!"/".equals(filePath)) && (!"".equals(filePath))){
				rep.findDirectory(filePath);
			}
		}catch(Exception e){
			
		}
		return tree;
	}
	
	private static boolean executeJob(JobMeta jm, Repository rep, String[] paramStr){
		
		Job job = null;
		JobConfiguration jc =new JobConfiguration(jm, new JobExecutionConfiguration());
		
		try {
			if (rep != null)
			{
				job = new Job(rep,jm);
			}else{
				String name = jm.getName();
				
				String filename = jm.getFilename();
				
				//job = new Job(name,filename,null);
				job = new Job(null,jm);
			}
			logChannel.logMinimal("ETL--JOB Start of run");
			String startTime = StrUtil.getSysdateToString("yyyy/MM/dd HH:mm:ss.SSS");
			String jobName = job.getJobname();
			String objId = null;
			if(job.getObjectId()!=null){
				objId = new CarteObjectEntry(jobName, job.getObjectId().toString()).getId();
			}else{
				objId = job.getFilename();
			}
			/*
		    if(job.getObjectId()!=null){
		    	objId = job.getObjectId().toString();
		    }
		    if(objId!=null){
			    jobMap.addJob(jobName, new CarteObjectEntry(jobName, objId).getId() , job, jc);
			    
		    }else{
		    	jobMap.addJob(jobName, job.getFilename() , job, jc);
		    }*/
			System.out.println(objId);
			jobMap.addJob(jobName, objId , job, jc);
			
			Iterator<String> iter = varMap.keySet().iterator();
			while (iter.hasNext()) {
			    String key = iter.next();
			    String value = varMap.get(key);
			    job.setVariable(key, value);
			}
			
		    job.beginProcessing();
			job.run();
			logChannel.logMinimal("ETL--JOB Finished!");
			String endTime = StrUtil.getSysdateToString("yyyy/MM/dd HH:mm:ss.SSS");
			logChannel.logMinimal("ETL--JOB Start=" + startTime + ", Stop=" + endTime);
			Long l = StrUtil.StringToDate(endTime, "yyyy/MM/dd HH:mm:ss.SSS").getTime() - StrUtil.StringToDate(startTime, "yyyy/MM/dd HH:mm:ss.SSS").getTime();
			logChannel.logMinimal("ETL--JOB Processing ended after " + l / 1000L + " seconds.");
			
			return true;
			
		} catch (KettleException e) {
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return false;
	}
	
	
	
	
	private static boolean executeTrans(TransMeta tm){
		
		TransConfiguration tc = new TransConfiguration(tm,new TransExecutionConfiguration());
		Trans trans = null; 
		try
	    {
			trans = new Trans(tm);
			logChannel.logMinimal("ETL--TRANS Start of run");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
		    Calendar cal = Calendar.getInstance();
		    Date startTime =  cal.getTime();
		    String transName = tm.getName();
		    String objId = null;
		    if(tm.getObjectId()!=null){
		    	objId = new CarteObjectEntry(transName, tm.getObjectId().toString()).getId();
		    }else{
		    	objId = tm.getFilename();
		    }
		    
		    transMap.addTransformation(transName, objId, trans, tc);
		    String[] args = tm.getArguments();
		    Iterator<String> iter = varMap.keySet().iterator();
			while (iter.hasNext()) {
			    String key = iter.next();
			    String value = varMap.get(key);
			    trans.setVariable(key, value);
			}
		    trans.prepareExecution(args);
		    trans.startThreads();
		    trans.waitUntilFinished();
		    logChannel.logMinimal("ETL--TRANS Finished");
		    Date endTime = cal.getTime();
		    String sTime = sdf.format(startTime).toString();
		    String eTime = sdf.format(endTime).toString();
		    logChannel.logMinimal("ETL--TRANS Start=" + startTime + ", Stop=" + endTime);
			
		    Long l = endTime.getTime()-startTime.getTime();
		    logChannel.logMinimal("ETL--TRANS Processing ended after " + l / 1000L + " seconds.");
		    return true;
		    
		    
	    }catch(Exception e){
	    	e.printStackTrace();
	    }
		return false;
	}
	
	public static  void stopJob(String jobName)
	{
		
		try{
			Job job = jobMap.getJob(jobName);
			job.stopAll();
		}catch(Exception ex){


		}
		
	}
	
	public static void stopTrans(String transName)
	{
		try{
			Trans trans = transMap.getTransformation(transName);
			if(trans.isRunning()){
				trans.stopAll();
			}
		}catch(Exception ex){


		}
	}
	
	public final String getRepTreeJSON(String Repositories,  String userName, String password) {
		StringBuffer paramSB = new StringBuffer("[");
		Repository r = null;
		r= this.getRep(Repositories, userName, password);
		RepositoryDirectoryInterface d = this.getDirectory(r, "/");
		
		String reStr = this.getRepTreeJSON(r, d, paramSB);
		r.disconnect();
		paramSB.append("]");
		return paramSB.toString();
	}
	
	private String getRepTreeJSON(Repository rep, RepositoryDirectoryInterface d, StringBuffer paramSB){
		
		
		try {
			ObjectId objId = d.getObjectId();
			Random random = new Random();
			ObjectId id = new StringObjectId(String.valueOf(random.nextLong()));
			String name = d.getName();
			paramSB.append("{").append("id:'" + id + "'").append(",text:'" + name + "'").append(",expanded:true");
			int i = d.getNrSubdirectories();
			String[] transNames = rep.getTransformationNames(objId, false);
			String[] jobNames = rep.getJobNames(objId, false);
			if(i+transNames.length+jobNames.length>0)
			{
				paramSB.append(",leaf:false,children:[");
				for(int j = 0; j < i; j++)
				{
					RepositoryDirectory rd = d.getSubdirectory(j);
					getRepTreeJSON(rep,rd,paramSB);
				}
				for(int k = 0; k < transNames.length; k++)
				{
					if(i+k>0){
						paramSB.append(",");
					}
					ObjectId oId= rep.getTransformationID(transNames[k], d);
					id = new StringObjectId(String.valueOf(random.nextLong())+k);
					paramSB.append("{").append("id:'" + id + "_trans'").append(",text:'" + transNames[k] + "[ktr]'").append(",leaf:true}");
					
				}
				for(int m = 0; m < jobNames.length; m++)
				{
					if (i + transNames.length + m > 0){
				
						paramSB.append(",");
					}
					ObjectId oId = rep.getJobId(jobNames[m], d);
					id = new StringObjectId(String.valueOf(random.nextLong())+m);
					paramSB.append("{").append("id:'" + id + "_job'").append(",text:'" + jobNames[m] + "[kjb]'").append(",leaf:true}");
				
				}
				paramSB.append("]");
			}
			

			paramSB.append("}");
			
		} catch (KettleException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		
		return paramSB.toString();
	}
	
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		KettleEngineImpl kes = new KettleEngineImpl();
		kes.getReps();
		//kes.execute("dd", "", "", "C:\\Users\\clyhs\\.kettle", "trantest","ktr");
		//kes.execute("C:\\Users\\clyhs\\.kettle\\trantest.ktr", "ktr");
		kes.execute("dd", "", "", "C:\\Users\\clyhs\\.kettle", "JobTest","kjb");
		//kes.execute("C:\\Users\\clyhs\\.kettle\\JobTest.kjb", "kjb");
		//System.out.println(kes.getRepTreeJSON("dd", "", "").toString());

	}
	public List<MonitorDataBean> getLogData(String repName, String userName,
			String passWord, String filePath, String fileName,
			String fileType) {
		// TODO Auto-generated method stub
		List<MonitorDataBean>  list =  new ArrayList<MonitorDataBean>();
		
		try
		{
			Repository r = this.getRep(repName, userName, passWord);
			RepositoryDirectoryInterface tree = this.getDirectory(r, filePath);
			String logTable="";
			if(tree!=null){
				if(fileType.equals(filetypes[0])){
					TransMeta tm =r.loadTransformation(fileName, tree, null, true, null);
					TransLogTable translogtable = tm.getTransLogTable();
					DatabaseMeta databasemeta =translogtable.getDatabaseMeta();
					String tablename = translogtable.getTableName();
					list = this.getLogData(databasemeta,tablename,fileType);
				}
				if(fileType.equals(filetypes[1])){
					JobMeta jm =r.loadJob(fileName, tree, null, null);
					JobLogTable joblogtable= jm.getJobLogTable();
					DatabaseMeta databasemeta = joblogtable.getDatabaseMeta();
					String tablename = joblogtable.getTableName();
					list = this.getLogData(databasemeta,tablename,fileType);
				}
			}
		}catch(Exception ex)
		{
		}
		return list;
	}
	
	private static List<MonitorDataBean> getLogData(DatabaseMeta dbm,String logTable,String fileType){
		
		List<MonitorDataBean> list = null;
		ResultSet resultset = null;
		try
		{
			list =  new ArrayList<MonitorDataBean>();
			Database db = new Database(dbm);
			db.connect();
			StringBuffer sb = new StringBuffer("SELECT * FROM ");
			sb.append(logTable).append(" WHERE STATUS='end' ORDER BY STARTDATE DESC");
			resultset = db.openQuery(sb.toString());
			while(resultset.next())
			{
				MonitorDataBean mdb = new MonitorDataBean();
				if("ktr".equals(fileType))
                {
                    mdb.setId_batch(resultset.getInt("ID_BATCH"));
                    mdb.setTransname(resultset.getString("TRANSNAME"));
                } else
                {
                    mdb.setId_batch(resultset.getInt("ID_JOB"));
                    mdb.setTransname(resultset.getString("JOBNAME"));
                }
                mdb.setStatus(resultset.getString("STATUS"));
                mdb.setLines_read(resultset.getInt("LINES_READ"));
                mdb.setLines_written(resultset.getInt("LINES_WRITTEN"));
                mdb.setLines_update(resultset.getInt("LINES_UPDATED"));
                mdb.setLines_input(resultset.getInt("LINES_INPUT"));
                mdb.setLines_output(resultset.getInt("LINES_OUTPUT"));
                mdb.setErrors(resultset.getInt("ERRORS"));
                mdb.setStartDate(resultset.getTimestamp("STARTDATE"));
                mdb.setEndDate(resultset.getTimestamp("ENDDATE"));
                mdb.setLogDate(resultset.getTimestamp("LOGDATE"));
                mdb.setDepDate(resultset.getTimestamp("DEPDATE"));
                mdb.setReplayDate(resultset.getTimestamp("REPLAYDATE"));
                list.add(mdb);
			}
			resultset.close();
			db.disconnect();
			
		}catch(Exception ex)
		{
			//logger.info("KettleEngineImpl.getLogData(Object obj,String logTable,String fileType):"+ex.toString());
		}
		finally
		{
			if(resultset!=null){
				try {
					resultset.close();
				} catch (SQLException e) {
					System.out.println(e.toString());
				}
			}
		}
		return list;
	}

}
