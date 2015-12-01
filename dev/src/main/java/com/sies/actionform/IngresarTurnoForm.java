package com.sies.actionform;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import util.UtilidadFecha;

/**
 * Clasde para ingresar Turnos
 * @author Juan David Ramírez (SiEs)
 *
 */
public class IngresarTurnoForm extends ActionForm
{

	/**
	 * Serial version UID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Manejador de flujo
	 */
	private String estado;
	
	/**
	 * Código del turno
	 */
	private int codigo;

	/**
	 * Descripción del turno
	 */
	private String descripcion;
	
	/**
	 * Manejo de los símbolos
	 */
	private String simbolo;
	
	/**
	 * Color de la letra representativa del turno
	 */
	private String colorLetra;

	/**
	 * Color del fondo del turno
	 */
	private String colorFondo;

	/**
	 * Hora de inicio del turno
	 */
	private String horaInicio;

	/**
	 * Hora finalización del turno
	 */
	private String numeroHoras;

	/**
	 * Tipo de turno (Mañana, tarde, corrido o otro)
	 */
	private char tipoTurno;
	
	/**
	 * Me indica si un turno se puede hacer todos los días, días festivos ó días ordinarios
	 */
	private int esFestivo;
	
	/**
	 * Indica si el turno viene por defecto o no
	 * Este tipo de turnos no es modificable
	 */
	private boolean porDefecto;
	
	/**
	 * Listado de los posibles tipos de turno
	 */
	private Collection<HashMap<String, Object>> listadoTiposTurno;
	
	/**
	 * Listado de los centros de costo
	 */
	private Collection<HashMap<String, Object>> listadoCentrosCosto;
	
	/**
	 * Listado con los turnos existentes
	 */
	private Collection<HashMap<String, Object>> turnosExistentes;
	
	/**
	 * Última propiedad de ordenamiento
	 */
	private String ultimaPropiedad;
	
	/**
	 * Propiedad de ordenamiento
	 */
	private String propiedad;
	
	/**
	 * Centro de costo relacionado al turno
	 */
	private Integer centroCosto;
	
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
	{
		if(centroCosto!=null && centroCosto<=0)
		{
			centroCosto=null;
		}
		if(estado.equalsIgnoreCase("guardar") || estado.equalsIgnoreCase("guardarModificacion"))
		{
			ActionErrors errores=new ActionErrors();
			if(descripcion.trim().equals(""))
			{
				errores.add("descripcion requerida", new ActionMessage("errors.required", "Descripción"));
			}
			if(simbolo.trim().equals(""))
			{
				errores.add("simbolo requerido", new ActionMessage("errors.required", "Simbolo"));
			}
			if(horaInicio.trim().equals(""))
			{
				errores.add("Hora Inicio requerido", new ActionMessage("errors.required", "Hora Inicio"));
			}
			else if(!UtilidadFecha.validacionHora(horaInicio).puedoSeguir)
			{
				errores.add("error hora", new ActionMessage("errors.formatoHoraInvalido", "Hora Inicio "+horaInicio));
			}
			if(numeroHoras.trim().equals(""))
			{
				errores.add("Hora Fin requerido", new ActionMessage("errors.required", "Número de Horas"));
			}
			else
			{
				try
				{
					double numeroHorasDou=Double.parseDouble(numeroHoras);
					if(numeroHorasDou<0 && porDefecto==false)
					{
						errores.add("error numero horas", new ActionMessage("errors.floatMayorOIgualQue", "El campo Número de Horas", "0"));
					}
				}
				catch (NumberFormatException e)
				{
					errores.add("error numero horas", new ActionMessage("errors.float", "El campo Número de Horas"));
				}
			}
			if(tipoTurno=='0')
			{
				errores.add("tipo turno", new ActionMessage("errors.required", "Tipo Turno"));
			}
			if(esFestivo==0)
			{
				errores.add("es_festivo", new ActionMessage("errors.required", "Días en que se Asigna"));
			}
			if(errores.isEmpty())
			{
				Iterator<HashMap<String, Object>> iterador=turnosExistentes.iterator();
				for(int i=0; i<turnosExistentes.size(); i++)
				{
					HashMap<String, Object> turno=iterador.next();
					if(codigo!=(Integer)turno.get("codigo") && simbolo.equalsIgnoreCase((String)turno.get("simbolo")) && colorLetra.equalsIgnoreCase((String)turno.get("color_letra")) && colorFondo.equalsIgnoreCase((String)turno.get("color_fondo")))
					{
						errores.add("simbolo repetido", new ActionMessage("error.ingresoTurno.repetido", "La combinacion Símbolo ("+simbolo+") y Colores de letra y fondo"));
					}
				}
			}
			
			return errores;
		}
		return null;
	}
	
