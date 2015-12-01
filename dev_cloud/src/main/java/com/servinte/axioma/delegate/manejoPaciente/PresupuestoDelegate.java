package com.servinte.axioma.delegate.manejoPaciente;

import java.util.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.math.BigDecimal;


import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;


import util.UtilidadFecha;

import com.servinte.axioma.dto.manejoPaciente.AutorizacionsSubServDto;
import com.servinte.axioma.dto.manejoPaciente.CantidadAutorizacionesPacienteDto;
import com.servinte.axioma.dto.manejoPaciente.UsuariosConsumidoresPresupuestoDto;
import com.servinte.axioma.dto.manejoPaciente.ValorClaseInventariosPacienteDto;
import com.servinte.axioma.dto.manejoPaciente.ValorGruposServicioPacienteDto;
import com.servinte.axioma.fwk.exception.BDException;
import com.servinte.axioma.fwk.persistencia.interfaz.IPersistenciaSvc;
import com.servinte.axioma.fwk.persistencia.servicio.PersistenciaSvc;
import com.servinte.axioma.orm.ClaseInventario;
import com.servinte.axioma.orm.DiagnosticosId;
import com.servinte.axioma.orm.Personas;


/**
 * @author davgommo
 * @version 1.0
 * @created 21-jun-2012 02:23:59 p.m.
 */
public class PresupuestoDelegate {
	
	IPersistenciaSvc persistenciaSvc;
	
