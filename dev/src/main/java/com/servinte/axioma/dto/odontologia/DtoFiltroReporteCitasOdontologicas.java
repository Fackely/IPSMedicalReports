/**
 * 
 */
package com.servinte.axioma.dto.odontologia;

import java.util.ArrayList;

import util.ConstantesIntegridadDominio;
import util.UtilidadFecha;

import com.princetonsa.dto.administracion.DtoEspecialidades;
import com.princetonsa.dto.administracion.DtoProfesional;
import com.princetonsa.dto.facturacion.DtoEmpresasInstitucion;
import com.princetonsa.dto.facturacion.DtoServicios;
import com.princetonsa.dto.manejoPaciente.DtoCentrosAtencion;
import com.princetonsa.dto.manejoPaciente.DtoRegionesCobertura;
import com.princetonsa.dto.tesoreria.DtoUsuarioPersona;
import com.servinte.axioma.dto.administracion.DtoCiudades;
import com.servinte.axioma.dto.administracion.DtoPaises;
import com.servinte.axioma.dto.consultaExterna.DtoUnidadesConsulta;

/**
 * @author armando
 *
 */
public class DtoFiltroReporteCitasOdontologicas 
{
	/**
	 * 
	 */
	private int institucion;
	
	/**
	 * 
	 */
	private String usuarioReporte;
	
	/**
	 * 
	 */
	private String fechaInicial;
	
	/**
	 * 
	 */
	private String fechaFinal;
	
	/**
	 * 
	 */
	private DtoPaises pais;
	
	/**
	 * 
	 */
	private DtoCiudades ciudad;
	
	/**
	 * 
	 */
	private DtoRegionesCobertura regionesCobertura;
	
	/**
	 * 
	 */
	private DtoEmpresasInstitucion empresaInstitucion;
	
	/**
	 * 
	 */
	private DtoCentrosAtencion centroAtencion;
	
	/**
	 * 
	 */
	private String[] tiposCita;

	/**
	 * 
	 */
	private String[] estadosCita;
	
	/**
	 * 
	 */
	private String cancelacionCita;

	/**
	 * 
	 */
	private int canceladaPor;
	
	/**
	 * 
	 */
	private DtoEspecialidades especialidad;
	
	/**
	 * 
	 */
	private DtoUnidadesConsulta unidadAgenda;
	
	/**
	 * 
	 */
	private DtoProfesional profesional;
	
	/**
	 * 
	 */
	private DtoUsuarioPersona usuario;
	
	/**
	 * 
	 */
	private ArrayList<DtoServicios> servicios;
	
	/**
	 * Atributo que almacena el nombre completo del usuario activo en la sesion.
	 */
	private String nombreUsuario; 
	
	private String[] ayudanteTiposCita;
	
	private String[] ayudanteEstadosCita;
	
	/**
	 * Almacena la ruta en la cual se encuentra almacenado el logo.
	 */
	private String rutaLogo;
	
	/**
	 * Almacena si el logo debe ser ubicado a la derecha o a la izquierda.
	 */
	private String ubicacionLogo;
	
	/**
	 * Atributo que almacena la fecha inicial de los criterios de
	 * b&uacute;squeda con el formato de impresi&oacute;n correcto.
	 */
	private String fechaInicialFormateado;
	
	/**
	 * Atributo que almacena la fecha final de los criterios de
	 * b&uacute;squeda con el formato de impresi&oacute;n correcto.
	 */
	private String fechaFinalFormateado;
	
	/**
	 * Almacena la razón social de la institución.
	 */
	private String razonSocial;
	
	/**
	 * Almacena el tipo de reporte a generar...detallado, resumido o ambos.
	 */
	private int tipoReporte;
	
	
	/**
	 * Mètodo constructor de la clase
	 *
	 * @author Yennifer Guerrero
	 */
	public DtoFiltroReporteCitasOdontologicas() {
		this.ayudanteEstadosCita = null;
	}
	
	
	public int getInstitucion() {
		return institucion;
	}

