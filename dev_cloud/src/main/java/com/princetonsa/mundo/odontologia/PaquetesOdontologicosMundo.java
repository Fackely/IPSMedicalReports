/**
 * 
 */
package com.princetonsa.mundo.odontologia;

import java.util.ArrayList;
import java.util.Set;

import util.ConstantesBD;
import util.UtilidadFecha;
import util.Utilidades;

import com.princetonsa.dto.odontologia.DtoDetallePaquetesOdontologicosConvenios;
import com.princetonsa.dto.odontologia.DtoPaquetesOdontologicos;
import com.princetonsa.dto.odontologia.DtoPrograma;
import com.princetonsa.dto.odontologia.DtoProgramasPaqueteOdonto;
import com.princetonsa.dto.odontologia.DtoServiciosPaqueteOdon;
import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.hibernate.HibernateUtil;
import com.servinte.axioma.orm.Especialidades;
import com.servinte.axioma.orm.Instituciones;
import com.servinte.axioma.orm.PaquetesOdontologicos;
import com.servinte.axioma.orm.ProgPaqueteOdonto;
import com.servinte.axioma.orm.Programas;
import com.servinte.axioma.orm.Servicios;
import com.servinte.axioma.orm.ServiciosPaqueteOdon;
import com.servinte.axioma.orm.Usuarios;
import com.servinte.axioma.orm.delegate.odontologia.PaquetesOdontologicosDelegate;
import com.servinte.axioma.orm.delegate.odontologia.ProgPaqueteOdontoDelegate;
import com.servinte.axioma.orm.delegate.odontologia.ServiciosPaqueteOdonDelegate;

/**
 * @author armando
 *
 */
public class PaquetesOdontologicosMundo 
{

