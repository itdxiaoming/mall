package com.worryfree.config.pojo;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * freightTemplate实体类
 * @author XM_Dong
 *
 */
@Table(name="tb_freight_template")
@Data
public class FreightTemplate implements Serializable {

	@Id
	private Integer id;//ID


	
	private String name;//模板名称
	private String type;//计费方式

	


}
