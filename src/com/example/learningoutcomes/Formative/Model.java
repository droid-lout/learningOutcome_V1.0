package com.example.learningoutcomes.Formative;

public class Model {

	private String name;
	private boolean selected;

	public Model(String name) {
		this.name = name;
		this.selected = false;
	}

	@Override
	public boolean equals(Object o) {
		// TODO Auto-generated method stub
		if (!(o instanceof Model))
			return false;
		Model temp = (Model) o;
		String t = temp.name;
		return t.equals(name);
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		int hash = name.hashCode();
		return hash;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

}