	/**
	 * Este método consulta los usuarios consumidores del presupuesto en el sistema dependiendo de los parámetros enviados
	 * @param fechaInicial
	 * @param fechaFinal
     * @param autorizaciones
     * @param convenio
     * @param viaIngreso
     * @param grupoSeleccionado
     * @param inventarioSeleccionado
     * @param nombreDiagnostico
     * @param valorInicial
     * @param valorFinal
     * @param tipoIdentificacion
     * @param numeroIdentificacion
	 * @return ArrayList<UsuariosConsumidoresPresupuestoDto> listaUsuarios
	 */
	@SuppressWarnings("unchecked")
	public List<UsuariosConsumidoresPresupuestoDto> consultarUsuariosAutor(String fechaInicial, String fechaFinal, String autorizaciones, String convenio, String viaIngreso, String grupoSeleccionado, String inventarioSeleccionado, String nombreDiagnostico, String valorInicial, String valorFinal,String tipoIdentificacion, String numeroIdentificacion) throws BDException{
		persistenciaSvc= new PersistenciaSvc();
		int codInventario=Integer.parseInt(inventarioSeleccionado);
		int codGrupo=Integer.parseInt(grupoSeleccionado);
		int convenios=Integer.parseInt(convenio);
		int viasIngreso= Integer.parseInt(viaIngreso);
		
		String autor="AUTOR";
	
		Criteria criteriaUsuarios = persistenciaSvc.getSession().createCriteria(Personas.class,"persona");
		if (autorizaciones.equals("2"))	
			{
				criteriaUsuarios.createAlias("persona.facturases", "facturas");
			}
		criteriaUsuarios.createAlias("persona.pacientes", "paciente")
			.createAlias("persona.tiposIdentificacion", "tiposIdentificacion")
			.createAlias("paciente.autorizacionesEntidadesSubs", "autEntSub", Criteria.INNER_JOIN)
			.createAlias("autEntSub.autorizacionesCapitacionSubs", "autCapSub", Criteria.INNER_JOIN);
		criteriaUsuarios.add(Restrictions.between("autEntSub.fechaAutorizacion", 
				UtilidadFecha.conversionFormatoFechaStringDate(fechaInicial), 
				UtilidadFecha.conversionFormatoFechaStringDate(fechaFinal)))
			.add(Restrictions.eq("autEntSub.estado", autor));
		
		/* CRITERIOS DE BUSQUEDA*/		
		
		if (!convenio.equals("-1")||!viaIngreso.equals("-1")||!nombreDiagnostico.equals(""))
			{
				criteriaUsuarios.createAlias("paciente.cuentases", "cuentas", Criteria.INNER_JOIN);
							}
		if (!convenio.equals("-1")||!nombreDiagnostico.equals(""))
		{
			criteriaUsuarios.createAlias("paciente.subCuentases", "subCuentas", Criteria.INNER_JOIN);
		}
		if (!convenio.equals("-1"))
			{
				criteriaUsuarios.createAlias("subCuentas.convenios", "convenio", Criteria.INNER_JOIN);
				criteriaUsuarios.add(Restrictions.eq("convenio.codigo", convenios));		
			}
		
		if(!viaIngreso.equals("-1"))
			{
				criteriaUsuarios.createAlias("cuentas.viasIngreso", "viaIngreso", Criteria.INNER_JOIN);
				criteriaUsuarios.add(Restrictions.eq("viaIngreso.codigo", viasIngreso));	
			}
		if (!nombreDiagnostico.equals(""))
			{
				criteriaUsuarios.createAlias("subCuentas.solicitudesSubcuentas", "solSub", Criteria.INNER_JOIN)
					.createAlias("solSub.solicitudes", "solicitud");
			}
		
		if(!grupoSeleccionado.equals("-1"))
			{
			if(inventarioSeleccionado.equals("-1"))
			{
			criteriaUsuarios.createAlias("autEntSub.autorizacionesEntSubServis", "autEntSS", Criteria.INNER_JOIN)
					.createAlias("autEntSS.servicios", "servicio", Criteria.INNER_JOIN)
					.createAlias("servicio.gruposServicios", "grupoServicio", Criteria.INNER_JOIN);
				criteriaUsuarios.add(Restrictions.eq("grupoServicio.codigo", codGrupo));
			}
			}
	
		if (!nombreDiagnostico.equals(""))
			{ 
	
			}
		if (!numeroIdentificacion.equals(""))
			{ 	
				criteriaUsuarios.add(Restrictions.eq("persona.numeroIdentificacion",numeroIdentificacion));
			}
		
		if (!inventarioSeleccionado.equals("-1"))
		   {
			if(grupoSeleccionado.equals("-1"))
			{
				Criteria criteriaSubGrupo = persistenciaSvc.getSession().createCriteria(ClaseInventario.class, "claseInventario");
				criteriaSubGrupo.createAlias("claseInventario.grupoInventarios", "grupos");
				criteriaSubGrupo.createAlias("grupos.subgrupoInventarios", "subGrupos", Criteria.INNER_JOIN);
				criteriaSubGrupo.add(Restrictions.eq("claseInventario.codigo" , codInventario));
				criteriaSubGrupo.setProjection(Projections.projectionList()
					.add(Projections.property("subGrupos.codigo")));
				criteriaUsuarios.createAlias("autEntSub.autorizacionesEntSubArticus", "autEntSA", Criteria.INNER_JOIN)
					.createAlias("autEntSA.articulo", "articulo", Criteria.INNER_JOIN);
				criteriaUsuarios.add(Restrictions.in("articulo.subgrupo" , criteriaSubGrupo.list()));
			}
		   }
		
		/*  Proyecciones*/
		ProjectionList projectionList = Projections.projectionList();
				projectionList.add(Projections.property("persona.primerApellido"),        "primerApellidoPersona");
				projectionList.add(Projections.property("persona.segundoApellido"),       "segundoApellidoPersona");
				projectionList.add(Projections.property("persona.primerNombre"),          "primerNombrePersona");
				projectionList.add(Projections.property("persona.segundoNombre"),         "segundoNombrePersona");
				projectionList.add(Projections.property("tiposIdentificacion.acronimo"),   "nombreTipoIdentificacion");
				projectionList.add(Projections.property("persona.numeroIdentificacion"),   "numeroIdentificacion");
				projectionList.add(Projections.property("persona.codigo"),                 "codigoPaciente");
				
			
				//);
		criteriaUsuarios.setProjection(projectionList);
		criteriaUsuarios.addOrder(Order.asc("persona.primerApellido"));
		criteriaUsuarios.setResultTransformer(Transformers.aliasToBean(UsuariosConsumidoresPresupuestoDto.class));
		
		List<UsuariosConsumidoresPresupuestoDto> listaUsuarios=  (List<UsuariosConsumidoresPresupuestoDto>)criteriaUsuarios.list();
		
	 	return listaUsuarios;
	}
	
