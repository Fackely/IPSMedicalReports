/*
 * Created on 29/09/2005
 * 
 * @author <a href="mailto:artotor@hotmail.com">Jorge Armando Osorio Velásquez</a>
 * 
 * Copyright Princeton S.A. &copy;&reg; 2005. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 *
 */
package com.princetonsa.actionform.tesoreria;

import java.sql.Connection;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import com.princetonsa.mundo.tesoreria.AnulacionRecibosCaja;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadValidacion;
import util.Utilidades;

/**
 * @version 1.0, 29/09/2005
 * @author <a href="mailto:armando@PrincetonSA.com">Jorge Armando Osorio Velásquez/a>
 */
public class AnulacionRecibosCajaForm extends ValidatorForm 
{
//	------------------------------------------------------------DEFINICION DE VARAIBLES---------------------------------------------------------------------------//
	/**
	 * Variable para manejar el estado en el que se encuentra la funcionalidad
	 */
	private String estado;
	
	/**
	 * Variable que hace referencia al consecutivo obtenido de la tabla consecutivos asignado para la anulacion
	 */
	private String numeroAnulacionReciboCaja;
	
	/**
	 * Variable para manejar el numero del recibo de caja que se desea anular.
	 */
	private String numeroReciboCaja;
	
	/**
	 * Variable para manejar la fecha del Recibo de Caja
	 */
	private String fechaReciboCaja;
	
	/**
	 * Variable para manejar la hora del recibo de caja.
	 */
	private String horaReciboCaja;
	
	
	/**
	 * Variable para manejar la fecha de Anulacion Recibo de Caja
	 */
	private String fechaAnulacionReciboCaja;
	
	/**
	 * Variable para manejar la hora de Anulacion recibo de caja.
	 */
	private String horaAnulacionReciboCaja;
	
	/**
	 * Consecutivo de la caja de Recibo
	 */
	private int consecutivoCaja;
	
	/**
	 * Codigo de la caja de recibo(Es el codigo visible para el usuario)
	 */
	private int codigoCaja;
	
	/**
	 * Variable para manejar la descripcion de la Caja.
	 */
	private String descripcionCaja;
	
	/**
	 * Variable para manejar el login de usuario asignado a la caja del RC.
	 */
	private String loginUsuarioCaja;
	
	/**
	 * Variable para manejar el nombre del usuario relacionado a la caja del RC.
	 */
	private String nombreUsuarioCaja;
	
	/**
	 * Campo para manejar el valor que tiene este campo en la tabla RC.
	 */
	private String recibidoDe;
	
	/**
	 * Mapa para manejar los conceptos del RC 
	 */
	private HashMap conceptosRC;
	
	/**
	 * Variable para tener el total del recibo de caja (Suma del valor conceptos.)
	 */
	private double totalRecibidoCaja;
	
	/**
	 * Variable para manejar el motivo por el cual se anula el RC.
	 */
	private int motivo;
	
	/**
	 * Variable para manejar las observaciones del RC.
	 */
	private String observaciones;
	
	/**
	 * institucion del usuario en el sistema
	 */
	private int institucion;
	
	/**
	 * Variable que indica si se cargo la informacion o no.
	 */
	private boolean informacionCargada;
	
	/**
	 * Variable para manejar el usuario que anula el recibo de caja.
	 */
	private String usuarioAnula;
	
	/**
	 * codigoCentroAtencion
	 */
	private int codigoCentroAtencion;
	
	/**
	 * nombreCentroAtencion
	 */
	private String nombreCentroAtencion;
	
	/**
	 * atributo que determian si se puede eliminar el recibo de caja
	 */
	private String eliminarRC;
	
	
	/**
	 * atributo para determinar si el Recibo de Caja por concepto de Ingreso es de tipo Anticipos Convenios Odontologia
	 */
	private boolean existeReciboCajaXConceptAnticipConvenioOdonto;
	
