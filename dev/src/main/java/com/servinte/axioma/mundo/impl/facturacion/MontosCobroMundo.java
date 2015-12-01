package com.servinte.axioma.mundo.impl.facturacion;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.ValoresPorDefecto;
import util.facturacion.UtilidadesFacturacion;

import com.princetonsa.dto.facturacion.DTOBusquedaMontoAgrupacionArticulo;
import com.princetonsa.dto.facturacion.DTOBusquedaMontoAgrupacionServicio;
import com.princetonsa.dto.facturacion.DTOBusquedaMontoArticuloEspecifico;
import com.princetonsa.dto.facturacion.DTOBusquedaMontoServicioEspecifico;
import com.princetonsa.dto.facturacion.DTOMontosCobro;
import com.princetonsa.dto.facturacion.DTOResultadoBusquedaDetalleMontos;
import com.princetonsa.dto.facturacion.DtoConvenio;
import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.dao.fabrica.ManejoPacienteDAOFabrica;
import com.servinte.axioma.dao.fabrica.facturacion.FacturacionFabricaDAO;
import com.servinte.axioma.dao.interfaz.facturacion.IDetalleMontoDAO;
import com.servinte.axioma.dao.interfaz.facturacion.IDetalleMontoGeneralDAO;
import com.servinte.axioma.dao.interfaz.facturacion.IHistoMontosCobroDAO;
import com.servinte.axioma.dao.interfaz.facturacion.IMontoAgrupacionServiciosDAO;
import com.servinte.axioma.dao.interfaz.facturacion.IMontoServicioEspecificoDAO;
import com.servinte.axioma.dao.interfaz.facturacion.IMontosCobroDAO;
import com.servinte.axioma.dao.interfaz.manejoPaciente.INaturalezaPacienteDAO;
import com.servinte.axioma.hibernate.HibernateUtil;
import com.servinte.axioma.mundo.fabrica.facturacion.FacturacionFabricaMundo;
import com.servinte.axioma.mundo.fabrica.odontologia.manejopaciente.ManejoPacienteFabricaMundo;
import com.servinte.axioma.mundo.interfaz.facturacion.IMontoArticuloEspecificoMundo;
import com.servinte.axioma.mundo.interfaz.facturacion.IMontosCobroMundo;
import com.servinte.axioma.orm.Convenios;
import com.servinte.axioma.orm.DetalleMonto;
import com.servinte.axioma.orm.DetalleMontoGeneral;
import com.servinte.axioma.orm.EmpresasInstitucion;
import com.servinte.axioma.orm.EstratosSociales;
import com.servinte.axioma.orm.HistoDetMontoGen;
import com.servinte.axioma.orm.HistoDetalleMonto;
import com.servinte.axioma.orm.HistoMontosCobro;
import com.servinte.axioma.orm.MontosCobro;
import com.servinte.axioma.orm.NaturalezaPacientes;
import com.servinte.axioma.orm.TiposAfiliado;
import com.servinte.axioma.orm.TiposMonto;
import com.servinte.axioma.orm.TiposPaciente;
import com.servinte.axioma.orm.Usuarios;
import com.servinte.axioma.orm.ViasIngreso;
import com.servinte.axioma.servicio.fabrica.facturacion.FacturacionServicioFabrica;
import com.servinte.axioma.servicio.fabrica.odontologia.administracion.AdministracionFabricaServicio;
import com.servinte.axioma.servicio.interfaz.administracion.IUsuariosServicio;
import com.servinte.axioma.servicio.interfaz.facturacion.IMontoAgrupacionArticuloServicio;

/**
 * Esta clase se encarga de definir los métodos de negocio para
 * la entidad Montos de Cobro
 * 
 * @author Angela Maria Aguirre
 * @since 26/08/2010
 */
public class MontosCobroMundo implements IMontosCobroMundo {
	
	IMontosCobroDAO dao;
	
	public MontosCobroMundo(){
		dao = FacturacionFabricaDAO.crearMontosCobroDAO();
	}
	
