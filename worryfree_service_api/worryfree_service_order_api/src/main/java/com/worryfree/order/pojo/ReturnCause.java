package com.worryfree.order.pojo;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * returnCause实体类
 * @author XM_Dong
 *
 */
@Table(name="tb_return_cause")
@Data
public class ReturnCause implements Serializable {

	@Id
	private Integer id;//ID


	
	private String cause;//原因
	private Integer seq;//排序
	private String status;//是否启用




}
