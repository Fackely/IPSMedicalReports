package com.princetonsa.actionform.odontologia;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import util.ConstantesBD;
import util.UtilidadFecha;
import util.UtilidadTexto;

import com.princetonsa.dto.administracion.DtoIntegridadDominio;
import com.princetonsa.dto.manejoPaciente.DtoCentrosAtencion;
import com.princetonsa.dto.odontologia.DtoFiltroReportePromocionesOdontologicas;
import com.princetonsa.enu.impresion.EnumTiposSalida;
import com.servinte.axioma.orm.Ciudades;
import com.servinte.axioma.orm.EmpresasInstitucion;
import com.servinte.axioma.orm.Especialidades;
import com.servinte.axioma.orm.Paises;
import com.servinte.axioma.orm.Programas;
import com.servinte.axioma.orm.RecomSerproSerpro;
import com.servinte.axioma.orm.RegionesCobertura;

public class ReportePromocionesOdontologicasForm extends ActionForm{

	private static final long serialVersionUID = 1L;
	
	/**
	 * Atributo usado para completar la ruta hacia la cual se debe 
	 * direccionar la aplicación.
	 */
	private String path;
	
	/**
	 * Almacena la acción a realizar desde las página
	 */
	private String estado;
	
	
	/**
	 * Atributo que indica si el campo de ciudad se encuentra deshabilitado.
	 */
	private boolean deshabilitaCiudad;
	
	
	/**
	 * Atributo que almacena los datos a enviar en la consulta
	 */
	private DtoFiltroReportePromocionesOdontologicas filtroPromociones;
	
	
	/**
	 * Atributo que almacena el nombre del archivo para el reporte
	 */
	private String nombreArchivoGenerado;
	
	
	/**
	 * Atributo que almacena el tipo de salida del reporte
	 */
	private String tipoSalida;
	
	
	/**
	 * enumeración del tipo de salida.
	 */
	private EnumTiposSalida enumTipoSalida;
	
	/**
	 * Atributo donde se almacenan los paises existentes en el sistema.
	 */
	private ArrayList<Paises> paises;
	
	/**
	 * Atributo donde se almacenan las cuidades existentes en el sistema.
	 */
	private ArrayList<Ciudades> listaCiudades;
	
	/**
	 * Atributo donde se almacenan las regiones existentes en el sistema.
	 */
	private ArrayList<RegionesCobertura> regiones;
	
	/**
	 * Atributo que almacena el listado de empresas
	 * institucion existentes en el sistema.
	 */
	private ArrayList<EmpresasInstitucion> listaEmpresaInstitucion;
	
	
	/**
	 * Atributo que indica si la institucion-empresa es multiempresa o no.
	 */
	private String institucionMultiempresa;
	
	
	/**
	 * Atributo donde se almacenan los centros de atenci&oacute;n existentes en el sistema.
	 */
	private ArrayList<DtoCentrosAtencion> centrosAtencion;
	
	
	/**
	 * Atributo que almacena el listado con las opciones
	 * femenino y masculino.
	 */
	private ArrayList<DtoIntegridadDominio> listaSexoPaciente;
	
	
	/**
	 * Atributo que almacena el listado con las opciones
	 * femenino y masculino.
	 */
	private ArrayList<DtoIntegridadDominio> listaEstadosPromocion;
	
	
	/**
	 * Atributo que almacena el listado de los estados civiles
	 * 
	 */
	@SuppressWarnings("rawtypes")
	private HashMap listaEstadosCiviles;
	
	/**
	 * Atributo que almacena el listado de las ocupaciones de los pacientes
	 * 
	 */
	@SuppressWarnings("rawtypes")
	private HashMap listaOcupaciones;
	
	
	/**
	 * Atributo que almacena el listado de los convenios de tipo atencion odontologica
	 * 
	 */
	ArrayList<HashMap<String, Object>> listadoConveniosAtencionOdonto;
	
	
	/**
	 * Atributo donde se almacenan las especialidades odontológicas existentes en el sistema.
	 */
	private List<Especialidades> especialidades;
	
	
	/**
	 * DTO RECOMENDACIONES PROG SER PROG SER
	 */	
	private RecomSerproSerpro dtoSerProSerPro = new RecomSerproSerpro();
	
	
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
	 * LISTA CODIGOS PROGRAMAS SERVICIOS
	 */
	private String listaCodigoProgramaServicios;
	
