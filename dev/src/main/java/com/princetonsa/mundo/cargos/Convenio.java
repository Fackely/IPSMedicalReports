/*
 * @(#)Convenio.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_01
 *
 */
package com.princetonsa.mundo.cargos;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.InfoDatosStr;
import util.UtilidadBD;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.dao.ConvenioDao;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.facturacion.DtoConvenio;
import com.princetonsa.dto.facturacion.DtoMediosAutorizacion;
import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.fwk.exception.IPSException;
import com.servinte.axioma.fwk.exception.util.CODIGO_ERROR_NEGOCIO;

/**
 * Clase para el manejo de convenios
 * @version 1.0, Abril 29, 2004
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Rï¿½os</a>
 */
public class Convenio 
{
	/**
	 * @param documentosAdjuntosGenerados the documentosAdjuntosGenerados to set
	 */
	public void setDocumentosAdjuntosGenerados(Map documentosAdjuntosGenerados) {
		this.documentosAdjuntosGenerados = documentosAdjuntosGenerados;
	}

	/**
	 * @return the numDocumentosAdjuntos
	 */
	public int getNumDocumentosAdjuntos() {
		return numDocumentosAdjuntos;
	}

	/**
	 * @param numDocumentosAdjuntos the numDocumentosAdjuntos to set
	 */
	public void setNumDocumentosAdjuntos(int numDocumentosAdjuntos) {
		this.numDocumentosAdjuntos = numDocumentosAdjuntos;
	}

	/**
	 * @return the documentosAdjuntosGenerados
	 */
	public Map getDocumentosAdjuntosGenerados() {
		return documentosAdjuntosGenerados;
	}

	/**
	 * medios de autorizacion
	 */
	public static String[] mediosAutorizacionVector= { ConstantesIntegridadDominio.acronimoTelefonica, ConstantesIntegridadDominio.acronimoFax, ConstantesIntegridadDominio.acronimoOtro, ConstantesIntegridadDominio.acronimoInternet };
	
	
	/**
	 * Interfaz para acceder y comunicarse con la fuente de datos
	 */
	private static ConvenioDao convenioDao;
	
	/**
	 * Para hacer logs de debug / warn / error de esta funcionalidad.
	 */
	private static Logger logger = Logger.getLogger(Convenio.class);
		
	/**
	 * Cï¿½digo del convenio
	 */
	private int codigo;
		
	/**
	 * cï¿½digo de la empresa (tabla empresas - estado activo)
	 */
	private int empresa;
		
	/**
	 * Campo para capturar el tipo de rï¿½gimen previamente
	 *  creado en el sistema
	 */
	private String tipoRegimen;
	private String descripcionTipoRegimen; //se usa para la carga
		
	/**
	 * Nombre del convenio
	 */
	private String nombre;
		
	/**
	 * Observaciones del convenio
	 */
	private String  observaciones;
		
	/**
	 * Descripciï¿½n del plan de beneficios
	 */
	private String planBeneficios;
		
	/**
	 * Cï¿½digo Minsalud
	 */
	private  String codigoMinSalud;
		
	/**
	 * Permite seleccionar el formato de la factura
	 * que utiliza el convenio (previamente definido en el sistema)
	 */
	private int formatoFactura;

	/**
	 * Dice si el convenio estï¿½ activo en el sistema o no.
	 */		
	private boolean activa;
	
	private boolean convenioManejaMontoCobro;
	
	/**
	 * Aux para poder modificar el tipo de Regimen
	 */
	private String tipoRegimenNuevo;
	
	/**
	 * Aux para modificar el cod de empresa 
	 */
	private int empresaNuevo;
	
	/**
	 * Aux para modificar formato Factura
	 */
	private int formatoFacturaNuevo;
	

	/**
	 * Auxiliar del campo boolean activa, para poder mandar
	 * un nuevo valor diferente de true o false en la bï¿½squeda
	 * avanzada
	 */
	private int activaAux;
	
	/**
	 * Para la bï¿½squeda (contiene razonSocial-empresa)
	 */
	private String razonSocial;
	
	/**
	 * Para la bï¿½squeda (contiene tipo rï¿½gimen)
	 */
	private String nombreTipoRegimen;
	
	/**
	 * Para la bï¿½squeda contiene Formato factura
	 */
	private String nombreFormatoFactura;
	
	/**
	 * Tipo de contrato (Capitado / Normal) 
	 */
	private int tipoContrato;
	
	/**
	 * Para la bï¿½squeda
	 * Contiene el nombre del tipo de contrato
	 * (table : tipos_contrato - campo: nombre)
	 */																
	private String nombreTipoContrato; 
	
	/**
	 * Dice si la info adicional en apertura cuenta paciente esta activa o n
	 */	
	private String checkInfoAdicCuenta;
	
	/**
	 * pyp
	 */
	private String pyp;
	
	/**
	 * Por medio de ï¿½ste campo se permite identificar si se unifica el cobro de actividades de pyp, con servicios No PyP en las solicitudes.
	 */
	private String unificarPyp;

	/**
	 * Auxiliar del campo boolean pyp, para poder mandar
	 * un nuevo valor diferente de true o false en la bï¿½squeda
	 * avanzada
	 */
	private int pypAux;
	
	private String numeroDiasVencimiento;
	private String requiereJustificacionServicios;
	private String requiereJustificacionArticulos;
	
	/**
	 * Requiere Justificacion de artï¿½culos No POS diferentes a los medicamentos (S/N)
	 */
	private String requiereJustArtNoposDifMed;
	
	private String manejaComplejidad;
	private String semanasMinimasCotizacion;
	private String requiereNumeroCarnet;

	private String tipoConvenio;
	private int tipoCodigo;
	private int tipoCodigoArt;
	private int clasificacionTipoConvenio;
	
	private String ajusteServicios;
	private String ajusteArticulos;
	private String interfaz;
	
	private String empresaInstitucion;
	
	
	 /**
	  * @param Cantidad max cirugia adicionales 
	  * */
	private String cantidadMaxCirugia;
	 
	 /**
	  * @param Cantidad Max ayudantes que paga 
	  * */
	private String cantidadMaxAyudpag;
	
	/**
	 * @param Tipo de Liquidacion Salas Cirugia
	 * */
	private String tipoLiquidacionScx;
	
	/**
	 * @param Tipo de Liquidacion Salas No Cruentos
	 * */
	private String tipoLiquidacionDyt;
	
	/**
	 * @param Tipo de Liquidacion General Cirugias
	 * */
	private String tipoLiquidacionGcx;
	
	/**
	 * @param Tipo de Liquidacion General No Cruetos
	 * */
	private String tipoLiquidacionGdyt;
	
	/**
	 * @param Tipo de Tarifa para liquidacion materiales Cirugia
	 * */
	private String tipoTarifaLiqMateCx;
	
	/**
	 * @param Tipo de Tarifa para liquidacion materiales No Cruentos
	 * */
	private String tipoTarifaLiqMateDyt;
	
	/**
	 * @param Tipo de Fecha liquidacion Tiempos Cirugia
	 * */
	private String tipoFechaLiqTiemPcx;
	
	/**
	 * @param Tipo de Fecha para Liquidacion Tiempos No Cruentos
	 * */
	private String tipoFechaLiqTiempDyt;
	
	/**
	 * @param Liquidacion de Tiempos x Fraccion Adicional Cumplida
	 * */
	private String liquidacionTmpFracAdd;
	
	
	private ArrayList planEspecialList;
	
	/**
	 * 
	 */
	private int institucion;
	
	/**
	 * 
	 */
	private int planEspecial;
	
	
	/**
	 * Variable para capturar el excento deudor
	 */
	private String excentoDeudor;
	
	/**
	 * Variable para Capturar el Excento Documento de Garantia
	 */
	private String excentoDocumentoGarantia;
	
	/**
	 * Variable para Capturar el VIP
	 */
	private String vip;
		
	/**
	 * Variable para Radicar Cuentas Negativas
	 * */
	private String radicarCuentasNegativas;
	
	/**
	 * Variables para Asignar en la Factura Como Valor del Paciente el Valor de Abonos
	 * */
	private String asignarFactValorPacValorAbono;
	
	
	//INDICADORES DEL TIPO DE REGIMEN
	private boolean requiereDeudor;
	private boolean requiereVerificacionDerechos;
	
	/**
	 * 
	 */
	private String encabezadoFactura;
	
	/**
	 * 
	 */
	private String pieFactura;
	
	private String numeroIdentificacion;
	
	/**
	 * Variable para captura de Dias control Post Operatorio
	 */
	private String diasCPO;
	
	/**
	 * Variable para captura de cantidad maxima citas post operatorio
	 */
	private String cantCPO;
	
	/**
	 * Variable String para manejar si el Campo Aseguradora es S ï¿½ N ? 
	 */
	private String aseguradora;
	
	/**
	 * Variable String para manejar almacenar el Cï¿½digo Aseguradora 
	 */
	private String codigoAseguradora;
	
	/**
	 * Variable para manejar el Valor Letras Factura
	 */
	private String valorLetrasFactura;
	
	
	//********************** Cambios ANEXO 753 decreto 4747 *******************************
	
	/**
	* Especifica si se realiza Reporte de inconsistencias Base de datos
	 */
	private String reporte_incons_bd;
	
	/**
	 * Especifica si se realiza Reporte de antencion inicial de Urgencias
	 */
	private String reporte_atencion_ini_urg;
	
	/**
	 * Especifica si se Genera Automanticamente la valoracion de Atencion Inicial de Urgencias 
	 */
	private String generacion_autom_val_urg;
	
	/**
	 * Especifica si Requeire autorizacion para servicios adicionales
	 */
	
	private String requiere_autorizacion_servicio;
	
	/**
	 * Almacena los medios de envio del informe
	 */
	private String mediosEnvio [] = new String [3];
	
	/**
	 * Alamacena los correos electronicos asociados al Convenio
	 */
	private HashMap correosElectronicos = new HashMap ();	

	//****************************************************************************************
	
	//********************** Cambios ANEXO 791 ***********************************************
	/*--------------------------------------------------------------------------
	 *    PROPIEDADES PARA MANEJAR LAS MULTAS POR INCUMPLIMIENTO DE CITAS
	 ---------------------------------------------------------------------------*/
	
	/**
	 * Variable para manejar si el convenio Maneja Multas Por Incumplimiento
	 */
	private String manejaMultasPorIncumplimiento;
	
	/**
	 * Variable para manejar el valor Multa Por Incumplimiento Citas
	 */
	private String valorMultaPorIncumplimientoCitas;
	
//	****************************************************************************************
	
//	******** CAMBIOS ANEXO 809 **********
	/*---------------------------------------------
	 * PROPIEDADES MANEJO CENTROS COSTO CONTABLE
	 *---------------------------------------------*/
	
	/**
	 * Variable para Almacenar Los Centro de Costo Contable
	 */
	private ArrayList ccContableList; 
	
	/**
	 * Variable para Capturar el Centro Costo Contable
	 */
	private int ccContable;
	
	/**
	 * Variable para Capturar el Centro de atenciï¿½n Contable
	 */
	private int cenAtencContable;
	
	/**
	 * Especifica el formato de las autorizaciones 
	 */
	private String formato_autorizacion;
	
