/*
 * @(#)ResponderCirugias.java
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
import java.util.HashMap;
import org.apache.log4j.Logger;
import util.ConstantesBD;
import util.UtilidadBD;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.ResponderCirugiasDao;

/**
 * Objeto que maneja la interacción entre la capa
 * de control y el acceso a la información de la 
 * Respuesta de las Cirugias
 * @author <a href="mailto:cperalta@PrincetonSA.com">Carlos Peralta</a>
 *	@version 1.0, 02 /Nov/ 2005
 */
public class ResponderCirugias
{
	
	
	/**
	 * Para hacer logs de debug / warn / error de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(ResponderCirugias.class);
	
	/**
	 * Mapa con las peticiones en el flujo por paciente
	 */
	private HashMap mapaPeticionesPaciente;
    
	/**
	 * Mapa con las peticiones en el flujo por medico
	 */
	private HashMap mapaPeticionesMedico;
    /**
     * registro seleccionado
     */
    private int regSeleccionado;
    
    /**
     * institucion del usuario
     */
    private int institucion;
    
	/**
     * Constructor del objeto (Solo inicializa el acceso a la fuente de datos)
     */
    public ResponderCirugias()
    {
        this.init(System.getProperty("TIPOBD"));
    }
    
    /**
	 * El DAO usado por el objeto <code>ResponderCirugiasDao</code> 
	 * para acceder a la fuente de datos. 
	 */
	private ResponderCirugiasDao responderCirugiasDao ;

