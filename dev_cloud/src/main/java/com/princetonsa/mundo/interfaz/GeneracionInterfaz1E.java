package com.princetonsa.mundo.interfaz;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadFileUpload;
import util.UtilidadTexto;
import util.ValoresPorDefecto;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.interfaz.GeneracionInterfaz1EDao;
import com.princetonsa.dto.interfaz.DtoInterfazCampoS1E;
import com.princetonsa.dto.interfaz.DtoInterfazLineaS1E;
import com.princetonsa.dto.interfaz.DtoInterfazS1EInfo;
import com.princetonsa.dto.interfaz.DtoLogInterfaz1E;
import com.princetonsa.dto.interfaz.DtoTipoMovimiento;
import com.princetonsa.dto.interfaz.DtoTipoMovimientoArchivo;
import com.princetonsa.mundo.UsuarioBasico;

public class GeneracionInterfaz1E
{
	static Logger logger = Logger.getLogger(GeneracionInterfaz1E.class);
	
	public static final String indicadorArchivoRecaudos = "REC";	
	public static final String indicadorArchivoRecaudosInco = "IncREC";
	
	public static final String indicadorArchivoEntidadesExt = "EXT";
	public static final String indicadorArchivoEntidadesExtInco = "IncEXT";
	
	public static final String indicadorArchivoHonoInt = "HON";
	public static final String indicadorArchivoHonoIntInco = "IncHON";
	
	public static final String indicadorArchivoVenta = "VTA";
	public static final String indicadorArchivoVentaInco = "IncVTA";
	
	public static final String indicadorArchivoAjuste = "AJU";
	public static final String indicadorArchivoAjusteInco = "IncAJU";
	
	public static final String indicadorArchivoCartera = "CAR";
	public static final String indicadorArchivoCarteraInco = "IncCAR";
	
	public static final String indicadorLineaInicio = "0";
	public static final String indicadorLineaInconsGeneral = "1";
	public static final String indicadorLineaDocumentoContable = "2";
	public static final String indicadorLineaDetMovDocumentoContable = "3";
	public static final String indicadorLineaDetCxC = "4";
	public static final String indicadorLineaDetCxP = "5";
	public static final String indicadorLineaFinal = "6";
	public static final String indicadorLineaEventoCartera = "7";
	
	public static final String indicadorTipoInconDatos = "Inconsistencia Datos. ";
	
	public static final String codigoTipoNumerico = "1";
	public static final String codigoTipoAlfanumerico = "2";
	public static final String codigoTipoNumValorMov = "3";
	public static final String codigoTipoFecha = "4";
	public static final String codigoTipoHora = "5";
	
	private ArrayList<DtoInterfazLineaS1E> arrayLineas;
	private ArrayList<String> inconsistenciasGenerales;
	private DtoInterfazS1EInfo dtoInfoInterfaz;
	
	public static final String acronimoTipoMovVentasInte = "VTA";
	public static final String acronimoTipoMovHonoraInte = "HON";
	
