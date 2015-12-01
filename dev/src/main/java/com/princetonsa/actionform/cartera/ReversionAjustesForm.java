/*
 * Created on 2/09/2005
 *
 * Bean de reversionAjustesForm
 */
package com.princetonsa.actionform.cartera;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import com.princetonsa.mundo.facturacion.ValidacionesFactura;

import util.ConstantesBD;
import util.InfoDatosInt;
import util.UtilidadFecha;
import util.UtilidadValidacion;
import util.Utilidades;


/**
 * @author artotor
 *
 * JavaBean para el manejo de reversion ajustes
 */
public class ReversionAjustesForm extends ValidatorForm 
{
	/**
	 * Estado de la funcionalidad
	 */
	private String estado;
	
	
	/*-------------------------------------------------DECLARACION DE LAS VARIABLES ESPEC&Iacute;FICA PARA LA BUSQUEDA---------------------------------------------*/
	/**
	 * variable que maneja el tipo de ajuste
	 */
	private String tipoAjusteBusqueda;
	
    /**
     * Variables para manejar el numero del ajuste.
     */
	private double numeroAjusteBusqueda;
	private String numeroAjusteBusquedaStr;
	
	/**
	 * Variable para manejar la fecha del ajustes
	 */
	private String fechaAjusteBusqueda;
	
	/**
	 * Mapa ajustes
	 */
	private HashMap ajustes;
	
	/**
	 * Ajuste seleccionado del listado
	 */
	private int regSeleccionado;
	/*---------------------------------------------FIN DECLARACION DE LAS VARIABLES ESPEC&Iacute;FICA PARA LA BUSQUEDA---------------------------------------------*/
	
	
	
	/*---------------------------------------------DECLARACION DE LAS VARIABLES PARA LA SECCION INFORMACION DEL AJUSTE---------------------------------------------*/
	
	/**
	 * Variable para manejar si una ajuste es de una cuenta de cobro(true) o una factura(false)
	 */
	private boolean informacionCargada;
	
	/**
	 * Variable para manejar si una ajuste es de una cuenta de cobro(true) o una factura(false)
	 */
	private boolean ajusteCuentaCobro;
	
	/**
	 * Variable para manejar el codigo del ajuste
	 */
	private double codigoAjuste;
	/**
	 * Variable que contiene el consecutivo del ajuste tomado de la tabla consecutivos.
	 */
	private String consecutivoAjuste;
	
	/**
	 * variable que maneja el tipo de ajuste
	 */
	private String tipoAjuste;
	
	/**
	 * Nombre del Tipo de Ajuste, Debito o Credito
	 */
	private String nomTipoAjuste;
	
	/**
	 * variable que maneja la institucion del ajuste
	 */
	private int institucion;
	
	/**
	 * variable que indica si el ajuste es de castigoCartera
	 */
	private boolean castigoCartera;
	
	/**
	 * Encaso de ser un castigo esta variable maneja el concepto del castigo.
	 */
	private String conceptoCastigoCartera;
	
	/**
	 * Para manejar la fecha del ajuste.
	 */
	private String fechaAjuste;
	
	/**
	 * Variable para manejar la cuenta de cobro a la que se le aplica el ajuste.
	 * pude ser null cuando el ajuste se hace directamente a una factura.
	 */
	private double cuentaCobro;
	private String cuentaCobroStr;
	
	/**
	 * Codigo de la factura, en caso de que el ajustes sea por factura.
	 */
	private int codigoFactura;
	
	/**
	 * Consecutivo de la factura
	 */
	private String consecutivoFactura;
	
	/**
	 * Infodatos para almacenar el convenio, ya sea para la factura o cuenta de cobro.
	 */
	private InfoDatosInt convenio;
	
	/**
	 * El concepto que se maneja del ajuste.
	 */
	private String conceptoAjuste;
	
	/**
	 * Metodo que se utiliza para la generacion de ajuste.
	 */
	private String metodoAjuste;
	
	/**
	 * valor total del ajuste.
	 */
	private double valorAjuste;

	/**
	 * valor total del ajuste String.
	 */
	private String valorAjusteStr;
	
	/**
	 * Observaciones creadas en la generacion de ajuste.
	 */
	private String observaciones;
	
	/**
	 * Fecha en que se hace la reversion
	 */
	private String fechaReversion;
	
	/**
	 * Observaciones que se dan de la reversion
	 */
	private String observacionesReversion;
	
	/**
	 * Para manejar ya sea el saldo de la factura o el saldo de la cuenta de cobro;
	 */
	private double saldo;
	
	/**
	 * Variable para manejar la fecha de aprobacion del ajuste.
	 */
	private String fechaAprobacionAjuste;
	
