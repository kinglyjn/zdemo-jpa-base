package jpa02.many2one_oneway;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * JPA entity Order
 * @author zhangqingli
 *
 */
@Entity(name = "jpa02_order")
@Table(name = "jpa02_order")
public class Order { //注意这个名字也是数据库的关键字
	private Integer id;
	private String name;
	private Customer customer; //关系属性

	public Order() {
		super();
	}
	public Order(String name) {
		this.name = name;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
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

	/*
	 * 映射n-1关联关系
	 */
	@ManyToOne(fetch=FetchType.LAZY) //默认情况下使用EAGER加载方式
	@JoinColumn(name="customer_id")
	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	@Override
	public String toString() {
		return "Order [id=" + id + ", name=" + name + "]";
	}
}
