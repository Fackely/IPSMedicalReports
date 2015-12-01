/*
 * @(#)NotasGeneralesEnfermeria.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_04
 *
 */

package com.princetonsa.mundo.ordenesmedicas.cirugias;


import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import util.ConstantesBD;
import util.UtilidadBD;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.NotasGeneralesEnfermeriaDao;
import com.servinte.axioma.dto.salascirugia.NotaEnfermeriaDto;
import com.servinte.axioma.fwk.exception.BDException;

/**
 * Objeto que maneja la interacción entre la capa
 * de control y el acceso a la información de las notas generales de enfermeria 
 * 
 * @author <a href="mailto:cperalta@PrincetonSA.com">Carlos Peralta</a>
 *	@version 1.0, 03 /Nov/ 2005
 */
public class NotasGeneralesEnfermeria
{
	
	/**
	 * Para hacer logs de debug / warn / error de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(NotasGeneralesEnfermeria.class);
	
	/**
	 * Mapa con las notas 
	 */
	private HashMap mapaNotasGeneralesEnfermeria;
    
    /**
     * registro seleccionado
     */
    private int regSeleccionado;
    
    /**
     * institucion del usuario
     */
    private int institucion;
    
	/**
	 * Numero de la solicitud asociado a la nota
	 */	
    private int numeroSolicitud;
    
	/**
     * Constructor del objeto (Solo inicializa el acceso a la fuente de datos)
     */
    public NotasGeneralesEnfermeria()
    {
        this.init(System.getProperty("TIPOBD"));
    }
    
    /**
	 * El DAO usado por el objeto <code>NotasGeneralesEnfermeriaDao</code> 
	 * para acceder a la fuente de datos. 
	 */
	private NotasGeneralesEnfermeriaDao notasGeneralesEnfermeriaDao ;

	/**
     * Metodo para inicializar los atributos de la clase.
     *
     */
    public void reset ()
    {
     this.institucion=ConstantesBD.codigoNuncaValido;
     this.regSeleccionado=ConstantesBD.codigoNuncaValido; 
     this.numeroSolicitud=ConstantesBD.codigoNuncaValido;
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
			notasGeneralesEnfermeriaDao = myFactory.getNotasGeneralesEnfermeriaDao();
			wasInited = (notasGeneralesEnfermeriaDao != null);
		}

