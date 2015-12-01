package com.princetonsa.actionform.facturacion;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.util.MessageResources;

import util.ConstantesBD;
import util.UtilidadTexto;

import com.princetonsa.dto.facturacion.DTOResultadoBusquedaDetalleMontos;
import com.princetonsa.dto.facturacion.DTOTarifasServicios;
import com.princetonsa.dto.facturacion.DtoReporteTarifasPorEsquemaTarifario;
import com.princetonsa.dto.facturacion.DtoTarifasPorEsquemaTarifario;
import com.princetonsa.enu.impresion.EnumTiposSalida;
import com.servinte.axioma.orm.EmpresasInstitucion;
import com.servinte.axioma.orm.Especialidades;
import com.servinte.axioma.orm.EsquemasTarifarios;
import com.servinte.axioma.orm.Programas;
import com.servinte.axioma.orm.Servicios;

/**
 * Esta clase se encarga de obtener los datos ingresados por el usuario y
 * mapearlos a los atributos asignados a cada uno.
 * 
 * @author Luis Fernando Hincapi&eacute; Ospina
 * @since 10/11/2010
 */
public class ReporteTarifasPorEsquemaTarifarioForm extends ActionForm {

	private static final long serialVersionUID = 1L;

	/**
	 * Almacena la acci&oacute;n a realizar desde las p&aacute;gina.
	 */
	private String estado;

	/**
	 * Atributo que almacena el listado de los esquemas tarifarios.
	 */
	private ArrayList<EsquemasTarifarios> listaEsquemasTarifarios;

	/**
	 * Atributo que almacena el listado de las especialidades existentes en el
	 * sistema.
	 */
	private ArrayList<Especialidades> listaEspecialidades;

	/**
	 * Atributo que almacena el listado de los programas odontol&oacute;gicos
	 * existentes en el sistema.
	 */
	private ArrayList<Programas> listaProgramas;

	/**
	 * Atributo que almacena el listado de los servicios existentes en el
	 * sistema.
	 */
	private ArrayList<Servicios> listaServicios;

	/**
	 * Atributo que determina si de debe mostrar el filtro de programas
	 */
	private boolean mostarFiltroPrograma;

	/**
	 * Atributo que indica el tipo de salida de impresi&oacute;n del reporte
	 * generado.
	 */
	private String tipoSalida;

	/**
	 * Enumeración del tipo de salida.
	 */
	private EnumTiposSalida enumTipoSalida;

	/**
	 * Atributo que almacena el c&oacute;digo del servicio.
	 */
	private int codigoServicio;

	/**
	 * Atributo que almacena el nombre del servicio.
	 */
	private String nombreServicio;

	/**
	 * Atributo que almacena el c&oacute;digo del programa de servicios.
	 */
	private String listaCodigoProgramaServicios;

	/**
	 * Atributo que almacena los filtros de b&uacute;squeda seleccinados por el
	 * usuario.
	 */
	private DtoReporteTarifasPorEsquemaTarifario dtoFiltros;

	private DTOResultadoBusquedaDetalleMontos detalleSeleccionado;

	/**
	 * Atributo que almacena el listado de las tarifas.
	 */
	private ArrayList<DtoTarifasPorEsquemaTarifario> listaTarifas;

	/**
	 * Atributo que almacena el listado de las tarifas de un esquema tarifario
	 * determinado.
	 */
	private ArrayList<DTOTarifasServicios> listaTarifasEsquemaTarifario;

	/**
	 * Almacena el nombre del archivo generado para luego ser visualizado
	 */
	private String nombreArchivoGenerado;

	private int registrosNuevos;

	/**
	 * Atributo que almacena el listado de empresas
	 * institución existentes en el sistema.
	 */
	private ArrayList<EmpresasInstitucion> listaEmpresaInstitucion;