	/**
	 * Este método consulta los Grupos de Servicios asociados a un paciente
	 * @param codigoPaciente
	 * @param autorizado
	 * @return ArrayList<ValorGruposServicioPacienteDto> listaGrupos
	 */
	@SuppressWarnings("unchecked")
	public List<ValorGruposServicioPacienteDto> consultarServicios (int codigoPaciente, String autorizado, Date fechaInicial, Date fechaFinal, String grupo, String clase)
	{
		List<ValorGruposServicioPacienteDto> listaGrupos= new ArrayList<ValorGruposServicioPacienteDto>();
		List<ValorGruposServicioPacienteDto> listaGruposF= new ArrayList<ValorGruposServicioPacienteDto>();
		List<ValorGruposServicioPacienteDto> listaGrup= new ArrayList<ValorGruposServicioPacienteDto>();
		persistenciaSvc= new PersistenciaSvc();
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("codigoPaciente", codigoPaciente);
		params.put("fechaInicial", fechaInicial);
		params.put("fechaFinal", fechaFinal);
		
		
		// Si la busqueda se realiza por autorizaciones facturadas
		if (autorizado.equals("2"))
			{
				
				listaGrupos=(List<ValorGruposServicioPacienteDto>)persistenciaSvc.createNamedQuery("presupuesto.consultarGrupoServicioAut",params);
				listaGruposF=(List<ValorGruposServicioPacienteDto>)persistenciaSvc.createNamedQuery("presupuesto.consultarGrupoServicioFac",params);
			
				List<ValorGruposServicioPacienteDto> listaGruposTT= new ArrayList<ValorGruposServicioPacienteDto>();
	
				for (ValorGruposServicioPacienteDto val1: listaGrupos)
			      {
					for (ValorGruposServicioPacienteDto val2: listaGruposF)
					{
					if (val1.getNombreGrupoServicio().equals(val2.getNombreGrupoServicio()))
					{
						val1.setValorFacturado(val2.getValorAutorizado());
						break;
					}
					else
					{
						val1.setValorFacturado(0);
					}
						
					}
					
			      }
				listaGruposTT=listaGrupos;
           
			return listaGruposTT;
			}
			else //Si la busqueda se realiza por cualquier autorizacion
				{
		
					listaGrupos=(List<ValorGruposServicioPacienteDto>)persistenciaSvc.createNamedQuery("presupuesto.consultarGrupoServicioAut",params);
							
				
				double cantidad=0;
				for (ValorGruposServicioPacienteDto val: listaGrupos)
				{
					if (!val.getNombreGrupoServicio().equals("Insumos y Materiales")&&!val.getNombreGrupoServicio().equals("Honorarios Medicos")&&!val.getNombreGrupoServicio().equals("Derechos de Sala"))
					{
						listaGrup.add(val);
						
					} 
					else{cantidad++;}
					
				}
				if (cantidad==0)
					{cantidad=1;}
				
				listaGrupos=listaGrup;


			
				return listaGrupos;
				}
	}
	 
