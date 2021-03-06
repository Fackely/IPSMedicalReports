package com.servinte.axioma.orm;

// Generated Apr 7, 2010 3:58:52 PM by Hibernate Tools 3.2.5.Beta

/**
 * PoolesEsquemaTarifarioId generated by hbm2java
 */
public class PoolesEsquemaTarifarioId implements java.io.Serializable {

	private int pool;
	private int esquemaTarifario;

	public PoolesEsquemaTarifarioId() {
	}

	public PoolesEsquemaTarifarioId(int pool, int esquemaTarifario) {
		this.pool = pool;
		this.esquemaTarifario = esquemaTarifario;
	}

	public int getPool() {
		return this.pool;
	}

	public void setPool(int pool) {
		this.pool = pool;
	}

	public int getEsquemaTarifario() {
		return this.esquemaTarifario;
	}

	public void setEsquemaTarifario(int esquemaTarifario) {
		this.esquemaTarifario = esquemaTarifario;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof PoolesEsquemaTarifarioId))
			return false;
		PoolesEsquemaTarifarioId castOther = (PoolesEsquemaTarifarioId) other;

		return (this.getPool() == castOther.getPool())
				&& (this.getEsquemaTarifario() == castOther
						.getEsquemaTarifario());
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getPool();
		result = 37 * result + this.getEsquemaTarifario();
		return result;
	}

}
