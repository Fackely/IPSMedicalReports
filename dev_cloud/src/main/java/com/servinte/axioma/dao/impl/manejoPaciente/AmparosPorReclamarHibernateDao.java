package com.servinte.axioma.dao.impl.manejoPaciente;

import java.math.BigDecimal;
import java.util.ArrayList;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;

import com.princetonsa.mundo.historiaClinica.RegistroEventosAdversos;
import com.servinte.axioma.dao.interfaz.manejoPaciente.IAmparosPorReclamarDao;
import com.servinte.axioma.dto.manejoPaciente.DtoAmparoXReclamar;
import com.servinte.axioma.dto.manejoPaciente.DtoCertAtenMedicaFurips;
import com.servinte.axioma.dto.manejoPaciente.DtoCertAtenMedicaFurpro;
import com.servinte.axioma.dto.manejoPaciente.DtoFiltroBusquedaAvanzadaReclamaciones;
import com.servinte.axioma.dto.manejoPaciente.DtoReclamacionesAccEveFact;
import com.servinte.axioma.dto.manejoPaciente.DtoServiciosReclamados;
import com.servinte.axioma.hibernate.HibernateUtil;
import com.servinte.axioma.orm.AmparoXReclamar;
import com.servinte.axioma.orm.CertAtenMedicaFurips;
import com.servinte.axioma.orm.CertAtenMedicaFurpro;
import com.servinte.axioma.orm.Diagnosticos;
import com.servinte.axioma.orm.DiagnosticosHome;
import com.servinte.axioma.orm.DiagnosticosId;
import com.servinte.axioma.orm.Facturas;
import com.servinte.axioma.orm.FacturasHome;
import com.servinte.axioma.orm.Ingresos;
import com.servinte.axioma.orm.Medicos;
import com.servinte.axioma.orm.Personas;
import com.servinte.axioma.orm.ReclamacionesAccEveFact;
import com.servinte.axioma.orm.RegistroAccidentesTransito;
import com.servinte.axioma.orm.RegistroAccidentesTransitoHome;
import com.servinte.axioma.orm.RegistroEventoCatastrofico;
import com.servinte.axioma.orm.RegistroEventoCatastroficoHome;
import com.servinte.axioma.orm.ServiciosReclamados;
import com.servinte.axioma.orm.TiposIdentificacion;
import com.servinte.axioma.orm.Usuarios;
import com.servinte.axioma.orm.delegate.UsuariosDelegate;
import com.servinte.axioma.orm.delegate.administracion.MedicosDelegate;
import com.servinte.axioma.orm.delegate.administracion.TiposIdentificacionDelegate;
import com.servinte.axioma.orm.delegate.manejoPaciente.AmparoXReclamarDelegate;
import com.servinte.axioma.orm.delegate.manejoPaciente.CertAtenMedicaFuripsDelegate;
import com.servinte.axioma.orm.delegate.manejoPaciente.CertAtenMedicaFurproDelegate;
import com.servinte.axioma.orm.delegate.manejoPaciente.IngresosDelegate;
import com.servinte.axioma.orm.delegate.manejoPaciente.ReclamacionesAccEveFactDelegate;
import com.servinte.axioma.orm.delegate.manejoPaciente.ServiciosReclamadosDelegate;
import com.sies.hibernate.delegate.PersonasDelegate;

public class AmparosPorReclamarHibernateDao implements IAmparosPorReclamarDao {

	
	
