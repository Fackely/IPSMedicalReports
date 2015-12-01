package com.mercury.actionform.odontologia;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.util.MessageResources;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.InfoDatosInt;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.reportes.InfoEncabezadoReporte;

import com.mercury.dto.odontologia.DtoCartaDental;
import com.mercury.dto.odontologia.DtoInterpretacionIndicePlaca;
import com.mercury.dto.odontologia.DtoOtroHallazgo;
import com.mercury.mundo.odontologia.ConsentimientoInformado;
import com.mercury.mundo.odontologia.EvolucionOdontologia;
import com.mercury.mundo.odontologia.IndicePlaca;
import com.mercury.mundo.odontologia.Odontograma;
import com.mercury.mundo.odontologia.Procedimiento;
import com.mercury.util.UtilidadDynaCollection;
import com.princetonsa.dto.administracion.DtoEspecialidades;
import com.princetonsa.dto.odontologia.DtoHallazgoOdontologico;
import com.princetonsa.dto.odontologia.DtoSectorSuperficieCuadrante;
import com.princetonsa.sort.odontologia.SortGenerico;

/**
 * 
 *
 */
@SuppressWarnings("unchecked")
public class TratamientosOdontologiaForm extends ValidatorForm
{
	
	/**
	 * Mensajes parametrizados de error.
	 */
	private MessageResources messageResource = MessageResources
			.getMessageResources("com.servinte.mensajes.historiaclinica.tratamientosOdontologia");
	
	
	
	/**
	 * Estado actual dentro del flujo de la funcionalidad
	 */ 
	private String		  estado;
	
	/**
	 * Estado anterior al estado actual, usado al ingresar nuevos tratamientos por diente
	 */
	private String			estadoAnterior;
	
	/**
	 * Indica si se deben mostrar o no los cabezotes en jsp
	 */
	private String		  ocultarCabezotes="";
	
	/**
	 * Columnas para el orden del listado de tratamientos
	 */
	private String		  columna;
	
	/**
	 * Colecci&oacute;n general con todos los tratamientos del paciente
	 */
	private Collection	  tratamientos;
	
	/**
	 * C&oacute;digo del tratamiento
	 */
	private int			 codigo;
	
	/**
	 * Tipo de tratamiento
	 */
	private InfoDatosInt	tipoTratamiento;
	
	/**
	 * Fecha de iniciaci&oacute;n
	 */
	private String		  fecIniciacion;
	
	/**
	 * Fecha de finalizaci&oacute;n
	 */
	private String		  fecFinalizacion;
	
	/**
	 * Diagn&oacute;stico y plan de tratamiento
	 */
	private String		  diagPlanTratamiento;

	private String		  motivoConsulta;

	/**
	 * Evoluciones registradas sobre el tratamiento
	 */
	private ArrayList	   evoluciones;
	
	private Map			 analisis;
	
	private int			 numAnalisis;
	
	private Map			 obsSeccion;
	
	private int			 numObsSeccion;
	
	private EvolucionOdontologia nuevaEvolucion;
	
	private Map				tratamientosDientes;
	
	private int				numTratamientosDientes;
	
	private Map				analisisDientes;
	
	private	int				numAnalisisDientes;
	
	/******************* ODONTOGRAMAS *********************/
	private ArrayList	   odontogramas;
	/*******************************************************/

	/********************* INDICE PLACA *******************/
	private ArrayList	   indicesPlaca;
	/******************************************************/

	private Map				dientesAusentes;
	
	/**
	 * Informacion del medico
	 */
	private String		  datosMedico;
	
	/**
	 * Observaciones anteriores del tratamiento
	 */
	private String		  observacionesAnteriores;

	/**
	 * Observaciones nuevas del tratamiento
	 */
	private String		  observacionesNuevas;
	
	private ArrayList procedimientos;
	
	private ArrayList procedimientosTemporal;
	
	private Procedimiento nuevoProcedimiento;
	
	private ArrayList consentimientosInformados;
	
	private ConsentimientoInformado nuevoConsentimientoInformado;
	
	private String estadoSeccionConsentimientos;
	
	private String estadoSeccionEvoluciones;
	
	private String estadoSeccionPlanTratamiento;
	
	private boolean erroresPlanTratamiento;
	
	private boolean erroresEvoluciones;
	
	private boolean erroresConsentimientos;
	
	private String ultimoOrdenamiento;
	
	//****************************************************************************
	
	/**
	 * Mapa que contienen los diagn&oacute;sticos de la carta dental
	 * */
	private HashMap diagnosticosCartadentalMap;
	
	/**
	 * Mapa que contiene los tratamientos de la carta dental
	 * */
	private HashMap tratamientosCartadentalMap;
	
	/**
	 *  Mapa que contiene las superficies de los dientes de la carta dental
	 * */
	private HashMap superficiesDienteMap;
	
	/**
	 * Dto Carta Dental
	 * */
	private DtoCartaDental dtoCartaDental;
	
	/**
	 * Almacena los diagn&oacute;sticos adicionados
	 * */
	private String diagnosticosAdd [];
	
	/**
	 * Almacena los tratamientos adicionados
	 * */
	private String tratamientoAdd [];   
	
	/**
	 * indica la posicion del diente dentro de la carta dental
	 * */
	private int numeroDienteIndex;
	
	/**
	 * Codigo del Tratamiento escogido en la carta dental
	 */
	private String codTratamientoCartDental;
	
	/**
	 * Almacena los hallagos que ya estan guardados en la carta dental
	 */
	private String antiguosHallazgos;
   
	/**
	 * Almacena los nuevos hallazgos de la carta dental 
	 */
	private String nuevosHallazgos;
	
	/**
	 * Índice del odontograma
	 */
	private int indiceOdontograma;
	
	//*****************************************************************************
	
	/*
	 * Cambio para versalles
	 */
	
	/**
	 * Contiene los hallazgos encontrados en la secci&oacute;n otros hallazgos
	 */
	private ArrayList<DtoOtroHallazgo> hallazgosOtros;
	
	/**
	 * Contiene los hallazgos encontrados en la secci&oacute;n boca
	 */
	private ArrayList<DtoOtroHallazgo> hallazgosBoca;
	
	/**
	 * Atributo de control para eliminar los hallazgos de la secci&oacute;n otros hallazgos
	 */
	private int hallazgoEliminado;
	
	/**
	 * XML utilizado para la comuinicaci&oacute;n entre el flash y el java
	 * Mantiene la informaci&oacute;n de las modificaciones realizadas al odontograma
	 */
	private String xmlOdontograma;
	
	/**
	 * XML utilizado para la comuinicaci&oacute;n entre el flash y el java
	 * Mantiene la lista de los posibles hallazgos parametrizados en el sistema, secci&oacute;n diente
	 */
	private String xmlHallazgoDiente;
	
	/**
	 * XML utilizado para la comuinicaci&oacute;n entre el flash y el java
	 * Mantiene la lista de los posibles hallazgos parametrizados en el sistema, secci&oacute;n superficie
	 */
	private String xmlHallazgoSuperficie;

