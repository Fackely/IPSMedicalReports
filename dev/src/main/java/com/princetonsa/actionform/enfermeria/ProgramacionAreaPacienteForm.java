package com.princetonsa.actionform.enfermeria;

import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;
import util.ConstantesBD;
import util.ResultadoBoolean;
import util.UtilidadFecha;



/**
 * @version 1.0
 * @fecha 06/01/09
 * @author Jhony Alexander Duque A. y Diego Fernando Bedoya Castaño
 *
 */
public class ProgramacionAreaPacienteForm extends ValidatorForm
{
	/**
	 * Para hacer logs de esta funcionalidad.
	 */	
	private Logger logger = Logger.getLogger(ProgramacionAreaPacienteForm.class);
	
	

	/*-----------------------------------------------
	 * 				ATRIBUTOS DEL PAGER
	 ------------------------------------------------*/
	/**
	 * String Patron Ordenar 
	 * **/
	private String patronOrdenar;
	
	/**
	 * String Ultimo Patron de Ordenamiento
	 * */
	private String ultimoPatron;
	
	/**
	 * String Link Siguiente
	 * */
	private String linkSiguiente;	
	
	/*-----------------------------------------------
	 * 				FIN ATRIBUTOS DEL PAGER
	 ------------------------------------------------*/
	
	
	
	/**
	 * estado del formulario
	 */
	private String estado;
	
	/**
	 * Atributo Mensaje
	 */
	private ResultadoBoolean mensaje=new ResultadoBoolean(false);
	
	/**
	 * Fecha Inicial Orden
	 */
	private String fechaIniOrden;
	
	/**
	 * Fecha Final Orden
	 */
	private String fechaFinOrden;
	
	/**
	 * Fecha Inicial Programacion
	 */
	private String fechaIniProg;
	
	/**
	 * Fecha Final Programacion
	 */
	private String fechaFinProg;
	
	/**
	 * Variable encargada de almacenar los datos del
	 * listado de ordenes de medicamentos.
	 */
	private HashMap ordenesMedicamentos = new HashMap ();
	
	/**
	 * Metodo encargado de almacenar los medicamentos
	 * a los cuales se les va a realizar la progrmacion o
	 * reprogramacion.
	 */
	private HashMap programacionAdmin= new HashMap ();
	
	

	/**
	 * Variable para almacenar las Areas
	 */
	private HashMap areasMap;
	
	/**
	 * Variable para almacenr el Area seleccionada
	 */
	private String area;
	
	/**
	 * Variable para almacenar los pisos
	 */
	private HashMap pisosMap;
	
	/**
	 * Variable para almacenar el piso seleccionado
	 */
	private String piso;
	
	/**
	 * Variable para almacenar las Habitaciones
	 */
	private HashMap habitacionesMap;
	
	/**
	 * Variable para almacenar la Habitacion seleccionada
	 */
	private String habitacion;
	
	/**
	 * Mapa para almacenar los Pacientes
	 */
	private HashMap pacientesMap;
	
	/**
	 * Variable para el check Programados
	 */
	private int checkP;
	
	/**
	 * Variable para manejo Codigo Paciente llamado a Paciente
	 */
	private int codigoPacienteA;
	
	/**
	 * Variable para manejo Codigo Cuenta llamado a Paciente
	 */
	private int codigoCuentaA;
	
	/**
	 * Variable que indica si se debe ocultar el cabezote y el encabezado
	 */
	private int ocultarEnc;
	
	/**
	 * Almacena los datos amostrar de la programacion 
	 */
	private HashMap mostrarProgramacionAdmin = new HashMap();
	
	private String codigoAdmin = "";
	
	
	private boolean permisoAdministrar=false;

	private String index="" ;
	
	/**
	 * Variable para la Hora Inicial de Programacion
	 */
	private String horaIniProg;
	
	/**
	 * Variable para la Hora Final de Programacion
	 */
	private String horaFinProg;
	
	private String esArea;
	
	private String tieneErroresPopUp;
	
	
	/**
	 * Resetea los valores de la forma cuando 
	 * se ingresa por la funcionalidad por paciente.
	 */
	public void resetXPaciente ()
	{
		this.ordenesMedicamentos=new HashMap ();
		this.setOrdenesMedicamentos("numRegistros", 0);
		this.programacionAdmin=new HashMap ();
		this.setProgramacionAdmin("numRegistros", 0);
		this.mostrarProgramacionAdmin=new HashMap ();
		this.setMostrarProgramacionAdmin("numRegistros", 0);
		this.codigoCuentaA=0;
		this.codigoPacienteA=0;
		this.ocultarEnc=0;
		this.codigoAdmin=ConstantesBD.codigoNuncaValido+"";
		this.permisoAdministrar=false;
		this.index= "";
		this.area = ConstantesBD.acronimoNo;
		this.tieneErroresPopUp = ConstantesBD.acronimoNo;
		this.esArea = ConstantesBD.acronimoNo;
	}	

