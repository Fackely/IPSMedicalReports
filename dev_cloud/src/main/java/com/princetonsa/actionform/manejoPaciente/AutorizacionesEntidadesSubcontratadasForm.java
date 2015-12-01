package com.princetonsa.actionform.manejoPaciente;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadFecha;

import com.princetonsa.dto.facturacion.DtoEntidadSubcontratada;
import com.princetonsa.dto.manejoPaciente.DtoDiagnostico;
import com.princetonsa.dto.manejoPaciente.DtoSolicitudesSubCuenta;

@SuppressWarnings("serial")
public class AutorizacionesEntidadesSubcontratadasForm extends ValidatorForm {

	Logger logger = Logger.getLogger(AutorizacionesEntidadesSubcontratadasForm.class);	
	
	
	private String estado;
	private ArrayList<DtoSolicitudesSubCuenta> solicitudesPendientes;
	private ArrayList<DtoEntidadSubcontratada> entidadesAutorizadas;
	@SuppressWarnings("rawtypes")
	private HashMap parametrosFiltros;
    private String propiedadOrdenar;
	private String ultimaPropiedad;
	private String entidadEjecutaSolicitud;
	private String entidadEjecuta;
	private String entidadAutorizada;
	private String centroAtencionIngreso;
    private String nombrePacinte;
    private String tipoIdPacinte;
    private String fechaVencimiento;
	private int posSolicitud;
	private String observaciones;
    private String nivelServicio;
    private String nomEntidadAutorizada;
    private String telEntidadAutorizada;
    private String dirEntidadAutorizada;
    private String numAutorizacion;
    private String estadoAutorizacion;
    private String institucionBasica;
    private String usuarioBasico;
    @SuppressWarnings("rawtypes")
	private ArrayList listadoConvenio;
	@SuppressWarnings("rawtypes")
	private HashMap listadoCentroAtencion;
    @SuppressWarnings("rawtypes")
	private HashMap parametrosBusqueda;
	private String centroAtencionFiltro;
	private String nomcentroAtencionBusquedaRango;
	private String viaIngresoFiltro;
	@SuppressWarnings("rawtypes")
	private HashMap centrosCosto;
	@SuppressWarnings("rawtypes")
	private HashMap viasIngreso;
	private ArrayList<HashMap<String, Object>> tiposPaciente;
	private String opcionListado;
	private String linkSiguiente;
	private ArrayList <DtoEntidadSubcontratada> agrupaListadoEntidadesSubAutoriz;
	private ArrayList <Integer> codigosArticulosServicios;
	private ArrayList <DtoSolicitudesSubCuenta> ImprimirListaArticulosServicios;
	private DtoDiagnostico dtoDiagnostico;
		
	/**
	 * lista que contiene los nombres de los reportes de las autorzaciones 
	 */
	private ArrayList<String> listaNombresReportes;
	/**
	 * indicativo que indica si la solicitud es de pyp.
	 */
	private boolean solPYP=false;
	
	
	public AutorizacionesEntidadesSubcontratadasForm()
	{
		this.reset();
	}
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void reset()
	{
		this.estado=new String("");
		this.posSolicitud=ConstantesBD.codigoNuncaValido;
		this.entidadEjecuta=new String("");
		this.setEntidadEjecutaSolicitud(new String(""));
		this.entidadAutorizada=new String("");
		this.solicitudesPendientes=new ArrayList<DtoSolicitudesSubCuenta>();
		this.entidadesAutorizadas=new ArrayList<DtoEntidadSubcontratada>(); 
		this.parametrosFiltros= new HashMap();
		this.parametrosFiltros.put("operacionExitosa",ConstantesBD.acronimoNo);
		this.parametrosBusqueda=new HashMap();
		this.propiedadOrdenar=new String("");
		this.ultimaPropiedad=new String("solicitud_");
		this.centroAtencionIngreso=new String("");
		this.nombrePacinte=new String("");
		this.tipoIdPacinte=new String("");
		this.fechaVencimiento=new String(UtilidadFecha.getFechaActual());
		this.observaciones=new String("");
		this.nivelServicio=new String("");
		this.nomEntidadAutorizada=new String("");
		this.telEntidadAutorizada=new String("");
		this.dirEntidadAutorizada=new String("");
		this.numAutorizacion=new String("");
		this.estadoAutorizacion=new String("");
		this.institucionBasica=new String("");
		this.usuarioBasico=new String("");
		this.listadoConvenio=new ArrayList();
		this.listadoCentroAtencion=new HashMap();
		this.listadoCentroAtencion.put("numRegistros", "0");
		this.centrosCosto=new HashMap();
		this.centrosCosto.put("numRegistros", "0");
		this.centroAtencionFiltro=new String("");
		this.nomcentroAtencionBusquedaRango=new String("");
		this.viaIngresoFiltro=new String("");
		this.viasIngreso=new HashMap();
		this.viasIngreso.put("numRegistros", "0");
		this.tiposPaciente=new ArrayList<HashMap<String, Object>>();
		this.opcionListado=new String("");	
		this.linkSiguiente=new String("");
		this.codigosArticulosServicios= new ArrayList<Integer>();
		this.agrupaListadoEntidadesSubAutoriz= new ArrayList<DtoEntidadSubcontratada>();
		this.listaNombresReportes=new ArrayList<String>();		
		
	}
	
