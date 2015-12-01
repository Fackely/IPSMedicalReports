/*
 * Creado en  21/06/2005
 *
 */
package com.sies.actionform;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import util.UtilidadFecha;

/**
 * @author Karenth Yuliana Marín
 * Modificado Juan David Ramírez
 * 
 * Creado en 21/06/2005
 * SiEs
 * Parquesoft Manizales
 */
public class GenerarCuadroTurnosForm extends ActionForm
{
	/**
	 * Serial version UID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Para manejar el estado en el que va a realizar la accion
	 */
	protected String estado; 
	
	/**
	 * codigo de la categoria
	 */
	protected int codigoCategoria;
	
	/**
	 * para manejar los nombres de las enfermeras en el textArea
	 */
	private String asignaciones;
	
	/**
	 * Para manejar las asignaciones de las enfermeras en las categorias
	 */
	private HashMap<String, Object> asignadas;
	
	/**
	 * Para manejar la consulta de las enfermeras
	 */
	private Collection<HashMap<String, Object>> listadoPersonas;

	/**
	 * Listado de categorías
	 */
	private Collection<HashMap<String, Object>> listadoCategorias;

	/**
	 * fecha de incio de la programacion del cuadro de turnos
	 */
	private String fechaInicio;
	
	/**
	 * Fecha finalizacion de la programación
	 */
	private String fechaFin;
	
	/**
	 * Fecha para listar los cuadros de turnos que la contengan
	 */
	private String fechaBusquedaEliminar;
	
	/**
	 * Listado de cuadros de turnos para eliminar
	 */
	private Collection<HashMap<String, Object>> listadoCuadros;
	
	/**
	 * Para manejar la consulta de las restricciones asociadas a una categoria
	 */
	private Collection<HashMap<String, Object>> listadoCategoriaRestriccion;
		
	/**
	 * Para manejar el codigo de la enfermera
	 */
	protected int codigoEnfermera;
	
	/**
	 * observacion que se hace al cuadro de turnos
	 */
	private String observacion;
	
	/**
	 * Fecha en que se le esta programando la observación a la enfermera
	 */
	private String fechaObservacion;
	
	/**
	 * Para guardar la cantidad de fechas que se van a mostrar
	 */
	private int cantFechas=0;
	
	/**
	 * Para manejar la antidad de Profesionales que se van a mostrar
	 */
	private int cantProfesionales=0;
	
	/**
	 * Mostrar la lista de los turnos parametrizados en el sistema
	 */
	private Collection<HashMap<String, Object>> turnosParametrizados=null;

	/**
	 * Mostrar la lista de los turnos activos parametrizados en el sistema
	 */
	private Collection<HashMap<String, Object>> turnosParametrizadosActivos=null;

	/**
	 * Mostrar el total de horas semanales
	 */
	private boolean mostrarHorasSemanales=false;
	
	/**
	 * Código de la novedad que se desea asignar
	 */
	protected int novedad;
	
	/**
	 * Tipo de Observación que se desea registrar
	 */
	private int tipoObservacion;
	
	/**
	 * Código del turno por el cual se va a cambiar
	 */
	private int codigoTurnoDestino;
	
	/**
	 * Código del cuadro de turnos
	 */
	private int codigoCuadro;

	/**
	 * Indica si el cuadro que estoy cargando es por categoría oó es de una persona
	 */
	private boolean esCategoria;

	/**
	 * Manejo de los mensajes de warning
	 * En la posición 0 debe ir la etiquieta del application resources
	 * En las demas posiciones van los argumentos 1, 2, 3, etc.
	 */
	private ArrayList<String> warning=null;
	
	/**
	 * HashMap para manejar las restricciones asociadas a la categoria
	 */
	private HashMap<String, Object> restriccionCategoria;
	
	/**
	 * HashMap para manejar los turnos de las enfermeras
	 */
	private HashMap<String, Object> turnosEnfermeras;
	
	/**
	 * Para manejar el resultado de la consulta de novedades asignadas a una enfermera
	 */
	private Collection<HashMap<String, Object>> listadoNovedad;
	
	/**
	 * Para manejar el nombre la Categoria
	 */
	private String nombreCategoria;
	
   /**
	* Para manejar las fechas que retorn la base de datos con
	* después de ingresar una fecha y una cantidad de días  
	*/
	private ArrayList<String> listadoFechas;
	
	/**
	 * Manejo de scroll horizontal
	 */
	private int scrollX;

	/**
	 * Manejo de scroll vertical
	 */
	private int scrollY;