	/**
	 * resetea los datos pertinentes al registro de empresa
	 */
	
	private String manejaBonos;
	private String requiereBono;
	private String manejaPromociones;
	private String esTargetaCliene;
	private String manejaDescuentoOdontologico;
	
	private String ingresoBdValido;
	private String ingresoPacienteReqAutorizacion;
	private String requiereIngresoValorAuto;
	private String reqIngresoValido;
	private String tipoAtencion;
	private ArrayList<DtoMediosAutorizacion> mediosAutorizacion;
	private boolean ambos;
	
	
	/**
	 * Colecciï¿½n con los nombres generados de los archivos adjuntos 
	 */
	private Map documentosAdjuntosGenerados = new HashMap(); 
	
	/**
	 * Nï¿½mero de documentos adjuntos
	 */
	private int numDocumentosAdjuntos = 0;
	
	private Contrato contrato;
	
	
	private String tipoLiquidacionPool;
	
	private String capitacionSubcontratada;
	
	private String manejaPresupCapitacion;
	


	public void reset()
	{
		this.codigo = 0;
		this.empresa=0;
		this.tipoRegimen="";
		this.descripcionTipoRegimen = "";
		this.nombre="";
		this.observaciones= "";
		this.planBeneficios="";
		this.codigoMinSalud="";
		this.formatoFactura=0;
		this.activa= false;
		this.convenioManejaMontoCobro=false;
		this.checkInfoAdicCuenta="";
		this.pyp="";
		this.unificarPyp="";
		
		this.empresaNuevo=0;
		this.tipoRegimenNuevo="";
		this.formatoFacturaNuevo=0;
		
		this.activaAux=0;
		this.pypAux=0;
		this.nombreFormatoFactura="";
		this.nombreTipoRegimen="";
		this.razonSocial="";
		
		this.tipoContrato= ConstantesBD.codigoNuncaValido;
		
		this.nombreTipoContrato="";
		this.numeroDiasVencimiento="";
		this.requiereJustificacionServicios="";
		this.requiereJustificacionArticulos="";
		this.requiereJustArtNoposDifMed="";
		this.manejaComplejidad="";
		this.semanasMinimasCotizacion="";
		this.requiereNumeroCarnet="";
		this.tipoConvenio=ConstantesBD.codigoNuncaValido+"";
		this.tipoCodigo=ConstantesBD.codigoNuncaValido;
		this.tipoCodigoArt=ConstantesBD.codigoNuncaValido;
		this.institucion=ConstantesBD.codigoNuncaValido;
		this.clasificacionTipoConvenio = 0;
		
		this.ajusteServicios="";
		this.ajusteArticulos="";
		this.interfaz="";
		
		this.requiereDeudor = false;
		this.requiereVerificacionDerechos = false;
		
		this.cantidadMaxCirugia = "";
		this.cantidadMaxAyudpag = "";
		this.tipoLiquidacionScx = "";
		this.tipoLiquidacionDyt = "";
		this.tipoLiquidacionGcx = "";
		this.tipoLiquidacionGdyt = "";
		this.tipoTarifaLiqMateCx = "";
		this.tipoTarifaLiqMateDyt = "";
		this.tipoFechaLiqTiemPcx = "";
		this.tipoFechaLiqTiempDyt = "";
		this.liquidacionTmpFracAdd = "";
		
		this.encabezadoFactura="";
		this.pieFactura="";
		this.empresaInstitucion=ConstantesBD.codigoNuncaValido+"";
		
		this.numeroIdentificacion = "";
		
		this.planEspecial=ConstantesBD.codigoNuncaValido;
		this.excentoDeudor= ConstantesBD.acronimoNo;
		this.excentoDocumentoGarantia= ConstantesBD.acronimoNo;
		this.vip= ConstantesBD.acronimoNo; 
		this.radicarCuentasNegativas = ConstantesBD.acronimoNo;
		this.asignarFactValorPacValorAbono = ConstantesBD.acronimoNo;
		this.aseguradora = ConstantesBD.acronimoNo;
		this.codigoAseguradora = "";
		this.valorLetrasFactura="";
		
		//*************	Cambios Anexo 753 Decreto 4747******************
		this.reporte_atencion_ini_urg=ConstantesBD.acronimoNo;
		this.reporte_incons_bd=ConstantesBD.acronimoNo;
		this.generacion_autom_val_urg=ConstantesBD.acronimoNo;
		this.mediosEnvio[0]=new String("");
		this.mediosEnvio[1]=new String("");
	    this.mediosEnvio[2]=new String("");
	    this.correosElectronicos=new HashMap();
	    this.correosElectronicos.put("numRegistros","0");
	    this.requiere_autorizacion_servicio=ConstantesBD.acronimoNo;
	    this.formato_autorizacion=ConstantesIntegridadDominio.acronimoFormatoEstandar;
	//*************************************************************
	    
	    //*************	Cambios Anexo 791 ******************************
		this.manejaMultasPorIncumplimiento="";
		this.valorMultaPorIncumplimientoCitas = "";
	    //*************************************************************
		
//		********* ANEXO 809 **********
	    this.ccContable = ConstantesBD.codigoNuncaValido;
	    this.ccContableList = new ArrayList();
	    
	    this.cenAtencContable=ConstantesBD.codigoNuncaValido;
	    
	    this.manejaBonos="";
		this.requiereBono = "";
		this.manejaPromociones = "";
		this.esTargetaCliene = "";
		this.ingresoBdValido = "";
		this.ingresoPacienteReqAutorizacion = "";
		this.reqIngresoValido = "";
		
	    this.numDocumentosAdjuntos = 0;
	    this.documentosAdjuntosGenerados.clear();
	    
	    this.contrato=new Contrato();
	    
	    this.tipoLiquidacionPool="";
	    this.manejaDescuentoOdontologico="";
	    
	    this.manejaPresupCapitacion="";
	    this.capitacionSubcontratada="";
		
		
		initMediosAutorizacion();
		
	}
	
	/**
	 * 
	 */
	private void initMediosAutorizacion() {
		
		this.mediosAutorizacion=new ArrayList<DtoMediosAutorizacion>();
		DtoMediosAutorizacion DtoAut;
		
		for(int i=0;i< mediosAutorizacionVector.length ;i++ ){
			
		DtoAut = new DtoMediosAutorizacion();
		
		DtoAut.setTipo(new InfoDatosStr(mediosAutorizacionVector[i], ValoresPorDefecto.getIntegridadDominio(mediosAutorizacionVector[i]).toString() ));
		this.mediosAutorizacion.add(DtoAut);
		}
				
	}
	
	public void resetOdontologia()
	{
		this.codigo = 0;
		this.empresa=0;
		this.tipoRegimen="";
		this.descripcionTipoRegimen = "";
		this.nombre="";
		this.observaciones= "";
		this.planBeneficios="";
		this.codigoMinSalud="";
		this.formatoFactura=0;
		this.activa= false;
		this.convenioManejaMontoCobro=false;
		this.checkInfoAdicCuenta="false";
		this.pyp="false";
		this.unificarPyp="false";
		
		this.empresaNuevo=0;
		this.tipoRegimenNuevo="";
		this.formatoFacturaNuevo=0;
		
		this.activaAux=0;
		this.pypAux=0;
		this.nombreFormatoFactura="";
		this.nombreTipoRegimen="";
		this.razonSocial="";
		
		this.tipoContrato=ConstantesBD.codigoTipoContratoEvento;
		
		this.nombreTipoContrato="";
		this.numeroDiasVencimiento="";
		this.requiereJustificacionServicios="";
		this.requiereJustificacionArticulos="";
		this.requiereJustArtNoposDifMed="";
		this.manejaComplejidad="";
		this.semanasMinimasCotizacion="";
		this.requiereNumeroCarnet="";
		this.tipoConvenio=ConstantesBD.codigoNuncaValido+"";
		this.tipoCodigo=ConstantesBD.codigoNuncaValido;
		this.tipoCodigoArt=ConstantesBD.codigoNuncaValido;
		this.institucion=ConstantesBD.codigoNuncaValido;
		this.clasificacionTipoConvenio = 0;
		
		this.ajusteServicios="";
		this.ajusteArticulos="";
		this.interfaz="";
		
		this.requiereDeudor = false;
		this.requiereVerificacionDerechos = false;
		
		this.cantidadMaxCirugia = "";
		this.cantidadMaxAyudpag = "";
		this.tipoLiquidacionScx = "";
		this.tipoLiquidacionDyt = "";
		this.tipoLiquidacionGcx = "";
		this.tipoLiquidacionGdyt = "";
		this.tipoTarifaLiqMateCx = "TARIFA";
		this.tipoTarifaLiqMateDyt = "TARIFA";
		this.tipoFechaLiqTiemPcx = "";
		this.tipoFechaLiqTiempDyt = "";
		this.liquidacionTmpFracAdd = "N";
		
		this.encabezadoFactura="";
		this.pieFactura="";
		this.empresaInstitucion=ConstantesBD.codigoNuncaValido+"";
		
		this.numeroIdentificacion = "";
		
		this.planEspecial=ConstantesBD.codigoNuncaValido;
		this.excentoDeudor= ConstantesBD.acronimoSi;
		this.excentoDocumentoGarantia= ConstantesBD.acronimoSi;
		this.vip= ConstantesBD.acronimoNo; 
		this.radicarCuentasNegativas = ConstantesBD.acronimoNo;
		this.asignarFactValorPacValorAbono = ConstantesBD.acronimoNo;
		this.aseguradora = ConstantesBD.acronimoNo;
		this.codigoAseguradora = "";
		this.valorLetrasFactura="";
		
		//*************	Cambios Anexo 753 Decreto 4747******************
		this.reporte_atencion_ini_urg=ConstantesBD.acronimoNo;
		this.reporte_incons_bd=ConstantesBD.acronimoNo;
		this.generacion_autom_val_urg=ConstantesBD.acronimoNo;
		this.mediosEnvio[0]=new String("");
		this.mediosEnvio[1]=new String("");
	    this.mediosEnvio[2]=new String("");
	    this.correosElectronicos=new HashMap();
	    this.correosElectronicos.put("numRegistros","0");
	    this.requiere_autorizacion_servicio=ConstantesBD.acronimoNo;
	    this.formato_autorizacion=ConstantesIntegridadDominio.acronimoFormatoEstandar;
	//*************************************************************
	    
	    //*************	Cambios Anexo 791 ******************************
		this.manejaMultasPorIncumplimiento="N";
		this.valorMultaPorIncumplimientoCitas = "";
	    //*************************************************************
		
//		********* ANEXO 809 **********
	    this.ccContable = ConstantesBD.codigoNuncaValido;
	    this.ccContableList = new ArrayList();
	    
	    
	    this.manejaBonos="";
		this.requiereBono = "";
		this.manejaPromociones = "";
		this.esTargetaCliene = "";
		this.ingresoBdValido = "";
		this.ingresoPacienteReqAutorizacion = "";
		this.reqIngresoValido = "";
		
		this.numDocumentosAdjuntos = 0;
	    this.documentosAdjuntosGenerados.clear();
		initMediosAutorizacion();
       
	}
	