	/**
	 * Este método consulta las Clases de Inventarios asociadas a un paciente
	 * @param codigoPaciente
	 * @param autorizado
	 * @return ArrayList<ValorClaseInventariosPacienteDto> listaGrupos
	 */
	@SuppressWarnings("unchecked")
	public List<ValorClaseInventariosPacienteDto> consultarInventario (int codigoPaciente, String autorizado, Date fechaInicial, Date fechaFinal, String grupo, String clase)
	{
		List<ValorClaseInventariosPacienteDto> listaInventario= new ArrayList<ValorClaseInventariosPacienteDto>();
		List<ValorClaseInventariosPacienteDto> listaInventarioF= new ArrayList<ValorClaseInventariosPacienteDto>();
		List<ValorClaseInventariosPacienteDto> listaInventarioT= new ArrayList<ValorClaseInventariosPacienteDto>();
		persistenciaSvc= new PersistenciaSvc();
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("codigoPaciente", codigoPaciente);
		params.put("fechaInicial", fechaInicial);
		params.put("fechaFinal", fechaFinal);

		
		// Si la busqueda se realiza por autorizaciones facturadas
		if (autorizado.equals("2"))
			{   
			List<Object[]> listaObject=null;
			List<Object[]> listaObjectF=null;
	
			listaObject=(List<Object[]>)persistenciaSvc.createNamedQuery("presupuesto.consultarClaseInventarioAut", params);
			listaObjectF=(List<Object[]>)persistenciaSvc.createNamedQuery("presupuesto.consultarClaseInventarioFac", params);
			
	
			for (Object[] lista: listaObject)
			{ ValorClaseInventariosPacienteDto dto= new ValorClaseInventariosPacienteDto();
			dto.setnombreClaseInventario((String)lista[0]);
			try{
			dto.setCantidadAutorizada((Double)lista[1]);
			}
			catch (Exception e)
			{
				dto.setCantidadAutorizada(0);
			}
			try {dto.setValorAutorizado((Double)lista[2]);}
			catch (Exception e)
			{
				dto.setValorAutorizado(0);
			}
			try {dto.setValorFacturado((Double)lista[3]);}
			catch (Exception e)
			{
				dto.setValorFacturado(0);
			}
			
			listaInventario.add(dto);
			}
			for (Object[] lista: listaObjectF)
			{ ValorClaseInventariosPacienteDto dto= new ValorClaseInventariosPacienteDto();
			dto.setnombreClaseInventario((String)lista[0]);
			try{
			dto.setCantidadAutorizada((Double)lista[1]);
			}
			catch (Exception e)
			{
				dto.setCantidadAutorizada(0);
			}
			try {dto.setValorAutorizado((Double)lista[2]);}
			catch (Exception e)
			{
				dto.setValorAutorizado(0);
			}
			try {dto.setValorFacturado((Double)lista[3]);}
			catch (Exception e)
			{
				dto.setValorFacturado(0);
			}
			
			listaInventarioF.add(dto);
			}
			
		    for(ValorClaseInventariosPacienteDto val1: listaInventario)
					{
						for(ValorClaseInventariosPacienteDto val2: listaInventarioF)
							{
							if (val1.getnombreClaseInventario().equals(val2.getnombreClaseInventario()))
								{
								val1.setValorFacturado(val2.getValorFacturado());
								break;
								}
							else {
								val1.setValorFacturado(0);
							}
							}
			
				}
		    listaInventarioT=listaInventario;
			return listaInventarioT;
			}
			else{
				//Si la busqueda se realiza por cualquier autorizacion
				List<Object[]> listaObject=null;
	
				listaObject=(List<Object[]>)persistenciaSvc.createNamedQuery("presupuesto.consultarClaseInventarioAut", params);
				
		
					for (Object[] lista: listaObject)
				{ ValorClaseInventariosPacienteDto dto= new ValorClaseInventariosPacienteDto();
				dto.setnombreClaseInventario((String)lista[0]);
				try{
				dto.setCantidadAutorizada((Double)lista[1]);
				}
				catch (Exception e)
				{
					dto.setCantidadAutorizada(0);
				}
				try {dto.setValorAutorizado((Double)lista[2]);}
				catch (Exception e)
				{
					dto.setValorAutorizado(0);
				}
				try {dto.setValorFacturado((Double)lista[3]);}
				catch (Exception e)
				{
					dto.setValorFacturado(0);
				}
				
				listaInventario.add(dto);
				}
				return listaInventario;
				}
	}
	
