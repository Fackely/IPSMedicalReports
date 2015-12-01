package com.princetonsa.mundo.facturacion;

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

import util.BackUpBaseDatos;
import util.ConstantesBD;
import util.InfoDatosString;
import util.UtilidadFileUpload;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.facturacion.ArchivoPlanoColsaDao;
import com.princetonsa.dto.facturacion.DtoArchivoPlanoColsa;
import com.princetonsa.dto.interfaz.DtoInterfazAxRips;


/**
 * 
 * @author Jose Eduardo Arias Doncel
 * 
 * */
public class ArchivoPlano
{ 
	
	//***********************Atributos
	
	/**
	 * HashMap de incosistencias
	 * */
	private HashMap inconsistenciasMap;
	
	/**
	 * String numero de la factura actual 
	 * */
	private String numeroFacturaActual;
	
	/**
	 * Array List posee los HashMap de inconsistencias generados por los
	 * archivos Planos Colsanitas, divididos por convenio.
	 * */
	private ArrayList<HashMap<String,Object>> InconsistenciasArray;
	
	
	public static Logger logger = Logger.getLogger(ArchivoPlano.class);	
	//**********************Metodos
	
	
	public static ArchivoPlanoColsaDao getArchivoPlanoColsaDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getArchivoPlanoColsaDao();
	}		
	
		
	/**
	 * Captura el Arraylist de Dtos de Facturas 
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public ArrayList<DtoArchivoPlanoColsa> getDtoFacturas(Connection con, 
		HashMap parametros,
		int baseConsulta)
			
	{		
		//almacena la informacion del archivo Plano
		ArrayList<DtoArchivoPlanoColsa> arrayArchivoPlano;		
		
		//captura las facturas del archivo plano
		arrayArchivoPlano = getArchivoPlanoColsaDao().getFacturasArchivoPlano(con, parametros, baseConsulta);
		
		logger.info("dtoFacturas tamaño >> "+arrayArchivoPlano.size());
		
		
		if(arrayArchivoPlano.size() > 0)								
			return arrayArchivoPlano;
		else
			return null;		
	}
	
	
	
	/**
	 * Genera el HashMap de Archivos Planos
	 * @param Connection con
	 * @param HashMap parametros
	 * @param HashMap convenios 
	 * */
	public HashMap getNombreArchivoPlano(Connection con,HashMap parametros,HashMap convenios)
	{
		int numConvenios = Utilidades.convertirAEntero(convenios.get("numRegistros").toString());
		int numNombreArchivo = 0;
		String fechaEnvio;
		String nombreArchivo = "";
		boolean existe = false;
		InfoDatosString infoInstitucion;
		
		HashMap nombreArchivos = new HashMap();
		nombreArchivos.put("numRegistros","0");
		nombreArchivos.put("indicadorRepetidos",ConstantesBD.acronimoNo);
		
		InfoDatosString info;		
			
		//capturo el mes y dia
		fechaEnvio = parametros.get("fechaEnvio").toString().substring(3,5);
		fechaEnvio+= parametros.get("fechaEnvio").toString().substring(0,2);
				
		//Inserta la informacion de la institucion 
		infoInstitucion = this.getInfoBasicaInstitucion(con,Utilidades.convertirAEntero(parametros.get("institucion").toString()));
		nombreArchivos.put("codigoPrestador",infoInstitucion.getNombre());
		nombreArchivos.put("nombreRazonSocial",infoInstitucion.getDescripcion());
				
		//Recorre los convenios
		for(int i=0; i<numConvenios; i++)
		{			
			existe = false;
			
			//verifica que no exista el convenio a generarse
			for(int j = 0; j < numConvenios; j++)
				if(nombreArchivos.containsKey("convenio_"+j) && nombreArchivos.get("convenio_"+j).equals(convenios.get("convenio_"+i)))
					existe = true;
					
			if(!existe)
			{
				nombreArchivos.put("convenio_"+i,convenios.get("convenio_"+i));
				nombreArchivos.put("descripcionConvenio_"+i,convenios.get("descripcionConvenio_"+i));
				nombreArchivos.put("identiInstitucion_"+i,ValoresPorDefecto.getIdentificadorInstitucionArchivosColsanitas(Utilidades.convertirAEntero(parametros.get("institucion").toString())));			
				info = this.getConvenioBasicoParametrizacion(con,Utilidades.convertirAEntero(convenios.get("convenio_"+i).toString()),Utilidades.convertirAEntero(parametros.get("institucion").toString()));		
				nombreArchivos.put("unidadEconomica_"+i,info.getCodigo());
				nombreArchivos.put("identifCompania_"+i,info.getNombre());
				nombreArchivos.put("identifPlan_"+i,info.getDescripcion().trim());
				nombreArchivos.put("identiEnvio_"+i,fechaEnvio);
				nombreArchivos.put("secuencia_"+i,parametros.get("secuencia"));
				nombreArchivos.put("ruta_"+i,parametros.get("ruta").toString().trim());
				nombreArchivos.put("sobreEscribir_"+i,ConstantesBD.acronimoNo);
				
				nombreArchivo = nombreArchivos.get("identiInstitucion_"+i).toString()+
								nombreArchivos.get("identifCompania_"+i).toString()+
								nombreArchivos.get("identifPlan_"+i).toString()+
								nombreArchivos.get("identiEnvio_"+i).toString()+
								nombreArchivos.get("secuencia_"+i).toString();	
				
				nombreArchivos.put("nombreArchivo_"+i,nombreArchivo);										
				
				if(UtilidadFileUpload.existeArchivo(parametros.get("ruta").toString().trim(),nombreArchivo+".txt"))
				{
					nombreArchivos.put("existe_"+i,ConstantesBD.acronimoSi);
					nombreArchivos.put("indicadorRepetidos",ConstantesBD.acronimoSi);
				}
				else
					nombreArchivos.put("existe_"+i,ConstantesBD.acronimoNo);
				
				numNombreArchivo++;
				nombreArchivos.put("numRegistros",numNombreArchivo);
			}
		}		
		
		return nombreArchivos;
	}
	

	/**
	 * Conforma el HashMap de dataFile en base a los numeros de Envio tomados de la interfaz AX_RIPS
	 * @param Connection con
	 * @param ArrayList<DtoInterfazAxRips>
	 * @param HashMap numerosSeleccionados
	 * */
	public HashMap interfazParametrosNumeroEnvio (
			Connection con,
			ArrayList<DtoInterfazAxRips> interfazRips,
			HashMap envioSeleccionados,
			HashMap parametrosBusqueda,
			HashMap nombreArchivosPlanosMap)
	{
		int numRegistros = Utilidades.convertirAEntero(envioSeleccionados.get("numRegistros").toString());
		String numeroFacturas = ""; 
		DtoInterfazAxRips dto;
		ArrayList<InfoDatosString> facturaConvenio;
		HashMap parametros = new HashMap();
		HashMap dataFile = new HashMap();
		
		//inicializa el valor del dataFile
		dataFile.put("numRegistros","0");
		
		//Datos comunes  		
		parametros.put("institucion",parametrosBusqueda.get("institucion"));
		parametros.put("fechaEnvio",parametrosBusqueda.get("fechaEnvio"));
		parametros.put("secuencia",parametrosBusqueda.get("secuencia"));
		parametros.put("ruta",parametrosBusqueda.get("ruta"));
		
		//Recorre los numero de envios seleccionados
		for(int i = 0; i < numRegistros; i++)
		{
			numeroFacturas = "";
			
			//consulta el dto del numero de envio
			dto = interfazRips.get(Utilidades.convertirAEntero(envioSeleccionados.get("indexArray_"+i).toString()));
			//captura las facturas convenios asociados al numero de envio 
			facturaConvenio = dto.getFacturaConvenio();
			
			//consulta las facturas asociadas al numero de envio y las agrupa en una lista separada con comas
			for(int j = 0; j <facturaConvenio.size() ; j++)			
				numeroFacturas += facturaConvenio.get(j).getNombre()+",";	
			
			//Quita de la lista la ultima coma 
			numeroFacturas = numeroFacturas.substring(0,numeroFacturas.length()-1);
			
			parametros.put("codigoFactura",numeroFacturas);
			parametros.put("convenio",dto.getCodigoConvenio().trim());
			
			logger.info("Parametros Numero Envio >> "+parametros);
			
			//llamado a la interfaz de Generacion de Archivos Planos
			dataFile.put("dtoFacturas_"+i,getDtoFacturas(con, parametros,1));
			dataFile.put("convenio_"+i,dto.getCodigoConvenio().trim());
			dataFile.put("archivo_"+i,this.getPosNombreArchivoPlano(nombreArchivosPlanosMap,dto.getCodigoConvenio()));
			dataFile.put("numRegistros",(i+1));			
		}
		
		return dataFile;
	}
	
	
	/**
	 * Almacen la Informacion del Dto de Facturas en un archivo 
	 * @param HashMap dataFile
	 * @param HashMap nombreArchivos
	 * @param ActionErrors errores 
	 * */
	public ActionErrors guardarArchivosPlanosColsa(HashMap dataFile,HashMap nombreArchivosMap,ActionErrors errores)
	{
		int numRegistrosDataFile = Utilidades.convertirAEntero(dataFile.get("numRegistros").toString());
		int posNombreArchivos = 0;
		int contadorRegistros = 0;
		String separadorRegis = "";
		String nombreArchivo = "";
		String registroRColsa = "";
		String registroSColsa = "";
		String aux="";		
		boolean indInconsistencia = false;
		ArrayList<DtoArchivoPlanoColsa> array;
		DtoArchivoPlanoColsa dto;
		
		
		//Inicializa el Array list que posee los HashMap de Inconsistencias
		this.InconsistenciasArray = new ArrayList<HashMap<String,Object>>();
		
		//Recorre los archivos a generar
		for(int i = 0; i< numRegistrosDataFile; i++)
		{
			posNombreArchivos = Utilidades.convertirAEntero(dataFile.get("archivo_"+i).toString());			
							
			//verifica que la posicion no sea Nula
			if(posNombreArchivos == ConstantesBD.codigoNuncaValido)
			{
				errores.add("descripcion",new ActionMessage("errors.invalid","Error al Cargar el Nombre de Archivo"));
				return errores;
			}		
				
								
			//Nombre del Archivo a Crear
			nombreArchivo = nombreArchivosMap.get("nombreArchivo_"+posNombreArchivos).toString();
			nombreArchivosMap.put("esGeneradoInconsitencia_"+posNombreArchivos,ConstantesBD.acronimoNo);
			contadorRegistros = 0;
			
			
			//Verifica si es posible escribir en el archivo
			if(nombreArchivosMap.get("existe_"+posNombreArchivos).toString().equals(ConstantesBD.acronimoNo) 
					|| (nombreArchivosMap.get("existe_"+posNombreArchivos).toString().equals(ConstantesBD.acronimoSi) && 
							nombreArchivosMap.get("sobreEscribir_"+posNombreArchivos).toString().equals(ConstantesBD.acronimoSi)))
			{
				try
				{
					try
					{
						//Apertura del archivo Colsanitas				
						File archivoColsa= new File(nombreArchivosMap.get("ruta_"+posNombreArchivos).toString(),nombreArchivo+".txt");					
						FileWriter streamColsa=new FileWriter(archivoColsa,false);
						BufferedWriter bufferAM=new BufferedWriter(streamColsa);						
						
						//Capturo el ArrayList donde se encuentran los Dto Registros, un Dto por registro
						array = (ArrayList<DtoArchivoPlanoColsa>)dataFile.get("dtoFacturas_"+i);

						//verifica que no este vacio						
						if(array!= null)
						{
							
							//Inicializa el HashMap de inconsistencias del Archivo a Generar
							this.inconsistenciasMap = new HashMap();
							this.inconsistenciasMap.put("archivoAsociado",posNombreArchivos);
							
							
							for(int j = 0; j < array.size(); j++)
							{
								//captura el Dto del Registro
								indInconsistencia = false;
								registroRColsa = "";
								registroSColsa = "";
								dto =  array.get(j);
								
								//captura de valores Fijos
								dto.setUnidadEconomica(nombreArchivosMap.get("unidadEconomica_"+posNombreArchivos).toString());							
								
								//Valida que exista el Dto
								if(dto != null)
								{
									
									//***********Inicia la captura Directa de los Campos del Registro**************************
									
									//***********Registro Tipo R
																	
									//CAMPO No 1. IDENTIFICADOR
									registroRColsa+= "R"+separadorRegis;									
									//-------------------------
									
									
									//CAMPO No 2. UNIDAD ECONOMICA
									aux = nombreArchivosMap.get("unidadEconomica_"+posNombreArchivos).toString();	
									if(aux.equals(""))
									{									
										indInconsistencia = true;
										
										this.setInconsistencias(dto.getCodigoFactura()+"",
												dto.getNumeroFactura()+"",
												dto.getCodigoPaciente()+"",
												dto.getNombrePaciente(),
												dto.getCuenta()+"",
												dto.getDescripcionViaIngreso(),
												'R',
												"Unidad Económica",
												"Campo Sin Datos");
									}
									else
									{
										if(aux.length() > 15)										
											aux = aux.substring(0,14);
																					
										registroRColsa+=completarFormato(aux,aux.length(),3,"alpha"," ")+separadorRegis;
									}
									//----------------------------
									
									
									//CAMPO No 3. CODIGO DE PRESTADOR
									aux = nombreArchivosMap.get("codigoPrestador").toString();									
									if(aux.equals(""))
									{										
										indInconsistencia = true;
										
										this.setInconsistencias(dto.getCodigoFactura()+"",
												dto.getNumeroFactura()+"",
												dto.getCodigoPaciente()+"",
												dto.getNombrePaciente(),
												dto.getCuenta()+"",
												dto.getDescripcionViaIngreso(),
												'R',
												"Código de Prestador",
												"Campo Sin Datos");
									}
									else if(aux.length() > 15)
									{
										indInconsistencia = true;
										
										this.setInconsistencias(dto.getCodigoFactura()+"",
												dto.getNumeroFactura()+"",
												dto.getCodigoPaciente()+"",
												dto.getNombrePaciente(),
												dto.getCuenta()+"",
												dto.getDescripcionViaIngreso(),
												'R',
												"Código de Prestador",
												"Tamaño Mayor a 15 Caracteres ");
									}
									else
									{
										registroRColsa+=completarFormato(aux,aux.length(),15,"alpha"," ")+separadorRegis;									
									}
									//----------------------------
									
																	
									//CAMPO No 4. NOMBRE o RAZON SOCIAL
									aux = nombreArchivosMap.get("nombreRazonSocial").toString();
									if(aux.equals(""))
									{
										indInconsistencia = true;
										
										this.setInconsistencias(dto.getCodigoFactura()+"",
												dto.getNumeroFactura()+"",
												dto.getCodigoPaciente()+"",
												dto.getNombrePaciente(),
												dto.getCuenta()+"",
												dto.getDescripcionViaIngreso(),
												'R',
												"Nombre o Razón Social",
												"Campo Sin Datos");
									}
									else
									{
										if(aux.length() > 50)										
											aux = aux.substring(0,50);
										
										registroRColsa+=completarFormato(aux,aux.length(),50,"alpha"," ").toUpperCase()+separadorRegis;																		
									}
									//--------------------------------
									
									
									//CAMPO No 5. NUMERO DE FACTURA
									aux = dto.getNumeroFactura();								
									if(aux.equals(""))
									{
										indInconsistencia = true;	
										
										this.setInconsistencias(dto.getCodigoFactura()+"",
												dto.getNumeroFactura()+"",
												dto.getCodigoPaciente()+"",
												dto.getNombrePaciente(),
												dto.getCuenta()+"",
												dto.getDescripcionViaIngreso(),
												'R',
												"Número de Factura",
												"Campo Sin Datos");
									}
									else
									{
										if(!dto.getPrefijoFactura().trim().equals(""))
											aux = dto.getPrefijoFactura().trim()+"-"+aux;
										
										registroRColsa+=completarFormato(aux,aux.length(),20,"alpha"," ")+separadorRegis;
									}
									//---------------------------------
																		
									//CAMPO No. 6 VALOR TOTAL FACTURA
									aux = dto.getValorTotalFactura()+"";									
									if(aux.equals("") || 
											aux.equals(ConstantesBD.codigoNuncaValido+""))
									{
										indInconsistencia = true;
										
										this.setInconsistencias(dto.getCodigoFactura()+"",
												dto.getNumeroFactura()+"",
												dto.getCodigoPaciente()+"",
												dto.getNombrePaciente(),
												dto.getCuenta()+"",
												dto.getDescripcionViaIngreso(),
												'R',
												"Valor Total Factura",
												"Campo Sin Datos");
									}
									else
									{
										aux = UtilidadTexto.formatearValores(aux,"######0.00"); 
										aux = aux.replace(".","");
										registroRColsa+=completarFormato(aux,aux.length(),14,"valor","0")+separadorRegis;
									}									
									//-----------------------------
									
									
									//CAMPO No. 7 VALOR RECAUDO POR CUOTAS MODERADORAS
									aux = dto.getValorRecaudoCuotasModeradoras()+"";
									if(dto.getTipoMonto().equals("2"))
									{
										if(aux.equals("") 
												|| aux.equals(ConstantesBD.codigoNuncaValido+""))
										{
											indInconsistencia = true;
											
											this.setInconsistencias(dto.getCodigoFactura()+"",
													dto.getNumeroFactura()+"",
													dto.getCodigoPaciente()+"",
													dto.getNombrePaciente(),
													dto.getCuenta()+"",
													dto.getDescripcionViaIngreso(),
													'R',
													"Valor Recaudo por Cuotas Moderadoras",
													"Campo Sin Datos");
										}
										else
										{
											aux = UtilidadTexto.formatearValores(aux,"######0.00");
											aux = aux.replace(".","");
											registroRColsa+=completarFormato(aux,aux.length(),14,"valor","0")+separadorRegis;
										}
									}
									else
									{
										registroRColsa+=completarFormato("",0,14,"valor","0")+separadorRegis;
									}								
									//------------------------------------------------
									
									
									//CAMPO No. 8 VALOR RECAUDO POR COPAGOS
									aux = dto.getValorRecaudoCopagos()+"";
									if(dto.getTipoMonto().equals("1"))
									{
										if(aux.equals("") 
												|| aux.equals(ConstantesBD.codigoNuncaValido+""))
										{
											indInconsistencia = true;
											
											this.setInconsistencias(dto.getCodigoFactura()+"",
													dto.getNumeroFactura()+"",
													dto.getCodigoPaciente()+"",
													dto.getNombrePaciente(),
													dto.getCuenta()+"",
													dto.getDescripcionViaIngreso(),
													'R',
													"Valor Recaudo por Copagos",
													"Campo Sin Datos");
										}
										else
										{
											aux = UtilidadTexto.formatearValores(aux,"######0.00");
											aux = aux.replace(".","");
											registroRColsa+=completarFormato(aux,aux.length(),14,"valor","0")+separadorRegis;
										}
									}
									else
									{
										aux = UtilidadTexto.formatearValores(aux,"######0.00");
										aux = aux.replace(".","");
										registroRColsa+=completarFormato("",0,14,"valor","0")+separadorRegis;
									}
									//-------------------------------------
									
									
									//CAMPO No. 9 VALOR RECAUDO POR PERIODOS MINIMOS DE COTIZACION
									registroRColsa+="00000000000000"+separadorRegis;
									//------------------------------------------------------------
									
									
									//CAMPO No. 10 FECHA INGRESO
									aux = dto.getFechaIngreso();
									if(aux.equals(""))
									{
										registroRColsa+="00000000"+separadorRegis;
									}
									else
									{
										registroRColsa+=aux+separadorRegis;	
									}
									//--------------------------
									
									
									//CAMPO No. 11 FECHA SALIDA
									aux = dto.getFechaSalida();
									if(aux.equals(""))
									{
										registroRColsa+="00000000"+separadorRegis;
									}
									else
									{
										registroRColsa+=aux+separadorRegis;	
									}
									//--------------------------
									
									
									//CAMPO No. 12 NUMERO DE SOLICITUD VOLANTE
									aux = dto.getNumeroSolicitudVolante();
									if(aux.equals("") || 
											aux.equals(ConstantesBD.codigoNuncaValido+""))
									{
										registroRColsa+="0000000000000000"+separadorRegis;
									}
									else
									{
										registroRColsa+=completarFormato(aux,aux.length(),16,"nume","0")+separadorRegis;																		
									}
									//----------------------------------------
									
									
									//CAMPO No. 13 FECHA ELABORACION FACTURA
									aux = dto.getFechaElaboracionFactura();									
									if(aux.equals(""))
									{
										indInconsistencia = true;
										
										this.setInconsistencias(dto.getCodigoFactura()+"",
												dto.getNumeroFactura()+"",
												dto.getCodigoPaciente()+"",
												dto.getNombrePaciente(),
												dto.getCuenta()+"",
												dto.getDescripcionViaIngreso(),
												'R',
												"Fecha Elaboracion Factura",
												"Campo Sin Datos");
									}
									else
									{
										registroRColsa+=aux+separadorRegis;
									}
									//--------------------------------------
									
									
									//CAMPO No. 14 SW DE CONTROL
									registroRColsa+="N";	
									//--------------------------
									
									
									//***********Registro Tipo S
									
									//CAMPO No. 1 IDENTIFICADOR
									registroSColsa+= "S"+separadorRegis;									
									//-------------------------
									
									
									//CAMPO No 2. CODIGO DE PRESTADOR
									aux = nombreArchivosMap.get("codigoPrestador").toString();									
									if(aux.equals(""))
									{
										indInconsistencia = true;
										
										this.setInconsistencias(dto.getCodigoFactura()+"",
												dto.getNumeroFactura()+"",
												dto.getCodigoPaciente()+"",
												dto.getNombrePaciente(),
												dto.getCuenta()+"",
												dto.getDescripcionViaIngreso(),
												'S',
												"Código de Prestador Socio",
												"Campo Sin Datos");
									}
									else if(aux.length() > 15)
									{
										indInconsistencia = true;
										
										this.setInconsistencias(dto.getCodigoFactura()+"",
												dto.getNumeroFactura()+"",
												dto.getCodigoPaciente()+"",
												dto.getNombrePaciente(),
												dto.getCuenta()+"",
												dto.getDescripcionViaIngreso(),
												'S',
												"Código de Prestador Socio",
												"Tamaño Mayor a 15 Caracteres ");
									}
									else
									{
										registroSColsa+=completarFormato(aux,aux.length(),15,"alpha"," ")+separadorRegis;									
									}
									//----------------------------
									
									
									//CAMPO No. 3 TIPO HONORARIO
									aux = dto.getIdentificador();									
									if(!aux.equals("HM") && !aux.equals("SA") && !aux.equals("SH") && !aux.equals("SU"))
									{
										indInconsistencia = true;
										
										this.setInconsistencias(dto.getCodigoFactura()+"",
												dto.getNumeroFactura()+"",
												dto.getCodigoPaciente()+"",
												dto.getNombrePaciente(),
												dto.getCuenta()+"",
												dto.getDescripcionViaIngreso(),
												'S',
												"Tipo de Honorario",
												"Dato no Valido debe ser (HM,SA,SH,SU)");
									}
									else
									{
										registroSColsa+=completarFormato(aux,aux.length(),2,"alpha"," ")+separadorRegis;
									}
									//--------------------------
									
									
									//CAMPO No. 4 VALOR FACTURADO SOCIO
									aux = dto.getValorTotalFactura()+"";
									if(aux.equals(""))
									{
										indInconsistencia = true;	
										
										this.setInconsistencias(dto.getCodigoFactura()+"",
												dto.getNumeroFactura()+"",
												dto.getCodigoPaciente()+"",
												dto.getNombrePaciente(),
												dto.getCuenta()+"",
												dto.getDescripcionViaIngreso(),
												'S',
												"Valor Facturado Socio",
												"Campo Sin Datos");
									}
									else
									{
										aux = UtilidadTexto.formatearValores(aux,"######0.00");
										aux = aux.replace(".","");
										registroSColsa+=completarFormato(aux,aux.length(),14,"valor","0");
									}															
									//---------------------------------	
								}	
								
								//***********FIN captura Directa de los Campos del Registro**************************								
																
								//Evalua que no hubieran existido inconsistencias
								if(!indInconsistencia)
								{
									registroRColsa+="\n";									
									bufferAM.write(registroRColsa);
									registroSColsa+="\n";
									bufferAM.write(registroSColsa);
									contadorRegistros++;			
								}
																
								
								//*****************************************************************************************
							}//Fin For
							
														
							//verifica si se escribieron registros en el archivo
							if(contadorRegistros > 0)
							{
								//Se indica que se genero el archivo asociado al Convenio
								nombreArchivosMap.put("esGenerado_"+posNombreArchivos,ConstantesBD.acronimoSi);
								bufferAM.close();
							}
							else
							{
								archivoColsa.delete();
								nombreArchivosMap.put("esGenerado_"+posNombreArchivos,ConstantesBD.acronimoNo);
								nombreArchivosMap.put("observacion_"+posNombreArchivos,"Los Registros no Cumplen con las Validaciones");
							}
							
							
							//Almacena el HashMap de Inconsistencias si Existieron
							if(this.inconsistenciasMap.containsKey("numRegistros") && 
									!this.inconsistenciasMap.get("numRegistros").equals("0"))
							{
								InconsistenciasArray.add(this.inconsistenciasMap);					
							}							
						}
						else
						{							
							//Se indica que no se genero el archivo asociado al Convenio
							archivoColsa.delete();
							nombreArchivosMap.put("esGenerado_"+posNombreArchivos,ConstantesBD.acronimoNo);									
							nombreArchivosMap.put("observacion_"+posNombreArchivos,"No se Encontraron Registros");							
						}						
					}					
					catch(FileNotFoundException e)
					{
						logger.error("No se pudo encontrar el archivo AM al generarlo: "+e);
						errores.add("descripcion",new ActionMessage("errors.invalid","Error al Cargar el Archivo "+nombreArchivo));		
						return errores;
					}
				}
				catch(IOException e)
				{
					e.printStackTrace();					
					logger.error("Error en los streams del archivo: "+e);
					return null;
				}			
			}
			else
			{
				logger.info("no es posible generar el archivo validaciones existe, sobreescribir");
				//Se indica que no se genero el archivo asociado al Convenio
				nombreArchivosMap.put("esGenerado_"+posNombreArchivos,ConstantesBD.acronimoNo);
				nombreArchivosMap.put("observacion_"+posNombreArchivos,"El Archivo ya Existe y No se Permitio Sobreescribir");
			}
		}			
		
		return errores;		
	}	
	
	
	
	
	/**
	 * Método que consulta el contenido de un archivo 
	 * @param nombreArchivoMap
	 * @return
	 */
	public HashMap cargarArchivo(HashMap nombreArchivosMap,String prefijo)
	{
		//objeto usado para llenar el contenido del archivo
		HashMap contenido=new HashMap();
		String separador=",";
		int pos = ConstantesBD.codigoNuncaValido;		
		
		pos = Utilidades.convertirAEntero(nombreArchivosMap.get("indicadorPos").toString());
		contenido.put("numRegistros","0");
		
		if(pos!= ConstantesBD.codigoNuncaValido)
		{	 
			 			
			try
			{
				int contador=0;
				String cadena="";
				
				//******SE INICIALIZA ARCHIVO*************************
				contenido.put("nombreArchivoMostrar",nombreArchivosMap.get("nombreArchivo"+prefijo+"_"+pos).toString()+".txt");
				
				File archivoColsa= new File(nombreArchivosMap.get("ruta_"+pos).toString(),nombreArchivosMap.get("nombreArchivo"+prefijo+"_"+pos).toString()+".txt");				
				FileReader stream=new FileReader(archivoColsa);
				BufferedReader buffer=new BufferedReader(stream);
				
				//********SE RECORRE LÍNEA POR LÍNEA**************
				cadena=buffer.readLine();
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
				logger.error("No se pudo encontrar el archivo "+nombreArchivosMap.get(prefijo+"nombreArchivo_"+pos).toString()+" al cargarlo: "+e);
				return contenido;
			}
			catch(IOException e)
			{
				logger.error("Error en los streams del archivo "+nombreArchivosMap.get(prefijo+"nombreArchivo_"+pos).toString()+" al cargarlo: "+e);
				return contenido;
			}
		}	
		
		return contenido;	
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
	public void setInconsistencias(
			String codigoFactura,
			String consecFactura,
			String idPaciente,
			String nombrePaciente,			
			String numeroCuenta,
			String descripcionViaIngreso,			
			char tipoRegistro,
			String nombreCampo,
			String observacion)
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
			
			
			//Almacena la informacion del encabezado			
			this.setInconsistenciasMap("noFactura_"+pos,consecFactura);
			this.setInconsistenciasMap("idPaciente_"+pos,idPaciente);
			this.setInconsistenciasMap("nombrePaciente_"+pos,nombrePaciente);
			this.setInconsistenciasMap("numeroCuenta_"+pos,numeroCuenta);
			this.setInconsistenciasMap("descripcionViaIngreso_"+pos,descripcionViaIngreso);
			
			//Campos para la administracion del HashMap			
			this.setInconsistenciasMap(codigoFactura,pos+"");
			this.setInconsistenciasMap("numElementos_"+pos,"1");
			
			//Almacena la informacion del detalle
			this.setInconsistenciasMap("tipoRegistro_"+pos+"_0",tipoRegistro);
			this.setInconsistenciasMap("nombreCampo_"+pos+"_0",nombreCampo);
			this.setInconsistenciasMap("observaciones_"+pos+"_0",observacion);
			
			//Aumenta una posicion del mapa
			this.setInconsistenciasMap("numRegistros",pos+1);			
		}
		else
		{
			int posEnca = Utilidades.convertirAEntero(this.getInconsistenciasMap(codigoFactura).toString());
			int numElementos = Utilidades.convertirAEntero(this.getInconsistenciasMap("numElementos_"+posEnca).toString());
			
			//Almacena la informacion del detalle
			this.setInconsistenciasMap("tipoRegistro_"+posEnca+"_"+numElementos,tipoRegistro);
			this.setInconsistenciasMap("nombreCampo_"+posEnca+"_"+numElementos,nombreCampo);
			this.setInconsistenciasMap("observaciones_"+posEnca+"_"+numElementos,observacion);
			
			this.setInconsistenciasMap("numElementos_"+posEnca,numElementos+1);
		}	
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
	 * Genera los archivos Planos "Fisicos" de Inconsistencias
	 * @param HashMap nombreArchivosPlanosMap
	 * @param ArrayList inconsistenciasArray
	 * */
	public HashMap guardarArchivosInconsistencia(HashMap nombreArchivosMap,ArrayList inconsistenciasArray)
	{
		int numHashMap = inconsistenciasArray.size();
		
		
		if(numHashMap <= 0)
			return null;
		
		HashMap temp;
		int indexNombreArchivosPlanosMap = 0;
		int numElementos = 0;
		
		for(int i=0; i<numHashMap; i++)
		{
			temp = (HashMap)inconsistenciasArray.get(i);		
						
			int numRegistros = Utilidades.convertirAEntero(temp.get("numRegistros").toString());			
			
			if(numRegistros > 0)
			{
				try
				{			
				
					//Captura la posicion del archivo asociado
					indexNombreArchivosPlanosMap = Utilidades.convertirAEntero(temp.get("archivoAsociado").toString());
					String nombreArchivo = "Incon"+nombreArchivosMap.get("nombreArchivo_"+indexNombreArchivosPlanosMap).toString();
					
					//Apertura del archivo Colsanitas				
					File archivoColsaIn= new File(nombreArchivosMap.get("ruta_"+indexNombreArchivosPlanosMap).toString(),nombreArchivo+".txt");					
					FileWriter streamColsaIn=new FileWriter(archivoColsaIn,false);
					BufferedWriter bufferAM=new BufferedWriter(streamColsaIn);		
					String registro = "";				
					String registroDetalle = "";
					String tmp = "";
					
					//Recorre el HashMap tomando los encabezados
					for(int k = 0; k < numRegistros; k++)
					{
						registro = "";
						registroDetalle ="";
						
						registro+="\n\n";
						registro+="No. Factura: "+temp.get("noFactura_"+k)+"\n";
						registro+="Tipo y Numero de Identificación del Paciente: "+temp.get("idPaciente_"+k)+"\n";
						registro+="Apellidos y Nombres del Paciente: "+temp.get("nombrePaciente_"+k)+"\n";
						registro+="No. Cuenta Paciente: "+temp.get("numeroCuenta_"+k)+"\n";
						registro+="Via de Ingreso: "+temp.get("descripcionViaIngreso_"+k)+"\n\n";
						registro+="Tipo Registro  Nombre del Campo                        Observaciones\n";
												
						//El numero de elementos del encabezado
						numElementos = Utilidades.convertirAEntero(temp.get("numElementos_"+k).toString());					 
						
						for(int j = 0; j < numElementos; j++)
						{
							tmp +="      "+temp.get("tipoRegistro_"+k+"_"+j).toString()+"        ";
							tmp +=temp.get("nombreCampo_"+k+"_"+j).toString();							
							tmp = this.completarFormato(tmp,tmp.length(),55, "alpha"," ");
							registroDetalle+=tmp+temp.get("observaciones_"+k+"_"+j).toString()+"\n";
							tmp = "";
						}						
															
						bufferAM.write(registro+registroDetalle);						
					}
					
					
					//Se indica que se genero el archivo de inconsistencias
					if(!registro.equals(""))
					{
						nombreArchivosMap.put("esGeneradoInconsitencia_"+indexNombreArchivosPlanosMap,ConstantesBD.acronimoSi);
						nombreArchivosMap.put("nombreArchivoIncon_"+indexNombreArchivosPlanosMap,nombreArchivo);					
						bufferAM.close();
					}
					else
					{
						archivoColsaIn.delete();	
					}
					
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}	
		}
		
		return nombreArchivosMap;
	}	
	
	
	/**
	 * Genera un archivo con extension ZIP
	 * @param String path
	 * @param String nombre
	 * */
	public static boolean generarArchivoZip(String path,String nombre)
	{
		String extZip = ".zip";
		String extTxt = ".txt";
		
		if(!path.endsWith("/"))
			path+="/";
		
		if(BackUpBaseDatos.EjecutarComandoSO("zip -j "+ValoresPorDefecto.getFilePath()+nombre+extZip+" "+path+nombre+extTxt) != ConstantesBD.codigoNuncaValido)
		{			    		
    		if(UtilidadFileUpload.existeArchivo(ValoresPorDefecto.getFilePath()+"", nombre+extZip))
    		{
    			logger.info("SE CREO EL ARCHIVO ZIP "+ValoresPorDefecto.getFilePath()+""+nombre+extZip);
    			return true;
    		}
    		else
    			logger.info("NO EXISTE EL ARCHIVO ZIP. zip "+ValoresPorDefecto.getFilePath()+""+nombre+extZip+" "+path+nombre+extTxt);
		}
    		
		logger.info("NO SE CREO EL ARCHIVO ZIP. zip "+ValoresPorDefecto.getFilePath()+""+nombre+extZip+" "+path+nombre+extTxt);
    	return false;	    
	}
	
	/**
	 * Genera un archivo con extension ZIP
	 * @param String path
	 * @param String nombre
	 * return la Ruta y nombre del archiv
	 * */
	public static String generarArchivoZipString(String path,String nombre)
	{
		String extZip = ".zip";
		String extTxt = ".txt";
		String ruta="";
		
		if(!path.endsWith("/"))
			path+="/";
		
		if(BackUpBaseDatos.EjecutarComandoSO("zip -j "+ValoresPorDefecto.getFilePath()+nombre+extZip+" "+path+nombre) != ConstantesBD.codigoNuncaValido)
		{			    		
    		if(UtilidadFileUpload.existeArchivo(ValoresPorDefecto.getFilePath()+"", nombre+extZip))
    		{
    			logger.info("SE CREO EL ARCHIVO ZIP "+ValoresPorDefecto.getFilePath()+""+nombre+extZip);
    			ruta=System.getProperty("ADJUNTOS")+System.getProperty("file.separator")+nombre+extZip;
    			return ruta;
    		}
    		else
    			logger.info("NO EXISTE EL ARCHIVO ZIP. zip "+ValoresPorDefecto.getFilePath()+""+nombre+extZip+" "+path+nombre+extTxt);
    	
    		
		}
    		
		logger.info("NO SE CREO EL ARCHIVO ZIP. zip "+ValoresPorDefecto.getFilePath()+""+nombre+extZip+" "+path+nombre+extTxt);
		
    	return ruta;	    
	}

	/**
	 * Devuelve la posicion dentro del HashMap de Nombre de Archivo plano donde se encuentra la informacion del archivo a crear 
	 * @param String Codigo convenio
	 * */
	public int getPosNombreArchivoPlano(HashMap nombreArchivosPlanosMap,String convenio)
	{		
		for(int i = 0; i < Utilidades.convertirAEntero(nombreArchivosPlanosMap.get("numRegistros").toString()); i++ )
			if(nombreArchivosPlanosMap.containsKey("convenio_"+i)
					&& nombreArchivosPlanosMap.get("convenio_"+i).toString().equals(convenio))
				return i;
				
		return ConstantesBD.codigoNuncaValido;
	}
	
	/**
	 * Consulta datos basicos de los convenios ingresados en la parametrica Archivo Planos Colsanitas
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public InfoDatosString getConvenioBasicoParametrizacion(Connection con, int convenio, int institucion)
	{
		return getArchivoPlanoColsaDao().getConvenioBasicoParametrizacion(con,convenio,institucion);
	}
	
	/**
	 * Inicializa los valores para realizar la busqueda
	 * @param Connection con
	 * @param HashMap busquedaMap
	 * @param int institucion
	 * */
	public static HashMap incializarBusquedaPlanos(Connection con,int institucion)
	{
		return getArchivoPlanoColsaDao().getUltimoHistorialArchivosPlanos(con, institucion);	
	}
	
	
	/**
	 * Consulta la ruta del ultimo Registro del Historial de Archivos Planos
	 * @param Connection con
	 * @param int institucion
	 * */
	public static String getUltimaRutaHistorialArchivosPlanos(Connection con,int institucion)
	{
		return getArchivoPlanoColsaDao().getUltimaRutaHistorialArchivosPlanos(con, institucion);		
	}
	
	
	/**
	 * Consulta la ruta del ultimo Registro del Historial de Archivos Planos
	 * @param Connection con
	 * @param int institucion
	 * */
	public static ArrayList<HashMap<String,Object>> getConveniosArchivosPlanos(Connection con,int institucion)
	{
		return getArchivoPlanoColsaDao().getConveniosArchivosPlanos(con, institucion);
	}
	
	/**
	 * Consulta las cuentas de cobro
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public static HashMap getCuentasCobro(Connection con, HashMap parametros)
	{
		return getArchivoPlanoColsaDao().getCuentasCobro(con, parametros);
	}	
	
	
	/**
	 * Insertar en Historial de Archivos Planos
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public static boolean setHistorialArhivosPlanos(Connection con,HashMap parametros)
	{
		return getArchivoPlanoColsaDao().setHistorialArhivosPlanos(con,parametros);
	}
	
		
	/**
	 * Consulta datos basicos de las instituciones
	 * @param Connection con
	 * @param int institucion
	 * */
	public InfoDatosString getInfoBasicaInstitucion(Connection con,int institucion)
	{
		return getArchivoPlanoColsaDao().getInfoBasicaInstitucion(con, institucion);
	}


	/**
	 * @return the inconsistenciasMap
	 */
	public HashMap getInconsistenciasMap() {
		return inconsistenciasMap;
	}


	/**
	 * @param inconsistenciasMap the inconsistenciasMap to set
	 */
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


	/**
	 * @return the numeroFacturaActual
	 */
	public String getNumeroFacturaActual() {
		return numeroFacturaActual;
	}


	/**
	 * @param numeroFacturaActual the numeroFacturaActual to set
	 */
	public void setNumeroFacturaActual(String numeroFacturaActual) {
		this.numeroFacturaActual = numeroFacturaActual;
	}


	/**
	 * @return the inconsistenciasArray
	 */
	public ArrayList<HashMap<String, Object>> getInconsistenciasArray() {
		return InconsistenciasArray;
	}


	/**
	 * @param inconsistenciasArray the inconsistenciasArray to set
	 */
	public void setInconsistenciasArray(
			ArrayList<HashMap<String, Object>> inconsistenciasArray) {
		InconsistenciasArray = inconsistenciasArray;
	}
}