	public void setInstitucion(int institucion) {
		this.institucion = institucion;
	}

	public String getUsuarioReporte() {
		return usuarioReporte;
	}

	public void setUsuarioReporte(String usuarioReporte) {
		this.usuarioReporte = usuarioReporte;
	}

	public String getFechaInicial() {
		return fechaInicial;
	}

	public void setFechaInicial(String fechaInicial) {
		this.fechaInicial = fechaInicial;
	}

	public String getFechaFinal() {
		return fechaFinal;
	}

	public void setFechaFinal(String fechaFinal) {
		this.fechaFinal = fechaFinal;
	}

	public DtoPaises getPais() {
		return pais;
	}

	public void setPais(DtoPaises pais) {
		this.pais = pais;
	}

	public DtoCiudades getCiudad() {
		return ciudad;
	}

	public void setCiudad(DtoCiudades ciudad) {
		this.ciudad = ciudad;
	}

	public DtoRegionesCobertura getRegionesCobertura() {
		return regionesCobertura;
	}

	public void setRegionesCobertura(DtoRegionesCobertura regionesCobertura) {
		this.regionesCobertura = regionesCobertura;
	}

	public DtoEmpresasInstitucion getEmpresaInstitucion() {
		return empresaInstitucion;
	}

	public void setEmpresaInstitucion(DtoEmpresasInstitucion empresaInstitucion) {
		this.empresaInstitucion = empresaInstitucion;
	}

	public DtoCentrosAtencion getCentroAtencion() {
		return centroAtencion;
	}

	public void setCentroAtencion(DtoCentrosAtencion centroAtencion) {
		this.centroAtencion = centroAtencion;
	}

	public String[] getTiposCita() {
		
		return tiposCita;
	}

	public void setTiposCita(String[] tiposCita) {
		this.tiposCita = tiposCita;
		
		this.ayudanteTiposCita = tiposCita.clone();
		
		if (this.ayudanteTiposCita != null && this.ayudanteTiposCita.length > 0) {
			
			for (int i = 0; i < ayudanteTiposCita.length; i++) {
				String tipos= ayudanteTiposCita [i];
				
				if (tipos.trim().equals(ConstantesIntegridadDominio.acronimoPrioritaria)) {
					this.ayudanteTiposCita [i] = "A Prior";
				}
				if (tipos.trim().equals(ConstantesIntegridadDominio.acronimoTipoCitaOdonValoracionInicial)) {
					this.ayudanteTiposCita [i] = "V Inic";
				}
				if (tipos.trim().equals(ConstantesIntegridadDominio.acronimoControlCitaOdon)) {
					this.ayudanteTiposCita [i] = "Cont";
				}
				if (tipos.trim().equals(ConstantesIntegridadDominio.acronimoRevaloracion)) {
					this.ayudanteTiposCita [i] = "Rev";
				}
				if (tipos.trim().equals(ConstantesIntegridadDominio.acronimoTratamiento)) {
					this.ayudanteTiposCita [i] = "Trat";
				}
				if (tipos.trim().equals(ConstantesIntegridadDominio.acronimoRemisionInterconsulta)) {
					this.ayudanteTiposCita [i] = "Int";
				}
				if (tipos.trim().equals(ConstantesIntegridadDominio.acronimoAuditoria)) {
					this.ayudanteTiposCita [i] = "Aud";
				}
			}
		}
	}

	public String[] getEstadosCita() {
		return estadosCita;
	}

