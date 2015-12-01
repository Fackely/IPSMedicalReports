package com.princetonsa.actionform.facturacion;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import util.ConstantesBD;

import com.princetonsa.dto.administracion.DtoIntegridadDominio;
import com.princetonsa.dto.facturacion.DTOMontosCobro;
import com.princetonsa.dto.facturacion.DTOResultadoBusquedaDetalleMontos;
import com.princetonsa.dto.facturacion.DtoConvenio;
import com.servinte.axioma.orm.EstratosSociales;
import com.servinte.axioma.orm.NaturalezaPacientes;
import com.servinte.axioma.orm.TiposAfiliado;
import com.servinte.axioma.orm.TiposMonto;
import com.servinte.axioma.orm.TiposPaciente;
import com.servinte.axioma.orm.ViasIngreso;

/**
 * Esta clase se encarga de inicializar los valores de las
 * p�ginas usadas para el ingreso y modificaci�n de los 
 * montos de cobro y sus detalles
 * 
 * @author Angela Maria Aguirre
 * @since 27/08/2010
 */
public class ConsultaMontosDeCobroForm extends ActionForm {
	
	private static final long serialVersionUID = 1L;

	private String estado="";
	
	/**
	 * Atributo que almacena los convenios activos y que manejan montos
	 */
	private ArrayList<DtoConvenio> listaConvenios;
		
	/**
	 * Atributo usado para almacenar el listado con los tipos de paciente
	 */
	private ArrayList<TiposPaciente> listadoTipoPaciente;
	
	/**
	 * Atributo usado para almacenar el listado con los tipos de paciente
	 */
	private ArrayList<TiposAfiliado> listadoTipoAfiliado;
	
	/**
	 * Atributo usado para almacenar el listado con los estratos sociales 
	 */
	private ArrayList<EstratosSociales> listadoEstratoSocial;
	
	/**
	 * Atributo que almacena los montos asociados al convenio
	 * seleccionado
	 */
	private ArrayList<DTOMontosCobro> listaMontosCobro;
	
	/**
	 * Atributo que almacena las naturalezas de paciente 
	 */
	private ArrayList<NaturalezaPacientes> listaNaturalezaPacientes;
	
	/**
	 * Atributo que almacena los tipos de detalle de montos de cobro 
	 */
	private ArrayList<DtoIntegridadDominio> listaTiposDetalleMonto;
	
	private ArrayList<ViasIngreso> listadoViasIngreso; 
	
	/**
	 * Atributo que almacena los tipos de montos registrados.
	 */
	private ArrayList<TiposMonto> listaTiposMonto;	
	
	/**
	 * Atributo usado para mostrar los mensajes de &eacute;xito.
	 */
	private String mostrarMensaje;
	/**
	 * Atributo que almacena los filtros de la b�squeda 
	 * 
	 */
	private DTOResultadoBusquedaDetalleMontos dtoFiltro;
	
	/**
	 * Atributo que almacena los filtros de la b�squeda:
	 * id del convenio y fecha de vigencia
	 */
	private DTOMontosCobro dtoMontoFiltro;
	
	/**
	 * Aributo que almacena el id del monto seleccionado para
	 * ver su detalle.
	 */
	private int idDetalleSeleccionado;
	
	/**
	 * Atributo que almacena el c�digo del detalle del monto seleccionado
	 * por el usuario
	 */
	private int codigoDetalle;
	
	/**
	 * Atributo que almacena el monto seleccionado por el usuario
	 * 
	 */
	private DTOMontosCobro montoSeleccionado;
	
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
	 * 
	 * Este m�todo se encarga de inicializar los valores de la 
	 * p�gina de consulta de convenios de montos de cobro
	 * 
	 * @author, Angela Maria Aguirre
	 *
	 */
	public void reset(){		
		listaMontosCobro=new ArrayList<DTOMontosCobro>();
		mostrarMensaje="";
		dtoFiltro=new DTOResultadoBusquedaDetalleMontos();
		dtoMontoFiltro = new DTOMontosCobro();
		dtoMontoFiltro.setConvenio(new DtoConvenio());
		dtoMontoFiltro.setListaDetalles(new ArrayList<DTOResultadoBusquedaDetalleMontos>());
		estado="";
	}
	
