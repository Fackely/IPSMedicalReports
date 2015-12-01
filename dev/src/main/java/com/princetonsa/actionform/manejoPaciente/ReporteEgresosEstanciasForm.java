/*
 * Sep 03, 2008
 */
package com.princetonsa.actionform.manejoPaciente;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.InfoDatosInt;
import util.InfoDatosString;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.manejoPaciente.ConstantesBDManejoPaciente;

/**
 * @author Sebastián Gómez 
 *
 * Clase que genera el reporte de estadísticas de egresos y estancias  
 */
public class ReporteEgresosEstanciasForm extends ValidatorForm 
{
	
	/**
	 * Objeto para manejar los logs de esta clase
	*/
	private Logger logger = Logger.getLogger(ReporteEgresosEstanciasForm.class);
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
	private ArrayList<HashMap<String, Object>> centrosAtencion = new ArrayList<HashMap<String,Object>>();
	private ArrayList arregloTiposPaciente = new ArrayList();
	private ArrayList<HashMap<String, Object>> arregloPaises = new ArrayList<HashMap<String,Object>>();
	private ArrayList<HashMap<String, Object>> arregloCiudades = new ArrayList<HashMap<String,Object>>();
	//************************************************************************************
	
	//**********PARÁMETROS DE LA BUSQUEDA******************************
	private InfoDatosInt centroAtencion;
	private String fechaInicial;
	private String fechaFinal;
	private InfoDatosInt viaIngreso;
	private InfoDatosInt sexo;
	private String estadoFacturacion;
	private HashMap<String, Object> diagnosticosEgreso = new HashMap<String, Object>();
	private String diagSeleccionados;
	private HashMap<String, Object> convenios = new HashMap<String, Object>();
	private HashMap<String, Object> centrosCosto = new HashMap<String, Object>();
	private InfoDatosString tipoPaciente = new InfoDatosString();
	private String prioridad;
	private InfoDatosString pais;
	private HashMap<String, Object> ciudades = new HashMap<String, Object>();
	private String estadoSalida;
	private String tipoEgreso;
	private String tipoSalida;
	//*********************************************************************
	
	//Atributos para abrir el archivo plano
	private String pathArchivoPlano;
	private String urlArchivoPlano;
	
	//Atributo que almacena el nombre del reporte
	private String nombreReporte;
	