	/**
	 * Listado de las posibles piezas dentales para mostrar en la secci&oacute;n otros hallazgos
	 */
	private ArrayList<Integer> listaPiezasDentales;
	
	/**
	 * Listado de los hallazgos de diente parametrizados
	 */
	private ArrayList<DtoHallazgoOdontologico> arrayHallazgosDiente;
	/**
	 * Listado de los hallazgos de superfice parametrizados
	 */
	private ArrayList<DtoHallazgoOdontologico> arrayHallazgosSuperficie;
	/**
	 * Listado de Listar los hallazgos de boca parametrizados
	 */
	private ArrayList<DtoHallazgoOdontologico> arrayHallazgosBoca;

	/**
	 * Atributo de control para la selección de los nuevos hallazgos
	 */
	private int hallazgoSeleccionado;
	/**
	 * Atributo de control para la selección de los nuevos hallazgos
	 */
	private int tipoHallazgoSeleccionado;
	/**
	 * Atributo de control para la selección de los nuevos hallazgos
	 */
	private int indiceHallazgoSeleccionado;
	/**
	 * Atributo de control para la selección de la pieza dental
	 */
	private int codigoPiezaSeleccionada;
	
	/**
	 * Atributo de control para la selección de la superficie de la pieza dental
	 */
	private int codigoSuperficieSeleccionada;
	/**
	 * Listado de las posibles superficies para las piezas dentales
	 */
	private ArrayList<DtoSectorSuperficieCuadrante> arraySuperficies;
	
	/**
	 * Listado de las posibles interpretaciones presentadas en el índice de placa
	 */
	private ArrayList<DtoInterpretacionIndicePlaca> arrayInterpretacionIndicePlaca;
	
	/**
	 * Info facade para mostrar el encabezado del reporte
	 */
	private InfoEncabezadoReporte infoEncabezadoReporte;
	
	/**
	 * atributo para Mostrar seccion indice de placa
	 * en la impresion higiene oral 
	 */
	private String mostrarSeccionIndicePlaca;
	
	/**
	 * Atributo para guardar la imagen del plan de tratamiento
	 */
	private String imagen;
	
	
	/**
	 *Lista de especialidades asociadas a un medico 
	 */
	private ArrayList<DtoEspecialidades> listaEspecialidades;
	
	/**
	 *Codigo de la especialidad seleccionada 
	 */
	private Integer especialidadSeleccionada;
	
	
	/**
	 *Nombres y apellidos del profesional de la salud 
	 */
	private String nombreApellidoProfesinalSalud;
	
	/**
	 * numero de registro asociado a Profesional de la salud
	 */
	private String numeroRegistro;
	
	
	
	/**
	 *Cuando el profesional de la salud solo tiene asociada una sola especialidad este campo se llena para mostrar con un bean:write 
	 */
	private String descripcionEspecialidadUnica;
	
	
	/**
	 *contiene la informacion profesional del medico , nombres , numero registro y especialidades asociadas 
	 */
	private String informacionProfesionalMedico;
	
	/**
	 * valor para permitir editar el combo de especialidad cuando tiene mas de una  
	 */
	private Boolean permiteEditar;
	
	/**
	 *valor usado para pintar o no contentido de un lawyer 
	 */
	private String pintar;
	
	/**
	 *descripcion de una especialidad que va a ser mostrar en los reportes en jsp  
	 */
	private String descripcionEspecialidad;
	
	/**
	 * nuevaObservacion
	 */
	private String nuevaObservacion;
	
	
	/**
	 * Observacion del indice de placa inicial, cuando se guarda por priemra vez
	 */
	private String observacionIndicePlacaInicial;
	
	
	/**
	 * Observacion de todo el plan de tratamiento
	 */
	private String observacionPlanTratamiento;
	

	/**
	 * @return Retorna  estado.
	 */
	public String getEstado()
	{
		return estado;
	}

	/**
	 * @param estado asigna estado.
	 */
	public void setEstado(String estado)
	{
		this.estado = estado;
	}
	
	/**
	 * @return Retorna  estado anterior.
	 */
	public String getEstadoAnterior()
	{
		return this.estadoAnterior;
	}

	/**
	 * @param estadoAnterior asigna estadoAnterior.
	 */
	public void setEstadoAnterior(String estadoAnterior)
	{
		this.estadoAnterior = estadoAnterior;
	}
	
	public String getOcultarCabezotes()
	{
		return this.ocultarCabezotes;
	}
	
	public void setOcultarCabezotes(String ocultarCabezotes)
	{
		this.ocultarCabezotes = ocultarCabezotes;
	}
	
	/**
	 * @return Retorna  columna.
	 */
	public String getColumna()
	{
		return columna;
	}

	/**
	 * @param estado asigna estado.
	 */
	public void setColumna(String columna)
	{
		this.columna = columna;
	}

	public int getNumTratamientos()
	{
		return this.tratamientos.size();
	}
	
	public void setTratamientos(Collection tratamientos)
	{
		this.tratamientos=tratamientos;
	}
	
	public Collection getTratamientos()
	{
		return this.tratamientos;
	}
	
	public void setCodigo(int codigo)
	{
		this.codigo=codigo;
	}
	
	public int getCodigo()
	{
		return codigo;
	}
	
	public InfoDatosInt getTipoTratamiento()
	{
		return this.tipoTratamiento;
	}
	
	public void setTipoTratamiento(InfoDatosInt tipoTratamiento)
	{
		this.tipoTratamiento=tipoTratamiento;
	}
	
	public void setFecIniciacion(String fecIniciacion)
	{
		this.fecIniciacion=fecIniciacion;
	}
	
	public String getFecIniciacion()
	{
		return this.fecIniciacion;
	}
	
	public void setFecFinalizacion(String fecFinalizacion)
	{
		this.fecFinalizacion=fecFinalizacion;
	}
	
	public String getFecFinalizacion()
	{
		return this.fecFinalizacion;
	}
	
	public void setDiagPlanTratamiento(String diagPlanTratamiento)
	{
		this.diagPlanTratamiento=diagPlanTratamiento;
	}
	
	public String getDiagPlanTratamiento()
	{
		return this.diagPlanTratamiento;
	}
	
	public Object getAnalisis(String key)
	{
		return this.analisis.get(key);
	}
	
	public void setAnalisis(String key, Object value)
	{
		this.analisis.put(key, value);
	}
	
	public int getNumAnalisis()
	{
		return this.numAnalisis;
	}
	
	public void setNumAnalisis(int numAnalisis)
	{
		this.numAnalisis=numAnalisis;
	}
	
	public Object getTratamientoDiente(String key)
	{
		return this.tratamientosDientes.get(key);
	}
	
	public void setTratamientoDiente(String key, Object value)
	{
		this.tratamientosDientes.put(key, value);
	}
	
	public int getNumTratamientosDientes()
	{
		return this.numTratamientosDientes;
	}
	
	public void setNumTratamientosDientes(int numTratamientosDientes)
	{
		this.numTratamientosDientes=numTratamientosDientes;
	}
	
	public Object getDienteAusente(String key)
	{
		return this.dientesAusentes.get(key);
	}
	
