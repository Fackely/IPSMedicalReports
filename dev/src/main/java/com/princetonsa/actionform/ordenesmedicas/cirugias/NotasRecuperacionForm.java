/*
 * Creado en Nov 23, 2005
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
 * @author <a href="mailto:cperalta@PrincetonSA.com">Carlos Peralta</a>
 * @version 1.0, 23 /Nov/ 2005
 */
@SuppressWarnings("unchecked")
public class NotasRecuperacionForm extends ActionForm 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Manejo de estados para el flujo de la funcionalidad
	 */
	private String estado;
	
	/**
	 * Fecha de registro de la nota de recuperacion
	 */
    private String fechaRecuperacion;
    
    /**
     * Hora de registro de la nota de recuperacion
     */
    private String horaRecuperacion;
	  
   /**
    * Se guardan las notas de recuperacion
    */
    private HashMap mapaNotas=new HashMap();
  
   
   /**
	 * Manejo del histórico del resumen gestacional
	 */
	private HashMap historicoNotasRecuperacion;
	
   /**
    * Campo para guardar las observaciones generales de las notas de recuperacion
    */
   private String observacionesGralesNueva;
   
   /**
    * Campo para guardar el nuevo registro de observaciones en las notas de recuperacion
    */
   private String observacionesGrales;
   
   /**
    * Cadena para guardar los medicamentos nuevos de las notas de recuperacion
    */
   private String medicamentosNuevos;
   
   /**
    * Cadena para guardar todos los medicamentos ingresados en las notas de recuperacion
    */
   private String medicamentosGrales;
     
	/**
	 * Notas de recuperacion parametrizadas
	 */
	private HashMap notasRecuperacionParam;
	
	/**
	 * Histórico de los nuevas notas de recuperacion ingresadas
	 */
	private HashMap historicoNuevasNotas;
	
	/**
	 * Es un string para saber desde que pagian se mando a cargar 
	 *
	 */
	private String pagina;
	
	/**
	 * Para las observaciones en el popOut
	 */
	private String temporalDinamico;
	
	/**
	 * String apra almacenar el valor de cada observacion nueva ingresada
	 */
	private String valorNotaDinamico;
	
	/**
	 * Entero con la posicion del mapa
	 */
	private int posicionMapa;
	
	/**
	 * Mapa con las fechas donde se realizaron notas de recuperacion
	 */
	private HashMap fechasNotasRecuperacion;
	
	/**
	 * Entero con el numero de solicitud ar hacer referencia
	 */
	private int numeroSolicitud;
	
	
	/**
	 * Entero con el numero de peticion
	 */
	private int numeroPeticion;
	
	/**
	 * Entero para saber si es resumen
	 */
	private int esResumen;
	
	/**
	 * Codigo de la cuenta del paciente cargado en session
	 */
	private int codigoCuenta;
	
	/**
	 * Via de ingreso del paciente cargado en session
	 */
	private int viaIngreso;
	
	/**
	 * Fecha de apertura de la cuenta del paciente cargado en session(Ambulatorios)
	 */
	private String fechaAperturaCuenta;
	
	/**
	 * Fecha de apertura de la cuenta para paciente de urgencias - Hospitalizacion
	 */
	private String fechaAdmision;
	
	
	private String horaAdmisionUrg;
	
	
	private String horaAdmisionHosp;
	
	
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
 	
 	/**
 	 * String patron ordenar
 	 * */
 	private String patronOrdenar;
 	
 	/**
 	 * String ultimo Patron
 	 * */
 	private String ultimoPatron;
 
 	
 	
 	// Anexo 179 - Cambio 1.50
	private boolean sinAutorizacionEntidadsubcontratada;
	/** * Mensajes de Advertencia  */
	private ArrayList<String> listaAdvertencias = new ArrayList<String>();
 	
 	
	
	/**
	 * Este método inicializa en valores vacíos, -mas no nulos- los atributos de este objeto.
	 */
	public void reset() 
	{
		this.mapaNotas=new HashMap();
		this.observacionesGrales="";
		this.observacionesGralesNueva="";
		this.notasRecuperacionParam=new HashMap();
		this.historicoNuevasNotas=new HashMap();
		this.historicoNotasRecuperacion=new HashMap();
		this.fechasNotasRecuperacion= new HashMap();
		this.medicamentosGrales="";
		this.medicamentosNuevos="";
		this.setMapaNotas("codigosOtros","");
		this.horaRecuperacion="";
		this.fechaRecuperacion="";
		this.pagina = "";
		this.temporalDinamico="";
		this.valorNotaDinamico="";
		this.posicionMapa=0;
		this.esResumen=0;
		this.mapaPeticionesPaciente = new HashMap();
		this.mapaPeticionesMedico = new HashMap();
		this.patronOrdenar = "";
		this.ultimoPatron = "";
		
		//this.sinAutorizacionEntidadsubcontratada = false;
		//this.listaAdvertencias = new ArrayList<String>();
	}
	
	  
	public void resetBasico()
	{
		this.fechaRecuperacion=UtilidadFecha.conversionFormatoFechaAAp(UtilidadFecha.getFechaActual());
		this.horaRecuperacion=UtilidadFecha.conversionFormatoFechaAAp(UtilidadFecha.getHoraActual());
	}
	
	
	public void resetMapaNuevo()
	{
		this.historicoNuevasNotas=new HashMap();
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
	 * @return Returns the horaAdmisionHosp.
	 */
	public String getHoraAdmisionHosp()
	{
		return horaAdmisionHosp;
	}

	/**
	 * @param horaAdmisionHosp The horaAdmisionHosp to set.
	 */
	public void setHoraAdmisionHosp(String horaAdmisionHosp)
	{
		this.horaAdmisionHosp=horaAdmisionHosp;
	}

	/**
	 * @return Returns the horaAdmisionUrg.
	 */
	public String getHoraAdmisionUrg()
	{
		return horaAdmisionUrg;
	}

	/**
	 * @param horaAdmisionUrg The horaAdmisionUrg to set.
	 */
	public void setHoraAdmisionUrg(String horaAdmisionUrg)
	{
		this.horaAdmisionUrg=horaAdmisionUrg;
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
		this.posicionMapa=posicionMapa;
	}
	/**
	 * @return Returns the valorNotaDinamico.
	 */
	public String getValorNotaDinamico()
	{
		return valorNotaDinamico;
	}
	/**
	 * @param valorNotaDinamico The valorNotaDinamico to set.
	 */
	public void setValorNotaDinamico(String valorNotaDinamico)
	{
		this.valorNotaDinamico=valorNotaDinamico;
	}
	/**
	 * @return Returns the temporalDinamico.
	 */
	public String getTemporalDinamico()
	{
		return temporalDinamico;
	}
	/**
	 * @param temporalDinamico The temporalDinamico to set.
	 */
	public void setTemporalDinamico(String temporalDinamico)
	{
		this.temporalDinamico=temporalDinamico;
	}
	/**
	 * @return Returns the fechaRecuperacion.
	 */
	public String getFechaRecuperacion()
	{
		return fechaRecuperacion;
	}
	/**
	 * @param fechaRecuperacion The fechaRecuperacion to set.
	 */
	public void setFechaRecuperacion(String fechaRecuperacion)
	{
		this.fechaRecuperacion=fechaRecuperacion;
	}
	/**
	 * @return Returns the horaRecuperacion.
	 */
	public String getHoraRecuperacion()
	{
		return horaRecuperacion;
	}
	/**
	 * @param horaRecuperacion The horaRecuperacion to set.
	 */
	public void setHoraRecuperacion(String horaRecuperacion)
	{
		this.horaRecuperacion=horaRecuperacion;
	}
	/**
	 * @return Returns the medicamentosGrales.
	 */
	public String getMedicamentosGrales()
	{
		return medicamentosGrales;
	}
	/**
	 * @param medicamentosGrales The medicamentosGrales to set.
	 */
	public void setMedicamentosGrales(String medicamentosGrales)
	{
		this.medicamentosGrales=medicamentosGrales;
	}
	/**
	 * @return Returns the medicamentosNuevos.
	 */
	public String getMedicamentosNuevos()
	{
		return medicamentosNuevos;
	}
	/**
	 * @param medicamentosNuevos The medicamentosNuevos to set.
	 */
	public void setMedicamentosNuevos(String medicamentosNuevos)
	{
		this.medicamentosNuevos=medicamentosNuevos;
	}
	/**
	 * @return Returns the estado.
	 */
	public String getEstado()
	{
		return estado;
	}
	/**
	 * @param estado The estado to set.
	 */
	public void setEstado(String estado)
	{
		this.estado = estado;
	}
	
	/**
	 * @return Returns the mapa.
	 */
	public HashMap getMapaNotas()
	{
		return mapaNotas;
	}
	/**
	 * @param mapa The mapa to set.
	 */
	public void setMapaNotas(HashMap mapaNotas)
	{
		this.mapaNotas = mapaNotas;
	}
	/**
	 * @return Returna la propiedad del mapa mapa.
	 */
	public Object getMapaNotas(String key)
	{
		return mapaNotas.get(key);
	}
	/**
	 * @param Asigna la propiedad al mapa
	 */
	public void setMapaNotas(String key, Object value)
	{
		this.mapaNotas.put(key, value);
	}

	/**
	 * @return Returns the observacionesGrales.
	 */
	public String getObservacionesGrales()
	{
		return observacionesGrales;
	}
	/**
	 * @param observacionesGrales The observacionesGrales to set.
	 */
	public void setObservacionesGrales(String observacionesGrales)
	{
		this.observacionesGrales = observacionesGrales;
	}
	
		/**
	 * @return Retorna observacionesGralesNueva.
	 */
	public String getObservacionesGralesNueva() 
	{
		return observacionesGralesNueva;
	}
	/**
	 * @param Asigna observacionesGralesNueva.
	 */
	public void setObservacionesGralesNueva(String observacionesGralesNueva) 
	{
		this.observacionesGralesNueva = observacionesGralesNueva;
	}
	
	/**
	 * @return Returns the mapa.
	 */
	public HashMap getNotasRecuperacionParam()
	{
		return notasRecuperacionParam;
	}
	
	/**
	 * @return Returna la propiedad del mapa mapa.
	 */
	public Object getNotasRecuperacionParam(String key)
	{
		return notasRecuperacionParam.get(key);
	}
	/**
	 * @param Asigna la propiedad al mapa
	 */
	public void setNotasRecuperacionParam(String key, Object value)
	{
		this.notasRecuperacionParam.put(key, value);
	}
	
	/**
	 * @param mapa The mapa to set.
	 */
	public void setNotasRecuperacionParam(HashMap notasRecuperacionParam)
	{
		this.notasRecuperacionParam = notasRecuperacionParam;
	}
	
	/**
	 * @return Returns the mapa.
	 */
	public HashMap getHistoricoNuevasNotas()
	{
		return historicoNuevasNotas;
	}
	
	/**
	 * @return Returna la propiedad del mapa mapa.
	 */
	public Object getHistoricoNuevasNotas(String key)
	{
		return historicoNuevasNotas.get(key);
	}
	/**
	 * @param Asigna la propiedad al mapa
	 */
	public void setHistoricoNuevasNotas(String key, Object value)
	{
		this.historicoNuevasNotas.put(key, value);
	}
	
	/**
	 * @param mapa The mapa to set.
	 */
	public void setHistoricoNuevasNotas(HashMap historicoNuevasNotas)
	{
		this.historicoNuevasNotas = historicoNuevasNotas;
	}
	
	
	/**
	 * @return Returns the mapa.
	 */
	public HashMap getHistoricoNotasRecuperacion()
	{
		return historicoNotasRecuperacion;
	}
	
	/**
	 * @return Returna la propiedad del mapa mapa.
	 */
	public Object getHistoricoNotasRecuperacion(String key)
	{
		return historicoNotasRecuperacion.get(key);
	}
	/**
	 * @param Asigna la propiedad al mapa
	 */
	public void setHistoricoNotasRecuperacion(String key, Object value)
	{
		this.historicoNotasRecuperacion.put(key, value);
	}
	
	/**
	 * @param mapa The mapa to set.
	 */
	public void setHistoricoNotasRecuperacion(HashMap historicoNotasRecuperacion)
	{
		this.historicoNotasRecuperacion = historicoNotasRecuperacion;
	}
	
	/**
	 * @return Retorna pagina.
	 */
	public String getPagina()
	{
		return pagina;
	}
	/**
	 * @param Asigna pagina.
	 */
	public void setPagina(String pagina) 
	{
		this.pagina = pagina;
	}
	
	/**
	 * @return Returns the mapa.
	 */
	public HashMap getFechasNotasRecuperacion()
	{
		return fechasNotasRecuperacion;
	}
	
	/**
	 * @return Returna la propiedad del mapa fechasNotasRecuperacion.
	 */
	public Object getFechasNotasRecuperacion(String key)
	{
		return fechasNotasRecuperacion.get(key);
	}
	/**
	 * @param Asigna la propiedad al mapa fechasNotasRecuperacion
	 */
	public void setFechasNotasRecuperacion(String key, Object value)
	{
		this.fechasNotasRecuperacion.put(key, value);
	}
	
	/**
	 * @param fechasNotasRecuperacion The fechasNotasRecuperacion to set.
	 */
	public void setFechasNotasRecuperacion(HashMap fechasNotasRecuperacion)
	{
		this.fechasNotasRecuperacion = fechasNotasRecuperacion;
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
		if(estado.equals("guardarNota"))
		{
			/**Fecha de la Nota requerida**/
			if(this.getFechaRecuperacion().trim().equals("")||this.getFechaRecuperacion().trim().equals(null))
			{
				errores.add("Fecha Notas requerido", new ActionMessage("errors.required"," La Fecha de la Nota "));
			}
			/**La hora de la nota es requerida**/
			if(this.getHoraRecuperacion().trim().equals("")||this.getHoraRecuperacion().trim().equals(null))
			{
				errores.add("Hora Notas requerido", new ActionMessage("errors.required"," La Hora de la Nota "));
			}
			/**La hora debe estar en formato valido HH:MM**/
			if(!UtilidadFecha.validacionHora(this.getHoraRecuperacion()).puedoSeguir)
			{
				errores.add("formato hora inválido", new ActionMessage("errors.formatoHoraInvalido",this.horaRecuperacion));
			}
			/**La fecha de la nota debe estar en formato valido dd/mm/aaaa***/
			if(!UtilidadFecha.validarFecha(this.getFechaRecuperacion()))
			{
				errores.add("formato fecha inválido", new ActionMessage("errors.formatoFechaInvalido",this.fechaRecuperacion));
			}
			/** De la via de ingreso depende la fecha de la nota (Hospitalizacion)**/
			if(this.getViaIngreso()==ConstantesBD.codigoViaIngresoHospitalizacion||this.getViaIngreso()==ConstantesBD.codigoViaIngresoUrgencias)
			{
				if((UtilidadFecha.conversionFormatoFechaABD(this.getFechaRecuperacion().trim())).compareTo(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual().trim()))<0)
				{
					if((UtilidadFecha.conversionFormatoFechaABD(this.getFechaRecuperacion())).compareTo(UtilidadFecha.conversionFormatoFechaABD(this.getFechaAdmision()))<0)
					{
						errores.add("Fecha Nota", new ActionMessage("errors.fechaAnteriorIgualActual"," de la Nota "+this.getFechaRecuperacion(), " de la Admision"));				
					}
					if((UtilidadFecha.conversionFormatoFechaABD(this.getFechaRecuperacion())).compareTo(UtilidadFecha.conversionFormatoFechaABD(this.getFechaAdmision()))==0)
					{
						if(UtilidadFecha.getHoraActual().trim().compareTo(this.getHoraAdmision())<0)
						{
							errores.add("Hora", new ActionMessage("errors.horaAnteriorIgualAOtraDeReferencia", "de la Nota", "de la Admisión"));
						}
					}

				}
				if((UtilidadFecha.conversionFormatoFechaABD(this.getFechaRecuperacion().trim())).compareTo(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual().trim()))==0)
				{
					if(this.getHoraRecuperacion().compareTo(UtilidadFecha.getHoraActual())>0)
					{
						errores.add("Hora", new ActionMessage("errors.horaPosteriorIgualAOtraDeReferencia", "de la Nota", "del Sistema"));
					}
				}
				
			}
			/** De la via de ingreso depende la fecha de la nota (Ambulatorios)**/
			if(this.getViaIngreso()==ConstantesBD.codigoViaIngresoAmbulatorios)
			{
				
				if((UtilidadFecha.conversionFormatoFechaABD(this.getFechaRecuperacion().trim())).compareTo(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual().trim()))<0)
				{
					if((UtilidadFecha.conversionFormatoFechaABD(this.getFechaRecuperacion())).compareTo(UtilidadFecha.conversionFormatoFechaABD(this.getFechaAperturaCuenta()))<0)
					{
						errores.add("Fecha Notas menor a la fecha de apertura de la cuenta ", new ActionMessage("errors.fechaAnteriorIgualActual"," de la Nota ", " de apertura de la Cuenta"));				
					}
					if((UtilidadFecha.conversionFormatoFechaABD(this.getFechaRecuperacion())).compareTo(UtilidadFecha.conversionFormatoFechaABD(this.getFechaAperturaCuenta()))==0)
					{
						if(UtilidadFecha.getHoraActual().trim().compareTo(this.getHoraApertura())<0)
						{
							errores.add("Hora", new ActionMessage("errors.horaAnteriorIgualAOtraDeReferencia", "de la Nota", "de la apertura de la Cuenta"));
						}
					}
				}
				
				if((UtilidadFecha.conversionFormatoFechaABD(this.getFechaRecuperacion().trim())).compareTo(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual().trim()))==0)
				{
					if(this.getHoraRecuperacion().compareTo(UtilidadFecha.getHoraActual())>0)
					{
						errores.add("Hora", new ActionMessage("errors.horaPosteriorIgualAOtraDeReferencia", "de la Nota", "del Sistema"));
					}
				}
				
				
			}
			
		}
		return errores;
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
	 * @return the mapaPeticionesMedico
	 */
	public Object getMapaPeticionesMedico(String key) {
		return mapaPeticionesMedico.get(key);
	}

	/**
	 * @param mapaPeticionesMedico the mapaPeticionesMedico to set
	 */
	public void setMapaPeticionesMedico(String key,Object value) {
		this.mapaPeticionesMedico.put(key, value);
	}

	/**
	 * @return the patronOrdenar
	 */
	public String getPatronOrdenar() {
		return patronOrdenar;
	}

	/**
	 * @param patronOrdenar the patronOrdenar to set
	 */
	public void setPatronOrdenar(String patronOrdenar) {
		this.patronOrdenar = patronOrdenar;
	}

	/**
	 * @return the ultimoPatron
	 */
	public String getUltimoPatron() {
		return ultimoPatron;
	}

	/**
	 * @param ultimoPatron the ultimoPatron to set
	 */
	public void setUltimoPatron(String ultimoPatron) {
		this.ultimoPatron = ultimoPatron;
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
