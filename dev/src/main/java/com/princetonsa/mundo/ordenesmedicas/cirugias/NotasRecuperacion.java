/*
 * Creado en Nov 24, 2005
 */
package com.princetonsa.mundo.ordenesmedicas.cirugias;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.NotasRecuperacionDao;
import com.servinte.axioma.dto.salascirugia.NotaRecuperacionDto;
import com.servinte.axioma.fwk.exception.BDException;

/**
 * @author <a href="mailto:cperalta@PrincetonSA.com">Carlos Peralta</a>
 * @version 1.0, 24 /Nov/ 2005
 */
public class NotasRecuperacion
{  
	/**
	 * Manejo de estados para el flujo de la funcionalidad
	 */
	private String estado;
	
	/**
	 * Fecha de registro de la nota de recuperacion
	 */
    private String fechaRecuperacion;
    
    /**
     * Hora de registro de la nota de recuperacion
     */
    private String horaRecuperacion;
	  
   /**
    * Se guardan las notas de recuperacion
    */
   
   private HashMap mapaNotas=new HashMap();
  
   
   /**
	 * Manejo del histórico del resumen gestacional
	 */
	private Collection historicoNotasRecuperacion;
	
   /**
    * Campo para guardar las observaciones generales de las notas de recuperacion
    */
   private String observacionesGralesNueva;
   
   /**
    * Campo para guardar el nuevo registro de observaciones en las notas de recuperacion
    */
   private String observacionesGrales;
   
   /**
    * Cadena para guardar los medicamentos nuevos de las notas de recuperacion
    */
   private String medicamentosNuevos;
   
   /**
    * Cadena para guardar todos los medicamentos ingresados en las notas de recuperacion
    */
   private String medicamentosGrales;
     
	/**
	 * Histórico de las notas de recuperacion parametrizadas
	 */
	private Collection historicoNotasRecuperacionParam;
	
	/**
	 * Histórico de los nuevas notas de recuperacion ingresadas
	 */
	private Collection historicoNuevasNotas;
	
	/**
	 * Es un string para saber desde que pagian se mando a cargar 
	 *
	 */
	private String pagina;
	
	/**
     * Constructor del objeto (Solo inicializa el acceso a la fuente de datos)
     */
    public NotasRecuperacion()
    {
        this.init(System.getProperty("TIPOBD"));
    }
    
    /**
	 * El DAO usado por el objeto <code>NotasRecuperacionDao</code> 
	 * para acceder a la fuente de datos. 
	 */
	private NotasRecuperacionDao notasRecuperacionDao ;
    
	/**
	 * Este método inicializa en valores vacíos, -mas no nulos- los atributos de este objeto.
	 */
	public void reset() 
	{
		this.mapaNotas=new HashMap();
		this.observacionesGrales="";
		this.observacionesGralesNueva="";
		this.historicoNotasRecuperacionParam=new ArrayList();
		this.historicoNuevasNotas=new ArrayList();
		this.historicoNotasRecuperacion=new ArrayList();
		this.medicamentosGrales="";
		this.medicamentosNuevos="";
		this.setMapaNotas("codigosOtros","");
		this.pagina = "";
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
			notasRecuperacionDao = myFactory.getNotasRecuperacionDao();
			wasInited = (notasRecuperacionDao != null);
		}

