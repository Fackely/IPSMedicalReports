package com.princetonsa.actionform.tesoreria;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.util.MessageResources;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.constantes.estadosJsp.IconstantesEstadosJsp;

import com.princetonsa.dto.administracion.DtoIntegridadDominio;
import com.princetonsa.dto.tesoreria.DtoConsolidadoCierreReporte;
import com.servinte.axioma.orm.CentroAtencion;
import com.servinte.axioma.orm.EmpresasInstitucion;
import com.servinte.axioma.orm.FormasPago;
import com.servinte.axioma.orm.Instituciones;

public class ConsultaConsolidadoCierresForm extends ValidatorForm {

	/**
	 * constantes de serialziar
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Estado de flujo
	 */
	private String estado;

	/**
	 * Lista de combos de tipo consolidado
	 */
	private ArrayList<DtoIntegridadDominio> listaComboTipoConsolidado;

	/**
	 * Es multiempresa
	 */
	private Boolean esMultiempresa;

	/**
	 * Lista de combos de institucion
	 */
	private ArrayList<EmpresasInstitucion> listaComboInsitucion;

	/**
	 * lista de centros d eatencion
	 */
	private ArrayList<CentroAtencion> listaCentrosAtencion;

	/**
	 * fecha inicial
	 */
	private String fechaInicial = "";

	/**
	 * tipo de consolidado
	 */
	private String tipoConsolidadoSeleccionado = "";

	/**
	 * Institcion seleccionada
	 */
	private String institucionSeleccionada = "";

	/**
	 * centro de atencion seleccionado
	 */
	private String centroAtencionSeleccionado = "";

	/**
	 * consolidado por centro de atencion
	 */
	private ArrayList<DtoConsolidadoCierreReporte> consolidadoCierres = new ArrayList<DtoConsolidadoCierreReporte>();

	/**
	 * consolidado por cierre cajas cajero
	 */
	private ArrayList<DtoConsolidadoCierreReporte> consolidadoCierresCajaCajero = new ArrayList<DtoConsolidadoCierreReporte>();

	/**
	 * Lista de totales de centro de atencion
	 */
	private ArrayList<DtoConsolidadoCierreReporte> totalesCentroAtencion = new ArrayList<DtoConsolidadoCierreReporte>();

	/**
	 * tipo de salida de reporte
	 */
	private String tipoSalida = "";

	/**
	 * nombre de archi generado
	 */
	private String nombreArchivoGenerado = "";

	/**
	 * instituciones
	 */
	private ArrayList<String> institucionesCajaCajero = new ArrayList<String>();

	/**
	 * centros de atencion
	 */
	private ArrayList<String> centrosAtencionCajaCajero = new ArrayList<String>();

	/**
	 * Mensajes parametrizados de error.
	 */
	private MessageResources messageResource = MessageResources
			.getMessageResources("com.servinte.mensajes.tesoreria.ConsultarConsolidadosCierres");

	/**
	 * Log de aplicacion
	 */
	private Logger logger = Logger
			.getLogger(ConsultaConsolidadoCierresForm.class);

	/**
	 * Lista con las formas de pago parametrizadas en el sistema
	 */
	private List<FormasPago> formasPago = new ArrayList<FormasPago>();

	/**
	 * Cantidad de colspan para mostrar en jsp
	 */
	private Integer cantidadColSpanCierres = new Integer(0);

	/**
	 * Lista de instituciones en el sistema
	 */
	private ArrayList<Instituciones> listaInstituciones = new ArrayList<Instituciones>();

	/**
	 * Lista de centro de atencion en el sistema
	 */
	private ArrayList<CentroAtencion> listaCentros = new ArrayList<CentroAtencion>();

