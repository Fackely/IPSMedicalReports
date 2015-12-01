package com.servinte.axioma.mundo.impl.odontologia.ventaTarjetaCliente;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.Errores;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.ValoresPorDefecto;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.RecibosCajaDao;
import com.princetonsa.dto.administracion.DtoPersonas;
import com.princetonsa.dto.facturacion.DtoContrato;
import com.princetonsa.dto.facturacion.DtoConvenio;
import com.princetonsa.dto.facturacion.DtoSeccionConvenioPaciente;
import com.princetonsa.dto.facturasVarias.DtoFacturaVaria;
import com.princetonsa.dto.odontologia.DtoBeneficiarioCliente;
import com.princetonsa.dto.odontologia.DtoEmisionTarjetaCliente;
import com.princetonsa.dto.odontologia.DtoTarjetaCliente;
import com.princetonsa.dto.odontologia.DtoVentaTarjetasCliente;
import com.princetonsa.dto.tesoreria.DtoBonoSerialValor;
import com.princetonsa.dto.tesoreria.DtoDetallePagosBonos;
import com.princetonsa.dto.tesoreria.DtoFormaPago;
import com.princetonsa.dto.tesoreria.DtoInformacionFormaPago;
import com.princetonsa.enu.administracion.EmunConsecutivosTesoreriaCentroAtencion;
import com.princetonsa.mundo.administracion.ConsecutivosCentroAtencion;
import com.princetonsa.mundo.odontologia.IngresoPacienteOdontologia;
import com.princetonsa.mundo.odontologia.TarjetaCliente;
import com.servinte.axioma.dao.fabrica.odontologia.ventastarjeta.VentaTarjetaFabricaDAO;
import com.servinte.axioma.dao.interfaz.odontologia.ventaTarjeta.IBeneficiarioDAO;
import com.servinte.axioma.dao.interfaz.odontologia.ventaTarjeta.IBeneficiarioTcPacienteDAO;
import com.servinte.axioma.dao.interfaz.odontologia.ventaTarjeta.IVentasTarjetaClienteDAO;
import com.servinte.axioma.hibernate.HibernateUtil;
import com.servinte.axioma.mundo.fabrica.TesoreriaFabricaMundo;
import com.servinte.axioma.mundo.fabrica.facturasvarias.FacturasVariasMundoFabrica;
import com.servinte.axioma.mundo.fabrica.odontologia.administracion.AdministracionFabricaMundo;
import com.servinte.axioma.mundo.fabrica.odontologia.facturacion.convenio.ConveniosFabricaMundo;
import com.servinte.axioma.mundo.fabrica.odontologia.manejopaciente.ManejoPacienteFabricaMundo;
import com.servinte.axioma.mundo.interfaz.administracion.IPersonas;
import com.servinte.axioma.mundo.interfaz.facturasvarias.IFacturasVariasMundo;
import com.servinte.axioma.mundo.interfaz.manejoPaciente.IPacientesMundo;
import com.servinte.axioma.mundo.interfaz.odontologia.administracion.IEmisionTarjetaClienteMundo;
import com.servinte.axioma.mundo.interfaz.odontologia.ventaTarjetaCliente.IVentaTarjetaClienteMundo;
import com.servinte.axioma.mundo.interfaz.tesoreria.IFormasPagoMundo;
import com.servinte.axioma.mundo.interfaz.tesoreria.IRecibosCajaMundo;
import com.servinte.axioma.orm.BeneficiarioTarjetaCliente;
import com.servinte.axioma.orm.BeneficiarioTcPaciente;
import com.servinte.axioma.orm.CajasCajeros;
import com.servinte.axioma.orm.CajasCajerosId;
import com.servinte.axioma.orm.CentroAtencion;
import com.servinte.axioma.orm.ConceptosIngTesoreria;
import com.servinte.axioma.orm.ConceptosIngTesoreriaHome;
import com.servinte.axioma.orm.ConceptosIngTesoreriaId;
import com.servinte.axioma.orm.Contratos;
import com.servinte.axioma.orm.ConveniosIngresoPaciente;
import com.servinte.axioma.orm.DetalleConceptosRc;
import com.servinte.axioma.orm.DetalleConceptosRcHome;
import com.servinte.axioma.orm.Deudores;
import com.servinte.axioma.orm.EstadosRecibosCaja;
import com.servinte.axioma.orm.FacturasVarias;
import com.servinte.axioma.orm.Instituciones;
import com.servinte.axioma.orm.Pacientes;
import com.servinte.axioma.orm.Personas;
import com.servinte.axioma.orm.RecibosCaja;
import com.servinte.axioma.orm.RecibosCajaId;
import com.servinte.axioma.orm.Servicios;
import com.servinte.axioma.orm.TiposTarjCliente;
import com.servinte.axioma.orm.Usuarios;
import com.servinte.axioma.orm.VentaTarjetaCliente;
import com.servinte.axioma.orm.delegate.facturacion.convenio.ConveniosIngresoPacienteDelegate;
import com.servinte.axioma.orm.delegate.facturasVarias.DeudoresDelegate;
import com.servinte.axioma.orm.delegate.odontologia.tipoTarjeta.TiposTarjClienteDelegate;
import com.servinte.axioma.persistencia.UtilidadPersistencia;
import com.servinte.axioma.persistencia.UtilidadTransaccion;