	/**
	 * Este método consulta la cantidad de autorizaciones en estado "Autorizado" asociadas a un paciente
	 * y la cantidad de autorizaciones facturadas
	 * @param codigoPaciente
	 * @return CantidadAutorizacionesPacienteDto listaCantidad
	 */
	@SuppressWarnings("unchecked")
	public List<CantidadAutorizacionesPacienteDto>consultarCantidadAutorizaciones (int codigoPaciente, Date fechaInicial, Date fechaFinal)
	{ 
		List<CantidadAutorizacionesPacienteDto> listaCantidad = new ArrayList <CantidadAutorizacionesPacienteDto>();
		List<CantidadAutorizacionesPacienteDto>  listaCantidad1 = new ArrayList <CantidadAutorizacionesPacienteDto>();
		List<CantidadAutorizacionesPacienteDto>  listaCantidad12 = new ArrayList <CantidadAutorizacionesPacienteDto>();
		List<CantidadAutorizacionesPacienteDto>  listaCantidad13 = new ArrayList <CantidadAutorizacionesPacienteDto>();
		List<CantidadAutorizacionesPacienteDto>  listaCantidad2 = new ArrayList <CantidadAutorizacionesPacienteDto>();
		
		persistenciaSvc= new PersistenciaSvc();
		Map<String,Object> params = new HashMap<String,Object>(); 
		params.put("codigoPaciente", codigoPaciente);
		params.put("fechaInicial", fechaInicial);
		params.put("fechaFinal", fechaFinal);
		
		listaCantidad = (List<CantidadAutorizacionesPacienteDto>)persistenciaSvc.createNamedQuery("presupuesto.consultarCantidadAutorizaciones", params);
		listaCantidad1 = (List<CantidadAutorizacionesPacienteDto>)persistenciaSvc.createNamedQuery("presupuesto.consultarCantidadIngresos", params);
		listaCantidad12 = (List<CantidadAutorizacionesPacienteDto>)persistenciaSvc.createNamedQuery("presupuesto.consultarCantidadIngresosXAut", params);
		listaCantidad13 = (List<CantidadAutorizacionesPacienteDto>)persistenciaSvc.createNamedQuery("presupuesto.consultarCantidadIngresosConAut", params);
		listaCantidad2 = (List<CantidadAutorizacionesPacienteDto>)persistenciaSvc.createNamedQuery("presupuesto.consultarCantidadIngresosEst", params);
		
		
		
		List<CantidadAutorizacionesPacienteDto> listaFinal= new ArrayList <CantidadAutorizacionesPacienteDto>();
		for(CantidadAutorizacionesPacienteDto val1: listaCantidad)
		{
			for(CantidadAutorizacionesPacienteDto val2: listaCantidad12)
			{
				val1.setCantidadIngresos(val2.getCantidadAutorizaciones());
			}
		
			
			listaFinal.add(val1);
		}
		for (CantidadAutorizacionesPacienteDto val: listaFinal)
		{   long i= val.getCantidadIngresos();
			for (CantidadAutorizacionesPacienteDto val1: listaCantidad1)
			{
				val.setCantidadIngresos(i+val1.getCantidadAutorizaciones());
			}
			i= val.getCantidadIngresos();
			for (CantidadAutorizacionesPacienteDto val1: listaCantidad13)
			{
				val.setCantidadIngresos(i-val1.getCantidadAutorizaciones());
			}
		}
		

		return listaFinal;
	}
	
