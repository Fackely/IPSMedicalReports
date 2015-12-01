/**
 * Juan David Ramírez 30/06/2006
 * Princeton S.A.
 */
package com.princetonsa.actionform.capitacion;

import java.util.Collection;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.UtilidadCadena;
import util.UtilidadFecha;

/**
 * @author Juan David Ramírez
 *
 */
public class AjustesCxCForm extends ValidatorForm
{
	/**
	 * Serial version UID
	 */
	private static final long serialVersionUID = 1L;

	/*--------------------------- ATRIBUTOS ------------------------------*/
	
	/**
	 * Estado del flujo
	 */
	private String estado;

	/**
	 * codigo del registro
	 */
	private int codigo;
	
	/**
	 * Tipo de ajuste
	 */
	private int tipoAjuste;
	
	/**
	 * Número del ajuste 
	 */
	private String numero;
	
	/**
	 * Cuenta de cobro
	 */
	private String cuentaCobro;
	
	/**
	 * Convenio
	 */
	private int convenio;
	
	/**
	 * Fecha del ajuste
	 */
	private String fecha;
	
	/**
	 * Comnepto del ajuste
	 */
	private String concepto;
	
	/**
	 * Observaciones del ajuste
	 */
	private String observaciones;

	/**
	 * Listado de los tipos de ajuste 
	 */
	private Collection tiposAjuste;
	
	/**
	 * Listado de los Convenios
	 */
	private Collection listadoConvenios;
	
	/**
	 * Listado de los conceptos de ajuste
	 */
	private Collection listadoConceptos;
	
	/**
	 * Mensaje de error en caso de no existir ajustes o cuentas cobro en BD
	 */
	private String mensaje;
	
	/**
	 * Estado del ajuste
	 */
	private String estadoAjuste;
	
	/**
	 * Saldo de la cuenta de cobro
	 */
	private double saldo;

	/**
	 * Valor del ajuste
	 */
	private double valorAjuste;
	
	/**
	 * Indica si estoy ingresando o modificando
	 */
	private boolean modificando;
	
	/**
	 * Motivo de anulación del ajuste
	 */
	private String motivoAnulacion;
	
	/**
	 * Mapa para manejar el detalee de los cargues
	 */
	private HashMap detalleCargues;
	
	/**
	 * Cantidad de ajustes para decidir mostrar mensaje
	 */
	private int cantidadAjustes;

	/**
	 * Código del estado del ajuste
	 */
	private int codigoEstado;
	
    /**
     * Modifico el Valor del Ajuste.
     */
    private boolean modificoValorAjuste;

    /**
     * Fecha de la Radicacion Cuenta de Cobro si esta en estado radicada. 
     */
    private String fechaRadicacionCc; 
	
	/*--------------------------- RESET ------------------------*/

	/**
	 * Método para resetear la clase
	 */
	public void reset()
	{
		this.tipoAjuste=0;
		this.numero="";
		this.cuentaCobro="";
		this.convenio=0;
		this.tiposAjuste=null;
		this.listadoConvenios=null;
		this.mensaje="";
		this.estadoAjuste="";
		this.saldo=0;
		this.valorAjuste=0;
		this.fecha="";
		this.modificando=false;
		this.motivoAnulacion="";
		this.detalleCargues=new HashMap();
		this.cantidadAjustes=0;
		this.codigoEstado=0;

		//-- Validar que se cambio el valor del Ajuste.
		this.modificoValorAjuste = false;
	}

	/*---------------------------------- MÉTODOS ------------------------------------------*/

