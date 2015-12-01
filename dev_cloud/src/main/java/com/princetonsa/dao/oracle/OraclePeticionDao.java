/*
 * 22 Oct 2005
 *
 */
package com.princetonsa.dao.oracle;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.RespuestaHashMap;
import util.UtilidadBD;

import com.princetonsa.dao.PeticionDao;
import com.princetonsa.dao.postgresql.PostgresqlPeticionDao;
import com.princetonsa.dao.sqlbase.SqlBasePeticionDao;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.mundo.UsuarioBasico;

/**
 * @author Sebastián Gómez
 *
 * Clase que maneja los métodos propìos de Oracle para el acceso a la fuente
 * de datos en la funcionalidad Generar - Consultar - Modificar - Anular Petición
 */
public class OraclePeticionDao implements PeticionDao {

	/**
	 * Para hacer logs de debug / warn / error de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(PostgresqlPeticionDao.class);
	  

	/**
	 * Método implementado para cargar los datos generales de las peticiones de un paciente
	 * @param con
	 * @param paciente
	 * @param HashMap filtro
	 * @return
	 */
	public HashMap cargarDatosGeneralesPeticion(Connection con,int paciente,HashMap filtro)
	{
		return SqlBasePeticionDao.cargarDatosGeneralesPeticion(con,paciente,filtro);
		
	}
	
	

	/**
	 * Método que asigna el numero del pedido asociado a una petición
	 * @param con
	 * @param campos
	 * @return
	 */
	public int actualizarPedidoPeticion(Connection con,HashMap campos)
	{
		return SqlBasePeticionDao.actualizarPedidoPeticion(con,campos);
	}
	
	/**
	 * Método que asigna el numero del pedido asociado a una petición
	 * @param con
	 * @return
	 */
	public int insertarPedidoPeticion(Connection con,HashMap campos)
	{
		return SqlBasePeticionDao.insertarPedidoPeticion(con, campos);
	}
	
	/**
	 * Metodo para realizar la consulta de todas las peticiones de cirugia del sistema
	 * (si el codigoPeticion > 0 entonces restringe busqueda por este criterio)
	 * @param con
	 * @param codigoPaciente
	 * @param HashMap filtro
	 * @return
	 */
	public HashMap cargarDatosGeneralesPeticion2(Connection con, int codigoPaciente, int codigoPeticion, HashMap filtro)
	{
		return SqlBasePeticionDao.cargarDatosGeneralesPeticion2(con,codigoPaciente, codigoPeticion,filtro);
	}
	
	/**
	 * Método para conlsutar las peticionnes generales del Cirugias ó de un paciente Especifico.  
	 * @param con
	 * @param paciente
	 * @param Filtro 
	 * @param codigoCuenta --> Para saber si la cuenta está abierta o cerrada se utiliza en preanestesia
	 * 													  Si es igual a -1 se ignora esto
	 * @param codigoCentroAtencion --> Se utiliza en preantestesia para filtrar o no las peticiones de acuerdo a si está
	 * 														abierta o cerrada la cuenta del paciente
	 * @param HashMap filtro
	 * @return
	 */
	
	public HashMap cargarPeticionesCirugias(Connection con,int paciente, int filtro, int codigoCuenta, int codigoCentroAtencion, HashMap filtroMap)
	{
		return SqlBasePeticionDao.cargarPeticionesCirugias(con, paciente, filtro, codigoCuenta, codigoCentroAtencion, filtroMap);
	}
	
