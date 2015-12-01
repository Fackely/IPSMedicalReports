package com.princetonsa.dto.manejoPaciente;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import util.ConstantesBD;
import util.UtilidadFecha;

import com.princetonsa.dto.administracion.DtoIntegridadDominio;
import com.princetonsa.dto.comun.DtoCheckBox;


/**
 * Esta clase se encarga de contener los datos de búsqueda para los totales 
 * de las ordenes autorizadas de ent Subcontratadas 
 * 
 * 
 * @author Camilo Gómez 
 */
public class DtoBusquedaTotalOrdenesAutorizadasEntSub implements Serializable{

		
		private static final long serialVersionUID = 1L;
		private Date fechaInicioBusqueda;
		private Date fechaFinBusqueda;
		private Long codigoEntidadSub;
		private String nombreEntidadSub;
		private String [] estadosAutorizacion;
		private String estado;
		private String tipoConsulta;
		private Integer convenio;
		private Integer institucion;
		private String rangoFechas;
		private String ubicacionLogo;
		private String rutaLogo;
		private String razonSocial;
		private String nit;
		private String nombreUsuario;
		
		private ArrayList<DtoIntegridadDominio> nombresEstadosAutorizaciones;
		private String nombreTipoConsulta;
		
		private int tipoSalida;
		
		public DtoBusquedaTotalOrdenesAutorizadasEntSub(){
			this.reset();
		}
		public void reset(){
			this.fechaInicioBusqueda=new Date();
			this.fechaFinBusqueda=new Date();
			this.codigoEntidadSub=ConstantesBD.codigoNuncaValidoLong;
			this.nombreEntidadSub="";
			this.estadosAutorizacion=new String[]{};
			this.estado="";
			this.tipoConsulta="";
			this.convenio=ConstantesBD.codigoNuncaValido;
			this.institucion=ConstantesBD.codigoNuncaValido;	
			this.nombresEstadosAutorizaciones = new ArrayList<DtoIntegridadDominio>();
			this.nombreTipoConsulta = "";
			this.setTipoSalida(ConstantesBD.codigoNuncaValido);
			
		}
		
		
		public String getNombreTipoConsulta() {
			return nombreTipoConsulta;
		}
		public void setNombreTipoConsulta(
				String nombreTipoConsulta) {
			this.nombreTipoConsulta = nombreTipoConsulta;
		}
		public ArrayList<DtoIntegridadDominio> getNombresEstadosAutorizaciones() {
			return nombresEstadosAutorizaciones;
		}
		public void setNombresEstadosAutorizaciones(
				ArrayList<DtoIntegridadDominio> nombresEstadosAutorizaciones) {
			this.nombresEstadosAutorizaciones = nombresEstadosAutorizaciones;
		}
		/**
		 * Este Método se encarga de obtener el valor 
		 * del atributo fechaIncioBusqueda
		
		 * @return retorna la variable fechaIncioBusqueda 
		 * @author Camilo Gómez 
		 */
		public Date getFechaInicioBusqueda() {
			return fechaInicioBusqueda;
		}

		/**
		 * Este Método se encarga de establecer el valor 
		 * del atributo fechaIncioBusqueda
		
		 * @param valor para el atributo fechaIncioBusqueda 
		 * @author Camilo Gómez 
		 */
		public void setFechaInicioBusqueda(Date fechaInicioBusqueda) {
			this.fechaInicioBusqueda = fechaInicioBusqueda;
		}

		/**
		 * Este Método se encarga de obtener el valor 
		 * del atributo fechaFinBusqueda
		
		 * @return retorna la variable fechaFinBusqueda 
		 * @author Camilo Gómez 
		 */
		public Date getFechaFinBusqueda() {
			return fechaFinBusqueda;
		}

		/**
		 * Este Método se encarga de establecer el valor 
		 * del atributo fechaFinBusqueda
		
		 * @param valor para el atributo fechaFinBusqueda 
		 * @author Camilo Gómez 
		 */
		public void setFechaFinBusqueda(Date fechaFinBusqueda) {
			this.fechaFinBusqueda = fechaFinBusqueda;
		}
		
