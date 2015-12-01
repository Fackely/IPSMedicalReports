package com.princetonsa.actionform.capitacion;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.util.MessageResources;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.capitacion.ConstantesCapitacion;


import com.ibm.icu.util.Calendar;
import com.princetonsa.action.capitacion.ConsultarImprimirOrdenesCapitacionSubcontratadaAction;
import com.princetonsa.enu.impresion.EnumTiposSalida;
import com.servinte.axioma.dto.capitacion.DtoContratoReporte;
import com.servinte.axioma.dto.capitacion.DtoConvenioReporte;
import com.servinte.axioma.dto.capitacion.DtoTipoConsulta;
import com.servinte.axioma.dto.comun.DtoAnio;
import com.servinte.axioma.dto.comun.DtoMes;
import com.servinte.axioma.orm.Contratos;
import com.servinte.axioma.orm.Convenios;

/**
 * Form que contiene los datos específicos para consultar e imprimir las ordenes
 * de capitacion subcontratadas
 * 
 * Además maneja el proceso de validación de errores de datos de entrada.
 * 
 * @version 1.0, Abr 18, 2011
 * @author <a href="mailto:ricardo.ruiz@servinte.com.co">Ricardo Ruiz</a>
 * @see ConsultarImprimirOrdenesCapitacionSubcontratadaAction
 * 
 */

