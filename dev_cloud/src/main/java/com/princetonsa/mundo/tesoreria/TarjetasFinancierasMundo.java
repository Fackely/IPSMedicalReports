/*
 * Creado   20/09/2005
 *
 *Copyright Princeton S.A. &copy;&reg; 2004. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 * Compilador	:J2SDK 1.5.0_03
 */
package com.princetonsa.mundo.tesoreria;

import java.util.ArrayList;
import java.util.Iterator;

import org.axioma.util.log.Log4JManager;

import util.ConstantesIntegridadDominio;
import util.UtilidadFecha;

import com.servinte.axioma.hibernate.HibernateUtil;
import com.servinte.axioma.orm.HistTarjFinancieraComision;
import com.servinte.axioma.orm.HistTarjFinancieraReteica;
import com.servinte.axioma.orm.HistTarjetasFinancieras;
import com.servinte.axioma.orm.TarjetaFinancieraComision;
import com.servinte.axioma.orm.TarjetaFinancieraComisionHome;
import com.servinte.axioma.orm.TarjetaFinancieraReteica;
import com.servinte.axioma.orm.TarjetaFinancieraReteicaHome;
import com.servinte.axioma.orm.TarjetasFinancieras;
import com.servinte.axioma.orm.TiposTarjetaFinanciera;
import com.servinte.axioma.orm.Usuarios;
import com.servinte.axioma.orm.delegate.tesoreria.TarjetasFinancierasDelegate;
import com.servinte.axioma.orm.delegate.tesoreria.TiposTarjetaFinancieraDelegate;
import com.servinte.axioma.persistencia.UtilidadTransaccion;
import com.servinte.axioma.servicio.fabrica.TesoreriaFabricaServicio;
import com.servinte.axioma.servicio.interfaz.tesoreria.ILogTarjetasFinancieraServicio;


/**
 * Clase para manejar los metodos de el modelo de la funcionalidad de TArjetas Financieras
 *
 * @version 1.0, 20/09/2005
 * @author <a href="mailto:joan@PrincetonSA.com">Joan L&oacute;pez</a>
 */
public class TarjetasFinancierasMundo
{
    
	static ILogTarjetasFinancieraServicio logTarjetasFinancieraServicio = TesoreriaFabricaServicio.crearLogTarjetasFinancieraServicio();
	
	
	