	/**
	 * Resetea los valores de la forma cuando
	 * se ingresa por la funcionalidad por Area
	 * @param codigoInstitucion
	 */
	public void reset(int codigoInstitucion)
	{
		this.areasMap = new HashMap();
		this.areasMap.put("numRegistros", "0");
		this.pisosMap = new HashMap();
		this.pisosMap.put("numRegistros", "0");
		this.habitacionesMap = new HashMap();
		this.habitacionesMap.put("numRegistros", "0");
		this.area="";
		this.piso="";
		this.habitacion="";
		this.pacientesMap = new HashMap();
		this.pacientesMap.put("numRegistros", "0");
		this.checkP=0;
		this.fechaFinOrden="";
		this.fechaIniOrden="";
		this.codigoCuentaA=0;
		this.codigoPacienteA=0;
		this.ocultarEnc=0;
		this.fechaFinProg="";
		this.fechaIniProg="";
		this.permisoAdministrar=false;
		this.index= "";
		this.horaIniProg="";
		this.horaFinProg="";
		this.tieneErroresPopUp = ConstantesBD.acronimoNo;
		this.esArea = ConstantesBD.acronimoNo;
	}


/**************************************************************
 * SETTERS AND GETTERS 	
 **************************************************************/
	
//---------	ordenesMedicamentos --------------------------//
	public HashMap getOrdenesMedicamentos() {
		return ordenesMedicamentos;
	}

	public void setOrdenesMedicamentos(HashMap ordenesMedicamentos) {
		this.ordenesMedicamentos = ordenesMedicamentos;
	}
	
	public Object getOrdenesMedicamentos(String key) {
		return ordenesMedicamentos.get(key);
	}

	public void setOrdenesMedicamentos(String key,Object value) {
		this.ordenesMedicamentos.put(key, value);
	}
//---------	fin ordenesMedicamentos --------------------------//

//---------manejo del pager y el ordenamiento ------------//	
	public String getPatronOrdenar() {
		return patronOrdenar;
	}

	public void setPatronOrdenar(String patronOrdenar) {
		this.patronOrdenar = patronOrdenar;
	}

	public String getUltimoPatron() {
		return ultimoPatron;
	}

	public void setUltimoPatron(String ultimoPatron) {
		this.ultimoPatron = ultimoPatron;
	}

	public String getLinkSiguiente() {
		return linkSiguiente;
	}

	public void setLinkSiguiente(String linkSiguiente) {
		this.linkSiguiente = linkSiguiente;
	}
//--------- fin manejo del pager y el ordenamiento ------------//

//-------------------- programacion administracion ----------------//	
	
	public HashMap getProgramacionAdmin() {
		return programacionAdmin;
	}

	public void setProgramacionAdmin(HashMap programacionAdmin) {
		this.programacionAdmin = programacionAdmin;
	}
	
	public Object getProgramacionAdmin(String key) {
		return programacionAdmin.get(key);
	}

	public void setProgramacionAdmin(String key,Object value) {
		this.programacionAdmin.put(key, value);
	}
//----------------------------------------------------------//
	
//-----------------------------------------------------------//
	public HashMap getMostrarProgramacionAdmin() {
		return mostrarProgramacionAdmin;
	}

	public void setMostrarProgramacionAdmin(HashMap mostrarProgramacionAdmin) {
		this.mostrarProgramacionAdmin = mostrarProgramacionAdmin;
	}
	
	public Object getMostrarProgramacionAdmin(String key) {
		return mostrarProgramacionAdmin.get(key);
	}

	public void setMostrarProgramacionAdmin(String key,Object value) {
		this.mostrarProgramacionAdmin.put(key, value);
	}
//------------------------------------------------------------------------//
	
//------------------------------------//	

	public String getCodigoAdmin() {
		return codigoAdmin;
	}

	public void setCodigoAdmin(String codigoAdmin) {
		this.codigoAdmin = codigoAdmin;
	}
//--------------------------------////
	
	
	public String getEstado() {
		return estado;
	}

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public ResultadoBoolean getMensaje() {
		return mensaje;
	}

