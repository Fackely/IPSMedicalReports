/*
 * Junio 27, 2006
 */
package com.princetonsa.mundo.interfaz;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;

import util.ConstantesBD;
import util.UtilidadFecha;
import util.UtilidadTexto;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.interfaz.GeneracionInterfazDao;
import com.princetonsa.mundo.cargos.Convenio;
import com.princetonsa.mundo.facturacion.ParametrizacionInstitucion;

/**
 * 
 * @author sgomez
 * Objeto que representa la generación interfaz facturación
 */
public class GeneracionInterfaz 
{

	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger=Logger.getLogger(GeneracionInterfaz.class);
	
	/**
	 * DAO para el manejo de los Tipos de Monitoreo
	 */
	private GeneracionInterfazDao generacionInterfazDao=null;
	
	//*******ATRIBUTOS*************************************************************
	/**
	 * Mapa que almacena los parametros de busqueda de facturas
	 */
	private HashMap campos = new HashMap();
	
	/**
	 * Mapa que almacena los registros parametrizados 
	 */
	private HashMap registros = new HashMap();
	
	/**
	 * Almacena el numero de registros del mapa registros
	 */
	private int numRegistros;
	
	/**
	 * Mapa que almacena la informacion general de facturas
	 */
	private HashMap facturas = new HashMap();
	
	/**
	 * Almacena el numero de facturas del mapa facturas 
	 */
	private int numFacturas;
	
	/**
	 * Mapa que almacena el detalle Servicios/Articulos de la factura
	 */
	private HashMap detalleFactura = new HashMap();
	
	/**
	 * Almacena el número de registros de detalle de la factura
	 */
	private int numDetalleFactura;
	
	/**
	 * Código de la institucion
	 */
	private String institucion;
	/**
	 * Path Absoluto del archivo
	 */
	private String pathArchivo;
	
	/**
	 * Nombre Archivo
	 */
	private String nombreArchivo;
	
	/**
	 * Parh Absoluto del archivo inconsistencias
	 */
	private String pathInconsistencias;
	
	
	/**
	 * Nombre archivo inconsistencias
	 */
	private String nombreInconsistencias;
	
	/**
	 * Indica si hubo inconsistencias
	 */
	private boolean huboInconsistencias;
	
	/**
	 * Secuencia de registros
	 */
	private long secuencia;
	
	/**
	 * Variable que indica si los campos cumplen todos los
	 * requerimientos para generar el registro repectivo
	 */
	private boolean generarRegistro;
	
	/**
	 * Número de campos de tipo valor de un registro
	 */
	private int numCamposValor;
	/**
	 * Número de campos de tipo valor con cantidad 0
	 * en un registro
	 */
	private int numCamposValorCero;
	
	/**
	 * Errores encontrados durante el proceso
	 * de generación de interfaz
	 */
	private ActionErrors errores;
	
	//atributos de parametrizacion campos ------------------
	private String separadorCampos;
	private int separadorDecimales;
	private String identificadorFinArchivo;
	private boolean presentaDevolucionPaciente;
	private boolean valorNegativoDevolPaciente;
	private String descripcionDebito;
	private String descripcionCredito;
	//------------------------------------------------------
	
	//atributos de información de busqueda-------------------
	private String fechaInicial;
	private String fechaFinal;
	//descripcion del tipo de interfaz
	private String tipoInterfaz;
	private int codigoTipoInterfaz;
	private String usuarioResponsable;
	private String documentoInicial;
	private String documentoFinal;
	//---------------------------------------------------------
	
	//atributos usados para el archivos de inconsistencias------------
	private HashMap datosInconsistencias = new HashMap();
	private int numInconsistencias; //número de inconsistencias
	//----------------------------------------------------------------
	
	//atributos usados para cargar archivo salida***************
	private HashMap mapArchivo = new HashMap();
	private int numMapArchivo; //número de registros del archivo
	
	//********CONSTRUCTORES & INICIALIZADORES ***************************************++
	/**
	 * Constructor
	 */
	public GeneracionInterfaz() {
		this.clean();
		this.init(System.getProperty("TIPOBD"));
	}
	/**
	 * método para inicializar los datos
	 *
	 */
	public void clean()
	{
		this.campos = new HashMap();
		this.registros = new HashMap();
		this.numRegistros = 0;
		this.facturas = new HashMap();
		this.numFacturas = 0;
		this.detalleFactura = new HashMap();
		this.numDetalleFactura = 0;
		this.institucion = "";
		
		this.pathArchivo = "";
		this.nombreArchivo = "";
		this.pathInconsistencias = "";
		this.nombreInconsistencias = "";
		this.huboInconsistencias = false;
		
		//atributos de parameteizacion campos********
		this.separadorCampos = "";
		this.separadorDecimales = 0;
		this.identificadorFinArchivo = "";
		this.presentaDevolucionPaciente = false;
		this.valorNegativoDevolPaciente = false;
		this.descripcionDebito = "";
		this.descripcionCredito = "";
		
		this.secuencia = 0;
		this.generarRegistro = true;
		this.numCamposValor = 0;
		this.numCamposValorCero = 0;
		
		this.errores = new ActionErrors();
		
		//atributos de información de búsqueda-----------
		this.fechaInicial = "";
		this.fechaFinal = "";
		this.tipoInterfaz = "";
		this.codigoTipoInterfaz = 0;
		this.usuarioResponsable = "";
		this.documentoInicial = "";
		this.documentoFinal = "";
		//----------------------------------------------
		
		//atributos de inconsistencias-----------------
		this.datosInconsistencias = new HashMap();
		this.numInconsistencias = 0;
		//----------------------------------------------
		
		//atributos para cargar la información del archivo--------------
		this.mapArchivo = new HashMap();
		this.numMapArchivo = 0;
		//---------------------------------------------------------------
		 
	}
	
	/**
	 * Inicializa el acceso a bases de datos de este objeto.
	 * @param tipoBD el tipo de base de datos que va a usar este objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 */
	public void init(String tipoBD) 
	{
		if (generacionInterfazDao == null) 
		{
			// Obtengo el DAO que encapsula las operaciones de BD de este objeto
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			generacionInterfazDao = myFactory.getGeneracionInterfazDao();
		}	
	}
	
	//**********METODOS**********************************************+
	/**
	 * Método implementado para consultar las facturas/anulaciones
	 * según los campos parametrizados
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap consultarFacturasAnulaciones(Connection con)
	{
		return generacionInterfazDao.consultarFacturasAnulaciones(con,this.campos);
	}
	
	/**
	 * Método implementado para consultar las generaciones de interfaz previas
	 * @param con
	 * @return
	 */
	public HashMap consultarGeneracionesInterfazPrevias(Connection con)
	{
		return generacionInterfazDao.consultarGeneracionesInterfazPrevias(con,this.campos);
	}
	