	/**
	 * 
	 * Este Método se encarga de consultar los montos de cobro de un 
	 * convenio específico por su id y la fecha de vigencia
	 * 
	 * @param DTOMontosCobro
	 * @return ArrayList<DTOMontosCobroDetalle>
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<DTOResultadoBusquedaDetalleMontos> obtenerMontosCobro(DTOMontosCobro monto){
		ArrayList<DTOResultadoBusquedaDetalleMontos>  listaMontos = dao.obtenerMontosCobro(monto);		
		return listaMontos;
	}

	/**
	 * 
	 * Este Método se encarga de consultar la fecha de vigencia máxima
	 * para un monto de cobro específico. 
	 * 
	 * @param int
	 * @return DtoConvenio
	 * @author, Angela Maria Aguirre
	 *
	 */
	public DTOMontosCobro obtenerFechaMaximaMonto(int codigo){
		return dao.obtenerFechaMaximaMonto(codigo);
	}

	/**
	 * 
	 * Este Método se encarga de retornar los montos de cobro
	 * organizados con sus detalles según la vía de ingreso y el
	 * tipo de paciente
	 * 
	 * @param DTOMontosCobro
	 * @return ArrayList<DTOMontosCobro>
	 * @author, Angela Maria Aguirre
	 *
	 */
	@Override
	public ArrayList<DTOMontosCobro> obtenerMontosCobroEstructurado(
			DTOMontosCobro montosCobro,UsuarioBasico usuarioSesion ) {
		
		ArrayList<DTOResultadoBusquedaDetalleMontos>  listaMontosDetalle = obtenerMontosCobro(montosCobro);
		ArrayList<DetalleMonto>  listaSubcuentasAsociadas = null;
		ArrayList<DTOMontosCobro>  listaMontos = null;
		DTOBusquedaMontoAgrupacionArticulo dtoAgrupacionArticulo=null;
		IDetalleMontoDAO detalleDAO = FacturacionFabricaDAO.crearDetalleMontoDAO();
		
		if(listaMontosDetalle!=null && listaMontosDetalle.size()>0){			
			listaMontos = new ArrayList<DTOMontosCobro>();
			DTOMontosCobro montoCobro=null;			
			int viaIngreso = listaMontosDetalle.get(0).getViaIngresoID();
			String acronimoTipoPaciente = listaMontosDetalle.get(0).getTipoPacienteAcronimo();
			ArrayList<DTOResultadoBusquedaDetalleMontos> listaDetalles = new  ArrayList<DTOResultadoBusquedaDetalleMontos>();
			IMontoAgrupacionServiciosDAO agrupacionServicioDAO = FacturacionFabricaDAO.crearMontoAgrupacionServiciosDAO();
			IMontoServicioEspecificoDAO servicioEspecificoDAO = FacturacionFabricaDAO.crearMontoServicioEspecificoDAO();
			IMontoAgrupacionArticuloServicio agrupacionArticuloServicio = FacturacionServicioFabrica
				.crearMontoAgrupacionArticuloServicio();
			
			int indice=0;
			int penultimoRegistro=0;
			int codigoInstitucion = usuarioSesion.getCodigoInstitucionInt();
			int codigoTipoTarifario=0;
			
			String codigoTarifario = ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(codigoInstitucion);
			if(UtilidadTexto.isNumber(codigoTarifario)){
				
				codigoTipoTarifario = Integer.parseInt(codigoTarifario);
			}
			
			for(DTOResultadoBusquedaDetalleMontos monto : listaMontosDetalle){				
				
				if(viaIngreso==monto.getViaIngresoID() &&
						acronimoTipoPaciente.equals(monto.getTipoPacienteAcronimo())){									
					listaDetalles.add(monto);
					
				}else{
					viaIngreso = monto.getViaIngresoID();
					acronimoTipoPaciente = monto.getTipoPacienteAcronimo();
					montoCobro=new DTOMontosCobro();
					poblarMontoCobro(montoCobro, indice, listaMontosDetalle);										
					montoCobro.setListaDetalles(listaDetalles);					
					listaMontos.add(montoCobro);
					
					listaDetalles = new  ArrayList<DTOResultadoBusquedaDetalleMontos>();
					listaDetalles.add(monto);
					
				}
				
				if(!UtilidadTexto.isEmpty(monto.getTipoDetalleAcronimo())){
					if(monto.getTipoDetalleAcronimo().equals(
							ConstantesIntegridadDominio.acronimoTipoDetalleMontoCobroDET)){
						
						monto.setTipoDetalle((String)ValoresPorDefecto.getIntegridadDominio(
								ConstantesIntegridadDominio.acronimoTipoDetalleMontoCobroDET));
						
						ArrayList<DTOBusquedaMontoAgrupacionServicio> listaAgrupacionServicios = 
							agrupacionServicioDAO.obtenerServiciosPorDetalleID(
								monto.getDetalleCodigo());
						
						
						ArrayList<DTOBusquedaMontoServicioEspecifico> listaServicioEspecifico =  
							servicioEspecificoDAO.obtenerServiciosPorDetalleID(monto.getDetalleCodigo(),codigoTipoTarifario);
						
						
						dtoAgrupacionArticulo= new DTOBusquedaMontoAgrupacionArticulo();
						dtoAgrupacionArticulo.setDetalleCodigo(monto.getDetalleCodigo());
						ArrayList<DTOBusquedaMontoAgrupacionArticulo> listaAgrupacionArticulo =
						agrupacionArticuloServicio.buscarMontoAgrupacionArticuloPorDetalleID(dtoAgrupacionArticulo);
						
						IMontoArticuloEspecificoMundo articuloEspecificoMundo = 
							FacturacionFabricaMundo.crearMontoArticuloEspecificoMundo();
						DTOBusquedaMontoArticuloEspecifico dtoArticulo = new DTOBusquedaMontoArticuloEspecifico();
						dtoArticulo.setDetalleCodigo(monto.getDetalleCodigo());
						ArrayList<DTOBusquedaMontoArticuloEspecifico> listaArticuloEspecifico = 
							articuloEspecificoMundo.buscarMontoArticuloEspecifico(dtoArticulo);
						
						if((listaAgrupacionServicios!=null && listaAgrupacionServicios.size()>0)||
								(listaServicioEspecifico!=null && listaServicioEspecifico.size()>0)||
								(listaAgrupacionArticulo!=null && listaAgrupacionArticulo.size()>0)||
								(listaArticuloEspecifico!=null && listaArticuloEspecifico.size()>0)){
							monto.setManejaDetalle(true);
						}
					}else{
						monto.setTipoDetalle((String)ValoresPorDefecto.getIntegridadDominio(
								ConstantesIntegridadDominio.acronimoTipoDetalleMontoCobroGEN));
						monto.setManejaDetalle(true);
					}
				}					
				
				if(UtilidadesFacturacion.montoCobroAsociadoSubcuenta(monto.getDetalleCodigo()))
				{					
					monto.setPermiteEliminar(false);
				}else{
					monto.setPermiteEliminar(true);
				}
				if(monto.getNaturalezaID()==null){
					monto.setNaturalezaID(ConstantesBD.codigoNuncaValido);
				}
			
				indice++;
			}			
			
			if(listaMontos.size()>0){
				if(listaMontos.size()>=2){
					penultimoRegistro=2;
				}else{
					penultimoRegistro=1;
				}
				
				if(listaMontos.get(listaMontos.size()-penultimoRegistro).getViaIngresoID()==viaIngreso &&
						listaMontos.get(listaMontos.size()-penultimoRegistro).getTipoPacienteAcronimo()
							.equals(acronimoTipoPaciente)){
					if(listaMontos.size()>0){
						listaMontos.get(listaMontos.size()-1).getListaDetalles().add(
								listaDetalles.get(listaDetalles.size()-1));						
					}				
				}else{
					montoCobro=new DTOMontosCobro();					
					poblarMontoCobro(montoCobro, indice, listaMontosDetalle);											
					montoCobro.setListaDetalles(listaDetalles);				
					listaMontos.add(montoCobro);
				}
			}else{
				montoCobro=new DTOMontosCobro();
				poblarMontoCobro(montoCobro, indice, listaMontosDetalle);											
				montoCobro.setListaDetalles(listaDetalles);				
				listaMontos.add(montoCobro);
			}
		}		
		return listaMontos;
		
	}
	
