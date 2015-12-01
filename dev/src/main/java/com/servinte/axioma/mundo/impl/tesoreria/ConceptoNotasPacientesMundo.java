package com.servinte.axioma.mundo.impl.tesoreria;

import java.util.ArrayList;
import java.util.HashMap;

import org.axioma.util.log.Log4JManager;
import org.hibernate.HibernateException;

import com.servinte.axioma.dao.fabrica.TesoreriaFabricaDAO;
import com.servinte.axioma.dao.interfaz.tesoreria.IConceptoNotasPacientesDAO;
import com.servinte.axioma.dto.tesoreria.DtoConcNotaPacCuentaCont;
import com.servinte.axioma.dto.tesoreria.DtoConceptoNotasPacientes;
import com.servinte.axioma.mundo.fabrica.TesoreriaFabricaMundo;
import com.servinte.axioma.mundo.fabrica.facturacion.FacturacionFabricaMundo;
import com.servinte.axioma.mundo.interfaz.facturacion.IEmpresasInstitucionMundo;
import com.servinte.axioma.mundo.interfaz.tesoreria.IConceptoNotaPacCuentaContMundo;
import com.servinte.axioma.mundo.interfaz.tesoreria.IConceptoNotasPacientesMundo;
import com.servinte.axioma.orm.ConcNotaPacCuentaCont;
import com.servinte.axioma.orm.ConceptoNotaPaciente;
import com.servinte.axioma.orm.CuentasContables;
import com.servinte.axioma.orm.EmpresasInstitucion;
import com.servinte.axioma.orm.delegate.interfaz.CuentasContablesDelegate;
import com.servinte.axioma.persistencia.UtilidadTransaccion;

/**
 * Define la logica de negocio relaciona con 
 * ConceptoNotasPacientes
 * @author diecorqu
 * @see IConceptoNotasPacientesMundo
 */
