package test;

import java.util.Date;

public class Dog {
	private int age;
	private double price;
	private Cat cat;
	/**
	 * @return the cat
	 */
	public Cat getCat() {
		return cat;
	}
	/**
	 * @param cat the cat to set
	 */
	public void setCat(Cat cat) {
		this.cat = cat;
	}
	/**
	 * @return the age
	 */
	public int getAge() {
		return age;
	}
	/**
	 * @param age the age to set
	 */
	public void setAge(int age) {
		this.age = age;
	}
	/**
	 * @return the price
	 */
	public double getPrice() {
		return price;
	}
	/**
	 * @param price the price to set
	 */
	public void setPrice(double price) {
		this.price = price;
	}
}