	/**
     * Metodo para inicializar los atributos de la clase.
     *
     */
    public void reset ()
    {
     this.institucion=ConstantesBD.codigoNuncaValido;
     this.regSeleccionado=ConstantesBD.codigoNuncaValido;    
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
			responderCirugiasDao = myFactory.getResponderCirugiasDao();
			wasInited = (responderCirugiasDao != null);
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
	 * @return Returns the mapaPeticionesPaciente.
	 */
	public HashMap getMapaPeticionesPaciente()
	{
		return mapaPeticionesPaciente;
	}
	
	/**
	 * @param mapaFacturasPaciente The mapaPeticionesPaciente to set.
	 */
	public void setMapaPeticionesPaciente(HashMap mapaPeticionesPaciente)
	{
		this.mapaPeticionesPaciente= mapaPeticionesPaciente;
	}
	
	/**
	 * @param key
	 * @param value
	 */
	public void setMapaPeticionesPaciente(String key, Object value) 
	{
		mapaPeticionesPaciente.put(key, value);
	}
	/**
	 * @param key
	 * @return
	 */
	public Object getMapaPeticionesPaciente(String key) 
	{
		return mapaPeticionesPaciente.get(key);
	}
	
	/**
	 * Método que carga las peticiones asociadas al paciente cargado en session
	 * @param con
	 * @param codigoPaciente
	 * @param institucionPaciente
	 * @return
	 * @throws SQLException
	 */
	public HashMap cargarPeticionesPaciente (Connection con, int codigoPaciente, int institucionPaciente, int idIngreso, int consecutivoOrdenesMedicas) throws SQLException
	{
		responderCirugiasDao= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getResponderCirugiasDao();
		try
		{
		    String[] colums={"codigoPeticion", "fechaCirugia", "consecutivoOrdenes","estadoMedico", "numeroSolicitud", "solicitante", "especialidad","pyp","justificacionsolicitud"};
		    this.mapaPeticionesPaciente=UtilidadBD.resultSet2HashMap(colums, responderCirugiasDao.cargarPeticionesPaciente(con, codigoPaciente, institucionPaciente, idIngreso, consecutivoOrdenesMedicas)  , true, true).getMapa();
		    this.consultarDetallePeticionPaciente(con);
		}
		catch(Exception e)
		{
			logger.warn("Error mundo Consulta de Peticiones Por Paciente" +e.toString());
			mapaPeticionesPaciente=null;
		}
		return mapaPeticionesPaciente;
	}
	
	
	/**
	 * Método que carga las peticiones asociadas al paciente cargado en session
	 * con peticion 
	 * @param con
	 * @param codigoPaciente
	 * @param institucionPaciente
	 * @return
	 * @throws SQLException
	 */
	public HashMap cargarPeticionesPacienteConPeticion (Connection con, int codigoPaciente, int institucionPaciente, int idCuenta, int consecutivoOrdenesMedicas) throws SQLException
	{
		responderCirugiasDao= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getResponderCirugiasDao();
		try
		{
		    String[] colums={"codigoPeticion", "fechaCirugia", "consecutivoOrdenes","estadoMedico", "numeroSolicitud", "solicitante", "especialidad","pyp","justificacionsolicitud"};
		    this.mapaPeticionesPaciente=UtilidadBD.resultSet2HashMap(colums, responderCirugiasDao.cargarPeticionesPacienteConOrden(con, codigoPaciente, institucionPaciente, idCuenta, consecutivoOrdenesMedicas)  , true, true).getMapa();
		    this.consultarDetallePeticionPaciente(con);
		}
		catch(Exception e)
		{
			logger.warn("Error mundo Consulta de Peticiones Por Paciente" +e.toString());
			mapaPeticionesPaciente=null;
		}
		return mapaPeticionesPaciente;
	}
    
	
	/**
	 * Método que carga las peticiones asociadas al médico que entró en el sistema
	 * @param con
	 * @param codigoPaciente
	 * @param institucionPaciente
	 * @return
	 * @throws SQLException
	 */
	public HashMap cargarPeticionesMedico (Connection con, String usuario, int institucionMedico, int centroCosto, int centroAtencion) throws SQLException
	{
		responderCirugiasDao= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getResponderCirugiasDao();
		try
		{
		    String[] colums={"codigoPeticion", "fechaCirugia", "consecutivoOrdenes", "numeroSolicitud","estadoMedico", "tipoId", "paciente", "codigoPaciente","pyp","justificacionsolicitud"};
		    this.mapaPeticionesMedico=UtilidadBD.resultSet2HashMap(colums, responderCirugiasDao.cargarPeticionesMedico(con, usuario, institucionMedico, centroCosto, centroAtencion), true, true).getMapa();
		    this.consultarDetallePeticionMedico(con);
		}
		catch(Exception e)
		{
			logger.warn("Error mundo Consulta de Peticiones Por Mèdico" +e.toString());
			mapaPeticionesMedico=null;
		}
		return mapaPeticionesMedico;
	}
	
	/**
	 * Metodo para enviar los parametros al metodo que realiza la consulta del
	 * detalle de los servicios dado el consecutivo de la peticion
	 * @param con
	 */
	public void consultarDetallePeticionMedico(Connection con)
	{
	     for(int j=0;j<Integer.parseInt(this.mapaPeticionesMedico.get("numRegistros")+"");j++)
	     {
	     	try
			{
	     		 HashMap mapaDetallePeticion=new HashMap();
		         mapaDetallePeticion=this.cargarServiciosPeticion(con,Integer.parseInt(this.mapaPeticionesMedico.get("codigoPeticion_"+j)+""));
		         this.mapaPeticionesMedico.put("detPeticion_"+j,mapaDetallePeticion);
			}
	     	catch(Exception e)
			{
	     		logger.warn("Error mundo Consulta de los Servicios por cada Peticion en el flujo del medico " +e.toString());
	     	}
	     	
	     }     
	}
	
	/**
	 * Metodo para enviar los parametros al metodo que realiza la consulta del
	 * detalle de los servicios dado el consecutivo de la peticion
	 * @param con
	 */
	public void consultarDetallePeticionPaciente(Connection con)
	{
	     for(int j=0;j<Integer.parseInt(this.mapaPeticionesPaciente.get("numRegistros")+"");j++)
	     {
	     	try
			{
	     		 HashMap mapaDetallePeticion=new HashMap();
		         mapaDetallePeticion=this.cargarServiciosPeticion(con,Integer.parseInt(this.mapaPeticionesPaciente.get("codigoPeticion_"+j)+""));
		         this.mapaPeticionesPaciente.put("detPeticion_"+j,mapaDetallePeticion);
			}
	     	catch(Exception e)
			{
	     		logger.warn("Error mundo Consulta de los Servicios por cada Peticion en el flujo del paciente " +e.toString());
	     	}
	     	
	     }     
	}
	
	/**
	 * Método para cargar los servicios de la peticion
	 * @param con
	 * @param codigoPeticion
	 * @return
	 * @throws SQLException
	 */
	public HashMap cargarServiciosPeticion(Connection con, int codigoPeticion)  throws SQLException
	{
		responderCirugiasDao= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getResponderCirugiasDao();
		HashMap mapaDetallePeticion= new HashMap();
		try
		{
		    String[] colums={"codigoServicio", "codCups", "servicio", "especialidad", "tipoCirugia"};
		    mapaDetallePeticion=UtilidadBD.resultSet2HashMap(colums, responderCirugiasDao.cargarServiciosPeticion(con, codigoPeticion), true, true).getMapa();
		}
		catch(Exception e)
		{
			logger.warn("Error mundo Consulta de Peticiones Por Mèdico" +e.toString());
			mapaDetallePeticion=null;
		}
		return mapaDetallePeticion;
	}
	
	
}