	public void setDienteAusente(String key, Object value)
	{
		this.dientesAusentes.put(key, value);
	}
	
	public Object getAnalisisDiente(String key)
	{
		return this.analisisDientes.get(key);
	}
	
	public void setAnalisisDiente(String key, Object value)
	{
		this.analisisDientes.put(key, value);
	}
	
	public int getNumAnalisisDientes()
	{
		return this.numAnalisisDientes;
	}
	
	public void setNumAnalisisDientes(int numAnalisisDientes)
	{
		this.numAnalisisDientes=numAnalisisDientes;
	}
	
	/*
	public HashMap getObsSeccion()
	{
		return new HashMap(this.obsSeccion);
	}
	*/
	
	public Object getObsSeccion(String key)
	{
		return this.obsSeccion.get(key);
	}
	
	public void setObsSeccion(String key, Object value)
	{
		this.obsSeccion.put(key, value);
	}
	
	public int getNumObsSeccion()
	{
		return this.numObsSeccion;
	}
	
	public void setNumObsSeccion(int numObsSeccion)
	{
		this.numObsSeccion=numObsSeccion;
	}
	
	/*public void setOdontogramaActual(Odontograma odontograma)
	{
		this.odontogramaActual=odontograma;
	}
	
	public Odontograma getOdontogramaActual()
	{
		return this.odontogramaActual;
	}*/
	
	public ArrayList getOdontogramas()
	{
		return this.odontogramas;
	}
	
	public void setOdontogramas(ArrayList odontogramas)
	{
		this.odontogramas=odontogramas;
	}
	
	public void setOdontograma(int indice, Odontograma odontograma)
	{
		int tamano = this.odontogramas.size();
		
		if(indice>=tamano)  // si intento obtener un objeto que no existe
		{
			for(int j=tamano; j<=indice; j++) // creo elementos en el arreglo hasta que llegue a la posicion que necesito
			{
				Odontograma tempOdontograma = new Odontograma();
				this.odontogramas.add(tempOdontograma);
			}
		}
		this.odontogramas.set(indice, odontograma);
	}
	
	/**
	 * 
	 * @param indice
	 * @return
	 */
	public Odontograma getOdontograma(int indice)
	{
		int tamano = this.odontogramas.size();
		
		if(indice>=tamano)  // si intento obtener un objeto que no existe
		{
			for(int j=tamano; j<=indice; j++) // creo elementos en el arreglo hasta que llegue a la posicion que necesito
			{
				Odontograma tempOdontograma = new Odontograma();
				this.odontogramas.add(tempOdontograma);
			}
		}
		return (Odontograma)this.odontogramas.get(indice);
	}
	
	/**
	 * 
	 * @return
	 */
	public int getNumOdontogramas()
	{
		return this.odontogramas.size();
	}
	
	public void eliminarUltimoOdontograma()
	{
		this.odontogramas.remove(this.odontogramas.size()-1);
	}

	public ArrayList getIndicesPlaca()
	{
		return this.indicesPlaca;
	}
	
	public void setIndicesPlaca(ArrayList indicesPlaca)
	{
		this.indicesPlaca=indicesPlaca;
	}
	
	public void setIndicePlaca(int indice, IndicePlaca indicePlaca)
	{
		int tamano = this.indicesPlaca.size();
		
		if(indice>=tamano)  // si intento obtener un objeto que no existe
		{
			for(int j=tamano; j<=indice; j++) // creo elementos en el arreglo hasta que llegue a la posicion que necesito
			{
				IndicePlaca tempIndicePlaca = new IndicePlaca();
				this.indicesPlaca.add(tempIndicePlaca);
			}
		}
		this.indicesPlaca.set(indice, indicePlaca);
	}
	
	public IndicePlaca getIndicePlaca(int indice)
	{
		int tamano = this.indicesPlaca.size();
		
		if(indice>=tamano)  // si intento obtener un objeto que no existe
		{
			for(int j=tamano; j<=indice; j++) // creo elementos en el arreglo hasta que llegue a la posicion que necesito
			{
				IndicePlaca tempIndicePlaca = new IndicePlaca();
				this.indicesPlaca.add(tempIndicePlaca);
			}
		}
		return (IndicePlaca)this.indicesPlaca.get(indice);
	}
	
	public int getNumIndicesPlaca()
	{
		return this.indicesPlaca.size();
	}
	
	public void eliminarUltimoIndicePlaca()
	{
		this.indicesPlaca.remove(this.indicesPlaca.size()-1);
	}

	public void setNuevaEvolucion(EvolucionOdontologia nuevaEvolucion)
	{
		this.nuevaEvolucion=nuevaEvolucion;
	}
	
	public EvolucionOdontologia getNuevaEvolucion()
	{
		return this.nuevaEvolucion;
	}
	
	public ArrayList getEvoluciones()
	{
		return this.evoluciones;
	}
	
	public void setEvoluciones(ArrayList evoluciones)
	{
		this.evoluciones=evoluciones;
	}
	
	public void setEvolucion(int indice, EvolucionOdontologia evolucion)
	{
		int tamano = this.evoluciones.size();
	
		if(indice>=tamano)  // si intento obtener un objeto que no existe
		{
			for(int j=tamano; j<=indice; j++) // creo elementos en el arreglo hasta que llegue a la posicion que necesito
			{
				EvolucionOdontologia tempEvolucion = new EvolucionOdontologia();
				this.evoluciones.add(tempEvolucion);
			}
		}
		this.evoluciones.set(indice, evolucion);
	}

	public EvolucionOdontologia getEvolucion(int indice)
	{
		int tamano = this.evoluciones.size();
		
		if(indice>=tamano)  // si intento obtener un objeto que no existe
		{
			for(int j=tamano; j<=indice; j++) // creo elementos en el arreglo hasta que llegue a la posicion que necesito
			{
				EvolucionOdontologia tempEvolucion = new EvolucionOdontologia();
				this.evoluciones.add(tempEvolucion);
			}
		}
		return (EvolucionOdontologia)this.evoluciones.get(indice);
	}
	
	public int getNumEvoluciones()
	{
		return this.evoluciones.size();
	}

	/**
	 * @return Returns the nuevoProcedimiento.
	 */
	public Procedimiento getNuevoProcedimiento()
	{
		return nuevoProcedimiento;
	}

	/**
	 * @param nuevoProcedimiento The nuevoProcedimiento to set.
	 */
	public void setNuevoProcedimiento(Procedimiento nuevoProcedimiento)
	{
		this.nuevoProcedimiento = nuevoProcedimiento;
	}

	/**
	 * @return Returns the procedimientos.
	 */
	public ArrayList getProcedimientos()
	{
		return procedimientos;
	}

	/**
	 * @param procedimientos The procedimientos to set.
	 */
	public void setProcedimientos(ArrayList procedimientos)
	{
		this.procedimientos = procedimientos;
	}

