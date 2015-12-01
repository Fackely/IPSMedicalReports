package com.princetonsa.dto.odontologia;

import java.io.Serializable;

import util.ConstantesBD;

/**
 * Guarda las relaciones de los programas con las superficies que tiene asociadas
 * @author Juan David Ram&iacute;rez
 * @since 2010-05-11
 */
public class DtoSuperficiesPorPrograma implements Serializable
{
	/**
	 * Versi&oacute;n serial
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * C&oacute;digo de la relaci&oacute;n con la tabla detPlanTratamiento
	 */
	private int detPlanTratamiento;
	
	/**
	 * Superficie para la que aplica el programa
	 */
	private int superficieDental;
	
	/**
	 * 
	 */
	private String descripcionSuperficie;
	
	
	public String getDescripcionSuperficie() {
		return descripcionSuperficie;
	}
	public void setDescripcionSuperficie(String descripcionSuperficie) {
		this.descripcionSuperficie = descripcionSuperficie;
	}
	/**
	 * Encabezado con el plan de tratamiento y la pieza dental 
	 */
	private DtoProgHallazgoPieza progHallazgoPieza;
	
	/**
	 * Datos de auditor&iacute;a
	 */
	private DtoInfoFechaUsuario infoFechaUsuario;
	
	/**
	 * 
	 * Constructor de la clase
	 */
	public DtoSuperficiesPorPrograma() 
	{
		super();
		this.detPlanTratamiento = ConstantesBD.codigoNuncaValido;
		this.superficieDental = ConstantesBD.codigoNuncaValido;
		this.progHallazgoPieza = new DtoProgHallazgoPieza();
		this.infoFechaUsuario = new DtoInfoFechaUsuario();
	}
	/**
	 * @return Retorna el atributo detPlanTratamiento
	 */
	public int getDetPlanTratamiento()
	{
		return detPlanTratamiento;
	}
	/**
	 * @param detPlanTratamiento Asigna el atributo detPlanTratamiento
	 */
	public void setDetPlanTratamiento(int detPlanTratamiento)
	{
		this.detPlanTratamiento = detPlanTratamiento;
	}
	/**
	 * @return Retorna el atributo superficieDental
	 */
	public int getSuperficieDental()
	{
		return superficieDental;
	}
	/**
	 * @param superficieDental Asigna el atributo superficieDental
	 */
	public void setSuperficieDental(int superficieDental)
	{
		this.superficieDental = superficieDental;
	}
	/**
	 * @return Retorna el atributo progHallazgoPieza
	 */
	public DtoProgHallazgoPieza getProgHallazgoPieza()
	{
		return progHallazgoPieza;
	}
	/**
	 * @param progHallazgoPieza Asigna el atributo progHallazgoPieza
	 */
	public void setProgHallazgoPieza(DtoProgHallazgoPieza progHallazgoPieza)
	{
		this.progHallazgoPieza = progHallazgoPieza;
	}
	/**
	 * @return Retorna el atributo infoFechaUsuario
	 */
	public DtoInfoFechaUsuario getInfoFechaUsuario()
	{
		return infoFechaUsuario;
	}
	/**
	 * @param infoFechaUsuario Asigna el atributo infoFechaUsuario
	 */
	public void setInfoFechaUsuario(DtoInfoFechaUsuario infoFechaUsuario)
	{
		this.infoFechaUsuario = infoFechaUsuario;
	}
	
}