public class ConceptoNotasPacientesMundo implements
		IConceptoNotasPacientesMundo {

	private IConceptoNotasPacientesDAO dao;
	
	private IConceptoNotaPacCuentaContMundo concNotaPacCueContMundo; 
	private IEmpresasInstitucionMundo empresaInstitucionMundo; 
	private CuentasContablesDelegate cuentasContablesDelegate;
	
	public ConceptoNotasPacientesMundo() {
		dao = TesoreriaFabricaDAO.crearConceptoNotasPacientesDAO();
		concNotaPacCueContMundo = TesoreriaFabricaMundo.crearConceptoNotaPacCuentaCont();
		empresaInstitucionMundo = FacturacionFabricaMundo.crearEmpresasInstitucionMundo();
		cuentasContablesDelegate = new CuentasContablesDelegate();
	}

	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.tesoreria.IConceptoNotasPacientesMundo#guardarConceptoNotaPaciente(com.servinte.axioma.orm.ConceptoNotaPaciente)
	 */
	@Override
	public boolean guardarConceptoNotaPaciente(
			ConceptoNotaPaciente conceptoNotaPaciente) {
		return dao.guardarConceptoNotaPaciente(conceptoNotaPaciente);
	}

	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.tesoreria.IConceptoNotasPacientesMundo#eliminarConceptoNotaPaciente(com.servinte.axioma.orm.ConceptoNotaPaciente)
	 */
	@Override
	public void eliminarConceptoNotaPaciente(long codigo) {
		ConceptoNotaPaciente conceptoNotaPaciente = findById(codigo);
		if (conceptoNotaPaciente != null) {
			dao.eliminarConceptoNotaPaciente(conceptoNotaPaciente);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.tesoreria.IConceptoNotasPacientesMundo#modificarConceptoNotaPaciente(com.servinte.axioma.orm.ConceptoNotaPaciente)
	 */
	@Override
	public ConceptoNotaPaciente modificarConceptoNotaPaciente(
			ConceptoNotaPaciente conceptoNotaPaciente) {
		return modificarConceptoNotaPaciente(conceptoNotaPaciente);
	}

	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.tesoreria.IConceptoNotasPacientesMundo#findById(long)
	 */
	@Override
	public ConceptoNotaPaciente findById(long codigo) {
		return dao.findById(codigo);
	}

	public ArrayList<EmpresasInstitucion> obtenerEmpresaInstitucion(int codigoInstitucion) {
		ArrayList<EmpresasInstitucion> empresasInstitucion = 
			empresaInstitucionMundo.listarEmpresaInstitucionPorInstitucion(codigoInstitucion);
		return empresasInstitucion;
	}
	
	public CuentasContables obtenerCuentaContable(long codigo) {
		return cuentasContablesDelegate.findById(codigo);
	}

	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.tesoreria.IConceptoNotasPacientesMundo#listarConceptoNotasPacientes()
	 */
	@Override
	public ArrayList<DtoConceptoNotasPacientes> listarConceptoNotasPacientes() {
		ArrayList<DtoConceptoNotasPacientes> listaConceptos = 
			new ArrayList<DtoConceptoNotasPacientes>();
		int i = 1;
		try {
			UtilidadTransaccion.getTransaccion().begin();
			listaConceptos = dao.listarConceptoNotasPacientes();
			for (DtoConceptoNotasPacientes dtoConceptoNotasPacientes : listaConceptos) {
				ArrayList<DtoConcNotaPacCuentaCont> listaConcNotaPacCueCont = 
					concNotaPacCueContMundo.listarConceptoNotaPacCuentaCont(dtoConceptoNotasPacientes.getCodigoPk());
				dtoConceptoNotasPacientes.setNumero(i++);
				if (listaConcNotaPacCueCont != null && !listaConcNotaPacCueCont.isEmpty()) {
					dtoConceptoNotasPacientes.setListaConceptoNotaPacCuentaCont(listaConcNotaPacCueCont);
				}
			}
			UtilidadTransaccion.getTransaccion().commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listaConceptos;
	}
	
	
	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.tesoreria.IConceptoNotasPacientesMundo#obtenerMapaNotasPacientesCuentasContables(java.util.ArrayList)
	 */
	@Override
	public HashMap<String, ArrayList<DtoConcNotaPacCuentaCont>> obtenerMapaNotasPacientesCuentasContables(
			ArrayList<DtoConceptoNotasPacientes> listaDtoConceptos) {
		HashMap<String, ArrayList<DtoConcNotaPacCuentaCont>> mapaCuentasContablesMultiInsitucion = 
			new HashMap<String, ArrayList<DtoConcNotaPacCuentaCont>>();
		int i = 1;
		try {
			for (DtoConceptoNotasPacientes dtoConceptoNotasPacientes : listaDtoConceptos) {
				dtoConceptoNotasPacientes.setNumero(i++);
				if (dtoConceptoNotasPacientes.getListaConceptoNotaPacCuentaCont() != null && 
						!dtoConceptoNotasPacientes.getListaConceptoNotaPacCuentaCont().isEmpty()) {
					mapaCuentasContablesMultiInsitucion.put(
							"codigo_cuenta_mod_"+dtoConceptoNotasPacientes.getCodigoPk(), 
							dtoConceptoNotasPacientes.getListaConceptoNotaPacCuentaCont());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mapaCuentasContablesMultiInsitucion;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.tesoreria.IConceptoNotasPacientesMundo#guardarConceptosNotasPacientes(java.util.ArrayList)
	 */
	@Override
	public boolean guardarConceptosNotasPacientes(
			ArrayList<DtoConceptoNotasPacientes> listaConceptosNotasPacientes) {
		boolean resultado = true;
		try {
			for (DtoConceptoNotasPacientes dtoConceptoNotasPacientes : listaConceptosNotasPacientes) {
				ConceptoNotaPaciente concepto = new ConceptoNotaPaciente();
				concepto.setCodigo(dtoConceptoNotasPacientes.getCodigo());
				concepto.setDescripcion(dtoConceptoNotasPacientes.getDescripcion());
				concepto.setActivo(dtoConceptoNotasPacientes.getActivo());
				concepto.setNaturaleza(dtoConceptoNotasPacientes.getNaturaleza());
				resultado = dao.guardarConceptoNotaPaciente(concepto);
				if (resultado) {
					if (dtoConceptoNotasPacientes.getListaConceptoNotaPacCuentaCont() != null) {
						for (ConcNotaPacCuentaCont dtoConcNotaPacCuentaCont : dtoConceptoNotasPacientes.getListaConceptoNotaPacCuentaCont()) {
							ConcNotaPacCuentaCont concNotaPacCuentaCont = new ConcNotaPacCuentaCont();
							concNotaPacCuentaCont.setCodigo(dtoConcNotaPacCuentaCont.getCodigo());
							concNotaPacCuentaCont.setCuentasContables(dtoConcNotaPacCuentaCont.getCuentasContables());
							concNotaPacCuentaCont.setEmpresasInstitucion(dtoConcNotaPacCuentaCont.getEmpresasInstitucion());
							concNotaPacCuentaCont.setInstituciones(dtoConcNotaPacCuentaCont.getInstituciones());
							concNotaPacCuentaCont.setConceptoNotaPaciente(concepto);
							concNotaPacCueContMundo.guardarConceptoNotaPacCuentaCont(concNotaPacCuentaCont);
						}
					}
				} else {
					break;
				}
			}
		} catch (Exception e) {
			Log4JManager.error("ERROR Guardando Conceptos Notas Pacientes");
			e.printStackTrace();
			throw new HibernateException("ERROR Modificando Conceptos Notas Pacientes" + e.getCause());
		} 
		
		return resultado;
	}

	@Override
	public boolean modificarConceptosNotasPacientes(
			ArrayList<DtoConceptoNotasPacientes> listaConceptosNotasPacientes) {
		boolean resultado = false;
		try {
			for (DtoConceptoNotasPacientes dtoConceptoNotasPacientes : listaConceptosNotasPacientes) {
				ConceptoNotaPaciente concepto = new ConceptoNotaPaciente();
				concepto.setCodigopk(dtoConceptoNotasPacientes.getCodigoPk());
				concepto.setCodigo(dtoConceptoNotasPacientes.getCodigo());
				concepto.setDescripcion(dtoConceptoNotasPacientes.getDescripcion());
				concepto.setActivo(dtoConceptoNotasPacientes.getActivo());
				concepto.setNaturaleza(dtoConceptoNotasPacientes.getNaturaleza());
				if (dao.modificarConceptoNotaPaciente(concepto) != null) {
					if (dtoConceptoNotasPacientes.getListaConceptoNotaPacCuentaCont() != null) {
						for (ConcNotaPacCuentaCont dtoConcNotaPacCuentaCont : dtoConceptoNotasPacientes.getListaConceptoNotaPacCuentaCont()) {
							ConcNotaPacCuentaCont concNotaPacCuentaCont = new ConcNotaPacCuentaCont();
							concNotaPacCuentaCont.setCodigo(dtoConcNotaPacCuentaCont.getCodigo());
							concNotaPacCuentaCont.setCuentasContables(dtoConcNotaPacCuentaCont.getCuentasContables());
							concNotaPacCuentaCont.setEmpresasInstitucion(dtoConcNotaPacCuentaCont.getEmpresasInstitucion());
							concNotaPacCuentaCont.setInstituciones(dtoConcNotaPacCuentaCont.getInstituciones());
							if (concNotaPacCuentaCont.getCodigo() == 0) {
								concNotaPacCuentaCont.setConceptoNotaPaciente(concepto);
								concNotaPacCueContMundo.guardarConceptoNotaPacCuentaCont(concNotaPacCuentaCont);
							} else {
								if (concNotaPacCuentaCont.getCuentasContables() != null) {
									concNotaPacCuentaCont.setConceptoNotaPaciente(concepto);
									concNotaPacCueContMundo.modificarConceptoNotaPacCuentaCont(concNotaPacCuentaCont);
								} else {
									ConcNotaPacCuentaCont concNotaPacCuentaTmp = 
										concNotaPacCueContMundo.findById(concNotaPacCuentaCont.getCodigo());
									concNotaPacCueContMundo.eliminarConceptoNotaPacCuentaCont(concNotaPacCuentaTmp);
								}
							}
						}
					}
				} else {
					break;
				}
			}
			resultado = true;
		} catch (Exception e) {
			Log4JManager.error("ERROR Modificando Conceptos Notas Pacientes" + e.getCause());
			e.printStackTrace();
			throw new HibernateException("ERROR Modificando Conceptos Notas Pacientes" + e.getCause());
		} 
		return resultado;
	}

	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.tesoreria.IConceptoNotasPacientesMundo#listarConceptoNotasPacientesBusquedaAvanzada(com.servinte.axioma.dto.tesoreria.DtoConceptoNotasPacientes)
	 */
	@Override
	public ArrayList<DtoConceptoNotasPacientes> listarConceptoNotasPacientesBusquedaAvanzada(
			DtoConceptoNotasPacientes dtoConceptosNotasPacientes) {
		ArrayList<DtoConceptoNotasPacientes> listaConceptos = 
			new ArrayList<DtoConceptoNotasPacientes>();
		int i = 1;
		try {
			UtilidadTransaccion.getTransaccion().begin();
			listaConceptos = dao.listarConceptoNotasPacientesBusquedaAvanzada(dtoConceptosNotasPacientes);
			for (DtoConceptoNotasPacientes dtoConceptoNotasPacientes : listaConceptos) {
				ArrayList<DtoConcNotaPacCuentaCont> concNotaPacCueCont = 
					concNotaPacCueContMundo.listarConceptoNotaPacCuentaCont(dtoConceptoNotasPacientes.getCodigoPk());
				dtoConceptoNotasPacientes.setNumero(i++);
				if (concNotaPacCueCont != null && !concNotaPacCueCont.isEmpty()) {
					dtoConceptoNotasPacientes.setListaConceptoNotaPacCuentaCont(concNotaPacCueCont);
				}
			}
			UtilidadTransaccion.getTransaccion().commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listaConceptos;
	}

	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.tesoreria.IConceptoNotasPacientesMundo#listarConceptoNotasPacientesEstado(boolean)
	 */
	@Override
	public ArrayList<DtoConceptoNotasPacientes> listarConceptoNotasPacientesEstado(
			boolean estado) {
		return dao.listarConceptoNotasPacientesEstado(estado);
	}
}