	/**
	 * 
	 * Este m�todo se encarga de realizar las validaciones de 
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
		boolean filtroSeleccionado=false;
		
		if(estado.equals("consultarMontos")){
			if(dtoMontoFiltro.getConvenio()!=null){
				if(dtoMontoFiltro.getConvenio().getCodigo()!=ConstantesBD.codigoNuncaValido){
					filtroSeleccionado=true;
				}								
			}
			if(!filtroSeleccionado){
				if(dtoMontoFiltro.getFechaVigenciaConvenio()==null){
					if(dtoFiltro.getViaIngresoID()==ConstantesBD.codigoNuncaValido){
						if(dtoFiltro.getTipoPacienteAcronimo().equals("")){
							if(dtoFiltro.getTipoAfiliadoAcronimo()!=ConstantesBD.codigoTipoAfiliadoBeneficiario.charAt(0) &&
									dtoFiltro.getTipoAfiliadoAcronimo()!=ConstantesBD.codigoTipoAfiliadoCotizante.charAt(0)){
								if(dtoFiltro.getEstratoID()==ConstantesBD.codigoNuncaValido){
									if(dtoFiltro.getNaturalezaID()==ConstantesBD.codigoNuncaValido){
										if(dtoFiltro.getTipoMontoID()==ConstantesBD.codigoNuncaValido){
											if(dtoFiltro.getTipoDetalleAcronimo().equals("")){
												if(dtoFiltro.getPorcentaje()==null){
													if(dtoFiltro.getValor()!=null){
														filtroSeleccionado=true;
													}
												}else{
													filtroSeleccionado=true;													
												}
											}else{
												filtroSeleccionado=true;
											}
										}else{
											filtroSeleccionado=true;
										}
									}else{
										filtroSeleccionado=true;
									}
								}else{
									filtroSeleccionado=true;
								}
							}else{
								filtroSeleccionado=true;
							}
						}else{
							filtroSeleccionado=true;
						}							
					}else{
						filtroSeleccionado=true;
					}
				}else{
					filtroSeleccionado=true;
				}
			}	
			if(filtroSeleccionado){
				if(dtoFiltro.getPorcentaje()!=null && 
						(dtoFiltro.getPorcentaje()<0 || dtoFiltro.getPorcentaje()>100)){
					errores.add("El Porcentaje del monto es inv�lido", 
						new ActionMessage("errores.modFacturacionConsultaMontosCobroPorcentajeInvalido"));
				}
				if(dtoFiltro.getValor()!=null && dtoFiltro.getValor()<0){
					errores.add("El Valor del monto es inv�lido", 
							new ActionMessage("errores.modFacturacionConsultaMontosCobroValorInvalido"));
				}
			}else{
				errores.add("Los Filtros son nulos", 
						new ActionMessage("errores.modFacturacionConsultaMontosCobroFiltrosNoIngresados"));
			}			
		}
		
		return errores;
	}

	/**
	 * Este M�todo se encarga de obtener el valor 
	 * del atributo estado
	
	 * @return retorna la variable estado 
	 * @author Angela Maria Aguirre 
	 */
	public String getEstado() {
		return estado;
	}	
	
	/**
	 * Este M�todo se encarga de establecer el valor 
	 * del atributo estado
	
	 * @param valor para el atributo estado 
	 * @author Angela Maria Aguirre 
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}

	/**
	 * Este M�todo se encarga de obtener el valor 
	 * del atributo listadoTipoPaciente
	
	 * @return retorna la variable listadoTipoPaciente 
	 * @author Angela Maria Aguirre 
	 */
	public ArrayList<TiposPaciente> getListadoTipoPaciente() {
		return listadoTipoPaciente;
	}

	/**
	 * Este M�todo se encarga de establecer el valor 
	 * del atributo listadoTipoPaciente
	
	 * @param valor para el atributo listadoTipoPaciente 
	 * @author Angela Maria Aguirre 
	 */
	public void setListadoTipoPaciente(
			ArrayList<TiposPaciente> listadoTipoPaciente) {
		this.listadoTipoPaciente = listadoTipoPaciente;
	}

