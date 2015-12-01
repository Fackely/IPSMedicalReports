package com.princetonsa.actionform.tesoreria;

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
import com.princetonsa.dto.tesoreria.DTOCambioResponsableDetFaltanteSobrante;
import com.princetonsa.dto.tesoreria.DTOHistoCambioResponsable;
import com.princetonsa.dto.tesoreria.DtoUsuarioPersona;
import com.servinte.axioma.orm.Cajas;
import com.servinte.axioma.orm.TiposMovimientoCaja;

/**
 * Esta clase se encarga de obtener los datos ingresados
 * por el usuario y mapearlos a los atributos asignados a
 * cada uno.
 * 
 * @author Yennifer Guerrero
 * @author Angela Aguirre
 *
 */
@SuppressWarnings("serial")
public class CambioResponsableFaltanteForm extends ActionForm {
		
	/**
	 * Almacena la acci&oacute;n a realizar desde las p&aacute;gina
	 */
	private String estado;
		
	/**
	 * DTO para almacenar los par&aacute;metros de b&uacute;squeda de los
	 * faltantes / sobrantes
	 */
	
	private DTOCambioResponsableDetFaltanteSobrante filtrosFaltanteSobrante;
	
	/**
	 * Atributo usado para almacenar el listado con los centros de atenci&oacute;n
	 */
	private ArrayList<DtoCentrosAtencion> listaCentrosAtencion;
	
	/**
	 * Atributo usado para almacenar el listado con los tipos diferencia
	 */
	private ArrayList<DtoIntegridadDominio> listadoTipoDiferencia;
	
	/**
	 * Lista que almacena todos los datos a cerca de los faltantes/sobrantes de caja
	 */
	ArrayList<DTOCambioResponsableDetFaltanteSobrante> listaDetFaltanteSobrantes;
	
	/**
	 * Listado que almacena los cajeros del sistema.
	 */
	private ArrayList<DtoUsuarioPersona> listaCajeros;
	
	/**
	 * Listado que almacena las cajas en el sistema.
	 */
	private ArrayList<Cajas> listaCajas;	
	
	/**
	 * Almacena el registro que el usuario ha seleccionado para realizar el cambio de responsable.
	 */
	private DTOCambioResponsableDetFaltanteSobrante dtoDetalle;	
	
	/**
	 * Atributo que almacena el centro de atencion seleccionado
	 */
	private int consecutivoCA;
	
	/**
	 * Listado que almacena los turnos de las cajas.
	 */
	private ArrayList<TiposMovimientoCaja> listadoTurnoFaltanteSobrante;
	
	/**
	 * Atributo que almacena el &iacute;ndice de la posici&oacute;n 
	 * en donde se encuentra el registro seleccionado.
	 */
	private int index;

	/**
	 * Atributo usado para completar la ruta hacia la cual se debe 
	 * direccionar la aplicaci&oacute;n.
	 */
	private String path;
	
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
	 * Atributo usado para mostrar los mensajes de &eacute;xito.
	 */
	private String mostrarMensaje;
	
	/**
	 * Atributo que almacena el estado hacia el cual debe dirigirse la 
	 * regla de navegaci&oacute;n dependiendo del n&uacute;mero registros 
	 * encontrados.
	 */
	private String reglaNavegacion;
	
	/**
	 * Atributo que define si se habilitan o no
	 * los campos de nuevo responsable y motivo 
	 */
	private Boolean resumen;
	
	/**
	 * Atributo usado para listar el estado del faltante sobrante.
	 */
	private ArrayList<DtoIntegridadDominio> listadoEstadoFaltanteSobrante;
	
	/**
	 * Atributo que determina si se debe o no mostrar la lista de Estado
	 * Faltante / Sobrante
	 */
	private String habilitaConsultaHistorico;
	
	/**
	 * Lista que se encarga de almacenar los datos de los 
	 * hist&oacute;rico de un faltante / sobrante
	 */
	private ArrayList<DTOHistoCambioResponsable>  listaHistorico;
	