	@Override
	public void insertarNuevaReclamacion(DtoReclamacionesAccEveFact amparoXReclamar) 
	{
		ReclamacionesAccEveFactDelegate recDao=new ReclamacionesAccEveFactDelegate();
		ReclamacionesAccEveFact dtoRec=new ReclamacionesAccEveFact();
		FacturasHome daoFac=new FacturasHome();
		Facturas fac=new Facturas();
		IngresosDelegate daoIng=new IngresosDelegate();
		Ingresos ing=new Ingresos();
		UsuariosDelegate usuDao=new UsuariosDelegate();
		Usuarios usuModifica=new Usuarios();
		Usuarios usuRegistra=new Usuarios();
		
		
		dtoRec.setAnioConsReclamacion(amparoXReclamar.getAnioConsReclamacion());
		dtoRec.setEstado(ConstantesIntegridadDominio.acronimoEstadoGenerado);
		
		fac=daoFac.findById(amparoXReclamar.getCodigoFactura());
		dtoRec.setFacturas(fac);
		dtoRec.setFechaModifca(UtilidadFecha.conversionFormatoFechaStringDate(UtilidadFecha.conversionFormatoFechaABD(amparoXReclamar.getFechaModifica())));
		dtoRec.setFechaRaclamacion(UtilidadFecha.conversionFormatoFechaStringDate(UtilidadFecha.conversionFormatoFechaABD(amparoXReclamar.getFechaReclamacion())));
		dtoRec.setFechaRegistra(UtilidadFecha.conversionFormatoFechaStringDate(UtilidadFecha.conversionFormatoFechaABD(amparoXReclamar.getFechaRegistro())));
		dtoRec.setHoraModifica(amparoXReclamar.getHoraModifica());
		dtoRec.setHoraReclamacion(amparoXReclamar.getHoraReclamacion());
		dtoRec.setHoraRegistra(amparoXReclamar.getHoraRegistro());
		
		ing=daoIng.findById(amparoXReclamar.getIngreso());
		dtoRec.setIngresos(ing);
		dtoRec.setNroReclamacion(amparoXReclamar.getNroReclamacion());
		dtoRec.setNumRadicaAnterior(amparoXReclamar.getNumRadicacionAnterior());
		if(!UtilidadTexto.isEmpty(amparoXReclamar.getRespuestaGlosa()))
			dtoRec.setRespuestaGlosa(amparoXReclamar.getRespuestaGlosa());
		dtoRec.setTipoEvento(amparoXReclamar.getTipoEvento());
		dtoRec.setTipoReclamacion(amparoXReclamar.getTipoReclamacion());
		
		
		usuModifica=usuDao.findById(amparoXReclamar.getUsuarioModifica());
		usuRegistra=usuDao.findById(amparoXReclamar.getUsuarioRegistro());
		dtoRec.setUsuariosByUsuarioModifica(usuModifica);
		dtoRec.setUsuariosByUsuarioRegistra(usuRegistra);
		
		if(amparoXReclamar.getTipoEvento().equals(ConstantesIntegridadDominio.acronimoAccidenteTransito))
		{
			RegistroAccidentesTransitoHome daoAccidnete=new RegistroAccidentesTransitoHome();
			RegistroAccidentesTransito dtoAccidente=daoAccidnete.findById(amparoXReclamar.getCodigoAccidente());
			dtoRec.setRegistroAccidentesTransito(dtoAccidente);
		}
		else
		{
			RegistroEventoCatastroficoHome daoEvento=new RegistroEventoCatastroficoHome();
			RegistroEventoCatastrofico dtoEvento=daoEvento.findById(amparoXReclamar.getCodigoEvento());
			dtoRec.setRegistroEventoCatastrofico(dtoEvento);
		}
		
		
		recDao.persist(dtoRec);
		
		if(dtoRec.getCodigoPk()>0)
		{
			amparoXReclamar.setCodigoPk((int)dtoRec.getCodigoPk());
			//asignar los atributos.
			if(amparoXReclamar.getTipoReclamacion().equals(ConstantesIntegridadDominio.acronimoFURIPS))
			{
				//seccion amparos por reclamar.
				AmparoXReclamarDelegate ampRecDao=new AmparoXReclamarDelegate();
				AmparoXReclamar dtoAmpRec=new AmparoXReclamar();
				dtoAmpRec.setReclamacionesAccEveFact(dtoRec);
				dtoAmpRec.setTotalFacAmpGastMedqx(amparoXReclamar.getAmparoXReclamar().getTotalFacAmpGastMedQX());
				dtoAmpRec.setTotalRecAmpGastMedqx(amparoXReclamar.getAmparoXReclamar().getTotalRecAmpGastMedQX());
				dtoAmpRec.setTotalFacAmpGastTransmov(amparoXReclamar.getAmparoXReclamar().getTotalFacAmpGastTransMov());
				dtoAmpRec.setTotalRecAmpGastTransmov(amparoXReclamar.getAmparoXReclamar().getTotalRecAmpGastTransMov());
				ampRecDao.persist(dtoAmpRec);
				
				//seccion certificacion medica
				CertAtenMedicaFuripsDelegate certDao=new CertAtenMedicaFuripsDelegate();
				CertAtenMedicaFurips dtoCert=new CertAtenMedicaFurips();
				dtoCert.setReclamacionesAccEveFact(dtoRec);
				if(amparoXReclamar.getCertAtenMedicaFurips().getTipoCieDxIngreso()>0)
				{
					DiagnosticosHome diagDao=new DiagnosticosHome();
					Diagnosticos diag=new Diagnosticos();
					diag=diagDao.findById(new DiagnosticosId(amparoXReclamar.getCertAtenMedicaFurips().getAcronimoDxIngreso(), amparoXReclamar.getCertAtenMedicaFurips().getTipoCieDxIngreso()));
					dtoCert.setDiagnosticosByPkCeratemedfuripsDxing(diag);
				}
				if(amparoXReclamar.getCertAtenMedicaFurips().getTipoCieDxEgreso()>0)
				{
					DiagnosticosHome diagDao=new DiagnosticosHome();
					Diagnosticos diag=new Diagnosticos();
					diag=diagDao.findById(new DiagnosticosId(amparoXReclamar.getCertAtenMedicaFurips().getAcronimoDxEgreso(), amparoXReclamar.getCertAtenMedicaFurips().getTipoCieDxEgreso()));
					dtoCert.setDiagnosticosByPkCeratemedfuripsDxegr(diag);
				}
				if(amparoXReclamar.getCertAtenMedicaFurips().getTipoCieDxRel1Ingreso()>0)
				{
					DiagnosticosHome diagDao=new DiagnosticosHome();
					Diagnosticos diag=new Diagnosticos();
					diag=diagDao.findById(new DiagnosticosId(amparoXReclamar.getCertAtenMedicaFurips().getAcronimoDxRel1Ingreso(), amparoXReclamar.getCertAtenMedicaFurips().getTipoCieDxRel1Ingreso()));
					dtoCert.setDiagnosticosByPkCeratemedfuripsReling1(diag);
				}
				if(amparoXReclamar.getCertAtenMedicaFurips().getTipoCieDxRel2Ingreso()>0)
				{
					DiagnosticosHome diagDao=new DiagnosticosHome();
					Diagnosticos diag=new Diagnosticos();
					diag=diagDao.findById(new DiagnosticosId(amparoXReclamar.getCertAtenMedicaFurips().getAcronimoDxRel2Ingreso(), amparoXReclamar.getCertAtenMedicaFurips().getTipoCieDxRel2Ingreso()));
					dtoCert.setDiagnosticosByPkCeratemedfuripsReling2(diag);
				}
				if(amparoXReclamar.getCertAtenMedicaFurips().getTipoCieDxRel1Egreso()>0)
				{
					DiagnosticosHome diagDao=new DiagnosticosHome();
					Diagnosticos diag=new Diagnosticos();
					diag=diagDao.findById(new DiagnosticosId(amparoXReclamar.getCertAtenMedicaFurips().getAcronimoDxRel1Egreso(), amparoXReclamar.getCertAtenMedicaFurips().getTipoCieDxRel1Egreso()));
					dtoCert.setDiagnosticosByPkCeratemedfuripsRelegr1(diag);
				}
				if(amparoXReclamar.getCertAtenMedicaFurips().getTipoCieDxRel2Egreso()>0)
				{
					DiagnosticosHome diagDao=new DiagnosticosHome();
					Diagnosticos diag=new Diagnosticos();
					diag=diagDao.findById(new DiagnosticosId(amparoXReclamar.getCertAtenMedicaFurips().getAcronimoDxRel2Egreso(), amparoXReclamar.getCertAtenMedicaFurips().getTipoCieDxRel2Egreso()));
					dtoCert.setDiagnosticosByPkCeratemedfuripsRelegr2(diag);
				}
				TiposIdentificacionDelegate tiposIdDao=new TiposIdentificacionDelegate();
				TiposIdentificacion tipoID=new TiposIdentificacion();
				MedicosDelegate medDao=new MedicosDelegate();
				Medicos med=new Medicos();
				
				med=medDao.findById(amparoXReclamar.getCertAtenMedicaFurips().getCodigoMedico());
				dtoCert.setMedicos(med);
				tipoID=tiposIdDao.findById(amparoXReclamar.getCertAtenMedicaFurips().getTipoDocumentoMedico());
				dtoCert.setTiposIdentificacion(tipoID);
				dtoCert.setNroDocumentoMedico(amparoXReclamar.getCertAtenMedicaFurips().getNumeroDocumentoMedico());
				dtoCert.setNroRegistroMedico(amparoXReclamar.getCertAtenMedicaFurips().getNumeroRegistroMedico());
				dtoCert.setPrimerApellidoMedico(amparoXReclamar.getCertAtenMedicaFurips().getPrimerApellidoMedico());
				dtoCert.setSegundoApellidoMedico(amparoXReclamar.getCertAtenMedicaFurips().getSegundoApellidoMedico());
				dtoCert.setPrimerNombreMedico(amparoXReclamar.getCertAtenMedicaFurips().getPrimerNombreMedico());
				dtoCert.setSegundoNombreMedico(amparoXReclamar.getCertAtenMedicaFurips().getSegundoNombreMedico());
				certDao.persist(dtoCert);
			}
			else if(amparoXReclamar.getTipoReclamacion().equals(ConstantesIntegridadDominio.acronimoFURPRO))
			{
				//seccion servicios relacionados
				ServiciosReclamadosDelegate servDao=new ServiciosReclamadosDelegate();
				ServiciosReclamados servDto=new ServiciosReclamados();
				servDto.setReclamacionesAccEveFact(dtoRec);
				servDto.setAdaptacionProtesis(amparoXReclamar.getServiciosReclamados().getAdaptacionProtesis());
				servDto.setDescProtServPres(amparoXReclamar.getServiciosReclamados().getDescProtesisServicioPrestado());
				servDto.setProtesis(amparoXReclamar.getServiciosReclamados().getProtesis());
				servDto.setRehabilitacion(amparoXReclamar.getServiciosReclamados().getRehabilitacion());
				servDto.setValorAdapProtesis(amparoXReclamar.getServiciosReclamados().getValorAdaptacionProtesis());
				servDto.setValorProtesis(amparoXReclamar.getServiciosReclamados().getValorProtesis());
				servDto.setValorRehabilitacion(amparoXReclamar.getServiciosReclamados().getValorRehabilitacion());
				servDao.persist(servDto);
				
				//seccion certificacion medica
				CertAtenMedicaFurproDelegate certDao=new CertAtenMedicaFurproDelegate();
				CertAtenMedicaFurpro dtoCert=new CertAtenMedicaFurpro();
				dtoCert.setReclamacionesAccEveFact(dtoRec);
				if(amparoXReclamar.getCertAtenMedicaFurpro().getTipoCieDxEgreso()>0)
				{
					DiagnosticosHome diagDao=new DiagnosticosHome();
					Diagnosticos diag=new Diagnosticos();
					diag=diagDao.findById(new DiagnosticosId(amparoXReclamar.getCertAtenMedicaFurpro().getAcronimoDxEgreso(), amparoXReclamar.getCertAtenMedicaFurpro().getTipoCieDxEgreso()));
					dtoCert.setDiagnosticosByPkCeratemedfurproDxegr(diag);
				}
				if(amparoXReclamar.getCertAtenMedicaFurpro().getTipoCieDxRel1Egreso()>0)
				{
					DiagnosticosHome diagDao=new DiagnosticosHome();
					Diagnosticos diag=new Diagnosticos();
					diag=diagDao.findById(new DiagnosticosId(amparoXReclamar.getCertAtenMedicaFurpro().getAcronimoDxRel1Egreso(), amparoXReclamar.getCertAtenMedicaFurpro().getTipoCieDxRel1Egreso()));
					dtoCert.setDiagnosticosByPkCeratemedfurproRelegr1(diag);
				}
				if(amparoXReclamar.getCertAtenMedicaFurpro().getTipoCieDxRel2Egreso()>0)
				{
					DiagnosticosHome diagDao=new DiagnosticosHome();
					Diagnosticos diag=new Diagnosticos();
					diag=diagDao.findById(new DiagnosticosId(amparoXReclamar.getCertAtenMedicaFurpro().getAcronimoDxRel2Egreso(), amparoXReclamar.getCertAtenMedicaFurpro().getTipoCieDxRel2Egreso()));
					dtoCert.setDiagnosticosByPkCeratemedfurproRelegr2(diag);
				}
				if(amparoXReclamar.getCertAtenMedicaFurpro().getTipoCieDxRel3Egreso()>0)
				{
					DiagnosticosHome diagDao=new DiagnosticosHome();
					Diagnosticos diag=new Diagnosticos();
					diag=diagDao.findById(new DiagnosticosId(amparoXReclamar.getCertAtenMedicaFurpro().getAcronimoDxRel3Egreso(), amparoXReclamar.getCertAtenMedicaFurpro().getTipoCieDxRel3Egreso()));
					dtoCert.setDiagnosticosByPkCeratemedfurproRelegr3(diag);
				}
				if(amparoXReclamar.getCertAtenMedicaFurpro().getTipoCieDxRel4Egreso()>0)
				{
					DiagnosticosHome diagDao=new DiagnosticosHome();
					Diagnosticos diag=new Diagnosticos();
					diag=diagDao.findById(new DiagnosticosId(amparoXReclamar.getCertAtenMedicaFurpro().getAcronimoDxRel4Egreso(), amparoXReclamar.getCertAtenMedicaFurpro().getTipoCieDxRel4Egreso()));
					dtoCert.setDiagnosticosByPkCeratemedfurproRelegr4(diag);
				}
				certDao.persist(dtoCert);
			}			
		}
		
	}