	/**
	 * Este M�todo se encarga de obtener el valor 
	 * del atributo listadoTipoAfiliado
	
	 * @return retorna la variable listadoTipoAfiliado 
	 * @author Angela Maria Aguirre 
	 */
	public ArrayList<TiposAfiliado> getListadoTipoAfiliado() {
		return listadoTipoAfiliado;
	}

	/**
	 * Este M�todo se encarga de establecer el valor 
	 * del atributo listadoTipoAfiliado
	
	 * @param valor para el atributo listadoTipoAfiliado 
	 * @author Angela Maria Aguirre 
	 */
	public void setListadoTipoAfiliado(
			ArrayList<TiposAfiliado> listadoTipoAfiliado) {
		this.listadoTipoAfiliado = listadoTipoAfiliado;
	}

	/**
	 * Este M�todo se encarga de obtener el valor 
	 * del atributo listadoEstratoSocial
	
	 * @return retorna la variable listadoEstratoSocial 
	 * @author Angela Maria Aguirre 
	 */
	public ArrayList<EstratosSociales> getListadoEstratoSocial() {
		return listadoEstratoSocial;
	}

	/**
	 * Este M�todo se encarga de establecer el valor 
	 * del atributo listadoEstratoSocial
	
	 * @param valor para el atributo listadoEstratoSocial 
	 * @author Angela Maria Aguirre 
	 */
	public void setListadoEstratoSocial(
			ArrayList<EstratosSociales> listadoEstratoSocial) {
		this.listadoEstratoSocial = listadoEstratoSocial;
	}

	/**
	 * Este M�todo se encarga de obtener el valor 
	 * del atributo listaMontosCobro
	
	 * @return retorna la variable listaMontosCobro 
	 * @author Angela Maria Aguirre 
	 */
	public ArrayList<DTOMontosCobro> getListaMontosCobro() {
		return listaMontosCobro;
	}

	/**
	 * Este M�todo se encarga de establecer el valor 
	 * del atributo listaMontosCobro
	
	 * @param valor para el atributo listaMontosCobro 
	 * @author Angela Maria Aguirre 
	 */
	public void setListaMontosCobro(
			ArrayList<DTOMontosCobro> listaMontosCobro) {
		this.listaMontosCobro = listaMontosCobro;
	}
	
	/**
	 * Este M�todo se encarga de obtener el valor 
	 * del atributo listaConvenios
	
	 * @return retorna la variable listaConvenios 
	 * @author Angela Maria Aguirre 
	 */
	public ArrayList<DtoConvenio> getListaConvenios() {
		return listaConvenios;
	}

	/**
	 * Este M�todo se encarga de establecer el valor 
	 * del atributo listaConvenios
	
	 * @param valor para el atributo listaConvenios 
	 * @author Angela Maria Aguirre 
	 */
	public void setListaConvenios(ArrayList<DtoConvenio> listaConvenios) {
		this.listaConvenios = listaConvenios;
	}

	/**
	 * Este M�todo se encarga de obtener el valor 
	 * del atributo listaTiposMonto
	
	 * @return retorna la variable listaTiposMonto 
	 * @author Angela Maria Aguirre 
	 */
	public ArrayList<TiposMonto> getListaTiposMonto() {
		return listaTiposMonto;
	}

	/**
	 * Este M�todo se encarga de establecer el valor 
	 * del atributo listaTiposMonto
	
	 * @param valor para el atributo listaTiposMonto 
	 * @author Angela Maria Aguirre 
	 */
	public void setListaTiposMonto(ArrayList<TiposMonto> listaTiposMonto) {
		this.listaTiposMonto = listaTiposMonto;
	}

	/**
	 * Este M�todo se encarga de obtener el valor 
	 * del atributo listaTiposDetalleMonto
	
	 * @return retorna la variable listaTiposDetalleMonto 
	 * @author Angela Maria Aguirre 
	 */
	public ArrayList<DtoIntegridadDominio> getListaTiposDetalleMonto() {
		return listaTiposDetalleMonto;
	}