	public static boolean insertarPaquete(DtoPaquetesOdontologicos paqueteOdontologico, UsuarioBasico usuario) 
	{
		try
		{
		
			//insertamos el encabezado.
			//Creamos el dao.
			PaquetesOdontologicosDelegate dao=new PaquetesOdontologicosDelegate();
			//Cargamos el ORM
			PaquetesOdontologicos paquete=new PaquetesOdontologicos();
			paquete.setCodigo(paqueteOdontologico.getCodigo());
			paquete.setDescripcion(paqueteOdontologico.getDescripcion());
			Especialidades esp=new Especialidades();
			esp.setCodigo(paqueteOdontologico.getCodigoEspecialidad());
			paquete.setEspecialidades(esp);
			Usuarios usu=new Usuarios();
			usu.setLogin(usuario.getLoginUsuario());
			paquete.setUsuarios(usu);
			paquete.setFechaModifica(UtilidadFecha.getFechaActual());
			paquete.setHoraModifica(UtilidadFecha.getHoraActual());
			Instituciones institucion=new Instituciones();
			institucion.setCodigo(usuario.getCodigoInstitucionInt());
			paquete.setInstituciones(institucion);
			dao.persist(paquete);
			
			//insertamos los detalles, si se inserto bien el encabezado.
			if(paquete.getCodigoPk()>0)
			{
				//insercion de los detalles de programa.
				for(DtoProgramasPaqueteOdonto detalle:paqueteOdontologico.getProgramasPaqueteOdonto())
				{
					//Creamos el dao del detalle.
					ProgPaqueteOdontoDelegate daoDetalle=new ProgPaqueteOdontoDelegate();
					ProgPaqueteOdonto detallePaquete=new ProgPaqueteOdonto();
					detallePaquete.setCantidad(detalle.getCantidad());
					detallePaquete.setPaquetesOdontologicos(paquete);
					
					//cargar los programas.
					Programas programa=new Programas();
					//Integer.pa
					programa.setCodigo((int)detalle.getPrograma().getCodigo());
					detallePaquete.setProgramas(programa);
					daoDetalle.persist(detallePaquete);
					
					//si inserto correctamente continua, de lo contrario aborta y termina.
					if(detallePaquete.getCodigoPk()<=0)
					{
						HibernateUtil.abortTransaction();
						return false;
					}
					
				}
				
				//insercion de los detalles de servicios
				for(DtoServiciosPaqueteOdon detalle:paqueteOdontologico.getServiciosPaqueteOdonto())
				{
					//Creamos el dao del detalle.
					ServiciosPaqueteOdonDelegate daoDetalle=new ServiciosPaqueteOdonDelegate();
					ServiciosPaqueteOdon detallePaquete=new ServiciosPaqueteOdon();
					detallePaquete.setCantidad(detalle.getCantidad());
					detallePaquete.setPaquetesOdontologicos(paquete);
					Servicios servicio=new Servicios();
					servicio.setCodigo(detalle.getServicio());
					detallePaquete.setServicios(servicio);
					daoDetalle.persist(detallePaquete);
					
					//si inserto correctamente continua, de lo contrario aborta y termina.
					if(detallePaquete.getCodigoPk()<=0)
					{
						HibernateUtil.abortTransaction();
						return false;
					}
				}
				HibernateUtil.endTransaction();
				return true;
			}
			else
			{
				HibernateUtil.abortTransaction();
				return false;
			}
		}
		catch(Exception e)
		{
			HibernateUtil.abortTransaction();
			e.printStackTrace();
			return false;
		}
	}
	
	
	public static boolean modificarPaquete(DtoPaquetesOdontologicos paqueteOdontologico, UsuarioBasico usuario) 
	{
		try
		{
		
			//insertamos el encabezado.
			//Creamos el dao.
			PaquetesOdontologicosDelegate dao=new PaquetesOdontologicosDelegate();
			//Cargamos el ORM
			PaquetesOdontologicos paquete=dao.findById(paqueteOdontologico.getCodigoPk());
			paquete.setCodigo(paqueteOdontologico.getCodigo());
			paquete.setDescripcion(paqueteOdontologico.getDescripcion());
			Especialidades esp=new Especialidades();
			esp.setCodigo(paqueteOdontologico.getCodigoEspecialidad());
			paquete.setEspecialidades(esp);
			Usuarios usu=new Usuarios();
			usu.setLogin(usuario.getLoginUsuario());
			paquete.setUsuarios(usu);
			paquete.setFechaModifica(UtilidadFecha.getFechaActual());
			paquete.setHoraModifica(UtilidadFecha.getHoraActual());
			Instituciones institucion=new Instituciones();
			institucion.setCodigo(usuario.getCodigoInstitucionInt());
			paquete.setInstituciones(institucion);
			dao.persist(paquete);
			
			//insertamos los detalles, si se inserto bien el encabezado.
			if(paquete.getCodigoPk()>0)
			{
				//insercion de los detalles de programa.
				for(DtoProgramasPaqueteOdonto detalle:paqueteOdontologico.getProgramasPaqueteOdonto())
				{
					if(!detalle.isExisteBD())
					{
						//Creamos el dao del detalle.
						ProgPaqueteOdontoDelegate daoDetalle=new ProgPaqueteOdontoDelegate();
						ProgPaqueteOdonto detallePaquete=new ProgPaqueteOdonto();
						detallePaquete.setCantidad(detalle.getCantidad());
						detallePaquete.setPaquetesOdontologicos(paquete);
						
						//cargar los programas.
						Programas programa=new Programas();
						//Integer.pa
						programa.setCodigo((int)detalle.getPrograma().getCodigo());
						detallePaquete.setProgramas(programa);
						daoDetalle.persist(detallePaquete);
						
						//si inserto correctamente continua, de lo contrario aborta y termina.
						if(detallePaquete.getCodigoPk()<=0)
						{
							HibernateUtil.abortTransaction();
							return false;
						}
					}
					
				}
				
				//insercion de los detalles de servicios
				for(DtoServiciosPaqueteOdon detalle:paqueteOdontologico.getServiciosPaqueteOdonto())
				{
					if(!detalle.isExisteBD())
					{
						//Creamos el dao del detalle.
						ServiciosPaqueteOdonDelegate daoDetalle=new ServiciosPaqueteOdonDelegate();
						ServiciosPaqueteOdon detallePaquete=new ServiciosPaqueteOdon();
						detallePaquete.setCantidad(detalle.getCantidad());
						detallePaquete.setPaquetesOdontologicos(paquete);
						Servicios servicio=new Servicios();
						servicio.setCodigo(detalle.getServicio());
						detallePaquete.setServicios(servicio);
						daoDetalle.persist(detallePaquete);
						
						//si inserto correctamente continua, de lo contrario aborta y termina.
						if(detallePaquete.getCodigoPk()<=0)
						{
							HibernateUtil.abortTransaction();
							return false;
						}
					}
				}
				HibernateUtil.endTransaction();
				return true;
			}
			else
			{
				HibernateUtil.abortTransaction();
				return false;
			}
		}
		catch(Exception e)
		{
			HibernateUtil.abortTransaction();
			e.printStackTrace();
			return false;
		}
	}
	