	/**
	 * Este mètodo se encarga de inicializar todos los valores de la forma.
	 *
	 *
	 * @autor Yennifer Guerrero
	 */
	public void reset(){
		this.filtroPromociones = new DtoFiltroReportePromocionesOdontologicas();
		this.paises=new ArrayList<Paises>();
		this.listaCiudades = new ArrayList<Ciudades>();
		this.regiones = new ArrayList<RegionesCobertura>();
		this.centrosAtencion = new ArrayList<DtoCentrosAtencion>();
		this.especialidades = new ArrayList<Especialidades>();
		this.estado = "";
		this.path = "";
		this.dtoSerProSerPro = 					new RecomSerproSerpro();
		this.dtoSerProSerPro.setProgramas(		new Programas());
		this.dtoSerProSerPro.setCodigoPk(ConstantesBD.codigoNuncaValido);
		this.setDeshabilitaPrograma("false");
	}
	
	
	/**
	 * Este m&eacutetodo se encarga de realizar las validaciones de 
	 * los datos ingresados por el usuario
	 * 
	 * @author Yennifer Guerrero
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {	
		ActionErrors errores=null;
		errores=new ActionErrors();
		if(estado.equals("generarReporte")){
			if(this.filtroPromociones.getFechaVigInicial()==null&&this.filtroPromociones.getFechaVigFinal()==null
					&&this.filtroPromociones.getFechaGenInicial()==null&&this.filtroPromociones.getFechaGenFinal()==null){
				
				errores.add("Las fechas Inicial y Finales de Generación y/o Vigencia son requeridas", 
						new ActionMessage("errors.notEspecific", "Las fechas Iniciales y Finales de Generación y/o Vigencia Son requeridas"));
				this.tipoSalida=null;
				
				
			}else
			{
				if(this.filtroPromociones.getFechaGenInicial()==null&&this.filtroPromociones.getFechaGenFinal()!=null)
				{
					errores.add("La fecha Inicial de Generación es requerida", 
						new ActionMessage("errors.required", "La fecha Inicial de Generación"));
					this.tipoSalida=null;
				}else
				if(this.filtroPromociones.getFechaGenInicial()!=null&&this.filtroPromociones.getFechaGenFinal()==null){
					String fechaInicial = UtilidadFecha.conversionFormatoFechaAAp(
							this.filtroPromociones.getFechaGenInicial());
					String fechaActual = UtilidadFecha.conversionFormatoFechaAAp(
							Calendar.getInstance().getTime());
					
					if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(
							fechaInicial, fechaActual)){
								
						 errores.add("FECHA INICIAL MAYOR QUE FECHA ACTUAL.", new ActionMessage(
										 "errors.fechaPosteriorIgualActual"," Inicial "+fechaInicial," Actual "+fechaActual));
						 this.tipoSalida=null;
					}
					else{
					errores.add("La fecha Final de Generación es requerida", 
							new ActionMessage("errors.required", "La fecha Final de Generación"));
						this.tipoSalida=null;
					}
				}else
				if(this.filtroPromociones.getFechaGenInicial()!=null&&this.filtroPromociones.getFechaGenFinal()!=null){
					String fechaInicial = UtilidadFecha.conversionFormatoFechaAAp(
							this.filtroPromociones.getFechaGenInicial());
					String fechaFinal = UtilidadFecha.conversionFormatoFechaAAp(
							this.filtroPromociones.getFechaGenFinal());
					
					if(UtilidadFecha.esFechaMenorQueOtraReferencia(fechaFinal,fechaInicial)){
						errores.add("FECHA FIN MENOR QUE FECHA INICIAL.", new ActionMessage(
										 "errors.fechaAnteriorIgualAOtraDeReferencia"," Final de Generación "+fechaFinal," Inicial de Generación "+fechaInicial));
						this.tipoSalida=null;
					}
				}
				
				
				if(this.filtroPromociones.getFechaVigInicial()==null&&this.filtroPromociones.getFechaVigFinal()!=null)
				{
					errores.add("La fecha Inicial de Vigencia es requerida", 
							new ActionMessage("errors.required", "La fecha Inicial de Vigencia"));
					this.tipoSalida=null;
				}else
				if(this.filtroPromociones.getFechaVigInicial()!=null&&this.filtroPromociones.getFechaVigFinal()==null){
					
					errores.add("La fecha Final de Vigencia es requerida", 
							new ActionMessage("errors.required", "La fecha Final de Vigencia"));
					this.tipoSalida=null;
				}else
				if(this.filtroPromociones.getFechaVigInicial()!=null&&this.filtroPromociones.getFechaVigFinal()!=null){
						String fechaInicial = UtilidadFecha.conversionFormatoFechaAAp(
								this.filtroPromociones.getFechaVigInicial());
						String fechaFinal = UtilidadFecha.conversionFormatoFechaAAp(
								this.filtroPromociones.getFechaVigFinal());
						
						if(UtilidadFecha.esFechaMenorQueOtraReferencia(fechaFinal,fechaInicial)){
							errores.add("FECHA FIN MENOR QUE FECHA INICIAL.", new ActionMessage(
											 "errors.fechaAnteriorIgualAOtraDeReferencia"," Final de Vigencia "+fechaFinal," Inicial de Vigencia "+fechaInicial));
							this.tipoSalida=null;
						}
				}
				
			
				
			}
			
			//validaciones rangos de edades
			if(this.filtroPromociones.getRangoEdadInicialPaciente()!=null && this.filtroPromociones.getRangoEdadFinalPaciente()==null){
				errores.add("EL RANGO DE EDAD FINAL ES REQUERIDO.", new ActionMessage(
				 "errors.rangoEdadFinalRequerido"));
				this.tipoSalida=null;
			}
			else 
				if(this.filtroPromociones.getRangoEdadFinalPaciente()!=null&&this.filtroPromociones.getRangoEdadInicialPaciente()!=null){
					  if (this.filtroPromociones.getRangoEdadInicialPaciente() >= this.filtroPromociones.getRangoEdadFinalPaciente()){
							
							errores.add("RANGO EDAD FINAL MENOR QUE EDAD INICIAL.", new ActionMessage(
									 "errors.edadInicialMayorIgualEdadFinal"," Inicial "
									 +this.filtroPromociones.getRangoEdadInicialPaciente()," Final "
									 +this.filtroPromociones.getRangoEdadFinalPaciente()));
							this.tipoSalida=null;
						
					  }
			  }
			
			if (UtilidadTexto.isEmpty(this.filtroPromociones.getCodigoPaisSeleccionado())
					|| this.filtroPromociones.getCodigoPaisSeleccionado().equals(ConstantesBD.codigoNuncaValido+"")) {
				
				errores.add("PAIS RESIDENCIA ES REQUERIDO.", new ActionMessage(
						 "errors.paisResidenciaRequerido"));
				this.tipoSalida=null;
			}
		}
		
		return errores;
	}


	/**
	 * Método que se encarga de obtener el 
	 * valor del atributo  path
	 *
	 * @return retorna la variable path
	 */
	public String getPath() {
		return path;
	}


