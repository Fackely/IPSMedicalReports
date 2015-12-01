package com.princetonsa.actionform.tesoreria;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import util.UtilidadFecha;
import util.UtilidadTexto;

import com.ibm.icu.util.Calendar;
import com.princetonsa.dto.manejoPaciente.DtoCentrosAtencion;
import com.princetonsa.dto.tesoreria.DtoReporteAnticiposRecibidosConvenio;
import com.princetonsa.enu.impresion.EnumTiposSalida;
import com.servinte.axioma.orm.Ciudades;
import com.servinte.axioma.orm.ConceptosIngTesoreria;
import com.servinte.axioma.orm.Convenios;
import com.servinte.axioma.orm.EmpresasInstitucion;
import com.servinte.axioma.orm.EstadosRecibosCaja;
import com.servinte.axioma.orm.Paises;
import com.servinte.axioma.orm.RegionesCobertura;

/**
 * Esta clase se encarga de obtener los datos ingresados
 * por el usuario y mapearlos a los atributos asignados a
 * cada uno.
 *
 * @author Diana Carolina G&oacute;mez
 * @since  24/11/10
 *
 */
public class ReporteAnticiposRecibidosConvenioForm extends ActionForm {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Almacena la acci&oacute;n a realizar desde las p&aacute;gina
	 */
	private String estado;
	
	/**
	 * Atributo usado para completar la ruta hacia la cual se debe 
	 * direccionar la aplicaci&oacute;n.
	 */
	private String path;
	
	/**
	 * Dto para almacenar los par&aacute;metros de b&uacute;squeda 
	 * del reporte Presupuestos odontol&oacute;gicos contratados
	 */
	
	private DtoReporteAnticiposRecibidosConvenio dtoFiltros;
	
	/**
	 * Atributo que almacena el listado de los paises.
	 */
	private ArrayList<Paises> listaPaises;
	
	/**
	 * Atributo que almacena el listado de las ciudades 
	 * pertenecientes a un pa&iacute;s determinado.
	 */
	private ArrayList<Ciudades> listaCiudades;
	
	/**
	 * Atributo que almacena el listado de regiones de 
	 * cobertura que se encuentran activas en el sistema.
	 */
	private ArrayList<RegionesCobertura> listaRegiones;
	
	/**
	 * Atributo que almacena el listado de empresas
	 * institucion existentes en el sistema.
	 */
	private ArrayList<EmpresasInstitucion> listaEmpresaInstitucion;
	
	/**
	 * Atributo que almacena el listado de los centros de atenci&oacute;n.
	 */
	private ArrayList<DtoCentrosAtencion> listaCentrosAtencion;
	
	/**
	 * Atributo que indica si el campo de ciudad se encuentra deshabilitado.
	 */
	private boolean deshabilitaCiudad;
	
	/**
	 * Atributo que indica si el campo de regi&oacute;n se encuentra deshabilitado.
	 */
	private boolean deshabilitaRegion;
	
	/**
	 * Atributo que indica si la institucion-empresa es multiempresa o no.
	 */
	private String esMultiempresa;
	
	private String tipoSalida;
	
	/**
	 * enumeración del tipo de salida.
	 */
	private EnumTiposSalida enumTipoSalida;
	
	/**
	 * Almacena el nombre del archivo generado para luego ser visualizado
	 */
	private String nombreArchivoGenerado;
	
	/**
	 * Atributo que almacena el acr&oacute;nimo de la ubicaci&oacute;n del
	 * logo seg&uacute;n est&eacute; definido en el par&aacute;metro general.
	 */
	private String ubicacionLogo;
	
	/**
	 * Atributo que almacena la ruta del logo de la institución.
	 */
	private String rutaLogo;
	
	/**
	 * Atributo que determina si de debe mostrar el filtro de instituciones
	 */
	private boolean mostarFiltroInstitucion;
	
	/**
	 * Atributo que almacena el listado de los convenios 
	 * 
	 */
	private ArrayList<Convenios> listaConvenios;

	/**
	 * Atributo que almacena el listado de conceptos RC de
	 * tipo ingreso Anticipos Convenios Odontol&oacute;gicos
	 */
	private ArrayList<ConceptosIngTesoreria> listaConceptos;
	
	/**
	 * Atributo que almacena el listado de estados de
	 * los recibos de caja
	 */
	private ArrayList<EstadosRecibosCaja> listaEstadosRC;
	