	/**
	 * 
	 * Este Método se encarga de retornar los montos de cobro
	 * organizados con sus detalles según el convenvio y la fecha 
	 * de vigencia del monto de cobro
	 * 
	 * @param DTOMontosCobro
	 * @return ArrayList<DTOMontosCobro>
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<DTOMontosCobro> consultaMontosCobroEstructurado(
			DTOMontosCobro dtoMontoFiltro) {
		
		 IMontosCobroDAO montoDAO = FacturacionFabricaDAO.crearMontosCobroDAO();
		 ArrayList<DTOMontosCobro> listaDTOMontos = new ArrayList<DTOMontosCobro>();
		 ArrayList<DTOResultadoBusquedaDetalleMontos> listaMontos = montoDAO.obtenerMontosCobro(dtoMontoFiltro);
		 ArrayList<DTOResultadoBusquedaDetalleMontos> listaDetalles = new ArrayList<DTOResultadoBusquedaDetalleMontos>();
		 int idMontoCobro =0;
		 DTOMontosCobro dtoMonto = null;
		 MontosCobro montoCobro = new MontosCobro();
		 Convenios convenio = new  Convenios();
		 
		 if(listaMontos!=null && listaMontos.size()>0){
			 
		 if(dtoMontoFiltro.getConvenio()!=null && dtoMontoFiltro.getConvenio().getCodigo()>0){
			 convenio.setCodigo(dtoMontoFiltro.getConvenio().getCodigo());
			 montoCobro.setConvenios(convenio);
		 }
		 if(dtoMontoFiltro.getFechaVigenciaConvenio()!=null){
			 montoCobro.setVigenciaInicial(dtoMontoFiltro.getFechaVigenciaConvenio());
		 }
		 
		 ArrayList<MontosCobro> montosOrdenados = montoDAO.obtenerMontosCobroOrdenado(montoCobro);
		 
		 for(MontosCobro montoOrdenado : montosOrdenados){
			 idMontoCobro=montoOrdenado.getCodigo();
			 for(DTOResultadoBusquedaDetalleMontos registro : listaMontos){					 
				 if(registro.getIdMontoCobro()==montoOrdenado.getCodigo()){												
					if(!UtilidadTexto.isEmpty(registro.getTipoDetalleAcronimo())){
						if(registro.getTipoDetalleAcronimo().equals(
								ConstantesIntegridadDominio.acronimoTipoDetalleMontoCobroDET)){
							
							registro.setTipoDetalle((String)ValoresPorDefecto.getIntegridadDominio(
									ConstantesIntegridadDominio.acronimoTipoDetalleMontoCobroDET));							
						}else{
							registro.setTipoDetalle((String)ValoresPorDefecto.getIntegridadDominio(
									ConstantesIntegridadDominio.acronimoTipoDetalleMontoCobroGEN));
						}
					}
					listaDetalles.add(registro);						
				 }						
				}
			 	if(listaDetalles!=null && listaDetalles.size()>0){
			 		DtoConvenio dtConvenio = new DtoConvenio();
				 	dtConvenio.setDescripcion(montoOrdenado.getConvenios().getNombre());
					
					dtoMonto = new DTOMontosCobro();						
					dtoMonto.setListaDetalles(listaDetalles);
					dtoMonto.setConvenio(dtConvenio);
					dtoMonto.setFechaVigenciaConvenio(montoOrdenado.getVigenciaInicial());
					dtoMonto.setMontoCobroID(idMontoCobro);
					listaDTOMontos.add(dtoMonto);					
					listaDetalles = new  ArrayList<DTOResultadoBusquedaDetalleMontos>();
			 	}				
			 }	
		 }	
		 
		 return listaDTOMontos;
		
	}
	
	
	/**
	 * 
	 * Este Método se encarga de adicionar datos a un  
	 * objeto de tipo DTOMontosCobro
	 * 
	 * @param DTOMontosCobro montoCobro, int indice,
			  ArrayList<DTOMontosCobroDetalle>  listaMontosDetalle
	 * @author, Angela Maria Aguirre
	 *
	 */
	private void poblarMontoCobro(DTOMontosCobro montoCobro, int indice,
			ArrayList<DTOResultadoBusquedaDetalleMontos>  listaMontosDetalle){
		
		montoCobro.setViaIngresoID(listaMontosDetalle.get(indice-1).getViaIngresoID());
		montoCobro.setViaIngresoNombre(listaMontosDetalle.get(indice-1).getViaIngresoNombre());
		montoCobro.setTipoPacienteAcronimo(listaMontosDetalle.get(indice-1).getTipoPacienteAcronimo());
		montoCobro.setTipoPacienteNombre(listaMontosDetalle.get(indice-1).getTipoPacienteNombre());
	}
	