	public void setProcedimiento(int indice, Procedimiento procedimiento)
	{
		int tamano = this.procedimientos.size();
	
		if(indice>=tamano)  // si intento obtener un objeto que no existe
		{
			for(int j=tamano; j<=indice; j++) // creo elementos en el arreglo hasta que llegue a la posicion que necesito
			{
				Procedimiento tempProcedimiento = new Procedimiento();
				this.procedimientos.add(tempProcedimiento);
			}
		}
		this.procedimientos.set(indice, procedimiento);
	}

	public Procedimiento getProcedimiento(int indice)
	{
		int tamano = this.procedimientos.size();
		
		if(indice>=tamano)  // si intento obtener un objeto que no existe
		{
			for(int j=tamano; j<=indice; j++) // creo elementos en el arreglo hasta que llegue a la posicion que necesito
			{
				Procedimiento tempProcedimiento = new Procedimiento();
				this.procedimientos.add(tempProcedimiento);
			}
		}
		return (Procedimiento)this.procedimientos.get(indice);
	}
	
	public int getNumProcedimientos()
	{
		return this.procedimientos.size();
	}

	/**
	 * @return Returns the consentimientosInformados.
	 */
	public ArrayList getConsentimientosInformados()
	{
		return consentimientosInformados;
	}

	/**
	 * @param consentimientosInformados The consentimientosInformados to set.
	 */
	public void setConsentimientosInformados(ArrayList consentimientosInformados)
	{
		this.consentimientosInformados = consentimientosInformados;
	}
	
	/**
	 * Retorna la cantidad de consentimientos informados
	 * @return
	 */
	public int getNumConsentimientosInformados()
	{
		return this.consentimientosInformados.size();
	}
	
	/**
	 * Establece consentimiento informado en el indice i
	 * @param indice
	 * @param consentimientoInformado
	 */
	public void setConsentimientoInformado(int indice, ConsentimientoInformado consentimientoInformado)
	{
		int tamano = this.consentimientosInformados.size();
		
		if(indice>=tamano)  // si intento obtener un objeto que no existe
		{
			for(int j=tamano; j<=indice; j++) // creo elementos en el arreglo hasta que llegue a la posicion que necesito
			{
				ConsentimientoInformado tempConsentimientoInformado = new ConsentimientoInformado();
				this.consentimientosInformados.add(tempConsentimientoInformado);
			}
		}
		this.consentimientosInformados.set(indice, consentimientoInformado);
	}

	public ConsentimientoInformado getConsentimientoInformado(int indice)
	{
		int tamano = this.consentimientosInformados.size();
		
		if(indice>=tamano)  // si intento obtener un objeto que no existe
		{
			for(int j=tamano; j<=indice; j++) // creo elementos en el arreglo hasta que llegue a la posicion que necesito
			{
				ConsentimientoInformado tempConsentimientoInformado = new ConsentimientoInformado();
				this.consentimientosInformados.add(tempConsentimientoInformado);
			}
		}
		return (ConsentimientoInformado)this.consentimientosInformados.get(indice);
	}

	/**
	 * @return Returns the nuevoConsentimientoInformado.
	 */
	public ConsentimientoInformado getNuevoConsentimientoInformado()
	{
		return nuevoConsentimientoInformado;
	}

	/**
	 * @param nuevoConsentimientoInformado The nuevoConsentimientoInformado to set.
	 */
	public void setNuevoConsentimientoInformado(
			ConsentimientoInformado nuevoConsentimientoInformado)
	{
		this.nuevoConsentimientoInformado = nuevoConsentimientoInformado;
	}

	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		this.erroresConsentimientos=false;
		this.erroresEvoluciones=false;
		this.erroresPlanTratamiento=false;
		ActionErrors errores = new ActionErrors();
		
		
		
		if(estado.equals("guardarNuevo")||estado.equals("guardarCambios"))
		{
			
			if (this.especialidadSeleccionada==null || this.especialidadSeleccionada.equals(ConstantesBD.codigoNuncaValido)) {
				errores.add(
						"especialidad obligatoria",
						new ActionMessage("errors.required", messageResource
								.getMessage("tratamientos_odontologicos_error_especialidad_obligatoria")));
				
			}
			/*
			if(UtilidadTexto.isEmpty(this.diagPlanTratamiento)){
				errores.add("El diagnostico no puede ser vacio",
						new ActionMessage("errors.required",  messageResource
								.getMessage("tratamientos_odontologicos_error_diagnostico")));
			}
			*/
		
		}
		
