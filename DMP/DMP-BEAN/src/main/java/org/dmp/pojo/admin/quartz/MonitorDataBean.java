package org.dmp.pojo.admin.quartz;

import java.util.Date;

public class MonitorDataBean {

	private Date depDate;
	
	private Date endDate;
	
	private int errors;
	
	private int id_batch;
	
	private int lines_input;
	
	private int lines_output;
	
	private int lines_read;
	
	private int lines_written;
	
	private int lines_update;
	
	private Date logDate;
	
	private Date replayDate;
	
	private Date startDate;
	
	private String status;
	
	private String transname;

	public Date getDepDate() {
		return depDate;
	}

	public void setDepDate(Date depDate) {
		this.depDate = depDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public int getErrors() {
		return errors;
	}

	public void setErrors(int errors) {
		this.errors = errors;
	}

	public int getId_batch() {
		return id_batch;
	}

	public void setId_batch(int id_batch) {
		this.id_batch = id_batch;
	}

	public int getLines_input() {
		return lines_input;
	}

	public void setLines_input(int lines_input) {
		this.lines_input = lines_input;
	}

	public int getLines_output() {
		return lines_output;
	}

	public void setLines_output(int lines_output) {
		this.lines_output = lines_output;
	}

	public int getLines_read() {
		return lines_read;
	}

	public void setLines_read(int lines_read) {
		this.lines_read = lines_read;
	}

	public int getLines_written() {
		return lines_written;
	}

	public void setLines_written(int lines_written) {
		this.lines_written = lines_written;
	}

	public Date getLogDate() {
		return logDate;
	}

	public void setLogDate(Date logDate) {
		this.logDate = logDate;
	}

	public Date getReplayDate() {
		return replayDate;
	}

	public void setReplayDate(Date replayDate) {
		this.replayDate = replayDate;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTransname() {
		return transname;
	}

	public void setTransname(String transname) {
		this.transname = transname;
	}

	public int getLines_update() {
		return lines_update;
	}

	public void setLines_update(int lines_update) {
		this.lines_update = lines_update;
	}
	
	
	
}
