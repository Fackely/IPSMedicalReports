package com.princetonsa.actionform.odontologia;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;

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
import com.princetonsa.dto.odontologia.DtoReportePresupuestosOdontologicosContratados;
import com.princetonsa.enu.impresion.EnumTiposSalida;
import com.servinte.axioma.orm.Ciudades;
import com.servinte.axioma.orm.EmpresasInstitucion;
import com.servinte.axioma.orm.Paises;
import com.servinte.axioma.orm.PaquetesOdontologicos;
import com.servinte.axioma.orm.Programas;
import com.servinte.axioma.orm.RecomSerproSerpro;
import com.servinte.axioma.orm.RecomendacionesServProg;
import com.servinte.axioma.orm.RegionesCobertura;

/**
 * Esta clase se encarga de obtener los datos ingresados
 * por el usuario y mapearlos a los atributos asignados a
 * cada uno.
 *
 * @author Carolina G&oacute;mez
 * @since  08/10/10
 *
 */
@SuppressWarnings("unchecked")
public class ReportePresupuestosOdontologicosContratadosForm extends ActionForm {

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
	
	private DtoReportePresupuestosOdontologicosContratados dtoFiltrosPresupuestosContratados;
	
	
	
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
	 * Atributo que almacena el acr&oacute;nimo de la ubicaci&oacute;n del
	 * logo seg&uacute;n est&eacute; definido en el par&aacute;metro general.
	 */
	
	
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
	 * Atributo que almacena el listado de los paquetes 
	 * odontol&oacute;gicos creados en el sistema.
	 */
	private ArrayList<PaquetesOdontologicos> listadoPaquetesOdonto;
	
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
	 * DTO Recomendaciones Prog Serv Prog Serv
	 */
	private RecomSerproSerpro dtoSerProSerPro = new RecomSerproSerpro();
	
	/**
	 * Lista Códigos Programas Servicios
	 */
	private String listaCodigoProgramaServicios;
	
	/**
	 * DTO PARA MANEJAR LA RECOMENDACIONES SERVICIO PROGRAMA
	 */
	private RecomendacionesServProg dtoRecomenServicioPrograma;
	
	/**
	 * Atributo que indica el tipo de salida de impresi&oacute;n 
	 * del reporte generado.
	 */
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
	 * Atributo que almacena el numero de servicios seleccionados
	 * en la b&uacute;squeda de servicios
	 */
	private int numeroServicios;
	
	/**
	 * HashMap para almacenar los servicios de una Unidad de Consulta.
	 */
	private HashMap servicios;
	
	/**
	 * codigos de los servicios insertados para no repetirlos
	 * en la busqueda avanzada de servicios
	 */
	private String codigosServiciosInsertados;
	
	/**
	 * HashMap de especialidades
	 * */
	private HashMap especialidadesMap;
	
	/**
	 * Collection para almacenar el resultado de la consulta del
	 * CUPS y el Servicio.
	 */
	private Collection coleccionCups;
	
	/**
	* Campo para capturar el codServicio asociado a la
	* unidad de consulta de la tabla servicios del campo codigo.
	*/
	private int codServicio;
	
	/**
	 * Atributo para capturar el codigo de la especialidad
	 * de la tabla Servicios del campo especialidad. 
	 */
	private int codigoEspecialidad;
	
	/**
	 * almacena la descripcion del servicio
	 */
	private String descripcionCUPS;
	
	/**
	 * HashMap para almacenar los servicios de una Unidad de Consulta.
	 */
	private HashMap serviciosBD;	
	
	private int registrosNuevos;
	
	
	/**
	 * String indicador del servicio
	 * */
	private String indexServicio;
	