	@Override
	public ArrayList<DtoReclamacionesAccEveFact> consultarReclamacionesFactura(int codigoFactura) 
	{
		ArrayList<DtoReclamacionesAccEveFact> resultado=new ArrayList<DtoReclamacionesAccEveFact>();
		ReclamacionesAccEveFactDelegate recDao=new ReclamacionesAccEveFactDelegate();
		ArrayList<ReclamacionesAccEveFact> listado=recDao.consultarReclamacionesFactura(codigoFactura);
		if(listado!=null)
		{
			for(ReclamacionesAccEveFact dtoListado:listado)
			{
				DtoReclamacionesAccEveFact dto=new DtoReclamacionesAccEveFact();
				dto.setCodigoPk((int)dtoListado.getCodigoPk());
				dto.setTipoEvento(dtoListado.getTipoEvento());
				dto.setTipoReclamacion(dtoListado.getTipoReclamacion());
				dto.setNroReclamacion(dtoListado.getNroReclamacion());
				dto.setEstado(dtoListado.getEstado());
				dto.setFechaReclamacion(UtilidadFecha.conversionFormatoFechaAAp(dtoListado.getFechaRaclamacion()));
				dto.setFechaRadicacion(UtilidadFecha.conversionFormatoFechaAAp(dtoListado.getFechaRadicacion()));
				dto.setFechaAnulacion(UtilidadFecha.conversionFormatoFechaAAp(dtoListado.getFechaAnulacion()));
				dto.setHoraRadicacion(dtoListado.getHoraRadicacion());
				dto.setHoraAnulacion(dtoListado.getHoraAnulacion());
				if(dtoListado.getUsuariosByUsuarioAnulacion()!=null)
					dto.setUsuarioAnulacion(dtoListado.getUsuariosByUsuarioAnulacion().getLogin());
				if(dtoListado.getUsuariosByUsuarioRadicacion()!=null)
					dto.setUsuarioRadicacion(dtoListado.getUsuariosByUsuarioRadicacion().getLogin());
				resultado.add(dto);
			}
		}
		return resultado;
	}
	