	/**
	 * Variable que me indica si una facura es del sistema o es externa
	 * si es false es del sistema
	 */
	private boolean facturaExterna;
	
	/**
	 * Mapa para almacenar las facturas relacionada a un ajuste.
	 */
	private HashMap facturas;
	/**
	 * Variable que indica si debo solicitar la confirmacion de la reversion del ajuste.
	 */
	private boolean confimacionReversion;
	
	/**
	 * 
	 */
	private int codigoCentroAtencion;
	
	/**
	 * 
	 */
	private String nombreCentroAtencion;
	/*-------------------------------------------FIN DECLARACION DE LAS VARIABLES PARA LA SECCION INFORMACION DEL AJUSTE---------------------------------------------*/
	/*-------------------------------------------   DECLARACION DE LAS VARIABLES PARA EL NUEVO AJUSTE---------------------------------------------*/
	
	/**
	 * Variable para manejar el valor del consecutivo asignado al nuevo ajuste
	 */
	private String consecutivoAjusteNuevo;
	
	/**
	 * Variable para manejar el tipo del nuevo ajuste
	 */
	private String tipoAjusteNuevo;
	
	/**
	 * Variable para manejar el nombre del consecutivo a manejar
	 */
	private String nombreConsecutivo;
	
	/**
	 * Variable para manejar la fecha de elaboracion del ajuste.
	 */
	private String fechaElaboracion;
	
	/**
	 * Variable para manejar la hora de elaboracion del ajuste.
	 */
	private String horaElaboracion;
	
	/**
	 * Estado del ajuste que se va a genenrerar
	 */
	private int estadoAjuste;
	
	
	/**
	 * Variable para manejar el codigo del ajuste Nuevo
	 */
	private String nomTipoAjusteNuevo;
	
