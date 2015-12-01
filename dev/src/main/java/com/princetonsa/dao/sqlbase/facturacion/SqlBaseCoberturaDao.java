package com.princetonsa.dao.sqlbase.facturacion;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.UtilidadBD;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.facturacion.InfoCobertura;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.mundo.facturacion.Cobertura;
import com.servinte.axioma.fwk.exception.BDException;
import com.servinte.axioma.fwk.exception.util.CODIGO_ERROR_NEGOCIO;

/**
 * @author Jose Eduardo Arias Doncel
 * jearias@princetonsa.com  
 * */

public class SqlBaseCoberturaDao
{
 
	/**
	 * Objeto para manejar log de la clase  
	 * */
	private static Logger logger = Logger.getLogger(SqlBaseCoberturaDao.class);
 
	/**
	 * Cadena de insercion de coberturas
	 * */
	private static final String cadenaInsertarStr = "INSERT INTO facturacion.cobertura (codigo_cobertura,institucion,descrip_cobertura,observ_cobertura,activo,usuario_modifica,fecha_modifica,hora_modifica , tipo_cobertura) VALUES(?,?,?,?,?,?,?,?,?) ";
 
	/**
	 * Cadena de modificacion de coberturas, permite modificar codigo_cobertura
	 * */ 
	private static final String cadenaModificarTStr = "UPDATE facturacion.cobertura SET codigo_cobertura=?, descrip_cobertura=?, observ_cobertura=?, activo=?, usuario_modifica=?, fecha_modifica=?, hora_modifica=?  , tipo_cobertura=?  WHERE codigo_cobertura=? AND institucion=? ";
	
	/**
	 * Cadena de modificacion de coberturas, actualiza todos los datos menos el codigo de la cobertura
	 * */ 
	private static final String cadenaModificarBStr = "UPDATE facturacion.cobertura SET descrip_cobertura=?, observ_cobertura=?, activo=?, usuario_modifica=?, fecha_modifica=?, hora_modifica=? , tipo_cobertura=?  WHERE codigo_cobertura=? AND institucion=? ";
 
	/**
	 * Cadena de eliminacion de coberturas 
	 * */ 
	private static final String cadenaEliminarStr = "DELETE FROM facturacion.cobertura WHERE codigo_cobertura=? AND institucion=? ";
	
	/**
	 * vector sting de indice del mapa de coberturas
	 * */
	private static final String[] indicesMapa = {"codigo_","institucion_","descripcion_","observacion_","estado_","usuariomodifica_","fechamodifica_","horamodifica_","ultimamodificacion_","estabd_","depende_","codigoOld_", "tipocobertura_"};
 
	/**
	 * Cadena de consulta de coberturas
	 * */
	private static String cadenaConsultaStr = "SELECT "+
	 													 "codigo_cobertura AS codigo, "+
	 													 "institucion AS institucion, "+
	 													 "descrip_cobertura AS descripcion, "+
	 													 "observ_cobertura AS observacion, "+
	 													 "tipo_cobertura AS tipocobertura, "+
	 													 "activo AS estado, "+
	 													 "usuario_modifica AS usuariomodifica, "+
	 													 "fecha_modifica AS fechamodifica, "+
	 													 "hora_modifica AS horamodifica, " +
	 													 "usuario_modifica || '_' || to_char(fecha_modifica,'DD/MM/YYYY') || '_' || substr(hora_modifica,0,6) AS ultimamodificacion, "+
	 													 "'"+ConstantesBD.acronimoSi+"' AS estabd," +
	 													 "'"+ConstantesBD.acronimoNo+"' AS depende " +	 													 
	 											   "FROM facturacion.cobertura ";
	 											   
 
	/**
	 * Insertar un registro de coberturas
	 * @param con
	 * @param Cobertura cobertura
	 * */
	public static boolean insertarCobertura(Connection con, Cobertura cobertura)
	{
		try
		{			
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaInsertarStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			int codigoCobertura=UtilidadBD.obtenerSiguienteValorSecuencia("odontologia.seq_coberturas");
			cobertura.setCodigoCobertura(codigoCobertura+"");
			ps.setString(1,cobertura.getCodigoCobertura().trim());
			ps.setInt(2,cobertura.getInstitucion());
			ps.setString(3,cobertura.getDescripcionCobertura());
			ps.setString(4,cobertura.getObservacionCobertura());
			ps.setString(5,cobertura.getEstadoCobertura());
			ps.setString(6,cobertura.getUsuarioModifica());
			ps.setDate(7,Date.valueOf(cobertura.getFechaModifica()));
			ps.setString(8,cobertura.getHoraModifica());			
			ps.setString(9, cobertura.getTipoCobertura());
			if(ps.executeUpdate()>0)
			  return true;	
			
		}
		catch(SQLException e)
		{
			e.printStackTrace();	  
		}
		return false;
	}
	
	
	/**
	 * Modifica una cobertura registrada
	 * @param con
	 * @param Cobertura cobertura
	 * */
	public static boolean modificarCobertura(Connection con, Cobertura cobertura)
	{
		try		
		{
			//if(cobertura)
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaModificarTStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1,cobertura.getCodigoCobertura()); 
			ps.setString(2,cobertura.getDescripcionCobertura());
			ps.setString(3,cobertura.getObservacionCobertura());
			ps.setString(4,cobertura.getEstadoCobertura());
			ps.setString(5,cobertura.getUsuarioModifica());
			ps.setDate(6,Date.valueOf(cobertura.getFechaModifica()));
			ps.setString(7,cobertura.getHoraModifica());			
			ps.setString(8,cobertura.getTipoCobertura());
			ps.setString(9,cobertura.getCodigoCoberturaOld());		
			ps.setInt(10,cobertura.getInstitucion());
			
			
			if(ps.executeUpdate()>0)
				return true;
		}	
		catch(SQLException e)
		{
			e.printStackTrace();						
		}
		
