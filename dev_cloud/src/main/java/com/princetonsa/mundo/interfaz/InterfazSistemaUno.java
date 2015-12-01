package com.princetonsa.mundo.interfaz;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.interfaz.InterfazSistemaUnoDao;
import com.princetonsa.dto.facturacion.DtoFactura;
import com.princetonsa.mundo.cargos.Cargos;
import com.servinte.axioma.fwk.exception.IPSException;


/**
 * 
 * @author Andres Silva Monsalve
 *
 */

public class InterfazSistemaUno
{

	private int consecutivoProceso;

	/**
	 * HashMap de incosistencias
	 * */
	private HashMap inconsistenciasMap;
	
	private HashMap formasPagoMap;
	
	private HashMap cuentaContableRc;
	
	
	private boolean transaccion;
	
	private BufferedWriter buffer;
	
	public static Logger logger = Logger.getLogger(InterfazSistemaUno.class);
	
	private int consecutivoProcesoFacRc =0;
	
	private HashMap nombresPacienteMap;
	
	
	/**
	 * Array List posee los HashMap de inconsistencias generados por los
	 * archivos Planos Colsanitas, divididos por convenio.
	 * */
	private ArrayList<HashMap<String,Object>> InconsistenciasArray;
	
	/**
	 * 
	 * @return
	 */
	public static InterfazSistemaUnoDao getInterfazSistemaUnoDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getInterfazSistemaUnoDao();
	}
	


	public Object setInterfazSistemaUno(Connection con, String tipo, String reproceso, String fechaInicial, String fechaFinal, String nombre, String path, String institucion, String usuario) 
	{
		
		
		return getInterfazSistemaUnoDao().setInterfazSistemaUno(con,tipo, reproceso, fechaInicial, fechaFinal, nombre, path, institucion, usuario, consecutivoProceso);
	}