	@Override
	public ArrayList<DtoReclamacionesAccEveFact> consultarReclamacionesBusquedaAvanzada(DtoFiltroBusquedaAvanzadaReclamaciones filtro) 
	{
		ArrayList<DtoReclamacionesAccEveFact> resultado=new ArrayList<DtoReclamacionesAccEveFact>();
		ReclamacionesAccEveFactDelegate recDao=new ReclamacionesAccEveFactDelegate();
		ArrayList<ReclamacionesAccEveFact> listado=recDao.consultarReclamacionesBusquedaAvanzada(filtro);
		if(listado!=null)
		{
			for(ReclamacionesAccEveFact dtoListado:listado)
			{
				DtoReclamacionesAccEveFact dto=new DtoReclamacionesAccEveFact();
				dto.setCodigoPk((int)dtoListado.getCodigoPk());
				dto.setTipoEvento(dtoListado.getTipoEvento());
				dto.setTipoReclamacion(dtoListado.getTipoReclamacion());
				dto.setNroReclamacion(dtoListado.getNroReclamacion());
				dto.setEstado(dtoListado.getEstado());
				dto.setFechaReclamacion(UtilidadFecha.conversionFormatoFechaAAp(dtoListado.getFechaRaclamacion()));
				dto.setFechaRadicacion(UtilidadFecha.conversionFormatoFechaAAp(dtoListado.getFechaRadicacion()));
				dto.setFechaAnulacion(UtilidadFecha.conversionFormatoFechaAAp(dtoListado.getFechaAnulacion()));
				dto.setHoraRadicacion(dtoListado.getHoraRadicacion());
				dto.setHoraAnulacion(dtoListado.getHoraAnulacion());
				
				//hacer debug aca y mirar si si carga.
				dto.setCodigoPaciente(dtoListado.getFacturas().getPersonas().getCodigo());
				Personas per=dtoListado.getFacturas().getPersonas().getPacientes().getPersonas();
				dto.setNombresPaciente(per.getPrimerNombre()+" "+(UtilidadTexto.isEmpty(per.getSegundoNombre())?"":per.getSegundoNombre()+" ")+per.getPrimerApellido()+" "+(UtilidadTexto.isEmpty(per.getSegundoApellido())?"":per.getSegundoApellido()));
				dto.setTipoIdPaciente(per.getTiposIdentificacion().getAcronimo());
				dto.setNumeroIdPaciente(per.getNumeroIdentificacion());
				dto.setConsecutivoIngreso(dtoListado.getFacturas().getCuentas().getIngresos().getConsecutivo());
				dto.setIngreso(dtoListado.getFacturas().getCuentas().getIngresos().getId());
				dto.setCodigoConvenio(dtoListado.getFacturas().getConvenios().getCodigo());
				dto.setNombreConvenio(dtoListado.getFacturas().getConvenios().getNombre());
				
				
				
				if(dtoListado.getUsuariosByUsuarioAnulacion()!=null)
					dto.setUsuarioAnulacion(dtoListado.getUsuariosByUsuarioAnulacion().getLogin());
				if(dtoListado.getUsuariosByUsuarioRadicacion()!=null)
					dto.setUsuarioRadicacion(dtoListado.getUsuariosByUsuarioRadicacion().getLogin());
				resultado.add(dto);
			}
		}
		return resultado;
	}
	