	/**
	 * 
	 * Este método se encarga de eliminar el registro
	 * de Montos de Cobro
	 * 
	 * @param int
	 * @return boolean 
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean eliminarMontoCobro(int idMontoCobro){
		return dao.eliminarMontoCobro(idMontoCobro);
	}	
	
	/**
	 * 
	 * Este Método se encarga de guardar los datos de un monto de
	 * cobro
	 * 
	 * @param DTOMontosCobro monto, UsuarioBasico usuarioSesion
	 * @author, Angela Maria Aguirre
	 *
	 */
	public void guarDatosDetalleMontoCobro(DTOMontosCobro monto, UsuarioBasico usuarioSesion){
		DetalleMontoGeneral detalleGeneral=null;
		DetalleMonto detalleMonto = null;				
		MontosCobro montoCobro=null;
		IDetalleMontoDAO detalleMontoDAO = FacturacionFabricaDAO.crearDetalleMontoDAO();
		IDetalleMontoGeneralDAO detalleMontoGeneralDAO = FacturacionFabricaDAO.crearDetalleMontoGeneralDAO();	
		Usuarios usuario = new Usuarios();
		Convenios convenio = new Convenios();
		int montoID = monto.getListaDetalles().get(0).getIdMontoCobro();
		IUsuariosServicio usuarioServicio = AdministracionFabricaServicio.crearUsuariosServicio();
		//usuario = usuarioServicio.buscarPorLogin(usuarioSesion.getLoginUsuario());	
		usuario.setLogin(usuarioSesion.getLoginUsuario());
		ArrayList<DTOResultadoBusquedaDetalleMontos> listaDetalles = monto.getListaDetalles();
		HistoDetMontoGen histoDetalleGeneral=null;
		HistoDetalleMonto histoDetalle = null;
		HistoMontosCobro histoMonto = new HistoMontosCobro();
		IHistoMontosCobroDAO histoMontoDAO = FacturacionFabricaDAO.crearHistoMontosCobroDAO();
		Set<HistoDetalleMonto> listaHistoDetalleMonto = new HashSet<HistoDetalleMonto>(0);
		
		if(montoID>0){
			String accionModificar = ConstantesIntegridadDominio.acronimoAccionHistoricaModificar;
			String accionInsertar = ConstantesIntegridadDominio.acronimoAccionHistoricaInsertar;
			
			montoCobro = dao.obtenerMontoCobroPorID(montoID);
			if(montoCobro!=null){		
				histoMonto=poblarHistoMontoCobro(accionModificar, usuario, montoCobro);
				if(listaDetalles!=null && listaDetalles.size()>0){
					for(DTOResultadoBusquedaDetalleMontos detalle :listaDetalles){
						detalleMonto= new DetalleMonto();
						detalleGeneral = new DetalleMontoGeneral();
						histoDetalle = new  HistoDetalleMonto();
						histoDetalleGeneral = new HistoDetMontoGen();
						
						if(detalle.getDetalleCodigo()>0){
							detalleMonto = detalleMontoDAO.buscarDetalleMontoPorID(detalle.getDetalleCodigo());
							if(detalleMonto!= null){
								poblarDetalleMonto(detalleMonto, detalle, usuario);
								histoDetalle = poblarHistoDetalle(detalleMonto, accionModificar,usuario);
								
								if(detalle.getTipoDetalleAcronimo().equals(
										ConstantesIntegridadDominio.acronimoTipoDetalleMontoCobroGEN)){
									detalleGeneral = detalleMontoGeneralDAO.buscarPorID(detalleMonto.getDetalleCodigo());
									if(detalleGeneral!=null){										
										poblarDetalleMontoGeneral(detalleGeneral, detalle, usuario);										
										detalleGeneral.setDetalleMonto(detalleMonto);
										detalleMonto.setDetalleMontoGeneral(detalleGeneral);	
										
										histoDetalleGeneral = poblarHistoricoDetalleGeneral(detalleGeneral,
												accionModificar,usuario);
										histoDetalleGeneral.setHistoDetalleMonto(histoDetalle);
										histoDetalle.setHistoDetMontoGen(histoDetalleGeneral);
									}									
								}
								detalleMonto.setMontosCobro(montoCobro);
								
								
								detalleMontoDAO.actualizarDetalleMontoCobro(detalleMonto);								
							}								
						}else{
							
							detalleGeneral = new DetalleMontoGeneral();
							poblarDetalleMonto(detalleMonto, detalle, usuario);
							histoDetalle = poblarHistoDetalle(detalleMonto, accionInsertar,usuario);
							
							if(detalle.getTipoDetalleAcronimo().equals(
								ConstantesIntegridadDominio.acronimoTipoDetalleMontoCobroGEN)){
								
								poblarDetalleMontoGeneral(detalleGeneral, detalle, usuario);
								
								detalleGeneral.setDetalleMonto(detalleMonto);
								detalleMonto.setDetalleMontoGeneral(detalleGeneral);
								
								histoDetalleGeneral = poblarHistoricoDetalleGeneral(detalleGeneral,
										accionInsertar,usuario);
								histoDetalleGeneral.setHistoDetalleMonto(histoDetalle);
								
								histoDetalle.setHistoDetMontoGen(histoDetalleGeneral);
								
							}
							
							detalleMonto.setMontosCobro(montoCobro);
							detalleMontoDAO.guardarDetalleMontoCobro(detalleMonto);
						}
						histoDetalle.setDetalleMonto(detalleMonto);
						histoDetalle.setHistoMontosCobro(histoMonto);
						listaHistoDetalleMonto.add(histoDetalle);
					}					
					histoMonto.setHistoDetalleMontos(listaHistoDetalleMonto);
					histoMontoDAO.guardarHistoDetalleMontoCobro(histoMonto);
				}
			}			
		}else{
			Set<DetalleMonto> listaDetalleMontos = new HashSet<DetalleMonto>(0);
			
			montoCobro=new MontosCobro();			
			EmpresasInstitucion empresa= new EmpresasInstitucion();
			
			empresa.setCodigo(usuarioSesion.getCodigoInstitucionInt());
			convenio.setCodigo(monto.getConvenio().getCodigo());
			montoCobro.setActivo(true);
			montoCobro.setEmpresasInstitucion(empresa);
			montoCobro.setVigenciaInicial(monto.getFechaVigenciaConvenio());
			montoCobro.setConvenios(convenio);	
			
			if(listaDetalles!=null && listaDetalles.size()>0){
			
				for(DTOResultadoBusquedaDetalleMontos detalle: listaDetalles){
					detalleMonto = new DetalleMonto();
					detalleGeneral = new DetalleMontoGeneral();
					
					if(detalle.getTipoDetalleAcronimo().equals(
						ConstantesIntegridadDominio.acronimoTipoDetalleMontoCobroGEN)){						
						poblarDetalleMontoGeneral(detalleGeneral, detalle, usuario);
						detalleGeneral.setDetalleMonto(detalleMonto);
						detalleMonto.setDetalleMontoGeneral(detalleGeneral);															
					}					
					poblarDetalleMonto(detalleMonto, detalle, usuario);										
					detalleMonto.setMontosCobro(montoCobro);
					listaDetalleMontos.add(detalleMonto);
				}
				montoCobro.setDetalleMontos(listaDetalleMontos);
				guardarMontosCobro(montoCobro,usuario);
			}
		}
	}
	