    /**
     * listarTajetasFinancieras
     * @return
     */
	public static ArrayList<TarjetasFinancieras> listarTajetasFinancieras() 
	{
		TarjetasFinancierasDelegate dao=new TarjetasFinancierasDelegate();
		ArrayList<com.servinte.axioma.orm.TarjetasFinancieras> resultado=dao.listarTarjetasFinancieras();
		return resultado;
	}

	
	/**
	 * listarTiposTarjetaFinanciera
	 * @return
	 */
	public static ArrayList<TiposTarjetaFinanciera> listarTiposTarjetaFinanciera() 
	{
		TiposTarjetaFinancieraDelegate dao=new TiposTarjetaFinancieraDelegate();
		ArrayList<TiposTarjetaFinanciera> resultado=dao.listarTiposTarjetasFinancieras();
		return resultado;
	}
	
	
	/**
	 * guadrar tarjetas
	 * @param tarjetas
	 * @return boolean
	 */
	public static boolean guadrar(ArrayList<TarjetasFinancieras> tarjetas, String loginUsuario)
	{
		try{
			HibernateUtil.beginTransaction();
			TarjetasFinancierasDelegate dao = new TarjetasFinancierasDelegate();
			for(TarjetasFinancieras tarjeta:tarjetas)
			{
				dao.attachDirty(tarjeta);
			}
			HibernateUtil.endTransaction();
		}
		catch (Exception e) {
			HibernateUtil.abortTransaction();
			Log4JManager.error("Error guardando las tarjetas financieras", e);
			return false;
		}
		finally
		{
			guadrarLogTarjetasFinancieras(tarjetas, ConstantesIntegridadDominio.acronimoAccionHistoricaInsertar, loginUsuario);
		}
		return true;
	}

	
	/**
	 * cargarTarjeta
	 * @param consecutivoTarjeta
	 */
	public static TarjetasFinancieras cargarTarjeta(int consecutivoTarjeta) 
	{
		TarjetasFinancierasDelegate dao=new TarjetasFinancierasDelegate();
		return dao.cargarTarjeta(consecutivoTarjeta);
	}

	
	/**
	 * eliminarTarjeta
	 * @param consecutivo
	 * @ return boolean
	 */
	@SuppressWarnings("unchecked")
	public static boolean eliminarTarjeta(int consecutivo, String loginUsuario) 
	{
		boolean resultado = true;
		
		try {
			UtilidadTransaccion.getTransaccion().begin();
			
			TarjetasFinancierasDelegate 	dao 				= new TarjetasFinancierasDelegate();
			TarjetaFinancieraComisionHome 	daoComision 		= new TarjetaFinancieraComisionHome();
			TarjetaFinancieraReteicaHome 	daoReteica 			= new TarjetaFinancieraReteicaHome();
			TarjetasFinancieras 			tarjetasFinancieras = dao.findById(consecutivo);
			
			String accionRealizada = ConstantesIntegridadDominio.acronimoAccionHistoricaEliminar;
			
			// Comision
			Iterator iterador=tarjetasFinancieras.getTarjetaFinancieraComisions().iterator();
			while(iterador.hasNext())
			{
				TarjetaFinancieraComision tarjetaFinancieraComision;
				tarjetaFinancieraComision = new TarjetaFinancieraComision();
				tarjetaFinancieraComision =  (TarjetaFinancieraComision)iterador.next();
				
				guadrarLogTarjetasFinancierasComision(tarjetaFinancieraComision, accionRealizada, loginUsuario);
				daoComision.delete(tarjetaFinancieraComision);
			}
			
			// Reteica
			iterador=tarjetasFinancieras.getTarjetaFinancieraReteicas().iterator();
			while(iterador.hasNext())
			{
				TarjetaFinancieraReteica tarjetaFinancieraReteica;
				tarjetaFinancieraReteica = new TarjetaFinancieraReteica();
				tarjetaFinancieraReteica = (TarjetaFinancieraReteica)iterador.next();
				
				guadrarLogTarjetasFinancierasReteica(tarjetaFinancieraReteica, accionRealizada, loginUsuario);
				daoReteica.delete(tarjetaFinancieraReteica);
			}
			
			// Tarjeta
			guadrarLogTarjetasFinancieras(tarjetasFinancieras, accionRealizada, loginUsuario);
			dao.delete(tarjetasFinancieras);

			//-----------------
			UtilidadTransaccion.getTransaccion().commit();
		}
		catch(Exception e) {
			UtilidadTransaccion.getTransaccion().rollback();
			resultado = false;
		}
		return resultado;
	}
	
	
	/**
	 * guardarDetalle de la tarjeta 
	 * 
	 * @param tarjetaFinanciera
	 * @param comision
	 * @param entidadesEliminadas
	 * @param reteica
	 * @param centroAtencionEliminado
	 * @param loginUsuario
	 * @return boolean
	 */
	public static boolean guardarDetalle(TarjetasFinancieras tarjetaFinanciera,
			ArrayList<TarjetaFinancieraComision> comision,
			ArrayList<TarjetaFinancieraComision> entidadesEliminadas,
			ArrayList<TarjetaFinancieraReteica> reteica,
			ArrayList<TarjetaFinancieraReteica> centroAtencionEliminado,String loginUsuario)
	{
		
		String accionRealizadaInsert = ConstantesIntegridadDominio.acronimoAccionHistoricaInsertar;
		String accionRealizadaElimin = ConstantesIntegridadDominio.acronimoAccionHistoricaEliminar;
		String accionRealizadaModifi = ConstantesIntegridadDominio.acronimoAccionHistoricaModificar;
		
		
		
		boolean resultado=false;
		
		try
		{
			HibernateUtil.beginTransaction();
			TarjetasFinancierasDelegate dao=new TarjetasFinancierasDelegate();
			TarjetasFinancieras tarjeta=dao.findById(tarjetaFinanciera.getConsecutivo());
			tarjeta.setBaseRete(tarjetaFinanciera.getBaseRete());
			tarjeta.setCuentaContableComision(tarjetaFinanciera.getCuentaContableComision());
			tarjeta.setCuentaContableRetefte(tarjetaFinanciera.getCuentaContableRetefte());
			tarjeta.setCuentaContableReteica(tarjetaFinanciera.getCuentaContableReteica());
			tarjeta.setCuentaContableReteiva(tarjetaFinanciera.getCuentaContableReteiva());
			tarjeta.setRetefte(tarjetaFinanciera.getRetefte());
			tarjeta.setReteiva(tarjetaFinanciera.getReteiva());
			
			dao.attachDirty(tarjeta);
			guadrarLogTarjetasFinancieras(tarjeta, accionRealizadaModifi, loginUsuario);

			
			//eliminar comision
			for(TarjetaFinancieraComision tempo:entidadesEliminadas)
			{
				if(tempo.getCodigoPk()>0)
				{
					TarjetaFinancieraComisionHome daoDetalle=new TarjetaFinancieraComisionHome();
					
					guadrarLogTarjetasFinancierasComision(tempo, accionRealizadaElimin, loginUsuario);
					daoDetalle.delete(tempo);
				}
			}
			entidadesEliminadas.clear();
			
			//eliminar reteica
			for(TarjetaFinancieraReteica tempo:centroAtencionEliminado)
			{
				if(tempo.getCodigoPk()>0)
				{
					TarjetaFinancieraReteicaHome daoDetalle=new TarjetaFinancieraReteicaHome();
					
					guadrarLogTarjetasFinancierasReteica(tempo, accionRealizadaElimin, loginUsuario);
					daoDetalle.delete(tempo);
				}
			}
			centroAtencionEliminado.clear();
			
			//insetar detalles
			for(TarjetaFinancieraComision tempo:comision)
			{
				Usuarios usuario=new Usuarios();
				usuario.setLogin(loginUsuario);
				tempo.setUsuarios(usuario);
				tempo.setFecha(UtilidadFecha.getFechaActualTipoBD());
				tempo.setHora(UtilidadFecha.getHoraActual());
				tempo.setTarjetasFinancieras(tarjetaFinanciera);
				
				TarjetaFinancieraComisionHome daoDetalle=new TarjetaFinancieraComisionHome();
				
				guadrarLogTarjetasFinancierasComision(tempo, accionRealizadaInsert, loginUsuario);
				daoDetalle.attachDirty(tempo);
			}
			
			
			for(TarjetaFinancieraReteica tempo:reteica)
			{
				Usuarios usuario=new Usuarios();
				usuario.setLogin(loginUsuario);
				tempo.setUsuarios(usuario);
				tempo.setFecha(UtilidadFecha.getFechaActualTipoBD());
				tempo.setHora(UtilidadFecha.getHoraActual());
				tempo.setTarjetasFinancieras(tarjetaFinanciera);

				TarjetaFinancieraReteicaHome daoDetalle=new TarjetaFinancieraReteicaHome();
				
				guadrarLogTarjetasFinancierasReteica(tempo, accionRealizadaInsert, loginUsuario);
				daoDetalle.attachDirty(tempo);
			}
			
			resultado=true;
			//UtilidadTransaccion.getTransaccion().commit();
			HibernateUtil.endTransaction();
		}
		catch(Exception e)
		{
			resultado=false;
			//UtilidadTransaccion.getTransaccion().rollback();
			HibernateUtil.abortTransaction();
		}
		return resultado;
	}
	
	
	/**
	 * Guarda el log de la funcionalidad
	 * @param lista de tarjetasFinancieras
	 * @author Cristhian Murillo
	 */
	public static void guadrarLogTarjetasFinancieras(ArrayList<TarjetasFinancieras> tarjetas, String accionRealizada, String loginUsuario)
	{
		try {
			UtilidadTransaccion.getTransaccion().begin();
			
			for (TarjetasFinancieras tarjetasFinancieras : tarjetas) 
			{
				HistTarjetasFinancieras histTarjetasFinancieras;
				histTarjetasFinancieras = new HistTarjetasFinancieras();
				
				histTarjetasFinancieras.setAccionRealizada(accionRealizada);
				histTarjetasFinancieras.setActivo(tarjetasFinancieras.isActivo());
				histTarjetasFinancieras.setCodigo(tarjetasFinancieras.getCodigo());
				histTarjetasFinancieras.setDescripcion(tarjetasFinancieras.getDescripcion());
				histTarjetasFinancieras.setDirectoBanco(tarjetasFinancieras.isDirectoBanco());
				histTarjetasFinancieras.setFechaModifica(UtilidadFecha.getFechaActualTipoBD());
				histTarjetasFinancieras.setHoraModifica(UtilidadFecha.getHoraActual());
				histTarjetasFinancieras.setInstitucion(tarjetasFinancieras.getInstituciones().getCodigo());
				histTarjetasFinancieras.setTipoTarjetaFinanciera(tarjetasFinancieras.getTiposTarjetaFinanciera().getCodigo());
				histTarjetasFinancieras.setUsuarioModifica(loginUsuario);
				
				logTarjetasFinancieraServicio.guardarHistTarjetasFinancieras(histTarjetasFinancieras);
			}
			
			UtilidadTransaccion.getTransaccion().commit();
			Log4JManager.info("Se guardo el Log de la funcionalidad Tarjetas Financieras");
			
		} catch (Exception e) {
			UtilidadTransaccion.getTransaccion().rollback();
			Log4JManager.info("No se pudo guardar el Log de la funcionalidad Tarjetas Financieras");
		}
		
	}
	
	
	/**
	 * Guarda el log de la funcionalidad.
	 * Este metodo utiliza la misma transaccion del proceso que lo llama.
	 * @param tarjetasFinancieras
	 * @author Cristhian Murillo
	 */
	public static void guadrarLogTarjetasFinancieras(TarjetasFinancieras tarjetasFinancieras, String accionRealizada, String loginUsuario)
	{
		try {
				HistTarjetasFinancieras histTarjetasFinancieras;
				histTarjetasFinancieras = new HistTarjetasFinancieras();
				
				histTarjetasFinancieras.setAccionRealizada(accionRealizada);
				histTarjetasFinancieras.setActivo(tarjetasFinancieras.isActivo());
				histTarjetasFinancieras.setCodigo(tarjetasFinancieras.getCodigo());
				histTarjetasFinancieras.setDescripcion(tarjetasFinancieras.getDescripcion());
				histTarjetasFinancieras.setDirectoBanco(tarjetasFinancieras.isDirectoBanco());
				histTarjetasFinancieras.setFechaModifica(UtilidadFecha.getFechaActualTipoBD());
				histTarjetasFinancieras.setHoraModifica(UtilidadFecha.getHoraActual());
				histTarjetasFinancieras.setInstitucion(tarjetasFinancieras.getInstituciones().getCodigo());
				histTarjetasFinancieras.setTipoTarjetaFinanciera(tarjetasFinancieras.getTiposTarjetaFinanciera().getCodigo());
				histTarjetasFinancieras.setUsuarioModifica(loginUsuario);
				
				logTarjetasFinancieraServicio.guardarHistTarjetasFinancieras(histTarjetasFinancieras);
			
		} catch (Exception e) {
			Log4JManager.info("No se pudo guardar el Log de la funcionalidad Tarjetas Financieras");
		}
	}
	
	
	/**
	 * Guarda el log de la funcionalidad.
	 * Este metodo utiliza la misma transaccion del proceso que lo llama.
	 * @param tarjetaFinancieraReteica
	 * @author Cristhian Murillo
	 */
	public static void guadrarLogTarjetasFinancierasReteica(TarjetaFinancieraReteica tarjetaFinancieraReteica, String accionRealizada, String loginUsuario)
	{
		try {
				HistTarjFinancieraReteica histTarjFinancieraReteica;
				histTarjFinancieraReteica = new HistTarjFinancieraReteica();
				
				histTarjFinancieraReteica.setAccionRealizada(accionRealizada);
				histTarjFinancieraReteica.setCentroAtencion(tarjetaFinancieraReteica.getCentroAtencion().getConsecutivo());
				histTarjFinancieraReteica.setFechaModifica(UtilidadFecha.getFechaActualTipoBD());
				histTarjFinancieraReteica.setHoraModifica(UtilidadFecha.getHoraActual());
				histTarjFinancieraReteica.setReteica(tarjetaFinancieraReteica.getReteica());
				histTarjFinancieraReteica.setTarjetaFinanciera(tarjetaFinancieraReteica.getTarjetasFinancieras().getConsecutivo());
				histTarjFinancieraReteica.setUsuarioModifica(loginUsuario);
				
				logTarjetasFinancieraServicio.guardarHistTarjFinancieraReteica(histTarjFinancieraReteica);
			
		} catch (Exception e) 
		{
			Log4JManager.info("No se pudo guardar el Log de la funcionalidad Tarjetas Financieras Reteica");
		}
	}
	
	
	/**
	 * Guarda el log de la funcionalidad.
	 * Este metodo utiliza la misma transaccion del proceso que lo llama.
	 * @param tarjetaFinancieraComision
	 * @author Cristhian Murillo
	 */
	public static void guadrarLogTarjetasFinancierasComision(TarjetaFinancieraComision tarjetaFinancieraComision, String accionRealizada, String loginUsuario)
	{
		try {
				HistTarjFinancieraComision histTarjFinancieraComision;
				histTarjFinancieraComision = new HistTarjFinancieraComision();
				
				histTarjFinancieraComision.setAccionRealizada(accionRealizada);
				histTarjFinancieraComision.setComision(tarjetaFinancieraComision.getComision());
				histTarjFinancieraComision.setEntidadFinanciera(tarjetaFinancieraComision.getEntidadesFinancieras().getConsecutivo());
				histTarjFinancieraComision.setFechaModifica(UtilidadFecha.getFechaActualTipoBD());
				histTarjFinancieraComision.setHoraModifica(UtilidadFecha.getHoraActual());
				histTarjFinancieraComision.setTarjetaFinanciera(tarjetaFinancieraComision.getTarjetasFinancieras().getConsecutivo());
				histTarjFinancieraComision.setUsuarioModifica(loginUsuario);
				
				logTarjetasFinancieraServicio.guardarHistTarjFinancieraComision(histTarjFinancieraComision);
			
		} catch (Exception e) 
		{
			Log4JManager.info("No se pudo guardar el Log de la funcionalidad Tarjetas Financieras Reteica");
		}
	}
	

}