	@SuppressWarnings("unchecked")
	public List<AutorizacionsSubServDto>consultarValorAutS (int codigoPaciente, String autorizado, Date fechaInicial, Date fechaFinal, String grupo, String clase)
	{ 
		List<AutorizacionsSubServDto> listaCantidad = new ArrayList <AutorizacionsSubServDto>();
		List<AutorizacionsSubServDto> listaCantidadF = new ArrayList <AutorizacionsSubServDto>();
		
		persistenciaSvc= new PersistenciaSvc();
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("codigoPaciente", codigoPaciente);
		params.put("fechaInicial", fechaInicial);
		params.put("fechaFinal", fechaFinal);

		if (autorizado.equals("1"))
		{
		listaCantidad = (List<AutorizacionsSubServDto>)persistenciaSvc.createNamedQuery("presupuesto.consultarValoresAutServ", params);
		}
		if (autorizado.equals("2"))
		{
			listaCantidad = (List<AutorizacionsSubServDto>)persistenciaSvc.createNamedQuery("presupuesto.consultarValoresAutServ", params);
			listaCantidadF = (List<AutorizacionsSubServDto>)persistenciaSvc.createNamedQuery("presupuesto.consultarValoresAutServF", params);
			
			for (AutorizacionsSubServDto var: listaCantidad)
			{
				for (AutorizacionsSubServDto var1: listaCantidadF)
				{
					if((var.getConsecutivo()==var1.getConsecutivo()) && (var.getNombre().equals(var1.getNombre())) )
					{
						var.setValorTarifaSF(var1.getValorTarifaS());
					}
				
				}
				
			}
		}
		
		return listaCantidad;
	}

