package com.princetonsa.dto.consultaExterna;

import java.io.Serializable;

import util.ConstantesBD;
import util.UtilidadFecha;

import com.princetonsa.dto.odontologia.DtoAgendaOdontologica;

@SuppressWarnings("serial")
public class DtoExcepcionesHorarioAtencion extends DtoAgendaOdontologica implements Serializable{

	private int codigoPk; 
	private String Observaciones;
	private int institucion;
	private String fechaModifica;
	private String horaModifica;
	private String usuarioModifica;
	private String eliminado;
	private String fechaEliminacion;
	private String horaEliminacion;
	private String usuarioEliminacion;
	private String esGenerar;
	
	public DtoExcepcionesHorarioAtencion(){
		super();
		this.clean();
		
 	}
	
	private void clean(){
		this.codigoPk=0; 
		this.Observaciones="";
		this.institucion=0;
		this.eliminado="";
		this.fechaEliminacion="";
		this.horaEliminacion="";
		this.usuarioEliminacion="";
		this.esGenerar=ConstantesBD.acronimoNo;
	}
	/**
	 * @return the codigoPk
	 */
	public int getCodigoPk() {
		return codigoPk;
	}
	/**
	 * @param codigoPk the codigoPk to set
	 */
	public void setCodigoPk(int codigoPk) {
		this.codigoPk = codigoPk;
	}
	/**
	 * @return the observaciones
	 */
	public String getObservaciones() {
		return Observaciones;
	}
	/**
	 * @param observaciones the observaciones to set
	 */
	public void setObservaciones(String observaciones) {
		Observaciones = observaciones;
	}
	/**
	 * @return the institucion
	 */
	public int getInstitucion() {
		return institucion;
	}
	/**
	 * @param institucion the institucion to set
	 */
	public void setInstitucion(int institucion) {
		this.institucion = institucion;
	}

	/**
	 * @return the fechaEliminacion
	 */
	public String getFechaEliminacion() {
		return fechaEliminacion;
	}
	/**
	 * @param fechaEliminacion the fechaEliminacion to set
	 */
	public void setFechaEliminacion(String fechaEliminacion) {
		this.fechaEliminacion = fechaEliminacion;
	}
	/**
	 * @return the horaEliminacion
	 */
	public String getHoraEliminacion() {
		return horaEliminacion;
	}
	/**
	 * @param horaEliminacion the horaEliminacion to set
	 */
	public void setHoraEliminacion(String horaEliminacion) {
		this.horaEliminacion = horaEliminacion;
	}

	/**
	 * @return the eliminado
	 */
	public String getEliminado() {
		return eliminado;
	}

	/**
	 * @param eliminado the eliminado to set
	 */
	public void setEliminado(String eliminado) {
		this.eliminado = eliminado;
	}

	/**
	 * @return the fechaModifica
	 */
	public String getFechaModifica() {
		return fechaModifica;
	}

	/**
	 * @param fechaModifica the fechaModifica to set
	 */
	public void setFechaModifica(String fechaModifica) {
		this.fechaModifica = fechaModifica;
	}

	/**
	 * @return the horaModifica
	 */
	public String getHoraModifica() {
		return horaModifica;
	}

	/**
	 * @param horaModifica the horaModifica to set
	 */
	public void setHoraModifica(String horaModifica) {
		this.horaModifica = horaModifica;
	}

	/**
	 * @return the usuarioModifica
	 */
	public String getUsuarioModifica() {
		return usuarioModifica;
	}


	/**
	 * @param usuarioModifica the usuarioModifica to set
	 */
	public void setUsuarioModifica(String usuarioModifica) {
		this.usuarioModifica = usuarioModifica;
	}


	/**
	 * @return the usuarioEliminacion
	 */
	public String getUsuarioEliminacion() {
		return usuarioEliminacion;
	}


	/**
	 * @param usuarioEliminacion the usuarioEliminacion to set
	 */
	public void setUsuarioEliminacion(String usuarioEliminacion) {
		this.usuarioEliminacion = usuarioEliminacion;
	}


	/**
	 * Método que obtiene la fecha inicio en formato BD
	 * @return
	 */
	public String getFechaInicioBD()
	{
		return UtilidadFecha.conversionFormatoFechaABD(this.getFechaInicio());
	}

	/**
	 * Obtiene el valor del atributo esGenerar
	 *
	 * @return Retorna atributo esGenerar
	 */
	public String getEsGenerar()
	{
		return esGenerar;
	}

	/**
	 * Establece el valor del atributo esGenerar
	 *
	 * @param valor para el atributo esGenerar
	 */
	public void setEsGenerar(String esGenerar)
	{
		this.esGenerar = esGenerar;
	}
}
