/*
 * @(#)RecargoTarifaForm.java
 * 
 * Created on 05-May-2004
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados
 * 
 * Lenguaje : Java
 * Compilador : J2SDK 1.4
 */
package com.princetonsa.actionform.cargos;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

/**
 * ActionForm, tiene la función de bean dentro de la forma, que contiene todos
 * los datos generales de un recargo de una tarifa. Y adicionalmente hace el manejo de reset 
 * de la forma y de validación de errores de datos. 
 * 
 * @version 1.0, 05-May-2004
 * @author <a href="mailto:Liliana@PrincetonSA.com">Liliana Caballero</a>
 */
public class RecargoTarifaForm extends ValidatorForm
{
	

	private String nombreConvenio;
	private int servicioRadio;
	
	private int especialidadRadio;
	
	private int recargoRadio;
	/**
	 * Signo del porcentaje de recargo
	 */
	private char signoPorcentaje;
	
	/**
	 * enlace para ver detalle del recargo
	 */
	private String enlace;
	/**
	 * accion que se quiere realizar sobre el recargo
	 */
	private String accion;
	/**
	 * convenio correspondiente al recargo
	 */
	
	private int convenio;
	/**
	 * Estado dentro del flujo
	 */
	private String estado;
	
	/**
	 * Codigo del recargo
	 */
	private int codigo;
	
	/**
	 * Porcentaje de recargo
	 */
	private double porcentaje;
	
	/**
	 * Valor del recargo
	 */
	private double valor;
	/**
	 * Código del contrato asociado al recargo
	 */
	private int codigoContrato;
	
	/**
	 * Número del contrato asociado al recargo
	 */
	private String numeroContrato;
	
	/**
	 * Código del servicio asociado al recargo 
	 */
	private int codigoServicio;

	/**
	 * Nombre del servicio asociado al recargo 
	 */
	private String nombreServicio;
	
	/**
	 * Código especialidad asociada al recargo
	 */
	private int codigoEspecialidad;
	
	/**
	 * Nombre especialidad asociada al recargo
	 */
	private String nombreEspecialidad;
	
	/**
	 * Código de la Via de ingreso asociada al recargo
	 */
	private int codigoViaIngreso;

	/**
	 * Nombre de la Via de ingreso asociada al recargo
	 */
	private String nombreViaIngreso;
	
	/**
	 * Codigo del tipo de recargo
	 */
	private int codigoTipoRecargo;
	
	/**
	 * Nombre del tipo de recargo
	 */
	private String nombreTipoRecargo;
	
	/**
	 * Almacena la coleccion de la consulta Recargo Tarifa.
	 */
	private Collection coleccionRecargo;
	
	