/**
 * 
 * @author Edgar Carvajal
 *
 */
public class VentaTarjetaClienteMundo implements IVentaTarjetaClienteMundo{

	/**
	 * Intefaz DAO Venta Tarjeta Cliente
	 */
	private IVentasTarjetaClienteDAO ventaDAO;
	
	/**
	 * 
	 */
	public VentaTarjetaClienteMundo(){
		ventaDAO=VentaTarjetaFabricaDAO.crearVentaTarjetaDAO();
	}
	
	@Override
	public VentaTarjetaCliente buscarxId(Number id) {
		return ventaDAO.buscarxId(id);
	}

	@Override
	public void eliminar(VentaTarjetaCliente objeto) {
		ventaDAO.eliminar(objeto);
	}

	@Override
	public ArrayList<Errores> guardarVenta(DtoVentaTarjetasCliente dtoVenta, DtoFacturaVaria dtoFactura, DtoPersonas dtoCompradorTarjeta) {
		ArrayList<Errores> errores=new ArrayList<Errores>();
		boolean generarReciboCaja=false;
		
		if(dtoVenta.getValorTotalTarjetas()!=0 && dtoVenta.getCaja()!=null)
		{
			if(UtilidadTexto.getBoolean(ValoresPorDefecto.getReciboCajaAutomaticoVentaTarjeta(dtoVenta.getInstitucion())))
			{
				generarReciboCaja=true;
			}
		}
		
		/*
		 * 1. Verificar seriales
		 */
		IEmisionTarjetaClienteMundo emisionMundo=AdministracionFabricaMundo.crearEmisionTarjetaClienteMundo();
		ArrayList<DtoEmisionTarjetaCliente> listaEmision=emisionMundo.consultarEmisionTarjeta((int)dtoVenta.getTipoTarjeta(), dtoFactura.getCentroAtencion());
		
		boolean usarSeriales=false;

		IBeneficiarioDAO beneficiarioDao=VentaTarjetaFabricaDAO.crearBeneficiarioDAO();

		cicloBeneficiarios:for(DtoBeneficiarioCliente dtoBeneficiario:dtoVenta.getListaBeneficiarios())
		{
			if(!UtilidadTexto.isEmpty(dtoBeneficiario.getSerial()))
			{
				usarSeriales=true;
				for(DtoEmisionTarjetaCliente emision:listaEmision)
				{
					Long serial=Long.parseLong(dtoBeneficiario.getSerial());
					if(serial>=emision.getSerialInicial().longValue() && serial<=emision.getSerialFinal().longValue())
					{
						continue cicloBeneficiarios;
					}
				}
				errores.add(new Errores("VentaTarjetaClienteForm.error.noEmisionSerial"));
				return errores;
			}
			else
			{
				break;
			}
		}
		
		/*
		 * Se verifica la NO existencia de los seriales en los beneficiarios previamente registrados
		 */
		if(usarSeriales)
		{
			for(DtoBeneficiarioCliente dtoBeneficiario:dtoVenta.getListaBeneficiarios())
			{
				Long serial=Long.parseLong(dtoBeneficiario.getSerial());
				
				long codigoTipoTarjeta = new BigDecimal(dtoVenta.getTipoTarjeta()).longValue();

				if(beneficiarioDao.existeSerial(serial, codigoTipoTarjeta))
				{
					errores.add(new Errores("VentaTarjetaClienteForm.error.existeSerial"));
					return errores;
				}
			}
		}
		
		Deudores deudor=new DeudoresDelegate().consultarDeudor(dtoCompradorTarjeta);

		/*
		 * 2. Verificar deudor
		 */
		Usuarios usuarioVendedorOrm=new Usuarios();
		usuarioVendedorOrm.setLogin(dtoVenta.getUsuarioVendedor());

		Usuarios usuarioModificaOrm=new Usuarios();
		usuarioModificaOrm.setLogin(dtoVenta.getUsuarioModifica());

		if(deudor==null)
		{
			/*
			 * 2a. crear deudor
			 */
			deudor=this.crearDeudor(dtoCompradorTarjeta, usuarioVendedorOrm);
		}
		
		/*
		 * 3. Se genera la factura varia
		 */
		dtoFactura.setDeudor(deudor.getCodigo());
		this.generarFacturaVaria(dtoVenta, dtoFactura);

		/*
		 * 4. Se registra la venta en BD
		 */
		VentaTarjetaCliente venta=new VentaTarjetaCliente();
		
		venta.setTipoVenta(dtoVenta.getTipoVenta());
		
		FacturasVarias facturaVaria=new FacturasVarias();
		facturaVaria.setCodigoFacVar(dtoFactura.getCodigoFacturaVaria());
		venta.setFacturasVarias(facturaVaria);
		
		TiposTarjCliente tipoTarjeta=new TiposTarjCliente();
		tipoTarjeta.setCodigoPk((long) dtoVenta.getTipoTarjeta());
		venta.setTiposTarjCliente(tipoTarjeta);
		
		Servicios servicio=new Servicios();
		servicio.setCodigo(dtoVenta.getCodigoServicio());
		venta.setServicios(servicio);
		
		venta.setValorUnitarioTarjeta(new BigDecimal(dtoVenta.getValorUnitarioTarjeta()));
		venta.setCantidad(dtoVenta.getCantidad());
		venta.setValorTotalTarjetas(new BigDecimal(dtoVenta.getValorTotalTarjetas()));
		venta.setObservaciones(dtoVenta.getObservaciones());
		
		usuarioVendedorOrm.setLogin(dtoVenta.getUsuarioVendedor());
		venta.setUsuariosByUsuarioVendedor(usuarioVendedorOrm);
		venta.setUsuariosByUsuarioModifica(usuarioModificaOrm);
		
		venta.setEstadoVenta(ConstantesIntegridadDominio.acronimoEstadoFacturado);
		
		venta.setDeudores(deudor);
		
		Contratos contratos=new Contratos();
		contratos.setCodigo(dtoVenta.getContratoTarjeta().getCodigo());
		venta.setContratos(contratos);
		
		Long consecutivo = new Long(UtilidadBD.obtenerValorConsecutivoDisponible(ConstantesBD.nombreConsecutivoVentasTarjetasCliente, dtoVenta.getInstitucion()));

		venta.setConsecutivoVenta(consecutivo.toString());
		
		Instituciones institucion=new Instituciones();
		institucion.setCodigo(dtoVenta.getInstitucion());
		venta.setInstituciones(institucion);
		
		Date fecha=UtilidadFecha.getFechaActualTipoBD();
		String hora=UtilidadFecha.getHoraActual();
		venta.setFechaVenta(fecha);
		venta.setHoraVenta(hora);
		venta.setFechaModifica(fecha);
		venta.setHoraModifica(hora);
		
		ventaDAO.modificar(venta);
		
		dtoVenta.setConsecutivoVenta(Double.parseDouble(venta.getConsecutivoVenta()));
		dtoVenta.setCodigoPk(venta.getCodigoPk());

		Connection conH=UtilidadPersistencia.getPersistencia().obtenerConexion();
		UtilidadBD.cambiarUsoFinalizadoConsecutivo(conH,ConstantesBD.nombreConsecutivoVentasTarjetasCliente, dtoVenta.getInstitucion(), consecutivo.toString(), ConstantesBD.acronimoSi, ConstantesBD.acronimoSi);

		/*
		 * 5. Se registran los beneficiarios de la tarjeta
		 */
		boolean consecutivoSerialAutomatico=false;
		int i=0;
		DtoTarjetaCliente dtoTarjetaCliente=new DtoTarjetaCliente();
		dtoTarjetaCliente.setCodigoPk(dtoVenta.getTipoTarjeta());
		int consecutivoSerial=TarjetaCliente.cargar(dtoTarjetaCliente).get(0).getConsecutivoSerial();
		
		for(DtoBeneficiarioCliente dtoBeneficiario:dtoVenta.getListaBeneficiarios())
		{
			if(!dtoBeneficiario.isTieneTarjetaPrevia())
			{
				BeneficiarioTarjetaCliente beneficiario=new BeneficiarioTarjetaCliente();
				
				beneficiario.setVentaTarjetaCliente(venta);
				
				if(usarSeriales)
				{
					beneficiario.setSerial(Long.parseLong(dtoBeneficiario.getSerial()));
				}
				else{
					consecutivoSerialAutomatico=true;
					beneficiario.setSerial(consecutivoSerial+i);
					beneficiario.setNumTarjeta(dtoBeneficiario.getNumTarjeta());
				}
				
				beneficiario.setIndicativoPrincipal(dtoBeneficiario.getIndicativoPrincipal());
				
				// Si se genera recibo de caja o el valor es cero, debe quedar activa
				if(generarReciboCaja || dtoVenta.getValorTotalTarjetas()==0)
				{
					beneficiario.setEstadoTarjeta(ConstantesIntegridadDominio.acronimoEstadoActivo);
				}
				else
				{
					beneficiario.setEstadoTarjeta(ConstantesIntegridadDominio.acronimoEstadoInactivo);
				}
				
				beneficiario.setTiposTarjCliente(tipoTarjeta);
				
				beneficiario.setFechaModifica(fecha);
				beneficiario.setHoraModifica(hora);
				
				// FIXME Usar usuarioOrm (Arreglar referencia en BD)
				beneficiario.setUsuarioModifica(dtoVenta.getUsuarioModifica());
				
				beneficiario.setInstituciones(institucion);
				
				// Desde la venta siempre será No aliado
				beneficiario.setIndicadorAlidado(ConstantesBD.acronimoNo);
				
				beneficiarioDao.modificar(beneficiario);

				i++;

				/*
				 * 5a. Asocio paciente y beneficiarios
				 */
				ArrayList<Errores> erroresBeneficiario=esValidoBeneficiario(dtoBeneficiario);
				dtoBeneficiario.setErrores(erroresBeneficiario);
				int codigoPaciente=0;
				if(erroresBeneficiario.isEmpty() && !UtilidadTexto.isEmpty(dtoBeneficiario.getDtoPersonas().getTipoIdentificacion()))
				{
					IPacientesMundo mundoPacientes=ManejoPacienteFabricaMundo.crearPacientesMundo();
					DtoPersonas paciente=mundoPacientes.obtenerPaciente(dtoBeneficiario.getDtoPersonas().getNumeroIdentificacion(), dtoBeneficiario.getDtoPersonas().getTipoIdentificacion());
					if(paciente==null)
					{
						IPersonas personaMundo=com.servinte.axioma.mundo.fabrica.AdministracionFabricaMundo.crearPersonasMundo();
						
						DtoPersonas persona=personaMundo.buscarPersona(dtoBeneficiario.getDtoPersonas());
						if(persona==null)
						{
							DtoPersonas dtoPersona=dtoBeneficiario.getDtoPersonas();
							Personas personaOrm=personaMundo.attachDirty(dtoPersona);
							
							codigoPaciente=crearPaciente(mundoPacientes, personaOrm, dtoVenta.getInstitucion());
						}
						else
						{
							Personas personaOrm=new Personas();
							personaOrm.setCodigo(persona.getCodigo());
							codigoPaciente=crearPaciente(mundoPacientes, personaOrm, dtoVenta.getInstitucion());
						}
						paciente=mundoPacientes.obtenerPaciente(dtoBeneficiario.getDtoPersonas().getNumeroIdentificacion(), dtoBeneficiario.getDtoPersonas().getTipoIdentificacion());
					}
					else
					{
						codigoPaciente=paciente.getCodigo();
					}
					
					BeneficiarioTcPaciente beneficiarioPaciente=new BeneficiarioTcPaciente();
					beneficiarioPaciente.setBeneficiarioTarjetaCliente(beneficiario);
					
					Pacientes pacienteOrm=new Pacientes();
					pacienteOrm.setCodigoPaciente(codigoPaciente);
					beneficiarioPaciente.setPacientes(pacienteOrm);
					
					IBeneficiarioTcPacienteDAO beneficiarioTcPacienteDAO=VentaTarjetaFabricaDAO.crearBeneficiarioTcPacienteDAO();
					beneficiarioTcPacienteDAO.modificar(beneficiarioPaciente);
					
					/*
					 * 6. Asociar el convenio al paciente
					 * FIXME verificar si ya existe
					 */
					DtoConvenio convenio=new DtoConvenio();
					convenio.setCodigo(dtoVenta.getConvenioTipoTarjeta());
					ArrayList<DtoContrato> listaContratosTarjeta=ConveniosFabricaMundo.crearConveniosMundo().listarContratosConvenio(convenio);
					if(listaContratosTarjeta!=null && listaContratosTarjeta.size()>0)
					{
						DtoContrato contrato=listaContratosTarjeta.get(0);
						ConveniosIngresoPacienteDelegate conveniosIngresoPacienteDelegate=new ConveniosIngresoPacienteDelegate();
						ConveniosIngresoPaciente convenioVerificacion=conveniosIngresoPacienteDelegate.obtenerConvenioIngresoPacientePorContrato(paciente.getCodigo(), contrato.getCodigo());
						if(convenioVerificacion==null)
						{
							/*
							 * 7. Se verifica si el convenio está parametrizado como por defecto
							 */
							DtoSeccionConvenioPaciente dtoSeccionConvenioPaciente=new DtoSeccionConvenioPaciente();
							dtoSeccionConvenioPaciente.setPorDefecto(ConstantesBD.acronimoNoChar);
							ArrayList<HashMap<String, Object>> listaConveniosPorDefecto=ValoresPorDefecto.getConveniosAMostrarPresupuestoOdo();
							for(HashMap<String, Object> convenioMapa:listaConveniosPorDefecto)
							{
								if(Integer.parseInt(convenioMapa.get("codigoContrato")+"")==contrato.getCodigo());
								{
									dtoSeccionConvenioPaciente.setPorDefecto(ConstantesBD.acronimoSiChar);
								}
							}
							dtoSeccionConvenioPaciente.setCodigoContrato(contrato.getCodigo()+"");
							dtoSeccionConvenioPaciente.setActivo(ConstantesBD.acronimoSi);
							IngresoPacienteOdontologia pacienteOdontologia=new IngresoPacienteOdontologia();
							ConveniosIngresoPaciente conveniosIngresoPaciente=pacienteOdontologia.construirConvenioPaciente(pacienteOrm, usuarioModificaOrm, hora, fecha, dtoSeccionConvenioPaciente);
							conveniosIngresoPacienteDelegate.attachDirty(conveniosIngresoPaciente);
						}
					}
					else
					{
						errores.add(new Errores("VentaTarjetaClienteForm.error.noContratoVigente"));
					}

				}
				else{
					errores.addAll(erroresBeneficiario);				
				}
			}

		}
		
		/*
		 * 7. Modificar el consecutivo serial del tipo de tarjeta
		 */
		if(consecutivoSerialAutomatico)
		{
			Long codigoTipoTarjeta=((Double)dtoVenta.getTipoTarjeta()).longValue();
			new TiposTarjClienteDelegate().modificarConsecutivoSerialDesdeVenta(codigoTipoTarjeta, new Long(consecutivoSerial+i));
		}
		
		/*
		 * 8. Generación del recibo de caja
		 */
		if(generarReciboCaja)
		{
			this.generarReciboCaja(dtoVenta, dtoFactura, dtoCompradorTarjeta);
		}
		
		return errores;
	}