	/**
	 * 
	 * 
	 * M&eacutetodo encargado de realizar las validaciones de 
	 * los datos ingresados por el usuario.
	 * 
	 * @param mapping
	 * @param request
	 * @return errores
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 * 
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {

		ActionErrors errores = null;
		errores = new ActionErrors();
		MessageResources mensajes = MessageResources
				.getMessageResources("com.servinte.mensajes.facturacion.ReporteTarifasPorEsquemaTarifarioForm");

		if(estado.equals("imprimirReporte")){
			if (UtilidadTexto.isEmpty(dtoFiltros.getCodigoEsquemaTarifario())
					|| String.valueOf(dtoFiltros.getCodigoEsquemaTarifario()).trim().equals("-1")) {
				
				errores.add("esquema tarifario requerido", new ActionMessage(
						"errors.notEspecific",mensajes.getMessage("reporteTarifasPorEsquemaTarifario.esquemaTarifarioRequerido")));
			}
		}

		return errores;

	}

	/**
	 * M&eacute;todo encargado de inicializar todos los valores de la forma.
	 * 
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 * @since 10/11/2010
	 */
	public void reset() {
		this.estado = "";
		this.listaEsquemasTarifarios = new ArrayList<EsquemasTarifarios>();
		this.listaEspecialidades = new ArrayList<Especialidades>();
		this.listaProgramas = new ArrayList<Programas>();
		this.listaServicios = new ArrayList<Servicios>();
		this.mostarFiltroPrograma = false;
		this.tipoSalida = "";
		this.enumTipoSalida = null;
		this.dtoFiltros = new DtoReporteTarifasPorEsquemaTarifario();
		this.dtoFiltros.setCodigoEsquemaTarifario(ConstantesBD.codigoNuncaValido);
		this.dtoFiltros.setCodigoEspecialidad(ConstantesBD.codigoNuncaValido);
		this.dtoFiltros.setCodigoPrograma(ConstantesBD.codigoNuncaValido);
		this.dtoFiltros.setCodigoServicio(String.valueOf(ConstantesBD.codigoNuncaValido));
		this.detalleSeleccionado = new DTOResultadoBusquedaDetalleMontos();
		this.listaCodigoProgramaServicios = "";
		this.nombreArchivoGenerado = "";
		this.registrosNuevos=ConstantesBD.codigoNuncaValido;
		this.dtoFiltros.setProgramas(new Programas());
		this.dtoFiltros.getProgramas().setCodigo(ConstantesBD.codigoNuncaValidoLong);
		this.listaEmpresaInstitucion = new ArrayList<EmpresasInstitucion>();
	}