	/**
	 * Metodo para cargar el encabezado del detalle de la peticion de cirugia 
	 * @param con
	 * @param Nro Peticion
	 * @param HashMap filtros
	 * @return
	 */
	public HashMap cargarEncabezadoPeticion(Connection con, int nroPeticion, HashMap filtros) 
	{
		//logger.info("\n entro a cargarEncabezadoPeticion -->"+nroPeticion);
		
		String consulta =  " SELECT pqx.codigo as consecutivo, to_char(pqx.fecha_peticion, 'DD/MM/YYYY') as fecha_peticion," +
						   "        coalesce(pqx.tipo_anestesia||'','') as tipo_anestesia, coalesce(getnombretipoanestesia(pqx.tipo_anestesia),'') AS nombre_tipo_anestesia , " + 
				   		   "		substr(pqx.hora_peticion,1,5) as hora_peticion, pet.nombre as estado_peticion, pet.codigo AS codigo_estado_peticion, " + 
						   "		to_char(pqx.fecha_cirugia,'DD/MM/YYYY') as fecha_cirugia, pqx.duracion as duracion, " + 
						   "		getNombrePersona(pqx.solicitante) AS medico, pqx.solicitante AS codigo_medico, tpac.nombre as tipo_paciente, " + 
						   "		pqx.requiere_uci as requiere_uci, pqx.tipo_paciente AS codigo_tipo_paciente, " +
						   "		apqx.motivos_anulacion AS codigo_motivo_anulacion, mapqx.nombre AS nombre_motivo_anulacion, apqx.comentario AS comentario_anulacion, " +
						   "		psqx.sala AS sala, sala.descripcion AS nombre_sala,getsexopaciente(pqx.paciente) AS codigo_sexo," +
						   //pendiente cargar el list, arreglo rapido pruebas versalles 2 de la tarde
						   "        '' As pedido, pqx.obs_materiales_especiales AS observMaterEspe, pqx.urgente AS urgente, " +
						   "		ca.descripcion AS centro_atencion "+
						   "		FROM peticion_qx pqx " +
						   "			INNER JOIN estados_peticion pet ON ( pet.codigo =  pqx.estado_peticion ) " +
						   "			INNER JOIN centro_atencion ca ON ( ca.consecutivo =  pqx.centro_atencion) "+
						   "			LEFT OUTER JOIN tipos_paciente tpac ON ( tpac.acronimo = pqx.tipo_paciente ) " +    	
						   "			LEFT OUTER JOIN anulacion_peticion_qx apqx ON(apqx.peticion_qx=pqx.codigo) " +
						   "			LEFT OUTER JOIN motivo_anulacion_pet mapqx ON(mapqx.codigo=apqx.motivos_anulacion) " +
						   "			LEFT OUTER JOIN programacion_salas_qx psqx ON(psqx.peticion=pqx.codigo) " +
						   "			LEFT OUTER JOIN salas sala ON(psqx.sala=sala.consecutivo) " +
						   "			LEFT OUTER JOIN pedidos_peticiones_qx ppq ON(ppq.peticion=pqx.codigo) " +
						   "				WHERE pqx.codigo = ? " ;
						
		
		//columnas
		String[] columnas = {"consecutivo", "fecha_peticion", "hora_peticion", "estado_peticion", "codigo_estado_peticion","tipo_anestesia","nombre_tipo_anestesia",
							 "fecha_cirugia",  "duracion", "medico", "codigo_medico", "tipo_paciente", "requiere_uci",
							 "codigo_tipo_paciente", "codigo_motivo_anulacion","nombre_motivo_anulacion", "comentario_anulacion", "sala", "nombre_sala", "codigo_sexo","pedido", "observMaterEspe", "urgente","centro_atencion"};
		
		
		if(filtros!=null && filtros.containsKey("programable"))
			consulta+=" AND pqx.programable = '"+filtros.get("programable").toString()+"' ";		
		
		
		consulta+=" GROUP BY pqx.codigo,pqx.fecha_peticion,pqx.tipo_anestesia,pqx.hora_peticion,pet.nombre," +
				  "pet.codigo,pqx.fecha_cirugia,pqx.duracion,pqx.solicitante,tpac.nombre,pqx.requiere_uci,pqx.tipo_paciente," +
				  "apqx.motivos_anulacion,mapqx.nombre,apqx.comentario,psqx.sala,sala.descripcion,pqx.paciente, pqx.obs_materiales_especiales, pqx.urgente, ca.descripcion";
		logger.info("\n cadena -->"+consulta);
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,nroPeticion);
			RespuestaHashMap listado=UtilidadBD.resultSet2HashMap(columnas,new ResultSetDecorator(pst.executeQuery()),false,true);
			return listado.getMapa();
		}
		catch (SQLException e)
		{
			logger.error("Error en cargarEncabezadoPeticion de SqlBaseMaterialesQxDao: "+e);
			return null;
		}
	}
	
	
	/**
	 * Metodo para cargar los servicios de una peticion  
	 * @param con
	 * @param HashMap filtro
	 * @return
	 */
	public HashMap cargarServiciosPeticion(Connection con, int NroPeticion,int indicador, HashMap filtro)
	{

		if(indicador==0)
			return SqlBasePeticionDao.cargarServiciosPeticion2(con, NroPeticion,filtro);
		else
			return SqlBasePeticionDao.cargarServiciosPeticion(con,NroPeticion);
	}
	
	/**
	 * Metodo para cargar los profesionales de la peticion de cirugia 
	 * @param con
	 * @param numeroPeticion
	 * @param HashMap filtro
	 * @return
	 */
	public HashMap cargarProfesionalesPeticion(Connection con,int NroPeticion,HashMap filtro)
	{
		return SqlBasePeticionDao.cargarProfesionalesPeticion(con, NroPeticion,filtro);
	}
	
	/**
	 * Metodo para cargar los materiales de la peticion de cirugia 
	 * @param con
	 * @param numeroPeticion
	 * @param HashMap filtros
	 * @return
	 */
	public HashMap cargarMaterialesPeticion(Connection con,int NroPeticion, HashMap filtros)
	{
		return SqlBasePeticionDao.cargarMaterialesPeticion(con, NroPeticion, filtros);
	}
	
	
	
	/**
	 * Metodo para realizar la consulta de peticiones segun parametros 
	 * @param con
	 * @param nroIniServicio
	 * @param nroFinServicio
	 * @param fechaIniPeticion
	 * @param fechaFinServicio
	 * @param fechaIniCirugia
	 * @param fechaFinCirugia
	 * @param profesional
	 * @param estadoPeticion
	 * @param origen
	 * @param centroAtención --> Centro de atención seleccionado en la búsqueda
	 * @param codigoCentroAtencion --> Se utiliza para realizar el filtro de las peticiones por centro de atención en preanestesia,
	 * 																	del usuario en sesión
	 * @param String programable
	 * @return
	 */
	public HashMap consultarPeticiones(Connection con, int nroIniServicio, int nroFinServicio, String fechaIniPeticion, String fechaFinServicio,
													   String fechaIniCirugia, String fechaFinCirugia, int profesional, int estadoPeticion, String origen, int centroAtencion, int codigoCentroAtencion,
													   String programable)
	{
		return SqlBasePeticionDao.consultarPeticiones(con, nroIniServicio, nroFinServicio, fechaIniPeticion, fechaFinServicio,
													  fechaIniCirugia, fechaFinCirugia, profesional, estadoPeticion, origen, centroAtencion, codigoCentroAtencion,programable);
	}

	/**
	 * Método para insertar una petición de corugías en la BD a traves de HashMaps
	 * @param con Conexión con la BD
	 * @param mapaPeticionEncabezado HashMap con los datos del encabezado
	 * @param mapaPeticionServicios HashMap con los datos de los servicios
	 * @param mapaPeticionProfesionales HashMap con los datos de los profesionales participantes
	 * @param mapaPeticionMateriales HashMap con los datos de los materiales especiales
	 * @param codigoPersona Persona a la cual se le desea ingresar la petición
	 * @param usuario Usuario del sistema
	 * @param esContinuarTransaccion, boolean que indica si la transaccion ya fue inicializada
	 * @return Número de inserciónes en la BD (posición 0) y codigo de la petición (posición 1)
	 */
	public int[] insertar( Connection con, 
									HashMap mapaPeticionEncabezado, 
									HashMap mapaPeticionServicios, 
									HashMap mapaPeticionProfesionales, 
									HashMap mapaPeticionMateriales, 
									int codigoPersona,
									int idIngreso,
									UsuarioBasico usuario,
									boolean esContinuarTransaccion, 
									boolean esModificar)
	{
		return SqlBasePeticionDao.insertar(con, mapaPeticionEncabezado, mapaPeticionServicios, mapaPeticionProfesionales, mapaPeticionMateriales, 
				codigoPersona, idIngreso, usuario, esContinuarTransaccion, esModificar);
	}

	/**
	 * Metodo que carga los servicios dados los codigos de peticiones
	 * @param con
	 * @param codigosPeticionesSeparadosPorComas
	 * @return
	 */
	public HashMap cargarServiciosDadasPeticiones(Connection con, String codigosPeticionesSeparadosPorComas)
	{
		return SqlBasePeticionDao.cargarServiciosDadasPeticiones(con, codigosPeticionesSeparadosPorComas);
	}
	
	/**
	 * Metodo que carga los materiales especiales PARAMETRIZADOS de una peticion
	 * @param con
	 * @param codigoPeticion
	 * @return
	 */
	public HashMap cargarMaterialesEspeciales2(Connection con, String codigoPeticion)
	{
		return SqlBasePeticionDao.cargarMaterialesEspeciales2(con, codigoPeticion);
	}
	
	/**
	 * Metodo que actualiza (fecha Estimada Cx- Duracion Aprox) informacion de la peticion
	 * @param con
	 * @param fechaEstimadaCirugia
	 * @param duracion
	 * @param codigoPeticion
     * @param requiereUci
	 * @return
	 */
	public boolean actualizarFechaDuracionRequiereUciPeticion(Connection con, String fechaEstimadaCirugia, String duracion, String codigoPeticion, String requiereUci)
	{
		return SqlBasePeticionDao.actualizarFechaDuracionRequiereUciPeticion(con, fechaEstimadaCirugia, duracion, codigoPeticion, requiereUci);
	}
	 
	/**
	 * metodo que carga el encabezaod de la peticion sin restrcciones, a menos de que se especifiquen los
	 * codigos de la peticion.
	 * @param con
	 * @param codigosPeticionesSeparadosPorComas
	 * @param HashMap filtros
	 * @return
	 */
	public Collection cargarEncabezadoPeticionSinRestricciones(Connection con, String codigosPeticionesSeparadosPorComas, HashMap filtros)
	{
		return SqlBasePeticionDao.cargarEncabezadoPeticionSinRestricciones(con, codigosPeticionesSeparadosPorComas,filtros);
	}
	 
	/**
	 * Método para anular la petición de cirugías
	 * @param con
	 * @param numeroPeticion
	 * @param motivoAnulacion
	 * @return Mayor que 0 si la anulación fue correcta
	 */
	public int anularPeticion(Connection con, int numeroPeticion, int motivoAnulacion, String comentario, String loginUsuario)
	{
		return SqlBasePeticionDao.anularPeticion(con, numeroPeticion, motivoAnulacion, comentario, loginUsuario);
	}

       /**
     * metodo que actualiza el estado de la peticion
     * @param con
     * @param codigoEstado
     * @param codigoPeticion
     * @return
     */
    public boolean actualizarEstadoPeticion(Connection con, int codigoEstado, String codigoPeticion)
    {
        return SqlBasePeticionDao.actualizarEstadoPeticion(con, codigoEstado, codigoPeticion);
    }
    
    /**
	 * Método que consulta lso apellidos y nombre del paciente de la peticion
	 * @param con
	 * @param codigoPeticion
	 * @return
	 */
	public String getApellidosNombresPacientePeticion(Connection con,int codigoPeticion)
	{
		return SqlBasePeticionDao.getApellidosNombresPacientePeticion(con, codigoPeticion);
	}	 



	/** (non-Javadoc)
	 * @see com.princetonsa.dao.PeticionDao#desAsociarPeticionSolicitud(java.sql.Connection, int)
	 */
	@Override
	public boolean desAsociarPeticionSolicitud(Connection con,
			int numeroSolicitud) {
		return SqlBasePeticionDao.desAsociarPeticionSolicitud(con, numeroSolicitud);
	}	 
}