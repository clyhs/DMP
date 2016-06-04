package org.dmp.module.admin.quartz.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.dmp.core.util.StrUtil;
import org.dmp.module.admin.quartz.service.TaskService;
import org.dmp.module.common.form.FormResponse;
import org.dmp.module.common.grid.Grid;
import org.dmp.pojo.admin.quartz.Task;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class TaskController {

	@Resource(name = "TaskService")
	private TaskService taskService;
	
	/**
	 * 入口(Task)
	 * @param oRequest
	 * @return
	 */
	@RequestMapping(value = "/module/admin/quartz/taskindex")
	public String taskIndex(HttpServletRequest oRequest)
	{
		return "/admin/quartz/taskindex";
	}
	
	/**
	 * 获取列表信息
	 * @param oRequest
	 * @return
	 */
	@RequestMapping(value = "/module/admin/quartz/getTaskList", method = RequestMethod.POST)
	@ResponseBody
	public Grid<Task> getTaskList(HttpServletRequest oRequest){
		String name = StrUtil.toQueryStr(oRequest.getParameter("name"), "%");
		int start = StrUtil.toInt(oRequest.getParameter("start"), 0); 
		int limit = StrUtil.toInt(oRequest.getParameter("limit"), 100);
		Grid<Task> grid = new Grid<Task>();
		List<Task> taskList = taskService.getTaskList(name, start, limit);
		long total =  taskService.getTaskList(name, 0, 0).size();
		grid.setData(taskList);
		grid.setTotal(total);
		return grid;
	}
	
    /**
     * 增加
     * @param oRequest
     * @return
     * @throws Exception
     */
	@RequestMapping(value = "/module/admin/quartz/addTask", method = RequestMethod.POST)
	public String addTask(HttpServletRequest oRequest) throws Exception
	{
		String taskCode = StrUtil.toStr(oRequest.getParameter("taskCode"), "");
		String name = StrUtil.toStr(oRequest.getParameter("name"), "");
		String beginTime = StrUtil.toStr(oRequest.getParameter("TaskBeginTime"), "");
		String endTime = StrUtil.toStr(oRequest.getParameter("TaskEndTime"), "");
		Task task = taskService.getTask(taskCode);
		//用Freemaker呈现结果
	    FormResponse oFormResponse = new FormResponse();
		if(task == null)
		{
			int nTotal = taskService.addTask(taskCode,name,beginTime,endTime);
			oFormResponse.setSuccess(nTotal > 0 ? Boolean.TRUE : Boolean.FALSE);
			oFormResponse.setMsg(nTotal > 0 ? "增加数据成功" : "增加数据失败");			
		}else
		{
			oFormResponse.setSuccess(Boolean.FALSE);
			oFormResponse.setMsg("此任务编码已存在，请重新输入");
		}
		oRequest.setAttribute("sResponse", oFormResponse);
		return "/admin/common/form_response";
	}
	
	/**
	 * 修改
	 * @param oRequest
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/module/admin/quartz/editTask", method = RequestMethod.POST)
	public String editTask(HttpServletRequest oRequest) throws Exception
	{
		String taskCode = StrUtil.toStr(oRequest.getParameter("taskCode"), "");
		String name = StrUtil.toStr(oRequest.getParameter("name"), "");
		String beginTime = StrUtil.toStr(oRequest.getParameter("TaskBeginTime"), "");
		String endTime = StrUtil.toStr(oRequest.getParameter("TaskEndTime"), "");
		int nTotal = taskService.editTask(taskCode,name,beginTime,endTime);
		//用Freemaker呈现结果
		FormResponse oFormResponse = new FormResponse();
		oFormResponse.setSuccess(nTotal > 0 ? Boolean.TRUE : Boolean.FALSE);
		oFormResponse.setMsg(nTotal > 0 ? "修改数据成功" : "修改数据失败");
		oRequest.setAttribute("sResponse", oFormResponse);
		return "/admin/common/form_response";
	}	
	
	/**
	 * 删除
	 * @param sTaskIdList
	 * @return
	 */
	@RequestMapping(value = "/module/admin/quartz/delTask", method = RequestMethod.POST)
	@ResponseBody
	public FormResponse delTask(String sTaskIdList)
	{
		int nTotal = taskService.delTask(sTaskIdList);
		FormResponse oFormResponse = new FormResponse();
		oFormResponse.setSuccess(nTotal > 0 ? Boolean.TRUE : Boolean.FALSE);
		oFormResponse.setMsg(nTotal > 0 ? "删除成功" : "删除失败");
		return oFormResponse;
	}
	
}
