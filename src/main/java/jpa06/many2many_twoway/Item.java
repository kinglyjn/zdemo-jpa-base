package jpa06.many2many_twoway;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

/**
 * jpa entity item
 * 
 * @author zhangqingli
 *
 */
@Entity(name = "jpa06_item")
@Table(name = "jpa06_item")
public class Item {
	private Integer id;
	private String name;
	private Set<Category> categories = new HashSet<>();

	public Item() {
		super();
	}
	public Item(String name) {
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
	 * 映射n-n关联关系（关系维护方）
	 * 
	 *  @JoinTable#name: 映射当前类对应表在中间表的表名
	 *  @JoinTable#joinColumns: 映射当前类对应表在中间表的外键列
	 *  @JoinTable#inverseJoinColumns: 映射关联的类对应表在中间表的外键列
	 *  
	 */
	@ManyToMany
	@JoinTable(name="jpa06_item_category",
		joinColumns={@JoinColumn(name="item_id", referencedColumnName="id")},  
		inverseJoinColumns={@JoinColumn(name="category_id", referencedColumnName="id")}
	)
	public Set<Category> getCategories() {
		return categories;
	}

	public void setCategories(Set<Category> categories) {
		this.categories = categories;
	}

	@Override
	public String toString() {
		return "Item [id=" + id + ", name=" + name + "]";
	}
}
