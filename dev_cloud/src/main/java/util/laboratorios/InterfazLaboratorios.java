/*
 * Nov 17, 2006
 */
package util.laboratorios;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;

import com.princetonsa.mundo.Cuenta;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.Usuario;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.agenda.Cita;
import com.princetonsa.mundo.cargos.Cargos;
import com.princetonsa.mundo.cargos.Contrato;
import com.princetonsa.mundo.ordenesmedicas.procedimientos.RespuestaProcedimientos;
import com.princetonsa.mundo.solicitudes.Solicitud;
import com.princetonsa.mundo.solicitudes.SolicitudProcedimiento;
import com.servinte.axioma.fwk.exception.IPSException;

import util.BackUpBaseDatos;
import util.ConstantesBD;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.UtilidadValidacion;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.interfaces.UtilidadBDInterfaz;

/**
 * @author Sebastián Gómez 
 *
 *Clase que sirve maneja los procesos relacionados con la interfaz
 *de laboratorios
 */
public class InterfazLaboratorios 
{
	
	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger=Logger.getLogger(InterfazLaboratorios.class);
	
	
	
	/**
	 * Método que verifica si un centro de costo es de Terceros
	 * @param codigoCentroCosto
	 * @param institucion
	 * @return
	 */	
	public static boolean esCentroCostoTerceros(int codigoCentroCosto,int institucion)
	{
		boolean existe = false;
		HashMap centrosCostoTerceros = ValoresPorDefecto.getCentroCostoTerceros();
		
		for(int i=0;i<Integer.parseInt(centrosCostoTerceros.get("numRegistros").toString());i++)
		{
			int instTerceros = Integer.parseInt(centrosCostoTerceros.get("institucion_"+i).toString());
			int centroTercero = Integer.parseInt(centrosCostoTerceros.get("centrocosto_"+i).toString());
			if(codigoCentroCosto==centroTercero&&institucion==instTerceros)
				existe = true;
		}
		return existe;
	}
	
	/**
	 * Método implemenado para generar el archivo interfaz x orden de procedimientos
	 * @param infoInterfaz
	 * @param paciente
	 * @param cliente
	 * @return
	 */
	public static ActionErrors generarRegistroArchivo(HashMap infoInterfaz,PersonaBasica paciente, String cliente, ActionErrors errores)
	{
		if(cliente.equals(ConstantesBD.clienteSUBA))
			errores = generarRegistroArchivoSUBA(infoInterfaz, paciente, errores);
		else
			errores = generarRegistroArchivoSHAIO2(infoInterfaz, paciente, errores);
		
		return errores;
	}
	
	/**
	 * Método para generar el ARchivo plano con LABSOFT para el laboratoio de la clínica SHAIO
	 * (basado en la interfaz con SUBA)
	 * @param infoInterfaz
	 * @param paciente
	 * @param errores 
	 * @return
	 */
	private static ActionErrors generarRegistroArchivoSHAIO2(HashMap infoInterfaz,PersonaBasica paciente, ActionErrors errores)
	{
		int institucion = Integer.parseInt(infoInterfaz.get("institucion").toString());
		
		boolean existe = false;
		boolean urgente = false;
		
		
		//SE VERIFICA SI EXISTEN SOLICITUDES DIRIGIDAS A CENTRO COSTO TERCERO===========================0
		for(int i=0;i<Integer.parseInt(infoInterfaz.get("numRegistros").toString());i++)
		{
			if(esCentroCostoTerceros(Integer.parseInt(infoInterfaz.get("centroCosto_"+i).toString()),institucion)&&
				!infoInterfaz.get("codigoCUPS_"+i).toString().equalsIgnoreCase("X00086")&&
				!infoInterfaz.get("codigoCUPS_"+i).toString().equalsIgnoreCase("M19275")
				)
			{
				existe = true;
				if(UtilidadTexto.getBoolean(infoInterfaz.get("urgente_"+i).toString()))
					urgente = true;
			}
		}
		
		//SI EXISTE ALGUNA SOLICITUD SE CONTINUA CON LA CREACIÓN DEL ARCHIVO================================
		if(existe)
		{
			
			String nombreArchivo = "";
			try
			{
				String contenido = ""; //variable para almacenar el contenido del archivo
				String aux = "";
				String[] vector = new String[0];
				//String separador=System.getProperty("file.separator"); //separador
				
				//preparación del nombre del archivo *******************************
				nombreArchivo="axioma"+infoInterfaz.get("numeroDocumento").toString()+".txt";
				
				//se valida el directorio*******************************************************
				logger.info("RUTA IONTERFAZ LÑABORATORIOS: "+ValoresPorDefecto.getFilePathInterfazLaboratorios());
				File directorio=new File(ValoresPorDefecto.getFilePathInterfazLaboratorios());
				
				if(!directorio.isDirectory() && !directorio.exists())	{
					if(!directorio.mkdirs()) {
						logger.error("Error creando el directorio "+ValoresPorDefecto.getFilePathInterfazLaboratorios());
						errores.add("",new ActionMessage("errors.notEspecific","Error creando el directorio "+ValoresPorDefecto.getFilePathInterfazLaboratorios()));
						return errores;
					}
				}
				
				//apertura del archivo Inconsistencias********************************
				File archivoIncon= new File(directorio,"temp"+nombreArchivo);
				FileWriter streamIncon=new FileWriter(archivoIncon,true);
				BufferedWriter bufferIncon=new BufferedWriter(streamIncon);
				
				
				//1) CAMPO NOMBRE============================================================
				aux = paciente.getApellidosNombresPersona(false);
				if(aux.length()>60)
					aux = aux.substring(0,60);
				else
					aux = editarEspacios(aux,aux.length(),60,true," ");
				contenido += aux;
				
				//2) CAMPO TIPODOC ===========================================================
				aux = paciente.getCodigoTipoIdentificacionPersona();
				if(aux.length()>3)
					aux = aux.substring(0,3);
				else
					aux = editarEspacios(aux,aux.length(),3,true," ");
				contenido += aux;
				
				//3) CAMPO NUMDOC =============================================================
				aux = paciente.getNumeroIdentificacionPersona();
				if(aux.length()>40)
					aux = aux.substring(0,40);
				else
					aux = editarEspacios(aux,aux.length(),40,true," ");
				contenido += aux;
				
				//4) CAMPO SEXO =================================================================
				if(paciente.getCodigoSexo()==ConstantesBD.codigoSexoMasculino)
					aux = "M";
				else
					aux = "F";
				contenido += aux;
				
				//5) CAMPO FECHANAC ==============================================================
				aux = paciente.getFechaNacimiento();
				vector = aux.split("/");
				aux = vector[1] + "/" + vector[0] + "/" + vector[2];
				contenido += aux;
				
				//6) CAMPO TELEFONO ==============================================================
				aux = paciente.getTelefono();
				if(aux.length()>40)
					aux = aux.substring(0,40);
				else
					aux = editarEspacios(aux,aux.length(),40,true," ");
				contenido += aux;
				
				//7) CAMPO DOMPART ===================================================================
				aux = paciente.getDireccion();
				if(aux.length()>40)
					aux = aux.substring(0,40);
				else
					aux = editarEspacios(aux,aux.length(),40,true," ");
				contenido += aux;
				
				//8) CAMPO NUMAFI =======================================================================
				aux = paciente.getNumeroIdentificacionPersona(); //Es la misma identificacion del paciente
				if(aux.length()>20)
					aux = aux.substring(0,20);
				else
					aux = editarEspacios(aux,aux.length(),20,true," ");
				contenido += aux;
				
				//9) CAMPO N° ORDEN ====================================================================
				aux = infoInterfaz.get("numeroDocumento").toString();
				if(aux.length()>30)
					aux = aux.substring(0,30);
				else
					aux = editarEspacios(aux,aux.length(),30,true," ");
				contenido += aux;
				
				//10) CAMPO FECHA DE ALTA ===================================================================
				aux = infoInterfaz.get("fechaSolicitud").toString();
				vector = aux.split("/");
				aux = vector[1] + "/" + vector[0] + "/" + vector[2];
				contenido += aux;
				
				//11) CAMPO TIPO POBLADOR ===================================================================
				aux = paciente.getCodigoUltimaViaIngreso() + "";
				if(aux.length()>10)
					aux = aux.substring(0,10);
				else
					aux = editarEspacios(aux,aux.length(),10,true," ");
				contenido += aux;
				
				//12) CAMPO CODIGOSERVICIO ==================================================================
				aux = paciente.getCodigoArea() + "";
				if(aux.length()>10)
					aux = aux.substring(0,10);
				else
					aux = editarEspacios(aux,aux.length(),10,true," ");
				contenido += aux;
				
				//13) CAMPO NOMBRESERVICIO ==================================================================
				aux = paciente.getArea();
				if(aux.length()>30)
					aux = aux.substring(0,30);
				else
					aux = editarEspacios(aux,aux.length(),30,true," ");
				contenido += aux;
				
				//14) CAMPO CODIGOOBRASOCIAL =================================================================
				aux = paciente.getCodigoConvenio() + "";
				if(aux.length()>10)
					aux = aux.substring(0,10);
				else
					aux = editarEspacios(aux,aux.length(),10,true," ");
				contenido += aux;
				
				//15) CAMPO NOMBREOBRASOCIAL =================================================================
				aux = paciente.getConvenioPersonaResponsable();
				if(aux.length()>40)
					aux = aux.substring(0,40);
				else
					aux = editarEspacios(aux,aux.length(),40,true," ");
				contenido += aux;
				
				//16) CAMPO CODIGOPROFESIONAL =================================================================
				aux = infoInterfaz.get("codigoMedico").toString();
				if(aux.length()>10)
					aux = aux.substring(0,10);
				else
					aux = editarEspacios(aux,aux.length(),10,true," ");
				contenido += aux;
				
				//17) CAMPO NOMBREPROFESIONAL ===================================================================
				aux = infoInterfaz.get("nombreMedico").toString();
				if(aux.length()>30)
					aux = aux.substring(0,30);
				else
					aux = editarEspacios(aux,aux.length(),30,true," ");
				contenido += aux;
				
				//18) CAMPO UBICACION ===================================================================================
				aux = infoInterfaz.get("numeroCama").toString().trim();
				if(aux.length()>15)
					aux = aux.substring(0,15);
				else
					aux = editarEspacios(aux,aux.length(),15,true," ");
				contenido += aux;
				
				//19) CAMPO URGENTE ==========================================================================================
				if(urgente)
					contenido += "S"; 
				else
					contenido += "N";
				
				
				//Se iteran las solicitudes
				for(int i=0;i<Integer.parseInt(infoInterfaz.get("numRegistros").toString());i++)
				{
					if(esCentroCostoTerceros(Integer.parseInt(infoInterfaz.get("centroCosto_"+i).toString()),institucion)&&
							//Se excluye la glucometría y el paquete dwe coagulación
						!infoInterfaz.get("codigoCUPS_"+i).toString().equalsIgnoreCase("X00086")&&
						!infoInterfaz.get("codigoCUPS_"+i).toString().equalsIgnoreCase("M19275")
						)
					{
						// LF ==========================================================================================================
						contenido += "\n";
				
						
						//20) CAMPO CODIGO ESTUDIO ==========================================================================
						aux = infoInterfaz.get("numeroSolicitud_"+i).toString();
						if(aux.length()>10)
							aux = aux.substring(0,10);
						else
							aux = editarEspacios(aux,aux.length(),10,true," ");
						contenido += aux;
						
						//21) CAMPO CODIGO CUPS =============================================================================
						aux = infoInterfaz.get("codigoCUPS_"+i).toString();
						if(aux.length()>10)
							aux = aux.substring(0,10);
						else
							aux = editarEspacios(aux,aux.length(),10,true," ");
						contenido += aux;
						
						//22) CAMPO ESTADO ================================================================================
						aux = infoInterfaz.get("estado_"+i).toString();
						if(aux.length()>1)
							aux = aux.substring(0,1);
						else
							aux = editarEspacios(aux,aux.length(),1,true," ");
						contenido += aux;
						
						
					}
				}
				
				// LF + CR ====================================================================================
				contenido += "\r\n";
						
				
				//se escirbe el contenido
				bufferIncon.write(contenido);
				bufferIncon.close();
				
				//Se cambia encoding del archivo
				if(BackUpBaseDatos.EjecutarComandoSO("/usr/bin/iconv "+ValoresPorDefecto.getFilePathInterfazLaboratorios()+"temp"+nombreArchivo+" -f UTF-8 -t ISO_8859-1 -o "+ValoresPorDefecto.getFilePathInterfazLaboratorios()+nombreArchivo) != -1)
				{
					//Se cambian los permisos del archivo y se borra el temporal
					String[] cmd2={"/bin/chmod","777",ValoresPorDefecto.getFilePathInterfazLaboratorios()+nombreArchivo};
					Runtime.getRuntime().exec(cmd2);
				}
				else
				{
					logger.error("Error tratando de cambiar el encoding del archivo plano interfaz laboratorios");
					errores.add("",new ActionMessage("errors.notEspecific","Error tratando de cambiar el encoding del archivo plano interfaz laboratorios"));
				}
				
				
				
				
				
				
				//*************SE GENERA COPIA DE SEGURIDAD DEL ARCHIVO******************************
				File directorioBackup=new File(ValoresPorDefecto.getFilePathInterfazLaboratorios()+System.getProperty("file.separator")+"backup");
				
				if(!directorioBackup.isDirectory() && !directorioBackup.exists())	{
					if(!directorioBackup.mkdirs()) {
						logger.error("Error creando el directorio "+ValoresPorDefecto.getFilePathInterfazLaboratorios()+System.getProperty("file.separator")+"backup");
						errores.add("",new ActionMessage("errors.notEspecific","Error creando el directorio "+ValoresPorDefecto.getFilePathInterfazLaboratorios()+System.getProperty("file.separator")+"backup"));
						return errores;
					}
				}
				String[] horaActual = UtilidadFecha.getHoraActual().split(":");
				String nombreArchivoBackup = "axioma"+infoInterfaz.get("numeroDocumento").toString()+"-"+UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())+"-"+horaActual[0]+horaActual[1]+".txt";
				File archivoBackup = new File(directorioBackup,"temp"+nombreArchivoBackup);
				try
				{
					BufferedWriter bufferBackup=new BufferedWriter(new FileWriter(archivoBackup,true)); 
					//Se vuelve a tomar archivo original;
					BufferedReader bufferOriginal = new BufferedReader(new FileReader(archivoIncon));
					
					String cadena = bufferOriginal.readLine();
					contenido = "";
					int i=0;
					logger.info("voy a leer archivo temporal: "+archivoIncon.getAbsolutePath());
					while(cadena!=null)
					{
						logger.info("[0]: "+cadena);
						if(i!=0)
						{
							contenido += "\n";
						}
						
						contenido += cadena;
						i++;
						cadena = bufferOriginal.readLine();
					}
					logger.info("termine de ller archivo temporal");
					contenido += "\r\n";
					bufferBackup.write(contenido);
					bufferBackup.flush();
					bufferBackup.close();
					bufferOriginal.close();
					
					
					//Se cambia el encoding del archivo
					
					if(BackUpBaseDatos.EjecutarComandoSO("/usr/bin/iconv "+ValoresPorDefecto.getFilePathInterfazLaboratorios()+System.getProperty("file.separator")+"backup"+System.getProperty("file.separator")+"temp"+nombreArchivoBackup+" -f UTF-8 -t ISO_8859-1 -o "+ValoresPorDefecto.getFilePathInterfazLaboratorios()+System.getProperty("file.separator")+"backup"+System.getProperty("file.separator")+nombreArchivoBackup) != -1)
					{
						///Se cambian los permisos del archivo backup y se elimina el temporal
						String[] cmd1={"/bin/rm",ValoresPorDefecto.getFilePathInterfazLaboratorios()+"temp"+nombreArchivo};
						Runtime.getRuntime().exec(cmd1);
						String[] cmd01={"/bin/rm",ValoresPorDefecto.getFilePathInterfazLaboratorios()+System.getProperty("file.separator")+"backup"+System.getProperty("file.separator")+"temp"+nombreArchivoBackup};
						Runtime.getRuntime().exec(cmd01);
						String[] cmd02={"/bin/chmod","777",ValoresPorDefecto.getFilePathInterfazLaboratorios()+System.getProperty("file.separator")+"backup"+System.getProperty("file.separator")+nombreArchivoBackup};
						Runtime.getRuntime().exec(cmd02);
					}
					else
					{
						logger.error("Error tratando de cambiar el encoding del archivo plano (BACKUP) interfaz laboratorios");
						errores.add("",new ActionMessage("errors.notEspecific","Error tratando de cambiar el encoding del archivo plano (BACKUP) interfaz laboratorios"));
					}
					
					
					
					 
					
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
				//************************************************************************************
				
				return errores;
			
			}
			catch(FileNotFoundException e)
			{
				logger.error("No se pudo encontrar el archivo Incon al generarlo: "+e);
				errores.add("",new ActionMessage("errors.notEspecific","No se pudo encontrar el archivo Incon al generarlo: "+e));
				return errores;
			}
			catch(IOException e)
			{
				logger.error("Error en los streams del archivo Incon: "+e);
				errores.add("",new ActionMessage("errors.notEspecific","Error en los streams del archivo Incon: "+e));
				return errores;
			}
		}
		return errores;
	}
	
	
	

