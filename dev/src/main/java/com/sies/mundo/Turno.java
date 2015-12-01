package com.sies.mundo;

import java.io.Serializable;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;

public class Turno implements Serializable
{
	/**
	 * Manejador de logs de la clase
	 */
	private transient Logger logger=Logger.getLogger(Turno.class);
	
	/**
	 * Serial version UID para la lase serializable
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Código del turno
	 */
	private int codigo;
	
	private String tipoTurno;
	
	private String simbolo;
	
	private String colorLetra;
	
	private String colorFondo;
	
	private double numeroHoras;
	
	private int esFestivo;
	
	private boolean porDefecto;
	
	private Boolean prioridad;
	
	private String observaciones;
	
	/**
	 * Indica si el turno se puede mover para equilibrar
	 */
	private boolean modificable;
	/**
	 * Código del cuadro al que pertenece
	 */
	private int codigoCuadro;
	
	/**
	 * Código con el que queda almacenado un turno en la tabla ct_turno por cada enfermera
	 */
	private int codigoTurnoEnfermera;
	
	/**
	 * Indica si el turno tiene observaciones 
	 */
	private boolean tieneObservacion;
	
	/**
	 * Color de la categoría
	 */
	private String colorCategoria;

	/**
	 * Nombre de la categoría en la cual se encuentra el turno cubierto
	 */
	private String nombreCategoriaCubierta;

	/**
	 * Código de la categoría en la cual se encuentra el turno cubierto
	 */
	private int codigoCategoriaCubierta;
	
	/**
	 * Hora en la que empieza el turno
	 */
	private String horaDesde;
	
	/**
	 * Esta propiedad indica si el turno se puede mover para equilibrar las cargas
	 */
	private boolean sePuedeMover;
	
	/**
	 * Almacena el número de horas en las cuales se modificó el turno
	 */
	private double horasModificadas;

	/**
	 * Constructor vacío
	 */
	public Turno()
	{
		this.codigo=0;
		this.tipoTurno="";
		this.simbolo="";
		this.colorLetra="#000000";
		this.colorFondo="#FFFFFF";
		this.numeroHoras=0;
		this.esFestivo=SiEsConstantes.TURNO_TODOS_LOS_DIAS;
		this.porDefecto=true;
		this.prioridad=null;
		this.observaciones="";
		this.codigoTurnoEnfermera=0;
		this.codigoCuadro=0;
		this.tieneObservacion=false;
		this.colorCategoria=null;
		this.nombreCategoriaCubierta="";
		this.horaDesde="00:00";
		this.modificable=true;
		this.sePuedeMover=true;
		this.horasModificadas=0;
	}

	/**
	 * Constructor del turno
	 * Para el caso de las novedades se debe utilizar el
	 * contrsuctor de novedades, el cual tiene una prioridad
	 * @param codigo
	 * @param tipoTurno
	 * @param simbolo
	 * @param colorLetra
	 * @param colorFondo
	 * @param numeroHoras
	 * @param esFestivo
	 * @param porDefecto
	 */
	public Turno(int codigo, String tipoTurno, String simbolo, String colorLetra, String colorFondo, double numeroHoras, int esFestivo, boolean porDefecto)
	{
		this.codigo=codigo;
		this.tipoTurno=tipoTurno;
		this.simbolo=simbolo;
		this.colorLetra=colorLetra;
		this.colorFondo=colorFondo;
		this.numeroHoras=numeroHoras;
		this.esFestivo=esFestivo;
		this.porDefecto=porDefecto;
		this.prioridad=null;
		this.observaciones="";
		this.codigoCuadro=0;
		this.modificable=true;
		this.sePuedeMover=true;
	}