	/**
	 * Método para validar la clase
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
	{
		this.mensaje="";
		ActionErrors errores=new ActionErrors();
		if(estado.equals("buscarAjuste"))
		{
			try
			{
				Integer.parseInt(getNumero());
			}
			catch(Exception e)
			{
				errores.add("errors.integer", new ActionMessage("errors.integer", "Numero"));
			}
		}
		if(estado.equals("buscarCuentaCobro"))
		{
			try
			{
				Integer.parseInt(getCuentaCobro());
			}
			catch(Exception e)
			{
				errores.add("errors.integer", new ActionMessage("errors.integer", "Numero Cuenta Cobro"));
			}
		}
		
		if(estado.equalsIgnoreCase("guardar"))
		{
		
			if(tipoAjuste==0)
			{
				errores.add("tipo ajuste", new ActionMessage("errors.required", "Tipo de Ajuste"));
			}
			if(UtilidadCadena.noEsVacio(fecha))
			{
				if(!UtilidadFecha.validarFecha(fecha))
				{
					errores.add("fecha", new ActionMessage("errors.formatoFechaInvalido", "de Ajuste ("+fecha+")"));
				}
				else
				{
					//-- Validar que sea menor o igual que la del sistema.
					if((UtilidadFecha.conversionFormatoFechaABD(this.fecha)).compareTo(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()))>0)
					{
						errores.add("fecha", new ActionMessage("errors.fechaPosteriorAOtraDeReferencia", "Fecha Generación Ajuste", "Actual"));
					}

				}
			}
			else
			{
				errores.add("fecha", new ActionMessage("errors.required", "Fecha de Ajuste"));
			}
			if(concepto.equals(""))
			{
				errores.add("concepto ajuste", new ActionMessage("errors.required", "Concepto Ajuste"));
			}
			if(valorAjuste==0)
			{
				errores.add("saldo", new ActionMessage("errors.required", "Valor del Ajuste"));
			}
			else
			{
				if(valorAjuste<0)
				{
					errores.add("saldo mayor que 0", new ActionMessage("error.ajustesCxC.valorAjuste", "mayor", "que 0"));
				}
				else if(tipoAjuste==ConstantesBD.codigoConceptosCarteraCredito)
				{
					if(valorAjuste>saldo)
					{
						errores.add("saldo mayor que ajuste", new ActionMessage("error.ajustesCxC.valorAjuste", "menor o igual", "al campo 'Saldo'"));
					}
				}
			}
			
			if ( UtilidadCadena.noEsVacio(this.fechaRadicacionCc))
			{	
				if((UtilidadFecha.conversionFormatoFechaABD(this.fechaRadicacionCc)).compareTo(UtilidadFecha.conversionFormatoFechaABD(this.fecha))>0)
				{
					errores.add("fecha", new ActionMessage("errors.fechaPosteriorAOtraDeReferencia", " Radicación Cuenta Cobro", " Aprobación"));
				}
			}	

		}
		return errores;
	}

	/*------------------------- GETTERS y SETTERS -------------------------------------------*/
	
	/**
	 * @return Retorna estado.
	 */
	public String getEstado()
	{
		return estado;
	}

	/**
	 * @param estado Asigna estado.
	 */
	public void setEstado(String estado)
	{
		this.estado = estado;
	}

	/**
	 * @return Retorna tiposAjuste.
	 */
	public Collection getTiposAjuste()
	{
		return tiposAjuste;
	}

	/**
	 * @param tiposAjuste Asigna tiposAjuste.
	 */
	public void setTiposAjuste(Collection tiposAjuste)
	{
		this.tiposAjuste = tiposAjuste;
	}

	/**
	 * @return Retorna tipoAjuste.
	 */
	public int getTipoAjuste()
	{
		return tipoAjuste;
	}

	/**
	 * @param tipoAjuste Asigna tipoAjuste.
	 */
	public void setTipoAjuste(int tipoAjuste)
	{
		this.tipoAjuste = tipoAjuste;
	}

	/**
	 * @return Retorna cuentaCobro.
	 */
	public String getCuentaCobro()
	{
		return cuentaCobro;
	}

	/**
	 * @param cuentaCobro Asigna cuentaCobro.
	 */
	public void setCuentaCobro(String cuentaCobro)
	{
		this.cuentaCobro = cuentaCobro;
	}

	/**
	 * @return Retorna numero.
	 */
	public String getNumero()
	{
		return numero;
	}

	/**
	 * @param numero Asigna numero.
	 */
	public void setNumero(String numero)
	{
		this.numero = numero;
	}

	/**
	 * @return Retorna listadoConvenios.
	 */
	public Collection getListadoConvenios()
	{
		return listadoConvenios;
	}

	/**
	 * @param listadoConvenios Asigna listadoConvenios.
	 */
	public void setListadoConvenios(Collection convenios)
	{
		this.listadoConvenios = convenios;
	}

	/**
	 * @return Retorna convenio.
	 */
	public int getConvenio()
	{
		return convenio;
	}

	/**
	 * @param convenio Asigna convenio.
	 */
	public void setConvenio(int convenio)
	{
		this.convenio = convenio;
	}

	/**
	 * @return Retorna concepto.
	 */
	public String getConcepto()
	{
		return concepto;
	}

	/**
	 * @param concepto Asigna concepto.
	 */
	public void setConcepto(String concepto)
	{
		this.concepto = concepto;
	}

	/**
	 * @return Retorna fecha.
	 */
	public String getFecha()
	{
		return fecha;
	}

	/**
	 * @param fecha Asigna fecha.
	 */
	public void setFecha(String fecha)
	{
		this.fecha = fecha;
	}

