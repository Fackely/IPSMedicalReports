package com.princetonsa.dto.manejoPaciente;

/**
 * Esta clase se encarga de contener los datos de la 
 * autorizacion, necesarios para generar el reporte de autorizacion
 * de artículos con el formato de Versalles
 * 
 * @author Angela Maria Aguirre
 * @since 27/12/2010
 */
public class DTOReporteAutorizacionSeccionAutorizacion {
	
	private String entidadSub;
	private String numeroAutorizacion;
	private String direccionEntidadSub;
	private String telefonoEntidadSub;
	private String fechaAutorizacion;
	private String fechaVencimiento;
	private String estadoAutorizacion;
	private String entidadAutoriza;
	private String usuarioAutoriza;
	private String observaciones;
	
	/**
	 * Atributos empleados en la impresi&oacute;n
	 * de la autorizaci&oacute;n de ingreso estancia
	 * @author Diana Carolina G
	 */
	private String fechaInicioAutorizacion;
	private int    diasEstanciaAutorizados;
	
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo entidadSub
	
	 * @return retorna la variable entidadSub 
	 * @author Angela Maria Aguirre 
	 */
	public String getEntidadSub() {
		return entidadSub;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo entidadSub
	
	 * @param valor para el atributo entidadSub 
	 * @author Angela Maria Aguirre 
	 */
	public void setEntidadSub(String entidadSub) {
		this.entidadSub = entidadSub;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo numeroAutorizacion
	
	 * @return retorna la variable numeroAutorizacion 
	 * @author Angela Maria Aguirre 
	 */
	public String getNumeroAutorizacion() {
		return numeroAutorizacion;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo numeroAutorizacion
	
	 * @param valor para el atributo numeroAutorizacion 
	 * @author Angela Maria Aguirre 
	 */
	public void setNumeroAutorizacion(String numeroAutorizacion) {
		this.numeroAutorizacion = numeroAutorizacion;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo direccionEntidadSub
	
	 * @return retorna la variable direccionEntidadSub 
	 * @author Angela Maria Aguirre 
	 */
	public String getDireccionEntidadSub() {
		return direccionEntidadSub;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo direccionEntidadSub
	
	 * @param valor para el atributo direccionEntidadSub 
	 * @author Angela Maria Aguirre 
	 */
	public void setDireccionEntidadSub(String direccionEntidadSub) {
		this.direccionEntidadSub = direccionEntidadSub;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo telefonoEntidadSub
	
	 * @return retorna la variable telefonoEntidadSub 
	 * @author Angela Maria Aguirre 
	 */
	public String getTelefonoEntidadSub() {
		return telefonoEntidadSub;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo telefonoEntidadSub
	
	 * @param valor para el atributo telefonoEntidadSub 
	 * @author Angela Maria Aguirre 
	 */
	public void setTelefonoEntidadSub(String telefonoEntidadSub) {
		this.telefonoEntidadSub = telefonoEntidadSub;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo fechaAutorizacion
	
	 * @return retorna la variable fechaAutorizacion 
	 * @author Angela Maria Aguirre 
	 */
	public String getFechaAutorizacion() {
		return fechaAutorizacion;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo fechaAutorizacion
	
	 * @param valor para el atributo fechaAutorizacion 
	 * @author Angela Maria Aguirre 
	 */
	public void setFechaAutorizacion(String fechaAutorizacion) {
		this.fechaAutorizacion = fechaAutorizacion;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo fechaVencimiento
	
	 * @return retorna la variable fechaVencimiento 
	 * @author Angela Maria Aguirre 
	 */
	public String getFechaVencimiento() {
		return fechaVencimiento;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo fechaVencimiento
	
	 * @param valor para el atributo fechaVencimiento 
	 * @author Angela Maria Aguirre 
	 */
	public void setFechaVencimiento(String fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo estadoAutorizacion
	
	 * @return retorna la variable estadoAutorizacion 
	 * @author Angela Maria Aguirre 
	 */
	public String getEstadoAutorizacion() {
		return estadoAutorizacion;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo estadoAutorizacion
	
	 * @param valor para el atributo estadoAutorizacion 
	 * @author Angela Maria Aguirre 
	 */
	public void setEstadoAutorizacion(String estadoAutorizacion) {
		this.estadoAutorizacion = estadoAutorizacion;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo entidadAutoriza
	
	 * @return retorna la variable entidadAutoriza 
	 * @author Angela Maria Aguirre 
	 */
	public String getEntidadAutoriza() {
		return entidadAutoriza;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo entidadAutoriza
	
	 * @param valor para el atributo entidadAutoriza 
	 * @author Angela Maria Aguirre 
	 */
	public void setEntidadAutoriza(String entidadAutoriza) {
		this.entidadAutoriza = entidadAutoriza;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo usuarioAutoriza
	
	 * @return retorna la variable usuarioAutoriza 
	 * @author Angela Maria Aguirre 
	 */
	public String getUsuarioAutoriza() {
		return usuarioAutoriza;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo usuarioAutoriza
	
	 * @param valor para el atributo usuarioAutoriza 
	 * @author Angela Maria Aguirre 
	 */
	public void setUsuarioAutoriza(String usuarioAutoriza) {
		this.usuarioAutoriza = usuarioAutoriza;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo observaciones
	
	 * @return retorna la variable observaciones 
	 * @author Angela Maria Aguirre 
	 */
	public String getObservaciones() {
		return observaciones;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo observaciones
	
	 * @param valor para el atributo observaciones 
	 * @author Angela Maria Aguirre 
	 */
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
	
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo fechaInicioAutorizacion
	 * @return fechaInicioAutorizacion
	 * @author Diana Carolina G
	 */
	public String getFechaInicioAutorizacion() {
		return fechaInicioAutorizacion;
	}
	
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo fechaInicioAutorizacion
	 * @param fechaInicioAutorizacion
	 * @author Diana Carolina G
	 */
	public void setFechaInicioAutorizacion(String fechaInicioAutorizacion) {
		this.fechaInicioAutorizacion = fechaInicioAutorizacion;
	}
	
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo diasEstanciaAutorizados
	 * @return diasEstanciaAutorizados
	 * @author Diana Carolina G
	 */
	public int getDiasEstanciaAutorizados() {
		return diasEstanciaAutorizados;
	}
	
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo diasEstanciaAutorizados
	 * @param diasEstanciaAutorizados
	 * @author Diana Carolina G
	 */
	public void setDiasEstanciaAutorizados(int diasEstanciaAutorizados) {
		this.diasEstanciaAutorizados = diasEstanciaAutorizados;
	}
	
	
	
	

}