	/**
	 * Constructor del turno
	 * Para el caso de las novedades se debe utilizar el
	 * contrsuctor de novedades, el cual tiene una prioridad
	 * @param codigo
	 * @param tipoTurno
	 * @param simbolo
	 * @param colorLetra
	 * @param colorFondo
	 * @param numeroHoras
	 * @param esFestivo
	 * @param porDefecto
	 * @param horaDesde
	 */
	public Turno(int codigo, String tipoTurno, String simbolo, String colorLetra, String colorFondo, double numeroHoras, int esFestivo, boolean porDefecto, String horaDesde)
	{
		this.codigo=codigo;
		this.tipoTurno=tipoTurno;
		this.simbolo=simbolo;
		this.colorLetra=colorLetra;
		this.colorFondo=colorFondo;
		this.numeroHoras=numeroHoras;
		this.esFestivo=esFestivo;
		this.porDefecto=porDefecto;
		this.prioridad=null;
		this.observaciones="";
		this.codigoCuadro=0;
		this.horaDesde=horaDesde;
		this.modificable=true;
		this.sePuedeMover=true;
	}

	/**
	 * Constructor del turno para las novedades (Adicionando una prioridad a la novedad)
	 * @param codigo
	 * @param tipoTurno
	 * @param simbolo
	 * @param colorLetra
	 * @param colorFondo
	 * @param numeroHoras
	 * @param esFestivo
	 * @param porDefecto
	 */
	public Turno(int codigo, String tipoTurno, String simbolo, String colorLetra, String colorFondo, double numeroHoras, int esFestivo, boolean porDefecto, boolean prioridad)
	{
		this.codigo=codigo;
		this.tipoTurno=tipoTurno;
		this.simbolo=simbolo;
		this.colorLetra=colorLetra;
		this.colorFondo=colorFondo;
		this.numeroHoras=numeroHoras;
		this.esFestivo=esFestivo;
		this.porDefecto=porDefecto;
		this.prioridad=prioridad;
		this.observaciones="";
		this.codigoCuadro=0;
		this.modificable=true;
		this.sePuedeMover=true;
	}

	public Turno(Turno turno)
	{
		try
		{
			PropertyUtils.copyProperties(this, turno);
		}
		catch (Exception e)
		{
			logger.error("Error copiando las propiedades del turno: "+e);
			
		}
	}

	/**
	 * @return codigo
	 */
	public int getCodigo()
	{
		return codigo;
	}

	/**
	 * @param codigo Asigna codigo
	 */
	public void setCodigo(int codigo)
	{
		this.codigo = codigo;
	}

	/**
	 * @return simbolo
	 */
	public String getSimbolo()
	{
		return simbolo;
	}

	/**
	 * @param simbolo Asigna simbolo
	 */
	public void setSimbolo(String simbolo)
	{
		this.simbolo = simbolo;
	}

	/**
	 * @return tipoTurno
	 */
	public String getTipoTurno()
	{
		return tipoTurno;
	}

	/**
	 * @param tipoTurno Asigna tipoTurno
	 */
	public void setTipoTurno(String tipoTurno)
	{
		this.tipoTurno = tipoTurno;
	}

	/**
	 * @return colorFondo
	 */
	public String getColorFondo()
	{
		return colorFondo;
	}

	/**
	 * @param colorFondo Asigna colorFondo
	 */
	public void setColorFondo(String colorFondo)
	{
		this.colorFondo = colorFondo;
	}

	/**
	 * @return colorLetra
	 */
	public String getColorLetra()
	{
		return colorLetra;
	}

	/**
	 * @param colorLetra Asigna colorLetra
	 */
	public void setColorLetra(String colorLetra)
	{
		this.colorLetra = colorLetra;
	}

	/**
	 * @return numeroHoras
	 */
	public double getNumeroHoras()
	{
		return numeroHoras;
	}

	/**
	 * @param numeroHoras Asigna numeroHoras
	 */
	public void setNumeroHoras(double numeroHoras)
	{
		this.numeroHoras = numeroHoras;
	}

	/**
	 * @return esFestivo
	 */
	public int getEsFestivo()
	{
		return esFestivo;
	}

