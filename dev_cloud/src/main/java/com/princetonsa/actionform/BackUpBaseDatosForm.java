
/*
 * Creado   03 /Ago/ 2005
 *
 *Copyright Princeton S.A. &copy;&reg; 2004. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 * Compilador	:J2SDK 1.4.2_04
 */
package com.princetonsa.actionform;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

/**
 * Clase para manejar
 *
 * @version 1.0, 03 /Ago/ 2005
 * @author <a href="mailto:cperalta@PrincetonSA.com">Carlos Peralta</a>
 */
public class BackUpBaseDatosForm extends ValidatorForm 
{
    
    /**
     * Estado del flujo de la funcionalidad
     */
    private String estado;
    
    /**
     * Criterio que se debe especificar para saber de donde
     * se desea sacar el dump (Servidor-Local)
     */
    private String criterio;
   
    /**
     * String para almacenar 'SI' - 'NO' cuando se realiza la accion del dump.
     */
    private String realizoBackUp="";
    
    /**
     * Int indicador si se realizo back Up
     */
    private int indicador;
    
	/**
	 * inicializa los datos del formulario
	 */
    public void reset()
    {
        this.criterio="";
        this.realizoBackUp="";
        this.indicador=0;
    }

	/**
	 * Función de validación
	 * @param mapping
	 * @param request
	 * @return ActionError que especifica el error
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores = new ActionErrors();
		if(estado.equals("realizarBackUp"))
		{
			if(this.getCriterio().equals(""))
			{
				errores.add("base de datos", new ActionMessage("errors.required", "La Base de Datos de origen a la que desea realizar Back Up "));
			}
		}
	
		return errores;
	}	
	
	
    
	/**
	 * @return Returns the indicador.
	 */
	public int getIndicador()
	{
		return indicador;
	}
	/**
	 * @param indicador The indicador to set.
	 */
	public void setIndicador(int indicador)
	{
		this.indicador= indicador;
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
		this.estado= estado;
	}
	/**
	 * @return Returns the criterio.
	 */
	public String getCriterio()
	{
		return criterio;
	}
	/**
	 * @param criterio The criterio to set.
	 */
	public void setCriterio(String criterio)
	{
		this.criterio= criterio;
	}
	
	
	/**
	 * @return Returns the realizoBackUp.
	 */
	public String getRealizoBackUp()
	{
		return realizoBackUp;
	}
	/**
	 * @param realizoBackUp The realizoBackUp to set.
	 */
	public void setRealizoBackUp(String realizoBackUp)
	{
		this.realizoBackUp= realizoBackUp;
	}
}
