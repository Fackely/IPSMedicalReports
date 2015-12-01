package com.princetonsa.actionform.facturacion;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.util.MessageResources;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.ResultadoBoolean;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.ValoresPorDefecto;

import com.princetonsa.dto.facturacion.DTOFacturasConvenios;
import com.princetonsa.dto.facturacion.DtoReporteValoresFacturadosPorConvenio;
import com.princetonsa.dto.facturacion.DtoValoresFacturadosPorConvenio;
import com.princetonsa.dto.manejoPaciente.DtoCentrosAtencion;
import com.princetonsa.enu.impresion.EnumTiposSalida;
import com.servinte.axioma.orm.Ciudades;
import com.servinte.axioma.orm.Convenios;
import com.servinte.axioma.orm.EmpresasInstitucion;
import com.servinte.axioma.orm.Paises;

/**
 * Esta clase se encarga de obtener los datos ingresados por el usuario y
 * mapearlos a los atributos asignados a cada uno.
 * 
 * @author Luis Fernando Hincapi&eacute; Ospina
 * @since 01/12/2010
 */
public class VentasEmpresaConvenioForm extends ValidatorForm {

	private static final long serialVersionUID = 1L;

	/**
	 * Almacena la acci&oacute;n a realizar desde las p&aacute;gina.
	 */
	private String estado;

	/**
	 * Atributo que almacena el listado de los países.
	 */
	private ArrayList<Paises> listaPaises;

	/**
	 * Atributo que almacena el listado de las ciudades pertenecientes a un
	 * pa&iacute;s determinado.
	 */
	private ArrayList<Ciudades> listaCiudades;

	/**
	 * Atributo que almacena el listado de empresas instituci&oacute;n
	 * existentes en el sistema.
	 */
	private ArrayList<EmpresasInstitucion> listaEmpresaInstitucion;

	/**
	 * Atributo que almacena el listado de los centros de atenci&oacute;n.
	 */
	private ArrayList<DtoCentrosAtencion> listaCentrosAtencion;

	/**
	 * Atributo que almacena el listado de los convenios.
	 */
	private ArrayList<Convenios> listaConvenios;

	/**
	 * Atributo que indica el tipo de salida de impresi&oacute;n del reporte
	 * generado.
	 */
	private String tipoSalida;

	/**
	 * Enumeraci&oacute;n del tipo de salida.
	 */
	private EnumTiposSalida enumTipoSalida;

	/**
	 * Atributo que almacena los filtros de b&uacute;squeda seleccinados por el
	 * usuario.
	 */
	private DtoReporteValoresFacturadosPorConvenio dtoFiltros;

	/**
	 * Atributo que almacena el listado de los valores facturados.
	 */
	private ArrayList<DtoValoresFacturadosPorConvenio> listaValoresFacturados;

	/**
	 * Atributo que almacena el listado de los valores facturados de un convenio
	 * determinado.
	 */
	private ArrayList<DTOFacturasConvenios> listaValoresFacturadosConvenio;

	/**
	 * Atributo que determina si de debe mostrar el filtro de instituciones.
	 */
	private boolean mostarFiltroInstitucion;

	/**
	 * Almacena el nombre del archivo generado para luego ser visualizado
	 */
	private String nombreArchivoGenerado;

	private String centroAtencion;
	private String fechaInicial;
	private String fechaFinal;
	private String empresa;
	private String convenio;
	@SuppressWarnings("rawtypes")
	private HashMap mapaConsultaVentas;
	private ResultadoBoolean mensaje = new ResultadoBoolean(false);
	private String pathArchivoTxt;
	private boolean archivo;
	private boolean zip;
	private String nit;
			
