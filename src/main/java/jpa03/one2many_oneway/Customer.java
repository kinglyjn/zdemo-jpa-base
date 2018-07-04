package jpa03.one2many_oneway;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * JPA entity Customer
 * @author zhangqingli
 *
 */
@Entity(name="jpa03_customer")
@Table(name="jpa03_customer")
public class Customer {
	private Integer id;
	private String name;
	private Integer age;
	private Set<Order> orders = new HashSet<Order>();
	
	
	public Customer() {
		super();
	}
	public Customer(String name, Integer age) {
		super();
		this.name = name;
		this.age = age;
	}

	
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

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}
	
	/*
	 * 映射1-n关联关系
	 */
	@OneToMany(fetch=FetchType.LAZY, cascade={CascadeType.REMOVE})
	@JoinColumn(name="custom_id")
	public Set<Order> getOrders() {
		return orders;
	}
	public void setOrders(Set<Order> orders) {
		this.orders = orders;
	}
	
	
	@Override
	public String toString() {
		return "Customer [id=" + id + ", name=" + name + ", age=" + age + "]";
	}
}
