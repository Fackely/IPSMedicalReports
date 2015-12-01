package com.princetonsa.actionform.tesoreria;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import util.ConstantesBD;
import util.UtilidadFecha;

import com.princetonsa.dto.administracion.DtoIntegridadDominio;
import com.princetonsa.dto.manejoPaciente.DtoCentrosAtencion;
import com.princetonsa.dto.tesoreria.DtoConsolidadoMovimiento;
import com.princetonsa.dto.tesoreria.DtoConsultaTrasladosCajasRecaudo;
import com.princetonsa.dto.tesoreria.DtoInformacionEntrega;
import com.princetonsa.dto.tesoreria.DtoUsuarioPersona;
import com.servinte.axioma.orm.Cajas;

/**
 * 
 * Esta clase se encarga de obtener los datos ingresados
 * por el usuario y mapearlos a los atributos asignados a
 * cada uno.
 *
 * @author Yennifer Guerrero
 * @since  02/08/2010
 *
 */
public class ConsultaTrasladosCajasRecaudoForm extends ActionForm {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Atributo usado para completar la ruta hacia la cual se debe 
	 * direccionar la aplicaci&oacute;n.
	 */
	private String path;
	
	/**
	 * Atributo que almacena la acci&oacute;n que debe realizar la p&aacute;gina en
	 * un determinado momento.
	 */
	private String estado;
	
	/**
	 * Atributo usado para almacenar el listado con los centros de atenci&oacute;n
	 */
	private ArrayList<DtoCentrosAtencion> listaCentrosAtencion;
	
	/**
	 * Atributo usado para listar el estado del traslado de caja recaudo.
	 */
	private ArrayList<DtoIntegridadDominio> listadoEstadoTraslado;
	
	/**
	 * Atributo que contiene la lista de las cajas de origen del traslado
	 */
	private ArrayList<Cajas> listaCajaOrigen;
	
	/**
	 * Atributo que contiene la lista de las cajas destino del traslado
	 */
	private ArrayList<Cajas> listaCajaDestino;
	
	/**
	 * Atributo que contiene la lista de los cajeros de donde se
	 * realiza el traslado
	 */
	private ArrayList<DtoUsuarioPersona> listaCajeroSolicitante;
	
	/**
	 * Atributo que contiene la lista de los cajeros que aceptan
	 * el traslado	
	 */	
	private ArrayList<DtoUsuarioPersona> listaCajeroAcepta;
	
	/**
	 * Atributo que contiene los filtros de b&uacute;squeda seleccionados
	 * por el usuario
	 */
	private DtoConsultaTrasladosCajasRecaudo filtroSolicitud;
		
	/**
	 * Atributo que almacena los resultados de la búsqueda de las solicitudes
	 * de traslados de cajas de recaudo
	 */
	private ArrayList<DtoConsultaTrasladosCajasRecaudo> listaRegistrosSolicitud;
	
	/**
	 * Atributo que almacena el &iacute;ndice del registro seleccionado por
	 * el usuario
	 */
	private int index;
	
	/**
	 * atributo usado para la paginaci&oacute;n del listado de faltantes sobrantes encontrados.
	 */
	private int posArray;
	
	/**
	 * Atributo que alamcena el nombre de la columna por la cual deben ser
	 * ordenados los registros encontrados.
	 */
	private String patronOrdenar;
	
	/**
	 * Atributo usado para ordenar descendentemente.
	 */
	private String esDescendente;
	
	/**
	 * Almacena el registro que el usuario ha seleccionado para mostrar el detalle de la 
	 * solicitud seleccionada.
	 */
	private DtoConsultaTrasladosCajasRecaudo dtoDetalle;
	
	/**
	 * Contiene toda la informacion del detalle de la solicitud de
	 * traslado a caja de recaudo.
	 */
	private DtoInformacionEntrega dtoInformacionEntrega;
	
	/**
	 * Almacena un consolidado de la informaci&oacute;n referente a las solicitudes 
	 * de traslado de caja y las formas de pago.
	 */
	private DtoConsolidadoMovimiento consolidadoMovimientoDTO;
	
	/**
	 * Atributo que permite mostrar la secci&oacute;n de totales entregados.
	 */
	private String mostrarParaSeccionEspecial;
	
	/**
	 * Atributo que permite mostrar la secci&oacute;n de totales entregados.
	 */
	private String consulta;
	
	/**
	 * Atributo que permite identificar si se ha seleccionado un adjunto.
	 */
	private String activo;
	
	/**
	 * Atributo que indica si la institucion es multiempresa o no.
	 */
	private String esMultiEmpresa;
	
