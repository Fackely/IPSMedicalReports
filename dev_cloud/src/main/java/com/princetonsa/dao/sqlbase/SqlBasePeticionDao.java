/*
 * 22 Oct 2005
 *
 */
package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.Collection;
import java.util.HashMap;
import org.apache.log4j.Logger;
import org.axioma.util.log.Log4JManager;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.UsuarioBasico;
import util.ConstantesBD;
import util.RespuestaHashMap;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

/**
 * @author Sebastián Gómez
 *
 * Objeto usado para manejar los accesos comunes a la fuente de datos
 * para la funcionalidad de Generar - Consultar - Modificar - Anular Petición
 */
public class SqlBasePeticionDao {
	
	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger=Logger.getLogger(SqlBasePeticionDao.class);
	
	
	/**
	 * Sección SELECT-FROM para la consulta de las peticiones del paciente
	 */
	private static final String cargarDatosGeneralesPeticionSELECT = "SELECT "+
		"p.codigo AS peticion,"+
		"to_char(p.fecha_peticion,'DD/MM/YYYY') AS fecha_peticion, " +
		"coalesce(getconsecutivoingreso(c.id_ingreso),'') AS numero_ingreso, " +
		"substr(p.hora_peticion,0,6) AS hora_peticion,  "+
		"p.solicitante AS codigo_medico_solicita,"+
		"getnombrepersona(p.solicitante) AS medico_solicita,"+
		"to_char(p.fecha_cirugia,'DD/MM/YYYY') AS fecha_cirugia, " +
		"getNomEstadoPeticion(p.estado_peticion) AS estado , "+
		"CASE WHEN s.consecutivo_ordenes_medicas IS NULL THEN 0 ELSE s.consecutivo_ordenes_medicas END AS orden,"+
		"CASE WHEN s.estado_historia_clinica = "+ConstantesBD.codigoEstadoHCInterpretada+" THEN coalesce(getnombrepersona(usu.codigo_persona),'')  ELSE '' END AS medico_responde, "+
		"CASE WHEN s.estado_historia_clinica = "+ConstantesBD.codigoEstadoHCInterpretada+" THEN coalesce(usu.codigo_persona,0)  ELSE 0 END AS codigo_medico_responde," +
		"p.programable "+		
		"FROM peticion_qx p " +
		"LEFT OUTER JOIN solicitudes_cirugia sc ON(sc.codigo_peticion=p.codigo) "+
		"LEFT OUTER JOIN solicitudes s ON(sc.numero_solicitud=s.numero_solicitud) "+
		"LEFT OUTER JOIN cuentas c ON(c.id=s.cuenta) "+
		"LEFT OUTER JOIN usuarios usu ON(usu.login=sc.usuario_grabacion_salida) ";
	
	
	/**
	 * Sección WHERE para la consulta de las peticiones del paciente
	 */
	private static final String cargarDatosGeneralesPeticionWHERE = "WHERE " +
		"p.paciente=? AND " +
		"(   p.estado_peticion = "+ConstantesBD.codigoEstadoPeticionPendiente+" OR " +
			"p.estado_peticion = "+ConstantesBD.codigoEstadoPeticionProgramada+" OR " +
			"p.estado_peticion = "+ConstantesBD.codigoEstadoPeticionReprogramada+" OR " +
			"(p.estado_peticion = "+ConstantesBD.codigoEstadoPeticionAtendida+" AND esSolicitudTotalPendiente(s.numero_solicitud) = '"+ConstantesBD.acronimoSi+"')  ) AND " +
		"(sc.numero_solicitud IS NULL OR (tieneConsumoMatPendiente(sc.numero_solicitud) = '"+ConstantesBD.acronimoSi+"' AND s.estado_historia_clinica not in ("+ConstantesBD.codigoEstadoHCAnulada+"))) AND " +
		"(c.id IS NULL OR c.estado_cuenta not in ("+ConstantesBD.codigoEstadoCuentaCerrada+")) ";
	
	
	/**
	 * Sección ORDER para la consulta de las peticiones del paciente
	 */
	private static final String cargarDatosGeneralesPeticionORDER =	"ORDER BY p.fecha_cirugia ";
	
	/**
	 * Seccion SELECT-FORM para la consulta de los servicios de la petición
	 */
	private static final String cargarServiciosPeticionSELECT = "SELECT " +
			"ps.servicio AS codigo_servicio," +
			"getnombreservicio(ps.servicio,"+ConstantesBD.codigoTarifarioCups+") AS servicio, " +
			"ps.numero_servicio AS numero_servicio, " +
			"ps.cubierto AS cubierto, " +
			"ps.contrato_convenio AS contrato_convenio "+
			"FROM peticiones_servicio ps ";
	
	/**
	 * Sección EHERE para la consulta de los servicios de la petición
	 */
	private static final String cargarServiciosPeticionWHERE = "WHERE ps.peticion_qx=? ORDER BY ps.numero_servicio";
	
	/**
	 * Cadena que actualiza el pedido de una peticion
	 */
	private static final String actualizarPedidoPeticionStr= "UPDATE pedidos_peticiones_qx  SET fecha_modificacion = CURRENT_DATE, hora_modificacion = "+ValoresPorDefecto.getSentenciaHoraActualBD()+", usuario_modificacion = ? WHERE pedido=? AND peticion = ?";
	
	/**
	 * Cadena que inserta el pedido de una peticion
	 */
	private static final String insertarPedidoPeticionStr = "INSERT INTO pedidos_peticiones_qx (pedido,peticion,fecha_modificacion,hora_modificacion,usuario_modificacion) VALUES (?,?,CURRENT_DATE,"+ValoresPorDefecto.getSentenciaHoraActualBD()+",?)";
	
	/**
	 * Sentencia para insertar la peticion de cirugía 71212
	 */
	private static final String insertarPeticionQxStr="INSERT INTO peticion_qx(codigo, paciente, tipo_paciente, fecha_peticion, hora_peticion, duracion, solicitante, fecha_cirugia, usuario, estado_peticion, requiere_uci, institucion, centro_atencion,tipo_anestesia,programable, obs_materiales_especiales, ingreso, urgente, acronimo_diagnostico, tipo_cie_diagnostico) VALUES(?,?,?,?,?,?,?,?,?,"+ConstantesBD.codigoEstadoPeticionPendiente+",?,?,?,?,?,?,?,?,?,?)";

	/**
	 * Sentencia para insertar la peticion de cirugía
	 */
	private static final String updatePeticionQxStr="UPDATE peticion_qx SET tipo_paciente=?, duracion=?, solicitante=?, fecha_cirugia=?, requiere_uci=?,tipo_anestesia=?, obs_materiales_especiales=?, urgente=? WHERE codigo=?";

	/**
	 * Sentencia para ingresar los participantes de la petición
	 */
	private static final String insertarParticipantesStr="INSERT INTO prof_partici_peticion_qx(peticion_qx, codigo_medico, tipo_participante, especialidad) VALUES(?,?,?,?)";
	
	/**
	 * Sentencia para ingresar los materiales de la petición
	 */
	private static final String insertarMaterialesStr="INSERT INTO articulos_peticion_qx(articulo, peticion_qx, cantidad) VALUES(?,?,?)";
	
	/**
	 * Sentencia para ingresar los servicios de la petición
	 */
	private static final String insertarServiciosStr="INSERT INTO peticiones_servicio(peticion_qx, servicio, numero_servicio, observaciones, cubierto, contrato_convenio) VALUES(?,?,?,?,?,?)";
	
	
    /**
     * carga los servicios de una peticion
     */
    private static final String cargarServiciosDadasPeticionesStr= "SELECT DISTINCT " +
                                                                                            "ps.servicio AS codigoServicio, " +
                                                                                            "ps.numero_servicio AS numeroServicio, " +
                                                                                            "rs.codigo_propietario AS codigoCups, " +
                                                                                            "s.especialidad ||'-'|| ps.servicio  ||' '|| rs.descripcion ||' - '|| CASE WHEN s.espos ="+ValoresPorDefecto.getValorTrueParaConsultas()+" THEN 'POS' ELSE 'NOPOS' END AS descripcionServicio, " +
                                                                                            "s.especialidad AS codigoEspecialidad, " +
                                                                                            "e.nombre AS descripcionEspecialidad, " +
                                                                                            "CASE WHEN s.espos ="+ValoresPorDefecto.getValorTrueParaConsultas()+" THEN 'POS' ELSE 'NOPOS' END AS esPos, " +
                                                                                            "-1 AS codigoCirujano, " + //-1 porque no existe en la peticion pero en la orden si
                                                                                            "-1 AS codigoAyudante, " + //-1 porque no existe en la peticion pero en la orden si
                                                                                            "'false' AS fueEliminadoServicio, " +
                                                                                            "0 AS valor_unitario, " +
                                                                                            "CASE WHEN ps.observaciones IS NULL THEN '' ELSE ps.observaciones END AS observaciones  " +
                                                                                            "FROM " +
                                                                                            "peticiones_servicio ps " +
                                                                                            "INNER JOIN peticion_qx pq ON (ps.peticion_qx= pq.codigo) " +
                                                                                            "INNER JOIN referencias_servicio rs ON (rs.servicio=ps.servicio) " +
                                                                                            "INNER JOIN servicios s ON (s.codigo = ps.servicio) " +
                                                                                            "INNER JOIN especialidades e ON (e.codigo=s.especialidad) ";
    
