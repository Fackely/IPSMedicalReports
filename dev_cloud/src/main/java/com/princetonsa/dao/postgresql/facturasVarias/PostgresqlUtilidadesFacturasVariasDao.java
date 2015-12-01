package com.princetonsa.dao.postgresql.facturasVarias;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;

import com.princetonsa.dao.facturasVarias.UtilidadesFacturasVariasDao;
import com.princetonsa.dao.sqlbase.facturasVarias.SqlBaseUtilidadesFacturasVarias;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.facturasVarias.DtoRecibosCaja;

/**
 * @author Víctor Hugo Gómez L.
 */
public class PostgresqlUtilidadesFacturasVariasDao implements UtilidadesFacturasVariasDao{
	
	
	private Logger logger = Logger.getLogger(PostgresqlUtilidadesFacturasVariasDao.class);
	/***
	 * ObtenerConceptosFraVarias
	 * @param con
	 * @param institucion
	 * @param activo
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> obtenerConceptosFraVarias(Connection con, int institucion, boolean activo)
	{
		return SqlBaseUtilidadesFacturasVarias.obtenerConceptosFraVarias(con, institucion, activo);
	}
	/**
	 * 
	 */
	@Override
	public ArrayList<HashMap<String, Object>> obtenerConceptosFraVarias(
			Connection con, int institucion, boolean activo, String tipoConcepto) {
		
	return SqlBaseUtilidadesFacturasVarias.obtenerConceptosFraVarias(con, institucion, activo, tipoConcepto);
	}
	
	
	@Override
	public BigDecimal obtenerPkFacturaVaria(BigDecimal consecutivoFacturaVaria,
			int institucion) {
		
		return SqlBaseUtilidadesFacturasVarias.obtenerPkFacturaVaria(consecutivoFacturaVaria, institucion);
	}
	
	
	
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public String aplicaReciboCaja(Connection con, DtoRecibosCaja dto)
	{
		PreparedStatementDecorator ps = null;
		ResultSetDecorator rs = null;
		String retorna = "";
		String consulta="select case "+ 
		 
						" WHEN 	(	" +
						
									" ( select count(0) " +
									"	from " +
									"		tesoreria.recibos_caja rc " +
									"	INNER JOIN " +
									"		tesoreria.devol_recibos_caja drc  " +
									"				ON (rc.numero_recibo_caja= drc.numero_rc and drc.doc_soporte='"+dto.getNroFactura()+"' and rc.estado <> "+ConstantesBD.codigoEstadoReciboCajaAnulado+" and drc.estado <> '"+ConstantesIntegridadDominio.acronimoEstadoAnulado+"') "+
									"	)=" +
									"	("+
									"	select count(0) "+ 
									" 		from tesoreria.recibos_caja rc "+ 
									" INNER JOIN "+
									"		tesoreria.detalle_conceptos_rc dcr ON  (rc.numero_recibo_caja=dcr.numero_recibo_caja and rc.estado <> "+ConstantesBD.codigoEstadoReciboCajaAnulado+" and dcr.doc_soporte ='"+dto.getNroFactura()+"') "+ 
									" INNER JOIN "+ 
									" 		conceptos_ing_tesoreria cit ON (cit.codigo=dcr.concepto and cit.institucion=dcr.institucion and cit.codigo_tipo_ingreso=3) " +
										")" + 
									"" +
									" )then 'S' else 'N' END AS sePuedeGenerarRC  ";
									
							
			
		logger.info("Sql \n"+consulta+"\n");
		
		try
		{
			ps = new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			rs = new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				retorna =rs.getString("sePuedeGenerarRC");
			}
		}
		catch (Exception e) {
			Log4JManager.error("Error en la Busqueda >>>>>>>> "+consulta);
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
				if (rs != null) {
					rs.close();
				}
			} catch (Exception e2) {
				Log4JManager.error("Error cerrando PreparedStatementDecorator - ResulsetDecorator: " + e2);
			}
		}
		return retorna;
	}
	
	

}
