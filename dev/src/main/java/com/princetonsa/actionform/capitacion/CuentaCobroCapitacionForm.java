/*
 * Creado en Jun 13, 2006
 * Andrés Mauricio Ruiz Vélez
 * Princeton S.A (Parquesoft - Manizales)
 */
package com.princetonsa.actionform.capitacion;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import com.princetonsa.dto.administracion.DtoFirmasValoresPorDefecto;

import util.ConstantesBD;
import util.UtilidadCadena;
import util.UtilidadFecha;

public class CuentaCobroCapitacionForm extends ValidatorForm
{
	/**
	 * Serial version UID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Estado para el manejo del flujo de la funcionalidad
	 */
	private String estado;
	
	//	---------------------------------------------------DECLARACIÓN DE LOS ATRIBUTOS-----------------------------------------------------------//
	/**
	 * Campo que contiene la fecha inicial para realizar la búsqueda
	 * de los cargos
	 */
	private String fechaInicialGeneracion;
	
	/**
	 * Campo que contiene la fecha final para realizar la búsqueda
	 * de los cargos
	 */
	private String fechaFinalGeneracion;
	
	/**
	 * Campo que guarda las observaciones de la cuenta de cobro
	 */
	private String observacionesCuentaCobro;
	
	  /**
     * Mapa que contiene los cargues consultados de acuerdo a los parámetros
     * de busqueda    
     */
    private HashMap mapaCargues;
    
    /**
     * 
     */
    private HashMap mapaMensajeError;
    
    /**
     * Campo que guarda el convenio que se selecciona en la búsqueda
     * de cuenta de cobro por convenio
     */
    private int convenioCapitado;
    
    /**
     * Mapa que contiene las cuenta de cobro por convenio que se muestran
     * al generar la cuenta de cobro
     */
    private HashMap mapaCuentasCobroConvenio;
    
    /**
     * Campo que contiene el nombre del convenio seleccionado, en el detalle
     */
    private String nombreConvenio;
    
    /**
     * Campo que contiene el número de la cuenta de cobro seleccionado, en el detalle
     */
    private int numeroCuentaCobro;
    
    /**
     * Campo que guarda el valor total de cuenta de cobro seleccionado, en el detalle
     */
    private String totalCuentaCobro;
    
    /**
     * Mapa que contiene los contratos asociados a la cuenta de cobro, en el detalle
     */
    private HashMap mapaDetalleCuentaCobro;
    
    /**
     * Campo que guarda la cuenta de cobro digitada para buscar en el modificar, consultar
     */
    private String cuentaCobroBuscar;
    
    /**
     * Campo que guarda la cuenta de cobro final en la busqueda avanzada de la consulta
     */
    private String cuentaCobroFinal;
    
    /**
     * Campo que guarda el nombre del estado de la cuenta de cobro en el modificar, consultar
     */
    private String nombreEstadoCxCModificar;
    
    /**
     * Campo que guarda el código del estado de la cuenta de cobro en el modificar,consultar
     */
    private int codigoEstadoModificar;
    
    /**
     * Campo que guarda el nombre del estado de la cuenta de cobro en el modificar,consultar
     */
    private String nombreConvenioModificar;
    
    /**
     * Campo que guarda el código del convenio de la cuenta de cobro a modificar,consultar
     */
    private int codigoConvenioModificar;
    
    /**
     * Campo que guarda la fecha de elaboración en el modificar,consultar
     */
    private String fechaElaboracionModificar;
    
    /**
     * Campo que guarda la fecha inicial en el modificar,consultar
     */
    private String fechaInicialModificar;
    
    /**
     * Campo que guarda la fecha inicial anterior en el modificar
     */
    private String fechaInicialModAnterior;
    
    /**
     * Campo que guarda la fecha final en el modificar,consultar
     */
    private String fechaFinalModificar;
    
    /**
     * Campo que guarda la fecha final anterior en el modificar
     */
    private String fechaFinalModAnterior;
    
    /**
     * Campo que guarda las observaciones de la cuenta de cobro  en el modificar,consultar
     */
    private String observacionesModificar;
    
    /**
     * Campo que guarda las observaciones anteriores de la cuenta de cobro  en el modificar
     */
    private String observacionesModAnterior;
    
    /**
     * Collection que guarda la información de la cuenta de cobro consultada
     */
    private HashMap mapaInfoCuentaCobro;
    
    /**
     * Campo que guarda el estado seleccionado en la bùsqueda avanzada
     */
    private int estadoCuentaCobroBuscar;
    
    /**
     * Campo que guarda el motivo de anulación
     */
    private String motivoAnulacion;
    
    /**
     * Origen de la generación de la cuenta de cobro, para saber
     * que resumen se debe mostrar
     */
    private String origenGeneracionCuentaCobro;
    
    /**
     * Mapa que contiene las facturas asociadas a la cuenta de cobro
     */
    private HashMap mapaFacturasCxC;
    
    /**
     * Campo que guarda el número de la factura a buscar
     */
    private String numeroFacturaBuscar;
    
    /**
     * Campo que guarda la fecha de la factura a buscar
     */
    private String fechaFacturaBuscar;
    
    /**
     * Campo que guarda la vía de ingreso de facturas a buscar
     */
    private int viaIngresoBuscar;
    
    /**
     * Campo que guarda el valor de pagos de la cuenta de cobro
     */
    private String pagosCuentaCobro;
    
    /**
     * Campo que guarda el valor total de la cuenta de cobro
     */
    private String valorTotalCuentaCobro;
    
    /**
     * Campo que guarda el valor del ajsute debito de la cuenta de cobro
     */
    private String valorAjusteDebito;
    
    /**
     * Campo que guarda el valor del ajuste credito de la cuenta de cobro
     */
    private String valorAjusteCredito;
    
    /**
     * Campo que guarda el saldo total de la cuenta de cobro
     */
    private String saldoTotalCuentaCobro;
    
    private String prefijoFactura;

    
//  --------------------Pager del listado de Cargues ------------------------------//
    /**
     * Número de registros por pager
     */
    private int maxPageItems;
    
    /**
     * Variables para manejar el paginador de la consulta de pacientes del centro de costo
     */
    private int index;
	private String linkSiguiente;
	
	/**
     * Almacena el indice por el cual 
     * se va ordenar el HashMap
     */
    private String patronOrdenar;
    
    /**
     * Almacena el ultimo indice por el 
     * cual se ordeno el HashMap
     */
    private String ultimoPatron;
	

    /**
     * Mapa para almacenar el encabezado de la impresion de cuentas de cobro.
     */
    private HashMap mapaEncImpresion;

    /**
     * Mapa para almacenar el detalle de la impresion de cuentas de cobro.
     */
    private HashMap mapaDetImpresion;
    