    /**
     * carga los materiales especiales PARAMETRIZADOS dada la peticion
     */
    private static final String cargarMaterialesEspeciales2Str= "SELECT " +
                                                                    "va.codigo AS codigoArticulo, " +
                                                                    "va.descripcion ||'-CONC:'|| va.concentracion ||'-F.F:'|| getNomFormaFarmaceutica(va.forma_farmaceutica) ||'-'|| CASE WHEN va.es_pos= "+ValoresPorDefecto.getValorTrueParaConsultas()+" THEN 'POS' ELSE 'NOPOS' END AS descripcionArticulo, " +
                                                                    "getNomUnidadMedida(va.unidad_medida) AS unidadMedidaArticulo, " +
                                                                    "ap.cantidad  AS cantidadDespachadaArticulo, " +
                                                                    "'' AS autorizacionArticulo, " +
                                                                    "'false' AS fueEliminadoArticulo, " +
                                                                    "CASE WHEN es_pos= "+ValoresPorDefecto.getValorTrueParaConsultas()+" THEN 'POS' ELSE 'NOPOS' END AS tipoPosArticulo " +
                                                                    "FROM view_articulos va, " +
                                                                    "articulos_peticion_qx ap " +
                                                                    "where " +
                                                                    "va.codigo=ap.articulo " +
                                                                    "AND va.estado="+ValoresPorDefecto.getValorTrueParaConsultas()+" " +
                                                                    "AND ap.peticion_qx=? ";
    
    /**
     * Cadena que actualiza (fecha Estimada Cx- Duracion Aprox - requiere uci) informacion de la peticion
     */
    private static final String actualizarFechaDuracionRequiereUciPeticionStr= "UPDATE peticion_qx SET fecha_cirugia=?, duracion=?, requiere_uci=? WHERE codigo=?";
    
    /**
     * carga informacion del encabezado de la peticion sin restrcciones
     */
    private static final String cargarEncabezadoPeticionSinRestriccionesStr = "SELECT " +
                                                                                "pq.codigo AS consecutivoPeticion, " +
                                                                                "to_char(pq.fecha_peticion, 'DD/MM/YYYY') AS fechaPeticion, " +
                                                                                "coalesce(pq.tipo_anestesia||'','') AS tipoAnestesia, "+
                                                                                "CASE WHEN pq.fecha_cirugia IS NULL THEN '' ELSE to_char(pq.fecha_cirugia,'DD/MM/YYYY') END AS fechaEstimadaCirugia, " +
                                                                                "p.primer_apellido ||' '|| p.segundo_apellido ||' ' || p.primer_nombre ||' '|| p.segundo_nombre AS profesionalSolicita, " +
                                                                                "ep.nombre AS estadoPeticion, " +
                                                                                "pq.obs_materiales_especiales AS observMaterEspe " +
                                                                                "FROM " +
                                                                                "peticion_qx pq, " +
                                                                                "medicos m, " +
                                                                                "personas p, " +
                                                                                "estados_peticion ep " +
                                                                                "WHERE " +
                                                                                "pq.solicitante=m.codigo_medico " +
                                                                                "AND m.codigo_medico=p.codigo " +
                                                                                "AND ep.codigo= pq.estado_peticion ";
    
    /**
     * actualiza el estado de la peticion
     */
    private static final String actualizarEstadoPeticionStr="UPDATE peticion_qx set estado_peticion=? where codigo=?";
    