	/**
	 * 
	 * Este Método se encarga de adicionar los datos a actualizar
	 * del detalle del  monto de cobro
	 *
	 * @param DTOMontosCobroDetalle detalle,
			  DetalleMonto detalleMonto,UsuarioBasico usuarioSesion
	 * @author, Angela Maria Aguirre
	 *
	 */
	private void  poblarDetalleMonto(DetalleMonto detalleMonto,
			DTOResultadoBusquedaDetalleMontos detalle,Usuarios usuario){
				
		Date fechaActual = Calendar.getInstance().getTime();
		String horaActual = UtilidadFecha.conversionFormatoHoraABD(fechaActual);	
		
		ViasIngreso viasIngreso = new ViasIngreso();
		viasIngreso.setCodigo(detalle.getViaIngresoID());
		
		TiposAfiliado tipoAfiliado = new TiposAfiliado();
		tipoAfiliado.setAcronimo(detalle.getTipoAfiliadoAcronimo());
				
		TiposPaciente tipoPaciente = new TiposPaciente();
		tipoPaciente.setAcronimo(detalle.getTipoPacienteAcronimo());
		
		EstratosSociales estrato = new EstratosSociales();
		estrato.setCodigo(detalle.getEstratoID());
			
		NaturalezaPacientes naturaleza =null;		
		if(detalle.getNaturalezaID()!=null && 
				detalle.getNaturalezaID()!=ConstantesBD.codigoNuncaValido){
			
			INaturalezaPacienteDAO naturalezaDAO = ManejoPacienteDAOFabrica.crearNaturalezaPacienteDAO();
			
			naturaleza =  naturalezaDAO.fidByID(
					detalle.getNaturalezaID().intValue());
		}
				
		TiposMonto tipoMonto = new TiposMonto();
		tipoMonto.setCodigo(detalle.getTipoMontoID());
		
		detalleMonto.setViasIngreso(viasIngreso);
		detalleMonto.setTiposAfiliado(tipoAfiliado);
		detalleMonto.setTiposPaciente(tipoPaciente);
		detalleMonto.setEstratosSociales(estrato);
		detalleMonto.setNaturalezaPacientes(naturaleza);
		detalleMonto.setTiposMonto(tipoMonto);
		detalleMonto.setTipoDetalle(detalle.getTipoDetalleAcronimo());		
		detalleMonto.setFechaRegistro(fechaActual);
		detalleMonto.setHoraRegistro(horaActual);	
		detalleMonto.setUsuarios(usuario);
		detalleMonto.setActivo(true);
	}
	
