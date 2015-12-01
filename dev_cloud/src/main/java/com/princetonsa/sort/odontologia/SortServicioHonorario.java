package com.princetonsa.sort.odontologia;

import java.util.Comparator;

import com.princetonsa.dto.odontologia.DtoServicioHonorarios;

public class SortServicioHonorario implements Comparator<DtoServicioHonorarios> {

	/**
	 * 
	 */
	private String patronOrdenar;

	/**
	 * 
	 * @param patronOrden
	 */
	public SortServicioHonorario(String patronOrdenar) {
		this.patronOrdenar = patronOrdenar;
	}

	public int compare(DtoServicioHonorarios one, DtoServicioHonorarios two) {
		if (this.getPatronOrdenar().equals("codigo")) {
			return new Double(one.getCodigo()).compareTo(new Double(two
					.getCodigo()));
		}
		if (this.getPatronOrdenar().equals("esquema_tarifario")) {
			return new Integer(one.getEsquemaTarifario().getCodigo())
					.compareTo(new Integer(two.getEsquemaTarifario()
							.getCodigo()));
		}
		if (this.getPatronOrdenar().equals("nombre_esquema_tarifario")) {
			return one.getEsquemaTarifario().getNombre().toUpperCase().compareTo(
					two.getEsquemaTarifario().getNombre().toUpperCase());
		}
		if (this.getPatronOrdenar().equals("nombre_centro_atencion")) {
			return one.getCentroAtencion().getNombre().toUpperCase().compareTo(
					two.getCentroAtencion().getNombre().toUpperCase());
		}
		if (this.getPatronOrdenar().equals("institucion")) {
			return new Integer(one.getInstitucion()).compareTo(new Integer(two
					.getInstitucion()));
		}
		if (this.getPatronOrdenar().equals("fecha_modifica")) {
			return one.getFechaModifica().compareTo(two.getFechaModifica());
		}

		if (this.getPatronOrdenar().equals("hora_modifica")) {
			return one.getHoraModifica().compareTo(two.getHoraModifica());
		}

		if (this.getPatronOrdenar().equals("usuario_modifica")) {
			return one.getUsuarioModifica().compareTo(two.getUsuarioModifica());
		}

		if (this.getPatronOrdenar().equals("codigo_descendente")) {
			return new Double(two.getCodigo()).compareTo(new Double(one
					.getCodigo()));
		}
		if (this.getPatronOrdenar().equals("esquema_tarifario_descendente")) {
			return new Integer(two.getEsquemaTarifario().getCodigo())
					.compareTo(new Integer(one.getEsquemaTarifario()
							.getCodigo()));
		}
		if (this.getPatronOrdenar().equals(
				"nombre_esquema_tarifario_descendente")) {
			return two.getEsquemaTarifario().getNombre().toUpperCase().compareTo(
					one.getEsquemaTarifario().getNombre().toUpperCase());
		}
		if (this.getPatronOrdenar().equals(
				"nombre_centro_atencion_descendente")) {
			return two.getCentroAtencion().getNombre().toUpperCase().compareTo(
					one.getCentroAtencion().getNombre().toUpperCase());
		}
		if (this.getPatronOrdenar().equals("institucion_descendente")) {
			return new Integer(two.getInstitucion()).compareTo(new Integer(one
					.getInstitucion()));
		}
		if (this.getPatronOrdenar().equals("fecha_modifica_descendente")) {
			return two.getFechaModifica().compareTo(one.getFechaModifica());
		}

		if (this.getPatronOrdenar().equals("hora_modifica_descendente")) {
			return two.getHoraModifica().compareTo(one.getHoraModifica());
		}

		if (this.getPatronOrdenar().equals("usuario_modifica_descendente")) {
			return two.getUsuarioModifica().compareTo(one.getUsuarioModifica());
		}

		else {
			return new Double(one.getCodigo()).compareTo(new Double(two
					.getCodigo()));
		}
	}

	/**
	 * @return the patronOrdenar
	 */
	public String getPatronOrdenar() {
		return this.patronOrdenar;
	}

	/**
	 * @param patronOrdenar
	 *            the patronOrdenar to set
	 */
	public void setPatronOrdenar(String patronOrdenar) {
		this.patronOrdenar = patronOrdenar;
	}

}
