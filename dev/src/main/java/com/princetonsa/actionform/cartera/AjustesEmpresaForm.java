/*
 *
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
import util.ResultadoBoolean;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.UtilidadValidacion;
import util.Utilidades;
import util.ValoresPorDefecto;
 
/** 
 * @version 1.0, Julio 22 / 2005	
 * @author <a href="mailto:armando@PrincetonSA.com">Jorge Armando Osorio Velasquez</a>
 */
public class AjustesEmpresaForm extends ValidatorForm 
{
	
	/**
	 * Serial version UID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Variable que contien el codigo de la factura
	 */
	private double codigo;
	
	/**
	 * Variable que contiene el consecutivo del ajuste tomado de la tabla consecutivos.
	 */
	private String consecutivoAjuste;
	
	/**
	 * variable que maneja el tipo de ajuste
	 */
	private String tipoAjuste;
	
	/**
	 * variable que maneja la institucion del ajuste
	 */
	private int institucion;
	
	/**
	 * A veces no se toma desde el usuario, entonces la iniciare desde la forma.
	 */
	private int institucionForm;
	
	/**
	 * variable que indica si el ajuste es de castigoCartera
	 */
	private boolean castigoCartera;
	
	/**
	 * Encaso de ser un castigo esta variable maneja el concepto del castigo.
	 */
	private String conceptoCastigoCartera;
	
	
	/**
	 * variable que indica si el ajuste es de castigoCartera
	 */
	private boolean castigoCarteraS3;
	
	/**
	 * Encaso de ser un castigo esta variable maneja el concepto del castigo.
	 */
	private String conceptoCastigoCarteraS3;
	
	/**
	 * Para manejar la fecha del ajuste.
	 */
	private String fechaAjuste;
	
	/**
	 * Variable para manejar la fecha de elaboracion del ajuste
	 */
	private String fechaElaboracion;
	
	/**
	 * Variable para manejar la hora de elaboracion del ajuste.
	 */
	private String horaElaboracion;
	
	/**
	 * variable para manejar el login del usuario que genera el ajuste.
	 */
	private String usuario;
	
	/**
	 * Variable para manejar la cuenta de cobro a la que se le aplica el ajuste.
	 * pude ser null cuando el ajuste se hace directamente a una factura.
	 */
	private double cuentaCobro;
	
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
	 * Estado del ajuste.
	 */
	private int estadoAjuste;
	
	/**
	 * Estado de la funcionalidad
	 */
	private String estado;
	
	
	//////mapas para el manejo de las facturas y los detalles facturas.
	/**
	 * Mapa para manejar las facturas.
	 */
	private HashMap facturas;
	
	/**
	 * Mapa para menejar los servicios de una factura.
	 */
	private HashMap serviciosFacturas;
	
	/**
	 * variable para capturar el consecutivo de una factura, en caso de que 
	 * realice el ajuste a una factura espec&iacute;fica
	 */
	private int factura;
    
    /**
     * consecutivo Factura para consulta
     */
    private String consecutivoFac;
    
    /**
     * consecutivo cuentaCobroStr para consulta
     */
    private String cuentaCobroStr;
    
    private int codigoFactura;
    
    /**
     * 
     */
    private String nombreCentroAtencion;
    
    /**
     * 
     */
    private int codigoCentroAtencion;
	
    /**
     * Variables para manejar el numero del ajuste.
     */
	private double numeroAjuste;
	private String numeroAjusteStr;
	
	/**
	 * Variable para manejar el tipo de ajuste, de la informacion de captura es decir la 3 seccion (Informacion General del Ajuste).
	 */
	private String tipoAjusteSeccion3;
	/**
	 * En caso de tratarse ajustes por factura
	 * Variable que me indica si la factura es externa, espto para saber si debo llevar el ajuste a niver de servicios o no
	 */
	private boolean facturaExterna;
	
	/**
	 * Variable que me indica si el ajuste se esta realizando por factura.
	 */
	private boolean ajustePorFactura;
	/**
	 * Variable que me indica si el ajuste se esta realizando por CuentaCobro.
	 */
	private boolean ajustePorCuentaCobro;
	
	private ResultadoBoolean mostrarMensaje;
	/**
	 * variable para almacenar el nombre del consecutivo para los ajustes.
	 */
	private String nombreConsecutivo;
	
	/**
	 * Infodatos para almacenar el convenio, ya sea para la factura o cuenta de cobro.
	 */
	private InfoDatosInt convenio;
	
	/**
	 * Variable para manejar el saldo, ya sea para la factura o la cuenta de cobro.
	 */
	private double saldo;
	/**
	 * Variable para manejar el saldo, ya sea para la factura o la cuenta de cobro.
	 */
	private String saldoStr;
	
	/**
	 * VAriable que me indica si se cargo informacion o no
	 */
	private boolean informacionCargada;
	
	private int estadoCuentaCobro;
	
	/**
	 * Variable que maneja las facturas definidas para el ajuste manual.
	 * codigo@consecutivo@saldo+ConstanteBD.separadosSplit+codigo@consecutivo@saldo
	 */
	private String facturasRelacionada;
	/////////*******VARIABLES PROPIAS DE UNA FACTURA********////////////////////
	
	private double saldoFactura;
    private double totalFactura;
	private String saldoFacturaStr;
	private double valorAjusteFactura;
	private String valorAjusteFacturaStr;
	private String metodoAjusteFactura; 
	private String conceptoAjusteFactura;
	/////////***FIN VARIABLES PROPIAS DE UNA FACTURA********////////////////////
	
	//variables necesarias para realizar la busqueda de las facturas en un pop-up
	private HashMap facturasPopUp;
	private int numeroFacturasPopUp;
	
	//variables necesarias para realizar la busqueda de los servicios de las facturas en un pop-up
	private HashMap serviciosFacturasPopUp;
	private int numeroserviciosFacturasPopUp;

	
	/**
	 *Nombre del indice en el mapa por el cual se va a ordenar. 
	 */
	private String indice;
	private String ultimoIndice;
	
	private String indiceServicios;
	private String ultimoIndiceServicios;
	
	/**
	 * Variable para manejar el indice de los mapas.
	 */
	private int index;

	//variables para caso de modificacion de ajustes de una factura.
	private boolean confirmacionModificacionAjusteFactura;
	private boolean existeModifiacionValorAjusteAF;
	private boolean existeModifiacionMetodoAjusteAF;
	private boolean recalcular;
	
	
	/**
	 * Variable para manejar solo en la modificacion.
	 * Dice si una factura o cuenta de cobro se encuentra en estado radicada.
	 */
	private boolean facturaCuentaCobroRadicada;
	/**
	 * Variable que me indica que es modificacion.
	 */
	private boolean modificacion;
	

	/**
	 * Campo para realizar las busquedas en los pop-up's
	 */
	private String campoBusqueda;
	private String valorCampoBusqueda;
	
