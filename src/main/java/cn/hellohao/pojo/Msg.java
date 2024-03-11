package cn.hellohao.pojo;

import java.io.Serializable;

/**
 * 交互消息,默认成功
 * @author Hellohao
 *
 */
public class Msg implements Serializable {

	private static final long serialVersionUID = 5196249482551119279L;

	private String code ="200";
	
	private String info="操作成功";
	
	private Object data;
	
	private String exceptions;

	
	public Msg() {}

	public void set(String code, String info){
		this.code = code;
		this.info = info;
	}
	
	/**
	 * 默认成功操作
	 * @param data
	 */
	public Msg(Object data) {
		super();
		this.data = data;
	}

	public Msg(String code, String info) {
		super();
		this.code = code;
		this.info = info;
	}


	public Msg(String code, String info, Object data) {
		super();
		this.code = code;
		this.info = info;
		this.data = data;
	}


	public Msg(String code, String info, Object data, String exceptions) {
		super();
		this.code = code;
		this.info = info;
		this.data = data;
		this.exceptions = exceptions;
	}


	public String getCode() {
		return code;
	}


	public void setCode(String code) {
		this.code = code;
	}


	public String getInfo() {
		return info;
	}


	public void setInfo(String info) {
		this.info = info;
	}


	public Object getData() {
		return data;
	}


	public void setData(Object data) {
		this.data = data;
	}


	public String getExceptions() {
		return exceptions;
	}


	public void setExceptions(String exceptions) {
		this.exceptions = exceptions;
	}

	@Override
	public String toString() {
		return "Msg [code=" + code + ", info=" + info + ", data=" + data
				+ ", exceptions=" + exceptions + "]";
	}
	
}