	/**
	 * Método para registrar el archivo de interfaz para el cliente SHAIO
	 * @param infoInterfaz
	 * @param paciente
	 * @return
	 */
	private static ActionErrors generarRegistroArchivoSHAIO1(HashMap infoInterfaz,PersonaBasica paciente) 
	{
		ActionErrors errores = new ActionErrors();
		int institucion = Integer.parseInt(infoInterfaz.get("institucion").toString());
		
		boolean existe = false,urgente = false;
		HashMap listadoLaboratorios = new HashMap(); //listado de laboratorios
		
		//SE VERIFICA SI EXISTEN SOLICITUDES DIRIGIDAS A CENTRO COSTO TERCERO===========================0
		for(int i=0;i<Integer.parseInt(infoInterfaz.get("numRegistros").toString());i++)
		{
			if(esCentroCostoTerceros(Integer.parseInt(infoInterfaz.get("centroCosto_"+i).toString()),institucion))
			{
				//if(validacion.equals(""))
				existe = true;
				if(UtilidadTexto.getBoolean(infoInterfaz.get("urgente_"+i).toString()))
					urgente = true;
				
				if(Utilidades.convertirAEntero(infoInterfaz.get("codigoLaboratorio_"+i).toString())<=0)
					errores.add("",new ActionMessage("errors.notEspecific","El servicio "+infoInterfaz.get("nombreServicio_"+i)+" no tiene un codigo de laboratorio asignado para la interfaz. Proceso Cancelado"));
				
				
				int numLaboratorios = Utilidades.convertirAEntero(listadoLaboratorios.get("numRegistros")+"",true);
				listadoLaboratorios.put(""+numLaboratorios, infoInterfaz.get("codigoLaboratorio_"+i).toString());
				listadoLaboratorios.put("cups_"+numLaboratorios, infoInterfaz.get("codigoCUPS_"+i).toString());
				numLaboratorios++;
				listadoLaboratorios.put("numRegistros", numLaboratorios+"");
				
				//codigoLaboratorio_
				/**if(listadoLaboratorios.containsValue(infoInterfaz.get("codigoLaboratorio_"+i).toString()))
				{
					validacion += (validacion.equals("")?"ERROR Problemas generando interfaz con laboratorios debido a que los siguientes procedimientos tienen el mismo codigo de laboratorio: ":"");
					validacion += " " +  infoInterfaz.get("codigoCUPS_"+i).toString();
					
					existe = false;
				}
				else
					listadoLaboratorios.put(""+i, infoInterfaz.get("codigoLaboratorio_"+i).toString());**/
			}
		}
		
		//***********SE VERIFICA QUE NO HAYAN CODIGOS LABORATORIOS REPETIDOS***********
		Utilidades.imprimirMapa(listadoLaboratorios);
		HashMap listadoLabRepetidos = new HashMap();
		for(int i=0;i<Utilidades.convertirAEntero(listadoLaboratorios.get("numRegistros")+"", true);i++)
		{
			String listadoCUPSTemp = "";
			for(int j=(Utilidades.convertirAEntero(listadoLaboratorios.get("numRegistros")+"", true)-1);j>i;j--)
			{
				if(listadoLaboratorios.get(i+"").toString().equals(listadoLaboratorios.get(j+"").toString()))
				{
					listadoCUPSTemp += ","+listadoLaboratorios.get("cups_"+j) ;
					
				}
			}
			
			if(!listadoLabRepetidos.containsValue(listadoLaboratorios.get(i+"")+"")&&!listadoCUPSTemp.equals(""))
				errores.add("",new ActionMessage("errors.notEspecific","Problemas generando interfaz con laboratorios debido a que los siguientes procedimientos tienen el mismo codigo de laboratorio: "+listadoLaboratorios.get("cups_"+i)+listadoCUPSTemp));
			
			listadoLabRepetidos.put(""+i,listadoLaboratorios.get(i+""));
		}
		//******************************************************************************
		
		
		
		logger.info("mapa centros costo terceros: "+ValoresPorDefecto.getCentroCostoTerceros());
		logger.info("EXISTEN ORDENES DE LABORATORIO: "+existe);
		
		
		
		//SI EXISTE ALGUNA SOLICITUD SE CONTINUA CON LA CREACIÓN DEL ARCHIVO================================
		if(existe)
		{
			
			String nombreArchivo = "";
			try
			{
				String contenido = ""; //variable para almacenar el contenido del archivo
				String aux = "",horaSegundos = "", consecutivoIngreso = "";
				String[] vector = new String[0];
				//String separador=System.getProperty("file.separator"); //separador
				
				//Edicion de las horas del sistema
				vector = infoInterfaz.get("horaSistema").toString().split(":");
				horaSegundos = vector[0] + vector[1] + vector[2];
				
				//Edicion del consecutivo de ingreso
				aux = paciente.getConsecutivoIngreso();
				if(aux.length()>6)
					aux = aux.substring(0,6);
				else
					aux = editarEspacios(aux, aux.length(), 6, false, "0");
				consecutivoIngreso = aux; 
				
				//se valida el directorio*******************************************************
				File directorio=new File(ValoresPorDefecto.getFilePathInterfazLaboratorios());
				
				if(!directorio.isDirectory() && !directorio.exists())	{
					if(!directorio.mkdirs()) {
						errores.add("",new ActionMessage("errors.notEspecific","Error creando el directorio "+ValoresPorDefecto.getFilePathInterfazLaboratorios()));
						return errores;
					}
				}
				
				//******************************************************************************
				//******************GENERAR ARCHIVO M MAESTRO**************************************
				///preparación del nombre del archivo *******************************
				
				nombreArchivo="M"+consecutivoIngreso+"_"+horaSegundos+".DAT";
				
				//apertura del archivo Inconsistencias********************************
				File archivoIncon= new File(directorio,nombreArchivo);
				FileWriter streamIncon=new FileWriter(archivoIncon,true); 
				
				BufferedWriter bufferIncon=new BufferedWriter(streamIncon);
				
				//1) NÚMERO DE INGRESO============================================================
				aux = paciente.getConsecutivoIngreso();
				if(aux.length()>8)
					aux = aux.substring(0,8);
				else
					aux = editarEspacios(aux, aux.length(), 8, false, "0");
				contenido += aux + ",";
				
				//2) FECHA DE SOLICITUD =============================================================
				aux = infoInterfaz.get("fechaSolicitud").toString();
				vector = aux.split("/");
				aux = vector[2]  + vector[1] +  vector[0];
				contenido += aux + ",";
				
				//3) TIPO DE IDENTIFICACION =============================================================
				aux = paciente.getCodigoTipoIdentificacionPersona();
				contenido += aux + ",";
				
				//4) NUMERO DE DOCUMENTO =================================================================
				aux = paciente.getNumeroIdentificacionPersona();
				if(aux.length()>13)
					aux = aux.substring(0,13);
				else
					aux = editarEspacios(aux, aux.length(), 13, true, " ");
				contenido += aux + ",";
				
				//5) NOMBRE DEL PACIENTE =================================================================
				aux = UtilidadTexto.cambiarCaracteresEspeciales(paciente.getPrimerNombre()+(paciente.getSegundoNombre().equals("")?"":" "+paciente.getSegundoNombre()) + " " + paciente.getPrimerApellido()+(paciente.getSegundoApellido().equals("")?"":" "+paciente.getSegundoApellido())).toUpperCase();
				if(aux.length()>60)
					aux = aux.substring(0,60);
				else
					aux = editarEspacios(aux,aux.length(),60,true," ");
				contenido += aux.toUpperCase() + ",";
				
				//6) SEXO DEL PACIENTE =====================================================================
				aux = paciente.getSexo();
				if(aux.length()>9)
					aux = aux.substring(0,9);
				else
					aux = editarEspacios(aux,aux.length(),9,true," ");
				contenido += aux.toUpperCase() + ",";
				
				//7) FECHA DE NACIMIENTO ===================================================================
				aux = paciente.getFechaNacimiento();
				vector = aux.split("/");
				aux = vector[2]  + vector[1] +  vector[0];
				contenido += aux + ",";
				
				//8) EDAD DEL PACIENTE ============================================================
				aux = paciente.getEdad() +"";
				if(aux.length()>3)
					aux = aux.substring(0,3);
				else
					aux = editarEspacios(aux, aux.length(), 3, false, "0");
				contenido += aux + ",";
				
				//9) ESPACION EN BLANCO =============================================================
				contenido += " ,";
				
				//10) CODIGO ENTIDAD ==================================================================
				aux = infoInterfaz.get("nitEmpresa").toString();
				if(aux.length()>13)
					aux = aux.substring(0,13);
				else
					aux = editarEspacios(aux, aux.length(), 13, true, " ");
				contenido += aux.toUpperCase() + ",";
				
				//11) NUMERO CARNET =================================================================
				aux = infoInterfaz.get("nroCarnet").toString();
				if(aux.length()>20)
					aux = aux.substring(0, 20);
				else
					aux = editarEspacios(aux, aux.length(), 20, true, " ");
				contenido += aux.toUpperCase() + ",";
				
				//12) UBICACION CAMA ====================================================================
				aux = infoInterfaz.get("numeroCama").toString();
				if(!aux.equals(""))
				{
					vector = aux.split("-");
					if(vector.length>1)
						aux = vector[0]+vector[1];
				}
				
				if(aux.length()>6)
					aux = aux.substring(0,6);
				else
					aux = editarEspacios(aux, aux.length(), 6, true, " ");
				contenido += aux.toUpperCase() + ",";
				
				//13) VIA DE INGRESO =====================================================================
				if(paciente.getCodigoUltimaViaIngreso()==ConstantesBD.codigoViaIngresoHospitalizacion)
					aux = "INTERNO";
				else
					aux = "URGENCIAS";
				if(aux.length()>9)
					aux = aux.substring(0,9);
				else
					aux = editarEspacios(aux, aux.length(), 9, true, " ");
				contenido += aux.toUpperCase() + ",";
				
				//14) CONSTANTE =========================================================================
				contenido+= "    /  /  ,";
				
				//15) REGISTRO MÉDICO ====================================================================
				aux = infoInterfaz.get("codigoEspecialidadSolicitante").toString();
				if(aux.length()>13)
					aux = aux.substring(0,13);
				else
					aux = editarEspacios(aux, aux.length(), 13, false, "0");
				contenido += aux.toUpperCase() + ",";
				
				//16) NOMBRE MEDICO ======================================================================
				aux = UtilidadTexto.cambiarCaracteresEspeciales(infoInterfaz.get("nombreMedico").toString()); 
				if(aux.length()>60)
					aux = aux.substring(0,60);
				else
					aux = editarEspacios(aux, aux.length(), 60, true, " ");
				contenido += aux.toUpperCase() + ",";
				
				
				//17) CODIGO VIA INGRESO =========================================================
				aux = paciente.getCodigoUltimaViaIngreso() + "";
				if(aux.length()>2)
					aux = aux.substring(0,2);
				else
					aux = editarEspacios(aux, aux.length(), 2, false, "0");
				contenido += aux + ",";
				
				//18) CIE PREVIO ==================================================================
				aux = "000000";
				//vector = aux.split(ConstantesBD.separadorSplit);
				aux = editarEspacios(aux, aux.length(), 6, false, "0");
				contenido += aux + ",";
				
				//19) URGENCIAS ============================================================
				aux = urgente?"S":"N";
				contenido += aux + ",";
				
				//20) DIRECCION PACIENTE ==============================================
				aux = paciente.getDireccion();
				if(aux.length()>30)
					aux = aux.substring(0,30);
				else
					aux = editarEspacios(aux, aux.length(), 30, true, " ");
				contenido += aux.toUpperCase() + ",";
				
				//21) TELEFONO PACIENTE ======================================================
				aux = paciente.getTelefono();
				if(aux.length()>15)
					aux = aux.substring(0,15);
				else
					aux = editarEspacios(aux, aux.length(), 15, true, " ");
				contenido += aux.toUpperCase() + ",";
				
					
				/**
				aux = paciente.getPrimerNombre()+(paciente.getSegundoNombre().equals("")?"":" "+paciente.getSegundoNombre());
				if(aux.length()>60)
					aux = aux.substring(0,60);
				else
					aux = editarEspacios(aux,aux.length(),60,true," ");
				contenido += aux;
				
				//2) CAMPO APELLIDOS ===========================================================
				aux = paciente.getPrimerApellido()+(paciente.getSegundoApellido().equals("")?"":" "+paciente.getSegundoApellido());
				if(aux.length()>60)
					aux = aux.substring(0,60);
				else
					aux = editarEspacios(aux,aux.length(),60,true," ");
				contenido += aux;
				
				//3) CAMPO FECHA NACIMIENTO ==============================================================
				aux = paciente.getFechaNacimiento();
				vector = aux.split("/");
				aux = vector[0] +  vector[1] +  vector[2];
				contenido += aux;
				
				//4) CAMPO SEXO =================================================================
				if(paciente.getCodigoSexo()==ConstantesBD.codigoSexoMasculino)
					aux = "M";
				else
					aux = "F";
				contenido += aux;
				
				//5) CAMPO TIPO DE PACIENTE =======================================================
				if(paciente.getCodigoUltimaViaIngreso()==ConstantesBD.codigoViaIngresoUrgencias||
					paciente.getCodigoUltimaViaIngreso()==ConstantesBD.codigoViaIngresoHospitalizacion)
					aux = "0";
				else
					aux = "1";
				contenido += aux;
				
				//6) CAMPO CLAVE SERVICIO + TIPO SERVICIO ===============================================
				aux = paciente.getCodigoArea() + "";
				if(aux.length()>8)
					aux = aux.substring(0, 8);
				else
					aux = editarEspacios(aux,aux.length(),8,false,"0");
				contenido += aux;
				
				//7) CAMPO CAMA===================================================================================
				aux = infoInterfaz.get("numeroCama").toString().trim();
				if(aux.length()>36)
					aux = aux.substring(0,36);
				else
					aux = editarEspacios(aux,aux.length(),36,true," ");
				contenido += aux;
				
				//8) CAMPO COMENTARIO / DIAGNOSTICO =================================================================
				aux = infoInterfaz.get("comentarioDiagnostico").toString().trim();
				if(aux.length()>60)
					aux = aux.substring(0,60);
				else
					aux = editarEspacios(aux,aux.length(),60,true," ");
				contenido += aux;
				
				//9) CAMPO DIRECCION ===============================================================================
				aux = paciente.getDireccion();
				if(aux.length()>30)
					aux = aux.substring(0,30);
				else
					aux = editarEspacios(aux,aux.length(),30,true," ");
				contenido += aux;
				
				//10) CAMPO CIUDAD RESIDENCIA ===============================================================
				aux = paciente.getNombreCiudadVivienda();
				if(aux.length()>30)
					aux = aux.substring(0,30);
				else
					aux = editarEspacios(aux,aux.length(),30,true," ");
				contenido += aux;
				
				//11) CAMPO TELEFONO ===========================================================================
				aux = paciente.getTelefono();
				if(aux.length()>12)
					aux = aux.substring(0,12);
				else
					aux = editarEspacios(aux,aux.length(),12,true," ");
				contenido += aux;
				
				//12) CAMPO CODIGO EMPRESA=================================================================
				aux = paciente.getCodigoConvenio() + "";
				if(aux.length()>11)
					aux = aux.substring(0,11);
				else
					aux = editarEspacios(aux,aux.length(),11,false,"0");
				contenido += aux;
				
				//13) CAMPO CODIGO MEDICO =================================================================
				aux = infoInterfaz.get("codigoMedico").toString();
				if(aux.length()>11)
					aux = aux.substring(0,11);
				else
					aux = editarEspacios(aux,aux.length(),11,true," ");
				contenido += aux;
				
				//14) CAMPO FECHA DE CARGO ===================================================================
				aux = infoInterfaz.get("fechaSolicitud").toString();
				vector = aux.split("/");
				aux = vector[0]  + vector[1] +  vector[2];
				contenido += aux;
				
				//15) CAMPO HORA DE CARGO ===================================================================
				aux = infoInterfaz.get("horaSolicitud").toString();
				vector = aux.split(":");
				aux = vector[0]  + vector[1] +  "00";
				contenido += aux;
				
				//16) CAMPO EDAD ===============================================================
				aux = paciente.getEdad()+"";
				if(aux.length()>3)
					aux = aux.substring(0,3);
				else
					aux = editarEspacios(aux,aux.length(),3,false,"0");
				contenido += aux;
				
				//17 CAMPO TIPO EDAD ===========================================================
				int numeroDias = UtilidadFecha.numeroDiasEntreFechas(paciente.getFechaNacimiento(), infoInterfaz.get("fechaSolicitud").toString());
				
				if(numeroDias>365)
					aux = "01";
				else if(numeroDias>=0&&numeroDias<=6)
					aux = "00";
				else if(numeroDias>6&&numeroDias<=15)
					aux = "07";
				else if(numeroDias>15&&numeroDias<=30)
					aux = "16";
				else if(numeroDias>30&&numeroDias<=60)
					aux = "1M";
				else if(numeroDias>60&&numeroDias<=180)
					aux = "2M";
				else if(numeroDias>180&&numeroDias<=365)
					aux = "7M";
				contenido += aux;
				
				//18) CAMPO N° HISTORIA DEL PACIENTE ====================================================
				aux = paciente.getHistoriaClinica();
				if(aux.length()>25)
					aux = aux.substring(0,25);
				else
					aux = editarEspacios(aux,aux.length(),25,false,"0");
				contenido += aux;
				
				//19) CAMPO N° IDENTIFICACION DEL PACIENTE ====================================================
				aux = paciente.getNumeroIdentificacionPersona();
				if(aux.length()>20)
					aux = aux.substring(0,20);
				else
					aux = editarEspacios(aux,aux.length(),20,true," ");
				contenido += aux;
				
				//20) CAMPO N° ORDEN ====================================================================
				aux = infoInterfaz.get("numeroDocumento").toString();
				if(aux.length()>10)
					aux = aux.substring(0,10);
				else
					aux = editarEspacios(aux,aux.length(),10,false,"0");
				contenido += aux;
				
				
				//Se iteran las solicitudes
				for(int i=0;i<Integer.parseInt(infoInterfaz.get("numRegistros").toString());i++)
				{
					if(esCentroCostoTerceros(Integer.parseInt(infoInterfaz.get("centroCosto_"+i).toString()),institucion))
					{
						//21) CAMPO NUMERO SOLICITUD ==========================================================================
						aux = infoInterfaz.get("numeroSolicitud_"+i).toString();
						if(aux.length()>12)
							aux = aux.substring(0,12);
						else
							aux = editarEspacios(aux,aux.length(),12,false,"0");
						contenido += aux;
					}
				}
				
				//Se editan los espacios faltantes
				contenido = editarEspacios(contenido,contenido.length(),997,true," ");
				contenido += "1";
				/// CR + LF ====================================================================================
				contenido += "\n\r";
				*
				
				///se escirbe el contenido
				bufferIncon.write(contenido);
				
				
				contenido = "";
				
				//Se iteran las solicitudes
				for(int i=0;i<Integer.parseInt(infoInterfaz.get("numRegistros").toString());i++)
				{
					if(esCentroCostoTerceros(Integer.parseInt(infoInterfaz.get("centroCosto_"+i).toString()),institucion))
					{
						//22) CAMPO CODIGO EXAMEN (CUPS) ==========================================================================
						aux = infoInterfaz.get("codigoCUPS_"+i).toString();
						if(aux.length()>20)
							aux = aux.substring(0,20);
						else
							aux = editarEspacios(aux,aux.length(),20,false,"0");
						contenido += aux;
					}
				}
				
				//Se editan los espacios faltantes
				contenido = editarEspacios(contenido,contenido.length(),997,true," ");
				contenido += "2";
				/// CR + LF ====================================================================================
				contenido += "\n\r";
				
				**/
				
				bufferIncon.write(contenido);
				bufferIncon.close();
				
				//Se cambian los permisos del archivos
				String[] cmd={"/bin/chmod","777",ValoresPorDefecto.getFilePathInterfazLaboratorios()+nombreArchivo};
				Runtime.getRuntime().exec(cmd);
				
				contenido = "";
				///******************************************************************************
				//******************GENERAR ARCHIVO D DETALLE**************************************
				///preparación del nombre del archivo *******************************
				
				nombreArchivo="D"+consecutivoIngreso+"_"+horaSegundos+".DAT";
				
				//apertura del archivo Inconsistencias********************************
				archivoIncon= new File(directorio,nombreArchivo);
				streamIncon=new FileWriter(archivoIncon,true); 
				
				bufferIncon=new BufferedWriter(streamIncon);
				
				//Se iteran las solicitudes
				for(int i=0;i<Integer.parseInt(infoInterfaz.get("numRegistros").toString());i++)
				{
					if(esCentroCostoTerceros(Integer.parseInt(infoInterfaz.get("centroCosto_"+i).toString()),institucion))
					{
						if(!contenido.equals(""))
							contenido += "\r\n";
						
						//1) CODIGO LABORATORIO ==========================================================================
						aux = infoInterfaz.get("codigoLaboratorio_"+i).toString();
						if(aux.length()>5)
							aux = aux.substring(0,5);
						else if(aux.length()<5)
							aux = editarEspacios(aux, aux.length(), 4, false, "0");
						contenido += aux + ",";
						
						//2) CONSTANTES vs ===========================================================================
						contenido += "VS,";
						
						//3) CODIGO DE LA CITA ==================================================
						aux = infoInterfaz.get("numeroSolicitud_"+i).toString();
						if(aux.length()>10)
							aux = aux.substring(0,10);
						else
							aux = editarEspacios(aux,aux.length(),10,false," ");
						contenido += aux ;
						
						
					}
				}
				/// CR + LF ==========================================================
				contenido += "\r\n";
				
				bufferIncon.write(contenido);
				bufferIncon.close();
				
				//Se cambian los permisos del archivos
				String[] cmd0={"/bin/chmod","777",ValoresPorDefecto.getFilePathInterfazLaboratorios()+nombreArchivo};
				Runtime.getRuntime().exec(cmd0);
				
				contenido = "";
				
				
				///******************************************************************************
				//******************GENERAR ARCHIVO O OBSERVACIONES**************************************
				///preparación del nombre del archivo *******************************
				
				nombreArchivo="O"+consecutivoIngreso+"_"+horaSegundos+".DAT";
				
				//apertura del archivo Inconsistencias********************************
				archivoIncon= new File(directorio,nombreArchivo);
				streamIncon=new FileWriter(archivoIncon,true); 
				
				bufferIncon=new BufferedWriter(streamIncon);

				//Generación del encabezado
				contenido = editarEspacios("HABITACION",10,11,true," ") + editarEspacios("PROCEDIMIENTO",13,41,true," ") + editarEspacios("FECHA",5,9,true," ") + editarEspacios("HORA",4,8,true," ") + editarEspacios("OBSERVACION",11,200,true," ") + "\n";
				
				//Se iteran las solicitudes
				for(int i=0;i<Integer.parseInt(infoInterfaz.get("numRegistros").toString());i++)
				{
					if(esCentroCostoTerceros(Integer.parseInt(infoInterfaz.get("centroCosto_"+i).toString()),institucion))
					{
						//1) HABITACION ==========================================================================
						aux = infoInterfaz.get("numeroCama").toString();
						if(aux.length()>10)
							aux = aux.substring(0,10)+" ";
						else
							aux = editarEspacios(aux,aux.length(),11,true," ");
						contenido += aux ;
						
						//2) PROCEDIMIENTO ===========================================================================
						aux = UtilidadTexto.cambiarCaracteresEspeciales(infoInterfaz.get("nombreServicio_"+i).toString().toUpperCase());
						if(aux.length()>40)
							aux = aux.substring(0,40)+ " ";
						else
							aux = editarEspacios(aux,aux.length(),41,true," ");
						contenido += aux ;
						
						//3) FECHA ====================================================================
						aux = infoInterfaz.get("fechaSolicitud").toString();
						vector = aux.split("/");
						aux = vector[2]  + vector[1] +  vector[0];
						contenido += aux + " ";
						
						//4) HORA =======================================================================
						aux = infoInterfaz.get("horaSolicitud").toString();
						vector = aux.split(":");
						aux = vector[0]  + vector[1] +  "00";
						contenido += aux + " ";
						
						//5) OBSERVACIONES =======================================================================
						aux = UtilidadTexto.cambiarCaracteresEspeciales(infoInterfaz.get("observaciones").toString()).toUpperCase();
						contenido += aux ;
						
						// CR + LF ==========================================================
						contenido += "\n";
					}
				}
				
				bufferIncon.write(contenido);
				bufferIncon.close();
				
				//Se cambian los permisos del archivos
				String[] cmd1={"/bin/chmod","777",ValoresPorDefecto.getFilePathInterfazLaboratorios()+nombreArchivo};
				Runtime.getRuntime().exec(cmd1);
				
				
				
				//*************************************************************************************
				
				return errores;
			
			}
			catch(FileNotFoundException e)
			{
				logger.error("No se pudo encontrar el archivo Incon al generarlo: "+e);
				errores.add("",new ActionMessage("errors.notEspecific","No se pudo encontrar el archivo Incon al generarlo: "+e));
				return errores;
			}
			catch(IOException e)
			{
				logger.error("Error en los streams del archivo Incon: "+e);
				errores.add("",new ActionMessage("errors.notEspecific","Error en los streams del archivo Incon: "+e));
				return errores;
			}
		}
		return errores;
	}

