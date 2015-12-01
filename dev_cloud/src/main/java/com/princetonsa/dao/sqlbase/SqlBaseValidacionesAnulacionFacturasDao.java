/*
 * @(#)SqlBaseValidacionesAnulacionFacturasDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_05
 *
 */
package com.princetonsa.dao.sqlbase;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Vector;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.ValoresPorDefecto;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.facturacion.DtoConsultaFacturasAnuladas;

/**
 * Esta clase implementa la funcionalidad común a todas (o casi todas)
 * las BD utilizadas en axioma, en general se trata de las consultas que
 * utilizan SQL estándar. Métodos particulares a Validaciones 
 * de anulación de  Facturas
 *
 * @version 1.0 Ago 20, 2005
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Ríos</a>
 */
public class SqlBaseValidacionesAnulacionFacturasDao 
{
    /**
	* Objeto para manejar los logs de esta clase
	*/
	private static Logger logger = Logger.getLogger(SqlBaseValidacionesAnulacionFacturasDao .class);

	/**
	 * Metodo que verifica la existencia de otras cuentas abiertas, abiertasDistribuidas, asociadas, asociadasDistribuidas 
	 * asociadasFacturadaParcial, facturadaParcial de un paciente y una cuenta dado el idFactura
	 * @param con
	 * @param codigofactura
	 * @return
	 */
	public static boolean existenOtrasCuentasAbiertas(Connection con, int codigoFactura)
	{
		try
		{
		    String verificarOtrasCuentasAbiertasStr=	"SELECT id AS otra_cuenta " +
																		"FROM cuentas WHERE id<>(SELECT f.cuenta FROM facturas f WHERE f.codigo=? ) " +
																		"AND codigo_paciente=(SELECT f.cod_paciente FROM facturas f WHERE f.codigo=?) " +
																		"AND estado_cuenta IN " +
																		"("+ConstantesBD.codigoEstadoCuentaActiva+", " +
																		//si en algun momento se va annadir otro estado, toca evaluar
																		//el funcionamineto en la funcionalidad anulación de Facturas
																		ConstantesBD.codigoEstadoCuentaAsociada+", "+
																		ConstantesBD.codigoEstadoCuentaFacturadaParcial+")";
			 
			boolean resp=false; 
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(verificarOtrasCuentasAbiertasStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,codigoFactura);
			pst.setInt(2,codigoFactura);
			
			ResultSetDecorator rs=new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
			{
				resp=true;
			}
			
			return resp;			
		}
		catch(SQLException e)
		{
			logger.error("Error en existenOtrasCuentasAbiertas de SqlBaseValidacionesAnulacionFacturasDao: "+e);
			return false;
		}
	}
	 
