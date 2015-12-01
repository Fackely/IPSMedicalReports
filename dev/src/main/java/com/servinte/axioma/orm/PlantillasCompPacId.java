package com.servinte.axioma.orm;

// Generated Apr 7, 2010 3:58:52 PM by Hibernate Tools 3.2.5.Beta

/**
 * PlantillasCompPacId generated by hbm2java
 */
public class PlantillasCompPacId implements java.io.Serializable {

	private long plantillaPaciente;
	private int plantillaComponente;
	private long componenteIngreso;

	public PlantillasCompPacId() {
	}

	public PlantillasCompPacId(long plantillaPaciente, int plantillaComponente,
			long componenteIngreso) {
		this.plantillaPaciente = plantillaPaciente;
		this.plantillaComponente = plantillaComponente;
		this.componenteIngreso = componenteIngreso;
	}

	public long getPlantillaPaciente() {
		return this.plantillaPaciente;
	}

	public void setPlantillaPaciente(long plantillaPaciente) {
		this.plantillaPaciente = plantillaPaciente;
	}

	public int getPlantillaComponente() {
		return this.plantillaComponente;
	}

	public void setPlantillaComponente(int plantillaComponente) {
		this.plantillaComponente = plantillaComponente;
	}

	public long getComponenteIngreso() {
		return this.componenteIngreso;
	}

	public void setComponenteIngreso(long componenteIngreso) {
		this.componenteIngreso = componenteIngreso;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof PlantillasCompPacId))
			return false;
		PlantillasCompPacId castOther = (PlantillasCompPacId) other;

		return (this.getPlantillaPaciente() == castOther.getPlantillaPaciente())
				&& (this.getPlantillaComponente() == castOther
						.getPlantillaComponente())
				&& (this.getComponenteIngreso() == castOther
						.getComponenteIngreso());
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + (int) this.getPlantillaPaciente();
		result = 37 * result + this.getPlantillaComponente();
		result = 37 * result + (int) this.getComponenteIngreso();
		return result;
	}

}
