package com.princetonsa.dao.sqlbase.facturacion;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.Utilidades;

import com.princetonsa.dao.sqlbase.inventarios.SqlBaseArticuloCatalogoDao;
import com.princetonsa.decorator.ResultSetDecorator;

public class SqlBasePendienteFacturarDao 
{

	
	/**
	 * 
	 */
	public static Logger logger=Logger.getLogger(SqlBaseArticuloCatalogoDao.class);
	
	
	/**
	 * Cadena para la Busqueda 
	 */
	private static final String cadenaBuscarStr = "SELECT " +
														"c.fecha_corte as fechacorte, " +
														"c.codigo_medico as codigomedico, " +
														"getnombrepersona(c.codigo_medico) as nombremedico, " +
														"p.tipo_identificacion as tipoidentificacion, " +
														"p.numero_identificacion as numeroidentificacion, " +
														"getapellidos(c.codigo_medico) as apellidos, " +
														"getnombres(c.codigo_medico) as nombres, " +
														"sum(c.cantidad_cargada) as cantidad, " +
														"sum(c.valor_total_cargado) as valorcargado " +
													"FROM " +
														"consumos_liquidados c " +
													"inner join " +
														"personas p on(p.codigo=c.codigo_medico) " +
													"where " +
														"tipo_solicitud not in(6,9,13,15) ";
	
	/**
	 * 
	 * @param con
	 * @param fechaCorte
	 * @param medico
	 * @return
	 */
	public static HashMap consultarHonorariosPendientes(Connection con, String fechaCorte, String medico) 
	{
		
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		
		String cadena=cadenaBuscarStr;
		
		if(!fechaCorte.equals(""))
			cadena += "AND c.fecha_corte='"+UtilidadFecha.conversionFormatoFechaABD(fechaCorte)+"' ";
		
		if(!medico.equals(""))
			cadena += "AND c.codigo_medico="+medico;
		
		cadena+=" group by " +
					"c.fecha_corte," +
					"c.codigo_medico," +
					"getnombrepersona(c.codigo_medico)," +
					"p.tipo_identificacion," +
					"p.numero_identificacion," +
					"getapellidos(c.codigo_medico)," +
					"getnombres(c.codigo_medico) " +
				"order by c.codigo_medico";
		
		logger.info("CONSULTA -> "+cadena);
		
		try
		{
			PreparedStatementDecorator busqueda= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(busqueda.executeQuery()));
		}
		catch(SQLException e)
		{
		e.printStackTrace();
		}
		double total=0;
		int cantidadtotal=0;
		for(int i=0;i<Integer.parseInt(mapa.get("numRegistros")+"");i++)
		{
			total=total+Utilidades.convertirADouble(mapa.get("valorcargado_"+i)+"");
			cantidadtotal=cantidadtotal+Utilidades.convertirAEntero(mapa.get("cantidad_"+i)+"");
		}
		mapa.put("total", total+"");
		mapa.put("cantidadtotal", cantidadtotal+"");
		
		return mapa;
		
	}

	
	
}
