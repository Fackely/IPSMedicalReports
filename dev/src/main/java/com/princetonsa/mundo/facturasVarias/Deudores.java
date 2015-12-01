package com.princetonsa.mundo.facturasVarias;

import java.sql.Connection;
import java.util.HashMap;

import org.axioma.util.log.Log4JManager;

import util.ResultadoBoolean;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.facturasVarias.DeudoresDao;
import com.princetonsa.dto.administracion.DtoPersonas;
import com.princetonsa.dto.facturasVarias.DtoDeudor;
import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.hibernate.HibernateUtil;
import com.servinte.axioma.mundo.fabrica.AdministracionFabricaMundo;
import com.servinte.axioma.mundo.interfaz.administracion.IPersonas;



/**
 * @author Juan Sebastián Castaño C. 
 *
 *Clase que representa el Mundo con sus atributos y métodos de la funcionalidad
 * DEUDORES
 */
public class Deudores {

		
	private DeudoresDao deudoresDao= null;
	
	private int institucion;
	
	private String usuario;
	
	/**
	 * CONSTRUCTORES
	 */
	public Deudores()
	{
		this.clean();
		this.init(System.getProperty("TIPOBD"));
	}
	
	public void clean()
	{
		this.deudoresDao = null;
		this.institucion = 0;
		this.usuario = "";
	}
	