	/**
	 * Método implementado para generar la Interfaz
	 * @param con
	 * @return
	 */
	public boolean generarInterfaz(Connection con)
	{
		
		
		String registro = ""; //variable donde se almacena una linea del archivo
		this.secuencia = 0; //se inicia secuencia
		boolean exito = true; //determina el éxito de la generacion
		this.setHuboInconsistencias(false);
		this.numInconsistencias = 0;
		this.datosInconsistencias = new HashMap();
		
		try
		{
			//PREPARACION DEL ARCHIVO********************************
			File archivo=new File(this.pathArchivo,this.nombreArchivo);
			FileWriter stream=new FileWriter(archivo,false); //se coloca false para el caso de que esté repetido
			BufferedWriter buffer=new BufferedWriter(stream);
			
			
			//******ITERACION DE FACTURAS/ANULACIONES***********************************
			for(int i=0;i<this.numFacturas;i++)
			{
				
				//******ITERACION DE REGISTROS******************************************
				for(int j=0;j<this.numRegistros;j++)
				{
					//se re-inicia registro
					registro = "";
					//se consultan los campos del registro
					this.setRegistros("campos_"+j,generacionInterfazDao.consultarCamposRegistroInterfaz(con,this.getRegistros("codigo_"+j).toString()));
					
					//se toma el tipo de registro
					int tipoRegistro = Integer.parseInt(this.getRegistros("codigoTipoRegistro_"+j).toString());
					
					//se toma el tipo de interfaz del registro
					int tipoInterfaz = Integer.parseInt(this.getRegistros("codigoTipoInterfaz_"+j).toString());
					
					//El tipo de interfaz del registro debe corresponder con la anulacion o factura 
					if((tipoInterfaz==ConstantesBD.tipoInterfazAnulacion&&!this.getFacturas("consecutivo_anulacion_"+i).toString().equals(""))||
					   (tipoInterfaz==ConstantesBD.tipoInterfazFacturacion&&this.getFacturas("consecutivo_anulacion_"+i).toString().equals("")))
					{
						switch(tipoRegistro)
						{
							/**REGISTRO DE TIPO TOTAL FACTURA**/
							case ConstantesBD.tipoRegistroInterfazTotalesFactura:
								registro = generarRegistroTotalFactura(con,i,j,tipoRegistro);
							break;
							
							/**REGISTRO DE TIPO SERVICIO FACTURA **/
							case ConstantesBD.tipoRegistroInterfazServiciosFactura:
								registro = generarRegistrosServiciosFactura(con,i,j,tipoRegistro);
							break;
							
							/**REGISTRO DE TIPO ARTICULO FACTURA **/
							case ConstantesBD.tipoRegistroInterfazArticulosFactura:
								registro = generarRegistrosArticulosFactura(con,i,j,tipoRegistro);
							break;
								
							
						}
						
						buffer.write(registro);
					}
					
				}
				//******FIN ITERACIÓN DE REGISTROS**************************************
			}
			//******FIN ITERACION DE FACTURAS/ANULACIONES******************************
			
			buffer.write(this.identificadorFinArchivo);
			buffer.close();
			//verificacion de las inconsistencias
			if(this.isHuboInconsistencias())
			{
				exito = false;
				archivo.delete();
				if(this.generarInconsistencias())
				{
					this.errores.add("Inconsistencias",
						new ActionMessage("error.interfaz.generacionInterfaz.inconsistencias",
							this.pathInconsistencias+System.getProperty("file.separator")+this.nombreInconsistencias));
				}
			}
			else
			{
				//se elimina archivo inconsistencias si existe
				File archivoIncon=new File(this.pathInconsistencias,this.nombreInconsistencias);
				if(archivoIncon.exists())
					archivoIncon.delete();
				
			}
			
			//*****SE INSERTAR INFORMACIÓN DE AUDITORÍA EN BD**************************************
			this.setCampos("tipoInterfaz",this.codigoTipoInterfaz);
			this.setCampos("institucion",this.institucion);
			this.setCampos("usuario",this.usuarioResponsable);
			this.setCampos("fechaInicial",this.fechaInicial);
			this.setCampos("fechaFinal",this.fechaFinal);
			this.setCampos("documentoInicial",this.documentoInicial);
			this.setCampos("documentoFinal",this.documentoFinal);
			this.setCampos("exitoso",exito);
			this.setCampos("pathSalida",this.pathArchivo+System.getProperty("file.separator")+this.nombreArchivo);
			this.setCampos("pathInconsistencias",this.pathInconsistencias+System.getProperty("file.separator")+this.nombreInconsistencias);
			generacionInterfazDao.insertarAuditoriaGeneracion(con,campos);
			//************************************************************************************************
			
			return exito;
		}
		catch(FileNotFoundException e)
		{
			logger.error("No se pudo encontrar el archivo "+this.nombreArchivo+" al generarlo: "+e);
			this.errores.add("No se pudo encontrar el archivo",new ActionMessage("error.interfaz.generacionInterfaz.falloGeneracion",this.nombreArchivo));
			return false;
		}
		catch(IOException e)
		{
			logger.error("Error en los streams del archivo "+this.nombreArchivo+": "+e);
			this.errores.add("Error en los streams del archivo",new ActionMessage("error.interfaz.generacionInterfaz.falloGeneracion","Error accesando al archivo "+this.nombreArchivo));
			return false;
		}
	}
	
	
	/**
	 * Método implementado para generar las inconsistencias
	 *
	 */
	private boolean generarInconsistencias() 
	{
		try
		{
			
			String registroIncon="";
			String aux = "";
			
			//apertura de archivo Incon
			
			File archivoIncon=new File(this.pathInconsistencias,this.nombreInconsistencias);
			FileWriter streamIncon=new FileWriter(archivoIncon,false); //se coloca false para el caso de que esté repetido
			BufferedWriter bufferIncon=new BufferedWriter(streamIncon);
			
			//*********EDICIÓN DEL ENCABEZADO DEL ARCHIVO**********************
			registroIncon += "Tipo de interfaz: "+this.tipoInterfaz+"    " +
				"Fechas generadas: "+this.fechaInicial+" - "+this.fechaFinal+"    " +
				"Fecha generación: "+UtilidadFecha.getFechaActual()+"    " +
				"Hora generación: "+UtilidadFecha.getHoraActual()+"    " +
				"Usuario responsable: "+this.usuarioResponsable+"\n\n\n\n";
			bufferIncon.write(registroIncon);
			//*********************************************************************
			
			//******EDICION DE LAS COLUMNAS**********************************
			registroIncon = this.editarEspacios("Número de documento",19,30,false)+
				this.editarEspacios("Tipo de documento",17,20,false)+
				this.editarEspacios("Número de solicitud",19,30,false)+
				"Detalle inconsistencia\n\n";
			bufferIncon.write(registroIncon);
			//***************************************************************
			
			//***EDICION DEL DETALLE******************************************
			for(int i=0;i<this.numInconsistencias;i++)
			{
				registroIncon = "";
				
				aux = this.datosInconsistencias.get("numeroDocumento_"+i).toString();
				registroIncon += this.editarEspacios(aux,aux.length(),30,false);
				aux = this.datosInconsistencias.get("tipoDocumento_"+i).toString();
				registroIncon += this.editarEspacios(aux,aux.length(),20,false);
				aux = this.datosInconsistencias.get("numeroSolicitud_"+i).toString();
				registroIncon += this.editarEspacios(aux,aux.length(),30,false);
				aux = this.datosInconsistencias.get("detalleInconsistencia_"+i).toString();
				registroIncon += aux+"\n";
				bufferIncon.write(registroIncon);
				
			}
			//*****************************************************************
			
			
			
			bufferIncon.close();
			return true;
			
		}
		catch(FileNotFoundException e)
		{
			logger.error("No se pudo encontrar el archivo "+this.nombreInconsistencias+" al generarlo: "+e);
			this.errores.add("No se pudo encontrar archivo",new ActionMessage("error.interfaz.generacionInterfaz.falloInconsistencias","No se pudo encontrar el archivo "+this.nombreInconsistencias));
			return false;
		}
		catch(IOException e)
		{
			logger.error("Error en los streams del archivo "+this.nombreInconsistencias+": "+e);
			this.errores.add("Error en los streams del archivo",new ActionMessage("error.interfaz.generacionInterfaz.falloInconsistencias","Error accesando al archivo "+this.nombreInconsistencias));
			return false;
		}
		
	}
	/**
	 * Método implementado para editar registros de tipo ARTICULOS FACTURA
	 * @param con
	 * @param posF (posicion mapa donde se encuentra la informacion de facturas)
	 * @param posR (posicion mapa donde se ecnuentran los campos del registro)
	 * @param tipoRegistro
	 * @return
	 */
	private String generarRegistrosArticulosFactura(Connection con, int posF, int posR, int tipoRegistro) 
	{
		String registroFactura = "";
		String registro = "";
		
		//***CONSULTAR DETALLE ARTÍCULOS DE LA FACTURA****************+
		this.setDetalleFactura(generacionInterfazDao.consultaDetalleArticulosFactura(con,this.getFacturas("codigo_"+posF).toString()));
		this.setNumDetalleFactura(Integer.parseInt(this.getDetalleFactura("numRegistros").toString()));
		
		//*****ITERACION DEL DETALLE DE ARTÍCULOS***********************
		for(int j=0;j<this.getNumDetalleFactura();j++)
		{
		
			// ******* ITERACION DE LOS CAMPOS********************************
			HashMap camposReg = (HashMap)this.getRegistros("campos_"+posR);
			int numCamposReg = Integer.parseInt(camposReg.get("numRegistros").toString());
			this.secuencia++;
			this.generarRegistro = true;
			//campos que se utilizan para verificar si los todos los campos tipo VALOR
			//del registro tienen cantidad 0 
			this.numCamposValor = 0;
			this.numCamposValorCero = 0;
			//se blanqque registro
			registro = "";
			
			for(int i=0;i<numCamposReg;i++)
			{
				
				if(i>0)
					registro+=this.separadorCampos;
				registro += obtenerValorCampo(con,posF,posR,camposReg,i,j,tipoRegistro);
			}
			//****************************************************************
			
			//se verifica estado de los campos tipo VALOR
			if(this.numCamposValorCero>=this.numCamposValor)
				this.generarRegistro = false;
			
			//se termina un registro
			if(generarRegistro)
				registroFactura+=registro+"\n";
			else
				this.secuencia--; //se disminuye secuencia
		}
		//**************************************************************
		return registroFactura;
	}
	/**
	 * Método implementado para editar los registros de tipo SERVICIOS FACTURA
	 * @param con
	 * @param posF (posicion mapa donde se encuentra la informacion de facturas)
	 * @param posR (posicion mapa donde se ecnuentran los campos del registro) 
	 * @param tipoRegistro 
	 *  
	 * @return
	 */
	private String generarRegistrosServiciosFactura(Connection con, int posF, int posR, int tipoRegistro) 
	{
		String registroFactura = "";
		String registro = "";
		
		//***CONSULTAR DETALLE SERVICIOS DE LA FACTURA****************+
		this.setDetalleFactura(generacionInterfazDao.consultaDetalleServiciosFactura(con,this.getFacturas("codigo_"+posF).toString()));
		this.setNumDetalleFactura(Integer.parseInt(this.getDetalleFactura("numRegistros").toString()));
		
		//*****ITERACION DEL DETALLE DE SERVICIOS***********************
		for(int j=0;j<this.getNumDetalleFactura();j++)
		{
		
			// ******* ITERACION DE LOS CAMPOS********************************
			HashMap camposReg = (HashMap)this.getRegistros("campos_"+posR);
			int numCamposReg = Integer.parseInt(camposReg.get("numRegistros").toString());
			this.secuencia++;
			this.generarRegistro = true;
			//campos que se utilizan para verificar si los todos los campos tipo VALOR
			//del registro tienen cantidad 0 
			this.numCamposValor = 0;
			this.numCamposValorCero = 0;
			//se inicia el registro
			registro = "";
			
			for(int i=0;i<numCamposReg;i++)
			{
				
				if(i>0)
					registro+=this.separadorCampos;
				registro += obtenerValorCampo(con,posF,posR,camposReg,i,j,tipoRegistro);
				
			}
			//****************************************************************
			
			
			
			//se verifica estado de los campos tipo VALOR
			if(this.numCamposValorCero>=this.numCamposValor)
				this.generarRegistro = false;
			
			//se termina un registro
			if(generarRegistro)
				registroFactura+=registro+"\n";
			else
				this.secuencia--; //se disminuye secuencia
		}
		//**************************************************************
		return registroFactura;
	}
	/**
	 * Método implementado para editar un registro TOTAL FACTURA para el archivo
	 * @param con
	 * @param posF (posicion mapa donde se encuentra la informacion de facturas)
	 * @param posR (posicion mapa donde se encuentran los campos del registro)
	 * @param tipoRegistro 
	 * @return
	 */
	private String generarRegistroTotalFactura(Connection con, int posF, int posR, int tipoRegistro) 
	{
		String registroFactura = "";
		// ******* ITERACION DE LOS CAMPOS********************************
		HashMap camposReg = (HashMap)this.getRegistros("campos_"+posR);
		int numCamposReg = Integer.parseInt(camposReg.get("numRegistros").toString());
		
		this.secuencia++;
		this.generarRegistro = true;
		//campos que se utilizan para verificar si los todos los campos tipo VALOR
		//del registro tienen cantidad 0 
		this.numCamposValor = 0;
		this.numCamposValorCero = 0;
		
		for(int i=0;i<numCamposReg;i++)
		{
			
			if(!registroFactura.equals(""))
				registroFactura+=this.separadorCampos;
			registroFactura += obtenerValorCampo(con,posF,posR,camposReg,i,-1,tipoRegistro);
			
		}
		
		
		
		//se verifica estado de los campos tipo VALOR
		if(this.numCamposValorCero>=this.numCamposValor)
			this.generarRegistro = false;
		//se termina el registro
		if(generarRegistro)
			registroFactura += "\n";
		else
		{
			registroFactura = "";
			this.secuencia--; //se disminuye secuencia
		}
		//****************************************************************
		return registroFactura;
	}
	