	/**
	 * M&eacute;todo encargado de inicializar todos los valores de la forma.
	 * 
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 * @since 01/12/2010
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void reset(String centroAtencion, String institucion) {
		this.estado = "";
		this.centroAtencion = centroAtencion;
		this.fechaInicial = "";
		this.fechaFinal = "";
		this.empresa = "";
		this.convenio = "";
		this.mapaConsultaVentas = new HashMap();
		this.mapaConsultaVentas.put("numRegistros", "0");
		this.pathArchivoTxt = "";
		this.archivo = false;
		this.zip = false;
		this.nit = "";
		this.listaPaises = new ArrayList<Paises>();
		this.dtoFiltros = new DtoReporteValoresFacturadosPorConvenio();
		this.dtoFiltros.setCiudadDeptoPais("");
		this.dtoFiltros.setCodigoEmpresaInstitucion(ConstantesBD.codigoNuncaValidoLong);
		this.dtoFiltros.setConsecutivoCentroAtencion(ConstantesBD.codigoNuncaValido);
		this.dtoFiltros.setCodigoEmpresa(ConstantesBD.codigoNuncaValido);
		this.dtoFiltros.setCodigoConvenio(ConstantesBD.codigoNuncaValido);
		this.mostarFiltroInstitucion = false;
		this.listaCiudades = new ArrayList<Ciudades>();
		this.listaEmpresaInstitucion = new ArrayList<EmpresasInstitucion>();
		this.listaCentrosAtencion = new ArrayList<DtoCentrosAtencion>();
		this.listaConvenios = new ArrayList<Convenios>();
		this.tipoSalida = "";
		this.enumTipoSalida = null;
		this.nombreArchivoGenerado = "";
		
	}

	/**
	 * 
	 * M&eacutetodo encargado de realizar las validaciones de los datos
	 * ingresados por el usuario.
	 * 
	 * @param mapping
	 * @param request
	 * @return errores
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 * 
	 */
	public ActionErrors validate(ActionMapping mapping,
			HttpServletRequest request) {

		ActionErrors errores = null;
		errores = new ActionErrors();
		MessageResources mensajes = MessageResources
				.getMessageResources("com.servinte.mensajes.facturacion.VentasEmpresaConvenioForm");

		if(estado.equals("imprimirReporte")){

			boolean centinelaErrorFechasVacias = false;
			if (UtilidadTexto.isEmpty(UtilidadFecha
					.conversionFormatoFechaAAp(this.dtoFiltros.getFechaInicial()))) {
				errores.add("fecha inicial factura requerida", new ActionMessage(
						"errors.notEspecific",mensajes.getMessage("ventasEmpresaConvenio.fechaInicialFacturaRequerida")));
				centinelaErrorFechasVacias = true;
			}
			if (UtilidadTexto.isEmpty(UtilidadFecha
					.conversionFormatoFechaAAp(this.dtoFiltros.getFechaFinal()))) {
				errores.add("fecha final factura requerida", new ActionMessage(
						"errors.notEspecific",mensajes.getMessage("ventasEmpresaConvenio.fechaFinalFacturaRequerida")));
				centinelaErrorFechasVacias = true;
			}

			if (!centinelaErrorFechasVacias) {

				if (!UtilidadTexto.isEmpty(UtilidadFecha.conversionFormatoFechaAAp(this.dtoFiltros.getFechaInicial()))
						|| !UtilidadTexto.isEmpty(UtilidadFecha.conversionFormatoFechaAAp(this.dtoFiltros.getFechaFinal()))) {

					boolean centinelaErrorFechas = false;
					if (!UtilidadFecha.esFechaValidaSegunAp(UtilidadFecha.conversionFormatoFechaAAp(this.dtoFiltros.getFechaInicial()))) {
						errores.add("fecha inicial factura formato incorrecto", new ActionMessage(
								"errors.notEspecific",mensajes.getMessage("ventasEmpresaConvenio.fechaInicialFacturaFormatoIncorrecto")));
						centinelaErrorFechas = true;
					}
					if (!UtilidadFecha.esFechaValidaSegunAp(UtilidadFecha.conversionFormatoFechaAAp(this.dtoFiltros.getFechaFinal()))) {
						errores.add("fecha final factura formato incorrecto", new ActionMessage(
								"errors.notEspecific",mensajes.getMessage("ventasEmpresaConvenio.fechaFinalFacturaFormatoIncorrecto")));
						centinelaErrorFechas = true;
					}

					if (!centinelaErrorFechas) {

						if (!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(UtilidadFecha.conversionFormatoFechaAAp(this
								.dtoFiltros.getFechaInicial()), UtilidadFecha.conversionFormatoFechaAAp(this.dtoFiltros.getFechaFinal()))) {
							errores.add("fecha final factura menor que fecha inicial factura", new ActionMessage(
									"errors.notEspecific",mensajes.getMessage("ventasEmpresaConvenio.fechaFinalFacturaMenorFechaInicialFactura")));
						}

						if (!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(UtilidadFecha.conversionFormatoFechaAAp(this
								.dtoFiltros.getFechaInicial()), UtilidadFecha.getFechaActual())) {
							errores.add("fecha inicial factura mayor que fecha actual", new ActionMessage(
									"errors.notEspecific",mensajes.getMessage("ventasEmpresaConvenio.fechaInicialFacturaMayorFechaActual")));
						}
						if (!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(UtilidadFecha.conversionFormatoFechaAAp(this
								.dtoFiltros.getFechaFinal()), UtilidadFecha.getFechaActual())) {
							errores.add("fecha final factura mayor que fecha actual", new ActionMessage(
									"errors.notEspecific",mensajes.getMessage("ventasEmpresaConvenio.fechaFinalFacturaMayorFechaActual")));
						}

					}

				}

			}

			if (UtilidadTexto.isEmpty(this.dtoFiltros.getCodigoPais())
					|| String.valueOf(this.dtoFiltros.getCodigoPais()).trim().equals("-1")) {
				
				errores.add("pais requerido", new ActionMessage(
						"errors.notEspecific",mensajes.getMessage("ventasEmpresaConvenio.paisRequerido")));
			}
		}

		return errores;

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
	 * M&eacute;todo encargado de obtener el valor del atributo listaPaises.
	 * 
	 * @return listaPaises
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public ArrayList<Paises> getListaPaises() {
		return listaPaises;
	}

	/**
	 * M&eacute;todo encargado de establecer el valor del atributo listaPaises.
	 * 
	 * @param listaPaises
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public void setListaPaises(ArrayList<Paises> listaPaises) {
		this.listaPaises = listaPaises;
	}

	/**
	 * M&eacute;todo encargado de obtener el valor del atributo listaCiudades.
	 * 
	 * @return listaCiudades
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public ArrayList<Ciudades> getListaCiudades() {
		return listaCiudades;
	}

	/**
	 * M&eacute;todo encargado de establecer el valor del atributo
	 * listaCiudades.
	 * 
	 * @param listaCiudades
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public void setListaCiudades(ArrayList<Ciudades> listaCiudades) {
		this.listaCiudades = listaCiudades;
	}

	/**
	 * M&eacute;todo encargado de obtener el valor del atributo
	 * listaEmpresaInstitucion.
	 * 
	 * @return listaEmpresaInstitucion
	 * @author Luis Fernando Hincapi&eacute; Ospina
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

	/**
	 * M&eacute;todo encargado de obtener el valor del atributo
	 * listaCentrosAtencion.
	 * 
	 * @return listaCentrosAtencion
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public ArrayList<DtoCentrosAtencion> getListaCentrosAtencion() {
		return listaCentrosAtencion;
	}

	/**
	 * M&eacute;todo encargado de establecer el valor del atributo
	 * listaCentrosAtencion.
	 * 
	 * @param listaCentrosAtencion
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public void setListaCentrosAtencion(
			ArrayList<DtoCentrosAtencion> listaCentrosAtencion) {
		this.listaCentrosAtencion = listaCentrosAtencion;
	}

	/**
	 * M&eacute;todo encargado de obtener el valor del atributo listaConvenios.
	 * 
	 * @return listaConvenios
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public ArrayList<Convenios> getListaConvenios() {
		return listaConvenios;
	}

	/**
	 * M&eacute;todo encargado de establecer el valor del atributo
	 * listaConvenios.
	 * 
	 * @param listaConvenios
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public void setListaConvenios(ArrayList<Convenios> listaConvenios) {
		this.listaConvenios = listaConvenios;
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
	 * @return dtoFiltros
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public DtoReporteValoresFacturadosPorConvenio getDtoFiltros() {
		return dtoFiltros;
	}

	/**
	 * M&eacute;todo encargado de establecer el valor del atributo dtoFiltros.
	 * 
	 * @param dtoFiltros
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public void setDtoFiltros(DtoReporteValoresFacturadosPorConvenio dtoFiltros) {
		this.dtoFiltros = dtoFiltros;
	}

	/**
	 * M&eacute;todo encargado de obtener el valor del atributo
	 * listaValoresFacturados.
	 * 
	 * @return listaValoresFacturados
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public ArrayList<DtoValoresFacturadosPorConvenio> getListaValoresFacturados() {
		return listaValoresFacturados;
	}

	/**
	 * M&eacute;todo encargado de establecer el valor del atributo
	 * listaValoresFacturados.
	 * 
	 * @param listaValoresFacturados
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public void setListaValoresFacturados(
			ArrayList<DtoValoresFacturadosPorConvenio> listaValoresFacturados) {
		this.listaValoresFacturados = listaValoresFacturados;
	}

	/**
	 * M&eacute;todo encargado de obtener el valor del atributo
	 * listaValoresFacturadosConvenio.
	 * 
	 * @return listaValoresFacturadosConvenio
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public ArrayList<DTOFacturasConvenios> getListaValoresFacturadosConvenio() {
		return listaValoresFacturadosConvenio;
	}

	/**
	 * M&eacute;todo encargado de establecer el valor del atributo
	 * listaValoresFacturadosConvenio.
	 * 
	 * @param listaValoresFacturadosConvenio
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public void setListaValoresFacturadosConvenio(
			ArrayList<DTOFacturasConvenios> listaValoresFacturadosConvenio) {
		this.listaValoresFacturadosConvenio = listaValoresFacturadosConvenio;
	}

	/**
	 * M&eacute;todo encargado de obtener el valor del atributo
	 * mostarFiltroInstitucion.
	 * 
	 * @return mostarFiltroInstitucion
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public boolean isMostarFiltroInstitucion() {
		return mostarFiltroInstitucion;
	}

	/**
	 * M&eacute;todo encargado de establecer el valor del atributo
	 * mostarFiltroInstitucion.
	 * 
	 * @param mostarFiltroInstitucion
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public void setMostarFiltroInstitucion(boolean mostarFiltroInstitucion) {
		this.mostarFiltroInstitucion = mostarFiltroInstitucion;
	}

	/**
	 * M&eacute;todo encargado de obtener el valor del atributo
	 * nombreArchivoGenerado.
	 * 
	 * @return nombreArchivoGenerado
	 * @author Luis Fernando Hincapi&eacute; Ospina
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
	 * M&eacute;todo encargado de obtener el valor del atributo centroAtencion.
	 * 
	 * @return centroAtencion
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public String getCentroAtencion() {
		return centroAtencion;
	}

	/**
	 * M&eacute;todo encargado de establecer el valor del atributo
	 * centroAtencion.
	 * 
	 * @param centroAtencion
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public void setCentroAtencion(String centroAtencion) {
		this.centroAtencion = centroAtencion;
	}

	/**
	 * M&eacute;todo encargado de obtener el valor del atributo fechaInicial.
	 * 
	 * @return fechaInicial
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public String getFechaInicial() {
		return fechaInicial;
	}

	/**
	 * M&eacute;todo encargado de establecer el valor del atributo fechaInicial.
	 * 
	 * @param fechaInicial
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public void setFechaInicial(String fechaInicial) {
		this.fechaInicial = fechaInicial;
	}

	/**
	 * M&eacute;todo encargado de obtener el valor del atributo fechaFinal.
	 * 
	 * @return fechaFinal
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public String getFechaFinal() {
		return fechaFinal;
	}

	/**
	 * M&eacute;todo encargado de establecer el valor del atributo fechaFinal.
	 * 
	 * @param fechaFinal
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public void setFechaFinal(String fechaFinal) {
		this.fechaFinal = fechaFinal;
	}

	/**
	 * M&eacute;todo encargado de obtener el valor del atributo empresa.
	 * 
	 * @return empresa
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public String getEmpresa() {
		return empresa;
	}

	/**
	 * M&eacute;todo encargado de establecer el valor del atributo empresa.
	 * 
	 * @param empresa
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public void setEmpresa(String empresa) {
		this.empresa = empresa;
	}

	/**
	 * M&eacute;todo encargado de obtener el valor del atributo convenio.
	 * 
	 * @return convenio
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public String getConvenio() {
		return convenio;
	}

	/**
	 * M&eacute;todo encargado de establecer el valor del atributo convenio.
	 * 
	 * @param convenio
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public void setConvenio(String convenio) {
		this.convenio = convenio;
	}

	/**
	 * M&eacute;todo encargado de obtener el valor del atributo
	 * mapaConsultaVentas.
	 * 
	 * @return HashMap mapaConsultaVentas
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	@SuppressWarnings("rawtypes")
	public HashMap getMapaConsultaVentas() {
		return mapaConsultaVentas;
	}

	/**
	 * M&eacute;todo encargado de establecer el valor del atributo
	 * mapaConsultaVentas.
	 * 
	 * @param HashMap
	 *            mapaConsultaVentas
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	@SuppressWarnings("rawtypes")
	public void setMapaConsultaVentas(HashMap mapaConsultaVentas) {
		this.mapaConsultaVentas = mapaConsultaVentas;
	}

	/**
	 * M&eacute;todo encargado de obtener el valor del atributo
	 * mapaConsultaVentas.
	 * 
	 * @param String
	 *            key
	 * @return Object mapaConsultaVentas
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public Object getMapaConsultaVentas(String key) {
		return mapaConsultaVentas.get(key);
	}

	/**
	 * M&eacute;todo encargado de establecer el valor del atributo
	 * mapaConsultaVentas.
	 * 
	 * @param String
	 *            key
	 * @param Object
	 *            value
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	@SuppressWarnings("unchecked")
	public void setMapaConsultaVentas(String key, Object value) {
		this.mapaConsultaVentas.put(key, value);
	}

	/**
	 * M&eacute;todo encargado de obtener el valor del atributo mensaje.
	 * 
	 * @return mensaje
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public ResultadoBoolean getMensaje() {
		return mensaje;
	}

	/**
	 * M&eacute;todo encargado de establecer el valor del atributo mensaje.
	 * 
	 * @param mensaje
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public void setMensaje(ResultadoBoolean mensaje) {
		this.mensaje = mensaje;
	}

	/**
	 * M&eacute;todo encargado de obtener el valor del atributo pathArchivoTxt.
	 * 
	 * @return pathArchivoTxt
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public String getPathArchivoTxt() {
		return pathArchivoTxt;
	}

	/**
	 * M&eacute;todo encargado de establecer el valor del atributo
	 * pathArchivoTxt.
	 * 
	 * @param pathArchivoTxt
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public void setPathArchivoTxt(String pathArchivoTxt) {
		this.pathArchivoTxt = pathArchivoTxt;
	}

	/**
	 * M&eacute;todo encargado de obtener el valor del atributo archivo.
	 * 
	 * @return archivo
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public boolean isArchivo() {
		return archivo;
	}

	/**
	 * M&eacute;todo encargado de establecer el valor del atributo archivo.
	 * 
	 * @param archivo
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public void setArchivo(boolean archivo) {
		this.archivo = archivo;
	}

	/**
	 * M&eacute;todo encargado de obtener el valor del atributo zip.
	 * 
	 * @return zip
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public boolean isZip() {
		return zip;
	}

	/**
	 * M&eacute;todo encargado de establecer el valor del atributo zip.
	 * 
	 * @param zip
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public void setZip(boolean zip) {
		this.zip = zip;
	}

	/**
	 * M&eacute;todo encargado de obtener el valor del atributo nit.
	 * 
	 * @return nit
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public String getNit() {
		return nit;
	}

	/**
	 * M&eacute;todo encargado de establecer el valor del atributo nit.
	 * 
	 * @param nit
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public void setNit(String nit) {
		this.nit = nit;
	}

	/**
	 * M&eacute;todo que muestra mensaje de informaci&oacute;n cuando se genera
	 * el archivo correctamente.
	 * 
	 * @return resourceMensajeNombreArchivo
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public String getResourceMensajeNombreArchivo() {

		String[] componentesNombreArchivo = nombreArchivoGenerado.split("/");
		MessageResources mensajes = MessageResources
				.getMessageResources("com.servinte.mensajes.facturacion.VentasEmpresaConvenioForm");

		return mensajes.getMessage("ventasEmpresaConvenio.archivoGenerado",
				componentesNombreArchivo[1], ValoresPorDefecto
						.getDirectorioAxiomaBase() + componentesNombreArchivo[0] + "/");

	}

}