		if(estado.equals("guardarNuevo"))
		{
			
		
			
		
				
			
			if( this.getTipoTratamiento().getCodigo()<0 )
			{
				errores.add("El tipo de tratamiento no puede ser vacio", new ActionMessage("errors.required", "Tipo de tratamiento"));
			}
			
			if(UtilidadCadena.noEsVacio(this.getFecIniciacion()))
			{
				if(UtilidadFecha.esFechaValidaSegunAp(this.getFecIniciacion()))
				{
					if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.getFecIniciacion(), UtilidadFecha.getFechaActual()))
					{
						errores.add("La fecha de iniciación debe ser menor o igual que la fecha actual", new ActionMessage("errors.fechaPosteriorAOtraDeReferencia", "de iniciación", "actual"));
					}
				}
				else
				{
					errores.add("La fecha de iniciación es invalida", new ActionMessage("errors.formatoFechaInvalido", "de iniciación"));
				}
			}
			else
			{
				errores.add("La fecha de iniciacion no puede ser vacia", new ActionMessage("errors.required", "fecha de iniciación"));
			}

			/* XPlanner 2008 163952
			 * if(!UtilidadCadena.noEsVacio(this.getMotivoConsulta()))
			{ 
				errores.add("El motivo de la consulta no puede ser vacío", new ActionMessage("errors.required", "Motivo Consulta y Enfermedad Actual"));
			}*/
			
			if(hallazgosOtros.size()>0)
			{
				int i=0;
				for(DtoOtroHallazgo hallazgo:hallazgosOtros)
				{
					if(hallazgo.getPieza().getCodigoPk()<=0)
					{
						errores.add("Pieza requerida", new ActionMessage("errors.required", "Sección Otros Hallazgos - Piezas Dentales: La pieza dental para el registro "+(i+1)));
					}
					if(UtilidadTexto.isEmpty(hallazgo.getHallazgo().getConsecutivo()))
					{
						errores.add("Hallazgo requerido", new ActionMessage("errors.required", "Sección Otros Hallazgos - Piezas Dentales: El hallazgo para el registro "+(i+1)));
					}
					if(hallazgo.getSuperficie().getCodigoPk()<=0 && hallazgo.getHallazgo().getAplica_a().equals(ConstantesIntegridadDominio.acronimoAplicaASuperficie))
					{
						errores.add("Superficie requerido", new ActionMessage("errors.required", "Sección Otros Hallazgos - Piezas Dentales: La superficie para el registro "+(i+1)));
					}
					int j;
					for(j=i+1; j<hallazgosOtros.size(); j++)
					{
						DtoOtroHallazgo hallazgoEvaludaoParaRepetido=hallazgosOtros.get(j);
						if(
								hallazgo.getPieza().getCodigoPk()==hallazgoEvaludaoParaRepetido.getPieza().getCodigoPk()
							&&
								hallazgo.getHallazgo().getConsecutivo().equals(hallazgoEvaludaoParaRepetido.getHallazgo().getConsecutivo())
							&&
								hallazgo.getSuperficie().getCodigoPk()==hallazgoEvaludaoParaRepetido.getSuperficie().getCodigoPk()
						)
						{
							errores.add("Registros repetidos", new ActionMessage("error.tratamientosodontologicos.hallazgosuperficiedienterepetidoDiente", (i+1), (j+1)));
						}
					}
					i++;
				}
			}
			if(hallazgosBoca.size()>0)
			{
				int i=0;
				for(DtoOtroHallazgo hallazgo:hallazgosBoca)
				{
					if(UtilidadTexto.isEmpty(hallazgo.getHallazgo().getConsecutivo()))
					{
						errores.add("Hallazgo requerido", new ActionMessage("errors.required", "Sección Otros Hallazgos - Piezas Dentales: El hallazgo para el registro "+(i+1)));
					}
					int j;
					for(j=i+1; j<hallazgosBoca.size(); j++)
					{
						DtoOtroHallazgo hallazgoEvaludaoParaRepetido=hallazgosBoca.get(j);
						if(
								hallazgo.getPieza().getCodigoPk()==hallazgoEvaludaoParaRepetido.getPieza().getCodigoPk()
							&&
								hallazgo.getHallazgo().getConsecutivo().equals(hallazgoEvaludaoParaRepetido.getHallazgo().getConsecutivo())
							&&
								hallazgo.getSuperficie().getCodigoPk()==hallazgoEvaludaoParaRepetido.getSuperficie().getCodigoPk()
						)
						{
							errores.add("Registros repetidos", new ActionMessage("error.tratamientosodontologicos.hallazgosuperficiedienterepetidoBoca", (i+1), (j+1)));
						}
					}
					i++;
				}				
			}
			
			if(!errores.isEmpty())
			{
				estado="ingresando";
			}
		}
		
		
	
		
		
		
		
		
		
		
		
		
		return errores;
	}
	
	public void setDatosMedico(String datosMedico)
	{
		this.datosMedico=datosMedico;
	}
	
	public String getDatosMedico()
	{
		return this.datosMedico;
	}
	
	public void setObservacionesAnteriores(String observacionesAnteriores)
	{
		this.observacionesAnteriores=observacionesAnteriores;
	}
	
	public String getObservacionesAnteriores()
	{
		return this.observacionesAnteriores;
	}
	
	public void setObservacionesNuevas(String observacionesNuevas)
	{
		this.observacionesNuevas=observacionesNuevas;
	}
	
	public String getObservacionesNuevas()
	{
		return this.observacionesNuevas;
	}
	
	public void ordenarTratamientos()
	{
	  String columnaordenar="";		
		if(UtilidadCadena.noEsVacio(this.columna))
		{   
			if(this.ultimoOrdenamiento != null && this.ultimoOrdenamiento.equals(this.columna))
			{
				columnaordenar=this.columna+"_descendente";
			}else
			{
				columnaordenar=this.columna;
			}
			this.setTratamientos(UtilidadDynaCollection.ordenar(this.getTratamientos(),columnaordenar));
			this.ultimoOrdenamiento=this.columna;
		}
			
	}
	
	/**
	 * 
	 */
	public void limpiarSecciones()
	{
		this.evoluciones=new ArrayList();
		this.nuevaEvolucion=new EvolucionOdontologia();
		this.analisis=new HashMap();
		this.numAnalisis=0;
		this.obsSeccion=new HashMap();
		this.numObsSeccion=0;
		this.odontogramas=new ArrayList();
		this.analisisDientes=new HashMap();
		this.numAnalisisDientes=0;
		this.tratamientosDientes=new HashMap();
		this.numTratamientosDientes=0;
		this.indicesPlaca=new ArrayList();
		this.procedimientos=new ArrayList();
		this.procedimientosTemporal= new ArrayList();
		this.nuevoProcedimiento=new Procedimiento();

		this.estadoSeccionConsentimientos="listando";
		this.estadoSeccionEvoluciones="listando";
		this.estadoSeccionPlanTratamiento="listando";
		this.consentimientosInformados=new ArrayList();
		this.nuevoConsentimientoInformado= new ConsentimientoInformado();
		this.dientesAusentes = new HashMap();
		this.erroresPlanTratamiento=false;
		this.erroresConsentimientos=false;
		this.erroresEvoluciones=false;
	}
	
	/**
	 * 
	 */
	public void clean()
	{
		this.codigo=-1;
		this.tipoTratamiento=new InfoDatosInt();
		this.fecIniciacion=UtilidadFecha.getFechaActual();
		this.fecFinalizacion="";
		this.diagPlanTratamiento="";
		this.motivoConsulta="";
		this.evoluciones=new ArrayList();
		this.observacionesAnteriores="";
		this.observacionesNuevas="";
		this.nuevaEvolucion=new EvolucionOdontologia();
		this.estado="cargar"; // el estado por defecto es cargar
		this.estadoAnterior="";
		this.analisis=new HashMap();
		this.numAnalisis=0;
		this.obsSeccion=new HashMap();
		this.numObsSeccion=0;
		this.odontogramas=new ArrayList();
		this.indiceOdontograma = 0;
		
		this.analisisDientes=new HashMap();
		this.numAnalisisDientes=0;
		this.tratamientosDientes=new HashMap();
		this.numTratamientosDientes=0;
		this.indicesPlaca=new ArrayList();
		this.procedimientos=new ArrayList();
		this.procedimientosTemporal= new ArrayList();
		this.nuevoProcedimiento=new Procedimiento();
		
		this.estadoSeccionConsentimientos="listando";
		this.estadoSeccionEvoluciones="listando";
		this.estadoSeccionPlanTratamiento="listando";
		this.consentimientosInformados=new ArrayList();
		this.nuevoConsentimientoInformado= new ConsentimientoInformado();
		
		this.dientesAusentes = new HashMap();

		this.erroresPlanTratamiento=false;
		this.erroresConsentimientos=false;
		this.erroresEvoluciones=false;
				
		this.diagnosticosCartadentalMap=new HashMap();
		this.diagnosticosCartadentalMap.put("numRegistros", "0");  
		this.tratamientosCartadentalMap=new HashMap();
		this.tratamientosCartadentalMap.put("numRegistros", "0");  
		this.codTratamientoCartDental=new String("");
		this.nuevosHallazgos=new String("");
		this.antiguosHallazgos=new String("");
		this.diagnosticosAdd = new String[100];
		this.tratamientoAdd  = new String[100];
		this.dtoCartaDental = new DtoCartaDental();
		this.numeroDienteIndex = ConstantesBD.codigoNuncaValido;
		
		this.hallazgosOtros=new ArrayList<DtoOtroHallazgo>();
		this.hallazgosBoca=new ArrayList<DtoOtroHallazgo>();
		this.hallazgoEliminado=ConstantesBD.codigoNuncaValido;
		
		this.xmlOdontograma=new String();
		this.infoEncabezadoReporte= new InfoEncabezadoReporte();
		this.mostrarSeccionIndicePlaca=ConstantesBD.acronimoNo;
		this.listaEspecialidades= new ArrayList<DtoEspecialidades>();
		this.especialidadSeleccionada=new Integer(0);
		this.imagen="";
		this.pintar="hidden";
		this.descripcionEspecialidad="";
		this.nuevaObservacion	= "";
		this.observacionIndicePlacaInicial = "";
		//this.observacionPlanTratamiento = "";
	}

	public String getEstadoSeccionConsentimientos()
	{
		return estadoSeccionConsentimientos;
	}

	public void setEstadoSeccionConsentimientos(String estadoSeccionConsentimientos)
	{
		this.estadoSeccionConsentimientos = estadoSeccionConsentimientos;
	}

	public String getEstadoSeccionEvoluciones()
	{
		return estadoSeccionEvoluciones;
	}

	public void setEstadoSeccionEvoluciones(String estadoSeccionEvoluciones)
	{
		this.estadoSeccionEvoluciones = estadoSeccionEvoluciones;
	}

	public String getEstadoSeccionPlanTratamiento()
	{
		return estadoSeccionPlanTratamiento;
	}

	public void setEstadoSeccionPlanTratamiento(String estadoSeccionPlanTratamiento)
	{
		this.estadoSeccionPlanTratamiento = estadoSeccionPlanTratamiento;
	}

	public boolean getErroresConsentimientos()
	{
		return erroresConsentimientos;
	}

	public void setErroresConsentimientos(boolean erroresConsentimientos)
	{
		this.erroresConsentimientos = erroresConsentimientos;
	}

	public boolean getErroresEvoluciones()
	{
		return erroresEvoluciones;
	}

	public void setErroresEvoluciones(boolean erroresEvoluciones)
	{
		this.erroresEvoluciones = erroresEvoluciones;
	}

	public boolean getErroresPlanTratamiento()
	{
		return erroresPlanTratamiento;
	}

	public void setErroresPlanTratamiento(boolean erroresPlanTratamiento)
	{
		this.erroresPlanTratamiento = erroresPlanTratamiento;
	}

	public HashMap getDiagnosticosCartadentalMap() {
		return diagnosticosCartadentalMap;
	}

	public void setDiagnosticosCartadentalMap(HashMap diagnosticosCartadentalMap) {
		this.diagnosticosCartadentalMap = diagnosticosCartadentalMap;
	}	
	
	public HashMap getTratamientosCartadentalMap() {
		return tratamientosCartadentalMap;
	}

	public void setTratamientosCartadentalMap(HashMap tratamientosCartadentalMap) {
		this.tratamientosCartadentalMap = tratamientosCartadentalMap;
	}

	public String[] getDiagnosticosAdd() {
		return diagnosticosAdd;
	}

	public void setDiagnosticosAdd(String[] diagnosticosAdd) {
		this.diagnosticosAdd = diagnosticosAdd;
	}

	public String[] getTratamientoAdd() {
		return tratamientoAdd;
	}

	public void setTratamientoAdd(String[] tratamientoAdd) {
		this.tratamientoAdd = tratamientoAdd;
	}

	public DtoCartaDental getDtoCartaDental() {
		return dtoCartaDental;
	}

	public void setDtoCartaDental(DtoCartaDental dtoCartaDental) {
		this.dtoCartaDental = dtoCartaDental;
	}

	public int getNumeroDienteIndex() {
		return numeroDienteIndex;
	}

	public void setNumeroDienteIndex(int numeroDienteIndex) {
		this.numeroDienteIndex = numeroDienteIndex;
	}

	public HashMap getSuperficiesDienteMap() {
		return superficiesDienteMap;
	}

	public void setSuperficiesDienteMap(HashMap superficiesDienteMap) {
		this.superficiesDienteMap = superficiesDienteMap;
	}

	public String getCodTratamientoCartDental() {
		return codTratamientoCartDental;
	}

	public void setCodTratamientoCartDental(String codTratamientoCartDental) {
		this.codTratamientoCartDental = codTratamientoCartDental;
	}

	public String getAntiguosHallazgos() {
		return antiguosHallazgos;
	}

	public void setAntiguosHallazgos(String antiguosHallazgos) {
		this.antiguosHallazgos = antiguosHallazgos;
	}

	public String getNuevosHallazgos() {
		return nuevosHallazgos;
	}

	public void setNuevosHallazgos(String nuevosHallazgos) {
		this.nuevosHallazgos = nuevosHallazgos;
	}

	/**
	 * @return the ultimoOrdenamiento
	 */
	public String getUltimoOrdenamiento() {
		return ultimoOrdenamiento;
	}

	/**
	 * @param ultimoOrdenamiento the ultimoOrdenamiento to set
	 */
	public void setUltimoOrdenamiento(String ultimoOrdenamiento) {
		this.ultimoOrdenamiento = ultimoOrdenamiento;
	}

	/**
	 * @return Retorna el atributo hallazgosOtros
	 */
	public ArrayList<DtoOtroHallazgo> getHallazgosOtros()
	{
		return hallazgosOtros;
	}

	/**
	 * @param hallazgosOtros Asigna el atributo hallazgosOtros
	 */
	public void setHallazgosOtros(ArrayList<DtoOtroHallazgo> hallazgosOtros)
	{
		this.hallazgosOtros = hallazgosOtros;
	}

	/**
	 * @return Retorna el atributo hallazgosBoca
	 */
	public ArrayList<DtoOtroHallazgo> getHallazgosBoca()
	{
		return hallazgosBoca;
	}

	/**
	 * @param hallazgosBoca Asigna el atributo hallazgosBoca
	 */
	public void setHallazgosBoca(ArrayList<DtoOtroHallazgo> hallazgosBoca)
	{
		this.hallazgosBoca = hallazgosBoca;
	}

	/**
	 * @return Retorna el atributo hallazgoEliminado
	 */
	public int getHallazgoEliminado()
	{
		return hallazgoEliminado;
	}

	/**
	 * @param hallazgoEliminado Asigna el atributo hallazgoEliminado
	 */
	public void setHallazgoEliminado(int hallazgoEliminado)
	{
		this.hallazgoEliminado = hallazgoEliminado;
	}

	/**
	 * @return Retorna el atributo xmlOdontograma
	 */
	public String getXmlOdontograma()
	{
		return xmlOdontograma;
	}

	/**
	 * @param xmlOdontograma Asigna el atributo xmlOdontograma
	 */
	public void setXmlOdontograma(String xmlOdontograma)
	{
		this.xmlOdontograma = xmlOdontograma;
	}

	/**
	 * @return Retorna el atributo xmlHallazgoDiente
	 */
	public String getXmlHallazgoDiente()
	{
		return xmlHallazgoDiente;
	}

	/**
	 * @param xmlHallazgoDiente Asigna el atributo xmlHallazgoDiente
	 */
	public void setXmlHallazgoDiente(String xmlHallazgoDiente)
	{
		this.xmlHallazgoDiente = xmlHallazgoDiente;
	}

	/**
	 * @return Retorna el atributo xmlHallazgoSuperficie
	 */
	public String getXmlHallazgoSuperficie()
	{
		return xmlHallazgoSuperficie;
	}

	/**
	 * @param xmlHallazgoSuperficie Asigna el atributo xmlHallazgoSuperficie
	 */
	public void setXmlHallazgoSuperficie(String xmlHallazgoSuperficie)
	{
		this.xmlHallazgoSuperficie = xmlHallazgoSuperficie;
	}

	/**
	 * @return Retorna el atributo listaPiezasDentales
	 */
	public ArrayList<Integer> getListaPiezasDentales()
	{
		return listaPiezasDentales;
	}

	/**
	 * @param listaPiezasDentales Asigna el atributo listaPiezasDentales
	 */
	public void setListaPiezasDentales(ArrayList<Integer> listaPiezasDentales)
	{
		this.listaPiezasDentales = listaPiezasDentales;
	}

	/**
	 * @return Retorna el atributo arrayHallazgosDiente
	 */
	public ArrayList<DtoHallazgoOdontologico> getArrayHallazgosDiente()
	{
		return arrayHallazgosDiente;
	}

	/**
	 * @param arrayHallazgosDiente Asigna el atributo arrayHallazgosDiente
	 */
	public void setArrayHallazgosDiente(
			ArrayList<DtoHallazgoOdontologico> arrayHallazgosDiente)
	{
		this.arrayHallazgosDiente = arrayHallazgosDiente;
	}

	/**
	 * @return Retorna el atributo arrayHallazgosSuperficie
	 */
	public ArrayList<DtoHallazgoOdontologico> getArrayHallazgosSuperficie()
	{
		return arrayHallazgosSuperficie;
	}

	/**
	 * @param arrayHallazgosSuperficie Asigna el atributo arrayHallazgosSuperficie
	 */
	public void setArrayHallazgosSuperficie(
			ArrayList<DtoHallazgoOdontologico> arrayHallazgosSuperficie)
	{
		this.arrayHallazgosSuperficie = arrayHallazgosSuperficie;
	}

	/**
	 * @return Retorna el atributo arrayHallazgosBoca
	 */
	public ArrayList<DtoHallazgoOdontologico> getArrayHallazgosBoca()
	{
		return arrayHallazgosBoca;
	}

	/**
	 * @param arrayHallazgosBoca Asigna el atributo arrayHallazgosBoca
	 */
	public void setArrayHallazgosBoca(
			ArrayList<DtoHallazgoOdontologico> arrayHallazgosBoca)
	{
		this.arrayHallazgosBoca = arrayHallazgosBoca;
	}

	public int getTipoHallazgoSeleccionado() {
		return tipoHallazgoSeleccionado;
	}

	public void setTipoHallazgoSeleccionado(int tipoHallazgoSeleccionado) {
		this.tipoHallazgoSeleccionado = tipoHallazgoSeleccionado;
	}

	public int getIndiceHallazgoSeleccionado() {
		return indiceHallazgoSeleccionado;
	}

	public void setIndiceHallazgoSeleccionado(int indiceHallazgoSeleccionado) {
		this.indiceHallazgoSeleccionado = indiceHallazgoSeleccionado;
	}

	public int getHallazgoSeleccionado() {
		return hallazgoSeleccionado;
	}

	public void setHallazgoSeleccionado(int hallazgoSeleccionado) {
		this.hallazgoSeleccionado = hallazgoSeleccionado;
	}

	public int getCodigoPiezaSeleccionada() {
		return codigoPiezaSeleccionada;
	}

	public void setCodigoPiezaSeleccionada(int codigoPiezaSeleccionada) {
		this.codigoPiezaSeleccionada = codigoPiezaSeleccionada;
	}

	public ArrayList<DtoSectorSuperficieCuadrante> getArraySuperficies() {
		return arraySuperficies;
	}

	public void setArraySuperficies(
			ArrayList<DtoSectorSuperficieCuadrante> arraySuperficies) {
		this.arraySuperficies = arraySuperficies;
	}

	public int getCodigoSuperficieSeleccionada() {
		return codigoSuperficieSeleccionada;
	}

	public void setCodigoSuperficieSeleccionada(int codigoSuperficieSeleccionada) {
		this.codigoSuperficieSeleccionada = codigoSuperficieSeleccionada;
	}
	
	public ArrayList<Integer> getListadoDientes()
	{
		ArrayList<Integer> listadoDientes=new ArrayList<Integer>();
		for(DtoSectorSuperficieCuadrante superficie:arraySuperficies)
		{
			if(!listadoDientes.contains(superficie.getPieza()))
			{
				listadoDientes.add(superficie.getPieza());
			}
		}
		return listadoDientes;
	}

	/**
	 * @return Retorna atributo arrayInterpretacionIndicePlaca
	 */
	public ArrayList<DtoInterpretacionIndicePlaca> getArrayInterpretacionIndicePlaca()
	{
		return arrayInterpretacionIndicePlaca;
	}

	/**
	 * @param arrayInterpretacionIndicePlaca Asigna atributo arrayInterpretacionIndicePlaca
	 */
	public void setArrayInterpretacionIndicePlaca(
			ArrayList<DtoInterpretacionIndicePlaca> arrayInterpretacionIndicePlaca)
	{
		this.arrayInterpretacionIndicePlaca = arrayInterpretacionIndicePlaca;
	}

	/**
	 * @return Retorna atributo motivoConsulta
	 */
	public String getMotivoConsulta()
	{
		return motivoConsulta;
	}

	/**
	 * @param motivoConsulta Asigna atributo motivoConsulta
	 */
	public void setMotivoConsulta(String motivoConsulta)
	{
		this.motivoConsulta = motivoConsulta;
	}

	/**
	 * Establece el valor del atributo infoEncabezadoReporte
	 *
	 * @param valor para el atributo infoEncabezadoReporte
	 */
	public void setInfoEncabezadoReporte(InfoEncabezadoReporte infoEncabezadoReporte)
	{
		this.infoEncabezadoReporte = infoEncabezadoReporte;
	}

	/**
	 * Obtiene el valor del atributo infoEncabezadoReporte
	 *
	 * @return Retorna atributo infoEncabezadoReporte
	 */
	public InfoEncabezadoReporte getInfoEncabezadoReporte()
	{
		return infoEncabezadoReporte;
	}

	/**
	 * Establece el valor del atributo mostrarSeccionIndicePlaca
	 *
	 * @param valor para el atributo mostrarSeccionIndicePlaca
	 */
	public void setMostrarSeccionIndicePlaca(String mostrarSeccionIndicePlaca)
	{
		this.mostrarSeccionIndicePlaca = mostrarSeccionIndicePlaca;
	}

	/**
	 * Obtiene el valor del atributo mostrarSeccionIndicePlaca
	 *
	 * @return Retorna atributo mostrarSeccionIndicePlaca
	 */
	public String getMostrarSeccionIndicePlaca()
	{
		return mostrarSeccionIndicePlaca;
	}

	/**
	 * Establece el valor del atributo imagen
	 *
	 * @param valor para el atributo imagen
	 */
	public void setImagen(String imagen)
	{
		this.imagen = imagen;
	}

	/**
	 * Obtiene el valor del atributo imagen
	 *
	 * @return Retorna atributo imagen
	 */
	public String getImagen()
	{
		return imagen;
	}

	/**
	 * Obtiene el valor del atributo indexOdontograma
	 *
	 * @return Retorna atributo indexOdontograma
	 */
	public int getIndiceOdontograma()
	{
		return indiceOdontograma;
	}

	/**
	 * Establece el valor del atributo indexOdontograma
	 *
	 * @param valor para el atributo indexOdontograma
	 */
	public void setIndiceOdontograma(int indexOdontograma)
	{
		this.indiceOdontograma = indexOdontograma;
	}

	/**
	 * @return the listaEspecialidades
	 */
	public ArrayList<DtoEspecialidades> getListaEspecialidades() {
		return listaEspecialidades;
	}

	/**
	 * @param listaEspecialidades the listaEspecialidades to set
	 */
	public void setListaEspecialidades(
			ArrayList<DtoEspecialidades> listaEspecialidades) {
		this.listaEspecialidades = listaEspecialidades;
	}

	/**
	 * @return the especialidadSeleccionada
	 */
	public Integer getEspecialidadSeleccionada() {
		return especialidadSeleccionada;
	}

	/**
	 * @param especialidadSeleccionada the especialidadSeleccionada to set
	 */
	public void setEspecialidadSeleccionada(Integer especialidadSeleccionada) {
		this.especialidadSeleccionada = especialidadSeleccionada;
	}

	/**
	 * @return the nombreApellidoProfesinalSalud
	 */
	public String getNombreApellidoProfesinalSalud() {
		return nombreApellidoProfesinalSalud;
	}

	/**
	 * @param nombreApellidoProfesinalSalud the nombreApellidoProfesinalSalud to set
	 */
	public void setNombreApellidoProfesinalSalud(
			String nombreApellidoProfesinalSalud) {
		this.nombreApellidoProfesinalSalud = nombreApellidoProfesinalSalud;
	}

	/**
	 * @return the numeroRegistro
	 */
	public String getNumeroRegistro() {
		return numeroRegistro;
	}

	/**
	 * @param numeroRegistro the numeroRegistro to set
	 */
	public void setNumeroRegistro(String numeroRegistro) {
		this.numeroRegistro = numeroRegistro;
	}

	/**
	 * @return the descripcionEspecialidadUnica
	 */
	public String getDescripcionEspecialidadUnica() {
		return descripcionEspecialidadUnica;
	}

	/**
	 * @param descripcionEspecialidadUnica the descripcionEspecialidadUnica to set
	 */
	public void setDescripcionEspecialidadUnica(String descripcionEspecialidadUnica) {
		this.descripcionEspecialidadUnica = descripcionEspecialidadUnica;
	}

	/**
	 * @return the informacionProfesionalMedico
	 */
	public String getInformacionProfesionalMedico() {
		return informacionProfesionalMedico;
	}

	/**
	 * @param informacionProfesionalMedico the informacionProfesionalMedico to set
	 */
	public void setInformacionProfesionalMedico(String informacionProfesionalMedico) {
		this.informacionProfesionalMedico = informacionProfesionalMedico;
	}

	/**
	 * @return the permiteEditar
	 */
	public Boolean getPermiteEditar() {
		return permiteEditar;
	}

	/**
	 * @param permiteEditar the permiteEditar to set
	 */
	public void setPermiteEditar(Boolean permiteEditar) {
		this.permiteEditar = permiteEditar;
	}

	/**
	 * @return the pintar
	 */
	public String getPintar() {
		return pintar;
	}

	/**
	 * @param pintar the pintar to set
	 */
	public void setPintar(String pintar) {
		this.pintar = pintar;
	}

	/**
	 * @return the descripcionEspecialidad
	 */
	public String getDescripcionEspecialidad() {
		return descripcionEspecialidad;
	}

	/**
	 * @param descripcionEspecialidad the descripcionEspecialidad to set
	 */
	public void setDescripcionEspecialidad(String descripcionEspecialidad) {
		this.descripcionEspecialidad = descripcionEspecialidad;
	}

	/**
	 * @return valor de nuevaObservacion
	 */
	public String getNuevaObservacion() {
		return nuevaObservacion;
	}

	/**
	 * @param nuevaObservacion el nuevaObservacion para asignar
	 */
	public void setNuevaObservacion(String nuevaObservacion) {
		this.nuevaObservacion = nuevaObservacion;
	}

	/**
	 * @return valor de observacionIndicePlacaInicial
	 */
	public String getObservacionIndicePlacaInicial() {
		return observacionIndicePlacaInicial; 
	}

	/**
	 * @param observacionIndicePlacaInicial el observacionIndicePlacaInicial para asignar
	 */
	public void setObservacionIndicePlacaInicial(String observacionIndicePlacaInicial) {
		this.observacionIndicePlacaInicial = observacionIndicePlacaInicial; 
	}

	
	/**
	 * Organiza los indices de placa de tal manera que el primer registro (registro en posicion 0)
	 *  sea el indice de placa inicial, ya que en la presentación siempre se toma el primer registro
	 *  como el indice de placa inicial
	 *
	 * @autor Cristhian Murillo
	*/
	public void ordenarIndicesPlaca() 
	{
		SortGenerico sortG = new SortGenerico("Codigo",true);
		Collections.sort(this.indicesPlaca,sortG);
	}

	/**
	 * @return valor de observacionPlanTratamiento
	 */
	public String getObservacionPlanTratamiento() {
		return observacionPlanTratamiento;
	}

	/**
	 * @param observacionPlanTratamiento el observacionPlanTratamiento para asignar
	 */
	public void setObservacionPlanTratamiento(String observacionPlanTratamiento) {
		this.observacionPlanTratamiento = observacionPlanTratamiento;
	}


	
	
	
	
	
	
	/**
	 * Asigna la misma observación general a todos los procedimientos
	 * @param forma
	 *
	 * @autor Cristhian Murillo
	*/
	public void asignarObservacionProcedimientos()
	{
		int tamano = this.procedimientos.size();
		
		if(tamano > 0)  
		{
			for(int i=0; i<tamano; i++)
			{
				Procedimiento procedimiento = new Procedimiento();
				procedimiento = (Procedimiento)this.procedimientos.get(i);
				
				if(!UtilidadTexto.isEmpty(this.observacionPlanTratamiento)){
					if(procedimiento.getCodigoCUPS() == this.nuevoProcedimiento.getCodigoCUPS() && procedimiento.getCodigoServicio() == this.nuevoProcedimiento.getCodigoServicio())
					{
						procedimiento.setObsNuevas(this.observacionPlanTratamiento);
					}
				}
			}
		}
	}

	/**
	 * @return valor de procedimientosTemporal
	 */
	public ArrayList getProcedimientosTemporal() {
		return procedimientosTemporal;
	}

	/**
	 * @param procedimientosTemporal el procedimientosTemporal para asignar
	 */
	public void setProcedimientosTemporal(ArrayList procedimientosTemporal) {
		this.procedimientosTemporal = procedimientosTemporal;
	}

}