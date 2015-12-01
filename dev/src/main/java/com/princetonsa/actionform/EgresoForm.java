/*
 * @(#)EgresoForm.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */

package com.princetonsa.actionform;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.UtilidadFecha;
import util.UtilidadValidacion;

/**
 * Form para la funcionalidad de Egreso. 
 * 
 *	@version 1.0, Jul 17, 2003
 */
public class EgresoForm extends ValidatorForm
{
	/**
	 * Fecha en la que el médico asegura haber dado la orden
	 * de salida
	 */
	private String fechaEgreso="11/07/1999";

	/**
	 * Hora en la que el médico asegura haber dado la orden
	 * de salida
	 */
	private String horaEgreso="10:35";

	/**
	 * Fecha del sistema en la cual se dio la orden
	 * de salida
	 */
	private String fechaGrabacionEgreso="11/07/1999";

	/**
	 * Hora del sistema en la cual se dio la orden
	 * de salida
	 */
	private String horaGrabacionEgreso="10:35";

	/**
	 * Caracter que indica en que tipo de admisión
	 * se encuentra esta cuenta
	 */
	private char tipoAdmision='h';

	/**
	 * Estado en el que se encuentra el proceso.
	 */
	private String estado = "";

	/**
	 * En el caso que el estado sea finalizar,
	 * hay que distinguir entre que se debe hacer.
	 * En esta clase esta por uniformidad y seguridad
	 * (cualquiera puede poner el parámetro en el path)
	 * Valores validos: insertar
	 * 
	 */
	private String accionAFinalizar="";

	/**
	 * Número de autorización
	 */
	private String numeroAutorizacion="";
	
	/**
	 * En el estado resumen se puede mostrar un mensaje
	 * y este atributo es el encargado de manejarlo
	 */
	private String mensajeResumen="";

	/**
	 * Boolean que dice el mensaje de resumen se acaba de
	 * poner o corresponde una inserción anterior
	 */
	private boolean acabaAparecerResumen=false;

	/**
	 * Si estamos en el caso de un semiEgreso, este dice si
	 * el semiEgreso se completó o no
	 */
	private boolean semiEgresoCompleto=true;

	
	//***********************************************************************
	/**
	 * Variables usadas para el resumen de SemiEgreso en Reusmen de Atenciones
	 */
	private boolean resumenSemiEgreso;
	private int idCuenta;
	//variable que se usa para cambiar la vista del resumen del semiEgreso
	private boolean indicador;
	//***********************************************************************
	/**
	 * Variables usadas para el resumen de Egresos Automáticos en Resumen de Atenciones
	 */
	private boolean resumenEgresoAutomatico;
	//variable que se usa para cambiar la vista del resumen del Egreso Automático
	private boolean indicador1;
	//**********************************************************************
	
	/**
	 * fecha de salida de piso
	 */
	private String fechaSalidaDePiso;
	
	/**
	 * hota de salida de piso
	 */
	private String horaSalidaDePiso;
	
	/**
	 * Nombre del usuario que realiza el egreso
	 */
	private String nombreUsuario;
	
	/**
	 * Login del usuario que realiza el egreso
	 */
	private String loginUsuario;
	
	/**
	 * Campo para almacenar el tipo de monitoreo
	 */
	private int codigoTipoMonitoreo;
	
	
	/******************************************************************************************************************
	 *  modificado por anexo 747
	 */
		private boolean paramBoletaSalidaDespuesdeIngresoCerrado=false;
	
		/**
		 * encargado de almacenar los nuevos datos
		 */
		private HashMap datosNuevos= new HashMap ();
		
		/**
		 * encargado de almacenar los signos vitales
		 */
		private ArrayList signosVitales=new ArrayList ();
		
		
		private HashMap datosIngresos = new HashMap();
		/*
		 ************************ SETTERS AND GETTERS**********************************
		 */

			public boolean isParamBoletaSalidaDespuesdeIngresoCerrado() {
				return paramBoletaSalidaDespuesdeIngresoCerrado;
			}
			public void setParamBoletaSalidaDespuesdeIngresoCerrado(
					boolean paramBoletaSalidaDespuesdeIngresoCerrado) {
				this.paramBoletaSalidaDespuesdeIngresoCerrado = paramBoletaSalidaDespuesdeIngresoCerrado;
			}


			public ArrayList getSignosVitales() {
				return signosVitales;
			}
			public void setSignosVitales(ArrayList signosVitales) {
				this.signosVitales = signosVitales;
			}

			public HashMap getDatosNuevos() {
				return datosNuevos;
			}
			public void setDatosNuevos(HashMap datosNuevos) {
				this.datosNuevos = datosNuevos;
			}
			public Object getDatosNuevos(String key) {
				return datosNuevos.get(key);
			}
			public void setDatosNuevos(String key,Object value) {
				this.datosNuevos.put(key, value);
			}
			public HashMap getDatosIngresos() {
				return datosIngresos;
			}
			public void setDatosIngresos(HashMap datosIngresos) {
				this.datosIngresos = datosIngresos;
			}
			public Object getDatosIngresos(String key) {
				return datosIngresos.get(key);
			}
			public void setDatosIngresos(String key,Object value) {
				this.datosIngresos.put(key, value);
			}
			
