package jpa04.many2one_twoway;

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
//注意在一个持久化单元中entity_name必须唯一（默认值为非全限类名）
@Entity(name = "jpa04_order") 
@Table(name = "jpa04_order")
public class Order {
	private Integer id;
	private String name;
	private Customer customer;

	
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
	 *【注意一般情况下映射双向n-1关联关系时两边的joinColumn_name必须一致】
	 * 
	 */
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="custom_id") 
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