		return false;
	}
	
	
	/**
	 * Elimina una cobertura registrada
	 * @param con
	 * @param Cobertura cobertura
	 * */
	
	public static boolean eliminarCobertura(Connection con, String codigoCobertura, int institucion)
	{
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaEliminarStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1,codigoCobertura);
			ps.setInt(2,institucion);	
			
			if(ps.executeUpdate()>0)
				return true;
			
		}				
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		return false;
	}
	
	/**
	 * Consulta basica de coberturas por keys
	 * @param con
	 * @param codigoCobertura
	 * @param institucion
	 * */
	public static HashMap consultaCoberturaBasica(Connection con, String codigoCobertura, int institucion)
	{
		HashMap mapa = new HashMap();
		mapa.put("numRegistros",0);
		
		String cadenaConsulta= cadenaConsultaStr+"WHERE codigo_cobertura=? " +
												  "AND  institucion=? ";			
		
		try
		{
			PreparedStatementDecorator ps;
			ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaConsulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1,codigoCobertura);
			ps.setInt(2,institucion);	
			mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));		 	 
		}
		catch(SQLException e)
		{
			e.printStackTrace(); 	
		}		
		
		mapa.put("INDICES_MAPA",indicesMapa);
		
		return mapa;
	}
	
	/**
	 * Verifica que otra funcionalidad no dependa del codigoCobertura
	 * @param Connection con
	 * @param String codigoCobertura
	 * */
	public static boolean verificarDependencia(Connection con,String codigoCobertura)
	{
		return false;
		//return true;
	}
	
	/**
	 * Consulta avanzada de coberturas por cada uno de los campos
	 * @param con
	 * @param HashMap condicion 
	 * */

	public static HashMap consultaCoberturaAvanzada(Connection con,HashMap condicion)
	{	    
		String cadenaConsulta=""; 
		int numCol=0,i=0;
		String[] cadenaTmp={"","","","","","","","","",""};	
		
		if(!condicion.get("flagInicio").equals("true"))
		{
			if(!condicion.get("codigoC").equals("")){
				cadenaTmp[numCol]= " codigo_cobertura="+condicion.get("codigoC").toString();
				numCol++;
			}
		 
			if(!condicion.get("institucionC").equals("")){
				cadenaTmp[numCol]= " institucion="+condicion.get("institucionC").toString();
				numCol++; 
			}
		  
			if(!condicion.get("descripcionC").equals("")){
				cadenaTmp[numCol]= " descrip_cobertura="+condicion.get("descripcionC").toString();
				numCol++;
			}
		 
			if(!condicion.get("observacionC").equals("")){
				cadenaTmp[numCol]= " observ_cobertura="+condicion.get("observacionC").toString();
				numCol++;
			}
		 
			if(!condicion.get("estadoC").equals("")){
				cadenaTmp[numCol]= " activo='"+condicion.get("estadoC").toString()+"'";
				numCol++;
			}	
			
			if(!condicion.get("tipoC").equals("")){
				cadenaTmp[numCol]= " tipo_cobertura='"+condicion.get("tipoC").toString()+"'";
				numCol++;
			}	
		 
			if(numCol>0){	 
				cadenaConsulta=" WHERE "; 	 
				while(i < numCol){
					cadenaConsulta+=cadenaTmp[i];	  
					if(i < (numCol-1))
						cadenaConsulta+= " AND ";  	
					i++;
				}
			}
		}
		else
			cadenaConsulta =" WHERE institucion="+condicion.get("institucionC").toString();
			
		 
		cadenaConsulta= cadenaConsultaStr+" "+cadenaConsulta+"  ";
		
		 
		HashMap mapa = new HashMap();
		mapa.put("numRegistros","0");
		PreparedStatementDecorator ps;	
		
		try{
		 ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaConsulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		 mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));	 
		}
		catch(SQLException e){		
		 e.printStackTrace(); 	
		}	
		
		mapa.put("INDICES_MAPA",indicesMapa);	
		Utilidades.imprimirMapa(mapa);
		return mapa;	
	}
	
	
	///////////////////////////////////////////VALIDACIONES DE LA COBERTURA
	
	
	//************************VALIDACION COBERTURA ENTIDADES SUBCONTRATADAS*********************************************************
	
	/**
	 * Método implementado para validar la cobertura de un servicio por flujo de entidades subcontratadas
	 */
	public static InfoCobertura validacionCoberturaServicioEntidadSub(Connection con,long codigoContrato,int codigoViaIngreso,String tipoPaciente,int codigoServicio,int codigoNaturalezaPaciente, int codigoInstitucion ) throws BDException
	{
		InfoCobertura infoCobertura = new InfoCobertura();
		//La filosofia de cobertura es primero buscar las EXCEPCIONES de esta y luego buscar las coberturas
		logger.info("**********************OBTENER COBERTURA X SERVICIO***************************");
		//primero obtenemos lo de la excepcion
		infoCobertura=obtenerExcepcionCoberturaServicioEntidadSub(con, codigoContrato, codigoViaIngreso, tipoPaciente, codigoServicio, codigoNaturalezaPaciente, codigoInstitucion);
		
		if(infoCobertura.existe())
		{
			//no se debe seguir buscando existe en la excepcion 
			return infoCobertura;
		}
		//de lo contrario se busca en las coberturas
		logger.info("no existia en las excpciones por lo tanto se busca en las coberturas");
		infoCobertura= obtenerCoberturaServicioEntidadSub(con, codigoContrato, codigoViaIngreso, tipoPaciente, codigoServicio, codigoNaturalezaPaciente, codigoInstitucion);
		
		return infoCobertura;
	}
	
	/**
	 * metodo que obtiene la cobertura x servicio
	 * @param con
	 * @param codigoContrato
	 * @param codigoViaIngreso
	 * @param codigoServicio
	 * @param codigoNaturalezaPaciente
	 * @param codigoInstitucion
	 * @return
	 */
	private static InfoCobertura obtenerCoberturaServicioEntidadSub(Connection con, long codigoContrato, int codigoViaIngreso, String tipoPaciente, int codigoServicio, int codigoNaturalezaPaciente, int codigoInstitucion) throws BDException
	{
		logger.info("**********************OBTENER COBERTURA (NO EXISTE EXCEPCION)***************************");
		InfoCobertura infoCobertura= new InfoCobertura();

		Vector codigosDetalleCoberturaGeneral= obtenerCodigoDetalleCoberturaGeneralEntidadSub(con,codigoContrato, codigoViaIngreso, tipoPaciente, codigoNaturalezaPaciente,  codigoInstitucion);
		
		if(codigosDetalleCoberturaGeneral.size()<1)
		{
			//no se debe seguir buscando no existe  
			logger.info("->NO EXISTE COBERTURA!!!!");
			return infoCobertura;
		}
		logger.info("\n\nNUMERO DETALLES COBERTURA-->"+codigosDetalleCoberturaGeneral.size());
		for(int w=0; w<codigosDetalleCoberturaGeneral.size(); w++)
		{	
			logger.info("\n\n*****************************CODIGO DETALLE COBERTURA "+codigosDetalleCoberturaGeneral.get(w)+"**************w-->"+w);
			//si llega aca entonces se debe evaluar por servicio especifico
			infoCobertura= obtenerCoberturaServicioEspecifico(con,Integer.parseInt(codigosDetalleCoberturaGeneral.get(w).toString()), codigoServicio);
			if(infoCobertura.existe())
			{
				//no se debe seguir buscando existe para el servicio mandamos el String cargado
				logger.info("Existe cobertura por servicio especifico!!!! cubre?->"+infoCobertura.getIncluido());
				return infoCobertura;
			}
			
			//de lo contrario debe seguir buscando en los grupos
			infoCobertura= obtenerCoberturaServicioAgrupado(con, Integer.parseInt(codigosDetalleCoberturaGeneral.get(w).toString()), codigoServicio);
			if(infoCobertura.existe())
			{
				//no se debe seguir buscando existe para el servicio mandamos el String cargado
				logger.info("Existe cobertura por servicio agupado!!!! cubre?->"+infoCobertura.getIncluido());
				return infoCobertura;
			}
			logger.info("continua siguiente");
		}	

		return infoCobertura;
	}
	
	/**
	 * metodo para obtener el scodigo de detalle de la cobertura general
	 * @param con
	 * @param codigoContrato
	 * @param codigoViaIngreso
	 * @param codigoNaturalezaPaciente
	 * @param codigoInstitucion
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static Vector obtenerCodigoDetalleCoberturaGeneralEntidadSub(Connection con, long codigoContrato, int codigoViaIngreso, String tipoPaciente, int codigoNaturalezaPaciente, int codigoInstitucion) throws BDException
	{
		Vector vector= new Vector();
		PreparedStatement pst=null;
		ResultSet rs=null;
		
		try {
			Log4JManager.info("############## Inicio obtenerCodigoDetalleCoberturaGeneralEntidadSub");
			String consulta=	"SELECT "+
				"dc.codigo_detalle_cob as codigo "+ 
				"FROM cobertura c "+ 
				"INNER JOIN coberturas_entidad_sub cxc ON(cxc.cobertura=c.codigo_cobertura and cxc.institucion=c.institucion) "+ 
				"INNER JOIN detalle_cobertura dc ON (dc.codigo_cobertura=c.codigo_cobertura and dc.institucion=c.institucion) "+ 
				"WHERE "+ 
				"c.institucion=? and "+ 
				"c.activo='"+ConstantesBD.acronimoSi+"' and "+ 
				"cxc.contrato_entidad_sub=? and "+
				"cxc.activo = '"+ConstantesBD.acronimoSi+"' and "+
				"(dc.via_ingreso is null or dc.via_ingreso=?) and "+ 
				"(dc.tipo_paciente is null or dc.tipo_paciente=?) and "+ 
				"(dc.naturaleza_paciente is null or dc.naturaleza_paciente=?) "; 
			
			//para darle prioridad a los not null entonces hacemos un order by por via_ingreso y naturaleza del paciente
			consulta+=" ORDER BY cxc.nro_prioridad, dc.via_ingreso, dc.tipo_paciente, dc.naturaleza_paciente ";
			
			pst =  con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setInt(1,codigoInstitucion);
			pst.setLong(2, codigoContrato);
			pst.setInt(3,codigoViaIngreso);
			pst.setString(4, tipoPaciente);
			
			if(codigoNaturalezaPaciente!=0 && 
					codigoNaturalezaPaciente!=ConstantesBD.codigoNuncaValido){
				pst.setInt(5,codigoNaturalezaPaciente);
			}else{
				pst.setNull(5,Types.INTEGER);
			}
			
			rs=pst.executeQuery();
			while(rs.next())
			{
				vector.add(rs.getString("codigo"));
			}
		} 
		catch(SQLException sqe){
			Log4JManager.error(sqe);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, sqe);
		}
		catch(Exception e){
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		finally{
			try{
				if(rs != null){
					rs.close();
				}
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException se) {
				Log4JManager.error("###########  Error close ResultSet - PreparedStatement", se);
			}
		}
		Log4JManager.info("############## Fin obtenerCodigoDetalleCoberturaGeneralEntidadSub");
		return vector;		
	}
	
	
	/**
	 * metodo para obtener las excepcione de cobertura x servicio
	 * @param con
	 * @param codigoContrato
	 * @param codigoViaIngreso
	 * @param codigoServicio
	 * @param codigoNaturalezaPaciente
	 * @param codigoInstitucion
	 * @return
	 */
	private static InfoCobertura obtenerExcepcionCoberturaServicioEntidadSub(Connection con, long codigoContrato, int codigoViaIngreso, String tipoPaciente, int codigoServicio, int codigoNaturalezaPaciente, int codigoInstitucion) 
	{
		InfoCobertura infoCobertura= new InfoCobertura();
		logger.info("**********************OBTENER EXCEPCIONES COBERTURA***************************");
		int codigoExcepcionCoberturaGeneral= obtenerCodigoExcepcionCoberturaGeneralEntidadSub(con,codigoContrato, codigoViaIngreso, tipoPaciente, codigoNaturalezaPaciente,  codigoInstitucion);
		
		if(codigoExcepcionCoberturaGeneral<1)
		{
			//no se debe seguir buscando no existe  
			logger.info("->NO EXISTE EXCPCIONES COBERTURA!!!!");
			return infoCobertura;
		}
		
		//si llega aca entonces se debe evaluar por servicio especifico
		infoCobertura= obtenerExcepcionCoberturaServicioEspecificoEntidadSub(con, codigoExcepcionCoberturaGeneral, codigoServicio);
		if(infoCobertura.existe())
		{
			//no se debe seguir buscando existe excepciones para el servicio mandamos el String cargado
			logger.info("Existe excepcion cobertura por servicio especifico!!!! cubre?->"+infoCobertura.getIncluido());
			return infoCobertura;
		}
		
		//de lo contrario debe seguir buscando en los grupos
		infoCobertura= obtenerExcepcionCoberturaServicioAgrupadoEntidadSub(con, codigoExcepcionCoberturaGeneral, codigoServicio);

		return infoCobertura;
	}
	
	
	/**
	 * metodo que obtiene el codigo de la excepcion de la cobertura general
	 * @param con
	 * @param codigoContrato
	 * @param codigoViaIngreso
	 * @param codigoNaturalezaPaciente
	 * @param codigoInstitucion
	 * @return
	 */
	private static int obtenerCodigoExcepcionCoberturaGeneralEntidadSub(Connection con,long codigoContrato, int codigoViaIngreso, String tipoPaciente, int codigoNaturalezaPaciente, int codigoInstitucion) 
	{
		
		PreparedStatement pst=null;
		ResultSet rs=null;
		int resultado=ConstantesBD.codigoNuncaValido;
		try 
		{
			logger.info("############## Inicio obtenerCodigoExcepcionCoberturaGeneralEntidadSub");
			String consulta=" SELECT "+ 
			"consecutivo as codigo "+ 
			"FROM ex_coberturas_entidad_sub "+ 
			"WHERE "+ 
			"contrato_entidad_sub = ? and "+ 
			"institucion=? and "+ 
			"(via_ingreso is null or via_ingreso=?) and "+ 
			"(tipo_paciente is null or tipo_paciente=?) and "+ 
			"(naturaleza is null or naturaleza=?) and " +
			"activo = '"+ConstantesBD.acronimoSi+"' "; 
		
			//para darle prioridad a los not null entonces hacemos un order by por via_ingreso y naturaleza del paciente
			consulta+=" ORDER BY via_ingreso, naturaleza ";
			
			pst =  con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setLong(1, codigoContrato);
			pst.setInt(2,codigoInstitucion);
			pst.setInt(3,codigoViaIngreso);
			pst.setString(4, tipoPaciente);
			
			if(codigoNaturalezaPaciente != 0 && 
					codigoNaturalezaPaciente!=ConstantesBD.codigoNuncaValido){
				pst.setInt(5,codigoNaturalezaPaciente);
			}else{
				pst.setNull(5,Types.INTEGER);
			}
			
			rs=pst.executeQuery();
			if(rs.next()){
				resultado= rs.getInt("codigo");
			}
		} 
		catch(SQLException sqe){
			logger.error("############## SQLException ERROR obtenerCodigoExcepcionCoberturaGeneralEntidadSub",sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR obtenerCodigoExcepcionCoberturaGeneralEntidadSub", e);
		}
		finally{
			try{
				if(rs != null){
					rs.close();
				}
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException se) {
				logger.error("###########  Error close ResultSet - PreparedStatement", se);
			}
		}
		logger.info("############## Fin obtenerCodigoExcepcionCoberturaGeneralEntidadSub");
		return resultado;
	}
	
	/**
	 * obtiene la cobertura de servicio especifico
	 * @param con
	 * @param codigoExcepcionCoberturaGeneral
	 * @param codigoServicio
	 * @return
	 */
	private static InfoCobertura obtenerExcepcionCoberturaServicioEspecificoEntidadSub(Connection con, int codigoExcepcionCoberturaGeneral, int codigoServicio) 
	{
		InfoCobertura infoCobertura= new InfoCobertura();
		PreparedStatement pst=null;
		ResultSet rs=null;
		try 
		{
			logger.info("############## Inicio obtenerExcepcionCoberturaServicioEspecificoEntidadSub");
			String consulta="SELECT "+ 
			"incluye "+ 
			"FROM ex_cober_ser_ent_sub "+ 
			"WHERE "+ 
			"ex_cober_entidad_sub=? AND "+ 
			"servicio=? and "+ 
			"activo = '"+ConstantesBD.acronimoSi+"'";
			pst = con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setDouble(1, Utilidades.convertirADouble(codigoExcepcionCoberturaGeneral+""));
			pst.setInt(2, codigoServicio);
			rs=pst.executeQuery();
			if(rs.next())
			{
				infoCobertura.setExiste(true);
				infoCobertura.setIncluido(rs.getString("incluye"));
			}	
		} 
		catch(SQLException sqe){
			logger.error("############## SQLException ERROR obtenerExcepcionCoberturaServicioEspecificoEntidadSub",sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR obtenerExcepcionCoberturaServicioEspecificoEntidadSub", e);
		}
		finally{
			try{
				if(rs != null){
					rs.close();
				}
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException se) {
				logger.error("###########  Error close ResultSet - PreparedStatement", se);
			}
		}
		logger.info("############## Fin obtenerExcepcionCoberturaServicioEspecificoEntidadSub");
		return infoCobertura;
	}
	
	/**
	 * metodo para obtener la excpcion cobertuta x servicio agrupado
	 * @param con
	 * @param codigoExcepcionCoberturaGeneral
	 * @param codigoServicio
	 * @return
	 */
	private static InfoCobertura obtenerExcepcionCoberturaServicioAgrupadoEntidadSub(Connection con, int codigoExcepcionCoberturaGeneral, int codigoServicio) 
	{
		//la prioridad en este punto va primero por la especialidad, luego x tipo servicio, luego grupo servicio, luego tipo pos
		InfoCobertura infoCobertura= new InfoCobertura();
		PreparedStatement pst=null;
		ResultSet rs=null;
		try 
		{
			logger.info("############## Inicio obtenerExcepcionCoberturaServicioAgrupadoEntidadSub");
			String consulta="SELECT "+ 
				"incluye "+ 
				"from ex_cober_agru_ser_ent_sub "+ 
				"where "+ 
				"ex_cober_entidad_sub = ? and "+ 
				"(especialidad=(select s.especialidad from servicios s where s.codigo=?) or especialidad is null) and "+ 
				"(tipo_servicio=(select s.tipo_servicio from servicios s where s.codigo=?) or tipo_servicio is null) and "+ 
				"(grupo=(select s.grupo_servicio from servicios s where s.codigo=?) or grupo is null) and "+ 
				"(pos=(select case when s.espos = "+ValoresPorDefecto.getValorTrueParaConsultas()+" then '"+ConstantesBD.acronimoSi+"' else '"+ConstantesBD.acronimoNo+"' end from servicios s where s.codigo=?) or pos is null) and "+ 
				"(pos_subsidiado=(select s.espossubsidiado from servicios s where s.codigo=?) or pos_subsidiado is null) and "+ 
				"activo = '"+ConstantesBD.acronimoSi+"' ";
			
			//para que la consulta nos entregue los resultados con prioridad que no sean null solo es requerido hacerle un order by
			consulta+=" ORDER BY especialidad, tipo_servicio, grupo, pos ";
			
			pst = con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setDouble(1, Utilidades.convertirADouble(codigoExcepcionCoberturaGeneral+""));
			pst.setInt(2, codigoServicio);
			pst.setInt(3, codigoServicio);
			pst.setInt(4, codigoServicio);
			pst.setInt(5, codigoServicio);
			pst.setInt(6, codigoServicio);
			rs=pst.executeQuery();
			if(rs.next())
			{
				infoCobertura.setExiste(true);
				infoCobertura.setIncluido(rs.getString("incluye"));
				
			}	
		} 
		catch(SQLException sqe){
			logger.error("############## SQLException ERROR obtenerExcepcionCoberturaServicioAgrupadoEntidadSub",sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR obtenerExcepcionCoberturaServicioAgrupadoEntidadSub", e);
		}
		finally{
			try{
				if(rs != null){
					rs.close();
				}
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException se) {
				logger.error("###########  Error close ResultSet - PreparedStatement", se);
			}
		}
		logger.info("############## Fin obtenerExcepcionCoberturaServicioAgrupadoEntidadSub");
		return infoCobertura;
	}
	
	/**
	 * metodo estatico que evalua la cobertura o no de un ARTICULO para un contrato - via ingreso - naturaleza paciente - institucion, 
	 * devuelve un objeto InfoCobertura que contiene los siguientes atributos:
	 * 
	 * -->existe 	= indica si encontro o no parametrizacion de la cobertura -> BOOLEAN
	 * -->incluido 	= indica si esta o no incluido	-> BOOLEAN  
	 * @param con
	 * @param codigoContrato
	 * @param codigoViaIngreso
	 * @param codigoArticulo
	 * @param codigoNaturalezaPaciente
	 * @param codigoInstitucion
	 * @return
	 */
	public static InfoCobertura validacionCoberturaArticuloEntidadSub(Connection con, long codigoContrato, int codigoViaIngreso, String tipoPaciente, int codigoArticulo, int codigoNaturalezaPaciente, int codigoInstitucion ) throws BDException
	{
		InfoCobertura infoCobertura= new InfoCobertura();
		boolean coberturaEntidadSub = true;
		
		//La filosofia de cobertura es primero buscar las EXCEPCIONES de esta y luego buscar las coberturas
		logger.info("**********************OBTENER COBERTURA X ARTICULO***************************");
		//primero obtenemos lo de la excepcion
		infoCobertura=obtenerExcepcionCoberturaArticuloEntidadSub(con, codigoContrato, codigoViaIngreso, tipoPaciente, codigoArticulo, codigoNaturalezaPaciente, codigoInstitucion);
		
		if(infoCobertura.existe())
		{
			//no se debe seguir buscando existe en la excepcion 
			return infoCobertura;
		}
		//de lo contrario se busca en las coberturas
		logger.info("no existia en las excpciones por lo tanto se busca en las coberturas");
		
		//Se debe llamar al nuevo método 
		infoCobertura= obtenerCoberturaArticulo(con, codigoContrato, codigoViaIngreso, 
				tipoPaciente, codigoArticulo, codigoNaturalezaPaciente, codigoInstitucion,coberturaEntidadSub );
		
		
		//finalmente si no existe entonces no aplica validar semanas de cotizacion y esta cubierto
		/*if(!infoCobertura.existe())
			infoCobertura.setIncluido(true);*/
		
		return infoCobertura;
	
	}
	
	/**
	 * metodo para obtener la excepcion de cobertura x articulo
	 * @param con
	 * @param codigoContrato
	 * @param codigoViaIngreso
	 * @param codigoArticulo
	 * @param codigoNaturalezaPaciente
	 * @param codigoInstitucion
	 * @return
	 */
	private static InfoCobertura obtenerExcepcionCoberturaArticuloEntidadSub(Connection con, long codigoContrato, int codigoViaIngreso, String tipoPaciente,  int codigoArticulo, int codigoNaturalezaPaciente, int codigoInstitucion) 
	{
		InfoCobertura infoCobertura= new InfoCobertura();
		logger.info("**********************OBTENER EXCEPCIONES COBERTURA***************************");
		int codigoExcepcionCoberturaGeneral= obtenerCodigoExcepcionCoberturaGeneralEntidadSub(con,codigoContrato, codigoViaIngreso, tipoPaciente, codigoNaturalezaPaciente,  codigoInstitucion);
		
		if(codigoExcepcionCoberturaGeneral<1)
		{
			//no se debe seguir buscando no existe  
			logger.info("->NO EXISTE EXCPCIONES COBERTURA!!!!");
			return infoCobertura;
		}
		
		//si llega aca entonces se debe evaluar por servicio especifico
		infoCobertura= obtenerExcepcionCoberturaArticuloEspecificoEntidadSub(con, codigoExcepcionCoberturaGeneral, codigoArticulo);
		if(infoCobertura.existe())
		{
			//no se debe seguir buscando existe excepciones para el servicio mandamos el String cargado
			logger.info("Existe excepcion cobertura por articulo especifico!!!! cubre?->"+infoCobertura.getIncluido());
			return infoCobertura;
		}
		
		//de lo contrario debe seguir buscando en los grupos
		infoCobertura= obtenerExcepcionCoberturaArticuloAgrupadoEntidadSub(con, codigoExcepcionCoberturaGeneral, codigoArticulo);

		return infoCobertura;
	}
	
	/**
	 * metodo para obtener la excepcion de cobertura articulo agrupado
	 * @param con
	 * @param codigoExcepcionCoberturaGeneral
	 * @param codigoArticulo
	 * @return
	 */
	private static InfoCobertura obtenerExcepcionCoberturaArticuloAgrupadoEntidadSub(Connection con, int codigoExcepcionCoberturaGeneral, int codigoArticulo) 
	{
		//la prioridad en este punto va primero por la subgrupo, luego x grupo, luego clase, luego naturaleza
		InfoCobertura infoCobertura= new InfoCobertura();
		String consulta="SELECT "+
			"incluye "+ 
			"from ex_cober_agru_art_ent_sub "+ 
			"where "+ 
			//"ex_cobertura_entidad_sub = ? and "+ 
			"ex_cobertura_entidad = ? and "+
			"(subgrupo_inventario=(select s.subgrupo from subgrupo_inventario s where s.codigo=(select a.subgrupo from articulo a where a.codigo=?)) or subgrupo_inventario is null) and "+ 
			"(grupo_inventario=(select s.grupo from subgrupo_inventario s where s.codigo=(select a.subgrupo from articulo a where a.codigo=?)) or grupo_inventario is null) and "+ 
			"(clase_inventario=(select s.clase from subgrupo_inventario s where s.codigo=(select a.subgrupo from articulo a where a.codigo=?)) or clase_inventario is null) and "+ 
			"(naturaleza=(select a.naturaleza from articulo a where a.codigo=?) or naturaleza is null) and "+ 
			"activo = '"+ConstantesBD.acronimoSi+"' ";
							
		//para que la consulta nos entregue los resultados con prioridad que no sean null solo es requerido hacerle un order by
		consulta+=" ORDER BY subgrupo_inventario, grupo_inventario, clase_inventario, naturaleza ";
		
		PreparedStatementDecorator ps;
		ResultSetDecorator rs;
		try 
		{
			logger.info("obtenerExcepcionCoberturaArticuloAgrupado->"+consulta+" codigoGen->"+codigoExcepcionCoberturaGeneral+" codArt->"+codigoArticulo);
			ps =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDouble(1, Utilidades.convertirADouble(codigoExcepcionCoberturaGeneral+""));
			ps.setInt(2, codigoArticulo);
			ps.setInt(3, codigoArticulo);
			ps.setInt(4, codigoArticulo);
			ps.setInt(5, codigoArticulo);
			rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				infoCobertura.setExiste(true);
				infoCobertura.setIncluido(rs.getString("incluye"));
				//infoCobertura.setRequiereAutorizacion(rs.getString("requiereaut"));
				//infoCobertura.setSemanasMinimasCotizacion(rs.getInt("semanas"));
				//infoCobertura.setCantidad(rs.getInt("cantidad"));
			}	
		} 
		catch (SQLException e) 
		{
			logger.error("ERROR EN obtenerExcepcionCoberturaArticuloAgrupado");
			e.printStackTrace();
		}
		logger.info(infoCobertura.loggerCobertura());
		return infoCobertura;
	}
	
	
	/**
	 * metodo para obtener la excepcion de cobertura articulo especifico
	 * @param con
	 * @param codigoExcepcionCoberturaGeneral
	 * @param codigoArticulo
	 * @return
	 */
	private static InfoCobertura obtenerExcepcionCoberturaArticuloEspecificoEntidadSub(Connection con, int codigoExcepcionCoberturaGeneral, int codigoArticulo) 
	{
		InfoCobertura infoCobertura= new InfoCobertura();
		String consulta="SELECT "+ 
			"incluye "+ 
			"FROM ex_cober_art_ent_sub "+ 
			"WHERE "+ 
			"ex_cober_entidad_sub=? AND "+ 
			"articulo=? and "+ 
			"activo = '"+ConstantesBD.acronimoSi+"'";
		
		PreparedStatementDecorator ps;
		ResultSetDecorator rs;
		try 
		{
			logger.info("obtenerExcepcionCoberturaArticuloEspecificoEntidadSub->"+consulta+" codGeneral->"+codigoExcepcionCoberturaGeneral+" codArt->"+codigoArticulo);
			ps =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDouble(1, Utilidades.convertirADouble(codigoExcepcionCoberturaGeneral+""));
			ps.setInt(2, codigoArticulo);
			rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				infoCobertura.setExiste(true);
				infoCobertura.setIncluido(rs.getString("incluye"));
			}	
		} 
		catch (SQLException e) 
		{
			logger.error("ERROR EN obtenerExcepcionCoberturaArticuloEspecificoEntidadSub 1: "+e);
			e.printStackTrace();
		}
		logger.info(infoCobertura.loggerCobertura());
		return infoCobertura;
	
	}
	
	//********************************************************************************************************************
	/**
	 * metodo estatico que evalua la cobertura o no de un servicio para un contrato - via ingreso - naturaleza paciente - institucion, 
	 * devuelve un objeto InfoCobertura que contiene los siguientes atributos:
	 * 
	 * -->existe 	= indica si encontro o no parametrizacion de la cobertura -> BOOLEAN
	 * -->incluido 	= indica si esta o no incluido	-> BOOLEAN
	 * -->requiereAutorizacion = indica si requiere o no aut -> BOOLEAN
	 * -->semanasMinimasCotizacion = -> INT  
	 * @param con
	 * @param codigoContrato
	 * @param codigoViaIngreso
	 * @param codigoServicio
	 * @param codigoNaturalezaPaciente
	 * @param codigoInstitucion
	 * @return
	 */
	public static InfoCobertura validacionCoberturaServicio(Connection con, long codigoContrato, int codigoViaIngreso,String tipoPaciente, int codigoServicio, int codigoNaturalezaPaciente, int codigoInstitucion ) throws BDException
	{
		InfoCobertura infoCobertura= new InfoCobertura();
		
		try {
			//La filosofia de cobertura es primero buscar las EXCEPCIONES de esta y luego buscar las coberturas
			//primero obtenemos lo de la excepcion
			infoCobertura=obtenerExcepcionCoberturaServicio(con, codigoContrato, codigoViaIngreso, tipoPaciente, codigoServicio, codigoNaturalezaPaciente, codigoInstitucion);
			
			if(infoCobertura.existe())
			{
				//no se debe seguir buscando existe en la excepcion 
				return infoCobertura;
			}
			//de lo contrario se busca en las coberturas
			infoCobertura= obtenerCoberturaServicio(con, codigoContrato, codigoViaIngreso, tipoPaciente, codigoServicio, codigoNaturalezaPaciente, codigoInstitucion);
			
			return infoCobertura;
		
		}
		catch (BDException bde) {
			throw bde;
		}
		catch (Exception e) {
			Log4JManager.error(e.getMessage(),e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
	
	}
	

	/**
	 * metodo para obtener las excepcione de cobertura x servicio
	 * @param con
	 * @param codigoContrato
	 * @param codigoViaIngreso
	 * @param codigoServicio
	 * @param codigoNaturalezaPaciente
	 * @param codigoInstitucion
	 * @return
	 */
	private static InfoCobertura obtenerExcepcionCoberturaServicio(Connection con, long codigoContrato, int codigoViaIngreso, String tipoPaciente, int codigoServicio, int codigoNaturalezaPaciente, int codigoInstitucion) throws BDException
	{
		InfoCobertura infoCobertura= new InfoCobertura();
		Log4JManager.info("**********************OBTENER EXCEPCIONES COBERTURA***************************");
		
		try{
			int codigoExcepcionCoberturaGeneral= obtenerCodigoExcepcionCoberturaGeneral(con,codigoContrato, codigoViaIngreso, tipoPaciente, codigoNaturalezaPaciente,  codigoInstitucion);
			
			if(codigoExcepcionCoberturaGeneral<1)
			{
				//no se debe seguir buscando no existe  
						Log4JManager.info("->NO EXISTE EXCPCIONES COBERTURA!!!!");
				return infoCobertura;
			}
			
			//si llega aca entonces se debe evaluar por servicio especifico
			infoCobertura= obtenerExcepcionCoberturaServicioEspecifico(con, codigoExcepcionCoberturaGeneral, codigoServicio);
			if(infoCobertura.existe())
			{
				//no se debe seguir buscando existe excepciones para el servicio mandamos el String cargado
						Log4JManager.info("Existe excepcion cobertura por servicio especifico!!!! cubre?->"+infoCobertura.getIncluido());
				return infoCobertura;
			}
			
			//de lo contrario debe seguir buscando en los grupos
			infoCobertura= obtenerExcepcionCoberturaServicioAgrupado(con, codigoExcepcionCoberturaGeneral, codigoServicio);
	
			return infoCobertura;
		}
		catch (BDException bde) {
			throw bde;
		}
		catch (Exception e) {
			Log4JManager.error(e.getMessage(),e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}	
			
	}

	/**
	 * metodo que obtiene el codigo de la excepcion de la cobertura general
	 * @param con
	 * @param codigoContrato
	 * @param codigoViaIngreso
	 * @param codigoNaturalezaPaciente
	 * @param codigoInstitucion
	 * @return
	 */
	private static int obtenerCodigoExcepcionCoberturaGeneral(Connection con, long codigoContrato, int codigoViaIngreso, String tipoPaciente, int codigoNaturalezaPaciente, int codigoInstitucion) throws BDException
	{
		
		PreparedStatement pst=null;
		ResultSet rs=null;
		int resultado=ConstantesBD.codigoNuncaValido;
		
		try {
			Log4JManager.info("############## Inicio obtenerCodigoExcepcionCoberturaGeneral");
			String consulta=" SELECT codigo " +
			"FROM exep_para_cob_x_convcont " +
			"WHERE codigo_contrato =? " +
			"and institucion=? " +
			"and (via_ingreso is null or via_ingreso=?) " +
			"and (tipo_paciente is null or tipo_paciente=?) " +
			"and (naturaleza_paciente is null or naturaleza_paciente=?)";

			//para darle prioridad a los not null entonces hacemos un order by por via_ingreso y naturaleza del paciente
			consulta+=" ORDER BY via_ingreso, naturaleza_paciente ";
			pst =  con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setLong(1, codigoContrato);
			pst.setInt(2,codigoInstitucion);
			pst.setInt(3,codigoViaIngreso);
			pst.setString(4, tipoPaciente);
						
			if(codigoNaturalezaPaciente != 0 && 
					codigoNaturalezaPaciente!=ConstantesBD.codigoNuncaValido){
				pst.setInt(5,codigoNaturalezaPaciente);
			}else{
				pst.setNull(5,Types.INTEGER);
			}
			
			rs=pst.executeQuery();
			if(rs.next()){
				resultado=rs.getInt("codigo");
			}
		} 
		catch(SQLException sqe){
			Log4JManager.error(sqe);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, sqe);
		}
		catch(Exception e){
			Log4JManager.error( e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		finally{
			try{
				if(rs != null){
					rs.close();
				}
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException se) {
				Log4JManager.error("Error close ResultSet - PreparedStatement", se);
			}
		}
		Log4JManager.info("############## Fin obtenerCodigoExcepcionCoberturaGeneral");
		return resultado;
	}
	
	/**
	 * obtiene la cobertura de servicio especifico
	 * @param con
	 * @param codigoExcepcionCoberturaGeneral
	 * @param codigoServicio
	 * @return
	 */
	private static InfoCobertura obtenerExcepcionCoberturaServicioEspecifico(Connection con, int codigoExcepcionCoberturaGeneral, int codigoServicio) throws BDException
	{
		InfoCobertura infoCobertura= new InfoCobertura();
		
		PreparedStatement pst=null;
		ResultSet rs=null;
		
		try {
			Log4JManager.info("############## Inicio obtenerExcepcionCoberturaServicioEspecifico");
			String consulta="SELECT " +
			"incluido as incluye, " +
			"requiere_autorizacion as requiereaut, " +
			"coalesce(semanas_min_cotizacion,"+ConstantesBD.codigoNuncaValido+") as semanas, " +
			"coalesce(cantidad_max_cub_x_ingreso, "+ConstantesBD.codigoNuncaValido+") as cantidad " +
			"FROM " +
				"serv_exe_cob_convxcont " +
			"WHERE codigo_excepcion=? AND codigo_servicio=?";
			pst =  con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setDouble(1, Utilidades.convertirADouble(codigoExcepcionCoberturaGeneral+""));
			pst.setInt(2, codigoServicio);
			rs=pst.executeQuery();
			if(rs.next())
			{
				infoCobertura.setExiste(true);
				infoCobertura.setIncluido(rs.getString("incluye"));
				infoCobertura.setRequiereAutorizacion(rs.getString("requiereaut"));
				infoCobertura.setSemanasMinimasCotizacion(rs.getInt("semanas"));
				infoCobertura.setCantidad(rs.getInt("cantidad"));
			}	
		} 
		catch(SQLException sqe){
			Log4JManager.error(sqe);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, sqe);
		}
		catch(Exception e){
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		finally{
			try{
				if(rs != null){
					rs.close();
				}
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException se) {
				Log4JManager.error("###########  Error close ResultSet - PreparedStatement", se);
			}
		}
		Log4JManager.info("############## Fin obtenerExcepcionCoberturaServicioEspecifico");
		return infoCobertura;
	}
	
	/**
	 * metodo para obtener la excpcion cobertuta x servicio agrupado
	 * @param con
	 * @param codigoExcepcionCoberturaGeneral
	 * @param codigoServicio
	 * @return
	 */
	private static InfoCobertura obtenerExcepcionCoberturaServicioAgrupado(Connection con, int codigoExcepcionCoberturaGeneral, int codigoServicio) throws BDException 
	{
		//la prioridad en este punto va primero por la especialidad, luego x tipo servicio, luego grupo servicio, luego tipo pos
		InfoCobertura infoCobertura= new InfoCobertura();
		PreparedStatement pst=null;
		ResultSet rs=null;
		
		try {
			Log4JManager.info("############## Inicio obtenerExcepcionCoberturaServicioAgrupado");
			String consulta="SELECT " +
						"incluido as incluye, " +
						"requiere_autorizacion as requiereaut, " +
						"coalesce(semanas_min_cotizacion,"+ConstantesBD.codigoNuncaValido+") as semanas, " +
						"coalesce(cantidad_max_cub_x_ingreso, "+ConstantesBD.codigoNuncaValido+") as cantidad " +
					"from " +
						"agru_ser_exep_cob_convxcont " +
					"where " +
						"codigo_excepcion=? " +
						"and (especialidad=(select s.especialidad from servicios s where s.codigo=?) or especialidad is null) " +
						"and (tipo_servicio=(select s.tipo_servicio from servicios s where s.codigo=?) or tipo_servicio is null) " +
						"and (grupo_servicio=(select s.grupo_servicio from servicios s where s.codigo=?) or grupo_servicio is null) "+
						"and (pos=(select case when s.espos="+ValoresPorDefecto.getValorTrueParaConsultas()+" then '"+ConstantesBD.acronimoSi+"' else '"+ConstantesBD.acronimoNo+"' end from servicios s where s.codigo=?) or pos is null) " +
						"and (possubsidiado=(select s.espossubsidiado from servicios s where s.codigo=?) or possubsidiado is null) ";
			
			//para que la consulta nos entregue los resultados con prioridad que no sean null solo es requerido hacerle un order by
			consulta+=" ORDER BY especialidad, tipo_servicio, grupo_servicio, pos ";

			pst = con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setDouble(1, Utilidades.convertirADouble(codigoExcepcionCoberturaGeneral+""));
			pst.setInt(2, codigoServicio);
			pst.setInt(3, codigoServicio);
			pst.setInt(4, codigoServicio);
			pst.setInt(5, codigoServicio);
			pst.setInt(6, codigoServicio);
			rs=pst.executeQuery();
			if(rs.next())
			{
				infoCobertura.setExiste(true);
				infoCobertura.setIncluido(rs.getString("incluye"));
				infoCobertura.setRequiereAutorizacion(rs.getString("requiereaut"));
				infoCobertura.setSemanasMinimasCotizacion(rs.getInt("semanas"));
				infoCobertura.setCantidad(rs.getInt("cantidad"));
			}	
		} 
		catch(SQLException sqe){
			Log4JManager.error(sqe);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, sqe);
		}
		catch(Exception e){
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		finally{
			try{
				if(rs != null){
					rs.close();
				}
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException se) {
				Log4JManager.error("###########  Error close ResultSet - PreparedStatement", se);
			}
		}
		Log4JManager.info("############## Fin obtenerExcepcionCoberturaServicioAgrupado");
		return infoCobertura;
	}
	
	/**
	 * metodo que obtiene la cobertura x servicio
	 * @param con
	 * @param codigoContrato
	 * @param codigoViaIngreso
	 * @param codigoServicio
	 * @param codigoNaturalezaPaciente
	 * @param codigoInstitucion
	 * @return
	 */
	private static InfoCobertura obtenerCoberturaServicio(Connection con, long codigoContrato, int codigoViaIngreso, String tipoPaciente, int codigoServicio, int codigoNaturalezaPaciente, int codigoInstitucion) throws BDException 
	{
		Log4JManager.info("**********************OBTENER COBERTURA (NO EXISTE EXCEPCION)***************************");
		InfoCobertura infoCobertura= new InfoCobertura();

		try {
			Vector codigosDetalleCoberturaGeneral= obtenerCodigoDetalleCoberturaGeneral(con,codigoContrato, codigoViaIngreso, tipoPaciente, codigoNaturalezaPaciente,  codigoInstitucion);
			
			if(codigosDetalleCoberturaGeneral.size()<1)
			{
				//no se debe seguir buscando no existe  
				Log4JManager.info("->NO EXISTE COBERTURA!!!!");
				return infoCobertura;
			}
				Log4JManager.info("\n\nNUMERO DETALLES COBERTURA-->"+codigosDetalleCoberturaGeneral.size());
			for(int w=0; w<codigosDetalleCoberturaGeneral.size(); w++)
			{	
				Log4JManager.info("\n\n*****************************CODIGO DETALLE COBERTURA "+codigosDetalleCoberturaGeneral.get(w)+"**************w-->"+w);
				//si llega aca entonces se debe evaluar por servicio especifico
				infoCobertura= obtenerCoberturaServicioEspecifico(con,Integer.parseInt(codigosDetalleCoberturaGeneral.get(w).toString()), codigoServicio);
				if(infoCobertura.existe())
				{
					//no se debe seguir buscando existe para el servicio mandamos el String cargado
						Log4JManager.info("Existe cobertura por servicio especifico!!!! cubre?->"+infoCobertura.getIncluido());
					return infoCobertura;
				}
				
				//de lo contrario debe seguir buscando en los grupos
				infoCobertura= obtenerCoberturaServicioAgrupado(con, Integer.parseInt(codigosDetalleCoberturaGeneral.get(w).toString()), codigoServicio);
				if(infoCobertura.existe())
				{
					//no se debe seguir buscando existe para el servicio mandamos el String cargado
					Log4JManager.info("Existe cobertura por servicio agupado!!!! cubre?->"+infoCobertura.getIncluido());
					return infoCobertura;
				}
				Log4JManager.info("continua siguiente");
			}	
	
			return infoCobertura;
			
		}
		catch (BDException bde) {
			throw bde;
		}
		catch (Exception e) {
			Log4JManager.error(e.getMessage(),e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
	}

	/**
	 * metodo para obtener el scodigo de detalle de la cobertura general
	 * @param con
	 * @param codigoContrato
	 * @param codigoViaIngreso
	 * @param codigoNaturalezaPaciente
	 * @param codigoInstitucion
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static Vector obtenerCodigoDetalleCoberturaGeneral(Connection con, long codigoContrato, int codigoViaIngreso, String tipoPaciente, int codigoNaturalezaPaciente, int codigoInstitucion) throws BDException
	{
		Vector vector= new Vector();
		PreparedStatement pst=null;
		ResultSet rs=null;
		
		try {
			Log4JManager.info("############## Inicio obtenerCodigoDetalleCoberturaGeneral");
			String consulta=	"SELECT " +
						"dc.codigo_detalle_cob as codigo " +
					"FROM " +
					"cobertura c " +
						"INNER JOIN coberturas_x_contrato cxc ON(cxc.codigo_cobertura=c.codigo_cobertura and cxc.institucion=c.institucion) " +
						"INNER JOIN detalle_cobertura dc ON (dc.codigo_cobertura=c.codigo_cobertura and dc.institucion=c.institucion) " +
					"WHERE " +
						"c.institucion=? and c.activo='"+ConstantesBD.acronimoSi+"' " +
						"and cxc.codigo_contrato=? " +
						"and (dc.via_ingreso is null or dc.via_ingreso=?) " +
						" and (dc.tipo_paciente is null or dc.tipo_paciente=?) " +
						"and (dc.naturaleza_paciente is null or dc.naturaleza_paciente=?) ";
			
			//para darle prioridad a los not null entonces hacemos un order by por via_ingreso y naturaleza del paciente
			consulta+=" ORDER BY cxc.prioridad, dc.via_ingreso, dc.tipo_paciente, dc.naturaleza_paciente ";

			pst =  con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setInt(1,codigoInstitucion);
			pst.setLong(2, codigoContrato);
			pst.setInt(3,codigoViaIngreso);
			pst.setString(4, tipoPaciente);
			
			if(codigoNaturalezaPaciente!=0 && 
					codigoNaturalezaPaciente!=ConstantesBD.codigoNuncaValido){
				pst.setInt(5,codigoNaturalezaPaciente);
			}else{
				pst.setNull(5,Types.INTEGER);
			}
			
			rs=pst.executeQuery();                    
			while(rs.next())
			{
				vector.add(rs.getString("codigo"));
			}
		} 
		catch(SQLException sqe){
			Log4JManager.error(sqe);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, sqe);
		}
		catch(Exception e){
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		finally{
			try{
				if(rs != null){
					rs.close();
				}
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException se) {
				Log4JManager.error("###########  Error close ResultSet - PreparedStatement", se);
			}
		}
		Log4JManager.info("############## Fin obtenerCodigoDetalleCoberturaGeneral");
		return vector;
		
	}

	/**
	 * metodo para obtener la cobertura de servicio especifico
	 * @param con
	 * @param codigoDetalleCoberturaGeneral
	 * @param codigoServicio
	 * @return
	 */
	private static InfoCobertura obtenerCoberturaServicioEspecifico(Connection con, int codigoDetalleCoberturaGeneral, int codigoServicio) throws BDException
	{
		InfoCobertura infoCobertura= new InfoCobertura();
		PreparedStatement pst=null;
		ResultSet rs=null;
		
		try {
			Log4JManager.info("############## Inicio obtenerCoberturaServicioEspecifico");
			String consulta="SELECT " +
				"requiere_autorizacion as requiereaut, " +
				"coalesce(semanas_min_cotizacion,"+ConstantesBD.codigoNuncaValido+") as semanas, " +
				"coalesce(cantidad_max_cub_x_ingreso, "+ConstantesBD.codigoNuncaValido+") as cantidad " +
			"from " +
				"cobertura_servicios " +
			"where " +
				"codigo_detalle_cob=? " +
				"and codigo_servicio=?";

			pst = con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setDouble(1, Utilidades.convertirADouble(codigoDetalleCoberturaGeneral+""));
			pst.setInt(2, codigoServicio);
			rs=pst.executeQuery();
			if(rs.next())
			{	
				infoCobertura.setExiste(true);
				infoCobertura.setIncluido(true);
				infoCobertura.setRequiereAutorizacion(rs.getString("requiereaut"));
				infoCobertura.setSemanasMinimasCotizacion(rs.getInt("semanas"));
				infoCobertura.setCantidad(rs.getInt("cantidad"));
			}	
		}
		catch(SQLException sqe){
			Log4JManager.error(sqe);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, sqe);
		}
		catch(Exception e){
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		finally{
			try{
				if(rs != null){
					rs.close();
				}
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException se) {
				Log4JManager.error("###########  Error close ResultSet - PreparedStatement", se);
			}
		}
		Log4JManager.info("############## Fin obtenerCoberturaServicioEspecifico");
		return infoCobertura;
	
	}

	/**
	 * metodo que obtiene la cobertura x servicio agrupado
	 * @param con
	 * @param codigoDetalleCoberturaGeneral
	 * @param codigoServicio
	 * @return
	 */
	private static InfoCobertura obtenerCoberturaServicioAgrupado(Connection con, int codigoDetalleCoberturaGeneral, int codigoServicio) throws BDException
	{
		//la prioridad en este punto va primero por la especialidad, luego x tipo servicio, luego grupo servicio, luego tipo pos
		InfoCobertura infoCobertura= new InfoCobertura();
		PreparedStatement pst=null;
		ResultSet rs=null;
		
		try {
			Log4JManager.info("############## Inicio obtenerCoberturaServicioAgrupado");
			String consulta="SELECT " +
						"requiere_autorizacion as requiereaut, " +
						"coalesce(semanas_min_cotizacion,"+ConstantesBD.codigoNuncaValido+") as semanas, " +
						"coalesce(cantidad_max_cub_x_ingreso, "+ConstantesBD.codigoNuncaValido+") as cantidad " +
					"from " +
						"facturacion.cob_agrup_servicios " +
					"where " +
						"codigo_detalle_cob=? " +
						"and ( (especialidad=(select s.especialidad from servicios s where s.codigo=?)) or (especialidad is null)) " +
						"and ( (tipo_servicio=(select s.tipo_servicio from servicios s where s.codigo=?)) or (tipo_servicio is null)) " +
						"and ( (grupo_servicio=(select s.grupo_servicio from servicios s where s.codigo=?)) or (grupo_servicio is null)) "+
						"and ( (pos=(select case when s.espos='"+ValoresPorDefecto.getValorTrueCortoParaConsultas()+"' then '"+ConstantesBD.acronimoSi+"' else '"+ConstantesBD.acronimoNo+"' end from servicios s where s.codigo=?)) or (pos is null)) " +
						"and ( (possubsidiado=(select s.espossubsidiado from servicios s where s.codigo=?)) or (possubsidiado is null)) ";
			
			//para que la consulta nos entregue los resultados con prioridad que no sean null solo es requerido hacerle un order by
			consulta+=" ORDER BY especialidad, tipo_servicio, grupo_servicio, pos ";

			pst = con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setDouble(1, Utilidades.convertirADouble(codigoDetalleCoberturaGeneral+""));
			pst.setInt(2, codigoServicio);
			pst.setInt(3, codigoServicio);
			pst.setInt(4, codigoServicio);
			pst.setInt(5, codigoServicio);
			pst.setInt(6, codigoServicio);
			rs=pst.executeQuery();
			if(rs.next())
			{	
				infoCobertura.setExiste(true);
				infoCobertura.setIncluido(true);
				infoCobertura.setRequiereAutorizacion(rs.getString("requiereaut"));
				infoCobertura.setSemanasMinimasCotizacion(rs.getInt("semanas"));
				infoCobertura.setCantidad(rs.getInt("cantidad"));
			}	
		} 
		catch(SQLException sqe){
			Log4JManager.error(sqe);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, sqe);
		}
		catch(Exception e){
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		finally{
			try{
				if(rs != null){
					rs.close();
				}
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException se) {
				Log4JManager.error("###########  Error close ResultSet - PreparedStatement", se);
			}
		}
		Log4JManager.info("############## Fin obtenerCoberturaServicioAgrupado");
		return infoCobertura;
	
	}

	
	/**
	 * metodo estatico que evalua la cobertura o no de un ARTICULO para un contrato - via ingreso - naturaleza paciente - institucion, 
	 * devuelve un objeto InfoCobertura que contiene los siguientes atributos:
	 * 
	 * -->existe 	= indica si encontro o no parametrizacion de la cobertura -> BOOLEAN
	 * -->incluido 	= indica si esta o no incluido	-> BOOLEAN
	 * -->requiereAutorizacion = indica si requiere o no aut -> BOOLEAN
	 * -->semanasMinimasCotizacion = -> INT  
	 * @param con
	 * @param codigoContrato
	 * @param codigoViaIngreso
	 * @param codigoArticulo
	 * @param codigoNaturalezaPaciente
	 * @param codigoInstitucion
	 * @return
	 */
	public static InfoCobertura validacionCoberturaArticulo(Connection con, long codigoContrato, int codigoViaIngreso, String tipoPaciente, int codigoArticulo, int codigoNaturalezaPaciente, int codigoInstitucion ) throws BDException
	{
		InfoCobertura infoCobertura= new InfoCobertura();
		boolean coberturaEntidadSub = false;
		
		try {
			//La filosofia de cobertura es primero buscar las EXCEPCIONES de esta y luego buscar las coberturas
			//primero obtenemos lo de la excepcion
			infoCobertura=obtenerExcepcionCoberturaArticulo(con, codigoContrato, codigoViaIngreso, tipoPaciente, codigoArticulo, codigoNaturalezaPaciente, codigoInstitucion);
			
			if(infoCobertura.existe())
			{
				//no se debe seguir buscando existe en la excepcion 
				return infoCobertura;
			}
			//de lo contrario se busca en las coberturas
			infoCobertura= obtenerCoberturaArticulo(con, codigoContrato, codigoViaIngreso, tipoPaciente, 
					codigoArticulo, codigoNaturalezaPaciente, codigoInstitucion,coberturaEntidadSub);
			
			//finalmente si no existe entonces no aplica validar semanas de cotizacion y esta cubierto
			/*if(!infoCobertura.existe())
				infoCobertura.setIncluido(true);*/
		
		}
		catch (BDException bde) {
			throw bde;
		}
		catch (Exception e) {
			Log4JManager.error(e.getMessage(),e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
	
		return infoCobertura;
	
	}

	/**
	 * metodo para obtener la excepcion de cobertura x articulo
	 * @param con
	 * @param codigoContrato
	 * @param codigoViaIngreso
	 * @param codigoArticulo
	 * @param codigoNaturalezaPaciente
	 * @param codigoInstitucion
	 * @return
	 */
	private static InfoCobertura obtenerExcepcionCoberturaArticulo(Connection con, long codigoContrato, int codigoViaIngreso, String tipoPaciente,  int codigoArticulo, int codigoNaturalezaPaciente, int codigoInstitucion) throws BDException
	{
		InfoCobertura infoCobertura= new InfoCobertura();
		Log4JManager.info("**********************OBTENER EXCEPCIONES COBERTURA***************************");
		
		try {
			int codigoExcepcionCoberturaGeneral= obtenerCodigoExcepcionCoberturaGeneral(con,codigoContrato, codigoViaIngreso, tipoPaciente, codigoNaturalezaPaciente,  codigoInstitucion);
			
			if(codigoExcepcionCoberturaGeneral<1)
			{
				//no se debe seguir buscando no existe  
				logger.info("->NO EXISTE EXCPCIONES COBERTURA!!!!");
				return infoCobertura;
			}
			
			//si llega aca entonces se debe evaluar por servicio especifico
			infoCobertura= obtenerExcepcionCoberturaArticuloEspecifico(con, codigoExcepcionCoberturaGeneral, codigoArticulo);
			if(infoCobertura.existe())
			{
				//no se debe seguir buscando existe excepciones para el servicio mandamos el String cargado
					Log4JManager.info("Existe excepcion cobertura por articulo especifico!!!! cubre?->"+infoCobertura.getIncluido());
				return infoCobertura;
			}
			
			//de lo contrario debe seguir buscando en los grupos
			infoCobertura= obtenerExcepcionCoberturaArticuloAgrupado(con, codigoExcepcionCoberturaGeneral, codigoArticulo);

		} 
		catch (BDException bde) {
			throw bde;
		}
		catch (Exception e) {
			Log4JManager.error(e.getMessage(),e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}	
			
		return infoCobertura;
	}

	/**
	 * metodo para obtener la excepcion de cobertura articulo especifico
	 * @param con
	 * @param codigoExcepcionCoberturaGeneral
	 * @param codigoArticulo
	 * @return
	 */
	private static InfoCobertura obtenerExcepcionCoberturaArticuloEspecifico(Connection con, int codigoExcepcionCoberturaGeneral, int codigoArticulo) throws BDException
	{
		InfoCobertura infoCobertura= new InfoCobertura();
		PreparedStatement pst=null;
		ResultSet rs=null;
		
		try {
			Log4JManager.info("############## Inicio obtenerExcepcionCoberturaArticuloEspecifico");
			String consulta="SELECT " +
				"incluido as incluye, " +
				"requiere_autorizacion as requiereaut, " +
				"coalesce(semanas_min_cotizacion,"+ConstantesBD.codigoNuncaValido+") as semanas, " +
				"coalesce(cantidad_max_cub_x_ingreso, "+ConstantesBD.codigoNuncaValido+") as cantidad " +
			"FROM " +
				"art_exp_cob_convxcont " +
			"WHERE " +
				"codigo_excepcion=? " +
				"AND codigo_articulo=?";

			pst = con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setDouble(1, Utilidades.convertirADouble(codigoExcepcionCoberturaGeneral+""));
			pst.setInt(2, codigoArticulo);
			rs=pst.executeQuery();
			if(rs.next())
			{
				infoCobertura.setExiste(true);
				infoCobertura.setIncluido(rs.getString("incluye"));
				infoCobertura.setRequiereAutorizacion(rs.getString("requiereaut"));
				infoCobertura.setSemanasMinimasCotizacion(rs.getInt("semanas"));
				infoCobertura.setCantidad(rs.getInt("cantidad"));
			}	
		} 
		catch(SQLException sqe){
			Log4JManager.error(sqe);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, sqe);
		}
		catch(Exception e){
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		finally{
			try{
				if(rs != null){
					rs.close();
				}
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException se) {
				Log4JManager.error("###########  Error close ResultSet - PreparedStatement", se);
			}
		}
		Log4JManager.info("############## Fin obtenerExcepcionCoberturaArticuloEspecifico");
		return infoCobertura;
	
	}

	/**
	 * metodo para obtener la excepcion de cobertura articulo agrupado
	 * @param con
	 * @param codigoExcepcionCoberturaGeneral
	 * @param codigoArticulo
	 * @return
	 */
	private static InfoCobertura obtenerExcepcionCoberturaArticuloAgrupado(Connection con, int codigoExcepcionCoberturaGeneral, int codigoArticulo) throws BDException
	{
		//la prioridad en este punto va primero por la subgrupo, luego x grupo, luego clase, luego naturaleza
		InfoCobertura infoCobertura= new InfoCobertura();
		PreparedStatement pst=null;
		ResultSet rs=null;
		
		Log4JManager.info("############## Inicio obtenerExcepcionCoberturaArticuloAgrupado");
			String consulta="SELECT " +
						"incluido as incluye, " +
						"requiere_autorizacion as requiereaut, " +
						"coalesce(semanas_min_cotizacion,"+ConstantesBD.codigoNuncaValido+") as semanas, " +
						"coalesce(cantidad_max_cub_x_ingreso, "+ConstantesBD.codigoNuncaValido+") as cantidad " +
					"from " +
						"agrup_art_exep_cob_convxcont " +
					"where " +
						"codigo_excepcion=? " +
						"and (subgrupo=(select s.subgrupo from subgrupo_inventario s where s.codigo=(select a.subgrupo from articulo a where a.codigo=?)) or subgrupo is null) " +
						"and (grupo=(select s.grupo from subgrupo_inventario s where s.codigo=(select a.subgrupo from articulo a where a.codigo=?)) or grupo is null) " +
						"and (clase=(select s.clase from subgrupo_inventario s where s.codigo=(select a.subgrupo from articulo a where a.codigo=?)) or clase is null) " +
						"and (naturaleza=(select a.naturaleza from articulo a where a.codigo=?) or naturaleza is null) ";
						
			//para que la consulta nos entregue los resultados con prioridad que no sean null solo es requerido hacerle un order by
			consulta+=" ORDER BY subgrupo, grupo, clase, naturaleza ";

		try {
			pst = con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setDouble(1, Utilidades.convertirADouble(codigoExcepcionCoberturaGeneral+""));
			pst.setInt(2, codigoArticulo);
			pst.setInt(3, codigoArticulo);
			pst.setInt(4, codigoArticulo);
			pst.setInt(5, codigoArticulo);
			rs=pst.executeQuery();
			if(rs.next())
			{
				infoCobertura.setExiste(true);
				infoCobertura.setIncluido(rs.getString("incluye"));
				infoCobertura.setRequiereAutorizacion(rs.getString("requiereaut"));
				infoCobertura.setSemanasMinimasCotizacion(rs.getInt("semanas"));
				infoCobertura.setCantidad(rs.getInt("cantidad"));
			}	
		} 
		catch(SQLException sqe){
			Log4JManager.error(sqe);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, sqe);
		}
		catch(Exception e){
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		finally{
			try{
				if(rs != null){
					rs.close();
				}
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException se) {
				Log4JManager.error("###########  Error close ResultSet - PreparedStatement", se);
			}
		}
		Log4JManager.info("############## Fin obtenerExcepcionCoberturaArticuloAgrupado");
		return infoCobertura;
	}
	
	/**
	 * metodo pa obtener la cobertura articulo
	 * @param con
	 * @param codigoContrato
	 * @param codigoViaIngreso
	 * @param codigoArticulo
	 * @param codigoNaturalezaPaciente
	 * @param codigoInstitucion
	 * @return
	 */
	private static InfoCobertura obtenerCoberturaArticulo(Connection con, long codigoContrato, int codigoViaIngreso, String tipoPaciente,
			int codigoArticulo, int codigoNaturalezaPaciente, int codigoInstitucion, boolean coberturaEntidadSub) throws BDException
	{
		Log4JManager.info("**********************OBTENER COBERTURA ARTICULO (NO EXISTE EXCEPCION)***************************");
		InfoCobertura infoCobertura= new InfoCobertura();
		Vector codigosDetalleCoberturaGeneral=null;
		
		try {
			//Se agrega validación para determinar si el detalle de cobertura se consulta a través
			//del contrato de entidad subcontratada o convenio		
			if(coberturaEntidadSub){
				codigosDetalleCoberturaGeneral= obtenerCodigoDetalleCoberturaGeneralEntidadSub(con,codigoContrato, 
						codigoViaIngreso, tipoPaciente, codigoNaturalezaPaciente,  codigoInstitucion);
			}else{
				codigosDetalleCoberturaGeneral= obtenerCodigoDetalleCoberturaGeneral(con,codigoContrato, 
						codigoViaIngreso, tipoPaciente, codigoNaturalezaPaciente,  codigoInstitucion);
			}		
			
			if(codigosDetalleCoberturaGeneral==null || codigosDetalleCoberturaGeneral.size()<1)
			{
				//no se debe seguir buscando no existe  
				Log4JManager.info("->NO EXISTE COBERTURA!!!!");
				return infoCobertura;
			}
			
			Log4JManager.info("\n NUMERO DE CODIGOS GENERALES->"+codigosDetalleCoberturaGeneral.size());
			for(int w=0; w<codigosDetalleCoberturaGeneral.size(); w++)
			{	
				Log4JManager.info("\n ******************************ENCABEZADO "+codigosDetalleCoberturaGeneral.get(w).toString()+"************* w="+w);
				//si llega aca entonces se debe evaluar por servicio especifico
				infoCobertura= obtenerCoberturaArticuloEspecifico(con, Integer.parseInt(codigosDetalleCoberturaGeneral.get(w).toString()), codigoArticulo);
				if(infoCobertura.existe())
				{
					//no se debe seguir buscando existe para el articulo mandamos el String cargado
					Log4JManager.info("RETORNA Existe cobertura por articulo especifico!!!! cubre?->"+infoCobertura.getIncluido());
					return infoCobertura;
				}
				
				//de lo contrario debe seguir buscando en los grupos
				infoCobertura= obtenerCoberturaArticuloAgrupado(con, Integer.parseInt(codigosDetalleCoberturaGeneral.get(w).toString()), codigoArticulo);
				if(infoCobertura.existe())
				{
					//no se debe seguir buscando existe para el articulo mandamos el String cargado
					Log4JManager.info("RETORNA Existe cobertura por articulo agrupado!!!! cubre?->"+infoCobertura.getIncluido());
					return infoCobertura;
				}
				Log4JManager.info("continua siguiente codigo");
			}	
		
		} 
		catch (BDException bde) {
			throw bde;
		}
		catch (Exception e) {
			Log4JManager.error(e.getMessage(),e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}	
		
		return infoCobertura;
	}

	/**
	 * metodo para obtener la cobertura de articulo especifico
	 * @param con
	 * @param codigoDetalleCoberturaGeneral
	 * @param codigoArticulo
	 * @return
	 */
	private static InfoCobertura obtenerCoberturaArticuloEspecifico(Connection con, int codigoDetalleCoberturaGeneral, int codigoArticulo) throws BDException
	{
		InfoCobertura infoCobertura= new InfoCobertura();
		PreparedStatement pst=null;
		ResultSet rs=null;
		
		try {
			Log4JManager.info("############## Inicio obtenerCoberturaArticuloEspecifico");
			String consulta=" SELECT " +
				"requiere_autorizacion as requiereaut, " +
				"coalesce(semanas_min_cotizacion,"+ConstantesBD.codigoNuncaValido+") as semanas, " +
				"coalesce(cantidad_max_cub_x_ingreso, "+ConstantesBD.codigoNuncaValido+") as cantidad " +
			"from " +
				"cobertura_articulos " +
			"where " +
				"codigo_detalle_cob=? and codigo_articulo=?";

			pst = con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setDouble(1, Utilidades.convertirADouble(codigoDetalleCoberturaGeneral+""));
			pst.setInt(2, codigoArticulo);
			rs=pst.executeQuery();
			if(rs.next())
			{	
				infoCobertura.setExiste(true);
				infoCobertura.setIncluido(true);
				infoCobertura.setRequiereAutorizacion(rs.getString("requiereaut"));
				infoCobertura.setSemanasMinimasCotizacion(rs.getInt("semanas"));
				infoCobertura.setCantidad(rs.getInt("cantidad"));
			}	
		} 
		catch(SQLException sqe){
			Log4JManager.error(sqe);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, sqe);
		}
		catch(Exception e){
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		finally{
			try{
				if(rs != null){
					rs.close();
				}
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException se) {
				Log4JManager.error("###########  Error close ResultSet - PreparedStatement", se);
			}
		}
		Log4JManager.info("############## Fin obtenerCoberturaArticuloEspecifico");
		return infoCobertura;
	}
	
	/**
	 * metodo para obrtener la cobertura articulo agrupado
	 * @param con
	 * @param codigoDetalleCoberturaGeneral
	 * @param codigoArticulo
	 * @return
	 */
	private static InfoCobertura obtenerCoberturaArticuloAgrupado(Connection con, int codigoDetalleCoberturaGeneral, int codigoArticulo) throws BDException 
	{
		//la prioridad en este punto va primero por la subgrupo, luego x grupo, luego clase, luego naturaleza
		InfoCobertura infoCobertura= new InfoCobertura();
		PreparedStatement pst=null;
		ResultSet rs=null;
		
		try {
			Log4JManager.info("############## Inicio obtenerCoberturaArticuloAgrupado");
			String consulta="SELECT " +
						"requiere_autorizacion as requiereaut, " +
						"coalesce(semanas_min_cotizacion,"+ConstantesBD.codigoNuncaValido+") as semanas, " +
						"coalesce(cantidad_max_cub_x_ingreso, "+ConstantesBD.codigoNuncaValido+") as cantidad " +
					"from " +
						"cob_agrup_articulos " +
					"where " +
						"codigo_detalle_cob=? " +
						"and (subgrupo=(select s.subgrupo from subgrupo_inventario s where s.codigo=(select a.subgrupo from articulo a where a.codigo=?)) or subgrupo is null) " +
						"and (grupo=(select s.grupo from subgrupo_inventario s where s.codigo=(select a.subgrupo from articulo a where a.codigo=?)) or grupo is null) " +
						"and (clase=(select s.clase from subgrupo_inventario s where s.codigo=(select a.subgrupo from articulo a where a.codigo=?)) or clase is null) " +
						"and (naturaleza=(select a.naturaleza from articulo a where a.codigo=?) or naturaleza is null) ";
						
			//para que la consulta nos entregue los resultados con prioridad que no sean null solo es requerido hacerle un order by
			consulta+=" ORDER BY subgrupo, grupo, clase, naturaleza ";

			pst = con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setDouble(1, Utilidades.convertirADouble(codigoDetalleCoberturaGeneral+""));
			pst.setInt(2, codigoArticulo);
			pst.setInt(3, codigoArticulo);
			pst.setInt(4, codigoArticulo);
			pst.setInt(5, codigoArticulo);
			rs=pst.executeQuery();
			if(rs.next())
			{	
				infoCobertura.setExiste(true);
				infoCobertura.setIncluido(true);
				infoCobertura.setRequiereAutorizacion(rs.getString("requiereaut"));
				infoCobertura.setSemanasMinimasCotizacion(rs.getInt("semanas"));
				infoCobertura.setCantidad(rs.getInt("cantidad"));
			}	
		} 
		catch(SQLException sqe){
			Log4JManager.error(sqe);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, sqe);
		}
		catch(Exception e){
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		finally{
			try{
				if(rs != null){
					rs.close();
				}
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException se) {
				Log4JManager.error("###########  Error close ResultSet - PreparedStatement", se);
			}
		}
		Log4JManager.info("############## Fin obtenerCoberturaArticuloAgrupado");
		return infoCobertura;
	}

	/**
	 * 
	 * @param con
	 * @param codigoContrato
	 * @param codigoViaIngreso
	 * @param tipoPaciente
	 * @param codigoServicioExcluyente
	 * @param codigoProgramaExcluyente
	 * @param codigoNaturalezaPaciente
	 * @param codigoInstitucion
	 * @return
	 */
	public static InfoCobertura validacionCoberturaPrograma(
			Connection con, long codigoContrato, int codigoViaIngreso,
			String tipoPaciente, Double codigoPrograma, int codigoNaturalezaPaciente,
			int codigoInstitucion) throws BDException
	{
		InfoCobertura infoCobertura= new InfoCobertura();
		//La filosofia de cobertura es primero buscar las EXCEPCIONES de esta y luego buscar las coberturas
		logger.info("**********************OBTENER COBERTURA X PROGRAMA***************************");
		//primero obtenemos lo de la excepcion
		infoCobertura=obtenerExcepcionCoberturaPrograma(con, codigoContrato, codigoViaIngreso, tipoPaciente, codigoPrograma, codigoNaturalezaPaciente, codigoInstitucion);
		
		if(infoCobertura.existe())
		{
			//no se debe seguir buscando existe en la excepcion 
			return infoCobertura;
		}
		//de lo contrario se busca en las coberturas
		logger.info("no existia en las excpciones por lo tanto se busca en las coberturas");
		infoCobertura= obtenerCoberturaPrograma(con, codigoContrato, codigoViaIngreso, tipoPaciente, codigoPrograma, codigoNaturalezaPaciente, codigoInstitucion);
		
		return infoCobertura;
	
	}

	
	/**
	 * metodo para obtener las excepcione de cobertura x programa
	 * @param con
	 * @param codigoContrato
	 * @param codigoViaIngreso
	 * @param codigoServicio
	 * @param codigoNaturalezaPaciente
	 * @param codigoInstitucion
	 * @return
	 */
	private static InfoCobertura obtenerExcepcionCoberturaPrograma(Connection con, long codigoContrato, int codigoViaIngreso, String tipoPaciente, Double codigoPrograma, int codigoNaturalezaPaciente, int codigoInstitucion) throws BDException
	{
		InfoCobertura infoCobertura= new InfoCobertura();
		logger.info("**********************OBTENER EXCEPCIONES COBERTURA***************************");
		int codigoExcepcionCoberturaGeneral= obtenerCodigoExcepcionCoberturaGeneral(con,codigoContrato, codigoViaIngreso, tipoPaciente, codigoNaturalezaPaciente,  codigoInstitucion);
		
		if(codigoExcepcionCoberturaGeneral<1)
		{
			//no se debe seguir buscando no existe  
			logger.info("->NO EXISTE EXCPCIONES COBERTURA!!!!");
			return infoCobertura;
		}
		
		//si llega aca entonces se debe evaluar por servicio especifico
		infoCobertura= obtenerExcepcionCoberturaProgramaEspecifico(con, codigoExcepcionCoberturaGeneral, codigoPrograma);
		if(infoCobertura.existe())
		{
			//no se debe seguir buscando existe excepciones para el servicio mandamos el String cargado
			logger.info("Existe excepcion cobertura por servicio especifico!!!! cubre?->"+infoCobertura.getIncluido());
			return infoCobertura;
		}
		
		//de lo contrario debe seguir buscando en los grupos
		//NO EXISTE POR PROGRAMA AGRUPADO.......

		return infoCobertura;
	}
	
	
	/**
	 * obtiene la cobertura de servicio especifico
	 * @param con
	 * @param codigoExcepcionCoberturaGeneral
	 * @param codigoServicio
	 * @return
	 */
	private static InfoCobertura obtenerExcepcionCoberturaProgramaEspecifico(Connection con, int codigoExcepcionCoberturaGeneral, Double codigoPrograma) 
	{
		InfoCobertura infoCobertura= new InfoCobertura();
		String consulta="SELECT " +
							"incluido as incluye, " +
							"coalesce(cantidad, "+ConstantesBD.codigoNuncaValido+") as cantidad " +
						"FROM " +
							"pro_exe_cob_convxcont " +
						"WHERE codigo_excepcion=? AND programa=?";
		
		PreparedStatementDecorator ps;
		ResultSetDecorator rs;
		try 
		{
			logger.info("obtenerExcepcionCoberturaProgramaEspecifico->"+consulta+" codGeneral->"+codigoExcepcionCoberturaGeneral+" codProg->"+codigoPrograma);
			ps =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDouble(1, Utilidades.convertirADouble(codigoExcepcionCoberturaGeneral+""));
			ps.setDouble(2, codigoPrograma);
			rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				infoCobertura.setExiste(true);
				infoCobertura.setIncluido(rs.getString("incluye"));
				infoCobertura.setCantidad(rs.getInt("cantidad"));
			}	
		} 
		catch (SQLException e) 
		{
			logger.error("ERROR EN obtenerExcepcionCoberturaProgramaEspecifico 1");
			e.printStackTrace();
		}
		logger.info(infoCobertura.loggerCobertura());
		return infoCobertura;
	}
	
	/**
	 * metodo que obtiene la cobertura x servicio
	 * @param con
	 * @param codigoContrato
	 * @param codigoViaIngreso
	 * @param codigoServicio
	 * @param codigoNaturalezaPaciente
	 * @param codigoInstitucion
	 * @return
	 */
	private static InfoCobertura obtenerCoberturaPrograma(Connection con, long codigoContrato, int codigoViaIngreso, String tipoPaciente, Double codigoPrograma, int codigoNaturalezaPaciente, int codigoInstitucion) throws BDException
	{
		logger.info("**********************OBTENER COBERTURA (NO EXISTE EXCEPCION)***************************");
		InfoCobertura infoCobertura= new InfoCobertura();

		Vector codigosDetalleCoberturaGeneral= obtenerCodigoDetalleCoberturaGeneral(con,codigoContrato, codigoViaIngreso, tipoPaciente, codigoNaturalezaPaciente,  codigoInstitucion);
		
		if(codigosDetalleCoberturaGeneral.size()<1)
		{
			//no se debe seguir buscando no existe  
			logger.info("->NO EXISTE COBERTURA!!!!");
			return infoCobertura;
		}
		logger.info("\n\nNUMERO DETALLES COBERTURA-->"+codigosDetalleCoberturaGeneral.size());
		for(int w=0; w<codigosDetalleCoberturaGeneral.size(); w++)
		{	
			logger.info("\n\n*****************************CODIGO DETALLE COBERTURA "+codigosDetalleCoberturaGeneral.get(w)+"**************w-->"+w);
			//si llega aca entonces se debe evaluar por servicio especifico
			infoCobertura= obtenerCoberturaProgramaEspecifico(con,Integer.parseInt(codigosDetalleCoberturaGeneral.get(w).toString()), codigoPrograma);
			if(infoCobertura.existe())
			{
				//no se debe seguir buscando existe para el servicio mandamos el String cargado
				logger.info("Existe cobertura por servicio especifico!!!! cubre?->"+infoCobertura.getIncluido());
				return infoCobertura;
			}
			
			//NO EXISTE PROGRAMA AGRUPADO
			
			logger.info("continua siguiente");
		}	

		return infoCobertura;
	}
	
	/**
	 * metodo para obtener la cobertura de programa especifico
	 * @param con
	 * @param codigoDetalleCoberturaGeneral
	 * @param codigoServicio
	 * @return
	 */
	private static InfoCobertura obtenerCoberturaProgramaEspecifico(Connection con, int codigoDetalleCoberturaGeneral, Double codigoPrograma) 
	{
		InfoCobertura infoCobertura= new InfoCobertura();
		String consulta="SELECT " +
							"coalesce(cantidad, "+ConstantesBD.codigoNuncaValido+") as cantidad " +
						"from " +
							"cobertura_programas " +
						"where " +
							"codigo_detalle_cob=? " +
							"and programa=?";
		
		PreparedStatementDecorator ps;
		ResultSetDecorator rs;
		try 
		{
			logger.info("obtenerCoberturapROGRAMAEspecifico->"+consulta+" codGeneral->"+codigoDetalleCoberturaGeneral+" codProg->"+codigoPrograma);
			ps =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDouble(1, Utilidades.convertirADouble(codigoDetalleCoberturaGeneral+""));
			ps.setDouble(2, codigoPrograma);
			rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{	
				infoCobertura.setExiste(true);
				infoCobertura.setIncluido(true);
				infoCobertura.setCantidad(rs.getInt("cantidad"));
			}	
		} 
		catch (SQLException e) 
		{
			logger.error("ERROR EN obtenerCoberturaServicioEspecifico 1");
			e.printStackTrace();
		}
		logger.info(infoCobertura.loggerCobertura());
		return infoCobertura;
	
	}
 }