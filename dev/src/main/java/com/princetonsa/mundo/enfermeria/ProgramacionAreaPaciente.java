package com.princetonsa.mundo.enfermeria;

import java.sql.Connection;
import java.util.HashMap;
import org.apache.log4j.Logger;
import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.Listado;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.Utilidades;
import com.princetonsa.actionform.enfermeria.ProgramacionAreaPacienteForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.enfermeria.ProgramacionAreaPacienteDao;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;

/**
 * @version 1.0
 * @fecha 06/01/09
 * @author Jhony Alexander Duque A. y Diego Fernando Bedoya Castaño
 *
 */
public class ProgramacionAreaPaciente 
{
	private static Logger logger = Logger.getLogger(ProgramacionAreaPaciente.class);
	
	private static ProgramacionAreaPacienteDao getProgramacionAreaPacienteDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getProgramacionAreaPacienteDao();
	}
	
/**
 * ###############################################################################################################################
 * ########################################## SECCION DE PROGRAMACION POR PACIENTE ###############################################
 * ###############################################################################################################################
 */
	
	//-------indices para el manejo de los datos-------//
	
	//indices para el manejo del listado de ordenes de medicamentos
	public static String [] indicesListadoOrdenesMedicas ={"numeroOrden0_","medicamento1_","presentacion2_","dosis3_","via4_",
														   "frecuencia5_","cuenta6","idPaciente7","checkTot8_","check9_",
														   "numeroSolicitud10_","articulo11_","tieneProg12_","nroDosisPendAdmon13_",
														   "tipoFrecuencia14_","horaP15_","unidaddosis16_"};
	//indices manejados para la programacion
	public static String [] indicesProgramacionAdmin ={"numeroOrden0_","medicamento1_","presentacion2_","dosis3_","via4_",
		   												"frecuencia5_","fechaIni6","horaIni7","observ8_","usuarioProg9_",
		   												"numeroSolicitud10_","articulo11_","institucion12","codigoProg13",
		   												"fechaProg14_","horaProg15_","activo16_","tieneProg17_","nroDosisPendAdmon18_",
		   												"tipoFrecuencia19_"};
	
	//indices utilizados para mostrar la informacion de la programacion
	public static String [] indicesMostrarProgAdmin={"fecha0_","hora1_","usuarioProg2_","codigoProgAdmin3_","codigoDetProgAdmin4_"};
	
	/**
	 * Metodo encargado de Consultar el listado de ordenes medicas
	 * de un paciente.
	 * @param connection
	 * @param criterios
	 * ---------------------------------------
	 * KEY'S DEL MAPA CRITERIOS
	 * ---------------------------------------
	 *  cuenta6 --> Requerido
	 * idPaciente7 --> Requerido
	 * @return HashMap
	 * ---------------------------------------
	 * KEY'S DEL MAPA RESULTADO
	 * ---------------------------------------
	 * numeroOrden0_,medicamento1_,presentacion2_,
	 * dosis3_,via4_,frecuencia5_
	 * 
	 */
	public static HashMap consultarListadoOrdenesMedicas (Connection connection,String cuenta,String idPaciente, String remite)
	{
		HashMap criterios = new HashMap ();
		criterios.put(indicesListadoOrdenesMedicas[6], cuenta);
		criterios.put(indicesListadoOrdenesMedicas[7], idPaciente);
		return getProgramacionAreaPacienteDao().consultarListadoOrdenesMedicas(connection,criterios,remite);
	}
	
	
	/**
	 * Metodo encargado de ordenar un mapa
	 * @param mapaOrdenar
	 * @param forma
	 * @return
	 */
	public static HashMap accionOrdenarMapa(HashMap mapaOrdenar,ProgramacionAreaPacienteForm forma , String [] indices)
	{			
		//logger.info("===> Entré a accionOrdenarMapa");
		int numReg = Integer.parseInt(mapaOrdenar.get("numRegistros")+"");	
		
		//se almacenan los datos que no tiene indice
		
		//checktot
		String chechTotal=mapaOrdenar.get(indicesListadoOrdenesMedicas[8])+"";
		
		mapaOrdenar = (Listado.ordenarMapa(indices,forma.getPatronOrdenar(),forma.getUltimoPatron(),mapaOrdenar,numReg));		
		forma.setUltimoPatron(forma.getPatronOrdenar());
		
		mapaOrdenar.put("numRegistros",numReg+"");
		mapaOrdenar.put(indicesListadoOrdenesMedicas[8], chechTotal);
		
		
		return mapaOrdenar;
	}	
	
	
	/**
	 * Metodo encargado pasar los medicamentos del listado a donde se van a
	 * programar.
	 * @param forma
	 */
	public void  organizarMedicamentosAdmistrar (ProgramacionAreaPacienteForm forma)
	{
		logger.info("\n entre a organizarMedicamentosAdmistrar ordenes -->"+forma.getOrdenesMedicamentos()+"  index-->"+forma.getIndex());
		String index [] = forma.getIndex().split(",");
		int k=0;
		
			//se recorren los medicamentos para saber cuales se van a prograr
			for (int i=0;i<index.length;i++)
			{
				//se evaluan si estan chequeados
				
					Listado.copyMapOnIndexMap(forma.getOrdenesMedicamentos(), forma.getProgramacionAdmin(), indicesListadoOrdenesMedicas, k, Utilidades.convertirAEntero(index[i]));
					forma.setProgramacionAdmin(indicesProgramacionAdmin[17]+k, forma.getOrdenesMedicamentos(indicesListadoOrdenesMedicas[12]+index[i]));
					forma.setProgramacionAdmin(indicesProgramacionAdmin[18]+k, forma.getOrdenesMedicamentos(indicesListadoOrdenesMedicas[13]+index[i]));
					forma.setProgramacionAdmin(indicesProgramacionAdmin[19]+k, forma.getOrdenesMedicamentos(indicesListadoOrdenesMedicas[14]+index[i]));
					k++;
				
					
			}
			//se postula la fecha actual
			forma.setProgramacionAdmin(indicesProgramacionAdmin[6], UtilidadFecha.getFechaActual());
			
			//se postula la hora actual
			forma.setProgramacionAdmin(indicesProgramacionAdmin[7], UtilidadFecha.getHoraActual());
			
			
			logger.info("\n al salir "+forma.getProgramacionAdmin());
	}
	
	/**
	 * Metodo encargado de insertar el encabezado de
	 * la programacion
	 * @param connection
	 * @param datos
	 * -----------------------------------
	 * KEY'S DEL MAPA DATOS
	 * -----------------------------------
	 * numeroSolicitud10_ --> Requerido
	 * articulo11_ --> Requerido
	 * fechaProg6_ --> Requerido
	 * horaProg7_ --> Requerido
	 * usuarioProg9_ --> Requerido
	 * institucion12 --> Requerido
	 * observ8_ --> Requerido
	 * @return
	 */
	public static int insertarEncabezadoProgramacion (Connection connection,HashMap datos)
	{
		return getProgramacionAreaPacienteDao().insertarEncabezadoProgramacion(connection, datos);
	}
	
	
	
	/**
	 * Metodo encargado de actualizar el estado del
	 * detalle de la programacion
	 * @param connection
	 * @param activo
	 * @param codigo
	 * @return
	 */
	public static boolean actualizarEstadoProgramacionAdmin (Connection connection,String activo,int codigo)
	{
		return getProgramacionAreaPacienteDao().actualizarEstadoProgramacionAdmin(connection, activo, codigo);
	}
	
	public static HashMap programarAdministracion (String tipoFrecuencia,int frecuencia,int dosisPendientes,String fechaInicio ,String horaInicio)
	{
		logger.info("\n entre a programarAdministracion    tipoFrecuencia -->"+tipoFrecuencia+"  frecuencia -->"+frecuencia+" dosisPendientes -->"+dosisPendientes+" fechaInicio-->"+fechaInicio+" horaInicio -->"+horaInicio);
		HashMap result =  new HashMap ();
		result.put("numRegistros", 0);
		String fecha=fechaInicio,hora=horaInicio;
		
			if (tipoFrecuencia.equals(ConstantesIntegridadDominio.acronimoDias))
			{
				for(int i=0;i<dosisPendientes;i++)
				{
					//fecha
					result.put(indicesProgramacionAdmin[14]+i, fecha);
					//hora
					result.put(indicesProgramacionAdmin[15]+i, hora);
					//activo
					result.put(indicesProgramacionAdmin[16]+i, ConstantesBD.acronimoSi);
					
					fecha=UtilidadFecha.incrementarDiasAFecha(fecha, (1*frecuencia), false);
				}
			}
			else
				if (tipoFrecuencia.equals(ConstantesIntegridadDominio.acronimoHoras))
				{
					for(int i=0;i<dosisPendientes;i++)
					{
						//fecha
						result.put(indicesProgramacionAdmin[14]+i, fecha);
						//hora
						result.put(indicesProgramacionAdmin[15]+i, hora);
						//activo
						result.put(indicesProgramacionAdmin[16]+i, ConstantesBD.acronimoSi);
						
						String []fechaHora=UtilidadFecha.incrementarMinutosAFechaHora(fecha, hora, (60*frecuencia), false);
						fecha=fechaHora[0];
						hora=fechaHora[1];
					}
				}
				else
					if (tipoFrecuencia.equals(ConstantesIntegridadDominio.acronimoMinutos))
					{
						for(int i=0;i<dosisPendientes;i++)
						{
							//fecha
							result.put(indicesProgramacionAdmin[14]+i, fecha);
							//hora
							result.put(indicesProgramacionAdmin[15]+i, hora);
							//activo
							result.put(indicesProgramacionAdmin[16]+i, ConstantesBD.acronimoSi);
							
							String []fechaHora=UtilidadFecha.incrementarMinutosAFechaHora(fecha, hora, (1*frecuencia), false);
							fecha=fechaHora[0];
							hora=fechaHora[1];
						}
					}
			result.put("numRegistros", dosisPendientes);
			logger.info("\n al salir es ->>"+result);
			return result;
	}
	
	
	
	/**
	 * Metodo encargado de guardar la progrmacion o reprogramacion
	 * @param connection
	 * @param forma
	 * @param usuario
	 */
	public static void guardar (Connection connection, ProgramacionAreaPacienteForm forma,UsuarioBasico usuario,PersonaBasica paciente)
	{
		logger.info("\n entre a guardar ");
		boolean transacction = UtilidadBD.iniciarTransaccion(connection);
		logger.info("----->INICIANDO TRANSACCION ....");
		int numReg=Utilidades.convertirAEntero(forma.getProgramacionAdmin("numRegistros")+"");
		int codigoEncab=ConstantesBD.codigoNuncaValido;
		
		for(int i=0;i<numReg && transacction;i++)
		{
			codigoEncab=Utilidades.convertirAEntero(forma.getProgramacionAdmin(indicesProgramacionAdmin[17]+i)+"");
			//se evalua si es para programar o para reprogramar
			
				//programar
				
				//ingresar emcabezado
				HashMap datos = new HashMap ();
				//numeroSolicitud10_
				datos.put(indicesProgramacionAdmin[10], forma.getProgramacionAdmin(indicesProgramacionAdmin[10]+i));
				//articulo11_
				datos.put(indicesProgramacionAdmin[11], forma.getProgramacionAdmin(indicesProgramacionAdmin[11]+i));
				//fechaIni6_
				datos.put(indicesProgramacionAdmin[6], forma.getProgramacionAdmin(indicesProgramacionAdmin[6]));
				//horaIni7_
				datos.put(indicesProgramacionAdmin[7], forma.getProgramacionAdmin(indicesProgramacionAdmin[7]));
				//usuarioProg9_
				datos.put(indicesProgramacionAdmin[9], usuario.getLoginUsuario());
				//institucion12
				datos.put(indicesProgramacionAdmin[12], usuario.getCodigoInstitucionInt());
				//observ8_
				datos.put(indicesProgramacionAdmin[8], forma.getProgramacionAdmin(indicesProgramacionAdmin[8]));
				//*********************************************************
			
				//esto solo se hace si es reprogramacion
				if (codigoEncab>0)
					transacction=actualizarEstadoProgramacion(connection, codigoEncab, ConstantesBD.acronimoNo);			
				
				// --------- Se insertan los datos del encabezado.---------
				codigoEncab=insertarEncabezadoProgramacion(connection, datos);
				
				//si se ingresa el encabezado se procede a insertar
				//los datos de la programacion
				if (codigoEncab>0)
					transacction=insertarDetalleProgramacion(connection, codigoEncab+"", forma.getProgramacionAdmin(indicesProgramacionAdmin[19]+i)+"", Utilidades.convertirAEntero(forma.getProgramacionAdmin(indicesProgramacionAdmin[5]+i)+""), Utilidades.convertirAEntero(forma.getProgramacionAdmin(indicesProgramacionAdmin[18]+i)+""), forma.getProgramacionAdmin(indicesProgramacionAdmin[6])+"", forma.getProgramacionAdmin(indicesProgramacionAdmin[7])+"");
		
		}
		
		
		if(transacction)
		{
			UtilidadBD.finalizarTransaccion(connection);			
			logger.info("----->TRANSACCION AL 100% ....");

			forma.resetXPaciente();
			//se hace la consulta del listado
			forma.setOrdenesMedicamentos(consultarListadoOrdenesMedicas(connection, paciente.getCodigoCuenta()+"", paciente.getCodigoPersona()+"", ""));
			
		}
		else
		{
			UtilidadBD.abortarTransaccion(connection);
		}
	}
	
	
	/**
	 * Metodo encargado de actualizar el estado de cada 
	 * programacion.
	 * @param connection
	 * @param codigoProg
	 * @param activo
	 * @return
	 */
	public static boolean actualizarEstadoProgramacion (Connection connection,int codigoProg,String activo)
	{
		HashMap detalle = new HashMap ();
		detalle.put("numRegistros", 0);
		boolean transacction = UtilidadBD.iniciarTransaccion(connection);
		logger.info("----->INICIANDO TRANSACCION ....");
		
		detalle=consultarProgramacionAdmin(connection, codigoProg,ConstantesBD.acronimoSi);
		
		int numReg = Utilidades.convertirAEntero(detalle.get("numRegistros")+"");
		
		for (int i=0;i<numReg && transacction;i++)
			transacction=actualizarEstadoProgramacionAdmin(connection, activo,Utilidades.convertirAEntero(detalle.get(indicesMostrarProgAdmin[4]+i)+""));
		
		
		if(transacction)
		{
			UtilidadBD.finalizarTransaccion(connection);			
			logger.info("----->TRANSACCION AL 100% ....");
		}
		else
		{
			UtilidadBD.abortarTransaccion(connection);
		}
		
		return transacction;
	}
	
	
	
		
	/**
	 * Metodo encargado de insertar los datos del detalle
	 * de la programacion
	 * @param connection
	 * @param datos
	 * ---------------------------
	 * KEY'S DEL MAPA DATOS
	 * ---------------------------
	 * codigoProg13 --> Requerido
	 * fechaProg14 --> Requerido
	 * horaProg15 --> Requerido
	 * activo16 --> Requerido
	 * @return
	 */
	public static boolean insertarDetalleProgramacion (Connection connection,String codigoProg,String tipoFrecuencia,int frecuencia,int dosisPendientes,String fechaInicio ,String horaInicio)
	{
		HashMap detalle = new HashMap ();
		detalle.put("numRegistros", 0);
		boolean transacction = UtilidadBD.iniciarTransaccion(connection);
		logger.info("----->INICIANDO TRANSACCION ....");
		
		//se hace la 
		detalle=programarAdministracion(tipoFrecuencia, frecuencia, dosisPendientes, fechaInicio, horaInicio);		
	
		int numRegDet=Utilidades.convertirAEntero(detalle.get("numRegistros")+"");
		
		if (numRegDet<=0)
			transacction=false;
		
		for (int j=0;j<numRegDet && transacction;j++)
		{
			HashMap datos= new HashMap ();
			
			datos.put(indicesProgramacionAdmin[13], codigoProg);
			datos.put(indicesProgramacionAdmin[14], detalle.get(indicesProgramacionAdmin[14]+j));
			datos.put(indicesProgramacionAdmin[15], detalle.get(indicesProgramacionAdmin[15]+j));
			datos.put(indicesProgramacionAdmin[16], detalle.get(indicesProgramacionAdmin[16]+j));
			
			
			if(getProgramacionAreaPacienteDao().insertarDetalleProgramacion(connection, datos)<0)
				transacction=false;
		}
		
		if(transacction)
		{
			UtilidadBD.finalizarTransaccion(connection);			
			logger.info("----->TRANSACCION AL 100% ....");
			
		}
		else
		{
			UtilidadBD.abortarTransaccion(connection);
		}
		
		return transacction;
	}
	
	
	
	/**
	 * Metodo encargado de consultar los datos de la
	 * programacion de administracion de medicamentos.
	 * @param connection
	 * @param codigoAdmin
	 * @return
	 */
	public static  HashMap consultarProgramacionAdmin (Connection connection,int codigoAdmin,String validaFecha)
	{
		return getProgramacionAreaPacienteDao().consultarProgramacionAdmin(connection, codigoAdmin, validaFecha);
	}
	
	
