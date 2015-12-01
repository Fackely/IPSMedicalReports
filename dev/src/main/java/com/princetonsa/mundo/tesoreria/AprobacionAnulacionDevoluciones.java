package com.princetonsa.mundo.tesoreria;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.Utilidades;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.tesoreria.AprobacionAnulacionDevolucionesDao;
import com.princetonsa.decorator.ResultSetDecorator;

public class AprobacionAnulacionDevoluciones 
{

	Logger logger =Logger.getLogger(AprobacionAnulacionDevoluciones.class);
	/**
	 * 
	 */
	private AprobacionAnulacionDevolucionesDao objetoDao;
	
	
	/**
	 * 
	 *
	 */
	public AprobacionAnulacionDevoluciones() 
	{
		init(System.getProperty("TIPOBD"));
	}
	
	
	
	/**
	 * Inicializa el acceso a la base de datos de este objeto, obteniendo su respectivo DAO.
	 * param tipoBD el tipo de bases de datos que va a usar este objeto.
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores validos para tipoBD.
	 * son los nombres y constantes definidos en <code>DaoFactory</code>
	 * @return <b>true</b> si la inicializacion fue exitosa, <code>false</code> si no.
	 */
	
	private boolean init(String tipoBD) 
	{
		if(objetoDao==null)
		{
			DaoFactory myFactory=DaoFactory.getDaoFactory(tipoBD);
			objetoDao=myFactory.getAprobacionAnulacionDevolucionesDao();
			if(objetoDao!=null)
				return true;
		}
		return false;
			
		
	}
	
	
	/**
	 * 
	 * @param con
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param devolInicial
	 * @param devolFinal
	 * @param motivoDevolucion
	 * @param tipoId
	 * @param numeroId
	 * @param centroAtencion
	 * @param caja
	 * @param reciboCaja 
	 * @return
	 */
	public HashMap BusquedaDevoluciones(Connection con, String fechaInicial, String fechaFinal, String devolInicial, String devolFinal, String motivoDevolucion, String tipoId, String numeroId, String centroAtencion, String caja, String reciboCaja) 
	{
		return  objetoDao.BusquedaDevoluciones(con, fechaInicial, fechaFinal, devolInicial, devolFinal, motivoDevolucion, tipoId, numeroId, centroAtencion, caja, reciboCaja);
	}


	/**
	 * 
	 * @param con
	 * @param devolucion
	 * @return
	 */
	public HashMap consultaDetalleDevolucion(Connection con, int devolucion) 
	{
		return  objetoDao.ConsultaDetalleDevolucion(con, devolucion);
	}

	/**
	 * 
	 * @param con
	 * @param recibo
	 * @return
	 */
	public String consultaEstadoRecibo(Connection con, int recibo) 
	{
		return  objetoDao.ConsultaEstadoRecibo(con, recibo);
	}

	/**
	 * 
	 * @param con
	 * @param codigoDevol
	 * @return
	 */
	public String consultaTipoConcepto(Connection con, int codigoDevol)
	{
		return  objetoDao.consultaTipoConcepto(con, codigoDevol);
	}

	/**
	 * 
	 * @param con
	 * @param documento
	 * @return
	 */
	public String consultaPagoConvenio(Connection con, int documento)
	{
		return  objetoDao.consultaPagoConvenio(con, documento);
	}

	/**
	 * 
	 * @param con
	 * @param codigoDevol
	 * @param estadoDevolucion
	 * @param loginUsuario
	 * @param motivoAnulacion
	 * @param valor
	 * @return
	 */
	public int insertarDevolucion(Connection con, int codigoDevol, String estadoDevolucion, String loginUsuario, String motivoAnulacion, double valor, long turnoAfecta) 
	{
		return  objetoDao.insertarDevolucion(con, codigoDevol, estadoDevolucion, loginUsuario, motivoAnulacion, valor, turnoAfecta);
	}
	
	/**
	 * 
	 * @param con
	 * @param devolucion
	 * @return
	 */
	public HashMap consultaDetalleDevolucionConsecutivo(Connection con, int devolucion) 
	{
		return  objetoDao.consultaDetalleDevolucionConsecutivo(con, devolucion);
	}
	
	
	
	/**
	 * 
	 * @param con
	 * @param numeroReciboCaja
	 * @return
	 */
	public int esReciboCajaXConceptAnticipConvenioOdonto(Connection con,String numeroReciboCaja) {
		
		
		int codContrato = 0;
    	ResultSetDecorator rs1=null;
    	try{
    	   rs1=objetoDao.esReciboCajaXConceptAnticipConvenioOdonto(con,numeroReciboCaja );
        
    	   if (rs1.next())  
    	   {
    		   codContrato=rs1.getInt("codcontrato");    		   
    	   }
    	  
    	}catch(SQLException e) {
    		logger.error("Error realizando Validacion Valor Recibo Caja [AnulacionRecibosCaja.java]");
            e.printStackTrace();
    	}
		
		return codContrato;
	}



	 public HashMap validarValorReciboCajaVsValorAnticipoDispContr(Connection con, int numContrato ,String valorReciboCaja )
    {
    	boolean esMenor = false;
    	HashMap mapa= new HashMap();
    	ResultSetDecorator rs1=null;
    	mapa.put("validacion", false);
    	mapa.put("valorAntConv", ConstantesBD.codigoNuncaValidoDouble);
    	
    	try{
    	   rs1=obtenerValorAnticipoDipoContratConveRecibCaja(con, numContrato);
        
    	   if (rs1.next())  
    	      if(rs1.getDouble("valor") > Utilidades.convertirADouble(valorReciboCaja))
               {  
    	    	mapa.put("validacion", true);
    	      	mapa.put("valorAntConv", rs1.getDouble("valorantconv")); 
        	   
               }
    	  
    	}catch(SQLException e) {
    		logger.error("Error realizando Validacion Valor Recibo Caja [AnulacionRecibosCaja.java]");
            e.printStackTrace();
    	}
       
    	return mapa;
    }
	
	 
	 
	 private ResultSetDecorator obtenerValorAnticipoDipoContratConveRecibCaja(Connection con,int  numContrato) {
			
			return objetoDao.obtenerValorAnticipoDipoContratConveRecibCaja(con, numContrato);
		}
	 
	
	
	 public  ResultSetDecorator consultarValorConceptosRecibosCaja(Connection con, String numReciboCaja, int codInstitucion)
	 {
		 return objetoDao.consultarValorConceptosRecibosCaja(con, numReciboCaja, codInstitucion);
	 }



	public boolean actualizarValorAnticipo(Connection con,int numContratoOdontologia, double nuevoValorAnticipoRecConv, String usuario) {
		
		return objetoDao.actualizarValorAnticipo(con, numContratoOdontologia, nuevoValorAnticipoRecConv, usuario);
	}
	
	
	
	
	

}