	public void resetDatosComunesOrden()
	{
		this.resetEntidadesAutorizadas();
		this.resetDatosAutorizacion();
		this.resetObservaciones();
	}
	
	public void resetEntidadesAutorizadas()
	{
		this.entidadesAutorizadas=new ArrayList<DtoEntidadSubcontratada>(); 
		this.nomEntidadAutorizada=new String("");
		this.entidadAutorizada=new String("");
		this.telEntidadAutorizada=new String("");
		this.dirEntidadAutorizada=new String("");
		this.agrupaListadoEntidadesSubAutoriz= new ArrayList<DtoEntidadSubcontratada>();
	}
	
	public void resetArraySolicitudesPendientes()
	{
		this.solicitudesPendientes=new ArrayList<DtoSolicitudesSubCuenta>();
	}
	
	public void resetObservaciones()
	{
		this.observaciones=new String("");
	}
	
	public void resetDatosAutorizacion()
	{
		this.numAutorizacion=new String("");
		this.estadoAutorizacion=new String("");
	}
	
	public String getEstado() {
		return estado;
	}
	
	
	public void setEstado(String estado) {
		this.estado = estado;
	}


	public ArrayList<DtoSolicitudesSubCuenta> getSolicitudesPendientes() {
		return solicitudesPendientes;
	}


	public void setSolicitudesPendientes(
			ArrayList<DtoSolicitudesSubCuenta> solicitudesPendientes) {
		this.solicitudesPendientes = solicitudesPendientes;
	}
		
	
	public String getSolicitudFiltro() {
		return this.parametrosFiltros.get("solicitudFiltro").toString();
	}
	
	@SuppressWarnings("unchecked")
	public void setSolicitudFiltro(String valor) {
		this.parametrosFiltros.put("solicitudFiltro",valor);
	}
	
	@SuppressWarnings("unchecked")
	public void setCodEntidadFiltro(String valor)
	{
		this.parametrosFiltros.put("codEntidadFiltro",valor);
	}
	
	public String getCodEntidadFiltro()
	{
		return this.parametrosFiltros.get("codEntidadFiltro").toString();
	}

	@SuppressWarnings("rawtypes")
	public HashMap getParametrosFiltros() {
		return parametrosFiltros;
	}

	@SuppressWarnings("rawtypes")
	public void setParametrosFiltros(HashMap parametrosFiltros) {
		this.parametrosFiltros = parametrosFiltros;
	}

	public Object getParametrosFiltros(String key) {
		return parametrosFiltros.get(key);
	}

	@SuppressWarnings("unchecked")
	public void setParametrosFiltros(String key, Object value) {
		this.parametrosFiltros.put(key,value);
	}


	public String getPropiedadOrdenar() {
		return propiedadOrdenar;
	}


	public void setPropiedadOrdenar(String propiedadOrdenar) {
		this.propiedadOrdenar = propiedadOrdenar;
	}


	public String getUltimaPropiedad() {
		return ultimaPropiedad;
	}


	public void setUltimaPropiedad(String ultimaPropiedad) {
		this.ultimaPropiedad = ultimaPropiedad;
	}


	public String getEntidadEjecuta() {
		return entidadEjecuta;
	}


	public void setEntidadEjecuta(String entidadEjecuta) {
		this.entidadEjecuta = entidadEjecuta;
	}


	public String getEntidadAutorizada() {
		return entidadAutorizada;
	}


	public void setEntidadAutorizada(String entidadAutorizada) {
		this.entidadAutorizada = entidadAutorizada;
	}


	public int getPosSolicitud() {
		return posSolicitud;
	}


	public void setPosSolicitud(int posSolicitud) {
		this.posSolicitud = posSolicitud;
	}


	public String getCentroAtencionIngreso() {
		return centroAtencionIngreso;
	}


	public void setCentroAtencionIngreso(String centroAtencionIngreso) {
		this.centroAtencionIngreso = centroAtencionIngreso;
	}


	public String getNombrePacinte() {
		return nombrePacinte;
	}