	/**
	 * Código del turno al cual se le va a asignar la observacion
	 */
	protected int codigoTurnoObservacion;

	/**
	 * Código del turno a cambiar
	 */
	private int turnoACambiar;

	/**
	 * Hacer comparaciones del turno libre
	 */
	private int codigoTurnoLibre;
	
	/**
	 * Indica si es cambio de turno
	 */
	private int opcionObservacion;
	
	/**
	 * Categoría destino donde s va a cubrir el turno
	 */
	protected int categoriaDestino;
	
	/**
	 * Color de la categoria en caso de que sea un turno cubierto
	 */
	protected String colorCategoria;
	
	/**
	 * Esta categoría no se debe mostrar en el listado de cubrir turno
	 */
	protected int codigoCategoriaParaNoMostrar;

	/**
	 * Número de días de la novedad, por defecto será 1
	 */
	protected int numeroDiasNovedad;
	
	/**
	 * Atributo que me indica si debo generar los turnos del cuadro o simplemente
	 * debo generar el espacio
	 */
	private boolean generarTurnos;
	
	/**
	 * Preferencia en el tipo de turnos que se desa generar
	 */
	private int preferencia;
	
	/**
	 * Atributo que permite almacenar un número de horas para 
	 * ser adicionadas o restadas a un turno específico.
	 */
	protected Double horasModificadas;

	/**
	 * Tipo de cuadro a generar (Aleatorio, Secuencial, Rotativo)
	 */
	protected int tipoCuadro;
	
	/**
	 * Para que en el cuadro secuencial
	 * no me repita al darle otra opción
	 */
	int inicioAnterior=-1;
	
	/**
	 * Validate the properties that have been set from this HTTP request, and
	 * return an <code>ActionErrors</code> object that encapsulates any
	 * validation errors that have been found. If no errors are found, return
	 * <code>null</code> or an <code>ActionErrors</code> object with no recorded
	 * error messages.
	 *
	 * @param mapping The mapping used to select this instance
	 * @param request The servlet request we are processing
	*/
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores=new ActionErrors();
		
		boolean fechaI=UtilidadFecha.validarFecha(fechaInicio);
		boolean fechaF=UtilidadFecha.validarFecha(fechaFin);
		
		//System.out.println("Estado Forma: "+estado);
		
		if (estado.equals("insercion"))
		{
			clean();
		}
		
		if (estado.equals("empezar"))
		{
			return null;
		}
		