	@Override
	public ArrayList<DtoReclamacionesAccEveFact> consultarReclamacionesEventoCatastrofico(int codigo,boolean todas) 
	{
		ArrayList<DtoReclamacionesAccEveFact> resultado=new ArrayList<DtoReclamacionesAccEveFact>();
		ReclamacionesAccEveFactDelegate recDao=new ReclamacionesAccEveFactDelegate();
		ArrayList<ReclamacionesAccEveFact> listado=recDao.consultarReclamacionesEventoCatastrofico(codigo,todas);
		if(listado!=null)
		{
			for(ReclamacionesAccEveFact dtoListado:listado)
			{
				DtoReclamacionesAccEveFact dto=new DtoReclamacionesAccEveFact();
				dto.setCodigoPk((int)dtoListado.getCodigoPk());
				dto.setTipoEvento(dtoListado.getTipoEvento());
				dto.setTipoReclamacion(dtoListado.getTipoReclamacion());
				dto.setNroReclamacion(dtoListado.getNroReclamacion());
				dto.setEstado(dtoListado.getEstado());
				dto.setFechaReclamacion(UtilidadFecha.conversionFormatoFechaAAp(dtoListado.getFechaRaclamacion()));
				dto.setFechaRadicacion(UtilidadFecha.conversionFormatoFechaAAp(dtoListado.getFechaRadicacion()));
				dto.setFechaAnulacion(UtilidadFecha.conversionFormatoFechaAAp(dtoListado.getFechaAnulacion()));
				dto.setHoraRadicacion(dtoListado.getHoraRadicacion());
				dto.setHoraAnulacion(dtoListado.getHoraAnulacion());
				dto.setConsecutivoFactura(dtoListado.getFacturas().getConsecutivoFactura()+"");
				if(dtoListado.getUsuariosByUsuarioAnulacion()!=null)
					dto.setUsuarioAnulacion(dtoListado.getUsuariosByUsuarioAnulacion().getLogin());
				if(dtoListado.getUsuariosByUsuarioRadicacion()!=null)
					dto.setUsuarioRadicacion(dtoListado.getUsuariosByUsuarioRadicacion().getLogin());
				resultado.add(dto);
			}
		}
		return resultado;
	}
	
	@Override
	public ArrayList<DtoReclamacionesAccEveFact> consultarReclamacionesAccidenteTransito(int codigo,boolean todas) 
	{
		ArrayList<DtoReclamacionesAccEveFact> resultado=new ArrayList<DtoReclamacionesAccEveFact>();
		ReclamacionesAccEveFactDelegate recDao=new ReclamacionesAccEveFactDelegate();
		ArrayList<ReclamacionesAccEveFact> listado=recDao.consultarReclamacionesAccidenteTransito(codigo,todas);
		if(listado!=null)
		{
			for(ReclamacionesAccEveFact dtoListado:listado)
			{
				DtoReclamacionesAccEveFact dto=new DtoReclamacionesAccEveFact();
				dto.setCodigoPk((int)dtoListado.getCodigoPk());
				dto.setTipoEvento(dtoListado.getTipoEvento());
				dto.setTipoReclamacion(dtoListado.getTipoReclamacion());
				dto.setNroReclamacion(dtoListado.getNroReclamacion());
				dto.setEstado(dtoListado.getEstado());
				dto.setFechaReclamacion(UtilidadFecha.conversionFormatoFechaAAp(dtoListado.getFechaRaclamacion()));
				dto.setFechaRadicacion(UtilidadFecha.conversionFormatoFechaAAp(dtoListado.getFechaRadicacion()));
				dto.setFechaAnulacion(UtilidadFecha.conversionFormatoFechaAAp(dtoListado.getFechaAnulacion()));
				dto.setHoraRadicacion(dtoListado.getHoraRadicacion());
				dto.setHoraAnulacion(dtoListado.getHoraAnulacion());
				dto.setConsecutivoFactura(dtoListado.getFacturas().getConsecutivoFactura()+"");
				if(dtoListado.getUsuariosByUsuarioAnulacion()!=null)
					dto.setUsuarioAnulacion(dtoListado.getUsuariosByUsuarioAnulacion().getLogin());
				if(dtoListado.getUsuariosByUsuarioRadicacion()!=null)
					dto.setUsuarioRadicacion(dtoListado.getUsuariosByUsuarioRadicacion().getLogin());
				resultado.add(dto);
			}
		}
		return resultado;
	}
	
