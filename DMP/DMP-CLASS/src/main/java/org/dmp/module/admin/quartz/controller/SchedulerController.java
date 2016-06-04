package org.dmp.module.admin.quartz.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.fileupload.FileItem;
import org.dmp.core.util.FileUtil;
import org.dmp.core.util.JsonUtil;
import org.dmp.core.util.StrUtil;
import org.dmp.module.admin.quartz.kettle.KettleEngineImpl;
import org.dmp.module.admin.quartz.service.IMonitorService;
import org.dmp.module.admin.quartz.service.ISchedulerService;
import org.dmp.module.common.combobox.ComboBox;
import org.dmp.module.common.form.FormLoad;
import org.dmp.module.common.form.FormResponse;
import org.dmp.module.common.grid.Grid;
import org.dmp.pojo.admin.quartz.Repository;
import org.dmp.pojo.admin.quartz.ScheduleBean;
import org.quartz.SimpleTrigger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;



@Controller
public class SchedulerController {

	@Resource(name="SchedulerService")
	private ISchedulerService schedulerService;
	
	@Resource(name = "MonitorService")
	private IMonitorService monitorService;
	
	

	@RequestMapping(value = "/module/admin/quartz/schedulerIndex")
	public String schedulerIndex(HttpServletRequest oRequest)
	{
		return "/admin/quartz/scheduler";
	}
	
	/**
	 * 获取任务调度的列表信息
	 * @param oRequest
	 * @return
	 */
	@RequestMapping(value = "/module/admin/quartz/getSchedulerlist", method = RequestMethod.POST)
	@ResponseBody
	public Grid<ScheduleBean> getSchedulerlist(HttpServletRequest oRequest)
	{
		List<ScheduleBean> scheduleList = schedulerService.getList();
		Grid<ScheduleBean> oGrid = new Grid<ScheduleBean>();
		oGrid.setData(scheduleList);
		return oGrid;
	}
	
	/**
	 * 获取调度的信息
	 * @param oRequest
	 * @return
	 */
	@RequestMapping(value = "/module/admin/quartz/getScheduler", method = RequestMethod.POST)
	@ResponseBody
	public FormLoad<ScheduleBean> getScheduler(HttpServletRequest oRequest)
	{
		String jobName = oRequest.getParameter("jobName");
		ScheduleBean scheduleBean = schedulerService.getSchedule(jobName);
		FormLoad<ScheduleBean>  scheduler = new FormLoad<ScheduleBean>();
		scheduler.setSuccess(Boolean.TRUE);
		scheduler.setData(scheduleBean);
		return scheduler;
	}
	
	@RequestMapping(value = "/module/admin/quartz/deletefile", method = RequestMethod.POST)
	@ResponseBody
	public FormResponse deletefile(HttpServletRequest oRequest)
	{
		String filePath = StrUtil.toStr(oRequest.getParameter("filePath"), "");
		String isFile = StrUtil.toStr(oRequest.getParameter("isFile"), "");
		filePath = filePath.replace('/',  System.getProperty("file.separator").toCharArray()[0]);
		if(Boolean.parseBoolean(isFile)){
			FileUtil.delFile(System.getProperty("user.home")+ 
					System.getProperty("file.separator")+".kettle"+
					System.getProperty("file.separator")+filePath);
		}else{
			FileUtil.delFolder(System.getProperty("user.home")+ 
					System.getProperty("file.separator")+".kettle"+
					System.getProperty("file.separator")+filePath);
		}
		FormResponse oFormResponse = new FormResponse();
		oFormResponse.setMsg("已成功删除文件");
		oFormResponse.setSuccess(true);
		return oFormResponse;
	}
	