	private int index;
	
	
	/**
	 * Atribuo para cargar cual es el codigo tarifario de la institucion
	 * Se Utiliza en presentacion grafica
	 */
	private String codigoTarifario;
	
	
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
			if((dtoFiltrosPresupuestosContratados.getFechaInicial()==null) || 
					((dtoFiltrosPresupuestosContratados.getFechaInicial().toString()).equals(""))){
				
				errores.add("La fecha Inicial es requerida", 
						new ActionMessage("errors.required", "El campo Fecha Inicial"));
				
			}else{
				String fechaInicial = UtilidadFecha.conversionFormatoFechaAAp(
						dtoFiltrosPresupuestosContratados.getFechaInicial());
				String fechaActual = UtilidadFecha.conversionFormatoFechaAAp(
						Calendar.getInstance().getTime());
				
				if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(
						fechaInicial, fechaActual)){
							
					 errores.add("FECHA INICIAL MAYOR QUE FECHA ACTUAL.", new ActionMessage(
									 "errors.fechaPosteriorIgualActual"," Inicial "+fechaInicial," Actual "+fechaActual));	
				}
			}
			if((dtoFiltrosPresupuestosContratados.getFechaFinal()==null) || 
						((dtoFiltrosPresupuestosContratados.getFechaFinal().toString()).equals(""))){
				
				errores.add("La fecha Fin es requerida", 
						new ActionMessage("errors.required", "El campo Fecha Fin"));
				
			}else{
				String fechaFin = UtilidadFecha.conversionFormatoFechaAAp(
						dtoFiltrosPresupuestosContratados.getFechaFinal());
				
				String fechaActual = UtilidadFecha.conversionFormatoFechaAAp(
						Calendar.getInstance().getTime());
				
				
				if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(fechaFin, fechaActual)){
					errores.add("La fecha Fin es mayor que fecha actual", 
							new ActionMessage("errors.fechaPosteriorIgualActual", " Fin "+fechaFin," Actual "+fechaActual));
				}
				if((dtoFiltrosPresupuestosContratados.getFechaInicial()!=null) && 
						(!(dtoFiltrosPresupuestosContratados.getFechaInicial().toString()).equals(""))){
					
					String fechaInicial = UtilidadFecha.conversionFormatoFechaAAp(
							dtoFiltrosPresupuestosContratados.getFechaInicial());					
					
					
					if(UtilidadFecha.esFechaMenorQueOtraReferencia(fechaFin,fechaInicial)){
						errores.add("FECHA FIN MENOR QUE FECHA INICIAL.", new ActionMessage(
										 "errors.fechaAnteriorIgualAOtraDeReferencia"," Final "+fechaFin," Inicial "+fechaInicial));
					}
				}
			}
			
			if (dtoFiltrosPresupuestosContratados.getValorContratoInicial()!=0) {
				
				if (dtoFiltrosPresupuestosContratados.getValorContratoFinal()==0){
					
					errores.add("EL RANGO DE VALOR FINAL CONTRATO ES REQUERIDO.", new ActionMessage(
					 "errors.rangoValorFinalContrato"));
					
				}
				else {
					
					if (dtoFiltrosPresupuestosContratados.getValorContratoInicial() >= dtoFiltrosPresupuestosContratados.getValorContratoFinal()){
						
						errores.add("VALOR FIN CONTRATO MENOR QUE VALOR INICIAL CONTRATO.", new ActionMessage(
								 "errors.valorInicialContratoMayorIgualValorFinalContrato"," Inicial "
								 +dtoFiltrosPresupuestosContratados.getValorContratoInicial()," Final "
								 +dtoFiltrosPresupuestosContratados.getValorContratoFinal()));
					}
				}
				
			}
			
			if (UtilidadTexto.isEmpty(dtoFiltrosPresupuestosContratados.getCodigoPaisResidencia())
					|| dtoFiltrosPresupuestosContratados.getCodigoPaisResidencia().trim().equals("-1")) {
				
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
		this.dtoFiltrosPresupuestosContratados= new DtoReportePresupuestosOdontologicosContratados();
		this.listaPaises=new ArrayList<Paises>();
		this.listaCiudades=new ArrayList<Ciudades>();
		this.listaRegiones=new ArrayList<RegionesCobertura>();
		this.listaEmpresaInstitucion=new ArrayList<EmpresasInstitucion>();
		this.listaCentrosAtencion=new ArrayList<DtoCentrosAtencion>();
		this.setDeshabilitaCiudad(false);
		this.deshabilitaRegion=false;
		this.esMultiempresa="";
		this.listadoIndicativoContrato=new ArrayList<DtoIntegridadDominio>();
		dtoFiltrosPresupuestosContratados.setIndicativoContrato("");
		this.listaProfesionalesOdont=new ArrayList<DtoPersonas>();
		this.listadoPaquetesOdonto=new ArrayList<PaquetesOdontologicos>();
		this.deshabilitaPrograma="false";
		this.utilizaProgramasOdonto="";
		this.listaCodigoProgramaServicios="";
		this.setDtoSerProSerPro(new RecomSerproSerpro());
		this.dtoSerProSerPro = new RecomSerproSerpro();
		this.dtoSerProSerPro.setProgramas(new Programas());
		this.dtoRecomenServicioPrograma = new RecomendacionesServProg();
		this.dtoFiltrosPresupuestosContratados.setCodigoPrograma(0);
		this.dtoSerProSerPro.setCodigoPk(0);
		this.dtoSerProSerPro.setProgramas(new Programas());
		//dtoFiltrosPresupuestosContratados.setServicio(new InfoDatosStr());
		dtoFiltrosPresupuestosContratados.getServicio().setNombre(null);
		dtoFiltrosPresupuestosContratados.getServicio().setCodigo(null);
		this.tipoSalida="";
		this.nombreArchivoGenerado="";
		this.enumTipoSalida=null;
		this.rutaLogo = "";
		this.setNumeroServicios(ConstantesBD.codigoNuncaValido);
		this.servicios=new HashMap();
		this.servicios.put("numeroServicios","0");
		this.codigosServiciosInsertados="";
		this.especialidadesMap=new HashMap();
		this.serviciosBD=new HashMap();
		this.registrosNuevos=ConstantesBD.codigoNuncaValido;
		this.indexServicio="";
		this.index=ConstantesBD.codigoNuncaValido;
		this.setCodigoTarifario("");
		
		
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
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo dtoFiltrosPresupuestosContratados
	 * @param dtoFiltrosPresupuestosContratados
	 */
	
	
	public void setDtoFiltrosPresupuestosContratados(
			DtoReportePresupuestosOdontologicosContratados dtoFiltrosPresupuestosContratados) {
		this.dtoFiltrosPresupuestosContratados = dtoFiltrosPresupuestosContratados;
	}
	
	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 * del atributo dtoFiltrosPresupuestosContratados
	 * 
	 * @return dtoFiltrosPresupuestosContratados
	 */

	public DtoReportePresupuestosOdontologicosContratados getDtoFiltrosPresupuestosContratados() {
		return dtoFiltrosPresupuestosContratados;
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

	/**
	 * M&eacute;todo que se encarga de establecer el valor
	 * del atributo listadoIndicativoContrato
	 * @param listadoIndicativoContrato
	 */
	
	
	public void setListadoIndicativoContrato(
			ArrayList<DtoIntegridadDominio> listadoIndicativoContrato) {
		this.listadoIndicativoContrato = listadoIndicativoContrato;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor
	 * del atributo listadoIndicativoContrato
	 * @return
	 */
	
	
	public ArrayList<DtoIntegridadDominio> getListadoIndicativoContrato() {
		return listadoIndicativoContrato;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo listaProfesionales
	 * 
	 * @param  valor para el atributo listaProfesionales 
	 */
	
	public void setListaProfesionales(ArrayList<DtoPersonas> listaProfesionales) {
		this.listaProfesionales = listaProfesionales;
	}
	
	/**
	 * 
	 * M&eacute;todo que se encarga de obtener el valor 
	 * del atributo listaProfesionales
	 *
	 * @return listaProfesionales
	 * 
	 */

	public ArrayList<DtoPersonas> getListaProfesionales() {
		return listaProfesionales;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo listaProfesionalesOdont
	 * @param listaProfesionalesOdont
	 */
	
	public void setListaProfesionalesOdont(ArrayList<DtoPersonas> listaProfesionalesOdont) {
		this.listaProfesionalesOdont = listaProfesionalesOdont;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor
	 * del atributo listaProfesionalesOdont
	 * @return listaProfesionalesOdont
	 */
	
	public ArrayList<DtoPersonas> getListaProfesionalesOdont() {
		return listaProfesionalesOdont;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo listadoPaquetesOdonto
	 * @param listadoPaquetesOdonto
	 */
	
	public void setListadoPaquetesOdonto(ArrayList<PaquetesOdontologicos> listadoPaquetesOdonto) {
		this.listadoPaquetesOdonto = listadoPaquetesOdonto;
	}
	
	/**
	 * M&eacute;todo que se encarga de obtener el valor
	 * del atributo listadoPaquetesOdonto
	 * @return listadoPaquetesOdonto
	 */
	
	public ArrayList<PaquetesOdontologicos> getListadoPaquetesOdonto() {
		return listadoPaquetesOdonto;
	}
	
	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo deshabilitaPrograma
	 * @param deshabilitaPrograma
	 */

	public void setDeshabilitaPrograma(String deshabilitaPrograma) {
		this.deshabilitaPrograma = deshabilitaPrograma;
	}
	
	/**
	 * M&eacute;todo que se encarga de obtener el valor
	 * del atributo deshabilitaPrograma
	 * @return deshabilitaPrograma
	 */

	public String getDeshabilitaPrograma() {
		return deshabilitaPrograma;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo utilizaProgramasOdonto
	 * @param utilizaProgramasOdonto
	 */
	
	public void setUtilizaProgramasOdonto(String utilizaProgramasOdonto) {
		this.utilizaProgramasOdonto = utilizaProgramasOdonto;
	}
	
	/**
	 * M&eacute;todo que se encarga de obtener el valor
	 * del atributo utilizaProgramasOdonto
	 * @return
	 */

	public String getUtilizaProgramasOdonto() {
		return utilizaProgramasOdonto;
	}
	
	/**
	 * M&eacute;todo que se encarga de establecer el valor
	 * del atributo dtoSerProSerPro
	 * @param dtoSerProSerPro
	 */

	public void setDtoSerProSerPro(RecomSerproSerpro dtoSerProSerPro) {
		this.dtoSerProSerPro = dtoSerProSerPro;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor
	 * del atributo dtoSerProSerPro
	 * @return
	 */
	
	public RecomSerproSerpro getDtoSerProSerPro() {
		
		dtoFiltrosPresupuestosContratados.setCodigoPrograma(dtoSerProSerPro.getProgramas().getCodigo());
		return dtoSerProSerPro;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor
	 * del atributo listaCodigoProgramaServicios
	 * @param listaCodigoProgramaServicios
	 */
	
	public void setListaCodigoProgramaServicios(
			String listaCodigoProgramaServicios) {
		this.listaCodigoProgramaServicios = listaCodigoProgramaServicios;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor
	 * del atributo listaCodigoProgramaServicios
	 * @return
	 */
	
	public String getListaCodigoProgramaServicios() {
		return listaCodigoProgramaServicios;
	}

	public void setDtoRecomenServicioPrograma(RecomendacionesServProg dtoRecomenServicioPrograma) {
		this.dtoRecomenServicioPrograma = dtoRecomenServicioPrograma;
	}

	public RecomendacionesServProg getDtoRecomenServicioPrograma() {
		return dtoRecomenServicioPrograma;
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

	public void setNumeroServicios(int numeroServicios) {
		this.numeroServicios = numeroServicios;
	}

	public int getNumeroServicios() {
		return numeroServicios;
	}

	public HashMap getServicios() {
		return servicios;
	}

	public void setServicios(HashMap servicios) {
		this.servicios = servicios;
	}
	
	/**
	 * @return Returns the servicios.
	 */
	public Object getServicios(String key) {
		return servicios.get(key);
	}
	/**
	 * @param servicios The servicios to set.
	 */
	public void setServicios(String key,Object value) {
		this.servicios.put(key,value);
	}
	
	public String getCodigosServiciosInsertados() {
		return codigosServiciosInsertados;
	}

	public void setCodigosServiciosInsertados(String codigosServiciosInsertados) {
		this.codigosServiciosInsertados = codigosServiciosInsertados;
	}

	public HashMap getEspecialidadesMap() {
		return especialidadesMap;
	}

	public void setEspecialidadesMap(HashMap especialidadesMap) {
		this.especialidadesMap = especialidadesMap;
	}

	public Collection getColeccionCups() {
		return coleccionCups;
	}

	public void setColeccionCups(Collection coleccionCups) {
		this.coleccionCups = coleccionCups;
	}

	public int getCodServicio() {
		return codServicio;
	}

	public void setCodServicio(int codServicio) {
		this.codServicio = codServicio;
	}

	public void setCodigoEspecialidad(int codigoEspecialidad) {
		this.codigoEspecialidad = codigoEspecialidad;
	}

	public int getCodigoEspecialidad() {
		return codigoEspecialidad;
	}

	public void setDescripcionCUPS(String descripcionCUPS) {
		this.descripcionCUPS = descripcionCUPS;
	}

	public String getDescripcionCUPS() {
		return descripcionCUPS;
	}

	public HashMap getServiciosBD() {
		return serviciosBD;
	}

	public void setServiciosBD(HashMap serviciosBD) {
		this.serviciosBD = serviciosBD;
	}
	
	public Object getServiciosBD(String key) {
		return serviciosBD.get(key);
	}
	/**
	 * @param servicios The servicios to set.
	 */
	public void setServiciosBD(String key,Object value) {
		this.serviciosBD.put(key,value);
	}

	public int getRegistrosNuevos() {
		return registrosNuevos;
	}

	public void setRegistrosNuevos(int registrosNuevos) {
		this.registrosNuevos = registrosNuevos;
	}

	public String getIndexServicio() {
		return indexServicio;
	}

	public void setIndexServicio(String indexServicio) {
		this.indexServicio = indexServicio;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public int getIndex() {
		return index;
	}

	public void setCodigoTarifario(String codigoTarifario) {
		this.codigoTarifario = codigoTarifario;
	}

	public String getCodigoTarifario() {
		return codigoTarifario;
	}
	
	
	

	
	
}
