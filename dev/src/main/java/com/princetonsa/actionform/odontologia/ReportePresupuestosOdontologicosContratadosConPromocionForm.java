package com.princetonsa.actionform.odontologia;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import util.ConstantesBD;
import util.UtilidadFecha;
import util.UtilidadTexto;

import com.princetonsa.dto.administracion.DtoIntegridadDominio;
import com.princetonsa.dto.administracion.DtoPersonas;
import com.princetonsa.dto.manejoPaciente.DtoCentrosAtencion;
import com.princetonsa.dto.odontologia.DtoReportePresupuestosOdontologicosContratadosConPromocion;
import com.princetonsa.enu.impresion.EnumTiposSalida;
import com.servinte.axioma.orm.Ciudades;
import com.servinte.axioma.orm.EmpresasInstitucion;
import com.servinte.axioma.orm.Paises;
import com.servinte.axioma.orm.PromocionesOdontologicas;
import com.servinte.axioma.orm.Programas;
import com.servinte.axioma.orm.RecomSerproSerpro;
import com.servinte.axioma.orm.RecomendacionesServProg;
import com.servinte.axioma.orm.RegionesCobertura;

/**
 * Esta clase se encarga de obtener los datos ingresados
 * por el usuario y mapearlos a los atributos asignados a
 * cada uno.
 *
 *@author  Javier Gonz&aacute;lez
 * @since  03/11/2010
 *
 */

public class ReportePresupuestosOdontologicosContratadosConPromocionForm extends ActionForm{

	
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
	
	/**
	 * Atributo que almacena un listado con los indicativos de contrato
	 * del presupuesto en estado contratado.
	 */
	private ArrayList<DtoIntegridadDominio> listadoIndicativoContrato;
	
	
	/**
	 * Atributo que almacena el listado de profesionales de la salud
	 * con ocupaciones: Auxiliar y Odontologo
	 */
	private ArrayList<DtoPersonas> listaProfesionales;
	
	/**
	 * Atributo que almacena el listado de profesionales de la salud
	 * con ocupación: Odontologo
	 */
	
	private ArrayList<DtoPersonas> listaProfesionalesOdont;
	
	/**
	 * Atributo que almacena el listado de los promocionesOdontologicas 
	 * odontol&oacute;gicos creados en el sistema.
	 */
	private ArrayList<PromocionesOdontologicas> listadoPromocionesOdon;
	
	/**
	 * Atributo que indica si se debe deshabilitar o no 
	 * el campo de promociones odontol&oacute;gicas.
	 */
	private boolean deshabilitaPromocion;
	
	/**
	 * Atributo que indica si se debe deshabilitar o no 
	 * el campo de programas odontol&oacute;gicos.
	 */
	private String deshabilitaPrograma;
	
	/**
	 * Atributo que indica si la instituci&oacute;n utiliza programas odontol&oacute;gicos.
	 */
	private String utilizaProgramasOdonto; 
	
	/**
	 * DTO RECOMENDACIONES PROG SER PROG SER
	 */
	private RecomSerproSerpro dtoSerProSerPro = new RecomSerproSerpro();
			
	/**
	 * Atributo que indica el tipo de salida de impresi&oacute;n 
	 * del reporte generado.
	 */
	private String tipoSalida;
	/**
	 * Atributo que almacena la raz&oacute;n social
	 * de la instituci&oacute;n.
	 */
	private String razonSocial;
	
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
	 * Dto para almacenar los par&aacute;metros de b&uacute;squeda 
	 * del reporte Presupuestos odontol&oacute;gicos contratados con Promocion
	 */
	
	private DtoReportePresupuestosOdontologicosContratadosConPromocion dtoFiltrosPresupuestosContratadosConPromocion;
	
	/**
	 * Lista Códigos Programas Servicios
	 */
	private String listaCodigoProgramaServicios;
	
	/**
	 * Atributo que almacena la fecha inicial de los criterios de
	 * b&uacute;squeda con el formato de impresi&oacute;n correcto.
	 */
	private String fechaInicialFormateado;
	
	/**
	 * Atributo que almacena el c&oacute;digo del
	 * Programa odontol&oacute;gico.
	 */
	private long codigoPrograma;
	
