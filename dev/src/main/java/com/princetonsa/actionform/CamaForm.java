package com.princetonsa.actionform;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import util.UtilidadFecha;

/**
 * ActionForm, tiene la función de bean dentro de la forma, que contiene todos
 * los datos asociados con una cama. Y adicionalmente hace el manejo de reset de
 * la forma y de validación de errores de datos de entrada.
 * @version 1.0, Septiembre 15, 2003
 * @author <a href="mailto:Liliana@PrincetonSA.com">Liliana Caballero</a>,
 * @see org.apache.struts.action.ActionForm#validate(ActionMapping,
 * HttpServletRequest)
 */
public class CamaForm extends ValidatorForm
{
	/**
	 * Estado de la aplicación dentro del flujo
	 */
	private String estado;
		
	/**
	 * Código de la cama del paciente
	 */
	private int codCamaInicial;
	
	/**
	 * Número de la cama (inistitucional) inicial del paciente
	 */
	private String numCamaInicial;
	
	/**
	 * Código de la nueva cama en la base de datos
	 */
	private int codigo;
	
	/**
	 * Número de la cama (institucional)
	 */
	private String numero;
	
	/**
	 * habitacion
	 */
	private String habitacion;
	
	/**
	 * Codigo del estado en el que se encuentra la cama. 
	 */
	private int estadoCama;
	
	/**
	 * Estado en el que se encuentra la camas. Ej. Desinfección, ocupada
	 */
	private String nombreEstadoCama;
	
	/**
	 * Código del centro de costo al cual pertenece la cama
	 */
	private int centroCosto;
	
	/**
	 * Nombre del centro de costo al cual pertenece la cama
	 */
	private String nombreCentroCosto;
	
	/**
	 * Código del tipo de usuario de la cama. 
	 */
	private int tipoUsuarioCama;
	
	/**
	 * Nombre del tipo de usuario de la cama. Ej. Hombres, niños...
	 */
	private String nombreTipoUsuarioCama;
	
	/**
	 * Descripción de la cama
	 */
	private String descripcion;
	
	/*****************************************************************************************
	 *   	INFORMACION  	 		 PACIENTE
	 * ****************************************************************************************/
	
	/**
	 * Código de admisión del paciente
	 */
	private int codigoAdmision;
	
	/**
	 * Fecha de admisión del paciente
	 */
	private String fechaAdmision;
	
	/**
	 * Hora de admisión del paciente
	 */
	private String horaAdmision;
	
	/**
	 * Fecha de traslado de cama
	 */
	private String fechaTraslado;
	
	/**
	 * Fecha del último traslado
	 */
	private String fechaUltTraslado;
	
	/**
	 * Hora de traslado de cama
	 */
	private String horaTraslado;
	
	/**
	 * Hora del último traslado
	 */
	private String horaUltTraslado;
	
	/**
	 * Tipo de identificación del paciente. Ej CC (Cédula de Ciudadania)
	 */
	private String tipoIdPaciente;
	
	/**
	 * Número de identificación del paciente.
	 */	
	private String numeroIdPaciente;
	
	/**
	 * Nombre completo del paciente que se encuentra en la cama, en caso que
	 * haya uno.
	 */
	private String nombrePaciente;
	
	/**
	 * Código en la fuente de datos del sexo del paciente
	 */
	private int codSexoPaciente;
	
	/**
	 * Sexo del paciente, F ó Femenino, M ó Masculino
	 */
	private String sexoPaciente;
	
	/**
	 * Edad del paciente, únicamente en años
	 */
	private int edadPaciente;
	
	/**
	 * Edad detallada del paciente. Ej. 1 año y 2 meses
	 */
	private String edadTextoPaciente;
	
	/**
	 * Número de días de estancia del paciente
	 */
	private int numDiasEstancia;
	