	/**
	 * 
	 * @param codigoPk
	 * @return
	 */
	public DtoReclamacionesAccEveFact consultarReclamacion(int codigoPk)
	{
		HibernateUtil.beginTransaction();
		
		ReclamacionesAccEveFactDelegate recDao=new ReclamacionesAccEveFactDelegate();
		ReclamacionesAccEveFact reclamacion=recDao.findById(codigoPk);
		
		DtoReclamacionesAccEveFact dto=new DtoReclamacionesAccEveFact();
		
		dto.setCodigoPk((int)reclamacion.getCodigoPk());
		dto.setTipoEvento(reclamacion.getTipoEvento());
		dto.setTipoReclamacion(reclamacion.getTipoReclamacion());
		dto.setNroReclamacion(reclamacion.getNroReclamacion());
		dto.setEstado(reclamacion.getEstado());
		dto.setFechaReclamacion(UtilidadFecha.conversionFormatoFechaAAp(reclamacion.getFechaRaclamacion()));
		dto.setFechaRadicacion(UtilidadFecha.conversionFormatoFechaAAp(reclamacion.getFechaRadicacion()));
		dto.setFechaAnulacion(UtilidadFecha.conversionFormatoFechaAAp(reclamacion.getFechaAnulacion()));
		dto.setHoraRadicacion(reclamacion.getHoraRadicacion());
		dto.setHoraAnulacion(reclamacion.getHoraAnulacion());
		dto.setCodigoFactura(reclamacion.getFacturas().getCodigo());
		dto.setHoraReclamacion(reclamacion.getHoraReclamacion());
		dto.setAnioConsReclamacion(reclamacion.getAnioConsReclamacion());
		dto.setRespuestaGlosa(reclamacion.getRespuestaGlosa()==null?"":reclamacion.getRespuestaGlosa());
		dto.setIngreso(reclamacion.getIngresos().getId());
		dto.setNumRadicacionAnterior(reclamacion.getNumRadicaAnterior());
		dto.setUsuarioRegistro(reclamacion.getUsuariosByUsuarioRegistra().getLogin());
		dto.setFechaRegistro(UtilidadFecha.conversionFormatoFechaAAp(reclamacion.getFechaRegistra()));
		dto.setHoraRegistro(reclamacion.getHoraRegistra());
		dto.setUsuarioModifica(reclamacion.getUsuariosByUsuarioModifica().getLogin());
		dto.setFechaModifica(UtilidadFecha.conversionFormatoFechaAAp(reclamacion.getFechaModifca()));
		dto.setHoraModifica(reclamacion.getHoraModifica());
		dto.setNroRadicado(reclamacion.getNroRadicado());
		dto.setMotivoAnulacion(reclamacion.getMotivoAnulacion());
		if(reclamacion.getUsuariosByUsuarioAnulacion()!=null)
			dto.setUsuarioAnulacion(reclamacion.getUsuariosByUsuarioAnulacion().getLogin());
		if(reclamacion.getUsuariosByUsuarioRadicacion()!=null)
			dto.setUsuarioRadicacion(reclamacion.getUsuariosByUsuarioRadicacion().getLogin());
		
		if(dto.getTipoReclamacion().equals(ConstantesIntegridadDominio.acronimoFURIPS))
		{
			DtoAmparoXReclamar amparoXReclamar=new DtoAmparoXReclamar();
			amparoXReclamar.setTotalFacAmpGastMedQX(reclamacion.getAmparoXReclamar().getTotalFacAmpGastMedqx()==null?new BigDecimal(0):reclamacion.getAmparoXReclamar().getTotalFacAmpGastMedqx());
			amparoXReclamar.setTotalFacAmpGastTransMov(reclamacion.getAmparoXReclamar().getTotalFacAmpGastTransmov()==null?new BigDecimal(0):reclamacion.getAmparoXReclamar().getTotalFacAmpGastTransmov());
			amparoXReclamar.setTotalRecAmpGastMedQX(reclamacion.getAmparoXReclamar().getTotalRecAmpGastMedqx()==null?new BigDecimal(0):reclamacion.getAmparoXReclamar().getTotalRecAmpGastMedqx());
			amparoXReclamar.setTotalRecAmpGastTransMov(reclamacion.getAmparoXReclamar().getTotalRecAmpGastTransmov()==null?new BigDecimal(0):reclamacion.getAmparoXReclamar().getTotalRecAmpGastTransmov());
			dto.setAmparoXReclamar(amparoXReclamar);
			
			DtoCertAtenMedicaFurips certificacion=new DtoCertAtenMedicaFurips();
			if(reclamacion.getCertAtenMedicaFurips().getDiagnosticosByPkCeratemedfuripsDxing()!=null)
			{
				certificacion.setTipoCieDxIngreso(reclamacion.getCertAtenMedicaFurips().getDiagnosticosByPkCeratemedfuripsDxing().getId().getTipoCie());
				certificacion.setAcronimoDxIngreso(reclamacion.getCertAtenMedicaFurips().getDiagnosticosByPkCeratemedfuripsDxing().getId().getAcronimo());
			}
			if(reclamacion.getCertAtenMedicaFurips().getDiagnosticosByPkCeratemedfuripsDxegr()!=null)
			{
				certificacion.setTipoCieDxEgreso(reclamacion.getCertAtenMedicaFurips().getDiagnosticosByPkCeratemedfuripsDxegr().getId().getTipoCie());
				certificacion.setAcronimoDxEgreso(reclamacion.getCertAtenMedicaFurips().getDiagnosticosByPkCeratemedfuripsDxegr().getId().getAcronimo());
			}
			if(reclamacion.getCertAtenMedicaFurips().getDiagnosticosByPkCeratemedfuripsReling1()!=null)
			{
				certificacion.setTipoCieDxRel1Ingreso(reclamacion.getCertAtenMedicaFurips().getDiagnosticosByPkCeratemedfuripsReling1().getId().getTipoCie());
				certificacion.setAcronimoDxRel1Ingreso(reclamacion.getCertAtenMedicaFurips().getDiagnosticosByPkCeratemedfuripsReling1().getId().getAcronimo());
			}
			if(reclamacion.getCertAtenMedicaFurips().getDiagnosticosByPkCeratemedfuripsReling2()!=null)
			{
				certificacion.setTipoCieDxRel2Ingreso(reclamacion.getCertAtenMedicaFurips().getDiagnosticosByPkCeratemedfuripsReling2().getId().getTipoCie());
				certificacion.setAcronimoDxRel2Ingreso(reclamacion.getCertAtenMedicaFurips().getDiagnosticosByPkCeratemedfuripsReling2().getId().getAcronimo());
			}
			if(reclamacion.getCertAtenMedicaFurips().getDiagnosticosByPkCeratemedfuripsRelegr1()!=null)
			{
				certificacion.setTipoCieDxRel1Egreso(reclamacion.getCertAtenMedicaFurips().getDiagnosticosByPkCeratemedfuripsRelegr1().getId().getTipoCie());
				certificacion.setAcronimoDxRel1Egreso(reclamacion.getCertAtenMedicaFurips().getDiagnosticosByPkCeratemedfuripsRelegr1().getId().getAcronimo());
			}
			if(reclamacion.getCertAtenMedicaFurips().getDiagnosticosByPkCeratemedfuripsRelegr2()!=null)
			{
				certificacion.setTipoCieDxRel2Egreso(reclamacion.getCertAtenMedicaFurips().getDiagnosticosByPkCeratemedfuripsRelegr2().getId().getTipoCie());
				certificacion.setAcronimoDxRel2Egreso(reclamacion.getCertAtenMedicaFurips().getDiagnosticosByPkCeratemedfuripsRelegr2().getId().getAcronimo());
			}
			
			if (reclamacion.getCertAtenMedicaFurips().getTiposIdentificacion() != null) {
				
			} else {
				certificacion.setTipoDocumentoMedico("");
			}
			
			certificacion.setTipoDocumentoMedico(
					(reclamacion.getCertAtenMedicaFurips().getTiposIdentificacion() != null) ? 
							reclamacion.getCertAtenMedicaFurips().getTiposIdentificacion().getAcronimo() : "");
			
			certificacion.setNumeroDocumentoMedico(
					(reclamacion.getCertAtenMedicaFurips().getNroDocumentoMedico() != null) ? 
							reclamacion.getCertAtenMedicaFurips().getNroDocumentoMedico() : "");
			
			certificacion.setNumeroRegistroMedico(
					(reclamacion.getCertAtenMedicaFurips().getNroRegistroMedico() != null) ?
							reclamacion.getCertAtenMedicaFurips().getNroRegistroMedico() : "");
			
			certificacion.setPrimerApellidoMedico(
					(reclamacion.getCertAtenMedicaFurips().getPrimerApellidoMedico() != null) ?
							reclamacion.getCertAtenMedicaFurips().getPrimerApellidoMedico() : "");
			
			certificacion.setSegundoApellidoMedico(
					(reclamacion.getCertAtenMedicaFurips().getSegundoApellidoMedico() != null) ?
							reclamacion.getCertAtenMedicaFurips().getSegundoApellidoMedico() : "");
			
			certificacion.setPrimerNombreMedico(
					(reclamacion.getCertAtenMedicaFurips().getPrimerNombreMedico() != null) ?
							reclamacion.getCertAtenMedicaFurips().getPrimerNombreMedico() : "");
			
			certificacion.setSegundoNombreMedico(
					(reclamacion.getCertAtenMedicaFurips().getSegundoNombreMedico() != null) ?
							reclamacion.getCertAtenMedicaFurips().getSegundoNombreMedico() : "");
			
			dto.setCertAtenMedicaFurips(certificacion);
		}
		else if(dto.getTipoReclamacion().equals(ConstantesIntegridadDominio.acronimoFURPRO))
		{
			DtoServiciosReclamados serviciosReclamados=new DtoServiciosReclamados();
			serviciosReclamados.setAdaptacionProtesis(reclamacion.getServiciosReclamados().getAdaptacionProtesis());
			serviciosReclamados.setDescProtesisServicioPrestado(reclamacion.getServiciosReclamados().getDescProtServPres()==null?"":reclamacion.getServiciosReclamados().getDescProtServPres());
			serviciosReclamados.setProtesis(reclamacion.getServiciosReclamados().getProtesis());
			serviciosReclamados.setRehabilitacion(reclamacion.getServiciosReclamados().getRehabilitacion());
			serviciosReclamados.setValorAdaptacionProtesis(reclamacion.getServiciosReclamados().getValorAdapProtesis());
			serviciosReclamados.setValorProtesis(reclamacion.getServiciosReclamados().getValorProtesis());
			serviciosReclamados.setValorRehabilitacion(reclamacion.getServiciosReclamados().getValorRehabilitacion());
			dto.setServiciosReclamados(serviciosReclamados);
			
			DtoCertAtenMedicaFurpro certificacion=new DtoCertAtenMedicaFurpro();
			if(reclamacion.getCertAtenMedicaFurpro().getDiagnosticosByPkCeratemedfurproDxegr()!=null)
			{
				certificacion.setTipoCieDxEgreso(reclamacion.getCertAtenMedicaFurpro().getDiagnosticosByPkCeratemedfurproDxegr().getId().getTipoCie());
				certificacion.setAcronimoDxEgreso(reclamacion.getCertAtenMedicaFurpro().getDiagnosticosByPkCeratemedfurproDxegr().getId().getAcronimo());
			}
			if(reclamacion.getCertAtenMedicaFurpro().getDiagnosticosByPkCeratemedfurproRelegr1()!=null)
			{
				certificacion.setTipoCieDxRel1Egreso(reclamacion.getCertAtenMedicaFurpro().getDiagnosticosByPkCeratemedfurproRelegr1().getId().getTipoCie());
				certificacion.setAcronimoDxRel1Egreso(reclamacion.getCertAtenMedicaFurpro().getDiagnosticosByPkCeratemedfurproRelegr1().getId().getAcronimo());
			}
			if(reclamacion.getCertAtenMedicaFurpro().getDiagnosticosByPkCeratemedfurproRelegr2()!=null)
			{
				certificacion.setTipoCieDxRel2Egreso(reclamacion.getCertAtenMedicaFurpro().getDiagnosticosByPkCeratemedfurproRelegr2().getId().getTipoCie());
				certificacion.setAcronimoDxRel2Egreso(reclamacion.getCertAtenMedicaFurpro().getDiagnosticosByPkCeratemedfurproRelegr2().getId().getAcronimo());
			}
			if(reclamacion.getCertAtenMedicaFurpro().getDiagnosticosByPkCeratemedfurproRelegr3()!=null)
			{
				certificacion.setTipoCieDxRel3Egreso(reclamacion.getCertAtenMedicaFurpro().getDiagnosticosByPkCeratemedfurproRelegr3().getId().getTipoCie());
				certificacion.setAcronimoDxRel3Egreso(reclamacion.getCertAtenMedicaFurpro().getDiagnosticosByPkCeratemedfurproRelegr3().getId().getAcronimo());
			}
			if(reclamacion.getCertAtenMedicaFurpro().getDiagnosticosByPkCeratemedfurproRelegr4()!=null)
			{
				certificacion.setTipoCieDxRel4Egreso(reclamacion.getCertAtenMedicaFurpro().getDiagnosticosByPkCeratemedfurproRelegr4().getId().getTipoCie());
				certificacion.setAcronimoDxRel4Egreso(reclamacion.getCertAtenMedicaFurpro().getDiagnosticosByPkCeratemedfurproRelegr4().getId().getAcronimo());
			}
			dto.setCertAtenMedicaFurpro(certificacion);
			
		}
		HibernateUtil.endTransaction();
		
		return dto;
	}
	
	
	/**
	 * 
	 * @param codigoPk
	 * @return
	 */
	public boolean radicarReclamacion(DtoReclamacionesAccEveFact amparoXReclamar)
	{
		HibernateUtil.beginTransaction();
		
		ReclamacionesAccEveFactDelegate recDao=new ReclamacionesAccEveFactDelegate();
		ReclamacionesAccEveFact reclamacion=recDao.findById(amparoXReclamar.getCodigoPk());
		reclamacion.setEstado(ConstantesIntegridadDominio.acronimoRadicado);
		reclamacion.setNroRadicado(amparoXReclamar.getNroRadicado());
		reclamacion.setFechaModifca(UtilidadFecha.conversionFormatoFechaStringDate(UtilidadFecha.conversionFormatoFechaABD(amparoXReclamar.getFechaModifica())));
		reclamacion.setFechaRadica(UtilidadFecha.conversionFormatoFechaStringDate(UtilidadFecha.conversionFormatoFechaABD(amparoXReclamar.getFechaModifica())));
		reclamacion.setFechaRadicacion(UtilidadFecha.conversionFormatoFechaStringDate(UtilidadFecha.conversionFormatoFechaABD(amparoXReclamar.getFechaRadicacion())));
		reclamacion.setHoraModifica(amparoXReclamar.getHoraModifica());
		reclamacion.setHoraRadica(amparoXReclamar.getHoraModifica());
		reclamacion.setHoraRadicacion(amparoXReclamar.getHoraRadicacion());
		
		Usuarios usuModifica=new Usuarios();
		UsuariosDelegate usuDao=new UsuariosDelegate();
		usuModifica=usuDao.findById(amparoXReclamar.getUsuarioModifica());
		reclamacion.setUsuariosByUsuarioModifica(usuModifica);
		reclamacion.setUsuariosByUsuarioRadicacion(usuModifica);
		
		recDao.persist(reclamacion);
		
		
		HibernateUtil.endTransaction();		
		return true;
	}

