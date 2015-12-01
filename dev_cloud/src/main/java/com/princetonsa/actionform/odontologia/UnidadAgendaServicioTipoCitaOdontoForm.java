
package com.princetonsa.actionform.odontologia;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.UtilidadTexto;

import com.princetonsa.dto.administracion.DtoIntegridadDominio;
import com.princetonsa.dto.odontologia.DtoUnidadAgendaServCitaOdonto;
import com.servinte.axioma.orm.UnidadesConsulta;

/**
 * Form que contiene los datos espec&iacute;ficos para la funcionalidad de parametrizaci&oacute;n
 * de la Unidad de Agenda-Servicio X Tipo de Cita Odontol&oacute;gica - Anexo 1119
 * 
 * Adem&aacute;s maneja el proceso de validaci&oacute;n de errores de datos de entrada.
 *
 * @author Jorge Armando Agudelo Quintero
 */
public class UnidadAgendaServicioTipoCitaOdontoForm extends ActionForm{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Atributo utilizado para el control del flujo de la aplicaci&oacute;n
	 */
	private String estado;
	
	/**
	 * Listado con los tipos de cita odontol&oacute;gicas disponibles para la parametrizaci&oacute;n.
	 */
	private ArrayList<DtoIntegridadDominio> listaTiposCitaOdontologica;
	
	/**
	 * Campo que lista las unidades de agenda de tipo Odontol&oacute;gica activas. 
	 */
	private ArrayList<UnidadesConsulta> listaUnidadAgendaOdontologica;
	
	/**
	 * listado de lo servicios odontol&oacute;gicos activos que pertenezcan a la unidad de agenda odontol&oacute;gica previamente seleccionada. 
	 */
	private ArrayList<DtoUnidadAgendaServCitaOdonto> listaServiciosOdontologicos;
	
	/**
	 * Unidad de Agenda seleccionada.
	 */
	private UnidadesConsulta unidadAgenda;
	
	/**
	 * Tipo de cita Odontol&oacute;gica seleccionada
	 */
	private DtoIntegridadDominio tiposCitaOdontologica;
	
	/**
	 * C&oacute;digo del servicio odontol&oacute;gico
	 */
	private String codigoServicioOdontologico;
	
	/**
	 * Servicio odontol&oacute;gico seleccionado por el usuario
	 */
	private DtoUnidadAgendaServCitaOdonto servicioOdontologico;
	
	/**
	 * Listado con los registros guardados en la parametrizaci&oacute;n
	 * por el Tipo de Cita, Unidad de Agenda y Servicio de Cita.
	 */
	private ArrayList<DtoUnidadAgendaServCitaOdonto> listaUnidAgenServCitaOdonRegistrados;
	
	/**
	 * Atributo con la posici&oacute;n del registro a eliminar
	 */
	private int posicionRegistroAEliminar;
	
	/**
	 * Atributo utilizado para habilitar o deshabilitar el listado de Unidades de 
	 * Agenda disponibles para la parametrizaci&oacute;n
	 */
	private boolean noActivaListaUnidadAgenda;
	
	/**
	 * Atributo utilizado para habilitar o deshabilitar el listado de Servicios de 
	 * odontol&oacute;gicos disponibles para la parametrizaci&oacute;n
	 */
	private boolean noActivaListaServiciosOdon;
	
	/**
	 * Constructor de la forma
	 */
	public UnidadAgendaServicioTipoCitaOdontoForm() {
		
		reset();
	}
	