public class ConsultarImprimirOrdenesCapitacionSubcontratadaForm extends
		ValidatorForm {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5312296239144994291L;

	/**
	 * Atributo que maneja el control del flujo de la funcionalidad
	 */
	private String estado;

	/**
	 * Atributo que representa el id del Año seleccionado para generar el
	 * reporte
	 */
	private int idAnio;

	/**
	 * Atributo que representa el id del Mes seleccionado para generar el
	 * reporte
	 */
	private int idMes;

	/**
	 * Atributo que representa el id del Tipo de Consulta seleccionado para
	 * generar el reporte
	 */
	private int idTipoConsulta;
	
	/**
	 * Atributo que representa el id del Tipo de Salida seleccionado para
	 * generar el reporte (PDF, EXCEL, PLANO)
	 */
	private String tipoSalida;
	
	/**
	 * enumeración del tipo de salida.
	 */
	private EnumTiposSalida enumTipoSalida;
	
	/**
	 * Atributo que representa el nombre del reporte generado
	 */
	private String nombreReporte;

	/**
	 * Atributo que representa el id del Convenio seleccionado para generar el
	 * reporte
	 */
	private Convenios convenio;

	/**
	 * Atributo que representa el id del Contrato seleccionado para generar el
	 * reporte
	 */
	private Contratos contrato;

	
	/**
	 * Atributo que representa la lista de los DtoAnio posibles para generar el
	 * reporte
	 */
	private List<DtoAnio> listadoAnios = new ArrayList<DtoAnio>();

	/**
	 * Atributo que representa la lista de los DtoMes de un año
	 */
	private List<DtoMes> listadoMeses = new ArrayList<DtoMes>();

	/**
	 * Atributo que representa la lista de los DtoTipoConsulta posibles para
	 * generar el reporte
	 */
	private List<DtoTipoConsulta> listadoTipoConsulta = new ArrayList<DtoTipoConsulta>();

	/**
	 * Atributo que representa la lista de los Convenios
	 */
	private ArrayList<Convenios> listadoConvenios = new ArrayList<Convenios>();

	
	/**
	 *Atributo que representa la lista de los Contratos asociados al convenio seleccionado 
	 */
	private ArrayList<Contratos> listadoContratos = new ArrayList<Contratos>();
	
	

	/** * Contiene la lista de mensajes correspondiente a esta forma */
	MessageResources messageResource = MessageResources.getMessageResources("com.servinte.mensajes.capitacion.ConsultarImprimirOrdenesCapitacionSubcontratadaForm");
	
	/**
	 *Atributo que representa la lista consolidada de los DtoConvenioReporte Autorizados
	 *para un mes y año seleccionados
	 */
	private ArrayList<DtoConvenioReporte> consolidadosConveniosAutorizados = new ArrayList<DtoConvenioReporte>();
	
	/**
	 *Atributo que representa la lista consolidada de los DtoConvenioReporte NO Autorizados
	 *para un mes y año seleccionados
	 */
	private ArrayList<DtoConvenioReporte> consolidadosConveniosNoAutorizados = new ArrayList<DtoConvenioReporte>();
	
	/**
	 *Atributo que representa el consolidado del DtoConvenioReporte 
	 *para un mes, año y convenio seleccionado
	 */
	private DtoConvenioReporte consolidadoConvenio = new DtoConvenioReporte();
	
	/**
	 *Atributo que representa el consolidado del DtoContratoReporte
	 *para un mes, año, convenio y contrato seleccionado
	 */
	private DtoContratoReporte consolidadoContrato = new DtoContratoReporte();
	
	/**
	 *Atributo que representa el mes y año seleccionados
	 */
	private String fechaSeleccionada;
	
	/**
	 * Constructor de la clase
	 */
	public ConsultarImprimirOrdenesCapitacionSubcontratadaForm() {

	}

	/**
	 * Método donde se centralizan las validaciones relacionadas con la consulta
	 * de las ordenes de capitación subcontratada.
	 */
	@Override
	public ActionErrors validate(ActionMapping mapping,
			HttpServletRequest request) {
		/*
		 * Almacenamiento de los errores encontrados
		 */
		ActionErrors errores = new ActionErrors();
		if (estado == null || estado.trim().isEmpty()) {
			errores.add("estado invalido", new ActionMessage("errors.estadoInvalido"));
			return errores;
		}
		
		if(this.estado.equals("consultarOrdenes")){
			int noValido = ConstantesBD.codigoNuncaValido;
			boolean validaciones=true;
			boolean mesAnioValido=true;
			
			if(this.getIdAnio() == noValido){
				errores.add("anio invalido", new ActionMessage("errors.required", messageResource.getMessage(
						"consultarImprimirOrdenesCapitacionSubcontratada.anio")));
				mesAnioValido=false;
				validaciones=false;
			}
			if(this.getIdMes() == noValido){
				errores.add("mes invalido", new ActionMessage("errors.required", messageResource.getMessage(
						"consultarImprimirOrdenesCapitacionSubcontratada.mes")));
				mesAnioValido=false;
				validaciones=false;
			}
			if(mesAnioValido){
				Calendar fechaActual = Calendar.getInstance();
				int anioActual=fechaActual.get(Calendar.YEAR);
				int mesActual=fechaActual.get(Calendar.MONTH);
				if((this.getIdAnio() == anioActual) && (this.getIdMes() > mesActual)){
					errores.add("mes mayor que actual", new ActionMessage("error.errorEnBlanco", messageResource.getMessage(
					"consultarImprimirOrdenesCapitacionSubcontratada.mesMayorMesActual")));
					validaciones=false;
				}
			}
			if(this.getIdTipoConsulta() == noValido){
				errores.add("tipo consulta invalido", new ActionMessage("errors.required", messageResource.getMessage(
						"consultarImprimirOrdenesCapitacionSubcontratada.tipoConsulta")));
				validaciones=false;
			}
			else{
				if(this.getIdTipoConsulta() == ConstantesCapitacion.codigoTipoConsultaGrupoSClaseI){					
					if(this.getConvenioHelper() == noValido){
						errores.add("convenio invalido", new ActionMessage("errors.required", messageResource.getMessage(
							"consultarImprimirOrdenesCapitacionSubcontratada.convenio")));
						validaciones=false;
					}
					if(this.getContratoHelper() == noValido){
						errores.add("contrato invalido", new ActionMessage("errors.required", messageResource.getMessage(
							"consultarImprimirOrdenesCapitacionSubcontratada.contrato")));
						validaciones=false;
					}
				}
				if(this.getIdTipoConsulta() == ConstantesCapitacion.codigoTipoConsultaConvenioContrato){					
					if(this.getConvenioHelper() == noValido){
						errores.add("convenio invalido", new ActionMessage("errors.required", messageResource.getMessage(
							"consultarImprimirOrdenesCapitacionSubcontratada.convenio")));
						validaciones=false;
					}
				}
			}
			if(!validaciones){
				this.setTipoSalida(null);
				this.setEnumTipoSalida(null);
			}
		}
		return errores;
	}

	/**
	 * Método para limpiar los atributos del formulario
	 */
	/**
	 * 
	 */
	public void reset() {		
		this.setIdAnio(ConstantesBD.codigoNuncaValido);
		this.setIdMes(ConstantesBD.codigoNuncaValido);
		this.setIdTipoConsulta(ConstantesBD.codigoNuncaValido);
		this.setConvenioHelper(ConstantesBD.codigoNuncaValido);
		this.setContratoHelper(ConstantesBD.codigoNuncaValido);
		this.setTipoSalida(null);
		this.setNombreReporte(null);
	}

	/**
	 * @return the estado
	 */
	public String getEstado() {
		return estado;
	}

	/**
	 * @param estado
	 *            the estado to set
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}

	/**
	 * @return the idAnio
	 */
	public int getIdAnio() {
		return idAnio;
	}

	/**
	 * @param idAnio
	 *            the idAnio to set
	 */
	public void setIdAnio(int idAnio) {
		this.idAnio = idAnio;
	}

	/**
	 * @return the idMes
	 */
	public int getIdMes() {
		return idMes;
	}

	/**
	 * @param idMes
	 *            the idMes to set
	 */
	public void setIdMes(int idMes) {
		this.idMes = idMes;
	}

	/**
	 * @return the idTipoConsulta
	 */
	public int getIdTipoConsulta() {
		return idTipoConsulta;
	}

	/**
	 * @param idTipoConsulta
	 *            the idTipoConsulta to set
	 */
	public void setIdTipoConsulta(int idTipoConsulta) {
		this.idTipoConsulta = idTipoConsulta;
	}

	

	/**
	 * @return the listadoAnios
	 */
	public List<DtoAnio> getListadoAnios() {
		return listadoAnios;
	}

	/**
	 * @param listadoAnios
	 *            the listadoAnios to set
	 */
	public void setListadoAnios(List<DtoAnio> listadoAnios) {
		this.listadoAnios = listadoAnios;
	}

	/**
	 * @return the listadoMeses
	 */
	public List<DtoMes> getListadoMeses() {
		return listadoMeses;
	}

	/**
	 * @param listadoMeses
	 *            the listadoMeses to set
	 */
	public void setListadoMeses(List<DtoMes> listadoMeses) {
		this.listadoMeses = listadoMeses;
	}

	/**
	 * @return the listadoTipoConsulta
	 */
	public List<DtoTipoConsulta> getListadoTipoConsulta() {
		return listadoTipoConsulta;
	}

	/**
	 * @param listadoTipoConsulta
	 *            the listadoTipoConsulta to set
	 */
	public void setListadoTipoConsulta(List<DtoTipoConsulta> listadoTipoConsulta) {
		this.listadoTipoConsulta = listadoTipoConsulta;
	}

	/**
	 * @return the listadoConvenios
	 */
	public ArrayList<Convenios> getListadoConvenios() {
		return listadoConvenios;
	}

	/**
	 * @param listadoConvenios
	 *            the listadoConvenios to set
	 */
	public void setListadoConvenios(ArrayList<Convenios> listadoConvenios) {
		this.listadoConvenios = listadoConvenios;
	}
	
	/**
	 * @return the listadoContratos
	 */
	public ArrayList<Contratos> getListadoContratos() {
		return listadoContratos;
	}

	/**
	 * @param listadoContratos the listadoContratos to set
	 */
	public void setListadoContratos(ArrayList<Contratos> listadoContratos) {
		this.listadoContratos = listadoContratos;
	}
	
	/**
	 * 
	 * Helper que retorna el código del Convenio seleccionado
	 * 
	 * @return int con el código del Convenio seleccionado
	 */
	public int getConvenioHelper() {

		if (convenio == null) {
			return ConstantesBD.codigoNuncaValido;
		}
		return convenio.getCodigo();
	}


	/**
     *
	 * Helper utilizado para asignar el Convenio que fue seleccionado por el usuario
	 *
	 * @param codigoConvenio
	 */
	public void setConvenioHelper(int codigoConvenio) {

		boolean asigno = false;

		for (Convenios convenioI : listadoConvenios) {
			if (convenioI.getCodigo() == codigoConvenio) {
				asigno = true;
				this.convenio = convenioI;
			}
		}

		if (!asigno) {
			this.setConvenio(null);
		}
	}
	
	
	/**
	 * 
	 * Helper que retorna el código del Contrato seleccionado
	 * 
	 * @return int con el código del Contrato seleccionado
	 */
	public int getContratoHelper() {

		if (contrato == null) {
			return ConstantesBD.codigoNuncaValido;
		}
		return contrato.getCodigo();
	}


	/**
     *
	 * Helper utilizado para asignar el Contrato que fue seleccionado por el usuario
	 *
	 * @param codigoConvenio
	 */
	public void setContratoHelper(int codigoContrato) {

		boolean asigno = false;

		for (Contratos contratoI : listadoContratos) {
			if (contratoI.getCodigo() == codigoContrato) {
				asigno = true;
				this.contrato = contratoI;
			}
		}

		if (!asigno) {
			this.setContrato(null);
		}
	}

	
	/**
	 * @return the convenio
	 */
	public Convenios getConvenio() {
		return convenio;
	}

	/**
	 * @param convenio the convenio to set
	 */
	public void setConvenio(Convenios convenio) {
		this.convenio = convenio;
	}

	/**
	 * @return the contrato
	 */
	public Contratos getContrato() {
		return contrato;
	}

	/**
	 * @param contrato the contrato to set
	 */
	public void setContrato(Contratos contrato) {
		this.contrato = contrato;
	}

	
	/**
	 * @return the consolidadoConvenio
	 */
	public DtoConvenioReporte getConsolidadoConvenio() {
		return consolidadoConvenio;
	}

	/**
	 * @param consolidadoConvenio the consolidadoConvenio to set
	 */
	public void setConsolidadoConvenio(DtoConvenioReporte consolidadoConvenio) {
		this.consolidadoConvenio = consolidadoConvenio;
	}

	/**
	 * @return the consolidadoContrato
	 */
	public DtoContratoReporte getConsolidadoContrato() {
		return consolidadoContrato;
	}

	/**
	 * @param consolidadoContrato the consolidadoContrato to set
	 */
	public void setConsolidadoContrato(DtoContratoReporte consolidadoContrato) {
		this.consolidadoContrato = consolidadoContrato;
	}

	/**
	 * @return the fechaSeleccionada
	 */
	public String getFechaSeleccionada() {
		return fechaSeleccionada;
	}

	/**
	 * @param fechaSeleccionada the fechaSeleccionada to set
	 */
	public void setFechaSeleccionada(String fechaSeleccionada) {
		this.fechaSeleccionada = fechaSeleccionada;
	}

	/**
	 * @return the consolidadosConveniosAutorizados
	 */
	public ArrayList<DtoConvenioReporte> getConsolidadosConveniosAutorizados() {
		return consolidadosConveniosAutorizados;
	}

	/**
	 * @param consolidadosConveniosAutorizados the consolidadosConveniosAutorizados to set
	 */
	public void setConsolidadosConveniosAutorizados(
			ArrayList<DtoConvenioReporte> consolidadosConveniosAutorizados) {
		this.consolidadosConveniosAutorizados = consolidadosConveniosAutorizados;
	}

	/**
	 * @return the consolidadosConveniosNoAutorizados
	 */
	public ArrayList<DtoConvenioReporte> getConsolidadosConveniosNoAutorizados() {
		return consolidadosConveniosNoAutorizados;
	}

	/**
	 * @param consolidadosConveniosNoAutorizados the consolidadosConveniosNoAutorizados to set
	 */
	public void setConsolidadosConveniosNoAutorizados(
			ArrayList<DtoConvenioReporte> consolidadosConveniosNoAutorizados) {
		this.consolidadosConveniosNoAutorizados = consolidadosConveniosNoAutorizados;
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
	 * @return the enumTipoSalida
	 */
	public EnumTiposSalida getEnumTipoSalida() {
		return enumTipoSalida;
	}

	/**
	 * @param enumTipoSalida the enumTipoSalida to set
	 */
	public void setEnumTipoSalida(EnumTiposSalida enumTipoSalida) {
		this.enumTipoSalida = enumTipoSalida;
	}

	/**
	 * @return the tipoSalida
	 */
	public String getTipoSalida() {
		return tipoSalida;
	}

	/**
	 * @param tipoSalida the tipoSalida to set
	 */
	public void setTipoSalida(String tipoSalida) {
		this.tipoSalida = tipoSalida;
	}

}