	/**
	 * Crear paciente en la BD
	 * @param mundoPacientes {@link IPacientesMundo} Datos del paciente
	 * @param personaOrm {@link Personas} Personas
	 * @param institucion 
	 * @return código del paciente ingresado
	 */
	private int crearPaciente(IPacientesMundo mundoPacientes,Personas personaOrm, int institucion)
	{
		Pacientes pacienteOrm=new Pacientes();
		pacienteOrm.setPersonas(personaOrm);
		pacienteOrm.setEstaVivo(true);
		pacienteOrm.setGrupoPoblacional(ConstantesIntegridadDominio.acronimoOtrosGruposPoblacionales);
		pacienteOrm.setActivo(ConstantesBD.acronimoSi);
		mundoPacientes.attachDirty(pacienteOrm, institucion);
		return pacienteOrm.getCodigoPaciente();
	}

	/**
	 * Validar todos los campos requeridos del beneficiario
	 * @param dtoBeneficiario {@link DtoBeneficiarioCliente} Datos del beneficiario
	 * @return Lista de posibles {@link Errores} del beneficiario
	 */
	private ArrayList<Errores> esValidoBeneficiario(DtoBeneficiarioCliente dtoBeneficiario) {
		ArrayList<Errores> errores=new ArrayList<Errores>();
		if(!UtilidadTexto.isEmpty(dtoBeneficiario.getDtoPersonas().getTipoIdentificacion())){
			if(UtilidadTexto.isEmpty(dtoBeneficiario.getDtoPersonas().getNumeroIdentificacion())){
				errores.add(new Errores("errors.camposRequeridos"));
				return errores;
			}
			if(UtilidadTexto.isEmpty(dtoBeneficiario.getDtoPersonas().getPrimerNombre())){
				errores.add(new Errores("errors.required", new String[]{"VentaTarjetaClienteForm.primerNombre"}, Errores.Tipo.ERROR));
				return errores;
			}
			if(UtilidadTexto.isEmpty(dtoBeneficiario.getDtoPersonas().getPrimerApellido())){
				errores.add(new Errores("errors.camposRequeridos"));
				return errores;
			}
		}
		return errores;
	}