		/*
		 * ************************************************************************
		 */
	/**
	 * 
	 ******************************************************************************************************************/
	
	/**
	 * Reset NO estandar, para limpiar al terminar todo el proceso, NO al
	 * cambiar de página. Limpia T ODOS los datos menos el estado y el
	 * mensaje de resumen
	 */
	
	public void reset ()
	{
		fechaEgreso="11/07/1999";
		horaEgreso="10:35";
		fechaGrabacionEgreso="11/07/1999";
		horaGrabacionEgreso="10:35";
		tipoAdmision='h';
		accionAFinalizar="";
		numeroAutorizacion="";
		semiEgresoCompleto=true;
		//************************
		resumenSemiEgreso=false;
		indicador=false;
		//******************************
		resumenEgresoAutomatico=false;
		indicador1=false;
		
		
		this.nombreUsuario = "";
		this.loginUsuario = "";
		this.codigoTipoMonitoreo = ConstantesBD.codigoNuncaValido;
		
	}
	
	public void resetNuevosDatos ()
	{
		this.paramBoletaSalidaDespuesdeIngresoCerrado=false;
		this.datosNuevos=new HashMap ();
		//this.datosIngresos= new HashMap ();
		//this.setDatosIngresos("numRegistros", 0);
	}



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
		// Perform validator framework validations
		ActionErrors errors = super.validate(mapping, request);
		if(this.estado.equals("salir"))
		{
		    try
	        {
			    Connection con= util.UtilidadBD.abrirConexion(); 
			    System.out.print("Es cuenta de urgencias????---->"+UtilidadValidacion.esCuentaUrgencias(con, this.idCuenta));
			    if(!UtilidadValidacion.esCuentaUrgencias(con, this.idCuenta))
			    {    
				    if(!UtilidadFecha.validarFecha(this.getFechaSalidaDePiso()))
				    {
				        errors.add("Fecha Salida de piso", new ActionMessage("errors.formatoFechaInvalido", "salida de piso"));		
				    }
				    if(!UtilidadFecha.validacionHora(this.getHoraSalidaDePiso()).puedoSeguir)
				    {
				        errors.add("Hora Salida de piso", new ActionMessage("errors.formatoHoraInvalido", "salida de piso"));	
				    }
				    if(errors.isEmpty())
				    { 
			            if(UtilidadValidacion.existeEvolucionConFechaSuperiorFormatoAp(con, this.idCuenta, this.fechaSalidaDePiso, this.horaSalidaDePiso))
				        {
				            errors.add("", new ActionMessage("errors.fechaAnteriorIgualActual", "salida de piso", "de la evolución que tiene orden de salida"));
				        }
				        if(UtilidadFecha.esFechaMenorQueOtraReferencia(UtilidadFecha.getFechaActual(), this.getFechaSalidaDePiso()))
				        {
				            errors.add("", new ActionMessage("errors.fechaPosteriorIgualActual", "salida de piso", "actual"));
				        }
				        else if (UtilidadFecha.getFechaActual().equals(this.getFechaSalidaDePiso()))
				        {
				           String t[]=this.getHoraSalidaDePiso().split(":");
				           String t1[]=UtilidadFecha.getHoraActual().split(":");
				           if(Integer.parseInt(t[0])>Integer.parseInt(t1[0]))
				           {
				               errors.add("", new ActionMessage("errors.fechaHoraPosteriorIgualActual", "salida de piso", "actual"));
				           }
				           else if (t[0].equals(t1[0]))
				           {
				               if(Integer.parseInt(t[1])>Integer.parseInt(t1[1]))
					           {
					               errors.add("", new ActionMessage("errors.fechaHoraPosteriorIgualActual", "salida de piso", "actual"));
					           }
				           }
				        }
				    }    
			    }    
			    util.UtilidadBD.cerrarConexion(con);
	        }//try
		    catch(SQLException sqle){}   
		}// if estado
		return errors;
	}	
	
	/**
	 * @return Returns the indicador.
	 */
	public boolean isIndicador() {
		return indicador;
	}
	/**
	 * @param indicador The indicador to set.
	 */
	public void setIndicador(boolean indicador) {
		this.indicador = indicador;
	}
	/**
	 * @return
	 */
	public String getEstado() {
		return estado;
	}

	/**
	 * @return
	 */
	public String getFechaEgreso() {
		return fechaEgreso;
	}

	/**
	 * @return
	 */
	public String getFechaGrabacionEgreso() {
		return fechaGrabacionEgreso;
	}

	/**
	 * @return Returns the idCuenta.
	 */
	public int getIdCuenta() {
		return idCuenta;
	}
	/**
	 * @param idCuenta The idCuenta to set.
	 */
	public void setIdCuenta(int idCuenta) {
		this.idCuenta = idCuenta;
	}
	/**
	 * @return Returns the resumenSemiEgreso.
	 */
	public boolean isResumenSemiEgreso() {
		return resumenSemiEgreso;
	}
	/**
	 * @param resumenSemiEgreso The resumenSemiEgreso to set.
	 */
	public void setResumenSemiEgreso(boolean resumenSemiEgreso) {
		this.resumenSemiEgreso = resumenSemiEgreso;
	}
	/**
	 * @return
	 */
	public String getHoraEgreso() {
		return horaEgreso;
	}

	/**
	 * @return
	 */
	public String getHoraGrabacionEgreso() {
		return horaGrabacionEgreso;
	}

	/**
	 * @return
	 */
	public char getTipoAdmision() {
		return tipoAdmision;
	}

	/**
	 * @param string
	 */
	public void setEstado(String string) {
		estado = string;
	}

	/**
	 * @param string
	 */
	public void setFechaEgreso(String string) {
		fechaEgreso = string;
	}

	/**
	 * @param string
	 */
	public void setFechaGrabacionEgreso(String string) {
		fechaGrabacionEgreso = string;
	}

	/**
	 * @param string
	 */
	public void setHoraEgreso(String string) {
		horaEgreso = string;
	}

	/**
	 * @param string
	 */
	public void setHoraGrabacionEgreso(String string) {
		horaGrabacionEgreso = string;
	}

	/**
	 * @param c
	 */
	public void setTipoAdmision(char c) {
		tipoAdmision = c;
	}

	/**
	 * @return
	 */
	public String getAccionAFinalizar() {
		return accionAFinalizar;
	}

	/**
	 * @param string
	 */
	public void setAccionAFinalizar(String string) {
		accionAFinalizar = string;
	}

	/**
	 * @return
	 */
	public String getNumeroAutorizacion() {
		return numeroAutorizacion;
	}

	/**
	 * @param string
	 */
	public void setNumeroAutorizacion(String string) {
		numeroAutorizacion = string;
	}

	/**
	 * @return
	 */
	public String getMensajeResumen() {
		return mensajeResumen;
	}

	/**
	 * @param string
	 */
	public void setMensajeResumen(String string) {
		mensajeResumen = string;
	}

	/**
	 * @return
	 */
	public boolean isAcabaAparecerResumen() {
		return acabaAparecerResumen;
	}

	/**
	 * @param b
	 */
	public void setAcabaAparecerResumen(boolean b) {
		acabaAparecerResumen = b;
	}

	/**
	 * @return
	 */
	public boolean getSemiEgresoCompleto() {
		return semiEgresoCompleto;
	}

	/**
	 * @param b
	 */
	public void setSemiEgresoCompleto(boolean b) {
		semiEgresoCompleto = b;
	}

	/**
	 * @return Returns the indicador1.
	 */
	public boolean isIndicador1() {
		return indicador1;
	}
	/**
	 * @param indicador1 The indicador1 to set.
	 */
	public void setIndicador1(boolean indicador1) {
		this.indicador1 = indicador1;
	}
	/**
	 * @return Returns the resumenEgresoAutomatico.
	 */
	public boolean isResumenEgresoAutomatico() {
		return resumenEgresoAutomatico;
	}
	/**
	 * @param resumenEgresoAutomatico The resumenEgresoAutomatico to set.
	 */
	public void setResumenEgresoAutomatico(boolean resumenEgresoAutomatico) {
		this.resumenEgresoAutomatico = resumenEgresoAutomatico;
	}
    /**
     * @return Returns the fechaSalidaDePiso.
     */
    public String getFechaSalidaDePiso() {
        return fechaSalidaDePiso;
    }
    /**
     * @param fechaSalidaDePiso The fechaSalidaDePiso to set.
     */
    public void setFechaSalidaDePiso(String fechaSalidaDePiso) {
        this.fechaSalidaDePiso = fechaSalidaDePiso;
    }
    /**
     * @return Returns the horaSalidaDePiso.
     */
    public String getHoraSalidaDePiso() {
        return horaSalidaDePiso;
    }
    /**
     * @param horaSalidaDePiso The horaSalidaDePiso to set.
     */
    public void setHoraSalidaDePiso(String horaSalidaDePiso) {
        this.horaSalidaDePiso = horaSalidaDePiso;
    }

	/**
	 * @return the loginUsuario
	 */
	public String getLoginUsuario() {
		return loginUsuario;
	}

	/**
	 * @param loginUsuario the loginUsuario to set
	 */
	public void setLoginUsuario(String loginUsuario) {
		this.loginUsuario = loginUsuario;
	}

	/**
	 * @return the nombreUsuario
	 */
	public String getNombreUsuario() {
		return nombreUsuario;
	}

	/**
	 * @param nombreUsuario the nombreUsuario to set
	 */
	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}

	/**
	 * @return the codigoTipoMonitoreo
	 */
	public int getCodigoTipoMonitoreo() {
		return codigoTipoMonitoreo;
	}

	/**
	 * @param codigoTipoMonitoreo the codigoTipoMonitoreo to set
	 */
	public void setCodigoTipoMonitoreo(int codigoTipoMonitoreo) {
		this.codigoTipoMonitoreo = codigoTipoMonitoreo;
	}
}