	/**
	 * @return Retorna observaciones.
	 */
	public String getObservaciones()
	{
		return observaciones;
	}

	/**
	 * @param observaciones Asigna observaciones.
	 */
	public void setObservaciones(String observaciones)
	{
		this.observaciones = observaciones;
	}

	/**
	 * @return Retorna mensaje.
	 */
	public String getMensaje()
	{
		return mensaje;
	}

	/**
	 * @param mensaje Asigna mensaje.
	 */
	public void setMensaje(String mensaje)
	{
		this.mensaje = mensaje;
	}

	/**
	 * @return Retorna estadoAjuste.
	 */
	public String getEstadoAjuste()
	{
		return estadoAjuste;
	}

	/**
	 * @param estadoAjuste Asigna estadoAjuste.
	 */
	public void setEstadoAjuste(String estadoAjuste)
	{
		this.estadoAjuste = estadoAjuste;
	}

	/**
	 * @return Retorna listadoConceptos.
	 */
	public Collection getListadoConceptos()
	{
		return listadoConceptos;
	}

	/**
	 * @param listadoConceptos Asigna listadoConceptos.
	 */
	public void setListadoConceptos(Collection listadoConceptos)
	{
		this.listadoConceptos = listadoConceptos;
	}

	/**
	 * @return Retorna saldo.
	 */
	public double getSaldo()
	{
		return saldo;
	}

	/**
	 * @param saldo Asigna saldo.
	 */
	public void setSaldo(double saldo)
	{
		this.saldo = saldo;
	}

	/**
	 * @return Retorna valorAjuste.
	 */
	public double getValorAjuste()
	{
		return valorAjuste;
	}

	/**
	 * @param valorAjuste Asigna valorAjuste.
	 */
	public void setValorAjuste(double valorAjuste)
	{
		this.valorAjuste = valorAjuste;
	}

	/**
	 * @return Retorna modificando.
	 */
	public boolean getModificando()
	{
		return modificando;
	}

	/**
	 * @param modificando Asigna modificando.
	 */
	public void setModificando(boolean modificando)
	{
		this.modificando = modificando;
	}

	/**
	 * @return Retorna codigo.
	 */
	public int getCodigo()
	{
		return codigo;
	}

	/**
	 * @param codigo Asigna codigo.
	 */
	public void setCodigo(int codigo)
	{
		this.codigo = codigo;
	}

	/**
	 * @return Retorna motivoAnulacion.
	 */
	public String getMotivoAnulacion()
	{
		return motivoAnulacion;
	}

	/**
	 * @param motivoAnulacion Asigna motivoAnulacion.
	 */
	public void setMotivoAnulacion(String motivoAnulacion)
	{
		this.motivoAnulacion = motivoAnulacion;
	}

	/**
	 * @return Retorna detalleCargues.
	 */
	public HashMap getDetalleCargues()
	{
		return detalleCargues;
	}

	/**
	 * @param detalleCargues Asigna detalleCargues.
	 */
	public void setDetalleCargues(HashMap detalleCargues)
	{
		this.detalleCargues = detalleCargues;
	}

	/**
	 * @return Retorna cantidadAjustes.
	 */
	public int getCantidadAjustes()
	{
		return cantidadAjustes;
	}

	/**
	 * @param cantidadAjustes Asigna cantidadAjustes.
	 */
	public void setCantidadAjustes(int cantidadAjustes)
	{
		this.cantidadAjustes = cantidadAjustes;
	}

	/**
	 * @return Retorna codigoEstado.
	 */
	public int getCodigoEstado()
	{
		return codigoEstado;
	}

	/**
	 * @param codigoEstado Asigna codigoEstado.
	 */
	public void setCodigoEstado(int codigoEstado)
	{
		this.codigoEstado = codigoEstado;
	}
	/**
	 * @return Retorna modificoValorAjuste.
	 */
	public boolean getModificoValorAjuste() {
		return modificoValorAjuste;
	}


	/**
	 * @param Asigna modificoValorAjuste.
	 */
	public void setModificoValorAjuste(boolean modificoValorAjuste) {
		this.modificoValorAjuste = modificoValorAjuste;
	}

	/**
	 * @return Retorna fechaRadicacionCc.
	 */
	public String getFechaRadicacionCc() {
		return fechaRadicacionCc;
	}

	/**
	 * @param Asigna fechaRadicacionCc.
	 */
	public void setFechaRadicacionCc(String fechaRadicacionCc) {
		this.fechaRadicacionCc = fechaRadicacionCc;
	}
}