	/**
	 * Método para generar el registro del archivo de labsoft
	 * @param infoInterfaz
	 * @param paciente
	 * @param errores2 
	 * @param tipoInterfaz
	 * @return
	 */
	private static ActionErrors generarRegistroArchivoSUBA(HashMap infoInterfaz,PersonaBasica paciente, ActionErrors errores)
	{
		int institucion = Integer.parseInt(infoInterfaz.get("institucion").toString());
		
		boolean existe = false;
		boolean urgente = false;
		
		//SE VERIFICA SI EXISTEN SOLICITUDES DIRIGIDAS A CENTRO COSTO TERCERO===========================0
		for(int i=0;i<Integer.parseInt(infoInterfaz.get("numRegistros").toString());i++)
		{
			if(esCentroCostoTerceros(Integer.parseInt(infoInterfaz.get("centroCosto_"+i).toString()),institucion))
			{
				existe = true;
				if(UtilidadTexto.getBoolean(infoInterfaz.get("urgente_"+i).toString()))
					urgente = true;
			}
		}
		
		//SI EXISTE ALGUNA SOLICITUD SE CONTINUA CON LA CREACIÓN DEL ARCHIVO================================
		if(existe)
		{
			
			String nombreArchivo = "";
			try
			{
				String contenido = ""; //variable para almacenar el contenido del archivo
				String aux = "";
				String[] vector = new String[0];
				//String separador=System.getProperty("file.separator"); //separador
				
				//preparación del nombre del archivo *******************************
				nombreArchivo="InterfazLabSoft.txt";
				
				//se valida el directorio*******************************************************
				File directorio=new File(ValoresPorDefecto.getFilePathInterfazLaboratorios());
				
				if(!directorio.isDirectory() && !directorio.exists())	{
					if(!directorio.mkdirs()) {
						logger.error("Error creando el directorio "+ValoresPorDefecto.getFilePathInterfazLaboratorios());
						errores.add("",new ActionMessage("errors.notEspecific","Error creando el directorio "+ValoresPorDefecto.getFilePathInterfazLaboratorios()));
						return errores;
					}
				}
				
				//apertura del archivo Inconsistencias********************************
				File archivoIncon= new File(directorio,nombreArchivo);
				FileWriter streamIncon=new FileWriter(archivoIncon,true); 
				
				BufferedWriter bufferIncon=new BufferedWriter(streamIncon);
				
				//1) CAMPO NOMBRE============================================================
				aux = paciente.getApellidosNombresPersona(false);
				if(aux.length()>60)
					aux = aux.substring(0,60);
				else
					aux = editarEspacios(aux,aux.length(),60,true," ");
				contenido += aux;
				
				//2) CAMPO TIPODOC ===========================================================
				aux = paciente.getCodigoTipoIdentificacionPersona();
				if(aux.length()>3)
					aux = aux.substring(0,3);
				else
					aux = editarEspacios(aux,aux.length(),3,true," ");
				contenido += aux;
				
				//3) CAMPO NUMDOC =============================================================
				aux = paciente.getNumeroIdentificacionPersona();
				if(aux.length()>40)
					aux = aux.substring(0,40);
				else
					aux = editarEspacios(aux,aux.length(),40,true," ");
				contenido += aux;
				
				//4) CAMPO SEXO =================================================================
				if(paciente.getCodigoSexo()==ConstantesBD.codigoSexoMasculino)
					aux = "M";
				else
					aux = "F";
				contenido += aux;
				
				//5) CAMPO FECHANAC ==============================================================
				aux = paciente.getFechaNacimiento();
				vector = aux.split("/");
				aux = vector[1] + "/" + vector[0] + "/" + vector[2];
				contenido += aux;
				
				//6) CAMPO TELEFONO ==============================================================
				aux = paciente.getTelefono();
				if(aux.length()>40)
					aux = aux.substring(0,40);
				else
					aux = editarEspacios(aux,aux.length(),40,true," ");
				contenido += aux;
				
				//7) CAMPO DOMPART ===================================================================
				aux = paciente.getDireccion();
				if(aux.length()>40)
					aux = aux.substring(0,40);
				else
					aux = editarEspacios(aux,aux.length(),40,true," ");
				contenido += aux;
				
				//8) CAMPO NUMAFI =======================================================================
				aux = paciente.getNumeroIdentificacionPersona(); //Es la misma identificacion del paciente
				if(aux.length()>20)
					aux = aux.substring(0,20);
				else
					aux = editarEspacios(aux,aux.length(),20,true," ");
				contenido += aux;
				
				//9) CAMPO N° ORDEN ====================================================================
				aux = infoInterfaz.get("numeroDocumento").toString();
				if(aux.length()>30)
					aux = aux.substring(0,30);
				else
					aux = editarEspacios(aux,aux.length(),30,true," ");
				contenido += aux;
				
				//10) CAMPO FECHA DE ALTA ===================================================================
				aux = infoInterfaz.get("fechaSolicitud").toString();
				vector = aux.split("/");
				aux = vector[1] + "/" + vector[0] + "/" + vector[2];
				contenido += aux;
				
				//11) CAMPO TIPO POBLADOR ===================================================================
				aux = paciente.getCodigoUltimaViaIngreso() + "";
				if(aux.length()>10)
					aux = aux.substring(0,10);
				else
					aux = editarEspacios(aux,aux.length(),10,true," ");
				contenido += aux;
				
				//12) CAMPO CODIGOSERVICIO ==================================================================
				aux = paciente.getCodigoArea() + "";
				if(aux.length()>10)
					aux = aux.substring(0,10);
				else
					aux = editarEspacios(aux,aux.length(),10,true," ");
				contenido += aux;
				
				//13) CAMPO NOMBRESERVICIO ==================================================================
				aux = paciente.getArea();
				if(aux.length()>30)
					aux = aux.substring(0,30);
				else
					aux = editarEspacios(aux,aux.length(),30,true," ");
				contenido += aux;
				
				//14) CAMPO CODIGOOBRASOCIAL =================================================================
				aux = paciente.getCodigoConvenio() + "";
				if(aux.length()>10)
					aux = aux.substring(0,10);
				else
					aux = editarEspacios(aux,aux.length(),10,true," ");
				contenido += aux;
				
				//15) CAMPO NOMBREOBRASOCIAL =================================================================
				aux = paciente.getConvenioPersonaResponsable();
				if(aux.length()>40)
					aux = aux.substring(0,40);
				else
					aux = editarEspacios(aux,aux.length(),40,true," ");
				contenido += aux;
				
				//16) CAMPO CODIGOPROFESIONAL =================================================================
				aux = infoInterfaz.get("codigoMedico").toString();
				if(aux.length()>10)
					aux = aux.substring(0,10);
				else
					aux = editarEspacios(aux,aux.length(),10,true," ");
				contenido += aux;
				
				//17) CAMPO NOMBREPROFESIONAL ===================================================================
				aux = infoInterfaz.get("nombreMedico").toString();
				if(aux.length()>30)
					aux = aux.substring(0,30);
				else
					aux = editarEspacios(aux,aux.length(),30,true," ");
				contenido += aux;
				
				//18) CAMPO UBICACION ===================================================================================
				aux = infoInterfaz.get("numeroCama").toString().trim();
				if(aux.length()>15)
					aux = aux.substring(0,15);
				else
					aux = editarEspacios(aux,aux.length(),15,true," ");
				contenido += aux;
				
				//19) CAMPO URGENTE ==========================================================================================
				if(urgente)
					contenido += "S"; 
				else
					contenido += "N";
				
				
				//Se iteran las solicitudes
				for(int i=0;i<Integer.parseInt(infoInterfaz.get("numRegistros").toString());i++)
				{
					if(esCentroCostoTerceros(Integer.parseInt(infoInterfaz.get("centroCosto_"+i).toString()),institucion))
					{
						// LF ==========================================================================================================
						contenido += "\n";
				
						
						//20) CAMPO CODIGO ESTUDIO ==========================================================================
						aux = infoInterfaz.get("numeroSolicitud_"+i).toString();
						if(aux.length()>10)
							aux = aux.substring(0,10);
						else
							aux = editarEspacios(aux,aux.length(),10,true," ");
						contenido += aux;
						
						//21) CAMPO CODIGO CUPS =============================================================================
						aux = infoInterfaz.get("codigoCUPS_"+i).toString();
						if(aux.length()>10)
							aux = aux.substring(0,10);
						else
							aux = editarEspacios(aux,aux.length(),10,true," ");
						contenido += aux;
						
						//22) CAMPO ESTADO ================================================================================
						aux = infoInterfaz.get("estado_"+i).toString();
						if(aux.length()>1)
							aux = aux.substring(0,1);
						else
							aux = editarEspacios(aux,aux.length(),1,true," ");
						contenido += aux;
						
						
					}
				}
				
				// LF + CR ====================================================================================
				contenido += "\r\n";
						
				
				//se escirbe el contenido
				bufferIncon.write(contenido);
				bufferIncon.close();
				
				//Se cambian los permisos del archivos
				String[] cmd={"/bin/chmod","777",ValoresPorDefecto.getFilePathInterfazLaboratorios()+nombreArchivo};
				Runtime.getRuntime().exec(cmd); 
				
				return errores;
			
			}
			catch(FileNotFoundException e)
			{
				logger.error("No se pudo encontrar el archivo Incon al generarlo: "+e);
				errores.add("",new ActionMessage("errors.notEspecific","No se pudo encontrar el archivo Incon al generarlo: "+e));
				return errores;
			}
			catch(IOException e)
			{
				logger.error("Error en los streams del archivo Incon: "+e);
				errores.add("",new ActionMessage("errors.notEspecific","Error en los streams del archivo Incon: "+e));
				return errores;
			}
		}
		return errores;
	}
	
	
	
	
	/**
	 * Método para editar los espacios del campo en el archivo RIPS
	 * @param campo
	 * @param tamano
	 * @param limite
	 * @param derecha : campo para saber si se llenan los espacios a izquierda o derecha 
	 * @return
	 */
	private static String editarEspacios(String campo, int tamano, int limite, boolean derecha,String caracter)
	{
		
		String aux;
		String espacios="";
		
		for(int i=tamano+1;i<=limite;i++)
			espacios+=caracter;
		
		if(derecha)
			aux=campo+espacios;
		else
			aux=espacios+campo;
		
		return aux;
	}