	/**
	 * Método implementado para obtener el valor del campo
	 * @param con
	 * @param posF (posicion actual del mapa de Facturas)
	 * @param posR (posicion actual del mapa de Registros)
	 * @param camposReg (Mapa de los campos)
	 * @param posC (posicion actual del mapa camposReg)
	 * @param posD (posicion actual del mapa detalleFactura)
	 * @param tipoRegistro 
	 * @return
	 */
	private String obtenerValorCampo(Connection con, int posF, int posR, HashMap camposReg, int posC, int posD, int tipoRegistro) 
	{
		//*********VARIABLES AUXILIARES**************************************
		String valor = "";
		String mensajeError = ""; //almacena mensaje error
		String hora = ""; //variable auxiliar para guardar la hora
		double valorD = 0; //variable auxiliar para manejar el valor
		HashMap paramCuentas = new HashMap(); //mapa para pasar los parámetros de consulta de cuentas interfaz
		boolean presentaInconsistencia = false; //indicativo temporal que avisa si el campo tuvo inconsistencia
		String descCampo = " (" +
			"Registro: "+this.getRegistros("consecutivo_"+posR).toString()+", "+
			"Orden del campo: "+camposReg.get("orden_campo_"+posC).toString()+", "+
			"Tipo de Campo: "+camposReg.get("nombre_campo_"+posC).toString()+
			")";
		
		
		
		
		//********************************************************************
		
		//se toma el tipo de campo
		int tipoCampo = Integer.parseInt(camposReg.get("tipo_"+posC).toString());
		int tipoValorCampo = 0;
		
		switch(tipoCampo)
		{
			//CAMPO SECUENCIA******************************************************
			case ConstantesBD.codigoTipoCampoSecuencia:
				valor = this.secuencia+"";
				valor = this.limpiarCaracteresInvalidos(valor,false);
			break;
			
			//CAMPO LIBRE******************************************************
			case ConstantesBD.codigoTipoCampoLibre:
				valor = camposReg.get("descripcion_"+posC).toString();
				valor = this.limpiarCaracteresInvalidos(valor,false);
				if(valor.equals(""))
					mensajeError = "No existe información "+descCampo;
			break;
			
			//CAMPO NUMERO DOCUMENTO******************************************************
			case ConstantesBD.codigoTipoCampoNumeroDocuemnto:
				//se toma valor del campo
				tipoValorCampo = Integer.parseInt(camposReg.get("codigo_valor1_"+posC).toString());
				switch (tipoValorCampo)
				{
					case ConstantesBD.valorCampoNumeroFactura:
						valor = this.getFacturas("consecutivo_factura_"+posF).toString();
					break;
					
					case ConstantesBD.valorCampoNumeroAnulacion:
						valor = this.getFacturas("consecutivo_anulacion_"+posF).toString();
					break;
					
					case ConstantesBD.valorCampoNumeroOrden:
						valor = this.getDetalleFactura("orden_"+posD).toString();
					break;
				}
				valor = this.limpiarCaracteresInvalidos(valor,false);
				if(valor.equals(""))
					mensajeError = "No existe información para el "+camposReg.get("nombre_valor1_"+posC)+descCampo;
			break;
			
			//CAMPO PREFIJO FACTURA***************************************************
			case ConstantesBD.codigoTipoCampoPrefijoFactura:
				valor = this.getFacturas("prefijo_factura_"+posF).toString();
				valor = this.limpiarCaracteresInvalidos(valor,false);
				if(valor.equals(""))
					mensajeError = "No existe información "+descCampo;
			break;
			
			//CAMPO FECHA-AÑO********************************************************
			case ConstantesBD.codigoTipoCampoFehcaAnio:
				//se toma valor del campo
				tipoValorCampo = Integer.parseInt(camposReg.get("codigo_valor1_"+posC).toString());
				switch (tipoValorCampo)
				{
					case ConstantesBD.valorCampoAnioFactura:
						valor = this.getFacturas("anio_factura_"+posF).toString();
					break;
					
					case ConstantesBD.valorCampoAnioAnulacion:
						valor = this.getFacturas("anio_anulacion_"+posF).toString();
					break;
					
					case ConstantesBD.valorCampoAnioGeneracion:
						valor = UtilidadFecha.getMesAnioDiaActual("anio") + "";
					break;
				}
				valor = this.limpiarCaracteresInvalidos(valor,false);
				if(valor.equals(""))
					mensajeError = "No existe información para "+camposReg.get("nombre_valor1_"+posC)+descCampo;
			break;
			
			//CAMPO FECHA-MES**********************************************************
			case ConstantesBD.codigoTipoCampoFechaMes:
				//se toma valor del campo
				tipoValorCampo = Integer.parseInt(camposReg.get("codigo_valor1_"+posC).toString());
				switch (tipoValorCampo)
				{
					case ConstantesBD.valorCampoMesFactura:
						valor = this.getFacturas("mes_factura_"+posF).toString();
					break;
					
					case ConstantesBD.valorCampoMesAnulacion:
						valor = this.getFacturas("mes_anulacion_"+posF).toString();
					break;
					
					case ConstantesBD.valorCampoMesGeneracion:
						valor = UtilidadFecha.getMesAnioDiaActual("mes") + "";
						valor = valor.length()==1?"0"+valor:valor;
					break;
				}
				valor = this.limpiarCaracteresInvalidos(valor,false);
				if(valor.equals(""))
					mensajeError = "No existe información para "+camposReg.get("nombre_valor1_"+posC)+descCampo;
			break;
			
			//CAMPO FECHA-DÍA *********************************************************
			case ConstantesBD.codigoTipoCampoFechaDia:
				//se toma valor del campo
				tipoValorCampo = Integer.parseInt(camposReg.get("codigo_valor1_"+posC).toString());
				switch (tipoValorCampo)
				{
					case ConstantesBD.valorCampoDiaFactura:
						valor = this.getFacturas("dia_factura_"+posF).toString();
					break;
					
					case ConstantesBD.valorCampoDiaAnulacion:
						valor = this.getFacturas("dia_anulacion_"+posF).toString();
					break;
					
					case ConstantesBD.valorCampoDiaGeneracion:
						valor = UtilidadFecha.getMesAnioDiaActual("dia") + "";
						valor = valor.length()==1?"0"+valor:valor;
					break;
				}
				valor = this.limpiarCaracteresInvalidos(valor,false);
				if(valor.equals(""))
					mensajeError = "No existe información para "+camposReg.get("nombre_valor1_"+posC)+descCampo;
			break;
			
			//CAMPO FECHA-HORA *************************************************************
			case ConstantesBD.codigoTipoCampoFechaHora:
				//se toma valor del campo
				tipoValorCampo = Integer.parseInt(camposReg.get("codigo_valor1_"+posC).toString());
				switch (tipoValorCampo)
				{
					case ConstantesBD.valorCampoHoraFactura:
						valor = this.getFacturas("hora_factura_"+posF).toString();
					break;
					
					case ConstantesBD.valorCampoHoraAnulacion:
						valor = this.getFacturas("hora_anulacion_"+posF).toString();
					break;
					
					case ConstantesBD.valorCampoHoraGeneracion:
						valor = UtilidadFecha.getHoraSegundosActual() + "";
					break;
				}
				if(valor.length()>8)
					valor = valor.substring(0,8);
				valor = this.limpiarCaracteresInvalidos(valor,false);
				if(valor.equals(""))
					mensajeError = "No existe información para "+camposReg.get("nombre_valor1_"+posC)+descCampo;
			break;
			
			//CAMPO FECHA dd/mm/aaaa *********************************************************
			case ConstantesBD.codigoTipoCampoFechaDDMMAAAA:
				//se toma valor del campo
				tipoValorCampo = Integer.parseInt(camposReg.get("codigo_valor1_"+posC).toString());
				switch (tipoValorCampo)
				{
					case ConstantesBD.valorCampoFechaFactura:
						valor = UtilidadFecha.conversionFormatoFechaAAp(this.getFacturas("fecha_factura_"+posF).toString());
					break;
					
					case ConstantesBD.valorCampoFechaAnulacion:
						valor = UtilidadFecha.conversionFormatoFechaAAp(this.getFacturas("fecha_anulacion_"+posF).toString());
					break;
					
					case ConstantesBD.valorCampoFechaGeneracion:
						valor = UtilidadFecha.getFechaActual();
					break;
				}
				valor = this.limpiarCaracteresInvalidos(valor,false);
				if(valor.equals(""))
					mensajeError = "No existe información para "+camposReg.get("nombre_valor1_"+posC)+descCampo;
			break;
			
			//CAMPO FECHA aaaa/mm/dd *********************************************************
			case ConstantesBD.codigoTipoCampoFehcaAAAAMMDD:
				//se toma valor del campo
				tipoValorCampo = Integer.parseInt(camposReg.get("codigo_valor1_"+posC).toString());
				switch (tipoValorCampo)
				{
					case ConstantesBD.valorCampoFechaFactura:
						valor = this.getFacturas("fecha_factura_"+posF).toString();
					break;
					
					case ConstantesBD.valorCampoFechaAnulacion:
						valor = this.getFacturas("fecha_anulacion_"+posF).toString();
					break;
					
					case ConstantesBD.valorCampoFechaGeneracion:
						valor = UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual());
					break;
				}
				
				valor = this.convertirAFechaAAAMMDD(valor);
				valor = this.limpiarCaracteresInvalidos(valor,false);
				
				if(valor.equals(""))
					mensajeError = "No existe información para "+camposReg.get("nombre_valor1_"+posC)+descCampo;
			break;
			
			//CAMPO FECHA aaaa/mm/dd-hh:mm:ss ***********************************************************
			case ConstantesBD.codigoTipoCampoFehcaHoraAAAAMMDDMMSS:
				//se toma valor del campo
				tipoValorCampo = Integer.parseInt(camposReg.get("codigo_valor1_"+posC).toString());
				hora = "";
				
				switch (tipoValorCampo)
				{
					case ConstantesBD.valorCampoFechaHoraFactura:
						hora = this.getFacturas("hora_factura_"+posF).toString();
						hora = hora.length()>8?hora.substring(0,9):hora;
						valor = this.convertirAFechaAAAMMDD(this.getFacturas("fecha_factura_"+posF).toString());
					break;
					
					case ConstantesBD.valorCampoFechaHoraAnulacion:
						hora = this.getFacturas("hora_anulacion_"+posF).toString();
						hora = hora.length()>8?hora.substring(0,9):hora;
						valor = this.convertirAFechaAAAMMDD(this.getFacturas("fecha_anulacion_"+posF).toString());
					break;
					
					case ConstantesBD.valorCampoFechaHoraGeneracion:
						hora = UtilidadFecha.getHoraSegundosActual();
						valor = this.convertirAFechaAAAMMDD(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
					break;
				}
				
				if(valor.equals("")||hora.equals(""))
					valor = "";
				else
					valor = valor + "-" + hora;
				
				valor = this.limpiarCaracteresInvalidos(valor,false);
				if(valor.equals(""))
					mensajeError = "No existe información para "+camposReg.get("nombre_valor1_"+posC)+descCampo;
			break;
			
			//CAMPO FECHA dd/mm/aaaa-hh:mm:ss *****************************************************************
			case ConstantesBD.codigoTipoCampoFechaHoraDDMMAAAAMMSS:
				//se toma valor del campo
				tipoValorCampo = Integer.parseInt(camposReg.get("codigo_valor1_"+posC).toString());
				hora = "";
				
				switch (tipoValorCampo)
				{
					case ConstantesBD.valorCampoFechaHoraFactura:
						hora = this.getFacturas("hora_factura_"+posF).toString();
						hora = hora.length()>8?hora.substring(0,9):hora;
						valor = UtilidadFecha.conversionFormatoFechaAAp(this.getFacturas("fecha_factura_"+posF).toString());
					break;
					
					case ConstantesBD.valorCampoFechaHoraAnulacion:
						hora = this.getFacturas("hora_anulacion_"+posF).toString();
						hora = hora.length()>8?hora.substring(0,9):hora;
						valor = UtilidadFecha.conversionFormatoFechaAAp(this.getFacturas("fecha_anulacion_"+posF).toString());
					break;
					
					case ConstantesBD.valorCampoFechaHoraGeneracion:
						hora = UtilidadFecha.getHoraSegundosActual();
						valor = UtilidadFecha.getFechaActual();
					break;
				}
				
				if(valor.equals("")||hora.equals(""))
					valor = "";
				else
					valor = valor + "-" + hora;
				
				valor = this.limpiarCaracteresInvalidos(valor,false);
				if(valor.equals(""))
					mensajeError = "No existe información para "+camposReg.get("nombre_valor1_"+posC)+descCampo;
			break;
			
			//CAMPO VALORES ************************************************************************************
			case ConstantesBD.codigoTipoCampoValores:
				//se toma valor del campo
				tipoValorCampo = Integer.parseInt(camposReg.get("codigo_valor1_"+posC).toString());
				hora = "";
				this.numCamposValor++; //se contabiliza el campo tipo valor del registro
				
				switch (tipoValorCampo)
				{
					case ConstantesBD.valorCampoValorConvenio:
						valor = this.getFacturas("valor_convenio_"+posF).toString();
						valorD = Double.parseDouble(valor);
						if(valorD==0)
							this.numCamposValorCero++;
						valor = aplicarSeparadorDecimales(valor);
						
					break;
					
					case ConstantesBD.valorCampoValorPaciente:
						valor = this.getFacturas("valor_paciente_"+posF).toString();
						valorD = Double.parseDouble(valor);
						
						//se verifica si hay valor paciente
						if(valorD>0)
							valor = aplicarSeparadorDecimales(valor);
						//se verifica si hay devolucion de paciente y se puede presentar dentro de este campo
						else if(valorD<0&&this.presentaDevolucionPaciente)
						{
							//se verifica si se puede mostrar la devolucion con valor negativo
							if(this.valorNegativoDevolPaciente)
								valor = aplicarSeparadorDecimales(valor);
							else
								valor = aplicarSeparadorDecimales((valorD*-1)+"");
						}
						else
						{
							this.numCamposValorCero++;
							valor = aplicarSeparadorDecimales("0");
						}
					break;
					
					case ConstantesBD.valorCampoValorDevolucionPaciente:
						valor = this.getFacturas("valor_paciente_"+posF).toString();
						valorD = Double.parseDouble(valor);
						if(valorD==0)
							this.numCamposValorCero++;
						
						//se verifica si hay devolucion de paciente y se puede presentar dentro de este campo
						if(valorD<0)
						{
							//se verifica si se puede mostrar la devolucion con valor negativo
							if(this.valorNegativoDevolPaciente)
								valor = aplicarSeparadorDecimales(valor);
							else
								valor = aplicarSeparadorDecimales((valorD*-1)+"");
						}
						else
							valor = aplicarSeparadorDecimales("0");
					break;
					
					case ConstantesBD.valorCampoValorDescuentoPaciente:
						valor = this.getFacturas("val_desc_pac_"+posF).toString();
						valorD = Double.parseDouble(valor);
						if(valorD==0)
							this.numCamposValorCero++;
						valor = aplicarSeparadorDecimales(valor);
					break;
					
					case ConstantesBD.valorCampoValorAbonosPaciente:
						valor = this.getFacturas("valor_abonos_"+posF).toString();
						valorD = Double.parseDouble(valor);
						if(valorD==0)
							this.numCamposValorCero++;
						valor = aplicarSeparadorDecimales(valor);
					break;
					
					case ConstantesBD.valorCampoValorServicios:
					case ConstantesBD.valorCampoValorInventarios:
						valor = this.getDetalleFactura("valor_total_"+posD).toString();
						valorD = Double.parseDouble(valor);
						if(valorD==0)
							this.numCamposValorCero++;
						valor = aplicarSeparadorDecimales(valor);
					break;
					
					case ConstantesBD.valorCampoValorPool:
						int grupoAsocio = Integer.parseInt(this.getDetalleFactura("grupo_asocio_"+posD).toString());
						if(grupoAsocio==ConstantesBD.codigoGrupoAsocioHonorarios||grupoAsocio==0)
						{
							valor = this.getDetalleFactura("valor_pool_"+posD).toString();
							valorD = Double.parseDouble(valor);
							if(valorD==0)
								this.numCamposValorCero++;
							valor = aplicarSeparadorDecimales(valor);
						}
						else
							this.generarRegistro = false;
					break;
					
					case ConstantesBD.valorCampoValorIngresoPool:
						grupoAsocio = Integer.parseInt(this.getDetalleFactura("grupo_asocio_"+posD).toString());
						if(grupoAsocio==ConstantesBD.codigoGrupoAsocioHonorarios||grupoAsocio==0)
						{
							valor = this.getDetalleFactura("valor_ingreso_pool_"+posD).toString();
							valorD = Double.parseDouble(valor);
							if(valorD==0)
								this.numCamposValorCero++;
							valor = aplicarSeparadorDecimales(valor);
						}
						else
							this.generarRegistro = false;
					break;
				}
				if(valor.equals(""))
					mensajeError = "No existe información para "+camposReg.get("nombre_valor1_"+posC)+descCampo;
				
					
			break;
			
			//CAMPO CUENTAS CONTABLES PARAMETRIZABLES *******************************************************
			case ConstantesBD.codigoTipoCampoCuentasContables:
				paramCuentas.put("institucion",this.institucion);
				//se toma valor del campo
				tipoValorCampo = Integer.parseInt(camposReg.get("codigo_valor2_"+posC).toString());
				
				switch (tipoValorCampo)
				{
					case ConstantesBD.valorCampoCuentaConvenio:
						paramCuentas.put("codigoConvenio",this.getFacturas("codigo_convenio_"+posF));
						paramCuentas.put("tipoCuenta",ConstantesBD.tipoCuentaConvenio+"");
						
						valor = generacionInterfazDao.consultarCuentaParametrizable(con,paramCuentas);
						valor = this.limpiarCaracteresInvalidos(valor,false);
						if(valor.equals(""))
							mensajeError = "No existe información para "+camposReg.get("nombre_valor2_"+posC)+descCampo;
						
					break;
					
					case ConstantesBD.valorCampoCuentaPaciente:
						paramCuentas.put("codigoConvenio",this.getFacturas("codigo_convenio_"+posF));
						paramCuentas.put("tipoCuenta",ConstantesBD.tipoCuentaPaciente+"");
						
						valor = generacionInterfazDao.consultarCuentaParametrizable(con,paramCuentas);
						valor = this.limpiarCaracteresInvalidos(valor,false);
						if(valor.equals(""))
							mensajeError = "No existe información para "+camposReg.get("nombre_valor2_"+posC)+descCampo;
					break;
					
					case ConstantesBD.valorCampoCuentaDevolucionPaciente:
						paramCuentas.put("codigoConvenio",this.getFacturas("codigo_convenio_"+posF));
						paramCuentas.put("tipoCuenta",ConstantesBD.tipoCuentaDevolucionPaciente+"");
						
						valor = generacionInterfazDao.consultarCuentaParametrizable(con,paramCuentas);
						valor = this.limpiarCaracteresInvalidos(valor,false);
						if(valor.equals(""))
							mensajeError = "No existe información para "+camposReg.get("nombre_valor2_"+posC)+descCampo;
					break;
					
					case ConstantesBD.valorCampoCuentaDescuentoPaciente:
						paramCuentas.put("codigoConvenio",this.getFacturas("codigo_convenio_"+posF));
						paramCuentas.put("tipoCuenta",ConstantesBD.tipoCuentaDescuentosPaciente+"");
						
						valor = generacionInterfazDao.consultarCuentaParametrizable(con,paramCuentas);
						valor = this.limpiarCaracteresInvalidos(valor,false);
						if(valor.equals(""))
							mensajeError = "No existe información para "+camposReg.get("nombre_valor2_"+posC)+descCampo;
					break;
					
					case ConstantesBD.valorCampoCuentaAbonosPaciente:
						paramCuentas.put("codigoConvenio",this.getFacturas("codigo_convenio_"+posF));
						paramCuentas.put("tipoCuenta",ConstantesBD.tipoCuentaAbonosPaciente+"");
						
						valor = generacionInterfazDao.consultarCuentaParametrizable(con,paramCuentas);
						valor = this.limpiarCaracteresInvalidos(valor,false);
						if(valor.equals(""))
							mensajeError = "No existe información "+camposReg.get("nombre_valor2_"+posC)+descCampo;
					break;
					
					case ConstantesBD.valorCampoCuentaServicios:
						paramCuentas.put("codigoServicio",this.getDetalleFactura("codigo_servicio_"+posD));
						paramCuentas.put("centroCosto",this.getDetalleFactura("centro_costo_solicitado_"+posD));
						paramCuentas.put("tipoCuenta",ConstantesBD.tipoCuentaServicios+"");
						
						valor = generacionInterfazDao.consultarCuentaParametrizable(con,paramCuentas);
						valor = this.limpiarCaracteresInvalidos(valor,false);
						if(valor.equals(""))
							mensajeError = "No existe información de "+camposReg.get("nombre_valor2_"+posC)+" para el servicio "+
								this.getDetalleFactura("codigo_especialidad_"+posD)+"-"+this.getDetalleFactura("codigo_servicio_"+posD)+
								descCampo;
					break;
					
					case ConstantesBD.valorCampoCuentaInventarios:
						paramCuentas.put("codigoArticulo",this.getDetalleFactura("codigo_articulo_"+posD));
						paramCuentas.put("centroCosto",this.getDetalleFactura("centro_costo_solicitante_"+posD));
						paramCuentas.put("tipoCuenta",ConstantesBD.tipoCuentaInventarios+"");
						
						valor = generacionInterfazDao.consultarCuentaParametrizable(con,paramCuentas);
						valor = this.limpiarCaracteresInvalidos(valor,false);
						if(valor.equals(""))
							mensajeError = "No existe información de "+camposReg.get("nombre_valor2_"+posC)+
							" para el articulo "+this.getDetalleFactura("codigo_articulo_"+posD)+descCampo;
					break;
					
					case ConstantesBD.valorCampoCuentaPool:
					case ConstantesBD.valorCampoCuentaIngresoPool:
						int grupoAsocio = Integer.parseInt(this.getDetalleFactura("grupo_asocio_"+posD).toString());
						if(grupoAsocio==ConstantesBD.codigoGrupoAsocioHonorarios||grupoAsocio==0)
						{
							paramCuentas.put("codigoPool",this.getDetalleFactura("codigo_pool_"+posD));
							paramCuentas.put("esquemaTarifario",this.getDetalleFactura("esquema_tar_"+posD));
							if(tipoValorCampo==ConstantesBD.valorCampoCuentaPool)
								paramCuentas.put("tipoCuenta",ConstantesBD.tipoCuentaPool+"");
							else
								paramCuentas.put("tipoCuenta",ConstantesBD.tipoCuentaIngresoPool+"");
							
							valor = generacionInterfazDao.consultarCuentaParametrizable(con,paramCuentas);
							valor = this.limpiarCaracteresInvalidos(valor,false);
							if(valor.equals(""))
								mensajeError = "No existe información de "+camposReg.get("nombre_valor2_"+posC)+" para el servicio "+
									this.getDetalleFactura("codigo_especialidad_"+posD)+"-"+this.getDetalleFactura("codigo_servicio_"+posD)+descCampo;
						}
						else
							this.generarRegistro=false;
					break;
					
					
				}
			break;
			
			//CAMPO TIPO DE MOVIMIENTO *******************************************************
			case ConstantesBD.codigoTipoCampoTipoMovimiento:
				//se toma valor del campo
				tipoValorCampo = Integer.parseInt(camposReg.get("codigo_valor2_"+posC).toString());
				
				switch (tipoValorCampo)
				{
					case ConstantesBD.valorCampoDebito:
						valor = this.descripcionDebito;
					break;
					case ConstantesBD.valorCampoCredito:
						valor = this.descripcionCredito;
					break;
				}
				valor = this.limpiarCaracteresInvalidos(valor,false);
				if(valor.equals(""))
					mensajeError = "No existe información para "+camposReg.get("nombre_valor2_"+posC)+descCampo;
			break;
			
			//CAMPO NÚMERO IDENTIFICACIÓN DEL SISTEMA ********************************************
			case ConstantesBD.codigoTipoCampoNumeroIdSistema:
				//se toma valor del campo
				tipoValorCampo = Integer.parseInt(camposReg.get("codigo_valor1_"+posC).toString());
				
				switch (tipoValorCampo)
				{
					case ConstantesBD.valorCampoTerceroConvenioRsponsable:
						valor = this.getFacturas("num_id_tercero_"+posF).toString();
						valor = this.limpiarCaracteresInvalidos(valor,false);
					break;
					case ConstantesBD.valorCampoTerceroPaciente:
						valor = this.getFacturas("num_id_paciente_"+posF).toString();
						valor = this.limpiarCaracteresInvalidos(valor,false);
					break;
					case ConstantesBD.valorCampoTerceroPool:
						int grupoAsocio = Integer.parseInt(this.getDetalleFactura("grupo_asocio_"+posD).toString());
						if(grupoAsocio==ConstantesBD.codigoGrupoAsocioHonorarios||grupoAsocio==0)
						{
							valor = this.getDetalleFactura("num_tercero_pool_"+posD).toString();
							valor = this.limpiarCaracteresInvalidos(valor,false);
						}
						else
							this.generarRegistro = false;
					break;
					case ConstantesBD.valorCampoTerceroMedico:
						grupoAsocio = Integer.parseInt(this.getDetalleFactura("grupo_asocio_"+posD).toString());
						if(grupoAsocio==ConstantesBD.codigoGrupoAsocioHonorarios||grupoAsocio==0)
						{
							valor = this.getDetalleFactura("num_id_medico_"+posD).toString();
							valor = this.limpiarCaracteresInvalidos(valor,false);
						}
						else
							this.generarRegistro = false;
					break;
				}
				if(valor.equals(""))
					mensajeError = "No existe información para "+camposReg.get("nombre_valor1_"+posC)+descCampo;
			break;
			
			//CAMPO CENTRO DE COSTO DEL SISTEMA ********************************************
			case ConstantesBD.codigoTipoCampoCentroCostoSistema:
				//se toma valor del campo
				tipoValorCampo = Integer.parseInt(camposReg.get("codigo_valor2_"+posC).toString());
				
				switch (tipoValorCampo)
				{
					case ConstantesBD.valorCampoCentroCostoSolicitado:
						valor = this.getDetalleFactura("centro_costo_solicitado_"+posD).toString();
						valor = this.limpiarCaracteresInvalidos(valor,false);
						if(valor.equals(""))
							mensajeError = "No existe información para "+camposReg.get("nombre_valor2_"+posC)+descCampo;
					break;
					case ConstantesBD.valorCampoCentroCostoSolicita:
						valor = this.getDetalleFactura("centro_costo_solicitante_"+posD).toString();
						valor = this.limpiarCaracteresInvalidos(valor,false);
						if(valor.equals(""))
							mensajeError = "No existe información para "+camposReg.get("nombre_valor2_"+posC)+descCampo;
					break;
					case ConstantesBD.valorCampoNulo:
						valor = "";
					break;
					case ConstantesBD.valorCampoValorFijo:
						valor = camposReg.get("descripcion_"+posC).toString();
						valor = this.limpiarCaracteresInvalidos(valor,false);
						if(valor.equals(""))
							mensajeError = "No existe información para "+camposReg.get("nombre_valor2_"+posC)+descCampo;
					break;
				}
				
			break;
			
			//CAMPO CAMPO DETALLE DEL SISTEMA ********************************************
			case ConstantesBD.codigoTipoCampoCampoDetalleSistema:
				//se toma valor del campo
				tipoValorCampo = Integer.parseInt(camposReg.get("codigo_valor1_"+posC).toString());
				
				switch (tipoValorCampo)
				{
					case ConstantesBD.valorCampoApellidosNombresPaciente:
						valor = this.getFacturas("nombre_paciente_"+posF).toString();
						valor = this.limpiarCaracteresInvalidos(valor,false);
						if(valor.equals(""))
							mensajeError = "No existe información para "+camposReg.get("nombre_valor1_"+posC)+descCampo;
					break;
					case ConstantesBD.valorCampoNombreConvenioResponsable:
						valor = this.getFacturas("nombre_convenio_"+posF).toString();
						valor = this.limpiarCaracteresInvalidos(valor,false);
						if(valor.equals(""))
							mensajeError = "No existe información para "+camposReg.get("nombre_valor1_"+posC)+descCampo;
					break;
					case ConstantesBD.valorCampoRazonSocialEmpresa:
						valor = this.getFacturas("nombre_empresa_"+posF).toString();
						valor = this.limpiarCaracteresInvalidos(valor,false);
						if(valor.equals(""))
							mensajeError = "No existe información para "+camposReg.get("nombre_valor1_"+posC)+descCampo;
					break;
					case ConstantesBD.valorCampoNombreTerceroEmpresa:
						valor = this.getFacturas("nombre_tercero_"+posF).toString();
						valor = this.limpiarCaracteresInvalidos(valor,false);
						if(valor.equals(""))
							mensajeError = "No existe información para "+camposReg.get("nombre_valor1_"+posC)+descCampo;
					break;
					case ConstantesBD.valorCampoDescripcionServicio:
						valor = this.getDetalleFactura("descripcion_servicio_"+posD).toString();
						valor = this.limpiarCaracteresInvalidos(valor,false);
						if(valor.equals(""))
							mensajeError = "No existe información para "+camposReg.get("nombre_valor1_"+posC)+descCampo;
					break;
					case ConstantesBD.valorCampoDescripcionArticulo:
						valor = this.getDetalleFactura("descripcion_articulo_"+posD).toString();
						valor = this.limpiarCaracteresInvalidos(valor,false);
						if(valor.equals(""))
							mensajeError = "No existe información para "+camposReg.get("nombre_valor1_"+posC)+descCampo;
					break;
					case ConstantesBD.valorCampoDescripcionCentroCostoSolicitado:
						valor = this.getDetalleFactura("nom_centro_costo_solicitado_"+posD).toString();
						valor = this.limpiarCaracteresInvalidos(valor,false);
						if(valor.equals(""))
							mensajeError = "No existe información para "+camposReg.get("nombre_valor1_"+posC)+descCampo;
					break;
					case ConstantesBD.valorCampoDescripcionCentroCostoSolicita:
						valor = this.getDetalleFactura("nom_centro_costo_solicita_"+posD).toString();
						valor = this.limpiarCaracteresInvalidos(valor,false);
						if(valor.equals(""))
							mensajeError = "No existe información para "+camposReg.get("nombre_valor1_"+posC)+descCampo;
					break;
					case ConstantesBD.valorCampoNombrePool:
						int grupoAsocio = Integer.parseInt(this.getDetalleFactura("grupo_asocio_"+posD).toString());
						if(grupoAsocio==ConstantesBD.codigoGrupoAsocioHonorarios||grupoAsocio==0)
						{
							valor = this.getDetalleFactura("nombre_pool_"+posD).toString();
							valor = this.limpiarCaracteresInvalidos(valor,false);
							if(valor.equals(""))
								mensajeError = "No existe información para "+camposReg.get("nombre_valor1_"+posC)+descCampo;
						}
						else
							this.generarRegistro = false;
					break;
					case ConstantesBD.valorCampoNombreMedico:
						grupoAsocio = Integer.parseInt(this.getDetalleFactura("grupo_asocio_"+posD).toString());
						if(grupoAsocio==ConstantesBD.codigoGrupoAsocioHonorarios||grupoAsocio==0)
						{
							valor = this.getDetalleFactura("nombre_medico_"+posD).toString();
							valor = this.limpiarCaracteresInvalidos(valor,false);
							if(valor.equals(""))
								mensajeError = "No existe información para "+camposReg.get("nombre_valor1_"+posC)+descCampo;
						}
						else
							this.generarRegistro = false;
					break;
					case ConstantesBD.valorCampoNombreUsuarioFactura:
						valor = this.getFacturas("usuario_factura_"+posF).toString();
						valor = this.limpiarCaracteresInvalidos(valor,false);
						if(valor.equals(""))
							mensajeError = "No existe información para "+camposReg.get("nombre_valor1_"+posC)+descCampo;
					break;
					case ConstantesBD.valorCampoNombreUsuarioAnula:
						valor = this.getFacturas("usuario_anula_"+posF).toString();
						valor = this.limpiarCaracteresInvalidos(valor,false);
						if(valor.equals(""))
							mensajeError = "No existe información para "+camposReg.get("nombre_valor1_"+posC)+descCampo;
					break;
					case ConstantesBD.valorCampoNulo:
						valor = "";
					break;
					case ConstantesBD.valorCampoDescripcionFija:
						valor = camposReg.get("descripcion_"+posC).toString();
						valor = this.limpiarCaracteresInvalidos(valor,false);
						if(valor.equals(""))
							mensajeError = "No existe información para "+camposReg.get("nombre_valor1_"+posC)+descCampo;
					break;
				}
			break;
			
			//CAMPO USUARIO ********************************************
			case ConstantesBD.codigoTipoCampoUsuario:
				//se toma valor del campo
				tipoValorCampo = Integer.parseInt(camposReg.get("codigo_valor1_"+posC).toString());
				
				switch (tipoValorCampo)
				{
					case ConstantesBD.valorCampoUsuarioFactura:
						valor = this.getFacturas("login_factura_"+posF).toString();
						valor = this.limpiarCaracteresInvalidos(valor,false);
					break;
					case ConstantesBD.valorCampoUsuarioAnula:
						valor = this.getFacturas("login_anula_"+posF).toString();
						valor = this.limpiarCaracteresInvalidos(valor,false);
					break;
				}
				if(valor.equals(""))
					mensajeError = "No existe información para "+camposReg.get("nombre_valor1_"+posC)+descCampo;
			break;
			
		}
		
		//**SE VERIFICA SI SE DEBE GENERAR REGISTRO*********************
		if(!this.generarRegistro)
			return valor; //ya no se continuan haciendo validaciones porque no se generará registro
		
		
		//**SE VERIFICA SI VALOR OBTENIDO ES REQUERIDO****************+
		
		if(!mensajeError.equals("")&&UtilidadTexto.getBoolean(camposReg.get("es_requerido_"+posC).toString()))
		{
			//Se toma el INDICATIVO SI NO EXISTE EL DATO
			int indExiste = Integer.parseInt(camposReg.get("indicativo_existe_"+posC).toString());
			
			switch(indExiste)
			{
				case ConstantesBD.tipoIndicativoExisteBlanco:
					valor = "";
				break;
				case ConstantesBD.tipoIndicativoExisteGenerarInconsistencia:
					presentaInconsistencia = true;
				break;
				case ConstantesBD.tipoIndicativoExisteValorDefault:
					valor = camposReg.get("valor_default_"+posC).toString();
					if(valor.equals(""))
					{
						presentaInconsistencia = true;
						mensajeError = "No existe información para el valor por defecto de "+
							camposReg.get("nombre_valor1_"+posC)+descCampo;
					}
				break;
			}
		}
		
		
		if(!presentaInconsistencia)
		{
			//***SE VERIFICA TAMAÑO DEL CAMPO******************************************
			//se toma el tipo de tamanio
			int campoTamanio = Integer.parseInt(camposReg.get("tamanio_campo_"+posC).toString());
			//se toma el valor del tamanio permitido
			int valorTamanio = 0;
			try
			{
				valorTamanio = Integer.parseInt(camposReg.get("valor_tamanio_"+posC).toString());
			}
			catch(Exception e)
			{
				//logger.info("Exception al tomar valor tamanio: "+e);
				valorTamanio = valor.length();
			}
			
			
			if(campoTamanio==ConstantesBD.codTamanioFijo)
			{
				if(valor.length()>valorTamanio)
					valor = valor.substring(0,valorTamanio);
				else
					valor = this.editarEspacios(valor,valor.length(),valorTamanio,false);
			}
			else if(campoTamanio==ConstantesBD.codTamanioMenorQue)
			{
				if(valor.length()>valorTamanio)
					valor = valor.substring(0,valorTamanio);
			}
			//**************************************************************************
		}
		else
		{
			//*****SE EDITA INCONSISTENCIA******************************************
			this.huboInconsistencias = true;
			int pos = this.numInconsistencias;
			if(this.getFacturas("consecutivo_anulacion_"+posF).toString().equals(""))
			{
				//Es anulacion
				this.datosInconsistencias.put("numeroDocumento_"+pos,this.getFacturas("consecutivo_factura_"+posF));
				this.datosInconsistencias.put("tipoDocumento_"+pos,"Facturación");
			}
			else
			{
				//Es factura
				this.datosInconsistencias.put("numeroDocumento_"+pos,this.getFacturas("consecutivo_anulacion_"+posF));
				this.datosInconsistencias.put("tipoDocumento_"+pos,"Anulación");
			}
			
			this.datosInconsistencias.put("detalleInconsistencia_"+pos,mensajeError);
			if(tipoRegistro==ConstantesBD.tipoRegistroInterfazServiciosFactura||tipoRegistro==ConstantesBD.tipoRegistroInterfazArticulosFactura)
				this.datosInconsistencias.put("numeroSolicitud_"+pos,this.getDetalleFactura("orden_"+posD));
			else
				this.datosInconsistencias.put("numeroSolicitud_"+pos,"");
			pos++;
			this.numInconsistencias = pos;
			this.datosInconsistencias.put("numRegistros",this.numInconsistencias);
				
			//**********************************************************************
		}
		
		return valor;
	}
	
