/*
 * @(#)ContratoForm.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.actionform.cargos;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.UtilidadValidacion;
import util.Utilidades;

import com.princetonsa.dto.facturacion.DtoControlAnticiposContrato;
import com.princetonsa.dto.facturacion.DtoLogControlAnticipoContrato;
import com.princetonsa.mundo.cargos.Convenio;
import com.servinte.axioma.fwk.exception.IPSException;


/**
 * Form que contiene todos los datos especificos para generar 
 * el Registro de contratos
 * Y adicionalmente hace el manejo de reset de la forma y de
 * validacion de errores de datos de entrada.
 * @version 1,0. Mayo 3, 2004
 * @author wrios 
 */
public class ContratoForm extends ValidatorForm
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -340164330028117407L;

	/**
	 * Para hacer logs de debug / warn / error de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(ContratoForm.class);
	
	/**
	 * Cidigo axioma del contrato
	 */
	private int codigo;

	/**
	 * codigo del convenio al cual corresponde el contrato
	 */
	private int codigoConvenio; 
	
	private String cadenaCodigoConvenio; 
	
	/**
	 * 
	 */
	private String tipoAtencionConvenio; 
	
	/**
	 * Nimero del contrato
	 */
	private String numeroContrato;
	
	
	/**
	 * Fecha inicial del contrato
	 */
	private String fechaInicial;
	
	/**
	 * Fecha final del contrato
	 */
	private String  fechaFinal;
	
	
	/**
	* Estado en el que se encuentra el proceso.       
	*/
	private String estado;
	
	/**
	 * Coleccion con los datos del listado, ya sea para consulta,
	 * como tambion para bisqueda avanzada (pager)
	 */
	private Collection col=null;
	
	/**
	 * Offset para el pager 
	 */
	private int offset=0;
	
	/**
	 * Para la bisqueda
	 * Contiene el nombre del convenio (tabla : convenios - campo: nombre)
	 */
	private String nombreConvenio; 
	
	
	/**
	 * Campo donde se restringe por qui criterios se va a buscar
	 */
	private String criteriosBusqueda[];
	
	/**
	 * Aux para conservar la fecha a la hora de modificar
	 */
	private String fechaInicialAntigua;
	
	/**
	 * Aux para conservar la fecha a la hora de modificar
	 */
	private String fechaFinalAntigua;
	
	/**
	 * Elimina o no un contrato, 
	 * pero la variable "puedoEliminar" 
	 * tiene que estar en true
	 */
	private boolean eliminar;
	
	/**
	 * Fecha del sistema (utilfecha)
	 */
	private String fechaSistema;
	
	/**
	 * valida si puede eliminar un contrato o no,
	 * evaluando sus fechas
	 */
	private boolean puedoEliminar;
	
	/**
	 * valida que se pueda modificar un contrato o no,
	 * evaluando sus fechas
	 */
	private boolean puedoModificar;
	/**
	 * Contiene el log con info original para
	 * almacenar esta info en el log tipo Archivo
	 */
	private String logInfoOriginalContrato;
	
	/**
	 * Contiene el valor true=fechaInicial, false=fechaFinal
	 * para la busqueda por rangos de fechas
	 */
	private String eleccionFechaBusqueda;
	
	/**
	 * valor del contrato
	 */
	private String valorContratoStr;
	
	/**
	 * valor acumulado 
	 */
	private String valorAcumuladoStr;
	
	/**
	 * codigo del tipo de contrato
	 */
	private String codigoTipoContrato="";
	
	/**
	 * estado temp
	 */
	private String estadoTemp;
	
	/**
	 * upc
	 */
	private String upc;
	
	/**
	 * cod tipo pago
	 */
	private String codigoTipoPago;
	
	/**
	 * nombre tipo pago
	 */
	private String nombreTipoPago;
	
	/**
	 * % pyp
	 */
	private String pyp;
	
	/**
	 * contrato secretaria
	 */
	private String contratoSecretaria;
	
	/**
	 * codigo del nivel de atencion
	 */
	private String consecutivoNivelAtencion;
	
	/**
	 * nivel de atencion mapa
	 */
	private HashMap nivelAtencionMap;
	
	/**
	 * indice para eliminar un nivel de atencion
	 */
	private String indexToDelete;
	
	/**
	 * nivel de atencion (busqueda)
	 */
	private String busquedaNivelAtencion;
	
	/**
	 * 
	 */
	private String porcentajeUpc;
	
	/**
	 * 
	 */
	private String base;
	
	/**
	 * 
	 */
	private String nombreBase;
	
	private String fechaFirmaContrato;
	private String diaLimiteRadicacion;
	private String diasRadicacion;
	private String controlaAnticipos;
	private String manejaCobertura;
	
	/**
	 * 
	 */
	private String requiereAutorizacionNoCobertura;
	
	/**
	 * 
	 */
	private HashMap esquemasInventario;
	
	/**
	 * 
	 */
	private HashMap esquemasProcedimientos;
	
	
	/**
	 * 
	 */
	private HashMap esquemasInventarioEliminados;
	
	/**
	 * 
	 */
	private HashMap esquemasProcedimientosEliminados;
	
	/**
	 * 
	 */
	private String accion="";
	
	/**
	 * 
	 */
	private int posEliminar;
	
	/**
	 * 
	 */
	private String sinContrato;
	
	/**
	 * 
	 */
	private String esquemaTarProcedimiento;
	
	/**
	 * 
	 */
	private String esquemaTarInventario;
	
	/**
	 * 
	 */
	private String pacientePagaAtencion;
	
	/**
	 * 
	 */
	private String validarAbonoAtencionOdo;
	
	
	
	/**
	 * 
	 */
	private int codigoTarifarioOficial;
	
	/**
	 * Boolean que indica si se puede eliminar el contrato
	 */
	private boolean esContratoUsado;
	
	
	
	private String frmObservaciones;
	
	/**
	 * 
	 * 
	 * 
	 */
	
	/**
	 * 
	 * 
	 * 
	 */
	
	private DtoControlAnticiposContrato dtoControlAnticipo;
	
	/**
	 * 
	 */
	private String convenioManejaMontos;
	
	/**
	 * reset de los atributos para capitacion
	 *
	 */
	private DtoLogControlAnticipoContrato dtoLogControlAnticipo;
    /**
	 * 
	 * 
	 */
	
	private ArrayList<DtoLogControlAnticipoContrato> listaLogControl;
	
	
	/**
	 * ANEXO 830 MODIFICACION 
	 */
	private boolean esConvenioOdontologicio;
	
	/**
	 * 
	 */
	private String mensaje;
	/**
	 * 
	 */
	private boolean vieneDeIngresarModificar;
	
	/**
	 * 
	 */
	private boolean manejaTarifasXCA;
	
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
		ActionErrors errores= new ActionErrors();					
		if(estado.equals("salir")||estado.equals("guardarModificacion"))
		{				
			errores=super.validate(mapping,request);

			if(this.controlaAnticipos.equals(ConstantesBD.acronimoSi))
			{
				if(! UtilidadTexto.isEmpty(this.valorContratoStr))
				{
					double tmpValorContrato=	Double.parseDouble(this.valorContratoStr) ;
					double tmpValorAnticipo= this.dtoControlAnticipo.getValorAnticipoContratadoConvenio(); 

					if(tmpValorContrato<tmpValorAnticipo)
					{
						errores.add("", new ActionMessage("errors.notEspecific", " El Valor de Contrado Deber Ser Mayor o Igual Que El Valor Total Anticipo "));
					}
				}
			}

			if(this.getControlaAnticipos().equals(ConstantesBD.acronimoSi)){

				if(this.getDtoControlAnticipo().getNumeroMaximoPaciente() > this.getDtoControlAnticipo().getValorAnticipoContratadoConvenio()){
					errores.add("", new ActionMessage("errors.MayorQue", " El Valor Anticipo Contratado"," Maximo por paciente"));
				}

				if(this.getDtoControlAnticipo().getValorAnticipoContratadoConvenio() <= 0){
					errores.add("", new ActionMessage("errors.MayorQue", "El Valor Anticipo Contratado","0"));
				}
			}


			if(this.getCodigoConvenio()<=0)
			{	
				errores.add("Campo Convenio no seleccionado", new ActionMessage("errors.required","El campo Convenio"));
			}
			if(this.numeroContrato.trim().equals(""))
			{
				errores.add("No contrato no ingresado", new ActionMessage("errors.required", "El campo No. Contrato"));
			}
			if(valorContratoStr.equals(""))
			{	
				errores.add("Campo valor contrato requerido", new ActionMessage("errors.required","El campo Vr.Contrato"));
			}
			else
			{
				try
				{
					double tempValor=Double.parseDouble(valorContratoStr);
					if(tempValor<0)
					{
						errores.add("Campo valor contrato es un double", new ActionMessage("errors.floatMayorOIgualQue","El campo Vr.Contrato","0"));
					}
				}
				catch(NumberFormatException e)
				{
					errores.add("Campo valor contrato es un double", new ActionMessage("errors.floatMayorOIgualQue","El campo Vr.Contrato","0"));
				}
			}
			if(valorAcumuladoStr.equals(""))
			{	
				errores.add("Campo valor acumulado requerido", new ActionMessage("errors.required","El campo Vr.Acumulado"));
			}
			else
			{
				try
				{
					double tempValor=Double.parseDouble(valorAcumuladoStr);
					if(tempValor<0)
					{
						errores.add("Campo valor acumulado es un double", new ActionMessage("errors.floatMayorOIgualQue","El campo Vr.Acumulado","0"));
					}
				}
				catch(NumberFormatException e)
				{
					errores.add("Campo valor acumulado es un double", new ActionMessage("errors.floatMayorOIgualQue","El campo Vr.Acumulado","0"));
				}
			}


			/*****************************VALIDACIONES DE CAPITACION*****************************************/
			if(codigoTipoContrato.equals(ConstantesBD.codigoTipoContratoCapitado+""))
			{
				if(this.numeroContrato.trim().equals(""))
				{
					errores.add("", new ActionMessage("error.contrato.requeridoCapitacion", "No. Contrato"));
				}
				if(codigoTipoPago.trim().equals(""))
				{
					errores.add("", new ActionMessage("error.contrato.requeridoCapitacion", "Tipo Pago"));
				}
				else if(codigoTipoPago.equals(ConstantesBD.codigoTipoPagoUpc))
				{	
					if(upc.trim().equals("") && (porcentajeUpc.trim().equals("")))
					{
						errores.add("", new ActionMessage("error.contrato.requeridoCapitacion", "UPC"));
					}
					else
					{
						if(!upc.trim().equals("") && !porcentajeUpc.trim().equals(""))
						{
							errores.add("", new ActionMessage("errors.notEspecific", "El campo UPC Contrato y el %UPC son EXCLUYENTES, solo uno debe tener valor, por favor verifique"));
						}

						if(!upc.trim().equals(""))
						{	
							double valorUpc=0;
							boolean centinela=false;
							try
							{
								valorUpc=Double.parseDouble(upc);
							}
							catch(Exception e)
							{
								//logger.info("\n linea ------> 394");
								errores.add("", new ActionMessage("error.contrato.requeridoCapitacion", "UPC debe ser mayor que 0 porque el tipo de pago es UPC ademis "));
								centinela=true;
							}
							if(!centinela)
							{	//se hace la modificacion de esta parte por tarea 10863
								if(valorUpc==0 )
								{
									//logger.info("\n linea ------> 401");
									errores.add("", new ActionMessage("error.contrato.requeridoCapitacion", "UPC debe ser mayor que 0  porque el tipo de pago es UPC ademis "));
								}
								else
								{
									if(UtilidadValidacion.esMayorNdecimales(upc, 2))
									{
										errores.add("", new ActionMessage("errors.numDecimales", "El UPC "+valorUpc, "2"));
									}
								}
							}
						}
						else if(!porcentajeUpc.trim().equals(""))
						{
							double valorPorcentajeUpc=0;
							boolean centinela=false;
							try
							{
								valorPorcentajeUpc=Double.parseDouble(porcentajeUpc);
							}
							catch(Exception e)
							{
								//logger.info("\n linea ------> 422");
								errores.add("", new ActionMessage("error.contrato.requeridoCapitacion", "Porcenteje UPC debe ser mayor que 0 porque el tipo de pago es UPC ademis "));
								centinela=true;
							}
							if(!centinela)
							{	
								if(valorPorcentajeUpc==0 || valorPorcentajeUpc>100)
								{
									//logger.info("\n linea ------> 432");
									errores.add("", new ActionMessage("error.contrato.requeridoCapitacion", "Porcentaje UPC debe ser mayor que 0 porque el tipo de pago es UPC ademis "));
									centinela=true;
								}
								else
								{
									if(esMayorADosDecimales(valorPorcentajeUpc))
									{
										errores.add("", new ActionMessage("errors.numDecimales", "El Porcentaje UPC "+valorPorcentajeUpc, "2"));
										centinela=true;
									}
								}
							}
							if(!centinela)
							{
								if(base.equals(""))
								{
									errores.add("", new ActionMessage("error.contrato.requeridoCapitacion", "Base"));
								}
							}

						}
					}
				}
				Convenio convenioMundo= new Convenio();
				Connection con=UtilidadBD.abrirConexion();
				try {
					convenioMundo.cargarResumen(con, this.codigoConvenio);
				} catch (IPSException e1) {
					Log4JManager.error(e1.getMessage(), e1);
				}
				UtilidadBD.closeConnection(con);
				if(convenioMundo.getPyp().equals("true") || convenioMundo.getPyp().equals("t"))
				{
					if(pyp.trim().equals(""))
					{
						errores.add("", new ActionMessage("error.contrato.requeridoCapitacion", "%PyP"));
					}
					else
					{
						double porcentajePyP=0;
						boolean centinela=false;
						try
						{
							porcentajePyP=Double.parseDouble(pyp);
						}
						catch(Exception e)
						{
							errores.add("", new ActionMessage("error.contrato.requeridoCapitacion", "%PYP debe ser mayor que 0 y menor que 100 ademis "));
							centinela=true;
						}
						if(!centinela)
						{	
							if(porcentajePyP==0 || porcentajePyP>100)
							{
								//logger.info("\n linea ------> 488");
								errores.add("", new ActionMessage("error.contrato.requeridoCapitacion", "UPC debe ser mayor que 0 y menor que 100 ademis "));
							}
							else
							{
								if(esMayorADosDecimales(porcentajePyP))
								{
									errores.add("", new ActionMessage("errors.numDecimales", "El % PYP "+porcentajePyP, "2"));
								}
							}
						}
					}
				}

				if(this.contratoSecretaria.length()<12)
				{
					errores.add("", new ActionMessage("error.contrato.requeridoCapitacion", "Contrato Secretaria debe ser monimo 12 caracteres"));
				}

				if(!existeNivelAtencion())
				{
					errores.add("", new ActionMessage("errors.required", "El campo Nivel Atencion"));
				}

			}
			/****************************FIN VALIDACIONES DE CAPITACION************************************************/
			boolean centinelaErrorFecaha=false;

			if(fechaInicial.equals(""))
			{	
				errores.add("Campo Fecha Inicial vacio", new ActionMessage("errors.required","El campo Fecha Inicial"));
				centinelaErrorFecaha=true;
			}
			else
			{
				if(!UtilidadFecha.validarFecha(fechaInicial))
				{
					errores.add("Fecha inicial", new ActionMessage("errors.formatoFechaInvalido", " Inicial"));
					centinelaErrorFecaha=true;
				}
				/*
				 * XPlanner oid=4782
				 * else if(validacionFechaSistema(fechaInicial, UtilidadFecha.getFechaActual()))
								{		
										if(this.puedoModificar==true || this.estado.equals("salir"))	
											errores.add("Fecha inicial", new ActionMessage("errors.fechaAnteriorIgualActual","inicial", "actual"));
								}*/
			}
			if(fechaFinal.equals(""))
			{	
				errores.add("Campo Fecha Final vacio", new ActionMessage("errors.required","El campo Fecha Final"));
				centinelaErrorFecaha=true;
			}
			else
			{
				if(!UtilidadFecha.validarFecha(fechaFinal))
				{
					errores.add("Fecha Final", new ActionMessage("errors.formatoFechaInvalido", "Final"));
					centinelaErrorFecaha=true;
				}
				else if(validacionFechaSistema(fechaFinal, UtilidadFecha.getFechaActual()))
				{
					errores.add("Fecha Final", new ActionMessage("errors.fechaAnteriorIgualActual","final", "actual"));
					centinelaErrorFecaha=true;
				}
			}
			if(!UtilidadTexto.isEmpty(fechaFirmaContrato))
			{
				if(!UtilidadTexto.getBoolean(this.sinContrato))
				{
					if(!UtilidadFecha.validarFecha(fechaFirmaContrato))
					{
						errores.add("Fecha Firma Contratos", new ActionMessage("errors.formatoFechaInvalido", "Firma Contratos"));
						centinelaErrorFecaha=true;
					}
					else if(UtilidadFecha.esFechaMenorQueOtraReferencia(UtilidadFecha.getFechaActual(),fechaFirmaContrato))
					{
						errores.add("Fecha Final", new ActionMessage("errors.fechaAnteriorIgualActual","Firma Contratos", "actual"));
						centinelaErrorFecaha=true;
					}
				}
				else
				{
					/*
					 *tarea id=32137 
					 */
					if(!UtilidadTexto.isEmpty(fechaFirmaContrato))
					{
						if(!UtilidadFecha.validarFecha(fechaFirmaContrato))
						{
							errores.add("Fecha Firma Contratos", new ActionMessage("errors.formatoFechaInvalido", "Firma Contratos"));
							centinelaErrorFecaha=true;
						}
						else if(UtilidadFecha.esFechaMenorQueOtraReferencia(UtilidadFecha.getFechaActual(),fechaFirmaContrato))
						{
							errores.add("Fecha Final", new ActionMessage("errors.fechaAnteriorIgualActual","Firma Contratos", "actual"));
							centinelaErrorFecaha=true;
						}
					}
				}
			}
			if(!centinelaErrorFecaha)
			{
				if(validacionFechaSistema(fechaFinal,fechaInicial))
				{
					errores.add("Fecha Final", new ActionMessage("errors.fechaAnteriorIgualActual", " Final", "Inicial"));
				}
			}

			/*if(tipoContrato<=0)
						{	
								errores.add("Tipo Contrato no seleccionado", new ActionMessage("errors.required","El campo Tipo Contrato"));
						}*/


			///validaciones de esquemas tarifarios
			//inventarios.
			validacionesEsquemasTarifarios(errores);	

			if(!errores.isEmpty())
			{
				if(estado.equals("salir"))
					this.setEstado("empezar");

				if(estado.equals("guardarModificacion"))
					this.setEstado("modificar");
			}
		}
		else if(estado.equals("inserta"))
		{
			mapping.findForward("principal");
		}
		else if(estado.equals("modificar"))
		{
			errores=super.validate(mapping,request);
		}
		else if(estado.equals("listar") || estado.equals("listarModificar"))
		{
			errores=super.validate(mapping,request);
		}	
		else if(estado.equals("busquedaAvanzada"))
		{
			errores=super.validate(mapping,request);
		}	
		else if(estado.equals("resultadoBusquedaAvanzada") || estado.equals("resultadoBusquedaAvanzadaModificar"))
		{		
			errores=super.validate(mapping,request);
			String bb[]= this.getCriteriosBusqueda();
			for(int i=0; i<bb.length; i++)
			{
				if(bb[i].equals("rangoFechas"))
				{
					if(eleccionFechaBusqueda.equals(""))
						errores.add("Campo fecha inicial/final", new ActionMessage("errors.required","Si selecciona bisqueda por rangos entonces fecha inicial/final "));
					else
					{
						if(fechaInicial.equals(""))
							errores.add("Campo Fecha DESDE vacio", new ActionMessage("errors.required","Si selecciona bisqueda por rangos entonces la Fecha DESDE "));
						else if(!UtilidadFecha.validarFecha(fechaInicial))
							errores.add("Fecha DESDE", new ActionMessage("errors.formatoFechaInvalido", " DESDE"));
						if(fechaFinal.equals(""))
							errores.add("Campo Fecha HASTA vacio", new ActionMessage("errors.required","Si selecciona bisqueda por rangos entonces la Fecha HASTA "));
						else if(!UtilidadFecha.validarFecha(fechaFinal))
							errores.add("Fecha HASTA", new ActionMessage("errors.formatoFechaInvalido", "HASTA"));
						if( UtilidadFecha.validarFecha(fechaInicial))
						{
							if( UtilidadFecha.validarFecha(fechaFinal))
							{
								if(validacionFechaSistema(fechaFinal, fechaInicial))
									errores.add("Fechas", new ActionMessage("errors.fechaAnteriorIgualActual","inicial", "final"));
							}
						}	
					}									
				}
				if(bb[i].equals("upc"))
				{
					try
					{
						Double.parseDouble(upc);
					}catch(Exception e)
					{
						errores.add("errors.float", new ActionMessage("errors.float", " UPC "));
					}
				}
				if(bb[i].equals("pyp"))
				{
					try
					{
						Integer.parseInt(pyp);
					}catch(Exception e)
					{
						errores.add("errors.float", new ActionMessage("errors.float", " %PYP "));
					}
				}
			}
		}		
		return errores;
	}

	/**
	 * @param errores
	 */
	private void validacionesEsquemasTarifarios(ActionErrors errores) 
	{
		if(Utilidades.convertirAEntero(esquemasInventario.get("numRegistros")+"")>0)
		{
			boolean esquemaInventarioGenerico=false;
			for(int i=0;i<Utilidades.convertirAEntero(esquemasInventario.get("numRegistros")+"");i++)
			{
				if((esquemasInventario.get("tiporegistro_"+i)+"").equals("MEM"))
				{
					if(this.isManejaTarifasXCA())
					{
						if(Utilidades.convertirAEntero(this.getEsquemasInventario("codcentroatencion_"+i)+"")<=0)
						{
							errores.add("Fecha Requerido",new ActionMessage("errors.required","El centro atencion "+(i+1)+" de esquemas tarifarios Inventarios"));
						}
					}
					
					if((esquemasInventario.get("esquematarifario_"+i)+"").trim().equals(""))
					{
						errores.add("Fecha Requerido",new ActionMessage("errors.required","El Esquema Tarifario "+(i+1)+" de esquemas tarifarios Inventarios"));
					}

					String fechaTempo=esquemasInventario.get("fechavigencia_"+i)+"";
					if(fechaTempo.trim().equals(""))
					{
						errores.add("Fecha Requerido",new ActionMessage("errors.required","La fecha de la Vigencia "+(i+1)+" de esquemas tarifarios Inventarios"));
					}
					else
					{
						if(!UtilidadFecha.validarFecha(fechaTempo))
						{
							errores.add("fecha", new ActionMessage("errors.formatoFechaInvalido",fechaTempo+" de esquemas tarifarios Inventarios"));
						}
						/*
						 * xplanner [id=31978]
									else
									{
										if(UtilidadFecha.esFechaMenorQueOtraReferencia( (esquemasInventario.get("fechavigencia_"+i)+""),fechaActual))
										{
											errores.add("Fecha Final", new ActionMessage("errors.fechaAnteriorIgualActual","Vigencia "+(i+1)+" de esquemas tarifarios Inventarios", "actual"));
										}
									}
						 */
					}
				}

				for(int j=0;j<i;j++)
				{
					if(this.isManejaTarifasXCA())
					{	
						if(	(esquemasInventario.get("claseinventario_"+j)+"").trim().equals(esquemasInventario.get("claseinventario_"+i)+"")
							&& (esquemasInventario.get("esquematarifario_"+j)+"").trim().equals(esquemasInventario.get("esquematarifario_"+i)+"")
							&&(esquemasInventario.get("fechavigencia_"+j)+"").trim().equals(esquemasInventario.get("fechavigencia_"+i)+"") 
							&&  (esquemasInventario.get("codcentroatencion_"+j)+"").trim().equals(esquemasInventario.get("codcentroatencion_"+i)+""))
						{
							errores.add("", new ActionMessage("error.errorEnBlanco","El registro "+(i+1)+" de Esquemas Tarifarios Inventarios, se encuentra repetido"));
						}
					}
					else
					{
						if(	(esquemasInventario.get("claseinventario_"+j)+"").trim().equals(esquemasInventario.get("claseinventario_"+i)+"")
								&& (esquemasInventario.get("esquematarifario_"+j)+"").trim().equals(esquemasInventario.get("esquematarifario_"+i)+"")
								&&(esquemasInventario.get("fechavigencia_"+j)+"").trim().equals(esquemasInventario.get("fechavigencia_"+i)+"")) 
							{
								errores.add("", new ActionMessage("error.errorEnBlanco","El registro "+(i+1)+" de Esquemas Tarifarios Inventarios, se encuentra repetido"));
							}
					}
					/* Se comenta por al tarea 141574
					 * else if((esquemasInventario.get("claseinventario_"+j)+"").trim().equals(esquemasInventario.get("claseinventario_"+i)+"")
										&&(esquemasInventario.get("fechavigencia_"+j)+"").trim().equals(esquemasInventario.get("fechavigencia_"+i)+""))
								{
									errores.add("", new ActionMessage("error.errorEnBlanco","El registro "+(i+1)+" de Esquemas Tarifarios Inventarios, se encuentra repetido"));
								}*/
				}

				if(UtilidadTexto.isEmpty(esquemasInventario.get("claseinventario_"+i)+""))
					esquemaInventarioGenerico=true;

			}
			if(!esquemaInventarioGenerico)
			{
				errores.add("", new ActionMessage("error.errorEnBlanco","Es requerido ingresar un registro generico en los Esquemas Tarifarios de Inventario"));
			}
		}
		else
		{
			errores.add("Fecha Requerido",new ActionMessage("errors.required","Esquemas Tarifarios Inventario"));
		}

		//procedimientos.
		if(Utilidades.convertirAEntero(esquemasProcedimientos.get("numRegistros")+"")>0)
		{
			boolean esquemaProcedimientoGenerico=false;
			for(int i=0;i<Utilidades.convertirAEntero(esquemasProcedimientos.get("numRegistros")+"");i++)
			{
				if((esquemasProcedimientos.get("tiporegistro_"+i)+"").equals("MEM"))
				{
					if(this.isManejaTarifasXCA())
					{
						if(Utilidades.convertirAEntero(this.getEsquemasProcedimientos("codcentroatencion_"+i)+"")<=0)
						{
							errores.add("Fecha Requerido",new ActionMessage("errors.required","El centro atencion "+(i+1)+" de esquemas tarifarios Procedimientos"));
						}
					}
					
					if((esquemasProcedimientos.get("esquematarifario_"+i)+"").trim().equals(""))
					{
						errores.add("Fecha Requerido",new ActionMessage("errors.required","El Esquema Tarifario "+(i+1)+" de esquemas tarifarios Procedimientos"));
					}

					String fechaTempo=esquemasProcedimientos.get("fechavigencia_"+i)+"";
					if(fechaTempo.trim().equals(""))
					{
						errores.add("Fecha Requerido",new ActionMessage("errors.required","La fecha de la Vigencia "+(i+1)+" de esquemas tarifarios Procedimientos"));
					}
					else
					{
						if(!UtilidadFecha.validarFecha(fechaTempo))
						{
							errores.add("fecha", new ActionMessage("errors.formatoFechaInvalido",fechaTempo+" de esquemas tarifarios Procedimientos"));
						}
						/*
						 * xplanner [id=31978]
									else
									{
										if(UtilidadFecha.esFechaMenorQueOtraReferencia( (esquemasProcedimientos.get("fechavigencia_"+i)+""),fechaActual))
										{
											errores.add("Fecha Final", new ActionMessage("errors.fechaAnteriorIgualActual","Vigencia "+(i+1)+" de esquemas tarifarios Procedimientos", "actual"));
										}
									}
						 */
					}
				}

				for(int j=0;j<i;j++)
				{
					if(this.isManejaTarifasXCA())
					{	
						if((esquemasProcedimientos.get("gruposervicio_"+j)+"").trim().equals(esquemasProcedimientos.get("gruposervicio_"+i)+"")
								&&(esquemasProcedimientos.get("esquematarifario_"+j)+"").trim().equals(esquemasProcedimientos.get("esquematarifario_"+i)+"")
								&&(esquemasProcedimientos.get("fechavigencia_"+j)+"").trim().equals(esquemasProcedimientos.get("fechavigencia_"+i)+"")
								&&(esquemasProcedimientos.get("codcentroatencion_"+j)+"").trim().equals(esquemasProcedimientos.get("codcentroatencion_"+i)+""))
						{
							errores.add("", new ActionMessage("error.errorEnBlanco","El registro "+(i+1)+" de Esquemas Tarifarios Procedimientos, se encuentra repetido"));
						}
					}
					else
					{
						if((esquemasProcedimientos.get("gruposervicio_"+j)+"").trim().equals(esquemasProcedimientos.get("gruposervicio_"+i)+"")
								&&(esquemasProcedimientos.get("esquematarifario_"+j)+"").trim().equals(esquemasProcedimientos.get("esquematarifario_"+i)+"")
								&&(esquemasProcedimientos.get("fechavigencia_"+j)+"").trim().equals(esquemasProcedimientos.get("fechavigencia_"+i)+""))
						{
							errores.add("", new ActionMessage("error.errorEnBlanco","El registro "+(i+1)+" de Esquemas Tarifarios Procedimientos, se encuentra repetido"));
						}
					}
					//Se modifico seg�n la Tarea 46910; porque el flujo estaba incorrecto. Es de precisar que esto se comentario porque la Tarea 36758
					/* Se comenta por al tarea 141574
					 * else if((esquemasProcedimientos.get("gruposervicio_"+j)+"").trim().equals(esquemasProcedimientos.get("gruposervicio_"+i)+"")&&(esquemasProcedimientos.get("fechavigencia_"+j)+"").trim().equals(esquemasProcedimientos.get("fechavigencia_"+i)+""))
								{
									errores.add("", new ActionMessage("error.errorEnBlanco","El registro "+(i+1)+" de Esquemas Tarifarios Procedimientos, se encuentra repetido"));
								}*/
				}

				if(UtilidadTexto.isEmpty(esquemasProcedimientos.get("gruposervicio_"+i)+""))
					esquemaProcedimientoGenerico=true;

			}
			if(!esquemaProcedimientoGenerico)
			{
				errores.add("", new ActionMessage("error.errorEnBlanco","Es requerido ingresar un registro generico en los Esquemas Tarifarios de Procedimientos"));
			}
		}
		else
		{
			errores.add("Fecha Requerido",new ActionMessage("errors.required","Esquemas Tarifarios Procedimientos"));

		}
	}

	/**
	 * indica la existencia de un nivel de atencion
	 * @return
	 */
	public boolean existeNivelAtencion()
	{
		boolean existeNivelAtencion=false;
		if(this.getNumeroRegistrosNivelAtencionMap()>0)
		{
			for(int w=0; w<this.getNumeroRegistrosNivelAtencionMap(); w++)
			{
				if(!UtilidadTexto.getBoolean(this.getNivelAtencionMap("fueEliminado_"+w)+""))
				{
					existeNivelAtencion=true;
				}
			}
		}
		return existeNivelAtencion;
	}
	
	/**
	 * es mayor a dos decimales
	 * @param a
	 * @return
	 */
	public boolean esMayorADosDecimales(double a)
	{
		String temp= a+"";
		int j=0;
		for(int i=0; i<temp.length(); i++)
		{
			if(temp.charAt(i)=='.')
			{
				j=temp.length()-i-1;
			}
		}
		if(j>2)
		return true;
		else
		return false;
	}
	
	//si retorna true es que es menor que la fecha del sistema
	public boolean validacionFechaSistema(String fecha, String fecha1)
	{
		String[] codigoNombre = fecha1.split("/", 3);
		String[] codigoNombre1 = fecha.split("/", 3);
		
		if(Integer.parseInt(codigoNombre1[2]) < Integer.parseInt(codigoNombre[2]))
			return true;
		else if(Integer.parseInt(codigoNombre1[2]) == Integer.parseInt(codigoNombre[2]))
		{
			if(Integer.parseInt(codigoNombre1[1]) < Integer.parseInt(codigoNombre[1]))
			return true;
			else if(Integer.parseInt(codigoNombre1[1]) == Integer.parseInt(codigoNombre[1]))
			{		
				if(Integer.parseInt(codigoNombre1[0]) < Integer.parseInt(codigoNombre[0]))
					return true;
				else if(Integer.parseInt(codigoNombre1[0]) == Integer.parseInt(codigoNombre[0]))	
					return false;
			}
		}	
		return false;	 
	}
			
	
	public void reset()
	{
		this.codigo=0;
		this.codigoConvenio=0;
		this.fechaFinal="";
		this.fechaInicial="";
		this.numeroContrato="";
		
		this.nombreConvenio="";
		
		this.fechaInicialAntigua="";
		this.fechaFinalAntigua="";
		
		this.eliminar=false;
		this.fechaSistema="";
		this.puedoEliminar=false;
		this.puedoModificar=false;
		
		this.logInfoOriginalContrato="";
		this.eleccionFechaBusqueda="";
		
		this.valorContratoStr="";
		this.valorAcumuladoStr="";
		
		this.codigoTipoContrato="";
		this.estadoTemp="";
		
		this.consecutivoNivelAtencion="";
		this.nivelAtencionMap= new HashMap();
		
		this.fechaFirmaContrato="";
		this.diaLimiteRadicacion="";
		this.diasRadicacion="";
		this.controlaAnticipos=ConstantesBD.acronimoNo;
		this.manejaCobertura=ConstantesBD.acronimoSi;
		
		this.requiereAutorizacionNoCobertura=ConstantesBD.acronimoNo;
		
		this.pacientePagaAtencion="";
		this.validarAbonoAtencionOdo="";
		
		this.esquemasInventario=new HashMap<String,Object>();
		this.esquemasInventario.put("numRegistros", "0");
			
		this.esquemasProcedimientos=new HashMap<String, Object>();
		this.esquemasProcedimientos.put("numRegistros", "0");
		
		this.esquemasInventarioEliminados=new HashMap<String,Object>();
		this.esquemasInventarioEliminados.put("numRegistros", "0");
			
		this.esquemasProcedimientosEliminados=new HashMap<String, Object>();
		this.esquemasProcedimientosEliminados.put("numRegistros", "0");
		
		this.posEliminar=ConstantesBD.codigoNuncaValido;
		this.sinContrato="";
		
		this.esquemaTarInventario="";
		this.esquemaTarProcedimiento="";
		
		this.codigoTarifarioOficial=ConstantesBD.codigoNuncaValido;
		this.esContratoUsado = false;
		
		this.frmObservaciones = "";
		
		this.cadenaCodigoConvenio = "";
		this.tipoAtencionConvenio = ConstantesIntegridadDominio.acronimoTipoAtencionConvenioMedicoGeneral;
		
		this.dtoControlAnticipo = new DtoControlAnticiposContrato();
		this.convenioManejaMontos="";
		
		this.dtoLogControlAnticipo = new DtoLogControlAnticipoContrato();
		
		this.listaLogControl = new ArrayList<DtoLogControlAnticipoContrato>();
		resetCapitacion();
		
		this.esConvenioOdontologicio= Boolean.FALSE;
		
		this.mensaje="";
		this.vieneDeIngresarModificar=false;
		this.manejaTarifasXCA= false;
		
	}	
	
	public int getCodigoTarifarioOficial() {
		return codigoTarifarioOficial;
	}

	public void setCodigoTarifarioOficial(int codigoTarifarioOficial) {
		this.codigoTarifarioOficial = codigoTarifarioOficial;
	}

	public void resetCapitacion()
	{
		this.upc="";
		this.codigoTipoPago="";
		this.nombreTipoPago="";
		this.pyp="";
		this.contratoSecretaria="";
		this.indexToDelete="";
		this.busquedaNivelAtencion="";
		
		this.porcentajeUpc="";
		this.base="";
		this.nombreBase="";
	}
	
	
	/**
	 * resetea en vector de strings que
	 * contiene los criterios de bisqueda 
	 *
	 */
	public void resetCriteriosBusqueda()
	{
		try
		{
			for(int k=0 ; k<criteriosBusqueda.length ; k++)
				criteriosBusqueda[k]="";
		}catch(Exception e)
		{
			logger.warn(" error en el reset de busqueda "+e);
		}
	}	
	
	/**
	 *  Retorna  Cidigo axioma del contrato
	 * @return
	 */
	public int getCodigo() {
		return codigo;
	}

	/**
	 *  Retorna cidigo del convenio al cual corresponde el contrato
	 * @return
	 */
	public int getCodigoConvenio() {
		
		logger.info("cadenaCodigoConvenio "+cadenaCodigoConvenio);
		if (! UtilidadTexto.isEmpty(cadenaCodigoConvenio))
		{
			String[] convenioCont=cadenaCodigoConvenio.split(ConstantesBD.separadorSplit); 
			
			if(!UtilidadTexto.isEmpty(convenioCont[0])){
				return Integer.parseInt(convenioCont[0]);
			}else{
				return ConstantesBD.codigoNuncaValido;
			}
		}
		return ConstantesBD.codigoNuncaValido;
	}
    

	/**
	 *  Retorna la fecha final del contrato
	 * @return
	 */
	public String getFechaFinal() {
		return fechaFinal;
	}

	/**
	 *  Retorna la fecha inicial del contrato
	 * @return
	 */
	public String getFechaInicial() {
		return fechaInicial;
	}

	/** 
	 * Retorna el nimero del contrato
	 * @return
	 */
	public String getNumeroContrato() {
		return numeroContrato;
	}

	/** 
	 * Asigna  Cidigo axioma del contrato
	 * @param i
	 */
	public void setCodigo(int i) {
		codigo = i;
	}

	/**
	 * Asigna cidigo del convenio al cual corresponde el contrato
	 * @param i
	 */
	public void setCodigoConvenio(int i) {
		codigoConvenio = i;
	}


	/**
	 * Asigna la fecha final del contrato
	 * @param string
	 */
	public void setFechaFinal(String string) {
		fechaFinal = string;
	}

	/**
	 * Asigna la fecha inicial del contrato
	 * @param string
	 */
	public void setFechaInicial(String string) {
		fechaInicial = string;
	}

	/**
	 * Asigna el nimero del contrato
	 * @param string
	 */
	public void setNumeroContrato(String string) {
		numeroContrato = string;
	}
	
	/**
	 * Retorna el estado en que se encuentra el registro del contrato
	 * @return
	 */
	public String getEstado() {
		return estado;
	}

	/**
	 * Asigna el estado en que se encuentra el registro del contrato
	 * @param string
	 */
	public void setEstado(String string) {
		estado = string;
	}

	/**
	 * Retorna Coleccion para mostrar datos en el pager
	 * @return
	 */
	public Collection getCol() {
		return col;
	}
	
	/**
	 * Asigna Coleccion para mostrar datos en el pager
	 * @param collection
	 */
	public void setCol(Collection collection) {
		col = collection;
	}
	
	public int getColSize()
	{
		if(col!=null)
			return col.size();
		else
			return 0;
	}
	
	/**
	 * Retorna Offset del pager
	 * @return
	 */
	public int getOffset()
	{
		return offset;
	}

	/**
	 * Asigna Offset del pager
	 * @param i
	 */
	public void setOffset(int i) 
	{
		offset = i;
	}

	/**
	 * Para la bisqueda, 
	 * Contiene el nombre del convenio 
	 * (tabla : convenios - campo: nombre)
	 * @return
	 */
	public String getNombreConvenio() {
		return nombreConvenio;
	}

	
	/**
	 *  Para la bisqueda, 
	 * Contiene el nombre del convenio 
	 * (tabla : convenios - campo: nombre)
	 * @param string
	 */
	public void setNombreConvenio(String string) {
		nombreConvenio = string;
	}


	
	/**
	 * Retorna los criterios de bisqueda como strings
	 * @return
	 */
	public String[] getCriteriosBusqueda() {
		return criteriosBusqueda;
	}

	/**
	 * Asigna los criterios de bisqueda como strings
	 * @param strings
	 */
	public void setCriteriosBusqueda(String[] strings) {
		criteriosBusqueda = strings;
	}

	/**
	 * Aux para conservar la fecha a la hora de modificar
	 * @return
	 */
	public String getFechaFinalAntigua() {
		return fechaFinalAntigua;
	}

	/**
	 * Aux para conservar la fecha a la hora de modificar
	 * @return
	 */
	public String getFechaInicialAntigua() {
		return fechaInicialAntigua;
	}

	/**
	 * Aux para conservar la fecha a la hora de modificar
	 * @param string
	 */
	public void setFechaFinalAntigua(String string) {
		fechaFinalAntigua = string;
	}

	/**
	 * Aux para conservar la fecha a la hora de modificar
	 * @param string
	 */
	public void setFechaInicialAntigua(String string) {
		fechaInicialAntigua = string;
	}

	/**
	 * Retorna= Elimina o no un contrato, 
	 * pero la variable "puedoEliminar" 
	 * tiene que estar en true
	 * @return
	 */

	public boolean getEliminar() {
		return eliminar;
	}

	/**
	 * 
	 * Asigna= Elimina o no un contrato, 
	 * pero la variable "puedoEliminar" 
	 * tiene que estar en true
	 * @param b
	 */
	public void setEliminar(boolean b) {
		eliminar = b;
	}

	
	/**
	 * retorna fechaSistema
	 * @return
	 */
	public String getFechaSistema()
	{
		if(this.fechaSistema!=null);
		return fechaSistema=UtilidadFecha.getFechaActual();
	}

	/**
	 * valida si puede eliminar un contrato o no,
	 * evaluando sus fechas
	 * @return
	 */
	public boolean getPuedoEliminar() {
		return puedoEliminar;
	}

	/**
	 * Asigna fechaSistema
	 * @param string
	 */
	public void setFechaSistema(String string) {
		fechaSistema = string;
	}

	/**
	 * valida si puede eliminar un contrato o no,
	 * evaluando sus fechas
	 * @param b
	 */
	public void setPuedoEliminar(boolean b) {
		puedoEliminar = b;
	}

	/**
	 * retorna el log con info original para
	 * almacenar esta info en el log tipo Archivo
	 * @return
	 */
	public String getLogInfoOriginalContrato() {
		return logInfoOriginalContrato;
	}

	/**
	 * Asigna el log con info original para
	 * almacenar esta info en el log tipo Archivo
	 * @param string
	 */
	public void setLogInfoOriginalContrato(String string) {
		logInfoOriginalContrato = string;
	}
	
	/**
	 * retorna el valor true=fechaInicial, false=fechaFinal
	 * para la busqueda por rangos de fechas
	 */
	public String getEleccionFechaBusqueda() {
		return eleccionFechaBusqueda;
	}

	/**
	 * asigna el valor true=fechaInicial, false=fechaFinal
	 * para la busqueda por rangos de fechas
	 */
	public void setEleccionFechaBusqueda(String string) {
		eleccionFechaBusqueda = string;
	}

	/**
	 * retorna si puedo modificar
	 * @return
	 */
	public boolean isPuedoModificar() {
		return puedoModificar;
	}

	/**
	 * asigna si puedo modificar
	 * @param b
	 */
	public void setPuedoModificar(boolean b) {
		puedoModificar = b;
	}

    /**
     * @return Returns the valorAcumuladoStr.
     */
    public String getValorAcumuladoStr() {
        return valorAcumuladoStr;
    }
    /**
     * @param valorAcumuladoStr The valorAcumuladoStr to set.
     */
    public void setValorAcumuladoStr(String valorAcumuladoStr) {
        this.valorAcumuladoStr = valorAcumuladoStr;
    }
    /**
     * @return Returns the valorContratoStr.
     */
    public String getValorContratoStr() {
        return valorContratoStr;
    }
    /**
     * @param valorContratoStr The valorContratoStr to set.
     */
    public void setValorContratoStr(String valorContratoStr) {
        this.valorContratoStr = valorContratoStr;
    }
	/**
	 * @return Returns the codigoTipoContrato.
	 */
	public String getCodigoTipoContrato() {
		return codigoTipoContrato;
	}
	/**
	 * @param codigoTipoContrato The codigoTipoContrato to set.
	 */
	public void setCodigoTipoContrato(String codigoTipoContrato) {
		this.codigoTipoContrato = codigoTipoContrato;
	}
	/**
	 * @return Returns the estadoTemp.
	 */
	public String getEstadoTemp() {
		return estadoTemp;
	}
	/**
	 * @param estadoTemp The estadoTemp to set.
	 */
	public void setEstadoTemp(String estadoTemp) {
		this.estadoTemp = estadoTemp;
	}
	/**
	 * @return Returns the upc.
	 */
	public String getUpc() {
		return upc;
	}
	/**
	 * @param upc The upc to set.
	 */
	public void setUpc(String upc) {
		this.upc = upc;
	}
	/**
	 * @return Returns the codigoTipoPago.
	 */
	public String getCodigoTipoPago() {
		return codigoTipoPago;
	}
	/**
	 * @param codigoTipoPago The codigoTipoPago to set.
	 */
	public void setCodigoTipoPago(String codigoTipoPago) {
		this.codigoTipoPago = codigoTipoPago;
	}

	/**
	 * @return Returns the pyp.
	 */
	public String getPyp() {
		return pyp;
	}
	/**
	 * @param pyp The pyp to set.
	 */
	public void setPyp(String pyp) {
		this.pyp = pyp;
	}
	/**
	 * @return Returns the contratoSecretaria.
	 */
	public String getContratoSecretaria() {
		return contratoSecretaria;
	}
	/**
	 * @param contratoSecretaria The contratoSecretaria to set.
	 */
	public void setContratoSecretaria(String contratoSecretaria) {
		this.contratoSecretaria = contratoSecretaria;
	}
	/**
	 * @return Returns the nivelAtencionMap.
	 */
	public HashMap getNivelAtencionMap() {
		return nivelAtencionMap;
	}
	/**
	 * @param nivelAtencionMap The nivelAtencionMap to set.
	 */
	public void setNivelAtencionMap(HashMap nivelAtencionMap) {
		this.nivelAtencionMap = nivelAtencionMap;
	}
	/**
	 * @return Returns the nivelAtencionMap.
	 */
	public String getNivelAtencionMap(String key) {
		return nivelAtencionMap.get(key).toString();
	}
	/**
	 * @param nivelAtencionMap The nivelAtencionMap to set.
	 */
	public void setNivelAtencionMap(String key, String valor) {
		this.nivelAtencionMap.put(key, valor);
	}
	/**
	 * @param nivelAtencionMap The nivelAtencionMap to set.
	 */
	public int getNumeroRegistrosNivelAtencionMap() 
	{
		if(this.getNivelAtencionMap().containsKey("numRegistros"))
			return Integer.parseInt(this.getNivelAtencionMap("numRegistros"));
		else
			return 0;
	}
	/**
	 * @return Returns the codigoNivelAtencion.
	 */
	public String getConsecutivoNivelAtencion() {
		return consecutivoNivelAtencion;
	}
	/**
	 * @param codigoNivelAtencion The codigoNivelAtencion to set.
	 */
	public void setConsecutivoNivelAtencion(String codigoNivelAtencion) {
		this.consecutivoNivelAtencion = codigoNivelAtencion;
	}
	

	/**
	 * @return Returns the indexToDelete.
	 */
	public String getIndexToDelete() {
		return indexToDelete;
	}



	/**
	 * @param indexToDelete The indexToDelete to set.
	 */
	public void setIndexToDelete(String indexToDelete) {
		this.indexToDelete = indexToDelete;
	}

	/**
	 * @return Returns the nombreTipoPago.
	 */
	public String getNombreTipoPago() {
		return nombreTipoPago;
	}

	/**
	 * @param nombreTipoPago The nombreTipoPago to set.
	 */
	public void setNombreTipoPago(String nombreTipoPago) {
		this.nombreTipoPago = nombreTipoPago;
	}

	/**
	 * @return Returns the busquedaNivelAtencion.
	 */
	public String getBusquedaNivelAtencion() {
		return busquedaNivelAtencion;
	}

	/**
	 * @param busquedaNivelAtencion The busquedaNivelAtencion to set.
	 */
	public void setBusquedaNivelAtencion(String busquedaNivelAtencion) {
		this.busquedaNivelAtencion = busquedaNivelAtencion;
	}

	/**
	 * @return Returns the base.
	 */
	public String getBase() {
		return base;
	}

	/**
	 * @param base The base to set.
	 */
	public void setBase(String base) {
		this.base = base;
	}

	/**
	 * @return Returns the nombreBase.
	 */
	public String getNombreBase() {
		return nombreBase;
	}

	/**
	 * @param nombreBase The nombreBase to set.
	 */
	public void setNombreBase(String nombreBase) {
		this.nombreBase = nombreBase;
	}

	/**
	 * @return Returns the porcentajeUpc.
	 */
	public String getPorcentajeUpc() {
		return porcentajeUpc;
	}

	/**
	 * @param porcentajeUpc The porcentajeUpc to set.
	 */
	public void setPorcentajeUpc(String porcentajeUpc) {
		this.porcentajeUpc = porcentajeUpc;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getControlaAnticipos() {
		return controlaAnticipos;
	}
	
	/**
	 * 
	 * @param controlaAnticipos
	 */
	public void setControlaAnticipos(String controlaAnticipos) {
		this.controlaAnticipos = controlaAnticipos;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getDiaLimiteRadicacion() {
		return diaLimiteRadicacion;
	}
	
	/**
	 * 
	 * @param diaLimiteRadicacion
	 */
	public void setDiaLimiteRadicacion(String diaLimiteRadicacion) {
		this.diaLimiteRadicacion = diaLimiteRadicacion;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getDiasRadicacion() {
		return diasRadicacion;
	}
	
	/**
	 * 
	 * @param diasRadicacion
	 */
	public void setDiasRadicacion(String diasRadicacion) {
		this.diasRadicacion = diasRadicacion;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getFechaFirmaContrato() {
		return fechaFirmaContrato;
	}
	
	/**
	 * 
	 * @param fechaFirmaContrato
	 */
	public void setFechaFirmaContrato(String fechaFirmaContrato) {
		this.fechaFirmaContrato = fechaFirmaContrato;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getManejaCobertura() {
		return manejaCobertura;
	}
	
	/**
	 * 
	 * @param manejaCobertura
	 */
	public void setManejaCobertura(String manejaCobertura) {
		this.manejaCobertura = manejaCobertura;
	}

	/**
	 * @return the requiereAutorizacionNoCobertura
	 */
	public String getRequiereAutorizacionNoCobertura() {
		return requiereAutorizacionNoCobertura;
	}

	/**
	 * @param requiereAutorizacionNoCobertura the requiereAutorizacionNoCobertura to set
	 */
	public void setRequiereAutorizacionNoCobertura(
			String requiereAutorizacionNoCobertura) {
		this.requiereAutorizacionNoCobertura = requiereAutorizacionNoCobertura;
	}

	public HashMap getEsquemasInventario() {
		return esquemasInventario;
	}

	public void setEsquemasInventario(HashMap esquemasInventario) {
		this.esquemasInventario = esquemasInventario;
	}

	public HashMap getEsquemasProcedimientos() {
		return esquemasProcedimientos;
	}

	public void setEsquemasProcedimientos(HashMap esquemasProcedimientos) {
		this.esquemasProcedimientos = esquemasProcedimientos;
	}
	public Object getEsquemasInventario(String key) {
		return esquemasInventario.get(key);
	}

	public void setEsquemasInventario(String key,Object value) {
		this.esquemasInventario.put(key, value);
	}

	public Object getEsquemasProcedimientos(String key) {
		return esquemasProcedimientos.get(key);
	}

	public void setEsquemasProcedimientos(String key,Object value) {
		this.esquemasProcedimientos.put(key, value);
	}

	public Logger getLogger() {
		return logger;
	}

	public void setLogger(Logger logger) {
		this.logger = logger;
	}

	public String getAccion() {
		return accion;
	}

	public void setAccion(String accion) {
		this.accion = accion;
	}

	public HashMap getEsquemasInventarioEliminados() {
		return esquemasInventarioEliminados;
	}

	public void setEsquemasInventarioEliminados(HashMap esquemasInventarioEliminados) {
		this.esquemasInventarioEliminados = esquemasInventarioEliminados;
	}

	public HashMap getEsquemasProcedimientosEliminados() {
		return esquemasProcedimientosEliminados;
	}

	public void setEsquemasProcedimientosEliminados(
			HashMap esquemasProcedimientosEliminados) {
		this.esquemasProcedimientosEliminados = esquemasProcedimientosEliminados;
	}

	public int getPosEliminar() {
		return posEliminar;
	}

	public String getSinContrato() {
		return sinContrato;
	}

	public void setSinContrato(String sinContrato) {
		this.sinContrato = sinContrato;
	}

	public void setPosEliminar(int posEliminar) {
		this.posEliminar = posEliminar;
	}

	public String getEsquemaTarInventario() {
		return esquemaTarInventario;
	}

	public void setEsquemaTarInventario(String esquemaTarInventario) {
		this.esquemaTarInventario = esquemaTarInventario;
	}

	public String getEsquemaTarProcedimiento() {
		return esquemaTarProcedimiento;
	}

	public void setEsquemaTarProcedimiento(String esquemaTarProcedimiento) {
		this.esquemaTarProcedimiento = esquemaTarProcedimiento;
	}

	/**
	 * @return the esContratoUsado
	 */
	public boolean isEsContratoUsado() {
		return esContratoUsado;
	}

	/**
	 * @param esContratoUsado the esContratoUsado to set
	 */
	public void setEsContratoUsado(boolean esContratoUsado) {
		this.esContratoUsado = esContratoUsado;
	}

	
	
	
	/** frmObservaciones  */
	public String getFrmObservaciones() {	return frmObservaciones;	}
	public void setFrmObservaciones(String frmObservaciones) {	this.frmObservaciones = frmObservaciones;	}

	/**
	 * @return the cadenaCodigoConvenio
	 */
	public String getCadenaCodigoConvenio() {
		return cadenaCodigoConvenio;
	}

	/**
	 * @param cadenaCodigoConvenio the cadenaCodigoConvenio to set
	 */
	public void setCadenaCodigoConvenio(String cadenaCodigoConvenio) {
		this.cadenaCodigoConvenio = cadenaCodigoConvenio;
		  
		
	}

	/**
	 * @return the tipoAtencionConvenio
	 */
	public String getTipoAtencionConvenio() {
		
           String[] convenioCont=cadenaCodigoConvenio.split(ConstantesBD.separadorSplit); 
		
		if(convenioCont.length > 1){
		return convenioCont[1];
		}else{
			return ConstantesIntegridadDominio.acronimoTipoAtencionConvenioMedicoGeneral;
		}
		
	}

	/**
	 * @param tipoAtencionConvenio the tipoAtencionConvenio to set
	 */
	public void setTipoAtencionConvenio(String tipoAtencionConvenio) {
		this.tipoAtencionConvenio = tipoAtencionConvenio;
	}

	/**
	 * @return the dtoControlAnticipo
	 */
	public DtoControlAnticiposContrato getDtoControlAnticipo() {
		return dtoControlAnticipo;
	}

	/**
	 * @param dtoControlAnticipo the dtoControlAnticipo to set
	 */
	public void setDtoControlAnticipo(DtoControlAnticiposContrato dtoControlAnticipo) {
		this.dtoControlAnticipo = dtoControlAnticipo;
	}

	/**
	 * @return the dtoLogControlAnticipo
	 */
	public DtoLogControlAnticipoContrato getDtoLogControlAnticipo() {
		return dtoLogControlAnticipo;
	}

	/**
	 * @param dtoLogControlAnticipo the dtoLogControlAnticipo to set
	 */
	public void setDtoLogControlAnticipo(
			DtoLogControlAnticipoContrato dtoLogControlAnticipo) {
		this.dtoLogControlAnticipo = dtoLogControlAnticipo;
	}

	/**
	 * @return the listaLogControl
	 */
	public ArrayList<DtoLogControlAnticipoContrato> getListaLogControl() {
		return listaLogControl;
	}

	/**
	 * @param listaLogControl the listaLogControl to set
	 */
	public void setListaLogControl(
			ArrayList<DtoLogControlAnticipoContrato> listaLogControl) {
		this.listaLogControl = listaLogControl;
	}

	public String getPacientePagaAtencion() {
		return pacientePagaAtencion;
	}

	public void setPacientePagaAtencion(String pacientePagaAtencion) {
		this.pacientePagaAtencion = pacientePagaAtencion;
	}

	public String getValidarAbonoAtencionOdo() {
		return validarAbonoAtencionOdo;
	}

	public void setValidarAbonoAtencionOdo(String validarAbonoAtencionOdo) {
		this.validarAbonoAtencionOdo = validarAbonoAtencionOdo;
	}

	public void setEsConvenioOdontologicio(boolean esConvenioOdontologicio) {
		this.esConvenioOdontologicio = esConvenioOdontologicio;
	}

	public boolean isEsConvenioOdontologicio() {
		return esConvenioOdontologicio;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public boolean isVieneDeIngresarModificar() {
		return vieneDeIngresarModificar;
	}

	public void setVieneDeIngresarModificar(boolean vieneDeIngresarModificar) {
		this.vieneDeIngresarModificar = vieneDeIngresarModificar;
	}

	/**
	 * @return the manejaTarifasXCA
	 */
	public boolean isManejaTarifasXCA() {
		return manejaTarifasXCA;
	}

	/**
	 * @param manejaTarifasXCA the manejaTarifasXCA to set
	 */
	public void setManejaTarifasXCA(boolean manejaTarifasXCA) {
		this.manejaTarifasXCA = manejaTarifasXCA;
	}

	public String getConvenioManejaMontos() {
		return convenioManejaMontos;
	}

	public void setConvenioManejaMontos(String convenioManejaMontos) {
		this.convenioManejaMontos = convenioManejaMontos;
	}
	
	
	
	
}
