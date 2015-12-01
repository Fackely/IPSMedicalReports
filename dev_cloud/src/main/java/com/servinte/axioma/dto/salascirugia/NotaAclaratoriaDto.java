/**
 * 
 */
package com.servinte.axioma.dto.salascirugia;

import java.io.Serializable;
import java.util.Date;

/**
 * @author jeilones
 * @created 17/06/2013
 *
 */
public class NotaAclaratoriaDto implements Serializable {

	/**
	 * serial
	 */
	private static final long serialVersionUID = -3518175891244274278L;
	/**
	 * Descripcion o contenido de la nota aclaratoria
	 */
	private String descripcion;
	/**
	 * Codigo de la nota aclaratoria
	 */
	private int codigo;
	/**
	 * Codigo del informe quirurgico por especialidad al que pertenece la nota aclaratoria
	 */
	private int codigoInformeQxEspecialidad;
	/**
	 * Feca de grabacion registrada
	 */
	private Date fechaGrabacion;
	/**
	 * Hora de grabacion registrada
	 */
	private String horaGrabacion;
	/**
	 * Login de usuario grabacion
	 */
	private String usuarioGrabacion;
	/**
	 * Especialidades del medico (usuario)
	 */
	private String especialidadesMedico;
	/**
	 * nombre completo del usuario
	 */
	private String nombreUsuario;
	/**
	 * Datos medico incluye nombre y Registro medico
	 */
	private String datosMedico;
	
	
	public NotaAclaratoriaDto() {
	}

	public NotaAclaratoriaDto(String descripcion, int codigo,
			Date fechaGrabacion, String horaGrabacion, String usuarioGrabacion, int codigoInformeQxEspecialidad, String especialidadesMedico, String nombreUsuario, String datosMedico) {
		super();
		this.codigoInformeQxEspecialidad = codigoInformeQxEspecialidad;
		this.descripcion = descripcion;
		this.codigo = codigo;
		this.fechaGrabacion = fechaGrabacion;
		this.horaGrabacion = horaGrabacion;
		this.usuarioGrabacion = usuarioGrabacion;
		this.especialidadesMedico = especialidadesMedico;
		this.nombreUsuario = nombreUsuario;
		this.datosMedico = datosMedico;
	}

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public Date getFechaGrabacion() {
		return fechaGrabacion;
	}

	public void setFechaGrabacion(Date fechaGrabacion) {
		this.fechaGrabacion = fechaGrabacion;
	}

	public String getHoraGrabacion() {
		return horaGrabacion;
	}

	public void setHoraGrabacion(String horaGrabacion) {
		this.horaGrabacion = horaGrabacion;
	}

	public String getUsuarioGrabacion() {
		return usuarioGrabacion;
	}

	public void setUsuarioGrabacion(String usuarioGrabacion) {
		this.usuarioGrabacion = usuarioGrabacion;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public int getCodigoInformeQxEspecialidad() {
		return codigoInformeQxEspecialidad;
	}

	public void setCodigoInformeQxEspecialidad(int codigoInformeQxEspecialidad) {
		this.codigoInformeQxEspecialidad = codigoInformeQxEspecialidad;
	}

	public String getEspecialidadesMedico() {
		return especialidadesMedico;
	}

	public void setEspecialidadesMedico(String especialidadesMedico) {
		this.especialidadesMedico = especialidadesMedico;
	}
	
	public String getNombreUsuario() {
		return nombreUsuario;
	}

	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}

	public String getDatosMedico() {
		return datosMedico;
	}

	public void setDatosMedico(String datosMedico) {
		this.datosMedico = datosMedico;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	protected NotaAclaratoriaDto clone() throws CloneNotSupportedException {
		NotaAclaratoriaDto notaAclaratoriaDto=new NotaAclaratoriaDto();
		notaAclaratoriaDto.setDescripcion(this.getDescripcion());
		return notaAclaratoriaDto;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + codigo;
		result = prime * result
				+ ((descripcion == null) ? 0 : descripcion.hashCode());
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
		NotaAclaratoriaDto other = (NotaAclaratoriaDto) obj;
		if (codigo != other.codigo)
			return false;
		if (descripcion == null) {
			if (other.descripcion != null)
				return false;
		} else if (!descripcion.equals(other.descripcion))
			return false;
		return true;
	}

}