	/**
	 * Método implementado para convertir una fecha
	 * AAA-MM-DD a AAAA/MM/DD
	 * @param valor
	 * @return
	 */
	private String convertirAFechaAAAMMDD(String valor) 
	{
		if(!valor.equals(""))
		{
			String vector[] = valor.split("-");
			valor = vector[0] + "/" + vector[1] + "/" + vector[2];
		}
		return valor;
	}
	/**
	 * Método implementado para limpiar caracteres inválidos 
	 * @param valor
	 * @param esNumerico 
	 * @return
	 */
	private String limpiarCaracteresInvalidos(String valor, boolean esNumerico) 
	{
		
		if(!esNumerico)
		{
			while(valor.indexOf(".")>=0)
			{
				if(valor.indexOf(".")==0)
					valor=valor.substring(valor.indexOf(".")+1, valor.length());
				else
					valor=valor.substring(0, valor.indexOf("."))+valor.substring(valor.indexOf(".")+1, valor.length());
			}
			while(valor.indexOf(",")>=0)
			{
				if(valor.indexOf(",")==0)
					valor=valor.substring(valor.indexOf(",")+1, valor.length());
				else
					valor=valor.substring(0, valor.indexOf(","))+valor.substring(valor.indexOf(",")+1, valor.length());
			}
		}
		
		while(valor.indexOf(this.separadorCampos)>=0)
		{
			if(valor.indexOf(this.separadorCampos)==0)
				valor=valor.substring(valor.indexOf(this.separadorCampos)+this.separadorCampos.length(), valor.length());
			else
				valor=valor.substring(0, valor.indexOf(this.separadorCampos))+valor.substring(valor.indexOf(this.separadorCampos)+this.separadorCampos.length(), valor.length());
		}
		if(!this.identificadorFinArchivo.equals(""))
		{
			while(valor.indexOf(this.identificadorFinArchivo)>=0)
			{
				if(valor.indexOf(this.identificadorFinArchivo)==0)
					valor=valor.substring(valor.indexOf(this.identificadorFinArchivo)+this.identificadorFinArchivo.length(), valor.length());
				else
					valor=valor.substring(0, valor.indexOf(this.identificadorFinArchivo))+valor.substring(valor.indexOf(this.identificadorFinArchivo)+this.identificadorFinArchivo.length(), valor.length());
			}
		}
		
		return valor;
	}
	/**
	 * Método implementado para aplicar separación de decimales
	 * a un valor
	 * @param valor
	 * @return
	 */
	private String aplicarSeparadorDecimales(String valor) 
	{
		valor = this.limpiarCaracteresInvalidos(valor,true);
		if(this.separadorDecimales==ConstantesBD.tipoSeparadorDecimalPunto)
			valor = UtilidadTexto.formatearValores(valor,2,false,false);
		else
			valor = UtilidadTexto.formatearValores(valor,2,false,true);
		
		return valor;
	}
	
