/*
 * @(#)ConvenioForm.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.actionform.cargos;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.InfoDatosStr;
import util.ResultadoBoolean;
import util.UtilidadCadena;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.dto.comun.DtoCheckBox;
import com.princetonsa.dto.facturacion.DtoMediosAutorizacion;
import com.princetonsa.mundo.UsuarioBasico;

/**
 * Form que contiene todos los datos especï¿½ficos para generar 
 * el Registro de convenio.
 * Y adicionalmente hace el manejo de reset de la forma y de
 * validaciï¿½n de errores de datos de entrada.
 * @version 1,0. Mayo 3, 2004
 * @author wrios 
 */
public class ConvenioForm extends ValidatorForm
{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/*-----------------------------------------------
	 * 	ATRIBUTOS DEL PAGER Y EL ORDENAMIENTO
	 ------------------------------------------------*/
	public static String[] mediosAutorizacionVector= { ConstantesIntegridadDominio.acronimoTelefonica, ConstantesIntegridadDominio.acronimoFax, ConstantesIntegridadDominio.acronimoOtro, ConstantesIntegridadDominio.acronimoInternet };
	
	/**
	 * 
	 */
	
	boolean odontologiaActivo;
	
	/**
	 * String Patron Ordenar 
	 */
	
	/**
	 * Colecciï¿½n con los nombres generados de los archivos adjuntos 
	 */
	private Map documentosAdjuntosGenerados = new HashMap(); 
	
	/**
	 * Nï¿½mero de documentos adjuntos
	 */
	private int numDocumentosAdjuntos = 0;
	
	/**
	 * String Patron Ordenar 
	 */
	
	private String patronOrdenar;
	
	
	/**
	 * String Ultimo Patron de Ordenamiento
	 **/
		
	private String ultimoPatron;
	    

	/**
     * Para controlar el link siguiente del pager 
     */
    private String linkSiguiente;
    
    /**
     * Atributo para el manejo de la paginacion con memoria 
     */
    private int currentPageNumber;	
	
	/*-----------------------------------------------
	 * 	FIN ATRIBUTOS DEL PAGER Y EL ORDENAMIENTO
	 ------------------------------------------------*/
    
    /*-----------------------------------------------
	 * 	ATRIBUTOS PARA MANEJAR LA ADICIï¿½N DE USUARIOS
	 ------------------------------------------------*/
    
    /**
     * HashMap que contiene los datos de adicionar usuarios convenio
     */
	private HashMap adicionarUsuariosConvenio = new HashMap ();
	
	/**
	 * Indica la posicion seleccionada 
	 * del mapa principal. Primer listado
	 */
	private String index=""; 
	
	/**
	 * Mensaje de exito o fracaso en de la operacion
	 */
	private ResultadoBoolean mensaje = new ResultadoBoolean(false);
	
	/**
	 * Nombre del usuario que serï¿½ agregado
	 */
	
	private String usuario;
	
	/**
	 * Tipo de usuario que se define como Auditor o Usuario Glosa
	 */
	private String tipoUsuario;
	
	/**
	 * String que indica si el texto de respuesta de procedimientos es activo o no?
	 */
	private String activo;
	
	/**
	 * Almacena los datos de adicionar usuarios convenio
	 */
	private HashMap criterios = new HashMap ();
	
	/**
	 * Almacena los usuarios
	 */
	private HashMap<String, Object> usuarios = new HashMap<String,Object>();
	
	/**
	 * Posiciï¿½n seleccionada del mapa donde se cargan los usuarios glosas convenio
	 */
	private String posicion;
	
	/**
	 * Variable que indica si esta en consulta o no
	 */
	private boolean esConsulta;
	
	/*-----------------------------------------------
	 * 	FIN ATRIBUTOS PARA MANEJAR LA ADICIï¿½N DE USUARIOS
	 ------------------------------------------------*/
	
    
	/**
	 * Para hacer logs de debug / warn / error de esta funcionalidad.
	 */
	private transient Logger logger = Logger.getLogger(ConvenioForm.class);
	
	/**
	 * Cï¿½digo del convenio
	 */
	private int codigo;
		
	/**
	 * cod convenio para la busqueda
	 */
	private String codigoStr;
	
	/**
	 * cï¿½digo de la empresa (tabla empresas - estado activo)
	 */
	private int empresa;
	
	/**
	 * Campo para capturar el tipo de rï¿½gimen previamente
	 *  creado en el sistema
	 */
	private String tipoRegimen;
		
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
	
	/**
	 * 
	 */
	private boolean convenioManejaMontoCobro;
	
	/**
	 * 
	 */
	private String convenioManejaMontoCobro1;
	
	/**
	* Estado en el que se encuentra el proceso.       
	*/
	
	private String estado;
	
	/**
	 * Aux para poder modificar el tipo de Regimen
	 */
	private String tipoRegimenNuevo;
	
//***************************** INICIO Cambios decreto 4747 ANDRES ORTIZ abril 16 - 2009 **************************
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
	 * Especifica si se requiere autorizacion para  servicios adicionales
	  */
	private String requiere_autoriz_servicios;
	
	/**
	 * Especifica el formato de las autorizaciones 
	 */
	private String formato_autorizacion;
	
	/**
	 * Almacena los medios de envio del informe
	 */
	private String mediosEnvio [] = new String [3];
	
	/**
	 * Alamacena los correos electronicos asociados al Convenio
	 */
	private HashMap correosElectronicos = new HashMap ();
			
	/**
	 * Posicion del Correo electronico en el mapa
	 */
	
	private int posicionMail;
	
	/**
	 * Este estado permire revisar si en el momento de realizar una adicion de un correo
	 * electronico el estado en que venia ( Modificar o empezar)
	 */
	private String estadoSecundario=new String("empezarGeneral");
	
	
//*************************** FIN Cambios decreto 4747 ANDRES ORTIZ	*********************************
	
	

	/**
	 * Aux para modificar el cod de empresa 
	 */
	private int empresaNuevo;
	
	/**
	 * Aux para modificar formato Factura
	 */
	private int formatoFacturaNuevo;
	
	/**
	 * Colecciï¿½n con los datos del listado, ya sea para consulta,
	 * como tambiï¿½n para bï¿½squeda avanzada (pager)
	 */
	private Collection col=null;
	