	/**
	 * Crear un deudor con base a los datos del comprador de la tarjeta
	 * @param dtoCompradorTarjeta {@link DtoPersonas} Datos del comprador
	 * @param usuario 
	 */
	private Deudores crearDeudor(DtoPersonas dtoCompradorTarjeta, Usuarios usuario) {
		DeudoresDelegate deudorDelegate=new DeudoresDelegate();
		Deudores deudor=new Deudores();
		Pacientes paciente=new Pacientes();
		paciente.setCodigoPaciente(dtoCompradorTarjeta.getCodigo());
		deudor.setPacientes(paciente);
		
		Instituciones institucion=new Instituciones();
		institucion.setCodigo(dtoCompradorTarjeta.getInstitucion());
		deudor.setInstituciones(institucion);
		
		deudor.setFechaModifica(UtilidadFecha.getFechaActualTipoBD());
		deudor.setHoraModifica(UtilidadFecha.getHoraActual());
		
		deudor.setUsuarios(usuario);
		
		deudor.setTipo(ConstantesIntegridadDominio.acronimoPaciente);
		
		deudor.setEsEmpresa(ConstantesBD.acronimoNo);

		deudor.setActivo(ConstantesBD.acronimoSiChar);
		
		deudorDelegate.attachDirty(deudor);
		
		deudor.getCodigo();
		
		UtilidadTransaccion.getTransaccion().commit();
		
		return deudor;
	}

