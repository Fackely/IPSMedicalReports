/*
 * @(#)NotasGeneralesEnfermeriaForm.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_04
 *
 */

package com.princetonsa.actionform.ordenesmedicas.cirugias;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import util.ConstantesBD;
import util.UtilidadFecha;


/**
 * Forma para manejo presentación de la funcionalidad 
 * Notas Generales de Enfermeria. 
 * @author <a href="mailto:cperalta@PrincetonSA.com">Carlos Peralta</a>
 *	@version 1.0, 01 /Nov/ 2005
 */
@SuppressWarnings("unchecked")
public class NotasGeneralesEnfermeriaForm extends ActionForm
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Estado en el que se encuentra el proceso.
	 */
	private  String estado = "";
	
	/**
	 * Cuenta del paciente cargado
	 */
	private int cuenta;
	
	/**
	 * Fecha en la que se realiza la nota
	 */
	private String fechaNota;
	
	/**
	 * Hora en que se realiza la nota
	 */
	private String horaNota;
	
	/**
	 * Nota como tal que se pretende ingresar
	 */
	private String nota;
	
	/**
	 * Enfermera que escribe la nota
	 */
	private String enfermera;
	
	/**
	 * Almacena los datos de la las notas generales de enfermeria
	 */
	private HashMap mapaNotasGeneralesEnfermeria;
	
	/**
	 * Patron de ordenamiento por columnas
	 */
	private String patronOrdenar;
	
	/**
	 * String ultimo patron de ordenamiento
	 */
	private String ultimoPatron;
	         
     /**
      * Poscicion del mapa en la consulta de facturas
      */
     private int posicionMapa;
     
     /**
 	 * Offset para el pager 
 	 */
 	private int offset=0;
 	
 	/**
 	 * Via de ingreso del paciente al que se le realizara la nota
 	 */
 	private int viaIngreso;
 	
 	/**
 	 * Fecha de apertura de la cuenta para pacientes de via de ingreso ambulatorios
 	 */
 	private String fechaAperturaCuenta;
 	
 	/**
 	 * Numero de solicitud para trabajar y hacer referencia
 	 */
 	private int numeroSolicitud;
 	
 	/**
 	 * Entero para saber si es resumen
 	 */
 	private int esResumen;
 	
 	/**
 	 * Numero de peticion
 	 */
 	private int numeroPeticion;
 	
 	/**
 	 * Fecha de admision para paciente de via de ingreso Urgencias - hospitalizacion 
 	 */
 	private String fechaAdmision;
 	
 	/**
 	 * Codigo de la cuenta del paciente
 	 */
 	private int codigoCuenta;
 	
 	/**
 	 * Hora de admision
 	 */
 	private String horaAdmision="";
 	
 	/**
 	 * Hora de apertura de la cuenta
 	 */
 	private String horaApertura="";
 	
 	/**
 	 * HashMap de peticiones Paciente
 	 * */
 	private HashMap mapaPeticionesPaciente;
 	
 	/**
 	 * HashMap de peticiones del Medico
 	 * */
 	private HashMap mapaPeticionesMedico;
 	
	
 	// Anexo 179 - Cambio 1.50
	private boolean sinAutorizacionEntidadsubcontratada = false;;
	/** * Mensajes de Advertencia  */
	private ArrayList<String> listaAdvertencias = new ArrayList<String>();
	
	
	
	public void reset ()
	{
		this.mapaNotasGeneralesEnfermeria = new HashMap ();
		this.estado="";
		this.cuenta=0;
		this.enfermera="";
		this.fechaNota="";
		this.horaNota="";
		this.nota="";
		this.fechaAperturaCuenta="";
		this.esResumen=0;
		this.mapaPeticionesMedico = new HashMap();
		this.mapaPeticionesPaciente = new HashMap();
		
		this.listaAdvertencias 						= new ArrayList<String>();
		//this.sinAutorizacionEntidadsubcontratada	= false;
	}
	
	
	/**
	 * Reset único para los mapas
	 */
	public void resetMapa()
	{
		this.mapaNotasGeneralesEnfermeria = new HashMap ();
	}
	
	
	
	
	/**
	 * @return Returns the horaAdmision.
	 */
	public String getHoraAdmision()
	{
		return horaAdmision;
	}


	/**
	 * @param horaAdmision The horaAdmision to set.
	 */
	public void setHoraAdmision(String horaAdmision)
	{
		this.horaAdmision=horaAdmision;
	}


	/**
	 * @return Returns the horaApertura.
	 */
	public String getHoraApertura()
	{
		return horaApertura;
	}


	/**
	 * @param horaApertura The horaApertura to set.
	 */
	public void setHoraApertura(String horaApertura)
	{
		this.horaApertura=horaApertura;
	}


	/**
	 * @return Returns the codigoCuenta.
	 */
	public int getCodigoCuenta()
	{
		return codigoCuenta;
	}
	/**
	 * @param codigoCuenta The codigoCuenta to set.
	 */
	public void setCodigoCuenta(int codigoCuenta)
	{
		this.codigoCuenta=codigoCuenta;
	}
	/**
	 * @return Returns the fechaAdmision.
	 */
	public String getFechaAdmision()
	{
		return fechaAdmision;
	}
	/**
	 * @param fechaAdmision The fechaAdmision to set.
	 */
	public void setFechaAdmision(String fechaAdmision)
	{
		this.fechaAdmision=fechaAdmision;
	}
	/**
	 * @return Returns the numeroPeticion.
	 */
	public int getNumeroPeticion()
	{
		return numeroPeticion;
	}
	/**
	 * @param numeroPeticion The numeroPeticion to set.
	 */
	public void setNumeroPeticion(int numeroPeticion)
	{
		this.numeroPeticion=numeroPeticion;
	}
	/**
	 * @return Returns the esResumen.
	 */
	public int getEsResumen()
	{
		return esResumen;
	}
	/**
	 * @param esResumen The esResumen to set.
	 */
	public void setEsResumen(int esResumen)
	{
		this.esResumen=esResumen;
	}
	/**
	 * @return Returns the numeroSolicitud.
	 */
	public int getNumeroSolicitud()
	{
		return numeroSolicitud;
	}
	/**
	 * @param numeroSolicitud The numeroSolicitud to set.
	 */
	public void setNumeroSolicitud(int numeroSolicitud)
	{
		this.numeroSolicitud=numeroSolicitud;
	}
	/**
	 * @return Returns the fechaAperturaCuenta.
	 */
	public String getFechaAperturaCuenta()
	{
		return fechaAperturaCuenta;
	}
	/**
	 * @param fechaAperturaCuenta The fechaAperturaCuenta to set.
	 */
	public void setFechaAperturaCuenta(String fechaAperturaCuenta)
	{
		this.fechaAperturaCuenta=fechaAperturaCuenta;
	}
	/**
	 * @return Returns the viaIngreso.
	 */
	public int getViaIngreso()
	{
		return viaIngreso;
	}
	
	/**
	 * @param viaIngreso The viaIngreso to set.
	 */
	public void setViaIngreso(int viaIngreso)
	{
		this.viaIngreso=viaIngreso;
	}
	
	/**
	 * @return Returns the posicionMapa.
	 */
	public int getPosicionMapa()
	{
		return posicionMapa;
	}
	
	/**
	 * @param posicionMapa The posicionMapa to set.
	 */
	public void setPosicionMapa(int posicionMapa)
	{
		this.posicionMapa= posicionMapa;
	}
	
	/**
	 * @return Returns the offset.
	 */
	public int getOffset()
	{
		return offset;
	}
	/**
	 * @param offset The offset to set.
	 */
	public void setOffset(int offset)
	{
		this.offset= offset;
	}
	
	/**
	 * @return Returns the cuenta.
	 */
	public int getCuenta()
	{
		return cuenta;
	}
	/**
	 * @param cuenta The cuenta to set.
	 */
	public void setCuenta(int cuenta)
	{
		this.cuenta= cuenta;
	}
	
    /**
     * @return Retorna el estado.
     */
    public  String getEstado()
    {
        return estado;
    }
    
    /**
     * @param estado El estado a establecer.
     */
    public void setEstado(String estado)
    {
        this.estado = estado;
    }
    
	/**
	 * @return Returns the mapaNotasGerenalesEnfermeria.
	 */
	public HashMap getMapaNotasGeneralesEnfermeria()
	{
		return mapaNotasGeneralesEnfermeria;
	}
	
	/**
	 * @param mapaFacturasPaciente The mapaNotasGeneralesEnfermeria to set.
	 */
	public void setMapaNotasGeneralesEnfermeria(HashMap mapaNotasGeneralesEnfermeria)
	{
		this.mapaNotasGeneralesEnfermeria= mapaNotasGeneralesEnfermeria;
	}
	
	/**
	 * @param key
	 * @param value
	 */
	public void setMapaNotasGeneralesEnfermeria(String key, Object value) 
	{
		mapaNotasGeneralesEnfermeria.put(key, value);
	}
	/**
	 * @param key
	 * @return
	 */
	public Object getMapaNotasGeneralesEnfermeria(String key) 
	{
		return mapaNotasGeneralesEnfermeria.get(key);
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
	 * @return Retorna el ultimoPatron.
	 */
	public String getUltimoPatron() 
	{
		return ultimoPatron;
	}
	/**
	 * @param ultimoPatron Asigna el ultimoPatron.
	 */
	public void setUltimoPatron(String ultimoPatron) 
	{
		this.ultimoPatron = ultimoPatron;
	}
	
	/**
	 * @return Returns the fechaNota.
	 */
	public String getFechaNota()
	{
		return fechaNota;
	}
	
	/**
	 * @param fechaNotas The fechaNota to set.
	 */
	public void setFechaNota(String fechaNota)
	{
		this.fechaNota=fechaNota;
	}
	
	/**
	 * @return Returns the horaNota.
	 */
	public String getHoraNota()
	{
		return horaNota;
	}
	
	/**
	 * @param horaNotas The horaNota to set.
	 */
	public void setHoraNota(String horaNota)
	{
		this.horaNota=horaNota;
	}
	
	/**
	 * @return Returns the nota.
	 */
	public String getNota()
	{
		return nota;
	}

	/**
	 * @param nota The nota to set.
	 */
	public void setNota(String nota)
	{
		this.nota=nota;
	}
	
	/**
	 * @return Returns the enfermera.
	 */
	public String getEnfermera()
	{
		return enfermera;
	}
	/**
	 * @param enfermera The enfermera to set.
	 */
	public void setEnfermera(String enfermera)
	{
		this.enfermera=enfermera;
	}
	
	
	
	/**
	 * Función de validación: 
	 * @param mapping
	 * @param request
	 * @return ActionError que especifica el error
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		
		ActionErrors errores = new ActionErrors();
		/*********************************************************************************/
		if(estado.equals("guardarNota") || estado.equals("guardarNotaEnfermera"))
		{
			/**Fecha de la Nota requerida**/
			if(this.getFechaNota().trim().equals("")||this.getFechaNota().trim().equals(null))
			{
				errores.add("Fecha Notas requerido", new ActionMessage("errors.required"," La Fecha de la Nota "));
			}
			/**La hora de la nota es requerida**/
			if(this.getHoraNota().trim().equals("")||this.getHoraNota().trim().equals(null))
			{
				errores.add("Hora Notas requerido", new ActionMessage("errors.required"," La Hora de la Nota "));
			}
			/**La nota es requerida**/
			if(this.getNota().trim().equals("")||this.getNota().trim().equals(null))
			{
				errores.add("La Nota es requerida", new ActionMessage("errors.required"," La Nota "));
			}
			else
			{
				if(this.getNota().trim().toString().length() > 3999)
					errores.add("La Nota es requerida", new ActionMessage("errors.notEspecific"," El Tamaño maximo para las anotaciones es de 3999"));
			}
			/**La hora debe estar en formato valido HH:MM**/
			if(!UtilidadFecha.validacionHora(this.getHoraNota()).puedoSeguir)
			{
				errores.add("formato hora inválido", new ActionMessage("errors.formatoHoraInvalido",this.horaNota));
			}
			/**La fecha de la nota debe estar en formato valido dd/mm/aaaa***/
			if(!UtilidadFecha.validarFecha(this.getFechaNota()))
			{
				errores.add("formato fecha inválido", new ActionMessage("errors.formatoFechaInvalido",this.fechaNota));
			}
			
			if(this.getViaIngreso()==ConstantesBD.codigoViaIngresoHospitalizacion||this.getViaIngreso()==ConstantesBD.codigoViaIngresoUrgencias)
			{
				if((UtilidadFecha.conversionFormatoFechaABD(this.getFechaNota().trim())).compareTo(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual().trim()))<0)
				{
					if((UtilidadFecha.conversionFormatoFechaABD(this.getFechaNota())).compareTo(UtilidadFecha.conversionFormatoFechaABD(this.getFechaAdmision()))<0)
					{
						errores.add("Fecha Nota", new ActionMessage("errors.fechaAnteriorIgualActual"," de la Nota "+this.getFechaNota(), " de la Admision"));				
					}
					if((UtilidadFecha.conversionFormatoFechaABD(this.getFechaNota())).compareTo(UtilidadFecha.conversionFormatoFechaABD(this.getFechaAdmision()))==0)
					{
						if(UtilidadFecha.getHoraActual().trim().compareTo(this.getHoraAdmision())<0)
						{
							errores.add("Hora", new ActionMessage("errors.horaAnteriorIgualAOtraDeReferencia", "de la Nota", "de la Admisión"));
						}
					}

				}
				if((UtilidadFecha.conversionFormatoFechaABD(this.getFechaNota().trim())).compareTo(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual().trim()))==0)
				{
					if(this.getHoraNota().compareTo(UtilidadFecha.getHoraActual())>0)
					{
						errores.add("Hora", new ActionMessage("errors.horaPosteriorIgualAOtraDeReferencia", "de la Nota", "del Sistema"));
					}
				}
				
			}
			/** De la via de ingreso depende la fecha de la nota (Ambulatorios)**/
			if(this.getViaIngreso()==ConstantesBD.codigoViaIngresoAmbulatorios)
			{
				
				if((UtilidadFecha.conversionFormatoFechaABD(this.getFechaNota().trim())).compareTo(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual().trim()))<0)
				{
					if((UtilidadFecha.conversionFormatoFechaABD(this.getFechaNota())).compareTo(UtilidadFecha.conversionFormatoFechaABD(this.getFechaAperturaCuenta()))<0)
					{
						errores.add("Fecha Notas menor a la fecha de apertura de la cuenta ", new ActionMessage("errors.fechaAnteriorIgualActual"," de la Nota ", " de apertura de la Cuenta"));				
					}
					if((UtilidadFecha.conversionFormatoFechaABD(this.getFechaNota())).compareTo(UtilidadFecha.conversionFormatoFechaABD(this.getFechaAperturaCuenta()))==0)
					{
						if(UtilidadFecha.getHoraActual().trim().compareTo(this.getHoraApertura())<0)
						{
							errores.add("Hora", new ActionMessage("errors.horaAnteriorIgualAOtraDeReferencia", "de la Nota", "de la apertura de la Cuenta"));
						}
					}
				}
				
				if((UtilidadFecha.conversionFormatoFechaABD(this.getFechaNota().trim())).compareTo(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual().trim()))==0)
				{
					if(this.getHoraNota().compareTo(UtilidadFecha.getHoraActual())>0)
					{
						errores.add("Hora", new ActionMessage("errors.horaPosteriorIgualAOtraDeReferencia", "de la Nota", "del Sistema"));
					}
				}
			}
			
		}
		return errores;
	}


	/**
	 * @return the mapaPeticionesMedico
	 */
	public HashMap getMapaPeticionesMedico() {
		return mapaPeticionesMedico;
	}


	/**
	 * @param mapaPeticionesMedico the mapaPeticionesMedico to set
	 */
	public void setMapaPeticionesMedico(HashMap mapaPeticionesMedico) {
		this.mapaPeticionesMedico = mapaPeticionesMedico;
	}


	/**
	 * @return the mapaPeticionesPaciente
	 */
	public HashMap getMapaPeticionesPaciente() {
		return mapaPeticionesPaciente;
	}


	/**
	 * @param mapaPeticionesPaciente the mapaPeticionesPaciente to set
	 */
	public void setMapaPeticionesPaciente(HashMap mapaPeticionesPaciente) {
		this.mapaPeticionesPaciente = mapaPeticionesPaciente;
	}
	
	/**
	 * @return the mapaPeticionesPaciente
	 */
	public Object getMapaPeticionesPaciente(String key) {
		return mapaPeticionesPaciente.get(key);
	}


	/**
	 * @param mapaPeticionesPaciente the mapaPeticionesPaciente to set
	 */
	public void setMapaPeticionesPaciente(String key, Object value) {
		this.mapaPeticionesPaciente.put(key, value);
	}
	
		
	/**
	 * @return the mapaPeticionesMedico
	 */
	public Object getMapaPeticionesMedico(String key) {
		return mapaPeticionesMedico.get(key);
	}


	/**
	 * @param mapaPeticionesMedico the mapaPeticionesMedico to set
	 */
	public void setMapaPeticionesMedico(String key, Object value) {
		this.mapaPeticionesMedico.put(key, value);
	}


	public boolean isSinAutorizacionEntidadsubcontratada() {
		return sinAutorizacionEntidadsubcontratada;
	}


	public void setSinAutorizacionEntidadsubcontratada(
			boolean sinAutorizacionEntidadsubcontratada) {
		this.sinAutorizacionEntidadsubcontratada = sinAutorizacionEntidadsubcontratada;
	}


	public ArrayList<String> getListaAdvertencias() {
		return listaAdvertencias;
	}


	public void setListaAdvertencias(ArrayList<String> listaAdvertencias) {
		this.listaAdvertencias = listaAdvertencias;
	}
}