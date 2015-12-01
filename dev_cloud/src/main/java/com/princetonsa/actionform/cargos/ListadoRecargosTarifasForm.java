/*
 * @(#)ListadoRecargosTarifasForm.java
 * 
 * Created on 05-May-2004
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados
 * 
 * Lenguaje : Java
 * Compilador : J2SDK 1.4
 */
package com.princetonsa.actionform.cargos;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

/**
 * ActionForm, tiene la función de bean dentro de la forma, que contiene todos
 * los datos generales de un conjunto de recargos de tarifas . Y adicionalmente hace el manejo de reset 
 * de la forma y de validación de errores de datos.  
 * 
 * @version 1.0, 05-May-2004
 * @author <a href="mailto:Liliana@PrincetonSA.com">Liliana Caballero</a>
 */
public class ListadoRecargosTarifasForm extends ValidatorForm
{
	
	private String nombreConvenio;
	/**
	 * tipo de modificacion que se solicito
	 */
	private String tipoModificacion;
	/**
	 * columna por la cual se quiere ordenar
	 */
	private String columna;
	/**
	 * ultima columna por la cual se ordeno
	 */
	private String ultimaPropiedad;
	/**
	 * accion que se quiere realizar sobre el recargo
	 */
	private String accion;
	/**
	 * convenio correspondiente al recargo
	 */
	
	private int convenio;
		
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
	 * Criterio(s) de búsqueda elegido(s).
	 */
	private String criteriosBusqueda[];

	/**
	 * Estado dentro del flujo
	 */
	private String estado;
		
	/**
	 * Lista con los recargos de las tarifas 
	 */
	private ArrayList recargosTarifas;
	
	
    /**
     * @return Retorna col.
     */
    public Collection getCol() {
        return col;
    }
    /**
     * @param col Asigna col.
     */
    public void setCol(Collection col) {
        this.col = col;
    }
    
	/**
	 * Almacena los datos de la consulta Avanzada.
	 */
	private Collection col;
	
	
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
		
