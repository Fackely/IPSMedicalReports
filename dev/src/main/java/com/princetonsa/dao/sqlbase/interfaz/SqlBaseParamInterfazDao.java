/*
 * Creado el 27/04/2006
 * Juan David Ramírez López
 */
package com.princetonsa.dao.sqlbase.interfaz;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import java.util.HashMap;
import org.apache.log4j.Logger;

import com.princetonsa.decorator.ResultSetDecorator;

import util.ConstantesBD;
import util.InfoDatosInt;
import util.UtilidadBD;
import util.Utilidades;
import util.ValoresPorDefecto;

public class SqlBaseParamInterfazDao
{
	/**
	 * Manejador de log de la clase
	 */
	private static Logger logger=Logger.getLogger(SqlBaseParamInterfazDao.class);
	
	/**
	 * Sentencia para consultar los campos de la interfaz
	 */
	private static final String consultarCamposInterfazStr="SELECT cr.codigo AS codigo_campo, tc.codigo AS codigo_tipo_campo, tc.nombre AS tipo_campo, cr.orden_campo AS orden_campo FROM campos_regis_interfaz cr INNER JOIN tipos_campo_interfaz tc ON(cr.tipo_campo_interfaz=tc.codigo) WHERE institucion=? ORDER BY orden_campo";
	
	/**
	 * Sentencia para ingresar el registro de interfaz
	 */
	private static final String ingresarRegistroInterfazStr="INSERT INTO param_regis_interfaz (codigo, consecutivo, tipo_interfaz, descripcion, login, fecha, hora, tipo_registro) VALUES (?,?,?,?,?, CURRENT_DATE, "+ValoresPorDefecto.getSentenciaHoraActualBDTipoTime()+", ?)";

	/**
	 * Sentencia para ingresar el detalle del registro de interfaz
	 */
	private static final String ingresarDetalleRegistroInterfazStr=" INSERT INTO detalle_regis_interfaz (codigo, param_regis_interfaz, campo_regis_interfaz, nombre, valor_tipo_campo_int, secun_tipo_campo_int, indicativo_existe, tamanio_campo) VALUES (?,?,?,?,?,?,?,?)";
	
	/**
	 * Sentencia para ingresar los valores escritos del detalle registro interfaz
	 */
	private static final String ingresarValoresInterfazStr="INSERT INTO detalle_valores_interfaz (detalle_registro_interfaz, valor, tipo) VALUES(?,?,?)";

	
	/**
	 * Sentencia para consultar el registro de interfaz
	 */
	private static final String consultarRegistroInterfazStr="SELECT codigo AS codigo, consecutivo AS consecutivo, tipo_interfaz AS tipo_interfaz, descripcion AS descripcion, tipo_registro AS tipo_registro FROM param_regis_interfaz WHERE codigo=?";

	/**
	 * Sentencia para consultar el detalle del registro de interfaz
	 */
	private static final String consultarDetalleRegistroInterfazStr="SELECT codigo AS codigo, param_regis_interfaz AS param_regis_interfaz, campo_regis_interfaz AS campo_regis_interfaz, nombre AS nombre, valor_tipo_campo_int AS valor_tipo_campo_int, secun_tipo_campo_int AS secun_tipo_campo_int, indicativo_existe AS indicativo_existe, tamanio_campo AS tamanio_campo FROM detalle_regis_interfaz WHERE param_regis_interfaz=?";
	
	/**
	 * Sentencia para consultar los valores escritos del detalle registro interfaz
	 */
	private static final String consultarValoresInterfazStr="SELECT valor AS valor, tipo AS tipo FROM detalle_valores_interfaz WHERE detalle_registro_interfaz=?";

	/**
	 * Listado de los registros de interfaz
	 */
	private static final String listadoRegistrosInterfazStr="SELECT " +
		"p.codigo AS codigo, " +
		"p.consecutivo AS consecutivo ," +
		"p.descripcion AS descripcion, " +
		"p.tipo_registro AS codigo_tipo_registro, " +
		"p.tipo_interfaz AS tipo_interfaz, " +
		"t.nombre AS tipo_registro " +
		"FROM param_regis_interfaz p " +
		"INNER JOIN tipos_registro t ON(t.codigo=p.tipo_registro) " +
		"INNER JOIN usuarios u on(u.login=p.login) " +
		"WHERE u.institucion=? ";
	
	/**
	 * Eliminar el detalle de la parametrización de interfaz
	 */
	private static final String eliminarAnteriorStr="" +
					"DELETE FROM detalle_valores_interfaz WHERE detalle_registro_interfaz IN (SELECT codigo FROM detalle_regis_interfaz WHERE param_regis_interfaz=?)" +
					"DELETE FROM detalle_regis_interfaz WHERE param_regis_interfaz=?";
	