	/**
	 * 
	 * @param codigoPk
	 * @return
	 */
	public boolean anularReclamacion(DtoReclamacionesAccEveFact amparoXReclamar)
	{
		HibernateUtil.beginTransaction();
		
		ReclamacionesAccEveFactDelegate recDao=new ReclamacionesAccEveFactDelegate();
		ReclamacionesAccEveFact reclamacion=recDao.findById(amparoXReclamar.getCodigoPk());
		reclamacion.setEstado(ConstantesIntegridadDominio.acronimoEstadoAnulado);
		
		reclamacion.setMotivoAnulacion(amparoXReclamar.getMotivoAnulacion());
		reclamacion.setFechaModifca(UtilidadFecha.conversionFormatoFechaStringDate(UtilidadFecha.conversionFormatoFechaABD(amparoXReclamar.getFechaModifica())));
		reclamacion.setFechaAnulacion(UtilidadFecha.conversionFormatoFechaStringDate(UtilidadFecha.conversionFormatoFechaABD(amparoXReclamar.getFechaModifica())));
		reclamacion.setHoraModifica(amparoXReclamar.getHoraModifica());
		reclamacion.setHoraAnulacion(amparoXReclamar.getHoraModifica());
		
		Usuarios usuModifica=new Usuarios();
		UsuariosDelegate usuDao=new UsuariosDelegate();
		usuModifica=usuDao.findById(amparoXReclamar.getUsuarioModifica());
		reclamacion.setUsuariosByUsuarioModifica(usuModifica);
		reclamacion.setUsuariosByUsuarioAnulacion(usuModifica);
		
		recDao.persist(reclamacion);
		
		
		HibernateUtil.endTransaction();		
		return true;
	}

	

}
