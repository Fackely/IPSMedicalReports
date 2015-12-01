package com.princetonsa.actionform.facturacion;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.util.MessageResources;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;

import com.ibm.icu.util.Calendar;
import com.princetonsa.action.facturacion.TotalServiciosArticulosValorizadosTipoConvenioAction;
import com.princetonsa.dto.capitacion.DtoNivelesAtencionTotalServiciosArticulosValorizadosPorConvenio;
import com.princetonsa.enu.impresion.EnumTiposSalida;
import com.servinte.axioma.dto.capitacion.DtoContratoReporte;
import com.servinte.axioma.dto.capitacion.DtoConvenioReporte;
import com.servinte.axioma.dto.capitacion.DtoTipoConsulta;
import com.servinte.axioma.dto.comun.DtoAnio;
import com.servinte.axioma.dto.comun.DtoMes;
import com.servinte.axioma.orm.Contratos;
import com.servinte.axioma.orm.Convenios;


/**
 * Form que contiene los datos específicos para consultar e imprimir el reporte
 * total de servicios/articulos valorizados por tipo y convenio
 * 
 * Además maneja el proceso de validación de errores de datos de entrada.
 * 
 * @version 1.0, Abr 25, 2011
 * @author <a href="mailto:ricardo.ruiz@servinte.com.co">Ricardo Ruiz</a>
 * @see TotalServiciosArticulosValorizadosTipoConvenioAction
 * 
 */
public class TotalServiciosArticulosValorizadosTipoConvenioForm extends	ValidatorForm {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4052316820577300683L;


	
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
	 * Atributo que representa el id del Mes Inicio seleccionado para generar el
	 * reporte
	 */
	private int idMesInicio;
	
	/**
	 * Atributo que representa el id del Mes Fin seleccionado para generar el
	 * reporte
	 */
	private int idMesFin;

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
	 * Atributo que representa el nombre del reporte generado
	 */
	private String nombreReporte;
	
	/**
	 * enumeración del tipo de salida.
	 */
	private EnumTiposSalida enumTipoSalida;
	
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
	 * Atributo que representa el tipo de detalle del reporte de la sección Detallado Por Servicios -> Nivel de Atención
	 */
	private boolean servicioNivel;
	
	/**
	 * Atributo que representa el tipo de detalle del reporte de la sección 
	 * Detallado Por Servicios -> Grupos de Servicios
	 */
	private boolean servicioGrupo;
	
	/**
	 * Atributo que representa el tipo de detalle del reporte de la sección 
	 * Detallado Por Servicios -> Servicio
	 */
	private boolean servicioServicio;
	
	/**
	 * Atributo que representa el tipo de detalle del reporte de la sección 
	 * Detallado Por Medicamentos/Insumos -> Nivel de Atención
	 */
	private boolean medicamentoNivel;
	
	/**
	 * Atributo que representa el tipo de detalle del reporte de la sección 
	 * Detallado Por Medicamentos/Insumos -> Clase de Medicamentos/Insumos
	 */
	private boolean medicamentoClase;
	
	/**
	 * Atributo que representa el tipo de detalle del reporte de la sección 
	 * Detallado Por Medicamentos/Insumos -> Medicamento/Insumo
	 */
	private boolean medicamentoInsumo;
	
	/**
	 * Atributo que representa el consolidado de servicios para el reporte de la sección 
	 * Detallado Por Servicios
	 */
	private ArrayList<DtoNivelesAtencionTotalServiciosArticulosValorizadosPorConvenio> consolidadoServicios = new ArrayList<DtoNivelesAtencionTotalServiciosArticulosValorizadosPorConvenio>();  
	
	/**
	 * Atributo que representa el consolidado de ariticulos para el reporte de la sección 
	 * Detallado Por Medicamentos/Insumos
	 */
	private ArrayList<DtoNivelesAtencionTotalServiciosArticulosValorizadosPorConvenio> consolidadoArticulos = new ArrayList<DtoNivelesAtencionTotalServiciosArticulosValorizadosPorConvenio>();
	