	/**
	 * 
	 * */
	public static ConvenioDao getConvenioDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConvenioDao();
	    //*************************************************************	    
	}
	
	/**
	 * Constructor de la clase, inicializa los atributos con los datos correspondientes.
	 * @param codigo, int, cï¿½digo del convenio
	 * @param empresa, int, cï¿½digo de la empresa (tabla empresas - estado activo)
	 * @param tipoRegimen, String, rï¿½gimen de acuerdo a los previam/t
	 * 				ingresados en el sistema
	 * @param nombre, String, nombre del convenio
	 * @param observaciones, String, observaciones del convenio 
	 * @param planBeneficios, String, descripciï¿½n del plan de beneficios
	 * @param codigoMinSalud, String, codigo Minsalud
	 * @param formatoFactura, int, selecciona el tipo de formato de factura 
	 * 				que utiliza el convenio
	 * @param activo, boolean, si el convenio estï¿½ activo en el sistema o no
	 * 
	 * @param Cantidad max cirugia adicionales
	 * @param Cantidad Max ayudantes que paga
	 * @param Tipo de Liquidacion Salas Cirugia
	 * @param Tipo de Liquidacion Salas No Cruentos
	 * @param Tipo de Liquidacion General Cirugias
	 * @param Tipo de Liquidacion General No Cruetos
	 * @param Tipo de Tarifa para liquidacion materiales Cirugia
	 * @param Tipo de Tarifa para liquidacion materiales No Cruentos
	 * @param Tipo de Fecha liquidacion Tiempos Cirugia
	 * @param Tipo de Fecha para Liquidacion Tiempos No Cruentos
	 * @param Liquidacion de Tiempos x Fraccion Adicional Cumplida
	 */
	public Convenio(	int codigo,	
						int empresa,
						String tipoRegimen,
						String nombre,
						String observaciones,
						String planBeneficios,
						String codigoMinSalud,
						int formatoFactura,
						boolean activa,
						boolean convenioManejaMontoCobro,
						int tipoContrato,
						String pyp, 
						String unificarPyp,
						String numDiasVencimiento,
						String requiereJustificacionServicios,
						String requiereJustificacionArticulos,
						String requiereJustArtNoposDifMed,
						String manejaComplejidad,
						String semanasMinCotizacion,
						String reqNumeroCarnet,
						String tipoConvenio,
						int tipoCodigo,
						int tipoCodigoArt,
						String ajusteServicios,
						String ajusteArticulos,
						String interfaz,
						int institucion,
						
						String cantidadMaxCirugia,
						String cantidadMaxAyudpag,
						String tipoLiquidacionScx,
						String tipoLiquidacionDyt,
						String tipoLiquidacionGcx,
						String tipoLiquidacionGdyt,
						String tipoTarifaLiqMateCx,
						String tipoTarifaLiqMateDyt,
						String tipoFechaLiqTiemPcx,
						String tipoFechaLiqTiempDyt,
						String liquidacionTmpFracAdd,
						int planEspecial,
						String excentoDeudor,
						String excentoDocumentoGarantia,
						String encabezadoFactura,
						String pieFactura,
						//*************	Cambios Anexo 753 Decreto 4747******************
						String repInconsistBD,
						String repAtencIniUrg,
						String generAutoValAteniniUrg,
						String[] medEnvio,
						HashMap correos,
						String requiereAutorServicio,
						String formatoAutorizacion,
						String tipoLiquidacionPool
						//****************************************************************
						)
	{
			this.codigo=codigo;
			this.empresa=empresa;
			this.tipoRegimen=tipoRegimen;
			this.nombre=nombre;
			this.observaciones=observaciones;
			this.planBeneficios=planBeneficios;
			this.codigoMinSalud= codigoMinSalud;
			this.formatoFactura= formatoFactura;
			this.activa=activa;
			this.convenioManejaMontoCobro=convenioManejaMontoCobro;
			this.tipoContrato= tipoContrato;
			this.pyp=pyp;
			this.unificarPyp=unificarPyp;
			this.numeroDiasVencimiento=numDiasVencimiento;
			this.requiereJustificacionServicios=requiereJustificacionServicios;
			this.requiereJustificacionArticulos=requiereJustificacionArticulos;
			this.requiereJustArtNoposDifMed=requiereJustArtNoposDifMed;
			this.manejaComplejidad=manejaComplejidad;
			this.semanasMinimasCotizacion=semanasMinCotizacion;
			this.requiereNumeroCarnet=reqNumeroCarnet;
			this.tipoConvenio=tipoConvenio;
			this.tipoCodigo=tipoCodigo;
			this.tipoCodigoArt=tipoCodigo;
			this.ajusteServicios=ajusteServicios;
			this.ajusteArticulos=ajusteArticulos;
			this.interfaz=interfaz;
			this.institucion=institucion;			
			this.cantidadMaxCirugia = cantidadMaxCirugia;
			this.cantidadMaxAyudpag = cantidadMaxAyudpag;
			this.tipoLiquidacionScx = tipoLiquidacionScx;
			this.tipoLiquidacionDyt = tipoLiquidacionDyt;
			this.tipoLiquidacionGcx = tipoLiquidacionGcx;
			this.tipoLiquidacionGdyt = tipoLiquidacionGdyt;
			this.tipoTarifaLiqMateCx = tipoTarifaLiqMateCx;
			this.tipoTarifaLiqMateDyt = tipoLiquidacionDyt;
			this.tipoFechaLiqTiemPcx = tipoFechaLiqTiemPcx;
			this.tipoFechaLiqTiempDyt = tipoFechaLiqTiempDyt;
			this.liquidacionTmpFracAdd = liquidacionTmpFracAdd;
			this.planEspecial =planEspecial;
			this.excentoDeudor=excentoDeudor;
			this.excentoDocumentoGarantia=excentoDocumentoGarantia;
			this.encabezadoFactura=encabezadoFactura;
			this.pieFactura=pieFactura;
			//**********Anexo 753 Decreto 4747**************************
			this.reporte_incons_bd=repInconsistBD;
			this.reporte_atencion_ini_urg=repAtencIniUrg;
			this.generacion_autom_val_urg=generAutoValAteniniUrg;
			this.mediosEnvio=medEnvio;
			this.correosElectronicos=correos;
			this.requiere_autorizacion_servicio=requiereAutorServicio;
			this.formato_autorizacion=formatoAutorizacion;
			this.tipoLiquidacionPool=tipoLiquidacionPool;
			//***********************************************************	
			this.init (System.getProperty("TIPOBD"));			
	}											

	/**
	 * 
	 * @param mediosAutorizacion
	 * @param con
	 * @return
	 */
	public static boolean insertarMediosAutorizacion(ArrayList<DtoMediosAutorizacion> mediosAutorizacion , Connection con)
	{
		for(DtoMediosAutorizacion i : mediosAutorizacion)
		{
			if(i.isActivo()) 
			{
				if(convenioDao.insertarMedioAutorizacion(i, con)<=0)
					return false;
			}	
		}
		logger.info("***************** SIZE "+ mediosAutorizacion.size());
		return true;
	}
	
	
	/***
	 * 
	 * 
	 * 
	 */
	
	public static boolean existeConvenioOdontologico(String tipoAtencion, int institucion){
		
				
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConvenioDao().existeConvenioOdontologico(tipoAtencion,institucion) ;
		
	}

	/**
	 * 
	 * @param con
	 * @param usuario
	 * @param codConvenio
	 * @param mediosEnvio
	 * @return
	 */
	public static int insertarMediosEnvio(Connection con,UsuarioBasico usuario,	int codConvenio,String[] mediosEnvio)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConvenioDao().insertarMediosEnvio(con, usuario, codConvenio, mediosEnvio);
	}
	
	/**
	 * Metodo que inserta los Correos electronicos del Convenio
	 * @param con
	 * @param usuario
	 * @param codConvenio
	 * @param correos
	 * @param insertarCorreoElectronicoStr 
	 * @return
	 */
	public static int insertarCorreosElectronicos(Connection con, UsuarioBasico usuario,int codConvenio, HashMap correos)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConvenioDao().insertarCorreosElectronicos(con, usuario, codConvenio, correos);
	}
	
	/**
	 * 
	 * @param convenio
	 * @param mediosAutorizacion
	 * @param con
	 * @return
	 */
	public static boolean modificarMediosAutorizacion(int convenio , ArrayList<DtoMediosAutorizacion> mediosAutorizacion , Connection con)
	{
		convenioDao.eliminarMedioAutorizacion(convenio,con);
		return insertarMediosAutorizacion(mediosAutorizacion, con);
	}
	
	/**
	 * Constructor de la clase, inicializa en vacio todos los parï¿½metros
	 */		
	public Convenio()
	{
		reset();
		this.init (System.getProperty("TIPOBD"));
	}
		
	
		
	/**
	 * Mï¿½todo para insertar un convenio
	 * @param con una conexion abierta con una fuente de datos
	 * @param nombre2 
	 * @param nombreOriginal 
	 * @param convenio 
	 * @return nï¿½mero de filas insertadas (1 o 0)
	 * @throws SQLException
	 */
	
	public boolean insertarAdjunto(Connection con, int convenio, String nombreOriginal, String nombre2){
		
		boolean resp1 = convenioDao.insertarDocumentoAdjuntoConvenio(con, convenio,nombreOriginal , nombre2);
		
		logger.info("insertando archivos --->" + resp1);
		
		return resp1;
	}
	public int insertarConvenio(Connection con, UsuarioBasico usuario)  
	{
		int resp1=convenioDao.insertar(	con, usuario, this.empresa,
									this.tipoRegimen, 
									this. nombre, 
		        					this.observaciones,
		        					this.planBeneficios,
		        					this.codigoMinSalud,
		        					this.formatoFactura,
		        					this.activa,
		        					this.convenioManejaMontoCobro,
		        					this.tipoContrato,
		        					this.checkInfoAdicCuenta,
		        					this.pyp,
		        					this.unificarPyp,
		        					this.numeroDiasVencimiento,
		        					this.requiereJustificacionServicios,
		        					this.requiereJustificacionArticulos,
		        					this.requiereJustArtNoposDifMed,
		        					this.manejaComplejidad,
		        					this.semanasMinimasCotizacion,
		        					this.requiereNumeroCarnet,
		        					this.tipoConvenio,
		        					this.tipoCodigo,
		        					this.tipoCodigoArt,
		        					this.ajusteServicios,
		        					this.ajusteArticulos,
		        					this.interfaz,
		        					this.institucion,
		        					Utilidades.convertirAEntero(this.cantidadMaxCirugia),
		        					Utilidades.convertirAEntero(this.cantidadMaxAyudpag),
		        					this.tipoLiquidacionScx,
		        					this.tipoLiquidacionDyt,
		        					this.tipoLiquidacionGcx,
		        					this.tipoLiquidacionGdyt,
		        					this.tipoTarifaLiqMateCx,
		        					this.tipoTarifaLiqMateDyt,
		        					this.tipoFechaLiqTiemPcx,
		        					this.tipoFechaLiqTiempDyt,
		        					this.liquidacionTmpFracAdd,
		        					this.planEspecial,
		        					this.excentoDeudor,
		        					this.excentoDocumentoGarantia,
		        					this.vip,
		        					this.radicarCuentasNegativas,
		        					this.asignarFactValorPacValorAbono,
		        					this.encabezadoFactura,
		        					this.pieFactura,
		        					this.empresaInstitucion,
		        					Utilidades.convertirAEntero(this.diasCPO),
		        					Utilidades.convertirAEntero(this.cantCPO),
		        					this.aseguradora,
		        					this.codigoAseguradora,
		        					this.valorLetrasFactura,
		        					//*****************Anexo 753 Decreto 4747**************************
		        					this.reporte_incons_bd,
		        					this.reporte_atencion_ini_urg,
		        					this.generacion_autom_val_urg,
		        					this.requiere_autorizacion_servicio,
		        					this.formato_autorizacion,
		                            //*****************************************************************
		        					
		        					//*****************Anexo 791 Decreto*******************************
		        					this.manejaMultasPorIncumplimiento,
		        					this.valorMultaPorIncumplimientoCitas,
		                            //*****************************************************************
		        					
//		        					********************** Anexo 809 **********************
		        					this.ccContable ,
		        					this.cenAtencContable, /* anexo 50 */
		        					this.manejaBonos,
		        					this.requiereBono,
		        					this.manejaPromociones,
		        					this.esTargetaCliene, 
		        					this.ingresoBdValido,
		        					this.ingresoPacienteReqAutorizacion,
		        					this.reqIngresoValido,
		        					this.tipoAtencion,
		        					this.tipoLiquidacionPool, //ANEXO 961
		        					this.manejaPresupCapitacion,
		        					this.capitacionSubcontratada
		        					
//		        					*******************************************************
		        					);
		logger.info("********  Ultimo insertado=  ************** "+resp1);
		return resp1;
	}

	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public boolean cargarResumen(Connection con, int codigo) throws IPSException
	{
		boolean band1=false;
		try	{
			Log4JManager.info("Entro a cargar resumen >>> ");			
			band1=convenioDao.cargarResumen(con,codigo, this);
			
			
			this.mediosAutorizacion= convenioDao.cargarMediosAutorizacion(con, codigo);
			
			
			HashMap<String, String> mapaTemporal = getConvenioDao().cargarMediosEnvio(con,codigo);		
			boolean med1=false, med2=false, med3=false;
			for(int i=0;i<Utilidades.convertirAEntero(mapaTemporal.get("numRegistros")+"");i++)
			{		
				HashMap mapa = new HashMap();
				
				if((mapaTemporal.get("medioEnvio_"+i)+"").equals(ConstantesIntegridadDominio.acronimoFax))
				{
					this.mediosEnvio[0]=(mapaTemporal.get("medioEnvio_"+i)+"");
					med1=true;
				}
				if((mapaTemporal.get("medioEnvio_"+i)+"").equals(ConstantesIntegridadDominio.acronimoEmail))
				{
					this.mediosEnvio[1]=(mapaTemporal.get("medioEnvio_"+i)+"");  
				    med2=true;
				}
				if((mapaTemporal.get("medioEnvio_"+i)+"").equals(ConstantesIntegridadDominio.acronimoIntercambioElectDatos))
				{	
					this.mediosEnvio[2]=(mapaTemporal.get("medioEnvio_"+i)+"");
				    med3=true;
				}
			}
			if(!med1)
				this.mediosEnvio[0]="";
			if(!med2)
				this.mediosEnvio[1]="";
			if(!med3)
				this.mediosEnvio[2]="";  
			Log4JManager.info("Medios de envio con vacios>>> cero:"+this.mediosEnvio[0]+" uno:"+this.mediosEnvio[1]+" dos:"+this.mediosEnvio[2]);
			
			this.cenAtencContable=convenioDao.cargarCentroAtencionContable(con, this.getCodigo());
			
			
			 if(band1 )
				 return true;
		    	else
				return false;
		}
		catch(SQLException e)
		{
			Log4JManager.error("Error en cargarResumen de Convenio: ",e);
			return false;
		}
		catch (IPSException ipsme) {
			throw ipsme;
		}
		catch (Exception e) {
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
	
	}
	
	/**
	 * Consulta los medio de envio de un convenio
	 * @param Connection con
	 * @param int convenio
	 * */
	public static ArrayList<HashMap<String,Object>> cargarMediosEnvio(Connection con,int convenio)
	{
		ArrayList<HashMap<String,Object>> array = new ArrayList<HashMap<String,Object>>();
		
		try
		{
			HashMap<String, String> mapaTemporal = getConvenioDao().cargarMediosEnvio(con,convenio);		
		    
			for(int i=0;i<Utilidades.convertirAEntero(mapaTemporal.get("numRegistros")+"");i++)
			{		
				HashMap mapa = new HashMap();
				
				if((mapaTemporal.get("medioEnvio_"+i)+"").equals(ConstantesIntegridadDominio.acronimoFax))
				{
					mapa.put("codigo",ConstantesIntegridadDominio.acronimoFax);
					mapa.put("descripcion","FAX");
				}
				if((mapaTemporal.get("medioEnvio_"+i)+"").equals(ConstantesIntegridadDominio.acronimoEmail))
				{
					mapa.put("codigo",ConstantesIntegridadDominio.acronimoEmail);
					mapa.put("descripcion","Correo Electronico");
				}
				if((mapaTemporal.get("medioEnvio_"+i)+"").equals(ConstantesIntegridadDominio.acronimoIntercambioElectDatos))
				{	
					mapa.put("codigo",ConstantesIntegridadDominio.acronimoIntercambioElectDatos);
					mapa.put("descripcion","Intercambio Electrï¿½nico de Datos (EDI)");
				}
				
				array.add(mapa);	   			   
			}
		}catch (Exception e) {
					
		}
		
		return array;
	}
	
//********************Cambio Anexo 753 Decreto 4747 ****************************************************************************
	public HashMap consultarCorreosElectronicos(Connection con)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConvenioDao().consultarCorreosElectronicos(con,this.codigo);
	}
	
