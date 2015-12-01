/*
 * @(#)ReversionEgresoForm.java
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
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.atencion.Egreso;

import util.ConstantesBD;
import util.UtilidadFecha;
import util.Utilidades;

/**
 * Form para la funcionalidad de Reversión de Egreso.
 * 
 *	@version 1.0, Jul 23, 2003
 */
public class ReversionEgresoForm extends ValidatorForm
{

	/**
	 * Estado en el que se encuentra el proceso.
	 */
	private String estado = "";

	/**
	 * En el caso que el estado sea finalizar,
	 * hay que distinguir entre que se debe hacer.
	 * En esta clase esta por uniformidad y seguridad
	 * (cualquiera puede poner el parámetro en el path)
	 */
	private String accionAFinalizar="";

	/**
	 * Esta variable le indica al JSP si debe mostrar los datos o si
	 * debe mostrar el resultado (Se ingreso bien...)
	 */
	private boolean seDebeMostrarResultado=false;
	
	/**
	 * Esta variable le indica al JSP si debe mostrar 
	 * la cama o no
	 */
	private boolean seDebeMostrarCama=false;
	
	/**
	 * Variable que indica si el ingreso es de tipo hospital día
	 */
	private boolean hospitalDia;
	
	
	/**
	 * Datos de la cama para reversar el egreso
	 */
	private HashMap<String,Object> datosCama = new HashMap<String,Object>();
	
	
	
	

	/**
	 * En este campo se almacena la explicación
	 * dada a la reversión del egreso
	 */
	private String motivoReversion="";
	
	/**
	 * Si estamos en el caso de un semiEgreso, este dice si
	 * el semiEgreso se completó o no
	 */
	private boolean semiEgresoCompleto=true;

	/** Indica el código del centro de costo de la cama */
	private String is_codigoCentroCostoCama = "";

	/**
	 * fecha de ingreso de piso
	 */
	private String fechaIngresoDePiso;
	
	/**
	 * hota de salida de piso
	 */
	private String horaIngresoDePiso;
	
	/**
	 * id de la cuenta
	 */
	private int idCuenta;
	
