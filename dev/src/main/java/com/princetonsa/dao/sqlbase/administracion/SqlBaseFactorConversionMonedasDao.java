package com.princetonsa.dao.sqlbase.administracion;

import java.sql.Connection;
import java.sql.Date;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.administracion.DtoFactorConversionMonedas;

/**
 * 
 * @author wilson
 *
 */
public class SqlBaseFactorConversionMonedasDao 
{
	/**
	 * Objeto para manejar los logs de esta clase
	 */
	private static Logger logger = Logger.getLogger(SqlBaseFactorConversionMonedasDao.class);
	
	/**
	 * cadena para la eliminacion
	 */
	private static final String cadenaEliminacionStr="DELETE FROM factor_conversion_monedas WHERE codigo=?";
	
	/**
	 * cadena para la modificacion
	 */
	private static final String cadenaModificacionSET_Str="UPDATE factor_conversion_monedas SET codigo_moneda=?, fecha_inicial=?, fecha_final=?, factor=?, institucion=?, usuario_modificacion=? ";
	
	private static final String cadenaModificacionWHERE_Str = " WHERE codigo=? ";
	
	
	
	/**
	 * cadena para la insercion
	 */
	private static final String cadenaInsertarStr="INSERT INTO factor_conversion_monedas (codigo, codigo_moneda, fecha_inicial, fecha_final, factor, institucion, fecha_modificacion, hora_modificacion, usuario_modificacion)";
	private static final String cadenaInsertarPos="VALUES (?, ?, ?, ?, ?, ?, CURRENT_DATE, "+ValoresPorDefecto.getSentenciaHoraActualBD()+", ?)";
	private static final String cadenaInsertarOra="VALUES (?, ?, ?, ?, ?, ?, (select to_date(to_char(sysdate, 'YYYYMMDD'),'YYYYMMDD') from dual), substr((select to_char(sysdate, 'hh24:mi:ss') from dual), 1,5), ?)";
	
	/**
	 * consulta la info EN CASO DE AGREGAR UN NUEVO CAMPO ENTONCES agregarlo al <INDICES_MAPA>
	 */
	private static final String cadenaConsultaStr= "SELECT " +
														"fc.codigo as codigo, " +
														"fc.codigo_moneda as codigotipomoneda, " +
														"tm.codigo_tipo_moneda ||' '|| tm.descripcion as descripciontipomoneda," +
														"fc.factor as factor, " +
														"fc.fecha_inicial as fechainicialbd, " +
														"to_char(fc.fecha_inicial, 'DD/MM/YYYY') as fechainicial, " +
														"fc.fecha_final as fechafinalbd, " +
														"to_char(fc.fecha_final, 'DD/MM/YYYY') as fechafinal, " +
														"fc.institucion as institucion, " +
														"fc.usuario_modificacion AS usuario," +
														"'"+ConstantesBD.acronimoSi+"' AS estabd " +
													"FROM " +
														"administracion.factor_conversion_monedas fc " +
														"INNER JOIN administracion.tipos_moneda tm ON (tm.codigo=fc.codigo_moneda) " +
													"WHERE 1=1 ";
	
	
	/**
	 * 
	 * @param dtoFactor
	 * @param tipoBD 
	 * @return
	 */
	private static String armarConsulta(DtoFactorConversionMonedas dtoFactor)
	{
		String cadena= cadenaConsultaStr;
		
				if(dtoFactor.getCodigo()>0)
			cadena+="AND fc.codigo="+dtoFactor.getCodigo()+" ";
		if(dtoFactor.getCodigoMoneda().getCodigo()>0)
			cadena+="AND fc.codigo_moneda="+dtoFactor.getCodigoMoneda().getCodigo()+" ";
		if(!UtilidadTexto.isEmpty(dtoFactor.getFactor()))
			cadena+="AND fc.factor="+dtoFactor.getFactor()+" ";
		if(!UtilidadTexto.isEmpty(dtoFactor.getFechaInicial()) && !UtilidadTexto.isEmpty(dtoFactor.getFechaFinal()))
		{	
			cadena+=	"AND (("+dtoFactor.getFechaInicial()+" >= fc.fecha_inicial " +
						"AND "+dtoFactor.getFechaInicial()+" <= fc.fecha_final) " +
						"OR ("+dtoFactor.getFechaFinal()+" >= fc.fecha_inicial " +
						"AND "+dtoFactor.getFechaFinal()+" <= fc.fecha_final)) ";
		}
		else
		{
			if(!UtilidadTexto.isEmpty(dtoFactor.getFechaInicial()))
				cadena+=" AND fc.fecha_final='"+dtoFactor.getFechaFinal()+"' ";
			if(!UtilidadTexto.isEmpty(dtoFactor.getFechaFinal()))
				cadena+=" AND fc.fecha_inicial='"+dtoFactor.getFechaFinal()+"' ";
		}
		if(dtoFactor.getCodigoInstitucion()>0)
			cadena+="AND fc.institucion= "+dtoFactor.getCodigoInstitucion();
		
		cadena+=" ORDER BY descripciontipomoneda, fechainicialbd ";
		
		//logger.info("\n CONSULTA FACTOR CONVERSION MONEDAS-->"+cadena);
		
		return cadena;
	}
	
	
	/**
	 * Consulta  
	 * @param con
	 * @param centroAtencion
	 * @return
	 */
	public static HashMap cargar(Connection con, DtoFactorConversionMonedas dtoFactor)
	{
		String consulta= armarConsulta(dtoFactor);
		String[] indicesMapa={"codigo_", "codigotipomoneda_", "descripciontipomoneda_", "factor_", "fechainicial_", "fechafinal_","acronimotipo_", "tipo_", "estabd_","usuario_","institucion_"};
		HashMap mapa= new HashMap();
		PreparedStatementDecorator ps=null;
		try 
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		} 
		catch (SQLException e) 
		{
			
			e.printStackTrace();
		}
		
