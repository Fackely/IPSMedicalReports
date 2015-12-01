/*
 * Ago 20, 2008
 */
package com.princetonsa.actionform.manejoPaciente;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import com.princetonsa.mundo.atencion.Diagnostico;

import util.ConstantesBD;
import util.InfoDatosInt;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.manejoPaciente.ConstantesBDManejoPaciente;


/**
 * @author Sebastián Gómez 
 *
 * Clase que genera el reporte de mortalidad  
 */
public class ReporteMortalidadForm extends ValidatorForm 
{
	/**
	 * Variable usada para almacenar el estado del controlador
	 */
	private String estado;
	
	/**
	 * Variable que contiene el tipo de reporte
	 */
	private int tipoReporte;
	
	/**
	 * Atributo usado como variable temporal en los filtros AJAX
	 */
	private String index;
	
	//*********ARREGLOS*******************************************************************
	private ArrayList<HashMap<String, Object>> tiposReporte = new ArrayList<HashMap<String,Object>>();
	private ArrayList viasIngreso = new ArrayList();
	private ArrayList<HashMap<String, Object>> sexos = new ArrayList<HashMap<String,Object>>();
	private ArrayList arregloConvenios = new ArrayList();
	private ArrayList<HashMap<String, Object>> arregloCentrosCosto = new ArrayList<HashMap<String,Object>>();
	private HashMap mapaCentrosCosto = new HashMap();
	private ArrayList<HashMap<String, Object>> centrosAtencion = new ArrayList<HashMap<String,Object>>();
	//************************************************************************************
	
	//**********PARÁMETROS DE LA BUSQUEDA******************************
	private InfoDatosInt centroAtencion;
	private String fechaInicial;
	private String fechaFinal;
	private InfoDatosInt viaIngreso;
	private InfoDatosInt sexo;
	private HashMap<String, Object> diagnosticosMuerte = new HashMap<String, Object>();
	private String diagSeleccionados;
	private HashMap<String, Object> convenios = new HashMap<String, Object>();
	private Diagnostico diagnosticoEgreso = new Diagnostico();
	private String edad;
	private String estancia;
	private HashMap<String, Object> centrosCosto = new HashMap<String, Object>();
	private String tipoSalida;
	private boolean mostrarTipoSalidaArchivo;
	//*********************************************************************
	
	
	//Atributos para abrir el archivo plano
	private String pathArchivoPlano;
	private String urlArchivoPlano;
	
	/**
	 * reset de los datos de la forma
	 *
	 */
	public void reset()
	{
		this.estado="";
		this.tipoReporte = ConstantesBD.codigoNuncaValido;
		this.tiposReporte = new ArrayList<HashMap<String,Object>>();
		this.viasIngreso = new ArrayList();
		this.sexos = new ArrayList<HashMap<String,Object>>();
		this.arregloConvenios = new ArrayList();
		this.mapaCentrosCosto = new HashMap();
		this.centrosAtencion = new ArrayList<HashMap<String,Object>>();
		this.index = "";
		
		this.resetParametros();
	}
	
	

	/**
	 * Reset de los parámetros de la busqueda de cada tipo de reporte
	 */
	public void resetParametros()
	{
		this.centroAtencion = new InfoDatosInt(ConstantesBD.codigoNuncaValido,"");
		this.fechaInicial = "";
		this.fechaFinal = "";
		this.viaIngreso = new InfoDatosInt(ConstantesBD.codigoNuncaValido,"");
		this.sexo = new InfoDatosInt(ConstantesBD.codigoNuncaValido,"");
		this.diagnosticosMuerte = new HashMap<String, Object>();
		this.diagSeleccionados = "";
		this.convenios = new HashMap<String, Object>();
		this.diagnosticoEgreso = new Diagnostico("",ConstantesBD.codigoNuncaValido);
		this.edad = "";
		this.estancia = "";
		this.tipoSalida = "";
		this.mostrarTipoSalidaArchivo = true;
		this.centrosCosto = new HashMap<String, Object>();
		this.arregloCentrosCosto = new ArrayList<HashMap<String,Object>>();
		
		this.pathArchivoPlano = "";
		this.urlArchivoPlano = "";
	}
	
	



	/**
	 * @return the diagSeleccionados
	 */
	public String getDiagSeleccionados() {
		return diagSeleccionados;
	}



	/**
	 * @param diagSeleccionados the diagSeleccionados to set
	 */
	public void setDiagSeleccionados(String diagSeleccionados) {
		this.diagSeleccionados = diagSeleccionados;
	}