	/**
	 * 
	 * */
	public static GeneracionInterfaz1EDao getDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getGeneracionInterfaz1EDao();
	}
	
	/**
	 * Generara los archivos planos
	 * @param Connection con
	 * @param DtoInterfazS1EInfo dtoInfo
	 * */
	public DtoInterfazS1EInfo dep_generarArchivosInterfaz(Connection con,DtoInterfazS1EInfo dtoInfo)
	{
		dtoInfoInterfaz = dtoInfo;
		HashMap<String, Object> resultado = new HashMap<String, Object>();
		ActionErrors errores = new ActionErrors();
		boolean existeInconEnLineas = false;
		resultado.put("exito","false");
		resultado.put("error",errores);
		UtilidadBD.iniciarTransaccion(con);
		
		//Recorre los tipos de movimientos seleccionados
		for(DtoTipoMovimiento dtoTipoMov : dtoInfoInterfaz.getTipoMovimientos())
		{
			if(dtoTipoMov.isSeleccionado())
			{
				for(DtoTipoMovimientoArchivo archivo : dtoTipoMov.archivos)
				{
					arrayLineas = new ArrayList<DtoInterfazLineaS1E>();
					existeInconEnLineas = false;
					
					//genera las lineas de inicio,encabezado,detalle y fin
					
					try 
					{
						DtoTipoMovimiento dtoTipoMovSelec = new DtoTipoMovimiento();
						PropertyUtils.copyProperties(dtoTipoMovSelec,dtoTipoMov);
						dtoInfoInterfaz.setTipoMovimientoSeleccionado(dtoTipoMovSelec);
						dtoInfoInterfaz.getTipoMovimientoSeleccionado().setCodigo(archivo.getIndicadorTipoMov());		
						
					}catch(Exception e){
						logger.warn(e);
					}
					
					arrayLineas.addAll(consultarDocumentosContables(con, dtoInfoInterfaz));
					
					//la inconsistencia general se toma de la linea de inicio
					
					if(arrayLineas.size() > 0)
						existeInconEnLineas = arrayLineas.get(0).isExisteInconsistencia();
					
					//arrayLineas
					if(existeInconEnLineas)
						this.dtoInfoInterfaz.setExisteInconsistenciArchivos(existeInconEnLineas);
					
					logger.info("..:Resumen movimiento: >> "+dtoTipoMov.getDescripcion()+" numero de lineas >> "+arrayLineas.size()+" existenincon >> "+existeInconEnLineas);
					
					//genera archivo plano en servidor
					dtoTipoMov = dep_generarArchivoPlano(arrayLineas, existeInconEnLineas,dtoTipoMov,archivo.getIndicadorTipoMov());
				}
			}
		}
		
		//Si existieron inconsistencias debo eliminar los archivos generados de la interfaz
		if(this.dtoInfoInterfaz.isExisteInconsistenciArchivos())
		{
			dep_eliminarArchivos();
			UtilidadBD.abortarTransaccion(con);
		}
		else
		{
			UtilidadBD.finalizarTransaccion(con);
		}
		
		//verifica si se generaron documentos
		this.dtoInfoInterfaz.setExisteInfoArchivos(verificarExistenArchivosCreados(this.dtoInfoInterfaz.getTipoMovimientos()));

		return this.dtoInfoInterfaz;
	}
	
	
	/**
	 * elimina archivos
	 * @param DtoTipoMovimiento tipoMov
	 * */
	private void dep_eliminarArchivos()
	{
		for(DtoTipoMovimiento tipoMov : this.dtoInfoInterfaz.getTipoMovimientos())
		{
			if(tipoMov.isSeleccionado())
			{
				for(DtoTipoMovimientoArchivo archivo : tipoMov.getArchivos())
				{
					if(archivo.isGenerado())
					{
						try
						{
							File archivoEliminar = new File(archivo.getPathArchivo(),archivo.getNombreArchivo());
							archivoEliminar.delete();
							archivo.setGenerado(false);
						}catch(Exception e) {
							logger.info("No se encontro el archivo "+archivo.getPathArchivo()+" "+archivo.getNombreArchivo()+" "+e);
						}
					}
				}
			}
		}
	}
	
	/**
	 * verifica que exista informacion de archivos creados
	 * @param ArrayList<DtoTipoMovimiento> arrayTipoMov
	 * */
	public boolean verificarExistenArchivosCreados(ArrayList<DtoTipoMovimiento> arrayTipoMov)
	{
		for(DtoTipoMovimiento tipoMov : this.dtoInfoInterfaz.getTipoMovimientos())
		{
			if(tipoMov.isSeleccionado())
			{
				for(DtoTipoMovimientoArchivo archivo : tipoMov.getArchivos())
				{
					if(archivo.isGenerado())
						return true;
				}
				
				for(DtoTipoMovimientoArchivo archivo : tipoMov.getArchivosIncon())
				{
					if(archivo.isGenerado())
						return true;
				}
			}
		}
		
		return false;
	}
	
	/**
	 * Añade un inconsistencia al listado de inconsistencias
	 * @param  String contenidoGeneral
	 * */
	private void dep_adicionarInconsitenciaGeneral(String contenidoGeneral)
	{
		inconsistenciasGenerales.add(contenidoGeneral);
	}
	
	/**
	 * Genera el archivo plano
	 * @param ArrayList<DtoInterfazLineaS1E> lineas
	 * */
	private DtoTipoMovimiento dep_generarArchivoPlano(
			ArrayList<DtoInterfazLineaS1E> lineas,
			boolean existeInconEnLineas,
			DtoTipoMovimiento tipoMov,
			String acronimoTipoMovEvaluado)
	{
		String lineaBuffer = "";
		int numeroLinea = 0;
		DtoTipoMovimientoArchivo archivoSeleccionado = new DtoTipoMovimientoArchivo();
		int posArchivoSeleccion = ConstantesBD.codigoNuncaValido;

		if(lineas.size() > 0)
		{	
			try
			{
				try
				{	
					if(existeInconEnLineas)
					{
						for(DtoTipoMovimientoArchivo archivo : tipoMov.getArchivos())
							archivo.setGenerado(false);
						
						//Tipo de Movimiento
						if(acronimoTipoMovEvaluado.equals(ConstantesIntegridadDominio.acronimoTipoMovRecaudos))
							posArchivoSeleccion = tipoMov.getPosArchivoIncon(indicadorArchivoRecaudosInco);
						else if(acronimoTipoMovEvaluado.equals(ConstantesIntegridadDominio.acronimoTipoMovServiciEntidaExter))
							posArchivoSeleccion = tipoMov.getPosArchivoIncon(indicadorArchivoEntidadesExtInco);
						else if(acronimoTipoMovEvaluado.equals(ConstantesIntegridadDominio.acronimoTipoMovAjusteReclasi))
							posArchivoSeleccion = tipoMov.getPosArchivoIncon(indicadorArchivoAjusteInco);
						else if(acronimoTipoMovEvaluado.equals(ConstantesIntegridadDominio.acronimoTipoMovCartera))
							posArchivoSeleccion = tipoMov.getPosArchivoIncon(indicadorArchivoCarteraInco);
						//Acronimo para el tipo de movimiento Ventas/Honorarios
						else if(acronimoTipoMovEvaluado.equals(acronimoTipoMovVentasInte))
							posArchivoSeleccion = tipoMov.getPosArchivoIncon(indicadorArchivoVentaInco);
						else if(acronimoTipoMovEvaluado.equals(acronimoTipoMovHonoraInte))
							posArchivoSeleccion = tipoMov.getPosArchivoIncon(indicadorArchivoHonoIntInco);
						
						
						archivoSeleccionado = tipoMov.getArchivosIncon().get(posArchivoSeleccion);
						archivoSeleccionado.setPathArchivo(this.dtoInfoInterfaz.getPathArchivoInconsis());
					}
					else
					{
						//evalua si existen inconsistencias Anteriores
						if(!this.dtoInfoInterfaz.isExisteInconsistenciArchivos())
						{
							//Tipo de Movimiento 
							if(tipoMov.getCodigo().equals(ConstantesIntegridadDominio.acronimoTipoMovRecaudos))
								posArchivoSeleccion = tipoMov.getPosArchivo(indicadorArchivoRecaudos);
							else if(tipoMov.getCodigo().equals(ConstantesIntegridadDominio.acronimoTipoMovServiciEntidaExter))
								posArchivoSeleccion = tipoMov.getPosArchivo(indicadorArchivoEntidadesExt);
							else if(tipoMov.getCodigo().equals(ConstantesIntegridadDominio.acronimoTipoMovAjusteReclasi))
								posArchivoSeleccion = tipoMov.getPosArchivo(indicadorArchivoAjuste);
							else if(tipoMov.getCodigo().equals(ConstantesIntegridadDominio.acronimoTipoMovCartera))
								posArchivoSeleccion = tipoMov.getPosArchivo(indicadorArchivoCartera);
							//Acronimo para el tipo de movimiento Ventas/Honorarios
							else if(acronimoTipoMovEvaluado.equals(acronimoTipoMovVentasInte))
								posArchivoSeleccion = tipoMov.getPosArchivo(indicadorArchivoVenta);
							else if(acronimoTipoMovEvaluado.equals(acronimoTipoMovHonoraInte))
								posArchivoSeleccion = tipoMov.getPosArchivo(indicadorArchivoHonoInt);
							

							if(posArchivoSeleccion >= 0)
							{
								archivoSeleccionado = tipoMov.getArchivos().get(posArchivoSeleccion);
								archivoSeleccionado.setPathArchivo(this.dtoInfoInterfaz.getPathArchivoInterfaz());
							}
							else
								logger.info("..:No se encontro el archivo con el indicador "+tipoMov.getDescripcion());
						}
						else
						{
							for(DtoTipoMovimientoArchivo archivo : tipoMov.getArchivos())
								archivo.setGenerado(false);
							
							return tipoMov; 
						}
					}
					
					//Apertura del archivo
					if(!archivoSeleccionado.getNombreArchivo().equals(""))
					{
						File archivoInterfaz  = new File(archivoSeleccionado.getPathArchivo(),archivoSeleccionado.getNombreArchivo());
						OutputStreamWriter stream = new OutputStreamWriter(new FileOutputStream(archivoInterfaz), "ISO-8859-1");
						BufferedWriter buffer = new BufferedWriter(stream);
						lineaBuffer = "";
				
						numeroLinea = 0; 
						for(DtoInterfazLineaS1E linea : lineas)
						{
							lineaBuffer = "";
							//si existen inconsistencias solo puedo buscar las lineas con inconsistencias
							if(existeInconEnLineas)
							{								
								if(linea.isExisteInconsistencia())								
									lineaBuffer = getLineaIncon(linea);							
																
								//Recorro el detalle 
								for(DtoInterfazLineaS1E lineaDetalle : linea.getArrayDetalle())								
									lineaBuffer += getLineaIncon(lineaDetalle);								
							}
							else
							{
								lineaBuffer = getLinea(linea)+"\r\n";
								
								//Recorro el detalle 
								for(DtoInterfazLineaS1E lineaDetalle : linea.getArrayDetalle())
									lineaBuffer += getLinea(lineaDetalle)+"\r\n";
							}
							
							if(!lineaBuffer.equals(""))
							{
								buffer.write(lineaBuffer);
								numeroLinea++;
							}
						}
						
						if(numeroLinea>0)
						{
							logger.info("..:Archivo a generar >> "+this.dtoInfoInterfaz.getPathArchivoInterfaz()+" "+archivoSeleccionado.getNombreArchivo());
							
							archivoSeleccionado.setGenerado(true);
							archivoSeleccionado.setFechaGeneracion(UtilidadFecha.getFechaActual());
							archivoSeleccionado.setHoraGeneracion(UtilidadFecha.getHoraActual());
							if(existeInconEnLineas)
								tipoMov.getArchivosIncon().set(posArchivoSeleccion,archivoSeleccionado);
							else
								tipoMov.getArchivos().set(posArchivoSeleccion,archivoSeleccionado);
							
							buffer.close();
						}
						else
						{
							logger.info("..:NO GENERO Archivo >> "+this.dtoInfoInterfaz.getPathArchivoInterfaz()+" "+archivoSeleccionado.getNombreArchivo());
							archivoSeleccionado.setGenerado(false);
							archivoInterfaz.delete();
						}
					}
					else
						logger.info("No se encontro nombre de archivo");
				}
				catch (Exception e) {
					logger.error("No se pudo encontrar el archivo al generarlo: "+e);
				}
			}catch (Exception e) {
				logger.error("Error en los streams del archivo: "+e);
			}
		}
		
		return tipoMov;
	}
	
	
	/**
	 * Arma una linea
	 * @param DtoInterfazLineaS1E linea
	 * */
	public String getLinea(DtoInterfazLineaS1E linea)
	{
		String resultado = "";		
		for(DtoInterfazCampoS1E campos : linea.getArrayCampos())
		{
			resultado +=  completarFormato(campos.getValor(),
					campos.getValor().length(),
					campos.getTamano(),
					campos.getTipo(),
					"");
		}
		
		return resultado;
	}
	
	/**
	 * Arma una linea
	 * @param DtoInterfazLineaS1E linea
	 * */
	public String getLineaIncon(DtoInterfazLineaS1E linea)
	{
		String resultado = "";		

		for(DtoInterfazCampoS1E campos : linea.getArrayCampos())
		{
			if(campos.isInconsistencia())
			{			
				resultado +=
					campos.getTipoDocumento()+" "+
					campos.getNumeroDocumento()+" "+
					campos.getTipoInconsistencia().getDescripcion()+" "+
					"["+campos.getDescripcionLinea()+"] "+
					campos.getDescripcionIncon()+"\r\n";
				
				// Si la inconcistencia se presenta en un recibo de caja se debe mostrar el consecutivo del documento en vez del numero del documento
				if (campos.getTipoDocumento().equals("Recibos de Caja")||campos.getTipoDocumento().equals("Recibos de Caja "))
				{	resultado="";
					resultado +=
				campos.getTipoDocumento()+" "+
				campos.getConsecutivoDocumento()+" "+
				campos.getTipoInconsistencia().getDescripcion()+" "+
				"["+campos.getDescripcionLinea()+"] "+
				campos.getDescripcionIncon()+"\r\n";}
			}
			else 
			{
				if(!campos.getDescripcionIncon().equals(""))
					logger.info("valor de la incon >> "+campos.getDescripcionIncon());
			}
		}
		
		return resultado;
	}
	
	/**
	 * Completa una cadena dada con ceros o espacios dependiendo de se tipo de dato
	 * @param String datp
	 * @param int maxTamano
	 * @param String tipo (alpha,nume,valor)
	 * @param valorRelleno
	 * */
	public String completarFormato(
			String dato,
			int tamano,
			int maxTamano,
			String tipo,
			String valorRelleno)
	{
		String aux ="";
		
		try
		{
			if(tipo.equals(codigoTipoAlfanumerico))
			{
				if(valorRelleno.equals(""))
					valorRelleno = " ";
				
				dato = UtilidadTexto.cambiarCaracteresEspeciales(dato);
				dato = UtilidadTexto.observacionATextoPlano(dato);
				
				if(dato.length() > maxTamano)
					dato = dato.substring(0,maxTamano);
				
				for(int i=tamano; i<maxTamano; i++)
					aux+=valorRelleno;
				
				return dato+aux;
			}
			else if(tipo.equals(codigoTipoNumerico))
			{
				if(valorRelleno.equals(""))
					valorRelleno = "0";
				
				for(int i = tamano; i < maxTamano; i++)
					aux+=valorRelleno;
				
				return aux+dato;
			}
			else if(tipo.equals(codigoTipoNumValorMov))
			{
				if(!dato.equals("+000000000000000.0000"))
				{
					String datoN [] = dato.split("\\.");
					String entero = "";
					valorRelleno = "0";
					
					if(datoN.length <= 1)
						entero = dato;
					else
						entero = datoN [0];
					
					int limite = 15;					
					if(maxTamano == 20)
						limite = 14;
						
					for(int i=entero.length(); i<limite; i++)
						aux+=valorRelleno;
					
					entero = aux+entero;
					
					if(datoN.length>1)
					{
						aux = "";
						valorRelleno = "0";
						
						for(int i=datoN[1].length(); i<=3; i++)
							aux += valorRelleno;
						
						aux = "."+datoN[1]+aux;
					}
					else
						aux = ".0000";
					
					dato = "+"+entero+aux;
				}
			}
			else if(tipo.equals(codigoTipoFecha))
			{	
				String datoN [] = dato.split("/");
				
				if(datoN.length == 3)
				{
					dato = datoN[2]+""+datoN[1]+""+datoN[0]; 
				}
				else
					dato = "        ";
			}
		}catch (Exception e) {
			logger.info("valor del error >> "+e+" >> dato "+dato+" >> tamano >> "+tamano+" >> maxTamano "+maxTamano+" >> tipo >> "+tipo +" >> valorRelleno "+valorRelleno);
		}
		
		return dato;
	}
	
	/**
	 * validaciones para la generacion del archivo plano
	 * @param DtoInterfazS1EInfo dto
	 * */
	public static ActionErrors validarCamposGeneracionArchivo(DtoInterfazS1EInfo dto)
	{
		ActionErrors errores = new ActionErrors();
		boolean existenTipo = false;
		
		for(int i=0; i < dto.getTipoMovimientos().size(); i++)
		{
			if(dto.getTipoMovimientos().get(i).isSeleccionado())
					existenTipo = true;
		}
		
		if(!existenTipo)
			errores.add("descripcion",new ActionMessage("errors.notEspecific","Debe Seleccionar por lo menos un Tipo de Movimiento."));
		
		if(dto.getFechaProceso().equals(""))
		{
			errores.add("descripcion",new ActionMessage("errors.required","La Fecha Proceso "));
		}
		else
		{
			if(!UtilidadFecha.validarFecha(dto.getFechaProceso()))				
				errores.add("descripcion",new ActionMessage("errors.formatoFechaInvalido"," Proceso "+dto.getFechaProceso()+" "));
			else
			{
				if(!UtilidadFecha.compararFechas(UtilidadFecha.getFechaActual(),"00:00",dto.getFechaProceso(),"00:00").isTrue())
				 	errores.add("descripcion",new ActionMessage("errors.fechaPosteriorIgualActual"," Proceso "+dto.getFechaProceso(),UtilidadFecha.getFechaActual()));
			}			
		}
		
		if(dto.getPathArchivoInterfaz().equals(""))
			errores.add("descripcion",new ActionMessage("errors.required","El Path Archivo Interfaz"));
		else
		{    				
			if(!UtilidadFileUpload.validarExistePath(dto.getPathArchivoInterfaz()))
				errores.add("descripcion",new ActionMessage("errors.notEspecific"," Verifique que Exista el Path Archivo Interfaz. "+dto.getPathArchivoInterfaz()));
		}
		
		if(dto.getPathArchivoInconsis().equals(""))
			errores.add("descripcion",new ActionMessage("errors.required","El Path Archivo de Inconsistencias"));
		else
		{    				
			if(!UtilidadFileUpload.validarExistePath(dto.getPathArchivoInconsis()))
				errores.add("descripcion",new ActionMessage("errors.notEspecific"," Verifique que Exista el Path Archivo de Inconsistencias. "+dto.getPathArchivoInconsis()));
		}
		
		return errores;
	}
	
	
	/**
	 * Arma los nombre los archivos a generar
	 * @param DtoInterfazS1EInfo dto
	 * */
	public static DtoInterfazS1EInfo getNombreArchivos(DtoInterfazS1EInfo dto)
	{
		dto.setPreguntarArchivosSobreEsc(false);
		for(int i = 0; i<dto.getTipoMovimientos().size(); i++)
		{	
			if(dto.getTipoMovimientos().get(i).isSeleccionado())
			{
				dto.getTipoMovimientos().get(i).setArchivos(new ArrayList<DtoTipoMovimientoArchivo>());
				dto.getTipoMovimientos().get(i).setArchivosIncon(new ArrayList<DtoTipoMovimientoArchivo>());
				
				if(dto.getTipoMovimientos().get(i).getCodigo().equals(ConstantesIntegridadDominio.acronimoTipoMovVentasHonoraInte))
				{
					//Archivo Venta
					DtoTipoMovimientoArchivo archivoIncon = new DtoTipoMovimientoArchivo();
					DtoTipoMovimientoArchivo archivo = new DtoTipoMovimientoArchivo();
					
					archivo.setPreguntarSobreEscribir(false);
					archivo.setSobreEscribir(false);
					archivoIncon.setPreguntarSobreEscribir(false);
					archivoIncon.setSobreEscribir(false);
					
					//asigna el subtipo de movimiento
					archivo.setIndicadorTipoMov(acronimoTipoMovVentasInte);
					archivoIncon.setIndicadorTipoMov(acronimoTipoMovVentasInte);
					
					archivo.setIndicadorArchivo(indicadorArchivoVenta);
					archivoIncon.setIndicadorArchivo(indicadorArchivoVentaInco);
					archivo.setNombreArchivo(indicadorArchivoVenta+dto.getFechaProcesoDdMmAaaa());
					archivoIncon.setNombreArchivo(indicadorArchivoVentaInco+dto.getFechaProcesoDdMmAaaa());
	
					if(UtilidadFileUpload.existeArchivo(dto.getPathArchivoInterfaz(),archivo.getNombreArchivo()))
					{
						dto.setPreguntarArchivosSobreEsc(true);
						archivo.setPreguntarSobreEscribir(true);
					}
	
					if(UtilidadFileUpload.existeArchivo(dto.getPathArchivoInconsis(),archivoIncon.getNombreArchivo()))
					{
						dto.setPreguntarArchivosSobreEsc(true);
						archivoIncon.setPreguntarSobreEscribir(true);
					}
					
					dto.getTipoMovimientos().get(i).archivos.add(archivo);
					dto.getTipoMovimientos().get(i).archivosIncon.add(archivoIncon);
					
					//Archivo Honorarios
					archivoIncon = new DtoTipoMovimientoArchivo();
					archivo = new DtoTipoMovimientoArchivo();
					
					archivo.setPreguntarSobreEscribir(false);
					archivo.setSobreEscribir(false);
					archivoIncon.setPreguntarSobreEscribir(false);
					archivoIncon.setSobreEscribir(false);
					
					//asigna el subtipo de movimiento
					archivo.setIndicadorTipoMov(acronimoTipoMovHonoraInte);
					archivoIncon.setIndicadorTipoMov(acronimoTipoMovHonoraInte);
					
					archivo.setIndicadorArchivo(indicadorArchivoHonoInt);
					archivoIncon.setIndicadorArchivo(indicadorArchivoHonoIntInco);
					archivo.setNombreArchivo(indicadorArchivoHonoInt+dto.getFechaProcesoDdMmAaaa());
					archivoIncon.setNombreArchivo(indicadorArchivoHonoIntInco+dto.getFechaProcesoDdMmAaaa());
	
					if(UtilidadFileUpload.existeArchivo(dto.getPathArchivoInterfaz(),archivo.getNombreArchivo()))
					{
						dto.setPreguntarArchivosSobreEsc(true);
						archivo.setPreguntarSobreEscribir(true);
					}
	
					if(UtilidadFileUpload.existeArchivo(dto.getPathArchivoInconsis(),archivoIncon.getNombreArchivo()))
					{
						dto.setPreguntarArchivosSobreEsc(true);
						archivoIncon.setPreguntarSobreEscribir(true);
					}
					
					dto.getTipoMovimientos().get(i).archivos.add(archivo);
					dto.getTipoMovimientos().get(i).archivosIncon.add(archivoIncon);
				}
				else if(dto.getTipoMovimientos().get(i).getCodigo().equals(ConstantesIntegridadDominio.acronimoTipoMovRecaudos))
				{
					DtoTipoMovimientoArchivo archivoIncon = new DtoTipoMovimientoArchivo();
					DtoTipoMovimientoArchivo archivo = new DtoTipoMovimientoArchivo();
					
					archivo.setPreguntarSobreEscribir(false);
					archivo.setSobreEscribir(false);
					archivoIncon.setPreguntarSobreEscribir(false);
					archivoIncon.setSobreEscribir(false);
					
					//asigna el sub tipo de moviento para este caso es el mismo tipo
					archivo.setIndicadorTipoMov(ConstantesIntegridadDominio.acronimoTipoMovRecaudos);
					archivoIncon.setIndicadorTipoMov(ConstantesIntegridadDominio.acronimoTipoMovRecaudos);
					
					archivo.setIndicadorArchivo(indicadorArchivoRecaudos);
					archivoIncon.setIndicadorArchivo(indicadorArchivoRecaudosInco);
					archivo.setNombreArchivo(indicadorArchivoRecaudos+dto.getFechaProcesoDdMmAaaa());
					archivoIncon.setNombreArchivo(indicadorArchivoRecaudosInco+dto.getFechaProcesoDdMmAaaa());
	
					if(UtilidadFileUpload.existeArchivo(dto.getPathArchivoInterfaz(),archivo.getNombreArchivo()))
					{
						dto.setPreguntarArchivosSobreEsc(true);
						archivo.setPreguntarSobreEscribir(true);
					}
	
					if(UtilidadFileUpload.existeArchivo(dto.getPathArchivoInconsis(),archivoIncon.getNombreArchivo()))
					{
						dto.setPreguntarArchivosSobreEsc(true);
						archivoIncon.setPreguntarSobreEscribir(true);						
					}
					
					dto.getTipoMovimientos().get(i).archivos.add(archivo);
					dto.getTipoMovimientos().get(i).archivosIncon.add(archivoIncon);
				}
				else if(dto.getTipoMovimientos().get(i).getCodigo().equals(ConstantesIntegridadDominio.acronimoTipoMovServiciEntidaExter))
				{
					DtoTipoMovimientoArchivo archivoIncon = new DtoTipoMovimientoArchivo();
					DtoTipoMovimientoArchivo archivo = new DtoTipoMovimientoArchivo();
					
					archivo.setPreguntarSobreEscribir(false);
					archivo.setSobreEscribir(false);
					archivoIncon.setPreguntarSobreEscribir(false);
					archivoIncon.setSobreEscribir(false);
					
					//asigna el sub tipo de moviento para este caso es el mismo tipo
					archivo.setIndicadorTipoMov(ConstantesIntegridadDominio.acronimoTipoMovServiciEntidaExter);
					archivoIncon.setIndicadorTipoMov(ConstantesIntegridadDominio.acronimoTipoMovServiciEntidaExter);
					
					archivo.setIndicadorArchivo(indicadorArchivoEntidadesExt);
					archivoIncon.setIndicadorArchivo(indicadorArchivoEntidadesExtInco);
					archivo.setNombreArchivo(indicadorArchivoEntidadesExt+dto.getFechaProcesoDdMmAaaa());
					archivoIncon.setNombreArchivo(indicadorArchivoEntidadesExtInco+dto.getFechaProcesoDdMmAaaa());
	
					if(UtilidadFileUpload.existeArchivo(dto.getPathArchivoInterfaz(),archivo.getNombreArchivo()))
					{
						dto.setPreguntarArchivosSobreEsc(true);
						archivo.setPreguntarSobreEscribir(true);
					}
	
					if(UtilidadFileUpload.existeArchivo(dto.getPathArchivoInconsis(),archivoIncon.getNombreArchivo()))
					{
						dto.setPreguntarArchivosSobreEsc(true);
						archivoIncon.setPreguntarSobreEscribir(true);
					}
					
					dto.getTipoMovimientos().get(i).archivos.add(archivo);
					dto.getTipoMovimientos().get(i).archivosIncon.add(archivoIncon);
				}
				else if(dto.getTipoMovimientos().get(i).getCodigo().equals(ConstantesIntegridadDominio.acronimoTipoMovAjusteReclasi))
				{
					DtoTipoMovimientoArchivo archivoIncon = new DtoTipoMovimientoArchivo();
					DtoTipoMovimientoArchivo archivo = new DtoTipoMovimientoArchivo();
					
					archivo.setPreguntarSobreEscribir(false);
					archivo.setSobreEscribir(false);
					archivoIncon.setPreguntarSobreEscribir(false);
					archivoIncon.setSobreEscribir(false);
					
					//asigna el sub tipo de moviento para este caso es el mismo tipo
					archivo.setIndicadorTipoMov(ConstantesIntegridadDominio.acronimoTipoMovAjusteReclasi);
					archivoIncon.setIndicadorTipoMov(ConstantesIntegridadDominio.acronimoTipoMovAjusteReclasi);
					
					archivo.setIndicadorArchivo(indicadorArchivoAjuste);
					archivoIncon.setIndicadorArchivo(indicadorArchivoAjusteInco);
					archivo.setNombreArchivo(indicadorArchivoAjuste+dto.getFechaProcesoDdMmAaaa());
					archivoIncon.setNombreArchivo(indicadorArchivoAjusteInco+dto.getFechaProcesoDdMmAaaa());
	
					if(UtilidadFileUpload.existeArchivo(dto.getPathArchivoInterfaz(),archivo.getNombreArchivo()))
					{
						dto.setPreguntarArchivosSobreEsc(true);
						archivo.setPreguntarSobreEscribir(true);
					}
	
					if(UtilidadFileUpload.existeArchivo(dto.getPathArchivoInconsis(),archivoIncon.getNombreArchivo()))
					{
						dto.setPreguntarArchivosSobreEsc(true);
						archivoIncon.setPreguntarSobreEscribir(true);						
					}
					
					dto.getTipoMovimientos().get(i).archivos.add(archivo);
					dto.getTipoMovimientos().get(i).archivosIncon.add(archivoIncon);
				}
				else if(dto.getTipoMovimientos().get(i).getCodigo().equals(ConstantesIntegridadDominio.acronimoTipoMovCartera))
				{
					DtoTipoMovimientoArchivo archivoIncon = new DtoTipoMovimientoArchivo();
					DtoTipoMovimientoArchivo archivo = new DtoTipoMovimientoArchivo();
					
					archivo.setPreguntarSobreEscribir(false);
					archivo.setSobreEscribir(false);
					archivoIncon.setPreguntarSobreEscribir(false);
					archivoIncon.setSobreEscribir(false);
					
					//asigna el sub tipo de moviento para este caso es el mismo tipo
					archivo.setIndicadorTipoMov(ConstantesIntegridadDominio.acronimoTipoMovCartera);
					archivoIncon.setIndicadorTipoMov(ConstantesIntegridadDominio.acronimoTipoMovCartera);
					
					archivo.setIndicadorArchivo(indicadorArchivoCartera);
					archivoIncon.setIndicadorArchivo(indicadorArchivoCarteraInco);
					archivo.setNombreArchivo(indicadorArchivoCartera+dto.getFechaProcesoDdMmAaaa());
					archivoIncon.setNombreArchivo(indicadorArchivoCarteraInco+dto.getFechaProcesoDdMmAaaa());
	
					if(UtilidadFileUpload.existeArchivo(dto.getPathArchivoInterfaz(),archivo.getNombreArchivo()))
					{
						dto.setPreguntarArchivosSobreEsc(true);
						archivo.setPreguntarSobreEscribir(true);
					}
	
					if(UtilidadFileUpload.existeArchivo(dto.getPathArchivoInconsis(),archivoIncon.getNombreArchivo()))
					{
						dto.setPreguntarArchivosSobreEsc(true);
						archivoIncon.setPreguntarSobreEscribir(true);						
					}
					
					dto.getTipoMovimientos().get(i).archivos.add(archivo);
					dto.getTipoMovimientos().get(i).archivosIncon.add(archivoIncon);
				}
			}
		}
		
		return dto;
	}
	
	/**
	 * carga los tipos de movimientos
	 * */
	public static ArrayList<DtoTipoMovimiento> cargarTiposMovimientos()
	{
		ArrayList<DtoTipoMovimiento> array = new ArrayList<DtoTipoMovimiento>();
		DtoTipoMovimiento dto = new DtoTipoMovimiento();
		
		dto = new DtoTipoMovimiento();
		dto.setCodigo(ConstantesIntegridadDominio.acronimoTodos);
		dto.setDescripcion("Todos");
		dto.setSeleccionado(false);
		array.add(dto);	
		
		dto = new DtoTipoMovimiento();
		dto.setCodigo(ConstantesIntegridadDominio.acronimoTipoMovVentasHonoraInte);
		dto.setDescripcion(ValoresPorDefecto.getIntegridadDominio(ConstantesIntegridadDominio.acronimoTipoMovVentasHonoraInte).toString());
		dto.setSeleccionado(false);
		array.add(dto);
		
		dto = new DtoTipoMovimiento();
		dto.setCodigo(ConstantesIntegridadDominio.acronimoTipoMovRecaudos);
		dto.setDescripcion(ValoresPorDefecto.getIntegridadDominio(ConstantesIntegridadDominio.acronimoTipoMovRecaudos).toString());
		dto.setSeleccionado(false);
		array.add(dto);
		
		dto = new DtoTipoMovimiento();
		dto.setCodigo(ConstantesIntegridadDominio.acronimoTipoMovAjusteReclasi);
		dto.setDescripcion(ValoresPorDefecto.getIntegridadDominio(ConstantesIntegridadDominio.acronimoTipoMovAjusteReclasi).toString());
		dto.setSeleccionado(false);
		array.add(dto);
		
		dto = new DtoTipoMovimiento();
		dto.setCodigo(ConstantesIntegridadDominio.acronimoTipoMovServiciEntidaExter);
		dto.setDescripcion(ValoresPorDefecto.getIntegridadDominio(ConstantesIntegridadDominio.acronimoTipoMovServiciEntidaExter).toString());
		dto.setSeleccionado(false);
		array.add(dto);
		
		dto = new DtoTipoMovimiento();
		dto.setCodigo(ConstantesIntegridadDominio.acronimoTipoMovCartera);
		dto.setDescripcion(ValoresPorDefecto.getIntegridadDominio(ConstantesIntegridadDominio.acronimoTipoMovCartera).toString());
		dto.setSeleccionado(false);
		array.add(dto);
		
		return array;
	}
	
	
	/**
	 * Método que consulta el contenido de un archivo 
	 * @param nombreArchivoMap
	 * @return
	 */
	public static HashMap cargarArchivo(String ruta,String nombreArchivo)
	{
		//objeto usado para llenar el contenido del archivo
		HashMap contenido=new HashMap();
		String separador=",";
		contenido.put("numRegistros","0");
			 			
		try
		{
			int contador=0;
			String cadena="";
			
			//******SE INICIALIZA ARCHIVO*************************
			contenido.put("nombreArchivoMostrar",nombreArchivo);
			
			File archivoColsa= new File(ruta,nombreArchivo);
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
			logger.error("No se pudo encontrar el archivo "+nombreArchivo+" al cargarlo: "+e);
			return contenido;
		}
		catch(IOException e)
		{
			logger.error("Error en los streams del archivo "+nombreArchivo+" al cargarlo: "+e);
			return contenido;
		}	
	}
	
	/** consulta informacion de tipo doc por tipo movimiento 1e
	 * @param Connection con
	 * @param String codigoTipoMovimiento
	 * */
	public static ArrayList<HashMap> consultarTipoDocXTipoMov1E(Connection con,String codigoTipoMovimiento){
		HashMap parametros = new HashMap();
		parametros.put("codigoTipoMov",codigoTipoMovimiento);
		return getDao().consultarTipoDocXTipoMov1E(con, parametros);
	}
	
	 /**
     * Metodo para Guardar el LOG de Interfaz 1E ( generado despues de desmarcar los Docuementos )
     * @param con
     * @param dto
     * @return
     */
	public static boolean guardarLogInterfaz1E(Connection con,DtoInterfazS1EInfo dtoInterfaz,UsuarioBasico usuario) 
	{
		String fecha = UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual());
		String hora = UtilidadFecha.getHoraActual();
		
		for(DtoTipoMovimiento dtoTipoMov : dtoInterfaz.getTipoMovimientos())
		{
			if(dtoTipoMov.isSeleccionado())
			{
				for(DtoTipoMovimientoArchivo archivo : dtoTipoMov.getArchivos())
				{
					if(archivo.isGenerado())
					{
						DtoLogInterfaz1E dto = new DtoLogInterfaz1E();
						dto.setUsuarioProcesa(usuario.getLoginUsuario());
						dto.setFechaGeneracion(fecha);
						dto.setHoraGeneracion(hora);
						dto.setTipoMovimiento(dtoTipoMov.getCodigo());
						dto.setPath(archivo.getPathArchivo());
						dto.setNombreArchivo(archivo.getNombreArchivo());
						dto.setFechaProceso(UtilidadFecha.conversionFormatoFechaABD(dtoInterfaz.getFechaProceso()));
						dto.setTipoArchivo(ConstantesIntegridadDominio.acronimoInterfaz);
						dto.setInstitucion(usuario.getCodigoInstitucion());
						dto.setTipoProceso(ConstantesIntegridadDominio.acronimoInterfaz);
						
						DesmarcarDocProcesados.guardarLogInterfaz1E(con, dto);
					}
				}
				
				for(DtoTipoMovimientoArchivo archivo : dtoTipoMov.getArchivosIncon())
				{
					if(archivo.isGenerado())
					{
						DtoLogInterfaz1E dto = new DtoLogInterfaz1E();
						dto.setUsuarioProcesa(usuario.getLoginUsuario());
						dto.setFechaGeneracion(fecha);
						dto.setHoraGeneracion(hora);
						dto.setTipoMovimiento(dtoTipoMov.getCodigo());
						dto.setPath(archivo.getPathArchivo());
						dto.setNombreArchivo(archivo.getNombreArchivo());
						dto.setFechaProceso(UtilidadFecha.conversionFormatoFechaABD(dtoInterfaz.getFechaProceso()));
						dto.setTipoArchivo(ConstantesIntegridadDominio.acronimoInconsistencia);
						dto.setInstitucion(usuario.getCodigoInstitucion());
						dto.setTipoProceso(ConstantesIntegridadDominio.acronimoInterfaz);
						
						DesmarcarDocProcesados.guardarLogInterfaz1E(con, dto);
					}
				}
			}
		}
		
		
		return true;
	}
	
	/**
	 * Método para consultar los documentos contables de un tipo de movimiento específico
	 * @param con
	 * @param parametrizacion
	 * @return
	 */
	public static ArrayList<DtoInterfazLineaS1E> consultarDocumentosContables(Connection con,DtoInterfazS1EInfo parametrizacion)
	{
		return getDao().consultarDocumentosContables(con, parametrizacion);
	}

	public ArrayList<DtoInterfazLineaS1E> getArrayLineas() {
		return arrayLineas;
	}

	public void setArrayLineas(ArrayList<DtoInterfazLineaS1E> arrayLineas) {
		this.arrayLineas = arrayLineas;
	}

	public DtoInterfazS1EInfo getDtoInfoInterfaz() {
		return dtoInfoInterfaz;
	}

	public void setDtoInfoInterfaz(DtoInterfazS1EInfo dtoInfoInterfaz) {
		this.dtoInfoInterfaz = dtoInfoInterfaz;
	}	
}