	/**
	 * @param esFestivo Asigna esFestivo
	 */
	public void setEsFestivo(int esFestivo)
	{
		this.esFestivo = esFestivo;
	}

	/**
	 * @return porDefecto
	 */
	public boolean getPorDefecto()
	{
		return porDefecto;
	}

	/**
	 * @param porDefecto Asigna porDefecto
	 */
	public void setPorDefecto(boolean porDefecto)
	{
		this.porDefecto = porDefecto;
	}

	/**
	 * @return prioridad
	 */
	public Boolean getPrioridad()
	{
		return prioridad;
	}

	/**
	 * @param prioridad Asigna prioridad
	 */
	public void setPrioridad(Boolean prioridad)
	{
		this.prioridad = prioridad;
	}

	/**
	 * @return observaciones
	 */
	public String getObservaciones()
	{
		return observaciones;
	}

	/**
	 * @param observaciones Asigna observaciones
	 */
	public void setObservaciones(String observaciones)
	{
		this.observaciones = observaciones;
	}

	/**
	 * @return codigoTurnoEnfermera
	 */
	public int getCodigoTurnoEnfermera() {
		return codigoTurnoEnfermera;
	}

	/**
	 * @param codigoTurnoEnfermera Asigna codigoTurnoEnfermera
	 */
	public void setCodigoTurnoEnfermera(int codigoTurnoEnfermera) {
		this.codigoTurnoEnfermera = codigoTurnoEnfermera;
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
	 * @return tieneObservacion
	 */
	public boolean getTieneObservacion()
	{
		return tieneObservacion;
	}

	/**
	 * @param tieneObservacion Asigna tieneObservacion
	 */
	public void setTieneObservacion(boolean tieneObservacion)
	{
		this.tieneObservacion = tieneObservacion;
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
	 * @return nombreCategoriaCubierta
	 */
	public String getNombreCategoriaCubierta()
	{
		return nombreCategoriaCubierta;
	}

	/**
	 * @param nombreCategoriaCubierta Asigna nombreCategoriaCubierta
	 */
	public void setNombreCategoriaCubierta(String nombreCategoriaCubierta)
	{
		this.nombreCategoriaCubierta = nombreCategoriaCubierta;
	}

	/**
	 * @return codigoCategoriaCubierta
	 */
	public int getCodigoCategoriaCubierta()
	{
		return codigoCategoriaCubierta;
	}

	/**
	 * @param codigoCategoriaCubierta Asigna codigoCategoriaCubierta
	 */
	public void setCodigoCategoriaCubierta(int codigoCategoriaCubierta)
	{
		this.codigoCategoriaCubierta = codigoCategoriaCubierta;
	}

	/**
	 * @return horaDesde
	 */
	public String getHoraDesde()
	{
		return horaDesde;
	}

	/**
	 * @param horaDesde Asigna horaDesde
	 */
	public void setHoraDesde(String horaDesde)
	{
		this.horaDesde = horaDesde;
	}

	/**
	 * @return Retorna modificable
	 */
	public boolean getModificable()
	{
		return modificable;
	}

	/**
	 * @param Asigna modificable
	 */
	public void setModificable(boolean modificable)
	{
		this.modificable = modificable;
	}

	/**
	 * @return Retorna sePuedeMover
	 */
	public boolean getSePuedeMover()
	{
		return sePuedeMover;
	}

	/**
	 * @param sePuedeMover Asigna sePuedeMover
	 */
	public void setSePuedeMover(boolean sePuedeMover)
	{
		this.sePuedeMover = sePuedeMover;
	}

	/**
	 * @return Obtiene horasModificadas
	 */
	public double getHorasModificadas()
	{
		return horasModificadas;
	}

	/**
	 * @param horasModificadas Asigna horasModificadas
	 */
	public void setHorasModificadas(double horasModificadas)
	{
		this.horasModificadas = horasModificadas;
	}

}