	/**
	 * reset de los datos de la forma
	 *
	 */
	public void reset()
	{
		this.estado="";
		this.tipoReporte = ConstantesBD.codigoNuncaValido;
		this.tiposReporte = new ArrayList<HashMap<String,Object>>();
		this.sexos = new ArrayList<HashMap<String,Object>>();
		this.arregloConvenios = new ArrayList();
		this.centrosAtencion = new ArrayList<HashMap<String,Object>>();
		this.arregloPaises = new ArrayList<HashMap<String,Object>>();
		this.index = "";
		this.setNombreReporte("");
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
		this.tipoPaciente = new InfoDatosString("","");
		this.diagnosticosEgreso = new HashMap<String, Object>();
		this.diagSeleccionados = "";
		this.convenios = new HashMap<String, Object>();
		this.centrosCosto = new HashMap<String, Object>();
		this.arregloCentrosCosto = new ArrayList<HashMap<String,Object>>();
		this.viasIngreso = new ArrayList();
		this.arregloTiposPaciente = new ArrayList();
		this.estadoFacturacion = "";
		this.prioridad = "";
		this.arregloCiudades = new ArrayList<HashMap<String,Object>>();
		this.ciudades = new HashMap<String, Object>();
		this.pais = new InfoDatosString("","");
		this.estadoSalida = "";
		this.tipoEgreso = "";
		this.tipoSalida = "";
		
		this.pathArchivoPlano = "";
		this.urlArchivoPlano = "";
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
	 * @return the tipoEgreso
	 */
	public String getTipoEgreso() {
		return tipoEgreso;
	}



	/**
	 * @param tipoEgreso the tipoEgreso to set
	 */
	public void setTipoEgreso(String tipoEgreso) {
		this.tipoEgreso = tipoEgreso;
	}



	/**
	 * @return the estadoSalida
	 */
	public String getEstadoSalida() {
		return estadoSalida;
	}



	/**
	 * @param estadoSalida the estadoSalida to set
	 */
	public void setEstadoSalida(String estadoSalida) {
		this.estadoSalida = estadoSalida;
	}
	
	/**
	 * Método para obtener la descripción del estado de salida
	 * @return
	 */
	public String getDescripcionEstadoSalida()
	{
		if(this.estadoSalida.equals(""))
			return "No aplica";
		else if(UtilidadTexto.getBoolean(this.estadoSalida))
			return "Vivo";
		else
			return "Muerto";
	}



	/**
	 * @return the prioridad
	 */
	public String getPrioridad() {
		return prioridad;
	}



	/**
	 * @param prioridad the prioridad to set
	 */
	public void setPrioridad(String prioridad) {
		this.prioridad = prioridad;
	}



	/**
	 * @return the estadoFacturacion
	 */
	public String getEstadoFacturacion() {
		return estadoFacturacion;
	}



	/**
	 * @param estadoFacturacion the estadoFacturacion to set
	 */
	public void setEstadoFacturacion(String estadoFacturacion) {
		this.estadoFacturacion = estadoFacturacion;
	}



	/**
	 * @return the arregloTiposPaciente
	 */
	public ArrayList getArregloTiposPaciente() {
		return arregloTiposPaciente;
	}



	/**
	 * @param arregloTiposPaciente the arregloTiposPaciente to set
	 */
	public void setArregloTiposPaciente(ArrayList arregloTiposPaciente) {
		this.arregloTiposPaciente = arregloTiposPaciente;
	}



	/**
	 * @return the tipoPaciente
	 */
	public InfoDatosString getTipoPaciente() {
		return tipoPaciente;
	}



	/**
	 * @param tipoPaciente the tipoPaciente to set
	 */
	public void setTipoPaciente(InfoDatosString tipoPaciente) {
		this.tipoPaciente = tipoPaciente;
	}
	
	/**
	 * @return the tipoPaciente
	 */
	public String getCodigoTipoPaciente() {
		return tipoPaciente.getCodigo();
	}



	/**
	 * @param tipoPaciente the tipoPaciente to set
	 */
	public void setCodigoTipoPaciente(String tipoPaciente) {
		this.tipoPaciente.setCodigo(tipoPaciente);
	}
	
	/**
	 * @return the tipoPaciente
	 */
	public String getNombreTipoPaciente() 
	{
		if(tipoPaciente.getNombre().equals(""))
		{
			boolean existe = false;
			for(Object objeto:this.arregloTiposPaciente)
			{
				HashMap elemento = (HashMap)objeto;
				if(elemento.get("tipopaciente").toString().equals(getCodigoTipoPaciente()))
				{
					existe = true;
					tipoPaciente.setNombre(elemento.get("nomtipopaciente").toString());
				}
			}
			
			if(!existe)
				tipoPaciente.setNombre("Todos");
		}
		
		return tipoPaciente.getNombre();
	}



	/**
	 * @param tipoPaciente the tipoPaciente to set
	 */
	public void setNombreTipoPaciente(String tipoPaciente) {
		this.tipoPaciente.setNombre(tipoPaciente);
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
			//Validación del tipo salida
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
			
				case ConstantesBDManejoPaciente.tipoReporteEgresosLugarResidencia:
					//logger.info("NÚMERO DE MESES ENTRE FECHAS: "+UtilidadFecha.numeroMesesEntreFechas(this.fechaInicial, this.fechaFinal));
					if(fechaInicialValida&&fechaFinalValida&&UtilidadFecha.numeroMesesEntreFechas(this.fechaInicial, this.fechaFinal,true)>3)
						errores.add("", new ActionMessage("errors.rangoMayorTresMeses",""));
						
					
					if(this.getCodigoPais().equals(""))
						errores.add("",new ActionMessage("errors.required","El país"));
				break;
				
				case ConstantesBDManejoPaciente.tipoReporteDiagnosticosEgresosRangoEdad:
					if(this.getCodigoViaIngreso()==ConstantesBD.codigoNuncaValido)
						errores.add("",new ActionMessage("errors.required","La vía de ingreso"));
					if(this.prioridad.equals(""))
						errores.add("",new ActionMessage("errors.required","La prioridad"));
					else if(Utilidades.convertirAEntero(this.prioridad)==ConstantesBD.codigoNuncaValido)
						errores.add("",new ActionMessage("errors.integer","La prioridad"));
					else if(Integer.parseInt(this.prioridad)<=0)
						errores.add("",new ActionMessage("errors.integerMayorQue","La prioridad","0"));
				break;
				
				case ConstantesBDManejoPaciente.tipoReporteTotalDiagnosticoEgreso:
					if(fechaInicialValida&&fechaFinalValida&&UtilidadFecha.numeroMesesEntreFechas(this.fechaInicial, this.fechaFinal,true)>3)
						errores.add("", new ActionMessage("errors.rangoMayorTresMeses",""));
				break;
				
				case ConstantesBDManejoPaciente.tipoReporteNPrimerasCausasMorbilidad:
					if(this.prioridad.equals(""))
						errores.add("",new ActionMessage("errors.required","La prioridad"));
					else if(Utilidades.convertirAEntero(this.prioridad)==ConstantesBD.codigoNuncaValido)
						errores.add("",new ActionMessage("errors.integer","La prioridad"));
					else if(Integer.parseInt(this.prioridad)<=0)
						errores.add("",new ActionMessage("errors.integerMayorQue","La prioridad","0"));
				break;
				
				case ConstantesBDManejoPaciente.tipoReporteEstanciaPromMensualPacEgresadosRan:
				case ConstantesBDManejoPaciente.tipoReportePacEgresadosPediatriaRangoEdad:
					if(fechaInicialValida&&fechaFinalValida&&UtilidadFecha.numeroMesesEntreFechas(this.fechaInicial, this.fechaFinal,true)>12)
						errores.add("", new ActionMessage("errors.rangoMayorMeses","","12"));
					if(this.getCodigoViaIngreso()==ConstantesBD.codigoNuncaValido)
						errores.add("",new ActionMessage("errors.required","La vía de ingreso"));
				break;
				
				
				
				case ConstantesBDManejoPaciente.tipoReporteEstanciaPacienteMayorNDias:
					if(this.getCodigoViaIngreso()==ConstantesBD.codigoNuncaValido)
						errores.add("",new ActionMessage("errors.required","La vía de ingreso"));
					if(this.prioridad.equals(""))
						errores.add("",new ActionMessage("errors.required","La prioridad"));
					else if(Utilidades.convertirAEntero(this.prioridad)==ConstantesBD.codigoNuncaValido)
						errores.add("",new ActionMessage("errors.integer","La prioridad"));
					else if(Integer.parseInt(this.prioridad)<=0)
						errores.add("",new ActionMessage("errors.integerMayorQue","La prioridad","0"));
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
	 * @return the diagnosticosEgreso
	 */
	public HashMap<String, Object> getDiagnosticosEgreso() {
		return diagnosticosEgreso;
	}

	/**
	 * @param diagnosticosEgreso the diagnosticosEgreso to set
	 */
	public void setDiagnosticosEgreso(HashMap<String, Object> diagnosticosEgreso) {
		this.diagnosticosEgreso = diagnosticosEgreso;
	}
	
	/**
	 * @return the diagnosticosEgreso
	 */
	public Object getDiagnosticosEgreso(String key) {
		return diagnosticosEgreso.get(key);
	}

	/**
	 * @param diagnosticosEgreso the diagnosticosEgreso to set
	 */
	public void setDiagnosticosEgreso(String key,Object obj) {
		this.diagnosticosEgreso.put(key, obj);
	}
	
	/**
	 * Método que retorna el número de diagnósticos de egreso
	 * @return
	 */
	public int getNumDiagEgreso()
	{
		return Utilidades.convertirAEntero(this.getDiagnosticosEgreso("numRegistros")+"", true);
	}
	
	/**
	 * Método que retorna el número de diagnósticos de egreso seleccionados
	 * @return
	 */
	private int getNumDiagEgresoSeleccionados()
	{
		int numDiag = 0;
		for(int i=0;i<Utilidades.convertirAEntero(this.getDiagnosticosEgreso("numRegistros")+"");i++)
			if(UtilidadTexto.getBoolean(this.getDiagnosticosEgreso("checkbox_"+i)+""))
				numDiag++;
		return numDiag;
	}
	
	/**
	 * Método que asigna el número de diagnosticos de egreso del mapa
	 * @param numRegistros
	 */
	public void setNumDiagEgreso(int numRegistros)
	{
		this.setDiagnosticosEgreso("numRegistros", numRegistros);
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
	public int getNumConveniosSeleccionados()
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
	public int getNumCentrosCostoSeleccionados()
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
		
		for(HashMap elemento:this.arregloCentrosCosto)
			if(Integer.parseInt(elemento.get("codigo").toString())==codigoCentroCosto)
				nombreCentroCosto = elemento.get("nombre").toString();
		
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
	 * Método para obtener el numero de tipos de paciente
	 * @return
	 */
	public int getNumTiposPaciente()
	{
		return this.arregloTiposPaciente.size();
	}



	/**
	 * @return the arregloPaises
	 */
	public ArrayList<HashMap<String, Object>> getArregloPaises() {
		return arregloPaises;
	}



	/**
	 * @param arregloPaises the arregloPaises to set
	 */
	public void setArregloPaises(ArrayList<HashMap<String, Object>> arregloPaises) {
		this.arregloPaises = arregloPaises;
	}



	/**
	 * @return the arregloCiudades
	 */
	public ArrayList<HashMap<String, Object>> getArregloCiudades() {
		return arregloCiudades;
	}



	/**
	 * @param arregloCiudades the arregloCiudades to set
	 */
	public void setArregloCiudades(
			ArrayList<HashMap<String, Object>> arregloCiudades) {
		this.arregloCiudades = arregloCiudades;
	}



	/**
	 * @return the pais
	 */
	public InfoDatosString getPais() {
		return pais;
	}



	/**
	 * @param pais the pais to set
	 */
	public void setPais(InfoDatosString pais) {
		this.pais = pais;
	}
	
	/**
	 * @return the pais
	 */
	public String getCodigoPais() {
		return pais.getCodigo();
	}



	/**
	 * @param pais the pais to set
	 */
	public void setCodigoPais(String pais) {
		this.pais.setCodigo(pais);
	}

	/**
	 * @return the pais
	 */
	public String getNombrePais() 
	{
		for(HashMap<String, Object> elemento:this.arregloPaises)
			if(elemento.get("codigo").toString().equals(pais.getCodigo()))
				pais.setNombre(elemento.get("descripcion").toString());
		
		return pais.getNombre();
	}



	/**
	 * @param pais the pais to set
	 */
	public void setNombrePais(String pais) {
		this.pais.setNombre(pais);
	}
	

	/**
	 * @return the ciudades
	 */
	public HashMap<String, Object> getCiudades() {
		return ciudades;
	}



	/**
	 * @param ciudades the ciudades to set
	 */
	public void setCiudades(HashMap<String, Object> ciudades) {
		this.ciudades = ciudades;
	}
	
	/**
	 * @return the ciudades
	 */
	public Object getCiudades(String key) {
		return ciudades.get(key);
	}



	/**
	 * @param ciudades the ciudades to set
	 */
	public void setCiudades(String key,Object obj) {
		this.ciudades.put(key,obj);
	}
	
	/**
	 * Método que retorna el número de ciudades del mapa ciudades
	 * @return
	 */
	public int getNumCiudades()
	{
		return Utilidades.convertirAEntero(this.ciudades.get("numRegistros")+"", true);
	}
	
	/**
	 * Método que asigna el tamaño del número de registros del mapa ciudades
	 * @param numRegistros
	 */
	public void setNumCiudades(int numRegistros)
	{
		this.ciudades.put("numRegistros",numRegistros);
	}
	
	/**
	 * Método que retorna el número de paises
	 * @return
	 */
	public int getNumPaises()
	{
		return this.arregloPaises.size();
	}
	
	/**
	 * Método para consultar el número de elementos del arreglo de centros de costo
	 * @return
	 */
	public int getNumArregloCentrosCosto()
	{
		return this.arregloCentrosCosto.size();
	}
	
	/**
	 * Método para obtener la descripción del estado de facturación
	 * @return
	 */
	public String getDescripcionEstadoFacturacion()
	{
		if(this.estadoFacturacion.equals(""))
			return "Todos";
		else if(UtilidadTexto.getBoolean(this.estadoFacturacion))
			return "Facturado";
		else
			return "No facturado";
	}
	
	/**
	 * Método para obtener la descripción del estado de facturación
	 * @return
	 */
	public String getDescripcionTipoEgreso()
	{
		if(this.tipoEgreso.equals(""))
			return "";
		else if(UtilidadTexto.getBoolean(this.tipoEgreso))
			return "Egreso";
		else
			return "Sin egreso";
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



	public void setNombreReporte(String nombreReporte) {
		this.nombreReporte = nombreReporte;
	}



	public String getNombreReporte() {
		return nombreReporte;
	}
	
}
