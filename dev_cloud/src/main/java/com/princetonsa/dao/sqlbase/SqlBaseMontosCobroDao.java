/*
 * Creado en 2/07/2004
 *
 * Juan David Ramírez
 * Princeton S.A.
 */
package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Collection;

import org.apache.log4j.Logger;

import com.princetonsa.actionform.cargos.MontosCobroForm;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.mundo.cargos.MontosCobro;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;

/**
 * @author Juan David Ramírez
 *
 */
public class SqlBaseMontosCobroDao
{
	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger = Logger.getLogger(SqlBaseMontosCobroDao.class);
	
	/**
	 * Sentencia para consultar los montos existentes
	 */
	private static final String buscarMontosStr="SELECT tm.nombre AS tipoMonto, es.descripcion AS estratoSocial, ta.nombre AS tipoAfiliado, vi.nombre AS viaIngreso, conv.nombre AS convenio, mc.codigo AS codigo, CASE WHEN mc.valor IS NULL THEN '0.0' ELSE mc.valor||'' END AS valor, CASE WHEN mc.porcentaje IS NULL THEN '0.0'  ELSE mc.porcentaje||'' END AS porcentaje, mc.activo AS activo, mc.vigencia_inicial AS fecha from facturacion.montos_cobro mc INNER JOIN facturacion.convenios conv ON (mc.convenio=conv.codigo) INNER JOIN manejopaciente.vias_ingreso vi ON(vi.codigo=mc.via_ingreso) INNER JOIN manejopaciente.tipos_afiliado ta ON(ta.acronimo=mc.tipo_afiliado) INNER JOIN manejopaciente.estratos_sociales es ON(es.codigo=mc.estrato_social) INNER JOIN facturacion.tipos_monto tm ON(tm.codigo=mc.tipo_monto)";
	
	/**
	 * Sentencia para modificar montos
	 */
	//private static final String modificarMontoStr="UPDATE montos_cobro set activo=? WHERE codigo=?";
	private static final String modificarMontoStr="UPDATE montos_cobro set convenio=?, via_ingreso=?, tipo_afiliado=?, estrato_social=?, tipo_monto=?, valor=?, porcentaje=?, activo=?, vigencia_inicial=? WHERE codigo=?";
	
	/**
	 * Sentencia para consultar los montos y enunciarlos en el form
	 */
	private static final String buscarMontosModificar="SELECT codigo AS codigo, convenio AS convenio, via_ingreso AS viaIngreso, tipo_afiliado AS tipoAfiliado, estrato_social AS estratoSocial, tipo_monto AS tipoMonto, valor AS valor, porcentaje AS porcentaje, activo AS activo, vigencia_inicial AS fecha from facturacion.montos_cobro";
	
	/**
	 * Sentencia para consultar un monto de cobro
	 */
	private static final String cargarMontoStr="SELECT codigo AS codigo, convenio AS convenio, via_ingreso AS viaIngreso, tipo_afiliado AS tipoAfiliado, estrato_social AS estratoSocial, tipo_monto AS tipoMonto, valor AS valor, porcentaje AS porcentaje, activo AS activo, vigencia_inicial AS fecha from montos_cobro WHERE codigo=?";

	/**
	 * Variable para guardar la consulta que se va a suar en el reporte
	 */
	private static String consultaSql="";
	
