package com.princetonsa.mundo.historiaClinica;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.log4j.Logger;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesCamposParametrizables;
import util.ConstantesIntegridadDominio;
import util.InfoDatosInt;
import util.InfoDatosString;
import util.ResultadoBoolean;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.historiaClinica.PlantillasDao;
import com.princetonsa.dao.sqlbase.historiaClinica.SqlBasePlantillasDao;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoCampoParametrizable;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoComponente;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoElementoParam;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoEscala;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoOpcionCampoParam;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoPlantilla;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoPlantillaServDiag;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoSeccionFija;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoSeccionParametrizable;
import com.princetonsa.mundo.UsuarioBasico;

/**
 * @author Jose Eduardo Arias Doncel 
 * */
public class Plantillas 
{
	/*************************************************************************************************/
	//*************************************************************************************************
	//Atributos Utilitarios******************************************************************************	
	/*************************************************************************************************/
	
	private static Logger logger = Logger.getLogger(Plantillas.class);
	
	/*************************************************************************************************/
	//*************************************************************************************************
	//Metodos Utilitarios******************************************************************************
	/*************************************************************************************************/		
			
	/**
	 * Instancia el Dao
	 * */
	public static PlantillasDao getPLantillasDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPlantillasDao();
	}
	
	//*************************************************************************************************
	
	/*************************************************************************************************/
	//*************************************************************************************************
	//Metodos Plantillas*******************************************************************************
	/*************************************************************************************************/
	
	/**
	 * Inserta información de la Plantilla
	 * @param Connection con
	 * @param String codigo Funcionalidad Parametrica
	 * @param String codigo institucion
	 * @param String centro Costo (opcional)
	 * @param String sexo (opcional)
	 * @param String especialidad (opcional)
	 * @param String codigoPlantilla (opcional)
	 * @param String nombrePlantilla (opcional)
	 * @param UsuarioBasico usuario
	 * */
	public static int insertarPlantilla(
			Connection con,
			int codigoFuncParam,			
			String institucion,			
			String codigoCentroCosto,
			String codigoSexo,
			String codigoEspecialidad,
			String codigoPlantilla,
			String nombrePlantilla,
			UsuarioBasico usuario,
			String tipoAtencion,
			String tipoFuncionalidad
			)
	{
		HashMap mapa = new HashMap();
		logger.info("EL NOMBRE DE LA PLANTILLA----->"+nombrePlantilla);
		mapa.put("funParam",codigoFuncParam);		
		mapa.put("centroCosto",codigoCentroCosto);
		mapa.put("sexo",codigoSexo);
		mapa.put("especialidad",codigoEspecialidad);
		mapa.put("codigoPlantilla",codigoPlantilla);
		mapa.put("nombrePlantilla",nombrePlantilla);		
		mapa.put("institucion",institucion);	
		mapa.put("usuarioModifica",usuario.getLoginUsuario());
		mapa.put("fechaModifica",UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
		mapa.put("horaModifica",UtilidadFecha.getHoraActual());
		mapa.put("tipoAtencion", tipoAtencion);
		mapa.put("tipoFuncionalidad", tipoFuncionalidad);
		
		return getPLantillasDao().insertarPlantilla(con, mapa);
	}
	
	//*************************************************************************************************
	
	/**
	 * Actualiza la información de la Plantilla
	 * @param Connection con
	 * @param int codigoPlantilla
	 * @param String codigoCentroCosto (opcional)
	 * @param String codigoSexo (opcional)
	 * @param String codigoEspecialidad (opcional)
	 * @param String codigoPlantilla (opcional)
	 * @param String nombrePlantilla (opcional)
	 * @param UsuarioBasico usuario
	 * */
	public static boolean actualizarPlantilla(Connection con,
			int codigoPkPlantilla,			
			String codigoCentroCosto,
			String codigoSexo,
			String codigoEspecialidad,
			String codigoPlantilla,
			String nombrePlantilla,
			UsuarioBasico usuario,
			String mostrarModificacion,
			String tipoAtencion,
			String tipoFuncionalidad)
	{
		HashMap mapa = new HashMap();
				
		mapa.put("codigoPk",codigoPkPlantilla);
		mapa.put("centroCosto",codigoCentroCosto);
		mapa.put("sexo",codigoSexo);
		mapa.put("especialidad",codigoEspecialidad);
		mapa.put("codigoPlantilla",codigoPlantilla);
		mapa.put("nombrePlantilla",nombrePlantilla);			
		mapa.put("usuarioModifica",usuario.getLoginUsuario());
		mapa.put("fechaModifica",UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
		mapa.put("horaModifica",UtilidadFecha.getHoraActual());
		mapa.put("tipoAtencion", tipoAtencion);
		mapa.put("tipoFuncionalidad", tipoFuncionalidad);
		
		if(!mostrarModificacion.equals(""))
			mapa.put("mostrarModificacion",mostrarModificacion);
		else
			mapa.put("mostrarModificacion",ConstantesBD.acronimoSi);
		
		return getPLantillasDao().actualizarPlantilla(con,mapa);
	}
	//*************************************************************************************************
	
	/**
	 * Metodo que  consulta la información de la tabla plantillas
	 * @param Connection con
	 * @param String institucion
	 * @param String codigoFuncionalidadParametrica
	 * @param String consecutivo planilla (opcional)
	 * @param String codigo planilla (opcional)
	 * */
	public static HashMap consultarListadoPlantillas(
			Connection con,
			String institucion,
			int codigoFuncionalidadParametrica,
			String consecutivoPlanilla,
			String codigoPlanilla,			
			boolean indicadorCargarServicios,
			String mostrarModificacion)
	{
		HashMap mapa = new HashMap();
		mapa.put("codigoPk",consecutivoPlanilla);		
		mapa.put("codigoPlantilla",consecutivoPlanilla);
		mapa.put("institucion",institucion);
		mapa.put("funParam",codigoFuncionalidadParametrica);
		mapa.put("mostrarModificacion",mostrarModificacion);
		
		int tarifario= Utilidades.convertirAEntero(ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(Utilidades.convertirAEntero(institucion)));
		if(tarifario<0)
			tarifario=ConstantesBD.codigoTarifarioCups;
		
		mapa.put("tarifario", tarifario);
		
		return getPLantillasDao().consultarListadoPlantillas(con,mapa,indicadorCargarServicios);
	}

	
//*************************************************************************************************
	
	/**
	 * Metodo sobrecargado que  consulta la informaciï¿½n de la tabla plantillas con mï¿½s parï¿½metros que posee ï¿½sta tabla
	 * @param Connection con
	 * @param String institucion
	 * @param String codigoFuncionalidadParametrica
	 * @param String consecutivo planilla (opcional)
	 * @param String codigo planilla (opcional)
	 * */
	public static HashMap consultarListadoPlantillas(
			Connection con,
			String institucion,
			int codigoFuncionalidadParametrica,
			String consecutivoPlanilla,
			String codigoPlanilla,			
			boolean indicadorCargarServicios,
			String mostrarModificacion,
			String tipoAtencion,
			String especialidad)
	{
		HashMap mapa = new HashMap();
		mapa.put("codigoPk",consecutivoPlanilla);		
		mapa.put("codigoPlantilla",codigoPlanilla);
		mapa.put("institucion",institucion);
		mapa.put("funParam",codigoFuncionalidadParametrica);
		mapa.put("mostrarModificacion",mostrarModificacion);
		mapa.put("tipoAtencion",tipoAtencion);
		mapa.put("especialidad",especialidad);
		
		int tarifario= Utilidades.convertirAEntero(ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(Utilidades.convertirAEntero(institucion)));
		if(tarifario<0)
			tarifario=ConstantesBD.codigoTarifarioCups;
		
		mapa.put("tarifario", tarifario);
		
		return getPLantillasDao().consultarListadoPlantillas(con,mapa,indicadorCargarServicios);
	}

	//*************************************************************************************************
	
	/**
	 * Método para cargar plantilla x solicitud si es con valores
	 * @param Connection con
	 * @param int codigoInstitucion
	 * @param int codigoFuncionalidad
	 * @param int codigoCentroCosto
	 * @param int codigoSexo
	 * @param int codigoEspecialidad
	 * @param boolean consultarRegistro
	 * @param int codigoPaciente
	 * @param int codigoIngreso
	 * @param int numeroSolicitud
	 */
	public static DtoPlantilla cargarPlantillaXSolicitud(
			Connection con,
			int codigoInstitucion,
			int codigoFuncionalidad,
			int codigoCentroCosto,
			int codigoSexo,
			int codigoEspecialidad,
			int codigoPlantilla,
			boolean consultarRegistro,
			int codigoPaciente,
			int codigoIngreso,
			int numeroSolicitud,
			int codigoSexoPaciente,
			int diasEdadPaciente,
			boolean filtroDatosPaciente)
	{
		HashMap campos = new HashMap();
		campos.put("codigoInstitucion",codigoInstitucion);
		campos.put("codigoFuncionalidad",codigoFuncionalidad);
		campos.put("codigoCentroCosto",codigoCentroCosto);
		campos.put("codigoSexo",codigoSexo);
		campos.put("codigoEspecialidad",codigoEspecialidad);
		campos.put("codigoPK",codigoPlantilla);
		campos.put("consultarRegistro",consultarRegistro);
		campos.put("numeroSolicitud",numeroSolicitud);
		campos.put("codigoIngreso",codigoIngreso);
		campos.put("codigoPaciente",codigoPaciente);
		campos.put("codigoEvolucion",ConstantesBD.codigoNuncaValido);
		campos.put("codigoRespuestaProcedimiento",ConstantesBD.codigoNuncaValido);
		campos.put("codigoSexoPaciente",codigoSexoPaciente);
		campos.put("diasEdadPaciente",diasEdadPaciente);
		campos.put("filtroDatosPaciente",filtroDatosPaciente);
		campos.put("tipoAtencion", "");
		campos.put("nombrePlantilla", "");
		campos.put("tipoFuncionalidad", "");
		
		//1) Se intenta consultar la plantilla por los parámetros ingresados
		DtoPlantilla plantilla = getPLantillasDao().cargarPlantilla(con, campos);
		
		/**
		 * Si no se encontró plantilla por parámetros ingresados 
		 * Se realizan consultas con la combinación de (Centro Costo y Sexo) y Especialidad
		 */
		if(plantilla.getCodigoPK().equals("")&&!consultarRegistro)
		{
			//Si la busqueda se hizo por especialidad se busca en todas las especialidades----------------------------------
			if(codigoEspecialidad!=ConstantesBD.codigoNuncaValido)
			{
				campos.put("codigoEspecialidad",ConstantesBD.codigoEspecialidadMedicaTodos);
				plantilla = getPLantillasDao().cargarPlantilla(con, campos);
			}
			//De lo contrario quiere decir que la búsqueda se hace por Centro Costo y Sexo-------------------------------------
			else
			{
				//Se consulta por ambos sexos y por el centro de costo enviado
				campos.put("codigoSexo",ConstantesBD.codigoSexoTodos);
				campos.put("codigoCentroCosto",codigoCentroCosto);
				plantilla = getPLantillasDao().cargarPlantilla(con, campos);
				
				//Si todavía no se ha encontrado plantilla se continua validando con otro cas
				if(plantilla.getCodigoPK().equals(""))
				{
					//Se consulta por el sexo ingreso y por todos los centros de costo
					campos.put("codigoSexo",codigoSexo);
					campos.put("codigoCentroCosto",ConstantesBD.codigoCentroCostoTodos);
					plantilla = getPLantillasDao().cargarPlantilla(con, campos);
					
					//Si todavía no se ha encontrado plantilla se continua validando con otro cas
					if(plantilla.getCodigoPK().equals(""))
					{
						//Se consulta todos los sexos y por todos los centros de costo
						campos.put("codigoSexo",ConstantesBD.codigoSexoTodos);
						campos.put("codigoCentroCosto",ConstantesBD.codigoCentroCostoTodos);
						plantilla = getPLantillasDao().cargarPlantilla(con, campos);
					}
				}
				
			}
		}
		
		return plantilla;
	}
	
	
	/**
	 * 
	 * @param con
	 * @param codigoInstitucion
	 * @param codigoCentroCosto
	 * @param codigoSexo
	 * @param codigoPlantilla
	 * @param consultarRegistro
	 * @param codigoPaciente
	 * @param codigoIngreso
	 * @param evolucion
	 * @return
	 */
	public static DtoPlantilla cargarPlantillaXEvolucion(
			Connection con,
			int codigoInstitucion,
			int codigoCentroCosto,
			int codigoSexo,
			int codigoPlantilla,
			boolean consultarRegistro,
			int codigoPaciente,
			int codigoIngreso,
			int evolucion,
			int codigoSexoPaciente,
			int diasEdadPaciente,
			boolean filtroDatosPaciente)
	{
		HashMap campos = new HashMap();
		campos.put("codigoInstitucion",codigoInstitucion);
		campos.put("codigoFuncionalidad",ConstantesCamposParametrizables.funcParametrizableEvolucion);
		campos.put("codigoCentroCosto",codigoCentroCosto);
		campos.put("codigoSexo",codigoSexo);
		campos.put("codigoEspecialidad",ConstantesBD.codigoNuncaValido);
		campos.put("codigoPK",codigoPlantilla);
		campos.put("consultarRegistro",consultarRegistro);
		campos.put("numeroSolicitud",ConstantesBD.codigoNuncaValido);
		campos.put("codigoIngreso",codigoIngreso);
		campos.put("codigoPaciente",codigoPaciente);
		campos.put("codigoEvolucion",evolucion);
		campos.put("codigoRespuestaProcedimiento",ConstantesBD.codigoNuncaValido);
		campos.put("codigoSexoPaciente",codigoSexoPaciente);
		campos.put("diasEdadPaciente",diasEdadPaciente);
		campos.put("filtroDatosPaciente",filtroDatosPaciente);
		campos.put("tipoAtencion", "");
		campos.put("nombrePlantilla", "");
		campos.put("tipoFuncionalidad", "");
		
		logger.info("***************INICIO CONSULTA DE LA PLANTILLA DE LA EVOLUCION**********************************");
		//1) Se intenta consultar la plantilla por los parámetros ingresados
		DtoPlantilla plantilla = getPLantillasDao().cargarPlantilla(con, campos);
		
		/**
		 * Si no se encontró plantilla por parámetros ingresados 
		 * Se realizan consultas con la combinación de (Centro Costo y Sexo) y Especialidad
		 */
		if(plantilla.getCodigoPK().equals("")&&!consultarRegistro)
		{
			
			//De lo contrario quiere decir que la búsqueda se hace por Centro Costo y Sexo-------------------------------------
			
			//Se consulta por ambos sexos y por el centro de costo enviado
			campos.put("codigoSexo",ConstantesBD.codigoSexoTodos);
			campos.put("codigoCentroCosto",codigoCentroCosto);
			plantilla = getPLantillasDao().cargarPlantilla(con, campos);
			
			//Si todavía no se ha encontrado plantilla se continua validando con otro cas
			if(plantilla.getCodigoPK().equals(""))
			{
				//Se consulta por el sexo ingreso y por todos los centros de costo
				campos.put("codigoSexo",codigoSexo);
				campos.put("codigoCentroCosto",ConstantesBD.codigoCentroCostoTodos);
				plantilla = getPLantillasDao().cargarPlantilla(con, campos);
				
				//Si todavía no se ha encontrado plantilla se continua validando con otro cas
				if(plantilla.getCodigoPK().equals(""))
				{
					//Se consulta todos los sexos y por todos los centros de costo
					campos.put("codigoSexo",ConstantesBD.codigoSexoTodos);
					campos.put("codigoCentroCosto",ConstantesBD.codigoCentroCostoTodos);
					plantilla = getPLantillasDao().cargarPlantilla(con, campos);
				}
			}
				
			
		}
		logger.info("***************FIN CONSULTA DE LA PLANTILLA DE LA EVOLUCION**********************************");
		
		return plantilla;
	}
	
	
	//*************************************************************************************************

	
	/**
	 * 
	 */
	public static DtoPlantilla cargarPlantillaParametrica(
			Connection con,
			int codigoPkPlantilla,
			int codigoInstitucion,
			int codigoFuncionalidad,
			int codigoCentroCosto,
			int codigoSexo,
			int codigoEspecialidad,
			int codigoSexoPaciente,
			int diasEdadPaciente,
			boolean filtroDatosPaciente,
			String tipoAtencion,
			String nombre,
			String tipoFuncionalidad)
	{
		HashMap campos = new HashMap();
		campos.put("codigoPK",codigoPkPlantilla);
		campos.put("codigoInstitucion",codigoInstitucion);
		campos.put("codigoFuncionalidad",codigoFuncionalidad);
		campos.put("codigoCentroCosto",codigoCentroCosto);
		campos.put("codigoSexo",codigoSexo);
		campos.put("codigoEspecialidad",codigoEspecialidad);
		campos.put("consultarRegistro",false);
		campos.put("tipoAtencion", tipoAtencion);
		campos.put("nombrePlantilla", nombre);
		campos.put("tipoFuncionalidad", tipoFuncionalidad);
		
		campos.put("numeroSolicitud",ConstantesBD.codigoNuncaValido);
		campos.put("codigoIngreso",ConstantesBD.codigoNuncaValido);
		campos.put("codigoPaciente",ConstantesBD.codigoNuncaValido);
		campos.put("codigoEvolucion",ConstantesBD.codigoNuncaValido);
		campos.put("codigoRespuestaProcedimiento",ConstantesBD.codigoNuncaValido);
		campos.put("codigoSexoPaciente",codigoSexoPaciente);
		campos.put("diasEdadPaciente",diasEdadPaciente);
		campos.put("filtroDatosPaciente",filtroDatosPaciente);
		
		 campos.put("codigoValoracionOdo",ConstantesBD.codigoNuncaValido);		
		 campos.put("codigoEvolucionOdo", ConstantesBD.codigoNuncaValido);
		
		logger.info("valor cargar parametrica >> "+campos);
		
		return getPLantillasDao().cargarPlantilla(con, campos);
	}	
	
	/**
	 * Método para cargar plantilla parametrica 
	 * @param Connection con
	 * @param int codigoPkPlantilla
	 * @param int codigoInstitucion
	 * @param int codigoFuncionalidad
	 * @param int codigoCentroCosto
	 * @param int codigoSexo
	 * @param int codigoEspecialidad 
	 */
	public static DtoPlantilla cargarPlantillaParametrica(
			Connection con,
			int codigoPkPlantilla,
			int codigoInstitucion,
			int codigoFuncionalidad,
			int codigoCentroCosto,
			int codigoSexo,
			int codigoEspecialidad,
			int codigoSexoPaciente,
			int diasEdadPaciente,
			boolean filtroDatosPaciente,
			String tipoAtencion,
			String nombre,
			String tipoFuncionalidad,
			int codigoValoracionOdo,
			int codigoEvolucionOdo)
	{
		HashMap campos = new HashMap();
		campos.put("codigoPK",codigoPkPlantilla);
		campos.put("codigoInstitucion",codigoInstitucion);
		campos.put("codigoFuncionalidad",codigoFuncionalidad);
		campos.put("codigoCentroCosto",codigoCentroCosto);
		campos.put("codigoSexo",codigoSexo);
		campos.put("codigoEspecialidad",codigoEspecialidad);
		campos.put("consultarRegistro",false);
		campos.put("tipoAtencion", tipoAtencion);
		campos.put("nombrePlantilla", nombre);
		campos.put("tipoFuncionalidad", tipoFuncionalidad);
		
		campos.put("numeroSolicitud",ConstantesBD.codigoNuncaValido);
		campos.put("codigoIngreso",ConstantesBD.codigoNuncaValido);
		campos.put("codigoPaciente",ConstantesBD.codigoNuncaValido);
		campos.put("codigoEvolucion",ConstantesBD.codigoNuncaValido);
		campos.put("codigoRespuestaProcedimiento",ConstantesBD.codigoNuncaValido);
		campos.put("codigoSexoPaciente",codigoSexoPaciente);
		campos.put("diasEdadPaciente",diasEdadPaciente);
		campos.put("filtroDatosPaciente",filtroDatosPaciente);
		
		campos.put("codigoValoracionOdo",codigoValoracionOdo);		
		campos.put("codigoEvolucionOdo", codigoEvolucionOdo);
		
		logger.info("valor cargar parametrica >> "+campos);
		
		return getPLantillasDao().cargarPlantilla(con, campos);
	}
	
	
	//*************************************************************************************************
		
	/**
	 * Método para cargar plantilla de Respuesta de Procedimiento 
	 * @param Connection con
	 * @param int codigoPkPlantilla
	 * @param int codigoInstitucion
	 * @param int codigoRespuestaProcedimiento
	 */
	public static DtoPlantilla cargarPlantillaXRespuestaProcedimiento(
			Connection con,
			String codigoPkPlantilla,
			String codigoInstitucion,
			String codigoRespuestaProcedimiento			
			)
	{
		HashMap campos = new HashMap();
		campos.put("codigoPK",codigoPkPlantilla);
		campos.put("codigoInstitucion",codigoInstitucion);		
		campos.put("codigoRespuestaProcedimiento",codigoRespuestaProcedimiento);
					
		campos.put("consultarRegistro",true);
		campos.put("codigoFuncionalidad",ConstantesCamposParametrizables.funcParametrizableRespuestaProcedimientos);
		
		campos.put("codigoEspecialidad",ConstantesBD.codigoNuncaValido);
		campos.put("codigoSexo",ConstantesBD.codigoNuncaValido);
		campos.put("codigoCentroCosto",ConstantesBD.codigoNuncaValido);
		campos.put("numeroSolicitud",ConstantesBD.codigoNuncaValido);		
		campos.put("codigoIngreso",ConstantesBD.codigoNuncaValido);		
		campos.put("codigoPaciente",ConstantesBD.codigoNuncaValido);
		campos.put("codigoEvolucion",ConstantesBD.codigoNuncaValido);		
		campos.put("codigoSexoPaciente",ConstantesBD.codigoNuncaValido);
		campos.put("diasEdadPaciente",ConstantesBD.codigoNuncaValido);
		campos.put("filtroDatosPaciente",false);
		
		campos.put("tipoAtencion", "");
		campos.put("nombrePlantilla", "");
		campos.put("tipoFuncionalidad", "");
		
		return getPLantillasDao().cargarPlantilla(con, campos);
	}	
	
	
	
	
	/**
	 * Metodo para Cargar Plantilla de Ingreso Paciente Odontologia
	 * @param con
	 * @param codigoInstitucion
	 * @param usuario
	 * @param codigoPaciente
	 * @param codigoSexoPaciente
	 * @param edadPaciente
	 * @return
	 */
	public static DtoPlantilla cargarPlantillaIngresoPacienteOdontologico(
			Connection con,			
			int codigoInstitucion,
			UsuarioBasico usuario,
			int codigoPaciente,
			String tipoFuncionalidad,
			int codigoPkPlantilla
			)
	{
		HashMap campos = new HashMap();
		campos.put("codigoInstitucion",codigoInstitucion);
		campos.put("codigoFuncionalidad",ConstantesCamposParametrizables.funcParametrizableInformacionPacienteOdontologico);
		campos.put("codigoPaciente",codigoPaciente);
		campos.put("codigoPK",codigoPkPlantilla);
		campos.put("filtroDatosPaciente",false);
		campos.put("tipoFuncionalidad",tipoFuncionalidad);
		
		if(codigoPkPlantilla > 0)
		{
			logger.info("Existe Plantilla Cod "+ codigoPkPlantilla +" ");
			campos.put("consultarRegistro",true);
		}
		else
		{
			logger.info("NO Existe Plantilla Cod "+ codigoPkPlantilla +" ");  
			campos.put("consultarRegistro",false);
		}

		campos.put("codigoCentroCosto",ConstantesBD.codigoNuncaValido);
	    campos.put("codigoSexo",ConstantesBD.codigoNuncaValido);
	    campos.put("codigoEspecialidad",ConstantesBD.codigoNuncaValido);		
	    campos.put("numeroSolicitud",ConstantesBD.codigoNuncaValido);
		campos.put("codigoIngreso",ConstantesBD.codigoNuncaValido);		
	    campos.put("codigoEvolucion",ConstantesBD.codigoNuncaValido);
	    campos.put("codigoRespuestaProcedimiento",ConstantesBD.codigoNuncaValido);
	    campos.put("codigoSexoPaciente",ConstantesBD.codigoNuncaValido);
	    campos.put("diasEdadPaciente",ConstantesBD.codigoNuncaValido);		
	    campos.put("tipoAtencion","");
	    campos.put("nombrePlantilla","");	
		
		logger.info("Plantillas tipo FUNCIONALIDAD >>"+tipoFuncionalidad);
		
		return getPLantillasDao().cargarPlantillaExistentePaciente(con, campos);
		//return getPLantillasDao().cargarPlantillaExistentePaciente(con, campos);
	}
	
	
	/**
	 * Metodo para Cargar Plantilla de Ingreso Paciente Odontologia
	 * @param con
	 * @param codigoInstitucion
	 * @param usuario
	 * @param codigoPaciente
	 * @param codigoSexoPaciente
	 * @param edadPaciente
	 * @return
	 */
	public static DtoPlantilla cargarPlantillaValoracionOdon(
			Connection con,			
			int codigoInstitucion,
			UsuarioBasico usuario,
			int codigoPaciente,
			String tipoFuncionalidad,
			int codigoPkPlantilla,
			int codigoIngreso,
			double valoracion
			)
	{
		HashMap campos = new HashMap();
		campos.put("codigoInstitucion",codigoInstitucion);
		campos.put("codigoFuncionalidad",ConstantesCamposParametrizables.funcParametrizableInformacionPacienteOdontologico);
		campos.put("codigoPaciente",codigoPaciente);
		campos.put("codigoPK",codigoPkPlantilla);
		campos.put("filtroDatosPaciente",false);
		campos.put("tipoFuncionalidad",tipoFuncionalidad);
		 
		campos.put("consultarRegistro",true);
		campos.put("vieneValoracion",true);
		  
		campos.put("codigoCentroCosto",ConstantesBD.codigoNuncaValido);
	    campos.put("codigoSexo",ConstantesBD.codigoNuncaValido);
	    campos.put("codigoEspecialidad",ConstantesBD.codigoNuncaValido);		
	    campos.put("numeroSolicitud",ConstantesBD.codigoNuncaValido);
		campos.put("codigoIngreso",codigoIngreso);		
	    campos.put("codigoEvolucion",ConstantesBD.codigoNuncaValido);
	    campos.put("codigoRespuestaProcedimiento",ConstantesBD.codigoNuncaValido);
	    campos.put("codigoSexoPaciente",ConstantesBD.codigoNuncaValido);
	    campos.put("diasEdadPaciente",ConstantesBD.codigoNuncaValido);		
	    campos.put("tipoAtencion","");
	    campos.put("nombrePlantilla","");	
		
	    campos.put("codigoValoracionOdo",valoracion);		
	    campos.put("codigoEvolucionOdo",ConstantesBD.codigoNuncaValido);
	    
		logger.info("Plantillas tipo FUNCIONALIDAD >>"+tipoFuncionalidad);
		
		return getPLantillasDao().cargarPlantilla(con, campos);
	}
	
	//*************************************************************************************************	
	
	/**
	 * Consulta la informacion basica de Plantillas Respuesta Procedimientos
	 * @param Connection con
	 * @param String codigoPkRespuestaSolProcedimiento
	 * */
	public static InfoDatosString consultarBasicaPlantillasResProc(Connection con,String codigoPkRespuestaSolProcedimiento)
	{
		HashMap parametros = new HashMap();
		parametros.put("codigoPkResSolProc",codigoPkRespuestaSolProcedimiento);
		return getPLantillasDao().consultarBasicaPlantillasResProc(con, parametros);
	}	
	
	//*************************************************************************************************
	
	/**
	 * Inserta Resultados Procedimiento Requeridos
	 * @param Connection con
	 * @param String codigoPlantilla
	 * @param boolean requerido
	 * */
	public static boolean insertarResultadosProcRequeridos(Connection con,String codigoPlantilla,boolean requerido,UsuarioBasico usuario)
	{
		HashMap mapa  = new HashMap();
		mapa.put("codigoPkPlantilla",codigoPlantilla);
		mapa.put("requerido",requerido?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
		mapa.put("usuarioModifica",usuario.getLoginUsuario());
		mapa.put("fechaModifica",UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
		mapa.put("horaModifica",UtilidadFecha.getHoraActual());
		
		//logger.info("mapa >> "+mapa);
		
		
		return getPLantillasDao().insertarResultadosProcRequeridos(con, mapa);
	}
	
	//*************************************************************************************************	
	
	/**
	 * Actualiza Resultados Procedimiento Requeridos
	 * @param Connection con
	 * @param String codigoPlantilla
	 * @param boolean requerido
	 * @param UsuarioBasico usuario
	 * */
	public static boolean actualizarResultadosProcRequeridos(Connection con,String codigoPlantilla,boolean requerido, UsuarioBasico usuario)
	{
		HashMap mapa  = new HashMap();
		mapa.put("codigoPkPlantilla",codigoPlantilla);
		mapa.put("requerido",requerido?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
		mapa.put("usuarioModifica",usuario.getLoginUsuario());
		mapa.put("fechaModifica",UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
		mapa.put("horaModifica",UtilidadFecha.getHoraActual());
		
		//logger.info("mapa >> "+mapa);
		
		return getPLantillasDao().actualizarResultadosProcRequeridos(con, mapa);
	}
	
	
	//*************************************************************************************************
	
	/**
	 * Consultar Resultados Procedimiento Requeridos
	 * @param Connection con
	 * @param String codigoPkPlantilla
	 * */
	public static String consultarResultadosProcRequeridos(Connection con,String codigoPkPlantilla)
	{
		HashMap mapa = new HashMap();
		mapa.put("codigoPkPlantilla",codigoPkPlantilla);
		
		return getPLantillasDao().consultarResultadosProcRequeridos(con,mapa);
	}
	
	
	//*************************************************************************************************	
	
	/*************************************************************************************************/
	//*************************************************************************************************
	//Metodos Servicios/Diagnosticos Plantilla*********************************************************
	/*************************************************************************************************/
		
	/**
	 * Actualiza la informacion del campo formulario del servicio
	 * @param Connection con
	 * @param int codigoPkPlantilla
	 * @param HashMap parametros 
	 * @param liberarServicios
	 * */
	public static boolean actualizarInsertarFormularioServicio(
			Connection con,
			int codigoPkPlantilla,
			ArrayList<DtoPlantillaServDiag> array, 
			UsuarioBasico usuario)
	{
		for(int i=0; i<array.size(); i++)
		{
			//logger.info("valores para insertar >> "+array.get(i).getEstaBD()+" >> "+array.get(i).getEsEliminado());
			if(array.get(i).getEstaBD().equals(ConstantesBD.acronimoSi))
			{
				//se evalua la eliminacion de registros
				if(array.get(i).getEsEliminado().equals(ConstantesBD.acronimoSi))
				{
					if(!eliminarFormularioRespServ(con,array.get(i).getCodigpPk()))					
						return false;					
				}
				else
				{
					if(!modificarFormularioRespServ(
							con,
							array.get(i).getCodigpPk(),
							codigoPkPlantilla,
							array.get(i).getCodigoServicio(),
							array.get(i).getCodigoDiagnostico(),
							usuario.getLoginUsuario()))
					return false;					
				}
			}			
			else if(array.get(i).getEstaBD().equals(ConstantesBD.acronimoNo) 
					&& array.get(i).getEsEliminado().equals(ConstantesBD.acronimoNo))
			{
				//Se evalua la insercion de nuevos campos
				if(insertarFormularioRespServ(
						con,
						usuario.getCodigoInstitucionInt(),
						codigoPkPlantilla,
						array.get(i).getCodigoServicio(),
						array.get(i).getCodigoDiagnostico()) < 0)
					return false;				
			}				
		}
		
		return true;
	}	
	
	//*************************************************************************************************	
	
	/**
	 * Eliminar parametrizaciones plantilla servicios/diagnosticos
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public static boolean eliminarFormularioRespServ(Connection con,int codigoPk)
	{
		HashMap parametros = new HashMap();
		parametros.put("codigoPk",codigoPk);
		return getPLantillasDao().eliminarFormularioRespServ(con,parametros);				
	}
	
	/*************************************************************************************************/
	//*************************************************************************************************
	//Metodos Plantillas Secciones ********************************************************************	
	/*************************************************************************************************/
	
	/**
	 * Inserta informacion en Plantillas Secciones
	 * @param Connection con
	 * @param String codigoPkPlantillaSecFijas
	 * @param String codigoPkSeccionParam
	 * */
	public static int insertarPlantillasSecciones(Connection con,String codigoPkPlantillaSecFijas,String codigoPkSeccionParam)
	{
		HashMap parametros = new HashMap();
		parametros.put("codigoPkPlanSecFija",codigoPkPlantillaSecFijas);
		parametros.put("codigoPkSeccionParam", codigoPkSeccionParam);
		
		return getPLantillasDao().insertarPlantillasSecciones(con, parametros);
	}
	
	
	/*************************************************************************************************/
	//*************************************************************************************************
	//Metodos Plantillas Secciones Fijas****************************************************************
	/*************************************************************************************************/
		
	/**
	 * Actualiza la información de Plantillas Secciones Fijas
	 * @paran Connection con
	 * @param int codigoPkPlantillaSecFija 
	 * @param int orden
	 * @param String mostrar
	 * @param String codigoPkFunParamSecFij
	 * @param String codigoPkSeccionParam
	 * @param UsuarioBasico usuario 
	 * */
	public static boolean actualizarPlantillaSeccionesFijas(
			Connection con,
			String codigoPkPlantillaSecFija,
			int orden,
			boolean mostrar,
			String codigoPkFunParamSecFij,
			String codigoPkSeccionParam,
			UsuarioBasico usuario)
	{
		HashMap mapa = new HashMap();
		mapa.put("codigoPk",codigoPkPlantillaSecFija);
		mapa.put("orden",orden);
		mapa.put("mostrar",mostrar?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
		
		mapa.put("codigoPkFunParamSecFij",codigoPkFunParamSecFij);
		mapa.put("codigoPkSeccionParam",codigoPkSeccionParam);
		
		mapa.put("usuarioModifica",usuario.getLoginUsuario());
		mapa.put("fechaModifica",UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
		mapa.put("horaModifica",UtilidadFecha.getHoraActual());
		
		return getPLantillasDao().actualizarPlantillasSeccionesFijas(con, mapa);
	}
	
	//*************************************************************************************************
	
	/**
	 * Insertar la información de Plantillas Secciones Fijas
	 * @param Connection con
	 * @param int codigoPkPlantilla
	 * @param int codigoPkFunParamSecFija
	 * @param int orden
	 * @param String mostrar
	 * @param UsuarioBasico usuario
	 * */
	public static int insertarPlantillasSeccionesFijas(
			Connection con,
			String codigoPkPlantilla,
			String codigoPkFunParamSecFija,
			String codigoSeccionParam,
			int orden,
			boolean mostrar,
			UsuarioBasico usuario)
	{
		HashMap mapa = new HashMap();
		
		mapa.put("codigoPkPlantilla",codigoPkPlantilla);
		mapa.put("codigoPkFunParamSecFija",codigoPkFunParamSecFija);
		mapa.put("codigoSeccionParam",codigoSeccionParam);
		mapa.put("orden",orden);
		mapa.put("mostrar",mostrar?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
		
		mapa.put("usuarioModifica",usuario.getLoginUsuario());
		mapa.put("fechaModifica",UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
		mapa.put("horaModifica",UtilidadFecha.getHoraActual());
		
		return getPLantillasDao().insertarPlantillasSeccionesFijas(con,mapa);		
	}

	
	/*************************************************************************************************/
	//*************************************************************************************************
	//Metodos Secciones Parametrizables****************************************************************
	/*************************************************************************************************/
	
	/**
	 * Valida que la información de la seccion Parametrizable no se repita
	 * @param Connection con
	 * @param DtoPlantilla plantillaDto
	 * @param DtoSeccionParametrizables seccionParametrizable
	 * @param String nivel. Nivel 1, Nivel 2, Nivel 3
	 * @param String indexSeccion (no requerido para el nivel 0)
	 * */
	public static boolean validarRepetidoSeccionParametrica(
			Connection con,
			DtoPlantilla plantillaDto,
			DtoSeccionParametrizable seccionParametrizable,
			String nivel,
			String indexSeccion)
	{		
		//Realiza la comparación a nivel de los elementos de la plantilla
		if(nivel.equals("fija"))
		{
			//Valida que la sección no se repita
			for(int j=0; j<plantillaDto.getSeccionesFijas().size();j++)
			{								
				//logger.info("00 "+plantillaDto.getSeccionesFijasPos(j).getCodigoSeccion()+" >> "+plantillaDto.getSeccionesFijasPos(j).getNombreSeccion());
				//Se evalua entre secciones parametrizables en el nivel de las fijas
				if(plantillaDto.getSeccionesFijasPos(j).getCodigoSeccion() == ConstantesBD.codigoNuncaValido) 
				{					
					if(plantillaDto.getSeccionesFijasPos(j).getElementos().size()>0 && 
							plantillaDto.getSeccionesFijasPos(j).getElementosPos(0).isSeccion())
					{
						
						/*logger.info("0comparacion de nivel1 >> "+
								plantillaDto.getSeccionesFijasPos(j).getElementoSeccionPos(0).getCodigo()+" >> "+
								plantillaDto.getSeccionesFijasPos(j).getElementoSeccionPos(0).getDescripcion()+" >> "+
								plantillaDto.getSeccionesFijasPos(j).getElementoSeccionPos(0).getCodigoPK()+" >> "+
								seccionParametrizable.getCodigo()+" >> "+
								seccionParametrizable.getDescripcion()+" >> "+
								seccionParametrizable.getCodigoPK());*/
												
						if(!plantillaDto.getSeccionesFijasPos(j).getElementoSeccionPos(0).getCodigoPK().equals(seccionParametrizable.getCodigoPK()) && 
							(plantillaDto.getSeccionesFijasPos(j).getElementoSeccionPos(0).getCodigo().equals(seccionParametrizable.getCodigo()) || 
									(!seccionParametrizable.getDescripcion().equals("") &&  plantillaDto.getSeccionesFijasPos(j).getElementoSeccionPos(0).getDescripcion().equals(seccionParametrizable.getDescripcion()))))
								return false;
					}
				}
				//Se evalua entre seccion fijas
				else
				{
					//logger.info("1comparacion de nivel 1 >> "+seccionParametrizable.getDescripcion()+" >> "+plantillaDto.getSeccionesFijasPos(j).getNombreSeccion()+" >> ");
					
					if(!seccionParametrizable.getDescripcion().equals("") && 
							seccionParametrizable.getDescripcion().equals(plantillaDto.getSeccionesFijasPos(j).getNombreSeccion()))	
						return false;					
				}
			}
		}
		
		//Realiza la comparación a nivel de los elementos dentro de una seccion
		else if(nivel.equals("2"))
		{
			int numRegistros = 0;
			int numSecciones = 0;
			numRegistros = plantillaDto.getSeccionesFijasPos(Integer.parseInt(indexSeccion)).getElementos().size();
			
			//Se recorre los elementos encontrados dentro de la sección 
			for(int i = 0; i<numRegistros; i++)
			{
				//Solo tomamos los elementos tipo sección
				if(plantillaDto.getSeccionesFijasPos(Integer.parseInt(indexSeccion)).getElementosPos(i).isSeccion())
				{
					numSecciones = plantillaDto.getSeccionesFijasPos(Integer.parseInt(indexSeccion)).getElementosPos(i).getSecciones().size();
					
					for(int j = 0; j <numSecciones; j++)
					{
						logger.info("comparacion de nivel 2 >> "+plantillaDto.getSeccionesFijasPos(Integer.parseInt(indexSeccion)).getElementosPos(i).getSeccionesPos(j).getCodigo()+" >> "+seccionParametrizable.getCodigo());
						
						if(!plantillaDto.getSeccionesFijasPos(Integer.parseInt(indexSeccion)).getElementosPos(i).getSeccionesPos(j).getCodigoPK().equals(seccionParametrizable.getCodigoPK()))
						{						
							if(plantillaDto.getSeccionesFijasPos(Integer.parseInt(indexSeccion)).getElementosPos(i).getSeccionesPos(j).getCodigo().equals(seccionParametrizable.getCodigo()))
								return false;
							
							if(!seccionParametrizable.getDescripcion().equals("") && 
									plantillaDto.getSeccionesFijasPos(Integer.parseInt(indexSeccion)).getElementosPos(i).getSeccionesPos(j).getDescripcion().equals(seccionParametrizable.getDescripcion()))
								return false;
						}
					}					
				}
			}
		}
		
		return true;
	}
	
	//*************************************************************************************************
	
	/**
	 * Consulta la información de la sección Parametrizable
	 * @param Connection con 
	 * @param int institucion 
	 * @param String codigoPkSeccionParam
	 * */
	public static ArrayList<DtoSeccionParametrizable> consultarSeccionesParametrizables(Connection con, int institucion, String codigoPkSeccionParam)
	{
		HashMap parametros = new HashMap();
		parametros.put("institucion",institucion);
		parametros.put("codigoPk",codigoPkSeccionParam);
		
		return getPLantillasDao().consultarSeccionesParametrizables(con, parametros);
	}
	
	//*************************************************************************************************
	
	/**
	 * Inserta información en secciones parametrizables
	 * @param Connection con 
	 * @param String codigoSeccion
	 * @param String descripcionSeccion
	 * @param String orden
	 * @param String columnaSeccion
	 * @param String tipo
	 * @param String seccionPadre
	 * @param String mostrar
	 * @param String mostrarModificacion
	 * @param UsuarioBasico usuario  
	 * */
	public static int insertarSeccionesParametrizablesConCodigo(
			Connection con,
			String codigoSeccion,
			String descripcionSeccion,
			String orden,
			String columnaSeccion,
			String sexoSeccion,
			String edadInicialDias,
			String edadFinalDias,
			String restriccionValCampo,
			String tipo,
			String seccionPadre,
			boolean mostrar,	
			boolean mostrarModificacion,
			UsuarioBasico usuario)
	{
		HashMap parametros = new HashMap();
		parametros.put("codigo",codigoSeccion);
		parametros.put("descripcion",descripcionSeccion);
		parametros.put("orden",orden);
		parametros.put("columnasSeccion",columnaSeccion);
		
		if(sexoSeccion.equals(ConstantesBD.codigoSexoMasculino+"")
				|| sexoSeccion.equals(ConstantesBD.codigoSexoFemenino+""))
			parametros.put("sexoSeccion",sexoSeccion);
		else
			parametros.put("sexoSeccion","");
		
		if(!edadInicialDias.equals("") && !edadFinalDias.equals(""))
		{
			parametros.put("edadInicialDias",edadInicialDias);
			parametros.put("edadFinalDias",edadFinalDias);
		}
		else
		{
			parametros.put("edadInicialDias","");
			parametros.put("edadFinalDias","");
		}
				
		parametros.put("restriccionValCampo",restriccionValCampo);
		parametros.put("tipo",tipo);		
		parametros.put("seccionPadre",seccionPadre);
		parametros.put("institucion",usuario.getCodigoInstitucion());
		parametros.put("mostrar",mostrar?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
		parametros.put("fechaModifica",UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
		parametros.put("horaModifica",UtilidadFecha.getHoraActual());
		parametros.put("usuarioModifica",usuario.getLoginUsuario());
		parametros.put("mostrarModificacion",mostrarModificacion?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
		
		logger.info("parametros >> "+parametros);
		
		return getPLantillasDao().insertarSeccionesParametrizables(con, parametros);	
	}	
	
	
	public static int insertarSeccionesParametrizables(
			Connection con,
			String descripcionSeccion,
			String orden,
			String columnaSeccion,
			String sexoSeccion,
			String edadInicialDias,
			String edadFinalDias,
			String restriccionValCampo,
			String tipo,
			String seccionPadre,
			boolean mostrar,	
			boolean mostrarModificacion,
			UsuarioBasico usuario)
	{
		HashMap parametros = new HashMap();
		parametros.put("descripcion",descripcionSeccion);
		parametros.put("orden",orden);
		parametros.put("columnasSeccion",columnaSeccion);
		
		if(sexoSeccion.equals(ConstantesBD.codigoSexoMasculino+"")
				|| sexoSeccion.equals(ConstantesBD.codigoSexoFemenino+""))
			parametros.put("sexoSeccion",sexoSeccion);
		else
			parametros.put("sexoSeccion","");
		
		if(!edadInicialDias.equals("") && !edadFinalDias.equals(""))
		{
			parametros.put("edadInicialDias",edadInicialDias);
			parametros.put("edadFinalDias",edadFinalDias);
		}
		else
		{
			parametros.put("edadInicialDias","");
			parametros.put("edadFinalDias","");
		}
				
		parametros.put("restriccionValCampo",restriccionValCampo);
		parametros.put("tipo",tipo);		
		parametros.put("seccionPadre",seccionPadre);
		parametros.put("institucion",usuario.getCodigoInstitucion());
		parametros.put("mostrar",mostrar?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
		parametros.put("fechaModifica",UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
		parametros.put("horaModifica",UtilidadFecha.getHoraActual());
		parametros.put("usuarioModifica",usuario.getLoginUsuario());
		parametros.put("mostrarModificacion",mostrarModificacion?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
		
		logger.info("parametros >> "+parametros);
		
		return getPLantillasDao().insertarSeccionesParametrizables(con, parametros);	
	}	
	
	//*************************************************************************************************
	
	/**
	 * Inserta una Nueva Sección Parametrizable
	 * @param Connection con
	 * @param DtoPlantilla dtoPlantilla
	 * @param ArrayList<DtoSeccionParametrizable> seccionParamLis
	 * @param String indexSeccionFija
	 * @param String indexElemento
	 * @param String indexSeccionNivel1
	 * @param String indexSeccionNivel2
	 * @param String nivel, indica a que nivel se creara la sección Parametrizable (1,2,3)
	 * @param UsuarioBasico usuario
	 * @param boolean actualizarOrdenMasTamano. indica si el orden del elemento que inicia desde 1 se debe
	 * actualizar = al orden + el tamaño de los elementos que ya se encontraban parametrizados 
	 * */
	public static ActionErrors insertarSeccionesParametrizable(
			Connection con,
			DtoPlantilla dtoPlantilla,
			ArrayList<DtoSeccionParametrizable> seccionParamLis, 
			String indexSeccionFija,
			String indexElemento,			
			String indexSeccionNivel2,
			String nivel,
			UsuarioBasico usuario,
			boolean actualizarOrdenMasTamano)
	{
		int numRegistros = seccionParamLis.size();
		int conSecParam = 0;
		String indicativoSeccPadre = ConstantesBD.acronimoNo;
		int conSecParam2n = 0;
		int conPlanSecFij = 0;
		int conPlantSecc = 0;		
		int tmp = 0;
		ActionErrors errores = new ActionErrors();
		
		logger.info("-------------------------------------");
		logger.info("Valor del Nivel >> "+nivel);
		logger.info("-------------------------------------");
			
		//Recorre las secciones Parametrizables a Insertar
		for(int i = 0; i <numRegistros; i++)
		{
			//Tomamos las Secciones Parametrizables Nuevas
			if(seccionParamLis.get(i).getCodigoPK().equals(""))
			{
				//Verifica a que nivel se realizara la inserción				
				if(nivel.equals("fija"))				
				{	
					//Actualiza el orden
					if(actualizarOrdenMasTamano)
						tmp = seccionParamLis.get(i).getOrden() + dtoPlantilla.getSeccionesFijas().size();
					else
						tmp = seccionParamLis.get(i).getOrden();
					
					//inserta la sección en Secciones Parametrizables
					conSecParam = insertarSeccionesParametrizables(
							con, 
							seccionParamLis.get(i).getDescripcion(),
							tmp+"",
							seccionParamLis.get(i).getColumnasSeccion()+"",
							seccionParamLis.get(i).getSexoSeccion(),
							seccionParamLis.get(i).getRangoInicialEdad(),
							seccionParamLis.get(i).getRangoFInalEdad(),
							seccionParamLis.get(i).getIndicativoRestriccionValCamp(),
							seccionParamLis.get(i).getTipoSeccion()+"",
							"",
							seccionParamLis.get(i).isVisible(),
							true,
							usuario);					
				
					if(conSecParam == ConstantesBD.codigoNuncaValido)					
					{
						errores.add("descripcion",new ActionMessage("errors.notEspecific","Error al insertar Seccion Parametrizable. Nivel Fija."));
						return errores;
					}
					else
					{		
						//logger.info("valor del consecutivo de seccion param >> "+conSecParam);
						//Inserta la información en Plantillas Secciones Fijas
						conPlanSecFij = insertarPlantillasSeccionesFijas(
								con, 
								dtoPlantilla.getCodigoPK()+"", 
								"", 
								conSecParam+"", 
								tmp, 
								true, 
								usuario);
												
						if(conPlanSecFij == ConstantesBD.codigoNuncaValido)					
						{
							errores.add("descripcion",new ActionMessage("errors.notEspecific","Error al insertar Plantillas Secciones Fijas. Nivel Fija."));
							return errores;
						}
						else
						{
							//logger.info("valor del consecutivo de plantillas seciones fijas  >> "+conPlanSecFij);
							//Inserta la informacion en Plantillas Secciones
							conPlantSecc = insertarPlantillasSecciones(con,conPlanSecFij+"", conSecParam+"");
							
							if(conPlantSecc == ConstantesBD.codigoNuncaValido)					
							{
								errores.add("descripcion",new ActionMessage("errors.notEspecific","Error al insertar Plantillas Secciones. Nivel Fija."));
								return errores;
							}
						}
					}
				}
				//Verifica a que nivel se realizara la inserción				
				if(nivel.equals("1"))				
				{	
					//Actualiza el orden
					if(actualizarOrdenMasTamano)
						tmp = seccionParamLis.get(i).getOrden() + dtoPlantilla.getSeccionesFijasPos(Integer.parseInt(indexSeccionFija)).getElementos().size();
					else
						tmp = seccionParamLis.get(i).getOrden();
					
					//inserta la sección en Secciones Parametrizables
					conSecParam = insertarSeccionesParametrizables(
							con, 
							seccionParamLis.get(i).getDescripcion(),
							tmp+"",
							seccionParamLis.get(i).getColumnasSeccion()+"",
							seccionParamLis.get(i).getSexoSeccion(),
							seccionParamLis.get(i).getRangoInicialEdad(),
							seccionParamLis.get(i).getRangoFInalEdad(),
							seccionParamLis.get(i).getIndicativoRestriccionValCamp(),
							seccionParamLis.get(i).getTipoSeccion()+"",
							"",
							seccionParamLis.get(i).isVisible(),
							true,
							usuario);					
				
					if(conSecParam == ConstantesBD.codigoNuncaValido)					
					{
						errores.add("descripcion",new ActionMessage("errors.notEspecific","Error al insertar Seccion Parametrizable. Nivel 1."));
						return errores;
					}
					else
					{
						
						if(dtoPlantilla.getSeccionesFijasPos(Integer.parseInt(indexSeccionFija)).getCodigoPK().equals(""))
						{	
						
							conPlanSecFij = insertarPlantillasSeccionesFijas(
									con, 
									dtoPlantilla.getCodigoPK()+"", 
									dtoPlantilla.getSeccionesFijasPos(Utilidades.convertirAEntero(indexSeccionFija)).getCodigoPkFunParamSecFij(), 
									"", 
									tmp, 
									true, 
									usuario);
						}	
						else
							conPlanSecFij=Utilidades.convertirAEntero(dtoPlantilla.getSeccionesFijasPos(Integer.parseInt(indexSeccionFija)).getCodigoPK());
						
						
						logger.info("indexFija>>>>>>>>>>>"+indexSeccionFija);
						logger.info("Sec>>>>>>>>>>>>>>"+dtoPlantilla.getSeccionesFijasPos(Integer.parseInt(indexSeccionFija)).getCodigoPK());
						
						//Inserta la informacion en Plantillas Secciones
						conPlantSecc = insertarPlantillasSecciones(
								con,
								conPlanSecFij+"", 
								conSecParam+"");
						
						if(conPlantSecc == ConstantesBD.codigoNuncaValido)					
						{
							errores.add("descripcion",new ActionMessage("errors.notEspecific","Error al insertar Plantillas Secciones. Nivel 1."));
							return errores;
						}
						
					}
				}
				else if(nivel.equals("2"))				
				{					
					//Se pregunta por el tipo de Sección 
					String tipoSeccion = dtoPlantilla.getSeccionesFijasPos(Integer.parseInt(indexSeccionFija)).getElementoSeccionPos(Integer.parseInt(indexElemento)).getTipoSeccion();

					//Sección de Tipo Sección
					if(tipoSeccion.equals(ConstantesIntegridadDominio.acronimoSeccion))
					{
						//Actualiza el orden
						if(actualizarOrdenMasTamano)
							tmp = seccionParamLis.get(i).getOrden() + dtoPlantilla.getSeccionesFijasPos(Integer.parseInt(indexSeccionFija)).getElementoSeccionPos(Integer.parseInt(indexElemento)).getSecciones().size();
						else
							tmp = seccionParamLis.get(i).getOrden();
						
						//Captura el CodigoPk de la Seccion Parametrizable de Primer Nivel     
						conSecParam = Integer.parseInt(dtoPlantilla.getSeccionesFijasPos(Integer.parseInt(indexSeccionFija)).getElementoSeccionPos(Integer.parseInt(indexElemento)).getCodigoPK());
						//Captura ell indicativo de la seccion Parametrizable de Primer Nivel
						indicativoSeccPadre = dtoPlantilla.getSeccionesFijasPos(Integer.parseInt(indexSeccionFija)).getElementoSeccionPos(Integer.parseInt(indexElemento)).getIndicativoRestriccionValCamp();
						//Captura el CodigoPk de la Plantilla Seccion Fija
						conPlanSecFij = Integer.parseInt(dtoPlantilla.getSeccionesFijasPos(Integer.parseInt(indexSeccionFija)).getCodigoPK());
						
						//Inserta la Nueva Sección Parametrizable de Segundo Nivel
						conSecParam2n = insertarSeccionesParametrizables(
								con, 
								seccionParamLis.get(i).getDescripcion(),
								seccionParamLis.get(i).getOrden()+"",
								seccionParamLis.get(i).getColumnasSeccion()+"",
								seccionParamLis.get(i).getSexoSeccion(),
								seccionParamLis.get(i).getRangoInicialEdad(),
								seccionParamLis.get(i).getRangoFInalEdad(),
								indicativoSeccPadre,
								seccionParamLis.get(i).getTipoSeccion()+"",
								conSecParam+"",
								seccionParamLis.get(i).isVisible(),
								true,								
								usuario);
						
						if(conSecParam2n == ConstantesBD.codigoNuncaValido)					
						{
							errores.add("descripcion",new ActionMessage("errors.notEspecific","El Consecutivo de la Sección Parametrizable de Segundo Nivel es Incorrecto."));
							return errores;
						}
						else
						{
							//Insertar plantillas Secciones
							conPlantSecc = insertarPlantillasSecciones(con,conPlanSecFij+"",conSecParam2n+"");
							
							if(conPlantSecc == ConstantesBD.codigoNuncaValido)					
							{
								errores.add("descripcion",new ActionMessage("errors.notEspecific","El Consecutivo de Plantillas Secciones de Segundo Nivel es Incorrecto."));
								return errores;
							}
							
						}					
					}										
				}				
			}		
		}		
		
		return errores;
	}	
	
	
	//*************************************************************************************************
	
	/**
	 * Modifica una Nueva Sección Parametrizable
	 * @param Connection con
	 * @param DtoPlantilla dtoPlantilla
	 * @param ArrayList<DtoSeccionParametrizable> seccionParamLis
	 * @param String indexSeccionFija
	 * @param String indexElemento
	 * @param String indexSeccionNivel1
	 * @param String indexSeccionNivel2
	 * @param String nivel, indica a que nivel se creara la sección Parametrizable (1,2,3)
	 * @param UsuarioBasico usuario
	 * @param boolean actualizarOrdenMasTamano. indica si el orden del elemento que inicia desde 1 se debe
	 * actualizar = al orden + el tamaño de los elementos que ya se encontraban parametrizados 
	 * */
	public static ActionErrors modificaSeccionesParametrizable(
			Connection con,
			DtoPlantilla dtoPlantilla,
			ArrayList<DtoSeccionParametrizable> seccionParamLis, 
			String indexSeccionFija,
			String indexElemento,			
			String indexSeccionNivel2,
			String nivel,
			UsuarioBasico usuario,
			boolean actualizarOrdenMasTamano)
	{
		int numRegistros = seccionParamLis.size();
		int conSecParam = 0;
		int conPlanSecFij = 0;
		int conPlantSecc = 0;
		int conPlantSecc2nivel = 0;
		int codigoPkOpcion = 0;
		int codigoPkPlanCamSec = 0;
		int tmp = 0;
		ArrayList<InfoDatosInt> cambioCodigoPkArray = new ArrayList<InfoDatosInt>();
		ActionErrors errores = new ActionErrors();
					
		logger.info("-------------------------------------");
		logger.info("Valor del Nivel >> "+nivel);
		logger.info("-------------------------------------");
		
		//Recorre las secciones Parametrizables a Modificar
		for(int i = 0; i <numRegistros; i++)
		{
			//Tomamos las Secciones Parametrizables Existentes
			if(!seccionParamLis.get(i).getCodigoPK().equals(""))
			{
				//******************************************************************************************************************************//
				//******************************************************************************************************************************//
				//******************************************************************************************************************************//

				//Verifica a que nivel se realizara la inserción				
				if(nivel.equals("fija"))
				{
					//Verifica que campos fueron modificados, si se modifico el nombre o el tipo de sección se debe crear
					//una copia de la estructura actual de la sección, la nueva sección se debe crear con el indicador de 
					//mostrar_modificacion en S y la antigua actualizar el mismo indicador en N
					if(!seccionParamLis.get(i).getDescripcion().equals(dtoPlantilla.getSeccionesFijasPos(Integer.parseInt(indexSeccionFija)).getNombreSeccion())
							|| !seccionParamLis.get(i).getTipoSeccion().equals(dtoPlantilla.getSeccionesFijasPos(Integer.parseInt(indexSeccionFija)).getElementoSeccionPos(0).getTipoSeccion())
								|| !seccionParamLis.get(i).getIndicativoRestriccionValCamp().equals(dtoPlantilla.getSeccionesFijasPos(Integer.parseInt(indexSeccionFija)).getElementoSeccionPos(0).getIndicativoRestriccionValCamp()))
					{
						logger.info("-----------------------------------------------------");
						logger.info("OPERACION 1. Replicar Seccion Parametrizable. Nivel Fija");
						logger.info("-----------------------------------------------------");
						
						//Tomo el DtoSeccionParametrizable a modificar
						DtoSeccionParametrizable dtoSeccionParamAnterior = dtoPlantilla.getSeccionesFijasPos(Integer.parseInt(indexSeccionFija)).getElementoSeccionPos(0);
						
						//Inserto el nuevo dtoSeccionParametrizable con los cambios en los campos
						conSecParam =  insertarSeccionesParametrizables(
								con,
								seccionParamLis.get(i).getDescripcion(),
								seccionParamLis.get(i).getOrden()+"", 
								seccionParamLis.get(i).getColumnasSeccion()+"",
								seccionParamLis.get(i).getSexoSeccion(),
								seccionParamLis.get(i).getRangoInicialEdad(),
								seccionParamLis.get(i).getRangoFInalEdad(),
								seccionParamLis.get(i).getIndicativoRestriccionValCamp(),
								seccionParamLis.get(i).getTipoSeccion()+"", 
								"", 
								seccionParamLis.get(i).isVisible(),
								true,							 
								usuario);
						
						if(conSecParam == ConstantesBD.codigoNuncaValido)
						{
							errores.add("descripcion",new ActionMessage("errors.notEspecific","Error Replicacion Seccion Parametrizable Nivel Fija"));
							return errores;
						}
						else
						{
							logger.info("-----------------------------------------------------------");
							logger.info("OPERACION 2. Insertar Nueva Seccion Parametrizable de Nivel Fija. conSecParam >> "+conSecParam);
							logger.info("-----------------------------------------------------------");
							
							//Actualiza el indicador de mostrar modificación para la sección parametrizable Anterior 
							if(actualizarMostrarModOrdenSeccionParam(
									con,
									false,
									"",
									dtoSeccionParamAnterior.getCodigoPK(),
									usuario))
							{
								logger.info("------------------------------------------------------------------------------");
								logger.info("OPERACION 3. Actualizar Anterior Seccion Parametrizable de Nivel Fija (Mostrar Mod) ");
								logger.info("------------------------------------------------------------------------------");
								
								//Inserta en Plantillas Seccion Fijas
								conPlanSecFij = insertarPlantillasSeccionesFijas(
										con, 
										dtoPlantilla.getCodigoPK()+"", 
										"",
										conSecParam+"", 
										dtoPlantilla.getSeccionesFijasPos(Integer.parseInt(indexSeccionFija)).getOrden(), 
										dtoPlantilla.getSeccionesFijasPos(Integer.parseInt(indexSeccionFija)).isVisible(), 
										usuario);
								
								if(conPlanSecFij == ConstantesBD.codigoNuncaValido)
								{
									errores.add("descripcion",new ActionMessage("errors.notEspecific","Error Replicación Plantilla Seccion Fijas Nivel Fija."));
									return errores;
								}
								else
								{
									logger.info("------------------------------------------------------");
									logger.info("OPERACION 4. Insertar Plantilla Seccion Fija la Nueva Seccion de Nivel Fija. conPlanSecFij >> "+conPlanSecFij);
									logger.info("------------------------------------------------------");
									
									//Se Inserta Plantillas Secciones
									conPlantSecc = insertarPlantillasSecciones(con,conPlanSecFij+"",conSecParam+"");
									
									if(conPlantSecc == ConstantesBD.codigoNuncaValido)
									{
										errores.add("descripcion",new ActionMessage("errors.notEspecific","Error Replicación Plantilla Secciones Nivel Fija"));
										return errores;
									}
									else
									{										
										//Desasocia la seccion de los campos tipo select que la tengan asociadas														
										logger.info("Desasociar Seccion de Opciones >> codigo Pk Plantillas Secciones >> "+dtoPlantilla.getSeccionesFijasPos(Integer.parseInt(indexSeccionFija)).getElementoSeccionPos(0).getConsecutivoParametrizacion());
										
										if(desasociarSeccionOculta(
																con,
																dtoPlantilla.getSeccionesFijasPos(Integer.parseInt(indexSeccionFija)).getElementoSeccionPos(0).getConsecutivoParametrizacion(),
																false,
																usuario))		
										{
											logger.info("CODIGO PLANTILLA SECCION NUEVA >>>> "+conPlantSecc);
											logger.info("CODIGO PLANTILLA VIEJA >>>>>>> "+dtoPlantilla.getSeccionesFijasPos(Integer.parseInt(indexSeccionFija)).getElementoSeccionPos(0).getConsecutivoParametrizacion());
											
											//Inserta las secciones asociadas creadas solo en el caso en que el indicativo de restriccion de valores este en S y esto en la nueva seccion											
											if(seccionParamLis.get(i).getIndicativoRestriccionValCamp().equals(ConstantesBD.acronimoSi))
											{
												if(!insertarSeccionesAsociadasOpcionesNuevas(
														con, 
														conPlantSecc+"", 
														dtoPlantilla.getSeccionesFijasPos(Integer.parseInt(indexSeccionFija)).getElementoSeccionPos(0).getConsecutivoParametrizacion(), 
														true, 
														usuario))
												{
													logger.info("NO GUARDO LAS NUEVAS SECCIONES ASOCIADAS");
													/*errores.add("descripcion",new ActionMessage("errors.notEspecific","Problemas Almacenando la Información de Secciones Asociadas a Opciones"));
													return errores;*/
												}
											}																					
										}
																				
										//en el caso en que la seccion se encontrara con Indicativo de Restriccion en N y se pasara a S, se debe borrar todas las secciones que tuviera asociadas
										if(seccionParamLis.get(i).getIndicativoRestriccionValCamp().equals(ConstantesBD.acronimoSi) && 
												dtoPlantilla.getSeccionesFijasPos(Integer.parseInt(indexSeccionFija)).getElementoSeccionPos(0).getIndicativoRestriccionValCamp().equals(ConstantesBD.acronimoNo))
										{
											if(!desasociarSeccionesOcultasDeSeccionN(
													con,
													dtoPlantilla.getSeccionesFijasPos(Integer.parseInt(indexSeccionFija)).getElementoSeccionPos(0).getConsecutivoParametrizacion(), 
													false,
													usuario))
											{
												logger.info("NO DESASOCIO LAS SECCIONES OCULTAS DE LA SECCION >> "+dtoPlantilla.getSeccionesFijasPos(Integer.parseInt(indexSeccionFija)).getElementoSeccionPos(0).getConsecutivoParametrizacion());
											}													
										}
										
										
										logger.info("---------------------------------------------------");
										logger.info("OPERACION 5. Insertar Plantilla Secciones la nueva Seccion de Nivel Fija. conPlantSecc >> "+conPlantSecc);
										logger.info("---------------------------------------------------");
										
										//Si se cambio el tipo de Sección, no se genera replicación de los elementos de la sección, 
										//estos no son tomados en cuenta y para el usuario parece que hubieran sido eliminados
										if(seccionParamLis.get(i).getTipoSeccion().equals(dtoPlantilla.getSeccionesFijasPos(Integer.parseInt(indexSeccionFija)).getElementoSeccionPos(0).getTipoSeccion()))
										{							
											logger.info("---------------------------------------------------");
											logger.info("OPERACION 6. Insertar Seccion de Segundo Nivel contenida en la Sección de Nivel Fija");
											logger.info("---------------------------------------------------");
											
											int conSecParam2 = 0 ;
											DtoSeccionParametrizable seccion2nivel;
											
											//Recorre los Elementos que Componen la Sección (Secciones)
											for(int j=0; j<dtoSeccionParamAnterior.getSecciones().size(); j++)
											{
												seccion2nivel = dtoSeccionParamAnterior.getSecciones().get(j);						
												
												//Inserta la Nueva Sección Parametrizable de Segundo Nivel
												conSecParam2 = insertarSeccionesParametrizables(
														con, 
														seccion2nivel.getDescripcion(),
														seccion2nivel.getOrden()+"",
														seccion2nivel.getColumnasSeccion()+"",
														seccion2nivel.getSexoSeccion(),
														seccion2nivel.getRangoInicialEdad(),
														seccion2nivel.getRangoFInalEdad(),
														seccionParamLis.get(i).getIndicativoRestriccionValCamp(),
														seccion2nivel.getTipoSeccion()+"",
														conSecParam+"",
														seccion2nivel.isVisible(),
														true,								
														usuario);
												
												
												if(conSecParam2 == ConstantesBD.codigoNuncaValido)					
												{
													errores.add("descripcion",new ActionMessage("errors.notEspecific","El Consecutivo de la Sección Parametrizable de Segundo Nivel es Incorrecto."));
													return errores;
												}
												else
												{
													//Insertar plantillas Secciones
													conPlantSecc2nivel = insertarPlantillasSecciones(con,conPlanSecFij+"",conSecParam2+"");
													
													if(conPlantSecc2nivel == ConstantesBD.codigoNuncaValido)					
													{
														errores.add("descripcion",new ActionMessage("errors.notEspecific","El Consecutivo de Plantillas Secciones de Segundo Nivel es Incorrecto."));
														return errores;
													}
													else
													{														
														
														logger.info("---------------------------------------------------");
														logger.info("OPERACION 7. Insertar Secciones contenidas dentro de la Sección de Segundo Nivel contenida dentro de la Seccion de Nivel Fija. conPlantSecc >> "+conPlantSecc );
														logger.info("---------------------------------------------------");
														
														DtoCampoParametrizable campo;
														int codigoPkCampoParam;
														cambioCodigoPkArray = new ArrayList<InfoDatosInt>();
														
														//Recorre los Campos de la sección
														for(int k=0; k<seccion2nivel.getCampos().size(); k++)
														{
															campo = seccion2nivel.getCampos().get(k);														
															
															//Toma los campos diferentes de tipo campo
															if(campo.getCodigoTipo() != ConstantesCamposParametrizables.tipoCampoFormula)
															{
																//Inserta la información del Campo Parametrizable
																codigoPkCampoParam = Plantillas.insertarCamposParametrizablesSeccion(
																		con,
																		campo.getNombre(),
																		campo.getEtiqueta(), 
																		campo.getCodigoTipo()+"",
																		campo.getTamanio()+"",
																		campo.getSigno(),
																		campo.getCodigoUnidad()+"",
																		campo.getValorPredeterminado()+"", 
																		campo.getMaximo()+"", 
																		campo.getMinimo()+"",
																		campo.getDecimales()+"",
																		campo.getColumnasOcupadas()+"",						 
																		campo.getOrden()+"", 
																		campo.isUnicoXFila(),
																		campo.isRequerido(),
																		campo.getFormulaCompleta(),
																		Plantillas.cargarTipoHtml(campo.getCodigoTipo(),campo.getTipoHtml()),						
																		true,
																		true,
																		usuario,
																		campo.getManejaImagen(),
																		campo.getManejaImagen().equals(ConstantesBD.acronimoSi)?Utilidades.convertirAEntero(campo.getImagenBase().getCodigo()):ConstantesBD.codigoNuncaValido);	
																
																if(codigoPkCampoParam == ConstantesBD.codigoNuncaValido)
																{
																	errores.add("descripcion",new ActionMessage("errors.notEspecific","El Consecutivo del Campo Parametrizable no es Valido."));
																	return errores;
																}
																else
																{
																	//Almacena la información del cambio de codigos
																	cambioCodigoPkArray.add(new InfoDatosInt(codigoPkCampoParam,campo.getCodigoPK()));
																	
																	//Valida el Ingreso de las Opciones
																	if(campo.getCodigoTipo() == ConstantesCamposParametrizables.tipoCampoSeleccion || 
																			campo.getCodigoTipo() == ConstantesCamposParametrizables.tipoCampoChequeo)
																	{
																		DtoOpcionCampoParam opcion;
																	
																		for(int l=0; l<campo.getOpciones().size(); l++)
																		{
																			opcion = campo.getOpciones().get(l);
																			
																			codigoPkOpcion = Plantillas.insertarOpcionesCamposParam(
																					con,
																					codigoPkCampoParam+"", 
																					opcion.getOpcion(), 
																					opcion.getValor(), 
																					usuario,
																					campo.getManejaImagen().equals(ConstantesBD.acronimoSi)?Utilidades.convertirAEntero(opcion.getConvencionOdon().getCod()):ConstantesBD.codigoNuncaValido);
																										
																			if(codigoPkOpcion == ConstantesBD.codigoNuncaValido)
																			{
																				errores.add("descripcion",new ActionMessage("errors.notEspecific","El Consecutivo para la Opcion del Campo Parametrizable no es Valido."));
																				return errores;
																			}																			
																			else if(seccionParamLis.get(i).getIndicativoRestriccionValCamp().equals(ConstantesBD.acronimoNo))
																			{
																				
																				//Inserta las secciones Asociados
																				for(int m = 0; m < opcion.getSecciones().size(); m++)
																				{
																					if(opcion.getSecciones().get(m).getCodigoPkDetSeccion().equals(""))
																					{
																						if(!insertarSeccionesAsociadasOpciones(
																								con, 
																								codigoPkOpcion+"", 
																								opcion.getSecciones().get(m).getConsecutivoParametrizacion()+"", 
																								true, 
																								usuario))
																						{
																							errores.add("descripcion",new ActionMessage("errors.notEspecific","Problemas Almacenando la Información de Secciones Asociadas a Opciones"));
																							return errores;
																						}
																					}
																				}
																				
																				//Inserta los Valores Asociados																			
																				for(int m = 0; m < opcion.getValoresOpcion().size(); m++)
																				{
																					if(opcion.getValoresOpcion().get(m).getCodigoPk().equals(""))
																					{
																						if(!insertarValoresAsociadosOpciones(
																								con,
																								codigoPkOpcion+"", 
																								opcion.getValoresOpcion().get(m).getValor(),
																								true,
																								usuario))
																						{
																							errores.add("descripcion",new ActionMessage("errors.notEspecific","Problemas Almacenando la Información de Valores Asociadas a Opciones"));
																							return errores;
																						}
																					}
																				}
																				
																			}
																		}
																	}
																	
																	codigoPkPlanCamSec = Plantillas.insertarPlantillasCamposSec(con,conPlantSecc2nivel+"",codigoPkCampoParam+"");																
																	if(codigoPkPlanCamSec == ConstantesBD.codigoNuncaValido)
																	{
																		errores.add("descripcion",new ActionMessage("errors.notEspecific","El Consecutivo para Plantilla Campos Sección no es Valido."));
																		return errores;
																	}											
																}
															}															
														}
														
														//evalua los campos tipos formula, la formula posee campos asociados si se generan de nuevo, los codigo pk cambian y 
														//estos tambien deben ser cambiados en la formula
														
														//Recorre los Campos de la sección
														for(int k=0; k<seccion2nivel.getCampos().size(); k++)
														{
															campo = seccion2nivel.getCampos().get(k);														
															
															//Toma los campos iguales de tipo campo
															if(campo.getCodigoTipo() == ConstantesCamposParametrizables.tipoCampoFormula)
															{
																//Inserta la información del Campo Parametrizable
																codigoPkCampoParam = Plantillas.insertarCamposParametrizablesSeccion(
																		con,
																		campo.getNombre(),
																		campo.getEtiqueta(), 
																		campo.getCodigoTipo()+"",
																		campo.getTamanio()+"",
																		campo.getSigno(),
																		campo.getCodigoUnidad()+"",
																		campo.getValorPredeterminado()+"", 
																		campo.getMaximo()+"", 
																		campo.getMinimo()+"",
																		campo.getDecimales()+"",
																		campo.getColumnasOcupadas()+"",						 
																		campo.getOrden()+"", 
																		campo.isUnicoXFila(),
																		campo.isRequerido(),
																		actualizarCodigosEnFormula(campo.getFormulaCompleta(),cambioCodigoPkArray),
																		Plantillas.cargarTipoHtml(campo.getCodigoTipo(),campo.getTipoHtml()),						
																		true,
																		true,
																		usuario,
																		campo.getManejaImagen(),
																		campo.getManejaImagen().equals(ConstantesBD.acronimoSi)?Utilidades.convertirAEntero(campo.getImagenBase().getCodigo()):ConstantesBD.codigoNuncaValido);	
																
																if(codigoPkCampoParam == ConstantesBD.codigoNuncaValido)
																{
																	errores.add("descripcion",new ActionMessage("errors.notEspecific","El Consecutivo del Campo Parametrizable no es Valido."));
																	return errores;
																}
																else
																{
																	codigoPkPlanCamSec = Plantillas.insertarPlantillasCamposSec(con,conPlantSecc2nivel+"",codigoPkCampoParam+"");																
																	if(codigoPkPlanCamSec == ConstantesBD.codigoNuncaValido)
																	{
																		errores.add("descripcion",new ActionMessage("errors.notEspecific","El Consecutivo para Plantilla Campos Sección no es Valido."));
																		return errores;
																	}											
																}
															}															
														}
													}
												}																	
											}									
											
											logger.info("---------------------------------------------------");
											logger.info("OPERACION 7. Insertar Campos contenidos dentro de la Sección de Segundo Nivel contenida dentro de la Seccion de Nivel Fija. conPlantSecc >> "+conPlantSecc );
											logger.info("---------------------------------------------------");
											
											DtoCampoParametrizable campo;
											int codigoPkCampoParam;
											cambioCodigoPkArray = new ArrayList<InfoDatosInt>();
											
											//Recorre los Elementos que Componen la Sección (Campos)
											for(int j=0; j<dtoSeccionParamAnterior.getCampos().size(); j++)
											{
												campo = dtoSeccionParamAnterior.getCampos().get(j);
												
												//Toma los campos diferentes de tipo campo
												if(campo.getCodigoTipo() != ConstantesCamposParametrizables.tipoCampoFormula)
												{
													//Inserta la información del Campo Parametrizable
													codigoPkCampoParam = Plantillas.insertarCamposParametrizablesSeccion(
															con,
															campo.getNombre(),
															campo.getEtiqueta(), 
															campo.getCodigoTipo()+"",
															campo.getTamanio()+"",
															campo.getSigno(),
															campo.getCodigoUnidad()+"",
															campo.getValorPredeterminado()+"", 
															campo.getMaximo()+"", 
															campo.getMinimo()+"",
															campo.getDecimales()+"",
															campo.getColumnasOcupadas()+"",						 
															campo.getOrden()+"", 
															campo.isUnicoXFila(),
															campo.isRequerido(),
															campo.getFormulaCompleta(),
															Plantillas.cargarTipoHtml(campo.getCodigoTipo(),campo.getTipoHtml()),						
															true,
															true,
															usuario,
															campo.getManejaImagen(),
															campo.getManejaImagen().equals(ConstantesBD.acronimoSi)?Utilidades.convertirAEntero(campo.getImagenBase().getCodigo()):ConstantesBD.codigoNuncaValido);	
													
													if(codigoPkCampoParam == ConstantesBD.codigoNuncaValido)
													{
														errores.add("descripcion",new ActionMessage("errors.notEspecific","El Consecutivo del Campo Parametrizable no es Valido."));
														return errores;
													}
													else
													{
														//Almacena la información del cambio de codigos
														cambioCodigoPkArray.add(new InfoDatosInt(codigoPkCampoParam,campo.getCodigoPK()));
														
														//Valida el Ingreso de las Opciones
														if(campo.getCodigoTipo() == ConstantesCamposParametrizables.tipoCampoSeleccion || 
																campo.getCodigoTipo() == ConstantesCamposParametrizables.tipoCampoChequeo)
														{
															DtoOpcionCampoParam opcion;
														
															for(int l=0; l<campo.getOpciones().size(); l++)
															{
																opcion = campo.getOpciones().get(l);
																
																codigoPkOpcion = Plantillas.insertarOpcionesCamposParam(
																		con,
																		codigoPkCampoParam+"", 
																		opcion.getOpcion(), 
																		opcion.getValor(), 
																		usuario,
																		campo.getManejaImagen().equals(ConstantesBD.acronimoSi)?Utilidades.convertirAEntero(opcion.getConvencionOdon().getCod()):ConstantesBD.codigoNuncaValido);
																							
																if(codigoPkOpcion == ConstantesBD.codigoNuncaValido)
																{
																	errores.add("descripcion",new ActionMessage("errors.notEspecific","El Consecutivo para la Opcion del Campo Parametrizable no es Valido."));
																	return errores;
																}																
																else if(seccionParamLis.get(i).getIndicativoRestriccionValCamp().equals(ConstantesBD.acronimoNo))
																{																	
																	//Inserta las secciones Asociados
																	for(int m = 0; m < opcion.getSecciones().size(); m++)
																	{
																		if(opcion.getSecciones().get(m).getCodigoPkDetSeccion().equals(""))
																		{
																			if(!insertarSeccionesAsociadasOpciones(
																					con, 
																					codigoPkOpcion+"", 
																					opcion.getSecciones().get(m).getConsecutivoParametrizacion()+"", 
																					true, 
																					usuario))
																			{
																				errores.add("descripcion",new ActionMessage("errors.notEspecific","Problemas Almacenando la Información de Secciones Asociadas a Opciones"));
																				return errores;
																			}
																		}
																	}
																	
																	//Inserta los Valores Asociados																			
																	for(int m = 0; m < opcion.getValoresOpcion().size(); m++)
																	{
																		if(opcion.getValoresOpcion().get(m).getCodigoPk().equals(""))
																		{
																			if(!insertarValoresAsociadosOpciones(
																					con,
																					codigoPkOpcion+"", 
																					opcion.getValoresOpcion().get(m).getValor(),
																					true,
																					usuario))
																			{
																				errores.add("descripcion",new ActionMessage("errors.notEspecific","Problemas Almacenando la Información de Valores Asociadas a Opciones"));
																				return errores;
																			}
																		}
																	}
																}
															}
														}
														
														codigoPkPlanCamSec = Plantillas.insertarPlantillasCamposSec(con,conPlantSecc+"",codigoPkCampoParam+"");																
														if(codigoPkPlanCamSec == ConstantesBD.codigoNuncaValido)
														{
															errores.add("descripcion",new ActionMessage("errors.notEspecific","El Consecutivo para Plantilla Campos Sección no es Valido."));
															return errores;
														}											
													}														
												}	
											}
											
											
											//Recorre los Elementos que Componen la Sección (Campos)
											for(int j=0; j<dtoSeccionParamAnterior.getCampos().size(); j++)
											{
												campo = dtoSeccionParamAnterior.getCampos().get(j);
												
												//Toma los campos de tipo campo
												if(campo.getCodigoTipo() == ConstantesCamposParametrizables.tipoCampoFormula)
												{												
													//Inserta la información del Campo Parametrizable
													codigoPkCampoParam = Plantillas.insertarCamposParametrizablesSeccion(
															con,
															campo.getNombre(),
															campo.getEtiqueta(), 
															campo.getCodigoTipo()+"",
															campo.getTamanio()+"",
															campo.getSigno(),
															campo.getCodigoUnidad()+"",
															campo.getValorPredeterminado()+"", 
															campo.getMaximo()+"", 
															campo.getMinimo()+"",
															campo.getDecimales()+"",
															campo.getColumnasOcupadas()+"",						 
															campo.getOrden()+"", 
															campo.isUnicoXFila(),
															campo.isRequerido(),
															actualizarCodigosEnFormula(campo.getFormulaCompleta(),cambioCodigoPkArray),														
															Plantillas.cargarTipoHtml(campo.getCodigoTipo(),campo.getTipoHtml()),						
															true,
															true,
															usuario,
															campo.getManejaImagen(),
															campo.getManejaImagen().equals(ConstantesBD.acronimoSi)?Utilidades.convertirAEntero(campo.getImagenBase().getCodigo()):ConstantesBD.codigoNuncaValido);	
													
													if(codigoPkCampoParam == ConstantesBD.codigoNuncaValido)
													{
														errores.add("descripcion",new ActionMessage("errors.notEspecific","El Consecutivo del Campo Parametrizable no es Valido."));
														return errores;
													}
													else
													{
														codigoPkPlanCamSec = Plantillas.insertarPlantillasCamposSec(con,conPlantSecc+"",codigoPkCampoParam+"");																
														if(codigoPkPlanCamSec == ConstantesBD.codigoNuncaValido)
														{
															errores.add("descripcion",new ActionMessage("errors.notEspecific","El Consecutivo para Plantilla Campos Sección no es Valido."));
															return errores;
														}	
													}
												}	
											}											
										}
										else
										{
											logger.info("---------------------------------------------------");
											logger.info("OPERACION 6. No Se replica las secciones o campos de la Seccion a nivel Fija porque se cambio el tipo de Sección.");
											logger.info("---------------------------------------------------");											
										}
									}
								}
							}
							else
							{
								errores.add("descripcion",new ActionMessage("errors.notEspecific","Error Actualizar Mostrar Modificación  Nivel Fija"));
								return errores;
							}
						}						
					}		
					else
					{
						logger.info("--------------------------------------------------------------------------");
						logger.info("OPERACION 1. Solo actualizar Seccion parametrizable y no replicar. Nivel Fija");	
						logger.info("--------------------------------------------------------------------------");
						
						//Actualiza la información 
						actualizarSeccionesParametrizables(
								con, 
								seccionParamLis.get(i).getCodigo()+"", 
								seccionParamLis.get(i).getDescripcion(), 
								seccionParamLis.get(i).getOrden()+"", 
								seccionParamLis.get(i).getColumnasSeccion()+"",
								seccionParamLis.get(i).getSexoSeccion(),
								seccionParamLis.get(i).getRangoInicialEdad(),
								seccionParamLis.get(i).getRangoFInalEdad(),
								seccionParamLis.get(i).getIndicativoRestriccionValCamp(),
								seccionParamLis.get(i).getTipoSeccion(),								
								seccionParamLis.get(i).isVisible(), 
								seccionParamLis.get(i).isMostrarModificacion(), 
								seccionParamLis.get(i).getCodigoPK()+"", 
								usuario);
					}					
					
				}
				//******************************************************************************************************************************//
				//******************************************************************************************************************************//
				//******************************************************************************************************************************//				
				else if(nivel.equals("1"))
				{
					//Verifica que campos fueron modificados, si se modifico el nombre o el tipo de sección se debe crear
					//una copia de la estructura actual de la sección, la nueva sección se debe crear con el indicador de 
					//mostrar_modificacion en S y la antigua actualizar el mismo indicador en N
					if(!seccionParamLis.get(i).getDescripcion().equals(dtoPlantilla.getSeccionesFijasPos(Integer.parseInt(indexSeccionFija)).getElementoSeccionPos(Integer.parseInt(indexElemento)).getDescripcion())
							|| !seccionParamLis.get(i).getTipoSeccion().equals(dtoPlantilla.getSeccionesFijasPos(Integer.parseInt(indexSeccionFija)).getElementoSeccionPos(Utilidades.convertirAEntero(indexElemento)).getTipoSeccion())							
								|| !seccionParamLis.get(i).getIndicativoRestriccionValCamp().equals(dtoPlantilla.getSeccionesFijasPos(Integer.parseInt(indexSeccionFija)).getElementoSeccionPos(Utilidades.convertirAEntero(indexElemento)).getIndicativoRestriccionValCamp()))
					{
						logger.info("-----------------------------------------------------");
						logger.info("OPERACION 1. Replicar Sección Parametrizable. Nivel 1");						
						
						//Tomo el DtoSeccionParametrizable a modificar
						DtoSeccionParametrizable dtoSeccionParamAnterior = dtoPlantilla.getSeccionesFijasPos(Integer.parseInt(indexSeccionFija)).getElementoSeccionPos(Utilidades.convertirAEntero(indexElemento));
						
						//Inserto el nuevo dtoSeccionParametrizable con los cambios en los campos
						conSecParam =  insertarSeccionesParametrizables(
								con,
								seccionParamLis.get(i).getDescripcion(),
								seccionParamLis.get(i).getOrden()+"", 
								seccionParamLis.get(i).getColumnasSeccion()+"",
								seccionParamLis.get(i).getSexoSeccion(),
								seccionParamLis.get(i).getRangoInicialEdad(),
								seccionParamLis.get(i).getRangoFInalEdad(),
								seccionParamLis.get(i).getIndicativoRestriccionValCamp(),
								seccionParamLis.get(i).getTipoSeccion()+"", 
								"", 
								seccionParamLis.get(i).isVisible(),
								true,							 
								usuario);
						
						if(conSecParam == ConstantesBD.codigoNuncaValido)
						{
							errores.add("descripcion",new ActionMessage("errors.notEspecific","Error Replicacion Seccion Parametrizable Nivel 1."));
							return errores;
						}
						else
						{
							logger.info("-----------------------------------------------------------");
							logger.info("OPERACION 2. Insertar Nueva Seccion Parametrizable de Nivel 1. conSecParam >> "+conSecParam);
							logger.info("-----------------------------------------------------------");
							
							//Actualiza el indicador de mostrar modificación para la sección parametrizable Anterior 
							if(actualizarMostrarModOrdenSeccionParam(
									con,
									false,
									"",
									dtoSeccionParamAnterior.getCodigoPK(),									
									usuario))
							{
								logger.info("------------------------------------------------------------------------------");
								logger.info("OPERACION 3. Actualizar Anterior Sección Parametrizable (Mostrar Mod) de Nivel 1 >> "+dtoSeccionParamAnterior.getCodigoPK()+" >> "+dtoSeccionParamAnterior.getDescripcion());
								logger.info("------------------------------------------------------------------------------");
													
								//Se Inserta Plantillas Secciones
								conPlantSecc = insertarPlantillasSecciones(
										con,
										dtoPlantilla.getSeccionesFijasPos(Integer.parseInt(indexSeccionFija)).getCodigoPK()+"",
										conSecParam+"");
								
								if(conPlantSecc == ConstantesBD.codigoNuncaValido)
								{
									errores.add("descripcion",new ActionMessage("errors.notEspecific","Error Replicación Plantilla Secciones Nivel 1."));
									return errores;
								}
								else
								{									
									
									//Desasocia la seccion de los campos tipo select que la tengan asociadas														
									logger.info("Desasociar Seccion de Opciones >> codigo Pk Plantillas Secciones >> "+dtoPlantilla.getSeccionesFijasPos(Integer.parseInt(indexSeccionFija)).getElementoSeccionPos(0).getConsecutivoParametrizacion());									
									if(desasociarSeccionOculta(
																	con,
																	dtoPlantilla.getSeccionesFijasPos(Integer.parseInt(indexSeccionFija)).getElementoSeccionPos(0).getConsecutivoParametrizacion(),
																	false,
																	usuario))		
									{										
										logger.info("CODIGO PLANTILLA SECCION NUEVA >>>> "+conPlantSecc);
										logger.info("CODIGO PLANTILLA VIEJA >>>>>>> "+dtoPlantilla.getSeccionesFijasPos(Integer.parseInt(indexSeccionFija)).getElementoSeccionPos(Utilidades.convertirAEntero(indexElemento)).getConsecutivoParametrizacion());
									
										if(seccionParamLis.get(i).getIndicativoRestriccionValCamp().equals(ConstantesBD.acronimoSi))
										{
											if(!insertarSeccionesAsociadasOpcionesNuevas(
													con, 
													conPlantSecc+"", 
													dtoPlantilla.getSeccionesFijasPos(Integer.parseInt(indexSeccionFija)).getElementoSeccionPos(Utilidades.convertirAEntero(indexElemento)).getConsecutivoParametrizacion(), 
													true, 
													usuario))
											{
												logger.info("NO GUARDO LAS NUEVAS SECCIONES ASOCIADAS");
												/*errores.add("descripcion",new ActionMessage("errors.notEspecific","Problemas Almacenando la Información de Secciones Asociadas a Opciones"));
												return errores;*/
											}
										}	
									}									
									
									//en el caso en que la seccion se encontrara con Indicativo de Restriccion en N y se pasara a S, se debe borrar todas las secciones que tuviera asociadas
									if(seccionParamLis.get(i).getIndicativoRestriccionValCamp().equals(ConstantesBD.acronimoSi) && 
											dtoPlantilla.getSeccionesFijasPos(Integer.parseInt(indexSeccionFija)).getElementoSeccionPos(Utilidades.convertirAEntero(indexElemento)).getIndicativoRestriccionValCamp().equals(ConstantesBD.acronimoNo))
									{
										if(!desasociarSeccionesOcultasDeSeccionN(
												con,
												dtoPlantilla.getSeccionesFijasPos(Integer.parseInt(indexSeccionFija)).getElementoSeccionPos(0).getConsecutivoParametrizacion(), 
												false,
												usuario))
										{
											logger.info("NO DESASOCIO LAS SECCIONES OCULTAS DE LA SECCION >> "+dtoPlantilla.getSeccionesFijasPos(Integer.parseInt(indexSeccionFija)).getElementoSeccionPos(0).getConsecutivoParametrizacion());
										}													
									}
									
									logger.info("---------------------------------------------------");
									logger.info("OPERACION 4. Insertar Plantilla Secciones de Nivel 1. conPlantSecc >> "+conPlantSecc);
									logger.info("---------------------------------------------------");									
									
									//Si se cambio el tipo de Sección, no se genera replicación de los elementos de la sección, 
									//estos no son tomados en cuenta y para el usuario parece que hubieran sido eliminados									
									if(seccionParamLis.get(i).getTipoSeccion().equals(dtoPlantilla.getSeccionesFijasPos(Integer.parseInt(indexSeccionFija)).getElementoSeccionPos(Utilidades.convertirAEntero(indexElemento)).getTipoSeccion()))
									{
										int conSecParam2 = 0 ;										
										DtoSeccionParametrizable seccion2nivel;
										
										//Recorre los Elementos que Componen la Sección (Secciones)
										for(int j=0; j<dtoSeccionParamAnterior.getSecciones().size(); j++)
										{										
											seccion2nivel = dtoSeccionParamAnterior.getSecciones().get(j);						
											
											//Inserta la Nueva Sección Parametrizable de Segundo Nivel
											conSecParam2 = insertarSeccionesParametrizables(
													con, 
													seccion2nivel.getDescripcion(),
													seccion2nivel.getOrden()+"",
													seccion2nivel.getColumnasSeccion()+"",
													seccion2nivel.getSexoSeccion(),
													seccion2nivel.getRangoInicialEdad(),
													seccion2nivel.getRangoFInalEdad(),
													seccionParamLis.get(i).getIndicativoRestriccionValCamp(),													
													seccion2nivel.getTipoSeccion()+"",
													conSecParam+"",
													seccion2nivel.isVisible(),
													true,								
													usuario);
											
											if(conSecParam2 == ConstantesBD.codigoNuncaValido)					
											{
												errores.add("descripcion",new ActionMessage("errors.notEspecific","El Consecutivo de la Sección Parametrizable de Segundo Nivel es Incorrecto."));
												return errores;
											}
											else
											{
												//Insertar plantillas Secciones
												conPlantSecc2nivel = insertarPlantillasSecciones(
														con,
														dtoPlantilla.getSeccionesFijasPos(Integer.parseInt(indexSeccionFija)).getCodigoPK()+"",
														conSecParam2+"");
												
												if(conPlantSecc2nivel == ConstantesBD.codigoNuncaValido)					
												{
													errores.add("descripcion",new ActionMessage("errors.notEspecific","El Consecutivo de Plantillas Secciones de Segundo Nivel es Incorrecto."));
													return errores;
												}
												else
												{
													logger.info("---------------------------------------------------");
													logger.info("OPERACION 7. Insertar Secciones contenidas dentro de la Sección de Segundo Nivel contenida dentro de la Seccion de Nivel 1. conPlantSecc >> "+conPlantSecc );
													logger.info("---------------------------------------------------");
													
													DtoCampoParametrizable campo;
													int codigoPkCampoParam;
													cambioCodigoPkArray = new ArrayList<InfoDatosInt>();
													
													//Recorre los Campos de la sección
													for(int k=0; k<seccion2nivel.getCampos().size(); k++)
													{
														campo = seccion2nivel.getCampos().get(k);
														
														//Toma los campos diferentes de tipo campo
														if(campo.getCodigoTipo() != ConstantesCamposParametrizables.tipoCampoFormula)
														{
															//Inserta la información del Campo Parametrizable
															codigoPkCampoParam = Plantillas.insertarCamposParametrizablesSeccion(
																	con,
																	campo.getNombre(),
																	campo.getEtiqueta(), 
																	campo.getCodigoTipo()+"",
																	campo.getTamanio()+"",
																	campo.getSigno(),
																	campo.getCodigoUnidad()+"",
																	campo.getValorPredeterminado()+"", 
																	campo.getMaximo()+"", 
																	campo.getMinimo()+"",
																	campo.getDecimales()+"",
																	campo.getColumnasOcupadas()+"",						 
																	campo.getOrden()+"", 
																	campo.isUnicoXFila(),
																	campo.isRequerido(),
																	campo.getFormulaCompleta(),
																	Plantillas.cargarTipoHtml(campo.getCodigoTipo(),campo.getTipoHtml()),						
																	true,
																	true,
																	usuario,
																	campo.getManejaImagen(),
																	campo.getManejaImagen().equals(ConstantesBD.acronimoSi)?Utilidades.convertirAEntero(campo.getImagenBase().getCodigo()):ConstantesBD.codigoNuncaValido);	
															
															if(codigoPkCampoParam == ConstantesBD.codigoNuncaValido)
															{
																errores.add("descripcion",new ActionMessage("errors.notEspecific","El Consecutivo del Campo Parametrizable no es Valido."));
																return errores;
															}
															else
															{
																//Almacena la información del cambio de codigos
																cambioCodigoPkArray.add(new InfoDatosInt(codigoPkCampoParam,campo.getCodigoPK()));																
																
																//Valida el Ingreso de las Opciones
																if(campo.getCodigoTipo() == ConstantesCamposParametrizables.tipoCampoSeleccion || 
																		campo.getCodigoTipo() == ConstantesCamposParametrizables.tipoCampoChequeo)
																{
																	DtoOpcionCampoParam opcion;
																
																	for(int l=0; l<campo.getOpciones().size(); l++)
																	{
																		opcion = campo.getOpciones().get(l);
																		
																		codigoPkOpcion = Plantillas.insertarOpcionesCamposParam(
																				con,
																				codigoPkCampoParam+"", 
																				opcion.getOpcion(), 
																				opcion.getValor(), 
																				usuario,
																				campo.getManejaImagen().equals(ConstantesBD.acronimoSi)?Utilidades.convertirAEntero(opcion.getConvencionOdon().getCod()):ConstantesBD.codigoNuncaValido);
																									
																		if(codigoPkOpcion == ConstantesBD.codigoNuncaValido)
																		{
																			errores.add("descripcion",new ActionMessage("errors.notEspecific","El Consecutivo para la Opcion del Campo Parametrizable no es Valido."));
																			return errores;
																		}																		
																		else if(seccionParamLis.get(i).getIndicativoRestriccionValCamp().equals(ConstantesBD.acronimoNo))
																		{
																			//Inserta las secciones Asociados
																			for(int m = 0; m < opcion.getSecciones().size(); m++)
																			{
																				if(opcion.getSecciones().get(m).getCodigoPkDetSeccion().equals(""))
																				{
																					if(!insertarSeccionesAsociadasOpciones(
																							con, 
																							codigoPkOpcion+"", 
																							opcion.getSecciones().get(m).getConsecutivoParametrizacion()+"", 
																							true, 
																							usuario))
																					{
																						errores.add("descripcion",new ActionMessage("errors.notEspecific","Problemas Almacenando la Información de Secciones Asociadas a Opciones"));
																						return errores;
																					}
																				}
																			}
																			
																			//Inserta los Valores Asociados
																			//Inserta las secciones y valores asociados
																			for(int m = 0; m < opcion.getValoresOpcion().size(); m++)
																			{
																				if(opcion.getValoresOpcion().get(m).getCodigoPk().equals(""))
																				{
																					if(!insertarValoresAsociadosOpciones(
																							con,
																							codigoPkOpcion+"", 
																							opcion.getValoresOpcion().get(m).getValor(),
																							true,
																							usuario))
																					{
																						errores.add("descripcion",new ActionMessage("errors.notEspecific","Problemas Almacenando la Información de Valores Asociadas a Opciones"));
																						return errores;
																					}
																				}
																			}								
																		}
																	}
																}
																
																codigoPkPlanCamSec = Plantillas.insertarPlantillasCamposSec(con,conPlantSecc2nivel+"",codigoPkCampoParam+"");																
																if(codigoPkPlanCamSec == ConstantesBD.codigoNuncaValido)
																{
																	errores.add("descripcion",new ActionMessage("errors.notEspecific","El Consecutivo para Plantilla Campos Sección no es Valido."));
																	return errores;
																}											
															}														
														}	
													}
													
													//evalua los campos tipos formula, la formula posee campos asociados si se generan de nuevo, los codigo pk cambian y 
													//estos tambien deben ser cambiados en la formula
													
													//Recorre los Campos de la sección
													for(int k=0; k<seccion2nivel.getCampos().size(); k++)
													{
														campo = seccion2nivel.getCampos().get(k);
														
														//Toma los campos de tipo campo
														if(campo.getCodigoTipo() == ConstantesCamposParametrizables.tipoCampoFormula)
														{
															//Inserta la información del Campo Parametrizable
															codigoPkCampoParam = Plantillas.insertarCamposParametrizablesSeccion(
																	con,
																	campo.getNombre(),
																	campo.getEtiqueta(), 
																	campo.getCodigoTipo()+"",
																	campo.getTamanio()+"",
																	campo.getSigno(),
																	campo.getCodigoUnidad()+"",
																	campo.getValorPredeterminado()+"", 
																	campo.getMaximo()+"", 
																	campo.getMinimo()+"",
																	campo.getDecimales()+"",
																	campo.getColumnasOcupadas()+"",						 
																	campo.getOrden()+"", 
																	campo.isUnicoXFila(),
																	campo.isRequerido(),
																	actualizarCodigosEnFormula(campo.getFormulaCompleta(),cambioCodigoPkArray),																	
																	Plantillas.cargarTipoHtml(campo.getCodigoTipo(),campo.getTipoHtml()),						
																	true,
																	true,
																	usuario,
																	campo.getManejaImagen(),
																	campo.getManejaImagen().equals(ConstantesBD.acronimoSi)?Utilidades.convertirAEntero(campo.getImagenBase().getCodigo()):ConstantesBD.codigoNuncaValido);	
															
															if(codigoPkCampoParam == ConstantesBD.codigoNuncaValido)
															{
																errores.add("descripcion",new ActionMessage("errors.notEspecific","El Consecutivo del Campo Parametrizable no es Valido."));
																return errores;
															}
															else
															{
																codigoPkPlanCamSec = Plantillas.insertarPlantillasCamposSec(con,conPlantSecc2nivel+"",codigoPkCampoParam+"");																
																if(codigoPkPlanCamSec == ConstantesBD.codigoNuncaValido)
																{
																	errores.add("descripcion",new ActionMessage("errors.notEspecific","El Consecutivo para Plantilla Campos Sección no es Valido."));
																	return errores;
																}	
															}
														}	
													}
												}
											}										 																																	
										}						
										
										DtoCampoParametrizable campo;
										int codigoPkCampoParam;
										cambioCodigoPkArray = new ArrayList<InfoDatosInt>();
										
										//Recorre los Elementos que Componen la Sección (Campos)
										for(int j=0; j<dtoSeccionParamAnterior.getCampos().size(); j++)
										{
											campo = dtoSeccionParamAnterior.getCampos().get(j);
											
											//Toma los campos diferentes de tipo campo
											if(campo.getCodigoTipo() != ConstantesCamposParametrizables.tipoCampoFormula)
											{
												//Inserta la información del Campo Parametrizable
												codigoPkCampoParam = Plantillas.insertarCamposParametrizablesSeccion(
														con,
														campo.getNombre(),
														campo.getEtiqueta(), 
														campo.getCodigoTipo()+"",
														campo.getTamanio()+"",
														campo.getSigno(),
														campo.getCodigoUnidad()+"",
														campo.getValorPredeterminado()+"", 
														campo.getMaximo()+"", 
														campo.getMinimo()+"",
														campo.getDecimales()+"",
														campo.getColumnasOcupadas()+"",						 
														campo.getOrden()+"", 
														campo.isUnicoXFila(),
														campo.isRequerido(),
														campo.getFormulaCompleta(),
														Plantillas.cargarTipoHtml(campo.getCodigoTipo(),campo.getTipoHtml()),						
														true,
														true,
														usuario,
														campo.getManejaImagen(),
														campo.getManejaImagen().equals(ConstantesBD.acronimoSi)?Utilidades.convertirAEntero(campo.getImagenBase().getCodigo()):ConstantesBD.codigoNuncaValido);	
												
												if(codigoPkCampoParam == ConstantesBD.codigoNuncaValido)
												{
													errores.add("descripcion",new ActionMessage("errors.notEspecific","El Consecutivo del Campo Parametrizable no es Valido."));
													return errores;
												}
												else
												{
													//Almacena la información del cambio de codigos
													cambioCodigoPkArray.add(new InfoDatosInt(codigoPkCampoParam,campo.getCodigoPK()));
													
													//Valida el Ingreso de las Opciones
													if(campo.getCodigoTipo() == ConstantesCamposParametrizables.tipoCampoSeleccion || 
															campo.getCodigoTipo() == ConstantesCamposParametrizables.tipoCampoChequeo)
													{
														DtoOpcionCampoParam opcion;
													
														for(int l=0; l<campo.getOpciones().size(); l++)
														{
															opcion = campo.getOpciones().get(l);
															
															codigoPkOpcion = Plantillas.insertarOpcionesCamposParam(
																	con,
																	codigoPkCampoParam+"", 
																	opcion.getOpcion(), 
																	opcion.getValor(), 
																	usuario,
																	campo.getManejaImagen().equals(ConstantesBD.acronimoSi)?Utilidades.convertirAEntero(opcion.getConvencionOdon().getCod()):ConstantesBD.codigoNuncaValido);
																						
															if(codigoPkOpcion == ConstantesBD.codigoNuncaValido)
															{
																errores.add("descripcion",new ActionMessage("errors.notEspecific","El Consecutivo para la Opcion del Campo Parametrizable no es Valido."));
																return errores;
															}
															else if(seccionParamLis.get(i).getIndicativoRestriccionValCamp().equals(ConstantesBD.acronimoNo))
															{
																//Inserta las secciones Asociados
																for(int m = 0; m < opcion.getSecciones().size(); m++)
																{
																	if(opcion.getSecciones().get(m).getCodigoPkDetSeccion().equals(""))
																	{
																		if(!insertarSeccionesAsociadasOpciones(
																				con, 
																				codigoPkOpcion+"", 
																				opcion.getSecciones().get(m).getConsecutivoParametrizacion()+"", 
																				true, 
																				usuario))
																		{
																			errores.add("descripcion",new ActionMessage("errors.notEspecific","Problemas Almacenando la Información de Secciones Asociadas a Opciones"));
																			return errores;
																		}
																	}
																}
																
																//Inserta los Valores Asociados
																//Inserta las secciones y valores asociados
																for(int m = 0; m < opcion.getValoresOpcion().size(); m++)
																{
																	if(opcion.getValoresOpcion().get(m).getCodigoPk().equals(""))
																	{
																		if(!insertarValoresAsociadosOpciones(
																				con,
																				codigoPkOpcion+"", 
																				opcion.getValoresOpcion().get(m).getValor(),
																				true,
																				usuario))
																		{
																			errores.add("descripcion",new ActionMessage("errors.notEspecific","Problemas Almacenando la Información de Valores Asociadas a Opciones"));
																			return errores;
																		}
																	}
																}								
															}
														}
													}
													
													codigoPkPlanCamSec = Plantillas.insertarPlantillasCamposSec(con,conPlantSecc+"",codigoPkCampoParam+"");																
													if(codigoPkPlanCamSec == ConstantesBD.codigoNuncaValido)
													{
														errores.add("descripcion",new ActionMessage("errors.notEspecific","El Consecutivo para Plantilla Campos Sección no es Valido."));
														return errores;
													}											
												}												
											}
										}
										
										//evalua los campos tipos formula, la formula posee campos asociados si se generan de nuevo, los codigo pk cambian y 
										//estos tambien deben ser cambiados en la formula
										
										//Recorre los Elementos que Componen la Sección (Campos)
										for(int j=0; j<dtoSeccionParamAnterior.getCampos().size(); j++)
										{
											campo = dtoSeccionParamAnterior.getCampos().get(j);
											
											//Toma los campos de tipo campo
											if(campo.getCodigoTipo() == ConstantesCamposParametrizables.tipoCampoFormula)
											{
												//Inserta la información del Campo Parametrizable
												codigoPkCampoParam = Plantillas.insertarCamposParametrizablesSeccion(
														con,
														campo.getNombre(),
														campo.getEtiqueta(), 
														campo.getCodigoTipo()+"",
														campo.getTamanio()+"",
														campo.getSigno(),
														campo.getCodigoUnidad()+"",
														campo.getValorPredeterminado()+"", 
														campo.getMaximo()+"", 
														campo.getMinimo()+"",
														campo.getDecimales()+"",
														campo.getColumnasOcupadas()+"",						 
														campo.getOrden()+"", 
														campo.isUnicoXFila(),
														campo.isRequerido(),
														actualizarCodigosEnFormula(campo.getFormulaCompleta(),cambioCodigoPkArray),														
														Plantillas.cargarTipoHtml(campo.getCodigoTipo(),campo.getTipoHtml()),						
														true,
														true,
														usuario,
														campo.getManejaImagen(),
														campo.getManejaImagen().equals(ConstantesBD.acronimoSi)?Utilidades.convertirAEntero(campo.getImagenBase().getCodigo()):ConstantesBD.codigoNuncaValido);	
												
												if(codigoPkCampoParam == ConstantesBD.codigoNuncaValido)
												{
													errores.add("descripcion",new ActionMessage("errors.notEspecific","El Consecutivo del Campo Parametrizable no es Valido."));
													return errores;
												}
												else
												{
													codigoPkPlanCamSec = Plantillas.insertarPlantillasCamposSec(con,conPlantSecc+"",codigoPkCampoParam+"");																
													if(codigoPkPlanCamSec == ConstantesBD.codigoNuncaValido)
													{
														errores.add("descripcion",new ActionMessage("errors.notEspecific","El Consecutivo para Plantilla Campos Sección no es Valido."));
														return errores;
													}											
												}												
											}
										}										
									}
								}
								
							}
							else
							{
								errores.add("descripcion",new ActionMessage("errors.notEspecific","Error Actualizar Mostrar Modificación  Nivel 1."));
								return errores;
							}
						}						
					}		
					else
					{
						logger.info("--------------------------------------------------------------------------");
						logger.info("OPERACION 1. Solo actualizar Seccion parametrizable y no replicar. Nivel 1");					
						
						//Actualiza la información 
						actualizarSeccionesParametrizables(
								con, 
								seccionParamLis.get(i).getCodigo()+"", 
								seccionParamLis.get(i).getDescripcion(), 
								seccionParamLis.get(i).getOrden()+"", 
								seccionParamLis.get(i).getColumnasSeccion()+"",
								seccionParamLis.get(i).getSexoSeccion(),
								seccionParamLis.get(i).getRangoInicialEdad(),
								seccionParamLis.get(i).getRangoFInalEdad(),
								seccionParamLis.get(i).getIndicativoRestriccionValCamp(),
								seccionParamLis.get(i).getTipoSeccion(),								
								seccionParamLis.get(i).isVisible(), 
								seccionParamLis.get(i).isMostrarModificacion(), 
								seccionParamLis.get(i).getCodigoPK()+"", 
								usuario);
					}
				}				
				//******************************************************************************************************************************//
				//******************************************************************************************************************************//
				//******************************************************************************************************************************//
				else if(nivel.equals("2"))
				{
					//Verifica que campos fueron modificados, si se modifico el nombre o el tipo de sección se debe crear
					//una copia de la estructura actual de la sección, la nueva sección se debe crear con el indicador de 
					//mostrar_modificacion en S y la antigua actualizar el mismo indicador en N
					if(!seccionParamLis.get(i).getDescripcion().equals(dtoPlantilla.getSeccionesFijasPos(Integer.parseInt(indexSeccionFija)).getElementoSeccionPos(Integer.parseInt(indexElemento)).getSeccionesPos(Integer.parseInt(indexSeccionNivel2)).getDescripcion())
							|| !seccionParamLis.get(i).getTipoSeccion().equals(dtoPlantilla.getSeccionesFijasPos(Integer.parseInt(indexSeccionFija)).getElementoSeccionPos(Integer.parseInt(indexElemento)).getSeccionesPos(Integer.parseInt(indexSeccionNivel2)).getTipoSeccion()))
					{
						logger.info("-----------------------------------------------------");
						logger.info("OPERACION 1. Replicar Seccion Parametrizable. Nivel 2");						
						
						//Tomo el DtoSeccionParametrizable a modificar
						DtoSeccionParametrizable dtoSeccionParamAnterior = dtoPlantilla.getSeccionesFijasPos(Integer.parseInt(indexSeccionFija)).getElementoSeccionPos(Integer.parseInt(indexElemento)).getSeccionesPos(Integer.parseInt(indexSeccionNivel2));
						
						//Inserto el nuevo dtoSeccionParametrizable con los cambios en los campos
						conSecParam =  insertarSeccionesParametrizables(
								con,
								seccionParamLis.get(i).getDescripcion(),
								seccionParamLis.get(i).getOrden()+"", 
								seccionParamLis.get(i).getColumnasSeccion()+"",
								seccionParamLis.get(i).getSexoSeccion(),
								seccionParamLis.get(i).getRangoInicialEdad(),
								seccionParamLis.get(i).getRangoFInalEdad(),
								seccionParamLis.get(i).getIndicativoRestriccionValCamp(),
								seccionParamLis.get(i).getTipoSeccion()+"", 
								seccionParamLis.get(i).getCodigoSeccionPadre(), 
								seccionParamLis.get(i).isVisible(),
								true,							 
								usuario);
						
						if(conSecParam == ConstantesBD.codigoNuncaValido)
						{
							errores.add("descripcion",new ActionMessage("errors.notEspecific","Error Replicacion Seccion Parametrizable Nivel 2."));
							return errores;
						}
						else
						{					
							logger.info("-----------------------------------------------------------");
							logger.info("OPERACION 2. Insertar Nueva Seccion Parametrizable. Nivel 2");							
							
							//Actualiza el indicador de mostrar modificación para la sección parametrizable Anterior 
							if(actualizarMostrarModOrdenSeccionParam(
									con,
									false,
									"",
									dtoSeccionParamAnterior.getCodigoPK(),									
									usuario))
							{
								logger.info("------------------------------------------------------------------------------");
								logger.info("OPERACION 3. Actualizar Anterior Seccion Parametrizable (Mostrar Mod). Nivel 2");								
													
								//Se Inserta Plantillas Secciones
								conPlantSecc = insertarPlantillasSecciones(
										con,
										dtoPlantilla.getSeccionesFijasPos(Integer.parseInt(indexSeccionFija)).getCodigoPK()+"",
										conSecParam+"");
								
								if(conPlantSecc == ConstantesBD.codigoNuncaValido)
								{
									errores.add("descripcion",new ActionMessage("errors.notEspecific","Error Replicación Plantilla Secciones Nivel 2."));
									return errores;
								}
								else
								{
									logger.info("---------------------------------------------------");
									logger.info("OPERACION 4. Insertar Plantilla Secciones.  Nivel 2");										
									
									//Si se cambio el tipo de Sección, no se genera replicación de los elementos de la sección, 
									//estos no son tomados en cuenta y para el usuario parece que hubieran sido eliminados
									if(seccionParamLis.get(i).getTipoSeccion().equals(dtoPlantilla.getSeccionesFijasPos(Integer.parseInt(indexSeccionFija)).getElementoSeccionPos(Integer.parseInt(indexElemento)).getSeccionesPos(Integer.parseInt(indexSeccionNivel2)).getTipoSeccion()))
									{						
										DtoCampoParametrizable campo; 
										int codigoPkCampoParam;
										cambioCodigoPkArray = new ArrayList<InfoDatosInt>();
										
										//Recorre los Elementos que Componen la Sección (Campos)
										for(int j=0; j<dtoSeccionParamAnterior.getCampos().size(); j++)
										{
											campo = dtoSeccionParamAnterior.getCampos().get(j);
											
											//Toma los campos diferentes de tipo campo
											if(campo.getCodigoTipo() != ConstantesCamposParametrizables.tipoCampoFormula)
											{												
												//Inserta la información del Campo Parametrizable
												codigoPkCampoParam = Plantillas.insertarCamposParametrizablesSeccion(
														con,
														campo.getNombre(),
														campo.getEtiqueta(), 
														campo.getCodigoTipo()+"",
														campo.getTamanio()+"",
														campo.getSigno(),
														campo.getCodigoUnidad()+"",
														campo.getValorPredeterminado()+"", 
														campo.getMaximo()+"", 
														campo.getMinimo()+"",
														campo.getDecimales()+"",
														campo.getColumnasOcupadas()+"",						 
														campo.getOrden()+"", 
														campo.isUnicoXFila(),
														campo.isRequerido(),
														campo.getFormulaCompleta(),
														Plantillas.cargarTipoHtml(campo.getCodigoTipo(),campo.getTipoHtml()),						
														true,
														true,
														usuario,
														campo.getManejaImagen(),
														campo.getManejaImagen().equals(ConstantesBD.acronimoSi)?Utilidades.convertirAEntero(campo.getImagenBase().getCodigo()):ConstantesBD.codigoNuncaValido);	
												
												if(codigoPkCampoParam == ConstantesBD.codigoNuncaValido)
												{
													errores.add("descripcion",new ActionMessage("errors.notEspecific","El Consecutivo del Campo Parametrizable no es Valido."));
													return errores;
												}
												else
												{
													//Almacena la información del cambio de codigos
													cambioCodigoPkArray.add(new InfoDatosInt(codigoPkCampoParam,campo.getCodigoPK()));
													
													//Valida el Ingreso de las Opciones
													if(campo.getCodigoTipo() == ConstantesCamposParametrizables.tipoCampoSeleccion || 
															campo.getCodigoTipo() == ConstantesCamposParametrizables.tipoCampoChequeo)
													{
														DtoOpcionCampoParam opcion;
													
														for(int l=0; l<campo.getOpciones().size(); l++)
														{
															opcion = campo.getOpciones().get(l);
															
															codigoPkOpcion = Plantillas.insertarOpcionesCamposParam(
																	con,
																	codigoPkCampoParam+"", 
																	opcion.getOpcion(), 
																	opcion.getValor(), 
																	usuario,
																	campo.getManejaImagen().equals(ConstantesBD.acronimoSi)?Utilidades.convertirAEntero(opcion.getConvencionOdon().getCod()):ConstantesBD.codigoNuncaValido);
																						
															if(codigoPkOpcion == ConstantesBD.codigoNuncaValido)
															{
																errores.add("descripcion",new ActionMessage("errors.notEspecific","El Consecutivo para la Opcion del Campo Parametrizable no es Valido."));
																return errores;
															}
															else
															{
																//Inserta las secciones Asociados
																for(int m = 0; m < opcion.getSecciones().size(); m++)
																{
																	if(opcion.getSecciones().get(m).getCodigoPkDetSeccion().equals(""))
																	{
																		if(!insertarSeccionesAsociadasOpciones(
																				con, 
																				codigoPkOpcion+"", 
																				opcion.getSecciones().get(m).getConsecutivoParametrizacion()+"", 
																				true, 
																				usuario))
																		{
																			errores.add("descripcion",new ActionMessage("errors.notEspecific","Problemas Almacenando la Información de Secciones Asociadas a Opciones"));
																			return errores;
																		}
																	}
																}
																
																//Inserta los Valores Asociados
																//Inserta las secciones y valores asociados
																for(int m = 0; m < opcion.getValoresOpcion().size(); m++)
																{
																	if(opcion.getValoresOpcion().get(m).getCodigoPk().equals(""))
																	{
																		if(!insertarValoresAsociadosOpciones(
																				con,
																				codigoPkOpcion+"", 
																				opcion.getValoresOpcion().get(m).getValor(),
																				true,
																				usuario))
																		{
																			errores.add("descripcion",new ActionMessage("errors.notEspecific","Problemas Almacenando la Información de Valores Asociadas a Opciones"));
																			return errores;
																		}
																	}
																}								
															}
														}
													}
													
													codigoPkPlanCamSec = Plantillas.insertarPlantillasCamposSec(con,conPlantSecc+"",codigoPkCampoParam+"");																
													if(codigoPkPlanCamSec == ConstantesBD.codigoNuncaValido)
													{
														errores.add("descripcion",new ActionMessage("errors.notEspecific","El Consecutivo para Plantilla Campos Sección no es Valido."));
														return errores;
													}											
												}											
											}
										}
										
										//evalua los campos tipos formula, la formula posee campos asociados si se generan de nuevo, los codigo pk cambian y 
										//estos tambien deben ser cambiados en la formula
										
										//Recorre los Elementos que Componen la Sección (Campos)
										for(int j=0; j<dtoSeccionParamAnterior.getCampos().size(); j++)
										{
											campo = dtoSeccionParamAnterior.getCampos().get(j);
											
											//Toma los campos de tipo campo
											if(campo.getCodigoTipo() == ConstantesCamposParametrizables.tipoCampoFormula)
											{
												//Inserta la información del Campo Parametrizable
												codigoPkCampoParam = Plantillas.insertarCamposParametrizablesSeccion(
														con,
														campo.getNombre(),
														campo.getEtiqueta(), 
														campo.getCodigoTipo()+"",
														campo.getTamanio()+"",
														campo.getSigno(),
														campo.getCodigoUnidad()+"",
														campo.getValorPredeterminado()+"", 
														campo.getMaximo()+"", 
														campo.getMinimo()+"",
														campo.getDecimales()+"",
														campo.getColumnasOcupadas()+"",						 
														campo.getOrden()+"", 
														campo.isUnicoXFila(),
														campo.isRequerido(),														
														actualizarCodigosEnFormula(campo.getFormulaCompleta(),cambioCodigoPkArray),
														Plantillas.cargarTipoHtml(campo.getCodigoTipo(),campo.getTipoHtml()),						
														true,
														true,
														usuario,
														campo.getManejaImagen(),
														campo.getManejaImagen().equals(ConstantesBD.acronimoSi)?Utilidades.convertirAEntero(campo.getImagenBase().getCodigo()):ConstantesBD.codigoNuncaValido);	
												
												if(codigoPkCampoParam == ConstantesBD.codigoNuncaValido)
												{
													errores.add("descripcion",new ActionMessage("errors.notEspecific","El Consecutivo del Campo Parametrizable no es Valido."));
													return errores;
												}
												else
												{													
													codigoPkPlanCamSec = Plantillas.insertarPlantillasCamposSec(con,conPlantSecc+"",codigoPkCampoParam+"");																
													if(codigoPkPlanCamSec == ConstantesBD.codigoNuncaValido)
													{
														errores.add("descripcion",new ActionMessage("errors.notEspecific","El Consecutivo para Plantilla Campos Sección no es Valido."));
														return errores;
													}											
												}											
											}
										}
									}
								}
								
							}
							else
							{
								errores.add("descripcion",new ActionMessage("errors.notEspecific","Error Actualizar Mostrar Modificación  Nivel 2."));
								return errores;
							}
						}						
					}		
					else
					{
						logger.info("--------------------------------------------------------------------------");
						logger.info("OPERACION 1. Solo actualizar Seccion parametrizable y no replicar. Nivel 2");					
						
						//Actualiza la información 
						actualizarSeccionesParametrizables(
								con, 
								seccionParamLis.get(i).getCodigo()+"", 
								seccionParamLis.get(i).getDescripcion(), 
								seccionParamLis.get(i).getOrden()+"", 
								seccionParamLis.get(i).getColumnasSeccion()+"",
								seccionParamLis.get(i).getSexoSeccion(),
								seccionParamLis.get(i).getRangoInicialEdad(),
								seccionParamLis.get(i).getRangoFInalEdad(),
								seccionParamLis.get(i).getIndicativoRestriccionValCamp(),
								seccionParamLis.get(i).getTipoSeccion(),								
								seccionParamLis.get(i).isVisible(),								
								seccionParamLis.get(i).isMostrarModificacion(), 
								seccionParamLis.get(i).getCodigoPK()+"", 
								usuario);
					}
				}
			}
		}
		
		return errores;
	}
	
	//*************************************************************************************************
		
	/**
	 * Actualizar secciones parametrizables
	 * @param Connection con
	 * @param String codigoSeccion
	 * @param String descripcionSeccion
	 * @param String orden
	 * @param String columnaSeccion
	 * @param String tipo
	 * @param String mostrar
	 * @param String mostrarModificacion
	 * @param String codigoPkSeccionParam
	 * @param UsuarioBasico usuario	
	 * */
	public static boolean actualizarSeccionesParametrizables(
			Connection con, 
			String codigoSeccion,
			String descripcionSeccion,
			String orden,
			String columnaSeccion,
			String sexoSeccion,
			String edadInicialDias,
			String edadFinalDias,
			String restriccionValCampo,
			String tipo,			
			boolean mostrar,
			boolean mostrarModificacion,
			String codigoPkSeccionParam,
			UsuarioBasico usuario)
	{
		HashMap parametros = new HashMap();
		parametros.put("codigo",codigoSeccion);
		parametros.put("descripcion",descripcionSeccion);
		parametros.put("orden",orden);
		parametros.put("columnasSeccion",columnaSeccion);
				
		if(sexoSeccion.equals(ConstantesBD.codigoSexoMasculino+"")
				|| sexoSeccion.equals(ConstantesBD.codigoSexoFemenino+""))
			parametros.put("sexoSeccion",sexoSeccion);
		else
			parametros.put("sexoSeccion","");
		
		if(!edadInicialDias.equals("") && !edadFinalDias.equals(""))
		{
			parametros.put("edadInicialDias",edadInicialDias);
			parametros.put("edadFinalDias",edadFinalDias);
		}
		else
		{
			parametros.put("edadInicialDias","");
			parametros.put("edadFinalDias","");
		}	
		
		parametros.put("restriccionValCampo",restriccionValCampo);
		
		parametros.put("tipo",tipo);
		parametros.put("mostrar",mostrar?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
		parametros.put("mostrarModificacion",mostrarModificacion?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
		parametros.put("fechaModificacion",UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
		parametros.put("horaModifica",UtilidadFecha.getHoraActual());
		parametros.put("usuarioModifica",usuario.getLoginUsuario());
		parametros.put("codigoPk",codigoPkSeccionParam);	
		
		return getPLantillasDao().actualizarSeccionesParametrizables(con, parametros);	
	}
	
	//*************************************************************************************************
	
	/**
	 * Actualizar secciones parametrizables
	 * @param Connection con
	 * @param String mostrarModificacion
	 * @param String orden (si se envia vacia no actualiza el orden)
	 * @param String codigoPkSeccionParam
	 * @param UsuarioBasico usuario
	 * */
	public static boolean actualizarMostrarModOrdenSeccionParam(
			Connection con,			
			boolean mostrarModificacion,
			String orden,
			String codigoPkSeccionParam,
			UsuarioBasico usuario)
	{
		HashMap parametros = new HashMap();		
		parametros.put("mostrarModificacion",mostrarModificacion?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
		parametros.put("orden",orden);
		parametros.put("fechaModifica",UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
		parametros.put("horaModifica",UtilidadFecha.getHoraActual());
		parametros.put("usuarioModifica",usuario.getLoginUsuario());
		parametros.put("codigoPk",codigoPkSeccionParam);		
		
		//logger.info("valor de parametros >> "+parametros);
		
		return getPLantillasDao().actualizarMostrarModSeccionParam(con, parametros);	
	}
	
	
	//*************************************************************************************************
		
	/**
	 * Método implementado para guardar los campos parametrizables en plantillas_ingreso
	 */
	public static ResultadoBoolean guardarCamposParametrizablesIngreso(Connection con,DtoPlantilla plantilla, int codigoIngreso, int numeroSolicitud, int codigoPaciente, String fecha,String hora, String loginUsuario, double valoracionOdonto)
	{
		HashMap campos = new HashMap();
		campos.put("codigoIngreso", codigoIngreso);
		campos.put("numeroSolicitud", numeroSolicitud);
		campos.put("codigoPaciente", codigoPaciente);
		campos.put("fecha", fecha);
		campos.put("hora", hora);
		campos.put("loginUsuario", loginUsuario);
		campos.put("valoracionOdonto", valoracionOdonto);
		
		return getPLantillasDao().guardarCamposParametrizablesIngreso(con, plantilla, campos);
	}		
	
	//**************************************************************************************************
	//Metodo Para Insertar Escalas y Componentes
	
	/**
	 * Inserta una escala en la tabla plantillas_escalas.
	 * @param con
	 * @param codigoPlantillaSecFija
	 * @param codigoEscala
	 * @param orden
	 * @param mostrar
	 * @param mostrarModidficacion
	 * @return 
	 */
	
	public static int insertarEscalaParametrizable(
			Connection con,
			String codigoPlantillaSecFija,			
			String codigoEscala,
			String orden,
			String mostrar,
			String mostrarModidficacion,
			String Requerida
			)
	{
		HashMap mapa = new HashMap();
		mapa.put("plantillaSecFija",codigoPlantillaSecFija);
		mapa.put("escala",codigoEscala);
		mapa.put("orden",orden);
		mapa.put("mostrar",mostrar);
		mapa.put("mostrarModificacion",mostrarModidficacion);
		mapa.put("requerida",Requerida);
		
		return getPLantillasDao().insertarEscalaParametrizable(con, mapa);
	}	

	//********************************************************************************************************
	
	/**
	 * Inserta una nueva escala
	 * @param Connection con
	 * @param ArrayList<DtoEscala> arrayEscala
	 * @param String indexSeccionFija
	 * @param String nivel
	 * @param boolean actualizarOrdenMasTamano
	 * */
	public static ActionErrors insertarEscalaParametrizableSeccion(
			Connection con,
			DtoPlantilla dtoPlantilla,
			ArrayList<DtoEscala> arrayEscala,
			String indexSeccionFija,
			String nivel,
			boolean actualizarOrdenMasTamano,
			UsuarioBasico usuario)
	{		
		int conSecParam = 0;		
		int conPlanSecFij = 0;
		int conPlantSecc = 0;		
		int tmp = 0;
		ActionErrors errores = new ActionErrors();
		
		//Se recorre las escalas a crear
		for(int i = 0; i <arrayEscala.size(); i++)
		{
			///Evaluar el nivel
			if(nivel.equals("fija"))
			{
				//Actualiza el orden
				if(actualizarOrdenMasTamano)
					tmp = arrayEscala.get(i).getOrden() + dtoPlantilla.getSeccionesFijas().size();
				else
					tmp = arrayEscala.get(i).getOrden();
				
				//logger.info("orden con que se crea seccion >> "+tmp);
				
				//inserta la sección en Secciones Parametrizables
				conSecParam = insertarSeccionesParametrizablesConCodigo(
						con, 
						"",
						"",
						tmp+"",
						"1",
						"",
						"",
						"",
						ConstantesBD.acronimoNo,
						ConstantesIntegridadDominio.acronimoSeccion,
						"",
						true,
						true,
						usuario);					
			
				if(conSecParam == ConstantesBD.codigoNuncaValido)					
				{
					errores.add("descripcion",new ActionMessage("errors.notEspecific","Error al insertar Sección Parametrizable Para la Escala. Nivel Fija."));
					return errores;
				}
				else
				{					
					//Inserta la información en Plantillas Secciones Fijas
					conPlanSecFij = insertarPlantillasSeccionesFijas(
							con, 
							dtoPlantilla.getCodigoPK()+"", 
							"", 
							conSecParam+"", 
							tmp, 
							true, 
							usuario);
											
					if(conPlanSecFij == ConstantesBD.codigoNuncaValido)					
					{
						errores.add("descripcion",new ActionMessage("errors.notEspecific","Error al insertar Plantillas Secciones Fijas Para la Escala. Nivel Fija."));
						return errores;
					}
					else
					{
						//Inserta la informacion en Plantillas Escalas
						conPlantSecc = insertarEscalaParametrizable(
								con, 
								conPlanSecFij+"", 
								arrayEscala.get(i).getCodigoPK()+"",
								tmp+"", 
								ConstantesBD.acronimoSi, 
								ConstantesBD.acronimoSi,
								arrayEscala.get(i).getRequerida()+""
						);
						
						if(conPlantSecc == ConstantesBD.codigoNuncaValido)					
						{
							errores.add("descripcion",new ActionMessage("errors.notEspecific","Error al insertar Plantillas Escalas Para la Escala. Nivel Fija."));
							return errores;
						}
					}
				}								
			}
			else if (nivel.equals("1"))
			{
				//Actualiza el orden
				if(actualizarOrdenMasTamano)
					tmp = arrayEscala.get(i).getOrden() + dtoPlantilla.getSeccionesFijasPos(Integer.parseInt(indexSeccionFija)).getElementos().size();
				else
					tmp = arrayEscala.get(i).getOrden();				
						
				//Inserta la informacion en Plantillas Escalas
				conPlantSecc = insertarEscalaParametrizable(
						con, 
						dtoPlantilla.getSeccionesFijasPos(Integer.parseInt(indexSeccionFija)).getCodigoPK(), 
						arrayEscala.get(i).getCodigoPK()+"",
						tmp+"", 
						ConstantesBD.acronimoSi, 
						ConstantesBD.acronimoSi,
						arrayEscala.get(i).getRequerida()
				);
				
				if(conPlantSecc == ConstantesBD.codigoNuncaValido)					
				{
					errores.add("descripcion",new ActionMessage("errors.notEspecific","Error al insertar Plantillas Escalas Para la Escala. Nivel 1."));
					return errores;
				}													
			}		
		}
		
		return errores;
	}
	
	//********************************************************************************************************
	
	
	/**
	 * Inserta una nuevo Componente
	 * @param Connection con
	 * @param ArrayList<DtoComponente> arrayComponente
	 * @param String indexSeccionFija
	 * @param String nivel
	 * @param boolean actualizarOrdenMasTamano
	 * */
	public static ActionErrors insertarComponenteParametrizableSeccion(
			Connection con,
			DtoPlantilla dtoPlantilla,
			ArrayList<DtoComponente> arrayComponente,
			String indexSeccionFija,
			String nivel,
			boolean actualizarOrdenMasTamano,
			UsuarioBasico usuario)
	{		
		int conSecParam = 0;		
		int conPlanSecFij = 0;
		int conPlantCompo = 0;		
		int tmp = 0;
		ActionErrors errores = new ActionErrors();
		
		//Se recorre las escalas a crear
		for(int i = 0; i <arrayComponente.size(); i++)
		{
			///Evaluar el nivel
			if(nivel.equals("fija"))
			{
				//Actualiza el orden
				if(actualizarOrdenMasTamano)
					tmp = arrayComponente.get(i).getOrden() + dtoPlantilla.getSeccionesFijas().size();
				else
					tmp = arrayComponente.get(i).getOrden();
				
				//logger.info("orden con que se crea seccion >> "+tmp);
				
				//inserta la sección en Secciones Parametrizables
				conSecParam = insertarSeccionesParametrizablesConCodigo(
						con, 
						"",
						"",
						tmp+"",
						"1",
						"",
						"",
						"",
						ConstantesBD.acronimoNo,
						ConstantesIntegridadDominio.acronimoSeccion,
						"",
						true,
						true,
						usuario);					
			
				if(conSecParam == ConstantesBD.codigoNuncaValido)					
				{
					errores.add("descripcion",new ActionMessage("errors.notEspecific","Error al insertar Sección Parametrizable Para El Componente. Nivel Fija."));
					return errores;
				}
				else
				{					
					//Inserta la información en Plantillas Secciones Fijas
					conPlanSecFij = insertarPlantillasSeccionesFijas(
							con, 
							dtoPlantilla.getCodigoPK()+"", 
							"", 
							conSecParam+"", 
							tmp, 
							true, 
							usuario);
											
					if(conPlanSecFij == ConstantesBD.codigoNuncaValido)					
					{
						errores.add("descripcion",new ActionMessage("errors.notEspecific","Error al insertar Plantillas Secciones Fijas Para el Componente. Nivel Fija."));
						return errores;
					}
					else
					{
						//Inserta la informacion en Plantillas Componentes
						conPlantCompo = insertarComponenteParametrizable(
								con,
								conPlanSecFij+"",
								arrayComponente.get(i).getCodigoPK(),
								tmp+"",
								ConstantesBD.acronimoSi,
								ConstantesBD.acronimoSi);
						
						if(conPlantCompo == ConstantesBD.codigoNuncaValido)					
						{
							errores.add("descripcion",new ActionMessage("errors.notEspecific","Error al insertar Plantillas Componentes Para el Componente. Nivel Fija."));
							return errores;
						}
					}
				}								
			}
			else if (nivel.equals("1"))
			{
				//Actualiza el orden
				if(actualizarOrdenMasTamano)
					tmp = arrayComponente.get(i).getOrden() + dtoPlantilla.getSeccionesFijasPos(Integer.parseInt(indexSeccionFija)).getElementos().size();
				else
					tmp = arrayComponente.get(i).getOrden();				
						
				//Inserta la información en Plantillas Escalas
				conPlantCompo = insertarComponenteParametrizable(
						con, 
						dtoPlantilla.getSeccionesFijasPos(Integer.parseInt(indexSeccionFija)).getCodigoPK(),
						arrayComponente.get(i).getCodigoPK()+"",
						tmp+"", 
						ConstantesBD.acronimoSi, 
						ConstantesBD.acronimoSi);		
				
				if(conPlantCompo == ConstantesBD.codigoNuncaValido)					
				{
					errores.add("descripcion",new ActionMessage("errors.notEspecific","Error al insertar Plantillas Componentes Para el Componente. Nivel 1."));
					return errores;
				}													
			}		
		}
		
		return errores;
	}
	
	//********************************************************************************************************
	
	
	 /**
	 * Inserta un componente en la tabla plantillas_componentes.
	 * @param con
	 * @param codigoPlantillaSecFija
	 * @param codigoComponente
	 * @param orden
	 * @param mostrar
	 * @param mostrarModidficacion
	 * @return
	 */
	public static int insertarComponenteParametrizable(
			Connection con,
			String codigoPlantillaSecFija,			
			String codigoComponente,
			String orden,
			String mostrar,
			String mostrarModidficacion		
			)
	{
		HashMap mapa = new HashMap();
		mapa.put("plantillaSecFija",codigoPlantillaSecFija);
		mapa.put("componente",codigoComponente);
		mapa.put("orden",orden);
		mapa.put("mostrar",mostrar);
		mapa.put("mostrarModificacion",mostrarModidficacion);
		mapa.put("fechaInicialRegistro",UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
		mapa.put("horaInicialRegistro",UtilidadFecha.getHoraActual());
		
		return getPLantillasDao().insertarComponenteParametrizable(con, mapa);
	}
	
	//*******************************************************************************************************
	
	//*******************************************************************************************************
		//Metodos Para Realizar Actualizaciones de Escalas y Componentes.
	
	
	/**
	 * 
	 * @param con
	 * @param codigoPk
	 * @param codigoEscala
	 * @param orden
	 * @param mostrar
	 * @param mostrarModificacion
	 * @return
	 */
	public static boolean actualizarEscalaParametrizable(
			Connection con,
			String codigoPk,			
			String orden,
			String mostrar,
			String mostrarModificacion
			)
	{
		HashMap parametros= new HashMap();
		parametros.put("codigoPk", codigoPk);		
		parametros.put("orden", orden);
		parametros.put("mostrar", mostrar);
		parametros.put("mostrarModificacion", mostrarModificacion);
		
		return getPLantillasDao().actualizarEscalaParametrizable(con, parametros);
		
	}
	
	
	//*******************************************************************************************************
	
	/**
	 * 
	 * @param con
	 * @param codigoPk
	 * @param codigoComponente
	 * @param orden
	 * @param mostrar
	 * @param mostrarModificacion
	 * @return
	 */
	public static boolean actualizarComponenteParametrizable(
			Connection con,
			String codigoPk,
			String codigoComponente,
			String orden,
			String mostrar,
			String mostrarModificacion
			)
	{
		HashMap parametros= new HashMap();
		parametros.put("codigoPk",codigoPk);
		parametros.put("componente",codigoComponente);
		parametros.put("orden", orden);
		parametros.put("mostrar", mostrar);
		parametros.put("mostrarModificacion", mostrarModificacion);
		parametros.put("fechaFinalRegistro", UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
		parametros.put("horaFinalRegistro", UtilidadFecha.getHoraActual());
		
		return getPLantillasDao().actualizarComponenteParametrizable(con, parametros);
		
	}	
	//*******************************************************************************************************
		
	/*************************************************************************************************/
	//*************************************************************************************************
	//Metodos Campo Parametrizables********************************************************************
	/*************************************************************************************************/
	
	
	/**
	 * Inserta datos en campo parametrizables
	 * @param Connection con
	 * @param String codigoCampo
	 * @param String nombreCampo
	 * @param String etiqueta
	 * @param String tipo
	 * @param String tamanio
	 * @param String signo
	 * @param String unidad
	 * @param String valorPredeterminado
	 * @param String maximo
	 * @param String minimo
	 * @param String decimales
	 * @param String columnasOcupadas
	 * @param String orden
	 * @param boolean unicoFila
	 * @param boolean requerido
	 * @param String formula
	 * @param String tipoHtml
	 * @param boolean mostrar
	 * @param UsuarioBasico usuario
	 * @return
	 */
	public static int insertarCamposParametrizablesSeccion(
			Connection con,
			String nombreCampo,
			String etiqueta,
			String tipo,
			String tamanio,
			String signo,
			String unidad,
			String valorPredeterminado,
			String maximo,
			String minimo,
			String decimales,
			String columnasOcupadas,
			String orden,
			boolean unicoFila,
			boolean requerido,
			String formula,
			String tipoHtml,
			boolean mostrar,
			boolean mostrarModificacion,
			UsuarioBasico usuario,
			String manejaImg,
			int imagenAsociar)
	{
		HashMap mapa = new HashMap();
		mapa.put("nombre",nombreCampo);
		mapa.put("etiqueta",etiqueta);
		mapa.put("tipo",tipo);
		mapa.put("tamanio",tamanio);
		mapa.put("signo",signo);
		mapa.put("unidad",unidad);
		mapa.put("valor_predeterminado",valorPredeterminado);
		mapa.put("maximo",maximo);
		mapa.put("minimo",minimo);
		mapa.put("decimales",decimales);
		mapa.put("columnas_ocupadas",columnasOcupadas);
		mapa.put("orden",orden);
		mapa.put("unico_fila",unicoFila?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
		mapa.put("requerido",requerido?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
		mapa.put("formula",formula);
		mapa.put("tipo_html",tipoHtml);
		mapa.put("mostrar",mostrar?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
		mapa.put("mostrarModificacion",mostrarModificacion?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);	
		
		mapa.put("institucion",usuario.getCodigoInstitucion());
		mapa.put("usuarioModifica",usuario.getLoginUsuario());
		mapa.put("fechaModifica",UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
		mapa.put("horaModifica",UtilidadFecha.getHoraActual());
		
		// Anexo 841
		mapa.put("manejaImg", manejaImg);
		if(imagenAsociar!=ConstantesBD.codigoNuncaValido)
			mapa.put("imagenAsociar",imagenAsociar);
		
		
		return getPLantillasDao().insertarCamposParametrizables(con,mapa);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoCampo
	 * @return
	 */
	public static int insertarCamposParametrizablesSeccionConCodigo(
			Connection con,
			String codigoCampo,
			String nombreCampo,
			String etiqueta,
			String tipo,
			String tamanio,
			String signo,
			String unidad,
			String valorPredeterminado,
			String maximo,
			String minimo,
			String decimales,
			String columnasOcupadas,
			String orden,
			boolean unicoFila,
			boolean requerido,
			String formula,
			String tipoHtml,
			boolean mostrar,
			boolean mostrarModificacion,
			UsuarioBasico usuario,
			String manejaImg,
			int imagenAsociar)
	{
		HashMap mapa = new HashMap();
		mapa.put("codigo",codigoCampo);
		mapa.put("nombre",nombreCampo);
		mapa.put("etiqueta",etiqueta);
		mapa.put("tipo",tipo);
		mapa.put("tamanio",tamanio);
		mapa.put("signo",signo);
		mapa.put("unidad",unidad);
		mapa.put("valor_predeterminado",valorPredeterminado);
		mapa.put("maximo",maximo);
		mapa.put("minimo",minimo);
		mapa.put("decimales",decimales);
		mapa.put("columnas_ocupadas",columnasOcupadas);
		mapa.put("orden",orden);
		mapa.put("unico_fila",unicoFila?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
		mapa.put("requerido",requerido?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
		mapa.put("formula",formula);
		mapa.put("tipo_html",tipoHtml);
		mapa.put("mostrar",mostrar?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
		mapa.put("mostrarModificacion",mostrarModificacion?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);	
		
		mapa.put("institucion",usuario.getCodigoInstitucion());
		mapa.put("usuarioModifica",usuario.getLoginUsuario());
		mapa.put("fechaModifica",UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
		mapa.put("horaModifica",UtilidadFecha.getHoraActual());
		
		// Anexo 841
		mapa.put("manejaImg", manejaImg);
		if(imagenAsociar!=ConstantesBD.codigoNuncaValido)
			mapa.put("imagenAsociar",imagenAsociar);
		
		
		return getPLantillasDao().insertarCamposParametrizables(con,mapa);
	}
	
	//********************************************************************************************************
	
	/**
	 * Actualiza los codigospk contenidos dentro de un campo tipo formula
	 * @param String cadenaFormula
	 * @param ArrayList<InfoDatosInt> cambioCodigoPkArray
	 * */
	public static String actualizarCodigosEnFormula(String cadenaFormula,ArrayList<InfoDatosInt> cambioCodigoPkArray)
	{	
		for(int i=0; i<cambioCodigoPkArray.size(); i++)
		{
			if(cadenaFormula.contains("__"+cambioCodigoPkArray.get(i).getNombre()+"__") && 
					Utilidades.convertirAEntero(cambioCodigoPkArray.get(i).getNombre()) > 0 && 
						cambioCodigoPkArray.get(i).getCodigo() > 0)
				cadenaFormula = cadenaFormula.replace("__"+cambioCodigoPkArray.get(i).getNombre()+"__","__"+cambioCodigoPkArray.get(i).getCodigo()+"__");			
		}		
		
		return cadenaFormula; 
	}
	
	//********************************************************************************************************
	
	/**
	 * Devuelve el tipo HTml dependiendo del Tipo de Campo
	 * @param int tipoCampo
	 * @param Strin tipoHtml (opcional)
	 * */
	public static String cargarTipoHtml(int tipoCampo, String tipoHtml)
	{
		if((tipoCampo==ConstantesCamposParametrizables.tipoCampoCaracter) ||
				(tipoCampo==ConstantesCamposParametrizables.tipoCampoNumericoEntero)|| 
					(tipoCampo==ConstantesCamposParametrizables.tipoCampoNumericoDecimal)||
						(tipoCampo==ConstantesCamposParametrizables.tipoCampoFecha)||
							(tipoCampo==ConstantesCamposParametrizables.tipoCampoHora)||
								(tipoCampo==ConstantesCamposParametrizables.tipoCampoFormula))		
			return ConstantesCamposParametrizables.campoTipoText;		
		else if(tipoCampo==ConstantesCamposParametrizables.tipoCampoAreaTexto)		
			return ConstantesCamposParametrizables.campoTipoTextArea;
		else if(tipoCampo==ConstantesCamposParametrizables.tipoCampoChequeo && tipoHtml.equals(""))
			return ConstantesCamposParametrizables.campoTipoCheckBox;
		else if(tipoCampo==ConstantesCamposParametrizables.tipoCampoChequeo && tipoHtml.equals(ConstantesCamposParametrizables.campoTipoRadio))		
			return ConstantesCamposParametrizables.campoTipoRadio;		
		else if(tipoCampo==ConstantesCamposParametrizables.tipoCampoChequeo && tipoHtml.equals(ConstantesCamposParametrizables.campoTipoCheckBox))
			return ConstantesCamposParametrizables.campoTipoCheckBox;
		else if(tipoCampo==ConstantesCamposParametrizables.tipoCampoSeleccion)		
			return ConstantesCamposParametrizables.campoTipoSelect;		
		else if(tipoCampo==ConstantesCamposParametrizables.tipoCampoArchivo)
			return ConstantesCamposParametrizables.campoTipoARCH;
		else if(tipoCampo==ConstantesCamposParametrizables.tipoCampoTextoPredeterminado)
			return ConstantesCamposParametrizables.campoTipoLabel;
		
		return "";
	}
	
	//********************************************************************************************************
	
	/**
	 * Inserta informacion dentro de la tabla de Opciones de Campo Param
	 * @param Connection con 
	 * @param String codigoPkCampoParam
	 * @param String opcion
	 * @param String valor 
	 * @param UsuarioBasico usuario
	 * */
	public static int insertarOpcionesCamposParam(Connection con,
			String codigoPkCampoParam,
			String opcion,
			String valor,
			UsuarioBasico usuario,
			int convencion)
	{
		HashMap mapa = new HashMap();
		mapa.put("campo_parametrizable",codigoPkCampoParam);
		mapa.put("opcion",opcion);
		mapa.put("valor",valor);
		mapa.put("institucion",usuario.getCodigoInstitucion());
		mapa.put("usuarioModifica",usuario.getLoginUsuario());
		mapa.put("fechaModifica",UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
		mapa.put("horaModifica",UtilidadFecha.getHoraActual());
		if(convencion!=ConstantesBD.codigoNuncaValido)
			mapa.put("convencion",convencion);
		
		
		return getPLantillasDao().insertarOpcionesCamposParam(con,mapa);
	}
	
	//*******************************************************************************************************
	
	/**
	 * Inserta informacion dentro de la tabla de Opciones de Campo Param
	 * @param Connection con 
	 * @param String codigoPkPlantillaSec
	 * @param ArrayList<DtoCampoParametrizable> camposArray
	 * @param UsuarioBasico usuario
	 * */
	public static ActionErrors insertarCamposParametrizablesSeccion(
			Connection con,
			String codigoPkPlantillaSec,
			ArrayList<DtoCampoParametrizable> camposArray,
			UsuarioBasico usuario)
	{
		ActionErrors errores  = new ActionErrors();
		DtoCampoParametrizable campo;
		DtoOpcionCampoParam opcion;
		int codigoPkCampoParam = 0;
		int codigoPkOpcion = 0;		
		int codigoPkPlanCamSec = 0;
						
		//Se recorren los campos 
		for(int i=0; i<camposArray.size(); i++)
		{
			campo = camposArray.get(i);
			
			//Verifica si existe el Campo			
			if(campo.getCodigoPK().equals(""))
			{			
				//Inserta la información del Campo Parametrizable
				codigoPkCampoParam = Plantillas.insertarCamposParametrizablesSeccion(
						con,
						campo.getNombre(),
						campo.getEtiqueta(), 
						campo.getCodigoTipo()+"",
						campo.getTamanio()+"",
						campo.getSigno(),
						campo.getCodigoUnidad()+"",
						campo.getValorPredeterminado()+"", 
						campo.getMaximo()+"", 
						campo.getMinimo()+"",
						campo.getDecimales()+"",
						campo.getColumnasOcupadas()+"",						 
						campo.getOrden()+"", 
						campo.isUnicoXFila(),
						campo.isRequerido(),
						campo.getFormulaCompleta(),
						Plantillas.cargarTipoHtml(campo.getCodigoTipo(),campo.getTipoHtml()),						
						true,
						true,
						usuario,
						campo.getManejaImagen(),
						campo.getManejaImagen().equals(ConstantesBD.acronimoSi)?Utilidades.convertirAEntero(campo.getImagenBase().getCodigo()):ConstantesBD.codigoNuncaValido);	
				
				if(codigoPkCampoParam == ConstantesBD.codigoNuncaValido)
				{
					errores.add("descripcion",new ActionMessage("errors.notEspecific","El Consecutivo del Campo Parametrizable no es Valido."));
					return errores;
				}
				else
				{
					//Valida el Ingreso de las Opciones
					if(campo.getCodigoTipo() == ConstantesCamposParametrizables.tipoCampoSeleccion || 
							campo.getCodigoTipo() == ConstantesCamposParametrizables.tipoCampoChequeo)
					{
						for(int j = 0 ; j <camposArray.get(i).getOpciones().size(); j++)
						{
							opcion = camposArray.get(i).getOpciones().get(j);
							
							codigoPkOpcion = Plantillas.insertarOpcionesCamposParam(
									con,
									codigoPkCampoParam+"", 
									opcion.getOpcion(), 
									opcion.getValor(), 
									usuario,
									camposArray.get(i).getManejaImagen().equals(ConstantesBD.acronimoSi)?Utilidades.convertirAEntero(opcion.getConvencionOdon().getCod()):ConstantesBD.codigoNuncaValido);
														
							if(codigoPkOpcion == ConstantesBD.codigoNuncaValido)
							{
								errores.add("descripcion",new ActionMessage("errors.notEspecific","El Consecutivo para la Opcion del Campo Parametrizable no es Valido."));
								return errores;
							}
							else
							{
								//Inserta las secciones Asociados
								for(int k = 0; k < opcion.getSecciones().size(); k++)
								{
									if(opcion.getSecciones().get(k).getCodigoPkDetSeccion().equals(""))
									{
										if(!insertarSeccionesAsociadasOpciones(
												con, 
												codigoPkOpcion+"", 
												opcion.getSecciones().get(k).getConsecutivoParametrizacion()+"", 
												true, 
												usuario))
										{
											errores.add("descripcion",new ActionMessage("errors.notEspecific","Problemas Almacenando la Información de Secciones Asociadas a Opciones"));
											return errores;
										}
									}
								}
								
								//Inserta los Valores Asociados								
								for(int k = 0; k < opcion.getValoresOpcion().size(); k++)
								{
									if(opcion.getValoresOpcion().get(k).getCodigoPk().equals(""))
									{
										if(!insertarValoresAsociadosOpciones(
												con,
												codigoPkOpcion+"", 
												opcion.getValoresOpcion().get(k).getValor(),
												true,
												usuario))
										{
											errores.add("descripcion",new ActionMessage("errors.notEspecific","Problemas Almacenando la Información de Valores Asociadas a Opciones"));
											return errores;
										}
									}
								}								
							}
						}
					}
					
					codigoPkPlanCamSec = Plantillas.insertarPlantillasCamposSec(con,codigoPkPlantillaSec,codigoPkCampoParam+"");
					
					if(codigoPkPlanCamSec == ConstantesBD.codigoNuncaValido)
					{
						errores.add("descripcion",new ActionMessage("errors.notEspecific","El Consecutivo para Plantilla Campos Sección no es Valido."));
						return errores;
					}
				}
			}
		}
		
		return errores;
	}	
	
	//*******************************************************************************************************
	
	
	/**
	 * Modifica información dentro de la tabla de Opciones de Campo Param
	 * @param Connection con 
	 * @param String codigoPkPlantillaSec
	 * @param ArrayList<DtoCampoParametrizable> camposArray
	 * @param UsuarioBasico usuario
	 * */
	public static ActionErrors actualizarCamposParametrizables(
			Connection con,
			String codigoPkPlantillaSec,
			ArrayList<DtoCampoParametrizable> camposArrayNuevo,
			ArrayList<DtoCampoParametrizable> camposArrayAnterior,
			UsuarioBasico usuario)
	{
		ActionErrors errores  = new ActionErrors();
		DtoCampoParametrizable campo;
		DtoOpcionCampoParam opcion;
		int codigoPkCampoParam = 0;
		int codigoPkOpcion = 0;		
		int codigoPkPlanCamSec = 0;
		ArrayList<InfoDatosInt> cambioCodigoPkArray = new ArrayList<InfoDatosInt>();
				
		//Se recorren los campos diferentes a tipo formula
		for(int i=0; i<camposArrayNuevo.size(); i++)
		{
			campo = camposArrayNuevo.get(i);			
			//Solo se toma los campos diferentes a formula
			if(campo.getCodigoTipo() != ConstantesCamposParametrizables.tipoCampoFormula)
			{			
				//Verifica si existe el Campo			
				if(!campo.getCodigoPK().equals(""))
				{
					//Evalua si se cambiaron los campos Nombre, Tipo, Etiqueta Y Unidad se Replica el Campo			
					if(validarHayCambioCamposReplicacion(con, campo, camposArrayAnterior))
					{
						logger.info("---------------------------------------------------");
						logger.info("OPERACION 1. Actualiza el Campo Parametrizable a Mostrar Modificacion en No (replicacion) ");
						
						//Actualizar indicador a No
						if(Plantillas.actualizarMostrarModCamposParametrizables(con,campo.getCodigoPK(),false, usuario))
						{						
							//Insertamos el nuevo campo
							codigoPkCampoParam = Plantillas.insertarCamposParametrizablesSeccionConCodigo(
									con,
									campo.getCodigo(),
									campo.getNombre(),
									campo.getEtiqueta(), 
									campo.getCodigoTipo()+"",
									campo.getTamanio()+"",
									campo.getSigno(),
									campo.getCodigoUnidad()+"",
									campo.getValorPredeterminado()+"", 
									campo.getMaximo()+"", 
									campo.getMinimo()+"",
									campo.getDecimales()+"",
									campo.getColumnasOcupadas()+"",						 
									campo.getOrden()+"", 
									campo.isUnicoXFila(),
									campo.isRequerido(),
									campo.getFormulaCompleta(),
									Plantillas.cargarTipoHtml(campo.getCodigoTipo(),campo.getTipoHtml()),						
									true,
									true,
									usuario,
									campo.getManejaImagen(),
									campo.getManejaImagen().equals(ConstantesBD.acronimoSi)?Utilidades.convertirAEntero(campo.getImagenBase().getCodigo()):ConstantesBD.codigoNuncaValido);	
							
							if(codigoPkCampoParam == ConstantesBD.codigoNuncaValido)
							{
								errores.add("descripcion",new ActionMessage("errors.notEspecific","El Consecutivo del Campo Parametrizable Replicado no es Valido."));
								return errores;
							}
							else
							{
								//Almacena la información del cambio de codigos
								cambioCodigoPkArray.add(new InfoDatosInt(codigoPkCampoParam,campo.getCodigoPK()));
								
								logger.info("---------------------------------------------------");
								logger.info("OPERACION 2. Inserta las Opciones Si Existen (replicacion)");
								
								//Valida el Ingreso de las Opciones
								if(campo.getCodigoTipo() == ConstantesCamposParametrizables.tipoCampoSeleccion || 
										campo.getCodigoTipo() == ConstantesCamposParametrizables.tipoCampoChequeo)
								{
									for(int j = 0 ; j <campo.getOpciones().size(); j++)
									{
										opcion = campo.getOpciones().get(j);
										
										codigoPkOpcion = Plantillas.insertarOpcionesCamposParam(
												con,
												codigoPkCampoParam+"", 
												opcion.getOpcion(), 
												opcion.getValor(), 
												usuario,
												campo.getManejaImagen().equals(ConstantesBD.acronimoSi)?Utilidades.convertirAEntero(opcion.getConvencionOdon().getCod()):ConstantesBD.codigoNuncaValido);
																	
										if(codigoPkOpcion == ConstantesBD.codigoNuncaValido)
										{
											errores.add("descripcion",new ActionMessage("errors.notEspecific","El Consecutivo para la Opcion Replicada del Campo Parametrizable no es Valido."));
											return errores;
										}
										else
										{
											//Inserta las secciones Asociados
											for(int m = 0; m < opcion.getSecciones().size(); m++)
											{
												if(opcion.getSecciones().get(m).getCodigoPkDetSeccion().equals(""))
												{
													if(!insertarSeccionesAsociadasOpciones(
															con, 
															codigoPkOpcion+"", 
															opcion.getSecciones().get(m).getConsecutivoParametrizacion()+"", 
															true, 
															usuario))
													{
														errores.add("descripcion",new ActionMessage("errors.notEspecific","Problemas Almacenando la Información de Secciones Asociadas a Opciones"));
														return errores;
													}
												}
											}
											
											//Inserta los Valores Asociados
											//Inserta las secciones y valores asociados
											for(int m = 0; m < opcion.getValoresOpcion().size(); m++)
											{
												if(opcion.getValoresOpcion().get(m).getCodigoPk().equals(""))
												{
													if(!insertarValoresAsociadosOpciones(
															con,
															codigoPkOpcion+"", 
															opcion.getValoresOpcion().get(m).getValor(),
															true,
															usuario))
													{
														errores.add("descripcion",new ActionMessage("errors.notEspecific","Problemas Almacenando la Información de Valores Asociadas a Opciones"));
														return errores;
													}
												}
											}								
										}
									}
								}
								
								logger.info("---------------------------------------------------");
								logger.info("OPERACION 3. Inserta Un nuevo Registro en Plantillas Campos Secciones (replicacion)");
								
								codigoPkPlanCamSec = Plantillas.insertarPlantillasCamposSec(con,codigoPkPlantillaSec,codigoPkCampoParam+"");
								
								if(codigoPkPlanCamSec == ConstantesBD.codigoNuncaValido)
								{
									errores.add("descripcion",new ActionMessage("errors.notEspecific","El Consecutivo para Plantilla Campos Sección Replicada no es Valida."));
									return errores;
								}							
							}					
						}
						else
						{
							errores.add("descripcion",new ActionMessage("errors.notEspecific","Error al Actualizar el Campo como Mostrar Modificacion No"));
							return errores;
						}									
					}
					else
					{
						logger.info("---------------------------------------------------");
						logger.info("OPERACION 1. Actualiza la Información del Campo Parametrizable");
						
						if(Plantillas.actualizarCamposParametrizables(
								con,
								campo.getCodigoPK(),
								campo.getCodigo(),
								campo.getNombre(),
								campo.getEtiqueta(), 
								campo.getCodigoTipo()+"",
								campo.getTamanio()+"",
								campo.getSigno(),
								campo.getCodigoUnidad()+"",
								campo.getValorPredeterminado()+"", 
								campo.getMaximo()+"", 
								campo.getMinimo()+"",
								campo.getDecimales()+"",
								campo.getColumnasOcupadas()+"",						 
								campo.getOrden()+"", 
								campo.isUnicoXFila(),
								campo.isRequerido(),
								campo.getFormulaCompleta(),
								Plantillas.cargarTipoHtml(campo.getCodigoTipo(),campo.getTipoHtml()),						
								true,							
								usuario,
								campo.getManejaImagen(),
								Utilidades.convertirAEntero(campo.getImagenAsociar())))
						{
							logger.info("---------------------------------------------------");
							logger.info("OPERACION 2. Actualiza las Opciones Si Existen");
							
							//Valida el Ingreso de las Opciones
							if(campo.getCodigoTipo() == ConstantesCamposParametrizables.tipoCampoSeleccion || 
									campo.getCodigoTipo() == ConstantesCamposParametrizables.tipoCampoChequeo)
							{
								for(int j = 0 ; j <campo.getOpciones().size(); j++)
								{
									opcion = campo.getOpciones().get(j);
									
									if(!opcion.getCodigoPk().equals(""))
									{	
										logger.info("\n\n\n\n*************** CONVENCION  codigo >>"+opcion.getConvencionOdon().getCod()+"  id>> "+opcion.getConvencionOdon().getId());
										if(!Plantillas.actualizarOpcionesCamposParam(
												con, 
												opcion.getCodigoPk(),
												opcion.getOpcion(),
												opcion.getValor(),
												usuario,
												Utilidades.convertirAEntero(opcion.getConvencionOdon().getCod())))
										{
											errores.add("descripcion",new ActionMessage("errors.notEspecific","No se Actualizo las Opciones del Campo Parametrizable."));
											return errores;								
										}
										else
										{
											//Inserta las secciones Asociados
											for(int m = 0; m < opcion.getSecciones().size(); m++)
											{
												if(opcion.getSecciones().get(m).getCodigoPkDetSeccion().equals(""))
												{
													if(!insertarSeccionesAsociadasOpciones(
															con, 
															opcion.getCodigoPk(),
															opcion.getSecciones().get(m).getConsecutivoParametrizacion(),
															true, 
															usuario))
													{
														errores.add("descripcion",new ActionMessage("errors.notEspecific","Problemas Almacenando la Información de Secciones Asociadas a Opciones"));
														return errores;
													}
												}
											}
											
											//Inserta los Valores Asociados																
											for(int m = 0; m < opcion.getValoresOpcion().size(); m++)
											{
												if(opcion.getValoresOpcion().get(m).getCodigoPk().equals(""))
												{
													if(!insertarValoresAsociadosOpciones(
															con, 
															opcion.getCodigoPk(),
															opcion.getValoresOpcion().get(m).getValor(), 
															true, 
															usuario))
													{					
														errores.add("descripcion",new ActionMessage("errors.notEspecific","Problemas Almacenando la Información de Valores Asociados a Opciones"));
														return errores;
													}
												}
											}								
										}
									}
									else
									{
										codigoPkOpcion = insertarOpcionesCamposParam(
												con, 
												campo.getCodigoPK()+"",											 
												opcion.getOpcion(),
												opcion.getValor(), 
												usuario,
												campo.getManejaImagen().equals(ConstantesBD.acronimoSi)?Utilidades.convertirAEntero(opcion.getConvencionOdon().getCod()):ConstantesBD.codigoNuncaValido);
										
										if(codigoPkOpcion == ConstantesBD.codigoNuncaValido)
										{
											errores.add("descripcion",new ActionMessage("errors.notEspecific","No se Inserto las Opciones del Campo Parametrizable."));
											return errores;
										}
										else
										{
											//Inserta las secciones Asociados
											for(int m = 0; m < opcion.getSecciones().size(); m++)
											{
												if(opcion.getSecciones().get(m).getCodigoPkDetSeccion().equals(""))
												{
													if(!insertarSeccionesAsociadasOpciones(
															con, 
															codigoPkOpcion+"", 
															opcion.getSecciones().get(m).getConsecutivoParametrizacion()+"", 
															true, 
															usuario))
													{
														errores.add("descripcion",new ActionMessage("errors.notEspecific","Problemas Almacenando la Información de Secciones Asociadas a Opciones"));
														return errores;
													}
												}
											}
											
											//Inserta los Valores Asociados										
											for(int m = 0; m < opcion.getValoresOpcion().size(); m++)
											{
												if(opcion.getValoresOpcion().get(m).getCodigoPk().equals(""))
												{
													if(!insertarValoresAsociadosOpciones(
															con,
															codigoPkOpcion+"", 
															opcion.getValoresOpcion().get(m).getValor(),
															true,
															usuario))
													{
														errores.add("descripcion",new ActionMessage("errors.notEspecific","Problemas Almacenando la Información de Valores Asociadas a Opciones"));
														return errores;
													}
												}
											}								
										}
									}
								}
							}
						}
						else
						{
							errores.add("descripcion",new ActionMessage("errors.notEspecific","No se Actualizo los Campos Parametrizables."));
							return errores;								
						}
					}				
				}
			}
		}
				
		//Se recorren los campos iguales a tipo formula
		for(int i=0; i<camposArrayNuevo.size(); i++)
		{
			campo = camposArrayNuevo.get(i);	
			
			//Solo se toma los campos diferentes a formula
			if(campo.getCodigoTipo() == ConstantesCamposParametrizables.tipoCampoFormula)
			{			
				//Verifica si existe el Campo
				if(!campo.getCodigoPK().equals(""))
				{
					//Evalua si se cambiaron los campos Nombre, Tipo, Etiqueta Y Unidad se Replica el Campo			
					if(validarHayCambioCamposReplicacion(con, campo, camposArrayAnterior))
					{
						logger.info("---------------------------------------------------");
						logger.info("OPERACION 1. Actualiza el Campo Tipo Formula Parametrizable a Mostrar Modificacion en No (replicacion) ");
						
						//Actualizar indicador a No
						if(Plantillas.actualizarMostrarModCamposParametrizables(con,campo.getCodigoPK(),false, usuario))
						{
							//Insertamos el nuevo campo
							codigoPkCampoParam = Plantillas.insertarCamposParametrizablesSeccionConCodigo(
									con,
									campo.getCodigo(),
									campo.getNombre(),
									campo.getEtiqueta(), 
									campo.getCodigoTipo()+"",
									campo.getTamanio()+"",
									campo.getSigno(),
									campo.getCodigoUnidad()+"",
									campo.getValorPredeterminado()+"", 
									campo.getMaximo()+"", 
									campo.getMinimo()+"",
									campo.getDecimales()+"",
									campo.getColumnasOcupadas()+"",						 
									campo.getOrden()+"", 
									campo.isUnicoXFila(),
									campo.isRequerido(),
									actualizarCodigosEnFormula(campo.getFormulaCompleta(),cambioCodigoPkArray),									
									Plantillas.cargarTipoHtml(campo.getCodigoTipo(),campo.getTipoHtml()),						
									true,
									true,
									usuario,
									campo.getManejaImagen(),
									campo.getManejaImagen().equals(ConstantesBD.acronimoSi)?Utilidades.convertirAEntero(campo.getImagenBase().getCodigo()):ConstantesBD.codigoNuncaValido);	
							
							if(codigoPkCampoParam == ConstantesBD.codigoNuncaValido)
							{
								errores.add("descripcion",new ActionMessage("errors.notEspecific","El Consecutivo del Campo Parametrizable Replicado no es Valido."));
								return errores;
							}
							else
							{
								//Almacena la información del cambio de codigos
								cambioCodigoPkArray.add(new InfoDatosInt(codigoPkCampoParam,campo.getCodigoPK()));
								
								logger.info("---------------------------------------------------");
								logger.info("OPERACION 2. Inserta Un nuevo Registro en Plantillas Campos Secciones (replicacion)");
								
								codigoPkPlanCamSec = Plantillas.insertarPlantillasCamposSec(con,codigoPkPlantillaSec,codigoPkCampoParam+"");								
								if(codigoPkPlanCamSec == ConstantesBD.codigoNuncaValido)
								{
									errores.add("descripcion",new ActionMessage("errors.notEspecific","El Consecutivo para Plantilla Campos Sección Replicada no es Valida."));
									return errores;
								}							
							}
						}
						else
						{
							errores.add("descripcion",new ActionMessage("errors.notEspecific","Error al Actualizar el Campo como Mostrar Modificacion No"));
							return errores;
						}									
					}
					else
					{
						logger.info("---------------------------------------------------");
						logger.info("OPERACION 1. Actualiza la Información del Campo Tipo Formula Parametrizable");
						
						if(!Plantillas.actualizarCamposParametrizables(
								con,
								campo.getCodigoPK(),
								campo.getCodigo(),
								campo.getNombre(),
								campo.getEtiqueta(), 
								campo.getCodigoTipo()+"",
								campo.getTamanio()+"",
								campo.getSigno(),
								campo.getCodigoUnidad()+"",
								campo.getValorPredeterminado()+"", 
								campo.getMaximo()+"", 
								campo.getMinimo()+"",
								campo.getDecimales()+"",
								campo.getColumnasOcupadas()+"",						 
								campo.getOrden()+"", 
								campo.isUnicoXFila(),
								campo.isRequerido(),
								actualizarCodigosEnFormula(campo.getFormulaCompleta(),cambioCodigoPkArray),								
								Plantillas.cargarTipoHtml(campo.getCodigoTipo(),campo.getTipoHtml()),						
								true,							
								usuario,
								campo.getManejaImagen(),
								Utilidades.convertirAEntero(campo.getImagenAsociar())))						
						{
							errores.add("descripcion",new ActionMessage("errors.notEspecific","No se Actualizo los Campos Parametrizables."));
							return errores;								
						}
					}				
				}
			}
		}
		
		return errores;
	}	
	
	//*******************************************************************************************************
	
	/**
	 * Valida si existe cambio en los campos del campo para la generacion de la replicacion 
	 * @param Connection 
	 * @param DtoCampoParametrizable campo
	 * @param ArrayList<DtoCampoParametrizable> arrayOriginal
	 * */
	public static boolean validarHayCambioCamposReplicacion(
			Connection con,
			DtoCampoParametrizable campo, 
			ArrayList<DtoCampoParametrizable> arrayOriginal)
	{	
		for(int i=0; i<arrayOriginal.size(); i++)
		{
			if(campo.getCodigoPK().equals(arrayOriginal.get(i).getCodigoPK()))
			{
				if(!campo.getNombre().equals(arrayOriginal.get(i).getNombre()) || 
						campo.getCodigoTipo() != arrayOriginal.get(i).getCodigoTipo() ||
							!campo.getEtiqueta().equals(arrayOriginal.get(i).getEtiqueta()) || 
								campo.getCodigoUnidad() != arrayOriginal.get(i).getCodigoUnidad())
				{
					return true;
				}
			}
		}
		
		return false;
	}
	
	//*******************************************************************************************************
	
	
	/**
	 * Inserta informacion dentro de la tabla de Opciones de Campo Param
	 * @param Connection con 
	 * @param String codigoPkPlantillaSec
	 * @param String codigoPkCampoParam
	 * */
	public static int insertarPlantillasCamposSec(Connection con,String codigoPkPlantillaSec,String codigoPkCampoParam)
	{
		HashMap mapa = new HashMap();
		mapa.put("codigoPkPlantillaSec", codigoPkPlantillaSec);
		mapa.put("codigoPkCampoParam", codigoPkCampoParam);
		return getPLantillasDao().insertarPlantillasCamposSec(con, mapa);
	}
	
	//*******************************************************************************************************
	
	/**
	 * Actualiza los campos parametrizables
	 * @param Connection con
	 * @param String codigoPkCampoParam,
	 * @param String codigoCampo
	 * @param String nombreCampo
	 * @param String etiqueta
	 * @param String tipo
	 * @param String tamanio
	 * @param String signo
	 * @param String unidad
	 * @param String valorPredeterminado
	 * @param String maximo
	 * @param String minimo
	 * @param String decimales
	 * @param String columnasOcupadas
	 * @param String orden
	 * @param boolean unicoFila
	 * @param boolean requerido
	 * @param String formula
	 * @param String tipoHtml
	 * @param boolean mostrar
	 * @param UsuarioBasico usuario
	 * @return
	 */
	public static boolean actualizarCamposParametrizables(
			Connection con,
			String codigoPkCampoParam,
			String codigoCampo,
			String nombreCampo,
			String etiqueta,
			String tipo,
			String tamanio,
			String signo,
			String unidad,
			String valorPredeterminado,
			String maximo,
			String minimo,
			String decimales,
			String columnasOcupadas,
			String orden,
			boolean unicoFila,
			boolean requerido,
			String formula,
			String tipoHtml,
			boolean mostrar,
			UsuarioBasico usuario,
			String manejaImagen,
			int imagenAsociar)
	{
		HashMap mapa = new HashMap();
		mapa.put("codigo",codigoCampo);
		mapa.put("nombre",nombreCampo);
		mapa.put("etiqueta",etiqueta);
		mapa.put("tipo",tipo);
		mapa.put("tamanio",tamanio);
		mapa.put("signo",signo);
		mapa.put("unidad",unidad);
		mapa.put("valor_predeterminado",valorPredeterminado);
		mapa.put("maximo",maximo);
		mapa.put("minimo",minimo);
		mapa.put("decimales",decimales);
		mapa.put("columnas_ocupadas",columnasOcupadas);
		mapa.put("orden",orden);
		mapa.put("unico_fila",unicoFila?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
		mapa.put("requerido",requerido?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
		mapa.put("formula",formula);
		mapa.put("tipo_html",tipoHtml);
		mapa.put("mostrar",mostrar?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);			
		
		mapa.put("institucion",usuario.getCodigoInstitucion());
		mapa.put("usuarioModifica",usuario.getLoginUsuario());
		mapa.put("fechaModifica",UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
		mapa.put("horaModifica",UtilidadFecha.getHoraActual());		
		
		mapa.put("codigoPk",codigoPkCampoParam);
		
		
		//anexo 841
		mapa.put("maneja_imagen",manejaImagen);
		mapa.put("imagen_a_asociar",imagenAsociar);
		
		return getPLantillasDao().actualizarCamposParametrizables(con, mapa);
	}
	
	//*******************************************************************************************************	
	
	/**
	 * Actualiza las Opciones de los Campos Parametrizables
	 * @param Connection con
	 * @param String codigoPkOpcion
	 * @param String opcion
	 * @param String valor
	 * @param UsuarioBasico usuario
	 * @return
	 */
	public static boolean actualizarOpcionesCamposParam(Connection con,
			String codigoPkOpcion,
			String opcion,
			String valor,
			UsuarioBasico usuario,
			int convencion)
	{
		HashMap mapa = new HashMap();
		mapa.put("codigoPk",codigoPkOpcion);
		mapa.put("opcion",opcion);
		mapa.put("valor",valor);
		mapa.put("usuarioModifica",usuario.getLoginUsuario());
		mapa.put("fechaModifica",UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
		mapa.put("horaModifica",UtilidadFecha.getHoraActual());
		
		// Anexo 841
		mapa.put("convencion", convencion);

		return getPLantillasDao().actualizarOpcionesCamposParam(con,mapa);
	}
	
	//*******************************************************************************************************
	
	/**
	 * Actualiza mostrar modificacion de los campos
	 * @param Connection con
	 * @param String codigoPkCampoParam
	 * @param boolean mostrarModificacion
	 * @param UsuarioBasico usuario
	 * @return
	 */
	public static boolean actualizarMostrarModCamposParametrizables(Connection con,
			String codigoPkCampoParam,
			boolean mostrarModificacion,
			UsuarioBasico usuario)
	{
		HashMap mapa = new HashMap();
		mapa.put("codigoPk",codigoPkCampoParam);
		mapa.put("mostrarModificacion",mostrarModificacion?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
		mapa.put("usuarioModifica",usuario.getLoginUsuario());
		mapa.put("fechaModifica",UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
		mapa.put("horaModifica",UtilidadFecha.getHoraActual());		
		
		return getPLantillasDao().actualizarMostrarModificacionCamposParametrizables(con,mapa);
	}
	
	//*******************************************************************************************************
	
	/**
	 * Elimina las opciones de una campo 
	 * @param Connection con 
	 * @param String codigoOpcionPk
	 * */
	public static boolean eliminarOpcionesCamposSec(Connection con,String codigoOpcionPk)
	{
		HashMap mapa = new HashMap();
		mapa.put("codigoPk",codigoOpcionPk);
		return getPLantillasDao().eliminarOpcionesCamposSec(con,mapa);		
	}
	
	
	
	//********************************************************************************************
	//*************************** INICIO SECCIONES Y VALORES ASOCIADOS A OPCIONES****************//
	
	
	/**
	 * 
	 */
	public static boolean insertarSeccionesAsociadasOpciones(Connection con,
			String codigoPkOpcionCampo,
			String codigoPkPlantillaSeccion,
			boolean mostrarModificacion,
			UsuarioBasico usuario
			)
	{
		HashMap mapa = new HashMap();
		mapa.put("codigoPkOpcionCampo", codigoPkOpcionCampo);
		mapa.put("codigoPkPlantillaSeccion", codigoPkPlantillaSeccion);
		mapa.put("mostrarModificacion", mostrarModificacion?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
		mapa.put("usuarioModifica", usuario.getLoginUsuario());
		mapa.put("fechaModifica", UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
		mapa.put("horaModifica", UtilidadFecha.getHoraActual());
		
		return getPLantillasDao().insertarSeccionesAsociadasOpciones(con, mapa);
	}
	
	
	/**
	 * 
	 * @param con
	 * @param codigoPkPlantillaSeccionNuevo
	 * @param codigoPkPlantillaSeccionViejo
	 * @param mostrarModificacion
	 * @param usuario
	 * @return
	 */
	public static boolean insertarSeccionesAsociadasOpcionesNuevas(Connection con,
			String codigoPkPlantillaSeccionNuevo,
			String codigoPkPlantillaSeccionViejo,
			boolean mostrarModificacion,
			UsuarioBasico usuario
			)
	{
		HashMap mapa = new HashMap();
		mapa.put("codigoPkPlantillaSeccionNuevo", codigoPkPlantillaSeccionNuevo);
		mapa.put("codigoPkPlantillaSeccionViejo", codigoPkPlantillaSeccionViejo);
		mapa.put("mostrarModificacion", mostrarModificacion?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
		mapa.put("usuarioModifica", usuario.getLoginUsuario());
		mapa.put("fechaModifica", UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
		mapa.put("horaModifica", UtilidadFecha.getHoraActual());
		
		return getPLantillasDao().insertarSeccionesAsociadasOpcionesNuevas(con, mapa);
	}
	
	
	
	/**
	 * 
	 * @param con
	 * @param codigoPkDetSeccionesAsociadas
	 * @param mostrarModificacion
	 * @param usuario
	 * @return
	 */
	public static boolean actualizarSeccionesAsociadasOpciones(Connection con,
			String codigoPkDetSeccionesAsociadas,
			boolean mostrarModificacion,
			UsuarioBasico usuario)
	
	{
		HashMap mapa = new HashMap();
		mapa.put("codigoPkDetSecciones", codigoPkDetSeccionesAsociadas);
		mapa.put("mostrarModificacion", mostrarModificacion?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
		mapa.put("usuarioModifica", usuario.getLoginUsuario());
		mapa.put("fechaModifica", UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
		mapa.put("horaModifica", UtilidadFecha.getHoraActual());
		
		logger.info("valor del mapa actualizar >> "+mapa);
		return getPLantillasDao().actualizarSeccionesAsociadasOpciones(con, mapa);
	}
	
	
	/**
	 * 
	 * @param con
	 * @param codigoPkOpcionCampo
	 * @param valor
	 * @param mostrarModificacion
	 * @param usuario
	 * @return
	 */
	public static boolean insertarValoresAsociadosOpciones(Connection con,
			String codigoPkOpcionCampo,
			String valor,
			boolean mostrarModificacion,
			UsuarioBasico usuario
			)
	{
		HashMap mapa = new HashMap();
		mapa.put("codigoPkOpcionCampo", codigoPkOpcionCampo);
		mapa.put("valor", valor);
		mapa.put("mostrarModificacion", mostrarModificacion?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
		mapa.put("usuarioModifica", usuario.getLoginUsuario());
		mapa.put("fechaModifica", UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
		mapa.put("horaModfica", UtilidadFecha.getHoraActual());
		
		return getPLantillasDao().insertarValoresAsociadosOpciones(con, mapa);
		
	}
	
	
	/**
	 * 
	 * @param con
	 * @param codigoPkDetValoresAsociados
	 * @param mostrarModificacion
	 * @param usuario
	 * @return
	 */
	public static boolean actualizarValoresAsociadosOpciones(Connection con, 
			String codigoPkDetValoresAsociados,
			boolean mostrarModificacion,
			UsuarioBasico usuario)
	{
		HashMap mapa = new HashMap();
		mapa.put("codigoPkDetValores", codigoPkDetValoresAsociados);
		mapa.put("mostrarModificacion", mostrarModificacion?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
		mapa.put("usuarioModifica", usuario.getLoginUsuario());
		mapa.put("fechaModifica", UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
		mapa.put("horaModifica", UtilidadFecha.getHoraActual());
		
		return getPLantillasDao().actualizarValoresAsociadosOpciones(con, mapa);		
	}
	
	
	/**
	 * 
	 * @param con
	 * @param mapa
	 * @return
	 */
	public static boolean desasociarSeccionOculta(
			Connection con,
			String codigoPkPlanSeccion,
			boolean mostrarModificacion,
			UsuarioBasico usuario)
	{
		HashMap mapa = new HashMap();
		mapa.put("codigoPkPlanSeccion", codigoPkPlanSeccion);
		mapa.put("mostrarModificacion", mostrarModificacion?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
		mapa.put("usuarioModifica", usuario.getLoginUsuario());
		mapa.put("fechaModifica", UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
		mapa.put("horaModifica", UtilidadFecha.getHoraActual());
		
		logger.info("valor del mapa desasociar >> "+mapa);
		return getPLantillasDao().desasociarSeccionOculta(con, mapa);
	}
	
	/**
	 * Desasocia todas las secciones ocultas que se encuentren asociadas a un seccion por medio de sus campos
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public static boolean desasociarSeccionesOcultasDeSeccionN(
			Connection con,
			String codigoPkPlanSeccion,
			boolean mostrarModificacion,
			UsuarioBasico usuario)
	{
		HashMap mapa = new HashMap();
		mapa.put("codigoPkPlanSeccion", codigoPkPlanSeccion);
		mapa.put("mostrarModificacion", mostrarModificacion?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
		mapa.put("usuarioModifica", usuario.getLoginUsuario());
		mapa.put("fechaModifica", UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
		mapa.put("horaModifica", UtilidadFecha.getHoraActual());
		
		logger.info("valor del mapa desasociarSeccionesOcultasDeSeccionN >> "+mapa);
		return getPLantillasDao().desasociarSeccionesOcultasDeSeccionN(con, mapa);
	}
	
	//*****************************************************************************************
	//******************	FIN SECCIONES Y VALORES ASOCIADAS A OPCIONES ***********************///
	
	
	
	//*******************************************************************************************************
		
	/*************************************************************************************************/
	//*************************************************************************************************
	//Metodos Para Formularios de Plantillas Procedimientos********************************************
	/*************************************************************************************************/
	
	
	/**
	 * Método implementado para guardar los campos parametrizables en plantillas_res_proc
	 * @param Connection con
	 * @param DtoPlantilla plantillaDto
	 * @param String codigoIngreso
	 * @param String codigoPkRespuestaSolProc
	 * @param String fechaEjecucion
	 * @param String horaEjecucion
	 * @param UsuarioBasico usuario
	 */
	public static ResultadoBoolean guardarDatosRespuestaProcedimiento(
			Connection con,
			DtoPlantilla plantillaDto,
			String codigoIngreso,
			String codigoPkRespuestaSolProc,
			String fechaEjecucion,
			String horaEjecucion,
			UsuarioBasico usuario)
	{
		HashMap campos = new HashMap();
		campos.put("codigoIngreso",codigoIngreso);
		campos.put("codigoPkResSolProc",codigoPkRespuestaSolProc);
		campos.put("fecha",UtilidadFecha.conversionFormatoFechaABD(fechaEjecucion));
		campos.put("hora",horaEjecucion);				
		campos.put("usuarioModifica",usuario.getLoginUsuario());
		campos.put("fechaModifica",UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
		campos.put("horaModifica",UtilidadFecha.getHoraActual());
				
		return getPLantillasDao().guardarDatosRespuestaProcedimiento(con,plantillaDto,campos);
	}
	
	
	/**
	 * Consulta los textos predeterminados para el campo resultado de la respuesta de procedimientos
	 * @param Connection con
	 * @param int institucion
	 * */
	public static HashMap consultarTextosRespuestaProc(Connection con,int institucion,String servicio)
	{
		HashMap parametros = new HashMap();
		parametros.put("activo",ConstantesBD.acronimoSi);
		parametros.put("institucion",institucion);
		parametros.put("codigoServicio",servicio);
		
		return getPLantillasDao().consultarTextosRespuestaProc(con,parametros);
	}	
	
	/*************************************************************************************************/
	//*************************************************************************************************
	//Metodos Para Plantillas Evolucion****************************************************************
	/*************************************************************************************************/
	
	
	/**
	 * Método implementado para guardar los campos parametrizables en plantillas_evolucion.
	 * @param Connection con
	 * @param DtoPlantilla plantillaDto
	 * @param String codigoIngreso
	 * @param String codigoPkEvolucion
	 * @param String fechaEjecucion
	 * @param String horaEjecucion
	 * @param UsuarioBasico usuario
	 */
	public static ResultadoBoolean guardarDatosEvolucion(
			Connection con,
			DtoPlantilla plantillaDto,
			String codigoIngreso,
			String codigoPkEvolucion,
			String fechaEjecucion,
			String horaEjecucion,
			UsuarioBasico usuario)
	{
		HashMap campos = new HashMap();
		campos.put("codigoIngreso",codigoIngreso);
		campos.put("codigoPkEvolucion",codigoPkEvolucion);
		campos.put("fecha",UtilidadFecha.conversionFormatoFechaABD(fechaEjecucion));
		campos.put("hora",horaEjecucion);				
		campos.put("usuarioModifica",usuario.getLoginUsuario());
		campos.put("fechaModifica",UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
		campos.put("horaModifica",UtilidadFecha.getHoraActual());
				
		return getPLantillasDao().guardarDatosEvolucion(con,plantillaDto,campos);
	}
	
	
	/*************************************************************************************************/
	//*************************************************************************************************
	//Metodos Utilitarios******************************************************************************
	/*************************************************************************************************/
	
	/**
	 * Método para obtener el código de una plantilla
	 */
	public static int obtenerCodigoPlantillaXIngreso(Connection con,int codigoIngreso,int codigoPaciente,int numeroSolicitud)
	{
		HashMap campos = new HashMap();
		campos.put("codigoIngreso",codigoIngreso);
		campos.put("codigoPaciente",codigoPaciente);
		campos.put("numeroSolicitud", numeroSolicitud);
		
		return getPLantillasDao().obtenerCodigoPlantillaXIngreso(con, campos);
	}
	
	/**
	 * Método para obtener el código de una plantilla
	 */
	public static int obtenerCodigoPlantillaXEvolucion(Connection con,int evolucion)
	{
		return getPLantillasDao().obtenerCodigoPlantillaXEvolucion(con, evolucion);
	}
	
	//*******************************************************************************************************
	
	/**
	 * Metodo para campturar los codigos de escalas insertadas
	 * @param DtoPlantilla plantilla
	 * */
	public static String obtenerCodigosInsertadosEscalas(DtoPlantilla plantilla)
	{
		String codigos = "";
		int numRegistros = 0 ;
		
		for(int i = 0; i < plantilla.getSeccionesFijas().size(); i++)
		{
			//Recorre cada elemento tipo escala de la seccion
			for(int j = 0; j < plantilla.getSeccionesFijasPos(i).getElementos().size(); j++)
			{
				if(plantilla.getSeccionesFijasPos(i).getElementos().get(j).isEscala())
				{
					if(!plantilla.getSeccionesFijasPos(i).getElementos().get(j).getCodigoPK().equals(""))
						codigos+=plantilla.getSeccionesFijasPos(i).getElementos().get(j).getCodigoPK()+",";
				}
			}
		}
		
		return codigos;
	}
	
	//*******************************************************************************************************
	
	/**
	 * Metodo para campturar los codigos de Componentes insertadas
	 * @param DtoPlantilla plantilla
	 * */
	public static String obtenerCodigosInsertadosComponentes(DtoPlantilla plantilla)
	{
		String codigos = "";
		int numRegistros = 0 ;
		
		for(int i = 0; i < plantilla.getSeccionesFijas().size(); i++)
		{
			//Recorre cada elemento tipo Componente de la seccion
			for(int j = 0; j < plantilla.getSeccionesFijasPos(i).getElementos().size(); j++)
			{
				if(plantilla.getSeccionesFijasPos(i).getElementos().get(j).isComponente())
				{
					if(!plantilla.getSeccionesFijasPos(i).getElementos().get(j).getCodigoPK().equals(""))
						codigos+=plantilla.getSeccionesFijasPos(i).getElementos().get(j).getCodigoPK()+",";
				}
			}
		}						
		
		//logger.info("valor de codigos >> "+codigos);
		return codigos;
	}
	
	//*******************************************************************************************************
	/**
	 * Método implementado para realizar las validaciones FORM de los campos de la plantilla
	 */
	public static ActionErrors validacionCamposPlantilla(DtoPlantilla plantilla,ActionErrors errores)
	{
		
		//*******************ITERACION DE LAS SECCIONES FIJAS*************************************
		for(DtoSeccionFija seccionFija:plantilla.getSeccionesFijas())
		{
			if(seccionFija.isVisible())
			{
				//Se itera cada elemento de la seccion fija
				for(DtoElementoParam elemento:seccionFija.getElementos())
				{
					if(elemento.isVisible())
					{
					
						//Si el tipo elemento es seccion
						if(elemento.isSeccion())
						{
							DtoSeccionParametrizable seccion = (DtoSeccionParametrizable)elemento;
							//Se verifica que la seccion NO sea con indicativo de restriccion por valor campo
							// o que sea SI el indicativo y que se encuentre activa
							if(!UtilidadTexto.getBoolean(seccion.getIndicativoRestriccionValCamp())
								||
								(UtilidadTexto.getBoolean(seccion.getIndicativoRestriccionValCamp())&&seccion.estaSeccionValorOpcionActiva(plantilla.getListadoSeccionesValorActivas()))
								)
							{
								errores = validarCamposSeccion(seccion,errores,UtilidadTexto.getBoolean(seccion.getIndicativoRestriccionValCamp())?seccion.getDescripcion():seccionFija.getNombreSeccion(),false,"");
							}
						}
						//Si el tipo elemento es escala
						else if(elemento.isEscala())
						{
							errores = validarCamposEscala((DtoEscala)elemento,errores,seccionFija.getNombreSeccion(),false,"");
							logger.info("errores "+errores);
						}
						//Si el tipo elemento es componente
						else if(elemento.isComponente())
						{
							DtoComponente componente = (DtoComponente)elemento;
							//logger.info("VALIDACION COMPONENTE "+componente.getDescripcion()+": "+errores.size());
							for(DtoElementoParam elementoComponente:componente.getElementos())
							{
								if(elementoComponente.isVisible())
								{
									//Si elemento del componente es seccion
									if(elementoComponente.isSeccion())
									{
										DtoSeccionParametrizable seccion = (DtoSeccionParametrizable)elementoComponente;
										//Se verifica que la seccion NO sea con indicativo de restriccion por valor campo
										// o que sea SI el indicativo y que se encuentre activa
										if(!UtilidadTexto.getBoolean(seccion.getIndicativoRestriccionValCamp())
											||
											(UtilidadTexto.getBoolean(seccion.getIndicativoRestriccionValCamp())&&seccion.estaSeccionValorOpcionActiva(plantilla.getListadoSeccionesValorActivas()))
											)
											errores = validarCamposSeccion(seccion,errores,UtilidadTexto.getBoolean(seccion.getIndicativoRestriccionValCamp())?seccion.getDescripcion():seccionFija.getNombreSeccion(),true,componente.getDescripcion());
									}
									//Si elemento del componente es escala
									else if(elementoComponente.isEscala())
									{
										errores = validarCamposEscala((DtoEscala)elementoComponente,errores,seccionFija.getNombreSeccion(),true,componente.getDescripcion());
									}
								}
							}
						}
					}
				}
			}
		}
		//*****************************************************************************************
		//****************ITERACIÓN DE LAS SECCIONES VALOR OPCION**********************************
		/**for(DtoElementoParam elemento:plantilla.getSeccionesValor())
		{
			DtoSeccionParametrizable seccion = (DtoSeccionParametrizable)elemento;
			//Se verifica si está la seccion valor activa para validar sus campos
			if(seccion.estaSeccionValorOpcionActiva(plantilla.getListadoSeccionesValorActivas()))
				errores = validarCamposSeccion(seccion, errores, "Sección dinámica", false, "");
		}**/
		//*****************************************************************************************
		
		return errores;
	}
	
	/**
	 * Método implementado para validar los campos/secciones/ de la escala
	 * @param escala
	 * @param errores
	 * @param nombreSeccion
	 * @return
	 */
	public static ActionErrors validarCamposEscala(DtoEscala escala, ActionErrors errores, String nombreSeccionFija,boolean vieneDeComponente,String nombreComponente) 
	{
		
		//Se revisan las observaciones de la escala
		if(escala.isObservacionesRequeridas()&&escala.getObservaciones().equals(""))
		{
			errores.add("", new ActionMessage("errors.paciente.requeridoIngresoDe","de las observaciones de la escala "+escala.getDescripcion() +(vieneDeComponente?" del componente "+nombreComponente:"")+(nombreSeccionFija.equals("")?"":" ("+nombreSeccionFija+")")));
		}
		
		//logger.info("\n\n\nVALIDACION ESCALA "+escala.getDescripcion()+" numero de secciones >> "+escala.getSecciones().size());
		for(DtoSeccionParametrizable seccion:escala.getSecciones())
			if(seccion.isVisible())
			{
				//logger.info("numero campos de la seccion "+seccion.getDescripcion()+">> "+seccion.getCampos().size());
				for(DtoCampoParametrizable campo:seccion.getCampos())
					if(campo.isMostrar())
					{
						logger.info("VALIDACION ESCALA "+escala.getDescripcion()+" campo >> "+campo.getNombre()+" >> requerido >obtenerCodigoPlantilla> "+campo.isRequerido()+" >> "+campo.getValor()+" >> observaciones >> ");	
						if(campo.isRequerido()&&campo.getValor().equals(""))
						{
							errores.add("", new ActionMessage("errors.required","El campo "+campo.getEtiqueta()+" en la escala "+escala.getDescripcion() + (vieneDeComponente?" del componente "+nombreComponente:"")  +(nombreSeccionFija.equals("")?"":" ("+nombreSeccionFija+")")));
						}
						else if(!campo.getValor().trim().equals(""))
						{
							//Se verifica que sea numérico
							try{
								double numerico=Double.parseDouble(campo.getValor());
								//Se verifica su rango
								if(numerico<campo.getMinimo()||numerico>campo.getMaximo())
								{
									errores.add("", new ActionMessage("errors.range","El campo "+campo.getEtiqueta()+" en la escala "+escala.getDescripcion()+" y sección "+seccion.getDescripcion()+(vieneDeComponente?" del componente "+nombreComponente:"")+(nombreSeccionFija.equals("")?"":" ("+nombreSeccionFija+")"),campo.getMinimo(),campo.getMaximo()));
								}
							}
							catch (NumberFormatException e) {
								errores.add("", new ActionMessage("errors.float","El campo "+campo.getEtiqueta()+" en la escala "+escala.getDescripcion()+" y sección "+seccion.getDescripcion()+(vieneDeComponente?" del componente "+nombreComponente:"")+(nombreSeccionFija.equals("")?"":" ("+nombreSeccionFija+")")));
							}
						}
						
						if(campo.isObservacionesRequeridas()&&campo.getObservaciones().equals(""))
						{
							errores.add("", new ActionMessage("errors.paciente.requeridoIngresoDe","de las observaciones del campo "+campo.getEtiqueta()+" en la escala "+escala.getDescripcion() +" y la sección "+seccion.getDescripcion()+(vieneDeComponente?" del componente "+nombreComponente:"")+(nombreSeccionFija.equals("")?"":" ("+nombreSeccionFija+")")));
						}
					}
			}
		
		//Si la escala había calculado un total y no se encontró un rango de factor de predicción
		if(escala.getTotalEscala()>0&&escala.getCodigoFactorPrediccion().trim().equals(""))
		{
			errores.add("", new ActionMessage("errors.range","El total de la escala "+escala.getDescripcion()+(vieneDeComponente?" del componente "+nombreComponente:"")+(nombreSeccionFija.equals("")?"":" ("+nombreSeccionFija+")"),"los rangos del factor de predicción: "+escala.getRangoMinimoFactorPrediccion(),escala.getRangoMaximoFactorPrediccion()+""));
		}
		
		return errores;
	}

	/**
	 * Método implementado para validar los campos de una seccion
	 * @param parametrizable
	 * @param errores
	 * @param nombreSeccionFija 
	 * @param esPrincipal 
	 * @return
	 */
	private static ActionErrors validarCamposSeccion(DtoSeccionParametrizable seccion, ActionErrors errores, String nombreSeccionFija, boolean vieneDeComponente,String nombreComponente) 
	{
		String mensaje = "";
		boolean tempo = false;
		
		if(UtilidadTexto.getBoolean(seccion.getIndicativoRestriccionValCamp()))
			mensaje = "de la sección dinámica "+nombreSeccionFija;
		else
			mensaje = (seccion.getDescripcion().equals("")?"":"de la sección "+seccion.getDescripcion())+(vieneDeComponente?" del componente "+nombreComponente:"")+(nombreSeccionFija.equals("")?"":" ("+nombreSeccionFija+")");
		
		//**********SE ITERAN LOS CAMPOS DE LA SECCION**************************
		for(DtoCampoParametrizable campo:seccion.getCampos())
			if(campo.isMostrar())
			{
				/****************VALIDACIONES CAMPO SELECT/RADIO/TEXTAREA/ARCHIVO*********************************************/
				if(
					campo.getTipoHtml().equals(ConstantesCamposParametrizables.campoTipoSelect)||
					campo.getTipoHtml().equals(ConstantesCamposParametrizables.campoTipoRadio)||
					campo.getTipoHtml().equals(ConstantesCamposParametrizables.campoTipoTextArea)||
					campo.getTipoHtml().equals(ConstantesCamposParametrizables.campoTipoARCH)
					)
				{
					//Se verifican los datos requeridos
					if(campo.isRequerido()&&campo.getValor().equals(""))
					{
						//Si el campo no tenía etiqueta el mensaje debe ser diferente
						if(!campo.getEtiqueta().trim().equals(""))
							errores.add("", new ActionMessage("errors.required","el campo "+campo.getEtiqueta()+" "+mensaje));
						else
						{
							errores.add("", new ActionMessage("errors.notEspecific","falta un campo requerido "+mensaje));
							campo.setResaltar(true);
						}
					}
					else
					{
						campo.setResaltar(false);
					}
					
					//Si se superan los caracteres permitidos
					if(campo.getValor().length()>4000)
					{
						//Si el campo no tenía etiqueta el mensaje debe ser diferente
						if(!campo.getEtiqueta().trim().equals(""))
							errores.add("", new ActionMessage("errores.notEspecific","el campo "+campo.getEtiqueta()+" "+mensaje+" no puede superar 4000 caracteres"));
						else
						{
							errores.add("", new ActionMessage("errors.notEspecific","Existe algún campo "+mensaje+" que ha superado el tamaño límite de caracteres (4000)"));
							campo.setResaltar(true);
						}
					}					
				}
				/*************VALIDACIONES CAMPO CHECKBOX**********************************************/
				if(campo.getTipoHtml().equals(ConstantesCamposParametrizables.campoTipoCheckBox))
				{
					//Se verifica si es requerido que se haya llenado
					if(campo.isRequerido()&&!campo.fueLlenadoCheckBox())
					{
						//Si el campo no tenía etiqueta el mensaje debe ser diferente
						if(!campo.getEtiqueta().trim().equals(""))
							errores.add("", new ActionMessage("errors.required","el campo "+campo.getEtiqueta()+" "+mensaje));
						else
						{
							errores.add("", new ActionMessage("errors.notEspecific","falta un campo requerido "+mensaje));
							campo.setResaltar(true);
						}
					}
					else
					{
						campo.setResaltar(false);
					}
				}				
				/*************VALIDACION CAMPO TEXT*****************************************************/
				if(campo.getTipoHtml().equals(ConstantesCamposParametrizables.campoTipoText))
				{
					campo.setValorPredeterminado("");
					campo.setResaltar(false);
					
					//Se verifican los datos requeridos
					if(campo.isRequerido()&&campo.getValor().equals(""))
					{						
						//Si el campo no tenía etiqueta el mensaje debe ser diferente
						if(!campo.getEtiqueta().trim().equals(""))
							errores.add("", new ActionMessage("errors.required","el campo "+campo.getEtiqueta()+" "+mensaje));
						else
						{
							errores.add("", new ActionMessage("errors.notEspecific","falta un campo requerido "+mensaje));
							campo.setResaltar(true);
						}
					}				
					//Si sí se ingresó información entonces se hacen validaciones dependiendo del tipo de campo
					else if(!campo.getValor().trim().equals(""))
					{	
						tempo = false;
						switch(campo.getCodigoTipo())
						{
							//CAMPO FECHA
							case ConstantesCamposParametrizables.tipoCampoFecha:
								if(!UtilidadFecha.validarFecha(campo.getValor()))
								{
									errores.add("", new ActionMessage("errors.formatoFechaInvalido",(!campo.getEtiqueta().trim().equals("")?"en el campo "+campo.getEtiqueta():"")+" "+mensaje));
									tempo = true;
								}
							break;
							//CAMPO HORA
							case ConstantesCamposParametrizables.tipoCampoHora:
								if(!UtilidadFecha.validacionHora(campo.getValor()).puedoSeguir)
								{
									errores.add("", new ActionMessage("errors.formatoHoraInvalido",(!campo.getEtiqueta().trim().equals("")?"en el campo "+campo.getEtiqueta():"")+" "+mensaje));
									tempo = true;
								}
							break;
							//CAMPO NUMERICO ENTERO
							case ConstantesCamposParametrizables.tipoCampoNumericoEntero:
								//Se verifica si el campo es entero
								if(Utilidades.convertirAEntero(campo.getValor())==ConstantesBD.codigoNuncaValido)
								{
									errores.add("", new ActionMessage("errors.integer",(!campo.getEtiqueta().trim().equals("")?"El campo "+campo.getEtiqueta():"Un campo")+" "+mensaje));
									tempo = true;
								}
								else 
								{
									int numeroEntero = Utilidades.convertirAEntero(campo.getValor());
									
									if(campo.getSigno().equals(ConstantesIntegridadDominio.acronimoPositivo)&&numeroEntero<0)
									{
										errores.add("", new ActionMessage("errors.integerMayorQue",(!campo.getEtiqueta().trim().equals("")?"El campo "+campo.getEtiqueta():"Un campo")+" "+mensaje,"0"));
										tempo = true;
									}
									else if(campo.getSigno().equals(ConstantesIntegridadDominio.acronimoNegativo)&&numeroEntero>=0)
									{
										errores.add("", new ActionMessage("errors.integerMenorQue",(!campo.getEtiqueta().trim().equals("")?"El campo "+campo.getEtiqueta():"Un campo")+" "+mensaje,"0"));
										tempo = true;
									}
									else if(numeroEntero<campo.getMinimo()||numeroEntero>campo.getMaximo())
									{
										errores.add("", new ActionMessage("errors.range",(!campo.getEtiqueta().trim().equals("")?"El campo "+campo.getEtiqueta():"Un campo")+" "+mensaje,campo.getMinimo(),campo.getMaximo()));
										tempo = true;
									}
								}
							break;
							//CAMPO NUMERICO DECIMAL
							case ConstantesCamposParametrizables.tipoCampoNumericoDecimal:
								//Se verifica si el campo es float
								if(Utilidades.convertirADouble(campo.getValor())==ConstantesBD.codigoNuncaValido)
								{
									errores.add("", new ActionMessage("errors.float",(!campo.getEtiqueta().trim().equals("")?"El campo "+campo.getEtiqueta():"Un campo")+" "+mensaje));
									tempo = true;
								}
								else 
								{
									double numeroFloat = Utilidades.convertirADouble(campo.getValor());
									
									if(campo.getSigno().equals(ConstantesIntegridadDominio.acronimoPositivo)&&numeroFloat<0)
									{
										errores.add("", new ActionMessage("errors.floatMayorQue",(!campo.getEtiqueta().trim().equals("")?"El campo "+campo.getEtiqueta():"Un campo")+" "+mensaje,"0"));
										tempo = true;
									}
									else if(campo.getSigno().equals(ConstantesIntegridadDominio.acronimoNegativo)&&numeroFloat>=0)
									{
										errores.add("", new ActionMessage("errors.floatMenorQue",(!campo.getEtiqueta().trim().equals("")?"El campo "+campo.getEtiqueta():"Un campo")+" "+mensaje,"0"));
										tempo = true;
									}
									else if(numeroFloat<campo.getMinimo()||numeroFloat>campo.getMaximo())
									{
										errores.add("", new ActionMessage("errors.range",(!campo.getEtiqueta().trim().equals("")?"El campo "+campo.getEtiqueta():"Un campo")+" "+mensaje,campo.getMinimo(),campo.getMaximo()));
										tempo = true;
									}
									
									//Se verifica si el número tiene parte decimal
									if(campo.getValor().indexOf('.')!=ConstantesBD.codigoNuncaValido)
									{
										String[] vector = campo.getValor().split("[.]");
										if(vector.length==2)
											if(vector[1].length()>campo.getDecimales())
											{
												errores.add("", new ActionMessage("errors.range",(!campo.getEtiqueta().trim().equals("")?"La parte decimal del campo "+campo.getEtiqueta():"La parte de un campo")+" "+mensaje,"0",campo.getDecimales()));
												tempo = true;
											}
									}
								}
									
							break;
						}
						
						if(tempo && campo.getEtiqueta().trim().equals(""))
							campo.setResaltar(true);						
					}
				}
				/******************************************************************************************/
			}
		//**********************FIN ITERACION CAMPOS SECCION************************************************
		
		//**************ITERACION DE LAS SUBSECCIONES***************************************************
		//De manera recursiva se acceden a los campos de las subsecciones
		for(DtoSeccionParametrizable subseccion:seccion.getSecciones())
			if(subseccion.isVisible())
				errores = validarCamposSeccion(subseccion, errores, nombreSeccionFija,vieneDeComponente,nombreComponente);
		//***********************************************************************************************
		
		
		return errores;
	}
	//********************************************************************************************************

	/**
	 * Método para obtener un listado de las escalas del ingreso
	 */
	public static ArrayList<DtoEscala> obtenerEscalasIngreso(Connection con,String idIngreso,String fechaInicial,String horaInicial,String fechaFinal,String horaFinal)
	{
		HashMap campos = new HashMap();
		campos.put("fechaInicial",fechaInicial);
		campos.put("horaInicial",horaInicial);
		campos.put("fechaFinal",fechaFinal);
		campos.put("horaFinal",horaFinal);
		campos.put("idIngreso",idIngreso);
		return getPLantillasDao().obtenerEscalasIngreso(con, campos);
	}
	
	//********************************************************************************************************
	
	/**
	 * Metodo que ordena una ArrayList de Campos por el orden
	 * @param ArrayList<DtoCampoParametrizable> array
	 * */
	public static ArrayList<DtoCampoParametrizable> ordenarArrayCamposPorOrden(ArrayList<DtoCampoParametrizable> array)
	{		
		int pos = 0;
		ArrayList<DtoCampoParametrizable> respuestaArray = new ArrayList<DtoCampoParametrizable>();
			
		logger.info("OPERACION. Ordenamiento Campos");
		try
		{
			//Inicializa el array de respuesta
			for(int i = 0; i < array.size(); i++)
				respuestaArray.add(new DtoCampoParametrizable());
		
			//Almacena la informacion
			for(int i = 0; i < array.size(); i++)						
			{
				pos = (array.get(i).getOrden()-1);
				logger.info("pos >> "+pos+" >> codigo "+array.get(i).getCodigo());
				respuestaArray.set(pos,array.get(i));
			}
			
			return respuestaArray;
		}
		catch (Exception e) {
			logger.info("error al realizar el ordenamiento de campos en plantillas, se retorna el array original >> "+e.getMessage());
			return array;
		}
	}
	
	//********************************************************************************************************
	/**
	 * Evalua el uso de un campo por la formula
	 * @param DtoCampoParametrizable campoEvaluar
	 * @param ArrayList<DtoCampoParametrizable> arrayCampos
	 * */
	public static DtoCampoParametrizable validarUsoCampoPorFormula(DtoCampoParametrizable campoEvaluar,ArrayList<DtoCampoParametrizable> arrayCampos)
	{	
		//Inicializa la informacion del campo 
		campoEvaluar.setUsadoFormula("","", false);
		DtoCampoParametrizable campoParam = new DtoCampoParametrizable();
		String [] formulaArray ;
		 
		logger.info("OPERACION. Validar Campo Usado por Formula");
		try
		{			
		
		 for(int ca=0; ca<arrayCampos.size(); ca++)
		 {
			campoParam = arrayCampos.get(ca);				
			 
			if(!campoParam.getFormulaCompleta().equals(""))
			{
				formulaArray = campoParam.getFormulaCompleta().split(ConstantesBD.separadorSplit);
			 
				for(int i=0; i<formulaArray.length; i++)
				{
					if(tipoCaracterFormula(formulaArray[i]).equals("campos"))
					{	
						if(formulaArray[i].replace("__","").toString().equals(campoEvaluar.getCodigoPK()))
						{
							campoEvaluar.setUsadoFormula(campoParam.getCodigoPK(),campoParam.getCodigo()+" - "+campoParam.getNombre(),true);
							return campoEvaluar;
						}				
					}
				}
			}
	 	}
		 
		 return campoEvaluar;
		 
		}
		catch (Exception e) {
			logger.info("Error al validar el campo y saber si esta siendo usado por una formula >> "+e.getMessage());
			return campoEvaluar;
		}
	}
	
	//********************************************************************************************************
	
	/**
	  * Indica el tipo de caracter que compone la formula
	  * @param String caracter
	  * */
	 public static String tipoCaracterFormula(String caracter)
	 {		 
		 if(caracter.trim().equals("-") || 
			 caracter.trim().equals("+") || 
			 	caracter.trim().equals("/") || 
			 		caracter.trim().equals("*") || 
			 			caracter.trim().equals("^"))
			 return "operadores";
		 else if(caracter.trim().equals("(") || 
				 caracter.trim().equals(")"))
			 return "signos";		 
		 else if(caracter.startsWith("__"+ConstantesCamposParametrizables.edadPacienteAnios+"__") ||
				 	caracter.startsWith("__"+ConstantesCamposParametrizables.edadPacienteMeses+"__") || 
				 		caracter.startsWith("__"+ConstantesCamposParametrizables.edadPacienteDias+"__"))
			 return "otrasconstantes";
		 else if(caracter.startsWith("__") && 
				 caracter.toString().endsWith("__"))
			 return "campos";
		 else
			 return "constantes";			 
		
	 }
	 
	//*******************************************************************************************************	
	
	/**
	 * Inserta parametrizaciones plantilla servicios/diagnosticos
	 * @param Connection con
	 * @param int institucion
	 * @param int codigoPkPlantilla
	 * @param int codigoServicio (CodigoNuncaValido = null)
	 * @param int codigoDiag (CodigoNuncaValido = null)
	 * */
	public static  int insertarFormularioRespServ(
			Connection con,
			int institucion,
			int codigoPkPlantilla,
			int codigoServicio,
			int codigoDiag)
	{
		HashMap parametros = new HashMap();
		parametros.put("institucion",institucion);
		parametros.put("codigoPkPlantilla",codigoPkPlantilla);
		parametros.put("codigoServicio",codigoServicio);
		parametros.put("codigoDiag",codigoDiag);		
		
		return getPLantillasDao().insertarFormularioRespServ(con, parametros);
	}	
	//*******************************************************************************************************
	
	
	/**
	 * Modifica parametrizaciones plantilla servicios/diagnosticos
	 * @param Connection con
	 * @param int codigoPk
	 * @param int codigoPkPlantilla
	 * @param int codigoServicio (CodigoNuncaValido = null)
	 * @param int codigoDiag (CodigoNuncaValido = null)
	 * @param String usuarioModifica
	 * */
	public static boolean modificarFormularioRespServ(
			Connection con,
			int codigoPk,
			int codigoPkPlantilla,
			int codigoServicio,
			int codigoDiag,
			String usuarioModifica)
	{
		HashMap parametros = new HashMap();
		parametros.put("codigoPkPlantilla",codigoPkPlantilla);
		parametros.put("codigoServicio",codigoServicio);
		parametros.put("codigoDiag",codigoDiag);
		parametros.put("usuarioModifica",usuarioModifica);
		parametros.put("codigoPk",codigoPk);		
		
		return getPLantillasDao().modificarFormularioRespServ(con, parametros);
	}
	
	//*******************************************************************************************************
		
	/**
	 * Consulta la informacion de las plantillas Servicios Diagnosticos
	 * @param Connection con
	 * @param int codigoPk (CodigoNuncaValido = N/A)
	 * @param int codigoPkPlantilla  (CodigoNuncaValido = N/A)
	 * @param int institucion
	 * */
	public static ArrayList<DtoPlantillaServDiag> consultarPlantillasServDiag(Connection con,int codigoPk,int codigoPkPlantilla, int institucion)
	{
		HashMap parametros = new HashMap();
		parametros.put("codigoPkPlantilla",codigoPkPlantilla);
		parametros.put("codigoPk",codigoPk);
		
		int tarifario= Utilidades.convertirAEntero(ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(institucion));
		if(tarifario<0)
			tarifario=ConstantesBD.codigoTarifarioCups;
		parametros.put("tarifario", tarifario);
		
		parametros.put("institucion",institucion);				
		
		return getPLantillasDao().consultarPlantillasServDiag(con, parametros);		
	}
			 
	//********************************************************************************************************
	
	/**
	 * Consulta los diagnosticos parametrizados necesarios para la parametrizacion de plantillas servicios/diag
	 * @param Connection con
	 * @param int institucion
	 * */
	public static ArrayList consultarDiagnosticosPlantillas(Connection con,int institucion)
	{
		HashMap parametros = new HashMap();
		parametros.put("institucion",institucion);
		
		return getPLantillasDao().consultarDiagnosticosPlantillas(con, parametros);		
	}
	
	//********************************************************************************************************
	
	/**
	 * Metodo que verifica que exista palntilla ingreso dado el numerosolicitud
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static boolean existePlantillaIngreso(Connection con, int numeroSolicitud)
	{
		return getPLantillasDao().existePlantillaIngreso(con, numeroSolicitud);
	}
	
	//********************************************************************************************************
	
	/**
	 * Metodo que verifica que exista plantilla evolucion dado la evolucion
	 * @param con
	 * @param evolucion
	 * @return
	 */
	public static boolean existePlantillaEvolucion(Connection con, int evolucion)
	{
		return getPLantillasDao().existePlantillaEvolucion(con, evolucion);
	}
	
	/**
	 * 
	 * @param con
	 * @param plantillaBase
	 * @param centroCosto
	 * @param sexo
	 * @param especialidad
	 * @return
	 */
	public HashMap<String, Object> consultarCamposExitentes(Connection con, int plantillaBase, int centroCosto, int sexo, int especialidad)
	{
		return getPLantillasDao().consultarCamposExitentes(con, plantillaBase, centroCosto, sexo, especialidad);
	}
	
	/**
	 * 
	 * @param con
	 * @param plantillaBase
	 * @param centroCosto
	 * @param sexo
	 * @param especialidad
	 * @return
	 */
	public HashMap<String, Object> consultarSeccionesExistentes(Connection con, int plantillaBase, int centroCosto, int sexo, int especialidad)
	{
		return getPLantillasDao().consultarSeccionesExistentes(con, plantillaBase, centroCosto, sexo, especialidad);
	}
	
	/**
	 * 
	 * @param con
	 * @param plantillasBase
	 * @param centroCosto
	 * @param sexo
	 * @param especialidad
	 * @return
	 */
	public HashMap<String, Object> consultarEscalasExitentesComponentes(Connection con, int plantillaBase, int centroCosto, int sexo, int especialidad)
	{
		return getPLantillasDao().consultarEscalasExitentesComponentes(con, plantillaBase, centroCosto, sexo, especialidad);
	}
	
	/**
	 * 
	 * @param codigoPkEscala
	 * @param codigoPkPerfilNED_OPCIONAL (SI ES MAYOR 0 EXISTE, DE LO CONTRARIO LA ESCALA VACIA)
	 * @return
	 */
	public static DtoEscala cargarEscalaPerfilNed(BigDecimal codigoPkEscala, double codigoPkPerfilNED_OPCIONAL)
	{
		return getPLantillasDao().cargarEscalaPerfilNed(codigoPkEscala, codigoPkPerfilNED_OPCIONAL);
	}

	
	/**
	 * Metodo para obtener el codigo de la plantilla paciente de Odontologia
	 * @param con
	 * @param codigo
	 * @return
	 */
	public static int obtenerCodigoPlantillaXPacienteOdont(Connection con,int codigo) {
		
		return getPLantillasDao().obtenerCodigoPlantillaXPacienteOdont(con, codigo);
	}

	
	/**
	 * Metodo para guardar los campos parametrizables de un paciente Odongologia que no tiene una plantilla previamente Asignada
	 * @param con
	 * @param plantilla
	 * @param codigoPersona
	 * @param tipoFuncionalidad
	 * @return
	 */
	public static ResultadoBoolean guardarCamposParametrizablesPacOdontologia(Connection con, DtoPlantilla plantilla, int codigoPersona,
			String tipoFuncionalidad, String loginUsuario, int institucion) {
		
		HashMap campos = new HashMap();
		campos.put("codigoPaciente", codigoPersona);
		campos.put("tipoFuncionalidad", tipoFuncionalidad);
		campos.put("loginUsuario", loginUsuario);
		campos.put("institucion", institucion);
		
		return getPLantillasDao().guardarCamposParametrizablesPacOdontologia(con, plantilla,campos) ;
	}

	/**
	 * Metodo qeu actualiza los campos parametrizables de un Paciente de Odontologia que ya se le asigno una Plantilla
	 * @param con
	 * @param plantilla
	 * @param codigoPersona
	 * @param codPlantillaPaciente
	 * @param tipoFuncionalidad
	 * @param loginUsuario
	 * @return
	 */
	public static ResultadoBoolean modificarCamposParametrizablesPacOdontologia(Connection con, DtoPlantilla plantilla, int codigoPersona,
			int codPlantillaPaciente, String tipoFuncionalidad,	String loginUsuario, int institucion) {
		
		HashMap campos = new HashMap();
		campos.put("codigoPaciente", codigoPersona);
		campos.put("tipoFuncionalidad", tipoFuncionalidad);
		campos.put("loginUsuario", loginUsuario);
		campos.put("codigoPlantillaPac",codPlantillaPaciente);
		campos.put("institucion",institucion);
		
		return getPLantillasDao().modificarCamposParametrizablesPacOdontologia(con,plantilla,campos);
	}
	
	public static boolean consultarPlantillaConIgualNombre(double plantillaBase,String nombre)
	{
		return getPLantillasDao().consultarPlantillaConIgualNombre(plantillaBase,nombre);
	}
	
	public static ArrayList obtenerEspecialidadesConPlantillaParametrizada(Connection con, String tipoEspecialidad, int funParam)
	{
		HashMap campos = new HashMap();
		campos.put("codigoMedico", "");
		campos.put("codigoEspecialidad", "");
		campos.put("tipoEspecialidad", tipoEspecialidad);
		campos.put("funParam", funParam);
		return getPLantillasDao().obtenerEspecialidadesConPlantillaParametrizada(con,campos);
	}
	
	/************************************************************************************
	 * METODOS JULIO
	 * Metodos para plantillas de Odontologia
	 ***********************************************************************************/

	//*************************************************************************************************
	
	/**
	 * Método implementado para guardar los campos parametrizables en plantillas_ingreso
	 * @param codigoPlantillaIngreso 
	 */
	public static double guardarCamposParametrizablesIngresoOdontologia(Connection con,DtoPlantilla plantilla, int codigoIngreso, int codigoPaciente, String fecha,String hora, String loginUsuario, double valoracionOdonto, double codigoPlantillaIngreso)
	{
		HashMap campos = new HashMap();
		campos.put("codigoIngreso", codigoIngreso);
		campos.put("codigoPaciente", codigoPaciente);
		campos.put("fecha", fecha);
		campos.put("hora", hora);
		campos.put("loginUsuario", loginUsuario);
		campos.put("valoracionOdonto", valoracionOdonto);
		campos.put("codigoPlantillaIngreso", codigoPlantillaIngreso);
		
		
		return getPLantillasDao().guardarCamposParametrizablesIngresoOdontologia(con, plantilla, campos);
	}	
	
	
    /**
     * Metodo implementado para consultar el codigo de la platilla, asociado a la Valoracion Odontologica de un Paciente
     * @param con
     * @param codPaciente
     * @return
     */
	public static int obtenerCodigoPlantillaValoracionConsultaExtOdonto(Connection con, int codPaciente)
	{		
		HashMap campos = new HashMap();
		campos.put("codPaciente",codPaciente);
		campos.put("codFuncionalidad", ConstantesCamposParametrizables.funcParametrizableValoracionConsultaExternaOdontologia);
		
		return getPLantillasDao().obtenerCodigoPlantillaValoracionConsultaExtOdonto(con, campos);
		
	}
	
	
	/**
	 * Motodo implementado para Cargar UNICAMENTE  el componente de Antecedentes Odontologia asociado a una
	 * Valoracion o Evolucion de un Paciente
	 * @param parametros
	 * @return
	 */
	public static DtoComponente cargarComponenteAntecedentesOdonto(HashMap parametros)
	{			
		return getPLantillasDao().cargarComponenteAntecedentesOdonto(parametros);
		
	}
	
	
	public static DtoComponente cargarComponenteGenericoPreview(HashMap paremetros)
	{
		return getPLantillasDao().cargarComponenteGenericoPreview(paremetros);
	}
	
	

	/**
	 * Método implementado para guardar los campos parametrizables en plantillas_evolucion.
	 * @param Connection con
	 * @param DtoPlantilla plantillaDto
	 * @param String codigoIngreso
	 * @param String codigoPkEvolucion
	 * @param String fechaEjecucion
	 * @param String horaEjecucion
	 * @param UsuarioBasico usuario
	 */
	public static int guardarEvolucionOdon(
			Connection con,
			DtoPlantilla plantillaDto,
			String codigoIngreso,
			String codigoPkEvolucion,
			String codigoPkPlantillaEvolucion,
			String fechaEjecucion,
			String horaEjecucion,
			UsuarioBasico usuario)
	{
		HashMap campos = new HashMap();
		campos.put("codigoIngreso",codigoIngreso);
		campos.put("codigoPkEvolucion",codigoPkEvolucion);
		campos.put("codigoPkPlantillaEvolucion",codigoPkPlantillaEvolucion);
		campos.put("fecha",UtilidadFecha.conversionFormatoFechaABD(fechaEjecucion));
		campos.put("hora",horaEjecucion);				
		campos.put("usuarioModifica",usuario.getLoginUsuario());
		campos.put("fechaModifica",UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
		campos.put("horaModifica",UtilidadFecha.getHoraActual());
				
		return getPLantillasDao().guardarEvolucionOdon(con,plantillaDto,campos);
	} 
	
	/**
	 * Metodo para Cargar Plantilla de Ingreso Paciente Odontologia
	 * @param con
	 * @param codigoInstitucion
	 * @param usuario
	 * @param codigoPaciente
	 * @param codigoSexoPaciente
	 * @param edadPaciente
	 * @return
	 */
	public static DtoPlantilla cargarPlantillaEvolucionOdon(
			Connection con,			
			int codigoInstitucion,
			UsuarioBasico usuario,
			int codigoPaciente,
			String tipoFuncionalidad,
			int codigoPkPlantilla,
			int codigoIngreso,
			double evolucion
			)
	{
		HashMap campos = new HashMap();
		campos.put("codigoInstitucion",codigoInstitucion);
		campos.put("codigoFuncionalidad",ConstantesCamposParametrizables.funcParametrizableEvolucionOdontologica);
		campos.put("codigoPaciente",codigoPaciente);
		campos.put("codigoPK",codigoPkPlantilla);
		campos.put("filtroDatosPaciente",false);
		campos.put("tipoFuncionalidad",tipoFuncionalidad);
		 
		campos.put("consultarRegistro",true);
		campos.put("vieneValoracion",true);
		  
		campos.put("codigoCentroCosto",ConstantesBD.codigoNuncaValido);
	    campos.put("codigoSexo",ConstantesBD.codigoNuncaValido);
	    campos.put("codigoEspecialidad",ConstantesBD.codigoNuncaValido);		
	    campos.put("numeroSolicitud",ConstantesBD.codigoNuncaValido);
		campos.put("codigoIngreso",codigoIngreso);		
	    campos.put("codigoEvolucion",ConstantesBD.codigoNuncaValido);
	    campos.put("codigoRespuestaProcedimiento",ConstantesBD.codigoNuncaValido);
	    campos.put("codigoSexoPaciente",ConstantesBD.codigoNuncaValido);
	    campos.put("diasEdadPaciente",ConstantesBD.codigoNuncaValido);		
	    campos.put("tipoAtencion","");
	    campos.put("nombrePlantilla","");	
	    
	    campos.put("codigoValoracionOdo",ConstantesBD.codigoNuncaValido);		
	    campos.put("codigoEvolucionOdo", evolucion);
		
		logger.info("Plantillas tipo FUNCIONALIDAD >>"+tipoFuncionalidad);
		
		return getPLantillasDao().cargarPlantilla(con, campos);
	}
	
	
	/**
	 * Metodo sobrecargado que  consulta la informaciï¿½n de la tabla plantillas con mï¿½s parï¿½metros que posee ï¿½sta tabla
	 * @param Connection con
	 * @param String institucion
	 * @param String codigoFuncionalidadParametrica
	 * @param String consecutivo planilla (opcional)
	 * @param String codigo planilla (opcional)
	 * */
	public static ArrayList<DtoPlantilla> obtenerListadoPlantillasEnArray(
			Connection con,
			String institucion,
			int codigoFuncionalidadParametrica,
			String consecutivoPlanilla,
			String codigoPlanilla,			
			boolean indicadorCargarServicios,
			String mostrarModificacion,
			String tipoAtencion,
			String especialidad)
	{
		HashMap mapa = new HashMap();
		mapa.put("codigoPk",consecutivoPlanilla);		
		mapa.put("codigoPlantilla",codigoPlanilla);
		mapa.put("institucion",institucion);
		mapa.put("funParam",codigoFuncionalidadParametrica);
		mapa.put("mostrarModificacion",mostrarModificacion);
		mapa.put("tipoAtencion",tipoAtencion);
		mapa.put("especialidad",especialidad);
		
		int tarifario= Utilidades.convertirAEntero(ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(Utilidades.convertirAEntero(institucion)));
		if(tarifario<0)
			tarifario=ConstantesBD.codigoTarifarioCups;
		
		mapa.put("tarifario", tarifario);
		
		return getPLantillasDao().obtenerListadoPlantillasEnArray(con,mapa);
	}
	
	public static boolean plantillaValoracionEsUsada(String plantilla)
	{
		return getPLantillasDao().plantillaValoracionEsUsada(plantilla);
	}
	
	/**
	 * Metodo que obtiene las escalas que son requeridas en una plantilla
	 * @param con
	 * @param codigoPlantilla
	 * @return
	 */
	public static ArrayList<DtoEscala> obtenerEscalasRequeridas(Connection con, int codigoPlantilla)
	{
		HashMap mapa = new HashMap();
		mapa.put("codigoPlantilla",codigoPlantilla);
		return getPLantillasDao().obtenerEscalasRequeridas(con, mapa);
	}
	
	/**
	 * Metodo que verifica si se ha guardado información de una escala para una plantilla determinada
	 * @param con
	 * @param codigoPlantilla
	 * @param codigoPaciente 
	 * @return
	 */
	public static boolean existeInfoEscala(Connection con, String codigoPlantilla, String codigoPaciente, String codigoEscala)
	{
		HashMap mapa = new HashMap();
		mapa.put("codigoPlantilla",codigoPlantilla);
		mapa.put("codigoPaciente",codigoPaciente);
		mapa.put("codigoEscala",codigoEscala);
		return getPLantillasDao().existeInfoEscala(con, mapa);
	}
	
	/**
	 * @param con
	 * @param codigoPkPlantilla
	 * @return
	 * @throws SQLException
	 */
	public static  Boolean consultarPlantillaFijaSinOrden(Connection con , Integer codigoPkPlantilla,String tipoFormato) throws SQLException
	{
		return getPLantillasDao().consultarPlantillaFijaSinOrden(con, codigoPkPlantilla, tipoFormato);
	}
	
	
	/**
	 * @param con
	 * @param codigoPlantilla
	 * @param visible
	 * @param nombreSeccion
	 * @throws SQLException
	 */
	public static void guardarPlantillaSinOrden(Connection con,Integer codigoPlantilla,Boolean visible,String nombreSeccion,Integer sexo,Integer centroCosto,String tipoFormato) throws SQLException
	{
		getPLantillasDao().guardarPlantillaSinOrden(con, codigoPlantilla, visible, nombreSeccion, sexo, centroCosto, tipoFormato);
	}
	
	
	/**
	 * @param con
	 * @param codigoPlantilla
	 * @param visible
	 * @param tipoBD
	 * @throws SQLException
	 */
	public static  void actualziarPlantillaFijaSinOrden(Connection con,Integer codigoPlantilla,Boolean visible,Integer sexo, Integer centroCosto ,String tipoFormato ) throws SQLException
	{
		getPLantillasDao().actualziarPlantillaFijaSinOrden(con, codigoPlantilla, visible,  sexo,  centroCosto, tipoFormato);
	}
	
	
	
	/**
	 * @param con
	 * @param codigoPkPlantilla
	 * @return
	 * @throws SQLException
	 */
	public static  Boolean consultarVisibilidadPlantillaFijaSinOrden(Connection con , Integer codigoPkPlantilla,String tipoFormato) throws SQLException
	{
		return getPLantillasDao().consultarVisibilidadPlantillaFijaSinOrden(con, codigoPkPlantilla, tipoFormato);
	}
	

	/**
	 * @param con
	 * @param codigoPkPlantilla
	 * @param sexo
	 * @param centroCosto
	 * @return
	 * @throws SQLException
	 */
	public static  Boolean consultarVisibilidadPlantillaFijaSinOrdenUrgencias(Connection con , Integer codigoPkPlantilla,Integer sexo , Integer centroCosto,String tipoFormato) throws SQLException
	{
		return getPLantillasDao().consultarVisibilidadPlantillaFijaSinOrdenUrgencias(con, codigoPkPlantilla, sexo, centroCosto, tipoFormato);
	}
	
	/**
	 * @see com.princetonsa.dao.historiaClinica.PlantillasDao#existePlantillaFijaSinOrdenUrgencias(java.sql.Connection, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.String)
	 */
	public static  Boolean existePlantillaFijaSinOrdenUrgencias(Connection con , Integer codigoPkPlantilla,Integer sexo , Integer centroCosto,String tipoFormato) throws SQLException
	{
		return getPLantillasDao().existePlantillaFijaSinOrdenUrgencias(con, codigoPkPlantilla, sexo, centroCosto, tipoFormato);
	}
	
	/**
	 * @param con
	 * @param codigoPkPlantilla
	 * @param especialidad
	 * @return
	 * @throws SQLException
	 */
	public static  Boolean existePlantillaFijaSinOrdenConsultaExterna(Connection con , Integer codigoPkPlantilla,Integer especialidad) throws SQLException{
		return getPLantillasDao().existePlantillaFijaSinOrdenConsultaExterna(con, codigoPkPlantilla, especialidad);
	}
	
	
	/**
	 * @param con
	 * @param codigoPlantilla
	 * @param visible
	 * @param nombreSeccion
	 * @param tipoBD
	 * @param especialdiad
	 * @throws SQLException
	 */
	public static  void guardarPlantillaSinOrdenConsultaExterna(Connection con,Integer codigoPlantilla,Boolean visible,String nombreSeccion,Integer especialdiad) throws SQLException
	{
		getPLantillasDao().guardarPlantillaSinOrdenConsultaExterna(con, codigoPlantilla, visible, nombreSeccion, especialdiad);
	}
	

	/**
	 * @param con
	 * @param codigoPlantilla
	 * @param visible
	 * @param especialidad
	 * @throws SQLException
	 */
	public  static void actualziarPlantillaFijaSinOrdenConsultaExterna(Connection con,Integer codigoPlantilla,Boolean visible ,Integer especialidad) throws SQLException
	{
		getPLantillasDao().actualziarPlantillaFijaSinOrdenConsultaExterna(con, codigoPlantilla, visible, especialidad);
	}
	
	/**
	 * @param con
	 * @param codigoPkPlantilla
	 * @param especialidad
	 * @return
	 * @throws SQLException
	 */
	public static   Boolean consultarVisibilidadPlantillaFijaSinOrdenConsultaExterna(Connection con , Integer codigoPkPlantilla,Integer especialidad) throws SQLException
	{
		return getPLantillasDao().consultarVisibilidadPlantillaFijaSinOrdenConsultaExterna(con, codigoPkPlantilla, especialidad);
	}
	
}