	/**
	 * 
	 * Este Método se encarga de adicionar los datos a actualizar
	 * del detalle general del  monto de cobro
	 * 
	 * @param DTOMontosCobroDetalle detalle,
			  DetalleMontoGeneral detalleGeneral,UsuarioBasico usuarioSesion
	 * @author, Angela Maria Aguirre
	 *
	 */
	private void poblarDetalleMontoGeneral(DetalleMontoGeneral detalleGeneral,
			DTOResultadoBusquedaDetalleMontos detalle,Usuarios usuario){
		
		Date fechaActual = Calendar.getInstance().getTime();
		String horaActual = UtilidadFecha.conversionFormatoHoraABD(fechaActual);		
				
		detalleGeneral.setCantidadMonto(detalle.getCantidadMonto());
		detalleGeneral.setFechaRegistro(fechaActual);
		detalleGeneral.setHoraRegistro(horaActual);
		detalleGeneral.setPorcentaje(detalle.getPorcentaje());
		detalleGeneral.setValor(detalle.getValor());
		detalleGeneral.setUsuarios(usuario);		
	}
	
	
	
	/**
	 * 
	 * Este Método se encarga de guardar un monto de
	 * cobro
	 * 
	 * @param MontosCobro, Usuarios usuario
	 * @return boolean
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean guardarMontosCobro(MontosCobro montoCobro,Usuarios usuario){		
		IHistoMontosCobroDAO histoMontoDAO = FacturacionFabricaDAO.crearHistoMontosCobroDAO();
		HistoMontosCobro histoMonto = new HistoMontosCobro();
		HistoDetalleMonto histoDetalle = new  HistoDetalleMonto();
		HistoDetMontoGen histoDetalleGeneral = new HistoDetMontoGen();
		Set<HistoDetalleMonto> listaHistoDetalleMonto = new HashSet<HistoDetalleMonto>(0);
		String accion = ConstantesIntegridadDominio.acronimoAccionHistoricaInsertar;
		
		if(dao.guardarMontosCobro(montoCobro)){
			
			histoMonto = poblarHistoMontoCobro(accion,usuario,montoCobro);
			
			if(montoCobro.getDetalleMontos()!=null && montoCobro.getDetalleMontos().size()>0){
				for(DetalleMonto registro :(Set<DetalleMonto>)montoCobro.getDetalleMontos()){
					histoDetalle = poblarHistoDetalle(registro, accion,usuario);
					
					if(registro.getTipoDetalle().equals(ConstantesIntegridadDominio.acronimoTipoDetalleMontoCobroGEN)){
						histoDetalleGeneral = poblarHistoricoDetalleGeneral(registro.getDetalleMontoGeneral(), accion,usuario);
						histoDetalleGeneral.setHistoDetalleMonto(histoDetalle);
						histoDetalle.setHistoDetMontoGen(histoDetalleGeneral);
					}
					histoDetalle.setHistoMontosCobro(histoMonto);
					listaHistoDetalleMonto.add(histoDetalle);
				}
			}
			histoMonto.setHistoDetalleMontos(listaHistoDetalleMonto);
			histoMontoDAO.guardarHistoDetalleMontoCobro(histoMonto);
			return true;
		}
			
		return false; 
	}
	
	/**
	 * 
	 * Este Método se encarga de
	 * @author, Angela Maria Aguirre
	 *
	 */
	private HistoMontosCobro poblarHistoMontoCobro(String accion,Usuarios usuario,MontosCobro montoCobro){
		HistoMontosCobro histoMonto = new HistoMontosCobro();
		histoMonto.setAccionRealizada(accion);
		histoMonto.setFechaRegistro(Calendar.getInstance().getTime());
		histoMonto.setHoraRegistro(UtilidadFecha.conversionFormatoHoraABD(Calendar
				.getInstance().getTime()));
		histoMonto.setUsuarios(usuario);
		histoMonto.setMontosCobro(montoCobro);
		
		return histoMonto;	
		
	}
	