	/** * Id Formato reporte para Servicios */
	private int idFormatoServicios;
	
	/** * Id Tipo Reporte Articulos */
	private int idFormatoArticulos;
	
		
	/**
	 * Atributo que representa la lista de los DtoAnio posibles para generar el
	 * reporte
	 */
	private List<DtoAnio> listadoAnios = new ArrayList<DtoAnio>();

	/**
	 * Atributo que representa la lista de los DtoMes de un año para el mes Inicio
	 */
	private List<DtoMes> listadoMesesInicio = new ArrayList<DtoMes>();
	
	/**
	 * Atributo que representa la lista de los DtoMes de un año para el mes Fin
	 */
	private List<DtoMes> listadoMesesFin = new ArrayList<DtoMes>();

	/**
	 * Atributo que representa la lista de los DtoTipoConsulta posibles para
	 * generar el reporte
	 */
	private List<DtoTipoConsulta> listadoTipoConsulta = new ArrayList<DtoTipoConsulta>();

	/**
	 * Atributo que representa la lista de los DtoConvenio
	 */
	private ArrayList<Convenios> listadoConvenios = new ArrayList<Convenios>();

	
	/**
	 *Atributo que representa la lista de los DtoContrato asociados al convenio seleccionado 
	 */
	private ArrayList<Contratos> listadoContratos = new ArrayList<Contratos>();
	
	
	/**
	 * Atributo que contiene los consolidados totales de Grupos de Servicios
	 * y/o Clases de inventarios
	 */
	private DtoConvenioReporte totalesGruposClases = new DtoConvenioReporte();
	
	/**
	 * Atributo que contiene los consolidados totales de Servicios
	 * y/o Medicamentos/Insumos
	 */
	private DtoConvenioReporte totalesServiciosMedicamentos = new DtoConvenioReporte();
	
	/**
	 * Atributo que contiene los consolidados totales de Niveles de atención
	 */
	private DtoContratoReporte totalesNiveles = new DtoContratoReporte();
	
	/**
	 * Atributo que representa si en el reporte se muestra la sección del reporte
	 * Detallado por: Nivel de Atención
	 */
	private boolean visibleNivel;
	
	/**
	 * Atributo que representa si en el reporte se muestra la sección del reporte
	 * Detallado por: Grupo de Servicios y/o Clase de Inventarios
	 */
	private boolean visibleGruposClases;
	
	/**
	 * Atributo que representa si en el reporte se muestra la sección del reporte
	 * Detallado por: Servicios y/o Medicamentos/Insumos
	 */
	private boolean visibleServiciosMedicamentos;
	

	/** * Contiene la lista de mensajes correspondiente a esta forma */
	MessageResources messageResource = MessageResources.getMessageResources("com.servinte.mensajes.facturacion.TotalServiciosArticulosValorizadosTipoConvenioForm");
	