	/**
	 * M&eacute;todo encargado de obtener el valor del atributo estado.
	 * 
	 * @return estado
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public String getEstado() {
		return estado;
	}

	/**
	 * M&eacute;todo encargado de establecer el valor del atributo estado.
	 * 
	 * @param estado
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}

	/**
	 * M&eacute;todo encargado de obtener el valor del atributo
	 * listaEsquemasTarifarios.
	 * 
	 * @return listaEsquemasTarifarios
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public ArrayList<EsquemasTarifarios> getListaEsquemasTarifarios() {
		return listaEsquemasTarifarios;
	}

	/**
	 * M&eacute;todo encargado de establecer el valor del atributo
	 * listaEsquemasTarifarios.
	 * 
	 * @param listaEsquemasTarifarios
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public void setListaEsquemasTarifarios(
			ArrayList<EsquemasTarifarios> listaEsquemasTarifarios) {
		this.listaEsquemasTarifarios = listaEsquemasTarifarios;
	}

	/**
	 * M&eacute;todo encargado de obtener el valor del atributo
	 * listaEspecialidades.
	 * 
	 * @return listaEspecialidades
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public ArrayList<Especialidades> getListaEspecialidades() {
		return listaEspecialidades;
	}

	/**
	 * M&eacute;todo encargado de establecer el valor del atributo
	 * listaEspecialidades.
	 * 
	 * @param listaEspecialidades
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public void setListaEspecialidades(
			ArrayList<Especialidades> listaEspecialidades) {
		this.listaEspecialidades = listaEspecialidades;
	}

	/**
	 * M&eacute;todo encargado de obtener el valor del atributo listaProgramas.
	 * 
	 * @return listaProgramas
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public ArrayList<Programas> getListaProgramas() {
		return listaProgramas;
	}

	/**
	 * M&eacute;todo encargado de establecer el valor del atributo
	 * listaProgramas.
	 * 
	 * @param listaProgramas
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public void setListaProgramas(ArrayList<Programas> listaProgramas) {
		this.listaProgramas = listaProgramas;
	}

	/**
	 * M&eacute;todo encargado de obtener el valor del atributo listaServicios.
	 * 
	 * @return listaServicios
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public ArrayList<Servicios> getListaServicios() {
		return listaServicios;
	}

	/**
	 * M&eacute;todo encargado de establecer el valor del atributo
	 * listaServicios.
	 * 
	 * @param listaServicios
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public void setListaServicios(ArrayList<Servicios> listaServicios) {
		this.listaServicios = listaServicios;
	}

	/**
	 * M&eacute;todo encargado de obtener el valor del atributo
	 * mostarFiltroPrograma.
	 * 
	 * @return mostarFiltroPrograma
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public boolean isMostarFiltroPrograma() {
		return mostarFiltroPrograma;
	}

	/**
	 * M&eacute;todo encargado de establecer el valor del atributo
	 * mostarFiltroPrograma.
	 * 
	 * @param mostarFiltroPrograma
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public void setMostarFiltroPrograma(boolean mostarFiltroPrograma) {
		this.mostarFiltroPrograma = mostarFiltroPrograma;
	}

	/**
	 * M&eacute;todo encargado de obtener el valor del atributo tipoSalida.
	 * 
	 * @return tipoSalida
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public String getTipoSalida() {
		return tipoSalida;
	}

	/**
	 * M&eacute;todo encargado de establecer el valor del atributo tipoSalida.
	 * 
	 * @param tipoSalida
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public void setTipoSalida(String tipoSalida) {
		this.tipoSalida = tipoSalida;
	}

	/**
	 * M&eacute;todo encargado de obtener el valor del atributo enumTipoSalida.
	 * 
	 * @return enumTipoSalida
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public EnumTiposSalida getEnumTipoSalida() {
		return enumTipoSalida;
	}

	/**
	 * M&eacute;todo encargado de establecer el valor del atributo
	 * enumTipoSalida.
	 * 
	 * @param enumTipoSalida
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public void setEnumTipoSalida(EnumTiposSalida enumTipoSalida) {
		this.enumTipoSalida = enumTipoSalida;
	}

	/**
	 * M&eacute;todo encargado de obtener el valor del atributo dtoFiltros.
	 * 
	 * @return listaServicios
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public DtoReporteTarifasPorEsquemaTarifario getDtoFiltros() {
		return this.dtoFiltros;
	}

	/**
	 * M&eacute;todo encargado de establecer el valor del atributo dtoFiltros.
	 * 
	 * @param dtoFiltros
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public void setDtoFiltros(DtoReporteTarifasPorEsquemaTarifario dtoFiltros) {
		this.dtoFiltros = dtoFiltros;
	}

	/**
	 * M&eacute;todo encargado de obtener el valor del atributo
	 * detalleSeleccionado.
	 * 
	 * @return detalleSeleccionado
	 * @author Luis Fernando Hincapi&eacute; Osina
	 */
	public DTOResultadoBusquedaDetalleMontos getDetalleSeleccionado() {
		return detalleSeleccionado;
	}

	/**
	 * M&eacute;todo encargado de establecer el valor del atributo
	 * detalleSeleccionado.
	 * 
	 * @param detalleSeleccionado
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public void setDetalleSeleccionado(
			DTOResultadoBusquedaDetalleMontos detalleSeleccionado) {
		this.detalleSeleccionado = detalleSeleccionado;
	}

	/**
	 * M&eacute;todo encargado de obtener el valor del atributo codigoServicio.
	 * 
	 * @return codigoServicio
	 * @author Luis Fernando Hincapi&eacute; Osina
	 */
	public int getCodigoServicio() {
		return codigoServicio;
	}

	/**
	 * M&eacute;todo encargado de establecer el valor del atributo
	 * codigoServicio.
	 * 
	 * @param codigoServicio
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public void setCodigoServicio(int codigoServicio) {
		this.codigoServicio = codigoServicio;
	}

	/**
	 * M&eacute;todo encargado de obtener el valor del atributo nombreServicio.
	 * 
	 * @return nombreServicio
	 * @author Luis Fernando Hincapi&eacute; Osina
	 */
	public String getNombreServicio() {
		return nombreServicio;
	}