/**
 * Consulta todas Las Facturas que hay en el rango de fechas
 * @param con
 * @param fechaInicial
 * @param fechaFinal
 * @param contabilizado
 * @return
 */
	public HashMap consultarFacturas(Connection con, String fechaInicial, String fechaFinal , String contabilizado)
	{
		
		return getInterfazSistemaUnoDao().consultarFacturas(con, fechaInicial, fechaFinal, contabilizado);
	}
	
	
	public HashMap consultarRecibosCaja(Connection con, String fechaInicial, String fechaFinal, String reproceso) 
	{
		
		return getInterfazSistemaUnoDao().consultarRecibosCaja(con, fechaInicial, fechaFinal, reproceso);
	}

	
	public Object cuentaContable(Connection con, int convenio, int tipoCuenta, String tipoRegimen, int institucion) 
	{
		return getInterfazSistemaUnoDao().consultarCuentaContable(con, convenio+"", tipoCuenta, tipoRegimen, institucion+"");
	}
	

	public ActionErrors armarPlano(Connection con, int numRegistros,  ArrayList<DtoFactura> facturaDto, String path, String nombre, ActionErrors errores,boolean esTodo) throws IPSException 
	{
		
		
		logger.info("Entro al metodo armar Plano");
		logger.info("Armar Plano numeroRegistros"+numRegistros);
		logger.info("Armar Plano FacturaDTo"+facturaDto);
		logger.info("Armar Plano nombre"+nombre);
		logger.info("Armar Plano ES TODO"+esTodo);
		
		String registrosFactura ="";
		
		String registrosFactura1 ="";
		String registrosFactura2 ="";
		String registrosFactura3 ="";
		String registrosFactura4 ="";
		String registrosFactura5 ="";
		String registrosFactura6 ="";
		String registrosFactura7 ="";
		String cuentaContable ="";
		String valorTrans ="";
		String campo32 ="";
		String campo89 ="";
		String campo92="";
		String campo93="";
		String campo94="";
		String campo95="";
		String campo96="";
		String intermedio6a18="";
		String total ="";
		
		int posNombreArchivos = 0;
		int contadorRegistros = 0;
		String aux="";
		String ccontable="";
		String tmpDetalle="";
		String [] split;
		String fecha="";
		String idPaciente ="";
		String idConvenio ="";
		String consecutivo ="";
		String naturaleza ="";
		String valorDoc="";
		String vencimiento ="";
		
		String convenioParticular="";
		
		double sumDebitos;
		double sumCreditos;
		double temporalValor;
		double valorConsumoPQ;
		
		double totales;
		boolean indInconsistencia = false;
		this.inconsistenciasMap = new HashMap();
		DtoFactura dto;
		
//		Inicializa el Array list que posee los HashMap de Inconsistencias
		this.InconsistenciasArray = new ArrayList<HashMap<String,Object>>();
		
		
		
			// Verificar que la posicion no sea Nula
			if(numRegistros == ConstantesBD.codigoNuncaValido)
			{
				errores.add("descripcion", new ActionMessage("errors.invalid","Error al Cargar el Nombre de Archivo"));
				return errores;
			}
			
			// Nombre del Archivo a Crear
			nombre+=".txt";
			contadorRegistros = 0;
			
			//Verificar si es posible escribir en el archivo
		/*	if(nombre.equals(ConstantesBD.acronimoNo) 
					|| nombre.equals(ConstantesBD.acronimoSi)
					&& nombre.equals(ConstantesBD.acronimoSi))*/
			
			if(!nombre.equals(""))
			
			{
				try
				{
					try
					{
						// Apertura del Archivo Plano Sistema Uno
						
											
						File archivo = new File(path,nombre);
						FileWriter streamUno = new FileWriter(archivo, false);
						this.buffer = new BufferedWriter(streamUno);
						
						logger.info("FACTURA DTO=> "+facturaDto);
						
						// Verificar que el Array no este vacio
						if(facturaDto!=null)
						{
							// Inicializa el HashMap de inconsistencias del Archivo a Generar
							this.inconsistenciasMap = new HashMap();
							this.inconsistenciasMap.put("numRegistros", 0);
							this.inconsistenciasMap.put("archivoAsociado",posNombreArchivos);
							
							consecutivoProceso = UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_interfaz_sistema_uno");
							
							logger.info("NUMERO DE FACTURAS=> "+facturaDto.size());
							
							for(int j=0; j<facturaDto.size(); j++)
							{
								indInconsistencia = false;
								registrosFactura1 = "";
								registrosFactura2 = "";
								registrosFactura3 = "";
								registrosFactura4 = "";
								registrosFactura5 = "";
								registrosFactura6 = "";
								registrosFactura7 = "";
								intermedio6a18 = "";
								consecutivo = "";
								naturaleza = "";
								valorDoc = "";
								vencimiento ="";
								convenioParticular="";
								dto = facturaDto.get(j);
								
								sumCreditos=0;
								sumDebitos=0;
								temporalValor=0;
								valorConsumoPQ=0;
								
								// Validar que exista el dto
								if(dto != null)
								{
									// ************** SE INICIALIZA LA CAPTURA DE LOS CAMPOS DEL REGISTRO *****************
									logger.info("\n\n");
									logger.info("..............::::::   DATOS REGISTRO  ::::::......... "+j);	
									logger.info("");
									
									// CAMPO Nº1 CONSECUTIVO DE GRABACION
									
									// CAMPO Nº2 INSTITUCION
									//consecutivo=dto.getInstitucion()+"";
									consecutivo="HH";
									consecutivo=completarFormato(consecutivo, consecutivo.length(), 2, "alpha", " ");
									
									registrosFactura1=consecutivo;
									
									// CAMPO Nº3 CENTRO DE ATENCION
									
									consecutivo=dto.getCentroAtencion().getCodigo()+"";
									consecutivo = completarFormato(consecutivo, consecutivo.length(), 3, "nume", "0");
									registrosFactura1+=consecutivo;
									
									// CAMPO Nº4 TIPO DE MOVIMIENTO
									
									consecutivo=ConstantesBD.tipoMovimientoFacturaVenta;
									consecutivo=completarFormato(consecutivo, consecutivo.length(), 2, "alpha", " ");
									registrosFactura1+=consecutivo;
									
									// CAMPO Nº5 NUMERO DE DOCUMENTO
									
									/*
									if(UtilidadTexto.formatearValores(dto.getConsecutivoFactura(),"0").length()>6)
								   {
										logger.info("ENTRO A VALIDACION DE TAMAÑO CONSECUTIVO ??????????????");
										//this.transaccion = false;
										errores.add("descripcion", new ActionMessage("error.numeroDocInterfazUno","Factura","6"));
										return errores;
										// Lineas para realizar truncamiento.
										//String nuevoCampo = UtilidadTexto.formatearValores(dto.getConsecutivoFactura(),"0").substring(0, 8); 
										//logger.info("Campo 5 corrtado->"+nuevoCampo);
									}
									else
									{ */
										consecutivo=UtilidadTexto.formatearValores(dto.getConsecutivoFactura(),"0");
										consecutivo=completarFormato(consecutivo, consecutivo.length(), 6, "nume", "0");
										registrosFactura1+=consecutivo;
								  //}
									
									// CAMPO Nº6 ID DEL PACIENTE
									
									// CAMPO Nº7 ENVIAR EN CERO
									
									intermedio6a18=completarFormato(aux,aux.length(), 2, "alpha","0");
									
									// CAMPO Nº8 FECHA DE EMISION DEL DOCUMENTO
									
									split = UtilidadFecha.conversionFormatoFechaABD(dto.getFecha()).split("-");
									fecha = split[0]+split[1]+split[2];
									
									intermedio6a18+=fecha;
									
									// CAMPO Nº9 PERIODO DE GENERACION DE LA INTERFAZ
									
									fecha = split[0]+split[1];
									intermedio6a18+=fecha;
									
									// CAMPO Nº10 ESPACIOS
									
									intermedio6a18+=completarFormato(aux,aux.length(), 8, "alpha"," ");
									
									
									// CAMPO Nº11 ESPACIOS
									
									intermedio6a18+=completarFormato(aux,aux.length(), 8, "alpha"," ");
									
									// CAMPO Nº 12 ESPACIOS
									
									intermedio6a18+=completarFormato(aux,aux.length(), 40, "alpha"," ");
									
									// CAMPO Nº 13 ESPACIOS
									
									intermedio6a18+=completarFormato(aux,aux.length(), 13, "alpha"," ");
									
									// CAMPO Nº 14 VALOR DEL DOCUMENTO
									
									valorDoc=(UtilidadTexto.formatearValores(dto.getValorTotal(),"#.00")+"").replaceAll("\\.", "");
									logger.info("TOTAL FACTURA -->"+valorDoc+"<--");
									valorDoc = completarFormato(valorDoc, valorDoc.length(), 17, "nume", "0")+"+";
									
									intermedio6a18+=valorDoc;
									
									// CAMPO Nº 15 INTERNO
									
									if(dto.getEstadoFacturacion().getCodigo() == ConstantesBD.codigoEstadoFacturacionAnulada)
										intermedio6a18+="X";
									else
										intermedio6a18+=" ";	
									
									intermedio6a18+=completarFormato(aux,aux.length(), 19, "alpha"," ");
									
									// CAMPO Nº 16 INTERNO
									
									intermedio6a18+=completarFormato(aux,aux.length(), 3, "alpha"," ");
									
									
									
									// CAMPO Nº 18 NUMERO ID DEL PACIENTE  = Nº6
									
									
									
									// CAMPO Nº 19 EN CEROS
									
									registrosFactura2=completarFormato(aux,aux.length(), 2, "nume","0");
									
									// CAMPO Nº 20 CENTRO DE ATENCION
									
									consecutivo=dto.getCentroAtencion().getCodigo()+"";
									consecutivo = completarFormato(consecutivo, consecutivo.length(), 3, "nume", "0");
									
									registrosFactura2+=consecutivo;
									
									// CAMPO Nº 21 NATURALEZA DE LA TRANSACCION
									
									
									// CAMPO Nº 23 VALOR EN MONEDA EXTRANJERA
									
									registrosFactura3+=completarFormato(aux,aux.length(), 17, "nume","0")+"+";
									
									// CAMPO Nº 24 TASA DE CONVENSION
									
									registrosFactura3+=completarFormato(aux,aux.length(), 11, "nume","0")+"+";
									
									// CAMPO Nº 25 TASA DE CAMBIO
									
									registrosFactura3+=completarFormato(aux,aux.length(), 11, "nume","0")+"+";
									
									// CAMPO Nº 26 IVA BASE
									
									registrosFactura3+=completarFormato(aux,aux.length(), 17, "nume","0")+"+";
									
									// CAMPO Nº 27 TASA DE IMPUESTO
									
									registrosFactura3+=completarFormato(aux,aux.length(), 6, "nume","0");
									
									// CAMPO Nº 28 % DE IVA
									
									registrosFactura3+=completarFormato(aux,aux.length(), 6, "nume","0");
									
									// CAMPO Nº 29 EN CEROS
									
									registrosFactura3+=completarFormato(aux,aux.length(), 13, "nume","0")+"+";
									
									// CAMPO Nº 30 DETALLE DE LA TRANSACCION
									
									registrosFactura3+=completarFormato(aux,aux.length(), 40, "alpha"," ");
									
									// CAMPO Nº 31 DETALLE DE LA TRANSACCION
									
									registrosFactura3+=completarFormato(aux,aux.length(), 40, "alpha"," ");
									
									// CAMPO Nº 33 CODIGO CONCEPTO DE PAGO	(DIFIERE PARA RECIBOS DE CAJA)
									
									registrosFactura4+=completarFormato(aux,aux.length(), 8, "alpha"," ");
									
									// CAMPO Nº 34 DESCRIPCION CONCEPTO DE PAGO (DIFIERE PARA RECIBOS DE CAJA)
									
									registrosFactura4+=completarFormato(aux,aux.length(), 40, "alpha"," ");
									
									// CAMPO Nº 35 EN BLANCO
									
									registrosFactura4+=completarFormato(aux,aux.length(), 10, "alpha"," ");
									
									// CAMPO POR FUERA DEL DOCUMENTO
									
									registrosFactura4+=completarFormato(aux,aux.length(), 40, "alpha"," ");
									
									// CAMPO Nº 36 NA
									
									registrosFactura4+=completarFormato(aux,aux.length(), 2, "alpha"," ");
									
									// CAMPO Nº 37 NA
									
									registrosFactura4+=completarFormato(aux,aux.length(), 6, "nume","0");
									
									// CAMPO Nº 38 NA
									
									registrosFactura4+=completarFormato(aux,aux.length(), 4, "alpha"," ");
									
									// CAMPO Nº 39 NA
									
									registrosFactura4+=completarFormato(aux,aux.length(), 12, "nume","0");
									
									// CAMPO Nº 40 NA
									
									registrosFactura4+=completarFormato(aux,aux.length(), 30, "alpha"," ");
									
									// CAMPO Nº 41 TIPO DOCUMENTO CRUCE
									
									registrosFactura4+=ConstantesBD.tipoMovimientoFacturaVenta;
									
									// CAMPO Nº 42 NRO FACTURA
									
									consecutivo=UtilidadTexto.formatearValores(dto.getConsecutivoFactura(),"0");
									consecutivo=completarFormato(consecutivo, consecutivo.length(), 6, "nume", "0");
									
									registrosFactura4+=consecutivo;
									
									
									// CAMPO Nº 43 NA
									
									registrosFactura4+=completarFormato(aux,aux.length(), 2, "nume","0");
									
									// CAMPO Nº 44 FECHA DE VENCIMIENTO
									
									vencimiento = completarFormato(aux,aux.length(), 8, "nume","0");
									
									// CAMPO Nº 45 NUMERO ID USUARIO CAJA
									
									registrosFactura5=completarFormato(aux,aux.length(), 13, "alpha"," ");
									
									// CAMPO Nº 46 CODIGO CONVENIO PACIENTE
									
									consecutivo=dto.getConvenio().getCodigo()+"";
									consecutivo = completarFormato(consecutivo, consecutivo.length(), 13, "alpha", " ");
									
									registrosFactura5+=consecutivo;
									
									// CAMPO Nº 47 NA
									
									registrosFactura5+=completarFormato(aux,aux.length(), 2, "nume","0");
									
									// CAMPO Nº 48 INDICATIVO DE ANTICIPO
									
									registrosFactura5+=completarFormato(aux,aux.length(), 1, "nume","0");
									
									// CAMPO Nº 49 REFERENCIA EL ANTICIPO
									
									registrosFactura5+=completarFormato(aux,aux.length(), 11, "alpha","0");
									
									// CAMPO Nº 50 DIAS DE GRACIA
									
									registrosFactura5+=completarFormato(aux,aux.length(), 3, "nume","0");
									
									// CAMPO Nº 51 NA
									
									registrosFactura5+=completarFormato(aux,aux.length(), 8, "nume","0");
									
									// CAMPO Nº 52 NA
									
									registrosFactura5+=completarFormato(aux,aux.length(), 13, "nume","0")+"+";
									
									// CAMPO Nº 53 NA
									
									registrosFactura5+=completarFormato(aux,aux.length(), 13, "nume","0")+"+";
									
									// CAMPO Nº 54 FECHA FACTURA (APLICA SOLO PARA SALDOS INICIALES)
									
									registrosFactura5+=completarFormato(aux,aux.length(), 8, "nume","0");
									
									// CAMPO Nº 55 INTERNO
									
									registrosFactura5+=completarFormato(aux,aux.length(), 1, "alpha"," ");
									
									// CAMPO Nº 56 INTERNO
									
									registrosFactura5+=completarFormato(aux,aux.length(), 11, "alpha"," ");
									
									// CAMPO Nº 57 NA
									
									registrosFactura5+=completarFormato(aux,aux.length(), 12, "alpha"," ");
									
									// CAMPO Nº 58 NA
									
									registrosFactura5+=completarFormato(aux,aux.length(), 2, "nume","0");
									
									// CAMPO Nº 59 NA
									
									registrosFactura5+=completarFormato(aux,aux.length(), 8, "nume","0");
									
									// CAMPO Nº 60 NA
									
									registrosFactura5+=completarFormato(aux,aux.length(), 8, "nume","0");
									
									// CAMPO Nº 61 NA
									
									registrosFactura5+=completarFormato(aux,aux.length(), 8, "alpha"," ");
									
									// CAMPO Nº 62 NA
									
									registrosFactura5+=completarFormato(aux,aux.length(), 13, "alpha"," ");
									
									// CAMPO Nº 63 NA
									
									registrosFactura5+=completarFormato(aux,aux.length(), 2, "nume","0");
									
									// CAMPO Nº 64 NA
									
									registrosFactura5+=completarFormato(aux,aux.length(), 3, "nume","0");
									
									// CAMPO Nº 65 NA
									
									registrosFactura5+=completarFormato(aux,aux.length(), 8, "alpha"," ");
									
									// CAMPO Nº 66 NA
									
									registrosFactura5+=completarFormato(aux,aux.length(), 10, "alpha"," ");
									
									// CAMPO Nº 67 NA
									
									registrosFactura5+=completarFormato(aux,aux.length(), 40, "alpha"," ");
									
									// CAMPO Nº 68 NA
									
									registrosFactura5+=completarFormato(aux,aux.length(), 8, "nume","0");
									
									// CAMPO Nº 69 NA
									
									registrosFactura5+=completarFormato(aux,aux.length(), 8, "nume","0");
									
									// CAMPO Nº 70 NA
									
									registrosFactura5+=completarFormato(aux,aux.length(), 20, "alpha"," ");
									
									// CAMPO Nº 71 CODIGO CAJA 										(DIFIERE PARA RECIBOS DE CAJA)
									
									registrosFactura5+=completarFormato(aux,aux.length(), 3, "alpha"," ");
									
									// CAMPO Nº 72 CODIFICACION DE LA CUENTA CONTABLE
									
									registrosFactura5+=completarFormato(aux,aux.length(), 3, "alpha"," ");
									
									// CAMPO Nº 73 NATURALEZA DE LA TRANSACCION CAJA 				(DIFIERE PARA RECIBOS DE CAJA)
									
									registrosFactura5+=completarFormato(aux,aux.length(), 1, "alpha"," ");
									
									// CAMPO Nº 74 FECHA DE RECAUDO (DIFIERE PARA RECIBOS DE CAJA)
									
									registrosFactura5+=completarFormato(aux,aux.length(), 8, "nume","0");
									
									// CAMPO Nº 75 TIPO DE PAGO 									(DIFIERE PARA RECIBOS DE CAJA)
									
									registrosFactura5+=completarFormato(aux,aux.length(), 1, "alpha"," ");
									
									// CAMPO Nº 76 INDICADOR DE MOVIMIENTO 							(DIFIERE PARA RECIBOS DE CAJA)
									
									registrosFactura5+=completarFormato(aux,aux.length(), 1, "alpha"," ");
									
									// CAMPO Nº 77 NA
									
									registrosFactura5+=completarFormato(aux,aux.length(), 1, "alpha"," ");
									
									// CAMPO Nº 78 TIPO DE PAGO 									(DIFIERE PARA RECIBOS DE CAJA)
									
									consecutivo = "001";
									registrosFactura5+=consecutivo;
									
									// CAMPO Nº 79 COD BANCO DEL CHEQUE								(DIFIERE PARA RECIBOS DE CAJA)
									
									registrosFactura5+=completarFormato(aux,aux.length(), 4, "alpha"," ");
									
									// CAMPO Nº 80 NUMERO DE CHEQUE
									
									registrosFactura5+=completarFormato(aux,aux.length(), 6, "nume","0");
									
									// CAMPO Nº 81 NA
									
									registrosFactura5+=completarFormato(aux,aux.length(), 3, "alpha"," ");
									
									// CAMPO Nº 82 NA
									
									registrosFactura5+=completarFormato(aux,aux.length(), 6, "nume","0");
									
									// CAMPO Nº 83 NA
									
									registrosFactura5+=completarFormato(aux,aux.length(), 10, "alpha"," ");
									
									// CAMPO Nº 84 NA
									
									registrosFactura5+=completarFormato(aux,aux.length(), 20, "alpha"," ");
									
									// CAMPO Nº 85 NA
									
									registrosFactura5+=completarFormato(aux,aux.length(), 15, "alpha"," ");
									
									// CAMPO Nº 86 NUMERO CUENTA DEL CHEQUE RECIBIDO
									
									registrosFactura5+=completarFormato(aux,aux.length(), 20, "alpha"," ");
									
									// CAMPO Nº 87 NA
									
									registrosFactura5+=completarFormato(aux,aux.length(), 8, "nume"," ");
									
									// CAMPO Nº 88 INTERNO
									
									registrosFactura5+=completarFormato(aux,aux.length(), 5, "alpha"," ");
									
									// CAMPO Nº 89 NUMERO DEL NIT DEL CONVENIO O PARTICULAR
									
									//CAMBIONUEVO
									campo89 = nitConvenio(con, dto.getConvenio().getCodigo())+"";
									idConvenio = campo89;
									
									idConvenio = completarFormato(idConvenio.trim(), idConvenio.length(), 13, "alpha", " ");
									campo89 = completarFormato(campo89.trim(), campo89.length(), 15, "alpha", " ");
									
									// CAMPO Nº 90 DIGITO VERIFICADOR
									
									String digiVerifica = digitoVerificadorTercero(con, dto.getConvenio().getCodigo())+"";
									digiVerifica = completarFormato(digiVerifica, digiVerifica.length(), 1, "nume", "0");
									registrosFactura6=digiVerifica;
									
									// CAMPO Nº 91 TIPO IDENTIFICACION TERCERO						(DIFIERE PARA RECIBOS DE CAJA)
									
									registrosFactura6+=completarFormato(aux,aux.length(), 1, "alpha","N");
									
									// CAMPO Nº 92 NOMBRE DEL CONVENIO								(DIFIERE PARA RECIBOS DE CAJA)
									
									campo92 = dto.getConvenio().getNombre();
									campo92 = completarFormato(campo92, campo92.length(), 50, "alpha", " ");
									
									// CAMPO Nº 93 NOMBRE DEL CONVENIO
									
									campo93=completarFormato(aux,aux.length(), 50, "alpha"," ");
									
									// CAMPO Nº 94 NA
									
									campo94=completarFormato(aux,aux.length(), 15, "alpha"," ");
									
									// CAMPO Nº 95 NA
									
									campo95=completarFormato(aux,aux.length(), 15, "alpha"," ");
									
									// CAMPO Nº 96 NA
									
									campo96=completarFormato(aux,aux.length(), 20, "alpha"," ");
									
									// CAMPO Nº 97 TIPO DE TERCERO									(DIFIERE PARA RECIBOS DE CAJA)
									
									int tipoTer = tipoTercero(con, dto.getConvenio().getCodigo());
									registrosFactura7=tipoTer+"";
									
									// CAMPO Nº 98 CODIGO DEL TIPO DE IDENTIFICACION DEL TERCERO
									
									registrosFactura7+=completarFormato(aux,aux.length(), 1, "nume","0");
									
									// CAMPO Nº 99 INDICADOR CLIENTE
									
									registrosFactura7+=completarFormato(aux,aux.length(), 1, "nume","1");
									
									// CAMPO Nº 100 NA
									
									registrosFactura7+=completarFormato(aux,aux.length(), 1, "nume","0");
									
									// CAMPO Nº 101 NA
									
									registrosFactura7+=completarFormato(aux,aux.length(), 1, "nume","0");
									
									// CAMPO Nº 102 NA
									
									registrosFactura7+=completarFormato(aux,aux.length(), 1, "nume","0");
									
									// CAMPO Nº 103 NA
									
									registrosFactura7+=completarFormato(aux,aux.length(), 1, "nume","0");
									
									// CAMPO Nº 104 NA
									
									registrosFactura7+=completarFormato(aux,aux.length(), 1, "nume","0");
									
									// CAMPO Nº 105 NA
									
									registrosFactura7+=completarFormato(aux,aux.length(), 1, "nume","0");
									
									// CAMPO Nº 106 ESTADO DEL CONVENIO
									if(dto.getConvenio().getActivo())
									{
										consecutivo=" ";
									}
									else
									{
										consecutivo="X";
										
									}
									registrosFactura7+=consecutivo;
									
									// CAMPO Nº 107 FECHA DE CREACION DEL CONVENIO					(DIFIERE PARA RECIBOS DE CAJA)
									
									consecutivo="20080101";
									registrosFactura7+=consecutivo;
									
									// CAMPO Nº 108 CODIGO DEL PAIS DEL CONVENIO					(DIFIERE PARA RECIBOS DE CAJA)
									
									HashMap convenioEmpresa = datoEmpresa(con, dto.getConvenio().getCodigo());
									consecutivo = convenioEmpresa.get("pais_principal_0").toString();
									consecutivo = completarFormato(consecutivo, consecutivo.length(), 3, "nume", "0");
									registrosFactura7+=consecutivo;
									
									
									// CAMPO Nº 109 CODIGO DEPARTAMENTO DEL CONVENIO				(DIFIERE PARA RECIBOS DE CAJA)
									
									consecutivo = convenioEmpresa.get("depto_principal_0").toString();
									consecutivo = completarFormato(consecutivo, consecutivo.length(), 2, "nume", "0");
									registrosFactura7+=consecutivo;
									
									
									// CAMPO Nº 110 CODIGO DE LA CIUDAD DEL CONVENIO
									
									consecutivo = convenioEmpresa.get("ciudad_principal_0").toString();
									consecutivo = completarFormato(consecutivo, consecutivo.length(), 3, "nume", "0");
									registrosFactura7+=consecutivo;
									
									// CAMPO Nº 111 DIRECCION CORRESPONDENCIA DEL CONVENIO 1
									
									consecutivo = convenioEmpresa.get("direccion_0").toString();
									consecutivo = completarFormato(consecutivo, consecutivo.length(), 120, "alpha", " ");
									registrosFactura7+=consecutivo;
									
									// CAMPO Nº 114 TELEFONO
									
									consecutivo = convenioEmpresa.get("telefono_0").toString();
									
									if(consecutivo.length()>15)
									{
										consecutivo = consecutivo.substring(0, 15);
									}
									
									consecutivo = completarFormato(consecutivo, consecutivo.length(), 15, "alpha", " ");
									registrosFactura7+=consecutivo;
									
									// CAMPO Nº 115 FAX
									
									registrosFactura5+=completarFormato(aux,aux.length(), 15, "alpha"," ");
									
									// CAMPO Nº 116 CODIGO POSTAL
									
									registrosFactura7+=completarFormato(aux,aux.length(), 25, "alpha"," ");
									
									// CAMPO Nº 117 DIRECCION ELECTRONICA DEL CONVENIO
									
									consecutivo = convenioEmpresa.get("email_0").toString();
									consecutivo = completarFormato(consecutivo, consecutivo.length(), 50, "alpha", " ");
									registrosFactura7+=consecutivo;
									
									// CAMPO Nº 118 NA
									
									registrosFactura7+=completarFormato(aux,aux.length(), 1, "nume","0");
									
									// CAMPO Nº 119 BARRIO
									
									registrosFactura7+=completarFormato(aux,aux.length(), 15, "alpha"," ");
									
									// CAMPO Nº 120 TELEFONO 2 DEL CONVENIO
									
									registrosFactura7+=completarFormato(aux,aux.length(), 15, "alpha"," ");
									
									// CAMPO Nº 121 NA
									
									registrosFactura7+=completarFormato(aux,aux.length(), 1, "nume","0");
									
									// CAMPO Nº 122 NA
									
									registrosFactura7+=completarFormato(aux,aux.length(), 6, "alpha"," ");
									
									// CAMPO Nº 123 INTERNO
									
									registrosFactura7+=completarFormato(aux,aux.length(), 58, "alpha"," ");
									
									// CAMPO Nº 124 CLASE DE CLIENTE
									
									consecutivo = tipoConvenio(con, dto.getConvenio().getCodigo())+"";
									if (consecutivo.equals(""))
									{
										consecutivo = completarFormato(aux, aux.length(), 6, "alpha", " ");
									}
									else
									{
										consecutivo = completarFormato(consecutivo, consecutivo.length(), 6, "alpha", " ");
									}
																		
									registrosFactura7+=consecutivo;
									
									// CAMPO Nº 125 CENTRO DE OPERACION DEL CLIENTE
									
									consecutivo = dto.getCentroAtencion().getCodigo()+"";
									consecutivo = completarFormato(consecutivo, consecutivo.length(), 3, "nume", "0");
									registrosFactura7+=consecutivo;
									
									// CAMPO Nº 126 CODIGO ZONA / SUBZONA
									
									consecutivo = "000001";
									registrosFactura7+=consecutivo;
									
									// CAMPO Nº 127 CODIGO VENDEDOR
									
									registrosFactura7+=completarFormato(aux,aux.length(), 4, "alpha"," ");
									
									// CAMPO Nº 128 NOMBRE DEL CONTACTO
									
									registrosFactura7+=completarFormato(aux,aux.length(), 40, "alpha"," ");
									
									// CAMPO Nº 129 CALIFICACION DEL CLIENTE
									
									consecutivo = "A";
									registrosFactura7+=consecutivo;
									
									// CAMPO Nº 130 INDICADOR RTE IVA
									
									registrosFactura7+=completarFormato(aux,aux.length(), 1, "nume","0");
									
									// CAMPO Nº 131 INDICADOR DE RETEFUENTE
									
									registrosFactura7+=completarFormato(aux,aux.length(), 1, "nume","0");
									
									// CAMPO Nº 132 INTERNO
									
									registrosFactura7+=completarFormato(aux,aux.length(), 3, "nume","0");
									
									// CAMPO Nº 133
									
									registrosFactura7+=completarFormato(aux,aux.length(), 2, "alpha"," ");
									
									// CAMPO Nº 134 OBSERVACION DEL CLIENTE
									
									registrosFactura7+=completarFormato(aux,aux.length(), 40, "alpha"," ");
									
									// CAMPO Nº 135 DIAS DE GRACIA
									
									registrosFactura7+=completarFormato(aux,aux.length(), 3, "nume","0");
									
									// CAMPO Nº 136 CUPO DE CREDITO
									
									registrosFactura7+=completarFormato(aux,aux.length(), 11, "nume","0");
									
									// CAMPO Nº 137 COD CRITERIO 1
									
									registrosFactura7+=completarFormato(aux,aux.length(), 4, "alpha"," ");
									
									// CAMPO Nº 138 COD CRITERIO 2
									
									registrosFactura7+=completarFormato(aux,aux.length(), 4, "alpha"," ");
									
									// CAMPO Nº 139 COD CRITERIO 3
									
									registrosFactura7+=completarFormato(aux,aux.length(), 4, "alpha"," ");
									
									// CAMPO Nº 140 IND DE BLOQUE POR CUPO
									
									registrosFactura7+=completarFormato(aux,aux.length(), 1, "nume","0");
									
									// CAMPO Nº 141 IND DE BLOQUE POR MORA
									
									registrosFactura7+=completarFormato(aux,aux.length(), 1, "nume","0");
									
									// CAMPO Nº 142 IND ESTADO DEL CLIENTE
									
									registrosFactura7+=completarFormato(aux,aux.length(), 1, "alpha"," ");
									
									// CAMPO Nº 143 IND EXIGE ORDEN DE COMPRA
									
									registrosFactura7+=completarFormato(aux,aux.length(), 1, "nume","0");
									
									// CAMPO Nº 144 IND EXIGE REMISION
									
									registrosFactura7+=completarFormato(aux,aux.length(), 1, "nume","0");
									
									// CAMPO Nº 145 CODIGO LISTA DE PRECIOS
									
									registrosFactura7+=completarFormato(aux,aux.length(), 3, "alpha"," ");
									
									// CAMPO Nº 146 CODIGO LISTA DE DESCUENTOS
									
									registrosFactura7+=completarFormato(aux,aux.length(), 2, "alpha"," ");
									
									// CAMPO Nº 147 INDICATIVO DESCUENTO GLOBAL
									
									registrosFactura7+=completarFormato(aux,aux.length(), 1, "nume","0");
									
									// CAMPO Nº 148 TASA DESCUENTO GLOBAL 
									
									registrosFactura7+=completarFormato(aux,aux.length(), 4, "nume","0");
									
									// CAMPO Nº 149 INDI EXCLUIDO IMPUESTO
									
									registrosFactura7+=completarFormato(aux,aux.length(), 1, "nume","0");
									
									// CAMPO Nº 150 FORMA DE PAGO
									
									registrosFactura7+=completarFormato(aux,aux.length(), 1, "nume","0");
									
									// CAMPO Nº 151 IND ANTICIPADO
									
									registrosFactura7+=completarFormato(aux,aux.length(), 1, "nume","0");
									
									// CAMPO Nº 152 IND CHEQUES POSTFECHADOS
									
									registrosFactura7+=completarFormato(aux,aux.length(), 1, "nume","0");
									
									// CAMPO Nº 153 COD MONEDA
									
									registrosFactura7+=completarFormato(aux,aux.length(), 2, "alpha"," ");
									
									// CAMPO Nº 154 OBSERVACION VENTA
									
									registrosFactura7+=completarFormato(aux,aux.length(), 40, "alpha"," ");
									
									// CAMPO Nº 155 OBSERVACION
									
									registrosFactura7+=completarFormato(aux,aux.length(), 40, "alpha"," ");
									
									// CAMPO Nº 156 CODIGO DESC PRONTO PAGO
									
									registrosFactura7+=completarFormato(aux,aux.length(), 2, "alpha"," ");
									
									// CAMPO Nº 157 IND RETE ICA
									
									registrosFactura7+=completarFormato(aux,aux.length(), 1, "nume","0");
									
									// CAMPO Nº 158 COD RUTA VISITA
									
									registrosFactura7+=completarFormato(aux,aux.length(), 4, "alpha"," ");
									
									// CAMPO Nº 159 RUTA DE TRANSPORTE
									
									registrosFactura7+=completarFormato(aux,aux.length(), 4, "alpha"," ");
									
									// CAMPO Nº 160 BARRIO CLIENTE
									
									registrosFactura7+=completarFormato(aux,aux.length(), 15, "alpha"," ");
									
									// CAMPO Nº 161 ESTADO DE CARTERA
									
									registrosFactura7+=completarFormato(aux,aux.length(), 1, "alpha"," ");
									
									// CAMPO Nº 162 MES/DIA NACIMIENTO CONTACTO
									
									registrosFactura7+=completarFormato(aux,aux.length(), 4, "nume","0");
									
									// CAMPO Nº 163 CONTACTO CLIENTE
									
									registrosFactura7+=completarFormato(aux,aux.length(), 40, "alpha"," ");
									
									// CAMPO Nº 164 MES/DIA NACIMIENTO CLIENTE
									
									registrosFactura7+=completarFormato(aux,aux.length(), 4, "nume","0");
									
									// CAMPO Nº 165 DESCUENTO GLOBAL
									
									registrosFactura7+=completarFormato(aux,aux.length(), 4, "nume","0");
									
									// CAMPO Nº 166 FECHA BLOQUE
									
									registrosFactura7+=completarFormato(aux,aux.length(), 8, "nume","0");
									
									// CAMPO Nº 167 PUNTO DE ENVIO CLIENTE
									
									registrosFactura7+=completarFormato(aux,aux.length(), 4, "alpha"," ");
									
									// CAMPO Nº 168 CLASE DEL PROVEEDOR
									
									registrosFactura7+=completarFormato(aux,aux.length(), 6, "alpha"," ");
									
									// CAMPO Nº 169 CONTACTO DEL PROVEEDOR
									
									registrosFactura7+=completarFormato(aux,aux.length(), 40, "alpha"," ");
									
									// CAMPO Nº 170 IND REGIMEN SIMPLIFICADO
									
									registrosFactura7+=completarFormato(aux,aux.length(), 1, "alpha"," ");
									
									// CAMPO Nº 171 IND AUTO RETENCION
									
									registrosFactura7+=completarFormato(aux,aux.length(), 1, "alpha"," ");
									
									// CAMPO Nº 172 IND RETE IVA
									
									registrosFactura7+=completarFormato(aux,aux.length(), 1, "alpha"," ");
									
									// CAMPO Nº 173 IND RETE ICA
									
									registrosFactura7+=completarFormato(aux,aux.length(), 1, "alpha"," ");
									
									// CAMPO Nº 174 CONDICION DE PAGO
									
									registrosFactura7+=completarFormato(aux,aux.length(), 2, "alpha"," ");
									
									// CAMPO Nº 175 DIAS DE GRACIA
									
									registrosFactura7+=completarFormato(aux,aux.length(), 3, "alpha"," ");
									
									// CAMPO Nº 176 CUPO CREDITO DEL PROVEEDOR
									
									registrosFactura7+=completarFormato(aux,aux.length(), 11, "alpha"," ");
									
									// CAMPO Nº 177 COD MONEDA
									
									registrosFactura7+=completarFormato(aux,aux.length(), 2, "alpha"," ");
									
									// CAMPO Nº 178 ESTADO DEL PROVEEDOR
									
									registrosFactura7+=completarFormato(aux,aux.length(), 1, "alpha"," ");
									
									// CAMPO Nº 179 OBSERVACION
									
									registrosFactura7+=completarFormato(aux,aux.length(), 40, "alpha"," ");
									
									// CAMPO Nº 180 INTERNO
									
									registrosFactura7+=completarFormato(aux,aux.length(), 1, "alpha"," ");
									
									// CAMPO Nº 181 COD RETE ICA
									
									registrosFactura7+=completarFormato(aux,aux.length(), 2, "alpha"," ");
									
									// CAMPO Nº 182 INTERNO
									
									registrosFactura7+=completarFormato(aux,aux.length(), 2, "alpha"," ");
									
									// CAMPO Nº 183 INTERNO
									
									registrosFactura7+=completarFormato(aux,aux.length(), 8, "alpha"," ");
									
									// CAMPO Nº 184 DESCUENTO OTORGADO
									
									registrosFactura7+=completarFormato(aux,aux.length(), 4, "alpha"," ");
									
									// CAMPO Nº 185 METODO DE PAGO
									
									registrosFactura7+=completarFormato(aux,aux.length(), 1, "alpha"," ");
									
									// CAMPO Nº 186 CD NRO CUENTA PROVEEDOR
									
									registrosFactura7+=completarFormato(aux,aux.length(), 2, "alpha"," ");
									
									// CAMPO Nº 187 INTERNO
									
									registrosFactura7+=completarFormato(aux,aux.length(), 1, "alpha"," ");
									
									// CAMPO Nº 188 CIUDAD DE EXPEDICION
									
									registrosFactura7+=completarFormato(aux,aux.length(), 20, "alpha"," ");
									
									// CAMPO Nº 189 FECHA DE INGRESO
									
									registrosFactura7+=completarFormato(aux,aux.length(), 8, "alpha"," ");
									
									// CAMPO Nº 190 FECHA DE NACIMIENTO
									
									registrosFactura7+=completarFormato(aux,aux.length(), 8, "alpha"," ");
									
									// CAMPO Nº 191 CODIGO PAIS DE ORIGEN
									
									registrosFactura7+=completarFormato(aux,aux.length(), 3, "alpha"," ");
									
									// CAMPO Nº 192 CODIGO DTO DE ORIGEN
									
									registrosFactura7+=completarFormato(aux,aux.length(), 2, "alpha"," ");
									
									// CAMPO Nº 193 COD CIUDAD DE ORIGEN
									
									registrosFactura7+=completarFormato(aux,aux.length(), 3, "alpha"," ");
									
									// CAMPO Nº 194 SEXO
									
									registrosFactura7+=completarFormato(aux,aux.length(), 1, "alpha"," ");
									
									// CAMPO Nº 195 ESTADO CIVIL
									
									registrosFactura7+=completarFormato(aux,aux.length(), 1, "alpha"," ");
									
									// CAMPO Nº 196 RUTA
									
									registrosFactura7+=completarFormato(aux,aux.length(), 1, "alpha"," ");
									
									// CAMPO Nº 197 ORDEN TRANSPORTE
									
									registrosFactura7+=completarFormato(aux,aux.length(), 3, "alpha"," ");
									
									// CAMPO Nº 198 CARGO EMPLEADO
									
									registrosFactura7+=completarFormato(aux,aux.length(), 4, "alpha"," ");
									
									// CAMPO Nº 199 INTERNO
									
									registrosFactura7+=completarFormato(aux,aux.length(), 50, "alpha"," ");
									
									
									/**
									 * 
									 * 
									 * **********************  LOS DETALLES DE LA FACTURA  ********************
									 * 
									 * 
									 */
									

									
//									 CAMPO Nº 32 CENTRO DE COSTO QUE SOLICITA  (DIFIERE PARA RECIBOS DE CAJA)
									
									for (int x=0; x<dto.getDetallesFactura().size();x++)
									{
										// verificar con 
										if (dto.getDetallesFactura().get(x).getCodigoTipoSolicitud() == ConstantesBD.codigoTipoSolicitudCargosDirectosArticulos || dto.getDetallesFactura().get(x).getCodigoTipoSolicitud() == ConstantesBD.codigoTipoSolicitudMedicamentos)
										{
											centroCostoSolicitaArti(con, dto.getDetallesFactura().get(x).getNumeroSolicitud());
										}
										else
										{
											centroCostoSolicitaServ(con, dto.getDetallesFactura().get(x).getNumeroSolicitud());
										}
									}
									
									// CAMPO Nº 17 CUENTA CONTABLE -----------------MUY IMPORTANTE -------------------
									
									
									//*******  IDENTIFICACION DEL PACIENTE  CAMPOS 89, 92,93,94,95,96 ********
									String ccPaciente ="";
									String primerNomFac = "";
									String segundoNomFac ="";
									String primerApeFac ="";
									String segundoApeFac ="";
									String nombresCompletos="";
									String apellidoNombre="";
									
									
									this.nombresPacienteMap = new HashMap();
									setNombresPacienteMap(nombresPacienteFac(con, dto.getCodigoPaciente()));
									int numDatosPaciente = Utilidades.convertirAEntero(getNombresPacienteMap("numRegistros").toString());
									
									logger.info("&&&&&&&&&&&&&&&&&&  CODIGO PACIENTE---------->>>"+dto.getCodigoPaciente()+"<<<");
									
									if(numDatosPaciente>0)
									{
										
										ccPaciente = getNombresPacienteMap("numero_identificacion_0").toString();
										
										idPaciente=ccPaciente;
										
										idPaciente = completarFormato(idPaciente.trim(), idPaciente.length(), 13, "alpha", " ");
										ccPaciente = completarFormato(ccPaciente.trim(), ccPaciente.length(), 15, "alpha", " ");
										
										primerApeFac= getNombresPacienteMap("primer_apellido_0").toString();
										primerNomFac= getNombresPacienteMap("primer_nombre_0").toString();
										segundoApeFac = getNombresPacienteMap("segundo_apellido_0").toString();
										segundoNomFac = getNombresPacienteMap("segundo_nombre_0").toString();
										
										primerNomFac = UtilidadTexto.cambiarCaracteresEspeciales(primerNomFac);
										segundoNomFac = UtilidadTexto.cambiarCaracteresEspeciales(segundoNomFac);
										primerApeFac = UtilidadTexto.cambiarCaracteresEspeciales(primerApeFac);
										segundoApeFac = UtilidadTexto.cambiarCaracteresEspeciales(segundoApeFac);
										
										
										// CAMPO Nº 92 NOMBRE DEL CONVENIO								(DIFIERE PARA RECIBOS DE CAJA)
										apellidoNombre=primerApeFac+" "+primerNomFac;
										if(apellidoNombre.length()>50)
											apellidoNombre=apellidoNombre.substring(0, 50);
										else
											apellidoNombre = completarFormato(apellidoNombre, apellidoNombre.length(), 50, "alpha", " ");
										
										// CAMPO Nº 93 NOMBRE DEL CONVENIO
										
										campo93=completarFormato(aux,aux.length(), 50, "alpha"," ");
										
										// CAMPO Nº 94 NA
										
										if(primerApeFac.length()>15)
											primerApeFac=primerApeFac.substring(0, 15);
										else
											primerApeFac=completarFormato(primerApeFac,primerApeFac.length(), 15, "alpha"," ");
										
										// CAMPO Nº 95 NA
										
										if(segundoApeFac.length()>15)
											segundoApeFac=segundoApeFac.substring(0, 15);
										else
											segundoApeFac=completarFormato(segundoApeFac,segundoApeFac.length(), 15, "alpha"," ");
										
										// CAMPO Nº 96 NA
										
										nombresCompletos=primerNomFac+" "+segundoNomFac;
										if(nombresCompletos.length()>20)
											nombresCompletos=nombresCompletos.substring(0, 20);
										else
											nombresCompletos=completarFormato(nombresCompletos,nombresCompletos.length(), 20, "alpha"," ");
								
									}	
										//******* FIN IDENTIFICACION DEL PACIENTE  CAMPOS 89, 92,93,94,95,96 ********
									
										
										
									
									
									
									
									
									
									boolean valido=this.debitosCreditosIguales(con,dto);
									logger.info("VALIDO--->"+valido);
									logger.info("\n\n\n FINALIZA CALCULO DEBITOS CREDITOS \n\n\n ==========================================================================================================================\n\n");
									
									for (int cuenta =1 ; cuenta<11 ;cuenta++)
									{
										switch (cuenta) {
										
										case ConstantesBD.tipoCuentaConvenio:  //CUENTA ASOCIADA AL CONVENIO
											ccontable = consultarCuentaParametrizable(con, dto.getConvenio().getCodigo(), ConstantesBD.tipoCuentaConvenio, dto.getInstitucion(), ConstantesBD.codigoNuncaValido, ConstantesBD.codigoNuncaValido, ConstantesBD.codigoNuncaValido, ConstantesBD.codigoNuncaValido, ConstantesBD.codigoNuncaValido);
											
											if (ccontable.equals(""))
											{
												indInconsistencia = true;
												inconsistencias(dto.getFecha(), dto.getHora(), UtilidadTexto.formatearValores(dto.getConsecutivoFactura(),"0"), "Cuenta Asociada al Convenio", "No esta parametrizado", true);
											}
											else if(valido)
											{
												
												//Capturo la Naturaleza de la cuenta contable
												//naturaleza = naturalezaTrans(con, ccontable); //PARA CUANDO SE TOME LA NATURALEZA DE LA PARAMETRIZACION
												naturaleza=ConstantesBD.naturalezaDebito; 
												
												ccontable= completarFormato(ccontable,ccontable.length() , 8, "alpha", " ");
												//logger.info("Campo 17  cuenta convenio->"+ccontable);
												
												if(dto.getValorConvenio()>0)
												{
													valorTrans=UtilidadTexto.formatearValores(dto.getValorConvenio(),"#.00")+"".replaceAll("\\.", "");
													
													if(valorTrans.indexOf(".")>0)
													{
														split=valorTrans.split("\\.");
														
														if(Utilidades.convertirAEntero(split[1]) > 9)
															valorTrans=split[0]+split[1].substring(0, 2);
														else
															valorTrans=split[0]+split[1].substring(0, 1)+"0";
													}
													else
													{
														//valorTrans=(dto.getValorConvenio()+"").replaceAll("\\.", "0");
														valorTrans=UtilidadTexto.formatearValores(dto.getValorConvenio())+"00";
													}
													if (Utilidades.convertirADouble(valorTrans)>0)
													{
														
														valorTrans=completarFormato(valorTrans, valorTrans.length(), 17, "nume", "0")+"+";
														
														if(dto.getEstadoFacturacion().getCodigo() == ConstantesBD.codigoEstadoFacturacionAnulada)
															valorTrans=completarFormato(aux, aux.length(), 17, "nume", "0")+"+";
														//logger.info("Campo 22 Valor Convenio->"+valorTrans);
													
														// SUMATORIA DEBIDOS Y CREDITOS
														temporalValor= dto.getValorConvenio();
														if(naturaleza.equals(ConstantesBD.naturalezaDebito))
															sumDebitos = sumDebitos+temporalValor;
														if(naturaleza.equals(ConstantesBD.naturalezaCredito))
															sumCreditos = sumCreditos+temporalValor;
														
														campo32 =completarFormato(aux,aux.length(), 8, "alpha"," ");
														
														 // SE REALIZA EL CONSECUTIVO POR REGISTRO
														consecutivoProcesoFacRc = consecutivoProcesoFacRc + 1;
														consecutivo = consecutivoProcesoFacRc+"";
														consecutivo = completarFormato(consecutivo, consecutivo.length(), 9, "nume",  "0");
													//	logger.info("Campo 1 ->"+consecutivo);
														// FIN DEL CONSECUTIVO POR REGISTRO
														
													//	logger.info("---------NATURALEZA conv----->"+naturaleza+"-\n");
															
														/////////////////////////////////////////////*********************	
														int diasVen = diasVencimientoFactura(con, dto.getConvenio().getCodigo()+"");
														String diaVencimiento = UtilidadFecha.incrementarDiasAFecha(dto.getFecha(), diasVen, false);
														split = UtilidadFecha.conversionFormatoFechaABD(diaVencimiento).split("-");
														diaVencimiento = split[0]+split[1]+split[2];
														/////////////////////////////////////////////*********************	
														
														logger.info("111111111111111111111111111111111");	
														total = consecutivo+registrosFactura1+idConvenio+intermedio6a18+ccontable+idConvenio+registrosFactura2+naturaleza+valorTrans+registrosFactura3+campo32+registrosFactura4+diaVencimiento+registrosFactura5+campo89+registrosFactura6+campo92+campo93+campo94+campo95+campo96+registrosFactura7+"\n";
														buffer.write(total);
													}
												}
											}
											break;

											
										case ConstantesBD.tipoCuentaPaciente: //CUENTA ASOCIADA AL PACIENTE
											ccontable = consultarCuentaParametrizable(con, dto.getConvenio().getCodigo(), ConstantesBD.tipoCuentaPaciente, dto.getInstitucion(), ConstantesBD.codigoNuncaValido, ConstantesBD.codigoNuncaValido, ConstantesBD.codigoNuncaValido, ConstantesBD.codigoNuncaValido, ConstantesBD.codigoNuncaValido);
											
											if (ccontable.equals(""))
											{
												indInconsistencia = true;
												inconsistencias(dto.getFecha(), dto.getHora(), UtilidadTexto.formatearValores(dto.getConsecutivoFactura(),"0"), "Cuenta Asociada al Paciente", "No esta parametrizado", true);
											}
											else if(valido)
											{
												

												// Capturo la Naturaleza de la cuenta contable
												//naturaleza = naturalezaTrans(con, ccontable);//PARA CUANDO SE TOME LA NATURALEZA DE LA PARAMETRIZACION
												naturaleza=ConstantesBD.naturalezaDebito; 
												
												ccontable= completarFormato(ccontable,ccontable.length() , 8, "alpha", " ");
												//logger.info("Campo 17  cuenta paciente->"+ccontable);
												
												if(dto.getValorNetoPaciente() > 0)
												{
												
													valorTrans=UtilidadTexto.formatearValores(dto.getValorNetoPaciente(),"#.0")+"";
													if(valorTrans.indexOf(".")>0)
													{
														split=valorTrans.split("\\.");
														
														if(Utilidades.convertirAEntero(split[1]) > 9)
															valorTrans=split[0]+split[1].substring(0, 2);
														else
															valorTrans=split[0]+split[1].substring(0, 1)+"0";
													}
													else
													{
														//valorTrans=(dto.getValorNetoPaciente()+"").replaceAll("\\.", "0");
														valorTrans=UtilidadTexto.formatearValores(dto.getValorNetoPaciente())+"00";
													}
													
													
													if (Utilidades.convertirADouble(valorTrans)>0)
													{
														valorTrans=completarFormato(valorTrans, valorTrans.length(), 17, "nume", "0")+"+";
														if(dto.getEstadoFacturacion().getCodigo() == ConstantesBD.codigoEstadoFacturacionAnulada)
															valorTrans=completarFormato(aux, aux.length(), 17, "nume", "0")+"+";
														//logger.info("Campo 22 Valor Paciente->"+valorTrans);
														
														// SUMATORIA DEBIDOS Y CREDITOS
														temporalValor= dto.getValorNetoPaciente();
														if(naturaleza.equals(ConstantesBD.naturalezaDebito))
															sumDebitos = sumDebitos+temporalValor;
														if(naturaleza.equals(ConstantesBD.naturalezaCredito))
															sumCreditos = sumCreditos+temporalValor;
														
														campo32 =completarFormato(aux,aux.length(), 8, "alpha"," ");
														
														// SE REALIZA EL CONSECUTIVO POR REGISTRO
														consecutivoProcesoFacRc = consecutivoProcesoFacRc + 1;
														consecutivo = consecutivoProcesoFacRc+"";
														consecutivo = completarFormato(consecutivo, consecutivo.length(), 9, "nume",  "0");
														//logger.info("Campo 1 ->"+consecutivo);
													    // FIN DEL CONSECUTIVO POR REGISTRO
													//	logger.info("---------NATURALEZA paci----->"+naturaleza+"-\n");
														
														
														/////////////////////////////////////////////*********************	
														int diasVen = diasVencimientoFactura(con, dto.getConvenio().getCodigo()+"");
														String diaVencimiento = UtilidadFecha.incrementarDiasAFecha(dto.getFecha(), diasVen, false);
														split = UtilidadFecha.conversionFormatoFechaABD(diaVencimiento).split("-");
														diaVencimiento = split[0]+split[1]+split[2];
														/////////////////////////////////////////////*********************
														
														logger.info("22222222222222222222");
														total = consecutivo+registrosFactura1+idPaciente+intermedio6a18+ccontable+idPaciente+registrosFactura2+naturaleza+valorTrans+registrosFactura3+campo32+registrosFactura4+diaVencimiento+registrosFactura5+ccPaciente+registrosFactura6+apellidoNombre+campo93+primerApeFac+segundoApeFac+nombresCompletos+registrosFactura7+"\n";
														buffer.write(total);
													}
												}
											}
											break;
											
											
										case ConstantesBD.tipoCuentaDevolucionPaciente:  //CUENTA ASOCIADA A DEVOLUCION PACIENTE
											ccontable = consultarCuentaParametrizable(con, dto.getConvenio().getCodigo(), ConstantesBD.tipoCuentaDevolucionPaciente, dto.getInstitucion(), ConstantesBD.codigoNuncaValido, ConstantesBD.codigoNuncaValido, ConstantesBD.codigoNuncaValido, ConstantesBD.codigoNuncaValido, ConstantesBD.codigoNuncaValido);
										//	logger.info("contable--->"+ccontable);

											if (ccontable.equals(""))
											{
												indInconsistencia = true;
												inconsistencias(dto.getFecha(), dto.getHora(), UtilidadTexto.formatearValores(dto.getConsecutivoFactura(),"0"), "Cuenta Asociada Devolucion Paciente", "No esta parametrizado", true);
											}
											else if(valido)
											{
												
												//  Capturo la Naturaleza de la cuenta contable
												//naturaleza = naturalezaTrans(con, ccontable);//PARA CUANDO SE TOME LA NATURALEZA DE LA PARAMETRIZACION
												naturaleza=ConstantesBD.naturalezaCredito; 
												
												ccontable= completarFormato(ccontable,ccontable.length() , 8, "alpha", " ");
										//		logger.info("Campo 17  cuenta devolucion paciente->"+ccontable);
												
												//	Valor Devolucion Paciente
												if(dto.getValorNetoPaciente() < 0)
												{	
													valorTrans=UtilidadTexto.formatearValores(dto.getValorNetoPaciente(),"#.0")+"";
													if(valorTrans.indexOf(".")>0)
													{
														split=valorTrans.split("\\.");
														
														if(Utilidades.convertirAEntero(split[1]) > 9)
															valorTrans=split[0]+split[1].substring(0, 2);
														else
															valorTrans=split[0]+split[1].substring(0, 1)+"0";
													}
													else
													{
														//valorTrans=(dto.getValorNetoPaciente()+"").replaceAll("\\.", "0");
														valorTrans=dto.getValorNetoPaciente()+"00";
													}
													
													if (Utilidades.convertirADouble(valorTrans)>0)
													{
														valorTrans=completarFormato(valorTrans, valorTrans.length(), 17, "nume", "0")+"+";
														
														if(dto.getEstadoFacturacion().getCodigo() == ConstantesBD.codigoEstadoFacturacionAnulada)
															valorTrans=completarFormato(aux, aux.length(), 17, "nume", "0")+"+";
											//			logger.info("Campo 22 Valor Devolucion Paciente->"+valorTrans);
															
														// SUMATORIA DEBIDOS Y CREDITOS
														temporalValor= dto.getValorNetoPaciente();
														if(naturaleza.equals(ConstantesBD.naturalezaDebito))
															sumDebitos = sumDebitos+temporalValor;
														if(naturaleza.equals(ConstantesBD.naturalezaCredito))
															sumCreditos = sumCreditos+temporalValor;
													
														campo32 = completarFormato(aux,aux.length(), 8, "alpha"," ");
														
														// SE REALIZA EL CONSECUTIVO POR REGISTRO
														consecutivoProcesoFacRc = consecutivoProcesoFacRc + 1;
														consecutivo = consecutivoProcesoFacRc+"";
														consecutivo = completarFormato(consecutivo, consecutivo.length(), 9, "nume",  "0");
											//			logger.info("Campo 1 ->"+consecutivo);
													    // FIN DEL CONSECUTIVO POR REGISTRO
											//			logger.info("---------NATURALEZA dpac----->"+naturaleza+"-\n");
														logger.info("3333333333333333333333333");
														total = consecutivo+registrosFactura1+idPaciente+intermedio6a18+ccontable+idPaciente+registrosFactura2+naturaleza+valorTrans+registrosFactura3+campo32+registrosFactura4+vencimiento+registrosFactura5+ccPaciente+registrosFactura6+apellidoNombre+campo93+primerApeFac+segundoApeFac+nombresCompletos+registrosFactura7+"\n";
														buffer.write(total);
													}
												}
											}
											break;
											
										
										case ConstantesBD.tipoCuentaDescuentosPaciente: // CUENTA ASOCIADA AL DESCUENTO PACIENTE
											ccontable = consultarCuentaParametrizable(con, dto.getConvenio().getCodigo(), ConstantesBD.tipoCuentaDescuentosPaciente, dto.getInstitucion(), ConstantesBD.codigoNuncaValido, ConstantesBD.codigoNuncaValido, ConstantesBD.codigoNuncaValido, ConstantesBD.codigoNuncaValido, ConstantesBD.codigoNuncaValido);
																				
											if (ccontable.equals(""))
											{
												//NO SE GENERA INCONSISTENCIA
												indInconsistencia = false;
											}
											else if(valido)
											{
												
												//Capturo la Naturaleza de la cuenta contable
												//naturaleza = naturalezaTrans(con, ccontable);//PARA CUANDO SE TOME LA NATURALEZA DE LA PARAMETRIZACION
												naturaleza=ConstantesBD.naturalezaDebito; 
												
												ccontable= completarFormato(ccontable,ccontable.length() , 8, "alpha", " ");
											//	logger.info("Campo 17  descuento paciente->"+ccontable);
												
												// Valor Descuento Paciente
												if(dto.getValorDescuentoPaciente() > 0)
												{	
													valorTrans=UtilidadTexto.formatearValores(dto.getValorDescuentoPaciente(),"#.0")+"";
													if(valorTrans.indexOf(".")>0)
													{
														split=valorTrans.split("\\.");
														
														if(Utilidades.convertirAEntero(split[1]) > 9)
															valorTrans=split[0]+split[1].substring(0, 2);
														else
															valorTrans=split[0]+split[1].substring(0, 1)+"0";
													}
													else
													{
														//valorTrans=(dto.getValorDescuentoPaciente()+"").replaceAll("\\.", "0");
														valorTrans=dto.getValorDescuentoPaciente()+"00";
													}
													
													
													if (Utilidades.convertirADouble(valorTrans)>0)
													{
														valorTrans=completarFormato(valorTrans, valorTrans.length(), 17, "nume", "0")+"+";
														
														if(dto.getEstadoFacturacion().getCodigo() == ConstantesBD.codigoEstadoFacturacionAnulada)
															valorTrans=completarFormato(aux, aux.length(), 17, "nume", "0")+"+";
												//		logger.info("Campo 22 Valor Descuento Paciente->"+valorTrans);
		
														// SUMATORIA DEBIDOS Y CREDITOS
														temporalValor= dto.getValorDescuentoPaciente();
														if(naturaleza.equals(ConstantesBD.naturalezaDebito))
															sumDebitos = sumDebitos+temporalValor;
														if(naturaleza.equals(ConstantesBD.naturalezaCredito))
															sumCreditos = sumCreditos+temporalValor;
														
														campo32 = completarFormato(aux,aux.length(), 8, "alpha"," ");
														
														// SE REALIZA EL CONSECUTIVO POR REGISTRO
														consecutivoProcesoFacRc = consecutivoProcesoFacRc + 1;
														consecutivo = consecutivoProcesoFacRc+"";
														consecutivo = completarFormato(consecutivo, consecutivo.length(), 9, "nume",  "0");
												//		logger.info("Campo 1 ->"+consecutivo);
													    // FIN DEL CONSECUTIVO POR REGISTRO
														
												//		logger.info("---------NATURALEZA desc----->"+naturaleza+"-\n");
														
														logger.info("44444444444444");
														total = consecutivo+registrosFactura1+idPaciente+intermedio6a18+ccontable+idPaciente+registrosFactura2+naturaleza+valorTrans+registrosFactura3+campo32+registrosFactura4+vencimiento+registrosFactura5+ccPaciente+registrosFactura6+apellidoNombre+campo93+primerApeFac+segundoApeFac+nombresCompletos+registrosFactura7+"\n";
														buffer.write(total);
													}
												}
											}
											break;
											
										
										case ConstantesBD.tipoCuentaAbonosPaciente:  //	CUENTA ASOCIADA A ABONOS PACIENTE
											ccontable = consultarCuentaParametrizable(con, dto.getConvenio().getCodigo(), ConstantesBD.tipoCuentaAbonosPaciente, dto.getInstitucion(), ConstantesBD.codigoNuncaValido, ConstantesBD.codigoNuncaValido, ConstantesBD.codigoNuncaValido, ConstantesBD.codigoNuncaValido, ConstantesBD.codigoNuncaValido);
											
											if (ccontable.equals(""))
											{
												indInconsistencia = true;
												inconsistencias(dto.getFecha(), dto.getHora(), UtilidadTexto.formatearValores(dto.getConsecutivoFactura(),"0"), "Cuenta Asociada Abonos Paciente", "No esta parametrizado",true);
											}
											else if(valido)
											{
												
//												Capturo la Naturaleza de la cuenta contable
												//naturaleza = naturalezaTrans(con, ccontable);//PARA CUANDO SE TOME LA NATURALEZA DE LA PARAMETRIZACION
												naturaleza=ConstantesBD.naturalezaDebito; 
												
												ccontable= completarFormato(ccontable,ccontable.length() , 8, "alpha", " ");
											//	logger.info("Campo 17  cuenta abonos paciente->"+ccontable);
												
												
												// Valor Abonos
												if(dto.getValorAbonos() > 0)
												{	
													valorTrans=UtilidadTexto.formatearValores(dto.getValorAbonos(),"#.0")+"";
													if(valorTrans.indexOf(".")>0)
													{
														split=valorTrans.split("\\.");
														
														if(Utilidades.convertirAEntero(split[1]) > 9)
															valorTrans=split[0]+split[1].substring(0, 2);
														else
															valorTrans=split[0]+split[1].substring(0, 1)+"0";
													}
													else
													{
														//valorTrans=(dto.getValorAbonos()+"").replaceAll("\\.", "0");
														valorTrans=dto.getValorAbonos()+"00";
													}
													
													if (Utilidades.convertirADouble(valorTrans)>0)
													{
														valorTrans=completarFormato(valorTrans, valorTrans.length(), 17, "nume", "0")+"+";
														
														if(dto.getEstadoFacturacion().getCodigo() == ConstantesBD.codigoEstadoFacturacionAnulada)
															valorTrans=completarFormato(aux, aux.length(), 17, "nume", "0")+"+";
													//	logger.info("Campo 22 Valor Abonos Paciente->"+valorTrans);
															
														// SUMATORIA DEBIDOS Y CREDITOS
														temporalValor= dto.getValorAbonos();
														if(naturaleza.equals(ConstantesBD.naturalezaDebito))
															sumDebitos = sumDebitos+temporalValor;
														if(naturaleza.equals(ConstantesBD.naturalezaCredito))
															sumCreditos = sumCreditos+temporalValor;
														
														campo32 = completarFormato(aux,aux.length(), 8, "alpha"," ");
														
														// SE REALIZA EL CONSECUTIVO POR REGISTRO
														consecutivoProcesoFacRc = consecutivoProcesoFacRc + 1;
														consecutivo = consecutivoProcesoFacRc+"";
														consecutivo = completarFormato(consecutivo, consecutivo.length(), 9, "nume",  "0");
													//	logger.info("Campo 1 ->"+consecutivo);
														// FIN DEL CONSECUTIVO POR REGISTRO
														
													//	logger.info("---------NATURALEZA abon----->"+naturaleza+"-\n");
														logger.info("5555555555555555");
														total = consecutivo+registrosFactura1+idPaciente+intermedio6a18+ccontable+idPaciente+registrosFactura2+naturaleza+valorTrans+registrosFactura3+campo32+registrosFactura4+vencimiento+registrosFactura5+ccPaciente+registrosFactura6+apellidoNombre+campo93+primerApeFac+segundoApeFac+nombresCompletos+registrosFactura7+"\n";
														buffer.write(total);
													}
												}
											}
											break;
										
											
										case ConstantesBD.tipoCuentaServicios: // CUENTA ASOCIADA A SERVICIOS
											

											for(int x=0; x<dto.getDetallesFactura().size(); x++)
											{	
												String codServicio = dto.getDetallesFactura().get(x).getCodigoServicio()+" ";
												int numeroSolicitudOriginal=dto.getDetallesFactura().get(x).getNumeroSolicitud();
												//logger.info("Cod Servicio ---->"+codServicio);
												//logger.info("solicitud ---->"+numeroSolicitudOriginal);
												if (dto.getDetallesFactura().get(x).getCodigoTipoSolicitud() != ConstantesBD.codigoTipoSolicitudCargosDirectosArticulos && dto.getDetallesFactura().get(x).getCodigoTipoSolicitud() != ConstantesBD.codigoTipoSolicitudMedicamentos)
												{	
													campo32 = centroCostoSolicitaServ(con, dto.getDetallesFactura().get(x).getNumeroSolicitud()).toString();
													if (campo32.equals(""))
													{
														indInconsistencia = true;
														inconsistencias(dto.getFecha(), dto.getHora(), UtilidadTexto.formatearValores(dto.getConsecutivoFactura(),"0"), "Centro de Costo Servicio Ejecuta", "No esta parametrizado", true);
													}
													else
													{
														if (campo32.length()>8)
														{
															indInconsistencia = true;
															inconsistencias(dto.getFecha(), dto.getHora(), UtilidadTexto.formatearValores(dto.getConsecutivoFactura(),"0"), "Identificacion Centro Costo", "Debe ser de maximo 8 caracteres", true);
														}
														else
														{
															campo32 =completarFormato(campo32, campo32.length(), 8, "alpha", " ");
														}
													}
													
													
													if(dto.getDetallesFactura().get(x).getCodigoTipoSolicitud() == ConstantesBD.codigoTipoSolicitudPaquetes)
													{
														for(int y=0; y<dto.getDetallesFactura().get(x).getPaquetizacionDetalleFactura().size();y++)
														{
														//	logger.info("solicitud PQ---->"+dto.getDetallesFactura().get(x).getPaquetizacionDetalleFactura().get(y).getSolicitud());
															//valorTrans=(valorCuentaContable(con, dto.getDetallesFactura().get(x).getPaquetizacionDetalleFactura().get(y).getSolicitud(), ConstantesBD.codigoNuncaValido)+"".toString()).replaceAll("\\.", "0"); 
															
															if(dto.getDetallesFactura().get(x).getPaquetizacionDetalleFactura().get(y).getTipoSolicitud() == ConstantesBD.codigoTipoSolicitudCirugia)
															{
																valorTrans=UtilidadTexto.formatearValores(dto.getDetallesFactura().get(x).getPaquetizacionDetalleFactura().get(y).getValorPool(),"#.0")+"";
																if(valorTrans.indexOf(".")>0)
																{
																	split=valorTrans.split("\\.");
																	
																	if(Utilidades.convertirAEntero(split[1]) > 9)
																		valorTrans=split[0]+split[1].substring(0, 2);
																	else
																		valorTrans=split[0]+split[1].substring(0, 1)+"0";
																}
																else
																{
																	//valorTrans=dto.getDetallesFactura().get(x).getPaquetizacionDetalleFactura().get(y).getValorPool().replaceAll("\\.", "0");
																	valorTrans=dto.getDetallesFactura().get(x).getPaquetizacionDetalleFactura().get(y).getValorPool()+"00";
																}
															}
															else
															{
																valorTrans=UtilidadTexto.formatearValores(dto.getDetallesFactura().get(x).getPaquetizacionDetalleFactura().get(y).getValorTotal(),"#.0")+"";
																if(valorTrans.indexOf(".")>0)
																{
																	split=valorTrans.split("\\.");
																	
																	if(Utilidades.convertirAEntero(split[1]) > 9)
																		valorTrans=split[0]+split[1].substring(0, 2);
																	else
																		valorTrans=split[0]+split[1].substring(0, 1)+"0";
																}
																else
																{
																	//valorTrans=(dto.getDetallesFactura().get(x).getPaquetizacionDetalleFactura().get(y).getValorTotal()+"").replaceAll("\\.", "0");
																	valorTrans=dto.getDetallesFactura().get(x).getPaquetizacionDetalleFactura().get(y).getValorTotal()+"00";
																}
															}
															
															if (Utilidades.convertirADouble(valorTrans)>0)
															{
																//********CALCULO VALOR DEL CONSUMO DEL PAQUETE *********
																double sumatoriaConsumo = dto.getDetallesFactura().get(x).getPaquetizacionDetalleFactura().get(y).getValorTotal();
																valorConsumoPQ=valorConsumoPQ+sumatoriaConsumo;
																//******************************************************
																
																valorTrans=completarFormato(valorTrans, valorTrans.length(), 17, "nume", "0")+"+";
															//	logger.info("Valor PQ --->"+valorTrans);
																
																ccontable = consultarCuentaParametrizable(con, dto.getConvenio().getCodigo(), ConstantesBD.tipoCuentaServicios, dto.getInstitucion(), dto.getDetallesFactura().get(x).getPaquetizacionDetalleFactura().get(y).getServicio().getCodigo(), ConstantesBD.codigoNuncaValido, ConstantesBD.codigoNuncaValido, ConstantesBD.codigoNuncaValido, Utilidades.obtenerEsquemasTarifarioServicioArticulo(con, dto.getSubCuenta()+"", Utilidades.obtenerCodigoContratoSubcuenta(dto.getSubCuenta()), dto.getDetallesFactura().get(x).getPaquetizacionDetalleFactura().get(y).getServicio().getCodigo(), true,UtilidadFecha.getFechaActual(con), Cargos.obtenerCentroAtencionCargoSolicitud(con,dto.getDetallesFactura().get(x).getNumeroSolicitud())));
																
																	
																	if (ccontable.equals(""))
																	{
																		indInconsistencia = true;
																		inconsistencias(dto.getFecha(), dto.getHora(), UtilidadTexto.formatearValores(dto.getConsecutivoFactura(),"0"), "Cuenta Servicio", "No esta parametrizado",true);
																	}
																	else if(valido)
																	{
																		logger.info("666666666666666666666");
																		//	Capturo la Naturaleza de la cuenta contable
																		//naturaleza = naturalezaTrans(con, ccontable);//PARA CUANDO SE TOME LA NATURALEZA DE LA PARAMETRIZACION
																		naturaleza=ConstantesBD.naturalezaCredito; 
																		
																		ccontable= completarFormato(ccontable,ccontable.length() , 8, "alpha", " ");
																	//	logger.info("Campo 17  cuenta servicios->"+ccontable);
																		//Uno la cadena de la cuenta contable con la del detalle
																		
																		// SE REALIZA EL CONSECUTIVO POR REGISTRO
																		consecutivoProcesoFacRc = consecutivoProcesoFacRc + 1;
																		consecutivo = consecutivoProcesoFacRc+"";
																		consecutivo = completarFormato(consecutivo, consecutivo.length(), 9, "nume",  "0");
																	//	logger.info("Campo 1 ->"+consecutivo);
																	    // FIN DEL CONSECUTIVO POR REGISTRO
																		
																	//	logger.info("---------NATURALEZA ser1----->"+naturaleza+"-\n");
																		
																		if(dto.getEstadoFacturacion().getCodigo() == ConstantesBD.codigoEstadoFacturacionAnulada)
																			valorTrans=completarFormato(aux, aux.length(), 17, "nume", "0")+"+";
																		
																		// SUMATORIA DEBIDOS Y CREDITOS
																		temporalValor= dto.getDetallesFactura().get(x).getPaquetizacionDetalleFactura().get(y).getValorTotal();
																		if(naturaleza.equals(ConstantesBD.naturalezaDebito))
																			sumDebitos = sumDebitos+temporalValor;
																		if(naturaleza.equals(ConstantesBD.naturalezaCredito))
																			sumCreditos = sumCreditos+temporalValor;
																		
																		
																		total = consecutivo+registrosFactura1+idConvenio+intermedio6a18+ccontable+idConvenio+registrosFactura2+naturaleza+valorTrans+registrosFactura3+campo32+registrosFactura4+vencimiento+registrosFactura5+campo89+registrosFactura6+campo92+campo93+campo94+campo95+campo96+registrosFactura7+"\n";
																		buffer.write(total);
																	}
															}// Fin registro con Valor
														}
													}
													else 
														if(dto.getDetallesFactura().get(x).getCodigoTipoSolicitud() == ConstantesBD.codigoTipoSolicitudCirugia)
														{
															for(int y=0; y<dto.getDetallesFactura().get(x).getAsociosDetalleFactura().size();y++)
															{
																// Valor Servicio Paquete
																valorTrans=UtilidadTexto.formatearValores(dto.getDetallesFactura().get(x).getValorTotal(),"#.0")+""; 
																if(valorTrans.indexOf(".")>0)
																{
																	split=valorTrans.split("\\.");
																	
																	if(Utilidades.convertirAEntero(split[1]) > 9)
																		valorTrans=split[0]+split[1].substring(0, 2);
																	else
																		valorTrans=split[0]+split[1].substring(0, 1)+"0";
																}
																else
																{
																	//valorTrans=(dto.getDetallesFactura().get(x).getValorTotal()+"").replaceAll("\\.", "0");
																	valorTrans=dto.getDetallesFactura().get(x).getValorTotal()+"00";
																}
																if (Utilidades.convertirADouble(valorTrans)>0)
																{
																	valorTrans=completarFormato(valorTrans, valorTrans.length(), 17, "nume", "0")+"+";
																	if(dto.getDetallesFactura().get(y).getEsServicio())
																	{
																		ccontable = consultarCuentaParametrizable(con, dto.getConvenio().getCodigo(), ConstantesBD.tipoCuentaServicios, dto.getInstitucion(), dto.getDetallesFactura().get(x).getAsociosDetalleFactura().get(y).getCodigoServicioAsocio(), ConstantesBD.codigoNuncaValido, ConstantesBD.codigoNuncaValido, ConstantesBD.codigoNuncaValido, Utilidades.obtenerEsquemasTarifarioServicioArticulo(con, dto.getSubCuenta()+"", Utilidades.obtenerCodigoContratoSubcuenta(dto.getSubCuenta()), dto.getDetallesFactura().get(x).getCodigoServicio(), true,UtilidadFecha.getFechaActual(con), Cargos.obtenerCentroAtencionCargoSolicitud(con,dto.getDetallesFactura().get(x).getNumeroSolicitud())));
																		
																		if (ccontable.equals(""))
																		{
																			indInconsistencia = true;
																			inconsistencias(dto.getFecha(), dto.getHora(), UtilidadTexto.formatearValores(dto.getConsecutivoFactura(),"0"), "Cuenta Servicio", "No esta parametrizado",true);
																		}
																		else if(valido)
																		{
																			logger.info("6666666666666666666666666666666");
																			//	Capturo la Naturaleza de la cuenta contable
																			//naturaleza = naturalezaTrans(con, ccontable);//PARA CUANDO SE TOME LA NATURALEZA DE LA PARAMETRIZACION
																			naturaleza=ConstantesBD.naturalezaCredito; 
																			
																			ccontable= completarFormato(ccontable,ccontable.length() , 8, "alpha", " ");
																		//	logger.info("Campo 17  cuenta servicios->"+ccontable);
																			//Uno la cadena de la cuenta contable con la del detalle
																			
																			// SE REALIZA EL CONSECUTIVO POR REGISTRO
																			consecutivoProcesoFacRc = consecutivoProcesoFacRc + 1;
																			consecutivo = consecutivoProcesoFacRc+"";
																			consecutivo = completarFormato(consecutivo, consecutivo.length(), 9, "nume",  "0");
																		//	logger.info("Campo 1 ->"+consecutivo);
																		    // FIN DEL CONSECUTIVO POR REGISTRO
																			
																		//	logger.info("---------NATURALEZA ser1----->"+naturaleza+"-\n");
																			
																			if(dto.getEstadoFacturacion().getCodigo() == ConstantesBD.codigoEstadoFacturacionAnulada)
																				valorTrans=completarFormato(aux, aux.length(), 17, "nume", "0")+"+";
																			
																			// SUMATORIA DEBIDOS Y CREDITOS
																			temporalValor= Utilidades.convertirADouble(valorCuentaContable(con, dto.getDetallesFactura().get(x).getNumeroSolicitud(), ConstantesBD.codigoNuncaValido)+"");
																			if(naturaleza.equals(ConstantesBD.naturalezaDebito))
																				sumDebitos = sumDebitos+temporalValor;
																			if(naturaleza.equals(ConstantesBD.naturalezaCredito))
																				sumCreditos = sumCreditos+temporalValor;
																			
																			
																			total = consecutivo+registrosFactura1+idConvenio+intermedio6a18+ccontable+idConvenio+registrosFactura2+naturaleza+valorTrans+registrosFactura3+campo32+registrosFactura4+vencimiento+registrosFactura5+campo89+registrosFactura6+campo92+campo93+campo94+campo95+campo96+registrosFactura7+"\n";
																			buffer.write(total);
																		}
																	}
																}//Fin registro con Valor
															}
														}
														
													else
													{
														// Valor Servicio
														valorTrans=	UtilidadTexto.formatearValores(dto.getDetallesFactura().get(x).getValorTotal(),"#.0")+"";
														if(valorTrans.indexOf(".")>0)
														{
															split=valorTrans.split("\\.");
															
															if(Utilidades.convertirAEntero(split[1]) > 9)
																valorTrans=split[0]+split[1].substring(0, 2);
															else
																valorTrans=split[0]+split[1].substring(0, 1)+"0";
														}
														else
														{
															//valorTrans=(dto.getDetallesFactura().get(x).getValorTotal()+"").replaceAll("\\.", "0");
															valorTrans=dto.getDetallesFactura().get(x).getValorTotal()+"00";
														}
														
														if (Utilidades.convertirADouble(valorTrans)>0)
														{
															valorTrans=completarFormato(valorTrans, valorTrans.length(), 17, "nume", "0")+"+";
															tmpDetalle = consultarCuentaParametrizable(con, dto.getConvenio().getCodigo(), ConstantesBD.tipoCuentaServicios, dto.getInstitucion(), dto.getDetallesFactura().get(x).getCodigoServicio(), ConstantesBD.codigoNuncaValido, ConstantesBD.codigoNuncaValido, ConstantesBD.codigoNuncaValido, Utilidades.obtenerEsquemasTarifarioServicioArticulo(con, dto.getSubCuenta()+"", Utilidades.obtenerCodigoContratoSubcuenta(dto.getSubCuenta()), dto.getDetallesFactura().get(x).getCodigoServicio(), true,UtilidadFecha.getFechaActual(con), Cargos.obtenerCentroAtencionCargoSolicitud(con,dto.getDetallesFactura().get(x).getNumeroSolicitud())));
															
															if (tmpDetalle.equals(""))
															{
																indInconsistencia = true;
																inconsistencias(dto.getFecha(), dto.getHora(), UtilidadTexto.formatearValores(dto.getConsecutivoFactura(),"0"), "Cuenta Servicio", "No esta parametrizado",true);
															}
															else if(valido)
															{
																
																//	Capturo la Naturaleza de la cuenta contable
																//naturaleza = naturalezaTrans(con, tmpDetalle);//PARA CUANDO SE TOME LA NATURALEZA DE LA PARAMETRIZACION
																naturaleza=ConstantesBD.naturalezaCredito; 
																
																tmpDetalle= completarFormato(tmpDetalle,tmpDetalle.length() , 8, "alpha", " ");
																
																// SE REALIZA EL CONSECUTIVO POR REGISTRO
																consecutivoProcesoFacRc = consecutivoProcesoFacRc + 1;
																consecutivo = consecutivoProcesoFacRc+"";
																consecutivo = completarFormato(consecutivo, consecutivo.length(), 9, "nume",  "0");
															//	logger.info("Campo 1 ->"+consecutivo);
															    // FIN DEL CONSECUTIVO POR REGISTRO
																
															//	logger.info("---------NATURALEZA ser2----->"+naturaleza+"-\n");
																
																if(dto.getEstadoFacturacion().getCodigo() == ConstantesBD.codigoEstadoFacturacionAnulada)
																	valorTrans=completarFormato(aux, aux.length(), 17, "nume", "0")+"+";
																																	//															SUMATORIA DEBIDOS Y CREDITOS
																temporalValor= dto.getDetallesFactura().get(x).getValorTotal();
																if(naturaleza.equals(ConstantesBD.naturalezaDebito))
																	sumDebitos = sumDebitos+temporalValor;
																if(naturaleza.equals(ConstantesBD.naturalezaCredito))
																	sumCreditos = sumCreditos+temporalValor;
																
																logger.info("7777777777777777777777");
																logger.info("CCONTABLE -->"+ccontable);
																logger.info("TMPDETALE -->"+tmpDetalle);
																total = consecutivo+registrosFactura1+idConvenio+intermedio6a18+tmpDetalle+idConvenio+registrosFactura2+naturaleza+valorTrans+registrosFactura3+campo32+registrosFactura4+vencimiento+registrosFactura5+campo89+registrosFactura6+campo92+campo93+campo94+campo95+campo96+registrosFactura7+"\n";
																buffer.write(total);
															}
														}//FIN registro con valor
													}
												}
											}
											break;
											
											
										case ConstantesBD.tipoCuentaInventarios: // CUENTA ASOCIADA A INVENTARIOS
											for(int x=0;x<dto.getDetallesFactura().size();x++)
											{
											//	logger.info("ARTICULO ----->"+dto.getDetallesFactura().get(x).getCodigoArticulo());
												String codArticulo = dto.getDetallesFactura().get(x).getCodigoArticulo()+" ";
												
												if (dto.getDetallesFactura().get(x).getCodigoTipoSolicitud() == ConstantesBD.codigoTipoSolicitudCargosDirectosArticulos || dto.getDetallesFactura().get(x).getCodigoTipoSolicitud() == ConstantesBD.codigoTipoSolicitudMedicamentos || dto.getDetallesFactura().get(x).getCodigoTipoSolicitud() == ConstantesBD.codigoTipoSolicitudPaquetes)
												{
													campo32 = centroCostoSolicitaArti(con, dto.getDetallesFactura().get(x).getNumeroSolicitud()).toString();
													if (campo32.equals(""))
													{
														indInconsistencia = true;
														inconsistencias(dto.getFecha(), dto.getHora(), UtilidadTexto.formatearValores(dto.getConsecutivoFactura(),"0"), "Centro de Costo Servicio Solicita", "No esta parametrizado", true);
													}
													else
													{
														if (campo32.length()>8)
														{
															indInconsistencia = true;
															inconsistencias(dto.getFecha(), dto.getHora(), UtilidadTexto.formatearValores(dto.getConsecutivoFactura(),"0"), "Identificacion Centro Costo", "Debe ser de maximo 8 caracteres", true);
														}
														else
														{
															campo32 =completarFormato(campo32, campo32.length(), 8, "alpha", " ");
														}
													}
													
												}
												
													// Valor Inventarios
													valorTrans=UtilidadTexto.formatearValores(dto.getDetallesFactura().get(x).getValorTotal(),"#.0")+"";
													
													if(valorTrans.indexOf(".")>0)
													{
														split=valorTrans.split("\\.");
														
														if(Utilidades.convertirAEntero(split[1]) > 9)
															valorTrans=split[0]+split[1].substring(0, 2);
														else
															valorTrans=split[0]+split[1].substring(0, 1)+"0";
													}
													else
													{
														//valorTrans=(dto.getDetallesFactura().get(x).getValorTotal()+"").replaceAll("\\.", "0");
														valorTrans=dto.getDetallesFactura().get(x).getValorTotal()+"00";
													}
													
													if (Utilidades.convertirADouble(valorTrans)>0)
													{
														valorTrans=completarFormato(valorTrans, valorTrans.length(), 17, "nume", "0")+"+";
													//	logger.info("Valor Total -->"+valorTrans);
														
														if(dto.getDetallesFactura().get(x).getCodigoArticulo()!=ConstantesBD.codigoNuncaValido)
														{
															tmpDetalle = consultarCuentaParametrizable(con, dto.getConvenio().getCodigo(), ConstantesBD.tipoCuentaInventarios, dto.getInstitucion(), ConstantesBD.codigoNuncaValido, ConstantesBD.codigoNuncaValido, dto.getDetallesFactura().get(x).getCodigoArticulo(), ConstantesBD.codigoNuncaValido, Utilidades.obtenerEsquemasTarifarioServicioArticulo(con, dto.getSubCuenta()+"", Utilidades.obtenerCodigoContratoSubcuenta(dto.getSubCuenta()), dto.getDetallesFactura().get(x).getCodigoArticulo(), false,UtilidadFecha.getFechaActual(con), Cargos.obtenerCentroAtencionCargoSolicitud(con,dto.getDetallesFactura().get(x).getNumeroSolicitud())));
														//	logger.info("Cuenta Contable Sin PQ-->"+tmpDetalle);
															if (tmpDetalle.equals(""))
															{
																indInconsistencia = true;
																inconsistencias(dto.getFecha(), dto.getHora(), UtilidadTexto.formatearValores(dto.getConsecutivoFactura(),"0"), "Cuenta Inventarios", "No esta parametrizado",true);
															}
															else if(valido)
															{
																logger.info("888888888888888888888");
																// Capturo la Naturaleza de la cuenta contable
																//naturaleza = naturalezaTrans(con, tmpDetalle);//PARA CUANDO SE TOME LA NATURALEZA DE LA PARAMETRIZACION
																naturaleza=ConstantesBD.naturalezaCredito; 
																
																tmpDetalle= completarFormato(tmpDetalle,tmpDetalle.length() , 8, "alpha", " ");
																
																// SE REALIZA EL CONSECUTIVO POR REGISTRO
																consecutivoProcesoFacRc = consecutivoProcesoFacRc + 1;
																consecutivo = consecutivoProcesoFacRc+"";
																consecutivo = completarFormato(consecutivo, consecutivo.length(), 9, "nume",  "0");
															//	logger.info("Campo 1 ->"+consecutivo);
															    // FIN DEL CONSECUTIVO POR REGISTRO
																
															//	logger.info("---------NATURALEZA inv2----->"+naturaleza+"-\n");
																
																if(dto.getEstadoFacturacion().getCodigo() == ConstantesBD.codigoEstadoFacturacionAnulada)
																	valorTrans=completarFormato(aux, aux.length(), 17, "nume", "0")+"+";
																
																// SUMATORIA DEBIDOS Y CREDITOS
																temporalValor= dto.getDetallesFactura().get(x).getValorTotal();
																if(naturaleza.equals(ConstantesBD.naturalezaDebito))
																	sumDebitos = sumDebitos+temporalValor;
																if(naturaleza.equals(ConstantesBD.naturalezaCredito))
																	sumCreditos = sumCreditos+temporalValor;
																
																
																total = consecutivo+registrosFactura1+idConvenio+intermedio6a18+tmpDetalle+idConvenio+registrosFactura2+naturaleza+valorTrans+registrosFactura3+campo32+registrosFactura4+vencimiento+registrosFactura5+campo89+registrosFactura6+campo92+campo93+campo94+campo95+campo96+registrosFactura7+"\n";
																buffer.write(total);
															}
														}
													}//FIN VALOR DIFERENTE DE CERO
											}											
											break;
											
											
										case ConstantesBD.tipoCuentaPool: //CUENTA ASOCIADA A POOL
											
											campo32 = completarFormato(aux,aux.length(), 8, "alpha"," ");
											
											for(int x=0;x<dto.getDetallesFactura().size();x++)
											{
												
												if (dto.getDetallesFactura().get(x).getCodigoTipoSolicitud() != ConstantesBD.codigoTipoSolicitudCargosDirectosArticulos && dto.getDetallesFactura().get(x).getCodigoTipoSolicitud() != ConstantesBD.codigoTipoSolicitudMedicamentos && dto.getDetallesFactura().get(x).getCodigoTipoSolicitud() != ConstantesBD.codigoTipoSolicitudCirugia && dto.getDetallesFactura().get(x).getPool()!= ConstantesBD.codigoNuncaValido)
												{	
													
													// Valor Honorarios Medicos
													valorTrans = UtilidadTexto.formatearValores(dto.getDetallesFactura().get(x).getValorMedico(),"#.0")+"";
													
													if(valorTrans.indexOf(".")>0)
													{
														split=valorTrans.split("\\.");
														
														if(Utilidades.convertirAEntero(split[1]) > 9)
															valorTrans=split[0]+split[1].substring(0, 2);
														else
															valorTrans=split[0]+split[1].substring(0, 1)+"0";
													}
													else
													{
														//valorTrans=(dto.getDetallesFactura().get(x).getValorMedico()+"").replaceAll("\\.", "0");
														valorTrans=dto.getDetallesFactura().get(x).getValorMedico()+"00";
													}
													
													
													if (Utilidades.convertirADouble(valorTrans)>0)
													{
														valorTrans=completarFormato(valorTrans, valorTrans.length(), 17, "nume", "0")+"+";
													//	logger.info("VALOR POOL --->"+valorTrans);
														if (dto.getDetallesFactura().get(x).getCodigoTipoSolicitud() == ConstantesBD.codigoTipoSolicitudPaquetes)
														{
															for (int y=0; y<dto.getDetallesFactura().get(x).getPaquetizacionDetalleFactura().size();y++)
															{
																ccontable = consultarCuentaParametrizable(con, dto.getConvenio().getCodigo(), ConstantesBD.tipoCuentaPool, dto.getInstitucion(), dto.getDetallesFactura().get(x).getPaquetizacionDetalleFactura().get(y).getServicio().getCodigo(), ConstantesBD.codigoNuncaValido, dto.getDetallesFactura().get(x).getPaquetizacionDetalleFactura().get(y).getArticulo().getCodigo(), dto.getDetallesFactura().get(x).getPool(), Utilidades.obtenerEsquemasTarifarioServicioArticulo(con, dto.getSubCuenta()+"", Utilidades.obtenerCodigoContratoSubcuenta(dto.getSubCuenta()), dto.getDetallesFactura().get(x).getCodigoServicio(), true,UtilidadFecha.getFechaActual(con), Cargos.obtenerCentroAtencionCargoSolicitud(con,dto.getDetallesFactura().get(x).getNumeroSolicitud())));
																
																if (ccontable.equals(""))
																{
																	indInconsistencia = true;
																	inconsistencias(dto.getFecha(), dto.getHora(), UtilidadTexto.formatearValores(dto.getConsecutivoFactura(),"0"), "Cuenta Pool", "No esta parametrizado",true);
																}
																else if(valido)
																{
																	logger.info("999999999999999999");
																	//	Capturo la Naturaleza de la cuenta contable
																	//naturaleza = naturalezaTrans(con, ccontable);//PARA CUANDO SE TOME LA NATURALEZA DE LA PARAMETRIZACION
																	naturaleza=ConstantesBD.naturalezaCredito; 
																	
																	ccontable= completarFormato(ccontable,ccontable.length() , 8, "alpha", " ");
																//	logger.info("Campo 17  cuenta pool->"+ccontable);
																	
																	// SE REALIZA EL CONSECUTIVO POR REGISTRO
																	consecutivoProcesoFacRc = consecutivoProcesoFacRc + 1;
																	consecutivo = consecutivoProcesoFacRc+"";
																	consecutivo = completarFormato(consecutivo, consecutivo.length(), 9, "nume",  "0");
																//	logger.info("Campo 1 ->"+consecutivo);
																    // FIN DEL CONSECUTIVO POR REGISTRO
																	
																//	logger.info("---------NATURALEZA pol1----->"+naturaleza+"-\n");
																	
																	if(dto.getEstadoFacturacion().getCodigo() == ConstantesBD.codigoEstadoFacturacionAnulada)
																		valorTrans=completarFormato(aux, aux.length(), 17, "nume", "0")+"+";
																	
																	// SUMATORIA DEBIDOS Y CREDITOS
																	temporalValor= dto.getDetallesFactura().get(x).getValorMedico();
																	if(naturaleza.equals(ConstantesBD.naturalezaDebito))
																		sumDebitos = sumDebitos+temporalValor;
																	if(naturaleza.equals(ConstantesBD.naturalezaCredito))
																		sumCreditos = sumCreditos+temporalValor;
																	
																	total = consecutivo+registrosFactura1+idConvenio+intermedio6a18+ccontable+idConvenio+registrosFactura2+naturaleza+valorTrans+registrosFactura3+campo32+registrosFactura4+vencimiento+registrosFactura5+campo89+registrosFactura6+campo92+campo93+campo94+campo95+campo96+registrosFactura7+"\n";
																	buffer.write(total);
																}
															}	
														}
														else
														{
															
															tmpDetalle = consultarCuentaParametrizable(con, dto.getConvenio().getCodigo(), ConstantesBD.tipoCuentaPool, dto.getInstitucion(), ConstantesBD.codigoNuncaValido, ConstantesBD.codigoNuncaValido, ConstantesBD.codigoNuncaValido, dto.getDetallesFactura().get(x).getPool(), Utilidades.obtenerEsquemasTarifarioServicioArticulo(con, dto.getSubCuenta()+"", Utilidades.obtenerCodigoContratoSubcuenta(dto.getSubCuenta()), dto.getDetallesFactura().get(x).getCodigoServicio(), true,UtilidadFecha.getFechaActual(con), Cargos.obtenerCentroAtencionCargoSolicitud(con,dto.getDetallesFactura().get(x).getNumeroSolicitud())));
														//	logger.info("CUENTA POOL--->"+tmpDetalle);
															if (tmpDetalle.equals(""))
															{
																indInconsistencia = true;
																inconsistencias(dto.getFecha(), dto.getHora(), UtilidadTexto.formatearValores(dto.getConsecutivoFactura(),"0"), "Cuenta Pool", "No esta parametrizado",true);
															}
															else if(valido)
															{
																logger.info("100000000");
														//		logger.info("DETALLE TEMPORAL ->"+tmpDetalle);
																//	Capturo la Naturaleza de la cuenta contable
																//naturaleza = naturalezaTrans(con, tmpDetalle);//PARA CUANDO SE TOME LA NATURALEZA DE LA PARAMETRIZACION
																naturaleza=ConstantesBD.naturalezaCredito; 
																
																tmpDetalle= completarFormato(tmpDetalle,tmpDetalle.length() , 8, "alpha", " ");
																
																// SE REALIZA EL CONSECUTIVO POR REGISTRO
																consecutivoProcesoFacRc = consecutivoProcesoFacRc + 1;
																consecutivo = consecutivoProcesoFacRc+"";
																consecutivo = completarFormato(consecutivo, consecutivo.length(), 9, "nume",  "0");
														//		logger.info("Campo 1 ->"+consecutivo);
															    // FIN DEL CONSECUTIVO POR REGISTRO
																
														//		logger.info("---------NATURALEZA pol2----->"+naturaleza+"-\n");
																
																if(dto.getEstadoFacturacion().getCodigo() == ConstantesBD.codigoEstadoFacturacionAnulada)
																	valorTrans=completarFormato(aux, aux.length(), 17, "nume", "0")+"+";
																
																// SUMATORIA DEBIDOS Y CREDITOS
																temporalValor= dto.getDetallesFactura().get(x).getValorMedico();
																if(naturaleza.equals(ConstantesBD.naturalezaDebito))
																	sumDebitos = sumDebitos+temporalValor;
																if(naturaleza.equals(ConstantesBD.naturalezaCredito))
																	sumCreditos = sumCreditos+temporalValor;
																
																
																total = consecutivo+registrosFactura1+idConvenio+intermedio6a18+tmpDetalle+idConvenio+registrosFactura2+naturaleza+valorTrans+registrosFactura3+campo32+registrosFactura4+vencimiento+registrosFactura5+campo89+registrosFactura6+campo92+campo93+campo94+campo95+campo96+registrosFactura7+"\n";
																buffer.write(total);
															}	
															
														}
															
													}	
												
												}	
												
											}
											break;
											
											
											case ConstantesBD.tipoCuentaIngresoPool: // CUENTA INGRESO POOL
												
												campo32 = completarFormato(aux,aux.length(), 8, "alpha"," ");
												
												for(int x=0;x<dto.getDetallesFactura().size();x++)
												{
													
													if (dto.getDetallesFactura().get(x).getCodigoTipoSolicitud() != ConstantesBD.codigoTipoSolicitudCargosDirectosArticulos && dto.getDetallesFactura().get(x).getCodigoTipoSolicitud() != ConstantesBD.codigoTipoSolicitudMedicamentos && dto.getDetallesFactura().get(x).getCodigoTipoSolicitud() != ConstantesBD.codigoTipoSolicitudCirugia && dto.getDetallesFactura().get(x).getPool()!= ConstantesBD.codigoNuncaValido )
													{
														
														valorTrans = UtilidadTexto.formatearValores(dto.getDetallesFactura().get(x).getValorPool(),"#.0")+"";
														
														if(valorTrans.indexOf(".")>0)
														{
															split=valorTrans.split("\\.");
															
															if(Utilidades.convertirAEntero(split[1]) > 9)
																valorTrans=split[0]+split[1].substring(0, 2);
															else
																valorTrans=split[0]+split[1].substring(0, 1)+"0";
														}
														else
														{
															//valorTrans=(dto.getDetallesFactura().get(x).getValorPool()+"").replaceAll("\\.", "0");
															valorTrans=dto.getDetallesFactura().get(x).getValorPool()+"00";
														}
														
														if (Utilidades.convertirADouble(valorTrans)>0) // SOLO ESCRIBO EN EL PLANO REGISTROS QUE CONTENGAN VALOR, EL RESTO LOS OMITO
														{
															
															valorTrans=completarFormato(valorTrans, valorTrans.length(), 17, "nume", "0")+"+";
															if (dto.getDetallesFactura().get(x).getCodigoTipoSolicitud() == ConstantesBD.codigoTipoSolicitudPaquetes)
															{
																for (int y=0; y<dto.getDetallesFactura().get(x).getPaquetizacionDetalleFactura().size();y++)
																{
																	ccontable = consultarCuentaParametrizable(con, dto.getConvenio().getCodigo(), ConstantesBD.tipoCuentaIngresoPool, dto.getInstitucion(), dto.getDetallesFactura().get(x).getPaquetizacionDetalleFactura().get(y).getServicio().getCodigo(), ConstantesBD.codigoNuncaValido, dto.getDetallesFactura().get(x).getPaquetizacionDetalleFactura().get(y).getArticulo().getCodigo(), dto.getDetallesFactura().get(x).getPool(), Utilidades.obtenerEsquemasTarifarioServicioArticulo(con, 
																			dto.getSubCuenta()+"", Utilidades.obtenerCodigoContratoSubcuenta( dto.getSubCuenta()), dto.getDetallesFactura().get(x).getCodigoServicio(),true,UtilidadFecha.getFechaActual(con), Cargos.obtenerCentroAtencionCargoSolicitud(con,dto.getDetallesFactura().get(x).getNumeroSolicitud())));
															//		logger.info("CUENTA INGRESO POOL ->"+ccontable);
																	if (ccontable.equals(""))
																	{
																		indInconsistencia = true;
																		inconsistencias(dto.getFecha(), dto.getHora(), UtilidadTexto.formatearValores(dto.getConsecutivoFactura(),"0"), "Cuenta Ingreso Pool", "No esta parametrizado",true);
																	}
																	else if(valido)
																	{
																		logger.info("11");
																		//	 Capturo la Naturaleza de la cuenta contable
																		//naturaleza = naturalezaTrans(con, ccontable);//PARA CUANDO SE TOME LA NATURALEZA DE LA PARAMETRIZACION
																		naturaleza=ConstantesBD.naturalezaCredito; 
																		
																		ccontable= completarFormato(ccontable,ccontable.length() , 8, "alpha", " ");
																//		logger.info("Campo 17  cuenta ingreso pool->"+tmpDetalle);
																		
																		//	SE REALIZA EL CONSECUTIVO POR REGISTRO
																		consecutivoProcesoFacRc = consecutivoProcesoFacRc + 1;
																		consecutivo = consecutivoProcesoFacRc+"";
																		consecutivo = completarFormato(consecutivo, consecutivo.length(), 9, "nume",  "0");
																//		logger.info("Campo 1 ->"+consecutivo);
																	    // FIN DEL CONSECUTIVO POR REGISTRO
																		
																//		logger.info("---------NATURALEZA ipo1----->"+naturaleza+"-\n");
																		
																		if(dto.getEstadoFacturacion().getCodigo() == ConstantesBD.codigoEstadoFacturacionAnulada)
																			valorTrans=completarFormato(aux, aux.length(), 17, "nume", "0")+"+";
																		
																		//SUMATORIA DEBIDOS Y CREDITOS
																		temporalValor= dto.getDetallesFactura().get(x).getValorPool();
																		if(naturaleza.equals(ConstantesBD.naturalezaDebito))
																			sumDebitos = sumDebitos+temporalValor;
																		if(naturaleza.equals(ConstantesBD.naturalezaCredito))
																			sumCreditos = sumCreditos+temporalValor;
																		
																		total = consecutivo+registrosFactura1+idConvenio+intermedio6a18+ccontable+idConvenio+registrosFactura2+naturaleza+valorTrans+registrosFactura3+campo32+registrosFactura4+vencimiento+registrosFactura5+campo89+registrosFactura6+campo92+campo93+campo94+campo95+campo96+registrosFactura7+"\n";
																		buffer.write(total);
																	}	
																}	
															}
															else
															{
																tmpDetalle = consultarCuentaParametrizable(	con, 
																		ConstantesBD.codigoNuncaValido, 
																		ConstantesBD.tipoCuentaIngresoPool, 
																		dto.getInstitucion(), 
																		ConstantesBD.codigoNuncaValido, 
																		ConstantesBD.codigoNuncaValido, 
																		ConstantesBD.codigoNuncaValido, 
																		dto.getDetallesFactura().get(x).getPool(), 
																		Utilidades.obtenerEsquemasTarifarioServicioArticulo(con, dto.getSubCuenta()+"", 
																							Utilidades.obtenerCodigoContratoSubcuenta( dto.getSubCuenta()), dto.getDetallesFactura().get(x).getCodigoServicio(), 
																																		true,UtilidadFecha.getFechaActual(con), Cargos.obtenerCentroAtencionCargoSolicitud(con,dto.getDetallesFactura().get(x).getNumeroSolicitud())
																																	  ));
																
																if (tmpDetalle.equals(""))
																{
																	indInconsistencia = true;
																	inconsistencias(dto.getFecha(), dto.getHora(), UtilidadTexto.formatearValores(dto.getConsecutivoFactura(),"0"), "Cuenta Ingreso Pool", "No esta parametrizado",true);
																}
																else if(valido)
																{
																	logger.info("12");
																	//	Capturo la Naturaleza de la cuenta contable
																	//naturaleza = naturalezaTrans(con, ccontable);//PARA CUANDO SE TOME LA NATURALEZA DE LA PARAMETRIZACION
																	naturaleza=ConstantesBD.naturalezaCredito; 
																	
																	tmpDetalle= completarFormato(tmpDetalle,tmpDetalle.length() , 8, "alpha", " ");
																	
																	// SE REALIZA EL CONSECUTIVO POR REGISTRO
																	consecutivoProcesoFacRc = consecutivoProcesoFacRc + 1;
																	consecutivo = consecutivoProcesoFacRc+"";
																	consecutivo = completarFormato(consecutivo, consecutivo.length(), 9, "nume",  "0");
															//		logger.info("Campo 1 ->"+consecutivo);
																    // FIN DEL CONSECUTIVO POR REGISTRO
																	
															//		logger.info("---------NATURALEZA ipo2----->"+naturaleza+"-\n");
																	
																	if(dto.getEstadoFacturacion().getCodigo() == ConstantesBD.codigoEstadoFacturacionAnulada)
																		valorTrans=completarFormato(aux, aux.length(), 17, "nume", "0")+"+";
																	
																	//SUMATORIA DEBIDOS Y CREDITOS
																	temporalValor= dto.getDetallesFactura().get(x).getValorPool();
																	if(naturaleza.equals(ConstantesBD.naturalezaDebito))
																		sumDebitos = sumDebitos+temporalValor;
																	if(naturaleza.equals(ConstantesBD.naturalezaCredito))
																		sumCreditos = sumCreditos+temporalValor;
																	
																	total = consecutivo+registrosFactura1+idConvenio+intermedio6a18+tmpDetalle+idConvenio+registrosFactura2+naturaleza+valorTrans+registrosFactura3+campo32+registrosFactura4+vencimiento+registrosFactura5+campo89+registrosFactura6+campo92+campo93+campo94+campo95+campo96+registrosFactura7+"\n";
																	buffer.write(total);
																}
															}
															
														}
													}
												}
												break;
												
												
											case ConstantesBD.tipoCuentaAsociadaUtilidadPerdida: // CUENTA ASOCIADA UTILIDAD O PERDIDA
													// ESTO ES SOLO PARA PAQUETES	
												for (int x=0; x<dto.getDetallesFactura().size();x++)
												{
													if (dto.getDetallesFactura().get(x).getCodigoTipoSolicitud() == ConstantesBD.codigoTipoSolicitudPaquetes)
													{
														//CALCULO VALOR
													//	logger.info("VALOR TOTAL FACTURA ->"+dto.getDetallesFactura().get(x).getValorTotal());
													//	logger.info("VALOR CONSUMO PQ ---->"+valorConsumoPQ);
														totales = dto.getDetallesFactura().get(x).getValorTotal() -	valorConsumoPQ;
														
														if(totales !=0)
														{
															if(totales>0)
																ccontable=cuentaContablePaquetes(con, dto.getDetallesFactura().get(x).getNumeroSolicitud(), true);
															else
																ccontable=cuentaContablePaquetes(con, dto.getDetallesFactura().get(x).getNumeroSolicitud(), false);
															
														//	logger.info("CUENTA CONTABLE DEL PAQUETE -->"+ccontable);
														//	logger.info("VALOR DEL PAQUETE ------------>"+totales);
															
															if (ccontable.equals(""))
															{
																indInconsistencia = true;
																inconsistencias(dto.getFecha(), dto.getHora(), UtilidadTexto.formatearValores(dto.getConsecutivoFactura(),"0"), "Utilidad o Perdida", "No esta parametrizado la Cuenta del Paquete",true);
															}
															else if(valido)
															{
																logger.info("13");
																//Capturo la Naturaleza de la cuenta contable
																naturaleza = naturalezaTrans(con, ccontable);
																
																ccontable= completarFormato(ccontable, ccontable.length() , 8, "alpha", " ");
																campo32 = completarFormato(aux,aux.length(), 8, "alpha"," ");
																
																//VALOR DE LA UTILIDAD
																valorTrans = UtilidadTexto.formatearValores(Math.abs(totales),"#.0")+"";
																
																if(valorTrans.indexOf(".")>0)
																{
																	split=valorTrans.split("\\.");
																	
																	if(Utilidades.convertirAEntero(split[1]) > 9)
																		valorTrans=split[0]+split[1].substring(0, 2);
																	else
																		valorTrans=split[0]+split[1].substring(0, 1)+"0";
																}
																else
																{
																	//valorTrans=(Math.abs(totales)+"").replaceAll("\\.", "0");
																	valorTrans=+Math.abs(totales)+"00";
																}
																
																
																valorTrans=completarFormato(valorTrans, valorTrans.length(), 17, "nume", "0")+"+";
															//	logger.info("¿¿¿¿¿¿¿¿¿->"+valorTrans);
																
																if(dto.getEstadoFacturacion().getCodigo() == ConstantesBD.codigoEstadoFacturacionAnulada)
																	valorTrans=completarFormato(aux, aux.length(), 17, "nume", "0")+"+";
																
																// SUMATORIA DEBIDOS Y CREDITOS
																temporalValor= dto.getDetallesFactura().get(x).getValorTotal() - valorConsumoPQ;
																temporalValor=Math.abs(temporalValor);
																if(naturaleza.equals(ConstantesBD.naturalezaDebito))
																	sumDebitos = sumDebitos+temporalValor;
																if(naturaleza.equals(ConstantesBD.naturalezaCredito))
																	sumCreditos = sumCreditos+temporalValor;
																	
																
																// SE REALIZA EL CONSECUTIVO POR REGISTRO
																consecutivoProcesoFacRc = consecutivoProcesoFacRc + 1;
																consecutivo = consecutivoProcesoFacRc+"";
																consecutivo = completarFormato(consecutivo, consecutivo.length(), 9, "nume",  "0");
															//	logger.info("Campo 1 ->"+consecutivo);
															    // FIN DEL CONSECUTIVO POR REGISTRO
																
															//	logger.info("---------Naturaleza Utilidad----->"+naturaleza+"<-\n");
																
																total = consecutivo+registrosFactura1+idConvenio+intermedio6a18+ccontable+idConvenio+registrosFactura2+naturaleza+valorTrans+registrosFactura3+campo32+registrosFactura4+vencimiento+registrosFactura5+campo89+registrosFactura6+campo92+campo93+campo94+campo95+campo96+registrosFactura7+"\n";
																buffer.write(total);
															}
														}
													}
												}
												break; 
										}
									}
									//*********************************************************************************************************
									//VERIFICO CREDITOS CON DEBTOS SEAN IGUALES
									
									logger.info("............................::::::::::::::::::::::::::::::::::::::................");
									logger.info("FACTURA --->>>>>>>>>>"+UtilidadTexto.formatearValores(dto.getConsecutivoFactura(),"0"));
									logger.info("SUMATORIA DEBITOS -->"+sumDebitos+"<-");
									logger.info("SUMATORIA CREDITO -->"+sumCreditos+"<-");
									double debitosMenosCreditos = sumDebitos-sumCreditos;
									logger.info("TOTAL -------------->"+debitosMenosCreditos+"<-");
									logger.info("............................::::::::::::::::::::::::::::::::::::::................");
									
									if (!valido|| debitosMenosCreditos!=0)
									{
										indInconsistencia = true;
										inconsistencias(dto.getFecha(), dto.getHora(), UtilidadTexto.formatearValores(dto.getConsecutivoFactura(),"0"), "DEBITOS y CREDITOS", "Las Cuentas no cuadran, verifique las cuentas",true);
									}
									
									//*********************************************************************************************************
									
									//	Evalua que no hubieran existido inconsistencias
									if(!indInconsistencia)
									{
										// Se Marca la Factura como Contabilizada
										if(!marcaFactura(con, dto.getCodigo()))
										{
											this.transaccion = false;
											errores.add("descripcion", new ActionMessage("errors.problemasGenericos","No se realizó la Marca a la Factura"));
											return errores;
										}
										contadorRegistros++;			
									}
									
									
									logger.info("inconsistencia: "+indInconsistencia);		
								} 
	
							}
							logger.info("Numero Contador=> "+contadorRegistros);
							if (!esTodo)
							{
								buffer.close();
							}
							
							
							if(this.inconsistenciasMap.containsKey("numRegistros") && 
									!this.inconsistenciasMap.get("numRegistros").equals("0"))
							{
								InconsistenciasArray.add(this.inconsistenciasMap);					
							}				
						}
						
					}catch(FileNotFoundException e) 
					{
						errores.add("descripcion",new ActionMessage("errors.invalid","Error al Cargar el Archivo "+nombre));		
						return errores;
					}
					
					
				}catch(IOException e)
				{
					e.printStackTrace();					
					logger.error("Error en los streams del archivo: "+e);
					return null;
				}	
			}
			else
			{
				logger.info("no es posible generar el archivo validaciones existe, sobreescribir");		
			}
			
		
		return errores;
		
		
	}
	



	private HashMap nombresPacienteFac(Connection con, int codigoPaciente) 
	{
		return getInterfazSistemaUnoDao().nombresPacienteFac(con, codigoPaciente);
	}



	private String acronimoTipoConvenio(Connection con, int codigo) 
	{
		return getInterfazSistemaUnoDao().acronimoTipoConvenio(con,codigo);
	}



	private boolean debitosCreditosIguales(Connection con, DtoFactura dto) throws IPSException 
	{

		String ccontable="";
		String naturaleza = "";
		String valorTrans ="";
		
		double sumDebitosTemp=0;
		double sumCreditosTemp=0;
		double valorConsumoPQTemp=0;
		double temporalValor=0;
		boolean respuesta=false;
		for (int cuenta =1 ; cuenta<11 ;cuenta++)
		{
			switch (cuenta) {
			
			case ConstantesBD.tipoCuentaConvenio:  //CUENTA ASOCIADA AL CONVENIO
				ccontable = consultarCuentaParametrizable(con, dto.getConvenio().getCodigo(), ConstantesBD.tipoCuentaConvenio, dto.getInstitucion(), ConstantesBD.codigoNuncaValido, ConstantesBD.codigoNuncaValido, ConstantesBD.codigoNuncaValido, ConstantesBD.codigoNuncaValido, ConstantesBD.codigoNuncaValido);
				
				if (ccontable.equals(""))
				{
					return false;
				}
				else
				{
					//Capturo la Naturaleza de la cuenta contable
					//naturaleza = naturalezaTrans(con, ccontable);//PARA CUANDO SE TOME LA NATURALEZA DE LA PARAMETRIZACION					
					naturaleza=ConstantesBD.naturalezaDebito; 
					
					if(dto.getValorConvenio() > 0)
					{
						valorTrans=(dto.getValorConvenio()+"").replaceAll("\\.", "0");
						
						if (Utilidades.convertirADouble(valorTrans)>0)
						{
							// SUMATORIA DEBIDOS Y CREDITOS
							if(naturaleza.equals(ConstantesBD.naturalezaDebito))
								sumDebitosTemp = sumDebitosTemp+dto.getValorConvenio();
							if(naturaleza.equals(ConstantesBD.naturalezaCredito))
								sumCreditosTemp = sumCreditosTemp+dto.getValorConvenio();
						}
					}
				}
			break;
			case ConstantesBD.tipoCuentaPaciente: //CUENTA ASOCIADA AL PACIENTE
				ccontable = consultarCuentaParametrizable(con, dto.getConvenio().getCodigo(), ConstantesBD.tipoCuentaPaciente, dto.getInstitucion(), ConstantesBD.codigoNuncaValido, ConstantesBD.codigoNuncaValido, ConstantesBD.codigoNuncaValido, ConstantesBD.codigoNuncaValido, ConstantesBD.codigoNuncaValido);
				
				if (ccontable.equals(""))
				{
					return false;
				}
				else
				{
					//Capturo la Naturaleza de la cuenta contable
					//naturaleza = naturalezaTrans(con, ccontable);//PARA CUANDO SE TOME LA NATURALEZA DE LA PARAMETRIZACION					
					naturaleza=ConstantesBD.naturalezaDebito; 
					if(dto.getValorNetoPaciente() > 0)
					{
						valorTrans=(dto.getValorNetoPaciente()+"").replaceAll("\\.", "0");
						if (Utilidades.convertirADouble(valorTrans)>0)
						{
							
							// SUMATORIA DEBIDOS Y CREDITOS
							if(naturaleza.equals(ConstantesBD.naturalezaDebito))
								sumDebitosTemp = sumDebitosTemp+dto.getValorNetoPaciente();
							if(naturaleza.equals(ConstantesBD.naturalezaCredito))
								sumCreditosTemp = sumCreditosTemp+dto.getValorNetoPaciente();
						}
					}
				}
				break;
				
				
			case ConstantesBD.tipoCuentaDevolucionPaciente:  //CUENTA ASOCIADA A DEVOLUCION PACIENTE
				ccontable = consultarCuentaParametrizable(con, dto.getConvenio().getCodigo(), ConstantesBD.tipoCuentaDevolucionPaciente, dto.getInstitucion(), ConstantesBD.codigoNuncaValido, ConstantesBD.codigoNuncaValido, ConstantesBD.codigoNuncaValido, ConstantesBD.codigoNuncaValido, ConstantesBD.codigoNuncaValido);

				if (ccontable.equals(""))
				{
					return false;
				}
				else
				{
					//Capturo la Naturaleza de la cuenta contable
					//naturaleza = naturalezaTrans(con, ccontable);//PARA CUANDO SE TOME LA NATURALEZA DE LA PARAMETRIZACION
					naturaleza=ConstantesBD.naturalezaCredito; 
					if(dto.getValorNetoPaciente() < 0)
					{	
						valorTrans=(dto.getValorNetoPaciente()+"").replaceAll("\\.", "0");
						
						if (Utilidades.convertirADouble(valorTrans)>0)
						{
							if(naturaleza.equals(ConstantesBD.naturalezaDebito))
								sumDebitosTemp = sumDebitosTemp+dto.getValorNetoPaciente();
							if(naturaleza.equals(ConstantesBD.naturalezaCredito))
								sumCreditosTemp = sumCreditosTemp+dto.getValorNetoPaciente();
						}
					}
				}
				break;
				
			
			case ConstantesBD.tipoCuentaDescuentosPaciente: // CUENTA ASOCIADA AL DESCUENTO PACIENTE
				ccontable = consultarCuentaParametrizable(con, dto.getConvenio().getCodigo(), ConstantesBD.tipoCuentaDescuentosPaciente, dto.getInstitucion(), ConstantesBD.codigoNuncaValido, ConstantesBD.codigoNuncaValido, ConstantesBD.codigoNuncaValido, ConstantesBD.codigoNuncaValido, ConstantesBD.codigoNuncaValido);
													
				if (ccontable.equals(""))
				{
					return false;
				}
				else
				{

					//Capturo la Naturaleza de la cuenta contable
					//naturaleza = naturalezaTrans(con, ccontable);//PARA CUANDO SE TOME LA NATURALEZA DE LA PARAMETRIZACION
					naturaleza=ConstantesBD.naturalezaDebito; 
					if(dto.getValorDescuentoPaciente() > 0)
					{	
						valorTrans=(dto.getValorDescuentoPaciente()+"").replaceAll("\\.", "0");
						if (Utilidades.convertirADouble(valorTrans)>0)
						{
							if(naturaleza.equals(ConstantesBD.naturalezaDebito))
								sumDebitosTemp = sumDebitosTemp+dto.getValorDescuentoPaciente();
							if(naturaleza.equals(ConstantesBD.naturalezaCredito))
								sumCreditosTemp = sumCreditosTemp+dto.getValorDescuentoPaciente();
						}
					}
				}
				break;
				
			
			case ConstantesBD.tipoCuentaAbonosPaciente:  //	CUENTA ASOCIADA A ABONOS PACIENTE
				ccontable = consultarCuentaParametrizable(con, dto.getConvenio().getCodigo(), ConstantesBD.tipoCuentaAbonosPaciente, dto.getInstitucion(), ConstantesBD.codigoNuncaValido, ConstantesBD.codigoNuncaValido, ConstantesBD.codigoNuncaValido, ConstantesBD.codigoNuncaValido, ConstantesBD.codigoNuncaValido);
				
				if (ccontable.equals(""))
				{
					return false;
				}
				else
				{
//					Capturo la Naturaleza de la cuenta contable
					//naturaleza = naturalezaTrans(con, ccontable);//PARA CUANDO SE TOME LA NATURALEZA DE LA PARAMETRIZACION
					naturaleza=ConstantesBD.naturalezaDebito; 
					if(dto.getValorAbonos() > 0)
					{	
						valorTrans=(dto.getValorAbonos()+"").replaceAll("\\.", "0");
						if (Utilidades.convertirADouble(valorTrans)>0)
						{
							if(naturaleza.equals(ConstantesBD.naturalezaDebito))
								sumDebitosTemp = sumDebitosTemp+dto.getValorAbonos();
							if(naturaleza.equals(ConstantesBD.naturalezaCredito))
								sumCreditosTemp = sumCreditosTemp+dto.getValorAbonos();
						}
					}
				}
				break;
			
				
			case ConstantesBD.tipoCuentaServicios: // CUENTA ASOCIADA A SERVICIOS
				

				for(int x=0; x<dto.getDetallesFactura().size(); x++)
				{	
					if (dto.getDetallesFactura().get(x).getCodigoTipoSolicitud() != ConstantesBD.codigoTipoSolicitudCargosDirectosArticulos && dto.getDetallesFactura().get(x).getCodigoTipoSolicitud() != ConstantesBD.codigoTipoSolicitudMedicamentos)
					{	
						if(dto.getDetallesFactura().get(x).getCodigoTipoSolicitud() == ConstantesBD.codigoTipoSolicitudPaquetes)
						{
							for(int y=0; y<dto.getDetallesFactura().get(x).getPaquetizacionDetalleFactura().size();y++)
							{
								if(dto.getDetallesFactura().get(x).getPaquetizacionDetalleFactura().get(y).getTipoSolicitud() == ConstantesBD.codigoTipoSolicitudCirugia)
									valorTrans=(dto.getDetallesFactura().get(x).getPaquetizacionDetalleFactura().get(y).getValorPool()+"").replaceAll("\\.", "0");
								else
									valorTrans=(dto.getDetallesFactura().get(x).getPaquetizacionDetalleFactura().get(y).getValorTotal()+"").replaceAll("\\.", "0");
								
								if (Utilidades.convertirADouble(valorTrans)>0)
								{
									double sumatoriaConsumo = dto.getDetallesFactura().get(x).getPaquetizacionDetalleFactura().get(y).getValorTotal();
									valorConsumoPQTemp=valorConsumoPQTemp+sumatoriaConsumo;
									//********CALCULO VALOR DEL CONSUMO DEL PAQUETE *********
									
									ccontable = consultarCuentaParametrizable(con, dto.getConvenio().getCodigo(), ConstantesBD.tipoCuentaServicios, dto.getInstitucion(), dto.getDetallesFactura().get(x).getPaquetizacionDetalleFactura().get(y).getServicio().getCodigo(), ConstantesBD.codigoNuncaValido, ConstantesBD.codigoNuncaValido, ConstantesBD.codigoNuncaValido, Utilidades.obtenerEsquemasTarifarioServicioArticulo(con, dto.getSubCuenta()+"", Utilidades.obtenerCodigoContratoSubcuenta(dto.getSubCuenta()), dto.getDetallesFactura().get(x).getPaquetizacionDetalleFactura().get(y).getServicio().getCodigo(), true,UtilidadFecha.getFechaActual(con), Cargos.obtenerCentroAtencionCargoSolicitud(con,dto.getDetallesFactura().get(x).getNumeroSolicitud())));
										
										if (ccontable.equals(""))
										{
											return false;
										}
										else
										{
												//	Capturo la Naturaleza de la cuenta contable
												//naturaleza = naturalezaTrans(con, ccontable);//PARA CUANDO SE TOME LA NATURALEZA DE LA PARAMETRIZACION
												naturaleza=ConstantesBD.naturalezaCredito; 

												if(naturaleza.equals(ConstantesBD.naturalezaDebito))
													sumDebitosTemp = sumDebitosTemp+dto.getDetallesFactura().get(x).getPaquetizacionDetalleFactura().get(y).getValorTotal();
												if(naturaleza.equals(ConstantesBD.naturalezaCredito))
													sumCreditosTemp = sumCreditosTemp+dto.getDetallesFactura().get(x).getPaquetizacionDetalleFactura().get(y).getValorTotal();
											
										}
								}// Fin registro con Valor
							}
						}
						else 
							if(dto.getDetallesFactura().get(x).getCodigoTipoSolicitud() == ConstantesBD.codigoTipoSolicitudCirugia)
							{
								for(int y=0; y<dto.getDetallesFactura().get(x).getAsociosDetalleFactura().size();y++)
								{
									// Valor Servicio Paquete
									valorTrans=(dto.getDetallesFactura().get(x).getValorTotal()+"").replaceAll("\\.", "0"); 
									
									if (Utilidades.convertirADouble(valorTrans)>0)
									{
										valorTrans=completarFormato(valorTrans, valorTrans.length(), 17, "nume", "0")+"+";
										if(dto.getDetallesFactura().get(y).getEsServicio())
										{
											ccontable = consultarCuentaParametrizable(con, dto.getConvenio().getCodigo(), ConstantesBD.tipoCuentaServicios, dto.getInstitucion(), dto.getDetallesFactura().get(x).getAsociosDetalleFactura().get(y).getCodigoServicioAsocio(), ConstantesBD.codigoNuncaValido, ConstantesBD.codigoNuncaValido, ConstantesBD.codigoNuncaValido, Utilidades.obtenerEsquemasTarifarioServicioArticulo(con, dto.getSubCuenta()+"", Utilidades.obtenerCodigoContratoSubcuenta(dto.getSubCuenta()), dto.getDetallesFactura().get(x).getCodigoServicio(), true,UtilidadFecha.getFechaActual(con), Cargos.obtenerCentroAtencionCargoSolicitud(con,dto.getDetallesFactura().get(x).getNumeroSolicitud())));
											
											if (ccontable.equals(""))
											{
												return false;
											}
											else
											{
												//	Capturo la Naturaleza de la cuenta contable
												//naturaleza = naturalezaTrans(con, ccontable);//PARA CUANDO SE TOME LA NATURALEZA DE LA PARAMETRIZACION
												naturaleza=ConstantesBD.naturalezaCredito; 
												
													if(naturaleza.equals(ConstantesBD.naturalezaDebito))
														sumDebitosTemp = sumDebitosTemp+Utilidades.convertirADouble(valorCuentaContable(con, dto.getDetallesFactura().get(x).getNumeroSolicitud(), ConstantesBD.codigoNuncaValido)+"");
													if(naturaleza.equals(ConstantesBD.naturalezaCredito))
														sumCreditosTemp = sumCreditosTemp+Utilidades.convertirADouble(valorCuentaContable(con, dto.getDetallesFactura().get(x).getNumeroSolicitud(), ConstantesBD.codigoNuncaValido)+"");
												
											}
										}
									}//Fin registro con Valor
								}
							}
							
						else
						{
							// Valor Servicio
							valorTrans=	(dto.getDetallesFactura().get(x).getValorTotal()+"").replaceAll("\\.", "0");
							
							if (Utilidades.convertirADouble(valorTrans)>0)
							{
								valorTrans=completarFormato(valorTrans, valorTrans.length(), 17, "nume", "0")+"+";
								String tmpDetalle = consultarCuentaParametrizable(con, dto.getConvenio().getCodigo(), ConstantesBD.tipoCuentaServicios, dto.getInstitucion(), dto.getDetallesFactura().get(x).getCodigoServicio(), ConstantesBD.codigoNuncaValido, ConstantesBD.codigoNuncaValido, ConstantesBD.codigoNuncaValido, Utilidades.obtenerEsquemasTarifarioServicioArticulo(con, dto.getSubCuenta()+"", Utilidades.obtenerCodigoContratoSubcuenta(dto.getSubCuenta()), dto.getDetallesFactura().get(x).getCodigoServicio(), true,UtilidadFecha.getFechaActual(con), Cargos.obtenerCentroAtencionCargoSolicitud(con,dto.getDetallesFactura().get(x).getNumeroSolicitud())));
								
								if (tmpDetalle.equals(""))
								{
									return false;
								}
								else
								{
									//	Capturo la Naturaleza de la cuenta contable
									//naturaleza = naturalezaTrans(con, tmpDetalle);//PARA CUANDO SE TOME LA NATURALEZA DE LA PARAMETRIZACION
									naturaleza=ConstantesBD.naturalezaCredito; 
									
									if(naturaleza.equals(ConstantesBD.naturalezaDebito))
										sumDebitosTemp = sumDebitosTemp+dto.getDetallesFactura().get(x).getValorTotal();
									if(naturaleza.equals(ConstantesBD.naturalezaCredito))
										sumCreditosTemp = sumCreditosTemp+dto.getDetallesFactura().get(x).getValorTotal();
									
								}
							}//FIN registro con valor
						}
					}
				}
				break;
				
				
			case ConstantesBD.tipoCuentaInventarios: // CUENTA ASOCIADA A INVENTARIOS
				for(int x=0;x<dto.getDetallesFactura().size();x++)
				{
					
						// Valor Inventarios
						valorTrans=(dto.getDetallesFactura().get(x).getValorTotal()+"").replaceAll("\\.", "0");
						
						if (Utilidades.convertirADouble(valorTrans)>0)
						{
							if(dto.getDetallesFactura().get(x).getCodigoArticulo()!=ConstantesBD.codigoNuncaValido)
							{
								String tmpDetalle = consultarCuentaParametrizable(con, dto.getConvenio().getCodigo(), ConstantesBD.tipoCuentaInventarios, dto.getInstitucion(), ConstantesBD.codigoNuncaValido, ConstantesBD.codigoNuncaValido, dto.getDetallesFactura().get(x).getCodigoArticulo(), ConstantesBD.codigoNuncaValido, Utilidades.obtenerEsquemasTarifarioServicioArticulo(con, dto.getSubCuenta()+"", Utilidades.obtenerCodigoContratoSubcuenta(dto.getSubCuenta()), dto.getDetallesFactura().get(x).getCodigoArticulo(), false,UtilidadFecha.getFechaActual(con),Cargos.obtenerCentroAtencionCargoSolicitud(con,dto.getDetallesFactura().get(x).getNumeroSolicitud())));
								//logger.info("Cuenta Contable Sin PQ-->"+tmpDetalle);
								if (tmpDetalle.equals(""))
								{
									return false;
								}
								else
								{
									// Capturo la Naturaleza de la cuenta contable
									//naturaleza = naturalezaTrans(con, tmpDetalle);//PARA CUANDO SE TOME LA NATURALEZA DE LA PARAMETRIZACION
									naturaleza=ConstantesBD.naturalezaCredito; 
									
									
									if(naturaleza.equals(ConstantesBD.naturalezaDebito))
										sumDebitosTemp = sumDebitosTemp+dto.getDetallesFactura().get(x).getValorTotal();
									if(naturaleza.equals(ConstantesBD.naturalezaCredito))
										sumCreditosTemp = sumCreditosTemp+dto.getDetallesFactura().get(x).getValorTotal();
									
								}
							}
						}//FIN VALOR DIFERENTE DE CERO
				}											
				break;
				
				
			case ConstantesBD.tipoCuentaPool: //CUENTA ASOCIADA A POOL
				for(int x=0;x<dto.getDetallesFactura().size();x++)
				{
					if (dto.getDetallesFactura().get(x).getCodigoTipoSolicitud() != ConstantesBD.codigoTipoSolicitudCargosDirectosArticulos && dto.getDetallesFactura().get(x).getCodigoTipoSolicitud() != ConstantesBD.codigoTipoSolicitudMedicamentos && dto.getDetallesFactura().get(x).getCodigoTipoSolicitud() != ConstantesBD.codigoTipoSolicitudCirugia && dto.getDetallesFactura().get(x).getPool()!= ConstantesBD.codigoNuncaValido)
					{	
						// Valor Honorarios Medicos
						valorTrans = (dto.getDetallesFactura().get(x).getValorMedico()+"").replaceAll("\\.", "0");
						if (Utilidades.convertirADouble(valorTrans)>0)
							{
							if (dto.getDetallesFactura().get(x).getCodigoTipoSolicitud() == ConstantesBD.codigoTipoSolicitudPaquetes)
							{
								for (int y=0; y<dto.getDetallesFactura().get(x).getPaquetizacionDetalleFactura().size();y++)
								{
									ccontable = consultarCuentaParametrizable(con, dto.getConvenio().getCodigo(), ConstantesBD.tipoCuentaPool, dto.getInstitucion(), dto.getDetallesFactura().get(x).getPaquetizacionDetalleFactura().get(y).getServicio().getCodigo(), ConstantesBD.codigoNuncaValido, dto.getDetallesFactura().get(x).getPaquetizacionDetalleFactura().get(y).getArticulo().getCodigo(), dto.getDetallesFactura().get(x).getPool(), Utilidades.obtenerEsquemasTarifarioServicioArticulo(con, dto.getSubCuenta()+"", Utilidades.obtenerCodigoContratoSubcuenta(dto.getSubCuenta()), dto.getDetallesFactura().get(x).getCodigoServicio(), true,UtilidadFecha.getFechaActual(con), Cargos.obtenerCentroAtencionCargoSolicitud(con,dto.getDetallesFactura().get(x).getNumeroSolicitud())));
									
									if (ccontable.equals(""))
									{
										return false;
									}
									else
									{
										
										//Capturo la Naturaleza de la cuenta contable
										//naturaleza = naturalezaTrans(con, ccontable);//PARA CUANDO SE TOME LA NATURALEZA DE LA PARAMETRIZACION
										naturaleza=ConstantesBD.naturalezaCredito; 
										
										// SUMATORIA DEBIDOS Y CREDITOS
										if(naturaleza.equals(ConstantesBD.naturalezaDebito))
											sumDebitosTemp = sumDebitosTemp+dto.getDetallesFactura().get(x).getValorMedico();
										if(naturaleza.equals(ConstantesBD.naturalezaCredito))
											sumCreditosTemp = sumCreditosTemp+dto.getDetallesFactura().get(x).getValorMedico();
									
									}
								}	
							}
							else
							{
								
								String tmpDetalle = consultarCuentaParametrizable(con, dto.getConvenio().getCodigo(), ConstantesBD.tipoCuentaPool, dto.getInstitucion(), ConstantesBD.codigoNuncaValido, ConstantesBD.codigoNuncaValido, ConstantesBD.codigoNuncaValido, dto.getDetallesFactura().get(x).getPool(), Utilidades.obtenerEsquemasTarifarioServicioArticulo(con, dto.getSubCuenta()+"", Utilidades.obtenerCodigoContratoSubcuenta(dto.getSubCuenta()), dto.getDetallesFactura().get(x).getCodigoServicio(), true,UtilidadFecha.getFechaActual(con), Cargos.obtenerCentroAtencionCargoSolicitud(con,dto.getDetallesFactura().get(x).getNumeroSolicitud())));
								if (tmpDetalle.equals(""))
								{
									return false;
								}
								else
								{
									
									
									//	Capturo la Naturaleza de la cuenta contable
									//naturaleza = naturalezaTrans(con, tmpDetalle);//PARA CUANDO SE TOME LA NATURALEZA DE LA PARAMETRIZACION
									naturaleza=ConstantesBD.naturalezaCredito; 

									
									// SUMATORIA DEBIDOS Y CREDITOS
									if(naturaleza.equals(ConstantesBD.naturalezaDebito))
										sumDebitosTemp = sumDebitosTemp+dto.getDetallesFactura().get(x).getValorMedico();
									if(naturaleza.equals(ConstantesBD.naturalezaCredito))
										sumCreditosTemp = sumCreditosTemp+dto.getDetallesFactura().get(x).getValorMedico();
								
								}	
								
							}
								
						}	
					
					}	
					
				}
				break;
				
				
				case ConstantesBD.tipoCuentaIngresoPool: // CUENTA INGRESO POOL
					
					for(int x=0;x<dto.getDetallesFactura().size();x++)
					{
						
						if (dto.getDetallesFactura().get(x).getCodigoTipoSolicitud() != ConstantesBD.codigoTipoSolicitudCargosDirectosArticulos && dto.getDetallesFactura().get(x).getCodigoTipoSolicitud() != ConstantesBD.codigoTipoSolicitudMedicamentos && dto.getDetallesFactura().get(x).getCodigoTipoSolicitud() != ConstantesBD.codigoTipoSolicitudCirugia && dto.getDetallesFactura().get(x).getPool()!= ConstantesBD.codigoNuncaValido )
						{
							
							valorTrans = (dto.getDetallesFactura().get(x).getValorPool()+"").replaceAll("\\.", "0")+"";
							
							if (Utilidades.convertirADouble(valorTrans)>0) // SOLO ESCRIBO EN EL PLANO REGISTROS QUE CONTENGAN VALOR, EL RESTO LOS OMITO
							{
								
								if (dto.getDetallesFactura().get(x).getCodigoTipoSolicitud() == ConstantesBD.codigoTipoSolicitudPaquetes)
								{
									for (int y=0; y<dto.getDetallesFactura().get(x).getPaquetizacionDetalleFactura().size();y++)
									{
										ccontable = consultarCuentaParametrizable(con, dto.getConvenio().getCodigo(), ConstantesBD.tipoCuentaIngresoPool, dto.getInstitucion(), dto.getDetallesFactura().get(x).getPaquetizacionDetalleFactura().get(y).getServicio().getCodigo(), ConstantesBD.codigoNuncaValido, dto.getDetallesFactura().get(x).getPaquetizacionDetalleFactura().get(y).getArticulo().getCodigo(), dto.getDetallesFactura().get(x).getPool(), Utilidades.obtenerEsquemasTarifarioServicioArticulo(con, 
												dto.getSubCuenta()+"", Utilidades.obtenerCodigoContratoSubcuenta( dto.getSubCuenta()), dto.getDetallesFactura().get(x).getCodigoServicio(),true,UtilidadFecha.getFechaActual(con), Cargos.obtenerCentroAtencionCargoSolicitud(con,dto.getDetallesFactura().get(x).getNumeroSolicitud())));
									//	logger.info("CUENTA INGRESO POOL ->"+ccontable);
										if (ccontable.equals(""))
										{
											return false;
										}
										else
										{
											//	 Capturo la Naturaleza de la cuenta contable
											//naturaleza = naturalezaTrans(con, ccontable);//PARA CUANDO SE TOME LA NATURALEZA DE LA PARAMETRIZACION
											naturaleza=ConstantesBD.naturalezaCredito; 
											
											
											//SUMATORIA DEBIDOS Y CREDITOS
											if(naturaleza.equals(ConstantesBD.naturalezaDebito))
												sumDebitosTemp = sumDebitosTemp+dto.getDetallesFactura().get(x).getValorPool();
											if(naturaleza.equals(ConstantesBD.naturalezaCredito))
												sumCreditosTemp = sumCreditosTemp+dto.getDetallesFactura().get(x).getValorPool();
											
										}	
									}	
								}
								else
								{
									String tmpDetalle = consultarCuentaParametrizable(	con, 
											ConstantesBD.codigoNuncaValido, 
											ConstantesBD.tipoCuentaIngresoPool, 
											dto.getInstitucion(), 
											ConstantesBD.codigoNuncaValido, 
											ConstantesBD.codigoNuncaValido, 
											ConstantesBD.codigoNuncaValido, 
											dto.getDetallesFactura().get(x).getPool(), 
											Utilidades.obtenerEsquemasTarifarioServicioArticulo(con, dto.getSubCuenta()+"", 
																Utilidades.obtenerCodigoContratoSubcuenta( dto.getSubCuenta()), dto.getDetallesFactura().get(x).getCodigoServicio(), 
																											true,UtilidadFecha.getFechaActual(con), Cargos.obtenerCentroAtencionCargoSolicitud(con,dto.getDetallesFactura().get(x).getNumeroSolicitud())
																										  ));
									
									if (tmpDetalle.equals(""))
									{
										return false;
									}
									else
									{
										//	Capturo la Naturaleza de la cuenta contable
										//naturaleza = naturalezaTrans(con, ccontable);//PARA CUANDO SE TOME LA NATURALEZA DE LA PARAMETRIZACION
										naturaleza=ConstantesBD.naturalezaCredito; 
										
										//SUMATORIA DEBIDOS Y CREDITOS
										if(naturaleza.equals(ConstantesBD.naturalezaDebito))
											sumDebitosTemp = sumDebitosTemp+dto.getDetallesFactura().get(x).getValorPool();
										if(naturaleza.equals(ConstantesBD.naturalezaCredito))
											sumCreditosTemp = sumCreditosTemp+dto.getDetallesFactura().get(x).getValorPool();
										
									}
								}
								
							}
						}
					}
					break;
					
					
				case ConstantesBD.tipoCuentaAsociadaUtilidadPerdida: // CUENTA ASOCIADA UTILIDAD O PERDIDA
						// ESTO ES SOLO PARA PAQUETES	
					for (int x=0; x<dto.getDetallesFactura().size();x++)
					{
						if (dto.getDetallesFactura().get(x).getCodigoTipoSolicitud() == ConstantesBD.codigoTipoSolicitudPaquetes)
						{
							//CALCULO VALOR
							double totales = dto.getDetallesFactura().get(x).getValorTotal() -	valorConsumoPQTemp;
							
							if(totales !=0)
							{
								if(totales>0)
									ccontable=cuentaContablePaquetes(con, dto.getDetallesFactura().get(x).getNumeroSolicitud(), true);
								else
									ccontable=cuentaContablePaquetes(con, dto.getDetallesFactura().get(x).getNumeroSolicitud(), false);
								
								if (ccontable.equals(""))
								{
									return false;
								}
								else
								{
									//Capturo la Naturaleza de la cuenta contable
									naturaleza = naturalezaTrans(con, ccontable);
									
									// SUMATORIA DEBIDOS Y CREDITOS
									if(naturaleza.equals(ConstantesBD.naturalezaDebito))
										sumDebitosTemp = sumDebitosTemp+Math.abs(dto.getDetallesFactura().get(x).getValorTotal() - valorConsumoPQTemp);
									if(naturaleza.equals(ConstantesBD.naturalezaCredito))
										sumCreditosTemp = sumCreditosTemp+Math.abs(dto.getDetallesFactura().get(x).getValorTotal() - valorConsumoPQTemp);
										
								}
							}
						}
					}
					break; 
			}
		}
		//*********************************************************************************************************
		//VERIFICO CREDITOS CON DEBTOS SEAN IGUALES
		
		logger.info("............................::::::::::::::::::::::::::::::::::::::................");
		logger.info("FACTURA --->>>>>>>>>>"+UtilidadTexto.formatearValores(dto.getConsecutivoFactura(),"0"));
		logger.info("SUMATORIA DEBITOS -->"+sumDebitosTemp+"<-");
		logger.info("SUMATORIA CREDITO -->"+sumCreditosTemp+"<-");
		double debitosMenosCreditos = sumDebitosTemp-sumCreditosTemp;
		logger.info("TOTAL -------------->"+debitosMenosCreditos+"<-");
		logger.info("............................::::::::::::::::::::::::::::::::::::::................");
		
		if(debitosMenosCreditos ==0)
			respuesta=true;
		else
			respuesta=false;
		
		return respuesta;
	
	}



	private String cuentaContablePaquetes(Connection con, int numeroSolicitud, boolean utilidad) 
	{
		
		return getInterfazSistemaUnoDao().cuentaContablePaquete(con, numeroSolicitud, utilidad);
	}



	private int diasVencimientoFactura(Connection con, String convenio) {
		
		return getInterfazSistemaUnoDao().diasVencimientoFactura(con, convenio);
	}



	private int tipoTercero(Connection con, int codigo) {
		
		return getInterfazSistemaUnoDao().tipoTercero(con, codigo);
	}



	private String digitoVerificadorTercero(Connection con, int codigo) {
		
		return getInterfazSistemaUnoDao().digitoVerificadorTercero(con, codigo);
	}



	private String naturalezaTrans(Connection con, String ccontable) {
		
		return getInterfazSistemaUnoDao().naturalezaTrans(con, ccontable);
	}



	private boolean marcaFactura(Connection con, int codigo) {
		
		return getInterfazSistemaUnoDao().marcaFactura(con, codigo);
	}



	private HashMap datoEmpresa(Connection con, int codigo) {
		
		return getInterfazSistemaUnoDao().datoEmpresa(con, codigo);
	}



	private Object tipoConvenio(Connection con, int codigo) 
	{
		
		return getInterfazSistemaUnoDao().tipoConvenio(con, codigo);
	}



	private Object nitConvenio(Connection con, int codigo) 
	{
		
		return getInterfazSistemaUnoDao().nitConvenio(con, codigo);
	}



	private Object centroCostoSolicitaServ(Connection con, int numeroSolicitud) 
	{
		
		return getInterfazSistemaUnoDao().centroCostoSolicitaServ(con, numeroSolicitud);
	}