	/**
	 * Método que devuelve el estado de la cuenta dado su id
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public static int getEstadoCuenta(Connection con, int idCuenta)
	{
		try
		{
		    String verificarSiFueDistribuidaStr= "SELECT estado_cuenta AS estado from cuentas WHERE id=?";
			int resp=ConstantesBD.codigoNuncaValido;
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(verificarSiFueDistribuidaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,idCuenta);
			ResultSetDecorator rs=new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
			    resp= rs.getInt("estado");
			return resp;
		}
		catch(SQLException e)
		{
			logger.error("Error en getEstadoCuenta de SqlBaseValidacionesAnulacionFacturasDao: "+e);
			return ConstantesBD.codigoNuncaValido;
		}
	}
	
	/**
	 * Metodo que indica si la factura pertenece a una cuenta de cobro en caso de ser asi devuelve el
	 * numero de cuenta de cobro
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public static String getCuentaCobroDeFactura(Connection con, int codigoFactura)
	{
	    try
		{
	        String verificar=	"SELECT numero_cuenta_cobro  AS numeroCuentaCobro FROM facturas WHERE codigo=?";
			double numeroCuentaCobro= 0; 
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(verificar,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,codigoFactura);
			ResultSetDecorator rs=new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
				numeroCuentaCobro=rs.getDouble("numeroCuentaCobro");
			if(numeroCuentaCobro<=0)
			    return "";
			else
			    return numeroCuentaCobro+"";
		}
		catch(SQLException e)
		{
			logger.error("Error en getCuentaCobroDeFactura de SqlBaseValidacionesAnulacionFacturasDao: "+e);
			return "";
		}
	}
	
	/**
	 * Metodo que indica si la facura pertenece a un particular o a un convenio
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public static boolean esFacturaResponsableParticular(Connection con, int codigoFactura)
	{
	    try
		{
		    String verificarStr=	"SELECT CASE WHEN cod_res_particular IS NULL THEN false ELSE true END AS esResponsableParticular FROM facturas WHERE codigo=?";
			boolean resp=false; 
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(verificarStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,codigoFactura);
			
			ResultSetDecorator rs=new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
			{
				resp=rs.getBoolean("esResponsableParticular");
			}
			
			return resp;			
		}
		catch(SQLException e)
		{
			logger.error("Error en esFacturaResponsableParticular de SqlBaseValidacionesAnulacionFacturasDao: "+e);
			return false;
		}
	}
	
	/**
	 * Metodo que indica si una factura pertenece a un responsable CAPITADO
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public static boolean facturaPerteneceAConvenioCapitado(Connection con, int codigoFactura)
	{
	    try
		{
		    String verificarStr=	"SELECT (	SELECT CASE WHEN t.codigo = "+ConstantesBD.codigoTipoContratoCapitado+" THEN true " +
		    												"ELSE false END FROM tipos_contrato t WHERE t.codigo=c.tipo_contrato) " +
		    												"AS facturaPerteneceConvenioCapitado " +
		    							"FROM facturas f " +
		    							"INNER JOIN convenios c ON (c.codigo=f.convenio) WHERE f.codigo=?";
			boolean resp=false; 
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(verificarStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,codigoFactura);
			
			ResultSetDecorator rs=new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
			{
				resp=rs.getBoolean("facturaPerteneceConvenioCapitado");
			}
			
			return resp;			
		}
		catch(SQLException e)
		{
			logger.error("Error en esFacturaResponsableParticular de SqlBaseValidacionesAnulacionFacturasDao: "+e);
			return false;
		}
	}
	
	/**
	 * Método que indica si una factura esta asociada a un pagare
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public static String estaFacturaAsociadaAPagare(Connection con, int codigoFactura)
	{
	    String numeroPagare="";
	    if(!getCuentaCobroDeFactura(con, codigoFactura).equals(""))
	    {
	        if(esFacturaResponsableParticular(con, codigoFactura))
	        {
	            //@todo se cargará en un futuro el numero de pagaré-.. cuando se este la doucmentacion por el momento se deja XXXXXX
	            numeroPagare="XXXXXXX";
	        }
	    }
	    return numeroPagare;
	}
	
	/**
	 * Método que devuelve  'naturalezaAjuste - numeroAjuste' ya sea por factura o por cuenta de cobro, 
	 * toma los ajustes que estan en estado generado, es decir, los que estan pendientes de anular o aprobar,
	 * en caso de ser "" entonces es que no existen. 
	 * @param con
	 * @param codigoFactura
	 * @param cuentaCobro
	 * @return
	 */
	public static String facturaTieneAjustesPendientesEnFacturaYCuentaCobro(Connection con, int codigoFactura, String cuentaCobro)
	{
	    try
		{
		    String casoXFacturas=	"SELECT ta.descripcion ||'-'|| ae.consecutivo_ajuste ||',' AS tipoAjusteYConsecutivoAjuste " +
		    									"FROM ajus_fact_empresa a " +
		    									"INNER JOIN ajustes_empresa ae ON (a.codigo=ae.codigo) " +
		    									"INNER JOIN tipos_ajuste ta ON (ta.codigo=ae.tipo_ajuste) " +
		    									"WHERE a.factura=? and ae.estado="+ConstantesBD.codigoEstadoCarteraGenerado;
		    
		    String casoXCuentaCobro="SELECT ta.descripcion ||'-'|| ae.consecutivo_ajuste ||',' AS tipoAjusteYConsecutivoAjuste " +
		    										"FROM ajustes_empresa ae " +
		    										"INNER JOIN tipos_ajuste ta ON (ta.codigo=ae.tipo_ajuste) " +
		    										"WHERE ae.cuenta_cobro=? and ae.estado="+ConstantesBD.codigoEstadoCarteraGenerado;
		    
			String resp=""; 
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(casoXFacturas+" UNION "+casoXCuentaCobro,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,codigoFactura);
			pst.setString(2, cuentaCobro);
			
			ResultSetDecorator rs=new ResultSetDecorator(pst.executeQuery());
			while(rs.next())
			{
				resp+=rs.getString("tipoAjusteYConsecutivoAjuste");
			}
			return resp;			
		}
		catch(SQLException e)
		{
			logger.error("Error en facturaTieneAjustesPendientesEnFacturaYCuentaCobro de SqlBaseValidacionesAnulacionFacturasDao: "+e);
			return "";
		}
	}
	
