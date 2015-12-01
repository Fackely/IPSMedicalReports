package com.princetonsa.mundo.capitacion;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.LogsAxioma;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadTexto;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.capitacion.UnidadPagoDao;


public class UnidadPago {

	/**
	 * Para hacer logs de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(UnidadPago.class);
	
	/**
	 * Interfaz para acceder a la fuente de datos
	 */
	private UnidadPagoDao  unidadPagoDao = null;

	
	/**
	 * Mapa para el manejo de las consultas
	 */
	private HashMap mapa;
	
	/**
	 * Limpiar la informacion del mundo
	 *
	 */
	void clean()
	{
		this.mapa = new HashMap();
	}
	
	/**
	 * Constructor de la clase, inicializa en vacío todos los atributos
	 */
	public UnidadPago ()
	{
		this.init(System.getProperty("TIPOBD"));
	}
	
	/**
	 * Inicializa el acceso a bases de datos de este objeto, obteniendo su respectivo DAO.
	 * @param tipoBD el tipo de base de datos que va a usar este objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 * @return <b>true</b> si la inicialización fue exitosa, <code>false</code> si no.
	 */
	public boolean init(String tipoBD)
	{
		boolean wasInited = false;
		DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);

		if (myFactory != null)
		{
			unidadPagoDao = myFactory.getUnidadPagoDao();
			wasInited = (unidadPagoDao != null);
		}
		return wasInited;
	}
	
	
	/**
	 * Funcion para cosultar informacion 
	 * @param con
	 * @param mapaParam
	 * @return
	 * @throws SQLException 
	 */
	public HashMap consultarInformacion(Connection con, HashMap mapaParam) throws SQLException
	{
		if (unidadPagoDao==null)
		{
			throw new SQLException ("No se pudo inicializar la conexión con la fuente de datos (AntecedentesVacunas - AntecedentesVacunasDao )");
		}
		
		logger.info("\n\n nroConsulta   [" + mapaParam.get("nroConsulta") +"]  \n\n");

		if ( (mapaParam.get("nroConsulta")+"").equals("1") )
		{
			//--Quitar los ceros .... Ej: 15.00 --> 15
			HashMap mp = new HashMap();
			int num = 0;
			String val = "", val1 = "";
			int numRows = 0;
			
			mp = unidadPagoDao.consultarInformacion(con, mapaParam);

			if ( UtilidadCadena.noEsVacio(mp.get("numRegistros")+"") )
			{
				numRows = Integer.parseInt(mp.get("numRegistros")+"");
			}
			
			for (int i = 0; i < numRows; i++)
			{
				val = mp.get("valor_" + i)+"";
				if (val.indexOf(".") != -1)
				{
					val1 = val.substring(val.indexOf(".")+1, val.length());
					num = Integer.parseInt(val1);
					if ( num == 0 ) 
					{
						 mp.put("valor_" + i, val.substring(0,val.indexOf(".")) );
						 mp.put("h_valor_" + i, val.substring(0,val.indexOf(".")) );
					}
				}
			}
			
			return mp;
		}
		else
		{
			return unidadPagoDao.consultarInformacion(con, mapaParam);
		}
		
	}

	
	/**
	 * Metodo para inserta o modificar los valores.
	 * @param con
	 * @param loginUsuario
	 * @return
	 * @throws SQLException 
	 */
	public int insertar(Connection con, String loginUsuario) throws SQLException
	{
		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));

		int  resp1=0;
			
		if (unidadPagoDao==null)
		{
			throw new SQLException ("No se pudo inicializar la conexión con la fuente de datos (unidadPagoDao - insertar)"); 
		}
		//Iniciamos la transacción, si el estado es empezar
		boolean inicioTrans;
		
		inicioTrans=myFactory.beginTransaction(con);
		
		//-- Barrer el MAPA que contiene la Informacion Nueva.
		int nroRows = 0;
		if ( UtilidadCadena.noEsVacio(mapa.get("nroRegistrosNv")+"") )
		{
			nroRows = Integer.parseInt(mapa.get("nroRegistrosNv")+"");
			
			for (int i=0; i<nroRows; i++)
			{
				String fi  =  UtilidadFecha.conversionFormatoFechaABD(this.mapa.get("fi_nv_" + i) + "");  
				String ff  =  UtilidadFecha.conversionFormatoFechaABD(this.mapa.get("ff_nv_" + i) + "");  
				String valor  =  this.mapa.get("valor_nv_" + i) + "";
				
				logger.info("\n\n Los Nuevos FI [" + fi + "] ff [" + ff + "] valor [" + valor + "]  \n\n");
				resp1=unidadPagoDao.insertar(con, 0, -1, fi, ff, valor); //-El cero es para que inserte.
				if (resp1 < 1) { break; }
				
			}
		}	

		//-- Verificar si hubo cambios en la informacion ya registrada.  
		nroRows = 0;
		if ( UtilidadCadena.noEsVacio(mapa.get("numRegistros")+"") )
		{
			nroRows = Integer.parseInt(mapa.get("numRegistros")+"");
			
			for (int i=0; i<nroRows; i++)
			{
				int codigo =  Integer.parseInt(this.mapa.get("codigo_" + i) + "");  
				String fi  =  UtilidadFecha.conversionFormatoFechaABD(this.mapa.get("fi_" + i) + "");  
				String ff  =  UtilidadFecha.conversionFormatoFechaABD(this.mapa.get("ff_" + i) + "");  
				String valor  =  this.mapa.get("valor_" + i) + "";
				
				
				String h_fi  =  UtilidadFecha.conversionFormatoFechaABD(this.mapa.get("h_fi_" + i) + "");  
				String h_ff  =  UtilidadFecha.conversionFormatoFechaABD(this.mapa.get("h_ff_" + i) + "");  
				String h_valor  =  this.mapa.get("h_valor_" + i) + "";
				
				//-- Verificar que se Haya modificado  el regitro.   
				if ( !fi.equals(h_fi) || !ff.equals(h_ff) || !valor.equals(h_valor) ) 
				{
					logger.info("\n\n  Entro a modificar ff [" + ff +"] fi [" + fi + "] valor [" + valor + "] \n\n");
					
					resp1 = unidadPagoDao.insertar(con, 1, codigo, fi, ff, valor); //-El cero es para que inserte.
					if (resp1 < 1) { break; }
					
					//-- Si se modifico la tabla se debe generar el Log.	
					if ( resp1 > 0 )
					{
					   String log =  "\n====================== MODIFICACIÓN DE UNA UNIDAD DE PAGO CAPITACIÓN ==================================";
						  	  log += "\n CODIGO                    : " + codigo; 
							  	 if ( !fi.equals(h_fi) )
							  	 {
								  	  log += "\n FECHA INICIO ANTERIOR     : " + UtilidadFecha.conversionFormatoFechaAAp(h_fi); 
								  	  log += "\n FECHA INICIO NUEVA        : " + UtilidadFecha.conversionFormatoFechaAAp(fi); 
							  	 }
							  	 if ( !ff.equals(h_ff) )
							  	 {
								  	  log += "\n FECHA FINAL ANTERIOR      : " + UtilidadFecha.conversionFormatoFechaAAp(h_ff); 
								  	  log += "\n FECHA FINAL NUEVA         : " + UtilidadFecha.conversionFormatoFechaAAp(ff); 
							  	 }
							  	 if ( !valor.equals(h_valor) )
							  	 {
								  	  log += "\n VALOR ANTERIOR            : " + h_valor; 
								  	  log += "\n VALOR NUEVO               : " + valor; 
							  	 }
						      log += "\n=======================================================================================================\n";
						
						//-Generar el log 
						LogsAxioma.enviarLog(ConstantesBD.logUnidadPagoCapitacionCodigo, log, ConstantesBD.tipoRegistroLogModificacion, loginUsuario);
					}
					
					
					if (resp1 < 1) { break; }
				}
			}
		}	
		
		
		
		if (!inicioTrans||resp1<1)
		{
		    myFactory.abortTransaction(con);
			return -1;
		}
		else
		{
		    myFactory.endTransaction(con);
		}
		return resp1;	
	}


	public int eliminar(Connection con, String loginUsuario) throws SQLException
	{
		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));

		int  resp1=0;
			
		if (unidadPagoDao==null)
		{
			throw new SQLException ("No se pudo inicializar la conexión con la fuente de datos (unidadPagoDao - insertar)"); 
		}
		//Iniciamos la transacción, si el estado es empezar
		boolean inicioTrans;
		
		inicioTrans=myFactory.beginTransaction(con);
		
			//-- Barrer el MAPA que contiene la Informacion Nueva.
			if ( UtilidadCadena.noEsVacio(mapa.get("nroRegEliminar")+"") && UtilidadCadena.noEsVacio(mapa.get("posNroRegEliminar")+"") )
			{
				int nroEliminar = Integer.parseInt(mapa.get("nroRegEliminar")+"");
				int nroPosMapa = Integer.parseInt(mapa.get("posNroRegEliminar")+"");
				
				resp1=unidadPagoDao.insertar(con, 2, nroEliminar, "", "", ""); //-El 2 es para eliminar.
				
				//--Registrar en el LOG
				if (resp1 > 0)
				{
					String log = "\n================================ELIMINACION DE UNA UNIDAD DE PAGO CAPITACIÓN====================================";
						   log += "\n CODIGO        : " + mapa.get("codigo_" + nroPosMapa); 
						   log += "\n FECHA INICIO  : " + UtilidadFecha.conversionFormatoFechaAAp(mapa.get("h_fi_" + nroPosMapa)+""); 
						   log += "\n FECHA FIN     : " + UtilidadFecha.conversionFormatoFechaAAp(mapa.get("h_ff_" + nroPosMapa)+""); 
						   log += "\n VALOR         : " + mapa.get("h_valor_" + nroPosMapa); 
						   log += "\n================================================================================================================";
						   
					//logger.info("\n\n  Entro a generar el LOG  [" + log + "]  \n\n");	   
					
					//-- Generar el log. 
					LogsAxioma.enviarLog(ConstantesBD.logUnidadPagoCapitacionCodigo, log, ConstantesBD.tipoRegistroLogEliminacion, loginUsuario);
				}
			}	

		if (!inicioTrans||resp1<1)
		{
		    myFactory.abortTransaction(con);
			return -1;
		}
		else
		{
		    myFactory.endTransaction(con);
		}
		return resp1;	
	}
	
	/**
	 * @return Retorna mapa.
	 */
	public HashMap getMapa() {
		return mapa;
	}

	/**
	 * @param Asigna mapa.
	 */
	public void setMapa(HashMap mapa) {
		this.mapa = mapa;
	}

	/**
	 * @return Retorna mapa.
	 */
	public Object getMapa(String key)
	{
		return mapa.get(key);
	}

	/**
	 * @param Asigna mapa.
	 */
	public void setMapa(String key, Object obj) 
	{
		this.mapa.put(key, obj);
	}



}
