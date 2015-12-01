/**
 * 
 */
package com.princetonsa.dto.manejoPaciente;

import java.io.Serializable;
import java.util.ArrayList;

import net.sf.jasperreports.engine.JRDataSource;

import com.princetonsa.dto.inventario.DtoArticulosAutorizaciones;
import com.princetonsa.dto.inventario.DtoServiciosAutorizaciones;

/**
 * Esta clase se encarga de contener los datos 
 * para la generación del reporte de autorizaciones de
 * servicios en el formato estándar 
 * 
 * @author Angela Maria Aguirre
 * @since 21/12/2010
 */
@SuppressWarnings("serial")
public class DTOReporteEstandarAutorizacionServiciosArticulos implements Serializable {
	
	private String nombrePaciente;
	private String tipoDocPaciente;
	private String numeroDocPaciente;
	private String tipoContrato;
	private String entidadSubcontratada;
	private String numeroAutorizacion;
	private String direccionEntidadSub;
	private String telefonoEntidadSub;
	private String fechaAutorizacion;
	private String fechaVencimiento;
	private String estadoAutorizacion;
	private String entidadAutoriza;
	private String usuarioAutoriza;
	private String observaciones;	
	private String rutaLogo;
	private String ubicacionLogo;
	private String nombreLogo;
	private String formatoMediaCarta;
	private String infoParametroGeneral;
	private String infoPiePagina;
	private String logoDerecha;
	private String logoIzquierda;
	private ArrayList<DtoServiciosAutorizaciones> listaServiciosAutorizados;
	private JRDataSource dsServiciosAutorizados;
	private ArrayList<DtoArticulosAutorizaciones> listaArticulosAutorizados;
	private JRDataSource dsMedicamentosAutorizados;
	private JRDataSource dsInsumosAutorizados;
	
	
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo infoParametroGeneral
	
