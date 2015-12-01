package com.princetonsa.dao.sqlbase.parametrizacion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadTexto;
import util.ValoresPorDefecto;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.servinte.axioma.dto.salascirugia.TipoAnestesiaDto;
import com.servinte.axioma.fwk.exception.BDException;
import com.servinte.axioma.fwk.exception.util.CODIGO_ERROR_NEGOCIO;

/**
 * 
 * @author wilson
 *
 */
public class SqlBaseTecnicaAnestesiaDao 
{
	/**
	* Objeto para manejar los logs de esta clase
	*/
	private static Logger logger= Logger.getLogger(SqlBaseTecnicaAnestesiaDao.class);
	
	/**
	 * 
	 * @param con
	 * @param centroCosto
	 * @param institucion
	 * @return
	 */
	private static boolean existeParametrizacionTecnicaAnestesiaCC(Connection con, int centroCosto, int institucion)
	{
		String consulta="SELECT codigo from tipos_anestesia_inst_cc where centro_costo=? and institucion=? ";
		
		logger.info("\n existeParametrizacionTecnicaAnestesiaCC->"+consulta+" cc->"+centroCosto+" ins->"+institucion);
		
		PreparedStatementDecorator ps=null;
		try 
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, centroCosto);
			ps.setInt(2, institucion);
			if(ps.executeQuery().next())
				return true;
		}	
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return false;
	}	
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static boolean existeTecnicaAnestesia(Connection con, int numeroSolicitud)
	{
		String consulta="SELECT numero_solicitud from hoja_tipo_anestesia where numero_solicitud=?";
		
		PreparedStatementDecorator ps=null;
		try 
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, numeroSolicitud);
			if(ps.executeQuery().next())
				return true;
		}	
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	@Deprecated
	public static HashMap<Object, Object> cargarTecnicaAnestesiaSolicitud(Connection con, int numeroSolicitud)
	{
		HashMap<Object, Object> mapa= new HashMap<Object, Object>();
		mapa.put("numRegistros", 0);
		String consultaStr=	"";
		
		consultaStr=	"SELECT " +
							"hta.tipo_anestesia_inst_cc AS tipo_anestesia_inst_cc, " +
							"hta.tipo_anestesia AS tipo_anestesia, " +
							"ta.descripcion AS descripcion, " +
							"ta.acronimo AS acronimo " +
						"FROM " +
							"hoja_tipo_anestesia hta " +
							"INNER JOIN tipos_anestesia ta ON(ta.codigo=hta.tipo_anestesia) " +
						"WHERE " +
							"hta.numero_solicitud= "+numeroSolicitud+" ";
		
		logger.info("\n cargarTecnicaAnestesiaSolicitud->"+consultaStr+"\n");
		
		PreparedStatementDecorator ps=null;
		try 
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			mapa= UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}	
		catch (SQLException e) 
		{
			e.printStackTrace();
			return null;
		}
		
		return mapa;
	}
	
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @param centroCosto
	 * @param institucion
	 * @return
	 */
	public static HashMap<Object, Object> obtenerTecnicaAnestesia(Connection con, int numeroSolicitud, int centroCosto, int institucion, Vector<String> tiposAnestInstCCNoMostrar)
	{
		HashMap<Object, Object> mapa= new HashMap<Object, Object>();
		//primero se carga los historicos, 
		String consultaStr=	"";
		
		consultaStr=	"SELECT " +
							"hta.tipo_anestesia_inst_cc AS tipo_anestesia_inst_cc, " +
							"hta.tipo_anestesia AS tipo_anestesia, " +
							"ta.descripcion AS descripcion, " +
							"ta.acronimo AS acronimo " +
						"FROM " +
							"hoja_tipo_anestesia hta " +
							"INNER JOIN tipos_anestesia ta ON(ta.codigo=hta.tipo_anestesia) " +
						"WHERE " +
							"hta.numero_solicitud= "+numeroSolicitud+" ";
		
		if(tiposAnestInstCCNoMostrar.size()>0)
			consultaStr+="and hta.tipo_anestesia_inst_cc not in("+UtilidadTexto.convertirVectorACodigosSeparadosXComas(tiposAnestInstCCNoMostrar, false)+") ";
		
		
		if(existeParametrizacionTecnicaAnestesiaCC(con, centroCosto, institucion))
		{
			consultaStr+=	"UNION "+
							"SELECT " +
								"taic.codigo AS tipo_anestesia_inst_cc, " +
								"taic.tipo_anestesia AS tipo_anestesia, " +
								"ta.descripcion AS descripcion, " +
								"ta.acronimo AS acronimo " +
							"FROM " +
								"tipos_anestesia_inst_cc taic " +
								"INNER JOIN tipos_anestesia ta ON(ta.codigo=taic.tipo_anestesia) " +
							"WHERE " +
								"taic.centro_costo= "+centroCosto+" " +
								"AND taic.institucion= "+institucion+" " +
								"AND taic.activo='"+ConstantesBD.acronimoSi+"' " +
								"AND ta.mostrar_en_hanes="+ValoresPorDefecto.getValorTrueParaConsultas()+" ";
		
			if(tiposAnestInstCCNoMostrar.size()>0)
				consultaStr+="and taic.codigo not in("+UtilidadTexto.convertirVectorACodigosSeparadosXComas(tiposAnestInstCCNoMostrar, false)+") ";
													
			consultaStr+=	"order by descripcion ";
			
		}
		else 
		{
			consultaStr+=	"UNION "+
							"SELECT " +
								"taic.codigo AS tipo_anestesia_inst_cc, " +
								"taic.tipo_anestesia AS tipo_anestesia, " +
								"ta.descripcion AS descripcion, " +
								"ta.acronimo AS acronimo " +
							"FROM " +
								"tipos_anestesia_inst_cc taic " +
								"INNER JOIN tipos_anestesia ta ON(ta.codigo=taic.tipo_anestesia) " +
							"WHERE " +
								"taic.centro_costo is null " +
								"AND taic.institucion= "+institucion+" " +
								"AND taic.activo='"+ConstantesBD.acronimoSi+"' " +
								"AND ta.mostrar_en_hanes="+ValoresPorDefecto.getValorTrueParaConsultas()+" ";
			
			if(tiposAnestInstCCNoMostrar.size()>0)
				consultaStr+="and taic.codigo not in("+UtilidadTexto.convertirVectorACodigosSeparadosXComas(tiposAnestInstCCNoMostrar, false)+") ";
																		
								consultaStr+=	"order by descripcion ";		
		}
		
		logger.info("\n obtenerTecnicaAnestesia->"+consultaStr+"\n");
		
		PreparedStatementDecorator ps=null;
		try 
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			mapa= UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}	
		catch (SQLException e) 
		{
			e.printStackTrace();
			return null;
		}
		
		return mapa;
	}
	
	/**
	 * 
	 * @param con
	 * @param mapaSignoVitalAnestesia
	 * @return
	 */
    public static boolean insertarTecnicaAnestesia(Connection con, int numeroSolicitud, int tipoAnestesia, int tipoAnestesiaInstCC, String loginUsuario)
    {
    	String cadena="INSERT INTO hoja_tipo_anestesia 	(numero_solicitud, " +				//1
    														"tipo_anestesia, " +			//2
    														"tipo_anestesia_inst_cc, " +	//3	
    														"fecha_modifica, " +			
    														"hora_modifica, " +				
    														"usuario_modifica) "+			//4
    														"values (?, ?, ?," +	
    																"CURRENT_DATE, " +
    																""+ValoresPorDefecto.getSentenciaHoraActualBD()+", " +
    																"?)";
    	
    	try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, numeroSolicitud);
			ps.setInt(2, tipoAnestesia);
			ps.setInt(3, tipoAnestesiaInstCC);
			ps.setString(4, loginUsuario);
			
			if(ps.executeUpdate()>0)
				return true;
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return false;
    } 
	
	/**
	 * 
	 * @param con
	 * @param mapaSignoVitalAnestesia
	 * @return
	 */
    public static boolean eliminarTecnicaAnestesia(Connection con, int numeroSolicitud)
    {
    	String cadena="DELETE FROM hoja_tipo_anestesia WHERE numero_solicitud=? ";			
    	
    	try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, numeroSolicitud);
			
			if(ps.executeUpdate()>=0)
				return true;
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return false;
    } 
    
    
    
    /**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static int obtenerCodigoTecnicaDadoTecnicaCCInst(Connection con, int tipoAnestesiaInstCC)
	{
		String consultaStr=	"";
		
		consultaStr=	"SELECT " +
							"tipo_anestesia AS tipo_anestesia " +
						"FROM " +
							"tipos_anestesia_inst_cc " +
						"WHERE " +
							"codigo=? ";		
		
		logger.info("\n obtenerCodigoTecnicaDadoTecnicaCCInst->"+consultaStr+"\n");
		
		PreparedStatementDecorator ps=null;
		try 
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, tipoAnestesiaInstCC);
			ResultSetDecorator rs= new ResultSetDecorator(ps.executeQuery());
			
			if(rs.next())
			{	
				return rs.getInt("tipo_anestesia");
			}	
		}	
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		return ConstantesBD.codigoNuncaValido;
	}
	
	
	/**
	 * Método para consultar los tipos aplicables para la hoja de anestesia según la institución
	 * @param con
	 * @param institucion
	 * @param nroConsulta
	 * @return Collection
	 */
	public static Collection<Object> consultarTipoParametrizado (Connection con, int institucion, int nroConsulta)
	{
		String consultaStr="";
		
		if(nroConsulta==1)
		{	
			//Consulta de los tipos de técnicas de anestesia general que tienen opciones para la institución
		    consultaStr="SELECT " +
		    				"dtai.codigo AS codigo, " +
		    				"tta.descripcion AS nombre_tecnica, " +
		    				"cta.descripcion AS nombre_opcion " +
		        		"FROM " +
		        			"det_tec_anes_inst dtai " +
		        			"INNER JOIN tipo_tecnica_anestesia tta ON (dtai.tecnica=tta.codigo) " +
		        			"INNER JOIN campo_tec_anestesia cta ON (dtai.campo_tecnica=cta.codigo) " +
		        		"WHERE " +
		        			"dtai.institucion=? " +
		        			"AND tta.tipo="+ConstantesBD.codigoTecAnestesiaGralOpciones+
		        			" AND dtai.activo = "+ValoresPorDefecto.getValorTrueParaConsultas() + 
						" ORDER BY dtai.codigo, tta.codigo, cta.codigo";
		}
		else if(nroConsulta==2)
		{	
		    //Consulta de los tipos de técnicas de anestesia general sin opciones para la institución
			consultaStr="SELECT " +
							"dtai.codigo AS codigo, " +
							"tta.descripcion AS nombre_tecnica " +
					     "FROM " +
					     	"det_tec_anes_inst dtai " +
					     	"INNER JOIN tipo_tecnica_anestesia tta ON (dtai.tecnica=tta.codigo) " +
					     "WHERE " +
					     	"dtai.institucion=? " +
					     	"AND tta.tipo="+ConstantesBD.codigoTecAnestesiaGral+" "+   
							"AND dtai.activo="+ValoresPorDefecto.getValorTrueParaConsultas();
		}
		else if(nroConsulta==3)
		{	
		    //Consulta de los tipos de técnicas de anestesia general sin opciones para la institución
			consultaStr="SELECT " +
							"dtai.codigo AS codigo, " +
							"tta.descripcion AS nombre_tecnica " +
					     "FROM " +
					     	"det_tec_anes_inst dtai " +
					     	"INNER JOIN tipo_tecnica_anestesia tta ON (dtai.tecnica=tta.codigo) " +
					     "WHERE " +
					     	"dtai.institucion=? " +
					     	"AND tta.tipo="+ConstantesBD.codigoTecAnestesiaRegional+" "+   
							"AND dtai.activo="+ValoresPorDefecto.getValorTrueParaConsultas();
		}
					
		try
		{
	     	logger.info("\n\n\n La sentencia SQL BASE (tipos)" + consultaStr + " \n\n\n");
			PreparedStatementDecorator psConsulta =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			psConsulta.setInt(1, institucion);
			return UtilidadBD.resultSet2Collection(new ResultSetDecorator(psConsulta.executeQuery()));				
		} 
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	/**
     * Metodo para consultar y cargar la información de la sección Técnica de Anestesia
     * @param con
     * @param nroSolicitud
     * @return Collection -> Información de la sección técnica de anestesia
     */
    public static Collection<Object> cargarTecnicaAnestesiaGeneralRegional(Connection con, int nroSolicitud)
    {
    	String consultaStr="SELECT " +
    							"dhat.tecnica AS codigo, " +
    							"dhat.valor AS valor " +
    						"FROM " +
    							"det_hoja_anes_tecnica dhat " +
    							"INNER JOIN det_tec_anes_inst dtai ON  (dtai.codigo=dhat.tecnica)  " +
    						"WHERE " +
    							"dhat.numero_solicitud=? " +
    							"AND dtai.activo="+ValoresPorDefecto.getValorTrueParaConsultas();
    	
    	logger.info("cargarTecnicaAnestesiaGeneralRegional-->"+consultaStr+" numsol->"+nroSolicitud);
    	
    	try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, nroSolicitud);
			
			return UtilidadBD.resultSet2Collection(new ResultSetDecorator(ps.executeQuery()));
	    }
		catch (SQLException e)
		{
			e.printStackTrace();
			return null;
		}    	
    }
    
    /**
	 * Método para insertar las técnicas de anestesia general y regional parametrizadas
	 * @param con una conexion abierta con una fuente de datos
	 * @param nroSolicitud
	 * @param tecAnestesiaInst
	 * @param valor
	 * @return tecAnestesia
	 */
	public static int insertarTecnicaAnestesiaGeneralRegional (Connection con, int nroSolicitud, int tecAnestesiaInst, String valor)
	{
		PreparedStatementDecorator ps;
		int resp=0, codigo=0;
		String consultaStr = "";
		
		try
		{
			codigo = existeTecnicaAnestesia (con,nroSolicitud, tecAnestesiaInst);
			
			if(codigo!=-1)  
			{  
				 //-Si existe se modifica
				consultaStr=" UPDATE det_hoja_anes_tecnica SET valor = ? " +
															" WHERE numero_solicitud = ? AND tecnica= ?";
				
				ps= new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				
				ps.setString(1, valor);
				ps.setInt(2, nroSolicitud);					
				ps.setInt(3, tecAnestesiaInst);
				
				resp = ps.executeUpdate();
			}
			else
			{
				//Si el valor no es vacío
				if(UtilidadCadena.noEsVacio(valor))
				{
					consultaStr = "INSERT INTO det_hoja_anes_tecnica	(numero_solicitud, " +
																		 "tecnica, " +
																		 "valor) " +
																		 " VALUES " +
																		 " (?, ?, ?) ";
					
					ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					ps.setInt(1, nroSolicitud);				
					ps.setInt(2, tecAnestesiaInst);								
					ps.setString(3, valor);
					
					resp = ps.executeUpdate();
				}
				else
				{
					//Para que no aborte la transaccion así no haya insertado
					resp = 1;
				}
			}
			
			if(resp > 0)
			{
				resp = tecAnestesiaInst;
			}
					
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en la inserción o modificación de la técnica de anestesia : SqlBaseHojaAnestesiaDao "+e.toString() );
			resp = 0;
		}
		return resp;
	}
	
	/**
	 * Método para saber si existe o no la técnica de anestesia
	 * @param con -> conexion
	 * @param nroSolicitud
	 * @param tecAnestesiaInst
	 * @return nroSolicitud si existe sino retorna -1
	 */
	public static int existeTecnicaAnestesia (Connection con, int nroSolicitud, int tecAnestesiaInst)
	{
		String consultaStr="SELECT " +
								"numero_solicitud AS numero_solicitud " +
							"FROM " +
								"det_hoja_anes_tecnica " +
							"WHERE " +
								"numero_solicitud = ? AND tecnica = ? ";

		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, nroSolicitud);
			ps.setInt(2, tecAnestesiaInst);
			
			ResultSetDecorator resultado=new ResultSetDecorator(ps.executeQuery());
			
			if(resultado.next())
			{
				return resultado.getInt("numero_solicitud");
			}
			else
			{
				return ConstantesBD.codigoNuncaValido;
			}	
		}
		catch(SQLException e)
		{
			logger.warn(e+" Vericando Existencia de la técnica de anestesia : SqlBaseHojaAnestesiaDao "+e.toString());
			return ConstantesBD.codigoNuncaValido;
		}
	}
    
	
	/**
	 * Consulta las tecnicas de anestesia (tipos de anestesia) registrados en la hoja de anestesia
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return tipos de anestesia de la hoja de anestesia
	 * @throws BDException
	 * @author jeilones
	 * @created 16/07/2013
	 */
	public static List<TipoAnestesiaDto> consultarTecnicasAnestesiaXSolicitud(Connection con, int numeroSolicitud) throws BDException
	{
		StringBuffer consulta=	new StringBuffer("SELECT " )
							.append("ta.codigo AS codigo, "  )
							.append("ta.descripcion AS descripcion, " )
							.append("ta.acronimo AS acronimo " )
						.append("FROM salascirugia.hoja_tipo_anestesia hta " )
						.append("INNER JOIN salascirugia.tipos_anestesia ta ON(ta.codigo=hta.tipo_anestesia) " )
						.append("WHERE " )
							.append("hta.numero_solicitud= "+numeroSolicitud+" ")
							.append(" ORDER BY hta.FECHA_MODIFICA desc, hta.HORA_MODIFICA desc ");
		
		List<TipoAnestesiaDto>tiposAnestesia=new ArrayList<TipoAnestesiaDto>(0);
		PreparedStatement ps=null;
		ResultSet rs = null;
		try 
		{
			String cadena=consulta.toString();
			ps= con.prepareStatement(cadena);
			rs = ps.executeQuery();
			while(rs.next()){
				TipoAnestesiaDto tipoAnestesiaDto=new TipoAnestesiaDto();
				tipoAnestesiaDto.setCodigo(rs.getInt("codigo"));
				tipoAnestesiaDto.setAcronimo(rs.getString("acronimo"));
				tipoAnestesiaDto.setDescripcion(rs.getString("descripcion"));
				
				tiposAnestesia.add(tipoAnestesiaDto);
			}
		}	
		catch (SQLException e) 
		{
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		}finally{
			UtilidadBD.cerrarObjetosPersistencia(ps, rs, null);
		}
		
		return tiposAnestesia;
	}
}
