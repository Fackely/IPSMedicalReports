/*
 * Mayo 25, 2007
 */
package com.princetonsa.dao.postgresql.manejoPaciente;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.InfoDatosInt;
import util.InfoDatosString;
import util.UtilidadBD;
import util.UtilidadTexto;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.manejoPaciente.UtilidadesManejoPacienteDao;
import com.princetonsa.dao.sqlbase.SqlBaseUtilidadesBDDao;
import com.princetonsa.dao.sqlbase.manejoPaciente.SqlBaseUtilidadesManejoPacienteDao;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.facturacion.DtoFactura;
import com.princetonsa.dto.manejoPaciente.DtoAutorizacion;
import com.princetonsa.dto.manejoPaciente.DtoCentrosAtencion;
import com.princetonsa.dto.manejoPaciente.DtoCuentas;
import com.princetonsa.dto.manejoPaciente.DtoInfoAmparosReclamacion;
import com.princetonsa.dto.manejoPaciente.DtoIngresos;
import com.princetonsa.dto.manejoPaciente.DtoResponsablePaciente;
import com.princetonsa.mundo.Paciente;
import com.servinte.axioma.dto.manejoPaciente.DtoCertAtenMedicaFurips;
import com.servinte.axioma.dto.manejoPaciente.DtoCertAtenMedicaFurpro;
import com.servinte.axioma.fwk.exception.BDException;

/**
 * 
 * @author sgomez
 * Clase que maneja los métodos propìos de Postgres para el acceso a la fuente
 * de datos en las utilidades del módulo de MANEJO PACIENTE
 */