	/*-------------------------------------------FIN DECLARACION DE LAS VARIABLES PARA EL NUEVO AJUSTE---------------------------------------------*/
	
	
	/*--------------------------------------------------------------------VALIDATE-----------------------------------------------------------------------------------*/
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		this.confimacionReversion=false;
		ActionErrors errores= new ActionErrors();
		if(this.estado.equals("buscarPorAjuste"))
		{
			this.setInformacionCargada(false);
			this.ajustes=new HashMap();
			if(this.numeroAjusteBusquedaStr.equals("")||tipoAjusteBusqueda.equals(ConstantesBD.codigoNuncaValido+""))
			{
				errores.add("Cargar Ajuste",new ActionMessage("error.cartera.ajustes.tipoAjusteYNumeroRequerido"));
			}
			else
			{
				this.codigoAjuste=Utilidades.obtenercodigoAjusteEmpresa(this.numeroAjusteBusquedaStr,this.tipoAjusteBusqueda,this.institucion);
				if(tipoAjusteBusqueda.equals(ConstantesBD.ajustesDebitoFuncionalidadAjustes.getAcronimo()))
				{
					this.nomTipoAjuste=ConstantesBD.ajustesDebitoFuncionalidadAjustes.getNombre();
				}
				else if(tipoAjusteBusqueda.equals(ConstantesBD.ajustesCreditoFuncionalidadAjustes.getAcronimo()))
				{
					this.nomTipoAjuste=ConstantesBD.ajustesCreditoFuncionalidadAjustes.getNombre();
				}
				if(this.codigoAjuste==ConstantesBD.codigoNuncaValido)
				{
					errores.add("No existe Ajuste", new ActionMessage("error.ajusteInexistente",this.nomTipoAjuste,numeroAjusteBusquedaStr));
				}
				else
				{
					if(Integer.parseInt(((Utilidades.obtenerestadoAjuste(this.codigoAjuste)).split(ConstantesBD.separadorSplit))[0])!=ConstantesBD.codigoEstadoCarteraAprobado)
					{
						errores.add("Ajuste estado diferente", new ActionMessage("error.ajustesEmpresa.ajusteEstadoDiferente",this.nomTipoAjuste,numeroAjusteBusquedaStr,((Utilidades.obtenerestadoAjuste(this.codigoAjuste)).split(ConstantesBD.separadorSplit))[1]));
					}
				}
				
			}
		}
		else if(this.estado.equals("buscarPorFecha"))
		{
			this.setInformacionCargada(false);
			if((this.fechaAjusteBusqueda.trim()).equals(""))
			{
				errores.add("Fecha Requerido",new ActionMessage("errors.required","La fecha del ajuste"));
			}
			else
			{
				if(!UtilidadFecha.validarFecha(this.fechaAjusteBusqueda))
				{
					errores.add("fechaAjusteBusqueda", new ActionMessage("errors.formatoFechaInvalido",this.fechaAjusteBusqueda));
				}
				else
				{
					if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.fechaAjusteBusqueda,UtilidadFecha.getFechaActual()))
					{
						errores.add("Fecha Mayor del Sistema",new ActionMessage("errors.fechaPosteriorAOtraDeReferencia","del ajuste ("+this.fechaAjusteBusqueda+")","Actual ("+UtilidadFecha.getFechaActual()+")"));
					}
				}
			}
		}
		else if(this.estado.equals("confimacionReversar"))
		{
			if((this.fechaReversion.trim()).equals(""))
			{
				errores.add("Fecha Requerido",new ActionMessage("errors.required","La fecha de Reversion"));
			}
			else
			{
				if(!UtilidadFecha.validarFecha(this.fechaReversion))
				{
					errores.add("fechaAprobacionAjuste", new ActionMessage("errors.formatoFechaInvalido",this.fechaReversion));
				}
				else
				{
					//@todo falta validar el estado del ajuste.
					if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.fechaReversion,UtilidadFecha.getFechaActual()))
					{
						errores.add("Fecha Mayor del Sistema",new ActionMessage("errors.fechaPosteriorAOtraDeReferencia","de Reversion ("+this.fechaReversion+")","Actual ("+UtilidadFecha.getFechaActual()+")"));
					}
					if(UtilidadFecha.esFechaMenorQueOtraReferencia(this.fechaReversion,this.fechaAprobacionAjuste))
					{
						errores.add("Fecha Mayor del Sistema",new ActionMessage("errors.fechaAnteriorAOtraDeReferencia","de Reversion ("+this.fechaReversion+")","de Aprobación ("+this.fechaAprobacionAjuste+")"));
					}
					String[] fechaVector = this.fechaReversion.split("/", 3);
					if(UtilidadValidacion.existeCierreMensual(Integer.parseInt(fechaVector[1]),Integer.parseInt(fechaVector[2]),this.institucion))
					{
						errores.add("Existe cierre para esa fecha",new ActionMessage("error.cierre.yaTieneCierreMensual"));
					}
					if(UtilidadValidacion.existeCierreSaldosIniciales(this.institucion))
					{
						if(UtilidadValidacion.fechaMenorIgualAFechaCierreSalodsIniciales(this.fechaReversion,this.institucion))
							{
							errores.add("Fecha menor A Cierre Saldo Inicial",new ActionMessage("errors.fechaAnteriorAOtraDeReferencia","de Reversion ","del Cierre de Saldo Inicial"));
							}
					}
				}
			}
			this.codigoAjuste=Utilidades.obtenercodigoAjusteEmpresa(this.numeroAjusteBusquedaStr,this.tipoAjusteBusqueda,this.institucion);
			if(tipoAjusteBusqueda.equals(ConstantesBD.ajustesDebitoFuncionalidadAjustes.getAcronimo()))
			{
				this.nomTipoAjuste=ConstantesBD.ajustesDebitoFuncionalidadAjustes.getNombre();
			}
			else if(tipoAjusteBusqueda.equals(ConstantesBD.ajustesCreditoFuncionalidadAjustes.getAcronimo()))
			{
				this.nomTipoAjuste=ConstantesBD.ajustesCreditoFuncionalidadAjustes.getNombre();
			}
			if(Utilidades.esAjusteDeReversion(this.codigoAjuste))
			{
				errores.add("Es Ajuste Tipo Reversion",new ActionMessage("error.ajustesEmpresa.ajusteTipoReversion",this.nomTipoAjuste,this.consecutivoAjuste));
			}
			else if(UtilidadValidacion.esAjusteReversado(this.codigoAjuste))
			{
				errores.add("Ajuste ya reversado",new ActionMessage("error.ajustesEmpresa.ajusteYaReversado",this.nomTipoAjuste,this.consecutivoAjuste));
			}
			else
			{
				if(!UtilidadValidacion.existeDistribucionAjusteNivelFacturas(this.codigoAjuste))
				{
					errores.add("Ajuste Sin Distribucion Nivel Facturas",new ActionMessage("error.ajustesEmpresa.ajusteSinDistribucionNivelFacturas",this.nomTipoAjuste,this.consecutivoAjuste));
				}
				if(!isAjusteCuentaCobro())
				{
					String[] estadoVector=Utilidades.obtenerEstadoFactura(this.codigoFactura).split(ConstantesBD.separadorSplit);
					if(Integer.parseInt(estadoVector[0])!=ConstantesBD.codigoEstadoFacturacionFacturada)
					{
						errores.add("estado diferente", new ActionMessage("error.ajustesEmpresa.facturaEstadoDiferenteFacturada",this.consecutivoFactura,estadoVector[1]));
					}
					this.facturaExterna=ValidacionesFactura.esFacturaExterna(this.codigoFactura);
					if(ValidacionesFactura.facturaTieneAjustesPendientes(this.codigoFactura))
					{
						errores.add("Ajustes Pendientes", new ActionMessage("error.ajustesEmpresa.facturaConAjustesPendientes",this.consecutivoFactura+""));
					}
					if(ValidacionesFactura.facturaTienePagosPendientes(this.codigoFactura))
					{
						errores.add("Pagos Pendientes", new ActionMessage("error.ajustesEmpresa.facturaConPagosPendientes",this.consecutivoFactura+""));
					}
					if(ValidacionesFactura.esFacturaCerrada(this.codigoFactura))
					{
						errores.add("Factura Cerrada", new ActionMessage("error.ajustesEmpresa.facturaCerrada",this.consecutivoFactura+""));
					}
					if(!facturaExterna)
					{
						if(!UtilidadValidacion.existeDistribucionAjusteNivelServicios(this.codigoAjuste,this.codigoFactura))
						{
							errores.add("Ajuste Sin Distribucion Nivel Servicios-Factura", new ActionMessage("error.ajustesEmpresa.ajusteSinDistribucionNivelServiciosFacturas",this.nomTipoAjuste,this.consecutivoAjuste,this.consecutivoFactura));
						}
					}
					if(tipoAjusteBusqueda.equals(ConstantesBD.ajustesDebitoFuncionalidadAjustes.getAcronimo()))
					{
						if(this.valorAjuste>this.saldo)
						{
							errores.add("El valor del ajuste es mayor al saldo", new ActionMessage("error.ajustesEmpresa.valorAjusteMayoAlSaldo",this.saldo+"",this.consecutivoFactura));
						}
					}
				}
				else
				{
					for(int i=0;i<Integer.parseInt(facturas.get("numeroregistros")+"");i++)
					{
						int codigoFac=Integer.parseInt(facturas.get("codigofactura_"+i)+"");
						String[] estadoVector=Utilidades.obtenerEstadoFactura(codigoFac).split(ConstantesBD.separadorSplit);
						if(Integer.parseInt(estadoVector[0])!=ConstantesBD.codigoEstadoFacturacionFacturada)
						{
							errores.add("estado diferente", new ActionMessage("error.ajustesEmpresa.facturaEstadoDiferenteFacturada",facturas.get("consecutivofactura_"+i)+"",estadoVector[1]));
						}
						this.facturaExterna=ValidacionesFactura.esFacturaExterna(codigoFac);
						if(ValidacionesFactura.facturaTieneAjustesPendientes(codigoFac))
						{
							errores.add("Ajustes Pendientes", new ActionMessage("error.ajustesEmpresa.facturaConAjustesPendientes",facturas.get("consecutivofactura_"+i)+""));
						}
						if(ValidacionesFactura.facturaTienePagosPendientes(codigoFac))
						{
							errores.add("Pagos Pendientes", new ActionMessage("error.ajustesEmpresa.facturaConPagosPendientes",facturas.get("consecutivofactura_"+i)+""));
						}
						if(ValidacionesFactura.esFacturaCerrada(codigoFac))
						{
							errores.add("Factura Cerrada", new ActionMessage("error.ajustesEmpresa.facturaCerrada",facturas.get("consecutivofactura_"+i)+""));
						}
						if(!facturaExterna)
						{
							if(!UtilidadValidacion.existeDistribucionAjusteNivelServicios(this.codigoAjuste,codigoFac))
							{
								errores.add("Ajuste Sin Distribucion Nivel Servicios-Factura", new ActionMessage("error.ajustesEmpresa.ajusteSinDistribucionNivelServiciosFacturas",this.nomTipoAjuste,this.consecutivoAjuste,facturas.get("consecutivofactura_"+i)+""));
							}
						}
						if(tipoAjusteBusqueda.equals(ConstantesBD.ajustesDebitoFuncionalidadAjustes.getAcronimo()))
						{
							if(Double.parseDouble(facturas.get("valorajuste_"+i)+"")>Double.parseDouble(facturas.get("saldofactura_"+i)+""))
							{
								errores.add("El valor del ajuste es mayor al saldo", new ActionMessage("error.ajustesEmpresa.valorAjusteMayoAlSaldo",facturas.get("saldofactura_"+i)+"",facturas.get("consecutivofactura_"+i)+""));
							}
						}
					}
				}
			}
		}
		return errores;
	}
	/*----------------------------------------------------------------FIN VALIDATE-----------------------------------------------------------------------------------*/
	
	/*--------------------------------------------------------------------------RESET`S--------------------------------------------------------------------*/
	/**
	 * Metodo para manejar el reseto de las variables.
	 */
	public void reset()
	{
		this.numeroAjusteBusquedaStr="";
		this.numeroAjusteBusqueda=-1;
		this.tipoAjusteBusqueda="";
		this.fechaAjusteBusqueda="";
		this.resetSeccionInfAjuste();
		this.codigoCentroAtencion=ConstantesBD.codigoNuncaValido;
		this.nombreCentroAtencion="";
	}
	
	public void resetSeccionInfAjuste()
	{
		this.informacionCargada=false;
		this.ajusteCuentaCobro=false;
		this.codigoAjuste=ConstantesBD.codigoNuncaValido;
		this.consecutivoAjuste="";
		this.tipoAjuste="";
		this.institucion=ConstantesBD.codigoNuncaValido;
		this.castigoCartera=false;
		this.conceptoCastigoCartera="";
		this.fechaAjuste="";
		this.cuentaCobro=ConstantesBD.codigoNuncaValido;
		this.cuentaCobroStr="";
		this.codigoFactura=ConstantesBD.codigoNuncaValido;
		this.consecutivoFactura="";
		this.convenio= new InfoDatosInt();
		this.conceptoAjuste="";
		this.metodoAjuste="";
		this.valorAjuste=ConstantesBD.codigoNuncaValidoDouble;
		this.valorAjusteStr="";
		this.observaciones="";
		this.fechaReversion="";
		this.observacionesReversion="";
		this.saldo=ConstantesBD.codigoNuncaValido;
		this.nomTipoAjuste="";
		this.fechaReversion=UtilidadFecha.getFechaActual();
		this.fechaAprobacionAjuste="";
		this.facturaExterna=false;
		this.facturas=new HashMap();
		this.confimacionReversion=false;
		this.consecutivoAjusteNuevo="";
		this.tipoAjusteNuevo="";
		this.nombreConsecutivo="";
		this.fechaElaboracion="";
		this.horaElaboracion="";
		this.estadoAjuste=ConstantesBD.codigoNuncaValido;
		this.nomTipoAjusteNuevo="";
		this.ajustes=new HashMap();
		this.regSeleccionado=ConstantesBD.codigoNuncaValido;
	}
	/*--------------------------------------------------------------------------FIN RESET`S--------------------------------------------------------------------*/
	
	/**
	 * @return Returns the estado.
	 */
	public String getEstado() {
		return estado;
	}
	/**
	 * @param estado The estado to set.
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}
	
	/**
	 * @return Returns the numeroAjuste.
	 */
	public double getNumeroAjusteBusqueda() {
		return numeroAjusteBusqueda;
	}
	/**
	 * @param numeroAjuste The numeroAjuste to set.
	 */
	public void setNumeroAjusteBusqueda(double numeroAjuste) 
	{
		this.numeroAjusteBusqueda = numeroAjuste;
		String temp=this.numeroAjusteBusqueda+"";
		if(temp.indexOf(".")<0)
		{
			this.numeroAjusteBusquedaStr=temp;
		}
		else
		{
			this.numeroAjusteBusquedaStr=temp.substring(0,temp.indexOf("."));
		}		

		
	}
	/**
	 * @return Returns the numeroAjusteStr.
	 */
	public String getNumeroAjusteBusquedaStr() {
		return numeroAjusteBusquedaStr;
	}
	/**
	 * @param numeroAjusteStr The numeroAjusteStr to set.
	 */
	public void setNumeroAjusteBusquedaStr(String numeroAjusteStr) {
		this.numeroAjusteBusquedaStr = numeroAjusteStr;
		this.numeroAjusteBusqueda=Double.parseDouble(numeroAjusteStr.equals("")?"0":numeroAjusteStr);
	}
	/**
	 * @return Returns the tipoAjuste.
	 */
	public String getTipoAjusteBusqueda() {
		return tipoAjusteBusqueda;
	}
	/**
	 * @param tipoAjuste The tipoAjuste to set.
	 */
	public void setTipoAjusteBusqueda(String tipoAjuste) {
		this.tipoAjusteBusqueda = tipoAjuste;
	}
	/**
	 * @return Returns the fechaAjuste.
	 */
	public String getFechaAjusteBusqueda() {
		return fechaAjusteBusqueda;
	}
	/**
	 * @param fechaAjuste The fechaAjuste to set.
	 */
	public void setFechaAjusteBusqueda(String fechaAjuste) {
		this.fechaAjusteBusqueda = fechaAjuste;
	}
	
	/**
	 * @return Returns the valorAjuste.
	 */
	public double getValorAjuste() {
		return valorAjuste;
	}
	/**
	 * @param valorAjuste The valorAjuste to set.
	 */
	public void setValorAjuste(double valorAjuste) {
		this.valorAjuste = valorAjuste;
		String temp=this.valorAjuste+"";
		if(temp.indexOf(".")<0)
		{
			this.valorAjusteStr=temp;
		}
		else
		{
			this.valorAjusteStr=temp.substring(0,temp.indexOf("."));
		}		
	}
	/**
	 * @return Returns the valorAjusteStr.
	 */
	public String getValorAjusteStr() {
		return valorAjusteStr;
	}
	/**
	 * @param valorAjusteStr The valorAjusteStr to set.
	 */
	public void setValorAjusteStr(String valorAjusteStr) 
	{
		this.valorAjusteStr = valorAjusteStr;
		this.valorAjuste=Double.parseDouble(valorAjusteStr.equals("")?"-1":valorAjusteStr);
	}
	/**
	 * @return Returns the cuentaCobro.
	 */
	public double getCuentaCobro() {
		return cuentaCobro;
	}
	/**
	 * @param cuentaCobro The cuentaCobro to set.
	 */
	public void setCuentaCobro(double cuentaCobro) 
	{
		this.cuentaCobro = cuentaCobro;
		String temp=this.cuentaCobro+"";
		if(temp.indexOf(".")<0)
		{
			this.cuentaCobroStr=temp;
		}
		else
		{
			this.cuentaCobroStr=temp.substring(0,temp.indexOf("."));
		}		
	}
	/**
	 * @return Returns the cuentaCobroStr.
	 */
	public String getCuentaCobroStr() 
	{
		return cuentaCobroStr;
	}
	/**
	 * @param cuentaCobroStr The cuentaCobroStr to set.
	 */
	public void setCuentaCobroStr(String cuentaCobroStr) 
	{
		this.cuentaCobroStr = cuentaCobroStr;
		this.cuentaCobro=Double.parseDouble(cuentaCobroStr.equals("")?"0":cuentaCobroStr);
	}
	/**
	 * @return Returns the castigoCartera.
	 */
	public boolean isCastigoCartera() {
		return castigoCartera;
	}
	/**
	 * @param castigoCartera The castigoCartera to set.
	 */
	public void setCastigoCartera(boolean castigoCartera) {
		this.castigoCartera = castigoCartera;
	}
	/**
	 * @return Returns the codigoAjuste.
	 */
	public double getCodigoAjuste() {
		return codigoAjuste;
	}
	/**
	 * @param codigoAjuste The codigoAjuste to set.
	 */
	public void setCodigoAjuste(double codigoAjuste) {
		this.codigoAjuste = codigoAjuste;
	}
	/**
	 * @return Returns the codigoFactura.
	 */
	public int getCodigoFactura() {
		return codigoFactura;
	}
	/**
	 * @param codigoFactura The codigoFactura to set.
	 */
	public void setCodigoFactura(int codigoFactura) {
		this.codigoFactura = codigoFactura;
	}
	/**
	 * @return Returns the conceptoAjuste.
	 */
	public String getConceptoAjuste() {
		return conceptoAjuste;
	}
	/**
	 * @param conceptoAjuste The conceptoAjuste to set.
	 */
	public void setConceptoAjuste(String conceptoAjuste) {
		this.conceptoAjuste = conceptoAjuste;
	}
	/**
	 * @return Returns the conceptoCastigoCartera.
	 */
	public String getConceptoCastigoCartera() {
		return conceptoCastigoCartera;
	}
	/**
	 * @param conceptoCastigoCartera The conceptoCastigoCartera to set.
	 */
	public void setConceptoCastigoCartera(String conceptoCastigoCartera) {
		this.conceptoCastigoCartera = conceptoCastigoCartera;
	}
	/**
	 * @return Returns the consecutivoAjuste.
	 */
	public String getConsecutivoAjuste() {
		return consecutivoAjuste;
	}
	/**
	 * @param consecutivoAjuste The consecutivoAjuste to set.
	 */
	public void setConsecutivoAjuste(String consecutivoAjuste) {
		this.consecutivoAjuste = consecutivoAjuste;
	}
	/**
	 * @return Returns the consecutivoFactura.
	 */
	public String getConsecutivoFactura() {
		return consecutivoFactura;
	}
	/**
	 * @param consecutivoFactura The consecutivoFactura to set.
	 */
	public void setConsecutivoFactura(String consecutivoFactura) {
		this.consecutivoFactura = consecutivoFactura;
	}
	/**
	 * @return Returns the convenio.
	 */
	public InfoDatosInt getConvenio() {
		return convenio;
	}
	/**
	 * @param convenio The convenio to set.
	 */
	public void setConvenio(InfoDatosInt convenio) {
		this.convenio = convenio;
	}
	/**
	 * @return Returns the fechaAjuste.
	 */
	public String getFechaAjuste() {
		return fechaAjuste;
	}
	/**
	 * @param fechaAjuste The fechaAjuste to set.
	 */
	public void setFechaAjuste(String fechaAjuste) {
		this.fechaAjuste = fechaAjuste;
	}
	/**
	 * @return Returns the fechaReversion.
	 */
	public String getFechaReversion() {
		return fechaReversion;
	}
	/**
	 * @param fechaReversion The fechaReversion to set.
	 */
	public void setFechaReversion(String fechaReversion) {
		this.fechaReversion = fechaReversion;
	}
	/**
	 * @return Returns the institucion.
	 */
	public int getInstitucion() {
		return institucion;
	}
	/**
	 * @param institucion The institucion to set.
	 */
	public void setInstitucion(int institucion) {
		this.institucion = institucion;
	}
	/**
	 * @return Returns the isAjusteCuentaCobro.
	 */
	public boolean isAjusteCuentaCobro() {
		return ajusteCuentaCobro;
	}
	/**
	 * @param isAjusteCuentaCobro The isAjusteCuentaCobro to set.
	 */
	public void setAjusteCuentaCobro(boolean isAjusteCuentaCobro) {
		this.ajusteCuentaCobro = isAjusteCuentaCobro;
	}
	/**
	 * @return Returns the metodoAjuste.
	 */
	public String getMetodoAjuste() {
		return metodoAjuste;
	}
	/**
	 * @param metodoAjuste The metodoAjuste to set.
	 */
	public void setMetodoAjuste(String metodoAjuste) {
		this.metodoAjuste = metodoAjuste;
	}
	/**
	 * @return Returns the observaciones.
	 */
	public String getObservaciones() {
		return observaciones;
	}
	/**
	 * @param observaciones The observaciones to set.
	 */
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
	/**
	 * @return Returns the observacionesReversion.
	 */
	public String getObservacionesReversion() {
		return observacionesReversion;
	}
	/**
	 * @param observacionesReversion The observacionesReversion to set.
	 */
	public void setObservacionesReversion(String observacionesReversion) {
		this.observacionesReversion = observacionesReversion;
	}

	/**
	 * @return Returns the informacionCargada.
	 */
	public boolean isInformacionCargada() {
		return informacionCargada;
	}
	/**
	 * @param informacionCargada The informacionCargada to set.
	 */
	public void setInformacionCargada(boolean informacionCargada) {
		this.informacionCargada = informacionCargada;
	}
	/**
	 * @return Returns the tipoAjuste.
	 */
	public String getTipoAjuste() {
		return tipoAjuste;
	}
	/**
	 * @param tipoAjuste The tipoAjuste to set.
	 */
	public void setTipoAjuste(String tipoAjuste) {
		this.tipoAjuste = tipoAjuste;
	}
	/**
	 * @return Returns the saldo.
	 */
	public double getSaldo() {
		return saldo;
	}
	/**
	 * @param saldo The saldo to set.
	 */
	public void setSaldo(double saldo) {
		this.saldo = saldo;
	}
	/**
	 * @return Returns the nomTipoAjuste.
	 */
	public String getNomTipoAjuste() {
		return nomTipoAjuste;
	}
	/**
	 * @param nomTipoAjuste The nomTipoAjuste to set.
	 */
	public void setNomTipoAjuste(String nomTipoAjuste) {
		this.nomTipoAjuste = nomTipoAjuste;
	}
	/**
	 * @return Returns the fechaAprobacionAjuste.
	 */
	public String getFechaAprobacionAjuste() {
		return fechaAprobacionAjuste;
	}
	/**
	 * @param fechaAprobacionAjuste The fechaAprobacionAjuste to set.
	 */
	public void setFechaAprobacionAjuste(String fechaAprobacionAjuste) {
		this.fechaAprobacionAjuste = fechaAprobacionAjuste;
	}
	/**
	 * @return Returns the facturaExterna.
	 */
	public boolean isFacturaExterna() {
		return facturaExterna;
	}
	/**
	 * @param facturaExterna The facturaExterna to set.
	 */
	public void setFacturaExterna(boolean facturaExterna) {
		this.facturaExterna = facturaExterna;
	}
	/**
	 * @return Returns the facturas.
	 */
	public HashMap getFacturas() {
		return facturas;
	}
	/**
	 * @param facturas The facturas to set.
	 */
	public void setFacturas(HashMap facturas) {
		this.facturas = facturas;
	}
	/**
	 * @return Returns the facturas.
	 */
	public Object getFacturas(String key) {
		return facturas.get(key);
	}
	/**
	 * @param facturas The facturas to set.
	 */
	public void setFacturas(String key,Object value) 
	{
		this.facturas.put(key,value);
	}
	
	/**
	 * @return Returns the confimacionReversion.
	 */
	public boolean isConfimacionReversion() {
		return confimacionReversion;
	}
	/**
	 * @param confimacionReversion The confimacionReversion to set.
	 */
	public void setConfimacionReversion(boolean confimacionReversion) {
		this.confimacionReversion = confimacionReversion;
	}
	/**
	 * @return Returns the consecutivoAjusteNuevo.
	 */
	public String getConsecutivoAjusteNuevo() {
		return consecutivoAjusteNuevo;
	}
	/**
	 * @param consecutivoAjusteNuevo The consecutivoAjusteNuevo to set.
	 */
	public void setConsecutivoAjusteNuevo(String consecutivoAjusteNuevo) {
		this.consecutivoAjusteNuevo = consecutivoAjusteNuevo;
	}
	/**
	 * @return Returns the tipoAjusteNuevo.
	 */
	public String getTipoAjusteNuevo() {
		return tipoAjusteNuevo;
	}
	/**
	 * @param tipoAjusteNuevo The tipoAjusteNuevo to set.
	 */
	public void setTipoAjusteNuevo(String tipoAjusteNuevo) {
		this.tipoAjusteNuevo = tipoAjusteNuevo;
	}
	/**
	 * @return Returns the nombreConsecutivo.
	 */
	public String getNombreConsecutivo() {
		return nombreConsecutivo;
	}
	/**
	 * @param nombreConsecutivo The nombreConsecutivo to set.
	 */
	public void setNombreConsecutivo(String nombreConsecutivo) {
		this.nombreConsecutivo = nombreConsecutivo;
	}
	/**
	 * @return Returns the fechaElaboracion.
	 */
	public String getFechaElaboracion() {
		return fechaElaboracion;
	}
	/**
	 * @param fechaElaboracion The fechaElaboracion to set.
	 */
	public void setFechaElaboracion(String fechaElaboracion) {
		this.fechaElaboracion = fechaElaboracion;
	}
	/**
	 * @return Returns the hotaElaboracion.
	 */
	public String getHoraElaboracion() {
		return horaElaboracion;
	}
	/**
	 * @param hotaElaboracion The hotaElaboracion to set.
	 */
	public void setHoraElaboracion(String hotaElaboracion) {
		this.horaElaboracion = hotaElaboracion;
	}
	/**
	 * @return Returns the estadoAjuste.
	 */
	public int getEstadoAjuste() {
		return estadoAjuste;
	}
	/**
	 * @param estadoAjuste The estadoAjuste to set.
	 */
	public void setEstadoAjuste(int estadoAjuste) {
		this.estadoAjuste = estadoAjuste;
	}
	/**
	 * @return Returns the ajustes.
	 */
	public HashMap getAjustes() {
		return ajustes;
	}
	/**
	 * @param ajustes The ajustes to set.
	 */
	public void setAjustes(HashMap ajustes) {
		this.ajustes = ajustes;
	}
	
	/**
	 * @return Returns the ajustes.
	 */
	public Object getAjustes(String key) {
		return ajustes.get(key);
	}
	/**
	 * @param ajustes The ajustes to set.
	 */
	public void setAjustes(String key,Object value) {
		this.ajustes.put(key,value);
	}
	/**
	 * @return Returns the regSeleccionado.
	 */
	public int getRegSeleccionado() {
		return regSeleccionado;
	}
	/**
	 * @param regSeleccionado The regSeleccionado to set.
	 */
	public void setRegSeleccionado(int regSeleccionado) {
		this.regSeleccionado = regSeleccionado;
	}
	/**
	 * @return Returns the nombreConsecutivoNuevo.
	 */
	public String getNomTipoAjusteNuevo() {
		return nomTipoAjusteNuevo;
	}
	/**
	 * @param nombreConsecutivoNuevo The nombreConsecutivoNuevo to set.
	 */
	public void setNomTipoAjusteNuevo(String nombre) {
		this.nomTipoAjusteNuevo = nombre;
	}
	public int getCodigoCentroAtencion() {
		return codigoCentroAtencion;
	}
	public void setCodigoCentroAtencion(int codigoCentroAtencion) {
		this.codigoCentroAtencion = codigoCentroAtencion;
	}
	public String getNombreCentroAtencion() {
		return nombreCentroAtencion;
	}
	public void setNombreCentroAtencion(String nombreCentroAtencion) {
		this.nombreCentroAtencion = nombreCentroAtencion;
	}
}