	/**
	 * @return the tiposReporte
	 */
	public ArrayList<HashMap<String, Object>> getTiposReporte() {
		return tiposReporte;
	}

	/**
	 * @param tiposReporte the tiposReporte to set
	 */
	public void setTiposReporte(ArrayList<HashMap<String, Object>> tiposReporte) {
		this.tiposReporte = tiposReporte;
	}

	/**
	 * Validate the properties that have been set from this HTTP request, and
	 * return an <code>ActionErrors</code> object that encapsulates any
	 * validation errors that have been found.  If no errors are found, return
	 * <code>null</code> or an <code>ActionErrors</code> object with no recorded
	 * error messages.
	 *
	 * @param mapping The mapping used to select this instance
	 * @param request The servlet request we are processing
	*/
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores = new ActionErrors();
		
		if(this.estado.equals("imprimir"))
		{
			///Validación del tipo salida
			if(this.tipoSalida.equals(""))
				errores.add("", new ActionMessage("errors.required","El tipo de salida"));
			
			//*****VALIDACION DE LAS FECHAS******************************
			boolean fechaInicialValida = false, fechaFinalValida = false;
			if(this.fechaInicial.equals(""))
				errores.add("",new ActionMessage("errors.required","La fecha inicial"));
			else if(!UtilidadFecha.validarFecha(this.fechaInicial))
				errores.add("", new ActionMessage("errors.formatoFechaInvalido","inicial"));
			else
				fechaInicialValida = true;
			
			if(this.fechaFinal.equals(""))
				errores.add("",new ActionMessage("errors.required","La fecha final"));
			else if(!UtilidadFecha.validarFecha(this.fechaFinal))
				errores.add("", new ActionMessage("errors.formatoFechaInvalido","final"));
			else
			{
				fechaFinalValida = true;
				String fechaSistema =  UtilidadFecha.getFechaActual();
				if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.fechaFinal,fechaSistema ))
					errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual","final","del sistema: "+fechaSistema));
			}
			
			if(fechaFinalValida&&fechaInicialValida&&!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.fechaInicial, this.fechaFinal))
				errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual","inicial","final"));
			//*************************************************************
			
			
			
			switch(this.tipoReporte)
			{
				case ConstantesBDManejoPaciente.tipoReporteNumeroPacientesFallecidosDxMuerte:
				case ConstantesBDManejoPaciente.tipoReporteMortalidadDxMuerteCentroCosto:
					//Se verifica que se haya elegido como mínimo un solo diagnostico de muerte
					if(this.getNumDiagMuerteSeleccionados()<=0)
						errores.add("",new ActionMessage("errors.minimoCampos","la selección de 1 diagnóstico de muerte","impresión del reporte"));
				break;
				case ConstantesBDManejoPaciente.tipoReporteMortalidadMensualConvenio:
					//Se verifica que se haya elegido como mínimo un solo convenio
					if(this.getNumConveniosSeleccionados()<=0)
						errores.add("",new ActionMessage("errors.minimoCampos","la selección de 1 convenio","impresión del reporte"));
					
					if(!this.edad.equals(""))
					{
						if(Utilidades.convertirAEntero(this.edad)==ConstantesBD.codigoNuncaValido)
							errores.add("", new ActionMessage("errors.integer","La edad"));
						else if(Utilidades.convertirAEntero(this.edad)<0||Utilidades.convertirAEntero(this.edad)>120)
							errores.add("", new ActionMessage("errors.range","La edad","0","120 años"));
						
					}
				break;
				case ConstantesBDManejoPaciente.tipoReporteMortalidadRangoTiempos:
					if(this.getCodigoViaIngreso()==ConstantesBD.codigoNuncaValido)
						errores.add("", new ActionMessage("errors.required","La vía de ingreso"));
					if(!this.edad.equals(""))
					{
						if(Utilidades.convertirAEntero(this.edad)==ConstantesBD.codigoNuncaValido)
							errores.add("", new ActionMessage("errors.integer","La edad"));
						else if(Utilidades.convertirAEntero(this.edad)<0||Utilidades.convertirAEntero(this.edad)>120)
							errores.add("", new ActionMessage("errors.range","La edad","0","120 años"));
						
					}
				break;
				case ConstantesBDManejoPaciente.tipoReporteEstanciaPromPacFallecidosRangoEdad:
					if(this.getCodigoViaIngreso()==ConstantesBD.codigoNuncaValido)
						errores.add("", new ActionMessage("errors.required","La vía de ingreso"));
					if(!this.estancia.equals("")&&Utilidades.convertirAEntero(this.estancia)==ConstantesBD.codigoNuncaValido)
						errores.add("", new ActionMessage("errors.integer","La estancia"));
				break;
				case ConstantesBDManejoPaciente.tipoReporteMortalidadRangoTiempoCentroCosto:
					if(this.getNumCentrosCostoSeleccionados()<=0)
						errores.add("",new ActionMessage("errors.minimoCampos","la selección de 1 centro de costo","impresión del reporte"));
				break;
				case ConstantesBDManejoPaciente.tipoReporteListadoPacientesFallecidos:
					if(this.getCodigoViaIngreso()==ConstantesBD.codigoNuncaValido)
						errores.add("", new ActionMessage("errors.required","La vía de ingreso"));
					if(!this.edad.equals(""))
					{
						if(Utilidades.convertirAEntero(this.edad)==ConstantesBD.codigoNuncaValido)
							errores.add("", new ActionMessage("errors.integer","La edad"));
						else if(Utilidades.convertirAEntero(this.edad)<0||Utilidades.convertirAEntero(this.edad)>120)
							errores.add("", new ActionMessage("errors.range","La edad","0","120 años"));
						
					}
					if(!this.estancia.equals("")&&Utilidades.convertirAEntero(this.estancia)==ConstantesBD.codigoNuncaValido)
						errores.add("", new ActionMessage("errors.integer","La estancia"));
				break;
				case ConstantesBDManejoPaciente.tipoReporteMortalidadSexo:
				case ConstantesBDManejoPaciente.tipoReporteMortalidadGlobal:
					if(!this.edad.equals(""))
					{
						if(Utilidades.convertirAEntero(this.edad)==ConstantesBD.codigoNuncaValido)
							errores.add("", new ActionMessage("errors.integer","La edad"));
						else if(Utilidades.convertirAEntero(this.edad)<0||Utilidades.convertirAEntero(this.edad)>120)
							errores.add("", new ActionMessage("errors.range","La edad","0","120 años"));
						
					}
				break;
				
			}
		}
		
		return errores;
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
	 * @return the tipoReporte
	 */
	public int getTipoReporte() {
		return tipoReporte;
	}

	/**
	 * @param tipoReporte the tipoReporte to set
	 */
	public void setTipoReporte(int tipoReporte) {
		this.tipoReporte = tipoReporte;
	}

	/**
	 * @return the centroAtencion
	 */
	public InfoDatosInt getCentroAtencion() {
		return centroAtencion;
	}

	/**
	 * @param centroAtencion the centroAtencion to set
	 */
	public void setCentroAtencion(InfoDatosInt centroAtencion) {
		this.centroAtencion = centroAtencion;
	}
	
	
	/**
	 * @return the centroAtencion
	 */
	public int getCodigoCentroAtencion() {
		return centroAtencion.getCodigo();
	}

	/**
	 * @param centroAtencion the centroAtencion to set
	 */
	public void setCodigoCentroAtencion(int centroAtencion) {
		this.centroAtencion.setCodigo(centroAtencion);
	}
	
	/**
	 * @return the centroAtencion
	 */
	public String getNombreCentroAtencion() {
		if(centroAtencion.getNombre().equals(""))
		{
			for(HashMap elemento:this.centrosAtencion)
				if(Integer.parseInt(elemento.get("consecutivo").toString())==this.getCodigoCentroAtencion())
					centroAtencion.setNombre(elemento.get("descripcion").toString());
		}
		return centroAtencion.getNombre();
	}

	/**
	 * @param centroAtencion the centroAtencion to set
	 */
	public void setNombreCentroAtencion(String centroAtencion) {
		this.centroAtencion.setNombre(centroAtencion);
	}
	

	/**
	 * @return the viaIngreso
	 */
	public InfoDatosInt getViaIngreso() {
		return viaIngreso;
	}

	/**
	 * @param viaIngreso the viaIngreso to set
	 */
	public void setViaIngreso(InfoDatosInt viaIngreso) {
		this.viaIngreso = viaIngreso;
	}
	
	/**
	 * @return the viaIngreso
	 */
	public int getCodigoViaIngreso() {
		return viaIngreso.getCodigo();
	}

	/**
	 * @param viaIngreso the viaIngreso to set
	 */
	public void setCodigoViaIngreso(int viaIngreso) {
		this.viaIngreso.setCodigo(viaIngreso);
	}
	
	/**
	 * @return the viaIngreso
	 */
	public String getNombreViaIngreso() 
	{
		if(viaIngreso.getNombre().equals(""))
		{
			for(Object objeto:this.viasIngreso)
			{
				HashMap elemento = (HashMap)objeto;
				if(Integer.parseInt(elemento.get("codigo").toString())==this.getCodigoViaIngreso())
					this.viaIngreso.setNombre(elemento.get("nombre").toString());
			}
			if(viaIngreso.getNombre().equals(""))
				this.viaIngreso.setNombre("Todas");
		}
		
		return viaIngreso.getNombre();
	}

	/**
	 * @param viaIngreso the viaIngreso to set
	 */
	public void setNombreViaIngreso(String viaIngreso) {
		this.viaIngreso.setNombre(viaIngreso);
	}
	
	

	/**
	 * @return the sexo
	 */
	public InfoDatosInt getSexo() {
		return sexo;
	}

	/**
	 * @param sexo the sexo to set
	 */
	public void setSexo(InfoDatosInt sexo) {
		this.sexo = sexo;
	}
	
	/**
	 * @return the sexo
	 */
	public int getCodigoSexo() {
		return sexo.getCodigo();
	}

	/**
	 * @param sexo the sexo to set
	 */
	public void setCodigoSexo(int sexo) {
		this.sexo.setCodigo(sexo);
	}
	
	/**
	 * @return the sexo
	 */
	public String getNombreSexo() 
	{
		if(sexo.getNombre().equals(""))
		{
			for(HashMap elemento:this.sexos)
				if(Integer.parseInt(elemento.get("codigo").toString())==this.getCodigoSexo())
					this.sexo.setNombre(elemento.get("nombre").toString());
			
			if(sexo.getNombre().equals(""))
				sexo.setNombre("Ambos");
		}
		return sexo.getNombre();
	}

	/**
	 * @param sexo the sexo to set
	 */
	public void setNombreSexo(String sexo) {
		this.sexo.setNombre(sexo);
	}

	/**
	 * @return the diagnosticosMuerte
	 */
	public HashMap<String, Object> getDiagnosticosMuerte() {
		return diagnosticosMuerte;
	}

	/**
	 * @param diagnosticosMuerte the diagnosticosMuerte to set
	 */
	public void setDiagnosticosMuerte(HashMap<String, Object> diagnosticosMuerte) {
		this.diagnosticosMuerte = diagnosticosMuerte;
	}
	
	/**
	 * @return the diagnosticosMuerte
	 */
	public Object getDiagnosticosMuerte(String key) {
		return diagnosticosMuerte.get(key);
	}

	/**
	 * @param diagnosticosMuerte the diagnosticosMuerte to set
	 */
	public void setDiagnosticosMuerte(String key,Object obj) {
		this.diagnosticosMuerte.put(key, obj);
	}
	
	/**
	 * Método que retorna el número de diagnósticos de muerte
	 * @return
	 */
	public int getNumDiagMuerte()
	{
		return Utilidades.convertirAEntero(this.getDiagnosticosMuerte("numRegistros")+"", true);
	}
	
	/**
	 * Método que retorna el número de diagnósticos de muerte seleccionados
	 * @return
	 */
	private int getNumDiagMuerteSeleccionados()
	{
		int numDiag = 0;
		for(int i=0;i<Utilidades.convertirAEntero(this.getDiagnosticosMuerte("numRegistros")+"");i++)
			if(UtilidadTexto.getBoolean(this.getDiagnosticosMuerte("checkbox_"+i)+""))
				numDiag++;
		return numDiag;
	}
	
	/**
	 * Método que asigna el número de diagnosticos de muerte del mapa
	 * @param numRegistros
	 */
	public void setNumDiagMuerte(int numRegistros)
	{
		this.setDiagnosticosMuerte("numRegistros", numRegistros);
	}

	/**
	 * @return the convenios
	 */
	public HashMap<String, Object> getConvenios() {
		return convenios;
	}

	/**
	 * @param convenios the convenios to set
	 */
	public void setConvenios(HashMap<String, Object> convenios) {
		this.convenios = convenios;
	}
	
	/**
	 * @return the convenios
	 */
	public Object getConvenios(String key) {
		return convenios.get(key);
	}

	/**
	 * @param convenios the convenios to set
	 */
	public void setConvenios(String key,Object value) {
		this.convenios.put(key, value);
	}
	
	/**
	 * Método para obtener el nombre del convenio
	 * @return
	 */
	public String getNombreConvenio()
	{
		int codigoConvenio = Utilidades.convertirAEntero(this.convenios.get("codigo")+"");
		String valor = "";
		
		for(Object elemento:this.arregloConvenios)
		{
			HashMap convenio = (HashMap)elemento;
			if(Integer.parseInt(convenio.get("codigoConvenio").toString())==codigoConvenio)
				valor = convenio.get("nombreConvenio").toString();
		}
		
		return valor;
	}
	
	/**
	 * Número de convenios
	 * @return
	 */
	public int getNumConvenios()
	{
		return Utilidades.convertirAEntero(this.getConvenios("numRegistros")+"", true);
	}
	
	/**
	 * Número de convenios
	 * @return
	 */
	private int getNumConveniosSeleccionados()
	{
		int numConvenios = 0;
		for(int i=0;i<Utilidades.convertirAEntero(this.getConvenios("numRegistros")+"");i++)
			if(UtilidadTexto.getBoolean(this.getConvenios("checkbox_"+i)+""))
				numConvenios++;
		return numConvenios;
	}
	
	/**
	 * Método para asignar el número de convenios
	 * @param numRegistros
	 */
	public void setNumConvenios(int numRegistros)
	{
		this.setConvenios("numRegistros", numRegistros);
	}

	/**
	 * @return the diagnosticoEgreso
	 */
	public Diagnostico getDiagnosticoEgreso() {
		return diagnosticoEgreso;
	}

	/**
	 * @param diagnosticoEgreso the diagnosticoEgreso to set
	 */
	public void setDiagnosticoEgreso(Diagnostico diagnosticoEgreso) {
		this.diagnosticoEgreso = diagnosticoEgreso;
	}

	/**
	 * @return the edad
	 */
	public String getEdad() {
		return edad;
	}

	/**
	 * @param edad the edad to set
	 */
	public void setEdad(String edad) {
		this.edad = edad;
	}

	/**
	 * @return the estancia
	 */
	public String getEstancia() {
		return estancia;
	}

	/**
	 * @param estancia the estancia to set
	 */
	public void setEstancia(String estancia) {
		this.estancia = estancia;
	}

	/**
	 * @return the centrosCosto
	 */
	public HashMap<String, Object> getCentrosCosto() {
		return centrosCosto;
	}

	/**
	 * @param centrosCosto the centrosCosto to set
	 */
	public void setCentrosCosto(HashMap<String, Object> centrosCosto) {
		this.centrosCosto = centrosCosto;
	}
	
	/**
	 * @return the centrosCosto
	 */
	public Object getCentrosCosto(String key) {
		return centrosCosto.get(key);
	}

	/**
	 * @param centrosCosto the centrosCosto to set
	 */
	public void setCentrosCosto(String key,Object value) {
		this.centrosCosto.put(key, value);
	}
	
	/**
	 * Método que retorna el número de centros de costo
	 * @return
	 */
	public int getNumCentrosCosto()
	{
		return Utilidades.convertirAEntero(this.getCentrosCosto("numRegistros")+"", true);
	}
	
	/**
	 * Método que retorna el número de centros de costo
	 * @return
	 */
	private int getNumCentrosCostoSeleccionados()
	{
		int numCentrosCosto = 0;
		for(int i=0;i<Utilidades.convertirAEntero(this.getCentrosCosto("numRegistros")+"");i++)
			if(UtilidadTexto.getBoolean(this.getCentrosCosto("checkbox_"+i)+""))
				numCentrosCosto++;
		return numCentrosCosto;
	}
	
	/**
	 * Método para asignar el número de centros de costo
	 * @param numRegistros
	 */
	public void setNumCentrosCosto(int numRegistros)
	{
		this.setCentrosCosto("numRegistros", numRegistros);
	}

	/**
	 * @return the viasIngreso
	 */
	public ArrayList getViasIngreso() {
		return viasIngreso;
	}

	/**
	 * @param viasIngreso the viasIngreso to set
	 */
	public void setViasIngreso(ArrayList viasIngreso) {
		this.viasIngreso = viasIngreso;
	}



	/**
	 * @return the sexos
	 */
	public ArrayList<HashMap<String, Object>> getSexos() {
		return sexos;
	}



	/**
	 * @param sexos the sexos to set
	 */
	public void setSexos(ArrayList<HashMap<String, Object>> sexos) {
		this.sexos = sexos;
	}



	/**
	 * @return the arregloConvenios
	 */
	public ArrayList getArregloConvenios() {
		return arregloConvenios;
	}



	/**
	 * @param arregloConvenios the arregloConvenios to set
	 */
	public void setArregloConvenios(ArrayList arregloConvenios) {
		this.arregloConvenios = arregloConvenios;
	}



	/**
	 * @return the arregloCentrosCosto
	 */
	public ArrayList<HashMap<String, Object>> getArregloCentrosCosto() {
		return arregloCentrosCosto;
	}



	/**
	 * @param arregloCentrosCosto the arregloCentrosCosto to set
	 */
	public void setArregloCentrosCosto(
			ArrayList<HashMap<String, Object>> arregloCentrosCosto) {
		this.arregloCentrosCosto = arregloCentrosCosto;
	}

	
	/**
	 * Método que retorna el nombre del centro de costo seleccionado
	 * @return
	 */
	public String getNombreCentroCosto()
	{
		int codigoCentroCosto = Utilidades.convertirAEntero(this.getCentrosCosto("codigo")+"");
		String nombreCentroCosto = "";
		
		for(int i=0;i<Utilidades.convertirAEntero(this.mapaCentrosCosto.get("numRegistros")+"");i++)
			if(codigoCentroCosto==Integer.parseInt(this.mapaCentrosCosto.get("codigo_"+i).toString()))
				nombreCentroCosto = this.mapaCentrosCosto.get("nombre_"+i).toString();
		return nombreCentroCosto;
	}

	/**
	 * @return the index
	 */
	public String getIndex() {
		return index;
	}



	/**
	 * @param index the index to set
	 */
	public void setIndex(String index) {
		this.index = index;
	}



	/**
	 * @return the mapaCentrosCosto
	 */
	public HashMap getMapaCentrosCosto() {
		return mapaCentrosCosto;
	}



	/**
	 * @param mapaCentrosCosto the mapaCentrosCosto to set
	 */
	public void setMapaCentrosCosto(HashMap mapaCentrosCosto) {
		this.mapaCentrosCosto = mapaCentrosCosto;
	}



	/**
	 * @return the fechaInicial
	 */
	public String getFechaInicial() {
		return fechaInicial;
	}



	/**
	 * @param fechaInicial the fechaInicial to set
	 */
	public void setFechaInicial(String fechaInicial) {
		this.fechaInicial = fechaInicial;
	}



	/**
	 * @return the fechaFinal
	 */
	public String getFechaFinal() {
		return fechaFinal;
	}



	/**
	 * @param fechaFinal the fechaFinal to set
	 */
	public void setFechaFinal(String fechaFinal) {
		this.fechaFinal = fechaFinal;
	}



	/**
	 * @return the centrosAtencion
	 */
	public ArrayList<HashMap<String, Object>> getCentrosAtencion() {
		return centrosAtencion;
	}



	/**
	 * @param centrosAtencion the centrosAtencion to set
	 */
	public void setCentrosAtencion(
			ArrayList<HashMap<String, Object>> centrosAtencion) {
		this.centrosAtencion = centrosAtencion;
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
	 * @return the pathArchivoPlano
	 */
	public String getPathArchivoPlano() {
		return pathArchivoPlano;
	}



	/**
	 * @param pathArchivoPlano the pathArchivoPlano to set
	 */
	public void setPathArchivoPlano(String pathArchivoPlano) {
		this.pathArchivoPlano = pathArchivoPlano;
	}



	/**
	 * @return the urlArchivoPlano
	 */
	public String getUrlArchivoPlano() {
		return urlArchivoPlano;
	}



	/**
	 * @param urlArchivoPlano the urlArchivoPlano to set
	 */
	public void setUrlArchivoPlano(String urlArchivoPlano) {
		this.urlArchivoPlano = urlArchivoPlano;
	}



	/**
	 * @return the mostrarTipoSalidaArchivo
	 */
	public boolean isMostrarTipoSalidaArchivo() {
		return mostrarTipoSalidaArchivo;
	}



	/**
	 * @param mostrarTipoSalidaArchivo the mostrarTipoSalidaArchivo to set
	 */
	public void setMostrarTipoSalidaArchivo(boolean mostrarTipoSalidaArchivo) {
		this.mostrarTipoSalidaArchivo = mostrarTipoSalidaArchivo;
	}
}