		if (estado.equals("generarCuadro"))
		{
		  		   			
			if (this.fechaInicio.equals(""))
			{
				errores.add("Fecha Inicio", new ActionMessage("errors.required", "El campo Fecha de Inicio"));
			} 
			if (this.fechaFin.equals(""))
			{
				errores.add("Fecha Fin", new ActionMessage("errors.required", "El campo Fecha de Finalización"));
			} 
			if (!this.fechaInicio.equals("") && !this.fechaFin.equals(""))
			{

				if (!fechaI)
				{
					errores.add("La Fecha de Inicio debe estar en formato dd/mm/aa", new ActionMessage("errors.formatoFechaInvalido", "de inicio"));
				}
			
				if (!fechaF)
				{
					errores.add("La Fecha de Fin debe estar en formato dd/mm/aa", new ActionMessage("errors.formatoFechaInvalido", "de finalización"));
				}
				if (codigoCategoria==-1)
				{
					errores.add("No ha Elegido Ninguna Categoria", new ActionMessage("errors.required", "El campo Categoria"));
				}
				
				if (fechaI && fechaF)
				{
					/*if (UtilidadFecha.conversionFormatoFechaABD(fechaInicio).compareTo(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()))<=0)
					{
						errores.add("Fecha Inicio Fecha Actual", new ActionMessage("errors.fechaAnteriorIgualActual", "de Inicio", "Actual"));
					}
					else
					{*/
						if (UtilidadFecha.conversionFormatoFechaABD(fechaInicio).compareTo(UtilidadFecha.conversionFormatoFechaABD(fechaFin))>=0)
						{
							errores.add("La Fecha de finalización de la Programacion debe ser mayor que la fecha de incio", new ActionMessage("errors.fechaPosteriorAOtraDeReferencia", "de Inicio" , "de Finalización" ));
						}
						else
						{
							int dias=UtilidadFecha.numeroDiasEntreFechas(fechaInicio, fechaFin)+1;
							if (dias<7)
							{
								errores.add("El rango de fechas", new  ActionMessage("errors.range", "El rango de fechas de la programacion", "7", "en adelante"));
							}

						}
					//}
				}
			}
		}
		return errores;
	}
	
	
	public void clean()
	{
		this.codigoCategoria=0;
		this.fechaFin="";
		this.fechaInicio="";

		this.asignaciones="";
		this.asignadas= new HashMap<String, Object>();
		this.restriccionCategoria= new HashMap<String, Object>();
		this.turnosEnfermeras=new HashMap<String, Object>();
		
		this.listadoPersonas=new ArrayList<HashMap<String, Object>>();
		this.listadoCategoriaRestriccion= new ArrayList<HashMap<String, Object>>();
		this.listadoFechas=new ArrayList<String>();
		this.cantFechas=0;
		this.cantProfesionales=0;
		this.nombreCategoria="";
		this.turnosParametrizados=null;
		this.mostrarHorasSemanales=false;
		this.warning=null;
		this.esCategoria=true;
		this.codigoEnfermera=0;
		this.codigoTurnoLibre=0;
		this.categoriaDestino=0;
		this.colorCategoria=null;
		this.codigoCategoriaParaNoMostrar=0;
		this.numeroDiasNovedad=1;
		this.fechaBusquedaEliminar=UtilidadFecha.getFechaActual();
		this.generarTurnos=true;
		this.preferencia=0;
		this.horasModificadas=0d;
		this.tipoCuadro=0;
		this.inicioAnterior=-1;
	}

	/**
	 * Resetear el scroll de la pantalla
	 */
	public void resetScroll()
	{
		this.scrollX=0;
		this.scrollY=0;
	}

	/**
	 * @return asignaciones
	 */
	public String getAsignaciones()
	{
		return asignaciones;
	}

	/**
	 * @param asignaciones Asigna asignaciones
	 */
	public void setAsignaciones(String asignaciones)
	{
		this.asignaciones = asignaciones;
	}

	/**
	 * @return asignadas
	 */
	public HashMap<String, Object> getAsignadas()
	{
		return asignadas;
	}

	/**
	 * @param asignadas Asigna asignadas
	 */
	public void setAsignadas(HashMap<String, Object> asignadas)
	{
		this.asignadas = asignadas;
	}

	/**
	 * @return codigoCategoria
	 */
	public int getCodigoCategoria()
	{
		return codigoCategoria;
	}

	/**
	 * @param codigoCategoria Asigna codigoCategoria
	 */
	public void setCodigoCategoria(int codigoCategoria)
	{
		this.codigoCategoria = codigoCategoria;
	}

	/**
	 * @return estado
	 */
	public String getEstado()
	{
		return estado;
	}

	/**
	 * @param estado Asigna estado
	 */
	public void setEstado(String estado)
	{
		this.estado = estado;
	}

	/**
	 * @return fechaFin
	 */
	public String getFechaFin()
	{
		return fechaFin;
	}

	/**
	 * @param fechaFin Asigna fechaFin
	 */
	public void setFechaFin(String fechaFin)
	{
		this.fechaFin = fechaFin;
	}

	/**
	 * @return fechaInicio
	 */
	public String getFechaInicio()
	{
		return fechaInicio;
	}

	/**
	 * @param fechaInicio Asigna fechaInicio
	 */
	public void setFechaInicio(String fechaInicio)
	{
		this.fechaInicio = fechaInicio;
	}
	
	/**
	 * @return Retorna el valor del Key
	 */
	public Object getTurno(String key)
	{
		return turnosEnfermeras.get(key);
	}
	
	/**
	 * @param asigna Iserta elemento en el cuadro
	 */
	public void setTurno(String key, Object value)
	{
		this.turnosEnfermeras.put(key, value);
	}

	/**
	 * @return listadoPersonas
	 */
	public Collection<HashMap<String, Object>> getListadoPersonas()
	{
		return listadoPersonas;
	}

	/**
	 * @param listadoPersonas Asigna listadoPersonas
	 */
	public void setListadoPersonas(Collection<HashMap<String, Object>> listadoPersonas)
	{
		this.listadoPersonas = listadoPersonas;
	}

	/**
	 * @return listadoCategoriaRestriccion
	 */
	public Collection<HashMap<String, Object>> getListadoCategoriaRestriccion()
	{
		return listadoCategoriaRestriccion;
	}

	/**
	 * @return turnosEnfermeras
	 */
	public HashMap<String, Object> getTurnosEnfermeras()
	{
		return turnosEnfermeras;
	}

	/**
	 * @param turnosEnfermeras Asigna turnosEnfermeras
	 */
	public void setTurnosEnfermeras(HashMap<String, Object> turnosEnfermeras)
	{
		this.turnosEnfermeras = turnosEnfermeras;
	}

	/**
	 * @return nombreCategoria
	 */
	public String getNombreCategoria()
	{
		return nombreCategoria;
	}

	/**
	 * @param nombreCategoria Asigna nombreCategoria
	 */
	public void setNombreCategoria(String nombreCategoria)
	{
		this.nombreCategoria = nombreCategoria;
	}

	/**
	 * @param listadoCategoriaRestriccion Asigna listadoCategoriaRestriccion
	 */
	public void setListadoCategoriaRestriccion(Collection<HashMap<String, Object>> listadoCategoriaRestriccion)
	{
		this.listadoCategoriaRestriccion = listadoCategoriaRestriccion;
	}

	/**
	 * @return listadoNovedad
	 */
	public Collection<HashMap<String, Object>> getListadoNovedad()
	{
		return listadoNovedad;
	}

	/**
	 * @param listadoNovedad Asigna listadoNovedad
	 */
	public void setListadoNovedad(Collection<HashMap<String, Object>> listadoNovedad)
	{
		this.listadoNovedad = listadoNovedad;
	}

	/**
	 * @return listadoFechas
	 */
	public ArrayList<String> getListadoFechas()
	{
		return listadoFechas;
	}

	/**
	 * @param listadoFechas Asigna listadoFechas
	 */
	public void setListadoFechas(ArrayList<String> listadoFechas)
	{
		this.listadoFechas = listadoFechas;
	}

	/**
	 * @return restriccionCategoria
	 */
	public HashMap<String, Object> getRestriccionCategoria()
	{
		return restriccionCategoria;
	}

	/**
	 * @param restriccionCategoria Asigna restriccionCategoria
	 */
	public void setRestriccionCategoria(HashMap<String, Object> restriccionCategoria)
	{
		this.restriccionCategoria = restriccionCategoria;
	}

	/**
	 * @return turnosParametrizados
	 */
	public Collection<HashMap<String, Object>> getTurnosParametrizados()
	{
		return turnosParametrizados;
	}

	/**
	 * @param turnosParametrizados Asigna turnosParametrizados
	 */
	public void setTurnosParametrizados(Collection<HashMap<String, Object>> turnosParametrizados)
	{
		this.turnosParametrizados = turnosParametrizados;
	}

	/**
	 * @return codigoEnfermera
	 */
	public int getCodigoEnfermera()
	{
		return codigoEnfermera;
	}

	/**
	 * @param codigoEnfermera Asigna codigoEnfermera
	 */
	public void setCodigoEnfermera(int codigoEnfermera)
	{
		this.codigoEnfermera = codigoEnfermera;
	}

	/**
	 * @return fechaObservacion
	 */
	public String getFechaObservacion()
	{
		return fechaObservacion;
	}

	/**
	 * @param fechaObservacion Asigna fechaObservacion
	 */
	public void setFechaObservacion(String fechaObservacion)
	{
		this.fechaObservacion = fechaObservacion;
	}

	/**
	 * @return mostrarHorasSemanales
	 */
	public boolean isMostrarHorasSemanales()
	{
		return mostrarHorasSemanales;
	}

	/**
	 * @param mostrarHorasSemanales Asigna mostrarHorasSemanales
	 */
	public void setMostrarHorasSemanales(boolean mostrarHorasSemanales)
	{
		this.mostrarHorasSemanales = mostrarHorasSemanales;
	}

	/**
	 * @return observacion
	 */
	public String getObservacion()
	{
		return observacion;
	}

	/**
	 * @param observacion Asigna observacion
	 */
	public void setObservacion(String observacion)
	{
		this.observacion = observacion;
	}
	
	/**
	 * @return warning
	 */
	public ArrayList<String> getWarning()
	{
		return warning;
	}
	
	/**
	 * @param warning Asigna warning
	 */
	public void setWarning(ArrayList<String> warning)
	{
		this.warning = warning;
	}
	
	/**
	 * @return codigoCuadro
	 */
	public int getCodigoCuadro()
	{
		return codigoCuadro;
	}
	
	/**
	 * @param codigoCuadro Asigna codigoCuadro
	 */
	public void setCodigoCuadro(int codigoCuadro)
	{
		this.codigoCuadro = codigoCuadro;
	}
	
	/**
	 * @return codigoTurnoDestino
	 */
	public int getCodigoTurnoDestino()
	{
		return codigoTurnoDestino;
	}
	
	/**
	 * @param codigoTurnoDestino Asigna codigoTurnoDestino
	 */
	public void setCodigoTurnoDestino(int codigoTurnoDestino)
	{
		this.codigoTurnoDestino = codigoTurnoDestino;
	}
	
	/**
	 * @return novedad
	 */
	public int getNovedad()
	{
		return novedad;
	}
	
	/**
	 * @param novedad Asigna novedad
	 */
	public void setNovedad(int novedad)
	{
		this.novedad = novedad;
	}
	
	/**
	 * @return tipoObservacion
	 */
	public int getTipoObservacion()
	{
		return tipoObservacion;
	}
	
	/**
	 * @param tipoObservacion Asigna tipoObservacion
	 */
	public void setTipoObservacion(int tipoObservacion)
	{
		this.tipoObservacion = tipoObservacion;
	}
	
	/**
	 * @return esCategoria
	 */
	public boolean getEsCategoria()
	{
		return esCategoria;
	}
	
	/**
	 * @param esCategoria Asigna esCategoria
	 */
	public void setEsCategoria(boolean esCategoria)
	{
		this.esCategoria = esCategoria;
	}
	
	/**
	 * @return scrollX
	 */
	public int getScrollX()
	{
		return scrollX;
	}
	
	/**
	 * @param scrollX Asigna scrollX
	 */
	public void setScrollX(int scrollX)
	{
		this.scrollX = scrollX;
	}
	
	/**
	 * @return scrollY
	 */
	public int getScrollY()
	{
		return scrollY;
	}
	
	/**
	 * @param scrollY Asigna scrollY
	 */
	public void setScrollY(int scrollY)
	{
		this.scrollY = scrollY;
	}
	
	/**
	 * @return Retorna cantFechas.
	 */
	public int getCantFechas()
	{
		return cantFechas;
	}
	
	/**
	 * @param cantFechas Asigna cantFechas.
	 */
	public void setCantFechas(int cantFechas)
	{
		this.cantFechas = cantFechas;
	}
	
	/**
	 * @return Retorna cantProfesionales.
	 */
	public int getCantProfesionales()
	{
		return cantProfesionales;
	}

	/**
	 * @param cantProfesionales Asigna cantProfesionales.
	 */
	public void setCantProfesionales(int cantProfesionales)
	{
		this.cantProfesionales = cantProfesionales;
	}

	/**
	 * @return codigoTurnoObservacion
	 */
	public int getCodigoTurnoObservacion()
	{
		return codigoTurnoObservacion;
	}

	/**
	 * @param codigoTurnoObservacion Asigna codigoTurnoObservacion
	 */
	public void setCodigoTurnoObservacion(int codigoTurnoObservacion)
	{
		this.codigoTurnoObservacion = codigoTurnoObservacion;
	}

	/**
	 * @return turnoACambiar
	 */
	public int getTurnoACambiar()
	{
		return turnoACambiar;
	}

	/**
	 * @param turnoACambiar Asigna turnoACambiar
	 */
	public void setTurnoACambiar(int turnoACambiar)
	{
		this.turnoACambiar = turnoACambiar;
	}

	/**
	 * @return codigoTurnoLibre
	 */
	public int getCodigoTurnoLibre()
	{
		return codigoTurnoLibre;
	}

	/**
	 * @param codigoTurnoLibre Asigna codigoTurnoLibre
	 */
	public void setCodigoTurnoLibre(int codigoTurnoLibre)
	{
		this.codigoTurnoLibre = codigoTurnoLibre;
	}

	/**
	 * @return opcionObservacion
	 */
	public int getOpcionObservacion()
	{
		return opcionObservacion;
	}

	/**
	 * @param opcionObservacion Asigna opcionObservacion
	 */
	public void setOpcionObservacion(int opcionObservacion)
	{
		this.opcionObservacion = opcionObservacion;
	}

	/**
	 * @return categoriaDestino
	 */
	public int getCategoriaDestino()
	{
		return categoriaDestino;
	}

	/**
	 * @param categoriaDestino Asigna categoriaDestino
	 */
	public void setCategoriaDestino(int categoriaDestino)
	{
		this.categoriaDestino = categoriaDestino;
	}

	/**
	 * @return codigoCategoriaParaNoMostrar
	 */
	public int getCodigoCategoriaParaNoMostrar()
	{
		return codigoCategoriaParaNoMostrar;
	}

	/**
	 * @param codigoCategoriaParaNoMostrar Asigna codigoCategoriaParaNoMostrar
	 */
	public void setCodigoCategoriaParaNoMostrar(int codigoCategoriaParaNoMostrar)
	{
		this.codigoCategoriaParaNoMostrar = codigoCategoriaParaNoMostrar;
	}

	/**
	 * @return colorCategoria
	 */
	public String getColorCategoria()
	{
		return colorCategoria;
	}

	/**
	 * @param colorCategoria Asigna colorCategoria
	 */
	public void setColorCategoria(String colorCategoria)
	{
		this.colorCategoria = colorCategoria;
	}

	/**
	 * @return Retorna fechaBusquedaEliminar
	 */
	public String getFechaBusquedaEliminar()
	{
		return fechaBusquedaEliminar;
	}

	/**
	 * @param Asigna fechaBusquedaEliminar
	 */
	public void setFechaBusquedaEliminar(String fechaBusquedaEliminar)
	{
		this.fechaBusquedaEliminar = fechaBusquedaEliminar;
	}

	/**
	 * @return Retorna listadoCuadros
	 */
	public Collection<HashMap<String, Object>> getListadoCuadros()
	{
		return listadoCuadros;
	}

	/**
	 * @param Asigna listadoCuadros
	 */
	public void setListadoCuadros(Collection<HashMap<String, Object>> listadoCuadros)
	{
		this.listadoCuadros = listadoCuadros;
	}

	/**
	 * @return Retorna listadoCategorias
	 */
	public Collection<HashMap<String, Object>> getListadoCategorias()
	{
		return listadoCategorias;
	}

	/**
	 * @param Asigna listadoCategorias
	 */
	public void setListadoCategorias(Collection<HashMap<String, Object>> listadoCategorias)
	{
		this.listadoCategorias = listadoCategorias;
	}


	/**
	 * @return Retorna generarTurnos
	 */
	public boolean getGenerarTurnos()
	{
		return generarTurnos;
	}


	/**
	 * @param Asigna generarTurnos
	 */
	public void setGenerarTurnos(boolean generarTurnos)
	{
		this.generarTurnos = generarTurnos;
	}


	/**
	 * @return Retorna numeroDiasNovedad
	 */
	public int getNumeroDiasNovedad()
	{
		return numeroDiasNovedad;
	}


	/**
	 * @param Asigna numeroDiasNovedad
	 */
	public void setNumeroDiasNovedad(int numeroDiasNovedad)
	{
		this.numeroDiasNovedad = numeroDiasNovedad;
	}

	/**
	 * 
	 * @return tipoCuadro
	 */
	public int getPreferencia()
	{
		return preferencia;
	}

	/**
	 * 
	 * @param Asigna tipoCuadro
	 */
	public void setPreferencia(int tipoCuadro)
	{
		this.preferencia = tipoCuadro;
	}

	/**
	 * @return Obtiene turnosParametrizadosActivos
	 */
	public Collection<HashMap<String, Object>> getTurnosParametrizadosActivos()
	{
		return turnosParametrizadosActivos;
	}

	/**
	 * @param turnosParametrizadosActivos Asigna turnosParametrizadosActivos
	 */
	public void setTurnosParametrizadosActivos(Collection<HashMap<String, Object>> turnosParametrizadosActivos)
	{
		this.turnosParametrizadosActivos = turnosParametrizadosActivos;
	}

	/**
	 * @return Obtiene horasModificadas
	 */
	public Double getHorasModificadas()
	{
		return horasModificadas;
	}

	/**
	 * @param horasModificadas Asigna horasModificadas
	 */
	public void setHorasModificadas(Double horasModificadas)
	{
		this.horasModificadas = horasModificadas;
	}

	/**
	 * @return Obtiene tipoCuadro
	 */
	public int getTipoCuadro()
	{
		return tipoCuadro;
	}

	/**
	 * @param tipoCuadro Asigna tipoCuadro
	 */
	public void setTipoCuadro(int tipoCuadro)
	{
		this.tipoCuadro = tipoCuadro;
	}


	/**
	 * @return Obtiene inicioAnterior
	 */
	public int getInicioAnterior()
	{
		return inicioAnterior;
	}


	/**
	 * @param inicioAnterior Asigna inicioAnterior
	 */
	public void setInicioAnterior(int inicioAnterior)
	{
		this.inicioAnterior = inicioAnterior;
	}

}