	private String ubicacionLogo;
	
	/**
	 * Atributo que permite habilitar o no el listado de 
	 * cajero que acepta.
	 */
	private boolean deshabilitaCajeroAcepta;
	
	/**
	 * Atributo que almacena el estado hacia el cual debe dirigirse la 
	 * regla de navegaci&oacute;n dependiendo del n&uacute;mero registros 
	 * encontrados.
	 */
	private String reglaNavegacion;
	
	/**
	 * Atributo que indica si el archivo fue adjuntado de forma exitosa.
	 */
	private String confirmado;
	
	/**
	 * Este m&eacutetodo se encarga de realizar las validaciones de 
	 * los datos ingresados por el usuario
	 * 
	 * @param ActionMapping
	 * @param HttpServletRequest
	 * @return ActionErrors
	 * 
	 *  @author Yennifer Guerrero
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {		
		ActionErrors errores=null;
		errores=new ActionErrors();
		if(estado.equals("buscarRegistros")){
			if((filtroSolicitud.getFechaInicial()==null) || 
					((filtroSolicitud.getFechaInicial().toString()).equals(""))){
				
				errores.add("La fecha Inicial es requerida", 
						new ActionMessage("errors.required", "El campo Fecha Inicial"));
				
			}else{
				String fechaInicial = UtilidadFecha.conversionFormatoFechaAAp(
						filtroSolicitud.getFechaInicial());
				String fechaActual = UtilidadFecha.conversionFormatoFechaAAp(
						Calendar.getInstance().getTime());
				
				if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(
						fechaInicial, fechaActual)){
							
					 errores.add("FECHA INICIAL MAYOR QUE FECHA ACTUAL.", new ActionMessage(
									 "errors.fechaPosteriorIgualActual"," Inicial "+fechaInicial," Actual "+fechaActual));	
				}
			}
			if((filtroSolicitud.getFechaFin()==null) || 
						((filtroSolicitud.getFechaFin().toString()).equals(""))){
				
				errores.add("La fecha Fin es requerida", 
						new ActionMessage("errors.required", "El campo Fecha Fin"));
				
			}else{
				String fechaFin = UtilidadFecha.conversionFormatoFechaAAp(
						filtroSolicitud.getFechaFin());
				
				String fechaActual = UtilidadFecha.conversionFormatoFechaAAp(
						Calendar.getInstance().getTime());
				
				
				if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(fechaFin, fechaActual)){
					errores.add("La fecha Fin es mayor que fecha actual", 
							new ActionMessage("errors.fechaPosteriorIgualActual", " Fin "+fechaFin," Actual "+fechaActual));
				}
				if((filtroSolicitud.getFechaInicial()!=null) && 
						(!(filtroSolicitud.getFechaInicial().toString()).equals(""))){
					
					String fechaInicial = UtilidadFecha.conversionFormatoFechaAAp(
							filtroSolicitud.getFechaInicial());					
					
					
					if(UtilidadFecha.esFechaMenorQueOtraReferencia(fechaFin,fechaInicial)){
						errores.add("FECHA FIN MENOR QUE FECHA INICIAL.", new ActionMessage(
										 "errors.fechaAnteriorIgualAOtraDeReferencia"," Final "+fechaFin," Inicial "+fechaInicial));
					}
				}
			}
			if ((filtroSolicitud.getFechaInicial()!=null) && !((filtroSolicitud.getFechaInicial().toString()).equals(""))&&
				(filtroSolicitud.getFechaFin()!=null) && !((filtroSolicitud.getFechaFin().toString()).equals(""))) {
				try {
					
					String fechaInicial = UtilidadFecha.conversionFormatoFechaAAp(
							filtroSolicitud.getFechaInicial());	
					
					String fechaFin = UtilidadFecha.conversionFormatoFechaAAp(
							filtroSolicitud.getFechaFin());
					
					long anio = UtilidadFecha.obtenerDiferenciaEntreFechas(fechaInicial,fechaFin, 5);
					
					if (anio> 365) {
						errores.add("EL RANGO MAXIMO ENTRE LAS FECHAS DEBE SER INFERIOR A UN AÑO.", new ActionMessage(
								 "errors.rangoEntreFechasInvalido"," Final "+fechaFin," Inicial "+fechaInicial));
					}
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			if(filtroSolicitud.getConsecutivoCentroAtencion()==ConstantesBD.codigoNuncaValido){
				errores.add("Centro de Atencion Requerido.", new ActionMessage(
						 "errores.modTesoreria.centroAtencionRequerido"));
			}
		}			
		return errores;
	}
	
	/**
	 * Este m&eacute;todo se encarga de inicializar los valores de 
	 * la p&aacute;gina de consulta de los traslados de caja recaudo.
	 * 
	 * @author Yennifer Guerrero
	 */
	public void reset(){		
		filtroSolicitud = new DtoConsultaTrasladosCajasRecaudo();
		index=0;	
		estado="";
		listaCajaDestino= new ArrayList<Cajas>();
		listaCajaOrigen= new ArrayList<Cajas>();
		listaCajeroAcepta= new ArrayList<DtoUsuarioPersona>();
		listaCajeroSolicitante= new ArrayList<DtoUsuarioPersona>();
		listaCentrosAtencion=new ArrayList<DtoCentrosAtencion>();
		listaRegistrosSolicitud = new ArrayList<DtoConsultaTrasladosCajasRecaudo>();
		consolidadoMovimientoDTO = new DtoConsolidadoMovimiento();
		this.deshabilitaCajeroAcepta = true;
		this.confirmado = "false";
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
	 *  del atributo estado
	 * 
	 * @return  Retorna la variable estado
	 */
	public String getEstado() {
		return estado;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo estado
	 * 
	 * @param  valor para el atributo estado 
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo listaCentrosAtencion
	 * 
	 * @return  Retorna la variable listaCentrosAtencion
	 */
	public ArrayList<DtoCentrosAtencion> getListaCentrosAtencion() {
		return listaCentrosAtencion;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo listaCentrosAtencion
	 * 
	 * @param  valor para el atributo listaCentrosAtencion 
	 */
	public void setListaCentrosAtencion(
			ArrayList<DtoCentrosAtencion> listaCentrosAtencion) {
		this.listaCentrosAtencion = listaCentrosAtencion;
	}
	
	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo listadoEstadoTraslado
	 * 
	 * @return  Retorna la variable listadoEstadoTraslado
	 */
	public ArrayList<DtoIntegridadDominio> getListadoEstadoTraslado() {
		return listadoEstadoTraslado;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo listadoEstadoTraslado
	 * 
	 * @param  valor para el atributo listadoEstadoTraslado 
	 */
	public void setListadoEstadoTraslado(
			ArrayList<DtoIntegridadDominio> listadoEstadoTraslado) {
		this.listadoEstadoTraslado = listadoEstadoTraslado;
	}

	/**
	 * Este M&eacute;todo se encarga de obtener el valor 
	 * del atributo listaCajaOrigen
	
	 * @return retorna la variable listaCajaOrigen 
	 * @author Angela Maria Aguirre 
	 */
	public ArrayList<Cajas> getListaCajaOrigen() {
		return listaCajaOrigen;
	}

	/**
	 * Este M&eacute;todo se encarga de establecer el valor 
	 * del atributo listaCajaOrigen
	
	 * @param valor para el atributo listaCajaOrigen 
	 * @author Angela Maria Aguirre 
	 */
	public void setListaCajaOrigen(ArrayList<Cajas> listaCajaOrigen) {
		this.listaCajaOrigen = listaCajaOrigen;
	}

	/**
	 * Este M&eacute;todo se encarga de obtener el valor 
	 * del atributo listaCajaDestino
	
	 * @return retorna la variable listaCajaDestino 
	 * @author Angela Maria Aguirre 
	 */
	public ArrayList<Cajas> getListaCajaDestino() {
		return listaCajaDestino;
	}

	/**
	 * Este M&eacute;todo se encarga de establecer el valor 
	 * del atributo listaCajaDestino
	
	 * @param valor para el atributo listaCajaDestino 
	 * @author Angela Maria Aguirre 
	 */
	public void setListaCajaDestino(ArrayList<Cajas> listaCajaDestino) {
		this.listaCajaDestino = listaCajaDestino;
	}

	/**
	 * Este M&eacute;todo se encarga de obtener el valor 
	 * del atributo listaCajeroSolicitante
	
	 * @return retorna la variable listaCajeroSolicitante 
	 * @author Angela Maria Aguirre 
	 */
	public ArrayList<DtoUsuarioPersona> getListaCajeroSolicitante() {
		return listaCajeroSolicitante;
	}

	/**
	 * Este M&eacute;todo se encarga de establecer el valor 
	 * del atributo listaCajeroSolicitante
	
	 * @param valor para el atributo listaCajeroSolicitante 
	 * @author Angela Maria Aguirre 
	 */
	public void setListaCajeroSolicitante(
			ArrayList<DtoUsuarioPersona> listaCajeroSolicitante) {
		this.listaCajeroSolicitante = listaCajeroSolicitante;
	}

	/**
	 * Este M&eacute;todo se encarga de obtener el valor 
	 * del atributo listaCajeroAcepta
	
	 * @return retorna la variable listaCajeroAcepta 
	 * @author Angela Maria Aguirre 
	 */
	public ArrayList<DtoUsuarioPersona> getListaCajeroAcepta() {
		return listaCajeroAcepta;
	}

	/**
	 * Este M&eacute;todo se encarga de establecer el valor 
	 * del atributo listaCajeroAcepta
	
	 * @param valor para el atributo listaCajeroAcepta 
	 * @author Angela Maria Aguirre 
	 */
	public void setListaCajeroAcepta(ArrayList<DtoUsuarioPersona> listaCajeroAcepta) {
		this.listaCajeroAcepta = listaCajeroAcepta;
	}

	/**
	 * Este M&eacute;todo se encarga de obtener el valor 
	 * del atributo filtroSolicitud
	
	 * @return retorna la variable filtroSolicitud 
	 * @author Angela Maria Aguirre 
	 */
	public DtoConsultaTrasladosCajasRecaudo getFiltroSolicitud() {
		return filtroSolicitud;
	}

	/**
	 * Este M&eacute;todo se encarga de establecer el valor 
	 * del atributo filtroSolicitud
	
	 * @param valor para el atributo filtroSolicitud 
	 * @author Angela Maria Aguirre 
	 */
	public void setFiltroSolicitud(DtoConsultaTrasladosCajasRecaudo filtroSolicitud) {
		this.filtroSolicitud = filtroSolicitud;
	}

	/**
	 * Este M&eacute;todo se encarga de obtener el valor 
	 * del atributo listaRegistrosSolicitud
	
	 * @return retorna la variable listaRegistrosSolicitud 
	 * @author Angela Maria Aguirre 
	 */
	public ArrayList<DtoConsultaTrasladosCajasRecaudo> getListaRegistrosSolicitud() {
		return listaRegistrosSolicitud;
	}

	/**
	 * Este M&eacute;todo se encarga de establecer el valor 
	 * del atributo listaRegistrosSolicitud
	
	 * @param valor para el atributo listaRegistrosSolicitud 
	 * @author Angela Maria Aguirre 
	 */
	public void setListaRegistrosSolicitud(
			ArrayList<DtoConsultaTrasladosCajasRecaudo> listaRegistrosSolicitud) {
		this.listaRegistrosSolicitud = listaRegistrosSolicitud;
	}

	/**
	 * Este M&eacute;todo se encarga de obtener el valor 
	 * del atributo index
	
	 * @return retorna la variable index 
	 * @author Angela Maria Aguirre 
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * Este M&eacute;todo se encarga de establecer el valor 
	 * del atributo index
	
	 * @param valor para el atributo index 
	 * @author Angela Maria Aguirre 
	 */
	public void setIndex(int index) {
		this.index = index;
	}

	/**
	 * Este M&eacute;todo se encarga de obtener el valor 
	 * del atributo posArray
	
	 * @return retorna la variable posArray 
	 * @author Angela Maria Aguirre 
	 */
	public int getPosArray() {
		return posArray;
	}

	/**
	 * Este M&eacute;todo se encarga de establecer el valor 
	 * del atributo posArray
	
	 * @param valor para el atributo posArray 
	 * @author Angela Maria Aguirre 
	 */
	public void setPosArray(int posArray) {
		this.posArray = posArray;
	}

	/**
	 * Este M&eacute;todo se encarga de obtener el valor 
	 * del atributo patronOrdenar
	
	 * @return retorna la variable patronOrdenar 
	 * @author Angela Maria Aguirre 
	 */
	public String getPatronOrdenar() {
		return patronOrdenar;
	}

	/**
	 * Este M&eacute;todo se encarga de establecer el valor 
	 * del atributo patronOrdenar
	
	 * @param valor para el atributo patronOrdenar 
	 * @author Angela Maria Aguirre 
	 */
	public void setPatronOrdenar(String patronOrdenar) {
		this.patronOrdenar = patronOrdenar;
	}

	/**
	 * Este M&eacute;todo se encarga de obtener el valor 
	 * del atributo esDescendente
	
	 * @return retorna la variable esDescendente 
	 * @author Angela Maria Aguirre 
	 */
	public String getEsDescendente() {
		return esDescendente;
	}

	/**
	 * Este M&eacute;todo se encarga de establecer el valor 
	 * del atributo esDescendente
	
	 * @param valor para el atributo esDescendente 
	 * @author Angela Maria Aguirre 
	 */
	public void setEsDescendente(String esDescendente) {
		this.esDescendente = esDescendente;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo dtoDetalle
	 * 
	 * @return  Retorna la variable dtoDetalle
	 */
	public DtoConsultaTrasladosCajasRecaudo getDtoDetalle() {
		return dtoDetalle;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo dtoDetalle
	 * 
	 * @param  valor para el atributo dtoDetalle 
	 */
	public void setDtoDetalle(DtoConsultaTrasladosCajasRecaudo dtoDetalle) {
		this.dtoDetalle = dtoDetalle;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo dtoInformacionEntrega
	 * 
	 * @return  Retorna la variable dtoInformacionEntrega
	 */
	public DtoInformacionEntrega getDtoInformacionEntrega() {
		return dtoInformacionEntrega;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo dtoInformacionEntrega
	 * 
	 * @param  valor para el atributo dtoInformacionEntrega 
	 */
	public void setDtoInformacionEntrega(DtoInformacionEntrega dtoInformacionEntrega) {
		this.dtoInformacionEntrega = dtoInformacionEntrega;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo consolidadoMovimientoDTO
	 * 
	 * @return  Retorna la variable consolidadoMovimientoDTO
	 */
	public DtoConsolidadoMovimiento getConsolidadoMovimientoDTO() {
		return consolidadoMovimientoDTO;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo consolidadoMovimientoDTO
	 * 
	 * @param  valor para el atributo consolidadoMovimientoDTO 
	 */
	public void setConsolidadoMovimientoDTO(
			DtoConsolidadoMovimiento consolidadoMovimientoDTO) {
		this.consolidadoMovimientoDTO = consolidadoMovimientoDTO;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo mostrarParaSeccionEspecial
	 * 
	 * @return  Retorna la variable mostrarParaSeccionEspecial
	 */
	public String getMostrarParaSeccionEspecial() {
		return mostrarParaSeccionEspecial;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo mostrarParaSeccionEspecial
	 * 
	 * @param  valor para el atributo mostrarParaSeccionEspecial 
	 */
	public void setMostrarParaSeccionEspecial(String mostrarParaSeccionEspecial) {
		this.mostrarParaSeccionEspecial = mostrarParaSeccionEspecial;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo consulta
	 * 
	 * @return  Retorna la variable consulta
	 */
	public String getConsulta() {
		return consulta;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo consulta
	 * 
	 * @param  valor para el atributo consulta 
	 */
	public void setConsulta(String consulta) {
		this.consulta = consulta;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo activo
	 * 
	 * @return  Retorna la variable activo
	 */
	public String getActivo() {
		return activo;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo activo
	 * 
	 * @param  valor para el atributo activo 
	 */
	public void setActivo(String activo) {
		this.activo = activo;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo esMultiEmpresa
	 * 
	 * @return  Retorna la variable esMultiEmpresa
	 */
	public String getEsMultiEmpresa() {
		return esMultiEmpresa;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo esMultiEmpresa
	 * 
	 * @param  valor para el atributo esMultiEmpresa 
	 */
	public void setEsMultiEmpresa(String esMultiEmpresa) {
		this.esMultiEmpresa = esMultiEmpresa;
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
	 *  del atributo deshabilitaCajeroAcepta
	 * 
	 * @return  Retorna la variable deshabilitaCajeroAcepta
	 */
	public boolean isDeshabilitaCajeroAcepta() {
		return deshabilitaCajeroAcepta;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo deshabilitaCajeroAcepta
	 * 
	 * @param  valor para el atributo deshabilitaCajeroAcepta 
	 */
	public void setDeshabilitaCajeroAcepta(boolean deshabilitaCajeroAcepta) {
		this.deshabilitaCajeroAcepta = deshabilitaCajeroAcepta;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo reglaNavegacion
	 * 
	 * @return  Retorna la variable reglaNavegacion
	 */
	public String getReglaNavegacion() {
		return reglaNavegacion;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo reglaNavegacion
	 * 
	 * @param  valor para el atributo reglaNavegacion 
	 */
	public void setReglaNavegacion(String reglaNavegacion) {
		this.reglaNavegacion = reglaNavegacion;
	}

	/**
	 * Método que se encarga de obtener el 
	 * valor del atributo  confirmado
	 *
	 * @return retorna la variable confirmado
	 */
	public String getConfirmado() {
		return confirmado;
	}

	/**
	 * Método que se encarga de establecer el valor
	 * del atributo confirmado
	 * @param confirmado es el valor para el atributo confirmado 
	 */
	public void setConfirmado(String confirmado) {
		this.confirmado = confirmado;
	}
}