	/**
	 * Offset para el pager 
	 */
	private int offset=0;
	
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
	 * Campo donde se restringe por quï¿½ criterios se va a buscar
	 */
	private String criteriosBusqueda[];
	
	/**
	 * Contiene el log con info original para
	 * almacenar esta info en el log tipo Archivo
	 */
	private String logInfoOriginalConvenio;
	
	
	/**
	 * Tipo de contrato (Normal- Capitado)
	 */
	private int tipoContrato;
	
	/**
	 * Nombre del tipo de contrato
	 */
	private String nombreTipoContrato;
	
	/**
	 * Dice si la info adicional en apertura cuenta paciente esta activa o n
	 */		
	private String checkInfoAdicCuenta="false";
	
	/**
	 * indica si es pyp o no.
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
	
	
	private String estadoTemp;
	
	private String numeroDiasVencimiento;
	private String manejaComplejidad;
	private String semanasMinimasCotizacion;
	private String requiereNumeroCarnet;
	private String requiereJustificacionServicios;
	private String requiereJustificacionArticulos;
	
	/**
	 * Requiere Justificacion de artï¿½culos No POS diferentes a los medicamentos (S/N)
	 */
	private String requiereJustArtNoposDifMed;

	private String tipoConvenio;
	
	private int tipoCodigo;
	private int tipoCodigoArt;
	
	private String ajusteServicios;
	private String ajusteArticulos;
	private String interfaz;
	
	
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
	private String tipoLiquidacionScx="";
	
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
	
	/**
	 * 
	 */
	private String encabezadoFactura;
	
	/**
	 * 
	 */
	private String pieFactura;
	
	/**
	 * 
	 */
	private String empresaInstitucion;
	
	/**
	 * 
	 */
	private int institucion;
	
	/**
	 * Variable para Almacenar Los Centro de Costo Plan Especial
	 */
	private ArrayList planEspecialList; 
	
	/**
	 * Variable para Capturar el Centro Costo Plan Especial
	 */
	private String planEspecial;	

	/**
	 * Variable para capturar la cadena a mostrar en la jsp en el select
	 */
	private String nombreCentroCostoPlanEspecial;
	
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
	 * Variable para captura de Dias control Post Operatorio
	 */
	private String diasCPO;
	
	/**
	 * Variable para captura de cantidad maxima citas post operatorio
	 */
	private String cantCPO;
	
	/**
	 * Variable para Radicar Cuentas Negativas
	 * */
	private String radicarCuentasNegativas;
	
	/**
	 * Variables para Asignar en la Factura Como Valor del Paciente el Valor de Abonos
	 * */
	private String asignarFactValorPacValorAbono;
	
	/**
	 * Variable String para manejar si el Campo Aseguradora es S ï¿½ N ? 
	 */
	private String aseguradora;
	
	/**
	 * Variable String para manejar almacenar el Cï¿½digo Aseguradora 
	 */
	private String codigoAseguradora;
	
	/**
	 * Variable para manejar el Valor letras Factura
	 */
	private String valorLetrasFactura;
	
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
	
	/*-----------------------------------------
	 * 		PROPIEDADES ANEXO 809
	 ------------------------------------------*/
	
	/**
	 * Variable para Almacenar Los Centro de Costo Contable
	 */
	private ArrayList ccContableList; 

	/**
	 * Lista de centros de atenciï¿½n
	 */
	private Collection cenAtencContableList;
	
	/**
	 * Variable para Capturar el Centro Costo Contable
	 */
	private String ccContable;
	
	/**
	 * Centro de atenciï¿½n contable
	 */
	private int cenAtencContable;
	
	/**
	 * Variable para capturar la cadena a mostrar en la jsp en el select
	 */
	private String nombreCentroCostoContable;

	/**
	 * Variable para almacenar el cento de atecnciï¿½n contable
	 */
	private String nombreCentroAtencionContable;
	
	
	private String manejaBonos;
	private String requiereBono;
	private String manejaPromociones;
	private String esTargetaCliene;
	private String ingresoBdValido;
	private String ingresoPacienteReqAutorizacion;
	private String reqIngresoValido;
	private String tipoAtencion;
	
	private ArrayList<DtoMediosAutorizacion> mediosAutorizacion;

	private String manejaDescuentoOdontologico;
	
	private boolean ambos;
	
	private HashMap documentoAdjuntoGenerado;
	/*-------------------------------------------------------
	 * 						METODOS
	 --------------------------------------------------------*/
	
	/**
	 * CAMBIO ANEXO 961
	 */
	private String tipoLiquidacionPool;
	/**
	 * 
	 */
	
	private String convenioPoseeContratoActivo;
	
	private boolean tipoContratoCapitado;
	
	private String manejaPresupuestoCapitacion;
	
	private ArrayList<DtoCheckBox> listaCapitacionSubcontratada;
	
	private String convenioManejaAutorizacionCapitada;
	
	public void resetUsuarios()
	{
		logger.info("===> Mira aqui Pipe !!! -> Entre al forma.reset !!!");
		this.adicionarUsuariosConvenio = new HashMap ();
		this.setAdicionarUsuariosConvenio("numRegistros",0);
		this.usuario = "";
		this.tipoUsuario = "";
		this.activo = ConstantesBD.acronimoSi;
		this.posicion = "";
		
	}
	 public void resetMedios(){
		 this.mediosAutorizacion=new ArrayList<DtoMediosAutorizacion>();
			mediosAutorizacion.add(new DtoMediosAutorizacion());
			mediosAutorizacion.add(new DtoMediosAutorizacion());
			mediosAutorizacion.add(new DtoMediosAutorizacion());
			mediosAutorizacion.add(new DtoMediosAutorizacion());
		 
	 }
	public void resetEsConsulta()
	{
		logger.info("===> Voy a resetar esConsulta");
		this.setEsConsulta(false);
	}
	
	public void resetpager()
	{
		logger.info("===> Mira aqui Pipe !!! -> Entre al forma.resetpager :P !!!");
		//PARA EL MANEJO DEL PAGER
    	this.currentPageNumber = 1;
        this.linkSiguiente = "";
        this.offset = 0;
        this.patronOrdenar = "";
    	this.ultimoPatron = "";
	}
	