    /**
     * Mapa para almacenar el detalle de los cargues de tipo grupo etáreo
     */
    private HashMap mapaCarguesGrupoEtareo;
    
    //---------------Para mostrar en el encabezado de la impresión -------------------------//
    /**
     * Campo que guarda el nit del convenio para mostrar en la impresion
     */
    private String nitConvenio;
    
    /**
     * Campo que guarda la fecha de radicación de la cuenta de cobro
     * para mostrarse en la impresión
     */
    private String fechaRadicacion;
    
    /**
     * Campo que guarda el número de radicación de la cuenta de cobro
     * para mostrarse en la impresión
     */
    private String numeroRadicacion;
    
    /**
     * Campo que guarda la direccion del convenio
     */
    private String direccionConvenio;
    
    /**
     * Campo que guarda el telefono del convenio
     */
    private String telefonoConvenio;
    
    /**
     * Campo para indicar si la cuenta de cobro es normal o de saldos
     * iniciales, se utiliza en el modificar  1=Normal , 2= Saldos Iniciales
     */
    private int tipoCuentaCobro;
    
    
    //Anexo 922
    /**
     * Campo para guardar la hora
     */
    private String horaElaboracion;
    
    /**
     * ArrayList para guardar las firmas
     */
    private ArrayList<DtoFirmasValoresPorDefecto> listadoFirmas;
	
//	---------------------------------------------------FIN DECLARACIÓN DE LOS ATRIBUTOS-----------------------------------------------------------//
	
	/**
	 * Método para limpiar los atributos de la clase
	 */
	public void reset()
	{
		//-----------Generación cuenta de cobro ------------------//
		this.origenGeneracionCuentaCobro="";
		this.fechaInicialGeneracion="";
		this.fechaFinalGeneracion="";
		this.observacionesCuentaCobro="";
		this.mapaCargues=new HashMap();
		this.mapaCuentasCobroConvenio=new HashMap();
		this.nombreConvenio="";
		this.numeroCuentaCobro=0;
		this.totalCuentaCobro="0";
		this.pagosCuentaCobro="0";
		this.valorTotalCuentaCobro="0";
		this.valorAjusteDebito = "";
		this.valorAjusteCredito = "";
		this.saldoTotalCuentaCobro="0";
		this.mapaDetalleCuentaCobro=new HashMap();
		
		this.convenioCapitado=-1;
		this.maxPageItems=0;
		this.patronOrdenar="";
    	this.ultimoPatron="";
		
		//-----------Modificación Cuenta de Cobro --------------------//
		this.cuentaCobroBuscar="";
		this.cuentaCobroFinal="";
		this.nombreEstadoCxCModificar="";
		this.codigoEstadoModificar=0;
		this.nombreConvenioModificar="";
		this.codigoConvenioModificar=-1;
		this.fechaElaboracionModificar="";
		this.fechaInicialModificar="";
		this.fechaInicialModAnterior="";
		this.fechaFinalModificar="";
		this.fechaFinalModAnterior="";
		this.observacionesModificar="";
		this.observacionesModAnterior="";
		this.mapaInfoCuentaCobro=new HashMap();
		this.motivoAnulacion="";
		
		this.mapaFacturasCxC=new HashMap();
		this.numeroFacturaBuscar="";
		this.fechaFacturaBuscar="";
		this.viaIngresoBuscar=-1;
		
		//----------------Búsqueda avanzada -----------------------//
		this.estadoCuentaCobroBuscar=-1;
		
		this.nitConvenio="";
		this.fechaRadicacion="";
		this.numeroRadicacion = "";
		this.direccionConvenio = "";
		this.telefonoConvenio = "";
		
		this.tipoCuentaCobro=ConstantesBD.codigoNuncaValido;
		
		this.mapaMensajeError= new HashMap();
		this.mapaMensajeError.put("numRegistros", "0");
		
		//Anexo 992
		this.horaElaboracion="";
		this.listadoFirmas=new ArrayList<DtoFirmasValoresPorDefecto>();
		
		this.prefijoFactura="";
		
	}
	
	/**
	 * Método para limpiar los atributos de la clase
	 */
	public void resetBusquedaFacturas()
	{
		this.mapaFacturasCxC=new HashMap();
		this.numeroFacturaBuscar="";
		this.fechaFacturaBuscar="";
		this.viaIngresoBuscar=-1;
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
	 @SuppressWarnings("unused")
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores= new ActionErrors();
		//-----Si el estado es busquedaCarguesPeriodo --//
		if(estado.equals("busquedaCarguesPeriodo"))
		{
			boolean errorFechaInicial=false, errorFechaFinal=false;
			//----------Validación de la fecha inicial-------------//
			if (!UtilidadCadena.noEsVacio(fechaInicialGeneracion))
				{
				errorFechaInicial=true;
				errores.add("Fecha Inicial vacio", new ActionMessage("errors.required","La Fecha Inicial"));
				}
			else if (!UtilidadFecha.validarFecha(fechaInicialGeneracion))
				{
				errorFechaInicial=true;
				errores.add("Fecha Inicial Invalido", new ActionMessage("errors.formatoFechaInvalido", " Inicial"));
				}
			else
				{
				//---- Validar que la fecha inicial sea menor o igual a la fecha del sistema-----//
				if((UtilidadFecha.conversionFormatoFechaABD(fechaInicialGeneracion)).compareTo(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()))>0)
					{
						errores.add("fechaInicial", new ActionMessage("errors.fechaPosteriorIgualAOtraDeReferencia", "Inicial", "actual"));
					}
				}
			
			//----------Validación de la fecha final-------------//
			if (!UtilidadCadena.noEsVacio(fechaFinalGeneracion))
				{
				errorFechaFinal=true;
				errores.add("Fecha Final vacio", new ActionMessage("errors.required","La Fecha Final"));
				}
			else if (!UtilidadFecha.validarFecha(fechaFinalGeneracion))
				{
				errorFechaFinal=true;
				errores.add("Fecha Final Invalido", new ActionMessage("errors.formatoFechaInvalido", " Final"));
				}
			else if(!errorFechaInicial)
				{
				//---- Validar que la fecha final sea mayor a la fecha inicial-----//
				if((UtilidadFecha.conversionFormatoFechaABD(fechaFinalGeneracion)).compareTo(UtilidadFecha.conversionFormatoFechaABD(fechaInicialGeneracion))<=0)
					{
						errores.add("fechaFinal", new ActionMessage("errors.fechaAnteriorAOtraDeReferencia", "Final", "Inicial"));
					}
				else
					{
						/*
						 * tarea xplanner 3  Francisco - Abr2007 - Validación generar cuentas para mas de un mes [id=7155]
						 * 
						if (UtilidadFecha.numeroDiasEntreFechas(fechaInicialGeneracion, fechaFinalGeneracion)>30)
						{
							errores.add("fechaFinalMayorUnMes", new ActionMessage("error.capitacion.cuentaCobroCapitacion.fechaFinalMayorUnMesFechaInicial"));
						}
						*/
						
					}
				}
		}//if estado=busquedaCarguesPeriodo
		