	/**
	 * Método para editar los espacios del campo en el archivo RIPS
	 * @param campo
	 * @param tamano
	 * @param limite
	 * @param esNumerico: si es numérico los espacios se añaden antes, si es cadena los espacios se añaden después
	 * @return
	 */
	private String editarEspacios(String campo, int tamano, int limite,boolean esNumerico)
	{
		
		String aux;
		String espacios="";
		
		for(int i=tamano+1;i<=limite;i++)
			espacios+=" ";
		
		if(esNumerico)
			aux=espacios+campo;
		else
			aux=campo+espacios;
		
		return aux;
	}
	
	
	/**
	 * Método implementado para cargar el archivo de salida
	 * @return
	 */
	public boolean cargarArchivoSalida()
	{
	
		logger.info("*******CARGAR ARCHIVO DE SALIDA**********************************");
		try
		{
			this.numMapArchivo = 0;
			this.mapArchivo = new HashMap();
			String cadena="";
			
			//*****SE CARGAN LAS COLUMNAS DEL ARCHIVO**************
			HashMap camposReg = (HashMap)this.getRegistros("campos_"+0);
			int numCamposReg = Integer.parseInt(camposReg.get("numRegistros").toString());
			this.setMapArchivo("numCampos",numCamposReg);
			
			for(int i=0;i<numCamposReg;i++)
				this.setMapArchivo("titulo_"+i,camposReg.get("nombre_campo_"+i));
			
			//******SE INICIALIZA ARCHIVO*************************
			File archivo=new File(this.pathArchivo,this.nombreArchivo);
			FileReader stream=new FileReader(archivo); //se coloca false para el caso de que esté repetido
			BufferedReader buffer=new BufferedReader(stream);
			
			//********SE RECORRE LÍNEA POR LÍNEA**************
			cadena=buffer.readLine();
			while(cadena!=null)
			{
				
				//se divide el registro por cada campo
				String[] campos = cadena.split(this.separadorCampos);
				
				
				
				for(int i=0;i<campos.length;i++)
				{
					//se almacena el campo en el mapa
					this.setMapArchivo(i+"_"+this.numMapArchivo,campos[i]);
				}
				
				//se tomará como nuevo registro si no empieza con el caracteres de identificador de fin de archivo
				if(!this.identificadorFinArchivo.equals("")&&campos.length>0&&!campos[0].equals(this.identificadorFinArchivo))
					this.numMapArchivo++;
				
				cadena=buffer.readLine();
			}
			this.setMapArchivo("numMapArchivo",this.numMapArchivo);
			
			//***************CERRAR ARCHIVO****************************
			buffer.close();
			
			return true;
		}
		catch(FileNotFoundException e)
		{
			logger.error("No se pudo encontrar el archivo "+this.nombreArchivo+" al cargarlo: "+e);
			this.errores.add("No se pudo encontrar el archivo",new ActionMessage("error.interfaz.generacionInterfaz.falloLectura",this.nombreArchivo));
			return false;
		}
		catch(IOException e)
		{
			logger.error("Error en los streams del archivo "+this.nombreArchivo+" al cargarlo: "+e);
			this.errores.add("Error en los streams del archivo",new ActionMessage("error.interfaz.generacionInterfaz.falloLectura","Error accesando al archivo "+this.nombreArchivo));
			return false;
		}
	}
	