	/**
	 * Método que se encarga de establecer el valor
	 * del atributo path
	 * @param path es el valor para el atributo path 
	 */
	public void setPath(String path) {
		this.path = path;
	}


	/**
	 * Método que se encarga de obtener el 
	 * valor del atributo  estado
	 *
	 * @return retorna la variable estado
	 */
	public String getEstado() {
		return estado;
	}


	/**
	 * Método que se encarga de establecer el valor
	 * del atributo estado
	 * @param estado es el valor para el atributo estado 
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}


	public void setDeshabilitaCiudad(boolean deshabilitaCiudad) {
		this.deshabilitaCiudad = deshabilitaCiudad;
	}


	public boolean isDeshabilitaCiudad() {
		return deshabilitaCiudad;
	}


	public void setFiltroPromociones(DtoFiltroReportePromocionesOdontologicas filtroPromociones) {
		this.filtroPromociones = filtroPromociones;
	}


	public DtoFiltroReportePromocionesOdontologicas getFiltroPromociones() {
		return filtroPromociones;
	}


	public void setNombreArchivoGenerado(String nombreArchivoGenerado) {
		this.nombreArchivoGenerado = nombreArchivoGenerado;
	}


	public String getNombreArchivoGenerado() {
		return nombreArchivoGenerado;
	}


	public void setTipoSalida(String tipoSalida) {
		this.tipoSalida = tipoSalida;
	}


	public String getTipoSalida() {
		return tipoSalida;
	}


	public void setEnumTipoSalida(EnumTiposSalida enumTipoSalida) {
		this.enumTipoSalida = enumTipoSalida;
	}


	public EnumTiposSalida getEnumTipoSalida() {
		return enumTipoSalida;
	}


	public void setPaises(ArrayList<Paises> paises) {
		this.paises = paises;
	}


	public ArrayList<Paises> getPaises() {
		return paises;
	}


	public void setListaCiudades(ArrayList<Ciudades> listaCiudades) {
		this.listaCiudades = listaCiudades;
	}


	public ArrayList<Ciudades> getListaCiudades() {
		return listaCiudades;
	}


	public void setRegiones(ArrayList<RegionesCobertura> regiones) {
		this.regiones = regiones;
	}


	public ArrayList<RegionesCobertura> getRegiones() {
		return regiones;
	}


	public void setListaEmpresaInstitucion(ArrayList<EmpresasInstitucion> listaEmpresaInstitucion) {
		this.listaEmpresaInstitucion = listaEmpresaInstitucion;
	}


	public ArrayList<EmpresasInstitucion> getListaEmpresaInstitucion() {
		return listaEmpresaInstitucion;
	}


	public void setInstitucionMultiempresa(String institucionMultiempresa) {
		this.institucionMultiempresa = institucionMultiempresa;
	}


	public String getInstitucionMultiempresa() {
		return institucionMultiempresa;
	}


	public void setCentrosAtencion(ArrayList<DtoCentrosAtencion> centrosAtencion) {
		this.centrosAtencion = centrosAtencion;
	}


	public ArrayList<DtoCentrosAtencion> getCentrosAtencion() {
		return centrosAtencion;
	}


	public void setListaSexoPaciente(ArrayList<DtoIntegridadDominio> listaSexoPaciente) {
		this.listaSexoPaciente = listaSexoPaciente;
	}


	public ArrayList<DtoIntegridadDominio> getListaSexoPaciente() {
		return listaSexoPaciente;
	}


	@SuppressWarnings("rawtypes")
	public void setListaEstadosCiviles(HashMap listaEstadosCiviles) {
		this.listaEstadosCiviles = listaEstadosCiviles;
	}


	@SuppressWarnings("rawtypes")
	public HashMap getListaEstadosCiviles() {
		return listaEstadosCiviles;
	}


	@SuppressWarnings("rawtypes")
	public void setListaOcupaciones(HashMap listaOcupaciones) {
		this.listaOcupaciones = listaOcupaciones;
	}


	@SuppressWarnings("rawtypes")
	public HashMap getListaOcupaciones() {
		return listaOcupaciones;
	}
	
	public void setListadoConveniosAtencionOdonto(ArrayList<HashMap<String, Object>> listadoConveniosAtencionOdonto) {
		this.listadoConveniosAtencionOdonto = listadoConveniosAtencionOdonto;
	}


	public ArrayList<HashMap<String, Object>> getListadoConveniosAtencionOdonto() {
		return listadoConveniosAtencionOdonto;
	}



	public void setEspecialidades(List<Especialidades> especialidades) {
		this.especialidades = especialidades;
	}


	public List<Especialidades> getEspecialidades() {
		return especialidades;
	}


	public void setDtoSerProSerPro(RecomSerproSerpro dtoSerProSerPro) {
		this.dtoSerProSerPro = dtoSerProSerPro;
	}


	public RecomSerproSerpro getDtoSerProSerPro() {
		filtroPromociones.setCodigoPrograma(dtoSerProSerPro.getProgramas().getCodigo());
		return dtoSerProSerPro;
	}


	public void setDeshabilitaPrograma(String deshabilitaPrograma) {
		this.deshabilitaPrograma = deshabilitaPrograma;
	}


	public String getDeshabilitaPrograma() {
		return deshabilitaPrograma;
	}


	public void setUtilizaProgramasOdonto(String utilizaProgramasOdonto) {
		this.utilizaProgramasOdonto = utilizaProgramasOdonto;
	}


	public String getUtilizaProgramasOdonto() {
		return utilizaProgramasOdonto;
	}


	public void setListaCodigoProgramaServicios(
			String listaCodigoProgramaServicios) {
		this.listaCodigoProgramaServicios = listaCodigoProgramaServicios;
	}


	public String getListaCodigoProgramaServicios() {
		return listaCodigoProgramaServicios;
	}


	public void setListaEstadosPromocion(ArrayList<DtoIntegridadDominio> listaEstadosPromocion) {
		this.listaEstadosPromocion = listaEstadosPromocion;
	}


	public ArrayList<DtoIntegridadDominio> getListaEstadosPromocion() {
		return listaEstadosPromocion;
	}
}