	/**
	 * atibuto que contiene el numero del contrato asociado al convenio del recibo de caja
	 */
	private int numContratoOdontologia;
	
	/**
	 * 
	 */
	private String codigoReciboCaja;
	
//  ----------------------------------------------------------FIN DEFINICION DE VAIRABLES-------------------------------------------------------------------------//
    
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores= new ActionErrors();
		   
	    
		if(estado.equals("cargarReciboCaja"))
		{
			AnulacionRecibosCaja mundo=new AnulacionRecibosCaja();
			Connection con = null;
			//intentamos abrir una conexion con la fuente de datos 
			con = UtilidadBD.abrirConexion();
			mundo.cargarReciboCaja(con,this.numeroReciboCaja,this.institucion);
			if(numeroReciboCaja.trim().equals(""))
			{
			    errores.add("Recibo Caja Requerido", new ActionMessage("errors.required","El Recibo de Caja"));
			    this.reset();
			}
			else
			{
				this.codigoReciboCaja=UtilidadValidacion.existeReciboCaja(this.numeroReciboCaja,this.institucion,this.codigoCentroAtencion);
			    if(Utilidades.convertirAEntero(this.codigoReciboCaja)<=0)
			    {	
			    	
			        //errores.add("Recibo Caja Inexistente", new ActionMessage("error.reciboCajaInexistente",this.numeroReciboCaja));
			        errores.add("", new ActionMessage("error.recibosCaja.noPuedeAnular", this.numeroReciboCaja, mundo.getNombreCentroAtencion()));
			        this.reset();
			    }
			    else
			    {
			        String estadoReciboCaja[]=(Utilidades.obtenerEstadoReciboCaja(this.codigoReciboCaja,this.institucion).split(ConstantesBD.separadorSplit));
			        if(Integer.parseInt(estadoReciboCaja[0])!=ConstantesBD.codigoEstadoReciboCajaRecaudado)
			        {
			            errores.add("Recibo Caja Estado Diferente", new ActionMessage("error.reciboCajaEstadoDiferente",this.numeroReciboCaja,estadoReciboCaja[1]));
			            this.reset();
			        }
			    }
			}
			UtilidadBD.closeConnection(con);
		}
		else if(estado.equals("guardar")&&this.isInformacionCargada())
        {
		    String estadoReciboCaja[]=(Utilidades.obtenerEstadoReciboCaja(this.codigoReciboCaja,this.institucion).split(ConstantesBD.separadorSplit));
	        if(Integer.parseInt(estadoReciboCaja[0])!=ConstantesBD.codigoEstadoReciboCajaRecaudado)
	        {
	            errores.add("Recibo Caja Estado Diferente", new ActionMessage("error.reciboCajaEstadoDiferente",this.numeroReciboCaja,estadoReciboCaja[1]));
	            this.reset();
	        }
	        else
	        {
	            if(this.motivo==ConstantesBD.codigoNuncaValido)
	            {
	                errores.add("Motivo de anulacion requerido", new ActionMessage("errors.required","El motivo de Anulacion"));
	            }
	            /*
	             * Validaciones adicionales según el tipo de concepto de ingreso  del recibo de caja:
	             * Conceptos Tipo  0 = Ninguno  y Concepto Tipo 1= Pago Pacientes
	             * 			En estos casos el sistema no requiere validaciones adicionales.
	             * Concepto Tipo 2 = Pago Convenios
	             * 			En este caso se debe verificar que el recibo que se está anulando no esté 
	             * 			registrado en aplicación de pagos de cartera.
	             * Concepto Tipo 3 = Pago Otras Cuentas x Cobrar
	             * 			Es este caso, por ahora el sistema no realiza validaciones adicionales. 
	             * 			Está pendiente la documentación de las funcionalidades de otras cuentas por cobrar. 
	             * 			(Se debe contemplar su adecuación más adelante)
	             * Concepto Tipo 4 = Pago cartera particular
	             * 			Es este caso, por ahora el sistema no realiza validaciones adicionales.
	             * 			Está pendiente la documentación de las funcionalidades de cartera particulares. 
	             * 			(Se debe contemplar su adecuación más adelante)
	             * Concepto Tipo 5 = Abono pacientes
	             * 			En este caso el sistema debe validar que exista en los registros de abonos del paciente 
	             * 			un saldo disponible de abono mayor igual que el valor del concepto correspondiente al 
	             * 			abono del recibo de caja que se está anulando.
	             * 
	             * 
	             * NOTA: estas validaciones se hacen en el action.
	             */
	        }
        }
		
