package edu.sammoffat.advert.gsons;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Areas {

	@SerializedName("list")
	@Expose
	private List<AreaList> list = null;

	public List<AreaList> getList() {
		return list;
	}

	public void setList(List<AreaList> list) {
		this.list = list;
	}

}