		return wasInited;
	}
	
	
	
	
    
    /**
     * @return Retorna regSeleccionado.
     */
    public int getRegSeleccionado() 
    {
        return regSeleccionado;
    }
    /**
     * @param regSeleccionado Asigna regSeleccionado.
     */
    public void setRegSeleccionado(int regSeleccionado) 
    {
        this.regSeleccionado = regSeleccionado;
    }
    /**
     * @return Retorna institucion.
     */
    public int getInstitucion() 
    {
        return institucion;
    }
    /**
     * @param institucion Asigna institucion.
     */
    public void setInstitucion(int institucion) 
    {
        this.institucion = institucion;
    }   
    
	/**
	 * @return Returns the numeroSolicitud.
	 */
	public int getNumeroSolicitud()
	{
		return numeroSolicitud;
	}
	/**
	 * @param numeroSolicitud The numeroSolicitud to set.
	 */
	public void setNumeroSolicitud(int numeroSolicitud)
	{
		this.numeroSolicitud=numeroSolicitud;
	}
	
	
	
	
    /**
	 * @return Returns the apaNotasGeneralesEnfermeria.
	 */
	public HashMap getMapaNotasGeneralesEnfermeria()
	{
		return mapaNotasGeneralesEnfermeria;
	}
	
	/**
	 * @param mapaNotasGeneralesEnfermeria The mapaNotasGeneralesEnfermeria to set.
	 */
	public void setMapaNotasGeneralesEnfermeria(HashMap mapaNotasGeneralesEnfermeria)
	{
		this.mapaNotasGeneralesEnfermeria= mapaNotasGeneralesEnfermeria;
	}
	
	/**
	 * @param key
	 * @param value
	 */
	public void setMapaNotasGeneralesEnfermeria(String key, Object value) 
	{
		mapaNotasGeneralesEnfermeria.put(key, value);
	}
	/**
	 * @param key
	 * @return
	 */
	public Object getMapaNotasGeneralesEnfermeria(String key) 
	{
		return mapaNotasGeneralesEnfermeria.get(key);
	}
	
	/**
	 * Método que carga las peticiones asociadas al paciente cargado en session
	 * @param con
	 * @param codigoPaciente
	 * @param institucionPaciente
	 * @return
	 * @throws SQLException
	 */
	public HashMap cargarNotasPaciente (Connection con, int numeroSolicitud) throws SQLException
	{
		notasGeneralesEnfermeriaDao= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getNotasGeneralesEnfermeriaDao();
		try
		{
		    String[] colums={"codigo", "numeroSolicitud", "fechaHoraNota", "nota", "enfermera"};
		    this.mapaNotasGeneralesEnfermeria=UtilidadBD.resultSet2HashMap(colums, notasGeneralesEnfermeriaDao.cargarNotasPaciente(con, numeroSolicitud) , true, true).getMapa();
		}
		catch(Exception e)
		{
			logger.warn("Error mundo Notas generales de enfermeria en  la consulta de las notas " +e.toString());
			mapaNotasGeneralesEnfermeria=null;
		}
		return mapaNotasGeneralesEnfermeria;
	}
    
	/**
	 * Método para insertar una nota
	 * @param con
	 * @param numeroSolicitud
	 * @param fechaNota
	 * @param horaNota
	 * @param nota
	 * @param codigoEnfermera
	 * @param institucion
	 * @param fechaGrabacion
	 * @param horaGrabacion
	 * @return
	 */
	public int insertarNota(Connection con, int numeroSolicitud, String fechaNota, String horaNota, String nota, int codigoEnfermera, int institucion, String fechaGrabacion, String horaGrabacion)
	{
	 	DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
	    int resp1=0;
	    try
	    {
			if (notasGeneralesEnfermeriaDao==null)
			{
				throw new SQLException ("No se pudo inicializar la conexión con la fuente de datos (notasGeneralesEnfermeriaDao - insertar )");
			}
			/**Iniciamos la transacción, si el estado es empezar***/
			boolean inicioTrans;
			inicioTrans=myFactory.beginTransaction(con);
			resp1=notasGeneralesEnfermeriaDao.insertarNota(con, numeroSolicitud, fechaNota, horaNota, nota, codigoEnfermera, institucion, fechaGrabacion, horaGrabacion);
			if (!inicioTrans||resp1<1  )
			{
			    myFactory.abortTransaction(con);
				return -1;
			}
			else
			{
			    myFactory.endTransaction(con);
			}
	    }	
		catch (SQLException e) 
		{
		    resp1=0;
        }	
		return resp1;
	}
	
	/**
	 * Metodo que carga una lista de Notas enfermeria a partir de un numero de Solicitud dado
	 * @param con
	 * @param numeroSolicitud
	 * @return listaNotasEnfermeria
	 * @throws BDException
	 * @author Oscar Pulido
	 * @created 02/07/2013
	 */
	public List<NotaEnfermeriaDto> listaNotasEnfermeria(Connection con,int numeroSolicitud, boolean esAscendente) throws BDException{
		
		List<NotaEnfermeriaDto>listaNotasEnfermeria=new ArrayList<NotaEnfermeriaDto>();
		listaNotasEnfermeria = notasGeneralesEnfermeriaDao.listaNotasEnfermeria(con, numeroSolicitud, esAscendente);
		return listaNotasEnfermeria;
	}
	
	/**
	 * Permite persistir la informacion de una nota enfermeria
	 * @param con
	 * @param dto NotaEnfermeriaDto
	 * @throws BDException
	 * @created 03/07/2013
	 */
	public void guardarNotaEnfermeria(Connection con, NotaEnfermeriaDto dto)throws BDException{
		notasGeneralesEnfermeriaDao.guardarNotaEnfermeria(con, dto);
	}
}