	/**
	 * Validate the properties that have been set from this HTTP request, and
	 * return an <code>ActionErrors</code> object that encapsulates any
	 * validation errors that have been found.  If no errors are found, return
	 * <code>null</code> or an <code>ActionErrors</code> object with no recorded
	 * error messages.
	 *
	 * @param mapping The mapping used to select this instance
	 * @param request The servlet request we are processing
	*/
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores = new ActionErrors();
		if(this.estado.equals("guardar") && (this.accion.equals("insertar")||this.accion.equals("modificar"))){
			//validar datos de ingreso
			if(this.codigoContrato==-1){
				errores.add("", new ActionMessage("errors.required", "El campo Contrato"));
			}
			if(this.codigoTipoRecargo==-1||codigoTipoRecargo==0){
				errores.add("", new ActionMessage("errors.required", "El campo Tipo de Recargo"));
			}
			
			if(this.codigoViaIngreso==-1){
				errores.add("", new ActionMessage("errors.required", "El campo Vía de Ingreso"));
			}
			if(this.servicioRadio!=-1){
				if(this.servicioRadio==0){
					if(this.codigoServicio==-1){
						errores.add("", new ActionMessage("errors.required", "El campo Servicio"));
					}
				}else{
					this.codigoServicio=0;
				}
			}else{
				errores.add("", new ActionMessage("errors.required", "El campo Servicio"));
			}
			if(this.especialidadRadio!=-1){
				if(this.especialidadRadio==0){
					if(this.codigoEspecialidad==-1){
						errores.add("", new ActionMessage("errors.required", "El campo Especialidad"));
					}
				}else{
					this.codigoEspecialidad=0;
				}
			}else{
				errores.add("", new ActionMessage("errors.required", "El campo Especialidad"));
			}
			if(this.recargoRadio!=-1){
				if(this.recargoRadio==0){
					if(this.porcentaje<=0){
						errores.add("", new ActionMessage("errors.floatMayorQue", "El campo Porcentaje","cero"));				
					}
					if(this.signoPorcentaje=='-'){
						this.porcentaje=-this.porcentaje;
					}
				}else if(this.recargoRadio==1){
					if(this.valor<=0 ){
						errores.add("", new ActionMessage("errors.floatMayorQue", "El campo Valor","cero"));				
					}
				}
			}else{
				errores.add("recargo requerido", new ActionMessage("errors.required", "El Recargo (Porcentaje o Valor)"));
			}
			
		}
		
		
		return errores;
	}	
	
	/**
	 * Método que inicializa todos los atributos de la forma
	 */
	public void reset() 
	{
		this.codigo = 0;
		this.porcentaje = 0.0;
		this.valor =0.0;		
		this.codigoViaIngreso = -1;
		this.nombreViaIngreso = "";
		this.codigoEspecialidad = -1;
		this.nombreEspecialidad = "";
		this.codigoServicio = -1;
		this.nombreServicio = "";
		this.codigoContrato = -1;
		this.numeroContrato = "";
		this.codigoTipoRecargo = -1;
		this.nombreTipoRecargo = "";
		this.enlace="";
		this.coleccionRecargo=null;
		this.convenio=-1;
		this.signoPorcentaje='+';
		this.recargoRadio=-1;
		this.servicioRadio=-1;
		this.especialidadRadio=-1;
	}	
	
	/**
	 * Retorna el codigo del recargo
	 * @return
	 */
	public int getCodigo()
	{
		return codigo;
	}

	/**
	 * Asigna el codigo del recargo
	 * @param codigo
	 */
	public void setCodigo(int codigo)
	{
		this.codigo = codigo;
	}

	/**
	 * Retorna el porcentaje de recargo
	 * @return
	 */
	public double getPorcentaje()
	{
		return porcentaje;
	}

	/**
	 * Asigna el porcentaje de recargo
	 * @param porcentaje
	 */
	public void setPorcentaje(double porcentaje)
	{
		this.porcentaje = porcentaje;
	}

	/**
	 * Retorna el valor del recargo
	 * @return
	 */
	public double getValor()
	{
		return valor;
	}

	/**
	 * Asigna el alor del recargo
	 * @param valor
	 */
	public void setValor(double valor)
	{
		this.valor = valor;
	}

	/**
	 * Retorna el código del contrato asociado al recargo
	 * @return
	 */
	public int getCodigoContrato()
	{
		return codigoContrato;
	}

	/**
	 * Asigna el código del contrato asociado al recargo
	 * @param codigoContrato
	 */
	public void setCodigoContrato(int codigoContrato)
	{
		this.codigoContrato = codigoContrato;
	}

	/**
	 * Retorna el número del contrato asociado al recargo
	 * @return
	 */
	public String getNumeroContrato()
	{
		return numeroContrato;
	}

	/**
	 * Asigna el número del contrato asociado al recargo
	 * @param numeroContrato
	 */
	public void setNumeroContrato(String numeroContrato)
	{
		this.numeroContrato = numeroContrato;
	}

	/**
	 * Retorna el código del servicio asociado al recargo 
	 * @return
	 */
	public int getCodigoServicio()
	{
		return codigoServicio;
	}

	/**
	 * Asigna el código del servicio asociado al recargo 
	 * @param codigoServicio
	 */
	public void setCodigoServicio(int codigoServicio)
	{
		this.codigoServicio = codigoServicio;
	}

	/**
	 * Retorna el nombre del servicio asociado al recargo 
	 * @return
	 */
	public String getNombreServicio()
	{
		return nombreServicio;
	}

	/**
	 * Asigna el nombre del servicio asociado al recargo 
	 * @param nombreServicio
	 */
	public void setNombreServicio(String nombreServicio)
	{
		this.nombreServicio = nombreServicio;
	}

	/**
	 * Retorna el código especialidad asociada al recargo
	 * @return
	 */
	public int getCodigoEspecialidad()
	{
		return codigoEspecialidad;
	}

	/**
	 * Asigna el código especialidad asociada al recargo
	 * @param codigoEspecialidad 
	 */
	public void setCodigoEspecialidad(int codigoEspecialidad)
	{
		this.codigoEspecialidad = codigoEspecialidad;
	}

	/**
	 * Retorna el nombre especialidad asociada al recargo
	 * @return
	 */
	public String getNombreEspecialidad()
	{
		return nombreEspecialidad;
	}

	/**
	 * Asigna el nombre especialidad asociada al recargo
	 * @param nombreEspecialidad
	 */
	public void setNombreEspecialidad(String nombreEspecialidad)
	{
		this.nombreEspecialidad = nombreEspecialidad;
	}

	/**
	 * Retorna el ódigo de la Via de ingreso asociada al recargo
	 * @return
	 */
	public int getCodigoViaIngreso()
	{
		return codigoViaIngreso;
	}

	/**
	 * Asigna el código de la Via de ingreso asociada al recargo
	 * @param codigoViaIngreso
	 */
	public void setCodigoViaIngreso(int codigoViaIngreso)
	{
		this.codigoViaIngreso = codigoViaIngreso;
	}

	/**
	 * Retorna el nombre de la Via de ingreso asociada al recargo
	 * @return
	 */
	public String getNombreViaIngreso()
	{
		return nombreViaIngreso;
	}

	/**
	 * Asigna el nombre de la Via de ingreso asociada al recargo
	 * @param nombreViaIngreso
	 */
	public void setNombreViaIngreso(String nombreViaIngreso)
	{
		this.nombreViaIngreso = nombreViaIngreso;
	}

	/**
	 * Retorna el estado dentro del flujo
	 * @return
	 */
	public String getEstado()
	{
		return estado;
	}

	/**
	 * Asigna el estado dentro del flujo
	 * @param estado
	 */
	public void setEstado(String estado)
	{
		this.estado = estado;
	}	

	/**
	 * Retorna el codigo del tipo de recargo
	 * @return
	 */
	public int getCodigoTipoRecargo()
	{
		return codigoTipoRecargo;
	}

	/**
	 * Asigna el codigo del tipo de recargo
	 * @param codigoTipoRecargo
	 */
	public void setCodigoTipoRecargo(int codigoTipoRecargo)
	{
		this.codigoTipoRecargo = codigoTipoRecargo;
	}

	/**
	 * Retorna el nombre del tipo de recargo
	 * @return
	 */
	public String getNombreTipoRecargo()
	{
		return nombreTipoRecargo;
	}

	/**
	 * Asigna el nombre del tipo de recargo
	 * @param nombreTipoRecargo
	 */
	public void setNombreTipoRecargo(String nombreTipoRecargo)
	{
		this.nombreTipoRecargo = nombreTipoRecargo;
	}

	/**
	 * Returns the convenio.
	 * @return int
	 */
	public int getConvenio()
	{
		return convenio;
	}

	/**
	 * Sets the convenio.
	 * @param convenio The convenio to set
	 */
	public void setConvenio(int convenio)
	{
		this.convenio = convenio;
	}

	/**
	 * Returns the accion.
	 * @return String
	 */
	public String getAccion()
	{
		return accion;
	}

	/**
	 * Sets the accion.
	 * @param accion The accion to set
	 */
	public void setAccion(String accion)
	{
		this.accion = accion;
	}

	/**
	 * Returns the enlace.
	 * @return String
	 */
	public String getEnlace()
	{
		return enlace;
	}

	/**
	 * Sets the enlace.
	 * @param enlace The enlace to set
	 */
	public void setEnlace(String enlace)
	{
		this.enlace = enlace;
	}

	
	
	/**
	 * Returns the signoPorcentaje.
	 * @return String
	 */
	public char getSignoPorcentaje()
	{
		return signoPorcentaje;
	}

	/**
	 * Sets the signoPorcentaje.
	 * @param signoPorcentaje The signoPorcentaje to set
	 */
	public void setSignoPorcentaje(char signoPorcentaje)
	{
		this.signoPorcentaje = signoPorcentaje;
	}

	/**
	 * Returns the recargoRadio.
	 * @return int
	 */
	public int getRecargoRadio()
	{
		return recargoRadio;
	}

	/**
	 * Sets the recargoRadio.
	 * @param recargoRadio The recargoRadio to set
	 */
	public void setRecargoRadio(int recargoRadio)
	{
		this.recargoRadio = recargoRadio;
	}

	/**
	 * Returns the especialidadRadio.
	 * @return int
	 */
	public int getEspecialidadRadio()
	{
		return especialidadRadio;
	}

	/**
	 * Returns the servicioRadio.
	 * @return int
	 */
	public int getServicioRadio()
	{
		return servicioRadio;
	}

	/**
	 * Sets the especialidadRadio.
	 * @param especialidadRadio The especialidadRadio to set
	 */
	public void setEspecialidadRadio(int especialidadRadio)
	{
		this.especialidadRadio = especialidadRadio;
	}

	/**
	 * Sets the servicioRadio.
	 * @param servicioRadio The servicioRadio to set
	 */
	public void setServicioRadio(int servicioRadio)
	{
		this.servicioRadio = servicioRadio;
	}
	/**
	 * Method adecuarValores.
	 */
	public void adecuarValores()
	{
		//todos o un servicio
		if(this.codigoServicio==-1){
			this.servicioRadio=1;
			this.codigoServicio=0;
			this.nombreServicio="Todos";
		}else{
			this.servicioRadio=0;
		}
		//todos o una especialidad
		if(this.codigoEspecialidad==-1){
			this.especialidadRadio=1;
			this.codigoEspecialidad=0;
			this.nombreEspecialidad="Todas";
		}else{
			this.especialidadRadio=0;
		}
		//tipo de recargo
		if(this.porcentaje==0){
			this.recargoRadio=1;
		}else if(this.valor<=0){
			this.recargoRadio=0;
			if(porcentaje<0){
				this.porcentaje=this.porcentaje*(-1);
				this.signoPorcentaje='-';
			}else{
				this.signoPorcentaje='+';
			}
		}
		
	}

	/**
	 * Returns the nombreConvenio.
	 * @return String
	 */
	public String getNombreConvenio()
	{
		return nombreConvenio;
	}

	/**
	 * Sets the nombreConvenio.
	 * @param nombreConvenio The nombreConvenio to set
	 */
	public void setNombreConvenio(String nombreConvenio)
	{
		this.nombreConvenio = nombreConvenio;
	}
	/**
	 * Method adecuarValoresModificar.
	 */
	public void adecuarValoresModificar()
	{
		
	}

    /**
     * @return Retorna coleccionRecargo.
     */
    public Collection getColeccionRecargo() {
        return coleccionRecargo;
    }
    /**
     * @param coleccionRecargo Asigna coleccionRecargo.
     */
    public void setColeccionRecargo(Collection coleccionRecargo) {
        this.coleccionRecargo = coleccionRecargo;
    }
    
}