	public void setEstadosCita(String[] estadosCita) {
		this.estadosCita = estadosCita;
		
		this.ayudanteEstadosCita = estadosCita.clone();
		
		if (this.ayudanteEstadosCita != null && this.ayudanteEstadosCita.length > 0) {
			
			for (int i = 0; i < ayudanteEstadosCita.length; i++) {
				String estadoCita= ayudanteEstadosCita [i];
				
				if (estadoCita.trim().equals(ConstantesIntegridadDominio.acronimoReservado)) {
					this.ayudanteEstadosCita[i] = "Res";
				}
				if (estadoCita.trim().equals(ConstantesIntegridadDominio.acronimoProgramado)) {
					this.ayudanteEstadosCita[i] = "Prog";
				}
				if (estadoCita.trim().equals(ConstantesIntegridadDominio.acronimoAsignado)) {
					this.ayudanteEstadosCita[i] = "Asig";
				}
				if (estadoCita.trim().equals(ConstantesIntegridadDominio.acronimoAreprogramar)) {
					this.ayudanteEstadosCita[i] = "A Rep";
				}
				if (estadoCita.trim().equals(ConstantesIntegridadDominio.acronimoNoAsistio)) {
					this.ayudanteEstadosCita[i] = "N Asis";
				}
				if (estadoCita.trim().equals(ConstantesIntegridadDominio.acronimoNoAtencion)) {
					this.ayudanteEstadosCita[i] = "N Aten";
				}
				if (estadoCita.trim().equals(ConstantesIntegridadDominio.acronimoAtendida)) {
					this.ayudanteEstadosCita[i] = "Aten";
				}
				if (estadoCita.trim().equals(ConstantesIntegridadDominio.acronimoCancelado)) {
					this.ayudanteEstadosCita[i] = "Can";
				}
			}
		}
	}

	public String getCancelacionCita() {
		return cancelacionCita;
	}

	public void setCancelacionCita(String cancelacionCita) {
		this.cancelacionCita = cancelacionCita;
	}

	public int getCanceladaPor() {
		return canceladaPor;
	}

	public void setCanceladaPor(int canceladaPor) {
		this.canceladaPor = canceladaPor;
	}

	public DtoEspecialidades getEspecialidad() {
		return especialidad;
	}

	public void setEspecialidad(DtoEspecialidades especialidad) {
		this.especialidad = especialidad;
	}

	public DtoUnidadesConsulta getUnidadAgenda() {
		return unidadAgenda;
	}

	public void setUnidadAgenda(DtoUnidadesConsulta unidadAgenda) {
		this.unidadAgenda = unidadAgenda;
	}

	public DtoProfesional getProfesional() {
		return profesional;
	}

	public void setProfesional(DtoProfesional profesional) {
		this.profesional = profesional;
	}

	public DtoUsuarioPersona getUsuario() {
		return usuario;
	}

	public void setUsuario(DtoUsuarioPersona usuario) {
		this.usuario = usuario;
	}

	public ArrayList<DtoServicios> getServicios() {
		return servicios;
	}