		//-----Si el estado es busquedaCarguesConvenio --//
		if(estado.equals("busquedaCarguesConvenio"))
		{
			//---------------------------Se realizan las validaciones para pode realizar la busqueda ----------------------------------//
			boolean errorFechaInicial=false, errorFechaFinal=false;
			
			//--------Se valida que se haya seleccionado algún convenio ---------------------------//
			if (convenioCapitado == -1)
				{
				errores.add("Convenio vacio", new ActionMessage("errors.required","El Convenio"));
				}
			
			//----------Validación de la fecha inicial-------------//
			if (!UtilidadCadena.noEsVacio(fechaInicialGeneracion))
				{
				errorFechaInicial=true;
				errores.add("Fecha Inicial vacio", new ActionMessage("errors.required","La Fecha Inicial"));
				}
			else if (!UtilidadFecha.validarFecha(fechaInicialGeneracion))
				{
				errorFechaInicial=true;
				errores.add("Fecha Inicial Invalido", new ActionMessage("errors.formatoFechaInvalido", " Inicial"));
				}
			else
				{
				//---- Validar que la fecha inicial sea menor o igual a la fecha del sistema-----//
				if((UtilidadFecha.conversionFormatoFechaABD(fechaInicialGeneracion)).compareTo(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()))>0)
					{
						errores.add("fechaInicial", new ActionMessage("errors.fechaPosteriorAOtraDeReferencia", "Inicial", "actual"));
					}
				}
			
