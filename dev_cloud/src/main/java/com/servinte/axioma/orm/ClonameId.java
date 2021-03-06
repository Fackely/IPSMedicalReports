package com.servinte.axioma.orm;

// Generated Apr 7, 2010 3:58:52 PM by Hibernate Tools 3.2.5.Beta

/**
 * ClonameId generated by hbm2java
 */
public class ClonameId implements java.io.Serializable {

	private Integer pk;
	private Integer a;
	private Integer b;

	public ClonameId() {
	}

	public ClonameId(Integer pk, Integer a, Integer b) {
		this.pk = pk;
		this.a = a;
		this.b = b;
	}

	public Integer getPk() {
		return this.pk;
	}

	public void setPk(Integer pk) {
		this.pk = pk;
	}

	public Integer getA() {
		return this.a;
	}

	public void setA(Integer a) {
		this.a = a;
	}

	public Integer getB() {
		return this.b;
	}

	public void setB(Integer b) {
		this.b = b;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof ClonameId))
			return false;
		ClonameId castOther = (ClonameId) other;

		return ((this.getPk() == castOther.getPk()) || (this.getPk() != null
				&& castOther.getPk() != null && this.getPk().equals(
				castOther.getPk())))
				&& ((this.getA() == castOther.getA()) || (this.getA() != null
						&& castOther.getA() != null && this.getA().equals(
						castOther.getA())))
				&& ((this.getB() == castOther.getB()) || (this.getB() != null
						&& castOther.getB() != null && this.getB().equals(
						castOther.getB())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + (getPk() == null ? 0 : this.getPk().hashCode());
		result = 37 * result + (getA() == null ? 0 : this.getA().hashCode());
		result = 37 * result + (getB() == null ? 0 : this.getB().hashCode());
		return result;
	}

}