	/**
	 * 
	 * @param codigo
	 * @param codigoInstitucion
	 * @return
	 */
	public static boolean existePaqueteOdontologico(String codigo,int codigoInstitucion) 
	{
		PaquetesOdontologicosDelegate dao=new PaquetesOdontologicosDelegate();
		boolean resultado=dao.existePaqueteOdontologico(codigo,codigoInstitucion);
		HibernateUtil.endTransaction();
		return resultado;
	}

	
	/**
	 * Metodo que carga los encabezados de los paquetes odontologicos. no carga el detalle de programas ni el detalle de servicios.
	 * @return
	 */
	public static ArrayList<DtoPaquetesOdontologicos> cargarPaquetesOdontologico(String codigoPaquete,String descripcionPaquete,int codigoEspecialidad) 
	{
		ArrayList<DtoPaquetesOdontologicos> resultados=new ArrayList<DtoPaquetesOdontologicos>();
		PaquetesOdontologicosDelegate dao=new PaquetesOdontologicosDelegate();
		ArrayList<PaquetesOdontologicos> paquetes=dao.listarPaquetesOdontologicos(codigoPaquete,descripcionPaquete,codigoEspecialidad);
		for(PaquetesOdontologicos paquete:paquetes)
		{
			DtoPaquetesOdontologicos resultado=new DtoPaquetesOdontologicos();
			resultado.setCodigo(paquete.getCodigo());
			resultado.setCodigoPk(paquete.getCodigoPk());
			resultado.setCodigoEspecialidad(paquete.getEspecialidades().getCodigo());
			resultado.setDescripcionEspecialidad(paquete.getEspecialidades().getNombre());
			resultado.setDescripcion(paquete.getDescripcion());
			resultado.setFechaModifica(paquete.getFechaModifica());
			resultado.setHoraModifica(paquete.getHoraModifica());
			resultado.setInstitucion(paquete.getInstituciones().getCodigo());
			resultado.setUsuarioModifica(paquete.getUsuarios().getLogin());
			resultado.setEsUsado(paquete.getDetPaqOdontConvenios().size()>0);
			resultados.add(resultado);
		}
		return resultados;
	}
	
	/**
	 * Metodo que carga los encabezados de los paquetes odontologicos. no carga el detalle de programas ni el detalle de servicios.
	 * @return
	 */
	public static ArrayList<DtoPaquetesOdontologicos> cargarPaquetesOdontologicoDiferentesArray(ArrayList<Integer> codigoPaquetes) 
	{
		ArrayList<DtoPaquetesOdontologicos> resultados=new ArrayList<DtoPaquetesOdontologicos>();
		PaquetesOdontologicosDelegate dao=new PaquetesOdontologicosDelegate();
		ArrayList<PaquetesOdontologicos> paquetes=dao.listarPaquetesOdontologicos("","",ConstantesBD.codigoNuncaValido);
		for(PaquetesOdontologicos paquete:paquetes)
		{
			if(!codigoPaquetes.contains(paquete.getCodigoPk()))
			{
				DtoPaquetesOdontologicos resultado=new DtoPaquetesOdontologicos();
				resultado.setCodigo(paquete.getCodigo());
				resultado.setCodigoPk(paquete.getCodigoPk());
				resultado.setCodigoEspecialidad(paquete.getEspecialidades().getCodigo());
				resultado.setDescripcionEspecialidad(paquete.getEspecialidades().getNombre());
				resultado.setDescripcion(paquete.getDescripcion());
				resultado.setFechaModifica(paquete.getFechaModifica());
				resultado.setHoraModifica(paquete.getHoraModifica());
				resultado.setInstitucion(paquete.getInstituciones().getCodigo());
				resultado.setUsuarioModifica(paquete.getUsuarios().getLogin());
				resultados.add(resultado);
			}
		}
		return resultados;
	}
	
	/**
	 * 
	 * @param codigoPk
	 * @return
	 */
	public static DtoPaquetesOdontologicos cargarPaqueteOdontologico(int codigoPk) 
	{
		DtoPaquetesOdontologicos resultado=new DtoPaquetesOdontologicos();
		PaquetesOdontologicosDelegate dao=new PaquetesOdontologicosDelegate();
		PaquetesOdontologicos paquete=dao.findById(codigoPk);
		resultado.setCodigo(paquete.getCodigo());
		resultado.setCodigoPk(paquete.getCodigoPk());
		resultado.setCodigoEspecialidad(paquete.getEspecialidades().getCodigo());
		resultado.setDescripcionEspecialidad(paquete.getEspecialidades().getNombre());
		resultado.setDescripcion(paquete.getDescripcion());
		resultado.setFechaModifica(paquete.getFechaModifica());
		resultado.setHoraModifica(paquete.getHoraModifica());
		resultado.setInstitucion(paquete.getInstituciones().getCodigo());
		resultado.setUsuarioModifica(paquete.getUsuarios().getLogin());
		resultado.setEsUsado(paquete.getDetPaqOdontConvenios().size()>0);
		resultado.setProgramasPaqueteOdonto(cargarProgramasPAquete(paquete.getProgPaqueteOdontos(),resultado.getInstitucion()));
		resultado.setServiciosPaqueteOdonto(cargarServiciosPaquete(paquete.getServiciosPaqueteOdons(),resultado.getInstitucion()));
		return resultado;
	}

