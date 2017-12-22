package org.geowe.datastore.layer;

import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;

public class Layer {

	@Id
	@NotEmpty
	private String id;
	
	@NotEmpty
	private String name;
	
	private String description;
	
	@NotNull
	private DataFormat format;
	
	private String projection;
	
	private LocalDateTime lastUpdate;
	
	private boolean openData;
	
	private String data;
	
	public Layer() {
		super();
	}

	public Layer(String name, DataFormat format, String projection) {
		super();
		this.name = name;
		this.format = format;
		this.projection = projection;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public DataFormat getFormat() {
		return format;
	}

	public void setFormat(DataFormat format) {
		this.format = format;
	}

	public String getProjection() {
		return projection;
	}

	public void setProjection(String projection) {
		this.projection = projection;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public LocalDateTime getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(LocalDateTime lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
	
	public boolean isOpenData() {
		return openData;
	}

	public void setOpenData(boolean openData) {
		this.openData = openData;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Layer other = (Layer) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Layer [id=" + id + ", name=" + name + ", description=" + description + ", format=" + format
				+ ", projection=" + projection + ", lastUpdate=" + lastUpdate + ", openData=" + openData + "]";
	}

}
