/*
 * Abril 21, 2009
 */
package com.princetonsa.dto.manejoPaciente;

import java.io.Serializable;

import util.ConstantesBD;

import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.atencion.Diagnostico;

/**
 * DTO: que modela los diagn�sticos de la autorizacion (diag_autorizacion)
 * @author Sebasti�n G�mez
 *
 */
public class DtoDiagAutorizacion implements Serializable
{
	private String codigoPK;
	private String autorizacion;
	private Diagnostico diagnostico;
	private String fechaModifica;
	private String horaModifica;
	private UsuarioBasico usuarioModifica;
	
	/**
	 * Atributo para saber si debo eliminar
	 */
	private boolean eliminado;
	private boolean activo;
	
	/**
	 * Constructor
	 */
	public DtoDiagAutorizacion()
	{
		this.clean();
	}
	
	public void clean()
	{
		this.codigoPK = "";
		this.autorizacion = "";
		this.diagnostico = new Diagnostico("",ConstantesBD.codigoNuncaValido);
		this.fechaModifica = "";
		this.horaModifica = "";
		this.usuarioModifica = new UsuarioBasico();
		this.eliminado = false;
		this.activo = true;
		
	}

	/**
	 * @return the codigoPK
	 */
	public String getCodigoPK() {
		return codigoPK;
	}

	/**
	 * @param codigoPK the codigoPK to set
	 */
	public void setCodigoPK(String codigoPK) {
		this.codigoPK = codigoPK;
	}

	/**
	 * @return the autorizacion
	 */
	public String getAutorizacion() {
		return autorizacion;
	}

	/**
	 * @param autorizacion the autorizacion to set
	 */
	public void setAutorizacion(String autorizacion) {
		this.autorizacion = autorizacion;
	}

	/**
	 * @return the diagnostico
	 */
	public Diagnostico getDiagnostico() {
		return diagnostico;
	}

	/**
	 * @param diagnostico the diagnostico to set
	 */
	public void setDiagnostico(Diagnostico diagnostico) {
		this.diagnostico = diagnostico;
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
	public UsuarioBasico getUsuarioModifica() {
		return usuarioModifica;
	}

	/**
	 * @param usuarioModifica the usuarioModifica to set
	 */
	public void setUsuarioModifica(UsuarioBasico usuarioModifica) {
		this.usuarioModifica = usuarioModifica;
	}

	/**
	 * @return the eliminado
	 */
	public boolean isEliminado() {
		return eliminado;
	}

	/**
	 * @param eliminado the eliminado to set
	 */
	public void setEliminado(boolean eliminado) {
		this.eliminado = eliminado;
	}

	public boolean isActivo() {
		return activo;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}
	
}
