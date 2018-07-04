package jpa08.jpql;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * JPA entity Order
 * @author zhangqingli
 *
 */
@Entity(name = "jpa08_order") 
@Table(name = "jpa08_order")
public class Order {
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
	@GeneratedValue
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

	@ManyToOne(fetch=FetchType.LAZY)
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
