/*
 * PostgresqlDevolucionPedidosDao.java 
 * Autor			:  mdiaz
 * Creado el	:  16-sep-2004
 * 
 * Lenguaje		: Java
 * Compilador	: J2SDK 1.4.1_01
 * 
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 * */
package com.princetonsa.dao.postgresql;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.ValoresPorDefecto;

import com.princetonsa.dao.DevolucionPedidosDao;
import com.princetonsa.dao.sqlbase.SqlBaseDevolucionPedidosDao;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.mundo.pedidos.DetalleDevolucionPedidos;
import com.princetonsa.mundo.pedidos.DevolucionPedidos;

/**
 * descripcion de esta clase
 *
 * @version 1.0, 16-sep-2004
 * @author <a href="mailto:miguel@PrincetonSA.com">Miguel Arturo Diaz</a>
 */
public class PostgresqlDevolucionPedidosDao implements DevolucionPedidosDao {


	
  public int insertarDetalle(Connection con, int codigoDevolucion, DetalleDevolucionPedidos detalleDevolucion){
		String insertQuery = "";
		int resultado = 0;
 
		try{
			
			String fechav=UtilidadFecha.conversionFormatoFechaABD(detalleDevolucion.getFechaVencimiento());//,auto_por_subcontratacion//va despues de fecha_vencimiento
			insertQuery = " INSERT INTO  detalle_devol_pedido (codigo, devolucion, pedido, articulo, cantidad,lote,fecha_vencimiento,auto_por_subcontratacion) " +
				"VALUES (nextval('seq_detalle_devol_pedido'), "+codigoDevolucion+", "+detalleDevolucion.getPedido()+", "+detalleDevolucion.getArticulo()+", "+detalleDevolucion.getCantidad()
														  +", "+(UtilidadTexto.isEmpty(detalleDevolucion.getLote())?"null":("'"+detalleDevolucion.getLote()+"'"))+", "+(UtilidadTexto.isEmpty(fechav)?"null":("'"+fechav+"'"))
														  +", '" + ConstantesBD.acronimoNo+"'"
														  +") ";   
			Statement insertSt = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			
			resultado = insertSt.executeUpdate(insertQuery);
			
			
		}
		catch(SQLException e){
			SqlBaseDevolucionPedidosDao.logger.warn("Error insertando el detalle de la devolucion de pedidos en la base de datos [Clase: PostgresqlDevolucionPedidosDao | Tabla: detalle_devol_pedido ]: \n query = \n\n Exception: " +e );
			resultado = 0;
		}

    return resultado;
  }
	