	/**
	 * Validacion de la sumatoria del movimiento en cartera = 0, es decir (ajustes_debito - ajustes_credito) = 0 
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public static boolean sumatoriaMovCarteraEsCero(Connection con, int codigoFactura)
	{
	    try
		{
		    String verificarStr=	"SELECT CASE WHEN (ajustes_debito - ajustes_credito) = 0  THEN 'true' " +
		    							"ELSE 'false' END AS sumatoriaMovCarteraEsCero FROM facturas WHERE codigo=?";
			boolean resp=false; 
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(verificarStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,codigoFactura);
			
			ResultSetDecorator rs=new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
			{
				resp=UtilidadTexto.getBoolean(rs.getString("sumatoriaMovCarteraEsCero"));
			}
			
			return resp;			
		}
		catch(SQLException e)
		{
			logger.error("Error en sumatoriaMovCarteraEsCero de SqlBaseValidacionesAnulacionFacturasDao: "+e);
			return false;
		}
	}
	
	/**
	 * Método que indica si una factura tiene un valor de abono
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public static boolean existeValorAbonoFactura (Connection con, int codigoFactura)
	{
	    try
		{
		    String verificarStr=	"SELECT CASE WHEN valor_abonos > 0 THEN 'true' ELSE 'false' END AS existeValorAbono FROM facturas WHERE codigo=?"; 
			boolean resp=false; 
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(verificarStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,codigoFactura);
			ResultSetDecorator rs=new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
			{
				resp= UtilidadTexto.getBoolean(rs.getString("existeValorAbono"));
			}
			return resp;			
		}
		catch(SQLException e)
		{
			logger.error("Error en existeValorAbonoFactura de SqlBaseValidacionesAnulacionFacturasDao: "+e);
			return false;
		}
	}
	
	/**
	 * Método que indica si una factura tiene un valor bruto paciente
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public static boolean existeValorBrutoPaciente (Connection con, int codigoFactura)
	{
	    try
		{
		    String verificarStr=	"SELECT CASE WHEN valor_bruto_pac > 0 THEN 'true' ELSE 'false' END AS existeValor FROM facturas WHERE codigo=?";
		    logger.info("EXISTE VALOR BRUPTO PACIENTE------>"+verificarStr+" ---->codFact--->"+codigoFactura);
			boolean resp=false; 
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(verificarStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,codigoFactura);
			ResultSetDecorator rs=new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
			{
				resp=UtilidadTexto.getBoolean(rs.getString("existeValor"));
			}
			logger.info("retorna existe val bruto pac ----->"+resp);
			return resp;			
		}
		catch(SQLException e)
		{
			logger.error("Error en existeValorBrutoPaciente de SqlBaseValidacionesAnulacionFacturasDao: "+e);
			return false;
		}
	}
	
	/**
	 * Método que indica si una cuenta tiene o no asocio
	 * @param con
	 * @param codigoCuenta
	 * @param valorTrueSegunBD
	 * @return
	 */
	public static boolean cuentaTieneAsocio(Connection con, int codigoCuenta, String valorTrueSegunBD)
	{
	    try
		{
		    String verificarStr="SELECT codigo FROM asocios_cuenta WHERE (cuenta_inicial=? OR cuenta_final=?) AND activo="+valorTrueSegunBD+"";
		    logger.info("verificarSTr cuenta tiene asocio--->"+verificarStr+" -->codCuenta="+codigoCuenta);
		    PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(verificarStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,codigoCuenta);
			pst.setInt(2, codigoCuenta);
			ResultSetDecorator rs=new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
			    return true;
			return false;
		}
		catch(SQLException e)
		{
			logger.error("Error en existeValorBrutoPaciente de SqlBaseValidacionesAnulacionFacturasDao: "+e);
			return false;
		}
	}
	