	public boolean cargarArchivoInconsistencias()
	{
		try
		{
			this.numMapArchivo = 0;
			this.mapArchivo = new HashMap();
			String cadena="";
			
			
			
			//******SE INICIALIZA ARCHIVO*************************
			File archivo=new File(this.pathInconsistencias,this.nombreInconsistencias);
			FileReader stream=new FileReader(archivo); //se coloca false para el caso de que esté repetido
			BufferedReader buffer=new BufferedReader(stream);
			
			//********SE RECORRE LÍNEA POR LÍNEA**************
			cadena=buffer.readLine();
			while(cadena!=null)
			{
				
				//se almacena el registro en el mapa
				this.setMapArchivo(this.numMapArchivo+"",cadena);
				
				
				this.numMapArchivo++;
				cadena=buffer.readLine();
			}
			this.setMapArchivo("numMapArchivo",this.numMapArchivo);
			
			//***************CERRAR ARCHIVO****************************
			buffer.close();
		
			return true;
		}
		catch(FileNotFoundException e)
		{
			logger.error("No se pudo encontrar el archivo "+this.nombreInconsistencias+" al cargarlo: "+e);
			this.errores.add("No se pudo encontrar archivo",new ActionMessage("error.interfaz.generacionInterfaz.falloLectura","No se pudo encontrar el archivo "+this.nombreInconsistencias));
			return false;
		}
		catch(IOException e)
		{
			logger.error("Error en los streams del archivo "+this.nombreInconsistencias+" al cargarlo: "+e);
			this.errores.add("Error en los streams del archivo",new ActionMessage("error.interfaz.generacionInterfaz.falloLectura","Error accesando al archivo "+this.nombreInconsistencias));
			return false;
		}
	}
	//************SETTERS & GETTERS***************************************
	/**
	 * @return Returns the campos.
	 */
	public HashMap getCampos() {
		return campos;
	}
	/**
	 * @param campos The campos to set.
	 */
	public void setCampos(HashMap campos) {
		this.campos = campos;
	}
	
