package jpa06.many2many_twoway;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

/**
 * jpa entity category
 * 
 * @author zhangqingli
 *
 */
@Entity(name = "jpa06_category")
@Table(name = "jpa06_category")
public class Category {
	private Integer id;
	private String name;
	private Set<Item> items = new HashSet<>(); //关系属性

	public Category() {
		super();
	}
	public Category(String name) {
		super();
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

	/*
	 * 映射n-n关联关系（关系被维护方）
	 * 需要有一端放弃关联关系的维护，否则关联关系会被维护两次（可能会有两张第三张表）
	 * 
	 */
	@ManyToMany(mappedBy="categories")
	public Set<Item> getItems() {
		return items;
	}

	public void setItems(Set<Item> items) {
		this.items = items;
	}

	@Override
	public String toString() {
		return "Category [id=" + id + ", name=" + name + "]";
	}
}