	/**
	 * Método que indica si la cuenta asociada esta en una sola factura
	 * @param con
	 * @param codigoCuenta
	 * @param codigoCuentaAsociada
	 * @return
	 */
	public static boolean esCuentaAsociadaEnUnaSolaFactura(Connection con, int codigoCuenta, int codigoCuentaAsociada)
	{
	    try
	    {
	        String verificarStr="select count(1) AS contador from facturas WHERE cuenta IN (?,?) AND estado_facturacion<>"+ConstantesBD.codigoEstadoFacturacionAnulada;
	        PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(verificarStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,codigoCuenta);
			pst.setInt(2, codigoCuentaAsociada);
			ResultSetDecorator rs=new ResultSetDecorator(pst.executeQuery());
			int contador=0;
			if(rs.next())
			    contador= rs.getInt("contador");
			if(contador==1)
			    return true;
			else
			    return false;
	    }
	    catch (SQLException e)
	    {
	        logger.error("Error en esCuentaAsociadaEnUnaSolaFactura de SqlBaseValidacionesAnulacionFacturasDao: "+e);
			return false;
	    }
	}
	
	/**
	 * Método que devuelve le codigo de la cuenta asociada dado un id de cuenta
	 * @param con
	 * @param codigoCuenta
	 * @param valorTrueSegunBD
	 * @return
	 */
	public static Vector<String> getCodigoCuentasAsociadas(Connection con, int ingreso)
	{
		Vector<String> vector = new Vector<String>();
	    try
	    {
	        String verificarStr="SELECT " +
	        						"tabla.* " +
	        					"FROM " +
										"(" +
											"(	" +
												"SELECT " +
													"c.id " +
												"FROM " +
													"cuentas c " +
													"INNER JOIN asocios_cuenta a ON(a.cuenta_inicial=c.id) " +
												"WHERE " +
													"c.id_ingreso=? " +
													"AND activo="+ValoresPorDefecto.getValorTrueParaConsultas()+"" +
											") " +
											"UNION " +
											"(" +
												"SELECT " +
													"c.id " +
												"FROM " +
													"cuentas c " +
													"INNER JOIN asocios_cuenta a ON(a.cuenta_final=c.id) " +
												"WHERE " +
													"c.id_ingreso=? " +
													"AND activo="+ValoresPorDefecto.getValorTrueParaConsultas()+"" +
											")" +
										")tabla " +
	       						"WHERE " +
	       							"tabla.id <> (SELECT max(id) from cuentas where id_ingreso=?) ";
	        
	        logger.info("\n\n******************************************************************************************************");
	        logger.info("getCodigoCuentasAsociadas---->"+verificarStr+" ->"+ingreso);
	        logger.info("******************************************************************************************************\n\n");
	        
	        PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(verificarStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1, ingreso);
			pst.setInt(2, ingreso);
			pst.setInt(3, ingreso);
			
			ResultSetDecorator rs=new ResultSetDecorator(pst.executeQuery());
			while(rs.next())
			{
			    vector.add(rs.getString(1));
			}
	    }
	    catch (SQLException e)
	    {
	        logger.error("Error en getCodigoCuentaAsociada de SqlBaseValidacionesAnulacionFacturasDao: "+e);
	    }
	    return vector;
	}
	