	/**
	 * 
	 * @param serviciosPaqueteOdons
	 * @param codigoInstitucion 
	 * @return
	 */
	private static ArrayList<DtoServiciosPaqueteOdon> cargarServiciosPaquete(Set<ServiciosPaqueteOdon> serviciosPaqueteOdons, int codigoInstitucion) 
	{
		ArrayList<DtoServiciosPaqueteOdon> resultado=new ArrayList<DtoServiciosPaqueteOdon>();
		for(ServiciosPaqueteOdon servicio:serviciosPaqueteOdons)
		{
			DtoServiciosPaqueteOdon dto=new DtoServiciosPaqueteOdon();
			dto.setCantidad(servicio.getCantidad());
			dto.setCodigoPk(servicio.getCodigoPk());
			dto.setCodigoPkPaquete(servicio.getPaquetesOdontologicos().getCodigoPk());
			dto.setDescripcionServicio(com.princetonsa.mundo.parametrizacion.Servicios.obtenerNombreServicioTarifarioParametrizado(servicio.getServicios().getCodigo(),codigoInstitucion));
			dto.setServicio(servicio.getServicios().getCodigo());
			dto.setExisteBD(true);
			resultado.add(dto);
		}
		return resultado;
	}

	/**
	 * @param codigoInstitucion 
	 * 
	 */
	private static ArrayList<DtoProgramasPaqueteOdonto> cargarProgramasPAquete(Set<ProgPaqueteOdonto> progPaqueteOdontos, int codigoInstitucion) 
	{
		ArrayList<DtoProgramasPaqueteOdonto> resultado=new ArrayList<DtoProgramasPaqueteOdonto>();
		for(ProgPaqueteOdonto programa:progPaqueteOdontos)
		{
			DtoProgramasPaqueteOdonto dto=new DtoProgramasPaqueteOdonto();
			dto.setCantidad(programa.getCantidad());
			dto.setCodigoPk(programa.getCodigoPk());
			dto.setCodigoPkPaquete(programa.getPaquetesOdontologicos().getCodigoPk());
			DtoPrograma temporal=new DtoPrograma();
			temporal.setCodigo(programa.getProgramas().getCodigo());
			temporal.setInstitucion(codigoInstitucion);
			dto.setPrograma(Programa.cargar(temporal).get(0));//buscamos por llave primaria, entonces siempre trae un solo registro.
			dto.setExisteBD(true);
			resultado.add(dto);
		}
		return resultado;
	}

	public static boolean eliminarPrograma(int codigoPk) 
	{
		try
		{
			ProgPaqueteOdontoDelegate dao=new ProgPaqueteOdontoDelegate();
			ProgPaqueteOdonto dto=dao.findById(codigoPk);
			dao.delete(dto);
			HibernateUtil.endTransaction();

			return true;
		}
		catch(Exception e)
		{
			HibernateUtil.abortTransaction();
			return false;
		}
	}
	
	public static boolean eliminarServicio(int codigoPk) 
	{
		try
		{
			ServiciosPaqueteOdonDelegate dao=new ServiciosPaqueteOdonDelegate();
			ServiciosPaqueteOdon dto=dao.findById(codigoPk);
			dao.delete(dto);
			HibernateUtil.endTransaction();
			return true;
		}
		catch(Exception e)
		{
			HibernateUtil.abortTransaction();
			return false;
		}
	}


	/**
	 * 
	 * @param codigoPk
	 */
	public static void eliminarPaqueteOdontologico(int codigoPk) 
	{
		HibernateUtil.beginTransaction();
		PaquetesOdontologicosDelegate dao=new PaquetesOdontologicosDelegate();
		PaquetesOdontologicos paquete=dao.findById(codigoPk);
		eliminarProgPaquOdo(paquete.getProgPaqueteOdontos());
		eliminarServOdo(paquete.getServiciosPaqueteOdons());
		dao.delete(paquete);
		
		HibernateUtil.endTransaction();

	}


	/**
	 * 
	 * @param serviciosPaqueteOdons
	 */
	private static void eliminarServOdo(Set<ServiciosPaqueteOdon> serviciosPaqueteOdons) 
	{
		for(ServiciosPaqueteOdon serv:serviciosPaqueteOdons)
		{
			ServiciosPaqueteOdonDelegate daoServ=new ServiciosPaqueteOdonDelegate();
			daoServ.delete(serv);
		}
	}


	/**
	 * 
	 * @param progPaqueteOdontos
	 */
	private static void eliminarProgPaquOdo(Set<ProgPaqueteOdonto>  progPaqueteOdontos) 
	{
		for(ProgPaqueteOdonto prog:progPaqueteOdontos)
		{
			ProgPaqueteOdontoDelegate daoProg=new ProgPaqueteOdontoDelegate();
			daoProg.delete(prog);
		}
		
	}

}