	/**
	 * Constructor de la clase
	 */
	public TotalServiciosArticulosValorizadosTipoConvenioForm() {

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
		
		if(this.estado.equals("buscarTotales")){
			int noValido = ConstantesBD.codigoNuncaValido;
			boolean mesInicio=true;
			boolean mesFin=true;
			boolean validaciones=true;
			
			if(this.getIdAnio() == noValido){
				errores.add("anio invalido", new ActionMessage("errors.required", messageResource.getMessage(
						"totalServiciosArticulosValorizadosTipoConvenio.anio")));
				validaciones=false;
			}
			if(this.getIdMesInicio() == noValido){
				errores.add("mes inicio invalido", new ActionMessage("errors.required", messageResource.getMessage(
						"totalServiciosArticulosValorizadosTipoConvenio.mesInicio")));
				mesInicio=false;
				validaciones=false;
			}
			if(this.getIdMesFin() == noValido){
				errores.add("mes fin invalido", new ActionMessage("errors.required", messageResource.getMessage(
						"totalServiciosArticulosValorizadosTipoConvenio.mesFin")));
				mesFin=false;
				validaciones=false;
			}
			if(mesInicio && mesFin){
				Calendar fechaActual = Calendar.getInstance();
				int anioActual=fechaActual.get(Calendar.YEAR);
				int mesActual=fechaActual.get(Calendar.MONTH);
				if(this.getIdMesInicio()>this.getIdMesFin()){
					errores.add("mes inicio mayor al fin", new ActionMessage("error.errorEnBlanco", messageResource.getMessage(
						"totalServiciosArticulosValorizadosTipoConvenio.mesInicialMayorMesFinal")));
					validaciones=false;
				}
				if((this.getIdAnio() == anioActual) && (this.getIdMesFin()>mesActual)){
					errores.add("mes fin mayor al actual", new ActionMessage("error.errorEnBlanco", messageResource.getMessage(
						"totalServiciosArticulosValorizadosTipoConvenio.mesFinalMayorMesSistema")));
					validaciones=false;
				}
			}
			if(this.getIdTipoConsulta() == noValido){
				errores.add("tipo consulta invalido", new ActionMessage("errors.required", messageResource.getMessage(
						"totalServiciosArticulosValorizadosTipoConvenio.tipoConsulta")));
				validaciones=false;
			}
			if(this.getConvenioHelper() == noValido){
				errores.add("convenio invalido", new ActionMessage("errors.required", messageResource.getMessage(
						"totalServiciosArticulosValorizadosTipoConvenio.convenio")));
				validaciones=false;
			}
			if(!this.isServicioNivel() && !this.isServicioGrupo() && !this.isServicioServicio() 
					&& !this.isMedicamentoNivel() && !this.isMedicamentoClase() && !this.isMedicamentoInsumo()){
				errores.add("no selecciona filtro", new ActionMessage("error.errorEnBlanco", messageResource.getMessage(
				"totalServiciosArticulosValorizadosTipoConvenio.filtrosInvalidos")));
				validaciones=false;
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
		this.setIdMesInicio(ConstantesBD.codigoNuncaValido);
		this.setIdMesFin(ConstantesBD.codigoNuncaValido);
		this.setIdTipoConsulta(ConstantesBD.codigoNuncaValido);
		this.setConvenioHelper(ConstantesBD.codigoNuncaValido);
		this.setContratoHelper(ConstantesBD.codigoNuncaValido);
		this.setListadoConvenios(new ArrayList<Convenios>());
		this.setListadoContratos(new ArrayList<Contratos>());
		this.setServicioNivel(false);
		this.setServicioGrupo(false);
		this.setServicioServicio(false);
		this.setMedicamentoNivel(false);
		this.setMedicamentoClase(false);
		this.setMedicamentoInsumo(false);
		this.setEnumTipoSalida(null);
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
	 * @param estado the estado to set
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
	 * @param idAnio the idAnio to set
	 */
	public void setIdAnio(int idAnio) {
		this.idAnio = idAnio;
	}

	/**
	 * @return the idMesInicio
	 */
	public int getIdMesInicio() {
		return idMesInicio;
	}

	/**
	 * @param idMesInicio the idMesInicio to set
	 */
	public void setIdMesInicio(int idMesInicio) {
		this.idMesInicio = idMesInicio;
	}

	/**
	 * @return the idMesFin
	 */
	public int getIdMesFin() {
		return idMesFin;
	}

	/**
	 * @param idMesFin the idMesFin to set
	 */
	public void setIdMesFin(int idMesFin) {
		this.idMesFin = idMesFin;
	}

	/**
	 * @return the idTipoConsulta
	 */
	public int getIdTipoConsulta() {
		return idTipoConsulta;
	}

	/**
	 * @param idTipoConsulta the idTipoConsulta to set
	 */
	public void setIdTipoConsulta(int idTipoConsulta) {
		this.idTipoConsulta = idTipoConsulta;
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
	 * @return the listadoAnios
	 */
	public List<DtoAnio> getListadoAnios() {
		return listadoAnios;
	}

	/**
	 * @param listadoAnios the listadoAnios to set
	 */
	public void setListadoAnios(List<DtoAnio> listadoAnios) {
		this.listadoAnios = listadoAnios;
	}

	/**
	 * @return the listadoMesesInicio
	 */
	public List<DtoMes> getListadoMesesInicio() {
		return listadoMesesInicio;
	}

	/**
	 * @param listadoMesesInicio the listadoMesesInicio to set
	 */
	public void setListadoMesesInicio(List<DtoMes> listadoMesesInicio) {
		this.listadoMesesInicio = listadoMesesInicio;
	}

	/**
	 * @return the listadoMesesFin
	 */
	public List<DtoMes> getListadoMesesFin() {
		return listadoMesesFin;
	}

	/**
	 * @param listadoMesesFin the listadoMesesFin to set
	 */
	public void setListadoMesesFin(List<DtoMes> listadoMesesFin) {
		this.listadoMesesFin = listadoMesesFin;
	}

	/**
	 * @return the listadoTipoConsulta
	 */
	public List<DtoTipoConsulta> getListadoTipoConsulta() {
		return listadoTipoConsulta;
	}

	/**
	 * @param listadoTipoConsulta the listadoTipoConsulta to set
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
	 * @param listadoConvenios the listadoConvenios to set
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
	 * @return the totalesGruposClases
	 */
	public DtoConvenioReporte getTotalesGruposClases() {
		return totalesGruposClases;
	}

	/**
	 * @param totalesGruposClases the totalesGruposClases to set
	 */
	public void setTotalesGruposClases(DtoConvenioReporte totalesGruposClases) {
		this.totalesGruposClases = totalesGruposClases;
	}

	/**
	 * @return the totalesServiciosMedicamentos
	 */
	public DtoConvenioReporte getTotalesServiciosMedicamentos() {
		return totalesServiciosMedicamentos;
	}

	/**
	 * @param totalesServiciosMedicamentos the totalesServiciosMedicamentos to set
	 */
	public void setTotalesServiciosMedicamentos(
			DtoConvenioReporte totalesServiciosMedicamentos) {
		this.totalesServiciosMedicamentos = totalesServiciosMedicamentos;
	}

	/**
	 * @return the totalesNiveles
	 */
	public DtoContratoReporte getTotalesNiveles() {
		return totalesNiveles;
	}

	/**
	 * @param totalesNiveles the totalesNiveles to set
	 */
	public void setTotalesNiveles(DtoContratoReporte totalesNiveles) {
		this.totalesNiveles = totalesNiveles;
	}

	/**
	 * @return the visibleNivel
	 */
	public boolean isVisibleNivel() {
		return visibleNivel;
	}

	/**
	 * @param visibleNivel the visibleNivel to set
	 */
	public void setVisibleNivel(boolean visibleNivel) {
		this.visibleNivel = visibleNivel;
	}

	/**
	 * @return the visibleGruposClases
	 */
	public boolean isVisibleGruposClases() {
		return visibleGruposClases;
	}

	/**
	 * @param visibleGruposClases the visibleGruposClases to set
	 */
	public void setVisibleGruposClases(boolean visibleGruposClases) {
		this.visibleGruposClases = visibleGruposClases;
	}

	/**
	 * @return the visibleServiciosMedicamentos
	 */
	public boolean isVisibleServiciosMedicamentos() {
		return visibleServiciosMedicamentos;
	}

	/**
	 * @param visibleServiciosMedicamentos the visibleServiciosMedicamentos to set
	 */
	public void setVisibleServiciosMedicamentos(boolean visibleServiciosMedicamentos) {
		this.visibleServiciosMedicamentos = visibleServiciosMedicamentos;
	}

	/**
	 * @return the servicioNivel
	 */
	public boolean isServicioNivel() {
		return servicioNivel;
	}

	/**
	 * @param servicioNivel the servicioNivel to set
	 */
	public void setServicioNivel(boolean servicioNivel) {
		this.servicioNivel = servicioNivel;
	}

	/**
	 * @return the servicioGrupo
	 */
	public boolean isServicioGrupo() {
		return servicioGrupo;
	}

	/**
	 * @param servicioGrupo the servicioGrupo to set
	 */
	public void setServicioGrupo(boolean servicioGrupo) {
		this.servicioGrupo = servicioGrupo;
	}

	/**
	 * @return the servicioServicio
	 */
	public boolean isServicioServicio() {
		return servicioServicio;
	}

	/**
	 * @param servicioServicio the servicioServicio to set
	 */
	public void setServicioServicio(boolean servicioServicio) {
		this.servicioServicio = servicioServicio;
	}

	/**
	 * @return the medicamentoNivel
	 */
	public boolean isMedicamentoNivel() {
		return medicamentoNivel;
	}

	/**
	 * @param medicamentoNivel the medicamentoNivel to set
	 */
	public void setMedicamentoNivel(boolean medicamentoNivel) {
		this.medicamentoNivel = medicamentoNivel;
	}

	/**
	 * @return the medicamentoClase
	 */
	public boolean isMedicamentoClase() {
		return medicamentoClase;
	}

	/**
	 * @param medicamentoClase the medicamentoClase to set
	 */
	public void setMedicamentoClase(boolean medicamentoClase) {
		this.medicamentoClase = medicamentoClase;
	}

	/**
	 * @return the medicamentoInsumo
	 */
	public boolean isMedicamentoInsumo() {
		return medicamentoInsumo;
	}

	/**
	 * @param medicamentoInsumo the medicamentoInsumo to set
	 */
	public void setMedicamentoInsumo(boolean medicamentoInsumo) {
		this.medicamentoInsumo = medicamentoInsumo;
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
	 * @return the consolidadoServicios
	 */
	public ArrayList<DtoNivelesAtencionTotalServiciosArticulosValorizadosPorConvenio> getConsolidadoServicios() {
		return consolidadoServicios;
	}

	/**
	 * @param consolidadoServicios the consolidadoServicios to set
	 */
	public void setConsolidadoServicios(
			ArrayList<DtoNivelesAtencionTotalServiciosArticulosValorizadosPorConvenio> consolidadoServicios) {
		this.consolidadoServicios = consolidadoServicios;
	}

	/**
	 * @return the consolidadoArticulos
	 */
	public ArrayList<DtoNivelesAtencionTotalServiciosArticulosValorizadosPorConvenio> getConsolidadoArticulos() {
		return consolidadoArticulos;
	}

	/**
	 * @param consolidadoArticulos the consolidadoArticulos to set
	 */
	public void setConsolidadoArticulos(
			ArrayList<DtoNivelesAtencionTotalServiciosArticulosValorizadosPorConvenio> consolidadoArticulos) {
		this.consolidadoArticulos = consolidadoArticulos;
	}

	/**
	 * @return the idFormatoServicios
	 */
	public int getIdFormatoServicios() {
		return idFormatoServicios;
	}

	/**
	 * @param idFormatoServicios the idFormatoServicios to set
	 */
	public void setIdFormatoServicios(int idFormatoServicios) {
		this.idFormatoServicios = idFormatoServicios;
	}

	/**
	 * @return the idFormatoArticulos
	 */
	public int getIdFormatoArticulos() {
		return idFormatoArticulos;
	}

	/**
	 * @param idFormatoArticulos the idFormatoArticulos to set
	 */
	public void setIdFormatoArticulos(int idFormatoArticulos) {
		this.idFormatoArticulos = idFormatoArticulos;
	}
	
	

}