	/**
	 * Método que indica si una cuenta distribuida es unida, de lo contrario es independiente
	 * @param con
	 * @param codigoCuenta
	 * @return
	 */
	public static boolean cuentaDistribuidaEsUnida(Connection con, int codigoCuenta)
	{
	    try
	    {
	        String verificarStr="SELECT es_unida FROM sub_cuentas WHERE cuenta=?";
	        PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(verificarStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,codigoCuenta);
			ResultSetDecorator rs=new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
			{
			    return rs.getBoolean("es_unida");
			}
			return false;
	    }
	    catch(SQLException e)
	    {
	        logger.error("Error en esCuentaDistribuidaUnida de SqlBaseValidacionesAnulacionFacturasDao: "+e);
			return false;
	    }
	}
	
	/**
	 * Metodo que indica si una cuenta  tiene mi9nimo otra factura facturada
	 * @param con
	 * @param codigoFactura
	 * @param codigoCuenta
	 * @return
	 */
	public static boolean cuentaTieneMinimoOtraFacturaFacturada (Connection con, int codigoFactura, int codigoCuenta)
	{
	    try
	    {
	        String verificarStr="SELECT CASE WHEN estado_facturacion="+ConstantesBD.codigoEstadoFacturacionFacturada+" THEN 'true' ELSE 'false' END AS esFact FROM facturas WHERE cuenta=? AND codigo<>?";
	        PreparedStatementDecorator pst= new PreparedStatementDecorator(con, verificarStr);
			pst.setInt(1,codigoCuenta);
			pst.setInt(2, codigoFactura);
			
			ResultSetDecorator rs=new ResultSetDecorator(pst.executeQuery());
			while(rs.next())
			{
			    if(UtilidadTexto.getBoolean(rs.getString("esFact")))
			    {
			    	pst.close();
			    	rs.close();
			        return true;
			    }
			}
			pst.close();
	    	rs.close();
			return false;
	    }
	    catch(SQLException e)
	    {
	        logger.error("Error en cuentaUnicaDistribuidaTieneMinimoOtraFacturaFacturada de SqlBaseValidacionesAnulacionFacturasDao: "+e);
			return false;
	    }
	}
	
	/**
	 * Método que devuelve le codigo de la cuenta asociada dado un id de cuenta
	 * @param con
	 * @param codigoCuenta
	 * @param valorTrueSegunBD
	 * @return
	 */
	public static int getCodigoCuentaDadaFactura(Connection con, int codigoFactura)
	{
	    try
	    {
	        String verificarStr="SELECT cuenta FROM facturas WHERE codigo=?";
	        PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(verificarStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,codigoFactura);
			ResultSetDecorator rs=new ResultSetDecorator(pst.executeQuery());
			int cuenta=0;
			if(rs.next())
			{
			    cuenta= rs.getInt("cuenta");
			}
			return cuenta;
	    }
	    catch (SQLException e)
	    {
	        logger.error("Error en getCodigoCuentaDadaFactura de SqlBaseValidacionesAnulacionFacturasDao: "+e);
			return 0;
	    }
	}
	
	/**
	 * Método que evalua si existe la parametrizacion de lod motivos de anulacion
	 * @param con
	 * @param codigoInstitucion
	 * @return
	 */
	public static boolean existenMotivosAnulacion(Connection con, int codigoInstitucion)
	{
	    try
	    {
	        String verificarStr="SELECT count(1) as cont FROM motivos_anul_fact WHERE institucion=?";
	        PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(verificarStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,codigoInstitucion);
			ResultSetDecorator rs=new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
			{
			    if(rs.getInt("cont")>0)
			        return true;
			    else
			        return false;
			}
			return false;
	    }
	    catch(SQLException e)
	    {
	        logger.error("Error en existenMotivosAnulacion de SqlBaseValidacionesAnulacionFacturasDao: "+e);
			return false;
	    }
	}
	