	/**
	 * Reset NO estandar, para limpiar al terminar todo el proceso, NO al
	 * cambiar de página. Limpia T ODOS los datos menos el estado.
	 */
	public void reset ()
	{
		this.accionAFinalizar="";
		this.seDebeMostrarResultado=false;
		this.datosCama = new HashMap();
		
		motivoReversion="";
		seDebeMostrarCama=false;
		semiEgresoCompleto=true;
		hospitalDia = false;
		is_codigoCentroCostoCama = "";
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
//		se obtiene el paciente cargado en sesion.
		PersonaBasica paciente = (PersonaBasica)request.getSession().getAttribute("pacienteActivo");
		
		if (estado!=null&&estado.equals("salir")&&accionAFinalizar!=null&&accionAFinalizar.equals("insertar"))
		{
			System.out.print("\n entrando datosCama ");
			Utilidades.imprimirMapa(datosCama);
			if (this.seDebeMostrarCama)
			{
				if (this.getDatosCama("codigoCama")==null||this.getDatosCama("codigoCama").toString().equals(""))
				{
					System.out.print("\n pentre1 |!!!!!!!!!!!!!!!!!!!!!!!!!!!!! ");
					//Si Hospitalizacion-Cirugia Ambulatoria no es requerida cama. o si es de urgencias
					if ((paciente.getCodigoUltimaViaIngreso()!=ConstantesBD.codigoViaIngresoHospitalizacion 
							|| !paciente.getCodigoTipoPaciente().equals(ConstantesBD.tipoPacienteCirugiaAmbulatoria))
							&& paciente.getCodigoUltimaViaIngreso()!=ConstantesBD.codigoViaIngresoUrgencias)
					{
						System.out.print("\n problemasssssssssssssssssssssssssssssssssssssssssssssss via de ingreso --> "+paciente.getCodigoUltimaViaIngreso()+"   urgencias es --> "+ConstantesBD.codigoViaIngresoUrgencias);
								errors.add("camaNoSeleccionada", new ActionMessage("errors.seleccion", "cama"));
								System.out.print("\n pentre2 |!!!!!!!!!!!!!!!!!!!!!!!!!!!!! ");
					}
				}
			}
			System.out.print("\n pase1 |!!!!!!!!!!!!!!!!!!!!!!!!!!!!! ");
			//Solo en este caso, el motivo de la reversion no debe ser ni nulo ni vacio
			//el tamaño maximo se maneja en el JSP y por val. XML
			if (motivoReversion==null||motivoReversion.equals(""))
			{
				errors.add("motivoVacio", new ActionMessage("errors.required", "El motivo de la reversión"));
			}
			
			if(errors.isEmpty() && this.seDebeMostrarCama)
			{
			    if(!UtilidadFecha.validarFecha(this.getFechaIngresoDePiso()))
			    {
			        errors.add("Fecha Ingreso de piso", new ActionMessage("errors.formatoFechaInvalido", "salida de piso"));		
			    }
			    if(!UtilidadFecha.validacionHora(this.getHoraIngresoDePiso()).puedoSeguir)
			    {
			        errors.add("Hora Ingreso de piso", new ActionMessage("errors.formatoHoraInvalido", "salida de piso"));	
			    }
			    if(errors.isEmpty())
			    { 
			        try
			        {
			            Connection con= util.UtilidadBD.abrirConexion();
			            Egreso mundoEgreso= new Egreso();
			            mundoEgreso.cargarFechaHoraEgreso(con, this.idCuenta);
			            
				        if(UtilidadFecha.esFechaMenorQueOtraReferencia( this.fechaIngresoDePiso, UtilidadFecha.conversionFormatoFechaAAp(mundoEgreso.getFechaEgreso())))
				        {
				            errors.add("", new ActionMessage("errors.fechaAnteriorIgualActual", "ingreso de piso", "de egreso"));
				        }
				        else if(UtilidadFecha.conversionFormatoFechaAAp(mundoEgreso.getFechaEgreso()).equals(this.fechaIngresoDePiso))
				        {
				            String t[]=this.getHoraIngresoDePiso().split(":");
					        String t1[]=UtilidadFecha.convertirHoraACincoCaracteres(mundoEgreso.getHoraEgreso()).split(":");
					        if(Integer.parseInt(t[0])<Integer.parseInt(t1[0]))
					        {
					            errors.add("", new ActionMessage("errors.fechaHoraAnteriorIgualActual", "ingreso de piso", "egreso"));
					        }
					        else if (t[0].equals(t1[0]))
					        {
					            if(Integer.parseInt(t[1])<Integer.parseInt(t1[1]))
						        {
						            errors.add("", new ActionMessage("errors.fechaHoraAnteriorIgualActual", "ingreso de piso", "egreso"));
						        }
					        }
				        }
				        if(UtilidadFecha.esFechaMenorQueOtraReferencia(UtilidadFecha.getFechaActual(), this.getFechaIngresoDePiso()))
				        {
				            errors.add("", new ActionMessage("errors.fechaPosteriorIgualActual", "ingreso de piso", "actual"));
				        }
				        else if (UtilidadFecha.getFechaActual().equals(this.getFechaIngresoDePiso()))
				        {
				           String t[]=this.getHoraIngresoDePiso().split(":");
				           String t1[]=UtilidadFecha.getHoraActual().split(":");
				           if(Integer.parseInt(t[0])>Integer.parseInt(t1[0]))
				           {
				               errors.add("", new ActionMessage("errors.fechaHoraPosteriorIgualActual", "ingreso de piso", "actual"));
				           }
				           else if (t[0].equals(t1[0]))
				           {
				               if(Integer.parseInt(t[1])>Integer.parseInt(t1[1]))
					           {
					               errors.add("", new ActionMessage("errors.fechaHoraPosteriorIgualActual", "ingreso de piso", "actual"));
					           }
				           }
				        }
			            util.UtilidadBD.cerrarConexion(con);
			        }catch(SQLException sqle){}   
			    }        
			}
			
		}
		System.out.print("\n salientdo datosCama");
		Utilidades.imprimirMapa(datosCama);
		return errors;
	}	

	
	/**
	 * @return
	 */
	public String getAccionAFinalizar() {
		return accionAFinalizar;
	}