	/**
	 * 转换文件的上传
	 * @param oRequest
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/module/admin/quartz/uploadfile", method = RequestMethod.POST)
	public String uploadFile(HttpServletRequest oRequest) throws IOException
	{
		String filePath = StrUtil.toStr(oRequest.getParameter("filePath"), "");
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) oRequest;
		
		String totalPath = "";
	    for(int i= 0;i< filePath.split("/").length; i++)
	    {
	    	totalPath = totalPath+ System.getProperty("file.separator")+ filePath.split("/")[i];
	    }
        CommonsMultipartFile file = (CommonsMultipartFile) multipartRequest.getFile("file");
        FileItem    fileItem =  file.getFileItem();
        InputStream input;
        OutputStream os = null;
		try {
			input = fileItem.getInputStream();
			File uploadFile = new File(System.getProperty("user.home") + System.getProperty("file.separator")+
					".kettle"+ totalPath+System.getProperty("file.separator")+ file.getOriginalFilename());
			os = new FileOutputStream(uploadFile);
			byte[]  buffer = new byte[10 * 1024];
			int i = 0;
			while((i = input.read(buffer)) != -1)
			{
				os.write(buffer, 0, i);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			os.flush();
			os.close();
		}
		FormResponse oFormResponse = new FormResponse();
		oFormResponse.setMsg("文件上传成功");
		oFormResponse.setSuccess(Boolean.TRUE);
		oRequest.setAttribute("sResponse", oFormResponse);
		return "/admin/common/form_response";
	}
	
	/**
	 * 检测任务的唯一性
	 * @param oRequest
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/module/admin/quartz/checkJobName", method = RequestMethod.POST)
	@ResponseBody
	public FormResponse checkJobName(HttpServletRequest oRequest) throws Exception
	{
		String jobName = StrUtil.toStr(oRequest.getParameter("jobName"), "");
		List<DynaBean> jobList = schedulerService.checkJob(jobName);
		FormResponse oFormResponse = new FormResponse();
		oFormResponse.setMsg("调度任务已存在");
		oFormResponse.setSuccess(jobList.size()>=1?true: false);
		return oFormResponse;
	}
	/**
	 * 获取转换文件的列表tree结构信息
	 * @param oRequest
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping(value = "/module/admin/quartz/getRepTreeJSON", method = RequestMethod.POST)
	@ResponseBody
	public String getRepTreeJSON(HttpServletRequest oRequest) throws UnsupportedEncodingException
	{
		KettleEngineImpl kettleEngine = new KettleEngineImpl();
		String repName = StrUtil.toStr(oRequest.getParameter("repName"), "");
		String repoTreeJson = kettleEngine.getRepTreeJSON(repName,"admin","admin");
		String encoding = StrUtil.getEncoding(repoTreeJson);
		try {
			if(encoding.equals("ISO-8859-1")){
				repoTreeJson = new String(repoTreeJson.getBytes("ISO-8859-1"), "ISO-8859-1");
			}
			if(encoding.equals("GBK")){
				repoTreeJson = new String(repoTreeJson.getBytes("GBK"), "GBK");
			}
			if(encoding.equals("GB2312")){
				repoTreeJson = new String(repoTreeJson.getBytes("GB2312"), "GB2312");
			}
			if(encoding.equals("UTF-8")){
				repoTreeJson = new String(repoTreeJson.getBytes("UTF-8"), "UTF-8");
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return repoTreeJson;
	}

	/**
	 * 添加调度任务
	 * @param oRequest
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/module/admin/quartz/addScheduler", method = RequestMethod.POST)
	public String addScheduler(HttpServletRequest oRequest) throws Exception
	{
		schedulerService.add(this.combineToScheduler(oRequest));
		FormResponse oFormResponse = new FormResponse();
		oFormResponse.setMsg("添加调度任务成功");
		oFormResponse.setSuccess(Boolean.TRUE);
		oRequest.setAttribute("sResponse", oFormResponse);
		return "/admin/common/form_response";
	}
	
	@RequestMapping(value = "/module/admin/quartz/editScheduler", method = RequestMethod.POST)
	public String editScheduler(HttpServletRequest oRequest) throws Exception
	{
		ScheduleBean schedulerBean = this.combineToScheduler(oRequest);
		schedulerService.update(schedulerBean,schedulerBean.getJobName());
		FormResponse oFormResponse = new FormResponse();
		oFormResponse.setMsg("修改调度任务成功");
		oFormResponse.setSuccess(Boolean.TRUE);
		oRequest.setAttribute("sResponse", oFormResponse);
		return "/admin/common/form_response";
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/module/admin/quartz/runSchedulerList", method = RequestMethod.POST)
	@ResponseBody
	public FormResponse runSchedulerList(HttpServletRequest oRequest)
	{
		String jobNameString = StrUtil.toStr(oRequest.getParameter("jobNameArray"), "");
		JsonUtil jsonUtil = new JsonUtil();
        List<String> jobNameList = (List<String>) jsonUtil.decode(jobNameString);
		schedulerService.run(toStringArray(jobNameList));
		FormResponse oFormResponse = new FormResponse();
		return oFormResponse;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/module/admin/quartz/resumeSchedulerList", method = RequestMethod.POST)
	@ResponseBody
	public FormResponse resumeSchedulerList(HttpServletRequest oRequest)
	{
		String jobNameString = StrUtil.toStr(oRequest.getParameter("jobNameArray"), "");
		JsonUtil jsonUtil = new JsonUtil();
        List<String> jobNameList = (List<String>) jsonUtil.decode(jobNameString);
		schedulerService.resume(toStringArray(jobNameList));
		FormResponse oFormResponse = new FormResponse();
		return oFormResponse;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/module/admin/quartz/completedeleteSchedulerList", method = RequestMethod.POST)
	@ResponseBody
	public FormResponse completedeleteSchedulerList(HttpServletRequest oRequest)
	{
		String jobNameString = StrUtil.toStr(oRequest.getParameter("jobNameArray"), "");
		JsonUtil jsonUtil = new JsonUtil();
        List<String> jobNameList = (List<String>) jsonUtil.decode(jobNameString);
		schedulerService.completedelete(toStringArray(jobNameList));
		FormResponse oFormResponse = new FormResponse();
		oFormResponse.setMsg("删除调度任务成功");
		oFormResponse.setSuccess(Boolean.TRUE);
		return oFormResponse;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/module/admin/quartz/pauseSchedulerList", method = RequestMethod.POST)
	@ResponseBody
	public FormResponse pauseSchedulerList(HttpServletRequest oRequest)
	{
		String jobNameString = StrUtil.toStr(oRequest.getParameter("jobNameArray"), "");
		JsonUtil jsonUtil = new JsonUtil();
        List<String> jobNameList = (List<String>) jsonUtil.decode(jobNameString);
		schedulerService.pause(toStringArray(jobNameList));
		FormResponse oFormResponse = new FormResponse();
		return oFormResponse;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/module/admin/quartz/deleteSchedulerList", method = RequestMethod.POST)
	@ResponseBody
	public FormResponse deleteSchedulerList(HttpServletRequest oRequest)
	{
		String jobNameString = StrUtil.toStr(oRequest.getParameter("jobNameArray"), "");
		JsonUtil jsonUtil = new JsonUtil();
        List<String> jobNameList = (List<String>) jsonUtil.decode(jobNameString);
		schedulerService.delete(toStringArray(jobNameList));
		FormResponse oFormResponse = new FormResponse();
		return oFormResponse;
	}
	
	/**
	 * 获取资源库文件列表
	 * @param oRequest
	 * @return
	 */
	@SuppressWarnings({ "rawtypes"})
	@RequestMapping(value = "/module/admin/quartz/getRespositorylist")
	@ResponseBody
	public ComboBox<Repository> getRespositorylist(HttpServletRequest oRequest)
	{
		ComboBox<Repository> comboBox = new ComboBox<Repository>();
		KettleEngineImpl kettleEngine = new KettleEngineImpl();
		List repositoryList = kettleEngine.getReps();
		System.out.println(repositoryList.toString()+"------------");
		Repository[] repositoryArray = new Repository[repositoryList.size()];
		for(int i = 0; i< repositoryList.size(); i++ )
		{
			Repository reposityBean = new Repository();
			reposityBean.setRepositoryId((String)repositoryList.get(i)+"_"+i);
			reposityBean.setRepositoryName((String)repositoryList.get(i));
			repositoryArray[i] = reposityBean;
		}
		comboBox.setArr(repositoryArray);
		return comboBox;
	}
	