	/**
	 * Generar el recibo de caja correspondiente a la venta de la tarjeta
	 * @param dtoVenta {@link DtoVentaTarjetasCliente} Dto con la información
	 */
	@SuppressWarnings({"unchecked", "unused"}) 
	private void generarReciboCaja(DtoVentaTarjetasCliente dtoVenta, DtoFacturaVaria dtoFactura, DtoPersonas dtoComprador) {
		IRecibosCajaMundo reciboCajaMundo=TesoreriaFabricaMundo.crearRecibosCajaMundo();
		RecibosCaja dtoRecibo=new RecibosCaja();

		String consecutivo =null;
		boolean manejaConsecutivoTesoreriaXCentroAtencion=UtilidadTexto.getBoolean(ValoresPorDefecto.getManejaConsecutivosTesoreriaPorCentroAtencion(dtoVenta.getInstitucion()));
		if(manejaConsecutivoTesoreriaXCentroAtencion)
		{
			Connection con=HibernateUtil.obtenerConexion();
			consecutivo=ConsecutivosCentroAtencion.incrementarConsecutivoXCentroAtencion(
					dtoFactura.getCentroAtencion(), 
					EmunConsecutivosTesoreriaCentroAtencion.ReciboCaja.getNombreConsecutivoBaseDatos(), dtoVenta.getInstitucion()).toString();
		}
		else
		{
			consecutivo=UtilidadBD.obtenerValorConsecutivoDisponible(ConstantesBD.nombreConsecutivoReciboCaja,dtoVenta.getInstitucion());
		}
		
		dtoVenta.setNumeroReciboCaja(Integer.parseInt(consecutivo)); 
		String codigo=UtilidadBD.obtenerSiguienteValorSecuencia("tesoreria.seq_recibos_caja")+"";
		RecibosCajaId id=new RecibosCajaId(codigo, dtoVenta.getInstitucion());
		dtoRecibo.setId(id);
		dtoRecibo.setConsecutivoRecibo(consecutivo);
		dtoRecibo.setRecibidoDe(dtoComprador.getNombreCompleto());
		dtoRecibo.setFecha(UtilidadFecha.getFechaActualTipoBD());
		dtoRecibo.setHora(UtilidadFecha.getHoraActualFormatoDate());
		dtoRecibo.setContabilizado(ConstantesBD.acronimoNo);
		
		CentroAtencion centroAtencion=new CentroAtencion();
		centroAtencion.setConsecutivo(dtoFactura.getCentroAtencion());
		dtoRecibo.setCentroAtencion(centroAtencion);
		
		CajasCajeros cajas=new CajasCajeros();
		CajasCajerosId cajasId=new CajasCajerosId(dtoVenta.getCaja().getConsecutivo(), dtoVenta.getUsuarioModifica());
		cajas.setId(cajasId);
		cajas.setCajas(dtoVenta.getCaja());
		dtoRecibo.setCajasCajeros(cajas);
		
		EstadosRecibosCaja estado=new EstadosRecibosCaja();
		estado.setCodigo(ConstantesBD.codigoEstadoReciboCajaRecaudado);
		dtoRecibo.setEstadosRecibosCaja(estado);
		
		//dtoRecibo.setInstituciones();
		
		reciboCajaMundo.generarReciboCaja(dtoRecibo);
		
		DetalleConceptosRc detalleConceptos=new DetalleConceptosRc();

		ConceptosIngTesoreriaId idConcepto=null;
		String conceptoPorDefecto=ValoresPorDefecto.getConceptoIngresoFacturasVarias(dtoVenta.getInstitucion());
		idConcepto=new ConceptosIngTesoreriaId(new BigDecimal(conceptoPorDefecto).intValue()+"", dtoVenta.getInstitucion());
		ConceptosIngTesoreria concepto=new ConceptosIngTesoreriaHome().findById(idConcepto);
		
		detalleConceptos.setConceptosIngTesoreria(concepto);
		detalleConceptos.setRecibosCaja(reciboCajaMundo.findById(dtoRecibo.getId()));
		detalleConceptos.setDocSoporte(dtoFactura.getCodigoFacturaVaria()+"");
		DetalleConceptosRcHome dh=new DetalleConceptosRcHome();
		dtoRecibo.getDetalleConceptosRcs().add(detalleConceptos);
		//dh.attachDirty(detalleConceptos);
		HibernateUtil.endTransaction();
		Connection con=HibernateUtil.obtenerConexion();
		
		RecibosCajaDao reciboCajaDao=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getRecibosCajaDao();
		
		/*
		 * Se envia también como documento de soporte el código de la factura Varia generada.
		 */
		reciboCajaDao.insertarDetalleConceptosRecibosCaja(con, dtoRecibo.getId().getNumeroReciboCaja(), dtoVenta.getInstitucion()+"", concepto.getId().getCodigo(), dtoFactura.getCodigoFacturaVaria()+"", dtoVenta.getValorTotalTarjetas()+"", dtoComprador.getTipoIdentificacion(), dtoComprador.getNumeroIdentificacion(), dtoComprador.getNombreCompleto(), null, null, null, dtoVenta.getInstitucion(), ConstantesBD.codigoNuncaValido, ConstantesBD.codigoNuncaValido+"", ConstantesBD.codigoNuncaValido+"");

/*		
		DetallePagosRc detallePagos=new DetallePagosRc();
		detallePagos.setRecibosCaja(reciboCajaMundo.findById(dtoRecibo.getId()));
		FormasPago fp=new FormasPago();
		fp.setConsecutivo(dtoVenta.getFormaPago().getConsecutivo());
		detallePagos.setFormasPago(fp);
		detallePagos.setValor(dtoVenta.getValorTotalTarjetas());
		IDetallePagosRcDAO detallePagosDao=TesoreriaFabricaDAO.crearDetallePagosRcDAO();
*/	
		
		for(DtoInformacionFormaPago formaPago:dtoVenta.getListaInformacionFormasPago())
		{
			if(formaPago.isValido())
			{
				if(formaPago.getFormaPago().getTipoDetalle().getCodigo()==ConstantesBD.codigoTipoDetalleFormasPagoBono)
				{
					this.guardarDetalleBono(con, reciboCajaDao, formaPago, dtoRecibo.getId().getNumeroReciboCaja(), dtoVenta.getInstitucion());
				}
				else
				{
					int detPago=UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_detalle_pagos_rc");
					reciboCajaDao.insertarDetallePagoRC(con, detPago, dtoRecibo.getId().getNumeroReciboCaja(), dtoVenta.getInstitucion(), formaPago.getFormaPago().getConsecutivo()+"", formaPago.getValor()+"");
					
					if(formaPago.getFormaPago().getTipoDetalle().getCodigo()==ConstantesBD.codigoTipoDetalleFormasPagoCheque)
					{
						this.guardarDetalleCheque(con, reciboCajaDao, formaPago, detPago);
					}
					if(formaPago.getFormaPago().getTipoDetalle().getCodigo()==ConstantesBD.codigoTipoDetalleFormasPagoTarjeta)
					{
						this.guardarDetalleTarjeta(con, reciboCajaDao, formaPago, detPago);
					}
				}
			}
			
		}
		
		//detallePagosDao.attachDirty(detallePagos);
		
		UtilidadBD.cambiarUsoFinalizadoConsecutivo(con,ConstantesBD.nombreConsecutivoReciboCaja, dtoVenta.getInstitucion(), consecutivo.toString(), ConstantesBD.acronimoSi, ConstantesBD.acronimoSi);
		

	}

