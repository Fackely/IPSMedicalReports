package com.princetonsa.actionform.odontologia;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import util.ConstantesBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;

import com.princetonsa.dto.administracion.DtoPersonas;
import com.princetonsa.dto.facturacion.DtoServicios;
import com.princetonsa.dto.manejoPaciente.DtoCentrosAtencion;
import com.princetonsa.dto.odontologia.DtoFiltroReporteTiempoEsperaAtencionCitasOdontologicas;
import com.princetonsa.dto.tesoreria.DtoUsuarioPersona;
import com.princetonsa.enu.impresion.EnumTiposSalida;
import com.servinte.axioma.dto.consultaExterna.DtoUnidadesConsulta;
import com.servinte.axioma.orm.Ciudades;
import com.servinte.axioma.orm.EmpresasInstitucion;
import com.servinte.axioma.orm.Especialidades;
import com.servinte.axioma.orm.Paises;
import com.servinte.axioma.orm.RegionesCobertura;

public class ReporteTiempoEsperaAtencionCitasOdontologicasForm extends ActionForm{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public ReporteTiempoEsperaAtencionCitasOdontologicasForm() {
		// TODO Auto-generated constructor stub
		
	}

	/**
	 * Atributo donde se almacenan el estado o accion a realizar en las páginas
	 */
	private String estado;
	
	/**
	 * Atributo usado para completar la ruta hacia la cual se debe 
	 * direccionar la aplicación.
	 */
	private String path;
	
	/**
	 * Dto para almacenar los parámetros de búsqueda 
	 * del reporte tiempo espera atención citas odontológicas
	 */
	private DtoFiltroReporteTiempoEsperaAtencionCitasOdontologicas filtroTiempoEspera;
	
	
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
	 * Atributo que almacena el parametro general del módulo de Historia Clínica Ocupación Odontólogo
	 */
	private String ocupacionOdontologo;
	
	/**
	 * Atributo donde se almacenan los centros de atenci&oacute;n existentes en el sistema.
	 */
	private ArrayList<DtoCentrosAtencion> centrosAtencion;
	
	/**
	 * Atributo que indica si el campo de ciudad se encuentra deshabilitado.
	 */
	private boolean deshabilitaCiudad;
	
	/**
	 * Atributo donde se almacenan las especialidades odontológicas existentes en el sistema.
	 */
	private List<Especialidades> especialidades;
	
	/**
	 * Atributo donde se almacenan las unidades agenda.
	 */
	private List<DtoUnidadesConsulta> unidadesConsulta;
	
	/**
	 * Atributo donde se almacenan el tamano de unidades de consulta.
	 */
	private int tamanoUnidadesConsulta;
	
	/**
	 * Atributo donde se almacenan los profesionales de la salud existentes en el sistema.
	 */
	private ArrayList<DtoPersonas> profesionales;
	
	/**
	 * Atributo donde se almacenan los usuarios existentes en el sistema.
	 */
	private ArrayList<DtoUsuarioPersona> usuarios;
	
	/**
	 * Atributo que almacena el código del
	 * servicio odontológico a eliminar.
	 */
	private int indiceEliminarServicio;
	
	/**
	 * Atributo que almacena el c&oacute;digo del
	 * servicio odontológico.
	 */
	private int codigoServicio=ConstantesBD.codigoNuncaValido;
	
	
	/**
	 * Atributo que almacena el nombre del
	 * servicio odontológico.
	 */
	private String nombreServicio="";
	
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
	 * Este método se encarga de inicializar todos los valores de la forma.
	 *  @author Fabian Becerra
     */
	public void reset()
	{
		this.filtroTiempoEspera = 					new DtoFiltroReporteTiempoEsperaAtencionCitasOdontologicas();
		this.paises = 						new ArrayList<Paises>();
		this.listaCiudades = 					new ArrayList<Ciudades>();
		this.regiones = 					new ArrayList<RegionesCobertura>();
		this.centrosAtencion = 			new ArrayList<DtoCentrosAtencion>();
		this.listaEmpresaInstitucion = 			new ArrayList<EmpresasInstitucion>();
		this.getFiltroTiempoEspera().setServicios(new ArrayList<DtoServicios>());
		this.getFiltroTiempoEspera().setCiudadDeptoPais(ConstantesBD.codigoNuncaValido+"");
		this.especialidades= new ArrayList<Especialidades>();
		this.getFiltroTiempoEspera().setTiposCita(new String[]{}) ;
		
		
	}
	
