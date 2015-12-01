package com.princetonsa.mundo.carteraPaciente;

import java.sql.Connection;
import java.util.HashMap;
import org.apache.log4j.Logger;
import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.Utilidades;
import com.princetonsa.action.carteraPaciente.DocumentosGarantiaAction;
import com.princetonsa.dao.carterapaciente.DocumentosGarantiaDao;
import com.princetonsa.dao.DaoFactory;


/**
 * @author Jose Eduardo Arias Doncel
 * jearias@princetonsa.com
 * */
public class DocumentosGarantia
{

	//----Atributo	
	//----Fin Atributo	
	
	//----Metodos
	
	static Logger logger = Logger.getLogger(DocumentosGarantiaAction.class);
	
	/**
	 * Instancia DAO
	 * */
	public static DocumentosGarantiaDao documentosGarantiaDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getDocumentosGarantiaDao();		
	}	
	
	
	/**
	 * consulta lista de Garantia Generados
	 * @param Connection con
	 * @param HashMap parametros 
	 * */
	public static HashMap consultarListaDocumentosGarantia(Connection con,
											HashMap parametros)
	{
		return documentosGarantiaDao().consultarListaDocumentosGarantia(con,parametros);
	}
	
	
	/**
	 * Actuliza Documentos en Garantia
	 * @param Connection con
	 * @param HashMap parametros 
	 * */
	public static boolean actualizarDocumentoGarantia(Connection con, 
													HashMap parametros)
	{
		return documentosGarantiaDao().actualizarDocumentoGarantia(con, parametros);
	}
	
	/**
	 * Inserta un nuevo registro de tipo Documentos en Garantia
	 * en caso de ser exitosa la insercion retorna la llave primaria del documento, de lo contrario retorna -1
	 * @param Connection con
	 * @param HashMap parametros 
	 * */
	public static int insertarDocumentoGarantia(Connection con, 
													HashMap parametros)
	{
		return documentosGarantiaDao().insertarDocumentoGarantia(con, parametros);
	}
	
	
	/**
	 * consulta ingresos de pacientes
	 * @param Connection con
	 * @param HashMap parametros 
	 * */
	public static HashMap consultarIngresos(Connection con,
									 HashMap parametros)
	{
		return documentosGarantiaDao().consultarIngresos(con, parametros);
	}
	

	
	/**
	 * Verifica si es posible modifica la informacion del deudorCo
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public static HashMap infoDocsDependientes(Connection con,
											   HashMap parametros) 
	{		
		return documentosGarantiaDao().infoDocsDependientes(con, parametros);
	}
	
	/**
	 * Consulta la dependencia que existe entre el Deudor y el Codeudor
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public static HashMap consultarDependenciaDeudorCodeudor(Connection con, 
												  		     HashMap parametros)
	{
		return documentosGarantiaDao().consultarDependenciaDeudorCodeudor(con, parametros);
	}
	
	/**
	 * Método que verifica si existe un deudor por ingreso
	 * @param con
	 * @param codigoIngreso
	 * @param codigoInstitucion
	 * @return
	 */
	public static boolean existeDeudorXIngreso(Connection con,int codigoIngreso,int codigoInstitucion)
	{
		boolean existe = false;
		HashMap campos = new HashMap();
		campos.put("ingreso", codigoIngreso);
		campos.put("institucion", codigoInstitucion);
		campos.put("claseDeudorCo", ConstantesIntegridadDominio.acronimoDeudor);
		
		HashMap resultados = documentosGarantiaDao().consultarDependenciaDeudorCodeudor(con, campos);
		
		if(Utilidades.convertirAEntero(resultados.get("numRegistros").toString())>0)
			existe = true;
		
		return existe;
	}
	
	
	/**
	 * Consulta los datos de la persona
	 * @param Connection con
	 * @param HashMap parametros
	 * @param String OpcionConsulta
	 * */
	public static HashMap consultarDatosPaciente(Connection con,
												HashMap parametros)
	{
		return documentosGarantiaDao().consultarDatosPaciente(con, parametros);
	}
	
	/**
	 * Consulta los datos de la persona
	 * @param Connection con
	 * @param HashMap parametros
	 * @param String OpcionConsulta
	 * */
	public static HashMap consultarResponsablePaciente(Connection con, 
														HashMap parametros)
	{
		return documentosGarantiaDao().consultarResponsablePaciente(con, parametros);
	}
	
	/**
	 * inserta un nuevo responsable de paciente
	 * @param Connection con
	 * @param HashMap parametros 
	 * */
	public static boolean insertarResponsablePaciente(Connection con, 
													  HashMap parametros)
	{
		return documentosGarantiaDao().insertarResponsablePaciente(con, parametros);
	}
	
	
	/**
	 * inserta un nuevo responsable de paciente
	 * @param Connection con
	 * @param HashMap parametros 
	 * */
	public static boolean actualizarResponsablePaciente(Connection con, 
													  HashMap parametros)
	{
		return documentosGarantiaDao().actualizarResponsablePaciente(con, parametros);
	}	
	
	
	/**
	 * Verifica las dependencia de documentos de garantia a un ingreso
	 * respuesta[0] = si existen o no documentos de garantia
	 * respuesta[1] = si o no modificable la informacion del DeudorCo solo si NO posee documentos de 
	 * 				  garantia en estado vigente o entregado.
	 * @param parametros
	 * */
	public static String[] darDependenciasDoscGarantias(HashMap parametros)
	{
		String [] respuesta = {"",""};		
		
		if(!parametros.get("numRegistros").equals("0"))
		{	
			respuesta[0] = ConstantesBD.acronimoNo;//existen documentos garantia dependientes del ingreso
			respuesta[1] = ConstantesBD.acronimoSi;//es modificable la informacion del DeudorCo (deudor o codeudor)			
			
			logger.info("dar dependencias >> "+parametros);
			
			for(int i=0; i<Integer.parseInt(parametros.get("numRegistros").toString()); i++)
			{
				//existen documentos si el estado de los documentos es diferente a Anulado
				if(!parametros.get("estado_"+i).equals(ConstantesIntegridadDominio.acronimoEstadoAnulado))
					respuesta[0] = ConstantesBD.acronimoSi;//existen documentos garantia dependientes del ingreso					
				
				//logger.info("\n ***********ENTRO AL IF DE DEPENDENCIAS" );
				//logger.info("\n\nif ( "+(parametros.get("estado_"+i).equals(ConstantesIntegridadDominio.acronimoPolizaVigente))+" || "+(parametros.get("estado_"+i).equals(ConstantesIntegridadDominio.acronimoEstadoEntregado))+")");
				if((parametros.get("estado_"+i).equals(ConstantesIntegridadDominio.acronimoPolizaVigente)) ||
						(parametros.get("estado_"+i).equals(ConstantesIntegridadDominio.acronimoEstadoEntregado)))
				{
					respuesta[1] = ConstantesBD.acronimoNo;
					i = 1000;
				}
			}				
		}
		else
		{
			respuesta[0] = ConstantesBD.acronimoNo; //existen documentos garantia dependientes del ingreso
			respuesta[1] = ConstantesBD.acronimoSi; //es modificable la informacion del DeudorCo (deudor o codeudor)
		}
		
		
		logger.info("valor de la respuesta >> "+respuesta);
		return respuesta;
	}
	
	/**
	 * inserta un registro de tipo deudorco
	 * si es exitosa la insercion retorna el codigoPK de lo contrario retorna -1
	 * @param Connection con
	 * @param HashMap parametros 
	 * */
	public static int insertarDeudorCo(Connection con, HashMap parametros)
	{
		return  documentosGarantiaDao().insertarDeudorCo(con, parametros);
	}	
	
	/**
	 * Consulta Informacion DeudorCo
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public static HashMap consultarDeudorCo(Connection con, HashMap parametros)
	{
		return documentosGarantiaDao().consultarDeudorCo(con, parametros);
	}
	
	/**
	 * Actualiza un registro de tipo deudorco
	 * @param Connection con
	 * @param HashMap parametros 
	 * */
	public static boolean actualizarDeudorCo(Connection con,
									  HashMap parametros) 
	{
		return documentosGarantiaDao().actualizarDeudorCo(con, parametros);		
	}
	
	/**
	 * Actualiza los registros de la verificacion
	 * Connection con
	 * HashMap parametros
	 * */
	public static boolean actualizarverficacion(Connection con,
										 HashMap parametros)
	{
		return documentosGarantiaDao().actualizarverficacion(con, parametros);
	}

	
	/**
	 * Eliminacion de Informacion DeudorCo
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public static boolean eliminarDeudorCo(Connection con, HashMap parametros)
	{	
		return documentosGarantiaDao().eliminarDeudorCo(con, parametros);
	}
	
	
	
	/**
	 * Actualiza el codigo del Responsable en la Tabla Cuentas
	 * Connection con
	 * HashMap parametros
	 * */
	public static boolean actualizarCuenta(Connection con, 
									HashMap parametros)
	{
		return documentosGarantiaDao().actualizarCuenta(con, parametros);
	}
	
	
	/**
	 * @autor Jhony Alexander Duque A. 
	 * Metodo encargado de de consultar los ingresos que tienen documentos de garantia
	 * @param Connection con
	 * @param HashMap parametros 
	 */
	public static HashMap consultarListadoingresosDocumentosGarantia (Connection connection, HashMap parametros)
	{
		return documentosGarantiaDao().consultarListadoingresosDocumentosGarantia(connection, parametros);
	}
	
	/**
	 * Consulta por Rangos los documentos de garantia existentes
	 * @author Jhony Alexander Duque A.
	 * @param Connection con
	 * @param HashMap parametros
	 * en el hashmap parametros deben venir los criterios de la busqueda; estos datos pueden o no venir.
	 * institucion --> Requerido
	 * centroatencion --> Opcional
	 * fechainicial --> Opcional
	 * fechafinal --> Opcional
	 * onsecutivo --> Opcional
	 * tipodocumento --> Opcional 
	 * estadodoc --> Opcional
	 */
	public static HashMap consultaXRangosDocumentosGarantia (Connection connection, HashMap parametros)
	{
		return documentosGarantiaDao().consultaXRangosDocumentosGarantia(connection, parametros);
	}
	
	
	/**
	 * verifica si el registro se ha modificado
	 * @param con
	 * @param HashMap mapaEvaluar 
 	 * @param HashMap parametros
	 * @param int pos
	 * @param int opcion
	 * */
	public static boolean existeModificacion(Connection con,
											 HashMap mapaEvaluar,
											 HashMap parametros, 
											 String pos, 
											 int opcion)
	{
		HashMap temp = new HashMap();		 
		String[] indices = (String[])mapaEvaluar.get("INDICES_MAPA");
		String indicador="0";		
		
		switch (opcion) 
		{			
			//DuedorCo
			case 1: 
				temp = consultarDeudorCo(con, parametros);				
				indicador="";  
				pos="";
			break;		
		}	 		
						
		
		for(int i=0;i<indices.length;i++)
		{				
			/*logger.info("-----------------------------------------------");
			logger.info("cadenas de comparacion >> "+temp.containsKey(indices[i]+indicador)+" >> "+mapaEvaluar.containsKey(indices[i].split("_")[0]+pos));
			logger.info("-----------------------------------------------");*/
			if(temp.containsKey(indices[i]+indicador)&&mapaEvaluar.containsKey(indices[i].split("_")[0]+pos))
			{			
				/*logger.info("-----------------------------------------------");
				logger.info("valor del comparacion >> "+temp.get(indices[i]+indicador)+" >> "+mapaEvaluar.get(indices[i].split("_")[0]+pos).toString());
				logger.info("-----------------------------------------------");*/
				
				if(!((temp.get(indices[i]+indicador)+"").trim().equals((mapaEvaluar.get(indices[i].split("_")[0]+pos)).toString().trim())))
				{					
					return true;
				}				
			}
		}	
		return false;		
	}	
	
	
	
	
	
	/**
	 * Actualiza la informacion del DeudorCo de Documentos de Garantia cuando se ha 
	 * modificado la informacion del paciente o del Responsable
	 * @param Connection con
	 * @param int institucion
	 * @param Integer codigoPersona
	 * @param String tipoDeudorCo (PACI,RPACI,AOTR)
	 * @param String tipoDocumento 
	 * @param String numeroDocumento 
	 * */
	public static boolean actualizarDatosPersonaDocGarantia(Connection con,
			int institucion,
			int ingreso,
			int codigoPersona, 
			String tipoDeudorCo, 
			String tipoIdentificacion,
			String numeroIdentificacion)
	{		
		HashMap campos = new HashMap();		
		campos.put("institucion",institucion);
		campos.put("ingreso",ingreso);
		campos.put("codigoPaciente",codigoPersona);
		campos.put("tipoDeudorCo",tipoDeudorCo);
		campos.put("tipoIdentificacion",tipoIdentificacion);
		campos.put("numeroIdentificacion",numeroIdentificacion);		
		
		return documentosGarantiaDao().actualizarDatosPersonaDocGarantia(con,campos);
	}
	
	
	/**
	 * Método que verifica  si existen documentos de garabtía x ingreso
	 */
	public static boolean existenDocumentosGarantiaXIngreso(Connection con,int idIngreso,int codigoPaciente,int codigoInstitucion)
	{
		HashMap campos = new HashMap();		
		campos.put("codigoInstitucion",codigoInstitucion);
		campos.put("idIngreso",idIngreso);
		campos.put("codigoPaciente",codigoPaciente);
		
		return documentosGarantiaDao().existenDocumentosGarantiaXIngreso(con, campos);
		
	}
	//----Fin Metodos
}	