		logger.warn(mapa);
		mapa.put("INDICES_MAPA", indicesMapa);
		return mapa;
	}
	
	/**
	 * 
	 * @param con
	 * @param string
	 * @return
	 */
	public static boolean eliminarRegistro(Connection con, int codigo)
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaEliminacionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigo);
			return ps.executeUpdate()>0;
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
	 * @param Tipo_BD 
	 * @param codigo
	 */
	public static boolean insertar(Connection con, DtoFactorConversionMonedas dtoFactor, int Tipo_BD)
	{
		
		//logger.info("entro a insertar "+dtoFactor.obtenerDtoString());
		try
		
		{
			
						String cadenaInsertar=cadenaInsertarStr;
			switch(Tipo_BD)
			{
			case DaoFactory.ORACLE:
				cadenaInsertar+=cadenaInsertarOra;
				break;
			case DaoFactory.POSTGRESQL:
				cadenaInsertar+=cadenaInsertarPos;
				break;
				default:
						}
			
			
			
						PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaInsertar,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_factor_conversion_monedas"));
			ps.setInt(2, dtoFactor.getCodigoMoneda().getCodigo());
			ps.setDate(3, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(dtoFactor.getFechaInicial())));
			ps.setDate(4, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(dtoFactor.getFechaFinal())));
			ps.setDouble(5, Utilidades.convertirADouble(dtoFactor.getFactor()));
			ps.setInt(6, dtoFactor.getCodigoInstitucion());
			ps.setString(7, dtoFactor.getLoginUsuario());
			
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
	 * @param tipoBD 
	 * @param codigo
	 */
	public static boolean modificar(Connection con, DtoFactorConversionMonedas dtoFactor, int tipoBD )
	{
		try
		{
			String cadena = cadenaModificacionSET_Str;
			
			switch(tipoBD)
			{
				case DaoFactory.ORACLE:
					cadena += ",fecha_modificacion=(select to_date(to_char(sysdate, 'YYYYMMDD'),'YYYYMMDD') from dual),hora_modificacion=(select to_char(sysdate, 'hh24:mi') from dual)";
				break;
				case DaoFactory.POSTGRESQL:
					cadena += ",fecha_modificacion=CURRENT_DATE, hora_modificacion="+ValoresPorDefecto.getSentenciaHoraActualBD()+" ";
				break;
			}
			cadena += cadenaModificacionWHERE_Str;
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, dtoFactor.getCodigoMoneda().getCodigo());
			ps.setDate(2, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(dtoFactor.getFechaInicial())));
			ps.setDate(3, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(dtoFactor.getFechaFinal())));
			ps.setDouble(4, Utilidades.convertirADouble(dtoFactor.getFactor()));
			ps.setInt(5, dtoFactor.getCodigoInstitucion());
			ps.setString(6, dtoFactor.getLoginUsuario());
			ps.setInt(7, dtoFactor.getCodigo());
			if(ps.executeUpdate()>0)
				return true;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return false;
	}
}