	/**
	 * Este método se encarga de realizar las validaciones de 
	 * los datos ingresados por el usuario
	 * 
	 * @param ActionMapping
	 * @param HttpServletRequest
	 * @return ActionErrors
	 * 
	 *  @author Fabian Becerra
	 */
	public ActionErrors validate(ActionMapping arg0, HttpServletRequest arg1){
		// TODO Auto-generated method stub
		ActionErrors errores=null;
		errores=new ActionErrors();
		if(estado.equals("generarReporte")){
			
			//validaciones rango de fechas
			if(this.filtroTiempoEspera.getFechaInicial()==null){
				
				errores.add("La fecha Inicial es requerida", 
						new ActionMessage("errors.required", "El campo Fecha Inicial"));
				this.tipoSalida=null;
				
				
			}else{
				String fechaInicial = UtilidadFecha.conversionFormatoFechaAAp(
						this.filtroTiempoEspera.getFechaInicial());
				String fechaActual = UtilidadFecha.conversionFormatoFechaAAp(
						Calendar.getInstance().getTime());
				
				if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(
						fechaInicial, fechaActual)){
							
					 errores.add("FECHA INICIAL MAYOR QUE FECHA ACTUAL.", new ActionMessage(
									 "errors.fechaPosteriorIgualActual"," Inicial "+fechaInicial," Actual "+fechaActual));
					 this.tipoSalida=null;
					
				}
			}
			if(this.filtroTiempoEspera.getFechaFinal()==null){
				
				errores.add("La fecha Fin es requerida", 
						new ActionMessage("errors.required", "El campo Fecha Fin"));
				this.tipoSalida=null;
			
				
			}else{
				String fechaFin = UtilidadFecha.conversionFormatoFechaAAp(
						this.filtroTiempoEspera.getFechaFinal());
				
				String fechaActual = UtilidadFecha.conversionFormatoFechaAAp(
						Calendar.getInstance().getTime());
				
				
				if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(fechaFin, fechaActual)){
					errores.add("La fecha Fin es mayor que fecha actual", 
							new ActionMessage("errors.fechaPosteriorIgualActual", " Fin "+fechaFin," Actual "+fechaActual));
					this.tipoSalida=null;
		
				}
				if(this.filtroTiempoEspera.getFechaInicial()!=null){
					
					String fechaInicial = UtilidadFecha.conversionFormatoFechaAAp(
							this.filtroTiempoEspera.getFechaInicial());					
					
					
					if(UtilidadFecha.esFechaMenorQueOtraReferencia(fechaFin,fechaInicial)){
						errores.add("FECHA FIN MENOR QUE FECHA INICIAL.", new ActionMessage(
										 "errors.fechaAnteriorIgualAOtraDeReferencia"," Final "+fechaFin," Inicial "+fechaInicial));
						this.tipoSalida=null;
					
					}
				}
			}
			
			
			if (UtilidadTexto.isEmpty(this.filtroTiempoEspera.getCodigoPaisSeleccionado())
					|| this.filtroTiempoEspera.getCodigoPaisSeleccionado().equals(ConstantesBD.codigoNuncaValido+"")) {
				
				errores.add("PAIS RESIDENCIA ES REQUERIDO.", new ActionMessage(
						 "errors.paisResidenciaRequerido"));
				this.tipoSalida=null;
			}
			