		/**
		 * Este Método se encarga de obtener el valor 
		 * del atributo codigoEntidadSub
		
		 * @return retorna la variable codigoEntidadSub 
		 * @author Camilo Gómez 
		 */
		public Long getCodigoEntidadSub() {
			return codigoEntidadSub;
		}

		/**
		 * Este Método se encarga de establecer el valor 
		 * del atributo codigoEntidadSub
		 * 		
		 * @param valor para el atributo codigoEntidadSub 
		 * @author Camilo Gómez 
		 */
		public void setCodigoEntidadSub(Long codigoEntidadSub) {
			this.codigoEntidadSub = codigoEntidadSub;
		}		
				
		
		/**
		 *Metodo para obtener el estado de la autorizacion 
		 * @return estado estadoAutorizacion
		 * @author Camilo Gómez
		 */
		public String[] getEstadosAutorizacion() {
			return estadosAutorizacion;
		}
		
		/**
		 * Establece el estado de la autorizacion escogido para la buequeda
		 * @param estadoAutorizacion
		 * @author Camilo Gómez
		 */
		public void setEstadosAutorizacion(String[] estadosAutorizacion) {
			this.estadosAutorizacion=estadosAutorizacion;
		}	
		
	
		/**
		 *Metodo para obtener el tipo de consulta 
		 * @return tipoConsulta
		 * @author Camilo Gómez
		 */
		public String getTipoConsulta() {
			return tipoConsulta;
		}

		/**
		 * Establece el Tipo de consulta
		 * @param TipoConsulta
		 * @author Camilo Gómez
		 */
		public void setTipoConsulta(String tipoConsulta) {
			this.tipoConsulta = tipoConsulta;
		}

		/**
		 * Establece el estado de la autorizacion
		 * @param tipoConsulta
		 * @author Camilo Gómez
		 */
		public void setConvenio(Integer convenio) {
			this.convenio = convenio;
		}

		/**
		 *Metodo para obtener el convenio 
		 * @return convenio
		 * @author Camilo Gómez
		 */
		public Integer getConvenio() {
			return convenio;
		}

		/**
		 * Establece el codigo de la institucion
		 * @param institucion
		 * @author Camilo Gómez
		 */
		public void setInstitucion(Integer institucion) {
			this.institucion = institucion;
		}

		/**
		 *Metodo para obtener la institucion 
		 * @return institucion
		 * @author Camilo Gómez
		 */
		public Integer getInstitucion() {
			return institucion;
		}

		public String getRangoFechas() {
			String rango=UtilidadFecha.conversionFormatoFechaAAp(this.getFechaInicioBusqueda())+" - "+
				UtilidadFecha.conversionFormatoFechaAAp(this.getFechaFinBusqueda());
			return  rango;
		}

		public void setNombreEntidadSub(String nombreEntidadSub) {
			this.nombreEntidadSub = nombreEntidadSub;
		}

		public String getNombreEntidadSub() {
			return nombreEntidadSub;
		}

		public void setEstado(String estado) {
			this.estado = estado;
		}

		public String getEstado() {
			return estado;
		}
		public void setUbicacionLogo(String ubicacionLogo) {
			this.ubicacionLogo = ubicacionLogo;
		}
		public String getUbicacionLogo() {
			return ubicacionLogo;
		}
		public void setRutaLogo(String rutaLogo) {
			this.rutaLogo = rutaLogo;
		}
		public String getRutaLogo() {
			return rutaLogo;
		}
		public void setRazonSocial(String razonSocial) {
			this.razonSocial = razonSocial;
		}
		public String getRazonSocial() {
			return razonSocial;
		}
		public void setNit(String nit) {
			this.nit = nit;
		}
		public String getNit() {
			return nit;
		}
		public void setNombreUsuario(String nombreUsuario) {
			this.nombreUsuario = nombreUsuario;
		}
		public String getNombreUsuario() {
			return nombreUsuario;
		}
		public void setTipoSalida(int tipoSalida) {
			this.tipoSalida = tipoSalida;
		}
		public int getTipoSalida() {
			return tipoSalida;
		}

}
