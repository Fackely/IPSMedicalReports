/**
 * 
 */
package com.princetonsa.mundo.odontologia;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;

import util.ConstantesBD;
import util.InfoDatosDouble;
import util.InfoDatosInt;
import util.UtilidadFecha;
import util.facturacion.InfoTarifaVigente;
import util.odontologia.InfoDetallePaquetePresupuesto;
import util.odontologia.InfoPaquetesPresupuesto;
import util.odontologia.InfoPresupuestoXConvenioProgramaServicio;
import util.odontologia.InfoServiciosProgramaPaquetePresupuesto;
import util.odontologia.InfoTarifaServicioPresupuesto;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dto.odontologia.DtoAgrupacionHallazgoSuperficie;
import com.princetonsa.dto.odontologia.DtoInfoFechaUsuario;
import com.princetonsa.dto.odontologia.DtoListaCalculoCantidadesPresupuesto;
import com.princetonsa.dto.odontologia.DtoPresuOdoProgServ;
import com.princetonsa.dto.odontologia.DtoPresupuestoDetalleServiciosProgramaDao;
import com.princetonsa.dto.odontologia.DtoPresupuestoOdoConvenio;
import com.princetonsa.dto.odontologia.DtoPresupuestoPaquetes;
import com.princetonsa.dto.odontologia.DtoPresupuestoPiezas;
import com.princetonsa.dto.odontologia.DtoPresupuestoTotalConvenio;
import com.princetonsa.enu.general.EnumTipoModificacion;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.cargos.Cargos;
import com.princetonsa.mundo.cargos.CargosOdon;
import com.princetonsa.mundo.cargos.EsquemaTarifario;
import com.servinte.axioma.fwk.exception.IPSException;

/**
 * 
 * @author Wilson Rios 
 *
 * Jun 6, 2010 - 9:04:28 AM
 */