			if (Utilidades.isEmpty(this.usuarios))
					{
				
				errores.add("USUARIO REQUERIDO.", new ActionMessage(
						 "errors.modOdontoReporteTiemposEsperaNingunUsuarioConPermisosCentroCosto"));
				this.tipoSalida=null;
			}
			
			
		}
		
			
	     return errores;
	}

	/**
	 * Método que se encarga de establecer el valor 
	 *  del atributo estado
	 * 
	 * @param  valor para el atributo estado
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}

	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo estado
	 * 
	 * @return  Retorna la variable estado
	 */
	public String getEstado() {
		return estado;
	}

	/**
	 * Método que se encarga de establecer el valor 
	 *  del atributo path
	 * 
	 * @param  valor para el atributo path
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo path
	 * 
	 * @return  Retorna la variable path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * Método que se encarga de establecer el valor 
	 *  del atributo filtroTiempoEspera
	 * 
	 * @param  valor para el atributo filtroTiempoEspera
	 */
	public void setFiltroTiempoEspera(DtoFiltroReporteTiempoEsperaAtencionCitasOdontologicas filtroTiempoEspera) {
		this.filtroTiempoEspera = filtroTiempoEspera;
	}

	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo filtroTiempoEspera
	 * 
	 * @return  Retorna la variable filtroTiempoEspera
	 */
	public DtoFiltroReporteTiempoEsperaAtencionCitasOdontologicas getFiltroTiempoEspera() {
		return filtroTiempoEspera;
	}

	/**
	 * Método que se encarga de establecer el valor 
	 *  del atributo paises
	 * 
	 * @param  valor para el atributo paises
	 */
	public void setPaises(ArrayList<Paises> paises) {
		this.paises = paises;
	}

	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo paises
	 * 
	 * @return  Retorna la variable paises
	 */
	public ArrayList<Paises> getPaises() {
		return paises;
	}

	/**
	 * Método que se encarga de establecer el valor 
	 *  del atributo listaCiudades
	 * 
	 * @param  valor para el atributo listaCiudades
	 */
	public void setListaCiudades(ArrayList<Ciudades> listaCiudades) {
		this.listaCiudades = listaCiudades;
	}

	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo listaCiudades
	 * 
	 * @return  Retorna la variable listaCiudades
	 */
	public ArrayList<Ciudades> getListaCiudades() {
		return listaCiudades;
	}

	/**
	 * Método que se encarga de establecer el valor 
	 *  del atributo regiones
	 * 
	 * @param  valor para el atributo regiones
	 */
	public void setRegiones(ArrayList<RegionesCobertura> regiones) {
		this.regiones = regiones;
	}

	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo regiones
	 * 
	 * @return  Retorna la variable regiones
	 */
	public ArrayList<RegionesCobertura> getRegiones() {
		return regiones;
	}

	/**
	 * Método que se encarga de establecer el valor 
	 *  del atributo listaEmpresaInstitucion
	 * 
	 * @param  valor para el atributo listaEmpresaInstitucion
	 */
	public void setListaEmpresaInstitucion(ArrayList<EmpresasInstitucion> listaEmpresaInstitucion) {
		this.listaEmpresaInstitucion = listaEmpresaInstitucion;
	}

	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo listaEmpresaInstitucion
	 * 
	 * @return  Retorna la variable listaEmpresaInstitucion
	 */
	public ArrayList<EmpresasInstitucion> getListaEmpresaInstitucion() {
		return listaEmpresaInstitucion;
	}

	/**
	 * Método que se encarga de establecer el valor 
	 *  del atributo institucionMultiempresa
	 * 
	 * @param  valor para el atributo institucionMultiempresa
	 */
	public void setInstitucionMultiempresa(String institucionMultiempresa) {
		this.institucionMultiempresa = institucionMultiempresa;
	}

	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo institucionMultiempresa
	 * 
	 * @return  Retorna la variable institucionMultiempresa
	 */
	public String getInstitucionMultiempresa() {
		return institucionMultiempresa;
	}

	/**
	 * Método que se encarga de establecer el valor 
	 *  del atributo centrosAtencion
	 * 
	 * @param  valor para el atributo centrosAtencion
	 */
	public void setCentrosAtencion(ArrayList<DtoCentrosAtencion> centrosAtencion) {
		this.centrosAtencion = centrosAtencion;
	}

	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo centrosAtencion
	 * 
	 * @return  Retorna la variable centrosAtencion
	 */
	public ArrayList<DtoCentrosAtencion> getCentrosAtencion() {
		return centrosAtencion;
	}

	/**
	 * Método que se encarga de establecer el valor 
	 *  del atributo deshabilitaCiudad
	 * 
	 * @param  valor para el atributo deshabilitaCiudad
	 */
	public void setDeshabilitaCiudad(boolean deshabilitaCiudad) {
		this.deshabilitaCiudad = deshabilitaCiudad;
	}

	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo deshabilitaCiudad
	 * 
	 * @return  Retorna la variable deshabilitaCiudad
	 */
	public boolean isDeshabilitaCiudad() {
		return deshabilitaCiudad;
	}

	/**
	 * Método que se encarga de establecer el valor 
	 *  del atributo especialidades
	 * 
	 * @param  valor para el atributo especialidades
	 */
	public void setEspecialidades(List<Especialidades> especialidades) {
		this.especialidades = especialidades;
	}

	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo especialidades
	 * 
	 * @return  Retorna la variable especialidades
	 */
	public List<Especialidades> getEspecialidades() {
		return especialidades;
	}

	/**
	 * Método que se encarga de establecer el valor 
	 *  del atributo profesionales
	 * 
	 * @param  valor para el atributo profesionales
	 */
	public void setProfesionales(ArrayList<DtoPersonas> profesionales) {
		this.profesionales = profesionales;
	}

	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo profesionales
	 * 
	 * @return  Retorna la variable profesionales
	 */
	public ArrayList<DtoPersonas> getProfesionales() {
		return profesionales;
	}

	/**
	 * Método que se encarga de establecer el valor 
	 *  del atributo usuarios
	 * 
	 * @param  valor para el atributo usuarios
	 */
	public void setUsuarios(ArrayList<DtoUsuarioPersona> usuarios) {
		this.usuarios = usuarios;
	}

	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo usuarios
	 * 
	 * @return  Retorna la variable usuarios
	 */
	public ArrayList<DtoUsuarioPersona> getUsuarios() {
		return usuarios;
	}

	/**
	 * Método que se encarga de establecer el valor 
	 *  del atributo indiceEliminarServicio
	 * 
	 * @param  valor para el atributo indiceEliminarServicio
	 */
	public void setIndiceEliminarServicio(int indiceEliminarServicio) {
		this.indiceEliminarServicio = indiceEliminarServicio;
	}

	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo indiceEliminarServicio
	 * 
	 * @return  Retorna la variable indiceEliminarServicio
	 */
	public int getIndiceEliminarServicio() {
		return indiceEliminarServicio;
	}

	/**
	 * Método que se encarga de establecer el valor 
	 *  del atributo codigoServicio
	 * 
	 * @param  valor para el atributo codigoServicio
	 */
	public void setCodigoServicio(int codigoServicio) {
		this.codigoServicio = codigoServicio;
	}

	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo codigoServicio
	 * 
	 * @return  Retorna la variable codigoServicio
	 */
	public int getCodigoServicio() {
		return codigoServicio;
	}

	/**
	 * Método que se encarga de establecer el valor 
	 *  del atributo nombreServicio
	 * 
	 * @param  valor para el atributo nombreServicio
	 */
	public void setNombreServicio(String nombreServicio) {
		this.nombreServicio = nombreServicio;
	}

	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo nombreServicio
	 * 
	 * @return  Retorna la variable nombreServicio
	 */
	public String getNombreServicio() {
		return nombreServicio;
	}

	/**
	 * Método que se encarga de establecer el valor 
	 *  del atributo unidadesConsulta
	 * 
	 * @param  valor para el atributo unidadesConsulta
	 */
	public void setUnidadesConsulta(List<DtoUnidadesConsulta> unidadesConsulta) {
		this.unidadesConsulta = unidadesConsulta;
	}

	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo unidadesConsulta
	 * 
	 * @return  Retorna la variable unidadesConsulta
	 */
	public List<DtoUnidadesConsulta> getUnidadesConsulta() {
		return unidadesConsulta;
	}

	/**
	 * Método que se encarga de establecer el valor 
	 *  del atributo nombreArchivoGenerado
	 * 
	 * @param  valor para el atributo nombreArchivoGenerado
	 */
	public void setNombreArchivoGenerado(String nombreArchivoGenerado) {
		this.nombreArchivoGenerado = nombreArchivoGenerado;
	}

	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo nombreArchivoGenerado
	 * 
	 * @return  Retorna la variable nombreArchivoGenerado
	 */
	public String getNombreArchivoGenerado() {
		return nombreArchivoGenerado;
	}

	/**
	 * Método que se encarga de establecer el valor 
	 *  del atributo tipoSalida
	 * 
	 * @param  valor para el atributo tipoSalida
	 */
	public void setTipoSalida(String tipoSalida) {
		this.tipoSalida = tipoSalida;
	}

	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo tipoSalida
	 * 
	 * @return  Retorna la variable tipoSalida
	 */
	public String getTipoSalida() {
		return tipoSalida;
	}

	/**
	 * Método que se encarga de establecer el valor 
	 *  del atributo enumTipoSalida
	 * 
	 * @param  valor para el atributo enumTipoSalida
	 */
	public void setEnumTipoSalida(EnumTiposSalida enumTipoSalida) {
		this.enumTipoSalida = enumTipoSalida;
	}

	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo enumTipoSalida
	 * 
	 * @return  Retorna la variable enumTipoSalida
	 */
	public EnumTiposSalida getEnumTipoSalida() {
		return enumTipoSalida;
	}

	/**
	 * Método que se encarga de establecer el valor 
	 *  del atributo ocupacionOdontologo
	 * 
	 * @param  valor para el atributo ocupacionOdontologo
	 */
	public void setOcupacionOdontologo(String ocupacionOdontologo) {
		this.ocupacionOdontologo = ocupacionOdontologo;
	}

	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo ocupacionOdontologo
	 * 
	 * @return  Retorna la variable ocupacionOdontologo
	 */
	public String getOcupacionOdontologo() {
		return ocupacionOdontologo;
	}

	public boolean getAyudanteTiposCita(String valor) {
		boolean respuesta=false;
		for(int i=0; i<this.filtroTiempoEspera.getTiposCita().length; i++)
		{
			if(this.filtroTiempoEspera.getTiposCita()[i].equals(valor))
			{
				respuesta=true;
			}
		}
		return respuesta;
	}
	
	/**
	 * Limpia el indicativo de confirmación para que se dejen deschekear los elementos
	 * @return
	 */
	public boolean getLimpiarAyudanteTiposCita()
	{
		this.filtroTiempoEspera.setTiposCita(new String[0]);
		return true;
	}

	public void setTamanoUnidadesConsulta(int tamanoUnidadesConsulta) {
		this.tamanoUnidadesConsulta = tamanoUnidadesConsulta;
	}

	public int getTamanoUnidadesConsulta() {
		return tamanoUnidadesConsulta;
	}
	
	

}