public class PostgresqlUtilidadesManejoPacienteDao implements
		UtilidadesManejoPacienteDao {
	
	
	
	private static Logger logger=Logger.getLogger(PostgresqlUtilidadesManejoPacienteDao.class);
	
	/**
	 * Método implementado para obtener las camas segun el filtro definido
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap obtenerCamas(Connection con, HashMap campos)
	{
		return SqlBaseUtilidadesManejoPacienteDao.obtenerCamas(con, campos);
	}
	
	/**
	 * Método implementado para insertar la reserva de cama y el log de base de datos segun el filtro definido
	 * @param con
	 * @param mapa
	 * @return
	 */
	public int insertarReservarCamas(Connection con, HashMap mapa)
	{
		return SqlBaseUtilidadesManejoPacienteDao.insertarReservarCama(con, mapa);
	}
	
	/**
	 * Método implementado para modificar la reserva de cama segun el filtro definido
	 * @param con
	 * @param mapa
	 * @return
	 */
	public boolean modificarReservarCamas(Connection con, HashMap mapa)
	{
		return SqlBaseUtilidadesManejoPacienteDao.modificarReservarCama(con, mapa);
	}
	
	
	
	/**
	 * Consulta las reserva cama
	 * @param con
	 * @param codigoPaciente
	 * @param codigoInstitucion
	 * @return
	 */
	public HashMap consultarReservarCama(Connection con,String codigoPaciente, int codigoInstitucion)
	{
		return SqlBaseUtilidadesManejoPacienteDao.consultarReservarCama(con, codigoPaciente, codigoInstitucion);
	}
	
	/**
	 * Método implementado para obtener los origenes de la admision
	 * @param con
	 * @return
	 */
	public ArrayList obtenerOrigenesAdmision(Connection con)
	{
		return SqlBaseUtilidadesManejoPacienteDao.obtenerOrigenesAdmision(con);
	}
	
	/**
	 * Método que consulta los datos de la cama reservada de un paciente
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap consultarCamaReservada(Connection con,HashMap campos)
	{
		return SqlBaseUtilidadesManejoPacienteDao.consultarCamaReservada(con, campos);
	}
	
	/**
	 * Método que libera una cama reserva de un paciente y le cambia su estado
	 * @param con
	 * @param campos
	 * @return
	 */
	public boolean liberarReservaCama(Connection con,HashMap campos)
	{
		return SqlBaseUtilidadesManejoPacienteDao.liberarReservaCama(con, campos);
	}
	
	/**
	 * Método que consulta las etnias activas del sistema
	 * @param con
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> obtenerEtnias(Connection con)
	{
		return SqlBaseUtilidadesManejoPacienteDao.obtenerEtnias(con);
	}
	
	/**
	 * Método que consulta las religiones parametrizadas por institucion
	 * @param con
	 * @param institucion
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> obtenerReligiones(Connection con,int institucion)
	{
		return SqlBaseUtilidadesManejoPacienteDao.obtenerReligiones(con, institucion);
	}
	
	/**
	 * Método que consulta los estudios parametrizados del sistema
	 * @param con
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> obtenerEstudios(Connection con)
	{
		return SqlBaseUtilidadesManejoPacienteDao.obtenerEstudios(con, DaoFactory.POSTGRESQL);
	}
	
	/**
	 * Método que consulta los convenios de un paciente, que están asociados a la estructura convenios_paciente
	 * @param con
	 * @param codigoPaciente
	 * @return
	 */
	public HashMap consultarConveniosPaciente(Connection con,String codigoPaciente)
	{
		return SqlBaseUtilidadesManejoPacienteDao.consultarConveniosPaciente(con, codigoPaciente);
	}
	
	/**
	 * Método que consulta los tipos de paciente definidos por vía de ingreso
	 * @param con
	 * @param campos
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> obtenerTiposPacienteXViaIngreso(Connection con,HashMap campos)
	{
		return SqlBaseUtilidadesManejoPacienteDao.obtenerTiposPacienteXViaIngreso(con, campos);
	}
	
	/**
	 * Método implementado para cargar el responsable del paciente
	 * @param con
	 * @param campos
	 * @return
	 */
	public DtoResponsablePaciente cargarResponsablePaciente(Connection con,HashMap campos)
	{
		return SqlBaseUtilidadesManejoPacienteDao.cargarResponsablePaciente(con, campos);
	}
	
	/**
	 * Método que obtiene los profesiones de la salud por institucion mas otros filtros
	 * @param con
	 * @param campos
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> obtenerProfesionales(Connection con,HashMap campos)
	{
		return SqlBaseUtilidadesManejoPacienteDao.obtenerProfesionales(con, campos);
	}
	
	/**
	 * Método que verifica si es requerido ingresar al documento de garantía 
	 * @param con
	 * @param campos
	 * @return
	 */
	public boolean esRequeridoDocumentoGarantia(Connection con,HashMap campos)
	{
		return SqlBaseUtilidadesManejoPacienteDao.esRequeridoDocumentoGarantia(con, campos);
	}
	/**
	 * Método que verifica si es requerido el deudor
	 * @param con
	 * @param campos
	 * @return
	 */
	public boolean esRequeridoDeudor(Connection con,HashMap campos)
	{
		return SqlBaseUtilidadesManejoPacienteDao.esRequeridoDeudor(con, campos);
	}
	
	/**
	 * Metodo que me devuelve un arraylist de Hashmap
	 * con la informacion de los estados de la cama.
	 * Los key's del HashMap son:
	 * -codigo
	 * -nombre
	 * @author Adicionado por Jhony Alexander Duque A.
	 * @param connection
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> obtenerEstadosCama (Connection connection) 
	{
		return SqlBaseUtilidadesManejoPacienteDao.obtenerEstadosCama(connection);
	}
	
	
	/**
	 * Metodo que devuelve un Arraylist de HashMap
	 * con la informacion de los centros de atencion.
	 * Contiene los siguientes Key's -> codigo , nomcentrocosto,
	 * codigotipoarea,nombretipoarea,activo,identificador,manejacamas,
	 * unidadfuncional,descunidadfuncional,codcentroatencion,
	 * nomcentroatencion.
	 * @author Jhony Alexander Duque A.
	 * @param con --> Requerido
	 * @param institucion --> Requerido
	 * @param tiposArea -->No Requerido
	 * @param todos --> False o true
	 * @param centroAtencion --> Requerido para todos = 0
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> obtenerCentrosCosto(Connection con, int institucion, String tiposArea, boolean todos, int centroAtencion)
	{
		return SqlBaseUtilidadesManejoPacienteDao.obtenerCentrosCosto(con, institucion, tiposArea, todos, centroAtencion);
	}
	
	/**
	 * Metodo que retona un Arraylist de HashMap con los
	 * Centros de Atencion.
	 * @author Jhony Alexander Duque A.
	 * @param con
	 * @param codigoInstitucion
	 * @param filtrarInsSirc
	 *  S --> filtrar los que tienen instucion sirc
	 *  N --> los que no tengan institucion sirc  
	 *  "" --> para no filtrar por institucion sirc 
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> obtenerCentrosAtencion(Connection con, int codigoInstitucion,String  filtrarInsSirc) 
	{
		return SqlBaseUtilidadesManejoPacienteDao.obtenerCentrosAtencion(con, codigoInstitucion,filtrarInsSirc);
	}
	
	
	/**
	 * Metodo que retorna un Arraylist de Dto con los Centros de Atencíon 
	 * Recibe un Dto Tipo Centro Atencion 
	 * @param DtoCentro Atencion@param
	 */
	
	public ArrayList<DtoCentrosAtencion> obtenerCentrosAtencion(DtoCentrosAtencion dto) 
	{
		//return SqlBaseUtilidadesManejoPacienteDao.obtenerCentrosAtencion(dto);
		ArrayList<DtoCentrosAtencion> arrayDto = new ArrayList<DtoCentrosAtencion>();
		String consultaStr= " Select 	c.consecutivo  as consecutivo ," +
														 " c.codigo as codigo ," +              
														 " c.descripcion as descripcion ," +        
														 " c.activo as activo," +            
														 " c.cod_institucion as codInstitucion ," +     
														 " c.codupgd as codupgd ," +            
														 " c.codigo_inst_sirc  as codigoInstSirc ," +    
														 " c.empresa_institucion  as empresaInstitucion," +
														 " c.direccion as direccion ," +          
														 " c.pais as pais," +                
														 " c.departamento as departamento ," +        
														 " c.ciudad  as ciudad," +             
														 " c.telefono as telefono ," +         
														 " c.region_cobertura as regionConbertura," +    
														 " c.categoria_atencion as categoriaAtencion ," +
														 " (select   p.descripcion  from paises p where p.codigo_pais=c.pais) as nombrePais " +
														 "from administracion.centro_atencion c"+
														" where 1=1 " +
														"" ;
			
			consultaStr+=(0<dto.getConsecutivo())?" AND consecutivo ="+dto.getConsecutivo():"";
			consultaStr+=UtilidadTexto.isEmpty(dto.getCodigo())?" ":" AND codigo='"+dto.getCodigo()+"'";
			consultaStr+=UtilidadTexto.isEmpty(dto.getDescripcion())?" ":" AND descripcion='"+dto.getDescripcion()+"'";
			consultaStr+=(0<dto.getCodInstitucion())?" AND cod_institucion ="+dto.getCodInstitucion():"";
			consultaStr+=(0<dto.getCodupgd())?" AND codupgd ="+dto.getCodupgd():"";
			consultaStr+=UtilidadTexto.isEmpty(dto.getCodigoInstSirc())?"":"AND codigo_inst_sirc ='"+dto.getCodigoInstSirc()+"'" ;
			consultaStr+=(0<dto.getEmpresaInstitucion())?" AND empresa_institucion ="+dto.getEmpresaInstitucion():"";
			consultaStr+= UtilidadTexto.isEmpty(dto.getDireccion())?"": " AND direccion ='"+dto.getDireccion()+"'";
			consultaStr+= UtilidadTexto.isEmpty(dto.getPais())?"": " AND pais ='"+dto.getPais()+"'";
			consultaStr+= UtilidadTexto.isEmpty(dto.getDepartamento())?"": " AND departamento ='"+dto.getDepartamento()+"'";
			consultaStr+= UtilidadTexto.isEmpty(dto.getCiudad())?"":  " AND ciudad ='"+dto.getCiudad()+"'";
			
			consultaStr+= UtilidadTexto.isEmpty(dto.getTelefono())?"": " AND telefono ='"+dto.getTelefono()+"'";
			consultaStr+=(dto.getRegionCobertura()>0)?" AND  region_cobertura ="+dto.getRegionCobertura():" ";
			consultaStr+=(0<dto.getCategoriaAtencion())?" AND  categoria_atencion ="+dto.getCategoriaAtencion():" ";
			consultaStr+=" AND  activo='"+dto.isActivo()+"'"; 
			
			

			logger.info("\n\n\n\n***********************************************************************************************");
			logger.info(" ----------------------------CARGANDO CENTROS DE ATENCION------------------------------------- \n\n\n");
			logger.info("\n\n\n CONSULTA   "+consultaStr);
			logger.info("-------------------\n\n\n\n\n\n");
			
			logger.info("-- Consultando Centro ATENCION -----------------");
			
			 try 
			 {
				Connection con = UtilidadBD.abrirConexion();
				PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr+" order by descripcion",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
				while(rs.next())
				 {
					DtoCentrosAtencion newdto = new DtoCentrosAtencion();
					newdto.setConsecutivo(rs.getInt("consecutivo"));
					newdto.setCodigo(rs.getString("codigo"));
					newdto.setDescripcion(rs.getString("descripcion"));
					newdto.setCodInstitucion(rs.getInt("codInstitucion"));
					newdto.setCodupgd(rs.getInt("codupgd"));
					newdto.setCodigoInstSirc(rs.getString("codigoInstSirc"));
					newdto.setEmpresaInstitucion(rs.getInt("empresaInstitucion"));
					newdto.setDireccion(rs.getString("direccion"));
					newdto.setPais(rs.getString("pais"));
					newdto.setDepartamento(rs.getString("departamento"));
					newdto.setCiudad(rs.getString("ciudad"));
					newdto.setTelefono(rs.getString("telefono"));
					newdto.setRegionCobertura(rs.getDouble("regionConbertura"));
					newdto.setCategoriaAtencion(rs.getDouble("categoriaAtencion"));
					newdto.setNombrePais(rs.getString("nombrePais"));
					newdto.setActivo(dto.isActivo());
					arrayDto.add(newdto);
				 }
				SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs , con);
			 }
			catch (SQLException e) 
			{
				logger.error("error en carga==> "+e);
			}
			
			return arrayDto;
	}
	
	
	/**
	 * Metodo que retorna los pisos filtrandolos
	 * por institucion o centro de atencion que son
	 * los valores que pueden ir en el hashmap.
	 * los key's en el hashmap parametros deben llevar los
	 * siguientes nombres --> institucion y/o centroatencion
	 * @author Jhony Alexander Duque A.
	 * @param connection
	 * @param parametros
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> obtenerPisos (Connection connection, HashMap parametros)
	{
		return SqlBaseUtilidadesManejoPacienteDao.obtenerPisos(connection, parametros);
	}
	
	/**
	 * Método que consulta las habitaciones
	 * @param con
	 * @param campos
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> obtenerHabitaciones(Connection con,HashMap campos)
	{
		return SqlBaseUtilidadesManejoPacienteDao.obtenerHabitaciones(con, campos);
	}
	
	/**
	 * Método para consultar los tipos de habitaciones
	 * @param con
	 * @param campos
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> obtenerTiposHabitaciones(Connection con,HashMap campos)
	{
		return SqlBaseUtilidadesManejoPacienteDao.obtenerTiposHabitaciones(con, campos);
	}
	
	/**
	 * Método que consulta el tipo paciente de una cuenta
	 * @param con
	 * @param campos
	 * @return
	 */
	public InfoDatosString obtenerTipoPacienteCuenta(Connection con,HashMap campos)
	{
		return SqlBaseUtilidadesManejoPacienteDao.obtenerTipoPacienteCuenta(con, campos);
	}
	
	
	/**
	 * Método que consulta el tipo paciente de una solicitud
	 * @param con
	 * @param campos
	 * @return
	 */
	public InfoDatosString obtenerTipoPacienteSolicitud(Connection con,HashMap campos) throws BDException
	{
		return SqlBaseUtilidadesManejoPacienteDao.obtenerTipoPacienteSolicitud(con, campos);
	}
	
	
	
	
	/**
	 * Método que consulta el id de la cuenta de un paciente que pertenezca a un ingreso abierto,
	 * una via de ingreso especifica, un centro de atencion especifico
	 * @param con
	 * @param campos
	 * @return
	 */
	public String getIdCuentaIngresoAbiertoPaciente(Connection con,HashMap campos)
	{
		return SqlBaseUtilidadesManejoPacienteDao.getIdCuentaIngresoAbiertoPaciente(con, campos);
	}
	
	/**
	 * Método que consulta el centro de atencion de la cuenta del ingreso abierto del paciente.
	 * 
	 * Nota* Se envian como parámetros el codgio del pacietne y las vias de ingreso que se desean filtrar
	 * se pasan separadas por comas
	 * @param con
	 * @param campos
	 * @return
	 */
	public int getCentroAtencionCuentaIngresoAbiertoPaciente(Connection con,HashMap campos)
	{
		return SqlBaseUtilidadesManejoPacienteDao.getCentroAtencionCuentaIngresoAbiertoPaciente(con, campos);
	}
	
	/**
	 * Método que realiza la isnerción del log de cambio de estado de camas
	 * @param con
	 * @param campos
	 * @return
	 */
	public int insertarLogCambioEstadoCama(Connection con,HashMap campos)
	{
		campos.put("secuencia", "nextval('seq_log_estados_cama')");
		return SqlBaseUtilidadesManejoPacienteDao.insertarLogCambioEstadoCama(con, campos);
	}
	
	
	/**
	 * Metodo adicionado por Jhony Alexander Duque A.
	 * Metodo encargado de consultar Datos de la cama.
	 * Este Metodo recibe un hashmap con dos parametros
	 * -------------------------------------
	 *      KEY'S DEL HASHMAP PARAMETROS
	 * -------------------------------------
	 * --codigoCama --> Requerido
	 * -->institucion --> Requerido
	 * Devuelve un Mapa con los siguientes Key's
	 *  habitacion,tipoHabitacion,numeroCama,
	 *  nombreCentroCosto,codigoCama,piso,
	 *  tipoUsuario, centroAtencion
	 * @param connection
	 * @param parametros
	 * @return mapa
	 */
	public HashMap obtenerDatosCama (Connection connection, HashMap parametros)
	{
		return SqlBaseUtilidadesManejoPacienteDao.obtenerDatosCama(connection, parametros);
	}
	
	/**
	 * Método que consulta las camas por habitación
	 * @param con
	 * @param campos
	 * @return
	 */
	public ArrayList<HashMap<String,Object>> obtenerCamasXHabitacion(Connection con,HashMap campos)
	{
		return SqlBaseUtilidadesManejoPacienteDao.obtenerCamasXHabitacion(con, campos);
	}
	
	/**
	 * Método implementado para consultar las entidades subcontratadas parametrizadas
	 * @param con
	 * @param campos
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> obtenerEntidadesSubcontratadas(Connection con,HashMap campos)
	{
		return SqlBaseUtilidadesManejoPacienteDao.obtenerEntidadesSubcontratadas(con, campos);
	}
	
	/**
	 * Método uqe consultar el valor de un parámetro general de los planos entidades subcontratadas 
	 * @param con
	 * @param campos
	 * @return
	 */
	public String getValorParametroGeneralPlanosEntidadSubcontratada(Connection con,HashMap campos)
	{
		return SqlBaseUtilidadesManejoPacienteDao.getValorParametroGeneralPlanosEntidadSubcontratada(con, campos);
	}
	
	/**
	 * Metodo Adicionado por Jhony Alexander Duque A.
	 * 11/01/2008
	 * Metodo encargado de obtener las secciones de parametros
	 * entidades subcontratadas.
	 * @param con
	 * @return
	 */
	public ArrayList obtenerSeccionesParametrosEntidadesSubcontratadas(Connection con)
	{
		return SqlBaseUtilidadesManejoPacienteDao.obtenerSeccionesParametrosEntidadesSubcontratadas(con);
	}
	
	
	/**
	 * Adicionado por Jhony Alexander Duque A.
	 * 15/01/2008
	 * Metodo encargado de consultar los tipos de 
	 * monto de cobro pudiendo filtar por difentes
	 * campos
	 * @param connection
	 * @param criterios
	 * -------------------------------------------
	 * 		KEY'S DEL HASHMAP CRITERIOS
	 * -------------------------------------------
	 *  -- activo --> Opcional 
	 *  -- convenio --> Opcional 
	 *  -- viaIngreso --> Opcional 
	 *  -- tipoAfiliado --> Opcional 
	 * @return ArrayList<HashMap<String,Objet>>
	 * --------------------------------------------------------
	 * 	KEY'S DEL MAPA DENTRO DEL ARRAY QUE DEVUELVE EL METODO
	 * --------------------------------------------------------
	 * -- codigo
	 * -- convenio
	 * -- viaIngreso
	 * -- tipoAfiliado
	 * -- estratoSocial
	 * -- tipoMonto
	 * -- valor
	 * -- porcentaje
	 * -- activo
	 */
	public ArrayList<HashMap<String, Object>> obtenerMontosCobro (Connection connection,HashMap criterios)
	{
		return SqlBaseUtilidadesManejoPacienteDao.obtenerMontosCobro(connection, criterios);
	}
	
	/**
	 * Adicionado por Julio Hernández
	 * 24/09/2009
	 * Metodo encargado de consultar los tipos de 
	 * monto de cobro pudiendo filtar por difentes
	 * campos
	 * @param connection
	 * @param criterios
	 * -------------------------------------------
	 * 		KEY'S DEL HASHMAP CRITERIOS
	 * -------------------------------------------
	 *  -- activo --> Opcional 
	 *  -- convenio --> Opcional 
	 *  -- viaIngreso --> Opcional 
	 *  -- tipoAfiliado --> Opcional 
	 * @return ArrayList<HashMap<String,Objet>>
	 * --------------------------------------------------------
	 * 	KEY'S DEL MAPA DENTRO DEL ARRAY QUE DEVUELVE EL METODO
	 * --------------------------------------------------------
	 * -- codigo
	 * -- convenio
	 * -- viaIngreso
	 * -- tipoAfiliado
	 * -- estratoSocial
	 * -- tipoMonto
	 * -- valor
	 * -- porcentaje
	 * -- activo
	 */
	public ArrayList<HashMap<String, Object>> obtenerMontosCobroVigentes (Connection connection,HashMap criterios)
	{
		return SqlBaseUtilidadesManejoPacienteDao.obtenerMontosCobroVigentes(connection, criterios);
	}
	
	
	/**
	 * Metodo encargado de consultar todos los tipos de 
	 * paciente
	 * @param connection
	 * @return ArrayList<HashMap<String, Object>> TiposPaciente
	 * ----------------------------------------------------
	 * KEY'S DEL HASHMAP DEL ARRAYLIST DE TIPOS PACIENTE
	 * ----------------------------------------------------
	 * -- acronimo
	 * -- nombre
	 */
	public  ArrayList<HashMap<String, Object>>ObtenerTiposPaciente (Connection connection, int viaIngreso)
	{
		return SqlBaseUtilidadesManejoPacienteDao.ObtenerTiposPaciente(connection,viaIngreso);
	}
	
	
	/**
	 * Adicionado por Jhony Alexander Duque A.
	 * 16/01/2008
	 * Metodo encargado de consultar las areas de ingreso
	 * por via de ingreso.
	 * @param connection
	 * @param criterios
	 * -----------------------------------------------
	 * 			KEY'S DEL HASHMAP CRITERIOS
	 * -----------------------------------------------
	 *  -- institucion --> Requerido
	 *  -- viaIngreso --> Opcional
	 *  -- centroCosto --> Opcional
	 * @return rrayList<HashMap<String, Object>> areaIngreso
	 * ----------------------------------------------------
	 * KEY'S DEL HASHMAP DENTRO DEL ARRAY AREA INGRESO
	 * ----------------------------------------------------
	 * -- codigo
	 * -- nombre
	 * -- centroCosto
	 * -- viaIngreso
	 */
	public ArrayList<HashMap<String, Object>>ObtenerAreaIngreso (Connection connection,HashMap criterios)
	{
		return SqlBaseUtilidadesManejoPacienteDao.ObtenerAreaIngreso(connection, criterios);
	}
	
	/**
	 * Método implementado para consultar los usuarios que han registrado ingresos en la estructura de Pacientes entidades subcontratadas 
	 * 
	 * Key's login,nombre
	 * @param con
	 * @param HashMap campos
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> obtenerUsuariosEntidadesSubcontratadas(Connection con,HashMap campos)
	{
		return SqlBaseUtilidadesManejoPacienteDao.obtenerUsuariosEntidadesSubcontratadas(con,campos);
	}
	
	/**
	 * Método implementado para obtener los constratos vigentes de un usuario capitado
	 * @param con
	 * @param campos
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> obtenerContratosVigentesUsuarioCapitado(Connection con,HashMap campos)
	{
		return SqlBaseUtilidadesManejoPacienteDao.obtenerContratosVigentesUsuarioCapitado(con, campos);
	}
	/**
	 * Método implementado para obtener los constratos vigentes de un usuario capitado
	 * @param con
	 * @param campos
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> obtenerContratosUsuarioCapitado(Connection con,HashMap campos)
	{
		return SqlBaseUtilidadesManejoPacienteDao.obtenerContratosUsuarioCapitado(con, campos);
	}
	/**
	 * Método para obtener la descripcion de un tipo cie
	 * @param con
	 * @param codigo
	 * @return
	 */
	public String obtenerDescripcionTipoCie(Connection con,int codigo)
	{
		return SqlBaseUtilidadesManejoPacienteDao.obtenerDescripcionTipoCie(con, codigo);
	}
	
	/**
	 * Adicionado por: Giovanny Arias
	 * 07/03/08
	 * Método para obtener la cama asignada a una admision segun el codigo de la cuenta
	 * @param con
	 * @param codigo cuenta
	 * @return codigo cama
	 */
	public int obtenerCamaAdmision(Connection con,int codigo)
	{
		return SqlBaseUtilidadesManejoPacienteDao.obtenerCamaAdmision(con, codigo);
	}
	
	/**
	 * Método para consultar los estados de la cuenta
	 * @param con
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> obtenerEstadosCuenta(Connection con)
	{
		return SqlBaseUtilidadesManejoPacienteDao.obtenerEstadosCuenta(con);
	}
	
	/**
	 * Método implmentado para listar los tipos de monitoreo
	 * @param con
	 * @param codigoInstitucion
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> obtenerTiposMonitoreo(Connection con,int codigoInstitucion)
	{
		return SqlBaseUtilidadesManejoPacienteDao.obtenerTiposMonitoreo(con, codigoInstitucion);
	}
	
	/**
	 * Método que retorna si un paciente tiene o no, un preingreso pendiente
	 * @param con
	 * @param codigoInstitucio
	 * @param codigoPaciente
	 * @return codigoIngreso
	 */
	public int tienePreingresoPendiente(Connection con, int codigoInstitucion, int codigoPaciente)
	{
		return SqlBaseUtilidadesManejoPacienteDao.tienePreingresoPendiente(con, codigoInstitucion, codigoPaciente);
	}
	
	/**
	 * Método que retorna si un ingreso tiene o no, un preingreso pendiente
	 * @param con
	 * @param codigoInstitucio
	 * @param codigoPaciente
	 * @return
	 */
	public boolean ingresoConPreingresoPendiente(Connection con,int codigoInstitucion, int codigoIngreso)
	{
		return SqlBaseUtilidadesManejoPacienteDao.ingresoConPreingresoPendiente(con, codigoInstitucion, codigoIngreso);
	}
	
	/**
	 * Método que retorna si un paciente tiene o no, un preingreso pendiente
	 * @param con
	 * @param codigoInstitucio
	 * @param codigoPaciente
	 * @return codigoIngreso
	 */
	public int tienePreingresoPendienteXCentroAtencion(Connection con, int codigoInstitucion, int centroAtencion, int codigoPaciente)
	{
		return SqlBaseUtilidadesManejoPacienteDao.tienePreingresoPendienteXCentroAtencion(con, codigoInstitucion, centroAtencion, codigoPaciente);
	}
	
	/**
	 * Método implementado para obtener el origen de admision de una cuenta
	 * @param con
	 * @param codigoCuenta
	 * @return
	 */
	public InfoDatosInt obtenerOrigenAdmisionCuenta(Connection con,int codigoCuenta)
	{
		return SqlBaseUtilidadesManejoPacienteDao.obtenerOrigenAdmisionCuenta(con, codigoCuenta);
	}
	
	/**
	 * Método implementado para actualizar el estado del preingreso
	 * @param con
	 * @param ingreso
	 * @param estado
	 * @return
	 */
	public boolean actualizarEstadoPreingreso(Connection con, int ingreso, String estado, String usuario)
	{
		return SqlBaseUtilidadesManejoPacienteDao.actualizarEstadoPreingreso(con, ingreso, estado, usuario,DaoFactory.POSTGRESQL);
	}
	
	/**
	 * Método implementado para consultar si el ingreso tuvo un cierre manual
	 * @param con
	 * @param ingreso
	 * @return
	 */
	public boolean tieneIngresoManual(Connection con, int ingreso)
	{
		return SqlBaseUtilidadesManejoPacienteDao.tieneIngresoManual(con, ingreso);
	}
	
	/**
	 * Método implementado para consultar el reingreso de un ingreso; si no tiene retorna (-1)
	 * @param con
	 * @param ingreso
	 * @return
	 */
	public int obtenerReingreso(Connection con, int ingreso)
	{
		return SqlBaseUtilidadesManejoPacienteDao.obtenerReingreso(con, ingreso);
	}
	
	/**
	 * Método implementado para obtener los ingresosCerradosPendientesXFacturar
	 * (si el ingreso se encuentra en estado cerrado con indicativo de cierre manual y cuenta en estado activa, asociada o facturacion parcial)
	 * @param con
	 * @param ingreso
	 * @return
	 */
	public String ingresosCerradosPendientesXFacturar(Connection con, int paciente)
	{
		return SqlBaseUtilidadesManejoPacienteDao.ingresosCerradosPendientesXFacturar(con, paciente);
	}
	
	/**
	 * Metodo encargado de consultar la fecha de apertura de la cuenta.
	 * @param connection
	 * @param cuenta
	 * @return fecha apertura
	 */
	public  String obtenerFechaAperturaCuenta (Connection connection, String cuenta)
	{
		return SqlBaseUtilidadesManejoPacienteDao.obtenerFechaAperturaCuenta(connection, cuenta);
	}
	
	/**
	 * Método para verificar si es requerido la verificación de derechos
	 * @param con
	 * @param campos
	 * @return
	 */
	public boolean esRequeridaVerificacionDerechos(Connection  con,HashMap campos)
	{
		return SqlBaseUtilidadesManejoPacienteDao.esRequeridaVerificacionDerechos(con, campos);
	}
	
	/**
	 * Método que verifica si existe verificación de derechos por la subcuenta
	 * @param con
	 * @param subCuenta
	 * @return
	 */
	public boolean existeVerificacionDerechosSubcuenta(Connection con,String subCuenta)
	{
		return SqlBaseUtilidadesManejoPacienteDao.existeVerificacionDerechosSubcuenta(con, subCuenta);
	}
	
	/**
	 * Método paara verificar si existe un ingreso cuidado especial activo
	 * @param con
	 * @param campos
	 * @return
	 */
	public boolean existeIngresoCuidadoEspecialActivo(Connection con,HashMap campos)
	{
		return SqlBaseUtilidadesManejoPacienteDao.existeIngresoCuidadoEspecialActivo(con, campos);
	}
	
	/**
	 * Método que verifica si el ingreso es de control postoperatorio
	 * @param con
	 * @param codigoIngreso
	 * @return
	 */
	public boolean esIngresoControlPostOperatorioCx(Connection con,String codigoIngreso)
	{
		return SqlBaseUtilidadesManejoPacienteDao.esIngresoControlPostOperatorioCx(con, codigoIngreso);
	}
	
	/**
	 * 
	 */
	public ArrayList<HashMap<String, Object>> obtenerCentrosCostoViaingreso(Connection con, int institucion, String tiposArea, boolean todos, int centroAtencion) {
		
		return SqlBaseUtilidadesManejoPacienteDao.obtenerCentrosCostoViaingreso(con, institucion, tiposArea, todos, centroAtencion);
	}
	
		/**
	 * Metodo encargado de consultar la fecha de apertura del ingreso
	 * @param connection
	 * @param ingreso
	 * @return fecha apertura
	 */
	public String obtenerFechaAperturaIngreso (Connection connection, String ingreso)
	{
		return SqlBaseUtilidadesManejoPacienteDao.obtenerFechaAperturaIngreso(connection, ingreso);
	}
	
	/**
	 * Método implementado para obtener el codigo del tipo de monitoreo del ingreso a cuidado especial
	 * @param con
	 * @param campos
	 * @return
	 */
	public int obtenerCodigoTipoMonitoreoIngresoCuidadoEspecial(Connection con,HashMap campos)
	{
		return SqlBaseUtilidadesManejoPacienteDao.obtenerCodigoTipoMonitoreoIngresoCuidadoEspecial(con, campos);
	}
	/**
	 * Método que actualiza el tipo de monitoreo de un ingresi cuidado especial
	 * @param con
	 * @param campos
	 * @return
	 */
	public int actualizarTipoMonitoreoIngresoCuidadoEspecial(Connection con,HashMap campos)
	{
		return SqlBaseUtilidadesManejoPacienteDao.actualizarTipoMonitoreoIngresoCuidadoEspecial(con, campos);
	}
	
	/**
	 * Método implementado para reversar un ingreso cuidado especial
	 * @param con
	 * @param campos
	 * @return
	 */
	public boolean reversarIngresoCuidadoEspecial(Connection con,HashMap campos)
	{
		return SqlBaseUtilidadesManejoPacienteDao.reversarIngresoCuidadoEspecial(con, campos);
	}
	
	/**
	 * Método implementado para obtener los tipos de reporte
	 * @param con
	 * @param campos
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> obtenerTiposReporte(Connection con,HashMap campos)
	{
		return SqlBaseUtilidadesManejoPacienteDao.obtenerTiposReporte(con, campos);
	}
	
	/**
	 * Método implementado para insertar el log de reportes
	 * @param con
	 * @param campos
	 * @return
	 */
	public int insertarLogReportes(Connection con,HashMap campos)
	{
		return SqlBaseUtilidadesManejoPacienteDao.insertarLogReportes(con, campos);
	}
	
	/**
	 * Carga todas las cuentas del paciente
	 * @param Connection con
	 * @param String codigoPaciente
	 * */
	public ArrayList cuentasPaciente(Connection con,HashMap parametros)
	{
		return SqlBaseUtilidadesManejoPacienteDao.cuentasPaciente(con,parametros);
	}
	
	/**
	 * Inserta el log de las autorizaciones ingresadas al paciente
	 * @param Connection con
	 * @param String autorizacion
	 * @param String numeroId
	 * @param String tipoId
	 * @param UsuarioBasico usuario
	 * @param String funcionalidad
	 * */
	public boolean insertarLogAutorizacionIngresoEvento(
			Connection con,
			HashMap parametros)
	{
		return SqlBaseUtilidadesManejoPacienteDao.insertarLogAutorizacionIngresoEvento(con,parametros);
	}
	
	/**
	 * Método para obtener el identificador del centro de costo
	 * @param con
	 * @param codigo
	 * @return
	 */
	public String obtenerIdentificadorCentroCosto(Connection con,int codigo)
	{
		return SqlBaseUtilidadesManejoPacienteDao.obtenerIdentificadorCentroCosto(con, codigo);
	}
	
	/**
	 * Método para obtener el número de carnet del ingreso del paciente
	 * @param con
	 * @param codigoIngreso
	 * @return
	 */
	public String obtenerNroCarnetIngresoPaciente(Connection con,int codigoIngreso)
	{
		return SqlBaseUtilidadesManejoPacienteDao.obtenerNroCarnetIngresoPaciente(con, codigoIngreso);
	}
	
	/**
	 * Método para consultar el codigo paciente de un ingreso
	 * @param con
	 * @param codigoIngreso
	 * @return
	 */
	public int obtenerCodigoPacienteXIngreso(Connection con,int codigoIngreso)
	{
		return SqlBaseUtilidadesManejoPacienteDao.obtenerCodigoPacienteXIngreso(con, codigoIngreso);
	}
	
	/**
	 * Metodo que devuelve un Arraylist de HashMap
	 * con la informacion de los centros de atencion.
	 * Contiene los siguientes Key's -> codigo , nomcentrocosto,
	 * codigotipoarea,nombretipoarea,activo,identificador,manejacamas,
	 * unidadfuncional,descunidadfuncional,codcentroatencion,
	 * nomcentroatencion, viaingtipopac.
	 * @author Jhony Alexander Duque A.
	 * @param con --> Requerido
	 * @param institucion --> Requerido
	 * @param tiposArea -->No Requerido
	 * @param todos --> False o true
	 * @param centroAtencion --> Requerido para todos = 0
	 * @param manejanCamas --> Opcional
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> obtenerCentrosCosto(Connection con, int institucion, String tiposArea, boolean todos, int centroAtencion, String manejanCamas)
	{
		return SqlBaseUtilidadesManejoPacienteDao.obtenerCentrosCosto(con, institucion, tiposArea, todos, centroAtencion, manejanCamas);
	}
	
	/**
	 * Método que verifica si está el centro de costo asociado a un tipo de monitoreo específico
	 * @param con
	 * @param centroCosto
	 * @param tipoMonitoreo
	 * @return
	 */
	public boolean estaCentroCostoEnTipoMonitoreo(Connection con,int centroCosto,int tipoMonitoreo)
	{
		return SqlBaseUtilidadesManejoPacienteDao.estaCentroCostoEnTipoMonitoreo(con, centroCosto, tipoMonitoreo);
	}
	
	/**
	 * Metodo encargado de consultar y devolver las cuentas de un ongreso
	 * separadas por comas. 
	 * @param connection
	 * @param ingreso
	 * @return
	 */
	public String obtenerCuentasXIngreso (Connection connection, String ingreso)
	{
		return SqlBaseUtilidadesManejoPacienteDao.obtenerCuentasXIngreso(connection, ingreso);
	}

	/**
	 * Metodo encargado de consultar si un registro de sol_cirugia_por_servicio se encuentra
	 * en la tabla de informacion_parto
	 * @param connection
	 * @param codigoSolCxServ
	 * @return consecutivo informacion parto (-1 si no encontro nada)
	 */
	public int obtenerConsecutivoPartoXcodigoSolCxServ (Connection connection, String codigoSolCxServ )
	{
		return SqlBaseUtilidadesManejoPacienteDao.obtenerConsecutivoPartoXcodigoSolCxServ(connection, codigoSolCxServ);
	}
	
	/**
	 * Método para obtener el codigo del medico tratante
	 * @param con
	 * @param ingreso
	 */
	public int obtenerCodigoMedicoTratante(Connection con, int ingreso)
	{
		return SqlBaseUtilidadesManejoPacienteDao.obtenerCodigoMedicoTratante(con, ingreso);
	}
	
	/**
	 * Metodo encargado de consultar algunos datos del ingreso.
	 * @param connection
	 * @param ingreso
	 * @return HashMap
	 */
	public  HashMap obtenerDatosIngreso (Connection connection, String ingreso,String institucion)
	{
		return SqlBaseUtilidadesManejoPacienteDao.obtenerDatosIngreso(connection, ingreso, institucion);
	}
	
	/**
	 * Metodo encargado de consultar el codigo de interfaz segun la via de ingreso y el tipo de paciente.
	 * @param connection
	 * @param viaIngreso
	 * @param tipoPaciente
	 * @return
	 */
	public String obtenerCodInterfazXViaIngresoTipoPac(Connection connection, int viaIngreso, String tipoPaciente)
	{
		return SqlBaseUtilidadesManejoPacienteDao.obtenerCodInterfazXViaIngresoTipoPac(connection, viaIngreso, tipoPaciente);
	}
	
	/**
	 * Metodo encargado de consultar el codigo de interfaz segun la cuenta.
	 * @param connection
	 * @param cuenta
	 * @return
	 */
	public String obtenerCodInterfazXCuenta(Connection connection, String cuenta)
	{
		return SqlBaseUtilidadesManejoPacienteDao.obtenerCodInterfazXCuenta(connection, cuenta);
	}
	
	/**
	 * Método implementado para cargar los tipos de servicios solicitados
	 * @param con
	 * @param campos
	 * @return
	 */
	public ArrayList<HashMap<String,Object>> obtenerTiposServicioSolicitados(Connection con,HashMap campos)
	{
		return SqlBaseUtilidadesManejoPacienteDao.obtenerTiposServicioSolicitados(con, campos);
	}
	
	/**
	 * Método que consulta el tipo de cobertura de una sub cuenta
	 * @param con
	 * @param idSubCuenta
	 * @return
	 */
	public InfoDatosInt obtenerTipoCoberturaSubCuenta(Connection con,String idSubCuenta)
	{
		return SqlBaseUtilidadesManejoPacienteDao.obtenerTipoCoberturaSubCuenta(con, idSubCuenta);
	}
	
	/**
	 * Método para obtener las coberturas salud por tipo de regimen de una sub_cuenta
	 * @param con
	 * @param campos
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> obtenerCoberturasSaludXTipoRegimenSubCuenta(Connection con,HashMap campos)
	{
		return SqlBaseUtilidadesManejoPacienteDao.obtenerCoberturasSaludXTipoRegimenSubCuenta(con, campos);
	}
	
	/**
	 * Método para obtener las coberturas de salud x el tipo de régimen
	 * @param con
	 * @param campos
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> obtenerCoberturasSaludXTipoRegimen(Connection con,HashMap campos)
	{
		return SqlBaseUtilidadesManejoPacienteDao.obtenerCoberturasSaludXTipoRegimen(con, campos);
	}
	
	/**
	 * Obtiene los convenios por ingreso
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public ArrayList<HashMap<String,Object>> obtenerConveniosXIngreso(Connection con,HashMap parametros)
	{
		return SqlBaseUtilidadesManejoPacienteDao.obtenerConveniosXIngreso(con,parametros);
	}
	
	/**
	 * obtiene los profesionales que ordenan solicitud/es
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public ArrayList<HashMap<String,Object>> obtenerProfSolicitaSolicitudes(Connection con, HashMap parametros)
	{
		return SqlBaseUtilidadesManejoPacienteDao.obtenerProfSolicitaSolicitudes(con, parametros);
	}
	
	/**
	 * Método para obtener los convenios del ingreso y su informacion de autorizacion asociada
	 * @param con
	 * @param campos
	 * @return
	 */
	public ArrayList<DtoAutorizacion> obtenerConveniosIngresoAutorizacionesAdmision(Connection con,HashMap<String,Object> campos)
	{
		return SqlBaseUtilidadesManejoPacienteDao.obtenerConveniosIngresoAutorizacionesAdmision(con, campos);
	}
	
	/**
	 * Método para obtener el estado de la autorizacion de admisión de una subcuenta
	 * @param con
	 * @param campos
	 * @return
	 */
	public ArrayList<DtoAutorizacion> obtenerAutorizacionesAdmisionSubCuenta(Connection con,HashMap<String,Object> campos)
	{
		return SqlBaseUtilidadesManejoPacienteDao.obtenerAutorizacionesAdmisionSubCuenta(con, campos);
	}
	
	/**
	 * Método para obtener el tipo de entidad del centro de costo que ejecuta
	 * @param con
	 * @param codigoCentroCosto
	 * @return
	 */
	public String obtenerTipoEntidadEjecutaCentroCosto(Connection con,int codigoCentroCosto)
	{
		return SqlBaseUtilidadesManejoPacienteDao.obtenerTipoEntidadEjecutaCentroCosto(con, codigoCentroCosto);
	}
	
	/**
	 * Método para cargar la descripcion de la entidaad subcontratad de la autorizacion de una solicitud
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public String consultarDescripcionEntidadSubcontratadaAutorizacionSolicitud(Connection con,String numeroSolicitud)
	{
		return SqlBaseUtilidadesManejoPacienteDao.consultarDescripcionEntidadSubcontratadaAutorizacionSolicitud(con, numeroSolicitud);
	}

	/**
	 * 
	 */
	public ArrayList<Paciente> obtenerDatosPaciente(Paciente obj) {
		
		return SqlBaseUtilidadesManejoPacienteDao.obtenerDatosPaciente(obj);
	}
	
	/**
	 * Método para cargar los datos básicos de la cuenta
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public DtoCuentas consultarDatosCuenta(Connection con,BigDecimal idCuenta)
	{
		return SqlBaseUtilidadesManejoPacienteDao.consultarDatosCuenta(con, idCuenta);
	}
	
	/**
	 * Método para consultar el numero de autorizacion de ingreso
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public String obtenerAutorizacionIngresoXFactura(Connection con, String codigoFactura)
	{
		return SqlBaseUtilidadesManejoPacienteDao.obtenerAutorizacionIngresoXFactura(con, codigoFactura);
	}
	
	/**
	 * Método para verificar si un paciente tiene un ingreso abierto sin importar a cual centro de atención hace parte
	 * @param con
	 * @param codigoPaciente
	 * @return
	 */
	public boolean tienePacienteIngresoAbierto(Connection con,BigDecimal codigoPaciente)
	{
		return SqlBaseUtilidadesManejoPacienteDao.tienePacienteIngresoAbierto(con, codigoPaciente);
	}
	
	@Override
	public int obtenerCentroAtencionDuenioPaciente(Connection con,
			int codigoPaciente) {
		return SqlBaseUtilidadesManejoPacienteDao.obtenerCentroAtencionDuenioPaciente(con, codigoPaciente);
	}
	
	
	

	@Override
	public DtoInfoAmparosReclamacion consultarInformacionAmparosReclamacion(Connection con, String ingreso) 
	{
		return SqlBaseUtilidadesManejoPacienteDao.consultarInformacionAmparosReclamacion(con, ingreso);
	}
	
	@Override
	public ArrayList<DtoIngresos> consultarInformacionAmparosReclamacion(
			int codigoPaciente) {
		return SqlBaseUtilidadesManejoPacienteDao.consultarInformacionAmparosReclamacion(codigoPaciente);
	}

	
	
	@Override
	public ArrayList<DtoIngresos> consultarIngresosRegistrosEventosYAccidentes(int codigoPaciente)
	{
		return SqlBaseUtilidadesManejoPacienteDao.consultarIngresosRegistrosEventosYAccidentes(codigoPaciente);
	}

	@Override
	public ArrayList<DtoFactura> consultarFacturasIngresosConveniosAseguradoras(
			int ingreso) {
		return SqlBaseUtilidadesManejoPacienteDao.consultarFacturasIngresosConveniosAseguradoras(ingreso);
	}
	
	
	/**
	 * 
	 * @param codigo
	 * @return
	 */
	public String facturaTieneReclamacionRadicadaPrevia(int codigo)
	{
		return SqlBaseUtilidadesManejoPacienteDao.facturaTieneReclamacionRadicadaPrevia(codigo);
	}

	@Override
	public DtoCertAtenMedicaFurips cargarCetificacionMedicaFurips(int ingreso) {
		return SqlBaseUtilidadesManejoPacienteDao.cargarCetificacionMedicaFurips(ingreso,DaoFactory.POSTGRESQL);
	}

	@Override
	public DtoCertAtenMedicaFurpro cargarCetificacionMedicaFurpro(int ingreso) {
		return SqlBaseUtilidadesManejoPacienteDao.cargarCetificacionMedicaFurpro(ingreso);
	}

	
	@Override
	public HashMap<String, String> obtenerUltimosSignosVitalesRE(
			Connection con, int idCuenta) {
		return SqlBaseUtilidadesManejoPacienteDao.obtenerUltimosSignosVitalesRE(con,idCuenta);
	}
	
	
}