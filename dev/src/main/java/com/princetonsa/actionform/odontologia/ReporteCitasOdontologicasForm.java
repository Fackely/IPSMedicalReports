/**
 * 
 */
package com.princetonsa.actionform.odontologia;

import java.util.ArrayList;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.UtilidadFecha;

import com.princetonsa.dto.administracion.DtoEspecialidades;
import com.princetonsa.dto.administracion.DtoProfesional;
import com.princetonsa.dto.facturacion.DtoEmpresasInstitucion;
import com.princetonsa.dto.facturacion.DtoServicios;
import com.princetonsa.dto.manejoPaciente.DtoCentrosAtencion;
import com.princetonsa.dto.manejoPaciente.DtoRegionesCobertura;
import com.princetonsa.dto.tesoreria.DtoUsuarioPersona;
import com.princetonsa.enu.impresion.EnumTiposSalida;
import com.servinte.axioma.dto.administracion.DtoCiudades;
import com.servinte.axioma.dto.administracion.DtoPaises;
import com.servinte.axioma.dto.consultaExterna.DtoUnidadesConsulta;
import com.servinte.axioma.dto.odontologia.DtoFiltroReporteCitasOdontologicas;
import com.servinte.axioma.dto.odontologia.DtoResultadoReporteCitasOdontologicas;

/**
 * @author armando
 *
 */