			//----------Validación de la fecha final-------------//
			if (!UtilidadCadena.noEsVacio(fechaFinalGeneracion))
				{
				errorFechaFinal=true;
				errores.add("Fecha Final vacio", new ActionMessage("errors.required","La Fecha Final"));
				}
			else if (!UtilidadFecha.validarFecha(fechaFinalGeneracion))
				{
				errorFechaFinal=true;
				errores.add("Fecha Final Invalido", new ActionMessage("errors.formatoFechaInvalido", " Final"));
				}
			else if(!errorFechaInicial)
				{
				//---- Validar que la fecha final sea mayor a la fecha inicial-----//
				if((UtilidadFecha.conversionFormatoFechaABD(fechaFinalGeneracion)).compareTo(UtilidadFecha.conversionFormatoFechaABD(fechaInicialGeneracion))<=0)
					{
						errores.add("fechaFinal", new ActionMessage("errors.fechaAnteriorAOtraDeReferencia", "Final", "Inicial"));
					}
				else
					{
						/*
						 * 142143
						if (UtilidadFecha.numeroDiasEntreFechas(fechaInicialGeneracion, fechaFinalGeneracion)>31)
						{
							errores.add("fechaFinalMayorUnMes", new ActionMessage("error.capitacion.cuentaCobroCapitacion.fechaFinalMayorUnMesFechaInicial"));
						}
						*/
						
					}
				}
		}//if estado
		
		//-----Si el estado es busquedaCxCModificar --//
		if(estado.equals("busquedaCxCModificar"))
		{
			//------------Se verifica que no esté vacío el nùmero de la cuenta de cobro que se desea buscar---------//
			if (!UtilidadCadena.noEsVacio(this.cuentaCobroBuscar))
				{
				errores.add("Cuenta Cobro vacio", new ActionMessage("errors.required","La Cuenta por Cobrar"));
				this.setEstado("empezarModificarCxC");
				}
		}
		
		//-----------------------------Si el estado es continuarModificarCxC --------------------------------------------//
		if(estado.equals("continuarModificarCxC"))
		{
			boolean errorFechaInicial=false, errorFechaFinal=false;
			
			//----------Si la cuanta de cobro es de tipo normal -----------------//
			if(this.tipoCuentaCobro==ConstantesBD.codigoTipoCuentaCobroCapitacionNormal)
			{
			//----------Validación de la fecha inicial-------------//
			if (!UtilidadCadena.noEsVacio(fechaInicialModificar))
				{
				errorFechaInicial=true;
				errores.add("Fecha Inicial vacio", new ActionMessage("errors.required","La Fecha Inicial"));
				}
			else if (!UtilidadFecha.validarFecha(fechaInicialModificar))
				{
				errorFechaInicial=true;
				errores.add("Fecha Inicial Invalido", new ActionMessage("errors.formatoFechaInvalido", " Inicial"));
				}
			else
				{
				//---- Validar que la fecha inicial sea menor a la fecha del sistema-----//
				if((UtilidadFecha.conversionFormatoFechaABD(this.fechaInicialModificar)).compareTo(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()))>0)
					{
						errores.add("fechaIniciaMayorIgualActual", new ActionMessage("errors.fechaPosteriorIgualActual", "Inicial", "Actual"));
					}
				}
			
			//----------Validación de la fecha final-------------//
			if (!UtilidadCadena.noEsVacio(fechaFinalModificar))
				{
				errorFechaFinal=true;
				errores.add("Fecha Final vacio", new ActionMessage("errors.required","La Fecha Final"));
				}
			else if (!UtilidadFecha.validarFecha(fechaFinalModificar))
				{
				errorFechaFinal=true;
				errores.add("Fecha Final Invalido", new ActionMessage("errors.formatoFechaInvalido", " Final"));
				}
			else if(!errorFechaInicial)
				{
				//---- Validar que la fecha final sea mayor a la fecha inicial-----//
				if((UtilidadFecha.conversionFormatoFechaABD(fechaFinalModificar)).compareTo(UtilidadFecha.conversionFormatoFechaABD(fechaInicialModificar))<=0)
					{
						errores.add("fechaFinal", new ActionMessage("errors.fechaAnteriorAOtraDeReferencia", "Final", "Inicial"));
					}
				else
					{
						//-----------Validar que la fecha final no sea mayor a un mes con respecto a la fecha inicial----//
					/*
					 * tarea 142143
						if (UtilidadFecha.numeroDiasEntreFechas(fechaInicialModificar, fechaFinalModificar)>31)
						{
							errores.add("fechaFinalMayorUnMes", new ActionMessage("error.capitacion.cuentaCobroCapitacion.fechaFinalMayorUnMesFechaInicial"));
						}
						*/
					}
				}
			}//if la cuentaCobro es normal
	
		else if(this.tipoCuentaCobro==ConstantesBD.codigoTipoCuentaCobroCapitacionSaldosIniciales)
			{
			//----------Validación de la fecha inicial-------------//
			if (!UtilidadCadena.noEsVacio(fechaInicialModificar))
				{
				errorFechaInicial=true;
				errores.add("Fecha Inicial vacio", new ActionMessage("errors.required","La Fecha Inicial"));
				}
			else if (!UtilidadFecha.validarFecha(fechaInicialModificar))
				{
				errorFechaInicial=true;
				errores.add("Fecha Inicial Invalido", new ActionMessage("errors.formatoFechaInvalido", " Inicial"));
				}
			else
				{
				//---- Validar que la fecha inicial sea menor a la fecha del sistema-----//
				if((UtilidadFecha.conversionFormatoFechaABD(fechaInicialModificar)).compareTo(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()))>0)
					{
						errores.add("fechaInicialMayorIgualActual", new ActionMessage("errors.fechaPosteriorIgualActual", "Inicial", "Actual"));
					}
				//---- Validar que la fecha inicial sea mayor o igual a la fecha de elaboración de la cuenta de cobro-----//
				if((UtilidadFecha.conversionFormatoFechaABD(fechaInicialModificar)).compareTo(UtilidadFecha.conversionFormatoFechaABD(this.fechaElaboracionModificar))<0)
					{
						errores.add("fechaInicialMenorElaboracion", new ActionMessage("errors.fechaAnteriorIgualAOtraDeReferencia", "Inicial", "de Elaboración"));
					}
				}
			
			//----------Validación de la fecha final-------------//
			if (!UtilidadCadena.noEsVacio(fechaFinalModificar))
				{
				errorFechaFinal=true;
				errores.add("Fecha Final vacio", new ActionMessage("errors.required","La Fecha Final"));
				}
			else if (!UtilidadFecha.validarFecha(fechaFinalModificar))
				{
				errorFechaFinal=true;
				errores.add("Fecha Final Invalido", new ActionMessage("errors.formatoFechaInvalido", " Final"));
				}
			else if(!errorFechaInicial)
				{
				//---- Validar que la fecha final sea menor a la fecha del sistema-----//
				if((UtilidadFecha.conversionFormatoFechaABD(fechaFinalModificar)).compareTo(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()))>=0)
					{
						errores.add("fechaFinalMayorIgualSistema", new ActionMessage("errors.fechaPosteriorAOtraDeReferencia", "Final", "Actual"));
					}
				
				//---- Validar que la fecha final sea mayor o igual a la fecha inicial-----//
				if((UtilidadFecha.conversionFormatoFechaABD(fechaFinalModificar)).compareTo(UtilidadFecha.conversionFormatoFechaABD(fechaInicialModificar))<0)
					{
						errores.add("fechaFinalMenorInicial", new ActionMessage("errors.fechaAnteriorIgualAOtraDeReferencia", "Final", "Inicial"));
					}
				
				/*Se quita la siguiente validación por la Tarea 64937
				//---- Validar que la fecha final sea igual a la fecha de elaboración de la cuenta de cobro-----//
				if(!this.fechaFinalModificar.equals(this.fechaElaboracionModificar))
					{
						errores.add("fechaFinaDiferenteElaboracion", new ActionMessage("errors.fechaDiferenteAOtraDeReferencia", "Final", "de Elaboración"));
					}
				*/
				}
			}//if cuentaCobro saldos iniciales
			
		}//if estado=continuarModificarCxC
		
		if(estado.equals("anularCuentaCobro"))
		{
			if(this.motivoAnulacion.length()>512)
			{
				errores.add("errors.maxlength", new ActionMessage("errors.maxlength", "Motivo Anulacion","512"));
			}
		}
		//----------------------------Si el estado es busquedaAvanzadaConsultar cuando le dan buscar en la busqueda avanzada -------------------------------//
		if (estado.equals("busquedaAvanzadaConsultar"))
		{
			boolean errorFechaInicial=false, errorFechaFinal=false;
			//---------------------Se realiza la validación de los parámetros de búsqueda avanzada ----------------------------------------//
			if (!UtilidadCadena.noEsVacio(this.cuentaCobroBuscar) && !UtilidadCadena.noEsVacio(this.cuentaCobroFinal) && this.convenioCapitado==-1 && !UtilidadCadena.noEsVacio(this.fechaElaboracionModificar) && this.estadoCuentaCobroBuscar==-1 && !UtilidadCadena.noEsVacio(this.fechaInicialModificar) && !UtilidadCadena.noEsVacio(this.fechaFinalModificar))
			{
				errores.add("noHayParametros", new ActionMessage("errors.requridoMinimoUnParametroParaEjecutarConsulta", "UN"));
			}
			else
			{
				//------------Validación de la cuenta de cobro inicial ----------------//
				if (UtilidadCadena.noEsVacio(this.cuentaCobroBuscar))
				{
					//-----------Se verifica si está vacío la cuenta de cobro final ---------------//
					if (!UtilidadCadena.noEsVacio(this.cuentaCobroFinal))
					{
						errores.add("Cuenta Cobro Final vacio", new ActionMessage("errors.required","La Cuenta de Cobro Final"));
					}
				}
				
				//------------Validación de la cuenta de cobro final ----------------//
				if (UtilidadCadena.noEsVacio(this.cuentaCobroFinal))
				{
					//-----------Se verifica si está vacío la cuenta de cobro final ---------------//
					if (!UtilidadCadena.noEsVacio(this.cuentaCobroBuscar))
					{
						errores.add("Cuenta Cobro Inicial vacio", new ActionMessage("errors.required","La Cuenta de Cobro Inicial"));
					}
				}
				
				//---------Si no hay errores en las cuentas de cobro inicial y finall ----------//
				if (UtilidadCadena.noEsVacio(this.cuentaCobroBuscar) && UtilidadCadena.noEsVacio(this.cuentaCobroFinal))
					{
						//---Se verifica que la cuenta de cobro inicial sea menor o igual a la cuenta de cobro final ----------//
						if(Integer.parseInt(this.cuentaCobroBuscar)>Integer.parseInt(this.cuentaCobroFinal))
						{
							errores.add("CxC Inicial Mayor Final", new ActionMessage("error.capitacion.cuentaCobroCapitacion.cuentaCobroInicialMayorFinal"));
						}
					}
				
				//---------Validación de la fecha de elaboración-----------------//
				if (UtilidadCadena.noEsVacio(this.fechaElaboracionModificar))
				{
					//---------Se verifica el formato de la fecha de elaboración---------------//
					if (!UtilidadFecha.validarFecha(this.fechaElaboracionModificar))
					{
					errores.add("Fecha Elaboracion Invalido", new ActionMessage("errors.formatoFechaInvalido", " Elaboración"));
					}
					//--------------Se verifica que la fecha de elaboración sea menor o igual a la fecha del sistema -----------//
					else if((UtilidadFecha.conversionFormatoFechaABD(this.fechaElaboracionModificar)).compareTo(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()))>0)
								{
									errores.add("fechaElaboracion", new ActionMessage("errors.fechaPosteriorIgualAOtraDeReferencia", "Elaboración", "actual"));
								}
				}
				
				//------------------Validación de la fecha Inicial -------------------//
				if (UtilidadCadena.noEsVacio(this.fechaInicialModificar))
				{
					//---------Se verifica el formato de la fecha inicial---------------//
					if (!UtilidadFecha.validarFecha(this.fechaInicialModificar))
					{
					errorFechaInicial=true;
					errores.add("Fecha Inicial Invalido", new ActionMessage("errors.formatoFechaInvalido", " Inicial"));
					}
					//--------------Se verifica que la fecha inicial sea menor a la fecha del sistema -----------//
					else if((UtilidadFecha.conversionFormatoFechaABD(this.fechaInicialModificar)).compareTo(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()))>0)
					{
						errores.add("fechaInicial", new ActionMessage("errors.fechaPosteriorIgualActual", "Inicial", "actual"));
					}
					//------------------Validación que sea requerida la fecha final -------------------//
					if (!UtilidadCadena.noEsVacio(this.fechaFinalModificar))
					{
						errorFechaFinal=true;
						errores.add("Fecha Final vacio", new ActionMessage("errors.required","La Fecha Final"));
					}
				}//if fechaInicial no es vacío
				
				//------------------Validación de la fecha final -------------------//
				if (UtilidadCadena.noEsVacio(this.fechaFinalModificar))
				{
					//---------Se verifica el formato de la fecha final---------------//
					if (!UtilidadFecha.validarFecha(this.fechaFinalModificar))
					{
					errorFechaFinal=true;
					errores.add("Fecha Final Invalido", new ActionMessage("errors.formatoFechaInvalido", " Final"));
					}
					//------------------Validación que sea requerida la fecha inicial -------------------//
					if (!UtilidadCadena.noEsVacio(this.fechaInicialModificar))
					{
						errorFechaInicial=true;
						errores.add("Fecha Inicial vacio", new ActionMessage("errors.required","La Fecha Inicial"));
					}
					
					//---------Se verifica que la fecha final sea mayor a la fecha inicial ----------//
					if (!errorFechaInicial && !errorFechaFinal)
						{
						//--------------Se verifica que la fecha final sea menor o igual a la fecha del sistema -----------//
						if((UtilidadFecha.conversionFormatoFechaABD(this.fechaInicialModificar)).compareTo(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()))>0)
						{
							errores.add("fechaFinal", new ActionMessage("errors.fechaPosteriorIgualActual", "Final", "actual"));
						}
						
						//---- Validar que la fecha final sea mayor a la fecha inicial-----//
						if((UtilidadFecha.conversionFormatoFechaABD(this.fechaFinalModificar)).compareTo(UtilidadFecha.conversionFormatoFechaABD(this.fechaInicialModificar))<=0)
							{
								errores.add("fechaFinal", new ActionMessage("errors.fechaAnteriorAOtraDeReferencia", "Final", "Inicial"));
							}
						}
				}//if fechaFinal no es vacío
			}
		}
		
		return errores;
		
	}
	