	public ConsultaConsolidadoCierresForm() {
		this.listaComboTipoConsolidado = new ArrayList<DtoIntegridadDominio>();
		this.listaComboInsitucion = new ArrayList<EmpresasInstitucion>();
		this.listaCentrosAtencion = new ArrayList<CentroAtencion>();
		this.consolidadoCierres = new ArrayList<DtoConsolidadoCierreReporte>();
		this.formasPago = new ArrayList<FormasPago>();
		this.cantidadColSpanCierres = new Integer(0);
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
	 * @return the listaComboTipoConsolidado
	 */
	public ArrayList<DtoIntegridadDominio> getListaComboTipoConsolidado() {
		return listaComboTipoConsolidado;
	}

	/**
	 * @param listaComboTipoConsolidado
	 *            the listaComboTipoConsolidado to set
	 */
	public void setListaComboTipoConsolidado(
			ArrayList<DtoIntegridadDominio> listaComboTipoConsolidado) {
		this.listaComboTipoConsolidado = listaComboTipoConsolidado;
	}

	/**
	 * @return the esMultiempresa
	 */
	public Boolean getEsMultiempresa() {
		return esMultiempresa;
	}

	/**
	 * @param esMultiempresa
	 *            the esMultiempresa to set
	 */
	public void setEsMultiempresa(Boolean esMultiempresa) {
		this.esMultiempresa = esMultiempresa;
	}

	/**
	 * @return the listaComboInsitucion
	 */
	public ArrayList<EmpresasInstitucion> getListaComboInsitucion() {
		return listaComboInsitucion;
	}

	/**
	 * @param listaComboInsitucion
	 *            the listaComboInsitucion to set
	 */
	public void setListaComboInsitucion(
			ArrayList<EmpresasInstitucion> listaComboInsitucion) {
		this.listaComboInsitucion = listaComboInsitucion;
	}

	/**
	 * @return the listaCentrosAtencion
	 */
	public ArrayList<CentroAtencion> getListaCentrosAtencion() {
		return listaCentrosAtencion;
	}

	/**
	 * @param listaCentrosAtencion
	 *            the listaCentrosAtencion to set
	 */
	public void setListaCentrosAtencion(
			ArrayList<CentroAtencion> listaCentrosAtencion) {
		this.listaCentrosAtencion = listaCentrosAtencion;
	}

	/**
	 * @return the fechaInicial
	 */
	public String getFechaInicial() {
		return fechaInicial;
	}

	/**
	 * @param fechaInicial
	 *            the fechaInicial to set
	 */
	public void setFechaInicial(String fechaInicial) {
		this.fechaInicial = fechaInicial;
	}

	/**
	 * @return the tipoConsolidadoSeleccionado
	 */
	public String getTipoConsolidadoSeleccionado() {
		return tipoConsolidadoSeleccionado;
	}

	/**
	 * @param tipoConsolidadoSeleccionado
	 *            the tipoConsolidadoSeleccionado to set
	 */
	public void setTipoConsolidadoSeleccionado(
			String tipoConsolidadoSeleccionado) {
		this.tipoConsolidadoSeleccionado = tipoConsolidadoSeleccionado;
	}

	/**
	 * @return the institucionSeleccionada
	 */
	public String getInstitucionSeleccionada() {
		return institucionSeleccionada;
	}

	/**
	 * @param institucionSeleccionada
	 *            the institucionSeleccionada to set
	 */
	public void setInstitucionSeleccionada(String institucionSeleccionada) {
		this.institucionSeleccionada = institucionSeleccionada;
	}

	/**
	 * @return the centroAtencionSeleccionado
	 */
	public String getCentroAtencionSeleccionado() {
		return centroAtencionSeleccionado;
	}

	/**
	 * @param centroAtencionSeleccionado
	 *            the centroAtencionSeleccionado to set
	 */
	public void setCentroAtencionSeleccionado(String centroAtencionSeleccionado) {
		this.centroAtencionSeleccionado = centroAtencionSeleccionado;
	}

	/**
	 * @return the consolidadoCierres
	 */
	public ArrayList<DtoConsolidadoCierreReporte> getConsolidadoCierres() {
		return consolidadoCierres;
	}

	/**
	 * @param consolidadoCierres
	 *            the consolidadoCierres to set
	 */
	public void setConsolidadoCierres(
			ArrayList<DtoConsolidadoCierreReporte> consolidadoCierres) {
		this.consolidadoCierres = consolidadoCierres;
	}

	/**
	 * @return the tipoSalida
	 */
	public String getTipoSalida() {
		return tipoSalida;
	}

	/**
	 * @param tipoSalida
	 *            the tipoSalida to set
	 */
	public void setTipoSalida(String tipoSalida) {
		this.tipoSalida = tipoSalida;
	}

	/**
	 * @return the nombreArchivoGenerado
	 */
	public String getNombreArchivoGenerado() {
		return nombreArchivoGenerado;
	}

	/**
	 * @param nombreArchivoGenerado
	 *            the nombreArchivoGenerado to set
	 */
	public void setNombreArchivoGenerado(String nombreArchivoGenerado) {
		this.nombreArchivoGenerado = nombreArchivoGenerado;
	}

	/**
	 * @return the consolidadoCierresCajaCajero
	 */
	public ArrayList<DtoConsolidadoCierreReporte> getConsolidadoCierresCajaCajero() {
		return consolidadoCierresCajaCajero;
	}

	/**
	 * @param consolidadoCierresCajaCajero
	 *            the consolidadoCierresCajaCajero to set
	 */
	public void setConsolidadoCierresCajaCajero(
			ArrayList<DtoConsolidadoCierreReporte> consolidadoCierresCajaCajero) {
		this.consolidadoCierresCajaCajero = consolidadoCierresCajaCajero;
	}

	/**
	 * @return the institucionesCajaCajero
	 */
	public ArrayList<String> getInstitucionesCajaCajero() {
		return institucionesCajaCajero;
	}

	/**
	 * @param institucionesCajaCajero
	 *            the institucionesCajaCajero to set
	 */
	public void setInstitucionesCajaCajero(
			ArrayList<String> institucionesCajaCajero) {
		this.institucionesCajaCajero = institucionesCajaCajero;
	}

	/**
	 * @return the centrosAtencionCajaCajero
	 */
	public ArrayList<String> getCentrosAtencionCajaCajero() {
		return centrosAtencionCajaCajero;
	}

	/**
	 * @param centrosAtencionCajaCajero
	 *            the centrosAtencionCajaCajero to set
	 */
	public void setCentrosAtencionCajaCajero(
			ArrayList<String> centrosAtencionCajaCajero) {
		this.centrosAtencionCajaCajero = centrosAtencionCajaCajero;
	}

	/**
	 * @see org.apache.struts.validator.ValidatorForm#validate(org.apache.struts.action.ActionMapping,
	 *      javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public ActionErrors validate(ActionMapping mapping,
			HttpServletRequest request) {

		ActionErrors errores = new ActionErrors();

		// validacion de estado invalido desde el jsp
		if (estado == null || estado.trim().isEmpty()) {
			errores.add("estado invalido", new ActionMessage(
					"errors.estadoInvalido"));
			return errores;
		}

		// validaciones en estado de generar reporte
		if (this.estado.equals(IconstantesEstadosJsp.ESTADO_GENERAR_REPORTE)) {

			// validacion de fecha requerida
			if (this.fechaInicial == null || this.fechaInicial.equals("")) {
				errores.add(
						"fecha inicial  requerida",
						new ActionMessage("errors.required", messageResource
								.getMessage("consoldiacion_fecha_error")));

				this.estado = IconstantesEstadosJsp.ESTADO_DATOS_OBLIGATORIOS;

			}

			// validacion de tipo de consolidado orequerida
			if (this.tipoConsolidadoSeleccionado == null
					|| this.tipoConsolidadoSeleccionado.equals("")
					|| this.tipoConsolidadoSeleccionado.equals(String
							.valueOf(ConstantesBD.codigoNuncaValido))) {
				errores.add(
						"tipo consolidado  requerida",
						new ActionMessage(
								"errors.required",
								messageResource
										.getMessage("consoldiacion_tipo_consolidado_error")));

				this.estado = IconstantesEstadosJsp.ESTADO_DATOS_OBLIGATORIOS;
			}

			// validacion de campo fecha requerido
			Date fechaIngresada = new Date();
			Date fechaActual = new Date();
			DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			try {
				fechaIngresada = df.parse(this.fechaInicial);
			} catch (ParseException e1) {
				logger.warn("No se generar la fecha en el formato dado"
						+ e1.toString());
			}

			if (fechaIngresada.compareTo(fechaActual) > 0) {
				errores.add(
						"fecha superior a actual ",
						new ActionMessage(
								"consultar.consolidado.cierre.error.fecha",
								messageResource
										.getMessage("consoldiacion_fecha_error")));

				this.estado = IconstantesEstadosJsp.ESTADO_DATOS_OBLIGATORIOS;
			}

		}
		return errores;
	}

	/**
	 * @return the listaInstituciones
	 */
	public ArrayList<Instituciones> getListaInstituciones() {
		return listaInstituciones;
	}

	/**
	 * @param listaInstituciones
	 *            the listaInstituciones to set
	 */
	public void setListaInstituciones(
			ArrayList<Instituciones> listaInstituciones) {
		this.listaInstituciones = listaInstituciones;
	}

	/**
	 * @return the listaCentros
	 */
	public ArrayList<CentroAtencion> getListaCentros() {
		return listaCentros;
	}

	/**
	 * @param listaCentros
	 *            the listaCentros to set
	 */
	public void setListaCentros(ArrayList<CentroAtencion> listaCentros) {
		this.listaCentros = listaCentros;
	}

	/**
	 * @return the totalesCentroAtencion
	 */
	public ArrayList<DtoConsolidadoCierreReporte> getTotalesCentroAtencion() {
		return totalesCentroAtencion;
	}

	/**
	 * @param totalesCentroAtencion
	 *            the totalesCentroAtencion to set
	 */
	public void setTotalesCentroAtencion(
			ArrayList<DtoConsolidadoCierreReporte> totalesCentroAtencion) {
		this.totalesCentroAtencion = totalesCentroAtencion;
	}

	/**
	 * @return the formasPago
	 */
	public List<FormasPago> getFormasPago() {
		return formasPago;
	}

	/**
	 * @param formasPago
	 *            the formasPago to set
	 */
	public void setFormasPago(List<FormasPago> formasPago) {
		this.formasPago = formasPago;
	}

	/**
	 * @return the cantidadColSpanCierres
	 */
	public Integer getCantidadColSpanCierres() {
		return cantidadColSpanCierres;
	}

	/**
	 * @param cantidadColSpanCierres
	 *            the cantidadColSpanCierres to set
	 */
	public void setCantidadColSpanCierres(Integer cantidadColSpanCierres) {
		this.cantidadColSpanCierres = cantidadColSpanCierres;
	}

}