  	/**
	 * Método implementado para cargar los datos generales de las peticiones de un paciente
	 * @param con
	 * @param paciente
	 * @param HashMap filtros
	 * @return
	 */
	public static HashMap cargarDatosGeneralesPeticion(Connection con,int paciente,HashMap filtros)
	{
		//columnas
		String[] columnas={
				"peticion",
				"numero_ingreso",
				"fecha_peticion",
				"hora_peticion",
				"estado",
				"codigo_medico_solicita",
				"medico_solicita",
				"fecha_cirugia",
				"orden",
				"medico_responde",
				"codigo_medico_responde"
				};
		try
		{
			String consulta = cargarDatosGeneralesPeticionSELECT + cargarDatosGeneralesPeticionWHERE ;
			
			if(filtros!=null && filtros.containsKey("programable"))			
				consulta+= " AND p.programable ='"+filtros.get("programable").toString()+"' ";
			
			consulta +=	cargarDatosGeneralesPeticionORDER;
			
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,paciente);
			logger.info("consulta peticion=> "+consulta.replace("?", paciente+""));
			RespuestaHashMap listado=UtilidadBD.resultSet2HashMap(columnas,new ResultSetDecorator(pst.executeQuery()),false,true);
			return listado.getMapa();
			
		}
		catch(SQLException e)
		{
			logger.error("Error en cargarDatosGeneralesPeticion: "+e);
			return null;
		}
	}
	
	/**
	 * Método implementado para consultar los servicios de una petición
	 * @param con
	 * @param numeroPeticion
	 * @return
	 */
	public static HashMap cargarServiciosPeticion(Connection con,int numeroPeticion)
	{
		logger.info("\n entre a cargarServiciosPeticion peticion -->"+numeroPeticion);
		try
		{
			String consulta = cargarServiciosPeticionSELECT + cargarServiciosPeticionWHERE;
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,numeroPeticion);
			HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()),true,false);
			pst.close();
			return mapaRetorno;
		}
		catch(SQLException e)
		{
			logger.error("Error en cargarServiciosPeticion de SqlBasePeticionDao: "+e);
			return null;
		}
	}
	
	/**
	 * Método que asigna el numero del pedido asociado a una petición
	 * @param con
	 * @param campos
	 * @return
	 */
	public static int actualizarPedidoPeticion(Connection con,HashMap campos)
	{
		try
		{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(actualizarPedidoPeticionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setObject(1,campos.get("usuario"));
			pst.setObject(2,campos.get("numeroPedido"));
			pst.setObject(3,campos.get("numeroPeticion"));
			
			return pst.executeUpdate();
		}
		catch(SQLException e)
		{
			logger.error("Error en actualizarPedidoPeticion de SqlBasePeticionDao: "+e);
			return -1;
		}
	}
	
	/**
	 * Método que asigna el numero del pedido asociado a una petición
	 * @param con
	 * @return
	 */
	public static int insertarPedidoPeticion(Connection con,HashMap campos)
	{
		try
		{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(insertarPedidoPeticionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			logger.info("numeroPedido=> "+campos.get("numeroPedido"));
			pst.setObject(1,campos.get("numeroPedido"));
			pst.setObject(2,campos.get("numeroPeticion"));
			pst.setObject(3,campos.get("usuario"));
			
			return pst.executeUpdate();
		}
		catch(SQLException e)
		{
			logger.error("Error en insertarPedidoPeticion de SqlBasePeticionDao: "+e);
			return -1;
		}
	}
	
    /**
     * metodo que actualiza el estado de la peticion
     * @param con
     * @param codigoEstado
     * @param codigoPeticion
     * @return
     */
    public static boolean actualizarEstadoPeticion(Connection con, int codigoEstado, String codigoPeticion)
    {
        try
        {
            PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(actualizarEstadoPeticionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            pst.setInt(1,codigoEstado);
            pst.setString(2, codigoPeticion);
            
            if(pst.executeUpdate()>0)
                return true;
            else
                return false;
        }
        catch(SQLException e)
        {
            logger.error("Error en actualizarEstadoPeticion de SqlBasePeticionDao: "+e);
            return false;
        }
    }
    
	/**
	 * Método para conlsutar los datos generales de una peticion de un paciente especifico
     * (si el codigoPeticion > 0 entonces restringe busqueda por este criterio)
	 * @param con
	 * @param paciente
	 * @param HashMap filtros
	 * @return
	 */
	public static HashMap cargarDatosGeneralesPeticion2(Connection con,int paciente, int codigoPeticion, HashMap filtros)
	{
		String consulta =  " SELECT to_char(pqx.fecha_peticion,'yyyy-mm-dd') as fecha_peticion, pqx.hora_peticion as hora_peticion, " +
 		 				   "		pqx.codigo as codigo, pet.nombre as estado_peticion, to_char(pqx.fecha_cirugia,'yyyy-mm-dd') as fecha_cirugia," +
 		 				   "		pqx.duracion as duracion, getNombreUsuario(pqx.usuario) AS medico, " +
						   "		tpac.nombre as nombre, pqx.requiere_uci as requiere_uci,pqx.tipo_anestesia as tipoanestesia,getnombretipoanestesia(tipo_anestesia) as nombretipoanestesia, pqx.solicitante AS codigo_medico, pet.codigo AS codigo_estado" +
						   " FROM peticion_qx pqx " +
	 			 		   " INNER JOIN estados_peticion pet ON ( pet.codigo =  pqx.estado_peticion ) " +
	 			 		   " LEFT OUTER JOIN tipos_paciente tpac ON ( tpac.acronimo = pqx.tipo_paciente ) " +	
	 			 		   " WHERE 1=1  ";
		
        if(paciente>0)
        {
            consulta+=" AND pqx.paciente = ? ";
        }
        
        if(codigoPeticion>0)
        {
            consulta+=" AND pqx.codigo = "+codigoPeticion;           
        }
        
        if(filtros!=null && filtros.containsKey("programable"))
        	consulta+=" AND pqx.programable = '"+filtros.get("programable").toString()+"' ";
        
		//columnas
		String[] columnas = {"fecha_peticion", "hora_peticion", "codigo", "estado_peticion",
							 "fecha_cirugia",  "duracion", "medico", "nombre",	"requiere_uci",
							 "tipoanestesia","nombretipoanestesia","codigo_medico", "codigo_estado"
							 };
		
		try
		{
			logger.info("-->"+consulta+"---"+paciente);
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            if(paciente>0)
                pst.setInt(1,paciente);
                
			RespuestaHashMap listado=UtilidadBD.resultSet2HashMap(columnas,new ResultSetDecorator(pst.executeQuery()),false,true);
			return listado.getMapa();
		}
		catch (SQLException e)
		{
			logger.error("Error en cargarDatosGeneralesPeticion2 de SqlBaseMaterialesQxDao: "+e);
			return null;
		}
	}
	
	
	/**
	 * Método para conlsutar las peticionnes generales del Cirugias de un paciente Especifico.  
	 * @param con
	 * @param paciente
	 * @param filtro
	 * @param codigoCuenta --> Para saber si la cuenta está abierta o cerrada se utiliza en preanestesia
	 * 													  Si es igual a -1 se ignora esto
	 * @param codigoCentroAtencion --> Se utiliza en preantestesia para filtrar o no las peticiones de acuerdo a si está
	 * 														abierta o cerrada la cuenta del paciente
	 * @param HashMap filtros
	 * @return
	 */ 
	public static HashMap cargarPeticionesCirugias(Connection con,int paciente, int filtro, int codigoCuenta, int codigoCentroAtencion, HashMap filtrosMap)
	{
		StringBuffer consulta = new StringBuffer();
		consulta.append("  SELECT pqx.codigo as codigo_peticion, to_char(pqx.fecha_peticion,'DD/MM/YYYY')  as fecha_peticion, " +
			 			" 		  to_char(pqx.fecha_cirugia,'DD/MM/YYYY') as fecha_cirugia, getNombrePersona(med.codigo_medico) as medico, med.codigo_medico AS codigo_medico, " +
			 			"		  ep.nombre as estado_peticion, ep.codigo AS codigo_estado_peticion, ca.descripcion as centro_atencion" +
						"		  FROM peticion_qx pqx " +    
			 			"			   INNER JOIN estados_peticion ep ON ( ep.codigo = pqx.estado_peticion ) " +
						"			   INNER JOIN medicos med ON ( med.codigo_medico =  pqx.solicitante )" +
						"			   INNER JOIN centro_atencion ca ON (ca.consecutivo = pqx.centro_atencion )" +
						"			   WHERE pqx.paciente =  " + paciente+" ");
						//" 			   AND getIndQxPeticion(pqx.codigo) IN ('"+ConstantesIntegridadDominio.acronimoIndicativoCargoCirugia+"','"+ConstantesIntegridadDominio.acronimoIndicativoCargoNoCruento+"') ");
		
		//---Si el codigo de la cuenta es diferente de -1 y de 0, es porque la cuenta del paciente está abierta y el origen es de preanestesia, entonces
		//--se filtran las peticiones por el centro de atención del usuario en sesión
		if(codigoCuenta!=-1 && codigoCuenta!=0)
		{
			consulta.append(" AND pqx.centro_atencion="+codigoCentroAtencion);
		}
				
		if(filtrosMap!=null && filtrosMap.containsKey("programable"))
			consulta.append(" AND pqx.programable = '"+filtrosMap.get("programable").toString()+"' ");
		
		//columnas
		String[] columnas = {"codigo_peticion", "fecha_peticion", "fecha_cirugia", "medico", "codigo_medico", "estado_peticion", "codigo_estado_peticion", "centro_atencion"};
		
		try
		{

			if ( filtro == 1 ) //-Filtrar la tabla
			  consulta.append(" AND (pqx.estado_peticion = " + ConstantesBD.codigoEstadoPeticionPendiente+" OR pqx.estado_peticion = "+ConstantesBD.codigoEstadoPeticionProgramada+" OR pqx.estado_peticion = "+ConstantesBD.codigoEstadoPeticionReprogramada+")");	

			consulta.append(" ORDER BY pqx.codigo");			
			
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta.toString(),ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			RespuestaHashMap listado=UtilidadBD.resultSet2HashMap(columnas,new ResultSetDecorator(pst.executeQuery()),false,true);
			return listado.getMapa();
		}
		catch (SQLException e)
		{
			logger.error("Error en cargarDatosGeneralesPeticion2 de SqlBaseMaterialesQxDao: "+e);
			return null;
		}
	}
	
	
	/**
	 * Metodo para cargar los servicios de una peticion especifica de un paciente determinado
	 * @param con
	 * @param numeroPeticion
	 * @param HashMap filtros
	 * @return
	 */
	public static HashMap cargarServiciosPeticion2(Connection con,int numeroPeticion, HashMap filtros)
	{
		logger.info("\n entre a cargarServiciosPeticion2 peticion -->"+numeroPeticion);
		
		String consulta = " SELECT ps.numero_servicio as numero_servicio, " +
				"rser.codigo_propietario as codigo_propietario, " +
				"ser.codigo AS codigo_servicio," +    
		   		"ser.codigo || '-' || ser.especialidad || ' ' || rser.descripcion || ' ' ||  esp.nombre as servicio, " +    
				"esp.nombre as especialidad, " +
				"ser.espos AS es_pos, " +
				"ps.cubierto AS cubierto, " +
				"ps.contrato_convenio AS contrato_convenio, "+
				"ps.observaciones  as observaciones " +
						  "		   FROM peticion_qx pqx " +    
						  "				INNER JOIN peticiones_servicio ps ON ( ps.peticion_qx  = pqx.codigo ) " +    
						  "				INNER JOIN servicios ser ON ( ser.codigo = ps.servicio ) " +     
						  "				INNER JOIN referencias_servicio rser ON ( rser.servicio = ser.codigo ) " +        
						  "				INNER JOIN especialidades esp ON ( esp.codigo = ser.especialidad ) " +           
   						  "							WHERE rser.tipo_tarifario = " + ConstantesBD.codigoTarifarioCups +    
   						  "							  AND pqx.codigo = ? "; 					   

		//columnas
		String[] columnas={	"numero_servicio", "codigo_propietario", "codigo_servicio", "servicio",
				"especialidad", "es_pos", "cubierto", "contrato_convenio", "observaciones"};
		
		if(filtros!=null && filtros.containsKey("programable"))
			consulta+=" AND pqx.programable = '"+filtros.get("programable").toString()+"' ";
		
		consulta+=" ORDER BY ps.numero_servicio   ";
		logger.info("\n cadena -->"+consulta);
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,numeroPeticion);
			RespuestaHashMap listado=UtilidadBD.resultSet2HashMap(columnas,new ResultSetDecorator(pst.executeQuery()),false,true);
			return listado.getMapa();
		}
		catch(SQLException e)
		{
			logger.error("Error en cargarServiciosPeticion2 de SqlBasePeticionDao: "+e);
			return null;
		}
	}

	/**
	 * Metodo para cargar los profesionales de la peticion de cirugia 
	 * @param con
	 * @param numeroPeticion
	 * @param HashMap filtros
	 * @return
	 */
	public static HashMap cargarProfesionalesPeticion(Connection con,int numeroPeticion, HashMap filtros)
	{
		String consulta = " 	SELECT  pppq.codigo_medico AS codigo_medico, getNombrePersona (pppq.codigo_medico) as medico, " +
				   		  " 			esp.nombre as especialidad, esp.codigo AS codigo_especialidad, tpqx.nombre as tipo_profesional, tpiq.codigo AS codigo_tipo_profesional" +    
				   		  "				FROM peticion_qx pqx " +             
				   		  "					 INNER JOIN prof_partici_peticion_qx pppq ON (pqx.codigo = pppq.peticion_qx) " +
				   		  "					 INNER JOIN medicos med ON (pppq.codigo_medico = med.codigo_medico)" +             
				   		  "					 INNER JOIN especialidades esp ON (pppq.especialidad = esp.codigo) " +
				   		  " 				 INNER JOIN tipos_participantes_inst_qx tpiq ON (tpiq.codigo = pppq.tipo_participante) " +    
				   		  "					 INNER JOIN tipos_participantes_qx tpqx ON (tpqx.codigo = tpiq.tipo_profesional) " +
						  "								WHERE pqx.codigo = ? " ;
						  
		
		//columnas
		String[] columnas={	"codigo_medico", "medico", "especialidad", "codigo_especialidad", "tipo_profesional", "codigo_tipo_profesional"};
		
		
		if(filtros!=null && filtros.containsKey("programable"))
			consulta+=" AND pqx.programable = '"+filtros.get("programable").toString()+"' ";
		
		consulta+=" AND tpiq.activo = " + ValoresPorDefecto.getValorTrueParaConsultas();;
		
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,numeroPeticion);
			RespuestaHashMap listado=UtilidadBD.resultSet2HashMap(columnas,new ResultSetDecorator(pst.executeQuery()),false,true);
			return listado.getMapa();
		}
		catch(SQLException e)
		{
			logger.error("Error en cargarProfesionalesPeticion de SqlBasePeticionDao: "+e);
			return null;
		}
	}
	
	
	/**
	 * Metodo para cargar los materiales de una peticion especifica de un paciente determinado
	 * @param con
	 * @param numeroPeticion
	 * @param HashMap filtros
	 * @return
	 */
	public static HashMap cargarMaterialesPeticion(Connection con,int numeroPeticion, HashMap filtros)
	{
		
		String consulta = "  SELECT  art.codigo AS codigo_articulo, " +
				"art.codigo || '-' || coalesce(art.descripcion,'') || '-' || coalesce(art.concentracion,'') || '-' || coalesce(art.forma_farmaceutica,'') || '-' || coalesce(um.acronimo,'') as articulo, " +
								"um.nombre AS unidad_medida, " +
								" apqx.cantidad as cantidad, na.es_pos as es_pos " +
						  "				FROM peticion_qx pqx " +
						  "					 INNER JOIN articulos_peticion_qx apqx ON ( apqx.peticion_qx = pqx.codigo ) " +
						  "					 INNER JOIN articulo art ON ( art.codigo = apqx.articulo ) " +
						  "					 INNER JOIN unidad_medida um ON ( art.unidad_medida = um.acronimo ) " +
						  "					 INNER JOIN naturaleza_articulo na ON ( na.acronimo = art.naturaleza ) " +
						  "							WHERE pqx.codigo = ? ";
		//columnas
		String[] columnas={	"codigo_articulo", "articulo", "unidad_medida", "cantidad", "es_pos"};
		
		if(filtros!=null && filtros.containsKey("programable"))
			consulta+=" AND pqx.programable = '"+filtros.get("programable").toString()+"' ";
		
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,numeroPeticion);
			
			RespuestaHashMap listado=UtilidadBD.resultSet2HashMap(columnas,new ResultSetDecorator(pst.executeQuery()),false,true);
			return listado.getMapa();
		}
		catch(SQLException e)
		{
			logger.error("Error en cargarMaterialesPeticion de SqlBasePeticionDao: "+e);
			return null;
		}
	}

	/**
	 * Metodo para realizar la consulta de peticiones segun parametros  de consulta 
	 * @param con
	 * @param nroIniServicio
	 * @param nroFinServicio
	 * @param fechaIniPeticion
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
	public static HashMap consultarPeticiones(Connection con, int nroIniServicio, int nroFinServicio, String fechaIniPeticion,
											  String fechaFinPeticion, String fechaIniCirugia, String fechaFinCirugia, 
											  int profesional, int estadoPeticion, String origen, int centroAtencion, int codigoCentroAtencion,
											  String programable)
	{
		StringBuffer consulta = new StringBuffer();
		int indice = 0;
		
		//---Columnas
		String[] columnas = {"consecutivo","fecha_peticion","fecha_cirugia","tId","nroId",
					 		 "paciente","medico","estado_peticion", "codigo_estado", "codigo_medico","centro_atencion" };
		
		/*consulta.append(" SELECT  pqx.codigo as consecutivo, to_char(pqx.fecha_peticion, 'DD/MM/YYYY') as fecha_peticion, " +
						"		  to_char(pqx.fecha_cirugia, 'DD/MM/YYYY') as fecha_cirugia, " +
						"	      per.tipo_identificacion as tId, per.numero_identificacion as nroId, " + 
						"	      per.primer_apellido || ' ' || per.segundo_apellido || ' ' || per.primer_nombre || ' ' || per.segundo_nombre as paciente, " +	
						"		  perso.primer_apellido || ' ' || perso.segundo_apellido || ' ' || perso.primer_nombre || ' ' || perso.segundo_nombre as medico, " +
						"		  ep.nombre as estado_peticion, ep.codigo AS codigo_estado,  perso.codigo AS codigo_medico" +
						"		  FROM peticion_qx pqx "+
						"	     	   INNER JOIN pacientes pac ON ( pqx.paciente = pac.codigo_paciente) "+		   		 
						"			   INNER JOIN personas per ON ( per.codigo = pac.codigo_paciente ) "+
						"			   INNER JOIN usuarios us ON ( us.login = pqx.usuario ) "+
						"			   INNER JOIN personas perso ON ( us.codigo_persona = perso.codigo ) " +
						"			   INNER JOIN estados_peticion ep ON (pqx.estado_peticion = ep.codigo ) " +
						"						  WHERE	TRUE AND ");*/
		try
		{
		if(origen.equals("preanestesia"))
		{
		consulta.append(" SELECT * FROM (" +
						"		  SELECT  pqx.codigo as consecutivo, to_char(pqx.fecha_peticion, 'DD/MM/YYYY') as fecha_peticion, " +    
						"		  to_char(pqx.fecha_cirugia, 'DD/MM/YYYY') as fecha_cirugia,    " +
						"	      per.tipo_identificacion as tId, per.numero_identificacion as nroId,     " +
						"	      per.primer_apellido || ' ' || per.segundo_apellido || ' ' || per.primer_nombre || ' ' || per.segundo_nombre as paciente, " +    	
						"		  perso.primer_apellido || ' ' || perso.segundo_apellido || ' ' || perso.primer_nombre || ' ' || perso.segundo_nombre as medico, " +    
						"		  ep.nombre as estado_peticion, ep.codigo AS codigo_estado,  perso.codigo AS codigo_medico ," +
						"		  ca.descripcion as centro_atencion,programable	  " +
					    "		  FROM peticion_qx pqx "   +
						"	     	   INNER JOIN pacientes pac ON ( pqx.paciente = pac.codigo_paciente) " +  		   		 
						"			   INNER JOIN personas per ON ( per.codigo = pac.codigo_paciente )   " +
						"			   INNER JOIN personas perso ON ( pqx.solicitante = perso.codigo )   " +
						"			   INNER JOIN estados_peticion ep ON (pqx.estado_peticion = ep.codigo ) " +    
						"			   INNER JOIN centro_atencion ca ON (ca.consecutivo = pqx.centro_atencion ) " +
						"			   INNER JOIN cuentas cu ON ( cu.codigo_paciente = per.codigo )");
		
		//-Se consultan las peticiones de los paciente que tienen la cuenta abierta filtrado por el centro de atención del usuario en sesión
		consulta.append(" WHERE cu.estado_cuenta NOT IN (" + ConstantesBD.codigoEstadoCuentaFacturada +  "," +							   																									  
														   + ConstantesBD.codigoEstadoCuentaCerrada +  "," +
														   + ConstantesBD.codigoEstadoCuentaExcenta + 
												      ") " +
						"						AND pqx.centro_atencion = " + codigoCentroAtencion+" "
						
						//" 	                    AND getIndQxPeticion(pqx.codigo) IN ('"+ConstantesIntegridadDominio.acronimoIndicativoCargoCirugia+"','"+ConstantesIntegridadDominio.acronimoIndicativoCargoNoCruento+"') " 
						);
		//----Se consultan las peticiones de los paciente que tienen la cuenta cerrada sin hacer filtro por el centro de atención
		consulta.append("  UNION ALL "+
							"SELECT  pqx.codigo as consecutivo, to_char(pqx.fecha_peticion, 'DD/MM/YYYY') as fecha_peticion, " +    
							"		  to_char(pqx.fecha_cirugia, 'DD/MM/YYYY') as fecha_cirugia,    " +
							"	      per.tipo_identificacion as tId, per.numero_identificacion as nroId,     " +
							"	      per.primer_apellido || ' ' || per.segundo_apellido || ' ' || per.primer_nombre || ' ' || per.segundo_nombre as paciente, " +    	
							"		  perso.primer_apellido || ' ' || perso.segundo_apellido || ' ' || perso.primer_nombre || ' ' || perso.segundo_nombre as medico, " +    
							"		  ep.nombre as estado_peticion, ep.codigo AS codigo_estado,  perso.codigo AS codigo_medico ," +
							"		  ca.descripcion as centro_atencion,pqx.programable	  " +
						    "		  FROM peticion_qx pqx "   +
							"	     	   INNER JOIN pacientes pac ON ( pqx.paciente = pac.codigo_paciente) " +  		   		 
							"			   INNER JOIN personas per ON ( per.codigo = pac.codigo_paciente )   " +
							"			   INNER JOIN personas perso ON ( pqx.solicitante = perso.codigo )   " +
							"			   INNER JOIN estados_peticion ep ON (pqx.estado_peticion = ep.codigo ) " +    
							"			   INNER JOIN centro_atencion ca ON (ca.consecutivo = pqx.centro_atencion ) " +
							"			   INNER JOIN cuentas cu ON ( cu.codigo_paciente = per.codigo ) " +    
							"		  WHERE	cu.estado_cuenta IN (" + ConstantesBD.codigoEstadoCuentaFacturada +  "," +														  
															   	   + ConstantesBD.codigoEstadoCuentaCerrada +  "," +
															   	   + ConstantesBD.codigoEstadoCuentaExcenta + 
													      		") " 
							//"		  AND getIndQxPeticion(pqx.codigo) IN ('"+ConstantesIntegridadDominio.acronimoIndicativoCargoCirugia+"','"+ConstantesIntegridadDominio.acronimoIndicativoCargoNoCruento+"') "
							);
		
			consulta.append(")tabla WHERE true ");
			
			//-Parametros de consulta
			if (nroIniServicio != 0)
			{
				consulta.append(" AND tabla.consecutivo >= " + nroIniServicio);
			}
			if (nroFinServicio != 0)
			{
					consulta.append(" AND tabla.consecutivo<= " + nroFinServicio);
			}
			if (!fechaIniPeticion.trim().equals(""))
			{
					consulta.append(" AND tabla.fecha_peticion >= '" + UtilidadFecha.conversionFormatoFechaABD(fechaIniPeticion) +"'" );
			}
			if (!fechaFinPeticion.trim().equals(""))
			{
					consulta.append("  AND  tabla.fecha_peticion <= '" + UtilidadFecha.conversionFormatoFechaABD(fechaFinPeticion) +"'" );
			}
			if (!fechaIniCirugia.trim().equals(""))
			{
					consulta.append(" AND tabla.fecha_cirugia >= '" + UtilidadFecha.conversionFormatoFechaABD(fechaIniCirugia)+"'" );
			}
			if (!fechaFinCirugia.trim().equals(""))
			{
					consulta.append(" AND tabla.fecha_cirugia <= '"+ UtilidadFecha.conversionFormatoFechaABD(fechaFinCirugia)+"'");
			}			
			if(!programable.equals(""))
				consulta.append(" AND tabla.programable = '"+programable+"' ");
			
			if ( profesional != -1)
			{
					consulta.append(" AND tabla.codigo_medico = " + profesional);
			}
			
			if ( estadoPeticion != -1)
			{
					consulta.append(" AND tabla.codigo_estado = " + estadoPeticion);
			}

			/*if ( centroAtencion != 0)
			{
				if (indice!=0)
					consulta.append(" AND ca.consecutivo = " + centroAtencion);
				else
					consulta.append(" ca.consecutivo = " + centroAtencion);					
			}*/
			
			/*if(origen.equals("preanestesia") || origen.equals("modificar"))
				{*/
					consulta.append(" AND (tabla.codigo_estado = " + ConstantesBD.codigoEstadoPeticionPendiente+" OR tabla.codigo_estado = "+ConstantesBD.codigoEstadoPeticionProgramada+" OR tabla.codigo_estado = "+ConstantesBD.codigoEstadoPeticionReprogramada+")");
				//}
			
			consulta.append(" ORDER BY tabla.consecutivo ");
		}//If origen es preanestesia
		else
		{
			consulta.append(" SELECT  pqx.codigo as consecutivo, to_char(pqx.fecha_peticion, 'DD/MM/YYYY') as fecha_peticion, " +    
											"		  to_char(pqx.fecha_cirugia, 'DD/MM/YYYY') as fecha_cirugia,    " +
											"	      per.tipo_identificacion as tId, per.numero_identificacion as nroId,     " +
											"	      per.primer_apellido || ' ' || per.segundo_apellido || ' ' || per.primer_nombre || ' ' || per.segundo_nombre as paciente, " +    	
											"		  perso.primer_apellido || ' ' || perso.segundo_apellido || ' ' || perso.primer_nombre || ' ' || perso.segundo_nombre as medico, " +    
											"		  ep.nombre as estado_peticion, ep.codigo AS codigo_estado,  perso.codigo AS codigo_medico ," +
											"		  ca.descripcion as centro_atencion	  " +
										    "		  FROM peticion_qx pqx "   +
											"	     	   INNER JOIN pacientes pac ON ( pqx.paciente = pac.codigo_paciente) " +  		   		 
											"			   INNER JOIN personas per ON ( per.codigo = pac.codigo_paciente )   " +
											"			   INNER JOIN personas perso ON ( pqx.solicitante = perso.codigo )   " +
											"			   INNER JOIN estados_peticion ep ON (pqx.estado_peticion = ep.codigo ) " +    
											"			   INNER JOIN centro_atencion ca ON (ca.consecutivo = pqx.centro_atencion ) " +
											"		  WHERE 1=1 " );
											//"getIndQxPeticion(pqx.codigo) IN ('"+ConstantesIntegridadDominio.acronimoIndicativoCargoCirugia+"','"+ConstantesIntegridadDominio.acronimoIndicativoCargoNoCruento+"') ");
			
			//-Parametros de consulta
			if (nroIniServicio != 0)
			{
				consulta.append(" AND pqx.codigo >= " + nroIniServicio);
			}
			if (nroFinServicio != 0)
			{
					consulta.append(" AND pqx.codigo <= " + nroFinServicio);
			}
			if (!fechaIniPeticion.trim().equals(""))
			{
				consulta.append(" AND pqx.fecha_peticion >= '" + UtilidadFecha.conversionFormatoFechaABD(fechaIniPeticion) +"'" );
				
			}
			if (!fechaFinPeticion.trim().equals(""))
			{
				consulta.append("  AND  pqx.fecha_peticion <= '" + UtilidadFecha.conversionFormatoFechaABD(fechaFinPeticion) +"'" );
			}
			if (!fechaIniCirugia.trim().equals(""))
			{
				consulta.append(" AND pqx.fecha_cirugia >= '" + UtilidadFecha.conversionFormatoFechaABD(fechaIniCirugia)+"'" );
			}
			if (!fechaFinCirugia.trim().equals(""))
			{
				consulta.append(" AND pqx.fecha_cirugia <= '"+ UtilidadFecha.conversionFormatoFechaABD(fechaFinCirugia)+"'");
			}
			if(!programable.equals(""))
				consulta.append(" AND pqx.programable = '"+programable+"' ");
			
			if ( profesional != -1)
			{
				consulta.append(" AND perso.codigo = " + profesional);
			}
			
			if ( estadoPeticion != -1)
			{
				consulta.append(" AND pqx.estado_peticion = " + estadoPeticion);
			}

			if ( centroAtencion != 0)
			{
				consulta.append(" AND ca.consecutivo = " + centroAtencion);
			}
			
			
			
			if(origen.equals("modificar"))
				{
					consulta.append(" AND (pqx.estado_peticion = " + ConstantesBD.codigoEstadoPeticionPendiente+" OR pqx.estado_peticion = "+ConstantesBD.codigoEstadoPeticionProgramada+" OR pqx.estado_peticion = "+ConstantesBD.codigoEstadoPeticionReprogramada+")");
				}
			
			consulta.append(" ORDER BY pqx.codigo ");
		}//else origen preanestesia
			
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta.toString(),ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		
			
			RespuestaHashMap listado=UtilidadBD.resultSet2HashMap(columnas,new ResultSetDecorator(pst.executeQuery()),false,true);
			return listado.getMapa();
		}
		catch (SQLException e)
		{
			logger.error("Error en consultarPeticiones de SqlBaseMaterialesQxDao: "+e);
			return null;
		}
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
	 * @param esModificar Booleano que me indica si el método va a Insertar o a Modificar una peticion existente
	 * @param esContinuarTransaccion, boolean que indica si la transaccion ya fue inicializada
     * @return Numero de inserciónes en la BD (posición 0) y codigo de la petición (posición 1)
	 */
	public static int[] insertar(  Connection con, 
                                            HashMap mapaPeticionEncabezado, 
                                            HashMap mapaPeticionServicios, 
                                            HashMap mapaPeticionProfesionales, 
                                            HashMap mapaPeticionMateriales, 
                                            int codigoPersona, 
                                            int idIngreso,
                                            UsuarioBasico usuario,
                                            boolean esContinuarTransaccion,
                                            boolean esModificar) {
		logger.info("888888888888888 INSERTAR ");
		logger.info("\n entre a  insertar peticion centro atencion-->"+usuario.getCodigoCentroAtencion());

		int numeroInserciones=0;
		Integer codigoContrato = null;
		String cubierto = "";
		
		try
		{
            if(!esContinuarTransaccion)
            {    
                UtilidadBD.iniciarTransaccion(con);
            }
            PreparedStatementDecorator ingresoPeticion=null;
            String valor=null;
            int codigo=0;
            if(esModificar)
            {
            	ingresoPeticion= new PreparedStatementDecorator(con.prepareStatement(updatePeticionQxStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
    			valor=(String)mapaPeticionEncabezado.get("tipoPaciente");
    			if(!valor.equals("0"))
    			{
    				ingresoPeticion.setString(1, valor);
    			}
    			else
    			{
    				ingresoPeticion.setNull(1, Types.CHAR);
    			}
    			
    			valor=(String)mapaPeticionEncabezado.get("duracion");
    			if(!valor.trim().equals(""))
    			{
    				ingresoPeticion.setString(2, valor);
    			}
    			else
    			{
    				ingresoPeticion.setNull(2, Types.TIME);
    			}
    			
    			valor=(String)mapaPeticionEncabezado.get("solicitante");
    			ingresoPeticion.setInt(3, Integer.parseInt(valor));
    			
    			valor=(String)mapaPeticionEncabezado.get("fechaEstimada");
    			if(valor!=null && !valor.trim().equals(""))
    			{
    				ingresoPeticion.setString(4, UtilidadFecha.conversionFormatoFechaABD(valor));
    			}
    			else
    			{
    				ingresoPeticion.setNull(4, Types.DATE);
    			}

    			valor=(String)mapaPeticionEncabezado.get("requiereUci");
                if(valor!=null && !valor.trim().equals(""))
                {    
                    ingresoPeticion.setBoolean(5, UtilidadTexto.getBoolean(valor));
                }
                else
                {
                    ingresoPeticion.setObject(5, null);
                }
                if(Utilidades.convertirAEntero(mapaPeticionEncabezado.get("tipoAnestesia")+"")<=0)
                	ingresoPeticion.setObject(6, null);
                else
                	ingresoPeticion.setObject(6, mapaPeticionEncabezado.get("tipoAnestesia"));
                if(mapaPeticionMateriales.containsKey("observaciones")&&!mapaPeticionMateriales.get("observaciones").equals("")){
                	ingresoPeticion.setString(7, mapaPeticionMateriales.get("observaciones").toString());
                }	
                else{
                	ingresoPeticion.setString(7, "");
                }
                if(mapaPeticionEncabezado.containsKey("urgente")&&!mapaPeticionEncabezado.get("urgente").equals("")){
                	ingresoPeticion.setBoolean(8,UtilidadTexto.getBoolean(mapaPeticionEncabezado.get("urgente")+""));
                }
                else{
                	ingresoPeticion.setBoolean(8, false);
                }
                valor=(String)mapaPeticionEncabezado.get("numeroPeticion");
                codigo=Integer.parseInt(valor);
                ingresoPeticion.setInt(9, codigo);
            }
            else
			{
            	ingresoPeticion= new PreparedStatementDecorator(con.prepareStatement(insertarPeticionQxStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
    			codigo=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).incrementarValorSecuencia(con, "seq_peticion_qx");
    			
    			ingresoPeticion.setInt(1, codigo);
    			ingresoPeticion.setInt(2, codigoPersona);
    			
    			valor=(String)mapaPeticionEncabezado.get("tipoPaciente");
    			if(!valor.equals("0"))
    			{
    				ingresoPeticion.setString(3, valor);
    			}
    			else
    			{
    				ingresoPeticion.setNull(3, Types.CHAR);
    			}

    			valor=(String)mapaPeticionEncabezado.get("fechaPeticion");
    			ingresoPeticion.setString(4, UtilidadFecha.conversionFormatoFechaABD(valor));
    			
    			
    			valor=(String)mapaPeticionEncabezado.get("horaPeticion");
    			ingresoPeticion.setString(5, valor);

    			valor=(String)mapaPeticionEncabezado.get("duracion");
    			if(!valor.trim().equals(""))
    			{
    				ingresoPeticion.setString(6, valor);
    			}
    			else
    			{
    				ingresoPeticion.setNull(6, Types.TIME);
    			}
    			
    			valor=(String)mapaPeticionEncabezado.get("solicitante");
    			ingresoPeticion.setInt(7, Integer.parseInt(valor));

    			valor=(String)mapaPeticionEncabezado.get("fechaEstimada");
    			if(valor!=null && !valor.trim().equals(""))
    			{
    				ingresoPeticion.setString(8, UtilidadFecha.conversionFormatoFechaABD(valor));
    			}
    			else
    			{
    				ingresoPeticion.setNull(8, Types.DATE);
    			}

    			ingresoPeticion.setString(9, usuario.getLoginUsuario());

    			valor=(String)mapaPeticionEncabezado.get("requiereUci");
                if(valor!=null && !valor.trim().equals(""))
                {    
                    ingresoPeticion.setBoolean(10, UtilidadTexto.getBoolean(valor));
                }
                else
                {
                    ingresoPeticion.setObject(10, null);
                }

    			ingresoPeticion.setInt(11, usuario.getCodigoInstitucionInt());
                ingresoPeticion.setInt(12, usuario.getCodigoCentroAtencion());
                if(Utilidades.convertirAEntero(mapaPeticionEncabezado.get("tipoAnestesia")+"")<=0)
                	ingresoPeticion.setObject(13, null);
                else
                	ingresoPeticion.setObject(13, mapaPeticionEncabezado.get("tipoAnestesia"));
                
                if(mapaPeticionEncabezado.containsKey("programable")&&!mapaPeticionEncabezado.get("programable").equals(""))
                	ingresoPeticion.setObject(14, mapaPeticionEncabezado.get("programable"));
                else
                	ingresoPeticion.setObject(14, ConstantesBD.acronimoSi);

                //71212
                if(mapaPeticionMateriales.containsKey("observaciones")&&!mapaPeticionMateriales.get("observaciones").equals("")){
                	ingresoPeticion.setString(15, mapaPeticionMateriales.get("observaciones").toString());
                }	
                else{
                	ingresoPeticion.setString(15, "");
                }
                if(idIngreso==ConstantesBD.codigoNuncaValido){
                	ingresoPeticion.setNull(16, Types.INTEGER);
                }
                else{
                	ingresoPeticion.setInt(16, idIngreso);
                }
                if(mapaPeticionEncabezado.containsKey("urgente")&&!mapaPeticionEncabezado.get("urgente").equals("")){
                	ingresoPeticion.setBoolean(17,UtilidadTexto.getBoolean(mapaPeticionEncabezado.get("urgente")+""));
                }
                else{
                	ingresoPeticion.setBoolean(17, false);
                }	
                
                if(mapaPeticionEncabezado.containsKey("acronimoDiagnostico")&&!mapaPeticionEncabezado.get("acronimoDiagnostico").equals("")){
                	ingresoPeticion.setString(18,mapaPeticionEncabezado.get("acronimoDiagnostico").toString());
                }
                else{
                	ingresoPeticion.setString(18, "");
                }
                
                if(mapaPeticionEncabezado.containsKey("tipoDiagnostico")&&!mapaPeticionEncabezado.get("tipoDiagnostico").equals("")){
                	ingresoPeticion.setInt(19, Integer.parseInt(mapaPeticionEncabezado.get("tipoDiagnostico").toString()));
                }
                else{
                	ingresoPeticion.setNull(19, Types.INTEGER);
                }
			}

			numeroInserciones=ingresoPeticion.executeUpdate();
			
			//--------------------------------------------------------------------------------------------------------
			

			if( numeroInserciones==0  )
			{
				UtilidadBD.abortarTransaccion(con);
				return new int[2];
			}
			else
			{
				if(esModificar)
				{
					try
					{
						ingresoPeticion= new PreparedStatementDecorator(con.prepareStatement("DELETE FROM prof_partici_peticion_qx WHERE peticion_qx =?",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
						ingresoPeticion.setInt(1, codigo);
						ingresoPeticion.executeUpdate();

						ingresoPeticion= new PreparedStatementDecorator(con.prepareStatement("DELETE FROM articulos_peticion_qx WHERE peticion_qx =?",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
						ingresoPeticion.setInt(1, codigo);
						ingresoPeticion.executeUpdate();

						ingresoPeticion= new PreparedStatementDecorator(con.prepareStatement("DELETE FROM peticiones_servicio WHERE peticion_qx =?",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
						ingresoPeticion.setInt(1, codigo);
						ingresoPeticion.executeUpdate();
						
					}
					catch(SQLException e)
					{
						logger.error("Error modificando la petición de cirugía : "+e);
						UtilidadBD.abortarTransaccion(con);
						return new int[2];
					}
				}
				
				ingresoPeticion= new PreparedStatementDecorator(con.prepareStatement(insertarParticipantesStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				
				int numeroProfesionales=Integer.parseInt(mapaPeticionProfesionales.get("numeroProfesionales")+"");
				
				for(int i=0; i<numeroProfesionales;i++)
				{
					ingresoPeticion.setInt(1, codigo);
					valor=(String)mapaPeticionProfesionales.get("profesional_"+i);
					ingresoPeticion.setInt(2, Integer.parseInt(valor));
					valor=(String)mapaPeticionProfesionales.get("tipo_participante_"+i);
					ingresoPeticion.setInt(3, Integer.parseInt(valor));
					valor=(String)mapaPeticionProfesionales.get("especialidades_"+i);
					ingresoPeticion.setInt(4, Integer.parseInt(valor));
					numeroInserciones+=ingresoPeticion.executeUpdate();
				}
				
				int numeroMateriales=Integer.parseInt(mapaPeticionMateriales.get("numeroMateriales")+"");
				ingresoPeticion= new PreparedStatementDecorator(con.prepareStatement(insertarMaterialesStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ingresoPeticion.setInt(2, codigo);
				for(int i=0; i<numeroMateriales;i++)
				{
					if(!(mapaPeticionMateriales.get("fueEliminadoArticulo_"+i)+"").equals("true"))
					{    
						valor=(String)mapaPeticionMateriales.get("codigoArticulo_"+i);
						ingresoPeticion.setInt(1, Integer.parseInt(valor));
						valor=(String)mapaPeticionMateriales.get("cantidadDespachadaArticulo_"+i);
						ingresoPeticion.setInt(3, Integer.parseInt(valor));
						numeroInserciones+=ingresoPeticion.executeUpdate();
					}
				}
				
				int numeroServicios=Integer.parseInt(mapaPeticionServicios.get("numeroFilasMapaServicios")+"");
				ingresoPeticion= new PreparedStatementDecorator(con.prepareStatement(insertarServiciosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            	ingresoPeticion.setInt(1, codigo);

            	for(int i=0; i<numeroServicios;i++)
				{
            		cubierto = mapaPeticionServicios.get("cubierto_"+i).toString();
            		codigoContrato = Integer.parseInt(mapaPeticionServicios.get("contrato_convenio_"+i).toString());

					valor=(String)mapaPeticionServicios.get("fueEliminadoServicio_"+i);
					if(!valor.equals("true"))
					{
						valor=mapaPeticionServicios.get("codigoServicio_"+i)+"";
						ingresoPeticion.setInt(2, Integer.parseInt(valor));
						
						valor=(String)mapaPeticionServicios.get("numeroServicio_"+i);
						ingresoPeticion.setInt(3, Integer.parseInt(valor));
						
						valor=mapaPeticionServicios.get("observaciones_"+i)+"";
						if(valor.equals("null") || valor.trim().equals("")){
							ingresoPeticion.setNull(4, Types.VARCHAR);
						}else{
							ingresoPeticion.setString(4, valor);
						}
						
						if(cubierto.isEmpty()){
							ingresoPeticion.setNull(5, Types.VARCHAR);
						}else{
							ingresoPeticion.setString(5, cubierto);
						}
						
						if (codigoContrato == null){
							ingresoPeticion.setNull(6, Types.INTEGER);
						}else {
							ingresoPeticion.setInt(6, codigoContrato);
						}
						
						
						
						numeroInserciones+=ingresoPeticion.executeUpdate();
						
					}
				}
			}

			if(!esContinuarTransaccion)
            {    
                UtilidadBD.finalizarTransaccion(con);
            }    
			ingresoPeticion.close();
			int[] resultado={numeroInserciones, codigo};
			return resultado;
		}
		catch (SQLException e)
		{
			if(esModificar)
			{
				logger.error("Error modificando la petición de cirugía : "+e);
			}
			else
			{
				logger.error("Error ingresando la petición de cirugía : "+e);
			}
			UtilidadBD.abortarTransaccion(con);
			return new int[2];
		}
	}
    
	
    /**
     * Metodo que carga los servicios dados los codigos de peticiones
     * @param con
     * @param codigosPeticionesSeparadosPorComas
     * @return
     */
    public static HashMap cargarServiciosDadasPeticiones(Connection con, String codigosPeticionesSeparadosPorComas)
    {
        HashMap map= new HashMap();
        try
        {
            String[] colums={   "codigoServicio",
                                        "numeroServicio",
                                        "codigoCups",
                                        "descripcionServicio",
                                        "codigoEspecialidad",
                                        "descripcionEspecialidad",
                                        "esPos",
                                        "codigoCirujano",
                                        "codigoAyudante",
                                        "fueEliminadoServicio",
                                        "valor_unitario",
                                        "observaciones"
                                   };
            
            
            String consulta=cargarServiciosDadasPeticionesStr;
            consulta+=" WHERE " +
                            " rs.tipo_tarifario="+ConstantesBD.codigoTarifarioCups+" "+
                            " AND pq.codigo IN("+codigosPeticionesSeparadosPorComas+") " +
                            " ORDER BY numeroServicio ";
            
            PreparedStatementDecorator cargarStatement= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            map=UtilidadBD.resultSet2HashMap(colums,new ResultSetDecorator(cargarStatement.executeQuery()), false, true).getMapa();
            map.put("numColumnas", colums.length+"");
            return map;
        }
        catch(SQLException e)
        {
            logger.warn(e+" Error en cargarServiciosDadasPeticiones de : SqlBasePeticionDao "+e.toString());
            return map;
        }
    }
    
    /**
     * Metodo que carga los materiales especiales PARAMETRIZADOS de una peticion
     * @param con
     * @param codigoPeticion
     * @return
     */
    public static HashMap cargarMaterialesEspeciales2(Connection con, String codigoPeticion)
    {
        HashMap map= new HashMap();
        try
        {
            String[] colums={   "codigoArticulo",
                                        "descripcionArticulo",
                                        "unidadMedidaArticulo",
                                        "cantidadDespachadaArticulo",
                                        "autorizacionArticulo",
                                        "fueEliminadoArticulo",
                                        "tipoPosArticulo"
                                   };
            
            
            PreparedStatementDecorator cargarStatement= new PreparedStatementDecorator(con.prepareStatement(cargarMaterialesEspeciales2Str,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            cargarStatement.setString(1, codigoPeticion);
            map=UtilidadBD.resultSet2HashMap(colums,new ResultSetDecorator(cargarStatement.executeQuery()), false, true).getMapa();
            map.put("numColumnas", colums.length+"");
            return map;
        }
        catch(SQLException e)
        {
            logger.warn(e+" Error en cargarMaterialesEspeciales2 de : SqlBasePeticionDao "+e.toString());
            return map;
        }
    }
    
    /**
     * Metodo que actualiza (fecha Estimada Cx- Duracion Aprox -RequiereUci) informacion de la peticion
     * @param con
     * @param fechaEstimadaCirugia
     * @param duracion
     * @param codigoPeticion
     * @param requiereUci
     * @return
     */
     public static boolean actualizarFechaDuracionRequiereUciPeticion(Connection con, String fechaEstimadaCirugia, String duracion, String codigoPeticion, String requiereUci)
     {
         try
         {
             PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(actualizarFechaDuracionRequiereUciPeticionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
             if(fechaEstimadaCirugia.trim().equals(""))
                 pst.setObject(1, null);
             else
                 pst.setString(1,UtilidadFecha.conversionFormatoFechaABD(fechaEstimadaCirugia));
             if(duracion.trim().equals(""))
                 pst.setObject(2,null);
             else
                 pst.setString(2,duracion);
             if(requiereUci.equals(""))
                 pst.setObject(3, null);
             else
                 pst.setBoolean(3, UtilidadTexto.getBoolean(requiereUci));
             pst.setString(4, codigoPeticion);
             
             if(pst.executeUpdate()>0)
                 return true;
             else
                 return false;
         }
         catch(SQLException e)
         {
             logger.error("Error en actualizarFechaDuracionRequiereUciPeticion de SqlBasePeticionDao: "+e);
             return false;
         }
     }
     
     /**
      * metodo que carga el encabezaod de la peticion sin restrcciones, a menos de que se especifiquen los
      * codigos de la peticion.
      * @param con
      * @param codigosPeticionesSeparadosPorComas
      * @param HashMap filtros
      * @return
      */
     public static Collection cargarEncabezadoPeticionSinRestricciones(Connection con, String codigosPeticionesSeparadosPorComas, HashMap filtros)
     {
         try
         {
             String consulta=cargarEncabezadoPeticionSinRestriccionesStr;
             if(!codigosPeticionesSeparadosPorComas.trim().equals(""))
                 consulta+=" AND pq.codigo IN ("+codigosPeticionesSeparadosPorComas+") ";
             if(filtros!=null && filtros.containsKey("programable"))
            	 consulta+=" AND pq.programable = '"+filtros.get("programable").toString()+"' ";
             
             consulta+=" ORDER BY  fechaEstimadaCirugia";
             
             PreparedStatementDecorator cargarStatement= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
             Collection col=UtilidadBD.resultSet2Collection(new ResultSetDecorator(cargarStatement.executeQuery()));
             return col;
         }
         catch(SQLException e)
         {
             logger.warn(e+" Error en cargarEncabezadoPeticionSinRestricciones de : SqlBasePeticionDao "+e.toString());
             return null;
         }
     }
     
 	/**
 	 * Método para anular la petición de cirugías
 	 * @param con Conexión con la BD
 	 * @param numeroPeticion Petición a modificar
 	 * @param motivoAnulacion Motivo de la anulación
 	 * @param comentario Comentarios de la anulación
 	 * @param loginUsuario Usuario que realiuzó la anulación
 	 * @return Mayor que 0 si la anulación fue correcta
 	 */
 	public static int anularPeticion(Connection con, int numeroPeticion, int motivoAnulacion, String comentario, String loginUsuario)
 	{
		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		int numeroModificaciones=0;
 		try
		{
 			myFactory.beginTransaction(con);
		}
		catch (SQLException e)
		{
			logger.error("Error iniciando la transacción : "+e);
			// En este caso no se hace necesario abortar la transacción
			return -1;
		}
		try
		{
 			PreparedStatementDecorator anulacionPeticion= new PreparedStatementDecorator(con.prepareStatement("UPDATE peticion_qx SET estado_peticion =? WHERE codigo=?",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
 			anulacionPeticion.setInt(1, ConstantesBD.codigoEstadoPeticionAnulada);
 			anulacionPeticion.setInt(2, numeroPeticion);
 			numeroModificaciones=anulacionPeticion.executeUpdate();
		}
		catch (SQLException e)
		{
			logger.error("Error actualizando el estado de la petición : "+e);
			return abortarAnulacion(con, myFactory);
		}
		if(numeroModificaciones<=0)
		{
			logger.error("No se modificó el estado de la petición");
			return abortarAnulacion(con, myFactory);
		}
		else
		{
			try
			{
	 			PreparedStatementDecorator anulacionPeticion= new PreparedStatementDecorator(con.prepareStatement("INSERT INTO anulacion_peticion_qx (peticion_qx, motivos_anulacion, usuario, comentario, fecha, hora) VALUES(?,?,?,?,CURRENT_DATE,"+ValoresPorDefecto.getSentenciaHoraActualBDTipoTime()+")",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	 			anulacionPeticion.setInt(1, numeroPeticion);
	 			anulacionPeticion.setInt(2, motivoAnulacion);
	 			anulacionPeticion.setString(3, loginUsuario);
	 			anulacionPeticion.setString(4, comentario);
	 			int numInserciones=anulacionPeticion.executeUpdate();
	 			if(numInserciones<=0)
	 			{
	 				logger.error("No se ingresó ningún valor en anulaciones petición");
	 				return abortarAnulacion(con, myFactory);
	 			}
	 			numeroModificaciones+=numInserciones;
			}
 			catch (SQLException e)
 			{
 				logger.error("Error ingresando la anulación de la petición : "+e);
 				return abortarAnulacion(con, myFactory);
 			}
		}
		try
		{
			myFactory.endTransaction(con);
		}
		catch (SQLException e)
		{
			logger.error("Error finalizando la transacción : "+e);
			return -2;
		}
 		return numeroModificaciones;
 	}

 	/**
 	 * Método que aborta una transacción y retorna entero -1 para manejo de errores
 	 * @param con Conexión con la BD
 	 * @param myFactory DaoFactory para poder abortar la transacción
 	 * @return -1 en caso de abortar correctamente, -2 en caso de error abortando
 	 */
	private static int abortarAnulacion(Connection con, DaoFactory myFactory)
	{
		try
		{
			myFactory.abortTransaction(con);
		}
		catch (SQLException e)
		{
			logger.error("Error abortando la transacción : "+e);
			return -2;
		}
		return -1;
	}
	
	/**
	 * Método que consulta lso apellidos y nombre del paciente de la peticion
	 * @param con
	 * @param codigoPeticion
	 * @return
	 */
	public static String getApellidosNombresPacientePeticion(Connection con,int codigoPeticion)
	{
		String paciente = "";
		try
		{
			String consulta = "SELECT getnombrepersona(paciente) As paciente from peticion_qx where codigo = "+codigoPeticion;
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			ResultSetDecorator rs = new ResultSetDecorator(st.executeQuery(consulta));
			
			if(rs.next())
				paciente = rs.getString("paciente");
			
		}
		catch(SQLException e)
		{
			logger.error("Error en getApellidosNombresPacientePeticion: "+e);
		}
		
		return paciente;
	}
	
	/**
 	 * Método para desasociar la petición de la solicitud
 	 * @param con Conexión con la BD
 	 * @param numeroSolicitud
 	 * @return si se pudo actualizar
 	 */
	public static boolean desAsociarPeticionSolicitud(Connection con, int numeroSolicitud){
		boolean exito=false;
		PreparedStatement pst= null;
		try
		{
			String update = "UPDATE salascirugia.solicitudes_cirugia SET codigo_peticion=? WHERE numero_solicitud=?";
			pst= con.prepareStatement(update, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			pst.setNull(1, Types.INTEGER);
			pst.setInt(2, numeroSolicitud);
			if(pst.executeUpdate() > 0){
				exito=true;
			}
		}
		catch(SQLException e){
			exito=false;
			Log4JManager.error(e);
		}
		finally{
			try {
				if(pst != null){
					pst.close();
				}
			} catch (SQLException sqle) {
				Log4JManager.error(sqle);
			}
		}
		return exito;
	}
}