	public void setServicios(ArrayList<DtoServicios> servicios) {
		this.servicios = servicios;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo nombreUsuario
	 * 
	 * @return  Retorna la variable nombreUsuario
	 */
	public String getNombreUsuario() {
		return nombreUsuario;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo nombreUsuario
	 * 
	 * @param  valor para el atributo nombreUsuario 
	 */
	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}

	/**
	 * Método que se encarga de obtener el 
	 * valor del atributo  ayudanteTiposCita
	 *
	 * @return retorna la variable ayudanteTiposCita
	 */
	public String[] getAyudanteTiposCita() {
		return ayudanteTiposCita;
	}

	/**
	 * Método que se encarga de establecer el valor
	 * del atributo ayudanteTiposCita
	 * @param ayudanteTiposCita es el valor para el atributo ayudanteTiposCita 
	 */
	public void setAyudanteTiposCita(String[] ayudanteTiposCita) {
		this.ayudanteTiposCita = ayudanteTiposCita;
	}

	/**
	 * Método que se encarga de obtener el 
	 * valor del atributo  ayudanteEstadosCita
	 *
	 * @return retorna la variable ayudanteEstadosCita
	 */
	public String[] getAyudanteEstadosCita() {
		return ayudanteEstadosCita;
	}

	/**
	 * Método que se encarga de establecer el valor
	 * del atributo ayudanteEstadosCita
	 * @param ayudanteEstadosCita es el valor para el atributo ayudanteEstadosCita 
	 */
	public void setAyudanteEstadosCita(String[] ayudanteEstadosCita) {
		this.ayudanteEstadosCita = ayudanteEstadosCita;
	}


	/**
	 * Método que se encarga de obtener el 
	 * valor del atributo  rutaLogo
	 *
	 * @return retorna la variable rutaLogo
	 */
	public String getRutaLogo() {
		return rutaLogo;
	}


	/**
	 * Método que se encarga de establecer el valor
	 * del atributo rutaLogo
	 * @param rutaLogo es el valor para el atributo rutaLogo 
	 */
	public void setRutaLogo(String rutaLogo) {
		this.rutaLogo = rutaLogo;
	}


	/**
	 * Método que se encarga de obtener el 
	 * valor del atributo  ubicacionLogo
	 *
	 * @return retorna la variable ubicacionLogo
	 */
	public String getUbicacionLogo() {
		return ubicacionLogo;
	}


	/**
	 * Método que se encarga de establecer el valor
	 * del atributo ubicacionLogo
	 * @param ubicacionLogo es el valor para el atributo ubicacionLogo 
	 */
	public void setUbicacionLogo(String ubicacionLogo) {
		this.ubicacionLogo = ubicacionLogo;
	}


	/**
	 * Método que se encarga de obtener el 
	 * valor del atributo  fechaInicialFormateado
	 *
	 * @return retorna la variable fechaInicialFormateado
	 */
	public String getFechaInicialFormateado() {
		this.fechaInicialFormateado = UtilidadFecha.conversionFormatoFechaAAp(this.fechaInicial);
		return fechaInicialFormateado;
	}


	/**
	 * Método que se encarga de establecer el valor
	 * del atributo fechaInicialFormateado
	 * @param fechaInicialFormateado es el valor para el atributo fechaInicialFormateado 
	 */
	public void setFechaInicialFormateado(String fechaInicialFormateado) {
		this.fechaInicialFormateado = fechaInicialFormateado;
	}


	/**
	 * Método que se encarga de obtener el 
	 * valor del atributo  fechaFinalFormateado
	 *
	 * @return retorna la variable fechaFinalFormateado
	 */
	public String getFechaFinalFormateado() {
		this.fechaFinalFormateado = UtilidadFecha.conversionFormatoFechaAAp(this.fechaFinal);
		return fechaFinalFormateado;
	}


	/**
	 * Método que se encarga de establecer el valor
	 * del atributo fechaFinalFormateado
	 * @param fechaFinalFormateado es el valor para el atributo fechaFinalFormateado 
	 */
	public void setFechaFinalFormateado(String fechaFinalFormateado) {
		this.fechaFinalFormateado = fechaFinalFormateado;
	}


	/**
	 * Método que se encarga de obtener el 
	 * valor del atributo  razonSocial
	 *
	 * @return retorna la variable razonSocial
	 */
	public String getRazonSocial() {
		return razonSocial;
	}


	/**
	 * Método que se encarga de establecer el valor
	 * del atributo razonSocial
	 * @param razonSocial es el valor para el atributo razonSocial 
	 */
	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}


	/**
	 * Método que se encarga de obtener el 
	 * valor del atributo  tipoReporte
	 *
	 * @return retorna la variable tipoReporte
	 */
	public int getTipoReporte() {
		return tipoReporte;
	}


	/**
	 * Método que se encarga de establecer el valor
	 * del atributo tipoReporte
	 * @param tipoReporte es el valor para el atributo tipoReporte 
	 */
	public void setTipoReporte(int tipoReporte) {
		this.tipoReporte = tipoReporte;
	}
}
