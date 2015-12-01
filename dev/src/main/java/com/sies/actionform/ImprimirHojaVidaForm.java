/**
 * 
 */
package com.sies.actionform;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.servinte.axioma.orm.Personas;

/**
 * @author Juan David Ramírez López
 * Creado el 28/05/2008
 */
public class ImprimirHojaVidaForm extends ActionForm
{
	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Manejo de estados de la clase
	 */
	private String estado;
	
	/**
	 * Código de la persona que se desea imprimir
	 */
	private int codigo;
	
	/**
	 * Nombre de la persona que se desea imprimir
	 */
	private String nombrePersona;
	
	/**
	 * Se utiliza para las búsquedas de personas
	 */
	private String textProfesional;
	
	/**
	 * Datos de la persona buscada
	 */
	private Personas persona;
	
	/**
	 * Nombre de la ciudad de nacimiento
	 */
	private String ciudadNacimiento;
	
	/**
	 * Nombre de la ciudad de vivienda
	 */
	private String ciudadVivienda;

	/**
	 * Nombre del departamento de nacimiento
	 */
	private String departamentoNacimiento;
	
	/**
	 * Nombre del departamento de vivienda
	 */
	private String departamentoVivienda;
	
	/**
	 * Estado civil de la persona
	 */
	private String estadoCivil;

	/**
	 * Ocupación médica en caso de que sea profesional de la salud
	 */
	private String ocupacionMedica;
	
	public void clean()
	{
		codigo=0;
		nombrePersona="";
		textProfesional="";
		persona=null;
		ciudadNacimiento="";
		ciudadVivienda="";
		departamentoNacimiento="";
		departamentoVivienda="";
		estadoCivil="";
		ocupacionMedica="";
	}
	
	@Override
	public ActionErrors validate(ActionMapping mapping,
			HttpServletRequest request)
	{
		return null;
	}

	/**
	 * @return Obtiene estado
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
	 * @return Obtiene codigo
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
	 * @return Obtiene nombrePersona
	 */
	public String getNombrePersona()
	{
		return nombrePersona;
	}

	/**
	 * @param nombrePersona Asigna nombrePersona
	 */
	public void setNombrePersona(String nombrePersona)
	{
		this.nombrePersona = nombrePersona;
	}

	/**
	 * @return Obtiene textProfesional
	 */
	public String getTextProfesional()
	{
		return textProfesional;
	}

	/**
	 * @param textProfesional Asigna textProfesional
	 */
	public void setTextProfesional(String textProfesional)
	{
		this.textProfesional = textProfesional;
	}

	/**
	 * @return Obtiene persona
	 */
	public Personas getPersona()
	{
		return persona;
	}

	/**
	 * @param persona Asigna persona
	 */
	public void setPersona(Personas persona)
	{
		this.persona = persona;
	}

	/**
	 * @return Obtiene ciudadNacimiento
	 */
	public String getCiudadNacimiento()
	{
		return ciudadNacimiento;
	}

	/**
	 * @param ciudadNacimiento Asigna ciudadNacimiento
	 */
	public void setCiudadNacimiento(String ciudadNacimiento)
	{
		this.ciudadNacimiento = ciudadNacimiento;
	}

	/**
	 * @return Obtiene ciudadVivienda
	 */
	public String getCiudadVivienda()
	{
		return ciudadVivienda;
	}

	/**
	 * @param ciudadVivienda Asigna ciudadVivienda
	 */
	public void setCiudadVivienda(String ciudadVivienda)
	{
		this.ciudadVivienda = ciudadVivienda;
	}

	/**
	 * @return Obtiene departamentoNacimiento
	 */
	public String getDepartamentoNacimiento()
	{
		return departamentoNacimiento;
	}

	/**
	 * @param departamentoNacimiento Asigna departamentoNacimiento
	 */
	public void setDepartamentoNacimiento(String departamentoNacimiento)
	{
		this.departamentoNacimiento = departamentoNacimiento;
	}

	/**
	 * @return Obtiene departamentoVivienda
	 */
	public String getDepartamentoVivienda()
	{
		return departamentoVivienda;
	}

	/**
	 * @param departamentoVivienda Asigna departamentoVivienda
	 */
	public void setDepartamentoVivienda(String departamentoVivienda)
	{
		this.departamentoVivienda = departamentoVivienda;
	}

	/**
	 * @return Obtiene estadoCivil
	 */
	public String getEstadoCivil()
	{
		return estadoCivil;
	}

	/**
	 * @param estadoCivil Asigna estadoCivil
	 */
	public void setEstadoCivil(String estadoCivil)
	{
		this.estadoCivil = estadoCivil;
	}

	/**
	 * @return Obtiene ocupacionMedica
	 */
	public String getOcupacionMedica()
	{
		return ocupacionMedica;
	}

	/**
	 * @param ocupacionMedica Asigna ocupacionMedica
	 */
	public void setOcupacionMedica(String ocupacionMedica)
	{
		this.ocupacionMedica = ocupacionMedica;
	}
}