/**
 * Para Movimientos en los que se asocia articulos y servicios, se debe enviar el centro de costo ejecuta y solicita respectivamente 
 * @param con
 * @param numeroSolicitud
 * @return 
 */
	private Object centroCostoSolicitaArti(Connection con, int numeroSolicitud) {
		
		return getInterfazSistemaUnoDao().centroCostoSolicitaArti(con, numeroSolicitud);
		
	}



	private Object valorCuentaContable(Connection con, int numeroSolicitud, int institucion) 
	{
		
		
		return getInterfazSistemaUnoDao().valorCuentaContable(con, numeroSolicitud, institucion);
	}



	/**
	 * Completa una cadena dada con ceros o espacios dependiendo de se tipo de dato
	 * @param String datp
	 * @param int maxTamano
	 * @param String tipo (alpha,nume,valor)
	 * @param valorRelleno
	 * */
	public String completarFormato(String dato,int tamano,int maxTamano,String tipo,String valorRelleno)
	{
		String aux ="";
		
		if(tipo == "alpha")
		{
			for(int i = tamano; i < maxTamano; i++)
				aux+=valorRelleno;
			
			return dato+aux;							
		}
		else if(tipo == "nume")
		{
			for(int i = tamano; i < maxTamano; i++)
				aux+=valorRelleno;
			
			return aux+dato;						
		}
		else if(tipo == "valor")
		{						
			for(int i = tamano; i < maxTamano; i++)
				aux+=valorRelleno;				
			
			return aux+dato;						
		}			
		
		return dato;		
	}	
	
	
	
	/**
	 * Método implementado para consultar la cuenta parametrizable 
	 * según el tipo y los parámetros enviados por el mapa paramCuentas
	 * @param con
	 * @param paramCuentas
	 * @return
	 */
	private String consultarCuentaParametrizable(Connection con,int codigoConvenio,int tipoCuenta,int institucion,int codigoServicio,int centroCosto,int codigoArticulo,int codigoPool,int esquemaTarifario)
	{
		HashMap paramCuentas = new HashMap();
		paramCuentas.put("codigoConvenio", codigoConvenio);
		paramCuentas.put("tipoCuenta", tipoCuenta);
		paramCuentas.put("institucion", institucion);
		paramCuentas.put("codigoServicio", codigoServicio);
		paramCuentas.put("centroCosto", centroCosto);
		paramCuentas.put("codigoArticulo", codigoArticulo);
		paramCuentas.put("codigoPool", codigoPool);
		paramCuentas.put("esquemaTarifario", esquemaTarifario);
		
		
		//ConstantesBD.codigoNuncaValido
		return getInterfazSistemaUnoDao().consultarCuentaParametrizable(con, paramCuentas);
	}


	
	
	/**
	 * Guarda la informacion de las incosistencias generadas durante la generacion del archivo Plano 
	 * @param numeroFactura
	 * @param codigoPersona
	 * @param numeroCuenta
	 * @param viaIngreso
	 * @param tipoRegistro
	 * @param nombreCampo
	 * @param observacion
	 * */
	public void inconsistencias(
			String fecha,
			String hora,
			String codigoFactura,
			String nombreCampo,
			String observacion,
			boolean tipo)
	{	
		if(this.getInconsistenciasMap(codigoFactura)==null)
		{
			
			int pos;
			if(this.getInconsistenciasMap("numRegistros")==null)
			{
				this.setInconsistenciasMap("numRegistros","0");
				pos = 0;
			}
			else
				//captura la secuencia donde se encuentra el mapa
				pos = Utilidades.convertirAEntero(this.getInconsistenciasMap("numRegistros").toString());			
			
			
			//Almacena la informacion			
			this.setInconsistenciasMap("fecha_"+pos, fecha);
			this.setInconsistenciasMap("hora_"+pos,hora);
			this.setInconsistenciasMap("codigoFactura_"+pos,codigoFactura);
			this.setInconsistenciasMap("tipo_"+pos,tipo);
		
			
			//Campos para la administracion del HashMap			
			this.setInconsistenciasMap(codigoFactura,pos+"");
			this.setInconsistenciasMap("numElementos_"+pos,"0");
			
						
			//Aumenta una posicion del mapa
			this.setInconsistenciasMap("numRegistros",pos+1);			
		}
		
		int posEnca = Utilidades.convertirAEntero(this.getInconsistenciasMap(codigoFactura).toString());
		int numElementos = Utilidades.convertirAEntero(this.getInconsistenciasMap("numElementos_"+posEnca).toString());
		
		//Almacena la informacion del detalle
		this.setInconsistenciasMap("nombreCampo_"+posEnca+"_"+numElementos,nombreCampo);
		this.setInconsistenciasMap("observaciones_"+posEnca+"_"+numElementos,observacion);
		
		
		this.setInconsistenciasMap("numElementos_"+posEnca,numElementos+1);
			
	}
	
	
	
	/**
	 * Genera los archivos Planos "Fisicos" de Inconsistencias
	 * @param string 
	 * @param HashMap nombreArchivosPlanosMap
	 * @param ArrayList inconsistenciasArray
	 * */
	public HashMap guardarArchivosInconsistencia(String path, String nombre,HashMap inconsistenciasMap)
	{
		int indexNombreArchivosPlanosMap = 0;
		int numElementos = 0;
		
		int numRegistros = Utilidades.convertirAEntero(inconsistenciasMap.get("numRegistros")+"",true);			
			
			
			if(numRegistros > 0)
			{
				try
				{			
				
					//Captura la posicion del archivo asociado
				
					String nombreArchivo = "Incon"+nombre+".txt";
					
					//Apertura del archivo 				
					File archivoIn= new File(path, nombreArchivo);					
					FileWriter streamIn=new FileWriter(archivoIn,false);
					BufferedWriter buffer=new BufferedWriter(streamIn);		
					String registro = "";				
					String registroDetalle = "";
					String tmp = "";
					
					//Recorre el HashMap tomando los encabezados
					for(int k = 0; k < numRegistros; k++)
					{
						registro = "";
						registroDetalle ="";
						
						if(inconsistenciasMap.get("tipo_"+k).equals(true)) // SI LA INCONSISTENCIA ES DE FACTURAS
						{
							registro+="\n\n";
							registro+="Fecha: "+inconsistenciasMap.get("fecha_"+k)+"\n";
							registro+="Hora: "+inconsistenciasMap.get("hora_"+k)+"\n";
							registro+="Nº Factura: "+inconsistenciasMap.get("codigoFactura_"+k)+"\n";
							
							registro+="Nombre del Campo 						Observaciones\n";
													
							//El numero de elementos del encabezado
							numElementos = Utilidades.convertirAEntero(inconsistenciasMap.get("numElementos_"+k).toString());					 
							
							logger.info("Numero de Elementos "+numElementos);
							
							for(int j = 0; j < numElementos; j++)
							{
								//logger.info("Mapa en posicion"+inconsistenciasMap("nombreCampo_"+k+j).toString());
								
								tmp =inconsistenciasMap.get("nombreCampo_"+k+"_"+j).toString();							
								tmp = this.completarFormato(tmp,tmp.length(),55, "alpha"," ");
								registroDetalle+=tmp+inconsistenciasMap.get("observaciones_"+k+"_"+j).toString()+"\n";
								tmp = "";
							}						
																
							buffer.write(registro+registroDetalle);
						}
						else
						{
							registro+="\n\n";
							registro+="Fecha: "+inconsistenciasMap.get("fecha_"+k)+"\n";
							registro+="Hora: "+inconsistenciasMap.get("hora_"+k)+"\n";
							registro+="Nº Recibo Caja: "+inconsistenciasMap.get("codigoFactura_"+k)+"\n";
							
							registro+="Nombre del Campo 						Observaciones\n";
													
							//El numero de elementos del encabezado
							numElementos = Utilidades.convertirAEntero(inconsistenciasMap.get("numElementos_"+k).toString());					 
							
							logger.info("Numero de Elementos "+numElementos);
							
							for(int j = 0; j < numElementos; j++)
							{
								//logger.info("Mapa en posicion"+inconsistenciasMap("nombreCampo_"+k+j).toString());
								
								tmp =inconsistenciasMap.get("nombreCampo_"+k+"_"+j).toString();							
								tmp = this.completarFormato(tmp,tmp.length(),55, "alpha"," ");
								registroDetalle+=tmp+inconsistenciasMap.get("observaciones_"+k+"_"+j).toString()+"\n";
								tmp = "";
							}						
																
							buffer.write(registro+registroDetalle);
						}
						
					}
					
					
					//Se indica que se genero el archivo de inconsistencias
					if(!registro.equals(""))
					{
						logger.info("Existen Inconsistencias");
						//	nombre.put("esGeneradoInconsitencia_"+indexNombreArchivosPlanosMap,ConstantesBD.acronimoSi);
					//	nombreArchivosMap.put("nombreArchivoIncon_"+indexNombreArchivosPlanosMap,nombreArchivo);					
						buffer.close();
					}
					else
					{
						archivoIn.delete();	
					}
					
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
			else
			{
				logger.info("No Hay Incosistencias");
			}
		
		return inconsistenciasMap ;
	}	
	
	/**
	 * Metodo para cargar solo el Archivo de Incosistencias......
	 * No es necesario cargar el plano debido a que es muy extenso y no es necesario conocer su información.
	 * @param directorio
	 * @param nombre
	 * @return
	 */
	public HashMap<String,Object> cargarArchivo(String directorio,String nombre)
	{
		//objeto usado para llenar el contenido del archivo
		HashMap contenido=new HashMap();
		String separador=System.getProperty("file.separator");
		String nombreArchivo = "Incon"+nombre+".txt";
		
		try
		{
			int contador=0;
			String cadena="";
			//******SE INICIALIZA ARCHIVO*************************
			
			
			File archivo=new File(directorio, nombreArchivo);			
			logger.info("Cargar Archivo"+archivo);
			FileReader stream=new FileReader(archivo); //se coloca false para el caso de que esté repetido
			BufferedReader buffer=new BufferedReader(stream);
			
			
			
			//********SE RECORRE LÍNEA POR LÍNEA**************
			cadena=buffer.readLine();
			logger.info("Cargar Archivo Buffer ->"+archivo);
			while(cadena!=null)
			{
				//se almacena cada línea en el hashmap
				contenido.put(contador+"",cadena);
				
				contador++;
				cadena=buffer.readLine();
			}
			contenido.put("numRegistros",contador+"");
			
			//***************CERRAR ARCHIVO****************************
			buffer.close();
		
			return contenido;
		}
		catch(FileNotFoundException e)
		{
			logger.info("Es posible que no existan inconsistencias");
			logger.info("No se pudo encontrar el archivo "+nombreArchivo+" al cargarlo: "+e);
			
			return null;
		}
		catch(IOException e)
		{
			logger.error("Error en los streams del archivo "+nombreArchivo+" al cargarlo: "+e);
			return null;
		}
	}
	
	

	public ArrayList<HashMap<String, Object>> getInconsistenciasArray() {
		return InconsistenciasArray;
	}



	public void setInconsistenciasArray(
			ArrayList<HashMap<String, Object>> inconsistenciasArray) {
		InconsistenciasArray = inconsistenciasArray;
	}



	public HashMap getInconsistenciasMap() {
		return inconsistenciasMap;
	}



	public void setInconsistenciasMap(HashMap inconsistenciasMap) {
		this.inconsistenciasMap = inconsistenciasMap;
	}
	
	
	
	/**
	 * @return the inconsistenciasMap
	 */
	public Object getInconsistenciasMap(String key) {
		return inconsistenciasMap.get(key);
	}


	/**
	 * @param inconsistenciasMap the inconsistenciasMap to set
	 */
	public void setInconsistenciasMap(String key, Object value) {
		this.inconsistenciasMap.put(key, value);
	}



	public boolean isTransaccion() {
		return transaccion;
	}



	public void setTransaccion(boolean transaccion) {
		this.transaccion = transaccion;
	}


	/**
	 * Metodo para Armar el plano de los Recibos de Caja por el Rango de fechas que el usuario eligio.
	 * @param con
	 * @param numRegistros
	 * @param recibosCaja
	 * @param nombre
	 * @param path 
	 * @param errores
	 */
	public ActionErrors armarPlanoRC(Connection con, int numRegistros, HashMap recibosCaja, String path, String nombre, ActionErrors errores, boolean esTodo) 
	{
		logger.info("Entro al metodo armar Plano rc");
		logger.info("Armar Plano numeroRegistros"+numRegistros);
		logger.info("Armar Plano nombre"+nombre);
		logger.info("Armar Plano ES TODO"+esTodo);
		
		String registrosFactura ="";
		
		String rc1 ="";
		String rc2 ="";
		String rc3 ="";
		String rc4 ="";
		String rc5 ="";
		
		String codigoConvenio ="";
		String tipoPago ="";
		
		String total ="";
		
		int posNombreArchivos = 0;
		int contadorRegistros = 0;
		String aux="";
		
		String [] split;
		String fecha="";
		String idPaciente ="";
		String idPaciente1 ="";
		String consecutivo ="";
		String centroAtencion ="";
		String naturalezaDebito ="";
		String naturalezaCredito ="";
		String tipoConcepto ="";
		String conceptoDetalle ="";
		String campoInterfaz46 ="";
		String campoInterfaz124 ="";
		
		
		
		double totales;
		boolean indInconsistencia = false;
		this.inconsistenciasMap = new HashMap();
		
//		Inicializa el Array list que posee los HashMap de Inconsistencias
		this.InconsistenciasArray = new ArrayList<HashMap<String,Object>>();
		
	
				
			// Nombre del Archivo a Crear
			nombre+=".txt";
			contadorRegistros = 0;
			
			if(!nombre.equals(""))
			
			{
				try
				{
					
					try
					{
						// Apertura del Archivo Plano Sistema Uno
						if(!esTodo)
						{	
							File archivo = new File(path,nombre);
							FileWriter streamUno = new FileWriter(archivo, false);
							this.buffer = new BufferedWriter(streamUno);
						}
						
						
						
							
							// Verificar que la posicion no sea Nula
							if(numRegistros == ConstantesBD.codigoNuncaValido)
							{
								errores.add("descripcion", new ActionMessage("errors.invalid","Error al Cargar el Nombre de Archivo"));
								return errores;
							}
						
						
							// Verificar que el Array no este vacio
							if(recibosCaja!=null)
							{
								// Inicializa el HashMap de inconsistencias del Archivo a Generar
								this.inconsistenciasMap = new HashMap();
								this.inconsistenciasMap.put("numRegistros", 0);
								this.inconsistenciasMap.put("archivoAsociado",posNombreArchivos);
								
								consecutivoProceso = UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_interfaz_sistema_uno");
								
								
								for(int j=0; j<numRegistros; j++)
								{
									indInconsistencia = false;
									rc1 = "";
									rc2 = "";
									rc3 = "";
									rc4 = "";
									idPaciente ="";
									idPaciente1 ="";
									consecutivo = "";
									centroAtencion ="";
									naturalezaDebito = "";
									naturalezaCredito = "";
									tipoPago ="";
									tipoPago ="";
									tipoConcepto ="";
									conceptoDetalle ="";
									campoInterfaz46="";
									campoInterfaz124="";
									
									
									
									// Validar que exista el Mapa
									if(j+"" != null)
									{
										// ************** SE INICIALIZA LA CAPTURA DE LOS CAMPOS DEL REGISTRO *****************
										logger.info("\n\n");
										logger.info("..............::::::   DATOS REGISTRO  ::::::......... "+j);	
										logger.info("");
										
										// CAMPO Nº1 CONSECUTIVO DE GRABACION
										
										// CAMPO Nº2 INSTITUCION
										
										//consecutivo = recibosCaja.get("institucion_"+j)+"";
										consecutivo="HH";
										consecutivo=completarFormato(consecutivo, consecutivo.length(), 2, "alpha", " ");
										rc1=consecutivo;
																				
										// CAMPO Nº3 CENTRO DE ATENCION
										
										centroAtencion = centroAtencionRc(con, recibosCaja.get("numero_recibo_caja_"+j)+"", recibosCaja.get("institucion_"+j)+"")+"";
										centroAtencion = completarFormato(centroAtencion, centroAtencion.length(), 3, "nume", "0");
										rc1+=centroAtencion;
										
										// CAMPO Nº4 TIPO DE MOVIMIENTO
										
										consecutivo=ConstantesBD.tipoMovimientoReciboCaja;
										consecutivo=completarFormato(consecutivo, consecutivo.length(), 2, "alpha", " ");
										rc1+=consecutivo;
										
										// CAMPO Nº5 NUMERO DE DOCUMENTO
										
										
										consecutivo=UtilidadTexto.formatearValores(recibosCaja.get("numero_recibo_caja_"+j)+"","0");
										consecutivo=completarFormato(consecutivo, consecutivo.length(), 6, "nume", "0");
										rc1+=consecutivo;
										
										// CAMPO Nº6 ID DEL PACIENTE
										
										idPaciente = idPacienteRc(con, recibosCaja.get("numero_recibo_caja_"+j)+"", recibosCaja.get("institucion_"+j)+"")+"";
										idPaciente=idPaciente.trim();
										
										if (idPaciente.length()>13)
										{
											idPaciente=idPaciente.substring(0, 13);
										}
										else
										{
											idPaciente = completarFormato(idPaciente, idPaciente.length(), 13, "alpha", " ");
										}
										
										rc1+=idPaciente; 
										
										// CAMPO Nº7 ENVIAR EN CERO
										
										rc1+=completarFormato(aux, aux.length(), 2, "alpha","0");
										
										// CAMPO Nº8 FECHA DE EMISION DEL DOCUMENTO
										
										split = UtilidadFecha.conversionFormatoFechaABD(recibosCaja.get("fecha_"+j)+"").split("-");
										fecha = split[0]+split[1]+split[2];
										rc1+=fecha;
										
										// CAMPO Nº9 PERIODO DE GENERACION DE LA INTERFAZ
										
										fecha = split[0]+split[1];
										rc1+=fecha;
										
										// CAMPO Nº10 ESPACIOS
										
										rc1+=completarFormato(aux,aux.length(), 8, "alpha"," ");
										
										
										// CAMPO Nº11 ESPACIOS
										
										rc1+=completarFormato(aux,aux.length(), 8, "alpha"," ");
										
										// CAMPO Nº 12 ESPACIOS
										
										rc1+=completarFormato(aux,aux.length(), 40, "alpha"," ");
										
										// CAMPO Nº 13 ESPACIOS
										
										rc1+=completarFormato(aux,aux.length(), 13, "alpha"," ");
										
										// CAMPO Nº 14 VALOR DEL DOCUMENTO
										
										//Para convertir en double en cadena normal 
										//consecutivo=UtilidadTexto.formatearValores(dto.getConsecutivoFactura(),"0");
										
										consecutivo = valorTotalRc(con, recibosCaja.get("numero_recibo_caja_"+j)+"", recibosCaja.get("institucion_"+j)+"")+"";
										
										if(consecutivo.contains("."))									
											consecutivo = consecutivo.replaceAll("\\.", "0");
										else
											consecutivo += "00" ;
										
										consecutivo = completarFormato(consecutivo,consecutivo.length(), 17, "nume","0")+"+";
										rc1+=consecutivo;
										
										// CAMPO Nº 15 INTERNO
										
										if(estadoRc(con, recibosCaja.get("numero_recibo_caja_"+j)+"",recibosCaja.get("institucion_"+j)+"")  == ConstantesBD.codigoEstadoReciboCajaAnulado)
											rc1+="X";
										else
											rc1+=" ";
										 
										rc1+=completarFormato(aux,aux.length(), 19, "alpha"," ");
										
										// CAMPO Nº 16 INTERNO
										
										rc1+=completarFormato(aux,aux.length(), 3, "alpha"," ");
										
										// CAMPO Nº 18 NUMERO ID DEL PACIENTE  = Nº6
										
										rc2=idPaciente;
										
										// CAMPO Nº 19 EN CEROS
										
										rc2+=completarFormato(aux,aux.length(), 2, "nume","0");
										
										// CAMPO Nº 20 CENTRO DE ATENCION
										
										rc2+=centroAtencion;
										
										// CAMPO Nº 21 NATURALEZA DE LA TRANSACCION
										
										
										//CAMPO Nº 22 VALOR DE LA TRANSACCION
										
										
										// CAMPO Nº 23 VALOR EN MONEDA EXTRANJERA
										
										rc3+=completarFormato(aux,aux.length(), 17, "nume","0")+"+";
										
										// CAMPO Nº 24 TASA DE CONVENSION
										
										rc3+=completarFormato(aux,aux.length(), 11, "nume","0")+"+";
										
										// CAMPO Nº 25 TASA DE CAMBIO
										
										rc3+=completarFormato(aux,aux.length(), 11, "nume","0")+"+";
										
										// CAMPO Nº 26 IVA BASE
										
										rc3+=completarFormato(aux,aux.length(), 17, "nume","0")+"+";
										
										// CAMPO Nº 27 TASA DE IMPUESTO
										
										rc3+=completarFormato(aux,aux.length(), 6, "nume","0");
										
										// CAMPO Nº 28 % DE IVA
										
										rc3+=completarFormato(aux,aux.length(), 6, "nume","0");
										
										// CAMPO Nº 29 EN CEROS
										
										rc3+=completarFormato(aux,aux.length(), 13, "nume","0")+"+";
										
										// CAMPO Nº 30 DETALLE DE LA TRANSACCION
										
										rc3+=completarFormato(aux,aux.length(), 40, "alpha"," ");
										
										// CAMPO Nº 31 DETALLE DE LA TRANSACCION
										
										rc3+=completarFormato(aux,aux.length(), 40, "alpha"," ");
										
										// CAMPO Nº 32 CENTRO DE COSTO QUE SOLICITA
										String centroCostoConceptoRc = centroCostoConceptoRc(con, recibosCaja.get("numero_recibo_caja_"+j)+"", recibosCaja.get("institucion_"+j)+"")+"";
										
										
										if(centroCostoConceptoRc.equals(""))
										{
											indInconsistencia=true;
											inconsistencias(recibosCaja.get("fecha_"+j)+"", recibosCaja.get("hora_"+j)+"", recibosCaja.get("numero_recibo_caja_"+j)+"", "Concepto Ingreso Tesoreria", "No tiene asociado un Centro de Costo", false);
										}
										else
										{
											if(centroCostoConceptoRc.length()>8)
											{
												indInconsistencia=true;
												inconsistencias(recibosCaja.get("fecha_"+j)+"", recibosCaja.get("hora_"+j)+"", recibosCaja.get("numero_recibo_caja_"+j)+"", "Identificacion Centro Costo", "Debe ser de maximo 8 caracteres", false);
											}
											else
											{
												centroCostoConceptoRc=completarFormato(centroCostoConceptoRc,centroCostoConceptoRc.length(), 8, "alpha"," ");
												rc3+=centroCostoConceptoRc; 
											}
										}
										
										
										
										// CAMPO Nº 33 CODIGO CONCEPTO DE PAGO	(DIFIERE PARA RECIBOS DE CAJA)
										
										consecutivo = conceptoPagoRc(con, recibosCaja.get("numero_recibo_caja_"+j)+"", recibosCaja.get("institucion_"+j)+"")+"";
										consecutivo = completarFormato(consecutivo, consecutivo.length(), 8, "alpha", " ");
										rc3+=consecutivo;
										
										// CAMPO Nº 34 DESCRIPCION CONCEPTO DE PAGO (DIFIERE PARA RECIBOS DE CAJA)
										
										consecutivo = descripConceptoPagoRc(con, recibosCaja.get("numero_recibo_caja_"+j)+"", recibosCaja.get("institucion_"+j)+"");
										if (consecutivo.length()>40)
										{
											consecutivo= consecutivo.substring(0, 40);
										}
										consecutivo = completarFormato(consecutivo, consecutivo.length(), 40, "alpha", " ");
										rc3+=consecutivo;
										
										// CAMPO Nº 35 EN BLANCO
										
										rc3+=completarFormato(aux,aux.length(), 10, "alpha"," ");
										
										// Campo que no existe en el documento y que hace falta implementar
										rc3+=completarFormato(aux,aux.length(), 40, "alpha"," ");
										
										// CAMPO Nº 36 NA
										
										rc3+=completarFormato(aux,aux.length(), 2, "alpha"," ");
										
										// CAMPO Nº 37 NA
										
										rc3+=completarFormato(aux,aux.length(), 6, "nume","0");
										
										// CAMPO Nº 38 NA
										
										rc3+=completarFormato(aux,aux.length(), 4, "alpha"," ");
										
										// CAMPO Nº 39 NA
										
										rc3+=completarFormato(aux,aux.length(), 12, "nume","0");
										
										// CAMPO Nº 40 NA
										
										rc3+=completarFormato(aux,aux.length(), 30, "alpha"," ");
										
										// CAMPO Nº 41 TIPO DOCUMENTO CRUCE
										
										rc3+=ConstantesBD.tipoMovimientoReciboCaja;
										
										// CAMPO Nº 42 NRO FACTURA
										
										consecutivo=recibosCaja.get("numero_recibo_caja_"+j)+"";
										consecutivo=completarFormato(consecutivo, consecutivo.length(), 6, "nume", "0");
										rc3+=consecutivo;
										
										// CAMPO Nº 43 NA
										
										rc3+=completarFormato(aux,aux.length(), 2, "nume","0");
										
										// CAMPO Nº 44 FECHA DE VENCIMIENTO
										
										split = UtilidadFecha.conversionFormatoFechaABD(recibosCaja.get("fecha_"+j)+"").split("-");
										fecha = split[0]+split[1]+split[2];
										
										rc3+=fecha;
										
										// CAMPO Nº 45 NUMERO ID USUARIO CAJA
										
										consecutivo = 1+"";
										consecutivo = completarFormato(consecutivo, consecutivo.length(), 13, "nume", "0");
										rc3+=consecutivo;
										
										// CAMPO Nº 46 CODIGO CONVENIO PACIENTE
										
										campoInterfaz46 = completarFormato(aux, aux.length(), 13, "alpha", " ");
										
										// CAMPO Nº 47 NA
										
										rc4=completarFormato(aux,aux.length(), 2, "nume","0");
										
										// CAMPO Nº 48 INDICATIVO DE ANTICIPO
										
										rc4+=completarFormato(aux,aux.length(), 1, "nume","0");
										
										// CAMPO Nº 49 REFERENCIA EL ANTICIPO
										
										rc4+=completarFormato(aux,aux.length(), 11, "alpha","0");
										
										// CAMPO Nº 50 DIAS DE GRACIA
										
										rc4+=completarFormato(aux,aux.length(), 3, "nume","0");
										
										// CAMPO Nº 51 NA
										
										rc4+=completarFormato(aux,aux.length(), 8, "nume","0");
										
										// CAMPO Nº 52 NA
										
										rc4+=completarFormato(aux,aux.length(), 13, "nume","0")+"+";
										
										// CAMPO Nº 53 NA
										
										rc4+=completarFormato(aux,aux.length(), 13, "nume","0")+"+";
										
										// CAMPO Nº 54 FECHA FACTURA (APLICA SOLO PARA SALDOS INICIALES)
										
										rc4+=completarFormato(aux,aux.length(), 8, "nume","0");
										
										// CAMPO Nº 55 INTERNO
										
										rc4+=completarFormato(aux,aux.length(), 1, "alpha"," ");
										
										// CAMPO Nº 56 INTERNO
										
										rc4+=completarFormato(aux,aux.length(), 11, "alpha"," ");
										
										// CAMPO Nº 57 NA
										
										rc4+=completarFormato(aux,aux.length(), 12, "alpha"," ");
										
										// CAMPO Nº 58 NA
										
										rc4+=completarFormato(aux,aux.length(), 2, "nume","0");
										
										// CAMPO Nº 59 NA
										
										rc4+=completarFormato(aux,aux.length(), 8, "nume","0");
										
										// CAMPO Nº 60 NA
										
										rc4+=completarFormato(aux,aux.length(), 8, "nume","0");
										
										// CAMPO Nº 61 NA
										
										rc4+=completarFormato(aux,aux.length(), 8, "alpha"," ");
										
										// CAMPO Nº 62 NA
										
										rc4+=completarFormato(aux,aux.length(), 13, "alpha"," ");
										
										// CAMPO Nº 63 NA
										
										rc4+=completarFormato(aux,aux.length(), 2, "nume","0");
										
										// CAMPO Nº 64 NA
										
										rc4+=completarFormato(aux,aux.length(), 3, "nume","0");
										
										// CAMPO Nº 65 NA
										
										rc4+=completarFormato(aux,aux.length(), 8, "alpha"," ");
										
										// CAMPO Nº 66 NA
										
										rc4+=completarFormato(aux,aux.length(), 10, "alpha"," ");
										
										// CAMPO Nº 67 NA
										
										rc4+=completarFormato(aux,aux.length(), 40, "alpha"," ");
										
										// CAMPO Nº 68 NA
										
										rc4+=completarFormato(aux,aux.length(), 8, "nume","0");
										
										// CAMPO Nº 69 NA
										
										rc4+=completarFormato(aux,aux.length(), 8, "nume","0");
										
										// CAMPO Nº 70 NA
										
										rc4+=completarFormato(aux,aux.length(), 20, "alpha"," ");
										
										// CAMPO Nº 71 CODIGO CAJA 										(DIFIERE PARA RECIBOS DE CAJA)
										
										consecutivo = codigoCajaRc(con, recibosCaja.get("numero_recibo_caja_"+j)+"", recibosCaja.get("institucion_"+j)+"")+"";
										consecutivo = completarFormato(consecutivo, consecutivo.length(), 3, "alpha", " ");
										rc4+=consecutivo;
										
										// CAMPO Nº 72 CODIFICACION DE LA CUENTA CONTABLE
										
										rc4+=completarFormato(aux,aux.length(), 3, "alpha"," ");
										
										// CAMPO Nº 73 NATURALEZA DE LA TRANSACCION CAJA 	
										
										rc4+="E";
										
										// CAMPO Nº 74 FECHA DE RECAUDO (DIFIERE PARA RECIBOS DE CAJA)
										
										fecha = split[0]+split[1]+split[2];
										
										rc4+=fecha;
										
										// CAMPO Nº 75 TIPO DE PAGO 									(DIFIERE PARA RECIBOS DE CAJA)
										
										tipoPago = tipoPagoRc(con, recibosCaja.get("numero_recibo_caja_"+j)+"", recibosCaja.get("institucion_"+j)+"")+"";
										consecutivo=completarFormato(tipoPago, tipoPago.length(), 1, "alpha", " ");
										rc4+= consecutivo;
										
										// CAMPO Nº 76 INDICADOR DE MOVIMIENTO 							(DIFIERE PARA RECIBOS DE CAJA)
										
										rc4+=completarFormato(aux,aux.length(), 1, "alpha","0");
										
										// CAMPO Nº 77 NA
										
										rc4+=consecutivo;
										
										// CAMPO Nº 78 TIPO DE PAGO 									(DIFIERE PARA RECIBOS DE CAJA)
										
										consecutivo=completarFormato(tipoPago, tipoPago.length(), 3, "nume", "0");
										rc4+=consecutivo;
										
										/*String digiVerifica = digitoVerificadorTercero(con, dto.getConvenio().getCodigo())+"";
										
										digiVerifica = completarFormato(digiVerifica, digiVerifica.length(), 1, "nume", "0");
										
										registrosFactura4+=digiVerifica;*/
										
										
										// CAMPO Nº 79 COD BANCO DEL CHEQUE								(DIFIERE PARA RECIBOS DE CAJA)
										
										consecutivo = bancoChequeRc(con, recibosCaja.get("numero_recibo_caja_"+j)+"", recibosCaja.get("institucion_"+j)+"")+"";
										rc4+=completarFormato(consecutivo,consecutivo.length(), 4, "alpha"," ");
										
										// CAMPO Nº 80 NUMERO DE CHEQUE
										
										rc4+=completarFormato(aux,aux.length(), 6, "nume","0");
										
										// CAMPO Nº 81 NA
										
										rc4+=completarFormato(aux,aux.length(), 3, "alpha"," ");
										
										// CAMPO Nº 82 NA
										
										rc4+=completarFormato(aux,aux.length(), 6, "nume","0");
										
										// CAMPO Nº 83 NA
										
										rc4+=completarFormato(aux,aux.length(), 10, "alpha"," ");
										
										// CAMPO Nº 84 NA
										
										rc4+=completarFormato(aux,aux.length(), 20, "alpha"," ");
										
										// CAMPO Nº 85 NA
										
										rc4+=completarFormato(aux,aux.length(), 15, "alpha"," ");
										
										// CAMPO Nº 86 NUMERO CUENTA DEL CHEQUE RECIBIDO
										
										rc4+=completarFormato(aux,aux.length(), 20, "alpha"," ");
										
										// CAMPO Nº 87 NA
										
										rc4+=completarFormato(aux,aux.length(), 8, "nume"," ");
										
										// CAMPO Nº 88 INTERNO
										
										rc4+=completarFormato(aux,aux.length(), 20, "alpha"," ");
										
										// CAMPO Nº 89 NUMERO DEL NIT DEL CONVENIO O PARTICULAR
										
										consecutivo=completarFormato(idPaciente, idPaciente.length(), 15, "alpha", " ");
										rc4+=consecutivo;
										
										// CAMPO Nº 90 DIGITO VERIFICADOR
										
										rc4+=completarFormato(aux,aux.length(), 1, "nume","0");
										
										// CAMPO Nº 91 TIPO IDENTIFICACION TERCERO						(DIFIERE PARA RECIBOS DE CAJA)
										
										rc4+=completarFormato(aux,aux.length(), 1, "alpha"," ");
										
										// CAMPO Nº 92 NOMBRE DEL CONVENIO								(DIFIERE PARA RECIBOS DE CAJA)
										
										this.nombresPacienteMap = new HashMap();
										setNombresPacienteMap(nombresPacienteRc(con, recibosCaja.get("numero_recibo_caja_"+j)+""));
										int numDatosPaciente = Utilidades.convertirAEntero(getNombresPacienteMap("numRegistros").toString());
										
										logger.info("Numero de datos nombres paciente------>"+numDatosPaciente);
										if(numDatosPaciente ==0)
										{
											String nombreYApellidos="";
											String responsableRC = responsablesRC(con, recibosCaja.get("numero_recibo_caja_"+j)+"").trim();
											
											//CAMPO 92
											nombreYApellidos=responsableRC;
											nombreYApellidos=UtilidadTexto.cambiarCaracteresEspeciales(nombreYApellidos);
											if(nombreYApellidos.length()>50)
											{
												nombreYApellidos=nombreYApellidos.substring(0, 50);
											}
											nombreYApellidos = completarFormato(nombreYApellidos, nombreYApellidos.length(), 50, "alpha", " ");
											rc4+=nombreYApellidos;
											
											
											//CAMPO 93
											rc4+=completarFormato(aux,aux.length(), 50, "alpha"," ");
											
											
											//Campo 94 y 95
											
											String spliert[] = responsableRC.split(" ");
											String a="";
											String b="";
											if (spliert.length >= 2)
											{
												a= spliert[0];
												for (int x=1;x<spliert.length;x++)
												{
													b+= (b.equals("")?"":" ") + spliert[x];
												}
											}
											a = UtilidadTexto.cambiarCaracteresEspeciales(a);
											b = UtilidadTexto.cambiarCaracteresEspeciales(b);
											a = completarFormato(a, a.length(), 20, "alpha", " ");
											
											if(a.length()>20)
												a=a.substring(0, 20);
											else
												a = completarFormato(a, a.length(), 20, "alpha", " ");											
											
											if(b.length()>30)
												b=b.substring(0,30);
											else
												b = completarFormato(b, b.length(), 30, "alpha", " ");
											
											rc4+=b;
											
											// CAMPO Nº 96 A
											rc4+=a;
											
										}
										else
										{
//											String nombre1= numDatosPaciente>0?getNombresPacienteMap("primer_nombre_0").toString():"";
											String nombre1="";
											String nombre2="";
											String apellido1="";
											String apellido2="";
											
											if(numDatosPaciente>0)
											{
												apellido1 = getNombresPacienteMap("primer_apellido_0").toString();
												nombre1 = getNombresPacienteMap("primer_nombre_0").toString();
												
												nombre1 = UtilidadTexto.cambiarCaracteresEspeciales(nombre1);
												apellido1 = UtilidadTexto.cambiarCaracteresEspeciales(apellido1);
												
												nombre1=apellido1+" "+nombre1;
												
												logger.info("primer nombre ->"+nombre1);
												if (nombre1.length()>50)
												{
													nombre1= nombre1.substring(0, 50);
												}
											}
											
											nombre1 = completarFormato(nombre1, nombre1.length(), 50, "alpha", " ");
											rc4+=nombre1;
											
											
											// CAMPO Nº 93 NOMBRE DEL CONVENIO
										//	String nombre2= numDatosPaciente>0?getNombresPacienteMap("segundo_nombre_0").toString():"";
											
											/**
											if(numDatosPaciente>0)
											{
												nombre2 = getNombresPacienteMap("segundo_nombre_0").toString();
												nombre2 = UtilidadTexto.cambiarCaracteresEspeciales(nombre2);
												logger.info("segundo nombre ->"+nombre2);
												if (nombre2.length()>50)
												{
													nombre2= nombre2.substring(0, 50);
												}
											}**/
											
											
											nombre2 = completarFormato(nombre2, nombre2.length(), 50, "alpha", " ");
											rc4+=nombre2;
											
											// CAMPO Nº 94 NA
											
											//String apellido1= numDatosPaciente>0?getNombresPacienteMap("primer_apellido_0").toString():"";
											
											
											if(numDatosPaciente>0)
											{
												apellido1 = getNombresPacienteMap("primer_apellido_0").toString();
												apellido1 = UtilidadTexto.cambiarCaracteresEspeciales(apellido1);
												logger.info("PRIMER APELLIDO ->"+apellido1);
												if (apellido1.length()>15)
												{
													apellido1= apellido1.substring(0, 15);
												}
											}
											
											apellido1 = completarFormato(apellido1, apellido1.length(), 15, "alpha", " ");
											rc4+=apellido1;
											
											// CAMPO Nº 95 NA
											
											//String apellido2= numDatosPaciente>0?getNombresPacienteMap("segundo_apellido_0").toString():"";
											
											
											if(numDatosPaciente>0)
											{
												apellido2 = getNombresPacienteMap("segundo_apellido_0").toString();
												apellido2 = UtilidadTexto.cambiarCaracteresEspeciales(apellido2);
												logger.info("segundo APELLIDO ->"+apellido2);
												if (apellido2.length()>15)
												{
													apellido2= apellido2.substring(0, 15);
												}
											}
											
											apellido2 = completarFormato(apellido2, apellido2.length(), 15, "alpha", " ");
											rc4+=apellido2;
											
											// CAMPO Nº 96 B
											
											String nombresCompletos="";
											
											if(numDatosPaciente>0)
											{
												nombre1 = getNombresPacienteMap("primer_nombre_0").toString().trim();
												nombre1 = UtilidadTexto.cambiarCaracteresEspeciales(nombre1);
												nombre2 = getNombresPacienteMap("segundo_nombre_0").toString().trim();
												if(nombre2.length()>0)
												{
													nombre2 = UtilidadTexto.cambiarCaracteresEspeciales(nombre2);
													nombresCompletos=nombre1+" "+nombre2;
												}
												else
												{
													nombresCompletos=nombre1;
												}
												
												if (nombresCompletos.length()>20)
												{
													nombresCompletos= nombresCompletos.substring(0, 20);
												}
											}
											rc4+=completarFormato(nombresCompletos,nombresCompletos.length(), 20, "alpha"," ");	
										}
										
										// CAMPO Nº 96 NA
										
										//rc4+=completarFormato(aux,aux.length(), 20, "alpha"," ");
										
										// CAMPO Nº 97 TIPO DE TERCERO									(DIFIERE PARA RECIBOS DE CAJA)
										
										rc4+=completarFormato(aux,aux.length(), 1, "nume","0");
										
										// CAMPO Nº 98 CODIGO DEL TIPO DE IDENTIFICACION DEL TERCERO
										
										rc4+=completarFormato(aux,aux.length(), 1, "nume","0");
										
										// CAMPO Nº 99 INDICADOR CLIENTE
										
										rc4+=completarFormato(aux,aux.length(), 1, "nume","1");
										
										// CAMPO Nº 100 NA
										
										rc4+=completarFormato(aux,aux.length(), 1, "nume","0");
										
										// CAMPO Nº 101 NA
										
										rc4+=completarFormato(aux,aux.length(), 1, "nume","0");
										
										// CAMPO Nº 102 NA
										
										rc4+=completarFormato(aux,aux.length(), 1, "nume","0");
										
										// CAMPO Nº 103 NA
										
										rc4+=completarFormato(aux,aux.length(), 1, "nume","0");
										
										// CAMPO Nº 104 NA
										
										rc4+=completarFormato(aux,aux.length(), 1, "nume","0");
										
										// CAMPO Nº 105 NA
										
										rc4+=completarFormato(aux,aux.length(), 1, "nume","0");
										
										// CAMPO Nº 106 ESTADO DEL CONVENIO
										
										rc4+=completarFormato(aux,aux.length(), 1, "alpha"," ");
										
										// CAMPO Nº 107 FECHA DE CREACION DEL CONVENIO					(DIFIERE PARA RECIBOS DE CAJA)
										
										consecutivo = "20080101";
										consecutivo=completarFormato(consecutivo,consecutivo.length(), 8, "nume","0");
										rc4+=consecutivo;
										
										// CAMPO Nº 108 CODIGO DEL PAIS DEL CONVENIO					(DIFIERE PARA RECIBOS DE CAJA)
										
										rc4+=completarFormato(aux,aux.length(), 3, "nume","0");
										
										// CAMPO Nº 109 CODIGO DEPARTAMENTO DEL CONVENIO				(DIFIERE PARA RECIBOS DE CAJA)
										
										consecutivo ="01";
										rc4+=consecutivo;
										
										// CAMPO Nº 110 CODIGO DE LA CIUDAD DEL CONVENIO
										
										consecutivo ="001";
										rc4+=consecutivo;
										
										// CAMPO Nº 111 DIRECCION CORRESPONDENCIA DEL CONVENIO 1
										
										logger.info("Cuidado con esto!!!!!!! hay que tomar la dirección de la institución OJOJOJOJOJOJOJOJOJOJOJOJOJOJOJOJOJOJOJ");
										consecutivo = "carrera 7a. No 68-70";
										consecutivo = completarFormato(consecutivo, consecutivo.length(), 120, "alpha", " ");
										rc4+=consecutivo;
									
										// CAMPO Nº 114 TELEFONO
										
										rc4+=completarFormato(aux,aux.length(), 15, "alpha"," ");
										
										// CAMPO Nº 115 FAX
										
										rc4+=completarFormato(aux,aux.length(), 15, "alpha"," ");
										
										// CAMPO Nº 116 CODIGO POSTAL
										
										rc4+=completarFormato(aux,aux.length(), 10, "alpha"," ");
										
										// CAMPO Nº 117 DIRECCION ELECTRONICA DEL CONVENIO
										
										rc4+=completarFormato(aux,aux.length(), 50, "alpha"," ");
										
										// CAMPO Nº 118 NA
										
										rc4+=completarFormato(aux,aux.length(), 1, "nume","0");
										
										// CAMPO Nº 119 BARRIO
										
										rc4+=completarFormato(aux,aux.length(), 15, "alpha"," ");
										
										// CAMPO Nº 120 TELEFONO 2 DEL CONVENIO
										
										rc4+=completarFormato(aux,aux.length(), 15, "alpha"," ");
										
										// CAMPO Nº 121 NA
										
										rc4+=completarFormato(aux,aux.length(), 1, "nume","0");
										
										// CAMPO Nº 122 NA
										
										rc4+=completarFormato(aux,aux.length(), 6, "alpha"," ");
										
										// CAMPO Nº 123 INTERNO
										
										rc4+=completarFormato(aux,aux.length(), 58, "alpha"," ");
										
										// CAMPO Nº 124 CLASE DE CLIENTE
										
										campoInterfaz124 = completarFormato(aux, aux.length(), 6, "alpha", " ");
										
										// CAMPO Nº 125 CENTRO DE OPERACION DEL CLIENTE
										
										rc5=centroAtencion;
										
										// CAMPO Nº 126 CODIGO ZONA / SUBZONA
										
										consecutivo="1";
										consecutivo=completarFormato(consecutivo,consecutivo.length(), 6, "nume","0");
										rc5+=consecutivo;
										
										// CAMPO Nº 127 CODIGO VENDEDOR
										
										rc5+=completarFormato(aux,aux.length(), 4, "alpha"," ");
										
										// CAMPO Nº 128 NOMBRE DEL CONTACTO
										
										rc5+=completarFormato(aux,aux.length(), 40, "alpha"," ");
										
										// CAMPO Nº 129 CALIFICACION DEL CLIENTE
										
										rc5+="A";
										
										// CAMPO Nº 130 INDICADOR RTE IVA
										
										rc5+=completarFormato(aux,aux.length(), 1, "nume","0");
										
										// CAMPO Nº 131 INDICADOR DE RETEFUENTE
										
										rc5+=completarFormato(aux,aux.length(), 1, "nume","0");
										
										// CAMPO Nº 132 INTERNO
										
										rc5+=completarFormato(aux,aux.length(), 3, "nume","0");
										
										// CAMPO Nº 133
										
										rc5+=completarFormato(aux,aux.length(), 2, "alpha"," ");
										
										// CAMPO Nº 134 OBSERVACION DEL CLIENTE
										
										rc5+=completarFormato(aux,aux.length(), 40, "alpha"," ");
										
										// CAMPO Nº 135 DIAS DE GRACIA
										
										rc5+=completarFormato(aux,aux.length(), 3, "nume","0");
										
										// CAMPO Nº 136 CUPO DE CREDITO
										
										rc5+=completarFormato(aux,aux.length(), 11, "nume","0");
										
										// CAMPO Nº 137 COD CRITERIO 1
										
										rc5+=completarFormato(aux,aux.length(), 4, "alpha"," ");
										
										// CAMPO Nº 138 COD CRITERIO 2
										
										rc5+=completarFormato(aux,aux.length(), 4, "alpha"," ");
										
										// CAMPO Nº 139 COD CRITERIO 3
										
										rc5+=completarFormato(aux,aux.length(), 4, "alpha"," ");
										
										// CAMPO Nº 140 IND DE BLOQUE POR CUPO
										
										rc5+=completarFormato(aux,aux.length(), 1, "nume","0");
										
										// CAMPO Nº 141 IND DE BLOQUE POR MORA
										
										rc5+=completarFormato(aux,aux.length(), 1, "nume","0");
										
										// CAMPO Nº 142 IND ESTADO DEL CLIENTE
										
										rc5+=completarFormato(aux,aux.length(), 1, "alpha"," ");
										
										// CAMPO Nº 143 IND EXIGE ORDEN DE COMPRA
										
										rc5+=completarFormato(aux,aux.length(), 1, "nume","0");
										
										// CAMPO Nº 144 IND EXIGE REMISION
										
										rc5+=completarFormato(aux,aux.length(), 1, "nume","0");
										
										// CAMPO Nº 145 CODIGO LISTA DE PRECIOS
										
										rc5+=completarFormato(aux,aux.length(), 3, "alpha"," ");
										
										// CAMPO Nº 146 CODIGO LISTA DE DESCUENTOS
										
										rc5+=completarFormato(aux,aux.length(), 2, "alpha"," ");
										
										// CAMPO Nº 147 INDICATIVO DESCUENTO GLOBAL
										
										rc5+=completarFormato(aux,aux.length(), 1, "nume","0");
										
										// CAMPO Nº 148 TASA DESCUENTO GLOBAL 
										
										rc5+=completarFormato(aux,aux.length(), 4, "nume","0");
										
										// CAMPO Nº 149 INDI EXCLUIDO IMPUESTO
										
										rc5+=completarFormato(aux,aux.length(), 1, "nume","0");
										
										// CAMPO Nº 150 FORMA DE PAGO
										
										rc5+=completarFormato(aux,aux.length(), 1, "nume","0");
										
										// CAMPO Nº 151 IND ANTICIPADO
										
										rc5+=completarFormato(aux,aux.length(), 1, "nume","0");
										
										// CAMPO Nº 152 IND CHEQUES POSTFECHADOS
										
										rc5+=completarFormato(aux,aux.length(), 1, "nume","0");
										
										// CAMPO Nº 153 COD MONEDA
										
										rc5+=completarFormato(aux,aux.length(), 2, "alpha"," ");
										
										// CAMPO Nº 154 OBSERVACION VENTA
										
										rc5+=completarFormato(aux,aux.length(), 40, "alpha"," ");
										
										// CAMPO Nº 155 OBSERVACION
										
										rc5+=completarFormato(aux,aux.length(), 40, "alpha"," ");
										
										// CAMPO Nº 156 CODIGO DESC PRONTO PAGO
										
										rc5+=completarFormato(aux,aux.length(), 2, "alpha"," ");
										
										// CAMPO Nº 157 IND RETE ICA
										
										rc5+=completarFormato(aux,aux.length(), 1, "nume","0");
										
										// CAMPO Nº 158 COD RUTA VISITA
										
										rc5+=completarFormato(aux,aux.length(), 4, "alpha"," ");
										
										// CAMPO Nº 159 RUTA DE TRANSPORTE
										
										rc5+=completarFormato(aux,aux.length(), 4, "alpha"," ");
										
										// CAMPO Nº 160 BARRIO CLIENTE
										
										rc5+=completarFormato(aux,aux.length(), 15, "alpha"," ");
										
										// CAMPO Nº 161 ESTADO DE CARTERA
										
										rc5+=completarFormato(aux,aux.length(), 1, "alpha"," ");
										
										// CAMPO Nº 162 MES/DIA NACIMIENTO CONTACTO
										
										rc5+=completarFormato(aux,aux.length(), 4, "nume","0");
										
										// CAMPO Nº 163 CONTACTO CLIENTE
										
										rc5+=completarFormato(aux,aux.length(), 40, "alpha"," ");
										
										// CAMPO Nº 164 MES/DIA NACIMIENTO CLIENTE
										
										rc5+=completarFormato(aux,aux.length(), 4, "nume","0");
										
										// CAMPO Nº 165 DESCUENTO GLOBAL
										
										rc5+=completarFormato(aux,aux.length(), 4, "nume","0");
										
										// CAMPO Nº 166 FECHA BLOQUE
										
										rc5+=completarFormato(aux,aux.length(), 8, "nume","0");
										
										// CAMPO Nº 167 PUNTO DE ENVIO CLIENTE
										
										rc5+=completarFormato(aux,aux.length(), 4, "alpha"," ");
										
										// CAMPO Nº 168 CLASE DEL PROVEEDOR
										
										rc5+=completarFormato(aux,aux.length(), 6, "alpha"," ");
										
										// CAMPO Nº 169 CONTACTO DEL PROVEEDOR
										
										rc5+=completarFormato(aux,aux.length(), 40, "alpha"," ");
										
										// CAMPO Nº 170 IND REGIMEN SIMPLIFICADO
										
										rc5+=completarFormato(aux,aux.length(), 1,  "alpha"," ");
										
										// CAMPO Nº 171 IND AUTO RETENCION
										
										rc5+=completarFormato(aux,aux.length(), 1,  "alpha"," ");
										
										// CAMPO Nº 172 IND RETE IVA
										
										rc5+=completarFormato(aux,aux.length(), 1,  "alpha"," ");
										
										// CAMPO Nº 173 IND RETE ICA
										
										rc5+=completarFormato(aux,aux.length(), 1,  "alpha"," ");
										
										// CAMPO Nº 174 CONDICION DE PAGO
										
										rc5+=completarFormato(aux,aux.length(), 2, "alpha"," ");
										
										// CAMPO Nº 175 DIAS DE GRACIA
										
										rc5+=completarFormato(aux,aux.length(), 3,  "alpha"," ");
										
										// CAMPO Nº 176 CUPO CREDITO DEL PROVEEDOR
										
										rc5+=completarFormato(aux,aux.length(), 11,  "alpha"," ");
										
										// CAMPO Nº 177 COD MONEDA
										
										rc5+=completarFormato(aux,aux.length(), 2,  "alpha"," ");
										
										// CAMPO Nº 178 ESTADO DEL PROVEEDOR
										
										rc5+=completarFormato(aux,aux.length(), 1, "alpha"," ");
										
										// CAMPO Nº 179 OBSERVACION
										
										rc5+=completarFormato(aux,aux.length(), 40, "alpha"," ");
										
										// CAMPO Nº 180 INTERNO
										
										rc5+=completarFormato(aux,aux.length(), 1,  "alpha"," ");
										
										// CAMPO Nº 181 COD RETE ICA
										
										rc5+=completarFormato(aux,aux.length(), 2,  "alpha"," ");
										
										// CAMPO Nº 182 INTERNO
										
										rc5+=completarFormato(aux,aux.length(), 2, "alpha"," ");
										
										// CAMPO Nº 183 INTERNO
										
										rc5+=completarFormato(aux,aux.length(), 8, "alpha"," ");
										
										// CAMPO Nº 184 DESCUENTO OTORGADO
										
										rc5+=completarFormato(aux,aux.length(), 4,  "alpha"," ");
										
										// CAMPO Nº 185 METODO DE PAGO
										
										rc5+=completarFormato(aux,aux.length(), 1,  "alpha"," ");
										
										// CAMPO Nº 186 CD NRO CUENTA PROVEEDOR
										
										rc5+=completarFormato(aux,aux.length(), 2,  "alpha"," ");
										
										// CAMPO Nº 187 INTERNO
										
										rc5+=completarFormato(aux,aux.length(), 1,  "alpha"," ");
										
										// CAMPO Nº 188 CIUDAD DE EXPEDICION
										
										rc5+=completarFormato(aux,aux.length(), 20,  "alpha"," ");
										
										// CAMPO Nº 189 FECHA DE INGRESO
										
										rc5+=completarFormato(aux,aux.length(), 8,  "alpha"," ");
										
										// CAMPO Nº 190 FECHA DE NACIMIENTO
										
										rc5+=completarFormato(aux,aux.length(), 8,  "alpha"," ");
										
										// CAMPO Nº 191 CODIGO PAIS DE ORIGEN
										
										rc5+=completarFormato(aux,aux.length(), 3,  "alpha"," ");
										
										// CAMPO Nº 192 CODIGO DTO DE ORIGEN
										
										rc5+=completarFormato(aux,aux.length(), 2,  "alpha"," ");
										
										// CAMPO Nº 193 COD CIUDAD DE ORIGEN
										
										rc5+=completarFormato(aux,aux.length(), 3,  "alpha"," ");
										
										// CAMPO Nº 194 SEXO
										
										rc5+=completarFormato(aux,aux.length(), 1,  "alpha"," ");
										
										// CAMPO Nº 195 ESTADO CIVIL
										
										rc5+=completarFormato(aux,aux.length(), 1, "alpha"," ");
										
										// CAMPO Nº 196 RUTA
										
										rc5+=completarFormato(aux,aux.length(), 1, "alpha"," ");
										
										// CAMPO Nº 197 ORDEN TRANSPORTE
										
										rc5+=completarFormato(aux,aux.length(), 3,  "alpha"," ");
										
										// CAMPO Nº 198 CARGO EMPLEADO
										
										rc5+=completarFormato(aux,aux.length(), 4, "alpha"," ");
										
										// CAMPO Nº 199 INTERNO
										
										rc5+=completarFormato(aux,aux.length(), 50, "alpha"," ");
										
										
										
										/**
										 * 
										 * 
										 * **********************  LOS DETALLES DEL RECIBO DE CAJA  ********************
										 * 
										 * 
										 */
										
										this.formasPagoMap = new HashMap();
										
										// Se captura la forma de Pago para la partida Debito y el Valor para debitos y Creditos 
										setFormasPagoMap(formasPagoRc(con, recibosCaja.get("numero_recibo_caja_"+j)+"", recibosCaja.get("institucion_"+j)+""));
										int registrosFormas =Utilidades.convertirAEntero(getFormasPagoMap().get("numRegistros").toString());
										
										// Se captura el tipo de concepto para el Recibo de caja, solo utilizado para la partida credito
										tipoConcepto = tipoConceptoRc(con, recibosCaja.get("numero_recibo_caja_"+j)+"", recibosCaja.get("institucion_"+j)+"")+"";
										
										
										
										// SE INICIALIZAN LOS VALORES PARA SU COMPARACION
										double ValorDebito=0;
										double ValorCredito=0;
										String cuentaContable ="";
										String valorCuenta="";
										
										
										int estado = estadoRc(con, recibosCaja.get("numero_recibo_caja_"+j)+"",recibosCaja.get("institucion_"+j)+"");
										
										//naturalezaCredito = naturalezaConceptoRc(con, recibosCaja.get("numero_recibo_caja_"+j)+"",recibosCaja.get("institucion_"+j)+"");
										//SE QUEMA LA NATURALEZA CREDITO POR ID 28203 -----OCT 20 - 08
										
										
										
										// =========================PARTIDA CREDITO PARA CONCEPTOS RC ===============================
										
										naturalezaCredito = ConstantesBD.naturalezaCredito;
										
										if(naturalezaCredito.equals(ConstantesBD.naturalezaCredito))
										{
											String valorconcepto="";
											
											// Se compara todo los tipos de Conceptos para el recibo de Caja 
											if(Utilidades.convertirAEntero(tipoConcepto) == ConstantesBD.codigoTipoIngresoTesoreriaNinguno || Utilidades.convertirAEntero(tipoConcepto) == ConstantesBD.codigoTipoIngresoTesoreriaAbonos || Utilidades.convertirAEntero(tipoConcepto) == ConstantesBD.codigoTipoIngresoTesoreriaCarteraParticular || Utilidades.convertirAEntero(tipoConcepto) == ConstantesBD.codigoTipoIngresoTesoreriaOtrasCxC)
											{
												cuentaContable = conceptoIngTesoreriaRc(con, recibosCaja.get("numero_recibo_caja_"+j)+"", recibosCaja.get("institucion_"+j)+"", tipoConcepto)+"";
													
												if(cuentaContable.equals(""))
												{
													indInconsistencia=true;
													inconsistencias(recibosCaja.get("fecha_"+j)+"", recibosCaja.get("hora_"+j)+"", recibosCaja.get("numero_recibo_caja_"+j)+"", "Concepto de Recaudo", "No tiene parametrizada cuentas", false);
												}
												else
												{
													cuentaContable = completarFormato(cuentaContable, cuentaContable.length(), 8, "alpha", " ");
													
													if(estado != ConstantesBD.codigoEstadoReciboCajaAnulado)
													{
														/**
														 * El valor para hacer la comparacion por cada forma de pago
														 */
														
														valorconcepto = valorConceptoRc(con, recibosCaja.get("numero_recibo_caja_"+j)+"",recibosCaja.get("institucion_"+j)+"");	
														//valorConcepto = (getFormasPagoMap().get("valor_"+x)+"");
														
														Double valorCreditoDbl = new Double(Utilidades.convertirADouble(valorconcepto, true));
														ValorCredito=ValorCredito + valorCreditoDbl.intValue();
														
														if(valorconcepto.contains("."))									
															valorconcepto = valorconcepto.replaceAll("\\.", "0");
														else
															valorconcepto += "00" ;
														
														valorconcepto = completarFormato(valorconcepto, valorconcepto.length(), 17, "nume","0")+"+";
													}
													else
													{
														valorconcepto = completarFormato(aux, aux.length(), 17, "nume","0")+"+";
													}
														
													naturalezaCredito = completarFormato(naturalezaCredito, naturalezaCredito.length(), 1, "alpha", " ");
													
//													 SE REALIZA EL CONSECUTIVO POR REGISTRO
													consecutivoProcesoFacRc = consecutivoProcesoFacRc + 1;
													consecutivo = consecutivoProcesoFacRc+"";
													consecutivo = completarFormato(consecutivo, consecutivo.length(), 9, "nume",  "0");
													logger.info("Campo 1c ->"+consecutivo);
													// FIN DEL CONSECUTIVO POR REGISTRO
													
													
													total = consecutivo+rc1+cuentaContable+rc2+naturalezaCredito+valorconcepto+rc3+campoInterfaz46+rc4+campoInterfaz124+rc5+"\n";
														
													buffer.write(total);
												}
											}
											else if(Utilidades.convertirAEntero(tipoConcepto) == ConstantesBD.codigoTipoIngresoTesoreriaConvenios) // TIPO DE RECAUDO O CONCEPTO CONVENIO = 2
											{
												cuentaContable = cuentaConvenioRc(con, recibosCaja.get("numero_recibo_caja_"+j)+"", recibosCaja.get("institucion_"+j)+"", tipoConcepto)+"";
												logger.info("CUENTA CONVENIO "+cuentaContable);
													
												if(cuentaContable.equals(""))	//si no hay valor por cuenta convenio se busca por cuenta regimen
												{
													cuentaContable = cuentaRegimenRc(con, recibosCaja.get("numero_recibo_caja_"+j)+"", recibosCaja.get("institucion_"+j)+"", tipoConcepto)+"";
													logger.info("CUENTA REGIMEN CONVENIO "+cuentaContable);
														
													if(cuentaContable.equals(""))	// si no hay valor en cuenta regimen se genera la incosistencia
													{
														indInconsistencia=true;
														inconsistencias(recibosCaja.get("fecha_"+j)+"", recibosCaja.get("hora_"+j)+"", recibosCaja.get("numero_recibo_caja_"+j)+"", "Cuenta Convenio y Cuenta Regimen", "No Existen datos para el Tipo Cuenta Convenio", false);
													}
												}
													
												cuentaContable = completarFormato(cuentaContable, cuentaContable.length(), 8, "alpha", " ");
												
												if(estado != ConstantesBD.codigoEstadoReciboCajaAnulado)
												{
													valorconcepto = valorConceptoRc(con, recibosCaja.get("numero_recibo_caja_"+j)+"",recibosCaja.get("institucion_"+j)+"");
													//valorDetalle = (getFormasPagoMap().get("valor_"+x)+"");
													
													Double valorCreditoDbl = new Double(Utilidades.convertirADouble(valorconcepto, true));
													ValorCredito=ValorCredito + valorCreditoDbl.intValue();
													
													if(valorconcepto.contains("."))									
														valorconcepto = valorconcepto.replaceAll("\\.", "0");
													else
														valorconcepto += "00" ;
													valorconcepto = completarFormato(valorconcepto, valorconcepto.length(), 17, "nume","0")+"+";
												}
												else
												{
													valorconcepto = completarFormato(aux, aux.length(), 17, "nume","0")+"+";
												}
												
												naturalezaCredito = completarFormato(naturalezaCredito, naturalezaCredito.length(), 1, "alpha", " ");
												
												
												
//												 SE REALIZA EL CONSECUTIVO POR REGISTRO
												consecutivoProcesoFacRc = consecutivoProcesoFacRc + 1;
												consecutivo = consecutivoProcesoFacRc+"";
												consecutivo = completarFormato(consecutivo, consecutivo.length(), 9, "nume",  "0");
												logger.info("Campo 1d ->"+consecutivo);
												// FIN DEL CONSECUTIVO POR REGISTRO
												
												total = consecutivo+rc1+cuentaContable+rc2+naturalezaCredito+valorconcepto+rc3+campoInterfaz46+rc4+campoInterfaz124+rc5+"\n";
												
												buffer.write(total);
													
											}
											else if(Utilidades.convertirAEntero(tipoConcepto) == ConstantesBD.codigoTipoIngresoTesoreriaPacientes) // TIPO DE RECAUDO O CONCEPTO PACIENTE = 1
											{
																										
												cuentaContable = cuentaPacienteConvenioRc(con, recibosCaja.get("numero_recibo_caja_"+j)+"", recibosCaja.get("institucion_"+j)+"", tipoConcepto)+"";
													
												logger.info("CUENTA CONVENIO PACIENTE "+cuentaContable);
													
												campoInterfaz46 = interfazConvenioPacienteRc(con, recibosCaja.get("numero_recibo_caja_"+j)+"", recibosCaja.get("institucion_"+j)+"")+"";
												campoInterfaz46 = completarFormato(campoInterfaz46, campoInterfaz46.length(), 13, "alpha", " ");
												
												campoInterfaz124 = claseClienteRc(con, recibosCaja.get("numero_recibo_caja_"+j)+"", recibosCaja.get("institucion_"+j)+"")+"";
												
												campoInterfaz124 = completarFormato(campoInterfaz124, campoInterfaz124.length(), 6, "alpha", " ");														
													
												if(cuentaContable.equals("")) //si no hay valor por cuenta convenio se busca por cuenta regimen
												{
													cuentaContable = cuentaPacienteRegimenRc(con, recibosCaja.get("numero_recibo_caja_"+j)+"", recibosCaja.get("institucion_"+j)+"", tipoConcepto)+"";
													logger.info("CUENTA REGIMEN PACIENTE "+cuentaContable);
													
													if(cuentaContable.equals("")) // si no hay valor en cuenta regimen se genera la incosistencia
													{
														indInconsistencia = true;
														inconsistencias(recibosCaja.get("fecha_"+j)+"", recibosCaja.get("hora_"+j)+"", recibosCaja.get("numero_recibo_caja_"+j)+"", "Cuenta Convenio y Cuenta Regimen", "No Existen datos para el Tipo Cuenta Paciente", false);
													}
															
												}
													
												cuentaContable = completarFormato(cuentaContable, cuentaContable.length(), 8, "alpha", " ");
												
												if(estado != ConstantesBD.codigoEstadoReciboCajaAnulado)
												{
													valorconcepto = valorConceptoRc(con, recibosCaja.get("numero_recibo_caja_"+j)+"",recibosCaja.get("institucion_"+j)+"");
													//valorDetalle = (getFormasPagoMap().get("valor_"+x)+"");
													
													Double valorCreditoDbl = new Double(Utilidades.convertirADouble(valorconcepto, true));
													ValorCredito=ValorCredito + valorCreditoDbl.intValue();
													
													logger.info("Valor Credito 3 ->"+ValorCredito);
													
													if(valorconcepto.contains("."))									
														valorconcepto = valorconcepto.replaceAll("\\.", "0");
													else
														valorconcepto += "00" ;
													valorconcepto = completarFormato(valorconcepto, valorconcepto.length(), 17, "nume","0")+"+";
												}
												else
												{
													valorconcepto = completarFormato(aux, aux.length(), 17, "nume","0")+"+";
												}
												
												naturalezaCredito = completarFormato(naturalezaCredito, naturalezaCredito.length(), 1, "alpha", " ");
												logger.info("-------VALOR CONCEPTO -> "+valorconcepto);
												
//												 SE REALIZA EL CONSECUTIVO POR REGISTRO
												consecutivoProcesoFacRc = consecutivoProcesoFacRc + 1;
												consecutivo = consecutivoProcesoFacRc+"";
												consecutivo = completarFormato(consecutivo, consecutivo.length(), 9, "nume",  "0");
												logger.info("Campo 1e ->"+consecutivo);
												// FIN DEL CONSECUTIVO POR REGISTRO
												
												total = consecutivo+rc1+cuentaContable+rc2+naturalezaCredito+valorconcepto+rc3+campoInterfaz46+rc4+campoInterfaz124+rc5+"\n";
													
												buffer.write(total);
											}
										} // FIN PARTIDA CREDITO
										
										
										for (int x=0; x<registrosFormas;x++)
										{
											this.cuentaContableRc = new HashMap();
											
											int forma = Utilidades.convertirAEntero(getFormasPagoMap().get("forma_pago_"+x)+"");
											// Se Captura la Cuenta Contable y la Naturaleza para todas las formas de pago del recibo de caja
											setCuentaContableRc(cContableyNaturalezaRc(con, forma));
											
											int registroContableNatura = Utilidades.convertirAEntero(getCuentaContableRc().get("numRegistros").toString());
											
											if(registroContableNatura > 0)
											{
												for(int y=0; y<registroContableNatura;y++)
												{
													//naturalezaDebito = getCuentaContableRc().get("naturaleza_"+y)+""; //ES LA NATURALEZA ANTERIOR QUE TOMA LA PARAMETRIZADA EN LA CUENTA CONTABLE
													// SE QUEMA LAS NATURALEZA POR SOLICITUD DE INMACULADA OCT20 - 08
													
													//====================================PARTIDA DEBITO PARA LAS FORMAS DE PAGO =================
													
													naturalezaDebito=ConstantesBD.naturalezaDebito;
													
													
													if(estado == ConstantesBD.codigoEstadoReciboCajaAnulado)
													{
														cuentaContable = getCuentaContableRc().get("cuenta_contable_"+y)+"";
														cuentaContable = completarFormato(cuentaContable, cuentaContable.length(), 8, "alpha", " ");
														
														valorCuenta = (getFormasPagoMap().get("valor_"+x)+"");
														//Double valorDebitoDbl = new Double(Utilidades.convertirADouble(valorCuenta, true));
														//ValorDebito=ValorDebito+valorDebitoDbl.intValue();
														//valorCuenta =valorCuenta.replaceAll(".", "0");
														naturalezaDebito = completarFormato(naturalezaDebito, naturalezaDebito.length(), 1, "alpha", " ");
														valorCuenta = completarFormato(aux, aux.length(), 17, "nume", "0")+"+";
														
														// SE REALIZA EL CONSECUTIVO POR REGISTRO
														consecutivoProcesoFacRc = consecutivoProcesoFacRc + 1;
														consecutivo = consecutivoProcesoFacRc+"";
														consecutivo = completarFormato(consecutivo, consecutivo.length(), 9, "nume",  "0");
														logger.info("Campo 1a ->"+consecutivo);
														// FIN DEL CONSECUTIVO POR REGISTRO
														
														total = consecutivo+rc1+cuentaContable+rc2+naturalezaDebito+valorCuenta+rc3+campoInterfaz46+rc4+campoInterfaz124+rc5+"\n";
														buffer.write(total);
													}
													else if(estado!= ConstantesBD.codigoEstadoReciboCajaAnulado)
													{
														if(naturalezaDebito.equals(ConstantesBD.naturalezaDebito))
														{
															//-------------PARTIDA DEBITO
																																									
															cuentaContable = getCuentaContableRc().get("cuenta_contable_"+y)+"";
																
															cuentaContable = completarFormato(cuentaContable, cuentaContable.length(), 8, "alpha", " ");
															
															valorCuenta = (getFormasPagoMap().get("valor_"+x)+"");
															
															logger.info(">>>>>>VALOR CUENTA DEBITO  >"+valorCuenta+"<-");
															
															Double valorDebitoDbl = new Double(Utilidades.convertirADouble(valorCuenta, true));
															ValorDebito=ValorDebito+valorDebitoDbl.intValue();
																
															if(valorCuenta.contains("."))									
																valorCuenta = valorCuenta.replaceAll("\\.", "0");
															else
																valorCuenta += "00" ;
																
															naturalezaDebito = completarFormato(naturalezaDebito, naturalezaDebito.length(), 1, "alpha", " ");
																
															valorCuenta = completarFormato(valorCuenta, valorCuenta.length(), 17, "nume", "0")+"+";
															
//															 SE REALIZA EL CONSECUTIVO POR REGISTRO
															consecutivoProcesoFacRc = consecutivoProcesoFacRc + 1;
															consecutivo = consecutivoProcesoFacRc+"";
															consecutivo = completarFormato(consecutivo, consecutivo.length(), 9, "nume",  "0");
															logger.info("Campo 1b ->"+consecutivo);
															// FIN DEL CONSECUTIVO POR REGISTRO
															
															total = consecutivo+rc1+cuentaContable+rc2+naturalezaDebito+valorCuenta+rc3+campoInterfaz46+rc4+campoInterfaz124+rc5+"\n";
															
															buffer.write(total);
														}
														
													}// FIN NO ES ANULADO
												
												}// FIN DEL FOR REGISTROS CONTABLES Y NATURALEZA
												
												
											}// FIN REGISTRO REGISTROS CONTABLES Y NATURALEZA
											else if(registroContableNatura < 0)
											{
												indInconsistencia=true;
												inconsistencias(recibosCaja.get("fecha_"+j)+"", recibosCaja.get("hora_"+j)+"", recibosCaja.get("numero_recibo_caja_"+j)+"", "Cuentas Contables", "No estan Parametrizadas", false);
											}
										} // FIN FORMAS DE PAGO DEL RECIBO DE CAJA
										
										
										/**
										 * SE COMPARAN LOS VALORES DEBITOS Y CREDITOS, SI NO SON IGUALES SE CANCELA EL PROCESO
										 */
										double valor =ValorDebito-ValorCredito;
										logger.info("======================================");
										logger.info("Debito ="+ValorDebito);
										logger.info("Credito ="+ValorCredito);
										logger.info("SUMATORIA ="+valor);
										logger.info("======================================");
										
										if(ValorDebito != ValorCredito)
										{
											indInconsistencia=true;
											inconsistencias(recibosCaja.get("fecha_"+j)+"", recibosCaja.get("hora_"+j)+"", recibosCaja.get("numero_recibo_caja_"+j)+"", "Debitos y Creditos", "La sumatoria es Diferente", false);
											logger.info(".......::::::    DEBITOS Y CREDITOS ES DIFERENTE    :::...... ");
										}
										
										
										
										logger.info("Contador Facturas"+contadorRegistros);
										logger.info("inconsistencias"+indInconsistencia);
										
										if(!indInconsistencia)
										{
											// SE MARCA EL RECIBO DE CAJA COMO CONTABILIZADO
											if(!marcaReciboCaja(con, recibosCaja.get("numero_recibo_caja_"+j)+""))
											{
												this.transaccion = false;
												logger.info("NO SE PUDO MARCAR LA FACTURA");
												errores.add("descripcion", new ActionMessage("errors.problemasGenericos","No se realizó la Marca a la Factura"));
												return errores;
											}
											contadorRegistros++;			
													
											logger.info("Contador Facturas despues de if"+contadorRegistros);
										}
																
										logger.info("inconsistencia: "+indInconsistencia);	
									
									} //FIN VALIDACION QUE EXISTA EL MAPA

								}// FIN DE FOR DE TODOS LOS RECIBOS CAJA CARGADOS EN EL MAPA
								//if (contadorRegistros > 0)
								
									buffer.close();
																	
								
								
								
								if(this.inconsistenciasMap.containsKey("numRegistros") && 
										!this.inconsistenciasMap.get("numRegistros").equals("0"))
								{
									InconsistenciasArray.add(this.inconsistenciasMap);					
								}				
							}
						
						
					}catch(FileNotFoundException e) 
					{
						errores.add("descripcion",new ActionMessage("errors.invalid","Error al Cargar el Archivo "+nombre));		
						return errores;
					}
					
					
				}catch(IOException e)
				{
					e.printStackTrace();					
					logger.error("Error en los streams del archivo: "+e);
					return null;
				}	
			}
			else
			{
				logger.info("no es posible generar el archivo validaciones existe, sobreescribir");		
			}
	
		return errores;
		
		
	}



	
	
	private String centroCostoConceptoRc(Connection con, String codigoRc, String institucion) 
	{
		return getInterfazSistemaUnoDao().centroCostoConceptoRc(con, codigoRc, institucion);
	}



	private String responsablesRC(Connection con, String responsable) 
	{
		return getInterfazSistemaUnoDao().responsablesRC(con,responsable);
	}



	private HashMap nombresPacienteRc(Connection con, String codigoRc) 
	{
		return getInterfazSistemaUnoDao().nombresPacienteRc(con, codigoRc);
	}



	private String valorConceptoRc(Connection con, String codigoRc, String institucion) 
	{
		return getInterfazSistemaUnoDao().valorConceptoRc(con, codigoRc, institucion);
	}



	private String naturalezaConceptoRc(Connection con, String codigoRc, String institucion) 
	{
		return getInterfazSistemaUnoDao().naturalezaConceptoRc(con, codigoRc, institucion);
	}



	private int estadoRc(Connection con, String codigoRc, String institucion) 
	{
		
		return getInterfazSistemaUnoDao().estadoRc(con, codigoRc, institucion);
	}



	private boolean marcaReciboCaja(Connection con, String codigoRc) 
	{
		return getInterfazSistemaUnoDao().marcaReciboCaja(con, codigoRc);
	}



	private String bancoChequeRc(Connection con, String codigoRc, String institucion) 
	{
		return getInterfazSistemaUnoDao().bancoChequeRc(con, codigoRc, institucion);
	}



	private String interfazConvenioPacienteRc(Connection con, String codigoRc, String institucion) 
	{
		return getInterfazSistemaUnoDao().interfazConvenioPacienteRc(con, codigoRc, institucion);
	}

	
	private String claseClienteRc(Connection con, String codigoRc, String institucion) 
	{
		return getInterfazSistemaUnoDao().claseClienteRc(con, codigoRc, institucion);
	}
	

	private String descripConceptoPagoRc(Connection con, String codigoRc, String institucion) 
	{
		return getInterfazSistemaUnoDao().descripConceptoPagoRc(con, codigoRc, institucion);
	}



	private String conceptoPagoRc(Connection con, String codigoRc, String institucion) 
	{
		return getInterfazSistemaUnoDao().conceptoPagoRc(con, codigoRc, institucion);
	}



	private String cuentaPacienteRegimenRc(Connection con, String codigoRc, String institucion, String conceptoRc) 
	{
		return getInterfazSistemaUnoDao().cuentaPacienteRegimenRc(con, codigoRc, institucion, conceptoRc);
	}



	private String cuentaPacienteConvenioRc(Connection con, String codigoRc, String institucion, String conceptoRc) 
	{
		return getInterfazSistemaUnoDao().cuentaPacienteConvenioRc(con, codigoRc, institucion, conceptoRc);
	}



	private String cuentaRegimenRc(Connection con, String codigoRc, String institucion, String conceptoRc) 
	{
		return getInterfazSistemaUnoDao().cuentaRegimenRc(con, codigoRc, institucion, conceptoRc);
	}



	private String cuentaConvenioRc(Connection con, String codigoRc, String institucion, String conceptoRc) 
	{
		return getInterfazSistemaUnoDao().cuentaConvenioRc(con, codigoRc, institucion, conceptoRc);
	}



	private String conceptoIngTesoreriaRc(Connection con, String codigoRc, String institucion, String conceptoRc) 
	{
		return getInterfazSistemaUnoDao().conceptoIngTesoreriaRc(con, codigoRc, institucion, conceptoRc);
	}



	private String tipoConceptoRc(Connection con, String codigoRc, String institucion) 
	{
		return getInterfazSistemaUnoDao().tipoConceptoRc(con, codigoRc, institucion);
	}



	private HashMap cContableyNaturalezaRc(Connection con, int formaPago) 
	{
		return getInterfazSistemaUnoDao().cContableyNaturalezaRc(con, formaPago);
	}



	private HashMap formasPagoRc(Connection con, String codigoRc, String institucion) 
	{
		return getInterfazSistemaUnoDao().formasPagoRc(con, codigoRc, institucion);
	}



	private Object tipoPagoRc(Connection con, String codigoRc, String institucion) 
	{
		return getInterfazSistemaUnoDao().tipoPagoRc(con, codigoRc, institucion);
	}



	private Object codigoCajaRc(Connection con, String codigoRc, String institucion) 
	{
		return getInterfazSistemaUnoDao().codigoCajaRc(con, codigoRc, institucion);
	}



	private Object usuarioCajero(Connection con, String codigoRc, String institucion) 
	{
		return getInterfazSistemaUnoDao().usuarioCajero(con, codigoRc, institucion);
	}



	private Object valorTotalRc(Connection con, String codigoRc, String institucion) 
	{
		return getInterfazSistemaUnoDao().valorTotalRc(con, codigoRc, institucion);
	}



	private Object idPacienteRc(Connection con, String codigoRc, String institucion) 
	{
		return getInterfazSistemaUnoDao().idPacienteRc(con, codigoRc, institucion);
	}



	private Object centroAtencionRc(Connection con, String codigoRc, String institucion) 
	{
		return getInterfazSistemaUnoDao().centroAtencionRc(con, codigoRc, institucion);
	}



	public HashMap getCuentaContableRc() {
		return cuentaContableRc;
	}



	public void setCuentaContableRc(HashMap cuentaContableRc) {
		this.cuentaContableRc = cuentaContableRc;
	}


	public void setCuentaContableRc(String key, Object value) {
		this.cuentaContableRc.put(key, value);
	}



	public HashMap getFormasPagoMap() {
		return formasPagoMap;
	}



	public void setFormasPagoMap(HashMap formasPagoMap) {
		this.formasPagoMap = formasPagoMap;
	}
		
	
	public void setFormasPagoMap(String key, Object value) {
		this.formasPagoMap.put(key, value);
	}



	public HashMap getNombresPacienteMap() {
		return nombresPacienteMap;
	}

	public Object getNombresPacienteMap(String key) {
		return nombresPacienteMap.get(key);
	}

	public void setNombresPacienteMap(HashMap nombresPacienteMap) {
		this.nombresPacienteMap = nombresPacienteMap;
	}
	
	public void setNombresPacienteMap(String key, Object values) {
		this.nombresPacienteMap.put(key, values);
	}
	
}