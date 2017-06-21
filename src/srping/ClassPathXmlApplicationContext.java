package srping;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class ClassPathXmlApplicationContext implements BeanFactory {
	private String configPath;
	//存放bean实例
	private static Map<String, Object> beanContext=new HashMap<>();
	private Map<String, Bean> beanConfig=new HashMap<>();
	@Override
	public Object getBean(String id) {
		Object target=beanContext.get(id);
		if(target==null)
			target=createBean(beanConfig.get(id));
		return target;
	}
	public ClassPathXmlApplicationContext(String configPath){
		readConfig(configPath);
//		for(Entry<String,Bean> entry:beanConfig.entrySet()){
//			Bean bean=entry.getValue();
//			beanContext.put(bean.getId(), createBean(bean));
//		}
	}
	/**
	 * 根据bean的属性使用反射创建对象
	 * @param bean
	 * @return
	 */
	public Object createBean(Bean bean){
		Object ob=null;
		Class cl=null;
		try {
			cl=Class.forName(bean.getBeanPath());
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("配置文件里id为"+bean.getId()+"的对象路径出错");
		}
		try {
			ob=cl.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			throw new RuntimeException("配置文件里id为"+bean.getId()+"的对象实例化出错");
		}
		Method[] mList=cl.getMethods();
		for(Property pro:bean.getProperties()){
			String name=pro.getName();
			String value=pro.getValue();
			String setM="set"+name.substring(0,1).toUpperCase()+name.substring(1);
			if(pro.getValue()!=null){
				for(Method m:mList){
					if(m.getName().equals(setM)){
						Class[] pars=m.getParameterTypes();
						try {
							if(pars[0].getName().contains("int")||pars[0].getName().contains("Integer")){
								m.invoke(ob, Integer.parseInt(value));
								break;
							}
							else if(pars[0].getName().contains("double")||pars[0].getName().contains("Double")){
								m.invoke(ob, Double.parseDouble(value));
								break;
							}
							else if(pars[0].getName().contains("long")||pars[0].getName().contains("Long")){
								m.invoke(ob, Long.parseLong(value));
								break;
							}
							else if(pars[0].getName().contains("short")||pars[0].getName().contains("Short")){
								System.out.println(pars[0].getName());
								m.invoke(ob, Short.parseShort(value));
								break;
							}
							m.invoke(ob, value);
						} catch (Exception e) {
							throw new RuntimeException("set出错");
						}
					}
				}
			}
			else if(pro.getRef()!=null){
				if(beanContext.get(pro.getRef())!=null){
					Bean refBean=beanConfig.get(pro.getRef());
					Class refClass;
					try {
						refClass = Class.forName(refBean.getBeanPath());
						Method refMethod=cl.getMethod(setM, refClass);
						refMethod.invoke(ob, beanContext.get(pro.getRef()));
					}
					catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else{
					beanContext.put(pro.getRef(), createBean(beanConfig.get(pro.getRef())));
					Bean refBean=beanConfig.get(pro.getRef());
					Class refClass;
					try {
						refClass = Class.forName(refBean.getBeanPath());
						Method refMethod=cl.getMethod(setM, refClass);
						refMethod.invoke(ob, beanContext.get(pro.getRef()));
					}
					catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		mList=null;
		return ob;
	}
	/**
	 * 读入配置文件的bean属性等信息
	 * @param configPath
	 */
	public void readConfig(String configPath){
		SAXReader saxReader=new SAXReader();
		Document document=null;
		try {
			document=saxReader.read(ClassPathXmlApplicationContext.class.getResourceAsStream(configPath));
		} catch (DocumentException e) {
			System.out.println("配置文件路径可能出错");
			throw new RuntimeException("配置文件路径可能出错");
		}
		Element root=document.getRootElement();
		if(!root.getName().equals("beans")){
			System.out.println("配置文件出错");
			throw new RuntimeException("配置文件出错");
		}
		List<Element> beansCon=root.elements();
		for(Element beanCon:beansCon){
			Bean bean=new Bean();
			String id=beanCon.attributeValue("id");
			String beanPath=beanCon.attributeValue("class");
			bean.setId(id);
			bean.setBeanPath(beanPath);
			List<Element> prosCon=beanCon.elements();
			for(Element proCon:prosCon){
				Property pro=new Property();
				String name=proCon.attributeValue("name");
				String value=proCon.attributeValue("value");
				String ref=proCon.attributeValue("ref");
				pro.setName(name);
				pro.setValue(value);
				pro.setRef(ref);
				bean.getProperties().add(pro);
			}
			beanConfig.put(id, bean);
		}
	}
}