	/**
	 * @return Retorna un elemento del mapa campos.
	 */
	public Object getCampos(String key) {
		return campos.get(key);
	}
	/**
	 * @param Asigna un elemento al mapa campos.
	 */
	public void setCampos(String key,Object obj) {
		this.campos.put(key,obj);
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
	 * @return Retorna un elemento del mapa facturas.
	 */
	public Object getFacturas(String key) {
		return facturas.get(key);
	}
	/**
	 * @param Asigna un elemento al mapa facturas.
	 */
	public void setFacturas(String key,Object obj) {
		this.facturas.put(key,obj);
	}
	/**
	 * @return Returns the numFacturas.
	 */
	public int getNumFacturas() {
		return numFacturas;
	}
	/**
	 * @param numFacturas The numFacturas to set.
	 */
	public void setNumFacturas(int numFacturas) {
		this.numFacturas = numFacturas;
	}
	/**
	 * @return Returns the numRegistros.
	 */
	public int getNumRegistros() {
		return numRegistros;
	}
	/**
	 * @param numRegistros The numRegistros to set.
	 */
	public void setNumRegistros(int numRegistros) {
		this.numRegistros = numRegistros;
	}
	/**
	 * @return Returns the registros.
	 */
	public HashMap getRegistros() {
		return registros;
	}
	/**
	 * @param registros The registros to set.
	 */
	public void setRegistros(HashMap registros) {
		this.registros = registros;
	}
	/**
	 * @return Retorna un elemento del mapa registros.
	 */
	public Object getRegistros(String key) {
		return registros.get(key);
	}
	/**
	 * @param Asigna un elemento al mapa registros.
	 */
	public void setRegistros(String key,Object obj) {
		this.registros.put(key,obj);
	}
	/**
	 * @return Returns the nombreArchivo.
	 */
	public String getNombreArchivo() {
		return nombreArchivo;
	}
	/**
	 * @param nombreArchivo The nombreArchivo to set.
	 */
	public void setNombreArchivo(String nombreArchivo) {
		this.nombreArchivo = nombreArchivo;
	}
	/**
	 * @return Returns the nombreInconsistencias.
	 */
	public String getNombreInconsistencias() {
		return nombreInconsistencias;
	}
	/**
	 * @param nombreInconsistencias The nombreInconsistencias to set.
	 */
	public void setNombreInconsistencias(String nombreInconsistencias) {
		this.nombreInconsistencias = nombreInconsistencias;
	}
	/**
	 * @return Returns the pathArchivo.
	 */
	public String getPathArchivo() {
		return pathArchivo;
	}
	/**
	 * @param pathArchivo The pathArchivo to set.
	 */
	public void setPathArchivo(String pathArchivo) {
		this.pathArchivo = pathArchivo;
	}
	/**
	 * @return Returns the pathInconsistencias.
	 */
	public String getPathInconsistencias() {
		return pathInconsistencias;
	}
	/**
	 * @param pathInconsistencias The pathInconsistencias to set.
	 */
	public void setPathInconsistencias(String pathInconsistencias) {
		this.pathInconsistencias = pathInconsistencias;
	}
	/**
	 * @return Returns the huboInconsistencias.
	 */
	public boolean isHuboInconsistencias() {
		return huboInconsistencias;
	}
	/**
	 * @param huboInconsistencias The huboInconsistencias to set.
	 */
	public void setHuboInconsistencias(boolean huboInconsistencias) {
		this.huboInconsistencias = huboInconsistencias;
	}
	/**
	 * @return Returns the descripcionCredito.
	 */
	public String getDescripcionCredito() {
		return descripcionCredito;
	}
	/**
	 * @param descripcionCredito The descripcionCredito to set.
	 */
	public void setDescripcionCredito(String descripcionCredito) {
		this.descripcionCredito = descripcionCredito;
	}
	/**
	 * @return Returns the descripcionDebito.
	 */
	public String getDescripcionDebito() {
		return descripcionDebito;
	}
	/**
	 * @param descripcionDebito The descripcionDebito to set.
	 */
	public void setDescripcionDebito(String descripcionDebito) {
		this.descripcionDebito = descripcionDebito;
	}
	/**
	 * @return Returns the identificadorFinArchivo.
	 */
	public String getIdentificadorFinArchivo() {
		return identificadorFinArchivo;
	}
	/**
	 * @param identificadorFinArchivo The identificadorFinArchivo to set.
	 */
	public void setIdentificadorFinArchivo(String identificadorFinArchivo) {
		this.identificadorFinArchivo = identificadorFinArchivo;
	}
	/**
	 * @return Returns the presentaDevolucionPaciente.
	 */
	public boolean isPresentaDevolucionPaciente() {
		return presentaDevolucionPaciente;
	}
	/**
	 * @param presentaDevolucionPaciente The presentaDevolucionPaciente to set.
	 */
	public void setPresentaDevolucionPaciente(boolean presentaDevolucionPaciente) {
		this.presentaDevolucionPaciente = presentaDevolucionPaciente;
	}
	/**
	 * @return Returns the separadorCampos.
	 */
	public String getSeparadorCampos() {
		return separadorCampos;
	}
	/**
	 * @param separadorCampos The separadorCampos to set.
	 */
	public void setSeparadorCampos(String separadorCampos) {
		this.separadorCampos = separadorCampos;
	}
	/**
	 * @return Returns the separadorDecimales.
	 */
	public int getSeparadorDecimales() {
		return separadorDecimales;
	}
	/**
	 * @param separadorDecimales The separadorDecimales to set.
	 */
	public void setSeparadorDecimales(int separadorDecimales) {
		this.separadorDecimales = separadorDecimales;
	}
	/**
	 * @return Returns the valorNegativoDevolPaciente.
	 */
	public boolean isValorNegativoDevolPaciente() {
		return valorNegativoDevolPaciente;
	}
	/**
	 * @param valorNegativoDevolPaciente The valorNegativoDevolPaciente to set.
	 */
	public void setValorNegativoDevolPaciente(boolean valorNegativoDevolPaciente) {
		this.valorNegativoDevolPaciente = valorNegativoDevolPaciente;
	}
	/**
	 * @return Returns the detalleFactura.
	 */
	public HashMap getDetalleFactura() {
		return detalleFactura;
	}
	/**
	 * @param detalleFactura The detalleFactura to set.
	 */
	public void setDetalleFactura(HashMap detalleFactura) {
		this.detalleFactura = detalleFactura;
	}
	/**
	 * @return Retorna un elemento del mapa detalleFactura.
	 */
	public Object getDetalleFactura(String key) {
		return detalleFactura.get(key);
	}
	/**
	 * @param Asignar elemento al mapa detalleFactura.
	 */
	public void setDetalleFactura(String key,Object obj) {
		this.detalleFactura.put(key,obj);
	}
	/**
	 * @return Returns the numDetalleFactura.
	 */
	public int getNumDetalleFactura() {
		return numDetalleFactura;
	}
	/**
	 * @param numDetalleFactura The numDetalleFactura to set.
	 */
	public void setNumDetalleFactura(int numDetalleFactura) {
		this.numDetalleFactura = numDetalleFactura;
	}
	/**
	 * @return Returns the institucion.
	 */
	public String getInstitucion() {
		return institucion;
	}
	/**
	 * @param institucion The institucion to set.
	 */
	public void setInstitucion(String institucion) {
		this.institucion = institucion;
	}
	/**
	 * @return Returns the fechaFinal.
	 */
	public String getFechaFinal() {
		return fechaFinal;
	}
	/**
	 * @param fechaFinal The fechaFinal to set.
	 */
	public void setFechaFinal(String fechaFinal) {
		this.fechaFinal = fechaFinal;
	}
	/**
	 * @return Returns the fechaInicial.
	 */
	public String getFechaInicial() {
		return fechaInicial;
	}
	/**
	 * @param fechaInicial The fechaInicial to set.
	 */
	public void setFechaInicial(String fechaInicial) {
		this.fechaInicial = fechaInicial;
	}
	/**
	 * @return Returns the tipoInterfaz.
	 */
	public String getTipoInterfaz() {
		return tipoInterfaz;
	}
	/**
	 * @param tipoInterfaz The tipoInterfaz to set.
	 */
	public void setTipoInterfaz(String tipoInterfaz) {
		this.tipoInterfaz = tipoInterfaz;
	}
	/**
	 * @return Returns the usuarioResponsable.
	 */
	public String getUsuarioResponsable() {
		return usuarioResponsable;
	}
	/**
	 * @param usuarioResponsable The usuarioResponsable to set.
	 */
	public void setUsuarioResponsable(String usuarioResponsable) {
		this.usuarioResponsable = usuarioResponsable;
	}
	/**
	 * @return Returns the codigoTipoInterfaz.
	 */
	public int getCodigoTipoInterfaz() {
		return codigoTipoInterfaz;
	}
	/**
	 * @param codigoTipoInterfaz The codigoTipoInterfaz to set.
	 */
	public void setCodigoTipoInterfaz(int codigoTipoInterfaz) {
		this.codigoTipoInterfaz = codigoTipoInterfaz;
	}
	/**
	 * @return Returns the documentoFinal.
	 */
	public String getDocumentoFinal() {
		return documentoFinal;
	}
	/**
	 * @param documentoFinal The documentoFinal to set.
	 */
	public void setDocumentoFinal(String documentoFinal) {
		this.documentoFinal = documentoFinal;
	}
	/**
	 * @return Returns the documentoInicial.
	 */
	public String getDocumentoInicial() {
		return documentoInicial;
	}
	/**
	 * @param documentoInicial The documentoInicial to set.
	 */
	public void setDocumentoInicial(String documentoInicial) {
		this.documentoInicial = documentoInicial;
	}
	/**
	 * @return Returns the errores.
	 */
	public ActionErrors getErrores() {
		return errores;
	}
	/**
	 * @param errores The errores to set.
	 */
	public void setErrores(ActionErrors errores) {
		this.errores = errores;
	}
	/**
	 * @return Returns the mapArchivo.
	 */
	public HashMap getMapArchivo() {
		return mapArchivo;
	}
	/**
	 * @param mapArchivo The mapArchivo to set.
	 */
	public void setMapArchivo(HashMap mapArchivo) {
		this.mapArchivo = mapArchivo;
	}
	/**
	 * @return Retorna un elemento de mapArchivo.
	 */
	public Object getMapArchivo(String key) {
		return mapArchivo.get(key);
	}
	/**
	 * @param mapArchivo The mapArchivo to set.
	 */
	public void setMapArchivo(String key,Object obj) {
		this.mapArchivo.put(key,obj);
	}
	/**
	 * @return Returns the numMapArchivo.
	 */
	public int getNumMapArchivo() {
		return numMapArchivo;
	}
	/**
	 * @param numMapArchivo The numMapArchivo to set.
	 */
	public void setNumMapArchivo(int numMapArchivo) {
		this.numMapArchivo = numMapArchivo;
	}
}