	/**
	 * Actualizar datos de la parametrización de interfaz
	 */
	private static final String modificarRegistroInterfazStr="UPDATE param_regis_interfaz SET descripcion=? WHERE codigo=?";
	
	/**
	 * Método que consulta los campos parametrizados en la funcionalidad Parametrización Campos Interfaz
	 * @param con
	 * @param institucion
	 * @return
	 */
	public static HashMap consultarCamposInterfaz(Connection con, int institucion)
	{
		try
		{
			PreparedStatementDecorator stm= new PreparedStatementDecorator(con.prepareStatement(consultarCamposInterfazStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			stm.setInt(1, institucion);
			HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(stm.executeQuery()));
			stm.close();
			return mapaRetorno;
		}
		catch (SQLException e)
		{
			logger.error("Error consultando los campos de la interfaz : "+e);
			return null;
		}
	}
	
	/**
	 * Método para consultar las opciones de los selectores de la funcionalidad
	 * @param con
	 * @param numeroConsulta
	 * @return
	 */
	public static Collection consultarTipos(Connection con, int numeroConsulta)
	{
		String consulta="";
		switch(numeroConsulta)
		{
			case 1:
				consulta="SELECT codigo AS codigo, nombre AS nombre FROM tipos_indicativo_existe";
			break;
			case 2:
				consulta="SELECT codigo AS codigo, nombre AS nombre FROM tipo_tamanio_campo";
			break;
			case 3:
				consulta="SELECT vt.codigo AS codigo_opcion, ts.codigo AS codigo_tipo, ts.nombre AS nombre_opcion, ts.mostrar_en_anulacion AS mostrar_en_anulacion FROM tipo_selector_interfaz ts INNER JOIN valor_tipo_campo_interfaz vt ON(vt.tipo_selector_interfaz=ts.codigo)";
			break;
			case 4:
				consulta="SELECT codigo AS codigo_selector, tipo_selector_interfaz AS tipo_selector, tipo_campo_interfaz AS tipo_campo, es_principal AS principal from valor_tipo_campo_interfaz";
			break;
			case 5:
				consulta="SELECT codigo AS codigo_interfaz, nombre AS nombre_interfaz FROM tipos_interfaz WHERE codigo!="+ConstantesBD.tipoInterfazAmbos;
			break;
			case 6:
				consulta="SELECT codigo AS codigo, nombre AS nombre FROM tipos_registro";
			break;
			case 7:
				consulta="SELECT codigo AS codigo FROM valor_tipo_campo_interfaz WHERE tipo_selector_interfaz IN("+ConstantesBD.tipoSelectorInterfazDescripcionFija+", "+ConstantesBD.tipoSelectorInterfazFijo+")";
			break;
		}
		try
		{
			PreparedStatementDecorator stm= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			return UtilidadBD.resultSet2Collection(new ResultSetDecorator(stm.executeQuery()));
		}
		catch (SQLException e)
		{
			logger.error("Error consultando los campos de la interfaz : "+e);
			return null;
		}
	}
	
	/**
	 * Método para ingresar la parametrización del registro de interfaz
	 * @param con
	 * @param tipoInterfaz
	 * @param descripcion
	 * @param login
	 * @param codigoModificacion @todo
	 * @return
	 */
	public static int ingresarRegistroInterfaz(Connection con, int tipoInterfaz, String descripcion, String login, int tipoRegistro, int codigoModificacion)
	{
		try
		{
			PreparedStatementDecorator stm=null;
			if(codigoModificacion!=0)
			{
				stm= new PreparedStatementDecorator(con.prepareStatement(eliminarAnteriorStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				stm.setInt(1, codigoModificacion);
				stm.setInt(2, codigoModificacion);
				stm.executeUpdate();
			}

			int codigo=0;
			int consecutivo=0;
			if(codigoModificacion!=0)
			{
				codigo=codigoModificacion;
				stm= new PreparedStatementDecorator(con.prepareStatement(modificarRegistroInterfazStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				stm.setString(1, descripcion);
				stm.setInt(2, codigo);
			}
			else
			{
				stm= new PreparedStatementDecorator(con.prepareStatement(ingresarRegistroInterfazStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				codigo=UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_param_regis_interfaz");
				if(tipoInterfaz==ConstantesBD.tipoInterfazFacturacion)
				{
					consecutivo=UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_reg_interfaz_factur");
				}
				else
				{
					consecutivo=UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_reg_interfaz_anulfac");
				}
				stm.setInt(1, codigo);
				stm.setInt(2, consecutivo);
				stm.setInt(3, tipoInterfaz);
				stm.setString(4, descripcion);
				stm.setString(5, login);
				stm.setDouble(6, Utilidades.convertirADouble(tipoRegistro+""));
			}
			if(stm.executeUpdate()>0)
			{
				return codigo;
			}
			else
			{
				return 0;
			}
		}
		catch (SQLException e)
		{
			logger.error("Error ingresando el registro de interfaz "+e);
			return ConstantesBD.codigoNuncaValido;
		}
	}

	/**
	 * Método que ingresa el detalle del registro de interfaz
	 * @param con
	 * @param codigoRegistroInterfaz
	 * @param campoRegistroInterfaz
	 * @param nombre
	 * @param valor
	 * @param valorSecundario
	 * @param indicativoExiste
	 * @param tamanio
	 * @param codigoModificacion @todo
	 * @return
	 */
	public static int ingresarDetalleRegistroInterfaz(Connection con, int codigoRegistroInterfaz, int campoRegistroInterfaz, String nombre, InfoDatosInt valor, InfoDatosInt valorSecundario, InfoDatosInt indicativoExiste, InfoDatosInt tamanio)
	{
		/*
		 * Esto es para saber si el valo seleccionado es de tipo libre
		 */
		Collection colLibres=consultarTipos(con, 7);
		int numRegistrosLibres=colLibres.size();
		Iterator<HashMap> tiposLibresIt=colLibres.iterator();
		Vector<Integer> tiposLibres=new Vector<Integer>();
		for(int zz=0;zz<numRegistrosLibres;zz++)
		{
			HashMap tipoLibre=tiposLibresIt.next();
			tiposLibres.add(Integer.parseInt(tipoLibre.get("codigo")+""));
		}

		
		try
		{
			/*
			 * codigo,
			 * param_regis_interfaz,
			 * campo_regis_interfaz,
			 * nombre,
			 * valor_tipo_campo_int,
			 * secun_tipo_campo_int,
			 * indicativo_existe,
			 * tamanio_campo
			 */
			PreparedStatementDecorator stm= new PreparedStatementDecorator(con.prepareStatement(ingresarDetalleRegistroInterfazStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			int codigo=UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_detalle_regis_interfaz");
			stm.setInt(1, codigo);
			stm.setInt(2, codigoRegistroInterfaz);
			stm.setInt(3, campoRegistroInterfaz);
			stm.setString(4, nombre);
			boolean ingresarLibre=false;
			int codigoTempo=0;
			if(valor==null || valor.getCodigo()==-1)
			{
				stm.setNull(5, Types.INTEGER);
				if(valor!=null && valor.getCodigo()==-1)
				{
					ingresarLibre=true;
				}
			}
			else
			{
				codigoTempo=valor.getCodigo();
				stm.setInt(5, codigoTempo);
			}
			boolean ingresarFijo=false;
			if(codigoTempo==ConstantesBD.tipoSelectorInterfazDescripcionFija)
			{
				ingresarFijo=true;
			}
			if(valorSecundario==null)
			{
				stm.setNull(6, Types.INTEGER);
			}
			else
			{
				codigoTempo=valorSecundario.getCodigo();
				stm.setInt(6, codigoTempo);
			}
			boolean ingresarFijoSec=false;
			if(tiposLibres.contains(codigoTempo))
			{
				ingresarFijoSec=true;
			}
			int codigoExiste=indicativoExiste.getCodigo();
			stm.setInt(7, codigoExiste);
			int codigoTamanio=tamanio.getCodigo();
			stm.setInt(8, codigoTamanio);
			int resultadoFinal=stm.executeUpdate();
			if(codigoExiste==ConstantesBD.codIndicativoSiNoExisteDefault)
			{
				ingresarValoresInterfaz(con, codigo, indicativoExiste.getNombre(), ConstantesBD.tipoValorInterfazDefault);
			}
			if(codigoTamanio==ConstantesBD.codTamanioMenorQue)
			{
				ingresarValoresInterfaz(con, codigo, tamanio.getNombre(), ConstantesBD.tipoValorInterfazTamanoMenorQue);
			}
			else if(codigoTamanio==ConstantesBD.codTamanioFijo)
			{
				ingresarValoresInterfaz(con, codigo, tamanio.getNombre(), ConstantesBD.tipoValorInterfazTamanoFijo);
			}
			if(ingresarFijo)
			{
				ingresarValoresInterfaz(con, codigo, valor.getNombre(), ConstantesBD.tipoValorInterfazDescripcionFija);
			}
			if(ingresarFijoSec)
			{
				ingresarValoresInterfaz(con, codigo, valorSecundario.getNombre(), ConstantesBD.tipoValorInterfazDescripcionFija);
			}
			if(ingresarLibre)
			{
				ingresarValoresInterfaz(con, codigo, valor.getNombre(), ConstantesBD.tipoValorInterfazLibre);
			}
			return resultadoFinal;
		}
		catch (SQLException e)
		{
			logger.error("Error ingresando el detalle registro de interfaz "+e);
			return ConstantesBD.codigoNuncaValido;
		}
	}
	
	/**
	 * Método para ingresar los valores escritos por el usuario (Libre, default, tamaño fijo, etc)
	 * @param con
	 * @param codigoDetalleRegistroInterfaz
	 * @param valor
	 * @param tipoValor
	 * @return
	 */
	private static int ingresarValoresInterfaz(Connection con, int codigoDetalleRegistroInterfaz, String valor, int tipoValor)
	{
		try
		{
			/*
			 * detalle_registro_interfaz
			 * valor
			 * tipo
			 */
			PreparedStatementDecorator stm= new PreparedStatementDecorator(con.prepareStatement(ingresarValoresInterfazStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			stm.setInt(1, codigoDetalleRegistroInterfaz);
			stm.setString(2, valor);
			stm.setInt(3, tipoValor);
			return stm.executeUpdate();
		}
		catch (SQLException e)
		{
			logger.error("Error ingresando los valores del detalle del registro de interfaz "+e);
			return ConstantesBD.codigoNuncaValido;
		}
	}

	/**
	 * Método para consultar la parametrización del registro de interfaz
	 * @param con
	 * @param codigoRegistro
	 * @return
	 */
	public static HashMap consultarRegistroInterfaz(Connection con, int codigoRegistro)
	{
		try
		{
			PreparedStatementDecorator stm= new PreparedStatementDecorator(con.prepareStatement(consultarRegistroInterfazStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			stm.setInt(1, codigoRegistro);
			HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(stm.executeQuery()));
			stm.close();
			return mapaRetorno;
		}
		catch (SQLException e)
		{
			logger.error("Error consultando parametrización registro interfaz "+e);
			return null;
		}
	}

	/**
	 * Método para consultar el detalle del registro de interfaz
	 * @param con
	 * @param codigoRegistro
	 * @return
	 */
	public static HashMap consultarDetalleRegistroInterfaz(Connection con, int codigoRegistro)
	{
		try
		{
			PreparedStatementDecorator stm= new PreparedStatementDecorator(con.prepareStatement(consultarDetalleRegistroInterfazStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			stm.setInt(1, codigoRegistro);
			HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(stm.executeQuery()));
			stm.close();
			return mapaRetorno;
		}
		catch (SQLException e)
		{
			logger.error("Error consultando el detalle del registro interfaz "+e);
			return null;
		}
	}

	/**
	 * Método para consultar los valores de un detalle especifico de registro de interfaz
	 * @param con
	 * @param codigoDetalle
	 * @return
	 */
	public static HashMap consultarValoresRegistroInterfaz(Connection con, int codigoDetalle)
	{
		try
		{
			PreparedStatementDecorator stm= new PreparedStatementDecorator(con.prepareStatement(consultarValoresInterfazStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			stm.setInt(1, codigoDetalle);
			HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(stm.executeQuery()));
			stm.close();
			return mapaRetorno;
		}
		catch (SQLException e)
		{
			logger.error("Error consultando los valores del registro interfaz "+e);
			return null;
		}
	}
	
	/**
	 * Método que lista los registros de parametrización de interfaz 
	 * @param con
	 * @param codigoInstitucion
	 * @param tipoInterfaz 
	 * @param esOrdenado
	 * @return
	 */
	public static Collection listadoRegistrosInterfaz(Connection con, int codigoInstitucion, int tipoInterfaz,boolean esOrdenado)
	{
		try
		{
			
			String consulta = listadoRegistrosInterfazStr;
			
			//Se verifica el tipo de interfaz
			if(tipoInterfaz!=ConstantesBD.tipoInterfazAmbos)
				consulta += " AND tipo_interfaz= "+tipoInterfaz;
			
			//Se verifica si es ordenado
			if(esOrdenado)
				consulta += " ORDER BY p.tipo_registro ";
			
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1, codigoInstitucion);
			return UtilidadBD.resultSet2Collection(new ResultSetDecorator(pst.executeQuery()));
		}
		catch (SQLException e)
		{
			logger.error("Error listando los registros de parametrización interfaz "+e);
			return null;
		}
	}

}