	/**
	 * 
	 * Este método se encarga de
	 * @author Angela Maria Aguirre
	 */
	private HistoDetMontoGen poblarHistoricoDetalleGeneral(DetalleMontoGeneral registro, String accion,Usuarios usuario){
		HistoDetMontoGen histoDetalleGeneral = new HistoDetMontoGen();
		
		histoDetalleGeneral.setAccionRealizada(accion);
		histoDetalleGeneral.setCantidadMonto(registro.getCantidadMonto());
		histoDetalleGeneral.setPorcentaje(registro.getPorcentaje());
		histoDetalleGeneral.setValor(registro.getValor());		
		histoDetalleGeneral.setFechaRegistro(Calendar.getInstance().getTime());
		histoDetalleGeneral.setHoraRegistro(UtilidadFecha.conversionFormatoHoraABD(Calendar
				.getInstance().getTime()));
		histoDetalleGeneral.setUsuarios(usuario);		
		return histoDetalleGeneral;
	}
	/**
	 * 
	 * Este método se encarga de
	 * @author Angela Maria Aguirre
	 */
	private HistoDetalleMonto poblarHistoDetalle(DetalleMonto registro, String accion,Usuarios usuario){
		HistoDetalleMonto histoDetalle = new  HistoDetalleMonto();
		
		histoDetalle.setAccionRealizada(accion);
		histoDetalle.setDetalleMonto(registro);
		histoDetalle.setEstratoSocialCodigo(registro.getEstratosSociales().getCodigo());		
		histoDetalle.setFechaRegistro(Calendar.getInstance().getTime());
		histoDetalle.setHoraRegistro(UtilidadFecha.conversionFormatoHoraABD(Calendar
				.getInstance().getTime()));
		histoDetalle.setUsuarios(usuario);	
		
		if(registro.getNaturalezaPacientes()!=null && registro.getNaturalezaPacientes().getCodigo()>0){
			histoDetalle.setNaturalezaCodigo(registro.getNaturalezaPacientes().getCodigo());
		}	
		
		histoDetalle.setTipoAfiliadoCodigo(registro.getTiposAfiliado().getAcronimo());
		histoDetalle.setTipoDetalle(registro.getTipoDetalle());
		histoDetalle.setTipoMontoCodigo(registro.getTiposMonto().getCodigo());
		histoDetalle.setTipoPacienteCodigo(registro.getTiposPaciente().getAcronimo().charAt(0));
		histoDetalle.setViaIngresoCodigo(registro.getViasIngreso().getCodigo());		
		
		return histoDetalle;
	}
	