	/**
	 * Método que devuelve le codigo de la subcuenta dado un id de cuenta
	 * @param con
	 * @param subCuenta
	 * @return
	 */
	public static int getSubCuentaDadaFactura(Connection con, int codigoFactura)
	{
	    try
	    {
	        String verificarStr="SELECT sub_cuenta FROM facturas WHERE codigo=?";
	        PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(verificarStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,codigoFactura);
			ResultSetDecorator rs=new ResultSetDecorator(pst.executeQuery());
			int subCuenta=0;
			if(rs.next())
			{
			    subCuenta= rs.getInt("sub_cuenta");
			}
			return subCuenta;
	    }
	    catch (SQLException e)
	    {
	        logger.error("Error en getSubCuentaDadaFactura de SqlBaseValidacionesAnulacionFacturasDao: "+e);
			return 0;
	    }
	}
	
	/**
	 * Metodo que verifica si el valor neto a cargo del paciente es mayor a cero o no
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public static boolean esValorNetoACargoPacienteMayorCero(Connection con, String codigoFactura)
	{
	    try
	    {
	        String verificarStr="SELECT CASE WHEN valor_neto_paciente>0 THEN 'true' ELSE 'false' END AS esMayorCero FROM facturas WHERE codigo=?";
	        PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(verificarStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	        logger.info("esValorNetoACargoPacienteMayorCero==>"+verificarStr+" --->codFact="+codigoFactura);
			pst.setString(1,codigoFactura);
			ResultSetDecorator rs=new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
			{
			    return UtilidadTexto.getBoolean(rs.getString("esMayorCero"));
			}
			return false;
	    }
	    catch (SQLException e)
	    {
	        logger.error("Error en esValorNetoACargoPacienteMayorCero de SqlBaseValidacionesAnulacionFacturasDao: "+e);
			return false;
	    }
	}
	
	/**
	 * metodo que retorna el codigo del estado de pago del paciente
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public static int getCodigoEstadoPagoPaciente(Connection con, String codigoFactura)
	{
	    try
	    {
	        String verificarStr="select estado_paciente AS estado from facturas where codigo=?";
	        logger.info("getCodigoEstadoPagoPaciente ="+verificarStr+" -->codFact="+codigoFactura );
	        PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(verificarStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setString(1,codigoFactura);
			ResultSetDecorator rs=new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
			{
			    return rs.getInt("estado");
			}
			return ConstantesBD.codigoNuncaValido;
	    }
	    catch (SQLException e)
	    {
	        logger.error("Error en esValorNetoACargoPacienteMayorCero de SqlBaseValidacionesAnulacionFacturasDao: "+e);
			return ConstantesBD.codigoNuncaValido;
	    }
	}
	
	
	/**
	 * obtiene el valor de bruto paciente menos el valor de descuento
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public static double getValorBrutoPacMenosValDescuento(Connection con, String codigoFactura)
	{
	    try
	    {
	        String verificarStr=" SELECT valor_bruto_pac-val_desc_pac AS valor FROM facturas where codigo=?";
	        PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(verificarStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setString(1,codigoFactura);
			ResultSetDecorator rs=new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
			{
			    return rs.getDouble("valor");
			}
		}
	    catch(SQLException e)
	    {
	        logger.error("Error en getValorBrutoPacMenosValDescuento de SqlBaseValidacionesAnulacionFacturasDao: "+e);
		}
	    return 0;
	}

	/**
	 * 
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public static int getCodigoCuentaPrincipalDadaFactura(Connection con, int codigoFactura) 
	{
		try
		{
			String verificarStr="SELECT max(c.id) as cuenta FROM cuentas c WHERE c.id_ingreso=(select c1.id_ingreso FROM cuentas c1 INNER JOIN facturas f1 on(f1.cuenta=c1.id) where f1.codigo=?)";
			
			logger.info("\n\n*************************************************************************");
			logger.info("getCodigoCuentaPrincipalDadaFactura->"+verificarStr+" ->"+codigoFactura);
			logger.info("*************************************************************************\n\n");
			
	        PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(verificarStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,codigoFactura);
			ResultSetDecorator rs=new ResultSetDecorator(pst.executeQuery());
			int cuenta=0;
			
			if(rs.next())
			{
			    cuenta= rs.getInt("cuenta");
			}
			return cuenta;
		}
		catch (SQLException e)
		{
			logger.error("Error en getCodigoCuentaDadaFactura de SqlBaseValidacionesAnulacionFacturasDao: "+e);
			return 0;
		}
	}

	/**
	 * Restricciones del reporte
	 * @param dto
	 * @return
	 */
	public static String obtenerRestriccionesReporte(DtoConsultaFacturasAnuladas dto)
	{
		logger.info("\n\n CODIGO FACTURA-->"+dto.getCodigoFactura()+"\n\n");
		
		String restricciones="WHERE a.institucion= "+dto.getInstitucion();
        restricciones+= dto.getConsecutivoFactura().doubleValue()>0?" AND a.consecutivo_factura='"+dto.getConsecutivoFactura()+"' ":" "; 
        restricciones+=(dto.getConsecutivoAnulacion().doubleValue()>0)?" AND a.consecutivo_anulacion='"+dto.getConsecutivoAnulacion()+"' ":" ";
        restricciones+=(!dto.getFechaFinalAnulacion().equals(""))?" AND (to_char(a.fecha_grabacion, 'YYYY-MM-DD') >= '"+UtilidadFecha.conversionFormatoFechaABD(dto.getFechaInicialAnulacion())+"' and to_char(a.fecha_grabacion, 'YYYY-MM-DD') <= '"+UtilidadFecha.conversionFormatoFechaABD(dto.getFechaFinalAnulacion())+"') ":" ";
        restricciones+= (!dto.getLoginUsuario().trim().equals(""))?" AND a.usuario = '"+dto.getLoginUsuario()+"' ":" ";
        restricciones+= (dto.getCentroAtencion()>0)?" AND f.centro_aten = "+dto.getCentroAtencion()+" ":" ";
        restricciones+= (dto.getEmpresaInstitucion()>0)?" AND f.empresa_institucion = "+dto.getEmpresaInstitucion()+" ":" ";
        restricciones+=" ORDER BY a.consecutivo_factura ";
        return restricciones;
	}
	
	/**
	 * Metodo para validar si la factura viene o no de un ingreso odontologico
	 * @param codigoFactura
	 * @return
	 */
	public static boolean esFacturaIngresoOdontologico(BigDecimal codigoFactura)
	{
		boolean  retorna= false;
		String consultaStr="select " +
								"i.tipo_ingreso " +
							"from " +
								"manejopaciente.ingresos i " +
								"INNER JOIN manejopaciente.cuentas cu on(cu.id_ingreso=i.id) " +
								"INNER JOIN facturacion.facturas f on(f.cuenta=cu.id) " +
							"where " +
								"f.codigo=? ";
		
		
		try
		{
			Connection con= UtilidadBD.abrirConexion();
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setBigDecimal(1,codigoFactura);
			ResultSetDecorator rs=new ResultSetDecorator(pst.executeQuery());
			
			if(rs.next())
			{
				retorna= rs.getString(1).equals(ConstantesIntegridadDominio.acronimoTipoIngresoOdontologico);
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(pst, rs, con);
		}
		catch (SQLException e)
		{
			logger.error("Error en esFacturaIngresoOdontologico de SqlBaseValidacionesAnulacionFacturasDao: ",e);
		}
		return retorna;
	}
}