	@SuppressWarnings("unchecked")
	public List<AutorizacionsSubServDto>consultarValorAutA (int codigoPaciente, String autorizado, Date fechaInicial, Date fechaFinal, String grupo, String clase)
	{ 
		List<AutorizacionsSubServDto> listaCantidad = new ArrayList <AutorizacionsSubServDto>();
		List<AutorizacionsSubServDto> listaCantidadF = new ArrayList <AutorizacionsSubServDto>();
		List<Object[]> listaObject=null;
		List<Object[]> listaObjectF=null;
		
		persistenciaSvc= new PersistenciaSvc();
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("codigoPaciente", codigoPaciente);
		params.put("fechaInicial", fechaInicial);
		params.put("fechaFinal", fechaFinal);

		if(autorizado.equals("1"))
		{
		listaObject=(List<Object[]>)persistenciaSvc.createNamedQuery("presupuesto.consultarValoresAutArt", params);
		
		for (Object[] lista: listaObject)
		{ AutorizacionsSubServDto dto= new AutorizacionsSubServDto();
		dto.setNombre((String)lista[1]);
		try{
		dto.setConsecutivo((Integer)lista[0]);
		}
		catch (Exception e)
		{
			dto.setConsecutivo(0);
		}
		try {dto.setCantidad((Integer)lista[2]);}
		catch (Exception e)
		{
			dto.setCantidad(0);
		}
		try {dto.setValorTarifaS((Double)lista[3]);}
		catch (Exception e)
		{
			dto.setValorTarifaS(0);
		}
		try {dto.setCodGrupClas((Integer)lista[4]);}
		catch (Exception e)
		{
			dto.setNumSol(0);
		}
		
		
		listaCantidad.add(dto);
		}
		}
		if(autorizado.equals("2"))
		{
		listaObject=(List<Object[]>)persistenciaSvc.createNamedQuery("presupuesto.consultarValoresAutArt", params);
		listaObjectF=(List<Object[]>)persistenciaSvc.createNamedQuery("presupuesto.consultarValoresAutArtF", params);
		
		for (Object[] lista: listaObject)
		{ AutorizacionsSubServDto dto= new AutorizacionsSubServDto();
		dto.setNombre((String)lista[1]);
		try{
		dto.setConsecutivo((Integer)lista[0]);
		}
		catch (Exception e)
		{
			dto.setConsecutivo(0);
		}
		try {dto.setCantidad((Integer)lista[2]);}
		catch (Exception e)
		{
			dto.setCantidad(0);
		}
		try {dto.setValorTarifaS((Double)lista[3]);}
		catch (Exception e)
		{
			dto.setValorTarifaS(0);
		}
		try {dto.setCodGrupClas((Integer)lista[4]);}
		catch (Exception e)
		{
			dto.setNumSol(0);
		}
		
		listaCantidad.add(dto);
		}
		
		for (Object[] lista: listaObjectF)
		{ AutorizacionsSubServDto dto= new AutorizacionsSubServDto();
		dto.setNombre((String)lista[1]);
		try{
		dto.setConsecutivo((Integer)lista[0]);
		}
		catch (Exception e)
		{
			dto.setConsecutivo(0);
		}
		try {dto.setCantidad((Integer)lista[2]);}
		catch (Exception e)
		{
			dto.setCantidad(0);
		}
		try {dto.setValorTarifaS((Double)lista[3]);}
		catch (Exception e)
		{
			dto.setValorTarifaS(0);
		}
		try {dto.setCodGrupClas((Integer)lista[4]);}
		catch (Exception e)
		{
			dto.setNumSol(0);
		}
		
		listaCantidadF.add(dto);
		}
		
		for (AutorizacionsSubServDto var: listaCantidad)
		{
			for (AutorizacionsSubServDto var1: listaCantidadF)
			{
				if((var.getConsecutivo()==var1.getConsecutivo()) && (var.getNombre().equals(var1.getNombre())) )
				{
					var.setValorTarifaSF(var1.getValorTarifaS());
				}
			
			}
			
		}
		}
		return listaCantidad;
	}
	@SuppressWarnings("unchecked")
	public List<AutorizacionsSubServDto>consultarAutorizacionesDiag (int codigoPaciente, String autorizado, Date fechaInicial, Date fechaFinal, String grupo, String clase)
	{ 
		List<AutorizacionsSubServDto> listaCantidadSer = new ArrayList <AutorizacionsSubServDto>();
		List<AutorizacionsSubServDto> listaCantidadOrd = new ArrayList <AutorizacionsSubServDto>();
		
		persistenciaSvc= new PersistenciaSvc();
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("codigoPaciente", codigoPaciente);
		params.put("fechaInicial", fechaInicial);
		params.put("fechaFinal", fechaFinal);

		listaCantidadSer = (List<AutorizacionsSubServDto>)persistenciaSvc.createNamedQuery("presupuesto.consultarAutorizacionesDiagnosticoSol", params);
		listaCantidadOrd = (List<AutorizacionsSubServDto>)persistenciaSvc.createNamedQuery("presupuesto.consultarAutorizacionesDiagnosticoOrd", params);
		
		for (AutorizacionsSubServDto aut: listaCantidadOrd)
		{
			listaCantidadSer.add(aut);
		}
		
		return listaCantidadSer;
	}
	@SuppressWarnings("unchecked")
	public List<AutorizacionsSubServDto>consultarAutorizacionesViaIngreso (int codigoPaciente, String autorizado, Date fechaInicial, Date fechaFinal, String grupo, String clase)
	{ 
		List<AutorizacionsSubServDto> listaCantidadSer = new ArrayList <AutorizacionsSubServDto>();
		List<AutorizacionsSubServDto> listaCantidadOrd = new ArrayList <AutorizacionsSubServDto>();
		
		persistenciaSvc= new PersistenciaSvc();
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("codigoPaciente", codigoPaciente);
		params.put("fechaInicial", fechaInicial);
		params.put("fechaFinal", fechaFinal);

		listaCantidadSer = (List<AutorizacionsSubServDto>)persistenciaSvc.createNamedQuery("presupuesto.consultarAutorizacionesViaIngresoSol", params);
		listaCantidadOrd = (List<AutorizacionsSubServDto>)persistenciaSvc.createNamedQuery("presupuesto.consultarAutorizacionesViaIngresoOrd", params);
		
		for (AutorizacionsSubServDto aut: listaCantidadOrd)
		{
			int repetido=0;
			for (AutorizacionsSubServDto aut1: listaCantidadSer)
			{
				if((aut.getConsecutivo()==aut1.getConsecutivo())&&(aut.getCodigo()==aut1.getCodigo()))
				{repetido=1;}
				
			}
			if(repetido==0)
			{
		    listaCantidadSer.add(aut);
			}
		}
		
		return listaCantidadSer;
	}

	
}