	/**
	 * 
	 * Este Método se encarga de buscar un monto de
	 * cobro por su id
	 * 
	 * @param MontosCobro
	 * @return boolean
	 * @author, Angela Maria Aguirre
	 *
	 */
	public MontosCobro obtenerMontoCobroPorID(int id){
		return dao.obtenerMontoCobroPorID(id);
	}

	@Override
	public DTOResultadoBusquedaDetalleMontos obtenerDetalleMontoCobroPorId(
			int codigoDetalleMonto) {
		return dao.obtenerDetalleMontoCobroPorId(codigoDetalleMonto);
	}

	/**
	 * 
	 * Este Método se encarga de consultar el monto de cobro de un 
	 * convenio específico por su id y la fecha de vigencia seleccionada
	 * por el usuario
	 * 
	 * @param DTOMontosCobro
	 * @return DTOMontosCobro
	 * @author, Angela Maria Aguirre
	 *
	 */
	public DTOMontosCobro obtenerMontosCobroPorConvenioYFechaVigenciaPostulada(DTOMontosCobro monto){
		return dao.obtenerMontosCobroPorConvenioYFechaVigenciaPostulada(monto);
	}
	
	/**
	 * 
	 * Este Método se encarga de consultar los montos de cobro de un 
	 * convenio específico por su id y la fecha de vigencia, agrupados
	 * por el convenio y la fecha de vigencia 
	 * 
	 * @return ArrayList<MontosCobro>
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<MontosCobro> obtenerMontosCobroOrdenado(MontosCobro montoCobro){
		return dao.obtenerMontosCobroOrdenado(montoCobro);
	}
}