	/**
	 * M&eacute;todo que inicializa los valores de los listados
	 * y dem&aacute;s atributos del formulario
	 */
	public void reset() {
		
		this.setEstado("");
		this.setListaTiposCitaOdontologica(new ArrayList<DtoIntegridadDominio>());
		this.setListaUnidadAgendaOdontologica(new ArrayList<UnidadesConsulta>());
		this.setListaServiciosOdontologicos(new ArrayList<DtoUnidadAgendaServCitaOdonto>());
		this.setListaUnidAgenServCitaOdonRegistrados(new ArrayList<DtoUnidadAgendaServCitaOdonto>());
		this.setPosicionRegistroAEliminar(ConstantesBD.codigoNuncaValido);
		this.setTiposCitaOdontologica(null);
		this.setUnidadAgenda(null);
		this.setServicioOdontologico(null);
		this.setCodigoServicioOdontologico("");
		this.setNoActivaListaServiciosOdon(Boolean.TRUE);
		this.setNoActivaListaUnidadAgenda(Boolean.TRUE);
	}
	
	/**
	 * M&eacute;todo que inicializa los valores de los listados
	 * y dem&aacute;s atributos del formulario para ingresar un nuevo registro
	 */
	public void inicializarNuevoRegistro() {
		
		this.setTiposCitaOdontologicaHelper("");
		this.setUnidadAgendaHelper(ConstantesBD.codigoNuncaValido);
		this.setListaServiciosOdontologicos(new ArrayList<DtoUnidadAgendaServCitaOdonto>());
		this.setPosicionRegistroAEliminar(ConstantesBD.codigoNuncaValido);
		this.setTiposCitaOdontologica(null);
		this.setUnidadAgenda(null);
		this.setServicioOdontologico(null);
		this.setCodigoServicioOdontologico("");
		this.setNoActivaListaServiciosOdon(Boolean.TRUE);
		this.setNoActivaListaUnidadAgenda(Boolean.TRUE);
	}

	/**
	 * M&eacute;todo encargado de procesar los errores presentados
	 * @param forma
	 */
	@Override
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {

		ActionErrors errores = new ActionErrors();

		if (UtilidadTexto.isEmpty(estado)) {
			
			errores.add("estado invalido", new ActionMessage("errors.estadoInvalido"));
			return errores;

		} else if (estado.equals("guardar")) {

			if (getTiposCitaOdontologica() != null) 
			{
				if(getUnidadAgenda() == null){
					
					errores.add("valor requerido", new ActionMessage("errors.required", "La Unidad de Agenda"));
				}

				if (getServicioOdontologico() == null) {
					
					errores.add("valor requerido", new ActionMessage("errors.required", "El Servicio"));
				}
			}
		}
		
		return errores;
	}

	/**
	 * @param estado the estado to set
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}

	/**
	 * @return the estado
	 */
	public String getEstado() {
		return estado;
	}

	/**
	 * @param listaTiposCitaOdontologica the listaTiposCitaOdontologica to set
	 */
	public void setListaTiposCitaOdontologica(
			ArrayList<DtoIntegridadDominio> listaTiposCitaOdontologica) {
		this.listaTiposCitaOdontologica = listaTiposCitaOdontologica;
	}

	/**
	 * @return the listaTiposCitaOdontologica
	 */
	public ArrayList<DtoIntegridadDominio> getListaTiposCitaOdontologica() {
		return listaTiposCitaOdontologica;
	}

	/**
	 * @param listaUnidadAgendaOdontologica the listaUnidadAgendaOdontologica to set
	 */
	public void setListaUnidadAgendaOdontologica(
			ArrayList<UnidadesConsulta> listaUnidadAgendaOdontologica) {
		this.listaUnidadAgendaOdontologica = listaUnidadAgendaOdontologica;
	}

	/**
	 * @return the listaUnidadAgendaOdontologica
	 */
	public ArrayList<UnidadesConsulta> getListaUnidadAgendaOdontologica() {
		return listaUnidadAgendaOdontologica;
	}

	/**
	 * @param unidadAgenda the unidadAgenda to set
	 */
	public void setUnidadAgenda(UnidadesConsulta unidadAgenda) {
		this.unidadAgenda = unidadAgenda;
	}

