package com.princetonsa.actionform.manejoPaciente;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import util.ConstantesBD;

import com.princetonsa.dto.facturacion.DtoEntidadSubcontratada;
import com.princetonsa.dto.inventario.DtoArticulosAutorizaciones;
import com.princetonsa.dto.inventario.DtoServiciosAutorizaciones;
import com.princetonsa.dto.manejoPaciente.DTOAutorEntidadSubcontratadaCapitacion;
import com.princetonsa.dto.manejoPaciente.DTOAutorizacionIngresoEstancia;
import com.servinte.axioma.dto.capitacion.NivelAutorizacionDto;
import com.servinte.axioma.dto.facturacion.EntidadSubContratadaDto;
import com.servinte.axioma.dto.manejoPaciente.AutorizacionCapitacionDto;

/**
 * Esta clase se encarga de obtener los datos ingresados
 * por el usuario y mapearlos a los atributos asignados a
 * cada propiedad respectiva.
 * 
 * @author Angela Maria Aguirre
 * @since 09/01/2011
 */
public class AdministracionAutorizacionCapitacionDetalleForm extends
		ActionForm {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Atributo usado para definir la acción que se está
	 * realizando
	 */
	private String estado="";
	
	
	/**
	 * Atributo que almacena el código de la autorización seleccionada para ver su detalle
	 */
	private long codigoAutorizacion;	
	
	/**
	 * Atributo que almacena los datos de la autorización de capitación
	 */
	private DTOAutorEntidadSubcontratadaCapitacion dtoAutorizacionCapitacion;
	
	
	/**
	 * Atributo que almacena los datos de la autorización de ingreso estancia
	 */
	private DTOAutorizacionIngresoEstancia dtoAutorizacionIngEstancia;
	
	/**
	 * Atributo que determina si la autorizacion es de ingreso estancia o por solicitud
	 */
	private String tipoAutorizacion;
	
	/**
	 * Atributo que contiene los servicios de la autorización
	 */	
	private List<DtoServiciosAutorizaciones> listaServicios;
	
	/**
	 * Atributo que contiene los artículos de la autorización
	 */
	private List<DtoArticulosAutorizaciones> listaArticulos;
	
	/**
	 * Atributo que determina a que página debe volver la funcionlidad
	 */
	private String paginaRedireccion;	
	
	/**
	 * Atributo que almacena la entidad subcontratada seleccionada
	 * en el proceso de autorización temporal
	 */
	private DtoEntidadSubcontratada entidadSubSeleccionada;
	
	/**
	 * Atrintuo que contiene los días de estancia ingresados por el usuario
	 */
	private int diasEstanciaActualizados;
	
	private boolean procesoExitoso;
	
	private long indiceEntidadSub;
	
	private List<DtoEntidadSubcontratada> listadoEntidadesSub;
	
	private String abrirPopPupProrroga;
	
	private String fechaProrrogaServicio;
	
	private String fechaProrrogaArticulo;
	
	private String abrirPopPupAnulacion;
	
	private String abrirPopUpModificacionDiasEstancia;
	
	private String abrirPopUpConfirmarAutoEntSub;
	
	private String motivoAnulacion;
	
	private String fechaActual;
	
	private String nombreUsuarioAnula;
	
	private String abrirPopPupEntidadesSub;
	
	private boolean mostrarBotonProrroga;
	
	private boolean mostrarBotonAnular;
	
	private boolean imprimirAutorizacion;
	
	private String nombreReporte;
	
	private boolean mostrarBotonAutorizarEntSub;
	
	private List<NivelAutorizacionDto>nivelesAutorizacionUsuario=new ArrayList<NivelAutorizacionDto>(0);
	private List<EntidadSubContratadaDto>entidadesSubcontratadas=new ArrayList<EntidadSubContratadaDto>(0);
	
	/**
	 * Autorizacion que contiene los servicios discriminados por orden o solicitud
	 *  
	 */
	private AutorizacionCapitacionDto autorizacionCapitacionDto= new AutorizacionCapitacionDto();
	
	/**
	 * Atributo que almacena la bandera que permite mostrar popup cuando es la primera impresion de la autorizacion 
	 */
	private boolean mostrarPopupPrimeraImpresion;
	
	/**
	 * Atributo que almacena la bandera que permite identificar en la impresion si es (true: original, false:copia, null:nunguna de las anteriores)
	 */
	private Boolean esImpresionOriginal;
	
	/**
	 * Atributo que almacena el nombre de la persona quien recibe la autorizacion
	 */
	private String personaRecibeAutorizacion;
	
	/**
	 * Atributo que almacena las observaciones en la entrega de la autorizacion
	 */
	private String observacionesEntregaAutorizacion;
	
	/**
	 * 
	 * Este método se encarga de inicializar los valores de la 
	 * página de administración de las autorizaciones de capitación por paciente
	 * 
	 * @author, Angela Maria Aguirre
	 *
	 */
	public void reset(){	
		this.dtoAutorizacionCapitacion = new DTOAutorEntidadSubcontratadaCapitacion();
		this.dtoAutorizacionIngEstancia = new DTOAutorizacionIngresoEstancia();
		this.autorizacionCapitacionDto= new AutorizacionCapitacionDto();
		this.listaServicios = new ArrayList<DtoServiciosAutorizaciones>();
		this.listaArticulos = new ArrayList<DtoArticulosAutorizaciones>();
		this.diasEstanciaActualizados = ConstantesBD.codigoNuncaValido;
		this.procesoExitoso = false;
		this.indiceEntidadSub = ConstantesBD.codigoNuncaValidoLong;
		this.listadoEntidadesSub = new ArrayList<DtoEntidadSubcontratada>();
		this.entidadSubSeleccionada = new DtoEntidadSubcontratada();
		this.entidadSubSeleccionada.setCodigoPk(ConstantesBD.codigoNuncaValidoLong);
		this.abrirPopPupProrroga =ConstantesBD.acronimoNo;
		this.abrirPopPupAnulacion =ConstantesBD.acronimoNo;
		this.abrirPopUpModificacionDiasEstancia =ConstantesBD.acronimoNo;
		this.abrirPopUpConfirmarAutoEntSub =ConstantesBD.acronimoNo;
		this.motivoAnulacion ="";
		this.fechaActual ="";
		this.nombreUsuarioAnula="";
		this.abrirPopPupEntidadesSub=ConstantesBD.acronimoNo;
		this.fechaProrrogaArticulo="";
		this.fechaProrrogaServicio="";
		this.mostrarBotonProrroga=false;
		this.mostrarBotonAnular=false;
		this.imprimirAutorizacion=false;
		this.mostrarBotonAutorizarEntSub=false;
		this.nombreReporte="";
		this.mostrarPopupPrimeraImpresion = false;
		this.esImpresionOriginal = null;
		this.personaRecibeAutorizacion  = "";
		this.observacionesEntregaAutorizacion = "";
	}
	
	
	/**
	 * 
	 * Este m&eacute;todo se encarga de realizar las validaciones de 
	 * los datos ingresados por el usuario
	 * 
	 * @param ActionMapping
	 * @param HttpServletRequest
	 * @return ActionErrors
	 * 
	 * @author, Angela Maria Aguirre
	 *
	 */
	@Override
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
		ActionErrors errores=null;
		errores=new ActionErrors();
		
		
		return errores;
		
	}


	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo estado
	
	 * @return retorna la variable estado 
	 * @author Angela Maria Aguirre 
	 */
	public String getEstado() {
		return estado;
	}


	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo estado
	
	 * @param valor para el atributo estado 
	 * @author Angela Maria Aguirre 
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}


	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo codigoAutorizacion
	
	 * @return retorna la variable codigoAutorizacion 
	 * @author Angela Maria Aguirre 
	 */
	public long getCodigoAutorizacion() {
		return codigoAutorizacion;
	}


	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo codigoAutorizacion
	
	 * @param valor para el atributo codigoAutorizacion 
	 * @author Angela Maria Aguirre 
	 */
	public void setCodigoAutorizacion(long codigoAutorizacion) {
		this.codigoAutorizacion = codigoAutorizacion;
	}


	public DTOAutorEntidadSubcontratadaCapitacion getDtoAutorizacionCapitacion() {
		return dtoAutorizacionCapitacion;
	}


	public void setDtoAutorizacionCapitacion(
			DTOAutorEntidadSubcontratadaCapitacion dtoAutorizacionCapitacion) {
		this.dtoAutorizacionCapitacion = dtoAutorizacionCapitacion;
	}


	public DTOAutorizacionIngresoEstancia getDtoAutorizacionIngEstancia() {
		return dtoAutorizacionIngEstancia;
	}


	public void setDtoAutorizacionIngEstancia(
			DTOAutorizacionIngresoEstancia dtoAutorizacionIngEstancia) {
		this.dtoAutorizacionIngEstancia = dtoAutorizacionIngEstancia;
	}


	public String getTipoAutorizacion() {
		return tipoAutorizacion;
	}


	public void setTipoAutorizacion(String tipoAutorizacion) {
		this.tipoAutorizacion = tipoAutorizacion;
	}


	public List<DtoServiciosAutorizaciones> getListaServicios() {
		return listaServicios;
	}


	public void setListaServicios(
			List<DtoServiciosAutorizaciones> listaServicios) {
		this.listaServicios = listaServicios;
	}


	public List<DtoArticulosAutorizaciones> getListaArticulos() {
		return listaArticulos;
	}


	public void setListaArticulos(
			List<DtoArticulosAutorizaciones> listaArticulos) {
		this.listaArticulos = listaArticulos;
	}


	public String getPaginaRedireccion() {
		return paginaRedireccion;
	}


	public void setPaginaRedireccion(String paginaRedireccion) {
		this.paginaRedireccion = paginaRedireccion;
	}


	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo diasEstanciaActualizados
	
	 * @return retorna la variable diasEstanciaActualizados 
	 * @author Angela Maria Aguirre 
	 */
	public int getDiasEstanciaActualizados() {
		return diasEstanciaActualizados;
	}


	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo diasEstanciaActualizados
	
	 * @param valor para el atributo diasEstanciaActualizados 
	 * @author Angela Maria Aguirre 
	 */
	public void setDiasEstanciaActualizados(int diasEstanciaActualizados) {
		this.diasEstanciaActualizados = diasEstanciaActualizados;
	}


	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo procesoExitoso
	
	 * @return retorna la variable procesoExitoso 
	 * @author Angela Maria Aguirre 
	 */
	public boolean isProcesoExitoso() {
		return procesoExitoso;
	}


	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo procesoExitoso
	
	 * @param valor para el atributo procesoExitoso 
	 * @author Angela Maria Aguirre 
	 */
	public void setProcesoExitoso(boolean procesoExitoso) {
		this.procesoExitoso = procesoExitoso;
	}


	/**
	 * @return the indiceEntidadSub
	 */
	public long getIndiceEntidadSub() {
		return indiceEntidadSub;
	}


	/**
	 * @param indiceEntidadSub the indiceEntidadSub to set
	 */
	public void setIndiceEntidadSub(long indiceEntidadSub) {
		this.indiceEntidadSub = indiceEntidadSub;
	}


	/**
	 * @return the listadoEntidadesSub
	 */
	public List<DtoEntidadSubcontratada> getListadoEntidadesSub() {
		return listadoEntidadesSub;
	}


	/**
	 * @param listadoEntidadesSub the listadoEntidadesSub to set
	 */
	public void setListadoEntidadesSub(
			List<DtoEntidadSubcontratada> listadoEntidadesSub) {
		this.listadoEntidadesSub = listadoEntidadesSub;
	}


	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo entidadSubSeleccionada
	
	 * @return retorna la variable entidadSubSeleccionada 
	 * @author Angela Maria Aguirre 
	 */
	public DtoEntidadSubcontratada getEntidadSubSeleccionada() {
		return entidadSubSeleccionada;
	}


	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo entidadSubSeleccionada
	
	 * @param valor para el atributo entidadSubSeleccionada 
	 * @author Angela Maria Aguirre 
	 */
	public void setEntidadSubSeleccionada(
			DtoEntidadSubcontratada entidadSubSeleccionada) {
		this.entidadSubSeleccionada = entidadSubSeleccionada;
	}


	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo abrirPopPupProrroga
	
	 * @return retorna la variable abrirPopPupProrroga 
	 * @author Angela Maria Aguirre 
	 */
	public String getAbrirPopPupProrroga() {
		return abrirPopPupProrroga;
	}


	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo abrirPopPupProrroga
	
	 * @param valor para el atributo abrirPopPupProrroga 
	 * @author Angela Maria Aguirre 
	 */
	public void setAbrirPopPupProrroga(String abrirPopPupProrroga) {
		this.abrirPopPupProrroga = abrirPopPupProrroga;
	}


	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo abrirPopPupAnulacion
	
	 * @return retorna la variable abrirPopPupAnulacion 
	 * @author Angela Maria Aguirre 
	 */
	public String getAbrirPopPupAnulacion() {
		return abrirPopPupAnulacion;
	}


	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo abrirPopPupAnulacion
	
	 * @param valor para el atributo abrirPopPupAnulacion 
	 * @author Angela Maria Aguirre 
	 */
	public void setAbrirPopPupAnulacion(String abrirPopPupAnulacion) {
		this.abrirPopPupAnulacion = abrirPopPupAnulacion;
	}

	
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo abrirPopUpModificacionDiasEstancia
	 * 
	 * @param abrirPopUpModificacionDiasEstancia
	 * @author jeilones
	 * @created 14/08/2012
	 */
	public String getAbrirPopUpModificacionDiasEstancia() {
		return this.abrirPopUpModificacionDiasEstancia;
	}
	
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo abrirPopUpModificacionDiasEstancia
	 * 
	 * @param abrirPopUpModificacionDiasEstancia
	 * @author jeilones
	 * @created 14/08/2012
	 */
	public void setAbrirPopUpModificacionDiasEstancia(String abrirPopUpModificacionDiasEstancia) {
		this.abrirPopUpModificacionDiasEstancia = abrirPopUpModificacionDiasEstancia;
	}
	/**
	 * @return the abrirPopUpConfirmarAutoEntSub
	 * @author jeilones
	 * @created 15/08/2012
	 */
	public String getAbrirPopUpConfirmarAutoEntSub() {
		return abrirPopUpConfirmarAutoEntSub;
	}


	/**
	 * @param abrirPopUpConfirmarAutoEntSub the abrirPopUpConfirmarAutoEntSub to set
	 * @author jeilones
	 * @created 15/08/2012
	 */
	public void setAbrirPopUpConfirmarAutoEntSub(
			String abrirPopUpConfirmarAutoEntSub) {
		this.abrirPopUpConfirmarAutoEntSub = abrirPopUpConfirmarAutoEntSub;
	}
	
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo motivoAnulacion
	
	 * @return retorna la variable motivoAnulacion 
	 * @author Angela Maria Aguirre 
	 */
	public String getMotivoAnulacion() {
		return motivoAnulacion;
	}


	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo motivoAnulacion
	
	 * @param valor para el atributo motivoAnulacion 
	 * @author Angela Maria Aguirre 
	 */
	public void setMotivoAnulacion(String motivoAnulacion) {
		this.motivoAnulacion = motivoAnulacion;
	}


	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo fechaActual
	
	 * @return retorna la variable fechaActual 
	 * @author Angela Maria Aguirre 
	 */
	public String getFechaActual() {
		return fechaActual;
	}


	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo fechaActual
	
	 * @param valor para el atributo fechaActual 
	 * @author Angela Maria Aguirre 
	 */
	public void setFechaActual(String fechaActual) {
		this.fechaActual = fechaActual;
	}


	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo nombreUsuarioAnula
	
	 * @return retorna la variable nombreUsuarioAnula 
	 * @author Angela Maria Aguirre 
	 */
	public String getNombreUsuarioAnula() {
		return nombreUsuarioAnula;
	}


	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo nombreUsuarioAnula
	
	 * @param valor para el atributo nombreUsuarioAnula 
	 * @author Angela Maria Aguirre 
	 */
	public void setNombreUsuarioAnula(String nombreUsuarioAnula) {
		this.nombreUsuarioAnula = nombreUsuarioAnula;
	}


	/**
	 * @return the abrirPopPupEntidadesSub
	 */
	public String getAbrirPopPupEntidadesSub() {
		return abrirPopPupEntidadesSub;
	}


	/**
	 * @param abrirPopPupEntidadesSub the abrirPopPupEntidadesSub to set
	 */
	public void setAbrirPopPupEntidadesSub(String abrirPopPupEntidadesSub) {
		this.abrirPopPupEntidadesSub = abrirPopPupEntidadesSub;
	}


	/**
	 * @return the fechaProrrogaServicio
	 */
	public String getFechaProrrogaServicio() {
		return fechaProrrogaServicio;
	}


	/**
	 * @param fechaProrrogaServicio the fechaProrrogaServicio to set
	 */
	public void setFechaProrrogaServicio(String fechaProrrogaServicio) {
		this.fechaProrrogaServicio = fechaProrrogaServicio;
	}


	/**
	 * @return the fechaProrrogaArticulo
	 */
	public String getFechaProrrogaArticulo() {
		return fechaProrrogaArticulo;
	}


	/**
	 * @param fechaProrrogaArticulo the fechaProrrogaArticulo to set
	 */
	public void setFechaProrrogaArticulo(String fechaProrrogaArticulo) {
		this.fechaProrrogaArticulo = fechaProrrogaArticulo;
	}


	/**
	 * @return the mostrarBotonProrroga
	 */
	public boolean isMostrarBotonProrroga() {
		return mostrarBotonProrroga;
	}


	/**
	 * @param mostrarBotonProrroga the mostrarBotonProrroga to set
	 */
	public void setMostrarBotonProrroga(boolean mostrarBotonProrroga) {
		this.mostrarBotonProrroga = mostrarBotonProrroga;
	}


	/**
	 * @return the imprimirAutorizacion
	 */
	public boolean isImprimirAutorizacion() {
		return imprimirAutorizacion;
	}


	/**
	 * @param imprimirAutorizacion the imprimirAutorizacion to set
	 */
	public void setImprimirAutorizacion(boolean imprimirAutorizacion) {
		this.imprimirAutorizacion = imprimirAutorizacion;
	}


	/**
	 * @return the nombreReporte
	 */
	public String getNombreReporte() {
		return nombreReporte;
	}


	/**
	 * @param nombreReporte the nombreReporte to set
	 */
	public void setNombreReporte(String nombreReporte) {
		this.nombreReporte = nombreReporte;
	}


	/**
	 * @return the mostrarBotonAnular
	 */
	public boolean isMostrarBotonAnular() {
		return mostrarBotonAnular;
	}


	/**
	 * @param mostrarBotonAnular the mostrarBotonAnular to set
	 */
	public void setMostrarBotonAnular(boolean mostrarBotonAnular) {
		this.mostrarBotonAnular = mostrarBotonAnular;
	}


	/**
	 * @return the mostrarBotonAutorizarEntSub
	 */
	public boolean isMostrarBotonAutorizarEntSub() {
		return mostrarBotonAutorizarEntSub;
	}


	/**
	 * @param mostrarBotonAutorizarEntSub the mostrarBotonAutorizarEntSub to set
	 */
	public void setMostrarBotonAutorizarEntSub(boolean mostrarBotonAutorizarEntSub) {
		this.mostrarBotonAutorizarEntSub = mostrarBotonAutorizarEntSub;
	}


	/**
	 * @return the nivelesAutorizacionUsuario
	 */
	public List<NivelAutorizacionDto> getNivelesAutorizacionUsuario() {
		return nivelesAutorizacionUsuario;
	}


	/**
	 * @param nivelesAutorizacionUsuario the nivelesAutorizacionUsuario to set
	 */
	public void setNivelesAutorizacionUsuario(
			List<NivelAutorizacionDto> nivelesAutorizacionUsuario) {
		this.nivelesAutorizacionUsuario = nivelesAutorizacionUsuario;
	}


	/**
	 * @return the entidadesSubcontratadas
	 */
	public List<EntidadSubContratadaDto> getEntidadesSubcontratadas() {
		return entidadesSubcontratadas;
	}


	/**
	 * @param entidadesSubcontratadas the entidadesSubcontratadas to set
	 */
	public void setEntidadesSubcontratadas(
			List<EntidadSubContratadaDto> entidadesSubcontratadas) {
		this.entidadesSubcontratadas = entidadesSubcontratadas;
	}


	/**
	 * @return the autorizacionCapitacionDto
	 */
	public AutorizacionCapitacionDto getAutorizacionCapitacionDto() {
		return autorizacionCapitacionDto;
	}


	/**
	 * @param autorizacionCapitacionDto the autorizacionCapitacionDto to set
	 */
	public void setAutorizacionCapitacionDto(
			AutorizacionCapitacionDto autorizacionCapitacionDto) {
		this.autorizacionCapitacionDto = autorizacionCapitacionDto;
	}

	/**
	 * @return the mostrarPopupPrimeraImpresion
	 */
	public boolean isMostrarPopupPrimeraImpresion() {
		return mostrarPopupPrimeraImpresion;
	}

	/**
	 * @param mostrarPopupPrimeraImpresion the mostrarPopupPrimeraImpresion to set
	 */
	public void setMostrarPopupPrimeraImpresion(boolean mostrarPopupPrimeraImpresion) {
		this.mostrarPopupPrimeraImpresion = mostrarPopupPrimeraImpresion;
	}

	/**
	 * @return the esImpresionOriginal
	 */
	public Boolean getEsImpresionOriginal() {
		return esImpresionOriginal;
	}

	/**
	 * @param esImpresionOriginal the esImpresionOriginal to set
	 */
	public void setEsImpresionOriginal(Boolean esImpresionOriginal) {
		this.esImpresionOriginal = esImpresionOriginal;
	}

	/**
	 * @return the personaRecibeAutorizacion
	 */
	public String getPersonaRecibeAutorizacion() {
		return personaRecibeAutorizacion;
	}

	/**
	 * @param personaRecibeAutorizacion the personaRecibeAutorizacion to set
	 */
	public void setPersonaRecibeAutorizacion(String personaRecibeAutorizacion) {
		this.personaRecibeAutorizacion = personaRecibeAutorizacion;
	}

	/**
	 * @return the observacionesEntregaAutorizacion
	 */
	public String getObservacionesEntregaAutorizacion() {
		return observacionesEntregaAutorizacion;
	}

	/**
	 * @param observacionesEntregaAutorizacion the observacionesEntregaAutorizacion to set
	 */
	public void setObservacionesEntregaAutorizacion(
			String observacionesEntregaAutorizacion) {
		this.observacionesEntregaAutorizacion = observacionesEntregaAutorizacion;
	}
}