	/**
	 * 
	 * 
	 * Este m&eacutetodo se encarga de realizar las validaciones de 
	 * los datos ingresados por el usuario
	 * 
	 * @param ActionMapping
	 * @param HttpServletRequest
	 * @return ActionErrors
	 * 
	 * @author  Carolina G&oacute;mez
	 * 
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {		
		ActionErrors errores=null;
		errores=new ActionErrors();
		
		if(estado.equals("imprimirReporte")){
			if((dtoFiltros.getFechaInicial()==null) || 
					((dtoFiltros.getFechaInicial().toString()).equals(""))){
				
				errores.add("La fecha Inicial es requerida", 
						new ActionMessage("errors.required", "El campo Fecha Inicial"));
				
			}else{
				String fechaInicial = UtilidadFecha.conversionFormatoFechaAAp(
						dtoFiltros.getFechaInicial());
				String fechaActual = UtilidadFecha.conversionFormatoFechaAAp(
						Calendar.getInstance().getTime());
				
				if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(
						fechaInicial, fechaActual)){
							
					 errores.add("FECHA INICIAL MAYOR QUE FECHA ACTUAL.", new ActionMessage(
									 "errors.fechaPosteriorIgualActual"," Inicial "+fechaInicial," Actual "+fechaActual));	
				}
			}
			if((dtoFiltros.getFechaFinal()==null) || 
						((dtoFiltros.getFechaFinal().toString()).equals(""))){
				
				errores.add("La fecha Fin es requerida", 
						new ActionMessage("errors.required", "El campo Fecha Fin"));
				
			}else{
				String fechaFin = UtilidadFecha.conversionFormatoFechaAAp(
						dtoFiltros.getFechaFinal());
				
				String fechaActual = UtilidadFecha.conversionFormatoFechaAAp(
						Calendar.getInstance().getTime());
				
				
				if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(fechaFin, fechaActual)){
					errores.add("La fecha Fin es mayor que fecha actual", 
							new ActionMessage("errors.fechaPosteriorIgualActual", " Fin "+fechaFin," Actual "+fechaActual));
				}
				if((dtoFiltros.getFechaInicial()!=null) && 
						(!(dtoFiltros.getFechaInicial().toString()).equals(""))){
					
					String fechaInicial = UtilidadFecha.conversionFormatoFechaAAp(
							dtoFiltros.getFechaInicial());					
					
					
					if(UtilidadFecha.esFechaMenorQueOtraReferencia(fechaFin,fechaInicial)){
						errores.add("FECHA FIN MENOR QUE FECHA INICIAL.", new ActionMessage(
										 "errors.fechaAnteriorIgualAOtraDeReferencia"," Final "+fechaFin," Inicial "+fechaInicial));
					}
				}
			}  
			
			
			if (UtilidadTexto.isEmpty(dtoFiltros.getCodigoPaisResidencia())
					|| dtoFiltros.getCodigoPaisResidencia().trim().equals("-1")) {
				
				errores.add("PAÍS RESIDENCIA ES REQUERIDO.", new ActionMessage(
						 "errors.paisResidenciaRequerido"));
			}
			
			
		} 
		
		return errores;
		
	}
		
	/**
	 * Este m&eacute;todo se encarga de inicializar todos los valores de la forma.
	 * 
	 * @author  Carolina G&oacute;mez
	 */
	
	
	public void reset(){
		
		this.path="";
		this.dtoFiltros= new DtoReporteAnticiposRecibidosConvenio();
		this.listaPaises=new ArrayList<Paises>();
		this.listaCiudades=new ArrayList<Ciudades>();
		this.listaRegiones=new ArrayList<RegionesCobertura>();
		this.listaEmpresaInstitucion=new ArrayList<EmpresasInstitucion>();
		this.listaCentrosAtencion=new ArrayList<DtoCentrosAtencion>();
		this.setDeshabilitaCiudad(false);
		this.deshabilitaRegion=false;
		this.esMultiempresa="";
		this.tipoSalida="";
		this.nombreArchivoGenerado="";
		this.enumTipoSalida=null;
		this.rutaLogo = "";
		this.listaConvenios=new ArrayList<Convenios>();
		this.listaConceptos=new ArrayList<ConceptosIngTesoreria>();
		this.listaEstadosRC=new ArrayList<EstadosRecibosCaja>();
		
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
	 *  del atributo estado
	 * 
	 * @return  Retorna la variable estado
	 */
	
	public String getEstado() {
		return estado;
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
	 *  del atributo path
	 * 
	 * @return  Retorna la variable path
	 */
	
	public String getPath() {
		return path;
	}


	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 * del atributo listaPaises
	 * @return listaPaises
	 */
	
	public ArrayList<Paises> getListaPaises() {
		return listaPaises;
	}
	
	/**
	 * M&eacute;todo que se encarga de establecer el valor
	 * del atributo listaPaises
	 * @param listaPaises
	 */

	public void setListaPaises(ArrayList<Paises> listaPaises) {
		this.listaPaises = listaPaises;
	}
	
	/**
	 * M&eacute;todo que se encarga de obtener el valor
	 * del atributo listaCiudades
	 * @return listaCiudades
	 */

	public ArrayList<Ciudades> getListaCiudades() {
		return listaCiudades;
	}
	
	/**
	 * M&eacute;todo que se encarga de establecer el valor
	 * del atributo listaCiudades
	 * @param listaCiudades
	 */
	

	public void setListaCiudades(ArrayList<Ciudades> listaCiudades) {
		this.listaCiudades = listaCiudades;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor
	 * del atributo listaRegiones
	 * @return listaRegiones
	 */
	
	public ArrayList<RegionesCobertura> getListaRegiones() {
		return listaRegiones;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor
	 * del atributo listaRegiones
	 * @param listaRegiones
	 */
	
	public void setListaRegiones(ArrayList<RegionesCobertura> listaRegiones) {
		this.listaRegiones = listaRegiones;
	}
	
	/**
	 * M&eacute;todo que se encarga de establecer el valor
	 * del atributo listaEmpresaInstitucion
	 * @param listaEmpresaInstitucion
	 */

	public void setListaEmpresaInstitucion(ArrayList<EmpresasInstitucion> listaEmpresaInstitucion) {
		this.listaEmpresaInstitucion = listaEmpresaInstitucion;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor
	 * del atributo listaEmpresaInstitucion
	 * @return listaEmpresaInstitucion
	 */
	
	public ArrayList<EmpresasInstitucion> getListaEmpresaInstitucion() {
		return listaEmpresaInstitucion;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo listaCentrosAtencion
	 * @param listaCentrosAtencion
	 */
	
	public void setListaCentrosAtencion(ArrayList<DtoCentrosAtencion> listaCentrosAtencion) {
		this.listaCentrosAtencion = listaCentrosAtencion;
	}
	
	/**
	 * M&eacute;todo que se encarga de obtener el valor
	 * del atributo listaCentrosAtencion
	 * @return 
	 */

	public ArrayList<DtoCentrosAtencion> getListaCentrosAtencion() {
		return listaCentrosAtencion;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor
	 * del atributo deshabilitaRegion
	 * @return
	 */
	
	public boolean isDeshabilitaRegion() {
		return deshabilitaRegion;
	}
	
	/**
	 * M&eacute;todo que se encarga de establecer el valor
	 * del atributo deshabilitaRegion
	 * @param deshabilitaRegion
	 */

	public void setDeshabilitaRegion(boolean deshabilitaRegion) {
		this.deshabilitaRegion = deshabilitaRegion;
	}
	
	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo esMultiempresa
	 * @param esMultiempresa
	 */

	public void setEsMultiempresa(String esMultiempresa) {
		this.esMultiempresa = esMultiempresa;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor
	 * del atributo esMultiempres
	 * @return esMultiempres
	 */
	
	public String getEsMultiempresa() {
		return esMultiempresa;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor
	 * del atributo deshabilitaCiudad
	 * @param deshabilitaCiudad
	 */
	
	public void setDeshabilitaCiudad(boolean deshabilitaCiudad) {
		this.deshabilitaCiudad = deshabilitaCiudad;
	}
	
	/**
	 * M&eacute;todo que se encarga de obtener el valor
	 * del atributo deshabilitaCiudad
	 * @return
	 */
	

	public boolean isDeshabilitaCiudad() {
		return deshabilitaCiudad;
	}


	public String getTipoSalida() {
		return tipoSalida;
	}

	public void setTipoSalida(String tipoSalida) {
		this.tipoSalida = tipoSalida;
	}

	public EnumTiposSalida getEnumTipoSalida() {
		return enumTipoSalida;
	}

	public void setEnumTipoSalida(EnumTiposSalida enumTipoSalida) {
		this.enumTipoSalida = enumTipoSalida;
	}

	public String getNombreArchivoGenerado() {
		return nombreArchivoGenerado;
	}

	public void setNombreArchivoGenerado(String nombreArchivoGenerado) {
		this.nombreArchivoGenerado = nombreArchivoGenerado;
	}

	public String getUbicacionLogo() {
		return ubicacionLogo;
	}

	public void setUbicacionLogo(String ubicacionLogo) {
		this.ubicacionLogo = ubicacionLogo;
	}

	public String getRutaLogo() {
		return rutaLogo;
	}

	public void setRutaLogo(String rutaLogo) {
		this.rutaLogo = rutaLogo;
	}

	public void setMostarFiltroInstitucion(boolean mostarFiltroInstitucion) {
		this.mostarFiltroInstitucion = mostarFiltroInstitucion;
	}

	public boolean isMostarFiltroInstitucion() {
		return mostarFiltroInstitucion;
	}

	public void setDtoFiltros(DtoReporteAnticiposRecibidosConvenio dtoFiltros) {
		this.dtoFiltros = dtoFiltros;
	}

	public DtoReporteAnticiposRecibidosConvenio getDtoFiltros() {
		return dtoFiltros;
	}

	public void setListaConvenios(ArrayList<Convenios> listaConvenios) {
		this.listaConvenios = listaConvenios;
	}

	public ArrayList<Convenios> getListaConvenios() {
		return listaConvenios;
	}

	public void setListaConceptos(ArrayList<ConceptosIngTesoreria> listaConceptos) {
		this.listaConceptos = listaConceptos;
	}

	public ArrayList<ConceptosIngTesoreria> getListaConceptos() {
		return listaConceptos;
	}

	public void setListaEstadosRC(ArrayList<EstadosRecibosCaja> listaEstadosRC) {
		this.listaEstadosRC = listaEstadosRC;
	}

	public ArrayList<EstadosRecibosCaja> getListaEstadosRC() {
		return listaEstadosRC;
	}
}