		if(this.estado.equals("buscar")){
		
			if(this.tipoModificacion.equals("consulta") || this.tipoModificacion.equals("modificar"))
			{
				if(this.codigoContrato==-1 || this.codigoContrato==0 ){
					errores.add("", new ActionMessage("errors.required", "El campo convenio"));
				}
			}
			if(this.criteriosBusqueda!=null){
				if(this.criteriosBusqueda.length>0){
					for(int i=0;i<criteriosBusqueda.length;i++){
						if(this.codigoContrato==-1 && this.criteriosBusqueda[i].equals("codigoContrato")){
							errores.add("", new ActionMessage("errors.required", "El campo contrato"));
						}
					
						
						if(this.codigo<1 && this.criteriosBusqueda[i].equals("codigo")){
							errores.add("", new ActionMessage("errors.required", "El campo Código"));
						}
						if(this.codigoViaIngreso<1 && this.criteriosBusqueda[i].equals("codigoViaIngreso")){
							errores.add("", new ActionMessage("errors.required", "El campo Vía de Ingreso"));
						}
						if(this.codigoTipoRecargo<1 && this.criteriosBusqueda[i].equals("codigoTipoRecargo")){
							errores.add("", new ActionMessage("errors.required", "El campo Tipo de Recargo"));
						}
						if(this.codigoEspecialidad<1 && this.criteriosBusqueda[i].equals("codigoEspecialidad")){
							errores.add("", new ActionMessage("errors.required", "El campo Especialidad"));
						}
						if(this.codigoServicio<1 && this.criteriosBusqueda[i].equals("codigoServicio")){
							errores.add("", new ActionMessage("errors.required", "El campo Procedimiento"));
						}
						if(this.porcentaje<0 && this.criteriosBusqueda[i].equals("porcentaje")){
							errores.add("", new ActionMessage("errors.floatMayorQue", "El campo porcentaje","cero"));				
						}
						if(this.valor<0 && this.criteriosBusqueda[i].equals("valor")){
							errores.add("", new ActionMessage("errors.floatMayorQue", "El campo valor","cero"));				
						}
						
						
					}
				}
			}
		}
		return errores;
	}	
	
	/**
	 * Método que inicializa todos los atributos de la forma
	 */
	public void reset() 
	{
		this.recargosTarifas = new ArrayList();
		this.accion="";
		this.codigo=-1;
		this.codigoContrato=-1;
		this.codigoEspecialidad=-1;
		this.codigoServicio=-1;
		this.codigoTipoRecargo=-1;
		this.codigoViaIngreso=-1;
		this.convenio=-1;
		this.criteriosBusqueda=null;
		this.nombreEspecialidad="";
		this.nombreServicio="";
		this.nombreTipoRecargo="";
		this.nombreViaIngreso="";
		this.numeroContrato="";
		this.porcentaje=Double.NaN;
		this.valor=Double.NaN;
	
				
	}	
	
	/**
	 * Retorna el recargo de la tarifa al indice dado dentro de la colección.
	 * Si el indice es mayor o igual al tamaño de la colección retorna null
	 * @param indice. int, indice a retornar de la colección
	 * @return
	 */
	public RecargoTarifaForm getRecargoTarifa(int indice)
	{
		if( indice >= this.recargosTarifas.size() )
			return null;
		else
			return (RecargoTarifaForm)this.recargosTarifas.get(indice);		
	}
	
	/**
	 * Adiciona al final de la colección el recargo de tarifa dada.
	 * @param tarifa. RecargoTarifaForm, Recargo de la Tarifa a ingresar
	 */
	public void setRecargoTarifa(RecargoTarifaForm recargoTarifa)
	{
		this.recargosTarifas.add(recargoTarifa);
	}
	
	/**
	 * Retorna el número de recargos existentes en la colección
	 * @return
	 */
	public int getNumRecargosTarifas()
	{
		return this.recargosTarifas.size();
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
	 * Returns the criteriosBusqueda.
	 * @return String[]
	 */
	public String[] getCriteriosBusqueda()
	{
		return criteriosBusqueda;
	}

	/**
	 * Returns the recargosTarifas.
	 * @return ArrayList
	 */
	public ArrayList getRecargosTarifas()
	{
		return recargosTarifas;
	}

	/**
	 * Sets the criteriosBusqueda.
	 * @param criteriosBusqueda The criteriosBusqueda to set
	 */
	public void setCriteriosBusqueda(String[] criteriosBusqueda)
	{
		this.criteriosBusqueda = criteriosBusqueda;
	}

	/**
	 * Sets the recargosTarifas.
	 * @param recargosTarifas The recargosTarifas to set
	 */
	public void setRecargosTarifas(ArrayList recargosTarifas)
	{
		this.recargosTarifas = recargosTarifas;
	}

	/**
	 * Returns the columna.
	 * @return String
	 */
	public String getColumna()
	{
		return columna;
	}

	/**
	 * Returns the ultimaPropiedad.
	 * @return String
	 */
	public String getUltimaPropiedad()
	{
		return ultimaPropiedad;
	}

	/**
	 * Sets the columna.
	 * @param columna The columna to set
	 */
	public void setColumna(String columna)
	{
		this.columna = columna;
	}

	/**
	 * Sets the ultimaPropiedad.
	 * @param ultimaPropiedad The ultimaPropiedad to set
	 */
	public void setUltimaPropiedad(String ultimaPropiedad)
	{
		this.ultimaPropiedad = ultimaPropiedad;
	}

	/**
	 * Returns the tipoModificacion.
	 * @return String
	 */
	public String getTipoModificacion()
	{
		return tipoModificacion;
	}

	/**
	 * Sets the tipoModificacion.
	 * @param tipoModificacion The tipoModificacion to set
	 */
	public void setTipoModificacion(String tipoModificacion)
	{
		this.tipoModificacion = tipoModificacion;
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

}
