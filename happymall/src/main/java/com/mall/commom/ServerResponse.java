package com.mall.commom;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;
/**
 * 通用数据端的响应对象
 * @param <T> 需要被封装的实体类
 */
// 当返回信息只有status而没有mes和data时(mes/data为null)，那么mes和data可以不用显示在前端
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class ServerResponse<T> implements Serializable{
	private static final long serialVersionUID = 1L;
	private int status;// 操作结果：0是成功，1是失败，如修改用户密码成功后返回的就是0
	private String mes;// 操作成功或失败后的提示信息
	private T data;// 储存的实体对象，例如登录成功后返回的用户对象
	
	private ServerResponse(int status){
		this.status = status;
	}
	
	private ServerResponse(int status,String mes){
		this.status = status;
		this.mes = mes;
	}
	
	private ServerResponse(int status,T data){
		this.status = status;
		this.data = data;
	}
	
	private ServerResponse(int status,String mes,T data){
		this.status = status;
		this.mes = mes;
		this.data = data;
	}
	@JsonIgnore // 使该字段不会出现在json序列号的结果中
	public boolean isSuccess(){
		return this.status == ResponseCode.SUCCESS.getCode();
	}
	
	//------------操作成功的返回----------------
	/**
	 * 返回成功操作后的status：就是0
	 * @return
	 */
	public static <T> ServerResponse<T> createBySuccess(){
		return new ServerResponse<T>(ResponseCode.SUCCESS.getCode());
	}
	
	/**
	 * 返回操作成功后的status和成功信息
	 * @param mes 返回一个文本供前端使用
	 * @return
	 */
	public static <T> ServerResponse<T> createBySuccessMessage(String mes){
		return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(),mes);
	}
	
	/**
	 * 返回status和携带的数据
	 * @param data 返回的数据
	 * @return
	 */
	public static <T> ServerResponse<T> createBySuccess(T data){
		return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(),data);
	}
	
	/**
	 * 返回status成功信息和携带的数据
	 * @param mes 返回一个文本供前端使用
	 * @param data 返回的数据
	 * @return
	 */
	public static <T> ServerResponse<T> createBySuccess(String mes,T data){
		return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(),mes,data);
	}
	
	
	//------------操作失败的返回----------------
	/**
	 * 操作失败后返回status
	 * @return
	 */
	public static <T> ServerResponse<T> createByError(){
		return new ServerResponse<T>(ResponseCode.ERROR.getCode(),ResponseCode.ERROR.getDesc());
	}
	
	/**
	 * 操作失败后返回的status和提示信息
	 * @param errorMes 失败的提示信息
	 * @return
	 */
	public static <T> ServerResponse<T> createByErrorMessage(String errorMes){
		return new ServerResponse<T>(ResponseCode.ERROR.getCode(),errorMes);
	}
	
	/**
	 * 操作失败后对应的status和对应的提示信息
	 * @param status 不同错误返回的不同status
	 * @param errorMes 不同失败的提示信息
	 * @return
	 */
	public static <T> ServerResponse<T> createByErrorMessage(int status,String errorMes){
		return new ServerResponse<T>(status,errorMes);
	}
	public String getMes() {
		return mes;
	}
	public T getData() {
		return data;
	}
	public int getStatus() {
		return status;
	}
	
}
