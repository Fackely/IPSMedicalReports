/*
 * Creado el Jun 1, 2006
 * por Julian Montoya
 */
package com.princetonsa.mundo.historiaClinica;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import util.ConstantesBD;
import util.LogsAxioma;
import util.UtilidadCadena;
import util.UtilidadTexto;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.historiaClinica.SignosSintomasDao;

public class SignosSintomas {

	
	/**
	 * Interfaz para acceder a la fuente de datos
	 */
	private SignosSintomasDao  signosSintomasDao = null;
	
	/**
	 * Mapa para guardar toda las descripciones de los signos y los sintomas.
	 */
	private HashMap mapa;

	/**
     * Variable para contabilizar el numero de registros nuevos  
     */
    private int nroRegistros;
    
	
	/**
	 * Constructor de la clase, inicializa en vacío todos los atributos
	 */
	public SignosSintomas ()
	{
		reset();
		this.init(System.getProperty("TIPOBD"));
	}

	/**
	 * Este método inicializa los atributos de la clase con valores vacíos
	 */
	public void reset()
	{
		this.mapa = new HashMap();
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
			signosSintomasDao = myFactory.getSignosSintomasDao();
			wasInited = (signosSintomasDao != null);
		}
		return wasInited;
	}

	/**
	 * Metodo para cargar la informacion de los signos y sintomas.
	 * @param con
	 * @param institucion 
	 * @return
	 */
	public HashMap cargarSignosSintomas(Connection con, int institucion)
	{
		return signosSintomasDao.cargarSignosSintomas(con, institucion); 
	}

	/**
	 * Metodo para insertar los nuevos metodos.
	 * @param con
	 * @param codigoInstitucion 
	 * @param loginUsuario 
	 * @return
	 * @throws SQLException 
	 */
	public int insertar(Connection con, int codigoInstitucion, String loginUsuario) throws SQLException
	{
		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));

		int  resp1=0;
			
		if (signosSintomasDao==null)
		{
			throw new SQLException ("No se pudo inicializar la conexión con la fuente de datos (signosSintomasDao - insertar)");
		}
		
		//Iniciamos la transacción, si el estado es empezar
		boolean inicioTrans;
		inicioTrans=myFactory.beginTransaction(con);

		
		//-----Validando cambios de los registrados anteriormente.
		int nroReg = 0;
		if ( UtilidadCadena.noEsVacio(this.mapa.get("numRegistros")+"") ) { nroReg = Integer.parseInt(this.mapa.get("numRegistros")+""); }

		for (int i = 0; i < nroReg; i++)
		{
		 	int consecutivo = ConstantesBD.codigoNuncaValido;
		 	if(!UtilidadTexto.isEmpty(this.getMapa("consecutivo_" + i)+""))
		 		consecutivo = Integer.parseInt(this.getMapa("consecutivo_" + i)+"");  

			String codigo = this.getMapa("codigo_" + i)+"".trim();  
			String h_codigo = this.getMapa("h_codigo_" + i)+"".trim();  
			String descripcion = this.getMapa("signo_" + i)+"".trim(); 
			String h_descripcion = this.getMapa("h_signo_" + i)+"".trim();
			
			if ( !codigo.equals(h_codigo) || !descripcion.equals(h_descripcion))
			{
				  StringBuffer log=new StringBuffer();
				  log.append("\n=========================MODIFICACIÓN SIGNOS Y SINTOMAS ================");
				  log.append("\n CODIGO SIGNO/SINTOMA ANTERIORES 		 :" + h_codigo);
				  log.append("\n DESCRIPCÍON SIGNO/SINTOMA ANTERIORES	 :" + h_descripcion);
				  log.append("\n OBSERVACIONES ANTERIORES 				 :" + codigo);
				  log.append("\n OBSERVACIONES NUEVAS 					 :" + descripcion);
				  log.append("\n=========================================================================\n");
				  //-Generar el log.
				  LogsAxioma.enviarLog(ConstantesBD.logSignosSintomasCodigo, log.toString(), ConstantesBD.tipoRegistroLogModificacion, loginUsuario);

				  //--Se modifica el registro.	
				  resp1=signosSintomasDao.insertar(con, consecutivo, codigo, descripcion, codigoInstitucion);
				  if (resp1 <= 0)	{ break; }
			}
		}

		//---Barrer el mapa con los nuevos signos y sintomas. 
		for (int i = 0; i <= this.nroRegistros; i++)
		{
			String codigo = this.getMapa("codigoN_" + i)+"".trim();  
			String descripcion = this.getMapa("signoN_" + i)+"".trim(); 
			if ( UtilidadCadena.noEsVacio(codigo+"") && UtilidadCadena.noEsVacio(descripcion) )
			{
				resp1=signosSintomasDao.insertar(con, -1, codigo, descripcion, codigoInstitucion);
				if (resp1 <= 0)	{ break; }
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
	 * Metodo para eliminar un sintoma o diagnostico.
	 * @param con
	 * @param nroRegEliminar
	 * @param loginUsuario 
	 * @return
	 * @throws SQLException 
	 */
	public int eliminar(Connection con, int nroRegEliminar, String loginUsuario) throws SQLException
	{
		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));

		int  resp1=0;
			
		if (signosSintomasDao==null)
		{
			throw new SQLException ("No se pudo inicializar la conexión con la fuente de datos (signosSintomasDao - insertar)");
		}
		
		//Iniciamos la transacción, si el estado es empezar
		boolean inicioTrans;
		inicioTrans=myFactory.beginTransaction(con);
		
		//-eliminar el sintoma o signo. 
		resp1=signosSintomasDao.eliminar(con, nroRegEliminar);
		
		if (resp1>0)
		{
			  String indice = this.mapa.get("indiceEliminado") +  "";
			  String codigo = this.mapa.get("codigo_"+indice) +  "";
			  String nombre = this.mapa.get("signo_"+indice) +  "";
			  StringBuffer log=new StringBuffer();
			  log.append("\n===================ELIMINACIÓN SIGNOS Y SINTOMAS ================");
			  log.append("\n CODIGO SIGNO/SINTOMA  		 :" + codigo);
			  log.append("\n DESCRIPCÍON SIGNO/SINTOMA   :" + nombre);
			  log.append("\n=========================================================================\n");
			
			  //-Generar el log 
			  LogsAxioma.enviarLog(ConstantesBD.logSignosSintomasCodigo, log.toString(), ConstantesBD.tipoRegistroLogEliminacion, loginUsuario);
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
	 * @return Retorna mapa
	 */
	public HashMap getMapa() {
		return mapa;
	}
	/**
	 * @param Asigna mapa
	 */
	public void setMapa(HashMap mapaAux) {
		this.mapa = mapaAux;
	}
	/**
	 * @return Retorna mapa
	 */
	public Object getMapa(Object key) {
		return mapa.get(key);
	}
	/**
	 * @param Asigna dato.
	 */
	public void setMapa(Object key, Object dato) {
		this.mapa.put(key, dato);
	}

	/**
	 * @return Retorna nroRegistros.
	 */
	public int getNroRegistros() {
		return nroRegistros;
	}

	/**
	 * @param Asigna nroRegistros.
	 */
	public void setNroRegistros(int nroRegistros) {
		this.nroRegistros = nroRegistros;
	}



}