	/**
	 * Nombre del convenio del paciente
	 */
	private String responsableCuenta;
	
	
	/**
	 * Pone en blanco los campos del form
	 */
	public void reset()
	{
		codCamaInicial = 0;
		numCamaInicial = "";
		codigo = 0;
		numero = "";
		habitacion="";
		estadoCama = 0;
		nombreEstadoCama = "";
		centroCosto = 0;
		nombreCentroCosto = "";
		tipoUsuarioCama = 0;
		nombreTipoUsuarioCama = "";
		descripcion = "";
		
		
		codigoAdmision = 0;
		fechaAdmision = "";
		horaAdmision = "";
		fechaTraslado = "";
		horaTraslado = "";
		fechaUltTraslado = "";
		horaUltTraslado = "";		
		codSexoPaciente = 0;
		edadPaciente = 0;		
		this.tipoIdPaciente = "";
		this.numeroIdPaciente = "";
		this.sexoPaciente = "";
		this.edadTextoPaciente = "";
		this.numDiasEstancia = -1;
		this.responsableCuenta = "";
		this.nombrePaciente = "";
	}
	
	/**
	* Valida  las propiedades que han sido establecidas para este request HTTP,
	* y retorna un objeto <code>ActionErrors</code> que encapsula los errores de
	* validación encontrados. Si no se encontraron errores de validación,
	* retorna <code>null</code>.
	* @param mapping el mapeado usado para elegir esta instancia
	* @param request el <i>servlet request</i> que está siendo procesado en este momento
	* @return un objeto <code>ActionErrors</code> con los (posibles) errores encontrados al validar este formulario,
	* o <code>null</code> si no se encontraron errores.
	*/
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores = new ActionErrors();
		