	/**
	 * Guardar la información para la forma de pago bono
	 * @param con Concexión con la BD
	 * @param reciboCajaDao Objeto para manejar la Persistencia
	 * @param formaPago Objeto co la información completa de la forma de pago
	 * @param numeroReciboCaja Número de recibo de caja Generado
	 * @param institucion Institución en la que se hizo la venta
	 * @author Juan David Ramírez
	 * @since 19 septiembre 2010
	 */
	private void guardarDetalleBono(Connection con, RecibosCajaDao reciboCajaDao, DtoInformacionFormaPago formaPago, String numeroReciboCaja, int institucion) {
		DtoDetallePagosBonos bonos=formaPago.getDetalleBonos();
		for(DtoBonoSerialValor bono:bonos.getSerialesBonos())
		{
			int detPago=UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_detalle_pagos_rc");
			reciboCajaDao.insertarDetallePagoRC(con, detPago, numeroReciboCaja, institucion, formaPago.getFormaPago().getConsecutivo()+"", bono.getValor()+"");
			reciboCajaDao.insertarMovimientoBono(con, bono.getSerial(), detPago+"", bonos.getObservaciones());
		}
	}

	/**
	 * Guardar la información para la forma de pago tarjeta
	 * @param con
	 * @param reciboCajaDao
	 * @param formaPago
	 * @param detPago
	 * @author Juan David Ramírez
	 * @since 18 septiembre 2010
	 */
	private void guardarDetalleTarjeta(Connection con,
			RecibosCajaDao reciboCajaDao, DtoInformacionFormaPago formaPago,
			int detPago) {
		String[] ciudadGirador=new String[3];
		if(UtilidadTexto.isEmpty(formaPago.getCuidadDepartamentoPaisCheque()))
		{
			ciudadGirador=formaPago.getCuidadDepartamentoPaisGirador().split(ConstantesBD.separadorSplit);
		}
		reciboCajaDao.insertarMovimientosTarjetas(
				con,
				detPago+"",
				formaPago.getCodigoTarjetaFinanciera()+"",
				formaPago.getNumeroTarjeta(),
				formaPago.getNumeroComprobante(),
				formaPago.getNumeroAutorizacion(),
				formaPago.getFecha(),
				formaPago.getValor()+"",
				formaPago.getNombre(),
				formaPago.getDireccion(),
				ciudadGirador[0],
				ciudadGirador[1],
				formaPago.getTelefono(),
				formaPago.getObservaciones(),
				ciudadGirador[2],
				formaPago.getFechaVencimiento(),
				formaPago.getCodigoSeguridad(),
				formaPago.getEntidadFinanciera());
		
	}

