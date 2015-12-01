/**
 * 
 */
package com.servinte.axioma.dto.consultaExterna;

import java.util.Date;

/**
 * @author jeilones
 * @created 23/10/2012
 *
 */
public class CitaDto {

	private int codigo;
	
	private Date fecha;
	
	private String hora;
	
	private DtoUnidadesConsulta unidadConsulta;
	
	/**
	 * 
	 * @author jeilones
	 * @created 23/10/2012
	 */
	public CitaDto() {
		
	}

	public CitaDto(int codigo,String descripcionUnidadConsulta,Date fecha,String hora){
		this.codigo=codigo;
		this.unidadConsulta=new DtoUnidadesConsulta();
		this.unidadConsulta.setDescripcion(descripcionUnidadConsulta);
		this.fecha=fecha;
		this.hora=hora;
	}
	
	/**
	 * @return the codigo
	 */
	public int getCodigo() {
		return codigo;
	}

	/**
	 * @param codigo the codigo to set
	 */
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	/**
	 * @return the unidadConsulta
	 */
	public DtoUnidadesConsulta getUnidadConsulta() {
		return unidadConsulta;
	}

	/**
	 * @param unidadConsulta the unidadConsulta to set
	 */
	public void setUnidadConsulta(DtoUnidadesConsulta unidadConsulta) {
		this.unidadConsulta = unidadConsulta;
	}

	/**
	 * @return the fecha
	 */
	public Date getFecha() {
		return fecha;
	}

	/**
	 * @param fecha the fecha to set
	 */
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	/**
	 * @return the hora
	 */
	public String getHora() {
		return hora;
	}

	/**
	 * @param hora the hora to set
	 */
	public void setHora(String hora) {
		this.hora = hora;
	}

}