		return errores;
	}
    
    public void reset()
    {
        this.numeroAnulacionReciboCaja="";
        this.numeroReciboCaja="";
        this.codigoReciboCaja="";
        this.fechaReciboCaja="";
        this.horaReciboCaja="";
        this.fechaAnulacionReciboCaja="";
        this.horaAnulacionReciboCaja="";
        this.consecutivoCaja=ConstantesBD.codigoNuncaValido;
        this.codigoCaja=ConstantesBD.codigoNuncaValido;
        this.descripcionCaja="";
        this.loginUsuarioCaja="";
        this.nombreUsuarioCaja="";
        this.recibidoDe="";
        this.codigoCentroAtencion=ConstantesBD.codigoNuncaValido;
        this.nombreCentroAtencion="";
        this.conceptosRC=new HashMap();
        this.totalRecibidoCaja=ConstantesBD.codigoNuncaValido;
        this.motivo=ConstantesBD.codigoNuncaValido;
        this.institucion=ConstantesBD.codigoNuncaValido;
        this.observaciones="";
        this.conceptosRC.put("numeroregistros","0");//inicializando la numera de registros
        this.informacionCargada=false;
        this.usuarioAnula="";
        this.eliminarRC = ConstantesBD.acronimoNo;
        this.existeReciboCajaXConceptAnticipConvenioOdonto= false;
        this.numContratoOdontologia= ConstantesBD.codigoNuncaValido;
    }
    
    /**
     * @return Returns the codigoCaja.
     */
    public int getCodigoCaja()
    {
        return codigoCaja;
    }
    
    /**
     * @param codigoCaja The codigoCaja to set.
     */
    public void setCodigoCaja(int codigoCaja)
    {
        this.codigoCaja = codigoCaja;
    }
    
    /**
     * @return Returns the conceptosRC.
     */
    public HashMap getConceptosRC()
    {
        return conceptosRC;
    }
    
    /**
     * @param conceptosRC The conceptosRC to set.
     */
    public void setConceptosRC(HashMap conceptosRC)
    {
        this.conceptosRC = conceptosRC;
    }
    
    /**
     * @return Returns the conceptosRC.
     */
    public Object getConceptosRC(String key)
    {
        return conceptosRC.get(key);
    }
    
    /**
     * @param conceptosRC The conceptosRC to set.
     */
    public void setConceptosRC(String key, Object value)
    {
        this.conceptosRC.put(key,value);
    }
    
    /**
     * @return Returns the consecutivoCaja.
     */
    public int getConsecutivoCaja()
    {
        return consecutivoCaja;
    }
    
    /**
     * @param consecutivoCaja The consecutivoCaja to set.
     */
    public void setConsecutivoCaja(int consecutivoCaja)
    {
        this.consecutivoCaja = consecutivoCaja;
    }
    
    /**
     * @return Returns the descripcionCaja.
     */
    public String getDescripcionCaja()
    {
        return descripcionCaja;
    }
    
    /**
     * @param descripcionCaja The descripcionCaja to set.
     */
    public void setDescripcionCaja(String descripcionCaja)
    {
        this.descripcionCaja = descripcionCaja;
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
	    this.estado = estado;
	}
    
	/**
     * @return Returns the fechaReciboCaja.
     */
    public String getFechaReciboCaja()
    {
        return fechaReciboCaja;
    }
   
    /**
     * @param fechaReciboCaja The fechaReciboCaja to set.
     */
    public void setFechaReciboCaja(String fechaReciboCaja)
    {
        this.fechaReciboCaja = fechaReciboCaja;
    }
    
    /**
     * @return Returns the horaReciboCaja.
     */
    public String getHoraReciboCaja()
    {
        return horaReciboCaja;
    }
    
    /**
     * @param horaReciboCaja The horaReciboCaja to set.
     */
    public void setHoraReciboCaja(String horaReciboCaja)
    {
        this.horaReciboCaja = horaReciboCaja;
    }
   
    /**
     * @return Returns the loginUsuarioCaja.
     */
    public String getLoginUsuarioCaja()
    {
        return loginUsuarioCaja;
    }
    
    /**
     * @param loginUsuarioCaja The loginUsuarioCaja to set.
     */
    public void setLoginUsuarioCaja(String loginUsuarioCaja)
    {
        this.loginUsuarioCaja = loginUsuarioCaja;
    }
    
    /**
     * @return Returns the motivo.
     */
    public int getMotivo()
    {
        return motivo;
    }
    
    /**
     * @param motivo The motivo to set.
     */
    public void setMotivo(int motivo)
    {
        this.motivo = motivo;
    }
    
    /**
     * @return Returns the nombreUsuarioCaja.
     */
    public String getNombreUsuarioCaja()
    {
        return nombreUsuarioCaja;
    }
    
    /**
     * @param nombreUsuarioCaja The nombreUsuarioCaja to set.
     */
    public void setNombreUsuarioCaja(String nombreUsuarioCaja)
    {
        this.nombreUsuarioCaja = nombreUsuarioCaja;
    }
    
    /**
     * @return Returns the numeroAnulacionReciboCaja.
     */
    public String getNumeroAnulacionReciboCaja()
    {
        return numeroAnulacionReciboCaja;
    }
    
    /**
     * @param numeroAnulacionReciboCaja The numeroAnulacionReciboCaja to set.
     */
    public void setNumeroAnulacionReciboCaja(String numeroAnulacionReciboCaja)
    {
        this.numeroAnulacionReciboCaja = numeroAnulacionReciboCaja;
    }
    
    /**
     * @return Returns the numeroReciboCaja.
     */
    public String getNumeroReciboCaja()
    {
        return numeroReciboCaja;
    }
    
    /**
     * @param numeroReciboCaja The numeroReciboCaja to set.
     */
    public void setNumeroReciboCaja(String numeroReciboCaja)
    {
        this.numeroReciboCaja = numeroReciboCaja;
    }
    
    /**
     * @return Returns the observaciones.
     */
    public String getObservaciones()
    {
        return this.observaciones;
    }
    
    /**
     * @param observaciones The observaciones to set.
     */
    public void setObservaciones(String observaciones)
    {
        this.observaciones = observaciones;
    }
    
    /**
     * @return Returns the recibidoDe.
     */
    public String getRecibidoDe()
    {
        return recibidoDe;
    }
    
    /**
     * @param recibidoDe The recibidoDe to set.
     */
    public void setRecibidoDe(String recibidoDe)
    {
        this.recibidoDe = recibidoDe;
    }
    
    /**
     * @return Returns the totalRecibidoCaja.
     */
    public double getTotalRecibidoCaja()
    {
        return totalRecibidoCaja;
    }
   
    /**
     * @param totalRecibidoCaja The totalRecibidoCaja to set.
     */
    public void setTotalRecibidoCaja(double totalRecibidoCaja)
    {
        this.totalRecibidoCaja = totalRecibidoCaja;
    }
    /**
     * @return Returns the institucion.
     */
    public int getInstitucion()
    {
        return institucion;
    }
    /**
     * @param institucion The institucion to set.
     */
    public void setInstitucion(int institucion)
    {
        this.institucion = institucion;
    }
    /**
     * @return Returns the informacionCargada.
     */
    public boolean isInformacionCargada()
    {
        return informacionCargada;
    }
    /**
     * @param informacionCargada The informacionCargada to set.
     */
    public void setInformacionCargada(boolean informacionCargada)
    {
        this.informacionCargada = informacionCargada;
    }
    /**
     * @return Returns the fechaAnulacionReciboCaja.
     */
    public String getFechaAnulacionReciboCaja()
    {
        return fechaAnulacionReciboCaja;
    }
    /**
     * @param fechaAnulacionReciboCaja The fechaAnulacionReciboCaja to set.
     */
    public void setFechaAnulacionReciboCaja(String fechaAnulacionReciboCaja)
    {
        this.fechaAnulacionReciboCaja = fechaAnulacionReciboCaja;
    }
    /**
     * @return Returns the horaAnulacionReciboCaja.
     */
    public String getHoraAnulacionReciboCaja()
    {
        return horaAnulacionReciboCaja;
    }
    /**
     * @param horaAnulacionReciboCaja The horaAnulacionReciboCaja to set.
     */
    public void setHoraAnulacionReciboCaja(String horaAnulacionReciboCaja)
    {
        this.horaAnulacionReciboCaja = horaAnulacionReciboCaja;
    }
    /**
     * @return Returns the usuarioAnula.
     */
    public String getUsuarioAnula()
    {
        return usuarioAnula;
    }
    /**
     * @param usuarioAnula The usuarioAnula to set.
     */
    public void setUsuarioAnula(String usuarioAnula)
    {
        this.usuarioAnula = usuarioAnula;
    }

	/**
	 * @return Returns the codigoCentroAtencion.
	 */
	public int getCodigoCentroAtencion() {
		return codigoCentroAtencion;
	}

	/**
	 * @param codigoCentroAtencion The codigoCentroAtencion to set.
	 */
	public void setCodigoCentroAtencion(int codigoCentroAtencion) {
		this.codigoCentroAtencion = codigoCentroAtencion;
	}

	/**
	 * @return Returns the nombreCentroAtencion.
	 */
	public String getNombreCentroAtencion() {
		return nombreCentroAtencion;
	}

	/**
	 * @param nombreCentroAtencion The nombreCentroAtencion to set.
	 */
	public void setNombreCentroAtencion(String nombreCentroAtencion) {
		this.nombreCentroAtencion = nombreCentroAtencion;
	}

	/**
	 * @return the eliminarRC
	 */
	public String getEliminarRC() {
		return eliminarRC;
	}

	/**
	 * @param eliminarRC the eliminarRC to set
	 */
	public void setEliminarRC(String eliminarRC) {
		this.eliminarRC = eliminarRC;
	}

	/**
	 * @return the existeReciboCajaXConceptAnticipConvenioOdonto
	 */
	public boolean isExisteReciboCajaXConceptAnticipConvenioOdonto() {
		return existeReciboCajaXConceptAnticipConvenioOdonto;
	}

	/**
	 * @param existeReciboCajaXConceptAnticipConvenioOdonto the existeReciboCajaXConceptAnticipConvenioOdonto to set
	 */
	public void setExisteReciboCajaXConceptAnticipConvenioOdonto(
			boolean existeReciboCajaXConceptAnticipConvenioOdonto) {
		this.existeReciboCajaXConceptAnticipConvenioOdonto = existeReciboCajaXConceptAnticipConvenioOdonto;
		
	}

	/**
	 * @return the numContratoOdontologia
	 */
	public int getNumContratoOdontologia() {
		return numContratoOdontologia;
	}

	/**
	 * @param numContratoOdontologia the numContratoOdontologia to set
	 */
	public void setNumContratoOdontologia(int numContratoOdontologia) {
		this.numContratoOdontologia = numContratoOdontologia;
	}



	public void setCodigoReciboCaja(String codigoReciboCaja) {
		this.codigoReciboCaja = codigoReciboCaja;
	}

	public String getCodigoReciboCaja() {
		return codigoReciboCaja;
	}
	
}
