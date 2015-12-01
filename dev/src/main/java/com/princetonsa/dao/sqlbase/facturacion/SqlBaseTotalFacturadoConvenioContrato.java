package com.princetonsa.dao.sqlbase.facturacion;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.decorator.ResultSetDecorator;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.Utilidades;

public class SqlBaseTotalFacturadoConvenioContrato 
{

	
	/**
	 * 
	 */
	public static Logger logger = Logger.getLogger(SqlBaseTotalFacturadoConvenioContrato.class);
	
	
	/**
	 * 
	 */
	public static String cadenaConsultaTotalFacturadoConvContrato = "SELECT " +
																	"sc.convenio as codigoconvenio, " +
																	"getnombreconvenio(sc.convenio) as nombreconvenio, " +
																	"c.numero_contrato as numerocontrato, " +
																	"to_char(c.fecha_inicial,'dd/mm/yyyy') as fechainicial, " +
																	"to_char(c.fecha_final,'dd/mm/yyyy') as fechafinal, " +
																	"sum(f.valor_convenio) as valorconvenio, " +
																	"sum(f.valor_bruto_pac) as valorpaciente, " +
																	"(sum(f.valor_convenio)+sum(f.valor_bruto_pac)) as valortotal " +
																	"FROM facturas f " +
																	"LEFT OUTER JOIN anulaciones_facturas af ON(af.codigo=f.codigo) " +
																	"INNER JOIN sub_cuentas sc ON(sc.sub_cuenta=f.sub_cuenta) " +
																	"INNER JOIN contratos c ON(c.codigo=sc.contrato) " +
																	"WHERE to_char(f.fecha,'yyyy-mm')=? ";
	
	
	
	/**
	 * 
	 * @param con
	 * @param excluirFacturas
	 * @param convenio
	 * @param contrato
	 * @return
	 */
	public static String cambiarConsulta(Connection con, String excluirFacturas, String convenio, String contrato, String periodo) 
	{
		String  cadena="";
		
		if(excluirFacturas.equals(ConstantesBD.acronimoSi))
		{
			cadena=" f.codigo not in(select codigo from anulaciones_facturas where to_char(fecha_grabacion,'yyyy-mm')>='"+periodo+"') ";
		}
		else
		{
			cadena=" f.codigo not in(select codigo from anulaciones_facturas where to_char(fecha_grabacion,'yyyy-mm')='"+periodo+"') ";
		}
		
		if(!convenio.equals(""))
		{
			cadena+=" AND f.convenio="+convenio;
		}
		if(!contrato.equals(""))
		{
			cadena+=" AND sc.contrato="+contrato;
		}
		
		cadena+=" group by sc.convenio,c.numero_contrato,c.fecha_inicial,c.fecha_final ";
		
		return cadena;
	}
	
	
	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public static HashMap consultarTotalFacturado(Connection con, HashMap vo) 
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		
		String cadena=cadenaConsultaTotalFacturadoConvContrato;
		
		if((vo.get("excluirfacturas")+"").equals(ConstantesBD.acronimoSi))
		{	
			cadena+=" AND f.codigo not in(select codigo from anulaciones_facturas where to_char(fecha_grabacion,'yyyy-mm')>='"+vo.get("periodo")+"') ";
		}
		else
		{
			cadena+=" AND f.codigo not in(select codigo from anulaciones_facturas where to_char(fecha_grabacion,'yyyy-mm')='"+vo.get("periodo")+"') ";
		}
		if(!(vo.get("convenio")+"").equals(""))
		{
			cadena+=" AND f.convenio="+vo.get("convenio");
		}
		if(!(vo.get("contrato")+"").equals(""))
		{
			cadena+=" AND sc.contrato="+vo.get("contrato") ;
		}
		
		cadena+=" group by sc.convenio, c.numero_contrato, c.fecha_inicial, c.fecha_final";
		
		try
		{
			PreparedStatementDecorator busqueda= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			busqueda.setString(1, vo.get("periodo")+"");
			logger.info("cadena >>>>>>> "+cadena);
			
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(busqueda.executeQuery()));
		}
		catch(SQLException e)
		{
		e.printStackTrace();
		}
		return mapa;
	}

	
	
}