	/**
	 * Inicializa el acceso a bases de datos de este objeto.
	 * @param tipoBD el tipo de base de datos que va a usar este objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 */
	public void init(String tipoBD) 
	{
		if (deudoresDao == null) 
		{
			// Obtengo el DAO que encapsula las operaciones de BD de este objeto
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			deudoresDao = myFactory.getDeudoresDao();
		}	
	}
	
	
	/**
	 * Método para obtener la instancia DAO de deudores
	 * @return
	 */
	public static DeudoresDao deudoresDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getDeudoresDao();
	}
	
	public HashMap<String, Object> cargarTerceros (Connection con)
	{
		System.out.print("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n en el mundo insti : " + this.institucion);
		return deudoresDao.cargarTerceros(con, institucion);
	}

	/**
	 * @return the institucion
	 */
	public int getInstitucion() {
		return institucion;
	}

	/**
	 * @param institucion the institucion to set
	 */
	public void setInstitucion(int institucion) {
		this.institucion = institucion;
	}

	/**
	 * @return the usuario
	 */
	public String getUsuario() {
		return usuario;
	}

	/**
	 * @param usuario the usuario to set
	 */
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	
	
	
	
	
	
	
	/**
	 * Metodo de consulta de deudores dependiendo del tipo de deudor seleccionado
	 * @param con
	 * @param tipoDeudorSeleccionado
	 * @return
	 */
	public HashMap<String, Object> consultarDeudoresPorTipoSelec (Connection con, String tipoDeudorSeleccionado)
	{
		return deudoresDao.consultarDeudoresPorTipoSelec(con, tipoDeudorSeleccionado , institucion);
	}
	
	/**
	 * Método para cargar información del nuevo deudor
	 * @param con
	 * @param codigo
	 * @param tipoDeudor
	 * @return
	 */
	public static DtoDeudor cargarInformacionNuevoDeudor(Connection con,String codigo,String tipoDeudor)
	{
		return deudoresDao().cargarInformacionNuevoDeudor(con, codigo, tipoDeudor);
	}
	
	/**
	 * Método usado para ingresar un nuevo deudor
	 * @param con
	 * @param deudor
	 * @return
	 */
	public static ResultadoBoolean ingresar(Connection con,DtoDeudor deudor)
	{
		return deudoresDao().ingresar(con, deudor);
	}
	
	/**
	 * Método implementado para cargar la información del deudor
	 * @param con
	 * @param tipoId
	 * @param numeroId
	 * @param tipo 
	 * @return
	 */
	public static DtoDeudor cargar(Connection con,String tipoId, String numeroId, String tipo)
	{
		return deudoresDao().cargar(con, tipoId, numeroId, tipo);
	}

	/**
	 * Método implementado para cargar la información del deudor
	 * @param con
	 * @param campos
	 * @return
	 */
	public static DtoDeudor cargar(Connection con,String codigoDeudor)
	{
		HashMap<String, Object> campos = new HashMap<String, Object>();
		campos.put("codigoDeudor",codigoDeudor);
		return deudoresDao().cargar(con, campos);
	}
	
	/**
	 * Método para cargar información de un paciente que aun no es deudor
	 * @param codigo
	 * @param usuario
	 * @return deudor tmp con los datos tomados del paciente
	 */
	public static DtoDeudor cargarPacienteNoDeudor(String codigo, UsuarioBasico usuario)
	{
		DtoDeudor deudor = null;
		try{
			HibernateUtil.beginTransaction();
			//La información del deudor es tomada del paciente
			IPersonas iPersonas = AdministracionFabricaMundo.crearPersonasMundo();
			DtoPersonas dtoPersona = iPersonas.buscarPersonaPorCodigo(Integer.parseInt(codigo));
			
			if(dtoPersona != null){
				deudor  = new DtoDeudor();
				//Se coloca en blanco el codigo porque aun el deudor no ha sido creado, 
				//por lo tanto su codigo no existe
				deudor.setCodigo(" ");
				deudor.setTipoIdentificacion(dtoPersona.getTipoIdentificacion());
				deudor.setNumeroIdentificacion(dtoPersona.getNumeroIdentificacion());
				deudor.setCodigoPaciente(codigo);
				deudor.setDireccion(dtoPersona.getDireccion()==null?"":dtoPersona.getDireccion());
				deudor.setTelefono(dtoPersona.getTelefonoFijo());
				deudor.setEmail(dtoPersona.getEmail()==null?"":dtoPersona.getEmail());
				deudor.setCodigoInstitucion(usuario.getCodigoInstitucionInt());
				deudor.setUsuarioModifica(usuario);
				deudor.setTipoDeudor("PACI");
				deudor.setActivo("S");
				deudor.setExistePacienteNoDeudor(true);
	
				//Asigna el tipo y número de identificación, los apellidos y nombres del paciente.
				deudor.setRazonSocial(dtoPersona.getRazonSocial());
			}
			HibernateUtil.endTransaction();
		}
		catch (Exception e) {
			Log4JManager.error(e);
			HibernateUtil.abortTransaction();
		}
			
		return deudor;
	}
	
	/**
	 * Método que realiza una modificación del deudor
	 * @param con
	 * @param deudor
	 * @return
	 */
	public static ResultadoBoolean modificar(Connection con,DtoDeudor deudor)
	{
		return deudoresDao().modificar(con, deudor);
	}
	
	/**
	 * Método para generar el log archivo de los deudores
	 * @param deudorAnterior
	 * @param deudor
	 * @param usuario
	 */
	public static void generarLogArchivoDeudores(DtoDeudor deudorAnterior,DtoDeudor deudor,UsuarioBasico usuario)
	{
		String[] indices = {"razonSocial_","activo_","tipoDeudor_","direccion_","telefono_","email_","representanteLegal_","nombreContacto_","observaciones_",""};
		HashMap<String, Object> mapaAnterior = new HashMap<String, Object>();
		mapaAnterior.put("razonSocial_0", deudorAnterior.getRazonSocial());
		mapaAnterior.put("activo_0", ValoresPorDefecto.getIntegridadDominio(deudorAnterior.getActivo()));
		mapaAnterior.put("tipoDeudor_0", ValoresPorDefecto.getIntegridadDominio(deudorAnterior.getTipoDeudor()));
		mapaAnterior.put("direccion_0", deudorAnterior.getDireccion());
		mapaAnterior.put("telefono_0", deudorAnterior.getTelefono());
		mapaAnterior.put("email_0", deudorAnterior.getEmail());
		mapaAnterior.put("representanteLegal_0", deudorAnterior.getNombreRepresentante());
		mapaAnterior.put("nombreContacto_0", deudorAnterior.getNombreContacto());
		mapaAnterior.put("observaciones_0", deudorAnterior.getObservaciones());
		mapaAnterior.put("numRegistros", "1");
		mapaAnterior.put("INDICES", indices);
		
		HashMap<String, Object> mapa = new HashMap<String, Object>();
		mapa.put("razonSocial_0", deudor.getRazonSocial());
		mapa.put("activo_0", ValoresPorDefecto.getIntegridadDominio(deudor.getActivo()));
		mapa.put("tipoDeudor_0", ValoresPorDefecto.getIntegridadDominio(deudor.getTipoDeudor()));
		mapa.put("telefono_0", deudor.getTelefono());
		mapa.put("direccion_0", deudor.getDireccion());
		mapa.put("email_0", deudor.getEmail());
		mapa.put("representanteLegal_0", deudor.getNombreRepresentante());
		mapa.put("nombreContacto_0", deudor.getNombreContacto());
		mapa.put("observaciones_0", deudor.getObservaciones());
		mapa.put("numRegistros", "1");
		mapa.put("INDICES", indices);
		
		Utilidades.generarLogGenerico(mapa, 0, mapaAnterior, usuario.getLoginUsuario(), false, 0, util.ConstantesBD.logDeudoresCodigo,indices);
	}
	
	
	
	/**
	 * 
	 * @param con
	 * @param deudor
	 * @return
	 */
	public  static ResultadoBoolean modificarDeudor(Connection con,DtoDeudor deudor){
		 return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getDeudoresDao().modificarDeudor(con, deudor);
	}	
	
}