	/**
	 * Atributo que almacena la fecha final de los criterios de
	 * b&uacute;squeda con el formato de impresi&oacute;n correcto.
	 */
	private String fechaFinalFormateado;
	/**
	 * Atributo que determina si de debe mostrar el filtro de programas
	 */
	private boolean mostarFiltroPrograma;
	/**
	 * LISTA CODIGOS PROGRAMAS SERVICIOS
	 */
	

	private RecomendacionesServProg dtoRecomenServicioPrograma;
	
	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
	public ArrayList<Paises> getListaPaises() {
		return listaPaises;
	}

	public void setListaPaises(ArrayList<Paises> listaPaises) {
		this.listaPaises = listaPaises;
	}

	public ArrayList<Ciudades> getListaCiudades() {
		return listaCiudades;
	}

	public void setListaCiudades(ArrayList<Ciudades> listaCiudades) {
		this.listaCiudades = listaCiudades;
	}

	public ArrayList<RegionesCobertura> getListaRegiones() {
		return listaRegiones;
	}

	public void setListaRegiones(ArrayList<RegionesCobertura> listaRegiones) {
		this.listaRegiones = listaRegiones;
	}

	public ArrayList<EmpresasInstitucion> getListaEmpresaInstitucion() {
		return listaEmpresaInstitucion;
	}

	public void setListaEmpresaInstitucion(
			ArrayList<EmpresasInstitucion> listaEmpresaInstitucion) {
		this.listaEmpresaInstitucion = listaEmpresaInstitucion;
	}

	public ArrayList<DtoCentrosAtencion> getListaCentrosAtencion() {
		return listaCentrosAtencion;
	}

	public void setListaCentrosAtencion(
			ArrayList<DtoCentrosAtencion> listaCentrosAtencion) {
		this.listaCentrosAtencion = listaCentrosAtencion;
	}

	public boolean isDeshabilitaCiudad() {
		return deshabilitaCiudad;
	}

	public void setDeshabilitaCiudad(boolean deshabilitaCiudad) {
		this.deshabilitaCiudad = deshabilitaCiudad;
	}

	public boolean isDeshabilitaRegion() {
		return deshabilitaRegion;
	}

	public void setDeshabilitaRegion(boolean deshabilitaRegion) {
		this.deshabilitaRegion = deshabilitaRegion;
	}

	public String getEsMultiempresa() {
		return esMultiempresa;
	}

	public void setEsMultiempresa(String esMultiempresa) {
		this.esMultiempresa = esMultiempresa;
	}

	public ArrayList<DtoIntegridadDominio> getListadoIndicativoContrato() {
		return listadoIndicativoContrato;
	}

	public void setListadoIndicativoContrato(
			ArrayList<DtoIntegridadDominio> listadoIndicativoContrato) {
		this.listadoIndicativoContrato = listadoIndicativoContrato;
	}

	public ArrayList<DtoPersonas> getListaProfesionales() {
		return listaProfesionales;
	}

	public void setListaProfesionales(ArrayList<DtoPersonas> listaProfesionales) {
		this.listaProfesionales = listaProfesionales;
	}

	public ArrayList<DtoPersonas> getListaProfesionalesOdont() {
		return listaProfesionalesOdont;
	}

	public void setListaProfesionalesOdont(
			ArrayList<DtoPersonas> listaProfesionalesOdont) {
		this.listaProfesionalesOdont = listaProfesionalesOdont;
	}

	public void setListadoPromocionesOdon(ArrayList<PromocionesOdontologicas> listadoPromocionesOdon) {
		this.listadoPromocionesOdon = listadoPromocionesOdon;
	}

	public ArrayList<PromocionesOdontologicas> getListadoPromocionesOdon() {
		return listadoPromocionesOdon;
	}
	
	public String getDeshabilitaPrograma() {
		return deshabilitaPrograma;
	}

	public void setDeshabilitaPrograma(String deshabilitaPrograma) {
		this.deshabilitaPrograma = deshabilitaPrograma;
	}

	public String getUtilizaProgramasOdonto() {
		return utilizaProgramasOdonto;
	}

	public void setUtilizaProgramasOdonto(String utilizaProgramasOdonto) {
		this.utilizaProgramasOdonto = utilizaProgramasOdonto;
	}

	public RecomSerproSerpro getDtoSerProSerPro() {
		
		return dtoSerProSerPro;
	}