	/**
	 * @return the unidadAgenda
	 */
	public UnidadesConsulta getUnidadAgenda() {
		return unidadAgenda;
	}

	
	/**
	 * M&eacute;todo que asigna la Unidad de Agenda seleccionada desde el listado.
	 * 
	 * @param unidadAgenda the unidadAgenda to set
	 */
	public void setUnidadAgendaHelper (int codigoUnidadAgenda) {
		
		boolean asigno = false;

		if (listaUnidadAgendaOdontologica != null) {
			for (UnidadesConsulta unidadAgenda : listaUnidadAgendaOdontologica) {
				if (unidadAgenda.getCodigo() == codigoUnidadAgenda) {
					asigno = true;
					this.setUnidadAgenda(unidadAgenda);
				}
			}
		}

		if (!asigno) {

			this.setUnidadAgenda(null);
		}
	}

	/**
	 * @return c&oacute;digo de la unidad de agenda seleccionada.
	 */
	public int getUnidadAgendaHelper() {
		
		if(unidadAgenda!=null){
			
			return unidadAgenda.getCodigo();
			
		}else{
			
			return ConstantesBD.codigoNuncaValido;
		}
	}
	
	/**
	 * @param listaServiciosOdontologicos the listaServiciosOdontologicos to set
	 */
	public void setListaServiciosOdontologicos(ArrayList<DtoUnidadAgendaServCitaOdonto> listaServiciosOdontologicos) {
		
		this.listaServiciosOdontologicos = listaServiciosOdontologicos;
	}

	/**
	 * @return the listaServiciosOdontologicos
	 */
	public ArrayList<DtoUnidadAgendaServCitaOdonto> getListaServiciosOdontologicos() {
		
		return listaServiciosOdontologicos;
	}

	/**
	 * @param tiposCitaOdontologica the tiposCitaOdontologica to set
	 */
	public void setTiposCitaOdontologica(DtoIntegridadDominio tiposCitaOdontologica) {
		this.tiposCitaOdontologica = tiposCitaOdontologica;
	}

	/**
	 * @return the tiposCitaOdontologica
	 */
	public DtoIntegridadDominio getTiposCitaOdontologica() {
		return tiposCitaOdontologica;
	}
	
	/**
	 * M&eacute;todo que asigna el Tipo de Cita odontol&oacute;gica seleccionada desde el listado.
	 * 
	 * @param tiposCitaOdontologica the tiposCitaOdontologica to set
	 */
	public void setTiposCitaOdontologicaHelper(String acronimoTipoCitaOdontologica) {
		
		boolean asigno = false;

		if (listaTiposCitaOdontologica != null) {
			for (DtoIntegridadDominio tipoCitaOdontologica : listaTiposCitaOdontologica) {
				if (tipoCitaOdontologica.getAcronimo().equals(acronimoTipoCitaOdontologica)) {
					asigno = true;
					this.setTiposCitaOdontologica(tipoCitaOdontologica);
				}
			}
		}
		
		if (!asigno) {

			this.setTiposCitaOdontologica(null);
		}
	}

	/**
	 * @return String con el acr&oacute;nimo del tipo de cita odontol&oacute;gica seleccionada
	 */
	public String getTiposCitaOdontologicaHelper() {
		
		if(tiposCitaOdontologica!=null)
		{
			return tiposCitaOdontologica.getAcronimo();
		
		}
		
		return ConstantesBD.codigoNuncaValido+"";
	}


	/**
	 * @param servicioOdontologico the servicioOdontologico to set
	 */
	public void setServicioOdontologico(DtoUnidadAgendaServCitaOdonto servicioOdontologico) {
		this.servicioOdontologico = servicioOdontologico;
	}

	/**
	 * @return the servicioOdontologico
	 */
	public DtoUnidadAgendaServCitaOdonto getServicioOdontologico() {
		return servicioOdontologico;
	}
	
