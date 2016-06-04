package org.dmp.pojo.admin.reportmgr;

import java.io.Serializable;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity(name="ReportButton")
@Table(name = "TD_S_REPORT_BUTTON")
@Cacheable
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class ReportButton implements Serializable{

	private static final long serialVersionUID = 4270611487333387750L;

	@Id
	@Column(name = "N_ID")
	private String id;
	@Column(name = "S_BUTTON")
	private String button;
	@Column(name = "S_BUTTON_NAME")
	private String buttonName;
	@Column(name = "N_LAYOUT")
	private long layout;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getButton() {
		return button;
	}
	public void setButton(String button) {
		this.button = button;
	}
	public String getButtonName() {
		return buttonName;
	}
	public void setButtonName(String buttonName) {
		this.buttonName = buttonName;
	}
	public long getLayout() {
		return layout;
	}
	public void setLayout(long layout) {
		this.layout = layout;
	}

}
