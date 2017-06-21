package srping;

import java.util.ArrayList;
import java.util.List;

public class Bean {
	private String id;
	private String beanPath;
	private List<Property> properties=new ArrayList<>();
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return the beanPath
	 */
	public String getBeanPath() {
		return beanPath;
	}
	/**
	 * @param beanPath the beanPath to set
	 */
	public void setBeanPath(String beanPath) {
		this.beanPath = beanPath;
	}
	/**
	 * @return the properties
	 */
	public List<Property> getProperties() {
		return properties;
	}
	/**
	 * @param properties the properties to set
	 */
	public void setProperties(List<Property> properties) {
		this.properties = properties;
	}
}
