package jpa01.hello;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 * JPA实体类
 * 建议将映射注解加在get方法上，便于在get方法中控制和操作属性
 * @author zhangqingli
 *
 */
@Entity(name="jpa01_customer")
@Table(name = "jpa01_customer")
public class Customer {
	private Integer id;
	private String name;
	private String email;
	private int age;
	private Date createTime;
	private Date birthday;

	public Customer() {
		super();
	}
	public Customer(String name, String email, int age, Date createTime,
			Date birthday) {
		super();
		this.name = name;
		this.email = email;
		this.age = age;
		this.createTime = createTime;
		this.birthday = birthday;
	}
	
	@Transient //映射不需要持久化的字段或get方法
	public String getInfo() {
		return "this is customer info";
	}


	/*
	 * @TableGenerator映射的生成主键的表 
	 * ------------------------------- 
	 * PK_NAME		ID_VAL
	 * CUSTOMER_ID 	1 
	 * ORDER_ID 	3 
	 * -------------------------------
	 */
	/*
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "ID_GENERATOR")
	@TableGenerator(
		name = "ID_GENERATOR", 
		table = "JPA_ID_GENERATOR", 
		pkColumnName = "PK_NAME", 
		pkColumnValue = "CUSTOMER_ID", 
		valueColumnName = "ID_VAL", 
		initialValue = 1, 
		allocationSize = 1)
	*/
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	@Temporal(TemporalType.TIMESTAMP) //映射时间类型
	@Column(name="create_time")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Temporal(TemporalType.DATE)
	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	
	@Override
	public String toString() {
		return "Customer [id=" + id + ", name=" + name + ", email=" + email
				+ ", age=" + age + "]";
	}
}