	/**
	 * @return the codigoServicioOdontologico
	 */
	public String getCodigoServicioOdontologico() {
		
		codigoServicioOdontologico = getServicioOdontologicoHelper()+"";
		return codigoServicioOdontologico;
	}

	/**
	 * @param codigoServicioOdontologico the codigoServicioOdontologico to set
	 */
	public void setCodigoServicioOdontologico(String codigoServicioOdontologico) {
		
		this.codigoServicioOdontologico = codigoServicioOdontologico;
		
		if(UtilidadTexto.isNumber(codigoServicioOdontologico)){
			
			setServicioOdontologicoHelper(Integer.parseInt(codigoServicioOdontologico));
		}
	}
	
	/**
	 * 
	 * M&eacute;todo que asigna el Servicio Odontol&oacute;gico seleccionado desde el listado.
	 * 
	 * @param servicioCitaOdontologica the servicioCitaOdontologica to set
	 */
	public void setServicioOdontologicoHelper(int codigoServicio) {
		
		Log4JManager.info("llegue al metodo ************ " + codigoServicio);
		
		boolean asigno = false;

		if (listaServiciosOdontologicos != null) {
			for (DtoUnidadAgendaServCitaOdonto servicioOdontologico : listaServiciosOdontologicos) {
				if (servicioOdontologico.getCodigoServicio()== codigoServicio) {
					asigno = true;
					this.setServicioOdontologico(servicioOdontologico);
				}
			}
		}
		
		if (!asigno) {

			this.setServicioOdontologico(null);
		}
	}

	/**
	 * @return the servicioCitaOdontologica
	 */
	public int getServicioOdontologicoHelper() {
		
		if(servicioOdontologico!=null)
		{
			return servicioOdontologico.getCodigoServicio();
		}
		
		return ConstantesBD.codigoNuncaValido;
	}

	
	/**
	 * @param listaUnidAgenServCitaOdonRegistrados the listaUnidAgenServCitaOdonRegistrados to set
	 */
	public void setListaUnidAgenServCitaOdonRegistrados(
			ArrayList<DtoUnidadAgendaServCitaOdonto> listaUnidAgenServCitaOdonRegistrados) {
		this.listaUnidAgenServCitaOdonRegistrados = listaUnidAgenServCitaOdonRegistrados;
	}

	/**
	 * @return the listaUnidAgenServCitaOdonRegistrados
	 */
	public ArrayList<DtoUnidadAgendaServCitaOdonto> getListaUnidAgenServCitaOdonRegistrados() {
		return listaUnidAgenServCitaOdonRegistrados;
	}

	/**
	 * @param posicionRegistroAEliminar the posicionRegistroAEliminar to set
	 */
	public void setPosicionRegistroAEliminar(int posicionRegistroAEliminar) {
		this.posicionRegistroAEliminar = posicionRegistroAEliminar;
	}

	/**
	 * @return the posicionRegistroAEliminar
	 */
	public int getPosicionRegistroAEliminar() {
		return posicionRegistroAEliminar;
	}

	/**
	 * @param noActivaListaUnidadAgenda the noActivaListaUnidadAgenda to set
	 */
	public void setNoActivaListaUnidadAgenda(boolean noActivaListaUnidadAgenda) {
		this.noActivaListaUnidadAgenda = noActivaListaUnidadAgenda;
	}

	/**
	 * @return the noActivaListaUnidadAgenda
	 */
	public boolean isNoActivaListaUnidadAgenda() {
		return noActivaListaUnidadAgenda;
	}

	/**
	 * @param noActivaListaServiciosOdon the noActivaListaServiciosOdon to set
	 */
	public void setNoActivaListaServiciosOdon(boolean noActivaListaServiciosOdon) {
		this.noActivaListaServiciosOdon = noActivaListaServiciosOdon;
	}

	/**
	 * @return the noActivaListaServiciosOdon
	 */
	public boolean isNoActivaListaServiciosOdon() {
		return noActivaListaServiciosOdon;
	}

	
	
}