	public void setDtoSerProSerPro(RecomSerproSerpro dtoSerProSerPro) {
		this.dtoSerProSerPro = dtoSerProSerPro;
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

	public boolean isMostarFiltroInstitucion() {
		return mostarFiltroInstitucion;
	}

	public void setMostarFiltroInstitucion(boolean mostarFiltroInstitucion) {
		this.mostarFiltroInstitucion = mostarFiltroInstitucion;
	}

	public void setDtoFiltrosPresupuestosContratadosConPromocion(
			DtoReportePresupuestosOdontologicosContratadosConPromocion dtoFiltrosPresupuestosContratadosConPromocion) {
		this.dtoFiltrosPresupuestosContratadosConPromocion = dtoFiltrosPresupuestosContratadosConPromocion;
	}

	public DtoReportePresupuestosOdontologicosContratadosConPromocion getDtoFiltrosPresupuestosContratadosConPromocion() {
		return dtoFiltrosPresupuestosContratadosConPromocion;
	}
	
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
	 * @author  Javier Gonz&aacute;lez
	 * 
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {		
		ActionErrors errores=null;
		errores=new ActionErrors();
		
		
		
		
		if(estado.equals("imprimirReporte")){
			
			if(dtoFiltrosPresupuestosContratadosConPromocion.getFechaInicial() == null && dtoFiltrosPresupuestosContratadosConPromocion.getFechaFinal() == null){
			
				if (dtoFiltrosPresupuestosContratadosConPromocion.getCodigoPromocionOdontologica() <= 0 && dtoFiltrosPresupuestosContratadosConPromocion.getCodigoPrograma() <= 0){
					errores.add("Rangos de Fechas o la Promocion o el Programa Requeridos.", new ActionMessage(
					 "errores.fechasOPromocionOProgramaRequeridos"));
					this.tipoSalida=null;
				}
				
			}else{
				
				if((dtoFiltrosPresupuestosContratadosConPromocion.getFechaInicial()==null) || 
						((dtoFiltrosPresupuestosContratadosConPromocion.getFechaInicial().toString()).equals(""))){
							
					errores.add("La fecha Inicial es requerida", 
							new ActionMessage("errors.required", "El campo Fecha Inicial"));
					this.tipoSalida=null;
					
				}else{
					
					
					String fechaInicial = UtilidadFecha.conversionFormatoFechaAAp(
							dtoFiltrosPresupuestosContratadosConPromocion.getFechaInicial());
					String fechaActual = UtilidadFecha.getFechaActual();
					
					if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(
							fechaInicial, fechaActual)){
								
						 errores.add("FECHA INICIAL MAYOR QUE FECHA ACTUAL.", new ActionMessage(
										 "errors.fechaPosteriorIgualActual"," Inicial "+fechaInicial," Actual "+fechaActual));
						 this.tipoSalida=null;
					}
				}
				
				if((dtoFiltrosPresupuestosContratadosConPromocion.getFechaFinal()==null) || 
							((dtoFiltrosPresupuestosContratadosConPromocion.getFechaFinal().toString()).equals(""))){
					
					errores.add("La fecha Fin es requerida", 
							new ActionMessage("errors.required", "El campo Fecha Fin"));
					this.tipoSalida=null;
				}else{
					String fechaFin = UtilidadFecha.conversionFormatoFechaAAp(
							dtoFiltrosPresupuestosContratadosConPromocion.getFechaFinal());
					
					String fechaActual = UtilidadFecha.getFechaActual();
					
					if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(fechaFin, fechaActual)){
						errores.add("La fecha Fin es mayor que fecha actual", 
								new ActionMessage("errors.fechaPosteriorIgualActual", " Fin "+fechaFin," Actual "+fechaActual));
						this.tipoSalida=null;
					}
					if((dtoFiltrosPresupuestosContratadosConPromocion.getFechaInicial()!=null) && 
							(!(dtoFiltrosPresupuestosContratadosConPromocion.getFechaInicial().toString()).equals(""))){
						
						String fechaInicial = UtilidadFecha.conversionFormatoFechaAAp(
								dtoFiltrosPresupuestosContratadosConPromocion.getFechaInicial());					
						
						
						if(UtilidadFecha.esFechaMenorQueOtraReferencia(fechaFin,fechaInicial)){
							errores.add("FECHA FIN MENOR QUE FECHA INICIAL.", new ActionMessage(
											 "errors.fechaAnteriorIgualAOtraDeReferencia"," Final "+fechaFin," Inicial "+fechaInicial));
							this.tipoSalida=null;
						}
					}
				}	
			}
		  			  
			
			
			
			if (UtilidadTexto.isEmpty(dtoFiltrosPresupuestosContratadosConPromocion.getCodigoPaisResidencia())
					|| dtoFiltrosPresupuestosContratadosConPromocion.getCodigoPaisResidencia().trim().equals("-1")) {
				
				errores.add("PAÍS RESIDENCIA ES REQUERIDO.", new ActionMessage(
						 "errors.paisResidenciaRequerido"));
				this.tipoSalida=null;
			}
			
			
		}
		