	/**
	 * @return
	 */
	public String getEstado() {
		return estado;
	}

	/**
	 * @param string
	 */
	public void setAccionAFinalizar(String string) {
		accionAFinalizar = string;
	}

	/**
	 * @param string
	 */
	public void setEstado(String string) {
		estado = string;
	}

	/**
	 * @return
	 */
	public boolean isSeDebeMostrarResultado() {
		return seDebeMostrarResultado;
	}

	/**
	 * @return
	 */
	public boolean getSeDebeMostrarResultado() {
		return seDebeMostrarResultado;
	}

	/**
	 * @param b
	 */
	public void setSeDebeMostrarResultado(boolean b) {
		seDebeMostrarResultado = b;
	}

	

	/**
	 * @return
	 */
	public String getMotivoReversion() {
		return motivoReversion;
	}

	/**
	 * @param string
	 */
	public void setMotivoReversion(String string) {
		motivoReversion = string;
	}


	/**
	 * @return
	 */
	public boolean isSeDebeMostrarCama() {
		return seDebeMostrarCama;
	}

	/**
	 * @return
	 */
	public boolean getSeDebeMostrarCama() {
		return seDebeMostrarCama;
	}
	/**
	 * @param b
	 */
	public void setSeDebeMostrarCama(boolean b) {
		seDebeMostrarCama = b;
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

	/** Obtiene el código del centro de costo de la cama */
	public String getCodigoCentroCostoCama()
	{
		return is_codigoCentroCostoCama;
	}

	/** Asigna el codigo del centro de costo de la cama */
	public void setCodigoCentroCostoCama(String as_codigoCentroCostoCama)
	{
		if(as_codigoCentroCostoCama != null)
			is_codigoCentroCostoCama = as_codigoCentroCostoCama.trim();
	}
    /**
     * @return Returns the fechaIngresoDePiso.
     */
    public String getFechaIngresoDePiso() {
        return fechaIngresoDePiso;
    }
    /**
     * @param fechaIngresoDePiso The fechaIngresoDePiso to set.
     */
    public void setFechaIngresoDePiso(String fechaIngresoDePiso) {
        this.fechaIngresoDePiso = fechaIngresoDePiso;
    }
    /**
     * @return Returns the horaIngresoDePiso.
     */
    public String getHoraIngresoDePiso() {
        return horaIngresoDePiso;
    }
    /**
     * @param horaIngresoDePiso The horaIngresoDePiso to set.
     */
    public void setHoraIngresoDePiso(String horaIngresoDePiso) {
        this.horaIngresoDePiso = horaIngresoDePiso;
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
	 * @return the datosCama
	 */
	public HashMap getDatosCama() {
		return datosCama;
	}

	/**
	 * @param datosCama the datosCama to set
	 */
	public void setDatosCama(HashMap<String,Object> datosCama) {
		this.datosCama = datosCama;
	}
	
	/**
	 * @return the datosCama
	 */
	public Object getDatosCama(String key) {
		return datosCama.get(key);
	}

	/**
	 * @param Asigna elemento a mapa  datosCama to set
	 */
	public void setDatosCama(String key,Object obj) {
		this.datosCama.put(key,obj);
	}

	/**
	 * @return the hospitalDia
	 */
	public boolean isHospitalDia() {
		return hospitalDia;
	}

	/**
	 * @param hospitalDia the hospitalDia to set
	 */
	public void setHospitalDia(boolean hospitalDia) {
		this.hospitalDia = hospitalDia;
	}
}