public class ReporteCitasOdontologicasForm extends ValidatorForm 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	private String estado;
	
	/**
	 * 
	 */
	private boolean institucionMultiempresa;
	
	/*objetos para mostrar en el filtro de las busquedas* */
	
	private ArrayList<DtoPaises> paises;
	
	private ArrayList<DtoCiudades> ciudad;
	
	private ArrayList<DtoRegionesCobertura> regiones;
	
	private ArrayList<DtoEmpresasInstitucion> empresasInstitucion;
	
	private ArrayList<DtoCentrosAtencion> centrosAtencion;
	
	private ArrayList<DtoEspecialidades> especialidades;
	
	private ArrayList<DtoUnidadesConsulta> unidadesConsulta;
	
	
	private ArrayList<DtoUsuarioPersona> usuarios;
	
	private ArrayList<DtoProfesional> profesionales;
	
	private int codigoServicio=ConstantesBD.codigoNuncaValido;
	
	private String nombreServicio="";
	
	private int indiceEliminarServicio;
	
	private String indicaCadenaVaciaEstados;
	
	private String indicaCadenaVaciaTipos;
	

	/***********VARIABLES PARA LOS FILTROS DE LA CONSULTA*********/
	/**
	 * 
	 */
	private Date fechaInicial;
	
	/**
	 * 
	 */
	private Date fechaFinal;
	
	
	/**
	 * 
	 */
	private int indicePaisSeleccionado;
	
	
	/**
	 * 
	 */
	private int indiceCiudadSeleccionada;
	
	/**
	 * 
	 */
	private int indiceRegionSeleccionada;
	
	/**
	 * 
	 */
	private int indiceEmpresaInstitucionSeleccionada;
	
	/**
	 * 
	 */
	private int indiceCentroAtencionSeleccionado;
	
	
	private String[] tiposCita;
	

	private String[] estadosCita;
	
	private String cancelacionCita;
	
	private int canceladaPor;
	
	private int indiceEspecialidadUA;
	
	private int indiceUnidadAgenda;
	
	private int indiceProfesional;
	
	private int indiceUsuario;
	
	private ArrayList<DtoServicios> servicios;
	
	/**
	 * 
	 */
	private int tipoReporte;
	
	
	/**objetos de retorno en el reporte **/
	private boolean reporteGenerado=false;
	
	private ArrayList<DtoResultadoReporteCitasOdontologicas> resultadoReporte;
	
	
	private DtoFiltroReporteCitasOdontologicas filtro;
	
	/**
	 * Atributo usado para completar la ruta hacia la cual se debe 
	 * direccionar la aplicaci&oacute;n.
	 */
	private String path;
	
	/**
	 * Atributo que almacena el acr&oacute;nimo de la ubicaci&oacute;n del
	 * logo seg&uacute;n est&eacute; definido en el par&aacute;metro general.
	 */
	private String ubicacionLogo;
	
	/**
	 * Atributo que almacena la ruta del logo de la institución.
	 */
	private String rutaLogo;
	
	/**
	 * Fecha en la cual es generado el reporte.
	 */
	private String fechaActual;
	
	/**
	 * Atributo que indica el tipo de salida para el reporte.
	 */
	private String tipoSalida;
	
	/**
	 * enumeración del tipo de salida.
	 */
	private EnumTiposSalida enumTipoSalida;
	
	/**
	 * Almacena el nombre del archivo generado para luego ser visualizado
	 */
	private String nombreArchivoGenerado;
	
	@Override
	public ActionErrors validate(ActionMapping arg0, HttpServletRequest arg1) 
	{
		ActionErrors errores=new ActionErrors();
		if(this.estado.equals("generarReporte"))
		{
			if((fechaInicial==null) || 
					((fechaInicial.toString()).equals(""))){
				
				errores.add("La fecha Inicial es requerida", 
						new ActionMessage("errors.required", "El campo Fecha Inicial"));
				
			}
			
			if((fechaFinal==null) || 
						((fechaFinal.toString()).equals(""))){
				
				errores.add("La fecha Fin es requerida", 
						new ActionMessage("errors.required", "El campo Fecha Fin"));
				
			}else{
				String fechaFin = UtilidadFecha.conversionFormatoFechaAAp(
						fechaFinal);
				
				if((fechaInicial!=null) && 
						(!(fechaInicial.toString()).equals(""))){
					
					String fechaInicial = UtilidadFecha.conversionFormatoFechaAAp(
							this.fechaInicial);					
					
					
					if(UtilidadFecha.esFechaMenorQueOtraReferencia(fechaFin,fechaInicial)){
						errores.add("FECHA FIN MENOR QUE FECHA INICIAL.", new ActionMessage(
										 "errors.fechaAnteriorIgualAOtraDeReferencia"," Final "+fechaFin," Inicial "+fechaInicial));
					}
				}
			}
			if(this.indicePaisSeleccionado==ConstantesBD.codigoNuncaValido)
			{
				errores.add("error", new ActionMessage("errors.required", "El Pais"));
			}
			
			if(this.tipoReporte==ConstantesBD.codigoNuncaValido)
			{
				errores.add("error", new ActionMessage("errors.required", "El tipo de reporte"));
			}
			
			if (this.cancelacionCita.equals(ConstantesBD.acronimoSi) ) {
				if (this.canceladaPor < 0) {
					errores.add("error", new ActionMessage("errors.required", "El campo Cancelada por"));
				}
			}
		}
		return errores;
	}
	
	public void reset()
	{
		this.reporteGenerado=false;
		this.fechaInicial=null;
		this.fechaFinal=null;
		this.indiceCentroAtencionSeleccionado=ConstantesBD.codigoNuncaValido;
		this.indiceCiudadSeleccionada=ConstantesBD.codigoNuncaValido;
		this.indiceRegionSeleccionada=ConstantesBD.codigoNuncaValido;
		this.indiceEmpresaInstitucionSeleccionada=ConstantesBD.codigoNuncaValido;
		this.indicePaisSeleccionado=ConstantesBD.codigoNuncaValido;
		this.tiposCita= new String[]{};
		this.estadosCita= new String[]{};
		this.cancelacionCita=ConstantesBD.acronimoNo;
		this.canceladaPor=ConstantesBD.codigoNuncaValido;
		this.indiceEspecialidadUA=ConstantesBD.codigoNuncaValido;
		this.indiceUnidadAgenda=ConstantesBD.codigoNuncaValido;
		this.indiceProfesional=ConstantesBD.codigoNuncaValido;
		this.indiceUsuario=ConstantesBD.codigoNuncaValido;
		this.resultadoReporte=new ArrayList<DtoResultadoReporteCitasOdontologicas>();
		this.filtro=new DtoFiltroReporteCitasOdontologicas();
		this.tipoSalida = null;
		this.enumTipoSalida = null;
		this.nombreArchivoGenerado = null;
		this.estado = "";
		this.indicaCadenaVaciaEstados = ConstantesBD.acronimoSi;
		this.indicaCadenaVaciaTipos = ConstantesBD.acronimoSi;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public ArrayList<DtoPaises> getPaises() {
		return paises;
	}

	public void setPaises(ArrayList<DtoPaises> paises) {
		this.paises = paises;
	}

	public ArrayList<DtoCiudades> getCiudad() {
		return ciudad;
	}

	public void setCiudad(ArrayList<DtoCiudades> ciudad) {
		this.ciudad = ciudad;
	}

	public ArrayList<DtoRegionesCobertura> getRegiones() {
		return regiones;
	}

	public void setRegiones(ArrayList<DtoRegionesCobertura> regiones) {
		this.regiones = regiones;
	}

	public ArrayList<DtoEmpresasInstitucion> getEmpresasInstitucion() {
		return empresasInstitucion;
	}

	public void setEmpresasInstitucion(
			ArrayList<DtoEmpresasInstitucion> empresasInstitucion) {
		this.empresasInstitucion = empresasInstitucion;
	}

	public boolean isInstitucionMultiempresa() {
		return institucionMultiempresa;
	}

	public void setInstitucionMultiempresa(boolean institucionMultiempresa) {
		this.institucionMultiempresa = institucionMultiempresa;
	}

	public int getIndicePaisSeleccionado() {
		return indicePaisSeleccionado;
	}

	public void setIndicePaisSeleccionado(int indicePaisSeleccionado) {
		this.indicePaisSeleccionado = indicePaisSeleccionado;
	}

	public int getIndiceCiudadSeleccionada() {
		return indiceCiudadSeleccionada;
	}

	public void setIndiceCiudadSeleccionada(int indiceCiudadSeleccionada) {
		this.indiceCiudadSeleccionada = indiceCiudadSeleccionada;
	}


	public int getIndiceEmpresaInstitucionSeleccionada() {
		return indiceEmpresaInstitucionSeleccionada;
	}

	public void setIndiceEmpresaInstitucionSeleccionada(
			int indiceEmpresaInstitucionSeleccionada) {
		this.indiceEmpresaInstitucionSeleccionada = indiceEmpresaInstitucionSeleccionada;
	}

	public int getIndiceCentroAtencionSeleccionado() {
		return indiceCentroAtencionSeleccionado;
	}

	public void setIndiceCentroAtencionSeleccionado(
			int indiceCentroAtencionSeleccionado) {
		this.indiceCentroAtencionSeleccionado = indiceCentroAtencionSeleccionado;
	}

	public int getIndiceRegionSeleccionada() {
		return indiceRegionSeleccionada;
	}

	public void setIndiceRegionSeleccionada(int indiceRegionSeleccionada) {
		this.indiceRegionSeleccionada = indiceRegionSeleccionada;
	}

	public ArrayList<DtoCentrosAtencion> getCentrosAtencion() {
		return centrosAtencion;
	}

	public void setCentrosAtencion(ArrayList<DtoCentrosAtencion> centrosAtencion) {
		this.centrosAtencion = centrosAtencion;
	}

	public String[] getTiposCita() {
		return tiposCita;
	}
	
	
	public boolean getAyudanteTiposCita(String valor) {
		boolean respuesta=false;
		for(int i=0; i<tiposCita.length; i++)
		{
			if(tiposCita[i].equals(valor))
			{
				respuesta=true;
			}
		}
		return respuesta;
	}
	
	/**
	 * Limpia el indicativo de confirmación para que se dejen deschekear los elementos
	 * @return
	 */
	public boolean getLimpiarAyudanteTiposCita()
	{
		tiposCita=new String[0];
		return true;
	}

	public void setTiposCita(String[] tiposCita) {
		this.tiposCita = tiposCita;
	}

	public String[] getEstadosCita() {
		return estadosCita;
	}
	
	
	public boolean getAyudanteEstadosCita(String valor) {
		boolean respuesta=false;
		for(int i=0; i<estadosCita.length; i++)
		{
			if(estadosCita[i].equals(valor))
			{
				respuesta=true;
			}
		}
		return respuesta;
	}
	
	/**
	 * Limpia el indicativo de confirmación para que se dejen deschekear los elementos
	 * @return
	 */
	public boolean getLimpiarAyudanteEstadosCita()
	{
		estadosCita=new String[0];
		return true;
	}

	public void setEstadosCita(String[] estadosCita) {
		this.estadosCita = estadosCita;
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

	public int getIndiceEspecialidadUA() {
		return indiceEspecialidadUA;
	}

	public void setIndiceEspecialidadUA(int indiceEspecialidadUA) {
		this.indiceEspecialidadUA = indiceEspecialidadUA;
	}

	public int getIndiceUnidadAgenda() {
		return indiceUnidadAgenda;
	}

	public void setIndiceUnidadAgenda(int indiceUnidadAgenda) {
		this.indiceUnidadAgenda = indiceUnidadAgenda;
	}

	public int getIndiceProfesional() {
		return indiceProfesional;
	}

	public void setIndiceProfesional(int indiceProfesional) {
		this.indiceProfesional = indiceProfesional;
	}

	public int getIndiceUsuario() {
		return indiceUsuario;
	}

	public void setIndiceUsuario(int indiceUsuario) {
		this.indiceUsuario = indiceUsuario;
	}

	public ArrayList<DtoUnidadesConsulta> getUnidadesConsulta() {
		return unidadesConsulta;
	}

	public void setUnidadesConsulta(ArrayList<DtoUnidadesConsulta> unidadesConsulta) {
		this.unidadesConsulta = unidadesConsulta;
	}

	public ArrayList<DtoEspecialidades> getEspecialidades() {
		return especialidades;
	}

	public void setEspecialidades(ArrayList<DtoEspecialidades> especialidades) {
		this.especialidades = especialidades;
	}

	public ArrayList<DtoUsuarioPersona> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(ArrayList<DtoUsuarioPersona> usuarios) {
		this.usuarios = usuarios;
	}

	public ArrayList<DtoProfesional> getProfesionales() {
		return profesionales;
	}

	public void setProfesionales(ArrayList<DtoProfesional> profesionales) {
		this.profesionales = profesionales;
	}

	public ArrayList<DtoServicios> getServicios() {
		return servicios;
	}

	public void setServicios(ArrayList<DtoServicios> servicios) {
		this.servicios = servicios;
	}

	public int getCodigoServicio() {
		return codigoServicio;
	}

	public void setCodigoServicio(int codigoServicio) {
		this.codigoServicio = codigoServicio;
	}

	public String getNombreServicio() {
		return nombreServicio;
	}

	public void setNombreServicio(String nombreServicio) {
		this.nombreServicio = nombreServicio;
	}

	public int getIndiceEliminarServicio() {
		return indiceEliminarServicio;
	}

	public void setIndiceEliminarServicio(int indiceEliminarServicio) {
		this.indiceEliminarServicio = indiceEliminarServicio;
	}

	public int getTipoReporte() {
		return tipoReporte;
	}

	public void setTipoReporte(int tipoReporte) {
		this.tipoReporte = tipoReporte;
	}

	public boolean isReporteGenerado() {
		return reporteGenerado;
	}

	public void setReporteGenerado(boolean reporteGenerado) {
		this.reporteGenerado = reporteGenerado;
	}

	public ArrayList<DtoResultadoReporteCitasOdontologicas> getResultadoReporte() {
		return resultadoReporte;
	}

	public void setResultadoReporte(
			ArrayList<DtoResultadoReporteCitasOdontologicas> resultadoReporte) {
		this.resultadoReporte = resultadoReporte;
	}

	public DtoFiltroReporteCitasOdontologicas getFiltro() {
		return filtro;
	}

	public void setFiltro(DtoFiltroReporteCitasOdontologicas filtro) {
		this.filtro = filtro;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo path
	 * 
	 * @return  Retorna la variable path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo path
	 * 
	 * @param  valor para el atributo path 
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo fechaInicial
	 * 
	 * @return  Retorna la variable fechaInicial
	 */
	public Date getFechaInicial() {
		return fechaInicial;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo fechaInicial
	 * 
	 * @param  valor para el atributo fechaInicial 
	 */
	public void setFechaInicial(Date fechaInicial) {
		this.fechaInicial = fechaInicial;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo fechaFinal
	 * 
	 * @return  Retorna la variable fechaFinal
	 */
	public Date getFechaFinal() {
		return fechaFinal;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo fechaFinal
	 * 
	 * @param  valor para el atributo fechaFinal 
	 */
	public void setFechaFinal(Date fechaFinal) {
		this.fechaFinal = fechaFinal;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo ubicacionLogo
	 * 
	 * @return  Retorna la variable ubicacionLogo
	 */
	public String getUbicacionLogo() {
		return ubicacionLogo;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo ubicacionLogo
	 * 
	 * @param  valor para el atributo ubicacionLogo 
	 */
	public void setUbicacionLogo(String ubicacionLogo) {
		this.ubicacionLogo = ubicacionLogo;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo rutaLogo
	 * 
	 * @return  Retorna la variable rutaLogo
	 */
	public String getRutaLogo() {
		return rutaLogo;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo rutaLogo
	 * 
	 * @param  valor para el atributo rutaLogo 
	 */
	public void setRutaLogo(String rutaLogo) {
		this.rutaLogo = rutaLogo;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo fechaActual
	 * 
	 * @return  Retorna la variable fechaActual
	 */
	public String getFechaActual() {
		return fechaActual;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo fechaActual
	 * 
	 * @param  valor para el atributo fechaActual 
	 */
	public void setFechaActual(String fechaActual) {
		this.fechaActual = fechaActual;
	}

	public String getTipoSalida() {
		return tipoSalida;
	}

	public void setTipoSalida(String tipoSalida) {
		this.tipoSalida = tipoSalida;
	}

	public EnumTiposSalida getEnumTipoSalida() {
		return enumTipoSalida;
	}

	public void setEnumTipoSalida(EnumTiposSalida enumTipoSalida) {
		this.enumTipoSalida = enumTipoSalida;
	}

	public String getNombreArchivoGenerado() {
		return nombreArchivoGenerado;
	}

	public void setNombreArchivoGenerado(String nombreArchivoGenerado) {
		this.nombreArchivoGenerado = nombreArchivoGenerado;
	}

	/**
	 * Método que se encarga de obtener el 
	 * valor del atributo  indicaCadenaVaciaEstados
	 *
	 * @return retorna la variable indicaCadenaVaciaEstados
	 */
	public String getIndicaCadenaVaciaEstados() {
		return indicaCadenaVaciaEstados;
	}

	/**
	 * Método que se encarga de establecer el valor
	 * del atributo indicaCadenaVaciaEstados
	 * @param indicaCadenaVaciaEstados es el valor para el atributo indicaCadenaVaciaEstados 
	 */
	public void setIndicaCadenaVaciaEstados(String indicaCadenaVaciaEstados) {
		this.indicaCadenaVaciaEstados = indicaCadenaVaciaEstados;
	}

	/**
	 * Método que se encarga de obtener el 
	 * valor del atributo  indicaCadenaVaciaTipos
	 *
	 * @return retorna la variable indicaCadenaVaciaTipos
	 */
	public String getIndicaCadenaVaciaTipos() {
		return indicaCadenaVaciaTipos;
	}

	/**
	 * Método que se encarga de establecer el valor
	 * del atributo indicaCadenaVaciaTipos
	 * @param indicaCadenaVaciaTipos es el valor para el atributo indicaCadenaVaciaTipos 
	 */
	public void setIndicaCadenaVaciaTipos(String indicaCadenaVaciaTipos) {
		this.indicaCadenaVaciaTipos = indicaCadenaVaciaTipos;
	}
}