	/**
	 * Guardar la información para la forma de pago cheque
	 * @param con
	 * @param reciboCajaDao
	 * @param formaPago
	 * @param detPago
	 * @author Juan David Ramírez
	 * @since 17 septiembre 2010
	 */
	private void guardarDetalleCheque(Connection con, RecibosCajaDao reciboCajaDao,
			DtoInformacionFormaPago formaPago, int detPago) {
		String[] ciudadPlaza=new String[3];
		if(UtilidadTexto.isEmpty(formaPago.getCuidadDepartamentoPaisCheque()))
		{
			ciudadPlaza=formaPago.getCuidadDepartamentoPaisCheque().split(ConstantesBD.separadorSplit);
		}
		String[] ciudadGirador=new String[3];
		if(UtilidadTexto.isEmpty(formaPago.getCuidadDepartamentoPaisCheque()))
		{
			ciudadGirador=formaPago.getCuidadDepartamentoPaisGirador().split(ConstantesBD.separadorSplit);
		}
		reciboCajaDao.insertarMovimientoCheque(con, 
				detPago+"", 
				formaPago.getNumeroCheque(), 
				formaPago.getCodigoBanco()+"",
				formaPago.getNumeroCuenta(),
				ciudadPlaza[0],
				ciudadPlaza[1],
				UtilidadFecha.conversionFormatoFechaABD(formaPago.getFecha()),
				formaPago.getValor()+"",
				formaPago.getNombre(),
				formaPago.getDireccion(),
				ciudadGirador[0],
				ciudadGirador[1],
				formaPago.getTelefono(),
				formaPago.getObservaciones(),
				ciudadPlaza[2],
				ciudadGirador[2],
				formaPago.getNumeroAutorizacion());
	}