		return errores;
		
	}
		
	/**
	 * Este m&eacute;todo se encarga de inicializar todos los valores de la forma.
	 * 
	 * @author  Javier Gonz&aacute;lez
	 */
	
	
	public void reset(){
		
		this.path="";
		this.dtoFiltrosPresupuestosContratadosConPromocion= new DtoReportePresupuestosOdontologicosContratadosConPromocion();
		this.listaPaises=new ArrayList<Paises>();
		this.listaCiudades=new ArrayList<Ciudades>();
		this.listaRegiones=new ArrayList<RegionesCobertura>();
		this.listaEmpresaInstitucion=new ArrayList<EmpresasInstitucion>();
		this.listaCentrosAtencion=new ArrayList<DtoCentrosAtencion>();
		this.setDeshabilitaCiudad(false);
		this.deshabilitaRegion=false;
		this.esMultiempresa="";
		this.setListaCodigoProgramaServicios("");
		this.listadoIndicativoContrato=new ArrayList<DtoIntegridadDominio>();
		this.listaProfesionalesOdont=new ArrayList<DtoPersonas>();
		//this.setListadoPromocionesOdon(new ArrayList<PromocionesOdontologicas>());
		this.listadoPromocionesOdon=new ArrayList<PromocionesOdontologicas>();
		this.setDeshabilitaPromocion(false);
		this.deshabilitaPrograma="false";
		this.utilizaProgramasOdonto="";
		this.setDtoSerProSerPro(new RecomSerproSerpro());
		this.dtoSerProSerPro = new RecomSerproSerpro();
		this.setDtoRecomenServicioPrograma(new RecomendacionesServProg());
		this.dtoSerProSerPro.setProgramas(new Programas());
		this.dtoSerProSerPro.setCodigoPk(0);
		this.tipoSalida="";
		this.nombreArchivoGenerado="";
		this.rutaLogo = "";
		this.mostarFiltroPrograma=false;
		this.enumTipoSalida = null;
		
	}

	public void setFechaInicialFormateado(String fechaInicialFormateado) {
		this.fechaInicialFormateado = fechaInicialFormateado;
	}

	public String getFechaInicialFormateado() {
		return fechaInicialFormateado;
	}

	public void setFechaFinalFormateado(String fechaFinalFormateado) {
		this.fechaFinalFormateado = fechaFinalFormateado;
	}

	public String getFechaFinalFormateado() {
		return fechaFinalFormateado;
	}

	public void setDtoRecomenServicioPrograma(RecomendacionesServProg dtoRecomenServicioPrograma) {
		this.dtoRecomenServicioPrograma = dtoRecomenServicioPrograma;
	}

	public RecomendacionesServProg getDtoRecomenServicioPrograma() {
		return dtoRecomenServicioPrograma;
	}

	public void setListaCodigoProgramaServicios(
			String listaCodigoProgramaServicios) {
		this.listaCodigoProgramaServicios = listaCodigoProgramaServicios;
	}

	public String getListaCodigoProgramaServicios() {
		return listaCodigoProgramaServicios;
	}

	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}

	public String getRazonSocial() {
		return razonSocial;
	}

	public void setDeshabilitaPromocion(boolean deshabilitaPromocion) {
		this.deshabilitaPromocion = deshabilitaPromocion;
	}

	public boolean isDeshabilitaPromocion() {
		return deshabilitaPromocion;
	}

	public void setCodigoPrograma(long codigoPrograma) {
		this.codigoPrograma = codigoPrograma;
	}

	public long getCodigoPrograma() {
		return codigoPrograma;
	}

	public void setMostarFiltroPrograma(boolean mostarFiltroPrograma) {
		this.mostarFiltroPrograma = mostarFiltroPrograma;
	}

	public boolean isMostarFiltroPrograma() {
		return mostarFiltroPrograma;
	}

	



}
