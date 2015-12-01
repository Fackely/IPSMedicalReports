/*
 * Mayo 10, 2007
 */
package util.historiaClinica;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.InfoDatosInt;
import util.InfoDatosString;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadValidacion;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.historiaClinica.UtilidadesHistoriaClinicaDao;
import com.princetonsa.dto.facturacion.DtoConvenio;
import com.princetonsa.dto.historiaClinica.DtoEspecialidad;
import com.princetonsa.dto.historiaClinica.DtoInformacionParto;
import com.princetonsa.dto.historiaClinica.DtoInformacionRecienNacido;
import com.princetonsa.dto.historiaClinica.DtoMedicamentosOriginales;
import com.princetonsa.dto.historiaClinica.DtoRevisionSistema;
import com.princetonsa.dto.historiaClinica.componentes.DtoHistoriaMenstrual;
import com.princetonsa.dto.historiaClinica.componentes.DtoOftalmologia;
import com.princetonsa.dto.historiaClinica.componentes.DtoPediatria;
import com.princetonsa.dto.manejoPaciente.DtoRequsitosPaciente;
import com.princetonsa.dto.manejoPaciente.DtoSolicitudesSubCuenta;
import com.princetonsa.dto.manejoPaciente.DtoSubCuentas;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.antecedentes.AntecedentesGinecoObstetricos;
import com.princetonsa.mundo.atencion.Diagnostico;
import com.princetonsa.mundo.atencion.SignoVital;
import com.princetonsa.mundo.hojaObstetrica.HojaObstetrica;
import com.servinte.axioma.fwk.exception.IPSException;

/**
 * 
 * @author sgomez
 * Clase que contiene las utilidades del módulo de HISTORIA CLINICA
 */
/**
 * @author LuiCasOv
 *
 */
public class UtilidadesHistoriaClinica 
{
	