	/**
	 * Maneja el motivo de la Anulacion.
	 */
	private String motivoAnulacion;
    
       
    /**
     * Mapa para almacenar los asocios de un servicio.
     */
    private HashMap serviciosAsocios;
    
    /**
     * Mapa para almacenar los asocios de un servicio.
     */
    private HashMap serviciosAsociosPopUp;
    
    /**
     * 
     */
    private HashMap facturasMismoConsecutivo;
    
    /**
     * 
     */
    private boolean mostrarPopUpFacturasMismoConsecutivo;
    
    /**
     * 
     */
    private int indiceConsecutivoCargar;
    
    /**
     * 
     */
    private String nombreEntidadFactura;
    
    /**
     * 
     */
    private int regEliminar;
	
    
    /**
     * 
     */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores= new ActionErrors();
		if(estado.equals("cargarFactura"))
		{	
			this.mostrarMensaje.setResultado(false);
			this.resetBasico();
			this.informacionCargada=false;
			if(this.castigoCartera)
			{
				if(this.conceptoCastigoCartera.equals(ConstantesBD.codigoNuncaValido+""))
				{
					errores.add("Concepto Castigo Cartera",new ActionMessage("errors.required","El Concepto del Castigo de Cartera"));
				}
			}
			if(this.consecutivoFac.equals(""))
			{
				errores.add("consecutvio factura requerido",new ActionMessage("errors.required","El Consecutivo de la Factura"));
			}
			else
			{
				this.codigoFactura=Utilidades.obtenerCodigoFactura(Utilidades.convertirAEntero(this.getConsecutivoFac()),this.getInstitucion());
				
			}
			if(!UtilidadTexto.getBoolean(ValoresPorDefecto.getInstitucionMultiempresa(institucionForm)))
			{
				if(this.codigoFactura==ConstantesBD.codigoNuncaValido)
				{
					this.codigoFactura=Utilidades.obtenerCodigoFactura(this.codigoFactura,this.institucionForm);
				}
				else if(this.codigoFactura==ConstantesBD.codigoNuncaValido)
				{
					errores.add("No existe Factura", new ActionMessage("error.facturaInexistente"));
				}
				else
				{
					//aqui van las validaciones para la factura
					String[] estadoVector=Utilidades.obtenerEstadoFactura(this.codigoFactura).split(ConstantesBD.separadorSplit);
					if(Integer.parseInt(estadoVector[0])!=ConstantesBD.codigoEstadoFacturacionFacturada)
					{
						errores.add("estado diferente", new ActionMessage("error.facturaEstadoDiferenteFacturada",estadoVector[1]));
					}
					
					this.facturaExterna=ValidacionesFactura.esFacturaExterna(this.codigoFactura);
					if(ValidacionesFactura.facturaTieneAjustesPendientes(this.codigoFactura))
					{
						errores.add("Ajustes Pendientes", new ActionMessage("error.facturaConAjustesPendientes"));
					}
					if(ValidacionesFactura.facturaTienePagosPendientes(this.codigoFactura))
					{
						errores.add("Pagos Pendientes", new ActionMessage("error.facturaConPagosPendientes"));
					}
					if(ValidacionesFactura.esFacturaCerrada(this.codigoFactura))
					{
						errores.add("Factura Cerrada", new ActionMessage("error.ajustesEmpresa.facturaCerrada",this.consecutivoFac+""));
					}
				}
			}
		}
		else if(estado.equals("cargarCuentaCobro"))
		{	
			this.mostrarMensaje.setResultado(false);
			this.resetBasico();
			this.informacionCargada=false;
			if(this.castigoCartera)
			{
				if(this.conceptoCastigoCartera.equals(ConstantesBD.codigoNuncaValido+""))
				{
					errores.add("Concepto Castigo Cartera",new ActionMessage("errors.required","El Concepto del Castigo de Cartera"));
				}
			}
			if(this.cuentaCobroStr.equals(""))
			{
				errores.add("Cuenta Cobro requerido",new ActionMessage("errors.required","La Cuenta de Cobro"));
			}
			else if(!UtilidadValidacion.existeCuentaCobro(this.cuentaCobro,this.institucion))
			{
				errores.add("No existe Cuenta Cobro", new ActionMessage("error.cuentaCobro.Inexistente"));
			}
			else
			{
				this.estadoCuentaCobro=Utilidades.obtenerEstadoCuentaCobro(this.cuentaCobro,this.institucion);
				if(this.estadoCuentaCobro!=ConstantesBD.codigoEstadoCarteraGenerado&&this.estadoCuentaCobro!=ConstantesBD.codigoEstadoCarteraRadicada)
				{
					errores.add("Solo ajustes para CXC en estado generado-radicado", new ActionMessage("error.cartera.ajustes.soloAjustesEstadoRadicadoGenerado"));
				}
				if(UtilidadValidacion.cuentaCobroAjustesPendientes(this.cuentaCobro,this.institucion))
				{
					errores.add("cuenta cobro con ajustes Pendientes", new ActionMessage("error.cartera.ajustes.cuentaCobroConAjustesPendites"));
				}
				//@todo pendiente validar si la cuenta de cobro tiene pagos pendientes (todavia no se sabe la modificacion de la estructura de pagos).
				if(Utilidades.numFacturasCuentaCobro(this.cuentaCobro,this.institucion)<=0)
				{
					errores.add("cuenta cobro sin facturas relacionadas",new ActionMessage("error.cartera.ajustes.cuentaCobroSinFacturasRelacionadas",this.cuentaCobroStr));
				}
				if(UtilidadValidacion.cuentaCobroConFactEnAjustesPendiente(this.cuentaCobro,this.institucion))
				{
					errores.add("cuenta cobro con facturas en estados pendiente",new ActionMessage("error.cartera.ajustes.cuentaCobroFacturasAjustesPendientas",this.cuentaCobroStr));
				}
				//@todo falta validar que las facturas relacionadas a la cuenta de cobro no se encuentren ligadas a niguna aplicacion de pagos en estado pendiente.
			}
		}
		//validacion comunes de cargar cuenta de cobro y cargar facturas.
		else if(estado.equals("guardarAjusteGeneral"))
		{
			if(this.castigoCarteraS3)
			{
				if(this.conceptoCastigoCarteraS3.equals(ConstantesBD.codigoNuncaValido+""))
				{
					errores.add("Concepto Castigo Cartera",new ActionMessage("errors.required","El Concepto del Castigo de Cartera"));
				}
			}
			if(this.tipoAjusteSeccion3.equals(ConstantesBD.codigoNuncaValido+""))
			{
				errores.add("El tipo de Ajuste es Requerido",new ActionMessage("errors.required","El Tipo de Ajuste"));
			}
			if((this.fechaAjuste.trim()).equals(""))
			{
				errores.add("Fecha Requerido",new ActionMessage("errors.required","La fecha del ajuste"));
			}
			else
			{
				if(!UtilidadFecha.validarFecha(this.fechaAjuste))
				{
					errores.add("fecha", new ActionMessage("errors.formatoFechaInvalido",this.fechaAjuste));
				}
				else
				{
					//validaciones con la fecha
					if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.fechaAjuste,UtilidadFecha.getFechaActual()))
					{
						errores.add("Fecha Mayor del Sistema",new ActionMessage("errors.fechaPosteriorAOtraDeReferencia","del ajuste ("+this.fechaAjuste+")","Actual ("+UtilidadFecha.getFechaActual()+")"));
					}
					String[] fechaVector = this.fechaAjuste.split("/", 3);
					if(UtilidadValidacion.existeCierreMensual(Integer.parseInt(fechaVector[1]),Integer.parseInt(fechaVector[2]),this.institucion))
					{
						errores.add("Existe cierre para esa fecha",new ActionMessage("error.cierre.yaTieneCierreMensual"));
					}
					if(UtilidadValidacion.existeCierreSaldosIniciales(this.institucion))
					{
						if(UtilidadValidacion.fechaMenorIgualAFechaCierreSalodsIniciales(this.fechaAjuste,this.institucion))
							{
							errores.add("Fecha menor A Cierre Saldo Inicial",new ActionMessage("errors.fechaAnteriorAOtraDeReferencia","del ajuste","del Cierre de Saldo Inicial"));
							}
					}
					if(Utilidades.obtenerEstadoCuentaCobro(Integer.parseInt(this.cuentaCobroStr),this.institucion)==ConstantesBD.codigoEstadoCarteraRadicada)
					{
						if(UtilidadFecha.esFechaMenorQueOtraReferencia(this.fechaAjuste,Utilidades.obtenerFechaRadicacionCuentaCobro(Integer.parseInt(this.cuentaCobroStr))))
						{
							errores.add("Fecha menor A Radicacion",new ActionMessage("errors.fechaAnteriorIgualActual","del ajuste ("+this.fechaAjuste+")","Radicación ("+Utilidades.obtenerFechaRadicacionCuentaCobro(Integer.parseInt(this.cuentaCobroStr))+")"));
						}
					}
				}
			}
			if(this.conceptoAjuste.equals("-1"))
			{
				errores.add("Concepto Ajuste",new ActionMessage("errors.required","El concepto del Ajuste"));
			}
			if(this.metodoAjuste.equals("-1"))
			{
				errores.add("Metodo Ajuste",new ActionMessage("errors.required","El Metodo de Ajuste"));
			}
			//validacion preventiva en caso del que el usuario envie el estado por la URL
			if(this.valorAjusteStr.equals(""))
			{
				errores.add("Valor Ajustes",new ActionMessage("errors.required","El Valor del Ajuste"));
			}
			if(this.valorAjuste<=0)
			{
				errores.add("Valor Ajuste igualmenor cero",new ActionMessage("errors.floatMayorQue","El Valor del Ajuste","0"));
			}
			if(tipoAjusteSeccion3.equals(ConstantesBD.ajustesCreditoFuncionalidadAjustes.getAcronimo()))
			{
				if(this.valorAjuste>this.saldo)
				{
					errores.add("El valor ajuste mayor a saldo",new ActionMessage("errors.MenorIgualQue","El valor del ajuste",this.saldoStr));
				}
			}
		}
		else if(estado.equals("cargarAjuste"))
		{
			this.setInformacionCargada(false);
			this.cuentaCobroStr="";
			this.consecutivoFac="";
			this.mostrarMensaje.setResultado(false);
			if(this.numeroAjusteStr.equals("")||tipoAjuste.equals(ConstantesBD.codigoNuncaValido+""))
			{
				errores.add("Cargar Ajuste",new ActionMessage("error.cartera.ajustes.tipoAjusteYNumeroRequerido"));
			}
			else
			{
				if(Utilidades.obtenercodigoAjusteEmpresa(this.numeroAjusteStr,this.tipoAjuste,this.institucion)>0)
					{
						String estadoAjuste=UtilidadValidacion.obtenerEstadoAjusteCartera(Utilidades.obtenercodigoAjusteEmpresa(this.numeroAjusteStr,this.tipoAjuste,this.institucion),this.institucion);
						if(Integer.parseInt(estadoAjuste.split("@")[0])!=ConstantesBD.codigoEstadoCarteraGenerado)
						{
							String nomAjuste="";
							if(tipoAjuste.equals(ConstantesBD.ajustesDebitoFuncionalidadAjustes.getAcronimo()))
							{
								nomAjuste=ConstantesBD.ajustesDebitoFuncionalidadAjustes.getNombre();
							}
							else if(tipoAjuste.equals(ConstantesBD.ajustesCreditoFuncionalidadAjustes.getAcronimo()))
							{
								nomAjuste=ConstantesBD.ajustesCreditoFuncionalidadAjustes.getNombre();
							}
							errores.add("Ajuste Estado Diferente",new ActionMessage("error.cartera.ajustes.ajusteEstado",nomAjuste,numeroAjusteStr,estadoAjuste.split("@")[1]));
						}
					}
				else
				{
					String nomAjuste="";
					if(tipoAjuste.equals(ConstantesBD.ajustesDebitoFuncionalidadAjustes.getAcronimo()))
					{
						nomAjuste=ConstantesBD.ajustesDebitoFuncionalidadAjustes.getNombre();
					}
					else if(tipoAjuste.equals(ConstantesBD.ajustesCreditoFuncionalidadAjustes.getAcronimo()))
					{
						nomAjuste=ConstantesBD.ajustesCreditoFuncionalidadAjustes.getNombre();
					}
					errores.add("No existe Ajuste", new ActionMessage("error.ajusteInexistente",nomAjuste,numeroAjusteStr));
				}
			}
		}
		else if(estado.equals("anularAjuste"))
		{
			if(this.motivoAnulacion.trim().equals(""))
			{
				errores.add("Motivo de Anulacion",new ActionMessage("errors.required","El Motivo de la Anulacion"));
			}
		}
		else if(estado.equals("guardarDetalle"))
		{
			HashMap tempoValAjus=new HashMap();
			int numReg=0;
			for(int k = 0 ; k < Utilidades.convertirAEntero(this.getServiciosFacturas("numeroregistros")+"") ; k ++)
			{
				if(Utilidades.convertirAEntero(this.getServiciosFacturas("concepto_"+k)+"")<=0)
				{
					errores.add("concepto",new ActionMessage("errors.required","Concepto de ajuste del servicio/articulo "+this.getServiciosFacturas("nombreservart_"+k)));
				}
				
				for(int i=0;i<k;i++)
				{
					if(
							(Utilidades.convertirAEntero(this.getServiciosFacturas("solicitud_"+i)+"")==Utilidades.convertirAEntero(this.getServiciosFacturas("solicitud_"+k)+""))&&
							(Utilidades.convertirAEntero(this.getServiciosFacturas("codigoservart_"+i)+"")==Utilidades.convertirAEntero(this.getServiciosFacturas("codigoservart_"+k)+""))&&
							(this.getServiciosFacturas("concepto_"+i)+"").equals(this.getServiciosFacturas("concepto_"+k)+"")
					)
					{
						errores.add("CONCEPTO REPETIDO",new ActionMessage("error.ajustesEmpresa.concetpAjusteRepetido"," del servicio/articulo "+this.getServiciosFacturas("nombreservart_"+k)));
					}
				}
				if(this.getTipoAjusteSeccion3().equals(ConstantesBD.ajustesCreditoFuncionalidadAjustes.getAcronimo()))
				{
					String key=this.getServiciosFacturas("solicitud_"+k)+"_"+this.getServiciosFacturas("codigoservart_"+k);
					if(tempoValAjus.containsKey(key))
					{
						double valAjus=Utilidades.convertirADouble(tempoValAjus.get("valorajuste_"+key)+"",true)+Utilidades.convertirADouble(this.getServiciosFacturas("valorajuste_"+k)+"",true);
						tempoValAjus.put("valorajuste_"+key, valAjus+"");
					}
					else
					{
						tempoValAjus.put("llave_"+numReg,key);
						tempoValAjus.put(key, this.getServiciosFacturas("nombreservart_"+k)+"");
						tempoValAjus.put("saldo_"+key, this.getServiciosFacturas("saldo_"+k)+"");
						tempoValAjus.put("valorajuste_"+key, this.getServiciosFacturas("valorajuste_"+k)+"");
						numReg++;
					}
				}
			}
			if(this.getTipoAjusteSeccion3().equals(ConstantesBD.ajustesCreditoFuncionalidadAjustes.getAcronimo()))
			{
				for(int i=0;i<numReg;i++)
				{
					String key=tempoValAjus.get("llave_"+i)+"";
					if(Utilidades.convertirADouble(tempoValAjus.get("valorajuste_"+key)+"",true)>Utilidades.convertirADouble(tempoValAjus.get("saldo_"+key)+"",true))
					{
						errores.add("El valor ajuste mayor a saldo",new ActionMessage("errors.MenorIgualQue","El valor del ajuste del servicio/articulo "+tempoValAjus.get(key)+" "+UtilidadTexto.formatearValores(tempoValAjus.get("valorajuste_"+key)+""),UtilidadTexto.formatearValores(tempoValAjus.get("saldo_"+key)+"")));
					}
				}
			}
			
		}
		else if(estado.equals("guardarDetalleAsocios"))
		{
			HashMap tempoValAjus=new HashMap();
			int numReg=0;
			for(int k = 0 ; k < Utilidades.convertirAEntero(this.getServiciosAsocios("numRegistros")+"") ; k ++)
			{
				if(Utilidades.convertirAEntero(this.getServiciosAsocios("concepto_"+k)+"")<=0)
				{
					errores.add("concepto",new ActionMessage("errors.required","Concepto de ajuste del asocio "+this.getServiciosAsocios("nombreasocio_"+k)));
				}
				
				
				for(int i=0;i<k;i++)
				{
					if(
							(this.getServiciosAsocios("codigoaxioma_"+i)+"").equals(this.getServiciosAsocios("codigoaxioma_"+k)+"")&&
							(this.getServiciosAsocios("concepto_"+i)+"").equals(this.getServiciosAsocios("concepto_"+k)+"")
					)
					{
						errores.add("CONCEPTO REPETIDO",new ActionMessage("error.ajustesEmpresa.concetpAjusteRepetido"," del Asocio "+this.getServiciosAsocios("nombreasocio_"+k)));
					}
				}
				if(this.getTipoAjusteSeccion3().equals(ConstantesBD.ajustesCreditoFuncionalidadAjustes.getAcronimo()))
				{
					String key=this.getServiciosAsocios("codigoaxioma_"+k)+"";
					if(tempoValAjus.containsKey(key))
					{
						double valAjus=Utilidades.convertirADouble(tempoValAjus.get("valorajuste_"+key)+"",true)+Utilidades.convertirADouble(this.getServiciosAsocios("valorajuste_"+k)+"",true);
						tempoValAjus.put("valorajuste_"+key, valAjus+"");
					}
					else
					{
						tempoValAjus.put("llave_"+numReg,key);
						tempoValAjus.put(key, this.getServiciosAsocios("nombreasocio_"+k)+"");
						tempoValAjus.put("saldo_"+key, this.getServiciosAsocios("saldo_"+k)+"");
						tempoValAjus.put("valorajuste_"+key, this.getServiciosAsocios("valorajuste_"+k)+"");
						numReg++;
					}
				}
			}
			if(this.getTipoAjusteSeccion3().equals(ConstantesBD.ajustesCreditoFuncionalidadAjustes.getAcronimo()))
			{
				for(int i=0;i<numReg;i++)
				{
					String key=tempoValAjus.get("llave_"+i)+"";
					if(Utilidades.convertirADouble(tempoValAjus.get("valorajuste_"+key)+"",true)>Utilidades.convertirADouble(tempoValAjus.get("saldo_"+key)+"",true))
					{
						errores.add("El valor ajuste mayor a saldo",new ActionMessage("errors.MenorIgualQue","El valor del ajuste del asocio "+tempoValAjus.get(key)+" "+UtilidadTexto.formatearValores(tempoValAjus.get("valorajuste_"+key)+""),UtilidadTexto.formatearValores(tempoValAjus.get("saldo_"+key)+"")));
					}
				}
			}
		}
			
		return errores;
	}
	
	public void reset(int institucion)
	{
		this.institucionForm=institucion;
		this.consecutivoFac="";
		this.tipoAjuste=ConstantesBD.codigoNuncaValido+"";
		this.castigoCartera=false;
		this.conceptoCastigoCartera="-1";
		this.numeroAjuste=ConstantesBD.codigoNuncaValido;
		this.numeroAjusteStr="";
		this.cuentaCobroStr="";
		this.codigo=ConstantesBD.codigoNuncaValido;
		this.consecutivoAjuste="";
		this.tipoAjuste=ConstantesBD.codigoNuncaValido+"";
		this.institucion=ConstantesBD.codigoNuncaValido;
		this.castigoCartera=false;
		this.conceptoCastigoCartera="-1";
		this.castigoCarteraS3=false;
		this.conceptoCastigoCarteraS3="-1";
		this.fechaAjuste=UtilidadFecha.getFechaActual();
		this.fechaElaboracion="";
		this.horaElaboracion="";
		this.usuario="";
		this.cuentaCobro=ConstantesBD.codigoNuncaValido;
		this.conceptoAjuste="-1";
		this.metodoAjuste="-1";
		this.valorAjuste=ConstantesBD.codigoNuncaValido;
		this.observaciones="";
		this.estadoAjuste=ConstantesBD.codigoNuncaValido;
		this.estado="";
		this.facturas=new HashMap();
		this.serviciosFacturas=new HashMap();
		this.factura=ConstantesBD.codigoNuncaValido;
		this.codigoFactura=ConstantesBD.codigoNuncaValido;
		this.cuentaCobroStr="";
		this.facturaExterna=false;
		this.tipoAjusteSeccion3=ConstantesBD.codigoNuncaValido+"";
		this.ajustePorFactura=false;
		this.ajustePorCuentaCobro=false;
		this.nombreConsecutivo="";
		this.mostrarMensaje=new ResultadoBoolean(false);
		this.convenio=new InfoDatosInt();
		this.saldo=ConstantesBD.codigoNuncaValido;
		this.saldoStr="";
		this.informacionCargada=false;
		this.valorAjusteStr="";
		this.estadoCuentaCobro=ConstantesBD.codigoNuncaValido;
		this.facturasRelacionada="";
		this.index=ConstantesBD.codigoNuncaValido;
		this.facturasPopUp=new HashMap();
		this.numeroFacturasPopUp=0;
		this.serviciosFacturasPopUp=new HashMap();
		this.numeroserviciosFacturasPopUp=0;
		this.indice="";
		this.ultimoIndice="";
		this.indiceServicios="";
		this.ultimoIndiceServicios="";
		this.confirmacionModificacionAjusteFactura=false;
		this.existeModifiacionValorAjusteAF=false;
		this.existeModifiacionMetodoAjusteAF=false;
		this.recalcular=false;
		this.modificacion=false;
		this.facturaCuentaCobroRadicada=false;
		this.campoBusqueda="";
		this.valorCampoBusqueda="";
		this.motivoAnulacion="";
        this.serviciosAsocios=new HashMap();
        this.totalFactura=ConstantesBD.codigoNuncaValidoDouble;
        this.serviciosAsociosPopUp=new HashMap();
        this.codigoCentroAtencion=ConstantesBD.codigoNuncaValido;
        this.nombreCentroAtencion="";
        this.facturasMismoConsecutivo=new HashMap();
        this.facturasMismoConsecutivo.put("numRegistros", "0");
        this.mostrarPopUpFacturasMismoConsecutivo=false;
        this.indiceConsecutivoCargar=ConstantesBD.codigoNuncaValido;
        this.nombreEntidadFactura="";
        this.regEliminar=ConstantesBD.codigoNuncaValido;
	}
	
	public void resetBasico()
	{
		this.numeroAjuste=ConstantesBD.codigoNuncaValido;
		this.numeroAjusteStr="";
		this.tipoAjuste=ConstantesBD.codigoNuncaValido+"";
		this.setValorAjusteStr("");
		this.setMetodoAjuste("");
		this.fechaAjuste=UtilidadFecha.getFechaActual();
		this.fechaElaboracion="";
		this.horaElaboracion="";
		this.conceptoCastigoCarteraS3="-1";
		this.informacionCargada=false;
		this.saldoStr="";
		this.regEliminar=ConstantesBD.codigoNuncaValido;
	}
	
	/**
	 * @return the regEliminar
	 */
	public int getRegEliminar() {
		return regEliminar;
	}

	/**
	 * @param regEliminar the regEliminar to set
	 */
	public void setRegEliminar(int regEliminar) {
		this.regEliminar = regEliminar;
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
		if(!isInformacionCargada())
			this.castigoCarteraS3=castigoCartera;
	}
	/**
	 * @return Returns the codigo.
	 */
	public double getCodigo() {
		return codigo;
	}
	/**
	 * @param codigo The codigo to set.
	 */
	public void setCodigo(double codigo) {
		this.codigo = codigo;
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
	 * @return Returns the horaElaboracion.
	 */
	public String getHoraElaboracion() {
		return horaElaboracion;
	}
	/**
	 * @param horaElaboracion The horaElaboracion to set.
	 */
	public void setHoraElaboracion(String horaElaboracion) {
		this.horaElaboracion = horaElaboracion;
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
	 * @return Returns the usuario.
	 */
	public String getUsuario() {
		return usuario;
	}
	/**
	 * @param usuario The usuario to set.
	 */
	public void setUsuario(String usuario) {
		this.usuario = usuario;
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
		String temp=UtilidadTexto.formatearValores(this.valorAjuste+"","#");
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
	 * @return Returns the factura.
	 */
	public int getFactura() {
		return factura;
	}
	/**
	 * @param factura The factura to set.
	 */
	public void setFactura(int factura) 
	{
		this.factura = factura;
		this.consecutivoFac=factura+"";
		this.codigoFactura=Utilidades.obtenerCodigoFactura(this.factura,this.institucion);
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
	
	public Object getFacturas(String key)
	{
		return this.facturas.get(key);
	}
	
	public void setFacturas(String key,Object value) 
	{
		this.facturas.put(key,value);
	}
	/**
	 * @return Returns the serviciosFacturas.
	 */
	public HashMap getServiciosFacturas() {
		return serviciosFacturas;
	}
	/**
	 * 
	 * @param serviciosFacturas The serviciosFacturas to set.
	 */
	public void setServiciosFacturas(HashMap serviciosFacturas) {
		this.serviciosFacturas = serviciosFacturas;
	}
	
	
	public Object getServiciosFacturas(String key)
	{
		return this.serviciosFacturas.get(key);
	}
	
	public void setServiciosFacturas(String key,Object value) 
	{
		this.serviciosFacturas.put(key,value);
	}
	
	public void removeServiciosFacturas(String key){
		this.serviciosFacturas.remove(key);
	}
	
	/**
	 * @return Returns the consecutivoFac.
	 */
	public String getConsecutivoFac() {
		return consecutivoFac;
	}
	/**
	 * @param consecutivoFac The consecutivoFac to set.
	 */
	public void setConsecutivoFac(String consecutivoFac) 
	{
		try
		{
			this.consecutivoFac = consecutivoFac.trim();
			this.factura=Integer.parseInt(this.consecutivoFac.equals("")?"0":this.consecutivoFac);
			//this.codigoFactura=Utilidades.obtenerCodigoFactura(this.factura,this.institucion);
		}
		catch(Exception e)
		{
			
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
		try
		{
			this.cuentaCobroStr = cuentaCobroStr;
			this.cuentaCobro=Utilidades.convertirADouble(cuentaCobroStr.equals("")?"0":cuentaCobroStr);
		}
		catch(Exception e)
		{
			
		}
	}
	/**
	 * @return Returns the numeroAjuste.
	 */
	public double getNumeroAjuste() {
		return numeroAjuste;
	}
	/**
	 * @param numeroAjuste The numeroAjuste to set.
	 */
	public void setNumeroAjuste(double numeroAjuste) 
	{
		this.numeroAjuste = numeroAjuste;
		String temp=this.numeroAjuste+"";
		if(temp.indexOf(".")<0)
		{
			this.numeroAjusteStr=temp;
		}
		else
		{
			this.numeroAjusteStr=temp.substring(0,temp.indexOf("."));
		}		

		
	}
	/**
	 * @return Returns the numeroAjusteStr.
	 */
	public String getNumeroAjusteStr() {
		return numeroAjusteStr;
	}
	/**
	 * @param numeroAjusteStr The numeroAjusteStr to set.
	 */
	public void setNumeroAjusteStr(String numeroAjusteStr) 
	{
		try
		{
			this.numeroAjusteStr = numeroAjusteStr;
			this.numeroAjuste=Utilidades.convertirADouble(numeroAjusteStr.equals("")?"0":numeroAjusteStr);
		}
		catch(Exception e)
		{
			
		}

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
	 * @return Returns the codigoFactura.
	 */
	public int getCodigoFactura() {
		return codigoFactura;
	}
	/**
	 * @param codigoFactura The codigoFactura to set.
	 */
	public void setCodigoFactura(int codigoFactura) 
	{
		this.codigoFactura = codigoFactura;
	}
	/**
	 * @return Returns the tipoAjusteSeccion3.
	 */
	public String getTipoAjusteSeccion3() {
		return tipoAjusteSeccion3;
	}
	/**
	 * @param tipoAjusteSeccion3 The tipoAjusteSeccion3 to set.
	 */
	public void setTipoAjusteSeccion3(String tipoAjusteSeccion3) {
		this.tipoAjusteSeccion3 = tipoAjusteSeccion3;
	}
	/**
	 * @return Returns the ajustePorCuentaCobro.
	 */
	public boolean isAjustePorCuentaCobro() {
		return ajustePorCuentaCobro;
	}
	/**
	 * @param ajustePorCuentaCobro The ajustePorCuentaCobro to set.
	 */
	public void setAjustePorCuentaCobro(boolean ajustePorCuentaCobro) {
		this.ajustePorCuentaCobro = ajustePorCuentaCobro;
	}
	/**
	 * @return Returns the ajustePorFactura.
	 */
	public boolean isAjustePorFactura() {
		return ajustePorFactura;
	}
	/**
	 * @param ajustePorFactura The ajustePorFactura to set.
	 */
	public void setAjustePorFactura(boolean ajustePorFactura) {
		this.ajustePorFactura = ajustePorFactura;
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
		if(!UtilidadTexto.isEmpty(valorAjusteStr))
			this.valorAjusteStr = UtilidadTexto.formatearValores(valorAjusteStr,"#");
		else
			this.valorAjusteStr="";
		this.valorAjuste=Utilidades.convertirADouble(valorAjusteStr.equals("")?"-1":valorAjusteStr);
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
	 * @return Returns the mostrarMensaje.
	 */
	public ResultadoBoolean getMostrarMensaje() {
		return mostrarMensaje;
	}
	/**
	 * @param mostrarMensaje The mostrarMensaje to set.
	 */
	public void setMostrarMensaje(ResultadoBoolean mostrarMensaje) {
		this.mostrarMensaje = mostrarMensaje;
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
	 * @return Returns the saldo.
	 */
	public double getSaldo() {
		return saldo;
	}
	/**
	 * @param saldo The saldo to set.
	 */
	public void setSaldo(double saldo) 
	{
		this.saldo = saldo;
		String temp=this.saldo+"";
		if(temp.indexOf(".")<0)
		{
			this.saldoStr=temp;
		}
		else
		{
			this.saldoStr=temp.substring(0,temp.indexOf("."));
		}
	}
	/**
	 * @return Returns the saldoStr.
	 */
	public String getSaldoStr() {
		return saldoStr;
	}
	/**
	 * @param saldoStr The saldoStr to set.
	 */
	public void setSaldoStr(String saldoStr) 
	{
		try
		{
			this.saldoStr = saldoStr;
			this.saldo=Utilidades.convertirADouble(this.saldoStr);
		}
		catch(Exception e)
		{
			
		}

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
	 * @return Returns the castigoCarteraS3.
	 */
	public boolean isCastigoCarteraS3() {
		return castigoCarteraS3;
	}
	/**
	 * @param castigoCarteraS3 The castigoCarteraS3 to set.
	 */
	public void setCastigoCarteraS3(boolean castigoCarteraS3) {
		this.castigoCarteraS3 = castigoCarteraS3;
	}
	/**
	 * @return Returns the conceptoCastigoCarteraS3.
	 */
	public String getConceptoCastigoCarteraS3() {
		return conceptoCastigoCarteraS3;
	}
	/**
	 * @param conceptoCastigoCarteraS3 The conceptoCastigoCarteraS3 to set.
	 */
	public void setConceptoCastigoCarteraS3(String conceptoCastigoCarteraS3) {
		this.conceptoCastigoCarteraS3 = conceptoCastigoCarteraS3;
	}
	/**
	 * @return Returns the conceptoAjusteFactura.
	 */
	public String getConceptoAjusteFactura() {
		return conceptoAjusteFactura;
	}
	/**
	 * @param conceptoAjusteFactura The conceptoAjusteFactura to set.
	 */
	public void setConceptoAjusteFactura(String conceptoAjusteFactura) {
		this.conceptoAjusteFactura = conceptoAjusteFactura;
	}
	/**
	 * @return Returns the metodoAjusteFactura.
	 */
	public String getMetodoAjusteFactura() {
		return metodoAjusteFactura;
	}
	/**
	 * @param metodoAjusteFactura The metodoAjusteFactura to set.
	 */
	public void setMetodoAjusteFactura(String metodoAjusteFactura) {
		this.metodoAjusteFactura = metodoAjusteFactura;
	}
	/**
	 * @return Returns the saldoFactura.
	 */
	public double getSaldoFactura() {
		return saldoFactura;
	}
	/**
	 * @param saldoFactura The saldoFactura to set.
	 */
	public void setSaldoFactura(double saldoFactura) 
	{
		this.saldoFactura = saldoFactura;
		String temp=this.saldoFactura+"";
		if(temp.indexOf(".")<0)
		{
			this.saldoFacturaStr=temp;
		}
		else
		{
			this.saldoFacturaStr=temp.substring(0,temp.indexOf("."));
		}
	}
	/**
	 * @return Returns the saldoFacturaStr.
	 */
	public String getSaldoFacturaStr() {
		return saldoFacturaStr;
	}
	/**
	 * @param saldoFacturaStr The saldoFacturaStr to set.
	 */
	public void setSaldoFacturaStr(String saldoFacturaStr) 
	{
		try
		{
			this.saldoFacturaStr = saldoFacturaStr;
			this.saldoFactura=Utilidades.convertirADouble(this.saldoFacturaStr);
		}
		catch(Exception e)
		{
			
		}

	}
	/**
	 * @return Returns the valorAjusteFactura.
	 */
	public double getValorAjusteFactura() {
		return valorAjusteFactura;
	}
	/**
	 * @param valorAjusteFactura The valorAjusteFactura to set.
	 */
	public void setValorAjusteFactura(double valorAjusteFactura) 
	{
		this.valorAjusteFactura = valorAjusteFactura;
		String temp=UtilidadTexto.formatearValores(this.valorAjusteFactura+"","#");
		if(temp.indexOf(".")<0)
		{
			this.valorAjusteFacturaStr=temp;
		}
		else
		{
			this.valorAjusteFacturaStr=temp.substring(0,temp.indexOf("."));
		}
	}
	/**
	 * @return Returns the valorAjusteFacturaStr.
	 */
	public String getValorAjusteFacturaStr() {
		return UtilidadTexto.formatearValores(valorAjusteFacturaStr,"#");
	}
	/**
	 * @param valorAjusteFacturaStr The valorAjusteFacturaStr to set.
	 */
	public void setValorAjusteFacturaStr(String valorAjusteFacturaStr) 
	{
		try
		{
			if(!UtilidadTexto.isEmpty(valorAjusteFacturaStr))
				this.valorAjusteFacturaStr = UtilidadTexto.formatearValores(valorAjusteFacturaStr,"#");
			else
				this.valorAjusteFacturaStr = "";
			this.valorAjusteFactura=Utilidades.convertirADouble(this.valorAjusteFacturaStr);
		}
		catch(Exception e)
		{
			
		}

	}
	/**
	 * @return Returns the estadoCuentaCobro.
	 */
	public int getEstadoCuentaCobro() {
		return estadoCuentaCobro;
	}
	/**
	 * @param estadoCuentaCobro The estadoCuentaCobro to set.
	 */
	public void setEstadoCuentaCobro(int estadoCuentaCobro) {
		this.estadoCuentaCobro = estadoCuentaCobro;
	}
	/**
	 * @return Returns the facturasRelacionada.
	 */
	public String getFacturasRelacionada() {
		return facturasRelacionada;
	}
	/**
	 * @param facturasRelacionada The facturasRelacionada to set.
	 */
	public void setFacturasRelacionada(String facturasRelacionada) {
		this.facturasRelacionada = facturasRelacionada;
	}
	/**
	 * @return Returns the index.
	 */
	public int getIndex() {
		return index;
	}
	/**
	 * @param index The index to set.
	 */
	public void setIndex(int index) {
		this.index = index;
	}
	/**
	 * @return Returns the facturasPopUp.
	 */
	public HashMap getFacturasPopUp() {
		return facturasPopUp;
	}
	/**
	 * @param facturasPopUp The facturasPopUp to set.
	 */
	public void setFacturasPopUp(HashMap facturasPopUp) {
		this.facturasPopUp = facturasPopUp;
	}

	/**
	 * @return Returns the facturasPopUp.
	 */
	public Object getFacturasPopUp(String key) {
		return facturasPopUp.get(key);
	}
	/**
	 * @param facturasPopUp The facturasPopUp to set.
	 */
	public void setFacturasPopUp(String key,Object value) 
	{
		this.facturasPopUp.put(key,value);
	}
	
	/**
	 * @return Returns the numeroFacturasPopUp.
	 */
	public int getNumeroFacturasPopUp() {
		return numeroFacturasPopUp;
	}
	/**
	 * @param numeroFacturasPopUp The numeroFacturasPopUp to set.
	 */
	public void setNumeroFacturasPopUp(int numeroFacturasPopUp) {
		this.numeroFacturasPopUp = numeroFacturasPopUp;
	}
	/**
	 * @return Returns the numeroserviciosFacturasPopUp.
	 */
	public int getNumeroserviciosFacturasPopUp() {
		return numeroserviciosFacturasPopUp;
	}
	/**
	 * @param numeroserviciosFacturasPopUp The numeroserviciosFacturasPopUp to set.
	 */
	public void setNumeroserviciosFacturasPopUp(int numeroserviciosFacturasPopUp) {
		this.numeroserviciosFacturasPopUp = numeroserviciosFacturasPopUp;
	}
	/**
	 * @return Returns the serviciosFacturasPopUp.
	 */
	public HashMap getServiciosFacturasPopUp() {
		return serviciosFacturasPopUp;
	}
	/**
	 * @param serviciosFacturasPopUp The serviciosFacturasPopUp to set.
	 */
	public void setServiciosFacturasPopUp(HashMap serviciosFacturasPopUp) {
		this.serviciosFacturasPopUp = serviciosFacturasPopUp;
	}
	/**
	 * @return Returns the serviciosFacturasPopUp.
	 */
	public Object getServiciosFacturasPopUp(String key) {
		return serviciosFacturasPopUp.get(key);
	}
	/**
	 * @param serviciosFacturasPopUp The serviciosFacturasPopUp to set.
	 */
	public void setServiciosFacturasPopUp(String key,Object value) 
	{
		this.serviciosFacturasPopUp.put(key,value);
	}
	/**
	 * @return Returns the indice.
	 */
	public String getIndice() {
		return indice;
	}
	/**
	 * @param indice The indice to set.
	 */
	public void setIndice(String indice) {
		this.indice = indice;
	}
	/**
	 * @return Returns the ultimoIndice.
	 */
	public String getUltimoIndice() {
		return ultimoIndice;
	}
	/**
	 * @param ultimoIndice The ultimoIndice to set.
	 */
	public void setUltimoIndice(String ultimoIndice) {
		this.ultimoIndice = ultimoIndice;
	}
	/**
	 * @return Returns the indiceServicios.
	 */
	public String getIndiceServicios() {
		return indiceServicios;
	}
	/**
	 * @param indiceServicios The indiceServicios to set.
	 */
	public void setIndiceServicios(String indiceServicios) {
		this.indiceServicios = indiceServicios;
	}
	/**
	 * @return Returns the ultimoIndiceServicios.
	 */
	public String getUltimoIndiceServicios() {
		return ultimoIndiceServicios;
	}
	/**
	 * @param ultimoIndiceServicios The ultimoIndiceServicios to set.
	 */
	public void setUltimoIndiceServicios(String ultimoIndiceServicios) {
		this.ultimoIndiceServicios = ultimoIndiceServicios;
	}
	/**
	 * @return Returns the confirmacionModificacionAjusteFactura.
	 */
	public boolean isConfirmacionModificacionAjusteFactura() {
		return confirmacionModificacionAjusteFactura;
	}
	/**
	 * @param confirmacionModificacionAjusteFactura The confirmacionModificacionAjusteFactura to set.
	 */
	public void setConfirmacionModificacionAjusteFactura(
			boolean confirmacionModificacionAjusteFactura) {
		this.confirmacionModificacionAjusteFactura = confirmacionModificacionAjusteFactura;
	}
	/**
	 * @return Returns the existeModifiacionMetodoAjusteAF.
	 */
	public boolean isExisteModifiacionMetodoAjusteAF() {
		return existeModifiacionMetodoAjusteAF;
	}
	/**
	 * @param existeModifiacionMetodoAjusteAF The existeModifiacionMetodoAjusteAF to set.
	 */
	public void setExisteModifiacionMetodoAjusteAF(
			boolean existeModifiacionMetodoAjusteAF) {
		this.existeModifiacionMetodoAjusteAF = existeModifiacionMetodoAjusteAF;
	}
	/**
	 * @return Returns the existeModifiacionValorAjusteAF.
	 */
	public boolean isExisteModifiacionValorAjusteAF() {
		return existeModifiacionValorAjusteAF;
	}
	/**
	 * @param existeModifiacionValorAjusteAF The existeModifiacionValorAjusteAF to set.
	 */
	public void setExisteModifiacionValorAjusteAF(
			boolean existeModifiacionValorAjusteAF) {
		this.existeModifiacionValorAjusteAF = existeModifiacionValorAjusteAF;
	}
	/**
	 * @return Returns the recalcular.
	 */
	public boolean isRecalcular() {
		return recalcular;
	}
	/**
	 * @param recalcular The recalcular to set.
	 */
	public void setRecalcular(boolean recalcular) {
		this.recalcular = recalcular;
	}
	/**
	 * @return Returns the modificacion.
	 */
	public boolean isModificacion() {
		return modificacion;
	}
	/**
	 * @param modificacion The modificacion to set.
	 */
	public void setModificacion(boolean modificacion) {
		this.modificacion = modificacion;
	}
	/**
	 * @return Returns the facturaCuentaCobroRadicada.
	 */
	public boolean isFacturaCuentaCobroRadicada() {
		return facturaCuentaCobroRadicada;
	}
	/**
	 * @param facturaCuentaCobroRadicada The facturaCuentaCobroRadicada to set.
	 */
	public void setFacturaCuentaCobroRadicada(boolean facturaCuentaCobroRadicada) {
		this.facturaCuentaCobroRadicada = facturaCuentaCobroRadicada;
	}

	
	/**
	 * @return Returns the campoBusqueda.
	 */
	public String getCampoBusqueda() {
		return campoBusqueda;
	}
	/**
	 * @param campoBusqueda The campoBusqueda to set.
	 */
	public void setCampoBusqueda(String campoBusqueda) {
		this.campoBusqueda = campoBusqueda;
	}
	/**
	 * @return Returns the valorCampoBusqueda.
	 */
	public String getValorCampoBusqueda() {
		return valorCampoBusqueda;
	}
	/**
	 * @param valorCampoBusqueda The valorCampoBusqueda to set.
	 */
	public void setValorCampoBusqueda(String valorCampoBusqueda) {
		this.valorCampoBusqueda = valorCampoBusqueda;
	}
	/**
	 * @return Returns the motivoAnulacion.
	 */
	public String getMotivoAnulacion() {
		return motivoAnulacion;
	}
	/**
	 * @param motivoAnulacion The motivoAnulacion to set.
	 */
	public void setMotivoAnulacion(String motivoAnulacion) {
		this.motivoAnulacion = motivoAnulacion;
	}

    /**
     * @return Returns the serviciosAsocios.
     */
    public HashMap getServiciosAsocios()
    {
        return serviciosAsocios;
    }

    /**
     * @param serviciosAsocios The serviciosAsocios to set.
     */
    public void setServiciosAsocios(HashMap serviciosAsocios)
    {
        this.serviciosAsocios = serviciosAsocios;
    }

    /**
     * @return Returns the serviciosAsocios.
     */
    public Object getServiciosAsocios(String key)
    {
        return serviciosAsocios.get(key);
    }

    /**
     * @param serviciosAsocios The serviciosAsocios to set.
     */
    public void setServiciosAsocios(String key,Object value)
    {
        this.serviciosAsocios.put(key, value);
    }

    /**
     * @return Returns the totalFactura.
     */
    public double getTotalFactura()
    {
        return totalFactura;
    }

    /**
     * @param totalFactura The totalFactura to set.
     */
    public void setTotalFactura(double totalFactura)
    {
        this.totalFactura = totalFactura;
    }

    /**
     * @return Returns the serviciosAsociosPopUp.
     */
    public HashMap getServiciosAsociosPopUp()
    {
        return serviciosAsociosPopUp;
    }

    /**
     * @param serviciosAsociosPopUp The serviciosAsociosPopUp to set.
     */
    public void setServiciosAsociosPopUp(HashMap serviciosAsociosPopUp)
    {
        this.serviciosAsociosPopUp = serviciosAsociosPopUp;
    }

    /**
     * @return Returns the serviciosAsociosPopUp.
     */
    public Object getServiciosAsociosPopUp(String key)
    {
        return serviciosAsociosPopUp.get(key);
    }

    /**
     * @param serviciosAsociosPopUp The serviciosAsociosPopUp to set.
     */
    public void setServiciosAsociosPopUp(String key,Object value)
    {
        this.serviciosAsociosPopUp.put(key, value);
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

	public HashMap getFacturasMismoConsecutivo() {
		return facturasMismoConsecutivo;
	}

	public void setFacturasMismoConsecutivo(HashMap facturasMismoConsecutivo) {
		this.facturasMismoConsecutivo = facturasMismoConsecutivo;
	}

	public Object getFacturasMismoConsecutivo(String key) {
		return facturasMismoConsecutivo.get(key);
	}

	public void setFacturasMismoConsecutivo(String key,Object value) {
		this.facturasMismoConsecutivo.put(key, value);
	}

	public boolean isMostrarPopUpFacturasMismoConsecutivo() {
		return mostrarPopUpFacturasMismoConsecutivo;
	}

	public void setMostrarPopUpFacturasMismoConsecutivo(
			boolean mostrarPopUpFacturasMismoConsecutivo) {
		this.mostrarPopUpFacturasMismoConsecutivo = mostrarPopUpFacturasMismoConsecutivo;
	}

	public int getIndiceConsecutivoCargar() {
		return indiceConsecutivoCargar;
	}

	public void setIndiceConsecutivoCargar(int indiceConsecutivoCargar) {
		this.indiceConsecutivoCargar = indiceConsecutivoCargar;
	}

	public String getNombreEntidadFactura() {
		return nombreEntidadFactura;
	}

	public void setNombreEntidadFactura(String nombreEntidadFactura) {
		this.nombreEntidadFactura = nombreEntidadFactura;
	}
}
