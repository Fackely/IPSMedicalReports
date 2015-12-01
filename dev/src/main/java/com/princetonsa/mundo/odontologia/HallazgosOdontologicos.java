package com.princetonsa.mundo.odontologia;

import java.sql.Connection;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;

import util.ConstantesBD;
import util.UtilidadTexto;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.odontologia.HallazgosOdontologicosDao;
import com.princetonsa.dto.odontologia.DtoHallazgoOdontologico;
/**
 * Lógica de negocio de la funcionalidad Hallazgos odontológicos
 * @author Jorge Andrés Ortiz
 * @version 1.1
 */
public class HallazgosOdontologicos
{
	private static Logger logger = Logger.getLogger(HallazgosOdontologicos.class);
	
	private static HallazgosOdontologicosDao getHallazgosOdontologicosDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getHallazgosOdontologicosDao();
	}

	/**
	 * Método para realizar la búsqueda de los Hallazgos Dentales existentes Asociados a una Institucion
	 * @param codigoInstitucionInt
	 * @return
	 */
	public ArrayList<DtoHallazgoOdontologico> consultarHallazgosDentales(
			Connection con, int codigoInstitucionInt) {
		return getHallazgosOdontologicosDao().consultarHallazgosDentales(con, codigoInstitucionInt);
	}

	/**
	 * BUSQUEDA AVANZADA PARA EL HALLAGOS.
	 * @return
	 */
	public static ArrayList<DtoHallazgoOdontologico> busquedaAvanzadaHallazgos(DtoHallazgoOdontologico dto)
	{
			return getHallazgosOdontologicosDao().busquedaAvanzadaHallazgos(dto);
	}
	
	/**
	 * Método para crear un nuevo hallazgo Odontológico 
	 * @param con
	 * @param nuevoHallazgo
	 * @param loginUsuario
	 * @param codigoInstitucionInt
	 * @return
	 */
	public boolean crearNuevoHallazgoOdontologico(Connection con,DtoHallazgoOdontologico nuevoHallazgo, String loginUsuario,int codigoInstitucionInt) {
		return getHallazgosOdontologicosDao().crearNuevoHallazgoOdontologico(con,nuevoHallazgo,loginUsuario,codigoInstitucionInt);
	}
	
	/**
	 * Método para modificar Hallazgo Odontológico
	 * @param con
	 * @param nuevoHallazgo
	 * @param consecutivoHallazgo
	 * @param loginUsuario
	 * @return
	 */
	public boolean modificarHallazgoOdontologico(Connection con,DtoHallazgoOdontologico nuevoHallazgo, int consecutivoHallazgo,String loginUsuario) {
		return getHallazgosOdontologicosDao().modificarHallazgoOdontologico(con,nuevoHallazgo,consecutivoHallazgo,loginUsuario);
	}
	
	/**
	 * Metodo para realizar las validaciones de campos requeridos y posible datos repetidos
	 * @param nuevoHallazgo
	 * @param hallazgosDentales
	 * @param pos
	 * @return
	 */
	public ActionErrors validacionNuevosDatos(DtoHallazgoOdontologico nuevoHallazgo,ArrayList<DtoHallazgoOdontologico> hallazgosDentales,int pos) {
		ActionErrors errores = new ActionErrors();
		
		if(nuevoHallazgo.getNombre().equals(""))
		{
			errores.add("descripcion",new ActionMessage("errors.required", "El Nombre "));
		}
		else
		{
			  if(existeDato("nombre",nuevoHallazgo.getNombre(),hallazgosDentales,pos))
			  {
				  errores.add("descripcion",new ActionMessage("errors.notEspecific","El Nombre "+nuevoHallazgo.getNombre()+" ya fue asignado, por favor modifique "));
			  }
		}
		
		if(nuevoHallazgo.getAcronimo().equals(""))
		{
			errores.add("descripcion",new ActionMessage("errors.required", "El Acrónimo "));
		}
		else
		{
			if(existeDato("acronimo",nuevoHallazgo.getAcronimo(),hallazgosDentales,pos))
			{
				errores.add("descripcion",new ActionMessage("errors.notEspecific","El Acrónimo "+nuevoHallazgo.getAcronimo()+" ya fue asignado, por favor modifique "));
			}
		} 
		
		if(UtilidadTexto.isEmpty(nuevoHallazgo.getAplica_a()))
		{
			errores.add("descripcion",new ActionMessage("errors.required", "Aplica a "));
		}
		
		if(UtilidadTexto.isEmpty(nuevoHallazgo.getActivo()))
		{
			errores.add("descripcion",new ActionMessage("errors.required", "Activo "));
		}
		
		return errores;
	}
	
	
	
	/**
	 * Método para evaluar si la información de un campo esta repetida
	 * @param idCampo
	 * @param dato
	 * @param arreglo
	 * @param pos
	 * @return
	 */
	public boolean existeDato(String idCampo,String dato,ArrayList<DtoHallazgoOdontologico> arreglo,int pos)
	{
		boolean existe=false;
		for(int i=0; i<arreglo.size();i++)
		 {
			if(pos!=i)
			{
				if(idCampo.equals("codigo"))
				{
					if(arreglo.get(i).getCodigo().equals(dato))
					{
						existe=true;
						i=arreglo.size();
					}
				}
				
				if(idCampo.equals("nombre"))
				{
					if(arreglo.get(i).getNombre().equals(dato))
					{
						existe=true;
						i=arreglo.size();
					}
				}
				if(idCampo.equals("acronimo"))
				{
					/*
					 * Solución tarea 134124
					 */
					if(arreglo.get(i).getAcronimo().equals(dato))
					{
						existe=true;
						i=arreglo.size();
					}
				}
			}
			
		}		
		return existe;	
	}

	/**
	 * Metodo para Validar si se realizaron modificaciones
	 * @param nuevoHallazgo
	 * @param hallazgo
	 * @return
	 */
	public boolean validarExistenciaCambios(DtoHallazgoOdontologico nuevoHallazgo,DtoHallazgoOdontologico hallazgo) {
		boolean existioModificacion= true;
		
		logger.info("Validacion CAMBIOS EXISTENTES");
		if(nuevoHallazgo.getCodigo().equals(hallazgo.getCodigo()))
		{
			if(nuevoHallazgo.getNombre().equals(hallazgo.getNombre()))
			{
				if(nuevoHallazgo.getAcronimo().equals(hallazgo.getAcronimo()))
				{
					if(nuevoHallazgo.getDiagnostico().equals(hallazgo.getDiagnostico()))
					{
						if(nuevoHallazgo.getTipo_cie().equals(hallazgo.getTipo_cie()))
						{
							if(nuevoHallazgo.getAplica_a().equals(hallazgo.getAplica_a()))
							{
								if(nuevoHallazgo.getConvencion().equals(hallazgo.getConvencion()))
								{
									if(nuevoHallazgo.getA_tratar().equals(hallazgo.getA_tratar()))											
									{
										if(nuevoHallazgo.getActivo().equals(hallazgo.getActivo()))											
										{
											if(nuevoHallazgo.getTipoHallazgo() == hallazgo.getTipoHallazgo())											
											{
												existioModificacion=false;
											}	
										}
									}	 
								}	 
							}
						}
					}	
				}
			}
		}
		return existioModificacion;
	
	}

	/**
	 * Eliminar un hallazgo odontológico
	 * @param codigoHallazgo Código del hallazgo a eliminar
	 * @return true en caso de que se haya eliminado correctamente
	 */
	public boolean eliminarHallazgoOdontologico(int codigoHallazgo) {
		return getHallazgosOdontologicosDao().eliminarHallazgoOdontologico(codigoHallazgo);
	}

	/**
	 * Retorna las convenciones parametrizadas dentro de los hallazgos
	 * @param institucion Institución que se desea filtrar
	 * @param aplicaA Filtro para el campo aplicaA
	 * @param buscarConConvencion
	 * @param buscarConConvencionVacia
	 * @return Lista de {@link DtoHallazgoOdontologico} con la información resultante e la búsqueda
	 */
	public static ArrayList<DtoHallazgoOdontologico> busquedaConvencionesHallagos(
			int institucion,
			String aplicaA,
			boolean buscarConConvencion,
			boolean buscarConConvencionVacia)
	{
		DtoHallazgoOdontologico dto = new DtoHallazgoOdontologico();
		dto.setInstitucion(institucion);
		dto.setAplica_a(aplicaA);
		dto.setBuscarConConvencion(buscarConConvencion);
		dto.setBuscarConConvencionVacia(buscarConConvencionVacia);
		return getHallazgosOdontologicosDao().busquedaConvencionesHallagos(dto);
	}

	/**
	 * Retorna las convenciones parametrizadas dentro de los hallazgos
	 * @param institucion Institución que se desea buscar
	 * @param aplicaA Filtro para el campo aplicaA
	 * @param path
	 * @return String que contiene un XML con el resultado de la búsqueda, este es utilizado para pasarlo como parámetro al componente Flash
	 */
	public static String busquedaConvencionesHallagosXML(int institucion,String aplicaA,String path)
	{
		DtoHallazgoOdontologico dto = new DtoHallazgoOdontologico();
		String xml = "";
		
		dto.setInstitucion(institucion);
		dto.setAplica_a(aplicaA);
		dto.setBuscarConConvencion(true);
		dto.setBuscarConConvencionVacia(false);
		dto.setPath(path);
		ArrayList<DtoHallazgoOdontologico> array = getHallazgosOdontologicosDao().busquedaConvencionesHallagos(dto);
		
		xml ="<Elemento>" +
				"<codigo>"+ConstantesBD.codigoNuncaValido+"</codigo>" +
				"<descripcion>Seleccione</descripcion>" +
				"<imagen></imagen>"+
				"<borde></borde>"+
			  "</Elemento>";
		
		for(int i = 0; i<array.size(); i++)
		{
			xml +="<Elemento>" +
					"<codigo>"+array.get(i).getConsecutivo()+"</codigo>" +
					"<descripcion>"+UtilidadTexto.cambiarCaracteresEspeciales((array.get(i).getCodigo()+" "+array.get(i).getNombre()))+"</descripcion>" ;
					
					if(!array.get(i).getInfoConvecion().getDescripcion().equals(""))
						xml += "<imagen>"+array.get(i).getInfoConvecion().getIndicativo()+""+array.get(i).getInfoConvecion().getDescripcion()+"</imagen>";
					else
						xml += "<imagen></imagen>";
							
					xml += "<convencion>"+array.get(i).getConvencion()+"</convencion>";
					if(!UtilidadTexto.isEmpty(array.get(i).getInfoConvecion().getNombre()))
					{
						xml += "<borde>"+array.get(i).getInfoConvecion().getNombre()+"</borde>";
					}
					else
					{
						xml += "<borde></borde>";
					}
					xml += "</Elemento>";
		}
		
		if(!xml.equals(""))
			xml = "<Contenido>"+xml+"</Contenido>";
		
		return xml;
	}	
}