/**
 * ###############################################################################################################################
 * ########################################## FIN SECCION DE PROGRAMACION POR PACIENTE ###############################################
 * ###############################################################################################################################
 */
	

	
	
	
	
	

/**
 * ###############################################################################################################################
 * ########################################## SECCION DE PROGRAMACION POR AREA  ###############################################
 * ###############################################################################################################################
 */
	
	/**
	 * Metodo para consultar las Areas por Vias de Ingreso segun centro atencion
	 */
	public static HashMap<String, Object> listarAreas (Connection con, int centroAtencion)
	{
		return getProgramacionAreaPacienteDao().listaAreas(con, centroAtencion);
	}
	
	/**
	 * Metodo para consultar los Pisos segun centro atencion
	 */
	public static HashMap<String, Object> listarPisos (Connection con, int centroAtencion)
	{
		return getProgramacionAreaPacienteDao().listaPisos(con, centroAtencion);
	}
	
	/**
	 * Metodo para consultar las Habitaciones segun piso
	 */
	public static HashMap<String, Object> listarHabitaciones (Connection con, int piso)
	{
		return getProgramacionAreaPacienteDao().listaHabitaciones(con, piso);
	}
	
	/**
	 * Consulta por Area de los Pacientes
	 * @param con
	 * @param fechaIniProg
	 * @param fechaFinProg
	 * @param area
	 * @param piso
	 * @param habitacion
	 * @param checkProg
	 * @param horaIniProg
	 * @param horaFinProg
	 * @return
	 */
	public static HashMap<String, Object> ConsultaPacientes (Connection con, String fechaIniProg, String fechaFinProg, String area, String piso, String habitacion, int checkProg, String horaIniProg, String horaFinProg, int remite)
	{
		return getProgramacionAreaPacienteDao().ConsultaPacientes(con, fechaIniProg, fechaFinProg, area, piso, habitacion, checkProg, horaIniProg, horaFinProg, remite);
	}

/**
 * ###############################################################################################################################
 * ########################################## SECCION DE PROGRAMACION POR AREA ###############################################
 * ###############################################################################################################################
 */
	
	
	
	
	
}