	/**
	 * M&eacute;todo encargado de establecer el valor del atributo
	 * nombreServicio.
	 * 
	 * @param nombreServicio
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public void setNombreServicio(String nombreServicio) {
		this.nombreServicio = nombreServicio;
	}

	/**
	 * M&eacute;todo encargado de obtener el valor del atributo
	 * listaCodigoProgramaServicios.
	 * 
	 * @return listaCodigoProgramaServicios
	 * @author Luis Fernando Hincapi&eacute; Osina
	 */
	public String getListaCodigoProgramaServicios() {
		return listaCodigoProgramaServicios;
	}

	/**
	 * M&eacute;todo encargado de establecer el valor del atributo
	 * listaCodigoProgramaServicios.
	 * 
	 * @param listaCodigoProgramaServicios
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public void setListaCodigoProgramaServicios(
			String listaCodigoProgramaServicios) {
		this.listaCodigoProgramaServicios = listaCodigoProgramaServicios;
	}

	/**
	 * M&eacute;todo encargado de obtener el valor del atributo
	 * listaTarifas.
	 * 
	 * @return listaTarifas
	 * @author Luis Fernando Hincapi&eacute; Osina
	 */
	public ArrayList<DtoTarifasPorEsquemaTarifario> getListaTarifas() {
		return listaTarifas;
	}

	/**
	 * M&eacute;todo encargado de establecer el valor del atributo
	 * listaTarifas.
	 * 
	 * @param listaTarifas
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public void setListaTarifas(ArrayList<DtoTarifasPorEsquemaTarifario> listaTarifas) {
		this.listaTarifas = listaTarifas;
	}

	/**
	 * M&eacute;todo encargado de obtener el valor del atributo
	 * listaTarifasEsquemaTarifario.
	 * 
	 * @return listaTarifasEsquemaTarifario
	 * @author Luis Fernando Hincapi&eacute; Osina
	 */
	public ArrayList<DTOTarifasServicios> getListaTarifasEsquemaTarifario() {
		return listaTarifasEsquemaTarifario;
	}

	/**
	 * M&eacute;todo encargado de establecer el valor del atributo
	 * listaTarifasEsquemaTarifario.
	 * 
	 * @param listaTarifasEsquemaTarifario
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public void setListaTarifasEsquemaTarifario(
			ArrayList<DTOTarifasServicios> listaTarifasEsquemaTarifario) {
		this.listaTarifasEsquemaTarifario = listaTarifasEsquemaTarifario;
	}

	/**
	 * M&eacute;todo encargado de obtener el valor del atributo
	 * nombreArchivoGenerado.
	 * 
	 * @return nombreArchivoGenerado
	 * @author Luis Fernando Hincapi&eacute; Osina
	 */
	public String getNombreArchivoGenerado() {
		return nombreArchivoGenerado;
	}

	/**
	 * M&eacute;todo encargado de establecer el valor del atributo
	 * nombreArchivoGenerado.
	 * 
	 * @param nombreArchivoGenerado
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public void setNombreArchivoGenerado(String nombreArchivoGenerado) {
		this.nombreArchivoGenerado = nombreArchivoGenerado;
	}

	/**
	 * M&eacute;todo encargado de obtener el valor del atributo registrosNuevos.
	 * 
	 * @return registrosNuevos
	 * @author Luis Fernando Hincapi&eacute; Osina
	 */
	public int getRegistrosNuevos() {
		return registrosNuevos;
	}

	/**
	 * M&eacute;todo encargado de establecer el valor del atributo
	 * registrosNuevos.
	 * 
	 * @param registrosNuevos
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public void setRegistrosNuevos(int registrosNuevos) {
		this.registrosNuevos = registrosNuevos;
	}

	/**
	 * M&eacute;todo encargado de obtener el valor del atributo
	 * listaEmpresaInstitucion.
	 * 
	 * @return listaEmpresaInstitucion
	 * @author Luis Fernando Hincapi&eacute; Osina
	 */
	public ArrayList<EmpresasInstitucion> getListaEmpresaInstitucion() {
		return listaEmpresaInstitucion;
	}

	/**
	 * M&eacute;todo encargado de establecer el valor del atributo
	 * listaEmpresaInstitucion.
	 * 
	 * @param listaEmpresaInstitucion
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public void setListaEmpresaInstitucion(
			ArrayList<EmpresasInstitucion> listaEmpresaInstitucion) {
		this.listaEmpresaInstitucion = listaEmpresaInstitucion;
	}

}