	/**
	 * Método que efectúa la anulación de un registro en el archivo
	 * @param infoInterfaz
	 * @param paciente
	 */
	public static String anulacionRegistroArchivo(HashMap infoInterfaz, PersonaBasica paciente,String cliente) 
	{
		String nombreArchivo="InterfazLabSoft.txt";
		boolean validacionCups = true;
		if(cliente.equals(ConstantesBD.clienteSUBA))
		{
			nombreArchivo="InterfazLabSoft.txt";
		}
		else
		{
			nombreArchivo="axioma"+infoInterfaz.get("numeroDocumento").toString()+".txt";
			//Se excluye la glucometrís y el paqueted e coagulación
			if(infoInterfaz.get("codigoCUPS_0").toString().equalsIgnoreCase("X00086")||
				infoInterfaz.get("codigoCUPS_0").toString().equalsIgnoreCase("M19275"))
			{
				validacionCups = false;
			}
		}
			
		String numeroSolicitud = infoInterfaz.get("numeroSolicitud_0").toString();
		int centroCosto = Integer.parseInt(infoInterfaz.get("centroCosto_0").toString());
		int institucion = Integer.parseInt(infoInterfaz.get("institucion").toString());
		String aux = "";
		String aux0 = "";
		boolean existe = false;
		
		//Se verifica si la solicitud tiene centro de costo tercero
		if(esCentroCostoTerceros(centroCosto,institucion)&&validacionCups)
		{
		
			//************SE CARGA EL ARCHIVO***************************************************************
			HashMap lineas = new HashMap();
			int numLineas = 0;
			try
			{
				String cadena = "";
				//******SE INICIALIZA ARCHIVO*************************
				File archivo=new File(ValoresPorDefecto.getFilePathInterfazLaboratorios(),nombreArchivo);
				FileReader stream=new FileReader(archivo); //se coloca false para el caso de que esté repetido
				BufferedReader buffer=new BufferedReader(stream);
				
				//********SE RECORRE LÍNEA POR LÍNEA**************
				cadena=buffer.readLine();
				while(cadena!=null)
				{
					lineas.put(numLineas+"",cadena);	
					cadena=buffer.readLine();
					numLineas ++;
				}
				//***************CERRAR ARCHIVO****************************
				buffer.close();
				//****************SE ELIMINA EL ARCHIVO********************
				archivo.delete();
			}
			catch(FileNotFoundException e)
			{
				logger.error("No se pudo encontrar el archivo "+nombreArchivo+" al cargarlo: "+e);
			}
			catch(IOException e)
			{
				logger.error("Error en los streams del archivo "+nombreArchivo+" al cargarlo: "+e);
			}
			//**********************************************************************************************
			
			//*************SE VERIFICA SI LA SOLICITUD ANULADA TODAVÍA EXISTE ********************************
			for(int i=0;i<numLineas;i++)
			{
				aux = lineas.get(i+"").toString();
				//si la cadena tiene longitud de 21 caracteres quiere decir que es un registro donde hay numeroSolicitud
				if(aux.length()==21)
				{
					//Se extrae el numero de solicitud de la linea
					aux0 = aux.substring(0,10);
					//Se compara si son iguales
					if(Integer.parseInt(numeroSolicitud)==Integer.parseInt(aux0.trim()))
					{
						//se activa bandera que dice que existe
						existe = true;
						//se cambia la linea con el nuevo estado
						aux = aux.substring(0,20) + ConstantesBD.codigoEstadoHCAnulada ;
						//se añade al arreglo
						lineas.put(i+"",aux);
					}
				}
				
				
			}
			//***************************************************************************************************
			
			//**************GENERACIÓN DEL ARCHIVO***************************************************************
			aux = "";
			
			//=========SE DEBE GENERAR ARCHIVO A PARTIR DEL ARREGLO MODIFICADO==============================
			aux = generarArchivoConMapaLineas(ValoresPorDefecto.getFilePathInterfazLaboratorios(),nombreArchivo,lineas,numLineas);
			if(aux.equals(""))
			{
				return "";
			}
			
			if(cliente.equals(ConstantesBD.clienteSHAIO)&&existe)
			{
				//*******************SE GENERA ARCHIVO BACKUP*******************************
				//Se elimina archivo si existe
				String[] horaActual = UtilidadFecha.getHoraActual().split(":");
				String nombreArchivoBackup = "axioma"+infoInterfaz.get("numeroDocumento").toString()+"-"+UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())+"-"+horaActual[0]+horaActual[1]+".txt";
				String directorioArchivoBackup = ValoresPorDefecto.getFilePathInterfazLaboratorios()+System.getProperty("file.separator")+"backup"+System.getProperty("file.separator");
				File archivo=new File(directorioArchivoBackup,nombreArchivoBackup);
				if(archivo.exists())
				{
					archivo.delete();
				}
				
				String auxBackup = generarArchivoConMapaLineas(directorioArchivoBackup,nombreArchivoBackup,lineas,numLineas);
				if(auxBackup.equals(""))
				{
					return "";
				}
				//****************************************************************************
			}
			//============================================================================================
			
			if(!existe)
			{
				//========SE ADICIONA REGISTRO AL ARCHIVO EXISTENTE==========================================
				generarRegistroArchivo(infoInterfaz, paciente, cliente, new ActionErrors());
				//===========================================================================================
			}
			
			
			
			
		}
		return aux;
	}
	
	/**
	 * 
	 * @param rutaDirectorio
	 * @param nombreArchivo
	 * @param lineas
	 * @param numLineas 
	 * @return
	 */
	private static String generarArchivoConMapaLineas(String rutaDirectorio,String nombreArchivo, HashMap lineas, int numLineas) 
	{
		try
		{
			
			String contenido = ""; //variable para almacenar el contenido del archivo
			
			//se valida el directorio*******************************************************
			File directorio=new File(rutaDirectorio);
			
			if(!directorio.isDirectory() && !directorio.exists())	
			{
				if(!directorio.mkdirs()) 
				{
					logger.error("Error creando el directorio "+rutaDirectorio);
					return "";
				}
			}
			
			//apertura del archivo Inconsistencias********************************
			File archivoIncon= new File(directorio,nombreArchivo);
			FileWriter streamIncon=new FileWriter(archivoIncon,true); 
			BufferedWriter bufferIncon=new BufferedWriter(streamIncon);
			
			//Se adiciona linea por linea
			for(int i=0;i<numLineas;i++)
			{
				contenido = lineas.get(i+"").toString();
				
				//cambio de orden
				if(contenido.length()>21&&i>0)
					contenido = "\r\n" + contenido;
				else if(contenido.length()==21)
					contenido = "\n" + contenido;
				
				
				bufferIncon.write(contenido);
			}
			if(numLineas>0)
				contenido = "\r\n";
			bufferIncon.write(contenido);
			bufferIncon.close();
			//Se cambian los permisos del archivos
			String[] cmd={"/bin/chmod","777",rutaDirectorio+nombreArchivo};
			Runtime.getRuntime().exec(cmd);
			
			
			return archivoIncon.getAbsolutePath();
		
		}
		catch(FileNotFoundException e)
		{
			logger.error("No se pudo encontrar el archivo Incon al generarlo: "+e);
			return "";
		}
		catch(IOException e)
		{
			logger.error("Error en los streams del archivo Incon: "+e);
			return "";
		}
	}

	/**
	 * Método para tomar informacion de la tabla RESLAB del AS400 y actualizar las estructuras
	 * de las tablas de respuesta de Axioma
	 * @param codigoInstitucion
	 * @param codigoUsuario
	 * @param pathAdjuntos
	 */
	public static void tomarInformacionTablaLaboratorioSHAIO01(int codigoInstitucion, int codigoUsuario, String pathAdjuntos) throws IPSException
	{
		//Se abre la conexión
		Connection con = UtilidadBD.abrirConexion();
		
		//Se instancia objeto de Solicitud
		SolicitudProcedimiento solicitud = new SolicitudProcedimiento();
		boolean resultado = false, existeSolicitud = false; //variable que indica si se procesó el resultado
		
		
		UtilidadBDInterfaz mundoInterfaz = new UtilidadBDInterfaz();
		ArrayList<HashMap<String, Object>> registros = mundoInterfaz.consultarRegistrosLaboratoriosPendientes(codigoInstitucion);
		
		//Se busca el login del usuario
		String loginUsuario = Usuario.buscarLoginEstatico(con, codigoUsuario, "POSTGRESQL");
		
		for(HashMap<String, Object> elemento:registros)
		{
			resultado = false;
			
			//Se toma el número de la solicitud
			String numeroSolicitud = elemento.get("numeroSolicitud").toString().trim();
			//Se toma el estado
			int estado = ConstantesBD.codigoNuncaValido;
			if(elemento.get("estado").toString().trim().equals("A"))
				estado = ConstantesBD.codigoEstadoHCAnulada;
			else if (elemento.get("estado").toString().trim().equals("P") || elemento.get("estado").toString().trim().equals("R"))
				estado = ConstantesBD.codigoEstadoHCRespondida;
			
			
			try
			{
				//Se carga la solicitud de procedimiento
				existeSolicitud = solicitud.cargarSolicitudProcedimiento(con,Integer.parseInt(numeroSolicitud));
			}
			catch(Exception e)
			{
				logger.error("Error en el procesamiento de la interfaz de laboratorios: "+e);
			}
			
			/**
			 * Se verifica que la solicitud existe para procesar el resultado
			 */
			if(existeSolicitud)
			{
				///Tomar informacion adicional de la solicitud
				HashMap infoAd = UtilidadLaboratorios.getInformacionAdicionalSolicitud(con,numeroSolicitud);
				logger.info("elemento encontrado: "+elemento);
				logger.info("estado Axioma: "+estado);
				
				switch(estado)
				{
					case ConstantesBD.codigoEstadoHCAnulada:
						resultado = actualizarAnulada(con, solicitud,codigoUsuario);
					break;
					case ConstantesBD.codigoEstadoHCRespondida:
						resultado = actualizarRespondidaSHAIO(con,solicitud,codigoUsuario,loginUsuario,pathAdjuntos,elemento,infoAd);
					break;
				}
			}
			else
			{
				resultado = true;
				logger.error("ERROR INTERFAZ LABORATORIOS: No existe la solicitud N° "+numeroSolicitud);
			}
			
			
			//Se registra la lectura como leída si hubo un buen resultado
			if(resultado)
			{
				mundoInterfaz.actualizarRegistroLaboratorioLeido(
					elemento.get("consecutivoIngreso").toString().trim(), 
					numeroSolicitud, 
					UtilidadFecha.getFechaActual(con), 
					UtilidadFecha.getHoraSegundosActual(con), 
					codigoInstitucion);
			}
			
		}
		
		//Se cierra la conexión
		UtilidadBD.closeConnection(con);
	}
	
	/**
	 * Método implementado para tomar la informacion de la tabla
	 * temporal de laboratorios
	 * @param pathAdjuntos 
	 *
	 */
	public static void tomarInformacionTablaLaboratorioSHAIO02(String pathAdjuntos)
	{
		//Se abre la conexión
		Connection con = UtilidadBD.abrirConexion();
		
		//Se instancia objeto de Solicitud
		SolicitudProcedimiento solicitud = new SolicitudProcedimiento();
		boolean resultado = false;
		boolean validacion = false;
		
		//Mapa donde se toma la informacion
		HashMap informacion = UtilidadLaboratorios.consultarInterfazLaboratorios(con);
		logger.info("Número de registros encontrados=> "+informacion.get("numRegistros"));
		for(int i=0;i<Integer.parseInt(informacion.get("numRegistros").toString());i++)
		{
			resultado = false;
			//Se toma el numero de solicitud
			String numeroSolicitud = informacion.get("num_solicitud_"+i).toString();
			//Se toma el estado de la solicitud
			int estado = Integer.parseInt(informacion.get("estado_"+i).toString());
			
			try
			{
				
				//Se carga la solicitud
				try 
				{
					solicitud.cargarSolicitudProcedimiento(con,Integer.parseInt(numeroSolicitud));
				} 
				catch (Exception e) 
				{
					logger.error("Error al cargar solicitud procedimiento en tomarInformacionTablaLaboratorio de InterfazLaboratorios: "+e);
				} 

				//Se realiza validacion del registro
				validacion = validarRegistroInterfazSHAIO(con,solicitud,informacion,i);
				
				//Si cumple validación efectúa la alimentación
				if(validacion)
				{
					///Tomar informacion adicional de la solicitud
					HashMap infoAd = UtilidadLaboratorios.getInformacionAdicionalSolicitud(con,numeroSolicitud);
						
					
					//Según el estado del registro se toman las acciones necesarias
					switch(estado)
					{
						case ConstantesBD.codigoEstadoHCEnProceso:
							resultado = actualizarEnProcesoSHAIO(con,solicitud,informacion,i,infoAd);
						break;
						case ConstantesBD.codigoEstadoHCRespondida:
							String usuarioModifica="axioma";
							/*
							 * Se quema este usuario porque:
							 * 1) Esta interfaz ya no se va a usar
							 * 2) Se corre desde un hilo automático que no tiene usuario
							 */
							resultado = actualizarRespondidaSHAIO(con,solicitud,informacion,i,infoAd,pathAdjuntos, usuarioModifica);
						break;
						case ConstantesBD.codigoEstadoHCNuevaMuestra:
							resultado = actualizarNuevaMuestra(con,solicitud,informacion,i);
						break;
							
					}
				}
				else
					resultado = true;
				
				if(resultado)
					///Se actualizan los registros como leidos
					UtilidadLaboratorios.actualizarLeidoInterfazLaboratorios(con,informacion.get("consecutivo_"+i).toString());
			}
			catch(Exception e)
			{
				UtilidadBD.abortarTransaccion(con);
				logger.error("Error al actualizar informacion de laboratorio de la solicitud "+numeroSolicitud+": "+e);
			}
		}
		
		
		
		//Se cierra la conexión
		UtilidadBD.closeConnection(con);
	}
	

	/**
	 * Método implementado para tomar la informacion de la tabla
	 * temporal de laboratorios
	 *
	 */
	public static void tomarInformacionTablaLaboratorioSUBA()
	{
		//Se abre la conexión
		Connection con = UtilidadBD.abrirConexion();
		
		//Se instancia objeto de Solicitud
		SolicitudProcedimiento solicitud = new SolicitudProcedimiento();
		boolean resultado = false;
		boolean validacion = false;
		
		//Mapa donde se toma la informacion
		HashMap informacion = UtilidadLaboratorios.consultarInterfazLaboratorios(con);
		logger.info("Número de registros encontrados=> "+informacion.get("numRegistros"));
		for(int i=0;i<Integer.parseInt(informacion.get("numRegistros").toString());i++)
		{
			resultado = false;
			//Se toma el numero de solicitud
			String numeroSolicitud = informacion.get("num_solicitud_"+i).toString();
			//Se toma el estado de la solicitud
			int estado = Integer.parseInt(informacion.get("estado_"+i).toString());
			
			try
			{
				
				//Se carga la solicitud
				try 
				{
					solicitud.cargarSolicitudProcedimiento(con,Integer.parseInt(numeroSolicitud));
				} 
				catch (Exception e) 
				{
					logger.error("Error al cargar solicitud procedimiento en tomarInformacionTablaLaboratorio de InterfazLaboratorios: "+e);
				} 

				//Se realiza validacion del registro
				validacion = validarRegistroInterfazSUBA(con,solicitud,informacion,i);
				
				//Si cumple validación efectúa la alimentación
				if(validacion)
				{
					///Tomar informacion adicional de la solicitud
					HashMap infoAd = UtilidadLaboratorios.getInformacionAdicionalSolicitud(con,numeroSolicitud);
						
					
					//Según el estado del registro se toman las acciones necesarias
					switch(estado)
					{
						case ConstantesBD.codigoEstadoHCEnProceso:
							resultado = actualizarEnProcesoSUBA(con,solicitud,informacion,i,infoAd);
						break;
						case ConstantesBD.codigoEstadoHCRespondida:
							resultado = actualizarRespondidaSUBA(con,solicitud,informacion,i,infoAd);
						break;
						case ConstantesBD.codigoEstadoHCNuevaMuestra:
							resultado = actualizarNuevaMuestra(con,solicitud,informacion,i);
						break;
							
					}
				}
				else
					resultado = true;
				
				if(resultado)
					///Se actualizan los registros como leidos
					UtilidadLaboratorios.actualizarLeidoInterfazLaboratorios(con,informacion.get("consecutivo_"+i).toString());
			}
			catch(Exception e)
			{
				UtilidadBD.abortarTransaccion(con);
				logger.error("Error al actualizar informacion de laboratorio de la solicitud "+numeroSolicitud+": "+e);
			}
		}
		
		
		
		//Se cierra la conexión
		UtilidadBD.closeConnection(con);
	}

	/**
	 * Método implementado para validar el registro que se lee de la tabla interfaz_laboratorios
	 * para interfaz con SUBA
	 * @param con
	 * @param solicitud
	 * @param informacion
	 * @param pos
	 * @return
	 */
	private static boolean validarRegistroInterfazSUBA(Connection con, SolicitudProcedimiento solicitud, HashMap informacion, int pos) 
	{
		boolean validacion = true;
		String numeroSolicitud = informacion.get("num_solicitud_"+pos).toString();
		int estado = Integer.parseInt(informacion.get("estado_"+pos).toString());
		String fecha = UtilidadFecha.conversionFormatoFechaAAp(informacion.get("fecha_"+pos).toString());
		String hora = UtilidadFecha.convertirHoraACincoCaracteres(informacion.get("hora_"+pos).toString());
		String descripcion = "";
		String consecutivo = informacion.get("consecutivo_"+pos).toString();;
		
		//1) Si el estado de la solicitud es respondida, los campos respuesta, path_pdf y profesional_responder no deben ser nulos.
		if(
			(
			estado==ConstantesBD.codigoEstadoHCRespondida&&(informacion.get("respuesta_"+pos).toString().equals("")||
			informacion.get("path_pdf_"+pos).toString().equals("")||Integer.parseInt(informacion.get("profesional_responde_"+pos).toString())<=0)
			)
			||
			//Caso adicional en que la solicitud es respuesta multiple sin finalizar
			(
			  (solicitud.getEstadoHistoriaClinica().getCodigo()==ConstantesBD.codigoEstadoHCRespondida||
			   solicitud.getEstadoHistoriaClinica().getCodigo()==ConstantesBD.codigoEstadoHCEnProceso)&&
			  Utilidades.tieneProcedimientoRespuestaMultiple(con,numeroSolicitud)&&
			  !Utilidades.estaFinalizadaRespuestaMultiple(con,numeroSolicitud)&&
			  estado==ConstantesBD.codigoEstadoHCEnProceso&&(informacion.get("respuesta_"+pos).toString().equals("")||
			  informacion.get("path_pdf_"+pos).toString().equals("")||Integer.parseInt(informacion.get("profesional_responde_"+pos).toString())<=0)
			  )
		)
		{
			validacion = false;
			descripcion = "Los datos de la respuesta están incompletos o son incorrectos. Por favor verificar";
			UtilidadLaboratorios.insertarLogInconsistencias(con,numeroSolicitud,estado,UtilidadFecha.getFechaActual(),UtilidadFecha.getHoraActual(),descripcion,consecutivo);
		}
		
		//2) El valor del campo usuario debe corresponder a un usuario del sistema.
		if(informacion.get("login_usuario_"+pos).toString().equals(""))
		{
			validacion = false;
			descripcion = "El campo usuario no corresponde a un usuario existente de Axioma. Por favor verificar";
			UtilidadLaboratorios.insertarLogInconsistencias(con,numeroSolicitud,estado,UtilidadFecha.getFechaActual(),UtilidadFecha.getHoraActual(),descripcion,consecutivo);
		}
		
		//3) La fecha/hora debe ser mayor/igual que la fecha/hora solicitud y menor/igual a la fecha/hora actual.
		if(!UtilidadFecha.compararFechas(fecha,hora,solicitud.getFechaSolicitud(),solicitud.getHoraSolicitud()).isTrue()||
			!UtilidadFecha.compararFechas(UtilidadFecha.getFechaActual(),UtilidadFecha.getHoraActual(),fecha,hora).isTrue())
		{
			validacion = false;
			descripcion = "La fecha/hora debe ser mayor o igual a la fecha/hora de solicitud ("+solicitud.getFechaSolicitud()+" - "+solicitud.getHoraSolicitud()+") y " +
				"menor o igual a la fecha/hora actual ("+UtilidadFecha.getFechaActual()+" - "+UtilidadFecha.getHoraActual()+"). Por favor verificar";
			UtilidadLaboratorios.insertarLogInconsistencias(con,numeroSolicitud,estado,UtilidadFecha.getFechaActual(),UtilidadFecha.getHoraActual(),descripcion,consecutivo);
		}
		
		return validacion;
	}
	
	/**
	 * Método implementado para validar el registro que se lee de la tabla interfaz_laboratorios 
	 * para interfaz con SHAIO
	 * @param con
	 * @param solicitud
	 * @param informacion
	 * @param pos
	 * @return
	 */
	private static boolean validarRegistroInterfazSHAIO(Connection con, SolicitudProcedimiento solicitud, HashMap informacion, int pos) 
	{
		boolean validacion = true;
		String numeroSolicitud = informacion.get("num_solicitud_"+pos).toString();
		int estado = Integer.parseInt(informacion.get("estado_"+pos).toString());
		String fecha = UtilidadFecha.conversionFormatoFechaAAp(informacion.get("fecha_"+pos).toString());
		String hora = UtilidadFecha.convertirHoraACincoCaracteres(informacion.get("hora_"+pos).toString());
		String descripcion = "";
		String consecutivo = informacion.get("consecutivo_"+pos).toString();;
		
		//1) Si el estado de la solicitud es respondida, los campos respuesta, path_pdf y profesional_responder no deben ser nulos.
		if(
			
			estado==ConstantesBD.codigoEstadoHCRespondida&&
			Integer.parseInt(informacion.get("profesional_responde_"+pos).toString())<=0
			
		)
		{
			validacion = false;
			descripcion = "El campo de profesional que responde es inválido. Por favor verificar";
			UtilidadLaboratorios.insertarLogInconsistencias(con,numeroSolicitud,estado,UtilidadFecha.getFechaActual(),UtilidadFecha.getHoraActual(),descripcion,consecutivo);
		}
		
		//2) El valor del campo usuario debe corresponder a un usuario del sistema.
		if(informacion.get("login_usuario_"+pos).toString().equals(""))
		{
			validacion = false;
			descripcion = "El campo usuario no corresponde a un usuario existente de Axioma. Por favor verificar";
			UtilidadLaboratorios.insertarLogInconsistencias(con,numeroSolicitud,estado,UtilidadFecha.getFechaActual(),UtilidadFecha.getHoraActual(),descripcion,consecutivo);
		}
		
		//3) La fecha/hora debe ser mayor/igual que la fecha/hora solicitud y menor/igual a la fecha/hora actual.
		if(!UtilidadFecha.compararFechas(fecha,hora,solicitud.getFechaSolicitud(),solicitud.getHoraSolicitud()).isTrue()||
			!UtilidadFecha.compararFechas(UtilidadFecha.getFechaActual(),UtilidadFecha.getHoraActual(),fecha,hora).isTrue())
		{
			validacion = false;
			descripcion = "La fecha/hora debe ser mayor o igual a la fecha/hora de solicitud ("+solicitud.getFechaSolicitud()+" - "+solicitud.getHoraSolicitud()+") y " +
				"menor o igual a la fecha/hora actual ("+UtilidadFecha.getFechaActual()+" - "+UtilidadFecha.getHoraActual()+"). Por favor verificar";
			UtilidadLaboratorios.insertarLogInconsistencias(con,numeroSolicitud,estado,UtilidadFecha.getFechaActual(),UtilidadFecha.getHoraActual(),descripcion,consecutivo);
		}
		
		return validacion;
	}

	/**
	 * Método implementado para actualizar las solicitudes que hayan pasado a Nueva Muestra
	 * @param con
	 * @param solicitud
	 * @param informacion
	 * @param pos
	 */
	private static boolean actualizarNuevaMuestra(Connection con, SolicitudProcedimiento solicitud, HashMap informacion, int pos) 
	{
		int estadoSolicitud = solicitud.getEstadoHistoriaClinica().getCodigo();
		boolean exito = false;
		
		//Se inicia transaccion
		UtilidadBD.iniciarTransaccion(con);
		
		//Se verifica estado de la solicitud
		if(estadoSolicitud==ConstantesBD.codigoEstadoHCSolicitada||estadoSolicitud==ConstantesBD.codigoEstadoHCTomaDeMuestra||
			estadoSolicitud==ConstantesBD.codigoEstadoHCEnProceso)
		{
			exito = UtilidadLaboratorios.pasarSolicitudATomaMuestras(
				con,
				informacion.get("num_solicitud_"+pos).toString(),
				informacion.get("fecha_"+pos).toString(),
				informacion.get("hora_"+pos).toString(),
				informacion.get("login_usuario_"+pos).toString(),
				ConstantesBD.codigoEstadoHCSolicitada);
			
		}
		//Se genera inconsistencia porque se trató de pasar a solicitada una orden que ya fue respondida
		else 
		{
			String descripcion = "Se intentó registrar nueva muestra a una solicitud en estado "+solicitud.getEstadoHistoriaClinica().getNombre()+". Por favor verificar";
			UtilidadLaboratorios.insertarLogInconsistencias(con,informacion.get("num_solicitud_"+pos).toString(),ConstantesBD.codigoEstadoHCRespondida,UtilidadFecha.getFechaActual(),UtilidadFecha.getHoraActual(),descripcion,informacion.get("consecutivo_"+pos).toString());
			exito = true;
		}
		
		if(exito)
			UtilidadBD.finalizarTransaccion(con);
		else
			UtilidadBD.abortarTransaccion(con);
		return exito;
	}

	/**
	 * Método implementado para registrar la respuesta de procedimiento
	 * para el cliente SHAIO
	 * @param con
	 * @param solicitud
	 * @param codigoUsuario
	 * @param loginUsuario
	 * @param pathAdjuntos 
	 * @param elemento 
	 * @param infoAd 
	 * @return
	 */
	private static boolean actualizarRespondidaSHAIO(Connection con,
			SolicitudProcedimiento solicitud, int codigoUsuario, String loginUsuario, String pathAdjuntos, HashMap<String, Object> elemento, HashMap infoAd) throws IPSException
	{
		boolean exito = false,validacion = true;
		int estadoSolicitud = solicitud.getEstadoHistoriaClinica().getCodigo();
		String fechaSistema = UtilidadFecha.getFechaActual(con);
		String[] vector = fechaSistema.split("/");
		String horaSistema = UtilidadFecha.getHoraActual(con);
		
		//Se toma la fecha del laboratorios
		String aux = elemento.get("fechaLaboratorio").toString().trim();
		String fechaGrabacion = aux.substring(0, 4) + "-" + aux.substring(4, 6) + "-" + aux.substring(6, 8);
		//Se toma la hora del laboratorio
		aux = elemento.get("horaLaboratorio").toString().trim();
		String horaGrabacion = aux.substring(0, 2) + ":" + aux.substring(2, 4) ;
		
		//Se verifica estado de la solicitud
		if(estadoSolicitud==ConstantesBD.codigoEstadoHCSolicitada||estadoSolicitud==ConstantesBD.codigoEstadoHCTomaDeMuestra||
			estadoSolicitud==ConstantesBD.codigoEstadoHCEnProceso)
		{
			
			//**********SE REALIZA VALIDACION DE LAS FECHAS**************************************************************
			logger.info("fecha grabacion: "+UtilidadFecha.conversionFormatoFechaAAp(fechaGrabacion));
			logger.info("hora grabacion: "+horaGrabacion);
			logger.info("fecha solicitud: "+solicitud.getFechaSolicitud());
			logger.info("hora solicitud: "+solicitud.getHoraSolicitud());
			logger.info("fecha sistema: "+fechaSistema);
			logger.info("hora sistema: "+horaSistema);
			if(!UtilidadFecha.compararFechas(UtilidadFecha.conversionFormatoFechaAAp(fechaGrabacion),horaGrabacion,solicitud.getFechaSolicitud(),solicitud.getHoraSolicitud()).isTrue()||
					!UtilidadFecha.compararFechas(fechaSistema,horaSistema,UtilidadFecha.conversionFormatoFechaAAp(fechaGrabacion),horaGrabacion).isTrue())
			{
				validacion = false;
				
				aux = "La fecha/hora debe ser mayor o igual a la fecha/hora de solicitud ("+solicitud.getFechaSolicitud()+" - "+solicitud.getHoraSolicitud()+") y " +
					"menor o igual a la fecha/hora actual ("+fechaSistema+" - "+horaSistema+"). Por favor verificar";
				UtilidadLaboratorios.insertarLogInconsistencias(con,solicitud.getNumeroSolicitud()+"",ConstantesBD.codigoEstadoHCRespondida,fechaSistema,horaSistema,aux,"");
			}
			//*******************************************************************************************************
			
			
			if(validacion)
			{
					UtilidadBD.iniciarTransaccion(con);
					///*********SE GENERA EL ARCHIVO PLANO QUE TIENE LA RUTA********************
					File directorio=new File(pathAdjuntos+"laboratorios/");
					Random random = new Random();
					String contenido = "";
					String nombreArchivo = vector[0]+"-"+vector[1]+"-"+vector[2]+"-"+random.nextInt()+".html"; 
					try
					{
						if(!directorio.exists())
							directorio.mkdirs();
						
						File archivo= new File(directorio,nombreArchivo);
						FileWriter streamAdj=new FileWriter(archivo,true); 
						BufferedWriter bufferAdj=new BufferedWriter(streamAdj);
						contenido = "" +
							"<script language=\"javascript\">" +
								"location.href = 'http://servidorweblab/cgi/Usuario.cgi?AccionServidor=AccionImprimirNShaio&Alias=HIS&Clave=HIS&NShaio="+elemento.get("consecutivoIngreso")+"&CodProcedimiento="+solicitud.getNumeroSolicitud()+"';" +
							"</script>";
						bufferAdj.write(contenido);
						bufferAdj.close();
						
					}
					catch(FileNotFoundException e)
					{
						logger.error("Error al crear el archivo adjunto en la interfaz de laboratorio: "+e);
					}
					catch(IOException e1)
					{
						logger.error("Error de in/out al generar el archivo adjunto en la interfaz de laboratorios: "+e1);
					}
					//*************************************************************************
					
					
					boolean finalizar = false;
					
					if(Utilidades.tieneProcedimientoRespuestaMultiple(con,solicitud.getNumeroSolicitud()+""))
						finalizar = true;
					
					exito = UtilidadLaboratorios.insertarRespuestaProcedimientos(
						con,
						solicitud.getNumeroSolicitud()+"",
						fechaGrabacion,
						horaGrabacion,
						"",
						ConstantesBD.codigoTipoRecargoSinRecargo,"","",
						nombreArchivo,
						codigoUsuario+"",
						loginUsuario,
						infoAd,
						finalizar); 
					
					//siempre se recalcula el cargo
					if(exito)
						exito = generarCargoSolicitud(con,solicitud.getNumeroSolicitud()+"",loginUsuario,ConstantesBD.codigoEstadoHCRespondida,infoAd);
						
					if(exito)
						UtilidadBD.finalizarTransaccion(con);
					else
						UtilidadBD.abortarTransaccion(con);
			}
			else
				exito = true;
		}
		else
		{
			exito = true;
		}
		
		
		
		
		
		
		return exito;
	}
	
	/**
	 * Método implementado para actualizar las solicitudes que hayan pasado a estado Respondida
	 * @param con
	 * @param solicitud
	 * @param informacion
	 * @param pos
	 * @param infoAd 
	 */
	private static boolean actualizarRespondidaSUBA(Connection con, SolicitudProcedimiento solicitud, HashMap informacion, int pos, HashMap infoAd) throws IPSException
	{
		int estadoSolicitud = solicitud.getEstadoHistoriaClinica().getCodigo();
		String numeroSolicitud = informacion.get("num_solicitud_"+pos).toString();
		boolean exito = false;
		
		//Se inicia transaccion
		UtilidadBD.iniciarTransaccion(con);
		
		//Se verifica estado de la solicitud
		if(estadoSolicitud==ConstantesBD.codigoEstadoHCSolicitada||estadoSolicitud==ConstantesBD.codigoEstadoHCTomaDeMuestra||
			estadoSolicitud==ConstantesBD.codigoEstadoHCEnProceso)
		{
			boolean finalizar = false;
			
			if(Utilidades.tieneProcedimientoRespuestaMultiple(con,numeroSolicitud))
				finalizar = true;
			
			exito = UtilidadLaboratorios.insertarRespuestaProcedimientos(
				con,
				numeroSolicitud,
				informacion.get("fecha_"+pos).toString(),
				informacion.get("hora_"+pos).toString(),
				informacion.get("respuesta_"+pos).toString(),
				ConstantesBD.codigoTipoRecargoSinRecargo,"","",
				informacion.get("path_pdf_"+pos).toString(),
				informacion.get("profesional_responde_"+pos).toString(),
				informacion.get("login_usuario_"+pos).toString(),
				infoAd,
				finalizar); 
			
			//siempre se recalcula el cargo
			exito = generarCargoSolicitud(con,numeroSolicitud,informacion.get("login_usuario_"+pos).toString(),ConstantesBD.codigoEstadoHCRespondida,infoAd);
				
			
		}
		//Esta Respondida y tiene respuesta multiple sin finalizar
		else if(estadoSolicitud==ConstantesBD.codigoEstadoHCRespondida&&
				Utilidades.tieneProcedimientoRespuestaMultiple(con,numeroSolicitud)&&
				!Utilidades.estaFinalizadaRespuestaMultiple(con,numeroSolicitud))
		{
			boolean reproceso = UtilidadTexto.getBoolean(informacion.get("reproceso_"+pos).toString());
			//no debe tener reproceso
			if(!reproceso)
			{
				
				exito = UtilidadLaboratorios.insertarRespuestaProcedimientos(
						con,
						numeroSolicitud,
						informacion.get("fecha_"+pos).toString(),
						informacion.get("hora_"+pos).toString(),
						informacion.get("respuesta_"+pos).toString(),
						ConstantesBD.codigoTipoRecargoSinRecargo,"","",
						informacion.get("path_pdf_"+pos).toString(),
						informacion.get("profesional_responde_"+pos).toString(),
						informacion.get("login_usuario_"+pos).toString(),
						infoAd,
						true);
				
			}
			else
				exito = true;
			
		}
		//Esta Respondida y tiene repsuesta multiple finalizada
		//Esta Respondida y no tiene respuesta multiple
		//Esta Interpretada
		else if(
			(estadoSolicitud==ConstantesBD.codigoEstadoHCRespondida&&
			Utilidades.tieneProcedimientoRespuestaMultiple(con,numeroSolicitud)&&
			Utilidades.estaFinalizadaRespuestaMultiple(con,numeroSolicitud))||
			(estadoSolicitud==ConstantesBD.codigoEstadoHCRespondida&&
			!Utilidades.tieneProcedimientoRespuestaMultiple(con,numeroSolicitud))||
			estadoSolicitud==ConstantesBD.codigoEstadoHCInterpretada)
		{
			boolean reproceso = UtilidadTexto.getBoolean(informacion.get("reproceso_"+pos).toString());
			//debe ser reproceso
			if(reproceso)
			{
				boolean tiempoReproceso = UtilidadLaboratorios.validarTiempoReproceso(
					con,
					numeroSolicitud,
					UtilidadFecha.conversionFormatoFechaAAp(informacion.get("fecha_"+pos).toString()),
					informacion.get("hora_"+pos).toString());
				
				if(tiempoReproceso)
				{
					//Se ajusta la respuesta por el reproceso
					String respuesta = informacion.get("respuesta_"+pos).toString() + 
						"\n\nProcedimiento Reprocesado ("+
						UtilidadFecha.conversionFormatoFechaAAp(informacion.get("fecha_"+pos).toString())+" - "+
						informacion.get("hora_"+pos).toString()+") "+
						"\n"+informacion.get("motivo_reproceso_"+pos).toString();
					
					exito = UtilidadLaboratorios.insertarRespuestaProcedimientos(
							con,
							numeroSolicitud,
							informacion.get("fecha_"+pos).toString(),
							informacion.get("hora_"+pos).toString(),
							respuesta,
							ConstantesBD.codigoTipoRecargoSinRecargo,"","",
							informacion.get("path_pdf_"+pos).toString(),
							informacion.get("profesional_responde_"+pos).toString(),
							informacion.get("login_usuario_"+pos).toString(),
							infoAd,
							false);
				}
				else
					exito = true;
				
				
			}
			else
				exito = true;
		}
		else
			exito = true;
		
		
		if(exito)
			UtilidadBD.finalizarTransaccion(con);
		else
			UtilidadBD.abortarTransaccion(con);
		
		return exito;
	}
	
	/**
	 * Método implementado para actualizar las solicitudes que hayan pasado a estado Respondida
	 * @param con
	 * @param solicitud
	 * @param informacion
	 * @param pos
	 * @param infoAd 
	 * @param pathAdjuntos 
	 * @param usuarioModifica TODO
	 */
	private static boolean actualizarRespondidaSHAIO(Connection con, SolicitudProcedimiento solicitud, HashMap informacion, int pos, HashMap infoAd, String pathAdjuntos, String usuarioModifica) throws IPSException 
	{
		int estadoSolicitud = solicitud.getEstadoHistoriaClinica().getCodigo();
		String numeroSolicitud = informacion.get("num_solicitud_"+pos).toString();
		boolean exito = false;
		String fechaSistema = UtilidadFecha.getFechaActual(con);
		String[] vector = fechaSistema.split("/");
		
		//Se inicia transaccion
		UtilidadBD.iniciarTransaccion(con);
		
		//Se verifica estado de la solicitud
		if(estadoSolicitud==ConstantesBD.codigoEstadoHCSolicitada||estadoSolicitud==ConstantesBD.codigoEstadoHCTomaDeMuestra||
			estadoSolicitud==ConstantesBD.codigoEstadoHCEnProceso)
		{
			boolean finalizar = false;
			
			if(Utilidades.tieneProcedimientoRespuestaMultiple(con,numeroSolicitud))
				finalizar = true;
			
			////*********SE GENERA EL ARCHIVO PLANO QUE TIENE LA RUTA********************
			logger.info("path adjuntos: "+pathAdjuntos+"laboratorios/");
			File directorio=new File(pathAdjuntos+"laboratorios/");
			Random random = new Random();
			String contenido = "";
			String nombreArchivo = vector[0]+"-"+vector[1]+"-"+vector[2]+"-"+random.nextInt()+".html"; 
			try
			{
				if(!directorio.exists())
					directorio.mkdirs();
				
				File archivo= new File(directorio,nombreArchivo);
				FileWriter streamAdj=new FileWriter(archivo,true); 
				BufferedWriter bufferAdj=new BufferedWriter(streamAdj);
				contenido = "<IFRAME   id=\"frameSolicitud\" frameborder=\"0\" SRC=\"http://193.1.1.249/cgi/Impresion.cgi?Alias=HIS&Clave=HIS&nAxioma="+solicitud.getNumeroDocumento()+"\"  NAME=\"frameSolicitud\" WIDTH=\"100%\" HEIGHT=\"100%\" ALIGN=\"center\" SCROLLING=\"yes\">"+
							"</IFRAME>";
				bufferAdj.write(contenido);
				bufferAdj.close();
				
			}
			catch(FileNotFoundException e)
			{
				logger.error("Error al crear el archivo adjunto en la interfaz de laboratorio: "+e);
			}
			catch(IOException e1)
			{
				logger.error("Error de in/out al generar el archivo adjunto en la interfaz de laboratorios: "+e1);
			}
			//*************************************************************************
			
			exito = UtilidadLaboratorios.insertarRespuestaProcedimientos(
				con,
				numeroSolicitud,
				informacion.get("fecha_"+pos).toString(),
				informacion.get("hora_"+pos).toString(),
				informacion.get("respuesta_"+pos).toString(),
				ConstantesBD.codigoTipoRecargoSinRecargo,"","",
				nombreArchivo,
				informacion.get("profesional_responde_"+pos).toString(),
				informacion.get("login_usuario_"+pos).toString(),
				infoAd,
				finalizar); 
			
			//siempre se recalcula el cargo
			exito = generarCargoSolicitud(con,numeroSolicitud,informacion.get("login_usuario_"+pos).toString(),ConstantesBD.codigoEstadoHCRespondida,infoAd);
			
			//******************VALIDACION CONSULTA EXTERNA********************************************+
			if(exito)
			{
				try
				{
					String codCitaTemp=RespuestaProcedimientos.validacionCita(con,Utilidades.convertirAEntero(numeroSolicitud));
					//Si la solicitud hace parte de una cita, se prosigue a actualizar la cita a cumplida si se puede
					if(!codCitaTemp.equals(""))
					{
						Cita cita = new Cita();
						
						ResultadoBoolean resp = cita.actualizarEstadoCitaTransaccional(con, ConstantesBD.codigoEstadoCitaAtendida, Integer.parseInt(codCitaTemp), ConstantesBD.continuarTransaccion, usuarioModifica);
						if(!resp.isTrue())
						{
							logger.error("Problemas tratando de actualizar el estado de la cita de la solicitud "+numeroSolicitud+" (interfaz laboratorios) "+resp.getDescripcion());
							exito = false;
						}
						
						//Se verifica si se debe interpretar--------------------------------------------
						boolean deboInterpretar = false;
						int viaIngreso = Integer.parseInt(infoAd.get("codigo_via_ingreso").toString());
						
						if(viaIngreso == ConstantesBD.codigoViaIngresoConsultaExterna || 
							viaIngreso == ConstantesBD.codigoViaIngresoAmbulatorios ||
							!RespuestaProcedimientos.servicioRequiereInterpretacion(con,Utilidades.convertirAEntero(numeroSolicitud)))
						{
							deboInterpretar = true;
						}
						
						if(deboInterpretar)
						{
							//La solicitud se debe interpetar cuando viene de consulta externa
							HashMap campos = new HashMap();
							campos.put("interpretacion", "");
							campos.put("codigoMedico", informacion.get("profesional_responde_"+pos).toString()+"");
							campos.put("fechaInterpretacion", UtilidadFecha.getFechaActual(con));
							campos.put("horaInterpretacion", UtilidadFecha.getHoraActual(con));
							campos.put("numeroSolicitud", numeroSolicitud+"");
							
							int resp0 = Solicitud.interpretarSolicitud(con, campos);
							if(resp0<=0)
							{
								logger.error("Problemas tratando de interpretar la solicitud "+numeroSolicitud+" (interfaz laboratorios) "+resp.getDescripcion());
								exito = false;
							}
						}
						//------------------------------------------------------------------------------------
					}
				}
				catch(Exception e)
				{
					exito = false;
					logger.error("Problemas tratando de actualizar el estado de la cita de la solicitud "+numeroSolicitud+" (interfaz laboratorios) "+e);
				}
				
			}
			//*******************************************************************************************
				
			
		}
		//Se genera inconsistencia porque se trató de pasar a respondida una orden que ya fue respondida
		else 
		{
			String descripcion = "Se intentó registrar respuesta a una solicitud en estado "+solicitud.getEstadoHistoriaClinica().getNombre()+". Por favor verificar";
			UtilidadLaboratorios.insertarLogInconsistencias(con,numeroSolicitud,ConstantesBD.codigoEstadoHCRespondida,UtilidadFecha.getFechaActual(),UtilidadFecha.getHoraActual(),descripcion,informacion.get("consecutivo_"+pos).toString());
			exito = true;
		}
		
		
		if(exito)
			UtilidadBD.finalizarTransaccion(con);
		else
			UtilidadBD.abortarTransaccion(con);
		
		return exito;
	}

	/**
	 * Método implementado para actualizar las solicitudes que hayan pasado a estado en Proceso
	 * para la interfaz con SUBA
	 * @param con
	 * @param solicitud
	 * @param informacion
	 * @param pos
	 * @param infoAd 
	 * @return
	 */
	private static boolean actualizarEnProcesoSUBA(Connection con, SolicitudProcedimiento solicitud, HashMap informacion, int pos, HashMap infoAd) throws IPSException
	{
		int estadoSolicitud = solicitud.getEstadoHistoriaClinica().getCodigo();
		String numeroSolicitud = informacion.get("num_solicitud_"+pos).toString();
		boolean exito = false;
		
		//Se inicia transaccion
		UtilidadBD.iniciarTransaccion(con);
		//Se verifica estado de la solicitud
		if(estadoSolicitud==ConstantesBD.codigoEstadoHCSolicitada||estadoSolicitud==ConstantesBD.codigoEstadoHCTomaDeMuestra)
		{
			exito = UtilidadLaboratorios.pasarSolicitudAEnProceso(
					con,
					numeroSolicitud,
					informacion.get("fecha_"+pos).toString(),
					informacion.get("hora_"+pos).toString(),
					informacion.get("login_usuario_"+pos).toString()
				);
			
			//Siempre se recalcula el cargo.
			exito = generarCargoSolicitud(con,numeroSolicitud,informacion.get("login_usuario_"+pos).toString(),ConstantesBD.codigoEstadoHCEnProceso,infoAd);
			
			//Se verifica si es respuesta multiple y tiene respuesta
			if(Utilidades.tieneProcedimientoRespuestaMultiple(con,numeroSolicitud)&&
				!informacion.get("respuesta_"+pos).toString().equals("")&&
				!informacion.get("path_pdf_"+pos).toString().equals("")&&
				Integer.parseInt(informacion.get("profesional_responde_"+pos).toString())>0)
			{
				exito = UtilidadLaboratorios.insertarRespuestaProcedimientos(
						con,
						numeroSolicitud,
						informacion.get("fecha_"+pos).toString(),
						informacion.get("hora_"+pos).toString(),
						informacion.get("respuesta_"+pos).toString(),
						ConstantesBD.codigoTipoRecargoSinRecargo,"","",
						informacion.get("path_pdf_"+pos).toString(),
						informacion.get("profesional_responde_"+pos).toString(),
						informacion.get("login_usuario_"+pos).toString(),
						infoAd,
						false);
				
				if(exito)
					exito = Solicitud.cambiarEstadosSolicitudStatico(con,Integer.parseInt(numeroSolicitud),0,ConstantesBD.codigoEstadoHCEnProceso).isTrue();
			}
			
		}
		//Se verifica solicitud respondida con respuesta múltiple sin finalizar
		else if((estadoSolicitud==ConstantesBD.codigoEstadoHCEnProceso||estadoSolicitud==ConstantesBD.codigoEstadoHCRespondida)&&
			Utilidades.tieneProcedimientoRespuestaMultiple(con,numeroSolicitud)&&
			!Utilidades.estaFinalizadaRespuestaMultiple(con,numeroSolicitud))
		{
			exito = UtilidadLaboratorios.insertarRespuestaProcedimientos(
					con,
					numeroSolicitud,
					informacion.get("fecha_"+pos).toString(),
					informacion.get("hora_"+pos).toString(),
					informacion.get("respuesta_"+pos).toString(),
					ConstantesBD.codigoTipoRecargoSinRecargo,"","",
					informacion.get("path_pdf_"+pos).toString(),
					informacion.get("profesional_responde_"+pos).toString(),
					informacion.get("login_usuario_"+pos).toString(),
					infoAd,
					false);
		}
		else
			exito = true;
		
			
		if(exito)
			UtilidadBD.finalizarTransaccion(con);
		else
			UtilidadBD.abortarTransaccion(con);
		
		return exito;
	}
	
	/**
	 * Método implementado para actualizar las solicitudes que hayan pasado a estado en Proceso
	 * @param con
	 * @param solicitud
	 * @param informacion
	 * @param pos
	 * @param infoAd 
	 * @return
	 */
	private static boolean actualizarEnProcesoSHAIO(Connection con, SolicitudProcedimiento solicitud, HashMap informacion, int pos, HashMap infoAd) throws IPSException
	{
		int estadoSolicitud = solicitud.getEstadoHistoriaClinica().getCodigo();
		String numeroSolicitud = informacion.get("num_solicitud_"+pos).toString();
		boolean exito = false;
		
		//Se inicia transaccion
		UtilidadBD.iniciarTransaccion(con);
		//Se verifica estado de la solicitud
		if(estadoSolicitud==ConstantesBD.codigoEstadoHCSolicitada||estadoSolicitud==ConstantesBD.codigoEstadoHCTomaDeMuestra)
		{
			exito = UtilidadLaboratorios.pasarSolicitudAEnProceso(
					con,
					numeroSolicitud,
					informacion.get("fecha_"+pos).toString(),
					informacion.get("hora_"+pos).toString(),
					informacion.get("login_usuario_"+pos).toString()
				);
			
			//Siempre se recalcula el cargo.
			exito = generarCargoSolicitud(con,numeroSolicitud,informacion.get("login_usuario_"+pos).toString(),ConstantesBD.codigoEstadoHCEnProceso,infoAd);
			
			
			
		}
		//Se genera inconsistencia porque se trató de pasar en proceso una orden que ya fue puesta en proceso
		else 
		{
			String descripcion = "Se intentó registrar en proceso a una solicitud en estado "+solicitud.getEstadoHistoriaClinica().getNombre()+". Por favor verificar";
			UtilidadLaboratorios.insertarLogInconsistencias(con,numeroSolicitud,ConstantesBD.codigoEstadoHCEnProceso,UtilidadFecha.getFechaActual(),UtilidadFecha.getHoraActual(),descripcion,informacion.get("consecutivo_"+pos).toString());
			exito = true;
		}
		
		
			
		if(exito)
			UtilidadBD.finalizarTransaccion(con);
		else
			UtilidadBD.abortarTransaccion(con);
		
		return exito;
	}
	
	
	/**
	 * Método para registrar la anulación de una solicitud
	 * @param con
	 * @param solicitud
	 * @param codigoUsuario 
	 * @return
	 */
	private static boolean actualizarAnulada(Connection con, SolicitudProcedimiento solicitud, int codigoUsuario)
	{
		boolean exito = false;
		
		//Solo las solicitudes que tengan estado solicitada y toma de muestra se pueden anular
		if(solicitud.getEstadoHistoriaClinica().getCodigo()==ConstantesBD.codigoEstadoHCSolicitada||
			solicitud.getEstadoHistoriaClinica().getCodigo()==ConstantesBD.codigoEstadoHCAnulada)
		{
			solicitud.setMotivoAnulacion("Solicitud anulada desde interfaz de laboratorios (LABSOFT)");
			solicitud.setCodigoMedicoAnulacion(codigoUsuario);
			try
			{
				if(solicitud.anularSolicitudTransaccional(con, ConstantesBD.continuarTransaccion)>0)
					exito = true;
			}
			catch(SQLException e)
			{
				logger.error("Error al realizar la anulacion de la solicitud: "+e);
				exito = false;
			}
			
		}
		else
		{
			UtilidadLaboratorios.insertarLogInconsistencias(
				con, 
				solicitud.getNumeroSolicitud()+"", 
				ConstantesBD.codigoEstadoHCAnulada, 
				UtilidadFecha.getFechaActual(con), 
				UtilidadFecha.getHoraActual(con), 
				"La solicitud no pude ser anulada porque se encuentra en estado "+solicitud.getEstadoHistoriaClinica().getNombre(), 
				"");
			exito = true;
		}
		return exito;
	}

	

	/**
	 * Método implementado para generar el cargo de la solicitud
	 * @param con
	 * @param numeroSolicitud
	 * @param loginUsuario 
	 * @param estadoHC 
	 * @param info 
	 * @return
	 */
	private static boolean generarCargoSolicitud(Connection con,String numeroSolicitud, String loginUsuario, int estadoHC, HashMap info) throws IPSException
	{
		boolean exito = true;
		
		///Se carga cuenta
		Cuenta cuenta = new Cuenta();
		cuenta.cargarCuenta(con,info.get("cuenta").toString());
		
		
		
		if(UtilidadValidacion.esServicioViaIngresoCargoProceso(con,cuenta.getCodigoViaIngreso(),info.get("codigo_servicio").toString(),info.get("institucion").toString())||
			estadoHC==ConstantesBD.codigoEstadoHCRespondida)
		{
		
			//Se carga contrato
			Contrato contrato = new Contrato();
			contrato.cargar(con,cuenta.getCodigoContrato()+"");
			
			UsuarioBasico usuario= new UsuarioBasico();
			try 
			{
				usuario.cargarUsuarioBasico(con, loginUsuario);
			} 
			catch (SQLException e1) 
			{
				e1.printStackTrace();
			}
			
			//Se genera el cargo
			Cargos cargos = new Cargos();
			//cargo.setEsCita(false); //se dice que no es cita para que pueda generar cargo pendiente en caso de que no haya tarifa
			//cargo.setServicio(Integer.parseInt(info.get("codigo_servicio").toString()));
			try 
			{
				/*cargo.generarCargoTransaccional(
					con,
					Integer.parseInt(numeroSolicitud),
					Integer.parseInt(info.get("centro_costo").toString()),
					cuenta.getCodigoContrato(),
					!UtilidadTexto.getBoolean(UtilidadValidacion.revisarValidezContrato(con,Integer.parseInt(cuenta.getIdCuenta()))[3]),
					Integer.parseInt(cuenta.getCodigoViaIngreso()),
					ConstantesBD.codigoTipoSolicitudProcedimiento,
					esquemaTarifario.getTarifarioOficial().getCodigo(),
				    loginUsuario,
				    1,
				    0,
				    Integer.parseInt(info.get("codigo_servicio").toString()),
				    false,
					"",//aqui van las observaciones cuando se requieran
					ConstantesBD.continuarTransaccion,
					false,/*utilizarValorTarifaOpcional
					-1/*valorTarifaOpcional);*/
				//como solo es 1 servicio entonces no puede tener n responsables con cargo pendiente entonces le enviamos el convenio vacio
				cargos.recalcularCargoServicio(con, Integer.parseInt(numeroSolicitud), usuario, ConstantesBD.codigoNuncaValido/*codigoEvolucionOPCIONAL*/, "" /*observaciones*/, Integer.parseInt(info.get("codigo_servicio").toString()) /*CodigoServicioOPCIONAL*/, ConstantesBD.codigoNuncaValido /*subCuentaResponsable*/, ConstantesBD.codigoNuncaValido /*codigoEsquemaTarifarioOPCIONAL*/, false/*filtrarSoloCantidadesMayoresCero*/, ConstantesBD.acronimoNo /*esComponentePaquete*/, ""/*esPortatil*/,"");
			} 
			catch (Exception e) 
			{
				exito = false;
				logger.error("Error al generar el cargo en generarCargoSolicitud de InterfazLaboratorios: "+e);
			}
		}
		return exito;
	}
	
	 
}