	/**
	 * Variable para guardar los parámetros con los que se genera el reprote
	 */
	private static String paramsReporte="";
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param convenio
	 * @param viaIngreso
	 * @param tipoAfiliado
	 * @param estratoSocial
	 * @param tipoMonto
	 * @param valor
	 * @param porcentaje
	 * @param activo
	 * @param esModificar
	 * @return collection con los resultados de la búsqueda de montos
	 */
	public static Collection buscarMontos(Connection con, int codigo, int convenio, int viaIngreso, String tipoAfiliado, int estratoSocial, int tipoMonto, float valor, float porcentaje, String activo, boolean esModificar, String fecha)
	{
		boolean primeravez=false;
		String buscar;
		String orden;
		paramsReporte="Parámetros de generación del Reporte: ";
		if(esModificar)
		{
			buscar=buscarMontosModificar;
						orden=" ORDER BY codigo, convenio, estratosocial";
			if(codigo!=0)
			{
				buscar+= !primeravez ? " where": " AND";
				buscar+=" codigo = "+codigo;
				primeravez=true;
				paramsReporte+="Código: "+codigo+" ,";
			}
			if(convenio!=0)
			{
				buscar+= !primeravez ? " where": " AND";
				buscar+="  convenio="+convenio;
				primeravez=true;
				paramsReporte+="Convenio: "+Utilidades.obtenerNombreConvenioOriginal(con, convenio)+" ,";
			}
			if(viaIngreso!=0)
			{
				buscar+= !primeravez ? " where": " AND";
				buscar+=" via_ingreso="+viaIngreso;
				primeravez=true;
				paramsReporte="Via Ingreso: "+Utilidades.obtenerNombreViaIngreso(con, viaIngreso)+" ,";
			}
			if(!tipoAfiliado.equals(""))
			{
				buscar+= !primeravez ? " where": " AND";
				buscar+=" tipo_afiliado='"+tipoAfiliado+"'";
				primeravez=true;
				paramsReporte+="Via Ingreso: "+viaIngreso+" ,";
			}
			if(estratoSocial!=0)
			{
				buscar+= !primeravez ? " where": " AND";
				buscar+=" estrato_social="+estratoSocial;
				primeravez=true;
				paramsReporte+="Estrato: "+estratoSocial+" ,";
			}
			if(tipoMonto!=0)
			{
				buscar+= !primeravez ? " where": " AND";
				buscar+=" tipo_monto="+tipoMonto;
				primeravez=true;
				paramsReporte+="Tipo Monto: "+tipoMonto+" ,";
			}
			if(valor>=0)
			{
				buscar+= !primeravez ? " where": " AND";
				buscar+=" valor="+valor;
				primeravez=true;
				paramsReporte+="Valor: "+valor+" ,";
			}
			if(porcentaje>=0)
			{
				buscar+= !primeravez ? " where": " AND";
				buscar+=" porcentaje="+porcentaje;
				primeravez=true;
				paramsReporte+="Porcentaje: "+porcentaje+" ,";
			}
			if(!activo.equals(""))
			{
				buscar+= !primeravez ? " where": " AND";
				buscar+=" activo="+activo;
				primeravez=true;
			}

			if(!fecha.equals(""))
			{
				buscar+= !primeravez ? " where": " AND";
				buscar+=" vigencia_inicial='"+fecha+"'";
				primeravez=true;
				paramsReporte+="Fecha Vigencia Inicial: "+UtilidadFecha.conversionFormatoFechaAAp(fecha)+" ,";
			}
		}
		else
		{
			buscar=buscarMontosStr;
			orden=" ORDER BY mc.codigo, conv.nombre, es.descripcion";
			if(codigo!=0)
			{
				buscar+= !primeravez ? " where": " AND";
				buscar+=" mc.codigo = "+codigo;
				primeravez=true;
				paramsReporte+="Código: "+codigo+" ,";
			}
			if(convenio!=0)
			{
				buscar+= !primeravez ? " where": " AND";
				buscar+=" conv.codigo="+convenio;
				primeravez=true;
				paramsReporte+="Convenio: "+Utilidades.obtenerNombreConvenioOriginal(con,convenio)+" ,";
			}
			if(viaIngreso!=0)
			{
				buscar+= !primeravez ? " where": " AND";
				buscar+=" vi.codigo="+viaIngreso;
				primeravez=true;
				paramsReporte+="Via Ingreso: "+Utilidades.obtenerNombreViaIngreso(con,viaIngreso)+" ,";
			}
			if(!tipoAfiliado.equals(""))
			{
				buscar+= !primeravez ? " where": " AND";
				buscar+=" ta.acronimo='"+tipoAfiliado+"'";
				primeravez=true;
				paramsReporte+="Tipo Afiliado: "+tipoAfiliado+" ,";				
			}
			if(estratoSocial!=0)
			{
				buscar+= !primeravez ? " where": " AND";
				buscar+=" es.codigo="+estratoSocial;
				primeravez=true;
				paramsReporte+="Estrato: "+estratoSocial+" ,";
			}
			if(tipoMonto!=0)
			{
				buscar+= !primeravez ? " where": " AND";
				buscar+=" tm.codigo="+tipoMonto;
				primeravez=true;
				paramsReporte+="Tipo Monto: "+tipoMonto+" ,";
			}
			if(valor>=0)
			{
				buscar+= !primeravez ? " where": " AND";
				buscar+=" mc.valor="+valor;
				primeravez=true;
				paramsReporte+="Valor: "+valor+" ,";
			}
			if(porcentaje>=0)
			{
				buscar+= !primeravez ? " where": " AND";
				buscar+=" mc.porcentaje="+porcentaje;
				primeravez=true;
				paramsReporte+="Porcentaje: "+porcentaje+" ,";
			}
			if(!activo.equals(""))
			{
				buscar+= !primeravez ? " where": " AND";
				buscar+=" mc.activo+='"+activo;
				primeravez=true;
			}
			if(!fecha.equals(""))
			{
				buscar+= !primeravez ? " where": " AND";
				buscar+=" mc.vigencia_inicial='"+fecha+"'";
				primeravez=true;
				paramsReporte+="Fecha Vigencia Inicial: "+UtilidadFecha.conversionFormatoFechaAAp(fecha)+" ,";
			}
		}
		buscar+=orden;
		logger.info("\n\n BUESUQEDA MONTOS-->"+buscar+"\n\n");
		
		PreparedStatementDecorator busqueda;
		
		try
		{
			consultaSql=buscar;
			logger.info("VALOR DE LA CONSULTA--->"+buscar);
			
			busqueda =  new PreparedStatementDecorator(con.prepareStatement(buscar,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			logger.info("-----*-**-*-*-*-*-*-*-*-"+buscar);
			return UtilidadBD.resultSet2Collection(new ResultSetDecorator(busqueda.executeQuery()));
		}
		catch (SQLException e)
		{
			logger.error("Error haciendo la búsqueda de montos de cobro "+e);
			return null;
		}
	}
	
	/**
	 * Clase para modificar los montos de cobro
	 * @param con
	 * @param codigo
	 * @param convenio
	 * @param viaIngreso
	 * @param tipoAfiliado
	 * @param estratoSocial
	 * @param tipoMonto
	 * @param valor
	 * @param porcentaje
	 * @param activo
	 * @return
	 */
	public static boolean modificarMonto(Connection con, int codigo, int convenio, int viaIngreso, String tipoAfiliado, int estratoSocial, int tipoMonto, float valor, float porcentaje, boolean activo, String fecha)
	{
		try
		{
			PreparedStatementDecorator modificar= new PreparedStatementDecorator(con.prepareStatement(modificarMontoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			modificar.setInt(1, convenio);
			modificar.setInt(2, viaIngreso);
			modificar.setString(3, tipoAfiliado);
			modificar.setInt(4, estratoSocial);
			modificar.setInt(5, tipoMonto);
			
			boolean seInsertoNulo=false;
			
			if(valor>=0)
			{
			    modificar.setFloat(6, valor);
			}
			else
			{
			    seInsertoNulo=true;
			    modificar.setObject(6, null);
			}
			
			//Solo podemos insertar un nulo
			if (porcentaje<0&&seInsertoNulo)
			{
			    modificar.setFloat(7, 0);
			}
			else if(porcentaje<0&&!seInsertoNulo)
			{
				modificar.setObject(7, null);
			}
			else
			{
				modificar.setFloat(7, porcentaje);
			}
			modificar.setBoolean(8, activo);
			modificar.setString(9, fecha);
			modificar.setInt(10, codigo);
			int resultado=modificar.executeUpdate();
			if(resultado<=0)
			{
				logger.error("Error modifiacndo monto de cobro --> no se modificó ningún elemento");
				return false;
			}
			return true;
		}
		catch (SQLException e)
		{
			logger.error("Error modificando monto de cobro "+codigo+": "+e);
			return false;
		}
	}
	/**
	 * Ingresar montos
	 * @param con
	 * @param codigo
	 * @param convenio
	 * @param viaIngreso
	 * @param tipoAfiliado
	 * @param estratoSocial
	 * @param tipoMonto
	 * @param valor
	 * @param porcentaje
	 * @param activo
	 * @return
	 */
	public static boolean insertarMonto(Connection con, int convenio,
			int viaIngreso, String tipoAfiliado, int estratoSocial,
			int tipoMonto, float valor, float porcentaje, boolean activo, String fecha, String insertarMontoStr)
	{
		try
		{
			PreparedStatementDecorator insertar= new PreparedStatementDecorator(con.prepareStatement(insertarMontoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			insertar.setInt(1, convenio);
			insertar.setInt(2, viaIngreso);
			insertar.setString(3, tipoAfiliado);
			insertar.setInt(4, estratoSocial);
			insertar.setInt(5, tipoMonto);
			logger.info("insertarMontoStr>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>><<<"+insertar+"***"+convenio+"*"+viaIngreso+"*"+tipoAfiliado+"*"+estratoSocial+"*"+tipoMonto+"*"+insertarMontoStr);
			boolean seInsertoNulo=false;
			
			if(valor>=0)
			{
				insertar.setFloat(6, valor);
			}
			else
			{
			    seInsertoNulo=true;
				insertar.setNull(6, Types.DOUBLE);
			}
			
			//Solo podemos insertar un nulo
			if (porcentaje<0&&!seInsertoNulo)
			{
			    insertar.setNull(7, Types.DOUBLE);
			}
			else
			{
			    insertar.setFloat(7, porcentaje);
			}
			insertar.setBoolean(8, activo);
			insertar.setString(9,  UtilidadFecha.conversionFormatoFechaABD(fecha));
			int resultado=insertar.executeUpdate();
			if(resultado<=0)
			{
				logger.error("Error insertando monto de cobro --> no se insertó ningún elemento");
				return false;
			}
			else
			{
				return true;
			}
			
		}
		catch (SQLException e)
		{
			logger.error("Error insertando monto de cobro "+e);
			return false;
		}

	}

	/**
	 * Método para cargar un monto de cobro
	 * @param con Conexión con la BD
	 * @param codigo del monto de cobro
	 * @return Collection con los datos del monto de cobro
	 */
	public static Collection cargar(Connection con, int codigo)
	{
		try
		{
			PreparedStatementDecorator stm= new PreparedStatementDecorator(con.prepareStatement(cargarMontoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			stm.setInt(1, codigo);
			return UtilidadBD.resultSet2Collection(new ResultSetDecorator(stm.executeQuery()));
		}
		catch (SQLException e)
		{
			logger.error("Error consuiltando el monto de cobro "+e);
			return null;
		}
	}
	
	/**
	 * Adición Sebastián
	 * Método para cargar los códigos de los montos de cobro de acuerdo
	 * a ciertos parámetros, estos parámetros de búsqueda pueden omitirse
	 * con 0 (Enteros) o "" (String)
	 * @param con
	 * @param convenio
	 * @param tipoAfiliado
	 * @param estrato
	 * @param viaIngreso
	 * @return
	 */
	public static Collection consultarMontosCobro(Connection con,int convenio,String tipoAfiliado,int estrato,int viaIngreso){
		try{
			//regula la colocación de 'WHERE' en el Query
			int indicador=0;
			//	regula la existencia de una condición anterior
			int indicador2=0;
			// regula la cuenta de parámetros a insertar en el prepared
			int contador=1;
			String consultarMontosCobroStr="SELECT dm.detalle_codigo as codigo FROM detalle_monto dm inner join montos_cobro mc on dm.monto_codigo=mc.codigo ";
			if(convenio!=0){
				if(indicador==0){
					consultarMontosCobroStr+=" WHERE";
					indicador=1;
				}
				consultarMontosCobroStr+=" mc.convenio=?";
				indicador2=1;
			}
			if(!tipoAfiliado.equals("")){
				if(indicador==0){
					consultarMontosCobroStr+=" WHERE";
					indicador=1;
				}
				if(indicador2==1){
					consultarMontosCobroStr+=" AND";
				}
				consultarMontosCobroStr+=" dm.tipo_afiliado_codigo=?";
				indicador2=1;
			}
			if(estrato!=0){
				if(indicador==0){
					consultarMontosCobroStr+=" WHERE";
					indicador=1;
				}
				if(indicador2==1){
					consultarMontosCobroStr+=" AND";
				}
				consultarMontosCobroStr+=" dm.estrato_social_codigo=?";
				indicador2=1;
			}
			if(viaIngreso!=0){
				if(indicador==0){
					consultarMontosCobroStr+=" WHERE";
					indicador=1;
				}
				if(indicador2==1){
					consultarMontosCobroStr+=" AND";
				}
				consultarMontosCobroStr+=" dm.via_ingreso_codigo=?";
				indicador2=1;
			}
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(consultarMontosCobroStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			 if(convenio!=0){
			 	pst.setInt(contador,convenio);
			 	contador++;
			 }
			 if(!tipoAfiliado.equals("")){
			 	pst.setString(contador,tipoAfiliado);
			 	contador++;
			 }
			 if(estrato!=0){
			 	pst.setInt(contador,estrato);
			 	contador++;
			 }
			 if(viaIngreso!=0){
			 	pst.setInt(contador,viaIngreso);
			 	contador++;
			 }
			 return UtilidadBD.resultSet2Collection(new ResultSetDecorator(pst.executeQuery()));
			
			
		}
		catch(SQLException e){
			logger.error("Error en la consulta de los mosntos de Cobro SqlBaseMontosCobroDao: ",e);
			return null;
		}
		
	}

	public static String getConsultaSql() {
		return consultaSql;
	}

	public static void setConsultaSql(String consultaSql) {
		SqlBaseMontosCobroDao.consultaSql = consultaSql;
	}

	public static String getParamsReporte() {
		return paramsReporte;
	}

	public static void setParamsReporte(String paramsReporte) {
		SqlBaseMontosCobroDao.paramsReporte = paramsReporte;
	}

		
	
}