	 * @return retorna la variable infoParametroGeneral 
	 * @author Angela Maria Aguirre 
	 */
	public String getInfoParametroGeneral() {
		return infoParametroGeneral;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo infoParametroGeneral
	
	 * @param valor para el atributo infoParametroGeneral 
	 * @author Angela Maria Aguirre 
	 */
	public void setInfoParametroGeneral(String infoParametroGeneral) {
		this.infoParametroGeneral = infoParametroGeneral;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo nombrePaciente
	
	 * @return retorna la variable nombrePaciente 
	 * @author Angela Maria Aguirre 
	 */
	public String getNombrePaciente() {
		return nombrePaciente;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo nombrePaciente
	
	 * @param valor para el atributo nombrePaciente 
	 * @author Angela Maria Aguirre 
	 */
	public void setNombrePaciente(String nombrePaciente) {
		this.nombrePaciente = nombrePaciente;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo tipoDocPaciente
	
	 * @return retorna la variable tipoDocPaciente 
	 * @author Angela Maria Aguirre 
	 */
	public String getTipoDocPaciente() {
		return tipoDocPaciente;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo tipoDocPaciente
	
	 * @param valor para el atributo tipoDocPaciente 
	 * @author Angela Maria Aguirre 
	 */
	public void setTipoDocPaciente(String tipoDocPaciente) {
		this.tipoDocPaciente = tipoDocPaciente;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo numeroDocPaciente
	
	 * @return retorna la variable numeroDocPaciente 
	 * @author Angela Maria Aguirre 
	 */
	public String getNumeroDocPaciente() {
		return numeroDocPaciente;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo numeroDocPaciente
	
	 * @param valor para el atributo numeroDocPaciente 
	 * @author Angela Maria Aguirre 
	 */
	public void setNumeroDocPaciente(String numeroDocPaciente) {
		this.numeroDocPaciente = numeroDocPaciente;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo tipoContrato
	
	 * @return retorna la variable tipoContrato 
	 * @author Angela Maria Aguirre 
	 */
	public String getTipoContrato() {
		return tipoContrato;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo tipoContrato
	
	 * @param valor para el atributo tipoContrato 
	 * @author Angela Maria Aguirre 
	 */
	public void setTipoContrato(String tipoContrato) {
		this.tipoContrato = tipoContrato;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo entidadSubcontratada
	
	 * @return retorna la variable entidadSubcontratada 
	 * @author Angela Maria Aguirre 
	 */
	public String getEntidadSubcontratada() {
		return entidadSubcontratada;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo entidadSubcontratada
	
	 * @param valor para el atributo entidadSubcontratada 
	 * @author Angela Maria Aguirre 
	 */
	public void setEntidadSubcontratada(String entidadSubcontratada) {
		this.entidadSubcontratada = entidadSubcontratada;
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
	 * del atributo rutaLogo
	
	 * @return retorna la variable rutaLogo 
	 * @author Angela Maria Aguirre 
	 */
	public String getRutaLogo() {
		return rutaLogo;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo rutaLogo
	
	 * @param valor para el atributo rutaLogo 
	 * @author Angela Maria Aguirre 
	 */
	public void setRutaLogo(String rutaLogo) {
		this.rutaLogo = rutaLogo;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo nombreLogo
	
	 * @return retorna la variable nombreLogo 
	 * @author Angela Maria Aguirre 
	 */
	public String getNombreLogo() {
		return nombreLogo;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo nombreLogo
	
	 * @param valor para el atributo nombreLogo 
	 * @author Angela Maria Aguirre 
	 */
	public void setNombreLogo(String nombreLogo) {
		this.nombreLogo = nombreLogo;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo formatoMediaCarta
	
	 * @return retorna la variable formatoMediaCarta 
	 * @author Angela Maria Aguirre 
	 */
	public String getFormatoMediaCarta() {
		return formatoMediaCarta;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo formatoMediaCarta
	
	 * @param valor para el atributo formatoMediaCarta 
	 * @author Angela Maria Aguirre 
	 */
	public void setFormatoMediaCarta(String formatoMediaCarta) {
		this.formatoMediaCarta = formatoMediaCarta;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo listaServiciosAutorizados
	
	 * @return retorna la variable listaServiciosAutorizados 
	 * @author Angela Maria Aguirre 
	 */
	public ArrayList<DtoServiciosAutorizaciones> getListaServiciosAutorizados() {
		return listaServiciosAutorizados;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo listaServiciosAutorizados
	
	 * @param valor para el atributo listaServiciosAutorizados 
	 * @author Angela Maria Aguirre 
	 */
	public void setListaServiciosAutorizados(
			ArrayList<DtoServiciosAutorizaciones> listaServiciosAutorizados) {
		this.listaServiciosAutorizados = listaServiciosAutorizados;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo dsServiciosAutorizados
	
	 * @return retorna la variable dsServiciosAutorizados 
	 * @author Angela Maria Aguirre 
	 */
	public JRDataSource getDsServiciosAutorizados() {
		return dsServiciosAutorizados;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo dsServiciosAutorizados
	
	 * @param valor para el atributo dsServiciosAutorizados 
	 * @author Angela Maria Aguirre 
	 */
	public void setDsServiciosAutorizados(JRDataSource dsServiciosAutorizados) {
		this.dsServiciosAutorizados = dsServiciosAutorizados;
	}
	/**
	 * Este Método se encarga de obtener el valor
	 * del atributo listaArticulosAutorizados
	 * 
	 * @return retorna la variable listaArticulosAutorizados
	 * @author Diana Carolina G
	 */
	public ArrayList<DtoArticulosAutorizaciones> getListaArticulosAutorizados() {
		return listaArticulosAutorizados;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo listaArticulosAutorizados
	 * 
	 * @param valor para el atributo listaArticulosAutorizados
	 * @author Diana Carolina G
	 */
	public void setListaArticulosAutorizados(
			ArrayList<DtoArticulosAutorizaciones> listaArticulosAutorizados) {
		this.listaArticulosAutorizados = listaArticulosAutorizados;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo dsMedicamentosAutorizados
	 * 	
	 * @return retorna la variable dsMedicamentosAutorizados 
	 * @author Diana Carolina G
	 */
	public JRDataSource getDsMedicamentosAutorizados() {
		return dsMedicamentosAutorizados;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo dsMedicamentosAutorizados
	 * 
	 * @param valor para el atributo dsMedicamentosAutorizados
	 * @author Diana Carolina G
	 */
	public void setDsMedicamentosAutorizados(JRDataSource dsMedicamentosAutorizados) {
		this.dsMedicamentosAutorizados = dsMedicamentosAutorizados;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo dsInsumosAutorizados
	 * 
	 * @return retorna la variable dsInsumosAutorizados
	 * @author Diana Carolina G
	 */
	public JRDataSource getDsInsumosAutorizados() {
		return dsInsumosAutorizados;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo dsInsumosAutorizados 
	 *  
	 * @param valor para el atributo dsInsumosAutorizados
	 * @author Diana Carolina G
	 */
	public void setDsInsumosAutorizados(JRDataSource dsInsumosAutorizados) {
		this.dsInsumosAutorizados = dsInsumosAutorizados;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo infoPiePagina
	
	 * @return retorna la variable infoPiePagina 
	 * @author Angela Maria Aguirre 
	 */
	public String getInfoPiePagina() {
		return infoPiePagina;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo infoPiePagina
	
	 * @param valor para el atributo infoPiePagina 
	 * @author Angela Maria Aguirre 
	 */
	public void setInfoPiePagina(String infoPiePagina) {
		this.infoPiePagina = infoPiePagina;
	}
	
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo ubicacionLogo
	 * @param ubicacionLogo
	 * @author Diana Carolina G
	 */
	public void setUbicacionLogo(String ubicacionLogo) {
		this.ubicacionLogo = ubicacionLogo;
	}
	
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo ubicacionLogo
	 * @return
	 * @author Diana Carolina G
	 */
	public String getUbicacionLogo() {
		return ubicacionLogo;
	}
	public String getLogoDerecha() {
		return logoDerecha;
	}
	public void setLogoDerecha(String logoDerecha) {
		this.logoDerecha = logoDerecha;
	}
	public String getLogoIzquierda() {
		return logoIzquierda;
	}
	public void setLogoIzquierda(String logoIzquierda) {
		this.logoIzquierda = logoIzquierda;
	}
	
	
	
	

}