	/**
	 * Este M�todo se encarga de establecer el valor 
	 * del atributo listaTiposDetalleMonto
	
	 * @param valor para el atributo listaTiposDetalleMonto 
	 * @author Angela Maria Aguirre 
	 */
	public void setListaTiposDetalleMonto(
			ArrayList<DtoIntegridadDominio> listaTiposDetalleMonto) {
		this.listaTiposDetalleMonto = listaTiposDetalleMonto;
	}

	/**
	 * Este M�todo se encarga de obtener el valor 
	 * del atributo mostrarMensaje
	
	 * @return retorna la variable mostrarMensaje 
	 * @author Angela Maria Aguirre 
	 */
	public String getMostrarMensaje() {
		return mostrarMensaje;
	}

	/**
	 * Este M�todo se encarga de establecer el valor 
	 * del atributo mostrarMensaje
	
	 * @param valor para el atributo mostrarMensaje 
	 * @author Angela Maria Aguirre 
	 */
	public void setMostrarMensaje(String mostrarMensaje) {
		this.mostrarMensaje = mostrarMensaje;
	}

	/**
	 * Este M�todo se encarga de obtener el valor 
	 * del atributo listadoViasIngreso
	
	 * @return retorna la variable listadoViasIngreso 
	 * @author Angela Maria Aguirre 
	 */
	public ArrayList<ViasIngreso> getListadoViasIngreso() {
		return listadoViasIngreso;
	}

	/**
	 * Este M�todo se encarga de establecer el valor 
	 * del atributo listadoViasIngreso
	
	 * @param valor para el atributo listadoViasIngreso 
	 * @author Angela Maria Aguirre 
	 */
	public void setListadoViasIngreso(ArrayList<ViasIngreso> listadoViasIngreso) {
		this.listadoViasIngreso = listadoViasIngreso;
	}

	/**
	 * Este M�todo se encarga de obtener el valor 
	 * del atributo listaNaturalezaPacientes
	
	 * @return retorna la variable listaNaturalezaPacientes 
	 * @author Angela Maria Aguirre 
	 */
	public ArrayList<NaturalezaPacientes> getListaNaturalezaPacientes() {
		return listaNaturalezaPacientes;
	}

	/**
	 * Este M�todo se encarga de establecer el valor 
	 * del atributo listaNaturalezaPacientes
	
	 * @param valor para el atributo listaNaturalezaPacientes 
	 * @author Angela Maria Aguirre 
	 */
	public void setListaNaturalezaPacientes(
			ArrayList<NaturalezaPacientes> listaNaturalezaPacientes) {
		this.listaNaturalezaPacientes = listaNaturalezaPacientes;
	}

	/**
	 * Este M�todo se encarga de obtener el valor 
	 * del atributo dtoFiltro
	
	 * @return retorna la variable dtoFiltro 
	 * @author Angela Maria Aguirre 
	 */
	public DTOResultadoBusquedaDetalleMontos getDtoFiltro() {
		return dtoFiltro;
	}

	/**
	 * Este M�todo se encarga de establecer el valor 
	 * del atributo dtoFiltro
	
	 * @param valor para el atributo dtoFiltro 
	 * @author Angela Maria Aguirre 
	 */
	public void setDtoFiltro(DTOResultadoBusquedaDetalleMontos dtoFiltro) {
		this.dtoFiltro = dtoFiltro;
	}

	/**
	 * Este M�todo se encarga de obtener el valor 
	 * del atributo dtoMontoFiltro
	
	 * @return retorna la variable dtoMontoFiltro 
	 * @author Angela Maria Aguirre 
	 */
	public DTOMontosCobro getDtoMontoFiltro() {
		return dtoMontoFiltro;
	}

	/**
	 * Este M�todo se encarga de establecer el valor 
	 * del atributo dtoMontoFiltro
	
	 * @param valor para el atributo dtoMontoFiltro 
	 * @author Angela Maria Aguirre 
	 */
	public void setDtoMontoFiltro(DTOMontosCobro dtoMontoFiltro) {
		this.dtoMontoFiltro = dtoMontoFiltro;
	}