	public int insertar(Connection con, DevolucionPedidos devolucionPedidos) 
	{
		String insertQuery = "";
		PreparedStatementDecorator insert = null;
		int i, resultado = 1;
		
		
		int codigoDevolucion = 0;
		if(UtilidadTexto.isEmpty(devolucionPedidos.getSecuenciaAsignar())){
			codigoDevolucion=UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_devolucion_pedidos");
		}
		else{
			codigoDevolucion = Integer.parseInt(devolucionPedidos.getSecuenciaAsignar());
		}
		
		
		ArrayList detalleDevolucionPedidos = null;
		DetalleDevolucionPedidos detalle = null;
				
		SqlBaseDevolucionPedidosDao.beginTransaction(con);
		
		
		
		// INSERTAMOS LA CABECERA PARA ESTA DEVOLUCION 
		try{
			insertQuery = " INSERT INTO devolucion_pedidos(codigo, motivo, fecha, hora, 	fecha_grabacion, 	hora_grabacion, usuario, estado,observaciones,institucion,es_qx) VALUES ( ";
			con = SqlBaseDevolucionPedidosDao.refreshDBConnection(con);

			insertQuery += codigoDevolucion + ", ";
			insertQuery += "'" + devolucionPedidos.getMotivo() +"', ";
			insertQuery += " '" +UtilidadFecha.conversionFormatoFechaABD(devolucionPedidos.getFechaDevolucion()) + "', "; 
			insertQuery  += " '" +devolucionPedidos.getHoraDevolucion() +"', CURRENT_DATE, "+ValoresPorDefecto.getSentenciaHoraActualBD()+", ";
			insertQuery += "'" +devolucionPedidos.getUsuario() +"', "; 
			insertQuery += ConstantesBD.codigoEstadoDevolucionGenerada +", ";
			if(devolucionPedidos.getObservaciones().equals(""))
				insertQuery += "null, ";
			else
				insertQuery +="'"+ devolucionPedidos.getObservaciones()+"', ";
			insertQuery += devolucionPedidos.getInstitucion() + ",'"+devolucionPedidos.getEsQuirurgico()+"')";
			
			insert =  new PreparedStatementDecorator(con.prepareStatement(insertQuery,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			
			resultado = insert.executeUpdate();
		}
		catch(Exception e){
			SqlBaseDevolucionPedidosDao.abortTransaction(con);
			SqlBaseDevolucionPedidosDao.logger.warn("Error insertando el registro en la base de datos [Clase: PostgresqlDevolucionPedidosDao | Tabla: devolucion_pedido ]: \n query = \n" +insert.toString() + "\n Exception: " +e );
			resultado = 0;
      return resultado;
		}
		//se verifica éxito de la transaccion
		if(resultado>0)
		{
			//se finaliza la transaccion
			// INSERTAMOS TODOS LOS DETALLES DE ESTA DEVOLUCION		
			detalleDevolucionPedidos = devolucionPedidos.getDetalleDevolucionPedidos();
			
			// comprobamos que existan detalles de devolucion de pedido
			if(detalleDevolucionPedidos.size() == 0){
				SqlBaseDevolucionPedidosDao.abortTransaction(con);
				SqlBaseDevolucionPedidosDao.logger.warn("Error insertando el registro en la base de datos: no existen detalles de devoluciones de pedidos [Clase: PostgresqlDevolucionPedidosDao | Tabla: detalle_devolucion_pedido ]");
				resultado = 0;
			  return resultado;
			}
			
			for(i=0; i<detalleDevolucionPedidos.size(); i++){
				detalle = (DetalleDevolucionPedidos) detalleDevolucionPedidos.get(i);
				Log4JManager.info("llama insertadetalle----->"+detalle);
				if(insertarDetalle(con, codigoDevolucion, detalle) == 0){
					SqlBaseDevolucionPedidosDao.abortTransaction(con);
					resultado = 0;
				  return resultado;
				}
			}
			
			SqlBaseDevolucionPedidosDao.endTransaction(con);
			
			/*
			//se incrementa el valor de la secuencia para el proximo registro
			DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).incrementarValorSecuencia(con,"seq_devolucion_pedidos");
			
			//si el valor de la secuencia sigue en 1, se aumenta de nuevo
			try
			{
				int valorSeq = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).obtenerUltimoValorSecuencia(con,"seq_devolucion_pedidos");
				if(valorSeq==1)
					DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).incrementarValorSecuencia(con,"seq_devolucion_pedidos");
			
			}
			catch(SQLException e)
			{
				SqlBaseDevolucionPedidosDao.logger.warn("No se pudo obtener el valor de la secuencia seq_devolucion_pedidos para revision: "+e);
			}
			//----------------------------------------------------------------------------
			*/
			return resultado;
		}
		else
		{
			//se aborta transaccion
			SqlBaseDevolucionPedidosDao.abortTransaction(con);
			SqlBaseDevolucionPedidosDao.logger.warn("No se pudo insertar la devolucion del pedido en insertar de PostgresqlDevolucionPedidosDao ");
			resultado = 0;
			return resultado;
		}
	}

	
	
	
	public int getCodigoDevolucionDisponible(Connection con)
	{
	    int cod;
	    cod = SqlBaseDevolucionPedidosDao.getSequenceLastValue(con, "seq_devolucion_pedidos");
	    if (cod == -1)
	    	return -1;
	    return (cod+1);
	}

	
	
  public String getNombreCentroCosto(Connection con, int codCentroCosto){
		PreparedStatementDecorator busqueda;
		ResultSetDecorator rs = null;
		String selectQuery = "";
		String nombreCentroCosto = "";
			
		
	  try{
			selectQuery = "SELECT getNomCentroCosto(" + codCentroCosto +") as nom_centro_costo ";
			busqueda =  new PreparedStatementDecorator(con.prepareStatement(selectQuery,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			rs = new ResultSetDecorator(busqueda.executeQuery());
		  rs.next();
		  nombreCentroCosto = rs.getString("nom_centro_costo");
	  }
		catch (Exception e){
			SqlBaseDevolucionPedidosDao.logger.warn("Error obteniendo el nombre del centro de costo de la base de datos [Clase: PostgresqlDevolucionPedidosDao | funcion: getNomCentroCosto ] \n Exception: " +e );
		}
    
		return nombreCentroCosto;
  }
	
	
	public ResultSetDecorator buscar(Connection con, String[] selectedColumns, String from, String where, String orderBy) {
		return SqlBaseDevolucionPedidosDao.buscar(con, selectedColumns, from, where, orderBy);
	}
	
	/**
	 * Método que consulta la fecha y hora de un pedido
	 * @param con
	 * @param codigoPedido
	 * @return
	 */
	public String consultarFechaHoraDespacho(Connection con,String codigoPedido)
	{
		return SqlBaseDevolucionPedidosDao.consultarFechaHoraDespacho(con,codigoPedido);
	}
	
	/**
	 * Método implementado para realizar la busqueda avanzada de despachos
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap consultarDespachos(Connection con,HashMap campos)
	{
		return SqlBaseDevolucionPedidosDao.consultarDespachos(con,campos);
	}


	@Override
	public int getSiguienteCodigoDevolucionDisponible(Connection con) {
		
		int cod;
		
		cod = SqlBaseDevolucionPedidosDao.getSequenceNextValue(con, "inventarios.seq_devolucion_pedidos");
		
		if (cod == -1){
			return -1;
		}
			
		return cod;
		
	}
	
	

}