	/**
	 * Generar la factura varia relacionada con la venta de tarjetas
	 * @param dtoVenta {@link DtoVentaTarjetasCliente} Dto con la información a almacenar
	 * @param usuario Usuario que genera la factura
	 */
	private void generarFacturaVaria(DtoVentaTarjetasCliente dtoVenta, DtoFacturaVaria dtoFacturaVaria) {
		
		IFacturasVariasMundo facturasVariasMundo=FacturasVariasMundoFabrica.crearFacturasVariasMundo();

		String fecha=UtilidadFecha.getFechaActual();
		
		dtoFacturaVaria.setEstadoFactura(ConstantesIntegridadDominio.acronimoEstadoAprobado);
		dtoFacturaVaria.setFecha(fecha);
		dtoFacturaVaria.setFechaAprobacion(fecha);
		dtoFacturaVaria.setUsuarioModifica(dtoVenta.getUsuarioModifica());
		dtoFacturaVaria.setValorFactura(new BigDecimal(dtoVenta.getValorTotalTarjetas()));
		
		facturasVariasMundo.generarFacturaVaria(dtoFacturaVaria);
	}


	@Override
	public void guardarVentaEmpresarial(VentaTarjetaCliente objeto) {
		// FIXME Hacer la venta empresarial
	}

	@Override
	public void modificar(VentaTarjetaCliente objeto) {
		ventaDAO.modificar(objeto);
	}

	@Override
	public ArrayList<DtoFormaPago> listarFormasPagoActivas() {
		IFormasPagoMundo formasPagoMundo=TesoreriaFabricaMundo.crearFormasPagoMundo();
		DtoFormaPago formasPago=new DtoFormaPago();
		formasPago.setActivo(true);
		return (ArrayList<DtoFormaPago>)formasPagoMundo.obtenerFormasPagos(formasPago);
	}

}