	/**
	 * Este M�todo se encarga de obtener el valor 
	 * del atributo idDetalleSeleccionado
	
	 * @return retorna la variable idDetalleSeleccionado 
	 * @author Angela Maria Aguirre 
	 */
	public int getIdDetalleSeleccionado() {
		return idDetalleSeleccionado;
	}

	/**
	 * Este M�todo se encarga de establecer el valor 
	 * del atributo idDetalleSeleccionado
	
	 * @param valor para el atributo idDetalleSeleccionado 
	 * @author Angela Maria Aguirre 
	 */
	public void setIdDetalleSeleccionado(int idDetalleSeleccionado) {
		this.idDetalleSeleccionado = idDetalleSeleccionado;
	}

	/**
	 * Este M�todo se encarga de obtener el valor 
	 * del atributo serialversionuid
	
	 * @return retorna la variable serialversionuid 
	 * @author Angela Maria Aguirre 
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}	

	/**
	 * Este M�todo se encarga de obtener el valor 
	 * del atributo montoSeleccionado
	
	 * @return retorna la variable montoSeleccionado 
	 * @author Angela Maria Aguirre 
	 */
	public DTOMontosCobro getMontoSeleccionado() {
		return montoSeleccionado;
	}

	/**
	 * Este M�todo se encarga de establecer el valor 
	 * del atributo montoSeleccionado
	
	 * @param valor para el atributo montoSeleccionado 
	 * @author Angela Maria Aguirre 
	 */
	public void setMontoSeleccionado(DTOMontosCobro montoSeleccionado) {
		this.montoSeleccionado = montoSeleccionado;
	}

	/**
	 * Este M�todo se encarga de obtener el valor 
	 * del atributo posArray
	
	 * @return retorna la variable posArray 
	 * @author Angela Maria Aguirre 
	 */
	public int getPosArray() {
		return posArray;
	}

	/**
	 * Este M�todo se encarga de establecer el valor 
	 * del atributo posArray
	
	 * @param valor para el atributo posArray 
	 * @author Angela Maria Aguirre 
	 */
	public void setPosArray(int posArray) {
		this.posArray = posArray;
	}

	/**
	 * Este M�todo se encarga de obtener el valor 
	 * del atributo patronOrdenar
	
	 * @return retorna la variable patronOrdenar 
	 * @author Angela Maria Aguirre 
	 */
	public String getPatronOrdenar() {
		return patronOrdenar;
	}

	/**
	 * Este M�todo se encarga de establecer el valor 
	 * del atributo patronOrdenar
	
	 * @param valor para el atributo patronOrdenar 
	 * @author Angela Maria Aguirre 
	 */
	public void setPatronOrdenar(String patronOrdenar) {
		this.patronOrdenar = patronOrdenar;
	}

	/**
	 * Este M�todo se encarga de obtener el valor 
	 * del atributo esDescendente
	
	 * @return retorna la variable esDescendente 
	 * @author Angela Maria Aguirre 
	 */
	public String getEsDescendente() {
		return esDescendente;
	}

	/**
	 * Este M�todo se encarga de establecer el valor 
	 * del atributo esDescendente
	
	 * @param valor para el atributo esDescendente 
	 * @author Angela Maria Aguirre 
	 */
	public void setEsDescendente(String esDescendente) {
		this.esDescendente = esDescendente;
	}

	/**
	 * Este M�todo se encarga de obtener el valor 
	 * del atributo codigoDetalle
	
	 * @return retorna la variable codigoDetalle 
	 * @author Angela Maria Aguirre 
	 */
	public int getCodigoDetalle() {
		return codigoDetalle;
	}

	/**
	 * Este M�todo se encarga de establecer el valor 
	 * del atributo codigoDetalle
	
	 * @param valor para el atributo codigoDetalle 
	 * @author Angela Maria Aguirre 
	 */
	public void setCodigoDetalle(int codigoDetalle) {
		this.codigoDetalle = codigoDetalle;
	}	
}