	private static Logger logger =Logger.getLogger(UtilidadesHistoriaClinica.class);
	/**
	 * instancia del DAO
	 * @return
	 */
	public static UtilidadesHistoriaClinicaDao utilidadesDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesHistoriaClinicaDao();
	}
	
	/**
	 * Método que consulta los datos de accidente de trabajo del triage
	 * @param con
	 * @param consecutivo
	 * @param consecutivoFecha
	 * @return
	 */
	public static HashMap consultaDatosAccidenteTrabajoTriage(Connection con,String consecutivo,String consecutivoFecha)
	{
		return utilidadesDao().consultaDatosAccidenteTrabajoTriage(con, consecutivo, consecutivoFecha);
	}
	
	/**
	 * Método que consulta la última evolucion de un ingreso
	 * @param con
	 * @param ingreso
	 * @return
	 */
	public static int consultarUltimaEvolucionIngreso(Connection con,int ingreso)
	{
		return utilidadesDao().consultarUltimaEvolucionIngreso(con, ingreso);
	}
	
	/**
	 * Método que consula la última valoracion de un ingreso
	 * @param con
	 * @param ingreso
	 * @return
	 */
	public static int consultarUltimaValoracionIngreso(Connection con,int ingreso)
	{
		return utilidadesDao().consultarUltimaValoracionIngreso(con, ingreso);
	}
	
	/**
	 * Método que consulta los motivos sirc parametrizados por institucion
	 * @param con
	 * @param institucion
	 * @param tipoMotivo
	 * @param activo
	 * @return
	 */
	public static HashMap obtenerMotivosSirc(Connection con,int institucion,String tipoMotivo,String activo)
	{
		HashMap campos = new HashMap();
		campos.put("institucion",institucion+"");
		campos.put("tipoMotivo",tipoMotivo);
		campos.put("activo",activo);
		return utilidadesDao().obtenerMotivosSirc(con, campos);
	}
	
	/**
	 * Método que consulta los servicios SIRC de una institucion
	 * @param con
	 * @param institucion
	 * @param activo
	 * @param serviciosInsertados : lista de servicios Sirc insertados separada por comas, en el formato servicioSirc1-servicioAxioma,servicioSirc2-servicioAxioma2
	 * @return
	 */
	public static HashMap obtenerServiciosSirc(Connection con,int institucion,String activo, String serviciosInsertados)
	{
		HashMap campos = new HashMap();
		campos.put("institucion", institucion+"");
		campos.put("activo",activo);
		campos.put("serviciosInsertados",serviciosInsertados);
		HashMap resultados = utilidadesDao().obtenerServiciosSirc(con, campos);
		
		
		//******SE VERIFICA SI EL SERVICIO YA FUE SELECCIONADO SEGUN LA VARIABLE SEVICIOS INSERTADOS*********************
		if(serviciosInsertados.equals(""))
		{
			for(int i=0;i<Integer.parseInt(resultados.get("numRegistros").toString());i++)
				resultados.put("seleccionado_"+i, ConstantesBD.acronimoNo);
		}
		else
		{
			String[] vector = serviciosInsertados.split(",");
			String aux = "";
			boolean seleccionado = false;
			for(int i=0;i<Integer.parseInt(resultados.get("numRegistros").toString());i++)
			{
				aux = resultados.get("codigo_"+i)+"-"+resultados.get("servicio_"+i);
				seleccionado = false; 
				
				for(int j=0;j<vector.length;j++)
				{
					if(aux.equals(vector[j]))
						seleccionado = true;
				}
				
				if(seleccionado)
					resultados.put("seleccionado_"+i, ConstantesBD.acronimoSi);
				else
					resultados.put("seleccionado_"+i, ConstantesBD.acronimoNo);
			}
		}
		//**********************************************************************************************
		
		return resultados;
	}
	
	
	/**
	 * Método que consulta los estados de conciencia 
	 * @param con
	 * @param incluyeNoDefinido (si es true se incluirá el registro 'No definido')
	 * @return
	 */
	public static HashMap obtenerEstadosConciencia(Connection con,boolean incluyeNoDefinido)
	{
		return utilidadesDao().obtenerEstadosConciencia(con, incluyeNoDefinido);
	}
	
	/**
	 * Método que consulta las interpretaciones y la descripcion de los procedimientos de un ingreso
	 * validando el parámetro general "Valida en egreso solicitudes interpretadas:"
	 * @param con
	 * @param ingreso
	 * @param institucion
	 * @param procedimientos insertados: lista de solicitudes separada por comas ya insertadas
	 * @return
	 */
	public static HashMap obtenerInterpretacionProcedimientosIngreso(Connection con,int ingreso,int institucion,String procedimientosInsertados)
	{
		HashMap campos = new HashMap();
		campos.put("ingreso",ingreso+"");
		campos.put("institucion",institucion+"");
		HashMap resultados = utilidadesDao().obtenerInterpretacionProcedimientosIngreso(con, campos);
		
		//******QUITAR ESPACIOS DEL CAMPO INTERPRETACION DE LOS PROCEDIMIENTOS****************************************++
		String interpretacion = "";
		for(int i=0;i<Integer.parseInt(resultados.get("numRegistros").toString());i++)
		{
			interpretacion = resultados.get("interpretacion_"+i).toString();
			resultados.put("interpretacion_"+i, interpretacion.trim());
		}
		
		//******SE VERIFICA SI EL SERVICIO YA FUE SELECCIONADO SEGUN LA VARIABLE PROCEDIMIENTOS INSERTADOS*********************
		if(!procedimientosInsertados.equals(""))
		{
			String[] vector = procedimientosInsertados.split(",");
			int numRegistros = Integer.parseInt(resultados.get("numRegistros").toString());
			boolean seleccionado = false;
			for(int i=0;i<numRegistros;i++)
			{
				seleccionado = false;
				for(int j=0;j<vector.length;j++)
				{
					if(resultados.get("numero_solicitud_"+i).toString().equals(vector[j]))
						seleccionado = true;
				}
				
				if(seleccionado)
					resultados.put("seleccionado_"+i, ConstantesBD.acronimoSi);
				else
					resultados.put("seleccionado_"+i, ConstantesBD.acronimoNo);
			}
			
				
		}
		else
		{
			for(int i=0;i<Integer.parseInt(resultados.get("numRegistros").toString());i++)
				resultados.put("seleccionado_"+i, ConstantesBD.acronimoNo);
		}
		//*****************************************************************************************
		return resultados;
	}
	
	
	/**
	 * Método que consulta la última anamnesis del ingreso
	 * @param con
	 * @param ingreso
	 * @return
	 */
	public static String getUltimaAnamnesisIngreso(Connection con,int ingreso)
	{
		String anamnesis = "";
		logger.info("ingreso=> "+ingreso);
		int valoracion = consultarUltimaValoracionIngreso(con, ingreso);
		logger.info("valoracion=> "+ingreso);
		if(valoracion>0)
			anamnesis = utilidadesDao().getUltimaAnamnesisIngreso(con, valoracion+"");
		
		return anamnesis;
	}
	
	/**
	 * Método que consulta la ultima referencia  del paciente 
	 * @param con
	 * @param codigoPaciente
	 * @return
	 */
	public static String getUltimaReferenciaPaciente(Connection con,String codigoPaciente,String tipoReferencia,String estado)
	{
		return utilidadesDao().getUltimaReferenciaPaciente(con, codigoPaciente, tipoReferencia, estado);
	}
	
	/**
	 * Método que actualiza el estado de la referencia
	 * @param con
	 * @param numeroReferencia
	 * @param ingreso
	 * @param estado
	 * @param loginUsuario
	 * @return
	 */
	public static boolean actualizarEstadoReferencia(Connection con,String numeroReferencia,String ingreso,String estado,String loginUsuario)
	{
		return utilidadesDao().actualizarEstadoReferencia(con, numeroReferencia, ingreso, estado, loginUsuario);
	}
	
	/**
	 * Método que consulta el estado de la referencia de un ingreso
	 * Nota* Si retorna cadena vacía quiere decir que ese ingreso no tiene
	 * referencia asociada
	 * @param con
	 * @param idIngreso
	 * @param tipoReferencia
	 * @return
	 */
	public static String getEstadoReferenciaXIngreso(Connection con,String idIngreso,String tipoReferencia)
	{
		HashMap campos = new HashMap();
		campos.put("idIngreso", idIngreso);
		campos.put("tipoReferencia", tipoReferencia);
		return utilidadesDao().getEstadoReferenciaXIngreso(con, campos);
	}
	
	/**
	 * Método para consultar el diagnostico de ingreso que se debe postular
	 * en la valoracion de hospitalizacion en el caso de que se haya ingresado
	 * una referencia externa y el origen de la admision sea remitido
	 * @param con
	 * @param codigoCuenta
	 * Se retorna acronimo + separadorSplit + tipoCie + separadorSplit + nombre
	 * Si no encuentra nada retorna cadena vacía
	 */
	public static String getDxIngresoDeReferencia(Connection con,String codigoCuenta)
	{
		return utilidadesDao().getDxIngresoDeReferencia(con, codigoCuenta);
	}
	
	/**
	 * Método que consulta el número de referencia de un ingreso dependiendo del tipo de referencia
	 * Nota: En el caso de que no encuentre nada retorna una cadena vacía
	 * @param con
	 * @param idIngreso
	 * @param tipoReferencia
	 * @return
	 */
	public static String getNumeroReferenciaIngreso(Connection con,String idIngreso,String tipoReferencia)
	{
		return utilidadesDao().getNumeroReferenciaIngreso(con, idIngreso, tipoReferencia);
	}
	
	/**
	 * Método que verifica si hay una contrarreferencia para dado un número de referencia
	 * @param con
	 * @param numeroReferencia
	 * @return
	 */
	public static boolean existeContrarreferencia(Connection con,String numeroReferencia)
	{
		return utilidadesDao().existeContrarreferencia(con, numeroReferencia);
	}
	
	
	
	
	/***
	 * Método que retorna mensaje de validacion si hay referencia
	 * @param con
	 * @param idIngreso
	 * @param numeroSolicitud
	 * @param tipoReferencia
	 * @return
	 */
	public static String getMensajeReferenciaParaValidacion(Connection con,int idIngreso,String numeroSolicitud,String tipoReferencia)
	{
		HashMap campos = new HashMap();
		campos.put("idIngreso", idIngreso+"");
		campos.put("numeroSolicitud", numeroSolicitud);
		campos.put("tipoReferencia", tipoReferencia);
		return utilidadesDao().getMensajeReferenciaParaValidacion(con, campos);
	}
	
	/**
	 * Metodo que retorna un InfoDatosInt con el codigo-nombre de estado de una cuenta.
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public static InfoDatosInt obtenerEstadoCuenta(Connection con,int idCuenta)
	{
		return utilidadesDao().obtenerEstadoCuenta(con,idCuenta);
	}

	/**
	 * Metodo que retorna un InfoDatosInt con el codigo-nombre de estado de una cuenta.
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public static InfoDatosInt obtenerEstadoCuenta(int idCuenta)
	{
		Connection con= UtilidadBD.abrirConexion();
		InfoDatosInt info= utilidadesDao().obtenerEstadoCuenta(con,idCuenta);
		UtilidadBD.closeConnection(con);
		return info;
	}
	
	/**
	 * Metodo que retorna un InfoDatosInt con el codigo-nombre del centro de atencion asociado a una cuenta.
	 * @param con
	 * @param codigoCuenta
	 * @return
	 */
	public static InfoDatosInt obtenerCentroAtencionCuenta(Connection con, int idCuenta) 
	{
		return utilidadesDao().obtenerCentroAtencionCuenta(con,idCuenta);
	}

	/**
	 * Metodo que retorna un InfoDatosInt con el codigo-nombre del centro de atencion asociado a una cuenta.
	 * @param con
	 * @param codigoCuenta
	 * @return
	 */
	public static InfoDatosInt obtenerCentroAtencionCuenta(int idCuenta) 
	{
		Connection con= UtilidadBD.abrirConexion();
		InfoDatosInt info= utilidadesDao().obtenerCentroAtencionCuenta(con,idCuenta);
		UtilidadBD.closeConnection(con);
		return info;
	}
	
	/**
	 * Metodo que retorna un ArrayList<DtoSubCuentas> con los responsables de un ingreso.
	 * El array list esta indexado desde 0 .....ArrayList.length()
	 * @param con 
	 * @param codigoIngreso
	 * @param todos --> indica, si debe traer todos los responsables, o solo los activaos(No facturados.) 
	 * @param excluirResponsables ---> responsables a excluir
	 * @param pyp (que solo sean de pyp)
	 * @return
	 */
	public static ArrayList<DtoSubCuentas> obtenerResponsablesIngreso(Connection con, int codigoIngreso, boolean todos, String[] excluirResponsables,boolean pyp, String subCuenta,int codigoViaIngreso) throws IPSException 
	{
		return utilidadesDao().obtenerResponsablesIngreso(con,codigoIngreso,todos,excluirResponsables,pyp, subCuenta, codigoViaIngreso);
	}
	
	/**
	 * 
	 * @param codigoIngreso
	 * @param todos
	 * @param excluirResponsables
	 * @param pyp
	 * @param subCuenta
	 * @param codigoViaIngreso
	 * @return
	 */
	public static ArrayList<DtoSubCuentas> obtenerResponsablesIngreso(int codigoIngreso, boolean todos, String[] excluirResponsables,boolean pyp, String subCuenta,int codigoViaIngreso) throws IPSException 
	{
		Connection con=UtilidadBD.abrirConexion();
		ArrayList<DtoSubCuentas> resultado=new ArrayList<DtoSubCuentas>();
		resultado=utilidadesDao().obtenerResponsablesIngreso(con,codigoIngreso,todos,excluirResponsables,pyp, subCuenta, codigoViaIngreso);
		UtilidadBD.closeConnection(con);
		return resultado;
	}
	
	/**
	 * Metodo que retorna un ArrayList<DtoSubCuentas> con los responsables de una determinada solicitud - articulo/servicio
	 * El array list esta indexado desde 0 .....ArrayList.length()
	 * @param con 
	 * @param codigoIngreso
	 * @param todos --> indica, si debe traer todos los responsables, o solo los activaos(No facturados.) 
	 * @return
	 */
	public static ArrayList<DtoSubCuentas> obtenerResponsablesSolServArt(Connection con, int numeroSolicitud, int codArtiServ,boolean esServicio) 
	{
		return utilidadesDao().obtenerResponsablesSolServArt(con,numeroSolicitud,codArtiServ,esServicio);
	}
	
	/**
	 * Metodo que retorna DtoSubCuentas
	 * @param con 
	 * @param codigoIngreso
	 * @return
	 */
	public static DtoSubCuentas obtenerResponsable(Connection con, int codigoSubcuenta) throws IPSException
	{
		return utilidadesDao().obtenerResponsable(con,codigoSubcuenta);
	}
	
	
	/**
	 * Metodo que retorna DtoSubCuentas
	 * @param con 
	 * @param codigoIngreso
	 * @return
	 */
	public static DtoSubCuentas obtenerResponsable(Connection con, int codigoIngreso,int codigoConvenio) 
	{
		return utilidadesDao().obtenerResponsable(con,codigoIngreso,codigoConvenio);
	}
	
	
	/**
	 * Metodo que retorna un ArrayLists con Dto´s de todas las solicitudes asocioada a una subcuenta,
	 * se puede validar por estados, en caso de no querer hacer la validacion de estados enviar int[]
	 * @param con
	 * @param subCuenta
	 * @param estados 
	 * @return
	 */
	public static ArrayList<DtoSolicitudesSubCuenta> obtenerSolicitudesSubCuenta(Connection con, String subCuenta, int[] estados,boolean incluirPaquetizadas,boolean agruparPortatiles) 
	{
		return utilidadesDao().obtenerSolicitudesSubCuenta(con,subCuenta,estados,incluirPaquetizadas,agruparPortatiles);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoIngreso
	 * @return
	 */
	public static InfoDatosString obtenerEstadoIngreso(Connection con, int codigoIngreso)
	{
		return utilidadesDao().obtenerEstadoIngreso(con,codigoIngreso);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoCuenta
	 * @return
	 */
	public static int cuentaTieneAsocioCompleto(Connection con, int codigoCuenta)
	{
		return utilidadesDao().cuentaTieneAsocioCompleto(con,codigoCuenta);
	}
	
	
	/**
	 * 
	 * @param con
	 * @param codigoDetalleCargo
	 * @return
	 */
	public static InfoDatosInt obtenerEstadoDetalleCargo(Connection con, double codigoDetalleCargo)
	{
		return utilidadesDao().obtenerEstadoDetalleCargo(con,codigoDetalleCargo);
	}
	
	
	public static boolean esResponsableFacturado(Connection con, int codigoSubCuenta)
	{
		return utilidadesDao().esResponsableFacturado(con,codigoSubCuenta);
	}
	
	
	public static boolean esResponsableFacturado(Connection con, int codigoIngreso, int codigoConvenio)
	{
		return utilidadesDao().esResponsableFacturado(con,codigoIngreso,codigoConvenio);
	}
	
	
	public static ArrayList obtenerCodigoSolicitudesPendienteCargo(Connection con, int codigoIngreso) 
	{
		return utilidadesDao().obtenerCodigoSolicitudesPendienteCargo(con,codigoIngreso);
	}
	
	
	public static ArrayList obtenerCodigoSolicitudesPendienteCargo(Connection con, int codigoIngreso, int codigoConvenio) 
	{
		return utilidadesDao().obtenerCodigoSolicitudesPendienteCargo(con,codigoIngreso,codigoConvenio);
	}

	/**
	 * 
	 * @param con
	 * @param codigoConvenio
	 * @return
	 */
	public static ArrayList<DtoRequsitosPaciente> obtenerRequisitosPacienteConvenio(Connection con, int codigoConvenio,int codigoViaIngreso) 
	{
		return utilidadesDao().obtenerRequisitosPacienteConvenio(con,codigoConvenio,codigoViaIngreso);
	}

	/**
	 * Método que consulta los convenios pyp de un ingreso
	 * @param con
	 * @param idIngreso
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerConveniosPypIngreso(Connection con,String idIngreso)
	{
		HashMap campos = new HashMap();
		campos.put("idIngreso", idIngreso);
		return utilidadesDao().obtenerConveniosPypIngreso(con, campos);
	}
	
	/**
	 * Método que consulta el convenio PYP que tenga asociada la solicitud
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static DtoConvenio obtenerConvenioPypSolicitud(Connection con,String numeroSolicitud)
	{
		return utilidadesDao().obtenerConvenioPypSolicitud(con, numeroSolicitud);
	}

	/**
	 * 
	 * @param con
	 * @param codigoPersona
	 * @param codigoCentroAtencion
	 * @return
	 */
	public static HashMap<String, Object> consultarIngresosValidos(Connection con, int codigoPersona, int centroAtencion) 
	{
		return utilidadesDao().consultarIngresosValidos(con, codigoPersona,centroAtencion);
	}
	
	/**
	 * Método que consulta los tipos de consulta
	 * @param con
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerTiposConsulta(Connection con)
	{
		return utilidadesDao().obtenerTiposConsulta(con);
	}
	
	/**
	 * Metodo que retorna el codigo del centro de costo segun la id de la cuenta dada
	 * @param con
	 * @param id
	 * @return
	 */
	public static int obtenerCentroCostoCuenta(Connection con,int id)
	{
		return utilidadesDao().obtenerCentroCostoCuenta(con, id);
	}

	/**
	 * 
	 * @param con
	 * @param informacionParametrizable
	 * @param numeroSolicitud
	 */
	public static boolean insertarInformacionParametrizableValoraciones(Connection con, HashMap informacionParametrizable, int numeroSolicitud) 
	{
		return utilidadesDao().insertarInformacionParametrizableValoraciones(con,informacionParametrizable,numeroSolicitud);
	}
	
	/**
	 * Método implementado para insertar información de parto para RIPS
	 * @param con
	 * @param infoParto
	 * @param arregloRecienNacido
	 * @return
	 */
	public static boolean insertarInformacionPartoParaRips(Connection con,DtoInformacionParto infoParto, ArrayList<DtoInformacionRecienNacido> arregloRecienNacido)
	{
		return utilidadesDao().insertarInformacionPartoParaRips(con, infoParto, arregloRecienNacido);
	}
	 
	
	/**
	 * Obtiene el listado de Causas Externas	  
	 * @param con	 
	 * @return llaves(codigo,descripcion)
	 */
	public static ArrayList obtenerCausasExternas(Connection con,boolean solicitudAutorizacion) 
	{
		HashMap campos = new HashMap();
		campos.put("solicitudAutorizacion", solicitudAutorizacion);
		return utilidadesDao().obtenerCausasExternas(con,campos);
	}
	
	/**
	 * Obtiene el listado de Finalidades de la consulta	  
	 * @param con	 
	 * @return llaves(codigo,descripcion)
	 */
	public static ArrayList obtenerFinalidadesConsulta(Connection con) 
	{
		return utilidadesDao().obtenerFinalidadesConsulta(con);
	}
	
	/**
	 * Método implementado para cargar la información del parto ingresada para RIPS
	 * @param con
	 * @param campos
	 * @return
	 */
	public static DtoInformacionParto cargarInformacionPartoParaRips(Connection con,String numeroSolicitud,String codigoIngreso)
	{
		HashMap campos = new HashMap();
		campos.put("numeroSolicitud", numeroSolicitud);
		campos.put("codigoIngreso", codigoIngreso);
		
		return utilidadesDao().cargarInformacionPartoParaRips(con, campos);
	}
	
	/**
	 * Método para obtener el nombre de uan causa externa
	 * @param con
	 * @param codigo
	 * @return
	 */
	public static String obtenerNombreCausaExterna(Connection con,int codigo)
	{
		return utilidadesDao().obtenerNombreCausaExterna(con, codigo);
	}
	
	/**
	 * Método para obtener el nombre de la finalidad de la consulta
	 * @param con
	 * @param codigo
	 * @return
	 */
	public static String obtenerNombreFinalidadConsulta(Connection con,String codigo)
	{
		return utilidadesDao().obtenerNombreFinalidadConsulta(con, codigo);
	}
	
	/**
	 * Método que consulta el id del ingreso a partir de una solicitud
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static String obtenerIdIngresoSolicitud(Connection con,String numeroSolicitud)
	{
		return utilidadesDao().obtenerIdIngresoSolicitud(con, numeroSolicitud);
	}
	
	/**
	 * Método para obtener el codigo del paciente de la solicitud
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static int obtenerCodigoPacienteSolicitud(Connection con,String numeroSolicitud)
	{
		return utilidadesDao().obtenerCodigoPacienteSolicitud(con, numeroSolicitud);
	}
	
	/**
	 * Obtiene la informacion de la muerte del paciente
	 * 
	 * Llaves del Mapa >> estaVivo, fechaMuerte, horaMuerte, certificadoDefuncion, diagnosticoMuerte, diagnosticoMuerteCie, diagnosticoMuerteNombre, numRegistros
	 * 
	 * @param Connection con
	 * @param int codigoPersona (requerido)
	 * @param int codigoCuenta (opcional)
	 * */
	public static HashMap obtenerInfoMuertePaciente(Connection con, int codigoPersona,int codigoCuenta) 
	{
		HashMap parametros = new HashMap();
		parametros.put("codigoPaciente",codigoPersona);
		parametros.put("codigoCuenta",codigoCuenta);
		
		return utilidadesDao().obtenerInfoMuertePaciente(con, parametros);		
	}
	
	/**
	 * Metodo Validacion Medico Especialista justificacion No Pos y Ocupacion Medico Especialista
	 * @param con
	 * @param usuario
	 * @param paraArticulo
	 * @return
	 */
	public static boolean validarEspecialidadProfesionalSalud(Connection con, UsuarioBasico usuario, boolean paraArticulo)
	{
		return utilidadesDao().validarEspecialidadProfesionalSalud(con, usuario, paraArticulo);
	}
	
	/**
	 * Consultar Ingresos por paciente
	 * @param con
	 * @param Codigo Persona
	 * @return
	 */
	public static HashMap consultarIngresosXPaciente(Connection con, int codigoPersona)
	{
		return utilidadesDao().consultarIngresosXPaciente(con, codigoPersona);
	}
	
	/**
	 * Consultar Ingresos por paciente por cuenta
	 * @param con
	 * @param Codigo Persona
	 * @return
	 */
	public static HashMap consultarIngresosCuentaXPaciente(Connection con, int codigoPersona)
	{
		return utilidadesDao().consultarIngresosCuentaXPaciente(con, codigoPersona);
	}
	
	/**
	 * Método que consulta los signos vitales activos
	 * @param con
	 * @return
	 */
	public static ArrayList<SignoVital> cargarSignosVitales(Connection con)
	{
		return utilidadesDao().cargarSignosVitales(con);
	}
	
	/**
	 * Metodo que consulta las clasificaciones de eventos
	 * @param con
	 * @param institucion
	 * @return
	 */
	public static HashMap consultarClasificacionesEventos(Connection con, int institucion){
		return utilidadesDao().consultarClasificacionesEventos(con, institucion);
	}
	
	/**
	 * Método implementado para cargar la revision x sistemas comun y la del un listado de componentes
	 * @param con
	 * @param tiposComponente
	 * @return
	 */
	public static ArrayList<DtoRevisionSistema> cargarRevisionesSistemas(Connection con,ArrayList<Integer> tiposComponente)
	{
		return utilidadesDao().cargarRevisionesSistemas(con, tiposComponente);
	}
	
	/**
	 * Método implementado para cargar estados de conciencia
	 * @param con
	 * @param institucion
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> cargarEstadosConciencia(Connection con,int institucion)
	{
		return utilidadesDao().cargarEstadosConciencia(con, institucion);
	}
	
	/**
	 * Método para cargar las conductas de la valoracion
	 * @param con
	 * @return
	 */
	public static ArrayList<HashMap<String,Object>> cargarConductasValoracion(Connection con)
	{
		return utilidadesDao().cargarConductasValoracion(con);
	}
	
	/**
	 * Método implementado para cargar los tipos de diagnostico
	 * @param con
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> cargarTiposDiagnostico(Connection con)
	{
		return utilidadesDao().cargarTiposDiagnostico(con);
	}
	
	/**
	 * Método que consuta el nombre de una conducta de valoración
	 * @param con
	 * @param codigo
	 * @return
	 */
	public static String obtenerNombreConductaValoracion(Connection con,int codigo)
	{
		return utilidadesDao().obtenerNombreConductaValoracion(con, codigo);
	}
	
	/**
	 * Método usado para obtener el codigo de la valoración de urgencias
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public static int obtenerCodigoConductaValoracionUrgenciasCuenta(Connection con,String idCuenta)
	{
		return utilidadesDao().obtenerCodigoConductaValoracionUrgenciasCuenta(con, idCuenta);
	}
	
	/**
	 * Método implementado para consultar los ultimos diagnosticos del paciente, primero se busca por la evolucion,
	 * luego se busca por las valoraciones.
	 * Nota * En el caso de encontrar diagnosticos, el primer elemento del arreglo corresponderá a el diagnóstico principal
	 * @param con
	 * @param codigoPaciente
	 * @return
	 */
	public static ArrayList<Diagnostico> obtenerUltimosDiagnosticosPaciente(Connection con,String codigoPaciente)
	{
		return utilidadesDao().obtenerUltimosDiagnosticosPaciente(con, codigoPaciente);
	}
	
	/**
	 * Método que realiza la consulta de la historia menstrual 
	 * @param con
	 * @param codigoPaciente
	 * @param numeroSolicitud
	 * @return
	 */
	public static DtoHistoriaMenstrual cargarHistoriaMenstrual(Connection con,String codigoPaciente,String numeroSolicitud)
	{
		return utilidadesDao().cargarHistoriaMenstrual(con, codigoPaciente, numeroSolicitud);
	}
	
	/**
	 * Método implementado para registrar la historia menstrual del antecedente ginecologico del paciente
	 * @param con
	 * @param historiaMenstrual
	 * @param numeroSolicitud
	 * @param usuario
	 * @return
	 */
	public static ResultadoBoolean ingresarHistoriaMenstrual(Connection con,DtoHistoriaMenstrual historiaMenstrual,String numeroSolicitud,UsuarioBasico usuario)
	{
		ResultadoBoolean resultado = new ResultadoBoolean(true);
		
		//Se verifica que se haya ingresado informacion de historia menstrual
		if(historiaMenstrual.ingresoInformacion())
		{
			//Se pregunta si no habían antecedentes antes registrados para registrarlos
			if(!UtilidadValidacion.existeAntecedenteGinecoObstetrico(con, Integer.parseInt(historiaMenstrual.getCodigoPaciente())))
				if(HojaObstetrica.ingresarAntecedente(con, Integer.parseInt(historiaMenstrual.getCodigoPaciente()), usuario)<=0)
				{
					resultado.setResultado(false);
					resultado.setDescripcion("Problemas ingresando el antecedente de ginecología");
				}
			//Si todo va bien se insertan las datos de la historia menstrual
			if(resultado.isTrue())
			{
				if(AntecedentesGinecoObstetricos.inseretarDatosHistMenstrual(
					con, 
					historiaMenstrual.getCodigoEdadMenarquia(), 
					historiaMenstrual.getCodigoEdadMenopausia(), 
					historiaMenstrual.getOtraEdadMenarquia(), 
					historiaMenstrual.getOtraEdadMenopausia(), 
					historiaMenstrual.getCicloMenstrual(), 
					historiaMenstrual.getDuracionMenstruacion(), 
					historiaMenstrual.getFechaUltimaRegla(), 
					historiaMenstrual.getDolorMenstruacion()==null?"":(historiaMenstrual.getDolorMenstruacion()?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo), 
					historiaMenstrual.getCodigoConceptoMenstruacion(), 
					historiaMenstrual.getObservacionesMenstruales(), 
					usuario, 
					Integer.parseInt(historiaMenstrual.getCodigoPaciente()), 
					Integer.parseInt(numeroSolicitud))<=0)
				{
					resultado.setResultado(false);
					resultado.setDescripcion("Problemas ingresando el histórico menstrual del paciente");
				}
			}
		}
		
		return resultado;
	}
	
	/**
	 * Método para obtener los rangos edad menarquia
	 * @param con
	 * @param incluirOpcionInvalida
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerRangosEdadMenarquia(Connection con,boolean incluirOpcionInvalida)
	{
		return utilidadesDao().obtenerRangosEdadMenarquia(con, incluirOpcionInvalida); 	
	}
	
	/**
	 * Método para obtener los rangos de la edad de menopausia
	 * @param con
	 * @param incluirOpcionInvalida
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerRangosEdadMenopausia(Connection con,boolean incluirOpcionInvalida)
	{
		return utilidadesDao().obtenerRangosEdadMenopausia(con, incluirOpcionInvalida);
	}
	
	/**
	 * Método para obtener los conceptos de menstruacion
	 * @param con
	 * @param incluirOpcionInvalida
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerConceptosMenstruacion(Connection con,boolean incluirOpcionInvalida)
	{
		return utilidadesDao().obtenerConceptosMenstruacion(con, incluirOpcionInvalida);
	}
	
	/**
	 * Método para cargar toda la información del componente de oftalmología
	 * Nota * Este método se puede usar para dos propósitos
	 * 1. Para cargar la parametrizacion y postular la captura de informacion (Sin numero de solicitud y con codigoInstitucion)
	 * 2. Para cargar el resumen de lo que se guardó de una solicitud específica (Con numeroSolicitud y sin codigoInstitucion)
	 * @param con
	 * @param numeroSolicitud
	 * @param codigoInstitucion
	 * @return
	 */
	public static DtoOftalmologia cargarOftalmologia(Connection con,String numeroSolicitud,int codigoInstitucion)
	{
		return utilidadesDao().cargarOftalmologia(con, numeroSolicitud, codigoInstitucion);
	}
	
	/**
	 * Método implementado para ingrear informacion de oftalmología (Componente)
	 * @param con
	 * @param oftalmologia
	 * @return
	 */
	public static ResultadoBoolean ingresarOftalmologia(Connection con,DtoOftalmologia oftalmologia)
	{
		return utilidadesDao().ingresarOftalmologia(con, oftalmologia);
	}
	
	/**
	 * Método implementado para cargar la informacion del componente de pediatría
	 * @param con
	 * @param codigoPaciente
	 * @param numeroSolicitud
	 * @param edadPaciente
	 * @return
	 */
	public static DtoPediatria cargarPediatria(Connection con,String codigoPaciente,String numeroSolicitud,int edadPaciente,UsuarioBasico usuario)
	{
		return utilidadesDao().cargarPediatria(con, codigoPaciente, numeroSolicitud, edadPaciente, usuario);
	}
	
	/**
	 * Método implementado para insertar informacion del componente de pediatría
	 * @param con
	 * @param pediatria
	 * @return
	 */
	public static ResultadoBoolean ingresarPediatria(Connection con,DtoPediatria pediatria)
	{
		return utilidadesDao().ingresarPediatria(con, pediatria);
	}
	
	/**
	* Método para obtener los signos y sintomas
	* @param con
	* @param institucion
	* @return
	*/
	public static ArrayList<HashMap<String, Object>> obtenerSignosSintomas(Connection con,int institucion)
	{
		return utilidadesDao().obtenerSignosSintomas(con, institucion);
	}
	
	/**
	* Método para obtener las categorias triage
	* @param con
	* @param institucion
	* @return
	*/
	public static ArrayList<HashMap<String, Object>> obtenerCategoriasTriage(Connection con,int institucion)
	{
		return utilidadesDao().obtenerCategoriasTriage(con, institucion);
	}
	
	/**
	* Método para obtener los motivos de consulta urgencias
	* @param con
	* @param institucion
	* @return
	*/
	public static ArrayList<HashMap<String, Object>> obtenerMotivosConsultaUrgencias(Connection con,int institucion)
	{
		return utilidadesDao().obtenerMotivosConsultaUrgencias(con, institucion);
	}
	
	
	/**
	 * Metodo encargado de consultar la instituciones Sirc
	 * pudiendo filtrar por lo diferentes criterios.
	 * @author Felipe Perez
	 * @param con
	 * @param criterios
	 * ------------------------------------------
	 * KEY'S DEL MAPA CRITERIOS
	 * ------------------------------------------
	 * -- institucion --> Requerido
	 * -- codigo --> Opcional
	 * -- descripcion --> Opcional
	 * -- nivelservicio --> Opcional
	 * -- tipored --> Opcional
	 * -- tipoinstreferencia --> Opcional
	 * -- tipoinstambulancia --> Opcional
	 * -- activo --> Opcional
	 * -- centroAtencion --> Opcional
	 * @return ArrayListHashMap
	 *-----------------------------------------
	 *KEY'S DEL HASHMAP DENTRO DEL ARRAYLIST
	 *----------------------------------------- 
	 * codigo,descripcion,nivelservicio,tipored,
	 * tipoinstreferencia,tipoinstambulancia,activo
	 */
	public  static ArrayList<HashMap<String, Object>> obtenerInstitucionesSirc(Connection con,HashMap criterios)
	{
		return utilidadesDao().obtenerInstitucionesSirc(con, criterios);
	}
	
	/**
	 * Método para consultar el diagnostico de ingreso del paciente
	 * @param con
	 * @param campos
	 * @return
	 */
	public static String obtenerDescripcionDxIngresoPaciente(Connection con,int codigoCuenta,int codigoViaIngreso)
	{
		HashMap campos = new HashMap();
		campos.put("codigoViaIngreso", codigoViaIngreso);
		campos.put("codigoCuenta", codigoCuenta);
		return utilidadesDao().obtenerDescripcionDxIngresoPaciente(con, campos);
	}
	
	/**
	 * Método para verificar si un usuario está siendo autoatendido
	 * @param usuario
	 * @param paciente
	 * @return
	 */
	public static ResultadoBoolean esUsuarioAutoatendido(UsuarioBasico usuario,PersonaBasica paciente)
	{
		ResultadoBoolean respuesta = new ResultadoBoolean(false,"");
		if(usuario.getCodigoTipoIdentificacion().equals(paciente.getCodigoTipoIdentificacionPersona())&&
			usuario.getNumeroIdentificacion().equals(paciente.getNumeroIdentificacionPersona()))
		{
			respuesta.setResultado(true);
			respuesta.setDescripcion("errors.usuario.autoatendido");
		}
			
			
		return respuesta;
	}
	
	/**
	 * Método para consultar el pérfil de farmacoterapia
	 * El HashMap resultante contiene las siguientes llaves:
	 * 	-- cod_axioma_art_"i"
	 *  -- cod_interfaz_art_"i"
	 *  -- nombre_art_"i"
	 *  -- dosis_"i"
	 *  -- frecuencia_"i"
	 *  -- via_"i"
	 *  -- dia_"i"
	 *  -- mes_"i"
	 *  -- paciente
	 *  -- entidad
	 *  -- nro_ingreso
	 *  -- fecha ingreso
	 *  -- nro_doc
	 *  -- sexo
	 *  -- edad
	 *  -- cama
	 *  -- numRegistros
	 * @param con
	 * @param codigoIngreso
	 * @return
	 */
	public static HashMap obtenerPerfilFarmacoterapia(Connection con, int codigoIngreso)
	{
		return utilidadesDao().obtenerPerfilFarmacoterapia(con, codigoIngreso);
	}
	
	/**
	 * Metodo encargado de consultar todos los ingresos con su informacion
	 * no importa el estado del ingreso o de la cuenta.
	 * @param con
	 * @param codigoPersona
	 * @return HashMap
	 *------------------------------------
	 * KEY'S DEL MAPA RESULTADO
	 *------------------------------------
	 * centroAtencion_,nomCentroAtencion_,
	 * viaIngreso_, numIngreso_,codigoIngreso_,
	 * fechaIngreso_, estadoIngreso_,
	 * nomEstadoIngreso_,numCuenta_, nomEstadoCuenta_,
	 * estadoCuenta_, fechaEgreso_
	 */
	public static HashMap consultarTodosIngresosXPaciente(Connection con, int codigoPersona)
	{
		return utilidadesDao().consultarTodosIngresosXPaciente(con, codigoPersona); 
	}
	
	/**
	 * Método para obtener los ultimos diagnósticos de un ingreso
	 * @param con
	 * @param campos
	 * @return
	 */
	public static ArrayList<Diagnostico> obtenerUltimosDiagnosticoIngreso(Connection con,int codigoIngreso, boolean complicacion)
	{
		HashMap campos = new HashMap();
		campos.put("codigoUltimaEvolucion", consultarUltimaValoracionIngresoNoPos(con, codigoIngreso));
		campos.put("complicacion", complicacion);
		campos.put("codigoUltimaValoracion", consultarUltimaValoracionIngreso(con, codigoIngreso));
		return utilidadesDao().obtenerUltimosDiagnosticoIngreso(con, campos);
	}
	
	/**
	 * Método para obtener los ultimos diagnósticos de un ingreso
	 * @param con
	 * @param campos
	 * @return
	 */
	public static HashMap obtenerInfoInstitucionXIngreso(Connection con, String numeroIngreso)
	{
		return utilidadesDao().obtenerInfoInstitucionXIngreso(con, numeroIngreso);
	}
	
	/**
	 * Método para consultar los datos de la especialidad
	 * @param con
	 * @param especialidad
	 * @return
	 */
	public static void consultarEspecialidad(Connection con,DtoEspecialidad especialidad)
	{
		utilidadesDao().consultarEspecialidad(con, especialidad);
	}
	
	/**
	 * 
	 * @param codigoPersona
	 * @return
	 */
	public static boolean pacienteTieneHisotircosOtrosSistemas(int codigoPersona)
	{
		return utilidadesDao().pacienteTieneHisotircosOtrosSistemas(codigoPersona);
	}
	
	/**
	 * Método que devuelve la ultima especialidad por la que fue
	 * valorado o evolucionado el paciente.
	 * @param con
	 * @param ingreso
	 * @return
	 */
	public static int ultimaEspecialidadEvolucionValoracionIngreso (Connection con, int ingreso)
	{
		int solicitud = 0;
		solicitud = consultarUltimaEvolucionIngreso(con, ingreso);
		if (solicitud == 0)
			solicitud = consultarUltimaValoracionIngreso(con, ingreso);
		logger.info("solicitud:::"+solicitud);
		if (solicitud != 0)
			return utilidadesDao().ultimaEspecialidadEvolucionValoracionIngreso(con, solicitud);
		
		return solicitud;
	}
	
	/**
	 * @return estado funcionalidad de antecedentes 
	 */
	public  static Integer  consultarEstadoFuncionalidadAntecedentes(){
		Connection con=UtilidadBD.abrirConexion();
		Integer estadoFuncionalidad=new Integer(0);
		try {
			estadoFuncionalidad=utilidadesDao().consultarEstadoFuncionalidadAntecedentes(con);
		} catch (Exception e) {
			logger.error("error consultando el estado de funcionalidad de antecedentes "+e.getMessage());
		}
		UtilidadBD.closeConnection(con);
		return estadoFuncionalidad;
	}
	
	/**
	 * @return estado funcionalidad de historia clinica 
	 */
	public  static Integer  consultarEstadoFuncionalidadHistoriaClincia(Connection con){
		Integer estadoFuncionalidad=new Integer(0);
		try {
			estadoFuncionalidad=utilidadesDao().consultarEstadoFuncionalidadHistoriaClincia(con);
		} catch (Exception e) {
			logger.error("error consultando el estado de funcionalidad de historia clinica  "+e.getMessage());
		}
		return estadoFuncionalidad;
	}
	
	/**
	 * Obtiene el nombre del monitoreo de acuerdo al codigo ingresado
	 * @param codigoMonitoreo
	 * @param con
	 * @return nombre del tipo de monitoreo 
	 * @throws Exception
	 */
	public static String consultarTipoMonitoreoXCodigo(Integer codigoMonitoreo,Connection con) throws Exception{
		return utilidadesDao().consultarTipoMonitoreoXCodigo(codigoMonitoreo, con);
	}
	
	/**
	 * Obtiene el nombre del centro de costos del monitoreo de acuerdo al codigo ingresado
	 * @param codigoCentroCostos
	 * @param con
	 * @return nombre del centro costos de monitoreo 
	 * @throws Exception
	 * mt5887
	 */
	public static String consultarCentroCostosMonitoreoXCodigo(Integer codigoCentroCostos,Connection con) throws Exception{
		return utilidadesDao().consultarCentroCostosMonitoreoXCodigo(codigoCentroCostos, con);
	}
	/**
	 * @param codigoEquivalente
	 * @param con
	 * @param ingreso
	 * @return medicamento original
	 * @throws Exception
	 */
	public static DtoMedicamentosOriginales consultarMedicamentosOriginales(Integer codigoEquivalente,Connection con,Integer ingreso )throws Exception
	{
		return utilidadesDao().consultarMedicamentosOriginales(codigoEquivalente, con, ingreso);
	}
	
	/**
	 * @param con
	 * @param numeroSolicitud
	 * @return obtienen unidad de medida
	 * @throws Exception
	 */
	public static  String obtenerUnidadMedidaMedicamentoPrincipal(Connection con, Integer numeroSolicitud, Integer codigoArt) throws Exception
	{
		return utilidadesDao().obtenerUnidadMedidaMedicamentoPrincipal(con, numeroSolicitud, codigoArt);
	}
	
	/**
	 * @param con
	 * @param numeroSolicitud
	 * @return servicios de hoja QX
	 * @throws Exception
	 */
	public static  ArrayList<String> serviciosHojaQuirurgica(Connection con, Integer numeroSolicitud)throws Exception
	{
		return utilidadesDao().serviciosHojaQuirurgica(con, numeroSolicitud);
	}
	
	/**
	 * @see com.princetonsa.dao.historiaClinica.UtilidadesHistoriaClinicaDao#obtenerTipoPaciente(java.sql.Connection, java.lang.String)
	 */
	public static  String obtenerTipoPaciente(Connection con,String numeroCuenta) throws Exception
	{
		return utilidadesDao().obtenerTipoPaciente(con, numeroCuenta);
	}
	
	/**
	 * @see com.princetonsa.dao.historiaClinica.UtilidadesHistoriaClinicaDao#especialidadUrgencia(java.sql.Connection, java.lang.String)
	 */
	public static String especialidadUrgencia(Connection con,String numeroCuenta) throws Exception
	{
		return utilidadesDao().especialidadUrgencia(con, numeroCuenta);
	}


	/**
	 * @see com.princetonsa.dao.historiaClinica.UtilidadesHistoriaClinicaDao#obtenerTipoPacienteHosp(java.sql.Connection, java.lang.String)
	 */
	public static String obtenerTipoPacienteHosp(Connection con,String numeroCuenta) throws Exception
	{
		return utilidadesDao().obtenerTipoPacienteHosp(con, numeroCuenta);
	}


	/**
	 * @see com.princetonsa.dao.historiaClinica.UtilidadesHistoriaClinicaDao#obtenerTipoPacienteConsultaExterna(java.sql.Connection, java.lang.String)
	 */
	public static String obtenerTipoPacienteConsultaExterna(Connection con,String numeroCuenta) throws Exception
	{
		return utilidadesDao().obtenerTipoPacienteConsultaExterna(con, numeroCuenta);
	}
	
	/**
	 * @param cadenaOriginal
	 * @return
	 */
	public static String darVueltaCadena(String cadenaOriginal){
		String[] tmp = cadenaOriginal.split(",");
		String[] tmp2 = new String[tmp.length];
		Integer cont=0;
		String res="";
		for(int i = (tmp.length -1); i >= 0; i--){
			tmp2[i]=tmp[cont];
			cont++;
		}
	
		for (int i = 0; i < tmp2.length; i++) {
			if(i!=tmp2.length-1){
			res+=tmp2[i]+",";
			}else{
				res+=tmp2[i];
			}
		}
		
		return res;
		
	}
	
	
	
	/**
	 * @see com.princetonsa.dao.historiaClinica.UtilidadesHistoriaClinicaDao#obtenerFechaMuerte(java.sql.Connection, java.lang.String)
	 */
	public static String obtenerFechaMuerte(Connection con,String codigoPaciente) throws Exception
	{
		return utilidadesDao().obtenerFechaMuerte(con, codigoPaciente);
	}
	
	
	/**
	 * @param con
	 * @param ingreso
	 * @return
	 */
	public static int consultarUltimaValoracionIngresoNoPos(Connection con,int ingreso)
	{
		return utilidadesDao().consultarUltimaValoracionIngresoNoPos(con, ingreso);
	}
	
	
}