//******************************************************************************************************************************	

	
	
	/**
	 * Consulta la informaciï¿½n de los indicadores del convenio
	 * @param Connection con
	 * @param int codigoConvenio
	 * */
	public void consultarIndicadoresConvenio(Connection con,int codigoConvenio)
	{
		HashMap mapa = new HashMap();
		mapa.put("codigoConvenio",codigoConvenio);
		
		mapa = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConvenioDao().consultarIndicadoresConvenio(con, mapa);
		this.reporte_atencion_ini_urg = mapa.get("reporteAtencionIniUrg").toString();
	    this.generacion_autom_val_urg = mapa.get("genAutoValAteIniUrg").toString();
	    this.reporte_incons_bd = mapa.get("reporteInconBd").toString();
	    this.requiere_autorizacion_servicio=mapa.get("requiereAutorizacion").toString();
	}
	
	//***********************************************************************
	
	/**
	 * Carga el codigo del ultimo convenio insertado
	 * @param con
	 * @return
	 */
	public boolean cargarUltimoCodigo1(Connection con)
	{
		try
		{
			ResultSetDecorator rs=convenioDao.cargarUltimoCodigo(con);
			if(rs.next())
			{
				this.codigo=rs.getInt("codigo");
				return true;
			}
			else
			{
				return false;
			}
		}catch(SQLException e)
		{
			logger.warn("Error mundo convenio" +e.toString());	
			return false;
		}
	}	
	
	/**
	 * Mï¿½todo utilizado en la funcionalidad Modificar convenio
	 * que tiene como finalidad modificar el estado activo, empresa,
	 * tipo regimen ....
	 * @param con
	 * @return
	 * @throws SQLException
	 */
	public int modificarConvenio(Connection con, UsuarioBasico usuario) throws SQLException, IPSException 
	{
			int resp=0;
			logger.info("===>Valor en el mundo Plan Especial>> "+this.planEspecial);
			logger.info("===>Valor en el mundo Aseguradora >> "+this.aseguradora);
			logger.info("===>Valor en el mundo Codigo Aseguradora >> "+this.codigoAseguradora);
			
			// Anexo 1088 V 1.2
			if(this.getTipoAtencion().equals(ConstantesIntegridadDominio.acronimoTipoAtencionConvenioOdontologico))
			{
				this.setConvenioManejaMontoCobro(false);
			}
			
			resp=convenioDao.modificar(	con,usuario,
										this.getCodigo(),
										this.getEmpresa(),
										this.getTipoRegimen(),
       									this.getNombre(),
       									this.getObservaciones(),
       									this.getPlanBeneficios(), 
			        					this.getCodigoMinSalud(),
			        					this.getFormatoFactura(),
			        					this.getActiva(),
			        					this.isConvenioManejaMontoCobro(),
			        					this.tipoContrato,
			        					this.pyp,
			        					this.unificarPyp,
			    		        		this.numeroDiasVencimiento,
			    		        		this.requiereJustificacionServicios,
			    		        		this.requiereJustificacionArticulos,
			    		        		this.requiereJustArtNoposDifMed,
			    		        		this.manejaComplejidad,
			    		        		this.semanasMinimasCotizacion,
			    		        		this.requiereNumeroCarnet,
			    		        		this.tipoConvenio,
			    		        		this.tipoCodigo,
			    		        		this.tipoCodigoArt,
			    		        		this.ajusteServicios,
			    		        		this.ajusteArticulos,
			    		        		this.interfaz,
			    		        		Utilidades.convertirAEntero(this.cantidadMaxCirugia),
			    		        		Utilidades.convertirAEntero(this.cantidadMaxAyudpag),
			        					this.tipoLiquidacionScx,
			        					this.tipoLiquidacionDyt,
			        					this.tipoLiquidacionGcx,
			        					this.tipoLiquidacionGdyt,
			        					this.tipoTarifaLiqMateCx,
			        					this.tipoTarifaLiqMateDyt,
			        					this.tipoFechaLiqTiemPcx,
			        					this.tipoFechaLiqTiempDyt,
			        					this.liquidacionTmpFracAdd,
			        					this.encabezadoFactura,
			        					this.pieFactura,
			        					this.empresaInstitucion,
			        					this.planEspecial,
			        					this.excentoDeudor,
			        					this.excentoDocumentoGarantia,
			        					this.vip,
			        					this.radicarCuentasNegativas,
			        					this.asignarFactValorPacValorAbono,
			        					Utilidades.convertirAEntero(this.diasCPO),
			        					Utilidades.convertirAEntero(this.cantCPO),
			        					this.aseguradora,
			        					this.codigoAseguradora,
			        					this.valorLetrasFactura,
			        					//*****************Anexo 753 Decreto 4747**************************
			        					this.reporte_incons_bd,
			        					this.reporte_atencion_ini_urg,
			        					this.generacion_autom_val_urg,
			        					this.mediosEnvio,
			        					this.correosElectronicos,
			        					this.requiere_autorizacion_servicio,
			                            this.formato_autorizacion,
			        					//*****************************************************************
//			                            ********* Anexo 791 *********
			                            this.manejaMultasPorIncumplimiento,
			                            this.valorMultaPorIncumplimientoCitas,
//			                            *****************************
//			                            ******** cambios anexo 809 **********
			                            this.ccContable,
//			                            *************************************
			                            this.cenAtencContable,
			        					this.manejaBonos,
			        					this.requiereBono,
			        					this.manejaPromociones,
			        					this.esTargetaCliene, 
			        					this.ingresoBdValido,
			        					this.ingresoPacienteReqAutorizacion,
			        					this.reqIngresoValido,
			        					this.tipoAtencion,
			        					this.tipoLiquidacionPool,
			        					this.capitacionSubcontratada,
			        					this.manejaPresupCapitacion
			    		        		);
			boolean resp2= convenioDao.eliminarAjuntos(con, this.getCodigo());
			
			return resp;
	}
	
	/**
	 * Mï¿½todo que obtiene todos los resultados de la tabla convenios
	 * para mostrarlos en el listado
	 * @param con
	 * @return
	 */
	public Collection listadoConvenio(Connection con, int codigoInstitucion)
	{
		ConvenioDao consulta= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConvenioDao();
		Collection coleccion=null;
		try
		{
			logger.info("SE ENVIA ------->  " + this.getTipoAtencion()+ " --- "+ this.isAmbos() );
			coleccion=UtilidadBD.resultSet2Collection(consulta.listado(con, codigoInstitucion,this.getTipoAtencion(),this.isAmbos()));
		}
		catch(Exception e)
		{
			logger.warn("Error mundo convenio " +e.toString());
			coleccion=null;
		}
		return coleccion;
	}
	
	/**
	 * Mï¿½todo que contiene los resultados de la bï¿½squeda de convenios,
	 * segï¿½n los criterios dados en la bï¿½squeda avanzada. 
	 * @param con
	 * @return
	 */
	public Collection resultadoBusquedaAvanzada(Connection con, int codigoInstitucion)
	{
		ConvenioDao consulta= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConvenioDao();
		Collection coleccion=null;
		try
		{	
				coleccion=UtilidadBD.resultSet2Collection(consulta.busqueda(	con,
																				this.codigo,
																				this.razonSocial,
																				this.nombreTipoRegimen,
																				this.nombre,observaciones,
																				this.planBeneficios,
																				this.codigoMinSalud,
																				this.nombreFormatoFactura,
																				this.activaAux,
																				this.nombreTipoContrato,
																				codigoInstitucion,
																				this.pypAux,
																				this.unificarPyp,
																				this.interfaz,
																				Utilidades.convertirAEntero(this.cantidadMaxCirugia),
																				Utilidades.convertirAEntero(this.cantidadMaxAyudpag),
													        					this.tipoLiquidacionScx,
													        					this.tipoLiquidacionDyt,
													        					this.tipoLiquidacionGcx,
													        					this.tipoLiquidacionGdyt,
													        					this.tipoTarifaLiqMateCx,
													        					this.tipoTarifaLiqMateDyt,
													        					this.tipoFechaLiqTiemPcx,
													        					this.tipoFechaLiqTiempDyt,
													        					this.liquidacionTmpFracAdd,
													        					this.empresaInstitucion, 
													        					this.planEspecial,
													        					this.radicarCuentasNegativas,
																				this.asignarFactValorPacValorAbono,
																				this.manejaBonos,
																				this.requiereBono,
												                                this.manejaPromociones,
											                                   	this.esTargetaCliene,
												                                this.ingresoBdValido,
												                                this.ingresoPacienteReqAutorizacion,
												                                this.reqIngresoValido,
												                                this.tipoAtencion,
												                                this.tipoLiquidacionPool
																				));
		}
		catch(Exception e)
		{
			logger.warn("Error mundo empresa " +e.toString());
			coleccion=null;
		}
		return coleccion;		
	}
	
	/**
	 * Mï¿½todo para actualizar segun lo ingresado en parametros Generales
	 * @param con
	 * @param infoAdicIngresoConvenios
	 * @return
	 */
	public int modificarInfoAdicConvenio(Connection con, boolean infoAdicIngresoConvenios)
	{
		return convenioDao.modificarInfoAdicConvenio(con, infoAdicIngresoConvenios);
	}
	
	/**
	 * Mï¿½todo que consulta los contratos vigentes del convenio
	 * @param con
	 * @param codigoConvenio
	 * @return
	 */
	public static HashMap consultarContratosVigentesConvenio(Connection con,int codigoConvenio)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConvenioDao().consultarContratosVigentesConvenio(con,codigoConvenio);
	}
	
	
	
	/**
	 * 
	 * @param con
	 * @param codigoConvenio
	 * @return
	 */
	public static boolean existeVerificacionDerechosConvenio( Connection con, int codigoConvenio)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConvenioDao().existeVerificacionDerechosConvenio(con, codigoConvenio);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoConvenio
	 * @return
	 */
	public static boolean convenioManejaComplejidad(Connection con, int codigoConvenio) throws IPSException
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConvenioDao().convenioManejaComplejidad(con, codigoConvenio);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoPaciente
	 * @param codigoConvenio
	 * @param estratoSocial
	 * @param acronimoTipoAfiliado
	 * @param fechaAfiliacion
	 * @return
	 */
	public static boolean actualizarConveniosPacientes(	Connection con, int codigoPaciente, int codigoConvenio, int estratoSocial, String acronimoTipoAfiliado, String fechaAfiliacion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConvenioDao().actualizarConveniosPacientes(con, codigoPaciente, codigoConvenio, estratoSocial, acronimoTipoAfiliado, fechaAfiliacion);
	}
	
	/**
	 * Inicializa el acceso a bases de datos de este objeto, obteniendo su respectivo DAO.
	 * @param tipoBD el tipo de base de datos que va a usar este objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores vï¿½lidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 * @return <b>true</b> si la inicializaciï¿½n fue exitosa, <code>false</code> si no.
	 */
	public boolean init(String tipoBD)
	{
		boolean wasInited = false;
		DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);

		if (myFactory != null)
		{
			convenioDao = myFactory.getConvenioDao();
			wasInited = (convenioDao != null);
		}
		return wasInited;
	}

	/**
	 * metodo que indica si un convenio en el tipo de contrato es capitado o no
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public static boolean esConvenioCapitado(Connection con, String idCuenta)
	{
		DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		if (myFactory != null)
			convenioDao = myFactory.getConvenioDao();
		return convenioDao.esConvenioCapitado(con, idCuenta);
	}

	/**
	 * Verifica si el convenio es tarjeta cliente o no
	 * @param codigoConvenio Código del convenio de la tarjeta cliente
	 */
	public static boolean esConvenioTarjetaCliente(int codigoConvenio)
	{
		Convenio conv=new Convenio();
		
		try {
			Connection con=UtilidadBD.abrirConexion();
			conv.cargarResumen(con, codigoConvenio);
		} catch (Exception e) {
			Log4JManager.error(e);
		}	
		return UtilidadTexto.getBoolean(conv.getEsTargetaCliene());
		
	}

	
	/**
	 * 
	 * @param con
	 * @param codigoPaciente
	 * @param codigoConvenio
	 * @return
	 */
	public static int obtenerNumeroDiasVencimiento(Connection con, int codigoConvenio)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConvenioDao().obtenerNumeroDiasVencimiento(con, codigoConvenio);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoPaciente
	 * @param codigoConvenio
	 * @return
	 */
	public static int obtenerRadicarCuentasNegativas(Connection con, int codigoConvenio)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConvenioDao().obtenerRadicarCuentasNegativas(con, codigoConvenio);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoPaciente
	 * @param codigoConvenio
	 * @return
	 */
	public static String obtenerCuentaContableConvenio(Connection con, int codigoConvenio)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConvenioDao().obtenerCuentaContableConvenio(con, codigoConvenio);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoPaciente
	 * @param codigoConvenio
	 * @return
	 */
	public static String obtenerNroIdentificacionConvenio(Connection con, int codigoConvenio)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConvenioDao().obtenerNroIdentificacionConvenio(con, codigoConvenio);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoPaciente
	 * @param codigoConvenio
	 * @return
	 */
	public static String obtenerNroIdentificacionConvenio(int codigoConvenio)
	{
		Connection con= UtilidadBD.abrirConexion();
		String id= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConvenioDao().obtenerNroIdentificacionConvenio(con, codigoConvenio);
		UtilidadBD.closeConnection(con);
		return id;
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoConvenio
	 * @return
	 */
	public static double obtenerEmpresaInstitucionConvenio( int codigoConvenio)
	{
		Connection con= UtilidadBD.abrirConexion();
		double id= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConvenioDao().obtenerEmpresaInstitucionConvenio(con, codigoConvenio);
		UtilidadBD.closeConnection(con);
		return id;
	}
	/**
	 * 
	 * @param con
	 * @param codigoConvenio
	 * @return
	 */
	public static double obtenerEmpresaInstitucionConvenio(Connection con, int codigoConvenio)
	{
		double id= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConvenioDao().obtenerEmpresaInstitucionConvenio(con, codigoConvenio);
		return id;
	}
	
	/**
	 * @return Returns the checkInfoAdicCuenta.
	 */
	public String getCheckInfoAdicCuenta()
	{
		return checkInfoAdicCuenta;
	}
	/**
	 * @param checkInfoAdicCuenta The checkInfoAdicCuenta to set.
	 */
	public void setCheckInfoAdicCuenta(String checkInfoAdicCuenta)
	{
		this.checkInfoAdicCuenta=checkInfoAdicCuenta;
	}
	/**
	 * Retorna activo/inactivo convenio
	 * @return
	 */
	public boolean getActiva() {
		return activa;
	}

	/**
	 * Retorna el cï¿½digo del  convenio
	 * @return
	 */
	public int getCodigo() {
		return codigo;
	}

	/**
	 * Retorna el cï¿½digo min salud
	 * @return
	 */
	public String getCodigoMinSalud() {
		return codigoMinSalud;
	}

	/**
	 * Retorna el cï¿½digo de la empresa
	 * @return
	 */
	public int getEmpresa() {
		return empresa;
	}

	/**
	 * Retorna  el fromato de factura que utiliza el convenio
	 * @return
	 */
	public int getFormatoFactura() {
		return formatoFactura;
	}

	/**
	 * Retorna el nombre del convenio
	 * @return
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * Retorna las observaciones del convenio
	 * @return
	 */
	public String getObservaciones() {
		return observaciones;
	}

	/**
	 * Retorna ek plan de beneficios
	 * @return
	 */
	public String getPlanBeneficios() {
		return planBeneficios;
	}

	/**
	 * Retorna el tipo de regimen
	 * @return
	 */
	public String getTipoRegimen() {
		return tipoRegimen;
	}

	/**
	 * Asigna activo/inactivo convenio
	 * @param b
	 */
	public void setActiva(boolean b) {
		activa = b;
	}

	/**
	 * Asigna el cï¿½digo de la empresa
	 * @param i
	 */
	public void setCodigo(int i) {
		codigo = i;
	}

	/**
	 * Asigna el cod min salud
	 * @param string
	 */
	public void setCodigoMinSalud(String string) {
		codigoMinSalud = string;
	}

	/**
	 * Asigna el cï¿½digo de la empresa
	 * @param i
	 */
	public void setEmpresa(int i) {
		empresa = i;
	}

	/**
	 * Asigna el fromato de factura que utiliza el convenio
	 * @param i
	 */
	public void setFormatoFactura(int i) {
		formatoFactura = i;
	}

	/**
	 * Asigna el nombre del convenio
	 * @param string
	 */
	public void setNombre(String string) {
		nombre = string;
	}

	/**
	 * Asigna las observaciones del convenio
	 * @param string
	 */
	public void setObservaciones(String string) {
		observaciones = string;
	}

	/**
	 * Asigna el plan de beneficios
	 * @param string
	 */
	public void setPlanBeneficios(String string) {
		planBeneficios = string;
	}

	/**
	 * Asigna el tipo de rï¿½gimen
	 * @param string
	 */
	public void setTipoRegimen(String string) {
		tipoRegimen = string;
	}

	/**
	 * Retorna Aux para modificar formato Factura
	 * @return
	 */
	public int getFormatoFacturaNuevo() {
		return formatoFacturaNuevo;
	}

	/**
	 * Retorna Aux para modificar tipo rï¿½gimen
	 * @return
	 */
	public String getTipoRegimenNuevo() {
		return tipoRegimenNuevo;
	}

	/**
	 * Asigna Aux para modificar cod empresa
	 * @param i
	 */
	public void setEmpresaNuevo(int i) {
		empresaNuevo = i;
	}

	/**
	 * Asigna Aux para modificar formato Factura
	 * @param i
	 */
	public void setFormatoFacturaNuevo(int i) {
		formatoFacturaNuevo = i;
	}

	/**
	 * Asigna  Aux para modificar el tipo de rï¿½gimen
	 * @param string
	 */
	public void setTipoRegimenNuevo(String string) {
		tipoRegimenNuevo = string;
	}
	
	/**
	 * Retorna el Auxiliar del campo boolean activa, para poder mandar
	 * un nuevo valor diferente de true o false en la bï¿½squeda
	 * avanzada
	 */
	public int getActivaAux() {
		return activaAux;
	}

	/**
	 * Asigna el Auxiliar del campo boolean activa, para poder mandar
	 * un nuevo valor diferente de true o false en la bï¿½squeda
	 * avanzada
	 */
	public void setActivaAux(int i) {
		activaAux = i;
	}
	
	/**
	 * Para la bï¿½squeda contiene Formato factura
	 * @return
	 */
	public String getNombreFormatoFactura() {
		return nombreFormatoFactura;
	}

	/**
	 * Para la bï¿½squeda (contiene tipo rï¿½gimen)
	 * @return
	 */
	public String getNombreTipoRegimen() {
		return nombreTipoRegimen;
	}

	/**
	 * Para la bï¿½squeda (contiene razonSocial-empresa)
	 * @return
	 */
	public String getRazonSocial() {
		return razonSocial;
	}

	/**
	 * Para la bï¿½squeda contiene Formato factura
	 * @param string
	 */
	public void setNombreFormatoFactura(String string) {
		nombreFormatoFactura = string;
	}

	/**
	 * Para la bï¿½squeda (contiene tipo rï¿½gimen)
	 * @param string
	 */
	public void setNombreTipoRegimen(String string) {
		nombreTipoRegimen = string;
	}

	/**
	 * Para la bï¿½squeda (contiene razonSocial-empresa)
	 * @param string
	 */
	public void setRazonSocial(String string) {
		razonSocial = string;
	}

    /**
     * @return Returns the nombreTipoContrato.
     */
    public String getNombreTipoContrato() {
        return nombreTipoContrato;
    }
    /**
     * @param nombreTipoContrato The nombreTipoContrato to set.
     */
    public void setNombreTipoContrato(String nombreTipoContrato) {
        this.nombreTipoContrato = nombreTipoContrato;
    }
    /**
     * @return Returns the tipoContrato.
     */
    public int getTipoContrato() {
        return tipoContrato;
    }
    /**
     * @param tipoContrato The tipoContrato to set.
     */
    public void setTipoContrato(int tipoContrato) {
        this.tipoContrato = tipoContrato;
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
	 * @return Returns the pypAux.
	 */
	public int getPypAux() {
		return pypAux;
	}

	/**
	 * @param pypAux The pypAux to set.
	 */
	public void setPypAux(int pypAux) {
		this.pypAux = pypAux;
	}

	public String getUnificarPyp() {
		return unificarPyp;
	}

	public void setUnificarPyp(String unificarPyp) {
		this.unificarPyp = unificarPyp;
	}

	public String getManejaComplejidad() {
		return manejaComplejidad;
	}

	public void setManejaComplejidad(String manejaComplejidad) {
		this.manejaComplejidad = manejaComplejidad;
	}

	public String getNumeroDiasVencimiento() {
		return numeroDiasVencimiento;
	}

	public void setNumeroDiasVencimiento(String numeroDiasVencimiento) {
		this.numeroDiasVencimiento = numeroDiasVencimiento;
	}

	public String getRequiereNumeroCarnet() {
		return requiereNumeroCarnet;
	}

	public void setRequiereNumeroCarnet(String requiereNumeroCarnet) {
		this.requiereNumeroCarnet = requiereNumeroCarnet;
	}

	public String getSemanasMinimasCotizacion() {
		return semanasMinimasCotizacion;
	}

	public void setSemanasMinimasCotizacion(String semanasMinimasCotizacion) {
		this.semanasMinimasCotizacion = semanasMinimasCotizacion;
	}

	public int getInstitucion() {
		return institucion;
	}

	public void setInstitucion(int institucion) {
		this.institucion = institucion;
	}

	/**
	 * @return the clasificacionTipoConvenio
	 */
	public int getClasificacionTipoConvenio() {
		return clasificacionTipoConvenio;
	}

	/**
	 * @param clasificacionTipoConvenio the clasificacionTipoConvenio to set
	 */
	public void setClasificacionTipoConvenio(int clasificacionTipoConvenio) {
		this.clasificacionTipoConvenio = clasificacionTipoConvenio;
	}

	/**
	 * @return the descripcionTipoRegimen
	 */
	public String getDescripcionTipoRegimen() {
		return descripcionTipoRegimen;
	}

	/**
	 * @param descripcionTipoRegimen the descripcionTipoRegimen to set
	 */
	public void setDescripcionTipoRegimen(String descripcionTipoRegimen) {
		this.descripcionTipoRegimen = descripcionTipoRegimen;
	}

	/**
	 * @return the requiereDeudor
	 */
	public boolean isRequiereDeudor() {
		return requiereDeudor;
	}

	/**
	 * @param requiereDeudor the requiereDeudor to set
	 */
	public void setRequiereDeudor(boolean requiereDeudor) {
		this.requiereDeudor = requiereDeudor;
	}

	/**
	 * @return the requiereVerificacionDerechos
	 */
	public boolean isRequiereVerificacionDerechos() {
		return requiereVerificacionDerechos;
	}

	/**
	 * @param requiereVerificacionDerechos the requiereVerificacionDerechos to set
	 */
	public void setRequiereVerificacionDerechos(boolean requiereVerificacionDerechos) {
		this.requiereVerificacionDerechos = requiereVerificacionDerechos;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getRequiereJustificacionArticulos() {
		return requiereJustificacionArticulos;
	}
	
	/**
	 * 
	 * @param requiereJustificacionArticulos
	 */
	public void setRequiereJustificacionArticulos(
			String requiereJustificacionArticulos) {
		this.requiereJustificacionArticulos = requiereJustificacionArticulos;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getRequiereJustificacionServicios() {
		return requiereJustificacionServicios;
	}
	
	/**
	 * 
	 * @param requiereJustificacionServicios
	 */
	public void setRequiereJustificacionServicios(
			String requiereJustificacionServicios) {
		this.requiereJustificacionServicios = requiereJustificacionServicios;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getTipoCodigo() {
		return tipoCodigo;
	}
	/**
	 * 
	 * @return
	 */
	public int getTipoCodigoArt() {
		return tipoCodigoArt;
	}
	
	/**
	 * 
	 * @param tipoCodigo
	 */
	public void setTipoCodigo(int tipoCodigo) {
		this.tipoCodigo = tipoCodigo;
	}
	/**
	 * 
	 * @param tipoCodigo
	 */
	public void setTipoCodigoArt(int tipoCodigo) {
		this.tipoCodigoArt = tipoCodigo;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getAjusteArticulos() {
		return ajusteArticulos;
	}
	
	/**
	 * 
	 * @param ajusteArticulos
	 */
	public void setAjusteArticulos(String ajusteArticulos) {
		this.ajusteArticulos = ajusteArticulos;
	}
	
	/**
	 * 
	 */
	public String getAjusteServicios() {
		return ajusteServicios;
	}
	
	/**
	 * 
	 * @param ajusteServicios
	 */
	public void setAjusteServicios(String ajusteServicios) {
		this.ajusteServicios = ajusteServicios;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getInterfaz() {
		return interfaz;
	}
	
	/**
	 * 
	 * @param interfaz
	 */
	public void setInterfaz(String interfaz) {
		this.interfaz = interfaz;
	}

	/**
	 * @return the tipoConvenio
	 */
	public String getTipoConvenio() {
		return tipoConvenio;
	}

	/**
	 * @param tipoConvenio the tipoConvenio to set
	 */
	public void setTipoConvenio(String tipoConvenio) {
		this.tipoConvenio = tipoConvenio;
	}

	/**
	 * @return the cantidadMaxAyudpag
	 */
	public String getCantidadMaxAyudpag() {
		return cantidadMaxAyudpag;
	}

	/**
	 * @param cantidadMaxAyudpag the cantidadMaxAyudpag to set
	 */
	public void setCantidadMaxAyudpag(String cantidadMaxAyudpag) {
		this.cantidadMaxAyudpag = cantidadMaxAyudpag;
	}

	/**
	 * @return the cantidadMaxCirugia
	 */
	public String getCantidadMaxCirugia() {
		return cantidadMaxCirugia;
	}

	/**
	 * @param cantidadMaxCirugia the cantidadMaxCirugia to set
	 */
	public void setCantidadMaxCirugia(String cantidadMaxCirugia) {
		this.cantidadMaxCirugia = cantidadMaxCirugia;
	}

	/**
	 * @return the liquidacionTmpFracAdd
	 */
	public String getLiquidacionTmpFracAdd() {
		return liquidacionTmpFracAdd;
	}

	/**
	 * @param liquidacionTmpFracAdd the liquidacionTmpFracAdd to set
	 */
	public void setLiquidacionTmpFracAdd(String liquidacionTmpFracAdd) {
		this.liquidacionTmpFracAdd = liquidacionTmpFracAdd;
	}

	/**
	 * @return the tipoFechaLiqTiemPcx
	 */
	public String getTipoFechaLiqTiemPcx() {
		return tipoFechaLiqTiemPcx;
	}

	/**
	 * @param tipoFechaLiqTiemPcx the tipoFechaLiqTiemPcx to set
	 */
	public void setTipoFechaLiqTiemPcx(String tipoFechaLiqTiemPcx) {
		this.tipoFechaLiqTiemPcx = tipoFechaLiqTiemPcx;
	}

	/**
	 * @return the tipoFechaLiqTiempDyt
	 */
	public String getTipoFechaLiqTiempDyt() {
		return tipoFechaLiqTiempDyt;
	}

	/**
	 * @param tipoFechaLiqTiempDyt the tipoFechaLiqTiempDyt to set
	 */
	public void setTipoFechaLiqTiempDyt(String tipoFechaLiqTiempDyt) {
		this.tipoFechaLiqTiempDyt = tipoFechaLiqTiempDyt;
	}

	/**
	 * @return the tipoLiquidacionDyt
	 */
	public String getTipoLiquidacionDyt() {
		return tipoLiquidacionDyt;
	}

	/**
	 * @param tipoLiquidacionDyt the tipoLiquidacionDyt to set
	 */
	public void setTipoLiquidacionDyt(String tipoLiquidacionDyt) {
		this.tipoLiquidacionDyt = tipoLiquidacionDyt;
	}

	/**
	 * @return the tipoLiquidacionGcx
	 */
	public String getTipoLiquidacionGcx() {
		return tipoLiquidacionGcx;
	}

	/**
	 * @param tipoLiquidacionGcx the tipoLiquidacionGcx to set
	 */
	public void setTipoLiquidacionGcx(String tipoLiquidacionGcx) {
		this.tipoLiquidacionGcx = tipoLiquidacionGcx;
	}

	/**
	 * @return the tipoLiquidacionGdyt
	 */
	public String getTipoLiquidacionGdyt() {
		return tipoLiquidacionGdyt;
	}

	/**
	 * @param tipoLiquidacionGdyt the tipoLiquidacionGdyt to set
	 */
	public void setTipoLiquidacionGdyt(String tipoLiquidacionGdyt) {
		this.tipoLiquidacionGdyt = tipoLiquidacionGdyt;
	}

	/**
	 * @return the tipoLiquidacionScx
	 */
	public String getTipoLiquidacionScx() {
		return tipoLiquidacionScx;
	}

	/**
	 * @param tipoLiquidacionScx the tipoLiquidacionScx to set
	 */
	public void setTipoLiquidacionScx(String tipoLiquidacionScx) {
		this.tipoLiquidacionScx = tipoLiquidacionScx;
	}

	/**
	 * @return the tipoTarifaLiqMateCx
	 */
	public String getTipoTarifaLiqMateCx() {
		return tipoTarifaLiqMateCx;
	}

	/**
	 * @param tipoTarifaLiqMateCx the tipoTarifaLiqMateCx to set
	 */
	public void setTipoTarifaLiqMateCx(String tipoTarifaLiqMateCx) {
		this.tipoTarifaLiqMateCx = tipoTarifaLiqMateCx;
	}

	/**
	 * @return the tipoTarifaLiqMateDyt
	 */
	public String getTipoTarifaLiqMateDyt() {
		return tipoTarifaLiqMateDyt;
	}

	/**
	 * @param tipoTarifaLiqMateDyt the tipoTarifaLiqMateDyt to set
	 */
	public void setTipoTarifaLiqMateDyt(String tipoTarifaLiqMateDyt) {
		this.tipoTarifaLiqMateDyt = tipoTarifaLiqMateDyt;
	}


	/**
	 * @return the encabezadoFactura
	 */
	public String getEncabezadoFactura() {
		return encabezadoFactura;
	}


	/**
	 * @param encabezadoFactura the encabezadoFactura to set
	 */
	public void setEncabezadoFactura(String encabezadoFactura) {
		this.encabezadoFactura = encabezadoFactura;
	}


	/**
	 * @return the pieFactura
	 */
	public String getPieFactura() {
		return pieFactura;
	}


	/**
	 * @param pieFactura the pieFactura to set
	 */
	public void setPieFactura(String pieFactura) {
		this.pieFactura = pieFactura;
	}


	/**
	 * @return the empresaInstitucion
	 */
	public String getEmpresaInstitucion() {
		return empresaInstitucion;
	}


	/**
	 * @param empresasInstitucion the empresasInstitucion to set
	 */
	public void setEmpresaInstitucion(String empresaInstitucion) {
		this.empresaInstitucion = empresaInstitucion;
	}


	/**
	 * @return the numeroIdentificacion
	 */
	public String getNumeroIdentificacion() {
		return numeroIdentificacion;
	}


	/**
	 * @param numeroIdentificacion the numeroIdentificacion to set
	 */
	public void setNumeroIdentificacion(String numeroIdentificacion) {
		this.numeroIdentificacion = numeroIdentificacion;
	}

	/**
	 * 
	 * @param con
	 * @param codigoInstitucion
	 * @return
	 */
	public ArrayList cargarPlanEspecial(Connection con, String codigoInstitucion) {
		
		return convenioDao.cargarPlanEspecial(con, codigoInstitucion);
	}
	
	/**
	 *  Retorna el listado de centros de consto contables es decir con tipo area = directo e indirecto, y que se enecuentren activos
	 * @param con
	 * @param codigoInstitucion
	 * @return
	 */
	public ArrayList consultarCcContable(Connection con, String codigoInstitucion) {
		return convenioDao.consultarCcContable(con, codigoInstitucion);
	}

	/**
	 * 
	 * @return
	 */
	public int getPlanEspecial() {
		return planEspecial;
	}

	/**
	 * 
	 * @param planEspecial
	 */
	public void setPlanEspecial(int planEspecial) {
		this.planEspecial = planEspecial;
	}


	public String cargarNombreCentroCostoPlanEspecial(Connection con, String codigoPlanEspecial) 
	{
		
		return convenioDao.cargarNombreCentroCostoPlanEspecial(con, codigoPlanEspecial);
	}


	public ArrayList getPlanEspecialList() {
		if(this.planEspecialList == null)
			return new ArrayList();
		else
			return planEspecialList;
	}


	public void setPlanEspecialList(ArrayList planEspecialList) {
		this.planEspecialList = planEspecialList;
	}


	public String getExcentoDeudor() {
		return excentoDeudor;
	}


	public void setExcentoDeudor(String excentoDeudor) {
		this.excentoDeudor = excentoDeudor;
	}


	public String getExcentoDocumentoGarantia() {
		return excentoDocumentoGarantia;
	}


	public void setExcentoDocumentoGarantia(String excentoDocumentoGarantia) {
		this.excentoDocumentoGarantia = excentoDocumentoGarantia;
	}


	public String getVip() {
		return vip;
	}


	public void setVip(String vip) {
		this.vip = vip;
	}


	/**
	 * @return the cantCPO
	 */
	public String getCantCPO() {
		return cantCPO;
	}


	/**
	 * @param cantCPO the cantCPO to set
	 */
	public void setCantCPO(String cantCPO) {
		this.cantCPO = cantCPO;
	}


	/**
	 * @return the diasCPO
	 */
	public String getDiasCPO() {
		return diasCPO;
	}


	/**
	 * @param diasCPO the diasCPO to set
	 */
	public void setDiasCPO(String diasCPO) {
		this.diasCPO = diasCPO;
	}


	/**
	 * @return the asignarFactValorPacValorAbono
	 */
	public String getAsignarFactValorPacValorAbono() {
		return asignarFactValorPacValorAbono;
	}


	/**
	 * @param asignarFactValorPacValorAbono the asignarFactValorPacValorAbono to set
	 */
	public void setAsignarFactValorPacValorAbono(
			String asignarFactValorPacValorAbono) {
		this.asignarFactValorPacValorAbono = asignarFactValorPacValorAbono;
	}


	/**
	 * @return the radicarCuentasNegativas
	 */
	public String getRadicarCuentasNegativas() {
		return radicarCuentasNegativas;
	}


	/**
	 * @param radicarCuentasNegativas the radicarCuentasNegativas to set
	 */
	public void setRadicarCuentasNegativas(String radicarCuentasNegativas) {
		this.radicarCuentasNegativas = radicarCuentasNegativas;
	}


	/**
	 * @return the requiereJustArtNoposDifMed
	 */
	public String getRequiereJustArtNoposDifMed() {
		return requiereJustArtNoposDifMed;
	}


	/**
	 * @param requiereJustArtNoposDifMed the requiereJustArtNoposDifMed to set
	 */
	public void setRequiereJustArtNoposDifMed(String requiereJustArtNoposDifMed) {
		this.requiereJustArtNoposDifMed = requiereJustArtNoposDifMed;
	}

	/**
	 * @return the aseguradora
	 */
	public String getAseguradora() {
		return aseguradora;
	}

	/**
	 * @param aseguradora the aseguradora to set
	 */
	public void setAseguradora(String aseguradora) {
		this.aseguradora = aseguradora;
	}

	/**
	 * @return the codigoAseguradora
	 */
	public String getCodigoAseguradora() {
		return codigoAseguradora;
	}

	/**
	 * @param codigoAseguradora the codigoAseguradora to set
	 */
	public void setCodigoAseguradora(String codigoAseguradora) {
		this.codigoAseguradora = codigoAseguradora;
	}

	/**
	 * @return the valorLetrasFactura
	 */
	public String getValorLetrasFactura() {
		return valorLetrasFactura;
	}

	/**
	 * @param valorLetrasFactura the valorLetrasFactura to set
	 */
	public void setValorLetrasFactura(String valorLetrasFactura) {
		this.valorLetrasFactura = valorLetrasFactura;
	}
    
	/**
	 * 
	 * @return
	 */
	public String getReporte_incons_bd() {
		return reporte_incons_bd;
	}

	/**
	 * 
	 * @param reporte_incons_bd
	 */
	public void setReporte_incons_bd(String reporte_incons_bd) {
		this.reporte_incons_bd = reporte_incons_bd;
	}
    
	/**
	 * 
	 * @return
	 */
	public String getReporte_atencion_ini_urg() {
		return reporte_atencion_ini_urg;
	}
 
	/**
	 * 
	 * @param reporte_atencion_ini_urg
	 */
	public void setReporte_atencion_ini_urg(String reporte_atencion_ini_urg) {
		this.reporte_atencion_ini_urg = reporte_atencion_ini_urg;
	}

	/**
	 * 
	 * @return
	 */
	public String getGeneracion_autom_val_urg() {
		return generacion_autom_val_urg;
	}
    
	/**
	 * 
	 * @param generacion_autom_val_urg
	 */
	public void setGeneracion_autom_val_urg(String generacion_autom_val_urg) {
		this.generacion_autom_val_urg = generacion_autom_val_urg;
	}
	
	/**
	 * 
	 * @return
	 */
	public String[] getMediosEnvio() {
		return mediosEnvio;
	}
    
	/**
	 * 
	 * @param mediosEnvio
	 */
	public void setMediosEnvio(String[] mediosEnvio) {
		this.mediosEnvio = mediosEnvio;
	}
	
	/**
	 * 
	 * @return
	 */
	public HashMap getCorreosElectronicos() {
		return correosElectronicos;
	}

	/**
	 * 
	 * @param correosElectronicos
	 */
	public void setCorreosElectronicos(HashMap correosElectronicos) {
		this.correosElectronicos = correosElectronicos;
	}	
	
	/**
	 * 
	 * @return
	 */
	public String getRequiere_autorizacion_servicio() {
		return requiere_autorizacion_servicio;
	}
    
	/**
	 * 
	 * @param requiere_autorizacion_servicio
	 */
	public void setRequiere_autorizacion_servicio(
			String requiere_autorizacion_servicio) {
		this.requiere_autorizacion_servicio = requiere_autorizacion_servicio;
	}
    
	/**
	 * 
	 * @return
	 */
	public String getFormato_autorizacion() {
		return formato_autorizacion;
	}
    
	/**
	 * 
	 * @param formato_autorizacion
	 */
	public void setFormato_autorizacion(String formato_autorizacion) {
		this.formato_autorizacion = formato_autorizacion;
	}
	//********************** Cambios ANEXO 791 ***********************************************
	
	/**
	 * @return the manejaMultasPorIncumplimiento
	 */
	public String getManejaMultasPorIncumplimiento() {
		return manejaMultasPorIncumplimiento;
	}
	
	/**
	 * @param manejaMultasPorIncumplimiento the manejaMultasPorIncumplimiento to set
	 */
	public void setManejaMultasPorIncumplimiento(
			String manejaMultasPorIncumplimiento) {
		this.manejaMultasPorIncumplimiento = manejaMultasPorIncumplimiento;
	}

	/**
	 * @return the valorMultaPorIncumplimientoCitas
	 */
	public String getValorMultaPorIncumplimientoCitas() {
		return valorMultaPorIncumplimientoCitas;
	}

	/**
	 * @param valorMultaPorIncumplimientoCitas the valorMultaPorIncumplimientoCitas to set
	 */
	public void setValorMultaPorIncumplimientoCitas(
			String valorMultaPorIncumplimientoCitas) {
		this.valorMultaPorIncumplimientoCitas = valorMultaPorIncumplimientoCitas;
	}						
	//****************************************************************************************

	/**
	 * @return the ccContableList
	 */
	public ArrayList getCcContableList() {
		return ccContableList;
	}

	/**
	 * @param ccContableList the ccContableList to set
	 */
	public void setCcContableList(ArrayList ccContableList) {
		this.ccContableList = ccContableList;
	}
	
	/*--------------------------------
	 * getters y setters anexo 809
	 ---------------------------------*/

	/**
	 * @return the ccContable
	 */
	public int getCcContable() {
		return ccContable;
	}

	/**
	 * @param ccContable the ccContable to set
	 */
	public void setCcContable(int ccContable) {
		this.ccContable = ccContable;
	}

	/**
	 * @return the logger
	 */
	public static Logger getLogger() {
		return logger;
	}

	/**
	 * @param logger the logger to set
	 */
	public static void setLogger(Logger logger) {
		Convenio.logger = logger;
	}

	/**
	 * @return the manejaBonos
	 */
	public String getManejaBonos() {
		return manejaBonos;
	}

	/**
	 * @param manejaBonos the manejaBonos to set
	 */
	public void setManejaBonos(String manejaBonos) {
		this.manejaBonos = manejaBonos;
	}

	/**
	 * @return the requiereBono
	 */
	public String getRequiereBono() {
		return requiereBono;
	}

	/**
	 * @param requiereBono the requiereBono to set
	 */
	public void setRequiereBono(String requiereBono) {
		this.requiereBono = requiereBono;
	}

	/**
	 * @return the manejaPromociones
	 */
	public String getManejaPromociones() {
		return manejaPromociones;
	}

	/**
	 * @param manejaPromociones the manejaPromociones to set
	 */
	public void setManejaPromociones(String manejaPromociones) {
		this.manejaPromociones = manejaPromociones;
	}

	/**
	 * @return the esTargetaCliene
	 */
	public String getEsTargetaCliene() {
		return esTargetaCliene;
	}

	/**
	 * @param esTargetaCliene the esTargetaCliene to set
	 */
	public void setEsTargetaCliene(String esTargetaCliene) {
		this.esTargetaCliene = esTargetaCliene;
	}

	/**
	 * @return the ingresoBdValido
	 */
	public String getIngresoBdValido() {
		return ingresoBdValido;
	}

	/**
	 * @param ingresoBdValido the ingresoBdValido to set
	 */
	public void setIngresoBdValido(String ingresoBdValido) {
		this.ingresoBdValido = ingresoBdValido;
	}

	/**
	 * @return the ingresoPacienteReqAutorizacion
	 */
	public String getIngresoPacienteReqAutorizacion() {
		return ingresoPacienteReqAutorizacion;
	}

	/**
	 * @param ingresoPacienteReqAutorizacion the ingresoPacienteReqAutorizacion to set
	 */
	public void setIngresoPacienteReqAutorizacion(
			String ingresoPacienteReqAutorizacion) {
		this.ingresoPacienteReqAutorizacion = ingresoPacienteReqAutorizacion;
	}

	/**
	 * @return the reqIngresoValido
	 */
	public String getReqIngresoValido() {
		return reqIngresoValido;
	}

	/**
	 * @param reqIngresoValido the reqIngresoValido to set
	 */
	public void setReqIngresoValido(String reqIngresoValido) {
		this.reqIngresoValido = reqIngresoValido;
	}

	/**
	 * @return the tipoAtencion
	 */
	public String getTipoAtencion() {
		return tipoAtencion;
	}

	/**
	 * @param tipoAtencion the tipoAtencion to set
	 */
	public void setTipoAtencion(String tipoAtencion) {
		this.tipoAtencion = tipoAtencion;
	}

	/**
	 * @return the empresaNuevo
	 */
	public int getEmpresaNuevo() {
		return empresaNuevo;
	}

	/**
	 * @param convenioDao the convenioDao to set
	 */
	public static void setConvenioDao(ConvenioDao convenioDao) {
		Convenio.convenioDao = convenioDao;
	}


	/**
	 * @return the mediosAutorizacion
	 */
	public ArrayList<DtoMediosAutorizacion> getMediosAutorizacion() {
		return mediosAutorizacion;
	}


	/**
	 * @param mediosAutorizacion the mediosAutorizacion to set
	 */
	public void setMediosAutorizacion(
			ArrayList<DtoMediosAutorizacion> mediosAutorizacion) {
		this.mediosAutorizacion = mediosAutorizacion;
	}

	/**
	 * @return the mediosAutorizacionVector
	 */
	public static String[] getMediosAutorizacionVector() {
		return mediosAutorizacionVector;
	}

	/**
	 * @param mediosAutorizacionVector the mediosAutorizacionVector to set
	 */
	public static void setMediosAutorizacionVector(String[] mediosAutorizacionVector) {
		Convenio.mediosAutorizacionVector = mediosAutorizacionVector;
	}

	

	/**
	 * @return the ambos
	 */
	public boolean isAmbos() {
		return ambos;
	}

	/**
	 * @param ambos the ambos to set
	 */
	public void setAmbos(boolean ambos) {
		this.ambos = ambos;
	}
	
	/**
	 * Retorna el nombre generado del documento adjunto
	 * @param key
	 * @return Object
	 */
	
	
	public Object getDocumentoAdjuntoGenerado(String key)
	{
		return documentosAdjuntosGenerados.get(key);
	}
	
	public void setDocumentoAdjuntoGenerado(String key, Object value) 
	{
		String val = (String) value;
	
		if (val != null) 
			val = val.trim();

		documentosAdjuntosGenerados.put(key, value);
	}
	
	/**
	 * 
	 * @param convenio
	 * @return
	 */
	public static boolean manejaPromociones(int convenio)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConvenioDao().manejaPromociones(convenio);
	}
	
	
	
	/**
	 * 
	 * @param convenio
	 * @return
	 */
	public static String obtenerTipoAtencion(int convenio) throws IPSException
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConvenioDao().obtenerTipoAtencion(convenio);
	}

	public static ArrayList<Convenio> consultarConveniosPacienteUltimoIngreso(Connection con, int codigoPersona, boolean filtrarOdontologicos) {
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConvenioDao().consultarConveniosPacienteUltimoIngreso(con, codigoPersona, filtrarOdontologicos);
	}

	/**
	 * @return the contrato
	 */
	public Contrato getContrato() {
		return contrato;
	}

	/**
	 * @param contrato the contrato to set
	 */
	public void setContrato(Contrato contrato) {
		this.contrato = contrato;
	}

	/**
	 * @return the requiereIngresoValorAuto
	 */
	public String getRequiereIngresoValorAuto() {
		return requiereIngresoValorAuto;
	}

	/**
	 * @param requiereIngresoValorAuto the requiereIngresoValorAuto to set
	 */
	public void setRequiereIngresoValorAuto(String requiereIngresoValorAuto) {
		this.requiereIngresoValorAuto = requiereIngresoValorAuto;
	}

	/**
	 * @return the cenAtencContable
	 */
	public int getCenAtencContable() {
		return cenAtencContable;
	}

	/**
	 * @param cenAtencContable the cenAtencContable to set
	 */
	public void setCenAtencContable(int cenAtencContable) {
		this.cenAtencContable = cenAtencContable;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getTipoLiquidacionPool() {
		return tipoLiquidacionPool;
	}

	/**
	 * 
	 * @param tipoLiquidacionPool
	 */
	public void setTipoLiquidacionPool(String tipoLiquidacionPool) {
		this.tipoLiquidacionPool = tipoLiquidacionPool;
	}
	
	/**
	 * 
	 * @param convenio
	 * @return
	 */
	public static String obtenerTipoLiquidacionPool(int convenio)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConvenioDao().obtenerTipoLiquidacionPool(convenio);
	}
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static ArrayList<DtoConvenio> cargarConveniosArrayList(DtoConvenio dto){
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConvenioDao().cargarConveniosArrayList(dto);
	}
	

	public String getManejaDescuentoOdontologico() {
		return manejaDescuentoOdontologico;
	}

	public void setManejaDescuentoOdontologico(String manejaDescuentoOdontologico) {
		this.manejaDescuentoOdontologico = manejaDescuentoOdontologico;
	}

	public boolean isConvenioManejaMontoCobro() {
		return convenioManejaMontoCobro;
	}

	public void setConvenioManejaMontoCobro(boolean convenioManejaMontoCobro) {
		this.convenioManejaMontoCobro = convenioManejaMontoCobro;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo capitacionSubcontratada
	
	 * @return retorna la variable capitacionSubcontratada 
	 * @author Angela Maria Aguirre 
	 */
	public String getCapitacionSubcontratada() {
		return capitacionSubcontratada;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo capitacionSubcontratada
	
	 * @param valor para el atributo capitacionSubcontratada 
	 * @author Angela Maria Aguirre 
	 */
	public void setCapitacionSubcontratada(String capitacionSubcontratada) {
		this.capitacionSubcontratada = capitacionSubcontratada;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo manejaPresupCapitacion
	
	 * @return retorna la variable manejaPresupCapitacion 
	 * @author Angela Maria Aguirre 
	 */
	public String getManejaPresupCapitacion() {
		return manejaPresupCapitacion;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo manejaPresupCapitacion
	
	 * @param valor para el atributo manejaPresupCapitacion 
	 * @author Angela Maria Aguirre 
	 */
	public void setManejaPresupCapitacion(String manejaPresupCapitacion) {
		this.manejaPresupCapitacion = manejaPresupCapitacion;
	}

	/**
	 * 
	 * @param codigoConvenio
	 * @return
	 */
	public static boolean esConvenioTipoConventioEventoCatTrans(int codigoConvenio) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConvenioDao().esConvenioTipoConventioEventoCatTrans(codigoConvenio);
	}

	
	
}