	private String[] toStringArray(List<String> sourceList)
	{
		String[] target = new String[sourceList.size()];
		for(int i = 0; i < sourceList.size(); i++)
		{
			target[i] = sourceList.get(i).toString();
		}
		return target;
	}
	
	private ScheduleBean combineToScheduler(HttpServletRequest oRequest) throws Exception{
		ScheduleBean scheduleBean = new ScheduleBean();
		scheduleBean.setJobName(StrUtil.toStr(oRequest.getParameter("jobName"),""));
	    scheduleBean.setJobGroup(StrUtil.toStr(oRequest.getParameter("jobgroup"),"DEFAULT"));
	    scheduleBean.setTriggerName(StrUtil.toStr(oRequest.getParameter("jobName"), "" ));
	    scheduleBean.setTriggerGroup(StrUtil.toStr(oRequest.getParameter("triggergroup"),"DEFAULT"));
	    scheduleBean.setDescription(StrUtil.toStr(oRequest.getParameter("description"),""));
	    String actionPath =StrUtil.toStr(oRequest.getParameter("actionPath"),"");
	    
	    String filePath = "";
	    String fileName = "";
	    for(int i= 0;i< actionPath.split("/").length; i++)
	    {
	    	if(i == actionPath.split("/").length -1){
	    		fileName = actionPath.split("/")[i];
	    		break;
	    	}
	    	filePath = filePath+ System.getProperty("file.separator")+actionPath.split("/")[i];
	    }
	    int index = fileName.indexOf(".");
	    scheduleBean.setActionRef(fileName.substring(0, index));
	    scheduleBean.setActionPath(System.getProperty("user.home")+System.getProperty("file.separator")+".kettle"+filePath);
	    scheduleBean.setFileType(fileName.substring(index + 1,fileName.length()));
	    
	    scheduleBean.setRepName(StrUtil.toStr(oRequest.getParameter("repName"),""));
	    scheduleBean.setUserName(StrUtil.toStr(oRequest.getParameter("userName"),""));
	    scheduleBean.setPassWord(StrUtil.toStr(oRequest.getParameter("passWord"),""));
	    
	    int cycle = Integer.parseInt(StrUtil.toStr(oRequest.getParameter("cycle"), "1"));
	    String endDate = oRequest.getParameter("enddate");
	    String cyclenum = oRequest.getParameter("cyclenum");
	    String haveenddate = oRequest.getParameter("haveenddate");
	    
	    scheduleBean.setCycle(cycle);
	    scheduleBean.setCycleNum(StrUtil.toStr(oRequest.getParameter("cyclenum"),""));
	    scheduleBean.setStartTime(StrUtil.toStr(oRequest.getParameter("startTime"),""));
	    scheduleBean.setHaveEndDate(StrUtil.toStr(oRequest.getParameter("haveenddate"),""));
	    String nextFireTime = oRequest.getParameter("startTime");
	    scheduleBean.setNextFireTime(nextFireTime);
	   
	    Calendar calendar = Calendar.getInstance(); 
	    calendar.setTime(StrUtil.StringToDate(nextFireTime, "yyyy-MM-dd HH:mm:ss"));
	    String cronString="";
	    String weekNum="";
	    String dayNum="";
	    String monthNum="";
	    switch(cycle)
	    {
	    case 1:
	    	scheduleBean.setRepeatCount(0);
	    	long l = (StrUtil.StringToDate(nextFireTime, "yyyy-MM-dd hh:mm:ss").getTime()-new Date().getTime())/1000;
 	        if(l>0L){
	    		scheduleBean.setRepeatInterval(l);
	    	}else{
	    		scheduleBean.setRepeatInterval(-l);
	    	}
	    	break;
	    case 2:
	    	scheduleBean.setRepeatInterval(Long.parseLong(oRequest.getParameter("cyclenum"))* 1000L);
	    	scheduleBean.setRepeatCount(SimpleTrigger.REPEAT_INDEFINITELY);
	    	if("1".equals(haveenddate)){
	    		scheduleBean.setEndDate(endDate);
	    	}
	    	break;
	    case 3:
	    	scheduleBean.setRepeatInterval(Long.parseLong(cyclenum)* 60L * 1000L);
	    	scheduleBean.setRepeatCount(SimpleTrigger.REPEAT_INDEFINITELY);
	    	if("1".equals(haveenddate)){
	    		scheduleBean.setEndDate(endDate);
	    	}
	    	break;
	    case 4:
	    	scheduleBean.setRepeatInterval(Long.parseLong(cyclenum)* 60L* 60L * 1000L);
	    	scheduleBean.setRepeatCount(SimpleTrigger.REPEAT_INDEFINITELY);
	    	if("1".equals(haveenddate)){
	    		scheduleBean.setEndDate(endDate);
	    	}
	    	break;
	    case 5:
	    	String dayType = oRequest.getParameter("daytype");
	    	scheduleBean.setDayType(StrUtil.toStr(dayType,""));
	    	if("0".equals(dayType)){
	    		scheduleBean.setRepeatInterval(Long.parseLong(cyclenum) * 24L * 60L * 60L * 1000L);
	    	}else if("1".equals(dayType)){
	    		cronString = calendar.get(13)+" "+calendar.get(12) + " "+  calendar.get(11) + " ? * MON-FRI";
	    		scheduleBean.setCronString(cronString);
	    	}
	    	scheduleBean.setRepeatCount(SimpleTrigger.REPEAT_INDEFINITELY);
	    	if("1".equals(haveenddate)){
	    		scheduleBean.setEndDate(endDate);
	    	}
	    	break;
	    case 6:
	    	scheduleBean.setRepeatCount(SimpleTrigger.REPEAT_INDEFINITELY);
	    	cronString = calendar.get(13)+" "+calendar.get(12) + " "+  calendar.get(11) + " ? * " + cyclenum;
	    	scheduleBean.setCronString(cronString);
	    	if("1".equals(haveenddate)){
	    		scheduleBean.setEndDate(endDate);
	    	}
	    	break;
	    case 7:
	    	String monthType = oRequest.getParameter("monthtype");
	    	scheduleBean.setMonthType(monthType);
	    	if("0".equals(monthType)){
	    		String monthcyclenum= oRequest.getParameter("monthcyclenum");
	    		cronString = calendar.get(13)+" "+calendar.get(12) + " "+  calendar.get(11) + " " + monthcyclenum + " * ?";
	    	}else if("1".equals(monthType)){
	    		weekNum = oRequest.getParameter("monthweeknum");
	    		dayNum = oRequest.getParameter("monthdaynum");
	    		scheduleBean.setWeekNum(weekNum);
	    		scheduleBean.setDayNum(dayNum);
	    		if(!"L".equals(weekNum)){
	    			weekNum = "#" + weekNum;
	    		}
	    		cronString = calendar.get(13)+" "+calendar.get(12) + " "+  calendar.get(11) + " ? * " + dayNum + weekNum;
	    	}
	    	scheduleBean.setCronString(cronString);
	    	scheduleBean.setRepeatCount(SimpleTrigger.REPEAT_INDEFINITELY);
	    	if("1".equals(haveenddate)){
	    		scheduleBean.setEndDate(endDate);
	    	}
	    	break;
	    case 8:
	    	String yearType = oRequest.getParameter("yeartype");
	    	scheduleBean.setYearType(yearType);
	    	if("0".equals(yearType)){
	    		String[] cycles = cyclenum.split("-");
	    		cronString = calendar.get(13)+" "+calendar.get(12) + " "+  calendar.get(11) +" "+ cycles[1] + " " + cycles[0] + " ?";
	    	}else if("1".equals(yearType)){
	    		weekNum = oRequest.getParameter("yearweeknum");
	    		dayNum = oRequest.getParameter("yeardaynum");
	    		monthNum = oRequest.getParameter("yearmonthnum");
	    		scheduleBean.setMonthNum(monthNum);
	    		scheduleBean.setWeekNum(weekNum);
	    		scheduleBean.setDayNum(dayNum);
	    		if(!"L".equals(weekNum)){
	    			weekNum = "#" + weekNum;
	    		}
	    		cronString = calendar.get(13)+" "+calendar.get(12) + " "+  calendar.get(11) + " ? "+monthNum+" " + dayNum + weekNum;
	    	}
	    	scheduleBean.setCronString(cronString);
	    	scheduleBean.setRepeatCount(SimpleTrigger.REPEAT_INDEFINITELY);
	    	if("1".equals(haveenddate)){
	    		scheduleBean.setEndDate(endDate);
	    	}
	    	break;
	    	
	    }
	    return scheduleBean;
	}
}
