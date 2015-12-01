package com.servinte.axioma.orm.delegate.manejoPaciente;

import java.util.ArrayList;

import org.axioma.util.log.Log4JManager;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

import util.ConstantesBD;
import util.Errores;
import util.UtilidadTexto;
import util.Utilidades;

import com.princetonsa.dto.manejoPaciente.DTONaturalezaPaciente;
import com.servinte.axioma.orm.NaturalezaPacientes;
import com.servinte.axioma.orm.NaturalezaPacientesHome;

/**
 * Esta clase se encarga de ejecutar los m&eacute;todos
 * de negocio de la entidad Naturaleza Paciente
 * 
 * @author Angela Maria Aguirre
 * @since 11/08/2010
 */
@SuppressWarnings("unchecked")
public class NaturalezaPacienteDelegate extends NaturalezaPacientesHome {
	
	/**
	 * 
	 * Este M&eacute;todo se encarga de consultar todos los registros de 
	 * Naturalezas Pacientes del sistema
	 * 
	 * @return ArrayList<NaturalezaPacientes>
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<NaturalezaPacientes> consultarNaturalezaPacientes(){
		
		ArrayList<NaturalezaPacientes> lista =  (ArrayList<NaturalezaPacientes>)sessionFactory.getCurrentSession()
			.createCriteria(NaturalezaPacientes.class, "naturaleza")
			.addOrder(Order.asc("naturaleza.nombre"))
			.list();
		
		return lista;
	}
	
	/**
	 * 
	 * Este m&eacute;todo se encarga de actualizar el registro
	 * de Naturalezas Paciente
	 * 
	 * @param NaturalezaPacientes
	 * @return ArrayList<Errores> 
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<Errores> actualizarNaturalezaPaciente(NaturalezaPacientes registro){
		ArrayList<Errores> listaErores = new ArrayList<Errores>();		
		try{
			super.merge(registro);
		}catch (Exception e) {			
			Log4JManager.error("No se pudo actualizar el registro de Naturaleza Paciente: ",e);
			Errores error = new Errores("Los datos no se pudieron actualizar", 
			"errores.modManejoPaciente.registroFallido");
			listaErores.add(error);
		}		
		return listaErores;
	}
	
	/**
	 * 
	 * Este m&eacute;todo se encarga de consultar si el registro
	 * de Naturalezas Paciente tiene o no relaciones con otras
	 * tablas de la base de datos
	 * 
	 * @param NaturalezaPacientes
	 * @return boolean 
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean obtenerRelacionesForaneas(NaturalezaPacientes registro){
		boolean obtieneRelaciones=false;

		//MT6471 se cambia la consulta para que me cuente los registros
		Criteria criteria = sessionFactory.getCurrentSession()
			.createCriteria(NaturalezaPacientes.class, "naturaleza")					
		.createAlias("naturaleza.subCuentases", "subcuenta")
		.add(Restrictions.eq("naturaleza.codigo", registro.getCodigo()));
		
		criteria.setProjection(Projections.projectionList()				
				.add(Projections.count("naturaleza.codigo"),"codigo"));
		
		Long cantidad=Long.parseLong(criteria.uniqueResult().toString()); 
		
		
		if(cantidad>0){
			return true;
			
		}else{
			criteria = sessionFactory.getCurrentSession()
			.createCriteria(NaturalezaPacientes.class, "naturaleza")
			.createAlias("naturaleza.detalleCoberturas", "cobertura")
			.add(Restrictions.eq("naturaleza.codigo", registro.getCodigo()));	
			
			criteria.setProjection(Projections.projectionList()				
					.add(Projections.count("naturaleza.codigo"),"codigo"));
			
			 cantidad=Long.parseLong(criteria.uniqueResult().toString()); 
			
			if(cantidad>0){
				return true;
			}else{
				criteria = sessionFactory.getCurrentSession()
				.createCriteria(NaturalezaPacientes.class, "naturaleza")
				.createAlias("naturaleza.logExCoberturasEntSubs", "logCobertura")
				.add(Restrictions.eq("naturaleza.codigo", registro.getCodigo()));
				
				criteria.setProjection(Projections.projectionList()				
						.add(Projections.count("naturaleza.codigo"),"codigo"));
				
				 cantidad=Long.parseLong(criteria.uniqueResult().toString()); 
				
				if(cantidad>0){
					return true;
				}else{
					criteria = sessionFactory.getCurrentSession()
					.createCriteria(NaturalezaPacientes.class, "naturaleza")
					.createAlias("naturaleza.historicoSubcuentases", "historicoSubcuenta")
					.add(Restrictions.eq("naturaleza.codigo", registro.getCodigo()));
					
					criteria.setProjection(Projections.projectionList()				
							.add(Projections.count("naturaleza.codigo"),"codigo"));
						
					cantidad=Long.parseLong(criteria.uniqueResult().toString()); 
					
					if(cantidad>0){
						return true;
					}else{
						criteria = sessionFactory.getCurrentSession()
						.createCriteria(NaturalezaPacientes.class, "naturaleza")
						.createAlias("naturaleza.excepcionesNaturalezas", "excepciones")
						.add(Restrictions.eq("naturaleza.codigo", registro.getCodigo()));
						
						criteria.setProjection(Projections.projectionList()				
								.add(Projections.count("naturaleza.codigo"),"codigo"));
						
						cantidad=Long.parseLong(criteria.uniqueResult().toString()); 
						if(cantidad>0){
							return true;
						}else{
							criteria = sessionFactory.getCurrentSession()
							.createCriteria(NaturalezaPacientes.class, "naturaleza")
							.createAlias("naturaleza.exCoberturasEntidadSubs", "exCoberturas")
							.add(Restrictions.eq("naturaleza.codigo", registro.getCodigo()));
							
							criteria.setProjection(Projections.projectionList()				
									.add(Projections.count("naturaleza.codigo"),"codigo"));
								
							cantidad=Long.parseLong(criteria.uniqueResult().toString()); 
							
							if(cantidad>0){
								return true;
							}else{
								criteria = sessionFactory.getCurrentSession()
								.createCriteria(NaturalezaPacientes.class, "naturaleza")
								.createAlias("naturaleza.exepParaCobXConvconts", "exParaCoberturas")
								.add(Restrictions.eq("naturaleza.codigo", registro.getCodigo()));
								
								criteria.setProjection(Projections.projectionList()				
										.add(Projections.count("naturaleza.codigo"),"codigo"));
									
								cantidad=Long.parseLong(criteria.uniqueResult().toString()); 
								
								if(cantidad>0){
									return true;
								}									
							}
						}
					}
				}
			}	
		}
		return obtieneRelaciones;
	}
	
	/**
	 * 
	 * Este m&eacute;todo se encarga de eliminar el registro
	 * de Naturalezas Paciente
	 * 
	 * @param NaturalezaPacientes
	 * @return boolean 
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean eliminarNaturalezaPaciente(NaturalezaPacientes registro){
		boolean save = true;					
		try{
			registro = super.findById(registro.getCodigo());		
			super.delete(registro);
		} catch (Exception e) {
			save = false;
			Log4JManager.error("No se pudo eliminar el registro de Naturaleza Paciente: ",e);
		}				
		return save;		
	}
	
	/**
	 * 
	 * Este m&eacute;todo se encarga de crear un registro
	 * de Naturalezas Paciente
	 * 
	 * @param NaturalezaPacientes
	 * @return ArrayList<Errores> 
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<Errores> crearNaturalezaPaciente(NaturalezaPacientes registro){
		ArrayList<Errores> listaErores = new ArrayList<Errores>();
		try{
			//MT6471 se cambia a metodo merge
			super.merge(registro);			
		}catch (Exception e) {			
			Log4JManager.error("No se pudo guardar el registro de Naturalezas Paciente: ",e);
			Errores error = new Errores("Los datos no se pudieron registrar", 
			"errores.modManejoPaciente.registroFallido");
			listaErores.add(error);
		}		
		return listaErores;		
	}
	
	/**
	 * 
	 * Este M&eacute;todo se encarga de consultar todos los registros de 
	 * Naturalezas Pacientes del sistema por un campo especificado
	 * 
	 * @param  NaturalezaPacientes
	 * @return ArrayList<NaturalezaPacientes>
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<NaturalezaPacientes> consultarNaturalezaPacientesPorCampos(NaturalezaPacientes naturalezaPaciente){
		
		Criteria criteria =  sessionFactory.getCurrentSession()
			.createCriteria(NaturalezaPacientes.class, "naturaleza")
			.createAlias("naturaleza.usuarios", "usuario");
		
		if(naturalezaPaciente.getConsecutivo()!=null){
			criteria.add(Restrictions.eq("naturaleza.consecutivo",naturalezaPaciente.getConsecutivo()));
		}
		if(!UtilidadTexto.isEmpty(naturalezaPaciente.getNombre())){
			criteria.add(Restrictions.eq("naturaleza.nombre",naturalezaPaciente.getNombre()));
		}
		if(naturalezaPaciente.getFecha()!=null){
			criteria.add(Restrictions.eq("naturaleza.fecha",naturalezaPaciente.getFecha()));
		}		
		if(!UtilidadTexto.isEmpty(naturalezaPaciente.getHora())){
			criteria.add(Restrictions.eq("naturaleza.hora",naturalezaPaciente.getHora()));
		}
		if(naturalezaPaciente.getCodigo()!= ConstantesBD.codigoNuncaValido){
			criteria.add(Restrictions.eq("naturaleza.codigo",naturalezaPaciente.getCodigo()));
		}
		if(naturalezaPaciente.getUsuarios()!=null){
			criteria.add(Restrictions.eq("usuario.login",naturalezaPaciente.getUsuarios().getLogin()));
		}
		ArrayList<NaturalezaPacientes> lista = (ArrayList)criteria.list();
	    
		return lista;
	} 
	
	/**
	 * 
	 * Método se encarga de consultar las Naturaleza Pacientes asociadas al regimen
	 * del convenio
	 * 
	 * @param codigoConvenio
	 * 
	 * @return ArrayList<DTONaturalezaPaciente>
	 * @author Ricardo Ruiz
	 *
	 */
	public ArrayList<DTONaturalezaPaciente> listarNaturalezaPacientesPorConvenio(int codigoConvenio){
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(NaturalezaPacientes.class, "naturalezaPacientes");
		criteria.createAlias("naturalezaPacientes.excepcionesNaturalezas", "excepcion");
		criteria.createAlias("excepcion.tiposRegimen", "tipoRegimen");
		criteria.createAlias("tipoRegimen.convenioses", "convenio");
				
		ProjectionList projection = Projections.projectionList();
				projection.add(Projections.property("naturalezaPacientes.codigo"),"codigo");
				projection.add(Projections.property("naturalezaPacientes.nombre"),"nombre");
		
		criteria.setProjection(Projections.distinct(projection));
		criteria.add(Restrictions.eq("convenio.codigo" , codigoConvenio))
				.addOrder(Order.asc("naturalezaPacientes.nombre"))
				.setResultTransformer(Transformers.aliasToBean(DTONaturalezaPaciente.class));
		
		
		ArrayList<DTONaturalezaPaciente> lista =  (ArrayList<DTONaturalezaPaciente>)criteria.list();
		return lista;
	}
	
}