public class PresupuestoPaquetes 
{
	/**
	 * 
	 * Metodo para insertar el encabezado
	 * @param con
	 * @param dto
	 * @return
	 * @author   Wilson Rios
	 * @version  1.0.0 
	 * @see
	 */
	public static BigDecimal insertar(Connection con, DtoPresupuestoPaquetes dto)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPresupuestoPaquetesDao().insertar(con, dto);
	}
	
	/**
	 * 
	 * Metodo para eliminar 
	 * @param con
	 * @param codigoPk
	 * @return
	 * @author   Wilson Rios
	 * @version  1.0.0 
	 * @see
	 */
	public static boolean eliminar(Connection con, DtoPresupuestoPaquetes dto)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPresupuestoPaquetesDao().eliminar(con, dto);
	}
	
	/**
	 * 
	 * Metodo para cargar 
	 * @param con
	 * @param dto
	 * @return
	 * @author   Wilson Rios
	 * @version  1.0.0 
	 * @see
	 */
	public static ArrayList<DtoPresupuestoPaquetes> cargar(Connection con, DtoPresupuestoPaquetes dto)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPresupuestoPaquetesDao().cargar(con, dto);
	}

	/**
	 * 
	 * Metodo para hacer un merge entre lo que ya habia escogido y lo nuevo
	 * @param paquetesForma
	 * @param paquetesConsultados
	 * @return
	 * @author   Wilson Rios
	 * @version  1.0.0 
	 * @see
	 */
	public static ArrayList<InfoPaquetesPresupuesto> cargarPaquetesAplicanPresupuestoActualizado(ArrayList<InfoPaquetesPresupuesto> paquetesForma, ArrayList<InfoPaquetesPresupuesto> paquetesConsultados)
	{
		Iterator<InfoPaquetesPresupuesto> iterador= paquetesForma.iterator();
		///primero eliminamos los no existentes
		while(iterador.hasNext())
		{
			boolean existe= false;
			
			int detallePkPaqueteCONVENIO= iterador.next().getDetallePkPaqueteOdonCONVENIO();
			
			for(InfoPaquetesPresupuesto paqueteConsultado: paquetesConsultados)
			{
				if(detallePkPaqueteCONVENIO==paqueteConsultado.getDetallePkPaqueteOdonCONVENIO())
				{
					existe=true;
					break;
				}
			}
			if(!existe)
			{
				iterador.remove();
			}
		}
		
		///luego adicionamos eliminamos los no existentes
		for(InfoPaquetesPresupuesto paqueteConsultado: paquetesConsultados)
		{
			boolean existe= false;
			for(InfoPaquetesPresupuesto paqueteForma: paquetesForma)
			{
				if(paqueteConsultado.getDetallePkPaqueteOdonCONVENIO()==paqueteForma.getDetallePkPaqueteOdonCONVENIO())
				{
					existe=true;
					break;
				}
			}
			if(!existe)
			{
				paquetesForma.add(paqueteConsultado);
			}
		}
		return paquetesForma;
	}
	
	/**
	 * 
	 * Metodo para encargado de cargar toda la estructura de los paquetes que apliquen para el presuepuesto
	 * @param listaProgramasServicios
	 * @return
	 * @author   Wilson Rios
	 * @version  1.0.0 
	 * @param listaConvenios 
	 * @param arrayList 
	 * @see
	 */
	public static ArrayList<InfoPaquetesPresupuesto> cargarPaquetesAplicanPresupuesto(ArrayList<DtoPresuOdoProgServ> listaProgramasServicios, ArrayList<DtoPresupuestoTotalConvenio> listaConvenios, ArrayList<InfoPaquetesPresupuesto> listaPaquetesForma) throws IPSException 
	{
		ArrayList<InfoPaquetesPresupuesto> listaPaquetesAplica= new ArrayList<InfoPaquetesPresupuesto>();
		//comenzamos cargando los paquetes que apliquen por cada convenio
		for(DtoPresupuestoTotalConvenio dtoConvenio: listaConvenios)
		{
			ArrayList<InfoPaquetesPresupuesto> listaPaquetes=  cargarInfoPaquetesSinTarifas(dtoConvenio.getConvenio().getCodigo(), dtoConvenio.getContrato(), UtilidadFecha.getFechaActual());
			
			//con la lista de paquetes de ese convenio contrato entonces evaluamos si podemos paquetizar
			for(InfoPaquetesPresupuesto paquete: listaPaquetes)
			{
				boolean esPaqueteConvenioExistentePresupuestoBool= esPaqueteConvenioExistentePresupuesto(listaPaquetesForma, paquete);  
				int filtroConvenio= (!esPaqueteConvenioExistentePresupuestoBool)?dtoConvenio.getConvenio().getCodigo():-1;
				if(aplicaPaqueteAlPresupuesto(paquete, listaProgramasServicios, filtroConvenio))
				{
					listaPaquetesAplica.add(paquete);
				}
			}
		}
		
		calcularTarifaPaquete(listaPaquetesAplica);
		
		//ahora verificamos si existe cruce de programas para el mismo convenio
		for(InfoPaquetesPresupuesto paquete: listaPaquetesAplica)
		{
			for(InfoPaquetesPresupuesto paqueteInterno: listaPaquetesAplica)
			{
				if(paquete.getConvenio().getCodigo()==paqueteInterno.getConvenio().getCodigo())
				{	
					if(paquete.getDetallePkPaqueteOdonCONVENIO()!=paqueteInterno.getDetallePkPaqueteOdonCONVENIO())
					{
						for(InfoDetallePaquetePresupuesto programa: paquete.getListaProgramas())
						{
							for(InfoDetallePaquetePresupuesto programaInterno: paqueteInterno.getListaProgramas())
							{
								if(programa.getCodigoPkPrograma().doubleValue()==programaInterno.getCodigoPkPrograma().doubleValue())
								{
									if(!paquete.getListaDetallesPkPaqueteOdonCONVENIOCruceProgramas().contains(paqueteInterno.getDetallePkPaqueteOdonCONVENIO()))
									{	
										paquete.getListaDetallesPkPaqueteOdonCONVENIOCruceProgramas().add(paqueteInterno.getDetallePkPaqueteOdonCONVENIO());
									}	
								}
							}
						}
					}
				}	
			}
		}
		return listaPaquetesAplica;
	}

	/**
	 * Metodo para .......
	 * @param listaPaquetesAplica
	 * @author   Wilson Rios
	 * @version  1.0.0 
	 * @see      
	 */
	private static void calcularTarifaPaquete(
			ArrayList<InfoPaquetesPresupuesto> listaPaquetesAplica) throws IPSException {
		//con la lista de paquetes a las que aplica entonces le cargamos la tarifa de los servicios del programa
		for(InfoPaquetesPresupuesto paquete: listaPaquetesAplica)
		{
			for(InfoDetallePaquetePresupuesto programa: paquete.getListaProgramas())
			{
				for(InfoServiciosProgramaPaquetePresupuesto servicio: programa.getListaServicios())
				{
					int codigoTarifario= EsquemaTarifario.obtenerTarifarioOficialXCodigoEsquemaTar(paquete.getEsquemaTarifario());
					InfoTarifaVigente infoTarifa= Cargos.obtenerTarifaBaseServicio(codigoTarifario, servicio.getServicio(), paquete.getEsquemaTarifario(), "");
					servicio.setExisteTarifa(infoTarifa.isExiste());
					
					if(infoTarifa.isExiste())
					{	
						servicio.setValorBase(new BigDecimal(infoTarifa.getValorTarifa()));
					}
					else
					{
						servicio.setValorBase(BigDecimal.ZERO);
					}
				}
			}
		}
	}
	
	/**
	 * 
	 * Metodo para evaluar si el paquete ya fue adicionado al presupuesto para el convenio
	 * @param listaPaquetesForma
	 * @param paquete
	 * @return
	 * @author   Wilson Rios
	 * @version  1.0.0 
	 * @see
	 */
	private static boolean esPaqueteConvenioExistentePresupuesto(ArrayList<InfoPaquetesPresupuesto> listaPaquetesForma,	InfoPaquetesPresupuesto paquete) 
	{
		boolean retorna= false;
		for(InfoPaquetesPresupuesto paqueteForma: listaPaquetesForma)
		{
			if(paqueteForma.isSeleccionado())
			{
				if(paqueteForma.getDetallePkPaqueteOdonCONVENIO()==paquete.getDetallePkPaqueteOdonCONVENIO())
				{
					retorna= true;
					break;
				}
			}
		}
		return retorna;
	}

	/**
	 * 
	 * Metodo para cargar los objetos de paquetes presupuesto pero sin tarifas
	 * @param codigo
	 * @param fechaVigencia
	 * @return
	 * @author   Wilson Rios
	 * @version  1.0.0 
	 * @param contrato 
	 * @see
	 */
	public static ArrayList<InfoPaquetesPresupuesto> cargarInfoPaquetesSinTarifas(int convenio, int contrato, String fechaVigenciaApp) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPresupuestoPaquetesDao().cargarInfoPaquetesSinTarifas(convenio, contrato, fechaVigenciaApp);
	}
	
	/**
	 * 
	 * Metodo para determinar si un paquete aplica al presuepuesto
	 * @param paquete
	 * @param listaProgramasServicios
	 * @return
	 * @author   Wilson Rios
	 * @version  1.0.0 
	 * @param i 
	 * @see
	 */
	public static boolean aplicaPaqueteAlPresupuesto(	InfoPaquetesPresupuesto paquete,ArrayList<DtoPresuOdoProgServ> listaProgramasServicios, int convenio) 
	{
		//recorremos uno a uno los programas del paquete y verificamos si existen x cantidaddes en el epresupuesto
		boolean retorna= false;
		for(InfoDetallePaquetePresupuesto programaPaq: paquete.getListaProgramas())
		{
			boolean existePrograma= false;
			int cantidad= obtenerCantidadPrograma(programaPaq.getCodigoPkPrograma(), listaProgramasServicios, convenio);
			existePrograma= (cantidad>=programaPaq.getCantidad());
			if(!existePrograma)
			{
				//no seguimos buscando porque no existe
				return false;
			}
			retorna= true;
		}
		return retorna;
	}

	/**
	 * 
	 * Metodo para obtener la cantidad de un programa en el presupuesto
	 * @param codigoPkPrograma
	 * @param listaProgramasServicios
	 * @return
	 * @author   Wilson Rios
	 * @version  1.0.0 
	 * @param convenio 
	 * @see
	 */
	private static int obtenerCantidadPrograma(BigDecimal codigoPkPrograma,ArrayList<DtoPresuOdoProgServ> listaProgramasServicios, int convenio) 
	{
		int cantidad=0;
		for(DtoPresuOdoProgServ dto: listaProgramasServicios)
		{
			if(dto.getPrograma().getCodigo().doubleValue()==codigoPkPrograma.doubleValue() && !dto.getPaquetizadoXConvenio(convenio))
			{
				cantidad+= dto.getCantidad();
			}
		}
		return cantidad;
	}

	/**
	 * 
	 * Metodo que devuelve la paquetizacion de los programas, desarmando el agrupamiento y retornandolos en cantidades 1 a 1
	 * @param paquete
	 * @param listaProgramasServicios
	 * @author   Wilson Rios
	 * @version  1.0.0 
	 * @param convenio 
	 * @see
	 */
	public static void desarmarProgramasAgrupadosCantidad1a1(InfoPaquetesPresupuesto paquete, ArrayList<DtoPresuOdoProgServ> listaProgramasServicios, UsuarioBasico usuario) 
	{
		//ESTA LISTA VA HA CONTENER TODOS LOS NUEVOS QUE SE VAN ADICIONANDO CON CANTIDAD 1 Y VALOR TARIFA DEL PAQUETE
		ArrayList<DtoPresuOdoProgServ> listaConNuevosProgramasPaq= new ArrayList<DtoPresuOdoProgServ>();
		
		//1. tomamos uno a uno los programas del paquete
		for(InfoDetallePaquetePresupuesto programaPaq: paquete.getListaProgramas())
		{
			//CONTADOR CON LA CANTIDAD QUE VOY PAQUETIZANDO
			int cantidadPaquetizada=0;
			
			Iterator<DtoPresuOdoProgServ> iteradorProgPresupuesto= listaProgramasServicios.iterator();
			///primero eliminamos los no existentes
			while(iteradorProgPresupuesto.hasNext())
			{
				DtoPresuOdoProgServ programaPresupuesto= iteradorProgPresupuesto.next();
			
				if(programaPresupuesto.getPrograma().getCodigo()==programaPaq.getCodigoPkPrograma().doubleValue() 
					&& !programaPresupuesto.getPaquetizadoXConvenio(paquete.getConvenio().getCodigo()))
				{	
					//cantidad faltante x paquetizar es la cantidad del programa del paquete menos la cantidad q paquetice
					int cantidadFaltantePaquetizar= programaPaq.getCantidad()-cantidadPaquetizada;
				
					//si la cantidad de los programas AGRUPADOS del presupuesto es menor o igual a la cantidda faltante
					//entonces debemos eliminarlo porque se va ha remplazar con los nuevos, de lo contrario si es mayor 
					//ejemplo es cantidad 5 y el paquete es cantidad 3 entonces se paquetizan 3 nuevos y el programa actual
					//del presupuesto no se elimina y solmanete se le actualiza la cantidad a 2 (5-3).
					boolean eliminarPrograma= programaPresupuesto.getCantidad()<=cantidadFaltantePaquetizar;
					
					if(eliminarPrograma)
					{	
						for(int w=0; w<programaPresupuesto.getCantidad(); w++)
						{
							DtoPresuOdoProgServ programaNuevo= obtenerProgramaCantidad1TarifaPaquete(programaPresupuesto, programaPaq, paquete.getConvenio(), paquete.getContrato(), usuario, paquete.getDetallePkPaqueteOdonCONVENIO());
							listaConNuevosProgramasPaq.add(programaNuevo);
							cantidadPaquetizada++;
						}
						iteradorProgPresupuesto.remove();
					}	
					else
					{
						for(int w=0; w<cantidadFaltantePaquetizar; w++)
						{
							DtoPresuOdoProgServ programaNuevo= obtenerProgramaCantidad1TarifaPaquete(programaPresupuesto, programaPaq, paquete.getConvenio(), paquete.getContrato(), usuario, paquete.getDetallePkPaqueteOdonCONVENIO());
							listaConNuevosProgramasPaq.add(programaNuevo);
							cantidadPaquetizada++;
						}
						programaPresupuesto.setCantidad(programaPresupuesto.getCantidad()-cantidadFaltantePaquetizar);
						for(DtoAgrupacionHallazgoSuperficie dtoAgrupHallazgoSup: programaPresupuesto.getListaCalculoCantidades().getListaCalculoCantidades())
						{
							if(dtoAgrupHallazgoSup.getNumeroSuperficies()>0)
							{	
								dtoAgrupHallazgoSup.setContador(programaPresupuesto.getCantidad()*dtoAgrupHallazgoSup.getNumeroSuperficies());
							}
							else
							{
								dtoAgrupHallazgoSup.setContador(programaPresupuesto.getCantidad());
							}
						}
					}
					
					cantidadFaltantePaquetizar= programaPaq.getCantidad()-cantidadPaquetizada;
					if(cantidadFaltantePaquetizar==0)
					{
						break;
					}
				}	
			}
		}
		
		///finalmente adicionamos al objeto ppal todos los nuevos programas paquetizados
		for(DtoPresuOdoProgServ programaNuevo: listaConNuevosProgramasPaq)
		{	
			listaProgramasServicios.add(programaNuevo);
		}	
	}

	/**
	 * 
	 * Metodo para desagrupar la cantidad y retornar con la tarifa nueva
	 * @param programaPresupuesto
	 * @param programaPaq
	 * @return
	 * @author   Wilson Rios
	 * @version  1.0.0 
	 * @param convenio 
	 * @param contrato 
	 * @see
	 */
	private static DtoPresuOdoProgServ obtenerProgramaCantidad1TarifaPaquete( 	DtoPresuOdoProgServ programaPresupuesto,
																				InfoDetallePaquetePresupuesto programaPaq, 
																				InfoDatosInt convenio, 
																				int contrato,
																				UsuarioBasico usuario,
																				int codigoPkPaqueteOdonCONVENIO
																				) 
	{
		//SETEAMOS EL ENCABEZADO
		DtoPresuOdoProgServ dtoNuevo= new DtoPresuOdoProgServ();
		dtoNuevo.setCantidad(1);
		dtoNuevo.setCodigoPk(BigDecimal.ZERO);
		dtoNuevo.setEspecialidad(programaPresupuesto.getEspecialidad());
		dtoNuevo.setInclusion(programaPresupuesto.getInclusion());
		dtoNuevo.setPresupuesto(programaPresupuesto.getPresupuesto());
		dtoNuevo.setPrograma(new InfoDatosDouble( programaPresupuesto.getPrograma().getCodigo(), programaPresupuesto.getPrograma().getNombre(), programaPresupuesto.getPrograma().getDescripcion(),  programaPresupuesto.getPrograma().getActivo()));
		dtoNuevo.setServicio(programaPresupuesto.getServicio());
		dtoNuevo.setTipoModificacion(programaPresupuesto.getTipoModificacion());
		dtoNuevo.setUsuarioModifica(programaPresupuesto.getUsuarioModifica());
		
		//SETEAMOS LAS LISTA
		dtoNuevo.setListPresupuestoOdoConvenio(obtenerConveniosPaquetizados(programaPresupuesto.getListPresupuestoOdoConvenio(), programaPaq, convenio, contrato, usuario, codigoPkPaqueteOdonCONVENIO));
		dtoNuevo.setListPresupuestoPiezas(obtenerListaPiezasPaquetizadas(programaPresupuesto.getListPresupuestoPiezas(), programaPaq, convenio.getCodigo(), contrato));
		dtoNuevo.setListaCalculoCantidades(obtenerListaCantidadesPaquetizadas(programaPresupuesto.getListaCalculoCantidades(), programaPaq));
		
		return dtoNuevo;
	}

	/**
	 * 
	 * Metodo para obtener los convenios paquetizados
	 * @param listPresupuestoOdoConvenio
	 * @param programaPaq
	 * @return
	 * @author   Wilson Rios
	 * @version  1.0.0 
	 * @see
	 */
	private static ArrayList<DtoPresupuestoOdoConvenio> obtenerConveniosPaquetizados(
			ArrayList<DtoPresupuestoOdoConvenio> listPresupuestoOdoConvenio,
			InfoDetallePaquetePresupuesto programaPaq,
			InfoDatosInt convenio, 
			int contrato,
			UsuarioBasico usuario,
			int detallePkPaqueteOdonCONVENIO) 
	{
		ArrayList<DtoPresupuestoOdoConvenio> listaNueva= new ArrayList<DtoPresupuestoOdoConvenio>();
		for(DtoPresupuestoOdoConvenio convenioPresupuesto: listPresupuestoOdoConvenio)
		{
			DtoPresupuestoOdoConvenio dtoNuevo= new DtoPresupuestoOdoConvenio();
			//si es el mismo convenio contrato entonces seteamos los nuevos valores de tarifas
			if(convenioPresupuesto.getConvenio().getCodigo()==convenio.getCodigo()
				&& convenioPresupuesto.getContrato().getCodigo()==contrato)
			{
				dtoNuevo.setAdvertenciaBono("");
				dtoNuevo.setAdvertenciaPromocion("");
				dtoNuevo.setCodigoPK(BigDecimal.ZERO);
				//se deja igual el contrato - convenio
				dtoNuevo.setContratado(convenioPresupuesto.getContratado());
				dtoNuevo.setContrato(convenioPresupuesto.getContrato());
				dtoNuevo.setConvenio(convenioPresupuesto.getConvenio());
				dtoNuevo.setDescuentoComercialUnitario(BigDecimal.ZERO);
				dtoNuevo.setDetallePromocion(0);
				dtoNuevo.setErrorCalculoTarifa(programaPaq.getErroresTarifa());
				dtoNuevo.setPorcentajeDctoBono(0);
				dtoNuevo.setPorcentajeHonorarioPromocion(0.0);
				dtoNuevo.setPorcentajePromocion(0);
				dtoNuevo.setPresupuestoOdoProgServ(convenioPresupuesto.getPresupuestoOdoProgServ());
				dtoNuevo.setReservaAnticipo(convenioPresupuesto.getReservaAnticipo());
				dtoNuevo.setSeleccionadoBono(false);
				dtoNuevo.setSeleccionadoPorcentajeBono(false);
				dtoNuevo.setSeleccionadoPorcentajePromocion(false);
				dtoNuevo.setSeleccionadoPromocion(false);
				dtoNuevo.setSerialBono(BigDecimal.ZERO);
				dtoNuevo.setTipoModificacionCONVENIO(EnumTipoModificacion.NUEVO);
				dtoNuevo.setUsuarioModifica(convenioPresupuesto.getUsuarioModifica());
				dtoNuevo.setValorDescuentoBono(BigDecimal.ZERO);
				dtoNuevo.setValorDescuentoPromocion(BigDecimal.ZERO);
				dtoNuevo.setValorHonorarioPromocion(BigDecimal.ZERO);
				dtoNuevo.setValorUnitario(programaPaq.getValorUnitarioNetoPrograma());
				dtoNuevo.setListaDetalleServiciosPrograma(obtenerServiciosProgramaPaquetizados(convenioPresupuesto.getListaDetalleServiciosPrograma(), programaPaq.getListaServicios()));
				DtoPresupuestoPaquetes dtoPresuPaquete= new DtoPresupuestoPaquetes();
				dtoPresuPaquete.setCodigoPk(BigDecimal.ZERO);
				dtoPresuPaquete.setFHU(new DtoInfoFechaUsuario(usuario.getLoginUsuario()));
				dtoPresuPaquete.setDetallePaqueteOdontologicoConvenio(detallePkPaqueteOdonCONVENIO);
				dtoPresuPaquete.setPresupuesto(BigDecimal.ZERO);
				dtoPresuPaquete.setCodigoPaqueteMostrar(obtenerCodigoMostrarPaqueteOdontologico(programaPaq.getCodigoPkPaquete()));
				dtoNuevo.setDtoPresupuestoPaquete(dtoPresuPaquete);
			}
			else
			{
				dtoNuevo.setAdvertenciaBono(convenioPresupuesto.getAdvertenciaBono());
				dtoNuevo.setAdvertenciaPromocion(convenioPresupuesto.getAdvertenciaPromocion());
				dtoNuevo.setCodigoPK(convenioPresupuesto.getCodigoPK());
				dtoNuevo.setContratado(convenioPresupuesto.getContratado());
				dtoNuevo.setContrato(convenioPresupuesto.getContrato());
				dtoNuevo.setConvenio(convenioPresupuesto.getConvenio());
				dtoNuevo.setDescuentoComercialUnitario(convenioPresupuesto.getDescuentoComercialUnitario());
				dtoNuevo.setDetallePromocion(convenioPresupuesto.getDetallePromocion());
				dtoNuevo.setErrorCalculoTarifa(convenioPresupuesto.getErrorCalculoTarifa());
				dtoNuevo.setPorcentajeDctoBono(convenioPresupuesto.getPorcentajeDctoBono());
				dtoNuevo.setPorcentajeHonorarioPromocion(convenioPresupuesto.getPorcentajeHonorarioPromocion());
				dtoNuevo.setPorcentajePromocion(convenioPresupuesto.getPorcentajePromocion());
				dtoNuevo.setPresupuestoOdoProgServ(convenioPresupuesto.getPresupuestoOdoProgServ());
				dtoNuevo.setReservaAnticipo(convenioPresupuesto.getReservaAnticipo());
				dtoNuevo.setSeleccionadoBono(convenioPresupuesto.getSeleccionadoBono());
				dtoNuevo.setSeleccionadoPorcentajeBono(convenioPresupuesto.isSeleccionadoPorcentajeBono());
				dtoNuevo.setSeleccionadoPorcentajePromocion(convenioPresupuesto.isSeleccionadoPorcentajePromocion());
				dtoNuevo.setSeleccionadoPromocion(convenioPresupuesto.getSeleccionadoPromocion());
				dtoNuevo.setSerialBono(convenioPresupuesto.getSerialBono());
				dtoNuevo.setTipoModificacionCONVENIO(EnumTipoModificacion.NUEVO);
				dtoNuevo.setUsuarioModifica(convenioPresupuesto.getUsuarioModifica());
				dtoNuevo.setValorDescuentoBono(convenioPresupuesto.getValorDescuentoBono());
				dtoNuevo.setValorDescuentoPromocion(convenioPresupuesto.getValorDescuentoPromocion());
				dtoNuevo.setValorHonorarioPromocion(convenioPresupuesto.getValorHonorarioPromocion());
				dtoNuevo.setValorUnitario(convenioPresupuesto.getValorUnitario());
				dtoNuevo.setListaDetalleServiciosPrograma(obtenerServiciosProgramaPaquetizados(convenioPresupuesto.getListaDetalleServiciosPrograma(), null));
				dtoNuevo.setDtoPresupuestoPaquete(convenioPresupuesto.getDtoPresupuestoPaquete());
			}
			listaNueva.add(dtoNuevo);
		}
		return listaNueva;
	}

	/**
	 * 
	 * Metodo para cargar los servicios
	 * @param listaDetalleServiciosPrograma
	 * @param listaServicios
	 * @return
	 * @author   Wilson Rios
	 * @version  1.0.0 
	 * @see
	 */
	private static ArrayList<DtoPresupuestoDetalleServiciosProgramaDao> obtenerServiciosProgramaPaquetizados(
			ArrayList<DtoPresupuestoDetalleServiciosProgramaDao> listaDetalleServiciosPrograma,
			ArrayList<InfoServiciosProgramaPaquetePresupuesto> listaServiciosPaquete) 
	{
		ArrayList<DtoPresupuestoDetalleServiciosProgramaDao> listaNueva= new ArrayList<DtoPresupuestoDetalleServiciosProgramaDao>();
		for(DtoPresupuestoDetalleServiciosProgramaDao servicioPresupuesto: listaDetalleServiciosPrograma)
		{
			DtoPresupuestoDetalleServiciosProgramaDao dtoServicioNuevo= new DtoPresupuestoDetalleServiciosProgramaDao();
			if(listaServiciosPaquete!=null)
			{	
				for(InfoServiciosProgramaPaquetePresupuesto servicioPaquete: listaServiciosPaquete)
				{
					if(servicioPaquete.getServicio()==servicioPresupuesto.getServicio())
					{	
						dtoServicioNuevo.setCodigoPk(BigDecimal.ZERO);
						dtoServicioNuevo.setDctoComercialUnitario(BigDecimal.ZERO);
						dtoServicioNuevo.setErrorCalculoTarifa(servicioPaquete.isExisteTarifa()?"":"NO EXISTE TARIFA");
						dtoServicioNuevo.setFHU(servicioPresupuesto.getFHU());
						dtoServicioNuevo.setPorcentajeDctoBonoServicio(0);
						dtoServicioNuevo.setPorcentajeDctoPromocionServicio(0);
						dtoServicioNuevo.setPorcentajeHonorarioDctoPromocionServicio(0);
						dtoServicioNuevo.setPresupuestoOdoConvenio(BigDecimal.ZERO);
						dtoServicioNuevo.setPrograma(servicioPresupuesto.getPrograma());
						dtoServicioNuevo.setServicio(servicioPresupuesto.getServicio());
						dtoServicioNuevo.setValorDctoBonoServicio(BigDecimal.ZERO);
						dtoServicioNuevo.setValorDctoPromocionServicio(BigDecimal.ZERO);
						dtoServicioNuevo.setValorHonorarioDctoPromocionServicio(BigDecimal.ZERO);
						dtoServicioNuevo.setValorUnitarioServicio(servicioPaquete.getValorTotalNeto());
						listaNueva.add(dtoServicioNuevo);
						break;
					}	
				}
			}
			else
			{
				dtoServicioNuevo.setCodigoPk(servicioPresupuesto.getCodigoPk());
				dtoServicioNuevo.setDctoComercialUnitario(servicioPresupuesto.getDctoComercialUnitario());
				dtoServicioNuevo.setErrorCalculoTarifa(servicioPresupuesto.getErrorCalculoTarifa());
				dtoServicioNuevo.setFHU(servicioPresupuesto.getFHU());
				dtoServicioNuevo.setPorcentajeDctoBonoServicio(servicioPresupuesto.getPorcentajeDctoBonoServicio());
				dtoServicioNuevo.setPorcentajeDctoPromocionServicio(servicioPresupuesto.getPorcentajeDctoPromocionServicio());
				dtoServicioNuevo.setPorcentajeHonorarioDctoPromocionServicio(servicioPresupuesto.getPorcentajeHonorarioDctoPromocionServicio());
				dtoServicioNuevo.setPresupuestoOdoConvenio(servicioPresupuesto.getPresupuestoOdoConvenio());
				dtoServicioNuevo.setPrograma(servicioPresupuesto.getPrograma());
				dtoServicioNuevo.setServicio(servicioPresupuesto.getServicio());
				dtoServicioNuevo.setValorDctoBonoServicio(servicioPresupuesto.getValorDctoBonoServicio());
				dtoServicioNuevo.setValorDctoPromocionServicio(servicioPresupuesto.getValorDctoPromocionServicio());
				dtoServicioNuevo.setValorHonorarioDctoPromocionServicio(servicioPresupuesto.getValorHonorarioDctoPromocionServicio());
				dtoServicioNuevo.setValorUnitarioServicio(servicioPresupuesto.getValorUnitarioServicio());
				listaNueva.add(dtoServicioNuevo);
			}
		}
		return listaNueva;
	}

	/**
	 * 
	 * Metodo para obtener las cantidades paquetizadas
	 * @param listaCalculoCantidades
	 * @param programaPaq
	 * @return
	 * @author   Wilson Rios
	 * @version  1.0.0 
	 * @param arrayList 
	 * @see
	 */
	private static DtoListaCalculoCantidadesPresupuesto obtenerListaCantidadesPaquetizadas(
			DtoListaCalculoCantidadesPresupuesto listaCalculoCantidades,
			InfoDetallePaquetePresupuesto programaPaq) 
	{
		DtoListaCalculoCantidadesPresupuesto dtoListaNuevo= new DtoListaCalculoCantidadesPresupuesto();
		int indice=0;
		for(DtoAgrupacionHallazgoSuperficie agrupHallazgoSup: listaCalculoCantidades.getListaCalculoCantidades())
		{
			DtoAgrupacionHallazgoSuperficie dtoNuevo= new DtoAgrupacionHallazgoSuperficie();
			dtoNuevo.setContador(agrupHallazgoSup.getNumeroSuperficies());
			dtoNuevo.setNumeroSuperficies(agrupHallazgoSup.getNumeroSuperficies());
			dtoNuevo.setProgramaServicio(agrupHallazgoSup.getProgramaServicio());
			//@FIXME FALTA EL HALLAZGO
			//dtoNuevo.setHallazgo(hallazgo);
			dtoListaNuevo.getListaCalculoCantidades().add(dtoNuevo);
			indice++;
		}
		return dtoListaNuevo;
	}

	/**
	 * 
	 * Metodo para las piezas paqutizadas
	 * @param listPresupuestoPiezas
	 * @param programaPaq
	 * @return
	 * @author   Wilson Rios
	 * @version  1.0.0 
	 * @see
	 */
	private static ArrayList<DtoPresupuestoPiezas> obtenerListaPiezasPaquetizadas(
			ArrayList<DtoPresupuestoPiezas> listPresupuestoPiezas,
			InfoDetallePaquetePresupuesto programaPaq, int convenio, int contrato) 
	{
		ArrayList<DtoPresupuestoPiezas> listaNueva= new ArrayList<DtoPresupuestoPiezas>(); 
		int numSuperficiesAdicionadas=0;
		int numSupInicial=-2;
		Iterator<DtoPresupuestoPiezas> iteradorPiezaPresupuesto= listPresupuestoPiezas.iterator();
		
		while(iteradorPiezaPresupuesto.hasNext())
		{
			DtoPresupuestoPiezas piezaPresupuesto= iteradorPiezaPresupuesto.next();
			if(numSupInicial==-2)
			{
				numSupInicial= piezaPresupuesto.getNumSuperficies();
			}
			//si no contiene key convenio+"-"+contrato eso quiere decir que la podemos adicionar
			if(!piezaPresupuesto.getListaConvenioContratoPaquetiza().contains(convenio+"-"+contrato)
				&& piezaPresupuesto.getActivo() && piezaPresupuesto.getNumSuperficies()==numSupInicial)
			{
				DtoPresupuestoPiezas dtoNuevo= new DtoPresupuestoPiezas();
				piezaPresupuesto.getListaConvenioContratoPaquetiza().add(convenio+"-"+contrato);
				dtoNuevo.setActivo(true);
				dtoNuevo.setCodigoPk(BigDecimal.ZERO);
				dtoNuevo.setHallazgo(piezaPresupuesto.getHallazgo());
				dtoNuevo.setNumSuperficies(piezaPresupuesto.getNumSuperficies());
				dtoNuevo.setPieza(piezaPresupuesto.getPieza());
				dtoNuevo.setPresupuestoOdoProgServ(piezaPresupuesto.getPresupuestoOdoProgServ());
				dtoNuevo.setSeccion(piezaPresupuesto.getSeccion());
				dtoNuevo.setSuperficie(piezaPresupuesto.getSuperficie());
				dtoNuevo.setUsuarioModifica(piezaPresupuesto.getUsuarioModifica());
				
				ArrayList<String> listaNuevaP= new ArrayList<String>();
				for(String nuevo:piezaPresupuesto.getListaConvenioContratoPaquetiza())
				{
					listaNuevaP.add(nuevo);
				}
				dtoNuevo.setListaConvenioContratoPaquetiza(listaNuevaP);
				listaNueva.add(dtoNuevo);
				numSuperficiesAdicionadas++;
				
				///como ya fue adicionada en otra parte entonces la borramos
				iteradorPiezaPresupuesto.remove();
			}
			int numSuperficiesFaltantes=  piezaPresupuesto.getNumSuperficies() - numSuperficiesAdicionadas;
			if(numSuperficiesFaltantes<=0)
			{
				break;
			}
		}
		
		return listaNueva;
	}
	
	/**
	 * 
	 * Metodo para obtener el codigo a mostrar dato el codigopk del paquete odontologico del convenio
	 * @param codigoPkPaquete
	 * @return
	 * @author   Wilson Rios
	 * @version  1.0.0 
	 * @see
	 */
	public static String obtenerCodigoMostrarPaqueteOdontologico(int codigoPkPaquete) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPresupuestoPaquetesDao().obtenerCodigoMostrarPaqueteOdontologico(codigoPkPaquete);
	}

	/**
	 * 
	 * Metodo para actualizar el presupuesto_paquete de los convenios
	 * @param listaProgramasServicios
	 * @param dto
	 * @author   Wilson Rios
	 * @version  1.0.0 
	 * @see
	 */
	public static void setearPaquetesPresupuestoOdoConvenio(
			ArrayList<DtoPresuOdoProgServ> listaProgramasServicios,
			DtoPresupuestoPaquetes dto) 
	{
		for(DtoPresuOdoProgServ programa: listaProgramasServicios)
		{
			for(DtoPresupuestoOdoConvenio dtoConvenio: programa.getListPresupuestoOdoConvenio())
			{
				if(dtoConvenio.getDtoPresupuestoPaquete().getDetallePaqueteOdontologicoConvenio()>0)
				{
					if(dtoConvenio.getDtoPresupuestoPaquete().getDetallePaqueteOdontologicoConvenio()==dto.getDetallePaqueteOdontologicoConvenio())
					{
						dtoConvenio.getDtoPresupuestoPaquete().setCodigoPk(dto.getCodigoPk());
					}
				}
			}
		}
	}

	/**
	 * 
	 * Metodo para agrupar nuevamente las cantidades que quedaron sin paquetes
	 * @param listaProgramasServicios
	 * @author   Wilson Rios
	 * @version  1.0.0 
	 * @see
	 */
	public static void agruparCantidades(ArrayList<DtoPresuOdoProgServ> listaProgramasServicios) 
	{
		for(int w=0; w<listaProgramasServicios.size(); w++)
		{
			DtoPresuOdoProgServ programa= listaProgramasServicios.get(w);
			int cantidad=0;
			
			//si el programa interno tampoco esta paquetizado y no esta marcado para eliminar
			if(!programa.getPaquetizado() && !programa.isEliminar())
			{	
				//se le coloca la cantidad del programa externo
				cantidad+=programa.getCantidad();
				for(int i=0; i<listaProgramasServicios.size(); i++)
				{
					//comparamos contra una posicion diferente
					if(i!=w)
					{
						DtoPresuOdoProgServ programaInterno= listaProgramasServicios.get(i);
						
						//si el programa interno tampoco esta paquetizado y no esta marcado para eliminar
						if(!programaInterno.getPaquetizado() && !programaInterno.isEliminar())
						{
							//si el programa internoy externo son iguales entonces debemos unificar las cantidades
							if(programa.getPrograma().getCodigo().doubleValue()==programaInterno.getPrograma().getCodigo().doubleValue())
							{
								//sumamos la cantidad del programa interno
								cantidad+=programaInterno.getCantidad();
								//marcamos el programa para eliminarlo
								programaInterno.setEliminar(true);
								
								//copiamos la informacion de hallazgos
								for(DtoPresupuestoPiezas pieza: programaInterno.getListPresupuestoPiezas())
								{	
									programa.getListPresupuestoPiezas().add(pieza);
								}	
							}
						}
					}
				}
				///ya teniendo los programas internos para eliminar y la cantidad contabilizada  
				//entonces seteamos el prog externo que va ha quedar con la agrupacion
				programa.setCantidad(cantidad);
				
				for(DtoAgrupacionHallazgoSuperficie dtoAgrup: programa.getListaCalculoCantidades().getListaCalculoCantidades())
				{
					if(dtoAgrup.getNumeroSuperficies()>0)
					{	
						dtoAgrup.setContador(programa.getCantidad()*dtoAgrup.getNumeroSuperficies());
					}
					else
					{
						dtoAgrup.setContador(programa.getCantidad());
					}
				}
			}	
		}
		
		//recorremos los programas que quedaron marcados para eliminar y borramos
		Iterator<DtoPresuOdoProgServ> iterador= listaProgramasServicios.iterator();
		///primero eliminamos los no existentes
		while(iterador.hasNext())
		{
			DtoPresuOdoProgServ programaPresupuesto= iterador.next(); 
			if(programaPresupuesto.isEliminar())
			{
				iterador.remove();
			}
		}
	}

	/**
	 * 
	 * Metodo para limpiar los paquetes
	 * @param info
	 * @param listaProgramasServicios
	 * @param usuario
	 * @author   Wilson Rios
	 * @version  1.0.0 
	 * @param arrayList 
	 * @see
	 */
	public static void limpiarPaquete(InfoPaquetesPresupuesto paquete,	ArrayList<DtoPresuOdoProgServ> listaProgramasServicios,	UsuarioBasico usuario, PersonaBasica paciente) throws IPSException 
	{
		for(DtoPresuOdoProgServ programaPresupuesto: listaProgramasServicios)
		{
			for(DtoPresupuestoOdoConvenio dtocon: programaPresupuesto.getListPresupuestoOdoConvenio())
			{
				if(dtocon.getConvenio().getCodigo()==paquete.getConvenio().getCodigo() &&
					dtocon.getDtoPresupuestoPaquete().getDetallePaqueteOdontologicoConvenio()== paquete.getDetallePkPaqueteOdonCONVENIO())
				{
					dtocon.setDtoPresupuestoPaquete(new DtoPresupuestoPaquetes());
					String fechaCalculoTarifa=UtilidadFecha.getFechaActual();
					//le seteamos la tarifa nueva 
					InfoPresupuestoXConvenioProgramaServicio infoPresupuesto= CargosOdon.obtenerPresupuestoXConvenio(programaPresupuesto.getServicio().getCodigo(), programaPresupuesto.getPrograma().getCodigo().doubleValue(), 1 /*cantidad*/, paquete.getConvenio().getCodigo(), paquete.getContrato(), fechaCalculoTarifa, usuario.getCodigoInstitucionInt(), new BigDecimal(paciente.getCodigoCuenta()));
					dtocon.setValorUnitario(infoPresupuesto.getValorUnitarioProgramaServicioConvenio());
					dtocon.setErrorCalculoTarifa(infoPresupuesto.getErroresTotalesStr("<br>"));
					dtocon.setAdvertenciaPromocion(infoPresupuesto.getDetallePromocionDescuento().getAdvertencia());
					dtocon.setDescuentoComercialUnitario(infoPresupuesto.getValorUnitarioDctoComercial());
					dtocon.setPorcentajeHonorarioPromocion(infoPresupuesto.getDetallePromocionDescuento().getPorcentajeHonorario());
					dtocon.setValorHonorarioPromocion(infoPresupuesto.getDetallePromocionDescuento().getValorHonorario());
					dtocon.setValorDescuentoPromocion(infoPresupuesto.getDetallePromocionDescuento().getValorPromocion());
					dtocon.setPorcentajePromocion(infoPresupuesto.getDetallePromocionDescuento().getPorcentajePromocion());
					dtocon.setSerialBono(infoPresupuesto.getDetalleBonoDescuento().getSerial());
					dtocon.setValorDescuentoBono(infoPresupuesto.getDetalleBonoDescuento().getValorDctoCALCULADO());
					dtocon.setPorcentajeDctoBono(infoPresupuesto.getDetalleBonoDescuento().getPorcentajeDescuento());
					dtocon.setAdvertenciaBono(infoPresupuesto.getDetalleBonoDescuento().getAdvertencia());
					dtocon.setConvenio(paquete.getConvenio());
					dtocon.setContrato(new InfoDatosInt(paquete.getContrato()));
					dtocon.setDetallePromocion(infoPresupuesto.getDetallePromocionDescuento().getDetPromocion());
					
					dtocon.setSeleccionadoPorcentajeBono(infoPresupuesto.getDetalleBonoDescuento().isSeleccionadoPorcentaje());
					dtocon.setSeleccionadoPorcentajePromocion(infoPresupuesto.getDetallePromocionDescuento().isSeleccionadoPorcentaje());
					
					//si existe valor descuento x bono y x promocion entonces se debe postular la de mayor valor
					if(dtocon.getValorDescuentoBono().doubleValue()>0 && dtocon.getValorDescuentoPromocion().doubleValue()>0)
					{
						if(dtocon.getValorDescuentoBono().doubleValue()>dtocon.getValorDescuentoPromocion().doubleValue())
						{	
							dtocon.setSeleccionadoBono(true);
							dtocon.setSeleccionadoPromocion(false);
						}
						else
						{	
							dtocon.setSeleccionadoPromocion(true);
							dtocon.setSeleccionadoBono(false);
						}	
					}
					else
					{
						dtocon.setSeleccionadoBono(dtocon.getValorDescuentoBono().doubleValue()>0);
						dtocon.setSeleccionadoPromocion(dtocon.getValorDescuentoPromocion().doubleValue()>0);
					}
					
					//DEBEMOS SETEAR LOS DETALLES DE LOS SERVICIOS DEL PROGRAMA
					dtocon.setListaDetalleServiciosPrograma(cargarListaServiciosProgramaTarifas(infoPresupuesto.getDetalleTarifasServicio(), usuario, programaPresupuesto.getPrograma().getCodigo().doubleValue() ));
				}	
			}
			
			for(DtoPresupuestoPiezas pieza: programaPresupuesto.getListPresupuestoPiezas())
			{
				Iterator<String> convenioContratoPaquetiza=  pieza.getListaConvenioContratoPaquetiza().iterator();
				while(convenioContratoPaquetiza.hasNext())
				{
					String ccon= convenioContratoPaquetiza.next();
					if(ccon.equals(paquete.getConvenio().getCodigo()+"-"+paquete.getContrato()))
					{
						convenioContratoPaquetiza.remove();
					}
				}
			}
		}
	}

	/**
	 * 
	 * @param detalleTarifasServicio
	 * @return
	 */
	private static ArrayList<DtoPresupuestoDetalleServiciosProgramaDao> cargarListaServiciosProgramaTarifas(
			ArrayList<InfoTarifaServicioPresupuesto> detalleTarifasServicio, UsuarioBasico usuario, Double programa) 
	{
		ArrayList<DtoPresupuestoDetalleServiciosProgramaDao> array= new ArrayList<DtoPresupuestoDetalleServiciosProgramaDao>();
		
		for(InfoTarifaServicioPresupuesto info: detalleTarifasServicio)
		{
			DtoPresupuestoDetalleServiciosProgramaDao detalle= new DtoPresupuestoDetalleServiciosProgramaDao();
			detalle.setDctoComercialUnitario(info.getValorDescuentoComercial());
			detalle.setErrorCalculoTarifa(info.getError());
			detalle.setFHU(new DtoInfoFechaUsuario(usuario.getLoginUsuario()));
			detalle.setPorcentajeDctoBonoServicio(info.getPorcentajeDecuentoBonoUnitario());
			detalle.setPorcentajeDctoPromocionServicio(info.getPorcentajeDescuentoPromocionUnitario());
			//detalle.setPresupuestoOdoConvenio(presupuestoOdoConvenio);
			detalle.setPrograma(programa);
			detalle.setServicio(info.getServicio());
			detalle.setValorDctoBonoServicio(info.getValorDescuentoBonoUnitario());
			detalle.setValorDctoPromocionServicio(info.getValorDescuentoPromocionUnitario());
			detalle.setValorUnitarioServicio(info.getValorTarifaUnitaria());
			detalle.setValorHonorarioDctoPromocionServicio(info.getValorHonorarioPromocion());
			detalle.setPorcentajeHonorarioDctoPromocionServicio(info.getPorcentajeHonorarioPromocion());
			
			array.add(detalle);
		}
		
		return array;
	}
	
	/**
	 * 
	 * Metodo para cargar el info de paquete 
	 * @param codigoPkPresupuesto
	 * @return
	 * @author   Wilson Rios
	 * @version  1.0.0 
	 * @see
	 */
	public static ArrayList<InfoPaquetesPresupuesto> cargarInfoPaquetesTarifas(	BigDecimal codigoPkPresupuesto) throws IPSException
	{
		 ArrayList<InfoPaquetesPresupuesto> lista=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPresupuestoPaquetesDao().cargarInfoPaquetesTarifas(codigoPkPresupuesto);
		 calcularTarifaPaquete(lista);
		 return lista;
	}
	
	/**
	 * 
	 * Metodo para obtener los indices de las funciones js paquetes 
	 * 
	 * indicePrograma codigoConvenio indiceSumatoria
	 * 
	 * @param codigoPkPresupuestoPaquete
	 * @return
	 * @author   Wilson Rios
	 * @version  1.0.0 
	 * @see
	 */
	public static String getIndicesJs(ArrayList<DtoPresuOdoProgServ> listaProgramas, BigDecimal codigoPkPresupuestoPaquete, ArrayList<DtoPresupuestoTotalConvenio> listaSumatoriaConvenio)
	{
		String resultado="";
		if(codigoPkPresupuestoPaquete.doubleValue()>0)
		{
			for(int indiceSumatoria=0; indiceSumatoria<listaSumatoriaConvenio.size(); indiceSumatoria++)
			{
				DtoPresupuestoTotalConvenio dtoSumatoria= listaSumatoriaConvenio.get(indiceSumatoria);
				for(int indicePrograma=0; indicePrograma<listaProgramas.size(); indicePrograma++)
				{
					DtoPresuOdoProgServ dtoProg= listaProgramas.get(indicePrograma);
					
					for(DtoPresupuestoOdoConvenio dtoc: dtoProg.getListPresupuestoOdoConvenio())
					{
						if(dtoc.getConvenio().getCodigo()==dtoSumatoria.getConvenio().getCodigo() 
							&& dtoc.getDtoPresupuestoPaquete().getCodigoPk().doubleValue()==codigoPkPresupuestoPaquete.doubleValue())
						{
							if(!resultado.isEmpty())
							{
								resultado+=ConstantesBD.separadorSplitComplejo;
							}
							resultado+=indicePrograma+ConstantesBD.separadorSplit+dtoSumatoria.getConvenio().getCodigo()+ConstantesBD.separadorSplit+indiceSumatoria;
						}
					}
					
				}
			}	
		}	
		return resultado;
	}
	
	/**
	 * 
	 * Metodo para validar si es vigente o no el paquete odo convenio
	 * @param codigoPk
	 * @return
	 * @author   Wilson Rios
	 * @version  1.0.0 
	 * @see
	 */
	public static boolean esDetallePaqueteConvenioVigente(BigDecimal codigoPk, String fechaApp)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPresupuestoPaquetesDao().esDetallePaqueteConvenioVigente(codigoPk, fechaApp);
	}
}
