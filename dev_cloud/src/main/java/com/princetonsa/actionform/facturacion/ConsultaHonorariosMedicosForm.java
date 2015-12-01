/*
 * Creado el 28/12/2005
 * Jorge Armando Osorio Velasquez
 */
package com.princetonsa.actionform.facturacion;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.InfoDatosStr;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.dto.manejoPaciente.DtoCentrosAtencion;
import com.princetonsa.dto.manejoPaciente.DtoRegionesCobertura;
import com.princetonsa.dto.odontologia.DtoDetalleProgramas;

/**
 * 
 * @author Jorge Armando Osorio Velasquez
 * 
 * CopyRight Princeton S.A.
 * 28/12/2005
 */
public class ConsultaHonorariosMedicosForm extends ValidatorForm
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7745754085443518907L;

	/**
	 * Variable para manejo del workflow.
	 */
	private String estado;
	
	/**
	 * Institucion del usuario;
	 */
	private int institucion;
	
	/**
	 * Pool
	 */
	private int pool;
	
	/**
	 * Codigo del profesional
	 */
	private int profesional;
	
	/**
	 * Fecha inicial para la busqueda
	 */
	private String fechaInicial;
	
	/**
	 * Fecha final para la busqueda
	 */
	private String fechaFinal;
	
	/**
	 * Factura inicial para la busqueda
	 */
	private String facturaInicial;
	
	/**
	 * Factura final para la busqueda.
	 */
	private String facturaFinal;
	
	/**
	 * Mapa que contien los profesionales de la salud.
	 */
	private HashMap mapaProfesionales;
	
	/**
	 * Mapa para cargar los honorarios de los medicos
	 */
	private HashMap mapaHonorarios;
	/**
	 * Indice del profesional a mostrar.
	 */
	private int index;
	
	/**
     * Patron por el que se desea ordenar el listado
     */
    private String patronOrdenar;
    
    /**
     * Ultimo Patron por el que se ordena.
     */
    private String ultimoPatron;
	/**
	 * 
	 */
	private int maxPageItems;
	
	/**
	 * Centro de atención sobre el cual buscar, -1 para Todos
	 */
	private int centroAtencion;	
	
	/**
	 * 
	 */
	private String tipoReporte;
	
	/**
	 * 
	 */
	private String tipoSalida;	
	
	/**
	 * 
	 */
	private HashMap mapaArchivoPlano;
	
	/**
	 * 
	 */
	private ArrayList<HashMap<String, Object>> paises;
	
	/**
	 * 
	 */
	private HashMap instituciones;
	
	/**
	 * 
	 */
	private int pais;
	
	/**
	 * 
	 */
	private String deptoCiudad;
	
	/**
	 * 
	 */
	private int regional;
	
	/**
	 * 
	 */
	private int institucionSel;
	
	/**
	 * 
	 */
	private int especialidad;
	
	/**
	 * 
	 */
	private String consecutivoOrdenMedica;
	
	/**
	 * 
	 */
	private ArrayList<HashMap<String, Object>> ciudades;
	
	/**
	 * 
	 */
	private ArrayList pooles;
	
	/**
	 * 
	 */
	private ArrayList<DtoRegionesCobertura> regiones;
	
	/**
	 * 
	 */
	private ArrayList<DtoCentrosAtencion> centrosAtencion;
	
	/**
	 * 
	 */
	private String region;
	
	/**
	 * 
	 */
	private ArrayList<HashMap<String, Object>>  especialidades;
	
	/**
	 * 
	 */
	private ArrayList<HashMap<String, Object>> profesionales;
	
	
	/*
	 * Atributos Busqueda de programas
	 */

	/**
	 *Atributo para buscar el servicio por codigoPK 
	 */
	private String codigoPrograma;
	
	/**
	 *Atributo para mostrar el nombre del servicio 
	 */
	private String nombrePrograma;
	
	
	/**
	 * Atributos Busqueda de Servicios
	 */
	private String codigoServicio;
	
	
	/**
	 * Utilizar Programas Institucion
	 * Atributor para validar si una institucion utiliza programas o Servicios
	 * 
	 */
	private boolean utilizarProgramasInstitucion;
	
	
	/**
	 * Atributo, que lista todo los Detalles del programa 
	 * Este lista sirver para mostrar los servicios asociados a un programa Odontologico 
	 */
	private ArrayList<DtoDetalleProgramas> listaDetallesPrograma;
	
	/**
	 * Atributo que indica si hay más de un pool parametrizado en el sistema
	 * y por lo tanto se debe mostrar el select en los criterios de búsqueda.
	 */
	private String mostrarPooles;
	
	/**
	 * Atritibuto para Mostrar nombre Servicio 
	 */
	private String nombreServicio; 
	
	
	
	/**
	 * 
	 */
	private boolean esEmpresaInstitucion;
	
	/**
	 * 
	 */
	private String mostrarFacturasAnuladas;
	
	/**
	 * Atributo que almacena el tipo de búsqueda para los servicios
	 * ya sea por cups o por sonria.
	 */
	private String tipoBusquedaServicio; 
	
	/**
	 * Atributo que permite obtener el servicio por medio de
	 *  la b&uacute;squeda gen&eacute;rica. 
	 */
	private InfoDatosStr servicio;
	
	/**
	 * Reset de atributos.
	 */
	public void reset(int institucion)
	{
		this.institucion=ConstantesBD.codigoNuncaValido;
		this.pool=ConstantesBD.codigoNuncaValido;
		this.profesional=ConstantesBD.codigoNuncaValido;
		this.index=ConstantesBD.codigoNuncaValido;
		this.fechaInicial="";
		this.fechaFinal="";
		this.facturaInicial="";
		this.facturaFinal="";
		this.mapaProfesionales=new HashMap();
		this.mapaHonorarios=new HashMap();
        this.mapaProfesionales.put("numRegistros", "0");
        this.mapaHonorarios.put("numRegistros", "0");
		this.maxPageItems=20;
		this.patronOrdenar="";
        this.ultimoPatron="";
        this.centroAtencion=-1;
        this.tipoReporte="";
        this.tipoSalida="";
        this.mapaArchivoPlano=new HashMap();
        this.paises = new ArrayList<HashMap<String,Object>>();
        this.instituciones=new HashMap();
        this.consecutivoOrdenMedica="";
        this.pais=ConstantesBD.codigoNuncaValido;
        this.deptoCiudad="";
        this.regional=ConstantesBD.codigoNuncaValido;
        this.institucionSel=ConstantesBD.codigoNuncaValido;
        this.especialidad=ConstantesBD.codigoNuncaValido;
        this.ciudades=new ArrayList<HashMap<String,Object>>();
        this.pooles=new ArrayList();
        this.regiones= new ArrayList<DtoRegionesCobertura>();
        this.centrosAtencion = new ArrayList<DtoCentrosAtencion>();
        this.region="";
        this.especialidades = new ArrayList<HashMap<String,Object>>();
        this.profesionales = new ArrayList<HashMap<String,Object>>();
        this.codigoPrograma="";
        this.nombrePrograma="";
        this.codigoServicio="";
        this.nombreServicio="";
        this.institucion= institucion;
        this.esEmpresaInstitucion= UtilidadTexto.getBoolean(ValoresPorDefecto.getInstitucionMultiempresa(institucion));
        this.mostrarFacturasAnuladas=ConstantesBD.acronimoNo;
        this.utilizarProgramasInstitucion=Boolean.FALSE;
        this.listaDetallesPrograma=new ArrayList<DtoDetalleProgramas>();
        this.mostrarPooles= "true";
        this.tipoBusquedaServicio = "";
        this.servicio = new InfoDatosStr();
	}
	/**
	 * 
	 * Validaciones
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores = new ActionErrors();
		if(estado.equals("buscar"))
		{
			if(this.fechaInicial.trim().equals("")&&!this.fechaFinal.trim().equals(""))
				
				this.fechaInicial=this.fechaFinal;
			
			else if(this.fechaFinal.trim().equals("")&&!this.fechaInicial.trim().equals(""))
				
				this.fechaFinal=this.fechaInicial;
			
			if(this.facturaInicial.trim().equals("")&&!this.facturaFinal.trim().equals(""))
				
				this.facturaInicial=this.facturaFinal;
			
			else if(this.facturaFinal.trim().equals("")&&!this.facturaInicial.trim().equals(""))
				
				this.facturaFinal=this.facturaInicial;

			if(this.fechaInicial.trim().equals("")&&this.facturaInicial.trim().equals(""))
				
				errores.add("requerido fechas o facturas",new ActionMessage("error.facutraion.consultaHonorariosMedicos.fechaOFacturasRequeridos"));
			
			if(this.pais == ConstantesBD.codigoNuncaValido)
				
				errores.add("Requerido Tipo Reporte",new ActionMessage("errors.required", "Pais"));
			
			if(this.deptoCiudad.equals(""))
				
				errores.add("Requerido Tipo Reporte",new ActionMessage("errors.required", "Ciudad"));
			
			if(this.tipoReporte.equals(ConstantesBD.codigoNuncaValido+""))
				
				errores.add("Requerido Tipo Reporte",new ActionMessage("errors.required", "Tipo Reporte"));
			
			if(this.tipoSalida.equals(ConstantesBD.codigoNuncaValido+""))
			
				errores.add("Requerido Tipo Salida",new ActionMessage("errors.required", "Tipo Salida"));
			
			if(!this.fechaInicial.trim().equals(""))
			{
				boolean errorFecha=false;
				if(!UtilidadFecha.validarFecha(this.fechaInicial))
				{
					errores.add("fecha de Inicial", new ActionMessage("errors.formatoFechaInvalido",this.fechaInicial));
					errorFecha=true;
				}
				if(!UtilidadFecha.validarFecha(this.fechaFinal))
				{
					errores.add("fecha de Fianl", new ActionMessage("errors.formatoFechaInvalido",this.fechaFinal));
					errorFecha=true;
				}
				if(!errorFecha)
				{
					if((UtilidadFecha.conversionFormatoFechaABD(this.fechaInicial)).compareTo(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()))>0)
					{
						errores.add("fecha de Inicial", new ActionMessage("errors.fechaPosteriorAOtraDeReferencia", "Inicial", "Actual"));
					}
					if((UtilidadFecha.conversionFormatoFechaABD(this.fechaFinal)).compareTo(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()))>0)
					{
						errores.add("fecha de Final", new ActionMessage("errors.fechaPosteriorAOtraDeReferencia", "Final", "Actual"));
					}
					if((UtilidadFecha.conversionFormatoFechaABD(this.fechaInicial)).compareTo(UtilidadFecha.conversionFormatoFechaABD(fechaFinal))>0)
					{
						errores.add("fecha de Inicial-Final", new ActionMessage("errors.fechaPosteriorAOtraDeReferencia", "Inicial", "Final"));
					}				
				}
			}
			if(!this.facturaInicial.trim().equals(""))
			{
				if(Integer.parseInt(this.facturaInicial)>Integer.parseInt(this.facturaFinal))
				{
					errores.add("FacturaInicial>FacturaFinal", new ActionMessage("errors.integerMenorIgualQue", "Factura Inicial", "Factura Final"));
				}
			}
			
			
			
			if(!UtilidadTexto.isEmpty(this.facturaInicial) )
			{
				if(Utilidades.convertirADouble(this.facturaInicial)<=0)
				{
					errores.add("", new ActionMessage("errors.notEspecific", "Factura Inicial, deber ser Mayor que cero "));
				}
			}
			
			
			
			if(!UtilidadTexto.isEmpty(this.facturaFinal) )
			{
				if(Utilidades.convertirADouble(this.facturaFinal)<=0)
				{
					errores.add("", new ActionMessage("errors.notEspecific", "Factura Final, deber ser Mayor que cero "));
				}
			}
			
			
			
			
			
		}
		return errores;
	}
	/**
	 * @return Retorna estado.
	 */
	public String getEstado()
	{
		return estado;
	}
	/**
	 * @param estado Asigna estado.
	 */
	public void setEstado(String estado)
	{
		this.estado = estado;
	}
	/**
	 * @return Retorna institucion.
	 */
	public int getInstitucion()
	{
		return institucion;
	}
	/**
	 * @param institucion Asigna institucion.
	 */
	public void setInstitucion(int institucion)
	{
		this.institucion = institucion;
	}
	/**
	 * @return Retorna facturaFinal.
	 */
	public String getFacturaFinal()
	{
		return facturaFinal;
	}
	/**
	 * @param facturaFinal Asigna facturaFinal.
	 */
	public void setFacturaFinal(String facturaFinal)
	{
		this.facturaFinal = facturaFinal;
	}
	/**
	 * @return Retorna facturaInicial.
	 */
	public String getFacturaInicial()
	{
		return facturaInicial;
	}
	/**
	 * @param facturaInicial Asigna facturaInicial.
	 */
	public void setFacturaInicial(String facturaInicial)
	{
		this.facturaInicial = facturaInicial;
	}
	/**
	 * @return Retorna fechaFinal.
	 */
	public String getFechaFinal()
	{
		return fechaFinal;
	}
	/**
	 * @param fechaFinal Asigna fechaFinal.
	 */
	public void setFechaFinal(String fechaFinal)
	{
		this.fechaFinal = fechaFinal;
	}
	/**
	 * @return Retorna fechaInicial.
	 */
	public String getFechaInicial()
	{
		return fechaInicial;
	}
	/**
	 * @param fechaInicial Asigna fechaInicial.
	 */
	public void setFechaInicial(String fechaInicial)
	{
		this.fechaInicial = fechaInicial;
	}
	/**
	 * @return Retorna pool.
	 */
	public int getPool()
	{
		return pool;
	}
	/**
	 * @param pool Asigna pool.
	 */
	public void setPool(int pool)
	{
		this.pool = pool;
	}
	/**
	 * @return Retorna profesional.
	 */
	public int getProfesional()
	{
		return profesional;
	}
	/**
	 * @param profesional Asigna profesional.
	 */
	public void setProfesional(int profesional)
	{
		this.profesional = profesional;
	}
	/**
	 * @return Retorna mapaProfesionales.
	 */
	public HashMap getMapaProfesionales()
	{
		return mapaProfesionales;
	}
	/**
	 * @param mapaProfesionales Asigna mapaProfesionales.
	 */
	public void setMapaProfesionales(HashMap mapaProfesionales)
	{
		this.mapaProfesionales = mapaProfesionales;
	}
	/**
	 * @return Retorna mapaProfesionales.
	 */
	public Object getMapaProfesionales(String key)
	{
		return mapaProfesionales.get(key);
	}
	/**
	 * @param mapaProfesionales Asigna mapaProfesionales.
	 */
	public void setMapaProfesionales(String key,Object value)
	{
		this.mapaProfesionales.put(key,value);
	}
	/**
	 * @return Retorna index.
	 */
	public int getIndex()
	{
		return index;
	}
	/**
	 * @param index Asigna index.
	 */
	public void setIndex(int index)
	{
		this.index = index;
	}
	/**
	 * @return Retorna mapaHonorarios.
	 */
	public HashMap getMapaHonorarios()
	{
		return mapaHonorarios;
	}
	/**
	 * @param mapaHonorarios Asigna mapaHonorarios.
	 */
	public void setMapaHonorarios(HashMap mapaHonorarios)
	{
		this.mapaHonorarios = mapaHonorarios;
	}
	/**
	 * @return Retorna mapaHonorarios.
	 */
	public Object getMapaHonorarios(String key)
	{
		return mapaHonorarios.get(key);
	}
	/**
	 * @param mapaHonorarios Asigna mapaHonorarios.
	 */
	public void setMapaHonorarios(String key,Object value)
	{
		this.mapaHonorarios.put(key,value);
	}
	/**
	 * @return Retorna maxPageItems.
	 */
	public int getMaxPageItems()
	{
		return maxPageItems;
	}
	/**
	 * @param maxPageItems Asigna maxPageItems.
	 */
	public void setMaxPageItems(int maxPageItems)
	{
		this.maxPageItems = maxPageItems;
	}
	/**
	 * @return Retorna patronOrdenar.
	 */
	public String getPatronOrdenar()
	{
		return patronOrdenar;
	}
	/**
	 * @param patronOrdenar Asigna patronOrdenar.
	 */
	public void setPatronOrdenar(String patronOrdenar)
	{
		this.patronOrdenar = patronOrdenar;
	}
	/**
	 * @return Retorna ultimoPatron.
	 */
	public String getUltimoPatron()
	{
		return ultimoPatron;
	}
	/**
	 * @param ultimoPatron Asigna ultimoPatron.
	 */
	public void setUltimoPatron(String ultimoPatron)
	{
		this.ultimoPatron = ultimoPatron;
	}
	/**
	 * @return Returns the centroAtencion.
	 */
	public int getCentroAtencion() 
	{
		return centroAtencion;
	}
	/**
	 * @param centroAtencion The centroAtencion to set.
	 */
	public void setCentroAtencion(int centroAtencion) 
	{
		this.centroAtencion = centroAtencion;
	}
	/**
	 * @return the tipoReporte
	 */
	public String getTipoReporte() {
		return tipoReporte;
	}
	/**
	 * @param tipoReporte the tipoReporte to set
	 */
	public void setTipoReporte(String tipoReporte) {
		this.tipoReporte = tipoReporte;
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
	 * @return the mapaArchivoPlano
	 */
	public HashMap getMapaArchivoPlano() {
		return mapaArchivoPlano;
	}
	/**
	 * @param mapaArchivoPlano the mapaArchivoPlano to set
	 */
	public void setMapaArchivoPlano(HashMap mapaArchivoPlano) {
		this.mapaArchivoPlano = mapaArchivoPlano;
	}
	
	/**
	 * @param mapaArchivoPlano the mapaArchivoPlano to set
	 */
	public void setMapaArchivoPlano(String llave, Object obj) {
		this.mapaArchivoPlano.put(llave, obj);
	}
	
	/**
	 * @return the paises
	 */
	public ArrayList<HashMap<String, Object>> getPaises() {
		return paises;
	}
	/**
	 * @return the instituciones
	 */
	public HashMap getInstituciones() {
		return instituciones;
	}
	/**
	 * @param instituciones the instituciones to set
	 */
	public void setInstituciones(HashMap instituciones) {
		this.instituciones = instituciones;
	}
	/**
	 * @return the consecutivoOrdenMedica
	 */
	public String getConsecutivoOrdenMedica() {
		return consecutivoOrdenMedica;
	}
	/**
	 * @param consecutivoOrdenMedica the consecutivoOrdenMedica to set
	 */
	public void setConsecutivoOrdenMedica(String consecutivoOrdenMedica) {
		this.consecutivoOrdenMedica = consecutivoOrdenMedica;
	}
	/**
	 * @return the pais
	 */
	public int getPais() {
		return pais;
	}
	/**
	 * @param pais the pais to set
	 */
	public void setPais(int pais) {
		this.pais = pais;
	}
	/**
	 * @return the ciudad
	 */
	public String getDeptoCiudad() {
		return this.deptoCiudad;
	}
	/**
	 * @param ciudad the ciudad to set
	 */
	public void setDeptoCiudad(String deptoCiudad) {
		this.deptoCiudad = deptoCiudad;
	}
	/**
	 * @return the regional
	 */
	public int getRegional() {
		return regional;
	}
	/**
	 * @param regional the regional to set
	 */
	public void setRegional(int regional) {
		this.regional = regional;
	}
	/**
	 * @return the institucionSel
	 */
	public int getInstitucionSel() {
		return institucionSel;
	}
	/**
	 * @param institucionSel the institucionSel to set
	 */
	public void setInstitucionSel(int institucionSel) {
		this.institucionSel = institucionSel;
	}
	/**
	 * @return the especialidad
	 */
	public int getEspecialidad() {
		return especialidad;
	}
	/**
	 * @param especialidad the especialidad to set
	 */
	public void setEspecialidad(int especialidad) {
		this.especialidad = especialidad;
	}
	/**
	 * @param paises the paises to set
	 */
	public void setPaises(ArrayList<HashMap<String, Object>> paises) {
		this.paises = paises;
	}
	/**
	 * @return the ciudades
	 */
	public ArrayList<HashMap<String, Object>> getCiudades() {
		return ciudades;
	}
	/**
	 * @param ciudades the ciudades to set
	 */
	public void setCiudades(ArrayList<HashMap<String, Object>> ciudades) {
		this.ciudades = ciudades;
	}
	/**
	 * @return the pooles
	 */
	public ArrayList getPooles() {
		return pooles;
	}
	/**
	 * @param pooles the pooles to set
	 */
	public void setPooles(ArrayList pooles) {
		this.pooles = pooles;
	}
	/**
	 * @return the regiones
	 */
	public ArrayList<DtoRegionesCobertura> getRegiones() {
		return regiones;
	}
	/**
	 * @param regiones the regiones to set
	 */
	public void setRegiones(ArrayList<DtoRegionesCobertura> regiones) {
		this.regiones = regiones;
	}
	/**
	 * @return the region
	 */
	public String getRegion() {
		return region;
	}
	/**
	 * @param region the region to set
	 */
	public void setRegion(String region) {
		this.region = region;
	}
	/**
	 * @return the centrosAtencion
	 */
	public ArrayList<DtoCentrosAtencion> getCentrosAtencion() {
		return centrosAtencion;
	}
	/**
	 * @param centrosAtencion the centrosAtencion to set
	 */
	public void setCentrosAtencion(ArrayList<DtoCentrosAtencion> centrosAtencion) {
		this.centrosAtencion = centrosAtencion;
	}
	
	/**
	 * @return the profesionales
	 */
	public ArrayList<HashMap<String, Object>> getProfesionales() {
		return profesionales;
	}
	/**
	 * @param profesionales the profesionales to set
	 */
	public void setProfesionales(ArrayList<HashMap<String, Object>> profesionales) {
		this.profesionales = profesionales;
	}
	/**
	 * @return the codigoPrograma
	 */
	public String getCodigoPrograma() {
		return codigoPrograma;
	}
	/**
	 * @param codigoPrograma the codigoPrograma to set
	 */
	public void setCodigoPrograma(String codigoPrograma) {
		this.codigoPrograma = codigoPrograma;
	}
	/**
	 * @return the nombrePrograma
	 */
	public String getNombrePrograma() {
		return nombrePrograma;
	}
	/**
	 * @param nombrePrograma the nombrePrograma to set
	 */
	public void setNombrePrograma(String nombrePrograma) {
		this.nombrePrograma = nombrePrograma;
	}
	/**
	 * @return the esEmpresaInstitucion
	 */
	public boolean isEsEmpresaInstitucion() {
		return esEmpresaInstitucion;
	}
	/**
	 * @param esEmpresaInstitucion the esEmpresaInstitucion to set
	 */
	public void setEsEmpresaInstitucion(boolean esEmpresaInstitucion) {
		this.esEmpresaInstitucion = esEmpresaInstitucion;
	}
	/**
	 * @return the mostrarFacturasAnuladas
	 */
	public String getMostrarFacturasAnuladas() {
		return mostrarFacturasAnuladas;
	}
	/**
	 * @param mostrarFacturasAnuladas the mostrarFacturasAnuladas to set
	 */
	public void setMostrarFacturasAnuladas(String mostrarFacturasAnuladas) {
		this.mostrarFacturasAnuladas = mostrarFacturasAnuladas;
	}
	/**
	 * @return the especialidades
	 */
	public ArrayList<HashMap<String, Object>> getEspecialidades() {
		return especialidades;
	}
	/**
	 * @param especialidades the especialidades to set
	 */
	public void setEspecialidades(ArrayList<HashMap<String, Object>> especialidades) {
		this.especialidades = especialidades;
	}
	public void setUtilizarProgramasInstitucion(boolean utilizarProgramasInstitucion) {
		this.utilizarProgramasInstitucion = utilizarProgramasInstitucion;
	}
	public boolean isUtilizarProgramasInstitucion() {
		return utilizarProgramasInstitucion;
	}
	public void setListaDetallesPrograma(ArrayList<DtoDetalleProgramas> listaDetallesPrograma) {
		this.listaDetallesPrograma = listaDetallesPrograma;
	}
	public ArrayList<DtoDetalleProgramas> getListaDetallesPrograma() {
		return listaDetallesPrograma;
	}
	/**
	 * Método que se encarga de obtener el 
	 * valor del atributo  mostrarPooles
	 *
	 * @return retorna la variable mostrarPooles
	 */
	public String getMostrarPooles() {
		return mostrarPooles;
	}
	/**
	 * Método que se encarga de establecer el valor
	 * del atributo mostrarPooles
	 * @param mostrarPooles es el valor para el atributo mostrarPooles 
	 */
	public void setMostrarPooles(String mostrarPooles) {
		this.mostrarPooles = mostrarPooles;
	}
	
	/**
	 * @return the codigoServicio
	 */
	public String getCodigoServicio() {
		return codigoServicio;
	}
	/**
	 * @return the nombreServicio
	 */
	public String getNombreServicio() {
		return nombreServicio;
	}
	/**
	 * @param codigoServicio the codigoServicio to set
	 */
	public void setCodigoServicio(String codigoServicio) {
		this.codigoServicio = codigoServicio;
	}
	/**
	 * @param nombreServicio the nombreServicio to set
	 */
	public void setNombreServicio(String nombreServicio) {
		this.nombreServicio = nombreServicio;
	}
	
	/**
	 * Método que se encarga de obtener el 
	 * valor del atributo  tipoBusquedaServicio
	 *
	 * @return retorna la variable tipoBusquedaServicio
	 */
	public String getTipoBusquedaServicio() {
		return tipoBusquedaServicio;
	}
	/**
	 * Método que se encarga de establecer el valor
	 * del atributo tipoBusquedaServicio
	 * @param tipoBusquedaServicio es el valor para el atributo tipoBusquedaServicio 
	 */
	public void setTipoBusquedaServicio(String tipoBusquedaServicio) {
		this.tipoBusquedaServicio = tipoBusquedaServicio;
	}
	/**
	 * Método que se encarga de obtener el 
	 * valor del atributo  servicio
	 *
	 * @return retorna la variable servicio
	 */
	public InfoDatosStr getServicio() {
		return servicio;
	}
	/**
	 * Método que se encarga de establecer el valor
	 * del atributo servicio
	 * @param servicio es el valor para el atributo servicio 
	 */
	public void setServicio(InfoDatosStr servicio) {
		this.servicio = servicio;
	}
}
