package com.princetonsa.mundo;

import util.InfoDatosInt;
import util.TipoNumeroId;

/**
 * Contiene la información adicional asociada con las camas ocupadas
 * @version 1.0, Octubre 13, 2003
 * @author <a href="mailto:Liliana@PrincetonSA.com">Liliana Caballero</a>
 */
public class CamaOcupada 
{
	/**
	 * Tipo y número de identificación del paciente
	 */
	private TipoNumeroId idPaciente;
	
	private String nombreCompletoPaciente;
	
	/**
	 * Edad del paciente en entero y en cadena con su correspondiente mes, dia,
	 * año
	 */
	private InfoDatosInt edad;

	/**
	 * Código y nombre del sexo del paciente
	 */
	private InfoDatosInt sexo;
	
	/**
	 * Fecha de ingreso del paciente.
	 * Para pacientes hospitalizados es la fecha de la admisión.
	 * Para pacientes de urgencias la fecha de ingreso a observación.
	 */
	private String fechaIngreso;
	
	/**
	 * Definir la hora de ingreso del paciente
	 */
	private String horaIngreso; 
	
	/**
	 * Número de días de ocupación de la cama.
	 */
	private int numDiasEstancia;
	
	/**
	 * Convenio responsable del paciente
	 */	
	private String responsableCuenta;
	
	public CamaOcupada()
	{
		this.idPaciente = new TipoNumeroId();
		this.edad = new InfoDatosInt();
		this.sexo = new InfoDatosInt();
		this.fechaIngreso = "";
		this.numDiasEstancia = 0;
		this.responsableCuenta = "";
	}	
	
	/**
	 * Returns the edad.
	 * @return InfoDatosInt
	 */
	public InfoDatosInt getEdad()
	{
		return edad;
	}

	/**
	 * Returns the fechaIngreso.
	 * @return String
	 */
	public String getFechaIngreso()
	{
		return fechaIngreso;
	}

	/**
	 * Returns the idPaciente.
	 * @return TipoNumeroId
	 */
	public TipoNumeroId getIdPaciente()
	{
		return idPaciente;
	}

	/**
	 * Returns the numDias.
	 * @return int
	 */
	public int getNumDiasEstancia()
	{
		return numDiasEstancia;
	}

	/**
	 * Returns the sexo.
	 * @return InfoDatosInt
	 */
	public InfoDatosInt getSexo()
	{
		return sexo;
	}

	/**
	 * Sets the edad.
	 * @param edad The edad to set
	 */
	public void setEdad(InfoDatosInt edad)
	{
		this.edad = edad;
	}
	
	public void setEdad(int edad, String edadTexto)
	{
		InfoDatosInt pareja = new InfoDatosInt(edad, edadTexto);
		this.edad = pareja;
	}

	/**
	 * Sets the fechaIngreso.
	 * @param fechaIngreso The fechaIngreso to set
	 */
	public void setFechaIngreso(String fechaIngreso)
	{
		this.fechaIngreso = fechaIngreso;
	}

	/**
	 * Sets the idPaciente.
	 * @param idPaciente The idPaciente to set
	 */
	public void setIdPaciente(TipoNumeroId idPaciente)
	{
		this.idPaciente = idPaciente;
	}
	
	public void setIdPaciente(String tipoId, String numeroId)
	{
		TipoNumeroId id = new TipoNumeroId(tipoId, numeroId);
		this.idPaciente = id;
	}

	/**
	 * Sets the numDias.
	 * @param numDias The numDias to set
	 */
	public void setNumDiasEstancia(int numDias)
	{
		this.numDiasEstancia = numDias;
	}

	/**
	 * Sets the sexo.
	 * @param sexo The sexo to set
	 */
	public void setSexo(InfoDatosInt sexo)
	{
		this.sexo = sexo;
	}
	
	public void setSexo(int codSexo, String nombreSexo)
	{
		InfoDatosInt pareja = new InfoDatosInt(codSexo, nombreSexo);
		this.sexo = pareja;
	}
	
	/**
	 * Returns the nombreCompletoPaciente.
	 * @return String
	 */
	public String getNombreCompletoPaciente()
	{
		return nombreCompletoPaciente;
	}

	/**
	 * Sets the nombreCompletoPaciente.
	 * @param nombreCompletoPaciente The nombreCompletoPaciente to set
	 */
	public void setNombreCompletoPaciente(String nombreCompletoPaciente)
	{
		this.nombreCompletoPaciente = nombreCompletoPaciente;
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
	 * @return
	 */
	public String getHoraIngreso()
	{
		return horaIngreso;
	}

	/**
	 * @param string
	 */
	public void setHoraIngreso(String string)
	{
		if(string == null)
			horaIngreso = "";
		else
			horaIngreso = string;
	}

}