	public void setMensaje(ResultadoBoolean mensaje) {
		this.mensaje = mensaje;
	}

	public String getFechaIniOrden() {
		return fechaIniOrden;
	}

	public void setFechaIniOrden(String fechaIniOrden) {
		this.fechaIniOrden = fechaIniOrden;
	}

	public String getFechaFinOrden() {
		return fechaFinOrden;
	}

	public void setFechaFinOrden(String fechaFinOrden) {
		this.fechaFinOrden = fechaFinOrden;
	}
	
	public HashMap getAreasMap() {
		return areasMap;
	}	

	public void setAreasMap(HashMap areasMap) {
		this.areasMap = areasMap;
	}
	
	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}
	
	public Object getAreasMap(String key) {
		return areasMap.get(key);
	}

	public void setAreasMap(String key, Object value) {
		this.areasMap.put(key, value);
	}
	
	public HashMap getPisosMap() {
		return pisosMap;
	}

	public void setPisosMap(HashMap pisosMap) {
		this.pisosMap = pisosMap;
	}
	
	public Object getPisosMap(String key) {
		return pisosMap.get(key);
	}

	public void setPisosMap(String key, Object value) {
		this.pisosMap.put(key, value);
	}

	public String getPiso() {
		return piso;
	}

	public void setPiso(String piso) {
		this.piso = piso;
	}
	
	public HashMap getHabitacionesMap() {
		return habitacionesMap;
	}

	public void setHabitacionesMap(HashMap habitacionesMap) {
		this.habitacionesMap = habitacionesMap;
	}
	
	public Object getHabitacionesMap(String key) {
		return habitacionesMap.get(key);
	}

	public void setHabitacionesMap(String key, Object value) {
		this.habitacionesMap.put(key, value);
	}

	public String getHabitacion() {
		return habitacion;
	}

	public void setHabitacion(String habitacion) {
		this.habitacion = habitacion;
	}
	
	public HashMap getPacientesMap() {
		return pacientesMap;
	}

	public void setPacientesMap(HashMap pacientesMap) {
		this.pacientesMap = pacientesMap;
	}
	
	public Object getPacientesMap(String key) {
		return pacientesMap.get(key);
	}

	public void setPacientesMap(String key, Object value) {
		this.pacientesMap.put(key, value);
	}
	
	public int getCheckP() {
		return checkP;
	}

	public void setCheckP(int checkP) {
		this.checkP = checkP;
	}
	
	public int getCodigoPacienteA() {
		return codigoPacienteA;
	}

	public void setCodigoPacienteA(int codigoPacienteA) {
		this.codigoPacienteA = codigoPacienteA;
	}

	public int getCodigoCuentaA() {
		return codigoCuentaA;
	}

	public void setCodigoCuentaA(int codigoCuentaA) {
		this.codigoCuentaA = codigoCuentaA;
	}
	
	public int getOcultarEnc() {
		return ocultarEnc;
	}

	public void setOcultarEnc(int ocultarEnc) {
		this.ocultarEnc = ocultarEnc;
	}
	
	/**
	 * @return the fechaFinProg
	 */
	public String getFechaFinProg() {
		return fechaFinProg;
	}

	/**
	 * @param fechaFinProg the fechaFinProg to set
	 */
	public void setFechaFinProg(String fechaFinProg) {
		this.fechaFinProg = fechaFinProg;
	}
	
	/**
	 * @return the fechaIniProg
	 */
	public String getFechaIniProg() {
		return fechaIniProg;
	}
	
	/**
	 * @param fechaIniProg the fechaIniProg to set
	 */
	public void setFechaIniProg(String fechaIniProg) {
		this.fechaIniProg = fechaIniProg;
	}
	

	public boolean isPermisoAdministrar() {
		return permisoAdministrar;
	}

	public void setPermisoAdministrar(boolean permisoAdministrar) {
		this.permisoAdministrar = permisoAdministrar;
	}
	
	/**
	 * @return the horaIniProg
	 */
	public String getHoraIniProg() {
		return horaIniProg;
	}

	/**
	 * @param horaIniProg the horaIniProg to set
	 */
	public void setHoraIniProg(String horaIniProg) {
		this.horaIniProg = horaIniProg;
	}

	/**
	 * @return the horaFinProg
	 */
	public String getHoraFinProg() {
		return horaFinProg;
	}

	/**
	 * @param horaFinProg the horaFinProg to set
	 */
	public void setHoraFinProg(String horaFinProg) {
		this.horaFinProg = horaFinProg;
	}

	