	public IngresarTurnoForm()
	{
		this.reset();
	}
	
	/**
	 * Método para inicializar los valores
	 *
	 */
	public void reset()
	{
		this.descripcion="";
		this.simbolo="";
		this.colorLetra="#000000";
		this.colorFondo="#FFFFFF";
		this.horaInicio="";
		this.numeroHoras="";
		this.tipoTurno='0';
		this.listadoTiposTurno=null;
		this.listadoCentrosCosto=null;
		this.propiedad="";
		this.esFestivo=0;
		this.ultimaPropiedad="";
		this.porDefecto=false;
		this.centroCosto=null;
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
	 * @return descripcion
	 */
	public String getDescripcion()
	{
		return descripcion;
	}

	/**
	 * @param descripcion Asigna descripcion
	 */
	public void setDescripcion(String descripcion)
	{
		this.descripcion = descripcion;
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
	 * @return numeroHoras
	 */
	public String getNumeroHoras()
	{
		return numeroHoras;
	}

	/**
	 * @param numeroHoras Asigna numeroHoras
	 */
	public void setNumeroHoras(String horaFin)
	{
		this.numeroHoras = horaFin;
	}

	/**
	 * @return horaInicio
	 */
	public String getHoraInicio()
	{
		return horaInicio;
	}

	/**
	 * @param horaInicio Asigna horaInicio
	 */
	public void setHoraInicio(String horaInicio)
	{
		this.horaInicio = horaInicio;
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
	public char getTipoTurno()
	{
		return tipoTurno;
	}

	/**
	 * @param tipoTurno Asigna tipoTurno
	 */
	public void setTipoTurno(char tipoTurno)
	{
		this.tipoTurno = tipoTurno;
	}

	/**
	 * @return listadoTiposTurno
	 */
	public Collection<HashMap<String, Object>> getListadoTiposTurno()
	{
		return listadoTiposTurno;
	}

	/**
	 * @param listadoTiposTurno Asigna listadoTiposTurno
	 */
	public void setListadoTiposTurno(Collection<HashMap<String, Object>> listadoTiposTurno)
	{
		this.listadoTiposTurno = listadoTiposTurno;
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
	 * @return turnosExistentes
	 */
	public Collection<HashMap<String, Object>> getTurnosExistentes()
	{
		return turnosExistentes;
	}

	/**
	 * @param turnosExistentes Asigna turnosExistentes
	 */
	public void setTurnosExistentes(Collection<HashMap<String, Object>> turnosExistentes)
	{
		this.turnosExistentes = turnosExistentes;
	}

	/**
	 * @return propiedad
	 */
	public String getPropiedad()
	{
		return propiedad;
	}

	/**
	 * @param propiedad Asigna propiedad
	 */
	public void setPropiedad(String propiedad)
	{
		this.propiedad = propiedad;
	}

	/**
	 * @return ultimaPropiedad
	 */
	public String getUltimaPropiedad()
	{
		return ultimaPropiedad;
	}

	/**
	 * @param ultimaPropiedad Asigna ultimaPropiedad
	 */
	public void setUltimaPropiedad(String ultimaPropiedad)
	{
		this.ultimaPropiedad = ultimaPropiedad;
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
	public void setPorDefecto(boolean pordefecto)
	{
		this.porDefecto = pordefecto;
	}

	/**
	 * @return Obtiene centroCosto
	 */
	public Integer getCentroCosto()
	{
		return centroCosto;
	}

	/**
	 * @param centroCosto Asigna centroCosto
	 */
	public void setCentroCosto(Integer centroCosto)
	{
		this.centroCosto = centroCosto;
	}

	/**
	 * @return Obtiene listadoCentrosCosto
	 */
	public Collection<HashMap<String, Object>> getListadoCentrosCosto()
	{
		return listadoCentrosCosto;
	}

	/**
	 * @param listadoCentrosCosto Asigna listadoCentrosCosto
	 */
	public void setListadoCentrosCosto(Collection<HashMap<String, Object>> listadoCentrosCosto)
	{
		this.listadoCentrosCosto = listadoCentrosCosto;
	}

}