//	-------------------------------------------------------------SETS Y GETS----------------------------------------------------------------//

	/**
	 * @return Retorna the estado.
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
	 * @return Retorna the fechaFinalGeneracion.
	 */
	public String getFechaFinalGeneracion()
	{
		return fechaFinalGeneracion;
	}

	/**
	 * @param fechaFinalGeneracion The fechaFinalGeneracion to set.
	 */
	public void setFechaFinalGeneracion(String fechaFinalGeneracion)
	{
		this.fechaFinalGeneracion = fechaFinalGeneracion;
	}

	/**
	 * @return Retorna the fechaInicialGeneracion.
	 */
	public String getFechaInicialGeneracion()
	{
		return fechaInicialGeneracion;
	}

	/**
	 * @param fechaInicialGeneracion The fechaInicialGeneracion to set.
	 */
	public void setFechaInicialGeneracion(String fechaInicialGeneracion)
	{
		this.fechaInicialGeneracion = fechaInicialGeneracion;
	}

	/**
	 * @return Retorna the observacionesCuentaCobro.
	 */
	public String getObservacionesCuentaCobro()
	{
		return observacionesCuentaCobro;
	}

	/**
	 * @param observacionesCuentaCobro The observacionesCuentaCobro to set.
	 */
	public void setObservacionesCuentaCobro(String observacionesCuentaCobro)
	{
		this.observacionesCuentaCobro = observacionesCuentaCobro;
	}

	/**
	 * @return Retorna the mapaCargues.
	 */
	public HashMap getMapaCargues()
	{
		return mapaCargues;
	}

	/**
	 * @param mapaCargues The mapaCargues to set.
	 */
	public void setMapaCargues(HashMap mapaCargues)
	{
		this.mapaCargues = mapaCargues;
	}
	
	/**
	 * @return Retorna mapaCargues.
	 */
	public Object getMapaCargues(Object key) {
		return mapaCargues.get(key);
	}
	/**
	 * @param Asigna dato.
	 */
	public void setMapaCargues(Object key, Object dato) {
		this.mapaCargues.put(key, dato);
	}

	/**
	 * @return Retorna the convenioCapitado.
	 */
	public int getConvenioCapitado()
	{
		return convenioCapitado;
	}

	/**
	 * @param convenioCapitado The convenioCapitado to set.
	 */
	public void setConvenioCapitado(int convenioCapitado)
	{
		this.convenioCapitado = convenioCapitado;
	}

	/**
	 * @return Retorna the index.
	 */
	public int getIndex()
	{
		return index;
	}

	/**
	 * @param index The index to set.
	 */
	public void setIndex(int index)
	{
		this.index = index;
	}

	/**
	 * @return Retorna the linkSiguiente.
	 */
	public String getLinkSiguiente()
	{
		return linkSiguiente;
	}

	/**
	 * @param linkSiguiente The linkSiguiente to set.
	 */
	public void setLinkSiguiente(String linkSiguiente)
	{
		this.linkSiguiente = linkSiguiente;
	}

	/**
	 * @return Retorna the maxPageItems.
	 */
	public int getMaxPageItems()
	{
		return maxPageItems;
	}

	/**
	 * @param maxPageItems The maxPageItems to set.
	 */
	public void setMaxPageItems(int maxPageItems)
	{
		this.maxPageItems = maxPageItems;
	}

	/**
	 * @return Retorna the mapaCuentasCobroConvenio.
	 */
	public HashMap getMapaCuentasCobroConvenio()
	{
		return mapaCuentasCobroConvenio;
	}

	/**
	 * @param mapaCuentasCobroConvenio The mapaCuentasCobroConvenio to set.
	 */
	public void setMapaCuentasCobroConvenio(HashMap mapaCuentasCobroConvenio)
	{
		this.mapaCuentasCobroConvenio = mapaCuentasCobroConvenio;
	}
	
	/**
	 * @return Retorna mapaCuentasCobroConvenio.
	 */
	public Object getMapaCuentasCobroConvenio(Object key) {
		return mapaCuentasCobroConvenio.get(key);
	}
	/**
	 * @param Asigna dato.
	 */
	public void setMapaCuentasCobroConvenio(Object key, Object dato) {
		this.mapaCuentasCobroConvenio.put(key, dato);
	}

	/**
	 * @return Retorna the nombreConvenio.
	 */
	public String getNombreConvenio()
	{
		return nombreConvenio;
	}

	/**
	 * @param nombreConvenio The nombreConvenio to set.
	 */
	public void setNombreConvenio(String nombreConvenio)
	{
		this.nombreConvenio = nombreConvenio;
	}

	/**
	 * @return Retorna the numeroCuentaCobro.
	 */
	public int getNumeroCuentaCobro()
	{
		return numeroCuentaCobro;
	}

	/**
	 * @param numeroCuentaCobro The numeroCuentaCobro to set.
	 */
	public void setNumeroCuentaCobro(int numeroCuentaCobro)
	{
		this.numeroCuentaCobro = numeroCuentaCobro;
	}

	/**
	 * @return Retorna the totalCuentaCobro.
	 */
	public String getTotalCuentaCobro()
	{
		return totalCuentaCobro;
	}

	/**
	 * @param totalCuentaCobro The totalCuentaCobro to set.
	 */
	public void setTotalCuentaCobro(String totalCuentaCobro)
	{
		this.totalCuentaCobro = totalCuentaCobro;
	}

	/**
	 * @return Retorna the mapaDetalleCuentaCobro.
	 */
	public HashMap getMapaDetalleCuentaCobro()
	{
		return mapaDetalleCuentaCobro;
	}

	/**
	 * @param mapaDetalleCuentaCobro The mapaDetalleCuentaCobro to set.
	 */
	public void setMapaDetalleCuentaCobro(HashMap mapaDetalleCuentaCobro)
	{
		this.mapaDetalleCuentaCobro = mapaDetalleCuentaCobro;
	}
	
	/**
	 * @return Retorna mapaDetalleCuentaCobro.
	 */
	public Object getMapaDetalleCuentaCobro(Object key) {
		return mapaDetalleCuentaCobro.get(key);
	}
	/**
	 * @param Asigna dato.
	 */
	public void setMapaDetalleCuentaCobro(Object key, Object dato) {
		this.mapaDetalleCuentaCobro.put(key, dato);
	}

	/**
	 * @return Retorna the cuentaCobroBuscar.
	 */
	public String getCuentaCobroBuscar()
	{
		return cuentaCobroBuscar;
	}

	/**
	 * @param cuentaCobroBuscar The cuentaCobroBuscar to set.
	 */
	public void setCuentaCobroBuscar(String cuentaCobroBuscar)
	{
		this.cuentaCobroBuscar = cuentaCobroBuscar;
	}

	/**
	 * @return Retorna the fechaElaboracionModificar.
	 */
	public String getFechaElaboracionModificar()
	{
		return fechaElaboracionModificar;
	}

	/**
	 * @param fechaElaboracionModificar The fechaElaboracionModificar to set.
	 */
	public void setFechaElaboracionModificar(String fechaElaboracionModificar)
	{
		this.fechaElaboracionModificar = fechaElaboracionModificar;
	}

	/**
	 * @return Retorna the fechaFinalModificar.
	 */
	public String getFechaFinalModificar()
	{
		return fechaFinalModificar;
	}

	/**
	 * @param fechaFinalModificar The fechaFinalModificar to set.
	 */
	public void setFechaFinalModificar(String fechaFinalModificar)
	{
		this.fechaFinalModificar = fechaFinalModificar;
	}

	/**
	 * @return Retorna the fechaInicialModificar.
	 */
	public String getFechaInicialModificar()
	{
		return fechaInicialModificar;
	}

	/**
	 * @param fechaInicialModificar The fechaInicialModificar to set.
	 */
	public void setFechaInicialModificar(String fechaInicialModificar)
	{
		this.fechaInicialModificar = fechaInicialModificar;
	}

	/**
	 * @return Retorna the nombreConvenioModificar.
	 */
	public String getNombreConvenioModificar()
	{
		return nombreConvenioModificar;
	}

	/**
	 * @param nombreConvenioModificar The nombreConvenioModificar to set.
	 */
	public void setNombreConvenioModificar(String nombreConvenioModificar)
	{
		this.nombreConvenioModificar = nombreConvenioModificar;
	}

	/**
	 * @return Retorna the nombreEstadoCxCModificar.
	 */
	public String getNombreEstadoCxCModificar()
	{
		return nombreEstadoCxCModificar;
	}

	/**
	 * @param nombreEstadoCxCModificar The nombreEstadoCxCModificar to set.
	 */
	public void setNombreEstadoCxCModificar(String nombreEstadoCxCModificar)
	{
		this.nombreEstadoCxCModificar = nombreEstadoCxCModificar;
	}

	/**
	 * @return Retorna the observacionesModificar.
	 */
	public String getObservacionesModificar()
	{
		return observacionesModificar;
	}

	/**
	 * @param observacionesModificar The observacionesModificar to set.
	 */
	public void setObservacionesModificar(String observacionesModificar)
	{
		this.observacionesModificar = observacionesModificar;
	}

	/**
	 * @return Retorna the mapaInfoCuentaCobro.
	 */
	public HashMap getMapaInfoCuentaCobro()
	{
		return mapaInfoCuentaCobro;
	}

	/**
	 * @param mapaInfoCuentaCobro The mapaInfoCuentaCobro to set.
	 */
	public void setMapaInfoCuentaCobro(HashMap mapaInfoCuentaCobro)
	{
		this.mapaInfoCuentaCobro = mapaInfoCuentaCobro;
	}
	
	/**
	 * @return Retorna mapaInfoCuentaCobro.
	 */
	public Object getMapaInfoCuentaCobro(Object key) {
		return mapaInfoCuentaCobro.get(key);
	}
	/**
	 * @param Asigna dato.
	 */
	public void setMapaInfoCuentaCobro(Object key, Object dato) {
		this.mapaInfoCuentaCobro.put(key, dato);
	}

	/**
	 * @return Retorna the estadoCuentaCobroBuscar.
	 */
	public int getEstadoCuentaCobroBuscar()
	{
		return estadoCuentaCobroBuscar;
	}

	/**
	 * @param estadoCuentaCobroBuscar The estadoCuentaCobroBuscar to set.
	 */
	public void setEstadoCuentaCobroBuscar(int estadoCuentaCobroBuscar)
	{
		this.estadoCuentaCobroBuscar = estadoCuentaCobroBuscar;
	}

	/**
	 * @return Retorna the patronOrdenar.
	 */
	public String getPatronOrdenar()
	{
		return patronOrdenar;
	}

	/**
	 * @param patronOrdenar The patronOrdenar to set.
	 */
	public void setPatronOrdenar(String patronOrdenar)
	{
		this.patronOrdenar = patronOrdenar;
	}

	/**
	 * @return Retorna the ultimoPatron.
	 */
	public String getUltimoPatron()
	{
		return ultimoPatron;
	}

	/**
	 * @param ultimoPatron The ultimoPatron to set.
	 */
	public void setUltimoPatron(String ultimoPatron)
	{
		this.ultimoPatron = ultimoPatron;
	}

	/**
	 * @return Retorna the codigoEstadoModificar.
	 */
	public int getCodigoEstadoModificar()
	{
		return codigoEstadoModificar;
	}

	/**
	 * @param codigoEstadoModificar The codigoEstadoModificar to set.
	 */
	public void setCodigoEstadoModificar(int codigoEstadoModificar)
	{
		this.codigoEstadoModificar = codigoEstadoModificar;
	}

	/**
	 * @return Retorna the motivoAnulacion.
	 */
	public String getMotivoAnulacion()
	{
		return motivoAnulacion;
	}

	/**
	 * @param motivoAnulacion The motivoAnulacion to set.
	 */
	public void setMotivoAnulacion(String motivoAnulacion)
	{
		this.motivoAnulacion = motivoAnulacion;
	}

	/**
	 * @return Retorna the fechaFinalModAnterior.
	 */
	public String getFechaFinalModAnterior()
	{
		return fechaFinalModAnterior;
	}

	/**
	 * @param fechaFinalModAnterior The fechaFinalModAnterior to set.
	 */
	public void setFechaFinalModAnterior(String fechaFinalModAnterior)
	{
		this.fechaFinalModAnterior = fechaFinalModAnterior;
	}

	/**
	 * @return Retorna the fechaInicialModAnterior.
	 */
	public String getFechaInicialModAnterior()
	{
		return fechaInicialModAnterior;
	}

	/**
	 * @param fechaInicialModAnterior The fechaInicialModAnterior to set.
	 */
	public void setFechaInicialModAnterior(String fechaInicialModAnterior)
	{
		this.fechaInicialModAnterior = fechaInicialModAnterior;
	}

	/**
	 * @return Retorna the codigoConvenioModificar.
	 */
	public int getCodigoConvenioModificar()
	{
		return codigoConvenioModificar;
	}

	/**
	 * @param codigoConvenioModificar The codigoConvenioModificar to set.
	 */
	public void setCodigoConvenioModificar(int codigoConvenioModificar)
	{
		this.codigoConvenioModificar = codigoConvenioModificar;
	}

	/**
	 * @return Retorna the observacionesModAnterior.
	 */
	public String getObservacionesModAnterior()
	{
		return observacionesModAnterior;
	}

	/**
	 * @param observacionesModAnterior The observacionesModAnterior to set.
	 */
	public void setObservacionesModAnterior(String observacionesModAnterior)
	{
		this.observacionesModAnterior = observacionesModAnterior;
	}

	/**
	 * @return Retorna the cuentaCobroFinal.
	 */
	public String getCuentaCobroFinal()
	{
		return cuentaCobroFinal;
	}

	/**
	 * @param cuentaCobroFinal The cuentaCobroFinal to set.
	 */
	public void setCuentaCobroFinal(String cuentaCobroFinal)
	{
		this.cuentaCobroFinal = cuentaCobroFinal;
	}

	/**
	 * @return Retorna the origenGeneracionCuentaCobro.
	 */
	public String getOrigenGeneracionCuentaCobro()
	{
		return origenGeneracionCuentaCobro;
	}

	/**
	 * @param origenGeneracionCuentaCobro The origenGeneracionCuentaCobro to set.
	 */
	public void setOrigenGeneracionCuentaCobro(String origenGeneracionCuentaCobro)
	{
		this.origenGeneracionCuentaCobro = origenGeneracionCuentaCobro;
	}

	/**
	 * @return Retorna the mapaFacturasCxC.
	 */
	public HashMap getMapaFacturasCxC()
	{
		return mapaFacturasCxC;
	}

	/**
	 * @param mapaFacturasCxC The mapaFacturasCxC to set.
	 */
	public void setMapaFacturasCxC(HashMap mapaFacturasCxC)
	{
		this.mapaFacturasCxC = mapaFacturasCxC;
	}
	
	/**
	 * @return Retorna mapaFacturasCxC.
	 */
	public Object getMapaFacturasCxC(Object key) {
		return mapaFacturasCxC.get(key);
	}
	/**
	 * @param Asigna dato.
	 */
	public void setMapaFacturasCxC(Object key, Object dato) {
		this.mapaFacturasCxC.put(key, dato);
	}
	
		/**
	 * @return Retorna mapaDetImpresion.
	 */
	public HashMap getMapaDetImpresion() {
		return mapaDetImpresion;
	}

	/**
	 * @param Asigna mapaDetImpresion.
	 */
	public void setMapaDetImpresion(HashMap mapaDetImpresion) {
		this.mapaDetImpresion = mapaDetImpresion;
	}

	/**
	 * @return Retorna mapaDetImpresion.
	 */
	public Object getMapaDetImpresion(String key) {
		return mapaDetImpresion.get( key );
	}

	/**
	 * @param Asigna mapaDetImpresion.
	 */
	public void setMapaDetImpresion(String key, Object dato) {
		this.mapaDetImpresion.put(key, dato) ;
	}

	/**
	 * @return Retorna mapaCarguesGrupoEtareo.
	 */
	public HashMap getMapaCarguesGrupoEtareo() {
		return mapaCarguesGrupoEtareo;
	}

	/**
	 * @param Asigna mapaCarguesGrupoEtareo.
	 */
	public void setMapaCarguesGrupoEtareo(HashMap mapaCarguesGrupoEtareo) {
		this.mapaCarguesGrupoEtareo = mapaCarguesGrupoEtareo;
	}
	
	/**
	 * @return Retorna mapaEncImpresion.
	 */
	public Object getMapaCarguesGrupoEtareo(String key) {
		return mapaCarguesGrupoEtareo.get( key );
	}

	/**
	 * @param Asigna mapaCarguesGrupoEtareo.
	 */
	public void setMapaCarguesGrupoEtareo(String key, String dato) {
		this.mapaCarguesGrupoEtareo.put(key, dato);
	}

	/**
	 * @return Retorna the fechaFacturaBuscar.
	 */
	public String getFechaFacturaBuscar()
	{
		return fechaFacturaBuscar;
	}

	/**
	 * @param fechaFacturaBuscar The fechaFacturaBuscar to set.
	 */
	public void setFechaFacturaBuscar(String fechaFacturaBuscar)
	{
		this.fechaFacturaBuscar = fechaFacturaBuscar;
	}

	/**
	 * @return Retorna the numeroFacturaBuscar.
	 */
	public String getNumeroFacturaBuscar()
	{
		return numeroFacturaBuscar;
	}

	/**
	 * @param numeroFacturaBuscar The numeroFacturaBuscar to set.
	 */
	public void setNumeroFacturaBuscar(String numeroFacturaBuscar)
	{
		this.numeroFacturaBuscar = numeroFacturaBuscar;
	}

	/**
	 * @return Retorna the viaIngresoBuscar.
	 */
	public int getViaIngresoBuscar()
	{
		return viaIngresoBuscar;
	}

	/**
	 * @param viaIngresoBuscar The viaIngresoBuscar to set.
	 */
	public void setViaIngresoBuscar(int viaIngresoBuscar)
	{
		this.viaIngresoBuscar = viaIngresoBuscar;
	}

	/**
	 * @return Retorna the nitConvenio.
	 */
	public String getNitConvenio()
	{
		return nitConvenio;
	}

	/**
	 * @param nitConvenio The nitConvenio to set.
	 */
	public void setNitConvenio(String nitConvenio)
	{
		this.nitConvenio = nitConvenio;
	}

	/**
	 * @return Retorna the fechaRadicacion.
	 */
	public String getFechaRadicacion()
	{
		return fechaRadicacion;
	}

	/**
	 * @param fechaRadicacion The fechaRadicacion to set.
	 */
	public void setFechaRadicacion(String fechaRadicacion)
	{
		this.fechaRadicacion = fechaRadicacion;
	}

	/**
	 * @return Retorna the numeroRadicacion.
	 */
	public String getNumeroRadicacion()
	{
		return numeroRadicacion;
	}

	/**
	 * @param numeroRadicacion The numeroRadicacion to set.
	 */
	public void setNumeroRadicacion(String numeroRadicacion)
	{
		this.numeroRadicacion = numeroRadicacion;
	}

	/**
	 * @return Retorna the tipoCuentaCobro.
	 */
	public int getTipoCuentaCobro()
	{
		return tipoCuentaCobro;
	}

	/**
	 * @param tipoCuentaCobro The tipoCuentaCobro to set.
	 */
	public void setTipoCuentaCobro(int tipoCuentaCobro)
	{
		this.tipoCuentaCobro = tipoCuentaCobro;
	}
	
	/**
	 * @return Retorna mapaEncImpresion.
	 */
	public HashMap getMapaEncImpresion() {
		return mapaEncImpresion;
	}

	/**
	 * @param Asigna mapaEncImpresion.
	 */
	public void setMapaEncImpresion(HashMap mapaEncImpresion) {
		this.mapaEncImpresion = mapaEncImpresion;
	}
	
	/**
	 * @return Retorna mapaEncImpresion.
	 */
	public Object getMapaEncImpresion(String key) {
		return mapaEncImpresion.get( key );
	}

	/**
	 * @param Asigna mapaEncImpresion.
	 */
	public void setMapaEncImpresion(String key, String dato) {
		this.mapaEncImpresion.put(key, dato);
	}

	public String getPagosCuentaCobro() {
		return pagosCuentaCobro;
	}

	public void setPagosCuentaCobro(String pagosCuentaCobro) {
		this.pagosCuentaCobro = pagosCuentaCobro;
	}

	public String getSaldoTotalCuentaCobro() {
		return saldoTotalCuentaCobro;
	}

	public void setSaldoTotalCuentaCobro(String saldoTotalCuentaCobro) {
		this.saldoTotalCuentaCobro = saldoTotalCuentaCobro;
	}

	public String getValorTotalCuentaCobro() {
		return valorTotalCuentaCobro;
	}

	public void setValorTotalCuentaCobro(String valorTotalCuentaCobro) {
		this.valorTotalCuentaCobro = valorTotalCuentaCobro;
	}

	/**
	 * @return the direccionConvenio
	 */
	public String getDireccionConvenio() {
		return direccionConvenio;
	}

	/**
	 * @param direccionConvenio the direccionConvenio to set
	 */
	public void setDireccionConvenio(String direccionConvenio) {
		this.direccionConvenio = direccionConvenio;
	}

	/**
	 * @return the telefonoConvenio
	 */
	public String getTelefonoConvenio() {
		return telefonoConvenio;
	}

	/**
	 * @param telefonoConvenio the telefonoConvenio to set
	 */
	public void setTelefonoConvenio(String telefonoConvenio) {
		this.telefonoConvenio = telefonoConvenio;
	}

	/**
	 * @return the valorAjusteCredito
	 */
	public String getValorAjusteCredito() {
		return valorAjusteCredito;
	}

	/**
	 * @param valorAjusteCredito the valorAjusteCredito to set
	 */
	public void setValorAjusteCredito(String valorAjusteCredito) {
		this.valorAjusteCredito = valorAjusteCredito;
	}

	/**
	 * @return the valorAjusteDebito
	 */
	public String getValorAjusteDebito() {
		return valorAjusteDebito;
	}

	/**
	 * @param valorAjusteDebito the valorAjusteDebito to set
	 */
	public void setValorAjusteDebito(String valorAjusteDebito) {
		this.valorAjusteDebito = valorAjusteDebito;
	}
	
	/**
	 * 
	 * @return
	 */
	public HashMap getMapaMensajeError() {
		return mapaMensajeError;
	}

	/**
	 * 
	 * @param mapaMensajeError
	 */
	public void setMapaMensajeError(HashMap mapaMensajeError) {
		this.mapaMensajeError = mapaMensajeError;
	}
	
	//Aneno 992
	public String getHoraElaboracion() {
		return horaElaboracion;
	}

	public void setHoraElaboracion(String horaElaboracion) {
		this.horaElaboracion = horaElaboracion;
	}

	public ArrayList<DtoFirmasValoresPorDefecto> getListadoFirmas() {
		return listadoFirmas;
	}

	public void setListadoFirmas(ArrayList<DtoFirmasValoresPorDefecto> listadoFirmas) {
		this.listadoFirmas = listadoFirmas;
	}
	//Fin Anexo 992

	/**
	 * @return the prefijoFactura
	 */
	public String getPrefijoFactura() {
		return prefijoFactura;
	}

	/**
	 * @param prefijoFactura the prefijoFactura to set
	 */
	public void setPrefijoFactura(String prefijoFactura) {
		this.prefijoFactura = prefijoFactura;
	}


	
	
	

}