/**************************************************************
 *  FIN SETTERS AND GETTERS 	
 **************************************************************/	
	
	

	/**
	 * Metodo encargado de validar..
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
    {
		ActionErrors errores= new ActionErrors();
        errores=super.validate(mapping,request);
        String hora=UtilidadFecha.getHoraActual();
		String fecha=UtilidadFecha.getFechaActual();
        
        String fechaaux;
        
        logger.info("\n ordenes de medicamentossssssssssss "+this.ordenesMedicamentos);
        
        if(this.estado.equals("buscarPacientes")){
        	if(this.getFechaIniOrden().equals("") || this.getFechaFinOrden().equals(""))
			{
				errores.add("descripcion",new ActionMessage("errors.required","La Fecha Inicial y la Fecha Final"));
			}
			else
			{
				if(!this.getFechaFinOrden().equals("") && !this.getFechaIniOrden().equals("")){
					if(!UtilidadFecha.compararFechas(this.getFechaFinOrden().toString(), "00:00", this.getFechaIniOrden().toString(), "00:00").isTrue())
					{
						errores.add("descripcion",new ActionMessage("errors.invalid"," Fecha Inicial "+this.getFechaIniOrden().toString()+" mayor a la Fecha Final "+this.getFechaFinOrden().toString()+" "));
					}
					else
					{    					
						if(!UtilidadFecha.compararFechas(UtilidadFecha.getFechaActual(),"00:00",this.getFechaIniOrden().toString(), "00:00").isTrue())
						 	errores.add("descripcion",new ActionMessage("errors.fechaPosteriorIgualActual",this.getFechaIniOrden().toString(),UtilidadFecha.getFechaActual()));
						
						if(!UtilidadFecha.compararFechas(UtilidadFecha.getFechaActual(),"00:00",this.getFechaFinOrden().toString(), "00:00").isTrue())
							errores.add("descripcion",new ActionMessage("errors.fechaPosteriorIgualActual",this.getFechaFinOrden().toString(),UtilidadFecha.getFechaActual()));
						
						fechaaux=UtilidadFecha.incrementarDiasAFecha(this.getFechaIniOrden().toString(), 90, false);
						
						if(!UtilidadFecha.compararFechas(fechaaux, "00:00", this.getFechaFinOrden().toString(), "00:00").isTrue())
							errores.add("descripcion",new ActionMessage("errors.invalid","El rango entre Fecha inicial y Fecha final supera los 3 meses por lo tanto el rango elegido"));
					}
				}
			}
        	if(this.piso.equals("-1") && this.area.equals("-1"))
        	{
        		errores.add("descripcion",new ActionMessage("errors.required","El Area o el Piso"));
        	}
        	else
        	{
        		if(!this.area.equals("-1") && !this.piso.equals("-1"))
        			errores.add("descripcion",new ActionMessage("prompt.generico","La selección de Area y Piso es excluyente"));
//            	if(!this.habitacion.equals("-1") && this.piso.equals("-1"))
//            		errores.add("descripcion",new ActionMessage("errors.required","El Piso"));
        	}
        }
        
        if(this.estado.equals("buscarPacientesConsulta")){
        	if(this.getFechaIniProg().equals(""))
				errores.add("descripcion",new ActionMessage("errors.required","La Fecha Programación "));
        	if(this.getHoraIniProg().equals(""))
				errores.add("descripcion",new ActionMessage("errors.required","La Hora Programación "));
			if(this.piso.equals("-1") && this.area.equals("-1"))
        	{
        		errores.add("descripcion",new ActionMessage("errors.required","El Area o el Piso"));
        	}
        	else
        	{
        		if(!this.area.equals("-1") && !this.piso.equals("-1"))
        			errores.add("descripcion",new ActionMessage("prompt.generico","La selección de Area y Piso es excluyente"));
//            	if(!this.habitacion.equals("-1") && this.piso.equals("-1"))
//            		errores.add("descripcion",new ActionMessage("errors.required","El Piso"));
        	}
        }
        
        return errores;
	}

	public void setEsArea(String esArea) {
		this.esArea = esArea;
	}

	public String getEsArea() {
		return esArea;
	}

	/**
	 * @return the tieneErroresPopUp
	 */
	public String getTieneErroresPopUp() {
		return tieneErroresPopUp;
	}

	/**
	 * @param tieneErroresPopUp the tieneErroresPopUp to set
	 */
	public void setTieneErroresPopUp(String tieneErroresPopUp) {
		this.tieneErroresPopUp = tieneErroresPopUp;
	}
}