	/**
	 * Atributo que determina el cajero seleccionado como
	 * nuevo responsable del registro
	 */
	private String cajeroNuevoResponsable;
	
	/**
	 * Indica se se pas&oacute; de la p&aacute;gina del listado principal
	 * al detalle por primera vez, poniendo el &iacute;ndice del paginador en 0
	 * Solamente se pone en true al momento de pasar del general al detalle.
	 */
	private boolean mostrarDetalle=true;
	
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
		if(estado.equals("buscar"))
		{			
			if((filtrosFaltanteSobrante.getFechaGeneracionInicial()==null) || 
					((filtrosFaltanteSobrante.getFechaGeneracionInicial().toString()).equals(""))){
				errores.add("La fecha Inicial es requerida", 
						new ActionMessage("errors.required", "El campo Fecha Inicial"));
				
			}else{
				String fechaInicial = UtilidadFecha.conversionFormatoFechaAAp(
						filtrosFaltanteSobrante.getFechaGeneracionInicial());
				String fechaActual = UtilidadFecha.conversionFormatoFechaAAp(
						Calendar.getInstance().getTime());
				
				if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(
						fechaInicial, fechaActual)){
							
					 errores.add("FECHA INICIAL MAYOR QUE FECHA ACTUAL.", new ActionMessage(
									 "errors.fechaPosteriorIgualActual"," Inicial "+fechaInicial," Actual "+fechaActual));	
				}
			}
			if((filtrosFaltanteSobrante.getFechaGeneracionFin()==null) || 
						((filtrosFaltanteSobrante.getFechaGeneracionFin().toString()).equals(""))){
				
				errores.add("La fecha Fin es requerida", 
						new ActionMessage("errors.required", "El campo Fecha Fin"));
				
			}else{
				String fechaFin = UtilidadFecha.conversionFormatoFechaAAp(
						filtrosFaltanteSobrante.getFechaGeneracionFin());
				
				String fechaActual = UtilidadFecha.conversionFormatoFechaAAp(
						Calendar.getInstance().getTime());
				
				if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(fechaFin, fechaActual)){
					errores.add("La fecha Fin es mayor que fecha actual", 
							new ActionMessage("errors.fechaPosteriorIgualActual", " Fin "+fechaFin," Actual "+fechaActual));
				}
				if((filtrosFaltanteSobrante.getFechaGeneracionInicial()!=null) && 
						(!(filtrosFaltanteSobrante.getFechaGeneracionInicial().toString()).equals(""))){
					
					String fechaInicial = UtilidadFecha.conversionFormatoFechaAAp(
							filtrosFaltanteSobrante.getFechaGeneracionInicial());					
					
					
					if(UtilidadFecha.esFechaMenorQueOtraReferencia(fechaFin,fechaInicial)){
						errores.add("FECHA FIN MAYOR QUE FECHA INICIAL.", new ActionMessage(
										 "errors.fechaAnteriorIgualAOtraDeReferencia"," Final "+fechaFin," Inicial "+fechaInicial));
					}
					
				}
			}
			if(habilitaConsultaHistorico.equals("false")){
				if(filtrosFaltanteSobrante.getConsecutivoCA()==ConstantesBD.codigoNuncaValido){
					errores.add("Centro de Atencion Requerido.", new ActionMessage(
							 "errores.modTesoreria.centroAtencionRequerido"));
				}
			}else{
				if(request.getAttribute("habilitaConsultaHistorico")==null){
					request.setAttribute("habilitaConsultaHistorico", "true");	
				}
			}
		}
		
		return errores;
	}
	
	/**
	 * 
	 * Este m&eacute;todo se encarga de inicializar los valores de la 
	 * p&aacute;gina de consulta del faltante / sobrante 
	 * 
	 * @author, Angela Maria Aguirre
	 *
	 */
	public void reset()
	{
		filtrosFaltanteSobrante = new DTOCambioResponsableDetFaltanteSobrante();
		mostrarMensaje="";
		resumen=false;
	}
	

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 * del atributo estado
	 *  
	 * @return Retorna el valor de estado
	 */
	public String getEstado() {
		return estado;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo estado
	 * @param Valor para el atributo estado
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}

	/**
	 * @return Retorna el atributo listaCentrosAtencion
	 */
	public ArrayList<DtoCentrosAtencion> getListaCentrosAtencion() {
		return listaCentrosAtencion;
	}

	/**
	 * listaCentrosAtencion Asigna el atributo listaCentrosAtencion
	 * 
	 * @param ArrayList<DtoCentrosAtencion>
	 */
	public void setListaCentrosAtencion(
			ArrayList<DtoCentrosAtencion> listaCentrosAtencion) {
		this.listaCentrosAtencion = listaCentrosAtencion;
	}

	/**
	 * M&eacute;todo que se encarga de retornar el atributo listadoTipoDiferencia
	 * @return ArrayList<DtoIntegridadDominio>
	 */
	public ArrayList<DtoIntegridadDominio> getListadoTipoDiferencia() {
		return listadoTipoDiferencia;
	}

	/**
	 * @param M&eacute;todo que recibe por par&aacute;metro el atributo listadoTipoDiferencia y 
	 * se encarga de asignarlo a la variable listadoTipoDiferencia
	 */
	public void setListadoTipoDiferencia(
			ArrayList<DtoIntegridadDominio> listadoTipoDiferencia) {
		this.listadoTipoDiferencia = listadoTipoDiferencia;
	}	

	/**
	 * Este m&eacute;todo se encarga de obtener el valor 
	 * del atributo listaCajeros
	
	 * @return retorna la variable listaCajeros 
	 * @author Angela Maria Aguirre 
	 */
	public ArrayList<DtoUsuarioPersona> getListaCajeros() {
		return listaCajeros;
	}

	/**
	 * Este m&eacute;todo se encarga de establecer el valor 
	 * del atributo listaCajeros
	
	 * @param valor para el atributo listaCajeros 
	 * @author Angela Maria Aguirre 
	 */
	public void setListaCajeros(ArrayList<DtoUsuarioPersona> listaCajeros) {
		this.listaCajeros = listaCajeros;
	}

	/**
	 * Este m&eacute;todo se encarga de obtener el valor 
	 * del atributo listaCajas
	
	 * @return retorna la variable listaCajas 
	 * @author Angela Maria Aguirre 
	 */
	public ArrayList<Cajas> getListaCajas() {
		return listaCajas;
	}

	/**
	 * Este m&eacute;todo se encarga de establecer el valor 
	 * del atributo listaCajas
	
	 * @param valor para el atributo listaCajas 
	 * @author Angela Maria Aguirre 
	 */
	public void setListaCajas(ArrayList<Cajas> listaCajas) {
		this.listaCajas = listaCajas;
	}
	

	/**
	 * Este m&eacute;todo se encarga de obtener el valor 
	 * del atributo consecutivoCA
	
	 * @return retorna la variable consecutivoCA 
	 * @author Angela Maria Aguirre 
	 */
	public int getConsecutivoCA() {
		return consecutivoCA;
	}

	/**
	 * Este m&eacute;todo se encarga de establecer el valor 
	 * del atributo consecutivoCA
	
	 * @param valor para el atributo consecutivoCA 
	 * @author Angela Maria Aguirre 
	 */
	public void setConsecutivoCA(int consecutivoCA) {
		this.consecutivoCA = consecutivoCA;
	}
	
	/**
	 * Este m&eacute;todo se encarga de obtener el valor 
	 * del atributo tipoDiferencia
	
	 * @return retorna la variable tipoDiferencia 
	 * @author Angela Maria Aguirre 
	 */
	public String getTipoDiferencia() {
		//return tipoDiferencia;
		return filtrosFaltanteSobrante.getTipoDiferencia();
	}

	/**
	 * Este m&eacute;todo se encarga de establecer el valor 
	 * del atributo tipoDiferencia
	
	 * @param valor para el atributo tipoDiferencia 
	 * @author Angela Maria Aguirre 
	 */
	public void setTipoDiferencia(String tipoDiferencia) {
		this.filtrosFaltanteSobrante.setTipoDiferencia(tipoDiferencia);
	}

	/**
	 * Este m&eacute;todo se encarga de obtener el valor 
	 * del atributo listadoTurnoFaltanteSobrante
	
	 * @return retorna la variable listadoTurnoFaltanteSobrante 
	 * @author Angela Maria Aguirre 
	 */
	public ArrayList<TiposMovimientoCaja> getListadoTurnoFaltanteSobrante() {
		return listadoTurnoFaltanteSobrante;
	}

	/**
	 * Este m&eacute;todo se encarga de establecer el valor 
	 * del atributo listadoTurnoFaltanteSobrante
	
	 * @param valor para el atributo listadoTurnoFaltanteSobrante 
	 * @author Angela Maria Aguirre 
	 */
	public void setListadoTurnoFaltanteSobrante(
			ArrayList<TiposMovimientoCaja> listadoTurnoFaltanteSobrante) {
		this.listadoTurnoFaltanteSobrante = listadoTurnoFaltanteSobrante;
	}	

	/**
	 * Este m&eacute;todo se encarga de obtener el valor 
	 * del atributo index
	
	 * @return retorna la variable index 
	 * @author Angela Maria Aguirre 
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * Este m&eacute;todo se encarga de establecer el valor 
	 * del atributo index
	
	 * @param valor para el atributo index 
	 * @author Angela Maria Aguirre 
	 */
	public void setIndex(int index) {
		this.index = index;
	}

	/**
	 * Este m&eacute;todo se encarga de obtener el valor 
	 * del atributo filtrosFaltanteSobrante
	
	 * @return retorna la variable filtrosFaltanteSobrante 
	 * @author Angela Maria Aguirre 
	 */
	public DTOCambioResponsableDetFaltanteSobrante getFiltrosFaltanteSobrante() {
		return filtrosFaltanteSobrante;
	}

	/**
	 * Este m&eacute;todo se encarga de establecer el valor 
	 * del atributo filtrosFaltanteSobrante
	
	 * @param valor para el atributo filtrosFaltanteSobrante 
	 * @author Angela Maria Aguirre 
	 */
	public void setFiltrosFaltanteSobrante(
			DTOCambioResponsableDetFaltanteSobrante filtrosFaltanteSobrante) {
		this.filtrosFaltanteSobrante = filtrosFaltanteSobrante;
	}

	/**
	 * Este m&eacute;todo se encarga de obtener el valor 
	 * del atributo listaDetFaltanteSobrantes
	
	 * @return retorna la variable listaDetFaltanteSobrantes 
	 * @author Angela Maria Aguirre 
	 */
	public ArrayList<DTOCambioResponsableDetFaltanteSobrante> getListaDetFaltanteSobrantes() {
		return listaDetFaltanteSobrantes;
	}

	/**
	 * Este m&eacute;todo se encarga de establecer el valor 
	 * del atributo listaDetFaltanteSobrantes
	
	 * @param valor para el atributo listaDetFaltanteSobrantes 
	 * @author Angela Maria Aguirre 
	 */
	public void setListaDetFaltanteSobrantes(
			ArrayList<DTOCambioResponsableDetFaltanteSobrante> listaDetFaltanteSobrantes) {
		this.listaDetFaltanteSobrantes = listaDetFaltanteSobrantes;
	}

	/**
	 * Este m&eacute;todo se encarga de obtener el valor 
	 * del atributo dtoDetalle
	
	 * @return retorna la variable dtoDetalle 
	 * @author Angela Maria Aguirre 
	 */
	public DTOCambioResponsableDetFaltanteSobrante getDtoDetalle() {
		return dtoDetalle;
	}

	/**
	 * Este m&eacute;todo se encarga de establecer el valor 
	 * del atributo dtoDetalle
	
	 * @param valor para el atributo dtoDetalle 
	 * @author Angela Maria Aguirre 
	 */
	public void setDtoDetalle(DTOCambioResponsableDetFaltanteSobrante dtoDetalle) {
		this.dtoDetalle = dtoDetalle;
	}

	/**
	 * @return M&eacute;todo que se encarga de retornar el atributo posArray
	 */
	public int getPosArray() {
		return posArray;
	}

	/**
	 * @param M&eacute;todo que recibe por par&aacute;metro el atributo posArray y 
	 * se encarga de asignarlo a la variable posArray
	 */
	public void setPosArray(int posArray) {
		this.posArray = posArray;
	}

	/**
	 * M&eacute;todo que se encarga de retornar el atributo patronOrdenar
	 * @return String
	 */
	public String getPatronOrdenar() {
		return patronOrdenar;
	}

	/**
	 * M&eacute;todo que recibe por par&aacute;metro el atributo patronOrdenar y 
	 * se encarga de asignarlo a la variable patronOrdenar
	 * 
	 * @param String
	 */
	public void setPatronOrdenar(String patronOrdenar) {
		this.patronOrdenar = patronOrdenar;
	}

	/**
	 * M&eacute;todo que se encarga de retornar el atributo esDescendente
	 * 
	 * @return String
	 */
	public String getEsDescendente() {
		return esDescendente;
	}

	/**
	 * M&eacute;todo que recibe por par&aacute;metro el atributo esDescendente y 
	 * se encarga de asignarlo a la variable esDescendente
	 * @param String
	 */
	public void setEsDescendente(String esDescendente) {
		this.esDescendente = esDescendente;
	}
	
	/**
	 * Este m&eacute;todo se encarga de obtener el valor 
	 * del atributo path
	
	 * @return retorna la variable path 
	 * @author Angela Maria Aguirre 
	 */
	public String getPath() {
		return path;
	}

	/**
	 * Este m&eacute;todo se encarga de establecer el valor 
	 * del atributo path
	
	 * @param valor para el atributo path 
	 * @author Angela Maria Aguirre 
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * Este m&eacute;todo se encarga de obtener el valor 
	 * del atributo mostrarMensaje
	
	 * @return retorna la variable mostrarMensaje 
	 * @author Angela Maria Aguirre 
	 */
	public String getMostrarMensaje() {
		return mostrarMensaje;
	}

	/**
	 * Este m&eacute;todo se encarga de establecer el valor 
	 * del atributo mostrarMensaje
	
	 * @param valor para el atributo mostrarMensaje 
	 * @author Angela Maria Aguirre 
	 */
	public void setMostrarMensaje(String mostrarMensaje) {
		this.mostrarMensaje = mostrarMensaje;
	}

	/**
	 * Este m&eacute;todo se encarga de obtener el valor 
	 * del atributo reglaNavegacion
	
	 * @return retorna la variable reglaNavegacion 
	 * @author Angela Maria Aguirre 
	 */
	public String getReglaNavegacion() {
		return reglaNavegacion;
	}

	/**
	 * Este m&eacute;todo se encarga de establecer el valor 
	 * del atributo reglaNavegacion
	
	 * @param valor para el atributo reglaNavegacion 
	 * @author Angela Maria Aguirre 
	 */
	public void setReglaNavegacion(String reglaNavegacion) {
		this.reglaNavegacion = reglaNavegacion;
	}


	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo listadoEstadoFaltanteSobrante
	 * 
	 * @return  Retorna la variable listadoEstadoFaltanteSobrante
	 */
	public ArrayList<DtoIntegridadDominio> getListadoEstadoFaltanteSobrante() {
		return listadoEstadoFaltanteSobrante;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo listadoEstadoFaltanteSobrante
	 * 
	 * @param  valor para el atributo listadoEstadoFaltanteSobrante 
	 */
	public void setListadoEstadoFaltanteSobrante(
			ArrayList<DtoIntegridadDominio> listadoEstadoFaltanteSobrante) {
		this.listadoEstadoFaltanteSobrante = listadoEstadoFaltanteSobrante;
	}

	/**
	 * Este M&eacute;todo se encarga de obtener el valor 
	 * del atributo listaHistorico
	
	 * @return retorna la variable listaHistorico 
	 * @author Angela Maria Aguirre 
	 */
	public ArrayList<DTOHistoCambioResponsable> getListaHistorico() {
		return listaHistorico;
	}

	/**
	 * Este M&eacute;todo se encarga de establecer el valor 
	 * del atributo listaHistorico
	
	 * @param valor para el atributo listaHistorico 
	 * @author Angela Maria Aguirre 
	 */
	public void setListaHistorico(
			ArrayList<DTOHistoCambioResponsable> listaHistorico) {
		this.listaHistorico = listaHistorico;
	}

	/**
	 * Este M&eacute;todo se encarga de obtener el valor 
	 * del atributo cajeroNuevoResponsable
	
	 * @return retorna la variable cajeroNuevoResponsable 
	 * @author Angela Maria Aguirre 
	 */
	public String getCajeroNuevoResponsable() {
		return cajeroNuevoResponsable;
	}

	/**
	 * Este m&eacute;todo se encarga de establecer el valor 
	 * del atributo cajeroNuevoResponsable
	
	 * @param valor para el atributo cajeroNuevoResponsable 
	 * @author Angela Maria Aguirre 
	 */
	public void setCajeroNuevoResponsable(String cajeroNuevoResponsable) {
		this.cajeroNuevoResponsable = cajeroNuevoResponsable;
	}

	/**
	 * Este M&eacute;todo se encarga de obtener el valor 
	 * del atributo resumen
	
	 * @return retorna la variable resumen 
	 * @author Angela Maria Aguirre 
	 */
	public Boolean getResumen() {
		return resumen;
	}

	/**
	 * Este M&eacute;todo se encarga de establecer el valor 
	 * del atributo resumen
	
	 * @param valor para el atributo resumen 
	 * @author Angela Maria Aguirre 
	 */
	public void setResumen(Boolean resumen) {
		this.resumen = resumen;
	}

	/**
	 * Este M&eacute;todo se encarga de obtener el valor 
	 * del atributo mostrarDetalle
	
	 * @return retorna la variable mostrarDetalle 
	 * @author Angela Maria Aguirre 
	 */
	public boolean isMostrarDetalle() {
		return mostrarDetalle;
	}

	/**
	 * Este M&eacute;todo se encarga de establecer el valor 
	 * del atributo mostrarDetalle
	
	 * @param valor para el atributo mostrarDetalle 
	 * @author Angela Maria Aguirre 
	 */
	public void setMostrarDetalle(boolean mostrarDetalle) {
		this.mostrarDetalle = mostrarDetalle;
	}

	/**
	 * Este M&eacute;todo se encarga de obtener el valor 
	 * del atributo habilitaConsultaHistorico
	
	 * @return retorna la variable habilitaConsultaHistorico 
	 * @author Angela Maria Aguirre 
	 */
	public String getHabilitaConsultaHistorico() {
		return habilitaConsultaHistorico;
	}

	/**
	 * Este M&eacute;todo se encarga de establecer el valor 
	 * del atributo habilitaConsultaHistorico
	
	 * @param valor para el atributo habilitaConsultaHistorico 
	 * @author Angela Maria Aguirre 
	 */
	public void setHabilitaConsultaHistorico(String habilitaConsultaHistorico) {
		this.habilitaConsultaHistorico = habilitaConsultaHistorico;
	}

		
}