		return wasInited;
	}

	
	/**
	 * Método para insertar una nota de recuperacion basica
	 * @param con
	 * @param numeroSolicitud
	 * @param medicamentos
	 * @param observacionesGrales
	 * @return
	 */
	public int insertarNotaRecuperacion( Connection con, int numeroSolicitud, String medicamentos, String observacionesGrales) throws SQLException
	{
		return notasRecuperacionDao.insertarNotaRecuperacion(con, numeroSolicitud, medicamentos, observacionesGrales);
	}
	
	
	/**
	 * Método para insertar el detalle de una nota de recuperacion
	 * @param con
	 * @param numeroSolicitud
	 * @param fechaRecuperacion
	 * @param horaRecuperacion
	 * @param notaRecuperacion
	 * @param valorNota
	 * @param fechaGrabacion
	 * @param horaGrabacion
	 * @param codigoEnfermera
	 * @param institucion
	 * @return
	 */
	public int insertarDetalleNotaRecuperacion(Connection con, int numeroSolicitud, String fechaRecuperacion, String horaRecuperacion, int notaRecuperacion, String valorNota, String fechaGrabacion, String horaGrabacion, int codigoEnfermera, int institucion) throws SQLException
	{
		return notasRecuperacionDao.insertarDetalleNotaRecuperacion(con, numeroSolicitud, fechaRecuperacion, horaRecuperacion, notaRecuperacion, valorNota, fechaGrabacion, horaGrabacion, codigoEnfermera, institucion); 
	}
	
	
	
	/**
	 * Método para consultar los tipos de notas de recuperacion parametrizados a la institucion
	 * @param con
	 * @param institucion
	 * @return
	 */
	public HashMap consultarTiposNotasRecuperacion (Connection con, int institucion,  int numeroSolicitud) throws SQLException
	{
		return notasRecuperacionDao.consultarTiposNotasRecuperacion(con, institucion, numeroSolicitud);
	}
	
	
	/**
	 * Método para consultar las obervaciones de una nota de recuperacion segun el
	 * número de solicitud
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 * @throws SQLException
	 */
	public  String consultarObservacionesGenerales( Connection con, int numeroSolicitud) throws SQLException
	{
		return notasRecuperacionDao.consultarObservacionesGenerales(con, numeroSolicitud);
	}
	
	/**
	 * Método para consultar los medicamentos ingresados en una nota
	 * segun el número de solicitud
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 * @throws SQLException
	 */
	public String consultarMedicamentosGenerales( Connection con, int numeroSolicitud) throws SQLException
	{
		return notasRecuperacionDao.consultarMedicamentosGenerales(con, numeroSolicitud); 
	}
	
	/**
	 * Métodod para consultar la fecha - hora de todas las notas de recuperacion
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 * @throws SQLException
	 */
	public HashMap consultarFechaRecuperacion (Connection con, int numeroSolicitud) throws SQLException
	{
		return notasRecuperacionDao.consultarFechaRecuperacion(con, numeroSolicitud);
	}
	
	/**
	 * @return Returns the fechaRecuperacion.
	 */
	public String getFechaRecuperacion()
	{
		return fechaRecuperacion;
	}
	/**
	 * @param fechaRecuperacion The fechaRecuperacion to set.
	 */
	public void setFechaRecuperacion(String fechaRecuperacion)
	{
		this.fechaRecuperacion=fechaRecuperacion;
	}
	/**
	 * @return Returns the horaRecuperacion.
	 */
	public String getHoraRecuperacion()
	{
		return horaRecuperacion;
	}
	/**
	 * @param horaRecuperacion The horaRecuperacion to set.
	 */
	public void setHoraRecuperacion(String horaRecuperacion)
	{
		this.horaRecuperacion=horaRecuperacion;
	}
	/**
	 * @return Returns the medicamentosGrales.
	 */
	public String getMedicamentosGrales()
	{
		return medicamentosGrales;
	}
	/**
	 * @param medicamentosGrales The medicamentosGrales to set.
	 */
	public void setMedicamentosGrales(String medicamentosGrales)
	{
		this.medicamentosGrales=medicamentosGrales;
	}
	/**
	 * @return Returns the medicamentosNuevos.
	 */
	public String getMedicamentosNuevos()
	{
		return medicamentosNuevos;
	}
	/**
	 * @param medicamentosNuevos The medicamentosNuevos to set.
	 */
	public void setMedicamentosNuevos(String medicamentosNuevos)
	{
		this.medicamentosNuevos=medicamentosNuevos;
	}
	/**
	 * @return Returns the estado.
	 */
	public String getEstado()
	{
		return estado;
	}
	/**
	 * @param estado The estado to set.
	 */
	public void setEstado(String estado)
	{
		this.estado = estado;
	}
	
	/**
	 * @return Returns the mapa.
	 */
	public HashMap getMapaNotas()
	{
		return mapaNotas;
	}
	/**
	 * @param mapa The mapa to set.
	 */
	public void setMapaNotas(HashMap examenesLaboratorio)
	{
		this.mapaNotas = examenesLaboratorio;
	}
	/**
	 * @return Returna la propiedad del mapa mapa.
	 */
	public Object getMapaNotas(String key)
	{
		return mapaNotas.get(key);
	}
	/**
	 * @param Asigna la propiedad al mapa
	 */
	public void setMapaNotas(String key, Object value)
	{
		this.mapaNotas.put(key, value);
	}

	/**
	 * @return Returns the observacionesGrales.
	 */
	public String getObservacionesGrales()
	{
		return observacionesGrales;
	}
	/**
	 * @param observacionesGrales The observacionesGrales to set.
	 */
	public void setObservacionesGrales(String observacionesGrales)
	{
		this.observacionesGrales = observacionesGrales;
	}
	
		/**
	 * @return Retorna observacionesGralesNueva.
	 */
	public String getObservacionesGralesNueva() {
		return observacionesGralesNueva;
	}
	/**
	 * @param Asigna observacionesGralesNueva.
	 */
	public void setObservacionesGralesNueva(String observacionesGralesNueva) {
		this.observacionesGralesNueva = observacionesGralesNueva;
	}
	
	/**
	 * @return Returns the historicoNotasRecuperacionParam.
	 */
	public Collection getHistoricoNotasRecuperacionParam()
	{
		return historicoNotasRecuperacionParam;
	}
	/**
	 * @param historicoExamenesParam The historicoNotasRecuperacionParam to set.
	 */
	public void setHistoricoNotasRecuperacionParam(Collection historicoNotasRecuperacionParam)
	{
		this.historicoNotasRecuperacionParam = historicoNotasRecuperacionParam;
	}
	/**
	 * @return Returns the historicoNuevasNotas.
	 */
	public Collection getHistoricoNuevasNotas()
	{
		return historicoNuevasNotas;
	}
	/**
	 * @param historicoNuevasNotas The historicoNuevasNotas to set.
	 */
	public void setHistoricoNuevasNotas(Collection historicoNuevasNotas)
	{
		this.historicoNuevasNotas = historicoNuevasNotas;
	}
	/**
	 * @return Returns the historicoNotasRecuperacion.
	 */
	public Collection getHistoricoNotasRecuperacion()
	{
		return historicoNotasRecuperacion;
	}
	/**
	 * @param historicoResumenGestacional The historicoNotasRecuperacion to set.
	 */
	public void setHistoricoNotasRecuperacion(Collection historicoNotasRecuperacion)
	{
		this.historicoNotasRecuperacion = historicoNotasRecuperacion;
	}

	/**
	 * @return Retorna pagina.
	 */
	public String getPagina()
	{
		return pagina;
	}
	/**
	 * @param Asigna pagina.
	 */
	public void setPagina(String pagina) 
	{
		this.pagina = pagina;
	}
	
	/**
	 * Metodo que carga estructura e historico de notas recuperacion dado un numero de Solicitud
	 * @param con
	 * @param codigoInstitucion
	 * @param numeroSolicitud
	 * @param soloEstructuraParametrizada
	 * @return listaNotasRecuperacion
	 * @throws BDException
	 */
	public List<NotaRecuperacionDto> listaNotasRecuperacion(Connection con, int codigoInstitucion, int numeroSolicitud, boolean soloEstructuraParametrizada, boolean esAscendente) throws BDException{
		
		List<NotaRecuperacionDto>listaNotasRecuperacion=new ArrayList<NotaRecuperacionDto>();
		listaNotasRecuperacion = notasRecuperacionDao.listaNotasRecuperacion(con, codigoInstitucion, numeroSolicitud, soloEstructuraParametrizada,esAscendente);
		return listaNotasRecuperacion;
	}
	/**
	 * Metodo que Guarda nota de recuperacion general, especifica para fecha y detalle de la nota
	 * @param con
	 * @param dto
	 * @throws BDException
	 * @author Oscar Pulido
	 * @created 03/07/2013
	 */
	public void guardarNotaRecuperacion(Connection con, NotaRecuperacionDto dto)throws BDException{
		notasRecuperacionDao.guardarNotaRecuperacion(con, dto);
	}

}