	public void setNombrePacinte(String nombrePacinte) {
		this.nombrePacinte = nombrePacinte;
	}


	public String getTipoIdPacinte() {
		return tipoIdPacinte;
	}


	public void setTipoIdPacinte(String tipoIdPacinte) {
		this.tipoIdPacinte = tipoIdPacinte;
	}


	public ArrayList<DtoEntidadSubcontratada> getEntidadesAutorizadas() {
		return entidadesAutorizadas;
	}


	public void setEntidadesAutorizadas(
			ArrayList<DtoEntidadSubcontratada> entidadesAutorizadas) {
		this.entidadesAutorizadas = entidadesAutorizadas;
	}


	public String getFechaVencimiento() {
		return fechaVencimiento;
	}


	public void setFechaVencimiento(String fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
	}


	public String getObservaciones() {
		return observaciones;
	}


	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}


	public String getNivelServicio() {
		return nivelServicio;
	}


	public void setNivelServicio(String nivelServicio) {
		this.nivelServicio = nivelServicio;
	}


	public String getNomEntidadAutorizada() {
		return nomEntidadAutorizada;
	}


	public void setNomEntidadAutorizada(String nomEntidadAutorizada) {
		this.nomEntidadAutorizada = nomEntidadAutorizada;
	}


	public String getNumAutorizacion() {
		return numAutorizacion;
	}


	public void setNumAutorizacion(String numAutorizacion) {
		this.numAutorizacion = numAutorizacion;
	}


	public String getEstadoAutorizacion() {
		return estadoAutorizacion;
	}


	public void setEstadoAutorizacion(String estadoAutorizacion) {
		this.estadoAutorizacion = estadoAutorizacion;
	}


	public String getInstitucionBasica() {
		return institucionBasica;
	}


	public void setInstitucionBasica(String institucionBasica) {
		this.institucionBasica = institucionBasica;
	}


	public String getUsuarioBasico() {
		return usuarioBasico;
	}


	public void setUsuarioBasico(String usuarioBasico) {
		this.usuarioBasico = usuarioBasico;
	}


	@SuppressWarnings("rawtypes")
	public ArrayList getListadoConvenio() {
		return listadoConvenio;
	}


	@SuppressWarnings("rawtypes")
	public void setListadoConvenio(ArrayList listadoConvenio) {
		this.listadoConvenio = listadoConvenio;
	}


	@SuppressWarnings("rawtypes")
	public HashMap getListadoCentroAtencion() {
		return listadoCentroAtencion;
	}


	@SuppressWarnings("rawtypes")
	public void setListadoCentroAtencion(HashMap listadoCentroAtencion) {
		this.listadoCentroAtencion = listadoCentroAtencion;
	}


	@SuppressWarnings("rawtypes")
	public HashMap getParametrosBusqueda() {
		return parametrosBusqueda;
	}


	@SuppressWarnings("rawtypes")
	public void setParametrosBusqueda(HashMap parametrosBusqueda) {
		this.parametrosBusqueda = parametrosBusqueda;
	}
     
	public Object getParametrosBusqueda(String key) {
		return parametrosBusqueda.get(key);
	}

	@SuppressWarnings("unchecked")
	public void setParametrosBusqueda(String key, Object value) {
		this.parametrosBusqueda.put(key,value);
	}

	public String getCentroAtencionFiltro() {
		return centroAtencionFiltro;
	}


	public void setCentroAtencionFiltro(String centroAtencionFiltro) {
		this.centroAtencionFiltro = centroAtencionFiltro;
	}


	@SuppressWarnings("rawtypes")
	public HashMap getCentrosCosto() {
		return centrosCosto;
	}


	@SuppressWarnings("rawtypes")
	public void setCentrosCosto(HashMap centrosCosto) {
		this.centrosCosto = centrosCosto;
	}


	@SuppressWarnings("rawtypes")
	public HashMap getViasIngreso() {
		return viasIngreso;
	}


	@SuppressWarnings("rawtypes")
	public void setViasIngreso(HashMap viasIngreso) {
		this.viasIngreso = viasIngreso;
	}


	public ArrayList<HashMap<String, Object>> getTiposPaciente() {
		return tiposPaciente;
	}


	public void setTiposPaciente(ArrayList<HashMap<String, Object>> tiposPaciente) {
		this.tiposPaciente = tiposPaciente;
	}


	public String getViaIngresoFiltro() {
		return viaIngresoFiltro;
	}


	public void setViaIngresoFiltro(String viaIngresoFiltro) {
		this.viaIngresoFiltro = viaIngresoFiltro;
	}


	public String getNomcentroAtencionBusquedaRango() {
		return nomcentroAtencionBusquedaRango;
	}


	public void setNomcentroAtencionBusquedaRango(
			String nomcentroAtencionBusquedaRango) {
		this.nomcentroAtencionBusquedaRango = nomcentroAtencionBusquedaRango;
	}


	public String getOpcionListado() {
		return opcionListado;
	}


	public void setOpcionListado(String opcionListado) {
		this.opcionListado = opcionListado;
	}


	public String getLinkSiguiente() {
		return linkSiguiente;
	}


	public void setLinkSiguiente(String linkSiguiente) {
		this.linkSiguiente = linkSiguiente;
	}

	@Override
	public ActionErrors validate(ActionMapping arg0, HttpServletRequest arg1) {
		ActionErrors errores=new ActionErrors();
		if(estado.equals("guardarAutorizacion"))
		{
			if(entidadEjecuta.equals(""))
			{
				errores.add("entidadEjecuta", new ActionMessage("errors.required", "La entidad que ejecuta"));
			}else
			{
				if(entidadEjecuta.equals(ConstantesIntegridadDominio.acronimoInterna)&&nomEntidadAutorizada.equals(""))
				{
					errores.add("entidadAutorizada", new ActionMessage("errors.required", "La entidad que autoriza"));
				}
				if(entidadEjecuta.equals(ConstantesIntegridadDominio.acronimoExterna)&&entidadAutorizada.equals(""))
				{
					errores.add("entidadAutorizada", new ActionMessage("errors.required", "La entidad que autoriza"));
				}
			}
			
			if(!errores.isEmpty())
			{
				if(entidadEjecuta.equals(""))
				{
					setEntidadEjecutaSolicitud(((DtoSolicitudesSubCuenta)getSolicitudesPendientes().get(getPosSolicitud())).getTipoEntidadEjecuta());
				}
			}
		}
		return errores;
	}


	/**
	 * @return the telEntidadAutorizada
	 */
	public String getTelEntidadAutorizada() {
		return telEntidadAutorizada;
	}


	/**
	 * @param telEntidadAutorizada the telEntidadAutorizada to set
	 */
	public void setTelEntidadAutorizada(String telEntidadAutorizada) {
		this.telEntidadAutorizada = telEntidadAutorizada;
	}


	/**
	 * @return the dirEntidadAutorizada
	 */
	public String getDirEntidadAutorizada() {
		return dirEntidadAutorizada;
	}


	/**
	 * @param dirEntidadAutorizada the dirEntidadAutorizada to set
	 */
	public void setDirEntidadAutorizada(String dirEntidadAutorizada) {
		this.dirEntidadAutorizada = dirEntidadAutorizada;
	}


	public void setAgrupaListadoEntidadesSubAutoriz(
			ArrayList <DtoEntidadSubcontratada> agrupaListadoEntidadesSubAutoriz) {
		this.agrupaListadoEntidadesSubAutoriz = agrupaListadoEntidadesSubAutoriz;
	}


	public ArrayList <DtoEntidadSubcontratada> getAgrupaListadoEntidadesSubAutoriz() {
		return agrupaListadoEntidadesSubAutoriz;
	}


	public void setCodigosArticulosServicios(
			ArrayList <Integer> codigosArticulosServicios) {
		this.codigosArticulosServicios = codigosArticulosServicios;
	}


	public ArrayList <Integer> getCodigosArticulosServicios() {
		return codigosArticulosServicios;
	}


	public void setDtoDiagnostico(DtoDiagnostico dtoDiagnostico) {
		this.dtoDiagnostico = dtoDiagnostico;
	}


	public DtoDiagnostico getDtoDiagnostico() {
		return dtoDiagnostico;
	}

	
	public ArrayList<String> getListaNombresReportes() {
		return listaNombresReportes;
	}


	public void setListaNombresReportes(ArrayList<String> listaNombresReportes) {
		this.listaNombresReportes = listaNombresReportes;
	}


	public void setSolPYP(boolean solPYP) {
		this.solPYP = solPYP;
	}


	public boolean isSolPYP() {
		return solPYP;
	}


	public void setImprimirListaArticulosServicios(
			ArrayList <DtoSolicitudesSubCuenta> imprimirListaArticulosServicios) {
		ImprimirListaArticulosServicios = imprimirListaArticulosServicios;
	}


	public ArrayList <DtoSolicitudesSubCuenta> getImprimirListaArticulosServicios() {
		return ImprimirListaArticulosServicios;
	}


	public void setEntidadEjecutaSolicitud(String entidadEjecutaSolicitud) {
		this.entidadEjecutaSolicitud = entidadEjecutaSolicitud;
	}


	public String getEntidadEjecutaSolicitud() {
		return entidadEjecutaSolicitud;
	}
		
}
