package test;

import srping.BeanFactory;
import srping.ClassPathXmlApplicationContext;

public class SpringTest {
	public static void main(String[] args) {
		BeanFactory factory=new ClassPathXmlApplicationContext("ApplicationContext.xml");
		Dog dog=(Dog)factory.getBean("mydog");
		System.out.println("dog----age:"+dog.getAge());
		System.out.println("dog----price:"+dog.getPrice());
		System.out.println("cat----name:"+dog.getCat().getName());
		System.out.println("cat----age:"+dog.getCat().getAge());
		System.out.println("cat----addr:"+dog.getCat().getAddr());
		System.out.println("pig----name"+dog.getCat().getPig().getName());
		System.out.println("pig----age"+dog.getCat().getPig().getAge());
	}
}