		if( estado.equals("finalizarTraslado") )
		{
			if( this.codigo == -1 )
				errores.add("Selección Inválida ", new ActionMessage("errors.seleccion", "cama"));
				
			// Fecha actual y patrón de fecha a utilizar en las validaciones
			final Date fechaActual = new Date();
			final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
			final SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("dd/MM/yyyy:HH:mm");
			
			Date fechaTrasladoD = null;
			boolean ftdValida = false;
			try 
			{
				fechaTrasladoD = dateFormatter.parse(this.fechaTraslado);
				ftdValida = true;
			}	
			catch (java.text.ParseException e) 
			{
				errores.add("fecha traslado cama", new ActionMessage("errors.formatoFechaInvalido", "de Traslado"));
			}
			
			Date fechaHoraTraslado = null;
			boolean fhtValida = false;
			if( ftdValida )
			{
				try 
				{
					fechaHoraTraslado= dateTimeFormatter.parse(this.fechaTraslado + ":" + this.horaTraslado);
					fhtValida = true;
				}	
				catch (java.text.ParseException e) 
				{
					errores.add("hora traslado cama", new ActionMessage("errors.formatoHoraInvalido", "de Traslado"));
				}
			}

			// Validar que la fecha ingresada no sea superior a la fecha actual
			if( ftdValida )
			{
				//if (fechaActual.compareTo(fechaTrasladoD) < 0)
			    if(UtilidadFecha.esFechaMenorQueOtraReferencia(UtilidadFecha.getFechaActual(), fechaTraslado))
				{
					errores.add("fecha traslado cama", new ActionMessage("errors.fechaPosteriorIgualActual","Traslado", "Actual"));
				}
				// Validar que si la fecha ingresada es igual a la fecha actual, la hora ingresada no sea superior a la hora actual
				else
				{
					if ( fhtValida && fechaActual.compareTo(fechaHoraTraslado) < 0) 
					{
						errores.add("hora traslado cama", new ActionMessage("errors.fechaPosteriorAOtraDeReferencia", "de traslado de la cama", "actual"));
					}
				}
			}
			
			Date fechaUltTrasladoD = null;
			boolean futdValida = false;
			try 
			{
				fechaUltTrasladoD = dateFormatter.parse(this.fechaUltTraslado);
				futdValida = true;
			}	
			catch (java.text.ParseException e) 
			{
				errores.add("fecha ultimo traslado", new ActionMessage("errors.formatoFechaInvalido", "del último Traslado"));
			}

			
			Date fechaHoraUltTraslado = null;
			boolean fhutValida = false;
			try 
			{
				fechaHoraUltTraslado = dateTimeFormatter.parse(this.fechaUltTraslado + ":" + this.horaUltTraslado);
				fhutValida = true;
			}	
			catch (java.text.ParseException e) 
			{
					errores.add("hora ultimo traslado ", new ActionMessage("errors.formatoHoraInvalido", "del último traslado"));
			}

			if( ftdValida && futdValida )
			{
				if(fechaTrasladoD.compareTo(fechaUltTrasladoD) < 0) 
				{
					errores.add("fecha ultimo traslado mayor", new ActionMessage("errors.fechaAnteriorAOtraDeReferencia", "de traslado de la cama", "del último registro de cama para el paciente"));
				}
		
				// Validar que si la fecha de admisión es igual a la fecha de valoración, la hora de admisión no sea superior a la hora de valoración
				else 
				{
					if( fhtValida && fhutValida && fechaHoraTraslado.compareTo(fechaHoraUltTraslado) < 0 ) 
					{
						errores.add("hora ultimo traslado mayor", new ActionMessage("errors.horaSuperiorA", "del último registro de cama para el paciente", "de traslado actual"));
					}
				}
			}
		}
		return errores;
	}
	
	/**
	 * Returns the codCamaInicial.
	 * @return int
	 */
	public int getCodCamaInicial()
	{
		return codCamaInicial;
	}

	/**
	 * Sets the codCamaInicial.
	 * @param codCamaInicial The codCamaInicial to set
	 */
	public void setCodCamaInicial(int codCamaInicial)
	{
		this.codCamaInicial = codCamaInicial;
	}

	/**
	 * Returns the centroCosto.
	 * @return int
	 */
	public int getCentroCosto()
	{
		return centroCosto;
	}

	/**
	 * Returns the codigo.
	 * @return int
	 */
	public int getCodigo()
	{
		return codigo;
	}

	/**
	 * Returns the codigoAdmision.
	 * @return int
	 */
	public int getCodigoAdmision()
	{
		return codigoAdmision;
	}

	/**
	 * Returns the descripcion.
	 * @return String
	 */
	public String getDescripcion()
	{
		return descripcion;
	}

	/**
	 * Returns the estado.
	 * @return int
	 */
	public int getEstadoCama()
	{
		return estadoCama;
	}

	/**
	 * Returns the fechaAdmision.
	 * @return String
	 */
	public String getFechaAdmision()
	{
		return fechaAdmision;
	}

	/**
	 * Returns the fechaTraslado.
	 * @return String
	 */
	public String getFechaTraslado()
	{
		return fechaTraslado;
	}

	/**
	 * Returns the horaAdmision.
	 * @return String
	 */
	public String getHoraAdmision()
	{
		return horaAdmision;
	}

	/**
	 * Returns the horaTraslado.
	 * @return String
	 */
	public String getHoraTraslado()
	{
		return horaTraslado;
	}

	/**
	 * Returns the nombreCentroCosto.
	 * @return String
	 */
	public String getNombreCentroCosto()
	{
		return nombreCentroCosto;
	}

	/**
	 * Returns the nombrePaciente.
	 * @return String
	 */
	public String getNombrePaciente()
	{
		return nombrePaciente;
	}

	/**
	 * Returns the nombreTipoUsuarioCama.
	 * @return String
	 */
	public String getNombreTipoUsuarioCama()
	{
		return nombreTipoUsuarioCama;
	}

	/**
	 * Returns the numCamaInicial.
	 * @return String
	 */
	public String getNumCamaInicial()
	{
		return numCamaInicial;
	}

	/**
	 * Returns the numero.
	 * @return String
	 */
	public String getNumero()
	{
		return numero;
	}

	/**
	 * Returns the tipoUsuarioCama.
	 * @return int
	 */
	public int getTipoUsuarioCama()
	{
		return tipoUsuarioCama;
	}

	/**
	 * Sets the centroCosto.
	 * @param centroCosto The centroCosto to set
	 */
	public void setCentroCosto(int centroCosto)
	{
		this.centroCosto = centroCosto;
	}

	/**
	 * Sets the codigo.
	 * @param codigo The codigo to set
	 */
	public void setCodigo(int codigo)
	{
		this.codigo = codigo;
	}

	/**
	 * Sets the codigoAdmision.
	 * @param codigoAdmision The codigoAdmision to set
	 */
	public void setCodigoAdmision(int codigoAdmision)
	{
		this.codigoAdmision = codigoAdmision;
	}

	/**
	 * Sets the descripcion.
	 * @param descripcion The descripcion to set
	 */
	public void setDescripcion(String descripcion)
	{
		this.descripcion = descripcion;
	}

	/**
	 * Sets the estado.
	 * @param estado The estado to set
	 */
	public void setEstadoCama(int estado)
	{
		this.estadoCama = estado;
	}

	/**
	 * Sets the fechaAdmision.
	 * @param fechaAdmision The fechaAdmision to set
	 */
	public void setFechaAdmision(String fechaAdmision)
	{
		this.fechaAdmision = fechaAdmision;
	}

	/**
	 * Sets the fechaTraslado.
	 * @param fechaTraslado The fechaTraslado to set
	 */
	public void setFechaTraslado(String fechaTraslado)
	{
		this.fechaTraslado = fechaTraslado;
	}

	/**
	 * Sets the horaAdmision.
	 * @param horaAdmision The horaAdmision to set
	 */
	public void setHoraAdmision(String horaAdmision)
	{
		this.horaAdmision = horaAdmision;
	}

	/**
	 * Sets the horaTraslado.
	 * @param horaTraslado The horaTraslado to set
	 */
	public void setHoraTraslado(String horaTraslado)
	{
		this.horaTraslado = horaTraslado;
	}

	/**
	 * Sets the nombreCentroCosto.
	 * @param nombreCentroCosto The nombreCentroCosto to set
	 */
	public void setNombreCentroCosto(String nombreCentroCosto)
	{
		this.nombreCentroCosto = nombreCentroCosto;
	}

	/**
	 * Sets the nombrePaciente.
	 * @param nombrePaciente The nombrePaciente to set
	 */
	public void setNombrePaciente(String nombrePaciente)
	{
		this.nombrePaciente = nombrePaciente;
	}

	/**
	 * Sets the nombreTipoUsuarioCama.
	 * @param nombreTipoUsuarioCama The nombreTipoUsuarioCama to set
	 */
	public void setNombreTipoUsuarioCama(String nombreTipoUsuarioCama)
	{
		this.nombreTipoUsuarioCama = nombreTipoUsuarioCama;
	}

	/**
	 * Sets the numCamaInicial.
	 * @param numCamaInicial The numCamaInicial to set
	 */
	public void setNumCamaInicial(String numCamaInicial)
	{
		this.numCamaInicial = numCamaInicial;
	}

	/**
	 * Sets the numero.
	 * @param numero The numero to set
	 */
	public void setNumero(String numero)
	{
		this.numero = numero;
	}

	/**
	 * Sets the tipoUsuarioCama.
	 * @param tipoUsuarioCama The tipoUsuarioCama to set
	 */
	public void setTipoUsuarioCama(int tipoUsuarioCama)
	{
		this.tipoUsuarioCama = tipoUsuarioCama;
	}

	/**
	 * Returns the estado.
	 * @return String
	 */
	public String getEstado()
	{
		return estado;
	}

	/**
	 * Sets the estado.
	 * @param estado The estado to set
	 */
	public void setEstado(String estado)
	{
		this.estado = estado;
	}

	/**
	 * Returns the codSexoPaciente.
	 * @return int
	 */
	public int getCodSexoPaciente()
	{
		return codSexoPaciente;
	}

	/**
	 * Returns the edadPaciente.
	 * @return int
	 */
	public int getEdadPaciente()
	{
		return edadPaciente;
	}

	/**
	 * Sets the codSexoPaciente.
	 * @param codSexoPaciente The codSexoPaciente to set
	 */
	public void setCodSexoPaciente(int codSexoPaciente)
	{
		this.codSexoPaciente = codSexoPaciente;
	}

	/**
	 * Sets the edadPaciente.
	 * @param edadPaciente The edadPaciente to set
	 */
	public void setEdadPaciente(int edadPaciente)
	{
		this.edadPaciente = edadPaciente;
	}

	/**
	 * Returns the fechaUltTraslado.
	 * @return String
	 */
	public String getFechaUltTraslado()
	{
		return fechaUltTraslado;
	}

	/**
	 * Returns the horaUltTraslado.
	 * @return String
	 */
	public String getHoraUltTraslado()
	{
		return horaUltTraslado;
	}

	/**
	 * Sets the fechaUltTraslado.
	 * @param fechaUltTraslado The fechaUltTraslado to set
	 */
	public void setFechaUltTraslado(String fechaUltTraslado)
	{
		this.fechaUltTraslado = fechaUltTraslado;
	}

	/**
	 * Sets the horaUltTraslado.
	 * @param horaUltTraslado The horaUltTraslado to set
	 */
	public void setHoraUltTraslado(String horaUltTraslado)
	{
		this.horaUltTraslado = horaUltTraslado;
	}

	/**
	 * Returns the nombreEstadoCama.
	 * @return String
	 */
	public String getNombreEstadoCama()
	{
		return nombreEstadoCama;
	}

	/**
	 * Sets the nombreEstadoCama.
	 * @param nombreEstadoCama The nombreEstadoCama to set
	 */
	public void setNombreEstadoCama(String nombreEstadoCama)
	{
		this.nombreEstadoCama = nombreEstadoCama;
	}

	/**
	 * Returns the numeroIdPaciente.
	 * @return String
	 */
	public String getNumeroIdPaciente()
	{
		return numeroIdPaciente;
	}

	/**
	 * Returns the tipoIdPaciente.
	 * @return String
	 */
	public String getTipoIdPaciente()
	{
		return tipoIdPaciente;
	}

	/**
	 * Sets the numeroIdPaciente.
	 * @param numeroIdPaciente The numeroIdPaciente to set
	 */
	public void setNumeroIdPaciente(String numeroIdPaciente)
	{
		this.numeroIdPaciente = numeroIdPaciente;
	}

	/**
	 * Sets the tipoIdPaciente.
	 * @param tipoIdPaciente The tipoIdPaciente to set
	 */
	public void setTipoIdPaciente(String tipoIdPaciente)
	{
		this.tipoIdPaciente = tipoIdPaciente;
	}

	/**
	 * Returns the sexoPaciente.
	 * @return String
	 */
	public String getSexoPaciente()
	{
		return sexoPaciente;
	}

	/**
	 * Sets the sexoPaciente.
	 * @param sexoPaciente The sexoPaciente to set
	 */
	public void setSexoPaciente(String sexoPaciente)
	{
		this.sexoPaciente = sexoPaciente;
	}

	/**
	 * Returns the edadTextoPaciente.
	 * @return String
	 */
	public String getEdadTextoPaciente()
	{
		return edadTextoPaciente;
	}

	/**
	 * Sets the edadTextoPaciente.
	 * @param edadTextoPaciente The edadTextoPaciente to set
	 */
	public void setEdadTextoPaciente(String edadTextoPaciente)
	{
		this.edadTextoPaciente = edadTextoPaciente;
	}

	/**
	 * Returns the numDiasEstancia.
	 * @return int
	 */
	public int getNumDiasEstancia()
	{
		return numDiasEstancia;
	}

	/**
	 * Sets the numDiasEstancia.
	 * @param numDiasEstancia The numDiasEstancia to set
	 */
	public void setNumDiasEstancia(int numDiasEstancia)
	{
		this.numDiasEstancia = numDiasEstancia;
	}

	/**
	 * Returns the nombreConvenio.
	 * @return String
	 */
	public String getResponsableCuenta()
	{
		return responsableCuenta;
	}

	/**
	 * Sets the nombreConvenio.
	 * @param nombreConvenio The nombreConvenio to set
	 */
	public void setResponsableCuenta(String nombreConvenio)
	{
		this.responsableCuenta = nombreConvenio;
	}
    /**
     * @return Returns the habitacion.
     */
    public String getHabitacion() {
        return habitacion;
    }
    /**
     * @param habitacion The habitacion to set.
     */
    public void setHabitacion(String habitacion) {
        this.habitacion = habitacion;
    }
}