	public void resetMensaje()
	{
		this.mensaje = new ResultadoBoolean(false);
	}
	
	
	/*-------------------------------------------------------
	 * 						 FIN METODOS
	 --------------------------------------------------------*/
	
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
		Boolean flag = new Boolean(false);
		
		
		if(estado.equals("salir")||estado.equals("guardarModificacion"))
		{				
			errores=super.validate(mapping,request);
			
			
			
			if(empresa<=0)
			{
				errores.add("Campo Empresa vacio", new ActionMessage("errors.required","El campo Empresa"));
			}
			if(tipoRegimen.trim().equals(""))
			{	
				errores.add("Campo Tipo Rï¿½gimen vacio", new ActionMessage("errors.required","El campo Tipo Rï¿½gimen"));
			}
			if(nombre.trim().equals(""))
			{
				errores.add("Nombre del Convenio vacio", new ActionMessage("errors.required","El campo Nombre del Convenio"));
			}
			
			/**
			 * INICIO MODIFICACIï¿½N ANEXO 722 
			 */
			if(aseguradora.equals(ConstantesBD.acronimoSi))
			{
				if(!UtilidadCadena.noEsVacio(codigoAseguradora.trim()))
					errores.add("codigoAseguradora", new ActionMessage("errors.required","El campo Cï¿½digo Aseguradora"));
			}
			/**
			 * INICIO MODIFICACIï¿½N ANEXO 722 
			 */
			
			if(planBeneficios.trim().equals(""))
			{
				errores.add("Plan de Beneficios vacio", new ActionMessage("errors.required","El campo Plan de Beneficios"));
			}
			
			//Por tarea 157024 - El codigo minsalud no se hace requerido cuando el convenio es odontologico. EN BD se hace null el campo.
			if(codigoMinSalud.trim().equals("")&&this.tipoAtencion.equals(ConstantesIntegridadDominio.acronimoTipoAtencionConvenioMedicoGeneral))
			{
				errores.add("Cï¿½digo MinSalud vacio", new ActionMessage("errors.required","El campo Cï¿½digo MinSalud"));
			}
			if(Utilidades.convertirAEntero(tipoConvenio)<=0)
			{
				errores.add("Campo Tipo Convenio vacio", new ActionMessage("errors.required","El campo Tipo Convenio"));
			}
			if(convenioManejaMontoCobro1.equals("Seleccione"))
			{
				errores.add("Campo Convenio Maneja Montos vacio", new ActionMessage("errors.required","El campo Convenio Maneja Montos de Cobro"));
			}
			if(tipoCodigo<0)
			{
				errores.add("Campo tipoCodigo vacio", new ActionMessage("errors.required","El campo Tipo Cï¿½digo"));
			}
			if(tipoCodigoArt<0)
			{
				errores.add("Campo tipoCodigo vacio", new ActionMessage("errors.required","El campo Tipo Cï¿½digo Articulo"));
			}
			if(UtilidadTexto.getBoolean(ValoresPorDefecto.getInstitucionMultiempresa(this.institucion))&&Utilidades.convertirADouble(this.empresaInstitucion)<=0)
			{
				errores.add("", new ActionMessage("errors.required","Empresa-Instituciï¿½n"));
			}
			if(UtilidadTexto.isEmpty(cantCPO))
			{
				errores.add("Campo Cantidad Maxima Citas Control Post Operatorio", new ActionMessage("errors.integerMayorQue"," El campo Cantidad Mï¿½xima Citas Control Post Operatorio", " 0 "));
			}
			else
			{
				if(Utilidades.convertirAEntero(cantCPO)>99999)
				{
					errores.add("Campo Dias Control Post Operatorio", new ActionMessage("errors.integerMenorIgualQue"," El campo Cantidad Mï¿½xima Citas Control Post Operatorio", " 99999 "));
				}
			}
			if(UtilidadTexto.isEmpty(diasCPO))
			{
				errores.add("Campo Dias Control Post Operatorio", new ActionMessage("errors.integerMayorQue"," El campo Dï¿½as Control Post Operatorio", " 0 "));
			}
			else
			{
				if(Utilidades.convertirAEntero(diasCPO)>99999)
				{
					errores.add("Campo Dias Control Post Operatorio", new ActionMessage("errors.integerMenorIgualQue"," El campo Dï¿½as Control Post Operatorio", " 99999 "));
				}
			}
			/*
			 * Ya no aplica, puesto que si el campos en null, quiere decir que tendra el formato estandar en la impresion
			 * de la factura.
						if(formatoFactura<=0)
						{
								errores.add("Formato factura vacio", new ActionMessage("errors.required","El campo Formato factura"));
						}*/
			if(tipoContrato<=0)
			{	
				errores.add("Tipo Contrato no seleccionado", new ActionMessage("errors.required","El campo Tipo Contrato"));
			}
			if(!numeroDiasVencimiento.equals(""))
			{
				try
				{
					int numLineas=Integer.parseInt(numeroDiasVencimiento);
					if(numLineas > 999999999)
						errores.add("MaxPageItems mayor que 100",new ActionMessage("errors.MayorIgualQue",999999999+""));
				}
				catch (Exception e) 
				{
					errores.add("Numero invalido",new ActionMessage("errors.invalid",numeroDiasVencimiento));
				}
			}
			if(!semanasMinimasCotizacion.equals(""))
			{
				try
				{
					int numLineas=Integer.parseInt(semanasMinimasCotizacion);
					if(numLineas > 999999999)
						errores.add("MaxPageItems mayor que 100",new ActionMessage("errors.MayorIgualQue",999999999+""));
				}
				catch (Exception e) 
				{
					errores.add("Numero invalido",new ActionMessage("errors.invalid",semanasMinimasCotizacion));
				}
			}
			/*if(this.pyp.equals("true") && this.unificarPyp.equals("false"))
						{	
								errores.add("Unificar Pyp", new ActionMessage("errors.required"," Unificar Pyp "));
						}*/
			if(tipoTarifaLiqMateCx.equals(""))
			{
				errores.add("Campo Tipo de Tarifa para Liquidacion Materiales Cirugia ", new ActionMessage("errors.required","El campo Tipo de Tarifa para Liquidaciï¿½n Materiales Cirugï¿½a "));
			}
			if(tipoTarifaLiqMateDyt.equals(""))
			{
				errores.add("Campo Tipo de Tarifa para Liquidacion Materiales No Cruentos ", new ActionMessage("errors.required","El campo Tipo de Tarifa para Liquidaciï¿½n Materiales No Cruentos "));
			}
			if(liquidacionTmpFracAdd.equals(""))
			{
				errores.add("Campo Liquidacion de Tiempos por Fracciona Adicional Cumplida ", new ActionMessage("errors.required","El campo Liquidaciï¿½n de Tiempos por Fracciona Adicional Cumplida "));
			}
			if(tipoContrato<=0)
			{
				errores.add("Campo Tipo Contrato ", new ActionMessage("errors.required","El campo Tipo contrato"));
			}
			
			if((formatoFactura == ConstantesBD.codigoFormatoImpresionEstandar || formatoFactura == ConstantesBD.codigoFormatoImpresionVersalles) && (valorLetrasFactura.equals(""))&&!tipoAtencion.equals(ConstantesIntegridadDominio.acronimoTipoAtencionConvenioOdontologico)){
				errores.add("Campo Valor Letras Factura", new ActionMessage("errors.required","El Valor Letras por Factura"));
				flag=true;
			}
			
			
			if(flag==false){
				if(this.valorLetrasFactura==null || this.valorLetrasFactura.trim().equals("")){
						errores.add("Campo Valor Letras por Factura", new ActionMessage("errors.required","El campo Valor Letras por Factura"));
				}
			}
			
			int numReg=Integer.parseInt(this.correosElectronicos.get("numRegistros")+"");
			for(int i=0;i<numReg;i++)
			{
				if((this.correosElectronicos.get("mail_"+i)+"").trim().equals("") &&  (this.correosElectronicos.get("eliminar_"+i)+"").trim().equals(ConstantesBD.acronimoNo))
				{
					errores.add("", new ActionMessage("errors.required","La direccion de correo electronico "));
				}
				else
				{
					for(int l=0;l<i;l++)
					{
						if((this.correosElectronicos.get("mail_"+i)+"").equals(this.correosElectronicos.get("mail_"+l)+""+""))  
			             {		                  		                  
			                  errores.add("YA EXISTE EL REGISTRO.", new ActionMessage("errors.yaExiste","La direccion de Correo "+this.correosElectronicos.get("mail_"+l)+""));                 
			             }
					}
				}
			}
			UsuarioBasico user=(UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
			if(ValoresPorDefecto.getInstitucionManejaMultasPorIncumplimiento(user.getCodigoInstitucionInt()).equals(ConstantesBD.acronimoSi))
			{
				try
				{
					if (!manejaMultasPorIncumplimiento.equals(ConstantesBD.acronimoSi) && !manejaMultasPorIncumplimiento.equals(ConstantesBD.acronimoNo))
					{
						errores.add("", new ActionMessage("errors.required","El Campo Maneja Multas Por incumplimiento de Citas "));
					}
					else if(manejaMultasPorIncumplimiento.equals(ConstantesBD.acronimoSi))
					{
						if(valorMultaPorIncumplimientoCitas.equals(""))
						{
							errores.add("", new ActionMessage("errors.required","Valor Multa Por incumplimiento de Cita "));
						}
						else if(Utilidades.convertirAEntero(valorMultaPorIncumplimientoCitas) <= 0)
						{
							errores.add("entero",new ActionMessage("errors.integerMayorQue","El parï¿½metro  \"El Campo Valor Multa por Incumplimiento de Citas\" ", "0"));
						}
					}
				}
				catch (Exception e) {
					errores.add("", new ActionMessage("errors.required","El Campo Maneja Multas Por incumplimiento de Citas "));
				}
			}
			
			if(this.tipoContratoCapitado){
				if(UtilidadTexto.isEmpty(this.convenioManejaAutorizacionCapitada)){
					errores.add("entero",new ActionMessage(
							"errors.required","El dato de Capitación Subcontratada es requerido"));
				}
			}
			
			if(!errores.isEmpty())
			{
				if(estado.equals("salir"))
					
					if(this.tipoAtencion.equals(ConstantesIntegridadDominio.acronimoTipoAtencionConvenioMedicoGeneral)){
					this.setEstado("empezarGeneral");
					}else{
						if(this.tipoAtencion.equals(ConstantesIntegridadDominio.acronimoTipoAtencionConvenioOdontologico)){
							this.setEstado("empezarOdontologia");
						}	
					}
				
				
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
		else if(estado.equals("resultadoBusquedaAvanzada"))
		{
			errores=super.validate(mapping,request);
		}		
		return errores;
	}

	public void reset(int institucion)
	{
		this.codigo = 0;
		this.codigoStr="";
		this.empresa=0;
		this.tipoRegimen="";
		this.nombre="";
		this.observaciones= "";
		this.planBeneficios="";
		this.codigoMinSalud="";
		this.formatoFactura=0;
		this.activa= true;
		this.convenioManejaMontoCobro=true;
		this.convenioManejaMontoCobro1="true";
		this.checkInfoAdicCuenta="false";
		this.pyp="false";
		
		this.tipoRegimenNuevo="";
		this.empresaNuevo=0;
		this.formatoFacturaNuevo=0;	
		
		this.activaAux=0;
		this.pypAux=0;
		this.razonSocial="";
		this.nombreFormatoFactura="";
		this.nombreTipoRegimen="";
		
		this.logInfoOriginalConvenio="";
		this.tipoContrato=ConstantesBD.codigoNuncaValido;
		this.nombreTipoContrato="";
		this.estadoTemp="";
		this.unificarPyp = "false";
		
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
		
		this.ajusteServicios="";
		this.ajusteArticulos="";
		this.interfaz="";
		
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
		this.institucion=institucion;
		
		planEspecialList = new ArrayList();
		
		this.planEspecial=ConstantesBD.codigoNuncaValido+"";
		this.nombreCentroCostoPlanEspecial="";
		
		this.excentoDeudor = ConstantesBD.acronimoNo;
		this.excentoDocumentoGarantia = ConstantesBD.acronimoNo;
		this.vip = ConstantesBD.acronimoNo;
		
		this.radicarCuentasNegativas = ConstantesBD.acronimoNo;
		this.asignarFactValorPacValorAbono = ConstantesBD.acronimoNo;		
		
		this.cantidadMaxCirugia = "";
		this.cantidadMaxAyudpag = "";
		this.cantCPO="";
		this.diasCPO="";
		this.aseguradora = ConstantesBD.acronimoNo;
		this.codigoAseguradora = "";
		this.valorLetrasFactura="";
		
		//*************	Cambios Anexo 753 Decreto 4747***********
		this.reporte_atencion_ini_urg=ConstantesBD.acronimoNo;
		this.reporte_incons_bd=ConstantesBD.acronimoNo;
		this.generacion_autom_val_urg=ConstantesBD.acronimoNo;
		this.mediosEnvio[0]=new String("");
		this.mediosEnvio[1]=new String("");
	    this.mediosEnvio[2]=new String("");
	    this.correosElectronicos=new HashMap();
	    this.correosElectronicos.put("numRegistros","0");
	    this.posicionMail=0;
	  
	    //***********************************************************
	    
//	    ********** anexo 791 **********
	    this.manejaMultasPorIncumplimiento = "N";
	    this.valorMultaPorIncumplimientoCitas = "";
	    
//	    ********* ANEXO 809 **********
	    this.ccContable = "";
	    this.ccContableList = new ArrayList();
	    this.cenAtencContable=0;
	    this.cenAtencContableList = null;
	    this.nombreCentroCostoContable = "";
	    this.nombreCentroAtencionContable="";
	    
	    this.manejaBonos="";
		this.requiereBono = "";
		this.manejaPromociones = "";
		this.esTargetaCliene = "";
		this.ingresoBdValido = "";
		this.ingresoPacienteReqAutorizacion = "";
		this.reqIngresoValido = "";
		this.manejaDescuentoOdontologico="";
		this.formato_autorizacion=ConstantesIntegridadDominio.acronimoFormatoEstandar;
		this.documentoAdjuntoGenerado=new HashMap();
		this.convenioPoseeContratoActivo="";
		initMediosAutorizacion();
		
       
		
	}
	
	public void resetOdontologia(int institucion)
	{
		this.codigo = 0;
		this.codigoStr="";
		this.empresa=0;
		this.tipoRegimen="";
		this.nombre="";
		this.observaciones= "";
		this.planBeneficios="";
		this.codigoMinSalud="";
		this.formatoFactura=0;
		this.activa= true;
		this.convenioManejaMontoCobro=true;
		this.checkInfoAdicCuenta="false";
		this.pyp="false";
		this.numDocumentosAdjuntos = 0;
	    this.documentosAdjuntosGenerados.clear();
		this.tipoRegimenNuevo="";
		this.empresaNuevo=0;
		this.formatoFacturaNuevo=0;	
		
		this.activaAux=0;
		this.pypAux=0;
		this.razonSocial="";
		this.nombreFormatoFactura="";
		this.nombreTipoRegimen="";
		
		this.logInfoOriginalConvenio="";
		this.tipoContrato=ConstantesBD.codigoTipoContratoEvento;
		this.nombreTipoContrato="";
		this.estadoTemp="";
		this.unificarPyp = "false";
		
		this.numeroDiasVencimiento="";
		this.requiereJustificacionServicios="N";
		this.requiereJustificacionArticulos="N";
		this.requiereJustArtNoposDifMed="";
		this.manejaComplejidad="N";
		this.semanasMinimasCotizacion="";
		this.requiereNumeroCarnet="N";
		this.tipoConvenio=ConstantesBD.codigoNuncaValido+"";
		this.tipoCodigo=ConstantesBD.codigoNuncaValido;
		this.tipoCodigoArt=ConstantesBD.codigoNuncaValido;
		
		this.ajusteServicios="";
		this.ajusteArticulos="";
		this.interfaz="";
		
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
		this.institucion=institucion;
		
		planEspecialList = new ArrayList();
		
		this.planEspecial=ConstantesBD.codigoNuncaValido+"";
		this.nombreCentroCostoPlanEspecial="";
		
		this.excentoDeudor = ConstantesBD.acronimoSi;
		this.excentoDocumentoGarantia = ConstantesBD.acronimoSi;
		this.vip = ConstantesBD.acronimoNo;
		
		this.radicarCuentasNegativas = ConstantesBD.acronimoNo;
		this.asignarFactValorPacValorAbono = ConstantesBD.acronimoNo;		
		
		this.cantidadMaxCirugia = "";
		this.cantidadMaxAyudpag = "";
		this.cantCPO="0";
		this.diasCPO="0";
		this.aseguradora = ConstantesBD.acronimoNo;
		this.codigoAseguradora = "";
		this.valorLetrasFactura="";
		
		//*************	Cambios Anexo 753 Decreto 4747***********
		this.reporte_atencion_ini_urg=ConstantesBD.acronimoNo;
		this.reporte_incons_bd=ConstantesBD.acronimoNo;
		this.generacion_autom_val_urg=ConstantesBD.acronimoNo;
		this.mediosEnvio[0]=new String("");
		this.mediosEnvio[1]=new String("");
	    this.mediosEnvio[2]=new String("");
	    this.correosElectronicos=new HashMap();
	    this.correosElectronicos.put("numRegistros","0");
	    this.posicionMail=0;
	    this.requiere_autoriz_servicios=ConstantesBD.acronimoNo;
	    this.formato_autorizacion=ConstantesIntegridadDominio.acronimoFormatoEstandar;
	    //***********************************************************
	    
//	    ********** anexo 791 **********
	    this.manejaMultasPorIncumplimiento = "N";
	    this.valorMultaPorIncumplimientoCitas = "";
	    
//	    ********* ANEXO 809 **********
	    this.ccContable = "";
	    this.ccContableList = new ArrayList();
	    this.nombreCentroCostoContable = "";
	    
	    this.manejaBonos="";
		this.requiereBono = "";
		this.manejaPromociones = "";
		this.esTargetaCliene = "";
		this.ingresoBdValido = "";
		this.ingresoPacienteReqAutorizacion = "";
		this.reqIngresoValido = "";
		this.manejaDescuentoOdontologico="";
		this.documentoAdjuntoGenerado=new HashMap();
		this.convenioPoseeContratoActivo="";;
		
		initMediosAutorizacion();
       
		
	}
	
	/**
	 * 
	 */
	private void initMediosAutorizacion() {
		
		this.mediosAutorizacion=new ArrayList<DtoMediosAutorizacion>();
		DtoMediosAutorizacion DtoAut;
		
		for(int i=0;i < mediosAutorizacionVector.length ;i++ ){
			
		DtoAut = new DtoMediosAutorizacion();
		
		DtoAut.setTipo(new InfoDatosStr(mediosAutorizacionVector[i], ValoresPorDefecto.getIntegridadDominio(mediosAutorizacionVector[i]).toString() ));
		
		this.mediosAutorizacion.add(DtoAut);
		}
		
	
				
	}
	
	/**
	 * resetea en vector de strings que
	 * contiene los criterios de bï¿½squeda 
	 *
	 */
	public void resetCriteriosBusqueda()
	{
		
		try
		{
			this.mediosEnvio[0]=new String("");
			this.mediosEnvio[1]=new String("");
		    this.mediosEnvio[2]=new String("");
		    
			for(int k=0 ; k<criteriosBusqueda.length ; k++)
				criteriosBusqueda[k]="";
		}catch(Exception e)
		{
			logger.warn(" error en el reset de busqueda "+e);
		}
	}
	
	/**
	 * Este método se encarga de inicializar todos 
	 * los valores de la forma.
	 * @author Angela Aguirre
	 */
	public void reset(){
		this.estado="";
		this.manejaPresupuestoCapitacion="";
		this.convenioManejaAutorizacionCapitada="";
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
     * @return Returns the codigoStr.
     */
    public String getCodigoStr() {
        return codigoStr;
    }
    /**
     * @param codigoStr The codigoStr to set.
     */
    public void setCodigoStr(String codigoStr) {
        this.codigoStr = codigoStr;
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
	 * Retorna el estado en que se encuentra el registro del convenio
	 * @return
	 */
	public String getEstado() {
		return estado;
	}

	/**
	 * Asigna el estado en que se encuentra el registro del convenio
	 * @param string
	 */
	public void setEstado(String string) {
		estado = string;
	}

	/**
	 * Retorna Aux para modificar cod empresa
	 * @return
	 */
	public int getEmpresaNuevo() {
		return empresaNuevo;
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
	 * Retorna Colecciï¿½n para mostrar datos en el pager
	 * @return
	 */
	public Collection getCol() {
		return col;
	}
	
	/**
	 * Asigna Colecciï¿½n para mostrar datos en el pager
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
	 * Retorna los criterios de bï¿½squeda como strings
	 * @return
	 */
	public String[] getCriteriosBusqueda() {
		return criteriosBusqueda;
	}

	/**
	 * Asigna los criterios de bï¿½squeda como strings
	 * @param strings
	 */
	public void setCriteriosBusqueda(String[] strings) {
		criteriosBusqueda = strings;
	}
	
	/**
	 * retorna el log con info original para
	 * almacenar esta info en el log tipo Archivo
	 * @return
	 */
	public String getLogInfoOriginalConvenio() {
		return logInfoOriginalConvenio;
	}

	/**
	 * Asigna el log con info original para
	 * almacenar esta info en el log tipo Archivo
	 * @param string
	 */
	public void setLogInfoOriginalConvenio(String string) {
		logInfoOriginalConvenio = string;
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
	
	/**
	 * 
	 * @return
	 */
	public String getUnificarPyp() {
		return unificarPyp;
	}
	
	/**
	 * 
	 * @param unificarPyp
	 */
	public void setUnificarPyp(String unificarPyp) {
		this.unificarPyp = unificarPyp;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getManejaComplejidad() {
		return manejaComplejidad;
	}
	
	/**
	 * 
	 * @param manejaComplejidad
	 */
	public void setManejaComplejidad(String manejaComplejidad) {
		this.manejaComplejidad = manejaComplejidad;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getNumeroDiasVencimiento() {
		return numeroDiasVencimiento;
	}
	
	/**
	 * 
	 * @param numeroDiasVencimiento
	 */
	public void setNumeroDiasVencimiento(String numeroDiasVencimiento) {
		this.numeroDiasVencimiento = numeroDiasVencimiento;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getRequiereNumeroCarnet() {
		return requiereNumeroCarnet;
	}
	
	/**
	 * 
	 * @param requiereNumeroCarnet
	 */
	public void setRequiereNumeroCarnet(String requiereNumeroCarnet) {
		this.requiereNumeroCarnet = requiereNumeroCarnet;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getSemanasMinimasCotizacion() {
		return semanasMinimasCotizacion;
	}
	
	/**
	 * 
	 * @param semanasMinimasCotizacion
	 */
	public void setSemanasMinimasCotizacion(String semanasMinimasCotizacion) {
		this.semanasMinimasCotizacion = semanasMinimasCotizacion;
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
	 * @param tipoCodigo
	 */
	public void setTipoCodigo(int tipoCodigo) {
		this.tipoCodigo = tipoCodigo;
	}
	/**
	 * @return the tipoCodigoArt
	 */
	public int getTipoCodigoArt() {
		return tipoCodigoArt;
	}
	/**
	 * @param tipoCodigoArt the tipoCodigoArt to set
	 */
	public void setTipoCodigoArt(int tipoCodigoArt) {
		this.tipoCodigoArt = tipoCodigoArt;
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
	 * @return
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

	public String getEmpresaInstitucion() {
		return empresaInstitucion;
	}

	public void setEmpresaInstitucion(String empresaInstitucion) {
		this.empresaInstitucion = empresaInstitucion;
	}

	public int getInstitucion() {
		return institucion;
	}

	public void setInstitucion(int institucion) {
		this.institucion = institucion;
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
	 * 
	 * @return
	 */
	public ArrayList getPlanEspecialList() {
		return planEspecialList;
	}
	
	/**
	 * 
	 * @param planEspecialList
	 */
	public void setPlanEspecialList(ArrayList planEspecialList) {
		this.planEspecialList = planEspecialList;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getPlanEspecial() {
		return planEspecial;
	}

	/**
	 * 
	 * @param planEspecial
	 */
	public void setPlanEspecial(String planEspecial) {
		this.planEspecial = planEspecial;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getNombreCentroCostoPlanEspecial() {
		return nombreCentroCostoPlanEspecial;
	}
	
	/**
	 * 
	 * @param nombreCentroCostoPlanEspecial
	 */
	public void setNombreCentroCostoPlanEspecial(
			String nombreCentroCostoPlanEspecial) {
		this.nombreCentroCostoPlanEspecial = nombreCentroCostoPlanEspecial;
	}

	/**
	 * 
	 * @return
	 */
	public String getExcentoDeudor() {
		return excentoDeudor;
	}
	
	/**
	 * 
	 * @param excentoDeudor
	 */
	public void setExcentoDeudor(String excentoDeudor) {
		this.excentoDeudor = excentoDeudor;
	}

	/**
	 * 
	 * @return
	 */
	public String getExcentoDocumentoGarantia() {
		return excentoDocumentoGarantia;
	}

	/**
	 * 
	 * @param excentoDocumentoGarantia
	 */
	public void setExcentoDocumentoGarantia(String excentoDocumentoGarantia) {
		this.excentoDocumentoGarantia = excentoDocumentoGarantia;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getVip() {
		return vip;
	}
	
	/**
	 * 
	 * @param vip
	 */
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
	 * 
	 * @param patronOrdenar
	 */
	public void setPatronOrdenar(String patronOrdenar) {
		this.patronOrdenar = patronOrdenar;
	}

	/**
	 * 
	 * @return Patron Ordenar
	 */
	public String getPatronOrdenar() {
		return patronOrdenar;
	}
	
	/**
	 * 
	 * @return Ultimo Patrï¿½n
	 */
	public String getUltimoPatron() {
		return ultimoPatron;
	}

	/**
	 * 
	 * @param ultimoPatron
	 */
	public void setUltimoPatron(String ultimoPatron) {
		this.ultimoPatron = ultimoPatron;
	}

	/**
	 * 
	 * @return link siguiente
	 */
	public String getLinkSiguiente() {
		return linkSiguiente;
	}

	/**
	 * 
	 * @param linkSiguiente
	 */
	public void setLinkSiguiente(String linkSiguiente) {
		this.linkSiguiente = linkSiguiente;
	}

	/**
	 * 
	 * @return current page number
	 */
	public int getCurrentPageNumber() {
		return currentPageNumber;
	}

	/**
	 * 
	 * @param currentPageNumber
	 */
	public void setCurrentPageNumber(int currentPageNumber) {
		this.currentPageNumber = currentPageNumber;
	}

	/**
	 * 
	 * @return adicionarUsuariosConvenio
	 */
	public HashMap getAdicionarUsuariosConvenio() {
		return adicionarUsuariosConvenio;
	}

	/**
	 * 
	 * @param adicionarUsuariosConvenio
	 */
	public void setAdicionarUsuariosConvenio(HashMap adicionarUsuariosConvenio) {
		this.adicionarUsuariosConvenio = adicionarUsuariosConvenio;
	}
	
	public Object getAdicionarUsuariosConvenio(String key) {
		return adicionarUsuariosConvenio.get(key);
	}
	public void setAdicionarUsuariosConvenio(String key,Object value) {
		this.adicionarUsuariosConvenio.put(key, value);
	}

	/**
	 * 
	 * @return index
	 */
	public String getIndex() {
		return index;
	}

	/**
	 * 
	 * @param index
	 */
	public void setIndex(String index) {
		this.index = index;
	}

	/**
	 * 
	 * @return mensaje
	 */
	public ResultadoBoolean getMensaje() {
		return mensaje;
	}

	/**
	 * 
	 * @param mensaje
	 */
	public void setMensaje(ResultadoBoolean mensaje) {
		this.mensaje = mensaje;
	}

	/**
	 * 
	 * @return usuario
	 */
	public String getUsuario() {
		return usuario;
	}

	/**
	 * 
	 * @param usuario
	 */
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	/**
	 * 
	 * @return tipoUsuario
	 */
	public String getTipoUsuario() {
		return tipoUsuario;
	}

	/**
	 * 
	 * @param tipoUsuario
	 */
	public void setTipoUsuario(String tipoUsuario) {
		this.tipoUsuario = tipoUsuario;
	}

	/**
	 * 
	 * @return activo
	 */
	public String getActivo() {
		return activo;
	}

	/**
	 * 
	 * @param activo
	 */
	public void setActivo(String activo) {
		this.activo = activo;
	}

	public void setCriterios(HashMap criterios) {
		this.criterios = criterios;
	}

	public HashMap getCriterios() {
		return criterios;
	}
	
	public Object getCriterios(String key) {
		return criterios.get(key);
	}
	public void setCriterios(String key,Object value) {
		this.criterios.put(key, value);
	}

	public void setUsuarios(HashMap<String, Object> usuarios) {
		this.usuarios = usuarios;
	}

	public HashMap<String, Object> getUsuarios() {
		return usuarios;
	}

	public void setPosicion(String posicion) {
		this.posicion = posicion;
	}

	public String getPosicion() {
		return posicion;
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
	 * 
	 * @param esConsulta
	 */
	public void setEsConsulta(boolean esConsulta) {
		this.esConsulta = esConsulta;
	}

	/**
	 * 
	 * @return esConsulta
	 */
	public boolean getEsConsulta() {
		return esConsulta;
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

	public void setMediosEnvio(String[] mediosEnvio) {
		this.mediosEnvio=mediosEnvio;
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
	public int getPosicionMail() {
		return posicionMail;
	}
    
	/**
	 * 
	 * @param posicionMail
	 */
	public void setPosicionMail(int posicionMail) {
		this.posicionMail = posicionMail;
	}
    
	/**
	 * 
	 * @return
	 */
	public String getEstadoSecundario() {
		return estadoSecundario;
	}
    
	/**
	 * 
	 * @param estadoSecundario
	 */
	public void setEstadoSecundario(String estadoSecundario) {
		this.estadoSecundario = estadoSecundario;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getRequiere_autoriz_servicios() {
		return requiere_autoriz_servicios;
	}

	/**
	 * 
	 * @param requiere_autoriz_servicios
	 */
	public void setRequiere_autoriz_servicios(String requiere_autoriz_servicios) {
		this.requiere_autoriz_servicios = requiere_autoriz_servicios;
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
	
	/*--------------------------------------------------------------------------
	 *    METODOS PARA MANEJAR LAS MULTAS POR INCUMPLIMIENTO DE CITAS
	 ---------------------------------------------------------------------------*/
	
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

	/*----------------------------
	 * GETTERS SETTERS ANEXO 809
	 -----------------------------*/
	
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

	/**
	 * @return the ccContable
	 */
	public String getCcContable() {
		return ccContable;
	}

	/**
	 * @param ccContable the ccContable to set
	 */
	public void setCcContable(String ccContable) {
		this.ccContable = ccContable;
	}

	/**
	 * @return the nombreCentroCostoContable
	 */
	public String getNombreCentroCostoContable() {
		return nombreCentroCostoContable;
	}

	/**
	 * @param nombreCentroCostoContable the nombreCentroCostoContable to set
	 */
	public void setNombreCentroCostoContable(String nombreCentroCostoContable) {
		this.nombreCentroCostoContable = nombreCentroCostoContable;
	}

	/**
	 * @return the logger
	 */
	public Logger getLogger() {
		return logger;
	}

	/**
	 * @param logger the logger to set
	 */
	public void setLogger(Logger logger) {
		this.logger = logger;
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
	 * @return the odontologiaActivo
	 */
	public boolean isOdontologiaActivo() {
		return odontologiaActivo;
	}

	/**
	 * @return the odontologiaActivo
	 */
	public boolean getOdontologiaActivo() {
		return odontologiaActivo;
	}

	/**
	 * @param odontologiaActivo the odontologiaActivo to set
	 */
	public void setOdontologiaActivo(boolean odontologiaActivo) {
		this.odontologiaActivo = odontologiaActivo;
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
	public void setMediosAutorizacion(ArrayList<DtoMediosAutorizacion> mediosAutorizacion) {
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
		ConvenioForm.mediosAutorizacionVector = mediosAutorizacionVector;
	}
	/**
	 * @return the manejaDescuentoOdontologico
	 */
	public String getManejaDescuentoOdontologico() {
		return manejaDescuentoOdontologico;
	}
	/**
	 * @param manejaDescuentoOdontologico the manejaDescuentoOdontologico to set
	 */
	public void setManejaDescuentoOdontologico(String manejaDescuentoOdontologico) {
		this.manejaDescuentoOdontologico = manejaDescuentoOdontologico;
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
	 * @return the cenAtencContableList
	 */
	public Collection getCenAtencContableList() {
		return cenAtencContableList;
	}
	/**
	 * @param cenAtencContableList the cenAtencContableList to set
	 */
	public void setCenAtencContableList(Collection cenAtencContableList) {
		this.cenAtencContableList = cenAtencContableList;
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
	 * @return the nombreCentroAtencionContable
	 */
	public String getNombreCentroAtencionContable() {
		return nombreCentroAtencionContable;
	}
	/**
	 * @param nombreCentroAtencionContable the nombreCentroAtencionContable to set
	 */
	public void setNombreCentroAtencionContable(String nombreCentroAtencionContable) {
		this.nombreCentroAtencionContable = nombreCentroAtencionContable;
	}

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
	public void setTipoLiquidacionPool(String tipoLiquidacionPool) {
		this.tipoLiquidacionPool = tipoLiquidacionPool;
	}
	public String getTipoLiquidacionPool() {
		return tipoLiquidacionPool;
	}
	public HashMap getDocumentoAdjuntoGenerado() {
		return documentoAdjuntoGenerado;
	}
	public void setDocumentoAdjuntoGenerado(HashMap documentoAdjuntoGenerado) {
		this.documentoAdjuntoGenerado = documentoAdjuntoGenerado;
	}
	public String getConvenioPoseeContratoActivo() {
		return convenioPoseeContratoActivo;
	}
	public void setConvenioPoseeContratoActivo(String convenioPoseeContratoActivo) {
		this.convenioPoseeContratoActivo = convenioPoseeContratoActivo;
	}
	public boolean isConvenioManejaMontoCobro() {
		return convenioManejaMontoCobro;
	}
	public void setConvenioManejaMontoCobro(boolean convenioManejaMontoCobro) {
		this.convenioManejaMontoCobro = convenioManejaMontoCobro;
	}
	
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo tipoContratoCapitado
	
	 * @return retorna la variable tipoContratoCapitado 
	 * @author Angela Maria Aguirre 
	 */
	public boolean isTipoContratoCapitado() {
		return tipoContratoCapitado;
	}
	
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo tipoContratoCapitado
	
	 * @param valor para el atributo tipoContratoCapitado 
	 * @author Angela Maria Aguirre 
	 */
	public void setTipoContratoCapitado(boolean tipoContratoCapitado) {
		this.tipoContratoCapitado = tipoContratoCapitado;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo manejaPresupuestoCapitacion
	
	 * @return retorna la variable manejaPresupuestoCapitacion 
	 * @author Angela Maria Aguirre 
	 */
	public String getManejaPresupuestoCapitacion() {
		return manejaPresupuestoCapitacion;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo manejaPresupuestoCapitacion
	
	 * @param valor para el atributo manejaPresupuestoCapitacion 
	 * @author Angela Maria Aguirre 
	 */
	public void setManejaPresupuestoCapitacion(String manejaPresupuestoCapitacion) {
		this.manejaPresupuestoCapitacion = manejaPresupuestoCapitacion;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo listaCapitacionSubcontratada
	
	 * @return retorna la variable listaCapitacionSubcontratada 
	 * @author Angela Maria Aguirre 
	 */
	public ArrayList<DtoCheckBox> getListaCapitacionSubcontratada() {
		return listaCapitacionSubcontratada;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo listaCapitacionSubcontratada
	
	 * @param valor para el atributo listaCapitacionSubcontratada 
	 * @author Angela Maria Aguirre 
	 */
	public void setListaCapitacionSubcontratada(
			ArrayList<DtoCheckBox> listaCapitacionSubcontratada) {
		this.listaCapitacionSubcontratada = listaCapitacionSubcontratada;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo convenioManejaAutorizacionCapitada
	
	 * @return retorna la variable convenioManejaAutorizacionCapitada 
	 * @author Angela Maria Aguirre 
	 */
	public String getConvenioManejaAutorizacionCapitada() {
		return convenioManejaAutorizacionCapitada;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo convenioManejaAutorizacionCapitada
	
	 * @param valor para el atributo convenioManejaAutorizacionCapitada 
	 * @author Angela Maria Aguirre 
	 */
	public void setConvenioManejaAutorizacionCapitada(
			String convenioManejaAutorizacionCapitada) {
		this.convenioManejaAutorizacionCapitada = convenioManejaAutorizacionCapitada;
	}
	public void setConvenioManejaMontoCobro1(String convenioManejaMontoCobro1) {
		this.convenioManejaMontoCobro1 = convenioManejaMontoCobro1;
	}
	public String getConvenioManejaMontoCobro1() {
		return convenioManejaMontoCobro1;
	}
}